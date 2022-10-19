package com.mgdsstudio.blueberet.menusystem.gui;

import processing.core.PConstants;
import processing.core.PGraphics;

public class NES_CheckBox extends NES_GuiElement{
    private boolean flagSet;

    public NES_CheckBox(int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(centerX, centerY, width, height, name, graphics);
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
    }
    @Override
    protected void updateFunction() {

    }

    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {
            super.draw(graphic);
            drawName(graphic, PConstants.CENTER);
        }
    }
}
