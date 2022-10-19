package com.mgdsstudio.blueberet.mainpackage;
import com.mgdsstudio.blueberet.gamecontrollers.BulletTimeController;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameProcess;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;

//import org.jbox2d.dynamics.contacts.ContactPoint;
// import org.jbox2d.dynamics.contacts.ContactResult;import org.jbox2d.dynamics.joints.Jacobian;

public abstract class PhysicGameWorld {	//implements Runnable

	//
	private static final ArrayList<GameObject> gameObjectsWithBodies = new ArrayList<GameObject>(30);	//I need to create this object on every frame! That's why it is a field!
	public static Box2DProcessing controller;
	//private static AABB aabb;

	private static int goToSleepZoneWidth = Program.engine.width/8;
	//private static double frequency;
	//private static double lastUpdatingTime;
	
	public static final ArrayList <Contact> beginContacts = new ArrayList<>();
    public static final ArrayList <Contact> endContacts = new ArrayList<>();
    public static final ArrayList <Contact> preContacts = new ArrayList<>();
    public static final ArrayList <Contact> postContacts = new ArrayList<>();
	public static boolean assertionErrorAppears = false;
	private boolean gameChangedToSingleThreadAfterAssertionError;
	//private final static float NORMAL_TIME_STEP = 0.0166f*30f/30f;
	private static float NORMAL_TIME_STEP = 0.0166f*30f/ Program.NORMAL_FPS;
	private static float TIME_STEP_BY_LAGGING = NORMAL_TIME_STEP*2f;
	private static float TIME_STEP_BY_BULLET_TIME = NORMAL_TIME_STEP / BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS;
	//private final static float GRAVITY = -75f*30f/30f;
	public static float GRAVITY = -75f* Program.NORMAL_FPS/30f;
	private static GameProcess gameProcess;
	public static boolean bulletTime = false;

	public final static float yFlip = -1;
	public final static float SCREEN_CENTER_X = Program.engine.width/2f;
	public final static float SCREEN_CENTER_Y = Program.engine.height/2f;
	public final static float worldScale = 30;

	//Mutable
	private final static Vec2 mutTestPointPixelPos = new Vec2(0,0);
	private final static Vec2 mutTestPointWorldPos = new Vec2(0,0);
	private final static Vec2 mutTestCoordPixelsToWorld = new Vec2(0,0);
	private final static ArrayList <Body> mutBodiesList = new ArrayList<>(30);
	private static Timer lowFPSTimer;
	private static final int LOW_FPS_TIME_TO_SAVE_STEP = 1000;

	private static boolean normalPerformance;
	private static boolean normalPreviousPerformance;
	private final static PVector mutUpperVertex = new PVector();
	private final static PVector mutLowerVertex = new PVector();
	//private static AABB mutAABB = new AABB();

	public static void init(GameProcess gp) {
		gameProcess = gp;
		controller = new Box2DProcessing(Program.engine, worldScale);
		controller.createWorld();
		controller.setGravity(0, GRAVITY);
		controller.listenForCollisions();
		assertionErrorAppears = false;
	}

	public static void initFrameRateSpecificData(int framerate){
		NORMAL_TIME_STEP = 0.0166f*30f/ framerate;
		TIME_STEP_BY_LAGGING = NORMAL_TIME_STEP*2f;
		TIME_STEP_BY_BULLET_TIME = NORMAL_TIME_STEP / BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS;
		GRAVITY = -75f* framerate/30f;
	}

	public static void clearContactsWithBody(Body body){
		if (Program.debug) System.out.println("Try delete contacts with body");
		int contactsToBeDeletedCount = 0;
		for (int i = (beginContacts.size()-1); i >= 0; i--){
			Fixture f1 = beginContacts.get(i).getFixtureA();
			Fixture f2 = beginContacts.get(i).getFixtureB();
			Body b1 = f1.getBody();
			Body b2 = f2.getBody();
			if (b1.equals(body) || b2.equals(body)){
				beginContacts.remove(i);
				contactsToBeDeletedCount++;
			}
		}
		for (int i = (endContacts.size()-1); i >= 0; i--){
			Fixture f1 = endContacts.get(i).getFixtureA();
			Fixture f2 = endContacts.get(i).getFixtureB();
			Body b1 = f1.getBody();
			Body b2 = f2.getBody();
			if (b1.equals(body) || b2.equals(body)){
				endContacts.remove(i);
				contactsToBeDeletedCount++;
			}
		}
		for (int i = (preContacts.size()-1); i >= 0; i--){
			Fixture f1 = preContacts.get(i).getFixtureA();
			Fixture f2 = preContacts.get(i).getFixtureB();
			Body b1 = f1.getBody();
			Body b2 = f2.getBody();
			if (b1.equals(body) || b2.equals(body)){
				preContacts.remove(i);
				contactsToBeDeletedCount++;
			}
		}
		for (int i = (postContacts.size()-1); i >= 0; i--){
			Fixture f1 = postContacts.get(i).getFixtureA();
			Fixture f2 = postContacts.get(i).getFixtureB();
			Body b1 = f1.getBody();
			Body b2 = f2.getBody();
			if (b1.equals(body) || b2.equals(body)){
				postContacts.remove(i);
				contactsToBeDeletedCount++;
			}
		}
		if (Program.debug) System.out.println(contactsToBeDeletedCount + " contacts were deleted");
	}


	public static void setGoToSleepZoneWidth(int width) {
		goToSleepZoneWidth = width;
	}

	public static ArrayList <Body> getBodiesInCircle(Vec2 center, float radius) {
		ArrayList <Body> bodiesInCircle = new ArrayList<>();
		ArrayList<Body> allBodies = getBodies();
		for (Body body : allBodies){
			if (PApplet.dist(controller.coordWorldToPixels(body.getPosition()).x, controller.coordWorldToPixels(body.getPosition()).y, center.x, center.y)<radius){
				bodiesInCircle.add(body);
			}
		}
		System.out.println("In circle area :" + bodiesInCircle.size() + " objects");
		return bodiesInCircle;
	}

    public static void makeAllBodiesInactive() {
		for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()){
			b.setActive(false);
		}
    }

    public static boolean isPointInBody(Vec2 testPoint, Body body) {
		for (Fixture f = body.getFixtureList(); f != null; f=f.getNext()){
			if (f.testPoint(testPoint)){
				return true;
			}
		}
		return false;
    }

    public static boolean isSomeBodyInZone(Flag flag) {
		mutUpperVertex.x = flag.getPosition().x-flag.getWidth()/2;
		mutUpperVertex.y = flag.getPosition().y-flag.getHeight()/2;
		mutLowerVertex.x = flag.getPosition().x+flag.getWidth()/2;
		mutLowerVertex.y = flag.getPosition().y+flag.getHeight()/2;
		//mutAABB.lowerBound = mutLowerVertex;
		//mutAABB.upperBound = mutLowerVertex;
		Vec2 newLower = coordPixelsToWorld(mutLowerVertex);
		Vec2 newUpper = controller.coordPixelsToWorld(mutUpperVertex);
		AABB aabb = new AABB(newLower, newUpper);
		final Coordinate founded = new Coordinate(1,1);
		//AABB aabb = new AABB(newUpper, newLower);
		//AABB aabb = new AABB(mutLowerVertex, mutUpperVertex);
		controller.world.queryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				founded.x = 0;
				System.out.println("Fixture: " + fixture.getShape().getType() + " is in zone");
				return true;
			}
		}, aabb);
		if (founded.x>0.5f) return false;
		else return true;
    }

    public int getGoToSleepZoneWidth() {
		return goToSleepZoneWidth;
	}
	
	/*
	@Override
	public static void run() {
		while(1!=0) {
			update();
		}
	}*/
	
	public static void update() {
		updateLowPerformance();
		try {
			if (bulletTime) controller.step(TIME_STEP_BY_BULLET_TIME, 4, 7);
			else controller.step(NORMAL_TIME_STEP, 3, 5);
			/*
			if (!lowPerformance) {
				if (bulletTime) controller.step(TIME_STEP_BY_BULLET_TIME, 4, 7);
				else controller.step(NORMAL_TIME_STEP, 3, 5);
			}
			else {
				if (bulletTime) controller.step(TIME_STEP_BY_BULLET_TIME*2, 4, 7);
				else controller.step(NORMAL_TIME_STEP*2, 3, 5);
			}*/
		}
		catch (AssertionError error){
			System.out.println("Can not update world. Assertion error: ");
			error.printStackTrace();
			if (! assertionErrorAppears) {
				gameProcess.setMultithreadedGame(false);
				assertionErrorAppears = true;
				gameProcess.getCamera().makeCameraMovementWithoutSpring();
			}
		}
	}

	private static void updateLowPerformance() {
		normalPreviousPerformance = normalPerformance;
		if (Program.engine.frameRate >= (Program.NORMAL_FPS)*0.6f){
			normalPerformance = true;
		}
		else {
			normalPerformance = false;
		}
		if (normalPerformance != normalPreviousPerformance){
			if (lowFPSTimer == null) lowFPSTimer = new Timer(LOW_FPS_TIME_TO_SAVE_STEP);
			else {
				if (lowFPSTimer.isTime()){
					lowFPSTimer.setNewTimer(LOW_FPS_TIME_TO_SAVE_STEP);
				}
			}
		}
	}


	public static boolean mustRoundElementFallAsleep(PVector position, GameCamera gameCamera) {
		if (position.x < (gameCamera.getActualPosition().x-(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) ||
				position.x > (gameCamera.getActualPosition().x+(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) ||
				position.y < (gameCamera.getActualPosition().y-(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) ||
				position.y > (gameCamera.getActualPosition().y+(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale()))) {
					return true;
				}
		else return false;
	}
	
	public static boolean mustRoundElementAwake(PVector position, GameCamera gameCamera) {
		if (position.x > (gameCamera.getActualPosition().x-(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) &&
				position.x < (gameCamera.getActualPosition().x+(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) &&
				position.y > (gameCamera.getActualPosition().y-(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale())) &&
				position.y < (gameCamera.getActualPosition().y+(Program.engine.width+goToSleepZoneWidth)/(2*gameCamera.getScale()))) {
					return true;
				}
		else return false;
	}


	
	public static boolean arePointInAnyBody(PVector point) {
		//Vec2 testPoint = controller.coordPixelsToWorld(point);
		//int trying = 0;
		for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
			//if (!b.isBullet()) {
				for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {
					//trying++;
					if (f != null) {
						try {
							if (f.testPoint(coordPixelsToWorld(point))) {
								//if (f.m_filter.categoryBits != CollisionFilterCreator.CATEGORY_CAMERA) return true;
								if (b.getUserData() != null) {
									if (b.getUserData().equals(GameCamera.CAMERA_BODY_NAME)) {
										//System.out.println("It's not a camera; " + b.getUserData());
										//return false;
									} else return true;
								} else return true;
							}
						}
						catch (Exception e){
							System.out.println("Maybe this fixture were already deleted  " + (f == null));
							return false;
						}
					}
				}	
			//}
		}
		return false;
	}

	public static boolean arePointInAnyBodyButNotInBullet(PVector pointInPixelsCorrdinates) {
		Vec2 testPoint = coordPixelsToWorld(pointInPixelsCorrdinates);
		//int trying = 0;
		for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
			//if (!b.isBullet()) {
			for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {
				//trying++;
				if (f != null) {
					try {
						if (f.testPoint(testPoint)) {
							//if (f.m_filter.categoryBits != CollisionFilterCreator.CATEGORY_CAMERA) return true;
							if (b.getUserData() != null) {
								if (b.getUserData().equals(GameCamera.CAMERA_BODY_NAME)) {
									//System.out.println("It's not a camera; " + b.getUserData());
									return false;
								} else if (b.getUserData().equals(Bullet.BULLET)) {
									//System.out.println("It's not a camera; " + b.getUserData());
									return false;
								} else if (b.isBullet()) {
									//return false;	//} was so else if (b.isBullet()) return false;
								}
								else return true;
							} else return true;
						}
					}
					catch (Exception e){
						System.out.println("This body is in a some null fixture " + (f == null));
						//return false;
					}
				}
				else return false;
			}
			//}
		}
		return false;
	}

	public static Vec2 getBodyPixelCoord(Body body){
		return coordWorldToPixels(body.getPosition().x, body.getPosition().y);
	}

	public static Vec2 coordWorldToPixels(float worldX, float worldY) {
		float pixelX = PApplet.map(worldX, 0.0F, 1.0F, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale);
		float pixelY = PApplet.map(worldY, 0.0F, 1.0F, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale);
		if (yFlip == -1.0F) {
			pixelY = PApplet.map(pixelY, 0.0F, (float) Program.engine.height, (float) Program.engine.height, 0.0F);
		}
		mutTestPointWorldPos.x = pixelX;
		mutTestPointWorldPos.y = pixelY;
		return mutTestPointWorldPos;
	}

	public static Vec2 coordPixelsToWorld(PVector point) {
		float worldX = PApplet.map(point.x, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
		float worldY = point.y;
		if (yFlip == -1.0F) {
			worldY = PApplet.map(point.y, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
		}
		worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
		mutTestPointPixelPos.x = worldX;
		mutTestPointPixelPos.y = worldY;
		return mutTestPointPixelPos;
	}

	public static Vec2 coordPixelsToWorld(Coordinate point) {
		float worldX = PApplet.map(point.x, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
		float worldY = point.y;
		if (yFlip == -1.0F) {
			worldY = PApplet.map(point.y, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
		}
		worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
		mutTestPointPixelPos.x = worldX;
		mutTestPointPixelPos.y = worldY;
		return mutTestPointPixelPos;
	}

	public static Vec2 coordPixelsToWorld(float pixelX, float pixelY) {
		float worldX = PApplet.map(pixelX, SCREEN_CENTER_X, SCREEN_CENTER_X + worldScale, 0.0F, 1.0F);
		float worldY = pixelY;
		if (yFlip == -1.0F) {
			worldY = PApplet.map(pixelY, (float) Program.engine.height, 0.0F, 0.0F, (float) Program.engine.height);
		}
		worldY = PApplet.map(worldY, SCREEN_CENTER_Y, SCREEN_CENTER_Y + worldScale, 0.0F, 1.0F);
		mutTestCoordPixelsToWorld.x = worldX;
		mutTestCoordPixelsToWorld.y = worldY;
		return mutTestCoordPixelsToWorld;
	}

	public static Body getBodyAtPoint(PVector point) {
		Vec2 testPoint = coordPixelsToWorld(point);
		for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
			for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {
				if (f.testPoint(testPoint)) {
					return b;
				}
			}	
		}
		System.out.println("Can not find any body in point " + point);
		return null;
	}

	public static GameObject getGameObjectByFixture(GameRound gameRound, Fixture testFixture){
		return getGameObjectByBody(gameRound, testFixture.getBody());
	}
	
	public static GameObject getGameObjectByBody(GameRound gameRound, Body testBody){
		for (Person person : gameRound.getPersons()) {
			if (person.body.equals(testBody)) return person;
		}
		for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
			if (roundPipe.hasFlower()) {
				/*
				if (roundPipe.flower.jaw1.equals(testBody)) return roundPipe.flower;
				if (roundPipe.flower.jaw2.equals(testBody)) return roundPipe.flower;
				if (roundPipe.flower.body.equals(testBody)) return roundPipe.flower;
				*/
			}
			if (roundPipe.body.equals(testBody)) return roundPipe;
		}
		try {
			for (int i = 0; i < gameRound.collectableObjectsController.getObjectsNumber(); i++) {
				if (gameRound.collectableObjectsController.getCollectableObjects().get(i).body.equals(testBody))
					return gameRound.collectableObjectsController.getCollectableObjects().get(i);
			}
		}
		catch (Exception e){
			System.out.println("Can not get object by body: " + e);
		}
		for (int i = 0; i < gameRound.roundElements.size(); i++) {
			if (gameRound.roundElements.get(i).body.equals(testBody)) return gameRound.roundElements.get(i);
		}
		for (int i = 0; i < gameRound.getBullets().size();i++){
			if (gameRound.getBullets().get(i).body.equals(testBody)) return gameRound.getBullets().get(i);
		}
		for (int i = 0; i < gameRound.getRoundRotatingSticks().size(); i++){
			if (gameRound.getRoundRotatingSticks().get(i).body.equals(testBody)) return gameRound.getRoundRotatingSticks().get(i);
		}
		for (int i = 0; i < gameRound.getLaunchableWhizbangsController().getWhizbangsNumber(); i++){
			if (gameRound.getLaunchableWhizbangsController().getWhizbangs().get(i).body.equals(testBody)) return gameRound.getLaunchableWhizbangsController().getWhizbangs().get(i);
		}
		for (int i = 0; i < gameRound.getMoveablePlatformsControllers().size(); i++){
			for (int j = 0; j < gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().size(); j++){
				if (gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().get(j).body.equals(testBody)) {
					return gameRound.getMoveablePlatformsControllers().get(i).getPlatforms().get(j);
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < gameRound.getRoundPipes().size(); j++) {
				if (gameRound.getRoundPipes().get(j).hasFlower() && gameRound.getRoundPipes().get(j).getPlantController()!= null) {
					ArrayList <Body> bodies = gameRound.getRoundPipes().get(j).getPlantController().getBodies();
					if (bodies.get(i).equals(testBody)) {
						return gameRound.getRoundPipes().get(j).getPlantController().getPlants().get(i);
					}
				}
			}
		}
		if (testBody != null && testBody.getUserData() != null) {
			if (testBody.getUserData() == "Camera body") {
				PhysicGameWorld.controller.world.destroyBody(testBody);
				System.out.println("This body " + testBody + " belongs to no one object and was deleted");
			}
		}

		//System.out.println("Can not determine object with body");
		return null;
	}

	public static ArrayList<GameObject> getGameObjectsWithBodies(GameRound gameRound){
		gameObjectsWithBodies.clear();
		for (Person person : gameRound.getPersons()) {
			if (person.body != null) gameObjectsWithBodies.add(person);
		}
		for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
			if (roundPipe.hasFlower()) {
				//if (roundPipe.)
				//if (roundPipe.flower.jaw1 != null || roundPipe.flower.jaw2 != null || roundPipe.flower.body != null) gameObjectsWithBodies.add(roundPipe.flower);
			}
		}
		for (int i = 0; i < gameRound.collectableObjectsController.getObjectsNumber(); i++) {
			if (gameRound.collectableObjectsController.getCollectableObjects().get(i).body!= null) gameObjectsWithBodies.add(gameRound.collectableObjectsController.getCollectableObjects().get(i));
		}
		for (int i = 0; i < gameRound.roundElements.size(); i++) {
			if (gameRound.roundElements.get(i).body != null) gameObjectsWithBodies.add(gameRound.roundElements.get(i));
		}
		for (int i = 0; i < gameRound.getBullets().size();i++){
			if (gameRound.getBullets().get(i).body != null) gameObjectsWithBodies.add(gameRound.getBullets().get(i));
		}
		return gameObjectsWithBodies;
	}

	public static ArrayList<Body> getBodies(){
		mutBodiesList.clear();
		for (Body b = controller.world.getBodyList(); b!=null; b=b.getNext()) {
			mutBodiesList.add(b);
		}
		return mutBodiesList;
	}

	public void addBoundaries(ArrayList<RoundBox> roundSimpleTiles) {
		// TODO Auto-generated method stub
		
		for (RoundBox b: roundSimpleTiles) {
		    //boxes.add(b.getPhysicBody());
		  } 
	}

	public static ArrayList<Contact> getBeginContacts(){
		return beginContacts;
	}
	
	public static void addBeginContact(Contact newContact) {
    	beginContacts.add(newContact);
    }
    
    public static void clearBeginContacts() {
    	beginContacts.clear();
    }
    
    public static void addEndContact(Contact newContact) {
    	endContacts.add(newContact);
    }
    
    public static void clearEndContacts() {
    	endContacts.clear();
    }

	public static void addPostContact(Contact newContact, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		postContacts.add(newContact);
	}

	public static void addPreContact(Contact newContact, Manifold arg1) {
		// TODO Auto-generated method stub
		preContacts.add(newContact);
	}
    
	public static void clearPostContacts() {
		postContacts.clear();
    }
	
	public static void clearPreContacts() {
		preContacts.clear();
    }

	public static boolean areThereBody(Vec2 pos, int testCircleRadius) {
		try {
			System.out.println("This fun works not properly");
		Body testBody = controller.world.getBodyList();
		for (int i = 0; i < controller.world.getBodyCount(); i++) {			
			if (Program.engine.dist(pos.x, pos.y, controller.coordWorldToPixels((testBody.getPosition())).x, controller.coordWorldToPixels((testBody.getPosition())).y) < testCircleRadius) {
				return true;
			}
			else testBody.getNext();	// falsch!
			
		}
		return false;
		}
		catch (Exception e) {
			System.out.println("Can not test area to find a body");
			return false;
		}
	}

	public static void removeAllContactsWithFixture(Fixture fixture){
		int count = 0;
		for (int i = (beginContacts.size()-1); i >= 0; i--){
			if (beginContacts.get(i).m_fixtureA.equals(fixture) || beginContacts.get(i).m_fixtureB.equals(fixture)){
				beginContacts.remove(i);
				count++;
			}
		}
		for (int i = (endContacts.size()-1); i >= 0; i--){
			if (endContacts.get(i).m_fixtureA.equals(fixture) || endContacts.get(i).m_fixtureB.equals(fixture)){
				endContacts.remove(i);count++;
			}
		}
		for (int i = (preContacts.size()-1); i >= 0; i--){
			if (preContacts.get(i).m_fixtureA.equals(fixture) || preContacts.get(i).m_fixtureB.equals(fixture)){
				preContacts.remove(i);
				count++;
			}
		}
		for (int i = (postContacts.size()-1); i >= 0; i--){
			if (postContacts.get(i).m_fixtureA.equals(fixture) || postContacts.get(i).m_fixtureB.equals(fixture)){
				postContacts.remove(i);
				count++;
			}
		}
		System.out.println(count + " contacts were deleted.");
	}

	
}
