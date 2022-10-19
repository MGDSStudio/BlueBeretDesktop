package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

public class RayCastNew {
    RayCastCallback callback;
    World world;

    public RayCastNew(){
        world = PhysicGameWorld.controller.world;
    }

    public void init(){
        callback = new RayCastCallback() {
            @Override
            public float reportFixture(Fixture fixture, Vec2 vec2, Vec2 vec21, float v) {
                System.out.println("RAY_CAST!");
                return -1;
            }
        };
    }

    public void calculateRaycast(Vec2 start, Vec2 end){

        //callback listens to the query and calls reportRayFixture method on colliding with a fixture
        //start and end are start and end points of the ray

        world.raycast(callback,start,end);
        System.out.println("Started to determine raycast");
    }
}
