package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PFont;

public abstract class WithFrameAnimation extends AbstractAnimationSprite{
    protected int imageWidth, imageHeight;
    protected float frameCenterX, frameCenterY;
    protected Image imageInFrame;
    protected ImageZoneSimpleData inFrameImageData;
    protected EightPartsFrameImage eightPartsFrameImage;
    protected PFont font;
    protected Timer timer;
    protected final int TIMER_TIME = 1000;
    private int fontSize;
    protected static String SEC_TEXT = " SEC";

    protected WithFrameAnimation(int x, int y, Rectangular controlZone, int lastStage, int width, SoundInGameController soundInGameController){
        super(x, y, HIDDEN+1, soundInGameController);
        this.frameCenterX = controlZone.getCenterX();
        this.frameCenterY = controlZone.getCenterY();
        initFont();
        loadLanguageSpecific();
        imageWidth = width;
        imageHeight = (int) ( UpperPanel.HEIGHT_FOR_WEAPON_FRAME);
        ImageZoneSimpleData basicRectForSimpleFrames = HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners;
        eightPartsFrameImage = new EightPartsFrameImage(basicRectForSimpleFrames, UpperPanel.BASIC_SOURCE_HEIGHT_FOR_SIMPLE_FRAMES, UpperPanel.BASIC_SOURCE_HEIGHT_FOR_SIMPLE_FRAMES, imageWidth, imageHeight, new Vec2(frameCenterX-width/2, frameCenterY-imageHeight/2));
        initGraphic();
        setFlip(false);
    }

    protected abstract void initGraphic();

    protected abstract void loadLanguageSpecific();

    private void initFont() {
        if (Program.USE_MAIN_FONT_IN_GAME) {
            font = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.mainFont));
            fontSize = (int) (UpperPanel.NORMAL_FONT_HEIGHT*0.4f);
        }
        else {
            font = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.secondaryFont));
            fontSize = (int) ((float)UpperPanel.NORMAL_FONT_HEIGHT*Program.FONTS_DIMENSION_RELATIONSHIP*0.4f);
        }
    }

    protected final void initNominalDimensions(int width, int height){
        float sourceX = inFrameImageData.rightX-inFrameImageData.leftX;
        float sourceY  = inFrameImageData.lowerY-inFrameImageData.upperY;
        float dimCoef = sourceY/sourceX;
        if (sourceX>sourceY){
            this.imageWidth = width;
            this.imageHeight = (int) (dimCoef*height);
        }
        else if (sourceY>sourceX){
            this.imageHeight = height;
            this.imageWidth = (int) (width/dimCoef);
        }
        else {
            this.imageWidth = width;
            this.imageHeight = height;
        }
    }

    @Override
    public boolean mustBeControlButtonFlipped() {
        return false;
    }
}
