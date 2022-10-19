package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_Scrollbar extends androidGUI_Element {
    private static Image picture;
    private int arrowHeight;
    private int startScrollerCenter, endScrollerCenter;
    private int scrollerHeight;

    public androidAndroidGUI_Scrollbar(Vec2 pos, int w, int h, String name, int maxWay) {
        super(pos, w, h, name);
        if (!graphicLoaded){
            if (picture == null) {
                picture = new Image(Program.getAbsolutePathToAssetsFolder("GUI_scrollbar.png"));
                picture.getImage().resize(w,0);
            }
            graphicLoaded = true;
        }
        arrowHeight = w;

    }

    @Override
    public void update(Vec2 relativePos){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Program.engine.mousePressed) {
                    statement = PRESSED;
                    pressedNow = true;
                }
                else {
                    if (pressedNow == true) {
                        pressedNow = false;
                        statement = RELEASED;
                    }
                }
            }
            else if (statement == PRESSED && statement == RELEASED) {
                statement = ACTIVE;
                if (pressedNow) pressedNow = false;
            }
        }
    }

    @Override
    public void draw(PGraphics graphic){
        graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();

        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        graphic.endDraw();
        //drawName(graphic, PApplet.CENTER);
    }

    private void drawArrows(){

    }
}
