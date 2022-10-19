package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PConstants;
import processing.core.PVector;


public class RoundBox extends RoundElement implements ISimpleUpdateable, IDrawable{
	  public static final String CLASS_NAME = "RoundBox";
		private final String objectToDisplayName = "Box";

	  // Constructor
	  public RoundBox(GameObjectDataForStoreInEditor data) {
		  withSpring = data.isWithSpring();
		  BodyType bodyType = data.getBodyType();
		  if (withSpring)  bodyType = BodyType.DYNAMIC;
		  makeBody(data.getPosition(), data.getAngle(), data.getWidth(), data.getHeight(), bodyType);
		  setLife((int)data.getLife(), (int)data.getLife());

		  if (withSpring) {
			  spring = new Spring(this);
			  body.setFixedRotation(true);
		  }
		  graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
		  //String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillAre
		  boolean fill = false;
		  if (data.getFill()!=0) fill = true;
		  //System.out.println("Fill is: " + fill);
		  float graphicDelta = data.getWidth()/data.getHeight();
		  System.out.println("Delta: " + graphicDelta);
		  if (graphicDelta<2){
			  System.out.println("For this block is fill data wrong");
		  }
		  boolean realFillValue = testFillAreaValue(data.getWidth(), data.getHeight(), fill);
		  loadImageData(data.getPathToTexture(), data.getGraphicLeftX(), data.getGraphicUpperY(), data.getGraphicRightX(), data.getGraphicLowerY(), data.getWidth(), data.getHeight(), realFillValue);

	  }

	  public RoundBox(Vec2 position, float angle, float width, float height, int life, boolean withSpring) {
		  makeBody(new Vec2(position.x, position.y), angle, width, height, BodyType.DYNAMIC);
	      setLife(life, life);
	      this.withSpring = withSpring;
		    if (withSpring) {
			    spring = new Spring(this);
			    body.setFixedRotation(true);
		  }
		  graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
		  //CLASS_NAME = "RoundBox";
	  }

	public RoundBox(PVector position, float angle, float width, float height, int life, boolean withSpring) {
		makeBody(new Vec2(position.x, position.y), angle, width, height, BodyType.DYNAMIC);
		setLife(life, life);
		this.withSpring = withSpring;
		if (withSpring) {
			spring = new Spring(this);
			body.setFixedRotation(true);
		}
		graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
	}

	public RoundBox(Vec2 position, float angle, float width, float height, int life, boolean withSpring, BodyType bodyType) {
		makeBody(new Vec2(position.x, position.y), angle, width, height, bodyType);
		setLife(life, life);
		if (bodyType == BodyType.DYNAMIC) this.withSpring = withSpring;
		else withSpring = false;
		if (withSpring) {
			spring = new Spring(this);
			body.setFixedRotation(true);
		}
		graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
	}

	public RoundBox(Vec2 position, float angle, float width, float height, int life, BodyType bodyType) {
		makeBody(new Vec2(position.x, position.y), angle, width, height, bodyType);
		setLife(life, life);
		withSpring = false;
		if (withSpring) {
			spring = new Spring(this);
			body.setFixedRotation(true);
		}
		graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
		//setFilterData(body.getFixtureList().getShape(), COALISION_ROTATING_STICK_WITH_ROUND_ELEMENTS);
	}

	  void setLinearVelocity() {
		  body.setLinearVelocity(new Vec2(Program.engine.random(0, 0), Program.engine.random(0, 0)));
	  }
	  
	  void setSpeed() {
		  body.setAngularVelocity(Program.engine.random(0, 0));
	  }

	  // Is the particle ready for deletion?
	  boolean done() {
	    // Let's find the screen position of the particle
	    Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
	    // Is it off the bottom of the screen?
	    if (pos.y > Program.engine.height+boundingWidth*boundingHeight) {
	      killBody();
	      return true;
	    }
	    return false;
	  }


	  
	  void makeBody(Vec2 center, float angle, float width, float height, BodyType bodyType) {
	    PolygonShape sd = new PolygonShape();
		boundingWidth = width;
		boundingHeight = height;
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(width/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(height/2);
	    sd.setAsBox(box2dW, box2dH);
	    FixtureDef fd = new FixtureDef();
	    fd.shape = sd;
	    // Parameters that affect physics
		//fd.density = 0.02f;
	    fd.density = 2f;
	    fd.friction = 0.1f;
	    // fd.friction = 0.01f;
	    fd.restitution = 0.05f;
	    // Define the body and make it from the shape
		  boolean loaded = false;
		  while(!loaded)
		  try {
			  BodyDef bd = new BodyDef();
			  bd.type = bodyType;
			  if (center != null) System.out.println("Pos: " + center);
			  else System.out.println("Pos is null. Angle is: " + angle);

			  bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
			  bd.angle = (float) Math.toRadians(angle);
			  //bd.linearDamping = 1;
			  body = PhysicGameWorld.controller.createBody(bd);
			  body.createFixture(fd);
			  body.setGravityScale(1f);
			  loaded = true;
		  }
		  catch (Exception e){
		  	System.out.println("Can not create body for box; " + e);
		  }

		  setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	  }

	@Override
	public void draw(GameCamera gameCamera) {
	  	//if (body.getType() == BodyType.DYNAMIC) System.out.println("Dynamic");
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		float a = body.getAngle();
		//updateGraphicSidesData(); this is not needed
		if (true == true) {
			//if (isObjectGraphicInVisibleZone(gameCamera, graphicLeftSide, graphicRightSide, graphicUpperSide, graphicLowerSide)){
			//System.out.println("The object is shown");
			if (Program.debug || (Program.gameStatement == Program.LEVELS_EDITOR && sprite == null)) {
				//Programm.objectsFrame.beginDraw();
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
				Program.objectsFrame.rotate(-a);
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.noFill();
				Program.objectsFrame.rectMode(PConstants.CENTER);
				Program.objectsFrame.strokeWeight(1.8f);
				Program.objectsFrame.stroke(232,49,202);
				Program.objectsFrame.rect(0, 0, boundingWidth, boundingHeight);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
			}
			if (sprite != null) {
				tintUpdatingBySelecting();
				sprite.draw(gameCamera, body);
			}
			else if (spriteAnimation != null) {
				spriteAnimation.update();
				if (isSelected()) spriteAnimation.setTint(Program.engine.color(255, actualSelectionTintValue));
				spriteAnimation.draw(gameCamera, pos, a, false);
			}


			//else System.out.println("The object is not visible");
		}
	}


	
	@Override
	public void update() {
		
	}

	public boolean isDead() {		
		return dead;
	}


	@Override
	public String getClassName(){
	  	return CLASS_NAME;
	}

	@Override
	public String getStringData(){
		GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
		saveMaster.createRoundBox();
		System.out.println("Data string for round box: " +saveMaster.getDataString());
		return saveMaster.getDataString();
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}
}
