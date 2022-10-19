package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.menu.ShopMenu;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

import java.util.ArrayList;


public class AfterObjectBuyingMessageController {
    private WeaponType weaponTypeWasBought;
    private int collectableObjectNumberWasBought = -1;
    private String textToBeAdded;
    private ShopMenuController shopMenuController;
    private ShopMenu shopMenu;
    private String OK = " OK! ";

    private boolean guiWasCreated;

    int nextStatement;

    public AfterObjectBuyingMessageController(WeaponType weaponTypeWasBought, ShopMenuController shopMenuController, ShopMenu shopMenu, int nextStatement) {
        this.weaponTypeWasBought = weaponTypeWasBought;
        this.shopMenuController = shopMenuController;
        this.shopMenu = shopMenu;
        this.nextStatement = nextStatement;
    }

    public AfterObjectBuyingMessageController(int collectableObjectNumberWasBought, ShopMenuController shopMenuController, ShopMenu shopMenu, int nextStatement) {
        this.collectableObjectNumberWasBought = collectableObjectNumberWasBought;
        this.shopMenuController = shopMenuController;
        this.shopMenu = shopMenu;
        this.nextStatement = nextStatement;
    }


    void update(GameMenusController controller){
        if (!guiWasCreated) {
            if (shopMenu.getTextFrameForShop().isClicked(controller.getEngine()) || shopMenu.getTextFrameForShop().isFullApeared()) {
                createGui(controller.getEngine());
                shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
            }
        }
         {
            String buttonName = shopMenuController.getReleasedButtonName();
            if (buttonName != null) {
                shopMenuController.setStatement(nextStatement);
                shopMenu.createTextFrameForShop(TextFrameForShop.NORMAL_HEIGHT);
                if (nextStatement == ShopMenuController.ShopControllerStatements.WEAPONS_LIST) {
                    shopMenuController.createWeaponsListSubmenu(controller.getEngine());
                }
                else if (nextStatement == ShopMenuController.ShopControllerStatements.AMMO_TYPE){
                    shopMenuController.createSubmenuForStatement(nextStatement);
                }
                shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
            }
        }

    }

    private void createGui(PApplet engine) {

        ArrayList <String> names = new ArrayList<>();
        names.add(OK);
        //names.add(OK);
        //names.add( I_NEED_AMMO);
        //names.add(NO_I_NEED_SOMETHING_ELSE);
        //if (Program.OS == Program.ANDROID) names.add(I_DONT_HAVE_MONEY);
        //names.add(I_DONT_HAVE_MONEY);
        //names.add(NOTHING);
        EightPartsFrameImage frameImage = shopMenu.getTextFrameForShop().getFrame();
        float mainFrameLowerY = frameImage.getLeftUpperCorner().y+frameImage.getHeight();
        Rectangular textArea = shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().getTextArea();
        float stepToText = shopMenu.getTextFrameForShop().getGapToFrame();
        float buttonsAreaUpperLine = stepToText*2+textArea.getLeftUpperY()+textArea.getHeight();
        float height = mainFrameLowerY-buttonsAreaUpperLine-stepToText;
        float centerX = frameImage.getLeftUpperCorner().x+frameImage.getWidth()/2;
        float centerY = buttonsAreaUpperLine+height/2;
        Rectangular rectangular = new Rectangular(new Vec2(centerX, centerY), frameImage.getWidth()/1.6f, height);

        //shopMenuController.addSingleButtonInZone(OK, rectangular);
        shopMenuController.addSingleButton(OK);
        //shopMenu.getActionPanel().getFrame()
        System.out.println("GUI was created ");
        guiWasCreated = true;
    }

    private String getText() {
        String text;
        if (weaponTypeWasBought != null) {
            text = getTextForWeapon(weaponTypeWasBought);
        }
        else {
            text = getTextForItem(collectableObjectNumberWasBought);
        }
        return text;
    }

    private String getTextForItem(int collectableObjectNumberWasBought) {
        System.out.println("Object code: " + collectableObjectNumberWasBought + " for shotgun " + AbstractCollectable.BULLETS_FOR_SHOTGUN);
        String text = "Very well! IT IS USEFUL FOR YOUR ADVENTURES. "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+  " WOULD YOU LIKE TO BUY SOMETHING ELSE? ";
        if (Program.LANGUAGE == Program.RUSSIAN) text = "ОЧЕНЬ ХОРОШО! ЭТО ТЕБЕ ОДНОЗНАЧНО ПРИГОДИТСЯ. " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " КУПИШЬ ЕЩЕ ЧЕГО- НИБУДЬ В ДОРОГУ? ";
        if (collectableObjectNumberWasBought == AbstractCollectable.SYRINGE){
            text = "BE CAREFUL WITH THAT! THIS IS A POWERFUL THING. WHEN YOU USE IT EVERYTHING AROUND WILL SEEM SLOWER. YOUR ACCURACY AND RAPIDITY WILL INCREASE. ";

        }
        else if (collectableObjectNumberWasBought == AbstractCollectable.SMALL_MEDICAL_KIT){
            text = "THIS IS A SWEET DRINK. TASTE IT AND YOU FEEL WELL. WHEN YOU DRINK THIS IN A BATTLE YOU RECOVERY 25% OF YOUR MAX LIFE. DO NOT DRINK TOO MUCH! ";
        }
        else if (collectableObjectNumberWasBought == AbstractCollectable.MEDIUM_MEDICAL_KIT){
            text = "THIS IS A SIMPLE MEDICINE. WHEN YOU USE IT YOU RECOVERY 50% OF YOUR MAX LIFE. IT IS VERY HELPFUL! ";
        }
        else if (collectableObjectNumberWasBought == AbstractCollectable.MEDIUM_MEDICAL_KIT){
            text = "THIS IS A FULL MEDICAL KIT TO HEAL ALL THE WOUNDS YOU HAVE. WHEN MY FATHER PARTICIPATED IN THE WAR AT MY HOMELAND OUR MEDICAL ORDERLIES CARRIED THIS BOXES TO HEAL OUR WARRIORS ON THE BATTLEFIELDS! USE IT AND YOU RECOVERY YOUR LIFE";
        }
        else if (collectableObjectNumberWasBought == AbstractCollectable.BULLETS_FOR_SHOTGUN){

            text = "TAKE IT";

        }
        return text;
    }

    private String getTextForWeapon(WeaponType weaponType){
        String text = "Very well! IT IS A VERY GOOD WEAPON TO KILL YOUR ENEMIES!";
        //text = "YES! I'M GLAD TO SELL IT TO YOU! IT IS A POWERFUL WEAPON! IF YOU ALREADY USED A REVOLVER THIS HANDGUN IS MUCH MORE BETTER! IT HAS 12 BULLETS IN THE BOX MAGAZINE AND HIGHER RATE OF FIRE THAN THE REVOLVER. BUT YOU CAN NOT USE BULLETS FOR THE REVOLVER IN THIS HANDGUN. IF YOU WANT TO CHANGE YOUR WEAPON FROM THE HANDGUN TO THE REVOLVER YOU NEED TO OPEN YOUR WEAPON PANEL THROUGH A LONG CLICK. THEN YOU NEED A LONG PRESS ON THE ICON WITH YOUR HANDGUN TO SELECT THE REVOLVER.";

        if (weaponTypeWasBought == WeaponType.HANDGUN){
            //text = "YES! I'M GLAD TO SELL IT TO YOU! IT IS A POWERFUL WEAPON! IF YOU ALREADY USED A REVOLVER THIS HANDGUN IS MUCH MORE BETTER! IT HAS 12 BULLETS IN THE BOX MAGAZINE AND HIGHER RATE OF FIRE THAN THE REVOLVER. BUT YOU CAN NOT USE BULLETS FOR THE REVOLVER IN THIS HANDGUN. IF YOU WANT TO CHANGE YOUR WEAPON FROM THE HANDGUN TO THE REVOLVER YOU NEED TO OPEN YOUR WEAPON PANEL THROUGH A LONG CLICK. THEN YOU NEED A LONG PRESS ON THE ICON WITH YOUR HANDGUN TO SELECT THE REVOLVER. FOR YOU AS FOR MY BEST CUSTOMER I GIVE YOU A FULL MAGAZINE. ";
            //
            text = "IT IS A POWERFUL WEAPON! IT HAS 12 BULLETS IN THE BOX MAGAZINE AND HIGHER RATE OF FIRE THAN THE REVOLVER. BUT YOU CAN NOT USE BULLETS FOR THE REVOLVER IN THIS HANDGUN. FOR YOU AS FOR MY BEST CUSTOMER I GIVE YOU A FULL MAGAZINE. ";
            if (Program.LANGUAGE == Program.RUSSIAN){
                text = "ЭТО ТО ЧТО ТЕБЕ НУЖНО! У ЭТОГО ОРУЖИЯ 12 ПАТРОНОВ В МАГАЗИНЕ И БОЛЕЕ ВЫСОКИЙ ТЕМП СТРЕЛЬБЫ, ЧЕМ У РЕВОЛЬВЕРА. НО ТЫ НЕ СМОЖЕШЬ ИСПОЛЬЗОВАТЬ ПУЛИ ОТ РЕВОЛЬВЕРА В ЭТОМ ОРУЖИИ. КАК МОЕМУ ЛУЧШЕМУ КЛИЕНТУ, Я ДАРЮ ПОЛНОСТЬЮ ЗАРЯЖЕННЫЙ МАГАЗИН";
            }
        }
        else {
            System.out.println("Text for bought weapon must be implemented");
        }

        return text;
    }

    void createButtons(PApplet engine){

        //shopMenu.getActionPanel().clearGui();
        /*
        try {
            for (int i = (shopMenuController.guiElements.size()-1); i>= 0; i--){
                shopMenuController.guiElements.get(i).block(true);
                shopMenuController.guiElements.remove(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String text = getText();
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text);
*/
        /*
        ArrayList<String> names = new ArrayList<>();
        names.add(SubmenuAction.BACK);
        try {
            for (int i = (shopMenuController.guiElements.size()-1); i>= 0; i--){
                shopMenuController.guiElements.get(i).block(true);
                shopMenuController.guiElements.remove(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        EightPartsFrameImage frameImage = shopMenu.getTextFrameForShop().getFrame();

        float mainFrameLowerY = frameImage.getLeftUpperCorner().y+frameImage.getHeight();
        Rectangular textArea = shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().getTextArea();
        float stepToText = shopMenu.getTextFrameForShop().getGapToFrame();

        float buttonsAreaUpperLine = stepToText*2+textArea.getLeftUpperY()+textArea.getHeight();
        float height = mainFrameLowerY-buttonsAreaUpperLine-stepToText;

        float centerX = frameImage.getLeftUpperCorner().x+frameImage.getWidth()/2;
        float centerY = buttonsAreaUpperLine+height/2;

        Rectangular rectangular = new Rectangular(new Vec2(centerX, centerY), frameImage.getWidth()/1.6f, height);

        shopMenuController.addButtonsListInZone(names, rectangular);
        shopMenuController.setButtonsForActualSubmenuWereAdded(true);



        String text = getText(ShopMenuController.ShopControllerStatements.LEAVE);
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text);

        */
    }

    public void init() {
        shopMenu.getActionPanel().setVisible(false);
        shopMenu.getFrameWithObjectData().setVisible(false);
        for (NES_GuiElement guiElement : shopMenuController.guiElements){
            guiElement.hide(true);
        }
        try {
            for (int i = (shopMenuController.guiElements.size()-1); i>= 0; i--){
                shopMenuController.guiElements.get(i).block(true);
                shopMenuController.guiElements.remove(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String text = getText();

        shopMenu.createTextFrameForShop(TextFrameForShop.FULL_HEIGHT);
        shopMenu.getTextFrameForShop().setText(text, true);
        //shopMenu.getTextFrameForShop().recreateTextDrawingController(text, 20);
        //shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setStringsAlongY(20);
        //shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text);
        //shopMenu.getTextFrameForShop().recreateTextDrawingController(text);
        shopMenuController.setButtonsForActualSubmenuWereAdded(false);
        System.out.println("Buttons were hidden");
        guiWasCreated = false;
        shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
    }
}
