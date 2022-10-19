package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PImage;

public class Tileset implements Cloneable{
    //ArrayList<Image> image = new ArrayList<Image>();
    private int tilesetNumber;
    private final String path;
    protected Image picture;
    //private Image pictureWithTint;
    private byte spritesAlongX;
    private byte spritesAlongY;
    private float elementWidth;
    private float elementHeight;
    protected int tintForNextFrame = -1;
    private long frameOnWhichNewTintWasSet = -1;

    public Tileset(Image picture) {
        this.path = picture.getPath();
        this.picture = picture;
    }

    public Image getPicture() {

        return picture;
    }

    public Tileset(int tilesetNumber, Image picture, byte spritesAlongX, byte spritesAlongY){
        this.path = picture.getPath();
        this.tilesetNumber = tilesetNumber;
        this.spritesAlongX = spritesAlongX;
        this.spritesAlongY = spritesAlongY;
        this.picture = picture;
        if (picture == null){
            System.out.println("This picture is null");
        }

        if (spritesAlongX >= 1) elementWidth = picture.image.width/spritesAlongX;
        else elementWidth = picture.image.width/1;
        if (spritesAlongY >= 1) elementHeight = picture.image.height/spritesAlongY;
        else elementHeight = picture.image.height/1;
    }

    public Tileset(int tilesetNumber, String path, byte spritesAlongX, byte spritesAlongY){
        this.path = path;
        this.tilesetNumber = tilesetNumber;

        this.spritesAlongX = spritesAlongX;
        this.spritesAlongY = spritesAlongY;


        try{
            picture = new Image(Program.getAbsolutePathToAssetsFolder(path));
            if (picture == null){
                System.out.println("Is null");
            }

            elementWidth = picture.image.width/spritesAlongX;
            elementHeight = picture.image.height/spritesAlongY;


        }
        catch(Exception e){
            System.out.println("Can not load this picture " + path);
            System.out.println(e);
            picture = new Image(path);

            elementWidth = picture.image.width;
            elementHeight = picture.image.height;


        }
    }

    public Tileset(int tilesetNumber, String path){
        this.path = path;
        this.tilesetNumber = tilesetNumber;
        this.spritesAlongX = 1;
        this.spritesAlongY = 1;
        try{
            picture = new Image(path);
            if (picture == null){
                System.out.println("Is null");
            }
            elementWidth = picture.image.width;
            elementHeight = picture.image.height;


        }
        catch(Exception e){
            try {
                picture = new Image(Program.getAbsolutePathToAssetsFolder(path));
                elementWidth = picture.image.width;
                elementHeight = picture.image.height;


                System.out.println("Tileset was loaded after second trying"  + tilesetNumber + " is null " + (picture == null));
            }
            catch (Exception e1){
                picture = new Image(Program.getAbsolutePathToAssetsFolder("BlackRect.png"));
                elementWidth = picture.image.width;
                elementHeight = picture.image.height;
                System.out.println("Tileset can not be loaded second trying"  + tilesetNumber + " is null ");
            }

        }
    }

    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getTilesetNumber() {
        return tilesetNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPicture(Image image) {
        this.picture = image;
    }

    public void setAlpha(int alpha) {
        picture.image.loadPixels();
        int red = 0;
        int green = 0;
        int blue = 0;
        int actualAlpha = 255;
        int [] pixels = picture.image.pixels;
        int count = 0;
        for (int i = 0; i < pixels.length; i++){
            actualAlpha = (int) Program.engine.alpha(pixels[i]);
            if (actualAlpha >= 254){
                red = (int) Program.engine.red(pixels[i]);
                green = (int) Program.engine.green(pixels[i]);
                blue = (int) Program.engine.blue(pixels[i]);
                pixels[i] = Program.engine.color(red, green, blue, alpha);
                count++;
            }
        }
        System.out.println(count + " pixels were recolored ");
        picture.image.updatePixels();
    }

    public void setAlphaForColors(int alpha, int[] colorsToBeSaved) {
        picture.image.loadPixels();
        int red = 0;
        int green = 0;
        int blue = 0;
        int actualAlpha = 255;
        int [] pixels = picture.image.pixels;
        int count = 0;
        boolean mustBeLeaved = false;
        for (int i = 0; i < pixels.length; i++){
            for (int j = 0; j < colorsToBeSaved.length; j++){
                if (pixels[i] == colorsToBeSaved[j]){
                    mustBeLeaved = true;
                }
            }
            if (!mustBeLeaved) {
                actualAlpha = (int) Program.engine.alpha(pixels[i]);
                if (actualAlpha >= 252) {
                    red = (int) Program.engine.red(pixels[i]);
                    green = (int) Program.engine.green(pixels[i]);
                    blue = (int) Program.engine.blue(pixels[i]);
                    pixels[i] = Program.engine.color(red, green, blue, alpha);
                    count++;
                }
            }
            mustBeLeaved = false;
        }
        System.out.println(count + " pixels were recolored ");
        picture.image.updatePixels();
    }

    private void setTintForReservedTileset(int redNew, int greenNew, int blueNew, int alphaNew) {
        picture.image.loadPixels();

        int actualAlpha = 255;
        int [] pixels = picture.image.pixels;
        int count = 0;
        for (int i = 0; i < pixels.length; i++){
            actualAlpha = (int) Program.engine.alpha(pixels[i]);
            if (actualAlpha >= 254){
                pixels[i] = Program.engine.color(redNew, greenNew, blueNew, alphaNew);
                count++;
            }
        }
        picture.image.updatePixels();
    }



    public void setTintForNextFrame(int i, int i1, int i2, int i3) {
        tintForNextFrame = Program.engine.color(i, i1, i2, i3);
        frameOnWhichNewTintWasSet = Program.engine.frameCount+1;

    }

    public boolean hasTintForNextFrame(){
        if (tintForNextFrame >= 0){
            if (frameOnWhichNewTintWasSet == Program.engine.frameCount){
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void resetTintForNextFrame(){
        frameOnWhichNewTintWasSet = -1;
        tintForNextFrame = -1;
    }

    public void makeSpriteWithSpecificTintWhite(){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = picture.image;
        PImage hittedImage = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(getPath(), hittedImage);
        picture = image;
        int arrayLength = hittedImage.width*hittedImage.height;
        hittedImage.loadPixels();
        picture.image.loadPixels();
        source.loadPixels();
        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    hittedImage.loadPixels();
                    hittedImage.pixels[i] = Program.engine.color(255);
                    hittedImage.updatePixels();
                } else {
                    hittedImage.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        hittedImage.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){

            System.out.println("Can not create white tileset. Sources pixels array is null");
        }
    }

    public void makeSpriteWithSpecificTint(float coefRed, float coefGreen, float coefBlue){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = picture.image;
        PImage imageToBeRedrawn = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(getPath(), imageToBeRedrawn);
        picture = image;
        int arrayLength = imageToBeRedrawn.width*imageToBeRedrawn.height;
        imageToBeRedrawn.loadPixels();
        picture.image.loadPixels();
        source.loadPixels();

        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    imageToBeRedrawn.loadPixels();
                    int actualRed = (int) Program.engine.red(color);
                    int actualGreen = (int) Program.engine.red(color);
                    int actualBlue = (int) Program.engine.red(color);

                    actualRed*=coefRed;
                    actualGreen*=coefGreen;
                    actualBlue*=coefBlue;

                    imageToBeRedrawn.pixels[i] = Program.engine.color(actualRed, actualGreen, actualBlue);
                    imageToBeRedrawn.updatePixels();
                } else {
                    imageToBeRedrawn.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        imageToBeRedrawn.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){

            System.out.println("Can not create white tileset. Sources pixels array is null");
        }

    }

    public void makeSpriteWithSpecificTintWithCorrectColorMultiplication(float coefRed, float coefGreen, float coefBlue){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = picture.image;
        PImage imageToBeRedrawn = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(getPath(), imageToBeRedrawn);
        picture = image;
        int arrayLength = imageToBeRedrawn.width*imageToBeRedrawn.height;
        imageToBeRedrawn.loadPixels();
        picture.image.loadPixels();
        source.loadPixels();

        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    imageToBeRedrawn.loadPixels();
                    int actualRed = (int) Program.engine.red(color);
                    int actualGreen = (int) Program.engine.red(color);
                    int actualBlue = (int) Program.engine.red(color);
                    actualRed*=coefRed;
                    actualGreen*=coefGreen;
                    actualBlue*=coefBlue;

                    imageToBeRedrawn.pixels[i] = Program.engine.color(actualRed, actualGreen, actualBlue);
                    imageToBeRedrawn.updatePixels();
                } else {
                    imageToBeRedrawn.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        imageToBeRedrawn.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){

            System.out.println("Can not create white tileset. Sources pixels array is null");
        }

    }


    public void makeSpriteWithSpecificColor(int red, int green, int blue){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = picture.image;
        PImage hittedImage = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(getPath(), hittedImage);
        picture = image;
        int arrayLength = hittedImage.width*hittedImage.height;
        hittedImage.loadPixels();
        picture.image.loadPixels();
        source.loadPixels();
        float coefRed = 1f;
        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    hittedImage.loadPixels();
                    hittedImage.pixels[i] = Program.engine.color(red, green, blue);
                    hittedImage.updatePixels();
                } else {
                    hittedImage.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        hittedImage.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){

            System.out.println("Can not create white tileset. Sources pixels array is null");
        }

    }
}
