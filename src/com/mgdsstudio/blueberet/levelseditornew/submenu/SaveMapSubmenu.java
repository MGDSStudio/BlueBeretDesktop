package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameobjects.data.ObjectsClearingZoneData;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.loading.RoundLoader;
import com.mgdsstudio.blueberet.oldlevelseditor.Figure;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.engine.nesgui.ButtonWithCursor;
import com.mgdsstudio.engine.nesgui.GuiElement;
import com.mgdsstudio.engine.nesgui.Tab;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class SaveMapSubmenu extends AbstractSubmenu{

    private interface Statements{
        int AGREEMENT = 0;
    }

    private interface Constants{
        String SAVE = "SAVE";
    }

    public SaveMapSubmenu(LevelsEditor levelsEditor, GameRound gameRound, EditorControl editorControl, MapZone mapZone) {
        super(levelsEditor, gameRound, editorControl, mapZone);
    }

    @Override
    protected void initEditorForActualSubmenu() {
        recreateTabForNewStatement();
    }

    @Override
    protected void onReleased(GuiElement guiElement) {
        if (getStatement() == Statements.AGREEMENT) {
            if (guiElement.getName() == Constants.SAVE) {
                setStatement(END);
            }
        }
    }

    @Override
    protected void recreateTabForNewStatement() {
        if (getStatement() == END){
            ended();
        }
        else if (getStatement() == Statements.AGREEMENT){
            Tab tab = levelsEditor.getTab();
            tab.clear();
            tab.createGui(Constants.SAVE, ButtonWithCursor.class, null);
            tab.recalculateHeight();
            setTextForConsole("ARE YOU SURE YOU WANT TO SAVE THE DATA? ");
        }
    }

    @Override
    protected void ended(){
        super.ended();
        levelsEditor.writeObjectsDataToRoundFile();

    }
}
