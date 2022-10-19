package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_AnglePicker extends androidGUI_Element {
    private static Image arrow;
    private boolean graphicLoaded = false;
    private final int radius;
    private int arrowAngle = 0;
    private int distanceToArrow = 0;
    private Vec2[] basicValues;
    private int basicValuesZoneRadius;
    private byte step = 5;

    public androidAndroidGUI_AnglePicker(Vec2 pos, int w, int h) {
        super(pos, w, h, "");
        radius = (int) (9f * w / 10f);
        if (!graphicLoaded) {
            try {
                arrow = new Image(Program.getAbsolutePathToAssetsFolder("arrow.png"));
                arrow.getImage().resize(1 * radius / 2, 0);
                distanceToArrow = (int) (radius / 2.5f);
                graphicLoaded = true;
            } catch (Exception e) {
                Program.engine.println("This picture can not be founded" + e);
            }
        }
        float distToBaseCircle = 5f * w / 4f;
        float actualAngle = 0.0f;
        basicValuesZoneRadius = 8;
        basicValues = new Vec2[16];
        for (byte i = 0; i < 12; i++) {
            basicValues[i] = new Vec2((float) (distToBaseCircle * Math.cos(actualAngle)), (float) (distToBaseCircle * Math.sin(actualAngle)));
            actualAngle += PConstants.PI / 6f;
        }
        actualAngle = PConstants.PI / 4f;
        for (byte i = 12; i < basicValues.length; i++) {
            basicValues[i] = new Vec2((float) (distToBaseCircle * Math.cos(actualAngle)), (float) (distToBaseCircle * Math.sin(actualAngle)));
            actualAngle += PConstants.PI / 4f;
        }
    }

    @Override
    public int getValue(){
        //System.out.println("Value is "+ arrowAngle);
        return arrowAngle;
    }

    @Override
    public int getHeight(){
        return (int)(radius);
    }

    public void update(Vec2 relativePos) {
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if (Program.engine.dist(pos.x + relativePos.x, pos.y + relativePos.y, Program.engine.mouseX, Program.engine.mouseY) < (radius*0.8f)) {
                //if (Game2D.engine.dist(pos.x, pos.y, Game2D.engine.mouseX, Game2D.engine.mouseY)<radius) {
                if (Program.engine.mousePressed) {
                    statement = PRESSED;
                    pressedNow = true;
                    updateArrowAngle(relativePos);
                    //System.out.println("Arrow is chose");
                } else {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                    }
                    //else if (!pressedNow) statement = ACTIVE;
                }
            }
            else {
                if (statement == PRESSED && statement == RELEASED) {
                    statement = ACTIVE;
                    if (pressedNow) pressedNow = false;
                }
                for (byte i = 0; i < basicValues.length; i++) {
                    if (Program.engine.dist(Program.engine.mouseX + relativePos.x, Program.engine.mouseX + relativePos.y, basicValues[i].x, basicValues[i].y) < basicValuesZoneRadius) {
                        arrowAngle = getAngleForBasicZOne(i);
                        System.out.println("New angle is : " + arrowAngle);
                    }
                }
            }
        }
    }

    private int getAngleForBasicZOne(byte i) {
        if (i < 12) {
            return (int) (30 * i);
        } else return (int) ((i - 12) * 45);
    }

    private void updateArrowAngle(Vec2 relativePos) {
        //float newAngle = GameMechanics.angleDetermining(pos.x+relativePos.x, pos.y+relativePos.y, Game2D.engine.mouseX, Game2D.engine.mouseY);

        float newAngle = GameMechanics.angleDetermining(Program.engine.mouseX, Program.engine.mouseY, pos.x + relativePos.x, pos.y + relativePos.y);
        if (step != 1){
            float rest = newAngle%step;
            if (rest > (step/2.0f)) newAngle+=step-rest;
            else newAngle-=rest;
        }
        System.out.println("New angle is " + newAngle);
        arrowAngle = (int) newAngle;
    }


    private void drawText(PGraphics graphic){
        int angle = 0;
        byte number = 8;
        graphic.textAlign(PConstants.CENTER);
        int distanceToText = (int)(elementWidth/1.5f);
        calculateTextSize(elementWidth/0.85f, elementHeight/0.85f);
        graphic.fill(0xffFF0A0A);
        graphic.textSize(textHeight);
        graphic.textFont(textFont);
        graphic.fill(0xffFF0A0A);
        for (byte i = 0; i < number; i++){
            graphic.point(distanceToText*PApplet.cos(PApplet.radians(angle)),distanceToText*PApplet.sin(PApplet.radians(angle))+ Program.engine.width/111f);
            graphic.text(Integer.toString(angle), distanceToText*PApplet.cos(PApplet.radians(angle)),distanceToText*PApplet.sin(PApplet.radians(angle)));
            angle+=(360/number);
        }
    }

    public void draw(PGraphics graphic) {
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        graphic.translate(pos.x, pos.y);
        graphic.pushStyle();
        graphic.stroke(25);
        graphic.strokeWeight(5);
        graphic.point(0, 0);
        graphic.rotate(PApplet.radians(arrowAngle));
        graphic.image(arrow.getImage(), distanceToArrow, 0);
        graphic.rotate(PApplet.radians(-arrowAngle));
        drawText(graphic);
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
        //drawName(graphic, PApplet.CENTER);
    }
}
