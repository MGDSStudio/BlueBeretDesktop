package com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger;

import processing.core.PApplet;
import processing.data.JSONObject;

public class BeretColorLoadingMaster extends AbstractBeretColorMaster implements IBeretColors{

    public BeretColorLoadingMaster(PApplet engine) {
        try {
            init(engine);
        }
        catch (Exception e){
            BeretColorSaveMaster beretColorSaveMaster = new BeretColorSaveMaster(engine);
            beretColorSaveMaster.saveData();
            beretColorSaveMaster.saveOnDisk();
            System.out.println("This file doesnot exist. It will be created");
            try{
                init(engine);
                System.out.println("This file was sucessfully be created");
            }
            catch (Exception e2){
                System.out.println("This file can not be created");
            }
        }
    }

    private void init(PApplet engine){
        data = engine.loadJSONArray(getPathToFile());
    }

    public void loadData(){
        loadColors();
    }

    private void loadColors() {
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String name = object.getString(BERET_COLOR_NAME);
            int bright = object.getInt(BRIGHT_COLOR_NAME);
            int dark = object.getInt(DARK_COLOR_NAME);
            TwiceColor twiceColor = new TwiceColor(name, bright, dark);
            beretColor = twiceColor;
            }
        }


}
