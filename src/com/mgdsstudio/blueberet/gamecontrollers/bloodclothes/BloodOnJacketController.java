package com.mgdsstudio.blueberet.gamecontrollers.bloodclothes;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.weapon.WeaponType;

import java.util.HashMap;

public class BloodOnJacketController {
    private Soldier soldier;
    private static HashMap<WeaponType, BloodPoints> bloodPoints;


    private void init(){
        HumanAnimationController playerAnimationController = (HumanAnimationController) soldier.getPersonAnimationController();
        playerAnimationController.getTilesetForActualGraphic();
    }

    public void playerAttacked(){

    }

    private void createNewBloodPoints(){

    }

    public void deleteBloodPoints(){
        bloodPoints = null;
    }

}
