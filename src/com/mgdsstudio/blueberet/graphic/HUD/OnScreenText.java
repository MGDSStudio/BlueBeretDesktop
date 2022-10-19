package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PGraphics;

public class OnScreenText {
    private boolean graphicRenderer;
    private int x,y;
    //private String text;
    private int textSize;
    private int color;
    private int blackRectWidth = -1;

    public OnScreenText(int x, int y, int color, int textSize){
        this.x = x;
        this.y = y;
        this.color = color;
        if (Program.USE_MAIN_FONT_IN_GAME) this.textSize = textSize;
        else this.textSize = (int)((float)textSize*Program.FONTS_DIMENSION_RELATIONSHIP);
    }


    public void draw(PGraphics graphic, String text) {
        if (PlayerControl.withAdoptingGuiRedrawing){
            graphic.pushStyle();
            graphic.fill(color);
            graphic.textAlign(PConstants.CORNER, PConstants.CENTER);
            graphic.textSize(textSize);
            if (text != null) {
                if (blackRectWidth > 0 ) {
                    graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(),x+blackRectWidth/2, y, blackRectWidth, textSize, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                }
                else {
                    blackRectWidth = Program.engine.ceil(graphic.textWidth(text+ "    "));
                    System.out.println("Black rect text width: " + blackRectWidth) ;
                }
                graphic.text(text, x, y);
            }
            graphic.popStyle();
        }
        else {
            graphic.pushStyle();
            graphic.fill(color);
            graphic.textAlign(PConstants.CORNER, PConstants.CENTER);
            graphic.textSize(textSize);
            if (text != null) graphic.text(text, x, y);
            graphic.popStyle();
        }
    }

    public void draw(String text) {

            Program.engine.pushStyle();
            Program.engine.fill(color);
            Program.engine.textAlign(PConstants.CORNER, PConstants.CENTER);
            Program.engine.textSize(textSize);
            if (text != null) {
                Program.engine.text(text, x, y);
            }
            Program.engine.popStyle();
        }


}
