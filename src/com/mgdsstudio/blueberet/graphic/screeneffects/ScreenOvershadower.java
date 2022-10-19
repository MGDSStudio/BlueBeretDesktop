package com.mgdsstudio.blueberet.graphic.screeneffects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;

public abstract class ScreenOvershadower {
    //protected final int timeToHideScreen;
    protected int timeToNextAlphaChangingStage;
    protected int actualAlpha = 0;
    protected Timer timerToNextColorChangingStep;
    protected int stagesNumber;
    protected int actualAlphaChangingStageNumber = 0;
    protected int alphaChangingStep;
    protected int redValue;
    protected final int maxRedValue = 45;
    protected final static boolean IN_PROCESS = true;
    protected final static boolean ENDED = false;
    protected boolean statement = IN_PROCESS;
    public static boolean WITH_APPEARING = true;
    public static boolean WITH_HIDDING = false;
    protected boolean actualTintChangingDirection;
    protected Timer lastScreenTimer;

    protected void initStartStatements(boolean mode){
        final int additionalStage = -2;
        statement = IN_PROCESS;
        if (mode == WITH_HIDDING) {
            actualAlpha = 0;
            alphaChangingStep = 255/(additionalStage+stagesNumber);
        }
        if (mode == WITH_APPEARING) {
            actualAlpha = 255;
            alphaChangingStep = 255/(additionalStage+stagesNumber);
        }
        actualTintChangingDirection = mode;
        timerToNextColorChangingStep = null;
    }

    public void update(){
        if (statement == IN_PROCESS){
            tintUpdating();
        }
    }

    public final void draw(){
        if (statement == IN_PROCESS) drawBlackRect();
    }

    protected abstract void drawBlackRect();

    protected abstract void tintUpdating();

    public boolean isEnded(){
        if (statement == ENDED) return true;
        else return false;
    }

    public abstract void restart();

    public void setRedIncensity(float v) {
        if (v<=0) v = 0.1f;
        redValue = (int) (maxRedValue-(maxRedValue*(v)));
    }
}
