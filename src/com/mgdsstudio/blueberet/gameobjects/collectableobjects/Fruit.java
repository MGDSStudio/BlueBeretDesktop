package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class Fruit extends SimpleCollectableElement{


    public Fruit(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType) {
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    public Fruit(GameObjectDataForStoreInEditor objectData, GameRound gameRound){

        Vec2 position = objectData.getPosition();
        byte type = (byte) objectData.getLocalType();
        int valueToBeAddedByGain = objectData.getKeyValue();
        int fixationType = objectData.getFixationType();
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    protected void initBasicData(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType){
        this.type = type;
        this.fixationType = fixationType;
        dimensionCoefficient = dimensionCoefficient*0.65f;
        fillBasicGraphicData();
        calculateDimensions();
        this.valueToBeAddedByGain = valueToBeAddedByGain;
        //boolean bodyForm = BODY_FORM_CIRCLE;
        init(position, gameRound, width, height, getBodyTypeForType(type));
        initStars();
    }

    private final boolean getBodyTypeForType(byte type) {
        if (type == ANANAS) return BODY_FORM_RECT;
        else if (type == CARROT) return BODY_FORM_RECT;
        else if (type == APPLE) return BODY_FORM_RECT;
        else if (type == ORANGE) return BODY_FORM_RECT;
        else if (type == WATERMELON) return BODY_FORM_CIRCLE;
        else {
            if (Program.debug) System.out.println("There are no data about form for this graphic");
            return BODY_FORM_CIRCLE;
        }
    }


    private void fillBasicGraphicData() {
        graphicData = getGraphicDataForType(type);
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;

        if (InWorldObjectsGraphicData.mainGraphicTileset == null){
            InWorldObjectsGraphicData.mainGraphicTileset = HeadsUpDisplay.mainGraphicTileset;
            if (InWorldObjectsGraphicData.mainGraphicTileset == null){

            }
        }
        sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
        System.out.println("Graphic data for fruits is null: " + (InWorldObjectsGraphicData.mainGraphicTileset == null));
        loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
    }

    private final static ImageZoneSimpleData getGraphicDataForType(byte type){
        if (type == AbstractCollectable.ANANAS) return InWorldObjectsGraphicData.ananas;
        else if (type == AbstractCollectable.CARROT) return InWorldObjectsGraphicData.carrot;
        else if (type == AbstractCollectable.APPLE) return InWorldObjectsGraphicData.apple;
        else if (type == AbstractCollectable.ORANGE) return InWorldObjectsGraphicData.orange;
        else if (type == AbstractCollectable.WATERMELON) return InWorldObjectsGraphicData.watermelon;
        else {
            System.out.println("For this type of fruit there are no data about graphic " + type);
            return InWorldObjectsGraphicData.watermelon;
        }
    }

    @Override
    public String getStringAddedToWorldByBeGained(){
        if (Program.LANGUAGE == Program.RUSSIAN){
            return "ЖИЗНЬ+" + valueToBeAddedByGain + "";
        }
        else return "+" + valueToBeAddedByGain + " HP";
    }


    public String getObjectToDisplayName(){
        return "Fruit";
    }
}
