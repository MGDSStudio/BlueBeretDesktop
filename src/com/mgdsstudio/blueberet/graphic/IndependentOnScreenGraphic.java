package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public abstract class IndependentOnScreenGraphic extends SingleGameElement  implements ILayerable{
    public byte layer = BEHIND_PERSONS;
    protected Vec2 position;
    protected float angle;
    protected boolean flip = false;


    public void setPosition(Vec2 position){
        this.position = position;
    }

    public void setNewAngle(float angleInDegrees){
        this.angle = angleInDegrees;
    }

    public void setLayer(byte layer){
        this.layer = layer;
    }


    public abstract String getStringData() ;

    /*
    public String getStringData() {
        System.out.println("This method get string data for graphic must be overriden");
        return new String();
    }
    */



    public abstract float getWidth();


    public abstract float getHeight();

    /*
    public float getWidth() {
        System.out.println("This must be overriden " + this.getClassName());
        return 0;
    }


    public float getHeight() {
        System.out.println("This must be overriden " + this.getClassName());
        return 0;
    }
     */


    public Vec2 getPosition(){
        return position;
    }

    public float getAngle(){
        return angle;
    }

    public boolean getFlip(){
        return flip;
    }

    public abstract String getPath() ;
    /*
    {
        System.out.println("This method getPath must be overriden");
        return new String();
    }
     */

    public byte getLayer(){
        return layer;
    }

    protected void tintUpdatingBySelecting() {

    }

    public boolean isPointOnElement(Vec2 posToFind){
        return GameMechanics.isPointInRect(posToFind.x, posToFind.y, position.x-getWidth()/2, position.y-getHeight()/2, getWidth(), getHeight());
    }

    public boolean isPointOnElement(PVector posToFind){
        return GameMechanics.isPointInRect(posToFind.x, posToFind.y, position.x-getWidth()/2, position.y-getHeight()/2, getWidth(), getHeight());
    }

}
