package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

public abstract class NES_ElementWithFrameSelection extends NES_ElementWithCursor{
    protected static ImageZoneSimpleData cursorImageZoneSimpleData;
    protected final static float CURSOR_DIMENSIONS_COEF = 1.3f;
    protected EightPartsFrameImage frame;

    public NES_ElementWithFrameSelection(int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionCoefficient) {
        super(centerX, centerY, width, height, name, graphics, cursorDimensionCoefficient);
    }

    protected void init(int centerX, int centerY) {
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f*(Program.engine.width * NES_SCREEN_X_RESOLUTION)/Program.engine.width);
        int frameWidth = (int) (width*CURSOR_DIMENSIONS_COEF);
        int frameHeight = (int) (height*CURSOR_DIMENSIONS_COEF);
        frame = new EightPartsFrameImage(GameMenusController.sourceFile, cursorImageZoneSimpleData, basicWidth, basicWidth, frameWidth, frameHeight, new Vec2(centerX-frameWidth/2, centerY-frameHeight/2));

    }

    @Override
    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        super.setAnotherTextToBeDrawnAsName(anotherTextToBeDrawnAsName);
        System.out.println("Dimensions for frame must be recalculated");
        float coef = (float)anotherTextToBeDrawnAsName.length()/(float)name.length();
        frame.setWidth((int)((float)frame.getWidth()*coef));
        /*
        final int NES_SCREEN_X_RESOLUTION = 254;
        int basicWidth = (int) (0.2f*(Program.engine.width * NES_SCREEN_X_RESOLUTION)/Program.engine.width);
        float coef = (float)anotherTextToBeDrawnAsName.length()/(float)name.length();
        int frameWidth = (int) (coef*(width*CURSOR_DIMENSIONS_COEF));
        int frameHeight = (int) (coef*(height*CURSOR_DIMENSIONS_COEF));
        int centerX = (int) (frame.getLeftUpperCorner().x+frame.getWidth()/2);
        int centerY = (int) (frame.getLeftUpperCorner().x+frame.getWidth()/2);
        frame = new EightPartsFrameImage(GameMenusController.sourceFile, cursorImageZoneSimpleData, basicWidth, basicWidth, frameWidth, frameHeight, new Vec2(centerX-frameWidth/2, centerY-frameHeight/2));
*/
    }

    @Override
    protected void initGraphic(){
        super.initGraphic();
        if (cursorImageZoneSimpleData == null){
            cursorImageZoneSimpleData = GameMenusController.simpleFrameZone;
        }
    }

    @Override
    protected void drawCursor(PGraphics graphics){
        if (actualStatement == PRESSED) {
            if (graphics != null) {
                graphics.resetMatrix();
                frame.draw(graphics);
                //System.out.println("Drawn");
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY){
        if (prevStatement != actualStatement) prevStatement = actualStatement;
        if (actualStatement != BLOCKED && actualStatement != HIDDEN){
            if (GameMechanics.isPointInRect(mouseX, mouseY, leftX, upperY, frame.getWidth(), frame.getHeight())){
                if (Program.engine.mousePressed){
                    if (actualStatement != PRESSED) actualStatement = PRESSED;
                }
                else if (actualStatement == PRESSED){
                    actualStatement = RELEASED;
                }
                else if (actualStatement == RELEASED ){
                    actualStatement = ACTIVE;
                }
            }
            else{
                actualStatement = ACTIVE;
            }
        }
        //updateLongPressingTimer();
        updateFunction();
    }

}
