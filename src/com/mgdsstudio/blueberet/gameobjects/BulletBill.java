package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PVector;

public class BulletBill extends LaunchableWhizbang{
	private static StaticSprite sprite;
	private final String objectToDisplayName = "Rocket";

	public final static float NORMAL_SPEED = Program.engine.width/60;
	private float velocityError = 0.15f;	
	public final static int NORMAL_WIDTH = (int) (Program.engine.width/9.0);
	public final static int NORMAL_HEIGHT = (int) (NORMAL_WIDTH/2.2);
	
	public BulletBill(PVector position, boolean direction) {
		boundingWidth = NORMAL_WIDTH;
		boundingHeight = NORMAL_HEIGHT;
		speed = NORMAL_SPEED;
		this.direction = direction;
		makeBody(new Vec2(position.x, position.y), (int)boundingWidth, (int)boundingHeight);
		setData();
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
	}
	
	private void setData() {
	    if (direction == TO_RIGHT) body.setLinearVelocity(new Vec2(Program.engine.random(speed*0.9f, speed*1.1f), 0f));
	    else body.setLinearVelocity(new Vec2(Program.engine.random(-speed*(1-velocityError/2), -speed*(1+velocityError/2)), 0f));
		body.setGravityScale(0f);
	}

	public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
		sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
	}

	public static void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
		//System.out.print("There are no part for this picture");
	}

	public static StaticSprite getSprite(){
		return sprite;
	}

	@Override
	public void draw(GameCamera gameCamera) {
		//if (isObjectCenterInVisibleZone(gameCamera)) {
			if (Program.debug) super.draw(gameCamera);
			Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
			float a = body.getAngle();
			if (sprite != null) sprite.draw(gameCamera, pos, a);
		//}
	}

	@Override
	public Body getBindedBody() {
		return null;
	}

	@Override
	public boolean isBulletColisionWithFixtureMakeExplosion(Fixture collidedWhizbangFixture) {
		if (collidedWhizbangFixture.getType() == ShapeType.CIRCLE) return true;
		else return false;
	}

	@Override
	public boolean isColisionWithFixtureMakeExplosion(Body contactBody, GameRound gameRound, Fixture collidedWhizbangFixture) {
		return false;
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}
}
