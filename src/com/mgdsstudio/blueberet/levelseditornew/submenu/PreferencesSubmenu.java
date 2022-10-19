package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.EditorMenu;
import com.mgdsstudio.blueberet.levelseditornew.EditorPreferencesMaster;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.mainpackage.AbstractCamera;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextArea;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.engine.nesgui.*;
import com.yandex.metrica.impl.ob.Ed;

public class PreferencesSubmenu extends AbstractSubmenu {

    private interface Constants{
        String GRID_STEP = "GRID STEP";
        String SHOW_GRID = "SHOW GRID";
    }

    public PreferencesSubmenu(LevelsEditor levelsEditor, GameRound gameRound, EditorControl editorControl, MapZone mapZone) {
        super(levelsEditor, gameRound,  editorControl, mapZone);
    }

    @Override
    protected void initEditorForActualSubmenu() {
        setTextForConsole("CHANGE GRID STEP OR HIDE IT IF YOU DO NOT NEED IT");
        EditorPreferencesMaster master = new EditorPreferencesMaster(levelsEditor.getGameMainController().getEngine());
        master.updateData();
        Tab tab = levelsEditor.getTab();
        boolean withGrid = master.isShowGrid();
        //tab.clear();
        tab.createGui(Constants.GRID_STEP, DigitDataFieldWithText.class, master.getGridStep());
        tab.createGui(Constants.SHOW_GRID, CheckBox.class, null);
        if (withGrid){
            CheckBox checkBox = (CheckBox) tab.getElementByName(Constants.SHOW_GRID);
            checkBox.setChecked(true);
        }
        tab.recalculateHeight();
        System.out.println("Changed");
    }

    @Override
    protected void onReleased(GuiElement guiElement) {
        if (guiElement.getName() == Constants.SHOW_GRID){
            CheckBox checkBox = (CheckBox) guiElement;
            Editor2D.showGrid = checkBox.isChecked();
        }
        else if (guiElement.getName() == Constants.GRID_STEP){
            DigitDataFieldWithText field = (DigitDataFieldWithText) guiElement;
            Editor2D.gridSpacing = field.getValue();

            System.out.println("Grid step set on " + field.getValue());
        }
    }


}
