package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

public abstract class NES_ElementWithCursor extends NES_GuiElement{
    protected int cursorPosX;
    protected int cursorPosY;
    private float cursorDimensionCoefficient;
    protected static ImageZoneSimpleData cursorImageZoneSimpleData, lockImageZoneSimpleData;

    public NES_ElementWithCursor(int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionCoefficient) {
        super(centerX, centerY, width, height, name, graphics);
        cursorPosX = (int)(leftX-textHeight*cursorDimensionCoefficient*2);
        cursorPosY = upperY+textHeight/2;
        this.cursorDimensionCoefficient = cursorDimensionCoefficient;
    }

    @Override
    protected void initGraphic(){
        if (graphicFile == null) {
            super.initGraphic();
        }
    }

    protected abstract void drawCursor(PGraphics graphics);

    public int getCursorPosX(){
        //return cursorPosX;
        return (int)(leftX-textHeight*cursorDimensionCoefficient);
        //return leftX-textHeight;
    }

    public int getCursorPosY(){
        if (Program.USE_MAIN_FONT_IN_GAME) return (upperY+textHeight/2);
        else return (int) (upperY+(textHeight/2f)/Program.FONTS_DIMENSION_RELATIONSHIP);
    }
}
