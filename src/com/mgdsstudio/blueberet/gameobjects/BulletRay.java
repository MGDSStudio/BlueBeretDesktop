package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;

import java.util.ArrayList;

public class BulletRay implements IDrawable, RayCastCallback{
    private final static int BULLET_NORMAL_START_VELOCITY = 300;
    private int fromWeapon = FirearmsWeapon.HANDGUN;
    public int shotStartingFrame;
    private Weapon weapon;
    private float shotAngle;
    private Vec2 shotPosition;
    private int shotLength = 300;
    private boolean oneFrameIsOut = false;

    private RayCastCallback callback;
    private Vec2 collisionPoint;
    private boolean shotEnded = false;
    private boolean isColliding;
    private ArrayList<Body> collisionBodies;


    public BulletRay(Weapon weapon, float shotAngleInDegrees, Vec2 shotPosition){
        this.weapon = weapon;
        this.shotPosition = shotPosition;
        fromWeapon = weapon.type;
        this.shotAngle = shotAngleInDegrees;
        oneFrameIsOut = false;
        collisionBodies = new ArrayList<>();
    }

    public int framesAfterShot() {
        return (int) (Program.engine.frameCount-shotStartingFrame);
    }


    public void update(GameRound gameRound) {
        if (!shotEnded) {
            if (oneFrameIsOut) {
                //calculateRaycast();
                shotEnded = true;
            }
            if (!oneFrameIsOut) oneFrameIsOut = true;
        }
    }

    public boolean isShotEnded() {
        return shotEnded;
    }

    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        //The point of collision with the fixture in box coordinates
        System.out.println("Coalision with fixture: ");
        System.out.println(fixture + " at point " + PhysicGameWorld.controller.coordWorldToPixels(point));

        collisionPoint.set(point);
        //-1 to ignore this fixture and continue
        //0 to terminate the ray cast
        //fraction to clip the ray at this point
        //1 to not the clip the ray and continues

        isColliding=true;
        collisionPoint.set(point);
        if(!collisionBodies.contains(fixture.getBody()))
            collisionBodies.add(fixture.getBody());
        return fraction;

    }


    /*
    private void calculateAttack() {

        collisionPoint =new Vec2();
        callback=new RayCastCallback() {
            @Override
            public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
                //The point of collision with the fixture in box coordinates
                System.out.println("Coalision with fixture: ");
                System.out.println(fixture + " at point " + PhysicGameWorld.controller.coordWorldToPixels(point));

                collisionPoint.set(point);
                //-1 to ignore this fixture and continue
                //0 to terminate the ray cast
                //fraction to clip the ray at this point
                //1 to not the clip the ray and continues

                isColliding=true;
                collisionPoint.set(point);
                if(!collisionBodies.contains(fixture.getBody()))
                    collisionBodies.add(fixture.getBody());
                return fraction;

            }
        };

    }*/

    public void calculateRaycast(){
        Vec2 start= PhysicGameWorld.controller.coordPixelsToWorld(shotPosition);
        Vec2 end = new Vec2(start.x+PhysicGameWorld.controller.scalarWorldToPixels(shotLength)* Program.engine.cos(Program.engine.radians(shotAngle)), start.y+PhysicGameWorld.controller.scalarWorldToPixels(shotLength)* Program.engine.sin(Program.engine.radians(shotAngle)));

        //callback listens to the query and calls reportRayFixture method on colliding with a fixture
        //start and end are start and end points of the ray
        try{
            PhysicGameWorld.controller.world.raycast(callback, start, end);
        }
        catch (Exception e){
            System.out.println("Exception by ray casting: " + e);
        }
    }

    public Vec2 GetCollisionPoint(){
        return collisionPoint;
    }

    public boolean DidRayCollide(){
        return isColliding;
    }


    @Override
    public void draw(GameCamera gameCamera) {

        if (Program.debug) {
            //Programm.objectsFrame.beginDraw();
            Program.objectsFrame.rectMode(PApplet.CENTER);
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(shotPosition.x - gameCamera.getActualXPositionRelativeToCenter(), shotPosition.y - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.rotate(Program.engine.radians(shotAngle));
            Program.objectsFrame.pushStyle();
            Program.objectsFrame.strokeWeight(2);
            Program.objectsFrame.line(0,0, shotLength,0);
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
            //Programm.objectsFrame.endDraw();

        }

    }


}
