package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Lizard;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class LizardAttackController {

    final int NOTHING = 0;
    final static int STARTED = 1;
    public final static int LUGGAGE_FORWARD = 2;
    public final static int LUGGAGE_HOLD = 3;
    public final static int LUGGAGE_BACK = 4;
    public final static int ENDED  = 5;
    private int statement = NOTHING;
    private float luggageVel;
    private long startAttackTime, startBackMovementTime;
    private Timer holdLuggageTimer;
    private int timeToHoldLuggage;
    private Coordinate startCoordinate, actualCoordinate;
    private Coordinate center;
    private int attackDistance;
    private float angle;
    private final Vec2 luggageEnd, luggageCenter;
    private final PVector mutTestPointForLuggage = new PVector();
    private final Vec2 mutTestPointForLuggageInWorldCoordinates = new Vec2();

    private final Lizard lizard;
    private float luggageLength;

    LizardAttackController(Lizard lizard, int timeToAttack, int timeToHoldLuggage, Coordinate startCoordinate, int attackDistance) {
        this.lizard = lizard;
        this.timeToHoldLuggage = timeToHoldLuggage;
        this.startCoordinate = startCoordinate;
        this.attackDistance = attackDistance;
        luggageEnd = new Vec2();
        luggageCenter = new Vec2();
        init(timeToAttack);
    }

    private void init(int timeToAttack){
        luggageVel = (float) attackDistance/(float)timeToAttack;
        actualCoordinate = new Coordinate(startCoordinate.x, startCoordinate.y);
        center = new Coordinate(actualCoordinate.x, actualCoordinate.y);
        System.out.println("Velocity: " + luggageVel + " pix/sec");
    }

    void update(float angle, PVector bodyPos, GameRound gameRound) {
        startCoordinate.x = bodyPos.x;
        startCoordinate.y = bodyPos.y;
        if (lizard.isAlive()) {
            if (statement == STARTED) {
                statement = LUGGAGE_FORWARD;
                startAttackTime = LizzardGraphic.engine.millis();
            } else if (statement == LUGGAGE_FORWARD) {
                this.angle = angle;
                updateActualEndPos(gameRound);
            } else if (statement == LUGGAGE_HOLD) {
                updateLuggageHold(gameRound);
            } else if (statement == LUGGAGE_BACK) {
                updateActualEndPos(gameRound);
            } else if (statement == ENDED) {
                statement = NOTHING;
            }
        }
    }

    private void updateLuggageHold(GameRound gameRound) {
        if (holdLuggageTimer.isTime()){
            holdLuggageTimer.stop();
            startBackMovementTime = LizzardGraphic.engine.millis();
            statement = LUGGAGE_BACK;
        }
    }

    private void startLuggageHold(){
        statement = LUGGAGE_HOLD;
        if (holdLuggageTimer == null) holdLuggageTimer = new Timer(timeToHoldLuggage);
        else holdLuggageTimer.setNewTimer(timeToHoldLuggage);
        updateCenter();
    }

    private void stopCycle(){
        statement = ENDED;
        actualCoordinate.x = startCoordinate.x;
        actualCoordinate.y = startCoordinate.y;
        updateCenter();
    }


    private void updateActualEndPos(GameRound gameRound) {
        float startProcessTime = startAttackTime;
        if (statement == LUGGAGE_BACK) startProcessTime = startBackMovementTime;
        float deltaTime = LizzardGraphic.engine.millis()-startProcessTime;
        float dist = deltaTime*luggageVel;
        boolean mustBeLuggageStopped = false;

        if (statement == LUGGAGE_FORWARD &&  isSomethingOnWay()) mustBeLuggageStopped = true;
        if ((dist>=attackDistance || mustBeLuggageStopped) ){
            //System.out.println("try to find player on the way");

            boolean playerAttacked = isPlayerAttacked(gameRound.getPlayer());
            if (playerAttacked) System.out.println("Player attacked!");
            //boolean playerAttacked = false;
            //System.out.println("Player attacked " + playerAttacked);
            if (statement == LUGGAGE_FORWARD && !playerAttacked) {
                startLuggageHold();
            }
            else {
                if (playerAttacked){
                    gameRound.getHittingController().attackPlayerByLuggage(gameRound, angle, lizard);
                    if (statement == LUGGAGE_FORWARD ) statement = LUGGAGE_BACK;
                }
                else stopCycle();

            }
        }
        else {
            if (statement == LUGGAGE_FORWARD){
                actualCoordinate.x = startCoordinate.x+dist*PApplet.cos(PApplet.radians(angle));
                actualCoordinate.y = startCoordinate.y+dist*PApplet.sin(PApplet.radians(angle));
            }
            else {
                dist = attackDistance - dist;
                actualCoordinate.x = startCoordinate.x+dist*PApplet.cos(PApplet.radians(angle));
                actualCoordinate.y = startCoordinate.y+dist*PApplet.sin(PApplet.radians(angle));
            }
            updateCenter();
        }
        luggageEnd.x = actualCoordinate.x;
        luggageEnd.y = actualCoordinate.y;
        luggageCenter.x = center.x;
        luggageCenter.y = center.y;
        luggageLength = dist;
    }

    /*
    private void updateActualEndPos(GameRound gameRound) {
        float startProcessTime = startAttackTime;
        if (statement == LUGGAGE_BACK) startProcessTime = startBackMovementTime;
        float deltaTime = LizzardGraphic.engine.millis()-startProcessTime;
        float dist = deltaTime*luggageVel;

        boolean mustBeLuggageStopped = isSomethingOnWay();

        if (dist>=attackDistance || mustBeLuggageStopped){
            //System.out.println("try to find player on the way");

            //boolean playerAttacked = isPlayerAttacked(gameRound.getPlayer());

            boolean playerAttacked = false;
            //System.out.println("Player attacked " + playerAttacked);
            if (statement == LUGGAGE_FORWARD || !playerAttacked) {
                startLuggageHold();
            }
            else {
                if (playerAttacked){
                    gameRound.getHittingController().attackPlayerByLuggage(gameRound, angle, lizard);
                }
                stopCycle();

            }
        }
        else {
            if (statement == LUGGAGE_FORWARD){
                actualCoordinate.x = startCoordinate.x+dist*PApplet.cos(PApplet.radians(angle));
                actualCoordinate.y = startCoordinate.y+dist*PApplet.sin(PApplet.radians(angle));
            }
            else {
                dist = attackDistance - dist;
                actualCoordinate.x = startCoordinate.x+dist*PApplet.cos(PApplet.radians(angle));
                actualCoordinate.y = startCoordinate.y+dist*PApplet.sin(PApplet.radians(angle));
            }
            updateCenter();
        }
        luggageEnd.x = actualCoordinate.x;
        luggageEnd.y = actualCoordinate.y;
        luggageCenter.x = center.x;
        luggageCenter.y = center.y;
    }
     */


    private boolean isSomethingOnWay() {
        mutTestPointForLuggage.x = luggageEnd.x;
        mutTestPointForLuggage.y = luggageEnd.y;
        //System.out.println("try to find something on the way");
        boolean isLuggageInSomething = PhysicGameWorld.arePointInAnyBodyButNotInBullet(mutTestPointForLuggage);
        if (isLuggageInSomething){
            Body contactBody = PhysicGameWorld.getBodyAtPoint(mutTestPointForLuggage);
            if (contactBody.equals(lizard.body)){
                return false;
            }
            else return true;
        }
        return false;
        //return PhysicGameWorld.arePointInAnyBodyButNotInBullet(mutTestPointForLuggage);
    }

    private boolean isPlayerAttacked(Person player) {
        mutTestPointForLuggageInWorldCoordinates.x = PhysicGameWorld.coordPixelsToWorld(luggageEnd.x, luggageEnd.y).x;
        mutTestPointForLuggageInWorldCoordinates.y =  PhysicGameWorld.coordPixelsToWorld(luggageEnd.x, luggageEnd.y).y;
        for (Fixture f = player.body.getFixtureList(); f != null; f=f.getNext()){
            if (f.testPoint(mutTestPointForLuggageInWorldCoordinates)){
                return true;
            }
        }
        return false;
    }

    private void updateCenter(){
        center.x = startCoordinate.x+(actualCoordinate.x-startCoordinate.x)/2;
        center.y = startCoordinate.y+(actualCoordinate.y-startCoordinate.y)/2;
    }

    void startAttack(float angle){
        if (statement == NOTHING && statement != STARTED && statement != LUGGAGE_FORWARD){
            statement = STARTED;
            this.angle = angle;
            //System.out.println("Lizard attack by angle: " + angle);
        }
        else {
            //System.out.println("Already started. Statement: " + statement);
        }
    }

    void forceStopAttack(){
        statement = ENDED;
    }

    void stopAttack(){
        if (statement != NOTHING && statement != ENDED && statement != LUGGAGE_BACK){
            statement = LUGGAGE_BACK;
        }
        else {
            System.out.println("Attack can not be stopped. Statement: " + statement);
        }
    }

    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public Coordinate getActualCoordinate() {
        return actualCoordinate;
    }

    public boolean isAttackStarted(){
        if (statement != NOTHING) return true;
        else return false;
    }

    public boolean canBeHeadAngleChanged(){
        if (statement == NOTHING || statement == LUGGAGE_FORWARD){
            return true;
        }
        else return false;
    }


    public float getActualDist() {

        return PApplet.sqrt(PApplet.sq(actualCoordinate.x-startCoordinate.x) + PApplet.sq(actualCoordinate.y-startCoordinate.y));
    }

    public Coordinate getCenter() {
        return center;
    }

    public boolean isInHoldZone(){
        if (statement == LUGGAGE_HOLD){
            return true;
        }
        else return false;
    }

    public int getStatement() {
        return statement;
    }

    public void setStatement(int luggageForward) {
        //statement = luggageForward;
    }

    public Vec2 getActualLuggageEnd() {

        return luggageEnd;
    }

    public Vec2 getActualLuggageCenter() {
        return luggageCenter;
    }

    public float getLuggageLength() {

        return luggageLength ;
    }
}
