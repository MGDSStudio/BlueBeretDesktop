package com.mgdsstudio.blueberet.graphic.screeneffects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;
import processing.core.PImage;

// Don't use this class!
public class ScreenHiddingController {
    protected int timeToNextAlphaChangingStage;
    protected int actualAlpha = 0;
    protected Timer timerToNextColorChangingStep;
    protected int stagesNumber;
    protected int actualAlphaChangingStageNumber = 0;
    protected int alphaChangingStep;

    protected final static int NOTHING = 0;
    protected final static int TO_HIDING = 1;
    protected final static int HIDDEN = 2;
    protected final static int APPEARING = 3;
    protected final static int ENDED = 4;
    protected int statement = NOTHING;
    protected PImage backgroundGraphic, screenGraphic;
    protected float lastCameraScale;
    public static boolean WITH_APPEARING = true;
    public static boolean WITH_HIDDING = false;

    public ScreenHiddingController(int timeToHide, int stagesNumber, GameCamera gameCamera, boolean mode) {
        this.stagesNumber = stagesNumber;
        timeToNextAlphaChangingStage = timeToHide/stagesNumber;
        alphaChangingStep = 255/stagesNumber;
        lastCameraScale = gameCamera.getScale();
        System.out.println("Scale was " + lastCameraScale);
        initStartStatements(mode);
    }

    private void initStartStatements(boolean mode){
        if (mode == WITH_HIDDING) {
            statement = TO_HIDING;
            actualAlpha = 0;
        }
        if (mode == WITH_APPEARING) {
            statement = APPEARING;
            actualAlpha = 255;
        }
    }

    public ScreenHiddingController(int timeToAppear, int stagesNumber, boolean mode) {
        this.stagesNumber = stagesNumber;
        timeToNextAlphaChangingStage = timeToAppear/stagesNumber;
        alphaChangingStep = 255/stagesNumber;
        initStartStatements(mode);
        //lastCameraScale = gameCamera.getScale();
        //System.out.println("Scale was " + lastCameraScale);
    }

    public boolean isEnded(){
        if (statement == ENDED) return true;
        else return false;
    }

    public void startToHide(){
        statement = TO_HIDING;
    }

    public void startToAppear(){
        actualAlpha = 255;
        statement = APPEARING;
    }

    public boolean isHidden(){
        System.out.println("Statement: " + statement);
        if (statement == HIDDEN) return true;
        else return false;
    }

    public void update(){
        if (statement == TO_HIDING){
            if (screenGraphic == null || backgroundGraphic == null){
                try {
                    screenGraphic = (PImage) Program.objectsFrame.clone();
                    backgroundGraphic = (PImage) Program.objectsFrame.clone();
                    lastCameraScale = 1f;
                    screenGraphic.resize((int)(Program.engine.width*lastCameraScale), (int)(Program.engine.height*lastCameraScale));
                    backgroundGraphic.resize((int)(Program.engine.width*lastCameraScale), (int)(Program.engine.height*lastCameraScale));
                    //System.out.println("Dimensions: " + backgroundGraphic.width + "x" + backgroundGraphic.height );
                    /*
                    screenGraphic.resize(Programm.objectsFrame.width, Programm.objectsFrame.height);
                    backgroundGraphic.resize(Programm.backgroundsFrame.width, Programm.backgroundsFrame.height);

                     */
                    /*
                    screenGraphic.resize(Programm.engine.width, Programm.engine.height);
                    backgroundGraphic.resize(Programm.engine.width, Programm.engine.height);
                    */
                    Program.engine.background(0);
                }
                catch (CloneNotSupportedException e){
                    System.out.println("Can not clone graphic " + e);
                }
            }
            toHidding();
        }
        else if (statement == APPEARING){
            toAppearing();
        }

    }

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
    }

    private void toHidding(){
        if (timerToNextColorChangingStep == null) {
            timerToNextColorChangingStep = new Timer(timeToNextAlphaChangingStage);
            actualAlpha+=alphaChangingStep;
        }
        else{
            if (timerToNextColorChangingStep.isTime()){
                timerToNextColorChangingStep.setNewTimer(timeToNextAlphaChangingStage);
                actualAlpha+=alphaChangingStep;
                alphaChangingStep++;
                if (actualAlpha>=255){
                    actualAlpha = 255;
                    statement = HIDDEN;
                }
            }
        }
    }

    public void draw(PGraphics graphics){
        if (statement != NOTHING){
            if (statement == TO_HIDING) {
                drawHidding(graphics);
            }
            else if (statement == APPEARING) drawAppearing();
        }
    }

    private void drawAppearing() {
        Program.engine.pushStyle();
        Program.engine.noStroke();
        Program.engine.fill(0, actualAlpha);
        Program.engine.rect(Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height);
        Program.engine.popStyle();
        System.out.println("Draw with appearing");
    }

    private void drawHidding(PGraphics graphics){

            if (graphics!=null) {
                graphics.pushStyle();
                graphics.noStroke();
                graphics.fill(0, actualAlpha);
                graphics.rect(graphics.width / 2, graphics.height / 2, graphics.width, graphics.height);
                graphics.popStyle();
            }
            else {
                Program.engine.pushStyle();
                Program.engine.noStroke();
                Program.engine.fill(0, actualAlpha);
                float coef = 1f/lastCameraScale;
                Program.engine.clip((int)((Program.engine.width/2)), (int)((Program.engine.height/2)), (int)(Program.engine.width), (int)(Program.engine.height));
                Program.engine.image(backgroundGraphic, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width*coef, Program.engine.height*coef);
                Program.engine.image(screenGraphic, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width*coef, Program.engine.height*coef);
                Program.engine.rect(Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height);
                Program.engine.popStyle();
            }
            System.out.println("Draw with hidding");
    }

    public boolean isAppearing() {
        if (statement == APPEARING){
            return true;
        }
        else return false;
    }
}
