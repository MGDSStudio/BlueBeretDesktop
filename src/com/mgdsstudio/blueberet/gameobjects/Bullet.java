package com.mgdsstudio.blueberet.gameobjects;


import com.mgdsstudio.blueberet.classestoberemoved.BulletRayCast;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.RayCastNew;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PVector;

public class Bullet extends GameObject implements ISimpleUpdateable, IDrawable{
	private final String objectToDisplayName = "Bullet";
	private static StaticSprite sprite;
	private static boolean graphicLoaded;
	//public Body body;
	//private final static int BULLET_NORMAL_START_VELOCITY = 300;


	private final static int BULLET_NORMAL_START_VELOCITY_FOR_PISTOLE = 4;
	//private final static int BULLET_NORMAL_START_VELOCITY_FOR_PISTOLE_BY_BULLET_TIME = BULLET_NORMAL_START_VELOCITY_FOR_PISTOLE/2;

	private final static float BULLET_TIME_VELOCITY_COEF_FOR_PISTOLE = 0.5f;

	private final static int BULLET_NORMAL_START_VELOCITY = 20;
	//private final static int BULLET_NORMAL_START_VELOCITY_BY_BULLET_TIME = 6;

	private final static float BULLET_TIME_VELOCITY_COEF = 0.25f;
	private final static float BULLET_TIME_MASS_COEFF = 0.75f;

	int bulletStartSpeed;
	float bulletDensity;
	public int fromWeapon = 1;
	public int shotStartingFrame;
	//private boolean started;
	private Weapon weapon;
	private float lastAngle;
	private int startAngle;
	//float startAngle;
	private PVector shotPosition;
	public static final int MAX_FLYING_TIME = 2000; // Milliseconds
	private int bulletDeletingMoment;
	public final static String BULLET = "Bullet";
	
	// Mass categories. If the first bullet point is already in some fixture, the bullet has a reduced mass to prevent big start impulse on this body
	public static final boolean NOT_NULL_MASS = true;
	public static final boolean REDUCED_MASS = false;
	private boolean massCategory = NOT_NULL_MASS;

	private BulletRayCast rayCast;
	private RayCastNew rayCastNew;

	private boolean active;

	private Person owner;


	public Bullet(Person owner){
		init(null, 0f, new PVector(0,0), NOT_NULL_MASS, false, owner);
		body.setActive(false);
		active = false;
		setSleeped(true);
		loadGraphic();
	}

	private void loadGraphic() {
		if (!graphicLoaded){
			String path = HeadsUpDisplay.mainGraphicSource.getPath();
			float coef =1f;
			final int graphicWidth = (int) (15*coef);
			final int graphicHeight = (int) (4*coef);
			ImageZoneFullData imageZoneFullData = new ImageZoneFullData(path, 726,1, 748,7);
			//ImageZoneFullData imageZoneFullData = new ImageZoneFullData(path, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
			//ImageZoneFullData imageZoneFullData = ;
			sprite = new StaticSprite(imageZoneFullData, graphicWidth, graphicHeight);
			sprite.loadSprite(HeadsUpDisplay.mainGraphicTileset);
			if (HeadsUpDisplay.mainGraphicTileset != null) {
				graphicLoaded = true;
				System.out.println("Graphic for bullet was loaded: " + (HeadsUpDisplay.mainGraphicTileset == null));
			}
		}
	}



	public Bullet(Weapon weapon, float startAngle, PVector shotPosition, boolean massCategory, boolean bulletTime, Person owner){
		init(weapon, startAngle, shotPosition, massCategory, bulletTime, owner);
	}

	public void createFromPool(Weapon weapon, float startAngle, PVector shotPosition, boolean massCategory, boolean bulletTime, Person owner){
		init(weapon, startAngle, shotPosition, massCategory, bulletTime, owner);
		active = true;
		body.setActive(true);
		setSleeped(false);
		loadGraphic();
	}

	public boolean isActive(){
		return active;
	}

	private void init(Weapon weapon, float startAngle, PVector shotPosition, boolean massCategory, boolean bulletTimeActivated, Person owner){
		this.owner = owner;
		if (weapon == null)	weapon = new FirearmsWeapon(Weapon.HANDGUN, owner);
		this.weapon = weapon;
		if (this.shotPosition == null){
			this.shotPosition = new PVector(shotPosition.x, shotPosition.y);
		}
		else {
			this.shotPosition.x = shotPosition.x;
			this.shotPosition.y = shotPosition.y;
		}
		fromWeapon = weapon.type;
		this.massCategory = massCategory;
		if (weapon.getClass() == FirearmsWeapon.class) {
			if (weapon.type == FirearmsWeapon.HANDGUN || weapon.type == FirearmsWeapon.REVOLVER) {
				if (massCategory == NOT_NULL_MASS) {
					float normalMassForPistole = 4.33f;
					if (bulletTimeActivated){
						bulletStartSpeed = (int) ((float) BULLET_NORMAL_START_VELOCITY_FOR_PISTOLE * BULLET_TIME_VELOCITY_COEF_FOR_PISTOLE);        // Speed by start to prevent shot in your leg
						bulletDensity = normalMassForPistole*BULLET_TIME_MASS_COEFF;	// Speed by start to prevent shot in your leg
					}
					else {
						bulletStartSpeed = BULLET_NORMAL_START_VELOCITY_FOR_PISTOLE;        // Speed by start to prevent shot in your leg
						bulletDensity = normalMassForPistole;    // Speed by start to prevent shot in your leg
					}
				}
				else {
					bulletStartSpeed = 1;		// Speed by start to prevent shot in your leg
					bulletDensity = 0.001f;	// Speed by start to prevent shot in your leg
				}
			}
			else {
				if (massCategory == NOT_NULL_MASS) {
					float normalMass = 3.33f;
					if (bulletTimeActivated){

						bulletStartSpeed = (int) ((float)BULLET_NORMAL_START_VELOCITY*BULLET_TIME_VELOCITY_COEF);		// Speed by start to prevent shot in your leg
						bulletDensity = normalMass*BULLET_TIME_MASS_COEFF;	// Speed by start to prevent shot in your leg
						//System.out.println("Bullet by bulllet time !");
					}
					else {
						bulletStartSpeed = BULLET_NORMAL_START_VELOCITY;        // Speed by start to prevent shot in your leg
						bulletDensity = normalMass;    // Speed by start to prevent shot in your leg
					}
				}
				else {
					bulletStartSpeed = 1;		// Speed by start to prevent shot in your leg
					bulletDensity = 0.001f;	// Speed by start to prevent shot in your leg
				}
			}
			shotStartingFrame = Program.engine.frameCount+1;
			makeBullet(startAngle, shotPosition);
		}
		bulletDeletingMoment = Program.engine.millis()+MAX_FLYING_TIME;
		//System.out.println("was body.m_mass" + body.m_mass);
		lastAngle = startAngle;
		this.startAngle = (int) startAngle;
		//setFilterData(COALISION_PLAYER_WITH_BULLET_GROUP);
		//setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_BULLET);
		float shotLength = Program.engine.height;
		PVector shotEndPos = new PVector(shotPosition.x, shotPosition.y);
		shotEndPos.x+=shotLength*PApplet.cos(PApplet.radians(startAngle));
		shotEndPos.y+=shotLength*PApplet.sin(PApplet.radians(startAngle));
		/*
		RayCastCallback rayCastCallback = null;
		System.out.println("***** Shot start: " + shotPosition.x + "x" + shotPosition.y);
		System.out.println("***** Shot end: " + shotEndPos.x + "x" + shotEndPos.y);

		rayCast = new BulletRayCast();
		RaycastResult  result = new RaycastResult();
		RayCastInput input = new RayCastInput();
		//PhysicGameWorld.controller.world.raycast(rayCast, PhysicGameWorld.controller.coordPixelsToWorld(shotPosition), PhysicGameWorld.controller.coordPixelsToWorld(shotPosition));
		rayCastNew = new RayCastNew();
		rayCastNew.init();
		rayCastNew.calculateRaycast(PhysicGameWorld.controller.coordPixelsToWorld(shotPosition), PhysicGameWorld.controller.coordPixelsToWorld(shotPosition));*/
	}

	private void setInactive(boolean flag){
		active = flag;
		body.setActive(!flag);
		setSleeped(!flag);
	}

	public void setActive(boolean flag){
		active = flag;
		body.setActive(flag);
		setSleeped(!flag);
	}

	public PVector getShotPosition() {
		return shotPosition;
	}



	/*
	private void setFilterData(byte group) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.setFilterData(filter);
	}*/
	
	public boolean mustBeBulletDeletedAfterLongFlight() {
		if (bulletDeletingMoment< Program.engine.millis()) return true;
		else return false;
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

	public int getStartAngle(){
		return startAngle;
	}

	public int framesAfterShot() {
		return (int) (Program.engine.frameCount-shotStartingFrame);
	}
	
	public void reduceMassAndVelocity() {
		bulletStartSpeed = 300;	// was 1
		bulletDensity = 4.33f;
		body.getFixtureList().setDensity(bulletDensity);
		Vec2 linearVelocity = body.getLinearVelocity();	
		float angle = body.getAngle();
		final float bulletSpeed = PhysicGameWorld.controller.scalarWorldToPixels(bulletStartSpeed);		
		Vec2 shootingVector = new Vec2((float)(bulletSpeed*Math.cos(Math.toRadians(angle))), -(float)(bulletSpeed*Math.sin(Math.toRadians(angle))));	    
		body.setLinearVelocity(shootingVector);
	}
	
	public void reduceVelocity() {
		bulletStartSpeed = 600;	// was 1
		//bulletDensity = 0.01f;
		//body.getFixtureList().setDensity(bulletDensity);
		//Vec2 linearVelocity = body.getLinearVelocity();	
		float angle = body.getAngle();
		final float bulletSpeed = PhysicGameWorld.controller.scalarWorldToPixels(bulletStartSpeed);		
		Vec2 shootingVector = new Vec2((float)(bulletSpeed*Math.cos(Math.toRadians(angle))), -(float)(bulletSpeed*Math.sin(Math.toRadians(angle))));	    
		body.setLinearVelocity(shootingVector);
	}
	
	private void makeBullet(float angle, PVector shotPosition) {
		final int bulletRadius;
		if (fromWeapon == FirearmsWeapon.HANDGUN) {
			bulletRadius = 4;
		}
		else if (fromWeapon == FirearmsWeapon.SHOTGUN){
			bulletRadius = 4;
		}
		else bulletRadius = 4;
		final float weaponLength = weapon.getWeaponLength(angle);
		shotPosition.x+= weaponLength*Math.cos((float)Math.toRadians(angle));
		shotPosition.y+= weaponLength*Math.sin((float)Math.toRadians(angle));		
		// Define a body
	    BodyDef bd = new BodyDef();
	    // Set its position
	    bd.position = PhysicGameWorld.controller.coordPixelsToWorld(shotPosition.x, shotPosition.y);
	    bd.type = BodyType.DYNAMIC;
	    bd.bullet = true;
	    body = PhysicGameWorld.controller.createBody(bd);
	    // Make the body's shape a circle
	    CircleShape cs = new CircleShape();
	    cs.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(bulletRadius);
	    FixtureDef fd = new FixtureDef();
	    fd.shape = cs;
	    fd.density = bulletDensity;
	    fd.friction = 0.0001f;	//was 0.1f
	    fd.restitution = 0.3f; // was 0.3f
	    // Attach fixture to body
	    body.createFixture(fd);

		float testVel = 0.001f;
		//final float bulletSpeed = PhysicGameWorld.controller.scalarWorldToPixels(testVel);
		final float bulletSpeed = PhysicGameWorld.controller.scalarWorldToPixels(bulletStartSpeed);
	    Vec2 shootingVector = new Vec2((float)(bulletSpeed*Math.cos(Math.toRadians(angle))), -(float)(bulletSpeed*Math.sin(Math.toRadians(angle))));
	    
	    //System.out.print("Start speed is: "  +  shootingVector);
	    body.setLinearVelocity(shootingVector);	    
	    //body.setBullet(true);
	    body.getFixtureList().setSensor(false);

		//System.out.println("Bullet were no sensors");
	    float gravityScale = 0.05f;
	    if (fromWeapon == FirearmsWeapon.HANDGUN) {
	    	gravityScale = 0.05f;
		}
		else if (fromWeapon == FirearmsWeapon.SHOTGUN){
			gravityScale = 0.45f;
		}
		//gravityScale = 0.25f;
	    body.setGravityScale(gravityScale);
		body.setUserData(BULLET);


		/*
		BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape head = new CircleShape();
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(radius);
        body.createFixture(head, 0.5f);
        body.setFixedRotation(false);
        body.getFixtureList().setFriction(0.99f);
        body.setAngularDamping(0.99f);
		*/


	}
	
	@Override
	public void update() {
		//saveActualAngle();
	}

	private void saveActualAngle() {
		try {
			lastAngle = new PVector(body.getLinearVelocity().x, body.getLinearVelocity().y).heading();
		}
		catch (Exception e){
			System.out.println("can not set angle");
		}
	}

	@Override
	public void draw(GameCamera gameCamera) {
		//if (isObjectCenterInVisibleZone(gameCamera)) {
		//System.out.println("Bullet was drawn");
		if (active) {

			/*
			if (Program.debug || PhysicGameWorld.bulletTime) {
				Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
				float a = PApplet.radians(-startAngle);
				sprite.draw(gameCamera,pos,a);
			}*/
			if (Program.OS == Program.DESKTOP || PhysicGameWorld.bulletTime) {
				Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
				float a = PApplet.radians(-startAngle);
				sprite.draw(gameCamera,pos,a);
			}
		}
		else {
			//System.out.println("Bullet can not be drawn");
		}
	//}
	}

	private void drawSprite(GameCamera gameCamera) {
		if (sprite.getImage().getImage() == null) System.out.println("Sprite is null");
		else System.out.println("Sprite drawn");
		//Programm.objectsFrame.image(sprite.getImage().getImage(), 0,0, sp);

	}

	public float getLastAngleInDegrees(){
		//return PApplet.degrees(lastAngle);
		return lastAngle;
	}

	public void setVelocityForBulletTimeMode() {
		Vec2 normalVelocity = body.getLinearVelocity();
		if (fromWeapon == Weapon.HANDGUN || fromWeapon == Weapon.REVOLVER){
			normalVelocity.mul(BULLET_TIME_VELOCITY_COEF_FOR_PISTOLE);
		}
		else normalVelocity.mul(BULLET_TIME_VELOCITY_COEF);

	}

	public void setNormalVelocity() {
		Vec2 bulletTimeVelocity = body.getLinearVelocity();
		float velCoef;
		if (fromWeapon == Weapon.HANDGUN || fromWeapon == Weapon.REVOLVER){
			velCoef = 1f/BULLET_TIME_VELOCITY_COEF_FOR_PISTOLE;
			bulletTimeVelocity.mul(velCoef);
		}
		else {
			velCoef = 1f/BULLET_TIME_VELOCITY_COEF;
			bulletTimeVelocity.mul(velCoef);
		}

	}

	public Person getOwner() {
		return owner;
	}
}
