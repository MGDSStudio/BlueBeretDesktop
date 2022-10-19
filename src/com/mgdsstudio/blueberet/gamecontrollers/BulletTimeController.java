package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.Syringe;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenMovableSprite;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;


import java.util.ArrayList;


public class BulletTimeController {
    private boolean activated;
    private final GameRound gameRound;

    private Timer timer = new Timer();
    private final static int NORMAL_TIME = Syringe.NORMAL_SLOW_TIME;

    private final static float PLAYER_PERFORMANCE_COEFFICIENT = 1.5f;
    public final static float BULLET_TIME_COEF_FOR_OBJECTS = 4f, bulletTimeCoefForPlayer = BULLET_TIME_COEF_FOR_OBJECTS/PLAYER_PERFORMANCE_COEFFICIENT;
    private final float slowingCoefficientForEnemies = 1f/BULLET_TIME_COEF_FOR_OBJECTS;
    private final float slowingCoefficientForPlayer = (1f/PLAYER_PERFORMANCE_COEFFICIENT);

    /*
    private final int FULL_DISAPPEARING_TIME = 200;
    private final int startDisappearingAlpha = 150;
    private final int endDisappearingAlpha = 50;
    private Timer fullSmoothnessTimer;

     */
    private final int END_DISAPPEARING_ALPHA = 5;
    private float devidingCoef = 4;
    private float smothenessVelocity = 0.6f;
    //private int tintValue = Program.engine.color(255, 40);
    private int tintValue = Program.engine.color(127, 157, 125, 40);
    public final static boolean TO_SLOW = false, TO_FAST = true;
    //private PGraphics mutPreviousFrame = Program.engine.createGraphics(Program.objectsFrame.width, Program.objectsFrame.height, PApplet.P2D);
    public final static boolean WHITE_RECT_ON_ANDROID = true;
    private Vec2 mutNewVelocity;
    private final boolean useWhitening = true;

    public BulletTimeController(GameRound gameRound, int time) {
        this.gameRound = gameRound;
        calculateSmothnessVelocity();
        activate(time);
    }

    private void calculateSmothnessVelocity() {

    }

    public BulletTimeController(GameRound gameRound) {
        this.gameRound = gameRound;
        calculateSmothnessVelocity();
        PhysicGameWorld.bulletTime = false;
    }

    public void activate(int time){
        if (!activated) {
            activated = true;
            setGravityByGravityScale(TO_SLOW);
            changeAccelerateForPersons(TO_SLOW);
            changeVelocities(TO_SLOW);
            if (timer == null) timer = new Timer(time);
            else timer.setNewTimer(time);
            changeAnimationsFrequencyForPersons(TO_SLOW);
            changeMoveableGraphicAccelerates(TO_SLOW);
            changeAnimationsFrequencyForOnScreenAnimations(TO_SLOW);
            changeBulletsVelocity(TO_SLOW);
            gameRound.updateTimersForBulletTime(TO_SLOW);
        }
    }

    private void applyAction(int time, boolean flag){

    }




    public void activate(){
        activate(NORMAL_TIME);
    }

    private void setGravityByGravityScale(boolean direction) {
        Soldier soldier = (Soldier) gameRound.getPlayer();
        float playerGravityScale = gameRound.getPlayer().body.getGravityScale();
        if (direction == TO_SLOW) {
            PhysicGameWorld.bulletTime = true;
            playerGravityScale*=(BULLET_TIME_COEF_FOR_OBJECTS);
        }
        else {
            PhysicGameWorld.bulletTime = false;
            playerGravityScale/=(BULLET_TIME_COEF_FOR_OBJECTS);
        }
        soldier.recalculateJumpVelocity(!direction);
        gameRound.getPlayer().body.m_gravityScale=playerGravityScale;
        gameRound.getPlayer().body.resetMassData();

    }
    /*
     private void setGravityByGravityScale(boolean direction) {
        Soldier soldier = (Soldier) gameRound.getPlayer();
        float playerGravityScale = gameRound.getPlayer().body.getGravityScale();
        if (direction == TO_SLOW) {
            PhysicGameWorld.bulletTime = true;
            playerGravityScale*=(bulletTimeCoefForPlayer);
        }
        else {
            PhysicGameWorld.bulletTime = false;
            playerGravityScale/=(bulletTimeCoefForPlayer);
        }
        soldier.recalculateJumpVelocity(!direction);
        gameRound.getPlayer().body.m_gravityScale=playerGravityScale;
        gameRound.getPlayer().body.resetMassData();

    }
     */


    /*
    private void setGravityByGravityScale(boolean direction) {
        Soldier soldier = (Soldier) gameRound.getPlayer();
        float playerGravityScale = gameRound.getPlayer().body.getGravityScale();
        if (direction == TO_SLOW) {
            PhysicGameWorld.bulletTime = true;
            playerGravityScale*=(bulletTimeCoefForPlayer);
        }
        else {
            PhysicGameWorld.bulletTime = false;
            playerGravityScale/=(bulletTimeCoefForPlayer);
        }
        soldier.recalculateJumpVelocity(!direction);
        gameRound.getPlayer().body.m_gravityScale=playerGravityScale;
        gameRound.getPlayer().body.resetMassData();

    }
     */



    private void changeAnimationsFrequencyForPersons(boolean frequencyChangingDirection){
        ArrayList <Person> persons = gameRound.getPersons();
        for (Person person : persons){
            PersonAnimationController animationController = person.getPersonAnimationController();
            ArrayList <SpriteAnimation> animations = animationController.getAnimationsList();
            for (SpriteAnimation spriteAnimation : animations){
                float frequence = spriteAnimation.getUpdateFrequency();
                float changingValue = BULLET_TIME_COEF_FOR_OBJECTS; //*4 for enemies
                if (person.getClass() == Soldier.class ) changingValue = PLAYER_PERFORMANCE_COEFFICIENT; //*2 for enemies
                if (frequencyChangingDirection == TO_FAST) frequence=frequence*changingValue;
                else frequence=frequence/(changingValue);
                spriteAnimation.setUpdateFrequency((int) frequence);
            }
        }
    }

    private void changeMoveableGraphicAccelerates(boolean direction) {
        for (IndependentOnScreenMovableSprite sprite : gameRound.getMoveableSprites()){
            float accelerateY = sprite.getyAccelerate();
            float accelerateX = sprite.getxAccelerate();
            float angleAccelerate = sprite.getAngleAccelerate();
            float multValue = BULLET_TIME_COEF_FOR_OBJECTS; //*4 for enemies
            if (direction == TO_SLOW) multValue = 1f/BULLET_TIME_COEF_FOR_OBJECTS;
            sprite.setxAccelerate(accelerateX*multValue);
            sprite.setyAccelerate(accelerateY*multValue);
            sprite.setAngleAccelerate(angleAccelerate*multValue);
        }
    }

    private void changeAnimationsFrequencyForOnScreenAnimations(boolean frequencyChangingDirection){
        ArrayList <IndependentOnScreenAnimation> animations = gameRound.getIndependentOnScreenAnimations();
        for (IndependentOnScreenAnimation animation : animations){
            float frequence = animation.spriteAnimation.getUpdateFrequency();
            float multValue = BULLET_TIME_COEF_FOR_OBJECTS; //*4 for enemies
            if (frequencyChangingDirection == TO_SLOW) multValue = 1f/BULLET_TIME_COEF_FOR_OBJECTS;
            animation.spriteAnimation.setUpdateFrequency((int) (frequence*multValue));
        }
    }

    private void changeBulletsVelocity(boolean direction){
        ArrayList <Bullet> bullets = gameRound.getBullets();
        for (Bullet bullet :  bullets){
            if (direction == TO_FAST){
                bullet.setNormalVelocity();
            }
            else bullet.setVelocityForBulletTimeMode();
        }
    }


    public void deactivate(){
        if (activated) {
            activated = false;
            changeAnimationsFrequencyForPersons(TO_FAST);
            changeAccelerateForPersons(TO_FAST);
            changeVelocities(TO_FAST);
            setGravityByGravityScale(TO_FAST);
            changeMoveableGraphicAccelerates(TO_FAST);
            changeAnimationsFrequencyForOnScreenAnimations(TO_FAST);
            changeBulletsVelocity(TO_FAST);
            gameRound.updateTimersForBulletTime(TO_FAST);
            timer.stop();
        }
    }

    private void changeAccelerateForPersons(boolean frequencyChangingDirection) {
        for (Person person : gameRound.getPersons()){
            float accelerate = person.getActualAccelerate();
            if (frequencyChangingDirection == TO_SLOW){
                if (person.equals(gameRound.getPlayer())) accelerate*=(BULLET_TIME_COEF_FOR_OBJECTS*PLAYER_PERFORMANCE_COEFFICIENT);
                else accelerate*=slowingCoefficientForEnemies;
            }
            else{
                if (person.equals(gameRound.getPlayer())) accelerate/=(BULLET_TIME_COEF_FOR_OBJECTS*PLAYER_PERFORMANCE_COEFFICIENT);
                else accelerate/=slowingCoefficientForEnemies;
            }
            person.setActualAccelerate(accelerate);

        }
    }

    /*
    private void changeAccelerateForPersons(boolean frequencyChangingDirection) {
        for (Person person : gameRound.getPersons()){
            float accelerate = person.getActualAccelerate();
            if (frequencyChangingDirection == TO_SLOW){
                if (person.equals(gameRound.getPlayer())) accelerate*=slowingCoefficientForPlayer;
                else accelerate*=slowingCoefficientForEnemies;
            }
            else{
                if (person.equals(gameRound.getPlayer())) accelerate/=slowingCoefficientForPlayer;
                else accelerate/=slowingCoefficientForEnemies;
            }
            person.setActualAccelerate(accelerate);
        }
    }
     */


    private void changeMaxVelocities(boolean frequencyChangingDirection){
        for (Person person : gameRound.getPersons()){
            float maxVelocity = person.getMaxVelocityAlongX();
            float maxVelocityForRun = person.getMaxVelocityAlongXByRunning();
            if (frequencyChangingDirection == TO_SLOW){
                if (person.equals(gameRound.getPlayer())) {
                    maxVelocity*=bulletTimeCoefForPlayer;
                    maxVelocityForRun*=bulletTimeCoefForPlayer;
                }
                else {
                    maxVelocity*=slowingCoefficientForEnemies;
                    maxVelocityForRun*=slowingCoefficientForEnemies;
                }
            }
            else{
                if (person.equals(gameRound.getPlayer())) {
                    maxVelocity/=bulletTimeCoefForPlayer;
                    maxVelocityForRun/=bulletTimeCoefForPlayer;
                }
                else {
                    maxVelocity/=slowingCoefficientForEnemies;
                    maxVelocityForRun/=slowingCoefficientForEnemies;
                }
            }
            person.setMaxVelocityAlongX(maxVelocity);
            person.setMaxVelocityAlongXByRunning(maxVelocityForRun);
        }
    }
    private void changeVelocities(boolean frequencyChangingDirection) {

        changeMaxVelocities(frequencyChangingDirection);
        changeActualAlongYVelocities(frequencyChangingDirection);
    }

    private void changeActualAlongYVelocities(boolean frequencyChangingDirection) {
        for (Person person : gameRound.getPersons()){
            if (frequencyChangingDirection == TO_SLOW){
                if (person.equals(gameRound.getPlayer())) {
                    //person.body.getLinearVelocity().y/=(bulletTimeCoefForPlayer);
                }
                else {
                    //person.body.getLinearVelocity().y/=slowingCoefficientForEnemies;
                }
            }
            else{
                if (person.equals(gameRound.getPlayer())) {
                    person.body.getLinearVelocity().y/=(bulletTimeCoefForPlayer);
                    person.body.getLinearVelocity().x/=(bulletTimeCoefForPlayer);
                }
                else {
                    person.body.getLinearVelocity().y/=slowingCoefficientForEnemies;
                    person.body.getLinearVelocity().x/=(slowingCoefficientForEnemies);
                }
            }
        }
    }

    /*
    private void changeAccelerateForPersons(boolean frequencyChangingDirection) {
        for (Person person : gameRound.getPersons()){
            float maxVelocity = person.getMaxVelocityAlongX();
            float maxVelocityForRun = person.getMaxVelocityAlongXByRunning();
                    //Soldier soldier = (Soldier)person;
            if (frequencyChangingDirection == TO_SLOW){
                if (person.equals(gameRound.getPlayer())) {
                    maxVelocity*=bulletTimeCoefForPlayer;
                    maxVelocityForRun*=bulletTimeCoefForPlayer;
                }
                else {
                    maxVelocity*=slowingCoefficientForEnemies;
                    maxVelocityForRun*=slowingCoefficientForEnemies;
                }
            }
            else{
                if (person.equals(gameRound.getPlayer())) {
                    maxVelocity/=bulletTimeCoefForPlayer;
                    maxVelocityForRun/=bulletTimeCoefForPlayer;
                }
                else {
                    maxVelocity/=slowingCoefficientForEnemies;
                    maxVelocityForRun/=slowingCoefficientForEnemies;
                }
            }
            person.setMaxVelocityAlongX(maxVelocity);
            person.setMaxVelocityAlongXByRunning(maxVelocityForRun);
        }
    }
     */



    public void update(){
        if (activated) {
            if (timer != null && timer.isTime()) {
                deactivate();
                timer.stop();
            }
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void makeSmoothnessAndroidStyle(PGraphics objectsFrame) {
        float alphaChangingVelocity = smothenessVelocity*Program.deltaTime;
        objectsFrame.loadPixels();
        int red, green, blue;
        int arrayLength = objectsFrame.width*objectsFrame.height;
        for (int i = 0; i < arrayLength; i++){
            int color = objectsFrame.pixels[i];
            int actualAlpha = (color >> 24) & 0xFF;
            if (actualAlpha > END_DISAPPEARING_ALPHA) actualAlpha-=(alphaChangingVelocity);
            if (actualAlpha <= END_DISAPPEARING_ALPHA){
                actualAlpha = END_DISAPPEARING_ALPHA;
                objectsFrame.pixels[i] = END_DISAPPEARING_ALPHA | 255 | 255 | 255;
                //objectsFrame.pixels[i] = objectsFrame.color(255, 0);
            }
            else {
                red = (color >> 16) & 0xFF;
                red-=50;
                if (red<0) red = 0;
                green = (color >> 8) & 0xFF;
                //green+=50;
                if (green>255) green = 255;
                blue = color & 0xFF;
                blue-=50;
                if (blue<0) blue = 0;
                objectsFrame.pixels[i] = actualAlpha | red | green | blue;
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
            }

        }
        objectsFrame.updatePixels();
    }

    /*
    public void makeSmoothnessAndroidStyle(PGraphics objectsFrame) {
        float alphaChangingVelocity = smothenessVelocity*Program.deltaTime;
        objectsFrame.loadPixels();
        int red, green, blue;
        int arrayLength = objectsFrame.width*objectsFrame.height;
        for (int i = 0; i < arrayLength; i++){
            int color = objectsFrame.pixels[i];
            int actualAlpha = (color >> 24) & 0xFF;
            if (actualAlpha > END_DISAPPEARING_ALPHA) actualAlpha-=(alphaChangingVelocity);
            if (actualAlpha <= END_DISAPPEARING_ALPHA){
                actualAlpha = END_DISAPPEARING_ALPHA;
                objectsFrame.pixels[i] = END_DISAPPEARING_ALPHA | 255 | 255 | 255;
                //objectsFrame.pixels[i] = objectsFrame.color(255, 0);
            }
            else {
                red = (color >> 16) & 0xFF;
                red-=50;
                if (red<0) red = 0;
                green = (color >> 8) & 0xFF;
                //green+=50;
                if (green<0) green = 0;
                blue = color & 0xFF;
                blue-=50;
                if (blue<0) blue = 0;
                objectsFrame.pixels[i] = actualAlpha | red | green | blue;
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
            }

        }
        objectsFrame.updatePixels();
    }
     */


    public void makeSmoothnessWindowsStyle(PGraphics objectsFrame) {
        float alphaChangingVelocity = smothenessVelocity*Program.deltaTime;
        objectsFrame.loadPixels();
        int red, green, blue;
        int arrayLength = objectsFrame.width*objectsFrame.height;
        for (int i = 0; i < arrayLength; i++){
            int color = objectsFrame.pixels[i];
            int actualAlpha = (color >> 24) & 0xFF;
            if (actualAlpha > END_DISAPPEARING_ALPHA) actualAlpha-=(alphaChangingVelocity);
            if (actualAlpha <= END_DISAPPEARING_ALPHA){
                //objectsFrame.pixels[i] = Program.engine.color(255, 0);
                objectsFrame.pixels[i] = objectsFrame.color(255, 0);
            }
            else {
                red = (color >> 16) & 0xFF;
                green = (color >> 8) & 0xFF;
                blue = color & 0xFF;
                if (useWhitening){

                }
                //objectsFrame.pixels[i] = Program.engine.color(red, green, blue, actualAlpha);

                objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
                //System.out.println("Color by normal: " + objectsFrame.pixels[i] + " but color by bit shifting: " + (actualAlpha | red | green | blue));
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
            }
        }
        objectsFrame.updatePixels();
    }

    /*
    public void makeSmoothnessWindowsStyle(PGraphics objectsFrame) {
        float alphaChangingVelocity = smothenessVelocity*Program.deltaTime;
        objectsFrame.loadPixels();
        int red, green, blue;
        int arrayLength = objectsFrame.width*objectsFrame.height;
        for (int i = 0; i < arrayLength; i++){
            int color = objectsFrame.pixels[i];
            int actualAlpha = (color >> 24) & 0xFF;
            if (actualAlpha > END_DISAPPEARING_ALPHA) actualAlpha-=(alphaChangingVelocity);
            if (actualAlpha <= END_DISAPPEARING_ALPHA){
                //
                if (Program.engine.mousePressed) objectsFrame.pixels[i] = objectsFrame.color(255, 0);
                else objectsFrame.pixels[i] = 0 | 255 | 255 | 255;
            }
            else {
                red = (color >> 16) & 0xFF;
                green = (color >> 8) & 0xFF;
                blue = color & 0xFF;
                if (Program.engine.mousePressed) objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
                else objectsFrame.pixels[i] = actualAlpha | red | green | blue;
                //objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
            }
        }
        objectsFrame.updatePixels();
    }
     */


    public void makeSmothenessDoesNotWorkAgain(PGraphics objectsFrame) {
        System.out.println("Delta " + Program.deltaTime);
        objectsFrame.loadPixels();
        int red, green, blue;
        int arrayLength = objectsFrame.width*objectsFrame.height;
        //int x = 250;
        //int y = 400;
        for (int i = 0; i < arrayLength; i++){
            int color = objectsFrame.pixels[i];
            float actualAlpha = (color >> 24) & 0xFF;
           /* boolean textDrawn = false;
            if (actualAlpha>END_DISAPPEARING_ALPHA) {
                System.out.print("Alpha was " + actualAlpha);
                textDrawn = true;
            }*/
            if (actualAlpha > END_DISAPPEARING_ALPHA) actualAlpha-=1;
            //System.out.println(actualAlpha);
            if (actualAlpha <= END_DISAPPEARING_ALPHA){
                objectsFrame.pixels[i] = objectsFrame.color(0, 0, 0);
            }
            else {
                red = (color >> 16) & 0xFF;
                green = (color >> 8) & 0xFF;
                blue = color & 0xFF;
                objectsFrame.pixels[i] = objectsFrame.color(red, green, blue, actualAlpha);
            }
        }
        objectsFrame.updatePixels();
    }


}
