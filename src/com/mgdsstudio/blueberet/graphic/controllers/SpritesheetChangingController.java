package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.WeaponType;

public class SpritesheetChangingController {

    private HumanAnimationController oldPlayerAnimationController, newPlayerAnimationController;
    private String oldPath;
    private String newPath;
    private WeaponType oldWeaponType;
    private WeaponType newWeaponType;
    private boolean started;
    private boolean ended;

    public SpritesheetChangingController(WeaponType actualWeapon, HumanAnimationController playerAnimationController){
        this.oldWeaponType = actualWeapon;
        //
        this.oldPlayerAnimationController = playerAnimationController;

    }

    public void loadNewSpritesheet (WeaponType newWeaponType){
        this.newWeaponType = newWeaponType;
        startLoading();
    }

    public void endLoading(){
        started = false;
        oldWeaponType = newWeaponType;
        newWeaponType = null;
        oldPlayerAnimationController = newPlayerAnimationController;
        newPlayerAnimationController = null;
        ended = false;
    }

    private void startLoading() {
        started = true;
        boolean bulletTimeActivated = PhysicGameWorld.bulletTime;
        if (newWeaponType == WeaponType.SHOTGUN) newPlayerAnimationController = new PlayerShotgunAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        else if (newWeaponType == WeaponType.M79) newPlayerAnimationController = new PlayerGreenadeLauncherAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        else if (newWeaponType == WeaponType.HANDGUN) newPlayerAnimationController = new PlayerPistoleAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        else if (newWeaponType == WeaponType.SMG) newPlayerAnimationController = new PlayerSMGAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);

        else if (newWeaponType == WeaponType.REVOLVER) newPlayerAnimationController = new PlayerRevolverAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        else if (newWeaponType == WeaponType.SAWED_OFF_SHOTGUN) newPlayerAnimationController = new PlayerSawedOffShotgunAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        else if (newWeaponType == WeaponType.GRENADE) newPlayerAnimationController = new PlayerGreenadeAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);


        else newPlayerAnimationController = new PlayerShotgunAnimationController(oldPlayerAnimationController.getHuman(), bulletTimeActivated);
        System.out.println("Spritesheet was updated");
        ended = true;
        System.gc();
    }

    public boolean isStarted(){
        return started;
    }

    public boolean isEnded(){
        return ended;
    }

    public HumanAnimationController getPlayerAnimationController(){
        if (started) {
            if (!ended) {
                System.out.println("New animation controller was returned " + oldPlayerAnimationController.weaponType);
                return oldPlayerAnimationController;
            }
            else {
                System.out.println("New animation controller was returned " + newPlayerAnimationController.weaponType);
                return newPlayerAnimationController;
            }
        }
        else {
            System.out.println("Animation controller was not started to change yet");
            return null;
        }
    }



}
