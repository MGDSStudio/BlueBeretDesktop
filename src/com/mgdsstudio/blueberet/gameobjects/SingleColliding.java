package com.mgdsstudio.blueberet.gameobjects;

import org.jbox2d.dynamics.Fixture;

import processing.core.PVector;

public class SingleColliding {
	private PVector collidingPlace;
	private Fixture collidingfixture;
	private float relativeDistanceFromStart; 	// from 0 to 100%, where 0 - colliding at start point, 100 % - at end point
	private int attackValue;
	private float angle;
	//private byte weaponType = Weapon.CARBINE;
	
	public SingleColliding(PVector collidingPlace, Fixture collidingfixture, float relativeDistanceFromStart, float angle) {
		this.collidingPlace = collidingPlace;
		this.collidingfixture = collidingfixture;
		this.relativeDistanceFromStart = relativeDistanceFromStart;
		//System.out.println("Colliding place: " + collidingPlace);
		this.angle = angle;
	}
	
	public SingleColliding(PVector collidingPlace, Fixture collidingfixture, float relativeDistanceFromStart, int attackValue) {
		this.collidingPlace = collidingPlace;
		this.collidingfixture = collidingfixture;
		this.relativeDistanceFromStart = relativeDistanceFromStart;
		this.attackValue = attackValue;
		//System.out.println("Colliding place: " + collidingPlace);
	}
	
	public SingleColliding(PVector collidingPlace, Fixture collidingfixture, float relativeDistanceFromStart) {
		this.collidingPlace = collidingPlace;
		this.collidingfixture = collidingfixture;
		this.relativeDistanceFromStart = relativeDistanceFromStart;		
		//System.out.println("Colliding place: " + collidingPlace);
	}
	
	
	
	public Fixture getFixture() {
		return collidingfixture;
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
