package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.RayCastingForExplosion;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

//public class Explosion implements IDrawable, IUpdateable, ISpriteAnimationDrawable{
public class Explosion implements IDrawable, ISimpleUpdateable {
	private PVector position;
	private static SpriteAnimation explosionAnimation;
	private static int adjustingYValue = -7;

	private final static int leftX = 132;
	private final static int graphicWidth = 256;
	private final static int graphicHeight = 280;
	private final static ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(leftX, 512-graphicHeight, leftX+graphicWidth, 512);
	//private final static ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(128+adjustingValue, 256, 128+228+adjustingValue, 512);
	//
	private final static float NORMAL_DIMENSION_COEF_FOR_GRAPHIC = 2.8f;
	private static boolean graphicLoaded;

	// Damage distance radiuses
	public final static int BIG_DAMAGE_DISTANCE = 650;
	public final static int NORMAL_DAMAGE_DISTANCE = (int)(BIG_DAMAGE_DISTANCE/1.65f);
	public final static int SMALL_DAMAGE_DISTANCE = (int)(NORMAL_DAMAGE_DISTANCE/1.65f);
	private int maxDistance = NORMAL_DAMAGE_DISTANCE;
	
	// Damage value
	public final static int BIG_DAMAGE = 500;
	public final static int NORMAL_DAMAGE = BIG_DAMAGE/2;
	public final static int SMALL_DAMAGE = NORMAL_DAMAGE/3;
	private int maxDamage = SMALL_DAMAGE;
	
	// Impuls value
	public final static int BIG_IMPULSE = (int) (50*1.5);
	public final static int NORMAL_IMPULSE = (int)(BIG_IMPULSE/1.4f);
	public final static int SMALL_IMPULSE = (int) (NORMAL_IMPULSE/1.4f);
	private int maxImpuls = SMALL_IMPULSE;
	
	private long startTime;
	public static int EXPLOSION_TIME=1500;
	private boolean ended = false;
	
	final static int resolution = 30;	//Rays per rotation
	ArrayList<RayCastingForExplosion> rayCastings = new ArrayList<RayCastingForExplosion>();
	ArrayList<PVector> collidingPlaces = new ArrayList<PVector>();
	
	//RayCastCallback callback;
	//Vec2 collisionPoint;
	
	public Explosion(GameRound gameRound, Vec2 position, LaunchableWhizbang source) {
		loadGraphic(gameRound);
		if (source.getClass() == BulletBill.class) {
			maxDistance = NORMAL_DAMAGE_DISTANCE;
			maxDamage = NORMAL_DAMAGE;
			maxImpuls = NORMAL_IMPULSE;
		}
		else if (source.getClass() == LaunchableGrenade.class) {
			maxDistance = SMALL_DAMAGE_DISTANCE;
			maxDamage = SMALL_DAMAGE;
			maxImpuls = SMALL_IMPULSE;
		}
		else if (source.getClass() == HandGrenade.class) {
			maxDistance = SMALL_DAMAGE_DISTANCE;
			maxDamage = SMALL_DAMAGE;
			maxImpuls = SMALL_IMPULSE;
		}
		else {
			maxDistance = SMALL_DAMAGE_DISTANCE;
			maxDamage = SMALL_DAMAGE;
			maxImpuls = SMALL_IMPULSE;
		}
		this.position = new PVector(position.x, position.y);

		startTime = Program.engine.millis();
		createRaysList();
		//System.out.println("Distance: " + maxDistance);
		explosionAnimation.setWidth((int)(NORMAL_DIMENSION_COEF_FOR_GRAPHIC*maxDistance));
		explosionAnimation.setHeight((int)(NORMAL_DIMENSION_COEF_FOR_GRAPHIC*maxDistance));
		System.out.println("Explosion width: " + (int)(NORMAL_DIMENSION_COEF_FOR_GRAPHIC*maxDistance));
		IndependentOnScreenAnimation independentOnScreenAnimation = new IndependentOnScreenAnimation(explosionAnimation, position, 0f);
		independentOnScreenAnimation.setShowOnce(true);
		gameRound.independentOnScreenAnimations.add(independentOnScreenAnimation);
		if (Program.debug && Program.OS == Program.DESKTOP){
			DebugGraphic graphic = new DebugGraphic(DebugGraphic.CROSS, position);
			gameRound.addDebugGraphic(graphic);
		}

	}

	public static void loadGraphic(GameRound gameRound){
		if (!graphicLoaded) {
			byte collumnsInSpritesheet = 8;
			byte rowsInSpritesheet = 10;
			int updateFrequency = SpriteAnimation.NORMAL_ANIMATION_UPDATING_FREQUENCY;
			explosionAnimation = new SpriteAnimation(HeadsUpDisplay.mainGraphicSource.getPath(), (int) imageZoneSimpleData.leftX, (int)imageZoneSimpleData.upperY, (int) imageZoneSimpleData.rightX, (int) imageZoneSimpleData.lowerY, (int)(NORMAL_DAMAGE_DISTANCE*12) , (int) (NORMAL_DAMAGE_DISTANCE*12), rowsInSpritesheet, collumnsInSpritesheet, updateFrequency);
			explosionAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(explosionAnimation.getPath()));
			//explosionAnimation.loadAnimation(HeadsUpDisplay.mainGraphicTileset);
			System.out.println("Graphic for explosion is loaded");
			EXPLOSION_TIME = collumnsInSpritesheet*rowsInSpritesheet*updateFrequency;
			System.out.println("New explosion; Time: " + EXPLOSION_TIME);
			//graphicLoaded = true;
		}
	}

	void createRaysList() {
		for (int i = 0; i < resolution; i++) {
			System.out.println("Ray length = " + maxDistance);
			rayCastings.add(new RayCastingForExplosion(position, (360f/resolution)*i, maxDistance, (byte) 1));
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

	/*
	private float getRelativeDistanceFromStart(PVector actualPos) {	// from 0 to 100 %
		final float actualRayLength = Programm.engine.dist(startPos.x, startPos.y, actualPos.x, actualPos.y);
		int procentValue = (int)(100f*actualRayLength/rayLength);
		System.out.println("Ray length: " + rayLength + "; Collision on length: " + actualRayLength + "; in %: " + procentValue);
		return procentValue;
	}*/


	public ArrayList<SingleColliding> getCollidings (GameRound gameRound) {
		ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
		for (int i = (rayCastings.size()-1); i >= 0; i--) {
			ArrayList<SingleColliding> rayCollidings = rayCastings.get(i).getSingleCollidings(gameRound);	// Fill with all coalisions on the way of single ray
			for (int j = 0; j < rayCollidings.size(); j++) {
				singleCollidings.add(rayCollidings.get(j));				
			}
		}
		for (int i = 0; i < singleCollidings.size(); i++) {
			PVector newPos = singleCollidings.get(i).getCollidingPlace();
			collidingPlaces.add(newPos);
		}
		//System.out.println("Collidings number: " + collidings);
		return singleCollidings;
	}
	
	
	


	@Override
	public void draw(GameCamera gameCamera) {
		if (ended == false && Program.debug) {
			//Programm.objectsFrame.beginDraw();
			Program.objectsFrame.pushMatrix();
			Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
			Program.objectsFrame.translate(position.x-gameCamera.getActualPosition().x- Program.objectsFrame.width/2, position.y-gameCamera.getActualPosition().y- Program.objectsFrame.height/2);
			//Programm.objectsFrame.translate(position.x-gameCamera.getActualPosition().x+Programm.objectsFrame.width/2, position.y-gameCamera.getActualPosition().y+ Programm.objectsFrame.height/2);
			//
			Program.objectsFrame.scale(getActualScale());
			Program.objectsFrame.pushStyle();
			Program.objectsFrame.stroke(250,0,0);
			Program.objectsFrame.noFill();
			Program.objectsFrame.strokeWeight(0.8f);
			//Programm.objectsFrame.line(0, 0, maxDistance, 0);
			Program.objectsFrame.ellipse(0,0,maxDistance*2,maxDistance*2);
			Program.objectsFrame.popStyle();
			Program.objectsFrame.popMatrix();
			//Programm.objectsFrame.endDraw();
		}
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	private float getActualScale() {
		//return 1-((startTime+EXPLOSION_TIME)-Game2D.engine.millis()/EXPLOSION_TIME);
		return (1- Program.engine.map((startTime+EXPLOSION_TIME)- Program.engine.millis(), EXPLOSION_TIME, 0, 1, 0));
	}

	public PVector getPosition() {
		return position;
	}
}
