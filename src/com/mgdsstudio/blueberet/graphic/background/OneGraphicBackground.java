package com.mgdsstudio.blueberet.graphic.background;

import android.service.quicksettings.Tile;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.opengl.Texture;

public abstract class OneGraphicBackground extends Background {

    protected Image pictureMustNotBeUsed;  //to parent
    protected Tileset tileset;
    protected String path;
    protected int width, height;



    protected float relativeVelocity;

    protected abstract void loadGraphic();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getRelativeVelocity() {
        return relativeVelocity;
    }

    public Image getPicture() {
        return pictureMustNotBeUsed;
    }

    @Override
    public String getPath() {
        //System.out.println("This func was remade");
        //String shortPath = StringLibrary.getStringAfterPathDevider(path);
        return path;
        //return path;
    }

    @Override
    protected void finalize(){
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        //clearGraphic();
    }

    public void clearGraphic() {
        try {
            Object cacheInBackgrounds = Program.backgroundFrame.getCache(pictureMustNotBeUsed.image);
            if (cacheInBackgrounds instanceof Texture) ((Texture) cacheInBackgrounds).disposeSourceBuffer();
            Program.backgroundFrame.removeCache(pictureMustNotBeUsed.image);
            Program.engine.g.removeCache(pictureMustNotBeUsed.image);
            if (Program.debug){
                System.out.println("Object " + " was deleted from background: " + ", " + (Program.backgroundFrame.getCache(pictureMustNotBeUsed.image)!= null) + ", " + (Program.engine.g.getCache(pictureMustNotBeUsed.image)!= null));
            }
        }
        catch (Exception e){
            System.out.println("PImage is not in memory");
            e.printStackTrace();
        }
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }
}

