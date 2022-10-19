package com.mgdsstudio.blueberet.menusystem.menu;


import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;


public class HelpVariantsMenu extends AbstractMenuWithTwoButtonsAndFrame{
    private final boolean withPayPal = false;
    private final String URL_TO_CROWD = "https://boomstarter.ru/projects/1094528/200903?token=f0faa59e";
    private final String URL_TO_HOMEPAGE = "https://mgdsstudio.itch.io/";
    private final String MAIL = "mgdsstudio@yahoo.com";
    private final String MY_PAY_PAL_NUMBER= "No Pay Pal";

    private static String VISIT_CROWD_NAME = "EXPLORE CROWDFUNDING";
    private static String VISIT_HOMEPAGE_NAME = "VISIT HOMEPAGE";
    private static String I_WANT_TO_DONATE = "I WANT TO DONATE";
    private static String I_WATCH_ADDS = "I WATCH ADVERTISEMENT";
    private static String I_WANT_TO_PARTICIPATE = "I WANT TO participate";
    private static String NO_WAY = "NO WAY";

    private boolean emailShown, payPalShown;
    private final static String [] BUTTON_NAMES = new String [] {NO_WAY, I_WATCH_ADDS, I_WANT_TO_PARTICIPATE, VISIT_HOMEPAGE_NAME, I_WANT_TO_DONATE};



    public HelpVariantsMenu(PApplet engine, PGraphics graphics) {
        super(engine, graphics, MenuType.HELP_VARIANTS_MENU, BUTTON_NAMES);
        blockSomeButtons();
        translateButtons();
    }

    private void blockSomeButtons() {
            for (NES_GuiElement guiElement : guiElements){
                if (!Program.withAdds) {
                    if (guiElement.getName() == I_WATCH_ADDS) {
                        guiElement.block(true);
                    }
                }
                if (guiElement.getName() == I_WANT_TO_DONATE){
                    if (guiElement.getName() == I_WANT_TO_DONATE) {
                        guiElement.block(true);
                    }
                }

            }


    }

    private void translateButtons() {
        if (Program.LANGUAGE == Program.RUSSIAN){
            ArrayList <NES_GuiElement> elementsToBeAligned = new ArrayList<>();
            for (NES_GuiElement element : guiElements){
                /*
                VISIT_CROWD_NAME = "К САЙТУ ПРОЕКТА";
            VISIT_HOMEPAGE_NAME = "НА НАШ САЙТ";
            I_WANT_TO_PARTICIPATE = "НАПИСАТЬ НАМ ПИСЬМО";
            NO_WAY = "Я ПЕРЕДУМАЛ";
            titleName = "- РАЗВИТИЕ ПРОЕКТА -";
            MESSAGE_TEXT = "БЛАГОДАРИМ ВАС ЗА ПРОЯВЛЕННЫЙ ИНТЕРЕС К ИГРЕ. ВЫ МОЖЕТЕ ПОСЕТИТЬ НАШ САЙТ, ЧТОБЫ УЗНАТЬ БОЛЬШЕ О НАШЕМ НОВОМ ИГРОВОМ ДВИЖКЕ RPG ENGINE И О НАШИХ ИГРАХ. ВЫ МОЖЕТЕ ПОЗНАКОМИТЬСЯ СО СТРАНИЧКОЙ НАШЕГО КРАУДФАНДИНГОВОГО ПРОЕКТА, И ПОМОЧЬ НАМ. БЫТЬ МОЖЕТ ВЫ ХОТИТЕ СДЕЛАТЬ ПАРУ УРОВНЕЙ ИЛИ ПРИСЛАТЬ СВОИ ГРАФИЧЕСКИЕ НАРАБОТКИ, ЧТОБЫ МЫ ВКЛЮЧИЛИ ИХ В ИГРУ В ВИДЕ ТЕКСТУР, ПРОТИВНИКОВ, ПРЕДМЕТОВ ИЛИ РЕКЛАМЫ? СМЕЛО ПИШИТЕ НАМ!. ИЛИ ПРОСТО ПОСМОТРИТЕ РЕКЛАМУ РАДИ НАС";

                 */
                if (element.getName() == VISIT_CROWD_NAME) {
                    element.setAnotherTextToBeDrawnAsName("К САЙТУ ПРОЕКТА");
                    elementsToBeAligned.add(element);
                }
                else if (element.getName() == VISIT_HOMEPAGE_NAME) {
                    element.setAnotherTextToBeDrawnAsName("НА НАШ САЙТ");
                    elementsToBeAligned.add(element);
                }
                else if (element.getName() == I_WANT_TO_PARTICIPATE) {
                    element.setAnotherTextToBeDrawnAsName("НАПИСАТЬ НАМ ПИСЬМО");
                    elementsToBeAligned.add(element);
                }
                else if (element.getName() == NO_WAY) {
                    element.setAnotherTextToBeDrawnAsName("Я ПЕРЕДУМАЛ");
                    elementsToBeAligned.add(element);
                }
                else if (element.getName() == I_WANT_TO_DONATE){
                    element.setAnotherTextToBeDrawnAsName("Я ПОЖЕРТВУЮ НА ПРОЕКТ");
                    elementsToBeAligned.add(element);
                }
                else if (element.getName() == I_WATCH_ADDS) {
                    element.setAnotherTextToBeDrawnAsName("ПОСМОТРЮ РЕКЛАМУ");
                    elementsToBeAligned.add(element);
                }
            }
            alignButtonsAlongY(elementsToBeAligned);
        }
    }

    @Override
    protected void buttonPressed(GameMenusController gameMenusController, String name) {
        if (name == NO_WAY){
            gameMenusController.setNewMenu(MenuType.MAIN);
        }
        else if (name == VISIT_CROWD_NAME){
            try {
                gameMenusController.getEngine().link(URL_TO_CROWD);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (name == VISIT_HOMEPAGE_NAME){
            try {
                gameMenusController.getEngine().link(URL_TO_HOMEPAGE);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (name == I_WATCH_ADDS){
            MenuType nextMenuAfterAdd = MenuType.MAIN;
            gameMenusController.switchOffMusic();
            gameMenusController.setUserValue(nextMenuAfterAdd);
            if (Program.OS == Program.ANDROID) {
                gameMenusController.setNewMenu(MenuType.REWARDED_ADDS_MENU);
            }
        }
        else if (name == I_WANT_TO_PARTICIPATE){
            loadMailClient(gameMenusController.getEngine());
        }
        else if (name == I_WANT_TO_DONATE){
            initDonation(gameMenusController.getEngine());
        }
    }

    private void initDonation(PApplet engine) {
        if (Program.OS == Program.DESKTOP){

        }
        else if (Program.OS == Program.ANDROID){
            System.out.println("You can not donate via pay pal!");
            /*
            ClipboardManager clipboard = (ClipboardManager) Program.engine.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = new ClipData.Item(MY_PAY_PAL_NUMBER);
            String pathWasCopied = "Wallet was copied. You can donate via Pay pal!" ;
           ClipDescription clipDescription = new ClipDescription(pathWasCopied, new String[] {MAIL});
            ClipData clipData = new ClipData(clipDescription, item);
            clipboard.setPrimaryClip(clipData);
            System.out.println("Wallet number was copied");
            String toastText = "Our wallet account "+ MY_PAY_PAL_NUMBER+ " is copied to the clipboard. Use it to send us money for the development continues. ";
            if (Program.LANGUAGE == Program.RUSSIAN) toastText = "Адрес кошелька Pay Pal скопирован в буфер обмена. Вставьте его в поле адресат в вашем приложении Pay Pal чтобы пожертвовать нам на проект. ";
            Program.iEngine.addToastMessage(toastText);
            */
        }
        if (!payPalShown) {
            for (NES_GuiElement element : guiElements) {
                if (element.getName() == I_WANT_TO_DONATE) {

                    //element.setAnotherTextToBeDrawnAsName("PAY PAL: " + MY_PAY_PAL_NUMBER + "\n" +  MY_PAY_PAL_NUMBER);
                    payPalShown = true;
                }
            }
        }
    }

    private void loadMailClient(PApplet engine) {
        System.out.println("Ty to copy");
        if (Program.OS == Program.DESKTOP){

        }
        else if (Program.OS == Program.ANDROID){
            String pathWasCopied = "URL was copied. You can open your email client to write us!" ;
            Program.iEngine.copyDataToClipboard(MAIL, pathWasCopied);
            /*
            ClipboardManager clipboard = (ClipboardManager) Program.engine.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = new ClipData.Item(MAIL);
            ClipDescription clipDescription = new ClipDescription(pathWasCopied, new String[] {MAIL});
            ClipData clipData = new ClipData(clipDescription, item);
            clipboard.setPrimaryClip(clipData);*/


            System.out.println("E Mail was copied to the clipboard");
            String toastText = "Our E-mail is copied to the clipboard. Paste it in the recipient text field of your e-mail client to write us.";
            if (Program.LANGUAGE == Program.RUSSIAN) toastText = "Адрес нашей электронной почты скопирован в буфер обмена. Вставьте его в поле адресат в вашем почтовом клиенте чтобы отправить нам сообщение. ";
            Program.iEngine.addToastMessage(toastText);
        }
        if (!emailShown) {
            for (NES_GuiElement element : guiElements) {
                if (element.getName() == I_WANT_TO_PARTICIPATE) {
                    element.setAnotherTextToBeDrawnAsName(MAIL);
                    emailShown = true;
                }
            }
        }
    }

    @Override
    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            /*VISIT_CROWD_NAME = "К САЙТУ ПРОЕКТА";
            VISIT_HOMEPAGE_NAME = "НА НАШ САЙТ";
            I_WANT_TO_PARTICIPATE = "НАПИСАТЬ НАМ ПИСЬМО";
            NO_WAY = "Я ПЕРЕДУМАЛ";*/
            titleName = "- РАЗВИТИЕ ПРОЕКТА -";
            MESSAGE_TEXT = "БЛАГОДАРИМ ВАС ЗА ПРОЯВЛЕННЫЙ ИНТЕРЕС К ИГРЕ. ВЫ МОЖЕТЕ ПОСЕТИТЬ НАШ САЙТ, ЧТОБЫ УЗНАТЬ БОЛЬШЕ О НАШИХ ИГРАХ. БЫТЬ МОЖЕТ ВЫ ХОТИТЕ СДЕЛАТЬ ПАРУ УРОВНЕЙ ИЛИ ПРИСЛАТЬ СВОИ ГРАФИЧЕСКИЕ НАРАБОТКИ, ЧТОБЫ МЫ ВКЛЮЧИЛИ ИХ В ИГРУ? СМЕЛО ПИШИТЕ НАМ!. ИЛИ ПРОСТО ПОСМОТРИТЕ РЕКЛАМУ РАДИ НАС. ";
        }
        else{
            MESSAGE_TEXT = "THANK YOU FOR YOUR INTEREST TO OUR VIDEO GAME. YOU CAN VISIT OUR HOMEPAGE TO KNOW MORE ABOUT OUR VIDEO GAMES. MAYBE YOU WANT TO CREATE SOME LEVELS FOR THE GAME OR SEND US YOUR GRAPHIC SOURCES (TEXTURES, ENEMIES, ITEMS)? WRITE US IMMEDIATELY! OR YOU CAN SIMPLE WATCH AN ADVERTISEMENT. IT IS ALSO HELPFUL. ";
            titleName = "- PROJECT EVOLUTION -";
        }
    }


}
