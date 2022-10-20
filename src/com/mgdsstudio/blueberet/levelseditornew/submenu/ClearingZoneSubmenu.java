package com.mgdsstudio.blueberet.levelseditornew.submenu;

import com.mgdsstudio.blueberet.gameobjects.data.ObjectsClearingZoneData;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.levelseditornew.EditorControl;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.loading.RoundLoader;
import com.mgdsstudio.blueberet.oldlevelseditor.Figure;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.engine.nesgui.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class ClearingZoneSubmenu extends AbstractSubmenu {

    private interface Statements{
        int FIRST_POINT = 0;
        int SECOND_POINT = 1;
        int ACTIVATING_CONDITION = 2;
    }

    private interface Constants{
        String ADD_POINT = "ADD POINT";
        String DELETE_EVERY_OBJECT = "EVERYTHING";
        String DELETE_EVERY_PERSON = "PERSONS";
        String DELETE_EVERY_OBJECT_WITHOUT_PLAYER = "ENEMIES";
        String DELETE_CORPSES = "CORPSES";
        String DELETE_ROUND_ELEMENTS = "ROUND ELEMENTS";
    }

    public ClearingZoneSubmenu(LevelsEditor levelsEditor, GameRound gameRound, EditorControl editorControl, MapZone mapZone) {
        super(levelsEditor, gameRound, editorControl, mapZone);
        gameObjectData = new ObjectsClearingZoneData();
    }

    @Override
    protected void initEditorForActualSubmenu() {
        recreateTabForNewStatement();
        //setTextForConsole("ADD FIRST POINT FOR THE CLEARING ZONE");
        System.out.println("Changed");
    }

    @Override
    protected void onReleased(GuiElement guiElement) {
        if (guiElement.getName() == StandardGuiNames.BACK){
            setPrevStatement();
        }
        else {
            if (getStatement() == Statements.FIRST_POINT || getStatement() == Statements.SECOND_POINT) {
                if (guiElement.getName() == Constants.ADD_POINT) {
                    Vec2 pos = levelsEditor.getCrossPos();
                    levelsEditor.getMapZone().addPoint(pos);
                    if (getStatement() == Statements.FIRST_POINT) ((ObjectsClearingZoneData)gameObjectData).setFirstPos(pos);
                    else if (getStatement() == Statements.SECOND_POINT) ((ObjectsClearingZoneData)gameObjectData).setSecondPoint(pos);
                    setNextStatement();
                    if (getStatement() == Statements.ACTIVATING_CONDITION) {
                        levelsEditor.getMapZone().createFigureFromPoints(Figure.RECTANGULAR_SHAPE);
                    }
                }
            } 
            else if (getStatement() == Statements.ACTIVATING_CONDITION) {
                //System.out.println("User data for this type: " + guiElement.getUserData().getClass());
                int value = (Byte) guiElement.getUserData();
                //System.out.println(" is : " + value);
                ((ObjectsClearingZoneData)gameObjectData).setParameter(value);
                setStatement(END);
            }
        }

    }

    @Override
    protected void recreateTabForNewStatement() {
        if (getStatement() == END){
            ended();
        }
        else if (getStatement() == Statements.FIRST_POINT){
            Tab tab = levelsEditor.getTab();
            tab.clear();
            tab.createGui(Constants.ADD_POINT, ButtonWithCursor.class, null);
            tab.recalculateHeight();
            setTextForConsole("ADD FIRST POINT FOR THE CLEARING ZONE");
        }
        else if (getStatement() == Statements.SECOND_POINT){
            levelsEditor.getTab().clear();
            levelsEditor.getTab().createGui(Constants.ADD_POINT, ButtonWithCursor.class, null);
            levelsEditor.getTab().createGui(StandardGuiNames.BACK, ButtonWithCursor.class, null);
            setTextForConsole("ADD SECOND POINT");
            levelsEditor.getTab().recalculateHeight();
        }
        else if (getStatement() == Statements.ACTIVATING_CONDITION){
            levelsEditor.getTab().clear();
            levelsEditor.getTab().createGui(Constants.DELETE_EVERY_OBJECT, ButtonWithCursor.class, ObjectsClearingZone.DELETE_EVERY_OBJECT);
            levelsEditor.getTab().createGui(Constants.DELETE_EVERY_PERSON, ButtonWithCursor.class, ObjectsClearingZone.DELETE_EVERY_PERSON);
            levelsEditor.getTab().createGui(Constants.DELETE_EVERY_OBJECT_WITHOUT_PLAYER, ButtonWithCursor.class, ObjectsClearingZone.DELETE_EVERY_OBJECT_WITHOUT_PLAYER);
            levelsEditor.getTab().createGui(Constants.DELETE_CORPSES, ButtonWithCursor.class, ObjectsClearingZone.DELETE_CORPSES);
            levelsEditor.getTab().createGui(Constants.DELETE_ROUND_ELEMENTS, ButtonWithCursor.class, ObjectsClearingZone.DELETE_ROUND_ELEMENTS);
            levelsEditor.getTab().createGui(StandardGuiNames.BACK, ButtonWithCursor.class, null);
            setTextForConsole("WHICH OBJECT MUST BE DELETED IN ZONE?");
            levelsEditor.getTab().recalculateHeight();
        }
    }

    @Override
    protected void ended(){

        super.ended();
        RoundLoader roundLoader = new RoundLoader(gameObjectData.getDataString());
        ArrayList <ObjectsClearingZone> clearingZones = roundLoader.getObjectsClearingZones();
        System.out.println("We have " + clearingZones.size() + " new clearing zones to be added");
        gameRound.getObjectsClearingZones().add(clearingZones.get(0));
    }




}
