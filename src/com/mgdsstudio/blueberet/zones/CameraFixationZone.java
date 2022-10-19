package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PVector;

public class CameraFixationZone extends SingleFlagZone{
    // Works only for one activating condition
    public final static String CLASS_NAME = "CameraFixationZone";
    public final static boolean MIN_SCALE = false;
    public final static boolean MAX_SCALE = true;
    private boolean cameraScale = MIN_SCALE;

    public final static int PLAYER_LEAVES_ZONE = ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE;
    public final static int ENEMIES_LEAVES_ZONE = ACTIVATING_WHEN_ZONE_FREE_FROM_PERSONS;
    public final static int WITHOUT_DEACTIVATING = -1;
    private int deactivatingCondition = PLAYER_LEAVES_ZONE;
    private boolean activated;

    public final static boolean ONCE = false;
    public final static boolean REPEATING = true;
    private boolean repeateability = ONCE;
    //private final PVector pos = new PVector();
    private final float x, y;
    private boolean ended = false;

    public CameraFixationZone(Flag flag, int x, int y, int activatingCondition, int deactivatingCondition, boolean cameraScale, boolean repeateability){
        this.flag = flag;
        this.activatingCondition = activatingCondition;
        this.cameraScale = cameraScale;
        this.repeateability = repeateability;
        this.deactivatingCondition = deactivatingCondition;
        this.x = x;
        this.y = y;
    }

    /*
    public EndLevelZone(GameObjectDataForStoreInEditor objectData) {
        this.flag = objectData.getFlag();
        this.activatingCondition = objectData.getGoal();
    }
     */

    public CameraFixationZone(GameObjectDataForStoreInEditor objectData){
        this.flag = objectData.getFlag();
        this.activatingCondition = objectData.getActivatedBy();
        this.cameraScale = objectData.isCameraScale();
        this.repeateability = objectData.getRepeateability();
        this.deactivatingCondition = objectData.getDeactivatedBy();
        this.x = objectData.getConcentratingPointX();
        this.y = objectData.getConcentratingPointY();
    }

    private void updateActivating(GameRound gameRound, GameCameraController gameCameraController){
        if (activatingCondition == ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE){
            if (isGameObjectInZone(gameRound.getPlayer())){
                if (!activated) {
                    activated = true;
                }
                gameCameraController.addPosToConcentrate(new PVector(x,y), GameCameraController.CAMERA_FIXATION_ZONE);
                gameCameraController.setConstantScale(cameraScale);
            }
        }
    }

    public void update(GameRound gameRound, GameCameraController gameCameraController){
        if (!ended) {
            updateActivating(gameRound, gameCameraController);
            if (activated) updateDeactivating(gameRound, gameCameraController);
        }
    }

    private void updateDeactivating(GameRound gameRound, GameCameraController gameCameraController) {
        if (deactivatingCondition == PLAYER_LEAVES_ZONE){
            if (!isGameObjectInZone(gameRound.getPlayer())){
                System.out.println("player leaves the zone");
                activated = false;
                if (repeateability == ONCE){
                    ended = true;
                }
                else {
                    ended = false;

                }
                if (Program.debug) System.out.println("Camera is released ");
            }
        }
        else if (deactivatingCondition == ENEMIES_LEAVES_ZONE){
            boolean noEnemies = true;
            for (Person person : gameRound.getPersons()){
                if (isGameObjectInZone(person)){
                    if (!gameRound.getPlayer().equals(person)) {
                        noEnemies = false;
                        break;
                    }
                }
            }
            if (noEnemies){
                activated = false;
                if (repeateability == ONCE) {
                    ended = true;
                    if (Program.debug) System.out.println("Camera is released ");
                }
            }
        }
        if (!activated){
            if (gameCameraController.areThePositionForConcentrate()){
                gameCameraController.deletePosToConcentrate();
                System.out.println("delete from camera zone");
                gameCameraController.deleteConstantScale();
            }
        }
    }

    @Override
    public void draw(GameCamera gameCamera){
        super.draw(gameCamera);

    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createCameraFixationZone();
        System.out.println("Data string for camera fixation zone" + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    public boolean isCameraScale() {
        return cameraScale;
    }

    public int getDeactivatingCondition() {
        return deactivatingCondition;
    }

    public boolean isRepeateability() {
        return repeateability;
    }

    public float getConcentratingX() {
        return x;
    }

    public float getConcentratingY() {
        return y;
    }

    public void switchOff() {
        activated = true;
        repeateability = ONCE;
        ended = true;
    }
}
