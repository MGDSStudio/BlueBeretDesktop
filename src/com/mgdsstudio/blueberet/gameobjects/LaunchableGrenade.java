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
import processing.core.PApplet;
import processing.core.PVector;

public class LaunchableGrenade extends AbstractGrenade {
	private final String objectToDisplayName = "Launchable grenade";
	private static StaticSprite sprite;
	public final static float NORMAL_SPEED = Program.engine.width/8.2f;
	public final static int NORMAL_WIDTH = (int) (Program.engine.width/65f);
	public final static int NORMAL_HEIGHT = (int) (NORMAL_WIDTH/2.2f);

	private static final float dimensionCoefficient = 1.1f;
	
	public LaunchableGrenade(PVector position, float shootingAngle) {
		boundingWidth = NORMAL_WIDTH;
		boundingHeight = NORMAL_HEIGHT;
		speed = NORMAL_SPEED;
		System.out.println("This gpreenade has angle " + shootingAngle + " and direction: " +direction);
		direction = TO_RIGHT;	// only for body making
		makeBody(new Vec2(position.x, position.y), (int)boundingWidth, (int)boundingHeight);
		setData(shootingAngle, 0.5f);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_BULLET);
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}
	


	public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
		//if (sprite == null) {
			sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width, height);
		//}
	}

	public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight){
		//if (sprite == null) {
			sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, (int) ((xRight - xLeft) * dimensionCoefficient), (int) ((yRight - yLeft) * dimensionCoefficient));
		//}
	}

	public StaticSprite getSprite(){
		return sprite;
	}




	public static void loadSprites(Tileset tilesetUnderPath) {
		sprite.loadSprite(tilesetUnderPath);
		System.out.println("Sprite for hand grenade was uploaded from path " + tilesetUnderPath);
	}

	@Override
	public void draw(GameCamera gameCamera) {
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		float a = body.getAngle()- PApplet.PI/2;
		if (sprite != null) {
			sprite.draw(gameCamera, pos, a);
		}
	}

	@Override
	public boolean isBulletColisionWithFixtureMakeExplosion(Fixture collidedWhizbangFixture) {
		if (collidedWhizbangFixture.getType() == ShapeType.CIRCLE) return true;
		else return false;
	}

	@Override
	public boolean isColisionWithFixtureMakeExplosion(Body contactBody, GameRound gameRound, Fixture collidedWhizbangFixture) {
		if (collidedWhizbangFixture.getType() == ShapeType.CIRCLE) return  true;
		else return false;
	}


}
