package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class SingleColorBackground extends Background{
    private int red, green, blue;
    private final String objectToDisplayName = "Single color background";
    private boolean backgroundWasPrepared;

    public SingleColorBackground(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        System.out.println("Blue get as: " + blue);
        this.blue = blue;
    }

    public SingleColorBackground(GameObjectDataForStoreInEditor data){
        this.red = data.getRedValue();
        this.green = data.getGreenValue();
        this.blue = data.getBlueValue();
    }

    @Override
    protected void clearBackground(){

        if (!hide) {
            //if (!backgroundWasPrepared) {
                //Program.objectsFrame.beginDraw();
                //Programm.engine.background(red, green, blue);
               // Program.objectsFrame.background(red, green, blue);
                //Program.objectsFrame.endDraw();
                backgroundWasPrepared = true;

                //System.out.println("Drawn: " + red + ", " + green + ", " + blue);
            //}
        }
        else {
            //Program.objectsFrame.beginDraw();
            //Program.objectsFrame.clear();
            //Program.objectsFrame.endDraw();
            /*
            if (backgroundWasPrepared) {
                backgroundWasPrepared = false;
                Programm.backgroundsFrame.beginDraw();
                Programm.engine.clear();
                Programm.backgroundsFrame.clear();
                Programm.backgroundsFrame.endDraw();
                backgroundWasPrepared = true;
            }
            */
            //Programm.engine.clear();
        }
        
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createBackground();
        //System.out.println("Data string for background: " +saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public void draw(GameCamera gameCamera) {
      // Program.backgroundFrame.beginDraw();
       Program.backgroundFrame.background(red,green,blue);
       //Program.backgroundFrame.endDraw();
       //was so: clearBackground();

    }

    public void setRed(int red) {

        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public byte getType(){
        return SINGLE_COLOR_BACKGROUND;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getObjectToDisplayName(){
        return objectToDisplayName + ": "+red+","+green+","+blue;
    }
}
