package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DoYouLikeOurGameMenu extends AbstractMenuWithTwoButtonsAndFrame{
    private static String YES = "YES, LET"+ "'" +"S CONTINUE!";
    private static String NO = "NO, IT IS NOT FOR ME";
    private final Timer timer;
    private boolean toastAdded;
    {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            YES = "ДА, ПРОДОЛЖАЕМ!";
            NO = "НЕТ, Я ВЫРОС ИЗ ТАКИХ ИГР";
            titleName = "- УРОВЕНЬ 1 ПРОЙДЕН -";
            MESSAGE_TEXT = "ПОЗДРАВЛЯЕМ! ВЫ ПРОШЛИ ПЕРВЫЙ УРОВЕНЬ. ЭТО ДЛЯ ВАС ОКАЗАЛОСЬ НЕПРОСТО, НО ВЫ ОТЛИЧНО СПРАВИЛИСЬ! ХОТИТЕ ПРОДОЛЖЕНИЯ?";
        }
        else{
            MESSAGE_TEXT = "CONGRATULATION! YOU HAVE COMPLETED THE FIRST LEVEL. IT WAS NOT SIMPLE FOR YOU BUT YOU WAS A COOL SOLDIER. DO YOU WANT MORE?";
            titleName = "- LEVEL 1 COMPLETED -";
        }
    }
    private static String [] BUTTON_NAMES = new String [] {NO, YES};

    public DoYouLikeOurGameMenu(PApplet engine, PGraphics graphics) {

        super(engine,graphics, MenuType.DO_YOU_LIKE_OUR_GAME_MENU, BUTTON_NAMES);
        timer = new Timer(1000);
    }

    @Override
    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            YES = "ДА, ПРОДОЛЖАЕМ!";
            NO = "НЕТ, Я ВЫРОС ИЗ ТАКИХ ИГР";
            for (int i = 0; i < BUTTON_NAMES.length; i++){
                if (BUTTON_NAMES[i].contains("YES")){
                    System.out.print("Name was translated from: " + BUTTON_NAMES[i]);
                    BUTTON_NAMES[i] = YES;
                    System.out.println(" to: " + BUTTON_NAMES[i]);
                }
                else if (BUTTON_NAMES[i].contains("NO")){
                    BUTTON_NAMES[i] = NO;
                }
            }

            titleName = "- УРОВЕНЬ 1 ПРОЙДЕН -";
            MESSAGE_TEXT = "ПОЗДРАВЛЯЕМ! ВЫ ПРОШЛИ ПЕРВЫЙ УРОВЕНЬ. ЭТО ДЛЯ ВАС ОКАЗАЛОСЬ НЕПРОСТО, НО ВЫ ОТЛИЧНО СПРАВИЛИСЬ! ХОТИТЕ ПРОДОЛЖЕНИЯ?";
        }
        else{
            MESSAGE_TEXT = "CONGRATULATION! YOU HAVE COMPLETED THE FIRST LEVEL. IT WAS NOT SIMPLE FOR YOU BUT YOU WAS A COOL SOLDIER. DO YOU WANT MORE?";
            titleName = "- LEVEL 1 COMPLETED -";
        }
    }





    @Override
    protected void buttonPressed(GameMenusController gameMenusController, String name){
        if (name == NO) {
            gameMenusController.setNewMenu(MenuType.MAIN);

        } else if (name == YES) {
            gameMenusController.setNewMenu(MenuType.HELP_VARIANTS_MENU);
        }
        else System.out.println("Pressed button : " + name);
    }

    /*
    private void startGame(GameMenusController gameMenusController) {
        gameMenusController.switchOffMusic();
        PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
        playerProgressSaveMaster.writeDefaultValuesWithoutSaving();
        playerProgressSaveMaster.saveOnDisk();
        gameMenusController.setLevelNumberToBeLoadedNext(playerProgressSaveMaster.getNextLevel());
        gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
        System.out.println("Data was saved on disk");
    }
*/
}
