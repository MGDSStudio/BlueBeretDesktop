package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public class androidAndroidGUI_NES_StyleButton extends androidAndroidGUI_Button {
    //private static StaticSprite leftSideCursor;
    private static Image leftSideCursor;
    //private ImageZoneFullData cursorImageZoneFullData;
    private ImageZoneSimpleData cursorImageZoneSimpleData;
    private Timer blinkTimer;
    private final int blinkTime = 250;
    private int blinkTimes = 3;
    private boolean actualShownByBlinking = true;
    private boolean cursorLoaded;
    private final float cursorDimensionCoefficient = 1.2f;

    public androidAndroidGUI_NES_StyleButton(Vec2 pos, int w, int h, String name, boolean withFixation) {
        super(pos, w, h, name, withFixation);
        createFont();
        normalTextColor = Program.engine.color(255);
        blinkTimer = new Timer();
        loadCursorGraphic();
    }

    private void loadCursorGraphic(){
        if (!cursorLoaded){
            //HUD_GraphicData.playerFace;
            leftSideCursor = HeadsUpDisplay.mainGraphicSource;
            cursorImageZoneSimpleData = HUD_GraphicData.playerFaceFullLife;
            if (leftSideCursor.getImage() != null) cursorLoaded= true;
        }
    }

    private void createFont() {
        textFont = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.mainFont));
        textHeight = Program.engine.width/23;
    }

    @Override
    public void update(Vec2 relativePos){
        super.update(relativePos);
        if (statement == RELEASED){
            if (blinkTimer == null){
                blinkTimer = new Timer(blinkTime);
            }
            if (blinkTimer.isTime()){
                if (!actualShownByBlinking) blinkTimes++;
                actualShownByBlinking = !actualShownByBlinking;
                blinkTimer.setNewTimer(blinkTime);
            }
        }
    }

    @Override
    public void draw(PGraphics graphics){
        drawCursor(graphics);
        drawName(graphics, PApplet.CENTER);
    }

    public int getBlinkTimes(){
        return blinkTimes;
    }

    private void drawCursor(PGraphics graphics){
        if (statement == PRESSED && actualShownByBlinking) {
            if (graphics != null) {
                graphics.imageMode(PApplet.CENTER);
                graphics.image(leftSideCursor.getImage(),  textPos.x - 3f * elementWidth / 7f, textPos.y, cursorDimensionCoefficient*textHeight, cursorDimensionCoefficient*textHeight, cursorImageZoneSimpleData.leftX, cursorImageZoneSimpleData.upperY, cursorImageZoneSimpleData.rightX, cursorImageZoneSimpleData.lowerY);
            }
            else {
                Program.engine.pushStyle();
                Program.engine.imageMode(PApplet.CENTER);
                Program.engine.image(leftSideCursor.getImage(),  textPos.x - 3f*elementWidth / 7f, textPos.y, cursorDimensionCoefficient*textHeight, cursorDimensionCoefficient*textHeight, cursorImageZoneSimpleData.leftX, cursorImageZoneSimpleData.upperY, cursorImageZoneSimpleData.rightX, cursorImageZoneSimpleData.lowerY);
                Program.engine.popStyle();
            }

        }

    }
}
