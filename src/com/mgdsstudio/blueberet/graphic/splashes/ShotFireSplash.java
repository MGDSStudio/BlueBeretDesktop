package com.mgdsstudio.blueberet.graphic.splashes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;

public class ShotFireSplash extends Splash implements IPoolable{
    private final static int animationGraphicAngle = 0;
    //protected static SpriteAnimation pistolWeaponAnimation, shotgunWeaponAnimation;

    protected static SpriteAnimation pistolWeaponAnimation;
    protected static SpriteAnimation shotgunWeaponAnimation;
    //protected static IndependentOnScreenStaticSprite lightCircle;
    private static AnimationDataToStore fireFromWeaponAnimationDataToStore;

    private float weaponStartShotAngle;
    private Soldier shootingPerson;
    //private boolean withCircle = false;
    //private boolean animationType
    private WeaponType weaponType;
    private boolean flip = false;
    private final int nominalSplashWidth = 56;
    private final int nominalHeight = 48;
    private final int additionalX = 20;
    private static Tileset tileset;

    public ShotFireSplash(Soldier shootingPerson, Vec2 relativePos, int bulletAngle, boolean type, WeaponType weaponType){
        super(shootingPerson.body, relativePos, bulletAngle, type);
        initSecondaryData(shootingPerson, weaponType);
        initGraphic();

    }

    private void initGraphic() {
        if (tileset == null) tileset = HeadsUpDisplay.mainGraphicTileset;
        //initGraphic();
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) {
            loadAnimationForShotgunWeaponFire(tileset);
            shotgunWeaponAnimation.setPlayOnce(true);
        }
        else {
            loadAnimationForMainWeaponFire(tileset );
            pistolWeaponAnimation.setPlayOnce(true);
        }
        //if (withCircle) loadAnimationForLightCircle(tileset );
    }

    private void initSecondaryData(Soldier shootingPerson, WeaponType weaponType){
        this.shootingPerson = shootingPerson;
        weaponStartShotAngle = shootingPerson.getWeaponAngle();
        if (splashFallingTimer == null) splashFallingTimer = new Timer(TIME_TO_FALL);
        else splashFallingTimer.setNewTimer(TIME_TO_FALL);
        this.weaponType = weaponType;
        calculateFlip(bulletAngle);
    }

    protected void reset(Soldier player, Vec2 shotPointRelativeToBodyCenter, int weaponAngle, boolean dynamic, WeaponType weaponType){
        initBasicData(player.body, shotPointRelativeToBodyCenter, weaponAngle, dynamic);
        initSecondaryData(shootingPerson, weaponType);
        initGraphic();
        resetAnimationData();
    }

    private void calculateFlip(int bulletAngle) {
        if (bulletAngle < 270 && bulletAngle > 90) {
            flip = true;
        }
        //System.out.println("For angle: " + bulletAngle + " animations flip statement: " + flip);
    }


    /*private void loadAnimationForLightCircle(Tileset tileset) {
        int nominalWidth = 150;
        ImageZoneFullData zoneData = new ImageZoneFullData(HeadsUpDisplay.mainGraphicSource.getPath(), 640, 0, 640+32, 32);
        lightCircle = new StaticSprite(zoneData, nominalWidth, nominalWidth);
        //Tileset tileset = new Tileset((byte)1, HeadsUpDisplay.mainGraphicSource, fireFromWeaponAnimationDataToStore.getCollumnsNumber(), fireFromWeaponAnimationDataToStore.getRowsNumber());
        //Tileset tileset = new Tileset((byte)1, HeadsUpDisplay.mainGraphicSource.getPath());
        lightCircle.loadSprite(tileset);
        //lightCircle.getImage().loadNewImage(HeadsUpDisplay.mainGraphicSource.getImage());
    }*/

    private void loadAnimationForMainWeaponFire(Tileset tileset){
        if (pistolWeaponAnimation == null) {
            if (fireFromWeaponAnimationDataToStore == null) {
                int[] leftUpper = {394, 290};
                int[] rightLower = {(int) (leftUpper[0] + 64), (int) (leftUpper[1] + 64)};
                int nominalWidth = 80;
                fireFromWeaponAnimationDataToStore = new AnimationDataToStore(HeadsUpDisplay.mainGraphicSource.getPath(), leftUpper, rightLower, nominalWidth, nominalWidth, (byte) 2, (byte) 2, (int) 33);
            }
            pistolWeaponAnimation = new SpriteAnimation(fireFromWeaponAnimationDataToStore.getPath(), fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[0], fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[1], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[0], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[1], fireFromWeaponAnimationDataToStore.getGraphicWidth(), fireFromWeaponAnimationDataToStore.getGraphicHeight(), fireFromWeaponAnimationDataToStore.getRowsNumber(), fireFromWeaponAnimationDataToStore.getCollumnsNumber(), fireFromWeaponAnimationDataToStore.getFrequency());
            pistolWeaponAnimation.setLastSprite((int) 2);
            pistolWeaponAnimation.loadAnimation(tileset);
        }
    }

    private void loadAnimationForShotgunWeaponFire(Tileset tileset){
        if (shotgunWeaponAnimation == null) {
            if (fireFromWeaponAnimationDataToStore == null) {
                ImageZoneSimpleData shotgunWeaponFire = new ImageZoneSimpleData(additionalX + 402 - nominalSplashWidth / 2, 262 - nominalHeight / 2, additionalX + 4 * nominalSplashWidth + 402 - nominalSplashWidth / 2, 262 + nominalHeight / 2);

                int[] leftUpper = {(int) shotgunWeaponFire.leftX, (int) shotgunWeaponFire.upperY};
                int[] rightLower = {(int) (shotgunWeaponFire.rightX), (int) (shotgunWeaponFire.lowerY)};

                fireFromWeaponAnimationDataToStore = new AnimationDataToStore(HeadsUpDisplay.mainGraphicSource.getPath(), leftUpper, rightLower, nominalSplashWidth, nominalSplashWidth, (byte) 1, (byte) 4, (int) 8);
            }
            shotgunWeaponAnimation = new SpriteAnimation(fireFromWeaponAnimationDataToStore.getPath(), fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[0], fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[1], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[0], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[1], fireFromWeaponAnimationDataToStore.getGraphicWidth(), fireFromWeaponAnimationDataToStore.getGraphicHeight(), fireFromWeaponAnimationDataToStore.getRowsNumber(), fireFromWeaponAnimationDataToStore.getCollumnsNumber(), fireFromWeaponAnimationDataToStore.getFrequency());
            //shotgunWeaponAnimation = new SpriteAnimation(fireFromWeaponAnimationDataToStore.getPath(), fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[0], fireFromWeaponAnimationDataToStore.getLeftUpperCorner()[1], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[0], fireFromWeaponAnimationDataToStore.getRightLowerCorner()[1], fireFromWeaponAnimationDataToStore.getGraphicWidth(), fireFromWeaponAnimationDataToStore.getGraphicHeight(), fireFromWeaponAnimationDataToStore.getRowsNumber(), fireFromWeaponAnimationDataToStore.getCollumnsNumber(), fireFromWeaponAnimationDataToStore.getFrequency() );
            //
            shotgunWeaponAnimation.setLastSprite((int) 2);
            shotgunWeaponAnimation.loadAnimation(tileset);
        }

    }

    private void resetAnimationData() {

        if (pistolWeaponAnimation != null) {
            pistolWeaponAnimation.reset();
            pistolWeaponAnimation.setAnimationStatement(SpriteAnimation.ACTIVE);
        }
        if (shotgunWeaponAnimation != null) {
            shotgunWeaponAnimation.reset();
            shotgunWeaponAnimation.setAnimationStatement(SpriteAnimation.ACTIVE);
        }
        ended = false;
    }



    protected void updateEndStatement() {
        if (!ended) {
            if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN){
                if (shotgunWeaponAnimation.isAnimationAlreadyShown()) {
                    shotgunWeaponAnimation.setAnimationStatement(SpriteAnimation.PAUSED);
                    ended = true;
                }
            }
            else {
                if (pistolWeaponAnimation.isAnimationAlreadyShown()) {
                    pistolWeaponAnimation.setAnimationStatement(SpriteAnimation.PAUSED);
                    ended = true;
                }
            }
        }
    }


    @Override
    public void draw(GameCamera gameCamera) {
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) shotgunWeaponAnimation.update();
        else pistolWeaponAnimation.update();
        updateEndStatement();
        if (!ended) {
            //System.out.println("Statement: " + animation.getAnimationStatement() + "; sprite: " + animation.getActualSpriteNumber());
            float angle = animationGraphicAngle - bulletAngle + shootingPerson.getWeaponAngle()-weaponStartShotAngle;
            Vec2 splashActualPos = PhysicGameWorld.getBodyPixelCoord(attackedBody).add(basicSplashPos);
            if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) {
                shotgunWeaponAnimation.draw(gameCamera, splashActualPos, angle, false, flip);
            }
            else pistolWeaponAnimation.draw(gameCamera, splashActualPos, angle, false, flip);
            /*if (withCircle) {
                lightCircle.setTint(Program.engine.color(255,125));
                lightCircle.draw(gameCamera, splashActualPos, angle);
            }*/
        }
    }

    public boolean isEnded(){
        return ended;
    }


    public void recreate(Soldier player, Vec2 shotPointRelativeToBodyCenter, int weaponAngle, boolean dynamic, WeaponType weaponType) {
        reset(player, shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);
    }
}
