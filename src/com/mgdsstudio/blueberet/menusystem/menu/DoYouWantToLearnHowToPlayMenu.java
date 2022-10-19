package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressSaveMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DoYouWantToLearnHowToPlayMenu extends AbstractMenuWithTwoButtonsAndFrame{
    private static String YES = "YES, TELL ME MORE";
    private static String NO = "START THE GAME";
    //private final static String [] BUTTON_NAMES = new String [] {YES, NO};
    private final static String [] BUTTON_NAMES = new String [] {NO, YES};

    public DoYouWantToLearnHowToPlayMenu(PApplet engine, PGraphics graphics) {
        super(engine,graphics, MenuType.WOULD_YOU_LIKE_TO_LEARN_HOW_TO_PLAY_MENU, BUTTON_NAMES);
        /*type = MenuType.WOULD_YOU_LIKE_TO_LEARN_HOW_TO_PLAY_MENU;
        initLanguageSpecific();
        init(engine, graphics);
        initFrame(engine);*/
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            YES = "ДА, ЧТО ТАМ С УПРАВЛЕНИЕМ?";
            NO = "САМ РАЗБИРУСЬ. В БОЙ!";
            titleName = "- КАК ИГРАТЬ -";
            MESSAGE_TEXT = "ПОСКОЛЬКУ ТЫ ИГРАЕШЬ ВПЕРВЫЕ НАСТОЯТЕЛЬНО РЕКОМЕНДУЕТСЯ РАЗОБРАТЬСЯ В УПРАВЛЕНИИ. ЖЕЛАЕШЬ ЛИ ТЫ УЗНАТЬ, КАК УПРАВЛЯТЬ ПЕРСОНАЖЕМ В ИГРЕ?";
        }
        else{
            MESSAGE_TEXT = "IF YOU PLAY AT THE FIRST TIME IT IS RECOMMENDABLE TO LEARN HOW TO PLAY. DO YOU WANT TO GET TO KNOW THE GAME CONTROL SYSTEM?";
            titleName = "- DO YOU KNOW HOW TO PLAY? -";
        }
    }

    protected void buttonPressed(GameMenusController gameMenusController, String name){
        if (name == NO) {
            gameLaunchedSoundOn(gameMenusController);
            startGame(gameMenusController);

        } else if (name == YES) {
            gameMenusController.setNewMenu(MenuType.HOW_TO_PLAY_FROM_CAMPAIGN);
        }
    }

    private void startGame(GameMenusController gameMenusController) {
        gameMenusController.switchOffMusic();
        PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
        playerProgressSaveMaster.writeValuesWithoutSaving();
        playerProgressSaveMaster.saveOnDisk();
        gameMenusController.setLevelNumberToBeLoadedNext(playerProgressSaveMaster.getNextLevel());
        gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
        System.out.println("Data was saved on disk");
    }
}
