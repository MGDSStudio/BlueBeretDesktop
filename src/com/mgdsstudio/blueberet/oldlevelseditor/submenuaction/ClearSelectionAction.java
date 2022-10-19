package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

import java.util.ArrayList;

public class ClearSelectionAction extends SubmenuAction{
    private boolean selectionCleared = false;

    public ClearSelectionAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
    }



    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (tab.getElements().size()>0) {
            tab.clearElements();
        }
    }

    protected void updateTabController( ScrollableTabController tabController){
        tabUpdating(tabController.getTab().getElements());
    }

    protected void tabUpdating(ArrayList<androidGUI_Element> androidGui_elements) {


    }

    private void clearSelection(GameRound gameRound){
        SingleGameElement.resetTintValue();
        for (int i = 0; i < SelectingAction.selectedElements.size(); i++) {
            SelectingAction.selectedElements.get(i).getSelectedObject().setSelected(false);
            SelectingAction.selectedElements.get(i).getSelectedObject().clearSelection();
        }
        selectionCleared = true;
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Selection cleared");
                onScreenConsole.setText(actualConsoleText);
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
        updateTabController(tabController);
        if (!selectionCleared) clearSelection(levelsEditorProcess.getGameRound());
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        //Nothing to draw
    }

    @Override
    public byte getEndValue(){
        //System.out.println("This value is not end value");
        return 100;
    }

}
