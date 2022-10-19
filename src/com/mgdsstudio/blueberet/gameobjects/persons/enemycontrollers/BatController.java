package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class BatController extends EnemyController{
    public static final int HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE = 0;
    public static final int HANG = 1;
    public static final int FLY_IN_ZONE = 2;

    public static final int FLY_TO_ATTACK = 4;
    public static final int FLY_AWAY = 3;


    private int distanceToReactOnPlayer;
    private Flag patrolArea;
    private final int awakeningByAiming;
    public final static boolean CIRCLE_CORPSE = true;
    public final static boolean FALLING_RECT = false;
    private boolean deathType = FALLING_RECT;

    private final static int TIME_TO_REACT_ON_NOISE = 200;
    private Timer timerToReactOnNoise;
    private final PlayerObserver playerObserver;
    private BatFlyingController batFlyingController;
    private int updatingFrame;
    //private boolean alreadyAttacked
    private final PVector prevPos = new PVector();
    private int previousPlayerStatement = Person.IN_AIR;
    private final static float MOVEMENT_PRO_FRAME_TO_BE_AWAKED = 0.5f;

    public BatController(Enemy enemy, int behaviourType, int distanceToReactOnPlayer, int awakeningByAiming, Flag patrolArea, int updatingFrame){
        super(enemy, behaviourType);
        this.distanceToReactOnPlayer = distanceToReactOnPlayer;
        this.patrolArea = patrolArea;
        this.awakeningByAiming = awakeningByAiming;
        this.statement = behaviourType;
        this.updatingFrame = updatingFrame;
        playerObserver = new PlayerObserver(updatingFrame, distanceToReactOnPlayer, (int)(distanceToReactOnPlayer*2.5f), 0, enemy);
        createSpring();
        prevPos.x = enemy.getPixelPosition().x;
        prevPos.y = enemy.getPixelPosition().y;
    }



    @Override
    public void update(GameRound gameRound) {
        if (statement == HANG || statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
            if (enemy.isAlive()){

                if (isBatWasTranslatedPerLastFrame()){
                    if (statement == HANG) {
                        statement = FLY_IN_ZONE;
                    }
                    else if (statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
                        statement = FLY_TO_ATTACK;
                    }
                    enemy.simplifyBody();
                }
                else if (wasBatAwakedThoroughPlayerLandingOrRunning(gameRound.getPlayer())){
                    if (statement == HANG) {
                        statement = FLY_IN_ZONE;
                    }
                    else if (statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
                        statement = FLY_TO_ATTACK;
                    }
                    enemy.simplifyBody();
                }
                else if (timerToReactOnNoise != null){
                    if (timerToReactOnNoise.isTime()){
                        if (statement == HANG) {
                            statement = FLY_IN_ZONE;
                        }
                        else if (statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
                            statement = FLY_TO_ATTACK;
                        }
                        enemy.simplifyBody();
                    }
                }
                if (statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE && Program.engine.frameCount % updatingFrame == 0){
                    if (patrolArea.inZone(gameRound.getPlayer().getPixelPosition()) || gameRound.getPlayer().isPersonRunning()){
                        statement = FLY_TO_ATTACK;
                        enemy.simplifyBody();
                    }
                }
                if (playerObserver.isBoomOnDistance(gameRound) || playerObserver.isShotOnDistance(gameRound)){
                    timerToReactOnNoise = new Timer(TIME_TO_REACT_ON_NOISE);
                }
            }
        }
        else {
            if (enemy.isAlive()){
                if (!batFlyingController.isStarted()) {
                    if (statement == FLY_TO_ATTACK){
                        batFlyingController.startFlyingToPlayer(gameRound);
                        statement = FLY_IN_ZONE;
                    }
                    else if (statement == FLY_IN_ZONE || statement == FLY_AWAY) {
                        batFlyingController.startFlyingToRandomEnd(gameRound);
                    }
                    /*else if (statement == FLY_IN_ZONE || statement == FLY_AWAY) {
                        batFlyingController.startFlyingToRandomEnd(gameRound);
                    }*/
                }
                batFlyingController.update(gameRound);
            }
        }
    }

    private boolean wasBatAwakedThoroughPlayerLandingOrRunning(Person person) {
        //
        boolean awaked = false;
        if (person.getStatement() != previousPlayerStatement || (person.isPersonRunning() && (person.getStatement() == Person.ON_GROUND || person.getStatement() == Person.ON_MOVEABLE_PLATFORM))){
            float distance = getDistanceToPlayer(person);
            if (distance < distanceToReactOnPlayer){
                awaked = true;
            }
        }
        previousPlayerStatement = person.getStatement();
        return awaked;

    }



    private boolean isBatWasTranslatedPerLastFrame() {
        boolean awaked = false;
        if (enemy.getPixelPosition().x-prevPos.x >MOVEMENT_PRO_FRAME_TO_BE_AWAKED || enemy.getPixelPosition().y-prevPos.y >MOVEMENT_PRO_FRAME_TO_BE_AWAKED) {

            awaked = true;
        }
        prevPos.x = enemy.getPixelPosition().x;
        prevPos.y = enemy.getPixelPosition().y;
        return awaked;
    }

    @Override
    public void kill() {
        if (statement == HANG || statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
            deathType = FALLING_RECT;
        }
        else {
            deathType = CIRCLE_CORPSE;
        }
        enemy.body.setGravityScale(1f);
        float torque = -1f*enemy.getPersonWidth()*5f;
        if (enemy.body.getLinearVelocity().x < 0){
            torque*=(-1f);
        }
        enemy.body.setFixedRotation(false);
        enemy.body.resetMassData();
        enemy.body.applyTorque(torque);
        PhysicGameWorld.controller.world.destroyJoint(batFlyingController.spring);

    }

    public boolean isDeathType() {
        return deathType;
    }

    @Override
    public boolean isStartedToDie() {
        return false;
    }

    @Override
    public void attacked() {
        if (enemy.isAlive()){
            if (statement == HANG || statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
                statement = FLY_AWAY;
            }
        }
    }

    @Override
    public void draw(EnemiesAnimationController enemiesAnimationController, GameCamera gameCamera, Vec2 pos, float angleInRadians) {
        if (Program.debug){
            if (patrolArea != null){
                patrolArea.draw(gameCamera);
            }
        }
        if (!enemy.isAlive()) {
            if (deathType == CIRCLE_CORPSE) {
                enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.CORPSE_SINGLE_SPRITE, pos, angleInRadians, false);
            }
            else enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.HANG, pos, angleInRadians, false);
        }
        else {
            if (enemy.body.getLinearVelocity().x < 0) lastGraphicFlip = true;
            else lastGraphicFlip = false;
            if (statement == FLY_AWAY || statement == FLY_IN_ZONE || statement == FLY_TO_ATTACK) {
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.FLY, pos, angleInRadians, lastGraphicFlip);
            } else if (statement == HANG || statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE) {
                enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.HANG, pos, angleInRadians, false);

            }
        }
    }

    public boolean isSleeping() {
        if (statement == HANG || statement == HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
            return true;

        }
        else return false;
    }

    public void setDeathType(boolean circleCorpse) {
        deathType = circleCorpse;
    }


    public void createSpring() {
        batFlyingController = new BatFlyingController(patrolArea, (Enemy) enemy, updatingFrame);
        System.out.println("Spring was created for bat " + enemy );
    }
}
