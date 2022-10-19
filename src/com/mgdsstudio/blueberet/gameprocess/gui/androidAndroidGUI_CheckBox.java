package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_CheckBox extends androidGUI_Element {
    //Next string was static
    private static Image picture;
    //private ArrayList<GUI_RadioButton> radioButtons;
    final static public int NORMAL_HEIGHT = (int)(androidAndroidGUI_Button.NORMAL_HEIGHT/1.5f);
    private boolean flagSet = false;
    //Next string was static
    private static ImageZoneSimpleData checked, unchecked, checkedPressed, uncheckedPressed, checkedFocused, uncheckedFocused;

    public androidAndroidGUI_CheckBox(Vec2 pos, int w, int h, String name) {
        super(pos, w, h, name);
        try{
            if (!graphicLoaded) {
                if (picture == null) picture = new Image(Program.getAbsolutePathToAssetsFolder("GUI_CheckBox.png"));
                //if (picture == null) picture = new Image(Programm.getRelativePathToTextures()+"GUI_CheckBox.png");
                graphicLoaded = true;
                int step = 34;
                //int step = 42;
                checked = new ImageZoneSimpleData(5,5,5+step,5+step);
                unchecked = new ImageZoneSimpleData(88,46,121,79);
                checkedPressed = new ImageZoneSimpleData(46,46,46+step,46+step);
                uncheckedPressed = new ImageZoneSimpleData(88,4,88+step,4+step);
                checkedFocused = new ImageZoneSimpleData(4,46, 4+step,46+step);
                uncheckedFocused = new ImageZoneSimpleData(46,4, 46+step, 4+step);

                /*
                int step = 42;
                checked = new ImageZoneSimpleData(0,0,step,step);
                unchecked = new ImageZoneSimpleData(2*step,step,3*step,2*step);
                checkedPressed = new ImageZoneSimpleData(step,step,2*step,2*step);
                uncheckedPressed = new ImageZoneSimpleData(2*step,0,3*step,step);
                checkedFocused = new ImageZoneSimpleData(0,step, step,2*step);
                uncheckedFocused = new ImageZoneSimpleData(step,0, 2*step, step);
                */
            }
        }

        catch (Exception e){
            Program.engine.println("This picture was maybe already loaded" + e);
        }
        textPos = new Vec2(pos.x, pos.y);
        calculateTextSize(elementWidth-elementHeight, elementHeight);
    }

    @Override
    public void draw(PGraphics graphic){
        drawBody(graphic);
        drawName(graphic, PApplet.CENTER);
    }

    private ImageZoneSimpleData getActualImageZoneData(){
        if (flagSet){
            if (statement == PRESSED){
                return checkedPressed;
            }
            else if (statement == RELEASED){
                return checkedFocused;
            }
            else return checked;
        }
        else{
            if (statement == PRESSED){
                return uncheckedPressed;
            }
            else if (statement == RELEASED){
                return uncheckedFocused;
            }
            else return unchecked;

        }
    }

    /*
    protected void drawButtonBody(PGraphics graphic){
        graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        int xPosition = (int)(pos.x-(elementWidth/2f)+(elementHeight/2f));
        if (statement == ACTIVE){
            graphic.image(pictureActive.getImage(), xPosition,pos.y, elementHeight, elementHeight);
        }
        else if (statement == PRESSED){
            graphic.image(pictureFocused.getImage(), xPosition,pos.y,elementHeight, elementHeight);
        }
        else if (statement == RELEASED){
            graphic.image(pictureChecked.getImage(), xPosition,pos.y, elementHeight, elementHeight);
        }
        else graphic.image(pictureActive.getImage(), xPosition,pos.y, elementHeight, elementHeight);
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        graphic.endDraw();
    }
*/
    protected void drawBody(PGraphics graphic){
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        int xPosition = (int)(pos.x-(elementWidth/2f)+(elementHeight/2f));
        if (statement!=HIDDEN && statement!=BLOCKED){
            ImageZoneSimpleData data = getActualImageZoneData();
            graphic.image(picture.getImage(), xPosition,pos.y, elementHeight, elementHeight, data.leftX, data.upperY, data.rightX, data.lowerY);
        }
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
    }

    public void setFlagSet(boolean newFlagStatement){
        flagSet = newFlagStatement;
    }

    public boolean isFlagSet(){
        return flagSet;
    }

    private void changeFlagStatement(){
        if (flagSet) flagSet = false;
        else flagSet = true;
    }

    public void update(Vec2 relativePos){
        //System.out.println("Statement; " + statement + " flag: "  + flagSet);
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x - (elementWidth / 2) + relativePos.x + elementHeight)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {

                Vec2 actualMouse =new Vec2(Program.engine.mouseX, Program.engine.mouseY);
                Vec2 prevMouse = new Vec2(Program.engine.pmouseX, Program.engine.pmouseY);
                float mouseWayPerLastFrame = actualMouse.sub(prevMouse).length();
                if (onScrollableField && mouseWayPerLastFrame > androidGUI_ScrollableTab.CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING){
                    //System.out.println("Mouse is moving to fast to select this radio button " + mouseWayPerLastFrame);

                }
                else {
                    if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                        statement = PRESSED;
                        pressedNow = true;
                        changeFlagStatement();
                    }
                    else if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
                        if (statement != PRESSED) {
                            statement = PRESSED;
                            changeFlagStatement();
                        }
                        //changeFlagStatement();
                    }
                    else if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                        if (pressedNow == true) {
                            pressedNow = false;
                            statement = RELEASED;
                            //changeFlagStatement();
                            //changeFlagStatement();
                        }
                    }

                }


            }
            else if (statement == PRESSED || statement == RELEASED) {
                if (statement == PRESSED) statement = RELEASED;
                if (pressedNow) pressedNow = false;
            }
        }
    }

    /*
    public GUI_CheckBox(Vec2 pos, int w, int h, String name) {
        super(pos, w, h, name);
        try{
            if (!graphicLoaded) {
                if (picture == null) picture = new Image("GUI_CheckBox.png");
                graphicLoaded = true;
                int step = 42;
                checked = new ImageZoneSimpleData(0,0,step,step);
                unchecked = new ImageZoneSimpleData(2*step,step,3*step,2*step);
                checkedPressed = new ImageZoneSimpleData(step,step,2*step,2*step);
                uncheckedPressed = new ImageZoneSimpleData(2*step,0,3*step,step);
                checkedFocused = new ImageZoneSimpleData(0,step, step,2*step);
                uncheckedFocused = new ImageZoneSimpleData(step,0, 2*step, step);
            }
        }

        catch (Exception e){
            Programm.engine.println("This picture was maybe already loaded" + e);
        }
        textPos = new Vec2(pos.x, pos.y);
        calculateTextSize(elementWidth-elementHeight, elementHeight);
    }

    @Override
    public void draw(PGraphics graphic){
        drawBody(graphic);
        drawName(graphic, PApplet.CENTER);
    }

    private ImageZoneSimpleData getActualImageZoneData(){
        if (flagSet){
            if (statement == PRESSED){
                return checkedPressed;
            }
            else if (statement == RELEASED){
                return checkedFocused;
            }
            else return checked;
        }
        else{
            if (statement == PRESSED){
                return uncheckedPressed;
            }
            else if (statement == RELEASED){
                return uncheckedFocused;
            }
            else return unchecked;

        }
    }

    protected void drawBody(PGraphics graphic){
        graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        int xPosition = (int)(pos.x-(elementWidth/2f)+(elementHeight/2f));
        if (statement!=HIDDEN && statement!=BLOCKED){
            ImageZoneSimpleData data = getActualImageZoneData();
            graphic.image(picture.getImage(), xPosition,pos.y, elementHeight, elementHeight, data.getLeftUpperX(), data.getLeftUpperY(), data.getRightLowerX(), data.getRightLowerY());
        }
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        graphic.endDraw();
    }

    public void setFlagSet(boolean newFlagStatement){
        flagSet = newFlagStatement;
    }

    public boolean isFlagSet(){
        return flagSet;
    }

    private void changeFlagStatement(){
        if (flagSet) flagSet = false;
        else flagSet = true;
    }

    public void update(Vec2 relativePos){
        //System.out.println("Statement; " + statement + " flag: "  + flagSet);
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Programm.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Programm.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Programm.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                    statement = PRESSED;
                    pressedNow = true;
                }
                else if (!Programm.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                    }
                    changeFlagStatement();
                }
                else if (Programm.engine.mousePressed && Editor2D.prevMousePressedStatement){
                    statement = PRESSED;
                }
            }
            else if (statement == PRESSED || statement == RELEASED) {
                if (statement == PRESSED) statement = RELEASED;
                if (pressedNow) pressedNow = false;
            }
        }
    }
    */
}
