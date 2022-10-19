package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;

import java.util.ArrayList;

public class KickController {

    private Human attackingPerson;
    private HumanAnimationController playerAnimationController;
    private ArrayList <GameObject> attackedObjects;
    private ArrayList <Fixture> fixturesInZone;
    //private Timer kickingEndTimer;
    //private Timer toColisionTimer;
    //private final int attackFrameNumber;
    //private final int framesNumber;


    //statements
    public final static byte NO_ACTION = 0;
    public final static byte ANIMATION_STARTED = 1;
    public final static byte KICK = 2;
    public final static byte ENDED = 3;
    private byte statement = NO_ACTION;
    private HittingController hittingController;

    public KickController(Human attackingPerson, HumanAnimationController playerAnimationController, HittingController hittingController){
        this.attackingPerson = attackingPerson;
        this.playerAnimationController = playerAnimationController;
        attackedObjects = new ArrayList<>(2);
        fixturesInZone = new ArrayList<>(2);
        this.hittingController = hittingController;
    }

    public void update(GameRound gameRound){
        if (statement == ANIMATION_STARTED) {
            if (playerAnimationController.isKickMoment()){
                updateAttack(gameRound);
                if (attackedObjects.size()>0){
                    applyAttack(gameRound);
                }
                statement = KICK;
            }
        }
        else if (statement == KICK){
            //System.out.println("Kicking");
            if (playerAnimationController.isKickAnimationEnded()){
                statement = ENDED;
            }
        }
        else if (statement == ENDED) {
            System.out.println("Kick ended");
            attackingPerson.setKickEnded(true);
            statement = NO_ACTION;
        }
    }

    private void applyAttack(GameRound gameRound) {
        Vec2 attackValue = attackingPerson.getAttackValueForKick();
        //float attackingPersonAngle = attackingPerson.getWeaponAngle();
        System.out.println("Objects to be attacked: " + attackedObjects.size());
        boolean hittingMusicApplied = false;
        boolean killingMusicApplied = false;
        for (GameObject gameObject : attackedObjects){
            if (!gameObject.equals(attackingPerson)) {
                System.out.println("Impulse is set " + attackValue + " for " + gameObject.getClass());
                gameObject.body.setActive(true);
                gameObject.body.applyForceToCenter(PhysicGameWorld.controller.vectorPixelsToWorld(attackValue));
                gameObject.body.applyLinearImpulse(PhysicGameWorld.controller.vectorPixelsToWorld(attackValue), gameObject.body.getPosition(), true);
                gameObject.body.resetMassData();
                if (gameObject.canBeAttackedByKick()){
                    int attackPower = attackingPerson.getKickAttackPower();
                    if (gameObject instanceof Person) gameObject.attacked(attackPower);
                    else if (gameObject.getLife() <= 1) {
                        gameObject.attacked(attackPower);
                        if (gameObject instanceof RoundElement){
                            float kickAngle = attackingPerson.getKickAngleForActualStickOrientation(false);
                            if (hittingController != null){
                            }
                            else hittingController = gameRound.getHittingController();
                            hittingController.crushBrickInSegments(gameRound, (RoundElement) gameObject, new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y), kickAngle, true);

                            //crushBrickInSegments(gameRound, gameRound.roundElements.get(k), new Vec2(singleCollidings.get(j).getCollidingPlace().x, singleCollidings.get(j).getCollidingPlace().y), angle, ATTACK_FROM_EXPLOSION);
                        }
                        else {
                            gameObject.killBody();
                        }
                        System.out.println("Object is killed");
                    }
                    if (!gameObject.isAlive() && gameObject.hasAnyCollectableObjects()){
                        float releaseAngle = GameMechanics.getVectorAngleInRadians(attackValue);
                        gameRound.releaseGameObjects(gameObject, releaseAngle);
                        System.out.println("Objects were released after the kick ander angle: " + PApplet.degrees(releaseAngle));
                        killingMusicApplied = true;
                    }
                    else hittingMusicApplied = true;
                }

            }
            else {
                System.out.println("Only player is in zone");
            }
        }
        attackedObjects.clear();
        fixturesInZone.clear();
        if (killingMusicApplied) gameRound.getSoundController().setAndPlayAudio(SoundsInGame.ENEMY_HITTED_2);
        else gameRound.getSoundController().setAndPlayAudio(SoundsInGame.KICK);
    }

    private void updateAttack(GameRound gameRound) {
        determineAttackedObject(gameRound);
    }

    /*
    private ArrayList <GameObject> getAttackedObjects(){
        if (attackedObjects == null) {
            System.out.println("No object was attacked");
        }
        return attackedObjects;
    }*/

    private void determineAttackedObject(GameRound gameRound){
        if (attackedObjects.size()>0) attackedObjects.clear();
        Rectangular zone  = attackingPerson.getKickAttackZone();
        if (Program.OS == Program.DESKTOP) {
            DebugGraphic debugGraphic2 = new DebugGraphic(DebugGraphic.CROSS, new Vec2(zone.getCenter().x - zone.getWidth() / 2, zone.getCenter().y - zone.getHeight() / 2));
            DebugGraphic debugGraphic1 = new DebugGraphic(DebugGraphic.CROSS, new Vec2(zone.getCenter().x + zone.getWidth() / 2, zone.getCenter().y + zone.getHeight() / 2));
            gameRound.addDebugGraphic(debugGraphic1);
            gameRound.addDebugGraphic(debugGraphic2);
        }
        Vec2 rightUpperCorner = new Vec2((PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(zone.getRightLowerX(), zone.getRightLowerY()))).x, (PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(zone.getLeftUpperX(), zone.getLeftUpperY()))).y);
        Vec2 leftLowerCorner = new Vec2((PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(zone.getLeftUpperX(), zone.getLeftUpperY()))).x, (PhysicGameWorld.controller.coordPixelsToWorld(new Vec2(zone.getRightLowerX(), zone.getRightLowerY()))).y);
        System.out.println("Enemies in : " );
        for (Person person : gameRound.getPersons()){
            if (person.getClass() != Soldier.class){
                System.out.println("Enemy on: " + person.getPixelPosition());
            }
        }
        AABB aabb;
        System.out.println("Sight direction: " + attackingPerson.getSightDirection()  +"Orientation: " + attackingPerson.orientation );
        if (attackingPerson.orientation == Person.TO_RIGHT) aabb = new AABB(leftLowerCorner, rightUpperCorner);
        else aabb = new AABB(leftLowerCorner, rightUpperCorner);
        //ArrayList<Fixture> detectedFixtures = new ArrayList<>();
        QueryCallback callback = new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                GameObject object  = PhysicGameWorld.getGameObjectByBody(gameRound, fixture.getBody());
                boolean alreadyExists = false;
                for (GameObject attackedObjects : attackedObjects){
                    if (attackedObjects.equals(object)){
                        alreadyExists = true;
                    }
                }
                if (!alreadyExists) {
                    attackedObjects.add(object);
                    System.out.println("Kicked object was added to kicking list " + object.getClass());
                }
                else {
                    System.out.println("Kicked object was already added to kicking list and maust not be added again" + object.getClass());
                }
                return true;
            }
        };
        if (!PhysicGameWorld.controller.world.isLocked()) {
            if (callback != null && aabb != null) {
                try {
                    PhysicGameWorld.controller.world.queryAABB(callback, aabb);
                }
                catch (Exception e){

                }
            }

        }
        else{
            System.out.println("World is locked");
        }

    }




    public boolean canBeKickMade(){
        if (statement == NO_ACTION) {
            if (!attackingPerson.isKicking()) {
                if (attackingPerson.getStatement() != Person.IN_AIR) {
                    if (!attackingPerson.getActualWeapon().isReloading()) {
                        System.out.println("Start kicking");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void makeKick(){
        if (statement == NO_ACTION ){
            statement = ANIMATION_STARTED;
            attackingPerson.setKicking(true);
            System.out.println("Started to kick; Statement: " + statement);
        }
        else {
            System.out.println("Can not kick; Statement: " + statement);
        }
    }

}
