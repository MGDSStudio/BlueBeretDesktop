package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class ObjectOnMapSelectingController extends ObjectOnMapEditingController{

    public ObjectOnMapSelectingController(){
        allignedWithGrid = false;
    }


    @Override
    public void update(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        updateObjectOnMapZoneSelecting(gameCamera, levelsEditorProcess);
    }

    protected void updateObjectOnMapZoneSelecting(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (!Program.engine.mousePressed) {
            if (timer != null) {
                timer.stop();
                timer = null;
                System.out.println("Timer was switched off 1");
            }
        }
        else {
            if (LevelsEditorProcess.isPointOnMapZoneAndNotOnAnyGUI_Element(new Vec2(Program.engine.mouseX, Program.engine.mouseY), levelsEditorProcess.menuPanel)){
                if (Program.engine.mousePressed == true && !Editor2D.prevMousePressedStatement){
                    if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true){
                        if (timer == null) {
                            timer = new Timer(Editor2D.TIME_TO_ADD_NEW_POINT);
                            if (addingCross == null) addingCross = new AddingCross(AddingCross.ARROW);
                            else addingCross.recreate(AddingCross.ARROW);
                        }
                        else if (!timer.isSwitchedOff()) {
                            timer.setNewTimer(Editor2D.TIME_TO_ADD_NEW_POINT);
                        }
                    }
                }
                if (Program.engine.mousePressed && timer != null){
                    if (Program.debug) {
                        int x = (int)(gameCamera.getActualPosition().x+((Program.engine.mouseX-(Program.engine.width)/2)/gameCamera.getScale()));
                        int y = (int)((gameCamera.getActualPosition().y+((Program.engine.mouseY-(Program.engine.height)/2)/gameCamera.getScale())));
                        System.out.println("Actual mouse in world pos:" + x + "x" +y);
                    }
                    if (Editor2D.wasMouseMoved()) switchOffTimer();
                    else if (timer.isTime() ){
                        if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true){
                            if (!newObjectCanBeAdded) newObjectCanBeAdded = true;
                        }
                        else{
                            timer.stop();
                            timer = null;
                            System.out.println("Timer was switched off 2");
                            Program.engine.println("Mouse floated from the map zone");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        drawHands(gameCamera, levelsEditorProcess);
    }

    private void drawHands(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (timer!=null){
            if (!timer.isTime()){
                if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true){
                    PVector nearestPoint = levelsEditorProcess.getNearestPointOnGrid(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY));
                    if (!allignedWithGrid) nearestPoint = levelsEditorProcess.getPointInWorldPosition(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY));
                    addingCross.drawToVertex(Program.objectsFrame,(byte)(timer.getRestTime()*100f/(timer.getInstalledTime())), nearestPoint, gameCamera);
                }
            }
        }
    }

    public void endSelection() {
        newObjectCanBeAdded = false;
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
    }
}
