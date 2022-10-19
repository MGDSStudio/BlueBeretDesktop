package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_RadioButton;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectWithSetableFormAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.SingleFlagZoneAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewEndLevelZoneAction extends OneZoneAddingAction{

    public AddingNewEndLevelZoneAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(EndLevelZone.CLASS_NAME);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
            if (localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING) {
                tab.clearElements();
                tab.setMinimalHeight();
                androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.5f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, EndLevelZone.PLAYER_APPEARING_IN_ZONE_STRING);
                tab.addGUI_Element(element, null);
                element.setStatement(androidGUI_Element.PRESSED);
                androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.5f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, EndLevelZone.NPCS_LEAVING_ZONE_STRING);
                tab.addGUI_Element(element2, null);

                ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
                allRadioButtons.add(element);
                allRadioButtons.add(element2);

                element.addAnotherRadioButtonsInGroup(allRadioButtons);
                element2.addAnotherRadioButtonsInGroup(allRadioButtons);

                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 40 + 1 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);

                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
            }

    }

    @Override
    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> guiReleasedElements = tabController.getReleasedElements();
        if (guiReleasedElements.size()>0) {
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, guiReleasedElements);
        }
        if (Editor2D.canBeNextOperationMade()) {
            endLevelZoneUpdating(objectData, levelsEditorProcess, guiReleasedElements);
        }
    }

    private void endLevelZoneUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElement) {
        if (Editor2D.localStatement == SingleFlagZoneAdding.SAVING){
            for (androidGUI_Element element : releasedElement){
                if (element.getClass() == androidAndroidGUI_RadioButton.class){
                    byte goal = 0;
                    //if (element.getStatement() == GUI_Element.PRESSED || element.getStatement() == GUI_Element.RELEASED){
                    if (element.getName() == EndLevelZone.PLAYER_APPEARING_IN_ZONE_STRING) goal = EndLevelZone.PLAYER_APPEARING_IN_ZONE;
                    else if (element.getName() == EndLevelZone.NPCS_LEAVING_ZONE_STRING) goal = EndLevelZone.NPCS_LEAVING_ZONE;
                    objectData.setGoal(goal);
                    Editor2D.setNextLocalStatement();
                    //Editor2D.setNextLocalStatement();
                    System.out.println("Added "+ goal);
                }
                else if (element.getClass() == androidAndroidGUI_Button.class){
                    if (element.getName() == CANCEL){
                        Editor2D.localStatement = 0;
                        //levelsEditorProcess.pointsOnMap.clear();
                        System.out.println("Reset");
                    }
                }

            }
        }
        if (Editor2D.localStatement == SingleFlagZoneAdding.END){
            makePauseToNextOperation();
        }

    }


    private void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {

        for (androidGUI_Element element : releasedElements){
            if (Editor2D.localStatement == SingleFlagZoneAdding.SAVING) {
                if (element.getClass() == androidAndroidGUI_RadioButton.class) {
                    byte goal = 0;
                    if (element.getName() == EndLevelZone.PLAYER_APPEARING_IN_ZONE_STRING) {
                        goal = EndLevelZone.PLAYER_APPEARING_IN_ZONE;
                    }
                    else if (element.getName() == EndLevelZone.NPCS_LEAVING_ZONE_STRING) {
                        goal = EndLevelZone.NPCS_LEAVING_ZONE;
                    }
                    objectData.setGoal(goal);
                    Editor2D.setNextLocalStatement();
                    System.out.println("Added with goal " + goal);
                }
            }
            else if (element.getClass() == androidAndroidGUI_Button.class){
                if (element.getName() == CANCEL){
                    Editor2D.localStatement = 0;
                    levelsEditorProcess.pointsOnMap.clear();
                    System.out.println("Reset");
                }
            }
        }
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            if (Editor2D.getGlobalStatement() == Editor2D.ADDING_OBJECT_CLEARING_ZONE) {
                try {
                    if (Editor2D.localStatement == SingleFlagZoneAdding.FIRST_POINT_ADDING) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Add first zone corner");
                        onScreenConsole.setText(actualConsoleText);
                    } else if (Editor2D.localStatement == SingleFlagZoneAdding.SECOND_POINT_ADDING) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Add second zone corner");
                        onScreenConsole.setText(actualConsoleText);
                    } else if (Editor2D.localStatement == SingleFlagZoneAdding.END) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("New end level zone was added");
                        onScreenConsole.setText(actualConsoleText);
                    }
                }
                catch (Exception e) {

                }
            }
        }

    }


    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (rectangularElementAdding == null) rectangularElementAdding = new RectangularElementAdding();
        if (!rectangularElementAdding.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < RectangularElementAdding.ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                System.out.println("Added new polygon");
            }
        }
        else objectWasAdded(levelsEditorProcess);
    }

    @Override
    public byte getEndValue(){
        return SingleFlagZoneAdding.END;
    }

}
