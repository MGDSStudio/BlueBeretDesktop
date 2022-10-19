package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.mainpackage.Program;

import processing.core.PVector;

public class FlyingPath {
	float period;	// length in pixels
	float amplitude; 	// height in pixels
	float accelerate;
	float maxSpeed;
	int flyingTime;
	//Directions
	final static boolean TO_LEFT = true;
	final static boolean TO_RIGHT = false;	
	boolean actualDirection;
	PVector prevPosition;
	
	//Time
	long startTime;
	
	
	public FlyingPath(float period, float amplitude, float accelerate, float maxSpeed, boolean startDirection) {
		this.period = period;
		this.amplitude = amplitude;
		this.accelerate = accelerate;
		this.maxSpeed = maxSpeed;
		this.actualDirection = startDirection;
		startTime = Program.engine.millis();
		prevPosition = new PVector();
	}
	
	public FlyingPath(boolean startDirection) {
		setNormalValues();
		this.actualDirection = startDirection;
		startTime = Program.engine.millis();
		prevPosition = new PVector();
	}
	
	public FlyingPath() {
		setNormalValues();
		actualDirection = Program.engine.parseBoolean(Program.engine.round((Program.engine.random(0f, 1f))));
		startTime = Program.engine.millis();
		prevPosition = new PVector();
	}
	
	private void setNormalValues() {
		period = 350;
		amplitude = 150;
		accelerate = 3;
		maxSpeed = 60;
		flyingTime = 4000; //ms
		prevPosition = new PVector();
	}

	
	public PVector getPrevisiousPosition() {
		return prevPosition;
	}

	public boolean getActualDirection() {
		return actualDirection;
	}

	public PVector getActualPosition() {
		// y=b*sin(c*x/d)
		PVector actualPosition = new PVector();
		float b = amplitude/2;
		float c = 6.306f/period;
		float d = (float) (flyingTime/(5.714));
		if (mustBeDirectionChanged()) changeFlyingDirection();
		if (actualDirection == TO_RIGHT) {			
			actualPosition.x = (float)((Program.engine.millis()-startTime)/(5.714));
			actualPosition.y = b* Program.engine.sin(c*(Program.engine.millis()-startTime)/(5.714f));
		}
		else {				
			actualPosition.x = (float) (period-((Program.engine.millis()-startTime)/(5.714)));
			actualPosition.y = b* Program.engine.sin(c*(Program.engine.millis()-startTime-(flyingTime/2))/(5.714f));
		}
		prevPosition = actualPosition;
		return actualPosition;
	}
	
	private void changeFlyingDirection() {
		if (actualDirection == TO_LEFT) actualDirection = TO_RIGHT;
		else actualDirection = TO_LEFT;
		final int rest = Program.engine.millis()%(flyingTime/2);
		startTime = (Program.engine.millis()-rest);
		//System.out.println("Direction of flying was changed. Actual direction is: " + actualDirection);
	}

	boolean mustBeDirectionChanged() {
		if ((Program.engine.millis()-startTime)>(flyingTime/2)) {
			return true;
		}
		else return false;
	}

	

	
	
}
