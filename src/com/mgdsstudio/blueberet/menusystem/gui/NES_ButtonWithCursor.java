package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class NES_ButtonWithCursor extends NES_ElementWithCursor {
    public final static int BUTTON_NORMAL_WIDTH = (int) (Program.engine.width/1.5f);
    private int blinkTimes = 3;
    private boolean actualShownByBlinking = true;

    private final static float CURSOR_DIMENSIONS_COEF = 1.2f/Program.FONTS_DIMENSION_RELATIONSHIP;
    private boolean alignmentInCenterAlongY;

    public NES_ButtonWithCursor(int centerX, int centerY, int width, int height, String name, PGraphics graphics) {
        super(centerX, centerY, width, height, name, graphics, CURSOR_DIMENSIONS_COEF);
        //blinkTimer = new Timer();
    }

    @Override
    public void draw(PGraphics graphics){
        if (!hidden) {
            super.draw(graphics);
            drawCursor(graphics);
            if (isVisible()) {
                drawName(graphics);
            }

            //else System.out.println("Button is invisible " + name);
            //drawDebugRect(graphics);
        }
        //else System.out.println("Button is hidden " + name);
        //graphics.pus
        //graphics.rect(x,y,width,height);
    }

    public int getBlinkTimes(){
        return blinkTimes;
    }

    @Override
    protected void initGraphic(){
        super.initGraphic();
        if (cursorImageZoneSimpleData == null){
            cursorImageZoneSimpleData = GameMenusController.playerHead;
            lockImageZoneSimpleData = GameMenusController.lock;
        }
    }

    @Override
    protected void updateFunction() {
        /*
        if (actualStatement == RELEASED){
            if (blinkTimer == null){
                blinkTimer = new Timer(BLINK_TIME);
            }
            if (blinkTimer.isTime()){
                if (!actualShownByBlinking) blinkTimes++;
                //actualShownByBlinking = !actualShownByBlinking;
                blinkTimer.setNewTimer(BLINK_TIME);
            }
        }*/
    }

    @Override
    protected void drawCursor(PGraphics graphics){
        if (actualStatement == PRESSED || (actualShownByBlinking && actualStatement == RELEASED) || actualStatement == BLOCKED) {
            if (graphics != null) {
                graphics.pushStyle();
                graphics.imageMode(PApplet.CENTER);
                if (actualStatement == BLOCKED){
                    graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*textHeight, CURSOR_DIMENSIONS_COEF*textHeight, lockImageZoneSimpleData.leftX, lockImageZoneSimpleData.upperY, lockImageZoneSimpleData.rightX, lockImageZoneSimpleData.lowerY);
                }
                else {
                    graphics.image(graphicFile.getImage(),  getCursorPosX(), getCursorPosY(), CURSOR_DIMENSIONS_COEF*textHeight, CURSOR_DIMENSIONS_COEF*textHeight, cursorImageZoneSimpleData.leftX, cursorImageZoneSimpleData.upperY, cursorImageZoneSimpleData.rightX, cursorImageZoneSimpleData.lowerY);
                }
                graphics.popStyle();
            }
        }

    }

    public void setActualShownByBlinking(boolean actualShownByBlinking) {
        //if (actualShownByBlinking) System.out.println("Cursor for this button is visible now");
        //else  System.out.println("Cursor for this button is not more visible");
        this.actualShownByBlinking = actualShownByBlinking;
    }


    @Override
    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        super.setAnotherTextToBeDrawnAsName(anotherTextToBeDrawnAsName);
        System.out.println("Dimensions for button must be recalculated");
        float coef = 1f;
        if (anotherTextToBeDrawnAsName.contains("\n")){
            int deviderPlace = anotherTextToBeDrawnAsName.indexOf("\n");
            if (deviderPlace >0) {
                int lengthBefore = deviderPlace-1;
                int lengthAfter = anotherTextToBeDrawnAsName.length()-1-(deviderPlace+1);
                int maxLength = lengthBefore;
                if (lengthAfter > lengthBefore) maxLength = lengthAfter;
                coef = (float) maxLength / (float) name.length();
            }
        }
        else {
            coef = (float) anotherTextToBeDrawnAsName.length() / (float) name.length();
            if (coef > 1) {

            }

        }
        setWidth((int) ((float) width * coef));
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
}
