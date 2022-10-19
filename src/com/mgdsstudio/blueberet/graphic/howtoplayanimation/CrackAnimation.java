package com.mgdsstudio.blueberet.graphic.howtoplayanimation;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.ImageZoneVariableData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class CrackAnimation {
    private final static ImageZoneSimpleData basicGraphicData = GameMenusController.crack;
    private ImageZoneVariableData graphicData;
    private Image image;
    private final int maxWidth, maxHeight;
    private final int startWidth, startHeight;
    private int actualWidth, actualHeight;
    private int x,y;
    private int actualAlpha = 255;
    private float angle;
    private int timeToFullAppearing;
    private Timer timer;
    private int delayBeforeStart;

    final static int NOTHING = 0;
    final static int STAGE_APPEARING = 1;
    final static int STAGE_SHOW = 2;
    final static int STAGE_HIDING = 3;
    private int actualStage = NOTHING;
    //private int prevStage = actualStage;
    //private boolean withAutomaticUpdating;

    public CrackAnimation(PApplet engine, Image image, int maxWidth, int maxHeight, int x, int y, int timeToFullAppearing) {
        //this.graphicData = graphicData;
        this.image = image;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        startWidth = maxWidth /6;
        startHeight = maxHeight /6;
        actualWidth = startWidth;
        actualHeight = startHeight;
        this.x = x;
        this.y = y;
        this.timeToFullAppearing = timeToFullAppearing;
        delayBeforeStart = (int) ((float)timeToFullAppearing*0.8f);
        init(engine);
    }

    private void init(PApplet engine){
        graphicData = new ImageZoneVariableData(basicGraphicData);
        angle = engine.random(-PConstants.PI/10f, PConstants.PI/10f);
    }

    void update(){
        if (actualStage == STAGE_APPEARING) {
            updateAppearing();
        } else if (actualStage == STAGE_HIDING) {
            updateHiding();
        }
        //prevStage=actualStage;
    }

    /*
    private void updateOld(){
        if (actualStage != NOTHING){
            if (timer.isTime()){
                timer.setNewTimer(timeToFullAppearing);
                if (withAutomaticUpdating) toNextStage();
            }
        }

        prevStage=actualStage;
    }*/


    public int getActualStage() {
        return actualStage;
    }

    void toNextStage(){
        if (actualStage == NOTHING){
            setStartParameters();
            if (timer == null) timer = new Timer(timeToFullAppearing);
            else timer.setNewTimer(timeToFullAppearing);
        }
        else if (actualStage == STAGE_APPEARING){
            setFullAppeared();
        }
        else if (actualStage == STAGE_HIDING){
            setFullHidden();
        }
        actualStage++;
        if (actualStage > STAGE_HIDING){
            actualStage = NOTHING;
        }

    }

    private void setStartParameters() {
    }

    private void updateHiding() {
        updateActualDimensions(STAGE_HIDING);
    }

    private void updateAppearing() {
        float relativeTime = 1f-((float)(timer.getRestTime()))/(float)timeToFullAppearing;
        if (delayBeforeStart != 0){
            //System.out.println("Delay is " + delayBeforeStart + " rest time: " + timer.restTime());
            if (timer.getRestTime()>delayBeforeStart){
                relativeTime = 0f;
                //System.out.println("Alpha is null");
            }
            else {
                relativeTime = 1f-((float)(timer.getRestTime()))/((float)timeToFullAppearing-(float)delayBeforeStart);
            }
        }
        actualAlpha = (int)(relativeTime*255f);
        updateActualDimensions(STAGE_APPEARING);
    }

    private void updateAppearingWithoutDelay() {
        float relativeTime = 1f-((float)(timer.getRestTime()))/(float)timeToFullAppearing;
        if (delayBeforeStart != 0){
            //System.out.println("Delay is " + delayBeforeStart + " rest time: " + timer.restTime());
            if (timer.getRestTime()>delayBeforeStart){
                relativeTime = 0f;
                System.out.println("Alpha is null");
            }
        }
        actualAlpha = (int)(relativeTime*255f);
        updateActualDimensions(STAGE_APPEARING);
    }

    private void updateActualDimensions(int stage) {
        if (stage == STAGE_APPEARING){
            float relativeValue = (float)actualAlpha/255f;
            int maxWidthChangingValue = maxWidth -startWidth;
            int maxHeightChangingValue = maxHeight -startHeight;
            int newWidth = (int)(startWidth+(float)maxWidthChangingValue*relativeValue);
            int newHeight = (int)(startHeight+(float)maxHeightChangingValue*relativeValue);
            int centerX = (graphicData.leftX+graphicData.rightX)/2;
            int centerY = (graphicData.upperY+graphicData.lowerY)/2;
            graphicData.setActualLeftX(centerX-newWidth/2);
            graphicData.setActualUpperY(centerY-newHeight/2);
            graphicData.setActualRightX(centerX+newWidth/2);
            graphicData.setActualLowerY(centerY+newHeight/2);
            actualWidth = newWidth;
            actualHeight = newHeight;
        }
        else if (stage == STAGE_HIDING){
            float relativeTime = ((float)(timer.getRestTime()))/(float)timeToFullAppearing;
            int newWidth = (int)((float)maxWidth*relativeTime);
            int newHeight = (int)((float)maxHeight*relativeTime);
            int centerX = (graphicData.leftX+graphicData.rightX)/2;
            int centerY = (graphicData.upperY+graphicData.lowerY)/2;
            graphicData.setActualLeftX(centerX-newWidth/2);
            graphicData.setActualUpperY(centerY-newHeight/2);
            graphicData.setActualRightX(centerX+newWidth/2);
            graphicData.setActualLowerY(centerY+newHeight/2);
            actualWidth = (int)((float)maxWidth*relativeTime);
            actualHeight = (int)((float)maxHeight*relativeTime);
        }
    }

    private void setFullAppeared() {
        actualAlpha = 255;
    }

    private void setFullHidden() {
        actualAlpha = 0;
    }

    void draw(PGraphics graphics){
        if (actualStage != NOTHING) {
            graphics.pushMatrix();
            graphics.pushStyle();
            graphics.translate(x, y);
            graphics.rotate(angle);
            graphics.tint(255, actualAlpha);
            //System.out.println("Image was drawn");
            graphics.image(image.getImage(), 0, 0, actualWidth, actualHeight, graphicData.getActualLeftX(), graphicData.getActualUpperY(), graphicData.getActualRightX(), graphicData.getActualLowerY());
            graphics.popStyle();
            graphics.popMatrix();
        }
    }

    public void setActualStage(int actualStage) {
        this.actualStage = actualStage;
        if (actualStage != STAGE_SHOW) {
            if (timer == null) timer = new Timer(timeToFullAppearing);
            else timer.setNewTimer(timeToFullAppearing);
        }
        else {
            //System.out.println("Timer was restarted for statement: " + actualStage);
        }
    }

    public void restart() {
        setActualStage(STAGE_APPEARING);
        //angle = Program.engine.random(-PConstants.PI/10f, PConstants.PI/10f);
        actualAlpha = 255;
    }
}
