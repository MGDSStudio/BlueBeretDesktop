package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gameobjects.SingleColliding;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;


public class RayCastingForExplosion {
	final private PVector startPos;
	private PVector actualPos;
	private float rayLength;
	private float angle;
	private byte framesForRay = 3;
	private byte actualFrame = 1;
	private boolean ended;
	public static int NORMAL_RESOLUTION = 35;
	private int rayCastingStepAlongRayProItteration = 2;
	final float resolution; // Pixels
	public  static final int EXPLOSION_TEST_RADIUS = 15;
	
	public RayCastingForExplosion(PVector startPos, float angleInDegrees, float rayLength, byte framesForRay) {
		this.startPos = startPos;
		this.angle = angleInDegrees;
		this.rayLength = rayLength;
		this.framesForRay = 1;
		actualPos = new PVector(startPos.x, startPos.y);
		resolution = NORMAL_RESOLUTION;
	}
	
	public ArrayList<Fixture> getCollidedFixtures() {
		update();
		if (!ended) {
			PVector endPosition = getEndPos();
			ArrayList<Fixture> collidingFixtures = new ArrayList<Fixture>();
			PVector position = new PVector(actualPos.x, actualPos.y);
			int bodiesNumber = 0;
			int fixturesNumber = 0;
			for (int i = 0; i < (rayLength/(framesForRay*NORMAL_RESOLUTION)); i++) {
				position.x+=(resolution*Math.cos(Math.toRadians(angle)));
				position.y+=(resolution*Math.sin(Math.toRadians(angle)));
				try {
					for (Body body = PhysicGameWorld.controller.world.getBodyList(); body!= null; body=body.getNext()) {
						bodiesNumber++;
						try {
							for (Fixture fixture = body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
								boolean inside = false;						
								inside = fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position));
								if (inside == true) {
									if (!collidingFixtures.contains(fixture)) {
										collidingFixtures.add(fixture);
									}
									
								}
								fixturesNumber++;
							}
						}
						catch (Exception e) {
							System.out.println("Can not determine fixtures");
						}
					}
				}
				catch (Exception e) {
					System.out.println("Can not determine bodies");
				}
			}			
			actualPos = endPosition;
			if (collidingFixtures.size()>0) {
				//if (Game2D.DEBUG) System.out.println("Founded fixtures: " + collidingFixtures.size() + " ");
				//if (Game2D.DEBUG) System.out.println("There are : " + (bodiesNumber/(rayLength/(framesForRay*NORMAL_RESOLUTION))) + " bodies and " + (fixturesNumber/(rayLength/(framesForRay*NORMAL_RESOLUTION))) + " fixtures");
			}
			actualFrame++;
			return collidingFixtures;
		}
		else return null;
	}


	public ArrayList<SingleColliding> getSingleCollidings(GameRound gameRound) {
		update();
		if (!ended) {
			ArrayList<Fixture> collidingFixtures = new ArrayList<Fixture>();
			ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
			PVector position = new PVector(actualPos.x+ EXPLOSION_TEST_RADIUS *PApplet.cos(PApplet.radians(angle)), actualPos.y+ EXPLOSION_TEST_RADIUS *PApplet.sin(PApplet.radians(angle)));
			Vec2 newStartPos = new Vec2(position.x, position.y);
			boolean rayStopped = false;
			for (int i = 0; i < (PApplet.ceil(rayLength/(rayCastingStepAlongRayProItteration))); i++) {
				if (!rayStopped) {
					position.x += (rayCastingStepAlongRayProItteration * PApplet.cos(PApplet.radians(angle)));
					position.y += (rayCastingStepAlongRayProItteration * PApplet.sin(PApplet.radians(angle)));
					try {
						for (Body body = PhysicGameWorld.controller.world.getBodyList(); body != null; body = body.getNext()) {
							if (!rayStopped) {
								try {
									for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
										if (!rayStopped) {
											boolean inside;
											inside = fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position));
											if (inside == true) {    //test point in some body
												if (!Program.isBodyBullet(fixture.getBody())) {
													if (!collidingFixtures.contains(fixture)) {
														PVector collidingPosition = new PVector(position.x, position.y);

														singleCollidings.add(new SingleColliding(collidingPosition, fixture, getRelativeDistanceFromStart(collidingPosition), angle));
														rayStopped = true;
														//DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.ARROW, newStartPos, new Vec2(position.x, position.y));
														DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.ARROW, newStartPos, new Vec2(collidingPosition.x, collidingPosition.y));
														if (Program.OS == Program.DESKTOP) gameRound.addDebugGraphic(debugGraphic);
														System.out.println("Angle: " + angle + "; Expl center: " + newStartPos + "; on pos " + collidingPosition + " with distance " + getRelativeDistanceFromStart(collidingPosition));
														//System.out.println("Ray attacks body " + body + " and blocked; Angle was: "  + angle + "; Length from center: " + getRelativeDistanceFromStart(collidingPosition) );
														collidingFixtures.add(fixture);
													}
													else rayStopped = true;
												}
											}
										}
									}
								} catch (Exception e) {
									System.out.println("Can not determine fixtures; " + e);
								}
							}
						}
					} catch (Exception e) {
						System.out.println("Can not determine bodies; " + e);
					}
				}

			}
			//actualPos = endPosition;
			actualFrame++;
			return singleCollidings;
		}
		else return null;
	}





	/*
	public ArrayList<SingleColliding> getSingleCollidings() {
		update();
		if (!ended) {
			PVector endPosition = getEndPos();
			//if (Game2D.DEBUG) System.out.println("Start: " + actualPos + "; Angle:"+ angle + "; end: " + endPosition + "; itterations: " + (rayLength/(framesForRay*NORMAL_RESOLUTION)));
			ArrayList<Fixture> collidingFixtures = new ArrayList<Fixture>();
			ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
			PVector position = new PVector(actualPos.x, actualPos.y);
			int bodiesNumber = 0;
			int fixturesNumber = 0;
			for (int i = 0; i < (rayLength/(framesForRay*NORMAL_RESOLUTION)); i++) {
				position.x+=(resolution*Math.cos(Math.toRadians(angle)));
				position.y+=(resolution*Math.sin(Math.toRadians(angle)));
				try {
					for (Body body = PhysicGameWorld.controller.world.getBodyList(); body!= null; body=body.getNext()) {
						bodiesNumber++;
						try {
							for (Fixture fixture = body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
								boolean inside = false;						
								inside = fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position));
								if (inside == true) {

									if (!collidingFixtures.contains(fixture) && !Game2D.isBodyBullet(fixture.getBody())) {
										collidingFixtures.add(fixture);
										singleCollidings.add(new SingleColliding(position, fixture, getRelativeDistanceFromStart(position), angle));										
									}
									
								}

								fixturesNumber++;
							}
						}
						catch (Exception e) {
							System.out.println("Can not determine fixtures");
						}
					}
				}
				catch (Exception e) {
					System.out.println("Can not determine bodies");
				}
			}			
			actualPos = endPosition;
			if (collidingFixtures.size()>0) {
				//if (Game2D.DEBUG) System.out.println("Founded fixtures: " + collidingFixtures.size() + " ");
				//if (Game2D.DEBUG) System.out.println("There are : " + (bodiesNumber/(rayLength/(framesForRay*NORMAL_RESOLUTION))) + " bodies and " + (fixturesNumber/(rayLength/(framesForRay*NORMAL_RESOLUTION))) + " fixtures");
			}
			actualFrame++;
			return singleCollidings;
		}
		else return null;
	}

	 */
	
	private float getRelativeDistanceFromStart(PVector actualPos) {	// from 0 to 100 %
		final float actualRayLength = Program.engine.dist(startPos.x, startPos.y, actualPos.x, actualPos.y);
		int procentValue = (int)(100f*actualRayLength/rayLength);
		System.out.println("Ray length: " + rayLength + "; Collision on length: " + actualRayLength + "; in %: " + procentValue);
		return procentValue;
	}
	
	
	private void update() {
		if (actualFrame>framesForRay) {
			ended = true;
		}
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public Vec2 getNullVector() {
		//System.out.println("Angle: " + angle);
		PVector vector = PVector.fromAngle(Program.engine.radians(angle));
		
		//vector.rotate(Game2D.engine.radians(-angle));
		//vector.normalize();
		Vec2 nullVector = new Vec2(vector.x, vector.y);
		return nullVector;
	}

	public float getAngle() {		
		return angle;
	}

	public PVector getStartPos() {
		return startPos;
	}
	
	public PVector getEndPos() {
		PVector endPosition = new PVector((float)(actualPos.x+(rayLength/framesForRay)*Math.cos(Math.toRadians(angle))), (float) (actualPos.y+(rayLength/framesForRay)*Math.sin(Math.toRadians(angle))));
		return endPosition;
	}
	
	public PVector getVectorFromStartToEnd() {
		PVector vector = new PVector(getEndPos().x, getEndPos().y);
		vector.sub(startPos);
				return vector;
	}

}

		