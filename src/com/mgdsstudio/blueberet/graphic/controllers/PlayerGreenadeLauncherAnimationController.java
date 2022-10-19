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

public class PlayerGreenadeLauncherAnimationController extends HumanAnimationController {
    private final int ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER = 5;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private Timer timerToExtractSleeve;
    private final int timeToExtractSleeve = 400;



    public PlayerGreenadeLauncherAnimationController(Human soldier, boolean inBulletTimeMode) {
        super(soldier, Program.getAbsolutePathToAssetsFolder(PATH_TO_M79_ANIMATION), inBulletTimeMode);
    }

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
    public void startToReload() {
        System.out.println("Nothing to override for GL");
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
    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
            if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) {
                if (!sleeveAlreadyAddedByThisShot) {
                    sleeveAlreadyAddedByThisShot = true;
                    addSleeve(gameRound, InWorldObjectsGraphicData.grenadeLauncherSleeve, sleevesImageDimensionCoef, 1);
                }
            }
            else if (human.getPersonAnimationController().getAnimationForType(RELOAD).getActualSpriteNumber() == (1 + ADD_SLEEVE_TO_SCREEN_SPRITE_NUMBER)) sleeveAlreadyAddedByThisShot = false;
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);
        }
    }

    @Override
    protected boolean mustBeMagizineAddingSoundSwitchedOn() {
        if (reloadAnimation.getActualSpriteNumber() == 3 || reloadAnimation.getActualSpriteNumber() == 6){
            return  true;
        }
        else return false;
    }
}
