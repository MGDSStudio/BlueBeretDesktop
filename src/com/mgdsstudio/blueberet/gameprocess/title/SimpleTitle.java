package com.mgdsstudio.blueberet.gameprocess.title;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class SimpleTitle {

    private Image title;
    protected Timer showingTimer;
    protected int timeToShow;
    protected int timeToChangeAlpha;
    private final byte WITH_HIDDING = 1;
    public static final byte WITH_APPEARING_AND_HIDDING = 2;
    public static final byte WITH_APPEARING = 3;
    public static final byte ONLY_SHOW = 0;
    protected byte showingMode = WITH_APPEARING;
    protected float actualTintAlphaValue = 1f;
    protected float alphaChangingSpeed = 0.01f;
    //private boolean

    public SimpleTitle(String path, int timeToShow, int timeToChangeAlpha, int width, int height, byte showingMode){
        title = new Image(Program.getAbsolutePathToAssetsFolder(path));
        this.timeToShow = timeToShow;
        this.timeToChangeAlpha = timeToChangeAlpha;
        if (timeToChangeAlpha > timeToShow) this.timeToChangeAlpha = timeToShow;
        showingTimer = new Timer(timeToShow);
        title.getImage().resize(width, height);
        this.showingMode = showingMode;
        if (showingMode == WITH_APPEARING){
            actualTintAlphaValue = 0;
        }
        else if (showingMode == WITH_HIDDING){
            actualTintAlphaValue = 255;
        }
        alphaChangingSpeed = 255.00f/((float)(timeToChangeAlpha));
    }

    public void update(){
        if (!showingTimer.isTime()){
            updateTint();
        }
        else {
            //if (showingTimer != null) showingTimer = null;
        }
    }

    protected void updateTint() {
        if (showingMode == WITH_APPEARING){
            if (!showingTimer.isTime()){
                if (showingTimer.getRestTime()>(timeToShow-timeToChangeAlpha)){
                     actualTintAlphaValue+= (alphaChangingSpeed*(float) Program.deltaTime);
                     if (actualTintAlphaValue > 255) actualTintAlphaValue = 255;
                }
            }
        }
    }

    private void arrayRandomizator(){
        int [] startArray = new int[10];
        for (int i = 0; i < startArray.length; i++){
            startArray[i] = (int) Program.engine.random(10);
        }
        for (int i = 0; i < startArray.length; i++){

        }
    }


    public boolean isEnded(){
        if (showingTimer.isTime()){
            return true;
        }
        else return false;
    }

    public void draw(){
        //Game2D.objectsFrame.beginDraw();
        //Game2D.objectsFrame.tint(255, actualTintAlphaValue);

        Program.engine.tint(254, actualTintAlphaValue);
        Program.engine.image(title.getImage(), Program.engine.width/2f, UpperPanel.HEIGHT/2f, Program.engine.width*0.6f,(Program.engine.width*0.6f)/6f);
        Program.engine.noTint();

        //System.out.println("Drawing the title; Tint: " + actualTintAlphaValue + "; Delta time: " + (Game2D.deltaTime) + "; alphaChangingSpeed: " + alphaChangingSpeed);
        //Game2D.objectsFrame.endDraw();
    }
}
