package com.mgdsstudio.blueberet.graphic.splashes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.AnimationDataToStore;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;

public class BloodSplash extends Splash implements IDrawable {
    //private static IndependentOnScreenAnimation animation;
    protected static SpriteAnimation animation;
    private static AnimationDataToStore animationDataToStore;
    private static boolean animationLoaded;
    //private int animationWidth;
    //private int distanceFromCenterToSplashCenter = 35;
    private final static int animationGraphicAngle = 90;

    public BloodSplash(Body attackedBody,Vec2 relativePos, int bulletAngle, boolean type){
        super(attackedBody, relativePos, bulletAngle, type);
        if (!animationLoaded){
            int[] leftUpper = {0, 0};
            int[] rightLower = {512, 512};
            int nominalWidth = 140;
            animationDataToStore = new AnimationDataToStore(Program.getAbsolutePathToAssetsFolder("BloodSplashAnimation.png"), leftUpper, rightLower, nominalWidth, nominalWidth, (byte)4,(byte)4, (int)33);
            Tileset tileset = new Tileset((byte)1, animationDataToStore.getPath(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getRowsNumber());
            animation = new SpriteAnimation(animationDataToStore.getPath(), animationDataToStore.getLeftUpperCorner()[0], animationDataToStore.getLeftUpperCorner()[1], animationDataToStore.getRightLowerCorner()[0], animationDataToStore.getRightLowerCorner()[1], animationDataToStore.getGraphicWidth(), animationDataToStore.getGraphicHeight(), animationDataToStore.getRowsNumber(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getFrequency() );
            animation.loadAnimation(tileset);
            animation.getImage().loadNewImage(Program.getAbsolutePathToAssetsFolder("BloodSplashAnimation.png"));
            System.out.println("BloodSplashAnimation was succesfully loaded");
            animationLoaded = true;
        }
        else {
            resetAnimationData();
        }
        splashFallingTimer = new Timer(TIME_TO_FALL);
        //System.out.println("Calculated angle: " + bulletAngle + "; Body angle: " + this.bodyStartAngle);
    }

    private void resetAnimationData() {
        animation.reset();
        animation.setAnimationStatement(SpriteAnimation.ACTIVE);
        ended = false;

        System.out.println("Animation was reset");
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
            float angle = animationGraphicAngle - bulletAngle + PApplet.degrees(attackedBody.getAngle())-bodyStartAngle;
            Vec2 splashActualPos = PhysicGameWorld.controller.getBodyPixelCoord(attackedBody).add(basicSplashPos);
            animation.draw(gameCamera, splashActualPos, angle, false);
        }
    }
}
