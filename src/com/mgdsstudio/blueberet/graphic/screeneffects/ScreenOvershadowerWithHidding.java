package com.mgdsstudio.blueberet.graphic.screeneffects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class ScreenOvershadowerWithHidding extends ScreenOvershadower{


    public ScreenOvershadowerWithHidding(int timeToHide, int stagesNumber) {
        this.stagesNumber = stagesNumber;
        timeToNextAlphaChangingStage = timeToHide/(stagesNumber);
        initStartStatements(WITH_HIDDING);
        //this.stagesNumber++;
    }

    public boolean getActualTintChangingDirection() {
        return actualTintChangingDirection;
    }

    public void restart(){
        initStartStatements(WITH_APPEARING);
    }


/*
    private void toAppearing() {
        if (timerToNextColorChangingStep == null) {
            timerToNextColorChangingStep = new Timer(timeToNextAlphaChangingStage);
            actualAlpha-=alphaChangingStep;
        }
        else{
            if (timerToNextColorChangingStep.isTime()){
                timerToNextColorChangingStep.setNewTimer(timeToNextAlphaChangingStage);
                actualAlpha-=alphaChangingStep;
                alphaChangingStep++;
                if (actualAlpha<=0){
                    actualAlpha = 0;
                    statement = ENDED;
                }
            }
        }
    }*/

    protected void tintUpdating(){
        if (timerToNextColorChangingStep == null) {
            timerToNextColorChangingStep = new Timer(timeToNextAlphaChangingStage);
            actualAlpha+=alphaChangingStep;
            draw();
        }
        else{
            /*
            if (timerToNextColorChangingStep.isTime()){
                timerToNextColorChangingStep.setNewTimer(timeToNextAlphaChangingStage);
                actualAlpha+=alphaChangingStep;
                alphaChangingStep++;
                if (actualAlpha>=255){
                    actualAlpha = 255;
                    statement = ENDED;
                }
            }

             */
        }
    }



    /*
    public void draw(){
        if (statement == IN_PROCESS) drawBlackRect();
    }*/

    protected void drawBlackRect() {
        if (actualAlpha >= 255){
            if (statement != ENDED){
                if (lastScreenTimer == null ) {
                    lastScreenTimer = new Timer(timeToNextAlphaChangingStage);
                    renderBlack();
                }
                else {
                    if (lastScreenTimer.isTime()){
                        renderBlack();
                        statement = ENDED;
                    }
                }
            }
        }
        else if (timerToNextColorChangingStep.isTime()){
            timerToNextColorChangingStep.setNewTimer(timeToNextAlphaChangingStage);
            actualAlpha+=alphaChangingStep;
            alphaChangingStep++;
            if (actualAlpha>=255){
                if (actualAlpha > 255) renderBlack();
                actualAlpha = 255;
            }
            if (actualAlpha < 255){
                renderBlack();
            }
        }

    }

    private void renderBlack(){
        Program.engine.pushStyle();
        Program.engine.noStroke();
        Program.engine.fill(redValue,0,0, alphaChangingStep);
        Program.engine.rect(Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height);
        Program.engine.popStyle();
    }
}
