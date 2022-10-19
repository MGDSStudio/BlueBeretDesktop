package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.splashes.JumpSplash;

public class SplashOnGroundAddingController {
    //private final float CRITICAL_VELOCITY_TO_SHOW_SPLASH = 0.005f;
    private final float CRITICAL_VELOCITY_TO_SHOW_SPLASH = 15.5f;
    public SplashOnGroundAddingController() {
    }

    public void update(GameRound gameRound){
        for (Person person : gameRound.getPersons()) {
            if (person.canPersonAddSplash()) {  //Not for dragonflies
                if (!person.isSleeped()) {
                    if (person.isLanded() ) {
                    //if (person.isLanded() && (person.getLandingVelocity() < CRITICAL_VELOCITY_TO_SHOW_SPLASH)) {
                        //if (person.getStatement() == Person.IN_AIR) {
                            //JumpSplash splash = new JumpSplash(person);
                            gameRound.addJumpSplash(person);
                        //}
                    }

                    //else System.out.println("Landing velocity: " + person.getLandingVelocity());
                }



            }
           /* if (person instanceof Soldier) {
                if (person.isLanded()) {
                    System.out.println("Person " + person.getClass() + " landed: " + person.isLanded() + " but velocity: " + person.getLandingVelocity());
                }
            }*/
        }


    }


}
