package com.mgdsstudio.blueberet.menusystem.gui;

import processing.core.PApplet;
import processing.core.PGraphics;

public class NES_ButtonWithFrameSelection extends NES_ElementWithFrameSelection{

    public NES_ButtonWithFrameSelection(int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        init(centerX, centerY);
    }


    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        if (actualStatement == PRESSED || actualStatement == RELEASED) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            drawCursor(graphics);
            graphics.popStyle();
        }
        drawName(graphics);
    }

    @Override
    protected void updateFunction() {
    }
}
