package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Spider;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;

import java.util.ArrayList;

public class SpiderController extends EnemyController {


    public final static int MOVEMENT_ON_GROUND = 0;
    public final static int GO_ON_WALL = 2;
    public final static int GO_ON_WALL_AND_ATTACK = 5;
    public final static int MOVEMENT_ON_GROUND_AND_ATTACK = 4;

    private final static int TIME_FOR_STOPPING_BY_WALL = 1000;


    private boolean wallFounded;
    private final static boolean WALL_RIGHT = true, WALL_LEFT = false;
    private boolean wallSide;
    private float step = 2f;
    private final float maxTestDistance = 2000f;
    private int normalYVelocity = 4;
    private final boolean TO_DOWN = true;
    private final boolean TO_UP = false;
    private boolean actualYMovementDirection = TO_DOWN;
    private final static Vec2 mutableTestPoint = new Vec2();

    private RoundElement wall;

    private NetShootingController netShootingController;

    public SpiderController(Enemy person, int behaviourType) {
        super(person, behaviourType);
        timeForStoppingByWall = TIME_FOR_STOPPING_BY_WALL;
        stayingAfterFalling();
        step = Program.engine.random(step - step * 0.4f, step + step * 0.4f);
        if (behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK || behaviourType == GO_ON_WALL_AND_ATTACK) {
            netShootingController = new NetShootingController((Spider) enemy, this);
        }
    }

    private void jumpToNearestWall(GameRound gameRound) {
        RoundElement leftNearestWall = getNearestWall(gameRound, WALL_LEFT);
        RoundElement rightNearestWall = getNearestWall(gameRound, WALL_RIGHT);
        if (leftNearestWall == null && rightNearestWall != null) {
            wallSide = WALL_RIGHT;
            transferSpiderToWall(WALL_RIGHT, rightNearestWall);
        } else if (leftNearestWall != null && rightNearestWall == null) {
            transferSpiderToWall(WALL_LEFT, leftNearestWall);
            wallSide = WALL_LEFT;
        } else if (leftNearestWall != null && rightNearestWall != null) {
            transferSpiderToNearestFromTwoWalls(leftNearestWall, rightNearestWall);
        } else {
            behaviourType = MOVEMENT_ON_GROUND;
        }
    }

    private void transferSpiderToNearestFromTwoWalls(RoundElement leftNearestWall, RoundElement rightNearestWall) {
        float distToLeft = getXDistanceToRoundElement(leftNearestWall);
        float distToRight = getXDistanceToRoundElement(rightNearestWall);
        if (Program.debug) System.out.println("Dist: " + distToLeft + " and " + distToRight);
        if (distToRight > 0) {
            if (distToLeft <= distToRight) {
                transferSpiderToWall(WALL_LEFT, leftNearestWall);
                wallSide = WALL_LEFT;

            } else {
                transferSpiderToWall(WALL_RIGHT, rightNearestWall);
                wallSide = WALL_RIGHT;
            }
        } else {
            behaviourType = MOVEMENT_ON_GROUND;
        }
    }

    private void transferSpiderToWall(boolean wallSide, RoundElement nearestWall) {
        enemy.body.setGravityScale(0);
        float distanceToWall = getXDistanceToRoundElement(nearestWall);
        if (wallSide == WALL_LEFT) distanceToWall = -distanceToWall;
        float angle = -PApplet.PI / 2f;
        if (wallSide == WALL_RIGHT) {
            angle = -PApplet.PI / 2f;
        }
        wall = nearestWall;
        enemy.body.setTransform(new Vec2(enemy.body.getPosition().x + distanceToWall, enemy.body.getPosition().y), angle);
    }

    private float getXDistanceToRoundElement(RoundElement nearestWall) {
        float spiderX = PhysicGameWorld.controller.coordPixelsToWorld(enemy.getPixelPosition()).x;
        Body body = nearestWall.body;
        float distance = 0;
        float actualStep = step;
        if (nearestWall.getPixelPosition().x < enemy.getPixelPosition().x) {
            actualStep = -step;
        }
        int iterationsNumber = (int) (PApplet.abs(maxTestDistance / step));
        actualStep = PhysicGameWorld.controller.scalarPixelsToWorld(actualStep);
        Vec2 testPos = PhysicGameWorld.coordPixelsToWorld(enemy.getPixelPosition());
        for (int j = 0; j < iterationsNumber; j++) {
            testPos.x += actualStep;
            if (body.getFixtureList().testPoint(testPos)) {
                float actualDistance = PApplet.abs(spiderX - testPos.x);
                //System.out.println("Distance is " + PhysicGameWorld.controller.scalarWorldToPixels(actualDistance) + " itt: " + j);
                return actualDistance;
            } else {
                //System.out.println("Wall is on: " + PhysicGameWorld.controller.coordWorldToPixels(nearestWall.body.getPosition()) + "; test point: " + PhysicGameWorld.controller.coordWorldToPixels(testPos) + "");
            }
        }
        System.out.println("Distance is not determined !" + iterationsNumber);
        return 0f;
    }

    private RoundElement getNearestWall(GameRound gameRound, boolean side) {
        ArrayList<RoundElement> roundElements = gameRound.roundElements;
        float actualStep;
        int iterationsNumber = (int) (PApplet.abs(maxTestDistance / step));
        if (side == WALL_LEFT) actualStep = -1 * (step);
        else actualStep = +step;
        actualStep = PhysicGameWorld.controller.scalarPixelsToWorld(actualStep);
        int nearestNumber = -1;
        float nearestDistance = 99999f;
        float spiderX = PhysicGameWorld.controller.coordPixelsToWorld(enemy.getPixelPosition()).x;
        System.out.println("Step for side: " + side + " is " + actualStep + " and itterations: " + iterationsNumber);
        for (int i = 0; i < roundElements.size(); i++) {
            Vec2 testPos = PhysicGameWorld.controller.coordPixelsToWorld(enemy.getPixelPosition());
            Body body = roundElements.get(i).body;
            for (int j = 0; j < iterationsNumber; j++) {
                testPos.x += actualStep;
                if (body.getFixtureList().testPoint(testPos)) {
                    float actualDistance = PApplet.abs(spiderX - testPos.x);
                    if (actualDistance < nearestDistance) {
                        nearestDistance = actualDistance;
                        nearestNumber = i;
                    }
                }
            }
        }
        //System.out.println("Nearest round element for side " + side + " has number " + nearestNumber + " and distance to his " + PhysicGameWorld.controller.scalarWorldToPixels(nearestDistance));
        if (nearestNumber < 0) return null;
        else return roundElements.get(nearestNumber);
    }

    private void braking() {
        if (movementDirection == LEFT) {
            mutableBrakingAccelerate.x = enemy.body.getMass() * 32.2f;
        } else {
            mutableBrakingAccelerate.x = -enemy.body.getMass() * 32.2f;
        }
        enemy.body.applyForceToCenter(mutableBrakingAccelerate);
    }

    private void mirrorBody() {
        if (isOnWall()) {
            if (actualYMovementDirection == TO_DOWN) {
                Spider spider = (Spider) enemy;
                spider.mirrorBody(TO_DOWN);
            } else {
                if (actualYMovementDirection == TO_UP) {
                    Spider spider = (Spider) enemy;
                    spider.mirrorBody(TO_UP);
                }
            }
        }
    }
    /*
    private void mirrorBody() {
        if (isOnWall()) {
            if (actualYMovementDirection == TO_DOWN) {
                Spider spider = (Spider) enemy;
                spider.mirrorBody(TO_DOWN);
            } else {
                if (actualYMovementDirection == TO_UP) {
                    Spider spider = (Spider) enemy;
                    spider.mirrorBody(TO_UP);
                }
            }
        }
    }
     */


    private void updateGraphicFlip() {
        if (isOnWall()) {
            if (wallSide == WALL_LEFT) {
                graphicFlipX = false;
                if (actualYMovementDirection == TO_DOWN) {
                    graphicFlipY = false;
                } else {
                    graphicFlipX = false;
                    graphicFlipY = true;
                }
            } else {
                graphicFlipY = true;
                if (actualYMovementDirection == TO_DOWN) {
                    graphicFlipX = false;
                } else graphicFlipX = true;

                /*
                graphicFlipY = true;
                if (actualYMovementDirection == TO_DOWN) {
                    graphicFlipX = false;
                }
                else graphicFlipX = true;
                 */

            }
        } else {
            if (graphicFlipY == true) graphicFlipY = false;
        }
    }

    private void updateMovementAlongWall(GameRound gameRound) {
        boolean mustFall = false;
        if (Program.engine.frameCount % 15 == 1) {
            if (wall == null) mustFall = true;
            else if (wall.getLife() < 1) mustFall = true;
        }
        if (!mustFall) mustFall = mustSpiderFallFromWallOnPlayer(gameRound);
        if (!mustFall) {
            //if (!mustSpiderFallFromWallOnPlayer(gameRound)) {

            Vec2 linearVelocity = enemy.body.getLinearVelocity();
            if (actualYMovementDirection == TO_DOWN) {
                linearVelocity.y = -normalYVelocity;
            } else {
                linearVelocity.y = +normalYVelocity;
            }
            if (wallSide == WALL_LEFT) {
                linearVelocity.x = -0.5f;
            } else linearVelocity.x = 0.5f;
            if (Program.engine.frameCount % 3 == 0) {
                if (mustBeMovementAlongYChanged(gameRound)) {
                    actualYMovementDirection = !actualYMovementDirection;
                    Spider spider = (Spider) enemy;
                    if (actualYMovementDirection == TO_UP) {
                        spider.mirrorBody(TO_UP);
                    } else spider.mirrorBody(TO_DOWN);
                }
            }

            //}
            //else fall();
        } else {
            fall();
            System.out.println("Wall under the spider is crushed");
        }
    }

    private boolean mustSpiderFallFromWallOnPlayer(GameRound gameRound) {
        if (behaviourType == GO_ON_WALL) return false;
        else if (behaviourType == GO_ON_WALL_AND_ATTACK) {
            float deltaX = gameRound.getPlayer().getPixelPosition().x - enemy.getPixelPosition().x;
            if (enemy.getWidth() > PApplet.abs(deltaX * 1.5f)) {
                if (Program.debug)
                    System.out.println("We do not test. Maybe we have an object between spider at " + enemy.getPixelPosition() + " and player " + gameRound.getPlayer().getPixelPosition() + "");
                return true;
            } else return false;
        } else return false;
    }

    private void fall() {
        if (behaviourType == GO_ON_WALL) behaviourType = MOVEMENT_ON_GROUND;
        else if (behaviourType == GO_ON_WALL_AND_ATTACK) behaviourType = MOVEMENT_ON_GROUND_AND_ATTACK;
    }

    private boolean areThereFallsUnderFeet(GameRound gameRound) {
        float yToTestPoint = enemy.getPixelPosition().y + enemy.getHeight() / 2;
        if (actualYMovementDirection == TO_UP) {
            yToTestPoint = enemy.getPixelPosition().y - enemy.getHeight() / 2;
        }
        float xToTestPoint = enemy.getPixelPosition().x + enemy.getWidth() / 1.5f;
        if (wallSide == WALL_LEFT) {
            xToTestPoint = enemy.getPixelPosition().x - enemy.getWidth() / 1.5f;
        }
        mutableTestPoint.x = xToTestPoint;
        mutableTestPoint.y = yToTestPoint;
        for (RoundElement roundElement : gameRound.roundElements) {
            if (roundElement.body.getFixtureList().testPoint(PhysicGameWorld.controller.coordPixelsToWorld(mutableTestPoint))) {
                return false;
            }
        }
        if (Program.engine.frameCount % 4 == 0) {
            //DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, mutableTestPoint);
            //gameRound.addDebugGraphic(debugGraphic);
        }
        return true;
    }

    private boolean mustBeMovementAlongYChanged(GameRound gameRound) {
        if (areThereFallsUnderFeet(gameRound)) return true;
        else if (areTherePersonsOrLevelElementsOnTheWay(gameRound)) return true;
        else if (areThereClearingZonesOnTheWay(gameRound)) return true;
        else return false;
    }

    private boolean areThereClearingZonesOnTheWay(GameRound gameRound) {
        float yToTestPoint = enemy.getPixelPosition().y + enemy.getHeight() / 0.5f;
        if (actualYMovementDirection == TO_UP) {
            yToTestPoint = enemy.getPixelPosition().y - enemy.getHeight() / 0.5f;
        }
        float xToTestPoint = enemy.getPixelPosition().x;
        mutableTestPoint.x = xToTestPoint;
        mutableTestPoint.y = yToTestPoint;
        for (ObjectsClearingZone zone : gameRound.getObjectsClearingZones()) {
            if (zone.getFlag().inZone(mutableTestPoint)) {
                if (zone.activatingCondition != ObjectsClearingZone.DELETE_ROUND_ELEMENTS && zone.activatingCondition != ObjectsClearingZone.DELETE_CORPSES) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areTherePersonsOrLevelElementsOnTheWay(GameRound gameRound) {
        float yToTestPoint = enemy.getPixelPosition().y + enemy.getHeight() / 0.5f;
        if (actualYMovementDirection == TO_UP) {
            yToTestPoint = enemy.getPixelPosition().y - enemy.getHeight() / 0.5f;
        }
        float xToTestPoint = enemy.getPixelPosition().x;
        mutableTestPoint.x = xToTestPoint;
        mutableTestPoint.y = yToTestPoint;
        for (Person toTestPerson : gameRound.getPersons()) {
            if (!toTestPerson.equals(gameRound.getPlayer())) {
                for (Fixture f = toTestPerson.body.getFixtureList(); f != null; f = f.getNext()) {
                    if (!f.getBody().equals(enemy.body)) {
                        if (f.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(mutableTestPoint))) {
                            return true;
                        }
                    }
                }
            }
        }
        for (RoundElement toTestRoundElement : gameRound.roundElements) {
            if (!toTestRoundElement.equals(gameRound.getPlayer())) {
                for (Fixture f = toTestRoundElement.body.getFixtureList(); f != null; f = f.getNext()) {
                    if (!f.getBody().equals(enemy.body)) {
                        if (f.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(mutableTestPoint))) {
                            //System.out.println("Round Element is on the way");
                            //DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.ROUND, mutableTestPoint, 15);
                            //gameRound.addDebugGraphic(debugGraphic);
                            return true;
                        }
                    }
                }
            }
        }
        for (RoundPipe toTestPipe : gameRound.getRoundPipes()) {
            if (!toTestPipe.equals(gameRound.getPlayer())) {
                for (Fixture f = toTestPipe.body.getFixtureList(); f != null; f = f.getNext()) {
                    if (!f.getBody().equals(enemy.body)) {
                        if (f.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(mutableTestPoint))) {
                            System.out.println("Round pipe is on the way");
                            //DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.ROUND, mutableTestPoint, 15);
                            //gameRound.addDebugGraphic(debugGraphic);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    /*Public*/

    @Override
    public void kill() {
        if (netShootingController != null){
            netShootingController.kill();
        }
        if (Program.debug) System.out.println("This enemy dies without animations");
    }


    private void stayingAfterFalling() {
        stayingTimer = new Timer(TIME_FOR_STOPPING_BY_WALL);
        enemy.body.setLinearVelocity(new Vec2(0f, enemy.body.getLinearVelocity().y));
    }

    private void updateMovementOnGround(GameRound gameRound) {
        if (enemy.getStatement() != Person.IN_AIR) {
            boolean mustMoving = true;
            if (behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK){
                netShootingController.update(gameRound);
                if (netShootingController.isAttackingNow()){
                    mustMoving = false;
                }
            }
            if (mustMoving) move();
        }
    }

    /*
    private void attack(GameRound gameRound) {
        netShootingController.updateAttack(gameRound);
    }
*/

    private void move(){
        if (movementDirection == LEFT) {
            if (graphicFlipX == false) graphicFlipX = true;
            enemy.move(true, Person.TO_LEFT);
        } else if (movementDirection == RIGHT) {
            if (graphicFlipX == true) graphicFlipX = false;
            enemy.move(true, Person.TO_RIGHT);
        }
        if (isBlockedAlongX() == true) {
            changeMovementDirection();
            Spider spider = (Spider) enemy;
            spider.mirrorBody();
        }
    }


    @Override
    public boolean isItGoing() {
        if (enemy.getStatement() == Person.IN_AIR) {
            if (isOnWall()) return true;
            else return false;
        } else {
            return true;
        }
    }

    @Override
    public float getGraphicAngle(float a) {
        if (isOnWall()) {
            if (wallSide == WALL_LEFT) {
                if (actualYMovementDirection == TO_UP) {
                    return a + PApplet.PI;
                }
            }
        }
        return a;
    }

    @Override
    public void attacked() {
        if (behaviourType == GO_ON_WALL) {
            if (enemy.isAlive()) releaseFromWall(false);
            else releaseFromWall(true);
        }
    }


    public boolean isOnWall() {
        if (wallFounded) {
            return true;
        } else return false;
    }

    @Override
    public boolean isStartedToDie() {
        return false;
    }

    public void update(GameRound gameRound) {
        if (behaviourType == MOVEMENT_ON_GROUND || behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK) {
            boolean groundUnderFeet = areThereGroundUnderFeet(gameRound, movementDirection);
            boolean mustAttack = false;
            if (groundUnderFeet) {
                updateMovementOnGround(gameRound);

                //else updatePlayerAttack(gameRound);
            } else {
                boolean clearingZonesUnderFeet = areThereClearingZoneInFront(gameRound);
                if (!clearingZonesUnderFeet) {
                    /*if (behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK) {
                        netShootingController.update(gameRound);
                        mustAttack = mustSpiderAttackPlayer(gameRound);
                    }
                    if (!mustAttack) updateMovementOnGround();
                    else updatePlayerAttack(gameRound);*/
                    updateMovementOnGround(gameRound);
                } else {
                    braking();
                }
            }
        } else if (behaviourType == GO_ON_WALL || behaviourType == GO_ON_WALL_AND_ATTACK) {
            if (!wallFounded) {
                jumpToNearestWall(gameRound);
                mirrorBody();
                wallFounded = true;
            }
            updateMovementAlongWall(gameRound);
            updateGraphicFlip();
        }
    }





    public void releaseFromWall(boolean withRotation) {
        if (behaviourType == GO_ON_WALL) behaviourType = MOVEMENT_ON_GROUND;
        else if (behaviourType == GO_ON_WALL_AND_ATTACK) behaviourType = MOVEMENT_ON_GROUND_AND_ATTACK;
        enemy.body.setTransform(enemy.body.getPosition(), 0f);
        enemy.body.setGravityScale(1);
        float xVelocity = -3;
        if (wallSide == WALL_LEFT) xVelocity = 3;
        enemy.body.applyLinearImpulse(new Vec2(xVelocity, 0), enemy.body.getPosition(), true);
        Spider spider = (Spider) enemy;
        if (wallSide == WALL_LEFT) {
            spider.mirrorBody(Person.TO_RIGHT);
            if (actualYMovementDirection == TO_DOWN) {
                graphicFlipX = false;
                graphicFlipY = false;
            } else {
                graphicFlipX = false;
                graphicFlipY = true;
            }
        } else {
            spider.mirrorBody(Person.TO_LEFT);
            if (actualYMovementDirection == TO_DOWN) {
                graphicFlipX = true;
                graphicFlipY = false;
            } else {
                graphicFlipX = true;
                graphicFlipY = false;
            }
        }
        if (withRotation) {
            final float rotatingVelocity = -enemy.body.getMass() * 10f;
            if (wallSide == WALL_LEFT) enemy.body.setAngularVelocity(rotatingVelocity);
            else enemy.body.setAngularVelocity(-rotatingVelocity);
            System.out.println("Rotating velocity: " + rotatingVelocity);
        }
    }

    @Override
    public int getActualAnimation() {
        if (isItAttacks()) return EnemiesAnimationController.SHOT;
        else return EnemiesAnimationController.GO;
        /*if (behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK){
            if (netShootingController.isAttackingNow()){
                return EnemiesAnimationController.SHOT;
            }
            else return EnemiesAnimationController.GO;
        }
        else return EnemiesAnimationController.GO;*/
    }

    public boolean isItAttacks() {
        if (behaviourType == MOVEMENT_ON_GROUND_AND_ATTACK){
            if (netShootingController.isAttackingNow()){
                return true;
            }
            else return false;
        }
        else return false;
    }

}



