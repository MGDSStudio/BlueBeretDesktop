package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

public class Net {
    final static int STAY = 0, TO_UP = 1, TO_DOWN = 2;
    private MouseJoint spiderNetJoint;

    Net(float x, float y, Body spiderBody){
        bind(x, y, spiderBody);
    }

    private void bind(float x, float y, Body body) {
        MouseJointDef md = new MouseJointDef();
        md.bodyA = PhysicGameWorld.controller.getGroundBody();
        md.bodyB = body;
        Vec2 mp = PhysicGameWorld.controller.coordPixelsToWorld(x,y);
        md.target.set(mp);
        md.maxForce = 70.0f * body.m_mass;
        md.frequencyHz = 2.0f;
        md.dampingRatio = 0.2f;

        // Make the joint!
        spiderNetJoint = (MouseJoint) PhysicGameWorld.controller.world.createJoint(md);
        System.out.println("Net is created");
    }

    void moveSpiderOnNet(int direction){

    }


    /*
    public void update(){

    }*/

    public void deleteNet(Person spider){
        if (!PhysicGameWorld.controller.world.isLocked()) {
            spiderNetJoint.setDampingRatio(spiderNetJoint.getDampingRatio() / 20);
            spiderNetJoint.setFrequency(spiderNetJoint.getFrequency() / 20);
            spiderNetJoint.setMaxForce(0f);
            spiderNetJoint.setTarget(spider.body.getPosition());
            PhysicGameWorld.controller.world.destroyJoint(spiderNetJoint);
            spiderNetJoint = null;
        }
        else {
            System.out.println("Can not delete spring joint from the world");
        }
    }


}
