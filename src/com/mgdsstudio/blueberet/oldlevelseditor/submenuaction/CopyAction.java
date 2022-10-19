package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.loading.LevelDataStringDecoder;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.ObjectsByStringCreator;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public class CopyAction extends SubmenuAction{
    private final String TO_LEFT = "Copy to the left";
    private final String TO_RIGHT = "Copy to the right";
    private final String TO_UP = "Copy upwards";
    private final  String TO_DOWN = "Copy downwards";
    private final String MANUAL = "Select position";



    public final byte COPYING = 1;
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

    public CopyAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        setStartStatement();
        //pointAddingController = new PointAddingController();
        //Editor2D.setNewLocalStatement(COPYING);
    }

    private void setStartStatement(){
        if (SelectingAction.selectedElements != null) {
            if (SelectingAction.selectedElements.size() == 1) {
                Editor2D.setNewLocalStatement(COPYING);
            }
            else Editor2D.setNewLocalStatement(ERROR);
        }
        else Editor2D.setNewLocalStatement(ERROR);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == COPYING) {
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

            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
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

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= COPYING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Select copy direction or chose on map?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == SAVING)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully copied");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == SAVED)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully copied");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == POSITION_SELECTION)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set position for new element");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == ERROR)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("You need to select at least one element for copying");
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
        //System.out.println("Local statement: " + Editor2D.localStatement);
    }


    private void actionUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess,  ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElement) {
        for (androidGUI_Element element : releasedElement){
            if (Editor2D.localStatement <= COPYING) {
                choosenAction = element.getName();
                if (choosenAction != MANUAL) Editor2D.setNextLocalStatement();
                else {
                    if (choosenAction == CANCEL) {
                        Editor2D.setNewLocalStatement((byte) (COPYING-1));
                    }
                    else{
                        Editor2D.setNextLocalStatement();
                        Editor2D.setNewLocalStatement(POSITION_SELECTION);
                    }
                }
                makePauseToNextOperation();
            }
        }
    }

    private int getStepToFreePlace(String copyDirection, SingleGameElement element, GameRound gameRound){
        int width = 0;
        int height = 0;
        int step = 1;
        if (element instanceof IndependentOnScreenGraphic){
            IndependentOnScreenGraphic gameObject = (IndependentOnScreenGraphic) element;
            width = (int)gameObject.getWidth();
            height = (int)gameObject.getHeight();
            if (mustBeElementMadeSmaller((int)gameObject.getWidth(), (int)gameObject.getHeight(), (int)gameObject.getAngle())){
                width+=1;
                height+=1;
            }
            boolean isObjectInZone = false;
            PVector freePlace = new PVector();
            if (copyDirection == TO_UP) {
                freePlace.x = gameObject.getPosition().x;
                while (!isObjectInZone) {
                    freePlace.y = gameObject.getPosition().y-height*step;

                    if (gameRound.getGraphicAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_DOWN) {
                freePlace.x = gameObject.getPosition().x;
                while (!isObjectInZone) {
                    freePlace.y = gameObject.getPosition().y+height*step;
                    if (gameRound.getGraphicAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_LEFT) {
                //freePlace.x = gameObject.getAbsolutePosition().x;
                freePlace.y = gameObject.getPosition().y;
                while (!isObjectInZone) {
                    freePlace.x = gameObject.getPosition().x-width*step;
                    if (gameRound.getGraphicAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_RIGHT) {
                //freePlace.x = gameObject.getAbsolutePosition().x;
                freePlace.y = gameObject.getPosition().y;
                while (!isObjectInZone) {
                    freePlace.x = gameObject.getPosition().x+width*step;
                    if (gameRound.getGraphicAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            System.out.println("Graphic element was copied");
        }
        else if (element instanceof GameObject){
            GameObject gameObject = (GameObject)element;
            width = (int)gameObject.getWidth();
            height = (int)gameObject.getHeight();
            if (mustBeElementMadeSmaller((int)gameObject.getWidth(), (int)gameObject.getHeight(), (int)gameObject.body.getAngle())){
                width+=1;
                height+=1;
            }
            boolean isObjectInZone = false;

            PVector freePlace = new PVector();
            if (copyDirection == TO_UP) {
                freePlace.x = gameObject.getPixelPosition().x;
                while (!isObjectInZone) {
                    freePlace.y = gameObject.getPixelPosition().y-height*step;
                    if (PhysicGameWorld.getBodyAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_DOWN) {
                freePlace.x = gameObject.getPixelPosition().x;
                while (!isObjectInZone) {
                    freePlace.y = gameObject.getPixelPosition().y+height*step;
                    if (PhysicGameWorld.getBodyAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_LEFT) {
                //freePlace.x = gameObject.getAbsolutePosition().x;
                freePlace.y = gameObject.getPixelPosition().y;
                while (!isObjectInZone) {
                    freePlace.x = gameObject.getPixelPosition().x-width*step;
                    if (PhysicGameWorld.getBodyAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
            else if (copyDirection == TO_RIGHT) {
                //freePlace.x = gameObject.getAbsolutePosition().x;
                freePlace.y = gameObject.getPixelPosition().y;
                while (!isObjectInZone) {
                    freePlace.x = gameObject.getPixelPosition().x+width*step;
                    if (PhysicGameWorld.getBodyAtPoint(freePlace) != null) step++;
                    else isObjectInZone = true;
                }
            }
        }
        System.out.println("Actual step: " + step);
        return step;
    }

    private void createNewObjectInWorld(GameRound gameRound, String newObjectString) {
        LevelDataStringDecoder decoder = new LevelDataStringDecoder(newObjectString);
        String className = decoder.getClassNameFromDataString();
        System.out.println("Try to get class name ");
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
        }
        else if (className.equals(IndependentOnScreenStaticSprite.CLASS_NAME)){
            System.out.println("This is an independent static sprite; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            IndependentOnScreenStaticSprite independentOnScreenStaticSprite = objectsCreator.getIndependentOnScreenStaticSprites().get(0);
            gameRound.addNewGraphic(independentOnScreenStaticSprite);
            String stringData =  independentOnScreenStaticSprite.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
        }
        else if (className.equals(IndependentOnScreenAnimation.CLASS_NAME)){
            System.out.println("This is an sprite animation; Object string: " + newObjectString);
            String [] data = new String[1];
            data[0] = newObjectString;
            ObjectsByStringCreator objectsCreator = new ObjectsByStringCreator(data);
            IndependentOnScreenAnimation independentOnScreenAnimation = objectsCreator.getIndependentOnScreenAnimations().get(0);
            gameRound.addNewGraphic(independentOnScreenAnimation);
            String stringData =  independentOnScreenAnimation.getStringData();
            GameObjectDataForStoreInEditor newObjectData = new GameObjectDataForStoreInEditor(stringData);
            Editor2D.addDataForNewObject(newObjectData);
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
        }
        else {
            System.out.println("This is not round box; Object string: " + newObjectString + " is not " + className);
        }
    }



    /*
    private int getElementDimension(String className, String oldObjectDataString, LevelDataStringDecoder decoder, boolean widthOrHeight){
        int dimension = 80;
        int[] values;
        if (className == IndependentOnScreenStaticSprite.CLASS_NAME || className.equals(IndependentOnScreenStaticSprite.CLASS_NAME) || className == IndependentOnScreenAnimation.CLASS_NAME || className.equals(IndependentOnScreenAnimation.CLASS_NAME) ){
            values = decoder.getGraphicData(LoadingMaster.GRAPHIC_NAME_END_CHAR, LoadingMaster.DIVIDER_BETWEEN_GRAPHIC_DATA);
            if (widthOrHeight == WIDTH) dimension = values[4];
            else dimension = values[5];
        }
        else {
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
            if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME)) {
                if (widthOrHeight == WIDTH) dimension = values[3];
                else dimension = values[4];
            }
            else if (className == Gumba.CLASS_NAME || className.equals(Gumba.CLASS_NAME)) {
                if (values.length > 4) dimension = values[4];
                else dimension = Gumba.NORMAL_DIAMETER;
            }
        }
        return dimension;
    }*/

    /*
    private int[] getValuesFromString(String oldObjectDataString, LevelDataStringDecoder stringDecoder){
        String className = stringDecoder.getClassNameFromDataString();
        LevelDataStringDecoder decoder = new LevelDataStringDecoder(stringDecoder.getTextDataFromDataString(oldObjectDataString, className));
        int[] values;
        System.out.println("Class name for the object to be copied: " + className);
        if (className == IndependentOnScreenStaticSprite.CLASS_NAME || className.equals(IndependentOnScreenStaticSprite.CLASS_NAME) || className == IndependentOnScreenAnimation.CLASS_NAME || className.equals(IndependentOnScreenAnimation.CLASS_NAME) ){
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
        }
        else {
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
        }
        return values;
    }*/

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
            //String className = stringDecoder.getClassNameFromDataString();
            int elementWidth = getElementDimension(className, oldObjectDataString, stringDecoder, WIDTH);
            int elementHeight = getElementDimension(className, oldObjectDataString, stringDecoder, HEIGHT);
            String newValuesString ="";
            if (copyMethod == COPY_BY_BUTTONS) {
                if (direction == TO_UP) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] - elementHeight * step;
                    newValuesString += value;
                }
                if (direction == TO_DOWN) {
                    newValuesString += values[0];
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    int value = values[1] + elementHeight * step;
                    newValuesString += value;
                }
                if (direction == TO_LEFT) {
                    int value = values[0] - elementWidth * step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
                if (direction == TO_RIGHT) {
                    int value = values[0] + elementWidth * step;
                    newValuesString += value;
                    newValuesString += LoadingMaster.DIVIDER_BETWEEN_VALUES;
                    newValuesString += values[1];
                }
            }
            else {
                int value = (int)pointAddingController.getLastAddedPointPosition().x;
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
            //String
            //String restString = oldObjectDataString.substring(oldObjectDataString.indexOf(actualValuesString)+actualValuesString.length());
            //System.out.println("Rest string " + restString + " was got from string: " + oldObjectDataString);
            //System.out.println("; actual is " + actualValuesString + " was got from string: " + oldObjectDataString);
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
                    saveNewObject(levelsEditorProcess, COPY_BY_BUTTONS);
                } else if (Editor2D.localStatement == SAVED) {
                    Editor2D.setNewLocalStatement(COPYING);
                }
            }
        }
        if (Editor2D.localStatement == POSITION_SELECTION){
            if (pointAddingController == null) pointAddingController = new PointAddingController(PointAddingController.TO_CELL_CENTER);
            updateNewPointAdding(levelsEditorProcess, levelsEditorProcess.getEditorCamera());
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
                //pointAddingController.
                pointAddingController.addNewObject(gameCamera, levelsEditorProcess, null);
                System.out.println("Object added");
            }
            if (levelsEditorProcess.pointsOnMap.size() == 1){
                System.out.println("Point added");
                saveNewObject(levelsEditorProcess, COPY_BY_PLACE_SELECTION);
                levelsEditorProcess.pointsOnMap.clear();
            }

    }

    private void drawElementOnCopyingPlace(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        Vec2 drawPosition = pointAddingController.getActualCrossPosition(gameCamera, levelsEditorProcess);
        //System.out.println("Rest time: " + pointAddingController.getTimerRelativeTime(255));
        if (graphic == null) {
            graphic = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size()-1).getGraphic();
        }
        System.out.println("Drawing on " + drawPosition);
        if (graphic != null) graphic.draw(gameCamera, drawPosition, 0);
        else {
            System.out.println("I have no graphic data of this selected object");
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
        GameRound gameRound = levelsEditorProcess.getGameRound();
        int stepToNextElement = getStepToFreePlace(choosenAction, SelectingAction.selectedElements.get(SelectingAction.selectedElements.size() - 1).getSelectedObject(), gameRound);
        String oldObjectDataString = "";
        try {
            oldObjectDataString+= SelectingAction.selectedElements.get(SelectingAction.selectedElements.size() - 1).getSelectedObject().getStringData();
        } catch (Exception e) {
            System.out.println("Can not get data of first object in the array;" + e);
        }
        String newObjectString = "";
        try {
            if (copyMethod == COPY_BY_BUTTONS) newObjectString = getStringForTranslatedObject(stepToNextElement, oldObjectDataString, choosenAction, COPY_BY_BUTTONS);
            else {
                newObjectString = getStringForTranslatedObject(stepToNextElement, oldObjectDataString, choosenAction, COPY_BY_PLACE_SELECTION);
            }

        } catch (Exception e) {
            System.out.println("Can not create new String; " + e);
        }
        try {
            if (newObjectString != null)
                System.out.println("New object string: " + newObjectString);
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
