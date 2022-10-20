package com.mgdsstudio.blueberet.gameobjects.data;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class ObjectsClearingZoneData extends GameObjectData{
    private Vec2 firstPos;
    private Vec2 secondPoint;
    private int parameter;

    public ObjectsClearingZoneData() {
        className = ObjectsClearingZone.CLASS_NAME;
    }

    @Override
    public void createDataString() {
        createStartData();
        Vec2 center = getCenterBetweenTwoPoints(firstPos, secondPoint);
        addValueToDataStringWithCommaSeparator(center);
        addValueToDataStringWithCommaSeparator(PApplet.abs(secondPoint.x-firstPos.x));
        addValueToDataStringWithCommaSeparator(PApplet.abs(secondPoint.y-firstPos.y));
        dataString+=parameter;
    }

    private Vec2 getCenterBetweenTwoPoints(Vec2 firstPoint, Vec2 secondPoint){
        float x = (firstPoint.x+secondPoint.x)/2;
        float y = (firstPoint.y+secondPoint.y)/2;
        return new Vec2(x,y);

    }

    public void setFirstPos(Vec2 firstPos) {
        this.firstPos = firstPos.clone();
    }

    public void setSecondPoint(Vec2 secondPoint) {
        this.secondPoint = secondPoint.clone();
    }

    public void setParameter(int parameter) {
        this.parameter = parameter;
    }
}
