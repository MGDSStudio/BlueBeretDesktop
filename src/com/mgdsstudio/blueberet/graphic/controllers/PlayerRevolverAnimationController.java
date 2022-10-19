package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;

public class PlayerRevolverAnimationController extends HumanAnimationController {
    //Based on shotgun
    private final int ADD_BULLET_TO_SCREEN_SPRITE_NUMBER = 5;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private int bulletsInRevolverMagazine = 0;
    private int bulletsWasAlreadyAdded = 0;
    private int sleevesToBeAdded = 6;

    public PlayerRevolverAnimationController(Human soldier, boolean bulletTimeActivated){
        super(soldier, Program.getAbsolutePathToAssetsFolder(PATH_TO_REVOLVER_ANIMATION), bulletTimeActivated);
    }

    private int getMaxBulletsToBeAddedFromTheBag(){
        int inBag = human.getPlayerBag().getRestAmmoForWeapon(WeaponType.REVOLVER);
        if (inBag >= FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY) return (FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY);
        else {
            return (inBag+1);
        }
    }

    @Override
    protected void drawPlayerReloadingAnimation(GameCamera gameCamera, boolean flip, SoundInGameController soundController) {
        super.drawPlayerReloadingAnimation(gameCamera, flip, soundController);

        if (bulletsInRevolverMagazine < (getMaxBulletsToBeAddedFromTheBag()-1)) {
            if (reloadAnimation.getActualSpriteNumber() == 6) {
                reloadAnimation.setActualSpriteNumber((byte) 3);
                bulletsInRevolverMagazine++;
                bulletsWasAlreadyAdded++;
            }
        }
        if (!reloadAnimation.isAnimationAlreadyShown()) reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
        else {
            reloadAnimation.setActualSpriteNumber((byte) 0);
            reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
            bulletsInRevolverMagazine = FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY;
            reloadAnimation.reset();
            human.setReloadCompleted(false);
            bulletsWasAlreadyAdded = 0;
            reloadingCompletedOnThisFrame = true;
            reloadingSoundWasAdded = false;
        }
        //System.out.println("Bullets: " + bulletsInShotgunMagazine);
    }

    private void updateSleevesAdding(GameRound gameRound) {

            if (reloadAnimation.getActualSpriteNumber() == 1 && sleeveAlreadyAddedByThisShot == false) {
                sleeveAlreadyAddedByThisShot = true;
                addSleeve(gameRound, InWorldObjectsGraphicData.simpleBullet, sleevesImageDimensionCoef, (FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY- human.getActualWeapon().getRestBullets()));
            }
            else if (reloadAnimation.getActualSpriteNumber() == 2 && sleeveAlreadyAddedByThisShot == true) {
                sleeveAlreadyAddedByThisShot = false;
            }

    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
                updateSleevesAdding(gameRound);

            /*if (weaponAngleGraphicController.getShotBodyAnimationTo0().getActualSpriteNumber() == ADD_BULLET_TO_SCREEN_SPRITE_NUMBER) {
                if (!sleeveAlreadyAddedByThisShot) {
                    sleeveAlreadyAddedByThisShot = true;
                    addSleeve(gameRound, InWorldObjectsGraphicData.pistolAmmoInWorld, sleevesImageDimensionCoef, 1);
                }
            } else if (weaponAngleGraphicController.getShotBodyAnimationTo0().getActualSpriteNumber() == (1 + ADD_BULLET_TO_SCREEN_SPRITE_NUMBER))
                sleeveAlreadyAddedByThisShot = false;*/
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);
        }
    }

    @Override
    protected void setUniqueAnimationData() {
        reloadAnimation.setLastSprite((int) 10);
        //reloadAnimation.setLastSprite((int) 5);
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();

        SpriteAnimation spriteAnimationFor45 = weaponAngleGraphicController.getShotBodyAnimationTo45();
        SpriteAnimation spriteAnimationFor315 = weaponAngleGraphicController.getShotBodyAnimationTo315();
        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(false);
        weaponAngleGraphicController.setForShootingAnimationsFrequency(WeaponAngleGraphicController.NORMAL_SHOOTING_UPDATING_FREQUENCY);

        System.out.println("Animation has " + spriteAnimationFor45.getRowsNumber()+ "x" + spriteAnimationFor45.getColumnsNumber());
        spriteAnimation.setLastSprite((int) 5);
        spriteAnimationFor45.setLastSprite((int) 5);

        spriteAnimationFor315.setLastSprite((int) 5);
        weaponAngleGraphicController.setWithAngleSpecificGraphic(true);
        idleAnimation.setLastSprite(4);
        /*
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();
        spriteAnimation.setLastSprite((int) 5);

        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(false);
        weaponAngleGraphicController.setForShootingAnimationsFrequency(WeaponAngleGraphicController.FAST_SHOOTING_UPDATING_FREQUENCY);*/

    }

    @Override
    public void startToReload() {
        bulletsInRevolverMagazine = human.getActualWeapon().getRestBullets();
    }

    public int getBulletsWasAlreadyAdded() {
        return bulletsWasAlreadyAdded;
    }



    public void resetBulletsWasAlreadyAdded() {
        this.bulletsWasAlreadyAdded = 0;
    }

    @Override
    protected boolean mustBeMagizineAddingSoundSwitchedOn() {
        if (reloadAnimation.getActualSpriteNumber() == 3){
            return  true;
        }
        else return false;
    }
}
