package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamecontrollers.JumpingLavaBallsController;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.RectangularWithAngle;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameobjects.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class ObjectsActiveLoader {
    private int distanceToStartAwakingForCameraZoneX, distanceToStartAwakingForCameraZoneY;
    private int distanceToStartAwakingForAroundPlayerZoneX, distanceToStartAwakingForAroundPlayerZoneY;
    final private int uploadingThreadFrameFrequence = 2;
    public static boolean ACTIVE_LOADING = true;
    private final int reserveZone = -50;
    private final int additionalZoneWidthToAwakePlatforms = 60;
    private final int iterationNumber = 5;
    private boolean withTwoZones = true;
    // for debug

    // Mutable
    private final ArrayList<GameObject> mutInObjects = new ArrayList<GameObject>(25);
    private final ArrayList <GameObject> mutObjectsToBeAdded = new ArrayList<>(15);
    private final Vec2 mutLeftUpperInside = new Vec2(0,0);
    private final Vec2 mutLeftUpperOutside = new Vec2(1,1);
    private final RectangularWithAngle mutTestRect1 = new RectangularWithAngle();
    private final RectangularWithAngle mutTestRect2 = new RectangularWithAngle();
    private final Vec2 mutLeftUpper1Inside = new Vec2(0, 0);
    private final Vec2 mutLeftUpper2Inside = new Vec2(0, 0);
    private final RectangularWithAngle mutRectForCameraTestZone = new RectangularWithAngle();
    private final RectangularWithAngle mutRectForAroundPlayerTestZone = new RectangularWithAngle();
    private final RectangularWithAngle mutRectForCameraTestZoneWithAdditionalPlatforms = new RectangularWithAngle();
    private final RectangularWithAngle mutRectForAroundPlayerTestZoneWithAdditionalPlatforms = new RectangularWithAngle();
    //private final RectangularWithAngle mutRectForAroundPlayerZone1 = new RectangularWithAngle();
    private final RectangularWithAngle mutRectForToTestObject = new RectangularWithAngle();
    private PVector playerPos;



    public ObjectsActiveLoader() {
        distanceToStartAwakingForCameraZoneX =  (Program.engine.width / 2) - reserveZone;
        distanceToStartAwakingForCameraZoneY = (Program.engine.height / 2) - reserveZone;
        distanceToStartAwakingForAroundPlayerZoneX = (int) (distanceToStartAwakingForCameraZoneX*1.25f);
        distanceToStartAwakingForAroundPlayerZoneY = (int) (distanceToStartAwakingForCameraZoneY*1.25f);
        if (Program.gameStatement == Program.LEVELS_EDITOR) ACTIVE_LOADING= false;
        else ACTIVE_LOADING = true;
    }




    public void switchOnActiveObjectsLoading(GameRound gameRound, boolean flag) {
        ACTIVE_LOADING = flag;
        if (flag == false) {
            System.out.println("This level must not active object uploading");
            awakeAllObjects(gameRound);
        }
    }

    public void draw(GameCamera gameCamera) {
        if (Program.debug) {
            Program.engine.pushMatrix();
            Program.engine.translate(Program.engine.width / 2, Program.engine.height / 2);
            Program.engine.pushStyle();
            Program.engine.stroke(20, 240, 20);
            Program.engine.rectMode(PConstants.CENTER);
            Program.engine.noFill();
            Program.engine.strokeWeight(3.8f);
            Program.engine.rect(0, 0, distanceToStartAwakingForCameraZoneX * 2, distanceToStartAwakingForCameraZoneY * 2);
            Program.engine.popStyle();
            Program.engine.popMatrix();
        }
    }



    private boolean isObjectInLoadingZone(PVector actualCameraPos, Vec2 pos) {
        //System.out.println("To test zone X: " + (actualCameraPos.x-distanceToStartAwakingX) + "; Y: " + (actualCameraPos.y-distanceToStartAwakingY) + " to X: " + (actualCameraPos.x+distanceToStartAwakingX) + " Y: " + (actualCameraPos.y+distanceToStartAwakingY) + ";");
        if (pos.x < (actualCameraPos.x + distanceToStartAwakingForCameraZoneX)) {
            if (pos.x > (actualCameraPos.x - distanceToStartAwakingForCameraZoneX)) {
                if (pos.y < (actualCameraPos.y + distanceToStartAwakingForCameraZoneY)) {
                    if (pos.y > (actualCameraPos.y - distanceToStartAwakingForCameraZoneY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /*
    public void setAllDynamicObjectsAsSlept(GameRound gameRound) {
        for (Person person : gameRound.getPersons()) {
            if (person.getClass() != Soldier.class) person.setSleeped(true);
        }
        for (RoundElement roundDynamicElement : gameRound.roundElements) roundDynamicElement.setSleeped(true);
        for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
            if (roundPipe.hasFlower()) {
                roundPipe.flower.setSleeped(true);
            }
        }
        for (RoundRotatingStick roundRotatingStick : gameRound.getRoundRotatingSticks())
            roundRotatingStick.setSleeped(true);
        //for (RoundElement roundStaticObject : gameRound.roundStaticObjects) roundStaticObject.setSleeped(true);
    }
    */

    /*
    private ArrayList<GameObject> getObjectsInVisibleArea(GameRound gameRound, float x, float y, float x2, float y2) {  // It is only for active bodies!!!
        Vec2 rightUpperCorner =  PhysicGameWorld.controller.coordPixelsToWorld(PApplet.max(x,x2), PApplet.min(y,y2));
        Vec2 leftLowerCorner =  PhysicGameWorld.controller.coordPixelsToWorld(PApplet.min(x,x2), PApplet.max(y,y2));
        final ArrayList<Fixture> list = new ArrayList<Fixture>();
        AABB aabb = new AABB();
        aabb.upperBound.set(rightUpperCorner);
        aabb.lowerBound.set(leftLowerCorner);
        PhysicGameWorld.controller.world.queryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                list.add(fixture);
                return true;
            }
        }, aabb);

        ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
        for (Fixture fixture : list) {
            GameObject gameObject = PhysicGameWorld.getGameObjectByBody(gameRound, fixture.getBody());
            if (gameObject != null) {
                if (!alreadyExist(gameObjects, gameObject)) gameObjects.add(gameObject);
            }
        }
        return gameObjects;
    }
    */


    private boolean alreadyExist(ArrayList<GameObject> objects, GameObject gameObject) {
        for (int i = 0; i < objects.size(); i++) {
            if (gameObject.equals(objects.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void awakeObjects(ArrayList<GameObject> objectsInActualArea) {
        for (int i = 0; i < objectsInActualArea.size(); i++) {
            if (objectsInActualArea.get(i).isSleeped()) {
                try {
                    objectsInActualArea.get(i).setSleeped(false);
                } catch (Exception e) {
                    System.out.println("Can not awake this object " + objectsInActualArea.get(i) + " ; Exc: " + e);
                }
            }
        }
    }

    private void awakeAllObjects(GameRound gameRound) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        for (int i = 0; i < allObjectsWithBodies.size(); i++) {
            if (allObjectsWithBodies.get(i).isSleeped()) {
                try {
                    allObjectsWithBodies.get(i).setSleeped(false);
                } catch (Exception e) {
                    System.out.println("Can not awake this object " + allObjectsWithBodies.get(i) + " ; Exc: " + e);
                }
            }
        }
    }

    private void putToSleepObjects(ArrayList<GameObject> objectsOutsideVisibleArea) {
        for (int i = 0; i < objectsOutsideVisibleArea.size(); i++) {
            if (!objectsOutsideVisibleArea.get(i).isSleeped() && objectsOutsideVisibleArea.get(i).getClass() != Bullet.class) {
                try {
                    objectsOutsideVisibleArea.get(i).setSleeped(true);
                } catch (Exception e) {
                    System.out.println("Can not set slept this object " + objectsOutsideVisibleArea.get(i) + " ; Exc: " + e);
                }
            }
        }
        //System.out.println("Outside: " + objectsOutsideVisibleArea.size());
    }

    private ArrayList<GameObject> getObjectsOutsideVisibleArea(GameRound gameRound, ArrayList<GameObject> objectsInVisibleArea) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            boolean elementWasFromArrayDeleted = false;
            for (int j = 0; j < (objectsInVisibleArea.size()); j++) {
                try {
                    if (!elementWasFromArrayDeleted) {
                        if (allObjectsWithBodies.get(i).equals(objectsInVisibleArea.get(j)) || objectsInVisibleArea.get(j).isPartOfSomeJoint()) {
                            allObjectsWithBodies.remove(i);
                            break;
                        }
                    }

                }
                catch (Exception e) {
                    System.out.println("Can not get all bodies to find objects outside the visible area: " + e);
                }
            }
        }
        return allObjectsWithBodies;
    }


    public void update(GameRound gameRound, GameCamera gameCamera) {
        if (ACTIVE_LOADING) {
            if (Program.engine.frameCount % uploadingThreadFrameFrequence == 0) {
                if (!withTwoZones) {
                    ArrayList<GameObject> objectsInVisibleArea = getObjectsInVisibleArea(gameRound, (gameCamera.getActualPosition().x - distanceToStartAwakingForCameraZoneX / gameCamera.getScale()), (gameCamera.getActualPosition().y - distanceToStartAwakingForCameraZoneY / gameCamera.getScale()), (gameCamera.getActualPosition().x + distanceToStartAwakingForCameraZoneX / gameCamera.getScale()), (gameCamera.getActualPosition().y + distanceToStartAwakingForCameraZoneY / gameCamera.getScale()));
                    awakeObjects(objectsInVisibleArea);
                    ArrayList<GameObject> objectsOutsideVisibleArea = getObjectsOutsideVisibleArea(gameRound, objectsInVisibleArea);
                    putToSleepObjects(objectsOutsideVisibleArea);
                }
                else{
                    playerPos = gameRound.getPlayer().getPixelPosition();
                    ArrayList<GameObject> objectsInVisibleArea = getObjectsInTwoZones(gameRound, (gameCamera.getActualPosition().x - distanceToStartAwakingForCameraZoneX / gameCamera.getScale()), (gameCamera.getActualPosition().y - distanceToStartAwakingForCameraZoneY / gameCamera.getScale()), (gameCamera.getActualPosition().x + distanceToStartAwakingForCameraZoneX / gameCamera.getScale()), (gameCamera.getActualPosition().y + distanceToStartAwakingForCameraZoneY / gameCamera.getScale()),
                            playerPos.x- distanceToStartAwakingForAroundPlayerZoneX, playerPos.y- distanceToStartAwakingForAroundPlayerZoneY, playerPos.x+ distanceToStartAwakingForAroundPlayerZoneX, playerPos.y+ distanceToStartAwakingForAroundPlayerZoneY);
                    awakeObjects(objectsInVisibleArea);
                    //System.out.println("In area: " + objectsInVisibleArea.size());
                    ArrayList<GameObject> objectsOutsideVisibleArea = getObjectsOutsideVisibleArea(gameRound, objectsInVisibleArea);
                    putToSleepObjects(objectsOutsideVisibleArea);
                }
            }
        }
        for (JumpingLavaBallsController jumpingLavaBallsController :gameRound.jumpingLavaBallsControllers){
            jumpingLavaBallsController.updateOnScreenStatement(gameCamera);
        }
        int unactive = 0;
        int active = 0;
        int allObjects = 0;
        for (int i = allObjects; i<gameRound.roundElements.size(); i++){
            if (gameRound.roundElements.get(i).body.isActive()){
                active++;
            }
            else unactive++;
            allObjects++;
        }
        //System.out.println("All: " + allObjects + "; Uncative: " + unactive + "; Active: " + active );
    }

    private ArrayList<GameObject> getObjectsInVisibleArea(GameRound gameRound, float x, float y, float x2, float y2) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        mutInObjects.clear();
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            try {
                if (allObjectsWithBodies.get(i).getClass() != Bullet.class) {
                    //Vec2 leftUpperInside = new Vec2(x, y);
                    mutLeftUpperInside.x = x;
                    mutLeftUpperInside.y = y;
                    float zoneWidth = PApplet.abs(x2 - x);
                    float zoneHeight = PApplet.abs(y2 - y);
                    GameMechanics.getRectangularFromData(mutTestRect1, mutLeftUpperInside, zoneWidth, zoneHeight);
                    if (GameMechanics.coalisionAllignedRectWithRect(mutLeftUpperInside, zoneWidth, zoneHeight,
                            PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                    }
                    /*

                     */

                    else {
                        //Vec2 leftUpperOutside = new Vec2(x-additionalZoneWidthToAwakePlatforms, y-additionalZoneWidthToAwakePlatforms);
                        mutLeftUpperOutside.x = x-additionalZoneWidthToAwakePlatforms;
                        mutLeftUpperOutside.y = y-additionalZoneWidthToAwakePlatforms;
                        zoneWidth = PApplet.abs(x2 - x)+2*additionalZoneWidthToAwakePlatforms;
                        zoneHeight = PApplet.abs(y2 - y)+2*additionalZoneWidthToAwakePlatforms;
                        GameMechanics.getRectangularFromData(mutTestRect2, mutLeftUpperInside, zoneWidth, zoneHeight);
                        if (GameMechanics.coalisionAllignedRectWithRect(mutLeftUpperOutside, zoneWidth, zoneHeight, PhysicGameWorld.controller.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                            boolean canBeAdded = true;
                            if (allObjectsWithBodies.get(i) instanceof Person) {
                                canBeAdded = false;
                            }
                            else if (allObjectsWithBodies.get(i).getClass() == RoundBox.class || allObjectsWithBodies.get(i).getClass() == RoundPolygon.class) {
                                RoundElement roundElement = (RoundElement) allObjectsWithBodies.get(i); // cast to parent
                                if (allObjectsWithBodies.get(i).body.getType() == BodyType.DYNAMIC){
                                    if (!roundElement.hasSpring()) {
                                        canBeAdded = false;
                                    }
                                }
                            }

                            if (canBeAdded) mutInObjects.add(allObjectsWithBodies.get(i));
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Some object can not have body; i= " + i + " exc:" + e);
            }
        }

        mutObjectsToBeAdded.clear();
        for (int i = 0; i < mutInObjects.size(); i++) {
            if (mutInObjects.get(i).isPartOfSomeJoint()){
                ArrayList <Body> linkedBodies = mutInObjects.get(i).getJoindedBodies();
                for (Body body : linkedBodies){
                    boolean bodyAlreadyAdded = false;
                    for (int j = 0; j < mutInObjects.size(); j++) {
                        if (body != null) {
                            if (mutInObjects.get(j).body.equals(body)) {
                                bodyAlreadyAdded = true;
                            }
                        }
                    }
                    if (!bodyAlreadyAdded){
                        GameObject objectToAddToVisibleCollection = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                        boolean alreadyAddedToList = false;
                        for (GameObject alreadyAddedObject : mutObjectsToBeAdded) {
                            if (alreadyAddedObject.equals(objectToAddToVisibleCollection)) alreadyAddedToList = true;
                        }
                        if (!alreadyAddedToList && objectToAddToVisibleCollection.isAlive()){
                            //System.out.println("Added object from joints");
                            mutObjectsToBeAdded.add(objectToAddToVisibleCollection);
                        }
                    }
                }
            }
        }
        return mutInObjects;
    }

    /*
    public static boolean coalisionAllignedRectWithRect(Vec2 allignedRectLeftUpperCorner, float allingedRectWidth, float allingedRectHeight, Vec2 center, float angle, float width, float height){
        RectangularWithAngle first = new RectangularWithAngle(allignedRectLeftUpperCorner, allingedRectWidth, allingedRectHeight);
        RectangularWithAngle second = new RectangularWithAngle(center, angle, width, height);
     */

    private void addUnderFeetObjectToList(GameObject gameObject, ArrayList <GameObject> listToFill){
        boolean canBeAdded = true;
        if (gameObject instanceof Person) {
            canBeAdded = false;
        }
        else if (gameObject.getClass() == RoundBox.class || gameObject.getClass() == RoundPolygon.class || gameObject.getClass() == RoundCircle.class) {
            if (gameObject.body.getType() == BodyType.DYNAMIC){
                if (gameObject instanceof RoundElement) {
                    RoundElement roundElement = (RoundElement) gameObject; // cast to parent
                    if (!roundElement.hasSpring()) {
                        canBeAdded = false;
                    }
                }
                //else canBeAdded = false;
            }
        }
        if (canBeAdded) listToFill.add(gameObject);
    }

    private ArrayList<GameObject> getObjectsInTwoZones(GameRound gameRound, float leftX1, float upperY1, float rightX1, float lowerY1, float leftX2, float upperY2, float rightX2, float lowerY2) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        mutInObjects.clear();
        mutRectForCameraTestZone.setNewPointsByTwoAnglePositions(leftX1, upperY1, rightX1, lowerY1);
        mutRectForAroundPlayerTestZone.setNewPointsByTwoAnglePositions(leftX2, upperY2, rightX2, lowerY2);
        int addedToCameraZone = 0;
        int addedToPlayerZone = 0;
        int addedToAroundCameraZone = 0;
        int addedToAroundPlayerZone = 0;
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            try {
                if (allObjectsWithBodies.get(i).getClass() != Bullet.class) {
                    float objectX = PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).x;
                    float objectY = PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).y;
                    float width = allObjectsWithBodies.get(i).getWidth();
                    float height = allObjectsWithBodies.get(i).getHeight();
                    mutRectForToTestObject.setNewPointsByCenterAndDims(objectX, objectY, width, height);
                    if (GameMechanics.coalisionAllignedRectWithRect(mutRectForCameraTestZone, mutRectForToTestObject)) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                        addedToCameraZone++;
                    }
                    else {
                        if (GameMechanics.coalisionAllignedRectWithRect(mutRectForAroundPlayerTestZone, mutRectForToTestObject)) {
                            mutInObjects.add(allObjectsWithBodies.get(i));
                            addedToPlayerZone++;
                        }
                        else {
                            mutRectForCameraTestZoneWithAdditionalPlatforms.setNewPointsByTwoAnglePositions(leftX1-additionalZoneWidthToAwakePlatforms, upperY1-additionalZoneWidthToAwakePlatforms, rightX1+additionalZoneWidthToAwakePlatforms, lowerY1+additionalZoneWidthToAwakePlatforms);
                            if (GameMechanics.coalisionAllignedRectWithRect(mutRectForCameraTestZoneWithAdditionalPlatforms, mutRectForToTestObject)) {
                                addUnderFeetObjectToList(allObjectsWithBodies.get(i), mutInObjects);
                                addedToAroundCameraZone++;
                            }
                            else {
                                mutRectForAroundPlayerTestZoneWithAdditionalPlatforms.setNewPointsByTwoAnglePositions(leftX2-additionalZoneWidthToAwakePlatforms, upperY2-additionalZoneWidthToAwakePlatforms, rightX2+additionalZoneWidthToAwakePlatforms, lowerY2+additionalZoneWidthToAwakePlatforms);
                                if (GameMechanics.coalisionAllignedRectWithRect(mutRectForAroundPlayerTestZoneWithAdditionalPlatforms, mutRectForToTestObject)){
                                    addUnderFeetObjectToList(allObjectsWithBodies.get(i), mutInObjects);
                                    addedToAroundPlayerZone++;
                                }
                            }
                        }
                    }

                }
            }
            catch (Exception e) {
                System.out.println("Some object can not have body; i= " + i + " exc:" + e);
            }
        }
        //System.out.println("in camera: " + addedToCameraZone + "; By player zone: " + addedToPlayerZone +"; Around camera: " + addedToAroundCameraZone +  " and player" + addedToAroundPlayerZone+ " ; Objects in world: " + allObjectsWithBodies.size());

        mutObjectsToBeAdded.clear();
        for (int i = 0; i < mutInObjects.size(); i++) {
            if (mutInObjects.get(i).isPartOfSomeJoint()){
                ArrayList <Body> linkedBodies = mutInObjects.get(i).getJoindedBodies();
                for (Body body : linkedBodies){
                    boolean bodyAlreadyAdded = false;
                    for (int j = 0; j < mutInObjects.size(); j++) {
                        if (body != null) {
                            if (mutInObjects.get(j).body.equals(body)) {
                                bodyAlreadyAdded = true;
                            }
                        }
                    }
                    if (!bodyAlreadyAdded){
                        GameObject objectToAddToVisibleCollection = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                        boolean alreadyAddedToList = false;
                        for (GameObject alreadyAddedObject : mutObjectsToBeAdded) {
                            if (alreadyAddedObject.equals(objectToAddToVisibleCollection)) alreadyAddedToList = true;
                        }
                        if (!alreadyAddedToList && objectToAddToVisibleCollection.isAlive()){
                            mutObjectsToBeAdded.add(objectToAddToVisibleCollection);
                        }
                    }
                }
            }
        }
        return mutInObjects;
    }
/*
    private ArrayList<GameObject> getObjectsInTwoZonesNotLast(GameRound gameRound, float leftX1, float upperY1, float rightX1, float lowerY1, float leftX2, float upperY2, float rightX2, float lowerY2) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        mutInObjects.clear();
        int addedToCameraZone = 0;
        int addedToPlayerZone = 0;
        int addedToPlatformsZone = 0;
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            try {
                if (allObjectsWithBodies.get(i).getClass() != Bullet.class) {
                    mutLeftUpper1Inside.x = leftX1;
                    mutLeftUpper1Inside.y = upperY1;
                    mutLeftUpper2Inside.x = leftX2;
                    mutLeftUpper2Inside.y = upperY2;
                    float zone1Width = PApplet.abs(rightX1 - leftX1);
                    float zone1Height = PApplet.abs(lowerY1 - upperY1);
                    float zone2Width = PApplet.abs(rightX2 - leftX2);
                    float zone2Height = PApplet.abs(lowerY2 - upperY2);
                    mutRectForTestZone.setNewPoints(leftX1, upperY1, rightX1, lowerY1);
                    mutRectForToTestObject.setNewPoints(PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).x, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).y, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight());
                    if (GameMechanics.coalisionAllignedRectWithRect(mutRectForTestZone, mutRectForToTestObject)) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                        //Object in cameraZone!
                    }
                    else {
                        mutRectForTestZone.setNewPoints(leftX2, upperY2, rightX2, lowerY2);
                        if (GameMechanics.coalisionAllignedRectWithRect(mutRectForTestZone, mutRectForToTestObject)) {
                            mutInObjects.add(allObjectsWithBodies.get(i));
                            //Object around the player!
                        }
                        else {
                            Vec2 leftUpper1Outside = new Vec2(leftX1-additionalZoneWidthToAwakePlatforms, upperY1-additionalZoneWidthToAwakePlatforms);
                            zone1Width = PApplet.abs(rightX1 - leftX1)+2*additionalZoneWidthToAwakePlatforms;
                            zone1Height = PApplet.abs(lowerY1 - upperY1)+2*additionalZoneWidthToAwakePlatforms;
                            Vec2 leftUpper2Outside = new Vec2(leftX2-additionalZoneWidthToAwakePlatforms, upperY2-additionalZoneWidthToAwakePlatforms);
                            zone2Width = PApplet.abs(rightX2 - leftX2)+2*additionalZoneWidthToAwakePlatforms;
                            zone2Height = PApplet.abs(lowerY2 - upperY2)+2*additionalZoneWidthToAwakePlatforms;
                            //mutRectForTestZone.setNewPoints(leftX1, upperY1, rightX1, lowerY1);
                            if (GameMechanics.coalisionAllignedRectWithRect(leftUpper1Outside, zone1Width, zone1Height, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                                addUnderFeetObjectToList(allObjectsWithBodies.get(i), mutInObjects);
                            }
                            else if (GameMechanics.coalisionAllignedRectWithRect(leftUpper2Outside, zone2Width, zone2Height, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                                addUnderFeetObjectToList(allObjectsWithBodies.get(i), mutInObjects);
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Some object can not have body; i= " + i + " exc:" + e);
            }
        }
        mutObjectsToBeAdded.clear();
        for (int i = 0; i < mutInObjects.size(); i++) {
            if (mutInObjects.get(i).isPartOfSomeJoint()){
                ArrayList <Body> linkedBodies = mutInObjects.get(i).getJoindedBodies();
                for (Body body : linkedBodies){
                    boolean bodyAlreadyAdded = false;
                    for (int j = 0; j < mutInObjects.size(); j++) {
                        if (body != null) {
                            if (mutInObjects.get(j).body.equals(body)) {
                                bodyAlreadyAdded = true;
                            }
                        }
                    }
                    if (!bodyAlreadyAdded){
                        GameObject objectToAddToVisibleCollection = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                        boolean alreadyAddedToList = false;
                        for (GameObject alreadyAddedObject : mutObjectsToBeAdded) {
                            if (alreadyAddedObject.equals(objectToAddToVisibleCollection)) alreadyAddedToList = true;
                        }
                        if (!alreadyAddedToList && objectToAddToVisibleCollection.isAlive()){
                            mutObjectsToBeAdded.add(objectToAddToVisibleCollection);
                        }
                    }
                }
            }
        }
        return mutInObjects;
    }
*/

    /*
    private ArrayList<GameObject> getObjectsInTwoZonesWithMuchGarbage(GameRound gameRound, float leftX1, float upperY1, float rightX1, float lowerY1, float leftX2, float upperY2, float rightX2, float lowerY2) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        mutInObjects.clear();
        int addedToCameraZone = 0;
        int addedToPlayerZone = 0;
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            try {
                if (allObjectsWithBodies.get(i).getClass() != Bullet.class) {
                    mutLeftUpper1Inside.x = leftX1;
                    mutLeftUpper1Inside.y = upperY1;
                    mutLeftUpper2Inside.x = leftX2;
                    mutLeftUpper2Inside.y = upperY2;
                    float zone1Width = PApplet.abs(rightX1 - leftX1);
                    float zone1Height = PApplet.abs(lowerY1 - upperY1);
                    float zone2Width = PApplet.abs(rightX2 - leftX2);
                    float zone2Height = PApplet.abs(lowerY2 - upperY2);
                    mutRectForTestZone.setNewPoints(leftX1, upperY1, rightX1, lowerY1);
                    mutRectForToTestObject.setNewPoints(PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).x, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body).y, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight());
                    //mutRect1.setNewPoints(leftX1, upperY1, rightX1, lowerY1);
                    if (GameMechanics.coalisionAllignedRectWithRect(mutLeftUpper1Inside, zone1Width, zone1Height,
                            PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                    }
                    else if (GameMechanics.coalisionAllignedRectWithRect(mutLeftUpper2Inside, zone2Width, zone2Height,
                            PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                    }
                    else {
                        Vec2 leftUpper1Outside = new Vec2(leftX1-additionalZoneWidthToAwakePlatforms, upperY1-additionalZoneWidthToAwakePlatforms);
                        zone1Width = PApplet.abs(rightX1 - leftX1)+2*additionalZoneWidthToAwakePlatforms;
                        zone1Height = PApplet.abs(lowerY1 - upperY1)+2*additionalZoneWidthToAwakePlatforms;
                        Vec2 leftUpper2Outside = new Vec2(leftX2-additionalZoneWidthToAwakePlatforms, upperY2-additionalZoneWidthToAwakePlatforms);
                        zone2Width = PApplet.abs(rightX2 - leftX2)+2*additionalZoneWidthToAwakePlatforms;
                        zone2Height = PApplet.abs(lowerY2 - upperY2)+2*additionalZoneWidthToAwakePlatforms;
                        if (GameMechanics.coalisionAllignedRectWithRect(leftUpper1Outside, zone1Width, zone1Height, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                            boolean canBeAdded = true;
                            if (allObjectsWithBodies.get(i) instanceof Person) {
                                canBeAdded = false;
                            }
                            else if (allObjectsWithBodies.get(i).getClass() == RoundBox.class || allObjectsWithBodies.get(i).getClass() == RoundPolygon.class) {
                                RoundElement roundElement = (RoundElement) allObjectsWithBodies.get(i); // cast to parent
                                if (allObjectsWithBodies.get(i).body.getType() == BodyType.DYNAMIC){
                                    if (!roundElement.hasSpring()) {
                                        canBeAdded = false;
                                    }
                                }
                            }
                            if (canBeAdded) mutInObjects.add(allObjectsWithBodies.get(i));
                        }
                        else if (GameMechanics.coalisionAllignedRectWithRect(leftUpper2Outside, zone2Width, zone2Height, PhysicGameWorld.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                            boolean canBeAdded = true;
                            if (allObjectsWithBodies.get(i) instanceof Person) {
                                canBeAdded = false;
                            }
                            else if (allObjectsWithBodies.get(i).getClass() == RoundBox.class || allObjectsWithBodies.get(i).getClass() == RoundPolygon.class) {
                                RoundElement roundElement = (RoundElement) allObjectsWithBodies.get(i); // cast to parent
                                if (allObjectsWithBodies.get(i).body.getType() == BodyType.DYNAMIC){
                                    if (!roundElement.hasSpring()) {
                                        canBeAdded = false;
                                    }
                                }
                            }
                            if (canBeAdded) mutInObjects.add(allObjectsWithBodies.get(i));
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Some object can not have body; i= " + i + " exc:" + e);
            }
        }
        mutObjectsToBeAdded.clear();
        for (int i = 0; i < mutInObjects.size(); i++) {
            if (mutInObjects.get(i).isPartOfSomeJoint()){
                ArrayList <Body> linkedBodies = mutInObjects.get(i).getJoindedBodies();
                for (Body body : linkedBodies){
                    boolean bodyAlreadyAdded = false;
                    for (int j = 0; j < mutInObjects.size(); j++) {
                        if (body != null) {
                            if (mutInObjects.get(j).body.equals(body)) {
                                bodyAlreadyAdded = true;
                            }
                        }
                    }
                    if (!bodyAlreadyAdded){
                        GameObject objectToAddToVisibleCollection = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                        boolean alreadyAddedToList = false;
                        for (GameObject alreadyAddedObject : mutObjectsToBeAdded) {
                            if (alreadyAddedObject.equals(objectToAddToVisibleCollection)) alreadyAddedToList = true;
                        }
                        if (!alreadyAddedToList && objectToAddToVisibleCollection.isAlive()){
                            mutObjectsToBeAdded.add(objectToAddToVisibleCollection);
                        }
                    }
                }
            }
        }
        return mutInObjects;
    }

    */


    /*
    private ArrayList<GameObject> getObjectsInTwoZones(GameRound gameRound, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        ArrayList<GameObject> allObjectsWithBodies = PhysicGameWorld.getGameObjectsWithBodies(gameRound);
        mutInObjects.clear();
        int addedToCameraZone = 0;
        int addedToPlayerZone = 0;
        for (int i = (allObjectsWithBodies.size() - 1); i >= 0; i--) {
            try {
                if (allObjectsWithBodies.get(i).getClass() != Bullet.class) {
                    Vec2 leftUpper1Inside = new Vec2(x1, y1);
                    float zone1Width = PApplet.abs(x2 - x1);
                    float zone1Height = PApplet.abs(y2 - y1);
                    Vec2 leftUpper2Inside = new Vec2(x3, y3);
                    float zone2Width = PApplet.abs(x4 - x3);
                    float zone2Height = PApplet.abs(y4 - y3);
                    if (GameMechanics.coalisionAllignedRectWithRect(leftUpper1Inside, zone1Width, zone1Height,
                            PhysicGameWorld.controller.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                        addedToCameraZone++;
                    }
                    else if (GameMechanics.coalisionAllignedRectWithRect(leftUpper2Inside, zone2Width, zone2Height,
                            PhysicGameWorld.controller.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                        mutInObjects.add(allObjectsWithBodies.get(i));
                        addedToPlayerZone++;
                    }
                    else {
                        Vec2 leftUpper1Outside = new Vec2(x1-additionalZoneWidthToAwakePlatforms, y1-additionalZoneWidthToAwakePlatforms);
                        zone1Width = PApplet.abs(x2 - x1)+2*additionalZoneWidthToAwakePlatforms;
                        zone1Height = PApplet.abs(y2 - y1)+2*additionalZoneWidthToAwakePlatforms;
                        Vec2 leftUpper2Outside = new Vec2(x3-additionalZoneWidthToAwakePlatforms, y3-additionalZoneWidthToAwakePlatforms);
                        zone2Width = PApplet.abs(x4 - x3)+2*additionalZoneWidthToAwakePlatforms;
                        zone2Height = PApplet.abs(y4 - y3)+2*additionalZoneWidthToAwakePlatforms;
                        if (GameMechanics.coalisionAllignedRectWithRect(leftUpper1Outside, zone1Width, zone1Height, PhysicGameWorld.controller.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                            boolean canBeAdded = true;
                            if (allObjectsWithBodies.get(i) instanceof Person) {
                                canBeAdded = false;
                            }
                            else if (allObjectsWithBodies.get(i).getClass() == RoundBox.class || allObjectsWithBodies.get(i).getClass() == RoundPolygon.class) {
                                RoundElement roundElement = (RoundElement) allObjectsWithBodies.get(i); // cast to parent
                                if (allObjectsWithBodies.get(i).body.getType() == BodyType.DYNAMIC){
                                    if (!roundElement.hasSpring()) {
                                        canBeAdded = false;
                                    }
                                }
                            }
                            if (canBeAdded) mutInObjects.add(allObjectsWithBodies.get(i));
                        }
                        else if (GameMechanics.coalisionAllignedRectWithRect(leftUpper2Outside, zone2Width, zone2Height, PhysicGameWorld.controller.getBodyPixelCoord(allObjectsWithBodies.get(i).body), 0f, allObjectsWithBodies.get(i).getWidth(), allObjectsWithBodies.get(i).getHeight())) {
                            boolean canBeAdded = true;
                            if (allObjectsWithBodies.get(i) instanceof Person) {
                                canBeAdded = false;
                            }
                            else if (allObjectsWithBodies.get(i).getClass() == RoundBox.class || allObjectsWithBodies.get(i).getClass() == RoundPolygon.class) {
                                RoundElement roundElement = (RoundElement) allObjectsWithBodies.get(i); // cast to parent
                                if (allObjectsWithBodies.get(i).body.getType() == BodyType.DYNAMIC){
                                    if (!roundElement.hasSpring()) {
                                        canBeAdded = false;
                                    }
                                }
                            }
                            if (canBeAdded) mutInObjects.add(allObjectsWithBodies.get(i));
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Some object can not have body; i= " + i + " exc:" + e);
            }
        }
        //System.out.println("In camera zone: " + addedToCameraZone + "; in player zone: " + addedToPlayerZone);

        mutObjectsToBeAdded.clear();
        for (int i = 0; i < mutInObjects.size(); i++) {
            if (mutInObjects.get(i).isPartOfSomeJoint()){
                ArrayList <Body> linkedBodies = mutInObjects.get(i).getJoindedBodies();
                for (Body body : linkedBodies){
                    boolean bodyAlreadyAdded = false;
                    for (int j = 0; j < mutInObjects.size(); j++) {
                        if (body != null) {
                            if (mutInObjects.get(j).body.equals(body)) {
                                bodyAlreadyAdded = true;
                            }
                        }
                    }
                    if (!bodyAlreadyAdded){
                        GameObject objectToAddToVisibleCollection = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                        boolean alreadyAddedToList = false;
                        for (GameObject alreadyAddedObject : mutObjectsToBeAdded) {
                            if (alreadyAddedObject.equals(objectToAddToVisibleCollection)) alreadyAddedToList = true;
                        }
                        if (!alreadyAddedToList && objectToAddToVisibleCollection.isAlive()){
                            mutObjectsToBeAdded.add(objectToAddToVisibleCollection);
                        }
                    }
                }
            }
        }
        return mutInObjects;
    }
     */



    private ArrayList<GameObject> addPlatformsUnderPersons(GameRound gameRound, ArrayList<GameObject> inObjects) {
        System.out.print("It was: " + inObjects.size() + "; ");
        for (int i = 0; i < inObjects.size(); i++) {
            try {
                if (inObjects.get(i).body.getType() == BodyType.DYNAMIC) {
                    boolean platformFoundedAndAwoken = false;
                    for (int j = 1; j <= iterationNumber; j++) {
                        if (!platformFoundedAndAwoken) {
                            PVector testPoint = new PVector(inObjects.get(i).getPixelPosition().x,
                                    inObjects.get(i).getPixelPosition().y +
                                            (inObjects.get(i).getHeight() / 2) +
                                            i * additionalZoneWidthToAwakePlatforms / iterationNumber);
                            if (PhysicGameWorld.arePointInAnyBody(testPoint)) {
                                Body platformBody = PhysicGameWorld.getBodyAtPoint(testPoint);
                                if (platformBody != null) {
                                    GameObject platformObject = gameRound.getGameObjectByBody(platformBody);
                                    if (platformObject.getClass() == RoundBox.class || platformObject.getClass() == RoundPolygon.class || platformObject.getClass() == MovablePlatform.class || platformObject.getClass() == RoundElement.class || platformObject.getClass() == RoundPipe.class) {
                                        inObjects.add(platformObject);
                                        System.out.println("****The platform object: " + platformObject.getClass() + " is awoken ");
                                        platformFoundedAndAwoken = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Can not get bodies of the bullets to be awoke");
            }
        }
        //System.out.print("It is: " + inObjects.size() + "");
        //System.out.println();
        return  inObjects;
    }
}
