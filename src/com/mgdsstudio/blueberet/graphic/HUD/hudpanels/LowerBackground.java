package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PGraphics;

public class LowerBackground {
    public final int height;
    private boolean actualVisibilityStatement = OnScreenButton.VISIBLE;
    private boolean prevVisibilityStatement = !OnScreenButton.VISIBLE;
    private boolean regularRedrawn;

    public LowerBackground(int height) {
        this.height = height;
    }

    public void draw(PGraphics graphics){
        drawBackground(graphics);
    }

    public void clearPanel(PGraphics graphics){
        if (graphics.imageMode == PConstants.CORNER) graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), 0, Program.engine.height - height, Program.engine.width, height, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
        else graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width/2, Program.engine.height - height/2, Program.engine.width, height, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
        /*graphics.rectMode(PConstants.CENTER);
        graphics.fill(255,25,25);
        graphics.rect(Program.engine.width/2, Program.engine.height - height/2, Program.engine.width, height);*/

        //System.out.println("Background was cleared for lower panel");
    }

    private void drawBackground(PGraphics graphics){
        if (PlayerControl.withAdoptingGuiRedrawing && !regularRedrawn){
            if (prevVisibilityStatement != actualVisibilityStatement){
                if (graphics.imageMode == PConstants.CORNER) graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), 0, Program.engine.height - height, Program.engine.width, height, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                else graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width/2, Program.engine.height - height/2, Program.engine.width, height, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            }
        }
        else {
            graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width / 2, Program.engine.height - height / 2, Program.engine.width, height, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
        }
        actualVisibilityStatement = prevVisibilityStatement;
        /*
        graphics.pushStyle();
        graphics.noStroke();
        graphics.rectMode(PConstants.CORNER);
        graphics.fill(0);
        graphics.rect(0, Program.engine.height-height, Program.engine.width, Program.engine.height);
        graphics.popStyle();*/

    }

    public void setRegularRedrawn(boolean regularRedrawn) {
        this.regularRedrawn = regularRedrawn;
    }


    public void deleteAllFromThePanel() {
        prevVisibilityStatement = false;
        actualVisibilityStatement = true;
    }

    public boolean isMouseOnLowerBackground() {
        if (Program.engine.mouseX>0 && (Program.engine.mouseX<Program.engine.width)){
            if ((Program.engine.mouseY>(Program.engine.height-height) && (Program.engine.mouseY<Program.engine.height))){
                return true;
            }
        }
        return false;
    }
}
