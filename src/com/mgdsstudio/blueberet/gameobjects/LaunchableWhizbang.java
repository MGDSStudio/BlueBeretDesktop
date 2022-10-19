package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PConstants;

public abstract class LaunchableWhizbang extends GameObject implements ISimpleUpdateable, IDrawable{

	// Explosion ability types
	private Vec2 prevPos = new Vec2();
	protected final static boolean WITH_EXPLOSION = true;
	protected final static boolean WITHOUT_EXPLOSION = false;
	public boolean type = WITH_EXPLOSION;


	
	//protected int width;
	//protected int height;
	protected float speed;
	
	// Direction
	public final static boolean TO_LEFT = false;
	public final static boolean TO_RIGHT = true;
	protected boolean direction = TO_LEFT;
	
	protected void makeBody(Vec2 center, int w, int h) {		
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    body = PhysicGameWorld.controller.createBody(bd);

	    CircleShape circle = new CircleShape();
	    circle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(9*h/18);
	    Vec2 offset;
	    if (direction == TO_RIGHT) offset = new Vec2(w/2,0);
	    else offset = new Vec2(-w/2,0);
	    offset = PhysicGameWorld.controller.vectorPixelsToWorld(offset);
	    circle.m_p.set(offset.x,offset.y);

	    PolygonShape sd = new PolygonShape();
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(w/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(h/2);
	    sd.setAsBox(box2dW, box2dH);

	    body.createFixture(sd,1.0f);
	    body.createFixture(circle, 1.0f);
	    body.setBullet(true);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}

	// Overriden
	/*
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}*/
	
	@Override
	public void draw(GameCamera gameCamera) {
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
	    float a = body.getAngle();
	    //if (sprite != null) sprite.draw(gameCamera, pos, a);
	    //Programm.objectsFrame.beginDraw();
	    Program.objectsFrame.rectMode(PConstants.CENTER);
	    Program.objectsFrame.pushMatrix();
	    Program.objectsFrame.translate(pos.x-gameCamera.getActualPosition().x+ Program.objectsFrame.width/2, pos.y-gameCamera.getActualPosition().y+ Program.objectsFrame.height/2);
	    Program.objectsFrame.rotate(-a);
	    Program.objectsFrame.pushStyle();
	    Program.objectsFrame.noFill();
	    Program.objectsFrame.stroke(0);
	    Program.objectsFrame.rect(0,0,boundingWidth, boundingHeight);
	    if (direction == TO_RIGHT) Program.objectsFrame.ellipse(boundingWidth/2, 0, 16*boundingHeight/17, 16*boundingHeight/17);
	    else Program.objectsFrame.ellipse(-boundingWidth/2, 0, 16*boundingHeight/17, 16*boundingHeight/17);
	   // ellipse(0, h/2, r*2, r*2);	    
	    Program.objectsFrame.popStyle();
	    Program.objectsFrame.popMatrix();
	    //Programm.objectsFrame.endDraw();
	}

	@Override
	public void update() {
		prevPos = PhysicGameWorld.controller.coordWorldToPixels(body.getPosition());
	}
	
	public Vec2 getPrevPosition() {
		return prevPos;
	}

	public boolean getDirection(){
		return direction;
	}


	@Override
	public void kill() {
		dead = true;
		body.setGravityScale((float)(body.getGravityScale()));
		body.getFixtureList().setDensity(1);
		body.resetMassData();
		if (body.isFixedRotation()) body.setFixedRotation(false);
	}

	public abstract Body getBindedBody();

    public void setNormalGravitation() {
		body.setGravityScale(1);

	}
	/*
	@Override
	public void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
	}
	*/

	public int getAttackValue(){

		System.out.println("This attack value is not set for launchable whizbang");
		return 1;
	}

	public abstract boolean isBulletColisionWithFixtureMakeExplosion(Fixture collidedWhizbangFixture);
	public abstract boolean isColisionWithFixtureMakeExplosion(Body contactBody, GameRound gameRound, Fixture collidedWhizbangFixture);

	public boolean canBeExplodedByTimer() {
		return false;
	}
}
