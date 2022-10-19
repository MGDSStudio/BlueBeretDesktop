package com.mgdsstudio.blueberet.graphic.bullettimescreen;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;

public class BulletTime2DScreen extends BulletTimeScreen{
    private Tileset tileset;
    private final ImageZoneSimpleData imageZoneSimpleData = InWorldObjectsGraphicData.whiteScreen;
    //private int tint = Program.engine.color(255,50);
    private final int MAX_TINT_FOR_WINDOWS = 200;
    private final int MIN_TINT_FOR_WINDOWS = 70;
    //private final int MAX_TINT_FOR_ANDROID = 110;
    //private final int MIN_TINT_FOR_ANDROID = 30;

    private int maxTint = MAX_TINT_FOR_WINDOWS;
    private int minTint = MIN_TINT_FOR_WINDOWS;
    private float tint = maxTint;

    private boolean useShotSpecificZooming = true;


    //private int actualPulseNumber = 0; // Actual pulse
    private float actualTintChangingVelocity;   // Alpha changing statement pro millisecond

    public BulletTime2DScreen() {
    }

    private void update(GameCamera gameCamera){
        if (activated) {
            if (mainStage == LONG_PULSATION){
                if (timerToNextStage.isTime()){
                    actualLongPulsationStage++;
                    timerToNextStage.setNewTimer(longPulseTime);
                    resetTintValue();
                    if (useShotSpecificZooming) gameCamera.getGameCameraController().addScalingByShot();
                    else gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);

                }
                if (actualLongPulsationStage>=longPulsesNumber){
                    mainStage = SHORT_PULSATION;
                    timerToNextStage.setNewTimer(brakingPulsationTimes[0]);
                    resetTintValue();
                }
            }
            if (mainStage == SHORT_PULSATION){
                if (timerToNextStage.isTime()){
                    actualShortPulsationStage++;
                    timerToNextStage.setNewTimer(brakingPulsationTimes[actualShortPulsationStage]);
                    resetTintValue();
                    if (useShotSpecificZooming) gameCamera.getGameCameraController().addScalingByShot();
                    else gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);
                }
                if (actualShortPulsationStage>=brakingPulsationTimes.length){
                    mainStage = END;
                    timerToNextStage.stop();
                    activated = false;
                }
            }
            updateActualTint();
        }
    }


    private void updateActualTint() {
        tint -=(Program.deltaTime*actualTintChangingVelocity);
        if (tint < minTint) tint = minTint;
        //System.out.println("Tint: " + tint);
    }

    private void calculateAlphaChangingVelocity(int time){
        float deltaTint = maxTint - minTint;
        actualTintChangingVelocity = deltaTint/time;
    }

    private void resetTintValue() {
        if (mainStage == LONG_PULSATION){
            tint = maxTint;
            calculateAlphaChangingVelocity(longPulseTime);
        }
        else if (mainStage == SHORT_PULSATION){
            tint = maxTint;
            calculateAlphaChangingVelocity(brakingPulsationTimes[actualShortPulsationStage]);
        }
    }

    public void loadGraphic(Tileset tileset){
        if (this.tileset == null) this.tileset = tileset;
    }

    @Override
    public void draw(GameCamera gameCamera){
        if (activated) {
            update(gameCamera);
            //Program.objectsFrame.pushMatrix();
            Program.objectsFrame.pushStyle();
            Program.objectsFrame.imageMode(PConstants.CENTER);
            Program.objectsFrame.tint(57, 100, 15, tint);
            Program.objectsFrame.image(tileset.getPicture().getImage(), Program.objectsFrame.width / 2, Program.objectsFrame.height / 2, Program.objectsFrame.width, Program.objectsFrame.height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
            Program.objectsFrame.popStyle();
            //Program.objectsFrame.popMatrix();
        }
    }


@Override
    public void activate(int time) {
        this.activated = true;
        mainStage = LONG_PULSATION;
        actualLongPulsationStage = 0;
        actualShortPulsationStage = 0;
        if (tileset == null) tileset = InWorldObjectsGraphicData.mainGraphicTileset;
        calculateTimes(time);
        if (timerToNextStage == null) timerToNextStage = new Timer(longPulseTime);
        else timerToNextStage.setNewTimer(longPulseTime);
        calculateAlphaChangingVelocity(longPulseTime);
    }

    private void calculateTimes(int time) {
        float allBrakingTime = 0;
        for (int i = 0; i < brakingPulsationTimes.length; i++){
            allBrakingTime+=brakingPulsationTimes[i];
        }
        float restTimeForNormalPulses = time-allBrakingTime;
        longPulsesNumber = (PApplet.ceil(restTimeForNormalPulses/NORMAL_TIME_FOR_LONG_PULSE));
        longPulseTime = (int) (restTimeForNormalPulses/ longPulsesNumber);
        System.out.println("Stages " + longPulsesNumber + "; Long stages time" + longPulseTime);
    }
}
