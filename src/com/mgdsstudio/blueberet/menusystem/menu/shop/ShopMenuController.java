package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInMenuController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.AddingNewCollectableObjectAction;
import com.mgdsstudio.blueberet.loading.*;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.ColorWithName;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.menu.ShopMenu;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopMenuController {

    protected final ArrayList<NES_GuiElement> guiElements = new ArrayList<>();
    private String I_NEED_WEAPONS = "I NEED WEAPONS";
    private String I_NEED_AMMO = "I NEED SOME AMMO";
    private String NO_I_NEED_SOMETHING_ELSE = "NO, I NEED SOMETHING ELSE";
    private String I_DONT_HAVE_MONEY = "I DON'T HAVE ENOUGH MONEY";
    private String NOTHING = "NOTHING, I COME LATER";
    private String BACK = "NOTHING";

    private final ShopMenu shopMenu;
    private boolean buttonsMustBeAligned;
    private boolean buttonsForActualSubmenuWereAdded = true;
    private int statement = ShopControllerStatements.GREETING;
    private Timer leavingTimer;
    private AfterObjectBuyingMessageController afterObjectBuyingMessageController;


    public ShopMenuController(ShopMenu shopMenu) {
        ActionButtons.init();
        ItemsList.init();
        WatchAdds.init();
        AmmoList.init();
        System.out.println("*** I NEED TO TEST. MAYBE BUTTONS WITH THE SAME NAME WERE ALREADY ADDED! ***");
        this.shopMenu = shopMenu;
        initLanguageSpecific();
        shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(getText(ShopControllerStatements.GREETING), true);
    }

    private void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN){
            I_NEED_WEAPONS = "МНЕ НУЖНО ОРУЖИЕ";
            I_NEED_AMMO = "МНЕ НУЖНЫ БОЕПРИПАСЫ";
            NO_I_NEED_SOMETHING_ELSE = "НЕТ, МНЕ НУЖНО КОЕ-ЧТО ДРУГОЕ";
            I_DONT_HAVE_MONEY = "У МЕНЯ НЕДОСТАТОЧНО ДЕНЕГ";
            NOTHING = "ЗАБУДЬ ОБ ЭТОМ";
            BACK = "НИЧЕГО";
        }
    }

    public void update(GameMenusController gameMenusController, int mouseX, int mouseY) {
        if (buttonsMustBeAligned) {
            alignButtonsAlongY();
            buttonsMustBeAligned = false;
        }
        updateController(gameMenusController, mouseX, mouseY);
    }

    private void updateGreeting(PApplet engine, int mouseX, int mouseY){
        if (shopMenu.getTextFrameForShop().isClicked(engine) || shopMenu.getTextFrameForShop().isFullApeared()){
            createGuiForMainSubmenuSelecting(engine);
            shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
            statement = ShopControllerStatements.MAIN_LIST;
        }
    }

    private void updateController(GameMenusController controller, int mouseX, int mouseY) {
        for (NES_GuiElement element : guiElements) element.update(mouseX, mouseY);
        if (statement == ShopControllerStatements.GREETING){
            updateGreeting(controller.getEngine(), mouseX, mouseY);
        }
        else if (statement == ShopControllerStatements.MAIN_LIST){
            updateMainListSubmenu(controller.getEngine(), mouseX, mouseY);
        }
        else if (statement == ShopControllerStatements.LEAVE){
            updateLeaveSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.WEAPONS_LIST){
            updateWeaponsListSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.AMMO_TYPE){
            updateAmmoListSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.WATCH_ADDS){
            updateWatchAddsSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
            updateAnotherObjectsSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.ADDS_SHOWING){
            updateWatchPlayingSubmenu(controller);
        }
        else if (statement == ShopControllerStatements.AFTER_BUYING_THANKSGIVING){
            afterObjectBuyingMessageController.update(controller);
        }
        updateBuying(controller);
        updatePlayerSpeaking();
    }

    private void buyItem(String name, GameMenusController controller) {
        if (name == ActionButtons.BUY){
            if (statement == ShopControllerStatements.WEAPONS_LIST) {
                WeaponType weaponType = shopMenu.getActionPanel().getEmbeddedWeapon();
                System.out.println("Embedded weapon is " + weaponType);
                if (weaponType != null) {
                    System.out.println("Player has bought " + weaponType);
                    buyWeapon(weaponType, controller.getEngine());
                    controller.getSoundController().setAndPlayAudio(SoundInMenuController.OBJECT_BOUGHT);
                }
            }
        }
        else if (statement == ShopControllerStatements.AMMO_TYPE){
            int count = ActionButtons.getCountForButton(name);
            int ammoType = shopMenu.getActionPanel().getEmbeddedObjectNumber();
            WeaponType weaponType = shopMenu.getActionPanel().getEmbeddedWeapon();
            System.out.println("Player bought " + count + " pcs of " + ammoType + " for weapon " + weaponType );
            buyAmmo(ammoType, count, weaponType, controller.getEngine());
            controller.getSoundController().setAndPlayAudio(SoundInMenuController.OBJECT_BOUGHT);
        }
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
            int count = ActionButtons.getCountForButton(name);
            int objectType = shopMenu.getActionPanel().getEmbeddedObjectNumber();
            //WeaponType weaponType = shopMenu.getActionPanel().getEmbeddedWeapon();
            System.out.println("Player bought " + count + " pcs of " + objectType + " for weapon "  );
            buyItem(objectType, count, controller.getEngine());
            controller.getSoundController().setAndPlayAudio(SoundInMenuController.OBJECT_BOUGHT);
        }

    }


    private void buyItem(int type, int count, PApplet engine) {
        PlayerDataSimpleSaveMaster master = new PlayerDataSimpleSaveMaster();
        master.incrementItems(type, count);
        String itemName = ItemsList.getNameForType(type);
        int cost = getPriceForObject(itemName)*count;
        setAfterBought(master, cost);
        System.out.println("Object cost: " + cost);
        //buttonsForActualSubmenuWereAdded = false;
        afterObjectBuyingMessageController = new AfterObjectBuyingMessageController(type, this, shopMenu, ShopControllerStatements.ANOTHER_OBJECTS_TYPE);
        afterObjectBuyingMessageController.init();
    }

    private void buyAmmo(int type, int count, WeaponType weaponType, PApplet engine) {
        PlayerDataSimpleSaveMaster master = new PlayerDataSimpleSaveMaster();
        master.incrementAmmo(type, weaponType, count);
        String ammoName = AmmoList.getAmmoNameForWeapon(weaponType);
        int cost = getPriceForObject(ammoName)*count;
        setAfterBought(master, cost);
        System.out.println("Object cost: " + cost);
        //buttonsForActualSubmenuWereAdded = false;
        afterObjectBuyingMessageController = new AfterObjectBuyingMessageController(type, this, shopMenu, ShopControllerStatements.AMMO_TYPE);
        afterObjectBuyingMessageController.init();
    }

    private void setAfterBought(PlayerDataSimpleSaveMaster master, int cost){
        master.decrementMoney(cost);
        master.saveOnDisk();
        statement = ShopControllerStatements.AFTER_BUYING_THANKSGIVING;
        //afterObjectBuyingMessageController = new AfterObjectBuyingMessageController(weaponType, this, shopMenu);
        //afterObjectBuyingMessageController.init();
    }

    private void buyWeapon(WeaponType weaponType, PApplet engine) {
        PlayerDataSimpleSaveMaster master = new PlayerDataSimpleSaveMaster();
        System.out.println("Try to deblock weapon " + weaponType);
        master.deblockWeapon(weaponType);

        int cost = getPriceForObject(FirearmsWeapon.getWeaponNameForType(weaponType));
        //master.setNewMoney();
        master.decrementMoney(cost);
        //master.saveOnDisk();
        //statement = ShopControllerStatements.AFTER_BUYING_THANKSGIVING;
        afterObjectBuyingMessageController = new AfterObjectBuyingMessageController(weaponType, this, shopMenu, ShopControllerStatements.WEAPONS_LIST);
        afterObjectBuyingMessageController.init();
        System.out.println("Object cost: " + cost);
        //buttonsForActualSubmenuWereAdded = false;
        master.saveOnDisk();
    }

    private void updateBuying(GameMenusController controller) {
        if (shopMenu.getActionPanel().isVisible()){
            if (shopMenu.getActionPanel().getReleasedButton() != null){
                String name = shopMenu.getActionPanel().getReleasedButton().getName();
                System.out.println("Name from released button: " + name);
                if (name == ActionButtons.CANCEL) cancelAction(controller);
                else buyItem(name, controller);
            }
        }
    }

    private void cancelAction(GameMenusController controller) {
        if (statement == ShopControllerStatements.WEAPONS_LIST){
            shopMenu.getActionPanel().setVisible(false);
            shopMenu.getFrameWithObjectData().setVisible(false);
            createWeaponsListSubmenu(controller.getEngine());
        }
        else if (statement == ShopControllerStatements.AMMO_TYPE){
            shopMenu.getActionPanel().setVisible(false);
            shopMenu.getFrameWithObjectData().setVisible(false);
            createSubmenuForStatement(statement);//controller.getEngine());
        }
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
            shopMenu.getActionPanel().setVisible(false);
            shopMenu.getFrameWithObjectData().setVisible(false);
            createSubmenuForStatement(statement);//controller.getEngine());
        }
    }

    private void updateButtonsWereNotAdded(GameMenusController controller){
        if (shopMenu.getTextFrameForShop().isClicked(controller.getEngine())) {
            createButtons(controller.getEngine());
            shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
        } else {
            if (shopMenu.getTextFrameForShop().isFullApeared()) {
                createButtons(controller.getEngine());
                shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);

            }
        }
    }

    private void backToMainList(GameMenusController controller){
        statement = ShopControllerStatements.MAIN_LIST;
        guiElements.clear();
        createGuiForMainSubmenuSelecting(controller.getEngine());
        String text = getText(ShopControllerStatements.MAIN_LIST);
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
        buttonsForActualSubmenuWereAdded = false;
    }

    private void updateAnotherObjectsSubmenu(GameMenusController controller) {
        if (!buttonsForActualSubmenuWereAdded) {
            updateButtonsWereNotAdded(controller);
        }
        else {
            String name = getReleasedButtonName();
            if (name != null) {
                if (name == BACK) {
                    backToMainList(controller);
                }
                else {
                    loadActionPanelForItems(name);
                    loadItemsData(name);
                }
            }
        }
    }

    private void updateAmmoListSubmenu(GameMenusController controller) {
        if (!buttonsForActualSubmenuWereAdded) {
            updateButtonsWereNotAdded(controller);
        }
        else {
            String name = getReleasedButtonName();
            if (name != null) {
                if (name == BACK) {
                    backToMainList(controller);
                }
                else {
                    loadActionPanelForAmmo(name);
                    loadAmmoData(name);
                }
            }
        }
    }

    private void updatePlayerSpeaking() {
        boolean pressed = false;
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.PRESSED){
                pressed = true;
                break;
            }
        }
        if (pressed){
            shopMenu.getPlayerFace().setFaceStatement(FrameWithFace.SPEAKING);
        }
        else {
            shopMenu.getPlayerFace().setFaceStatement(FrameWithFace.WAITING);
        }
    }

    private void updateWatchPlayingSubmenu(GameMenusController controller){
        if (!buttonsForActualSubmenuWereAdded) {
            shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
            if (Program.OS == Program.ANDROID){
                controller.setUserValue(MenuType.SHOP);
                controller.setNewMenu(MenuType.REWARDED_ADDS_MENU);
            }
            else controller.setNewMenu(MenuType.SHOP);
            buttonsForActualSubmenuWereAdded = true;
        }
        else {

        }
    }

    private void updateWatchAddsSubmenu(GameMenusController controller){
        if (!buttonsForActualSubmenuWereAdded) {
            updateButtonsWereNotAdded(controller);
        }
        else {
            String name = getReleasedButtonName();
            if (name != null) {
                if (name == WatchAdds.NO) {
                    backToMainList(controller);

                } else if (name == WatchAdds.OK){
                    statement = ShopControllerStatements.ADDS_SHOWING;
                    guiElements.clear();
                    String text = getText(ShopControllerStatements.ADDS_SHOWING);
                    shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
                    buttonsForActualSubmenuWereAdded = false;
                }
            }
        }
    }


    private void updateWeaponsListSubmenu(GameMenusController controller) {
        if (!buttonsForActualSubmenuWereAdded) {
            updateButtonsWereNotAdded(controller);
        }
        else {
            String name = getReleasedButtonName();
            if (name != null) {
                if (name == BACK) {
                    backToMainList(controller);
                } else {
                    loadActionPanelForWeapon(name);
                    loadWeaponData(name);
                }
            }
        }
    }

    private void loadActionPanelForWeapon(String name) {
        shopMenu.getActionPanel().setVisible(true);
        shopMenu.changeFramesDimensions(true);
        String[] names = new String[2];
        names[0] = ActionButtons.BUY;
        names[1] = ActionButtons.CANCEL;
        int score = getScore();
        shopMenu.getActionPanel().createButtons(names, score);
        boolean deblocked = hasPlayerAlreadyThisWeaponDeblocked(name);
        if (deblocked) {
            shopMenu.getActionPanel().setNotNeedToBeBought(ActionButtons.BUY);
            System.out.println("Button " + ActionButtons.BUY + " was hidden");
        }
        else shopMenu.getActionPanel().setCanBeBought(ActionButtons.BUY, canBeBought(name, score));

    }

    private boolean hasPlayerAlreadyThisWeaponDeblocked(String name) {
        PlayerDataSimpleLoadMaster playerDataSimpleLoadMaster = new PlayerDataSimpleLoadMaster();
        playerDataSimpleLoadMaster.loadData();
        return playerDataSimpleLoadMaster.hasPlayerDeblockedWeapon(name);
    }


    private void loadActionPanelForItems(String name) {
        shopMenu.getActionPanel().setVisible(true);
        shopMenu.changeFramesDimensions(false);
        String[] names = new String[4];
        names[0] = ActionButtons.BUY_1;
        names[1] = ActionButtons.BUY_2;
        names[2] = ActionButtons.BUY_5;
        names[3] = ActionButtons.CANCEL;
        int score = getScore();
        shopMenu.getActionPanel().createButtons(names, score);
        HashMap <String, Integer> data = new HashMap<>();
        data.put(names[0], 1);
        data.put(names[1], 2);
        data.put(names[2], 5);
        int collectableObjectCode = ItemsList.getCodeNumberForObject(name);
        boolean listFull = false;
        int maxCount = -1;
        int actualCount = 0;
        if (collectableObjectCode >= 0){
            PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
            maxCount = playerBag.getMaxObjects(collectableObjectCode);
            PlayerDataSimpleLoadMaster master = new PlayerDataSimpleLoadMaster();
            actualCount = master.getObjectsCount(collectableObjectCode);
            if (actualCount >= maxCount) {
                listFull = true;
                System.out.println("Max ammo: " + maxCount + " for weapon " + collectableObjectCode + "; Button buy must be blocked");
            }
        }
        else {
            System.out.println("Trouble!" + this.getClass());
        }
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().clear();
        System.out.println("Text frame for shop was cleared");
        shopMenu.getActionPanel().setCanBeBought(data, getPriceForObject(name), score, actualCount, maxCount);
        if (listFull) {
            shopMenu.getActionPanel().setAmmoIsFull(ActionButtons.BUY);
        }

        //shopMenu.getActionPanel().setText();


        /*
        if (collectableObjectCode >=0){
            PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
            int maxCount = playerBag.getMaxObjects(collectableObjectCode);
            !shopMenu.getActionPanel().setNotNeedToBeBought(ActionButtons.BUY);
            System.out.println("Button " + ActionButtons.BUY + " was hidden");
        }
*/



        else shopMenu.getActionPanel().setCanBeBought(ActionButtons.BUY, canBeBought(name, score));

        /*
        int actualCount = getActualNumber(weaponType);
        int maxAmmo = 10;
        boolean ammoFull = false;
        if (weaponType != null){
            PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
            maxAmmo = playerBag.getMaxAmmoForWeapon(weaponType);
            PlayerDataSimpleLoadMaster master = new PlayerDataSimpleLoadMaster();
            int actualAmmo = master.getMagazinesForAmmo(weaponType);
            if (actualAmmo >= maxAmmo) {
                ammoFull = true;
                System.out.println("Max ammo: " + maxAmmo + " for weapon " + weaponType + "; Button buy must be blocked");
            }
        }
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().clear();
        System.out.println("Text frame for shop was cleared");
        shopMenu.getActionPanel().setCanBeBought(data, getPriceForObject(name), score, actualCount, maxAmmo);
        if (ammoFull) {
            shopMenu.getActionPanel().setAmmoIsFull(ActionButtons.BUY);
        }
         */



    }

    private void loadActionPanelForAmmo(String name) {
        shopMenu.getActionPanel().setVisible(true);
        shopMenu.changeFramesDimensions(false);
        String[] names = new String[5];
        names[0] = ActionButtons.BUY_1;
        names[1] = ActionButtons.BUY_2;
        names[2] = ActionButtons.BUY_5;
        names[3] = ActionButtons.BUY_10;
        names[4] = ActionButtons.CANCEL;
        //names[2] = getActualMoney();
        int score = getScore();
        shopMenu.getActionPanel().createButtons(names, score);
        HashMap <String, Integer> data = new HashMap<>();
        data.put(names[0], 1);
        data.put(names[1], 2);
        data.put(names[2], 5);
        data.put(names[3], 10);
        WeaponType weaponType = AmmoList.getWeaponTypeForAmmoName(name);
        int actualCount = getActualNumber(weaponType);
        int maxAmmo = 10;
        boolean ammoFull = false;
        if (weaponType != null){
            PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
            maxAmmo = playerBag.getMaxAmmoForWeapon(weaponType);
            PlayerDataSimpleLoadMaster master = new PlayerDataSimpleLoadMaster();
            int actualAmmo = master.getMagazinesForAmmo(weaponType);
            if (actualAmmo >= maxAmmo) {
                ammoFull = true;
                System.out.println("Max ammo: " + maxAmmo + " for weapon " + weaponType + "; Button buy must be blocked");
            }
        }
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().clear();
        System.out.println("Text frame for shop was cleared");
        shopMenu.getActionPanel().setCanBeBought(data, getPriceForObject(name), score, actualCount, maxAmmo);
        if (ammoFull) {
            shopMenu.getActionPanel().setAmmoIsFull(ActionButtons.BUY);
        }
    }

    private int getActualNumber(WeaponType weaponType) {
        PlayerDataSimpleLoadMaster master = new PlayerDataSimpleLoadMaster();
        master.loadData();
        return master.getMagazinesForAmmo(weaponType);

    }

    private boolean canBeBought(String name, int score) {
        if (getPriceForObject(name)<=score) return true;
        else return false;
    }

    private int getScore() {
        PlayerDataSimpleLoadMaster playerProgressLoadMaster = new PlayerDataSimpleLoadMaster();
        playerProgressLoadMaster.loadData();
        int value = playerProgressLoadMaster.getMoney();
        return value;
    }


    private int getPriceForObject(String name){
        int price = 1;
        if (name == WeaponsList.REVOLVER) price = 100;
        else if (name == WeaponsList.HANDGUN) price = 250;
        else if (name == WeaponsList.SO_SHOTGUN) price = 500;
        else if (name == WeaponsList.SHOTGUN) price = 800;
        else if (name == WeaponsList.HAND_GRENADE) price = 100;
        else if (name == WeaponsList.GRENADE_LAUNCHER) price = 1000;
        else if (name == WeaponsList.SMG) price = 1500;

        else if (name == AmmoList.BULLETS_FOR_REVOLVER) price = 5;
        else if (name == AmmoList.MAGAZINES_FOR_HANDGUN) price = 50;
        else if (name == AmmoList.SHELLS_FOR_SHOTGUN) price = 20;
        else if (name == AmmoList.GRENADE_LAUNCHER) price = 50;
        else if (name == AmmoList.HAND_GRENADES) price = 100;
        else if (name == AmmoList.SMG) price = 150;

        else if (name == ItemsList.SMALL_MEDICAL_KIT) price = 25;
        else if (name == ItemsList.MEDIUM_MEDICAL_KIT) price = 50;
        else if (name == ItemsList.LARGE_MEDICAL_KIT) price = 100;
        else if (name == ItemsList.DRUGS) price = 25;
        else {
            System.out.println("No data about cost for object " + name);
        }
        return price;
    }
    private String [] getTextDataForWeapon(String name){
        String [] data = new String[2];
        System.out.println("Try to find data for " + name);
        if (name == WeaponsList.REVOLVER){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ БАРАБАНА: 6 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ПУЛЯ  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "РЕВОЛЬВЕР ОБР. 1912 Г.";
            }
            else {
                data[0] = " Cylinder capacity: 6 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: BULLETS " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "REVOLVER 1912";
            }
        }
        else if (name == WeaponsList.HANDGUN){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ МАГАЗИНА: 12 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ПУЛЯ  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "ПИСТОЛЕТ";
            }
            else {
                data[0] = " MAGAZINE capacity: 12 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: BULLETS " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "HANDGUN";
            }
        }
        else if (name == WeaponsList.GRENADE_LAUNCHER){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ МАГАЗИНА: 1 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ГРАНАТА 40x46 мм" + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "ГРАНАТОМЕТ";
            }
            else {
                data[0] = " Magazine capacity: 1 "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: GRENADES 40x46 mm"  + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+ " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "GRENADE LAUNCHER";
            }
        }
        else if (name == WeaponsList.SO_SHOTGUN){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ МАГАЗИНА: 2 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ПАТРОН 12 калибра" + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "ОБРЕЗ";
            }
            else {
                data[0] = " Magazine capacity: 2 "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: SHELL 12-gauge "  + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+ " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "SAWED-OFF SHOTGUN";
            }
        }
        else if (name == WeaponsList.SHOTGUN){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ МАГАЗИНА: 7 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ПАТРОН 12 калибра  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "ПОМПОВОЕ РУЖЬЕ";
            }
            else {
                data[0] = " Magazine capacity: 7 "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: SHELL 12-gauge "  + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+ " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "PUMP ACTION SHOTGUN";
            }
        }
        else if (name == WeaponsList.SMG){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " ЕМКОСТЬ МАГАЗИНА: 30 " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "СНАРЯД: ПУЛЯ КАЛибра 9 мм " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "ПИСТОЛЕТ-ПУЛЕМЕТ";
            }
            else {
                data[0] = " Magazine capacity: 30 "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "AMMO: BULLET 9 mm "  + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+ " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "SUB MACHINE GUN";
            }
        }
        else if (name == WeaponsList.HAND_GRENADE){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = " СПОСОБ АТАКИ: БРОСОК " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "ЗАДЕРЖКА ДО ВЗРЫВА: 2 секУНДЫ     " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "РУЧНАЯ ОСКОЛОЧНАЯ ГРАНАТА";
            }
            else {
                data[0] = " ATTACK TYPE: THROW "+ TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + "DELAY UNTIL EXPLOSION: 2 SECONDS      "  + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR+ " " + "PRICE: " + getPriceForObject(name) + "$ ";
                data[1] = "HAND GRENADE";
            }
        }

        else {
            System.out.println("No data for this weapon or object" + name);
            data[0] = " no data ";
            data[1] = " no name ";
        }
        return data;
    }

    private String [] getTextDataForAmmo(String name){
        String [] data = new String[1];
        System.out.println("Try to find data for " + name);


        PlayerDataSimpleLoadMaster loadMaster = new PlayerDataSimpleLoadMaster();
        WeaponType weaponType = AmmoList.getWeaponTypeForAmmoName(name);

        int hasNow = loadMaster.getMagazinesForAmmo(weaponType);
        PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);

        int maxValue = playerBag.getMaxAmmoForWeapon(weaponType);
        String youHave = " YOU HAVE: ";
        if (Program.LANGUAGE == Program.RUSSIAN) youHave = " У ВАС: ";
        String riceString = " PRICE: " + getPriceForObject(name) + " $ " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + "" + youHave + "" +hasNow + '/' + maxValue;

        if (name == AmmoList.BULLETS_FOR_REVOLVER){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ПУЛИ ДЛЯ РЕВОЛЬВЕРА ОБР. 1912 Г." +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR +  riceString;
            }
            else {
                data[0] = "BULLETS FOR REVOLVER 1912. " + riceString;
            }
        }
        else if (name == AmmoList.MAGAZINES_FOR_HANDGUN){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "МАГАЗИН ДЛЯ ПИСТОЛЕТА."  + riceString;
            }
            else {
                data[0] = "MAGAZINE FOR HANDGUN."  + riceString;
            }
        }
        else if (name == AmmoList.GRENADE_LAUNCHER){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ГРАНАТА 40х46."  + riceString;
            }
            else {
                data[0] = "GRENADE 40x46."  + riceString;
            }
        }
        else if (name == AmmoList.SHELLS_FOR_SHOTGUN){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ПАТРОН 12-го калибра."  + riceString;
            }
            else {
                data[0] = "SHOTGUN SHELL CALIBER 12 mm."  + riceString;
            }
        }
        else if (name == AmmoList.SMG){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "МАГАЗИН ДЛЯ ПИСТОЛЕТА- ПУЛЕМЕТА."  + riceString;
            }
            else {
                data[0] = "MAGAZINE FOR SUBMASHINE GUN." + riceString;
            }
        }
        else if (name == AmmoList.HAND_GRENADES){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "РУЧНАЯ ОСКОЛОЧНАЯ ГРАНАТА. " + riceString;
            }
            else {
                data[0] = "HAND GREENADE." + riceString;
            }
        }

        else {
            System.out.println("No data for this weapon or object" + name);
            data[0] = " no data ";
            data[1] = " no name ";
        }
        return data;
    }

    private String [] getTextDataForItem(int id, String name){
        String [] data = new String[1];
        System.out.println("Try to find data for " + id);

        PlayerDataSimpleLoadMaster loadMaster = new PlayerDataSimpleLoadMaster();

        int hasNow = loadMaster.getObjectsCount(id);
        PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
        int maxValue = playerBag.getMaxObjects(id);
        String youHave = " YOU HAVE: ";
        if (Program.LANGUAGE == Program.RUSSIAN) youHave = " У ВАС: ";
        String priceString = " PRICE: " + getPriceForObject(name) + " $ " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR  + youHave + "" +hasNow + '/' + maxValue;
        if (name == ItemsList.SMALL_MEDICAL_KIT){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ЭНЕРГЕТИЧЕСКИЙ НАПИТОК. " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + "+ 25% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
            else {
                data[0] = ItemsList.SMALL_MEDICAL_KIT + " " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " + 25% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
        }
        else if (name == ItemsList.MEDIUM_MEDICAL_KIT){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ОБЕЗБОЛИВАЮЩЕЕ" +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " + 50% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
            else {
                data[0] = ItemsList.MEDIUM_MEDICAL_KIT + " " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " + 25% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
        }
        else if (name == ItemsList.LARGE_MEDICAL_KIT){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "АПТЕЧКА" +  +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " + 100% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
            else {
                data[0] = ItemsList.LARGE_MEDICAL_KIT + " " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " + 25% HP " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
        }
        else if (name == ItemsList.DRUGS){
            if (Program.LANGUAGE == Program.RUSSIAN){
                data[0] = "ТАБЛЕТКИ" +   TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " ЭФФЕКТ ЗАМЕДЛЕНИЯ ВРЕМЕНИ " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
            else {
                data[0] = ItemsList.DRUGS + " " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " SLOW MOTION EFFECT " +  TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " "  +   priceString;
            }
        }

        else {
            System.out.println("No data for this weapon or object" + name);
            data[0] = " no data ";
            data[1] = " no name ";
        }
        return data;
    }
    private void loadWeaponData(String name) {
        String [] data = getTextDataForWeapon(name);
        System.out.println("Weapon: " + name  + "; Data: " + data[0]);
        shopMenu.getFrameWithObjectData().setVisible(true);
        shopMenu.getFrameWithObjectData().createImage(getWeaponTypeForName(name));
        String textData = data[0];
        String nameData = data[1];
        shopMenu.getFrameWithObjectData().setText(textData, nameData);
        guiElements.clear();
        shopMenu.getFrameWithObjectData().startToShow();

        shopMenu.getActionPanel().setEmbeddedWeapon(FirearmsWeapon.getWeaponTypeForName(name));
        shopMenu.getTextFrameForShop().setText("", true);
    }

    private void loadAmmoData(String name) {
        String [] data = getTextDataForAmmo(name);
        System.out.println("Ammo: " + name  + "; Data: " + data[0]);
        shopMenu.getFrameWithObjectData().setVisible(true);
        shopMenu.getFrameWithObjectData().createImage(getAmmoForName(name), UpperPanel.HEIGHT_FOR_WEAPON_FRAME, UpperPanel.HEIGHT_FOR_WEAPON_FRAME);
        WeaponType weaponType =  AmmoList.getWeaponTypeForAmmoName(name);
        System.out.println("Weapon type for this ammo " + weaponType + " but name is: " + name);
        shopMenu.getActionPanel().setEmbeddedWeapon(weaponType);
        //shopMenu.getActionPanel().setEmbeddedWeapon(weaponType);

        String nameData = data[0];
        shopMenu.getFrameWithObjectData().setText(nameData, false);
        guiElements.clear();
        shopMenu.getFrameWithObjectData().startToShow();
        shopMenu.getTextFrameForShop().setText("", true);
    }

    private void loadItemsData(String name) {
        int objectCode = ItemsList.getCodeNumberForObject(name);
        String [] data = getTextDataForItem(objectCode, name);
        System.out.println("Items: " + name  + "; Data: " + data[0]);
        shopMenu.getFrameWithObjectData().setVisible(true);

        shopMenu.getFrameWithObjectData().createImage(objectCode, UpperPanel.HEIGHT_FOR_WEAPON_FRAME, UpperPanel.HEIGHT_FOR_WEAPON_FRAME);
        //WeaponType weaponType =  AmmoList.getWeaponTypeForAmmoName(name);
        //System.out.println("Weapon type for this ammo " + weaponType + " but name is: " + name);
        shopMenu.getActionPanel().setEmbeddedObjectNumber(objectCode);
        //shopMenu.getActionPanel().setEmbeddedWeapon(weaponType);

        String nameData = data[0];
        shopMenu.getFrameWithObjectData().setText(nameData, false);
        guiElements.clear();
        shopMenu.getFrameWithObjectData().startToShow();
        shopMenu.getTextFrameForShop().setText("", true);
    }

    private WeaponType getWeaponTypeForName(String name) {
        if (name == WeaponsList.REVOLVER) return WeaponType.REVOLVER;
        else if (name == WeaponsList.HANDGUN) return WeaponType.HANDGUN;
        else if (name == WeaponsList.SO_SHOTGUN) return WeaponType.SAWED_OFF_SHOTGUN;
        else if (name == WeaponsList.SHOTGUN) return WeaponType.SHOTGUN;
        else if (name == WeaponsList.HAND_GRENADE) return WeaponType.GRENADE;
        else if (name == WeaponsList.GRENADE_LAUNCHER) return WeaponType.M79;
        else if (name == WeaponsList.SMG) return WeaponType.SMG;
        else {
            System.out.println("No data fro this weapon");
            return null;
        }
    }

    private int getAmmoForName(String name) {
        if (name == AmmoList.BULLETS_FOR_REVOLVER) return AbstractCollectable.BULLETS_FOR_REVOLVER;
        else if (name == AmmoList.MAGAZINES_FOR_HANDGUN) return AbstractCollectable.BULLETS_FOR_PISTOL;
        else if (name == AmmoList.SHELLS_FOR_SHOTGUN) return AbstractCollectable.BULLETS_FOR_SHOTGUN;

        else if (name == AmmoList.HAND_GRENADES) return AbstractCollectable.BULLETS_FOR_HAND_GRENADE;
        else if (name == AmmoList.GRENADE_LAUNCHER) return AbstractCollectable.BULLETS_FOR_M79;
        else if (name == AmmoList.SMG) return AbstractCollectable.BULLETS_FOR_SMG;
        else {
            System.out.println("No data for this weapon");
            return -1;
        }
    }



    private ArrayList <String> getAddsAgreementList(){
        ArrayList <String> names = new ArrayList<>();
        names.add(WatchAdds.OK);
        names.add(WatchAdds.NO);
        return names;
    }

    private ArrayList <String> getAmmoList(){
        ArrayList <String> names = new ArrayList<>();
        names.add(AmmoList.BULLETS_FOR_REVOLVER);
        names.add(AmmoList.MAGAZINES_FOR_HANDGUN);
        names.add(AmmoList.SHELLS_FOR_SHOTGUN);
        names.add(AmmoList.HAND_GRENADES);
        names.add(AmmoList.GRENADE_LAUNCHER);
        names.add(AmmoList.SMG);
        names.add(BACK);
        return names;
    }

    private ArrayList <String> getItemsList(){
        ArrayList <String> names = new ArrayList<>();
        names.add(ItemsList.SMALL_MEDICAL_KIT);
        names.add(ItemsList.MEDIUM_MEDICAL_KIT);
        names.add(ItemsList.LARGE_MEDICAL_KIT);
        names.add(ItemsList.DRUGS);
        names.add(BACK);
        return names;
    }
    private ArrayList <String> getWeaponsList(){
        ArrayList <String> names = new ArrayList<>();
        names.add(WeaponsList.REVOLVER);
        names.add(WeaponsList.HANDGUN);
        names.add(WeaponsList.SO_SHOTGUN);
        names.add(WeaponsList.SHOTGUN);
        names.add(WeaponsList.HAND_GRENADE);
        names.add(WeaponsList.GRENADE_LAUNCHER);
        names.add(WeaponsList.SMG);
        names.add(BACK);
        return names;
    }

    void createButtons(PApplet engine){
        ArrayList <String> names;
        if (statement == ShopControllerStatements.WEAPONS_LIST) names = getWeaponsList();
        else if (statement == ShopControllerStatements.AMMO_TYPE) names = getAmmoList();
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE) names = getItemsList();
        else if (statement == ShopControllerStatements.WATCH_ADDS) names = getAddsAgreementList();
        else names = null;
        try {
            for (int i = (guiElements.size()-1); i>= 0; i--){
                guiElements.remove(i);
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

        addButtonsListInZone(names, rectangular);
        buttonsForActualSubmenuWereAdded = true;
    }

    private void updateLeaveSubmenu(GameMenusController controller) {
        if (shopMenu.getTextFrameForShop().isClicked(controller.getEngine())){
            controller.setNewMenu(MenuType.CONTINUE_LAST_GAME);
            controller.stopMusic();
            shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
        }
        else {
            if (shopMenu.getTextFrameForShop().isFullApeared()) {
                shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.WAITING);
                if (leavingTimer == null) leavingTimer = new Timer(1500);
                else if (leavingTimer.isTime()) {
                    controller.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                    controller.stopMusic();
                }
            }
        }
    }

    String getReleasedButtonName(){
        for (NES_GuiElement element : guiElements) {
            if (!element.isHidden()) {
                if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                    return element.getName();
                }
            }
        }
        return null;
    }

    void createSubmenuForStatement(int statement){
        String text = "";
        if (statement == ShopControllerStatements.AMMO_TYPE){
            text = getText(ShopControllerStatements.AMMO_TYPE);
        }
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
            text = getText(ShopControllerStatements.ANOTHER_OBJECTS_TYPE);
        }
        else {
            System.out.println("No data for this statement");
        }
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
        for (NES_GuiElement guiElement : guiElements){
            guiElement.hide(true);
        }
        buttonsForActualSubmenuWereAdded = false;
    }

    private void updateMainListSubmenu(PApplet engine, int mouseX, int mouseY) {
        for (NES_GuiElement element : guiElements) {
            if (element.getActualStatement() == NES_GuiElement.RELEASED){
                if (element.getName() == NOTHING){
                    statement = ShopControllerStatements.LEAVE;
                    createLeaveSubmenu(engine);
                    shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
                }
                else if (element.getName() == I_NEED_AMMO){
                    statement = ShopControllerStatements.AMMO_TYPE;
                    createSubmenuForStatement(statement);
                    shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
                }
                else if (element.getName() == I_NEED_WEAPONS){
                    statement = ShopControllerStatements.WEAPONS_LIST;
                    createWeaponsListSubmenu(engine);
                    shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
                }
                else if (element.getName() == NO_I_NEED_SOMETHING_ELSE){
                    statement = ShopControllerStatements.ANOTHER_OBJECTS_TYPE;
                    createSubmenuForStatement(statement);
                    shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
                }
                else if (element.getName() == I_DONT_HAVE_MONEY){
                    statement = ShopControllerStatements.WATCH_ADDS;
                    createWatchAddsSubmenu(Program.engine);
                    shopMenu.getHandlerFace().setFaceStatement(FrameWithFace.SPEAKING);
                }
            }
        }
    }

    private void createWatchAddsSubmenu(PApplet engine) {
        String text = getText(ShopControllerStatements.WATCH_ADDS);
        //!
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
        for (NES_GuiElement guiElement : guiElements){
            guiElement.hide(true);
        }
        buttonsForActualSubmenuWereAdded = false;
    }

    void createWeaponsListSubmenu(PApplet engine) {
        String text = getText(ShopControllerStatements.WEAPONS_LIST);
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
        for (NES_GuiElement guiElement : guiElements){
            guiElement.hide(true);
        }
        buttonsForActualSubmenuWereAdded = false;
    }

    private void createLeaveSubmenu(PApplet engine) {
        for (NES_GuiElement guiElement : guiElements){
            guiElement.hide(true);
        }

        String text = getText(ShopControllerStatements.LEAVE);
        shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().setNewText(text, true);
    }

    void createGuiForMainSubmenuSelecting(PApplet engine) {

        ArrayList <String> names = new ArrayList<>();
        names.add(I_NEED_WEAPONS);
        names.add( I_NEED_AMMO);
        names.add(NO_I_NEED_SOMETHING_ELSE);
        if (Program.withAdds) names.add(I_DONT_HAVE_MONEY);
        //names.add(I_DONT_HAVE_MONEY);
        names.add(NOTHING);
        EightPartsFrameImage frameImage = shopMenu.getTextFrameForShop().getFrame();
        float mainFrameLowerY = frameImage.getLeftUpperCorner().y+frameImage.getHeight();
        Rectangular textArea = shopMenu.getTextFrameForShop().getTextInSimpleFrameDrawingController().getTextArea();
        float stepToText = shopMenu.getTextFrameForShop().getGapToFrame();
        float buttonsAreaUpperLine = stepToText*2+textArea.getLeftUpperY()+textArea.getHeight();
        float height = mainFrameLowerY-buttonsAreaUpperLine-stepToText;
        float centerX = frameImage.getLeftUpperCorner().x+frameImage.getWidth()/2;
        float centerY = buttonsAreaUpperLine+height/2;
        Rectangular rectangular = new Rectangular(new Vec2(centerX, centerY), frameImage.getWidth()/1.6f, height);
        addButtonsListInZone(names, rectangular);
    }

    public void addButtonsListInZone(ArrayList<String> names, Rectangular rectangular) {
        int number = names.size();
        float buttonsStep = rectangular.getHeight()/number;
        if (number <= 2) buttonsStep = rectangular.getHeight()/4;
        float startY = rectangular.getCenterY()-rectangular.getHeight()/2+buttonsStep/2;
        System.out.println("Buttons: " + number + " ; Step: " + buttonsStep);
        for (int i = 0; i < names.size(); i++){
            NES_ButtonWithCursor button = new NES_ButtonWithCursor((int)(rectangular.getLeftUpperX()+rectangular.getWidth()/2), (int)startY, (int)rectangular.getWidth(), (int)(buttonsStep/2), names.get(i), shopMenu.getGraphic());
            startY+=buttonsStep;
            ColorWithName colorWithName = getColorForButton(names.get(i));
            button.setColor(colorWithName);
            guiElements.add(button);
        }
        buttonsMustBeAligned = true;
        for (NES_GuiElement guiElement : guiElements){
            guiElement.setVisible(false);
            //guiElement.setVisible(true);
        }
        System.out.println("New " + guiElements.size() + " buttons were created in zone " + rectangular.getCenterX() + "x" + rectangular.getCenterY() + " and dims: " + rectangular.getWidth() + "x" + rectangular.getHeight());
    }

    public void addSingleButtonInZone(String name, Rectangular rectangular) {
        float startY = rectangular.getCenterY();

            NES_ButtonWithCursor button = new NES_ButtonWithCursor((int)(rectangular.getLeftUpperX()+rectangular.getWidth()/2), (int)startY, (int)rectangular.getWidth(), (int)(NES_ButtonWithCursor.NORMAL_HEIGHT*2), name, shopMenu.getGraphic());
            guiElements.add(button);

        buttonsMustBeAligned = true;
        for (NES_GuiElement guiElement : guiElements){
            //guiElement.setVisible(false);
            //guiElement.setVisible(true);
        }
        System.out.println("New " + guiElements.size() + " buttons were created in zone " + rectangular.getCenterX() + "x" + rectangular.getCenterY() + " and dims: " + rectangular.getWidth() + "x" + rectangular.getHeight());
    }

    public void addSingleButton(String name) {
        float startX = shopMenu.getTextFrameForShop().frame.getLeftUpperCorner().x+shopMenu.getTextFrameForShop().frame.getWidth()/2;
        float startY = shopMenu.getTextFrameForShop().frame.getLeftUpperCorner().y-shopMenu.getTextFrameForShop().getGapToFrame()+shopMenu.getTextFrameForShop().frame.getHeight()-NES_ButtonWithCursor.NORMAL_HEIGHT;
        float buttonWidth = shopMenu.getTextFrameForShop().frame.getWidth()/1.6f;
        NES_ButtonWithCursor button = new NES_ButtonWithCursor((int) startX, (int) startY, (int) buttonWidth,(int)(NES_ButtonWithCursor.NORMAL_HEIGHT*2),  name, shopMenu.getGraphic());
        guiElements.add(button);

        buttonsMustBeAligned = true;

        //System.out.println("New " + guiElements.size() + " buttons were created in zone " + rectangular.getCenterX() + "x" + rectangular.getCenterY() + " and dims: " + rectangular.getWidth() + "x" + rectangular.getHeight());
    }

    private ColorWithName getColorForButton(String s) {
        if (statement == ShopControllerStatements.WEAPONS_LIST){
            PlayerDataSimpleLoadMaster master = new PlayerDataSimpleLoadMaster();
            master.loadData();
            ArrayList <WeaponType> deblockedWeapons = master.getDeblockedWeaponsList();
            //System.out.println("Player deblocked " + deblockedWeapons.size() + " weapons");
            for (WeaponType weaponType : deblockedWeapons){
                if (FirearmsWeapon.getWeaponTypeForName(s) == weaponType){
                    return new ColorWithName(ColorWithName.YELLOW);
                }
                //else System.out.println("Name " + s + " is not " + weaponType);
            }
            return new ColorWithName(ColorWithName.WHITE);
        }
        else if (statement == ShopControllerStatements.AMMO_TYPE){
            PlayerDataSimpleLoadMaster master =  new PlayerDataSimpleLoadMaster();
            master.loadData();
            WeaponType weaponType = AmmoList.getWeaponTypeForAmmoName(s);
            boolean full;
            if (weaponType == null) full = false;
            else full = master.hasFullMagazines(weaponType);
            if (full) return new ColorWithName(ColorWithName.RED);
            else {
                boolean deblocked = master.hasPlayerDeblockedWeapon(weaponType);
                if (deblocked) return new ColorWithName(ColorWithName.WHITE);
                else {
                    if (weaponType == WeaponType.SAWED_OFF_SHOTGUN || weaponType == WeaponType.SHOTGUN){
                        boolean hasPairedWeapon = false;
                        if (master.hasPlayerDeblockedWeapon(WeaponType.SHOTGUN) ||master.hasPlayerDeblockedWeapon(WeaponType.SAWED_OFF_SHOTGUN)) hasPairedWeapon = true;
                        if (hasPairedWeapon) return new  ColorWithName(ColorWithName.WHITE);
                        else return new  ColorWithName(ColorWithName.YELLOW);
                    }
                    else return new ColorWithName(ColorWithName.YELLOW);
                }
            }
        }
        else if (statement == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
            PlayerDataSimpleLoadMaster master =  new PlayerDataSimpleLoadMaster();
            master.loadData();
            int code = ItemsList.getCodeNumberForObject(s);
            boolean full;
            System.out.println("Code for item: " + code);
            if (code >= 0) {
                full = master.hasFullItems(code);
                if (full) return new ColorWithName(ColorWithName.RED);
                else {
                    return new ColorWithName(ColorWithName.WHITE);
                }
            }
            else return new ColorWithName(ColorWithName.WHITE);
        }
        else return new ColorWithName(ColorWithName.WHITE);
    }

    public int getStatement() {
        return statement;
    }

    protected void alignButtonsAlongY(){
        ArrayList<NES_GuiElement> elements = guiElements;
        int longestString = 0;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                int actualWidth = elements.get(i).getTextWidth();
                if (actualWidth > longestString) {
                    longestString = actualWidth;
                }
            }
        }
        System.out.println("Longest string is: " + longestString);
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                elements.get(i).setWidth(longestString);
            }
        }
        int leftX = shopMenu.getGraphic().width/2-longestString/2;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
               NES_ButtonWithCursor button = (NES_ButtonWithCursor) elements.get(i);
               button.setLeftX(leftX);
            }
        }
        for (NES_GuiElement guiElement : guiElements){
            guiElement.setVisible(true);
        }
    }

    public void draw(PGraphics graphics) {
        for (NES_GuiElement element : guiElements) {
            if (element.getActualStatement() != NES_GuiElement.HIDDEN) {
                element.draw(graphics);
            }
        }
    }

    public String getText(int textType) {
            String text = "";
            if (textType == ShopControllerStatements.GREETING){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " АС-САЛЯМУ АЛЕЙКУМ И ДОБРОГО ДНЯ ЗОЛОТОЙ МОЙ ЧЕЛОВЕК! ПРИШЕЛ ВЫБРАТЬ ЧТО-НИБУДЬ ДЛЯ САМООБОРОНЫ? ЗДЕСЬ ТЫ НАЙДЕШЬ ВСЕ ЧТО НУЖЕН НАСТОЯЩЕМУ ВОЙНУ АЛЛАХА В БОЮ.";
                }
                else {
                    text = " As-salamu alaykum and good day! DID YOU COME TO FIND SOMETHING SPECIAL FOR LOCAL DEFENCE? I HAVE ALL YOU NEED FOR A REAL WAR!";
                }
            }
            else if (textType == ShopControllerStatements.MAIN_LIST){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " НЕ УХОДИ ЗОЛОТОЙ МОЙ ЧЕЛОВЕК, ПОСМОТРИ, ЧТО У МЕНЯ ЕЩЕ ЕСТЬ ДЛЯ ТЕБЯ.";
                }
                else {
                    text = " DO NOT LEAVE MY SHOP. I HAVE MANY OTHERS USEFUL THINGS SPECIAL FOR YOU.";
                }
            }
            else if (textType == ShopControllerStatements.WEAPONS_LIST){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " ОТЛИЧНАЯ ИДЕЯ! У МЕНЯ КАК РАЗ ЗАВАЛЯЛОСЬ КОЕ-ЧТО СПЕЦИАЛЬНО ДЛЯ ТЕБЯ. ТЫ НЕ НАЙДЕШЬ НИЧЕГО ПОХОЖЕГО У КОГО-ЛИБО. ВЗГЛЯНИ НА МОЙ АРСЕНАЛ! ";
                }
                else {
                    text = " GOOD! IT IS A VERY GOOD IDEA! I HAVE SOMETHING SPECIAL FOR YOU. YOU CAN NOT FIND THIS WEAPONS SOMEWHERE ELSE. SEE MY ARSENAL!";
                }
            }
            else if (textType == ShopControllerStatements.LEAVE){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " ЖАЛЬ, НО СКОРО УВИДИМСЯ. ЗАХОДИ ЕЩЕ!";
                }
                else {
                    text = " WHAT A SHAME! OK, SEE YOU LATER";
                }
            }
            else if (textType == ShopControllerStatements.AMMO_TYPE){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " ААА! ВЕРНО ВЕРНО! КАЖДОМУ ОРУЖИЮ НУЖНЫ ПАТРОНЫ И ЧЕМ БОЛЬШЕ, ТЕМ ЛУЧШЕ. ТЕБЕ НРАВИТЬСЯ СЛУШАТЬ ПАДАЮЩИЕ ГИЛЬЗЫ? МНЕ ОЧЕНЬ!";
                }
                else {
                    text = " YEAH! RIGHT! EVERY WEAPON NEEDS AMMO. DO YOU LIKE TO HEAR A RAIN OF CARTRIDGE CASES FALLING FROM YOUR WEAPON? I LIKE IT!";
                }
            }
            else if (textType == ShopControllerStatements.ANOTHER_OBJECTS_TYPE){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " КОНЕЧНО! У МЕНЯ НАЙДЕТСЯ ТО, ЧТО ТЕБЕ НУЖНО! ВЗГЛЯНИ!";
                }
                else {
                    text = " SURE! I HAVE ALREADY PREPARED ALL YOU NEED AND READY TO SHOW IT! CHOOSE SOMETHING PLEASE!";
                }
            }
            else if (textType == ShopControllerStatements.WATCH_ADDS){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = " НЕ ПРОБЛЕМА, МОЖЕШЬ ПОСМОТРЕТЬ РЕКЛАМУ И МЫ ДОГОВОРИМСЯ";
                }
                else {
                    text = " NO PROBLEM! YOU CAN SEE AN ADVERTISEMENT";
                }
            }
            else if (textType == ShopControllerStatements.ADDS_SHOWING){
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text = "";
                }
                else {
                    text = "";
                }
            }
            else {
                System.out.println("No text for this submenu");
                return "No text data";
            }

            return text;
        }


    interface ShopControllerStatements{
        int GREETING = 0;
        int MAIN_LIST = 1;
        int LEAVE = 2;
        int WEAPONS_LIST = 3;



        int AMMO_TYPE = 4;
        int ANOTHER_OBJECTS_TYPE = 5;
        //int ANOTHER_OBJECTS_TYPE = 6;
        int WATCH_ADDS = 6;


        int ADDS_SHOWING = 7;

        int AFTER_BUYING_THANKSGIVING = 10;
    }

    static class WeaponsList{
        static String REVOLVER = AddingNewCollectableObjectAction.REVOLVER;
        static String HANDGUN = AddingNewCollectableObjectAction.HANDGUN;
        static String SO_SHOTGUN = AddingNewCollectableObjectAction.SO_SHOTGUN;
        static String SHOTGUN = AddingNewCollectableObjectAction.SHOTGUN;
        static String HAND_GRENADE = AddingNewCollectableObjectAction.HAND_GRENADE;
        static  String GRENADE_LAUNCHER = AddingNewCollectableObjectAction.M79;
        static String SMG = AddingNewCollectableObjectAction.SMG;

        /*
        String REVOLVER = (WeaponType.REVOLVER.toString());
        String HANDGUN = (WeaponType.HANDGUN.name());
        String SO_SHOTGUN = "SAWED OFF SHOTGUN";
        String SHOTGUN = "PUMP ACTION SHOTGUN";
        String HAND_GRENADE = "HAND GRENADE";
        String GRENADE_LAUNCHER = "GRENADE LAUNCHER";
        String SMG = "SUB MACHINE GUN";
         */

        static void init(){
            if (Program.LANGUAGE == Program.RUSSIAN){

                /*
                REVOLVER = "Револьвер";
                HANDGUN = "Пистолет";
                SO_SHOTGUN = "Обрез";
                SHOTGUN = "Дробовик";
                HAND_GRENADE = "Ручная граната";
                GRENADE_LAUNCHER = "Гранатомет";
                SMG = "Пистолет-пулемет";
                 */
            }
        }
    }

    static class AmmoList{
        static String BULLETS_FOR_REVOLVER = "REVOLVER BULLETS";
        static String MAGAZINES_FOR_HANDGUN = "HANDGUN MAGAZINES";
        static String SHELLS_FOR_SHOTGUN = "SHOTGUN SHELLS";
        static String HAND_GRENADES = "HAND GRENADES";
        static String GRENADE_LAUNCHER = "40X46MM GRENADES";
        static String SMG = "SMG MAGAZINES";

        static void init(){
            if (Program.LANGUAGE == Program.RUSSIAN) {
                BULLETS_FOR_REVOLVER = "ПАТРОНЫ К РЕВОЛЬВЕРУ";
                MAGAZINES_FOR_HANDGUN = "МАГАЗИН К ПИСТОЛЕТУ";
                SHELLS_FOR_SHOTGUN = "ПАТРОНЫ 12-го КАЛИБРА";
                HAND_GRENADES = "РУЧНЫЕ ГРАНАТЫ";
                GRENADE_LAUNCHER = "ГРАНАТЫ 40х46 мм";
                SMG = "МАГАЗИН ДЛЯ ПП";
            }
        }

        public static String getAmmoNameForWeapon(WeaponType weaponType) {
            if (weaponType == WeaponType.REVOLVER) return  BULLETS_FOR_REVOLVER;
            else if (weaponType == WeaponType.HANDGUN) return  MAGAZINES_FOR_HANDGUN;
            else if (weaponType == WeaponType.SHOTGUN || weaponType== WeaponType.SAWED_OFF_SHOTGUN) return  SHELLS_FOR_SHOTGUN;
            else if (weaponType == WeaponType.GRENADE) return  HAND_GRENADES;
            else if (weaponType == WeaponType.M79) return  GRENADE_LAUNCHER;
            else if (weaponType == WeaponType.SMG) return  SMG;
            else {
                System.out.println("No data for this weapon type " + weaponType);
                 return null;
            }
        }

        public static WeaponType getWeaponTypeForAmmoName(String name) {
            if (name == BULLETS_FOR_REVOLVER) return WeaponType.REVOLVER;
            else if (name == MAGAZINES_FOR_HANDGUN) return  WeaponType.HANDGUN;
            else if (name == SHELLS_FOR_SHOTGUN) return  WeaponType.SHOTGUN;
            else if (name == HAND_GRENADES) return  WeaponType.GRENADE;
            else if (name == GRENADE_LAUNCHER) return  WeaponType.M79;
            else if (name == SMG) return  WeaponType.SMG;
            else {
                System.out.println("No data for this weapon name " + name);
                return null;
            }
        }
    }

    static abstract class WatchAdds{
        static String NO = "NO WAY";
        static String OK = "OK, LETS SEE IT";

        static void init(){
            if (Program.LANGUAGE == Program.RUSSIAN) {
                NO = "НИ ЗА ЧТО";
                OK = "Без проблем";
            }
        }

    }

    static abstract class ItemsList{
        static String SMALL_MEDICAL_KIT = "ENERGY DRINK";
        static String MEDIUM_MEDICAL_KIT = "PAINKILLER";
        static String LARGE_MEDICAL_KIT = "MEDICAL KIT";
        static String DRUGS = "DRUGS";

        static void init(){
            if (Program.LANGUAGE == Program.RUSSIAN) {
                SMALL_MEDICAL_KIT = "ЭНЕРГЕТИК";
                MEDIUM_MEDICAL_KIT = "ОБЕЗБОЛИВАЮЩЕЕ";
                LARGE_MEDICAL_KIT = "АПТЕЧКА";
                DRUGS = "Препарат";
            }
        }

        static int getCodeNumberForObject(String name){
            if (name.contains(SMALL_MEDICAL_KIT)){
                return AbstractCollectable.SMALL_MEDICAL_KIT;
            }
            else if (name.contains(MEDIUM_MEDICAL_KIT)){
                return AbstractCollectable.MEDIUM_MEDICAL_KIT;
            }
            else if (name.contains(LARGE_MEDICAL_KIT)) return AbstractCollectable.LARGE_MEDICAL_KIT;
            else if (name.contains(DRUGS)) return AbstractCollectable.SYRINGE;
            else{
                System.out.println("I dont know object code for the name: " + name);
                return -1;
            }
        }

        public static String getNameForType(int type) {
            if (type == AbstractCollectable.SMALL_MEDICAL_KIT){
                return SMALL_MEDICAL_KIT;
            }
            else if (type == AbstractCollectable.MEDIUM_MEDICAL_KIT){
                return MEDIUM_MEDICAL_KIT;
            }
            else if (type == AbstractCollectable.LARGE_MEDICAL_KIT){
                return LARGE_MEDICAL_KIT;
            }
            else if (type == AbstractCollectable.SYRINGE){
                return DRUGS;
            }
            else{
                System.out.println("I dont know object name for code: " + type);
                return "";
            }
        }
    }

    static abstract class ActionButtons{
        static String BUY = " BUY";
        static String CANCEL = "BACK";

        static String BUY_1 = "BUY 1 PC.";
        static String BUY_2 = "BUY 2 PC.";
        static String BUY_5 = "BUY 5 PCS.";
        static String BUY_10 = "BUY 10 PCS.";

        static void init(){
            if (Program.LANGUAGE == Program.RUSSIAN){
                BUY =    " КУПИТЬ ";
                BUY_1 = "КУПИТЬ 1 ШТ.";
                BUY_2 = "КУПИТЬ 2 ШТ.";
                BUY_5 = "КУПИТЬ 5 ШТ.";
                BUY_10 = "КУПИТЬ 10 ШТ.";
                CANCEL = "ОТМЕНИТЬ";
            }
        }

        static int getCountForButton(String buttonName){
            if (buttonName == BUY || buttonName == BUY_1) return 1;
            else if (buttonName == BUY_2) return 2;
            else if (buttonName == BUY_5) return 5;
            else if (buttonName == BUY_10) return 10;
            else {
                System.out.println("No count for button named " + buttonName);
                return -1;
            }
        }
    }

    public boolean isButtonsForActualSubmenuWereAdded() {
        return buttonsForActualSubmenuWereAdded;
    }

    public void setButtonsForActualSubmenuWereAdded(boolean buttonsForActualSubmenuWereAdded) {
        this.buttonsForActualSubmenuWereAdded = buttonsForActualSubmenuWereAdded;
    }

    public void setStatement(int statement) {
        this.statement = statement;
    }
}
