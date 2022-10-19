package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class GraphicFocusMaster {
    public final static float NORMAL_ONE_STEP_RATE = 0.45f;
    public final static float MINIMAL_ONE_STEP_RATE = 0.6f;

    private final ArrayList<Background> backgrounds;
    private final ArrayList<Tileset> basicTilesets;
    private final ArrayList<Tileset> unfocusedTilesets;
    private final boolean UNFOCUSED = true;
    private final boolean ORIGINAL = false;

    private boolean statement = ORIGINAL;

    public GraphicFocusMaster(ArrayList<Background> backgrounds) {
        this.backgrounds = backgrounds;
        basicTilesets = new ArrayList<>();
        unfocusedTilesets = new ArrayList<>();
        fillData(backgrounds);
    }

    private void fillData(ArrayList<Background> backgrounds) {
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

    private void fillUnfocusedWithOriginal(){
        if (unfocusedTilesets.size() == 0){
            for (Tileset tileset : basicTilesets){
                Image image = new Image(tileset.getPath());
                Tileset manualCloned = new Tileset(image);
                unfocusedTilesets.add(manualCloned);
            }
        }
    }

    public void setBackgroundsAsAntialisedInOneStep(float range){
        if (statement == ORIGINAL) {
            if (range > 0 && range < 1f) {
                createAntialiasedTilesets(range);
                setNewBackgroundsGraphic(UNFOCUSED);
                statement = UNFOCUSED;
                if (Program.debug) System.out.println("Background set as smothes");
            } else System.out.println("Can not set as antialised");
        }
    }

    private void setNewBackgroundsGraphic(boolean type) {
            for (Background background : backgrounds){
                if (background instanceof ScrollableAlongXBackground) {
                    ScrollableAlongXBackground scrollableAlongXBackground = (ScrollableAlongXBackground) background;
                    String path = scrollableAlongXBackground.getPath();
                    if (type == UNFOCUSED){
                        for (Tileset unfocused : unfocusedTilesets){
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
                                System.out.println("Set original tileset");
                                break;
                            }
                        }
                    }
                }
            }

    }

    public void setBackgroundsAsNormalInOneStep(){
        if (statement == UNFOCUSED) {
            setNewBackgroundsGraphic(ORIGINAL);
            statement = ORIGINAL;
            System.out.println("Background set as normal");
        }

    }

    private void createAntialiasedTilesets(float range){
        fillUnfocusedWithOriginal();
        for (Tileset tileset : unfocusedTilesets){
            int originalW = tileset.picture.image.width;
            int originalH = tileset.picture.image.height;
            int newWidth = (int)(originalW*range);
            int newHeight = (int)(originalH*range);
            tileset.picture.image.resize(newWidth, newHeight);
            tileset.picture.image.resize(originalW, originalH);
        }
    }
}
