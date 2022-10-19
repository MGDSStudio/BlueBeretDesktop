package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import processing.core.PApplet;

public class BlackBoards {

    private final PApplet engine;
    private int stagesNumber = 8;
    private int actualStage = 0;
    private int timeToNextStage;
    private Timer timer;
    private final int timeToFullAppear;
    private int actualScreenWidth, actualScreenHeight;
    private int widthStep, heightStep;
    private boolean fullInvisible, fullVisible;
    private final static boolean HIDE_SCREEN = true;
    private final static boolean SHOW_SCREEN = false;
    private boolean actualDirection;

    /*
    private final static int HIDE_SCREEN = -1;
    private final static int SHOW_SCREEN = 1;

    private final static int FULL_SHOWN = 2;

    private final static int FULL_HIDDEN = -2;

    private int actualDirection;
     */

    public BlackBoards(PApplet engine, int timeToFullAppear, int stagesNumber) {
        this.engine = engine;
        this.timeToFullAppear = timeToFullAppear;
        this.stagesNumber = stagesNumber;
        init();
    }

    private void init() {
        timeToNextStage = timeToFullAppear/(stagesNumber-1);
        widthStep = (engine.ceil(engine.width/(stagesNumber-1)));
        heightStep = (engine.ceil(engine.height/(stagesNumber-1)));
    }

    private void update(){
        if (actualStage <= stagesNumber){
            if (timer != null){
                if (timer.isTime()){
                    actualStage++;
                    timer.setNewTimer(timeToNextStage);
                    updateDimsForStage();
                    System.out.println("Stage: " + actualStage + " width: " + actualScreenWidth );
                }
            }
        }
    }

    private void updateDimsForStage() {
        if (actualDirection == HIDE_SCREEN){
            int reverseStage = stagesNumber-actualStage;
            actualScreenWidth = reverseStage*widthStep;
            actualScreenHeight = reverseStage*heightStep;
        }
        else {
            actualScreenWidth = actualStage*widthStep;
            actualScreenHeight = actualStage*heightStep;
        }
    }

    public void stop(){
        if (actualDirection  == SHOW_SCREEN){
            actualDirection = HIDE_SCREEN;
            startTimerIfNeed();
        }
        else System.out.println("Already starts to hide");
    }

    private final void startTimerIfNeed(){
        if (timer == null){
            timer = new Timer(timeToNextStage);
        }
        else timer.setNewTimer(timeToNextStage);
        actualStage = 0;
    }

    public void start(){
        if (actualDirection  == HIDE_SCREEN){
            actualDirection = SHOW_SCREEN;
            startTimerIfNeed();
        }
        else System.out.println("Already starts to show");
    }

    public void draw(){
        update();
        if (actualStage < stagesNumber && actualStage > 0) {
            engine.pushMatrix();
            engine.pushStyle();
            engine.noStroke();
            engine.fill(0);
            engine.translate(engine.width / 2, engine.height / 2);
            engine.beginShape();
// Exterior part of shape, clockwise winding
            engine.vertex(-1, -1);
            engine.vertex(engine.width + 1, -1);
            engine.vertex(engine.width + 1, engine.height + 1);
            engine.vertex(-1, engine.height + 1);
// Interior part of shape, counter-clockwise winding
            engine.beginContour();
            engine.vertex(-actualScreenWidth / 2, -actualScreenHeight / 2);
            engine.vertex(-actualScreenWidth / 2, actualScreenHeight / 2);
            engine.vertex(actualScreenWidth / 2, actualScreenHeight / 2);
            engine.vertex(actualScreenWidth / 2, -actualScreenHeight / 2);
            engine.endContour();
            engine.endShape(PApplet.CLOSE);
            engine.popMatrix();
            engine.popStyle();
        }
        else{
            if (actualDirection == SHOW_SCREEN){

            }
            else {
                engine.background(0);
            }
        }
    }
}
