package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class SingleFlagZoneAdding extends ObjectWithSetableFormAdding{
    private ObjectsClearingZone objectsClearingZone;
    public final static byte SAVING = 3;
    public final static byte COMPLETED = 4;
    public final static byte END = COMPLETED;
    private byte goal = 0;

    public SingleFlagZoneAdding(){
        endStatement = END;
    }

    public void setGoal(byte goal){
        this.goal = goal;
    }

    public ObjectsClearingZone getObjectsClearingZone() {
        if (objectsClearingZone == null){
            objectsClearingZone = new ObjectsClearingZone(getFlag(), goal);
        }
        return objectsClearingZone;
    }

    private Flag getFlag(){
        Vec2 centerPos = GameMechanics.getCenterBetweenTwoPoints(firstPoint, secondPoint);
        int width = (int)getWidth(firstPoint, secondPoint);
        int height = (int)getHeight(firstPoint, secondPoint);
        Flag flag = new Flag(new PVector(centerPos.x, centerPos.y), width, height, Flag.NO_MISSION);
        return flag;
    }
}
