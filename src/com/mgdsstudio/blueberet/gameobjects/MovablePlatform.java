package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;

import processing.core.PApplet;
import processing.core.PVector;

public class MovablePlatform extends RoundElement implements IDrawable {
	//private StaticSprite sprite;
	private final String objectToDisplayName = "Platform";
	private final float w;
	private float h;
	//private PVector velocity;

	public MovablePlatform(Vec2 center, int width, int height) {
		w = width;
		h = height;
		float x = center.x;
		float y = center.y;
		PolygonShape sd = new PolygonShape();
		float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(w/2);
		float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(h/2);
		sd.setAsBox(box2dW, box2dH);
		BodyDef bd = new BodyDef();
		bd.type = BodyType.KINEMATIC;
		//bd.type = BodyType.DYNAMIC;
		bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(x, y));
		body = PhysicGameWorld.controller.createBody(bd);
		body.createFixture(sd,1);
		body.setGravityScale(0);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}
	
	public MovablePlatform(float xLeftUpper,float yLeftUpper, float xRightLower, float yRightLower) {	
		w = PApplet.abs(xRightLower - xLeftUpper);
		h = PApplet.abs(yRightLower - yLeftUpper);
		float x = xLeftUpper+w/2;
	    float y = yLeftUpper+h/2;
	    PolygonShape sd = new PolygonShape();	    
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(w/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(h/2);
	    sd.setAsBox(box2dW, box2dH);	    
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.KINEMATIC;
	    //bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(x, y));
	    body = PhysicGameWorld.controller.createBody(bd);
	    body.createFixture(sd,1);
	    body.setGravityScale(0);
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

	public StaticSprite getSprite(){
		return sprite;
	}

	public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea) {
		sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width, height, fillArea);
	}



		@Override
	public void loadSprites(Tileset tileset){
		//if (sprite == null) System.out.println("Don\t have sprite for platforms");
		sprite.loadSprite(tileset);
		//System.out.println("Textures for platforms were successfully uploaded");
		//super.loadSprites(tileset);
		//pipeWithoutFlangeSprite.loadSprite(tileset);
	}

	public void setMovementVelocity(int linearVelocity, float angle) {
		/*PVector velocity = new PVector(linearVelocity, 0);
		velocity.rotate(Game2D.engine.radians(angle));*/
		PVector velocity = new PVector(linearVelocity*PApplet.cos(Program.engine.radians(angle)), linearVelocity*PApplet.sin(Program.engine.radians(angle)));
		//velocity.rotate(Game2D.engine.radians(angle));
		System.out.println("Velocity: " + velocity);
		body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocity));
	}
	
	/*
	public Vec2 getMovementVelocity() {
		return PhysicGameWorld.controller.vectorWorldToPixels(body.getLinearVelocity());
		//body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocity));
	}*/
	
	public float getAbsoluteVelocity() {
		PVector velocity = new PVector(PhysicGameWorld.controller.vectorWorldToPixels(body.getLinearVelocity()).x, PhysicGameWorld.controller.vectorWorldToPixels(body.getLinearVelocity()).y);
		return velocity.mag();
		//body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocity));
	}

	@Override
	public void draw(GameCamera gameCamera) {
		//Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		if (Program.USE_BACKGROUND_BUFFER) {
		  	//Programm.objectsFrame.beginDraw();
			if (Program.debug && Program.OS == Program.DESKTOP) {
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				//Program.objectsFrame.translate(pos.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, pos.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.stroke(120, 45, 45);
				Program.objectsFrame.rectMode(PApplet.CENTER);
				Program.objectsFrame.noFill();
				Program.objectsFrame.strokeWeight(0.8f);
				Program.objectsFrame.rect(0, 0, w, h);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
			}
			//Programm.objectsFrame.endDraw();
			//.out.println("Drawn");
		}
		if (Program.WITH_GRAPHIC) {
			sprite.draw(gameCamera, body);
			//sprite.draw(gameCamera, pos, 0);
		}

	}

	public void shift(float velocity, int angle) {
		
		
		//Vec2 pos = body.getPosition();
		
		//pos.y+=PhysicGameWorld.controller.scalarPixelsToWorld(velocity);
		//body.setTransform(pos, body.getAngle());
		
		
		
	}

	public void addAccelerate(float angle, float actualAccelerate) {
		PVector velocity = new PVector();
		actualAccelerate*= Program.deltaTime;
		velocity.x = body.getLinearVelocity().x;
		velocity.y = body.getLinearVelocity().y;
		Vec2 acceleratePerFrame = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(actualAccelerate* Program.engine.cos(Program.engine.radians(angle)), actualAccelerate* Program.engine.sin(Program.engine.radians(angle))));
		velocity.add(new PVector(acceleratePerFrame.x, acceleratePerFrame.y));
		body.setLinearVelocity(new Vec2(velocity.x, velocity.y));
		//System.out.println(body.getLinearVelocity());
	}

}
