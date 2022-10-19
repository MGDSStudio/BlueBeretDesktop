package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Snake;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SnakeController extends EnemyController{

    //statements
    //public final static int SLEEP = 0;
    public final static int SLEEP = 1;
    public final static int GO_TO_PLAYER = 2;
    public final static int ATTACK = 3;
    public final static int FALL_ASLEEP = 4;
    public final static int AWAKE = 5;

    private boolean startToDie, deadAfterDyingAnimation, sleeping;
    public final static int MOVEMENT_ON_GROUND = 0;
    public final static int DOWN_FALLING_ON_NET = 1;
    public final static int SITTING_AWAKENING_ATTACK = 2;
    public final static int SITTING_EXTERNAL_AWAKENING = 3;
    public final static int GO_AND_ATTACK_IN_FRONT = 4;
    //public final static int GO_AND_ATTACK_IN_FRONT = 4;
    //private boolean actualDirection;
    private final static int TIME_FOR_STOPPING_BY_WALL = 1000;
    private final int distanceToFindPlayerByRunOrJump = 190;
    private final int distanceToFindPlayerByShooting = 350;
    //private boolean newMovementDirection;
    private Rectangular attackRectZone;
    private final float probabilityToSleepAgain = 0.5f;
    private final int attackSpriteNumber = 3;
    private final int awakeSpriteNumber = 2;
    private int distanceToAttackZone;
    private final PVector mutStartTestPoint = new PVector(0,0);
    private final PVector mutEndTestLinePoint = new PVector(0,0);
    private boolean debugLineMustBeAdded;
    private boolean attackedOnThisLoop;

    public SnakeController(Enemy person, int behaviourType) {
        super(person, behaviourType);
        timeForStoppingByWall = TIME_FOR_STOPPING_BY_WALL;
        //newMovementDirection = person.getSightDirection();
        //actualDirection = person.getSightDirection();
        sleeping = true;
        if (behaviourType == GO_AND_ATTACK_IN_FRONT) {
            System.out.println("statement is go to player");
            statement = GO_TO_PLAYER;
        }
        else {
            System.out.println("Statement: " + behaviourType);
            statement = SLEEP;
        }
        stayingAfterFalling();
        initAttackZone();
    }

    private void initAttackZone(){
        distanceToAttackZone = (int) (enemy.getWidth()/1.5f);
        int zoneWidth = (int) (enemy.getWidth()/0.8f);
        if (zoneWidth<70) zoneWidth = 70;
        int zoneHeight = (int) (enemy.getHeight()/1.0f);
        attackRectZone = new Rectangular(enemy.getPixelPosition().x+distanceToAttackZone+zoneWidth/2, enemy.getPixelPosition().y, zoneWidth, zoneHeight);
        System.out.println("Zone width for snake: " + zoneWidth + " height" + zoneHeight);
    }

    /*
    public SnakeController(Enemy person, int behaviourType, float netPosX, float netPosY) {
        super(person, behaviourType);
        timeForStoppingByWall = TIME_FOR_STOPPING_BY_WALL;
        //newMovementDirection = person.getSightDirection();
        actualDirection = person.getSightDirection();
        stayingAfterFalling();
    }*/

    public void update(GameRound gameRound){
        if (startToDie && !deadAfterDyingAnimation){
            if (enemy.isActualAnimationEnds()){
                deadAfterDyingAnimation = true;
                enemy.kill();
                Snake snake = (Snake) enemy;
                snake.simplifyBody();
                gameRound.releaseGameObjects(enemy, PApplet.HALF_PI*3);
                if (Program.debug) System.out.println("Snake was simplyfied from the snake controller");
            }
        }
        else {
            if (statement == SLEEP){
                if (canBeSnakeAwaked(gameRound)){
                    statement = AWAKE;
                    SpriteAnimation spriteAnimation = enemy.getPersonAnimationController().getAnimationForType(PersonAnimationController.AWAKE);
                    spriteAnimation.setAnimationPlayingDirection(SpriteAnimation.FORWARD);
                }
            }
            else if (statement == AWAKE){
                if (isPersonAwaked()){
                    behaviourType = GO_AND_ATTACK_IN_FRONT;
                    if (Program.debug) System.out.println("Snake is awakening");
                    statement = GO_TO_PLAYER;
                    boolean orientation = getMovementDirectionToPlayer(gameRound.getPlayer());
                    if (orientation == Person.TO_RIGHT) movementDirection = RIGHT;
                    else movementDirection = LEFT;
                    boolean groundUnderFeet = true;
                    boolean clearingZonesUnderFeet = false;
                    if (true){
                        int dir = RIGHT;
                        if (movementDirection == RIGHT) dir = LEFT;
                        groundUnderFeet = areThereGroundUnderFeet(gameRound, dir);
                        clearingZonesUnderFeet = areThereClearingZoneInFront(gameRound);
                    }
                    updateMovementOnGround(groundUnderFeet, clearingZonesUnderFeet);
                }
            }
            if (statement == GO_TO_PLAYER){
                updateAttackRect();
                if (isPlayerInFrontOfSnake(gameRound)){
                    correctPersonOrientation(gameRound.getPlayer());
                    startAttack(gameRound.getPlayer());
                }
                else {
                    boolean groundUnderFeet = true;
                    boolean clearingZonesUnderFeet = false;
                    if (true){
                        int dir = RIGHT;
                        if (movementDirection == RIGHT) dir = LEFT;
                        groundUnderFeet = areThereGroundUnderFeet(gameRound, dir);
                        clearingZonesUnderFeet = areThereClearingZoneInFront(gameRound);
                    }
                    updateMovementOnGround(groundUnderFeet, clearingZonesUnderFeet);
                }
                updateFallAsleepAgain(gameRound);
            }
            if (statement == ATTACK){
                updateAttack(gameRound);
            }
            else if (statement == FALL_ASLEEP){
                updateFallAsleep();
            }
        }
    }

    private void correctPersonOrientation(Person soldier) {
    }


    private boolean canBeSnakeAwaked(GameRound gameRound) {
        if ((doesPlayerRun(gameRound) || doesPlayerJumpOrFall(gameRound))) {
            if ((isPlayerAround(gameRound, distanceToFindPlayerByRunOrJump))) {
                return true;
            }
        }
        else if ((gameRound.areThereActiveBullets() || gameRound.areThereActiveExplosions()) && isPlayerAround(gameRound, distanceToFindPlayerByShooting)){
            return true;
        }
        return  false;
    }

    private void updateFallAsleepAgain(GameRound gameRound) {
        if (gameRound.areThereActiveBullets() || gameRound.areThereActiveExplosions()) {
            //System.out.print("Bullets in world: " + gameRound.areThereActiveBullets() );
            //System.out.println(". Explosions in world: " +gameRound.areThereActiveExplosions() );
            if (Program.engine.random(100f) <= probabilityToSleepAgain) {
                sleepSnakeAgain();
            }
        }
    }

    private boolean isPersonAwaked() {
        SpriteAnimation spriteAnimation = enemy.getPersonAnimationController().getAnimationForType(PersonAnimationController.AWAKE);
        if (spriteAnimation.isAnimationAlreadyShown()){
            spriteAnimation.reset();
            return true;
        }
        else return false;
    }

    @Override
    public void setContactWithPlayer(Person player) {
        if (statement == SLEEP) statement = AWAKE;
        else if (statement == FALL_ASLEEP) statement = GO_TO_PLAYER;
        else if (statement == GO_TO_PLAYER || statement == ATTACK){
           boolean orientation = getMovementDirectionToPlayer(player);
           if (orientation != enemy.orientation) {
               enemy.orientation = orientation;
               Snake snake = (Snake) enemy;
               snake.mirrorBody();
           }

           //System.out.println("Contact and start to attack player " + getMovementDirectionToPlayer(player) + " orientation");
           if (orientation == Person.TO_LEFT) {
               enemy.getPersonAnimationController().setGraphicFlip(true);
           }
           else enemy.getPersonAnimationController().setGraphicFlip(false);
           if (statement == GO_TO_PLAYER) {
               startAttack(player);
               statement = ATTACK;
           }
        }

    }

    @Override
    public void attacked() {
        if (statement == SLEEP){
            statement = AWAKE;
        }
    }

    private void updateFallAsleep() {
        SpriteAnimation spriteAnimation = enemy.getPersonAnimationController().getAnimationForType(PersonAnimationController.AWAKE);
        if (spriteAnimation.getActualSpriteNumber() == 0 ) statement = SLEEP;
        //if (person.getPersonAnimationController().)
    }

    private void sleepSnakeAgain() {
        statement = FALL_ASLEEP;
        SpriteAnimation spriteAnimation = enemy.getPersonAnimationController().getAnimationForType(PersonAnimationController.AWAKE);
        spriteAnimation.setAnimationPlayingDirection(SpriteAnimation.BACKWARD);
        //spriteAnimation
    }

    private boolean doesPlayerRun(GameRound gameRound) {
        if (PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(gameRound.getPlayer().body.getLinearVelocity().x)) >= HumanAnimationController.criticalVelocityToStartRunning){
            return true;
        }
        else return false;
    }

    private boolean doesPlayerJumpOrFall(GameRound gameRound) {
        if (gameRound.getPlayer().getStatement() == Person.IN_AIR && PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(gameRound.getPlayer().body.getLinearVelocity().y)) >= HumanAnimationController.criticalVelocityToStartRunning){
            return true;
        }
        else return false;
    }

    private void updateAttack(GameRound gameRound) {
        SpriteAnimation spriteAnimation = enemy.getPersonAnimationController().getAnimationForType(PersonAnimationController.ATTACK);

        if (spriteAnimation.getActualSpriteNumber() == attackSpriteNumber && !attackedOnThisLoop){
            updatePersonOrientationRelativeToPlayer(gameRound.getPlayer());
            System.out.print("Actual attack sprite: " + spriteAnimation.getActualSpriteNumber() + "; Snake orientation: " + enemy.orientation);
            updateAttackRect();
            System.out.print(". Player is on: " + (int)(gameRound.getPlayer().getPixelPosition().x) + "x" );
            System.out.println("; Zone from: "+ attackRectZone.getLeftUpperX()+"x" + attackRectZone.getRightLowerX());
            if (isPlayerInFrontOfSnake(gameRound)){
                Body bodyOnWay = getBodyOnWay(gameRound);
                if (bodyOnWay == null || bodyOnWay.equals(gameRound.getPlayer().body)) {
                    gameRound.attackPlayer(enemy);
                }
                else if (bodyOnWay != null && !bodyOnWay.equals(enemy.body)){
                    System.out.println("Another object is on the way");
                    attackObject(bodyOnWay, gameRound);
                }
                if (debugLineMustBeAdded){
                    if (Program.debug) {
                        DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.LINE, mutStartTestPoint, mutEndTestLinePoint);
                        gameRound.addDebugGraphic(debugGraphic);
                        System.out.println("Debug graphic added at " + mutStartTestPoint + ", to " + mutEndTestLinePoint);
                    }
                    debugLineMustBeAdded = false;
                }
                attackedOnThisLoop = true;
            }
        }
        else if (spriteAnimation.isAnimationAlreadyShown()) {
            if (attackedOnThisLoop) attackedOnThisLoop = false;
            if (!isPlayerInFrontOfSnake(gameRound)) {
                statement = GO_TO_PLAYER;
                spriteAnimation.reset();
            }
        }
    }

    private void attackObject(Body bodyOnWay, GameRound gameRound) {

        if (bodyOnWay.isActive()){
            if (bodyOnWay.getType() == BodyType.DYNAMIC){
                GameObject object = PhysicGameWorld.getGameObjectByBody(gameRound, bodyOnWay);
                if (object != null){
                    //if (object instanceof Person){
                        object.attacked(enemy.getAttackValue());
                        object.addJumpAfterAttack(enemy);
                        System.out.println("Object " + object.getClass() + " attacked");
                    //}
                }
            }
        }
    }

    private Body getBodyOnWay(GameRound gameRound) {
        ArrayList <Body> allBodies = PhysicGameWorld.getBodies();
        for (Body body : allBodies){
            if (!body.equals(enemy.body) && !body.equals(gameRound.getPlayer().body)) {
                Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
                if (attackRectZone.isPointInRect(pos.x, pos.y)) {
                    mutStartTestPoint.x = pos.x;
                    mutStartTestPoint.y = pos.y;
                    mutEndTestLinePoint.x = enemy.getPixelPosition().x;
                    mutEndTestLinePoint.y = enemy.getPixelPosition().y;
                    debugLineMustBeAdded = true;
                    return body;
                }
            }
        }
        return null;
    }


    private Body getBodyOnWayOld(GameRound gameRound) {
        float startX = enemy.getPixelPosition().x;


        float startY = enemy.getPixelPosition().y- enemy.getHeight()/8;
        int steps = 20;
        float testWidth = attackRectZone.getWidth();
        float stepAlongX = PhysicGameWorld.controller.scalarPixelsToWorld(testWidth)/(float)steps;
        //ArrayList <Body> bodiesOnWay = PhysicGameWorld.getBodies();
        boolean orientation = getMovementDirectionToPlayer(gameRound.getPlayer());
        if (orientation == Person.TO_RIGHT){
            startX = PApplet.min(attackRectZone.getLeftUpperX(), attackRectZone.getRightLowerX());

        }
        else {
            startX = PApplet.max(attackRectZone.getLeftUpperX(), attackRectZone.getRightLowerX());
            //stepAlongX=-stepAlongX;
        }
        //float stepAlongX = PhysicGameWorld.controller.scalarPixelsToWorld(testWidth)/(float)steps;


        //if (!orientation)
        //final Vec2 testPoint = new Vec2(PhysicGameWorld.controller.coordPixelsToWorld(startX, startY));
        mutStartTestPoint.x =startX;
        mutStartTestPoint.y =startY;
        ArrayList <Body> bodiesOnWay = new ArrayList<>();
        for (int i = 1; i < steps; i++){
            Body body = PhysicGameWorld.getBodyAtPoint(mutStartTestPoint);
            if (body != null && !body.equals(enemy.body)){
                if (!gameRound.getPlayer().body.equals(body))
                    if (!bodiesOnWay.contains(body)){
                        bodiesOnWay.add(body);
                    }
            }
            mutStartTestPoint.x+=stepAlongX;
        }
        System.out.println("On the way: " + bodiesOnWay.size() + " objects");
        if (bodiesOnWay.size()>1){


            mutEndTestLinePoint.y = startY;
            mutEndTestLinePoint.x = startX;
            mutStartTestPoint.x=startX+stepAlongX*steps;
            debugLineMustBeAdded = true;
        }
        else if (bodiesOnWay.size() == 1){
            mutEndTestLinePoint.y = startY;
            mutEndTestLinePoint.x = startX;
            mutStartTestPoint.x=startX+stepAlongX*steps;
            debugLineMustBeAdded = true;
            return bodiesOnWay.get(0);
        }


        System.out.println("No body on the way");
        //return attackRectZone.isPointInRect(gameRound.getPlayer().getPixelPosition());
        mutEndTestLinePoint.y = startY;
        mutEndTestLinePoint.x = startX;
        mutStartTestPoint.x=startX+stepAlongX*steps;
        debugLineMustBeAdded = true;
        return null;
    }

    private void updatePersonOrientationRelativeToPlayer(Person player) {
        boolean orientation = !getMovementDirectionToPlayer(player);
        if (orientation != enemy.orientation) {
            enemy.orientation = orientation;
            Snake snake = (Snake) enemy;
            snake.mirrorBody();
        }
    }

    private void updateAttackRect(){
        if (enemy.orientation == Person.TO_LEFT) attackRectZone.setCenterX(enemy.getPixelPosition().x+attackRectZone.getWidth()/2+distanceToAttackZone);
        else attackRectZone.setCenterX(enemy.getPixelPosition().x-attackRectZone.getWidth()/2-distanceToAttackZone);
        attackRectZone.setCenterY(enemy.getPixelPosition().y);
    }

    private void startAttack(Person player) {
        System.out.print("Attack started " );
        if (getMovementDirectionToPlayer(player) == Person.TO_RIGHT) {
            enemy.orientation = Person.TO_RIGHT;
            enemy.move(false, Person.TO_RIGHT);
            System.out.println(" to right");
        }
        else {
            enemy.move(false, Person.TO_LEFT);
            enemy.orientation = Person.TO_LEFT;
            System.out.println(" to left");
        }
        if (enemy.orientation == Person.TO_LEFT) {
            System.out.println("Snake sees on left");
            enemy.getPersonAnimationController().setGraphicFlip(true);
        }
        else {
            System.out.println("Snake sees on false");
            enemy.getPersonAnimationController().setGraphicFlip(false);
        }


        statement = ATTACK;
    }

    private boolean isPlayerInFrontOfSnake(GameRound gameRound) {
        return attackRectZone.isPointInRect(gameRound.getPlayer().getPixelPosition());
    }



    private boolean getMovementDirectionToPlayer(Person player) {
        if (player.getPixelPosition().x> enemy.getPixelPosition().x){
            return Person.TO_RIGHT;
        }
        else return Person.TO_LEFT;
    }


    private boolean isPlayerAround(GameRound gameRound, int criticalDist){
        PVector playerPos = gameRound.getPlayer().getPixelPosition();
        if (PApplet.dist(playerPos.x, playerPos.y, enemy.getPixelPosition().x, enemy.getPixelPosition().y)< criticalDist){
            return true;
        }
        else return false;
    }


    @Override
    public void kill() {
        if (startToDie == false) {
            startToDie = true;
            lastOrientation = enemy.orientation;
            //enemy.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.DYING).setPlayOnce(true);
            System.out.println("Snake started to die");
        }
    }

    public boolean isStartedToDie(){
        return startToDie;
    }



    private void stayingAfterFalling(){
        stayingTimer = new Timer(TIME_FOR_STOPPING_BY_WALL);
        enemy.body.setLinearVelocity(new Vec2(0f, enemy.body.getLinearVelocity().y));
    }

    @Override
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

    private void updateMovementOnGround(boolean groundInFront, boolean fallInFront) {
        if (enemy.getStatement() != Person.IN_AIR){
            if (isBlockedAlongX() == true || (fallInFront && !groundInFront)) {
                changeMovementDirection();
                Snake snake = (Snake) enemy;
                snake.mirrorBody();
            }
            if (movementDirection == RIGHT) {
                enemy.move(true, Person.TO_LEFT);
            }
            else if (movementDirection == LEFT) {
                enemy.move(true, Person.TO_RIGHT);
            }

            /*
            if (movementDirection == RIGHT) person.getPersonAnimationController().setGraphicFlip(true);
            else person.getPersonAnimationController().setGraphicFlip(false);*/
        }
    }




/*
    public void changeMovementDirection() {
        if (movementDirection == LEFT) movementDirection = RIGHT;
        else if (movementDirection == RIGHT) movementDirection = LEFT;
    }*/
}
