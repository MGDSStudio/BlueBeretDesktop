package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LevelsListCreator;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class OpenMapAction extends SubmenuAction{
    public final static byte ROUND_SELECTING = 1;
    public final static byte ROUND_SELECTED = 2;
    public final static byte END = 3;
    private boolean levelChanged;
    private int newLevelNumber;
    //private GameMainController gameMainController;

    public OpenMapAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        Editor2D.setNewLocalStatement((byte) 0);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement < ROUND_SELECTING) {
            tab.clearElements();
            LevelsListCreator levelsListCreator = new LevelsListCreator(ExternalRoundDataFileController.USER_LEVELS);
            ArrayList<String> fileNamesList = levelsListCreator.getLevelsNamesList();
            for (int i = 0; i < fileNamesList.size(); i++){
                androidGUI_Element button = new androidAndroidGUI_Button(new Vec2(tab.getWidth()/2, 0),  (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, fileNamesList.get(i), false);
                tab.addGUI_Element(button, null);
            }
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            Editor2D.setNewLocalStatement(ROUND_SELECTING);
            System.out.println("levels number: " + tab.getElements().size());
        }
        else if (localStatement == ROUND_SELECTED){
            tab.clearElements();
        }
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, releasedElements);
        }
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement == ROUND_SELECTING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set another level to open");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if ((Editor2D.localStatement >= ROUND_SELECTED)) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Loading");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            catch (Exception e) {
                System.out.println("Can not change the text of the console " + e);
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Text on console is not changeable");
                onScreenConsole.setText(actualConsoleText);
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0) {
            for (androidGUI_Element releasedElement : releasedElements) {
                if (Editor2D.localStatement == ROUND_SELECTING){
                    if (releasedElement.getClass() == androidAndroidGUI_Button.class) {
                        LevelsListCreator levelsListCreator = new LevelsListCreator(ExternalRoundDataFileController.USER_LEVELS);
                        String pathToFile = levelsListCreator.getLevelPathByLevelName(releasedElement.getName());
                        newLevelNumber = levelsListCreator.getLevelNumberByPath(pathToFile);
                        levelChanged = true;
                        //Program.actualRoundNumber = newLevelNumber;
                        //Editor2D.changeLevelNumber(newLevelNumber);
                        objectData.setClassName("");
                        System.out.println("Path to file: " + pathToFile + " new level: " + newLevelNumber);
                        //Editor2D.setNewGlobalStatement();
                        Editor2D.setNewLocalStatement(ROUND_SELECTED);

                        //makePauseToNextOperation();
                    }
                }
            }
        }
    }


    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement == ROUND_SELECTING){
            updateTabController(objectData, levelsEditorProcess, tabController);
        }
        else if (Editor2D.localStatement == ROUND_SELECTED){
                makePauseToNextOperation();
                Editor2D.setNextLocalStatement();
        }
        if (Editor2D.localStatement == END){
             Editor2D.setNewLocalStatement((byte)0);
            if (!Editor2D.isLevelChanged()) {
                Editor2D.changeLevelNumber(newLevelNumber);
                makePauseToNextOperation();
                Editor2D.setNextLocalStatement();
                System.out.println("Level number was changed");
            }
        }
        //System.out.println("Level number: " + Program.actualRoundNumber + "; Local: " + Editor2D.localStatement);
    }

    @Override
    public byte getEndValue(){
        return END;
    }

}
