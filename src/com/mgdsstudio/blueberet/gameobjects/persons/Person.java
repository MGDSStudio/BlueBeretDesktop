package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamecontrollers.WallInFrontOfPersonDeterminingController;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;
import processing.core.PApplet;

import java.util.ArrayList;

public abstract class Person extends GameObject {
	//public
	public ArrayList<FirearmsWeapon> weapons = new ArrayList<FirearmsWeapon>();
	public GlobalAI_Controller globalAIController;

	protected WallInFrontOfPersonDeterminingController wallInFrontOfPersonDeterminingController;
	// orientation
	public final static boolean TO_LEFT = false;
	public final static boolean TO_RIGHT = true;
	// For movement
	public boolean orientation = TO_RIGHT;
	// For shooting for the Bowser
	protected boolean sightDirection = TO_LEFT;
	
	public byte role;
	protected float weaponAngle;
	//public final static boolean GOING_MOVEMENT = false;
	//public final static boolean RUNNING_MOVEMENT = true;

	Timer timer, changingDirectionTimer;
	private boolean landedOnThisFrame;



	private float previsiousLinearSpeedAlongY = 0f;	// to determine that the player is in the air
	private float landingVelocity = 0f;	// to determine that the player is in the air
	private float previsiousLinearSpeedAlongX = 0f;	// to determine that the player is braking
	protected float normalAccelerate = 30;
	protected float actualAccelerate = normalAccelerate;
	public float maxVelocityAlongX = 8f;	//In World coordinates
	private float maxVelocityAlongXToBeStaying = 0.2f;
	public float maxVelocityAlongXByRunning = maxVelocityAlongX*2.4f;	//In World coordinates
	protected float jumpMaxHeight = 15;
	protected float jumpStartSpeed = 33f* Program.NORMAL_FPS/30f;
	protected float reboundStartSpeed = jumpStartSpeed/2.5f;
    //private boolean stopping = true;
    protected boolean moovingDirectionIsChanging = false;
	
    //Statement relative to the earth
	public final static byte ON_GROUND = 1;
	public final static byte IN_AIR = 2;
	public final static byte ON_MOVEABLE_PLATFORM = 3;
	protected byte statement = IN_AIR;
	protected byte previousStatement = statement;
	
	//Relationship to player
	public final static byte PLAYER = 1;
    public final static byte ALLY = 2;
    public final static byte NEUTRAL_CHARACTER = 3;
    public final static byte ENEMY = 4;
    public final static byte FRIEND = 5; 
    
    // Movement options
    public final static byte STAYING = 0;
    public final static byte ACCELERATING_FROM_LEFT_TO_RIGHT = 1;
    public final static byte ACCELERATING_FROM_RIGHT_TO_LEFT = 2;
    public final static byte BRAKING_FROM_LEFT_TO_RIGHT = 3;
    public final static byte BRAKING_FROM_RIGHT_TO_LEFT = 4;
    private byte accelerateStatement = STAYING;
	
	final static public float MAX_SPEED_BY_BLOCKED_STATEMENT = 2.15f;
	//final static public float MAX_SPEED_BY_BLOCKED_STATEMENT = 4.15f;
	
	// Levels for Koopas and Bowsers
	public final static byte FLYING = 4;
	public final static byte JUMPING = 3;
	public final static byte GOING = 2;
	public final static byte SITTING = 1;
	public final static byte WITHOUT_LEVEL_CHANGING = 0;
	public final static byte PATROL = 5;
	public byte level = WITHOUT_LEVEL_CHANGING;
	
	
	
	protected boolean rolledOver = false; 
	//protected int maxLife = 400;
	protected boolean playerFounded = false;

	public final static boolean GO_MOVEMENT = false;
	public final static boolean RUN_MOVEMENT = true;
	
	final float criticalSpeedAlongY = 350/116;	// To know, is person on the ground or in air

	protected Timer underAttackTimer;
	protected boolean underAttack = false;
	protected int timeAfterAttackInGodMode = 1600;
	protected boolean jumpAfterAttack = false;
	protected final boolean godMode = false;
	protected boolean controlBlocked;	//By hitting or by pause by dialogs
	protected boolean attackAbilityBlocked;
	protected int attackBlockingTimeAfterBeenHitted = 1500;
	protected Timer afterHittingAttackBlockingTimer ;
	protected boolean attackStartedOnThisFrame;
	protected boolean attackStartedOnPrevFrame;

	protected int actualByUserPressedMovement = PlayerControl.USER_DOESNOT_TOUCH_STICK;
	private final boolean moveObjectsUnderPerson = true;
	private final Vec2 mutMaxVelocity = new Vec2();
	private final Vec2 mutAccelerate = new Vec2();
	private final Vec2 mutForce = new Vec2();
	private final Vec2 mutJumpVector = new Vec2(0,0);
	private final Vec2 mutMoveVelocity = new Vec2(0, 0f);

	private int timeToDetermineLanding = 50;
	//private Body lowerLayingBody;

	protected void findPlayerAtStart(){
		if (Program.gameStatement == Program.GAME_PROCESS) {
			ArrayList<Body> bodies = PhysicGameWorld.getBodies();
			for (int i = 0; i < bodies.size(); i++) {
				if (bodies.get(i).getUserData() != null) {
					if (bodies.get(i).getUserData().equals(Soldier.CLASS_NAME)) {
						float xSoldier = PhysicGameWorld.controller.getBodyPixelCoord(bodies.get(i)).x;
						if (xSoldier > getPixelPosition().x) {
							orientation = TO_RIGHT;
						} else {
							orientation = TO_LEFT;

						}
					}
				}
			}
			//GameObject object = PhysicGameWorld.getGameObjectByBody();
		}
	}

	protected boolean startMovementDirectionToPlayerWasSet =false;

	public void update() {
		if (attackStartedOnThisFrame && !attackStartedOnPrevFrame) attackStartedOnPrevFrame = true;
		else if (attackStartedOnPrevFrame) {
			attackStartedOnThisFrame = false;
			attackStartedOnPrevFrame = false;
		}
		updateStatement();
		if (attackAbilityBlocked){
			if (afterHittingAttackBlockingTimer.isTime()){
				afterHittingAttackBlockingTimer.stop();
				attackAbilityBlocked = false;
				System.out.println(this.getClass() + " can attack now");
			}
		}
	}


	void goRight() {
		// TODO Auto-generated method stub		
	}

	void goLeft() {
		// TODO Auto-generated method stub		
	}

	public abstract void loadAnimationData(MainGraphicController mainGraphicController);

	public abstract PersonAnimationController getPersonAnimationController();
	
	public void createAI(GlobalAI_Controller globalAIController) {
		this.globalAIController = globalAIController;
	}
	
	public abstract void draw(GameCamera gameCamera) ;
	/*
	public void draw(GameCamera gameCamera) {
		System.out.println("This person must override draw() method");
	}
	 */

	
	public FirearmsWeapon getActualWeapon() {
		if (weapons.size()>0) {
			for (FirearmsWeapon weapon : weapons) {
				if (weapon.isActual()) {
					return weapon;
				}
			}
			if (Program.debug) System.out.println("No weapon is set as actual");
			return null;
		}
		else {
			System.out.println("This person has no weapon or the weapon was not initialized yet");
			return null;
		}
	}

	protected void updateStatement() {
		previousStatement = statement;
		float verticalVelocity = body.getLinearVelocity().y;

		if ((((verticalVelocity >= (-criticalSpeedAlongY) && verticalVelocity <= 0) || (verticalVelocity <= criticalSpeedAlongY && verticalVelocity >= 0)) && statement != ON_MOVEABLE_PLATFORM) || areThereObjectsUnderPerson()) {
			if (timer == null) {
				timer = new Timer(timeToDetermineLanding);
			}
			else if (timer.isSwitchedOff()) timer.setNewTimer(timeToDetermineLanding);
			else if (timer.isTime() && statement != ON_GROUND) {
				if ((Math.abs((verticalVelocity - previsiousLinearSpeedAlongY)) < 0.1f)) {
					mutMoveVelocity.x = body.getLinearVelocity().x;
					//mutMoveVelocity.y = (0f);
					body.setLinearVelocity(mutMoveVelocity);
					statement = ON_GROUND;
					timer.stop();
					try {
						gameRound.getSoundController().setAndPlayAudio(SoundsInGame.JUMP_ENDS);
					}
					catch (Exception e){
						System.out.println("Can not play audio for person landing. " + e);
					}
					System.out.println("Person is landed");
					landedOnThisFrame = true;
					if (jumpAfterAttack && controlBlocked) {
						controlBlocked = false;
						jumpAfterAttack = false;
					}
					//if (jumpAfterAttack) jumpAfterAttack = false;
				}
				else {
					landingVelocity = previsiousLinearSpeedAlongY;
				}
			}
			else landedOnThisFrame = false;
			//else if (!timer.isTime()) timer.setNewTimer(130);

			previsiousLinearSpeedAlongY = verticalVelocity;
		}
		else {
			if (landedOnThisFrame == true) landedOnThisFrame = false;
			boolean statementWasChanged = false;
			if (statement == ON_MOVEABLE_PLATFORM) {
				for (Contact contact : PhysicGameWorld.endContacts) {
					Fixture f1 = contact.getFixtureA();
					Fixture f2 = contact.getFixtureB();			
					Body b1 = f1.getBody();
					Body b2 = f2.getBody();	
					if (((b1.equals(body) && b2.getType() == BodyType.KINEMATIC)) ||
						(b2.equals(body) && b1.getType() == BodyType.KINEMATIC)) {
						statement = ON_MOVEABLE_PLATFORM;
						statementWasChanged = true;
						previsiousLinearSpeedAlongY = 0;
						return;
					}
				}
			}
			if (!statementWasChanged) {
				for (Contact contact : PhysicGameWorld.beginContacts) {
					Fixture f1 = contact.getFixtureA();
					Fixture f2 = contact.getFixtureB();			
					Body b1 = f1.getBody();
					Body b2 = f2.getBody();
					try {
						if (((b1.equals(body) && b2.getType() == BodyType.KINEMATIC)) ||
								(b2.equals(body) && b1.getType() == BodyType.KINEMATIC)) {
							statement = ON_MOVEABLE_PLATFORM;
							previsiousLinearSpeedAlongY = 0;
							statementWasChanged = true;
							return;
						}
					}
					catch (Exception e){
						System.out.println("Some fixtures were deleted. " + e);
					}
				}
			}
			if (!statementWasChanged) {
				//boolean personIsNotMoreCollideWithPlatform = false;
				if (statement == ON_MOVEABLE_PLATFORM) {
					for (Contact contact : PhysicGameWorld.endContacts) {
						Fixture f1 = contact.getFixtureA();
						Fixture f2 = contact.getFixtureB();			
						Body b1 = f1.getBody();
						Body b2 = f2.getBody();	
						if (((b1.equals(body) && b2.getType() == BodyType.KINEMATIC)) ||
							(b2.equals(body) && b1.getType() == BodyType.KINEMATIC)) {
							statement = IN_AIR;
							previsiousLinearSpeedAlongY = 0;	
							statementWasChanged = true;
							return;				
						}
					}
					for (Contact contact : PhysicGameWorld.endContacts) {
						Fixture f1 = contact.getFixtureA();
						Fixture f2 = contact.getFixtureB();			
						Body b1 = f1.getBody();
						Body b2 = f2.getBody();	
						if (((b1.equals(body) && b2.getType() == BodyType.KINEMATIC)) ||
							(b2.equals(body) && b1.getType() == BodyType.KINEMATIC)) {
							statement = IN_AIR;
							previsiousLinearSpeedAlongY = 0;	
							statementWasChanged = true;
							return;				
						}
					}
				}
				else {
					statement = IN_AIR;
					previsiousLinearSpeedAlongY = verticalVelocity;	
				}
			}
		}
		//System.out.println("Jump after attack: " + jumpAfterAttack);
		updateAccelerateStatement();
	}

	protected boolean areThereObjectsUnderPerson() {
		return false;
	}

	@Override
	public void kill(){
		super.kill();
		setFilterDataForCategory(CollisionFilterCreator.CATEGORY_CORPSE);
		//enemiesAnimationController.setDead(true);
	}
	
	
	private void updateAccelerateStatement() {		
	}
	
	public float getAccelerateAlongX() {
		return (body.getLinearVelocity().x-previsiousLinearSpeedAlongX); // if > 0 - accelerate from left to right; if 
	}
	
	public boolean hasPersonNormalAngleToBeActive() {
		if (GameMechanics.convertRadiansToDegrees(body.getAngle()) < 12) {
			return true;
		}
		else return false;
	}
	
	protected abstract void updateAngle();

	private void setStatement(byte statement) {
		this.statement = statement;
	}

	public float getWeaponAngle() {
		//if (this.getClass() == Human.class) {
		if (this instanceof Human) {
			return weaponAngle;
		}		
		else {
			System.out.println("This person has no weapon to get angle");
			return weaponAngle;
		}
	}

	public void setWeaponAngle(float angle) {
		System.out.println("This function setWeaponAngle must be overriden");
	}

	public byte getStatement() {
		// TODO Auto-generated method stub
		return statement;
	}

	public byte getPreviousStatement(){
		return previousStatement;
	}

	public boolean isLanded(){
		//if ((statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) && previousStatement == IN_AIR){

		/*if ((statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) && (previousStatement == IN_AIR)){
			System.out.println("Person from class " + this.getClass() + " is landed");
			return true;
		}
		else return false;*/
		if (landedOnThisFrame) return true;
		else return false;
	}

	public boolean isLandedOld(){
		//if ((statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) && previousStatement == IN_AIR){

		if ((statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) && (previousStatement == IN_AIR)){
			//System.out.println("Person from class " + this.getClass() + " is landed");
			return true;
		}
		else return false;
	}

	public void makeJump() {
		if (!dead) {
			mutJumpVector.x = body.getLinearVelocity().x;
			mutJumpVector.y = jumpStartSpeed;
			body.setLinearVelocity(mutJumpVector);
			setStatement(Person.IN_AIR);
		}
	}

	public boolean isPersonRunning(){
		if (Program.engine.abs(body.getLinearVelocity().x)< maxVelocityAlongXByRunning){
			//System.out.println("Velocity == " + Programm.engine.abs(body.getLinearVelocity().x) + "; MAx: " + maxRunAlongX);
			return false;
		}
		else return true;
	}

	public boolean isPersonStaying(){
		if (Program.engine.abs(body.getLinearVelocity().x)<maxVelocityAlongXToBeStaying){
			return true;
		}
		else return false;
	}

	private void moveObjectsUnderPerson(boolean direction){
		/*
		boolean frictionReset = false;
		if (moveObjectsUnderPerson){
			if (lowerLayingBody != null){
				if (!lowerLayingBody.equals(body)){
					setNoFriction();
					frictionReset = true;
					//lowerLayingBody.setType(BodyType.STATIC);
					//System.out.println("Body set as static");
				}
			}
		}
		if (!frictionReset) setNormalFriction();*/
	}

	protected void setNoFriction(){
		body.getFixtureList().setFriction(0.0f);
		System.out.println("Friction set on 0");
		/*
		for (Fixture f = body.getFixtureList(); f!= null; f.getNext()){
			f.setFriction(0.01f);
			System.out.println("Friction set on 0");
		}*/
	}

	protected void setNormalFriction(){
		body.getFixtureList().setFriction(5.01f);
		System.out.println("Friction set on 5");
		/*
		for (Fixture f = body.getFixtureList(); f!= null; f.getNext()){
			f.setFriction(5.01f);
			System.out.println("Friction set on 5");
		}*/
	}

	private void moveUpToMaxVelocityWithoutLowerObjectsBraking(boolean direction, boolean movementType, boolean withAccelerate){
		if (body.isFixedRotation()) {
			float maxVelocity = 0;
			if (movementType == GO_MOVEMENT) maxVelocity = maxVelocityAlongX;
			else if (movementType == RUN_MOVEMENT) {
				maxVelocity = maxVelocityAlongXByRunning;
			}
			if (!dead) {
				if (direction == TO_LEFT && body.getLinearVelocity().x > (-maxVelocity)) {
					if (withAccelerate)	{
						mutForce.x = -actualAccelerate * body.getMass();
						body.applyForceToCenter(mutForce);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=-maxVelocity;
						body.setLinearVelocity(velocity);
					}
					if (orientation == TO_RIGHT) {
						moovingDirectionIsChanging = true;
						orientation = TO_LEFT;
					}
				} else if (direction == TO_RIGHT && body.getLinearVelocity().x < maxVelocity) {
					if (withAccelerate)	{
						mutForce.x = actualAccelerate * body.getMass();
						body.applyForceToCenter(mutForce);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=maxVelocity;
						body.setLinearVelocity(velocity);
					}
					if (orientation == TO_LEFT) {
						moovingDirectionIsChanging = true;
						orientation = TO_RIGHT;
					}
				} else if (moovingDirectionIsChanging == true) {
					moovingDirectionIsChanging = false;
				}
				if (body.getLinearVelocity().x > maxVelocity) {
					body.getLinearVelocity().x = maxVelocity;
					//body.setLinearVelocity(new Vec2(maxVelocity, body.getLinearVelocity().y));
				}
				if (body.getLinearVelocity().x < (-maxVelocity)) {
					body.getLinearVelocity().x = -maxVelocity;
					//body.setLinearVelocity(new Vec2(-maxVelocity, body.getLinearVelocity().y));
				}
				previsiousLinearSpeedAlongX = body.getLinearVelocity().x;
			}
		}
		else {
			//if (personAnimationController.getSpriteAnimation().stop())
			//System.out.println("It is stopping");
		}
	}

	private void moveUpToMaxVelocity(boolean direction, boolean movementType, boolean withAccelerate){
		if (body.isFixedRotation()) {
			float maxVelocity = 0;
			if (movementType == GO_MOVEMENT) maxVelocity = maxVelocityAlongX;
			else if (movementType == RUN_MOVEMENT) {
				maxVelocity = maxVelocityAlongXByRunning;
			}
			if (!dead) {
				if (direction == TO_LEFT && body.getLinearVelocity().x > (-maxVelocity)) {
					if (withAccelerate)	{
						mutForce.x = -actualAccelerate * body.getMass();
						body.applyForceToCenter(mutForce);
						//System.out.println("Go left!");
						returnLowerObjects(direction, movementType,withAccelerate);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=-maxVelocity;
						//body.setLinearVelocity(velocity);
						returnLowerObjects(direction, movementType,withAccelerate);
					}
					if (orientation == TO_RIGHT) {
						moovingDirectionIsChanging = true;
						orientation = TO_LEFT;
					}
				} else if (direction == TO_RIGHT && body.getLinearVelocity().x < maxVelocity) {
					if (withAccelerate)	{
						mutForce.x = actualAccelerate * body.getMass();
						body.applyForceToCenter(mutForce);
						returnLowerObjects(direction, movementType,withAccelerate);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=maxVelocity;
						//body.setLinearVelocity(velocity);
						returnLowerObjects(direction, movementType,withAccelerate);
					}
					if (orientation == TO_LEFT) {
						moovingDirectionIsChanging = true;
						orientation = TO_RIGHT;
					}
				} else if (moovingDirectionIsChanging == true) {
					moovingDirectionIsChanging = false;
				}
				if (body.getLinearVelocity().x > maxVelocity) {
					body.getLinearVelocity().x = maxVelocity;
					//body.setLinearVelocity(new Vec2(maxVelocity, body.getLinearVelocity().y));
				}
				if (body.getLinearVelocity().x < (-maxVelocity)) {
					body.getLinearVelocity().x = -maxVelocity;
					//body.setLinearVelocity(new Vec2(-maxVelocity, body.getLinearVelocity().y));
				}
				previsiousLinearSpeedAlongX = body.getLinearVelocity().x;
			}
		}
		else {
			//if (personAnimationController.getSpriteAnimation().stop())
			//System.out.println("It is stopping");
		}
	}

	private void returnLowerObjects(boolean direction, boolean movementType, boolean withAccelerate) {
		ContactEdge edge = body.getContactList();
		//int i = 0;
		try {
			if (edge != null) {
				for (Contact contact = edge.contact; contact != null; contact = contact.getNext()) {
					//i++;
					Body b1 = contact.getFixtureA().getBody();
					Body b2 = contact.getFixtureB().getBody();
					if (b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC) {
						//System.out.println("All bodies are dynamic");
						if (b1.equals(body)) {
							if (!hasBodyCircleFixtures(b2))	returnObjectBack(b1, b2, contact);
						} else if (b2.equals(body)) {
							if (!hasBodyCircleFixtures(b1))	returnObjectBack(b2, b1, contact);
						}
						else {
							//System.out.println("Pos b1: " + PhysicGameWorld.coordWorldToPixels(b1.getPosition().x, b1.getPosition().y) + "x b2: " + PhysicGameWorld.coordWorldToPixels(b2.getPosition().x, b2.getPosition().y) + " but player at: " + PhysicGameWorld.coordWorldToPixels(body.getPosition().x, body.getPosition().y));
						}
					}
				}
			}
		}
		catch (Exception e){
			if (Program.debug) e.printStackTrace();
		}
		//System.out.println("Body has " + i + " edges" );
	}

	private boolean hasBodyCircleFixtures(Body b) {
		/*
		for (Fixture fixture = b.getFixtureList(); fixture != null; fixture= fixture.getNext()){
			if (fixture.getShape().getType() == ShapeType.CIRCLE) return true;
		}*/
		return false;
	}

	private void returnObjectBack(Body personBody, Body objectBody, Contact contact) {

		if (personBody.getPosition().y>objectBody.getPosition().y){	//Object is under relative Y. Relative X is not tested
			Manifold manifold = contact.getManifold();
			if (manifold.pointCount == 1){
				float relativeMassCoef = 0.25f*personBody.getMass()/objectBody.getMass();
				float xVel = personBody.getLinearVelocity().x;
				objectBody.getLinearVelocity().x -= (xVel)*relativeMassCoef;
				//System.out.println("Object comes back. Relative mass:  " + relativeMassCoef);
			}
			//else System.out.println("Manifold points: " + manifold.pointCount);
			/*Manifold.ManifoldType type = manifold.type;
			if (type == Manifold.ManifoldType.CIRCLES)*/

		}

	}

	/*
	private void moveUpToMaxVelocity(boolean direction, boolean movementType, boolean withAccelerate){
		if (body.isFixedRotation()) {
			float maxVelocity = 0;
			if (movementType == GO_MOVEMENT) maxVelocity = maxVelocityAlongX;
			else if (movementType == RUN_MOVEMENT) {
				maxVelocity = maxRunAlongX;
			}
			if (!dead) {
				if (direction == TO_LEFT && body.getLinearVelocity().x > (-maxVelocity)) {
					if (withAccelerate)	{
						body.applyForceToCenter(new Vec2(-actualAccelerate * body.getMass(), 0));


					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=-maxVelocity;
						body.setLinearVelocity(velocity);
					}
					if (orientation == TO_RIGHT) {
						moovingDirectionIsChanging = true;
						orientation = TO_LEFT;
					}
				} else if (direction == TO_RIGHT && body.getLinearVelocity().x < maxVelocity) {
					if (withAccelerate)	body.applyForceToCenter(new Vec2(actualAccelerate * body.getMass(), 0));
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=maxVelocity;
						body.setLinearVelocity(velocity);
					}
					if (orientation == TO_LEFT) {
						moovingDirectionIsChanging = true;
						orientation = TO_RIGHT;
					}
				} else if (moovingDirectionIsChanging == true) {
					moovingDirectionIsChanging = false;
				}
				if (body.getLinearVelocity().x > maxVelocity) {
					body.setLinearVelocity(new Vec2(maxVelocity, body.getLinearVelocity().y));
				}
				if (body.getLinearVelocity().x < (-maxVelocity)) {
					body.setLinearVelocity(new Vec2(-maxVelocity, body.getLinearVelocity().y));
				}
				previsiousLinearSpeedAlongX = body.getLinearVelocity().x;
			}
		}
		else {
			//if (personAnimationController.getSpriteAnimation().stop())
			//System.out.println("It is stopping");
		}
	}
	 */

	private void moveUpToMaxVelocityWithTroubles(boolean direction, boolean movementType, boolean withAccelerate){
		if (body.isFixedRotation()) {
			float maxVelocity = 0;
			float yVelocityToPreventSlip = 0;
			float yAccelerateToPreventSlip = 0;
			if (movementType == GO_MOVEMENT) maxVelocity = maxVelocityAlongX;
			else if (movementType == RUN_MOVEMENT) {
				maxVelocity = maxVelocityAlongXByRunning;
			}
			if (!dead) {
				if (direction == TO_LEFT && body.getLinearVelocity().x > (-maxVelocity)) {
					if (withAccelerate)	{
						mutAccelerate.x = -actualAccelerate * body.getMass();
						//mutAccelerate.y = body.getF
						body.applyForceToCenter(mutAccelerate);
						moveObjectsUnderPerson(direction);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=-maxVelocity;
						//velocity.y = yVelocityToPreventSlip;
						moveObjectsUnderPerson(direction);
					}
					if (orientation == TO_RIGHT) {
						moovingDirectionIsChanging = true;
						orientation = TO_LEFT;
					}
				} else if (direction == TO_RIGHT && body.getLinearVelocity().x < maxVelocity) {
					if (withAccelerate)	{
						mutAccelerate.x = actualAccelerate * body.getMass();
						//mutAccelerate.y = yAccelerateToPreventSlip;
						body.applyForceToCenter(mutAccelerate);
						moveObjectsUnderPerson(direction);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=maxVelocity;
						//velocity.y = yVelocityToPreventSlip;
						moveObjectsUnderPerson(direction);
					}
					if (orientation == TO_LEFT) {
						moovingDirectionIsChanging = true;
						orientation = TO_RIGHT;
					}
				} else if (moovingDirectionIsChanging == true) {
					moovingDirectionIsChanging = false;
				}
				if (body.getLinearVelocity().x > maxVelocity) {
					mutMaxVelocity.x = maxVelocity;
					body.setLinearVelocity(mutMaxVelocity);
				}
				else if (body.getLinearVelocity().x < (-maxVelocity)) {
					mutMaxVelocity.x = -maxVelocity;
					body.setLinearVelocity(mutMaxVelocity);
				}
				previsiousLinearSpeedAlongX = body.getLinearVelocity().x;
			}
		}
	}



	/*
	private void moveUpToMaxVelocity(boolean direction, boolean movementType, boolean withAccelerate){
		if (body.isFixedRotation()) {
			float maxVelocity = 0;
			float yVelocityToPreventSlip = 25;
			float notJumpingZoneHeight = 0.1f;
			if (movementType == GO_MOVEMENT) maxVelocity = maxVelocityAlongX;
			else if (movementType == RUN_MOVEMENT) {
				maxVelocity = maxRunAlongX;
			}
			if (!dead) {
				if (direction == TO_LEFT && body.getLinearVelocity().x > (-maxVelocity)) {
					if (withAccelerate)	{
						body.applyForceToCenter(new Vec2(-actualAccelerate * body.getMass(), yVelocityToPreventSlip));
						moveObjectsUnderPerson(direction);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=-maxVelocity;
						if (velocity.y>-notJumpingZoneHeight && )
						body.setLinearVelocity(velocity);
						moveObjectsUnderPerson(direction);
					}
					if (orientation == TO_RIGHT) {
						moovingDirectionIsChanging = true;
						orientation = TO_LEFT;
					}
				} else if (direction == TO_RIGHT && body.getLinearVelocity().x < maxVelocity) {
					if (withAccelerate)	{
						body.applyForceToCenter(new Vec2(actualAccelerate * body.getMass(), 0));
						moveObjectsUnderPerson(direction);
					}
					else {
						Vec2 velocity = body.getLinearVelocity();
						velocity.x=maxVelocity;
						body.setLinearVelocity(velocity);
						moveObjectsUnderPerson(direction);
					}
					if (orientation == TO_LEFT) {
						moovingDirectionIsChanging = true;
						orientation = TO_RIGHT;
					}
				} else if (moovingDirectionIsChanging == true) {
					moovingDirectionIsChanging = false;
				}
				if (body.getLinearVelocity().x > maxVelocity) {
					body.setLinearVelocity(new Vec2(maxVelocity, body.getLinearVelocity().y));
				}
				if (body.getLinearVelocity().x < (-maxVelocity)) {
					body.setLinearVelocity(new Vec2(-maxVelocity, body.getLinearVelocity().y));
				}
				previsiousLinearSpeedAlongX = body.getLinearVelocity().x;
			}
		}
		else {
			//if (personAnimationController.getSpriteAnimation().stop())
			//System.out.println("It is stopping");
		}
	}
	 */


	public void setFixedRotation(){
		int angular = (int)body.getAngularVelocity();
		float angle = GameMechanics.convertRadiansToDegrees(body.getAngle());
		//System.out.println("Person angle: " + angle + "; Angular velocity: "  + (angular));
		float criticalAngularVelocityToStopBody = 5;
		float criticalAngleToStopBody = 5;
		if (PApplet.abs(angular) < criticalAngularVelocityToStopBody && PApplet.abs(angle) < criticalAngleToStopBody) {
			Vec2 velocity = body.getLinearVelocity();
			body.setTransform(body.getPosition(), 0f);
			body.setFixedRotation(true);
			body.setAngularVelocity(0);
			body.setLinearVelocity(velocity);
		}
		else{
			//body.setAngularDamping();
			//System.out.println("This body is rotating too fast");
		}
	}

	public void run(boolean withAccelerate, boolean direction){
		moveUpToMaxVelocity(direction, RUN_MOVEMENT, withAccelerate);
	}

	public void move(boolean withAccelerate, boolean direction) {
		moveUpToMaxVelocity(direction, GO_MOVEMENT, withAccelerate);
	}
	
	public boolean isMoving() {
		final float boardSpeed = 0.02f;
		if (statement == IN_AIR || body.getLinearVelocity().x > boardSpeed || (body.getLinearVelocity().x < (-boardSpeed))) {
			return true;
		}
		else return false;
	}
	
	public boolean isMovingUnderControl() {
		return isMoving();
	}
	
	public byte getAccelerateStatement() {
		return accelerateStatement;
	}
	
	public float getSpeedAlongX() {
		return body.getLinearVelocity().x;
	}
	
	public float getMaxVelocityAlongX() {
		return maxVelocityAlongX;
	}

	public void setMaxVelocityAlongX(float maxVelocityAlongX) {
		this.maxVelocityAlongX = maxVelocityAlongX;
	}

	/*
	public void lowerLevel() {
		if (level != WITHOUT_LEVEL_CHANGING) {
			level--;
			if(level != WITHOUT_LEVEL_CHANGING) life = maxLife;	
			simplifyBody();
		}		
	}*/
	
	/*
	 public boolean mustBeLevelChanged() {
		if (this.getClass() == Koopa.class) {
			if (level == FLYING || level == GOING || level == JUMPING) {
				if (life <= 0) return true;
				else return false;
			}
			else return false;
		}
		else return false;
		
	}
	 */
	
	public void simplifyBody() {
		System.out.println("Nothing to simplify");
	}
	

	protected void remakeBody() {
		
	}
	
	public void awake() {
		if (level == SITTING) {
			level = GOING;
			life = maxLife;
			remakeBody();
		}
	}


	public void addMovementToUp() {		
		body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, (float)(350/17.5)));
	}

	public void addReboundJump() {
		System.out.println("Jump added ");
		body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, reboundStartSpeed));
		statement = IN_AIR;
	}

	public void resetAngle() {
		if (body.getAngle() > 0.1f || body.getAngle() < (-0.1)) {
			if (Program.debug) System.out.println("Person " + this.getClass() + " is awaked");
			body.setTransform(body.getPosition(), 0);
			body.setAngularVelocity(0);
			body.setFixedRotation(true);
		}
	}


	public void update(GameRound gameRound) {
		// TODO Auto-generated method stub
		//System.out.println("This update is not used");
		if (isAlive()) {
			underAttack = isPersonInGodModeUnderBeenAttacked();
		}
		//enemiesAnimationController.setUnderAttack(underAttack);
	}

	public boolean canFixtureBeAttacked(Fixture attackedFixture, Bullet bullet){
		return true;
	}

	/*	was moved to GameControl
	public void changeWeapon(boolean which) {
		if (weapons.size()>1) {
			byte actualWeaponNumber = 1;
			for (byte i = 0; i < weapons.size(); i++) {
				if (weapons.get(i).isActual()) actualWeaponNumber = i;	
			}
			if (which == GameControl.NEXT_WEAPON) {
				if (actualWeaponNumber==(weapons.size()-1)) {
					setWeaponAsActual((byte)0);
				}
				else setWeaponAsActual((byte)(actualWeaponNumber+1));
			}
			else {
				if (actualWeaponNumber == 0) {
					setWeaponAsActual((byte)(weapons.size()-1));
				}
				else setWeaponAsActual((byte)(actualWeaponNumber-1));
			}
		}
		else if (Game2D.DEBUG) System.out.println("Can not change weapon. There are not 2 or more weapons");
		for (byte i = 0; i < weapons.size(); i++) {
			System.out.println("Weapons: " + weapons.get(i).isActual());
		}
		
	}

	
	public void setWeaponAsActual(byte weaponNumber) {
		for (byte i = 0; i < weapons.size(); i++) weapons.get(i).setWeaponAsActual(false);
		weapons.get(weaponNumber).setWeaponAsActual(true);
	}
	 */

	public boolean getSightDirection() {
		return sightDirection;
	}

	public void setSightDirection(boolean sightDirection) {
		this.sightDirection = sightDirection;
	}


	public int getPersonWidth() {
		return (int) boundingWidth;
	}


	public void resetSightDirection() {
		if (sightDirection == TO_LEFT) sightDirection = TO_RIGHT;
		else sightDirection = TO_LEFT;
	}



    public float getLandingVelocity(){
		float landingVelocity = PhysicGameWorld.controller.scalarWorldToPixels(this.landingVelocity)-PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().y);
		//float landingVelocity = (previsiousLinearSpeedAlongY)-PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().y);

		//System.out.println("Velocity: " + landingVelocity + "; is: " + landingVelocity + " was " + previsiousLinearSpeedAlongY);
		return landingVelocity;
	}

	public float getLandingVelocityOld(){
		float landingVelocity = PhysicGameWorld.controller.scalarWorldToPixels(previsiousLinearSpeedAlongY)-PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().y);
		//float landingVelocity = (previsiousLinearSpeedAlongY)-PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().y);

		//System.out.println("Velocity: " + landingVelocity + "; is: " + landingVelocity + " was " + previsiousLinearSpeedAlongY);
		return landingVelocity;
	}

	public Rectangular getKickAttackZone(){
		System.out.println("This person can not kick");
		return null;
	}

	public Vec2 getKickAttackRightUpperCorner(){
		System.out.println("Kick attack place for this character is not known");
		return null;
	}

	public boolean isKicking(){
		System.out.println("This person can not kick");
		return false;
	}

	public boolean canBeKickMade(){
		return false;
	}

	public void makeKick(){
		System.out.println("This person can not kick");
	}

	public float getPrevisiousLinearSpeedAlongY() {
		return previsiousLinearSpeedAlongY;
	}

	public abstract boolean attackByDirectContact(Person nearestPerson);

	public abstract int getAttackValue();

	@Override
	public void attacked(int damageValue) {
		if (damageValue > 0) {
			super.attacked(damageValue);
			underAttack = true;
			//enemiesAnimationController.setUnderAttack(true);	to child
			if (underAttackTimer == null) {
				underAttackTimer = new Timer(timeAfterAttackInGodMode);
			} else underAttackTimer.setNewTimer(timeAfterAttackInGodMode);
		}
	}

	public boolean isPersonInGodModeUnderBeenAttacked(){
		if (underAttackTimer == null) return false;
		else if (underAttackTimer.isTime()) return false;
		else return true;
	}

	public boolean isUnderAttack() {
		return underAttack;
	}

	public int getTimeAfterAttackInGodMode() {
		return timeAfterAttackInGodMode;
	}



	public boolean isJumpAfterAttack() {
		return jumpAfterAttack;
	}

	/*
	public void setJumpAfterAttack(boolean jumpAfterAttack) {
		this.jumpAfterAttack = jumpAfterAttack;
	}*/

	public void blockAttackAbility(boolean attackAbilityBlocked){
		this.attackAbilityBlocked = attackAbilityBlocked;
		if (afterHittingAttackBlockingTimer == null) {
			afterHittingAttackBlockingTimer = new Timer(attackBlockingTimeAfterBeenHitted);
		}
		else afterHittingAttackBlockingTimer.setNewTimer(attackBlockingTimeAfterBeenHitted);
	}

	public boolean isAttackAbilityBlocked() {
		return attackAbilityBlocked;
	}

	public int getAttackBlockingTimeAfterBeenHitted() {
		return attackBlockingTimeAfterBeenHitted;
	}


	public void setAttackStartsOnThisFrame(){
		attackStartedOnThisFrame = true;
	}

	public boolean isActualAnimationEnds() {
		return false;
	}

	public float getActualAccelerate() {
		return actualAccelerate;
	}

	public void setActualAccelerate(float actualAccelerate) {
		this.actualAccelerate = actualAccelerate;

	}

	public float getNormalAccelerate() {
		return normalAccelerate;
	}


	public int getActualByUserPressedMovement() {
		//System.out.println("This character is not controlled by user");
		return actualByUserPressedMovement;
	}

	public void setActualByUserPressedMovement(int actualByUserPressedMovement) {

		this.actualByUserPressedMovement = actualByUserPressedMovement;
	}

	public void setContactWithPlayer(Person player) {


	}

	public void contactWithMoveableObject(){

	}

	public void recoveryLifeInPercent(float percent){
		float value = maxLife*percent/100f;
		life+=(value);
		System.out.println("Value " + value + " for percents " + percent);
		if (life > maxLife) life = maxLife;
	}

	public void recoveryLifeInAbsoluteValues(int value){
		life+=(value);
		System.out.println("Value " + value + " was added ");
		if (life > maxLife) life = maxLife;
	}

	public boolean canPersonAddSplash() {
		//Not for flying
		return true;
	}

	@Override
	public void addJumpAfterAttack(GameObject attackingObject){
		Vec2 vector = null;
		final int xCoef = 7;
		int yCoef = 20;
		float attackingPos = attackingObject.getPixelPosition().x;
		float actualPos = getPixelPosition().x;
		if (statement == IN_AIR) {
			System.out.println("Player was attacked in air and can not be jumped up");
			yCoef = 0;
		}
		if (attackingPos < actualPos) vector = new Vec2(body.getMass()*xCoef, body.getMass()*yCoef);
		else vector = new Vec2(-body.getMass()*xCoef, body.getMass()*yCoef);
		body.applyLinearImpulse(vector, body.getPosition(), true);
	}

	public void setLowerLayingBody(Body lowerLayingBody) {
		//this.lowerLayingBody = lowerLayingBody;
	}

	public Body getLowerLayingBody() {
		return null;
	}

	public boolean canPersonBeAttackedByJump() {
		return true;
	}

	public boolean canBeAttackedThroughtExplosion(Fixture fixture) {
		return true;
	}

	public boolean canBeAttackedByFlyingObject() {
		return true;
	}

	public float getPixelDistToSplash() {
		return boundingHeight/1.75f;
	}

	public float getMaxVelocityAlongXByRunning() {
		return maxVelocityAlongXByRunning;
	}

	public void setMaxVelocityAlongXByRunning(float maxVelocityAlongXByRunning) {
		this.maxVelocityAlongXByRunning = maxVelocityAlongXByRunning;
	}
}
