package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class androidAndroidGUI_RadioButton extends androidGUI_Element {
    private static Image pictureActive, pictureChecked, pictureFocused, pictureBlocked;
    private ArrayList<androidAndroidGUI_RadioButton> radioButtons;
    final static public int NORMAL_WIDTH_IN_REDACTOR = (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR);
    final static public int NORMAL_HEIGHT_IN_REDACTOR = (int)(androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR*0.85f);

    public androidAndroidGUI_RadioButton(Vec2 pos, int w, int h, String name) {
        super(pos, w, h, name);
        try{
            if (!graphicLoaded) {
                if (pictureActive == null) pictureActive = new Image(Program.getAbsolutePathToAssetsFolder("GUI_RadionButton.png"));
                if (pictureChecked == null) pictureChecked = new Image(Program.getAbsolutePathToAssetsFolder("GUI_RadionButtonChecked.png"));
                if (pictureFocused == null) pictureFocused = new Image(Program.getAbsolutePathToAssetsFolder("GUI_RadionButtonFocused.png"));
                graphicLoaded = true;
            }
        }

        catch (Exception e){
            Program.engine.println("This picture was maybe already loaded" + e);
        }
        textPos = new Vec2(pos.x+elementHeight/3f, pos.y);
        calculateTextSize(name,elementWidth-(elementHeight/1f), elementHeight);
    }

    public void draw(PGraphics graphic){
        drawButtonBody(graphic);
        drawName(graphic, PApplet.CENTER);
    }



    public void addAnotherRadioButtonsInGroup(ArrayList<androidAndroidGUI_RadioButton> radioButtons){
        this.radioButtons = radioButtons;
    }

    protected void drawButtonBody(PGraphics graphic){
        //graphic.beginDraw();
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
        //graphic.endDraw();
    }

    private void releaseAnotherRadioButtonsInGroup(){
        if (radioButtons!=null){
            for (androidAndroidGUI_RadioButton button: radioButtons){
                if (!button.equals(this)){
                    if (button.getStatement()!= BLOCKED){
                        button.setStatement(ACTIVE);
                    }
                }
            }
        }
    }

    public void update(Vec2 relativePos){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }

            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x - (elementWidth / 2) + relativePos.x+elementHeight)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
//if ((Programm.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Programm.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                Vec2 actualMouse =new Vec2(Program.engine.mouseX, Program.engine.mouseY);
                Vec2 prevMouse = new Vec2(Program.engine.pmouseX, Program.engine.pmouseY);
                float mouseWayPerLastFrame = actualMouse.sub(prevMouse).length();
                if (onScrollableField && mouseWayPerLastFrame > androidGUI_ScrollableTab.CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING){
                    System.out.println("Mouse is moving to fast to select this radio button " + mouseWayPerLastFrame);
                }
                else{
                    if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                        statement = PRESSED;
                        pressedNow = true;
                        releaseAnotherRadioButtonsInGroup();
                    }
                    else if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
                        statement = PRESSED;
                        releaseAnotherRadioButtonsInGroup();
                    }
                }
                if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                        releaseAnotherRadioButtonsInGroup();
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
private static Image pictureActive, pictureChecked, pictureFocused, pictureBlocked;
    private ArrayList<GUI_RadioButton> radioButtons;
    final static public int NORMAL_WIDTH_IN_REDACTOR = (int)(GUI_Button.NORMAL_WIDTH_IN_REDACTOR);
    final static public int NORMAL_HEIGHT_IN_REDACTOR = (int)(GUI_Button.NORMAL_HEIGHT_IN_REDACTOR*0.85f);
    private boolean markerSet = false;

    public GUI_RadioButton(Vec2 pos, int w, int h, String name) {
        super(pos, w, h, name);
        try{
            if (!graphicLoaded) {
                if (pictureActive == null) pictureActive = new Image("GUI_RadionButton.png");
                if (pictureChecked == null) pictureChecked = new Image("GUI_RadionButtonChecked.png");
                if (pictureFocused == null) pictureFocused = new Image("GUI_RadionButtonFocused.png");
                graphicLoaded = true;
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
        drawButtonBody(graphic);
        drawName(graphic, PApplet.CENTER);
    }

    public void addAnotherRadioButtonsInGroup(ArrayList<GUI_RadioButton> radioButtons){
        ArrayList<GUI_RadioButton> anotherRadioButtons = new ArrayList<>();
        for (int i = (radioButtons.size()-1); i>=0; i--){
            if (radioButtons.get(i).equals(this)){
                //System.out.println("Element " + i + " is this");
            }
            else anotherRadioButtons.add(radioButtons.get(i));
        }
        this.radioButtons = anotherRadioButtons;
    }

    public void setMarker(boolean flag){
        if (flag) markerSet = true;
        else markerSet = false;
    }

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

    private void releaseAnotherRadioButtonsInGroup(){
        if (radioButtons!=null){
            for (GUI_RadioButton button: radioButtons){
                if (!button.equals(this)){
                    if (button.getStatement()!= BLOCKED){
                        button.setStatement(ACTIVE);
                    }
                }
            }
        }
    }

    public void update(Vec2 relativePos){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Programm.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Programm.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Programm.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                    statement = PRESSED;
                    pressedNow = true;
                    releaseAnotherRadioButtonsInGroup();
                    //println("Button is pressed");
                } else if (!Programm.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                        releaseAnotherRadioButtonsInGroup();
                    }
                }
                else if (Programm.engine.mousePressed && Editor2D.prevMousePressedStatement){
                    statement = PRESSED;
                    releaseAnotherRadioButtonsInGroup();
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
