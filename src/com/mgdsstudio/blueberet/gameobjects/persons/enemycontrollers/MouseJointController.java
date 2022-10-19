package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

class MouseJointController {

    private Flag patrolZone;
    protected MouseJoint spring;
    protected Coordinate nextWayPoint;
    protected boolean alreadyOnWaypoint;

    protected Person enemy;

    MouseJointController(Flag patrolZone, Person enemy) {
        this.patrolZone = patrolZone;
        this.enemy = enemy;
        init();
    }

    private void init() {
        createSpring(PhysicGameWorld.controller);
    }

    protected void createSpring(Box2DProcessing worldController) {
        MouseJointDef md = new MouseJointDef();
        md.bodyA = worldController.getGroundBody();
        md.bodyB = enemy.body;
        Vec2 mp = enemy.body.getPosition();
        //Vec2 mp = worldController.coordPixelsToWorld(enemy.getPixelPosition().x, enemy.getPixelPosition().y);
        md.target.set(mp);
        if (worldController.equals(PhysicGameWorld.controller)) md.maxForce = (float) (150.0);
        else md.maxForce = (float) (20.0);
        md.frequencyHz = 4.5f;
        md.dampingRatio = 0.75f* Program.NORMAL_FPS/40f;
        spring = (MouseJoint) worldController.world.createJoint(md);
        //System.out.println("Mouse joint was created");
    }


    private void flyToWaypoint(GameRound gameRound) {
        spring.setTarget(PhysicGameWorld.coordPixelsToWorld(new PVector(nextWayPoint.x, nextWayPoint.y)));
        if (!alreadyOnWaypoint){
            if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, nextWayPoint.x, nextWayPoint.y) < enemy.getWidth()){
                createNextRandomWayPoint(gameRound);
                //restartTimer();
                alreadyOnWaypoint = true;
            }
        }
        else {
            //if (flyingOnPlaceTimer != null && flyingOnPlaceTimer.isTime()){
                alreadyOnWaypoint = false;
            //}
        }
    }


    private void createNextRandomWayPoint(GameRound gameRound) {
        /*
        if (statement == STATEMENT_FLY_TO_RANDOM_PLACE){
            boolean wayPointFounded = false;
            float randomX = 0;
            float randomY = 0;
            float minX = patrolZone.getPosition().x-patrolZone.getWidth()/2f;
            float minY = patrolZone.getPosition().y-patrolZone.getHeight()/2f;
            float maxX = patrolZone.getPosition().x+patrolZone.getWidth()/2f;
            float maxY = patrolZone.getPosition().y+patrolZone.getHeight()/2f;
            while (!wayPointFounded){
                randomX = Program.engine.random(minX, maxX);
                randomY = Program.engine.random(minY, maxY);
                wayPointFounded  = true;
            }
            if (nextWayPoint == null) nextWayPoint = new Coordinate(randomX, randomY);
            else {
                nextWayPoint.x = randomX;
                nextWayPoint.y = randomY;
            }
        }
        else if (statement == STATEMENT_FLY_TO_PLAYER){
            float x = gameRound.getPlayer().getPixelPosition().x;
            float y = gameRound.getPlayer().getPixelPosition().y;
            if (nextWayPoint == null) nextWayPoint = new Coordinate(x, y);
            else {
                nextWayPoint.x = x;
                nextWayPoint.y = y;
            }
        }
        if (Program.debug){
            System.out.println("Next way point for the dragonfly: " + nextWayPoint.x + ", " + nextWayPoint.y);
        }*/
    }

    /*
    private void restartTimer() {
        if (flyingOnPlaceTimer == null){
            flyingOnPlaceTimer = new Timer(getTimeForOnPlaceWaiting());
        }
        else flyingOnPlaceTimer.setNewTimer(getTimeForOnPlaceWaiting());
    }*/


}
