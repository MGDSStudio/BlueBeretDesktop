package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.EditorPreferencesMaster;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.engine.nesgui.*;

public class ClearingZoneSubmenu extends AbstractSubmenu{
    private interface Constants{
        String ADD_POINT = "ADD POINT";
    }

    public ClearingZoneSubmenu(LevelsEditor levelsEditor, GameRound gameRound, EditorControl editorControl, MapZone mapZone) {
        super(levelsEditor, gameRound, editorControl, mapZone);
    }

    @Override
    protected void initEditorForActualSubmenu() {
        setTextForConsole("ADD FIRST POINT FOR THE CLEARING ZONE");
        Tab tab = levelsEditor.getTab();
        tab.clear();
        tab.createGui(Constants.ADD_POINT, ButtonWithCursor.class, null);
        tab.recalculateHeight();
        System.out.println("Changed");
    }

    @Override
    protected void onReleased(GuiElement guiElement) {

    }
}
