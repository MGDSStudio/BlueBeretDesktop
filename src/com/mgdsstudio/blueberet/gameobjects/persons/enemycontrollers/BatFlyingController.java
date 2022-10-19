package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

class BatFlyingController extends MouseJointController{
    private final Coordinate globalWaypoint = new Coordinate(0,0);
    private final Vec2 mutGlobalWaypointInWorldCoord = new Vec2();
    //private int criticalDistance;
    private boolean started;
    private int updatingFrame = 1;
    private final PatrolAreaDevider patrolAreaDevider;
    private boolean simpleFlight = true;

    BatFlyingController(Flag patrolZone, Person enemy, int updatingFrame) {
        super(patrolZone, enemy);
        //criticalDistance = enemy.getPersonWidth()/2;
        this.updatingFrame = updatingFrame;
        patrolAreaDevider = new PatrolAreaDevider(patrolZone, 7, PatrolAreaDevider.USE_ONLY_PERIMETER);

    }

    void startFlyingToRandomEnd(GameRound gameRound){
        started  = true;
        createRandomWaypointInZone(gameRound);
        if (Program.debug) System.out.println("Bat is now at: " + (int)enemy.getPixelPosition().x + "x" + (int)enemy.getPixelPosition().y);
    }

    void startFlyingToPlayer(GameRound gameRound){
        started  = true;
        createWaypointOnPlayer(gameRound);
        if (Program.debug) System.out.println("Bat is now fly to player from: " + (int)enemy.getPixelPosition().x + "x" + (int)enemy.getPixelPosition().y);
    }

    private void createWaypointOnPlayer(GameRound gameRound){
        //Coordinate newPos = patrolAreaDevider.generateRandomPoint(enemy.getPixelPosition().x, enemy.getPixelPosition().y, gameRound);
        globalWaypoint.x = gameRound.getPlayer().getPixelPosition().x;
        globalWaypoint.y = gameRound.getPlayer().getPixelPosition().y;
        Vec2 worldCoord = PhysicGameWorld.coordPixelsToWorld(globalWaypoint);
        mutGlobalWaypointInWorldCoord.x = worldCoord.x;
        mutGlobalWaypointInWorldCoord.y = worldCoord.y;
    }

    void createRandomWaypointInZone(GameRound gameRound){
        Coordinate newPos = patrolAreaDevider.generateRandomPoint(enemy.getPixelPosition().x, enemy.getPixelPosition().y, gameRound);
        globalWaypoint.x = newPos.x;
        globalWaypoint.y = newPos.y;
        Vec2 worldCoord = PhysicGameWorld.coordPixelsToWorld(globalWaypoint);
        mutGlobalWaypointInWorldCoord.x = worldCoord.x;
        mutGlobalWaypointInWorldCoord.y = worldCoord.y;
    }

    void createRandomWaypointAroundEnemy(GameRound gameRound){

    }

    void createWaypointAwayFromPlayer(GameRound gameRound){

    }


    public void update(GameRound gameRound) {
        if (started) {
            if (Program.engine.frameCount%updatingFrame == 0) {
                if (isEnemyOnEndPoint()) {
                    createRandomWaypointInZone(gameRound);
                }
                else {
                    flyToCoordinate(gameRound);
                }
            }
        }
    }

    private void flyToCoordinate(GameRound gameRound) {
        if (!started){
            startFlyingToRandomEnd(gameRound);
        }
        else {
            if (simpleFlight) {
                spring.setTarget(PhysicGameWorld.coordPixelsToWorld(globalWaypoint));
            }
        }
    }

    private boolean isEnemyOnEndPoint() {
        if (PhysicGameWorld.isPointInBody(mutGlobalWaypointInWorldCoord, enemy.body)){
            return true;
        }
        else return false;
    }

    public boolean isStarted() {
        return started;
    }
}
