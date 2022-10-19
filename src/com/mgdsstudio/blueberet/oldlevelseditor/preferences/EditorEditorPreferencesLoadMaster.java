package com.mgdsstudio.blueberet.oldlevelseditor.preferences;


import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.menu.OptionsMenu;
import processing.core.PApplet;
import processing.data.JSONObject;

public class EditorEditorPreferencesLoadMaster extends EditorPreferencesDataController implements EditorPreferencesConstants {
    private PApplet engine;

    public EditorEditorPreferencesLoadMaster(PApplet engine) {
        //this.engine = engine;
        data = engine.loadJSONArray(getPathToFile());
    }

    public EditorEditorPreferencesLoadMaster() {
        System.out.println("I need to use an another library for preferences reader");
    }

    public void loadData(OptionsMenu optionsMenu){
        loadDefaultPreferences(optionsMenu);
    }



    public int getLastEditedLevel(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            int level = object.getInt(LAST_EDITED_LEVEL);
            return level;
        }
        System.out.println("In " + getPathToFile() + " there are no data about performance");
        return Program.actualRoundNumber;
    }

    private void loadDefaultPreferences(OptionsMenu optionsMenu) {
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            int lastEditedLevel = object.getInt(LAST_EDITED_LEVEL);
        }
    }



}
