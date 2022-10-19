package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.FlyingPath;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import org.jbox2d.common.Vec2;

import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PVector;

public class GlobalAI_Controller {
	private static final byte UPDATE_FREQUENCY_TO_TEST_ONE_PERSON_STAYS_ON_ANOTHER = 4;	// Frame to test
	// Behaviour models
	final static public byte CORPSE = 0;
	final static public byte SITTING_AND_WAITING = Person.SITTING;
	final static public byte GO_LEFT_AND_RIGHT = 2;
	final static public byte GO_AND_REGULARLY_JUMP = Person.JUMPING;
	final static public byte GO_AND_RANDOMLY_JUMP = -3;
	final static public byte FLYING_ALONG_SINUSOID_PATH = Person.FLYING;
	final static public byte BOWSER_AI = 5;
	byte behaviourModel;
	FlyingPath flyingPath;
	
	Enemy person;
	LocalAI_Controller localAIController;
	  
	  public GlobalAI_Controller(byte behaviourModel, Enemy person){
	    this.behaviourModel = behaviourModel;
	    this.person = person;
	    localAIController = new LocalAI_Controller(person);
	    if (behaviourModel == FLYING_ALONG_SINUSOID_PATH) {
	    	person.body.setGravityScale(0f);
	    	stopBody();
	    	System.out.println("Gravity was changed: ");
	    	flyingPath = new FlyingPath();
	    }
	  }
	  
	  GlobalAI_Controller(byte behaviourModel, int time){
	    this.behaviourModel = behaviourModel;
	    if (behaviourModel == GO_AND_REGULARLY_JUMP){
	      
	    }
	    else if (behaviourModel == FLYING_ALONG_SINUSOID_PATH) {
	    	person.body.setGravityScale(0);
	    	stopBody();
	    	System.out.println("Gravity was changed: ");
	    	person.body.setLinearDamping(0.9f);
	    	flyingPath = new FlyingPath();
	    }	    
	  }
	  
	  public Person getPerson() {
		  return person;
	  }
	  
	  private void stopBody() {
		  person.body.setLinearVelocity(new Vec2(0.0f,0));
		  person.body.setAngularVelocity(0);
		  //person.body.
		  //person.body.set
	  }
	  
	  public void changeBehaviourModel(byte behaviourModel) {
		  if (this.behaviourModel == FLYING_ALONG_SINUSOID_PATH && behaviourModel!= FLYING_ALONG_SINUSOID_PATH) person.body.setGravityScale(1);
		  this.behaviourModel = behaviourModel;
	  }
	  
	  public void updateBehaviourModel() {
		  
	  }

	  private boolean isPersonOnAnAnotherOne(GameRound gameRound){
		  for (int m = (PhysicGameWorld.beginContacts.size()-1); m>=0; m--) {
			  Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
			  Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
			  Body b1 = f1.getBody();
			  Body b2 = f2.getBody();
			  if (b1.equals(person.body)) {
				  for (Person testPerson : gameRound.getPersons()) {
					  if (b2.equals(testPerson.body)) {
					  	int deltaY = (int) (testPerson.getPixelPosition().y - person.getPixelPosition().y);
							if (deltaY > 0){	// Test persong is under this person
								if (deltaY > (person.getHeight()/2)){
									return true;
								}
							}
					  }
				  }
			  }
		  }
		  return false;
	  }


	  
	  public void update(GameRound gameRound){
		  localAIController.update();
		    if (behaviourModel == GO_AND_REGULARLY_JUMP){
		    	if (person.level != Person.JUMPING) behaviourModel = GO_LEFT_AND_RIGHT;	// It was downgraded
		    	if (localAIController.isBlockedAlongX() == true) {
		    		localAIController.changeMovementDirection();
		    	}
			    if (localAIController.isJumpingTimer() && person.getStatement() != Person.IN_AIR){
			        person.makeJump();
			    }
			    else if (person.getStatement() == Person.IN_AIR) localAIController.setJumpingTimerAgain();
		    }
		    else if (behaviourModel == GO_AND_RANDOMLY_JUMP){
		    	if (person.level != Person.JUMPING) behaviourModel = GO_LEFT_AND_RIGHT;	// It was downgraded
		    	if (localAIController.isBlockedAlongX() == true) {
		    		localAIController.changeMovementDirection();
		    	}
			    if (localAIController.isJumpingTimer() && person.getStatement() != Person.IN_AIR){
			        person.makeJump();
			    }
			    else if (person.getStatement() == Person.IN_AIR) {			    	
			    	localAIController.setRandomlyJumpingTimerAgain();
			    }
		    }		    
		    else if (behaviourModel == GO_LEFT_AND_RIGHT) {
		    	if (Program.engine.frameCount % UPDATE_FREQUENCY_TO_TEST_ONE_PERSON_STAYS_ON_ANOTHER == 0){
		    		if (isPersonOnAnAnotherOne(gameRound)){
		    			localAIController.changeMovementDirection();
		    			person.makeJump();
		    			System.out.println("*** The person " + person.getClass() + " is an another one and jumps");
					}
				}
		    	if (person.level != Person.GOING) behaviourModel = SITTING_AND_WAITING;	// It was downgraded
		    	if (localAIController.isBlockedAlongX() == true) {
		    		localAIController.changeMovementDirection();
		    		//if (person.level != Person.GOING)
		    	}
		    }
		    else if (behaviourModel == SITTING_AND_WAITING) {
		    	if (person.level != Person.SITTING) behaviourModel = 0;	// It was killed
		    	if (localAIController.isAwakingTimer() && person.getStatement() != Person.IN_AIR){
		    		if (person.hasPersonNormalAngleToBeActive()) {
				        person.awake();			        
				        behaviourModel = GO_LEFT_AND_RIGHT;
				        person.resetAngle();
		    		}
			    }
			    else if (person.getStatement() == Person.IN_AIR) localAIController.setAwakingTimerAgain();
		    }
		    
		    else if (behaviourModel == FLYING_ALONG_SINUSOID_PATH) {
		    	PVector prevPositon = flyingPath.getPrevisiousPosition();
		    	PVector newPositon = flyingPath.getActualPosition();
		    	Vec2 position = person.body.getPosition();
		    	person.body.setAngularVelocity(0);
		    	position.x+=PhysicGameWorld.controller.scalarPixelsToWorld(newPositon.x-prevPositon.x);
		    	position.y+=PhysicGameWorld.controller.scalarPixelsToWorld(newPositon.y-prevPositon.y);		    			    	
		    	float coef = 40;
		    	Vec2 velocityVector = new Vec2((newPositon.x-prevPositon.x)*coef, (newPositon.y-prevPositon.y)*coef);
		    	person.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocityVector));		    	
		    	if (person.level != Person.FLYING) {
		    		if (person.level == Person.JUMPING) changeBehaviourModel(GO_AND_REGULARLY_JUMP);
		    		else if (person.level == Person.GOING) changeBehaviourModel(GO_LEFT_AND_RIGHT);		    		
		    	}
		    }
		    else if (behaviourModel == BOWSER_AI) {
		    	if (localAIController.mustBeSeeDirectionChangedForBowser(gameRound)) {
		    		person.resetSightDirection();		    		
		    	}
		    	if (localAIController.isBlockedAlongX() == true) {
		    		localAIController.changeMovementDirection();
		    	}
			    if (localAIController.isJumpingTimer() && person.getStatement() != Person.IN_AIR){
			        person.makeJump();
			    }
			    if (localAIController.isShootingTimer() && !gameRound.isPlayerLoosed() && !gameRound.isPlayerWon()) {
			    	if (!gameRound.isLevelEnds() && gameRound.getPlayer().isAlive()) gameRound.addNewDragonFire(person);
			    	else System.out.println("Dragon fire can not be added");
			    }
			    else if (person.getStatement() == Person.IN_AIR) {			    	
			    	localAIController.setRandomlyJumpingTimerAgain();
			    }
		    }
		    
		    
		    
	  }
	  
	  
}
