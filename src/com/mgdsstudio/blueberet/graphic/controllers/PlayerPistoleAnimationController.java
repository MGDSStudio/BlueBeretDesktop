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

public class PlayerPistoleAnimationController  extends HumanAnimationController {
    private final int ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER = 2;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private Timer timerToExtractSleeve;
    private final int timeToExtractSleeve = 400;
    private boolean magazineSoundAdded;

    public PlayerPistoleAnimationController(Human soldier, boolean bulletTimeActivated) {
        super(soldier, Program.getAbsolutePathToAssetsFolder(PATH_TO_PISTOL_ANIMATION), bulletTimeActivated);
    }

    @Override
    protected void setUniqueAnimationData() {
        reloadAnimation.setLastSprite((int) 9);
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();
        SpriteAnimation spriteAnimationFor45 = weaponAngleGraphicController.getShotBodyAnimationTo45();
        SpriteAnimation spriteAnimationFor315 = weaponAngleGraphicController.getShotBodyAnimationTo315();
        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(false);
        weaponAngleGraphicController.setForShootingAnimationsFrequency(WeaponAngleGraphicController.NORMAL_SHOOTING_UPDATING_FREQUENCY);
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
    }

    @Override
    protected boolean mustBeMagizineAddingSoundSwitchedOn() {
        if (reloadAnimation.getActualSpriteNumber() == 3 || reloadAnimation.getActualSpriteNumber() == 5){
            return  true;
        }
        else return false;
    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
            updateClearMagazineInWorldThrowing(gameRound);
            //updateSleveAfterShotInWorldThrowing(gameRound);
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);

        }

    }

    private void updateClearMagazineInWorldThrowing(GameRound gameRound) {
        if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) {
            if (!sleeveAlreadyAddedByThisShot) {
                sleeveAlreadyAddedByThisShot = true;
                addSleeve(gameRound, InWorldObjectsGraphicData.pistolAmmoInWorld, sleevesImageDimensionCoef, 1);
            }
        }
        else if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (1 + ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) sleeveAlreadyAddedByThisShot = false;

    }


}
