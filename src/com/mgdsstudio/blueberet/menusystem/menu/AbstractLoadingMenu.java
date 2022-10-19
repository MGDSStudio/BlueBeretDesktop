package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextLabel;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class AbstractLoadingMenu extends AbstractMenu{
    public final static boolean LOADING_AS_EDITOR = true;
    public final static boolean LOADING_AS_LEVEL = false;

    private final String LEVEL_START_BUTTON_NAME = " ";
    private int centerX, centerY;
    private int centerWidth, centerHeight;
    //private final String labelText;
    private final int MIN_TIME_FOR_MENU = 400;
    private Timer showingTimer;
    protected boolean loadingMode;
    protected boolean menuLaunchedFromEditor;

    public AbstractLoadingMenu(PApplet engine, PGraphics graphics) {
        init(engine, graphics);
    }


    protected void addText(String labelText) {
        String text = getPleaseWaitTextForActualLanguage();
        NES_TextLabel pleaseWaitText = new NES_TextLabel(centerX, centerY+centerHeight*2, centerWidth, centerHeight, text, graphics);
        guiElements.add(pleaseWaitText);
        NES_TextLabel loadingText = new NES_TextLabel(centerX, pleaseWaitText.getUpperY()-pleaseWaitText.getHeight()*2, centerWidth, centerHeight, labelText, graphics);
        guiElements.add(loadingText);
    }

    private String getPleaseWaitTextForActualLanguage() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            return "ПОЖАЛУЙСТА  ПОДОЖДИТЕ";
        }
        return "PLEASE  WAIT";
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine,graphics);
        centerX = engine.width/2;
        centerY = engine.height*4/13;
        centerWidth = engine.width/12;
        centerHeight = (int) (centerWidth/2.5f);
        NES_TextLabel levelStartButton = new NES_TextLabel(engine.width/2, engine.height/2, engine.width, engine.height, LEVEL_START_BUTTON_NAME, graphics);
        guiElements.add(levelStartButton);
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (showingTimer == null){
            showingTimer = new Timer(MIN_TIME_FOR_MENU);
        }
        else if (showingTimer.isTime()) {
            closeMenuAndGoToGame(gameMenusController);
        }
        else {
            super.update(gameMenusController, mouseX, mouseY);
            for (NES_GuiElement element : guiElements) {
                if (element.getActualStatement() == NES_GuiElement.PRESSED) {

                }
            }
        }
    }

    protected abstract void closeMenuAndGoToGame(GameMenusController gameMenusController) ;

    public void draw(PGraphics graphics){
        super.draw(graphics);
        //System.out.println("Draw loading screen");
    }

    protected void setMenuLaunchedFromEditor(boolean menuLaunchedFromEditor) {
        this.menuLaunchedFromEditor = menuLaunchedFromEditor;
    }


}
