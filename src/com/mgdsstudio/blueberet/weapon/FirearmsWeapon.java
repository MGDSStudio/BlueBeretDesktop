package com.mgdsstudio.blueberet.weapon;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.controllers.PlayerGreenadeAnimationController;
import com.mgdsstudio.blueberet.graphic.splashes.ShotFireSplash;
import com.mgdsstudio.blueberet.graphic.splashes.Splash;
import com.mgdsstudio.blueberet.loading.PlayerSavingDataConstants;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;

public class FirearmsWeapon extends Weapon{	
	//Which is in the hands
	private boolean actual = false;
	private WeaponType weaponType;
	//Bullets number
	private static byte SHOTGUN_BULLETS_NUMBER = 5;
	private static byte SO_BULLETS_NUMBER = 4;
	
	//accuracy and dispersion
	private static float SHOTGUN_BULLETS_DISPERSION = 15;	// angle
	private static float SO_BULLETS_DISPERSION = 25;	// angle
	private static float ASSAULT_RIFFLE_BULLETS_DISPERSION = 42;	// angle
	private static float actualAssaulTRifleDispersion = 1;
	private static float assaulRifleDispersionVelocity = 3f;
	
	//MagazinCapacity
	public static final byte PISTOLE_MAGAZINE_CAPACITY = 12;
	public static final byte REVOLVER_MAGAZINE_CAPACITY = 6;
	public static final byte SHOTGUN_MAGAZINE_CAPACITY = 7;
	public static final byte SO_MAGAZINE_CAPACITY = 2;
	public static final byte SMG_MAGAZINE_CAPACITY = 30;
	public static final byte GRENADE_LAUNCHER_MAGAZINE_CAPACITY = 1;

	public static final byte HAND_GRENADE_MAGAZINE_CAPACITY = 1;
	private int magazineCapacity;
	private int maxMagazineCapacity;
	
	//Reloading time and other data
	private static final int CARBINE_RELOADING_TIME = 2500; //ms
	private static final int ASSAULT_RIFFLE_RELOADING_TIME = 3500; //ms
	private static final int SHOTGUN_RELOADING_TIME = 9600; //ms
	private static final int GREENADE_lAUNCHER_RELOADING_TIME = 3500; //ms
	private int reloadingTime; 
	private boolean reloading = false;	
	private boolean mustBeReloaded = true;
	
	
	public static final int CARBINE_FIRING_RATE = 55;	//Shots per min
	public static final int ASSAULT_RIFFLE_FIRING_RATE = 300;
	public static final int SHOTGUN_FIRING_RATE = 35;

	public static final int SO_FIRING_RATE = 60;
	//public static final int GREENADE_lAUNCHER_FIRING_RATE = 35;
	public int firingRate = 300;
	
	private boolean withExplosion = false;
	
	final float nominalWeaponLength = 500/15f;	// must be Game2D.engine.width/45f;
	Timer nextShotTimer, reloadingTimer;
	private boolean shootingOnThisFrameStarted;
	private int shotPower;
	private boolean reloadCompletedOnThisFrame = true;

	private boolean grenadeAttackStarted;
	private boolean grenadeAttackEnded = true;
	protected final Person owner;

	private void initHandgun(){
		shotPower = NORMAL_CARBINE_SHOT_POWER;
		firingRate = CARBINE_FIRING_RATE;
		magazineCapacity = PISTOLE_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = CARBINE_RELOADING_TIME;
		this.weaponType = WeaponType.HANDGUN;
	}

	private void initRevolver(){
		shotPower = NORMAL_CARBINE_SHOT_POWER;
		firingRate = SHOTGUN_FIRING_RATE;
		magazineCapacity = REVOLVER_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = SHOTGUN_RELOADING_TIME;
		this.weaponType = WeaponType.REVOLVER;
	}
	private void initShotgun(){
		shotPower = NORMAL_SHOTGUN_SHOT_POWER;
		firingRate = SHOTGUN_FIRING_RATE;
		magazineCapacity = SHOTGUN_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = SHOTGUN_RELOADING_TIME;
		this.weaponType = WeaponType.SHOTGUN;
	}

	private void initSO(){
		shotPower = NORMAL_SHOTGUN_SHOT_POWER;
		firingRate = SO_FIRING_RATE;
		magazineCapacity = SO_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = GREENADE_lAUNCHER_RELOADING_TIME;
		this.weaponType = WeaponType.SAWED_OFF_SHOTGUN;
	}

	private void initSMG(){
		shotPower = NORMAL_ASSAULT_RIFFLE_SHOT_POWER;
		firingRate = ASSAULT_RIFFLE_FIRING_RATE;
		magazineCapacity = SMG_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = ASSAULT_RIFFLE_RELOADING_TIME;
		this.weaponType = WeaponType.SMG;
	}

	private void initGL(){
		withExplosion = true;
		firingRate = SHOTGUN_FIRING_RATE;
		magazineCapacity = GRENADE_LAUNCHER_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = GREENADE_lAUNCHER_RELOADING_TIME;
		this.weaponType = WeaponType.M79;
	}

	private void initGrenade(){
		withExplosion = true;
		firingRate = SHOTGUN_FIRING_RATE;
		magazineCapacity = GRENADE_LAUNCHER_MAGAZINE_CAPACITY;
		maxMagazineCapacity = magazineCapacity;
		reloadingTime = GREENADE_lAUNCHER_RELOADING_TIME;
		this.weaponType = WeaponType.GRENADE;
	}

	public FirearmsWeapon(int weaponType, Person owner){
		this.owner = owner;
		  nextShotTimer = new Timer(0);		  
		  this.type = weaponType;
		  if (type == HANDGUN) {
			  initHandgun();
		  }
		  else if (type == SHOTGUN) {
			  initShotgun();
		  }
		  else if (type == SMG) {
			  initSMG();
		  }
		  else if (type == GREENADE_LAUNCHER) {
			  initGL();
		  }
		  else if (type == REVOLVER){
			  initRevolver();
		  }
		  else if (type == SO_SHOTGUN){
			  initSO();
		  }
		  else if (type == HAND_GREENADE){
			  initGrenade();
		  }
		  else {
			  firingRate = 30;
			  shotPower = 1;
			  magazineCapacity = 1;
			  maxMagazineCapacity = 1;
			  reloadingTime = 4000;
		  }
		  mustBeReloaded = true;
	  }

	public static String getWeaponNameForType(WeaponType weaponType) {
		if ( weaponType == WeaponType.SHOTGUN) return PlayerSavingDataConstants.SHOTGUN;
		else if ( weaponType == WeaponType.SAWED_OFF_SHOTGUN) return PlayerSavingDataConstants.SO_SHOTGUN;
		else if ( weaponType == WeaponType.REVOLVER) return PlayerSavingDataConstants.REVOLVER;
		else if ( weaponType == WeaponType.HANDGUN) return PlayerSavingDataConstants.HANDGUN;
		else if ( weaponType == WeaponType.GRENADE) return PlayerSavingDataConstants.HAND_GRENADE;
		else if ( weaponType == WeaponType.M79) return PlayerSavingDataConstants.M79;
		else if ( weaponType == WeaponType.SMG) return PlayerSavingDataConstants.SMG;
		else {
				System.out.println("There are no data about weapon for type " + weaponType);
				return null;

		}
	}

    public static WeaponType getWeaponTypeForName(String weaponName) {
		  if (weaponName.contains(PlayerSavingDataConstants.SHOTGUN)) return WeaponType.SHOTGUN;
		  else if (weaponName.contains(PlayerSavingDataConstants.SO_SHOTGUN)) return WeaponType.SAWED_OFF_SHOTGUN;
		  else if (weaponName.contains(PlayerSavingDataConstants.REVOLVER)) return WeaponType.REVOLVER;
		  else if (weaponName.contains(PlayerSavingDataConstants.HANDGUN)) return WeaponType.HANDGUN;
		  else if (weaponName.contains(PlayerSavingDataConstants.HAND_GRENADE)) return WeaponType.GRENADE;
		  else if (weaponName.contains(PlayerSavingDataConstants.M79)) return WeaponType.M79;
		  else if (weaponName.contains(PlayerSavingDataConstants.SMG)) return WeaponType.SMG;
			else {
				if (weaponName.contains("SAWED")){
					return WeaponType.SAWED_OFF_SHOTGUN;
				}
				else {
					if (weaponName.contains("SHOTGUN")){
						return WeaponType.SAWED_OFF_SHOTGUN;
					}
					System.out.println("There are no data about weapon for name " + weaponName);
					return null;
				}
		  }
    }

    	public int getShotPower() {
		  if (!withExplosion) return shotPower;
		  else return 0;
	  }
	  
	  @Override
	  public boolean canAttack() {
		  if (magazineCapacity<=0) return false; 
		  if (nextShotTimer.isTime()) return true;
		  else return false;
	  }
	  
	  public boolean areThereBulletsInMagazine() {
		 if (magazineCapacity<=0) return false;
		 else return true;
	  }

	  /*
	  public String getRestBulletsString(){
	  	String
	  }*/

	public int getRestBullets(){
		return magazineCapacity;
	}

	public void setMagazineCapacity(int magazineCapacity) {
		this.magazineCapacity = magazineCapacity;
		System.out.println("Magazine capacity set on " + magazineCapacity);
	}

	public int getMaxMagazineCapacity(){
		return maxMagazineCapacity;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponAsActual(boolean flag) {
		  actual = flag;		  
	  }
	  
	  public boolean isActual() {
		  return actual;
	  }
	  
	  @Override
	  public void reload() {
		  System.out.println("Started to reload");
		  if (reloadingTimer==null) reloadingTimer = new Timer(reloadingTime);
		  else reloadingTimer = new Timer(reloadingTime);
		  reloading = true;
		  reloadCompletedOnThisFrame = false;
	  }
	  
	  public boolean isReloading() {
		return reloading;
	  }

	  public void setReloadCompleted(int bullets){
		if (bullets<0) {
			reloading = false;
			reloadingTimer.stop();
			magazineCapacity = maxMagazineCapacity;
		}
		else {
			reloading = false;
			reloadingTimer.stop();
			magazineCapacity+= (byte) bullets;
		}
		reloadCompletedOnThisFrame = true;
	  }

	public void setReloadBroken(){
		reloading = false;
		reloadingTimer.stop();
		reloadCompletedOnThisFrame = true;
	}

	  @Override
	  public boolean isReloadCompleted() {
		if (reloadCompletedOnThisFrame) {
			return true;
		}/*
		else return false
		  if (reloadingTimer.isTime()) {
			  magazineCapacity = maxMagazineCapacity;
			  reloading = false;
			  reloadingTimer = null;
			  return true;			  
		  }*/
		  else return false;
	  }

	public boolean isReloadCompletedByTimer() {
		if (reloadingTimer.isTime()) {
			magazineCapacity = maxMagazineCapacity;
			reloading = false;
			reloadingTimer = null;
			return true;
		}
		else return false;
	}

	  public void update(GameRound gameRound, Human soldier){
		shootingOnThisFrameStarted = false;
		if (grenadeAttackStarted){
			if (!grenadeAttackEnded){
				if (soldier.getActualWeapon().getWeaponType() == WeaponType.GRENADE) {
					if (!soldier.isUnderAttack()) {
						HumanAnimationController controller = (HumanAnimationController) soldier.getPersonAnimationController();
						int actualSpriteNumber = controller.getActualBodyAndHandsSpriteNumberByAttack();
						//System.out.println("Actual sprite is: " + actualSpriteNumber);
						if (actualSpriteNumber == PlayerGreenadeAnimationController.SPRITE_FOR_GRENADE_ADDING){
							float shotLenght = 20;
							PVector grenadeApearingPlace = new PVector(gameRound.getPlayer().getPixelPosition().x+shotLenght*PApplet.cos(PApplet.radians(gameRound.getPlayer().getWeaponAngle())), gameRound.getPlayer().getPixelPosition().y+shotLenght*PApplet.sin(PApplet.radians(gameRound.getPlayer().getWeaponAngle())));
							if (Program.debug) System.out.println("Player on: " + gameRound.getPlayer().getPixelPosition()+ "; Grenade place: " + grenadeApearingPlace);
							gameRound.addNewHandGreenade(this, gameRound.getPlayer().getWeaponAngle(), grenadeApearingPlace);
							grenadeAttackStarted = false;
							grenadeAttackEnded = true;
						}
					} else {
						grenadeAttackStarted = false;
						grenadeAttackEnded = true;
						soldier.recoveryGrenade();
					}
				}
				else {
					grenadeAttackStarted = false;
					grenadeAttackEnded = true;

				}
			}
		}
	  }
	  	  
	  public boolean canBeWeaponReloaded(){		  
		  if (magazineCapacity!=maxMagazineCapacity) return true;
		  else return false;
	  }
	  

	  public void attack(float dispersion, GameRound gameRound) {
		  nextShotTimer.setNewTimer(36000/firingRate);
		  float shotAngle = 0;
		  if (type == HANDGUN || type == REVOLVER) gameRound.addNewBullet(this, gameRound.getPlayer().getWeaponAngle(), gameRound.getPlayer().getPixelPosition(), owner);
		  else if (type == SMG) {
			  shotAngle = Program.engine.random(-dispersion*ASSAULT_RIFFLE_BULLETS_DISPERSION/2, dispersion*ASSAULT_RIFFLE_BULLETS_DISPERSION/2)+gameRound.getPlayer().getWeaponAngle();
				System.out.println("Dispersion: " + dispersion);
			  gameRound.addNewBullet(this, shotAngle, gameRound.getPlayer().getPixelPosition(), owner);
		  }
		  else if (type == SHOTGUN) {
			  for (int i = 0; i < SHOTGUN_BULLETS_NUMBER; i++) {
				  shotAngle = Program.engine.random(-SHOTGUN_BULLETS_DISPERSION/2, SHOTGUN_BULLETS_DISPERSION/2)+gameRound.getPlayer().getWeaponAngle();
				  gameRound.addNewBullet(this, shotAngle, gameRound.getPlayer().getPixelPosition(), owner);
			  }
		  }
		  else if (type == SO_SHOTGUN) {
			  for (int i = 0; i < SHOTGUN_BULLETS_NUMBER; i++) {
				  shotAngle = Program.engine.random(-SO_BULLETS_DISPERSION/2, SO_BULLETS_DISPERSION/2)+gameRound.getPlayer().getWeaponAngle();
				  gameRound.addNewBullet(this, shotAngle, gameRound.getPlayer().getPixelPosition(), owner);
			  }
		  }
		  else if (type == GREENADE_LAUNCHER) {
			  gameRound.addNewLaunchableGreenade(this, gameRound.getPlayer().getWeaponAngle(), gameRound.getPlayer().getPixelPosition());
		  }
		  else if (type == HAND_GREENADE) {
			  if (!grenadeAttackStarted){
				  if (grenadeAttackEnded){
						grenadeAttackEnded = false;
						grenadeAttackStarted = true;
				  }
			  }
		  }
		  magazineCapacity--;
		  if (Program.debug) System.out.println("In magazine: " + magazineCapacity + "/" + maxMagazineCapacity);
		  int weaponAngle = (int)gameRound.getPlayer().getWeaponAngle();
		  Vec2 shotPointRelativeToBodyCenter = new Vec2(2*nominalWeaponLength* PApplet.cos(PApplet.radians(weaponAngle)),2*nominalWeaponLength* PApplet.sin(PApplet.radians(weaponAngle)));
		  if (type != HAND_GREENADE ) {
			  //nimations flip statem
			  //Splash splash = new ShotFireSplash((Soldier) (gameRound.getPlayer()), shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);
			  gameRound.addFireSplash((Soldier) (gameRound.getPlayer()), shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);

		  }


		  shootingOnThisFrameStarted = true;
		  /*
		  float shotAngle = gameRound.getPlayer().getWeaponAngle();
		  shotAngle = Game2D.engine.random(-dispersion*ASSAULT_RIFFLE_BULLETS_DISPERSION/2, dispersion*ASSAULT_RIFFLE_BULLETS_DISPERSION/2)+gameRound.getPlayer().getWeaponAngle();
		  BulletRay bullet = new BulletRay(this, shotAngle, new Vec2(gameRound.getPlayer().getAbsolutePosition().x, gameRound.getPlayer().getAbsolutePosition().y));
		  gameRound.getBulletRays().add(bullet);*/
	  }
	  
	  public boolean isShootingOnThisFrameStarted(){
		return shootingOnThisFrameStarted;
	  }
	  
	 public float getNominalWeaponLength() {
		return nominalWeaponLength;
	 }
	 
	 @Override
	 public PVector getBulletEndPlace(Weapon weapon, float angle) {
			final float weaponLength = nominalWeaponLength;
			PVector startPlace = new PVector(weaponLength* Program.engine.cos((float)(Math.toRadians(angle))),
											 weaponLength* Program.engine.sin((float)(Math.toRadians(angle))));
			return startPlace;
		}
	 
	  @Override
	  public int getWeaponLength(Person person) {
		    float angle = person.getWeaponAngle();		    
		    int weaponLength = Program.engine.floor((float) (500/33.3/Math.cos(Math.toRadians(angle))));
		    if (weaponLength<0 ) weaponLength*=-1;
			return weaponLength;
		} 
	  
	  @Override
	  public int getWeaponLength(float angle) {			  
		    int weaponLength = Program.engine.floor((float) (nominalWeaponLength/Math.cos(Math.toRadians(angle))));
		    //int weaponLength = Game2D.engine.floor((float) (Game2D.engine.width/33.3/Math.cos(Math.toRadians(angle))));
		    //System.out.println("Length: " + weaponLength);
		    if (weaponLength<0 ) weaponLength*=-1;
			return weaponLength;
		} 
	  
	  
	  @Override
	  public void drawInHands(GameCamera gameCamera, Person person) {		 
		  if (person.getClass() == Soldier.class) {
			Vec2 position = PhysicGameWorld.coordWorldToPixels(person.body.getPosition().x, person.body.getPosition().y);
			float angle = person.getWeaponAngle();
			
			if (Program.debug && Program.OS == Program.DESKTOP && true==false) {
				if (Program.USE_BACKGROUND_BUFFER) {
					Program.objectsFrame.pushMatrix();
					Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
					//Programm.objectsFrame.translate(position.x-gameCamera.getActualPosition().x+ Programm.objectsFrame.width/2, position.y-gameCamera.getActualPosition().y+ Programm.objectsFrame.height/2);

					Program.objectsFrame.translate(position.x - gameCamera.getActualXPositionRelativeToCenter(), position.y - gameCamera.getActualYPositionRelativeToCenter());
					//
					Program.objectsFrame.rotate((float)Math.toRadians(angle));
					Program.objectsFrame.pushStyle();
					if (type == HANDGUN) {
						Program.objectsFrame.strokeWeight(1.5f);
						Program.objectsFrame.stroke(20,145,145);
					}
					else if (type == SMG) {
						Program.objectsFrame.strokeWeight(2.5f);
						Program.objectsFrame.stroke(100,105,45);
					}
					else if (type == SHOTGUN) {
						Program.objectsFrame.strokeWeight(4.5f);
						Program.objectsFrame.stroke(220,045,145);
					}
					else if (type == GREENADE_LAUNCHER) {
						Program.objectsFrame.strokeWeight(9.5f);
						Program.objectsFrame.stroke(20,245,45);
					}
					Program.objectsFrame.line(0, 0, getWeaponLength(person), 0);;
					Program.objectsFrame.popStyle();
					Program.objectsFrame.popMatrix();
					//Programm.objectsFrame.endDraw();
				}
			}			
		  }		  
	  }

    public void fillMagazine() {
		magazineCapacity = maxMagazineCapacity;
    }
}

