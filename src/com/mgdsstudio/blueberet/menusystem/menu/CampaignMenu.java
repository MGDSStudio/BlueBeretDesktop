package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

public class CampaignMenu extends AbstractMenu{
    private static String CONTINUE_LAST_GAME = "CONTINUE LAST GAME";
    private static String START_NEW_GAME = "START NEW GAME";
    private static String BACK_TO_MAIN_MENU = "BACK TO MAIN MENU";
    private static String CHANGE_BERET_COLOR = "CHANGE BERET COLOR";
    private static String titleName = "- COMPANY -";
    private boolean menuBlocked;
    private MenuToGameTransfer menuToGameTransfer;
    private boolean withMenuHiding = true;
    private boolean withBeretColorSelection = true;


    public CampaignMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.CAMPAIGN;
        //initColorSelectorIfNeeds(engine);
        initLanguageSpecific();
        init(engine, graphics);
    }



    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            CONTINUE_LAST_GAME = "ПРОДОЛЖИТЬ ПОСЛЕДНЮЮ ИГРУ";
            START_NEW_GAME = "НАЧАТЬ НОВУЮ ИГРУ";
            BACK_TO_MAIN_MENU = "В ПРЕДЫДУЩЕЕ МЕНЮ";
            CHANGE_BERET_COLOR = "ВЫБРАТЬ ЦВЕТ БЕРЕТА";
            titleName = "- КАМПАНИЯ -";
        }
        else{

        }
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        int upperY = engine.height/3;
        initTitle(engine, upperY, titleName);
        upperY+= (guiElements.get(guiElements.size()-1).getHeight()*4);
        String [] names;
        if (wasGameAlreadyStarted()){
            withBeretColorSelection = true;
            if (hasPlayerEnoughtLifes()){
                if ( withBeretColorSelection){
                    names = new String[]{CONTINUE_LAST_GAME,  START_NEW_GAME, CHANGE_BERET_COLOR, BACK_TO_MAIN_MENU};
                }
                //else names = new String[]{CONTINUE_LAST_GAME,  START_NEW_GAME, BACK_TO_MAIN_MENU};
                else names = new String[]{CONTINUE_LAST_GAME,  START_NEW_GAME, CHANGE_BERET_COLOR, BACK_TO_MAIN_MENU};
            }
            //else names = new String[]{START_NEW_GAME, BACK_TO_MAIN_MENU};
            else names = new String[]{START_NEW_GAME, CHANGE_BERET_COLOR, BACK_TO_MAIN_MENU};
        }
        else {
            withBeretColorSelection = false;
            names = new String[]{START_NEW_GAME, CHANGE_BERET_COLOR, BACK_TO_MAIN_MENU};
        }
        fillGuiWithCursorButtons(engine,upperY, names,TO_DOWN);
        hideBeretColorButton(engine);
    }

    private void hideBeretColorButton(PApplet engine) {
        boolean mustBeBlocked = false;
        if (wasGameAlreadyStarted()){
        }
        else {
            if (introGraphicWasAlreadyDeveloped(engine)) mustBeBlocked  = true;
        }
        if (mustBeBlocked) {
            {
                for (NES_GuiElement element : guiElements){
                    if (element.getName() == CHANGE_BERET_COLOR){
                        element.block(true);
                    }
                }
            }
        }
    }

    private boolean introGraphicWasAlreadyDeveloped(PApplet engine) {
        boolean val = IntroMenu.areCutScenesAlreadyDeveloped(engine);
        return val;
    }


    private boolean hasPlayerEnoughtLifes() {
        PlayerProgressLoadMaster playerProgressLoadMaster = new PlayerProgressLoadMaster();
        playerProgressLoadMaster.loadData();
        int restLifes = playerProgressLoadMaster.getRestLifes();
        if (restLifes>0){
            return true;
        }
        else return false;
    }



    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (!menuBlocked) {
            super.update(gameMenusController, mouseX, mouseY);
            for (NES_GuiElement element : guiElements) {
                if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                    if (element.getName() == BACK_TO_MAIN_MENU) {
                        gameMenusController.setNewMenu(MenuType.MAIN);
                    } else if (element.getName() == START_NEW_GAME) {
                        gameLaunchedSoundOn(gameMenusController);
                        if (withMenuHiding){
                            menuBlocked = true;
                            menuToGameTransfer = new MenuToGameTransfer((NES_ButtonWithCursor) element);
                        }
                        else {
                            startGame(gameMenusController);
                        }
                    } else if (element.getName() == CONTINUE_LAST_GAME) {
                        gameMenusController.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                    }
                    else if (element.getName() == CHANGE_BERET_COLOR){
                        //if (Program.withAdds)
                            gameMenusController.setNewMenu(MenuType.DO_YOU_WANT_TO_CHANGE_COLOR);

                    }
                }
            }
        }
        else {
            if (menuToGameTransfer.statement == MenuToGameTransfer.END){
                startGame(gameMenusController);
            }
            else {
                menuToGameTransfer.update();
            }
        }
    }

    private void startGame(GameMenusController gameMenusController) {
        gameMenusController.setNewMenu(MenuType.INTRO_MENU);
    }

    @Override
    public void draw(PGraphics graphic){
        if (withMenuHiding) {
            if (menuBlocked) {
                setCursorVisibility();
            }
        }
        super.draw(graphic);
    }

    private void setCursorVisibility() {
        if (menuToGameTransfer.mustBeCursorShown()){
            NES_GuiElement gui_element = menuToGameTransfer.getButton();
            NES_ButtonWithCursor nes_buttonWithCursor = (NES_ButtonWithCursor) gui_element;
            nes_buttonWithCursor.setActualShownByBlinking(false);
        }
        else {
            NES_GuiElement gui_element = menuToGameTransfer.getButton();
            NES_ButtonWithCursor nes_buttonWithCursor = (NES_ButtonWithCursor) gui_element;
            nes_buttonWithCursor.setActualShownByBlinking(true);
        }
    }

    /*
    private void setCursorVisibility() {
        for (NES_GuiElement gui_element : guiElements){
            if (gui_element.getActualStatement() == NES_GuiElement.PRESSED || gui_element.getActualStatement() == NES_GuiElement.RELEASED){
                System.out.println("Cursor must be hidden");
                NES_ButtonWithCursor nes_buttonWithCursor = (NES_ButtonWithCursor) gui_element;
                int x = nes_buttonWithCursor.getCursorPosX();
                int y = nes_buttonWithCursor.getCursorPosY();
                nes_buttonWithCursor.setActualShownByBlinking(false);
            }
            else {
                NES_ButtonWithCursor nes_buttonWithCursor = (NES_ButtonWithCursor) gui_element;
                System.out.println("Cursor must be shown");
                int x = nes_buttonWithCursor.getCursorPosX();
                int y = nes_buttonWithCursor.getCursorPosY();
                nes_buttonWithCursor.setActualShownByBlinking(true);
            }
        }
    }*/

    class MenuToGameTransfer{
        final static int  CURSOR_HIDDING_1 = 1;
        final static int CURSOR_SHOWING_1 = 2;
        final static int  CURSOR_HIDDING_2 = 3;
        final static int CURSOR_SHOWING_2 = 4;
        final static int  CURSOR_HIDDING_3 = 5;
        final static int CURSOR_SHOWING_3 = 6;
        //final static int CURSOR_SHOWING_3 = 6;
        //final static int  CURSOR_HIDDING_4 = 7;
        final static int OVERSHADOWING = 7;
        final static int END = OVERSHADOWING+1;
        private int statement = 0;


        private final int VISIBILITY_TIME = 650;
        private final Timer blinkTimer;
        //private final Timer hidingTimer;

        private NES_ButtonWithCursor button;

        public MenuToGameTransfer(NES_ButtonWithCursor button) {
            blinkTimer = new Timer(VISIBILITY_TIME);
            statement = CURSOR_HIDDING_1;
            this.button = button;
        }

        int getStatement() {
            return statement;
        }

        void update(){
            if (statement < OVERSHADOWING){
                if (blinkTimer.isTime()){
                    statement++;
                    blinkTimer.stop();
                    blinkTimer.setNewTimer(VISIBILITY_TIME);
                    System.out.println("Timer was restarted to statement " + statement);
                }
            }
            else statement = END;
        }

        public boolean mustBeCursorShown() {
            if (statement == CURSOR_SHOWING_1 || statement == CURSOR_SHOWING_2 || statement == CURSOR_SHOWING_3){
                return true;
            }
            else return false;
        }

        NES_GuiElement getButton(){
            return  button;
        }
    }





}
