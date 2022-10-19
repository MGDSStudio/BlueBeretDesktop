package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.yandex.metrica.impl.ob.Ed;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;

public class EditorPreferencesMaster {
    private final String FILE_NAME = "Editor preferences.json";
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
    }

    private void updateValues() {
        JSONObject object = Program.engine.loadJSONObject(path);
        //JSONObject object = data.getJSONObject(0);
        gridStep = object.getInt(Constants.GRID_STEP);
        showGrid = object.getBoolean(Constants.SHOW_GRID);

    }

    private void createFile(File destination) {
        File source;
        if (Program.OS == Program.DESKTOP){
            loadDefaultData();
        }
        else {
            source = new File(FILE_NAME);
            Program.iEngine.copy(source, destination);
        }
    }

    private void loadDefaultData() {

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
}
