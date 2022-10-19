package com.mgdsstudio.blueberet.graphic.bullettimescreen;

import com.mgdsstudio.blueberet.gamecontrollers.LightsController;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.LightSource;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PApplet;

public  class BulletTime3DScreen extends BulletTimeScreen{
    private LightSource lightSource;
    private GameRound gameRound;
    private LightsController lightsController;
    private final int MAX_TINT_FOR_WINDOWS = 110;
    private final int MIN_TINT_FOR_WINDOWS = 30;
    private int maxTint = MAX_TINT_FOR_WINDOWS;
    private int minTint = MIN_TINT_FOR_WINDOWS;
    private float actualTintChangingVelocity;


    public BulletTime3DScreen(GameRound gameRound) {
        this.gameRound =gameRound;
    }


    public void activate(int time){
        this.activated = true;
        mainStage = LONG_PULSATION;
        actualLongPulsationStage = 0;
        actualShortPulsationStage = 0;
        longPulseTime = NORMAL_TIME_FOR_LONG_PULSE;
        calculateTimes(time);
        if (timerToNextStage == null) timerToNextStage = new Timer(longPulseTime);
        else timerToNextStage.setNewTimer(longPulseTime);
        calculateDistanceChangingVelocity(longPulseTime);

        gameRound.addBulletTimeLight(longPulseTime);
        System.out.println("Bullet time activated for " + longPulseTime);
    }


    private void calculateDistanceChangingVelocity(int time) {
        float deltaTint = maxTint - minTint;
        actualTintChangingVelocity = deltaTint/time;
    }



    @Override
    public void draw(GameCamera gameCamera) {
        if (activated) {
            update(gameCamera);
        }
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

    protected void update(GameCamera gameCamera){
        if (activated) {
            if (mainStage == LONG_PULSATION){
                if (timerToNextStage.isTime()){
                    actualLongPulsationStage++;
                    timerToNextStage.setNewTimer(longPulseTime);
                    gameRound.addBulletTimeLight(longPulseTime);
                    //resetTintValue();
                    //gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);
                    gameCamera.getGameCameraController().addScalingByShot();
                }
                if (actualLongPulsationStage>=longPulsesNumber){
                    mainStage = SHORT_PULSATION;
                    timerToNextStage.setNewTimer(brakingPulsationTimes[0]);
                    gameRound.addBulletTimeLight(brakingPulsationTimes[0]);
                    //resetTintValue();
                }
            }
            if (mainStage == SHORT_PULSATION){
                if (timerToNextStage.isTime()){
                    actualShortPulsationStage++;
                    int time = 0;
                    if (actualShortPulsationStage >= brakingPulsationTimes.length) time = brakingPulsationTimes[brakingPulsationTimes.length-1];
                    else time =brakingPulsationTimes[actualShortPulsationStage];
                    timerToNextStage.setNewTimer(time);
                    gameRound.addBulletTimeLight(time);
                    //resetTintValue();
                    //gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);
                    gameCamera.getGameCameraController().addScalingByShot();

                }
                if (actualShortPulsationStage>=brakingPulsationTimes.length){
                    mainStage = END;
                    timerToNextStage.stop();
                    activated = false;
                }
            }
            //updateActualTint();
        }
    }

    /*
    protected void update(GameCamera gameCamera){
        if (activated) {
            if (mainStage == LONG_PULSATION){
                if (timerToNextStage.isTime()){
                    actualLongPulsationStage++;
                    timerToNextStage.setNewTimer(longPulseTime);
                    gameRound.addBulletTimeLight(longPulseTime);
                    //resetTintValue();
                    gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);


                }
                if (actualLongPulsationStage>=longPulsesNumber){
                    mainStage = SHORT_PULSATION;
                    timerToNextStage.setNewTimer(brakingPulsationTimes[0]);
                    gameRound.addBulletTimeLight(brakingPulsationTimes[0]);
                    //resetTintValue();
                }
            }
            if (mainStage == SHORT_PULSATION){
                if (timerToNextStage.isTime()){
                    actualShortPulsationStage++;
                    timerToNextStage.setNewTimer(brakingPulsationTimes[actualShortPulsationStage]);
                    gameRound.addBulletTimeLight(brakingPulsationTimes[actualShortPulsationStage]);
                    //resetTintValue();
                    gameCamera.setScale(gameCamera.getScale()+cameraAddingScaleByPulse);
                }
                if (actualShortPulsationStage>=brakingPulsationTimes.length){
                    mainStage = END;
                    timerToNextStage.stop();
                    activated = false;
                }
            }
            //updateActualTint();
        }
    }
     */
}
