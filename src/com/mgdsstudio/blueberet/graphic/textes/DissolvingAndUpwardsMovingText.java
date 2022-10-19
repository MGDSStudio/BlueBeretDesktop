package com.mgdsstudio.blueberet.graphic.textes;


import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.AbstractText;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DissolvingAndUpwardsMovingText extends AbstractText {
    public final static float NORMAL_Y_VELOCITY = -0.05f;
    public final static int NORMAL_ALPHA_CHANGING_STEP = 40;
    public final static int NORMAL_DISSOLVING_TIME = 2500;
    public final static int NORMAL_TEXT_HEIGHT = 20;
    public final static int NORMAL_STAGES_NUMBER = 5;
    public final static int ONLY_TEXT = 0;
    public final static int MONEY = 1;
    public final static int MEDICAL_KIT = 2;
    public final static int AMMO = 10;
    // another values more than 10 are weapons


    //private static boolean fontUploaded;


    private int timeToNextAlphaChanging;
    private int alphaChangingStep;
    private int actualAlpha = 255;
    private boolean fullDissolve;
    private float yVelocity;
    private int valueType;


    public DissolvingAndUpwardsMovingText(float actualX, float actualY, float yVelocity, String text, int timeToShow, int stagesNumber, int  valueType) {
        initBasicValues(actualX, actualY, yVelocity, text, valueType);
        init(timeToShow, stagesNumber);
        if (!fontUploaded){
            createFont();
        }
    }

    public DissolvingAndUpwardsMovingText(float actualX, float actualY, String text) {
        valueType = 0;
        initBasicValues(actualX, actualY, yVelocity, text, valueType);
        init(NORMAL_DISSOLVING_TIME, NORMAL_STAGES_NUMBER);
        if (!fontUploaded){
            createFont();
        }
    }

    public DissolvingAndUpwardsMovingText(float actualX, float actualY, float yVelocity, String text, int timeToShow, int stagesNumber, int  valueType, int color) {
        initBasicValues(actualX, actualY, yVelocity, text, valueType);
        init(timeToShow, stagesNumber);
        if (!fontUploaded){
            createFont();
        }
        this.color = color;
    }

    private void initBasicValues(float actualX, float actualY, float yVelocity, String text, int valueType){
        fontHeight = NORMAL_TEXT_HEIGHT;
        this.actualX = actualX;
        this.actualY = actualY;
        this.yVelocity = yVelocity;
        this.text = text;
        this.valueType = valueType;
    }

    private void init(int timeToShow, int stagesNumber) {
        timeToNextAlphaChanging = timeToShow/stagesNumber;
        timer = new Timer(timeToNextAlphaChanging);
        alphaChangingStep = 255/stagesNumber;
    }



    public void update(){
        update(Program.deltaTime);
    }

    public void update(int deltaTime){
        if (!fullDissolve) {
            updateActualTint();
            updateActualPos(deltaTime);
        }
    }

    private void updateActualPos(float deltaTime) {
        actualY += (yVelocity * deltaTime);
        //System.out.println("New pos: " + actualY + " by delta: " +deltaTime + " and speed " + yVelocity);
    }

    private void updateActualTint() {
        if (actualAlpha > 0){
            if (timer.isTime()){
                timer.setNewTimer(timeToNextAlphaChanging);
                actualAlpha-=alphaChangingStep;
            }
            if (actualAlpha<=0) fullDissolve = true;
        }
        else if (!fullDissolve) fullDissolve = true;
    }

    public boolean canBeDeleted(){
        return fullDissolve;
    }

    public void draw(GameCamera gameCamera){
        if (!fullDissolve){
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.pushStyle();
            if (font == null) createFont();
            Program.objectsFrame.textFont(font, fontHeight);
            Program.objectsFrame.fill(color, actualAlpha);
            Program.objectsFrame.textAlign(PApplet.CENTER, PApplet.BOTTOM);
            Program.objectsFrame.text(text, actualX - gameCamera.getActualXPositionRelativeToCenter(),actualY - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
        }
    }

    public void draw(PGraphics graphics){
        if (!fullDissolve){
            //graphics.pushMatrix();
            //graphics.scale(Program.OBJECT_FRAME_SCALE);
            graphics.pushStyle();
            if (font == null) createFont();
            graphics.textFont(font, fontHeight);
            graphics.fill(Program.engine.color(255), actualAlpha);
            graphics.textAlign(PApplet.CENTER, PApplet.BOTTOM);
            //System.out.println();
            graphics.text(text, actualX,actualY);
            graphics.popStyle();
            //graphics.popMatrix();
        }
    }

    public boolean isMoneyText(){
        if (valueType == MONEY) return true;
        else return false;
    }

    public boolean isMedicalKitText(){
        if (valueType == MONEY) return true;
        else return false;
    }

    public boolean isAmmoText(){
        if (valueType >= AMMO) return true;
        else return false;
    }


}
