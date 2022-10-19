package com.mgdsstudio.blueberet.graphic.simplegraphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import processing.core.PApplet;
import processing.core.PImage;

public class Animation extends Image{
    public static int NORMAL_ALONG_X = 3;
    public static int NORMAL_ALONG_Y = 1;
    public static int NORMAL_FREQUENCY = SpriteAnimation.NORMAL_ANIMATION_UPDATING_FREQUENCY/2;


    private int leftX, upperY, rightX, lowerY;
    private int alongX = NORMAL_ALONG_X, alongY = NORMAL_ALONG_Y;
    private int frequency = NORMAL_FREQUENCY;
    private int startSpriteNumber = 0, lastSpriteNumber = (NORMAL_ALONG_X*NORMAL_ALONG_Y-1);

    //By time changeable
    private int actualLeftX, actualUpperY,actualRightX, actualLowerY;
    private Timer nextSpriteTimer;
    private int timeToNextSprite;
    private int actualSpriteNumber;

    public Animation(String path) {
        super(path);
        init();
    }

    public Animation(String path, int leftX, int upperY, int rightX, int lowerY, int alongX, int alongY) {
        super(path);
        this.leftX = leftX;
        this.upperY = upperY;
        this.rightX = rightX;
        this.lowerY = lowerY;
        this.alongX = alongX;
        this.alongY = alongY;
        init();
    }

    public Animation(String path, PImage anotherPImage, int leftX, int upperY, int rightX, int lowerY, int alongX, int alongY) {
        super(path, anotherPImage);
        this.leftX = leftX;
        this.upperY = upperY;
        this.rightX = rightX;
        this.lowerY = lowerY;
        this.alongX = alongX;
        this.alongY = alongY;
        init();
    }

    public Animation(Image anotherImage, int leftX, int upperY, int rightX, int lowerY, int alongX, int alongY) {
        super(anotherImage);
        this.leftX = leftX;
        this.upperY = upperY;
        this.rightX = rightX;
        this.lowerY = lowerY;
        this.alongX = alongX;
        this.alongY = alongY;
        init();
    }

    public Animation(Animation anotherAnimation) {
        super(anotherAnimation);
        init();
    }

    private void init(){
        startSpriteNumber = 0;
        lastSpriteNumber =alongX*alongY-1;
        updateTimerData();
    }


    public void update(){
        updateActualSpriteNumber();
        updateActualZoneData();
        //System.out.println("Time " + timeToNextSprite + " Freq: " + frequency);
    }

    private void updateActualZoneData() {
        int singleWidth = (rightX-leftX)/alongX;
        int singleHeight = (lowerY-upperY)/alongY;
        int actualRow = PApplet.floor(actualSpriteNumber/alongX);
        int actualColumn = PApplet.floor(actualSpriteNumber%alongX);
        int newLeftX = actualColumn*(rightX-leftX);
        actualLeftX = leftX+actualColumn*(singleWidth);
        actualUpperY = upperY+actualRow*(singleHeight);
        actualRightX = actualLeftX+singleWidth;
        actualLowerY = actualUpperY+singleHeight;
        //System.out.println("Width: " + singleWidth + "; Height: " + singleHeight + "; Row: " + actualColumn );
        //System.out.println("New source data: " + (int)leftX + "x" + (int)upperY + "; " + (int)rightX + "x" + (int)lowerY);
        //System.out.println("New local data: " + (int)actualLeftX + "x" + (int)actualUpperY + "; " + (int)actualRightX + "x" + (int)actualLowerY);
        /*if (newLeftX != leftX){

        }*/
    }

    private void updateActualSpriteNumber(){
        if (nextSpriteTimer == null){
            nextSpriteTimer = new Timer(timeToNextSprite);
        }
        else {
            if (nextSpriteTimer.isTime()){
                actualSpriteNumber++;
                if (actualSpriteNumber > lastSpriteNumber) {
                    actualSpriteNumber = startSpriteNumber;
                }
                nextSpriteTimer.setNewTimer(timeToNextSprite);
            }
        }
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getUpperY() {
        return upperY;
    }

    public void setUpperY(int upperY) {
        this.upperY = upperY;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getLowerY() {
        return lowerY;
    }

    public void setLowerY(int lowerY) {
        this.lowerY = lowerY;
    }

    public int getAlongX() {
        return alongX;
    }

    public void setAlongX(int alongX) {
        this.alongX = alongX;
    }

    public int getAlongY() {
        return alongY;
    }

    public void setAlongY(int alongY) {
        this.alongY = alongY;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
        updateTimerData();
    }

    private void updateTimerData(){
        timeToNextSprite = (int) (1000.0f/(float)frequency);
        System.out.println("Time to next sprite set on " + timeToNextSprite);
    }

    public int getLastSpriteNumber() {
        return lastSpriteNumber;
    }

    public void setLastSpriteNumber(int lastSpriteNumber) {
        this.lastSpriteNumber = lastSpriteNumber;
    }

    public int getActualLeftX() {
        return actualLeftX;
    }

    public int getActualUpperY() {
        return actualUpperY;
    }

    public int getActualRightX() {
        return actualRightX;
    }

    public int getActualLowerY() {
        return actualLowerY;
    }
}
