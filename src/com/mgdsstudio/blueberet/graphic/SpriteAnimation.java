package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class SpriteAnimation extends SingleGraphicElement{
    private Timer nextSpriteTimer;
    public final static byte NORMAL_ANIMATION_UPDATING_FREQUENCY = 33;

    private int singleSpriteWidth, singleSpriteHeight;
    //private boolean fillAreaWithSprite = false; // when false the sprite is drawn only once; when true: to complete fill the area
    private int updateFrequence;   // sprites pro second
    private int timeToNextSprite;
    private byte rowsNumber, columnsNumber;
    private byte actualSpriteNumber = 0;
    private int spritesNumber = 1;
    private int lastSprite;
    private int startSprite = 0;
    private boolean animationAlreadyShown = false;

    private final static byte SHOW_ACTUAL_SPRITE = -1;

    // Animation statements
    public final static byte SWITCHED_OFF = 0;
    public final static byte ACTIVE = 1;
    public final static byte PAUSED = 2;
    private byte animationStatement = ACTIVE;
    private int markedSprite = 0;

    public final static boolean BACKWARD = false;
    public final static boolean FORWARD = true;
    private boolean animationPlayingDirection  = FORWARD;
    private boolean logAdded;

    private boolean playOnce;

    public SpriteAnimation (AnimationDataToStore animationDataToStore){
        init(animationDataToStore.getPath(), animationDataToStore.getLeftUpperCorner()[0], animationDataToStore.getLeftUpperCorner()[1], animationDataToStore.getRightLowerCorner()[0], animationDataToStore.getRightLowerCorner()[1], animationDataToStore.getGraphicWidth(), animationDataToStore.getGraphicHeight());
        this.columnsNumber = animationDataToStore.getCollumnsNumber();
        this.rowsNumber = animationDataToStore.getRowsNumber();
        if (rowsNumber < 1) this.rowsNumber = 1;
        if (columnsNumber < 1) this.columnsNumber = 1;
        spritesNumber = (columnsNumber*rowsNumber);
        this.updateFrequence = animationDataToStore.getFrequency();
        calculateSingeSpriteDimensions(animationDataToStore.getLeftUpperCorner()[0], animationDataToStore.getLeftUpperCorner()[1], animationDataToStore.getRightLowerCorner()[0], animationDataToStore.getRightLowerCorner()[1]);
        calculateTimerData();
        lastSprite = (spritesNumber-1);
        //System.out.println("Animation data: W " + width + "; H: " + height +  "; Single: " + singleSpriteWidth + "x" + singleSpriteHeight + "; Rows: " + rowsNumber + "; Collumns: " + columnsNumber);
        //System.out.println("Path for stars: " + animationDataToStore.getPath() + "; leftUpper = " + getxLeft() + "x" + getyLeft() + "; RightLower: " + getxRight() + "x" + getyRight());
    }

    public SpriteAnimation (String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, byte rowsNumber, byte columnsNumber, int updateFrequency){
        init(path, xLeft, yLeft, xRight, yRight, width, height);
        this.columnsNumber = columnsNumber;
        this.rowsNumber = rowsNumber;
        if (rowsNumber < 1) this.rowsNumber = 1;
        if (columnsNumber < 1) this.columnsNumber = 1;
        spritesNumber = (int) (columnsNumber*rowsNumber);
        this.updateFrequence = updateFrequency;
        calculateSingeSpriteDimensions(xLeft, yLeft, xRight, yRight);
        calculateTimerData();
        lastSprite = (int)(spritesNumber-1);
    }


    public SpriteAnimation (String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, int spritesNumber, int updateFrequency){
        init(path, xLeft, yLeft, xRight, yRight, width, height);
        //new SpriteAnimation("Dragon fire animation.png", (int) 0, (int) 0, (int) 337, (int) 319, (int)80, (int) 35, (int)2, (int)10);
        this.columnsNumber = (byte) spritesNumber;
        if (columnsNumber < 1) this.columnsNumber = 1;
        this.spritesNumber = spritesNumber;
        this.rowsNumber = 1;
        //singleSpriteWidth = (int) (PApplet.abs(xRight - xLeft)/spritesNumber);
        //singleSpriteHeight = (int) (PApplet.abs(yRight - yLeft));
        this.updateFrequence = updateFrequency;
        calculateSingeSpriteDimensions(xLeft, yLeft, xRight, yRight);
        calculateTimerData();
        lastSprite = spritesNumber;
    }

    /*
    public SpriteAnimation (String path, int width, int height, int spritesNumber, int updateFrequency){
        Image imageToGetSpriteSheetDimensions = new Image(path);
        init(path, (int)0, (int)0, (int) imageToGetSpriteSheetDimensions.getImage().width, (int) imageToGetSpriteSheetDimensions.getImage().height, width, height);
        //private void init(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        //new SpriteAnimation("Dragon fire animation.png", (int) 0, (int) 0, (int) 337, (int) 319, (int)80, (int) 35, (int)1, (int)10);
        imageToGetSpriteSheetDimensions = null;
        this.columnsNumber = (byte)spritesNumber;
        this.rowsNumber = 1;
        if (rowsNumber < 1) this.rowsNumber = 1;
        if (columnsNumber < 1) this.columnsNumber = 1;
        spritesNumber = (int) (columnsNumber*rowsNumber);
        this.updateFrequence = updateFrequency;
        calculateSingeSpriteDimensions(xLeft, yLeft, xRight, yRight);
        calculateTimerData();
        lastSprite = spritesNumber;
    }*/

    /*
    public SpriteAnimation (String path, int width, int height, byte rowsNumber, byte columnsNumber, int updateFrequency){
        Image imageToGetSpriteSheetDimensions = new Image(path);
        init(path, (int)0, (int)0, (int) imageToGetSpriteSheetDimensions.getImage().width, (int) imageToGetSpriteSheetDimensions.getImage().height, width, height);
        imageToGetSpriteSheetDimensions = null;
        this.columnsNumber = columnsNumber;
        this.rowsNumber = rowsNumber;
        if (rowsNumber < 1) this.rowsNumber = 1;
        if (columnsNumber < 1) this.columnsNumber = 1;
        spritesNumber = (int) (columnsNumber*rowsNumber);
        this.updateFrequence = updateFrequency;
        calculateSingeSpriteDimensions(xLeft, yLeft, xRight, yRight);
        calculateTimerData();
        lastSprite = spritesNumber;
    }
*/

    private void init(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        this.path = path;
        this.xLeft = xLeft;
        this.yLeft = yLeft;
        this.xRight = xRight;
        this.yRight = yRight;
        this.width = width;
        this.height = height;
        //System.out.println("Animation data: " + xLeft + "x" + yLeft + " to " + xRight + "x" + yRight);
    }

    public byte getRowsNumber(){
        return rowsNumber;
    }

    public byte getColumnsNumber(){
        return columnsNumber;
    }



    public void setLastSprite(int lastSprite){
        this.lastSprite = lastSprite;
    }

    private void calculateSingeSpriteDimensions(int xLeft, int yLeft, int xRight, int yRight){
        singleSpriteWidth = (int) PApplet.abs((xRight - xLeft)/columnsNumber);
        singleSpriteHeight = (int) PApplet.abs((yRight - yLeft)/rowsNumber);
        //System.out.println("singleSpriteWidth: " + singleSpriteWidth + "; singleSpriteHeight: " + singleSpriteHeight);
    }

    private void calculateTimerData(){
        timeToNextSprite = (int) (1000.0f/ (float)updateFrequence);
        //System.out.println("timeToNextSprite: " + timeToNextSprite);
    }

    public int getSpritesNumber(){
        return spritesNumber;
    }


/*
    public String getPathToAnimation(){
        return path;
    }
*/



    public boolean isAnimationAlreadyShown(){
        return animationAlreadyShown;
    }

    public void loadAnimation(Tileset tileset){
        this.tileset = tileset;
    }

    public void loadAnimation(){
        Image image = new Image(path);
        tileset = new Tileset(image);
        System.out.println("Animation was uploaded from path: " + path);
        //System.out.println("New tileset was created. It is null: " + (tileset == null));
       // this.tileset = tileset;
    }

    private void updateActualSpriteNumber(){
        if (nextSpriteTimer == null){
            nextSpriteTimer = new Timer(timeToNextSprite);
        }
        else {
            if (nextSpriteTimer.isTime()){
                if (animationPlayingDirection == FORWARD) {
                    actualSpriteNumber++;
                    if (actualSpriteNumber > lastSprite) {
                        actualSpriteNumber = (byte) startSprite;
                        if (!animationAlreadyShown) {
                            animationAlreadyShown = true;
                        }
                    }
                    /*

                     */
                    nextSpriteTimer.setNewTimer(timeToNextSprite);
                    //System.out.println("Actual sprite number for: " + this + " is : " + actualSpriteNumber);
                }
                else{
                    actualSpriteNumber--;
                    if (actualSpriteNumber < startSprite) {
                        actualSpriteNumber = (byte) lastSprite;
                        if (!animationAlreadyShown) {
                            animationAlreadyShown = true;
                        }
                    }
                    nextSpriteTimer.setNewTimer(timeToNextSprite);
                }
            }
        }
    }

    public boolean isActualSpriteLast(){
        if (Program.debug && Program.engine.frameCount % 30 == 0) System.out.println("Maybe I need to use last sprite-1 for this function");
        if (actualSpriteNumber == (lastSprite)){
            return true;
        }
        else return false;
    }

    public int getActualSpriteNumber(){
        return actualSpriteNumber;
    }

    public void setActualSpriteNumber(byte actualSpriteNumber) {
        this.actualSpriteNumber = actualSpriteNumber;
    }

    public void update(){
        if (animationStatement != SWITCHED_OFF && visible) {
            //if (Program.debug && Program.engine.frameCount%200 == 1) System.out.println("This func was not tested");
            /*if (playOnce){
                if (isActualSpriteLast()) {
                    animationStatement = PAUSED;
                    actualSpriteNumber = (byte) (spritesNumber-1);
                    System.out.println("Animation ends");
                }
            }*/
            if (animationStatement == ACTIVE) {
                updateActualSpriteNumber();
            }

        }
        if (!logAdded) {
            logAdded = true;
            //System.out.println("Actual sprite: " + actualSpriteNumber + " class: " + this);
        }

    }

    public Tileset getTileset(){
        return tileset;
    }

    public void drawSingleSprite(GameCamera gameCamera, Vec2 pos, float angleInDegrees, boolean flip, int spriteNumber){
        if (animationStatement != SWITCHED_OFF) {
            angleInDegrees = PApplet.radians(angleInDegrees);
            if (Program.WITH_GRAPHIC && tileset != null) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-angleInDegrees);
                if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
                showSingleSprite(spriteNumber);
                Program.objectsFrame.popMatrix();
            }

        }
    }

    public void drawWithoutTransformations(PGraphics graphics){

    }

    public void draw(PGraphics graphics, float x, float y, float scale){
        if (animationStatement != SWITCHED_OFF && visible) {
            if (Program.WITH_GRAPHIC && tileset != null) {
                graphics.pushMatrix();
                try {
                    graphics.pushStyle();
                    graphics.imageMode(PConstants.CENTER);
                    graphics.translate(x, y);
                    if (scale != 1) {
                        if (scale < 0){
                            graphics.scale(scale, (PApplet.abs(scale)));
                        }
                        else graphics.scale(scale);
                    }
                    if (animationStatement == ACTIVE) showSingleSprite(graphics, SHOW_ACTUAL_SPRITE);
                    else if (animationStatement == PAUSED) showSingleSprite(graphics, SHOW_ACTUAL_SPRITE);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    graphics.popStyle();
                    graphics.popMatrix();
                }
            }
        }
    }

    public void draw(GameCamera gameCamera, float x, float y, float angleInDegrees, float relativeVelocity){
        if (animationStatement != SWITCHED_OFF && visible) {
            angleInDegrees = PApplet.radians(angleInDegrees);
            if (Program.WITH_GRAPHIC && tileset != null) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.translate(x - gameCamera.getActualPosition().x*relativeVelocity + Program.objectsFrame.width / 2, y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-angleInDegrees);
                if (animationStatement == ACTIVE) showSingleSprite(SHOW_ACTUAL_SPRITE);
                else if (animationStatement == PAUSED) showSingleSprite(SHOW_ACTUAL_SPRITE);
                if (PApplet.degrees(angleInDegrees) > 65 && PApplet.degrees(angleInDegrees) < 69) {
                    //Game2D.objectsFrame.image(tileset.picture.image, pixelsSpritesShifting.x, pixelsSpritesShifting.y, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);

                    //System.out.println("Draw: " + tileset.picture.path + "Sprite" + actualSpriteNumber + " coll" + getActualColumn(actualSpriteNumber) + ". Row:  " + getActualRow(actualSpriteNumber) + " rows:"  + rowsNumber + " collumns: " + columnsNumber);
                }
                Program.objectsFrame.popMatrix();

            }
            else{
                //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No splaches ");
            }
        }
        else{
            //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No statement splaches ");
        }
        //if (angleInDegrees > 60 && angleInDegrees < 69) System.out.println("Problem with splaches ");
    }

    public void draw(GameCamera gameCamera, PVector pos, float angleInDegrees, boolean flip){
        //System.out.println("Sprite Animation");
        if (animationStatement != SWITCHED_OFF && visible) {
            //Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
            angleInDegrees = PApplet.radians(angleInDegrees);
            if (Program.WITH_GRAPHIC && tileset != null) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-angleInDegrees);
                if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
                if (animationStatement == ACTIVE) showSingleSprite(SHOW_ACTUAL_SPRITE);
                else if (animationStatement == PAUSED) showSingleSprite(SHOW_ACTUAL_SPRITE);
                if (PApplet.degrees(angleInDegrees) > 65 && PApplet.degrees(angleInDegrees) < 69) {
                    //Game2D.objectsFrame.image(tileset.picture.image, pixelsSpritesShifting.x, pixelsSpritesShifting.y, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);

                    //System.out.println("Draw: " + tileset.picture.path + "Sprite" + actualSpriteNumber + " coll" + getActualColumn(actualSpriteNumber) + ". Row:  " + getActualRow(actualSpriteNumber) + " rows:"  + rowsNumber + " collumns: " + columnsNumber);
                }
                Program.objectsFrame.popMatrix();

            }
            else{
                //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No splaches ");
            }
        }
        else{
            //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No statement splaches ");
        }
        //if (angleInDegrees > 60 && angleInDegrees < 69) System.out.println("Problem with splaches ");
    }

    public void draw(GameCamera gameCamera, Vec2 pos, float angleInDegrees, boolean flip){
        //System.out.println("Sprite Animation");
        if (animationStatement != SWITCHED_OFF && visible)  {
            //Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
            //float inRadians = PApplet.radians(angleInDegrees);
            angleInDegrees = PApplet.radians(angleInDegrees);
            if (Program.WITH_GRAPHIC && tileset != null) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-angleInDegrees);
                //System.out.println("Drawn");
                if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
                if (animationStatement == ACTIVE) showSingleSprite(SHOW_ACTUAL_SPRITE);
                else if (animationStatement == PAUSED) showSingleSprite(SHOW_ACTUAL_SPRITE);
                //System.out.println("It is drawn "+ getxLeft() + "x" + getxRight() + "; Actual: " + actualSpriteNumber);
                if (PApplet.degrees(angleInDegrees) > 65 && PApplet.degrees(angleInDegrees) < 69) {
                    //Game2D.objectsFrame.image(tileset.picture.image, pixelsSpritesShifting.x, pixelsSpritesShifting.y, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);

                    //System.out.println("Draw: " + tileset.picture.path + "Sprite" + actualSpriteNumber + " coll" + getActualColumn(actualSpriteNumber) + ". Row:  " + getActualRow(actualSpriteNumber) + " rows:"  + rowsNumber + " collumns: " + columnsNumber);
                }
                Program.objectsFrame.popMatrix();
                //Programm.objectsFrame.endDraw();
            }
            else{
                System.out.println("Tileset is null " + (tileset == null));
                //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No splaches ");
            }
        }
        else{
            //System.out.println("It is switched off");
            //if (PApplet.degrees(angleInDegrees) > 60 && PApplet.degrees(angleInDegrees) < 69) System.out.println("No statement splaches ");
        }
        //if (angleInDegrees > 60 && angleInDegrees < 69) System.out.println("Problem with splaches ");
    }

    public void draw(GameCamera gameCamera, Vec2 pos, float angleInDegrees, boolean flipX, boolean flipY){
        //System.out.println("Sprite Animation");
        if (animationStatement != SWITCHED_OFF && visible) {
            //Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
            angleInDegrees = PApplet.radians(angleInDegrees);
            if (Program.WITH_GRAPHIC && tileset != null) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-angleInDegrees);
                if (flipX == Image.FLIP && flipY == Image.FLIP) Program.objectsFrame.scale(-1.0f, -1.0f);
                else if (flipY == Image.FLIP && flipY != Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
                else if (flipX != Image.FLIP && flipY == Image.FLIP) Program.objectsFrame.scale(1.0f, -1.0f);
                else if (flipY != Image.FLIP && flipY != Image.FLIP) Program.objectsFrame.scale(1.0f, 1.0f);
                if (animationStatement == ACTIVE) showSingleSprite(SHOW_ACTUAL_SPRITE);
                else if (animationStatement == PAUSED) showSingleSprite(SHOW_ACTUAL_SPRITE);
                Program.objectsFrame.popMatrix();
            }
        }
        else if (!visible) System.out.println(" it is not visible"  + Program.engine.frameCount);
    }


    private void showSingleSprite(int spriteToShow){
        showSingleSprite(Program.objectsFrame, spriteToShow);
        /*
        if (spriteToShow != SHOW_ACTUAL_SPRITE) actualSpriteNumber = (byte)spriteToShow;
        int actualColumn = getActualColumn(actualSpriteNumber);
        int actualRow = getActualRow(actualSpriteNumber);

        if (withTint) Program.objectsFrame.tint(tintColor);
        if (pixelsSpritesShifting == null) Program.objectsFrame.image(tileset.picture.image, 0, 0, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);
        else Program.objectsFrame.image(tileset.picture.image, pixelsSpritesShifting.x, pixelsSpritesShifting.y, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);
        if (withTint) Program.objectsFrame.noTint();*/
    }

    private void showSingleSprite(PGraphics graphics, int spriteToShow){
        if (spriteToShow != SHOW_ACTUAL_SPRITE) actualSpriteNumber = (byte)spriteToShow;
        int actualColumn = getActualColumn(actualSpriteNumber);
        int actualRow = getActualRow(actualSpriteNumber);


        //System.out.println("Animation drawn! " + Program.engine.frameCount);
        if (withTint) graphics.tint(tintColor);
        if (pixelsSpritesShifting == null) graphics.image(tileset.picture.image, 0, 0, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);
        else graphics.image(tileset.picture.image, pixelsSpritesShifting.x, pixelsSpritesShifting.y, width, height, xLeft+actualColumn*singleSpriteWidth, yLeft+actualRow*singleSpriteHeight, xLeft+(actualColumn+1)*singleSpriteWidth, yLeft+(actualRow+1)*singleSpriteHeight);
        if (withTint) graphics.noTint();
    }

    private int getActualColumn(byte actualSpriteNumber){
        int actualColumn = actualSpriteNumber;
        while (actualColumn > (columnsNumber-1)){
            actualColumn-=columnsNumber;
        }
        return actualColumn;
    }

    private int getActualRow(byte actualSpriteNumber){
        return (int) PApplet.floor(actualSpriteNumber/columnsNumber);
    }

    public void setAnimationStatement(byte newStatement){
        if (animationStatement == PAUSED && newStatement == ACTIVE){
            if (nextSpriteTimer == null) nextSpriteTimer = new Timer(timeToNextSprite);
            else nextSpriteTimer.setNewTimer(timeToNextSprite);
        }
        animationStatement = newStatement;
    }

    public byte getAnimationStatement(){ return animationStatement;}

    public int getUpdateFrequency() {
        return updateFrequence;
    }

    public void setUpdateFrequency(int updateFrequence){
        this.updateFrequence = updateFrequence;
        calculateTimerData();
    }

    public void tintUpdatingBySelecting(IndependentOnScreenGraphic independentOnScreenAnimation, int actualSelectingValue) {

        if (independentOnScreenAnimation.isSelectionWasCleared()){
            resetTint();
            independentOnScreenAnimation.clearSelection();
        }
        if (independentOnScreenAnimation.isSelected()) {
            setTint(Program.engine.color(255, actualSelectingValue));
        }
    }

    public void setStartSprite(int startSprite) {
        this.startSprite = startSprite;
    }

    public void reset() {
        //System.out.println("ITt was reset");
        if (nextSpriteTimer!=null){
            nextSpriteTimer.setNewTimer(timeToNextSprite);
        }
        else nextSpriteTimer = new Timer(timeToNextSprite);
        actualSpriteNumber = (byte)startSprite;
        if (animationAlreadyShown) animationAlreadyShown = false;
    }

    public int getMarkedSprite() {
        return markedSprite;
    }

    public void setMarkedSprite(int markedSprite) {
        this.markedSprite = markedSprite;
    }

    public boolean getAnimationPlayingDirection() {
        return animationPlayingDirection;
    }

    public void setAnimationPlayingDirection(boolean animationPlayingDirection) {
        this.animationPlayingDirection = animationPlayingDirection;
    }

    public int getLastSprite() {
        return lastSprite;
    }

    @Override
    public void draw(GameCamera gameCamera, Vec2 drawPosition, float angle) {
        draw(gameCamera, drawPosition, angle, false);
    }

    public void pause() {
        if (animationStatement != PAUSED) {
            if (Program.debug) System.out.println("Animation was paused");
            setAnimationStatement(PAUSED);
        }
    }

    public void resume() {
        if (animationStatement != ACTIVE) {
            if (Program.debug) System.out.println("Animation was resumed");
            setAnimationStatement(ACTIVE);
        }
    }

    @Override
    public void hide() {
        super.hide();
        animationStatement = SWITCHED_OFF;
    }

    public void setPlayOnce(boolean b) {
        this.playOnce = b;
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
        if (animationStatement == SWITCHED_OFF) animationStatement = ACTIVE;
    }
}
