package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gamecontrollers.*;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalToAnotherLevel;
import com.mgdsstudio.blueberet.gameobjects.portals.SimplePortal;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.MusicInGameController;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.PortraitPicture;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMSController;
import com.mgdsstudio.blueberet.graphic.background.*;
import com.mgdsstudio.blueberet.graphic.textes.OnDisplayText;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.blueberet.zones.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;
import processing.data.IntList;

import java.util.ArrayList;

public class LoadingMaster extends ExternalRoundDataFileController {

    private boolean loadingType = LOADING_WITH_JAVA_STRING_DECODER;
    static final char END_ROW_SYMBOL = '!';
    final static private String collectableObjectClassName = "CollectableObject";
    //private final GameRound gameRound;

    /*
    private final static String collectableObjectTypeCoin = "Coin";
    private final static String collectableObjectTypeStar = "Star";
    private final static String collectableObjectTypeMushroom = "Mushroom";
    */

    public LoadingMaster(int fileNumber, boolean levelType) {
        super(fileNumber, levelType, ExternalRoundDataFileController.FIND_FILE_IN_CACHE);
        boolean canNotLoadWithThisLoader = false;
        if (loadingType == LOADING_WITH_JAVA_STRING_DECODER) {
            canNotLoadWithThisLoader = loadingFileDataInJavaMode();
        }
        if (loadingType == LOADING_WITH_PROCESSING_STRING_DECODER || canNotLoadWithThisLoader == true || fileData == null) {
            loadingFileDataInProcessingMode();
        }
        if (path == null) System.out.println("Path to round file: " + fileNumber + " is null");
        else System.out.println("Path to round file: " + fileNumber + " is " + path);
    }

    LoadingMaster(){

    }

    public String[] getFileData() {
        return fileData;
    }

    private void deleteShortStrings(int criticalLength) {
        String[] reserveStrings = new String[fileData.length];

        int actualStringNumber = 0;
        for (int i = 0; i < fileData.length; i++) {
            if (fileData[i].length() > criticalLength) {
                reserveStrings[actualStringNumber] = fileData[i];
                actualStringNumber++;
            }
        }
        if (actualStringNumber < fileData.length) {
            System.out.println((fileData.length - actualStringNumber) + " clear strings were deleted");
        } else System.out.println(" All strings are data strings");
        fileData = null;
        fileData = new String[actualStringNumber + 1];
        for (int i = 0; i < actualStringNumber; i++) {
            fileData[i] = reserveStrings[i];
        }

    }

    private void printArrayElements(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println("Element " + i + " is: " + array[i]);
        }
    }

    public ArrayList<RoundRotatingStick> getRoundRotatingSticks() {
        ArrayList<RoundRotatingStick> rotatingSticks = new ArrayList<RoundRotatingStick>();
        String[] textData = getTextDataForType(fileData, "RoundRotatingStick");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // was graphic name start char
            RoundRotatingStick rotatingStick = createRoundRotatingStick(values);
            rotatingSticks.add(rotatingStick);
        }
        return rotatingSticks;
    }

    private RoundRotatingStick createRoundRotatingStick(int[] values) {
        PVector center = new PVector(values[0], values[1]);
        Body staticBody = PhysicGameWorld.getBodyAtPoint(center);
        float length = values[2];
        float thickness = values[3];
        float torque = values[4];
        float angularVelocity = values[4] / 100.00f;
        RoundRotatingStick roundRotatingStick = new RoundRotatingStick(staticBody, center, length, thickness, torque, angularVelocity);
        roundRotatingStick.loadImageData();
        return roundRotatingStick;
    }

    public ArrayList<IndependentOnScreenStaticSprite> getIndependentOnScreenStaticSprites() {
        ArrayList<IndependentOnScreenStaticSprite> independentOnScreenSprites = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, "IndependentOnScreenStaticSprite");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            IndependentOnScreenStaticSprite IndependentOnScreenStaticSprite = createIndependentOnScreenStaticSprite(values, pathToTexture, graphicData);
            independentOnScreenSprites.add(IndependentOnScreenStaticSprite);
        }
        return independentOnScreenSprites;
    }

    public ArrayList<IndependentOnScreenPixel> getIndependentOnScreenPixels() {
        ArrayList<IndependentOnScreenPixel> pixels = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, IndependentOnScreenPixel.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // Was graphic name start char
            IndependentOnScreenPixel pixel = createIndependentOnScreenPixel(values);
            pixels.add(pixel);
        }
        return pixels;
    }

    public ArrayList<IndependentOnScreenAnimation> getIndependentOnScreenAnimations() {
        ArrayList<IndependentOnScreenAnimation> independentOnScreenAnimations = new ArrayList<IndependentOnScreenAnimation>();
        String[] textData = getTextDataForType(fileData, IndependentOnScreenAnimation.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            IndependentOnScreenAnimation independentOnScreenAnimation = createIndependentOnScreenAnimation(values, pathToTexture, graphicData);
            independentOnScreenAnimations.add(independentOnScreenAnimation);
        }
        return independentOnScreenAnimations;

    }

    public ArrayList<JumpingLavaBallsController> getJumpingLavaBallsControllers() {
        ArrayList<JumpingLavaBallsController> jumpingLavaBallsControllers = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, "JumpingLavaBallsController");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);

            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
            JumpingLavaBallsController jumpingLavaBallsController = createJumpingLavaBallsController(values);
            jumpingLavaBallsControllers.add(jumpingLavaBallsController);
        }
        return jumpingLavaBallsControllers;
    }

    private JumpingLavaBallsController createJumpingLavaBallsController(int[] values) {
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.OBJECTS_APPEARING_ZONE);
        Vec2 velocity = new Vec2(values[4], values[5]);
        int normalTimeBetweenIterations = values[6];
        System.out.println("lava frequencY " + values[5] + " and " + values[6]);
        JumpingLavaBallsController jumpingLavaBallsController = new JumpingLavaBallsController(flag, velocity, normalTimeBetweenIterations);
        return jumpingLavaBallsController;
    }

    private IndependentOnScreenAnimation createIndependentOnScreenAnimation(int[] values, String pathToTexture, int[] graphicData) {
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
        Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        byte rowsNumber = (byte) graphicData[6];
        byte columnsNumber = (byte) graphicData[7];
        int updateFrequency = (int) graphicData[8];
        SpriteAnimation spriteAnimation = new SpriteAnimation(pathToTexture, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, rowsNumber, columnsNumber, updateFrequency);
        int posX = values[0];
        int posY = values[1];
        float angleInDegrees = values[2];
        boolean flip = false;
        if (values[3] != 0) flip = true;
        byte layer = 0;
        if (values.length>4) layer = (byte) values[4];
        /*
        IndependentOnScreenAnimation 1:4340,500,0,0,1#App//src//main//Assets//Tileset5.png;229x261x261x269x42x40x1x4x8x0
IndependentOnScreenAnimation 1:4380,500,0,0,1#App//src//main//Assets//Tileset5.png;229x261x261x269x42x40x1x4x8x0x1
IndependentOnScreenAnimation 1:4420,500,0,0,1#App//src//main//Assets//Tileset5.png;229x261x261x269x42x40x1x4x8x0x2
IndependentOnScreenAnimation 1:4460,500,0,0,1#App//src//main//Assets//Tileset5.png;229x261x261x269x42x40x1x4x8x0x3
IndependentOnScreenAnimation 1:4340,540,0,0,1#App//src//main//Assets//Tileset5.png;229x269x261x276x40x40x1x3x8x0
IndependentOnScreenAnimation 1:4380,540,0,0,1#App//src//main//Assets//Tileset5.png;229x269x261x276x40x40x1x3x8x0x1
IndependentOnScreenAnimation 1:4420,540,0,0,1#App//src//main//Assets//Tileset5.png;229x269x261x276x40x40x1x3x8x0x2
IndependentOnScreenAnimation 1:4460,540,0,0,1#App//src//main//Assets//Tileset5.png;229x269x261x276x40x40x1x3x8x0x3

         */

        IndependentOnScreenAnimation independentOnScreenAnimation = new IndependentOnScreenAnimation(spriteAnimation, new Vec2(posX, posY), angleInDegrees, flip, layer);
        if (graphicData.length>10) {
            int startSprite = graphicData[10];
            if (startSprite<=((rowsNumber* columnsNumber)-1)) {
                spriteAnimation.setActualSpriteNumber((byte) startSprite);
                if (Program.debug) System.out.println("Start sprite for anim: " + (byte)startSprite);
            }

        }
        return independentOnScreenAnimation;
    }


    private IndependentOnScreenStaticSprite createIndependentOnScreenStaticSprite(int[] values, String pathToTexture, int[] graphicData) {
        IndependentOnScreenStaticSprite independentOnScreenStaticSprite = null;
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
        Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        boolean fill = false;
        if (graphicData.length>6) {
            if (graphicData[6] != 0) {
                fill = true;
            }
        }
        StaticSprite staticSprite = new StaticSprite(pathToTexture, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, fill);
        int posX = values[0];
        int posY = values[1];
        float angleInDegrees = values[2];
        boolean flip = false;
        if (values[3] != 0) flip = true;
        if (values.length>4){
            byte layer = (byte)values[4];
            independentOnScreenStaticSprite = new IndependentOnScreenStaticSprite(staticSprite, new Vec2(posX, posY), angleInDegrees, flip, layer);
        }
        else
            independentOnScreenStaticSprite = new IndependentOnScreenStaticSprite(staticSprite, new Vec2(posX, posY), angleInDegrees, flip);

        return independentOnScreenStaticSprite;
    }

    private IndependentOnScreenPixel createIndependentOnScreenPixel(int[] values) {
        //IndependentOnScreenPixel 1:xPos, yPox, width, colorX, colorY,alpha
        int xPos = values[0];
        int yPos = values[1];
        int width = values[2];
        int colorX = values[3];
        int colorY = values[4];
        int alpha = values[5];
        int layer = values[6];
        if (layer < ILayerable.BEHIND_ALL) layer = ILayerable.BEHIND_ALL;
        else if (layer > ILayerable.IN_FRONT_OF_ALL) layer = ILayerable.IN_FRONT_OF_ALL;
        IndependentOnScreenPixel pixel = new IndependentOnScreenPixel(xPos, yPos, width, colorX, colorY, alpha, layer);
        return pixel;
    }

    private Person getSoldier(String data, GameRound gameRound) {
        Person soldier;
        LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(data);
        int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
        System.out.println("Soldier values: ");
        for (int i = 0; i < values.length; i++){
            System.out.println(values[i] + "x");
        }
        soldier = createPerson(values, soldierType, gameRound);
        return soldier;
    }

    public ArrayList<Person> getPersons(String[] toDecodeData, GameRound gameRound) {
        ArrayList<Person> persons = new ArrayList<>();
        boolean dataFromFile = false;
        if (toDecodeData == null) dataFromFile = true;
        String[] textData;
        int id = 1;
        if (dataFromFile) {
            textData = getTextDataForType(fileData, soldierType);  // if (dataFromFile)
            for (int i = 0; i < textData.length; i++) {
                persons.add(getSoldier(textData[i], gameRound));
            }
            textData = getTextDataForType(fileData, Gumba.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person gumba = createPerson(values, gumbaType, gameRound);
                    persons.add(gumba);
                    gumba.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, Koopa.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person koopa = createPerson(values, koopaType, gameRound);
                    persons.add(koopa);
                    koopa.setUniqueId(id);
                }

            }
            textData = getTextDataForType(fileData, Bowser.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person bowser = createPerson(values, Bowser.CLASS_NAME, gameRound);
                    persons.add(bowser);
                    bowser.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, Spider.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person spider = createPerson(values, Spider.CLASS_NAME, gameRound);
                    spider.setUniqueId(id);
                    persons.add(spider);
                }
                else System.out.println("This spider was already killed");
            }
            textData = getTextDataForType(fileData, Mercenary.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person spider = createPerson(values, Mercenary.CLASS_NAME, gameRound);
                    spider.setUniqueId(id);
                    persons.add(spider);
                }
                else System.out.println("This spider was already killed");
            }
            textData = getTextDataForType(fileData, Snake.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person snake = createPerson(values, Snake.CLASS_NAME, gameRound);
                    persons.add(snake);
                    snake.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, Lizard.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person lizard = createPerson(values, Lizard.CLASS_NAME, gameRound);
                    persons.add(lizard);
                    lizard.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, Bat.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person bat = createPerson(values, Bat.CLASS_NAME, gameRound);
                    persons.add(bat);
                    bat.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, BossBoar.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person boar = createPerson(values, BossBoar.CLASS_NAME, gameRound);
                    persons.add(boar);
                    boar.setUniqueId(id);
                }
            }
            textData = getTextDataForType(fileData, Dragonfly.CLASS_NAME);
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                id = levelDataStringDecoder.getId();
                if (canObjectBeAdded(id)) {
                    int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                    Person dragonfly = createPerson(values, Dragonfly.CLASS_NAME, gameRound);
                    persons.add(dragonfly);
                    dragonfly.setUniqueId(id);
                }
            }

        } else {
            textData = new String[1];
            textData = getTextDataForType(toDecodeData, gumbaType);
            System.out.print("Try to get Gumba data; ");
            System.out.println(textData[0]);
            //System.out.println(" Strings: " + textData.length + " from string " + textData[0]);

            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
                Person gumba = createPerson(values, gumbaType, gameRound);
                persons.add(gumba);
            }

        }


        return persons;
    }

    private Person createPerson(int[] values, String personType, GameRound gameRound) {
        Person person;
        //System.out.println("Person " + personType + " must be created");
        if (personType == soldierType) {
            PVector position = new PVector(values[0], values[1]);
            person = new Soldier(position, gameRound);
            System.out.println("Soldier on : " + values[0] + " x " + values[1]);
        }
        else if (personType == Mercenary.CLASS_NAME) {
            PVector position = new PVector(values[0], values[1]);

            WeaponType weaponType = FirearmsWeapon.getWeaponTypeForCode(values[2]);
            int behaviour = values[3];
            person = new Mercenary(position, gameRound, weaponType, behaviour);
            System.out.println("Mercenary on : " + values[0] + " x " + values[1] + " with weapon " + FirearmsWeapon.getWeaponTypeForCode(values[2]));
        }
        else if (personType == gumbaType) {
            PVector position = new PVector(values[0], values[1]);
            try{
                int life = values[3];
                int diameter = Gumba.NORMAL_DIAMETER;
                if (values.length>4){
                    diameter = values[4];
                }
                person = new Gumba(position, life, diameter);
                System.out.println("Gumba  on : " + values[0] + " x " + values[1] + " with life  " + life + "; and diameter: " + diameter);

            }
            catch (Exception e){
                System.out.println("This gumba has no life data");
                person = new Gumba(position);
            }
            byte AI_Model = (byte) values[2];
            person.createAI(new GlobalAI_Controller(AI_Model, (Enemy) person));

        } else if (personType == koopaType) {
            PVector position = new PVector(values[0], values[1]);
            byte AI_Model = (byte) values[2];
            //person = new Koopa(position, AI_Model);
            try{
                int life = values[3];
                int diameter = Koopa.NORMAL_DIAMETER;
                if (values.length>4){
                    diameter = values[4];
                }
                person = new Koopa(position, AI_Model, (int)life, diameter);
            }
            catch (Exception e){
                System.out.println("This koopa has no life data");
                person = new Koopa(position, AI_Model);
            }
            person.createAI(new GlobalAI_Controller(AI_Model, (Enemy) person));
        }
        else if (personType == Spider.CLASS_NAME) {
            PVector position = new PVector(values[0], values[1]);

            byte AI_Model = (byte) values[2];
            System.out.println("Spider start pos: " + position + " and AI: " + AI_Model + " but we have only: " + values.length);
            int life = values[3];
            int diameter = Koopa.NORMAL_DIAMETER;
            if (values.length>4){
                diameter = values[4];
            }
            person = new Spider(position, AI_Model, (int)life, diameter);


            try{

            }
            catch (Exception e){
                System.out.println("This spider has no life data. " + e);
                person = new Koopa(position, AI_Model);
            }
        }
        else if (personType == Snake.CLASS_NAME) {
            PVector position = new PVector(values[0], values[1]);
            int life = values[2];
            int diameter = values[3];
            byte AI_Model = (byte) values[4];
            person = new Snake(position, AI_Model, (int)life, diameter);
            System.out.println("Data for Snake: " + position + "; AI = " + AI_Model + "; Life: " + life + "; Diameter: " + diameter);
        }

        else if (personType == Dragonfly.CLASS_NAME) {
            person = Dragonfly.createPersonFromData(values);
        }
        else if (personType == Lizard.CLASS_NAME) {
            person = Lizard.createPersonFromData(values);
        }
        else if (personType == Bat.CLASS_NAME) {
            person = Bat.createPersonFromData(values);
        }
        else if (personType == BossBoar.CLASS_NAME) {
            person = BossBoar.createPersonFromData(values, gameRound);
        }
        else if (personType == bowserType) {
            PVector position = new PVector(values[0], values[1]);
            try{
                int life = values[2];
                int diameter = Bowser.NORMAL_DIAMETER;
                if (values.length>3){
                    diameter = values[3];
                }
                person = new Bowser(position, (int)life, diameter);
            }
            catch (Exception e){
                person = new Bowser(position);
            }
            person.createAI(new GlobalAI_Controller((GlobalAI_Controller.BOWSER_AI), (Enemy) person));
        }
        else person = null;
        return person;
    }

    public ArrayList <Background> getBackgrounds() {
        String[] textData = getTextDataForType(fileData, Background.CLASS_NAME);
        ArrayList <Background> backgrounds = new ArrayList<>();
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            //System.out.println("Background string is :" + textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            //System.out.println("Value " + values[0]);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            backgrounds.add(createBackground(values, pathToTexture, graphicData));
            //return createBackground(values, pathToTexture, graphicData);
        }
        return backgrounds;
    }

    public Background getBackground() {
        String[] textData = getTextDataForType(fileData, "Background");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            System.out.println("Value " + values[0]);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            return createBackground(values, pathToTexture, graphicData);
        }
        return null;
    }

    private Background createBackground(int[] values, String pathToTexture, int[] graphicData) {
        if (graphicData != null && graphicData.length >0){
            try {
                System.out.println("Background path: " + pathToTexture);
                if (values[0] == 1) {
                    int leftUpperX = graphicData[1];
                    int leftUpperY = graphicData[2];
                    float relativeMovementVelocity = graphicData[3] / 100.00f;
                    if (relativeMovementVelocity < 0) relativeMovementVelocity = 0;
                    else if (relativeMovementVelocity > 3) relativeMovementVelocity = 3;
                    int width = (int) graphicData[0];

                    return new SinglePictureBackground(pathToTexture, width, leftUpperX, leftUpperY, relativeMovementVelocity);
                } else if (values[0] == 2) {
                    return createScrollableAlongXBackground(values, pathToTexture, graphicData);
                } else if (values[0] == 3) {
                    return createRepeatingBackgroundElement(values, pathToTexture, graphicData);
                } else if (values[0] == 4) {
                    return createSingleColorBackground(values);
                }
                else return null;
            }
            catch (Exception e){
                System.out.println("This background needs no picture or asset not founded; " + e);
                System.out.println("Values: " + values.length);
                System.out.println("Graphic data: " + graphicData.length);
            }
        }
        else {
            if (values[0] == 4) return createSingleColorBackground(values);
            System.out.println("Graphic data is small, This background needs no picture");

        }
        return null;
    }

    private Background createSingleColorBackground(int[] values){
        System.out.print("Got values: ");
        for (int i = 0; i < values.length; i++){
            System.out.print (values[i] + ",");
        }
        System.out.println("");
        int red = values[1];
        int green = values[2];
        int blue = values[3];
        return new SingleColorBackground(red, green, blue);
    }

    private Background createRepeatingBackgroundElement(int[] values, String pathToTexture, int[] graphicData){
        Vec2 pos = new Vec2(values[1], values[2]);
        int xStep = values[3];
        int yStep = values[4];
        int angle = values[5];
        float relativeMovementVelocity = values[6] / 100.00f;
        if (relativeMovementVelocity < 0) relativeMovementVelocity = 0;
        else if (relativeMovementVelocity > 3) relativeMovementVelocity = 3;
        int xLeft = (int) graphicData[0];
        int yLeft = (int) graphicData[1];
        int xRight = (int) graphicData[2];
        int yRight = (int) graphicData[3];
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        StaticSprite staticSprite = new StaticSprite(pathToTexture, xLeft, yLeft, xRight, yRight, width, height);
        return new RepeatingBackgroundElement(staticSprite, pos, xStep, yStep, angle, relativeMovementVelocity);
        /*
        Vec2 pos = new Vec2(values[2], values[3]);
        int xStep = values[4];
        int yStep = values[5];
        int angle = values[6];
        float relativeMovementVelocity = values[7] / 100.00f;
        if (relativeMovementVelocity < 0) relativeMovementVelocity = 0;
        else if (relativeMovementVelocity > 3) relativeMovementVelocity = 3;
        int xLeft = (int) graphicData[0];
        int yLeft = (int) graphicData[1];
        int xRight = (int) graphicData[2];
        int yRight = (int) graphicData[3];
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        StaticSprite staticSprite = new StaticSprite(pathToTexture, xLeft, yLeft, xRight, yRight, width, height);
        return new RepeatingBackgroundElement(staticSprite, pos, xStep, yStep, angle, relativeMovementVelocity);
        */
    }

    private Background createScrollableAlongXBackground(int[] values, String pathToTexture, int[] graphicData){
        int width = (int) values[1];
        int height = (int) values[2];
        int leftUpperX = values[3];
        int leftUpperY = values[4];
        float relativeMovementVelocity = values[5] / 100.00f;
        if (relativeMovementVelocity < 0) relativeMovementVelocity = 0;
        else if (relativeMovementVelocity > 3) relativeMovementVelocity = 3;
        int withUpperLine = 1;
        int withLowerLine = 1;
        if (values.length>7){
            withUpperLine = values[6];
            withLowerLine = values[7];
        }
        boolean withUpperLineBool = true;
        boolean withLowerLineBool = true;
        if (withUpperLine == 0) withUpperLineBool = false;
        if (withLowerLine == 0) withLowerLineBool = false;

        //public ScrollableAlongXBackground (String path, int width, int height, int leftUpperX, int leftUpperY, float relativeVelocity){
        System.out.println("Background was created. Path: " + pathToTexture + " with lines: upper: " + withUpperLineBool + " Lower: " + withLowerLineBool);
        for (int i = 0; i < values.length; i++){
            //System.out.println("X " + values[i]);
        }
        ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(graphicData[0], graphicData[1], graphicData[2], graphicData[3]);
        return new ScrollableAlongXBackground(pathToTexture, width, height, leftUpperX, leftUpperY, relativeMovementVelocity, withUpperLineBool, withLowerLineBool, imageZoneSimpleData);

    }

    public ArrayList<Bridge> getBridges(GameRound gameRound) {
        ArrayList<Bridge> bridges = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, Bridge.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            Bridge bridge;
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            bridge = createBridge(gameRound, values);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            addBridgeGraphicData(bridge, pathToTexture, graphicData);
            bridges.add(bridge);
        }
        System.out.println("This level has no bridge");
        return bridges;
    }

    public Bridge getBridge(GameRound gameRound) {
        Bridge bridge;
        String[] textData = getTextDataForType(fileData, Bridge.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            bridge = createBridge(gameRound, values);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            addBridgeGraphicData(bridge, pathToTexture, graphicData);
            return bridge;
        }
        System.out.println("This level has no bridge");
        return null;
    }

    private void addBridgeGraphicData(Bridge bridge, String path, int[] graphicData) {
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
        Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        boolean fillArea = true;
        if (graphicData[6] == 0) fillArea = false;
        bridge.loadImageData(path, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, (int) (width+Bridge.GAP_WIDTH_BETWEEN_ELEMENTS), height, fillArea);
    }


    private Bridge createBridge(GameRound gameRound, int[] values) {
        PVector leftUpperPosition = new PVector(values[0], values[1]);
        PVector rightLowerPosition = new PVector(values[2], values[3]);
        int segmentsAlongX = values[4];
        PVector flagCenter = new PVector(values[5], values[6]);
        int flagZoneWidth = values[7];//50;
        int flagZoneHeight = values[8];
        Flag flag = new Flag(flagCenter, flagZoneWidth, flagZoneHeight, Flag.BRIDGE_CRUSH);
        Bridge bridge = new Bridge(gameRound, leftUpperPosition, rightLowerPosition, segmentsAlongX, flag);
        return bridge;
    }

    public ArrayList<MoveablePlatformsController> getMovablePlatforms() {
        ArrayList<MoveablePlatformsController> moveablePlatformsControllers = new ArrayList<MoveablePlatformsController>();
        String[] textData = getTextDataForType(fileData, "MoveablePlatformsController");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            MoveablePlatformsController moveablePlatformsController = createMoveablePlatformsControllers(values);
            addPlatformsGraphicData(moveablePlatformsController, pathToTexture, graphicData);
            moveablePlatformsControllers.add(moveablePlatformsController);
        }
        return moveablePlatformsControllers;
    }

    private void addPlatformsGraphicData(MoveablePlatformsController moveablePlatformsController, String pathToTexture, int[] graphicData) {
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
        Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        boolean fillArea = true;
        if (graphicData[6] == 0) fillArea = false;
        moveablePlatformsController.loadImageData(pathToTexture, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, fillArea);

    }

    private MoveablePlatformsController createMoveablePlatformsControllers(int[] values) {
        PVector enterPosition = new PVector(values[0], values[1]);
        int enterZoneWidth = values[2];
        int enterZoneHeight = values[3];
        Flag enter = new Flag(enterPosition, enterZoneWidth, enterZoneHeight, Flag.NO_DIRECTION);
        PVector exitPosition = new PVector(values[4], values[5]);
        int exitZoneWidth = values[6];
        int exitZoneHeight = values[7];
        Flag exit = new Flag(exitPosition, exitZoneWidth, exitZoneHeight, Flag.NO_DIRECTION);
        int velocity = (int) values[8];
        int width = (int) values[9];
        int height = (int) values[10];
        byte number = (byte) values[11];
        boolean repeatability = false;
        if (values[12] != 0) repeatability = true;
        MoveablePlatformsController moveablePlatformsController = new MoveablePlatformsController(enter, exit, velocity, width, height, number, repeatability);
        return moveablePlatformsController;
    }

    public ArrayList<ObjectsClearingZone> getObjectsClearingZones() {
        ArrayList<ObjectsClearingZone> objectsClearingZones = new ArrayList<ObjectsClearingZone>();
        String[] textData = getTextDataForType(fileData, "ObjectsClearingZone");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // Was graphic name start char
            ObjectsClearingZone objectsClearingZone = createObjectsClearingZone(values);
            objectsClearingZones.add(objectsClearingZone);
        }
        return objectsClearingZones;
    }



    public ArrayList<EndLevelZone> getEndLevelZones() {
        ArrayList<EndLevelZone> endLevelZones = new ArrayList<EndLevelZone>();
        String[] textData = getTextDataForType(fileData, EndLevelZone.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // Was graphic name start char
            EndLevelZone zone = createEndLevelZone(values);
            endLevelZones.add(zone);
        }
        return endLevelZones;
    }

    public ArrayList<AbstractTrigger> getTriggers(GameRound gameRound) {
        ArrayList<AbstractTrigger> abstractTriggers = new ArrayList<AbstractTrigger>();
        String[] textData = getTextDataForType(fileData, AbstractTrigger.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // Was graphic name start char
            AbstractTrigger zone = createTrigger(gameRound, values);
            abstractTriggers.add(zone);
        }
        return abstractTriggers;
    }


    private AbstractTrigger createTrigger(GameRound gameRound, int[] values) {
        Flag flag = createFlagByFirstValues(values);
        int posX = values[4];
        int posY = values[5];
        int action = values[6];
        int activatingCond = values[7];
        int objectType = values[8];
        int delay = values[9];
        boolean once = true;
        if (values[10] > 0 ) once = false;
        if (values.length>13){
            int newPosX = values[11];
            int newPosY = values[12];
            boolean withForceTeleportation = true;
            if (values[13] == 0) withForceTeleportation = false;
            objectType = values[8];
            System.out.println("New teleporter was created with activating " + activatingCond + "");
            return new TriggerTeleporter(gameRound, flag, posX, posY, action, activatingCond, objectType, delay, once, newPosX, newPosY, withForceTeleportation);

        }
        else System.out.println("There are trigger script. Values size: " + values.length);
        return new TriggerScript(gameRound, flag, posX, posY, action, activatingCond, objectType, delay, once);
        //GameRound gameRound, Flag flag, int objectX, int objectY, int action, int activatingCondition, int objectType, int delay, boolean once

    }


    public ArrayList<SecretAreaZone> getSecretAreaZones() {
        ArrayList<SecretAreaZone> secretAreaZones = new ArrayList<>(3);
        String[] textData = getTextDataForType(fileData, SecretAreaZone.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int id = levelDataStringDecoder.getId(SecretAreaZone.CLASS_NAME);
            if (canObjectBeAdded(id)) {
                if (id != 0 && id != 1) System.out.println("ID for this zone is: " + id);
                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); // Was graphic name start char
                SecretAreaZone zone = createSecretAreaZone(values);
                secretAreaZones.add(zone);
            }
        }
        return secretAreaZones;
    }

    private SecretAreaZone createSecretAreaZone(int[] values) {
        int x = values[0];
        int y = values[1];
        int width = values[2];
        int height = values[3];
        Flag flag = new Flag(new PVector(x,y),width,height,Flag.SECRET_AREA);
        int red = values[4];
        int green = values[5];
        int blue = values[6];
        SecretAreaZone secretAreaZone = new SecretAreaZone(flag, red, green, blue);
        if (Program.debug) System.out.println("Secret area added");
        return secretAreaZone;
    }

    public ArrayList<MessageAddingZone> getMessageAddingZones() {
        ArrayList<MessageAddingZone> messageAddingZones = new ArrayList<>(3);
        String[] textData = getTextDataForType(fileData, MessageAddingZone.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int id = levelDataStringDecoder.getId(MessageAddingZone.CLASS_NAME);
            if (canObjectBeAdded(id)) {

                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR); // Was graphic name start char
                String messageText = levelDataStringDecoder.getStringFromChar(GRAPHIC_NAME_START_CHAR);
                MessageAddingZone zone = createMessageAddingZone(values, messageText);
                messageAddingZones.add(zone);
                if (id != 0 && id != 1) {
                    zone.setUniqueId(id);
                    System.out.println("ID for this zone is: " + id);
                }
            }
        }
        return messageAddingZones;
    }

    private boolean canObjectBeAdded(int id){
        if (id < (-1) || id > 1){
            PlayerProgressLoadMaster playerProgressLoadMaster = new PlayerProgressLoadMaster();
            playerProgressLoadMaster.loadData();
            IntList data = playerProgressLoadMaster.getObjectsNotMoreUploaded();
            System.out.println(data.size() + " objects are blocked and can not be added");
            for (int i = 0; i < data.size(); i++){
                if (id == data.get(i)){
                    System.out.println("Object with id " + id + " can not be appeared again");
                    return false;
                }
            }
            return true;
        }
        else return true;
    }

    public ArrayList<OnDisplayText> getOnDisplayTexts() {
        ArrayList<OnDisplayText> onScreenTextes = new ArrayList<>(1);
        String[] textData = getTextDataForType(fileData, OnDisplayText.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String messageText = levelDataStringDecoder.getStringFromChar(GRAPHIC_NAME_START_CHAR);
            OnDisplayText text = createOnDisplayText(values, messageText);
                //public OnDisplayText(int x, int y, int red, int green, int blue, String text, int timeToShow) {
            onScreenTextes.add(text);
        }
        return onScreenTextes;
    }

    private OnDisplayText createOnDisplayText(int[] values, String messageText) {
        int x = values[0];
        int y = values[1];
        int red = values[2];
        int green = values[3];
        int blue = values[4];
        int time = values[5];
        String text = getFromStringLanguageSpecific(messageText);
        OnDisplayText messageAddingZone = new OnDisplayText(x, y, red, green, blue, text, time);
        System.out.println("On screen text was added " + text);
        return messageAddingZone;
    }

    private String getFromStringLanguageSpecific(String source){
        if (source.contains(LANGUAGE_SPECIFIC_CHARS_ENTER)){
            LanguageSpecificDataManager manager = new LanguageSpecificDataManager();
            String actualLanguage = manager.getLanguage();
            System.out.println("Actual language code is "+ actualLanguage);
            if (source.contains(actualLanguage) && Program.OS == Program.ANDROID){

            }
            else {
                actualLanguage = "en";
            }
            String stringToBeFounded = LANGUAGE_SPECIFIC_CHARS_ENTER+actualLanguage;
            System.out.println("Source string is: " + source + " AND CONTAINS THE ACTUAL LANGUAGE. Try to find string: " + stringToBeFounded);
            int start = source.indexOf(stringToBeFounded);
            if (start >= 0){
                String restString = source.substring(start+stringToBeFounded.length());
                int end = restString.indexOf(LANGUAGE_SPECIFIC_CHARS_ENTER);
                if (end<start) end = restString.length();
                String decodedString = restString.substring(0,end-2);
                System.out.println("Decoded string is " + decodedString);
                return decodedString;
            }
            else return source;
        }
        else return source;
    }

    public ArrayList<CameraFixationZone> getCameraFixationZones() {
        ArrayList<CameraFixationZone> cameraFixationZone = new ArrayList<>(3);
        String[] textData = getTextDataForType(fileData, CameraFixationZone.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
            CameraFixationZone zone = createCameraFixationZone(values);
            cameraFixationZone.add(zone);
        }
        return cameraFixationZone;
    }

    private Flag createFlagByFirstValues(int[]values){
        return createFlagByFirstValues(values, Flag.NO_MISSION);
        /*
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.NO_MISSION);
        return flag;
        */
    }

    private Flag createFlagByFirstValues(int[]values, int mission){
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, (byte) mission);
        return flag;
    }

    private Flag createFlagByLastValues(int[]values){
        PVector position = new PVector(values[values.length-5], values[values.length-4]);
        int zoneWidth = values[values.length-3];
        int zoneHeight = values[values.length-2];
        int mission = values[values.length-1];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, (byte) mission);
        return flag;
    }

    private CameraFixationZone createCameraFixationZone(int[] values) {
        //Flag flag, int x, int y, int activatingCondition, int deactivatingCondition, boolean cameraScale, boolean repeateability){
        Flag flag = createFlagByFirstValues(values, Flag.CAMERA_FIXATION_ZONE);
        int x = values[4];
        int y = values[5];
        int activatingCondition = values[6];
        int deactivatingCondition = values[7];
        boolean cameraScale = CameraFixationZone.MIN_SCALE;
        if (values[8]>= 1) cameraScale = CameraFixationZone.MAX_SCALE;
        boolean repeateability = CameraFixationZone.ONCE;
        if (values[9] != 0) repeateability = CameraFixationZone.REPEATING;
        CameraFixationZone cameraFixationZone = new CameraFixationZone(flag, x, y, activatingCondition, deactivatingCondition, cameraScale, repeateability);
        System.out.println("Camera fixation zone is added with repeateability " + repeateability + " and pos: " + flag.getPosition() + "; height: " + flag.getHeight());
        return cameraFixationZone;
    }


    private MessageAddingZone createMessageAddingZone(int[] values, String messageText) {
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        int activatingCondition = values[4];
        int endMessageStringValue = values[5];
        int portraitValue = values[6];
        int timeToShow = -1;
        if (values.length>7) timeToShow = values[7];
        int withPlayerBlockingInt = 0;
        if (values.length>8) withPlayerBlockingInt = values[8];
        String endMessageString = SMSController.getEndMessageStringForDigit(endMessageStringValue);
        PortraitPicture portraitPicture = SMS.getPortraitPictureForDigit(portraitValue);
        String text = getFromStringLanguageSpecific(messageText);
        /*if (zoneWidth == 0) {
            for (int  i = 0; i < values.length; i++){
                System.out.println("Data " + i + " = " + values[i]);
            }
        }
        System.out.println("Text message " + text + "; End string: " + endMessageString + " dims: " + zoneWidth + " x " + zoneHeight);*/
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.NO_MISSION);
        SMS sms = new SMS(text, endMessageString, portraitPicture, timeToShow);
        boolean withPlayerBlocking = false;
        if (withPlayerBlockingInt >0) withPlayerBlocking = true;
        PVector cameraJumpPos = null;
        boolean scale = CameraFixationZone.MIN_SCALE;
        if (values.length>10){
            cameraJumpPos = new PVector(values[9], values[10]);
            if (values.length>=12){
                if (values[11]>0) scale = CameraFixationZone.MAX_SCALE;
            }
        }
        MessageAddingZone messageAddingZone = new MessageAddingZone(flag, sms, activatingCondition, withPlayerBlocking, cameraJumpPos, scale);
        return messageAddingZone;
    }



    private EndLevelZone createEndLevelZone(int[] values) {
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.CLEAR_OBJECTS);
        byte mission = (byte) values[4];
        if (values.length>5){
            int endLevel = values[5];
            int nextZone = 1;
            int startZone = 1;
            if (values.length>6) {
                nextZone = values[6];
                startZone = values[7];
                if (Program.debug) {
                    System.out.println(" Next zone " + nextZone + "; Actual level: " + endLevel + "; Start zone: " + startZone);
                }
            }
            else {

            }
            return new EndLevelZone(flag, mission, endLevel, nextZone,startZone);

        }
        else return new EndLevelZone(flag, mission);
    }

    private ObjectsClearingZone createObjectsClearingZone(int[] values) {
        PVector position = new PVector(values[0], values[1]);
        int zoneWidth = values[2];
        int zoneHeight = values[3];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.CLEAR_OBJECTS);
        byte mission = (byte) values[4];
        return new ObjectsClearingZone(flag, mission);
    }

    public ArrayList<SimplePortal> getSimplePortals() {
        ArrayList<SimplePortal> portals = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, SimplePortal.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); //was graphic name start char
            SimplePortal portal = createSimplePortal(values);
            portals.add(portal);
        }
        return portals;
    }

    public ArrayList<PortalToAnotherLevel> getPortalsToAnotherLevel(GameRound gameRound, int actualLevelNumber) {
        ArrayList<PortalToAnotherLevel> portals = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, PortalToAnotherLevel.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); //was graphic name start char
            PortalToAnotherLevel portal = createPortalToAnotherLevel(gameRound, values, actualLevelNumber);
            portals.add(portal);

        }
        return portals;
    }

    private PortalToAnotherLevel createPortalToAnotherLevel(GameRound gameRound, int[] values, int actualLevelNumber) {
        PVector enterPosition = new PVector(values[0], values[1]);
        int enterZoneWidth = values[2];
        int enterZoneHeight = values[3];
        Flag enter = new Flag(enterPosition, enterZoneWidth, enterZoneHeight, Flag.PORTAL_ENTER_ZONE, Flag.NO_DIRECTION);
        PVector exitPosition = new PVector(values[4], values[5]);
        int exitZoneWidth = values[6];
        int exitZoneHeight = values[7];
        Flag exit = new Flag(exitPosition, exitZoneWidth, exitZoneHeight, Flag.PORTAL_EXIT_ZONE, Flag.NO_DIRECTION);
        int endLevelNumber = values[8];
        PortalToAnotherLevel portal = new PortalToAnotherLevel(gameRound, enter, exit, actualLevelNumber, endLevelNumber);
        return portal;
    }

    private SimplePortal createSimplePortal(int[] values) {
        PVector enterPosition = new PVector(values[0], values[1]);
        int enterZoneWidth = values[2];
        int enterZoneHeight = values[3];
        Flag enter = new Flag(enterPosition, enterZoneWidth, enterZoneHeight, Flag.PORTAL_ENTER_ZONE, Flag.NO_DIRECTION);
        PVector exitPosition = new PVector(values[4], values[5]);
        int exitZoneWidth = values[6];
        int exitZoneHeight = values[7];
        Flag exit = new Flag(exitPosition, exitZoneWidth, exitZoneHeight, Flag.PORTAL_EXIT_ZONE, Flag.NO_DIRECTION);
        byte portalFor = (byte) values[8];
        boolean portalTransferDirection = PipePortal.HERE_AND_THERE;
        if (values[9] != 0) portalTransferDirection = PipePortal.ENTER_TO_EXIT;
        boolean usingRepeateability = PipePortal.REUSEABLE;
        if (values[10] != 0) usingRepeateability = PipePortal.DISPOSABLE;
        SimplePortal portal = new SimplePortal(enter, exit, portalFor, portalTransferDirection, PipePortal.REUSEABLE);
        return portal;
    }

    public ArrayList<PipePortal> getPortals() {
        ArrayList<PipePortal> portals = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, "Portal");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL); //was graphic name start char
            PipePortal portal = createPortal(values);
            portals.add(portal);
        }
        return portals;
    }



    private PipePortal createPortal(int[] values) {
        PVector enterPosition = new PVector(values[0], values[1]);
        int enterZoneWidth = values[2];
        int enterZoneHeight = values[3];
        byte directionTo = (byte) values[4];
        /*
        public static final byte TO_RIGHT = 1;
        public static final byte TO_DOWN = 2;
        public static final byte TO_LEFT = 3;
        public static final byte TO_UP = 4;
        */
        Flag enter = new Flag(enterPosition, enterZoneWidth, enterZoneHeight, Flag.PORTAL_ENTER_ZONE, directionTo);
        PVector exitPosition = new PVector(values[5], values[6]);
        int exitZoneWidth = values[7];
        int exitZoneHeight = values[8];
        byte directionOut = (byte) values[9];
        Flag exit = new Flag(exitPosition, exitZoneWidth, exitZoneHeight, Flag.PORTAL_EXIT_ZONE, directionOut);
        byte portalFor = (byte) values[10];
        boolean portalTransferDirection = PipePortal.HERE_AND_THERE;
        if (values[11] != 0) portalTransferDirection = PipePortal.ENTER_TO_EXIT;
        boolean usingRepeateability = PipePortal.REUSEABLE;
        if (values[12] != 0) usingRepeateability = PipePortal.DISPOSABLE;
        PipePortal portal = new PipePortal(enter, exit, portalFor, portalTransferDirection, PipePortal.REUSEABLE);
        return portal;
    }

    public ArrayList<RoundPipe> getRoundPipes() {
        ArrayList<RoundPipe> roundPipes = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, "RoundPipe");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            RoundPipe roundPipe = createRoundPipe(values);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            addGraphicData(roundPipe, pathToTexture, graphicData);
            roundPipes.add(roundPipe);
        }
        return roundPipes;
    }

    private RoundPipe createRoundPipe(int[] values) {
        PVector position = new PVector(values[0], values[1]);
        float width = values[2];
        float height = values[3];
        int angle = (int) values[4];
        byte flowerBehaviour = (byte) values[5];
        int life = 120;
        int diameter = 50;
        if (values.length>7){
            life = (int)values[6];
            diameter = (int)values[7];
        }
        RoundPipe roundPipe = new RoundPipe(position, width, height, angle, flowerBehaviour, life, diameter);
        return roundPipe;
        //RoundPipe roundPipe = new RoundPipe(new PVector(-490+65,600+40), 130, 140, (int) 270, Flower.UP_BITE_DOWN);
        //			PVector leftUpperCorner, float w, float h, int angle, byte flowerBehaviour
    }

    public ArrayList<RoundElement> getRoundElements() {
        ArrayList<RoundElement> roundElements = new ArrayList<RoundElement>();
        String[] textData = getTextDataForType(fileData, RoundBox.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);

            RoundBox roundBox = createRoundBox(values);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            addGraphicData(roundBox, pathToTexture, graphicData);
            roundElements.add(roundBox);
        }
        textData = getTextDataForType(fileData, RoundCircle.CLASS_NAME);
        {
            for (int i = 0; i < textData.length; i++) {
                LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
                RoundCircle roundCircle = createRoundCircle(values);
                String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
                int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
                addGraphicData(roundCircle, pathToTexture, graphicData);
                roundElements.add(roundCircle);
            }
        }
        textData = getTextDataForType(fileData, RoundPolygon.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            RoundPolygon roundPolygon;
            if (levelDataStringDecoder.hasStringChar(VERTICES_START_CHAR)) {
                System.out.println("This polygon has old data format");
                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, VERTICES_START_CHAR);
                int[] vertices = levelDataStringDecoder.getValues(VERTICES_START_CHAR, DIVIDER_BETWEEN_VERTICES, GRAPHIC_NAME_START_CHAR);
                roundPolygon = createRoundPolygon(values, vertices);
                String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
                int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
                addGraphicData(roundPolygon, pathToTexture, graphicData);
            }
            else{
                System.out.println("This polygon has new data format");
                int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
                String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
                int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
                roundPolygon = createRoundPolygon(values);
                addGraphicData(roundPolygon, pathToTexture, graphicData);
            }
            roundElements.add(roundPolygon);
        }
        return roundElements;
    }

    private RoundCircle createRoundCircle(int[] values) {
        Vec2 position = new Vec2(values[0], values[1]);
        int radius = values[2];
        int life = (int) values[3];
        boolean withSpring = false;
        if (values[4] != 0) withSpring = true;
        BodyType bodyType;
        if (values[5] == 0) bodyType = BodyType.STATIC;
        else if (values[5] == 1) bodyType = BodyType.KINEMATIC;
        else bodyType = BodyType.DYNAMIC;
        System.out.println("Values :");
        for (int i = 0; i < values.length; i++) System.out.print(" " + values[i] + ";");
        RoundCircle roundCircle = new RoundCircle(position, (int) radius, life, withSpring, bodyType);
        return roundCircle;
    }

    private RoundPolygon createRoundPolygon(int[] values, int[] vertices) {
        Vec2 position = new Vec2(values[0], values[1]);
        float angle = values[2];
        int life = (int) values[3];
        boolean withSpring = false;
        if (values[4] != 0) withSpring = true;
        BodyType bodyType;
        if (values[5] == 0) bodyType = BodyType.STATIC;
        else if (values[5] == 1) bodyType = BodyType.KINEMATIC;
        else bodyType = BodyType.DYNAMIC;
        ArrayList<Vec2> points = new ArrayList<>();
        for (int i = 0; i < vertices.length; i += 2) points.add(new Vec2(vertices[i], vertices[i + 1]));
        RoundPolygon roundPolygon = new RoundPolygon(position, points, angle, life, withSpring, bodyType);
        return roundPolygon;
        //public RoundPolygon(ArrayList<Vec2> pointsPositionsInPixelCoordinates,  float angle, int life, boolean withSpring, BodyType bodyType) {
    }

    private RoundPolygon createRoundPolygon(int[] values) {
        ArrayList <Vec2> points = new ArrayList<>();
        points.add(new Vec2(values[0], values[1]));
        points.add(new Vec2(values[2], values[3]));
        points.add(new Vec2(values[4], values[5]));
        int angle = values[6];
        int life = values[7];
        boolean withSpring = false;
        if (values[8] != 0) withSpring = true;
        BodyType bodyType;
        if (values[9] == 0) bodyType = BodyType.STATIC;
        else if (values[9] == 1) bodyType = BodyType.KINEMATIC;
        else bodyType = BodyType.DYNAMIC;
        RoundPolygon roundPolygon = new RoundPolygon(points, angle, life, withSpring, bodyType);
        return roundPolygon;
        //public RoundPolygon(ArrayList<Vec2> pointsPositionsInPixelCoordinates,  float angle, int life, boolean withSpring, BodyType bodyType) {
    }

    public static Vec2 getGraphicRightLowerCornerForRoundBoxWithoutGraphic(float width, float height){
        Vec2 rightLower = new Vec2();
        float coef = width / height;
        if (coef > 1) {
            rightLower.x = 200;
            rightLower.y = (int) (rightLower.x / coef);
        } else if (coef < 1) {
            rightLower.y = 200;
            rightLower.x = (int) (rightLower.y * coef);
        }
        else {
            rightLower.y = 200;
            rightLower.x = 200;
        }

        return rightLower;
    }

    private void addGraphicData(GameObject gameObject, String path, int[] graphicData) {
        if (gameObject.getClass() == RoundBox.class) {
            Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            float width = (int) graphicData[4];
            float height = (int) graphicData[5];
            boolean fillArea = true;
            if (graphicData[6] == 0) fillArea = false;
            RoundBox roundBox = (RoundBox) gameObject;
            if (path == "BlackRect" || path == MainGraphicController.WITHOUT_GRAPHIC_STRING) {
                if (Program.gameStatement == Program.LEVELS_EDITOR) {
                    path = "BlackRect.png";
                    rightLower = getGraphicRightLowerCornerForRoundBoxWithoutGraphic(width, height);
                    leftUpper.x = 0;
                    leftUpper.y = 0;
                }
                 else {
                    path = "No_data";
                    System.out.println("Path for box: " + path);
                    System.out.println("For invsible sprite added blackRect graphic with zone up to : " + rightLower.x + "x" + rightLower.y + "; Basic dims: " + width + "x" + height);
                    }
                }
            roundBox.loadImageData(path, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, (int) width, (int) height, fillArea);
        }
        else if (gameObject.getClass() == RoundPolygon.class) {
            Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            int width = (int) graphicData[4];
            int height = (int) graphicData[5];
            RoundPolygon roundPolygon = (RoundPolygon) gameObject;
            roundPolygon.loadImageData(path, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height);
        } else if (gameObject.getClass() == RoundCircle.class) {
            Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            int width = (int) graphicData[4];
            int height = (int) graphicData[5];
            boolean fillArea = true;
            if (graphicData[6] == 0) fillArea = false;
            RoundCircle roundCircle = (RoundCircle) gameObject;
            roundCircle.loadImageData(path, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, fillArea);
        } else if (gameObject.getClass() == RoundPipe.class) {
            Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            int width = (int) graphicData[4];
            int height = (int) graphicData[5];
            RoundPipe roundPipe = (RoundPipe) gameObject;
            roundPipe.loadImageData(path, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height);
            System.out.println("Graphic data to pipe was added");
        }
    }

    private RoundBox createRoundBox(int[] values) {
        Vec2 position = new Vec2(values[0], values[1]);
        float angle = values[2];
        float width = values[3];
        float height = values[4];
        int life = (int) values[5];
        boolean withSpring = false;
        if (values[6] != 0) withSpring = true;
        BodyType bodyType;
        if (values[7] == 0) bodyType = BodyType.STATIC;
        else bodyType = BodyType.DYNAMIC;
        System.out.println("Body type: " + bodyType);
        RoundBox roundBox = new RoundBox(position, angle, width, height, life, withSpring, bodyType);
        return roundBox;
    }


    private String getTextDataForTypeFromString(String testString, String type) {
        String textData = "";
        if (type.contains(testString)) {    // If contains on every place
            if (type.equals(testString.substring(0, type.length()))) {
                textData = testString;
                return textData;
            } else {
                System.out.println("This object name is not at begin");
            }
        }
        return null;
    }




    /*
private String[] getTextDataForType(String[] text, String type){
        ArrayList <String> strings = new ArrayList<>();
            if (fileData.length > 0){
                for (int i = 0; i < fileData.length; i ++){
                    if (type.contains(getType(i))) {    // If contains on every place
                        if (type.equals(getType(i).substring(0,type.length()))) {
                            strings.add(fileData[i]);
                        }
                        else {
                            System.out.println("This object name is not at begin");
                        }
                    }
                }
            }
        String [] textData = new String[strings.size()];
        for (int i = 0; i < textData.length; i++){
            textData[i] = strings.get(i).substring(strings.get(i).indexOf(MAIN_DATA_START_CHAR)+1);
        }
        return textData;
    }
    */

    private String[] getTextDataForType(String[] text, String type) {
        ArrayList<String> strings = new ArrayList<>();
        if (text.length > 0) {
            for (int i = 0; i < text.length; i++) {
                if (text[i].charAt(0) == '/' && text[i].charAt(1) == '/'){
                    if (Program.debug) System.out.println("This string is a comment: " + text[i]);
                }
                else {
                    if (getType(text[i]).contains(type)) {
                        if (type.equals(getType(text[i]).substring(0, type.length()))) {
                            strings.add(text[i]);
                        } else {
                            System.out.println("This object name is not at begin");
                        }
                    }
                }
            }
        } else {
            System.out.println("String is too short");
        }
        String[] textData = new String[strings.size()];
        for (int i = 0; i < textData.length; i++) {
            textData[i] = strings.get(i).substring(strings.get(i).indexOf(" "));
            //System.out.println("TROUBLE !. Cam ");
            //textData[i] = strings.get(i).substring(strings.get(i).indexOf(MAIN_DATA_START_CHAR) + 1);
        }
        return textData;
    }

    public int getFileDataSize() {
        return fileData.length;
    }

    private String getType(String string) {
        char endSymbol = ' ';
        //System.out.println("Source string: " + string);
        int endSymbolNumber = string.indexOf(endSymbol);
        if (endSymbolNumber == -1){
            // Maybe level name
            endSymbolNumber = string.length()-1;
        }
        String objectName = string.substring(0, endSymbolNumber);
        //System.out.println("***** objectName:" + objectName);
        return objectName;
    }

    private String getType(int stringNumber) {   // Works good!
        if (stringNumber <= fileData.length) {
            char endSymbol = ' ';
            int endSymbolNumber = fileData[stringNumber].indexOf(endSymbol);
            String objectName = fileData[stringNumber].substring(0, endSymbolNumber);
            return objectName;
        } else return null;
    }

    public MusicInGameController getMusicInGameController() {
        MusicInGameController musicInGameController = null;
        String[] textData = getTextDataForType(fileData, MusicInGameController.CLASS_NAME);
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String track = levelDataStringDecoder.getPathToAudio(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            //levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            System.out.println("Track name: " + track + " source string " + levelDataStringDecoder.stringToBeDecoded + ". Values is all right: " + !(values == null));
            float volume = values[0];
            volume/=100f;
            System.out.println("Track name: " + track + " volume " + volume );
            musicInGameController = new MusicInGameController(track, true, volume);
        }
        //String pathWithoutPrefix, boolean continueFromPrev, float volume) {
        if (musicInGameController == null){
            System.out.println("No music data");
            musicInGameController = new MusicInGameController("", true, 0.0f);
        }
        return musicInGameController;
    }

    public LaunchableWhizbangsController getLaunchableWhizbangsController(GameCamera gameCamera) {
        LaunchableWhizbangsController launchableWhizbangsController = null;
        String[] textData = getTextDataForType(fileData, "LaunchableWhizbangsController");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
            launchableWhizbangsController = createLaunchableWhizbangsController(gameCamera, values);
            if (launchableWhizbangsController!=null) return launchableWhizbangsController;
        }
        launchableWhizbangsController = createLaunchableWhizbangsController(gameCamera);

        return launchableWhizbangsController;
    }

    private LaunchableWhizbangsController createLaunchableWhizbangsController(GameCamera gameCamera) {
        LaunchableWhizbangsController launchableWhizbangsController = new LaunchableWhizbangsController(false, gameCamera, null);
        loadLaunchableWhizbangsGraphic(false, launchableWhizbangsController);
        return launchableWhizbangsController;
    }

    private LaunchableWhizbangsController createLaunchableWhizbangsController(GameCamera gameCamera, int[] values) {
        boolean withBulletsBill = false;
        if (values[0] != 0) withBulletsBill = true;
        PVector position = new PVector(values[1], values[2]);
        int zoneWidth = values[3];
        int zoneHeight = values[4];
        Flag flag = new Flag(position, zoneWidth, zoneHeight, Flag.BULLET_BILL_ACTIVATING_ZONE);
        LaunchableWhizbangsController launchableWhizbangsController = new LaunchableWhizbangsController(withBulletsBill, gameCamera, flag);
        //if (withBulletsBill) loadLaunchableWhizbangsGraphic(withBulletsBill, launchableWhizbangsController);
        loadLaunchableWhizbangsGraphic(withBulletsBill, launchableWhizbangsController);
        return launchableWhizbangsController;
    }

    private void loadLaunchableWhizbangsGraphic(boolean withBulletsBill, LaunchableWhizbangsController launchableWhizbangsController) {
        if (withBulletsBill) launchableWhizbangsController.loadImageData(BulletBill.class, "Tileset3.png", (int) 192, (int) 33, (int) 207, (int) 47, (int) BulletBill.NORMAL_WIDTH, BulletBill.NORMAL_HEIGHT);
        launchableWhizbangsController.loadAnimationData(DragonFire.class, HeadsUpDisplay.mainGraphicSource.getPath(), (int)820, (int)0, (int)1024, (int)192, (int) 110, (int) 50, (byte) 3, (byte) 2, (int) 15);
        launchableWhizbangsController.loadImageData(LaunchableGrenade.class, HeadsUpDisplay.mainGraphicSource.getPath(), (int) InWorldObjectsGraphicData.grenadeLauncherBullet.leftX, InWorldObjectsGraphicData.grenadeLauncherBullet.upperY, InWorldObjectsGraphicData.grenadeLauncherBullet.rightX, InWorldObjectsGraphicData.grenadeLauncherBullet.lowerY, LaunchableGrenade.NORMAL_WIDTH, LaunchableGrenade.NORMAL_HEIGHT);
        launchableWhizbangsController.loadImageData( HandGrenade.class, HeadsUpDisplay.mainGraphicSource.getPath(), (int) InWorldObjectsGraphicData.handGrenadeInWorldAsBullet.leftX, InWorldObjectsGraphicData.handGrenadeInWorldAsBullet.upperY, InWorldObjectsGraphicData.handGrenadeInWorldAsBullet.rightX, InWorldObjectsGraphicData.handGrenadeInWorldAsBullet.lowerY, (int)(HandGrenade.NORMAL_DIAMETER*1.5f), (int)(HandGrenade.NORMAL_DIAMETER*1.5f));

        //launchableWhizbangsController.loadAnimationData(DragonFire.class, Programm.getRelativePathToAssetsFolder()+"Dragon fire animation.png", (int) 0, (int) 0, (int) 336, (int) 318, (int) 110, (int) 50, (byte) 3, (byte) 2, (int) 15);
        //
        System.out.println("Animation loaded for whizzbangs" + launchableWhizbangsController);
    }
/*
    public CollectableObjectsController getCollectableObjectsControllerInNESStyle(GameRound gameRound) {
        CollectableObjectsController collectableObjectsController = new CollectableObjectsController();
        ArrayList<AbstractCollectable> collectableObjects = new ArrayList<AbstractCollectable>();
        String[] textData = getTextDataForType(fileData, collectableObjectClassName);
        System.out.println("Data list has " + textData.length + "strings");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            CollectableObjectInNesStyle collectableObject = createCollectableObject(gameRound, values, pathToTexture, graphicData);
            if (collectableObject != null) {
                if (collectableObject.getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) {
                    collectableObjects.add(collectableObject);
                    System.out.println("The object is in World");
                } else {
                    collectableObjectsController.getAnotherObjectOnThisPlace(new Vec2(values[0], values[1]), gameRound).addNewCollectableObject(collectableObject);
                    System.out.println("The object is in some another");
                }
            }
        }

        collectableObjectsController.setCollectableObjects(collectableObjects);
        return collectableObjectsController;
    }
*/

    public CollectableObjectsController getCollectableObjectsController(GameRound gameRound) {
        CollectableObjectsController collectableObjectsController = new CollectableObjectsController();
        ArrayList<AbstractCollectable> collectableObjects = new ArrayList<AbstractCollectable>();
        String[] textData = getTextDataForType(fileData, SimpleCollectableElement.CLASS_NAME);
        //System.out.println("Data list has " + textData.length + "strings");
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
            AbstractCollectable collectableObject = createCollectableObject(gameRound, values);
            if (collectableObject != null) {
                if (collectableObject.getInWorldPosition() == AbstractCollectable.IN_WORLD) {
                    collectableObjects.add(collectableObject);
                } else {
                    collectableObjectsController.getAnotherObjectOnThisPlace(new Vec2(values[0], values[1]), gameRound).addNewCollectableObject(collectableObject);
                    System.out.println("The object is in some another");
                }
            }
        }
        //collectableObjectsController.getCollectableObjects() = collectableObjects;
        collectableObjectsController.setCollectableObjects(collectableObjects);
        return collectableObjectsController;
    }

    private ArrayList<CollectableObjectInNesStyle> getCollectableObjects(GameRound gameRound, String[] textData) {
        ArrayList<CollectableObjectInNesStyle> collectableObjects = new ArrayList<CollectableObjectInNesStyle>();
        for (int i = 0; i < textData.length; i++) {
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(textData[i]);
            int[] values = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, GRAPHIC_NAME_START_CHAR);
            String pathToTexture = levelDataStringDecoder.getPathToTexture(GRAPHIC_NAME_START_CHAR, GRAPHIC_NAME_END_CHAR);
            int[] graphicData = levelDataStringDecoder.getGraphicData(GRAPHIC_NAME_END_CHAR, DIVIDER_BETWEEN_GRAPHIC_DATA);    //System.out.print("Val:");
            System.out.println("Data for collectable objects controller succesfully loaded");
            CollectableObjectInNesStyle collectableObject = createCollectableObject(gameRound, values, pathToTexture, graphicData);
            if (collectableObject != null){
                if (collectableObject.getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) {
                    collectableObjects.add(collectableObject);
                    System.out.println("The object is in World");
                }
            }
        }
        return collectableObjects;
    }

    private AbstractCollectable createCollectableObject(GameRound gameRound, int[] values) {
        boolean isAppear = true;
        float probability = Program.engine.random(100);
        int value = values[2];
        if (probability < value){
            System.out.println("This collectable object was created");
        }
        else {
            System.out.println("This collectable object is not be created");
            isAppear = false;
        }
        if (isAppear){
            Vec2 pos = new Vec2(values[0], values[1]);
            // 2 is reserved
            byte type = (byte) values[3];
            int valueToBeAddedByAchivement =  values[4];
            int bodyType = values[5];
            AbstractCollectable collectableObject = null;
            if (type >= CollectableObjectInNesStyle.BULLETS_FOR_SHOTGUN){
                collectableObject = new WeaponMagazine(pos, type, gameRound, valueToBeAddedByAchivement, bodyType);
            }
            else if (type == AbstractCollectable.SMALL_MEDICAL_KIT || type == AbstractCollectable.MEDIUM_MEDICAL_KIT || type == AbstractCollectable.LARGE_MEDICAL_KIT){
                collectableObject = new MedicalKit(pos, type, gameRound, valueToBeAddedByAchivement, bodyType);
            }
            else if (type > AbstractCollectable.ABSTRACT_FRUIT && type <= AbstractCollectable.WATERMELON){
                System.out.println("It is a fruit " + type);
                collectableObject = new Fruit(pos, type, gameRound, valueToBeAddedByAchivement, bodyType);
            }
            else if (type == AbstractCollectable.SYRINGE){
                collectableObject = new Syringe(pos, type, gameRound, valueToBeAddedByAchivement, bodyType);
            }
            else if (Money.isTypeMoney(type)){
                System.out.println("It is a money " + type);
                collectableObject = new Money(pos, type, gameRound, valueToBeAddedByAchivement, bodyType);
            }
            else if (type == AbstractCollectable.SMALL_BAG || type == AbstractCollectable.BIG_BAG){
                collectableObject = new CollectableSack(pos, type, bodyType);
            }
            //System.out.println("It is a object with type " + type);
            return collectableObject;
        }
        else{
            return null;
        }
    }

    private CollectableObjectInNesStyle createCollectableObject(GameRound gameRound, int[] values, String pathToTexture, int[] graphicData) {
        boolean isAppear = true;
        if (values.length>5){
            float probability = Program.engine.random(100);
            if (probability < values[5]){
                System.out.println("This collectable object was created");
            }
            else {
                System.out.println("This collectable object is not be created");
                isAppear = false;
            }
        }
        if (isAppear){
            Vec2 pos = new Vec2(values[0], values[1]);
            int diameter = (int) values[2];
            byte type = (byte) values[3];
            boolean withSpring = false;
            if (values[4] != 0) withSpring = true;

            CollectableObjectInNesStyle collectableObject;
            collectableObject = new CollectableObjectInNesStyle(pos, type, gameRound, diameter, withSpring);

            Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            int width = (int) graphicData[4];
            int height = (int) graphicData[5];
            byte rowsNumber = (byte) graphicData[6];
            byte columnsNumber = (byte) graphicData[7];
            int updateFrequency = (int) graphicData[8];
            //1x1x799x599x35x35x2,3,65
            collectableObject.loadAnimationData(pathToTexture, (int) leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, rowsNumber, columnsNumber, updateFrequency);

            return collectableObject;
        }
        else{
            return null;
        }

        //Coin 1:410,282,35,1,0#Shining coin animation.png;1x1x799x599x35x35x2,3,65
    }

    public ArrayList<ObjectsAppearingZone> getObjectsAppearingZonesControllers(GameRound gameRound) {
        ArrayList<ObjectsAppearingZone> objectsAppearingZonesControllers = new ArrayList<>();
        String[] textData = getTextDataForType(fileData, "ObjectsAppearingZone");
        for (int i = 0; i < textData.length; i++) {
            String zoneDataString = LevelDataStringDecoder.getStringToValue(textData[i], VERTICES_START_CHAR);
            LevelDataStringDecoder levelDataStringDecoder = new LevelDataStringDecoder(zoneDataString);
            int[] zoneValues = levelDataStringDecoder.getValues(MAIN_DATA_START_CHAR, DIVIDER_BETWEEN_VALUES, END_ROW_SYMBOL);
            String insideObjectClassName = getType(textData[i].substring(textData[i].indexOf(VERTICES_START_CHAR) + 1));
            System.out.println("Data type + " + insideObjectClassName);
            String lastData = textData[i].substring(textData[i].indexOf(insideObjectClassName));
            GameObject gameObject = getInsideGameObject(gameRound, insideObjectClassName, lastData);
            objectsAppearingZonesControllers.add(createObjectsAppearingZone(zoneValues, gameRound, gameObject));
        }

        return objectsAppearingZonesControllers;
    }

    private GameObject getInsideGameObject(GameRound gameRound, String objectClassName, String dataString) {
        GameObject gameObject;
        //System.out.println("Test string to find " + dataString);
        String objectDataString = LevelDataStringDecoder.getStringFromValue(dataString, MAIN_DATA_START_CHAR);
        //System.out.println("Class name:_" + objectClassName  + "_must be_" + gumbaType);
        if (objectClassName.equals(gumbaType) || objectClassName.equals(koopaType) || objectClassName.equals(bowserType) || objectClassName.equals((Spider.CLASS_NAME)) || objectClassName.equals(Snake.CLASS_NAME) || objectClassName.equals((Dragonfly.CLASS_NAME))) {
            String[] textData = new String[1];
            textData[0] = dataString;
            ArrayList<Person> person = getPersons(textData, gameRound);
            gameObject = person.get(0);
        } else if (objectClassName.equals(collectableObjectClassName)) {
            String[] textData = new String[1];
            textData[0] = dataString;
            ArrayList<CollectableObjectInNesStyle> collectableObjects = getCollectableObjects(gameRound, textData);
            gameObject = collectableObjects.get(0);
        } else gameObject = null;
        return gameObject;
    }


    private ObjectsAppearingZone createObjectsAppearingZone(int[] values, GameRound gameRound, GameObject insideObject) {
        Vec2 zonePos = new Vec2(values[0], values[1]);
        int zoneWidth = (int) values[2];
        int zoneHeight = (int) values[3];
        Flag zoneFlag = new Flag(new PVector(zonePos.x, zonePos.y), zoneWidth, zoneHeight, Flag.OBJECTS_APPEARING_ZONE);
        Vec2 triggerPos = new Vec2(values[4], values[5]);
        int triggerWidth = (int) values[6];
        int triggerHeight = (int) values[7];
        byte triggerActivatingCondition = (byte) values[8];
        /*
        public final static byte PLAYER_COMMING_IN_ZONE_TRIGGER = 11;
        public final static byte PLAYER_LEAVING_ZONE_TRIGGER = 12;
        public final static byte PERSON_COMMING_IN_ZONE_TRIGGER = 13;
        public final static byte PERSONS_LEAVING_ZONE_TRIGGER = 14;	// when all persons leaving the area the flag is activated
        public final static byte FULL_CLEARING_ZONE_TRIGGER = 15;
        */
        Flag triggerFlag = new Flag(new PVector(triggerPos.x, triggerPos.y), triggerWidth, triggerHeight, triggerActivatingCondition);
        byte appearingDirection = (byte) values[9];
        /*
        public static final byte TO_LEFT = 1;
        public static final byte TO_RIGHT = 2;
        public static final byte TO_UP = 3;
        public static final byte TO_DOWN = 4;
        */
        int velocity = (int) values[10];
        int timeBetweenApearings = values[11];
        int objectsNumber = (int) values[12];
        /*
        GameObject insideObject;
        //insideObject = new Gumba(new PVector(400,-50));


        else insideObject = null;
        */
        System.out.println("Inside object: " + insideObject.getClass() + " is null: ");
        if (insideObject == null) System.out.println(" null");
        ObjectsAppearingZone objectsAppearingZone = new ObjectsAppearingZone(insideObject, zoneFlag, triggerFlag, appearingDirection, velocity, timeBetweenApearings, objectsNumber);

        //int dimension = 40;

        /*
        Vec2 pos = new Vec2(values[0], values[1]);
        byte type = (byte) values[2];
        int dimension = values[3];
        */
        /*
        CollectableObject collectableObject = new CollectableObject(pos, CollectableObjectInWorld.STAR, gameRound, dimension, false);
        collectableObject.loadAnimationData("star-collectibles.png", (int) 0, (int) 0, (int) 582, (int) 184, (int)(dimension) , (int) (dimension), (byte)1, (byte)3, (int) 85);


        ObjectsAppearingZone objectsAppearingZone = new ObjectsAppearingZone(collectableObject, flag, activatingFlag, Game2D.TO_UP, 15, 1000, 5);
        objectsAppearingZonesControllers.add(objectsAppearingZone);
        */
        /*
        Vec2 pos = new Vec2(values[0], values[1]);
        CollectableObject collectableObject = new CollectableObject(pos, CollectableObjectInWorld.STAR, gameRound, 25, false);
        collectableObject.loadAnimationData("star-collectibles.png", (int) 0, (int) 0, (int) 582, (int) 184, (int)(25) , (int) (25), (byte)1, (byte)3, (int) 85);
        Gumba gumba = new Gumba(new PVector(400,-50));

        Flag flag = new Flag(new PVector(pos.x, pos.y), 50, 90, Flag.OBJECTS_APPEARING_ZONE);
        Flag activatingFlag = new Flag(new PVector(pos.x+150, pos.y), 50, 90, Flag.PLAYER_COMMING_IN_ZONE_TRIGGER);
        ObjectsAppearingZone objectsAppearingZone = new ObjectsAppearingZone(gumba, flag, activatingFlag, Game2D.TO_UP, 15, 1000, 5);
        */
        return objectsAppearingZone;
    }


    public String getStringByNumber(int i) {
        if (fileData != null){
            return fileData[i];
        }
        else {
            System.out.println("File data was not loaded yet");
            return null;
        }
    }

    public String getLevelName() {
        if (fileData != null){
            String name = fileData[0].substring(2);
            return name;
        }
        else {
            System.out.println("File data was not loaded yet");
            return null;
        }
    }



}
