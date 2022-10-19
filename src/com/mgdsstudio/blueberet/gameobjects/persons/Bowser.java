package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
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
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.PApplet;
import processing.core.PVector;

public class Bowser extends Enemy implements IDrawable, ISimpleUpdateable {
	public static final String CLASS_NAME = "Bowser";
	private final String objectToDisplayName = "Dragon";
	private final static float additionalGraphicScale  = 1.55f;
	//public static final int NORMAL_LIFE = 600;
	//int width, height;
	final static int NORMAL_WIDTH = 80;
	final static int NORMAL_HEIGHT = 90;
	public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
	private int diameter = NORMAL_DIAMETER;
	private final float NORMAL_ACCELERATE = 7f;
	public final static int NORMAL_LIFE = 600;
	static int normalMovementImpulseX = 5; 
	static float maxNormalSpeed = 0.5f;
	private float headRadius;
	boolean targetObjectIsOnAnotherSide;
	private boolean bodyMustBeRecreatedByNextFrame;
	
	public Bowser(PVector position) {
		init(position, NORMAL_LIFE, NORMAL_DIAMETER);
	}

	private void init(PVector position, int life, int diameter){
		role = ENEMY;
		this.diameter = (int)diameter;
		boundingWidth = diameter;
		boundingHeight = diameter*9f/8f;
		headRadius = diameter/3.5f;
		makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight, true);
		mirrorBody();
		maxLife = (int)life;
		setLife((int)life, (int)life);
		level = PATROL;
		if (Program.debug) System.out.println("Bowser was initialize; Life: " + life);
		body.setFixedRotation(true);
		globalAIController = new GlobalAI_Controller(GlobalAI_Controller.BOWSER_AI, this);
		//setFilterData(COALISION_DRAGON_FIRE_WITH_BOWSER);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_DRAGON);
		System.out.println("THERE ARE NO COLLISION BETWEEN DRAGON AND FIRE");
	}

	public Bowser(PVector position, int life) {
		init(position, life, NORMAL_DIAMETER);
	}

	public Bowser(PVector position, int life, int diameter) {
		init(position, life, diameter);
	}

	public Bowser(Bowser objectTemplate) {
		init(objectTemplate.getPixelPosition(), objectTemplate.life, objectTemplate.getDiameter());
	}

	@Override
	public void loadAnimationData(MainGraphicController mainGraphicController){
		super.loadAnimationData(mainGraphicController);
		//SpriteAnimation spriteAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Bowser.gif")  , (int)0, (int)0, (int)231, (int)40, (int) (boundingWidth*additionalGraphicScale*1.00f), (int) (boundingHeight*additionalGraphicScale*1f), (byte)1, (byte)7, (int) 16);
		SpriteAnimation spriteAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Dragon"+ TextureDecodeManager.getExtensionForSpriteGraphicFile())  , (int)0, (int)0, (int)512+64, (int)64, (int) (boundingWidth*additionalGraphicScale*1.00f), (int) (boundingHeight*additionalGraphicScale*1f), (byte)1, (byte)9, (int) 16);
		//SpriteAnimation spriteAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Dragon.gif")  , (int)0, (int)0, (int)512+64, (int)64, (int) (boundingWidth*additionalGraphicScale*1.00f), (int) (boundingHeight*additionalGraphicScale*1f), (byte)1, (byte)9, (int) 16);

		spriteAnimation.setLastSprite(7);
		//spriteAnimation.setLastSprite((int)30);
		enemiesAnimationController.addNewAnimation(spriteAnimation, EnemiesAnimationController.GO);

		enemiesAnimationController.loadSprites(mainGraphicController);
		enemiesAnimationController.setDeadSprite(EnemiesAnimationController.GO, (int)8);
		float yShifting = 13f+((boundingWidth-65)/115f)*(33f-13f);
		System.out.println("Shifting for graphic " + yShifting);
		//enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(12,-13));
		enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(12,-yShifting));
//enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(12,-13));
	}
	
	private void makeBody(Vec2 center, float width, float height, boolean side) {
		BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    body = PhysicGameWorld.controller.createBody(bd);
	    PolygonShape corpus = new PolygonShape();
		float widthCoef = 0.35f;
		Vec2[] vertices = new Vec2[6];
		vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(- widthCoef*width, 0));
		vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-2*width/13), height/2));
		vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/13), height/2));
		vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2( widthCoef*width, 0));
		vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
		vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
		corpus.set(vertices, vertices.length);
	    CircleShape head = new CircleShape();
		head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(headRadius);	    
	    Vec2 offset = new Vec2((float) (vertices[0].x*1.72), (float) (vertices[1].y*(-0.40)));
	    head.m_p.set(offset.x,offset.y);
		body.createFixture(corpus,0.4f);
		body.createFixture(head, 1.1f);
	    moovingDirectionIsChanging = true;
		body.setFixedRotation(true);
		body.setAngularDamping(1);
		body.setGravityScale(1);
		body.setUserData("Bowser body");
		System.out.println("Bowser body was succesfully made");
	}

	private void createMainBody(){

	}

	private void makeBodyForSimpleBody(Vec2 center, float width, float height, boolean side) {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
		body = PhysicGameWorld.controller.createBody(bd);
		PolygonShape corpus = new PolygonShape();
		Vec2[] vertices;
		side = false;
		if (side){
			vertices = new Vec2[6];
			vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
			vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2));
			vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
			vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
			vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
			vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
			corpus.set(vertices, vertices.length);
		}
		else {
			vertices = new Vec2[5];
			vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
			vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
			vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
			vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
			vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
			corpus.set(vertices, vertices.length);
		}
		CircleShape head = new CircleShape();
		head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(headRadius);
		Vec2 offset = new Vec2((float) (vertices[0].x*0.92), (float) (vertices[1].y*(-0.92)));
		head.m_p.set(offset.x,offset.y);

		//PhysicGameWorld.controller.worl

		body.createFixture(corpus,0.4f);
		body.createFixture(head, 1.1f);

		moovingDirectionIsChanging = true;
		body.setFixedRotation(true);
		body.setAngularDamping(1);
		body.setGravityScale(1);
		body.setUserData("Bowser body");
		mirrorBody();	    System.out.println("Bowser body was succesfully made");
	}

	private void mirrorBody() {

		if (targetObjectIsOnAnotherSide) {
			//makeBody(new Vec2(getPixelPosition().x, getPixelPosition().y), boundingWidth, boundingHeight, sightDirection);
			System.out.println("Bowser body was mirrored");
			if (sightDirection == TO_RIGHT) {
				for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
					if (f.getType() == ShapeType.CIRCLE){
						CircleShape head = (CircleShape) f.getShape();
						head.m_p.x=Math.abs(head.m_p.x);
						targetObjectIsOnAnotherSide = false;
					}
					/*else {
						PolygonShape polygonShape = (PolygonShape) f.getShape();
						Vec2 point = polygonShape.getVertex(1);
						float relativeX  = point.x;
						point.x-=2*relativeX;
					}*/
				}
			}
			else {
				for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
					if (f.getType() == ShapeType.CIRCLE){
						CircleShape head = (CircleShape) f.getShape();
						head.m_p.x=-1*Math.abs(head.m_p.x);
						targetObjectIsOnAnotherSide = true;
					}
					/*else {
						PolygonShape polygonShape = (PolygonShape) f.getShape();
						Vec2 point = polygonShape.getVertex(1);
						float relativeX  = point.x;
						point.x+=2*relativeX;
					}*/
				}
			}
			setFilterDataForCategory(CollisionFilterCreator.CATEGORY_DRAGON);
		}
	}

	private void setFilterData(byte group, byte NOTHING) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.setFilterData(filter);
	}

	/*
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
	    body.setFixedRotation(false);
	    body.setAngularDamping(0);
	    if (Programm.DEBUG) System.out.println("Bowser's korpus was succesfully built");
	}*/
	
	@Override
	public void update(GameRound gameRound) {
		super.update(gameRound);
		updateStatement();
		globalAIController.update(gameRound);
		if (level == SITTING) {
			/*if (body.isFixedRotation()) {
				body.setFixedRotation(false);
			}*/
		}
		if (!dead) {
			if (Program.engine.frameCount % GameRound. UPDATE_FREQUENCY_FOR_SECONDARY_OBJECTS == 1) updateAngle();
		}
		/*
		else if (bodyMustBeRecreatedByNextFrame){
			System.out.println("Body has loosed his head");
			bodyMustBeRecreatedByNextFrame = false;
		}*/
	}

	@Override
    public void simplifyBody(){
		//Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
		//makeBody(pos, boundingWidth, boundingHeight, false);
		for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
			if (f.getType() == ShapeType.CIRCLE){

				CircleShape shape = (CircleShape) f.getShape();
				shape.m_radius=shape.m_radius/10f;
				f.m_density = (f.m_density*90);
				//body.destroyFixture(f);
				System.out.println("Head was deleted");
				break;
			}

		}
	}

	@Override
	protected void updateAngle() {
		/*
		if (!rolledOver) {
			if (Game2D.engine.degrees((Game2D.engine.abs(body.getAngle()))) > 12) {
				if (body.isFixedRotation()) body.setFixedRotation(false);
				rolledOver = true;
				System.out.println("!!! It is rolled over " + this.getClass() + "; Angle: " + Game2D.engine.degrees(body.getAngle()));				
				statement = WITHOUT_LEVEL_CHANGING;	//To test
				globalAI.changeBehaviourModel(GlobalAI.SITTING_AND_WAITING);
				
			}
		}
		else {
			if (Game2D.engine.degrees((Game2D.engine.abs(body.getAngle()))) < 9 && Game2D.engine.degrees((Game2D.engine.abs(body.getAngle()))) > 1) {				
				Vec2 pos = body.getPosition();
				pos.y-=PhysicGameWorld.controller.scalarPixelsToWorld(350/29);
				body.setTransform(pos, 0);
				body.setAngularVelocity(0);
				body.setLinearVelocity(new Vec2(0,0));
				body.setFixedRotation(true);
				rolledOver = false;
				System.out.println("Trying to reset my position");
			}
		}
		*/
	}
	


	private void mirrorBodyOnlyForHead() {
		if (targetObjectIsOnAnotherSide) {
			System.out.println("Bowser body was mirrored");
			if (sightDirection == TO_RIGHT) {
				for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
					if (f.getType() == ShapeType.CIRCLE){
						CircleShape head = (CircleShape) f.getShape();
						head.m_p.x=Math.abs(head.m_p.x);
						targetObjectIsOnAnotherSide = false;
					}
				}
			}
			else {
				for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
					if (f.getType() == ShapeType.CIRCLE){
						CircleShape head = (CircleShape) f.getShape();
						head.m_p.x=-1*Math.abs(head.m_p.x);
						targetObjectIsOnAnotherSide = true;
					}
				}
			}
		}


	}
	
	public void attack() {
		
	}
	
	
	boolean isSpeedMax() {
		if (body.getLinearVelocity().x >= maxVelocityAlongX) {
			return true;
		}
		else return false;
	}
	
	/*
	@Override
	protected void simplifyBody() {
		
		if (level == SITTING) {			
			Vec2 pos = new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
			Vec2 velocity = body.getLinearVelocity();
			//Fixture f = body.getFixtureList();
			//body.destroyFixture(f);	
			PhysicGameWorld.controller.destroyBody(body);
			makeOnlyCorpus(new Vec2(pos.x, pos.y), width, height);		
			body.setLinearVelocity(velocity);
		}
		
	}*/
	
	@Override
	protected void remakeBody() {
		Vec2 pos = new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
		PhysicGameWorld.controller.destroyBody(body);			
		makeBody(new Vec2(pos.x, pos.y), boundingWidth, boundingHeight, true);
	}

	
	@Override
	public void draw(GameCamera gameCamera) {
			Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		if (isVisibleOnScreen(gameCamera, pos)) {
			float a = body.getAngle();
			//if (!body.isFixedRotation()) System.out.println("This bowser is not more fixed");
			//else System.out.println("This bowser is fixed");
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

				//System.out.println("Got fixtures: " + (PolygonShape) f.getNext().getShape());


				for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
					if (f.getType() == ShapeType.CIRCLE) {
						CircleShape head = (CircleShape) f.getShape();
						Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).y, headRadius * 2, headRadius * 2);
					} else if (f.getType() == ShapeType.POLYGON) {
						PolygonShape ps = (PolygonShape) f.getShape();
						Program.objectsFrame.beginShape();
						if (ps != null) {
							for (int i = 0; i < ps.getVertexCount(); i++) {
								Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
								Program.objectsFrame.vertex(v.x, v.y);
							}
						}
						Program.objectsFrame.endShape(PApplet.CLOSE);
					}
				}

				/*
				Fixture f = body.getFixtureList();
				PolygonShape ps;
				try {
					CircleShape head = (CircleShape) f.getShape();
					Game2D.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).y, headRadius * 2, headRadius * 2);
					ps = (PolygonShape) f.getNext().getShape();
				}
				catch (Exception e) {
					ps = null;// TODO: handle exception
					System.out.println("Bowser body has no head");
					System.out.println(e);
				}
		    	*/
			    /*
			     {
			    	try {
			    		ps = (PolygonShape) f.getShape();
					} catch (Exception e) {
						ps = null;
					}
			    	
			    }
			     */
				if (isAttacked && isDead()) {
					Program.objectsFrame.strokeWeight(0.5f);
					Program.objectsFrame.line(0, 0, NORMAL_WIDTH / 6, NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, NORMAL_WIDTH / 6, -NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, -NORMAL_WIDTH / 6, NORMAL_HEIGHT / 6);
					Program.objectsFrame.line(0, 0, -NORMAL_WIDTH / 6, -NORMAL_HEIGHT / 6);
				}
				Program.objectsFrame.strokeWeight(2.8f);

				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();

			}

			drawGraphic(gameCamera, pos, a);
		}
	}

	private void drawGraphic(GameCamera gameCamera, Vec2 pos, float a){
		if (!dead) {
			tintUpdatingBySelecting();
			if (statement == IN_AIR){
				if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.ACTIVE){
					enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.PAUSED);
				}
			}
			else if (statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND){
				if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.PAUSED){
					enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.ACTIVE);
				}
			}
			if (sightDirection) enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, false);
			else enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, true);
		}
		else {
			if (sightDirection) enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, false);
			else enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, true);
		}
	}
	
	@Override
	public void awake() {
		
	}
	
	@Override
	public void kill() {
		if (life <= 0){
			level = WITHOUT_LEVEL_CHANGING;
			globalAIController.changeBehaviourModel(GlobalAI_Controller.CORPSE);
			dead = true;
			simplifyBody();
			body.setGravityScale(0.8f);
			body.getFixtureList().setDensity(body.getFixtureList().getDensity()/2);
			if (body.isFixedRotation()) body.setFixedRotation(false);
			setFilterDataForCategory(CollisionFilterCreator.CATEGORY_CORPSE);
			bodyMustBeRecreatedByNextFrame = true;

		}
	}
	
	/*private void lowerLevel() {
		
		if (level != WITHOUT_LEVEL_CHANGING) {
			level--;
			simplifyBody();
			if(level != WITHOUT_LEVEL_CHANGING) life = maxLife;	
		}
				
	}*/
	
	
	private boolean mustBeLevelChanged() {
		return false;
		/*
		if (this.getClass() == Bowser.class) {
			if (level == FLYING || level == GOING || level == JUMPING) {
				if (life <= 0) return true;
				else return false;
			}
			else return false;
		}
		else return false;
		*/
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	public static Vec2 getRelativeHeadPosition() {
		return new Vec2 ((float) ((NORMAL_WIDTH/2)*1.8), -(float)((NORMAL_WIDTH/2)));
	}

	@Override
	public void resetSightDirection() {
		if (sightDirection == TO_LEFT) sightDirection = TO_RIGHT;
		else sightDirection = TO_LEFT;
		targetObjectIsOnAnotherSide = true;
		mirrorBody();
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
		data+= life;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= diameter;
		System.out.println("Data for Bowser: " + data);
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

	int getDiameter(){
		return diameter;
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

			Vec2 velocity = body.getLinearVelocity();
			body.setTransform(body.getPosition(), 0f);
			body.setFixedRotation(true);
			body.setLinearVelocity(velocity);

		/*
		int angular = (int)body.getAngularVelocity();
		float angle = GameMechanics.convertRadiansToDegrees(body.getAngle());
		float criticalAngularVelocityToStopBody = 10;
		float criticalAngleToStopBody = 10;

		if ((PApplet.abs(angular) < criticalAngularVelocityToStopBody && PApplet.abs(angle) < criticalAngleToStopBody)) {
			Vec2 velocity = body.getLinearVelocity();
			body.setTransform(body.getPosition(), 0f);
			body.setFixedRotation(true);
			body.setLinearVelocity(velocity);
		}		*/
	}
}
