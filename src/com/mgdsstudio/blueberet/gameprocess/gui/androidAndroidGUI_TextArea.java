package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class androidAndroidGUI_TextArea extends androidGUI_Element {
    protected static Image picture;
    private int alignment = PConstants.CENTER;
    final static private float RELATIVE_TEXT_SHIFTING = -androidAndroidGUI_Button.NORMAL_HEIGHT/8f;
    protected ArrayList <String> textToBeShown;

    // For scrollable features
    private int relativePositionY = 0;
    private int maxRelativePositionY = 0;

    public androidAndroidGUI_TextArea(Vec2 pos, int w, int h, String name) {
        super(pos, w, h, name);
        textPos = new Vec2(pos.x, pos.y+RELATIVE_TEXT_SHIFTING);
        init();
        calculateTextSize(name, elementWidth, elementHeight);
        statement = ACTIVE;
    }
    public androidAndroidGUI_TextArea(Vec2 pos, int w, int h, String name, ArrayList<String> text, boolean onCenterAlongY) {
        super(pos, w, h, name);
        init();
        textToBeShown = text;
        if (text!= null){
            if (text.size()>0) calculateTextSize(getLongestStringFromArray(textToBeShown), elementWidth, elementHeight);
        }
        if (onCenterAlongY) textPos = new Vec2(pos.x, pos.y+RELATIVE_TEXT_SHIFTING);
        else textPos = new Vec2(pos.x, pos.y-(elementHeight/2)+elementHeight/12);
    }

    public void setTextToBeShown(ArrayList<String> text, int alignment){
        textToBeShown = text;
        this.alignment = alignment;
        calculateTextSize(getLongestStringFromArray(textToBeShown), elementWidth, elementHeight);
        if (mustBeTextAreaScrollable()){
            makeAreaScrollable();
        }
        else {
            makeAreaNotMoreScrollable();
        }
    }

    private void makeAreaScrollable(){
        int textZoneHeight = getTextZoneHeight(textToBeShown);
        maxRelativePositionY = (int)(textZoneHeight-elementHeight-textHeight);
    }


    private int getTextZoneHeight(ArrayList<String> text){
        return (int)(text.size()*textHeight + 4*textHeight);
    }

    private void makeAreaNotMoreScrollable(){
        relativePositionY = 0;
        maxRelativePositionY = 0;
    }

    @Override
    public void update(Vec2 relativePos){
        if (maxRelativePositionY>0){        //scrollable
            if (Program.engine.mousePressed){
                if (Program.engine.mouseY!= Program.engine.pmouseY){
                    float relativeMovement = Program.engine.mouseY- Program.engine.pmouseY;
                    relativePositionY-=relativeMovement;
                    if (relativePositionY>maxRelativePositionY) relativePositionY = maxRelativePositionY;
                    else if (relativePositionY<0) relativePositionY = 0;
                    System.out.println("Relative pos: " + relativePositionY + "; Max relative pos: " + maxRelativePositionY);
                }
            }
        }
    }

    private boolean mustBeTextAreaScrollable(){
        float textZoneHeight = getTextZoneHeight(textToBeShown);
        if (textZoneHeight >= elementHeight*0.8f){
            System.out.println("Area must be scrollable. Text zone: " + textZoneHeight + "; Text area: " + elementHeight);
            return true;
        }
        else {
            System.out.println("Area can not be scrollable. Text zone: " + textZoneHeight + "; Text area: " + elementHeight);
            return false;
        }
    }

    public void recalculateTextSize(){
        calculateTextSize(getLongestStringFromArray(textToBeShown), elementWidth, elementHeight);
    }

    private void init(){
        try{
            if (!graphicLoaded) {
                if (picture == null) {
                    picture = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TextArea.png"));
                    System.out.println("Graphic was loaded");
                }
                graphicLoaded = true;
            }
        }
        catch(Exception e){
            Program.engine.println("This picture was maybe already loaded" + e);
        }
    }

    @Override
    public void draw(PGraphics graphic){
        if (statement == ACTIVE){
            graphic.beginDraw();
            graphic.pushMatrix();
            graphic.pushStyle();
            graphic.imageMode(PConstants.CENTER);
            if (statement == ACTIVE){
                graphic.image(picture.getImage(), pos.x,pos.y, elementWidth, elementHeight);
            }
            graphic.imageMode(PConstants.CORNER);
            graphic.popStyle();
            graphic.popMatrix();
            graphic.endDraw();
        }
        drawText(graphic, alignment);
    }

    private void drawText(PGraphics graphic, int xAlignment){
        graphic.beginDraw();
        graphic.pushStyle();
        graphic.fill(0xff1040E8);
        graphic.textSize(textHeight);
        graphic.textAlign(xAlignment, PConstants.CENTER);
        int yPos = 0;
        int yNullPos = 0;
        if (textToBeShown!= null){
            if (textToBeShown.size()>0){
                for (int i = 0 ; i < textToBeShown.size(); i++) {
                    yPos = (int)(textPos.y+i*textHeight-relativePositionY);
                    yNullPos = (int)(textPos.y);
                    //if (i == 0) System.out.println("YPos: " + yPos);
                    if (isStringVisible(yPos, yNullPos)) graphic.text(textToBeShown.get(i), textPos.x, yPos);
                }
            }
        }
        graphic.popStyle();
        graphic.endDraw();
    }

    private boolean isStringVisible(int yPos, int nullPos) {
        if (maxRelativePositionY<=0) return true;
        else {
            if (yPos >= (pos.y + (elementHeight/2) * 0.9f-1*textHeight)) {      //if (yPos >= (pos.y + (elementHeight/2) * 0.9f-2*textHeight)) {
                 return false;
            }
            else {
                if (relativePositionY > 0) {
                    if (yPos < (nullPos)){    //if (yPos < (pos.y - textHeight-elementHeight/2)){
                        return false;
                    }
                    else return true;
                }
                else return true;
            }
        }
    }

    public void clearText() {
        textToBeShown.clear();

    }
}
