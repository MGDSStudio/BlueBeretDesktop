package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public abstract class EnemyController{
    protected static final byte LEFT = 1;
    protected static final byte RIGHT = 2;
    protected int movementDirection = RIGHT;
    protected final Person enemy;
    protected int behaviourType;
    protected Timer stayingTimer;
    protected int timeForStoppingByWall = 1000;
    protected boolean lastOrientation;
    protected int statement;
    protected int TO_FIND_CLEARING_ZONE_TEST_POINTS_NUMBER = 55;
    protected int TO_FIND_CLEARING_ZONE_TEST_POINTS_STEP = 15;
    protected boolean graphicFlipX, graphicFlipY;
    protected float graphicAngle;
    protected static final Vec2 mutableTestPointForGroundFinding = new Vec2(0,0);

    protected final static Vec2 mutableBrakingAccelerate = new Vec2(0, 0);

    protected boolean lastGraphicFlip;

    public EnemyController(Person enemy, int behaviourType) {
        this.enemy = enemy;
        this.behaviourType = behaviourType;
    }

    protected boolean isBlockedAlongX(){
        if (Math.abs(enemy.body.getLinearVelocity().x) < (Person.MAX_SPEED_BY_BLOCKED_STATEMENT)) {
            if (stayingTimer.wasSet() == false) stayingTimer.setNewTimer(timeForStoppingByWall);
            else {
                if (stayingTimer.isTime()) {
                    stayingTimer.reset();
                    return true;
                }
                else return false;
            }
            return false;
        }
        else return false;
    }

    public abstract void update(GameRound gameRound);

    public abstract void kill();

    public abstract boolean isStartedToDie();

    public boolean getLastOrientation() {
        return lastOrientation;
    }

    public int getBehaviourType() {
        return behaviourType;
    }

    public int getStatement() {
        return statement;
    }

    public void setContactWithPlayer(Person player) {


    }

    public boolean getGraphicFlipX(){
        return graphicFlipX;
    }

    public boolean getGraphicFlipY(){
        return graphicFlipY;
    }

    public boolean isItGoing(){
        System.out.println("It must be overriden");
        return true;
    }

    public float getGraphicAngle(float a) {
        return a;
    }

    public abstract void attacked();

    protected boolean areThereGroundUnderFeet(GameRound gameRound, int movementDirection) {
        mutableTestPointForGroundFinding.x = enemy.body.getPosition().x;
        if (movementDirection == LEFT){
            mutableTestPointForGroundFinding.x -= PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getWidth()/1.015f);
        }
        else{
            mutableTestPointForGroundFinding.x +=PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getWidth()/1.015f);
        }
        mutableTestPointForGroundFinding.y= enemy.body.getPosition().y-PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getHeight()*0.7f);
        if (Program.engine.frameCount%2 == 0) {
            if (!gameRound.isPointInSomeRoundElements(mutableTestPointForGroundFinding)) {
                return false;
            }
            else {
                if (Program.debug) {
                    //DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, PhysicGameWorld.controller.coordWorldToPixels(mutableTestPointForGroundFinding));
                    //gameRound.addDebugGraphic(debugGraphic);
                }
                return true;
            }
        }
        return true;
    }


    protected boolean areThereClearingZoneInFront(GameRound gameRound){
        float testX = enemy.getPixelPosition().x;
        if (movementDirection == RIGHT){
            testX+= enemy.getWidth();
        }
        else{
            testX-= enemy.getWidth();
        }
        float testY  = enemy.getPixelPosition().y+ enemy.getHeight();
        if (Program.engine.frameCount%2 == 0) {
            for (int i = 0; i < TO_FIND_CLEARING_ZONE_TEST_POINTS_NUMBER; i++) {
                if (gameRound.isPointInSomeDeadZones(testX, testY)) {
                    if (Program.debug) {
                        DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, new Vec2(testX, testY));
                        gameRound.addDebugGraphic(debugGraphic);
                        //System.out.println("Clearing zone founded");
                    }
                    return true;
                }
                testY += TO_FIND_CLEARING_ZONE_TEST_POINTS_STEP;
            }
        }
        return false;
    }

    public void draw(GameCamera gameCamera) {

    }

    public final void changeMovementDirection() {
        if (movementDirection == LEFT) movementDirection = RIGHT;
        else if (movementDirection == RIGHT) movementDirection = LEFT;
    }

    public void draw(EnemiesAnimationController enemiesAnimationController, GameCamera gameCamera, Vec2 pos, float a) {

    }

    protected float getDistanceToPlayer(Person person) {
        return PApplet.dist(person.getPixelPosition().x, person.getPixelPosition().y, enemy.getPixelPosition().x, enemy.getPixelPosition().y);
    }

    public int correctAttackValue(int value) {
        return value;
    }

    public boolean canBeAttacked() {
        return true;
    }

    public int getActualAnimation() {
        return EnemiesAnimationController.GO;
    }

    public boolean isItAttacks() {
        return false;
    }
}
