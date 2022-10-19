package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.blueberet.oldlevelseditor.ScrollableTabController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.Simple_NPC_AddingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class AddingNewNPC_Action extends SubmenuAction{
    Simple_NPC_AddingController npc_addingController;
    boolean lifeDataWasSetByLaunch = false;
    public static final byte ON_MAP_PLACING = 1;
    public static final byte AI_MODEL_SETTING = 2;
    public static final byte DIMENSION_SETTING = 3;
    public static final byte END = 4;
    protected Enemy debugCharacter;
    protected boolean debugTintWasSet;
    protected boolean debugCharacterwasDeleted;

    //behaviour
    protected static final String FLYING = "Fly";
    protected static final String GO_AND_JUMP = "Go and jump";
    protected static final String GO = "Go";

    public AddingNewNPC_Action(MapZone mapZone, GameObjectDataForStoreInEditor objectData, String personClassName) {
        super(mapZone, objectData);
        objectData.setClassName(personClassName);
        npc_addingController = new Simple_NPC_AddingController();
        calculateBasicLifeValue();
    }

    protected abstract void calculateBasicLifeValue();

    protected void makeTabWithLifeLine(androidGUI_ScrollableTab tab, int lifeValue){
        tab.clearElements();
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "", 1, GameObject.IMMORTALY_LIFE);
        slider.setText("Immortal");
        slider.setUserValue(lifeValue);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Life", true, 1, GameObject.IMMORTALY_LIFE);
        String text = "Set life";
        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        textField.setText(text);
        ((androidAndroidGUI_Slider) slider).setValue(lifeValue);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
        System.out.println("Local statement now: " + Editor2D.localStatement);
    }

    protected void resetPreviousData(){
        debugCharacterwasDeleted = false;
        debugTintWasSet = false;
    }

    protected void makeTabWithDimensionSetting(androidGUI_ScrollableTab tab, int dimensonValue, int min, int max){
        tab.clearElements();
        if (min <1) min = 1;
        if (max > 250) max = 250;
        System.out.println("Dimension for " + this + " is " + dimensonValue);
        if (dimensonValue>max) dimensonValue = max;
        else if (dimensonValue<min) dimensonValue = min;
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "", min, max);
        slider.setText(Integer.toString(max));
        slider.setUserValue(dimensonValue);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Diameter", true, min, max);
        String text = "Set diameter in px";
        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        textField.setText(text);
        ((androidAndroidGUI_Slider) slider).setValue(dimensonValue);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
        System.out.println("Local statement: " + Editor2D.localStatement);
    }


    public abstract void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement);

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, tabController.getTab().getElements(), releasedElements);
        }
    }

    protected void setLifeValue(GameObjectDataForStoreInEditor objectData, ArrayList <androidGUI_Element> androidGui_elements){
        for (androidGUI_Element element : androidGui_elements) {
            if (element.getClass() == androidAndroidGUI_Slider.class) {
                if (objectData.getLife() != element.getValue()) {
                    objectData.setLife((int) element.getValue());
                    System.out.println("Life updated to : " + objectData.getLife() + " by first launch");
                    lifeDataWasSetByLaunch = true;
                }
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList <androidGUI_Element> guiElements, ArrayList<androidGUI_Element> releasedElements) {
        //if (releasedElements.size()>0) {
            for (androidGUI_Element releasedElement : releasedElements) {
                if (Editor2D.localStatement == ON_MAP_PLACING) {
                    if (releasedElement.getClass() == androidAndroidGUI_Slider.class) {
                        if (objectData.getLife() != releasedElement.getValue()) {
                            objectData.setLife((int) releasedElement.getValue());
                            System.out.println("Life updated to : " + objectData.getLife());
                        }
                    }
                }
                else if (Editor2D.localStatement == AI_MODEL_SETTING) {
                    updateAiSetting(releasedElement.getName(), objectData);
                    //makePauseToNextOperation();

                }
                else if (Editor2D.localStatement == DIMENSION_SETTING) {
                    if (releasedElement.getClass() == androidAndroidGUI_Slider.class) {
                        if (debugCharacter != null) {
                            debugCharacter.setGraphicDimensionFromEditor(releasedElement.getValue());
                            System.out.println("Dimension was set to : " + releasedElement.getValue());
                        }
                        //}
                    }
                    if (releasedElement.getClass() == androidAndroidGUI_Button.class) {
                        for (androidGUI_Element androidGui_element : guiElements){
                            if (androidGui_element.getClass() == androidAndroidGUI_Slider.class){
                                objectData.setDimension((int) androidGui_element.getValue());
                                System.out.println("Dimension set to : " + objectData.getDimension());
                            }
                        }
                        Editor2D.setNewLocalStatement(END);
                        makePauseToNextOperation();
                        System.out.println("Ended");
                    }
                }
            }
    }

    protected void updateAiSetting(String name, GameObjectDataForStoreInEditor objectData) {

        System.out.println("No data about AI for this enemy");
    }

    /*
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ON_MAP_PLACING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("What must it do?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement < Simple_NPC_AddingController.COMPLETED && Editor2D.getGlobalStatement() != Editor2D.PLACE_KOOPA) || (Editor2D.localStatement == AddingNewKoopaAction.ADDING_LIFE_LINE && Editor2D.getGlobalStatement() == Editor2D.PLACE_KOOPA)) {
                    String name = "";
                    if (Editor2D.getGlobalStatement() == Editor2D.PLACE_GUMBA) name = "mushroom man";
                    else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_BOWSER) name = "dragon";
                    else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_KOOPA) name = "turtle";
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place the " + name);
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
    }*/

    public abstract void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData);

    /*
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        if (!lifeDataWasSetByLaunch) setLifeValue(objectData, tabController.getTab().getElements());
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement == Simple_NPC_AddingController.ADDING_LIFE_LINE_FOR_GUMBA){
            npc_addingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (npc_addingController.canBeNewObjectAdded()){
                Editor2D.setNewLocalStatement((byte)(Simple_NPC_AddingController.ADDING_LIFE_LINE_FOR_GUMBA+1));
            }
        }
        if (Editor2D.localStatement == (Simple_NPC_AddingController.ADDING_LIFE_LINE_FOR_GUMBA+1)){
            PVector nearestPointPos = LevelsEditorProcess.getPointInWorldPosition(levelsEditorProcess.getEditorCamera(), new PVector(Game2D.engine.mouseX, Game2D.engine.mouseY));
            objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
            if (objectData.getLife()<=0) {
                objectData.setLife(getLife(tabController));
            }
            Editor2D.setNewLocalStatement(Simple_NPC_AddingController.END);
            saveBasicLifeValue(objectData.getLife());
            npc_addingController.endAdding();
            System.out.println("Data stores in objectData");
            npc_addingController.switchOffTimer();
        }
    }*/

    protected abstract void saveBasicDimensionValue(int dimension);

    protected abstract void saveBasicLifeValue(int life);



    protected int getLife(ScrollableTabController tabController) {
        for (androidGUI_Element androidGui_element : tabController.getTab().getElements()){
            if (androidGui_element.getClass() == androidAndroidGUI_Slider.class){
                return androidGui_element.getValue();
            }
        }
        System.out.println("Can not get data from slider");
        return -1;
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        npc_addingController.draw(gameCamera, levelsEditorProcess);
    }

    protected void saveData(ScrollableTabController tabController, GameCamera editorCamera,  GameObjectDataForStoreInEditor objectData){
        PVector nearestPointPos = LevelsEditorProcess.getPointInWorldPosition(editorCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY));
        objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        if (objectData.getLife()<=0) {
            objectData.setLife(getLife(tabController));
        }
        saveBasicLifeValue(objectData.getLife());
        saveBasicDimensionValue(objectData.getDimension());
        //saveBasicDimensionValue(objectData.getDimension());
        npc_addingController.endAdding();
        System.out.println("Data stores in objectData");
        npc_addingController.switchOffTimer();
    }

    @Override
    public void clearRedundantObjects(GameRound gameRound){
        deleteDebugCharacter(gameRound);
        System.out.println("Debug character was deleted");
    }

    protected abstract void createDebugCharacter(GameRound gameRound, GameObjectDataForStoreInEditor objectData);

    protected void deleteDebugCharacter(GameRound gameRound) {
        for (int i = (gameRound.getPersons().size()-1); i>= 0; i--){
            if (debugCharacter != null) {
                if (gameRound.getPersons().get(i).equals(debugCharacter)) {
                    gameRound.getPersons().remove(debugCharacter);
                    System.out.println("Cleared from the world");
                    return;
                }
            }
        }
        System.out.println("Debug character is not founded in array; " + (debugCharacter == null));
        debugCharacter = null;
        //gameRound.addNewGameObject(debugCharacter);
    }

    @Override
    public byte getEndValue(){
        return END;
    }

}
