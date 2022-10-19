package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.controllers.GraduallyTextAppearingController;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class AbstractTextInFrameDrawingController {
    protected String sourceString;
    protected ArrayList<String> textToBeDrawn;
    protected Rectangular textArea;
    protected PFont textFont;
    protected GraduallyTextAppearingController graduallyTextAppearingController;
    protected int textStep;

    protected void drawMessageTextFromCorner(PGraphics graphics) {
        String [] visibleText = graduallyTextAppearingController.getActualDrawnText();
        for (int j = 0; j < visibleText.length; j++) {
            graphics.text(visibleText[j], textArea.getLeftUpperX(), textArea.getLeftUpperY() + (j) * textStep + textFont.getSize() / 2);
        }
    }

    protected void drawMessageText(PGraphics graphics) {
        graduallyTextAppearingController.update();
        if (graduallyTextAppearingController.isSingleString()){
            drawMessageTextFromCenter(graphics);
        }
        else {
            drawMessageTextFromCorner(graphics);
        }
    }

    protected void drawMessageTextFromCenter(PGraphics graphics) {
        String[] visibleText = graduallyTextAppearingController.getActualDrawnText();
        for (int j = 0; j < visibleText.length; j++) {
            graduallyTextAppearingController.getFullText()[0].length();
            graphics.text(visibleText[j], (textArea.getLeftUpperX() + textArea.getWidth() / 2) - graphics.textWidth(graduallyTextAppearingController.getFullText()[0])/2, textArea.getLeftUpperY() + (j) * textStep + textFont.getSize() / 2);
        }
    }

    public final void setAppearingVelocity(float appearingVelocity){
        graduallyTextAppearingController.setAppearingVelocity(appearingVelocity);
    }

    public final float getAppearingVelocity() {
        return graduallyTextAppearingController.getAppearingVelocity();
    }

    public abstract void update();

    public void setFullShow(){
        graduallyTextAppearingController.setFullAppeared();
    }

    public Rectangular getTextArea() {
        return textArea;
    }

    public void clear() {
        graduallyTextAppearingController.setText();
    }


}
