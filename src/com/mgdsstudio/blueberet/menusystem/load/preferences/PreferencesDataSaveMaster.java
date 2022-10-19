package com.mgdsstudio.blueberet.menusystem.load.preferences;

import com.mgdsstudio.blueberet.menusystem.gui.NES_ListButton;
import com.mgdsstudio.blueberet.menusystem.menu.OptionsMenu;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class PreferencesDataSaveMaster extends PreferencesDataController implements PreferencesDataConstants{
    private PApplet engine;
    private String soundValue = ON;
    private String musicValue = ON;
    private String performanceValue = HIGH;
    private String antialiasingValue = PIXEL_ART;
    private String cameraTargetValue = ON_CROSSHAIR;

    private String developerModeValue = OFF;

    private String lightsValue = ON;
    private String dPadValue = PreferencesDataConstants.LARGE;

    //Maybe next data must be added to an another class


    public PreferencesDataSaveMaster(PApplet engine) {
        data = new JSONArray();
        this.engine = engine;
    }

    public PreferencesDataSaveMaster(PApplet engine, OptionsMenu optionsMenu) {
        //Save values was set
        data = new JSONArray();
        this.engine = engine;
        saveDataFromOptionsMenu(optionsMenu);
    }

    private void saveDataFromOptionsMenu(OptionsMenu optionsMenu) {
        NES_ListButton element = (NES_ListButton) optionsMenu.getGuiByName(SOUND);
        soundValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(MUSIC);
        musicValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(PERFORMANCE);
        performanceValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(ANTI_ALIASING);
        antialiasingValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(CAMERA_TARGET);
        cameraTargetValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(LIGHTS);
        lightsValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(D_PAD);
        dPadValue = element.getSelectedString();
        element = (NES_ListButton) optionsMenu.getGuiByName(DEVELOPER_MODE);
        developerModeValue = element.getSelectedString();
    }

    public void saveData(){        
        saveDefaultPreferences();
        saveOnDisk();
    }

    private void saveDefaultPreferences() {
        JSONObject defaultData = new JSONObject();
        defaultData.setString(SOUND, soundValue);
        defaultData.setString(MUSIC, musicValue);
        defaultData.setString(PERFORMANCE, performanceValue);
        defaultData.setString(ANTI_ALIASING, antialiasingValue);
        defaultData.setString(CAMERA_TARGET, cameraTargetValue);
        defaultData.setString(LIGHTS, lightsValue);
        defaultData.setString(D_PAD, dPadValue);
        defaultData.setString(DEVELOPER_MODE, developerModeValue);

        this.data.setJSONObject(count, defaultData);
        count++;
    }

    protected void saveOnDisk(){
        engine.saveJSONArray(data, getPathToFile());
        //System.out.println("Data was saved to " + getPathToFile());
    }

}
