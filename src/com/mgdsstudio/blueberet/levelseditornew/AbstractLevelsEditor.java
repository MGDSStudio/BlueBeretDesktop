package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_MenuPanel;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.SaveMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.engine.nesgui.Frame;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class AbstractLevelsEditor {
    protected GameMainController gameMainController;
    protected GameRound gameRound;
    protected GameCamera editorCamera;

    public abstract OnScreenConsole getOnScreenConsole();


    public abstract void update(GameCamera editorCamera);

    public final GameRound getGameRound() {
        return gameRound;
    }

    public abstract void addNewFigure(Figure figure);

    public abstract void addTextureToActualFigure(GraphicDataForFigures data);

    public abstract void addTextureToFirstFigure(GraphicDataForFigures data);
    public abstract void drawObjectsInGameWorld(GameCamera gameCamera);

    public abstract void draw(GameCamera gameCamera);

    public static PVector getNearestCellCenterOnGrid(GameCamera gameCamera, PVector point){
        return null;
    }

    public static PVector getNearestPointOnGrid(GameCamera gameCamera, PVector point){
        return null;
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
        return true;
    }

    public static boolean isPointOnMapZoneAndNotOnAnyGUI_Element(Vec2 pos, androidGUI_MenuPanel menuPanel){
        return true;
    }


    public abstract void writeObjectsDataForLastObject();

    public abstract void writeObjectsDataToRoundFile();
    public abstract void drawBackground();



    public abstract void saveCameraPos();

    public abstract void recreateCamera(GameCamera gameCamera) ;

    public abstract MenuPanelController getMenuPanelController() ;

    public abstract void onFlick( float x, float y, float px, float py, float v);

    public abstract void onPinch(float x, float y, float value);


    public final GameCamera getEditorCamera(){
        return editorCamera;
    }

    public void setEditorCamera(GameCamera editorCamera) {
        this.editorCamera = editorCamera;
    }

    public abstract Frame getFrame();

    public GameMainController getGameMainController() {
        return gameMainController;
    }

    public void setActualPressedKey(char key) {

    }


}
