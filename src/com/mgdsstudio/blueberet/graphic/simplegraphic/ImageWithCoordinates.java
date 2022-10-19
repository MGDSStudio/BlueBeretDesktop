package com.mgdsstudio.blueberet.graphic.simplegraphic;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import processing.core.PGraphics;

public class ImageWithCoordinates extends ImageWithData{
    private Coordinate pos;

    public ImageWithCoordinates(Image image, int width, int height, ImageZoneSimpleData imageZoneSimpleData, Coordinate pos) {
        super(image, width, height, imageZoneSimpleData);
        this.pos = pos;
    }

    public ImageWithCoordinates(Image image, int width, int height, Coordinate pos) {
        super(image, width, height, new ImageZoneSimpleData(0,0,image.image.width, image.image.height));
        this.pos = pos;
    }

    @Override
    public void draw(PGraphics graphics){
        graphics.image(image.getImage(), pos.x,pos.y,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
    }


    public void draw(PGraphics graphics, int anchorX, int anchorY){
        graphics.image(image.getImage(), pos.x+anchorX,pos.y+anchorY,width, height, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
    }

    public float getPosX() {
        return pos.x;
    }

    public float getPosY() {
        return pos.y;
    }

    public Coordinate getCoordinate() {
        return pos;
    }

}
