package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class Rectangular {
    private float centerX, centerY;
    private float width, height;


    public Rectangular(Vec2 center, float width, float height) {
        centerX = center.x;
        centerY = center.y;
        this.width = width;
        this.height = height;
    }

    public Rectangular(float centerX, float centerY, float width, float height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public Vec2 getCenter() {
        return new Vec2(centerX, centerY);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getLeftUpperX(){
        return (centerX-width/2);
    }

    public float getLeftUpperY(){
        return (centerY-height/2);
    }

    public float getRightLowerY(){
        return (centerY+height/2);
    }

    public float getRightLowerX(){
        return (centerX+width/2);
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public boolean isPointInRect(PVector point){
        return GameMechanics.isPointInRect(point.x, point.y, this);
    }

    public boolean isPointInRect(float pointX, float pointY){
        return GameMechanics.isPointInRect(pointX, pointY, this);
    }


}
