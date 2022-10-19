package com.mgdsstudio.blueberet.loading;


import com.mgdsstudio.blueberet.gamecontrollers.MoveablePlatformsController;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.PlantController;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import com.mgdsstudio.blueberet.zones.SingleFlagZone;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.RepeatingBackgroundElement;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.graphic.background.SingleColorBackground;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

public class GameObjectStringDataConstructor {
    private GameObject gameObject;
    private Bridge bridge;
    //private ObjectsClearingZone zone;
    private SingleFlagZone zone;
    private PipePortal portal;
    private Background background;
    private MoveablePlatformsController moveablePlatformsController;
    private String dataString;
    private static final int elementsNumber = 1;
    IndependentOnScreenStaticSprite spriteAnimation;
    private IndependentOnScreenGraphic independentOnScreenAnimation;
    private IndependentOnScreenPixel pixel;

    public GameObjectStringDataConstructor (GameObject gameObject){
        dataString = new String();
        this.gameObject = gameObject;
    }

    public GameObjectStringDataConstructor (Bridge bridge){
        dataString = new String();
        this.bridge = bridge;
    }

    public GameObjectStringDataConstructor (IndependentOnScreenPixel gameObject){
        dataString = new String();
        this.pixel = gameObject;
    }

    public GameObjectStringDataConstructor (Background background){
        dataString = new String();
        this.background = background;
    }

    public GameObjectStringDataConstructor (MoveablePlatformsController moveablePlatformsController){
        dataString = new String();
        this.moveablePlatformsController = moveablePlatformsController;
    }

    public GameObjectStringDataConstructor (PipePortal portal){
        dataString = new String();
        this.portal = portal;
    }

    /*
    public GameObjectStringDataConstructor (ObjectsClearingZone zone){
        dataString = new String();
        this.zone = zone;
    }*/

    public GameObjectStringDataConstructor (SingleFlagZone zone){
        dataString = new String();
        this.zone = zone;
    }

    public GameObjectStringDataConstructor (IndependentOnScreenStaticSprite graphic){
        dataString = new String();
        spriteAnimation = graphic;
    }

    public GameObjectStringDataConstructor (IndependentOnScreenGraphic graphic){
        dataString = new String();
        independentOnScreenAnimation = graphic;
    }


    public String getDataString() {
        return dataString;
    }


    /*
    public void createObjectsClearingZone() {
        addNameAndNumber(zone.getCLASS_NAME());
        addPosition(zone.getAbsolutePosition());
        addWidth(zone.getWidth());
        addHeight(zone.getHeight());
        addMission(zone.activatingCondition);
    }*/

    public void createSingleFlagZone() {
        addNameAndNumber(zone.getClassName());
        addPosition(zone.getAbsolutePosition());
        addWidth(zone.getWidth());
        addHeight(zone.getHeight());
        addMissionWithoutDataDivider(zone.activatingCondition);
    }

    public void createCameraFixationZone() {
        //int activatingCondition, int deactivatingCondition, boolean cameraScale, boolean repeateability
        addNameAndNumber(zone.getClassName());
        addPosition(zone.getAbsolutePosition());
        addWidth(zone.getWidth());
        addHeight(zone.getHeight());
        CameraFixationZone cameraFixationZone = (CameraFixationZone) zone;
        addSingleValue((int) cameraFixationZone.getConcentratingX());
        addSingleValue((int) cameraFixationZone.getConcentratingY());
        addActivatedBy((byte) cameraFixationZone.activatingCondition);
        addActivatedBy((byte) cameraFixationZone.getDeactivatingCondition());
        addSingleValue(cameraFixationZone.isCameraScale());
        addSingleValue(cameraFixationZone.isRepeateability());
        removeEndStringComma();
    }

    public void createEndLevelZone() {
        addNameAndNumber(zone.getClassName());
        addPosition(zone.getAbsolutePosition());
        addWidth(zone.getWidth());
        addHeight(zone.getHeight());
        if (zone instanceof EndLevelZone){
            EndLevelZone endLevelZone = (EndLevelZone) zone;
            addSingleValue(zone.activatingCondition);
            int completedLevel = endLevelZone.getCompletedLevel();
            if (completedLevel >= 0) {
                addSingleValue(completedLevel);
                addSingleValue(((EndLevelZone) zone).getNextZone());
                removeEndStringComma();
            }
            else {
                addMissionWithoutDataDivider(zone.activatingCondition);
            }

        }
        else{
            addMissionWithoutDataDivider(zone.activatingCondition);
        }
    }

    public void createObjectsClearingZone() {
        addNameAndNumber(zone.getClassName());
        addPosition(zone.getAbsolutePosition());
        addWidth(zone.getWidth());
        addHeight(zone.getHeight());
        addMissionWithoutDataDivider(zone.activatingCondition);
    }

    public void createPixel() {
        addNameAndNumber(pixel.getClassName());
        addSingleValue(pixel.getxPos());
        addSingleValue(pixel.getyPos());
        addSingleValue(pixel.getDim());
        addSingleValue(pixel.getColorX());
        addSingleValue(pixel.getColorX());
        addSingleValue(pixel.getAlpha());
        addSingleValue(pixel.getLayer());
        addMissionWithoutDataDivider( zone.activatingCondition);
    }

    //Bridge 1:33405,1075,34040,1125,13,34095,925,50,75#Tileset1.png;137x304x152x319x50x50x1
    //public Bridge(GameRound gameRound, PVector leftUpperCorner, PVector rightLowerCorner, int segmentsAlongX, Flag flag){
    public void createBridge(){
        if (bridge != null){
            addNameAndNumber(Bridge.CLASS_NAME);
            System.out.println("Bridge LU: " + bridge.getLeftUpperCorner() + "; RL: " + bridge.getRightLowerCorner() + "; Flag: " + bridge.getFlag().getPosition());
            addPosition(bridge.getLeftUpperCorner());
            addPosition(bridge.getRightLowerCorner());
            addSingleValue(bridge.getSegments().size());
            addPosition(bridge.getFlag().getPosition());
            addSingleValue((int) bridge.getFlag().getWidth());
            addSingleValue((int) bridge.getFlag().getHeight());
            endDataString();
            RoundElement bridgePart = bridge.getSegments().get(0);
            int fill = 0;   //mayby = 1
            //
            int [] graphicData = {bridgePart.getSprite().getxLeft(), bridgePart.getSprite().getyLeft(), bridgePart.getSprite().getxRight(), bridgePart.getSprite().getyRight(), bridgePart.getSprite().getParentElementWidth(), bridgePart.getSprite().getParentElementHeight(), fill};
            String path = bridgePart.getSprite().getPath();
            if (path.equals(StaticSprite.BLACK_RECT_PATH)){
                path = MainGraphicController.WITHOUT_GRAPHIC_STRING;
                System.out.println("Name was reset on " + path);
            }
            addGraphicDataIndependentGraphic(path, graphicData);
        }
        else System.out.println("Bridge is null and the text string can not be created");
        //!
    }

    //RoundCircle 1:8005,1095,25,99,0,2#Tileset5.png;252x73x268x89x50x50x0
    public void createRoundCircle() {
        if (gameObject.getClass() == RoundCircle.class) {
            addNameAndNumber(gameObject.getClassName());
        }
        addPosition(gameObject.getPixelPosition());
        //addAngle(gameObject.body.getAngle());
        addSingleValue((int) gameObject.getWidth()/2);
        addSingleValue( gameObject.getLife());

        //addWidth(gameObject.getWidth());
        //addHeight(gameObject.getHeight());
        //addLife(gameObject.getLife());
        RoundCircle roundElement = (RoundCircle) gameObject;
        addSpring(roundElement.hasSpring());
        addBodyType(gameObject.body.getType());
        int fill = 0;
        //if (roundElement.getSprite().isFillAreaWithSprite()) fill = 1;
        int [] graphicData = {roundElement.getSprite().getxLeft(),roundElement.getSprite().getyLeft(), roundElement.getSprite().getxRight(), roundElement.getSprite().getyRight(),roundElement.getSprite().getParentElementWidth(), roundElement.getSprite().getParentElementHeight(), fill};
        endDataString();
        String path = roundElement.getSprite().getPath();
        if (path.equals(StaticSprite.BLACK_RECT_PATH)){
            path = MainGraphicController.WITHOUT_GRAPHIC_STRING;
            System.out.println("Name was reset on " + path);
        }
        addGraphicDataIndependentGraphic(path, graphicData);
    }

    //
    //RoundPolygon 1:-150,200,-150,100,-100,200,0,9999,0,0#Tileset5.png;35x4x40x9x50x50
    public void createRoundPolygon(Class clazz){
        if (clazz == RoundPolygon.class){
            System.out.println("This is a class");
        }
        addNameAndNumber(gameObject.getClassName());
        RoundPolygon roundElement = (RoundPolygon)gameObject;
        addPosition(roundElement.getVerteciesAbsolutePosInPixel().get(0));
        addPosition(roundElement.getVerteciesAbsolutePosInPixel().get(1));
        addPosition(roundElement.getVerteciesAbsolutePosInPixel().get(2));
        addAngle(roundElement.body.getAngle());
        addSingleValue( gameObject.getLife());
        addSpring(roundElement.hasSpring());
        addBodyType(gameObject.body.getType());
        int fill = 0;
        int [] graphicData = {roundElement.getSprite().getxLeft(),roundElement.getSprite().getyLeft(), roundElement.getSprite().getxRight(), roundElement.getSprite().getyRight(),roundElement.getSprite().getParentElementWidth(), roundElement.getSprite().getParentElementHeight(), fill};
        endDataString();
        String path = roundElement.getSprite().getPath();
        if (path.equals(StaticSprite.BLACK_RECT_PATH)){
            path = MainGraphicController.WITHOUT_GRAPHIC_STRING;
            System.out.println("Name was reset on " + path);
        }
        addGraphicDataIndependentGraphic(path, graphicData);
    }

    public void createRoundBox() {
        if (gameObject.getClass() == RoundBox.class) addNameAndNumber(gameObject.getClassName());
        addPosition(gameObject.getPixelPosition());
        addAngle(gameObject.body.getAngle());
        addWidth(gameObject.getWidth());
        addHeight(gameObject.getHeight());
        addLife(gameObject.getLife());
        RoundBox roundElement = (RoundBox)gameObject;
        addSpring(roundElement.hasSpring());
        addBodyType(gameObject.body.getType());
        int fill = 0;
        if (roundElement.getSprite().isFillAreaWithSprite()) fill = 1;
        int [] graphicData = {roundElement.getSprite().getxLeft(),roundElement.getSprite().getyLeft(), roundElement.getSprite().getxRight(), roundElement.getSprite().getyRight(),roundElement.getSprite().getParentElementWidth(), roundElement.getSprite().getParentElementHeight(), fill};
        endDataString();
        String path = roundElement.getSprite().getPath();
        if (path.equals(StaticSprite.BLACK_RECT_PATH)){
            path = MainGraphicController.WITHOUT_GRAPHIC_STRING;
            System.out.println("Name was reset on " + path);
        }
        addGraphicDataIndependentGraphic(path, graphicData);
    }

    public void createBackground(){
        addNameAndNumber(background.CLASS_NAME);
        addType(background.getType());
        if (background.getClass() == SingleColorBackground.class){
            SingleColorBackground singleColorBackground = (SingleColorBackground) background;
            addSingleValue(singleColorBackground.getRed());
            addSingleValue(singleColorBackground.getGreen());
            System.out.println("Blue value: " + singleColorBackground.getBlue());
            addSingleValue(singleColorBackground.getBlue());
            /*
            if (dataString.charAt(dataString.length()-1) == SaveMaster.DIVIDER_BETWEEN_VALUES){
                dataString = dataString.substring(0, (dataString.length()-1));
            }*/
            endDataString();
        }
        else if (background.getClass() == ScrollableAlongXBackground.class){
            ScrollableAlongXBackground scrollableAlongXBackground = (ScrollableAlongXBackground) background;
            addWidth(scrollableAlongXBackground.getWidth());
            addHeight(scrollableAlongXBackground.getHeight());
            addSingleValue((int)scrollableAlongXBackground.getLeftUpperX());
            addSingleValue((int)scrollableAlongXBackground.getLeftUpperY());
            float relativeVelocity = scrollableAlongXBackground.getRelativeVelocity();
            if (relativeVelocity>100) relativeVelocity/=100;
            else if (relativeVelocity < 1) relativeVelocity*=100;
            addSingleValue((int)(relativeVelocity));
            //addSingleValue((int)(scrollableAlongXBackground.getRelativeVelocity()));
            int [] graphicData = {0,0, scrollableAlongXBackground.getWidth(), scrollableAlongXBackground.getHeight()};
            endDataString();
            addGraphicDataForScrollableAlongXBackground(background.getPath(), graphicData);
        }
        else if (background.getClass() == RepeatingBackgroundElement.class){
            RepeatingBackgroundElement repeatingBackgroundElement = (RepeatingBackgroundElement) background;
                       addSingleValue((int)repeatingBackgroundElement.getPos().x);
            addSingleValue((int)repeatingBackgroundElement.getPos().y);

            //System.out.println("Step is: " + repeatingBackgroundElement.getStep());
            addSingleValue(repeatingBackgroundElement.getStep());
            addSingleValue(0);
            addSingleValue(repeatingBackgroundElement.getAngle());

            float relativeVelocity = repeatingBackgroundElement.getRelativeVelocity();
            //System.out.println("relative velocity; " + relativeVelocity);
            if (relativeVelocity>100) relativeVelocity/=100;
            else if (relativeVelocity < 1) relativeVelocity*=100;
            addSingleValue((int)(relativeVelocity));

            StaticSprite staticSprite = repeatingBackgroundElement.getSprite();
            int [] graphicData = {staticSprite.getxLeft(),staticSprite.getyLeft(), staticSprite.getxRight(), staticSprite.getyRight(),staticSprite.getParentElementWidth(), staticSprite.getParentElementHeight(), 1};
            endDataString();
            addGraphicDataIndependentGraphic(staticSprite.getPath(), graphicData);
        }
    }

    private void addGraphicDataForScrollableAlongXBackground(String path, int [] graphicData)
    {
        dataString+=path;
        dataString+= SaveMaster.GRAPHIC_NAME_END_CHAR;
        dataString+=graphicData[0];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[1];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[2];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[3];
    }

    public void addSingleValue(int valueToBeAdded){
        dataString+=(int)(valueToBeAdded);
        addDeviderBetweenMainDataValues();
    }

    public void addSingleValue(boolean valueToBeAdded){
        int value = 0;
        if (valueToBeAdded == true) value = 1;
        dataString+=(int)(value);
        addDeviderBetweenMainDataValues();
    }

    public void createRoundPipe() {
        //RoundPipe 1:720,1090,130,220,270,2#Tileset2.png;400x600x600x800x130x220
        if (gameObject.getClass() == RoundPipe.class) {
            RoundPipe roundPipe = (RoundPipe) gameObject;
            addNameAndNumber(gameObject.getClassName());
            addPosition(roundPipe.getPixelPosition());
            //addPosition(roundPipe.getLeftUpperCorner());
            addWidth(gameObject.getWidth());
            addHeight(gameObject.getHeight());
            addAngleInDegrees(roundPipe.getAngle());
            //System.out.println("Angle for this pipe as dataString: " + );
            addFlowerBehaviour(roundPipe.getFlowerBehaviour());
            if (roundPipe.getFlowerBehaviour() != PlantController.NO_FLOWER) {
                addLife(roundPipe.getPlantController().getRodLife());
                addDiameter(roundPipe.getPlantController().getJawDiameter());
            }
            else {
                addLife(0);
                addDiameter(0);
            }
            int[] graphicData = {roundPipe.getSprite().getxLeft(), roundPipe.getSprite().getyLeft(), roundPipe.getSprite().getxRight(), roundPipe.getSprite().getyRight(), roundPipe.getSprite().getParentElementWidth(), roundPipe.getSprite().getParentElementHeight(), 0};
            endDataString();
            addGraphicDataIndependentGraphic(roundPipe.getSprite().getPath(), graphicData);
        }
    }
/*
    private void addGraphicDataForStaticSprite(String fileName, int [] graphicData){
        dataString+=fileName;
        dataString+= SaveMaster.GRAPHIC_NAME_END_CHAR;
        dataString+=graphicData[0];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[1];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[2];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[3];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[4];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[5];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[6];
    }*/

    private void addGraphicDataIndependentGraphic(String fileName, int [] graphicData){
        dataString+=fileName;
        dataString+= SaveMaster.GRAPHIC_NAME_END_CHAR;
        for (int i = 0; i < (graphicData.length-1); i++){
            dataString+=graphicData[i];
            addDeviderBetweenGraphicDataValues();
        }
        dataString+=graphicData[graphicData.length-1];

    }

    private void addGraphicDataForSpriteAnimationForCollectable(String fileName, int [] graphicData){
        dataString+=fileName;
        dataString+= SaveMaster.GRAPHIC_NAME_END_CHAR;
        for (int i = 0; i < (graphicData.length-1); i++){
            dataString+=graphicData[i];
            addDeviderBetweenGraphicDataValues();
        }
        dataString+=graphicData[graphicData.length-1];

    }

    private void addStartGraphicSymbol(){
        dataString+=SaveMaster.GRAPHIC_NAME_START_CHAR;
    }

    private void addDeviderBetweenGraphicDataValues(){
        dataString+=SaveMaster.DIVIDER_BETWEEN_GRAPHIC_DATA;
    }

    private void endDataString() {
        if (dataString.charAt(dataString.length()-1) == SaveMaster.DIVIDER_BETWEEN_VALUES){
            dataString = dataString.substring(0, (dataString.length()-1));
        }
        dataString+=SaveMaster.GRAPHIC_NAME_START_CHAR;
    }

    private void removeEndStringComma(){
        if (dataString.charAt(dataString.length()-1) == SaveMaster.DIVIDER_BETWEEN_VALUES){
            dataString = dataString.substring(0, (dataString.length()-1));
            System.out.println("Last comma was deleted");
        }
        else{
            System.out.println("There is no comma at the end of the string");
        }
    }

    private void addSpring(boolean withSpring){
        if (withSpring) dataString+=1;
        else dataString+=0;
        addDeviderBetweenMainDataValues();
    }



    private void addBodyType(BodyType bodyType){
        byte value = 0;
        if (bodyType == BodyType.STATIC) value = 0;
        else if (bodyType == BodyType.KINEMATIC) value = 1;
        else value = 2;
        dataString+=(value);
        addDeviderBetweenMainDataValues();
    }

    private void addFixationTypeForSimpleCollectable(int fixationType){
        dataString+=(fixationType);
        addDeviderBetweenMainDataValues();
    }

    private void addLife(int life){
        dataString+=(int)(life);
        addDeviderBetweenMainDataValues();
    }

    //for pipes
    private void addFlowerBehaviour(byte behaviour){
        dataString+=(behaviour);
        addDeviderBetweenMainDataValues();
    }

    private void addHeight(float height) {
        dataString+=(int)(height);
        addDeviderBetweenMainDataValues();
    }

    private void addWidth(float width) {
        dataString+=(int)(width);
        addDeviderBetweenMainDataValues();
    }

    private void addDiameter(float diameter) {
        dataString+=(int)(diameter);
        addDeviderBetweenMainDataValues();
    }

    private void addType(byte type){
        dataString+=type;
        addDeviderBetweenMainDataValues();
    }

    private void addMissionWithoutDataDivider(int mission){
        dataString+=mission;
    }

    private void addMission(byte mission){
        dataString+=mission;
        addDeviderBetweenMainDataValues();
    }

    private void addAngle(float angleInRadians) {
        dataString+=(int)(PApplet.degrees(angleInRadians));
        addDeviderBetweenMainDataValues();
    }

    private void addAngleInDegrees(int angleInDegrees) {
        dataString+=angleInDegrees;
        addDeviderBetweenMainDataValues();
    }

    private void addFlipWithEndMainDataDevider(boolean flip) {
        int flipValue = 0;
        if (flip == true)  flipValue = 1;
        dataString+=(flipValue);
    }



    private void addFlip(boolean flip) {
        int flipValue = 0;
        if (flip == true)  flipValue = 1;
        dataString+=(flipValue);
        addDeviderBetweenMainDataValues();
    }

    private void addFill(boolean fill) {
        int fillValue = 0;
        if (fill == true)  fillValue = 1;
        dataString+=(fillValue);
        addDeviderBetweenMainDataValues();
    }

    private void addLayerWithEndMainDataDevider(byte layer) {
        //int flipValue = 0;
        //if (flip == true)  flipValue = 1;
        dataString+=(layer);
        //addDeviderBetweenMainDataValues();
    }

    private void addPosition(Vec2 absolutePosition) {
        dataString+=(int)(absolutePosition.x);
        addDeviderBetweenMainDataValues();
        dataString+=(int)(absolutePosition.y);
        addDeviderBetweenMainDataValues();
    }

    private void addPosition(PVector absolutePosition) {
        dataString+=(int)(absolutePosition.x);
        addDeviderBetweenMainDataValues();
        dataString+=(int)(absolutePosition.y);
        addDeviderBetweenMainDataValues();
    }

    private void addDeviderBetweenMainDataValues(){
        dataString+=SaveMaster.DIVIDER_BETWEEN_VALUES;
    }

    private void addNameAndNumber(String type){
        dataString = new String();
        //if (type == ObjectsClearingZone.CLASS_NAME){
            dataString = type;
        //}
        //dataString = gameObject.getCLASS_NAME();
        dataString+=' ';
        dataString+=elementsNumber;
        dataString+=SaveMaster.MAIN_DATA_START_CHAR;
        //elementsNumber++;
    }

    public void createIndependentSpriteAnimation() {
        IndependentOnScreenAnimation animation = (IndependentOnScreenAnimation) independentOnScreenAnimation;
        addNameAndNumber(animation.CLASS_NAME);
        addPosition(new PVector(animation.getPosition().x, animation.getPosition().y));
        addAngleInDegrees((int) animation.getAngle());
        addFlip(animation.getFlip());
        addLayerWithEndMainDataDevider(animation.getLayer());
        String path = animation.getPath();
        int leftUpperX = animation.spriteAnimation.getxLeft();
        int leftUpperY = animation.spriteAnimation.getyLeft();
        int rightLowerX = animation.spriteAnimation.getxRight();
        int rightLowerY = animation.spriteAnimation.getyRight();
        int intFill = 0;
        /*boolean fill = this.spriteAnimation.getFill();
        int intFill = 1;
        if (fill == false) intFill = 0;*/
        int width = (int) animation.getWidth();
        int height = (int) animation.getHeight();
        int rows = animation.spriteAnimation.getRowsNumber();
        int collummns = animation.spriteAnimation.getColumnsNumber();
        int frequency = animation.spriteAnimation.getUpdateFrequency();
        int [] graphicData = {leftUpperX , leftUpperY, rightLowerX, rightLowerY, width, height, rows, collummns, frequency, intFill};
        endDataString();
        addGraphicDataIndependentGraphic(path, graphicData);
    }

    public void createIndependentStaticSprite() {
        addNameAndNumber(spriteAnimation.CLASS_NAME);
        addPosition(new PVector(spriteAnimation.getPosition().x, spriteAnimation.getPosition().y));
        addAngleInDegrees((int) spriteAnimation.getAngle());
        addFlip(spriteAnimation.getFlip());
        addLayerWithEndMainDataDevider(spriteAnimation.getLayer());
        String path = spriteAnimation.getPath();
        int leftUpperX = spriteAnimation.staticSprite.getxLeft();
        int leftUpperY = spriteAnimation.staticSprite.getyLeft();
        int rightLowerX = spriteAnimation.staticSprite.getxRight();
        int rightLowerY = spriteAnimation.staticSprite.getyRight();
        boolean fill = spriteAnimation.getFill();
        int intFill = 1;
        if (fill == false) intFill = 0;
        int width = (int) spriteAnimation.getWidth();
        int height = (int) spriteAnimation.getHeight();
        int [] graphicData = {leftUpperX , leftUpperY, rightLowerX, rightLowerY, width, height, intFill};
        endDataString();
        addGraphicDataIndependentGraphic(path, graphicData);
    }

    public  void createCollectableObject(){
        if (gameObject.getClass() == CollectableObjectInNesStyle.class){
            CollectableObjectInNesStyle collectableObject = (CollectableObjectInNesStyle) gameObject;
            addNameAndNumber(collectableObject.getClassName());

            if (collectableObject.getInWorldPosition()) {
                addPosition(collectableObject.getPixelPosition());
            }
            else {
                addPosition(new PVector(collectableObject.getParentPos().x, collectableObject.getParentPos().y));
            }
            //addAngle(gameObject.body.getAngle());
            addWidth(collectableObject.getWidth());

            addType(collectableObject.getType());
            addSpring(collectableObject.hasSpring());
            int [] graphicData = {collectableObject.getSpriteAnimation().getxLeft(), collectableObject.getSpriteAnimation().getyLeft(), collectableObject.getSpriteAnimation().getxRight(), collectableObject.getSpriteAnimation().getyRight(), collectableObject.getSpriteAnimation().getParentElementWidth(), collectableObject.getSpriteAnimation().getParentElementHeight(), collectableObject.getSpriteAnimation().getRowsNumber(), collectableObject.getSpriteAnimation().getColumnsNumber(), collectableObject.getSpriteAnimation().getUpdateFrequency()};
            endDataString();
            addGraphicDataForSpriteAnimationForCollectable(collectableObject.getSpriteAnimation().getPath(), graphicData);
        }
    }

    public void createSimpleCollectableElement(){
        if (gameObject instanceof SimpleCollectableElement){
            SimpleCollectableElement simpleCollectableElement = null;
            if (gameObject.getClass() == Money.class){
                simpleCollectableElement = (Money) gameObject;
            }
            else if (gameObject.getClass() == WeaponMagazine.class){
                simpleCollectableElement = (WeaponMagazine) gameObject;
            }
            else if (gameObject.getClass() == MedicalKit.class) {
                simpleCollectableElement = (MedicalKit) gameObject;
            }
            else if (gameObject.getClass() == Syringe.class) {
                simpleCollectableElement = (Syringe) gameObject;
            }
            else if (gameObject.getClass() == Fruit.class) {
                simpleCollectableElement = (Fruit) gameObject;
            }
            createSimpleCollectable(simpleCollectableElement);
            /*

            if (gameObject.getClass() == Money.class){
                createMoney();
            }
            else if (gameObject.getClass() == WeaponMagazine.class){
                createWeaponMagazine();
            }
            else if (gameObject.getClass() == MedicalKit.class){
                createMedicalKit();
            }*/

        }
    }

    private void createSimpleCollectable(SimpleCollectableElement collectableObject){
        addNameAndNumber(SimpleCollectableElement.CLASS_NAME);
        PVector absPos = collectableObject.getPixelPosition();
        if (collectableObject.getInWorldPosition() == AbstractCollectable.IN_WORLD || absPos == null) {
            if (absPos == null) {
                absPos = new PVector(collectableObject.getParentPos().x, collectableObject.getParentPos().y);
                collectableObject.setInWorldPosition(AbstractCollectable.IN_BAG);
            }
        }
        addPosition(absPos);
        addSingleValue(100);    //Not known why
        addType(collectableObject.getType());
        addSingleValue(collectableObject.getValueToBeAddedByGain());
        addFixationTypeForSimpleCollectable(collectableObject.getFixationType());
        removeEndStringComma();
    }


    /*
    private void createMedicalKit(){
        SimpleCollectableElement collectableObject = (MedicalKit) gameObject;
        addNameAndNumber(SimpleCollectableElement.CLASS_NAME);
        if (collectableObject.getInWorldPosition()) {
            addPosition(collectableObject.getAbsolutePosition());
        }
        else {
            addPosition(collectableObject.getAbsolutePosition());
        }
        addSingleValue(100);    //Not known why
        addType(collectableObject.getType());
        addSingleValue(collectableObject.getValueToBeAddedByGain());
        addFixationTypeForSimpleCollectable(collectableObject.getFixationType());
        removeEndStringComma();
    }

    private void createWeaponMagazine(){
        SimpleCollectableElement collectableObject = (WeaponMagazine) gameObject;
        addNameAndNumber(SimpleCollectableElement.CLASS_NAME);
        if (collectableObject.getInWorldPosition()) {
            addPosition(collectableObject.getAbsolutePosition());
        }
        else {
            addPosition(collectableObject.getAbsolutePosition());
        }
        addSingleValue(100);    //Not known why
        addType(collectableObject.getType());
        addSingleValue(collectableObject.getValueToBeAddedByGain());
        addFixationTypeForSimpleCollectable(collectableObject.getFixationType());
        removeEndStringComma();
    }

    private void createMoney(){
        SimpleCollectableElement collectableObject = (Money) gameObject;
        addNameAndNumber(SimpleCollectableElement.CLASS_NAME);
        if (collectableObject.getInWorldPosition()) {
            addPosition(collectableObject.getAbsolutePosition());
        }
        else {
            addPosition(collectableObject.getAbsolutePosition());
            //addPosition(collectableObject.getParentPos());
        }
        //addWidth(collectableObject.getWidth());
        addSingleValue(100);    //Not known why
        addType(collectableObject.getType());
        addSingleValue(collectableObject.getValueToBeAddedByGain());
        //addSpring(collectableObject.hasSpring());
        addFixationTypeForSimpleCollectable(collectableObject.getFixationType());
        removeEndStringComma();

    }
*/

    public void createPortalSystem() {
        //Portal 1:1000,900,70,120,2,900,-20,70,120,4,1,1,0
        addNameAndNumber(portal.getClassName());
        addPosition(portal.enter.getPosition());
        addWidth(portal.enter.getWidth());
        addHeight(portal.enter.getHeight());
        addDirection(portal.enter.getDirection());

        addPosition(portal.exit.getPosition());
        addWidth(portal.exit.getWidth());
        addHeight(portal.exit.getHeight());
        addDirection(portal.exit.getDirection());
        addActivatedBy(portal.getActivatedBy());
        addTransferDirection(portal.getTransferDirection());
        addUsingRepeatabilityWithoutDivider(portal.getUsingRepeateability());
        //addDirectionWithoutDivider(portal.enter.getDirection());
    }

    private void addUsingRepeatabilityWithoutDivider(boolean usingRepeateability) {
        if (usingRepeateability == true) dataString += 1;
        else dataString += 0;
    }

    private void addTransferDirection(boolean transferDirection) {
        if (transferDirection == true) dataString += 1;
        else dataString += 0;
        addDeviderBetweenMainDataValues();
    }

    private void addActivatedBy(byte activatedBy) {
        dataString += activatedBy;
        addDeviderBetweenMainDataValues();
    }

    private void addDirection(byte direction) {
        dataString += direction;
        addDeviderBetweenMainDataValues();
    }

    private void addDirectionWithoutDivider(byte direction) {
        dataString += direction;
    }

    public void createMoveablePlatrofrmController() {
        addNameAndNumber(moveablePlatformsController.getClassName());
        addPosition(moveablePlatformsController.enter.getPosition());
        addWidth(moveablePlatformsController.enter.getWidth());
        addHeight(moveablePlatformsController.enter.getHeight());

        addPosition(moveablePlatformsController.exit.getPosition());
        addWidth(moveablePlatformsController.exit.getWidth());
        addHeight(moveablePlatformsController.exit.getHeight());

        addVelocity(moveablePlatformsController.getVelocity());
        addPlatformWidth(moveablePlatformsController.getPlatformWidth());
        addPlatformThickness(moveablePlatformsController.getPlatformThickness());
        addPlatformsNumber(moveablePlatformsController.getPlatformsNumber());
        System.out.println("Platform width: " + moveablePlatformsController.getPlatformWidth() + "x" + moveablePlatformsController.getPlatformThickness());
        addMovementParameter(moveablePlatformsController.getUsingRepeatability());
        int fill = 0;
        if (moveablePlatformsController.getPlatforms().get(0).getSprite().isFillAreaWithSprite()) fill = 1;
        int [] graphicData = {moveablePlatformsController.getPlatforms().get(0).getSprite().getxLeft(), moveablePlatformsController.getPlatforms().get(0).getSprite().getyLeft(), moveablePlatformsController.getPlatforms().get(0).getSprite().getxRight(), moveablePlatformsController.getPlatforms().get(0).getSprite().getyRight(),moveablePlatformsController.getPlatforms().get(0).getSprite().getParentElementWidth(), moveablePlatformsController.getPlatforms().get(0).getSprite().getParentElementHeight(), fill};
        endDataString();
        addGraphicDataIndependentGraphic(moveablePlatformsController.getPlatforms().get(0).getSprite().getPath(), graphicData);
    }

    private void addMovementParameter(boolean movementParameter) {
        if (movementParameter) dataString += 1;
        else dataString+= 0;
    }

    private void addPlatformsNumber(int platformsNumber) {
        dataString += platformsNumber;
        addDeviderBetweenMainDataValues();
    }

    private void addPlatformThickness(int platformThickness) {
        dataString += platformThickness;
        addDeviderBetweenMainDataValues();
    }

    private void addPlatformWidth(int platformWidth) {
        dataString += platformWidth;
        addDeviderBetweenMainDataValues();
    }

    private void addVelocity(int velocity) {
        dataString += velocity;
        addDeviderBetweenMainDataValues();
    }



}
