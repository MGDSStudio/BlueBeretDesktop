package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.ArrayList;

public class OnScreenConsole implements ISimpleUpdateable {
    private Timer timer;
    private final static int MIN_SHOW_TIME = 700;
    private static Image frameImage;
    ArrayList<String> text;
    private PFont textFont;
    private int textHeight = 1;
    private int textSpacing;
    private final Vec2 leftUppepCorner;
    private final int width;
    private final int heightForOneLine;
    private int height;
    private final int frameColor = Program.engine.color(148,131,216,128);
    private final int textColor = Program.engine.color(62,9,9);
    private int upperFreeZoneHeight = (int)(Program.engine.width/38f);
    final int lowerFreeZoneHeight = (int)(upperFreeZoneHeight*2.25f);
    private int textPositionX = (int)(Program.engine.width/2);
    private int textPositionY = (int)(Program.engine.height/2);
    //private final boolean heightResizeability;

    //public final static byte STATIC_TEXT_HEIGHT = 1;
    //public final static byte STATIC_FRAME_HEIGHT = 2;
    //public final static byte STATIC_FRAME_HEIGHT = 3;

    public OnScreenConsole(Vec2 leftUppepCorner, int width, int heightForOneLine){
        this.leftUppepCorner = leftUppepCorner;
        this.width = width;
        this.heightForOneLine = heightForOneLine;
        this.height = heightForOneLine;
        if (OnScreenConsole.frameImage == null) OnScreenConsole.frameImage = new Image(Program.getAbsolutePathToAssetsFolder("OnScreenConsole.png"));
        //calculateTextSize();
        //calculateTextSize(na, float maxWidth, float maxHeight){
        text = new ArrayList<>();
        text.add("");
        calculateTextSize();
    }

    private void calculateTextSize(){
        textHeight = height-upperFreeZoneHeight-lowerFreeZoneHeight-textSpacing;
        if (textHeight < 5) {
            System.out.println("Console is too low");
            textHeight = 5;
        }
        textSpacing = (int)(textHeight/5);
        textHeight = height-upperFreeZoneHeight-lowerFreeZoneHeight-textSpacing;
        if (textHeight < 0) {
            System.out.println("Calculated text height for text" + text + " was: " + textHeight);
            textHeight = 5;
        }

        textFont = Program.engine.createFont("Roboto", textHeight, true);
        textPositionX = (int)(leftUppepCorner.x+width/2);
        //textFont = Game2D.engine.createFont("Roboto", textHeight, true);
    }



    protected void calculateTextSize(String textToBeShown, float maxWidth, float maxHeight){
        textHeight = (int)(maxHeight*0.7f);
        Program.engine.textSize(textHeight);
        textFont = Program.engine.createFont("Roboto", textHeight, true);
        float actualWidth = Program.engine.textWidth(textToBeShown);
        if (actualWidth > (maxWidth*0.97f)) {
            while (actualWidth > (9 * maxWidth / 11)){
                textHeight--;
                Program.engine.textSize(textHeight);
                //Game2D.engine.textFont(textFont);
                textFont = Program.engine.createFont("Roboto", textHeight, true);
                actualWidth = Program.engine.textWidth(textToBeShown);
                //System.out.println("Actual height: " + textHeight + " ; width: " + actualWidth + "; Element width: " + width);
            }
        }
        //upperFreeZoneHeight = (int)(Game2D.engine.width/38f);
        upperFreeZoneHeight = (int)((height/2-textHeight/2)-(height*0.05f));


        /*
        if (maxHeight > 0 && maxWidth > 0) {
            if (actualWidth<maxWidth) {
                while (actualWidth < (9 * maxWidth / 11)) {
                    textHeight++;
                    Game2D.engine.textSize(textHeight);
                    Game2D.engine.textFont(textFont);
                    textFont = Game2D.engine.createFont("Roboto", textHeight, true);
                    actualWidth = Game2D.engine.textWidth(textToBeShown);
                    System.out.println("Actual height: " + textHeight + " ; width: " + actualWidth + "; Element width: " + width);
                }
            }
            if (actualWidth > maxWidth){
                while (actualWidth > (9 * maxWidth / 11)){
                    textHeight--;
                    Game2D.engine.textSize(textHeight);
                    Game2D.engine.textFont(textFont);
                    textFont = Game2D.engine.createFont("Roboto", textHeight, true);
                    actualWidth = Game2D.engine.textWidth(textToBeShown);
                    System.out.println("Actual height: " + textHeight + " ; width: " + actualWidth + "; Element width: " + width);
                }
            }

            */
            /*
            else{
                while (actualWidth > (9 * maxWidth / 11) || textHeight >= (9 * maxHeight / 11)) {
                    textHeight--;
                    actualWidth--;
                    Game2D.engine.textSize(textHeight);
                    Game2D.engine.textFont(textFont);
                    textFont = Game2D.engine.createFont("Roboto", textHeight, true);
                    actualWidth = Game2D.engine.textWidth(textToBeShown);
                    System.out.println("Actual height: " + textHeight + " ; width: " + actualWidth);

                }
            }
            */




        /*
        else if (maxHeight > 0 && maxWidth <= 0){

            while (textHeight <= (8 * maxHeight / 11)) {
                textHeight++;
                Game2D.engine.textSize(textHeight);
            }
        }
        if (textHeight>height*0.68f){
            textHeight = (int)(height*0.68f);
        }
        textFont = Game2D.engine.createFont(Game2D.mainFontName, textHeight, true);
*/
        //calculateTextSize();
        //Game2D.engine.println("Text size for console: " + textHeight + "; text length: " + Game2D.engine.textWidth(textToBeShown) + " Element width: " + width);
    }






    private static String getLongestStringFromArray(ArrayList<String> text){
        String longestString = "";
        int longestStringNumber = 0;
        for (int i =1; i < text.size(); i++){
            if (text.get(i).length()>text.get(longestStringNumber).length()){
                longestStringNumber = i;
            }
        }
        longestString = text.get(longestStringNumber);
        //System.out.println("Longest string: " + longestString);
        return longestString;
    }

    /*
    public OnScreenConsole(Vec2 leftUppepCorner, int width, int textHeight){
        this.leftUppepCorner = leftUppepCorner;
        this.width = width;
        this.height = height;
        heightResizeability = false;
    }*/

    public void setText(ArrayList <String> text){
        if (!text.equals(this.text)) {
            System.out.println("Old text was: " + text.get(0) + " . Now: " + this.text.get(0));
            this.text = text;
            calculateTextSize(getLongestStringFromArray(text), width, height);
            updateDimetions();

        }
        else {
            //System.out.println("Was: " + text.get(0) + " is " + this.text.get(0));
        }
        setTimer();
    }

    public void setText(String textString){
        if (this.text == null) text = new ArrayList<>();
        else {
            if (!text.get(text.size()-1).equals(textString)){
                text.clear();
                this.text.add(textString);
                calculateTextSize(textString, width, height);
                updateDimetions();
                //System.out.println("This is new text");
            }
            else {
                //System.out.println("This text was already");
            }
        }
        setTimer();
    }

    private void setTimer(){
        if (timer == null) timer = new Timer(MIN_SHOW_TIME);
        else timer.setNewTimer(MIN_SHOW_TIME);
    }

    public int getHeight() {
        return height;
    }

    public boolean canBeTextChanged(){
        if (timer!=null){
            return timer.isTime();
        }
        else return true;
    }

    private void updateDimetions(){
        height = (int)(heightForOneLine+ (text.size()-1)*(textSpacing+textHeight));
        /*
        if (text.size() == 1) height = heightForOneLine;
        else {
            height = (int)(heightForOneLine+ (text.size()-1)*heightForOneLine);
        }

         */
        textPositionY = (int)(leftUppepCorner.y+upperFreeZoneHeight+textHeight/2);
    }

    public void update(){

    }

    public void draw(){
        Program.engine.imageMode(PConstants.CORNER);
        Program.engine.pushStyle();
        Program.engine.tint(frameColor);

        Program.engine.image(frameImage.getImage(),leftUppepCorner.x, Editor2D.leftUpperCorner.x+Editor2D.zoneHeight+height/2, width,height);
        //Game2D.engine.image(frameImage.getImage(),leftUppepCorner.x, Game2D.engine.width-2*Editor2D.leftUpperCorner.x, width,height);
        Program.engine.popStyle();
        Program.engine.imageMode(PConstants.CENTER);
        drawText();
    }

    private void drawText(){
        Program.engine.pushStyle();
        Program.engine.fill(textColor);
        Program.engine.textFont(textFont);
        Program.engine.textSize(textHeight);
        Program.engine.textAlign(PConstants.CENTER, PConstants.CENTER);
        for (int i = 0; i < text.size(); i++){
            Program.engine.text(text.get(i), leftUppepCorner.x+width/2, textPositionY+i*textHeight+textSpacing);
        }
        Program.engine.popStyle();
    }

    public ArrayList<String> getText(){
        return text;
    }

    public void recalculateFontDimension() {
        if (text != null && text.size()>0) {
            int textWidth = (int) Program.engine.textWidth(text.get(0));
            if (textWidth > width){
                while (textWidth > width){
                    textHeight--;
                    Program.engine.textSize(textHeight);
                    textWidth = (int) Program.engine.textWidth(text.get(0));
                }
                textWidth--;
                textWidth--;
            }
            else {
                while (textWidth < width){
                    textHeight++;
                    Program.engine.textSize(textHeight);
                    textWidth = (int) Program.engine.textWidth(text.get(0));
                }
                textHeight--;
                textWidth--;
            }
            if (textHeight<=0) textHeight = 10;
        }
        else textHeight = 10;
        System.out.println("New text size: " + textHeight);
    }
}
