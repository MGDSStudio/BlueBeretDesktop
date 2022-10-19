package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Slider;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PlayerAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.Simple_NPC_AddingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

import java.util.ArrayList;

public class PlayerReplacingAction extends SubmenuAction{
    private final static byte ON_MAP_ADDING = 1;
    private final static byte COMPLETE = 2;
    public final static byte END = COMPLETE;

    private PlayerAddingController playerAddingController;

    public PlayerReplacingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(Soldier.CLASS_NAME);
        playerAddingController = new PlayerAddingController();
        playerAddingController.setAllignedWithGrid(false);
    }

    public PlayerAddingController getPlayerAddingController(){
       return playerAddingController;
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        int distanceToFirstElement = (int) (Program.engine.width / 11.1f);
        tab.clearElements();
        Editor2D.localStatement = 1;
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList <androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0) {
            for (androidGUI_Element releasedElement : releasedElements) {
                if (Editor2D.localStatement == Simple_NPC_AddingController.START_STATEMENT) {
                    if (releasedElement.getClass() == androidAndroidGUI_Slider.class) {
                        if (objectData.getLife() != releasedElement.getValue()) {
                            objectData.setLife((int) releasedElement.getValue());
                            System.out.println("Life updated to : " + objectData.getLife());
                        }
                    }
                }
            }
        }
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= ON_MAP_ADDING){
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set new position for player");
                    onScreenConsole.setText(actualConsoleText);
                }
                else {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Succesfully");
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

    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        playerAddingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
        //System.out.println("Replacing player local statement " + Editor2D.localStatement);
        if (playerAddingController.canBeNewObjectAdded() && Editor2D.localStatement < END){
            playerAddingController.addObjectOnNewPlace(levelsEditorProcess.getEditorCamera(), levelsEditorProcess.getGameRound().getPlayer(), objectData);
            //objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));

            //PVector nearestPointPos = LevelsEditorProcess.getPointInWorldPosition(levelsEditorProcess.getEditorCamera(), new PVector(Game2D.engine.mouseX, Game2D.engine.mouseY));
            //objectData.setPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
            //System.out.println("Position: " + nearestPointPos );
            //playerAddingController.deletePrevisiosPositionForPlayerInDataFile();
            Editor2D.setNewLocalStatement(END);
            levelsEditorProcess.pointsOnMap.clear();
            //playerAddingController.setOnNewPosition(levelsEditorProcess.getGameRound().getPlayer(), new Vec2(nearestPointPos.x, nearestPointPos.y));


            //playerAddingController.end();

            //playerAddingController.setOnNewPosition(levelsEditorProcess.getGameRound().getPlayer(), new Vec2(nearestPointPos.x, nearestPointPos.y));
        }


        /*
        playerAddingController.update(gameCamera, levelsEditorProcess);
        if (playerAddingController.canBeNewObjectAdded()){
            GameObject gameObject = new Soldier(new PVector(0,0));
            playerAddingController.addNewObject(gameCamera, levelsEditorProcess, gameObject);
            levelsEditorProcess.pointsOnMap.clear();
            levelsEditorProcess.figures.clear();
        }
        */




    }

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
        playerAddingController.draw(gameCamera, levelsEditorProcess);
    }

    /*
    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        Editor2D.localStatement = 0;
        makePauseToNextOperation();
    }*/

    @Override
    public byte getEndValue(){
        return END;
    }

}
