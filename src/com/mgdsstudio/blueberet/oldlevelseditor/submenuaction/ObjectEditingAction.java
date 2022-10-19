package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.loading.DeleteStringsMaster;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class ObjectEditingAction extends SubmenuAction{



    public final static byte START = 1;
    public final static byte  OBJECT_TYPE_DECODING = 2;

    //private final static byte INDEPENDENT_ON_SCREEN_STATIC_S = 11;
    private final static byte GRAPHIC_DIMENSION_CHANGING = 11;
    private final static byte GRAPHIC_LAYER_CHANGING = 12;
    private final static byte GRAPHIC_IN_LAYER_TRANSFORMATION = 13;
    private final String objectTypeName = "";

    public final static byte END = 100;

    protected final static byte ERROR_MANY_SELECTED = 101;
    protected final static byte ERROR_NOTHING_SELECTED = 102;
    private static int valueChangingStepForGraphicDimensions = 1;

    private final static String WIDTH_LARGER = "Width +";
    private final static String WIDTH_SMALLER = "Width -";
    private final static String HEIGHT_LARGER = "Height +";
    private final static String HEIGHT_SMALLER = "Height -";
    private final static String TO_THE_BACK = "To the back";
    private final static String TO_THE_FRONT = "To the front";

    private SingleGameElement selectedElement;
    private String sourceObjectDataString;

    public ObjectEditingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        setStartStatement();
        uploadSelectedElement();
    }

    private void uploadSelectedElement() {
        if (SelectingAction.selectedElements.size() == 1){
            selectedElement = SelectingAction.selectedElements.get(0).getSelectedObject();
            sourceObjectDataString = selectedElement.getStringData();
            System.out.println("Selected object has data string: " + sourceObjectDataString);
        }
        else System.out.println("There are no selected elements or there are too much of them");
    }

    private void setStartStatement(){
        if (SelectingAction.selectedElements != null) {
            if (SelectingAction.selectedElements.size() == 1) {
                Editor2D.setNewLocalStatement( OBJECT_TYPE_DECODING);
            }
            else Editor2D.setNewLocalStatement(ERROR_NOTHING_SELECTED);
        }
        else Editor2D.setNewLocalStatement(ERROR_NOTHING_SELECTED);
    }

    private void createGraphicDimensionsMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonManual = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WIDTH_LARGER, false);
        tab.addGUI_Element(buttonManual, null);
        androidGUI_Element buttonUp = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WIDTH_SMALLER, false);
        tab.addGUI_Element(buttonUp, null);
        androidGUI_Element buttonDown = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HEIGHT_LARGER, false);
        tab.addGUI_Element(buttonDown, null);
        androidGUI_Element buttonLeft = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HEIGHT_SMALLER, false);
        tab.addGUI_Element(buttonLeft, null);
        addSliderWithCopeledTextField(tab, valueChangingStepForGraphicDimensions, 1, getMaximalStepForGraphicDimensions(), null, "Enter value" );
        androidGUI_Element buttonNext = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, NEXT, false);
        tab.addGUI_Element(buttonNext, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == START) {
            tab.clearElements();
            tab.setMinimalHeight();
        }
        else if (localStatement == OBJECT_TYPE_DECODING) {
            tab.clearElements();
            tab.setMinimalHeight();
            for (int i = 0; i < SelectingAction.selectedElements.size(); i++){
                if (SelectingAction.selectedElements.get(i).getSelectedObject() instanceof IndependentOnScreenStaticSprite){
                    Editor2D.setNewLocalStatement(GRAPHIC_DIMENSION_CHANGING);
                }
            }
        }
        else if (localStatement == GRAPHIC_DIMENSION_CHANGING){
            createGraphicDimensionsMenu(tab);
        }
        else if (localStatement == GRAPHIC_LAYER_CHANGING){
            createLayerChoosingMenuWithBackAndNextButton(tab);
        }
        else if (localStatement == GRAPHIC_IN_LAYER_TRANSFORMATION){
            createInLayerTransformationMenu(tab);
        }
        else if (localStatement == ERROR_NOTHING_SELECTED) {
            tab.clearElements();
            tab.setMinimalHeight();
        }
    }



    private final void createStartTabForIndependentSprite(androidGUI_ScrollableTab tab){

    }


    protected void setConsoleText(OnScreenConsole console, String text){
        //System.out.println("This func must be in parent class");
        actualConsoleText.clear();
        actualConsoleText.add(text);
        console.setText(actualConsoleText);
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            if (Editor2D.localStatement == START) setConsoleText(onScreenConsole, "Select at least one object");
            else if (Editor2D.localStatement == OBJECT_TYPE_DECODING) setConsoleText(onScreenConsole, "Change dimensions for graphic");
            else if (Editor2D.localStatement == ERROR_MANY_SELECTED) setConsoleText(onScreenConsole, "You need to select only one object to edit");
            else if (Editor2D.localStatement == ERROR_NOTHING_SELECTED) setConsoleText(onScreenConsole, "You need select one element to edit");
            else if (Editor2D.localStatement == GRAPHIC_DIMENSION_CHANGING) setConsoleText(onScreenConsole, "Adjust graphic dimensions");
            else if (Editor2D.localStatement == GRAPHIC_LAYER_CHANGING) setConsoleText(onScreenConsole, "Change layer");
            else if (Editor2D.localStatement == GRAPHIC_IN_LAYER_TRANSFORMATION) setConsoleText(onScreenConsole, "Transfer this element within actual layer");
            else setConsoleText(onScreenConsole, " ");
        }
    }

    @Override
    public byte getEndValue() {
        return END;
    }

    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData) {
        if (Editor2D.localStatement == START) {
            if (SelectingAction.selectedElements.size() == 0) Editor2D.setNewLocalStatement(ERROR_NOTHING_SELECTED);
            else if (SelectingAction.selectedElements.size() > 1) Editor2D.setNewLocalStatement(ERROR_MANY_SELECTED);
            else {
                Editor2D.setNewLocalStatement(OBJECT_TYPE_DECODING);
                selectedElement = SelectingAction.selectedElements.get(0).getSelectedObject();
            }
        } else if (Editor2D.localStatement != OBJECT_TYPE_DECODING) {
            updateTabController(objectData, levelsEditorProcess, tabController, levelsEditorProcess.getGameRound());
        }
    }


    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameRound gameRound){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            if (pressedElements.size()>0 || releasedElements.size()>0) {
                tabUpdating(pressedElements, releasedElements, objectData, gameRound);
            }
        }
    }

    private void updateTabForGraphicDims(ArrayList <androidGUI_Element> pressedElements, ArrayList <androidGUI_Element> releasedElements, GameRound gameRound){
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getClass() == androidAndroidGUI_Button.class) {
                    IndependentOnScreenStaticSprite sprite = (IndependentOnScreenStaticSprite) selectedElement;
                    System.out.println("Data string was: " + sprite.getStringData() + " step " + valueChangingStepForGraphicDimensions) ;
                    if (androidGui_element.getName() == WIDTH_LARGER) {
                        sprite.setWidth((sprite.staticSprite.getParentElementWidth() + valueChangingStepForGraphicDimensions));
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "New width: " + sprite.staticSprite.getParentElementWidth());
                        saveData(sprite.getStringData());
                    }
                    else if (androidGui_element.getName() == WIDTH_SMALLER) {
                        sprite.setWidth((sprite.staticSprite.getParentElementWidth() - valueChangingStepForGraphicDimensions));
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "New width: " + sprite.staticSprite.getParentElementWidth());
                        saveData(sprite.getStringData());
                    }
                    else if (androidGui_element.getName() == HEIGHT_LARGER) {
                        sprite.setHeight((sprite.staticSprite.getParentElementHeight() + valueChangingStepForGraphicDimensions));
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "New height: " + sprite.staticSprite.getParentElementHeight());
                        saveData(sprite.getStringData());
                    }
                    else if (androidGui_element.getName() == HEIGHT_SMALLER) {
                        sprite.setHeight((int) (sprite.staticSprite.getParentElementHeight() - valueChangingStepForGraphicDimensions));
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "New height: " + sprite.staticSprite.getParentElementHeight());
                        saveData(sprite.getStringData());
                    }
                    else if (androidGui_element.getName() == APPLY) {
                        saveData(sprite.getStringData());
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "New dimensions were saved");
                    }
                    else if (androidGui_element.getName() == NEXT) {
                        Editor2D.setNewLocalStatement(GRAPHIC_LAYER_CHANGING);
                    }
                    System.out.println("Data string is: " + sprite.getStringData());
                }
                else if (androidGui_element.getClass() == androidAndroidGUI_Slider.class || androidGui_element.getClass() == androidAndroidGUI_TextField.class) {
                    int value = 0;
                    if (androidGui_element.getValue() > 1) {
                        value = androidGui_element.getValue();
                    }
                    if (value > 0 ) valueChangingStepForGraphicDimensions = value;
                    System.out.println("New step value was saved " + valueChangingStepForGraphicDimensions) ;
                }
            }
        }
    }

    private void saveData(String newString) {
        if (newString.length()>0) {
            DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, ExternalRoundDataFileController.USER_LEVELS);
            deleteStringsMaster.replaceString(sourceObjectDataString, newString);
            sourceObjectDataString = newString;
            System.out.println("Actual string is: " + sourceObjectDataString);
        }
        else {
            System.out.println("New string is too short. Something goes wrong");
        }
    }

    private void resendDataToTheFront(GameRound gameRound) {
        gameRound.sentObjectToArrayStart(selectedElement);
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, ExternalRoundDataFileController.USER_LEVELS);
        deleteStringsMaster.sentStringToTheFront(sourceObjectDataString);
        System.out.println("Actual string was sent to the front: " + sourceObjectDataString);
    }

    private void resendDataToTheBack(GameRound gameRound) {
        gameRound.sentObjectToArrayEnd(selectedElement);
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, ExternalRoundDataFileController.USER_LEVELS);
        deleteStringsMaster.sentStringToTheBack(sourceObjectDataString);
        System.out.println("Actual string was sent to the back: " + sourceObjectDataString);
    }


    protected void tabUpdating(ArrayList <androidGUI_Element> pressedElements, ArrayList <androidGUI_Element> releasedElements, GameObjectDataForStoreInEditor objectData, GameRound gameRound) {
        if (Editor2D.localStatement == GRAPHIC_DIMENSION_CHANGING) {
            updateTabForGraphicDims(pressedElements, releasedElements, gameRound);
        }
        else if (Editor2D.localStatement == GRAPHIC_LAYER_CHANGING){
            updateTabForLayerChanging(pressedElements, releasedElements, gameRound);
        }
        else if (Editor2D.localStatement == GRAPHIC_IN_LAYER_TRANSFORMATION){
            updateTabForInLayerTransformation(pressedElements, releasedElements, gameRound);
        }
    }

    private void updateTabForInLayerTransformation(ArrayList <androidGUI_Element> pressedElements, ArrayList <androidGUI_Element> releasedElements, GameRound gameRound){
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getClass() == androidAndroidGUI_Button.class) {
                    IndependentOnScreenStaticSprite sprite = (IndependentOnScreenStaticSprite) selectedElement;
                    if (androidGui_element.getName() == TO_THE_BACK) {
                        resendDataToTheBack(gameRound);
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "Graphic sent to the back of the layer");
                    }
                    else if (androidGui_element.getName() == TO_THE_FRONT) {
                        resendDataToTheFront(gameRound);
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "Graphic sent to the front of the layer");
                    }
                    else if (androidGui_element.getName() == BACK) {
                        Editor2D.setNewLocalStatement(GRAPHIC_LAYER_CHANGING);
                    }
                }
            }
        }
    }



    protected void addNewDissolvingTextToTheLastSelectedObject(GameRound gameRound, String text) {
        Vec2 pos = getPixelPosForLastSelectedElement();
        float x = pos.x;
        float y = pos.y;
        int valueType = 0;
        DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(x, y, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, text, DissolvingAndUpwardsMovingText.NORMAL_DISSOLVING_TIME, DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, valueType, DissolvingAndUpwardsMovingText.GRAY);
        gameRound.addNewDissolvingText(dissolvingText);
    }

    private void updateTabForLayerChanging(ArrayList <androidGUI_Element> pressedElements, ArrayList <androidGUI_Element> releasedElements, GameRound gameRound){
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getClass() == androidAndroidGUI_Button.class) {
                    IndependentOnScreenStaticSprite sprite = (IndependentOnScreenStaticSprite) selectedElement;
                    boolean dataMustBeSaved = false;
                    if (androidGui_element.getName() == BEHIND_OF_ALL) {
                        sprite.setLayer(IndependentOnScreenGraphic.BEHIND_ALL);
                        dataMustBeSaved = true;
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "Graphic was sent behind the game world");
                    }
                    else if (androidGui_element.getName() == IN_FRONT_OF_ALL) {
                        sprite.setLayer(IndependentOnScreenGraphic.IN_FRONT_OF_ALL);
                        dataMustBeSaved = true;
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "Graphic was sent in front of the game world");
                    }
                    else if (androidGui_element.getName() == BEHIND_OF_PERSONS) {
                        sprite.setLayer(IndependentOnScreenGraphic.BEHIND_PERSONS);
                        dataMustBeSaved = true;
                        addNewDissolvingTextToTheLastSelectedObject(gameRound, "Graphic was sent behind the persons");
                    }
                    else if (androidGui_element.getName() == BACK) {
                        System.out.println("Data is not saved");
                        Editor2D.setNewLocalStatement(GRAPHIC_DIMENSION_CHANGING);
                        //addNewDissolvingTextToGameWorld(gameRound, "Nothing was saved");
                    }
                    else if (androidGui_element.getName() == NEXT) {
                        System.out.println("Data is not saved");
                        Editor2D.setNewLocalStatement(GRAPHIC_IN_LAYER_TRANSFORMATION);

                    }
                    if (dataMustBeSaved == true){
                        System.out.println("Data must be saved with source data: " + sprite.getStringData() + " after the layer changing");
                        saveData(sprite.getStringData());
                    }
                }
            }
        }
    }

    private int getMaximalStepForGraphicDimensions(){
        float dimension = 2;
        //for (SelectedElement selectedElement : SelectingAction.selectedElements){
            try{
                RoundElement roundElement = (RoundElement) selectedElement;
                if (roundElement.getWidth() > roundElement.getHeight()) dimension = roundElement.getHeight();
                else dimension = roundElement.getWidth();
            }
            catch (Exception e){
            }
            try{
                IndependentOnScreenStaticSprite graphic = (IndependentOnScreenStaticSprite) selectedElement;
                if (graphic.getWidth() > graphic.getHeight()) dimension = graphic.getHeight();
                else dimension = graphic.getWidth();
            }
            catch (Exception e){
                System.out.println("Can not get width of the element. It's not an independent sprite");
            }
        //}
        return (int) dimension;
    }

    private void createInLayerTransformationMenu(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_THE_BACK, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_THE_FRONT, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

}
