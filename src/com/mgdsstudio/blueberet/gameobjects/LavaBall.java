package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

public class LavaBall extends RoundCircle{
    long appearingTime;
    private Timer layingOnGroundTimer;
    boolean layOnGround = false;
    final static private float MAXIMAL_LINEAR_VELOCITY_FOR_LAYING_BALL = 20f;
    final static private int COLOR_CHANGING_TIME = 2000;
    static private float colorChangingProFrame = 1;

    //int startColor = Game2D.engine.color(247,64,12);
    int basicColor = Program.engine.color(113,108,106, 0);
    int tintColor = Program.engine.color(113,108,106, 0);
    final static private int MINIMAL_TINT_FOR_NOT_FLAMING_LAVA_BALL = 125;
    final static public float MAX_TRACK_LENGTH = 80;
    private float maxTrackVelocity = 5;
    final static private float MIN_TRACK_LENGTH = 25;
    final static private float MIN_TRACK_VELOCITY = 0;

    public LavaBall(Vec2 position, int radius, int life, boolean withSpring, BodyType bodyType) {
        super(position, radius, life, withSpring, bodyType);
        appearingTime = Program.engine.millis();
        calculateColorChangingStep();
    }

    public void saveMaxTrackVelocity(){
        maxTrackVelocity =  PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().length()));
        //System.out.println("Max velocity: " + maxTrackVelocity);
    }

    private void calculateColorChangingStep(){
        colorChangingProFrame = (255f- MINIMAL_TINT_FOR_NOT_FLAMING_LAVA_BALL)/COLOR_CHANGING_TIME;
        //Game2D.engine.alpha(startColor);
        //float colorChangingProFrame = Game2D.engine.color
        //startColor.
    }

    public void update(){
        /*
        if (actualAlphaForTint > 128) {
            tintColor = Game2D.engine.color(255, actualAlphaForTint);
            actualAlphaForTint-=(colorChangingProFrame*Game2D.deltaTime);
            sprite.setTint(tintColor);
            spriteAnimation.setTint(tintColor);
            //System.out.println("Tint color is updated " + actualAlphaForTint);
        }
        else {
            if (actualAlphaForTint != 128) actualAlphaForTint = 0;
        }
        */

        updateLayingStatement();
        //if (layOnGround) System.out.println("Lays");

        //float bodyVelocity = PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().length()));
        //spriteAnimation.setHeight(PApplet.map());


    }

    private void updateLayingStatement(){
        float bodyVelocity = PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().length()));
        //System.out.println("bodyVelocity " + bodyVelocity);
        if (bodyVelocity<MAXIMAL_LINEAR_VELOCITY_FOR_LAYING_BALL){
            if (layingOnGroundTimer == null){
                layingOnGroundTimer = new Timer(COLOR_CHANGING_TIME);
            }
            else {
                layingOnGroundTimer.setNewTimer(COLOR_CHANGING_TIME);
            }

            if (layingOnGroundTimer.isTime()){
                layOnGround = true;
            }

        }
        else {
            if (layingOnGroundTimer != null){
                layingOnGroundTimer = null;
            }
        }

    }

    @Override
    public void draw(GameCamera gameCamera){
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle();
        updateGraphicSidesData();
        if (Program.debug) {
            drawDebugModel(gameCamera, pos,a);
        }
        if (spriteAnimation != null) {
            spriteAnimation.update();
            float angle = new PVector(body.getLinearVelocity().x, body.getLinearVelocity().y).heading();
            float newGraphicWidth = PApplet.map(PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().length())), MIN_TRACK_VELOCITY, maxTrackVelocity, MIN_TRACK_LENGTH, MAX_TRACK_LENGTH);
            spriteAnimation.setWidth((int)newGraphicWidth);

            spriteAnimation.draw(gameCamera, pos, PApplet.degrees(-angle), false);
        }
        if (sprite != null) {
            sprite.draw(gameCamera, body);
        }
        update();
    }
}
