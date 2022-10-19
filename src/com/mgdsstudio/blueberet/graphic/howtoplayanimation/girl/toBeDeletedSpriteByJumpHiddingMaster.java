package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import processing.core.PConstants;
import processing.core.PGraphics;

class toBeDeletedSpriteByJumpHiddingMaster {

    private int x,y;
    private int zoneWidth;
    private int timeForSingleStageHidding;
    private Timer timer;
    private int actualHeight;
    private int maxHeight;

    private final int STAGE_NOTHING = 0;
    final int STAGE_HIDDING = 1;
    final int STAGE_APPEARING = 2;
    private int stage;
    private int menuActualStage;

    //private boolean firstLaunch;

    toBeDeletedSpriteByJumpHiddingMaster(int x, int y, int zoneWidth, int zoneHeight, int timeToFullHiddingAndHidding) {
        this.x = x-zoneWidth/2;
        this.y = y-zoneHeight/2;
        this.zoneWidth = zoneWidth;
        this.timeForSingleStageHidding = timeToFullHiddingAndHidding/2;
        init(zoneHeight);
    }

    void init(int zoneHeight) {
        //timer = new Timer(timeForSingleStageHidding);
        initPos(zoneHeight);

    }

    private void initPos(int zoneHeight) {
        //float step = zoneHeight/(pos.length+1);
        maxHeight = zoneHeight;
        //float y = this.y-zoneHeight/2+(step/2f);
        /*for (int i = 0; i < pos.length; i++){
            pos[i] = new Coordinate(x, y);
            y+=step;
        }*/
    }

    void recreate(){
        timer.setNewTimer(timeForSingleStageHidding);
        actualHeight = 0;
    }

    void update(int stage){
        updateStatement();
    }

    private void updateStatement() {
        if (stage == STAGE_HIDDING){
            /*if (timer.isTime()){
                timer.setNewTimer(timeForSingleStageHidding);
                stage++;
            }*/
            updateHeight();
        }
        else if (stage == STAGE_APPEARING){
            /*if (timer.isTime()){
                timer.setNewTimer(timeForSingleStageHidding);
                stage++;
            }*/
            updateHeight();
        }
        else {
            //stage = 0;
            //if (timer !=) timer.stop();
        }
    }


    private void updateHeight(){
        if (stage == STAGE_HIDDING) {
            float deltaTime = (float) (timeForSingleStageHidding - timer.getRestTime()) / 1000f;
            actualHeight = (int) ((float) maxHeight * deltaTime);
        }
        else if (stage == STAGE_APPEARING) {
            float deltaTime = (float) (timer.getRestTime()-timeForSingleStageHidding) / 1000f;
            actualHeight = (int) ((float) maxHeight * deltaTime);
        }
        else actualHeight = 0;
        //System.out.println("Time: " + deltaTime);
    }

    void draw(PGraphics graphics){
        if (stage == STAGE_APPEARING || stage == STAGE_HIDDING) {
            graphics.pushStyle();
            graphics.rectMode(PConstants.CORNER);
            graphics.noStroke();
            graphics.fill(0, 0, 0);
            graphics.rect(x, y, zoneWidth, actualHeight);
            //System.out.println("Rect drawn with height: " + actualHeight + " and width " + zoneWidth + " at pos: " + pos[i].x + "x " + pos[i].y);
            graphics.popStyle();
        }
    }

    void setStage(int stage) {
        this.stage = stage;
    }

    void startToHide(){
        stage = STAGE_HIDDING;
        if (timer == null) timer = new Timer(timeForSingleStageHidding);
        else timer.setNewTimer(timeForSingleStageHidding);
    }

    void startToAppear(){
        stage = STAGE_APPEARING;
        if (timer == null) timer = new Timer(timeForSingleStageHidding);
        else timer.setNewTimer(timeForSingleStageHidding);
    }

    public void stop() {
        stage = STAGE_NOTHING;
    }
}
