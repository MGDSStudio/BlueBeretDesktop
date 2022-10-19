package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.persons.Koopa;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public class AddingNewKoopaAction extends AddingNewNPC_Action{
    public static final byte SETTING_AI_MODEL = 1;
    public static final byte ADDING_LIFE_LINE = 2;
    public static final byte PLACING_ON_FIELD = 3;
    public static final byte SAVING = 4;

    public final static byte COMPLETED = 4;
    //public final static byte END = COMPLETED;



    private static int BASIC_LIFE_VALUE = -1;
    private static int BASIC_DIMENTION_VALUE = 42;


    public AddingNewKoopaAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData, String personClassName) {
        super(mapZone, objectData, personClassName);
        //objectData.setClassName(personClassName);
        //Editor2D.setNewLocalStatement(SETTING_AI_MODEL);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement < ON_MAP_PLACING) {
            makeTabWithLifeLine(tab, BASIC_LIFE_VALUE);
            Editor2D.setNewLocalStatement(ON_MAP_PLACING);
            resetPreviousData();
        }
        else if (localStatement == AI_MODEL_SETTING){
            tab.clearElements();
            tab.setMinimalHeight();
            androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FLYING, false);
            tab.addGUI_Element(buttonStatic, null);
            androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO_AND_JUMP, false);
            tab.addGUI_Element(buttonDynamic, null);
            androidGUI_Element buttonWithoutGraphic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO, false);
            tab.addGUI_Element(buttonWithoutGraphic, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);

        }
        else if (localStatement == DIMENSION_SETTING){
            makeTabWithDimensionSetting(tab, BASIC_DIMENTION_VALUE, 10, 90);
        }
        /*
        if (localStatement <= SETTING_AI_MODEL) {
            tab.clearElements();
            tab.setMinimalHeight();
            GUI_Element buttonStatic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FLYING, false);
            tab.addGUI_Element(buttonStatic, null);
            GUI_Element buttonDynamic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO_AND_JUMP, false);
            tab.addGUI_Element(buttonDynamic, null);
            GUI_Element buttonWithoutGraphic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO, false);
            tab.addGUI_Element(buttonWithoutGraphic, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
        }
        else if (localStatement == ADDING_LIFE_LINE) {
            //int normalLife = Koopa.NORMAL_LIFE;
            makeTabWithLifeLine(tab, BASIC_LIFE_VALUE);
            Editor2D.setNextLocalStatement();
        }
        */
    }

    @Override
    protected void updateAiSetting(String name, GameObjectDataForStoreInEditor objectData) {
        if (name == GO) {
            objectData.setAI_Model(Person.GOING);
            Editor2D.setNextLocalStatement();
        }
        else if (name == GO_AND_JUMP) {
            objectData.setAI_Model(Person.JUMPING);
            Editor2D.setNextLocalStatement();
        }
        else if (name == FLYING) {
            objectData.setAI_Model(Person.FLYING);
            Editor2D.setNextLocalStatement();
        }
        else System.out.println("There are no data about person AI model");
    }

/*
    @Override
    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, GUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<GUI_Element> pressedElements, ArrayList<GUI_Element> releasedElements) {
        //System.out.println("Text updating");
        // System.out.println("Statement: " + Editor2D.localStatement);
        if (releasedElements.size() > 0) {
            for (GUI_Element releasedElement : releasedElements) {
                if (Editor2D.localStatement <= SETTING_AI_MODEL) {
                    if (releasedElement.getClass() == GUI_Button.class) {
                        if (releasedElement.getName() == GO) objectData.setAI_Model(Person.GOING);
                        else if (releasedElement.getName() == GO_AND_JUMP) objectData.setAI_Model(Person.JUMPING);
                        else if (releasedElement.getName() == FLYING) objectData.setAI_Model(Person.FLYING);
                        Editor2D.setNewLocalStatement(ADDING_LIFE_LINE);
                        makePauseToNextOperation();
                    }
                } else if (Editor2D.localStatement == PLACING_ON_FIELD) {
                    if (releasedElement.getClass() == GUI_Slider.class) {
                        if (objectData.getLife() != releasedElement.getValue()) {
                            objectData.setLife((int) releasedElement.getValue());
                            System.out.println("Life updated to : " + objectData.getLife());
                        }
                    } else if (releasedElement.getClass() == GUI_Button.class) {
                        if (releasedElement.getName() == CANCEL) {
                            Editor2D.setNewLocalStatement(SETTING_AI_MODEL);
                            levelsEditorProcess.pointsOnMap.clear();
                            levelsEditorProcess.figures.clear();
                            makePauseToNextOperation();
                        }
                    }
                }

            }
        }
    }
*/

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ON_MAP_PLACING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place turtle on map");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == AI_MODEL_SETTING)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("  What must it do?  ");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == DIMENSION_SETTING)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set dimension for it");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            catch (Exception e) {
            }
        }
        else {
            if (Editor2D.localStatement>DIMENSION_SETTING){
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully added!");
                onScreenConsole.setText(actualConsoleText);
            }
        }
    }



    /*
    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= SETTING_AI_MODEL){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("  What must it do?  ");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement == ADDING_LIFE_LINE)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place the turtle");
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
    */

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        if (!lifeDataWasSetByLaunch) setLifeValue(objectData, tabController.getTab().getElements());
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement == ON_MAP_PLACING){
            if (!debugCharacterwasDeleted) {
                deleteDebugCharacter(levelsEditorProcess.getGameRound());
                debugCharacterwasDeleted = true;
            }
            npc_addingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (npc_addingController.canBeNewObjectAdded()){
                Editor2D.setNextLocalStatement();
                saveData(tabController, levelsEditorProcess.getEditorCamera(), objectData);
                createDebugCharacter(levelsEditorProcess.getGameRound(), objectData);
            }
        }
        if (Editor2D.localStatement == DIMENSION_SETTING || Editor2D.localStatement == AI_MODEL_SETTING){
            if (!debugTintWasSet) {
                debugCharacter.setHalfTint();
                debugTintWasSet = true;
            }
        }
    }



    /*
    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (Editor2D.localStatement == PLACING_ON_FIELD){
            npc_addingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (npc_addingController.canBeNewObjectAdded()){
                Editor2D.setNewLocalStatement((SAVING));
            }
        }
        if (Editor2D.localStatement == (SAVING)){
            saveData(tabController, levelsEditorProcess.getEditorCamera(), objectData);
            Editor2D.setNewLocalStatement(END);
        }

    }*/

    @Override
    protected void calculateBasicLifeValue(){
        if (BASIC_LIFE_VALUE < 0) {
            int normalLife = Koopa.NORMAL_LIFE;
            BASIC_LIFE_VALUE = normalLife;
        }
    }

    @Override
    protected void saveBasicDimensionValue(int value){
        BASIC_DIMENTION_VALUE = value;
    }
    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_LIFE_VALUE = value;
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        super.draw(gameCamera, levelsEditorProcess);
    }

    protected void createDebugCharacter(GameRound gameRound, GameObjectDataForStoreInEditor objectData) {
        debugCharacter = new Koopa(new PVector(objectData.getPosition().x, objectData.getPosition().y), objectData.getAI_Model(), objectData.getLife(), BASIC_DIMENTION_VALUE);
        gameRound.addNewGameObject(debugCharacter);
        System.out.println("Debug koopa was created");
    }

    @Override
    public byte getEndValue(){
        return END;
    }
}
