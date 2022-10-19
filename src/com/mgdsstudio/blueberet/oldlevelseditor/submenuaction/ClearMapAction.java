package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PlayerAddingController;
import com.mgdsstudio.blueberet.loading.DeleteStringsMaster;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public class ClearMapAction extends  SubmenuAction{
    private final String YES = "Yes, clear this level";
    private final byte WAITING_FOR_AGREEMENT = 1;
    private final byte CLEARING = 2;
    private final byte CLEARED = 3;
    public static final byte END = 4;

    public ClearMapAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        Editor2D.localStatement = WAITING_FOR_AGREEMENT;
    }

    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= WAITING_FOR_AGREEMENT) {
            tab.clearElements();
            androidGUI_Element buttonYes = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, YES, false);

            tab.addGUI_Element(buttonYes, null);
            repositionGUIAlongY(tab, tab.getElements());
        }
        else {
            if (localStatement == CLEARING){
                tab.clearElements();
            }
            else {
                tab.clearElements();
            }
        }
    }

    protected void updateTabController( ScrollableTabController tabController){
        //if (Editor2D.canBeNextOperationMade()) {
            //ArrayList<GUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (releasedElements.size()>0) {
                tabUpdating(tabController, releasedElements);
            }
        //}
    }

    protected void tabUpdating(ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0) {
            if (Editor2D.localStatement <= WAITING_FOR_AGREEMENT) {
                for (androidGUI_Element releasedElement : releasedElements) {
                    if (releasedElement.getName() == YES) {
                        System.out.println("Map cleared succesfully");
                        Editor2D.setNewLocalStatement(CLEARING);
                        //reconstructTab(tabController.getTab(), Editor2D.getGlobalStatement(), Editor2D.localStatement);
                    }
                }
            }
        }
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= WAITING_FOR_AGREEMENT) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Are you sure you want to clear this map?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else{
                    if (Editor2D.localStatement >= CLEARING) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Succesfully cleared");
                        onScreenConsole.setText(actualConsoleText);
                    }
                }
            }
            catch (Exception e) {
                //System.out.println("Can not change the text of the console " + e);
                //ArrayList<String> actualConsoleText = new ArrayList<>();
                //actualConsoleText.add("Successfully");
                //onScreenConsole.setText(actualConsoleText);
            }
        }
    }

    private void clearMap(LevelsEditorControl levelsEditorControl, GameRound gameRound){
        Editor2D.getNewObjectsData().clear();
        levelsEditorControl.getObjectData().clear();
        gameRound.clearObjects("All");
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
        deleteStringsMaster.deleteAllStringsExceptLevelName();
    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        //System.out.println("Local " + Editor2D.localStatement);
        updateTabController(tabController);
        if (Editor2D.localStatement == CLEARING){
            levelsEditorProcess.getEditorCamera().setNewActualPosition(new PVector(0,0));
            levelsEditorProcess.getEditorCamera().setNewPosition(new Vec2(0, (Program.engine.height/2)-Editor2D.zoneHeight/2+Editor2D.leftUpperCorner.y));
            Vec2 nullPos = new Vec2(-Program.engine.width/2, -Program.engine.height/2);
            PlayerAddingController playerAddingController = new PlayerAddingController();
            objectData.setPosition(nullPos);
            objectData.setClassName(Soldier.CLASS_NAME);
            GameObject player = levelsEditorProcess.getGameRound().getPlayer();
            playerAddingController.addObjectOnPlace(nullPos, player, objectData);
            objectData.setDataString(player.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) objectData.clone();
            clearMap(levelsEditorProcess.levelsEditorControl, levelsEditorProcess.getGameRound());
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Soldier was replaced");
            levelsEditorProcess.writeObjectsDataForLastObject();
            Editor2D.setNewLocalStatement(CLEARED);
        }
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        // Nothing to draw
    }

    @Override
    public byte getEndValue(){
        return END;
    }

}
