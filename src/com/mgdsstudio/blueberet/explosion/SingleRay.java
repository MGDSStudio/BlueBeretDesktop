package com.mgdsstudio.blueberet.explosion;

import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PVector;

public class SingleRay {
    private final int rayLength;    //pixels
    private final PVector rayStartPos;
    private final float angle;
    private int step = 3;
    private int itterationsNumber;



    private PVector collisionPlace;

    public SingleRay(PVector rayStartPos, int rayLength, float angle){
        this.rayStartPos = rayStartPos;
        this.angle = angle;
        this.rayLength = rayLength;
        itterationsNumber = PApplet.ceil(rayLength /step);
    }

    public Body getNearestBody(){
        PVector actualPos = new PVector(rayStartPos.x, rayStartPos.y);
        //System.out.println("Itterations: " + itterationsNumber);
        for (int i = 0; i < itterationsNumber; i++){
            actualPos.x += (step * PApplet.cos(PApplet.radians(angle)));
            actualPos.y += (step * PApplet.sin(PApplet.radians(angle)));
            //System.out.println("Test pos: " + actualPos);
            Body body = getBodyOnPlace(actualPos);
            if (body != null) {
                collisionPlace = new PVector(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
                return body;
            }
        }
        return null;
    }

    private Body getBodyOnPlace(PVector actualPos) {
        //if (PhysicGameWorld.arePointInAnyBodyButNotInBullet(actualPos)) {
        if (PhysicGameWorld.arePointInAnyBody(actualPos)) {
            //System.out.println("Collision founded: ");
            return PhysicGameWorld.getBodyAtPoint(actualPos);
        }
        return null;
    }


    public PVector getCollisionPlace() {
        return collisionPlace;
    }

    float getRelativeDistanceFromStart() {	// from 0 to 100 %
        float actualRayLength = Program.engine.dist(rayStartPos.x, rayStartPos.y, collisionPlace.x, collisionPlace.y);
        int percentValue = (int)(100f*actualRayLength/rayLength);
        System.out.println("Ray length: " + rayLength + "; Collision on length: " + actualRayLength + "; in %: " + percentValue);
        return percentValue;
    }
}
