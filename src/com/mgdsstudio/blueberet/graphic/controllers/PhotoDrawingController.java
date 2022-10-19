package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.BeretColorChanger;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.PortraitPicture;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.IBeretColors;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import processing.core.PConstants;
import processing.core.PGraphics;

public class PhotoDrawingController {

    private EightPartsFrameImage frame;
    private Image graphicFile;
    private ImageZoneSimpleData mainData, secondaryData, thirdData;
    public final static float SCALE_FOR_PHOTO = 0.85f;
    private PortraitPicture portraitPicture;
    private Timer timer;
    private boolean mainPictureShown = true;
    private boolean withPictureChangeability;

    private final boolean TWO_PICTURES_CHANGEABILITY = true;
    public final boolean SUBSCRIBER_STOPS_TO_SPEAK = true;
    public final boolean THREE_PICTURES_CHANGEABILITY = false;
    private boolean changeabilityType = TWO_PICTURES_CHANGEABILITY;
    private SpeakingFaceController speakingFaceController;

    public PhotoDrawingController(EightPartsFrameImage frame, PortraitPicture subscriber) {
        this.frame = frame;
        if (BeretColorChanger.beretColorWasChanged){
            graphicFile = null;
            HUD_GraphicData.init();
            BeretColorChanger.beretColorWasChanged = false;
        }
        if (graphicFile == null) {
            if (HUD_GraphicData.mainGraphicFile!= null) {
                graphicFile = HUD_GraphicData.mainGraphicFile;
                recolorBeret();
                BeretColorChanger.beretColorWasChanged = false;
            }
        }
        init(subscriber);
        this.portraitPicture = subscriber;
        if (hasPhotoChangeability()){
            setTimer(false);
        }
        initChangeability();
    }

    private void initChangeability() {
        if (portraitPicture == PortraitPicture.PlAYER_FACE || portraitPicture == PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE || portraitPicture == PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE){
            changeabilityType = TWO_PICTURES_CHANGEABILITY;
            withPictureChangeability = true;
            //System.out.println("This subscriber has two pictures to be changed");
        }
        else  if (portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE || portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE || portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE){
            changeabilityType = THREE_PICTURES_CHANGEABILITY;
            speakingFaceController = new SpeakingFaceController();
            withPictureChangeability = true;
            //System.out.println("This subscriber has three pictures to be changed");
        }
        else withPictureChangeability = false;
    }

    private boolean hasPhotoChangeability(){
        if (portraitPicture == PortraitPicture.PlAYER_FACE || portraitPicture == PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE || portraitPicture == PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE){
            changeabilityType = TWO_PICTURES_CHANGEABILITY;
            return true;
        }
        else  if (portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE || portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE || portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE){
            changeabilityType = THREE_PICTURES_CHANGEABILITY;
            return true;
        }
        else return false;
    }

    private void recolorBeret() {
        BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(Program.engine);
        beretColorLoadingMaster.loadData();
        int bright = beretColorLoadingMaster.getBeretColor().getBrightColor();
        int dark = beretColorLoadingMaster.getBeretColor().getDarkColor();
        if (bright == IBeretColors.NORMAL_BERET_BRIGHT_COLOR || dark == IBeretColors.NORMAL_BERET_DARK_COLOR){
            //System.out.println("Graphic must not be redrawn. Color is " + beretColorName);
        }
        else {
            TwiceColor newColor = beretColorLoadingMaster.getBeretColor();
            BeretColorChanger colorChanger = new BeretColorChanger();
            colorChanger.changeColor(newColor, graphicFile);
        }
    }

    private void setTimer(boolean shortDuration){

        int TIME_TO_CHANGE_DATA = 2500;
        int RANDOM_TIME = 600;
        if (shortDuration){
            TIME_TO_CHANGE_DATA = 190;
            RANDOM_TIME = 40;
        }
        int delay = (int) Program.engine.random(TIME_TO_CHANGE_DATA-RANDOM_TIME, TIME_TO_CHANGE_DATA+RANDOM_TIME);
        if (timer == null) timer = new Timer(delay);
        else timer.setNewTimer(delay);
        System.out.println("Timer was set");
    }

    private void init(PortraitPicture subscriber){
        int smartphoneHeight = 57;
        int smartphoneWidth = 55;
        int startY = 29;
        int startX = 638;
        int endX = 683;
        if (subscriber == PortraitPicture.PlAYER_FACE) {
            mainData = HUD_GraphicData.playerFaceFullLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedFullLife;
            //thirdData = HUD_GraphicData.playerFaceFullLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.PLAYER_SPEAKING_FACE){
            mainData = HUD_GraphicData.playerFaceFullLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedFullLife;
            thirdData = HUD_GraphicData.playerFaceFullLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.BOSS_BOAR){
            mainData = HUD_GraphicData.boar;
            //secondaryData = HUD_GraphicData.boar;
            //thirdData = HUD_GraphicData.playerFaceFullLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.BOSS_BOAR_WITH_BLOOD){
            mainData = HUD_GraphicData.boarWithBlood;
            //secondaryData = HUD_GraphicData.boar;
            //thirdData = HUD_GraphicData.playerFaceFullLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE) {
            mainData = HUD_GraphicData.playerFaceHalfLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedHalfLife;
            //thirdData = HUD_GraphicData.playerFaceHalfLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE) {
            mainData = HUD_GraphicData.playerFaceHalfLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedHalfLife;
            thirdData = HUD_GraphicData.playerFaceHalfLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE) {
            mainData = HUD_GraphicData.playerFaceSmallLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedSmallLife;
            //thirdData = HUD_GraphicData.playerFaceSmallLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE) {
            mainData = HUD_GraphicData.playerFaceSmallLife;
            secondaryData = HUD_GraphicData.playerFaceEyesClosedSmallLife;
            thirdData = HUD_GraphicData.playerFaceSmallLifeMouthOpened;
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_UNREAD_MESSAGE) {
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
            startY = 85;
            secondaryData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_READ_MESSAGE) {
            startY = 85;
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
            secondaryData = mainData;
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_INCOMMING_CALL) {
            startY = 141;
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
            startY = 254;
            secondaryData = mainData;
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_BLOCKED_SCREEN) {
            startY = 197;
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_SUBSCRIBER) {
            startY = 254;
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
        }
        else if (subscriber == PortraitPicture.SMARTPHONE_WITH_BLACK_SCREEN) {
            startY = 258;
            mainData = new ImageZoneSimpleData(startX,startY, startX+smartphoneWidth, startY+smartphoneHeight);
        }
        else if (subscriber == PortraitPicture.NO_SUBSCRIBER) {
            mainData = HUD_GraphicData.blackButtonBackground;
        }
    }



    public  void draw(PGraphics graphics){
        ImageZoneSimpleData data = getActualData();
        if (graphics.imageMode == PConstants.CENTER) graphics.image(graphicFile.getImage(), frame.getLeftUpperCorner().x+frame.getWidth()/2, frame.getLeftUpperCorner().y+frame.getHeight()/2, frame.getWidth()* SCALE_FOR_PHOTO, frame.getHeight()* SCALE_FOR_PHOTO, data.leftX, data.upperY, data.rightX, data.lowerY);
        else graphics.image(graphicFile.getImage(), frame.getLeftUpperCorner().x, frame.getLeftUpperCorner().y, frame.getWidth()* SCALE_FOR_PHOTO, frame.getHeight()* SCALE_FOR_PHOTO, data.leftX, data.upperY, data.rightX, data.lowerY);
    }

    private ImageZoneSimpleData getActualData() {
        if (withPictureChangeability){
            ImageZoneSimpleData data;
            if (changeabilityType == TWO_PICTURES_CHANGEABILITY) data = getActualDataForTwoPicturesChangeability();
            else data = getActualDataForThreePicturesChangeability();
            return data;
        }
        else return mainData;
    }

    private ImageZoneSimpleData getActualDataForThreePicturesChangeability() {
        speakingFaceController.update(Program.engine);
        int actual = speakingFaceController.getActualImage();
        if (actual == SpeakingFaceController.EYES_OPENED) return mainData;
        else if (actual == SpeakingFaceController.EYES_CLOSED) return secondaryData;
        else  return thirdData;

        //return getActualDataForTwoPicturesChangeability();
    }



    private ImageZoneSimpleData getActualDataForTwoPicturesChangeability() {
        if (timer.isTime()){
            if (mainPictureShown){
                setTimer(true);
                mainPictureShown = false;
                return mainData;
            }
            else {
                setTimer(false);
                mainPictureShown = true;
                return secondaryData;
            }
        }
        else {
            if (mainPictureShown) return mainData;
            else return secondaryData;
        }
    }

    public void setPortraitPicture(PortraitPicture portraitPicture){
        init(portraitPicture);
    }

    public PortraitPicture getPortraitPicture() {
        return portraitPicture;
    }

    public void setChangeabilityType(boolean changeabilityType) {
        if (changeabilityType == TWO_PICTURES_CHANGEABILITY) {
            //System.out.println("Face stops to talk  ");
            this.changeabilityType = changeabilityType;
            //timer = new Timer();
            if (portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE) portraitPicture = PortraitPicture.PlAYER_FACE;
            else if (portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE) portraitPicture = PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE;
            else if (portraitPicture == PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE) portraitPicture = PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE;
            initChangeability();
        }
    }
}
