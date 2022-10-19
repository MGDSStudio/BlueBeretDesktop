package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class ObjectDeletingAction extends SubmenuAction{
    private final String YES = "Yes, delete this elements";
    private final byte WAITING_FOR_AGREEMENT = 1;
    private final byte CLEARING = 2;
    private final byte SUCCESFULLY_DELETED = 3;
    public static final byte END = 4;

    public ObjectDeletingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        Editor2D.localStatement = WAITING_FOR_AGREEMENT;
    }

    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= WAITING_FOR_AGREEMENT) {
            if (SelectingAction.selectedElements != null) {
                if (SelectingAction.selectedElements.size() > 0) {
                    tab.clearElements();
                    androidGUI_Element buttonYes = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 50), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, YES, false);
                    tab.addGUI_Element(buttonYes, null);
                    try {
                        repositionGUIAlongY(tab, tab.getElements());
                        tab.recalculateHeight(tab.getElements());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{

                }
            }
        }
        else {
            if (Editor2D.localStatement >= CLEARING){
                Editor2D.localStatement = WAITING_FOR_AGREEMENT;
            }
            tab.clearElements();

        }
    }

    protected void updateTabController(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
        if (releasedElements.size()>0) {
            tabUpdating(levelsEditorProcess , tabController, releasedElements);
        }
        if (Editor2D.localStatement == CLEARING) {
            Editor2D.setNewLocalStatement(SUCCESFULLY_DELETED);
        }
    }

    protected void tabUpdating(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0) {
            if (Editor2D.localStatement <= WAITING_FOR_AGREEMENT) {
                for (androidGUI_Element releasedElement : releasedElements) {
                    if (releasedElement.getName() == YES) {
                        System.out.println("Objects were cleared successfully");
                        Editor2D.setNewLocalStatement((byte)(CLEARING+1));
                        deleteObjects(levelsEditorProcess, " was deleted");
                    }
                }
            }
        }
    }



    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement == WAITING_FOR_AGREEMENT) {
                    int elementsToBeDeleted = 0;
                    if (SelectingAction.selectedElements != null){
                        if (SelectingAction.selectedElements.size()>0) elementsToBeDeleted = SelectingAction.selectedElements.size();
                    }
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    if (elementsToBeDeleted>0) {
                        actualConsoleText.add("Are you sure you want to delete");
                        actualConsoleText.add(" this " + elementsToBeDeleted + " objects from the world? ");
                    }
                    else {
                        actualConsoleText.add("You need select elements to be deleted");
                    }
                    onScreenConsole.setText(actualConsoleText);
                }
                else{
                    if (Editor2D.localStatement >= CLEARING || Editor2D.localStatement == 0) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Successfully deleted");
                    }
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
        updateTabController( levelsEditorProcess, tabController);
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        //Nothing to draw
    }

    @Override
    public byte getEndValue(){
        return END;
    }
}
