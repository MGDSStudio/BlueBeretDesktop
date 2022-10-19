package com.mgdsstudio.blueberet.graphic.effectsmasters;

import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class SpritesFocusMaster extends AbstractGraphicFocusMaster{

    private final ArrayList<IndependentOnScreenStaticSprite> backgrounds;

    public SpritesFocusMaster(ArrayList<IndependentOnScreenStaticSprite> backgrounds) {
        this.backgrounds = backgrounds;
        basicTilesets = new ArrayList<>();
        secondaryTilesets = new ArrayList<>();
        fillData();
        //createAntialiasedTilesets(range);
    }

    protected void fillData() {
        for (IndependentOnScreenStaticSprite background : backgrounds){
            Tileset tileset = background.staticSprite.getTileset();
            if (!basicTilesets.contains(tileset)){
                basicTilesets.add(tileset);
            }
        }
    }

    protected void setNewGraphic(boolean type) {
        for (IndependentOnScreenStaticSprite background : backgrounds){
                String path = background.getPath();
                if (type == WITH_EFFECT){
                    for (Tileset unfocused : secondaryTilesets){
                        if ( unfocused.getPath().equals(path) || unfocused.getPath() == path || unfocused.getPath().contains(path)){
                            background.staticSprite.setTileset(unfocused);
                            break;
                        }
                    }
                }
                else {
                    for (Tileset original : basicTilesets){
                        if ( original.getPath().equals(path) || original.getPath() == path || original.getPath().contains(path)){
                            background.staticSprite.setTileset(original);
                            if (Program.debug) System.out.println("Set original tileset");
                            break;
                        }
                    }
                }

        }

    }
}
