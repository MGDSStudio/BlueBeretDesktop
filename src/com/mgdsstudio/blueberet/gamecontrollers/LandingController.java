package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;

import java.util.ArrayList;

public class LandingController {
    private Person attackingPerson, hittedPerson;
    private byte prevStatement = Person.ON_GROUND;
    private float prevVelocityY;
    public final float maxAccelerate = -1200f;
    public final static float CRITICAL_DELTA_ACCELERATE_FOR_ATTACK = -800;
    //public final static float CRITICAL_DELTA_ACCELERATE_FOR_DUST = -400;
    private boolean fallingOnEnemy;
    private boolean fallingOnObjectStaticObject;
    private final float criticalDeltaAccelerate;
    private int attackValueInPercent;



    //private final float criticalAccelerate;

    //Mutable
    private Body mutB1;
    private Body mutB2;
    private Fixture attackedFixture;

    public LandingController(Person attackingPerson, float criticalAccelerate){
        this.attackingPerson = attackingPerson;
        if (criticalAccelerate > 0) criticalAccelerate*=-1;
        //this.criticalAccelerate = criticalAccelerate;
        this.criticalDeltaAccelerate = criticalAccelerate;
    }

    public boolean isFallingOnEnemy() {
        return fallingOnEnemy;
    }

    public boolean isFallingOnObjectStaticObject() {
        return fallingOnObjectStaticObject;
    }

    public void update(GameRound gameRound){
        if (prevStatement == Person.IN_AIR){
            float actualVelocity = PhysicGameWorld.controller.scalarWorldToPixels(attackingPerson.body.getLinearVelocity().y);
            float deltaVelocity = prevVelocityY-actualVelocity;
            if (prevVelocityY <0 && deltaVelocity < criticalDeltaAccelerate) {
                updateCollidingWithObjects(gameRound.getPersons());
                updateAttackValueInPercent(deltaVelocity);
                if (fallingOnEnemy) System.out.println("Velocity is: " + actualVelocity + "; was: " + prevVelocityY + "; Delta: " + deltaVelocity);
            }
            else resetStatement();
            prevVelocityY = actualVelocity;
        }
        else {
            updateAccelerate();
            resetStatement();
        }
        prevStatement = attackingPerson.getStatement();
    }



    private void resetStatement() {
        fallingOnEnemy = false;
        fallingOnObjectStaticObject = false;
        if (hittedPerson != null) hittedPerson = null;
    }

    private void updateCollidingWithObjects(ArrayList <Person> persons) {
        //System.out.println("Try to find in contacts with size: " + PhysicGameWorld.beginContacts.size());
        fallingOnEnemy = false;
        for (Contact contact : PhysicGameWorld.beginContacts){
            mutB1 = contact.getFixtureA().getBody();
            mutB2 = contact.getFixtureB().getBody();
            if (mutB1 != null && mutB2 != null) {
                if (mutB1.equals(attackingPerson.body)) {
                    for (Person person : persons) {
                        if (person.body.equals(mutB2)) {
                            hittedPerson = person;
                            fallingOnEnemy = true;
                            fallingOnObjectStaticObject = false;
                            attackedFixture = contact.getFixtureB();
                            if (Program.debug) System.out.println(" *** Player falls on enemy with accelerate " + prevVelocityY);
                            break;
                        }
                    }
                    if (!fallingOnEnemy) {
                        if (mutB2.getType() == BodyType.KINEMATIC || mutB2.getType() == BodyType.STATIC) {
                            fallingOnObjectStaticObject = true;
                        }
                    }
                } else if (mutB2.equals(attackingPerson.body)) {
                    for (Person person : persons) {
                        if (person.body.equals(mutB1)) {
                            hittedPerson = person;
                            fallingOnEnemy = true;
                            fallingOnObjectStaticObject = false;
                            attackedFixture = contact.getFixtureA();
                            if (Program.debug) System.out.println(" *** Player falls on enemy with accelerate " + prevVelocityY);
                            break;
                        }
                    }
                    if (!fallingOnEnemy) {
                        if (mutB1.getType() == BodyType.KINEMATIC || mutB1.getType() == BodyType.STATIC) {
                            fallingOnObjectStaticObject = true;
                        }
                    }
                }
            }
            else {
                System.out.println("Some of colliding bodies is null");
            }
        }
        /*if (!fallingOnEnemy){
            fallingOnObject = true;
            System.out.println("Player falls on an object");
        }*/
    }
/*
    private void updateCollidingWithObjects(ArrayList <Person> persons) {
        System.out.println("Try to find in contacts with size: " + PhysicGameWorld.beginContacts.size());
        for (Contact contact : PhysicGameWorld.beginContacts){
            Fixture f1 = contact.getFixtureA();
            Fixture f2 = contact.getFixtureB();
            Body b1 = f1.getBody();
            Body b2 = f2.getBody();
            fallingOnObject = true;
        }
    }*/

    private void updateSecondVersion(GameRound gameRound){
        /*
        if (prevStatement == Person.IN_AIR){
            //System.out.println("Player is flying with accelerate " + yAccelerate);
            float actualAccelerate = PhysicGameWorld.controller.scalarWorldToPixels(attackingPerson.body.getLinearVelocity().y);
            if (prevVelocityY <0 && actualAccelerate>0 && prevVelocityY <criticalAccelerate) {
                updateCollidingWithPersons(gameRound.getPersons());
                if (fallingOnEnemy) System.out.println(" Accelerate was: " + actualAccelerate + "; prev: " + prevVelocityY);
            }
            else resetStatement();
            prevVelocityY = actualAccelerate;
        }
        else {
            updateAccelerate();
            resetStatement();
        }
        //System.out.print("Statement: " + gameRound.getPlayer().getStatement() + "; Prev: " + prevStatement);
        prevStatement = attackingPerson.getStatement();
        */
    }

    private void updateFirstVersion(){
        /*
        if (attackingPerson.getStatement() == Person.ON_GROUND){
            if (prevStatement == Person.IN_AIR){
                //updateAccelerate();
                //System.out.println("Player falls accelerate " + yAccelerate);
                if (prevVelocityY >criticalAccelerate){
                    //for (PhysicGameWorld.)
                    System.out.println("Player falls on enemy with accelerate " + prevVelocityY);
                }
            }
        }
        updateAccelerate();
        //System.out.print("Statement: " + gameRound.getPlayer().getStatement() + "; Prev: " + prevStatement);
        prevStatement = attackingPerson.getStatement();

         */
    }

    private void updateAccelerate(){
        try {
            prevVelocityY = PhysicGameWorld.controller.scalarWorldToPixels(attackingPerson.body.getLinearVelocity().y);
        }
        catch (Exception e){
            prevVelocityY = 0;
        }
    }

    public void makeReboundJump() {
        attackingPerson.addReboundJump();
    }

    public Person getAttackedPerson() {
        return hittedPerson;
    }

    private void updateAttackValueInPercent(float deltaVelocity) {
        attackValueInPercent = (int)(100f*(PApplet.abs(deltaVelocity)-PApplet.abs(CRITICAL_DELTA_ACCELERATE_FOR_ATTACK))/(PApplet.abs(maxAccelerate)-PApplet.abs(CRITICAL_DELTA_ACCELERATE_FOR_ATTACK)));
        System.out.println("Value in percent: " + attackValueInPercent + " for delta: " + deltaVelocity);
    }

    public int getAttackValueInPercent() {
            return attackValueInPercent;
    }

    public Fixture getAttackedFixture() {
        return attackedFixture;
    }
}
