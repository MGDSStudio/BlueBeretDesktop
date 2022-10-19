package com.mgdsstudio.blueberet.graphic.HUD.window;

import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import org.jbox2d.common.Vec2;

public class SimpleWindow extends NotOpenableGraphicElement {
    private static StaticSprite sprite;
    private static boolean graphicLoaded;

    public SimpleWindow(Vec2 upperCenterPoint, int width, int height){
        this.upperCentralPoint = upperCenterPoint;
        this.width = width;
        this.height = height;
        //ImageZoneFullData imageZoneFullData = new ImageZoneFullData(HeadsUpDisplay.mainGraphicSource.getPath(), );
        //loadGraphic(imageZoneFullData);
    }

    private void loadGraphic(ImageZoneFullData imageZoneFullData){

        //sprite = new StaticSprite(width, height);
    }
}
