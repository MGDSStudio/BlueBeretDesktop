package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class AbstractTrigger extends SingleFlagZone implements IDrawable {
    final public static String CLASS_NAME = "Trigger";
    final static private String objectToDisplayName = CLASS_NAME;
    protected int action;
    public final static int NO_ACTION = -1;
    public final static int DELETE_OBJECT_WITH_BODY = 0;
    public final static int KILL_OBJECT_WITH_BODY = 1;
    public final static int DELETE_GRAPHIC_OBJECT = 2;
    public final static int STOP_ANIMATION = 3;
    public final static int RESUME_ANIMATION = 4;
    public final static int CHANGE_BODY_TYPE_TO_DYNAMIC = 5;
    public final static int RESUME_ANIMATION_IF_ACTIVATED_STOP_IF_DEACTIVATED = 6;
    public final static int TELEPORT_OBJECT = 7;
    protected boolean canBeDeleted;
    private boolean mustBeActivated;
    private int delay;
    private Timer delayTimer;
    protected GameObject gameObjectToBeActivated;
    protected IndependentOnScreenGraphic graphic;
    public final static int OBJECT_WITH_BODY = 0;
    public final static int GRAPHIC = 1;
    private boolean once;

    protected final static boolean SWITCH_ON_ACTION = false;
    protected final static boolean SWITCH_OFF_ACTION = true;
    protected boolean actionTypeToBeActivated = SWITCH_ON_ACTION;
    private int uniqueId = -1;


    AbstractTrigger(GameRound gameRound, Flag flag, int objectX, int objectY, int action, int activatingCondition, int objectType, int delay, boolean once){
        this.flag = flag;
        this.action = action;
        this.delay = delay;
        this.activatingCondition = activatingCondition;
        this.once = once;
        if (Program.debug){
            System.out.println("For this trigger action: " + action + " and type: " + objectType);
        }
        if ((action == DELETE_OBJECT_WITH_BODY || action == KILL_OBJECT_WITH_BODY || action == CHANGE_BODY_TYPE_TO_DYNAMIC || action == TELEPORT_OBJECT) && objectType == OBJECT_WITH_BODY){
            gameObjectToBeActivated = gameRound.getGameObjectByCoordinate(objectX, objectY);
            if (gameObjectToBeActivated == null){
                System.out.println("Physic object at point " + objectX + "x" + objectY + " was not founded");
                //System.out.println("persons are " + gameRound.getPersons().size());
                //System.out.println("Pos: " + gameRound.getPersons().get(0).getPixelPosition());
                canBeDeleted = true;
            }
            else System.out.println("Object founded " + gameObjectToBeActivated.getClass() + " at pos: " + objectX + " x " + objectY);
        }
        else if ((action == DELETE_GRAPHIC_OBJECT || action == STOP_ANIMATION || action == RESUME_ANIMATION || action == RESUME_ANIMATION_IF_ACTIVATED_STOP_IF_DEACTIVATED) && objectType == GRAPHIC){
            graphic = gameRound.getGraphicAtPoint(new PVector(objectX, objectY));
            if (graphic == null){
                System.out.println("Graphic Object at point " + objectX + "x" + objectY + " was not founded");
                canBeDeleted = true;
            }
            else System.out.println("Graphic founded " + graphic.getClass());
        }
        else {
            System.out.println("No action " + action + " for object type " + objectType);
            //canBeDeleted = true;
        }



        /*if (gameObjectToBeActivated == null && graphic == null) {
            if (gameObjectToBeActivated == null) System.out.println("Graphic element was not founded for the trigger");
            if (graphic == null) System.out.println("Graphic element was not founded for the trigger");
            this.action = NO_ACTION;
            canBeDeleted = true;
        }*/
    }



    public void update (GameRound gameRound){
        //System.out.println("Try to find player is in zone with cond " + activatingCondition + " and can be deleted " + canBeDeleted);
        if (!canBeDeleted){
            if (activatingCondition == ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE){

                if (isPlayerInZone(gameRound)){
                    //
                    mustBeActivated = true;
                }
            }
            else if (activatingCondition == ACTIVATING_WHEN_ZONE_FREE_FROM_PERSONS){
                if (isZoneFreeFromPersons(gameRound)){
                    mustBeActivated = true;
                }
            }
            else if (activatingCondition == ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE_AND_ZONE_FREE_FROM_PERSONS){
                if (isZoneFreeFromPersons(gameRound)){
                    if (isPlayerInZone(gameRound)) {
                        mustBeActivated = true;
                    }
                }
            }
            else if (activatingCondition == ACTIVATING_WHEN_ZONE_HAS_PERSONS){
                if (!isZoneFreeFromPersons(gameRound)){
                    mustBeActivated = true;
                }
            }
            else if (activatingCondition == WORK_ONLY_IF_ZONE_HAS_ALIVE_PERSONS){
                ArrayList <Person> persons = gameRound.getPersons();
                boolean bodyFounded = false;
                for (Person person : persons){
                    if (person.isAlive()){
                        if (GameMechanics.isPointInRect(person.getPixelPosition(), flag)){
                            actionTypeToBeActivated = SWITCH_ON_ACTION;
                            bodyFounded = true;
                            break;
                        }
                    }
                }
                if (!bodyFounded){
                    actionTypeToBeActivated = SWITCH_OFF_ACTION;
                }
                mustBeActivated = true;
            }
        }
        if (mustBeActivated == true && !canBeDeleted) updateEnding();
    }



    protected abstract void makeAction(GameRound gameRound);

    protected void deleteGameObject(GameRound gameRound) {
        gameRound.deleteObjectsFromMap(gameObjectToBeActivated);
        gameObjectToBeActivated.body.setActive(false);
        PhysicGameWorld.controller.destroyBody(gameObjectToBeActivated.body);
    }

    private void updateEnding() {
        if (delay == 0){
            makeAction(SingleGameElement.gameRound);
            if (once) canBeDeleted = true;
        }
        else {
            if (delayTimer == null) delayTimer = new Timer(delay);
            if (delayTimer.isTime()){
                makeAction(SingleGameElement.gameRound);
                if (once) canBeDeleted = true;
                else delayTimer = null;
            }
        }
        mustBeActivated = false;
    }




    public void draw(GameCamera gameCamera){
        if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR){
            flag.draw(gameCamera);
        }
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createSingleFlagZone();
        System.out.println("Data string for trigger" +saveMaster.getDataString());
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

    @Override
    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    @Override
    public int getUniqueId() {
        return uniqueId;
    }
}
