package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import processing.core.PVector;



public class GameCameraController {
    private PVector posToConcentrate;
    private GameCamera gameCamera;
    public final static boolean WITH_CAMERA_RELAXING = true;
    private final float criticalVelocityToAttachToThePlayer = 2400f;
    private final float SCALE_JUMP_BY_SHOT = 0.035f;
    private boolean WITH_START_CONCENTRATING_PERIOD = true;
    private boolean startPeriodIsEnded = true;
    private int START_CONCENTRATING_TIME = 2000;
    private Timer startTimer;
    private boolean constantScaleSet;
    private float constantScale;

    public final static boolean MESSAGE = true;
    public final static boolean CAMERA_FIXATION_ZONE = false;
    private boolean concentratePositionSource;

        private boolean timerStarted;

    public GameCameraController(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        if (WITH_START_CONCENTRATING_PERIOD) {
            startPeriodIsEnded = false;
        }
    }

    public void update(Person person, PlayerControl playerControl){
        if (WITH_START_CONCENTRATING_PERIOD && !startPeriodIsEnded && !timerStarted){
            startTimer = new Timer(START_CONCENTRATING_TIME);
            timerStarted = true;
        }
        if (posToConcentrate == null) {
            boolean concentrateCameraOnPlayer = playerControl.mustCameraConcentrateToPlayer();
            if (!startPeriodIsEnded) {
                if (startTimer.isTime()) {
                    startTimer.stop();
                    startPeriodIsEnded = true;
                }
                else concentrateCameraOnPlayer = true;
            }
            if (person.isAlive()) {
                if (person.getActualWeapon().isReloading() || concentrateCameraOnPlayer) {
                    gameCamera.updateForPosition(person.getPixelPosition(), GameCamera.maxScale);
                }
                else if (person.isTransferingThroughPortal()) {
                    System.out.println("Player transfers through a portal");
                    gameCamera.updateForPosition(person.getPixelPosition(), GameCamera.maxScale);
                }
                else {
                    if (person.getStatement() == Person.IN_AIR) {
                        if (PhysicGameWorld.controller.scalarWorldToPixels(person.body.getLinearVelocity().length()) < criticalVelocityToAttachToThePlayer) {

                            gameCamera.updateForCharacter(person, GameCamera.CAMERA_TO_AIM_PLACE);
                        }
                        else {
                            //System.out.println("Velocity is critical " + PhysicGameWorld.controller.scalarWorldToPixels(person.body.getLinearVelocity().length()));
                            gameCamera.updateForPosition(person.getPixelPosition(), GameCamera.maxScale);
                        }
                    }
                    else {
                        gameCamera.updateForCharacter(person, GameCamera.CAMERA_TO_AIM_PLACE);
                    }
                }
            }
            else {
                if (Program.OS == Program.DESKTOP || WITH_CAMERA_RELAXING) {
                    if (!person.isSleeped() && !gameCamera.isRelaxed()) {
                        gameCamera.relaxCameraSpring();
                    }
                }
                gameCamera.updateForPosition(person.getPixelPosition(), GameCamera.minScale);
            }
        }
        else
        {
            if (Program.engine.frameCount%8 == 0) {
                if (person.isDead()){
                    if (person.getClass() == Soldier.class){
                        posToConcentrate = null;
                    }
                }
            }
            if (constantScaleSet){
                //if (constantScale == CameraFixationZone.MAX_SCALE) gameCamera.update(posToConcentrate, GameCamera.maxScale);
                gameCamera.updateForPosition(posToConcentrate, constantScale);
            }
            else gameCamera.updateForPosition(posToConcentrate, GameCamera.minScale);
        }
    }

    public void deletePosToConcentrate(){
        if (posToConcentrate != null) {
            posToConcentrate = null;
            gameCamera.setInSomeCameraFixationZone(false);
            System.out.println("Position for concentrate was deleted. Source " + concentratePositionSource);
        }
    }

    public void addPosToConcentrate(PVector posToConcentrate, boolean source) {
        this.posToConcentrate = posToConcentrate;
        gameCamera.setInSomeCameraFixationZone(true);
        this.concentratePositionSource = source;
    }

    public boolean areThePositionForConcentrate() {
        if (posToConcentrate != null ) return true;
        else return false;
    }

    public void setConstantScale(boolean cameraScale) {
        constantScaleSet = true;
        if (cameraScale == CameraFixationZone.MIN_SCALE) constantScale = GameCamera.minScale;
        else constantScale = GameCamera.maxScale;
        //constantScale = cameraScale;
    }

    public void deleteConstantScale() {
        constantScaleSet = false;
    }

    public boolean isConcentratePositionSource() {
        return concentratePositionSource;
    }

    public void addScalingByShot() {
        if (posToConcentrate == null) {
            if (gameCamera.getScale()<= (GameCamera.maxScale-0.01f)) {
                gameCamera.setScale(gameCamera.getScale() + SCALE_JUMP_BY_SHOT);
            }
            else {
                gameCamera.setScale(GameCamera.maxScale);
                if (Program.debug){
                    System.out.println("It zoom is max");
                }
            }
        }
    }
}
