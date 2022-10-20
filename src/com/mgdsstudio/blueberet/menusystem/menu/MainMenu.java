package com.mgdsstudio.blueberet.menusystem.menu;


import com.mgdsstudio.blueberet.gamelibraries.Timer;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_SimpleImage;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;

public class MainMenu extends AbstractMenu{
    public static boolean mustBeEditorAndUserLevelsBlocked = true;

    private static String CAMPAIGN = "CAMPAIGN";
    private static String MY_LEVELS = "MY LEVELS";
    private static String LEVEL_EDITOR = "LEVEL EDITOR";
    private static String OPTIONS = "OPTIONS";
    private static String EXIT = "EXIT";

    //private static String DEBUG_HELP_PROJECT = "HELP US";
    //private PImage image;
    private final boolean WITH_APPEARING = true;
    private static boolean alreadyAppeared = false;
    private AppearingController appearingController;
    //private int relativePos;
    private final int TIME_BEFORE_BUTTONS_ACTIVE_AFTER_APPEARING = 350;
    private Timer afterAppearingTimer;
    private boolean buttonsBlocked = true;
    private boolean audioMustBeStartedOnThisFrame = false;
    private Timer timerToStartDataCopy;
    private final int TIME_BEFORE_FILE_COPYING_STARTS = 200;
    private boolean filesCopingEnded;
    private boolean audioInitialized;
    private Timer timerForWaitingUntilFilesCopied;
    private final int timeToWaitForCopying = 240;
    //private ImageZoneSimpleData emblemDa = GameMenusController.playerHead;

    public MainMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.MAIN;
        initLanguageSpecific();
        init(engine, graphics);

        System.out.println("Main menu was created");
        if (WITH_APPEARING && !alreadyAppeared){
            appearingController = new AppearingController(engine, engine.height, guiElements);
            System.out.println("It must be apeared");
        }
        else buttonsBlocked = false;


        //sound.volume(0.1f);
        loadGraphic(engine, graphics);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            CAMPAIGN = "КАМПАНИЯ";
            MY_LEVELS = "ОДИНОЧНЫЕ МИССИИ";
            LEVEL_EDITOR = "РЕДАКТОР УРОВНЕЙ";
            OPTIONS = "НАСТРОЙКИ";
            EXIT = "ВЫХОД";
        }
        else{
        }
    }

    public boolean canBeMusicSwitchedOn(){
        if (alreadyAppeared){
            return true;
        }
        else return false;

    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        int lowerY = engine.height-engine.width/8;
        String [] names = new String[]{"Exclusive for android users", "POWERED BY PROCESSING","© MGDS STUDIO 2022", " " };
        fillGuiWithTextLabels(engine,lowerY, names, TO_UP);
        lowerY= (guiElements.get(guiElements.size()-1).getUpperY()-guiElements.get(guiElements.size()-1).getHeight());
        names = new String[]{EXIT, OPTIONS,LEVEL_EDITOR,MY_LEVELS, CAMPAIGN};
        //names = new String[]{DEBUG_HELP_PROJECT, EXIT, OPTIONS,LEVEL_EDITOR,MY_LEVELS, CAMPAIGN};
        fillGuiWithCursorButtons(engine,lowerY, names,TO_UP);
        blockButtons();

    }



    private boolean mustBeEditorAndSingleLevelsDeblocked() {
        if (AbstractMenu.wasGameAlreadyStarted()){
            return true;
        }
        else return false;
    }
    private void blockButtons() {
        if (!mustBeEditorAndSingleLevelsDeblocked() && !Program.debug){
            for (NES_GuiElement guiElement : guiElements){
                if (guiElement.getName() == LEVEL_EDITOR || guiElement.getName() == MY_LEVELS){
                    guiElement.block(true);
                }
            }
        }
    }

    private void loadGraphic(PApplet engine, PGraphics graphics) {
        //NES_SimpleImage nes_simpleImage = new NES_SimpleImage("Game emblem.gif");
        int width = (int) (engine.width*0.8f);
        final PImage image = engine.loadImage(Program.getRelativePathToAssetsFolder()+"Game emblem.gif");
        image.resize((int) (engine.width*0.8f), 0);

        int height = image.height;
        int x = graphics.width/2;
        int y = 2*height+getRelativePos();
        NES_SimpleImage nes_simpleImage = new NES_SimpleImage(x,y,width, height, "Main emblem", graphics, (Program.getRelativePathToAssetsFolder()+"Game emblem.gif"));
        guiElements.add(nes_simpleImage);
        //image.resize((int) (engine.width*0.8f), 0);

    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (audioMustBeStartedOnThisFrame && audioInitialized){
            //gameMenusController.getMusicController().startToPlay();
            audioMustBeStartedOnThisFrame = false;
        }
        initCopyngTimer(gameMenusController);
        initAudio(gameMenusController);
        copyFilesToCache(gameMenusController);
        if (WITH_APPEARING && alreadyAppeared) {
            if (!buttonsBlocked && audioInitialized) {
                super.update(gameMenusController, mouseX, mouseY);
                for (NES_GuiElement element : guiElements) {
                    if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                        if (element.getName() == EXIT) {
                            gameMenusController.switchOffMusic();
                            gameMenusController.setNewMenu(MenuType.EXIT_MENU);
                            dispose(gameMenusController.getEngine());
                        } else if (element.getName() == OPTIONS) {
                            //gameMenusController.setUserValue(MenuType.MAIN);
                            //gameMenusController.setNewMenu(MenuType.FULL_SCREEN_ADD_MENU);
                            gameMenusController.setNewMenu(MenuType.OPTIONS);
                            dispose(gameMenusController.getEngine());
                        } else if (element.getName() == MY_LEVELS) {
                            gameMenusController.setNewMenu(MenuType.USER_LEVELS);
                            dispose(gameMenusController.getEngine());
                        } else if (element.getName() == CAMPAIGN) {
                            gameMenusController.setNewMenu(MenuType.CAMPAIGN);
                            dispose(gameMenusController.getEngine());
                        } else if (element.getName() == LEVEL_EDITOR) {
                            if (Program.OS == Program.ANDROID){
                                String toastMessage = "The editor mode is not completed right now and under development! Please threat the editor with understanding. ";
                                if (Program.LANGUAGE == Program.RUSSIAN){
                                    toastMessage = "Редактор на данном этапе не готов, находиться в стадии pre-alpha и имеет некоторые проблемы. Просим Вас отнестись с пониманием к возможным сложностям и вылетам приложения. ";
                                }
                                //MainActivity mainActivity = (MainActivity) Program.engine.getActivity();
                                Program.iEngine.addToastMessage(toastMessage);
                            }
                            gameMenusController.switchOffMusic();
                            gameMenusController.setNewMenu(MenuType.EDITOR_LOADING);
                            dispose(gameMenusController.getEngine());
                        }
                    }
                }
            }
            else {
                //System.out.println("Buttons are blocked");
                if (WITH_APPEARING){
                    if (afterAppearingTimer == null) {
                        System.out.println("Init audio");
                        audioInitialized = true;
                    }
                    else {
                        if (afterAppearingTimer.isTime()) {
                            buttonsBlocked = false;
                            System.out.println("Buttons deblocked at " + gameMenusController.getEngine().frameCount + " frame ");
                            gameMenusController.applyPreferences();
                            for (NES_GuiElement gui : guiElements) {
                                gui.setActualStatement(NES_GuiElement.ACTIVE);
                            }
                            blockButtons();
                            audioMustBeStartedOnThisFrame = true;
                        }
                    }
                }
            }
        }
        else if (WITH_APPEARING && !alreadyAppeared){
            updateGuiPos(gameMenusController.getEngine(), gameMenusController);
            if (gameMenusController.getEngine().mousePressed){
                alreadyAppeared = true;
                System.out.println("Try to stop gui 1");
                returnAllGraphicOnPlace(gameMenusController.getEngine());
                gameMenusController.launchMusicAndSwitchSoundOn();
                System.out.println("Menu returns at " + gameMenusController.getEngine().frameCount);
                afterAppearingTimer = new Timer(TIME_BEFORE_BUTTONS_ACTIVE_AFTER_APPEARING);
            }
        }

        //
    }


    private void initAudio(GameMenusController gameMenusController){
        if (filesCopingEnded){
            if (!audioInitialized){
                if (timerForWaitingUntilFilesCopied != null){
                    if (timerForWaitingUntilFilesCopied.isTime()){
                        gameMenusController.initAudioControllers();
                        audioInitialized = true;
                    }
                }

                //gameMenusController.applyPreferences();
            }
        }
    }
    private void copyFilesToCache(GameMenusController gameMenusController) {
        if (!filesCopingEnded) {
            if (timerToStartDataCopy != null) {
                if (timerToStartDataCopy.isTime()) {
                    timerForWaitingUntilFilesCopied = new Timer(timeToWaitForCopying);
                    if (Program.OS == Program.ANDROID) {
                        Program.iEngine.copyUserLevelsToCache();

                    }
                    filesCopingEnded = true;

                }

            }
        }
    }

    private void initCopyngTimer(GameMenusController gameMenusController) {
        if (gameMenusController.getEngine().frameCount >=5 ){
            if (timerToStartDataCopy == null){
                timerToStartDataCopy = new Timer(TIME_BEFORE_FILE_COPYING_STARTS);
                System.out.println("Timer started on " + gameMenusController.getEngine().millis());
            }
        }
    }

    private void returnAllGraphicOnPlace(PApplet engine) {
        appearingController.setGuiBack(guiElements);
    }

    private void updateGuiPos(PApplet engine,  GameMenusController gameMenusController) {
        if (WITH_APPEARING && !alreadyAppeared){
            appearingController.updatePos(engine);
            appearingController.shiftGui(guiElements);
            //System.out.println("Moving");
            if (appearingController.isEnded()){
                alreadyAppeared = true;
                System.out.println("Try to stop gui 2");
                afterAppearingTimer = new Timer(TIME_BEFORE_BUTTONS_ACTIVE_AFTER_APPEARING);
                gameMenusController.launchMusicAndSwitchSoundOn();
                System.out.println("Menu returns at " + gameMenusController.getEngine().frameCount);
            }
        }
    }

    /*
    @Override
    protected void dispose(PApplet engine){
        //engine.g.removeCache(image);
        //if (image != null) image = null;
        //super.dispose(engine);
        ///System.out.println("Images were deleted from graphic object");
    }*/

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        //drawTitle(graphic);
    }

    /*
    private void drawTitle(PGraphics graphic) {
        if (image != null){
            graphic.image(image, graphic.width/2, 2*image.height+getRelativePos());

        }
    }*/

    private int getRelativePos() {
        if (WITH_APPEARING && !alreadyAppeared){
            return appearingController.getRelativePos();
        }
        else return 0;
    }

    private class AppearingController{
        private boolean ended;
        private int startPos;
        final float MOVING_TIME = 2000;
        private float velocity;
        private int actualPos;
        private float deltaPos;
        private float deltaTime;
        private long prevFrameTime;
        private Timer reserveTimer;

        AppearingController(PApplet engine, int height, ArrayList<NES_GuiElement> guiElements){
            startPos = height;
            actualPos = startPos;
            velocity = startPos/MOVING_TIME;
            System.out.println("Moving velocity: " + velocity);
            prevFrameTime = engine.millis();
            reposGui(guiElements);
            reserveTimer = new Timer((int) (MOVING_TIME+250));
        }

        void updatePos(PApplet engine){
            deltaTime = engine.millis()-prevFrameTime;
            if (deltaTime < 60) {
                if (!ended) {
                    deltaPos = (velocity) * deltaTime;
                    actualPos -= deltaPos;
                    //System.out.println("Delta time: " + deltaTime);
                    if (actualPos <= 0) {
                        ended = true;
                        actualPos = 0;
                    }
                }
                if (reserveTimer.isTime()) {
                    System.out.println("Buttons are not on the place and must be returned external");
                    //ended = true;
                    //actualPos = 0;

                }
            }
            else {
                System.out.println("This game loading too long");
            }
            prevFrameTime = engine.millis();
        }

        int getRelativePos(){
            return actualPos;
        }

        boolean isEnded(){
            return ended;
        }

        public void shiftGui(ArrayList<NES_GuiElement> guiElements) {
            for (int i = 0; i < guiElements.size(); i++){
                int newPos = (int)(guiElements.get(i).getUpperY()-deltaPos);
                guiElements.get(i).setUpperY(newPos);
                if (i == 0) {
                   // System.out.println("Actual pos: " + guiElements.get(i).getUpperY());
                }
            }
        }

        public void reposGui(ArrayList<NES_GuiElement> guiElements) {
            for (int i = 0; i < guiElements.size(); i++){
                int newPos = (int)(guiElements.get(i).getUpperY()+startPos);
                guiElements.get(i).setUpperY(newPos);
                if (i == 0) {
                    //System.out.println("Actual pos: " + guiElements.get(i).getUpperY());
                }
            }
        }


        public void setGuiBack(ArrayList<NES_GuiElement> guiElements) {
            shiftGui(guiElements);
            int resetLenght = startPos-(startPos-actualPos);
            System.out.println("Start: " + startPos + " actual: " + actualPos + "; Delta: " + resetLenght);
            for (int i = 0; i < guiElements.size(); i++){
                int newPos = (int)(guiElements.get(i).getUpperY()-resetLenght);
                guiElements.get(i).setUpperY(newPos);
            }
        }
    }


}
