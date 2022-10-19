package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

abstract class FrameController {
    protected EightPartsFrameImage mainFrame;
    protected EightPartsFrameImage secondaryFrame;
    public final static int DRAW_IN_FRAME_CENTER = 0;

    public abstract void draw(PGraphics graphics, int graphicMethod);


    public abstract void clickOnFrame(GameRound gameRound);

    public abstract Vec2 getLeftUpperCornerForFullOpened();

    public abstract boolean isFrameClicked();


    //public abstract WeaponType getSelectedObject();
}
