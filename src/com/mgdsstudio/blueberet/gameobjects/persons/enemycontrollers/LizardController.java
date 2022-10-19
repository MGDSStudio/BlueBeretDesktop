package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Lizard;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class LizardController extends EnemyController {

    private Timer timerToBeforeAttackVabrations;

    private final static int TIME_TO_PREVENT_REPEATING_DIRECTION_CHANGING = 600;
    private final int attackTime, holdLuggageTime, delayBeforeAttackTime, attackDistance, attackProbability;
    private final boolean withFallingOnLoweredPlatform;

    //Statements
    public final static int ST_GO = -2;
    private final static int ST_READY_TO_ATTACK = -1;
    public final static int ST_AIMING = 0;
    public final static int ST_STARTED = LizardAttackController.STARTED;
    public final static int ST_LUGGAGE_FORWARD = LizardAttackController.LUGGAGE_FORWARD;
    public final static int ST_LUGGAGE_HOLD = LizardAttackController.LUGGAGE_HOLD;
    public final static int ST_LUGGAGE_BACK = LizardAttackController.LUGGAGE_BACK;
    public final static int ST_ENDED = LizardAttackController.ENDED;
    public final static int ST_KILLED = 15;
    //private int statement = ST_GO;

    protected LizzardGraphic lizzardGraphic;
    protected LizardAttackController lizardAttackController;
    protected LizardHeadController lizardHeadController;

    //private final ArrayList <GameObject> objectsOnAttackLine;
    //private final ArrayList <GameObject> objectsOnAttackLine;

    private final static int TIME_FOR_STOPPING_BY_WALL = 1000;

    private final PVector mutTestPoint = new PVector();
    private final Vec2 mutHeadCenter = new Vec2();
    private float headAngle = 0f;

    private boolean fixedFlip;
    private boolean aimingStartedOnThisFrame;
    private final Vec2 mutLuggagePos = new Vec2();
    private PlayerObserver playerObserver;
    private final PVector mutLuggagePoint = new PVector(0,0);

    private boolean playerWasCaughtByLuggage = false;
    private boolean hasAttackAbility = true;
    private boolean lastAttack = false;
    private int luggageHeight;

    public LizardController(Enemy person, int attackTime, int holdLuggageTime, int delayBeforeAttackTime, int attackDistance, boolean withFallingOnLoweredPlatform, int attackProbability, float graphicScale) {
        super(person, 0);
        this.attackTime = attackTime;
        this.holdLuggageTime = holdLuggageTime;
        this.delayBeforeAttackTime = delayBeforeAttackTime;
        this.attackDistance = attackDistance;
        this.withFallingOnLoweredPlatform = withFallingOnLoweredPlatform;
        this.attackProbability = attackProbability;

        lizzardGraphic = new LizzardGraphic(Program.engine, graphicScale);
        lizardAttackController = new LizardAttackController((Lizard) person, attackTime, holdLuggageTime, lizzardGraphic.headCenter, attackDistance);
        lizardHeadController = new LizardHeadController(lizardAttackController, attackTime * 4);

        statement = ST_GO;
        stayingTimer = new Timer(TIME_FOR_STOPPING_BY_WALL);
        playerObserver = new PlayerObserver(3, person.getPersonWidth()*4, person.getPersonWidth()*8, person.getPersonWidth()*4, person);
        //public PlayerObserver(int updatingFrame, int distToReactOnRunningPlayer, int distToReactOnBoom, int distToFindPlayerAhead, Soldier player, Enemy enemy) {
        //
    }


    @Override
    public void update(GameRound gameRound) {
        //System.out.println("On visible; " + isPlayerOnVisibleSide(gameRound.getPlayer()) + " orientation : " + enemy.orientation);

        if (statement == ST_GO) {
            if (playerWasCaughtByLuggage) makePlayerFree(gameRound.getPlayer());
            updateMovement(gameRound);
            if (lastAttack) hasAttackAbility = false;
        } else {
            if (hasAttackAbility) {
                if (statement == ST_AIMING) {
                    if (playerWasCaughtByLuggage) makePlayerFree(gameRound.getPlayer());
                    updateAiming(gameRound);
                    lizardAttackController.update(headAngle, enemy.getPixelPosition(), gameRound);
                    lizardHeadController.update();
                    //braking();
                } else if (statement == ST_STARTED) {
                    if (playerWasCaughtByLuggage) makePlayerFree(gameRound.getPlayer());
                    updateAiming(gameRound);
                    lizardAttackController.update(headAngle, enemy.getPixelPosition(), gameRound);
                    lizardHeadController.update();
                    if (timerToBeforeAttackVabrations.isTime()) {
                        startAttack(gameRound);
                    }
                } else if (statement == ST_KILLED) {

                } else updateAttack(gameRound);
            }
            else {

            }
        }


        //System.out.println("Statement: " + statement);
    }

    private void updateAttack(GameRound gameRound) {
        if (statement >= ST_LUGGAGE_FORWARD && statement <= ST_LUGGAGE_BACK) {
            Bullet bullet = contactWithBullet(gameRound.getBullets());
            lizardHeadController.update();
            lizardAttackController.update(headAngle, enemy.getPixelPosition(), gameRound);
            statement = lizardAttackController.getStatement();
            if (bullet != null){
                statement = ST_LUGGAGE_BACK;
                lizardAttackController.forceStopAttack();
                enemy.attacked(1);
                lastAttack = true;
                bullet.setActive(false);
            }
            else if (!gameRound.getPlayer().isUnderAttack()){
                if (isPlayerOnLuggage(gameRound.getPlayer())){
                    if (!playerWasCaughtByLuggage) {
                        playerWasCaughtByLuggage = true;
                    }
                    fixPlayerForOneFrame(gameRound.getPlayer());

                }
                else {
                    if (playerWasCaughtByLuggage)  makePlayerFree(gameRound.getPlayer());
                }
            }
        } else {
            statement = ST_GO;
            lizardAttackController.forceStopAttack();
            if (playerWasCaughtByLuggage)  makePlayerFree(gameRound.getPlayer());
        }

    }

    private void makePlayerGraphicFixed(Person player, boolean flag) {
        if (flag){
            //player.getPersonAnimationController().getTileset().setTintForNextFrame(255,0,255,85);
        }

    }

    private Bullet contactWithBullet(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets){
            if (bullet.isActive()) {
                Vec2 luggageCenter = lizardAttackController.getActualLuggageCenter();
                int luggageLength = (int)lizardAttackController.getLuggageLength();

                if (GameMechanics.isPointInRect(bullet.getPixelPosition().x, bullet.getPixelPosition().y, luggageCenter.x, luggageCenter.y, luggageLength, luggageHeight, headAngle)) {
                    System.out.println("Bullet attacks the luggage");
                    return bullet;
                }
            }
        }
        return null;
    }

    private void makePlayerFree(Person player){
        //Soldier soldier = (Soldier) player;
        //soldier.blockPlayerMovement(false);
        //System.out.println("Player deblocked");
    }

    private boolean isPlayerOnLuggage(Person player) {
        int step = 2;
        //int actualDist = PApplet.dist(mutHeadCenter.x, mutHeadCenter.y, )
        //int dist = lizardAttackController.getActualDist();
        for (int i = step; i <= lizardAttackController.getActualDist(); i+=step){

            mutLuggagePoint.x = mutHeadCenter.x+i*PApplet.cos(PApplet.radians(headAngle));
            mutLuggagePoint.y = mutHeadCenter.y+i*PApplet.sin(PApplet.radians(headAngle));
            //System.out.println("Test point: " + mutLuggagePoint);
            for (Fixture f = player.body.getFixtureList(); f != null; f=f.getNext()) {
                if (f.testPoint(PhysicGameWorld.coordPixelsToWorld(mutLuggagePoint))) {
                    //System.out.println("Player at: " + player.getPixelPosition().x + "x" + player.getPixelPosition().y + "; Luggage: " + mutLuggagePoint);
                    return true;
                }
            }
        }
        return false;
    }

    private void fixPlayerForOneFrame(Person player) {
        Soldier soldier = (Soldier) player;
        soldier.blockHumanMovementForOneFrame(true);
        System.out.println("Player blocked on luggage for one frame");
        makePlayerGraphicFixed(player, true);
    }

    private void startAttack(GameRound gameRound) {
        if (statement != ST_LUGGAGE_FORWARD) {
            lizardAttackController.startAttack(headAngle);
            statement = ST_LUGGAGE_FORWARD;
            System.out.println("Attack started");
        }
        lizardAttackController.update(headAngle, enemy.getPixelPosition(), gameRound);
    }


    private void updateAiming(GameRound gameRound) {
        if (isPlayerOnVisibleSideAndCanBeAttacked(gameRound.getPlayer())) {
            if (isPlayerOnRightDistance(gameRound.getPlayer())) {
                if (isAttackWayFreeFromRoundElements(gameRound)) {
                    if (statement != ST_STARTED) {
                        if (timerToBeforeAttackVabrations == null)
                            timerToBeforeAttackVabrations = new Timer(delayBeforeAttackTime);
                        else timerToBeforeAttackVabrations.setNewTimer(delayBeforeAttackTime);
                    }
                    statement = ST_STARTED;
                } else {
                    if (timerToBeforeAttackVabrations != null) timerToBeforeAttackVabrations.stop();
                    System.out.println("Something is on the way between the player and the lizard");
                }
            } else {
                if (timerToBeforeAttackVabrations != null) timerToBeforeAttackVabrations.stop();
                statement = ST_GO;
            }
        } else {
            if (timerToBeforeAttackVabrations != null) timerToBeforeAttackVabrations.stop();
            statement = ST_GO;
        }
    }

    /*
    private void updateHeadAngle(Person player) {
        PVector playerPos = player.getPixelPosition();
        PVector lizardPos = enemy.getPixelPosition();
        headAngle = GameMechanics.angleDetermining(playerPos.x, playerPos.y, lizardPos.x, lizardPos.y);
    }*/

    private void updateMovement(GameRound gameRound) {
        boolean groundUnderFeet = areThereGroundUnderFeet(gameRound, movementDirection);
        if (groundUnderFeet) {
            updateMovementOnGround(gameRound);
        } else {
            boolean clearingZonesUnderFeet = areThereClearingZoneInFront(gameRound);
            if (!clearingZonesUnderFeet) {
                updateMovementOnGround(gameRound);
            } else {
                braking();
            }
        }
        if (hasAttackAbility) updateReactions(gameRound);

    }

    private void updateReactions(GameRound gameRound) {
        updatePlayerFounding(gameRound);
        updateReactionsOnNoise(gameRound);
    }

    private void updateReactionsOnNoise(GameRound gameRound) {
        boolean canHearExplosion = playerObserver.isBoomOnDistanceFromTheBack(gameRound);
        if (!canHearExplosion) canHearExplosion = playerObserver.isShotAhead(gameRound);
        if (canHearExplosion){
            changeMovementDirection();
            //System.out.println("Direction was changed to explosion or shot");
            statement = ST_AIMING;
        }
        else {

        }
    }

    private void updatePlayerFounding(GameRound gameRound) {
        boolean playerRunsNear = false;
        boolean playerAhead = playerObserver.isPlayerAhead(gameRound.getPlayer());
        if (!playerAhead) {
            playerRunsNear = playerObserver.isPlayerRunningFromTheBackOnDistance(gameRound.getPlayer());
            if (playerRunsNear) System.out.println("Player runs");
        }
        if (playerAhead || playerRunsNear) {
            changeMovementDirection();
            //System.out.println("Direction was changed");
            statement = ST_AIMING;
        }
    }


    private void updateMovementOnGround(GameRound gameRound) {
        if (enemy.getStatement() != Person.IN_AIR) {
            if (movementDirection == LEFT && !lastGraphicFlip) lastGraphicFlip = true;
            else if (movementDirection == RIGHT && lastGraphicFlip) lastGraphicFlip = false;
            if (movementDirection == LEFT) {
                enemy.move(true, Person.TO_LEFT);
                updateTargetFinding(gameRound);
            } else {
                enemy.move(true, Person.TO_RIGHT);
                updateTargetFinding(gameRound);
            }
            if (statement == ST_GO) {
                if (isBlockedAlongX() == true && enemy.isAlive()) {
                    changeMovementDirection();
                }
            }
        }
    }

    private void updateTargetFinding(GameRound gameRound) {
        if (hasAttackAbility) {
            boolean attackOnlyPlayer = true;
            if (attackOnlyPlayer) {
                if (gameRound.getPlayer().isAlive() && !gameRound.getPlayer().isUnderAttack()) {
                    if (isPlayerOnVisibleSideAndCanBeAttacked(gameRound.getPlayer())) {
                        if (isPlayerOnRightDistance(gameRound.getPlayer())) {
                            statement = ST_AIMING;
                        }
                    }
                }
            }
        }
    }

    private boolean isPlayerOnVisibleSideAndCanBeAttacked(Person player) {
        if (player.isAlive()) {
            PVector playerPos = player.getPixelPosition();
            if (enemy.orientation == Person.TO_RIGHT) {
                if (playerPos.x > enemy.getPixelPosition().x) {
                    if (isPlayerInAnglesFork(playerPos)) {
                        return true;
                    }
                    else return false;
                } else return false;
            } else {
                if (playerPos.x < enemy.getPixelPosition().x) {
                    if (isPlayerInAnglesFork(playerPos)) {
                        return true;
                    }
                    else return false;
                } else return false;
            }
        } else return false;
    }

    private boolean isPlayerInAnglesFork(PVector playerPos) {
        float angleToPlayer = getAngleToObject(playerPos);
        //System.out.println("Angle to player: " + angleToPlayer);
        if (angleToPlayer>IAttackAngles.MAX_UPPER_ANGLE_FOR_RIGHT_SIDE && angleToPlayer <= 360){
            return true;
        }
        else if (angleToPlayer<IAttackAngles.MIN_LOWER_ANGLE_FOR_RIGHT_SIDE && angleToPlayer >= 0){
            return true;
        }
        else if (angleToPlayer >= IAttackAngles.MIN_LOWER_ANGLE_FOR_LEFT_SIDE && angleToPlayer <= IAttackAngles.MAX_UPPER_ANGLE_FOR_LEFT_SIDE){
            return true;
        }
        else return false;
    }

    private float getAngleToObject(PVector pos) {
        //return GameMechanics.angleDetermining(enemy.getPixelPosition().x, enemy.getPixelPosition().y, pos.x, pos.y);
        return GameMechanics.angleDetermining(pos.x, pos.y, enemy.getPixelPosition().x, enemy.getPixelPosition().y);
    }

    private boolean isAttackWayFreeFromRoundElements(GameRound gameRound) {
        PVector playerPos = gameRound.getPlayer().getPixelPosition();
        PVector lizardPos = enemy.getPixelPosition();
        final float dist = PApplet.dist(playerPos.x, playerPos.y, lizardPos.x, lizardPos.y);
        float STEP = 2f;
        headAngle = GameMechanics.angleDetermining(playerPos.x, playerPos.y, lizardPos.x, lizardPos.y);
        float angleToPlayerInRad = PApplet.radians(headAngle);

        //System.out.println("Angle is in degrees " + GameMechanics.angleDetermining(playerPos.x, playerPos.y, lizardPos.x, lizardPos.y) + " in rad: " + angleToPlayerInRad);

        int iterations = PApplet.floor(dist / STEP);

        boolean founded = false;
        for (int i = 0; i < iterations; i++) {
            mutTestPoint.x = lizardPos.x + STEP * i * PApplet.cos(angleToPlayerInRad);
            mutTestPoint.y = lizardPos.y + STEP * i * PApplet.sin(angleToPlayerInRad);
            boolean in = PhysicGameWorld.arePointInAnyBody(mutTestPoint);
            if (in) {
                Body bodyIn = PhysicGameWorld.getBodyAtPoint(mutTestPoint);
                if (!bodyIn.equals(gameRound.getPlayer().body) && !bodyIn.equals(enemy.body)) {
                    founded = true;
                    break;
                }
            }
        }
        return !founded;

    }

    private boolean isPlayerOnRightDistance(Person player) {
        float dist = PApplet.dist(player.getPixelPosition().x, player.getPixelPosition().y, enemy.getPixelPosition().x, enemy.getPixelPosition().y);
        if (dist <= attackDistance) {

            return true;
        } else return false;
    }

    private void braking() {
        if (movementDirection == LEFT) {
            enemy.body.applyForceToCenter(new Vec2(enemy.body.getMass() * 32.2f, 0));
        } else enemy.body.applyForceToCenter(new Vec2(-enemy.body.getMass() * 32.2f, 0));
    }

    @Override
    public void kill() {
        statement = ST_KILLED;
        fixedFlip = true;
    }

    @Override
    public boolean isStartedToDie() {
        return false;
    }

    @Override
    public void attacked() {
        if (statement != ST_KILLED){
            if (statement == ST_STARTED || statement == ST_STARTED){
                statement = ST_GO;
            }
            else if (statement == ST_LUGGAGE_HOLD || statement == ST_LUGGAGE_FORWARD){
                statement = ST_LUGGAGE_BACK;
            }
            /*if (statement == ST_GO){
                statement = ST_GO;
            }*/
        }
    }


    @Override
    public void draw(EnemiesAnimationController enemiesAnimationController, GameCamera gameCamera, Vec2 pos, float angleInRadians) {
        if (!enemy.isAlive()) {
            enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.CORPSE_SINGLE_SPRITE, pos, angleInRadians, lastGraphicFlip);
        } else {
            if (enemy.body.getLinearVelocity().x < 0) lastGraphicFlip = true;
            else lastGraphicFlip = false;
            if (statement == LizardController.ST_GO) {
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, angleInRadians, lastGraphicFlip);
            } else if (statement == LizardController.ST_AIMING || statement == ST_STARTED || statement == ST_LUGGAGE_FORWARD || statement == ST_LUGGAGE_HOLD || statement == ST_LUGGAGE_BACK) {
                float randomX = 0f;
                float randomY = 0f;
                if (statement == ST_STARTED || statement == ST_LUGGAGE_HOLD) {
                    randomX = Program.engine.random(-enemy.getPersonWidth() / 45f, enemy.getPersonWidth() / 45f);
                    randomY = Program.engine.random(-enemy.getPersonWidth() / 45f, enemy.getPersonWidth() / 45f);
                }
                boolean flipForAimingBody = getFlipForAimingBody();
                boolean flipForAimingHead = false;
                if (flipForAimingBody) flipForAimingHead = true;
                if (flipForAimingBody) mutHeadCenter.x = pos.x - lizzardGraphic.headAnchor.x;
                else mutHeadCenter.x = pos.x + lizzardGraphic.headAnchor.x + randomX;
                mutHeadCenter.y = pos.y + lizzardGraphic.headAnchor.y + randomY;
                if (statement == ST_LUGGAGE_FORWARD || statement == ST_LUGGAGE_HOLD || statement == ST_LUGGAGE_BACK) {
                    drawLuggage(enemiesAnimationController, gameCamera, headAngle, flipForAimingBody);
                }
                enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.BODY_BY_AIM, pos, angleInRadians, flipForAimingBody);
                enemiesAnimationController.drawSprite(gameCamera, (byte) lizardHeadController.getActualHeadType(statement), mutHeadCenter, PApplet.radians(-headAngle), false, flipForAimingHead);
            }
        }
    }


    private void drawLuggage(EnemiesAnimationController enemiesAnimationController, GameCamera gameCamera, float angleInDegrees, boolean flip) {
        if (lizardAttackController.isAttackStarted()) {
            Vec2 end = lizardAttackController.getActualLuggageEnd();
            Vec2 center = lizardAttackController.getActualLuggageCenter();
            int width = (int) lizardAttackController.getActualDist();
            enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.LUGGAGE_END, end, PApplet.radians(-angleInDegrees), false, flip);
            StaticSprite luggageSprite = enemiesAnimationController.getSprite(EnemiesAnimationController.LUGGAGE);
            luggageSprite.setWidth(width);
            enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.LUGGAGE, center, PApplet.radians(-angleInDegrees), false, flip);

        }
    }

    private boolean getFlipForAimingBody() {
        if (movementDirection == LEFT) {
            return true;
        } else return false;
    }

    public Vec2 getTailPos(Vec2 tailAnchor) {
        Vec2 pos = new Vec2(enemy.getPixelPosition().x, enemy.getPixelPosition().y);
        if (enemy.orientation == Person.TO_LEFT) pos.x += enemy.getPersonWidth() / 2f;
        else pos.x -= enemy.getPersonWidth() / 2f;
        pos.y += tailAnchor.y;
        return pos;
    }

    public float getDirectionCoef() {
        if (enemy.orientation == Person.TO_LEFT) return +1f;
        else return -1f;

    }

    private interface IAttackAngles {
        int ATTACK_UPPER_MAX_ANGLE = 45;
        int ATTACK_LOWER_MIN_ANGLE = 35;
        int MAX_UPPER_ANGLE_FOR_RIGHT_SIDE = 360-ATTACK_UPPER_MAX_ANGLE;
        int MIN_LOWER_ANGLE_FOR_RIGHT_SIDE = ATTACK_LOWER_MIN_ANGLE;
        int MAX_UPPER_ANGLE_FOR_LEFT_SIDE = 180+ATTACK_UPPER_MAX_ANGLE;

        int MIN_LOWER_ANGLE_FOR_LEFT_SIDE = 180-ATTACK_LOWER_MIN_ANGLE;

    }

    public void setLuggageHeight(int luggageHeight) {
        this.luggageHeight = luggageHeight;
    }
}