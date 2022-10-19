package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.AnimationData;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class Koopa extends Enemy implements IDrawable, ISimpleUpdateable {
	//int width, height;
	final static int NORMAL_WIDTH = 50;
	final static int NORMAL_HEIGHT = 50;
	public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
	private int diameter = NORMAL_WIDTH;
    public static final String CLASS_NAME = "Koopa";
	private final String objectToDisplayName = "Turtle";
    private final float NORMAL_ACCELERATE = 7f;
	public final static int NORMAL_LIFE = 100;
	static int normalMovementImpulseX = 5; 
	static float maxNormalSpeed = 0.5f;
	private float headRadius;
	private final static float additionalGraphicScale  = 1.65f;
	
	public Koopa(PVector position, byte level) {
		init(position,level, NORMAL_LIFE, NORMAL_DIAMETER);
	}

	public Koopa(PVector position, byte level, int life, int diameter) {
		init(position,level, life, diameter);
	}

	public Koopa(PVector position, byte level, int life) {
		init(position,level, life, NORMAL_DIAMETER);
	}

	public Koopa(Koopa objectTemplate) {
		init(objectTemplate.getPixelPosition(), objectTemplate.level, objectTemplate.life, objectTemplate.getDiameter());
	}

	private void init(PVector position, byte level, int life, int diameter) {
		this.diameter = (int)diameter;
		role = ENEMY;
		boundingWidth = diameter;
		boundingHeight = diameter;
		headRadius = boundingWidth/3.1f;
		makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
		maxLife = (int)life;
		setLife((int)life, (int)life);
		this.level = level;
		if (Program.debug) System.out.println("Koopa was uploaded");
		body.setFixedRotation(true);
		if (level == Koopa.FLYING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.FLYING_ALONG_SINUSOID_PATH, this);
		else if (level == Koopa.JUMPING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_AND_REGULARLY_JUMP, this);
		else if (level == Koopa.GOING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
		else globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
		findPlayerAtStart();
		//setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}

	private void updateAnimationData(byte level, byte statement){
		if (level == Person.GOING){
			if (statement != IN_AIR) {
				enemiesAnimationController.updateAnimationData(AnimationData.GO);
			}
			else {
				enemiesAnimationController.updateAnimationData(AnimationData.FLY);
			}
		}
		else if (level == Person.JUMPING){
			if (statement != IN_AIR) {
				enemiesAnimationController.updateAnimationData(AnimationData.GO);
			}
			else {
				enemiesAnimationController.updateAnimationData(AnimationData.FLY);
			}
		}
		else if (level == Person.FLYING){
			enemiesAnimationController.updateAnimationData(AnimationData.FLY);
		}

	}

	int getDiameter(){
		return diameter;
	}

	private void setAnimationForLevel(byte level, SpriteAnimation spriteAnimation){

		/*
		if (level == Person.GOING){
			if (statement != IN_AIR) {
				//System.out.println("Set value for animation" + level);
				spriteAnimation.setLastSprite((int) 2);
				spriteAnimation.setStartSprite((int) 1);
				//System.out.println("Succesfully" + level);
			}
			else {
				spriteAnimation.setLastSprite((int) 4);
				spriteAnimation.setStartSprite((int) 3);
				System.out.println("Flying anination" + level);
			}
		}
		else if (level == Person.JUMPING){
			spriteAnimation.setLastSprite((int) 2);
			spriteAnimation.setStartSprite((int) 1);
		}
		else if (level == Person.FLYING){
			spriteAnimation.setLastSprite((int) 4);
			spriteAnimation.setStartSprite((int) 3);
		}
		personAnimationController.resetAnimation(PersonAnimationController.GO);
		*/
	}

	private void setAnimationForStatement(byte newStatement, SpriteAnimation spriteAnimation){
		/*

		if (newStatement == Person.IN_AIR){
			//System.out.println("Set value for animation" + newStatement);
			spriteAnimation.setLastSprite((int) 4);
			spriteAnimation.setStartSprite((int) 3);

			System.out.println("IN AIR" + newStatement);
		}
		else if (newStatement == Person.ON_GROUND || newStatement == Person.ON_MOVEABLE_PLATFORM){
			spriteAnimation.setLastSprite((int) 2);
			spriteAnimation.setStartSprite((int) 1);
			System.out.println("ON GROUND" + newStatement);
		}
		personAnimationController.resetAnimation(PersonAnimationController.GO);
		*/
	}

	@Override
	public void loadAnimationData(MainGraphicController mainGraphicController){
		super.loadAnimationData(mainGraphicController);
		AnimationData animationDataForGoing = new AnimationData(AnimationData.GO, (byte) 0,  (byte) 1, (int) (33f/4f));
		AnimationData animationDataForFlying = new AnimationData(AnimationData.FLY, (byte) 2,  (byte) 3, (int) (33f/4f));
		enemiesAnimationController.addNewAnimationData(animationDataForGoing);
		enemiesAnimationController.addNewAnimationData(animationDataForFlying);
		SpriteAnimation spriteAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Koopa" + TextureDecodeManager.getExtensionForSpriteGraphicFile()), (int)0, (int)0, (int)153, (int)23, (int) (boundingWidth*additionalGraphicScale*0.95f), (int) (boundingHeight*additionalGraphicScale*1f), (byte)1, (byte)9, (int) (33f/4f));

		//SpriteAnimation spriteAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Koopa.gif"), (int)0, (int)0, (int)153, (int)23, (int) (boundingWidth*additionalGraphicScale*0.95f), (int) (boundingHeight*additionalGraphicScale*1f), (byte)1, (byte)9, (int) (33f/4f));
		enemiesAnimationController.addNewAnimation(spriteAnimation, EnemiesAnimationController.GO);
		enemiesAnimationController.loadSprites(mainGraphicController);
		enemiesAnimationController.setDeadSprite(EnemiesAnimationController.GO, (int)6);
		enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(9,-11));
		updateAnimationData(level, statement);
	}

	private void setSpritesNumberForActualStatement(){

	}

    private void makeBody(Vec2 center, float width, float height) {
		BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    body = PhysicGameWorld.controller.createBody(bd);
	    
	    PolygonShape corpus = new PolygonShape();	    
	    Vec2[] vertices = new Vec2[6];
	    vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
	    vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2));
	    vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
	    vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
	    vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
	    vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
	    corpus.set(vertices, vertices.length);	    
	    
	    CircleShape head = new CircleShape();
		head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(headRadius);	    
		Vec2 offset = new Vec2((float) (vertices[0].x*0.92), (float) (vertices[1].y*(-0.92)));
	    //offset = PhysicGameWorld.controller.vectorPixelsToWorld(offset);
	    head.m_p.set(offset.x,offset.y);
	    if (center == null) System.out.println("Position is null");
	    body.createFixture(corpus,1.4f);
	    body.createFixture(head, 0.1f);
	    moovingDirectionIsChanging = true;
	    mirrorBody();
	    System.out.println("Koopa body was succesfully made");
	    //body.setFixedRotation(false);
	    body.setFixedRotation(true);
	    body.setAngularDamping(2);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}
	
	private void makeOnlyCorpus(Vec2 center, float width, float height) {	
		BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;	    
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    body = PhysicGameWorld.controller.createBody(bd);	    
	    PolygonShape corpus = new PolygonShape();	    
	    Vec2[] vertices = new Vec2[6];
	    vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
	    vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2));
	    vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
	    vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
	    vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
	    vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
	    corpus.set(vertices, vertices.length); 
	    body.createFixture(corpus, 0.1f);	    
	    moovingDirectionIsChanging = true;
	    mirrorBody();	    
	    //body.setFixedRotation(false);
		body.setFixedRotation(true);
	    body.setAngularDamping(0);
	    if (Program.debug) System.out.println("Koopa's korpus was succesfully built");
	    setFilterDataForCategory(CollisionFilterCreator.CATEGORY_CORPSE);
	}
	
	@Override
	public void update(GameRound gameRound) {
		//System.out.println("Statement: " + statement + "; Level: " + level);
		super.update();
		globalAIController.update(gameRound);
		updateStatement();
		if (previousStatement != statement) {
			updateAnimationData(level, statement);
		}
		if (level!= SITTING && level != WITHOUT_LEVEL_CHANGING ) mirrorBody();
		if (level == SITTING) {
			if (body.isFixedRotation()) {

			}
		}
		if (!dead) {
			if (Program.engine.frameCount % GameRound. UPDATE_FREQUENCY_FOR_SECONDARY_OBJECTS == 1) updateAngle();
		}
	}
	
	@Override
	protected void updateAngle() {
	}
	
	private void mirrorBody() {
		if (moovingDirectionIsChanging) {
			if (body.getLinearVelocity().x>0.1f) {
				Fixture f = body.getFixtureList();	    	
			    CircleShape head = (CircleShape) f.getShape();
			    head.m_p.x=Math.abs(head.m_p.x);
			    sightDirection = TO_RIGHT;
			}
			else if (body.getLinearVelocity().x<(-0.1f)) {
				Fixture f = body.getFixtureList();	    	
			    CircleShape head = (CircleShape) f.getShape();
			    head.m_p.x=-1*Math.abs(head.m_p.x);
			    sightDirection = TO_LEFT;
			}
		}
	}
	
	
	boolean isSpeedMax() {
		if (body.getLinearVelocity().x >= maxVelocityAlongX) {
			return true;
		}
		else return false;
	}
	
	@Override
    public void simplifyBody() {
		if (level == SITTING) {			
			Vec2 pos = new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
			Vec2 velocity = body.getLinearVelocity();
			PhysicGameWorld.controller.destroyBody(body);
			makeOnlyCorpus(new Vec2(pos.x, pos.y), boundingWidth, boundingHeight);
			body.setLinearVelocity(velocity);
		}
	}
	
	@Override
	protected void remakeBody() {
		Vec2 pos = new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
		PhysicGameWorld.controller.destroyBody(body);			
		makeBody(new Vec2(pos.x, pos.y), boundingWidth, boundingHeight);
	}

	
	@Override
	public void draw(GameCamera gameCamera) {
		//System.out.println("Body fixed: " + body.isFixedRotation());
		//if (isObjectCenterInVisibleZone(gameCamera)) {
			Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		if (isVisibleOnScreen(gameCamera, pos)) {
			float a = body.getAngle();
			if (Program.debug) {
				//Programm.objectsFrame.beginDraw();
				Program.objectsFrame.rectMode(PApplet.CENTER);
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
				Program.objectsFrame.rotate(-a);
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.noFill();
				Program.objectsFrame.stroke(120, 0, 0);
				Program.objectsFrame.strokeWeight(2.5f);
				Fixture f = body.getFixtureList();
				//System.out.println("Got fixtures: " + (PolygonShape) f.getNext().getShape());
				PolygonShape ps;
				if (level != SITTING && level != WITHOUT_LEVEL_CHANGING) {
					try {
						CircleShape head = (CircleShape) f.getShape();
						Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).y, headRadius * 2, headRadius * 2);
						ps = (PolygonShape) f.getNext().getShape();
					} catch (Exception e) {
						ps = null;// TODO: handle exception
						System.out.println("Koopas body has no head");
						System.out.println(e);
					}

				} else {
					try {
						ps = (PolygonShape) f.getShape();
					} catch (Exception e) {
						ps = null;
					}

				}
				if (isAttacked && isDead()) {
					Program.objectsFrame.strokeWeight(0.5f);
					Program.objectsFrame.line(0, 0, NORMAL_WIDTH / 6, NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, NORMAL_WIDTH / 6, -NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, -NORMAL_WIDTH / 6, NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, -NORMAL_WIDTH / 6, -NORMAL_HEIGHT / 6);
				}
				Program.objectsFrame.strokeWeight(2.8f);
				Program.objectsFrame.beginShape();
				if (ps != null) {
					for (int i = 0; i < ps.getVertexCount(); i++) {
						Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
						Program.objectsFrame.vertex(v.x, v.y);
					}
				}
				Program.objectsFrame.endShape(PApplet.CLOSE);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
			}
			//}
			drawGraphic(gameCamera, pos, a);
		}
	}
	
	@Override
	public void awake() {
		if (level == SITTING) {
			level = GOING;
			life = maxLife;
			remakeBody();
		}
	}


	
	@Override
	public void kill() {
		if (mustBeLevelChanged()) {
			lowerLevel();
			notMoreAttacked();
		}
		else if (life <= 0){
			level = WITHOUT_LEVEL_CHANGING;
			dead = true;
			body.setGravityScale(0.8f);
			body.getFixtureList().setDensity(body.getFixtureList().getDensity()/6);
			if (body.isFixedRotation()) body.setFixedRotation(false);
			setFilterDataForCategory(CollisionFilterCreator.CATEGORY_CORPSE);
		}
	}
	
	private void lowerLevel() {
		if (level != WITHOUT_LEVEL_CHANGING) {
			level--;
			simplifyBody();
			if(level != WITHOUT_LEVEL_CHANGING) life = maxLife;
			//setAnimationForLevel(level, personAnimationController.getSpriteAnimation(PersonAnimationController.GO));
			//setAnimationForStatement(statement, personAnimationController.getSpriteAnimation(PersonAnimationController.GO));
			updateAnimationData(level, statement);
		}		
	}

	private void drawGraphic(GameCamera gameCamera, Vec2 pos, float a){
		if (!dead && level != Person.SITTING) {
			tintUpdatingBySelecting();
			if (sightDirection) enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, false);
			else enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, true);
		}
		else if (dead || level == Person.SITTING) {
			if (sightDirection) enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, false);
			else enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, true);
		}
	}
	
	
	private boolean mustBeLevelChanged() {
		if (this.getClass() == Koopa.class) {
			if (level == FLYING || level == GOING || level == JUMPING) {
				if (life <= 0) return true;
				else return false;
			}
			else return false;
		}
		else return false;
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStringData(){
		String data = new String();
		data = data+CLASS_NAME;
		data+= " 1";
		data+= LoadingMaster.MAIN_DATA_START_CHAR;
		data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).x;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).y;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= (int)level;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= life;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= diameter;
		System.out.println("Data for Koopa " + data);
		return data;
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

	@Override
	public int getPersonWidth(){
		return NORMAL_WIDTH;
	}

	@Override
	public void setGraphicDimensionFromEditor(int newDiameter){
		boundingWidth = newDiameter;
		boundingHeight = newDiameter*9f/8f;
		enemiesAnimationController.setElementWidth((int)(newDiameter*additionalGraphicScale));
		enemiesAnimationController.setElementHeight((int)(newDiameter*additionalGraphicScale*9f/8f));
		diameter = (int)newDiameter;
	}

	@Override
	public boolean attackByDirectContact(Person nearestPerson) {
		return true;
	}

	@Override
	public int getAttackValue() {
		return 40;
	}

	@Override
	public void setFixedRotation(){
		int angular = (int)body.getAngularVelocity();
		float angle = GameMechanics.convertRadiansToDegrees(body.getAngle());
		//System.out.println("Person angle: " + angle + "; Angular velocity: "  + (angular));
		float criticalAngularVelocityToStopBody = 5;
		float criticalAngleToStopBody = 5;

		if ((PApplet.abs(angular) < criticalAngularVelocityToStopBody && PApplet.abs(angle) < criticalAngleToStopBody) || level == FLYING || level == JUMPING || level == GOING) {
			Vec2 velocity = body.getLinearVelocity();
			//System.out.println("Rotation reset; angle was; " + angle );
			Vec2 pos = body.getPosition();
			pos.y+=0.1f;
			body.setTransform(body.getPosition(), 0f);
			body.setFixedRotation(true);
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(velocity));
			//System.out.println("Rotation reset");
		}
		else{
			//System.out.println("Level " + level);
			//System.out.println("Level " + level);
			//body.setAngularDamping();
			//System.out.println("This body is rotating too fast");
		}
	}

}
