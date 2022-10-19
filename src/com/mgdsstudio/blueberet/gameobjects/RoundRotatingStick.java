package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.RoundRotatingStickGraphicController;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class RoundRotatingStick extends RoundElement implements ISimpleUpdateable, IDrawable{

	private final String objectToDisplayName = "Rotating stick";
	RoundRotatingStickGraphicController roundRotatingStickGraphicController;
	private Body staticBody;
	//StaticSprite sprite;
	//private Vec2 axisPos;
	//RoundStaticBoxObject staticObject;	
	private RevoluteJoint joint;
	private PVector rotatingCenter;
	private float length;
	private float lengthFromCenter;
	
	//Thickness data
	public static final float NORMAL_THICKNESS = Program.engine.width/50;
	private float thickness = NORMAL_THICKNESS;
	
	//Rotating direction and velocity data
	public static final boolean CCW = true;
	public static final boolean CW = false;
	private float torque = 1000;
	private float speed = PConstants.PI;

	
	public RoundRotatingStick(Body staticBody, PVector rotatingCenter, float length, float thickness, float torque, float speed) {
		this.staticBody = staticBody;
		//staticBody.setType(BodyType.DYNAMIC);
		this.rotatingCenter = rotatingCenter;
		this.lengthFromCenter = lengthFromCenter;
		this.thickness = thickness;
		this.length = length;
		this.torque = torque;
		this.speed = speed;
		if (staticBody != null) {
			makeBody();
			makeRotating();
		}
		//setFilterDataF(body.getFixtureList().getShape(), COALISION_ROTATING_STICK_WITH_ROUND_ELEMENTS);
		//switchOffContactWithObjectsInArea();
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}

	/*
	private void switchOffContactWithObjectsInArea(){
		PhysicGameWorld.getBodiesInCircle(new Vec2(rotatingCenter.x, rotatingCenter.y), length);
	}*/

	/*
	private void setFilterData(Shape shape, byte group) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.getNext();
		fixture.getNext();
		fixture.setFilterData(filter);
	}*/


	public void loadImageData(){
		final float additionalGraphicScale  = 8f;
		float fireSpriteRadius = (thickness*additionalGraphicScale);
		sprite = new StaticSprite(Program.getAbsolutePathToAssetsFolder("Rotating fire.png"), (int)17, (int)0, (int)32, (int)16, (int) (thickness*additionalGraphicScale), (int) (thickness*additionalGraphicScale));
		float additionalScaleForController = 0.7f;
		roundRotatingStickGraphicController = new RoundRotatingStickGraphicController((int)length, (int)thickness, (int)(fireSpriteRadius*additionalScaleForController), 0f);

	}



	/*
	@Override
	public void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
	}
	*/


	/*
	@Override
	public StaticSprite getSprite(){
		return sprite;
	}
	*/
	
	private void makeBody() {			
	    PolygonShape sd = new PolygonShape();		    
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(length/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(thickness/2);
	    sd.setAsBox(box2dW, box2dH); 
	    FixtureDef fd = new FixtureDef();
	    fd.shape = sd;
	    fd.density = 0.7f;
	    fd.friction = 0.1f;
	    fd.restitution = 0.05f;
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(rotatingCenter.x, rotatingCenter.y)));
	    body = PhysicGameWorld.controller.createBody(bd);
	    body.createFixture(fd);	
	}
	
	private void makeRotating() {
		RevoluteJointDef rjd = new RevoluteJointDef();			
		Vec2 offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(length/2, 0f));
		try {
			rjd.initialize(body, staticBody, body.getWorldCenter().add(offset));
		}
		catch (Exception e) {
			System.out.println("Can not initialize rotating. body: " + body);		
		}	    
	    rjd.motorSpeed = speed;       // how fast?
	    rjd.maxMotorTorque = torque; // how powerful?
	    rjd.enableMotor = true;      // is it on?
	    joint = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd);
	    System.out.println("rotatingCenter: " + rotatingCenter + "; body center: " + PhysicGameWorld.controller.coordWorldToPixels(body.getPosition()));
	}
	
	
	
	void toggleMotor() {
	    joint.enableMotor(!joint.isMotorEnabled());
	  }

	boolean motorOn() {
	    return joint.isMotorEnabled();
	}

	@Override
	public void draw(GameCamera gameCamera) {
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		if (isVisibleOnScreen(gameCamera, pos)) {
			float a = body.getAngle();

			if (Program.debug) {
				//Programm.objectsFrame.beginDraw();
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
				Program.objectsFrame.rotate(-a);    //Why-?????
				Program.objectsFrame.noFill();
				Program.objectsFrame.rectMode(PConstants.CENTER);
				Program.objectsFrame.strokeWeight(0.8f);
				Program.objectsFrame.stroke(0);
				Program.objectsFrame.rect(0, 0, length, thickness);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
				//Programm.objectsFrame.endDraw();
			}
			if (sprite != null) {
				Vec2 jointAxisPos = new Vec2(rotatingCenter.x + length / 2, rotatingCenter.y);
				ArrayList<Vec2> firePositions = roundRotatingStickGraphicController.getFirePositions(jointAxisPos, -a);
				for (Vec2 firePosition : firePositions) {
					sprite.draw(gameCamera, firePosition, roundRotatingStickGraphicController.getActualAngle());
				}
			}
		}
	}


	@Override
	public void update() {
		if (sprite!= null) {
			roundRotatingStickGraphicController.update();
		}
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

}



/*
public class RoundRotatingStick extends GameObject implements IUpdateable, IDrawable{
	RoundRotatingStickGraphicController roundRotatingStickGraphicController;
	private Body staticBody;
	StaticSprite sprite;
	//private Vec2 axisPos;
	//RoundStaticBoxObject staticObject;
	private RevoluteJoint joint;
	private PVector rotatingCenter;
	private float length;
	private float lengthFromCenter;

	//Thickness data
	public static final float NORMAL_THICKNESS = Game2D.engine.width/50;
	private float thickness = NORMAL_THICKNESS;

	//Rotating direction and velocity data
	public static final boolean CCW = true;
	public static final boolean CW = false;
	private float torque = 1000;
	private float speed = PConstants.PI;


	public RoundRotatingStick(Body staticBody, PVector rotatingCenter, float length, float thickness, float torque, float speed) {
		this.staticBody = staticBody;
		//staticBody.setType(BodyType.DYNAMIC);
		this.rotatingCenter = rotatingCenter;
		this.lengthFromCenter = lengthFromCenter;
		this.thickness = thickness;
		this.length = length;
		this.torque = torque;
		this.speed = speed;
		if (staticBody != null) {
			makeBody();
			makeRotating();
		}

		setFilterData(body.getFixtureList().getShape(), COALISION_ROTATING_STICK_WITH_ROUND_ELEMENTS);
	}

	private void setFilterData(Shape shape, byte group) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.getNext();
		fixture.getNext();
		fixture.setFilterData(filter);
	}

	public void loadImageData(){
		final float additionalGraphicScale  = 8f;
		float fireSpriteRadius = (thickness*additionalGraphicScale);
		sprite = new StaticSprite("Rotating fire.png", (int)17, (int)1, (int)32, (int)15, (int) (thickness*additionalGraphicScale), (int) (thickness*additionalGraphicScale));
		float additionalScaleForController = 0.7f;
		roundRotatingStickGraphicController = new RoundRotatingStickGraphicController((int)length, (int)thickness, (int)(fireSpriteRadius*additionalScaleForController), 0f);
	}

	public void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
	}

	public StaticSprite getSprite(){
		return sprite;
	}

	private void makeBody() {
	    PolygonShape sd = new PolygonShape();
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(length/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(thickness/2);
	    sd.setAsBox(box2dW, box2dH);
	    FixtureDef fd = new FixtureDef();
	    fd.shape = sd;
	    fd.density = 0.7f;
	    fd.friction = 0.1f;
	    fd.restitution = 0.05f;
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(rotatingCenter.x, rotatingCenter.y)));
	    body = PhysicGameWorld.controller.createBody(bd);
	    body.createFixture(fd);
	}

	private void makeRotating() {
		RevoluteJointDef rjd = new RevoluteJointDef();
		Vec2 offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(length/2, 0f));
		try {
			rjd.initialize(body, staticBody, body.getWorldCenter().add(offset));
		}
		catch (Exception e) {
			System.out.println("Can not initialize rotating. body: " + body);
		}
	    rjd.motorSpeed = speed;       // how fast?
	    rjd.maxMotorTorque = torque; // how powerful?
	    rjd.enableMotor = true;      // is it on?
	    joint = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd);
	    System.out.println("rotatingCenter: " + rotatingCenter + "; body center: " + PhysicGameWorld.controller.coordWorldToPixels(body.getPosition()));
	}



	void toggleMotor() {
	    joint.enableMotor(!joint.isMotorEnabled());
	  }

	boolean motorOn() {
	    return joint.isMotorEnabled();
	}

	@Override
	public void draw(GameCamera gameCamera) {
		Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
	    float a = body.getAngle();
	    if (Game2D.DEBUG) {
			Game2D.objectsFrame.beginDraw();
			Game2D.objectsFrame.pushMatrix();
			Game2D.objectsFrame.pushStyle();
			Game2D.objectsFrame.translate(pos.x - gameCamera.getActualPosition().x + Game2D.objectsFrame.width / 2, pos.y - gameCamera.getActualPosition().y + Game2D.objectsFrame.height / 2);
			Game2D.objectsFrame.rotate(-a);    //Why-?????
			Game2D.objectsFrame.noFill();
			Game2D.objectsFrame.rectMode(PConstants.CENTER);
			Game2D.objectsFrame.strokeWeight(0.8f);
			Game2D.objectsFrame.stroke(0);
			Game2D.objectsFrame.rect(0, 0, length, thickness);
			Game2D.objectsFrame.popStyle();
			Game2D.objectsFrame.popMatrix();
			Game2D.objectsFrame.endDraw();
		}
		if (sprite!= null) {
			Vec2 jointAxisPos = new Vec2(rotatingCenter.x+length/2, rotatingCenter.y);
			ArrayList <Vec2> firePositions = roundRotatingStickGraphicController.getFirePositions(jointAxisPos , -a);
			for (Vec2 firePosition: firePositions) {
				sprite.draw(gameCamera, firePosition, roundRotatingStickGraphicController.getActualAngle());
			}
		}
	}

	@Override
	public void update() {
		if (sprite!= null) {
			roundRotatingStickGraphicController.update();
		}
	}

}
*/
