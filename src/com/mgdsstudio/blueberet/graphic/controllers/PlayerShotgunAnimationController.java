package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;

public class PlayerShotgunAnimationController extends HumanAnimationController {
    private final int ADD_BULLET_TO_SCREEN_SPRITE_NUMBER = 5;
    private boolean sleeveAlreadyAddedByThisShot = false;
    private int bulletsInShotgunMagazine = 0;
    private int bulletsWasAlreadyAdded = 0;

    public PlayerShotgunAnimationController(Human human, boolean bulletTimeActivated){
        super(human, Program.getAbsolutePathToAssetsFolder(PATH_TO_SHOTGUN_ANIMATION), bulletTimeActivated);
    }

    private int getMaxBulletsToBeAddedFromTheBag(){
        int inBag = human.getPlayerBag().getRestAmmoForWeapon(WeaponType.SHOTGUN);
        if (inBag >= FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY) return (FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY);
        else {
            return (inBag+1);
        }
    }

    @Override
    protected void drawPlayerReloadingAnimation(GameCamera gameCamera, boolean flip, SoundInGameController soundController) {
        super.drawPlayerReloadingAnimation(gameCamera, flip, soundController);
        if (bulletsInShotgunMagazine < (getMaxBulletsToBeAddedFromTheBag()-1)) {
            if (reloadAnimation.getActualSpriteNumber() == 5) {
                reloadAnimation.setActualSpriteNumber((byte) 2);
                bulletsInShotgunMagazine++;
                bulletsWasAlreadyAdded++;
            }
        }
        if (!reloadAnimation.isAnimationAlreadyShown()) reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
        else {
            reloadAnimation.setActualSpriteNumber((byte) 0);
            reloadAnimation.draw(gameCamera, human.getPixelPosition(), 0f, flip);
            bulletsInShotgunMagazine = FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY;
            reloadAnimation.reset();
            human.setReloadCompleted(false);
            bulletsWasAlreadyAdded = 0;
            reloadingCompletedOnThisFrame = true;
            reloadingSoundWasAdded = false;
        }
        //System.out.println("Bullets: " + bulletsInShotgunMagazine);
    }

    @Override
    public void update(GameRound gameRound){
        if (human.getPersonAnimationController() != null) {
            if (weaponAngleGraphicController.getShotBodyAnimationTo0().getActualSpriteNumber() == ADD_BULLET_TO_SCREEN_SPRITE_NUMBER) {
                if (!sleeveAlreadyAddedByThisShot) {
                    sleeveAlreadyAddedByThisShot = true;
                    addSleeve(gameRound, InWorldObjectsGraphicData.shotgunSleeve, shotgunSleevesImageDimensionCoef, 1);
                }
            } else if (weaponAngleGraphicController.getShotBodyAnimationTo0().getActualSpriteNumber() == (1 + ADD_BULLET_TO_SCREEN_SPRITE_NUMBER))
                sleeveAlreadyAddedByThisShot = false;
            weaponAngleGraphicController.update(gameRound);
            updateSoundAdding(gameRound);
        }
    }

    /*
    protected void addSleeve(GameRound gameRound) {


        ImageZoneSimpleData sleeveAnimation = HUD_GraphicData.shotgunSingleBullet;
        String path = HeadsUpDisplay.mainGraphicSource.getPath();
        final int sleeveWidth = (int) (13);
        final int beretHeight = sleeveWidth;
        float directionCoef = 1f;
        float distToAppearingPlace = gameRound.getPlayer().getPersonWidth()/2f;
        if (gameRound.getPlayer().getWeaponAngle() >90 && gameRound.getPlayer().getWeaponAngle()<270) {
            distToAppearingPlace*=(-1);
            directionCoef=-1f;
            System.out.println("Orientation is to left for angle : " + gameRound.getPlayer().getWeaponAngle());
        }
        SpriteAnimation weaponAnimation = new SpriteAnimation(path, (int) sleeveAnimation.leftX, (int) sleeveAnimation.upperY, (int) sleeveAnimation.rightX, (int) sleeveAnimation.lowerY, sleeveWidth, beretHeight, (byte) 1, (byte) 1, (int) 1);
        weaponAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(path));
        Vec2 position = new Vec2(gameRound.getPlayer().getAbsolutePosition().x+distToAppearingPlace, gameRound.getPlayer().getAbsolutePosition().y);
        float randomX = 55*directionCoef+Programm.engine.random(-20,20);
        float randomY = -65*directionCoef+Programm.engine.random(-25,25);
        MovableOnScreenAnimation movableOnScreenAnimation = new MovableOnScreenAnimation(weaponAnimation, position, 0f, randomX, randomY, 0, 245, directionCoef*350, 3000);
        gameRound.addNewIndependentOnScreenAnimation(movableOnScreenAnimation);
        System.out.println("Sleeve is added with dim: " + sleeveWidth + "; H: " + beretHeight);


    }*/


    @Override
    protected void setUniqueAnimationData() {
        reloadAnimation.setLastSprite((int) 5);
        SpriteAnimation spriteAnimation = weaponAngleGraphicController.getShotBodyAnimationTo0();
        spriteAnimation.setLastSprite((int) 7);
        weaponAngleGraphicController.setShotAnimationIsForEveryAngleSame(true);
        weaponAngleGraphicController.setForShootingAnimationsFrequency(WeaponAngleGraphicController.FAST_SHOOTING_UPDATING_FREQUENCY);
        weaponAngleGraphicController.setWithAngleSpecificGraphic(true);
        idleAnimation.setLastSprite(4);
    }

    @Override
    public void startToReload() {
        bulletsInShotgunMagazine = human.getActualWeapon().getRestBullets();
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
