package com.mgdsstudio.blueberet.graphic.textes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.AbstractText;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.SingleFlagZone;
import processing.core.PApplet;

public class OnDisplayText extends AbstractText {
    private int red, green, blue;
    private boolean ended;
    private boolean alreadyAppeared;
    private final int BEFORE_APPEARING_TIME = 1200;
    private final int timeToShow;
    public final static String CLASS_NAME = "OnDisplayText";
    private boolean started;
    private SingleFlagZone activatingZone;



    public OnDisplayText(int x, int y, int red, int green, int blue, String text, int timeToShow) {
        this.actualX = x+Program.engine.width/2;
        this.actualY = y+Program.engine.height/2-UpperPanel.HEIGHT;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.text = text;
        this.timeToShow = timeToShow;
        if (!fontUploaded){
            createFont();
        }
        fontHeight=Program.engine.width/25;
    }

    public void update(){
        if (!started){
            startAppearing();
            if (!fontUploaded) createFont();
        }
        else if (started && !ended) {
            //System.out.println("Showing " + timer.restTime());

            if (timer.isTime()){
                ended = true;
            }
        }
    }

    /*
    public void update(){
        if (!started){
            startAppearing();

        }
        if (started && !ended) {
            if (!fontUploaded) createFont();
            if (!alreadyAppeared){
                if (timer.isTime()){
                    timer.setNewTimer(timeToShow);
                    alreadyAppeared = true;
                }
            }
            else{
                if (alreadyAppeared){
                    if (timer.isTime()){
                        ended = true;
                    }
                }
            }
        }
    }
     */


    private void startAppearing() {
        //started = true;
        if (timer == null) timer = new Timer(BEFORE_APPEARING_TIME);
        if (timer.isTime()){
            started = true;
            timer.setNewTimer(timeToShow);
        }
        //else System.out.println("Waiting " + timer.restTime() + " time to show " + timeToShow);
    }

    @Override
    public void draw(GameCamera gameCamera) {
        System.out.println("Not overridden");
    }



    public boolean canBeDeleted(){
        return ended;
    }

    public void draw(){
        if (!ended && started){
            Program.engine.pushStyle();
            Program.engine.textFont(font, fontHeight);
            Program.engine.fill(red, green, blue);
            Program.engine.textAlign(PApplet.CENTER, PApplet.BOTTOM);
            //System.out.println("Text " + text + " is drawn");
            Program.engine.text(text, actualX, actualY);
            Program.engine.popStyle();
        }
    }
}
