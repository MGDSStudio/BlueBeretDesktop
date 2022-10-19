package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.loading.*;
import com.mgdsstudio.blueberet.androidspecific.MainActivity;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class AddRewardedMenu extends AddMenu{

    public AddRewardedMenu(PApplet engine, PGraphics graphics, Object userValue) {
        super(engine, userValue);
        this.type = MenuType.REWARDED_ADDS_MENU;
    }

    @Override
    protected void initAdd(PApplet engine){
        Program.iEngine.initRewardedAdd();
        //MainActivity mainActivity = (MainActivity) engine.getActivity();
        //mainActivity.stopMusicForAdd();
        //mainActivity.initRewardedAdd();

    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (addProgress){
            //MainActivity mainActivity = (MainActivity) gameMenusController.getEngine().getActivity();
            if (Program.iEngine.isRewardedAddStartedToUpload()){
                if (Program.iEngine.isPlayerMustBeRewarded()){
                    int value = Program.iEngine.getRewardValue();
                    rewardPlayerForAd(gameMenusController, value);
                }
            }
            else if (Program.iEngine.isRewardedAppIsDismissed()){
                int value = Program.iEngine.getRewardValue();
                rewardPlayerForAd(gameMenusController, value);
            }

            if (Program.iEngine.isRewardedAppFailedToUpload()){
                gameMenusController.backToPrevMenu(type, nextMenu);
                Program.iEngine.resetRewardAdd();
            }
        }
    }

    private void rewardPlayerForAd(GameMenusController gameMenusController, int value) {
        System.out.println("Try to reward the player for the add for " + value + "$");
        gameMenusController.setNewMenu(nextMenu);
        //MainActivity mainActivity = (MainActivity) gameMenusController.getEngine().getActivity();
        if (nextMenu == MenuType.SHOP){
            addMoneyToPlayer(value);
        }
        Program.iEngine.addToastMessage(getTextForToastMessage());
        Program.iEngine.resetRewardAdd();

        gameMenusController.stopMusic();
    }

    private void addMoneyToPlayer(int moneyForAdd) {
        try{

            PlayerDataSimpleLoadMaster playerDataLoadMaster = new PlayerDataSimpleLoadMaster();
            playerDataLoadMaster.loadData();
            int actualMoney = playerDataLoadMaster.getMoney();
            int newMoney = moneyForAdd+actualMoney;
            System.out.println("Try to resave money from prev value " + actualMoney + " to " + newMoney);
            PlayerDataSimpleSaveMaster playerDataSimpleSaveMaster = new PlayerDataSimpleSaveMaster();
            playerDataSimpleSaveMaster.setNewMoney(actualMoney, newMoney);
            playerDataSimpleSaveMaster.saveOnDisk();
            //PlayerDataSaveMaster master = new PlayerDataSaveMaster();
        }
        catch (Exception e){

        }
    }

    private String getTextForToastMessage(){
        String text = "";
        if (nextMenu == MenuType.BERET_COLOR_CHANGING){
            text = "You can change the color!";
            if (Program.LANGUAGE == Program.RUSSIAN){
                text = "Теперь Вы можете сменить цвет!";
            }
        }
        else if (nextMenu == MenuType.MAIN){
            text = "Thank you!";
            if (Program.LANGUAGE == Program.RUSSIAN){
                text = "Благодарим вас!";
            }
        }
        else if (nextMenu == MenuType.SHOP){
            text = "You have got " + ShopMenu.MONEY_FOR_ADD + " $! you can buy weapons and ammo ";
            if (Program.LANGUAGE == Program.RUSSIAN){
                text = "Вы заработали " + ShopMenu.MONEY_FOR_ADD +  " $ которые вы можете потратить на покупку оружия и боеприпасов ";
            }
        }

        return text;
    }

    public void backPressed(GameMenusController gameMenusController) {
        if (Program.OS == Program.ANDROID){
            //MainActivity mainActivity = (MainActivity) Program.engine.getActivity();
            if (Program.iEngine.isRewardedAppFailedToUpload()){
                System.out.println(" *** Can not upload the add ! ***");
                try {
                    //MenuType newMenu = nextMenu;
                    gameMenusController.setNewMenu(nextMenu);
                }
                catch (Exception e){
                    gameMenusController.setNewMenu(MenuType.MAIN);
                }
            }
            else {
                System.out.println(" *** Rewarded add was uploaded, but not shown ***");
                if (Program.iEngine.isRewardedAppIsDismissed()) {
                    System.out.println(" *** Rewarded add was uploaded, but not shown. It is dismissed: " + Program.iEngine.isRewardedAppIsDismissed() +"  ***");
                    try {
                        gameMenusController.setNewMenu(nextMenu);
                    }
                    catch (Exception e) {
                        gameMenusController.setNewMenu(MenuType.MAIN);
                    }
                }
            }
        }
        //System.out.println("Nothing implemented for back button");
    }
}
