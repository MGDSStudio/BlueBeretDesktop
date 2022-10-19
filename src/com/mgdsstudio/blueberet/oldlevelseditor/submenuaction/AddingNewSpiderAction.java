package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Spider;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.SpiderController;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class AddingNewSpiderAction extends AddingNewNPC_Action {
    /*public static final byte SETTING_AI_MODEL = 1;
    public static final byte ADDING_LIFE_LINE = 2;
    public static final byte PLACING_ON_FIELD = 3;
    public static final byte SAVING = 4;*/
    public final static byte COMPLETED = 4;
    //public final static byte END = COMPLETED;
    private static int BASIC_LIFE_VALUE = 100;
    private static int BASIC_DIMENTION_VALUE = 32;
    private final String GO_ON_GROUND = "On ground";
    private final String GO_ON_WALL = "On wall";


    public AddingNewSpiderAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData, String personClassName) {
        super(mapZone, objectData, personClassName);

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
            androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO_ON_GROUND, false);
            tab.addGUI_Element(buttonStatic, null);
            androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GO_ON_WALL, false);
            tab.addGUI_Element(buttonDynamic, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);

        }
        else if (localStatement == DIMENSION_SETTING){
            makeTabWithDimensionSetting(tab, BASIC_DIMENTION_VALUE, 20, 190);
        }
    }

    @Override
    protected void updateAiSetting(String name, GameObjectDataForStoreInEditor objectData) {
        if (name == GO_ON_GROUND) {
            objectData.setAI_Model((byte) SpiderController.MOVEMENT_ON_GROUND);
            Editor2D.setNextLocalStatement();
        }
        else if (name == GO_ON_WALL) {
            objectData.setAI_Model((byte) SpiderController.GO_ON_WALL);
            Editor2D.setNextLocalStatement();
        }
        else System.out.println("There are no data about person AI model");
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ON_MAP_PLACING){
                    setTextForConsole(onScreenConsole, "Place spider on the map");
                }
                else if ((Editor2D.localStatement == AI_MODEL_SETTING)) {
                    setTextForConsole(onScreenConsole, "Where must it go?");
                }
                else if ((Editor2D.localStatement == DIMENSION_SETTING)) {
                    setTextForConsole(onScreenConsole, "Set dimension for it");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            /*
            if (Editor2D.localStatement>DIMENSION_SETTING){
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully added!");
                onScreenConsole.setText(actualConsoleText);
            }*/
        }
    }


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

    @Override
    protected void calculateBasicLifeValue(){
        if (BASIC_LIFE_VALUE < 0) {
            int normalLife = Spider.NORMAL_LIFE;
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
        debugCharacter = new Spider(new PVector(objectData.getPosition().x, objectData.getPosition().y), objectData.getAI_Model(), objectData.getLife(), BASIC_DIMENTION_VALUE);
        gameRound.addNewGameObject(debugCharacter);
        System.out.println("Debug spider was created");
    }

    @Override
    public byte getEndValue(){
        return END;
    }
}
