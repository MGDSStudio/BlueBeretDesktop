package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.EndGameDeterminer;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ContinueLastGameMenu extends AbstractMenu{
    public static boolean mustBeShopBlocked = true;
    private String CONTINUE_FROM_LAST_ZONE = "CONTINUE FROM LAST ZONE";
    private String RESTART_LEVEL = "RESTART LEVEL";
    private String GO_TO_SHOP = "IN GUN STORE";
    private String TITLE = "- CONTINUE LAST GAME -";

    public ContinueLastGameMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.CONTINUE_LAST_GAME;

        initLanguageSpecific();
        init(engine, graphics);

    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            CONTINUE_FROM_LAST_ZONE = "ПРОДОЛЖИТЬ С ПОСЛЕДНЕЙ ЗОНЫ";
            RESTART_LEVEL = "НАЧАТЬ УРОВЕНЬ СНАЧАЛА";
           GO_TO_SHOP = "В МАГАЗИН";
            TITLE = "В ПРЕДЫДУЩЕЕ МЕНЮ";
        }
        else{

        }
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        int upperY = engine.height/3;
        initTitle(engine, upperY, TITLE );
        upperY+= (guiElements.get(guiElements.size()-1).getHeight()*4);
        String [] names = new String[]{CONTINUE_FROM_LAST_ZONE, RESTART_LEVEL, GO_TO_SHOP, GO_TO_PREVIOUS_MENU};
        fillGuiWithCursorButtons(engine,upperY, names,TO_DOWN);
        EndGameDeterminer endGameDeterminer = new EndGameDeterminer();
        boolean playerCanContinue=  true;
        if (endGameDeterminer.didPlayerEndGame()){
            playerCanContinue = false;
        }
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == GO_TO_SHOP){
                if (mustBeShopBlocked) {
                    guiElement.block(true);
                }
            }
            else if (!playerCanContinue && guiElement.getName() == CONTINUE_FROM_LAST_ZONE){
                guiElement.block(true);
            }
        }
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.CAMPAIGN);
                }
                else if (element.getName() == GO_TO_SHOP){
                    gameMenusController.switchOnScreenShadower();
                    gameMenusController.setNewMenu(MenuType.SHOP);
                }
                else if (element.getName() == CONTINUE_FROM_LAST_ZONE){
                    gameLaunchedSoundOn(gameMenusController);
                    PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
                    master.loadData();
                    int nextZone = master.getNextZone();
                    Program.actualRoundNumber = nextZone;
                    System.out.println("Launched zone: " + nextZone);
                    gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
                    gameMenusController.setLevelNumberToBeLoadedNext(nextZone);
                    //gameMenusController.switchOnScreenShadower();
                }
                else if (element.getName() == RESTART_LEVEL){
                    gameLaunchedSoundOn(gameMenusController);
                    PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
                    master.loadData();
                    int actualLevel = master.getFirstZoneForActualLevel();
                    Program.actualRoundNumber = actualLevel;
                    gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
                    gameMenusController.setLevelNumberToBeLoadedNext(actualLevel);
                    System.out.println("Launched level: " + actualLevel);
                    //gameMenusController.switchOnScreenShadower();
                }
            }
        }
    }


}
