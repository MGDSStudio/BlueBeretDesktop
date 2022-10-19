package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.engine.nesgui.FrameWithMoveableText;
import com.mgdsstudio.engine.nesgui.GuiElement;
import com.mgdsstudio.engine.nesgui.Tab;

public abstract class AbstractSubmenu {

    protected LevelsEditor levelsEditor;
    protected EditorControl editorControl;
    protected MapZone mapZone;
    protected GameRound gameRound;
    public AbstractSubmenu(LevelsEditor levelsEditor, GameRound gameRound, EditorControl editorControl, MapZone mapZone) {
        this.levelsEditor = levelsEditor;
        this.editorControl = editorControl;
        this.mapZone = mapZone;
        this.gameRound  = gameRound;
        initEditorForActualSubmenu();
    }

    protected void setTextForConsole (String newText){
        FrameWithMoveableText console = levelsEditor.getConsole();
        console.changeConsoleText(newText);
    }
    protected abstract void initEditorForActualSubmenu();

    //public abstract void update();

    public void update(){
        if (isSomeGuiReleased()){
            GuiElement guiElement = getPressedGuiReleased();
            if (guiElement!= null){
                onReleased(guiElement);
            }
        }
    }

    protected abstract void onReleased(GuiElement guiElement);

    protected boolean isSomeGuiReleased(){
        Tab tab = levelsEditor.getTab();
        for (GuiElement element : tab.getElements()){
            if (element.getActualStatement() == GuiElement.RELEASED){
                return true;
            }
        }
        return false;
    }

    protected GuiElement getPressedGuiReleased(){
        Tab tab = levelsEditor.getTab();
        for (GuiElement element : tab.getElements()){
            if (element.getActualStatement() == GuiElement.RELEASED){
                return element;
            }
        }
        return null;
    }
}
