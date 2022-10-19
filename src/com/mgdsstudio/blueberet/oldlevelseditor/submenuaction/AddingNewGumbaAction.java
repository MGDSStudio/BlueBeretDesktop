package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import processing.core.PVector;

import java.util.ArrayList;

public class AddingNewGumbaAction extends AddingNewNPC_Action{
    private static int BASIC_LIFE_VALUE = -1;
    private static int BASIC_DIMENTION_VALUE = 42;

    public AddingNewGumbaAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData, String personClassName) {
        super(mapZone, objectData, personClassName);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement < ON_MAP_PLACING) {
            makeTabWithLifeLine(tab, BASIC_LIFE_VALUE);
            Editor2D.setNewLocalStatement(ON_MAP_PLACING);
            resetPreviousData();
        }
        else if (localStatement == DIMENSION_SETTING){
            makeTabWithDimensionSetting(tab, BASIC_DIMENTION_VALUE, 10, 90);
        }
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ON_MAP_PLACING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Place mushroom man on map");
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


    @Override
    protected void calculateBasicLifeValue(){
        if (BASIC_LIFE_VALUE < 0) {
            int normalLife = Gumba.NORMAL_LIFE;
            BASIC_LIFE_VALUE = normalLife;
        }
    }

    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_LIFE_VALUE = value;
    }

    @Override
    protected void saveBasicDimensionValue(int value){
        BASIC_DIMENTION_VALUE = value;
    }



    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        if (!lifeDataWasSetByLaunch) setLifeValue(objectData, tabController.getTab().getElements());
        updateTabController(objectData, levelsEditorProcess, tabController);
        //System.out.println("Gumba adding " + Editor2D.localStatement);
        if (Editor2D.localStatement == ON_MAP_PLACING){
            npc_addingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (npc_addingController.canBeNewObjectAdded()){
                Editor2D.setNewLocalStatement((DIMENSION_SETTING));
                saveData(tabController, levelsEditorProcess.getEditorCamera(), objectData);
                createDebugCharacter(levelsEditorProcess.getGameRound(), objectData);
            }
        }
        if (Editor2D.localStatement == DIMENSION_SETTING){
            if (!debugTintWasSet) {
                debugCharacter.setHalfTint();
                debugTintWasSet = true;
            }
        }
    }

    @Override
    protected void createDebugCharacter(GameRound gameRound, GameObjectDataForStoreInEditor objectData) {
        debugCharacter = new Gumba(new PVector(objectData.getPosition().x, objectData.getPosition().y), objectData.getLife(), BASIC_DIMENTION_VALUE);
        gameRound.addNewGameObject(debugCharacter);
        //System.out.println("Debug Gumba was created");
    }

    @Override
    public byte getEndValue(){
        return END;
    }



}
