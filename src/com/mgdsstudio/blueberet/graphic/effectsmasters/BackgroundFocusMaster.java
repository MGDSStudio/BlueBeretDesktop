package com.mgdsstudio.blueberet.graphic.effectsmasters;

import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class BackgroundFocusMaster extends AbstractGraphicFocusMaster{


    private final ArrayList<Background> backgrounds;



    public BackgroundFocusMaster(ArrayList<Background> backgrounds) {
        this.backgrounds = backgrounds;
        basicTilesets = new ArrayList<>();
        secondaryTilesets = new ArrayList<>();
        fillData();
    }

    @Override
    protected void fillData() {
        for (Background background : backgrounds){
            if (background instanceof ScrollableAlongXBackground){
                ScrollableAlongXBackground scrollableAlongXBackground = (ScrollableAlongXBackground) background;
                Tileset tileset = scrollableAlongXBackground.getTileset();
                if (!basicTilesets.contains(tileset)){
                    basicTilesets.add(tileset);
                }
            }
        }
    }





    @Override
    protected void setNewGraphic(boolean type) {
            for (Background background : backgrounds){
                if (background instanceof ScrollableAlongXBackground) {
                    ScrollableAlongXBackground scrollableAlongXBackground = (ScrollableAlongXBackground) background;
                    String path = scrollableAlongXBackground.getPath();
                    if (type == WITH_EFFECT){
                        for (Tileset unfocused : secondaryTilesets){
                            if ( unfocused.getPath().equals(path) || unfocused.getPath() == path || unfocused.getPath().contains(path)){
                                scrollableAlongXBackground.setTileset(unfocused);
                                break;
                            }
                        }
                    }
                    else {
                        for (Tileset original : basicTilesets){
                            if ( original.getPath().equals(path) || original.getPath() == path || original.getPath().contains(path)){
                                scrollableAlongXBackground.setTileset(original);
                                if (Program.debug) System.out.println("Set original tileset");
                                break;
                            }
                        }
                    }
                }
            }

    }


}
