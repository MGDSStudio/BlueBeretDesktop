package com.mgdsstudio.blueberet.menusystem.gui;

import processing.core.PConstants;
import processing.core.PGraphics;

public class NES_TextLabel extends NES_GuiElement{


    public NES_TextLabel(int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(centerX, centerY, width, height, name, graphics);
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
    }



    @Override
    public void draw(PGraphics graphic) {
        if (actualStatement != BLOCKED) {
            super.draw(graphic);
            drawName(graphic, PConstants.CENTER);
        }
    }

    @Override

    public void update(int mouseX, int mouseY){
        //super.update(mouseX, mouseY);
        //Nothing to update
    }


    @Override
    protected void updateFunction() {
        // Npthing to update
    }
}
