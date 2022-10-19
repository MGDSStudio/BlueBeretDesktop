package com.mgdsstudio.blueberet.gameobjects.persons.flower;

import com.mgdsstudio.blueberet.classestoberemoved.Flower;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class PlantController {
    private final static int CRITICAL_ANGLE_TO_STOP_ROTATION = 88;
    // bite data
    private final static float NORMAL_BITE_SPEED = -PConstants.PI*2.5f;
    private final static float BITE_SPEED_BY_DYING = NORMAL_BITE_SPEED*2.5f;
    private final static float NORMAL_BITE_TORQUE = 1500;
    private final static float BITE_TORQUE_BY_DYING = NORMAL_BITE_TORQUE*2.5f;
    private float biteSpeed = NORMAL_BITE_SPEED;
    private float biteTorque = NORMAL_BITE_TORQUE;

    // Critical angles
    private float MAX_RIGHT_ANGLE;
    private float MIN_RIGHT_ANGLE;
    private float MAX_LEFT_ANGLE;
    private float MIN_LEFT_ANGLE;
    //private final static boolean MIN = false;
    //private final static boolean MAX = true;

    //behaviour
    public static final byte NO_FLOWER = -1;
    public static final byte NO_ACTIVE = 0;
    public static final byte STAY_AND_BITE = 1;
    public static final byte UP_BITE_DOWN = 2;
    public static final byte UP_UNDER_PLAYER = 3;
    public static final byte RISEN_UP_AND_STAY = 5;
    public static final byte FOLLOW_PLAYER = 4;
    private byte behaviourModel;

    private RoundPipe roundPipe;
    private Jaw leftJaw, rightJaw;
    private RevoluteJoint leftJawToRod, rightJawToRod;
    private Rod rod;
    private Flag movementZone;
    private ArrayList <Body> bodies;
    private ArrayList <Plant> plantParts;
    private int basicAngle;
    public static byte LEFT_JAW = 1, RIGHT_JAW = 2, ROD = 3;

    private boolean dead;
    private boolean paused;

    // Stages
    public static final byte JAW_CLOSE = 1;
    public static final byte JAW_OPEN = 2;
    private byte stage = JAW_CLOSE;
    //Movement variables
    private boolean directionWasChanged = true;
    final static private boolean MOVEMENT_TO_GOAL_PLACE = false;
    final static private boolean MOVEMENT_TO_BASE_PLACE = true;
    final static private float NORMAL_MOVEMENT_VELOCITY = 7;
    private boolean movementStatement = MOVEMENT_TO_GOAL_PLACE;
    private float movementVelocity = NORMAL_MOVEMENT_VELOCITY;


    //killing statements
    private boolean leftJointAlive = true;
    private boolean rightJointAlive = true;
    private boolean rodAlive = true;

    private Timer timerForChangingDirection;
    private final static int TIME_TO_CHANGE_DIRECTION = 300;

    private final static float JAWS_LIFE_COEFFICIENT = 0.7f;

    private final static int MINIMAL_PIPE_DIAMETER_FOR_PLANT = 99;
    public final static int MINIMAL_PIPE_HEIGHT_FOR_PLANT = 99;

    public PlantController(RoundPipe roundPipe, Vec2 position, int life, byte behaviourModel, int pipeInsideDiameter, int basicAngle, int flowerDiameter){
        //After position must be got from roundPipe.getPos()
        System.out.println("diameter at first: " + pipeInsideDiameter + " Angle: " + basicAngle);
        this.roundPipe = roundPipe;
        this.basicAngle = basicAngle;
        calculateCriticalJawAngles();
        int pipeHeight = 100;

        int jawDiameter = getJawDiameter(flowerDiameter);
        int rodHeight = getRodHeight(pipeHeight);
        //public Jaw(Vec2 position, int life, boolean side, int pipeInsideDiameter, int basicAngle, int pipeHeight){
        int jawLife = (int)(life*JAWS_LIFE_COEFFICIENT);
        if (jawLife <1) jawLife = 1;
        leftJaw = new Jaw(this, position,jawLife, Plant.LEFT, pipeInsideDiameter,  basicAngle, rodHeight, jawDiameter);
        rightJaw = new Jaw(this, position,jawLife, Plant.RIGHT, pipeInsideDiameter,  basicAngle, rodHeight, jawDiameter);
        rod = new Rod(this, position, pipeInsideDiameter, life, basicAngle, rodHeight);
        this.behaviourModel = behaviourModel;
        createKinematic(jawDiameter);
        //setFilterData(GameObject.COAlISION_FLOWER_WITH_PIPE_GROUP);
        if (behaviourModel == UP_BITE_DOWN || behaviourModel == UP_UNDER_PLAYER || behaviourModel == STAY_AND_BITE) {
            //if (behaviourModel == UP_BITE_DOWN || behaviourModel == UP_UNDER_PLAYER) {
                setStartVelocity();
                timerForChangingDirection = new Timer(TIME_TO_CHANGE_DIRECTION);
                System.out.println("Start movement statement: " + movementStatement);
            //}
        }
        fillBodies();
        createMovementZone(position, pipeHeight, (int) roundPipe.getWidth());
        setJointedBodiesForParts();
    }

    public static int getMaximalFLowerDiameter(int pipeOutsideDiameter) {
        return (int)(pipeOutsideDiameter-(2.2f*RoundPipe.WALL_THICKNESS));
    }

    public static int getMinimalFLowerDiameter() {
        return getMaximalFLowerDiameter(MINIMAL_PIPE_DIAMETER_FOR_PLANT);
    }

    private void createMovementZone(Vec2 position, int pipeHeight, int pipeOutsideDiameter){
        //PVector enterToPipePos = new PVector(position.x+(pipeOutsideDiameter/2)* PApplet.cos(PApplet.radians(basicAngle)), position.y+(pipeHeight/2)*PApplet.sin(PApplet.radians(basicAngle)));
        //
        PVector enterToPipePos = new PVector(position.x+(pipeHeight/1.5f)* PApplet.cos(PApplet.radians(basicAngle)), position.y+(pipeOutsideDiameter/1.5f)*PApplet.sin(PApplet.radians(basicAngle)));
        //PVector enterToPipePos = new PVector(position.x+(pipeHeight/2)* PApplet.cos(PApplet.radians(basicAngle)), position.y+(pipeHeight/2)*PApplet.sin(PApplet.radians(basicAngle)));
        PVector dimensions = new PVector(pipeOutsideDiameter, leftJaw.getJawDiameter()*2.5f);
        PVector newDimensions = dimensions.rotate(PApplet.radians(270-basicAngle));
        movementZone = new Flag(enterToPipePos, PApplet.abs(newDimensions.x), PApplet.abs(newDimensions.y), Flag.MOVEMENT_AREA_ZONE);
        //movementZone = new Flag(enterToPipePos, pipeInsideDiameter, leftJaw.getJawDiameter()*2.5f, Flag.MOVEMENT_AREA_ZONE);
        movementZone.setFrameColor(Program.engine.color(220,5,5));
    }



    private void createKinematic(int jawDiameter){
        RevoluteJointDef rjd1 = new RevoluteJointDef();
        RevoluteJointDef rjd2 = new RevoluteJointDef();
        createJoints(rjd1, rjd2, jawDiameter);
        makeRotating(rjd1, rjd2);

    }

    private int getJawDiameter(int pipeDiameter){
        int diameter = (int) (pipeDiameter);
        //if (pipeDiameter > 150) diameter = 150;
        if (pipeDiameter < MINIMAL_PIPE_DIAMETER_FOR_PLANT) diameter = MINIMAL_PIPE_DIAMETER_FOR_PLANT;
        diameter/=2.0f;
        return diameter;
    }

    private int getRodHeight(int pipeHeight){
        return (int)(MINIMAL_PIPE_HEIGHT_FOR_PLANT*1.5f);
        /*
        int height = (int)(pipeHeight-20);
        if (height < 50) height = 50;
        else if (height > )
        */
    }

    private void createJoints(RevoluteJointDef rjd1, RevoluteJointDef rjd2, int jawDiameter){
        try {
            float distanceToOffcetX = 0;
            float distanceToOffcetY = - rod.getHeight()/2f;
            if (basicAngle == 90 ){
                distanceToOffcetX = 0;
                distanceToOffcetY = +rod.getHeight()/2f;
            }
            if (basicAngle == 180) {
                distanceToOffcetX = -rod.getHeight()/2f;
                distanceToOffcetY = 0;
            }
            if (basicAngle == 0) {
                distanceToOffcetX = +rod.getHeight()/2f;
                distanceToOffcetY = 0;
            }
            Vec2 offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(distanceToOffcetX, distanceToOffcetY));
            rjd1.initialize(leftJaw.body, rod.body, rod.body.getWorldCenter().add(offset));
            rjd2.initialize(rightJaw.body, rod.body, rod.body.getWorldCenter().add(offset));
        }
        catch (Exception e) {
            System.out.println("Can not initialize joints.  " + e);
        }
    }

    private void makeRotating(RevoluteJointDef rjd1, RevoluteJointDef rjd2) {

        rjd1.motorSpeed = biteSpeed;       // how fast?
        rjd1.maxMotorTorque = biteTorque; // how powerful?
        rjd1.enableMotor = true;      // is it on?
        rjd2.motorSpeed = -biteSpeed;       // how fast?
        rjd2.maxMotorTorque = biteTorque; // how powerful?
        rjd2.enableMotor = true;      // is it on?

        leftJawToRod = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd1);
        rightJawToRod = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd2);
        leftJawToRod.enableLimit(true);
        rightJawToRod.enableLimit(true);
        leftJawToRod.setLimits(MIN_RIGHT_ANGLE, MAX_RIGHT_ANGLE);
        rightJawToRod.setLimits(MIN_LEFT_ANGLE, MAX_LEFT_ANGLE);
        leftJaw.body.setTransform(leftJaw.body.getPosition(), Program.engine.radians(150));
        rightJaw.body.setTransform(rightJaw.body.getPosition(), Program.engine.radians(-150));
    }

    private void setFilterData(byte group) {
        Filter filter = new Filter();
        filter.groupIndex = group;
        for (Fixture fixture = leftJaw.body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
            fixture.setFilterData(filter);
        }
        for (Fixture fixture = rightJaw.body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
            fixture.setFilterData(filter);
        }
        for (Fixture fixture = rod.body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
            fixture.setFilterData(filter);
        }
    }

    private void fillBodies() {
        bodies = new ArrayList<>();
        try {
            bodies.add(leftJaw.body);
            bodies.add(rightJaw.body);
            bodies.add(rod.body);
        }
        catch ( Exception e){
        }
        try {
            plantParts = new ArrayList<>();
            plantParts.add(leftJaw);
            plantParts.add(rightJaw);
            plantParts.add(rod);
        }
        catch ( Exception e){

        }
    }

    private void setJointedBodiesForParts(){

    }

    public void draw(GameCamera gameCamera){
        movementZone.draw(gameCamera);
/*
        rod.draw(gameCamera);
        leftJaw.draw(gameCamera);
        rightJaw.draw(gameCamera);*/
    }

    private Plant getPlantByBody(Body body) {
        if (leftJaw.body.equals(body)) return leftJaw;
        else if (rightJaw.body.equals(body)) return rightJaw;
        else if (rod.body.equals(body)) return rod;
        else {
            System.out.println("This body is not a part of some plant part");
            return null;
        }
    }

    public ArrayList <Body> getBodies(){
        for (int i = (bodies.size()-1); i >= 0; i--){
            Plant plantPart = getPlantByBody(bodies.get(i));
            boolean alive = plantPart.isAlive();
            if (bodies.get(i) == null || !alive){
                bodies.remove(i);
            }
        }


        /*
        bodies.add(leftJaw.body);
                bodies.add(rightJaw.body);
                bodies.add(rod.body);
        */


        return bodies;
    }

    public ArrayList <Plant> getPlants(){
        return plantParts;
    }



    public Plant getFlowerPart(byte type){
        if (type == LEFT_JAW) return leftJaw;
        else if (type == RIGHT_JAW) return rightJaw;
        else if (type == ROD) return rod;
        else return null;
    }

    private void calculateCriticalJawAngles() {
        //System.out.println("Basic Angle: " + basicAngle);
        int additionalValue = 10;
        if (basicAngle == 270 || basicAngle == 90) {
            MAX_RIGHT_ANGLE = Program.engine.radians(0);
            MIN_RIGHT_ANGLE = Program.engine.radians(-CRITICAL_ANGLE_TO_STOP_ROTATION-additionalValue);
            MAX_LEFT_ANGLE = Program.engine.radians(CRITICAL_ANGLE_TO_STOP_ROTATION+additionalValue);
            MIN_LEFT_ANGLE = Program.engine.radians(0);
        }
        if (basicAngle == 0 || basicAngle == 180) {
            float rotationValue = 0;
            MAX_RIGHT_ANGLE = Program.engine.radians(0+rotationValue);
            MIN_RIGHT_ANGLE = Program.engine.radians(-CRITICAL_ANGLE_TO_STOP_ROTATION-additionalValue+rotationValue);
            MAX_LEFT_ANGLE = Program.engine.radians(CRITICAL_ANGLE_TO_STOP_ROTATION+additionalValue+rotationValue);
            MIN_LEFT_ANGLE = Program.engine.radians(0+rotationValue);
        }

    }

    void reverseJawsOpeningDirection() {
        if (leftJawToRod != null && leftJawToRod.getMotorSpeed()>0) {
            leftJawToRod.setMotorSpeed(biteSpeed);
        }
        else if (leftJawToRod != null && leftJawToRod.getMotorSpeed()<0) {
            leftJawToRod.setMotorSpeed(-biteSpeed);
        }
        if (rightJawToRod != null && rightJawToRod.getMotorSpeed()>0) {
            rightJawToRod.setMotorSpeed(biteSpeed);
        }
        else if (rightJawToRod != null && rightJawToRod.getMotorSpeed()<0) {
            rightJawToRod.setMotorSpeed(-biteSpeed);
        }
    }


    public void update(GameRound gameRound) {
        //System.out.println("Stage: " + stage);

        if (!dead) {
            updateKilling(gameRound.getPersons());
            if (leftJawToRod!= null && rightJawToRod!=null) {
                if (leftJaw.body.getAngle()>=MAX_LEFT_ANGLE && rightJaw.body.getAngle()<=MIN_RIGHT_ANGLE	&& stage == JAW_CLOSE){
                    //System.out.print("Stage was: " + stage );
                    stage = JAW_OPEN;
                    reverseJawsOpeningDirection();
                    //System.out.println(" is now " + stage +  "; Angle for left: " + PApplet.degrees(leftJaw.body.getAngle())+ "; Angle for right: " + PApplet.degrees(rightJaw.body.getAngle()));
                }
                else if (leftJaw.body.getAngle()<=MIN_LEFT_ANGLE &&	rightJaw.body.getAngle()>=MAX_RIGHT_ANGLE &&	stage == JAW_OPEN){
                    stage = JAW_CLOSE;
                    reverseJawsOpeningDirection();
                }
            }
            else {
                if (leftJawToRod == null) {
                    if (rightJaw.body.getAngle() <= MAX_RIGHT_ANGLE && stage == JAW_CLOSE){
                        stage = JAW_OPEN;
                        reverseJawsOpeningDirection();
                    }
                    else if (rightJaw.body.getAngle()>=MIN_RIGHT_ANGLE && stage == JAW_OPEN){
                        stage = JAW_CLOSE;
                        reverseJawsOpeningDirection();
                    }
                }
                else if (rightJawToRod == null){
                    if (rightJaw.body.getAngle() >= MAX_LEFT_ANGLE && stage == JAW_CLOSE){
                        stage = JAW_OPEN;
                        reverseJawsOpeningDirection();
                    }
                    else if (leftJaw.body.getAngle()<=MIN_LEFT_ANGLE && stage == JAW_OPEN){
                        stage = JAW_CLOSE;
                        reverseJawsOpeningDirection();
                    }
                }
            }

            if (behaviourModel == UP_BITE_DOWN || behaviourModel == UP_UNDER_PLAYER ) {
                updateUpDownMovement();
                //updateMovingAndStopping();
            }
            if (behaviourModel == STAY_AND_BITE){
                upperPositionGained();
            }

            /*
            if (Game2D.engine.frameCount % GameRound.UPDATE_FREQUENCY_FOR_SECONDARY_OBJECTS == 0) {
                updateBitedObjects(gameRound);
            }*/
        }
    }

    private void updateMovingAndStopping(){
        boolean someBodyIsSleeped = false;
        if (leftJawToRod != null){
            if ((leftJaw.isAlive() && leftJaw.isSleeped())){
                someBodyIsSleeped = true;
            }
        }
        if (!someBodyIsSleeped && rightJawToRod != null){
            if ((rightJaw.isAlive() && rightJaw.isSleeped())){
                someBodyIsSleeped = true;
            }
        }
        if (!someBodyIsSleeped) {
            if (paused) startMovementAgain();
            updateUpDownMovement();
        }
        else {
            if (!paused) pauseMovement();
        }
    }

    private void startMovementAgain(){
        setStartVelocity();
        paused = false;
    }

    private void pauseMovement(){
        rod.body.setLinearVelocity(new Vec2(0,0));
        paused = true;
    }

    private void upperPositionGained(){
        //if (timerForChangingDirection.isTime()) {
            if ((leftJawToRod != null && !movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(leftJaw.body))) ||
                    (rightJawToRod != null && !movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(rightJaw.body)))) {
                System.out.println("Body can be stopped");
                behaviourModel = RISEN_UP_AND_STAY;
                timerForChangingDirection = null;
                stopUpDownMovement();
            }
        //}
    }

    private void updateUpDownMovement() {
        /*
        System.out.print("Statement:" );
        if (movementStatement == MOVEMENT_TO_GOAL_PLACE) System.out.println(" TO_GOAL");
        else System.out.println(" TO_BASE");
        */
        //if (leftJawToRod == null) System.out.println("Jaw joint is null");
        //if (rightJawToRod == null) System.out.println("RJaw joint is null");
        //System.out.println("Pos: " + movementZone.getPosition() + "; WxH" + movementZone.getWidth() + "x" + movementZone.getHeight());
        if (timerForChangingDirection.isTime()){
            if ((leftJawToRod != null && movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(leftJaw.body))) ||
                    (rightJawToRod != null && movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(rightJaw.body)))) {
                //if (movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(jaw1)) || movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(jaw2))){
                //System.out.println("Moving");
                directionWasChanged = false;

                //System.out.println("One or two jaws are in zone");
            } else {
                if (leftJawToRod == null && rightJawToRod == null) {
                    if (movementZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(rod.body))) {
                        System.out.println("Only one stab in zone");
                        directionWasChanged = false;
                        if (rod.body != null) rod.body.setType(BodyType.DYNAMIC);
                        if (leftJaw.body != null && leftJaw.body.getType() != BodyType.DYNAMIC)
                            leftJaw.body.setType(BodyType.DYNAMIC);
                        if (rightJaw.body != null && rightJaw.body.getType() != BodyType.DYNAMIC)
                            rightJaw.body.setType(BodyType.DYNAMIC);
                    }
                }
                if (!directionWasChanged) {


                        setVelocity();
                        directionWasChanged = true;

                }
            }
         }

    }

    private void setStartVelocity(){
        float nominalVelocity = 25*movementVelocity;
        float velocityX = nominalVelocity*PApplet.cos(PApplet.radians(basicAngle));
        float velocityY = nominalVelocity*PApplet.sin(PApplet.radians(basicAngle));
        System.out.println("Start velocity: "  + (int)velocityX + "x" + (int)velocityY + ": Basic angle: " + basicAngle);
        rod.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocityX, velocityY));
        if (MOVEMENT_TO_GOAL_PLACE == movementStatement) movementStatement = MOVEMENT_TO_BASE_PLACE;
        else movementStatement = MOVEMENT_TO_GOAL_PLACE;
        //System.out.println("Velocity changing: " + movementStatement + "; x: " +  velocityX  + "; y:" + velocityY);
    }

    private void stopUpDownMovement(){
        rod.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(0, 0));
    }

    private void setVelocity(){
        float velocityX = getVelocity(movementStatement)*PApplet.cos(PApplet.radians(basicAngle));
        float velocityY = getVelocity(movementStatement)*PApplet.sin(PApplet.radians(basicAngle));
        //System.out.println("Start velocity: "  + (int)velocityX + "x" + (int)velocityY);
        rod.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(velocityX, velocityY));
        if (MOVEMENT_TO_GOAL_PLACE == movementStatement) movementStatement = MOVEMENT_TO_BASE_PLACE;
        else movementStatement = MOVEMENT_TO_GOAL_PLACE;
        timerForChangingDirection = new Timer(TIME_TO_CHANGE_DIRECTION);
        //System.out.println("Velocity changing: " + movementStatement + "; x: " +  velocityX  + "; y:" + velocityY);
    }

    private float getVelocity(Boolean movementStatement){
        //System.out.println("Delta time: " + Game2D.deltaTime);
        if (Program.deltaTime > 9999 || Program.deltaTime < 1) {
            if (MOVEMENT_TO_GOAL_PLACE == movementStatement) return	-33*movementVelocity;
            else return 33*movementVelocity;
        }
        else {
            if (MOVEMENT_TO_BASE_PLACE == movementStatement) return -Program.deltaTime*movementVelocity;
            else return Program.deltaTime*movementVelocity;
        }
    }


    private void killPlantPart(byte part){
        if (part == LEFT_JAW){
            if (leftJawToRod.isActive()) {
                PhysicGameWorld.controller.world.destroyJoint(leftJawToRod);
                leftJawToRod = null;
                System.out.println("Left side is dead");
            }
            leftJointAlive = false;
            leftJaw.body.setGravityScale(1);
            leftJaw.body.resetMassData();
            leftJaw.body.setType(BodyType.DYNAMIC);
            resetMassDataAfterDeath(leftJaw.body);
        }
        else if (part == RIGHT_JAW){
            if (rightJawToRod.isActive()) {
                PhysicGameWorld.controller.world.destroyJoint(rightJawToRod);
                rightJawToRod = null;
                System.out.println("Right side is dead");
            }
            rightJointAlive = false;
            rightJaw.body.setGravityScale(1);
            rightJaw.body.resetMassData();
            rightJaw.body.setType(BodyType.DYNAMIC);
            resetMassDataAfterDeath(rightJaw.body);
        }
        else if (part == ROD){
            if (rightJointAlive) {
                if (rightJawToRod.isActive()) {
                    PhysicGameWorld.controller.world.destroyJoint(rightJawToRod);
                    rightJawToRod = null;
                    rightJaw.kill();
                    System.out.println("Right side is dead after death of the rod");
                    rightJointAlive = false;
                    rightJaw.body.setGravityScale(1);
                    resetMassDataAfterDeath(rightJaw.body);
                }
            }
            if (leftJointAlive){
                if (leftJawToRod.isActive()) {
                    PhysicGameWorld.controller.world.destroyJoint(leftJawToRod);
                    leftJawToRod = null;
                    leftJaw.kill();
                    System.out.println("Left side is dead after death of the rod");
                    leftJointAlive = false;
                    leftJaw.body.setGravityScale(1);
                    resetMassDataAfterDeath(leftJaw.body);
                }
            }
            rod.body.setType(BodyType.DYNAMIC);
            resetMassDataAfterDeath(rod.body);
            rodAlive = false;
        }
        if ((!leftJointAlive && !rightJointAlive && !dead)) dead = true;
    }

    private void updateKilling(ArrayList <Person> persons){
        if (rodAlive){
            if (rod.isDead()){
                killPlantPart(ROD);
            }
        }
        if (leftJointAlive) {
            if (leftJaw.isDead()) {
                killPlantPart(LEFT_JAW);
            }
        }
        if (rightJointAlive) {
            if (rightJaw.isDead()) {
                killPlantPart(RIGHT_JAW);
            }
        }
        if (dead) {
            releaseParts();
            System.out.println("Complete dead. Joints in the world: " + PhysicGameWorld.controller.world.getJointCount());
        }
        updateKilledParts(persons);
        //rod.body.setType(BodyType.STATIC);
    }

    private void plantPartsDeletingTest(Plant part, ArrayList <Person> persons){
        if (part.isDead() && !part.body.isActive()) {
            for (int i = (persons.size()-1); i>= 0; i--){
                if (part.equals(persons.get(i))) {
                    persons.remove(part);
                    System.out.println(part.getClassName() + " Removed from persons array");
                    if (part.body != null) {
                        part.body.setActive(false);
                        PhysicGameWorld.controller.world.destroyBody(part.body);
                    }
                }
            }
        }
    }

    private void updateKilledParts(ArrayList <Person> persons) {
        plantPartsDeletingTest(leftJaw, persons);
        plantPartsDeletingTest(rightJaw, persons);
        plantPartsDeletingTest(rod, persons);
    }

    private void releaseParts(){
        if (leftJawToRod != null){
            PhysicGameWorld.controller.world.destroyJoint(leftJawToRod);
            leftJawToRod = null;
            System.out.println("Joint deleted");
        }
        if (rightJawToRod != null){
            PhysicGameWorld.controller.world.destroyJoint(rightJawToRod);
            rightJawToRod = null;
            System.out.println("Joint deleted");
        }
        if (rod.body.getType()!= BodyType.DYNAMIC) {
            if (!rod.isDead()) rod.kill();
            rod.body.setType(BodyType.DYNAMIC);
        }
    }

    private void resetMassDataAfterDeath(Body body) {
        body.setType(BodyType.DYNAMIC);
        body.setGravityScale((float) (body.getGravityScale()*0.8));
        body.getFixtureList().setDensity(body.getFixtureList().getDensity()/2);
        body.resetMassData();
    }

    public boolean isDead(){
        return dead;
    }

    public boolean isAllPlantPartsKilled() {
        if (leftJaw.isDead() && rightJaw.isDead() && rod.isDead()){
            System.out.println("All parts are killed and this controller can be deleted");
            return true;
        }
        else return false;
    }

    public void loadNormalGraphic() {
        rod.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerBody"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()), Flower.ROD_PART, (int)1, (int)1, (int)31, (int)31, (int) rod.getHeight(), (int)rod.getHeight());
        leftJaw.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws" + TextureDecodeManager.getExtensionForSpriteGraphicFile()), Flower.LEFT_JAW, (int)0, (int)0, (int)26, (int)25, (int) leftJaw.getJawDiameter(), (int) leftJaw.getJawDiameter());
        rightJaw.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()), Flower.RIGHT_JAW, (int)0, (int)0, (int)26, (int)25, (int) rightJaw.getJawDiameter(), (int) rightJaw.getJawDiameter());
        /*
        rod.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerBody.png"), Flower.ROD_PART, (int)1, (int)1, (int)31, (int)31, (int) rod.getHeight(), (int)rod.getHeight());
        leftJaw.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws.png"), Flower.LEFT_JAW, (int)0, (int)0, (int)26, (int)25, (int) leftJaw.getJawDiameter(), (int) leftJaw.getJawDiameter());
        rightJaw.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws.png"), Flower.RIGHT_JAW, (int)0, (int)0, (int)26, (int)25, (int) rightJaw.getJawDiameter(), (int) rightJaw.getJawDiameter());

         */
    }

    public void loadSprites(Tileset tilesetForJaw, Tileset tilesetForRod){
        rod.loadSprite(tilesetForRod);
        leftJaw.loadSprite(tilesetForJaw);
        rightJaw.loadSprite(tilesetForJaw);
    }

    public int getRodLife() {
        return rod.getLife();
    }

    public int getJawDiameter() {
        return leftJaw.getJawDiameter();
    }



    /*
    public void pauseMovements(){
        rod.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(0, 0));
        paused = true;
    }

    public void launchMovementsAgain(){
        paused = false;
    }
    */

}
