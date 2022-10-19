package com.mgdsstudio.blueberet.graphic.effectsmasters;

import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

abstract class GraphicEffectMaster {
    protected final boolean WITH_EFFECT = true;
    protected final boolean ORIGINAL = false;
    protected ArrayList<Tileset> basicTilesets;
    protected ArrayList<Tileset> secondaryTilesets;


    protected boolean statement = ORIGINAL;

    protected abstract void fillData();

    protected void fillSecondaryTilesetsWithOriginal(){
        int count = 0;
        if (secondaryTilesets.size() == 0){
            for (Tileset tileset : basicTilesets){
                if (tileset != null) {
                    Image image = new Image(tileset.getPath());
                    Tileset manualCloned = new Tileset(image);
                    secondaryTilesets.add(manualCloned);
                    count++;
                }
            }
        }
        System.out.println("We have: " + count + " unique tilesets");
    }

    public void applyEffectToGraphic(float range){
        if (statement == ORIGINAL) {
            if (range > 0 && range < 1f) {
                try {
                    applyEffectToTilesets(range);
                    setNewGraphic(WITH_EFFECT);
                    statement = WITH_EFFECT;
                    if (Program.debug) System.out.println("Background set as smothes");
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            } else System.out.println("Can not set as antialised");
        }
    }

    protected abstract void setNewGraphic(boolean type);
    protected abstract void applyEffectToTilesets(float range);
}
