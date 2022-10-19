package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.RepeatingBackgroundElement;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.graphic.background.SingleColorBackground;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.NPC_AddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding.AddingRoundElement;
import com.mgdsstudio.blueberet.loading.DeleteStringsMaster;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public class AddingNewBackgroundAction extends AddingRoundElement implements RoundElementAddingConstants{

    public final static byte MANAGING = 1;
    public final static byte BACKGROUND_DELETING = 2;
    public final static byte BACKGROUND_TYPE_SELECTING = 3;
    public final static byte BACKGROUND_SINGLE_COLOR_SELECTING = 4;

    public final static byte NEW_OR_EXISTING_TILESET = 6;
    public final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = 7;
    public final static byte BACKGROUND_IN_DIRECTORY_CHOOSING = 8;
    public final static byte UPLOAD_TILESET_FROM_STORAGE_TO_CACHE = 9;
    public final static byte SET_POSITION_BACKGROUND = 10;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 11;
    public final static byte SET_LEFT_UPPER_CORNER_FOR_SINGLE_ELEMENT = 12;
    public final static byte SET_RIGHT_LOWER_FOR_SINGLE_ELEMENT = 13;
    public final static byte BACKGROUND_ADJUSTING = 14;
    //public final static byte SET_RELATIVE_VELOCITY = 13;

    public final static byte TEXTURE_REGION_CHOOSING = 16;
    public final static byte SINGLE_ELEMENT_REPEATABILITY = 17;
    public final static byte END = 19;

    //GUI names
    //MANAGING MENU
    private final String ADD_NEW_BACKGROUND_NAME = "Add new background";
    private final String DELETE_BACKGROUND_NAME = "Delete background";
    //BACKGROUND_TYPE_SELECTING
    private final String SCROLLABLE_PICTURE_BACKGROUND_NAME = "Scrollable background";
    private final String REPEATING_BACKGROUND_ELEMENT_NAME = "Single element";
    private final String SINGLE_COLOR_BACKGROUND_NAME = "Single color background";
    //BACKGROUND_SINGLE_COLOR_SELECTING
    private final String RED = "Red";
    private final String GREEN = "Green";
    private final String BLUE = "Blue";
    //SINGLE_ELEMENT_REPEATABILITY
    private final String DISTANCE_TO_NEXT_ELEMENT = "Distance to next";
    private Background background;
    private boolean backgroundCreated;

    //Radio button names
    private final static String ALIGN_TO_UPPER_LEFT = "Align to upper left";
    private final static String ALIGN_TO_UPPER_RIGHT = "Align to upper right";
    private final static String ALIGN_TO_LOWER_LEFT = "Align to lower left";
    private final static String ALIGN_TO_LOWER_RIGHT = "Align to lower right";
    private final static String ALIGN_TO_CENTER = "Align to center";

    //Adjusting Menu names
    /*
    private final String UP = "↑";
    private final String DOWN = "↓";
    private final String LEFT = "←";
    private final String RIGHT = "→";*/
    private final static int MINIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND = 300;
    private final static int MINIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND = 150;
    private final static int MAXIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND = 5000;
    private final static int MAXIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND = 3500;
    private final static int MINIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND = 5;
    private final static int MINIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND = 5;
    private final static int MAXIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND = 200;
    private final static int MAXIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND = 200;
    private final static float MINIMAL_RELATIVE_VELOCITY = 2;
    private final static float MAXIMAL_RELATIVE_VELOCITY = 100;
    private static int widthBasicValue = (int)(MINIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND +(MAXIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND - MINIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND)*0.25f);
    private static int heightBasicValue = (int)(MINIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND +(MAXIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND - MINIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND)*0.25f);
    private static float relativeVelocityBasicValue = MAXIMAL_RELATIVE_VELOCITY/2;
    private final String WIDTH_STRING = "Width in px";
    private final String HEIGHT_STRING = "Height in px";
    private final String RELATIVE_VELOCITY_STRING = "Relative velocity";
    /*
    private final String UP = "↑";
    private final String DOWN = "Down";
    private final String LEFT = "Left";
    private final String RIGHT = "Right";
    */
    private final float MOVEMENT_ACCELERATE = 1.6f;
    private float movementVelocity = 0;
    private byte backgroundType = Background.SCROLLABLE_PICTURE_BACKGROUND;
    private static boolean backgroundWasCompleteSetUp;


    //basic background data
    //private static int basicWidth = (int)(Programm.engine.width*1.5f);
    //private static int basicHeight = (int)(Programm.engine.width*0.9f);


    //String path, int width, int height, int leftUpperX, int leftUpperY, float relativeVelocity
    //private final String DELETE_BACKGROUND_NAME = "Unmark backgrounds you want to delete";

    private NPC_AddingController backgroundAddingController;
    private PointAddingController singleElementFirstPointAddingController, singleElementSecondPointAddingController;
    private RectangularElementAdding singleElementAdding;
    private static int basicStep = 200;
    private static int singleElementBasicWidth = (int)(MINIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND +(MAXIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND - MINIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND)*0.25f);
    private static int singleElementBasicHeight = (int)(MINIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND +(MAXIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND - MINIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND)*0.25f);
    private final static int MIN_STEP_VALUE = (int)(singleElementBasicWidth*2);
    private final static int MAX_STEP_VALUE = 2000;
    private static int basicStepValue = (int)(MIN_STEP_VALUE +(MAX_STEP_VALUE - MIN_STEP_VALUE)*0.25f);
    private final static String STEP_STRING = "Step along x";
    private static StaticSprite staticSpriteForRepeatinElement;



    private ArrayList <Background> backgroundsForDeleting;



    public AddingNewBackgroundAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(Background.CLASS_NAME);
        Editor2D.localStatement = MANAGING;
    }

    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        System.out.println("reconstruction " + Editor2D.localStatement);
        if (localStatement <= MANAGING) {
            if (backgroundAddingController != null ) backgroundAddingController = null;
            tab.clearElements();
            androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ADD_NEW_BACKGROUND_NAME, false);
            tab.addGUI_Element(buttonApply, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DELETE_BACKGROUND_NAME, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            Editor2D.localStatement = MANAGING;
        }
        else if (localStatement == BACKGROUND_DELETING) {
            createDeleteBackgroundsMenu(tab);
        }
        else if (localStatement == BACKGROUND_TYPE_SELECTING) {
            tab.clearElements();
            tab.setMinimalHeight();
            androidGUI_Element buttonSinglePicture = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SCROLLABLE_PICTURE_BACKGROUND_NAME, false);
            tab.addGUI_Element(buttonSinglePicture, null);
            androidGUI_Element buttonSingleColor = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SINGLE_COLOR_BACKGROUND_NAME, false);
            tab.addGUI_Element(buttonSingleColor, null);
            androidGUI_Element buttonSingleElement = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, REPEATING_BACKGROUND_ELEMENT_NAME, false);
            tab.addGUI_Element(buttonSingleElement, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (localStatement == UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            clearAndMinimizeTab(tab);
        }
        else if (localStatement == BACKGROUND_SINGLE_COLOR_SELECTING) {
            int yForCancelElement = tab.getLastElementPosition();
            int yForApplyElement = tab.getElementPositionY(tab.getElements().size()-2);
            tab.clearElements();
            int elementWidth = (int)(tab.getWidth()/6.5f);
            int elementHeight = (int)(yForApplyElement*0.6f);
            int distanceToCenter = (int)(yForApplyElement/2.1f);
            System.out.println("Width: " + elementWidth + "; Height:" + elementHeight);
            androidGUI_Element red = new androidAndroidGUI_ColorPicker(new Vec2(((3*tab.getWidth() / 12)), (int)(distanceToCenter)), elementWidth, elementHeight, RED, androidAndroidGUI_ColorPicker.RED);
            tab.addGUI_Element(red, null);
            androidGUI_Element green = new androidAndroidGUI_ColorPicker(new Vec2(((6*tab.getWidth() / 12)), (int)(distanceToCenter)), elementWidth, elementHeight, GREEN, androidAndroidGUI_ColorPicker.GREEN);
            tab.addGUI_Element(green, null);
            androidGUI_Element blue = new androidAndroidGUI_ColorPicker(new Vec2(((9*tab.getWidth() / 12)), (int)(distanceToCenter)), elementWidth, elementHeight, BLUE, androidAndroidGUI_ColorPicker.BLUE);
            tab.addGUI_Element(blue, null);
            androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), yForApplyElement), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
            tab.addGUI_Element(buttonApply, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((1 * tab.getWidth() / 2)), yForCancelElement), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            tab.recalculateHeight(tab.getElements());
            tab.setScrollable(false);
        }
        else if (localStatement == NEW_OR_EXISTING_TILESET){
            createNewOrExistingTilesetMenu(tab);
        }
        else if (localStatement == BACKGROUND_IN_DIRECTORY_CHOOSING) {
            if (backgroundType == ScrollableAlongXBackground.SCROLLABLE_PICTURE_BACKGROUND) createBackgroundsInDirectoryChoosingMenu(tab, backgroundStartName, backgroundExtensions);
        }
        else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING){
            if (backgroundType == ScrollableAlongXBackground.REPEATING_BACKGROUND_ELEMENTS) createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);

        }
        else if (localStatement == SET_POSITION_BACKGROUND){
            createSetOnScreenPositionBackground(tab);
        }
        else if (localStatement == BACKGROUND_ADJUSTING){
            if (backgroundType == Background.SCROLLABLE_PICTURE_BACKGROUND) createSetWidthAndHeightMenuForScrollable(tab);
            else if (backgroundType == Background.REPEATING_BACKGROUND_ELEMENTS) createSetWidthAndHeightMenuForSingleElement(tab);
        }
        else if (localStatement == SET_LEFT_UPPER_CORNER_FOR_SINGLE_ELEMENT){
            createSingleElementAddingMenu(tab);
        }
        else if (localStatement == TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);
        }
    }

    private void createDeleteBackgroundsMenu(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        if (backgroundsForDeleting != null && backgroundsForDeleting.size() >0){
            for (Background background : backgroundsForDeleting){
                String name = background.getObjectToDisplayName();
                androidAndroidGUI_CheckBox checkBox = new androidAndroidGUI_CheckBox(new Vec2(0,0), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.25f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, name);
                checkBox.setUserData(background.getStringData());
                checkBox.setFlagSet(true);
                tab.addGUI_Element(checkBox, null);
            }
        }
        else {
            System.out.println("There are no backgrounds; " + (backgroundsForDeleting == null));
        }
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4f)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4f)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);

        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    private void createSingleElementAddingMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135f*(Program.engine.width/ Program.DEBUG_DISPLAY_WIDTH)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void createSetWidthAndHeightMenuForScrollable(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        addArrows(tab);
        androidAndroidGUI_Slider widthSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , WIDTH_STRING, MINIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND, MAXIMAL_WIDTH_FOR_SCROLLABLE_BACKGROUND);
        widthSlider.setValue(widthBasicValue);
        tab.addGUI_Element(widthSlider, null);
        androidAndroidGUI_Slider heightSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , HEIGHT_STRING, MINIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND, MAXIMAL_HEIGHT_FOR_SCROLLABLE_BACKGROUND);
        heightSlider.setValue(heightBasicValue);
        tab.addGUI_Element(heightSlider, null);
        androidAndroidGUI_Slider relativeVelocitySlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , RELATIVE_VELOCITY_STRING, (int)MINIMAL_RELATIVE_VELOCITY, (int)MAXIMAL_RELATIVE_VELOCITY);
        relativeVelocitySlider.setValue((int)relativeVelocityBasicValue);
        tab.addGUI_Element(relativeVelocitySlider, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void createSetWidthAndHeightMenuForSingleElement(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        addArrows(tab);
        androidAndroidGUI_Slider widthSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , WIDTH_STRING, MINIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND, MAXIMAL_WIDTH_FOR_SINGLE_ELEMENT_BACKGROUND);
        widthSlider.setValue(singleElementBasicWidth);
        tab.addGUI_Element(widthSlider, null);
        androidAndroidGUI_Slider heightSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , HEIGHT_STRING, MINIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND, MAXIMAL_HEIGHT_FOR_SINGLE_ELEMENT_BACKGROUND);
        heightSlider.setValue(singleElementBasicHeight);
        tab.addGUI_Element(heightSlider, null);
        androidAndroidGUI_Slider relativeVelocitySlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , RELATIVE_VELOCITY_STRING, (int)MINIMAL_RELATIVE_VELOCITY, (int)MAXIMAL_RELATIVE_VELOCITY);
        relativeVelocitySlider.setValue((int)relativeVelocityBasicValue);
        tab.addGUI_Element(relativeVelocitySlider, null);
        androidAndroidGUI_Slider stepSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , STEP_STRING, (int)MIN_STEP_VALUE, (int)MAX_STEP_VALUE);
        stepSlider.setValue(basicStep);
        tab.addGUI_Element(stepSlider, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void addArrows(androidGUI_ScrollableTab tab){
        androidGUI_Element buttonUp = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, UP, false);
        tab.addGUI_Element(buttonUp, null);
        androidGUI_Element buttonDown = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DOWN, false);
        tab.addGUI_Element(buttonDown, null);
        androidGUI_Element buttonLeft = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, LEFT, false);
        tab.addGUI_Element(buttonLeft, null);
        androidGUI_Element buttonRight = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, RIGHT, false);
        tab.addGUI_Element(buttonRight, null);
    }

    private void createSetOnScreenPositionBackground(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, ALIGN_TO_UPPER_LEFT);
        tab.addGUI_Element(element, null);
        element.setStatement(androidGUI_Element.PRESSED);
        androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, ALIGN_TO_UPPER_RIGHT);
        tab.addGUI_Element(element2, null);
        androidAndroidGUI_RadioButton element3 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, ALIGN_TO_LOWER_LEFT);
        tab.addGUI_Element(element3, null);
        androidAndroidGUI_RadioButton element4 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, ALIGN_TO_LOWER_RIGHT);
        tab.addGUI_Element(element4, null);
        androidAndroidGUI_RadioButton element5 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, ALIGN_TO_CENTER);
        tab.addGUI_Element(element5, null);
        ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
        allRadioButtons.add(element);
        allRadioButtons.add(element2);
        allRadioButtons.add(element3);
        allRadioButtons.add(element4);
        allRadioButtons.add(element5);
        element.addAnotherRadioButtonsInGroup(allRadioButtons);
        element2.addAnotherRadioButtonsInGroup(allRadioButtons);
        element3.addAnotherRadioButtonsInGroup(allRadioButtons);
        element4.addAnotherRadioButtonsInGroup(allRadioButtons);
        element5.addAnotherRadioButtonsInGroup(allRadioButtons);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    @Override
    protected void createNewOrExistingTilesetMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, USE_EXISTING_GRAPHIC, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, LOAD_NEW_GRAPHIC, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    @Override
    protected void saveBasicLifeValue(int value) {

    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if ((Editor2D.localStatement < MANAGING)) {
                    if (background != null) setTextForConsole(onScreenConsole," ");
                }
                else if ((Editor2D.localStatement == MANAGING)) {
                    setTextForConsole(onScreenConsole,"What do you want to do?");
                }
                else if (Editor2D.localStatement == BACKGROUND_DELETING){
                    setTextForConsole(onScreenConsole,"Unmark backgrounds you want to delete");
                }
                else if (Editor2D.localStatement == BACKGROUND_TYPE_SELECTING){
                    setTextForConsole(onScreenConsole,"Choose background type");
                }
                else if ((Editor2D.localStatement == BACKGROUND_SINGLE_COLOR_SELECTING)) {
                    setTextForConsole(onScreenConsole,"Set color for background");
                }
                else if ((Editor2D.localStatement == SINGLE_ELEMENT_REPEATABILITY)) {
                    setTextForConsole(onScreenConsole,"Must be this element repeated infinitely?");
                }
                else if ((Editor2D.localStatement == NEW_OR_EXISTING_TILESET)) {
                    setTextForConsole(onScreenConsole,"Which graphic do you want to use?");
                }
                else if ((Editor2D.localStatement == GET_TILESET_FROM_EXTERNAL_STORAGE)) {
                    setTextForConsole(onScreenConsole,"Choose background from external storage");
                }
                else if (Editor2D.localStatement == BACKGROUND_IN_DIRECTORY_CHOOSING) {
                    setTextForConsole(onScreenConsole,"Choose graphic file");
                }
                else if (Editor2D.localStatement == SET_POSITION_BACKGROUND) {
                    setTextForConsole(onScreenConsole,"Set position for your background");
                }
                else if (Editor2D.localStatement == SET_LEFT_UPPER_CORNER_FOR_SINGLE_ELEMENT) {
                    setTextForConsole(onScreenConsole,"Set left upper corner for background element");
                }
                else if (Editor2D.localStatement == SET_RIGHT_LOWER_FOR_SINGLE_ELEMENT) {
                    setTextForConsole(onScreenConsole,"Set right lower corner for background element");
                }
                else if (Editor2D.localStatement == BACKGROUND_ADJUSTING) {
                    setTextForConsole(onScreenConsole,"Set dimensions and adjust position");
                }
                else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING){
                   setTextForConsole(onScreenConsole,"Select graphic file you want to use");
                }
                else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    setTextForConsole(onScreenConsole,"Choose picture area for this object");
                }
                else {
                    setTextForConsole(onScreenConsole," ");
                }
            }
            catch (Exception e) {

            }
        }
        else {
            if (Editor2D.localStatement>TEXTURE_REGION_CHOOSING){
                setTextForConsole(onScreenConsole," ");
            }
        }
    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement == SET_POSITION_BACKGROUND){
            backgroundAddingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (backgroundAddingController.canBeNewObjectAdded()){
                savePositionData(tabController, levelsEditorProcess.getEditorCamera(), objectData, levelsEditorProcess.getGameRound());
                Editor2D.setNewLocalStatement((BACKGROUND_ADJUSTING));
                makePauseToNextOperation();
            }
        }
    }



    protected void savePositionData(ScrollableTabController tabController, GameCamera editorCamera, GameObjectDataForStoreInEditor objectData, GameRound gameRound){
        PVector nearestPointPos = LevelsEditorProcess.getPointInWorldPosition(editorCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY));
        objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        ArrayList <androidGUI_Element> androidGui_elements = tabController.getTab().getElements();
        String alignment = "";
        for (androidGUI_Element androidGui_element : androidGui_elements){
            if (androidGui_element.getClass() == androidAndroidGUI_RadioButton.class){
                androidAndroidGUI_RadioButton rb = (androidAndroidGUI_RadioButton) androidGui_element;
                if (rb.getStatement() == androidGUI_Element.RELEASED || rb.getStatement() == androidGUI_Element.PRESSED){
                    alignment = rb.getName();
                    System.out.println("Alignment was set: " + alignment);
                }
            }
        }
        backgroundAddingController.endAdding();
        System.out.println("Data stores in objectData");
        backgroundAddingController.switchOffTimer();
        backgroundAddingController = null;
        if (backgroundType == Background.SCROLLABLE_PICTURE_BACKGROUND) {
            Vec2 leftUpperCorner = getLeftUpperCorner(nearestPointPos, alignment);
            savePositionDataForScrollableBackground(gameRound, leftUpperCorner, objectData);
        }
        else if (backgroundType == Background.REPEATING_BACKGROUND_ELEMENTS){
            Vec2 center = getCenter(nearestPointPos, alignment);
            savePositionDataForSingleElement(gameRound, center, objectData);
        }
    }

    //(SingleGraphicElement singleGraphicElement, Vec2 pos, int xStep, int yStep, int angle, float relativeVelocity
    private void savePositionDataForSingleElement(GameRound gameRound, Vec2 center, GameObjectDataForStoreInEditor objectData){
        if (staticSpriteForRepeatinElement == null) {
            staticSpriteForRepeatinElement  = new StaticSprite(SingleGraphicElement.CLEAR_RECT, singleElementBasicWidth, singleElementBasicHeight);
            staticSpriteForRepeatinElement.loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(staticSpriteForRepeatinElement.getPath()));
        }
        else{
            StaticSprite clone = staticSpriteForRepeatinElement.clone();
            staticSpriteForRepeatinElement  = clone;
            staticSpriteForRepeatinElement.loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(staticSpriteForRepeatinElement.getPath()));
        }
        //gameRound.get
        background = new RepeatingBackgroundElement(staticSpriteForRepeatinElement, center, basicStep, 0, 0, relativeVelocityBasicValue/100f);
        gameRound.addBackground(background);
        objectData.setPosition(center);
        System.out.println("New repeating background element is created");
    }

    private void savePositionDataForScrollableBackground(GameRound gameRound, Vec2 leftUpperCorner, GameObjectDataForStoreInEditor objectData){
        background = new ScrollableAlongXBackground(objectData.getPathToTexture(), widthBasicValue, heightBasicValue, (int)leftUpperCorner.x, (int)leftUpperCorner.y, relativeVelocityBasicValue/100f, true, true, null);
        System.out.println("Area to select visible zone on image must be selected");
        gameRound.addBackground(background);
        objectData.setLeftUpperCorner(new PVector(leftUpperCorner.x, leftUpperCorner.y));
        System.out.println("New scrollable background is created");
    }

    private Vec2 getLeftUpperCorner(PVector nearestPointPos, String allignment){
         if (allignment == ALIGN_TO_UPPER_LEFT) return new Vec2(nearestPointPos.x, nearestPointPos.y);
         else if (allignment == ALIGN_TO_UPPER_RIGHT) return new Vec2(nearestPointPos.x-widthBasicValue, nearestPointPos.y);
         else if (allignment == ALIGN_TO_LOWER_LEFT) return new Vec2(nearestPointPos.x, nearestPointPos.y-heightBasicValue);
         else if (allignment == ALIGN_TO_LOWER_RIGHT) return new Vec2(nearestPointPos.x-widthBasicValue, nearestPointPos.y-heightBasicValue);
         else if (allignment == ALIGN_TO_CENTER) return new Vec2(nearestPointPos.x-widthBasicValue/2, nearestPointPos.y-heightBasicValue/2);
         else return null;
    }

    private Vec2 getCenter(PVector nearestPointPos, String allignment){
        if (allignment == ALIGN_TO_UPPER_LEFT) return new Vec2(nearestPointPos.x+singleElementBasicWidth/2, nearestPointPos.y+singleElementBasicHeight/2);
        else if (allignment == ALIGN_TO_UPPER_RIGHT) return new Vec2(nearestPointPos.x-singleElementBasicWidth/2, nearestPointPos.y+singleElementBasicHeight/2);
        else if (allignment == ALIGN_TO_LOWER_LEFT) return new Vec2(nearestPointPos.x+singleElementBasicWidth/2, nearestPointPos.y-singleElementBasicHeight/2);
        else if (allignment == ALIGN_TO_LOWER_RIGHT) return new Vec2(nearestPointPos.x-singleElementBasicWidth/2, nearestPointPos.y-singleElementBasicHeight/2);
        else if (allignment == ALIGN_TO_CENTER) return new Vec2(nearestPointPos.x, nearestPointPos.y);
        else return null;
    }

    private void tabUpdatingByReleasedElements(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {
        //System.out.println("Local: " + Editor2D.localStatement);
        for (androidGUI_Element element : releasedElements){
            if (element.getName() == CANCEL){
                Editor2D.setNewLocalStatement((byte)(MANAGING-1));
                levelsEditorProcess.pointsOnMap.clear();
                dispose(levelsEditorProcess);
                System.out.println("Reset");
            }
            else if (Editor2D.localStatement == MANAGING) {
                if (element.getName() == ADD_NEW_BACKGROUND_NAME) {
                    Editor2D.setNewLocalStatement(BACKGROUND_TYPE_SELECTING);
                }
                else if (element.getName() == DELETE_BACKGROUND_NAME){
                    Editor2D.setNewLocalStatement(BACKGROUND_DELETING);
                    backgroundsForDeleting = levelsEditorProcess.getGameRound().getBackgrounds();
                }
                makePauseToNextOperation();
            }
            else if (Editor2D.localStatement == BACKGROUND_DELETING){
                if (element.getName() == APPLY || element.getName() == CANCEL){
                    Editor2D.setNewLocalStatement(MANAGING);
                    makePauseToNextOperation();
                    if (element.getName() == APPLY) {
                        System.out.println("Start to delete backgrounds");
                        deleteBackgrounds(levelsEditorProcess.getGameRound().getBackgrounds(), tab.getElements());
                    }
                }
                if (element.getClass() == androidAndroidGUI_CheckBox.class){
                    androidAndroidGUI_CheckBox checkBox = (androidAndroidGUI_CheckBox) element;
                    for (Background background : backgroundsForDeleting) {
                        if (background.getStringData() != null && background.getStringData().equals(checkBox.getUserData())){
                            if (background.isHide() && checkBox.isFlagSet()) {
                                System.out.println("Background was shown");
                                background.setHide(false);
                            }
                            else if (!background.isHide() && !checkBox.isFlagSet()) {
                                System.out.println("Background was hidden");
                                background.setHide(true);
                            }
                        }
                    }
                }
            }
            else if (Editor2D.localStatement == BACKGROUND_TYPE_SELECTING){
                System.out.print("Type set to");
                if (element.getName() == SCROLLABLE_PICTURE_BACKGROUND_NAME){
                    objectData.setType(Background.SCROLLABLE_PICTURE_BACKGROUND);
                    backgroundType = Background.SCROLLABLE_PICTURE_BACKGROUND;
                    Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                }
                else if (element.getName() == REPEATING_BACKGROUND_ELEMENT_NAME){
                    objectData.setType(Background.REPEATING_BACKGROUND_ELEMENTS);
                    backgroundType = Background.REPEATING_BACKGROUND_ELEMENTS;
                    /*
                    if (singleElementFirstPointAddingController == null) {
                        System.out.println("singleElementFirstPointAddingController is created");
                        singleElementFirstPointAddingController = new PointAddingController();
                    }*/
                    Editor2D.setNewLocalStatement(SET_POSITION_BACKGROUND);
                    if (backgroundAddingController == null) backgroundAddingController = new NPC_AddingController();
                    //Editor2D.setNewLocalStatement(SET_LEFT_UPPER_CORNER_FOR_SINGLE_ELEMENT);
                }
                else if (element.getName() == SINGLE_COLOR_BACKGROUND_NAME){
                    objectData.setType(Background.SINGLE_COLOR_BACKGROUND);
                    backgroundType = Background.SINGLE_COLOR_BACKGROUND;
                    Editor2D.setNewLocalStatement(BACKGROUND_SINGLE_COLOR_SELECTING);
                }
                System.out.println( " " + objectData.getType());
                makePauseToNextOperation();
            }
            else if (Editor2D.localStatement == BACKGROUND_SINGLE_COLOR_SELECTING){
                if (element.getName() == APPLY){
                    saveSingleBackgroundData(objectData, tab.getElements());
                    makePauseToNextOperation();
                    Editor2D.setNewLocalStatement(END);
                    backgroundWasCompleteSetUp = true;
                }
            }
            else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET){
                if (element.getName() == USE_EXISTING_GRAPHIC) {
                    if (backgroundType == Background.SCROLLABLE_PICTURE_BACKGROUND) Editor2D.setNewLocalStatement(BACKGROUND_IN_DIRECTORY_CHOOSING);
                    else if (backgroundType == Background.REPEATING_BACKGROUND_ELEMENTS) Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
                } else if (element.getName() == LOAD_NEW_GRAPHIC) {
                    System.out.println("Try to load external graphic");
                    Editor2D.setNewLocalStatement(GET_TILESET_FROM_EXTERNAL_STORAGE);
                }
            }
            else if (Editor2D.localStatement == BACKGROUND_IN_DIRECTORY_CHOOSING){
                if (objectData.getType() == Background.SCROLLABLE_PICTURE_BACKGROUND) {
                    Editor2D.setNewLocalStatement(SET_POSITION_BACKGROUND);
                    if (backgroundAddingController == null) backgroundAddingController = new NPC_AddingController();
                }
                else System.out.println("Which statement must be selected");
                String pathToSave = StringLibrary.getStringAfterPathDevider(element.getName());
                System.out.println("Path to strore in objects data: " + pathToSave);
                objectData.setPathToTexture(pathToSave);
                //objectData.setPathToTexture(element.getName());
                makePauseToNextOperation();
            }
            else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING){
                if  (objectData.getType() == Background.REPEATING_BACKGROUND_ELEMENTS) {
                    System.out.println("For black rect path is: " + element.getName());
                    Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
                    staticSpriteForRepeatinElement.setPath(element.getName());

                    staticSpriteForRepeatinElement.loadSprite(levelsEditorProcess.getGameRound().getMainGraphicController().getTilesetUnderPath(element.getName()));
                }
                objectData.setPathToTexture(element.getName());
                makePauseToNextOperation();
            }
            else if (Editor2D.localStatement == SET_POSITION_BACKGROUND){
                //System.out.println("Nothing");
            }
            else if (Editor2D.localStatement == BACKGROUND_ADJUSTING){
                if (movementVelocity != 0) movementVelocity = 0;
                if (element.getName() == APPLY){
                    saveWidthHeightAndScrollingData(objectData, tab.getElements());
                    if (backgroundType == Background.SCROLLABLE_PICTURE_BACKGROUND) {
                        Editor2D.setNewLocalStatement(END);
                        backgroundWasCompleteSetUp = true;
                    }
                    else if (backgroundType == Background.REPEATING_BACKGROUND_ELEMENTS) {
                        Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                        backgroundWasCompleteSetUp = true;
                    }
                    makePauseToNextOperation();
                }
            }
            else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING){
                if (element.getName() == APPLY) {
                    Editor2D.setNewLocalStatement(END);
                    backgroundWasCompleteSetUp = true;
                    TextureDataToStore data = new TextureDataToStore(tab.getTilesetZone().getGraphic(), tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), true);
                    objectData.setStaticSpriteByTextureData(data);
                    objectData.calculateGraphicDimentionsForRoundBox();
                    makePauseToNextOperation();
                }
                makePauseToNextOperation();
            }
        }
    }

    private void deleteBackgrounds(ArrayList<Background> backgroundsInWorld, ArrayList<androidGUI_Element> elements) {
        System.out.println("Backgrounds number was: " + backgroundsInWorld.size());
        boolean wasUnsaved = false;
        for (int i = (backgroundsInWorld.size() - 1); i >= 0; i--) {
            //if (backgroundsInWorld.get(i).isHide()) {
                for (int k = Editor2D.getNewObjectsData().size() - 1; k >= 0; k--) {
                    if (Editor2D.getNewObjectsData().get(k).equals(backgroundsInWorld.get(i).getStringData())) {
                        Editor2D.getNewObjectsData().remove(k);
                        System.out.println("This object was deleted from unsaved data");
                        wasUnsaved = true;
                    }
                }
                if (wasUnsaved) System.out.println("There was unsaved backgrounds");
                else System.out.println("There are no unsaved backgrounds");
                try {
                    DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
                    System.out.println("Try to delete background with string: " + backgroundsInWorld.get(i).getStringData());
                    deleteStringsMaster.deleteString(backgroundsInWorld.get(i).getStringData());
                    //System.out.println("This background was deleted from file");
                }
                catch (Exception e){
                    System.out.println("Can not delete background from file; Her name : " + backgroundsInWorld.get(i).getStringData());
                    e.printStackTrace();
                }

                backgroundsInWorld.remove(backgroundsInWorld.get(i));
                System.out.println("Background was deleted from actual world");
                System.out.println("After all background with full screen are deleted must be white screen shown");
                //break;
            //}
            //else System.out.println("this background " + backgroundsInWorld.get(i).getClass() + " is hide and can not be deleted");
        }





        /*
        for (int j = (backgroundsForDeleting.size()-1); j>=0; j--) {
            System.out.println("backgroundsForDeleting.size()" + backgroundsForDeleting.size());
            if (backgroundsForDeleting.get(j).isHide()) {
                for (int i = (backgroundsInWorld.size() - 1); i >= 0; i--) {
                    if (backgroundsInWorld.get(i).equals(backgroundsForDeleting.get(j))) {
                        for (int k = Editor2D.getNewObjectsData().size()-1; k >= 0; k--){
                            if (Editor2D.getNewObjectsData().get(k).equals(backgroundsForDeleting.get(j).getStringData())){
                                Editor2D.getNewObjectsData().remove(j);
                                System.out.println("This object was deleted from unsaved data");
                            }
                        }
                        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Programm.actualRoundNumber, LoadingMaster.USER_LEVELS);
                        deleteStringsMaster.deleteString(backgroundsForDeleting.get(j).getStringData());
                        System.out.println("This background was deleted from file");
                        backgroundsInWorld.remove(backgroundsForDeleting.get(j));

                        System.out.println("Background was deleted");
                        break;
                    }
                }
            }

        }

        */

        /*
        for (GUI_Element element : elements) {
            if (element.getClass() == GUI_CheckBox.class) {
                GUI_CheckBox checkBox = (GUI_CheckBox) element;
                for (Background background : backgroundsForDeleting) {
                    if (background.getStringData() != null && background.getStringData().equals(checkBox.getUserData())) {
                        if (background.isHide() && checkBox.isFlagSet()) background.setHide(false);
                        else if (!background.isHide() && !checkBox.isFlagSet()) background.setHide(true);
                    }
                }
            }
        }
        */
    }

    private void saveWidthHeightAndScrollingData(GameObjectDataForStoreInEditor objectData, ArrayList <androidGUI_Element> elements){
        for (androidGUI_Element androidGui_element : elements){
            if (androidGui_element.getClass() == androidAndroidGUI_Slider.class) {
                androidAndroidGUI_Slider slider = (androidAndroidGUI_Slider) androidGui_element;
                if (slider.getName() == WIDTH_STRING) {
                    objectData.setWidth((int)slider.getValue());
                }
                else if (slider.getName() == HEIGHT_STRING) {
                    objectData.setHeight((int)slider.getValue());
                }
                if (slider.getName() == RELATIVE_VELOCITY_STRING) {
                    objectData.setRelativeVelocity((int)(slider.getValue()));
                    System.out.println("Relative velocity set to " + (int)(slider.getValue()));
                }
                else if (slider.getName() == STEP_STRING) {
                    System.out.println("Step set to " + (int)(slider.getValue()) + "; Min is: " + slider.getMinValue());
                    objectData.setStep((int)slider.getValue());
                }
            }
        }
        System.out.println(" Width, height, and relative velocity data was saved");
    }

    private void updateBackgroundAdjusting(GameObjectDataForStoreInEditor objectData, androidGUI_Element pressedElement){
        String name = pressedElement.getName();
        if ((name == UP || name == DOWN || name == LEFT || name == RIGHT) && Program.engine.mousePressed){
            movementVelocity+=MOVEMENT_ACCELERATE;
            if (name == UP) background.shift(0, -movementVelocity);
            else if (name == DOWN) background.shift(0, movementVelocity);
            else if (name == LEFT) background.shift(-movementVelocity, 0);
            else if (name == RIGHT) background.shift(movementVelocity, 0);
            pressedElement.setStatement(androidGUI_Element.ACTIVE);
            System.out.println("Shifting; Name: " + name + "; Velocity: " + movementVelocity);
            if (backgroundType == Background.SCROLLABLE_PICTURE_BACKGROUND) {
                objectData.setPosition(new Vec2(background.getLeftUpperX(), background.getLeftUpperY()));
                objectData.setLeftUpperCorner(new PVector(background.getLeftUpperX(), background.getLeftUpperY()));
            }
            else if (backgroundType == Background.REPEATING_BACKGROUND_ELEMENTS){
                RepeatingBackgroundElement backgroundElement = (RepeatingBackgroundElement) background;
                objectData.setPosition(backgroundElement.getPos());
                objectData.setLeftUpperCorner(new PVector(backgroundElement.getLeftUpperX(), backgroundElement.getLeftUpperY()));
            }

        }
        else {
            if (movementVelocity != 0) movementVelocity = 0;
            if (Program.engine.frameCount%2 == 0) {
                if (name == WIDTH_STRING) {
                    int value = pressedElement.getValue();
                    background.setWidth(value);
                    objectData.setWidth((int)value);
                    singleElementBasicWidth = (int)value;
                }
                else if (name == HEIGHT_STRING) {
                    int value = pressedElement.getValue();
                    background.setHeight(value);
                    objectData.setHeight((int)value);
                    singleElementBasicHeight = (int)value;
                }
                else if (name == RELATIVE_VELOCITY_STRING){
                    float value = pressedElement.getValue();
                    background.setRelativeVelocity(value/100f);
                    objectData.setRelativeVelocity((int)value);
                    pressedElement.setStatement(androidGUI_Element.ACTIVE);
                    relativeVelocityBasicValue = value;
                }
                else if (name == STEP_STRING){
                    int value = pressedElement.getValue();
                    System.out.println("Step value from slider " + value);
                    RepeatingBackgroundElement backgroundElement = (RepeatingBackgroundElement) background;
                    backgroundElement.setStep((int)value);
                    objectData.setStep((int)value);
                    pressedElement.setStatement(androidGUI_Element.ACTIVE);
                    basicStepValue = (int)value;
                    basicStep = (int)value;
                    System.out.println("Step set to " + objectData.getStep());
                }
            }
        }

    }

    private void updateOnScreenSingleBackgroundColor(GameRound gameRound, androidAndroidGUI_ColorPicker element, GameCamera gameCamera) {
        if (!backgroundCreated){
            String name = element.getName();
            int redValue = 0;
            int greenValue = 0;
            int blueValue = 0;
            if (name == RED) redValue = element.getColorValue();
            else if (name == GREEN) greenValue = element.getColorValue();
            else if (name == BLUE) blueValue = element.getColorValue();
            backgroundCreated = true;
            background = new SingleColorBackground(redValue, greenValue, blueValue);
            gameRound.addBackground(background);
        }
        else {
            int value = element.getColorValue();
            System.out.println("Single color updating for " + element.getName());
            SingleColorBackground singleColorBackground = (SingleColorBackground) background;
            if (element.getName() == RED) {
                singleColorBackground.setRed(value);
            }
            else if (element.getName() == GREEN){
                singleColorBackground.setGreen(value);
            }
            else singleColorBackground.setBlue(value);
            singleColorBackground.redraw(gameCamera);
        }
    }


    private void saveSingleBackgroundData(GameObjectDataForStoreInEditor objectData, ArrayList <androidGUI_Element> elements) {
        for (androidGUI_Element element : elements) {
            if (element.getClass() == androidAndroidGUI_ColorPicker.class) {
                androidAndroidGUI_ColorPicker picker = (androidAndroidGUI_ColorPicker)element;
                if (picker.getName() == RED) {
                    objectData.setRedValue(picker.getColorValue());
                }
                else if (picker.getName() == GREEN){
                    objectData.setGreenValue(picker.getColorValue());
                }
                else if (picker.getName() == BLUE){
                    objectData.setBlueValue(picker.getColorValue());
                }
            }
        }
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> guiReleasedElements = tabController.getReleasedElements();
        ArrayList<androidGUI_Element> guiPressedElements = tabController.getPressedElements();
        if (guiReleasedElements.size()>0) {
            tabUpdatingByReleasedElements(objectData, tabController.getTab(), levelsEditorProcess, guiReleasedElements);
        }
        else {
            //if (Editor2D.localStatement == BACKGROUND_ADJUSTING && movementVelocity != 0) movementVelocity = 0;
        }
        if (guiPressedElements.size()>0){
            //if (Programm.engine.mousePressed) System.out.println("Pressed; pressed size: " + guiPressedElements.size() + "; released: " + guiReleasedElements.size());
            tabUpdatingByPressedElements(objectData,  tabController.getTab(), levelsEditorProcess, guiPressedElements);
        }
        if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
            zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_GRAPHIC_ELEMENT, TilesetZone.SPRITE);
            tabController.getTab().getTilesetZone().setSingleGraphicElement(staticSpriteForRepeatinElement);
        }
    }

    @Override
    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        System.out.println("This function is not used");
    }

    private void tabUpdatingByPressedElements(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> pressedElements){
        for (androidGUI_Element element : pressedElements) {
            if (Editor2D.localStatement == BACKGROUND_SINGLE_COLOR_SELECTING) {
                if (element.getClass() == androidAndroidGUI_ColorPicker.class) {
                    updateOnScreenSingleBackgroundColor(levelsEditorProcess.getGameRound(), (androidAndroidGUI_ColorPicker) element, levelsEditorProcess.getEditorCamera());
                }
            }
            if (Editor2D.localStatement == BACKGROUND_ADJUSTING){
                updateBackgroundAdjusting(objectData, element);
            }
        }
    }



    @Override
    public void dispose(LevelsEditorProcess levelsEditorProcess) {
        System.out.println("This menu is closed");
        if (background != null && !backgroundWasCompleteSetUp){
            ArrayList <Background> backgrounds = levelsEditorProcess.getGameRound().getBackgrounds();
            for (int i = (backgrounds.size()-1); i >= 0 ; i--){
                if (backgrounds.get(i).equals(background)){
                    backgrounds.remove(background);
                    System.out.println("This background must be deleted");
                }
            }
        }
    }

    @Override
    public byte getEndValue(){
        return END;
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (Editor2D.localStatement == SET_POSITION_BACKGROUND) {
            backgroundAddingController.draw(gameCamera, levelsEditorProcess);
        }
        /*
        else if (Editor2D.localStatement == SET_LEFT_UPPER_CORNER_FOR_SINGLE_ELEMENT) {
            if (singleElementFirstPointAddingController != null) singleElementFirstPointAddingController.draw(gameCamera, levelsEditorProcess);
        }
        else if (Editor2D.localStatement == SET_RIGHT_LOWER_FOR_SINGLE_ELEMENT) {
            if (singleElementSecondPointAddingController != null) singleElementSecondPointAddingController.draw(gameCamera, levelsEditorProcess);
        }

         */
    }



}
