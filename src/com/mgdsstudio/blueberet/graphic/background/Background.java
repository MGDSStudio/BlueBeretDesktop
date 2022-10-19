package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;

public abstract class Background implements IDrawable {
    public final static boolean withUnfocusing = true;
    public static final String CLASS_NAME = "Background";
    protected float leftUpperX;
    protected float leftUpperY;
    protected boolean moveable;
    public static final byte SCROLLABLE_PICTURE_BACKGROUND = 2;
    public static final byte REPEATING_BACKGROUND_ELEMENTS = 3;
    public static final byte SINGLE_COLOR_BACKGROUND = 4;
    protected boolean hide;
    public final static boolean backgroundAtAnotherFrame = true;
    public final static float BACKGROUND_DIMENSION_COEFFICIENT = 0.25f;

    protected void clearBackground(){
        if (backgroundAtAnotherFrame){
            Program.backgroundFrame.clear();
        }
        else {
            Program.objectsFrame.beginDraw();
            Program.objectsFrame.clear();
            Program.objectsFrame.endDraw();
        }
    }

    public void loadGraphic(Tileset tileset){

    }

    /*
    public static void drawBackgroundAsCleared(){
        Program.objectsFrame.beginDraw();
        Program.objectsFrame.clear();
        Program.objectsFrame.endDraw();
        //Programm.engine.clear();
    }*/

    public static void drawBackgroundAsWhite(){
        //Programm.backgroundsFrame.beginDraw();
        /*
        if (GameProcess.BACKGROUND_AT_SEPARATE_GRAPHIC) Program.objectsFrame.background(255,255,255);
        else
         */
        if (backgroundAtAnotherFrame) Program.backgroundFrame.background(255);
        else Program.objectsFrame.background(255);
        //Programm.backgroundsFrame.endDraw();
        //Programm.engine.clear();
    }


    public String getPath(){
        return null;
    }


    public abstract String getStringData();

    public abstract byte getType();


    public void setWidth(int value){

    }

    public void setHeight(int value) {
    }

    public void setRelativeVelocity(float value) {

    }

    public float getLeftUpperX() {
        return leftUpperX;
    }

    public float getLeftUpperY() {
        return leftUpperY;
    }


    public void shift(float xShifting, float yShifting){
        leftUpperY+=yShifting;
        leftUpperX+=xShifting;
    }

    public String getObjectToDisplayName(){
        System.out.println("This name must be overriden");
        return CLASS_NAME;
    }

    public void setHide(boolean flag){
        hide = flag;
    }

    public boolean isHide(){
        return hide;
    }


    public void redraw(GameCamera gameCamera) {
        draw(gameCamera);
    }
}
