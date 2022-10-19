package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.loading.LevelDataStringDecoder;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.ObjectsByStringCreator;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.util.ArrayList;

public class MovingAction extends SubmenuAction{
    private final String TO_LEFT = "Move to the left";
    private final String TO_RIGHT = "Move to the right";
    private final String TO_UP = "Move upwards";
    private final  String TO_DOWN = "Move downwards";
    private final String MANUAL = "Select position";
    private final  boolean WIDTH = true;
    private final boolean HEIGHT = false;
    private final String STEP_STRING = "Step";

    public final byte MOVING = 1;
    private final byte SAVING = 2;
    private final byte ERROR = 3;
    private final byte SAVED = 4;
    private final byte POSITION_SELECTION = 5;
    private final static byte COMPLETED = 6;
    public final static byte END = COMPLETED;
    private final boolean COPY_BY_BUTTONS = true;
    private final boolean COPY_BY_PLACE_SELECTION = false;
    private SingleGraphicElement graphic;
    private boolean graphicMustBeDrawn = true;

    private String choosenAction = "";

    private PointAddingController pointAddingController;
    private ArrayList <SelectedElement> movedElements;
    private static int actualStepValueFromSlider;

    public MovingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        setStartStatement();
        if (actualStepValueFromSlider == 0) actualStepValueFromSlider = 1;
        //pointAddingController = new PointAddingController();
        //Editor2D.setNewLocalStatement(COPYING);
    }

    private void setStartStatement(){
        if (SelectingAction.selectedElements != null) {
            if (SelectingAction.selectedElements.size() == 1) {
                Editor2D.setNewLocalStatement(MOVING);
            }
            else Editor2D.setNewLocalStatement(ERROR);
        }
        else Editor2D.setNewLocalStatement(ERROR);
    }



    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == MOVING) {
            tab.clearElements();
            tab.setMinimalHeight();
            androidGUI_Element buttonManual = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MANUAL, false);
            tab.addGUI_Element(buttonManual, null);
            androidGUI_Element buttonUp = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_UP, false);
            tab.addGUI_Element(buttonUp, null);
            androidGUI_Element buttonDown = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_DOWN, false);
            tab.addGUI_Element(buttonDown, null);
            androidGUI_Element buttonLeft = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_LEFT, false);
            tab.addGUI_Element(buttonLeft, null);
            androidGUI_Element buttonRight = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, TO_RIGHT, false);
            tab.addGUI_Element(buttonRight, null);
            System.out.println("actualStepValueFromSlider " + actualStepValueFromSlider);
            addSliderWithCopeledTextField(tab, actualStepValueFromSlider, 1, getMaximalStep(), null, "Enter moving step" );
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            if (movedElements!= null) movedElements.clear();
        }
        else if (localStatement == POSITION_SELECTION){
            tab.clearElements();
            tab.setMinimalHeight();
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (localStatement == ERROR) {
            tab.clearElements();
            tab.setMinimalHeight();
        }

    }

    private int getMaximalStep(){
        float dimension = 2;
        for (SelectedElement selectedElement : SelectingAction.selectedElements){
            try{
                RoundElement roundElement = (RoundElement) selectedElement.getSelectedObject();
                if (roundElement.getWidth() > roundElement.getHeight()) dimension = roundElement.getHeight();
                else dimension = roundElement.getWidth();
            }
            catch (Exception e){
            }
            try{
                IndependentOnScreenStaticSprite graphic = (IndependentOnScreenStaticSprite) selectedElement.getSelectedObject();
                if (graphic.getWidth() > graphic.getHeight()) dimension = graphic.getHeight();
                else dimension = graphic.getWidth();
            }
            catch (Exception e){
                System.out.println("Can not get width of the element. It's not an independent sprite");
            }
        }
        return (int) dimension;
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= MOVING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Select move direction or chose on map?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == SAVING)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully moved");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == SAVED)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully moved");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == POSITION_SELECTION)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set new position for element");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == ERROR)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("You need to select at least one element for moving");
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

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        //System.out.println("Statement: " + Editor2D.localStatement);
    }


    private void actionUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess,  ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElement) {
        for (androidGUI_Element element : releasedElement){
            if (Editor2D.localStatement <= MOVING && element.getClass() != androidAndroidGUI_Slider.class) {
                choosenAction = element.getName();
                if (choosenAction != MANUAL) Editor2D.setNextLocalStatement();
                else Editor2D.setNewLocalStatement(POSITION_SELECTION);
                makePauseToNextOperation();
            }
        }
    }

    private int getStepToFreePlace(String copyDirection, SingleGameElement element){

        int step = 1;
        return step;
    }

    private void createNewObjectInWorld(GameRound gameRound, String newObjectString) {
        LevelDataStringDecoder decoder = new LevelDataStringDecoder(newObjectString);
        String className = decoder.getClassNameFromDataString();
        System.out.println("Try to get class name: ");
        if (movedElements == null) movedElements = new ArrayList<>();

        if (className.equals(RoundBox.CLASS_NAME) || className == RoundBox.CLASS_NAME){
            System.out.println("This is round box; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            RoundBox roundBox = (RoundBox) objectsCreator.getRoundElements().get(0);
            gameRound.addNewGameObject(roundBox);
            String stringData =  roundBox.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
            movedElements.add(new SelectedElement(roundBox));
        }
        else if (className.equals(IndependentOnScreenStaticSprite.CLASS_NAME) || className == IndependentOnScreenStaticSprite.CLASS_NAME){
            System.out.println("This is a independent static sprite; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            IndependentOnScreenStaticSprite independentOnScreenStaticSprite = objectsCreator.getIndependentOnScreenStaticSprites().get(0);
            System.out.println("Object is null: " + (independentOnScreenStaticSprite == null));
            gameRound.addNewIndependentOnScreenStaticSprite(independentOnScreenStaticSprite);
            String stringData =  independentOnScreenStaticSprite.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
            movedElements.add(new SelectedElement(independentOnScreenStaticSprite));
            System.out.println("New string for this object " + stringData);
        }
        else if (className.equals(IndependentOnScreenAnimation.CLASS_NAME) || className == IndependentOnScreenAnimation.CLASS_NAME){
            System.out.println("This is a independent animation; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            IndependentOnScreenAnimation independentOnScreenAnimation = objectsCreator.getIndependentOnScreenAnimations().get(0);
            System.out.println("Object is null: " + (independentOnScreenAnimation == null));
            gameRound.addNewIndependentOnScreenAnimation(independentOnScreenAnimation);
            String stringData =  independentOnScreenAnimation.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
            movedElements.add(new SelectedElement(independentOnScreenAnimation));
        }
        else if (className.equals(Gumba.CLASS_NAME) || className == Gumba.CLASS_NAME){
            System.out.println("This is gumba; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            Gumba gumba = (Gumba) objectsCreator.getPersons(gameRound).get(0);
            gameRound.addNewGameObject(gumba);
            String stringData =  gumba.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
            movedElements.add(new SelectedElement(gumba));

        }
        else {
            System.out.println("This is not round box or gumba on something else; Object string: " + newObjectString + " is not " + className);
        }
    }

    private String getStringForTranslatedObject(int step, String oldObjectDataString, String direction, boolean copyMethod) {
        String newObjectString = "";
        System.out.println("old string was: " + oldObjectDataString);
        LevelDataStringDecoder stringDecoder = new LevelDataStringDecoder(oldObjectDataString);
        String className = stringDecoder.getClassNameFromDataString();
        if (true){   //was: if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME) || className.equals(Gumba.CLASS_NAME)){
            int[] values = getValuesFromString(oldObjectDataString, stringDecoder);
            System.out.println("Values: ");
            for (int i = 0; i < values.length; i++){
                System.out.print(values[i] + "x");
            }
            System.out.println();
            String actualValuesString = "" +values[0] + LoadingMaster.DIVIDER_BETWEEN_VALUES + values[1];
            //int elementWidth = getElementDimension(className, oldObjectDataString, stringDecoder, WIDTH);
            //int elementHeight = getElementDimension(className, oldObjectDataString, stringDecoder, HEIGHT);
            String newValuesString ="";
            if (copyMethod == COPY_BY_BUTTONS) {
                if (direction == TO_UP) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] - step;
                    newValuesString += value;
                }
                if (direction == TO_DOWN) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] + step;
                    newValuesString += value;
                }
                if (direction == TO_LEFT) {
                    int value = values[0] - step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
                if (direction == TO_RIGHT) {
                    int value = values[0] + step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
                actualStepValueFromSlider = step;
            }
            else {
                int value = (int)pointAddingController.getLastAddedPointPosition().x;
                int elementWidth = getElementDimension(className,  oldObjectDataString, stringDecoder, WIDTH);
                int elementHeight = getElementDimension(className,  oldObjectDataString, stringDecoder, HEIGHT);
                if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME) || className.equals(Gumba.CLASS_NAME)) {
                    value+=(int)(elementWidth/2f);
                }
                else {
                    System.out.println("Added to next pos");
                    value+=(int)(2*elementWidth/4);
                }
                newValuesString += value;
                newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;

                if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME) || className.equals(Gumba.CLASS_NAME)) {
                    newValuesString += (int)(pointAddingController.getLastAddedPointPosition().y+elementHeight/2);
                }
                else{
                    newValuesString += (int)(pointAddingController.getLastAddedPointPosition().y+2*elementHeight/4);
                }
            }
            System.out.println("Step was: " + step);
            System.out.println("Try to find string: " + actualValuesString + "  in string: " + oldObjectDataString);
            newObjectString = oldObjectDataString.substring(0, oldObjectDataString.indexOf(actualValuesString));
            newObjectString+=newValuesString;
            try {
                int nextCharNumber = oldObjectDataString.indexOf(actualValuesString)+actualValuesString.length();
                newObjectString+=oldObjectDataString.substring(nextCharNumber);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return newObjectString;
    }


    /*
    private String getStringForTranslatedObject(int step, String oldObjectDataString, String direction, boolean copyMethod) {
        String newObjectString = "";
        System.out.println("old string was: " + oldObjectDataString);
        LevelDataStringDecoder stringDecoder = new LevelDataStringDecoder(oldObjectDataString);
        String className = stringDecoder.getClassNameFromDataString();
        LevelDataStringDecoder decoder = new LevelDataStringDecoder(stringDecoder.getTextDataFromDataString(oldObjectDataString, className));
        if (true){   //was: if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME) || className.equals(Gumba.CLASS_NAME)){
            System.out.println("Try to create copy for " + stringDecoder.getTextDataFromDataString(oldObjectDataString, className));
            String objectName = decoder.getClassNameFromDataString();
            System.out.println("Object name: " + objectName);
            int[] values;
            if (objectName.equals(IndependentOnScreenStaticSprite.CLASS_NAME)){
                values = decoder.getValuesFromGraphicString();
                System.out.println("Data about width and height are got from string for " + objectName);
            }
            else {
                System.out.println("Data about width and height are got from string for " + objectName);
                values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
            }
            System.out.println("Values (Only for round elements. For graphic must be another data): ");
            for (int i = 0; i < values.length; i++){
                System.out.print(values[i] + "x");
            }
            System.out.println();
            String actualValuesString = "" +values[0] + LoadingMaster.DIVIDER_BETWEEN_VALUES + values[1];
            int elementWidth = getElementDimension(className, values, WIDTH);
            int elementHeight = getElementDimension(className, values, HEIGHT);
            String newValuesString ="";
            if (copyMethod == COPY_BY_BUTTONS) {
                if (direction == TO_UP) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] - step;
                    newValuesString += value;
                }
                if (direction == TO_DOWN) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] + step;
                    newValuesString += value;
                }
                if (direction == TO_LEFT) {
                    int value = values[0] - step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
                if (direction == TO_RIGHT) {
                    int value = values[0] + step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
            }
            else {
                int value = (int)pointAddingController.getLastAddedPointPosition().x;
                if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME) || className.equals(Gumba.CLASS_NAME)) value+=(int)(elementWidth/2f);

                newValuesString += value;
                newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                newValuesString += (int)(pointAddingController.getLastAddedPointPosition().y+elementHeight/2);
            }
            String restString = oldObjectDataString.substring(oldObjectDataString.indexOf(actualValuesString)+actualValuesString.length());
            System.out.println("Rest string " + restString);
            newObjectString = oldObjectDataString.substring(0, oldObjectDataString.indexOf(actualValuesString));
            newObjectString+=newValuesString;
            try {
                int nextCharNumber = oldObjectDataString.indexOf(actualValuesString)+actualValuesString.length();
                newObjectString+=oldObjectDataString.substring(nextCharNumber);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return newObjectString;
    }
*/


    private void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.localStatement != ERROR && SelectingAction.selectedElements.size() != 1){
            setStartStatement();
        }
        if (Editor2D.localStatement != ERROR && Editor2D.localStatement != POSITION_SELECTION) {
            if (Editor2D.canBeNextOperationMade()) {
                ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
                ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
                if (pressedElements.size() > 0 || releasedElements.size() > 0) {
                    actionUpdating(objectData, levelsEditorProcess, tabController, releasedElements);
                }
                if (Editor2D.localStatement == SAVING) {
                    repositionOfObject(levelsEditorProcess, COPY_BY_BUTTONS);
                    Editor2D.setNewLocalStatement(MOVING);
                }
                else if (Editor2D.localStatement == SAVED) {
                    Editor2D.setNewLocalStatement(MOVING);
                }
                updateActualSliderValue(pressedElements, releasedElements);
            }
        }
        if (Editor2D.localStatement == POSITION_SELECTION){
            if (pointAddingController == null) pointAddingController = new PointAddingController(PointAddingController.TO_CELL_CENTER);
            updateNewPointAdding(levelsEditorProcess, levelsEditorProcess.getEditorCamera());
        }
    }

    private void updateActualSliderValue(ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        for (androidGUI_Element element : pressedElements){
            if (element.getClass() == androidAndroidGUI_Slider.class){
                androidAndroidGUI_Slider slider  = (androidAndroidGUI_Slider) element;
                actualStepValueFromSlider = slider.getValue();
                //System.out.println("actual value set on: " + actualStepValueFromSlider);

            }
            else if (element.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField textField = (androidAndroidGUI_TextField) element;
                //actualStepValueFromSlider = slider.getUserValue();
                actualStepValueFromSlider = textField.getValue();
                //System.out.println("actual value set on: " + actualStepValueFromSlider);
            }
        }
            }

    private void selectObjectAgain(LevelsEditorProcess levelsEditorProcess) {
        for (SelectedElement movedElement : movedElements){
            SingleGameElement selectMovedObjectAgaing = movedElement.getSelectedObject();
            if (selectMovedObjectAgaing instanceof GameObject){
                GameObject gameObject = (GameObject) movedElement.getSelectedObject();
                Body body = gameObject.body;
                if (body == null) System.out.println("This object has no body and can not be selected");
                else {
                    System.out.println("New object was selected");
                    addNewObjectWithBodyToSelection(SelectingAction.selectedElements, levelsEditorProcess.getGameRound(), body);
                }
            }
            else {
                System.out.println("Try to select again this object. It is not a round element");
                SingleGameElement singleGraphicElement = movedElement.getSelectedObject();
                addNewObjectWithoutBodyToSelection(SelectingAction.selectedElements, levelsEditorProcess.getGameRound(), singleGraphicElement);
            }
            //selectedElement.getSelectedObject()
        }
        System.out.println("Selected elements number: " + SelectingAction.selectedElements.size());
    }

    private void deletePreviousObject(LevelsEditorProcess levelsEditorProcess) {
        ArrayList <SelectedElement> selectedElements = SelectingAction.selectedElements;
        for (int i = (selectedElements.size()-1); i >= 0; i--){
            deleteObjects(levelsEditorProcess, " was moved");
        }
    }

    private void updateNewPointAdding(LevelsEditorProcess levelsEditorProcess, GameCamera gameCamera) {
        pointAddingController.update(gameCamera, levelsEditorProcess);
        if (pointAddingController.isCrossDrawing()){
            graphicMustBeDrawn = true;
        }
        else {
            graphicMustBeDrawn = false;
        }
        if (pointAddingController.canBeNewObjectAdded()){
            pointAddingController.addNewObject(gameCamera, levelsEditorProcess, null);
            System.out.println("Object added");
        }
        if (levelsEditorProcess.pointsOnMap.size() == 1){
            System.out.println("Point added");
            repositionOfObject(levelsEditorProcess, COPY_BY_PLACE_SELECTION);
            levelsEditorProcess.pointsOnMap.clear();
            Editor2D.setNewLocalStatement(POSITION_SELECTION);
        }

    }

    private void repositionOfObject(LevelsEditorProcess levelsEditorProcess, boolean movingMethod){
        saveNewObject(levelsEditorProcess, movingMethod);
        deletePreviousObject(levelsEditorProcess);
        SelectingAction.selectedElements.clear();

        selectObjectAgain(levelsEditorProcess);
    }

    private void drawElementOnCopyingPlace(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        Vec2 drawPosition = pointAddingController.getActualCrossPosition(gameCamera, levelsEditorProcess);
        System.out.println("Rest time: " + pointAddingController.getTimerRelativeTime(255));
        if (graphic == null) {
            if (graphic.getClass() == StaticSprite.class){
                //graphic = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size()-1).getGraphic();
                graphic = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size()-1).getGraphic();
            }
            else graphic = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size()-1).getGraphic();
        }
        //System.out.println("Drawing on " + drawPosition);
        if (graphic != null) graphic.draw(gameCamera, drawPosition, 0);
        else {
            System.out.println("I have no graphic data for this selected object");
        }
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (graphicMustBeDrawn && pointAddingController != null) drawElementOnCopyingPlace(gameCamera, levelsEditorProcess);
        if (Editor2D.localStatement == POSITION_SELECTION) {
            if (pointAddingController != null) {
                pointAddingController.draw(gameCamera, levelsEditorProcess);
            }
        }
    }

    private void saveNewObject(LevelsEditorProcess levelsEditorProcess, boolean copyMethod) {
        //int stepToNextElement = (int) actualStepValueFromSlider;
        String oldObjectDataString = "";
        try {
            oldObjectDataString = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size() - 1).getSelectedObject().getStringData();
        }
        catch (Exception e) {
            System.out.println("Can not get data of first object in the array;" + e);
        }
        String newObjectString = "";
        try {
            if (copyMethod == COPY_BY_BUTTONS) newObjectString = getStringForTranslatedObject(actualStepValueFromSlider, oldObjectDataString, choosenAction, COPY_BY_BUTTONS);
            else {
                newObjectString = getStringForTranslatedObject(actualStepValueFromSlider, oldObjectDataString, choosenAction, COPY_BY_PLACE_SELECTION);
            }
        } catch (Exception e) {
            System.out.println("Can not create new String; " + e);
        }
        try {
            if (newObjectString != null) System.out.println("New object string: " + newObjectString);
            createNewObjectInWorld(levelsEditorProcess.getGameRound(), newObjectString);
        } catch (Exception e) {
            System.out.println("Can not create new object;" + e);
        }
        if (Editor2D.localStatement == SAVING) Editor2D.setNewLocalStatement(SAVED);
        else if (Editor2D.localStatement == POSITION_SELECTION){

        }
    }



    @Override
    public byte getEndValue(){
        return END;
    }

}
