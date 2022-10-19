package com.mgdsstudio.blueberet.graphic.howtoplayanimation;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.gameprocess.control.FivePartsStick;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl.*;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithData;
import com.mgdsstudio.blueberet.menusystem.menu.HowToPlaySubmenus;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class HowToPlayScreenGraphic implements HowToPlaySubmenus, IBehaviours {
    //private ArrayList<Animation> animations;

    //private Animation animation;
    private ImageWithData controlAreaImage;
    private final int timeBetweenOperations = 1000;
    private PApplet engine;
    private int page;
    private HandMovementController controller;
    //private Rectangular zone;
    private Rectangular controlZone, animationZone;
    private boolean debug = true;
    private AbstractGirlAnimation girlAnimation;
    private boolean flip;
    private SoundInGameController soundInGameController;

    public HowToPlayScreenGraphic(PApplet engine, int screenNumber, Rectangular zone){
        this.engine = engine;
        this.page = screenNumber;
        soundInGameController = new SoundInGameController(2);
        calculateZonesDimensions(zone);
        init();

    }

    private void calculateZonesDimensions(Rectangular zone) {
        controlZone = new Rectangular(zone.getCenterX()- zone.getWidth()/4, zone.getCenterY(), zone.getWidth()/2, zone.getHeight());
        animationZone = new Rectangular(zone.getCenterX()+ zone.getWidth()/4, zone.getCenterY(), zone.getWidth()/2, zone.getHeight());
    }

    private void init(){
        initControlArea();
        initHand();
        initAnimation(engine);
        //soundInGameController = new SoundInGameController(2);
        //initAnimation(engine);
    }

    private void initAnimation(PApplet engine) {
        //girlAnimation = new RunAnimation((int)animationZone.getLeftUpperX(), (int)animationZone.getLeftUpperY());
        controller.clearGraphic();
        if (page == RUN) girlAnimation = new RunAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == GO) girlAnimation = new GoAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == SHOOT) girlAnimation = new ShootAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == RELOAD) girlAnimation = new ReloadAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == KICK) girlAnimation = new KickAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == PORTAL) girlAnimation = new PortalAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == JUMP) girlAnimation = new JumpAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        else if (page == AIM) girlAnimation = new WeaponAngleChangingAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), timeBetweenOperations, soundInGameController);
        else if (page == OPEN_ITEMS_LIST) girlAnimation = new OpenItemsListAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), controlZone, soundInGameController);
        else if (page == OPEN_WEAPONS_LIST) girlAnimation = new OpenWeaponsListAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), controlZone, soundInGameController);
        else if (page == CHANGE_CELL) girlAnimation = new OpenSubcellAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), controlZone, soundInGameController);

        else if (page == USE_OBJECT) girlAnimation = new UseItemAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), controlZone, soundInGameController);
        else if (page == GOOD_LUCK) girlAnimation = new PortalAnimation((int) (((int)animationZone.getCenterX()+controlZone.getCenterX())/2), (int)animationZone.getCenterY(),soundInGameController);

        else girlAnimation = new GoAnimation((int)animationZone.getCenterX(), (int)animationZone.getCenterY(), soundInGameController);
        controller.addGraphic(girlAnimation, controlAreaImage);
    }

    private void initHand() {
        createHandMovementControllerForPage(page);
    }

    private final float getStickScale(ImageZoneSimpleData imageZoneSimpleData, float normalHeight){
        float height = imageZoneSimpleData.lowerY-imageZoneSimpleData.upperY;
        //float normalHeight = FivePartsStick.NORMAL_STICK_HEIGHT;
        float coef = normalHeight/height;
        System.out.println("Scale coef for stick: " + coef);
        return coef;
    }

    private void initControlArea(){
        if (controlAreaImage == null) {
            if (HUD_GraphicData.mainGraphicFile == null){
                HUD_GraphicData.init();
            }
        }
        ImageZoneSimpleData imageZoneSimpleData  = null;
        float scale = 1f;
        if (page == AIM){
            imageZoneSimpleData =  FivePartsStick.centralZonePictureData;
            scale = getStickScale(imageZoneSimpleData, FivePartsStick.NORMAL_STICK_HEIGHT);
        }
        else if (page == GO) {
            imageZoneSimpleData =  FivePartsStick.leftGoZonePictureData;
            scale = getStickScale(imageZoneSimpleData, FivePartsStick.NORMAL_STICK_HEIGHT);
        }
        else if (page == RUN) {
            imageZoneSimpleData =  FivePartsStick.leftRunZonePictureData;
            scale = getStickScale(imageZoneSimpleData, FivePartsStick.NORMAL_STICK_HEIGHT);
            //scale = -1f;
        }
        else if (page == JUMP) {
            imageZoneSimpleData =  HUD_GraphicData.jumpButton;
            scale = getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        else if (page == SHOOT) {
            imageZoneSimpleData =  HUD_GraphicData.shotButton;
            scale = getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        else if (page == RELOAD) {
            imageZoneSimpleData =  HUD_GraphicData.reloadButton;
            scale = getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        else if (page == KICK) {
            imageZoneSimpleData =  HUD_GraphicData.kickButton;
            scale = getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        else if (page == PORTAL) {
            imageZoneSimpleData =  HUD_GraphicData.toPortalButton;
            scale = getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        else if (page == USE_OBJECT || page == OPEN_ITEMS_LIST || page == OPEN_WEAPONS_LIST || page == CHANGE_CELL) {
            // imageZoneSimpleData =  HUD_GraphicData.basicRectForSimpleFrames;
            imageZoneSimpleData =  null;
            scale = getScaleForFrames();
        }
        else if (page == GOOD_LUCK) {
            imageZoneSimpleData =  HUD_GraphicData.blackColor;
        }
        else {
            System.out.println("For this page there are no data about control area");
            imageZoneSimpleData = FivePartsStick.leftRunZonePictureData;
            scale = 3f*getStickScale(imageZoneSimpleData, OnScreenButton.normalDimention*2f);
        }
        controlAreaImage = new ImageWithData(HUD_GraphicData.mainGraphicFile, imageZoneSimpleData, scale);
    }

    private float getScaleForFrames() {
        float width = UpperPanel.WIDTH_FOR_WEAPON_FRAME;
        float sourceWidth = HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners.rightX-HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners.leftX;
        float scale = width/sourceWidth;
        System.out.println("Scale for frames: " + scale);
        //scale = 1f;
        return scale;

    }

    private void createHandMovementControllerForPage(int page){
        Coordinate start;
        Coordinate end;
        int behaviour;
        boolean handSide = TouchingHand.LEFT;
        if (page == AIM) {
            start = new Coordinate(0, -controlAreaImage.getHeight()*0.45f);
            end = new Coordinate(0, +controlAreaImage.getHeight()*0.45f);
            behaviour = PRESS_SLIDE_BACK_HIDE;
            handSide = TouchingHand.LEFT;
        }
        else if (page == GO || page == RUN || page == JUMP || page == SHOOT || page == RELOAD || page == KICK || page == PORTAL) {
            start = new Coordinate(0, 0);
            end = new Coordinate(0, 0);
            behaviour = PRESS_AND_BACK;
            if (page == GO || page == RUN){
                handSide = TouchingHand.LEFT;
            }
            else handSide = !TouchingHand.LEFT;
        }
        else if (page == GOOD_LUCK) {
            start = new Coordinate(0, 0);
            end = new Coordinate(0, 0);
            behaviour = DO_NOT_SHOW;
        }
        else if (page == USE_OBJECT){
            start = new Coordinate(0, 0);
            end = new Coordinate(0, +0);
            System.out.println("There are no data about hand movement controller");
            behaviour = WAIT_SHORT_PRESS_BACK;
            handSide = !TouchingHand.LEFT;
        }
        else {
            start = new Coordinate(0, 0);
            end = new Coordinate(0, +0);
            System.out.println("There are no data about hand movement controller");
            behaviour = PRESS_AND_BACK;
            handSide = !TouchingHand.LEFT;
        }
        controller = new HandMovementController(engine, behaviour, timeBetweenOperations, start, end, handSide);

    }

    public void setNewPage(int actualPage) {
        this.page = actualPage;
        init();
    }

    public void update(){
        controller.update();
        if (girlAnimation != null) girlAnimation.update();
    }

    public void draw(PGraphics graphics){
        if (debug){

        }
        drawGraphic(graphics);
    }

    private void drawGraphic(PGraphics graphics) {
        if (controlAreaImage != null){
            graphics.pushStyle();
            girlAnimation.draw(graphics);
            graphics.translate(controlZone.getCenterX(), controlZone.getCenterY());
            if (controlAreaImage.hasImage()) controlAreaImage.draw(graphics);
            controller.draw(graphics);
            graphics.translate(-controlZone.getCenterX(), -controlZone.getCenterY());
            graphics.popStyle();
        }
    }

    private void drawDebugZones(PGraphics graphics) {
        graphics.pushStyle();
        graphics.strokeWeight(graphics.width/75);
        graphics.stroke(255);
        graphics.rectMode(PConstants.CENTER);
        graphics.noFill();
        graphics.rect(controlZone.getCenterX(), controlZone.getCenterY(), controlZone.getWidth(), controlZone.getHeight(), graphics.width/75);
        graphics.rect(animationZone.getCenterX(), animationZone.getCenterY(), animationZone.getWidth(), animationZone.getHeight(), graphics.width/75);
        graphics.popStyle();
    }
}
