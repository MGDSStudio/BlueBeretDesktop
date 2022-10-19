package com.mgdsstudio.blueberet.gameobjects.persons.flower;

import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import processing.core.PConstants;
import processing.core.PVector;

public class PlantMovementController {
    private PVector basePlace, goalPlace;
    private int angle;
    private int zoneWidth, zoneHeight;
    private Plant stem, jawLeft, jawRight;
    private RevoluteJoint joint1, joint2;
    final static private boolean MOVEMENT_TO_GOAL_PLACE = false;
    final static private boolean MOVEMENT_TO_BASE_PLACE = true;
    final static private float NORMAL_MOVEMENT_VELOCITY = 10;
    boolean movementStatement = MOVEMENT_TO_GOAL_PLACE;
    private float movementVelocity = NORMAL_MOVEMENT_VELOCITY;

    public final static int NORMAL_DIAMETER = (int) (70);
    private int diameter = NORMAL_DIAMETER;
    private int rodThickness = (int) (diameter/5f);
    private int rodLength = (int) (diameter*1.9f);

    //behaviour
    public static final byte NO_FLOWER = -1;
    public static final byte NO_ACTIVE = 0;
    public static final byte STAY_AND_BITE = 1;
    public static final byte UP_BITE_DOWN = 2;
    public static final byte UP_UNDER_PLAYER = 3;
    public static final byte FOLOW_PLAYER = 4;
    private byte behaviourModel = STAY_AND_BITE;

    // Attack
    public final static int NORMAL_ATTACK_POWER = 90;	//90
    public final static int ATTACK_POWER_BY_DYING = NORMAL_ATTACK_POWER*2;
    private int attackPower = NORMAL_ATTACK_POWER;

    private final static int MAX_UPPPER_POSITION = (int) (-NORMAL_DIAMETER*1.2);
    private final static int MIN_LOWER_POSITION = (int) (NORMAL_DIAMETER*0.5);

    public final static int NORMAL_LIFE = 20;
    public final static byte FULL_ALIVE = 1;
    public final static byte WITHOUT_LEFT_PART = 2;
    public final static byte WITHOUT_RIGHT_PART = 3;
    public final static byte ROD_IS_FREE = 4;
    public final static byte COMPLETE_DEAD = 5;
    private byte statement = FULL_ALIVE;

    //Memory clear data


    // bite data
    private final static float NORMAL_BITE_SPEED = -PConstants.PI*2.5f;
    private final static float BITE_SPEED_BY_DYING = NORMAL_BITE_SPEED*2.5f;
    private final static float NORMAL_BITE_TORQUE = 1000;
    private final static float BITE_TORQUE_BY_DYING = NORMAL_BITE_TORQUE*2.5f;
    private float biteSpeed = NORMAL_BITE_SPEED;
    private float biteTorque = NORMAL_BITE_TORQUE;

    // Stages
    public static final byte JAW_CLOSE = 1;
    public static final byte JAW_OPEN = 2;
    private byte stage = JAW_CLOSE;

    // Critical angles
    private static final float MAX_RIGHT_ANGLE = Program.engine.radians(0);
    private static final float MIN_RIGHT_ANGLE =  Program.engine.radians(-88);
    private static final float MAX_LEFT_ANGLE = Program.engine.radians(88);
    //private static final float MAX_LEFT_ANGLE = Game2D.engine.radians(88);
    private static final float MIN_LEFT_ANGLE = Program.engine.radians(0);
    private final static boolean MIN = false;
    private final static boolean MAX = true;

    public PlantMovementController(Vec2 position, int life, byte behaviourModel, int pipeInsideDiameter){
        this.behaviourModel = behaviourModel;
        /*
        stem = new Plant(position,  life, behaviourModel, Plant.ROD_PART, pipeInsideDiameter);
        jawLeft = new Plant(position,  life, behaviourModel, Plant.LEFT_JAW, pipeInsideDiameter);
        jawRight = new Plant(position,  life, behaviourModel, Plant.RIGHT_JAW, pipeInsideDiameter);
*/
        try {
            makeRotating();
            //setFilterData(COAlISION_FLOWER_WITH_PIPE_GROUP);
            if (behaviourModel == UP_BITE_DOWN || behaviourModel == UP_UNDER_PLAYER) {
                basePlace = new PVector(position.x, position.y + MIN_LOWER_POSITION);
                goalPlace = new PVector(position.x, position.y + MAX_UPPPER_POSITION);
                System.out.println("Upper flower pos: " + goalPlace.y);
                System.out.println("Lower flower pos: " + basePlace.y);
                setStartVelocity();
            }
        }
        catch (Exception e ){
            System.out.println("Can not create the flower 2");
        }
    }

    private void setStartVelocity(){
        if (MOVEMENT_TO_GOAL_PLACE == movementStatement) {
            stem.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(0, getVelocity(movementStatement)));
        }
        else {
            stem.body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(0, getVelocity(movementStatement)));
        }
    }

    private float getVelocity(Boolean movementStatement){
        if (Program.deltaTime > 9999 || Program.deltaTime < 1) {
            if (MOVEMENT_TO_GOAL_PLACE == movementStatement) return	-33*movementVelocity;
            else return 33*movementVelocity;
        }
        else {
            if (MOVEMENT_TO_BASE_PLACE == movementStatement) return -Program.deltaTime*movementVelocity;
            else return Program.deltaTime*movementVelocity;
        }
    }

    private void makeRotating() {
        RevoluteJointDef rjd1 = new RevoluteJointDef();
        RevoluteJointDef rjd2 = new RevoluteJointDef();
        try {
            Vec2 offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(0, -rodLength/2));
            rjd1.initialize(jawLeft.body, stem.body, stem.body.getWorldCenter().add(offset));
            rjd2.initialize(jawRight.body, stem.body, stem.body.getWorldCenter().add(offset));
        }
        catch (Exception e) {
            System.out.println("Can not initialize rotating. body: " + stem.body);
        }

        rjd1.motorSpeed = biteSpeed;       // how fast?
        rjd1.maxMotorTorque = biteTorque; // how powerful?
        rjd1.enableMotor = true;      // is it on?
        rjd2.motorSpeed = -biteSpeed;       // how fast?
        rjd2.maxMotorTorque = biteTorque; // how powerful?
        rjd2.enableMotor = true;      // is it on?

        joint1 = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd1);
        joint2 = (RevoluteJoint) PhysicGameWorld.controller.world.createJoint(rjd2);
        joint1.enableLimit(true);
        joint2.enableLimit(true);
        joint1.setLimits(MIN_RIGHT_ANGLE, MAX_RIGHT_ANGLE);
        joint2.setLimits(MIN_LEFT_ANGLE, MAX_LEFT_ANGLE);
        jawLeft.body.setTransform(jawLeft.body.getPosition(), Program.engine.radians(150));
        jawRight.body.setTransform(jawRight.body.getPosition(), Program.engine.radians(-150));
    }

    private void update(){
        if ( stem.isAlive()){

        }

    }

    public int getRodThickness(){
        return rodThickness;
    }

    public int getRodLength(){
        return rodLength;
    }

    public int getRodDiameter(){
        return diameter;
    }

    private void init(RoundPipe roundPipe){

    }



}
