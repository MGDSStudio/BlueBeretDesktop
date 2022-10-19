package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public abstract class LineController {
    protected int maxValue, previousMaxValue = -1;
    protected int actualValue, previousValue = -1;
    protected int valueForOneStick = 1;
    protected int stickStartsX;
    protected String prefixText;
    protected String valuesString = "";
    protected String maxValuesString = "";
    protected int leftUpperX, leftUpperY;
    protected int maxVisibleSticksNumber = 30;
    protected int activeColor = 255, passiveAlpha = 90;
    protected int activeRedColor = 255;
    protected int activeYellowColor = activeRedColor;

    protected boolean lineMustBeRedrawnAtThisFrame = true;
    protected int blackRectWidth, blackRectHeight;
    private final static boolean SHOW_BLACK_RECT_CONTURE = false;

    protected final static int USE_MAIN_COLOR = 0;
    protected final static int USE_YELLOW = 1;
    protected final static int USE_RED = 2;
    protected int colorToBeUsedType = USE_MAIN_COLOR;

    protected void init() {
    }

    protected final void initBasicValues(int leftUpperX, int leftUpperY){
        this.leftUpperX = leftUpperX;
        this.leftUpperY = leftUpperY;
    }

    public void update(Soldier soldier) {
        updateColorToBeUsedType();
    }

    protected void fillValuesString(){
        int sticks = PApplet.floor(actualValue);
        if (sticks<= maxVisibleSticksNumber) {
            if (valuesString.length() != sticks) {
                valuesString = "";
                for (int i = 0; i < sticks; i++) {
                    valuesString += "|";
                }
            }
        }
        else {
            valuesString = "";
            for (int i = 0; i < maxVisibleSticksNumber; i++) {
                valuesString += "|";
            }
        }
        if (maxValue != maxValuesString.length()){
            System.out.println("Max was : " + maxValue + " string length: "+ maxValuesString.length() );
            maxValuesString = "";
            for (int i = 0; i < maxValue; i++) {
                maxValuesString += "|";
            }
            System.out.println("Recalculated : " + maxValuesString.length() );
            System.out.println("Text: " + maxValuesString);

        }
    }

    protected void initBlackRectData(PGraphics graphics){
        int prefixWidth = PApplet.ceil(graphics.textWidth(prefixText));
        int valuesWidth = PApplet.ceil(graphics.textWidth('|'))*maxVisibleSticksNumber;
        blackRectWidth = valuesWidth+prefixWidth;
        float screenProportion = ((float)Program.engine.height/(float)Program.engine.width);
        float debugScreenProportion = (float)Program.DEBUG_DISPLAY_HEIGHT_FOR_XIAOMI_PROPORTION/(float)Program.DEBUG_DISPLAY_WIDTH_FOR_XIAOMI_PROPORTION;
        //float dimCoef = 1.4f;
        float coef = ((float)Program.engine.height/(float)Program.XIAOMI_REDMI_HEIGHT);
        blackRectHeight = PApplet.ceil((graphics.textFont.getSize()*1.2f*coef));
        //blackRectHeight = PApplet.floor(dimCoef*(graphics.textFont.getSize()/(screenProportion/debugScreenProportion)));
        System.out.println("Black rect size " + blackRectWidth + "x" + blackRectHeight + "; Coef original: " + screenProportion + "; Debug: " + debugScreenProportion + " text height: " + graphics.textFont.getSize());
    }


    public void setLineMustBeRedrawnAtThisFrame(boolean lineMustBeRedrawnAtThisFrame) {
        this.lineMustBeRedrawnAtThisFrame = lineMustBeRedrawnAtThisFrame;
    }

    protected void drawWithAdoptedRedrawing(PGraphics graphics) {
        if (lineMustBeRedrawnAtThisFrame) {
            drawBlackRect(graphics);
            setColorForName(graphics);
            drawPrefixText(graphics);
            setPassiveColor(graphics);
            drawMaxValueMaxValue(graphics);
            setActiveColor(graphics);
            drawActualValue(graphics);
            //System.out.println("Life line was redrawn");
        }
        lineMustBeRedrawnAtThisFrame = false;
    }

    protected void setColorForName(PGraphics graphics) {

    }

    public void draw(PGraphics graphics){
       // update()
        if (PlayerControl.withAdoptingGuiRedrawing){
            drawWithAdoptedRedrawing(graphics);
        }
        else drawWithRegularRedrawing(graphics);
    }

    protected void drawWithRegularRedrawing(PGraphics graphics) {
        setColorForName(graphics);
        drawPrefixText(graphics);
        setPassiveColor(graphics);
        drawMaxValueMaxValue(graphics);
        setActiveColor(graphics);
        drawActualValue(graphics);
    }

    protected void updateColorToBeUsedType(){
        if (colorToBeUsedType != USE_MAIN_COLOR) colorToBeUsedType = USE_MAIN_COLOR;
    }

    protected void setPassiveColor(PGraphics graphics){
        graphics.fill(activeColor, passiveAlpha); //maybe only once?
    }

    protected void setActiveColor(PGraphics graphics){
        graphics.fill(activeColor); //maybe only once?
    }

    protected void drawActualValue(PGraphics graphics) {
        graphics.text(valuesString,stickStartsX,leftUpperY);
    }

    private void drawMaxValueMaxValue(PGraphics graphics) {
        graphics.text(maxValuesString,stickStartsX,leftUpperY);
    }

    protected void drawPrefixText(PGraphics graphics) {
        graphics.text(prefixText,leftUpperX,leftUpperY);
        //System.out.println("Text is drawn "+ this.getClass());
    }

    protected void drawBlackRect(PGraphics graphics){
        if (graphics.imageMode == PConstants.CORNER){

            graphics.image(HUD_GraphicData.mainGraphicFile.getImage(), leftUpperX,leftUpperY, blackRectWidth,blackRectHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY); //maybe only once?

            if (SHOW_BLACK_RECT_CONTURE) {
                graphics.pushStyle();
                graphics.rectMode(PConstants.CORNER);
                graphics.stroke(255);
                graphics.strokeWeight(3);
                graphics.noFill();
                graphics.rect(leftUpperX, leftUpperY, blackRectWidth, blackRectHeight);
                graphics.popStyle();
            }
        }
        else {
            graphics.image(HUD_GraphicData.mainGraphicFile.getImage(), leftUpperX+blackRectWidth/2,leftUpperY+blackRectHeight/2, blackRectWidth,blackRectHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY); //maybe only once?
            if (SHOW_BLACK_RECT_CONTURE) {
                graphics.pushStyle();
                graphics.rectMode(PConstants.CENTER);
                graphics.stroke(255);
                graphics.strokeWeight(3);
                graphics.noFill();
                graphics.rect(leftUpperX+blackRectWidth/2, leftUpperY+blackRectHeight/2, blackRectWidth, blackRectHeight);
                graphics.popStyle();
            }
        }
        /*
        if (graphics.imageMode == PConstants.CORNER){

            graphics.image(HUD_GraphicData.mainGraphicFile.getImage(), leftUpperX,leftUpperY, blackRectWidth,blackRectHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY); //maybe only once?
        }
        else graphics.image(HUD_GraphicData.mainGraphicFile.getImage(), leftUpperX+blackRectWidth/2,leftUpperY+blackRectHeight/2, blackRectWidth,blackRectHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY); //maybe only once?

         */
    }

    protected abstract void updateActualValues(Soldier soldier);

    protected void drawValueAsText(PGraphics graphics) {
        graphics.text(actualValue,stickStartsX,leftUpperY);
    }

    protected void updatePreviousValues(){
        if (maxValue != previousMaxValue || actualValue != previousValue){
            lineMustBeRedrawnAtThisFrame = true;
            System.out.println("Line must be redrawn");
        }
        previousValue = actualValue;
        previousMaxValue = maxValue;
    }

    protected void updatePreviousValue(){
        if (actualValue != previousValue){
            lineMustBeRedrawnAtThisFrame = true;
            System.out.println("Line must be redrawn");
        }
        previousValue = actualValue;
    }



}
