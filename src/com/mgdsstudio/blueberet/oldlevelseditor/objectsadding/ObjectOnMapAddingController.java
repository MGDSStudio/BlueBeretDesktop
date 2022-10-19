package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.SaveMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;
import processing.core.PVector;

public class ObjectOnMapAddingController extends ObjectOnMapEditingController{
    public static final boolean TO_VERTEX = false;
    public static final boolean TO_CELL_CENTER = true;
    protected boolean magnetingTo = TO_VERTEX;
    protected Vec2 lastAddedPointPosition;
    private PVector mutNearestPoint = new PVector();
    private final PVector mutMousePos = new PVector(0,0);

    public boolean getStatement(){
        return statement;
    }

    public void setStatement(boolean flag){
        statement = flag;
    }

    @Override
    public void update(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        updateObjectOnMapZoneAdding(gameCamera, levelsEditorProcess);
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (DRAW_ON_OBJECTS_FRAME){
            drawCross(gameCamera, levelsEditorProcess, Program.objectsFrame);
        }
        else drawCross(gameCamera, levelsEditorProcess, levelsEditorProcess.mapZone.mapGraphic);
    }


/*
    private void drawCrossOnObjectsFrame(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {
        if (timer!=null){
            if (!timer.isTime()){
                if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true) {
                    //PVector nearestPoint;
                    mutMousePos.x = Program.engine.mouseX;
                    mutMousePos.y = Program.engine.mouseY;
                    if (!allignedWithGrid){
                        mutNearestPoint = levelsEditorProcess.getPointInWorldPosition(gameCamera, mutMousePos);
                        addingCross.drawToVertex(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                    }
                    else {
                        if (magnetingTo == TO_VERTEX) {

                            mutNearestPoint = levelsEditorProcess.getNearestPointOnGrid(gameCamera, mutMousePos);
                            System.out.println("Try to find point in the world on grid" + (int)mutNearestPoint.x + "x" + (int )mutNearestPoint.y);
                            addingCross.drawToVertex(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                        }
                        else if (magnetingTo == TO_CELL_CENTER) {
                            mutNearestPoint = levelsEditorProcess.getNearestCellCenterOnGrid(gameCamera, mutMousePos);
                            addingCross.drawToCenter(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);

                        }
                    }
                }
            }
        }
        else {
            //System.out.println("Timer for draw cross is null");
        }
    }*/

    public void endAdding(){
        if (timer != null){
            timer.stop();
            timer = null;
        }
        newObjectCanBeAdded = false;
    }

    public Vec2 getActualCrossPosition(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        Vec2 pos = new Vec2(levelsEditorProcess.getPointInWorldPosition(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY)).x, levelsEditorProcess.getPointInWorldPosition(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY)).y);
        return pos;
    }

    private void drawCross(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, PGraphics graphics){
        if (timer!=null){
            if (!timer.isTime()){
                if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true) {
                    //PVector nearestPoint;
                    mutMousePos.x = Program.engine.mouseX;
                    mutMousePos.y = Program.engine.mouseY;
                    if (!allignedWithGrid){
                        mutNearestPoint = levelsEditorProcess.getPointInWorldPosition(gameCamera, mutMousePos);
                        addingCross.drawToVertex(graphics, (byte) (timer.getRestTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                    }
                    else {
                        if (magnetingTo == TO_VERTEX) {
                            //mutNearestPoint = levelsEditorProcess.getPointInWorldPosition(gameCamera, mutMousePos);
                            mutNearestPoint = levelsEditorProcess.getNearestPointOnGrid(gameCamera, mutMousePos);
                            System.out.println("Try to find point in the world on grid: " + (int)mutNearestPoint.x + "x" + (int )mutNearestPoint.y);
                            addingCross.drawToVertex(graphics, (byte) (timer.getRestTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                        }
                        else if (magnetingTo == TO_CELL_CENTER) {
                            mutNearestPoint = levelsEditorProcess.getNearestCellCenterOnGrid(gameCamera, mutMousePos);
                            addingCross.drawToCenter(graphics, (byte) (timer.getRestTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);

                        }
                    }
                }
            }
        }
        else {
            //System.out.println("Timer for draw cross is null");
        }
    }

    /*
    private void drawCrossOld(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (timer!=null){
            if (!timer.isTime()){
                if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true) {
                    //PVector nearestPoint;
                    mutMousePos.x = Program.engine.mouseX;
                    mutMousePos.y = Program.engine.mouseY;
                    if (!allignedWithGrid){
                        mutNearestPoint = levelsEditorProcess.getPointInWorldPosition(gameCamera, mutMousePos);
                        addingCross.drawToVertex(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                    }
                    else {
                        if (magnetingTo == TO_VERTEX) {

                            mutNearestPoint = levelsEditorProcess.getNearestPointOnGrid(gameCamera, mutMousePos);
                            System.out.println("Try to find point in the world on grid" + (int)mutNearestPoint.x + "x" + (int )mutNearestPoint.y);
                            addingCross.drawToVertex(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);
                        }
                        else if (magnetingTo == TO_CELL_CENTER) {
                            mutNearestPoint = levelsEditorProcess.getNearestCellCenterOnGrid(gameCamera, mutMousePos);
                            addingCross.drawToCenter(levelsEditorProcess.mapZone.mapGraphic, (byte) (timer.restTime() * 100f / (timer.getInstalledTime())), mutNearestPoint, gameCamera);

                        }
                    }
                }
            }
        }
        else {
            //System.out.println("Timer for draw cross is null");
        }
    }*/

    public boolean isCrossDrawing(){
        if (timer != null){
            if (!timer.isTime()){
                if (!timer.isTime()){
                    return true;
                }
            }
        }
        return false;
    }

    protected void updateObjectOnMapZoneAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
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
                    if (Editor2D.wasMouseMoved()) switchOffTimer();
                    else if (timer.isTime() ){
                        if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true){
                            if (!newObjectCanBeAdded) {
                                newObjectCanBeAdded = true;
                            }
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

    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject){
    }



    protected void objectMustBeAdded(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
    }

    protected void objectMustBeAdded(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject){
    }

    protected void addTextDataToRoundFile(String textData, boolean replaceExisting){
        String path = ExternalRoundDataFileController.getPathToFileOnCache(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
        if (replaceExisting){
            String stringToBeDeleted = getClassNameFromString(textData);
            LoadingMaster loadingMaster = new LoadingMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
            deleteAnotherStringFromFile(loadingMaster, stringToBeDeleted);
            SaveMaster.saveDataToFile(path, textData);
        }
        SaveMaster.addDataToFile(path, textData);
        System.out.println("Text data was added: " + textData);
    }

    private void deleteAnotherStringFromFile(LoadingMaster loadingMaster, String stringToBeDeleted){
        String [] fileData = loadingMaster.getFileData();
        byte stringsInNewFile = 0;
        for (int i = 0; i < fileData.length; i++){
            if (fileData[i].contains(stringToBeDeleted)){
                //System.out.println("It founds on " + i);
            }
            else {
                stringsInNewFile++;
            }
        }
        System.out.println("Strings in new file: " + stringsInNewFile);
        System.out.println("Strings in old file: " + fileData.length);

        String [] newFileData = new String[stringsInNewFile];
        int actualElementNumber = 0;
        for (int i = 0; i < fileData.length; i++){
            if (fileData[i].contains(stringToBeDeleted)){
                //System.out.println("It founds on " + i);
            }
            else {
                newFileData[actualElementNumber] = fileData[i];
                actualElementNumber++;
            }
        }


    }

    private String getClassNameFromString(String textData){
        String className = textData.substring(0, textData.indexOf(" "));
        return className;
    }

    public float getTimerRelativeTime(int max) {
        if (timer != null){
            //max = Editor2D.TIME_TO_ADD_NEW_POINT;
            //min = 0;
            float relativeTime = timer.getRestTime()*max/Editor2D.TIME_TO_ADD_NEW_POINT;
            return relativeTime;
        }
        else return -1f;
    }
}
