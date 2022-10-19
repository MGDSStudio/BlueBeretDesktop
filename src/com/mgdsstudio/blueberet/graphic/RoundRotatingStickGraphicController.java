package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class RoundRotatingStickGraphicController {
    //private ArrayList<Vec2> firePositions;
    private final int elementsNumber;
    private final float distanceBetweenFires;
    private final float distanceFromAxisToFirstFire;
    private float fireRotatingVelocity = -PConstants.PI/500;     //Pro
    private float actualFireAngle = 0;

    public RoundRotatingStickGraphicController(int length, int thickness, int fireSpriteEffectiveRadius, float startAngle){
        elementsNumber = (int) PApplet.floor(length/fireSpriteEffectiveRadius);
        distanceBetweenFires = length/elementsNumber;
        float distanceBetweenExtremeFires = distanceBetweenFires*(elementsNumber-1);
        distanceFromAxisToFirstFire = (length-(length-(distanceBetweenExtremeFires))/2);
        //firePositions = new ArrayList<>();
        /*
        for (int i = 0; i < elementsNumber; i++){
            Vec2 firePosition = new Vec2(distanceFromAxisToFirstFire-i*distanceBetweenFires, startAngle);
        }

         */
    }

    public void update(){
        actualFireAngle+=fireRotatingVelocity* Program.deltaTime;
    }

    public float getActualAngle(){
        return actualFireAngle;
    }

    public ArrayList<Vec2> getFirePositions(Vec2 basicPosition, float angleInRadians){
        ArrayList<Vec2> firePositions = new ArrayList<>();
        for (int i = 0; i < elementsNumber; i++){
            Vec2 firePosition = new Vec2(((-distanceFromAxisToFirstFire+i*distanceBetweenFires)*PApplet.cos(angleInRadians)), ((-distanceFromAxisToFirstFire+i*distanceBetweenFires)*PApplet.sin(angleInRadians)));
            //Vec2 firePosition = new Vec2(((distanceFromAxisToFirstFire-i*distanceBetweenFires)*PApplet.cos(angleInRadians)), ((distanceFromAxisToFirstFire-i*distanceBetweenFires)*PApplet.sin(angleInRadians)));
            //
            firePosition.x+= basicPosition.x;
            firePosition.y+= basicPosition.y;
            firePositions.add(firePosition);
        }
        return firePositions;
    }





}
