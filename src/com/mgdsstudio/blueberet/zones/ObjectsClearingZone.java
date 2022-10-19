package com.mgdsstudio.blueberet.zones;


import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

import java.util.ArrayList;

public class ObjectsClearingZone extends SingleFlagZone implements IDrawable {
    final public static String CLASS_NAME = "ObjectsClearingZone";
    final static private String objectToDisplayName = "Clearing zone";
    final static public byte DELETE_EVERY_OBJECT = 0;
    final static public byte DELETE_EVERY_PERSON = 1;
    final static public byte DELETE_EVERY_OBJECT_WITHOUT_PLAYER = 2;
    final static public byte DELETE_CORPSES = 3;
    final static public byte DELETE_ROUND_ELEMENTS = 4;
    final static public String DELETE_EVERY_OBJECT_STRING = "Delete all objects";
    final static public String DELETE_EVERY_PERSON_STRING = "Delete only persons";
    final static public String DELETE_EVERY_PERSON_WITHOUT_PLAYER_STRING = "Delete persons without player";
    final static public String DELETE_CORPSES_STRING = "Delete corpses";
    final static public String DELETE_ROUND_ELEMENTS_STRING = "Delete round elements";


    ArrayList <GameObject> objectsToBeDeleted = new ArrayList<GameObject>();

    public ObjectsClearingZone(Flag flag){
        this.flag = flag;
        activatingCondition = DELETE_EVERY_OBJECT_WITHOUT_PLAYER;
    }

    public ObjectsClearingZone(Flag flag, byte goal){
        this.flag = flag;
        this.activatingCondition = goal;
    }

    public ObjectsClearingZone(GameObjectDataForStoreInEditor objectData) {
        //this.flag = objectData.getFlag();
        this.flag = new Flag(new PVector(objectData.getPosition().x, objectData.getPosition().y), objectData.getWidth(), objectData.getHeight(), Flag.CLEAR_OBJECTS);
        System.out.println("Activating condition: " + objectData.getActivatedBy() + " or " + objectData.getGoal());
        this.activatingCondition = objectData.getGoal();
    }



    public void update (GameRound gameRound){
        if (activatingCondition == DELETE_EVERY_OBJECT || activatingCondition == DELETE_EVERY_OBJECT_WITHOUT_PLAYER || activatingCondition == DELETE_ROUND_ELEMENTS) {
                for (int j = (gameRound.roundElements.size() - 1); j >= 0; j--) {
                    try {
                        if (flag.inZone(PhysicGameWorld.controller.getBodyPixelCoord(gameRound.roundElements.get(j).body)) && gameRound.roundElements.get(j).body.getType() == BodyType.DYNAMIC) {
                            gameRound.roundElements.get(j).body.setActive(false);
                            PhysicGameWorld.controller.destroyBody(gameRound.roundElements.get(j).body);
                            gameRound.roundElements.remove(gameRound.roundElements.get(j));
                        }
                    } catch (Exception e) {
                        System.out.println("Can not add object for deleting");
                    }
                }

            for (int j = (gameRound.bullets.size()-1); j >= 0; j--){
                try {
                    if (flag.inZone(PhysicGameWorld.controller.getBodyPixelCoord(gameRound.bullets.get(j).body)) && gameRound.bullets.get(j).body.getType() == BodyType.DYNAMIC) {
                        gameRound.bullets.get(j).setActive(false);
                    }
                }
                catch (Exception e ){
                    System.out.println("Can not add object for deleting");
                }
            }
            if (gameRound.launchableWhizbangsController != null) {
                for (int j = (gameRound.launchableWhizbangsController.getWhizbangsNumber()-1) ;j >= 0; j--){
                    if (flag.inZone(PhysicGameWorld.controller.getBodyPixelCoord(gameRound.launchableWhizbangsController.getWhizbangs().get(j).body))) {
                        gameRound.launchableWhizbangsController.getWhizbangs().get(j).body.setActive(false);
                        PhysicGameWorld.controller.destroyBody(gameRound.launchableWhizbangsController.getWhizbangs().get(j).body);
                        gameRound.launchableWhizbangsController.getWhizbangs().remove(gameRound.launchableWhizbangsController.getWhizbangs().get(j));
                    }
                }
            }
            if (gameRound.collectableObjectsController != null){
                if (gameRound.collectableObjectsController.getCollectableObjects().size()>0){
                    for (int j = (gameRound.collectableObjectsController.getCollectableObjects().size()-1); j>=0; j--){
                        if (flag.inZone(PhysicGameWorld.controller.getBodyPixelCoord(gameRound.collectableObjectsController.getCollectableObjects().get(j).body))) {
                            gameRound.collectableObjectsController.getCollectableObjects().get(j).body.setActive(false);
                            PhysicGameWorld.controller.destroyBody(gameRound.collectableObjectsController.getCollectableObjects().get(j).body);
                            gameRound.collectableObjectsController.getCollectableObjects().remove(gameRound.collectableObjectsController.getCollectableObjects().get(j));
                        }
                    }
                }
            }
        }
        for (int j = (gameRound.getPersons().size()-1); j >= 0; j--){
            if (activatingCondition != DELETE_ROUND_ELEMENTS) {
                updatePersonsInZoneClearing(gameRound, gameRound.getPersons().get(j));
            }
        }
        //System.out.println("Activating : " + activatingCondition );
    }

    private void deletePerson(Person person, GameRound gameRound){
        person.body.setActive(false);
        person.body.setAwake(false);
        PhysicGameWorld.controller.destroyBody(person.body);
        gameRound.removePerson(person);
    }

    private void updatePersonsInZoneClearing(GameRound gameRound, Person person){
        try {
            if (flag.inZone(PhysicGameWorld.controller.getBodyPixelCoord(person.body))) {
                if (person.getClass() != Soldier.class && activatingCondition == DELETE_EVERY_OBJECT_WITHOUT_PLAYER) {
                    deletePerson(person, gameRound);
                    System.out.println(person.getClass() + " was deleted");
                }
                else {
                    if ((activatingCondition == DELETE_EVERY_PERSON) || activatingCondition == DELETE_EVERY_OBJECT) {
                        person.body.resetMassData();
                        if (person.isAlive()) {
                            person.kill();
                        }
                        //
                        if (!PhysicGameWorld.controller.world.isLocked()) {
                            try {
                                person.body.setActive(false);
                                person.body.setAwake(false);
                                person.body.setTransform(person.body.getPosition(), 0f);
                                person.body.setGravityScale(0);
                                person.body.setType(BodyType.STATIC);
                            }
                            catch (AssertionError e){
                                PhysicGameWorld.assertionErrorAppears = true;
                                System.out.println("Assertion error by body from world deleting");
                            }
                            finally {
                                if (person.getClass() == Soldier.class) {
                                    if (!gameRound.isGameOverScreenShown()) {

                                        gameRound.addGameOverScreen();
                                        gameRound.setGameOver();
                                        gameRound.getSoundController().setAndPlayAudio(SoundsInGame.LOOSED);
                                        gameRound.getSoundController().noMoreAudio();

                                    }
                                }
                                else {
                                    gameRound.getSoundController().setAndPlayAudio(SoundsInGame.FALL_IN_WATER);
                                    person.setAppearedInClearingZone();
                                }
                            }
                        }
                        else{
                            System.out.println("World is locked; Can not deactivate the body");
                        }
                    }
                    else if (person.getClass() != Soldier.class && activatingCondition == DELETE_CORPSES){
                        if (!person.isAlive()){
                            person.body.setActive(false);
                            PhysicGameWorld.controller.destroyBody(person.body);
                            gameRound.removePerson(person);
                            System.out.println("Corpse of person " + person.getClass() + " was deleted");
                        }
                    }
                    else{
                        //System.out.println("This object can not be deleted");
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Can not add object for deleting");
        }

    }

    public void draw(GameCamera gameCamera){
        if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR){
            flag.draw(gameCamera);
        }
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        //saveMaster.createObjectsClearingZone();
        saveMaster.createSingleFlagZone();
        System.out.println("Data string for clearing zone" +saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }


    @Override
    public String getObjectToDisplayName(){
        return objectToDisplayName;
    }
}
