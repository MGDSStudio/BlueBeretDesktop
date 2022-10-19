package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_DropDownElement extends androidGUI_Element {
    private static Image unfocusedImage, focusedImage, pressedImage, disabledImage;
    private static boolean isGraphicLoaded = false;
    private PGraphics graphic;
    private androidGUI_MenuLowLevelTabType actionType;

    public androidAndroidGUI_DropDownElement(Vec2 leftUpperCorner, int width, int height, String name, androidGUI_MenuLowLevelTabType actionType){
        super(new Vec2(leftUpperCorner.x, leftUpperCorner.y), width, height, name);
        this.actionType = actionType;
        elementHeight = height;
        elementWidth = width;
        if (!isGraphicLoaded) {
            try {
                unfocusedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_Tab.png"));
                unfocusedImage.getImage().resize(elementWidth, height);
                focusedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TabFocused.png"));
                focusedImage.getImage().resize(elementWidth, height);
                pressedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TabPressed.png"));
                pressedImage.getImage().resize(elementWidth, height);
                disabledImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TabDisabled.png"));
                disabledImage.getImage().resize(elementWidth, height);
            }
            catch (Exception e){
                System.out.println("Images for combo box can not be founded " + e);
            }
        }
        calculateTextSize(elementWidth, height/2);

        textFont = Program.engine.createFont(Program.mainFontName, textHeight, true);
        float RELATIVE_TEXT_SHIFTING = -NORMAL_HEIGHT/7f;
        textPos = new Vec2(2f*height/7f, RELATIVE_TEXT_SHIFTING+height/2f);
        if (Program.graphicRenderer == Program.OPENGL_RENDERER) graphic = Program.engine.createGraphics(elementWidth, elementHeight, PApplet.P2D);
        else graphic = Program.engine.createGraphics(elementWidth, elementHeight, PApplet.JAVA2D);
        statement = ACTIVE;
        previousStatement = statement;
    }

    public void setStatement(byte newStatement){
        super.setStatement(newStatement);
        if (newStatement == BLOCKED){
            normalTextColor = blockedColor;
        }
    }

    public void setPosition(Vec2 newPos) {
        pos = newPos;
    }

    public Vec2 getPos(){
        return pos;
    }

    public void moveAlongY(float shifting){
        pos.y+=shifting;
    }

    public void update(){
        if (statement != BLOCKED) {
            previousStatement = statement;
        }
    }

    public androidGUI_MenuLowLevelTabType getActionType(){
        return actionType;
    }


/* from parent

    */

    public void drawOnSingleGraphic(PGraphics singleGraphic, Vec2 relativePos){
        //System.out.println("Pos: " + (relativePos.x+pos.x)+"x"+(relativePos.y+pos.y));

        if (statement == PRESSED) singleGraphic.image(pressedImage.getImage(), pos.x,pos.y);
        else {
            if (statement == ACTIVE) singleGraphic.image(unfocusedImage.getImage(), pos.x,pos.y);
            else {
                if (statement == BLOCKED) {
                    singleGraphic.image(disabledImage.getImage(), pos.x,pos.y);
                }
                else singleGraphic.image(focusedImage.getImage(), pos.x,pos.y);
            }
        }
        drawNameOnSingleGraphic(singleGraphic, PApplet.CORNER);
        //singleGraphic.endDraw();
        //if (Programm.engine.frameCount%100 == 1) System.out.println("Maybe next three lines must be deleted");

    }

    public void drawNameOnSingleGraphic(PGraphics graphic, int xAlignment){
        if (graphic!=null) {
            graphic.pushStyle();
            graphic.fill(normalTextColor);
            graphic.textSize(textHeight);
            graphic.textAlign(xAlignment, PConstants.CENTER);
            try {
                graphic.text(name, textPos.x+pos.x, textPos.y+pos.y);
            }
            catch (Exception e){

            }
            graphic.popStyle();
        }
    }

    public void draw(Vec2 relativePos){
        graphic.beginDraw();
        if (statement == PRESSED) graphic.image(pressedImage.getImage(), 0,0);
        else {
            if (statement == ACTIVE) graphic.image(unfocusedImage.getImage(), 0,0);
            else {
                if (statement == BLOCKED) {
                    graphic.image(disabledImage.getImage(), 0,0);
                }
                else graphic.image(focusedImage.getImage(), 0,0);
            }
        }
        //graphic.imageMode(PConstants.CENTER);
        drawName(graphic, PApplet.CORNER);
        graphic.endDraw();
        if (Program.engine.frameCount%100 == 1) System.out.println("Maybe next three lines must be deleted");
        Program.engine.imageMode(PConstants.CORNER);
        Program.engine.image(graphic, pos.x, pos.y+relativePos.y);
        Program.engine.imageMode(PConstants.CENTER);

        /*
        graphic.beginDraw();
        if (statement == PRESSED) graphic.image(pressedImage.getImage(), 0,0);
        else if (statement == ACTIVE) graphic.image(unfocusedImage.getImage(), 0,0);
        else graphic.image(disabledImage.getImage(), 0,0);
        //graphic.imageMode(PConstants.CENTER);
        graphic.endDraw();
        drawName(graphic, PApplet.CORNER);
        Game2D.engine.imageMode(PConstants.CORNER);
        Game2D.engine.image(graphic, pos.x, pos.y);
        Game2D.engine.imageMode(PConstants.CENTER);
        */
    }


}
