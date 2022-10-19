package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;


//Dynamic bodies with spring joint are not tested
public class WallInFrontOfPersonDeterminingController {
    //private int distanceToDetermineWallBeforeObject = 5;
    private final PVector [] testPositionsInFrontOfPerson;
    private final PVector [] testPositionsUnderPerson;
    private final float distanceToTestPointAlongX, distanceToTestPointAlongY;
    //private final float testBodyHeight;

    private final Person person;
    private final boolean switchedOn = true;
    private float yStep, xStep;
    private float testBodyHeight, testBodyWidth;
    private boolean testingOnlyOnGround = false;
    private final float runningDistanceCoef = 1.5f;

    public WallInFrontOfPersonDeterminingController (int testPointsNumber,  float distanceToTestPointAlongX, float distanceToTestPointAlongY, Person person) {
        if (testPointsNumber < 1) testPointsNumber = 1;
        this.testPositionsInFrontOfPerson = new PVector[testPointsNumber];
        this.testPositionsUnderPerson = new PVector[3];
        this.distanceToTestPointAlongX = distanceToTestPointAlongX+person.getPersonWidth()/2f;
        this.distanceToTestPointAlongY = distanceToTestPointAlongY+person.getHeight()/2f;
        this.person = person;
        initTestPositions(person.getHeight(), person.getPersonWidth());
    }

    private void initTestPositions(float testBodyHeight, float testBodyWidth) {
        this.testBodyHeight = testBodyHeight;
        this.testBodyWidth = testBodyWidth;
        if (testPositionsInFrontOfPerson.length == 1) testPositionsInFrontOfPerson[0] = new PVector(person.getPixelPosition().x, person.getPixelPosition().y);
        else {
            yStep = testBodyHeight/(testPositionsInFrontOfPerson.length-1);
            float basicY = person.getPixelPosition().y-testBodyHeight/2;
            for (int i = 0; i < testPositionsInFrontOfPerson.length; i++) {
                testPositionsInFrontOfPerson[i] = new PVector(person.getPixelPosition().x, basicY+yStep*i);
            }
        }
        testPositionsUnderPerson[0] = new PVector(person.getPixelPosition().x-testBodyWidth/2, person.getPixelPosition().y+testBodyHeight/2);
        testPositionsUnderPerson[1] = new PVector(person.getPixelPosition().x, person.getPixelPosition().y+testBodyHeight/2);
        testPositionsUnderPerson[2] = new PVector(person.getPixelPosition().x+testBodyWidth/2, person.getPixelPosition().y+testBodyHeight/2);
    }

    public boolean areThereWallUnderPlayer(){
        updateActualTestPositionsUnderPerson();
        for (int i = 0; i < testPositionsUnderPerson.length; i++){
            //if (testingOnlyOnGround){
                for (int j = 0; j < testPositionsUnderPerson.length; j++) {
                    if (PhysicGameWorld.arePointInAnyBody(testPositionsUnderPerson[j])) {
                        //System.out.println( "Some object is under player" );
                        return true;
                    }

                }
                //System.out.println(" but not under player");
            //}
        }
        return false;
    }

    public boolean areThereWallInFrontOfPlayer(boolean direction, boolean movementArt){
        if (switchedOn) {
            updateActualTestPositionsInFrontOfPerson(direction, movementArt);
            for (int i = 0; i < testPositionsInFrontOfPerson.length; i++) {
                if (PhysicGameWorld.arePointInAnyBody(testPositionsInFrontOfPerson[i])) {
                    Body bodyInFrontOfPlayer = PhysicGameWorld.getBodyAtPoint(testPositionsInFrontOfPerson[i]);
                    if ( bodyInFrontOfPlayer.getType() == BodyType.STATIC ||  bodyInFrontOfPlayer.getType() == BodyType.KINEMATIC) {
                        if (Program.debug) {
                            System.out.print("Wall or platform is in front of player on side ");
                            if (direction == Person.TO_LEFT) System.out.println(" left ");
                            else System.out.println(" right ");
                        }
                        /*
                        if (testingOnlyOnGround){
                            for (int j = 0; j < testPositionsUnderPerson.length; j++) {
                                if (PhysicGameWorld.arePointInAnyBody(testPositionsUnderPerson[j])) {
                                    System.out.println( "and there are an object under player" );
                                    return true;
                                }

                            }
                            System.out.println(" but not under player");
                        }

                         */
                        //else {
                           // System.out.println(" ");
                            return true;
                        //}
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean areThereWallInFrontOfPlayer(boolean direction, GameRound gameRound, boolean movementArt){
        if (switchedOn) {
            updateActualTestPositionsInFrontOfPerson(direction, movementArt);

            for (int i = 0; i < testPositionsInFrontOfPerson.length; i++) {
                if (PhysicGameWorld.arePointInAnyBody(testPositionsInFrontOfPerson[i])) {
                    if (PhysicGameWorld.getBodyAtPoint(testPositionsInFrontOfPerson[i]).getType() == BodyType.STATIC || PhysicGameWorld.getBodyAtPoint(testPositionsInFrontOfPerson[i]).getType() == BodyType.KINEMATIC) {
                        if (Program.debug) {
                            //DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, new Vec2(testPositionsInFrontOfPerson[i].x, testPositionsInFrontOfPerson[i].y));
                            //gameRound.addDebugGraphic(debugGraphic);
                        }

                        if (Program.debug) {
                            System.out.print("Wall is in front of player on side ");
                            if (direction == false) System.out.print(" left ");
                        }
                        else System.out.print(" right ");
                        if (testingOnlyOnGround){
                            for (int j = 0; j < testPositionsUnderPerson.length; j++) {
                                if (PhysicGameWorld.arePointInAnyBody(testPositionsUnderPerson[j])) {
                                    System.out.println( "and there are an object under player" );
                                    return true;
                                }

                            }
                            System.out.println(" but not under player");
                        }
                        else {
                            System.out.println(" ");
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void updateActualTestPositionsInFrontOfPerson(boolean direction, boolean movementArt) {
        float basicX = person.getPixelPosition().x;
        if (direction == Person.TO_RIGHT) {
            for (int i = 0; i < testPositionsInFrontOfPerson.length; i++){
                if (movementArt == Person.RUN_MOVEMENT) testPositionsInFrontOfPerson[i].x = basicX+distanceToTestPointAlongX*runningDistanceCoef;
                else testPositionsInFrontOfPerson[i].x = basicX+distanceToTestPointAlongX;
                //System.out.println("Body width: " + testBodyWidth + " dist to test point: " + distanceToTestPointAlongX + "; Height:" + testBodyHeight)  ;
            }
        }
        else {
            for (int i = 0; i < testPositionsInFrontOfPerson.length; i++){
                if (movementArt == Person.RUN_MOVEMENT) testPositionsInFrontOfPerson[i].x = basicX-distanceToTestPointAlongX*runningDistanceCoef;
                else testPositionsInFrontOfPerson[i].x = basicX-distanceToTestPointAlongX;
            }
        }
        float basicY = person.getPixelPosition().y-testBodyHeight/2;
        for (int i = 0; i < testPositionsInFrontOfPerson.length; i++) {
            testPositionsInFrontOfPerson[i].y = basicY+yStep*i;
        }
        //next code is experimental
        testPositionsInFrontOfPerson[0].y-=4;
    }

    private void updateActualTestPositionsUnderPerson() {
        float basicY = person.getPixelPosition().y+testBodyHeight/2;

        testPositionsUnderPerson[0].y = basicY+distanceToTestPointAlongX;
        testPositionsUnderPerson[1].y = basicY+distanceToTestPointAlongY;
        testPositionsUnderPerson[2].y = basicY+distanceToTestPointAlongY;
        float basicX = person.getPixelPosition().x;
        testPositionsUnderPerson[0].x = basicX-testBodyWidth/2;
        testPositionsUnderPerson[1].x = basicX;
        testPositionsUnderPerson[2].x = basicX+testBodyWidth/2;
        /*
        for (int i = 0; i < testPositionsInFrontOfPerson.length; i++) {
            testPositionsInFrontOfPerson[i].y = basicY+yStep*i;
        }*/



    }
}
