package com.mgdsstudio.blueberet.graphic.simplegraphic;

import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ImageWithData {
    protected Image image;
    protected int width = -1;
    protected int height = -1;
    protected ImageZoneSimpleData imageZoneSimpleData;
    protected boolean flip;
    private boolean withoutImage;

    public ImageWithData(Image image, ImageZoneSimpleData imageZoneSimpleData, float graphicScale) {
        this.image = image;
        loadImageZoneSimpleData(imageZoneSimpleData);
        if (!withoutImage) calculateDims(graphicScale);
    }

    private void loadImageZoneSimpleData(ImageZoneSimpleData imageZoneSimpleData){
        if (imageZoneSimpleData != null) {
            this.imageZoneSimpleData = imageZoneSimpleData;
        }
        else {
            withoutImage = true;
        }
    }

    public ImageWithData(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData) {
        this.image = image;
        this.width = width;
        this.height = height;
        loadImageZoneSimpleData(imageZoneSimpleData);
    }

    private void calculateDims(float graphicScale) {
        int basicWidth = imageZoneSimpleData.rightX- imageZoneSimpleData.leftX;
        int basicHeight = imageZoneSimpleData.lowerY- imageZoneSimpleData.upperY;
        this.width = (PApplet.abs((int)(basicWidth*graphicScale)));
        this.height = (PApplet.abs((int)(basicHeight*graphicScale)));
        if (graphicScale < 0) flip = true;
    }

    public void draw(PGraphics graphics){
        //System.out.println("Flip is: ");
        if (flip){
            graphics.scale(-1f, 1f);
        }
        graphics.image(image.getImage(), 0,0,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        if (flip) graphics.scale(1,1);
    }

    /*public void draw(PGraphics graphics, float angleInDegrees){
        if (flip){
            graphics.scale(-1f, 1f);
        }
        graphics.rotate(PApplet.radians(angleInDegrees));
        graphics.image(image.getImage(), 0,0,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        if (flip) graphics.scale(1,1);
    }*/

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage(){
        return image;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public void reverseFlip() {
        if (flip == true) flip = false;
        else flip = true;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean hasImage() {
        return !withoutImage;
    }
}
