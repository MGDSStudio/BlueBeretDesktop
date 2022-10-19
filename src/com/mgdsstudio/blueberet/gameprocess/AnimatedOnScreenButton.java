package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import processing.core.PGraphics;

public class AnimatedOnScreenButton extends OnScreenButton{
    private ImageZoneSimpleData secondImageZoneData;
    private Timer timerToNextPic;
    private final static int TIME_TO_NEXT_PICTURE = 400;

    private boolean actualPicture;

    private boolean backgroundCleared;

    AnimatedOnScreenButton(Tileset tileset, ImageZoneSimpleData imageZoneSimpleData, int x, int y, boolean circleOrRect, byte buttonFunction, boolean neverDrawBackground, ImageZoneSimpleData secondImageZoneData) {
        super(tileset, imageZoneSimpleData, x, y, circleOrRect, buttonFunction, true, false);
        this.secondImageZoneData = secondImageZoneData;
        timerToNextPic = new Timer(TIME_TO_NEXT_PICTURE);
    }

    public void draw(PGraphics graphics){
        if (timerToNextPic.isTime()){
            timerToNextPic.setNewTimer(TIME_TO_NEXT_PICTURE);
            if (actualPicture){
                actualPicture = false;
            }
            else actualPicture = true;
            drawBackground = true;
        }
        if (actualVisibilityStatement == VISIBLE){
            actualVisibilityStatement = !VISIBLE;
            drawWithAngleSettingWithAdoptedRedrawing(graphics); //Draw back
            actualVisibilityStatement = VISIBLE;
            backgroundCleared = false;
            drawWithAngleSettingWithAdoptedRedrawing(graphics); // Draw pic
        }
        else {
            if (!backgroundCleared){
                drawWithAngleSettingWithAdoptedRedrawing(graphics); //Draw back
                backgroundCleared = true;
            }
        }

        //drawWithRegularRedrawing(graphics);
        //else if (redrawnOnEveryFrame) redrawnOnEveryFrame = false;
        //super.draw(graphics);

        //System.out.println("Actual picture: " + actualPicture + " used " + getActualImageData());
    }

    @Override
    protected ImageZoneSimpleData getActualImageData(){
        if (actualPicture) return secondImageZoneData;
        else return imageZoneSimpleData;
    }
}
