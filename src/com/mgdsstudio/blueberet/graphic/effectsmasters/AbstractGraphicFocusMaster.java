package com.mgdsstudio.blueberet.graphic.effectsmasters;

import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public abstract class AbstractGraphicFocusMaster extends GraphicEffectMaster{
    public final static float NORMAL_ONE_STEP_RATE = 0.4f;
    public final static float MINIMAL_ONE_STEP_RATE = 0.9f;

    protected int smoothingStagesNumber = 4;



    @Override
    protected void applyEffectToTilesets(float range){
        fillSecondaryTilesetsWithOriginal();
        for (Tileset tileset : secondaryTilesets){
            try {
                int originalW = tileset.getPicture().image.width;
                int originalH = tileset.getPicture().image.height;
                int newWidth = (int)(originalW*range);
                int newHeight = (int)(originalH*range);
                for (int i = 0; i < smoothingStagesNumber; i++){
                    tileset.getPicture().image.resize(newWidth, newHeight);
                    tileset.getPicture().image.resize(originalW, originalH);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }





    public void setGraphicAsNormalInOneStep(){
        if (statement == WITH_EFFECT) {
            setNewGraphic(ORIGINAL);
            statement = ORIGINAL;
            System.out.println("Background set as normal");
        }
    }




}
