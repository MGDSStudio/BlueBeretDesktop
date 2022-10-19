package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.*;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ListButton;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextLabel;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class AbstractMenu {
    public final static int PIXELS_ALONG_X_FOR_NES = 254;
    protected static String GO_TO_PREVIOUS_MENU;
    protected PGraphics graphics;
    protected final static boolean TO_DOWN = true, TO_UP = false;
    protected final static float NORMAL_Y_DISTANCE_COEF_FOR_GUI = 2.4f;
    protected final ArrayList<NES_GuiElement> guiElements = new ArrayList<>();
    private ArrayList <NES_GuiElement> guiElementsToBeAligned;
    protected MenuType type;
    private boolean startBlackScreenShown;
    private static long memory;
    protected static Object dataToBeSaved;

    //protected abstract void init(PApplet engine, PGraphics graphics);

    protected void init(PApplet engine, PGraphics graphics) {
        this.graphics = graphics;
        initLanguageSpecificForBackButton();
    }

    private void initLanguageSpecificForBackButton() {
        //if (GO_TO_PREVIOUS_MENU != null) {
            if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
                GO_TO_PREVIOUS_MENU = "В ПРЕДЫДУЩЕЕ МЕНЮ";
            } else {
                GO_TO_PREVIOUS_MENU = "BACK TO PREVIOUS MENU";
            }
        //}
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        for (NES_GuiElement element : guiElements){
            element.update(mouseX, mouseY);
            if (element.getActualStatement() == NES_GuiElement.RELEASED){
                releasedSoundOn(gameMenusController, element);
            }
            else if (element.getActualStatement() == NES_GuiElement.PRESSED){
                pressedSoundOn(gameMenusController, element);
            }
        }
        if (Program.debug){
            if (Program.engine.frameCount%30 == 1){
                memory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
            }
        }
    }

    private void releasedSoundOn(GameMenusController gameMenusController, NES_GuiElement element) {
        gameMenusController.pressedSoundOn(element);
    }

    private void pressedSoundOn(GameMenusController gameMenusController, NES_GuiElement element) {
        gameMenusController.releasedSoundOn(element);
    }

    protected void gameLaunchedSoundOn(GameMenusController gameMenusController) {
        gameMenusController.switchOffMusic();
        gameMenusController.gameLaunchedSoundOn();
    }


    public void draw(PGraphics graphic){
        drawBackground(graphic);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() != NES_GuiElement.HIDDEN){
                element.draw(graphic);
            }
        }
        if (!startBlackScreenShown){
            drawBlackScreen(graphic);
        }
        if (Program.debug){
            graphic.fill(255);
            graphic.text(("RAM: "+memory),graphic.width/20, graphic.width/20);
        }
    }

    private void drawBlackScreen(PGraphics graphic) {
        alignButtonsAlongY(guiElementsToBeAligned);
        graphic.background(0);
        startBlackScreenShown = true;
    }

    private void drawBackground(PGraphics graphic){
        graphic.background(0);
    }

    protected void fillGuiWithCursorButtons(PApplet engine, int startPosY, String [] names, boolean direction){
        fillGuiWithCursorButtons(engine, startPosY,  names,  direction, NORMAL_Y_DISTANCE_COEF_FOR_GUI);
        /*for (String string : names){
            guiElements.add(new NES_ButtonWithCursor(engine.width/2, startPosY, NES_ButtonWithCursor.BUTTON_NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, string,  graphics));
            if (direction == TO_DOWN) {
                startPosY+= (guiElements.get(0).getHeight() * NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
            else{
                startPosY-=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
        }*/
    }

    protected void fillGuiWithCursorButtons(PApplet engine, int startPosY, String [] names, boolean direction, float relativeDistanceBetweenGui){
        for (String string : names){
            guiElements.add(new NES_ButtonWithCursor(engine.width/2, startPosY, NES_ButtonWithCursor.BUTTON_NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, string,  graphics));
            if (direction == TO_DOWN) {
                startPosY+= (guiElements.get(0).getHeight() * relativeDistanceBetweenGui);
            }
            else{
                startPosY-=(guiElements.get(0).getHeight()*relativeDistanceBetweenGui);
            }
        }
    }

    protected void fillGuiWithCursorButtons(PApplet engine, int startPosY, String [] names, int [] values, boolean direction){
        for (int i  =0 ; i < names.length; i++){
            NES_ButtonWithCursor buttonWithCursor = new NES_ButtonWithCursor(engine.width/2, startPosY, NES_ButtonWithCursor.BUTTON_NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, names[i],  graphics);
            buttonWithCursor.setUserData(new Integer(values[i]));
            guiElements.add(buttonWithCursor);
            if (direction == TO_DOWN) {
                startPosY+= (guiElements.get(0).getHeight() * NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
            else{
                startPosY-=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
        }
    }

    protected void fillGuiWithTextLabels(PApplet engine, int startPosY, String[] names,  boolean direction) {
        for (String string : names){
            guiElements.add(new NES_TextLabel(engine.width/2, startPosY, NES_ButtonWithCursor.BUTTON_NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, string, graphics));
            if (direction == TO_DOWN) {
                startPosY+=(guiElements.get(guiElements.size()-1).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
            else{
                startPosY-=(guiElements.get(guiElements.size()-1).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
        }
    }

    protected void fillGuiWithTextLabelsOld(PApplet engine, int startPosY, String[] names,  boolean direction) {
        for (String string : names){
            guiElements.add(new NES_TextLabel(engine.width/2, startPosY, NES_ButtonWithCursor.BUTTON_NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, string, graphics));
            if (direction == TO_DOWN) {
                startPosY+=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
            else{
                startPosY-=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
        }
    }

    protected void fillGuiWithListButtons(PApplet engine, String[] names, String[][] functions, int[] defaultValues, int startPosY, boolean direction) {
        for (int i = 0; i < names.length; i++){
            ArrayList <String> actions  = new ArrayList<>();
            for (int j = 0; j < functions[i].length; j++){
                if (functions[i][j] != null) actions.add(functions[i][j]);
            }
            NES_ListButton button = new NES_ListButton(engine.width/2, startPosY, NES_ListButton.NORMAL_WIDTH, NES_GuiElement.NORMAL_HEIGHT, names[i], actions, defaultValues[i], graphics);
            guiElements.add(button);
            if (direction == TO_DOWN) {
                startPosY+=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
            else{
                startPosY-=(guiElements.get(0).getHeight()*NORMAL_Y_DISTANCE_COEF_FOR_GUI);
            }
        }
    }

    protected void initTitle(PApplet engine, int upperY, String title){
        String [] names = new String[]{title};
        fillGuiWithTextLabels(engine, upperY, names, TO_UP);
    }

    protected void alignButtonsAlongY(ArrayList <NES_GuiElement> elements){
        if (elements == null){
            elements = guiElements;
        }
        else System.out.println("Not all elements must be aligned");
        int longestString = 0;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                int actualWidth = elements.get(i).getTextWidth();
                if (actualWidth > longestString) {
                    longestString = actualWidth;
                }
            }
        }
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                elements.get(i).setWidth(longestString);
            }
        }
        if (guiElementsToBeAligned != null) guiElementsToBeAligned = null;
    }



    protected void alignButtons(ArrayList <NES_GuiElement> elements){
        startBlackScreenShown = false;
        guiElementsToBeAligned = elements;
    }

    protected void dispose(PApplet engine){
        System.gc();
    }

    public MenuType getType() {
        return type;

    }

    public static boolean wasGameAlreadyStarted() {
        if (PlayerProgressLoadMaster.wasCompanyStarted()) return true;
        else return false;
    }


    public PGraphics getGraphic() {
        return graphics;
    }

    public void backPressed(GameMenusController gameMenusController) {
        System.out.println("Nothing implemented for back button");
    }
}
