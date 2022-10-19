package com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class BeretColorSaveMaster extends AbstractBeretColorMaster {
    private PApplet engine;


    public BeretColorSaveMaster(PApplet engine) {
        data = new JSONArray();
        this.engine = engine;
    }

    public BeretColorSaveMaster(PApplet engine, TwiceColor twiceColor) {
        data = new JSONArray();
        this.engine = engine;
        beretColor = twiceColor;
    }

    public void saveData(){
        if (beretColor == null) {
            saveDefaultColor();
        }
        else{
            saveColor(beretColor);
        }
        saveOnDisk();
    }

    private void saveColor(TwiceColor twiceColor) {
        JSONObject defaultData = new JSONObject();
        defaultData.setString(BERET_COLOR_NAME, twiceColor.getName());
        defaultData.setInt(BRIGHT_COLOR_NAME, twiceColor.getBrightColor());
        defaultData.setInt(DARK_COLOR_NAME, twiceColor.getDarkColor());
        this.data.setJSONObject(0, defaultData);
        count++;
        int rDark = twiceColor.getDarkRed();
        int gDark = twiceColor.getDarkGreen();
        int bDark = twiceColor.getDarkBlue();
        System.out.println("Color for beret was saved " + twiceColor.getName() + " and color: " + rDark + "," + gDark + "," + bDark);
    }

    private void saveDefaultColor() {
        JSONObject defaultData = new JSONObject();
        defaultData.setString(BERET_COLOR_NAME, SKY_BLUE_NAME_EN);
        defaultData.setInt(BRIGHT_COLOR_NAME, NORMAL_BERET_BRIGHT_COLOR);
        defaultData.setInt(DARK_COLOR_NAME, NORMAL_BERET_DARK_COLOR);
        this.data.setJSONObject(0, defaultData);
        count++;
    }

    protected void saveOnDisk(){
        engine.saveJSONArray(data, getPathToFile());
        System.out.println("Data was saved to " + getPathToFile());
    }
}

