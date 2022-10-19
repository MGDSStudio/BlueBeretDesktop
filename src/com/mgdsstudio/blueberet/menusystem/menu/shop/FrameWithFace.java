package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.BeretColorChanger;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.controllers.PhotoDrawingController;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.HashMap;
import java.util.Map;

public class FrameWithFace {
    public final static boolean WAITING = false;
    public final static boolean SPEAKING = true;
    private boolean faceStatement = WAITING;


    private final static int EYES_OPENED = 0;
    private final static int EYES_CLOSED = 1;
    private final static int MOUTH_OPENED = 2;
    private HashMap <Integer, ImageZoneSimpleData> images;
    private int actualImage = EYES_OPENED;

    private Timer timer;
    private final static int TIME_TO_NEXT_EYES_CLOSING_CIRCLE = 2000;
    private final static int TIME_TO_NEXT_EYES_OPENING_CIRCLE = 250;
    private final static int TIME_TO_NEXT_MOUTH_OPENED_CIRCLE = 200;
    private final float randomForkValue = 0.8f;
    private int speakingCicles = 0;
    private final static int FRAME_TO_CLOSE_EYES_BY_SPEAKING = 9;
    //private final int  = 0;

    private EightPartsFrameImage frame;
    public static final int PLAYER = 0;
    public static final int HANDLER = 1;

    private Vec2 frameCenter;
    private final Image image = new Image(Program.getAbsolutePathToAssetsFolder(HUD_GraphicData.mainGraphicFile.getPath()));

    public FrameWithFace(int faceType, Vec2 frameCenter ) {
        this.frameCenter = frameCenter;
        initFaceGraphic(faceType);
        changeBeretColor();
    }

    private void changeBeretColor() {
        BeretColorChanger beretColorChanger = new BeretColorChanger();
        BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(Program.engine);
        beretColorLoadingMaster.loadData();
        TwiceColor twiceColor = beretColorLoadingMaster.getBeretColor();
        beretColorChanger.changeColor(twiceColor, image);
    }

    private void initFaceGraphic(int faceType) {
        images = new HashMap<>();

        if (faceType == PLAYER){
            ImageZoneSimpleData eyesOpened = HUD_GraphicData.playerFaceFullLife;
            ImageZoneSimpleData eyesClosed = HUD_GraphicData.playerFaceEyesClosedFullLife;
            ImageZoneSimpleData mouthOpened = HUD_GraphicData.playerFaceFullLifeMouthOpened;

            images.put( EYES_OPENED, eyesOpened);
            images.put( EYES_CLOSED,eyesClosed);
            images.put(MOUTH_OPENED, mouthOpened);
        }
        else if (faceType == HANDLER){
            ImageZoneSimpleData eyesOpened = HUD_GraphicData.dealerFaceEyesOpened;
            ImageZoneSimpleData eyesClosed = HUD_GraphicData.dealerFaceEyesClosed;
            ImageZoneSimpleData mouthOpened = HUD_GraphicData.dealerFaceMouthOpened;

            images.put( EYES_OPENED, eyesOpened);
            images.put( EYES_CLOSED,eyesClosed);
            images.put(MOUTH_OPENED, mouthOpened);
        }
        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        float freeZone = Program.engine.width/200f;
        int width = (int) (UpperPanel.HEIGHT-2*freeZone);
        int height = width;

        Vec2 leftUpper = new Vec2(frameCenter.x-width/2, frameCenter.y-height/2);
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpper);
    }

    public void update(PApplet engine){
        if (faceStatement == WAITING){
            updateWaiting(engine);
        }
        else updateSpeaking(engine);
    }


    private int getNextRandomTimeForSpeaking(PApplet engine){
        if (speakingCicles % FRAME_TO_CLOSE_EYES_BY_SPEAKING == 1){
            return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);
        }
        else {
            if (actualImage == EYES_OPENED){
                return (int) engine.random(TIME_TO_NEXT_MOUTH_OPENED_CIRCLE-TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue, TIME_TO_NEXT_MOUTH_OPENED_CIRCLE+TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue);
            }
            else if (actualImage == MOUTH_OPENED){
                return (int) engine.random(TIME_TO_NEXT_MOUTH_OPENED_CIRCLE-TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue, TIME_TO_NEXT_MOUTH_OPENED_CIRCLE+TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue);
            }
            else {
                return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);

            }
            //return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);
        }

    }

    private int getNextRandomTimeForWaiting(PApplet engine){
        if (actualImage == EYES_OPENED){
            return (int) engine.random(TIME_TO_NEXT_EYES_CLOSING_CIRCLE-TIME_TO_NEXT_EYES_CLOSING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_CLOSING_CIRCLE+TIME_TO_NEXT_EYES_CLOSING_CIRCLE*randomForkValue);
        }
        return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);
    }

    private void updateSpeaking(PApplet engine) {
        if (timer == null){
            int time = getNextRandomTimeForSpeaking(engine);
            timer = new Timer(time);
        }
        else {
            if (timer.isTime()){
                speakingCicles++;
                if (speakingCicles % FRAME_TO_CLOSE_EYES_BY_SPEAKING == 1){
                    actualImage = EYES_CLOSED;
                }
                else {
                    if (actualImage == EYES_OPENED) actualImage = MOUTH_OPENED;
                    else if (actualImage == MOUTH_OPENED) actualImage = EYES_OPENED;
                    else actualImage = MOUTH_OPENED;
                }
                int time = getNextRandomTimeForSpeaking(engine);
                timer.setNewTimer(time);
            }

        }

    }


    private void updateWaiting(PApplet engine) {
        if (timer == null){
            timer = new Timer(getNextRandomTimeForWaiting(engine));
        }
        else {
            if (timer.isTime()){
                if (actualImage == EYES_OPENED) actualImage = EYES_CLOSED;
                else actualImage = EYES_OPENED;
                int time  = getNextRandomTimeForWaiting(engine);
                timer = new Timer(time);
                //System.out.println("Time for image by waiting:   " + actualImage + " set on " + time);
            }
        }

    }

    public void draw(PGraphics graphics){
        frame.draw(graphics);
        ImageZoneSimpleData data = getActualImageData();
        //System.out.println("Actual face statement: " + faceStatement + " image: " + actualImage);
        //System.out.println("Try to draw: " + image.getPath() + " at " + frameCenter.x + " x " + frameCenter.y + " and data " + data.leftX + " x " + data.upperY + " to " + data.rightX + " x" + data.lowerY);
        graphics.image(image.getImage(),frameCenter.x, frameCenter.y, frame.getWidth()* PhotoDrawingController.SCALE_FOR_PHOTO, frame.getHeight()* PhotoDrawingController.SCALE_FOR_PHOTO,
                data.leftX, data.upperY, data.rightX, data.lowerY);


    }

    private ImageZoneSimpleData getActualImageData() {
        for (Map.Entry data :  images.entrySet()) {
            if ((int) data.getKey() == actualImage) {
                return (ImageZoneSimpleData) data.getValue();
            }
        }
        //System.out.println("Can not find");
        return null;

    }

    public void setFaceStatement(boolean faceStatement) {
        if (this.faceStatement != faceStatement) {
            this.faceStatement = faceStatement;
            int time  = TIME_TO_NEXT_EYES_OPENING_CIRCLE;
            timer = new Timer(time);
            actualImage = EYES_CLOSED;
        }
    }

    public int getFrameWidth() {
        return frame.getWidth();
    }

    public Vec2 getLeftUpper() {
        return frame.getLeftUpperCorner();
    }
}
