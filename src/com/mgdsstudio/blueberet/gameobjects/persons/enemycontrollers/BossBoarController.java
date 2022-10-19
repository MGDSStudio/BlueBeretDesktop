package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.BossBoar;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.PortraitPicture;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMSController;
import com.mgdsstudio.blueberet.graphic.splashes.JumpSplash;
import com.mgdsstudio.blueberet.graphic.splashes.Splash;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import com.mgdsstudio.blueberet.zones.MessageAddingZone;
import com.mgdsstudio.blueberet.zones.SingleFlagZone;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.io.File;
import java.util.ArrayList;

public class BossBoarController extends EnemyController{



    private interface LocalStatements{
        int IDLE = 0;
        int RUN_4_TO_3 = 1;
        int RUN_3_TO_2 = 2;
        int RUN_2_TO_1_ABOVE = 3;
        int RUN_2_TO_1_LOWER = 4;
        int RUN_1_TO_4 = 5;
    }

    private interface GlobalStatements{
        int START_TIMER = 0;
        int FIRST_START_MESSAGE = 1;
        int SECOND_START_MESSAGE = 2;
        int FIRST_STAGE = 10;
        int WAIT_FOR_SECOND_MESSAGE = 11;
        int SECOND_MESSAGE = 12;
        int SECOND_STAGE = 13;
        int WAIT_FOR_THIRD_MESSAGE = 14;
        int THIRD_MESSAGE = 15;
        int THIRD_STAGE = 16;
        int WAIT_FOR_PLACE_FOR_DEAD = 17;
        int START_TO_DIE = 18;
        int END_MESSAGE = 19;

        int PLAYER_DEAD = 30;
        int WON = 20;
    }

    private interface Constants{
        int START_TIMER_TIME = 1000;
        boolean RUN_MOVEMENT = true;
        boolean MAX_VELOCITY = true;
        boolean MIN_VELOCITY = false;

        int BOAR_FIRST_MESSAGE = 0;
        int BOAR_SECOND_MESSAGE = 1;
        int BOAR_THIRD_MESSAGE = 2;
        int BEFORE_MUSIC_STARTING_TIME = 1300;
    }


    private final Flag leftLowerZone;
    private final Flag leftUpperZone;
    private final Flag rightLowerZone;
    private final Flag rightUpperZone;

    private final Flag rightLowerJump;
    private final Flag rightUpperJump;
    private final Flag leftUpperJump;
    private final Flag leftLowerJump;

    private final GameRound gameRound;

    //private final int

    private int localStatement = LocalStatements.IDLE;
    private int globalStatement = GlobalStatements.START_TIMER;


    private boolean movementArt = !Constants.RUN_MOVEMENT;
    private int actualAnimationArt = EnemiesAnimationController.IDLE;
    private boolean showMessages = true;
    private int criticalFirstLife;
    private int criticalSecondLife;
    private float randomCriticalValueForDirectionChanging = 80f;
    private float basicAccelerate;
    private float basicMaxVelocity;
    private Timer audioStartingTimer;

    private boolean audioStarted;
    private boolean jumpAfterPlayerDeadMustBeAdded;
    private boolean activeActionsWereStopped;
    private boolean bodyWasSimplyfiedAfterPlayedDead;
    private boolean helpMessageWasAlreadyShownIfNeed;
    //private MusicInGameController musicInGameController;

    public BossBoarController(Enemy enemy, Flag leftLower, Flag leftUpper, Flag rightLower, Flag rightUpper, GameRound gameRound, Flag rightLowerJump, Flag rightUpperJump, Flag leftUpperJump, Flag leftLowerJump) {
        super(enemy, 0);
        this.leftLowerZone = leftLower;
        this.leftUpperZone = leftUpper;
        this.rightLowerZone = rightLower;
        this.rightUpperZone = rightUpper;

        this.rightLowerJump = rightLowerJump;
        this.rightUpperJump = rightUpperJump;
        this.leftUpperJump = leftUpperJump;
        this.leftLowerJump = leftLowerJump;

        this.gameRound = gameRound;
        if (!showMessages) {
            globalStatement = GlobalStatements.FIRST_STAGE;
            localStatement = LocalStatements.RUN_4_TO_3;
        }
        initCriticalLifeValues();
        basicAccelerate = enemy.getActualAccelerate();
        basicMaxVelocity = enemy.getMaxVelocityAlongX();
        //musicInGameController = new MusicInGameController(Program.getAbsolutePathToAssetsFolder("Boss1.wav"), false, 1.0f);

    }

    private void initCriticalLifeValues() {
        final float CRITICAL_FIRST_VALUE = 0.75f;
        final float CRITICAL_SECOND_VALUE = 0.45f;
        int maxLife = enemy.getMaxLife();
        criticalFirstLife = (int) ((float)maxLife*CRITICAL_FIRST_VALUE);
        criticalSecondLife = (int) ((float)maxLife*CRITICAL_SECOND_VALUE);
    }
    //BossBoar 1:510,1640,150,10,-40,1700,-80,1400,680,1700,720,1400,240,240,440,1680,600,1480,40,1480,200,1680,80,80


    private Flag getZone(PVector pixelPos){
        if (leftLowerZone.inZone(pixelPos)) return leftLowerZone;
        else if (leftUpperZone.inZone(pixelPos)) return leftUpperZone;
        else if (rightLowerZone.inZone(pixelPos)) return  rightLowerZone;
        else if (rightUpperZone.inZone(pixelPos)) return rightUpperZone;
        else if (leftLowerJump.inZone(pixelPos)) return leftLowerJump;
        else if (leftUpperJump.inZone(pixelPos)) return leftUpperJump;
        else if (rightLowerJump.inZone(pixelPos)) return rightLowerJump;
        else if (rightUpperJump.inZone(pixelPos)) return rightUpperJump;
        else return null;
    }

    private boolean mustBeMovementStoppedAfterPlayerDead(GameRound gameRound) {
        int playerAngleInDegrees = (int) PApplet.degrees(gameRound.getPlayer().body.getAngle());
        if (playerAngleInDegrees > 20 || playerAngleInDegrees < (-20)) {
            //if (gameRound.getPlayer().body.getAngle() > PConstants.QUARTER_PI || gameRound.getPlayer().body.getAngle() < (-PConstants.QUARTER_PI)){
            //System.out.println("Player lays already");
            PVector playerPos = gameRound.getPlayer().getPixelPosition();
            Flag zoneOfPlayer = getZone(playerPos);
            if (zoneOfPlayer == null) {
                //System.out.println("Player doesnot lay on some zone");
                if (isInSomeKeyZone(enemy.getPixelPosition())) {
                    //System.out.println("Boar is in some zone. Movement must be stopped");
                    return true;
                }
                //else System.out.println("Boar is not in some zone");
            } else {
                Flag enemyZone = getZone(enemy.getPixelPosition());
                if (enemyZone == null) {
                    //System.out.println("Player lays in zone but the boar is in another zone. Boar must run");
                    return false;
                } else {
                    if (isObjectInOppositeZone(zoneOfPlayer, enemyZone)) {
                        //System.out.println("Enemy is in an another zone. Movement must be stopped");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    private boolean mustBeMovementStoppedAfterPlayerDead(GameRound gameRound){
        int playerAngleInDegrees = (int)PApplet.degrees(gameRound.getPlayer().body.getAngle());

        if (gameRound.getPlayer().body.getAngle() > PConstants.QUARTER_PI || gameRound.getPlayer().body.getAngle() < (-PConstants.QUARTER_PI)){
            System.out.println("Player lays already");
            PVector playerPos = gameRound.getPlayer().getPixelPosition();
            Flag zoneOfPlayer = getZone(playerPos);
            if (zoneOfPlayer == null){
                //System.out.println("Player doesnot lay on some zone");
                if (isInSomeKeyZone(enemy.getPixelPosition())){
                    //System.out.println("Boar is in some zone. Movement must be stopped");
                    return true;
                }
                //else System.out.println("Boar is not in some zone");
            }
            else {
                Flag enemyZone = getZone(enemy.getPixelPosition());
                if (enemyZone == null){
                    //System.out.println("Player lays in zone but the boar is in another zone. Boar must run");
                    return false;
                }
                else {
                    if (isObjectInOppositeZone(zoneOfPlayer, enemyZone)) {
                        //System.out.println("Enemy is in an another zone. Movement must be stopped");
                        return true;
                    }
                }
            }
        }
        return false;
    }
     */


    private boolean isObjectInOppositeZone(Flag zoneOfPlayer, Flag zoneOfBoar) {
        if (zoneOfPlayer.equals(zoneOfBoar)) return  false;
        else {
            return true;
        }

    }

    private boolean isInSomeKeyZone(PVector pixelPosition) {
        if (leftLowerZone.inZone(pixelPosition)) return true;
        else if (leftUpperZone.inZone(pixelPosition)) return true;
        else if (rightLowerZone.inZone(pixelPosition)) return true;
        else if (rightUpperZone.inZone(pixelPosition)) return true;
        else return false;
    }

    private void updateAfterPlayingDead(GameRound gameRound){
        if (activeActionsWereStopped) {
            globalStatement = GlobalStatements.FIRST_STAGE;
            localStatement = LocalStatements.IDLE;
            actualAnimationArt = EnemiesAnimationController.IDLE;
        }
        else {
            if (!bodyWasSimplyfiedAfterPlayedDead){
                BossBoar bossBoar = (BossBoar) enemy;
                bossBoar.deleteFeet();
                bodyWasSimplyfiedAfterPlayedDead = true;
            }
            else {
                if (mustBeMovementStoppedAfterPlayerDead(gameRound)) {
                    activeActionsWereStopped = true;
                    System.out.println("activities were stopped at " + Program.engine.frameCount);
                } else {
                    updateBoarGoing();
                    updateBoarJumping(gameRound);
                }
            }
        }
    }

    @Override
    public void update(GameRound gameRound) {
        if (!gameRound.getPlayer().isAlive()){
            updateAfterPlayingDead(gameRound);
        }
        else {
            if (globalStatement == GlobalStatements.START_TIMER) {
                if (stayingTimer == null) stayingTimer = new Timer(Constants.START_TIMER_TIME);
                if (stayingTimer.isTime()) {
                    createStartPlayerMessage(gameRound);
                    globalStatement = GlobalStatements.FIRST_START_MESSAGE;
                }
            } else if (globalStatement == GlobalStatements.FIRST_START_MESSAGE) {
                if (gameRound.isUpperPanelFreeFromMessages()) {
                    createBoarRrrrMessage(gameRound, Constants.BOAR_FIRST_MESSAGE);
                    globalStatement = GlobalStatements.SECOND_START_MESSAGE;
                }
            } else if (globalStatement == GlobalStatements.SECOND_START_MESSAGE) {
                if (gameRound.isUpperPanelFreeFromMessages()) {
                    globalStatement = GlobalStatements.FIRST_STAGE;
                    localStatement = LocalStatements.RUN_4_TO_3;
                    actualAnimationArt = EnemiesAnimationController.GO;
                    gameRound.getSoundController().setAndPlayAudio(SoundsInGame.BOSS_INTRO);
                    gameRound.loadNewSoundtrack(Program.getAbsolutePathToAssetsFolder("Boss1.wav"), false, 1.0f);
                    audioStartingTimer = new Timer(Constants.BEFORE_MUSIC_STARTING_TIME);

                }
            } else if (globalStatement == GlobalStatements.FIRST_STAGE || globalStatement == GlobalStatements.WAIT_FOR_SECOND_MESSAGE) {
                addHelpMessageIfNeeds(gameRound);
                if (!audioStarted) {
                    if (audioStartingTimer != null && audioStartingTimer.isTime()){
                        audioStarted = true;
                        //gameRound.getMusicController().startToPlay();
                        System.out.println("Audio started to play");
                    }
                }
                if (localStatement != LocalStatements.IDLE) {
                    updateBoarGoing();
                    updateBoarJumping(gameRound);
                    if (globalStatement == GlobalStatements.WAIT_FOR_SECOND_MESSAGE){
                        updateTransferToSecondMessage(gameRound);
                    }
                } else if (actualAnimationArt != EnemiesAnimationController.GO)
                    actualAnimationArt = EnemiesAnimationController.GO;
            }
            else if (globalStatement == GlobalStatements.SECOND_MESSAGE){
                if (gameRound.isUpperPanelFreeFromMessages()){
                    globalStatement = GlobalStatements.SECOND_STAGE;
                    actualAnimationArt = EnemiesAnimationController.RUN;
                    localStatement = LocalStatements.RUN_4_TO_3;
                    //BossBoar bossBoar = (BossBoar) enemy;
                    //bossBoar.dublicateAccelerate(bossBoar.getActualAccelerate());
                }
            }
            else if (globalStatement == GlobalStatements.SECOND_STAGE || globalStatement == GlobalStatements.WAIT_FOR_THIRD_MESSAGE) {
                if (localStatement != LocalStatements.IDLE) {
                    if (localStatement != LocalStatements.RUN_2_TO_1_ABOVE && localStatement != LocalStatements.RUN_2_TO_1_LOWER) {
                        updateBoarRunning();
                    }
                    else updateBoarGoing();
                    updateBoarJumping(gameRound);
                    if (globalStatement == GlobalStatements.WAIT_FOR_THIRD_MESSAGE){
                        updateTransferToThirdMessage(gameRound);
                    }
                } else if (actualAnimationArt != EnemiesAnimationController.RUN)
                    actualAnimationArt = EnemiesAnimationController.RUN;
            }
            else if (globalStatement == GlobalStatements.THIRD_MESSAGE){
                if (gameRound.isUpperPanelFreeFromMessages()){
                    globalStatement = GlobalStatements.THIRD_STAGE;
                    actualAnimationArt = EnemiesAnimationController.RUN;
                    localStatement = LocalStatements.RUN_4_TO_3;
                }
            }
            else if (globalStatement == GlobalStatements.THIRD_STAGE) {
                if (localStatement != LocalStatements.IDLE) {
                    if (localStatement != LocalStatements.RUN_2_TO_1_ABOVE && localStatement != LocalStatements.RUN_2_TO_1_LOWER) {
                        updateBoarRunning();
                        resetMaxVelocity();
                        updateBoarDirectionChanging(gameRound);
                    }
                    else updateBoarGoing();
                    updateBoarJumping(gameRound);
                }
            }
            else if (globalStatement == GlobalStatements.WAIT_FOR_PLACE_FOR_DEAD){
                updateBoarGoing();
                PVector pos = enemy.getPixelPosition();
                if (!leftUpperZone.inZone(pos) || !leftLowerZone.inZone(pos) || !rightUpperZone.inZone(pos) || !rightLowerZone.inZone(pos)){
                    globalStatement = GlobalStatements.START_TO_DIE;
                }
            }
            else if (globalStatement == GlobalStatements.START_TO_DIE){
                if (actualAnimationArt != EnemiesAnimationController.DYING) actualAnimationArt = EnemiesAnimationController.DYING;
                if (enemy.getPersonAnimationController().getAnimationForType(EnemiesAnimationController.DYING).isAnimationAlreadyShown()){
                    enemy.kill();
                    System.out.println("Boar dead");
                    globalStatement = GlobalStatements.END_MESSAGE;
                    enemy.simplifyBody();
                    //Splash splash = new JumpSplash(enemy);
                    //gameRound.addSplash(splash);
                    gameRound.addJumpSplash(enemy);
                    createBossKilledMessage(gameRound);
                }
            }
            else if (globalStatement == GlobalStatements.END_MESSAGE){
                if (gameRound.isUpperPanelFreeFromMessages()){
                    globalStatement = GlobalStatements.WON;
                    Flag endLevelFlag = new Flag(gameRound.getPlayer().getPixelPosition(), 800,800, Flag.END_LEVEL);
                    EndLevelZone endLevelZone = new EndLevelZone(endLevelFlag, (byte) SingleFlagZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE);
                    gameRound.addEndLevelZone(endLevelZone);
                    ArrayList <CameraFixationZone> cameraFixationZones = gameRound.getCameraFixationZones();
                    for (CameraFixationZone zone : cameraFixationZones) zone.switchOff();
                    System.out.println("Boss killed");
                }
            }
            //System.out.println("Statement: " + globalStatement);
            /*
            else if (globalStatement == GlobalStatements.SECOND_STAGE || globalStatement == GlobalStatements.WAIT_FOR_THIRD_MESSAGE) {
                if (localStatement != LocalStatements.IDLE) {
                    updateBoarGoing();
                    updateBoarJumping();
                    if (globalStatement == GlobalStatements.WAIT_FOR_SECOND_MESSAGE){
                        updateTransferToSecondMessage(gameRound);
                    }
                } else if (actualAnimationArt != EnemiesAnimationController.IDLE)
                    actualAnimationArt = EnemiesAnimationController.IDLE;
            }*/
        }
    }

    private void addHelpMessageIfNeeds(GameRound gameRound) {
        if (!helpMessageWasAlreadyShownIfNeed) {
                if (leftUpperJump.inZone(enemy.getPixelPosition())) {
                    HowToKillMessageAdder messageAdder = new HowToKillMessageAdder();
                    if (messageAdder.mustBeMessageAdded()) {
                        messageAdder.addMessage(gameRound);
                    }
                    helpMessageWasAlreadyShownIfNeed = true;
                }


        }
    }

    private void resetMaxVelocity() {
        if (localStatement != LocalStatements.RUN_4_TO_3) setVelocity(Constants.MIN_VELOCITY);
    }

    private void updateBoarDirectionChanging(GameRound gameRound) {
        if (localStatement == LocalStatements.RUN_3_TO_2){
            if (rightLowerJump.inZone(enemy.getPixelPosition())){
                if (rightLowerZone.inZone(gameRound.getPlayer().getPixelPosition())){
                    if (mustBeDirectionChanged()) {
                        if (Program.debug) System.out.println("Direction changed from 3-2 to 1-4");
                        localStatement = LocalStatements.RUN_1_TO_4;
                    }
                }
            }
        }
        else if (localStatement == LocalStatements.RUN_4_TO_3){
            if (leftLowerJump.inZone(enemy.getPixelPosition())){
                if (leftUpperZone.inZone(gameRound.getPlayer().getPixelPosition()) || leftUpperJump.inZone(gameRound.getPlayer().getPixelPosition())){
                    if (mustBeDirectionChanged()) {
                        if (Program.debug) System.out.println("Direction changed from 4-3 to 2-1 LOWER");
                        localStatement = LocalStatements.RUN_2_TO_1_LOWER;
                    }
                }
            }
        }
        else if (localStatement == LocalStatements.RUN_1_TO_4){
            if (rightLowerJump.inZone(enemy.getPixelPosition()) || leftLowerJump.inZone(enemy.getPixelPosition())){
                if (leftLowerZone.inZone(gameRound.getPlayer().getPixelPosition()) || leftLowerJump.inZone(gameRound.getPlayer().getPixelPosition())){
                    if (mustBeDirectionChanged()) {
                        if (Program.debug) System.out.println("Direction changed from 1-4 to 4-3");
                        localStatement = LocalStatements.RUN_4_TO_3;
                    }
                }
            }
        }
        else if (localStatement == LocalStatements.RUN_2_TO_1_ABOVE){
            if (leftLowerZone.inZone(gameRound.getPlayer().getPixelPosition()) || leftLowerJump.inZone(gameRound.getPlayer().getPixelPosition())){
                if (mustBeDirectionChanged()) {
                    if (Program.debug) System.out.println("Direction changed from 1-2 above to 2-3");
                    localStatement = LocalStatements.RUN_4_TO_3;
                    setVelocity(Constants.MAX_VELOCITY);
                }
            }
        }
    }

    private void setVelocity(boolean velocuty) {
        /*BossBoar bossBoar = (BossBoar)enemy;
        if (velocuty == Constants.MAX_VELOCITY){
            bossBoar.maxVelocityAlongX = basicMaxVelocity*2f;
            System.out.println("Velocity is up: " + bossBoar.maxVelocityAlongX);
        }
        else {
            bossBoar.maxVelocityAlongX = basicMaxVelocity;
            System.out.println("Velocity is down: " + bossBoar.maxVelocityAlongX);
        }*/
    }

    private boolean mustBeDirectionChanged() {
        float random = Program.engine.random(100f);
        if (random<=randomCriticalValueForDirectionChanging) {
            return true;
        }
        else return false;
    }

    private void updateTransferToSecondMessage(GameRound gameRound) {
        if (localStatement == LocalStatements.RUN_4_TO_3) {
            if (rightLowerZone.inZone(enemy.getPixelPosition())){
                if (enemy.body.getLinearVelocity().x<(-0.5f)){
                    localStatement = LocalStatements.IDLE;
                    actualAnimationArt = EnemiesAnimationController.IDLE;
                    createBoarRrrrMessage(gameRound,Constants.BOAR_SECOND_MESSAGE);
                    globalStatement = GlobalStatements.SECOND_MESSAGE;
                }
            }
        }
    }

    private void updateTransferToThirdMessage(GameRound gameRound) {
        if (localStatement == LocalStatements.RUN_4_TO_3) {
            if (rightLowerZone.inZone(enemy.getPixelPosition())){
                if (enemy.body.getLinearVelocity().x<(-0.5f)){
                    localStatement = LocalStatements.IDLE;
                    actualAnimationArt = EnemiesAnimationController.IDLE;
                    createBoarRrrrMessage(gameRound,Constants.BOAR_THIRD_MESSAGE);
                    globalStatement = GlobalStatements.THIRD_MESSAGE;
                }
            }
        }
    }



    private void updateBoarJumping(GameRound gameRound) {
        if (enemy.getStatement() != Person.IN_AIR){
            if (inJumpZone(gameRound)){
                makeJump();
            }
        }
    }



    private void makeJump() {
        enemy.makeJump();
    }

    private boolean inJumpZone(GameRound gameRound) {
        if (localStatement == LocalStatements.RUN_3_TO_2){
            if (rightLowerJump.inZone(enemy.getPixelPosition()) ){
                return true;
            }
        }
        else if (localStatement == LocalStatements.RUN_2_TO_1_ABOVE){
            if (rightUpperJump.inZone(enemy.getPixelPosition()) && enemy.body.getLinearVelocity().x<0.02f){
                return true;
            }
        }
        else if (localStatement == LocalStatements.RUN_2_TO_1_LOWER){
            if (leftLowerJump.inZone(enemy.getPixelPosition())){
                if (leftUpperZone.inZone(gameRound.getPlayer().getPixelPosition()) || leftUpperJump.inZone(gameRound.getPlayer().getPixelPosition())){
                    //Пластырь
                    return true;
                }
                else{
                    System.out.println("Trouble!");
                    localStatement = LocalStatements.RUN_4_TO_3;
                    return false;
                }
            }
        }
        return false;
    }

    private void updateBoarGoing() {
        if (actualAnimationArt != EnemiesAnimationController.GO) actualAnimationArt = EnemiesAnimationController.GO;
        if (localStatement == LocalStatements.RUN_4_TO_3 || localStatement == LocalStatements.RUN_2_TO_1_LOWER || localStatement == LocalStatements.RUN_2_TO_1_ABOVE){
            enemy.move(true, Person.TO_LEFT);
            if (leftLowerZone.inZone(enemy.getPixelPosition()) || leftUpperZone.inZone((enemy.getPixelPosition()))){
                if (localStatement == LocalStatements.RUN_4_TO_3) {
                    localStatement = LocalStatements.RUN_3_TO_2;
                }
                else if (localStatement == LocalStatements.RUN_2_TO_1_LOWER || localStatement == LocalStatements.RUN_2_TO_1_ABOVE) {
                    localStatement = LocalStatements.RUN_1_TO_4;
                }
            }
        }
        else if (localStatement == LocalStatements.RUN_3_TO_2 || localStatement == LocalStatements.RUN_1_TO_4){
            enemy.move(true, Person.TO_RIGHT);
            if (rightLowerZone.inZone(enemy.getPixelPosition())){
               localStatement = LocalStatements.RUN_4_TO_3;
            }
            else if (rightUpperZone.inZone(enemy.getPixelPosition())){
                localStatement = LocalStatements.RUN_2_TO_1_ABOVE;
            }
        }
        updateAnimationArt(gameRound);
    }

    private void updateBoarRunning() {
        if (localStatement == LocalStatements.RUN_4_TO_3 || localStatement == LocalStatements.RUN_2_TO_1_LOWER || localStatement == LocalStatements.RUN_2_TO_1_ABOVE){
            enemy.run(true, Person.TO_LEFT);
            if (leftLowerZone.inZone(enemy.getPixelPosition()) || leftUpperZone.inZone((enemy.getPixelPosition()))){
                if (localStatement == LocalStatements.RUN_4_TO_3) {
                    localStatement = LocalStatements.RUN_3_TO_2;
                }
                else if (localStatement == LocalStatements.RUN_2_TO_1_LOWER || localStatement == LocalStatements.RUN_2_TO_1_ABOVE) {
                    localStatement = LocalStatements.RUN_1_TO_4;
                }
            }
        }
        else if (localStatement == LocalStatements.RUN_3_TO_2 || localStatement == LocalStatements.RUN_1_TO_4){
            enemy.run(true, Person.TO_RIGHT);
            if (rightLowerZone.inZone(enemy.getPixelPosition())){
                localStatement = LocalStatements.RUN_4_TO_3;
            }
            else if (rightUpperZone.inZone(enemy.getPixelPosition())){
                localStatement = LocalStatements.RUN_2_TO_1_ABOVE;
            }
        }
        updateAnimationArt(gameRound);
    }

    private void updateAnimationArt(GameRound gameRound) {
        if (enemy.getStatement() != Person.IN_AIR ) {
            float criticalVelocityToSwitchOnRunAnimation = 12f;
            float criticalVelocityToSwitchOnGoAnimation = 0.1f;
            if (!activeActionsWereStopped) {
                if (enemy.body.getLinearVelocity().x > criticalVelocityToSwitchOnRunAnimation || enemy.body.getLinearVelocity().x < (-criticalVelocityToSwitchOnRunAnimation)) {
                    if (globalStatement != GlobalStatements.FIRST_STAGE && globalStatement != GlobalStatements.WAIT_FOR_PLACE_FOR_DEAD) actualAnimationArt = EnemiesAnimationController.RUN;
                    else actualAnimationArt = EnemiesAnimationController.GO;
                } else if (enemy.body.getLinearVelocity().x > criticalVelocityToSwitchOnGoAnimation || enemy.body.getLinearVelocity().x < (-criticalVelocityToSwitchOnGoAnimation)) {
                    actualAnimationArt = EnemiesAnimationController.GO;
                } else actualAnimationArt = EnemiesAnimationController.IDLE;
            }
            else actualAnimationArt = EnemiesAnimationController.IDLE;
        }
        else actualAnimationArt = EnemiesAnimationController.IDLE;
    }

    private void createBossKilledMessage(GameRound gameRound) {
        String text = " I thought that it is immortal.   ";
        if (Program.LANGUAGE == Program.RUSSIAN) text = " Я думала он бессмертный...        ";
        Flag activationZone = new Flag(new PVector(enemy.getPixelPosition().x, enemy.getPixelPosition().y), 2000, 2000, Flag.MESSAGE_ADDING_ZONE);
        SMS sms = new SMS(text, SMSController.CLOSE, PortraitPicture.PLAYER_SPEAKING_FACE, 2000);
        //public MessageAddingZone(Flag flag, SMS message, int activatingCondition, boolean withPlayerBlocking, PVector cameraPosToConcentrate, boolean scale
        MessageAddingZone messageAddingZone = new MessageAddingZone(activationZone, sms, MessageAddingZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE, true, enemy.getPixelPosition(), CameraFixationZone.MAX_SCALE);
        gameRound.addMessage(messageAddingZone);
    }

    private void createBoarRrrrMessage(GameRound gameRound, int agressiveStage) {
        String text = "Arrghh!!!";
        if (agressiveStage == Constants.BOAR_SECOND_MESSAGE) text = "Arrghh!!!           Rrrrrrr!      ";
        else if (agressiveStage == Constants.BOAR_THIRD_MESSAGE) text = "Arrghh!!!           Rrrrrrr!              Arrghh!! ";
        if (Program.LANGUAGE == Program.RUSSIAN) {
            text = " Ррррррр!!!    ";
            if (agressiveStage == Constants.BOAR_SECOND_MESSAGE) text = " Ррррррр!!!         Аррррррр!!!     ";
            else if (agressiveStage == Constants.BOAR_THIRD_MESSAGE) text = " Ррррррр!!!        Аррррррр!!!           Ррррррр!!! ";
        }
        PortraitPicture portraitPicture = PortraitPicture.BOSS_BOAR;
        if (agressiveStage == Constants.BOAR_THIRD_MESSAGE)  portraitPicture = PortraitPicture.BOSS_BOAR_WITH_BLOOD;
        Flag activationZone = new Flag(new PVector(enemy.getPixelPosition().x, enemy.getPixelPosition().y), 2000, 2000, Flag.MESSAGE_ADDING_ZONE);
        SMS sms = new SMS(text, SMSController.CLOSE, portraitPicture, 2000);
        //public MessageAddingZone(Flag flag, SMS message, int activatingCondition, boolean withPlayerBlocking, PVector cameraPosToConcentrate, boolean scale
        MessageAddingZone messageAddingZone = new MessageAddingZone(activationZone, sms, MessageAddingZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE, true, enemy.getPixelPosition(), CameraFixationZone.MAX_SCALE);
        gameRound.addMessage(messageAddingZone);
    }

    private void createStartPlayerMessage(GameRound gameRound) {
        String text = "My god! This wild boar seems to be invulnerable. I need to find its weak spot ";
        if (Program.LANGUAGE == Program.RUSSIAN) text = "Боже мой, этот кабан выглядит просто неуязвимым. Надо найти его слабое место ";
        Flag activationZone = new Flag(new PVector(enemy.getPixelPosition().x, enemy.getPixelPosition().y), 2000, 2000, Flag.MESSAGE_ADDING_ZONE);
        SMS sms = new SMS(text, SMSController.CLOSE, PortraitPicture.PLAYER_SPEAKING_FACE, 2000);
        //public MessageAddingZone(Flag flag, SMS message, int activatingCondition, boolean withPlayerBlocking, PVector cameraPosToConcentrate, boolean scale
        MessageAddingZone messageAddingZone = new MessageAddingZone(activationZone, sms, MessageAddingZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE, true, gameRound.getPlayer().getPixelPosition(), CameraFixationZone.MAX_SCALE);
        gameRound.addMessage(messageAddingZone);
    }

    @Override
    public void kill() {

    }

    @Override
    public boolean isStartedToDie() {
        return false;
    }



    @Override
    public void draw(EnemiesAnimationController enemiesAnimationController, GameCamera gameCamera, Vec2 pos, float angleInRadians) {
        if (Program.debug){
            leftLowerZone.draw(gameCamera);
            leftUpperZone.draw(gameCamera);
            rightLowerZone.draw(gameCamera);
            rightUpperZone.draw(gameCamera);

            rightLowerJump.draw(gameCamera);
            rightUpperJump.draw(gameCamera);
            leftUpperJump.draw(gameCamera);
            leftLowerJump.draw(gameCamera);
        }
        if (!enemy.isAlive()) {
            /*if (deathType == CIRCLE_CORPSE) {
                enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.CORPSE_SINGLE_SPRITE, pos, angleInRadians, false);
            }
            else enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.HANG, pos, angleInRadians, false);*/
            enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.CORPSE_SINGLE_SPRITE, pos, angleInRadians, lastGraphicFlip);
        }
        else {
            //gameCamera.setScale(GameCamera.minScale-0.2f);
            if (enemy.body.getLinearVelocity().x < -0.02f) lastGraphicFlip = true;
            else if (enemy.body.getLinearVelocity().x > 0.02f) lastGraphicFlip = false;
            //System.out.println("Art: " + actualAnimationArt);
            enemiesAnimationController.drawAnimation(gameCamera, actualAnimationArt, pos, angleInRadians, lastGraphicFlip);

        }
    }

    int getActualAnimationArt(){
        return actualAnimationArt;
    }

    @Override
    public void attacked(){
        //enemy.recoveryLifeInAbsoluteValues(enemy.getMaxLife());
        /*if (globalStatement == GlobalStatements.FIRST_STAGE){
            //if (enemy.getLife())
            globalStatement = GlobalStatements.WAIT_FOR_SECOND_MESSAGE;
        }*/

    }

    @Override
    public int correctAttackValue(int value) {
        int newValue = enemy.getLife()-value;
        if (globalStatement == GlobalStatements.FIRST_STAGE){
            if (newValue < criticalFirstLife){
                if (Program.debug) System.out.println("Transfer to second stage. Critical: " + criticalFirstLife + "; value: " + value + "; Actual life: " + enemy.getLife() + "; New value: " + newValue);
                globalStatement = GlobalStatements.WAIT_FOR_SECOND_MESSAGE;
                return (enemy.getLife()-criticalFirstLife);
            }
        }
        else if (globalStatement == GlobalStatements.SECOND_STAGE){
            if (newValue < criticalSecondLife){
                if (Program.debug) System.out.println("Transfer to third stage. Critical: " + criticalFirstLife + "; value: " + value + "; Actual life: " + enemy.getLife() + "; New value: " + newValue);
                globalStatement = GlobalStatements.WAIT_FOR_THIRD_MESSAGE;
                return (enemy.getLife()-criticalSecondLife);
            }
        }
        else if (globalStatement == GlobalStatements.THIRD_STAGE){
            //if (newValue <= 0){
                globalStatement = GlobalStatements.WAIT_FOR_PLACE_FOR_DEAD;
                return 0;
            //}
        }
        System.out.println("Boar can not be attacked right now");
        return 0;
    }

    class JumpController{
        private Flag lastVisitedZone;

        JumpController(){
            //lastVisitedZone = rightLowerZone;
        }

        void update(){
            //lastVisitedZone = getZone(enemy.getPixelPosition());
        }

        void canBeJumpMade(){
            //if ()
        }

        void jumpWasMade(Flag zone){
            this.lastVisitedZone = zone;
        }
    }



    @Override
    public boolean canBeAttacked() {
        /*
        int WAIT_FOR_PLACE_FOR_DEAD = 17;
        int START_TO_DIE = 18;
        int END_MESSAGE = 19;

        int PLAYER_DEAD = 30;
        int WON = 20;
         */
        if (globalStatement == GlobalStatements.WAIT_FOR_PLACE_FOR_DEAD || globalStatement == GlobalStatements.START_TO_DIE || globalStatement == GlobalStatements.END_MESSAGE || globalStatement == GlobalStatements.WON) return  false;
        else return true;
    }

    public void playerCanNotKillEnemy() {
        HowToKillMessageAdder messageAdder = new HowToKillMessageAdder();
        messageAdder.playerCanNotKillBoss();


    }
    private class HowToKillMessageAdder{
        private String fileName;
        private int trying = 1;
        private final String text = "Message 1 text was shown: ";

        HowToKillMessageAdder(){
            final String prefixName = "Boss1.txt";
            if (Program.OS == Program.ANDROID) fileName = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+prefixName;
            else if (Program.OS == Program.DESKTOP) fileName = Program.getAbsolutePathToAssetsFolder(prefixName);
        }

        boolean mustBeMessageAdded(){
            if (enemy.getLife() >= enemy.getMaxLife()) {
                File file = new File(fileName);
                if (file.exists()) {
                    if (Program.debug) System.out.println("Message was already shown");
                    String[] data = Program.engine.loadStrings(fileName);
                    try {
                        if (data != null) {
                            trying = Integer.parseInt(data[1]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trying > 0 && trying < 5) return true;
                    else return false;
                } else {
                    if (Program.debug) System.out.println("Message was not shown");
                    return false;
                }
            }
            else return false;
        }

        void addMessage(GameRound gameRound){
            String text = getMessageTextForTrying();

            Flag activationZone = new Flag(new PVector(enemy.getPixelPosition().x, enemy.getPixelPosition().y), 2000, 2000, Flag.MESSAGE_ADDING_ZONE);
            SMS sms = new SMS(text, SMSController.MARK_AS_READ, PortraitPicture.SMARTPHONE_WITH_UNREAD_MESSAGE, 3000);
            MessageAddingZone messageAddingZone = new MessageAddingZone(activationZone, sms, MessageAddingZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE, false, gameRound.getPlayer().getPixelPosition(), CameraFixationZone.MAX_SCALE);
            gameRound.addMessage(messageAddingZone);
            System.out.println("Message was added");
            //saveFile();
        }

        private String getMessageTextForTrying(){
            String text = " ";
            if (trying <= 1) {
                text = "Some wild animals have hard bones and difficult to killed from front. But the belly is not armored.  ";
                if (Program.LANGUAGE == Program.RUSSIAN)
                    text = "Некоторые дикие звери имеют крепкие кости и их сложно пробить. Но живот у животных не защищен.  ";
            }
            else if (trying == 2) {
                text = "Boars are armored but their tummies are weak. ";
                if (Program.LANGUAGE == Program.RUSSIAN)
                    text = "Дикие кабаны хорошо защищены, но их брюхо легко прострелить.  ";
            }
            else if (trying == 3) {
                text = "Shoot from down to up to hit boars. ";
                if (Program.LANGUAGE == Program.RUSSIAN)
                    text = "Стреляйте снизу вверх. Только так можно подстрелить кабанов. ";
            }
            else if (trying >= 4) {
                text = "Stay lower and shoot from down to up. Aim for the belly. It is only way to kill a wild boar.  ";
                if (Program.LANGUAGE == Program.RUSSIAN)
                    text = "Стойте ниже и стреляйте снизу вверх. Цельтесь в живот. Только так можно подстрелить кабана. ";
            }
            return text;
        }

        private void saveFile(){
            String[] data = new String[]{text, ""+trying};
            Program.engine.saveStrings(fileName, data);
            System.out.println("Data " + data[0] + "x" + data[1] + " were saved at " + fileName);
        }

        public void playerCanNotKillBoss() {
            File file = new File(fileName);
            if (file.exists()) {
                String [] data = Program.engine.loadStrings(fileName);
                try {
                    trying = Integer.parseInt(data[1]);
                    trying++;
                    saveFile();
                }
                catch (Exception e){
                    e.printStackTrace();
                    saveFile();
                }
                System.out.println("Player can not kill boss again. Already tryed " + trying + " ");
            }
            else {
                if (Program.debug) System.out.println("File " + fileName + " does not exist");
                saveFile();
            }
        }
    }

}
