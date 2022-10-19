package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import org.jbox2d.common.Vec2;

public class Money extends SimpleCollectableElement{

    public Money(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType) {
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
        /*
        this.type = type;
        this.fixationType = fixationType;
        fillBasicGraphicData();
        calculateDimensions();
        this.valueToBeAddedByGain = valueToBeAddedByGain;
        init(position, gameRound, width, height);
        initStars();
        */
    }

    public Money(GameObjectDataForStoreInEditor objectData, GameRound gameRound){
        Vec2 position = objectData.getPosition();
        byte type = (byte) objectData.getLocalType();
        int valueToBeAddedByGain = objectData.getKeyValue();
        int fixationType = objectData.getFixationType();
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    protected void initBasicData(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType){
        this.type = type;
        this.fixationType = fixationType;
        fillBasicGraphicData();
        calculateDimensions();
        this.valueToBeAddedByGain = valueToBeAddedByGain;
        boolean bodyForm = BODY_FORM_CIRCLE;
        if (isGem()) bodyForm = BODY_FORM_RECT;
        init(position, gameRound, width, height, bodyForm);
        initStars();
    }



    private void fillBasicGraphicData() {
        graphicData = getGraphicDataForType(type);
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        //graphicData = InWorldObjectsGraphicData.getGraphicDataForMedicalKit(type);
        //System.out.println();
        if (InWorldObjectsGraphicData.mainGraphicTileset == null){
            InWorldObjectsGraphicData.mainGraphicTileset = HeadsUpDisplay.mainGraphicTileset;
            if (InWorldObjectsGraphicData.mainGraphicTileset == null){

            }
        }
        sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
        System.out.println("Graphic data for money is null: " + (InWorldObjectsGraphicData.mainGraphicTileset == null));
        loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
    }

    private final static ImageZoneSimpleData getGraphicDataForType(byte type){
        if (type == AbstractCollectable.SMALL_RED_STONE) return InWorldObjectsGraphicData.smallRedStone;
        else if (type == AbstractCollectable.BIG_RED_STONE) return InWorldObjectsGraphicData.bigRedStone;
        else if (type == AbstractCollectable.SMALL_BLUE_STONE) return InWorldObjectsGraphicData.smallBlueStone;
        else if (type == AbstractCollectable.BIG_BLUE_STONE) return InWorldObjectsGraphicData.bigBlueStone;
        else if (type == AbstractCollectable.SMALL_YELLOW_STONE) return InWorldObjectsGraphicData.smallYellowStone;
        else if (type == AbstractCollectable.BIG_YELLOW_STONE) return InWorldObjectsGraphicData.bigYellowStone;
        else if (type == AbstractCollectable.SMALL_WHITE_STONE) return InWorldObjectsGraphicData.smallWhiteStone;
        else if (type == AbstractCollectable.BIG_WHITE_STONE) return InWorldObjectsGraphicData.bigWhiteStone;
        else if (type == AbstractCollectable.SMALL_GREEN_STONE) return InWorldObjectsGraphicData.smallGreenStone;
        else if (type == AbstractCollectable.BIG_GREEN_STONE) return InWorldObjectsGraphicData.bigGreenStone;

        else if (type == AbstractCollectable.SMALL_COIN_COPPER) return InWorldObjectsGraphicData.smallCoinCopper;
        else if (type == AbstractCollectable.MEDIUM_COIN_COPPER) return InWorldObjectsGraphicData.mediumCoinCopper;
        else if (type == AbstractCollectable.BIG_COIN_COPPER) return InWorldObjectsGraphicData.bigCoinCopper;

        else if (type == AbstractCollectable.SMALL_COIN_GOLD) return InWorldObjectsGraphicData.smallCoinGold;
        else if (type == AbstractCollectable.MEDIUM_COIN_GOLD) return InWorldObjectsGraphicData.mediumCoinGold;
        else if (type == AbstractCollectable.BIG_COIN_GOLD) return InWorldObjectsGraphicData.bigCoinGold;

        else if (type == AbstractCollectable.SMALL_COIN_SILVER) return InWorldObjectsGraphicData.smallCoinSilver;
        else if (type == AbstractCollectable.MEDIUM_COIN_SILVER) return InWorldObjectsGraphicData.mediumCoinSilver;
        else if (type == AbstractCollectable.BIG_COIN_SILVER) return InWorldObjectsGraphicData.bigCoinSilver;

            //else if (type == AbstractCollectable.SMALL_YELLOW_STONE) return InWorldObjectsGraphicData.smallYellowStone;
            //else if (type == AbstractCollectable.BIG_YELLOW_STONE) return InWorldObjectsGraphicData.bigYellowStone;
        else {
            System.out.println("For this type of money there are no data about graphic " + type);
            return InWorldObjectsGraphicData.smallRedStone;
        }



    }

    public boolean isCoin(){
        return isTypeCoin(type);
    }

    @Override
    public String getStringAddedToWorldByBeGained(){
        return valueToBeAddedByGain+" $";
    }


    public String getObjectToDisplayName(){
        return "Money";
    }
}
