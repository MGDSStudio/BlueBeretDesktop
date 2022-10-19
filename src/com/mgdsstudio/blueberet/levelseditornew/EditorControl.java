package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.TouchScreenActionBuffer;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.ScrollableTabController;
import com.mgdsstudio.blueberet.onscreenactions.OnPinchAction;
import com.mgdsstudio.blueberet.onscreenactions.OnScreenActionType;
import processing.core.PVector;

public class EditorControl implements IEditorControl{
    private final GameMainController gameMainController;
    private LevelsEditor levelsEditorProcess;
    private GameCamera editorCamera;




    public EditorControl(LevelsEditor levelsEditorProcess, GameCamera editorCamera, GameMainController gameMainController){
        this.gameMainController = gameMainController;
        this.editorCamera = editorCamera;
        this.levelsEditorProcess = levelsEditorProcess;

    }

    public void onPinch(float x, float y, float value){
        if ( ZONE_MANIPULATIONS_BY_DIRECT_CONTROL){
            System.out.println("Player zooms");
            OnPinchAction action = (OnPinchAction) TouchScreenActionBuffer.getAction();
            Program.openVirtualKeyboard(false);
        }
    }

    void update(){
        updateCameraMovement();
    }

    private void updateCameraMovement() {
        if (Program.OS == Program.DESKTOP) updateCameraMovementOnWindows();
        else updateCameraMovementOnAndroid();
    }

    private void updateCameraMovementOnWindows(){
        boolean noScalling = true;
        if (Program.OS == Program.DESKTOP && Program.getMouseWheelRotation() != 0) {
            scaleInDesktopMode();
            noScalling = false;
        }
        if (noScalling){
            moveZoneInWindowsStyle();
        }
    }

    private void scaleInDesktopMode(){
        scaling(Program.getMouseWheelRotation());
        levelsEditorProcess.closeSliders();
    }

    private void moveZoneInWindowsStyle(){
        if (levelsEditorProcess.isPointOnMapArea(Program.engine.mouseX, Program.engine.mouseY)){
            if (Editor2D.GAME_ZONE_CAN_BE_MOVED){
                if (Program.engine.mousePressed == true && Editor2D.prevMousePressedStatement == true){
                    if (Editor2D.wasMouseMoved(editorCamera.getScale())){ //was *
                        if (Program.engine.abs(Program.engine.dist(Program.engine.mouseX, Program.engine.mouseY, Program.engine.pmouseX, Program.engine.pmouseY))>(Editor2D.maxMovementProOneFrameForStaticMouse*editorCamera.getScale())){
                            if (!editorCamera.MOVEMENT_WITH_ACCELERATE){
                                editorCamera.translate(new PVector(-(Program.engine.mouseX - Program.engine.pmouseX) / editorCamera.getScale(), -(Program.engine.mouseY - Program.engine.pmouseY) / editorCamera.getScale()));
                                levelsEditorProcess.closeSliders();
                            }
                            else {
                                editorCamera.addMovementVector(Program.engine.mouseX, Program.engine.mouseY, Program.engine.pmouseX, Program.engine.pmouseY);
                            }
                            if (Program.isVirtualKeyboardOpened()) Program.openVirtualKeyboard(false);

                        }
                    }
                }
            }
        }
    }

    private void updateCameraMovementOnAndroid(){
        if (TouchScreenActionBuffer.getActionType() == OnScreenActionType.PINCH){
            OnPinchAction action = (OnPinchAction)TouchScreenActionBuffer.getAction();
            if (levelsEditorProcess.isPointOnMapArea(action.getCenter().x, action.getCenter().y)) {
                float scaleValue = action.getValue()*0.005f;    //System.out.print("Scale was: " + gameCamera.getScale() +"; Value: " + action.getValue() + " and pos : " + action.getPosition().x + "x" + action.getPosition());
                editorCamera.updateScaleForLevelseditor(scaleValue);
            }
            if (Program.isVirtualKeyboardOpened()) Program.openVirtualKeyboard(false);
        }
        else {
            moveZoneInWindowsStyle();
            TouchScreenActionBuffer.clearBuffer();
        }
        TouchScreenActionBuffer.clearBuffer();
    }
    private void scaling(byte direction){
        if (direction == Program.BACKWARD_ROTATION){
            editorCamera.changeScale(Editor2D.SCALLING_DOWN);
        }
        else if (direction == Program.FORWARD_ROTATION){
            editorCamera.changeScale(Editor2D.SCALLING_UP);
        }
        Program.setMouseWheelRotation((byte)0);
    }
}
