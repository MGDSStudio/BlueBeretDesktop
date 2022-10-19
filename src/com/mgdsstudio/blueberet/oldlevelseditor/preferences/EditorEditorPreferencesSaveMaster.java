package com.mgdsstudio.blueberet.oldlevelseditor.preferences;

import com.mgdsstudio.blueberet.menusystem.menu.OptionsMenu;
import processing.core.PApplet;
import processing.data.JSONArray;

public class EditorEditorPreferencesSaveMaster extends EditorPreferencesDataController implements EditorPreferencesConstants {
    private PApplet engine;

    //Maybe next data must be added to an another class



    public EditorEditorPreferencesSaveMaster(PApplet engine) {
        data = new JSONArray();
        this.engine = engine;
    }

    public EditorEditorPreferencesSaveMaster(PApplet engine, OptionsMenu optionsMenu) {
        //Save values was set
        data = new JSONArray();
        this.engine = engine;
        saveDataFromOptionsMenu(optionsMenu);
    }

    private void saveDataFromOptionsMenu(OptionsMenu optionsMenu) {

        /*NES_ListButton element = (NES_ListButton) optionsMenu.getGuiByName(SOUND);
        soundValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(MUSIC);
        musicValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(PERFORMANCE);
        performanceValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(ANTI_ALIASING);
        antialiasingValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(CAMERA_TARGET);
        cameraTargetValue = element.getSelectedString();
*/
    }

    public void saveData(){        
        saveDefaultPreferences();
        saveOnDisk();
    }

    private void saveDefaultPreferences() {
        /*JSONObject defaultData = new JSONObject();
        defaultData.setString(SOUND, soundValue);
        defaultData.setString(MUSIC, musicValue);
        defaultData.setString(PERFORMANCE, performanceValue);
        defaultData.setString(ANTI_ALIASING, antialiasingValue);
        defaultData.setString(CAMERA_TARGET, cameraTargetValue);
        this.data.setJSONObject(count, defaultData);
        count++;*/
    }

    protected void saveOnDisk(){
        engine.saveJSONArray(data, getPathToFile());
        System.out.println("Data was saved to " + getPathToFile());
    }

}
