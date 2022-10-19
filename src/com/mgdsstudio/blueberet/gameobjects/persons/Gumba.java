package com.mgdsstudio.blueberet.gameobjects.persons;



import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import com.mgdsstudio.texturepacker.TexturePacker;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class Gumba extends Enemy implements IDrawable, ISimpleUpdateable {
	public final static String CLASS_NAME = "Gumba";
	private final String objectToDisplayName = "Monster";
	private final static boolean gumbaAsDeathcap = true;
	private final static ImageZoneFullData imageZoneFullData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Gumba" + TextureDecodeManager.getExtensionForSpriteGraphicFile()) , (int)0, (int)0, (int)128, (int)18);
	//public final static ImageZoneFullData imageZoneFullData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Gumbas spritesheet.gif") , (int)1, (int)1, (int)489, (int)528);
	private final static float additionalGraphicScale  = 1.4f;
	//int width, height;

	public final static int NORMAL_WIDTH = 50;
	final static int NORMAL_HEIGHT = 50;
	public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
	private int diameter = NORMAL_WIDTH;
	private final float NORMAL_ACCELERATE = 555f;
	public static int NORMAL_LIFE = 200;
	static int normalMovementImpulseX = 5; 
	static float maxNormalSpeed = 0.5f;

	public Gumba(Gumba template){
		init(template.getPixelPosition(), template.getLife(), template.getDiameter());
	}

	public Gumba(PVector position) {
		init(position, 1, diameter );
	}

	public Gumba(PVector position, int life) {
		init(position, life, diameter );
	}

	public Gumba(PVector position, int life, int diameter) {
		init(position, life, diameter);
	}

	private void init(PVector position, int life, int diameter){
		this.diameter = (int)diameter;
		role = ENEMY;
		boundingWidth = diameter;
		boundingHeight = diameter;
		makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
		if (life<= 0) {
			maxLife = NORMAL_LIFE;
			setLife(NORMAL_LIFE, NORMAL_LIFE);
		}
		else {
			maxLife = (int) life;
			setLife((int) life, (int) life);
		}
		if (Program.debug) System.out.println("Gumba was uploaded");
		level = GOING;
		try {
			body.setFixedRotation(true);
		}
		catch (Exception e){
			System.out.println("Can not set fixed rotation for Gumba");
			e.printStackTrace();
			//System.out.println(e);
		}
		actualAccelerate = NORMAL_ACCELERATE;
		globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
		jumpStartSpeed = jumpStartSpeed/1.2f;
		findPlayerAtStart();
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}

	//public void getA

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

	@Override
	public void loadAnimationData(MainGraphicController mainGraphicController){
		super.loadAnimationData(mainGraphicController);
		SpriteAnimation spriteAnimation = null;
		if (gumbaAsDeathcap){
			spriteAnimation = new SpriteAnimation(imageZoneFullData.getName(), (int) imageZoneFullData.leftX, (int) imageZoneFullData.upperY, (int)imageZoneFullData.rightX, (int) imageZoneFullData.lowerY, (int) (boundingWidth*additionalGraphicScale), (int) (boundingHeight*additionalGraphicScale), (byte)1, (byte)8, (int) 16);

		}
		else spriteAnimation = new SpriteAnimation(imageZoneFullData.getName(), (int) imageZoneFullData.leftX, (int) imageZoneFullData.upperY, (int)imageZoneFullData.rightX, (int) imageZoneFullData.lowerY, (int) (boundingWidth*additionalGraphicScale), (int) (boundingHeight*additionalGraphicScale), (byte)6, (byte)7, (int) 16);

		spriteAnimation.setLastSprite((int)4);
		enemiesAnimationController.addNewAnimation(spriteAnimation, EnemiesAnimationController.GO);
		enemiesAnimationController.loadSprites(mainGraphicController);
		enemiesAnimationController.setDeadSprite(EnemiesAnimationController.GO, (int)7);
		enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(0,0));
	}
	
	private void makeBody(Vec2 center, float width, float height) {
	    PolygonShape sd = new PolygonShape();	    
	    Vec2[] vertices = new Vec2[6];
	    vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
	    vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2));
	    vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
	    vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
	    vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
	    vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
	    sd.set(vertices, vertices.length);
	    
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    body = PhysicGameWorld.controller.createBody(bd);
	    
	    body.createFixture(sd, 1.0f);
	    body.getFixtureList().setDensity(1);
		body.getFixtureList().setFriction(0.8f);
	}


	
		@Override
	public void draw(GameCamera gameCamera) {
		//System.out.println("Gumbas life " + maxLife);
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
			if (isVisibleOnScreen(gameCamera, pos)) {
		float a = body.getAngle();
		if (body != null) {
			if (Program.debug) {
				Fixture f = body.getFixtureList();
				//if (f!= null) {
				PolygonShape ps = (PolygonShape) f.getShape();
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
				if (isDead()) drawCross();
				Program.objectsFrame.strokeWeight(0.8f);
				Program.objectsFrame.beginShape();
				Program.objectsFrame.strokeWeight(2.5f);
				for (int i = 0; i < ps.getVertexCount(); i++) {
					Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
					Program.objectsFrame.vertex(v.x, v.y);
				}
				Program.objectsFrame.endShape(PApplet.CLOSE);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
				//Programm.objectsFrame.endDraw();

			}
			//System.out.println(" Dir " + body.getLinearVelocity().x);
			if (!dead) {
				if (statement == IN_AIR) {
					//System.out.println("enemiesAnimationController is null: " + (enemiesAnimationController == null));
					//System.out.println("enemiesAnimationController.getSpriteAnimation: " + (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO) == null));

					if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.ACTIVE) {
						enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.PAUSED);
						//System.out.println("Gumba in Air");
					}
				} else if (statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) {
					if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.PAUSED) {
						enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.ACTIVE);
						//System.out.println("Gumba on platform");
					}
				}
				tintUpdatingBySelecting();
				if (body.getLinearVelocity().x < 0)
					enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, false);
				else enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, true);
			} else {
				//System.out.println("is dead");
				enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, true);
			}
		}
		}
        //System.out.println("life: " + life);
	}


		
		private void drawCross() {
			Program.engine.strokeWeight(2);
		    Program.engine.line(0, 0, NORMAL_WIDTH/6, NORMAL_HEIGHT/6);
		    Program.engine.line(0, 0, NORMAL_WIDTH/6, -NORMAL_HEIGHT/6);
		    Program.engine.line(0, 0, -NORMAL_WIDTH/6, NORMAL_HEIGHT/6);
		    Program.engine.line(0, 0, -NORMAL_WIDTH/6, -NORMAL_HEIGHT/6);
		}


		@Override
		public void update(GameRound gameRound) {
			super.update();
			globalAIController.update(gameRound);
			if (!dead) {
				if (Program.engine.frameCount % GameRound. UPDATE_FREQUENCY_FOR_SECONDARY_OBJECTS == 1) updateAngle();
			}
			updateStatement();
		}
			

		@Override
		protected void updateAngle() {
			if (!rolledOver) {
				if (Program.engine.degrees((Program.engine.abs(body.getAngle()))) > 12) {
					if (body.isFixedRotation()) body.setFixedRotation(false);
					rolledOver = true;
					System.out.println("!!! It is rolled over " + this.getClass() + "; Angle: " + Program.engine.degrees(body.getAngle()));
					statement = WITHOUT_LEVEL_CHANGING;	//To test
					globalAIController.changeBehaviourModel(GlobalAI_Controller.SITTING_AND_WAITING);
					
				}
			}
			else {
				if (Program.engine.degrees((Program.engine.abs(body.getAngle()))) < 9 && Program.engine.degrees((Program.engine.abs(body.getAngle()))) > 1) {
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
		}
		
		boolean isSpeedMax() {
			if (body.getLinearVelocity().x >= maxNormalSpeed) {
				return true;
			}
			else return false;
		}





		
		
		/*
		public boolean isBlockedAlongX() {
			System.out.println("Gumba's speed is " + body.getLinearVelocity().x);
			if (body.getLinearVelocity().x < Math.abs(MAX_SPEED_BY_BLOCKED_STATEMENT)) {
				return true;
			}
			else return false;
		}
		*/

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
		data+= 2;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= life;
		data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
		data+= diameter;
		System.out.println("data for Gumba " + data);
		return data;
	}



	int getDiameter(){
		return diameter;
	}

	@Override
	public int getPersonWidth(){
		return NORMAL_WIDTH;
	}


	@Override
	public void setGraphicDimensionFromEditor(int newDiameter){
		enemiesAnimationController.setNewDimention((int)(newDiameter*additionalGraphicScale));
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
}
