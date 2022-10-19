package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import processing.core.PApplet;
import processing.core.PGraphics;

public class LifeLineValueController extends LineController{
    //private int maxLifeValue = 9999;
    protected int prevColorType;
    public final static float CRITICAL_VALUE_TO_BE_YELLOW = 0.35f;

    public LifeLineValueController(Person soldier, int leftUpperX, int leftUpperY, PGraphics graphics){
        init();
        initBasicValues(leftUpperX, leftUpperY);
        updateActualValues((Soldier) soldier);
        stickStartsX = (int)(leftUpperX+ graphics.textWidth(prefixText));
        System.out.println("Text width: " + graphics.textWidth(prefixText)+ " with size: " + graphics.textFont.getSize());
        System.out.println("symbol width for life line: " + graphics.textFont.width('L')*graphics.textFont.getSize());
        initBlackRectData(graphics);
    }



    protected void init(){
        prefixText = "LIFE: ";
        valueForOneStick = 10;
    }

    @Override
    public void update(Soldier soldier){
        super.update(soldier);
        updateActualValues((Soldier) soldier);
        fillValuesString();
        if (prevColorType != colorToBeUsedType) setLineMustBeRedrawnAtThisFrame(true);
    }

    @Override
    protected void updateActualValues(Soldier soldier) {
        maxValue = PApplet.floor(soldier.getMaxLife()/valueForOneStick);
        actualValue = PApplet.floor(soldier.getLife()/valueForOneStick);
        updatePreviousValues();
    }

    @Override
    protected void updateColorToBeUsedType(){
        prevColorType = colorToBeUsedType;

        if ((((float)actualValue)/(float)maxValue)<0.15f) {
            colorToBeUsedType = USE_RED;
        }
        else if ((((float)actualValue)/(float)maxValue)<CRITICAL_VALUE_TO_BE_YELLOW) {
            colorToBeUsedType = USE_YELLOW;
        }
        else {
            colorToBeUsedType = USE_MAIN_COLOR;
        }
        //System.out.println("Max: " + maxValue + " actual " + actualValue + " rel: " + (((float)actualValue)/(float)maxValue) + " Type " + colorToBeUsedType);
        /*
        if (actualValue < 20 && (((float)actualValue)/(float)maxValue)<0.25f) {
            colorToBeUsedType = USE_YELLOW;
        } else if (actualValue < 10 && (((float)actualValue)/(float)maxValue)<0.1f) {
            colorToBeUsedType = USE_RED;
        } else {
            colorToBeUsedType = USE_MAIN_COLOR;
        }
        System.out.println("Max: " + maxValue + " actual " + actualValue + " rel: " + (((float)actualValue)/(float)maxValue));
         */
    }


    @Override
    protected void setPassiveColor(PGraphics graphics){
        if (colorToBeUsedType == USE_MAIN_COLOR) graphics.fill(activeColor, passiveAlpha); //maybe only once?
        else if (colorToBeUsedType == USE_YELLOW) graphics.fill(activeYellowColor, activeYellowColor,0, passiveAlpha); //maybe only once?
        else if (colorToBeUsedType == USE_RED) graphics.fill(activeRedColor,0,0, passiveAlpha); //maybe only once?
    }



    protected void setColorForName(PGraphics graphics) {
        if (colorToBeUsedType == USE_MAIN_COLOR) graphics.fill(activeColor); //maybe only once?
        else if (colorToBeUsedType == USE_YELLOW) graphics.fill(activeYellowColor, activeYellowColor,0); //maybe only once?
        else if (colorToBeUsedType == USE_RED) graphics.fill(activeRedColor,0,0); //maybe only once?
    }
}
