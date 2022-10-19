package com.mgdsstudio.blueberet.menusystem;


import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.gameprocess.control.DPadSize;
import com.mgdsstudio.blueberet.gameprocess.sound.*;
import com.mgdsstudio.blueberet.gameprocess.sound.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;

import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataConstants;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataController;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataLoadMaster;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataSaveMaster;
import com.mgdsstudio.blueberet.menusystem.menu.*;
import com.mgdsstudio.blueberet.menusystem.menu.*;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import com.mgdsstudio.texturepacker.TextureEncryptManager;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class GameMenusController {
    public static Image sourceFile;
    public final static ImageZoneSimpleData background = new ImageZoneSimpleData(60,130,61,131);
    public final static ImageZoneSimpleData playerHead = new ImageZoneSimpleData(0,0,32,32);
    public final static ImageZoneSimpleData simpleFrameZone = new ImageZoneSimpleData(0,32,34,66);
    public final static ImageZoneSimpleData dialogFrameZone = new ImageZoneSimpleData(0,66,154,221);
    public final static ImageZoneSimpleData hand = new ImageZoneSimpleData(50-47,280-47,50+47,280+47);
    public final static ImageZoneSimpleData crack = new ImageZoneSimpleData(0,377,154,536);
    //public final static ImageZoneSimpleData hand = new ImageZoneSimpleData(50-47,273-40,50+47,273+47);
    public final static ImageZoneSimpleData lock  = new ImageZoneSimpleData(45-16,0,62,32);;
    private final PApplet engine;
    private final PGraphics graphics;
    private SoundInMenuController soundInMenuController;
    private MusicInMenuController musicInMenuController;
    private AbstractMenu menu;
    private MenuType menuType;
    private MenuType newMenuType;
    private final GameMainController gameMainController;
    private Boolean additionalValue;
    private Object userValue;
    private boolean active = true;

    private boolean updatedMusicUploadingSystem = false;

    private int levelNumberToBeLoadedNext;
    private final boolean withAudioInitAfterMenuStopped = true;

    public GameMenusController(GameMainController gameMainController){
        this.gameMainController = gameMainController;
        if (!withAudioInitAfterMenuStopped) musicInMenuController = new MusicInMenuController(TrackData.NORMAL_AUDIO);
        engine = gameMainController.getEngine();
        String path = Program.getAbsolutePathToAssetsFolder("Menu graphic original.gif");
        System.out.println("Path to graphic file is ; " + path);
        sourceFile = new Image(path);
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("OS: " + os + " android: " + (Program.OS == Program.ANDROID));
        boolean linux = (os.indexOf("nix")>=0);
        if (Program.USE_3D) graphics = engine.createGraphics(engine.width, engine.height, PApplet.P2D);
        else {
            if (linux && Program.OS != Program.ANDROID) {
                graphics = engine.createGraphics(engine.width, engine.height, PApplet.JAVA2D);
                graphics.noSmooth();
            } else {
                graphics = engine.createGraphics(engine.width, engine.height, PApplet.P2D);
                graphics.noSmooth();
            }
        }
        graphics.imageMode(PApplet.CENTER);
        newMenuType = MenuType.MAIN;
        //newMenuType = MenuType.DO_YOU_LIKE_OUR_GAME_MENU;
        //
        menuType = newMenuType;
        if (!withAudioInitAfterMenuStopped) soundInMenuController = new SoundInMenuController(1);
        init();
    }

    private void init() {
        createNewMenu();
        if (menu instanceof MainMenu){
            MainMenu mainMenu = (MainMenu) menu;
            if (mainMenu.canBeMusicSwitchedOn()){

            }
            else {
                System.out.println("Music was switched off from init() ");
                if (!withAudioInitAfterMenuStopped) musicInMenuController.pausePlay();
                //soundInMenuController.setActive(false);
            }
        }
    }

    public void applyPreferences() {
        if (PreferencesDataController.dataFileExists()){
            try{
                applyOptionsData();
            }
            catch (Exception e){
                System.out.println("Can not apply preferences data");
            }
        }
        else {
            PreferencesDataSaveMaster master = new PreferencesDataSaveMaster(engine);
            master.saveData();
        }
        System.out.println("Preferences data was applied");
    }

    public void initAudioControllers(){
        if (withAudioInitAfterMenuStopped) {
            //if (musicInMenuController == null)
            System.out.println("Next string made a trouble in the emulator mode!");
            if (!updatedMusicUploadingSystem) {
                if (musicInMenuController == null)
                    musicInMenuController = new MusicInMenuController(TrackData.NORMAL_AUDIO);

            }
            soundInMenuController = new SoundInMenuController(1);
            if (Program.debug) System.out.println("Audio was init after menu was stopped");
        }
    }



    private void applyOptionsData() {
        PreferencesDataLoadMaster master = new PreferencesDataLoadMaster(engine);
        int antiAliasing = master.getAntiAliasing();
        Program.ANTI_ALIASING = antiAliasing;
        String performance = master.getPerformance();
        int framerate = 30;
        if (performance == PreferencesDataConstants.HIGH || performance.equals(PreferencesDataConstants.HIGH)){
            framerate = 60;
        }
        else if (performance == PreferencesDataConstants.MEDIUM  || performance.equals(PreferencesDataConstants.MEDIUM)) {
            framerate =  50;
        }
        Program.NORMAL_FPS = framerate;
        System.out.println("Frame rate set on " + performance + " and " + framerate);
        PhysicGameWorld.initFrameRateSpecificData(Program.NORMAL_FPS);
        boolean withMusic = master.getWithMusic();
        if (withMusic) {
            MusicInGameController.withMusic = false;
            MusicInMenuController.withMusic = false;
        }
        else {
            MusicInGameController.withMusic = true;
            MusicInMenuController.withMusic = true;
        }
        boolean withSound = master.getWithSound();
        if (withSound) {
            AbstractSoundController.withSound = false;
        }
        else {
            AbstractSoundController.withSound = true;
        }
        String cameraTarget = master.getCameraTarget();
        if (cameraTarget == PreferencesDataConstants.ON_CROSSHAIR || cameraTarget.equals(PreferencesDataConstants.ON_CROSSHAIR)) {
            GameCamera.cameraInGameConcentrationOnCrosshair = true;
        }
        else     {
            GameCamera.cameraInGameConcentrationOnCrosshair = false;
        }
        String dPad = master.getDPad();
        if (dPad == PreferencesDataConstants.SMALL || dPad.contains(PreferencesDataConstants.SMALL)) PlayerControl.dPadSize = DPadSize.SMALL;
        else if (dPad == PreferencesDataConstants.MEDIUM || dPad.contains(PreferencesDataConstants.MEDIUM)) PlayerControl.dPadSize = DPadSize.MEDIUM;
        else {
            System.out.println(dPad + " is not " + PreferencesDataConstants.SMALL);
            PlayerControl.dPadSize = DPadSize.LARGE;
        }
        String developerMode = master.getDeveloperMode();
        if (developerMode == PreferencesDataConstants.ON || developerMode.contains(PreferencesDataConstants.ON)) {
            HeadsUpDisplay.showFPS = true;
            UserLevelsMenu.loadAllLevels = true;
        }
        else{
            HeadsUpDisplay.showFPS = false;
            UserLevelsMenu.loadAllLevels = false;
        }
        System.out.println("D pad must be set on: " + dPad + " and now: " + PlayerControl.dPadSize);
    }

    public void update(){
        if (menuType != newMenuType){
            createNewMenu();
            menuType = newMenuType;
        }
        else {
            menu.update(this, engine.mouseX, engine.mouseY);
        }
    }

    private void createNewMenu() {
        if (newMenuType == MenuType.OPTIONS){
            menu = new OptionsMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.MAIN){
            menu = new MainMenu(engine, graphics);
            //if (!withAudioInitAfterMenuStopped) musicInMenuController.startToPlay();
        }
        else if (newMenuType == MenuType.CAMPAIGN){
            menu = new CampaignMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.DO_YOU_WANT_TO_CHANGE_COLOR){
            menu = new DoYouWantChangeColorMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.BERET_COLOR_CHANGING){
            menu = new BeretColorChangingMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.INTRO_MENU){
            menu = new IntroMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.CONTINUE_LAST_GAME){
            menu = new ContinueLastGameMenu(engine, graphics);

        }
        else if (newMenuType == MenuType.HOW_TO_PLAY_FROM_OPTIONS){
            menu = new HowToPlayMenuFromOptions(engine, graphics);
        }
        else if (newMenuType == MenuType.HOW_TO_PLAY_FROM_CAMPAIGN){
            menu = new HowToPlayMenuFromCampaign(engine, graphics);
        }
        else if (newMenuType == MenuType.USER_LEVELS){
            menu = new UserLevelsMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.MAIN_LEVEL_LOADING){
            menu = new MainLevelLoadingMenu(engine, graphics, levelNumberToBeLoadedNext);
        }
        else if (newMenuType == MenuType.CREDITS){
            menu = new CreditsMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.END_CUTSCENE_MENU){
            menu = new EndCutSceneMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.WOULD_YOU_LIKE_TO_LEARN_HOW_TO_PLAY_MENU){
            menu = new DoYouWantToLearnHowToPlayMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.EXIT_MENU){
            menu = new ExitMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.YOU_NEED_TO_RELAUNCH_GAME){
            menu = new YouNeedToRelaunchMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.LEVEL_RESULTS_MENU){
            menu = new LevelResultsMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.DO_YOU_LIKE_OUR_GAME_MENU){
            menu = new DoYouLikeOurGameMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.FULL_SCREEN_ADD_MENU){
            menu = new AddFullScreenMenu(engine, graphics, userValue);
        }
        else if (newMenuType == MenuType.HELP_VARIANTS_MENU){
            menu = new HelpVariantsMenu(engine, graphics);
        }
        else if (newMenuType == MenuType.REWARDED_ADDS_MENU){
            menu = new AddRewardedMenu(engine, graphics, userValue);
        }
        else if (newMenuType == MenuType.SHOP){
            menu = new ShopMenu(engine, graphics, userValue, this);
        }
        else if (newMenuType == MenuType.USER_LEVEL_LOADING){
            boolean loadedFromEditor = true;
            if (additionalValue != null) {
                loadedFromEditor = additionalValue.booleanValue();
                System.out.println("Additional value " + (additionalValue != null) + " has value: " + additionalValue.booleanValue());
            }
            else System.out.println("Additional value is null");
            menu = new UserLevelLoadingMenu(engine, graphics, loadedFromEditor);
            gameMainController.startUserLevelLoading();
        }
        else if (newMenuType == MenuType.EDITOR_LOADING){
            menu = new EditorLoadingMenu(engine, graphics);
            gameMainController.startEditorLoading();
        }
    }

    public void draw(){
        graphics.beginDraw();
        menu.draw(graphics);
        graphics.endDraw();
        engine.imageMode(PConstants.CENTER);
        engine.image(graphics,engine.width/2, engine.height/2, engine.width, engine.height);
    }

    public void setNewMenu(MenuType menuType){
        newMenuType = menuType;
        setActive(true);
    }

    public PApplet getEngine() {
        return engine;
    }

    public void switchOnScreenShadower() {
    }
/*
    public boolean isLevelLoaded() {
        return gameMainController.isLevelLoaded();
    }*/

    public void loadLevel(boolean isEditor, boolean loadedFromEditor, boolean levelType) {
        additionalValue = new Boolean(loadedFromEditor);
        gameMainController.loadLevel(isEditor, loadedFromEditor, levelType);
        //gameMainController.loadLevel(isEditor, loadedFromEditor);
        setActive(false);
    }

    public void setUserValue(Object object){
        this.userValue = object;
    }

    public void setUserValue(Boolean object){
        this.additionalValue = object;
    }

    /*
    Boolean getAdditionalValue() {
        return additionalValue;
    }

    void setAdditionalValue(Boolean additionalValue) {
        this.additionalValue = additionalValue;
    }*/

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        //switchOffMusic();
    }

    public AbstractMenu getActualMenu() {
        return menu;
    }

    public boolean isMainMenu() {
        if (menu != null){
            if (menu.getClass() == MainMenu.class){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void switchOffSoundAndMusic() {
        soundInMenuController.stopAllAudio();
        musicInMenuController.stop();
    }
/*
    public void someGuiPressed(NES_GuiElement element){
        soundInMenuController.setAndPlayAudioForButtonPressed();
    }

    public void someGuiReleased(NES_GuiElement element){
        soundInMenuController.setAndPlayAudioForButtonReleased();
    }

    public void newLevelStartedButtonPressed(NES_GuiElement element){
        soundInMenuController.setAndPlayAudioForGameStarted();
    }*/

    public void pressedSoundOn(NES_GuiElement elementWasClicked) {
        soundInMenuController.setAndPlayAudioForButtonPressed(elementWasClicked);
    }

    public void releasedSoundOn(NES_GuiElement elementWasSelected) {
        soundInMenuController.setAndPlayAudioForButtonReleased(elementWasSelected);
    }

    public void switchOffMusic() {
        musicInMenuController.stop();
        //musicInMenuController = null;
    }

    public void backPressed() {
        if (isMainMenu()){
            System.out.println("End game");
            switchOffSoundAndMusic();
            setNewMenu(MenuType.EXIT_MENU);
        }
        else {
            if (menuType == MenuType.OPTIONS || menuType == MenuType.USER_LEVELS || menuType == MenuType.CAMPAIGN || menuType == MenuType.USER_LEVELS){
                setNewMenu(MenuType.MAIN);
            }
            else if (menuType == MenuType.HOW_TO_PLAY_FROM_OPTIONS) setNewMenu(MenuType.OPTIONS);
            else if (menuType == MenuType.HOW_TO_PLAY_FROM_CAMPAIGN) setNewMenu(MenuType.MAIN_LEVEL_LOADING);
            else if (menuType == MenuType.CREDITS) setNewMenu(MenuType.OPTIONS);
            else if (menuType == MenuType.INTRO_MENU || menuType == MenuType.CONTINUE_LAST_GAME || menuType == MenuType.DO_YOU_WANT_TO_CHANGE_COLOR) setNewMenu(MenuType.CAMPAIGN);

            else if (menuType == MenuType.SHOP) setNewMenu(MenuType.CONTINUE_LAST_GAME);
            else if (menuType == MenuType.REWARDED_ADDS_MENU) menu.backPressed(this);
            else {
                System.out.println("For this menu there are no data about backPressed action ");
            }
        }
    }

    public void stopMusic() {
        musicInMenuController.pausePlay();
    }

    public void resumeMusic() {
        musicInMenuController.resumePlay();
    }

    public void setLevelNumberToBeLoadedNext(int levelNumberToBeLoadedNext) {
        this.levelNumberToBeLoadedNext = levelNumberToBeLoadedNext;
    }

    public void launchMusicAndSwitchSoundOn() {
        if (musicInMenuController != null) {
            musicInMenuController.resumePlay();
            System.out.println("Music was resumed from launchMusicAndSwitchSoundOn function");
        }
        else {
            System.out.println("Music is null");
        }
        if (soundInMenuController != null) {
            // soundInMenuController.setActive(true);
            //System.out.println("Sound was resumed");
        }
        else {
            System.out.println("Sound is null");
        }
    }

    public void gameLaunchedSoundOn() {
        soundInMenuController.setAndPlayAudioForGameStarted();
    }

    public void initIntroMusic() {
        musicInMenuController.initIntroMusic();
        musicInMenuController.startToPlay();
    }

    public MusicInMenuController getMusicController() {
        return musicInMenuController;
    }

    public void backToPrevMenu(MenuType type, MenuType nextMenuThatCouldNotBeLoaded) {
        if (type == MenuType.REWARDED_ADDS_MENU){
            if (nextMenuThatCouldNotBeLoaded == MenuType.BERET_COLOR_CHANGING){
                System.out.println("The add can not be shown. We go back to the campaign menu");
                setNewMenu(MenuType.CAMPAIGN);
            }
            else {
                System.out.println("The add can not be shown and we dont know what the next menu must be shown. We go back to the main menu");
                setNewMenu(MenuType.MAIN);
            }
        }
        else {
            System.out.println("Data is wrong");
            setNewMenu(MenuType.MAIN);
        }
    }

    public void addSound(int sound) {
        System.out.println("Sound !");
        soundInMenuController.setAndPlayAudio(sound);
    }

    public void consoleInput(String s) {

    }

    public SoundInMenuController getSoundController() {
        return soundInMenuController;
    }


}
