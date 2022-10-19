package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PGraphics;

import java.util.HashMap;

public class RestAmmoValueController extends LineController{
    private WeaponType weaponType;
    private boolean textPosMustBeUpdated;
    private HashMap <WeaponType,Integer> restAmmo;
    private HashMap <WeaponType,Integer> maxAmmo;
    private PlayerBag playerBag;
    protected int prevColorType;

    public RestAmmoValueController (Person soldier, int leftUpperX, int leftUpperY, PGraphics graphics){
        fillHashMaps((Soldier) soldier);
        playerBag = ((Soldier) soldier).getPlayerBag();
        weaponType = soldier.getActualWeapon().getWeaponType();
        init();
        initBasicValues(leftUpperX, leftUpperY);
        updateActualValues((Soldier) soldier);
        int additionalSpace = (int) graphics.textWidth(' ');
        stickStartsX = (int)(leftUpperX+ graphics.textWidth(prefixText)+additionalSpace);
        System.out.println("Text width: " + graphics.textWidth(prefixText)+ " with size: " + graphics.textFont.getSize());
        System.out.println("Symbol width for bullets:  " + graphics.textFont.width('L')*graphics.textFont.getSize());
        initBlackRectData(graphics);
    }

    private void fillHashMaps(Soldier soldier){
        restAmmo = new HashMap<>();
        maxAmmo = new HashMap<>();
        for (WeaponType weaponType : soldier.getDeblockedWeapons()) {
            restAmmo.put(weaponType, soldier.getPlayerBag().getRestAmmoForWeapon(weaponType));
            maxAmmo.put(weaponType, soldier.getPlayerBag().getMaxAmmoForWeapon(weaponType));
        }
    }

    protected void init(){
        prefixText = "Ammo:";
        valueForOneStick = 1;
    }



    protected void updateActualValues(Soldier soldier){
        maxValue = playerBag.getMaxAmmoForWeapon(soldier.getActualWeapon().getWeaponType());
        actualValue = playerBag.getRestAmmoForWeapon(soldier.getActualWeapon().getWeaponType());
        updatePreviousValue();
    }




    @Override
    public void update(Soldier soldier){
        super.update(soldier);
        updateActualValues(soldier);
        updateActualWeaponNameString(soldier);
        fillValuesString();
        if (prevColorType != colorToBeUsedType) setLineMustBeRedrawnAtThisFrame(true);
    }

    private void updateActualWeaponNameString(Soldier soldier) {
        if (soldier.getActualWeapon().getWeaponType() != weaponType){
            weaponType = soldier.getActualWeapon().getWeaponType();
            if (Program.debug && Program.engine.frameCount % 300 == 1) System.out.println("It must be by weapon changing made");
            //prefixText = weaponType.name()+ ":";;
            //textPosMustBeUpdated = true;
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

    @Override
    protected void updateColorToBeUsedType(){
        prevColorType = colorToBeUsedType;
        if (weaponType == WeaponType.M79 || weaponType == WeaponType.GRENADE){
            if (actualValue == 0){
                colorToBeUsedType = USE_RED;
            }
            else if (actualValue == 1){
                colorToBeUsedType = USE_YELLOW;
            }
            else colorToBeUsedType = USE_MAIN_COLOR;
        }
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN || weaponType == WeaponType.SHOTGUN){
            if (actualValue == 0){
                colorToBeUsedType = USE_RED;
            }
            else if (actualValue <= 5){
                colorToBeUsedType = USE_YELLOW;
            }
            else colorToBeUsedType = USE_MAIN_COLOR;

        }
        else {
            if (actualValue == 2) {
                colorToBeUsedType = USE_YELLOW;
            } else if (actualValue <= 1) {
                colorToBeUsedType = USE_RED;
            } else {
                colorToBeUsedType = USE_MAIN_COLOR;
            }
        }

        //System.out.println("TypE: "+ weaponType +" Max: " + maxValue + " actual " + actualValue + " rel: " + (((float)actualValue)/(float)maxValue));
    }

}
