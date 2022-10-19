package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import org.jbox2d.common.Vec2;

public class MedicalKit extends SimpleCollectableElement{
    public final static int RECOVERY_VALUE_FOR_SMALL = 25;
    public final static int RECOVERY_VALUE_FOR_MEDIUM = 50;
    public final static int RECOVERY_VALUE_FOR_LARGE = 100;

    public MedicalKit(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType) {
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    public MedicalKit(GameObjectDataForStoreInEditor objectData, GameRound gameRound){
        Vec2 position = objectData.getPosition();
        byte type = (byte) objectData.getLocalType();
        int valueToBeAddedByGain = objectData.getKeyValue();
        int fixationType = objectData.getFixationType();
        boolean nullGravity = objectData.isNoGravity();
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    private void initBasicData(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType){
        this.type = type;
        this.fixationType = fixationType;
        fillBasicGraphicData();
        calculateDimensions();
        this.valueToBeAddedByGain = valueToBeAddedByGain;
        init(position, gameRound, width, height, BODY_FORM_RECT);
        initStars();
    }

    private void fillBasicGraphicData() {
        if (type == SMALL_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.smallMedicalKitInWorld;
        else if (type == MEDIUM_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.mediumMedicalKitInWorld;
        else if (type == LARGE_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.largeMedicalKitInWorld;
        else {
            graphicData = InWorldObjectsGraphicData.largeMedicalKitInWorld;
        }
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
        loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
    }

    @Override
    public String getStringAddedToWorldByBeGained(){
        if (type == SMALL_MEDICAL_KIT) return "PAINKILLER";
        else if (type == MEDIUM_MEDICAL_KIT) return "MEDICINE";
        else return "MEDICAL KIT";
    }

    public static int getLifeInPercentToBeAddedByMedicalKitEating(int type){
        if (type == SMALL_MEDICAL_KIT) return RECOVERY_VALUE_FOR_SMALL;
        else if (type == MEDIUM_MEDICAL_KIT) return RECOVERY_VALUE_FOR_MEDIUM;
        else if (type == LARGE_MEDICAL_KIT) return RECOVERY_VALUE_FOR_LARGE;
        else {
            System.out.println("There are not data about this medical kit");
            return 0;
        }
    }

    public String getObjectToDisplayName(){
        return "Medical kit";
    }
}
