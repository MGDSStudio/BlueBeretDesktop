package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class LocalAI_Controller {
	private static final byte LEFT = 1;
	private static final byte RIGHT = 2;
	
	// Timer constants
	final static private int MIN_TIME_TO_NEXT_RANDOM_JUMP = 1000;
	final static private int MAX_TIME_TO_NEXT_RANDOM_JUMP = 5000;
	
	//Fireball shooting
	final static public int NORMAL_TIME_TO_NEXT_FIREBALL = 3500;
	final static public int NORMAL_ERROR_BY_FIREBALL_SHOOTING = 700;
	private int timeBetweenFireballShooting;
	int maxBlockingTimeByWall = 1600;	
	int timeBetweenJumps = 2000;	
	int sittingMinTime = 8000;
	int sittingMaxTime = 25000;
	Timer timerBlocking, timerJumping, timerAwaking, timerShooting;
	private boolean MOVEMENT_WITH_ACCELERATE = true;
	byte movementDirection = RIGHT;	  
	Enemy person;
	  
	  LocalAI_Controller(Enemy person){
	    this.person = person;
	    timerBlocking = new Timer(maxBlockingTimeByWall);
	   // movementDirection = (byte)(Game2D.engine.round((Game2D.engine.random(0.6f, 2.4f))));
		  if (person.orientation == Person.TO_RIGHT) movementDirection = RIGHT;
		  else movementDirection = LEFT;
	  }
	  
	  
	  LocalAI_Controller(Enemy person, int time){
	    this.person = person;
	    timerBlocking = new Timer(time);
		  if (person.orientation == Person.TO_RIGHT) movementDirection = RIGHT;
		  else movementDirection = LEFT;
	  }
	  
	  boolean isJumpingTimer() {
		  if (timerJumping == null) {
			  timerJumping = new Timer(timeBetweenJumps);
			  return false;
		  }
		  else {
			  if (timerJumping.isTime()) {
				  //timerJumping.setNewTimer(maxTimeBetweenJumps);
				  return true;
			  }
			  else return false;
		  }		  
	  }
	  
	  void setJumpingTimerAgain() {
		  timerJumping.setNewTimer(timeBetweenJumps);
	  }
	  
	  void setRandomlyJumpingTimerAgain() {
		final int timeToNextJump = (int) Program.engine.random(MIN_TIME_TO_NEXT_RANDOM_JUMP, MAX_TIME_TO_NEXT_RANDOM_JUMP);
		timerJumping.setNewTimer(timeToNextJump);
	  }
	  	  
	  
	  boolean isBlockedAlongX(){		  
		  if (Math.abs(person.body.getLinearVelocity().x) < (Person.MAX_SPEED_BY_BLOCKED_STATEMENT)) {
			  if (timerBlocking.wasSet() == false) timerBlocking.setNewTimer(maxBlockingTimeByWall);
			  else {
				  if (timerBlocking.isTime()) {
					  timerBlocking.reset();					  
					  return true;
				  }
				  else return false;
			  }
			  return false;
		  }
		  else return false;
		  
	  }
	  
	void update(){
		if (person.level == Person.FLYING){	}		
		else if (person.level == GlobalAI_Controller.BOWSER_AI) {
			if (person.level!= GlobalAI_Controller.CORPSE) {
				if (movementDirection == LocalAI_Controller.LEFT) {
					person.move(MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
					//System.out.println("Go left");
			 	}
				else if (movementDirection == LocalAI_Controller.RIGHT) {
				  person.move(MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
				  //System.out.println("Go right");
			  }
			}
			
		}
		else {
			if (person.level!= GlobalAI_Controller.SITTING_AND_WAITING && person.level!= GlobalAI_Controller.CORPSE) {
				if (movementDirection == LocalAI_Controller.LEFT) {
				//System.out.println("Flying to left. Level: " + person.level + " Flying = "  +  Person.FLYING);
				person.move(MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
			 	}
				else if (movementDirection == LocalAI_Controller.RIGHT) {
				  //System.out.println("Flying to right. Level: " + person.level + " Flying = "  +  Person.FLYING);
				  person.move(MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
			  }
			}
		}
	}


	public void changeMovementDirection() {
		if (movementDirection == LEFT) movementDirection = RIGHT;
	    else if (movementDirection == RIGHT) movementDirection = LEFT;
	}


	boolean setAwakingTimerAgain() {
		final int time = (int) Program.engine.random(sittingMinTime, sittingMaxTime);
		timerAwaking.setNewTimer(time);
		return false;
	}


	public boolean isAwakingTimer() {
		if (timerAwaking == null) {
			final int time = (int) Program.engine.random(sittingMinTime, sittingMaxTime);
			timerAwaking = new Timer(time);
			  return false;
		  }
		  else {
			  if (timerAwaking.isTime()) {				  
				  return true;
			  }
			  else return false;
		  }	
		
	}


	public void addingMovementVector(byte behaviourModel) {		
		if (behaviourModel == GlobalAI_Controller.FLYING_ALONG_SINUSOID_PATH) {
			
		}
	}


	public boolean isShootingTimer() {
		if (timerShooting == null) {
			if (timeBetweenFireballShooting == 0) timeBetweenFireballShooting = NORMAL_TIME_TO_NEXT_FIREBALL;
			timerShooting = new Timer(timeBetweenFireballShooting + getNewFireballError());
			  return false;
		  }
		  else {
			  if (timerShooting.isTime()) {
				  timerShooting.setNewTimer(timeBetweenFireballShooting + getNewFireballError());
				  return true;
			  }
			  else return false;
		  }	
	}

	private int getNewFireballError() {
		int value = (int) Program.engine.random(NORMAL_ERROR_BY_FIREBALL_SHOOTING/2);
		float randomValue = Program.engine.random(100);
		if (randomValue >= 50f) return value;
		else return -value;
	}


	public boolean mustBeSeeDirectionChangedForBowser(GameRound gameRound) {
		if (person.getPixelPosition().x > gameRound.getPlayer().getPixelPosition().x) {
			if (person.getSightDirection() == Person.TO_RIGHT) {
				return true;
			}
			else return false;
		}
		else {
			if (person.getSightDirection() == Person.TO_RIGHT) {
				return false;
			}
			else return true;
		}
	}


	

	
}
