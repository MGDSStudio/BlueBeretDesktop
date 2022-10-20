package com.mgdsstudio.blueberet.gameprocess.control;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class FivePartsStick extends StickAbstract {
    public final static int NORMAL_STICK_HEIGHT = (int) (Program.engine.width/2.6f);
    private final int stayZoneWidth, goZoneWidth, runZoneWidth;
    private static Image centralZonePicture;
    //public static ImageZoneSimpleData centralZonePictureData, leftRunZonePictureData, leftGoZonePictureData;
    public static final ImageZoneSimpleData centralZonePictureData = new ImageZoneSimpleData(568,352, 644,512);
    public static final ImageZoneSimpleData leftGoZonePictureData = new ImageZoneSimpleData(534,352, 568, 512);
    public static final ImageZoneSimpleData leftRunZonePictureData = new ImageZoneSimpleData(509,352,534,512);
    private final int baseWidth, baseHeight;
    private final int centerLeftRun, centerLeftGo, centerStay, centerRightGo, centerRightRun;
    private Rectangular leftRunZone, leftGoZone, stayZone, rightGoZone, rightRunZone, allStickHoleZone;
    //private final int deadZoneRadius;
    private final Vec2 mutTouchesCenter;
    //private final ArrayList<PVector> onStickTouches = new ArrayList<>(4) ;
    private final ArrayList <Coordinate> onStickTouchesArray = new ArrayList<>(4);
    private final ArrayList <Coordinate> mutStickTouchesPool = new ArrayList<>(4);
    public final static int MAX_UPPER_ANGLE = 55;     //Relative to 0
    private final int MAX_LOWER_ANGLE = MAX_UPPER_ANGLE; //Relative to 0
    private final boolean HIDE_CROSSHAIR_UNDER_FINGER = true;
    //private final boolean withAdoptedRedrawing = PlayerControl.WITH_ADOPTING_GUI_REDRAWING;
    private boolean doNotDrawAtThisFrame = false;


    public FivePartsStick (int left, int lower, int width, int height){
        mutTouchesCenter = new Vec2(left+width/2,lower-height/2);
        if (stickPicture == null) {
            loadStickPictureData();
        }
        if (centralZonePicture == null) {
            loadBaseGraphicData();
        }
        float relationshipStay = 0.4f;
        float relationshipRun = 0.125f;
        float relativeDeadZoneRadius = 0.025f;
        deadZoneRadius = (int)(width*relativeDeadZoneRadius);
        float stayZonePartValue = relationshipStay;
        float goZonePartValue = (1f-relationshipRun*2-relationshipStay)/2f;
        float runZonePartValue = relationshipRun;
        stayZoneWidth = PApplet.ceil(width*stayZonePartValue);
        goZoneWidth = PApplet.ceil(width*goZonePartValue);
        runZoneWidth = PApplet.ceil(width*runZonePartValue);
        centerStay = left+width/2;
        centerLeftGo = left+width/2-(stayZoneWidth/2)-(goZoneWidth/2);
        centerLeftRun = centerLeftGo-(goZoneWidth/2)-(runZoneWidth/2);
        centerRightGo = left+width/2+(stayZoneWidth/2)+(goZoneWidth/2);
        centerRightRun = centerRightGo+(goZoneWidth/2)+(runZoneWidth/2);
        actualPosition = new Vec2(left+width/2,lower-height/2);
        basicPosition = new Vec2(left+width/2,lower-height/2);
        stickVisibleDiameter = (int)(height/2.8f);
        baseHeight = height;
        baseWidth = width;
        visibilityStatement = true;
        System.out.println("Width: stay: " + stayZoneWidth + " go " + goZoneWidth + " run " + runZoneWidth + " basic " + baseWidth);
        leftRunZone = new Rectangular(centerLeftRun, lower-height/2, runZoneWidth, height);
        leftGoZone = new Rectangular(centerLeftGo, lower-height/2, goZoneWidth, height);
        stayZone = new Rectangular(left+width/2,lower-height/2, stayZoneWidth, height);
        rightGoZone = new Rectangular(centerRightGo,lower-height/2, goZoneWidth, height)  ;
        rightRunZone = new Rectangular(centerRightRun, lower-height/2, runZoneWidth, height);
        allStickHoleZone = new Rectangular(left+width/2,lower-height/2, width, height);
        fillPositionsData();
    }

    /*
    public FivePartsStick (int x, int y, int width, int height){
        mutTouchesCenter = new Vec2(x,y);
        if (stickPicture == null) {
            loadStickPictureData();
        }
        if (centralZonePicture == null) {
            loadBaseGraphicData();
        }
        float relationshipStay = 0.4f;
        float relationshipRun = 0.125f;
        float relativeDeadZoneRadius = 0.025f;
        deadZoneRadius = (int)(width*relativeDeadZoneRadius);
        float stayZonePartValue = relationshipStay;
        float goZonePartValue = (1f-relationshipRun*2-relationshipStay)/2f;
        float runZonePartValue = relationshipRun;
        stayZoneWidth = PApplet.ceil(width*stayZonePartValue);
        goZoneWidth = PApplet.ceil(width*goZonePartValue);
        runZoneWidth = PApplet.ceil(width*runZonePartValue);
        centerStay = x;
        centerLeftGo = x-(stayZoneWidth/2)-(goZoneWidth/2);
        centerLeftRun = centerLeftGo-(goZoneWidth/2)-(runZoneWidth/2);
        centerRightGo = x+(stayZoneWidth/2)+(goZoneWidth/2);
        centerRightRun = centerRightGo+(goZoneWidth/2)+(runZoneWidth/2);
        actualPosition = new Vec2(x,y);
        basicPosition = new Vec2(x,y);
        stickVisibleDiameter = (int)(height/2.8f);
        baseHeight = height;
        baseWidth = width;
        visibilityStatement = true;
        System.out.println("Width: stay: " + stayZoneWidth + " go " + goZoneWidth + " run " + runZoneWidth + " basic " + baseWidth);
        leftRunZone = new Rectangular(centerLeftRun, y, runZoneWidth, height);
        leftGoZone = new Rectangular(centerLeftGo, y, goZoneWidth, height);
        stayZone = new Rectangular(x,y, stayZoneWidth, height);
        rightGoZone = new Rectangular(centerRightGo,y, goZoneWidth, height)  ;
        rightRunZone = new Rectangular(centerRightRun, y, runZoneWidth, height);
        allStickHoleZone = new Rectangular(x,y, width, height);
        fillPositionsData();
    }*/


    private void loadBaseGraphicData(){
        centralZonePicture = HeadsUpDisplay.mainGraphicSource;

    }

    private void fillPositionsData(){

    }

    public void update(){
        if (Program.OS == Program.ANDROID){
            updateStatementForAndroidMode();
        }
    }

    //public static boolean isPointInRect(int pointX, int pointY, int leftUpperX, int leftUpperY, int rectWidth, int rectHeight){


    private void updateStatementForAndroidMode() {
        Vec2 touchesCenter = getCenterTouchPosition();
        if (isPointOnStickZone(touchesCenter.x, touchesCenter.y)){
            updateActualZone(touchesCenter);
        }
        else actualZone = IN_DEAD_ZONE;
        updateAngleToPressPos(touchesCenter);
        updateStickPos(touchesCenter);
        updateStickInMovementZonePosition(touchesCenter);
    }



    private boolean isPointOnStickZone(float x, float y){
        //if (GameMechanics.isPointInRect((int)x, (int)y, (int)(basicPosition.x-baseWidth/2), (int)(basicPosition.y-baseHeight/2), (int)(basicPosition.x+baseWidth/2), (int)(basicPosition.y+baseHeight/2))){
        //
        if (GameMechanics.isPointInRect(x, y, allStickHoleZone)){
            return  true;
        }
        else return false;
    }

    private void updateActualZone(Vec2 touchesCenter) {
        if (GameMechanics.isPointInRect(touchesCenter.x, touchesCenter.y, leftRunZone)){
            actualZone = IN_RUN_ZONE;
            //actualZone = IN_LEFT_RUN_ZONE;
        }
        else if (GameMechanics.isPointInRect(touchesCenter.x, touchesCenter.y, leftGoZone)){
            actualZone = IN_GO_ZONE;
            //actualZone = IN_LEFT_GO_ZONE;
        }
        else if (GameMechanics.isPointInCircle(touchesCenter, basicPosition, deadZoneRadius)){
            actualZone = IN_DEAD_ZONE;
        }
        else if (GameMechanics.isPointInRect(touchesCenter.x, touchesCenter.y, stayZone)){
            actualZone = IN_AIMING_ZONE;
        }
        else if (GameMechanics.isPointInRect(touchesCenter.x, touchesCenter.y, rightGoZone)){
            actualZone = IN_GO_ZONE;
            //actualZone = IN_RIGHT_GO_ZONE;
        }
        else if (GameMechanics.isPointInRect(touchesCenter.x, touchesCenter.y, rightRunZone)){
            actualZone = IN_RUN_ZONE;
            //actualZone = IN_RIGHT_RUN_ZONE;
        }


    }

    private void updateAngleToPressPos(Vec2 touchesCenter){
        if (actualZone == IN_GO_ZONE || actualZone == IN_AIMING_ZONE || actualZone == IN_RUN_ZONE) updateAngle(touchesCenter);
        else if (!Program.engine.mousePressed) resetAngle();
    }

    private void fillOnStickTouchesArray(){
        Program.iEngine.fillOnStickTouchesArray(onStickTouchesArray, mutStickTouchesPool, allStickHoleZone);


    }

    private Vec2 getCenterTouchPosition() {
        if (Program.OS == Program.DESKTOP) {
            mutTouchesCenter.x = Program.engine.mouseX;
            mutTouchesCenter.y = Program.engine.mouseY;
        }
        else if (Program.OS == Program.ANDROID) {
            try {
                //System.out.println("MANY GARBAGE!");
                fillOnStickTouchesArray();
            }
            catch (Exception e){
                System.out.println("Touches array is null");
                e.printStackTrace();
            }
            float x = 0;
            float y = 0;
            for (int j = 0; j < onStickTouchesArray.size(); j++) {
                x+= onStickTouchesArray.get(j).x;
                y+= onStickTouchesArray.get(j).y;
            }
            x/= onStickTouchesArray.size();
            y/= onStickTouchesArray.size();
            mutTouchesCenter.x = x;
            mutTouchesCenter.y = y;
            //System.out.println("Touches center: " + mutTouchesCenter.x + "x" + mutTouchesCenter.y);
            return mutTouchesCenter;
        }
        else {
            System.out.println("There are no data about OS for this stick control");
            return null;
        }

        return mutTouchesCenter;
    }

    private void resetAngle(){

        if (angleToClickPlace >90 && angleToClickPlace <270) angleToClickPlace = 180;
        else angleToClickPlace = 0;
        //System.out.println("Angle was reset to: " + angleToClickPlace);
    }

    @Override
    protected void updateAngle(Vec2 touchesCenter) {
        float relativeDistanceAlongY = (int) (basicPosition.y-touchesCenter.y);
        float relativeAngle = 0;
        if (touchesCenter.x>basicPosition.x) {
            if (relativeDistanceAlongY > 0) {    // Weapon to up
                relativeAngle = (int) (relativeDistanceAlongY * MAX_UPPER_ANGLE) / (baseHeight / 2);
                angleToClickPlace=(int)(360-relativeAngle);
            } else {
                angleToClickPlace = (int) (-relativeDistanceAlongY * MAX_LOWER_ANGLE) / (baseHeight / 2);
            }
        }
        else {
            if (relativeDistanceAlongY > 0) {    // Weapon to up
                relativeAngle = (int) (relativeDistanceAlongY * MAX_UPPER_ANGLE) / (baseHeight / 2);
                angleToClickPlace=(int)(180+relativeAngle);
            } else {
                relativeAngle = (int) (-relativeDistanceAlongY * MAX_LOWER_ANGLE) / (baseHeight / 2);
                angleToClickPlace = (int)(180-relativeAngle);
            }
        }
        //System.out.println("Angle was set to " + angleToClickPlace);
    }

    private void drawWithRegularUpdating(PGraphics graphics){
        graphics.imageMode(PConstants.CENTER);
        float widthDimensionCoef = 0.93f;
        graphics.image(centralZonePicture.getImage(), centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
        graphics.image(centralZonePicture.getImage(), centerLeftGo, basicPosition.y, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
        graphics.image(centralZonePicture.getImage(), basicPosition.x, basicPosition.y, stayZoneWidth, baseHeight, centralZonePictureData.leftX, centralZonePictureData.upperY, centralZonePictureData.rightX, centralZonePictureData.lowerY);
        graphics.pushMatrix();
        graphics.translate(centerRightGo, basicPosition.y);
        graphics.rotate(PConstants.PI);
        graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
        graphics.popMatrix();
        graphics.pushMatrix();
        graphics.translate(centerRightRun, basicPosition.y);
        graphics.rotate(PConstants.PI);
        graphics.image(centralZonePicture.getImage(), 0, 0, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
        graphics.popMatrix();
        if (actualZone != IN_DEAD_ZONE && !HIDE_CROSSHAIR_UNDER_FINGER) graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.leftX, stickZoneSimpleData.upperY, stickZoneSimpleData.rightX, stickZoneSimpleData.lowerY);
    }

    private void clearForOneFrame(PGraphics graphics) {
        if (previousVisibilityStatement != visibilityStatement) {
            graphics.imageMode(PConstants.CENTER);
            float widthDimensionCoef = 0.93f;
            graphics.image(centralZonePicture.getImage(), centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.image(centralZonePicture.getImage(), centerLeftGo, basicPosition.y, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.image(centralZonePicture.getImage(), basicPosition.x, basicPosition.y, stayZoneWidth, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.pushMatrix();
            graphics.translate(centerRightGo, basicPosition.y);
            graphics.rotate(PConstants.PI);
            if (visibilityStatement == VISIBLE) graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            else graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.popMatrix();
            graphics.pushMatrix();
            graphics.translate(centerRightRun, basicPosition.y);
            graphics.rotate(PConstants.PI);
            graphics.image(centralZonePicture.getImage(), 0, 0, runZoneWidth, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.popMatrix();
            if (actualZone != IN_DEAD_ZONE && !HIDE_CROSSHAIR_UNDER_FINGER) graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.leftX, stickZoneSimpleData.upperY, stickZoneSimpleData.rightX, stickZoneSimpleData.lowerY);
        }
    }
    private void drawWithAdoptedRedrawing(PGraphics graphics) {
        if (previousVisibilityStatement != visibilityStatement) {
            graphics.imageMode(PConstants.CENTER);
            float widthDimensionCoef = 0.93f;
            if (visibilityStatement == VISIBLE){
                graphics.image(centralZonePicture.getImage(), centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
                graphics.image(centralZonePicture.getImage(), centerLeftGo, basicPosition.y, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
                graphics.image(centralZonePicture.getImage(), basicPosition.x, basicPosition.y, stayZoneWidth, baseHeight, centralZonePictureData.leftX, centralZonePictureData.upperY, centralZonePictureData.rightX, centralZonePictureData.lowerY);
                graphics.pushMatrix();
                graphics.translate(centerRightGo, basicPosition.y);
                graphics.rotate(PConstants.PI);
                graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
                //graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                graphics.popMatrix();
                graphics.pushMatrix();
                graphics.translate(centerRightRun, basicPosition.y);
                graphics.rotate(PConstants.PI);
                graphics.image(centralZonePicture.getImage(), 0, 0, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
                if (actualZone != IN_DEAD_ZONE && !HIDE_CROSSHAIR_UNDER_FINGER) graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.leftX, stickZoneSimpleData.upperY, stickZoneSimpleData.rightX, stickZoneSimpleData.lowerY);
                graphics.popMatrix();
            }
            else{
                //graphics.image(centralZonePicture.getImage(), centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
                //graphics.image(centralZonePicture.getImage(), centerLeftGo, basicPosition.y, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
                graphics.image(centralZonePicture.getImage(), basicPosition.x, basicPosition.y, stayZoneWidth+runZoneWidth*2+goZoneWidth*2, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                /*graphics.pushMatrix();
                graphics.translate(centerRightGo, basicPosition.y);
                graphics.rotate(PConstants.PI);
                //graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
                graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                graphics.popMatrix();
                graphics.pushMatrix();
                graphics.translate(centerRightRun, basicPosition.y);
                graphics.rotate(PConstants.PI);
                graphics.image(centralZonePicture.getImage(), 0, 0, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
*/
            }

        }
    }

    private void drawWithAdoptedRedrawingOld(PGraphics graphics) {
        if (previousVisibilityStatement != visibilityStatement) {
            graphics.imageMode(PConstants.CENTER);
            float widthDimensionCoef = 0.93f;
            graphics.image(centralZonePicture.getImage(), centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
            graphics.image(centralZonePicture.getImage(), centerLeftGo, basicPosition.y, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
            graphics.image(centralZonePicture.getImage(), basicPosition.x, basicPosition.y, stayZoneWidth, baseHeight, centralZonePictureData.leftX, centralZonePictureData.upperY, centralZonePictureData.rightX, centralZonePictureData.lowerY);
            graphics.pushMatrix();
            graphics.translate(centerRightGo, basicPosition.y);
            graphics.rotate(PConstants.PI);
            if (visibilityStatement == VISIBLE) graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, leftGoZonePictureData.leftX, leftGoZonePictureData.upperY, leftGoZonePictureData.rightX, leftGoZonePictureData.lowerY);
            else graphics.image(centralZonePicture.getImage(), 0, 0, goZoneWidth * widthDimensionCoef, baseHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            graphics.popMatrix();
            graphics.pushMatrix();
            graphics.translate(centerRightRun, basicPosition.y);
            graphics.rotate(PConstants.PI);
            graphics.image(centralZonePicture.getImage(), 0, 0, runZoneWidth, baseHeight, leftRunZonePictureData.leftX, leftRunZonePictureData.upperY, leftRunZonePictureData.rightX, leftRunZonePictureData.lowerY);
            graphics.popMatrix();
            if (actualZone != IN_DEAD_ZONE && !HIDE_CROSSHAIR_UNDER_FINGER) graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.leftX, stickZoneSimpleData.upperY, stickZoneSimpleData.rightX, stickZoneSimpleData.lowerY);
        }
    }


    @Override
    public void draw(PGraphics graphics) {
        if (PlayerControl.withAdoptingGuiRedrawing){
            if (!doNotDrawAtThisFrame) {
                drawWithAdoptedRedrawing(graphics);
                //System.out.println("Draw by adopted");
            }
            else {
                clearForOneFrame(graphics);
                doNotDrawAtThisFrame = false;
            }
        }
        else {
            if (visibilityStatement == VISIBLE) {
                if (!doNotDrawAtThisFrame){
                    drawWithRegularUpdating(graphics);
                    //System.out.println("Draw by regualar");
                }
                else {
                    clearForOneFrame(graphics);
                    doNotDrawAtThisFrame = false;
                }
            }
        }
        previousVisibilityStatement = visibilityStatement;
    }

    private void drawDebugGraphic(){
        /*
        graphics.beginDraw();
            graphics.pushStyle();
            graphics.fill(255,25,25,125);
            graphics.rectMode(PConstants.CENTER);
            float coef = 1.08f;
            float radius = 25;
            graphics.noStroke();
            //graphics.rect(basicPosition.x, basicPosition.y, coef*baseWidth, coef*baseHeight);
            graphics.fill(25,125,25,75);
            //graphics.stroke(5);

            graphics.strokeWeight(8);

            graphics.rect(centerLeftRun, basicPosition.y, runZoneWidth, baseHeight, radius);
            graphics.fill(0,0,125,122);
            //graphics.rect(centerLeftGo, basicPosition.y, goZoneWidth, baseHeight, radius);
            graphics.fill(0,75,0,255);
            graphics.rect(basicPosition.x, basicPosition.y, stayZoneWidth, baseHeight, radius);
            graphics.fill(125,25,125,175);
            //graphics.rect(centerRightGo, basicPosition.y, goZoneWidth, baseHeight, radius);
            graphics.fill(225,225,225,255);
            graphics.rect(centerRightRun, basicPosition.y, runZoneWidth, baseHeight, radius);

            graphics.image(stickHole.getImage(), basicPosition.x, basicPosition.y, holeVisibleDiameter, holeVisibleDiameter, stickHoleZoneSimpleData.getLeftUpperX(), stickHoleZoneSimpleData.getLeftUpperY(), stickHoleZoneSimpleData.getRightLowerX(), stickHoleZoneSimpleData.getRightLowerY());

            graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.getLeftUpperX(), stickZoneSimpleData.getLeftUpperY(), stickZoneSimpleData.getRightLowerX(), stickZoneSimpleData.getRightLowerY());

            graphics.popStyle();
            graphics.endDraw();
         */
    }

    public void hideForNextFrame(){
        doNotDrawAtThisFrame = true;
    }

    private void updateStickInMovementZonePosition(Vec2 touchesCenter){
        if (actualZone != IN_DEAD_ZONE && actualZone != IN_AIMING_ZONE) inMovementZone = true;
        else inMovementZone = false;
    }

    public void hideForOneFrame() {
    }
}
