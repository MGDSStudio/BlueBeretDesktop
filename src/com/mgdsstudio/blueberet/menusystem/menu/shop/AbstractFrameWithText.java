package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

abstract class AbstractFrameWithText {
    protected Vec2 leftUpperCorner;
    protected TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;

    protected EightPartsFrameImage frame;
    protected final static int ACTIVE = 0;
    protected final static int PRESSED = 1;
    protected final static int RELEASED = 2;
    protected int statement = ACTIVE;
    protected Timer timerBeforeFirstClick;

    protected boolean visible = true;

    public boolean isClicked(PApplet engine) {
        if (statement == RELEASED){
            return true;
        }
        else return false;
    }

    public EightPartsFrameImage getFrame() {
        return frame;
    }




    public void setText(String text, boolean alignmentAlongWidth) {
        textInSimpleFrameDrawingController.setNewText(text, alignmentAlongWidth);
    }

    public boolean isFullApeared() {
        return textInSimpleFrameDrawingController.isFullShown();
    }

    public TextInSimpleFrameDrawingController getTextInSimpleFrameDrawingController() {
        return textInSimpleFrameDrawingController;
    }

    public void draw(PGraphics graphics){
        if (visible) {
            frame.draw(graphics);

            if (textInSimpleFrameDrawingController != null) {
                textInSimpleFrameDrawingController.draw(graphics);
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
