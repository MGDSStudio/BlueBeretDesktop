package com.mgdsstudio.blueberet.graphic.screeneffects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.sql.Time;

public class ScreenOvershadowerWithAppearing extends ScreenOvershadower{


    public ScreenOvershadowerWithAppearing(int timeToHide, int stagesNumber) {
        this.stagesNumber = stagesNumber;
        timeToNextAlphaChangingStage = timeToHide/stagesNumber;
        initStartStatements(WITH_APPEARING);
        if (Program.debug) System.out.println("Start alpha: " + actualAlpha + " step " + alphaChangingStep);
    }

    public void restart(){
        initStartStatements(WITH_APPEARING);
    }

    protected void tintUpdating(){
        //System.out.println("Tint updating");
        if (timerToNextColorChangingStep == null) {
            timerToNextColorChangingStep = new Timer(timeToNextAlphaChangingStage);
            //actualAlpha-=alphaChangingStep;
        }
        else{
            if (timerToNextColorChangingStep.isTime()){
                timerToNextColorChangingStep.setNewTimer(timeToNextAlphaChangingStage);
                actualAlpha-=alphaChangingStep;
                alphaChangingStep++;
                if (actualAlpha<=0){
                    actualAlpha = 0;
                }
            }
            if (actualAlpha <= 0) {
                if (lastScreenTimer == null) lastScreenTimer = new Timer(timeToNextAlphaChangingStage);
                else if (lastScreenTimer.isTime()) {
                        statement = ENDED;
                    }
                }
            }

    }

    protected void tintUpdatingOld(){
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
                }
            }
            if (actualAlpha <= 0) {
                if (lastScreenTimer == null) lastScreenTimer = new Timer(timeToNextAlphaChangingStage);
                else if (lastScreenTimer.isTime()) {
                    statement = ENDED;
                }
            }
        }

    }



    protected void drawBlackRect() {
        //System.out.println("Drawn black rect with alpha " + actualAlpha);
        //System.out.println("Draw appearing");
        Program.engine.pushStyle();
        Program.engine.noStroke();
        Program.engine.fill(redValue,0,0, actualAlpha);
        Program.engine.rect(Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height);
        Program.engine.popStyle();
    }



}
