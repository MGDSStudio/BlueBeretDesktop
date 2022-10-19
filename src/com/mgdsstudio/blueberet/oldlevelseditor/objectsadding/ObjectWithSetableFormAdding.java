package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;

public class ObjectWithSetableFormAdding extends ObjectsAdding{
    protected Vec2 firstPoint;
    protected Vec2 secondPoint;
    protected Vec2 thirdPoint;

    public final static byte FIRST_POINT_ADDING = 1;
    public final static byte SECOND_POINT_ADDING = 2;

    public void addFirstPoint(Vec2 position){
        firstPoint = new Vec2(position.x, position.y);
    }

    public void addSecondPoint(Vec2 position){
        secondPoint = new Vec2(position.x, position.y);
    }

    public void addThirdPoint(Vec2 position){
        thirdPoint = new Vec2(position.x, position.y);
    }

    public int getWidth(){
        if (firstPoint != null && secondPoint != null){
            return (int)(secondPoint.x-firstPoint.x);
        }
        else return -1;
    }

    public PVector getPosition(){
        PVector pos = new PVector(0,0);
        if (firstPoint != null && secondPoint != null){
            pos.x = firstPoint.x+getWidth()/2;
            pos.y = firstPoint.y+getHeight()/2;
        }
        else {
            System.out.println("There are no data about key points");
        }
        return pos;
    }

    public int getHeight(){
        if (firstPoint != null && secondPoint != null){
            return (int)(secondPoint.y-firstPoint.y);
        }
        else return -1;
    }

    protected float getHeight(Vec2 firstPoint, Vec2 secondPoint){
        return PApplet.abs(PApplet.abs(firstPoint.y) - PApplet.abs(secondPoint.y));
    }

    protected float getWidth(Vec2 firstPoint, Vec2 secondPoint){
        return PApplet.abs(PApplet.abs(firstPoint.x) - PApplet.abs(secondPoint.x));
    }



}
