package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

public class Spring {
	public MouseJoint mouseJoint;
	//RoundElement roundElement;
	GameObject gameObject;

	/*
	public Spring(RoundElement roundElement) {
		this.roundElement = roundElement;
		MouseJointDef md = new MouseJointDef();	    
	    md.bodyA = PhysicGameWorld.controller.getGroundBody();
	    md.bodyB = roundElement.body;
	    Vec2 mp = roundElement.body.getPosition();
	    md.target.set(mp);	    
	    md.maxForce = (float) (1000.0 * roundElement.body.m_mass);
	    md.frequencyHz = 5.0f;
	    md.dampingRatio = 0.2f;		    
	    mouseJoint = (MouseJoint) PhysicGameWorld.controller.world.createJoint(md);
	}
*/
	public Spring(GameObject gameObject) {
		this.gameObject = gameObject;
		MouseJointDef md = new MouseJointDef();
		md.bodyA = PhysicGameWorld.controller.getGroundBody();
		md.bodyB = gameObject.body;
		Vec2 mp = gameObject.body.getPosition();
		md.target.set(mp);
		//gameObject.body.m_mass = gameObject.body.getMass()*0.01f;
		gameObject.body.m_mass = gameObject.body.getMass()*100;
		md.maxForce = 50000 * gameObject.body.m_mass;
		md.frequencyHz = 9.0f;
		md.dampingRatio = 0.2f;
		/*
		md.frequencyHz = 5.0f;
		md.dampingRatio = 0.2f;
		*/
		/*
		md.maxForce = (float) (6000.0 * gameObject.body.m_mass);
		md.frequencyHz = 5.0f;
		md.dampingRatio = 0.2f;
		*/

		//md.dampingRatio = 0.2f;
		mouseJoint = (MouseJoint) PhysicGameWorld.controller.world.createJoint(md);
		//mouseJoint.
	}



	public void destroy() {
	    // We can get rid of the joint when the mouse is released
	    if (mouseJoint != null) {
	    	PhysicGameWorld.controller.world.destroyJoint(mouseJoint);
	      mouseJoint = null;
	    }
	}

	/*
	public boolean mustBeDeleted() {
		if (roundElement.isDead()) {
			System.out.println("The body is deleted");
	    	destroy();
	    	return true;
	    }
		else return false;
	}
	*/

	public boolean mustBeDeleted() {
		if (gameObject.isDead()) {
			System.out.println("The body is deleted");
	    	destroy();	
	    	return true;
	    }
		else return false;
	}
	
	void draw(GameCamera gameCamera) {
	    if (mouseJoint != null) {
	      // We can get the two anchor points
	      Vec2 v1 = new Vec2(0,0);
	      mouseJoint.getAnchorA(v1);
	      Vec2 v2 = new Vec2(0,0);
	      mouseJoint.getAnchorB(v2);
	      // Convert them to screen coordinates
	      v1 = PhysicGameWorld.controller.coordWorldToPixels(v1);
	      v2 = PhysicGameWorld.controller.coordWorldToPixels(v2);
	      // And just draw a line
	      if (Program.debug) {
	    	  if (Program.USE_BACKGROUND_BUFFER) {
	  			Program.objectsFrame.beginDraw();
	  			Program.objectsFrame.pushMatrix();
	  		    Program.objectsFrame.translate(v1.x-gameCamera.getActualPosition().x+ Program.objectsFrame.width/2, v1.y-gameCamera.getActualPosition().y+ Program.objectsFrame.height/2);
	  		    Program.objectsFrame.pushStyle();
	  		    Program.objectsFrame.strokeWeight(0.8f);
	  		    Program.objectsFrame.stroke(0);
	  		  Program.engine.line(0, 0,v2.x,v2.y);
	  		    Program.objectsFrame.popStyle();
	  		    Program.objectsFrame.popMatrix();
	  			Program.objectsFrame.endDraw();
	  		}
	    	  	     
	      
	      }
	    }
	  }

	public void update() {
		if (Program.engine.frameCount%2 == 1) {
			final float criticalAngle = 0.05f;
			if (gameObject.body.getAngle() > criticalAngle) {
				gameObject.body.setTransform(gameObject.body.getPosition(), criticalAngle/2);
				//if (Game2D.DEBUG) System.out.println("Angle was reset");
			}
			else if (gameObject.body.getAngle() < (-criticalAngle)) {
				gameObject.body.setTransform(gameObject.body.getPosition(), -criticalAngle/2);
				//if (Game2D.DEBUG) System.out.println("Angle was reset");
			}	
			
		}
	}
	

}
