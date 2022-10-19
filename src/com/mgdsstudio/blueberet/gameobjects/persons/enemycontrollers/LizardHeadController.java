package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;

public class LizardHeadController {
    public final static ImageZoneSimpleData eyesOpened = new ImageZoneSimpleData(95,137, 126,159);
    public final static ImageZoneSimpleData eyesClosed = new ImageZoneSimpleData(95,137+42, 126,159+42);
    public final static ImageZoneSimpleData mouthOpened = new ImageZoneSimpleData(95,137+84, 126,159+84);
    private final Timer blinkTimer ;
    private final int timeToNextEyesClosedCycle;
    private final int timeToNextEyesOpenCycle;

    public final static int BLINKING = 0;
    public final static int LUGGAGE_FORWARD = LizardAttackController.LUGGAGE_FORWARD;
    public final static int LUGGAGE_HOLD = LizardAttackController.LUGGAGE_HOLD;

    private int statement = BLINKING;

    private final static boolean BLINKING_OPENED = false;
    private final static boolean BLINKING_CLOSED = true;
    private boolean blinkingStage  =BLINKING_OPENED;
    private LizardAttackController lizardAttackController;

    private boolean withDirectDrawingToScreen = false;

    public static final int HEAD_EYES_OPENED = 0;
    public static final int HEAD_EYES_CLOSED = 1;
    public static final int HEAD_MOUTH_OPENED = 2;
    public LizardHeadController(LizardAttackController lizardAttackController, int timeToNextEyesClosedCycle) {
        this.lizardAttackController =  lizardAttackController;
        this.timeToNextEyesClosedCycle = timeToNextEyesClosedCycle;
        this.timeToNextEyesOpenCycle = timeToNextEyesClosedCycle/5;
        blinkTimer = new Timer(timeToNextEyesOpenCycle);
    }

    public void update(){
        if (statement == BLINKING){
            if (blinkingStage == BLINKING_OPENED){
                if (blinkTimer.isTime()){
                    blinkingStage = BLINKING_CLOSED;
                    blinkTimer.setNewTimer(timeToNextEyesOpenCycle);
                }
            }
            else {
                if (blinkTimer.isTime()){
                    blinkingStage = BLINKING_OPENED;
                    blinkTimer.setNewTimer(timeToNextEyesClosedCycle);
                }
            }
        }
        else {
            statement = lizardAttackController.getStatement();
            if (statement < LizardAttackController.LUGGAGE_FORWARD  || statement > LizardAttackController.LUGGAGE_BACK){
                statement = BLINKING;
            }
        }
        //System.out.println("Statement: " + statement);
    }

    public void startAttack(){
        statement = LUGGAGE_FORWARD;
    }

    public ImageZoneSimpleData getActualHeadImageData(){
        if (statement == BLINKING){
            if (blinkingStage == BLINKING_OPENED){
                return eyesOpened;
            }
            else return eyesClosed;
        }
        else {
            if (statement == LUGGAGE_HOLD){
                return mouthOpened;
            }
            else return eyesOpened;
        }

    }

    /*
    public int getActualHeadType(){
        if (statement == BLINKING){
            if (blinkingStage == BLINKING_OPENED){
                return HEAD_EYES_OPENED;
            }
            else return HEAD_EYES_CLOSED;
        }
        else {
            if (statement == LUGGAGE_HOLD){
                return HEAD_MOUTH_OPENED;
            }
            else return HEAD_E;
        }
    }*/

    public int getActualHeadType(int statement){
        if (statement == BLINKING){
            if (blinkingStage == BLINKING_OPENED){
                return EnemiesAnimationController.HEAD_BY_AIM_EYES_OPENED;
            }
            else return EnemiesAnimationController.HEAD_BY_AIM_EYES_CLOSED;
        }
        else {
            if (statement == LUGGAGE_HOLD){
                return EnemiesAnimationController.HEAD_BY_AIM_MOUTH_OPENED;
            }
            else return EnemiesAnimationController.HEAD_BY_AIM_EYES_OPENED;
        }
    }
    /*
    public int getActualHeadType(int statement){
        if (statement == BLINKING){
            if (blinkingStage == BLINKING_OPENED){
                return EnemiesAnimationController.HEAD_BY_AIM_EYES_OPENED;
            }
            else return EnemiesAnimationController.HEAD_BY_AIM_EYES_CLOSED;
        }
        else {
            if (statement == LUGGAGE_HOLD){
                return EnemiesAnimationController.HEAD_BY_AIM_MOUTH_OPENED;
            }
            else return EnemiesAnimationController.HEAD_BY_AIM_EYES_OPENED;
        }
    }
     */

}
