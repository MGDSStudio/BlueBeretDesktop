package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import org.jbox2d.common.Vec2;

public class Syringe extends SimpleCollectableElement{
        public final static int NORMAL_SLOW_TIME = 10000;
        public final static int MAX_SLOW_TIME = 15000;
        public final static int MIN_SLOW_TIME = 5000;

        public Syringe (Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType) {
            initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
        }

        public Syringe(GameObjectDataForStoreInEditor objectData, GameRound gameRound){
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
            graphicData = InWorldObjectsGraphicData.syringeInWorld;
            /*
            if (type == SMALL_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.largeMedicalKit;
            else if (type == MEDIUM_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.mediumMedicalKit;
            else if (type == LARGE_MEDICAL_KIT) graphicData = InWorldObjectsGraphicData.smallMedicalKit;
            else {
                graphicData = InWorldObjectsGraphicData.largeMedicalKit;
            }*/
            graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
            //graphicData = InWorldObjectsGraphicData.getGraphicDataForMedicalKit(type);
            //System.out.println();
            sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
            loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
        }

        @Override
        public String getStringAddedToWorldByBeGained(){
            return "SLOW MOTION DRUG";
        }

    @Override
    public String getObjectToDisplayName(){
        return "SLOW MOTION DRUG";
    }
}
