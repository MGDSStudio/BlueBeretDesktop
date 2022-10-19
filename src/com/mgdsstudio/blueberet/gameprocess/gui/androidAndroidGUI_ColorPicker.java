package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_ColorPicker extends androidGUI_Element {
    private final ImageZoneSimpleData redColorPicker = new ImageZoneSimpleData(0,0,15,128);
    private final ImageZoneSimpleData greenColorPicker = new ImageZoneSimpleData(16,0,31,128);
    private final ImageZoneSimpleData blueColorPicker = new ImageZoneSimpleData(32,0,47,128);


    private final static String PATH_TO_MAIN_IMAGE = Program.getAbsolutePathToAssetsFolder("ColorPicker.png");
    protected final static Image mainImage = new Image(PATH_TO_MAIN_IMAGE);
    public static final byte RED = 0, GREEN = 1, BLUE = 2;
    private final byte color;
    private int actualFramePosition;
    private int upperPositionForMouse, lowerPositionForMouse;
    private float relativeValue= 0.00f;
    private boolean wasFirstTap;

    public androidAndroidGUI_ColorPicker(Vec2 pos, int w, int h, String name, byte color) {
        super(pos, w, h, name);
        this.color = color;
        statement = ACTIVE;
    }

    @Override
    public void draw(PGraphics pGraphics){
        drawColorField(pGraphics);
        if (wasFirstTap) drawChoosingFrame(pGraphics);
    }

    private void updateFramePosition(Vec2 relativePos, float y){
        upperPositionForMouse = (int)((pos.y - (elementHeight / 2) + relativePos.y));
        lowerPositionForMouse = (int)((pos.y + (elementHeight / 2) + relativePos.y));
        int relativeMoisePos = (int)(y-relativePos.y);
        relativeValue = 1-(y-upperPositionForMouse)/(lowerPositionForMouse-upperPositionForMouse);
        //System.out.println("Relative value is: " + relativeValue + "; Upper: " + upperPositionForMouse + "; Lower: " + lowerPositionForMouse + "; Y: " + y);
    }

    public int getColorValue(){
        return (int)(255*relativeValue);
    }

    public void update(Vec2 relativePos){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                    if (!wasFirstTap) wasFirstTap = true;
                    statement = PRESSED;
                    pressedNow = true;
                } else if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                    }
                }
                else if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
                    statement = PRESSED;
                }
            }
            else if (statement == PRESSED || statement == RELEASED) {
                statement = ACTIVE;
                if (pressedNow) pressedNow = false;
            }
            if (statement == PRESSED) updateFramePosition(relativePos, Program.engine.mouseY);
        }
    }


    protected void drawColorField(PGraphics graphic){
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        if (statement != HIDDEN && statement != BLOCKED){
            ImageZoneSimpleData imageZoneSimpleData = getImageZoneDataForColorPicker(color);
            graphic.image(mainImage.getImage(), pos.x,pos.y, elementWidth, elementHeight, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);

        }
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
    }

    protected void drawChoosingFrame(PGraphics graphic){
        graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        if (statement != HIDDEN && statement != BLOCKED){
            graphic.pushStyle();

            int actualY = (int)((pos.y+elementHeight/2f)-((relativeValue)*elementHeight));
            //System.out.println("Actual Y= " + actualY + "; Rel: " + relativeValue + "; Height: " + elementHeight + "; Pos: " + pos.y + "; elementHeight: " + elementHeight);
            graphic.stroke(142,121,24);
            graphic.strokeWeight(PApplet.ceil(Program.engine.width/75));
            //graphic.line(pos.x-elementWidth/2, actualY, pos.x+elementWidth/2, actualY);
            graphic.noFill();
            graphic.rectMode(PConstants.CENTER);
            graphic.rect(pos.x, actualY, elementWidth*1.1f, Program.engine.width/50, Program.engine.width/33);
            /*
            graphic.image(GUI.mainImage.getImage(), pos.x,pos.y, elementWidth, elementHeight, imageZoneSimpleData.getLeftUpperX(), imageZoneSimpleData.getLeftUpperY(), imageZoneSimpleData.getRightLowerX(), imageZoneSimpleData.getRightLowerY());
            */
            graphic.popStyle();

        }
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        graphic.endDraw();
    }

    protected ImageZoneSimpleData getImageZoneDataForColorPicker(byte color){
        if (color == RED) return redColorPicker;
        else if (color == GREEN) return greenColorPicker;
        else if (color == BLUE) return blueColorPicker;
        else {
            System.out.println("For this color " + color + " there are no image data");
            return null;
        }
    }

}
