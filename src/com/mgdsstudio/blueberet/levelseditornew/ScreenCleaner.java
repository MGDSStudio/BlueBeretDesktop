package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.engine.nesgui.*;
import processing.core.PConstants;
import processing.core.PGraphics;

class ScreenCleaner {
    private final ImageZoneSimpleData blackData;
    private final Image graphicSource;
    private final Frame mapZone;

    public ScreenCleaner(Frame rectangle, Tab tab) {
        graphicSource = tab.getGraphicFile();
        this.mapZone = rectangle;
        blackData = new ImageZoneSimpleData(109,266,110,267);
    }

    void clearFrame(PGraphics graphics){
        graphics.pushStyle();
        graphics.clear();
        graphics.imageMode(PConstants.CORNER);

        graphics.image(graphicSource.getImage(),0,0, mapZone.getLeftX(), graphics.height, blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //Right
        graphics.image(graphicSource.getImage(),mapZone.getLeftX()+mapZone.getWidth(),0, graphics.width-(mapZone.getLeftX()+mapZone.getWidth()), graphics.height, blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //upper
        graphics.image(graphicSource.getImage(),mapZone.getLeftX(),0, mapZone.getWidth(), mapZone.getUpperY(), blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        //lower
        graphics.image(graphicSource.getImage(),mapZone.getLeftX(),mapZone.getUpperY()+mapZone.getHeight(), mapZone.getWidth(), graphics.height-(mapZone.getUpperY()+mapZone.getHeight()), blackData.leftX, blackData.upperY, blackData.rightX, blackData.lowerY);
        graphics.popStyle();


    }
}
