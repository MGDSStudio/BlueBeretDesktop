package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ListButton;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataConstants;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataController;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataLoadMaster;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataSaveMaster;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class OptionsMenu extends AbstractMenu implements PreferencesDataConstants {

    private String OPTIONS = "- OPTIONS -";
    private String HOW_TO_PLAY = "HOW TO PLAY";
    private String CREDITS = "CREDITS";
    private String HELP_PROJECT = "HELP US";
    private String BACK_TO_MAIN_MENU = "BACK TO MAIN MENU";

    private static boolean mustBeRestarted;

    public OptionsMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.OPTIONS;
        initLanguageSpecific();
        init(engine, graphics);
        loadSavedValues(engine);
    }

    protected void initLanguageSpecific() {
        //if (GO_TO_PREVIOUS_MENU != null) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            OPTIONS = "- НАСТРОЙКИ -";
            HOW_TO_PLAY = "КАК ИГРАТЬ";
            CREDITS = "АВТОРЫ";
            BACK_TO_MAIN_MENU = "НАЗАД В ГЛАВНОЕ МЕНЮ";
            HELP_PROJECT = "ПОМОЧЬ ПРОЕКТУ";
        } else {
            /*
            OPTIONS = "- OPTIONS -";
            HOW_TO_PLAY = "HOW TO PLAY";
            CREDITS = "CREDITS";
            BACK_TO_MAIN_MENU = "BACK TO MAIN MENU";*/
        }
        //}
    }

    private void loadSavedValues(PApplet engine) {
        if (PreferencesDataController.dataFileExists()){
            PreferencesDataLoadMaster master = new PreferencesDataLoadMaster(engine);
            master.loadData(this);
        }
        else {
            System.out.println("File with data was not founded");
            PreferencesDataSaveMaster master = new PreferencesDataSaveMaster(engine);
            master.saveData();
            loadSavedValues(engine);
        }
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        int upperY = engine.height/4;

        initTitle(engine, upperY, OPTIONS);
        upperY+= (guiElements.get(guiElements.size()-1).getHeight()*2);
        initListButtons(engine, upperY);
        NES_GuiElement lastElement = guiElements.get(guiElements.size()-1);
        int step = lastElement.getUpperY()-guiElements.get(guiElements.size()-2).getUpperY();
        upperY=lastElement.getUpperY()+step*2;
        initButtons(engine, upperY);
    }



    private void initButtons(PApplet engine, int lowerY) {
        String [] names = new String[]{HOW_TO_PLAY, CREDITS, HELP_PROJECT, BACK_TO_MAIN_MENU};
        fillGuiWithCursorButtons(engine, lowerY, names,TO_DOWN);
    }

    private void initListButtons(PApplet engine, int upperY) {
        String [] names = {SOUND,MUSIC, PERFORMANCE ,ANTI_ALIASING, CAMERA_TARGET, LIGHTS, D_PAD, DEVELOPER_MODE};
        String [][] functions = new String[names.length][5];

        functions[0][0] = ON;
        functions[0][1] = OFF;

        functions[1][0] = ON;
        functions[1][1] = OFF;

        functions[2][0] = MEDIUM;
        functions[2][1] = HIGH;
        functions[2][2] = LOW;

        functions[3][0] = PIXEL_ART;
        functions[3][1] = x2;
        functions[3][2] = x4;
        functions[3][3] = x8;

        functions[4][0] = ON_CROSSHAIR;
        functions[4][1] = ON_PLAYER;

        functions[5][0] = ON;
        functions[5][1] = OFF;

        functions[6][0] = LARGE;
        functions[6][1] = MEDIUM;
        functions[6][2] = SMALL;

        functions[7][0] = OFF;
        functions[7][1] = ON;

        int [] defaultValues = new int[names.length];
        for (int i = 0; i < defaultValues.length; i++) defaultValues[i] = 0;
        fillGuiWithListButtons(engine, names, functions, defaultValues, upperY, TO_DOWN);

        blockAntiAliasing();
        initLanguageSpecific();
        setLanguageSpecificButtonNames();
        setLanguageSpecificActions();
    }

    private void setLanguageSpecificActions() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            for (NES_GuiElement guiElement : guiElements) {
                if (guiElement instanceof NES_ListButton) {
                    NES_ListButton button = (NES_ListButton) guiElement;
                    ArrayList<String> actions = new ArrayList<>();
                    if (guiElement.getName().equals(SOUND)) {
                        actions.add("ВКЛ");
                        actions.add("ОТКЛ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    } else if (guiElement.getName().equals(MUSIC)) {
                        actions.add("ВКЛ");
                        actions.add("ОТКЛ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    } else if (guiElement.getName().equals(PERFORMANCE)) {
                        actions.add("ОТЛИЧНАЯ");
                        actions.add("НАИЛУЧШАЯ");
                        actions.add("ХОРОШАЯ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    } else if (guiElement.getName().equals(ANTI_ALIASING)) {
                        actions.add("ПИКСЕЛЬ АРТ");
                        actions.add("X2");
                        actions.add("X4");
                        actions.add("X8");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    } else if (guiElement.getName().equals(CAMERA_TARGET)) {
                        actions.add("НА ПРИЦЕЛЕ");
                        actions.add("НА ПЕРСОНАЖЕ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    }
                    else if (guiElement.getName().equals(LIGHTS)) {
                        actions.add("ВКЛ");
                        actions.add("ОТКЛ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    }
                    else if (guiElement.getName().equals(D_PAD)) {
                        actions.add("БОЛЬШОЙ");
                        actions.add("СРЕДНИЙ");
                        actions.add("МАЛЫЙ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    }
                    else if (guiElement.getName().equals(DEVELOPER_MODE)) {
                        actions.add("ОТКЛ");
                        actions.add("ВКЛ");
                        button.setAnotherTextToBeDrawnAsActions(actions);
                    }
                }
            }
        }
    }

    private void setLanguageSpecificButtonNames() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            for (NES_GuiElement guiElement : guiElements) {
                if (guiElement.getName().equals(SOUND)){
                    guiElement.setAnotherTextToBeDrawnAsName("ЗВУК");
                }
                else if (guiElement.getName().equals(MUSIC)){
                    guiElement.setAnotherTextToBeDrawnAsName("МУЗЫКА");
                }
                else if (guiElement.getName().equals(PERFORMANCE)){
                    guiElement.setAnotherTextToBeDrawnAsName("ГРАФИКА");
                }
                else if (guiElement.getName().equals(ANTI_ALIASING)){
                    guiElement.setAnotherTextToBeDrawnAsName("СГЛАЖИВАНИЕ");
                }
                else if (guiElement.getName().equals(CAMERA_TARGET)){
                    guiElement.setAnotherTextToBeDrawnAsName("КАМЕРА");
                }
                else if (guiElement.getName().equals(LIGHTS)){
                    guiElement.setAnotherTextToBeDrawnAsName("3D ОСВЕЩЕНИЕ");
                }
                else if (guiElement.getName().equals(D_PAD)){
                    guiElement.setAnotherTextToBeDrawnAsName("ДЖОЙСТИК");
                }
                else if (guiElement.getName().equals(DEVELOPER_MODE)){
                    guiElement.setAnotherTextToBeDrawnAsName("РЕЖ. РАЗРАБ.");
                }
            }
        }
    }


    private void blockAntiAliasing() {
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName().equals(ANTI_ALIASING) || guiElement.getName()==ANTI_ALIASING){
                NES_ListButton buttonWithCursor = (NES_ListButton) guiElement;
                buttonWithCursor.block(true);
            }
        }
    }

/*
    private String[][] getButtonsStrings() {
        String [][] strings = {{},{}};
        return strings;
    }

    private int[] getDefaultValuesFromCache() {
        int [] values = {0,0,0,0,0,0};
        return values;
    }*/

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == BACK_TO_MAIN_MENU) {
                    if (mustBeGameStartedNew(gameMenusController)){
                        gameMenusController.setNewMenu(MenuType.YOU_NEED_TO_RELAUNCH_GAME);
                    }
                    else {
                        gameMenusController.setNewMenu(MenuType.MAIN);
                    }
                    saveAndApplyOptionsData(gameMenusController.getEngine(), gameMenusController);

                }
                else if (element.getName() == CREDITS){
                    gameMenusController.setNewMenu(MenuType.CREDITS);
                    saveAndApplyOptionsData(gameMenusController.getEngine(), gameMenusController);
                }
                else if (element.getName() == HOW_TO_PLAY){
                    gameMenusController.setNewMenu(MenuType.HOW_TO_PLAY_FROM_OPTIONS);
                    saveAndApplyOptionsData(gameMenusController.getEngine(), gameMenusController);
                }
                else if (element.getName() == HELP_PROJECT){
                    gameMenusController.switchOffMusic();
                    gameMenusController.setNewMenu(MenuType.HELP_VARIANTS_MENU);
                }
            }
        }
    }

    private boolean mustBeGameStartedNew(GameMenusController gameMenusController) {
        String actualRenderer = gameMenusController.getEngine().sketchRenderer();
        System.out.println("Actual renderer: " + actualRenderer);
        if (actualRenderer == PConstants.P3D){
            if (didPlayerSelect2D()){
                return true;
            }
            else return false;
        }
        else {
            if (didPlayerSelect2D()){
                return false;
            }
            else return true;
        }
    }

    private boolean didPlayerSelect2D() {
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == LIGHTS){
                NES_ListButton listButton = (NES_ListButton) guiElement;
                String selectedString = listButton.getSelectedString();
                System.out.println("3D selected as " + selectedString);
                if (selectedString.contains("ON")){
                    return false;
                }
                else return true;
            }
        }
        System.out.println("Button for " + LIGHTS + " is not founded");
        return false;
    }

    private void saveAndApplyOptionsData(PApplet engine, GameMenusController gameMenusController) {
        PreferencesDataSaveMaster master = new PreferencesDataSaveMaster(engine, this);
        master.saveData();
        gameMenusController.applyPreferences();
    }

    public NES_GuiElement getGuiByName(String name){
        for (NES_GuiElement element : guiElements){
            if (element.getName() == name || element.getName().equals(name)){
                return element;
            }
        }
        return null;
    }
}
