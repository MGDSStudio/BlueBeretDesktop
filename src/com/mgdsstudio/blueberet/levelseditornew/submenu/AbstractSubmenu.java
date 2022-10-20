package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectData;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.engine.nesgui.*;

public abstract class AbstractSubmenu implements IVirtualKeyboardController {
    private int statement = 0;
    protected GameObjectData gameObjectData;

    protected interface StandardGuiNames{
        String BACK = "BACK";
    }



    protected final static int END = 100;
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
        statement = 0;
    }

    protected void setTextForConsole (String newText){
        FrameWithMoveableText console = levelsEditor.getConsole();
        console.changeConsoleText(newText);
    }
    protected void initEditorForActualSubmenu() {
        setActualClassAsKeyboardManager();
    }

    //public abstract void update();

    public void update(){
        if (isSomeGuiReleased()){
            GuiElement guiElement = getPressedGuiReleased();
            if (guiElement!= null){
                onReleased(guiElement);
            }
        }
    }

    protected void setActualClassAsKeyboardManager(){
        Tab tab = levelsEditor.getTab();
        for (GuiElement guiElement : tab.getElements()){
            if (guiElement instanceof DigitDataFieldWithText ){
                DigitDataFieldWithText digitDataFieldWithText = (DigitDataFieldWithText) guiElement;
                digitDataFieldWithText.setVirtualKeyboardController(this);
            }
            else if (guiElement instanceof TextDataFieldWithText){
                TextDataFieldWithText digitDataFieldWithText = (TextDataFieldWithText) guiElement;
                digitDataFieldWithText.setVirtualKeyboardController(this);
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

    @Override
    public void virtualKeyboardMustBeShown() {
        if (Program.OS == Program.ANDROID){
            Program.iEngine.openKeyboard(true);
        }
        if (Program.debug) System.out.println("Keyboard must be opened");
    }

    @Override
    public void virtualKeyboardMustBeClosed() {
        if (Program.OS == Program.ANDROID){
            Program.iEngine.openKeyboard(false);
        }
        //if (Program.debug) System.out.println("Keyboard must be closed");
    }

    public void saveDataBySubmenuLeaving() {
        levelsEditor.getMapZone().clearPointsAndFigures();
        System.out.println("No data must be saved for this submenu by leaving");
    }

    protected void setNextStatement() {
        statement++;
        recreateTabForNewStatement();
    }

    protected void setStatement(int newStatement) {
        statement=newStatement;
        recreateTabForNewStatement();
    }

    protected void setPrevStatement() {
        statement--;
        recreateTabForNewStatement();
    }

    protected int getStatement() {
        return statement;
    }

    protected abstract void recreateTabForNewStatement();

    protected void ended() {
        setTextForConsole("SUCCESSFULLY");
        levelsEditor.getTab().clear();
        setStatement(0);
        if (gameObjectData != null) {
            gameObjectData.createDataString();
            if (gameObjectData.getDataString() != null) {
                levelsEditor.addUnsavedData(gameObjectData.getDataString());
            }
        }
        levelsEditor.getMapZone().clearPointsAndFigures();
    }
}
