package com.mgdsstudio.blueberet.graphic.splashes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;

public class Splash implements IDrawable {
    protected byte layer = IndependentOnScreenAnimation.IN_FRONT_OF_ALL;
    protected boolean ended;
    protected int bulletAngle;
    protected int bodyStartAngle;
    protected Body attackedBody;
    protected Vec2 basicSplashPos;
    protected Vec2 secondarySplashPos;
    protected Timer splashFallingTimer;
    protected int actualAngle;
    protected final static int TIME_TO_FALL = (int)(Program.NORMAL_FPS*1000f/ Program.NORMAL_FPS);
    protected final static byte MAX_DELTA_ANGLE_FOR_FALLING = 30;
    public final static boolean STATIC = true;
    public final static boolean DYNAMIC = false;
    protected boolean type;

    public Splash(Body attackedBody, Vec2 basicSplashPos, int bulletAngle, boolean type){
        initBasicData(attackedBody, basicSplashPos, bulletAngle, type);
    }

    protected void initBasicData(Body attackedBody, Vec2 centerPos, int bulletAngle, boolean type){
        this.attackedBody = attackedBody;
        this.basicSplashPos = centerPos;
        this.bulletAngle = bulletAngle;
        this.bodyStartAngle = (int)(PApplet.degrees(attackedBody.getAngle()));
        this.type = type;
    }

    public void update(){
        if (!ended){

        }
    }



    public byte getLayer(){
        return layer;
    }

    public void setLayer(byte layer) {
        this.layer = layer;
    }

    public boolean isEnded(){
        return ended;
    }

    @Override
    public void draw(GameCamera gameCamera) {
        System.out.println("This draw must be overriden");
    }

    public boolean canBeDeleted() {
        if (ended) return true;
        else return false;
    }

    public void loadAnimation(MainGraphicController mainGraphicController){

    }


    /*
    public void draw(GameCamera gameCamera) {
        if (!ended) {
            animation.update();
            float angle = 0 - bulletAngle + PApplet.degrees(attackedBody.getAngle())-bodyStartAngle;
            Vec2 splashActualPos = PhysicGameWorld.controller.getBodyPixelCoord(attackedBody).add(relativePos);
            animation.draw(gameCamera, splashActualPos, angle, false);
        }
    }*/
}
