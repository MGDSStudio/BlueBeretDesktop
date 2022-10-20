package com.mgdsstudio.blueberet.levelseditornew;


import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class EditorPreferencesMaster {
    //private final String FILE_NAME = "EditorPreferences.json";
    private final String FILE_NAME = "EditorPreferences.json";
    private JSONObject data;
    private final String path;
    private final PApplet engine;

    private int gridStep = 80;
    private boolean showGrid = false;

    public EditorPreferencesMaster(PApplet engine) {
        this.engine = engine;
        if (Program.OS == Program.DESKTOP){
            path = Program.getAbsolutePathToAssetsFolder(FILE_NAME);
        }
        else path = Program.iEngine.getPathInCache(FILE_NAME);
        if (Program.debug) System.out.println("Path to preferences data file: " + path);
    }

    public void updateData(){
        File file = new File(path);
        if (!file.exists()){
            createFile(file);
        }
        updateValues();
        applyValues();
    }

    private void applyValues() {
        Editor2D.gridSpacing = gridStep;
        Editor2D.showGrid = showGrid;
        System.out.println("Grid step set on: " + gridStep + " from the data file");
        System.out.println("Grid is visible: " + showGrid + " from the data file");
    }

    private void updateValues() {
        JSONObject object = Program.engine.loadJSONObject(path);
        gridStep = object.getInt(Constants.GRID_STEP);
        showGrid = object.getBoolean(Constants.SHOW_GRID);

    }

    private void createFile(File destination) {
        File source;
        if (Program.OS == Program.DESKTOP){
            loadDefaultData();
        }
        else {
            source = new File(Program.getAbsolutePathToAssetsFolder(FILE_NAME));
            if (source.exists()) {
                Program.iEngine.copy(source, destination);
            }
            else {
                String[] data = engine.loadStrings(FILE_NAME);
                engine.saveStrings( path, data);
            }
        }
    }

    private void loadDefaultData() {

    }

    public void saveChanged() {
        JSONObject object = Program.engine.loadJSONObject(path);
        object.setBoolean(Constants.SHOW_GRID, showGrid);
        object.setInt(Constants.GRID_STEP, gridStep);
        object.save(new File(path), null);
    }


    private interface Constants{
        String GRID_STEP = "GRID STEP";
        String SHOW_GRID = "SHOW GRID";
    }

    public int getGridStep() {
        return gridStep;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setGridStep(int gridStep) {
        this.gridStep = gridStep;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }
}
