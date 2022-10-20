package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.engine.nesgui.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;


import java.util.ArrayList;

class GUI_Controller{
    private final PGraphics graphics;
    private final ImageZoneSimpleData blackData;
    private Image graphicSource;
    private final Frame mapZone;
    //public static int distanceToMapZoneBoard = (int)(Program.engine.width/18.33f);
    private final int neutralAreaWidth;
    private final PApplet engine;
    private final Tab tab;
    private final FrameWithMoveableText console;
    private final ArrayList <SliderButtonWithText> sliders;

    private boolean drawingStarted = false;

    //private

    public GUI_Controller(Frame rectangle, PApplet engine, PGraphics graphics) {
        this.mapZone = rectangle;
        this.graphics = graphics;
        neutralAreaWidth = (int) (engine.width/18.33f);
        this.engine = engine;
        blackData = new ImageZoneSimpleData(109,266,110,267);
        float tabWidth = engine.width*0.75f;
        int y = engine.height/2;
        sliders = new ArrayList<>();
        tab = new Tab((int)(engine.width/2-tabWidth/2), y, (int)tabWidth, engine.height-y-neutralAreaWidth, engine.height-y-neutralAreaWidth, "Tab", graphics);    //int leftX, int upperY, int width, int height, int visibleHeight, String name, PGraphics graphics, true) {
        console = new FrameWithMoveableText((int)(mapZone.getLeftX()+mapZone.getWidth()/2), (int)(mapZone.getUpperY()+mapZone.getHeight()+mapZone.getUpperY()+ GuiElement.getNormalButtonHeightRelativeToScreenSize(graphics.width)/2), mapZone.getWidth(), GuiElement.getNormalButtonHeightRelativeToScreenSize(graphics.width)/2, "Console" , graphics, "      SELECT A COMMAND ON A RIGHT SLIDER BUTTON      ");
        graphicSource = Tab.getGraphicFile();
        init();
    }

    private void init() {

    }

    void update(){
        for (SliderButtonWithText buttonWithText : sliders){
            buttonWithText.update(engine.mouseX, engine.mouseY);
        }
        console.update(engine.mouseX, engine.mouseY);
        tab.update(engine.mouseX, engine.mouseY);
    }

    void draw(){
        if (!drawingStarted){
            drawingStarted = true;
        }
        else {
            graphics.beginDraw();
            clearFrame();
            tab.draw(graphics);
            console.draw(graphics);
            for (SliderButtonWithText slider : sliders) slider.draw(graphics);
            graphics.endDraw();
        }
            /*

        */

    }

    private void clearFrame(){
        graphics.imageMode(PConstants.CORNER);

        graphics.image(graphicSource.getImage(),0,0, mapZone.getLeftX(), graphics.height, blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //Right
        graphics.image(graphicSource.getImage(),mapZone.getLeftX()+mapZone.getWidth(),0, graphics.width-(mapZone.getLeftX()+mapZone.getWidth()), graphics.height, blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //upper
        graphics.image(graphicSource.getImage(),mapZone.getLeftX(),0, mapZone.getWidth(), mapZone.getUpperY(), blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //lower
        graphics.image(graphicSource.getImage(),mapZone.getLeftX(),mapZone.getUpperY()+mapZone.getHeight(), mapZone.getWidth(), graphics.height-(mapZone.getUpperY()+mapZone.getHeight()), blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);



    }


    public PGraphics getGraphic() {
        return graphics;
    }
}
