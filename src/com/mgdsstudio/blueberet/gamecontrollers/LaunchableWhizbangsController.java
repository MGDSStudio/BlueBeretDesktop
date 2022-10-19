package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.Bowser;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PVector;

import java.util.ArrayList;

public class LaunchableWhizbangsController {
	ArrayList<LaunchableWhizbang> whizbangs = new ArrayList<LaunchableWhizbang>();
	GameCamera gameCamera;
	long lastBulletAppearingTime;
	long newBulletApearingTime;
	Timer newBulletApearingTimer;
	final static int NORMAL_APEARING_TIME = 6000;
	int apearingTime = NORMAL_APEARING_TIME;
	int timeError = 1000;
	private boolean paused;
	private Flag bulletsBillActivatingFlag;
	
	// Fly direction
	public static final boolean TO_LEFT = false;
	public static final boolean TO_RIGHT = true;
	private boolean direction = TO_LEFT;

	private boolean withBulletBills;


	public LaunchableWhizbangsController(boolean withBulletBills, GameCamera gameCamera, Flag flag){
		this.withBulletBills = withBulletBills;
		if (withBulletBills) bulletsBillActivatingFlag = flag;
		this.gameCamera = gameCamera;
		lastBulletAppearingTime = Program.engine.millis();
		//newBulletApearingTime = (long) (lastBulletAppearingTime+apearingTime+Game2D.engine.random(-timeError/2, timeError/2));
		newBulletApearingTimer = new Timer((int) (apearingTime+ Program.engine.random(-timeError/2, timeError/2)));

	}

	public LaunchableWhizbangsController(GameCamera gameCamera){
		withBulletBills = false;
		this.gameCamera = gameCamera;
		lastBulletAppearingTime = Program.engine.millis();
		//newBulletApearingTime = (long) (lastBulletAppearingTime+apearingTime+Game2D.engine.random(-timeError/2, timeError/2));
		newBulletApearingTimer = new Timer((int) (apearingTime+ Program.engine.random(-timeError/2, timeError/2)));
	}
	
	public void update(GameRound gameRound) {
		if (withBulletBills) {
			if (bulletsBillActivatingFlag.inZone(gameCamera.getActualPosition())) {
				if (bulletsBillActivatingFlag.inZone(gameRound.getPlayer().getPixelPosition())) {
					if (newBulletApearingTimer.isTime()) {
						addNewBulletBill();
					}
				}
			}
		}
		for (int i = (whizbangs.size()-1); i >= 0; i--) {
			whizbangs.get(i).update();
			if (whizbangs.get(i).canBeExplodedByTimer()){
				addNewExplosion(gameRound, whizbangs.get(i));
			}
		}
		deleteOldBullet();
	}



	public ArrayList<LaunchableWhizbang> getWhizbangs(){
		return whizbangs;
	}

	private void deleteOldBullet() {
		if (direction == TO_LEFT) {
			for (int i = 0; i < whizbangs.size(); i++) {
				if (((whizbangs.get(i).getPixelPosition().x) < (gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)))) {
					if (Program.debug) {
						if (whizbangs.get(i).getClass() == BulletBill.class) System.out.println("Bullet bill has flown away to left");
						if (whizbangs.get(i).getClass() == DragonFire.class) System.out.println("Dragon fire has flown away to left");
						if (whizbangs.get(i).getClass() == LaunchableGrenade.class) System.out.println("Launchable greeenade has flown away to left");
					}
					killWhizbang(i);					
				}
			}
		}
		else if (direction == TO_RIGHT) {
			for (int i = 0; i < whizbangs.size(); i++) {
				if (((whizbangs.get(i).getPixelPosition().x) > (1.2*gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)))) {
					if (Program.debug) {
						if (whizbangs.get(i).getClass() == BulletBill.class) System.out.println("Bullet bill has flown away to right");
						if (whizbangs.get(i).getClass() == DragonFire.class) System.out.println("Dragon fire has flown away to right");
						if (whizbangs.get(i).getClass() == LaunchableGrenade.class) System.out.println("Launchable greeenade has flown away to right");
					}
					killWhizbang(i);
				}
			}
		}
	}
	
	public boolean getDirection() {
		return direction;
	}
	
	public void addNewLaunchableGreenade(LaunchableGrenade launchableGrenade) {
		whizbangs.add(launchableGrenade);
	}

	public void addNewHandGreenade(HandGrenade launchableGrenade) {
		whizbangs.add(launchableGrenade);
	}
	
	public void addNewDragonFire(Person person) {
		boolean side = person.getSightDirection();
		Vec2 shifting = Bowser.getRelativeHeadPosition();
		PVector position = person.getPixelPosition();
		position.y+=shifting.y;
		if (side == Person.TO_LEFT) position.x-=shifting.x;
		else position.x+=shifting.x;
		DragonFire dragonFire = new DragonFire(person.body, position, side);
		whizbangs.add(dragonFire);
	}

	private void addNewBulletBill() {
		newBulletApearingTimer.setNewTimer((int) (apearingTime+ Program.engine.random(-timeError/2, timeError/2)));
		float yPosition = Program.engine.random(bulletsBillActivatingFlag.getPosition().y-bulletsBillActivatingFlag.getHeight()/2, bulletsBillActivatingFlag.getPosition().y+bulletsBillActivatingFlag.getHeight()/2);
		PVector position = new PVector (gameCamera.getActualPosition().x+(500/10)/gameCamera.getScale(), yPosition);

		//position.y= Game2D.engine.random(((Game2D.engine.height/4)*gameCamera.getScale()), ((Game2D.engine.height-(Game2D.engine.height/4))*gameCamera.getScale()));
		BulletBill bulletBill = new BulletBill(position, LaunchableWhizbang.TO_LEFT);
		//!if (BulletBill.sprite == null) bulletBill.loadImageData("Tileset2.png", (int)192, (int)32, (int)207, (int)47, bulletBill.width, bulletBill.height);
		whizbangs.add(bulletBill);
	}

	public void loadAnimationData(Class type, String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, byte rowsNumber, byte columnsNumber, int updateFrequency){
		if (type == DragonFire.class){
			DragonFire.loadAnimationData(path, xLeft, yLeft, xRight, yRight, width, height, rowsNumber, columnsNumber, updateFrequency);
			System.out.println("Animation for dragon fire was successfully uploaded");
		}

	}

	public void loadImageData(Class type, String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
		if (type == BulletBill.class){
			BulletBill.loadImageData(path, xLeft, yLeft, xRight, yRight, width, height);
			System.out.println("Graphic for Bullet bill was uploaded");
		}
		else if (type == DragonFire.class){
			DragonFire.loadAnimationData(HeadsUpDisplay.mainGraphicSource.getPath(), (int)820, (int)0, (int)1024, (int)192, width, height, (byte)3, (byte)2 , (int)15);
			DragonFire.loadImageData(path, xLeft, yLeft, xRight, yRight, width, height);
			System.out.println("Graphic for dragon fire was uploaded");
		}
		else if (type == LaunchableGrenade.class){
			LaunchableGrenade.loadImageData(path, xLeft, yLeft, xRight, yRight);
			System.out.println("Sprites for the launchable greenade were succesfully uploaded for type with width: " + width + " and height " + height);
		}
		else if (type == HandGrenade.class){
			HandGrenade.loadImageData(path, xLeft, yLeft, xRight, yRight);
			System.out.println("Sprites for the hand greenade were succesfully uploaded for type with width: " + width + " and height " + height);
		}
		else {
			System.out.println("No data for withbang with type " + type.getName());
		}
		//super.loadImageData(path, xLeft, yLeft, xRight, (int)(yRight-(Game2D.engine.abs(yRight-yLeft))/2), width,  (int) (width/2));
		//pipeWithoutFlangeSprite = new StaticSprite(path, xLeft, (int)(yRight-(Game2D.engine.abs(yRight-yLeft))/2), xRight, yRight, width,  (int) (height-(width/2)));
		System.out.println("Sprites for launchable whizbangs were succesfully uploaded for type " + type.toString());
	}

	
	public void pause() {
		if (!paused) paused = true;
	}
	
	public void awake() {
		if (paused) paused = false;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void draw(GameCamera gameCamera) {
		for (int i =0; i < whizbangs.size(); i++){
			whizbangs.get(i).draw(gameCamera);
		}/*
		for (LaunchableWhizbang whizbang : whizbangs) {
			whizbang.draw(gameCamera);
		}*/

		if (withBulletBills){
			bulletsBillActivatingFlag.draw(gameCamera);
		}
	}

	public int getWhizbangsNumber() {
		return whizbangs.size();
		//return 0;
	}

	public Body getBody(int i) {
		if (whizbangs.get(i).body != null) return whizbangs.get(i).body;
		else {
			System.out.println("This bullet bill has a trouble");
			return null;
		}
	}
	
	public LaunchableWhizbang getWhizbang(int i) {
		return whizbangs.get(i); 
	}
	
	public LaunchableWhizbang getWhizbang(Body body) {
		for (LaunchableWhizbang  launchableWhizbang : whizbangs) {
			if (launchableWhizbang.body.equals(body)) return launchableWhizbang;
		}
		System.out.println("Can not find any whizbungs with this body");
		return null;
	}

	public void killWhizbang(int i) {
		killWhizbang(whizbangs.get(i));
	}
	
	public void killWhizbang(LaunchableWhizbang whizbang) {
		whizbang.kill();
		whizbang.killBody();
		whizbangs.remove(whizbang);
	}
	
	private void updateControl() {
		
	}

	private void stopWhizbang(int i) {		
		whizbangs.get(i).body.setGravityScale(1);
	}
	
	private void stopWhizbang(LaunchableWhizbang whizbang) {		
		whizbang.body.setGravityScale(1);
	}
	
	public void coalisionWithBullet(GameRound gameRound, LaunchableWhizbang whizbang, Fixture collidedFixture, Bullet bullet) {		
		if (whizbang.getClass() == DragonFire.class) {
			//System.out.println("Colision of bullet with dragon fire");
			killWhizbang(whizbang);
		}
		else {
			if (whizbang.isBulletColisionWithFixtureMakeExplosion(collidedFixture)) {
				addNewExplosion(gameRound, whizbang);
			}
			else stopWhizbang(whizbang);
		}
	}

	private void addNewExplosion(GameRound gameRound, LaunchableWhizbang whizbang) {
		float angle = whizbang.body.getAngle();
		Vec2 explosionPlace = new Vec2((whizbang.getPixelPosition().x), (whizbang.getPixelPosition().y));
		if (whizbang.getClass() == BulletBill.class && whizbang.getDirection() == LaunchableWhizbang.TO_LEFT){
			explosionPlace.x+=(whizbang.getHeight()*Math.cos(angle)/2);
			explosionPlace.y += (whizbang.getHeight() * Math.sin(angle) / 2);
		}
		else if (whizbang.getClass() == HandGrenade.class){

		}
		else {
			explosionPlace.x -= (whizbang.getHeight() * Math.cos(angle) / 2);
			explosionPlace.y += (whizbang.getHeight() * Math.sin(angle) / 2);
		}

		Explosion explosion = new Explosion(gameRound, explosionPlace, whizbang);
		gameRound.explosions.add(explosion);								
		killWhizbang(whizbang);
		ArrayList<SingleColliding> singleCollidings;
		singleCollidings = explosion.getCollidings(gameRound);
		gameRound.getHittingController().applyExplosionsDamages(gameRound, singleCollidings, gameRound.explosions.get(gameRound.explosions.size()-1).getMaxImpulse(), explosion.getMaxDamage());
		//gameRound.getHittingController().applyExplosionsDamages(gameRound, singleCollidings, gameRound.explosions.get(gameRound.explosions.size()-1).getMaxImpulse(), gameRound.explosions.get(gameRound.explosions.size()-1).getMaxDamage());
		//
		gameRound.getSoundController().setAndPlayAudio(SoundsInGame.EXPLOSION_2);
		gameRound.addExplosionLight(explosionPlace.x, explosionPlace.y);
	}


	public boolean isBodyBullet(Body body){
		if (body!= null) {
			if (body.getUserData() != null) {
				if (body.getUserData() == Bullet.BULLET) {
					return true;
				} else return false;
			}
			else return false;
		}
		else {
			System.out.println("This body is null ");
			return false;
		}
	}

	//updateContact(b1, b2, f1);
	private void updateContact(LaunchableWhizbang whizbang, Body contactBody, Fixture whizbangFixture, GameRound gameRound){
		if (getWhizbang(whizbang.body).getClass() == DragonFire.class) {
			if (contactBody != getWhizbang(whizbang.body).getBindedBody()) {
				killWhizbang(whizbang);
			}
		} else {
			if (whizbang.isColisionWithFixtureMakeExplosion(contactBody, gameRound, whizbangFixture)) {
				addNewExplosion(gameRound, getWhizbang(whizbang.body));
			}
			else stopWhizbang(whizbang);

			/*
			if (whizbangFixture.getType() == ShapeType.CIRCLE) {
				addNewExplosion(gameRound, getWhizbang(whizbang.body));
			}
			else stopWhizbang(whizbang);*/
		}
	}

	public void updateWhizbangsCoalisions(GameRound gameRound, HittingController hittingController) {
		try {
			for (int contactNumber = (PhysicGameWorld.beginContacts.size()-1); contactNumber>= 0; contactNumber--) {
				Contact contact = PhysicGameWorld.beginContacts.get(contactNumber);
				Fixture f1 = contact.getFixtureA();
				Fixture f2 = contact.getFixtureB();
				Body b1 = f1.getBody();
				Body b2 = f2.getBody();
				if (!isBodyBullet(b1) && !isBodyBullet(b2)) {
					for (int i = (getWhizbangsNumber()-1); i >= 0; i--) {
						if (getBody(i).equals(b1)) {
							updateContact(whizbangs.get(i), b2, f1, gameRound);
							/*
							if (getWhizbang(b1).getClass() == DragonFire.class) {
								if (b2 != getWhizbang(b1).getBindedBody()) {
									killWhizbang(i);
								}
							} else {
								if (f1.getType() == ShapeType.CIRCLE) {
									addNewExplosion(gameRound, getWhizbang(i));
								} else stopWhizbang(i);
							}*/

						} else if (getBody(i).equals(b2)) {
							updateContact(whizbangs.get(i), b1, f2, gameRound);
							/*
							if (getWhizbang(b2).getClass() == DragonFire.class) {
								System.out.println("Coalision with dragon fire 1");
								killWhizbang(i);
							} else {
								if (f2.getType() == ShapeType.CIRCLE) {
									addNewExplosion(gameRound, getWhizbang(i));
								} else stopWhizbang(i);
							}
							*/
						}

					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Can not update whizzbangs collisions");
			e.printStackTrace();
		}
	}

	/*
	public void updateWhizbangsCoalisions(GameRound gameRound, HittingController hittingController) {
		try {
			for (int contactNumber = (PhysicGameWorld.beginContacts.size()-1); contactNumber>= 0; contactNumber--) {
				Contact contact = PhysicGameWorld.beginContacts.get(contactNumber);
				Fixture f1 = contact.getFixtureA();
				Fixture f2 = contact.getFixtureB();
				Body b1 = f1.getBody();
				Body b2 = f2.getBody();
				if (!isBodyBullet(b1) && !isBodyBullet(b2)) {
					for (int i = (getWhizbangsNumber()-1); i >= 0; i--) {
						if (getBody(i).equals(b1)) {

							if (getWhizbang(b1).getClass() == DragonFire.class) {
								if (b2 != getWhizbang(b1).getBindedBody()) {
									killWhizbang(i);
								}
							} else {
								if (f1.getType() == ShapeType.CIRCLE) {
									addNewExplosion(gameRound, getWhizbang(i));
								} else stopWhizbang(i);
							}

						} else if (getBody(i).equals(b2)) {
							if (getWhizbang(b2).getClass() == DragonFire.class) {
								System.out.println("Coalision with dragon fire 1");
								killWhizbang(i);
							} else {
								if (f2.getType() == ShapeType.CIRCLE) {
									addNewExplosion(gameRound, getWhizbang(i));
								} else stopWhizbang(i);
							}
						}

					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Can not update whizzbangs collisions");
			e.printStackTrace();
		}
	}
	 */


    public void attackFromExplosion(int i) {

		if (whizbangs.get(i).getClass() == DragonFire.class){
			whizbangs.remove(i);
			System.out.println("Dragon fire was attacked by explosion");
		}
		else if (whizbangs.get(i).getClass() == BulletBill.class){
			whizbangs.get(i).setNormalGravitation();
		}
    }
}
