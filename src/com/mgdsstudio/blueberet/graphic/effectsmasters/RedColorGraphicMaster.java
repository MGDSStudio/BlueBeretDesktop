package com.mgdsstudio.blueberet.graphic.effectsmasters;

import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class RedColorGraphicMaster extends GraphicEffectMaster{
    public final static float NORMAL_RED_VALUE = 0.25f;
    private final float maxRange = 0.25f;
    private final float minRange = 0;
    private boolean USE_RED_COLOR_CHANGING = false;

    private final ArrayList<IndependentOnScreenStaticSprite> independentOnScreenStaticSprites;


    public RedColorGraphicMaster(ArrayList<IndependentOnScreenStaticSprite> independentOnScreenStaticSprites) {
        this.independentOnScreenStaticSprites = independentOnScreenStaticSprites;
        basicTilesets = new ArrayList<>();
        secondaryTilesets = new ArrayList<>();
        fillData();
        //if (Program.OS == Program.DESKTOP) USE_RED_COLOR_CHANGING = false;
    }

    protected void fillData() {
        for (IndependentOnScreenStaticSprite background : independentOnScreenStaticSprites){
            Tileset tileset = background.staticSprite.getTileset();
            if (!basicTilesets.contains(tileset)){
                basicTilesets.add(tileset);
            }
        }
    }

    protected void setNewGraphic(boolean type) {
        for (IndependentOnScreenStaticSprite background : independentOnScreenStaticSprites){
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

    @Override
    protected void applyEffectToTilesets(float range){
        fillSecondaryTilesetsWithOriginal();
        float valueMustBeAdded = 255f*range;
        int length = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        int count = 0;
        for (Tileset tileset : secondaryTilesets){
            try {
                if (tileset.getPicture().image != null) {
                    tileset.getPicture().image.loadPixels();
                    int[] pixels = tileset.getPicture().image.pixels;
                    length = pixels.length;
                    for (int i = 0; i < length; i++) {
                        alpha = (pixels[i] >> 24) & 0xFF;
                        if (alpha > 205) {
                            red = (pixels[i] >> 16) & 0xFF;
                            green = (pixels[i] >> 8) & 0xFF;
                            blue = pixels[i] & 0xFF;


                            green-=valueMustBeAdded;
                            blue-=valueMustBeAdded;
                            red += valueMustBeAdded;
                            if (red > 255) red = 255;
                            if (blue<0) blue = 0;
                            if (green < 0) green = 0;
                            //pixels[i] = Program.engine.color(red, green, blue, alpha);
                            //pixels[i] = alpha | red | green | blue;
                            pixels[i] = Program.engine.color(red, green, blue, alpha);
                            count++;
                        }
                    }
                    tileset.getPicture().image.updatePixels();
                }
                else System.out.println("Tileset is null");

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("We have " + count + " changed pixels");
    }

    public void setColorRelativeToLife(int startValue, int newLife, int maxLife) {
        if (USE_RED_COLOR_CHANGING){
            if (newLife == 0){
                applyEffectToGraphic(maxRange);
            }
            else if (newLife == maxLife){
                setNewGraphic(!WITH_EFFECT);
            }
            else {
                float range = maxRange*newLife/maxLife;
                applyEffectToGraphic(range);
            }
        }

    }
}
