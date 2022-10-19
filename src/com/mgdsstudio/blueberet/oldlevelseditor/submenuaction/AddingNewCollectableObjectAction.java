package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.MedicalKit;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.SimpleCollectableElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectOnMapAddingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

import java.util.ArrayList;

public class AddingNewCollectableObjectAction extends SubmenuAction{
    private final static String VALUE = "Value to be added";

    public final static String COIN = "Coin";
    public final static String WEAPON = "Weapon";
    public final static String MUSHROOM = "Mushroom";
    public final static String STAR = "Star";
    public final static String GEM = "Gem";
    public final static String MONEY_COIN = "Coin";
    public final static String AMMO = "Ammo";
    public final static String MEDICAL_KIT = "Medical kit";
    public final static String SYRINGE = "Slow motion";
    public final static String FRUIT = "Meal";

    //fruits
    public final static String ANANAS = "Pineapple";
    public final static String CARROT = "Carrot";
    public final static String APPLE = "Apple";
    public final static String ORANGE = "Orange";
    public final static String WATERMELON = "Watermelon";

    //GEMS
    public final static String GREEN_GEM = "Green";
    public final static String RED_GEM = "Red";
    public final static String BLUE_GEM = "Blue";
    public final static String YELLOW_GEM = "Yellow";
    public final static String WHITE_GEM = "White";

    //Coins
    public final static String GOLD = "Gold";
    public final static String SILVER = "Silver";
    public final static String COPPER = "Copper";

    //Dimensions
    public final static String BIG = "Big";
    public final static String MEDIUM  ="Medium";
    public final static String SMALL = "Tiny";

    //Currency
    public final static String DOLLAR = "USD";
    public final static String YEN = "JPY";
    public final static String ROUBLE = "RUB";

    //Weapons
    public final static String REVOLVER = "Revolver";
    public final static String HANDGUN = "Handgun";
    public final static String SO_SHOTGUN = "Sawed-off shotgun";
    public final static String SHOTGUN = "Shotgun";
    public final static String SMG = "Submachine gun";
    public final static String M79 = "Greenade launcher";
    public final static String HAND_GRENADE = "Hand grenade";



    //Body types
    private final static String WITH_SPRING = "With spring";
    private final static String STATIC = "Static";
    private final static String DYNAMIC = "Dynamic normal gravity";
    private final static String DYNAMIC_0_GRAVITY = "Dynamic 0 gravity";

    private ObjectOnMapAddingController objectOnMapAddingController;
    private static byte lastRadioButtonForMainTypeWasSet = 0;
    private static byte lastRadioButtonForGemColorWasSet = 0;
    private static byte lastRadioButtonForCoinMaterialWasSet = 0;
    private static byte lastRadioButtonForGemDimensionWasSet = 0;
    private static byte lastRadioButtonForCoinDimensionWasSet = 0;
    private static byte lastRadioButtonForBodyTypeWasSet = 0;
    private static int lastKeyValueWasSet = 1;

    //private static byte objectType = lastRadioButtonForMainTypeWasSet;

    public static final byte MAIN_TYPE_SELECTING = 1;
    // for gems
    public static final byte GEM_DIMENSION_SELECTING = 2;
    public static final byte GEM_COLOR_SELECTING = 3;
    //for coins
    public static final byte COIN_TYPE_SELECTING = 4;
    public static final byte COIN_MATERIAL_SELECTING = 5;
    //for ammo
    public static final byte WEAPON_SELECTING = 6;
    public static final byte FRUIT_SELECTING = 7;

    public static final byte MEDICAL_KIT_DIMENSION_SELECTING = 8;

    //public static final byte VALUE_SELECTING = 6;

    public static final byte VALUE_SELECTING = 10;
    public static final byte BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD = 11;
    public static final byte SUCCESSFULLY_ADDED = 12;
    public static final byte RETURN = 13;
    public static final byte END = 14;

    //private int value = 50;

    public final static boolean BIG_DIMENSION = true;
    public final static boolean SMALL_DIMENSION = false;

    private int abstractObjectType;
    private int normalUserValueForSlider;


    public AddingNewCollectableObjectAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectOnMapAddingController = new ObjectOnMapAddingController();
        //ditor2D.setNewLocalStatement(VALUE_SELECTING);
        Editor2D.setNewLocalStatement(MAIN_TYPE_SELECTING);
        objectData.setClassName(SimpleCollectableElement.CLASS_NAME);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= MAIN_TYPE_SELECTING) {
            loadStartScreen(tab);
            if (Editor2D.localStatement != 1) Editor2D.localStatement = 1;
        }
        else if (localStatement == FRUIT_SELECTING){
            loadFruitSelectingScreen(tab);
        }
        else if (localStatement == GEM_DIMENSION_SELECTING) {
            loadGemDimensionsScreen(tab);
        }
        else if (localStatement == COIN_TYPE_SELECTING ){
            loadCoinTypeScreen(tab);
        }
        else if (localStatement == MEDICAL_KIT_DIMENSION_SELECTING){
            loadMedicalKitDimensionScreen(tab);
        }
        else if (localStatement == WEAPON_SELECTING){
            loadWeaponNameScreen(tab);
        }
        else if (localStatement == GEM_COLOR_SELECTING){
            loadGemColorSelectingScreen(tab);
        }
        else if (localStatement == COIN_MATERIAL_SELECTING){
            loadCoinMaterialSelectingScreen(tab);
        }
        else if (localStatement == VALUE_SELECTING){    // For every type
            loadValueSelectingScreen(tab, -1);
        }
        else if (localStatement == BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD){
            loadBodyTypeScreen(tab);
        }
    }

    private void loadFruitSelectingScreen(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        androidGUI_Element buttonPineapple = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ANANAS, false);
        tab.addGUI_Element(buttonPineapple, null);
        androidGUI_Element buttonCarrot = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CARROT, false);
        tab.addGUI_Element(buttonCarrot, null);
        androidGUI_Element buttonApple = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLE, false);
        tab.addGUI_Element(buttonApple, null);
        androidGUI_Element buttonOrange = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ORANGE, false);
        tab.addGUI_Element(buttonOrange, null);
        androidGUI_Element buttonWatermelon = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WATERMELON, false);
        tab.addGUI_Element(buttonWatermelon, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void updateMainTypeSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(COIN)){
            objectData.setType(AbstractCollectable.ABSTRACT_COIN);   // Must be endcoded after
            Editor2D.setNewLocalStatement(COIN_TYPE_SELECTING);
            abstractObjectType = AbstractCollectable.ABSTRACT_COIN;
        }
        else if (element.getName().equals(GEM)){
            objectData.setType(AbstractCollectable.ABSTRACT_GEM);
            Editor2D.setNewLocalStatement(GEM_DIMENSION_SELECTING);
            abstractObjectType = AbstractCollectable.ABSTRACT_GEM;
        }
        else if (element.getName().equals(AMMO)){
            objectData.setType(AbstractCollectable.ABSTRACT_AMMO);
            Editor2D.setNewLocalStatement(WEAPON_SELECTING);
            abstractObjectType = AbstractCollectable.ABSTRACT_AMMO;
        }
        else if (element.getName().equals(MEDICAL_KIT)){
            objectData.setType(AbstractCollectable.ABSTRACT_MEDICAL_KIT);
            Editor2D.setNewLocalStatement(MEDICAL_KIT_DIMENSION_SELECTING);
            abstractObjectType = AbstractCollectable.ABSTRACT_MEDICAL_KIT;
        }
        else if (element.getName().equals(FRUIT)){
            objectData.setType(AbstractCollectable.ABSTRACT_FRUIT);
            Editor2D.setNewLocalStatement(FRUIT_SELECTING);
            abstractObjectType = AbstractCollectable.ABSTRACT_FRUIT;
        }
        else if (element.getName().equals(SYRINGE)){
            objectData.setType(AbstractCollectable.SYRINGE);
            objectData.setLocalType(AbstractCollectable.SYRINGE);
            Editor2D.setNewLocalStatement(BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD);
            abstractObjectType = AbstractCollectable.SYRINGE;
        }
        else updateCancelPressing(element);
    }

    private void updateDimensionSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(BIG)){
            objectData.setDimensionType(BIG_DIMENSION);   // Must be endcoded after
            Editor2D.setNewLocalStatement(GEM_COLOR_SELECTING);
        }
        else if (element.getName().equals(SMALL)){
            objectData.setDimensionType(SMALL_DIMENSION);
            Editor2D.setNewLocalStatement(GEM_COLOR_SELECTING);
        }
        else updateCancelPressing(element);
    }

    private void updateMedicalKitDimetionSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(BIG)){
            objectData.setDimensionStringName(BIG);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.LARGE_MEDICAL_KIT);
            Editor2D.setNewLocalStatement(BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD);
        }
        else if (element.getName().equals(MEDIUM)){
            objectData.setDimensionStringName(MEDIUM);
            objectData.setLocalType(AbstractCollectable.MEDIUM_MEDICAL_KIT);
            Editor2D.setNewLocalStatement(BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD);
        }
        else if (element.getName().equals(SMALL)){
            objectData.setDimensionStringName(SMALL);
            objectData.setLocalType(AbstractCollectable.SMALL_MEDICAL_KIT);
            Editor2D.setNewLocalStatement(BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD);
        }
        else updateCancelPressing(element);
    }

    private void updateWeaponSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(SHOTGUN)){
            objectData.setWeaponName(SHOTGUN);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_SHOTGUN);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(HANDGUN)){
            objectData.setWeaponName(HANDGUN);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_PISTOL);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(REVOLVER)){
            objectData.setWeaponName(REVOLVER);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_REVOLVER);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(SMG)){
            objectData.setWeaponName(SMG);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_SMG);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(HAND_GRENADE)){
            objectData.setWeaponName(HAND_GRENADE);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_HAND_GRENADE);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(M79)){
            objectData.setWeaponName(M79);   // Must be endcoded after
            objectData.setLocalType(SimpleCollectableElement.BULLETS_FOR_M79);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else updateCancelPressing(element);
    }

    private void updateCoinTypeSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(DOLLAR)){
            objectData.setCurrency(DOLLAR);   // Must be endcoded after
            Editor2D.setNewLocalStatement(COIN_MATERIAL_SELECTING);
        }
        else if (element.getName().equals(YEN)){
            objectData.setCurrency(YEN);
            Editor2D.setNewLocalStatement(COIN_MATERIAL_SELECTING);
        }
        else if (element.getName().equals(ROUBLE)){
            objectData.setCurrency(ROUBLE);
            Editor2D.setNewLocalStatement(COIN_MATERIAL_SELECTING);
        }
        else updateCancelPressing(element);
    }

    private void updateFruitSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(ANANAS)){
            //objectData.setType(AbstractCollectable.ANANAS);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.ANANAS);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(CARROT)){
            //objectData.setType(AbstractCollectable.CARROT);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.CARROT);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(APPLE)){
            //objectData.setType(AbstractCollectable.APPLE);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.APPLE);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(ORANGE)){
            //objectData.setType(AbstractCollectable.ORANGE);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.ORANGE);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else if (element.getName().equals(WATERMELON)){
            //objectData.setType(AbstractCollectable.WATERMELON);   // Must be endcoded after
            objectData.setLocalType(AbstractCollectable.WATERMELON);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
        }
        else updateCancelPressing(element);
    }

    //Ready
    private void updateColorSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(RED_GEM)){
            objectData.setColorOrMaterialType(RED_GEM);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByColorAndDimension(objectData);

        }
        else if (element.getName().equals(BLUE_GEM)){
            objectData.setColorOrMaterialType(BLUE_GEM);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByColorAndDimension(objectData);
        }
        else if (element.getName().equals(WHITE_GEM)){
            objectData.setColorOrMaterialType(WHITE_GEM);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByColorAndDimension(objectData);
        }
        else if (element.getName().equals(YELLOW_GEM)){
            objectData.setColorOrMaterialType(YELLOW_GEM);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByColorAndDimension(objectData);
        }
        else if (element.getName().equals(GREEN_GEM)){
            objectData.setColorOrMaterialType(GREEN_GEM);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByColorAndDimension(objectData);
        }
        else updateCancelPressing(element);
    }

    private void saveUserSetValueForSlider(GameObjectDataForStoreInEditor objectData){
            int value = 1;
            if (abstractObjectType == AbstractCollectable.ABSTRACT_COIN){
                if (objectData.getCurrency() == ROUBLE){
                    if (objectData.getColorOrMaterialType() == COPPER) value = 1;
                    else if (objectData.getColorOrMaterialType() == SILVER) value = 3;
                    else value = 12;
                }
                else if (objectData.getCurrency() == YEN){
                    if (objectData.getColorOrMaterialType() == COPPER) value = 2;
                    else if (objectData.getColorOrMaterialType() == SILVER) value = 6;
                    else value = 20;
                }
                else if (objectData.getCurrency() == DOLLAR){
                    if (objectData.getColorOrMaterialType() == COPPER) value = 5;
                    else if (objectData.getColorOrMaterialType() == SILVER) value = 10;
                    else value = 30;
                }
            }
            else if (abstractObjectType == AbstractCollectable.ABSTRACT_GEM){
                if (objectData.getDimensionType() == SMALL_DIMENSION){
                    if (objectData.getColorOrMaterialType() == YELLOW_GEM) value = 12;
                    else if (objectData.getColorOrMaterialType() == BLUE_GEM) value = 35;
                    else if (objectData.getColorOrMaterialType() == RED_GEM) value = 45;
                    else if (objectData.getColorOrMaterialType() == GREEN_GEM) value = 60;
                    else value = 100;
                }
                else {
                    if (objectData.getColorOrMaterialType() == YELLOW_GEM) value = 25;
                    else if (objectData.getColorOrMaterialType() == BLUE_GEM) value = 60;
                    else if (objectData.getColorOrMaterialType() == RED_GEM) value = 80;
                    else if (objectData.getColorOrMaterialType() == GREEN_GEM) value = 100;
                    else value = 150;
                }
            }
            else if (abstractObjectType == AbstractCollectable.ABSTRACT_MEDICAL_KIT){
                if (objectData.getLocalType() == AbstractCollectable.SMALL_MEDICAL_KIT){
                    value = MedicalKit.RECOVERY_VALUE_FOR_SMALL;
                }
                else if (objectData.getLocalType() == AbstractCollectable.MEDIUM_MEDICAL_KIT){
                    value = MedicalKit.RECOVERY_VALUE_FOR_MEDIUM;
                }
                else value = MedicalKit.RECOVERY_VALUE_FOR_LARGE;
            }
            else if (abstractObjectType == AbstractCollectable.ABSTRACT_MEDICAL_KIT){
                if (objectData.getWeaponName() == HANDGUN) value = 1;
                else if (objectData.getWeaponName() == SHOTGUN) value = 7;
                else if (objectData.getWeaponName() == SMG) value = 2;
                else if (objectData.getWeaponName() == M79) value = 1;
            }
        normalUserValueForSlider = value;
    }

    private void updateCoinMaterialSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(GOLD)){
            objectData.setColorOrMaterialType(GOLD);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByMaterialAndCurrency(objectData);

        }
        else if (element.getName().equals(SILVER)){
            objectData.setColorOrMaterialType(SILVER);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByMaterialAndCurrency(objectData);
        }
        else if (element.getName().equals(COPPER)){
            objectData.setColorOrMaterialType(COPPER);
            Editor2D.setNewLocalStatement(VALUE_SELECTING);
            updateLocalTypeByMaterialAndCurrency(objectData);
        }
        else updateCancelPressing(element);
    }

    public void updateLocalTypeByMaterialAndCurrency(GameObjectDataForStoreInEditor objectData) {
        String materialType = objectData.getColorOrMaterialType();
        String currency  = objectData.getCurrency();
        byte localType = 0;
        if (materialType == GOLD){
            if (currency == DOLLAR) localType = AbstractCollectable.BIG_COIN_GOLD;
            else if (currency == YEN) localType = AbstractCollectable.MEDIUM_COIN_GOLD;
            else if (currency == ROUBLE) localType = AbstractCollectable.SMALL_COIN_GOLD;
            else System.out.println("This currency is not known");
        }
        else if (materialType == SILVER){
            if (currency == DOLLAR) localType = AbstractCollectable.BIG_COIN_SILVER;
            else if (currency == YEN) localType = AbstractCollectable.MEDIUM_COIN_SILVER;
            else if (currency == ROUBLE) localType = AbstractCollectable.SMALL_COIN_SILVER;
            else System.out.println("This currency is not known");
        }
        else if (materialType == COPPER){
            if (currency == DOLLAR) localType = AbstractCollectable.BIG_COIN_COPPER;
            else if (currency == YEN) localType = AbstractCollectable.MEDIUM_COIN_COPPER;
            else if (currency == ROUBLE) localType = AbstractCollectable.SMALL_COIN_COPPER;
            else System.out.println("This currency is not known");
        }
        else System.out.println("This material is not known");
        objectData.setLocalType(localType);
    }

    public void updateLocalTypeByColorAndDimension(GameObjectDataForStoreInEditor objectData) {
        String colorType = objectData.getColorOrMaterialType();
        boolean dimensionType = objectData.getDimensionType();
        byte localType = 0;
        if (colorType == AddingNewCollectableObjectAction.BLUE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_BLUE_STONE;
            else localType = AbstractCollectable.SMALL_BLUE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.RED_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_RED_STONE;
            else localType = AbstractCollectable.SMALL_RED_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.GREEN_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_GREEN_STONE;
            else localType = AbstractCollectable.SMALL_GREEN_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.WHITE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_WHITE_STONE;
            else localType = AbstractCollectable.SMALL_WHITE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.YELLOW_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_YELLOW_STONE;
            else localType = AbstractCollectable.SMALL_YELLOW_STONE;
        }
        objectData.setLocalType(localType);
    }

    private void updateValueSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData, ArrayList <androidGUI_Element> elements){
        if (element.getName().equals(APPLY)){
            for (androidGUI_Element someElement: elements){
                if (someElement.getClass() == androidAndroidGUI_Slider.class){
                    androidAndroidGUI_Slider slider = (androidAndroidGUI_Slider) someElement;
                    objectData.setKeyValue(slider.getValue());
                    lastKeyValueWasSet = slider.getValue();
                    System.out.println("Value was set on: " + lastKeyValueWasSet);
                }
            }
            Editor2D.setNewLocalStatement(BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD);
        }
        else updateCancelPressing(element);
    }

    private void updateBodyTypeSelecting(androidGUI_Element element, GameObjectDataForStoreInEditor objectData){
        if (element.getName().equals(STATIC)){
            objectData.setBodyType(BodyType.STATIC);   // Must be endcoded after
        }
        else if (element.getName().equals(DYNAMIC)){
            objectData.setBodyType(BodyType.DYNAMIC);
            objectData.setWithSpring(false);
        }
        else if (element.getName().equals(DYNAMIC_0_GRAVITY)){
            objectData.setBodyType(BodyType.DYNAMIC);
            objectData.setWithSpring(false);
            objectData.setNoGravity(true);
        }
        else if (element.getName().equals(WITH_SPRING)){
            objectData.setBodyType(BodyType.DYNAMIC);
            objectData.setWithSpring(true);
        }
        else updateCancelPressing(element);
    }

    protected void tabUpdating(ArrayList <androidGUI_Element> elements, GameObjectDataForStoreInEditor objectData) {
        for (byte i = 0; i < elements.size(); i++) {
            if (elements.get(i).getStatement() == androidGUI_Element.RELEASED) {
                if (Editor2D.localStatement == MAIN_TYPE_SELECTING){
                    updateMainTypeSelecting(elements.get(i), objectData);
                }
                else if (Editor2D.localStatement == FRUIT_SELECTING){
                    updateFruitSelecting(elements.get(i), objectData);
                }
                else if (Editor2D.localStatement == GEM_DIMENSION_SELECTING){
                    updateDimensionSelecting(elements.get(i), objectData);
                }
                else if (Editor2D.localStatement == GEM_COLOR_SELECTING){
                    updateColorSelecting(elements.get(i), objectData);
                    saveUserSetValueForSlider(objectData);
                }// end gem
                else if (Editor2D.localStatement == COIN_TYPE_SELECTING){
                    updateCoinTypeSelecting(elements.get(i), objectData);
                }
                else if (Editor2D.localStatement == COIN_MATERIAL_SELECTING){
                    updateCoinMaterialSelecting(elements.get(i), objectData);
                    saveUserSetValueForSlider(objectData);
                }
                //end coins
                //for weapon
                else if (Editor2D.localStatement == WEAPON_SELECTING){
                    updateWeaponSelecting(elements.get(i), objectData);
                    saveUserSetValueForSlider(objectData);
                }
                //endWeapon
                //for medical
                else if (Editor2D.localStatement == MEDICAL_KIT_DIMENSION_SELECTING){
                    updateMedicalKitDimetionSelecting(elements.get(i), objectData);
                    saveUserSetValueForSlider(objectData);
                }
                //end medical
                else if (Editor2D.localStatement == VALUE_SELECTING){
                    updateValueSelecting(elements.get(i), objectData, elements);
                }
                else if (Editor2D.localStatement == BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD){
                    updateBodyTypeSelecting(elements.get(i), objectData);
                    lastRadioButtonForBodyTypeWasSet = i;
                }
            }
        }
    }

    private void initDataForGemsDimensionSelecting(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BIG);
        tab.addGUI_Element(element, null);
        androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SMALL);
        tab.addGUI_Element(element2, null);
        tab.getElements().get(lastRadioButtonForGemDimensionWasSet).setStatement(androidGUI_Element.PRESSED);
        //objectType = lastRadioButtonForMainTypeWasSet;
        ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
        allRadioButtons.add(element);
        allRadioButtons.add(element2);
        element.addAnotherRadioButtonsInGroup(allRadioButtons);
        element2.addAnotherRadioButtonsInGroup(allRadioButtons);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 22 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void loadStartScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonGem = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GEM, false);
        tab.addGUI_Element(buttonGem, null);
        androidGUI_Element buttonCoin = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MONEY_COIN, false);
        tab.addGUI_Element(buttonCoin, null);
        androidGUI_Element buttonAmmo = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AMMO, false);
        tab.addGUI_Element(buttonAmmo, null);
        androidGUI_Element buttonMedicalKit = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MEDICAL_KIT, false);
        tab.addGUI_Element(buttonMedicalKit, null);
        androidGUI_Element buttonFruit = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FRUIT, false);
        tab.addGUI_Element(buttonFruit, null);
        androidGUI_Element buttonSlowMo = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SYRINGE, false);
        tab.addGUI_Element(buttonSlowMo, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());        
    }

    private void loadGemDimensionsScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonBig = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BIG, false);
        tab.addGUI_Element(buttonBig, null);
        androidGUI_Element buttonSmall = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SMALL, false);
        tab.addGUI_Element(buttonSmall, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadCoinTypeScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonDollar = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DOLLAR, false);
        tab.addGUI_Element(buttonDollar, null);
        androidGUI_Element buttonYen = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, YEN, false);
        tab.addGUI_Element(buttonYen, null);
        androidGUI_Element buttonRouble = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ROUBLE, false);
        tab.addGUI_Element(buttonRouble, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadWeaponNameScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();

        androidGUI_Element buttonRevolver = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, REVOLVER, false);
        tab.addGUI_Element(buttonRevolver, null);
        androidGUI_Element buttonPistole = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HANDGUN, false);
        tab.addGUI_Element(buttonPistole, null);
        androidGUI_Element buttonShotgun = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SHOTGUN, false);
        tab.addGUI_Element(buttonShotgun, null);
        androidGUI_Element buttonM79 = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, M79, false);
        tab.addGUI_Element(buttonM79, null);
        androidGUI_Element buttonSMG = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SMG, false);
        tab.addGUI_Element(buttonSMG, null);
        androidGUI_Element buttonHandGrenade = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HAND_GRENADE, false);
        tab.addGUI_Element(buttonHandGrenade, null);


        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadMedicalKitDimensionScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonBig = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BIG, false);
        tab.addGUI_Element(buttonBig, null);
        androidGUI_Element buttonMedium = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MEDIUM, false);
        tab.addGUI_Element(buttonMedium, null);
        androidGUI_Element buttonSmall = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SMALL, false);
        tab.addGUI_Element(buttonSmall, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadWeaponSelecting(androidGUI_ScrollableTab tab){
        tab.clearElements();
        /*
        GUI_Element buttonRevolver = new GUI_Button(new Vec2(((tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, REVOLVER, false);
        tab.addGUI_Element(buttonRevolver, null);*/
        androidGUI_Element buttonPistol = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HANDGUN, false);
        tab.addGUI_Element(buttonPistol, null);
        androidGUI_Element buttonShotgun = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SHOTGUN, false);
        tab.addGUI_Element(buttonShotgun, null);
        androidGUI_Element buttonSMG = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SMG, false);
        tab.addGUI_Element(buttonSMG, null);
        /*
        GUI_Element buttonHandGreenade = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HAND_GRENADE, false);
        tab.addGUI_Element(buttonHandGreenade, null);*/
        androidGUI_Element buttonM79 = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonM79, null);

        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadGemColorSelectingScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonGreen = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GREEN_GEM, false);
        tab.addGUI_Element(buttonGreen, null);
        androidGUI_Element buttonRed = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, RED_GEM, false);
        tab.addGUI_Element(buttonRed, null);
        androidGUI_Element buttonBlue = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BLUE_GEM, false);
        tab.addGUI_Element(buttonBlue, null);
        androidGUI_Element buttonYellow = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, YELLOW_GEM, false);
        tab.addGUI_Element(buttonYellow, null);
        androidGUI_Element buttonWhite = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WHITE_GEM, false);
        tab.addGUI_Element(buttonWhite, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void loadCoinMaterialSelectingScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonGold = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GOLD, false);
        tab.addGUI_Element(buttonGold, null);
        androidGUI_Element buttonSilver = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SILVER, false);
        tab.addGUI_Element(buttonSilver, null);
        androidGUI_Element buttonCopper = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, COPPER, false);
        tab.addGUI_Element(buttonCopper, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }



    private void loadValueSelectingScreen(androidGUI_ScrollableTab tab, int max) {
        tab.clearElements();
        if (max<0) max = 100;
        int min = 1;
        if (abstractObjectType == AbstractCollectable.ABSTRACT_MEDICAL_KIT) max = 100;  // In percent
        int valueToBeSet = normalUserValueForSlider;
        if (valueToBeSet >max) valueToBeSet = max;
        else if (valueToBeSet <min) valueToBeSet = min;
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "", min, max);
        slider.setText(Integer.toString(max));
        slider.setUserValue(valueToBeSet);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, VALUE, true, min, max);
        String text = "Set value to be added";

        int valueToBeAdded = getNormalValueForObject();

        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        textField.setText(text);
        ((androidAndroidGUI_Slider) slider).setValue(valueToBeAdded);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private int getNormalValueForObject() {
        if (objectData.getType() == AbstractCollectable.ABSTRACT_AMMO){
            return lastKeyValueWasSet;
        }
        else {
            int normalValue = AbstractCollectable.getNormalValueForLocalObjectType(objectData.getLocalType());
            if (Program.debug) System.out.println("For this collectable the normal value is " + normalValue);
            return normalValue;
        }
    }

    private void loadBodyTypeScreen(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WITH_SPRING);
        tab.addGUI_Element(element, null);
        androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, STATIC);
        tab.addGUI_Element(element2, null);
        androidAndroidGUI_RadioButton element3 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DYNAMIC);
        tab.addGUI_Element(element3, null);
        androidAndroidGUI_RadioButton element4 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DYNAMIC_0_GRAVITY);
        tab.addGUI_Element(element4, null);
        int last = lastRadioButtonForBodyTypeWasSet;
        if (last > (tab.getElements().size()-1)) last = 0;
        tab.getElements().get(last).setStatement(androidGUI_Element.PRESSED);
        ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
        allRadioButtons.add(element);
        allRadioButtons.add(element2);
        allRadioButtons.add(element3);
        allRadioButtons.add(element4);
        element.addAnotherRadioButtonsInGroup(allRadioButtons);
        element2.addAnotherRadioButtonsInGroup(allRadioButtons);
        element3.addAnotherRadioButtonsInGroup(allRadioButtons);
        element4.addAnotherRadioButtonsInGroup(allRadioButtons);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 22 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }


    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (releasedElements.size()>0) {
                tabUpdating(tabController.getTab().getElements(), objectData);
            }
        }
    }



    protected void setConsoleText(OnScreenConsole console, String text){
        actualConsoleText.clear();
        actualConsoleText.add(text);
        console.recalculateFontDimension();
        console.setText(actualConsoleText);
    }

    /*
        public static final byte MAIN_TYPE_SELECTING = 0;
        // for gems
        public static final byte GEM_DIMENSION_SELECTING = 1;
        public static final byte GEM_COLOR_SELECTING = 2;
        //for coins
        public static final byte COIN_TYPE_SELECTING = 3;
        public static final byte COIN_MATERIAL_SELECTING = 4;
        //for ammo
        public static final byte WEAPON_SELECTING = 5;

        public static final byte MEDICAL_KIT_DIMENSION_SELECTING = 7;

        //public static final byte VALUE_SELECTING = 6;

        public static final byte VALUE_SELECTING = 9;
        public static final byte BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD = 10;
        public static final byte SUCCESSFULLY_ADDED = 11;
        public static final byte RETURN = 12;
        public static final byte END = 13;
         */
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            if (Editor2D.localStatement <= MAIN_TYPE_SELECTING) setConsoleText(onScreenConsole, "Select type for new element");
            else if (Editor2D.localStatement == GEM_DIMENSION_SELECTING) setConsoleText(onScreenConsole, "Select dimension for gem");
            else if (Editor2D.localStatement == GEM_COLOR_SELECTING) setConsoleText(onScreenConsole, "Which color must have new gem?");
            else if (Editor2D.localStatement == COIN_TYPE_SELECTING) setConsoleText(onScreenConsole, "Select currency");
            else if (Editor2D.localStatement == COIN_MATERIAL_SELECTING) setConsoleText(onScreenConsole, "What about material for coin?");
            else if (Editor2D.localStatement == WEAPON_SELECTING) setConsoleText(onScreenConsole, "Which weapon?");
            else if (Editor2D.localStatement == VALUE_SELECTING) setConsoleText(onScreenConsole, "Set key value");
            else if (Editor2D.localStatement == BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD) setConsoleText(onScreenConsole, " Place object on the field ");
            else if (Editor2D.localStatement == SUCCESSFULLY_ADDED) setConsoleText(onScreenConsole, "Successfully added!");
            else if (Editor2D.localStatement == RETURN) setConsoleText(onScreenConsole, "Successfully added!");
            else if (Editor2D.localStatement == END) setConsoleText(onScreenConsole, "Successfully added!");

            /*
            try {
                if (Editor2D.localStatement <= VALUE_SELECTING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place object in the world");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement> VALUE_SELECTING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            catch (Exception e) {
                System.out.println("Can not change the text of the console " + e);
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully");
                onScreenConsole.setText(actualConsoleText);
            }
            */

        }
    }

    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        //System.out.println("Local: " + Editor2D.localStatement);
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement == BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD){
            objectOnMapAddingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (objectOnMapAddingController.canBeNewObjectAdded()){
                Editor2D.setNewLocalStatement(SUCCESSFULLY_ADDED);
            }
        }
        else if (Editor2D.localStatement == SUCCESSFULLY_ADDED){
            if (Editor2D.canBeNextOperationMade()) {
                PVector nearestPointPos = LevelsEditorProcess.getNearestPointOnGrid(levelsEditorProcess.getEditorCamera(), new PVector(Program.engine.mouseX, Program.engine.mouseY));
                if (nearestPointPos != null){
                    objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
                    saveData(tabController, objectData);
                    Editor2D.setNextLocalStatement();
                    objectOnMapAddingController.endAdding();
                    System.out.println("Data stores in object Data");
                    objectOnMapAddingController.switchOffTimer();
                }
                else {
                    System.out.println("Point is not on the map zone");
                    Editor2D.setNewLocalStatement((byte)0);
                }
            }
        }
        else if (Editor2D.localStatement == RETURN){
            if (Editor2D.canBeNextOperationMade()) {
                Editor2D.setNewLocalStatement(END);
            }
        }
    }

    private void saveData(ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData) {
        /*
        ArrayList <GUI_Element> elements = tabController.getTab().getElements();
        System.out.println("Radio button " + lastRadioButtonForMainTypeWasSet + " was set");
        if (elements.get(lastRadioButtonForMainTypeWasSet).getName() == COIN){
            System.out.println("Coin was added");
            objectData.setType(CollectableObjectInNesStyle.ABSTRACT_COIN);
        }
        else if (elements.get(lastRadioButtonForMainTypeWasSet).getName() == MUSHROOM){
            System.out.println("Mushroom was added");
            objectData.setType(CollectableObjectInNesStyle.MUSHROOM);
        }
        else if (elements.get(lastRadioButtonForMainTypeWasSet).getName() == STAR){
            System.out.println("Star was added");
            objectData.setType(CollectableObjectInNesStyle.STAR);
        }
        objectData.setWithSpring(true);

        objectData.setPathToTexture(Programm.getRelativePathToAssetsFolder()+ CollectableObjectInNesStyle.getPathToMainTextureForType(objectData.getType()));
        objectData.setLeftUpperGraphicCorner(CollectableObjectInNesStyle.getLeftUpperGraphicCornerForType(objectData.getType()));
        objectData.setRightLowerGraphicCorner(CollectableObjectInNesStyle.getRightLowerGraphicCornerForType(objectData.getType()));
        objectData.setGraphicWidth(objectData.getDimension());
        objectData.setGraphicHeight(objectData.getDimension());
        objectData.setRowsNumber(CollectableObjectInNesStyle.getRowsNumberForAnimation(objectData.getType()));
        objectData.setCollumnsNumber(CollectableObjectInNesStyle.getCollumnsNumberForAnimation(objectData.getType()));
        objectData.setAnimationFrequency(CollectableObjectInNesStyle.getAnimationFrequency(objectData.getType()));

        */
    }


    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (Editor2D.localStatement == BODY_TYPE_SELECTING_AND_PLACING_ON_FIELD) {
            objectOnMapAddingController.draw(gameCamera, levelsEditorProcess);
        }
    }

    @Override
    public byte getEndValue(){
        return END;
    }


    /*
    public final static String COIN = "Coin";
    public final static String WEAPON = "Weapon";
    public final static String MUSHROOM = "Mushroom";
    public final static String STAR = "Star";
    public final static String GEM = "Gem";
    public final static String MONEY_COIN = "Coin";
    public final static String AMMO = "Ammo";

    private ObjectOnMapAddingController objectOnMapAddingController;
    private static byte lastRadioButtonWasSet = 0;
    private static byte objectType = lastRadioButtonWasSet;

    public static final byte ADDING = 0;
    public static final byte SUCCESFULLY_ADDED = 1;
    public static final byte RETURN = 2;
    public static final byte END = 3;

    public AddingNewCollectableObjectAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(CollectableObjectInNesStyle.CLASS_NAME);
        objectOnMapAddingController = new ObjectOnMapAddingController();
        Editor2D.setNewLocalStatement(ADDING);
    }

    @Override
    public void reconstructTab(GUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == ADDING) {
            tab.clearElements();
            tab.setMinimalHeight();
            GUI_RadioButton element = new GUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(GUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, COIN);
            tab.addGUI_Element(element, null);
            GUI_RadioButton element2 = new GUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(GUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MUSHROOM);
            tab.addGUI_Element(element2, null);
            GUI_RadioButton element3 = new GUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(GUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, STAR);
            tab.addGUI_Element(element3, null);
            tab.getElements().get(lastRadioButtonWasSet).setStatement(GUI_Element.PRESSED);
            objectType = lastRadioButtonWasSet;

            ArrayList<GUI_RadioButton> allRadioButtons = new ArrayList<>();
            allRadioButtons.add(element);
            allRadioButtons.add(element2);
            allRadioButtons.add(element3);

            element.addAnotherRadioButtonsInGroup(allRadioButtons);
            element2.addAnotherRadioButtonsInGroup(allRadioButtons);
            element3.addAnotherRadioButtonsInGroup(allRadioButtons);
            GUI_Element buttonCancel = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 22 + 2 * (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
        }
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<GUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<GUI_Element> releasedElements = tabController.getReleasedElements();
            if (releasedElements.size()>0) {
                tabUpdating(tabController.getTab().getElements(), releasedElements);
            }
        }
    }

    protected void tabUpdating(ArrayList <GUI_Element> elements, ArrayList<GUI_Element> releasedElements) {
        for (byte i = 0; i < elements.size(); i++) {
            if (elements.get(i).getStatement() == GUI_Element.RELEASED) {
                if (lastRadioButtonWasSet != i) {
                    System.out.println(" released  Radio button is: " + i);
                    lastRadioButtonWasSet = i;
                }
            }
        }
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ADDING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place object in the world");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement>ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            catch (Exception e) {
                System.out.println("Can not change the text of the console " + e);
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully");
                onScreenConsole.setText(actualConsoleText);
            }

        }
    }

    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        //System.out.println("Local: " + Editor2D.localStatement);
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement <= ADDING){
            objectOnMapAddingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (objectOnMapAddingController.canBeNewObjectAdded()){
                Editor2D.setNewLocalStatement(SUCCESFULLY_ADDED);
            }
        }
        else if (Editor2D.localStatement == SUCCESFULLY_ADDED){
            if (Editor2D.canBeNextOperationMade()) {
                //PVector nearestPointPos = LevelsEditorProcess.getPointInWorldPosition(levelsEditorProcess.getEditorCamera(), new PVector(Game2D.engine.mouseX, Game2D.engine.mouseY));
                PVector nearestPointPos = LevelsEditorProcess.getNearestPointOnGrid(levelsEditorProcess.getEditorCamera(), new PVector(Programm.engine.mouseX, Programm.engine.mouseY));
                objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
                saveData(tabController, objectData);
                Editor2D.setNextLocalStatement();
                objectOnMapAddingController.endAdding();
                System.out.println("Data stores in object Data");
                objectOnMapAddingController.switchOffTimer();
            }
        }
        else if (Editor2D.localStatement == RETURN){
            if (Editor2D.canBeNextOperationMade()) {
                Editor2D.setNewLocalStatement(END);
            }
        }
    }

    private void saveData(ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData) {
        ArrayList<GUI_Element> releasedElements = tabController.getReleasedElements();
        ArrayList <GUI_Element> elements = tabController.getTab().getElements();

        System.out.println("Radio button " + lastRadioButtonWasSet + " was set");
        if (elements.get(lastRadioButtonWasSet).getName() == COIN){
            System.out.println("Coin was added");
            objectData.setType(CollectableObjectInNesStyle.COIN);
        }
        else if (elements.get(lastRadioButtonWasSet).getName() == MUSHROOM){
            System.out.println("Mushroom was added");
            objectData.setType(CollectableObjectInNesStyle.MUSHROOM);
        }
        else if (elements.get(lastRadioButtonWasSet).getName() == STAR){
            System.out.println("Star was added");
            objectData.setType(CollectableObjectInNesStyle.STAR);
        }
        objectData.setWithSpring(true);
        objectData.setPathToTexture(Programm.getRelativePathToAssetsFolder()+ CollectableObjectInNesStyle.getPathToMainTextureForType(objectData.getType()));
        objectData.setLeftUpperGraphicCorner(CollectableObjectInNesStyle.getLeftUpperGraphicCornerForType(objectData.getType()));
        objectData.setRightLowerGraphicCorner(CollectableObjectInNesStyle.getRightLowerGraphicCornerForType(objectData.getType()));
        objectData.setGraphicWidth(objectData.getDimension());
        objectData.setGraphicHeight(objectData.getDimension());
        objectData.setRowsNumber(CollectableObjectInNesStyle.getRowsNumberForAnimation(objectData.getType()));
        objectData.setCollumnsNumber(CollectableObjectInNesStyle.getCollumnsNumberForAnimation(objectData.getType()));
        objectData.setAnimationFrequency(CollectableObjectInNesStyle.getAnimationFrequency(objectData.getType()));
    }


    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        objectOnMapAddingController.draw(gameCamera, levelsEditorProcess);
    }

    @Override
    public byte getEndValue(){
        return END;
    }

    */

}
