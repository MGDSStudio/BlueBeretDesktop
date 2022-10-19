package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_RadioButton;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectWithSetableFormAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.SingleFlagZoneAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewObjectClearingZoneAction extends OneZoneAddingAction{
    private static int lastRadioButtonToBeSaved = 0;

    public AddingNewObjectClearingZoneAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(ObjectsClearingZone.CLASS_NAME);

    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING || localStatement == ObjectWithSetableFormAdding.SECOND_POINT_ADDING) {
            tab.clearElements();
            tab.setMinimalHeight();
            androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ObjectsClearingZone.DELETE_EVERY_OBJECT_STRING);
            tab.addGUI_Element(element, null);
            element.setStatement(androidGUI_Element.PRESSED);
            androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ObjectsClearingZone.DELETE_EVERY_PERSON_STRING);
            tab.addGUI_Element(element2, null);
            androidAndroidGUI_RadioButton element3 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ObjectsClearingZone.DELETE_EVERY_PERSON_WITHOUT_PLAYER_STRING);
            tab.addGUI_Element(element3, null);
            androidAndroidGUI_RadioButton element4 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 90), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ObjectsClearingZone.DELETE_CORPSES_STRING);
            tab.addGUI_Element(element4, null);
            androidAndroidGUI_RadioButton element5 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 90), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ObjectsClearingZone.DELETE_ROUND_ELEMENTS_STRING);
            tab.addGUI_Element(element5, null);

            ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
            allRadioButtons.add(element);
            allRadioButtons.add(element2);
            allRadioButtons.add(element3);
            allRadioButtons.add(element4);
            allRadioButtons.add(element5);

            element.addAnotherRadioButtonsInGroup(allRadioButtons);
            element2.addAnotherRadioButtonsInGroup(allRadioButtons);
            element3.addAnotherRadioButtonsInGroup(allRadioButtons);
            element4.addAnotherRadioButtonsInGroup(allRadioButtons);
            element5.addAnotherRadioButtonsInGroup(allRadioButtons);
            selectSaved(tab.getElements());

            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
        }
        if (localStatement == ObjectWithSetableFormAdding.SECOND_POINT_ADDING){
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 25+22 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
        }

    }

    private void selectSaved(ArrayList<androidGUI_Element> elements) {
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == androidAndroidGUI_RadioButton.class){
                if (i == lastRadioButtonToBeSaved){
                    elements.get(i).setStatement(androidGUI_Element.PRESSED);
                    System.out.println("Selected " + i + " radio button");
                }
                else {
                    if (elements.get(i).getStatement() == androidGUI_Element.PRESSED || elements.get(i).getStatement() == androidGUI_Element.RELEASED){
                        elements.get(i).setStatement(androidGUI_Element.ACTIVE);
                    }
                }
            }
        }


    }

    @Override
    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> guiReleasedElements = tabController.getReleasedElements();
        ArrayList<androidGUI_Element> guiPressedElements = tabController.getPressedElements();
        if (guiReleasedElements.size()>0) {
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, guiReleasedElements);
        }
    }

    private void saveLastRadioButton(ArrayList<androidGUI_Element> elements) {
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == androidAndroidGUI_RadioButton.class && elements.get(i).getStatement() == androidGUI_Element.PRESSED || elements.get(i).getStatement() == androidGUI_Element.RELEASED){
                if (i != lastRadioButtonToBeSaved){
                    lastRadioButtonToBeSaved = i;

                }
            }
        }
    }
/*
    private void objectClearingZoneUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ArrayList<GUI_Element> releasedElements, ArrayList<GUI_Element> guiPressedElements) {
        if (Editor2D.localStatement == SingleFlagZoneAdding.SAVING){
            for (GUI_Element element : releasedElements){
                if (element.getClass() == GUI_RadioButton.class){
                    byte goal = 0;
                    if (element.getName() == ObjectsClearingZone.DELETE_EVERY_OBJECT_STRING) goal = ObjectsClearingZone.DELETE_EVERY_OBJECT;
                    else if (element.getName() == ObjectsClearingZone.DELETE_EVERY_PERSON_WITHOUT_PLAYER_STRING) goal = ObjectsClearingZone.DELETE_EVERY_OBJECT_WITHOUT_PLAYER;
                    else if (element.getName() == ObjectsClearingZone.DELETE_EVERY_PERSON_STRING) goal = ObjectsClearingZone.DELETE_EVERY_PERSON;
                    else if (element.getName() == ObjectsClearingZone.DELETE_ROUND_ELEMENTS_STRING) goal = ObjectsClearingZone.DELETE_ROUND_ELEMENTS;
                    else if (element.getName() == ObjectsClearingZone.DELETE_CORPSES_STRING) goal = ObjectsClearingZone.DELETE_CORPSES;
                    objectData.setGoal(goal);
                    objectData.setActivatedBy(goal);
                    //Editor2D.setNextLocalStatement();
                    //Editor2D.setNextLocalStatement();
                    //levelsEditorProcess.pointsOnMap.clear();
                    //levelsEditorProcess.figures.clear();
                    System.out.println("Added with goal "+ objectData.getGoal() + "; Local statement: "  + Editor2D.localStatement );
                }
                else if (element.getClass() == GUI_Button.class){
                    if (element.getName() == CANCEL){
                        Editor2D.localStatement = 0;
                        System.out.println("Reset");
                    }
                }
            }
        }
        if (Editor2D.localStatement == SingleFlagZoneAdding.END){
            makePauseToNextOperation();
        }

    }
*/

    private void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {
            for (androidGUI_Element element : releasedElements){
                if (Editor2D.localStatement >= SingleFlagZoneAdding.SAVING) {
                    if (element.getClass() == androidAndroidGUI_RadioButton.class) {
                        byte goal = 0;
                        //if (element.getStatement() == GUI_Element.PRESSED || element.getStatement() == GUI_Element.RELEASED){
                        if (element.getName() == ObjectsClearingZone.DELETE_EVERY_OBJECT_STRING)
                            goal = ObjectsClearingZone.DELETE_EVERY_OBJECT;
                        else if (element.getName() == ObjectsClearingZone.DELETE_EVERY_PERSON_WITHOUT_PLAYER_STRING)
                            goal = ObjectsClearingZone.DELETE_EVERY_OBJECT_WITHOUT_PLAYER;
                        else if (element.getName() == ObjectsClearingZone.DELETE_EVERY_PERSON_STRING)
                            goal = ObjectsClearingZone.DELETE_EVERY_PERSON;
                        else if (element.getName() == ObjectsClearingZone.DELETE_CORPSES_STRING)
                            goal = ObjectsClearingZone.DELETE_CORPSES;
                        else if (element.getName() == ObjectsClearingZone.DELETE_ROUND_ELEMENTS_STRING)
                            goal = ObjectsClearingZone.DELETE_ROUND_ELEMENTS;
                        //else
                            //objectData.setGoal(goal);
                        saveLastRadioButton(tab.getElements());
                        rectangularElementAdding.setCompleted(true);
                        //Editor2D.setNextLocalStatement();
                        objectData.setGoal(goal);
                        System.out.println("Added with goal " + goal + "; Statement: " + Editor2D.localStatement);
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
        if (Editor2D.localStatement == SingleFlagZoneAdding.END){
            makePauseToNextOperation();
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
                        actualConsoleText.add("New clearing zone was added");
                        onScreenConsole.setText(actualConsoleText);
                    }
                } catch (Exception e) {

                }
            }
        }

    }

    @Override
    public byte getEndValue(){
        return SingleFlagZoneAdding.END;
    }


    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        //System.out.println("Local: " + Editor2D.localStatement);
        if (rectangularElementAdding == null) rectangularElementAdding = new RectangularElementAdding();
        if (!rectangularElementAdding.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < RectangularElementAdding.ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                //objectData.setMission(Flag.CLEAR_OBJECTS);
                System.out.println("Added new point for the clearing zone");
                //if (Editor2D.localStatement > RectangularElementAdding.SECOND_POINT_ADDING) Editor2D.setNewLocalStatement(getEndValue());
                //rectangularElementAdding = null;
            }
        }
        else {
            System.out.println("Goal: " + objectData.getGoal());
            objectWasAdded(levelsEditorProcess);
            Editor2D.setNewLocalStatement(SingleFlagZoneAdding.END);
        }
    }


}
