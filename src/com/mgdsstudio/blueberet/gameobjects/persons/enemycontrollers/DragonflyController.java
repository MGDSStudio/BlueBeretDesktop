package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Dragonfly;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

public class DragonflyController  extends EnemyController{
    private Flag patrolZone;
    private MouseJoint spring;
    private Coordinate nextWayPoint;
    private Timer flyingOnPlaceTimer;

    //Flying on place duration
    private final static int LONG = 2000;
    private final static int NORMAL = 1000;
    private final static int SHORT = 500;
    private final int timeToFlyOnPlace;

    //Statements
    public final static int STATEMENT_FLY_TO_RANDOM_PLACE = 1;
    public final static int STATEMENT_FLY_ON_PLACE = 2;
    public final static int STATEMENT_FLY_TO_PLAYER = 3;

    //Behaviour types
    public final static int BEHAVIOUR_PATROL = 1;
    public final static int BEHAVIOUR_FLY_TO_PLAYER_WITHOUT_PAUSES = 2;
    public final static int BEHAVIOUR_FLY_TO_PLAYER_WITH_PAUSES = 3;

    private boolean startToDie, deadAfterDyingAnimation;
    private final int deadZoneWidth, deadZoneHeight;
    private float springMovementVelocity;
    private boolean alreadyOnWaypoint;

    public DragonflyController(Dragonfly dragonfly, int behaviourModel, int timeToFlyOnPlace, Flag patrolZone) {
        super(dragonfly, behaviourModel);
        this.patrolZone = patrolZone;
        this.timeToFlyOnPlace = timeToFlyOnPlace;
        springMovementVelocity = dragonfly.getPersonWidth()*1.5f;
        this.deadZoneWidth = (int) dragonfly.getWidth();
        this.deadZoneHeight = (int) dragonfly.getHeight();
        if (behaviourType == BEHAVIOUR_PATROL || behaviourType == BEHAVIOUR_FLY_TO_PLAYER_WITH_PAUSES) {
            statement = STATEMENT_FLY_ON_PLACE;
            restartTimer();
        }
        else if (behaviourType == BEHAVIOUR_FLY_TO_PLAYER_WITHOUT_PAUSES){
            statement = STATEMENT_FLY_TO_PLAYER;
        }
        createSpring(PhysicGameWorld.controller);
    }

    private void createSpring(Box2DProcessing worldController) {
        MouseJointDef md = new MouseJointDef();
        md.bodyA = worldController.getGroundBody();
        md.bodyB = enemy.body;
        Vec2 mp = worldController.coordPixelsToWorld(enemy.getPixelPosition().x, enemy.getPixelPosition().y);
        md.target.set(mp);
        if (worldController.equals(PhysicGameWorld.controller)) md.maxForce = (float) (150.0);
        else md.maxForce = (float) (20.0);
        md.frequencyHz = 3.5f;
        md.dampingRatio = 0.75f* Program.NORMAL_FPS/40f;
        spring = (MouseJoint) worldController.world.createJoint(md);
        //System.out.println("Mouse joint was created");
    }

    private void restartTimer() {
        if (flyingOnPlaceTimer == null){
            flyingOnPlaceTimer = new Timer(getTimeForOnPlaceWaiting());
        }
        else flyingOnPlaceTimer.setNewTimer(getTimeForOnPlaceWaiting());
    }

    private int getTimeForOnPlaceWaiting() {
        return timeToFlyOnPlace;
        //return NORMAL;
    }



    private void dying(GameRound gameRound){
        if (enemy.isActualAnimationEnds()){
            deadAfterDyingAnimation = true;
            Dragonfly dragonfly = (Dragonfly) enemy;
            dragonfly.killAfterStartedToDie();
            gameRound.releaseGameObjects(enemy, PApplet.HALF_PI*3);
            System.out.println("Dragonfly was simplyfied from the dragonfly controller");
        }
    }

    private void flyingOnPlace(GameRound gameRound){
        if (flyingOnPlaceTimer != null){
            if (flyingOnPlaceTimer.isTime()){
                if (behaviourType == BEHAVIOUR_PATROL){
                    statement = STATEMENT_FLY_TO_RANDOM_PLACE;
                    createNextWayPoint(gameRound);
                }
                else if (behaviourType == BEHAVIOUR_FLY_TO_PLAYER_WITH_PAUSES){
                    statement = STATEMENT_FLY_TO_PLAYER;
                    createNextWayPoint(gameRound);
                }
            }
        }
        else restartTimer();
    }

    @Override
    public void update(GameRound gameRound) {
        if (startToDie && !deadAfterDyingAnimation){
            dying(gameRound);
        }
        else {
            if (statement == STATEMENT_FLY_ON_PLACE){
                flyingOnPlace(gameRound);
                //System.out.println("It flies on place");
            }
            if (statement == STATEMENT_FLY_TO_RANDOM_PLACE || statement == STATEMENT_FLY_TO_PLAYER){
                flyToWaypoint(gameRound);
                //System.out.println("It flies to the waypoint");
            }
        }
    }

    private void flyToWaypoint(GameRound gameRound) {
        spring.setTarget(PhysicGameWorld.coordPixelsToWorld(new PVector(nextWayPoint.x, nextWayPoint.y)));
        if (!alreadyOnWaypoint){
            if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, nextWayPoint.x, nextWayPoint.y) < enemy.getWidth()){
                createNextWayPoint(gameRound);
                restartTimer();
                alreadyOnWaypoint = true;
            }
        }
        else {
            if (flyingOnPlaceTimer != null && flyingOnPlaceTimer.isTime()){
                alreadyOnWaypoint = false;
            }
        }
    }


    private void createNextWayPoint(GameRound gameRound) {
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
        }
    }


    @Override
    public void attacked() {

    }

    @Override
    public void kill() {
        if (startToDie == false) {
            startToDie = true;
            lastOrientation = enemy.orientation;
            System.out.println("Snake started to die");
        }
    }

    public void dyingAnimationEnds(){
        PhysicGameWorld.controller.world.destroyJoint(spring);
        enemy.body.setActive(false);
        PhysicGameWorld.controller.destroyBody(enemy.body);
    }

    public boolean isStartedToDie(){
        return startToDie;
    }

    /*
    public void changeMovementDirection() {
        if (movementDirection == LEFT) movementDirection = RIGHT;
        else if (movementDirection == RIGHT) movementDirection = LEFT;
    }*/

    @Override
    public void draw(GameCamera gameCamera) {
        patrolZone.draw(gameCamera);
    }
}
