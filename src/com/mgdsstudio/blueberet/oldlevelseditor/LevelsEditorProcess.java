package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_MenuPanel;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.OnScreenText;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.levelseditornew.AbstractLevelsEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.SelectingAction;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.SaveMaster;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.engine.nesgui.Frame;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class LevelsEditorProcess extends AbstractLevelsEditor {

    public MapZone mapZone;
    private androidGUI_ScrollableTab scrollableTab;
    //public GameCamera editorCamera;
    //GUI_Button exitButton;
    private OnScreenConsole onScreenConsole;
    public LevelsEditorControl levelsEditorControl;

    public androidGUI_MenuPanel menuPanel;
    private MenuPanelController menuPanelController;
    private Image backgroundImage;
    private NewObjectsCreator newObjectsCreator;
    private static byte loadingAttemts = 0;
    private byte countToMakeFirstStep;
    //private FPS_HUD fps_hud;
    private OnScreenText fpsHud;
    private long memory;

    public ArrayList<Point> pointsOnMap = new ArrayList<>();
    public ArrayList<Figure> figures = new ArrayList<>();

    //private byte updatingFrequency = 2;

    //ArrayList<Point> pointsOnMap = new ArrayList <Point>();
    //ArrayList<Figure>

    public LevelsEditorProcess(GameMainController gameMainController, GameRound gameRound, GameCamera gameCamera){
        System.out.println("Started to load levels editor process");
        this.gameMainController = gameMainController;
        HeadsUpDisplay.loadMainGraphicTileset();
        pointsOnMap = new ArrayList<>();
        figures = new ArrayList <Figure>();
        mapZone = new MapZone();
        this.gameRound = gameRound;
        int exitButtonWidth = (int)(Program.engine.width/12f);
        onScreenConsole = new OnScreenConsole(new Vec2(mapZone.getNullPosition().x, mapZone.getNullPosition().y+mapZone.zoneHeight + Editor2D.distanceToMapZoneBoard*0.5f), (int) mapZone.zoneWidth, (int)(Editor2D.distanceToMapZoneBoard*1.8f));
        menuPanel = new androidGUI_MenuPanel((byte)9);
        menuPanelController = new MenuPanelController(menuPanel, this);
        editorCamera = gameCamera;
        scrollableTab = new androidGUI_ScrollableTab(this);
        levelsEditorControl = new LevelsEditorControl(scrollableTab, this, gameCamera, gameMainController);
        backgroundImage = new Image(Program.getAbsolutePathToAssetsFolder("MenuBackground.png"));
        backgroundImage.getImage().resize(Program.engine.width, Program.engine.height);
        newObjectsCreator = new NewObjectsCreator();
        loadingAttemts++;

        PhysicGameWorld.makeAllBodiesInactive();
        fpsHud = new OnScreenText(Program.engine.width/35, Program.engine.width/35, Program.engine.color(255,0,0), Program.engine.height/40);
        deletePreviousSessionData();
    }

    private void deletePreviousSessionData() {
        SelectingAction.selectedElements.clear();
        Editor2D.setNewLocalStatement((byte) 0);
    }

    public OnScreenConsole getOnScreenConsole(){
        return onScreenConsole;
    }

    public void update(GameCamera editorCamera){
        if (SelectingAction.selectedElements.size()>0) SingleGameElement.updateActualSelectionTintValue();
        if (editorCamera == null) {
            System.out.println("Camera in game is null and editor camera will be used");
            editorCamera = this.editorCamera;
            if (editorCamera == null) editorCamera = new GameCamera(new PVector(GameCamera.lastCameraPositionInEditor.x, GameCamera.lastCameraPositionInEditor.y), GameCamera.CAMERA_IN_EDITOR);
        }
        if (newObjectsCreator.mustBeNewObjectCreated(this)){
            newObjectsCreator.createNewObject(this);
            levelsEditorControl = null;
            System.gc();
            levelsEditorControl = new LevelsEditorControl(scrollableTab, this, editorCamera, gameMainController);
            System.out.println("Game control was recreated! ");
        }
        //newObjectsCreator.update(this);
        levelsEditorControl.update(editorCamera, this);
        try {
            updateActualTextForConsole();
        }
        catch (Exception e){
            System.out.println("Text for the console can not be updated");
        }
        menuPanel.update();
        if (menuPanel.isSomeComboBoxShifted() && Program.isVirtualKeyboardOpened()) Program.openVirtualKeyboard(false);
        scrollableTab.update(this);
        if (Program.gameStatement == Program.LEVELS_EDITOR) makeStepAtStart();
        if (Program.engine.frameCount%20 == 0) memory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
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

    /*public GameRound getGameRound() {
        return gameRound;
    }*/

    public void addNewFigure(Figure figure){
        figures.add(figure);
    }

    /*
    public void addTextureToActualFigure(Image image, int [] vertexes){
        figures.get(figures.size()-1).setTexture(image, vertexes);
        System.out.println("New texture was loaded");
    }*/


/*
    public void addTextureToActualFigure(SingleImageDataForFigures data){
        figures.get(figures.size()-1).setTexture(data);
    }
    public void addTextureToFirstFigure(SingleImageDataForFigures data){
        figures.get(0).setTexture(data);
    }*/

    public void addTextureToActualFigure(GraphicDataForFigures data){
        figures.get(figures.size()-1).setGraphicData(data);
    }

    public void addTextureToFirstFigure(GraphicDataForFigures data){
        figures.get(0).setGraphicData(data);
    }






    private void updateActualTextForConsole(){
        if (levelsEditorControl.submenuAction != null) levelsEditorControl.submenuAction.updateTextForConsole(onScreenConsole, levelsEditorControl);
    }

    public void drawObjectsInGameWorld(GameCamera gameCamera){
        if (figures.size() > 0) {
            for (Figure figure : figures) {
                figure.draw(gameCamera, mapZone, null);
            }
        }
        if (pointsOnMap.size() > 0) {
            for (Point point : pointsOnMap) {
                point.draw(gameCamera, mapZone, null);
            }
        }
        levelsEditorControl.draw(gameCamera, this);
    }

    public void draw(GameCamera gameCamera){
        mapZone.mapGraphic.beginDraw();
        mapZone.mapGraphic.clear();
        if (!Point.ON_OBJECT_FRAME) {
            if (figures.size() > 0) {
                for (Figure figure : figures) {
                    figure.draw(gameCamera, mapZone, null);
                }
            }
            if (pointsOnMap.size() > 0) {
                for (Point point : pointsOnMap) {
                    point.draw(gameCamera, mapZone, null);
                }
            }

        }
        mapZone.mapGraphic.endDraw();
        //levelsEditorControl.draw(gameCamera, this);

        Program.engine.clip((int)((Program.engine.width/2)), (int)((Program.engine.width/2)), (int)(Program.engine.width-2*Editor2D.leftUpperCorner.x), (int)(Editor2D.rightLowerCorner.y-Editor2D.leftUpperCorner.y));
        Program.engine.imageMode(PConstants.CORNER);
        int mapGraphicWidth = (int)(Program.engine.width);

        Program.engine.image(mapZone.mapGraphic, 0,0, Program.objectsFrame.width, Program.objectsFrame.height,
                (int)((Program.objectsFrame.width/2)-  Program.OBJECT_FRAME_SCALE*Program.engine.width/2f/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)-  Program.OBJECT_FRAME_SCALE*Program.engine.height/2f/gameCamera.getScale()),
                (int)((Program.objectsFrame.width/2)+  Program.OBJECT_FRAME_SCALE*Program.engine.width/2f/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)+  Program.OBJECT_FRAME_SCALE*Program.engine.height/2f/gameCamera.getScale()));
        /*
        Program.engine.image(Program.objectsFrame, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height,
					(int) ((Program.objectsFrame.width / 2) - Program.OBJECT_FRAME_SCALE*Program.engine.width / 2 / gameCamera.getScale()),
					(int) ((Program.objectsFrame.height / 2) - Program.OBJECT_FRAME_SCALE*Program.engine.height / 2 / gameCamera.getScale()),
					(int) ((Program.objectsFrame.width / 2) + Program.OBJECT_FRAME_SCALE*Program.engine.width / 2 / gameCamera.getScale()),
					(int) ((Program.objectsFrame.height / 2) + Program.OBJECT_FRAME_SCALE*Program.engine.height / 2 / gameCamera.getScale()));


         */


        /*
        Program.engine.image(mapZone.mapGraphic, 0,0, mapGraphicWidth, Program.engine.height,
                (int)((Program.objectsFrame.width/2)-  Program.OBJECT_FRAME_SCALE*Program.engine.width/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)-  Program.OBJECT_FRAME_SCALE*Program.engine.height/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.width/2)+  Program.OBJECT_FRAME_SCALE*Program.engine.width/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)+  Program.OBJECT_FRAME_SCALE*Program.engine.height/2/gameCamera.getScale()));
         */



        /*
        Program.engine.image(mapZone.mapGraphic, 0,0, mapGraphicWidth, Program.engine.height,
                (int)((Program.objectsFrame.width/2)- Program.engine.width/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)- Program.engine.height/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.width/2)+ Program.engine.width/2/gameCamera.getScale()),
                (int)((Program.objectsFrame.height/2)+ Program.engine.height/2/gameCamera.getScale()));
         */


        Program.engine.imageMode(PConstants.CENTER);

        Program.engine.noClip();
        onScreenConsole.draw();
        scrollableTab.draw();
        menuPanel.draw();
        if (Program.debug) fpsHud.draw("FPS: " +(int)Program.engine.frameRate+ "; RAM: " + memory + " mb" );
        //showActiveBodies();
        //fps_hud.showFrameRateWithRenderer(Program.graphicRenderer);
    }

    private void showActiveBodies() {
        if (Program.engine.frameCount % 200 == 0) {
            float active = 0;
            int all = PhysicGameWorld.controller.world.getBodyCount();
            for (Body b = PhysicGameWorld.controller.world.getBodyList(); b != null; b = b.getNext()) {
                if (b.isActive()) {
                    active++;
                }
            }
            System.out.println("Bodies: " + all + "; Active: " + active + ". = " + (active / all)*100f + "%");
        }
    }

    private void clearMapZone(){


    }

    public static PVector getNearestCellCenterOnGrid(GameCamera gameCamera, PVector point){
        if (isPointOnMapZone(point)){
            float distanceToCenterLineX = (point.x - Program.engine.width/2);
            float distanceToCenterLineY = (point.y - Program.engine.height/2);
            point.x = ((gameCamera.getActualPosition().x + distanceToCenterLineX / gameCamera.getScale()));
            point.y = ((gameCamera.getActualPosition().y + distanceToCenterLineY / gameCamera.getScale()));

            /*
            point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.engine.width) / 2) / gameCamera.getScale())));
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.engine.height) / 2) / gameCamera.getScale())));
            */
            float positivDeltaX = PApplet.abs((point.x)%Editor2D.gridSpacing);
            float positivDeltaY = PApplet.abs((point.y)%Editor2D.gridSpacing);
            float negativDeltaX = PApplet.abs(Editor2D.gridSpacing-positivDeltaX);
            float negativDeltaY = PApplet.abs(Editor2D.gridSpacing-positivDeltaY);

            if (positivDeltaX>negativDeltaX) point.x+=(-positivDeltaX+(Editor2D.gridSpacing/2));
            else  point.x+=(negativDeltaX-(Editor2D.gridSpacing/2));
            if (positivDeltaY>negativDeltaY) point.y+=(-positivDeltaY+(Editor2D.gridSpacing/2));
            else point.y+=(negativDeltaY-(Editor2D.gridSpacing/2));

            return point;
        }
        else {
            Program.engine.println("Point is not on the map zone!");
            return null;
        }
    }

    public static PVector getNearestPointOnGrid(GameCamera gameCamera, PVector point){
        if (isPointOnMapZone(point)){
            float distanceToCenterLineX = (point.x - Program.engine.width/2);
            float distanceToCenterLineY = (point.y - Program.engine.height/2);
            point.x = ((gameCamera.getActualPosition().x + distanceToCenterLineX / gameCamera.getScale()));
            point.y = ((gameCamera.getActualPosition().y + distanceToCenterLineY / gameCamera.getScale()));
            /*
            float additionalX = -(Program.objectsFrame.width-Program.engine.width)*gameCamera.getScale()/2f;
            float additionalY = -(Program.objectsFrame.height-Program.engine.height)*gameCamera.getScale()/2f;
            System.out.println("Additional X : " + additionalX + "Y: " + additionalY);

            point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.engine.width) / 2f) / gameCamera.getScale())));
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.engine.height) / 2f) / gameCamera.getScale())));
*/
/*
            point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.objectsFrame.width) / 2f) / gameCamera.getScale())))+additionalX;
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.objectsFrame.height) / 2f) / gameCamera.getScale())))+additionalY;
            */
            /*
            point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.engine.width) / 2) / gameCamera.getScale())));
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.engine.height) / 2) / gameCamera.getScale())));
             */


            /*
             point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.engine.width) / 2) / gameCamera.getScale())));
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.engine.height) / 2) / gameCamera.getScale())));
             */



            if (Program.engine.frameCount %10 == 0) System.out.println("This function must be adjusted to object frame dimensions");
            /*
            point.x = ((gameCamera.getActualPosition().x + ((point.x - (Program.engine.width) / 2) / gameCamera.getScale())));
            point.y = ((gameCamera.getActualPosition().y + ((point.y - (Program.engine.height) / 2) / gameCamera.getScale())));

             */


            float positivDeltaX = point.x%Editor2D.gridSpacing;
            float positivDeltaY = point.y%Editor2D.gridSpacing;
            float negativDeltaX = Editor2D.gridSpacing-positivDeltaX;
            float negativDeltaY = Editor2D.gridSpacing-positivDeltaY;

            if (positivDeltaX>negativDeltaX) point.x+=negativDeltaX;
            else point.x-=positivDeltaX   ;
            if (positivDeltaY>negativDeltaY) point.y+=negativDeltaY;
            else point.y-=positivDeltaY ;

            return point;
        }
        else {
            Program.engine.println("Point is not on the map zone!");
            return null;
        }
    }

    public static PVector getPointInWorldPosition(GameCamera gameCamera, PVector point){
        if (isPointOnMapZone(point)){
            float distanceToCenterLineX = (point.x - Program.engine.width/2);
            float distanceToCenterLineY = (point.y - Program.engine.height/2);
            point.x = ((gameCamera.getActualPosition().x + distanceToCenterLineX / gameCamera.getScale()));
            point.y = ((gameCamera.getActualPosition().y + distanceToCenterLineY / gameCamera.getScale()));
            if (Program.engine.frameCount % 115 == 0 ) System.out.println("Something is wrong in this function by camera scale 0.5f");
            return point;
        }
        else {
            Program.engine.println("Point is not on the map zone!");
            return null;
        }
    }

    public static boolean isPointOnMapZone(float x, float y){
        if (x>Editor2D.leftUpperCorner.x &&
                x<Editor2D.rightLowerCorner.x &&
                y>Editor2D.leftUpperCorner.y &&
                y<Editor2D.rightLowerCorner.y){
            return true;
        }
        else return false;
    }

    public static boolean isPointOnMapZone(PVector pos){
        if (pos.x>Editor2D.leftUpperCorner.x &&
                pos.x<Editor2D.rightLowerCorner.x &&
                pos.y>Editor2D.leftUpperCorner.y &&
                pos.y<Editor2D.rightLowerCorner.y){
            return true;
        }
        else return false;
    }

    public static boolean isPointOnMapZoneAndNotOnAnyGUI_Element(Vec2 pos, androidGUI_MenuPanel menuPanel){
        if (pos.x>Editor2D.leftUpperCorner.x && pos.x<Editor2D.rightLowerCorner.x && pos.y>Editor2D.leftUpperCorner.y && pos.y<Editor2D.rightLowerCorner.y){
            if (!menuPanel.isSomeComboBoxShifted()) return true;
            else {
                if (menuPanel.getShiftedComboBox().isMouseOnElement(pos, PApplet.CORNER)){
                    System.out.println("Point is on the menu element");
                    return false;
                }
                else return true;
            }
        }
        else return false;
    }


    public void writeObjectsDataForLastObject(){
        if (Editor2D.getNewObjectsData().size()>0){
            int number = 0;
           // for (GameObjectDataForStoreInEditor data: Editor2D.getNewObjectsData()){
                GameObjectDataForStoreInEditor data = Editor2D.getNewObjectsData().get(Editor2D.getNewObjectsData().size()-1);
                String path = ExternalRoundDataFileController.getPathToFileOnCache(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
                SaveMaster.addDataToFile(path, data.getDataString());
                number++;
            //}
            System.out.println(number + " objects' data were written in data file under path: " + path);
            Editor2D.getNewObjectsData().remove(data);
        }
    }

    public void writeObjectsDataToRoundFile(){
        if (Editor2D.getNewObjectsData().size()>0){
            int number = 0;
            for (GameObjectDataForStoreInEditor data: Editor2D.getNewObjectsData()){
                if (data != null && !data.getDataString().contains(null)) {
                    String path = ExternalRoundDataFileController.getPathToFileOnCache(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
                    SaveMaster.addDataToFile(path, data.getDataString());
                    System.out.println("Write data: " + data.getDataString());
                    number++;
                }
                else System.out.println("Data can not be saved. Data is null: ");
            }
            System.out.println(number + " objects' data were written in data file" );
            Editor2D.getNewObjectsData().clear();
        }

    }
    public void drawBackground() {
        try {
            Program.engine.background(backgroundImage.getImage());
        }
        catch (Exception e){
            System.out.println("This background has not right dimensions to be drawn on to the screen!; " + e);
        }
    }


    public void saveCameraPos() {
        editorCamera.saveCameraPos();
    }

    @Override
    protected void finalize(){
        try {
            mapZone = null;
            gameRound = null;
            editorCamera = null;
            onScreenConsole = null;
            System.out.println("Level editing process was deleted");
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void recreateCamera(GameCamera gameCamera) {
        editorCamera = new GameCamera(new PVector(GameCamera.lastCameraPositionInEditor.x, GameCamera.lastCameraPositionInEditor.y), GameCamera.CAMERA_IN_EDITOR);
    }

    public MenuPanelController getMenuPanelController() {
        return menuPanelController;
    }



    public void onFlick( float x, float y, float px, float py, float v){
        levelsEditorControl.onFlick(x, y, px, py, v);
    }

    public void onPinch(float x, float y, float value){
        levelsEditorControl.onPinch(x, y, value);
    }

    @Override
    public Frame getFrame() {
        return null;
    }
}


