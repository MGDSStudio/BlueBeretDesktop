package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PGraphics;

public class BulletsValueController extends LineController {
    private WeaponType weaponType;
    private boolean textPosMustBeUpdated;
    protected int prevColorType;



    public BulletsValueController(Soldier soldier, int leftUpperX, int leftUpperY, PGraphics graphics){
        weaponType = soldier.getActualWeapon().getWeaponType();
        init();
        initBasicValues(leftUpperX, leftUpperY);
        updateActualValues(soldier);
        int additionalSpace = (int) graphics.textWidth(' ');
        stickStartsX = (int)(leftUpperX+ graphics.textWidth(prefixText)+additionalSpace);
        System.out.println("Text width: " + graphics.textWidth(prefixText)+ " with size: " + graphics.textFont.getSize());
        System.out.println("Symbol width for bullets:  " + graphics.textFont.width('L')*graphics.textFont.getSize());
        initBlackRectData(graphics);
    }

    protected void init(){
        //weaponType.name();
        //prefixText = weaponType.name() + ":";
        prefixText = weaponType.name() + ":";
        if (weaponType.name().contains("_")){
            //if (weaponType.name().contains("SAW")){
                prefixText = "SAWED-OFF:";
            //}
        }
        System.out.println("Name for weapon on line: " + prefixText);
        valueForOneStick = 1;
    }



    protected void updateActualValues(Soldier soldier){
        maxValue = soldier.getActualWeapon().getMaxMagazineCapacity();
        actualValue = soldier.getActualWeapon().getRestBullets();
        updatePreviousValues();

    }




    @Override
    public void update(Soldier soldier){
        super.update(soldier);
        updateActualValues(soldier);
        updateActualWeaponNameString(soldier);
        fillValuesString();
        if (prevColorType != colorToBeUsedType) setLineMustBeRedrawnAtThisFrame(true);
    }

    private void updateActualWeaponNameString(Person soldier) {
        if (soldier.getActualWeapon().getWeaponType() != weaponType){
            weaponType = soldier.getActualWeapon().getWeaponType();
            prefixText = weaponType.name()+ ":";
            if (weaponType.name().contains("_")){
                if (weaponType.name().contains("SAW")){
                    prefixText = "SAWED-OFF:";
                }
            }
            textPosMustBeUpdated = true;
        }
    }

    @Override
    public void draw(PGraphics graphics){
        if (textPosMustBeUpdated){
            int additionalSpace = (int) graphics.textWidth(' ');
            stickStartsX = (int)(leftUpperX+ graphics.textWidth(prefixText)+additionalSpace);
            textPosMustBeUpdated = false;
        }
        super.draw(graphics);
    }


    @Override
    protected void updateColorToBeUsedType(){
        prevColorType = colorToBeUsedType;
        if (weaponType == WeaponType.M79 || weaponType == WeaponType.GRENADE){
            if (actualValue == 0){
                colorToBeUsedType = USE_RED;
            }
            else colorToBeUsedType = USE_MAIN_COLOR;
        }
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN){
            if (actualValue == 0){
                colorToBeUsedType = USE_RED;
            }
            else if (actualValue == 1){
                colorToBeUsedType = USE_YELLOW;
            }
            else colorToBeUsedType = USE_MAIN_COLOR;
        }
        else {
            if (weaponType == WeaponType.SHOTGUN){
                if (actualValue == 2 || actualValue == 1) {
                    colorToBeUsedType = USE_YELLOW;
                } else if (actualValue < 1) {
                    colorToBeUsedType = USE_RED;
                } else {
                    colorToBeUsedType = USE_MAIN_COLOR;
                }
            }
            else {
                if (actualValue == 3 || actualValue == 2) {
                    colorToBeUsedType = USE_YELLOW;
                } else if (actualValue <= 1) {
                    colorToBeUsedType = USE_RED;
                } else {
                    colorToBeUsedType = USE_MAIN_COLOR;
                }
            }
        }
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
        //System.out.println("Color for name was changed " + colorToBeUsedType);
    }

    public void resetColorToNormal(PGraphics graphics){
        //setLineMustBeRedrawnAtThisFrame(true);
        //setColorForName(graphics);
    }

}
