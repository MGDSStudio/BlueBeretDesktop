package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class PlayerSawedOffShotgunAnimationController extends HumanAnimationController {
    //private final int ADD_BULLET_TO_SCREEN_SPRITE_NUMBER = 5;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private final int ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER = 5;

    //private int bulletsInShotgunMagazine = 0;
    private int bulletsWasAlreadyAdded = 0;

    private Timer timerToExtractSleeve;
    private final int timeToExtractSleeve = 400;

    private boolean twoSleevesMustBeThrownByThisReloading = false;

    public PlayerSawedOffShotgunAnimationController(Human soldier, boolean bulletTimeActivated){
        super(soldier, Program.getAbsolutePathToAssetsFolder(PATH_TO_SO_SHOTGUN_ANIMATION), bulletTimeActivated);
    }

    /*
    private int getMaxBulletsToBeAddedFromTheBag(){
        int inBag = player.getPlayerBag().getRestAmmoForWeapon(WeaponType.SHOTGUN);
        if (inBag >= FirearmsWeapon.SO_MAGAZINE_CAPACITY) return (FirearmsWeapon.SO_MAGAZINE_CAPACITY);
        else {
            return (inBag+1);
        }
    }*/

    @Override
    protected void setUniqueAnimationData() {
        reloadAnimation.setLastSprite((int) 8);
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();
        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(true);
        spriteAnimation.setLastSprite((int) 4);
        weaponAngleGraphicController.setForShootingAnimationsFrequency(WeaponAngleGraphicController.FAST_SHOOTING_UPDATING_FREQUENCY);
        weaponAngleGraphicController.setWithAngleSpecificGraphic(true);
        idleAnimation.setLastSprite(4);
    }

    @Override
    protected void drawPlayerReloadingAnimation(GameCamera gameCamera, boolean flip, SoundInGameController soundController) {
        super.drawPlayerReloadingAnimation(gameCamera, flip, soundController);
        if (!reloadAnimation.isAnimationAlreadyShown()) {
            if (reloadAnimation.getActualSpriteNumber() == (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER-1)){
                if (timerToExtractSleeve == null){
                    timerToExtractSleeve = new Timer(timeToExtractSleeve);
                }
                else if (timerToExtractSleeve.isSwitchedOff()){
                    timerToExtractSleeve.setNewTimer(timeToExtractSleeve);
                }

            }
            if (timerToExtractSleeve != null){
                if (!timerToExtractSleeve.isTime()) {

                }
                else {
                    timerToExtractSleeve.stop();
                }
            }
            reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
        }
        else {
            reloadAnimation.setActualSpriteNumber((byte) 0);
            reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
            reloadAnimation.reset();
            human.setReloadCompleted(false);
            reloadingCompletedOnThisFrame = true;
            reloadingSoundWasAdded = false;
        }
        //System.out.println("Bullets: " + bulletsInShotgunMagazine);
    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
            if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) {

                if (!sleeveAlreadyAddedByThisShot) {
                    sleeveAlreadyAddedByThisShot = true;
                    if (twoSleevesMustBeThrownByThisReloading) {
                        addSleeve(gameRound, InWorldObjectsGraphicData.shotgunSleeve, sleevesImageDimensionCoef, 2);
                    }
                    else addSleeve(gameRound, InWorldObjectsGraphicData.shotgunSleeve, shotgunSleevesImageDimensionCoef, 1);
                }
            }
            else if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (1 + ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) sleeveAlreadyAddedByThisShot = false;
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);
        }
    }



    @Override
    public void startToReload() {

        int restBullets = human.getActualWeapon().getRestBullets();
        if (restBullets == 0) twoSleevesMustBeThrownByThisReloading = true;
        else twoSleevesMustBeThrownByThisReloading = false;
        System.out.println("Player has in the barrel " + restBullets + " bullets");
    }

    public int getBulletsWasAlreadyAdded() {
        return bulletsWasAlreadyAdded;
    }



    public void resetBulletsWasAlreadyAdded() {
        this.bulletsWasAlreadyAdded = 0;
    }



    @Override
    protected boolean mustBeMagizineAddingSoundSwitchedOn() {
        if (reloadAnimation.getActualSpriteNumber() == 3 || reloadAnimation.getActualSpriteNumber() == 6){
            return  true;
        }
        else return false;
    }

    public boolean isTwoSleevesMustBeThrownByThisReloading() {
        return twoSleevesMustBeThrownByThisReloading;
    }
}
