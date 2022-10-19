package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.Spring;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class SpringRelaxationController {
    private byte actualFrameAfterRelaxationStart;
    private byte MAX_FRAMES_TO_DELETE_SPRING = 1;
    private Spring spring;
    private Body body;
    private boolean ended;

    public SpringRelaxationController(Spring spring, Body body){
        this.spring = spring;
        this.body = body;
    }

    public void update(){
        if (!ended) {
            if (actualFrameAfterRelaxationStart == MAX_FRAMES_TO_DELETE_SPRING) {
                Vec2 velocity = new Vec2(body.getLinearVelocity().x, body.getLinearVelocity().y);
                float angular = body.getAngularVelocity();
                PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                spring.mouseJoint = null;
                spring = null;
                body.setAngularVelocity(angular);
                body.setLinearVelocity(velocity.negate());
                body.resetMassData();
                ended = true;
                System.out.println("Ended to kill");
            }
            else {
                if (actualFrameAfterRelaxationStart == 0){
                    System.out.println("Started to kill");
                    spring.mouseJoint.setDampingRatio(spring.mouseJoint.getDampingRatio() / 10);
                    spring.mouseJoint.setFrequency(spring.mouseJoint.getFrequency() / 10);
                    spring.mouseJoint.setMaxForce(spring.mouseJoint.getMaxForce() / 10);
                    //body.m_mass = body.getMass()/10;
                    //body.resetMassData();
                }
                actualFrameAfterRelaxationStart++;
            }
        }
    }
}
