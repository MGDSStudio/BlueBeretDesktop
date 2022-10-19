package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gameprocess.GameProcess;


import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;

import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.menu.AbstractMenu;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PConstants;

public class GameMainController {
    public final static boolean releaseMenuSystem = true;
    private final PApplet engine;
    //private Menu menu;
    private GameProcess gameProcess;
    private GameMenusController gameMenusController;
    public static final int GAME_PROCESS = Program.GAME_PROCESS;
    public static final int LEVELS_EDITOR = Program.LEVELS_EDITOR;
    public static final int SOME_MENU = Program.SOME_MENU;
    private int previousStatement = Program.gameStatement;
    private boolean levelLoadingOnThisFrame;
    private boolean statementChanged = false;
    private boolean atLeastOneMenuScreenWasShown = true;
    private AddByBackPressingMenuController addByBackPressingMenuController;
    //public static int statement;

    public GameMainController(PApplet engine){
        this.engine = engine;
        HUD_GraphicData.init();
        InWorldObjectsGraphicData.init();
        if (releaseMenuSystem) gameMenusController = new GameMenusController(this);
        DebugVersionCreatingMaster.init(true);
        initPerformanceData();
    }

    private void initPerformanceData() {
        if (engine.sketchRenderer() == PConstants.P3D || engine.sketchRenderer() == PConstants.P2D){
            if (engine.sketchRenderer() == PConstants.P3D){
                engine.hint(PConstants.DISABLE_DEPTH_TEST);

            }
            engine.hint(PConstants.DISABLE_TEXTURE_MIPMAPS);
            engine.hint(PConstants.DISABLE_OPENGL_ERRORS);
            System.out.println("Open GL errors will not be checked");
        }
    }

    private void gameOrEditorUpdating(){
        if (releaseMenuSystem){
            gameOrEditorReleaseSystemUpdating();
        }
        else {
            System.out.println("it was remade to new menu system");
        }
    }

    private void gameOrEditorReleaseSystemUpdating() {
        if (!gameMenusController.isActive()) gameProcess.game();

        //if (gameMenusController.isActive()gameProcess != null ) gameProcess.game();
    }

    private void updateMenu(){
        if (releaseMenuSystem){
            updateReleaseMenuSystem();
        }
        else {
            System.out.println("Already changed");
        }
    }

    //Completed!
    private void updateReleaseMenuSystem() {
        if (gameMenusController.isActive()) gameMenusController.update();
        else System.out.println("Menu is not active");
        if (gameProcess != null && !levelLoadingOnThisFrame) {
            if (Program.gameStatement == Program.GAME_PROCESS) {
                gameProcess.switchOffMusic();
            }
            gameProcess = null;
            System.gc();
        }

    }

    public void update(){
         if (Program.gameStatement == Program.GAME_PROCESS || Program.gameStatement == Program.LEVELS_EDITOR){
             gameOrEditorUpdating();
         }
         else {
             updateMenu();
         }
        Program.updateLongPressingStatement(Program.engine.mousePressed, Editor2D.prevMousePressedStatement );
        Editor2D.prevMousePressedStatement = Program.engine.mousePressed;
        previousStatement = Program.gameStatement;
        levelLoadingOnThisFrame = false;
    }

    private void clearMemoryAfterGame() {
        try {
            System.out.println("In memory object frame " + Program.engine.g.getCache(Program.objectsFrame));
            System.out.println("In memory background frame " + Program.engine.g.getCache(Program.backgroundFrame));
            Program.objectsFrame.dispose();
            Program.backgroundFrame.dispose();
            Program.engine.g.removeCache(Program.objectsFrame);
            Program.engine.g.removeCache(Program.backgroundFrame);
            System.out.println("This frames are not more in the memory " + Program.engine.g.getCache(Program.backgroundFrame) + "; " + Program.engine.g.getCache(Program.objectsFrame));

        }
        catch (Exception e1){
            System.out.println("PGraphics can not be deleted from the main engine");
        }
        finally {
            System.gc();
        }
    }

    private void clearMemory(){

    }

    private void drawForRelease(){
        if (Program.isActualStatementMenu()) {
            if (gameMenusController != null) {
                gameMenusController.draw();
            }
        }
    }

    /*
    private void drawForOldMenuSystem(){
        if (Program.isActualStatementMenu()) {
            if (menu != null) {
                menu.draw();
            }
        } else if (Program.gameStatement == Program.GAME_PROCESS || Program.gameStatement == Program.LEVELS_EDITOR) {
            if (gameProcess == null) {
                if (Program.jumpFromRedactorToGame){
                    gameProcess = new GameProcess(this, true);
                }
                else gameProcess = new GameProcess(this, true);
                clearMemory(Program.gameStatement);
            }

        }
    }*/

    public void draw(){
        if (releaseMenuSystem){
            drawForRelease();
        }
        else {

            //drawForOldMenuSystem();
        }
    }

    public void clearMemory(byte newStatement){
        if (newStatement == Program.GAME_PROCESS){
            //if (menu !=null) menu = null;
            System.gc();
        }
    }

    public void contactPreSolveInterrupt(Contact arg0, Manifold arg1) {
        gameProcess.contactPreSolveInterrupt(arg0, arg1);
    }

    public void contactStartedSolveInterrupt(Contact arg0, Manifold arg1) {
        gameProcess.contactStartedSolveInterrupt(arg0, arg1);
    }

    public void addPreContact(Contact arg0, Manifold arg1) {
        gameProcess.addPreContact(arg0, arg1);
    }

    public void onFlick( float x, float y, float px, float py, float v){
        gameProcess.onFlick(x, y, px, py, v);
    }

    public void onPinch(float x, float y, float value){
        gameProcess.onPinch(x, y, value);
    }

    public void backPressed() {
        if (!releaseMenuSystem){
            if (Program.gameStatement == Program.LEVELS_EDITOR)
                Program.gameStatement = Program.SOME_MENU;
            else if (Program.gameStatement == Program.GAME_PROCESS) {
                if (Program.levelLaunchedFromRedactor) {
                    Program.jumpFromRedactorToGame = true;
                    Program.gameStatement = Program.LEVELS_EDITOR;
                }
                else Program.gameStatement = Program.SOME_MENU;
            }
            else if (Program.gameStatement == Program.USER_LEVELS_MENU) Program.gameStatement = Program.SOME_MENU;
        }
        if (gameProcess != null){
            gameProcess.backPressed();
        }
        if (releaseMenuSystem) {
            if (Program.gameStatement == Program.LEVELS_EDITOR) {
                Program.gameStatement = Program.SOME_MENU;
                gameMenusController.setNewMenu(MenuType.MAIN);
            }
            else if (Program.gameStatement == Program.GAME_PROCESS) {
                if (gameProcess.isLevelType() == ExternalRoundDataFileController.USER_LEVELS) {
                    if (gameProcess.isLoadedFromEditor()) {
                        gameMenusController.setUserValue(new Boolean(gameProcess.isLoadedFromEditor()));
                        gameProcess = null;
                        gameMenusController.setNewMenu(MenuType.EDITOR_LOADING);
                        Program.gameStatement = Program.SOME_MENU;
                        System.out.println("It was loaded from editor");
                    } else {
                        gameMenusController.setUserValue(new Boolean(gameProcess.isLoadedFromEditor()));
                        Program.gameStatement = Program.SOME_MENU;
                        gameMenusController.setNewMenu(MenuType.USER_LEVELS);
                        gameProcess = null;
                        System.out.println("It was loaded from menu");
                    }
                }
                else {
                    Program.gameStatement = Program.SOME_MENU;


                    if (gameProcess.hasPlayerWon()) {
                        gameMenusController.setNewMenu(MenuType.LEVEL_RESULTS_MENU);
                    }
                    else {
                        if (addByBackPressingMenuController == null) addByBackPressingMenuController = new AddByBackPressingMenuController();
                        if (addByBackPressingMenuController.mustBeFullScreenAddShown()){
                            MenuType nextMenuAfterAdd = MenuType.CONTINUE_LAST_GAME;
                            gameMenusController.setUserValue(nextMenuAfterAdd);
                            gameMenusController.setNewMenu(MenuType.FULL_SCREEN_ADD_MENU);
                        }
                        else{
                            gameMenusController.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                        }
                        stopMusic();
                        gameProcess.stopMusic();
                        addByBackPressingMenuController.incrementLaunchesNumber();

                    }
                    gameProcess = null;
                }
            }
            else {
                gameMenusController.backPressed();
            }
        }
    }



    public PApplet getEngine() {
        return engine;
    }

    public void startUserLevelLoading() {

    }

    public void startEditorLoading() {

    }

    public boolean isLevelLoaded() {
        if (gameProcess == null) return false;
        else {
            return gameProcess.isLevelLoaded();
        }
        //return false;
    }

    public void startGameAndCloseMenuSystem() {

    }

    public void loadLevel(boolean isEditor, boolean loadedFromEditor, boolean levelType) {
        draw();
        if (isEditor) {
            Program.gameStatement = Program.LEVELS_EDITOR;
        }
        else Program.gameStatement = Program.GAME_PROCESS;
        gameProcess = new GameProcess(this, loadedFromEditor, levelType);
        System.out.println("Start level loading from editor " + loadedFromEditor);
        levelLoadingOnThisFrame = true;
        gameMenusController.setActive(false);
    }

    public void jumpToMenu(MenuType menu) {
        draw();
        gameMenusController.setNewMenu(menu);
        Program.gameStatement = Program.SOME_MENU;
        if (gameProcess != null) {
            gameProcess = null;
        }
    }

    public AbstractMenu getActualMenu() {
        return gameMenusController.getActualMenu();
    }

    public GameMenusController getMenusController() {
        return gameMenusController;
    }

    public void onResume() {
        Program.appWasHidden = true;
        System.out.println("By next level loading the graphic must be recreated");
    }

    public boolean isSomeMenuShown() {
        if (gameMenusController != null) {
            if (gameMenusController.isActive()) {
                System.out.println("Menu " + gameMenusController.getActualMenu().getType());
                return true;
            } else return false;
        }
        else return false;
    }

    public void stopMusic() {
        if (gameMenusController != null) {
            if (isSomeMenuShown()) {
                if (gameMenusController.isActive()) {
                    gameMenusController.stopMusic();
                }
            }
        }
        if (isGameProcessActual()){
            gameProcess.pauseMusic();
        }
    }

    public void resumeMusic() {
        if (gameMenusController != null) {
            if (isSomeMenuShown()) {
                if (gameMenusController.isActive()) {
                    System.out.println("Music was resumed from game main controller");
                    gameMenusController.resumeMusic();
                }
            }
        }
        if (isGameProcessActual()){
            gameProcess.resumeMusic();
        }
    }

    public boolean isGameProcessActual() {
        if (gameProcess != null){
            if (Program.gameStatement == Program.GAME_PROCESS){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void continueTimers() {
        if (gameProcess != null){
            gameProcess.continueTimers();
        }
    }

    public void pauseTimers() {
        if (gameProcess != null){
            gameProcess.pauseTimers();
        }
    }

    public void consoleInput(String s) {
        if (gameProcess != null){
            gameProcess.consoleInput(s);
        }
        else if (gameMenusController != null){
            gameMenusController.consoleInput(s);
        }
    }
}
