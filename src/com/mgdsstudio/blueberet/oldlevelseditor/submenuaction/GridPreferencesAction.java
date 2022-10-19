package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class GridPreferencesAction extends SubmenuAction{
    public GridPreferencesAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
    }

    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        tab.clearElements();
        int distanceToFirstElement = (int) (Program.engine.width / 11.1f);
        androidAndroidGUI_CheckBox checkBox = new androidAndroidGUI_CheckBox(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Show grid");
        if (Editor2D.showGrid) checkBox.setFlagSet(true);
        else checkBox.setFlagSet(false);
        tab.addGUI_Element(checkBox, null);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Grid spacing in px, e.g. "+ Editor2D.gridSpacing, true, 1, 500);

        //String text = "" + Editor2D.gridSpacing;
        textField.setText("Grid spacing in px, e.g. " + Editor2D.gridSpacing);
        tab.addGUI_Element(textField, null);
        Editor2D.localStatement = 1;
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    protected void updateTabController( ScrollableTabController tabController){
        tabUpdating(tabController.getTab().getElements());

        /*
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<GUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<GUI_Element> releasedElements = tabController.getReleasedElements();
            //if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(tabController.getTab().getElements(), pressedElements, releasedElements);
            //}
        }*/
    }

    protected void tabUpdating(ArrayList <androidGUI_Element> androidGui_elements) {
        /*
        if (releasedElements.size()>0) {

            for (GUI_Element releasedElement : releasedElements) {
                if (releasedElement.getClass() == GUI_CheckBox.class) {
                    if (((GUI_CheckBox) releasedElement).isFlagSet()){
                        if (!Editor2D.showGrid) Editor2D.showGrid = true;
                    }
                    else {
                        if (Editor2D.showGrid) Editor2D.showGrid = false;
                    }
                }
            }



        }
        //if (pressedElements.size()>0) {
            for (GUI_Element pressedElement : pressedElements) {
                if (pressedElement.getClass() == GUI_CheckBox.class) {
                    if (((GUI_CheckBox) pressedElement).isFlagSet()) {
                        if (!Editor2D.showGrid) Editor2D.showGrid = true;
                    } else {
                        if (Editor2D.showGrid) Editor2D.showGrid = false;
                    }
                }
            }
        //}
*/
        for (androidGUI_Element element : androidGui_elements){
            if (element.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField textField = (androidAndroidGUI_TextField) element;
                if (textField.getDigitValue() != Editor2D.gridSpacing){
                    int value  = textField.getDigitValue();
                    if (value > 0) Editor2D.gridSpacing = textField.getDigitValue();
                    //System.out.println("Grid distance was change. Value: "+ textField.getDigitValue());
                }
            }
            else if (element.getClass() == androidAndroidGUI_CheckBox.class) {
                if (((androidAndroidGUI_CheckBox) element).isFlagSet()){
                    if (!Editor2D.showGrid) Editor2D.showGrid = true;
                    //System.out.println("Show grid");
                    //if (!Editor2D.showGrid) Editor2D.showGrid = true;
                }
                else {
                    //if (Editor2D.showGrid) Editor2D.showGrid = false;
                    if (Editor2D.showGrid) Editor2D.showGrid = false;
                    //System.out.println("Hide grid");
                }
            }
        }

    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Set grid data");
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
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        //Nothing to draw
    }

    @Override
    public byte getEndValue(){
        //System.out.println("This value is not end value");
        return 10;
    }
}
