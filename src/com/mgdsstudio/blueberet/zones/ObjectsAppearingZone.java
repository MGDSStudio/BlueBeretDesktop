package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableObjectInNesStyle;
import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameobjects.persons.Koopa;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

import java.io.IOException;

public class ObjectsAppearingZone extends SingleFlagZone{
    final public static String CLASS_NAME = "ObjectsAppearingZone";
    private GameObject objectTemplate;
    private Flag activatingFlag;
    private byte appearingDirection = Program.TO_UP;
    private int startVelocity;
    private int objectsToBeGeneratedNumber = 10;
    private Timer timer;
    private int appearingTime = 1000;
    private float angleFluctuation = PApplet.radians(25.0f);    //+- Grad
    private boolean activated;
    private boolean allObjectsWereGenerated = false;

    public ObjectsAppearingZone(GameObject objectTemplate, Flag flag, Flag activatingFlag, byte appearingDirection, int startVelocity, int appearingTime, int objectsToBeGeneratedNumber){
        this.objectTemplate = objectTemplate;
        objectTemplate.setSleeped(true);
        this.flag = flag;
        if (activatingFlag == null) activated = true;
        else this.activatingFlag = activatingFlag;

        this.appearingDirection = appearingDirection;
        this.startVelocity = startVelocity;
        this.appearingTime = appearingTime;
        this.objectsToBeGeneratedNumber = objectsToBeGeneratedNumber;
    }

    public void update(GameRound gameRound) throws IOException, ClassNotFoundException {
        if (activated) {
            if (!allObjectsWereGenerated) {
                if (timer == null) timer = new Timer(appearingTime);
                else {
                    if (timer.isTime()) {
                        System.out.println("Number " + objectsToBeGeneratedNumber);
                        timer.setNewTimer(appearingTime);
                        generateNewObject(gameRound);
                    }
                }
            }
        }
        else {
            if (activatingFlag!=null){
                if (activatingFlag.getMission() == Flag.PLAYER_COMMING_IN_ZONE_TRIGGER){
                    if (activatingFlag.inZone(gameRound.getPlayer().getPixelPosition())){
                        activated = true;
                        activatingFlag = null;
                    }
                }
                else if (activatingFlag.getMission() == Flag.PERSON_COMMING_IN_ZONE_TRIGGER){
                    for (Person person: gameRound.getPersons()){
                        if (activatingFlag.inZone(person.getPixelPosition())){
                            activated = true;
                            activatingFlag = null;
                            return;
                        }
                    }
                }
                else if (activatingFlag.getMission() == Flag.PLAYER_LEAVING_ZONE_TRIGGER){
                    for (Person person: gameRound.getPersons()){
                        if (!activatingFlag.inZone(person.getPixelPosition())){
                            activated = true;
                            activatingFlag = null;
                            return;
                        }
                    }
                }
                else if (activatingFlag.getMission() == Flag.PERSONS_LEAVING_ZONE_TRIGGER){
                    boolean personsInArea = false;
                    for (Person person: gameRound.getPersons()){
                        if (activatingFlag.inZone(person.getPixelPosition())){
                            personsInArea = true;
                            return;
                        }
                    }
                    if (!personsInArea){
                        activated = true;
                        activatingFlag = null;
                    }
                }
                else if (activatingFlag.getMission() == Flag.FULL_CLEARING_ZONE_TRIGGER) {
                    boolean somethingInArea = false;
                    for (Person person : gameRound.getPersons()) {
                        if (activatingFlag.inZone(person.getPixelPosition())) {
                            somethingInArea = true;
                            return;
                        }
                    }
                    if (!somethingInArea){
                        for (RoundElement roundElement : gameRound.roundElements) {
                            if (activatingFlag.inZone(roundElement.getPixelPosition()) && roundElement.getMaxLife() != GameObject.IMMORTALY_LIFE) {
                                somethingInArea = true;
                                return;
                            }
                        }
                    }
                    if (!somethingInArea){
                        activated = true;
                        activatingFlag = null;
                    }
                }
            }
        }
    }

    public boolean isActivated(){
        return activated;
    }

    public GameObject getObject(){
        return objectTemplate;
    }

    private GameObject createObject(GameRound gameRound){
        GameObject newGameObject;
        if (objectTemplate.getClass() == Gumba.class) newGameObject = new Gumba((Gumba)objectTemplate);
        else if (objectTemplate.getClass() == Koopa.class) newGameObject = new Koopa((Koopa)objectTemplate);
        else if (objectTemplate.getClass() == CollectableObjectInNesStyle.class) newGameObject = new CollectableObjectInNesStyle((CollectableObjectInNesStyle)objectTemplate, gameRound);
        /*
        else if (gameObject.getClass() == Koopa.class) {
            Koopa koopa = (Koopa) gameObject;
            koopa.loadAnimationData(mainGraphicController);
            persons.add(koopa);
        }
        else if (gameObject.getClass() == Bowser.class) {
            Bowser bowser = (Bowser) gameObject;
            bowser.loadAnimationData(mainGraphicController);
            persons.add(bowser);
        }
        else if (gameObject.getClass() == RoundBox.class) {
            RoundBox roundBox = (RoundBox) gameObject;
            if (roundBox.hasGraphic()) roundBox.loadSprites(mainGraphicController.getTilesetUnderPath(roundBox.getSprite().getPath()));
            roundElements.add(roundBox);
        }
        else if (gameObject.getClass() == RoundPolygon.class) {
            RoundPolygon roundPolygon = (RoundPolygon) gameObject;
            if (roundPolygon.hasGraphic()) roundPolygon.loadSprites(mainGraphicController.getTilesetUnderPath(roundPolygon.getSprite().getPath()));
            roundElements.add(roundPolygon);
        }
        else if (gameObject.getClass() == CollectableObject.class) {
            CollectableObject collectableObject = (CollectableObject) gameObject;
            if (collectableObject.hasGraphic()) collectableObject.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObject.getSprite().getPath()));
            collectableObjectsController.addNewcollectableObject(collectableObject);
        }
        */
        else newGameObject = null;
        return newGameObject;
    }

    private void generateNewObject(GameRound gameRound) throws IOException, ClassNotFoundException {
        GameObject newGameObject;
        newGameObject = createObject(gameRound);
        newGameObject.setSleeped(false);
        Vec2 velocityVector = new Vec2(0,0);
        float randomFluctuationAngle = Program.engine.random(-1*angleFluctuation, angleFluctuation);
        if (appearingDirection == Program.TO_RIGHT) {
            velocityVector.x = startVelocity*PApplet.cos(randomFluctuationAngle);
            velocityVector.y = startVelocity*PApplet.sin(randomFluctuationAngle);
        }
        if (appearingDirection == Program.TO_LEFT) {
            velocityVector.x = -startVelocity*PApplet.cos(randomFluctuationAngle);
            velocityVector.y = -startVelocity*PApplet.sin(randomFluctuationAngle);
        }
        if (appearingDirection == Program.TO_DOWN) {
            velocityVector.y = -startVelocity*PApplet.cos(randomFluctuationAngle);
            velocityVector.x = -startVelocity*PApplet.sin(randomFluctuationAngle);
        }
        if (appearingDirection == Program.TO_UP) {
            velocityVector.y = startVelocity*PApplet.cos(randomFluctuationAngle);
            velocityVector.x = startVelocity*PApplet.sin(randomFluctuationAngle);
        }
        newGameObject.body.setTransform(PhysicGameWorld.controller.coordPixelsToWorld(GameMechanics.pVectorToVec2(flag.getPosition())),0f);
        newGameObject.body.setLinearVelocity(velocityVector);
        gameRound.addNewGameObject(newGameObject);
        objectsToBeGeneratedNumber --;
        updateZoneClosingStatement();

    }

    private void updateZoneClosingStatement(){
        if (objectsToBeGeneratedNumber <= 0) allObjectsWereGenerated = true;
        if (allObjectsWereGenerated){
            PhysicGameWorld.controller.destroyBody(objectTemplate.body);
            objectTemplate = null;
            System.out.println("Template was deleted");
        }
    }

    public void draw(GameCamera gameCamera){
        if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR){
            flag.draw(gameCamera);
            if (activatingFlag!=null) activatingFlag.draw(gameCamera);
        }
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}
