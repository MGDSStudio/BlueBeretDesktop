package com.mgdsstudio.blueberet.graphic.HUD.window;

import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import org.jbox2d.common.Vec2;

public abstract class Window {
    protected int width, height;
    protected Vec2 upperCentralPoint;
    public final static boolean EMERGENCE_AND_HIDING = true;
    public final static boolean SHOW_AND_HIDE = false;
    protected boolean showMethod = SHOW_AND_HIDE;
    protected static StaticSprite horizontalLine, verticalLine;
    private static boolean lineGraphicWasUploaded;

    protected final static void loadLinesGraphic(){
        if (!lineGraphicWasUploaded){
            ImageZoneFullData horizontalLineData = new ImageZoneFullData(HeadsUpDisplay.mainGraphicSource.getPath(), 763, 1, 773,6);
            horizontalLine = new StaticSprite(horizontalLineData,25,25);
            horizontalLine.getImage();
            ImageZoneFullData verticalLineData = new ImageZoneFullData(HeadsUpDisplay.mainGraphicSource.getPath(), 751, 11, 771,25);
        }
    }
}
