package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import processing.core.PApplet;
import processing.core.PGraphics;

public class MoneyValueController extends LineController{

    public MoneyValueController(Person soldier, int leftUpperX, int leftUpperY, PGraphics graphics){
        init();
        initBasicValues(leftUpperX, leftUpperY);
        updateActualValues((Soldier) soldier);
        stickStartsX = (int)(leftUpperX+ graphics.textWidth(prefixText));
        System.out.println("Text width: " + graphics.textWidth(prefixText)+ " with size: " + graphics.textFont.getSize());
        System.out.println("symbol width for life line: " + graphics.textFont.width('L')*graphics.textFont.getSize());
        initBlackRectData(graphics);
    }

    protected void init(){
        prefixText = "MONEY: ";
        valueForOneStick = 10;
    }

    @Override
    public void update(Soldier soldier){
        super.update(soldier);
        updateActualValues(soldier);
        //fillValuesString();
    }

    @Override
    protected void updateActualValues(Soldier soldier) {
        //maxValue = PApplet.floor(soldier.getMaxLife()/valueForOneStick);
        actualValue = PApplet.floor(soldier.getMoney());
        updatePreviousValue();
        //System.out.println("Max life value set: " + soldier.getMaxLife() + "; Life: " + soldier.getLife());
    }

    /*
    @Override
    public void draw(PGraphics graphics){
        if (actualValue>0) {
            setActiveColor(graphics);
            drawPrefixText(graphics);
            drawValueAsText(graphics);
        }

    }*/
    @Override
    public void draw(PGraphics graphics){
        if (actualValue>0) {
            if (PlayerControl.withAdoptingGuiRedrawing) {
                drawWithAdoptedRedrawing(graphics);
            }
            else drawWithRegularRedrawing(graphics);
        }
    }

    @Override
    protected void drawWithAdoptedRedrawing(PGraphics graphics) {
        if (lineMustBeRedrawnAtThisFrame) {
            drawBlackRect(graphics);
            setActiveColor(graphics);
            drawPrefixText(graphics);
            drawValueAsText(graphics);
            /*
            drawPrefixText(graphics);
            setPassiveColor(graphics);
            drawMaxValueMaxValue(graphics);
            setActiveColor(graphics);
            drawActualValue(graphics);*/
            System.out.println("Money line was redrawn");
        }
        lineMustBeRedrawnAtThisFrame = false;
    }

    @Override
    public void drawWithRegularRedrawing(PGraphics graphics){
        setActiveColor(graphics);
        drawPrefixText(graphics);
        drawValueAsText(graphics);
    }


}
