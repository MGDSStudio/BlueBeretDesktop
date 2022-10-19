package com.mgdsstudio.blueberet.graphic.howtoplayanimation;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import processing.core.PApplet;
import processing.core.PConstants;

class HandScalingController {
    static final float NORMAL_MIN_SCALE = 0.85f;
    static final float NORMAL_MAX_SCALE = 1.15f;

    private float scaleRelativeValueForSinuszooming = 0.3f;
    private int period = 2000;
    private float pressedScale = 0.6f;
    private int timeToBeFullPressed = 800;
    private int normalWidth, normalHeight;
    private int actualWidth, actualHeight;
    private float startScale = 1f, endScale, actualScale = startScale;
    private int startTime = -1;
    private Timer scalingTimer;
    private int timeForScalling;
    //private boolean targetScaleAchieved;

    //final static int STATEMENT_TO_MAX_SCALE = 1;
    //final static int STATEMENT_TO_MIN_SCALE = 2;
    //final static int STATEMENT_HOLD_SCALE = 0;
    private int statement;

    final static int BEHAVIOUR_NOTHING = 1;
    final static int BEHAVIOUR_VARIABLE_SCALE = 2;
    final static int BEHAVIOUR_TO_MAX_SCALE = 3;
    final static int BEHAVIOUR_TO_MIN_SCALE = 4;
    final static int BEHAVIOUR_MAKE_SMALL = 5;
    final static int BEHAVIOUR_MAKE_LARGE = 5;
    private int behaviour;

    public HandScalingController(int behaviour) {
        this.behaviour = behaviour;
    }

    public HandScalingController(int behaviour, int time, float startScale, float endScale) {
        this.behaviour = behaviour;
        this.timeForScalling = time;
        this.startScale = startScale;
        this.endScale = endScale;
        init(behaviour);
    }

    /*
    public HandScalingController(int behaviour, int time) {
        this.behaviour = behaviour;
        this.timeForScalling = time;
        this.endScale = pressedScale;
        init(behaviour);
    }*/

    private void init(int behaviour) {
        if (behaviour == BEHAVIOUR_TO_MIN_SCALE || behaviour == BEHAVIOUR_TO_MAX_SCALE){
            if (scalingTimer == null){
                scalingTimer = new Timer(timeForScalling);
            }
            else scalingTimer.setNewTimer(timeForScalling);
            startScale = actualScale;
        }
        else {
            System.out.println("Nothing to init");
        }
    }

    void update(float time){
        if (behaviour == BEHAVIOUR_VARIABLE_SCALE){
            float newScale  = startScale + calculateActualVariableValue(time);
            actualScale = newScale;
        }
        else if (behaviour == BEHAVIOUR_NOTHING){

        }
        else if (behaviour == BEHAVIOUR_TO_MAX_SCALE){
            updateScallingToMax();
        }
        else if (behaviour == BEHAVIOUR_TO_MIN_SCALE){
            updateScallingToMin();
        }
        /*else if (behaviour == BEHAVIOUR_MAKE_SMALL){
            float newScale = calculateActualScaleForPressing();
        }*/
        //System.out.println("Scale " + actualScale);
    }

    private void updateScallingToMax() {
        float deltaScale = PApplet.abs(endScale-startScale);
        float deltaTimeInSec = (float)(timeForScalling-scalingTimer.getRestTime())/1000f;
        float wayFromStartAlongX = deltaScale* deltaTimeInSec;
        actualScale = startScale+wayFromStartAlongX;
        //System.out.println("Delta scale : " + deltaScale);
    }

    private void updateScallingToMin() {
        float deltaScale = PApplet.abs(endScale-startScale);
        float deltaTimeInSec = (float)(timeForScalling-scalingTimer.getRestTime())/1000f;
        float wayFromStartAlongX = deltaScale* deltaTimeInSec;
        actualScale = startScale-wayFromStartAlongX;
    }

    private float calculateActualScaleForPressing() {
        return 1;
    }

    private float calculateActualVariableValue(float time){
        return scaleRelativeValueForSinuszooming * PApplet.sin(PConstants.PI*(1f/2f*period)*time);
    }

    /*
    boolean isTargetScaleAchived(){
        if (behaviour == BEHAVIOUR_TO_MAX_SCALE){
            return targetScaleAchieved;
        }
        else if (behaviour == BEHAVIOUR_TO_MIN_SCALE){
            return targetScaleAchieved;
        }
        else {
            System.out.println("For this behaviour there is no target scale !");
            return false;
        }
    }*/

    float getActualScale(){
        return actualScale;
    }



    void setBehaviour(int behaviour) {
        this.behaviour = behaviour;
        if (behaviour == BEHAVIOUR_TO_MAX_SCALE || behaviour == BEHAVIOUR_TO_MIN_SCALE){
            init(behaviour);
        }
        else if (behaviour == BEHAVIOUR_MAKE_SMALL){
            //targetScaleAchieved = false;
            if (scalingTimer == null) scalingTimer = new Timer(timeToBeFullPressed);
            else scalingTimer.setNewTimer(timeToBeFullPressed);
        }
        else if (behaviour == BEHAVIOUR_MAKE_LARGE){
            if (scalingTimer == null) scalingTimer = new Timer(timeToBeFullPressed);
            else scalingTimer.setNewTimer(timeToBeFullPressed);
        }
    }

    public void setEndScale(float endScale) {
        this.endScale = endScale;
    }

    public int getBehaviour() {
        return behaviour;
    }

    public void setActualScale(float actualScale) {
        this.actualScale = actualScale;
    }
}
