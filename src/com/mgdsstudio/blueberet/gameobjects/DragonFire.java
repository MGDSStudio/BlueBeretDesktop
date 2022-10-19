package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
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

public class DragonFire extends LaunchableWhizbang {

	private int attackValue = 35;
	private final String objectToDisplayName = "Dragon fire";
	private static StaticSprite sprite;
	private static SpriteAnimation spriteAnimation;
	private float velocityError = 0.15f;
	public final static float NORMAL_SPEED = 15;
	public final static int NORMAL_WIDTH = (int) (40);
	public final static int NORMAL_HEIGHT = (int) (NORMAL_WIDTH/3);
	public final Body dragonBody;
	
	public DragonFire(Body dragonBody, PVector position, boolean direction) {
		type = WITHOUT_EXPLOSION;
		boundingWidth = NORMAL_WIDTH;
		boundingHeight = NORMAL_HEIGHT;
		speed = NORMAL_SPEED;
		this.direction = direction;
		makeBody(new Vec2(position.x, position.y), (int)boundingWidth, (int)boundingHeight);
		setData();
		this.dragonBody = dragonBody;
	}

	/*
	private void setFilterData(byte group) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.setFilterData(filter);
	}*/

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}
	
	private void setData() {
	    if (direction == TO_RIGHT) body.setLinearVelocity(new Vec2(Program.engine.random(speed*0.9f, speed*1.1f), 0f));
	    else body.setLinearVelocity(new Vec2(Program.engine.random(-speed*(1+velocityError/2), -speed*(1+velocityError/2)), 0f));
		body.setGravityScale(0f);
		//setFilterData(COALISION_DRAGON_FIRE_WITH_BOWSER);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_DRAGON_FIRE);
	}

	/*
	public static void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
	}*/

	public static void loadAnimation(Tileset tilesetUnderPath) {
		spriteAnimation.loadAnimation(tilesetUnderPath);
	}

	public static SpriteAnimation getSpriteAnimation (){
		return spriteAnimation;
	}

	/*
	public static StaticSprite getSprite(){
		return sprite;
	}
	*/


	public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
		sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
	}


	public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, byte spritesNumber, int updateFrequency){
		loadAnimationData(path, xLeft, yLeft, xRight, yRight, width,  height, spritesNumber, updateFrequency);
	}

	public static void loadAnimationData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, byte spritesNumber, int updateFrequency){
		spriteAnimation = new SpriteAnimation(path, xLeft, yLeft, xRight, yRight, width, height, spritesNumber, updateFrequency);
	}

	public static void loadAnimationData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height,  byte rowsNumber, byte columnsNumber, int updateFrequency){
		spriteAnimation = new SpriteAnimation(path, xLeft, yLeft, xRight, yRight, width,  height, rowsNumber, columnsNumber, updateFrequency);
	}



	/*
	@Override
	public void draw(GameCamera gameCamera) {
		if (Game2D.DEBUG) super.draw(gameCamera);
		Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
		float a = body.getAngle();
		if (sprite != null) sprite.draw(gameCamera, pos, a);
	}
	*/



	@Override
	public void draw(GameCamera gameCamera) {
		if (isVisibleOnScreen(gameCamera, getPixelPosition().x, getPixelPosition().y)) {
			float a = Program.engine.degrees(body.getAngle());
			if (spriteAnimation != null) {
				spriteAnimation.update();
				Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
				spriteAnimation.draw(gameCamera, pos, a, direction);
			}
		}
	}

	@Override
	public int getAttackValue(){
		return attackValue;
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
	public Body getBindedBody() {
		return dragonBody;
	}
}
