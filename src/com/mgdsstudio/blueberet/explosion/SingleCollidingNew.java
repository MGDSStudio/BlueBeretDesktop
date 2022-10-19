package com.mgdsstudio.blueberet.explosion;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PVector;

public class SingleCollidingNew {
	private PVector collidingPlace;
	private Body collidingBody;
	private float relativeDistanceFromStart; 	// from 0 to 100%, where 0 - colliding at start point, 100 % - at end point
	private float angle;
	//private byte weaponType = Weapon.CARBINE;

	public SingleCollidingNew(PVector collidingPlace, Body collidingBody, float relativeDistanceFromStart, float angle) {
		this.collidingPlace = collidingPlace;
		this.collidingBody = collidingBody;
		this.relativeDistanceFromStart = relativeDistanceFromStart;
		this.angle = angle;
	}

	public SingleCollidingNew(PVector collidingPlace, Body collidingBody, float relativeDistanceFromStart, int attackValue) {
		this.collidingPlace = collidingPlace;
		this.collidingBody = collidingBody;
		this.relativeDistanceFromStart = relativeDistanceFromStart;
	}

	public SingleCollidingNew(PVector collidingPlace, Body collidingBody, float relativeDistanceFromStart) {
		this.collidingPlace = collidingPlace;
		this.collidingBody = collidingBody;
		this.relativeDistanceFromStart = relativeDistanceFromStart;
	}
	
	public Body getCollidingBody(){
		return collidingBody;
	}
	
	public Fixture getFixture() {
		return collidingBody.getFixtureList();
	}
	
	public PVector getCollidingPlace() {
		return collidingPlace;
	}
	
	public float getRelativeDistanceFromStart() {
		return relativeDistanceFromStart;
	}
	
	public float getAngle() {
		return angle;
	}
	
}
