package com.mgdsstudio.blueberet.graphic.howtoplayanimation;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl.AbstractGirlAnimation;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PGraphics;

class HandMovementController implements IBehaviours {
    //private final int BEHAVIOUR_PRESSING = 0;
    //private final int BEHAVIOUR_PRESS_AND_SLIDE = 1;
    private ImageWithData controlAreaImage;;
    private int behaviour;
    private int timeToNextStage;
    private Timer timer;
    private Coordinate start, end, actualHandPos;
    private TouchingHand touchingHand;
    private HandScalingController scalingController;
    private CrackAnimation crackAnimation;
    private int actualStage, prevStage;
    private int stagesNumber;
    private AbstractGirlAnimation girlAnimation;
    private  boolean firstLoop = true;


    public HandMovementController(PApplet engine, int behaviour, int timeToNextStage, Coordinate start, Coordinate end, boolean side) {
        this.behaviour = behaviour;
        this.timeToNextStage = timeToNextStage;
        this.start = start;
        this.end = end;
        actualHandPos = new Coordinate(start.x, start.y);
        touchingHand = new TouchingHand(this, side);
        touchingHand.setActualPos(actualHandPos);
        createScaleController(behaviour);
        setStagesNumber(behaviour);
        initAnimation(engine);
    }

    private void createScaleController(int behaviour) {
        int scaleBehaviour = 0;
        float startScale = 1f;
        float endScale = 1f;
        if (behaviour == PRESS_AND_BACK){
            scaleBehaviour = HandScalingController.BEHAVIOUR_NOTHING;
        }
        else if (behaviour == PRESS_AND_HIDE || behaviour == PRESS || behaviour == PRESS_SLIDE_BACK_HIDE){
            scaleBehaviour = HandScalingController.BEHAVIOUR_NOTHING;
            startScale = 1f;
            endScale = HandScalingController.NORMAL_MIN_SCALE;
        }
        else {
            System.out.println("There are no data for this behaviour");
        }
        scalingController = new HandScalingController(scaleBehaviour, timeToNextStage, startScale, endScale);
    }

    private void initAnimation(PApplet engine) {
        int diameter = (int) (OnScreenButton.normalDimention*2.5f);
        int x = (int) start.x;
        int y = (int) start.y;
        crackAnimation = new CrackAnimation(engine, GameMenusController.sourceFile,diameter, diameter,  x,y, timeToNextStage);
    }

    private void setStagesNumber(int behaviour) {
        actualStage = 4;
        if (behaviour == PRESS_SLIDE_BACK_HIDE){
            stagesNumber = 4;
        }
        else if (behaviour == PRESS_AND_BACK){
            stagesNumber = 4;
        }
        else if (behaviour == WAIT_SHORT_PRESS_BACK){
            stagesNumber = 4;
        }
        else {
            System.out.println("No data about stages number for this behaviour");
            stagesNumber = 2;
        }
        prevStage = actualStage;
        if (timer == null) timer = new Timer(timeToNextStage);
        else timer.setNewTimer(timeToNextStage);
    }

    public void update() {
        if (behaviour != DO_NOT_SHOW) {
            if (timer.isTime()) {
                timer.setNewTimer(timeToNextStage);
                actualStage++;
                if (actualStage > stagesNumber) {
                    actualStage = 0;
                }
            }
            if (behaviour == PRESS_SLIDE_BACK_HIDE) {
                updatePressSlideBackHideAction();
            }
            else if (behaviour == PRESS_AND_BACK) {
                updatePressSlideBackHideAction();
            }
            else if (behaviour == WAIT_SHORT_PRESS_BACK) {
                updateWaitShortPressBackHideAction();
            }
            scalingController.update(Program.engine.millis());
        }
       if ((prevStage != actualStage) && actualStage == 1){
           scalingController.setBehaviour(HandScalingController.BEHAVIOUR_TO_MIN_SCALE);
       }
       if (prevStage != actualStage){
           if (girlAnimation != null) {
               flip();
               girlAnimation.setStage(actualStage);
           }
       }
       prevStage = actualStage;
       crackAnimation.update();
    }

    private void updateWaitShortPressBackHideAction() {

        if (actualStage == 0){
            actualHandPos.x = -9999;
            actualHandPos.y = -9999;
            if (crackAnimation.getActualStage() != CrackAnimation.NOTHING) {
                crackAnimation.setActualStage(CrackAnimation.NOTHING);

            }
        }
        if (actualStage == 1){
            actualHandPos.x = start.x;
            actualHandPos.y = start.y;
            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_TO_MIN_SCALE){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_TO_MIN_SCALE);
                scalingController.setEndScale(HandScalingController.NORMAL_MIN_SCALE);
                crackAnimation.restart();
            }
        }
        else if (actualStage == 2){
            /*
            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_NOTHING){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_NOTHING);
                crackAnimation.setActualStage(CrackAnimation.STAGE_SHOW);
            }
            float wayAlongX = end.x-start.x;
            float wayAlongY = end.y-start.y;
            float deltaTimeInSec = (float)(timeToNextStage-timer.restTime())/1000f;
            float wayFromStartAlongX = wayAlongX* deltaTimeInSec;
            float wayFromStartAlongY = wayAlongY* deltaTimeInSec;
            actualHandPos.x = start.x+wayFromStartAlongX;
            actualHandPos.y = start.y+ wayFromStartAlongY;
             */

            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_TO_MAX_SCALE){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_TO_MAX_SCALE);
                scalingController.setEndScale(HandScalingController.NORMAL_MAX_SCALE);
                crackAnimation.setActualStage(CrackAnimation.STAGE_HIDING);
            }
            actualHandPos.x = end.x;
            actualHandPos.y = end.y;
        }
        else if (actualStage >= stagesNumber){
            actualHandPos.x = -9999;
            actualHandPos.y = -9999;
            if (crackAnimation.getActualStage() != CrackAnimation.NOTHING) {
                crackAnimation.setActualStage(CrackAnimation.NOTHING);

            }
        }

    }

    private void flip(){
        if (firstLoop == false) {
            if (actualStage == ((stagesNumber))){
                if (girlAnimation.mustBeControlButtonFlipped()) {
                    if (girlAnimation.isFlip()) {
                        girlAnimation.setFlip(false);
                        controlAreaImage.reverseFlip();
                    }
                    else {
                    girlAnimation.setFlip(true);
                    controlAreaImage.reverseFlip();
                    }
                }
                System.out.println("Flip was changed");
            }
        }
        else firstLoop = false;
    }


    private void updatePressSlideBackHideAction() {
        if (actualStage == 0){
            actualHandPos.x = start.x;
            actualHandPos.y = start.y;
            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_TO_MIN_SCALE){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_TO_MIN_SCALE);
                scalingController.setEndScale(HandScalingController.NORMAL_MIN_SCALE);
                crackAnimation.restart();
            }
        }
        if (actualStage == 1){
            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_NOTHING){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_NOTHING);
                crackAnimation.setActualStage(CrackAnimation.STAGE_SHOW);
            }
            float wayAlongX = end.x-start.x;
            float wayAlongY = end.y-start.y;
            float deltaTimeInSec = (float)(timeToNextStage-timer.getRestTime())/1000f;
            float wayFromStartAlongX = wayAlongX* deltaTimeInSec;
            float wayFromStartAlongY = wayAlongY* deltaTimeInSec;
            actualHandPos.x = start.x+wayFromStartAlongX;
            actualHandPos.y = start.y+ wayFromStartAlongY;
        }
        else if (actualStage == 2){
            if (scalingController.getBehaviour() != HandScalingController.BEHAVIOUR_TO_MAX_SCALE){
                scalingController.setBehaviour(HandScalingController.BEHAVIOUR_TO_MAX_SCALE);
                scalingController.setEndScale(HandScalingController.NORMAL_MAX_SCALE);
                crackAnimation.setActualStage(CrackAnimation.STAGE_HIDING);
            }
            actualHandPos.x = end.x;
            actualHandPos.y = end.y;
        }
        else if (actualStage >= stagesNumber){
            actualHandPos.x = -9999;
            actualHandPos.y = -9999;
            if (crackAnimation.getActualStage() != CrackAnimation.NOTHING) {
                crackAnimation.setActualStage(CrackAnimation.NOTHING);

            }
        }
    }


    public void draw(PGraphics graphics) {
        crackAnimation.draw(graphics);
        if (behaviour != DO_NOT_SHOW) {
            touchingHand.draw(graphics, scalingController.getActualScale());
        }

    }

    public void addGraphic(AbstractGirlAnimation girlAnimation, ImageWithData controlAreaImage) {
        this.girlAnimation = girlAnimation;
        this.controlAreaImage =controlAreaImage;
    }

    public void clearGraphic() {
        if (this.girlAnimation != null){
            girlAnimation = null;
        }
        if (this.controlAreaImage != null){
            controlAreaImage = null;
        }
    }
}
