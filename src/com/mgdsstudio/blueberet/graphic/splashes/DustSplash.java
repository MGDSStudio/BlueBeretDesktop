package com.mgdsstudio.blueberet.graphic.splashes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;

public class DustSplash extends Splash implements IDrawable {
    //private final static String PATH_TO_SPRITESHEET = Programm.getRelativePathToAssetsFolder()+"DustSplashAnimation.png";
    private final static int animationGraphicAngle = 90;
    protected SpriteAnimation animation;
    private static AnimationDataToStore animationDataToStore;
    private static boolean animationLoaded;
    //private static Image animationSpritesheet;
    private Tileset animationTileset;



    public DustSplash(Body attackedBody,Vec2 relativePos, int bulletAngle, boolean type){
        super(attackedBody, relativePos, bulletAngle, type);
        initGraphic();

    }

    private void initGraphic() {
        if (animationTileset == null) animationTileset = HeadsUpDisplay.mainGraphicTileset;
        //Tileset tileset = HeadsUpDisplay.mainGraphicTileset;
        if (animationDataToStore == null) {
            int[] leftUpper = {128, 0};
            int[] rightLower = {128 + 256, 256};
            int nominalWidth = 200;
            animationDataToStore = new AnimationDataToStore(animationTileset.getPath(), leftUpper, rightLower, nominalWidth, nominalWidth, (byte) 4, (byte) 4, 17);
            //animationTileset = new Tileset((byte)1, animationDataToStore.getPath(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getRowsNumber());
        }
        animation = new SpriteAnimation(animationDataToStore.getPath(), animationDataToStore.getLeftUpperCorner()[0], animationDataToStore.getLeftUpperCorner()[1], animationDataToStore.getRightLowerCorner()[0], animationDataToStore.getRightLowerCorner()[1], animationDataToStore.getGraphicWidth(), animationDataToStore.getGraphicHeight(), animationDataToStore.getRowsNumber(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getFrequency() );
        animation.loadAnimation(animationTileset);
        animation.setLastSprite(10);
        animation.setTint(120);

        layer = IndependentOnScreenAnimation.BEHIND_ALL;

    }

    protected void reset(Body body, Vec2 bulletShotPlace, int bulletAngle, boolean aStatic){
        initBasicData(body, bulletShotPlace, bulletAngle, aStatic);
        initSecondaryData();
        resetAnimationData();
        //initSecondaryData(shootingPerson, weaponType);
        //resetAnimationData();

        //splashFallingTimer = new Timer(TIME_TO_FALL);
    }

    private void initSecondaryData(){
        //this. = shootingPerson;
        //weaponStartShotAngle = shootingPerson.getWeaponAngle();
        if (splashFallingTimer == null) splashFallingTimer = new Timer(TIME_TO_FALL);
        else splashFallingTimer.setNewTimer(TIME_TO_FALL);
    }

    private void resetAnimationData() {
        if (animation != null) {
            animation.reset();
            animation.setAnimationStatement(SpriteAnimation.ACTIVE);
        }
        ended = false;
    }

    protected void updateEndStatement() {
        if (!ended) {
            if (animation.isAnimationAlreadyShown()){
                animation.setAnimationStatement(SpriteAnimation.PAUSED);
                ended = true;
            }
        }
    }


    @Override
    public void draw(GameCamera gameCamera) {
        animation.update();
        updateEndStatement();
        if (!ended) {
            //System.out.println("Statement: " + animation.getAnimationStatement() + "; sprite: " + animation.getActualSpriteNumber());
            if (type == DYNAMIC) {
                float angle = animationGraphicAngle - bulletAngle + PApplet.degrees(attackedBody.getAngle()) - bodyStartAngle;
                Vec2 splashActualPos = PhysicGameWorld.getBodyPixelCoord(attackedBody).add(basicSplashPos);
                animation.draw(gameCamera, splashActualPos, angle, false);
            }
            else {
                //Vec2 splashActualPos = PhysicGameWorld.controller.getBodyPixelCoord(attackedBody).add(relativePos);
                float angle = animationGraphicAngle - bulletAngle + PApplet.degrees(attackedBody.getAngle()) - bodyStartAngle;
                animation.draw(gameCamera, basicSplashPos, angle, false);
                //System.out.println("Actual sprite: " + animation.getActualSpriteNumber());
            }
        }
    }

    public void recreate(Body body, Vec2 bulletShotPlace, int bulletAngle, boolean aStatic) {
        reset(body, bulletShotPlace, bulletAngle, aStatic);
    }
}
