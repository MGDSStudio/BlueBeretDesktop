package com.mgdsstudio.blueberet.explosion;

import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.RayCastingForExplosion;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PVector;

import java.util.ArrayList;

//public class Explosion implements IDrawable, IUpdateable, ISpriteAnimationDrawable{
public class ExplosionNewDoesnontUsed implements IDrawable, ISimpleUpdateable {
	private PVector position;
	private static SpriteAnimation explosionAnimation;
	private final static int leftX = 132;
	private final static int graphicWidth = 256;
	private final static int graphicHeight = 280;
	private final static ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(leftX, 512-graphicHeight, leftX+graphicWidth, 512);
	private final static float NORMAL_DIMENSION_COEF_FOR_GRAPHIC = 12f;
	private static boolean graphicLoaded;

	// Damage distance
	public final static int BIG_DAMAGE_DISTANCE = 1300/4;
	public final static int NORMAL_DAMAGE_DISTANCE = BIG_DAMAGE_DISTANCE/2;
	public final static int SMALL_DAMAGE_DISTANCE = NORMAL_DAMAGE_DISTANCE/3;
	private int maxDistance = NORMAL_DAMAGE_DISTANCE;

	// Damage value
	public final static int BIG_DAMAGE = 800;
	public final static int NORMAL_DAMAGE = BIG_DAMAGE/2;
	public final static int SMALL_DAMAGE = NORMAL_DAMAGE/3;
	private int maxDamage = SMALL_DAMAGE;

	// Impuls value
	public final static int BIG_IMPULSE = (int) (550*1.5);
	public final static int NORMAL_IMPULSE = BIG_IMPULSE/2;
	public final static int SMALL_IMPULSE = (int) (NORMAL_IMPULSE/1.2);
	private int maxImpuls = SMALL_IMPULSE;

	private long startTime;
	public static int EXPLOSION_TIME;
	private boolean ended = false;

	final static float resolution = 90f;	//Rays per rotation
	ArrayList<RayCastingForExplosion> rayCastings = new ArrayList<RayCastingForExplosion>();
	ArrayList<PVector> collidingPlaces = new ArrayList<PVector>();

	private ArrayList <Body> collidedBodies;
	private ArrayList <SingleCollidingNew> singleCollidings;

	public ExplosionNewDoesnontUsed(GameRound gameRound, Vec2 position, LaunchableWhizbang source) {
		loadGraphic(gameRound);
		if (source != null) {
			if (source.getClass() == BulletBill.class) {
				maxDistance = NORMAL_DAMAGE_DISTANCE;
				maxDamage = NORMAL_DAMAGE;
				maxImpuls = NORMAL_IMPULSE;
			} else if (source.getClass() == LaunchableGrenade.class) {
				maxDistance = SMALL_DAMAGE_DISTANCE;
				maxDamage = SMALL_DAMAGE;
				maxImpuls = SMALL_IMPULSE;
			}
		}
		this.position = new PVector();
		this.position.x = position.x;
		this.position.y = position.y;
		startTime = Program.engine.millis();

		System.out.println("Distance: " + maxDistance);
		explosionAnimation.setWidth((int)(NORMAL_DIMENSION_COEF_FOR_GRAPHIC*maxDistance));
		explosionAnimation.setHeight((int)(NORMAL_DIMENSION_COEF_FOR_GRAPHIC*maxDistance));
		IndependentOnScreenAnimation independentOnScreenAnimation = new IndependentOnScreenAnimation(explosionAnimation, position, 0f);
		independentOnScreenAnimation.setShowOnce(true);
		gameRound.independentOnScreenAnimations.add(independentOnScreenAnimation);
		if (Program.debug && Program.OS == Program.DESKTOP){
			DebugGraphic graphic = new DebugGraphic(DebugGraphic.CROSS, position);
			gameRound.addDebugGraphic(graphic);
		}
		addRaysList();
	}

	public static void loadGraphic(GameRound gameRound){
		if (!graphicLoaded) {
			byte collumnsInSpritesheet = 8;
			byte rowsInSpritesheet = 10;
			int updateFrequency = SpriteAnimation.NORMAL_ANIMATION_UPDATING_FREQUENCY;
			explosionAnimation = new SpriteAnimation(HeadsUpDisplay.mainGraphicSource.getPath(), (int) imageZoneSimpleData.leftX, (int)imageZoneSimpleData.upperY, (int) imageZoneSimpleData.rightX, (int) imageZoneSimpleData.lowerY, (int)(NORMAL_DAMAGE_DISTANCE*12) , (int) (NORMAL_DAMAGE_DISTANCE*12), rowsInSpritesheet, collumnsInSpritesheet, updateFrequency);
			explosionAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(explosionAnimation.getPath()));
			System.out.println("Graphic for explosion is loaded");
			EXPLOSION_TIME = collumnsInSpritesheet*rowsInSpritesheet*updateFrequency;
			System.out.println("New explosion; Time: " + EXPLOSION_TIME);
			graphicLoaded = true;
		}
	}

	public void createDebugGraphic(GameRound gameRound){
		//if (singleCollidings == null) System.out.println("Array is null");
		if (singleCollidings != null) {
			for (SingleCollidingNew singleColliding : singleCollidings) {
				Vec2 arrowStart = new Vec2(position.x, position.y);
				Vec2 arrowEnd = new Vec2(singleColliding.getCollidingPlace().x, singleColliding.getCollidingPlace().y);
				DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.ARROW, arrowStart, arrowEnd);
				gameRound.addDebugGraphic(debugGraphic);
			}
		}
	}

	private void addRaysList() {
		for (int i = 0; i < resolution; i++) {
			float actualAngle = (360f/(resolution/i));
			SingleRay ray = new SingleRay(position, maxDistance, actualAngle);
			Body attackedNearestBody = ray.getNearestBody();
			if (attackedNearestBody != null){
				System.out.println("Nearest body was founded! " + i);
				if (collidedBodies == null){
					collidedBodies = new ArrayList<>();
				}
				boolean bodyAlreadyCollided = false;
				for (Body alreadyCollidedBody : collidedBodies){
					if (alreadyCollidedBody.equals(attackedNearestBody)){
						bodyAlreadyCollided = true;
					}
					if (bodyAlreadyCollided) break;
				}
				if (!bodyAlreadyCollided){
					collidedBodies.add(attackedNearestBody);
					PVector collidingPlace = new PVector(ray.getCollisionPlace().x, ray.getCollisionPlace().y);
					float distanceToAttackedObjectInPercent = ray.getRelativeDistanceFromStart();
					SingleCollidingNew singleColliding = new SingleCollidingNew(collidingPlace, attackedNearestBody, distanceToAttackedObjectInPercent, actualAngle);
					if (singleCollidings == null) {
						singleCollidings = new ArrayList<>();
						System.out.println("Array of the collidings is created");
					}
					singleCollidings.add(singleColliding);
				}

			}
			//System.out.println("Nearest body is not founded. Test: " + i);
		}
	}


	
	public int getMaxDistance() {
		return maxDistance;
	}
	
	public int getMaxDamage() {
		return maxDamage;
	}
	
	public int getMaxImpulse() {
		return maxImpuls;
	}
	
	public void update() {
		if ((startTime+EXPLOSION_TIME)< Program.engine.millis()) {
			if (ended == false) {
				ended = true;
				if (Program.debug) System.out.println("The explosion is ended");
			}
		}		
	}
	

	
	public ArrayList<SingleColliding> getCollidings (GameRound gameRound) {
		ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
		int collidings = 0;
		if (Program.debug) gameRound.addDebugGraphic(new DebugGraphic(DebugGraphic.ROUND, new Vec2(rayCastings.get(0).getStartPos().x, rayCastings.get(0).getStartPos().y), RayCastingForExplosion.EXPLOSION_TEST_RADIUS));
		for (int i = (rayCastings.size()-1); i >= 0; i--) {
			ArrayList<SingleColliding> rayCollidings = rayCastings.get(i).getSingleCollidings(gameRound);	// Fill with all coalisions on the way of single ray
			System.out.println("Ray casting " + i + " has " +rayCollidings.size()+ " collidings");
			collidings+=rayCollidings.size();
			for (int j = 0; j < rayCollidings.size(); j++) {
				singleCollidings.add(rayCollidings.get(j));				
			}
		}
		for (int i = 0; i < singleCollidings.size(); i++) {
			PVector newPos = singleCollidings.get(i).getCollidingPlace();
			collidingPlaces.add(newPos);
		}
		System.out.println("Collidings number: " + collidings);
		return singleCollidings;
	}
	
	
	


	@Override
	public void draw(GameCamera gameCamera) {
		if (ended == false) {
			Program.objectsFrame.beginDraw();
			Program.objectsFrame.pushMatrix();
			Program.objectsFrame.translate(position.x-gameCamera.getActualPosition().x+ Program.objectsFrame.width/2, position.y-gameCamera.getActualPosition().y+ Program.objectsFrame.height/2);
			Program.objectsFrame.scale(getActualScale());
			Program.objectsFrame.pushStyle();
			Program.objectsFrame.stroke(250,0,0);
			Program.objectsFrame.noFill();
			Program.objectsFrame.strokeWeight(0.8f);
			//Programm.objectsFrame.line(0, 0, maxDistance, 0);
			Program.objectsFrame.ellipse(0,0,maxDistance*2,maxDistance*2);
			Program.objectsFrame.popStyle();
			Program.objectsFrame.popMatrix();
			Program.objectsFrame.endDraw();
		}
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	private float getActualScale() {
		//return 1-((startTime+EXPLOSION_TIME)-Game2D.engine.millis()/EXPLOSION_TIME);
		return (1- Program.engine.map((startTime+EXPLOSION_TIME)- Program.engine.millis(), EXPLOSION_TIME, 0, 1, 0));
	}

}
