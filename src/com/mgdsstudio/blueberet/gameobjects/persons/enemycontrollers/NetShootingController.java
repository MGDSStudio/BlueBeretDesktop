package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.persons.Spider;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

class NetShootingController {

    private interface Statements {
        int NOTHING = 0;
        int SHOOTING = 1;
        int NET_FLIES = 2;
        int PLAYER_IN_WITH_NET_ATTACKED = 3;
    }
    private final Spider spider;
    //private final static ImageZoneFullData = new ImageZoneFullData;
    private Timer nextNetShootingTimer;
    private final int timeToNextNetShot = 1500;
    private int statement = Statements.NOTHING;
    private final static int START_SPRITE_TO_ADD_NET = 5;

    private static AnimationDataToStore netAnimationFlyingData;
    private static AnimationDataToStore netAnimationBrakingData;
    private static AnimationDataToStore netAnimationBrakingDataFlipped;

    private MovableOnScreenAnimation netAnimation;
    private MovableOnScreenAnimation netCrushedAnimation;
    private final Vec2 mutNetStartPos = new Vec2();
    private final Vec2 mutCrushingNetAbsPos = new Vec2();
    private final Vec2 mutCrushingNetRelativePos = new Vec2();
    private final Vec2 mutNetVelocity = new Vec2();

    private final int TIME_TO_BLOCK_PLAYER = 1500;
    private Timer timerToBlockPlayer;
    private GameObject objectToBeCollided;
    private float collidedObjectAngle;
   // private
    //private SpiderController spiderController;

    NetShootingController(Spider spider, SpiderController spiderController) {
        this.spider = spider;
        //this.spiderController = spiderController;
    }

    void update(GameRound gameRound){
        if (statement == Statements.NOTHING){
            if (isPlayerOnAttackLine(gameRound)){
                if (canSpiderAttack(gameRound)) {
                    statement = Statements.SHOOTING;
                    if (Program.debug) System.out.println("Spider start to attack");
                }
            }
        }
        else if (statement == Statements.SHOOTING){
            if (spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.SHOT).getActualSpriteNumber() == START_SPRITE_TO_ADD_NET){
                addNet(gameRound);
            }
            else if (spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.SHOT).getActualSpriteNumber() == (spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.SHOT).getSpritesNumber()-1)){
                statement = Statements.NET_FLIES;
            }
            updateNetFlies(gameRound);
        }
        else if (statement == Statements.NET_FLIES){
            if (nextNetShootingTimer.isTime()){
                statement = Statements.NOTHING;
            }
            updateNetFlies(gameRound);
        }
        else if (statement == Statements.PLAYER_IN_WITH_NET_ATTACKED){
            runToPlayer(gameRound);
        }
        if (netCrushedAnimation != null){
            updateNetCrushedAnimationPos(gameRound);
            if (netCrushedAnimation.spriteAnimation.isActualSpriteLast()){
                netCrushedAnimation.spriteAnimation.pause();
                if (timerToBlockPlayer != null) {
                    if (timerToBlockPlayer.isTime()) {
                        netCrushedAnimation.spriteAnimation.hide();
                        netCrushedAnimation.setCanBeDeleted();
                    }
                }
            }
        }

    }

    private boolean canSpiderAttack(GameRound gameRound) {
        if (netCrushedAnimation == null) return true;
        else{
            if (netCrushedAnimation.canBeDeleted()){
                return true;
            }
            else return false;
        }
    }

    private void updateNetCrushedAnimationPos(GameRound gameRound) {
        if (objectToBeCollided != null) {
            if (objectToBeCollided.isAlive()) {
                if (objectToBeCollided.body != null) {
                    float angle = objectToBeCollided.body.getAngle();
                    float deltaAngle = collidedObjectAngle - angle;
                    mutCrushingNetAbsPos.x = objectToBeCollided.getPixelPosition().x + mutCrushingNetRelativePos.x;
                    mutCrushingNetAbsPos.y = objectToBeCollided.getPixelPosition().y + mutCrushingNetRelativePos.y;
                    PVector vector = new PVector();
                    float temp = mutCrushingNetAbsPos.x;
                    mutCrushingNetAbsPos.x = mutCrushingNetAbsPos.x * PApplet.cos(deltaAngle) - mutCrushingNetAbsPos.y * PApplet.sin(deltaAngle);
                    mutCrushingNetAbsPos.y = temp * PApplet.sin(deltaAngle) + mutCrushingNetAbsPos.y * PApplet.cos(deltaAngle);
                    netCrushedAnimation.setPosition(mutCrushingNetAbsPos);
                    if (netAnimation != null) {
                        if (netAnimation.isMustBeShownOnce()){  //Already collided

                        }
                    }
                }
            }
        }
        /*
        mutCrushingNetAbsPos.x=gameRound.getPlayer().getPixelPosition().x+mutCrushingNetRelativePos.x;
        mutCrushingNetAbsPos.y=gameRound.getPlayer().getPixelPosition().y+mutCrushingNetRelativePos.y;
        netCrushedAnimation.setPosition(mutCrushingNetAbsPos);
         */
    }

    private void runToPlayer(GameRound gameRound) {
        if (isPlayerFreeFromNet(gameRound.getPlayer())) statement = Statements.NOTHING;
        else{

        }
    }

    private boolean isPlayerFreeFromNet(Person player) {
        Soldier soldier = (Soldier) player;
        if (soldier.isPlayerFreeFromNet()) return true;
        else return false;
    }

    private void netColliding(GameRound gameRound, GameObject gameObject){
        //if (Program.debug) System.out.println("Net is in " + gameObject.getClass());
        if (netAnimation.spriteAnimation.getAnimationStatement() != SpriteAnimation.SWITCHED_OFF) {
            netAnimation.stopMovement();
            netAnimation.spriteAnimation.hide();


            //netAnimation.setShowOnce(true);
            //netAnimation.spriteAnimation.
            System.out.println("Hidden");
            //
            objectToBeCollided = gameObject;
            if (gameObject instanceof Soldier) {
                Soldier soldier = (Soldier) gameObject;
                soldier.setControlBlocked(TIME_TO_BLOCK_PLAYER);
                if (timerToBlockPlayer == null) timerToBlockPlayer = new Timer(TIME_TO_BLOCK_PLAYER);
                else timerToBlockPlayer.setNewTimer(TIME_TO_BLOCK_PLAYER);
                statement = Statements.PLAYER_IN_WITH_NET_ATTACKED;
            }
            createBrackingNet(gameRound);
        }
    }
    private void updateNetFlies(GameRound gameRound) {
        Soldier soldier = (Soldier) gameRound.getPlayer();
        if (!soldier.isControlBlocked()){
            if (netAnimation != null){
                if (GameMechanics.isIntersectionBetweenAllignedRects(gameRound.getPlayer().getPixelPosition().x, gameRound.getPlayer().getPixelPosition().y, netAnimation.getPosition().x, netAnimation.getPosition().y, gameRound.getPlayer().getPersonWidth(), gameRound.getPlayer().getHeight(), netAnimation.getWidth()/3f, netAnimation.getHeight()/3f)){
                //if (GameMechanics.isPointInRect(netAnimation.getPosition(), gameRound.getPlayer().getPixelPosition(), gameRound.getPlayer().getPersonWidth(), gameRound.getPlayer().getHeight())){
                    netColliding(gameRound, soldier);
                }
                else {
                    ArrayList <Body> bodies = PhysicGameWorld.getBodies();
                    Vec2 mutNetWorldPos;
                    mutNetWorldPos =PhysicGameWorld.coordPixelsToWorld(netAnimation.getPosition().x, netAnimation.getPosition().y);
                    for (Body body : bodies){
                        if (PhysicGameWorld.isPointInBody(mutNetWorldPos, body)){
                            try {
                                if (!body.equals(spider.body)) {
                                    if (body.getUserData() != null && body.getUserData().equals(Bullet.BULLET)){

                                    }
                                    else {
                                        GameObject gameObject = PhysicGameWorld.getGameObjectByBody(gameRound, body);
                                        netColliding(gameRound, gameObject);
                                    }
                                }
                                break;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }
                }
                /*if (gameRound.getPlayer().body.getFixtureList().testPoint(PhysicGameWorld.controller.coordPixelsToWorld(netAnimation.getPosition()))){

                    if (Program.debug) System.out.println("Net is in player");
                    netAnimation.stopMovement();*/
                }
                else {
                    // System.out.println("Net is on: " + PhysicGameWorld.coordPixelsToWorld(netAnimation.getPosition().x, netAnimation.getPosition().y) + " but player at:" + gameRound.getPlayer().body.getPosition());
                    try {
                        System.out.println("Net is on: " + netAnimation.getPosition() + " but player at:" + gameRound.getPlayer().getPixelPosition());
                    }
                    catch (Exception e){

                    }
                }
            }
        else {
            updateRunToPlayer(gameRound);
        }



    }

    private void createBrackingNet(GameRound gameRound) {
            boolean flip = false;
            if (objectToBeCollided != null){
                if (objectToBeCollided.getPixelPosition().x<netAnimation.getPosition().x) flip = true;
                collidedObjectAngle = objectToBeCollided.body.getAngle();
            }
            SpriteAnimation spriteAnimation;
            if (flip) spriteAnimation = new SpriteAnimation(netAnimationBrakingDataFlipped);
            else  spriteAnimation = new SpriteAnimation(netAnimationBrakingData);
            Vec2 pos = new Vec2(netAnimation.getPosition().x, netAnimation.getPosition().y);
            mutCrushingNetRelativePos.x = netAnimation.getPosition().x-objectToBeCollided.getPixelPosition().x;
            mutCrushingNetRelativePos.y = netAnimation.getPosition().y-objectToBeCollided.getPixelPosition().y;
            mutCrushingNetAbsPos.x = pos.x;
            mutCrushingNetAbsPos.y = pos.y;
            //gameRound.getPlayer()
            float angle = 0;
            if (isPlayerLeft(gameRound.getPlayer())) angle = 180;
            netCrushedAnimation = new MovableOnScreenAnimation(spriteAnimation, pos, angle, 0, 0, 0f, 0, 0f, TIME_TO_BLOCK_PLAYER);
            netCrushedAnimation.setLayer(ILayerable.IN_FRONT_OF_ALL);
            //netCrushedAnimation.setShowOnce(true);

            gameRound.addNewIndependentOnScreenAnimation(netCrushedAnimation);
    }

    private void updateRunToPlayer(GameRound gameRound) {
        if (isPlayerLeft(gameRound.getPlayer())){
            if (timerToBlockPlayer != null) {
                if (timerToBlockPlayer.isTime()){
                    Soldier soldier = (Soldier) gameRound.getPlayer();
                    soldier.setControlBlocked(false);
                }
            }
        }
    }

    private void addNet(GameRound gameRound) {
        if (nextNetShootingTimer == null) {
            nextNetShootingTimer = new Timer(timeToNextNetShot);
            createFlyingNet(gameRound);
        }
        else {
            if (nextNetShootingTimer.isTime()){
                nextNetShootingTimer.setNewTimer(timeToNextNetShot);
                createFlyingNet(gameRound);
            }
        }
    }

    private void createFlyingNet(GameRound gameRound) {
        System.out.println("Try to create net");
        if (netAnimationBrakingData == null) {
            int leftX = 186;
            int upperY = 0;
            int rightX = 383;
            int lowerY = 32;
            netAnimationBrakingData = new AnimationDataToStore(spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.GO).getPath(), leftX, upperY, rightX, lowerY, 28*3, 32*3, (byte)1, (byte)7, 16);

            upperY = 62;
            lowerY = 32+62;
            netAnimationBrakingDataFlipped = new AnimationDataToStore(spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.GO).getPath(), leftX, upperY, rightX, lowerY, 28*3, 32*3, (byte)1, (byte)7, 16);

            rightX = leftX+28*4;
            upperY = 32;
            lowerY = 32+32;
            //int leftX, int upperY, int rightX, int lowerY, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency
            netAnimationFlyingData = new AnimationDataToStore(spider.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.GO).getPath(), leftX, upperY, rightX, lowerY, 28*3, 32*3, (byte)1, (byte)4, 16);
        }
        System.out.println("Net created");
        SpriteAnimation spriteAnimation = new SpriteAnimation(netAnimationFlyingData);
        //public MovableOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, float xStartVelocity,float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity, int timeToClose) {

        netAnimation = new MovableOnScreenAnimation(spriteAnimation, getHeadPos(), 180+(180-getShootingAngle(gameRound)), getNetShootingVelocity(gameRound).x, getNetShootingVelocity(gameRound).y, 0f, 10f, 0f, 2500);
        //netAnimation.spriteAnimation.setFlipX(true);
        netAnimation.setLayer(ILayerable.IN_FRONT_OF_ALL);
        gameRound.addNewIndependentOnScreenAnimation(netAnimation);
    }

    private Vec2 getNetShootingVelocity(GameRound gameRound) {
        final float NORMAL_NET_VEL = 200f;
        float angle = getShootingAngle(gameRound);
        //float mult = 1f;
        //if (isPlayerLeft(gameRound.getPlayer())) mult = -1f;
        mutNetVelocity.x = NORMAL_NET_VEL* PApplet.cos(PApplet.radians(angle));
        mutNetVelocity.y = NORMAL_NET_VEL* PApplet.sin(PApplet.radians(angle));
        //mutNetVelocity.y = -22;
        return mutNetVelocity;
    }

    private boolean isPlayerLeft(Person player) {
        if (spider.getPixelPosition().x<player.getPixelPosition().x){
            return false;
        }
        else return true;
    }

    private float getShootingAngle(GameRound gameRound) {
        float angle = GameMechanics.getAngleToObject(spider.getPixelPosition(), gameRound.getPlayer().getPixelPosition());
        System.out.println("Shooting angle: " + angle);
        return angle;
    }

    private Vec2 getHeadPos() {
        mutNetStartPos.x = spider.getPixelPosition().x+spider.getPersonWidth()*0.75f;
        mutNetStartPos.y = spider.getPixelPosition().y-spider.getHeight()*0.15f;
        return mutNetStartPos;
    }

    private boolean isPlayerOnAttackLine(GameRound gameRound) {
        if (isOnAttackDistance(gameRound.getPlayer())) {
            if (spider.getPixelPosition().x < gameRound.getPlayer().getPixelPosition().x) {
                if (spider.orientation == Person.TO_RIGHT) {
                    if (spider.body.getLinearVelocity().x > 0.65f) {
                        System.out.println("Vel: " + spider.body.getLinearVelocity().x);
                        return true;
                    }
                    else return false;
                }

            } else if (spider.getPixelPosition().x > gameRound.getPlayer().getPixelPosition().x) {
                if (spider.orientation == Person.TO_LEFT) {
                    if (spider.body.getLinearVelocity().x < -0.65f) {
                        System.out.println("Vel: " + spider.body.getLinearVelocity().x);
                        return true;
                    }
                    else return false;
                }
            }
            return false;
        }
        else return false;
    }

    private boolean isOnAttackDistance(Person player) {
        if ((player.getPixelPosition().x-spider.getPixelPosition().x)<400) return true;
        else return false;
    }

    /*
    private boolean isPlayerOnAttackLine(GameRound gameRound) {
        if (spider.getPixelPosition().x<gameRound.getPlayer().getPixelPosition().x){
            if (spider.orientation == Person.TO_RIGHT){
                if (spiderController.getGraphicFlipX() == true){
                    return true;
                }
                else return false;
            }

        }
        else if (spider.getPixelPosition().x>gameRound.getPlayer().getPixelPosition().x){
            if (spider.orientation == Person.TO_LEFT){
                 if (spiderController.getGraphicFlipX() == false){
                    return true;
                }
                else return false;
            }

        }
        return false;
    }
     */


/*
    public void updateAttack(GameRound gameRound) {
        if (statement != Statements.SHOOTING) statement = Statements.SHOOTING;

    }*/
/*
    public boolean mustSpiderAttackPlayer(GameRound gameRound) {
        return true;
    }*/

    public boolean isAttackingNow() {
        if (statement == Statements.SHOOTING) return true;
        else return false;
    }

    public void kill() {
        if (netAnimation != null) netAnimation.setShowOnce(true);
        if (netCrushedAnimation != null) netCrushedAnimation.setShowOnce(true);
    }
}
