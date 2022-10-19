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

public class PlayerSMGAnimationController extends HumanAnimationController {

    private final int ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER = 5;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private Timer timerToExtractSleeve;
    private final int timeToExtractSleeve = 400;


    public PlayerSMGAnimationController(Human soldier, boolean bulletTimeActivated) {
        super(soldier, Program.getAbsolutePathToAssetsFolder(PATH_TO_SMG_ANIMATION), bulletTimeActivated);
    }

    @Override
    protected void setUniqueAnimationData() {
        reloadAnimation.setLastSprite((int) 11);
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();
        SpriteAnimation spriteAnimationFor45 = weaponAngleGraphicController.getShotBodyAnimationTo45();
        SpriteAnimation spriteAnimationFor315 = weaponAngleGraphicController.getShotBodyAnimationTo315();
        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(false);
        weaponAngleGraphicController.setForShootingAnimationsFrequency((int) (WeaponAngleGraphicController.FAST_SHOOTING_UPDATING_FREQUENCY));
        spriteAnimation.setLastSprite((int) 1);
        spriteAnimationFor45.setLastSprite((int) 1);
        spriteAnimationFor315.setLastSprite((int) 1);
        weaponAngleGraphicController.setWithAngleSpecificGraphic(true);
        idleAnimation.setLastSprite(4);
    }

    @Override
    public void startToReload() {
        System.out.println("Nothing to override for pistole");
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
                    //reloadAnimation.setActualSpriteNumber((byte) (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER - 1));
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
            reloadingCompletedOnThisFrame = true;
            reloadingSoundWasAdded = false;
            human.setReloadCompleted(false);
        }
        /*
        super.drawPlayerReloadingAnimation(gameCamera, flip);
        if (bulletsInShotgunMagazine < (FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY-1)) {
            if (reloadAnimation.getActualSpriteNumber() == 5) {
                reloadAnimation.setActualSpriteNumber((byte) 2);
                bulletsInShotgunMagazine++;
            }
        }
        if (!reloadAnimation.isAnimationAlreadyShown()) reloadAnimation.draw(gameCamera, player.getAbsolutePosition(), 0f, flip);
        else {
            reloadAnimation.setActualSpriteNumber((byte) 0);
            reloadAnimation.draw(gameCamera, player.getAbsolutePosition(), 0f, flip);
            bulletsInShotgunMagazine = FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY;
            reloadAnimation.reset();
            player.setReloadCompleted();
        }


         */

    }

    @Override
    protected boolean mustBeMagizineAddingSoundSwitchedOn() {
        if (reloadAnimation.getActualSpriteNumber() == 5 || reloadAnimation.getActualSpriteNumber() == 10){
            return  true;
        }
        else return false;
    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
            if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) {
                if (!sleeveAlreadyAddedByThisShot) {
                    sleeveAlreadyAddedByThisShot = true;
                    addSleeve(gameRound, InWorldObjectsGraphicData.smgAmmoInWorld, sleevesImageDimensionCoef, 1);
                }
            }
            else if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (1 + ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) sleeveAlreadyAddedByThisShot = false;
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);
        }
    }


}
