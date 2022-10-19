package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.graphic.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class CollectableSack extends SimpleCollectableElement{
    private ArrayList <AbstractCollectable> objects;
    private final static float dimensionCoefficient = 2.5f;

    public CollectableSack(Vec2 position, byte type, int fixationType) {
        this.type = type;
        this.fixationType = fixationType;
        objects = new ArrayList<>();
        inWorldPosition = IN_WORLD;
        fillBasicGraphicData();
        calculateDimensions(dimensionCoefficient);
        //this.valueToBeAddedByGain = valueToBeAddedByGain;
        init(position, null, (int)(width), (int)(height), BODY_FORM_RECT);
        body.setGravityScale(1.5f);
        initStars();
    }

    private void fillBasicGraphicData() {
        graphicData = InWorldObjectsGraphicData.sack;
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
        loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
    }

    private final static ImageZoneSimpleData getGraphicDataForType(byte type){
        System.out.println("For this type there are no data about graphic");
        return InWorldObjectsGraphicData.smallRedStone;
    }

    public ArrayList<AbstractCollectable> getObjects() {
        return objects;
    }

    public boolean areThereObjects(){
        if (objects.size()>0) return true;
        else return false;
    }
/*
    @Override

    public void update(GameRound gameRound){
        //System.out.println("Type: " + body.getType() + "; Scale: " + body.getGravityScale() + "; Mass: " + body.getMass());
    }*/

    public void addObject(AbstractCollectable object){
        objects.add(object);
        object.setInWorldPosition(IN_BAG);
        System.out.println("This sack has already " + objects.size() + " objects ");
        //PhysicGameWorld.controller.destroyBody(object.body);
    }

    public AbstractCollectable getObjectFromSack(int number){
        AbstractCollectable object;
        if (number <0 || number > (objects.size()-1)){
            object = objects.get(objects.size()-1);
            objects.remove(object);
        }
        else {
            object = objects.get(number);
            objects.remove(object);
        }

        return object;
    }

    @Override
    public String getStringAddedToWorldByBeGained(){
        boolean onlyMoney = true;
        for (AbstractCollectable object : objects){
            if (object.getClass() != Money.class){
                onlyMoney = false;
            }
        }
        if (onlyMoney) {
            int moneyInside = 0;
            for (AbstractCollectable object : objects){
                moneyInside+=object.valueToBeAddedByGain;
            }
            return moneyInside+"$";
        }
        else return "SOME OBJECTS";
    }

    public String getObjectToDisplayName(){
        return "Sack";
    }
}
