package com.mgdsstudio.blueberet.menusystem.load.preferences;


import com.mgdsstudio.blueberet.menusystem.gui.NES_ListButton;
import com.mgdsstudio.blueberet.menusystem.menu.OptionsMenu;

import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PreferencesDataLoadMaster extends PreferencesDataController implements PreferencesDataConstants{

    public PreferencesDataLoadMaster(PApplet engine) {
        data = engine.loadJSONArray(getPathToFile());
    }

    public PreferencesDataLoadMaster() {
        System.out.println("I need to use an another library for preferences reader");
    }



    public void loadData(OptionsMenu optionsMenu){
        loadDefaultPreferences(optionsMenu);
    }
    
    public boolean getWithSound(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String sound = object.getString(SOUND);
            if (sound == ON) return  true;
            else return false;
        }
        System.out.println("In " + getPathToFile() + " there are no data about sound");
        return true;
    }

    public boolean getWithMusic(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String music = object.getString(MUSIC);
            if (music == ON) return  true;
            else return false;
        }
        System.out.println("In " + getPathToFile() + " there are no data about music");
        return true;
    }




    public String getPerformance(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String performance = object.getString(PERFORMANCE);
            return performance;
        }
        System.out.println("In " + getPathToFile() + " there are no data about performance");
        return MEDIUM;
    }

    public int getAntiAliasing(){
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject object = data.getJSONObject(i);
                String antiAliasing = object.getString(ANTI_ALIASING);
                if (antiAliasing == PIXEL_ART || antiAliasing.equals(PIXEL_ART)) return 0;
                else if (antiAliasing == x2 || antiAliasing.equals(x2)) return 2;
                else if (antiAliasing == x4 || antiAliasing.equals(x4)) return 4;
                else if (antiAliasing == x8 || antiAliasing.equals(x8)) return 8;
                else {
                    System.out.println("In " + getPathToFile() + " there are no data about anti aliasing");
                    return 0;
                }
            }
            System.out.println("In " + getPathToFile() + " there are no data about anti aliasing");
        }
        return 0;
    }

    public String getCameraTarget(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String cameraTarget = object.getString(CAMERA_TARGET);
            return cameraTarget;
        }
        System.out.println("In " + getPathToFile() + " there are no data about camera target");
        return ON_CROSSHAIR;
    }

    public String getDPad(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String dPad = object.getString(D_PAD);
            return dPad;
        }
        System.out.println("In " + getPathToFile() + " there are no data about d-pad");
        return LARGE;
    }

    public String getDeveloperMode(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String dPad = object.getString(DEVELOPER_MODE);
            return dPad;
        }
        System.out.println("In " + getPathToFile() + " there are no data about developer mode");
        return OFF;
    }

    private void loadDefaultPreferences(OptionsMenu optionsMenu) {
        //for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(0);
            String sound = object.getString(SOUND);
            String music = object.getString(MUSIC);
            String performance = object.getString(PERFORMANCE);
            String antiAliasing = object.getString(ANTI_ALIASING);
            String cameraTarget = object.getString(CAMERA_TARGET);
            String lights = object.getString(LIGHTS);
            String dPad = object.getString(D_PAD);
            String developerMode = object.getString(DEVELOPER_MODE);
            if (sound != null) {
                try {
                    NES_ListButton element = (NES_ListButton) optionsMenu.getGuiByName(SOUND);
                    element.setSelectedString(sound);
                    element = (NES_ListButton) optionsMenu.getGuiByName(MUSIC);
                    element.setSelectedString(music);
                    element = (NES_ListButton) optionsMenu.getGuiByName(PERFORMANCE);
                    element.setSelectedString(performance);
                    element = (NES_ListButton) optionsMenu.getGuiByName(ANTI_ALIASING);
                    element.setSelectedString(antiAliasing);
                    element = (NES_ListButton) optionsMenu.getGuiByName(CAMERA_TARGET);
                    element.setSelectedString(cameraTarget);
                    element = (NES_ListButton) optionsMenu.getGuiByName(LIGHTS);
                    element.setSelectedString(lights);
                    element = (NES_ListButton) optionsMenu.getGuiByName(D_PAD);
                    if (element!=null && dPad!=null) element.setSelectedString(dPad);
                    else System.out.println("No data about D-pad scale");
                    element = (NES_ListButton) optionsMenu.getGuiByName(DEVELOPER_MODE);
                    element.setSelectedString(developerMode);
                }
                catch (Exception e){
                    System.out.println("This element can not be cast " + e);
                }
            }
        //}
    }

    private final ArrayList <String> getDataStringsFromFileWithoutJSON(){

        ArrayList <String> dataStrings = new ArrayList<>();
        try(FileReader reader = new FileReader(getPathToFile()))
        {
            int c;
            String dataString = "";
            while((c=reader.read())!=-1){
                char actualChar = (char)c;
                if (actualChar != '\n'){
                    dataString+=actualChar;
                }
                else {
                    dataStrings.add(dataString);
                    dataString = new String();
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return dataStrings;
    }

    public boolean getDynamicLightsWithoutProcessing(){
        boolean value = false;
        ArrayList <String> dataStrings = getDataStringsFromFileWithoutJSON();
        for (String string : dataStrings){
            String toBeFounded = LIGHTS+"\": \"";
            if (string.contains(toBeFounded)){
                int beginIndex = string.indexOf(toBeFounded)+toBeFounded.length();
                String substring = string.substring(beginIndex);
                System.out.println(LIGHTS + " are " + substring);
                if (substring.contains(ON)){
                    return true;
                }
                else return false;
            }
        }
        return value;
    }
    public int getAntiAliasingWithoutProcessing(){
        int value = 0;
        ArrayList <String> dataStrings = new ArrayList<>();
        try(FileReader reader = new FileReader(getPathToFile()))
        {
            int c;
            String dataString = "";
            while((c=reader.read())!=-1){
                char actualChar = (char)c;
                if (actualChar != '\n'){
                    dataString+=actualChar;
                }
                else {
                    dataStrings.add(dataString);
                    dataString = new String();
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        for (String string : dataStrings){
            String toBeFounded = "ANTI ALIASING\": \"";
            if (string.contains(toBeFounded)){
                int beginIndex = string.indexOf(toBeFounded)+toBeFounded.length();
                String substring = string.substring(beginIndex);
                System.out.println(ANTI_ALIASING + " is " + substring);
                if (substring.contains(PIXEL_ART)){
                    //System.out.println("Anti aliasing = " + 0);
                    return 0;
                }
                else if (string.contains("2")) return 2;
                else if (string.contains("4")) return 4;
                else if (string.contains("8")) return 8;
            }
            //System.out.println(string);
        }
        return value;
    }

    public void clearMemory(){

    }


}
