package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.HUD.OnScreenText;
import com.mgdsstudio.blueberet.levelseditornew.submenu.AbstractSubmenu;
import com.mgdsstudio.blueberet.levelseditornew.submenu.ClearingZoneSubmenu;
import com.mgdsstudio.blueberet.levelseditornew.submenu.PreferencesSubmenu;
import com.mgdsstudio.blueberet.levelseditornew.submenu.SaveMapSubmenu;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.SaveMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.SelectingAction;
import com.mgdsstudio.engine.nesgui.Frame;
import com.mgdsstudio.engine.nesgui.FrameWithMoveableText;
import com.mgdsstudio.engine.nesgui.Tab;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class LevelsEditor extends AbstractLevelsEditor {

    private final ArrayList <String> unsavedData = new ArrayList<>();
    private MapZone mapZone;
    private OnScreenText fpsHud;
    private int countToMakeFirstStep;
    private long memory;
    private LowLevelListSubbutons actualStatement, nextStatement;
    private EditorMenu editorMenu;
    private EditorControl editorControl;
    private Cross cross;

    private AbstractSubmenu submenu;


    public LevelsEditor(GameMainController gameMainController, GameRound gameRound, GameCamera gameCamera) {
        this.gameMainController = gameMainController;
        initGridParameters();
        this.gameRound = gameRound;
        this.editorCamera = gameCamera;
        fpsHud = new OnScreenText(Program.engine.width/35, Program.engine.width/35, Program.engine.color(255,0,0), Program.engine.height/40);

        editorMenu = new EditorMenu(gameMainController.getEngine(), this);
        mapZone = new MapZone(editorMenu.getFrame());
        editorControl = new EditorControl(this, editorCamera, gameMainController);
        cross = new Cross(this);

        submenu = new PreferencesSubmenu(this,gameRound, editorControl, mapZone);
    }

    private void initGridParameters() {
        EditorPreferencesMaster master = new EditorPreferencesMaster(gameMainController.getEngine());
        master.updateData();
        Editor2D.gridSpacing = master.getGridStep();
        Editor2D.showGrid = master.isShowGrid();
    }

    void setNewStatementByName(String name) {
        nextStatement = LowLevelListSubbutons.getStatementByName(name);   
    }

    @Override
    public OnScreenConsole getOnScreenConsole() {
        return null;
    }

    public void update(GameCamera editorCamera){
        editorCamera.updateCenterPositionInEditor(getFrame(), gameMainController.getEngine());
        if (actualStatement != nextStatement) changeStatement();
        else {
            if (SelectingAction.selectedElements.size()>0) SingleGameElement.updateActualSelectionTintValue();
            if (editorCamera == null) {
                System.out.println("Camera in game is null and editor camera will be used");
                editorCamera = this.editorCamera;
                if (editorCamera == null) editorCamera = new GameCamera(new PVector(GameCamera.lastCameraPositionInEditor.x, GameCamera.lastCameraPositionInEditor.y), GameCamera.CAMERA_IN_EDITOR);
            }
            editorMenu.update();
            editorControl.update();
            submenu.update();
            if (Program.gameStatement == Program.LEVELS_EDITOR) makeStepAtStart();
            if (Program.engine.frameCount%20 == 0) memory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
        }

    }

    @Override
    public void addNewFigure(Figure figure) {

    }

    @Override
    public void addTextureToActualFigure(GraphicDataForFigures data) {

    }

    @Override
    public void addTextureToFirstFigure(GraphicDataForFigures data) {

    }

    @Override
    public void drawObjectsInGameWorld(GameCamera gameCamera) {

        mapZone.draw(gameCamera, mapZone);
        cross.draw(gameCamera, Program.objectsFrame);
        //levelsEditorControl.draw(gameCamera, this);
    }


    public void draw(GameCamera gameCamera){
        editorMenu.getGraphics().beginDraw();
        editorMenu.draw();
        //cross.draw(gameCamera, editorMenu.getGraphics());
        gameMainController.getEngine().pushStyle();
        gameMainController.getEngine().imageMode(PConstants.CORNER);
        gameMainController.getEngine().image(editorMenu.getGraphics(),0,0);
        gameMainController.getEngine().popStyle();
        editorMenu.getGraphics().endDraw();
    }


    private void makeStepAtStart(){
        if (countToMakeFirstStep < 100) {
            countToMakeFirstStep++;
        }
        else if (countToMakeFirstStep == 100){
            PhysicGameWorld.controller.step(0.000001f, 5, 8);
            countToMakeFirstStep++;
            System.out.println("Single step was made");
        }
    }

    @Override
    public void writeObjectsDataForLastObject() {

    }

    @Override
    public void writeObjectsDataToRoundFile() {
        if (unsavedData.size()>0){
            int number = 0;
            String path = ExternalRoundDataFileController.getPathToFileOnCache(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
            for (String data: unsavedData){
                SaveMaster.addDataToFile(path, data);
                System.out.println("Write data: " + data);
                number++;
            }
            System.out.println(number + " objects' data were written in data file" );
            unsavedData.clear();
        }
    }

    @Override
    public void drawBackground() {

    }

    @Override
    public void saveCameraPos() {

    }

    @Override
    public void recreateCamera(GameCamera gameCamera) {

    }

    @Override
    public MenuPanelController getMenuPanelController() {
        return null;
    }

    @Override
    public void onFlick(float x, float y, float px, float py, float v) {

    }

    @Override
    public void onPinch(float x, float y, float value) {

    }
    private void changeStatement() {
        actualStatement = nextStatement;
        if (actualStatement == LowLevelListSubbutons.GRID_PREFERENCES) submenu = new PreferencesSubmenu(this,gameRound, editorControl, mapZone);
        else if (actualStatement == LowLevelListSubbutons.NEW_CLEARING_ZONE) submenu = new ClearingZoneSubmenu(this,gameRound, editorControl, mapZone);
        else if (actualStatement == LowLevelListSubbutons.SAVE_MAP) submenu = new SaveMapSubmenu(this,gameRound, editorControl, mapZone);

        else {
            System.out.println("There are no submenus for statement: " + actualStatement);
        }
    }

    public Frame getFrame(){
        return editorMenu.getFrame();
    }

    public void closeSliders() {
        editorMenu.closeSliders();
    }

    public boolean isPointOnMapArea(float x, float y){
        if (x>editorMenu.getFrame().getLeftX() &&
                x<(editorMenu.getFrame().getLeftX()+editorMenu.getFrame().getWidth()) &&
                y>editorMenu.getFrame().getUpperY() &&
                y<(editorMenu.getFrame().getUpperY()+editorMenu.getFrame().getHeight())){
            return true;
        }
        else return false;
    }

    public MapZone getMapZone() {
        return mapZone;
    }


    public Tab getTab() {
        return editorMenu.getTab();

    }

    public FrameWithMoveableText getConsole() {
        return editorMenu.getConsole();
    }

    @Override
    public void setActualPressedKey(char ch){
        if (getTab() != null){
            getTab().setActualPressedChar(ch);
            System.out.println("New char is: " + ch);
        }
    }

    public void saveDataForActualSubmenu() {
        submenu.saveDataBySubmenuLeaving();
    }

    public Vec2 getCrossPos() {
        return cross.getActualCrossPos();
    }

    public void addUnsavedData(String dataString) {
        unsavedData.add(dataString);
        if (Program.OS == Program.DESKTOP){
            System.out.println("Unsaved data list: ");
            for (String string : unsavedData){
                System.out.println("***" + string + "***");
            }
        }
    }
}
