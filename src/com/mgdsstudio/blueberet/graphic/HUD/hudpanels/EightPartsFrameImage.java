package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;

import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class EightPartsFrameImage {
    //private final
    private int width, height, basicWidth, basicHeight, angleWidth;
    private int elementWidth, elementHeight;    // on of 9 elements dimension
    //private float dimensionsCoefX = 1f, dimensionsCoefY = 1f;
    private Vec2 leftUpperCorner;
    private Image mainImage;
    //private boolean mainGraphicFileLoaded;
    private ImageZoneSimpleData leftUpperAngleImageData, rightUpperAngleImageData, leftLowerAngleImageData, rightLowerAngleImageData;
    private ImageZoneSimpleData upperLineImageData, lowerLineImageData, leftLineImageData, rightLineImageData;
    //private ImageZoneSimpleData centralZoneImageData;
    private boolean shown = true;

    private final static int LEFT_UPPER = 0, RIGHT_UPPER = 1, LEFT_LOWER = 2, RIGHT_LOWER = 3, UPPER = 4, RIGHT = 5, LOWER = 6, LEFT = 7;

    public EightPartsFrameImage(ImageZoneSimpleData basicRect, int basicWidth, int basicHeight, int width, int height, Vec2 leftUpperCorner){
        mainImage = HeadsUpDisplay.mainGraphicSource;
        init(width, height, leftUpperCorner);
        initBasicDimensions(basicRect, basicWidth, basicHeight);
        initImageZoneData(basicRect);
        initElementDimensions();
    }

    public EightPartsFrameImage(Image source, ImageZoneSimpleData basicRect, int basicWidth, int basicHeight, int width, int height, Vec2 leftUpperCorner){
        mainImage = source;
        init(width, height, leftUpperCorner);
        initBasicDimensions(basicRect, basicWidth, basicHeight);
        initImageZoneData(basicRect);
        initElementDimensions();
    }

    private void init( int width, int height, Vec2 leftUpperCorner){
        this.leftUpperCorner = leftUpperCorner;
        this.width = width;
        this.height = height;
    }

    private void initElementDimensions() {
        elementWidth = PApplet.ceil( basicWidth/3f);
        elementHeight = PApplet.ceil( basicHeight/3f);
    }

    private void initImageZoneData(ImageZoneSimpleData basicRect) {
        int singleZoneWidth = PApplet.ceil((basicRect.rightX-basicRect.leftX)/3f);
        int singleZoneHeight =  PApplet.ceil((basicRect.lowerY-basicRect.upperY)/3f);
        leftUpperAngleImageData = new ImageZoneSimpleData(basicRect.leftX,basicRect.upperY,basicRect.leftX+singleZoneWidth,basicRect.upperY+singleZoneHeight);
        rightUpperAngleImageData = new ImageZoneSimpleData(basicRect.rightX-singleZoneWidth, basicRect.upperY,basicRect.rightX,basicRect.upperY+singleZoneHeight);
        leftLowerAngleImageData = new ImageZoneSimpleData(basicRect.leftX,basicRect.lowerY-singleZoneHeight,basicRect.leftX+singleZoneWidth,basicRect.lowerY);
        rightLowerAngleImageData = new ImageZoneSimpleData(basicRect.rightX-singleZoneWidth, basicRect.lowerY-singleZoneHeight,basicRect.rightX,basicRect.lowerY);

        upperLineImageData = new ImageZoneSimpleData(basicRect.leftX+singleZoneWidth, basicRect.upperY,basicRect.rightX-singleZoneWidth, basicRect.upperY+singleZoneHeight);
        lowerLineImageData = new ImageZoneSimpleData(basicRect.leftX+singleZoneWidth, basicRect.lowerY-singleZoneHeight,basicRect.rightX-singleZoneWidth, basicRect.lowerY);
        leftLineImageData = new ImageZoneSimpleData(basicRect.leftX, basicRect.upperY+singleZoneHeight,basicRect.leftX+singleZoneWidth, basicRect.lowerY-singleZoneHeight);
        rightLineImageData = new ImageZoneSimpleData(basicRect.rightX-singleZoneWidth, basicRect.upperY+singleZoneHeight,basicRect.rightX, basicRect.lowerY-singleZoneHeight);
    }


    private void initBasicDimensions(ImageZoneSimpleData basicRect, int basicWidth, int basicHeight){
        if (basicWidth <= 0 || basicHeight <= 0){
            basicWidth = basicRect.rightX-basicRect.leftX;
            basicHeight = basicRect.lowerY-basicRect.upperY;
            System.out.println("Basic width set on: " + basicWidth + "; height: " + basicHeight);
        }
        else{
            this.basicHeight = basicHeight;
            this.basicWidth = basicWidth;
            System.out.println("Basic width set on: " + basicWidth + "; height: " + basicHeight);
        }
    }

    public void draw(PGraphics graphics){
        if (shown) {
            graphics.pushStyle();
            graphics.imageMode(PConstants.CORNER);
            drawCentralZone(graphics);
            drawLine(graphics, UPPER);
            drawLine(graphics, RIGHT);
            drawLine(graphics, LOWER);
            drawLine(graphics, LEFT);
            drawCorner(graphics, LEFT_UPPER);
            drawCorner(graphics, RIGHT_UPPER);
            drawCorner(graphics, LEFT_LOWER);
            drawCorner(graphics, RIGHT_LOWER);
            graphics.imageMode(PConstants.CORNER);
            graphics.popStyle();
        }
    }

    private void drawCentralZone(PGraphics graphics) {
        graphics.pushMatrix();
        graphics.translate(leftUpperCorner.x+elementWidth, leftUpperCorner.y+elementHeight);
        graphics.image(mainImage.getImage(), 0,0, width-2*elementWidth, height-2*elementHeight, leftUpperAngleImageData.rightX, leftUpperAngleImageData.lowerY, rightLowerAngleImageData.leftX, rightLowerAngleImageData.upperY);
        graphics.popMatrix();
    }

    private void drawLine(PGraphics graphics, int side) {
        graphics.pushMatrix();
        if (side == UPPER){
            graphics.translate(leftUpperCorner.x+elementWidth, leftUpperCorner.y);
            graphics.image(mainImage.getImage(), 0,0, width-2*elementWidth, elementHeight, upperLineImageData.leftX, upperLineImageData.upperY, upperLineImageData.rightX, upperLineImageData.lowerY);
        }
        else if (side == LOWER){
            graphics.translate(leftUpperCorner.x+elementWidth, leftUpperCorner.y+height-elementHeight);
            graphics.image(mainImage.getImage(), 0,0, width-2*elementWidth, elementHeight, lowerLineImageData.leftX, lowerLineImageData.upperY, lowerLineImageData.rightX, lowerLineImageData.lowerY);
        }
        else if (side == LEFT){
            graphics.translate(leftUpperCorner.x, leftUpperCorner.y+elementHeight);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, height-2*elementHeight, leftLineImageData.leftX, leftLineImageData.upperY, leftLineImageData.rightX, leftLineImageData.lowerY);
        }
        else{
            graphics.translate(leftUpperCorner.x+width-elementWidth, leftUpperCorner.y+elementHeight);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, height-2*elementHeight,  rightLineImageData.leftX, rightLineImageData.upperY,  rightLineImageData.rightX,  rightLineImageData.lowerY);
        }
        graphics.popMatrix();


    }

    private void drawCorner(PGraphics graphics, int side) {
        graphics.pushMatrix();
        if (side == LEFT_UPPER){
            graphics.translate(leftUpperCorner.x, leftUpperCorner.y);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, elementHeight, leftUpperAngleImageData.leftX, leftUpperAngleImageData.upperY, leftUpperAngleImageData.rightX, leftUpperAngleImageData.lowerY);
            //System.out.println("Drawn with data: " + leftUpperCorner.x +"x"+ leftUpperCorner.y + "; Dim: " +  elementWidth +"x"+ elementHeight);
        }
        else if (side == RIGHT_UPPER){
            graphics.translate(leftUpperCorner.x+width-elementWidth, leftUpperCorner.y);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, elementHeight, rightUpperAngleImageData.leftX, rightUpperAngleImageData.upperY, rightUpperAngleImageData.rightX, rightUpperAngleImageData.lowerY);
        }
        else if (side == LEFT_LOWER) {
            graphics.translate(leftUpperCorner.x, leftUpperCorner.y+height-elementHeight);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, elementHeight, leftLowerAngleImageData.leftX, leftLowerAngleImageData.upperY, leftLowerAngleImageData.rightX, leftLowerAngleImageData.lowerY);
        }
        else {
            graphics.translate(leftUpperCorner.x+width-elementWidth, leftUpperCorner.y+height-elementHeight);
            graphics.image(mainImage.getImage(), 0,0, elementWidth, elementHeight, rightLowerAngleImageData.leftX, rightLowerAngleImageData.upperY, rightLowerAngleImageData.rightX, rightLowerAngleImageData.lowerY);

        }

        graphics.popMatrix();
    }


    /*
    private void drawCorner(PGraphics graphics, int side) {
        graphics.pushMatrix();
        if (side == LEFT_UPPER){
            graphics.translate(leftUpperCorner.x-width/2f, leftUpperCorner.y);
            graphics.image(mainImage.getImage(), 0,0, angleWidth, angleWidth);
        }

        else if (side == RIGHT_UPPER){
            graphics.translate(leftUpperCorner.x+width/2f, leftUpperCorner.y);
            graphics.rotate(-PApplet.HALF_PI);
            graphics.image(mainImage.getImage(), 0,0, angleWidth, angleWidth);
        }
        else if (side == LEFT_LOWER){
            graphics.translate(leftUpperCorner.x-width/2f, leftUpperCorner.y+height);
            graphics.rotate(PApplet.HALF_PI);
            graphics.image(mainImage.getImage(), 0,0, angleWidth, angleWidth);
        }
        else {
            graphics.translate(leftUpperCorner.x+width/2f, leftUpperCorner.y+height);
            graphics.rotate(PApplet.PI);
            graphics.image(mainImage.getImage(), 0,0, angleWidth, angleWidth);
        }
        graphics.popMatrix();
    }
     */



    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vec2 getLeftUpperCorner() {
        return leftUpperCorner;
    }


    public void setCenterPosition(int xPos, int yPos) {
        leftUpperCorner.x = xPos-width/2;
        leftUpperCorner.y = yPos-height/2;
    }


    public boolean isClicked(int mouseX, int mouseY) {
        boolean clicked = GameMechanics.isPointInRect(mouseX, mouseY, leftUpperCorner.x, leftUpperCorner.y, width, height);
        if (clicked) System.out.println("Mouse is pressed on frame");
        return clicked;
    }

    public void setLeftUpperCorner(float x, float y) {
        this.leftUpperCorner.x = x;
        this.leftUpperCorner.y = y;

    }
}
