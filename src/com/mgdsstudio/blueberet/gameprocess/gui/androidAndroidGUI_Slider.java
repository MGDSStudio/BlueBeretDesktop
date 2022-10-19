package com.mgdsstudio.blueberet.gameprocess.gui;


import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_Slider extends androidGUI_Element {
    private androidAndroidGUI_TextField textField;
    //private final PFont textFont;
    private int actualCirclePositionX;
    private static Image picturePressed, pictureActive;

    private boolean graphicLoaded = false;
    private int minValue = 200;
    private int maxValue = 2960;
    private int actualValue = 0;
    private final byte LEFT_PART = 1;
    private final byte RIGHT_PART = 2;
    private final byte CIRCLE = 0;
    private String minValueString;
    private String maxValueString;
    private Vec2 minValueTextPos;
    private Vec2 maxValueTextPos;
    private int deadZone = 5;
    private boolean curveFunction = true;
    private float circleDiameter;


    public androidAndroidGUI_Slider(Vec2 pos, int w, int h, String name, int minValue, int maxValue) {
        super(pos, w, h, "");
        this.maxValue = maxValue;
        this.minValue = minValue;
        if (!graphicLoaded) {
            try {
                /*

                pictureActive = new Image(Programm.getRelativePathToTextures() + "GUI_Slider.png");
                picturePressed = new Image(Programm.getRelativePathToTextures() + "GUI_SliderPressed.png");
                 */
                pictureActive = new Image(Program.getAbsolutePathToAssetsFolder("GUI_Slider.png"));
                picturePressed = new Image(Program.getAbsolutePathToAssetsFolder("GUI_SliderPressed.png"));
                graphicLoaded = true;
            } catch (Exception e) {
                System.out.println("This picture can not be founded" + e);
            }
        }
        minValueString = new String();
        maxValueString = new String();
        minValueString += minValue;
        maxValueString += maxValue;
        minValueTextPos = new Vec2(pos.x - elementWidth / 2, pos.y + elementHeight / 1.65f);
        maxValueTextPos = new Vec2(pos.x + elementWidth / 2, pos.y + elementHeight / 1.65f);
        String longestString = "";
        longestString += maxValue;
        calculateTextSize(longestString, elementHeight, elementHeight);
        deadZone = (int) (elementWidth / 20f);
        //actualValue = (minValue+maxValue)/2;
        actualValue = minValue;
        //System.out.println("Actual value : " + actualValue);

        this.name = name;
        circleDiameter = elementHeight / 2f;
        actualCirclePositionX = (int) (pos.x - elementWidth / 2);
    }

    public boolean hasTextField() {
        if (textField == null) return false;
        else return true;
    }

    public void addCoppeledTextField(androidAndroidGUI_TextField textField) {
        this.textField = textField;
    }

    @Override
    public void setText(String text) {
        maxValueString = text;
    }

    public void setText(String text, boolean forMinValue) {
        if (forMinValue) minValueString = text;
        else maxValueString = text;
    }

    public void updateValueFromTextField() {
        if (textField.getStatement() == PRESSED) {
            int newValue = textField.getDigitIntegerValue();
            if (newValue >= minValue && newValue <= maxValue) {
                updateCirclePositionByValue();
                actualValue = newValue;
            }
        }
    }

    private void setDataOnTextField() {
        if (textField.getStatement() != PRESSED) {
            textField.setValue(actualValue);
            /*
            int newValue = textField.getDigitValue();
            if (newValue >= minValue && newValue <= maxValue) {
                actualValue = newValue;
                updateCirclePosition();
            }*/
        }
    }

    @Override
    public void update(Vec2 relativePos) {
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 4) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 4) + relativePos.y))) {
                if (Program.engine.mousePressed) {
                    statement = PRESSED;
                    pressedNow = true;
                    updateCirclePosition();
                    setNewValue(relativePos);
                    if (textField != null) setDataOnTextField();
                }
                else if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                }
                else if (!Program.engine.mousePressed && !Editor2D.prevMousePressedStatement && statement == RELEASED) statement = ACTIVE;
            }
            if (textField != null) updateValueFromTextField();
        }
    }


    public void oldUpdate(Vec2 relativePos) {
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 4) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 4) + relativePos.y))) {
                if (Program.engine.mousePressed) {
                    statement = PRESSED;
                    pressedNow = true;
                    updateCirclePosition();
                    setNewValue(relativePos);
                    if (textField != null) setDataOnTextField();
                }
                else {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                    }
                }
                if (!Program.engine.mousePressed && !Editor2D.prevMousePressedStatement && statement == RELEASED) statement = ACTIVE;
            }
            if (textField != null) updateValueFromTextField();
            else if (statement == PRESSED && statement == RELEASED) {
                statement = ACTIVE;
                if (pressedNow) pressedNow = false;
            }
        }
    }

    @Override
    public int getValue() {
        //System.out.println("value is "+ actualValue);
        if (!curveFunction) return actualValue;
        else {
            return actualValue;
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setValue(int newValue) {
        actualValue = newValue;
        updateCirclePositionByValue();
        //System.out.println("Actual value is " + actualValue);
    }

    @Override
    public void setUserValue(int newValue){
        setValue(newValue);
    }

    public void setMinValue() {
        actualValue = minValue;
        actualCirclePositionX = (int) (pos.x - elementWidth / 2);
    }

    public void draw(PGraphics graphic) {
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        /*if (statement == ACTIVE){

        }*/
        if (statement == PRESSED) {
            drawPicture(graphic, picturePressed, LEFT_PART);
            drawPicture(graphic, picturePressed, RIGHT_PART);
            drawPicture(graphic, picturePressed, CIRCLE);
        } else {
            //System.out.println("There are no another graphics for scroller");
            drawPicture(graphic, pictureActive, LEFT_PART);
            drawPicture(graphic, pictureActive, RIGHT_PART);
            drawPicture(graphic, pictureActive, CIRCLE);
            //graphic.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
        }
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
        drawNames(graphic);
        //drawName(graphic, PApplet.CENTER);
    }

    private void drawNames(PGraphics graphic) {
        if (graphic != null) {
            //graphic.beginDraw();
            graphic.pushStyle();
            graphic.fill(0xff1040E8);
            graphic.textSize(textHeight);
            graphic.textAlign(PConstants.CENTER, PConstants.CENTER);
            graphic.text(minValueString, minValueTextPos.x, maxValueTextPos.y);
            graphic.text(maxValueString, maxValueTextPos.x, maxValueTextPos.y);
            //graphic.textAlign(PConstants.CENTER, PConstants.CENTER);
            graphic.text(getValue(), actualCirclePositionX, pos.y - circleDiameter * 1.15f);
            graphic.popStyle();
            //graphic.endDraw();
        }
    }

    private void drawPicture(PGraphics graphic, Image image, byte picturePart) {
        //System.out.println("Actual circle pos: " + actualCirclePositionX);
        if (picturePart == CIRCLE) {
            graphic.imageMode(PConstants.CENTER);
            //graphic.tint(200,0,0);
            graphic.image(image.getImage(), actualCirclePositionX, pos.y, circleDiameter, circleDiameter * 2, 118, 0, 129, 44);
        } else if (picturePart == LEFT_PART) {
            graphic.imageMode(PConstants.CORNER);
            // graphic.image(image.getImage(), pos.x-elementWidth/2, pos.y-elementHeight/2, elementWidth*actualValue/maxValue, elementHeight, 0, 0, 117, 44);
            graphic.image(image.getImage(), pos.x - elementWidth / 2, pos.y - elementHeight / 2, elementWidth, elementHeight, 0, 0, 117, 44);
        }
        /*
        else if (picturePart == RIGHT_PART){
            graphic.imageMode(PConstants.CORNER);
            graphic.image(image.getImage(), pos.x+actualCirclePositionX - elementWidth/2, pos.y-elementHeight/2, elementWidth-(elementWidth*actualValue/maxValue), elementHeight, 130, 0, 245, 44);
        }*/
    }

    private void updateCirclePosition() {

        updateCirclePositionByValue();
    }

    private void updateCirclePositionByValue() {
        float leftEndPos = (pos.x - (elementWidth / 2));
        float wayFromLeftPos = PApplet.abs(actualValue-minValue) * elementWidth / (maxValue-minValue);
        actualCirclePositionX = (int) (leftEndPos + wayFromLeftPos);

    }


    /*
    maxValue = width
    actualValue = maxCircle = */

    private void setNewValue(Vec2 relativePos) {
        float partValue = (Program.engine.mouseX-relativePos.x-pos.x+(elementWidth/2f))/(float)elementWidth;
        float leftEndPos = (pos.x-(elementWidth/2)+relativePos.x);
        float distanceToNearestEnd = PApplet.abs((Program.engine.mouseX-leftEndPos));
        if (distanceToNearestEnd<(deadZone)){
            actualValue = (int)(minValue);
        }
        else {
            float rightEndPos = (pos.x+(elementWidth/2)+relativePos.x);
            distanceToNearestEnd = PApplet.abs((rightEndPos- Program.engine.mouseX));
            if (distanceToNearestEnd<(deadZone)){
                System.out.println("Max value");
                actualValue = (maxValue);
            }
            else {
                actualValue = (int)(partValue*(maxValue-minValue)+minValue);
                System.out.println("Actual value: " + actualValue + "; Part value: " + partValue);
            }
        }

        /*
        float leftEndPos = (pos.x - (elementWidth / 2) + relativePos.x);
        float partValue = (Programm.engine.mouseX - relativePos.x - pos.x + (elementWidth / 2f)) / (float) elementWidth;
        actualValue = (int) (partValue * (maxValue - minValue));
        float fromLeftEnd = (actualCirclePositionX - leftEndPos);
        actualValue = (int) (fromLeftEnd * (maxValue - minValue) / elementWidth) + minValue;

        */
    }

    @Override
    public void setPosition(Vec2 pos){
        System.out.println("Pos is null: " + (pos==null));
        System.out.println("Min pos was: " + minValueTextPos) ;
        Vec2 shifting = this.pos.sub(pos);
        System.out.println("Shifting = " + shifting);
        super.setPosition(pos);
        if (shifting == null) System.out.println("Shifting is null");
        else if (textPos == null) System.out.println("Text pos is null");

        minValueTextPos = minValueTextPos.sub(shifting);
        maxValueTextPos = maxValueTextPos.sub(shifting);
        System.out.println("Min pos is: " + minValueTextPos) ;
    }

}
