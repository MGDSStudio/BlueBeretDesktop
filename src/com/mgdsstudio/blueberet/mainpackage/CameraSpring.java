package com.mgdsstudio.blueberet.mainpackage;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

public class CameraSpring {
	MouseJoint mouseJoint;
	boolean relaxed = false;
	private final PVector actualPos = new PVector(0,0);
	private final Vec2 mutPos = new Vec2(0,0);
	//private final Vec2 mutPixelPos = new Vec2(0,0);
	/*public final static float NORMAL_FORCE = 150f;
	public final static float NORMAL_FREQUENCY = 3.5f;
	public final static  float NORMAL_DAMPING_RATIO = 0.3f;
	*/
		


	public CameraSpring(float x, float y, Body body, Box2DProcessing worldController) {
		MouseJointDef md = new MouseJointDef();
		md.bodyA = worldController.getGroundBody();
		md.bodyB = body;
		Vec2 mp = worldController.coordPixelsToWorld(x,y);
		md.target.set(mp);
		if (worldController.equals(PhysicGameWorld.controller)) md.maxForce = (float) (150.0);
		else md.maxForce = (float) (20.0);
		md.frequencyHz = 3.5f;
		md.dampingRatio = 0.75f*Program.NORMAL_FPS/40f;
		//md.dampingRatio = 0.3f*40f/Program.NORMAL_FPS;
		mouseJoint = (MouseJoint) worldController.world.createJoint(md);
		body.setUserData("Camera body");
	}

	public CameraSpring(float x, float y, Body body, Box2DProcessing worldController, float force, float frequency, float dampingRatio) {
		MouseJointDef md = new MouseJointDef();
		md.bodyA = worldController.getGroundBody();
		md.bodyB = body;
		Vec2 mp = worldController.coordPixelsToWorld(x,y);
		md.target.set(mp);
		md.maxForce = force;
		md.frequencyHz = frequency;
		md.dampingRatio = dampingRatio;
		mouseJoint = (MouseJoint) worldController.world.createJoint(md);
		body.setUserData("Camera body");
	}
	
	void destroy() {
	    // We can get rid of the joint when the mouse is released
	    if (mouseJoint != null) {
	    	PhysicGameWorld.controller.world.destroyJoint(mouseJoint);
	    	mouseJoint = null;
	    }
	}

	public void relax(){
		relaxed = true;
	}

	public boolean isRelaxed(){
		return relaxed;
	}

	public PVector getActualPosition(float x, float y) {
		Vec2 mouseWorldPos = PhysicGameWorld.coordPixelsToWorld(x,y);
		mouseJoint.setTarget(mouseWorldPos);
		mutPos.x = 0;
		mutPos.y = 0;
		mouseJoint.getAnchorB(mutPos);
		Vec2 pixelPos = PhysicGameWorld.coordWorldToPixels(mutPos.x, mutPos.y);
		actualPos.x = pixelPos.x;
		actualPos.y = pixelPos.y;
		return actualPos;
	}
}
