package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

public class NES_SimpleImage extends NES_GuiElement {
    private final Image image;

    public NES_SimpleImage(int centerX, int centerY, int width, int height, String name, PGraphics graphics, String path) {
        super(centerX, centerY, width, height, name, graphics);
        image = new Image(Program.getAbsolutePathToAssetsFolder(path));

    }

    @Override
    public void update(int mouseX, int mouseY){

    }

    @Override
    protected void updateFunction() {

    }

    @Override
    public void draw(PGraphics graphics) {
        graphics.image(image.getImage(), x, y, width, height);
    }
}
