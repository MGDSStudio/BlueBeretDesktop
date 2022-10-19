package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;

public class IndependentOnScreenPixel extends SingleGameElement implements IDrawable, ILayerable{

    public static final String CLASS_NAME = "IndependentOnScreenPixel";
    private final static String objectToDisplayName = "Pixel";
    private static Tileset tileset;
    // Encoded data string
    //Fields
    private ImageZoneSimpleData graphicData;
    private final int xPos, yPos, dim;
    private int colorX, colorY;
    private int alpha;
    private boolean withTint;
    private int tintColor;
    private int layer;

    private final ImageZoneSimpleData dataField = new ImageZoneSimpleData(75,210, 107, 242);

    public IndependentOnScreenPixel(int xPos, int yPos, int dim, int colorX, int colorY, int alpha, int layer) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.dim = dim;
        this.colorX = colorX;
        this.colorY = colorY;
        this.layer = layer;
        initGraphic(colorX, colorY);
        initAlpha(alpha);
        //System.out.println("New pixel was created");
    }

    public IndependentOnScreenPixel(IndependentOnScreenPixel template) {
        this.xPos = template.getxPos();
        this.yPos = template.getyPos();
        this.dim = template.getDim();
        this.graphicData = template.getGraphicData();
        this.colorX = template.getColorX();
        this.colorY = template.getColorY();
        this.layer = template.getLayer();
        initAlpha(template.getAlpha());
    }

    private void initAlpha(int alpha) {
        if (alpha < 255 && alpha >= 0) {
            this.alpha = alpha;
            withTint = true;
        }
        else this.alpha = 255;
        tintColor = Program.engine.color(255, this.alpha);
        System.out.println("With " + withTint + " alpha " + this.alpha);
    }

    private void initGraphic(int colorX, int colorY) {
        final int COLORS = 256;
        int colorsAlongX = (int) PApplet.sqrt(COLORS);
        if (colorX >  (colorsAlongX-1) || colorY >  (colorsAlongX-1)) {
            colorX = 0;
            colorY = 0;
        }
        final ImageZoneSimpleData sourceField = new ImageZoneSimpleData(75,210, 107, 242);
        int sourceDim = ((sourceField.rightX - sourceField.leftX)/ colorsAlongX);
        int xLeft = colorX*sourceDim;
        int yUpper = colorY*sourceDim;
        graphicData = new ImageZoneSimpleData(sourceField.leftX+xLeft, sourceField.upperY+yUpper, sourceField.leftX+xLeft+1,sourceField.upperY+yUpper+1);
        if (tileset == null) tileset = HeadsUpDisplay.mainGraphicTileset;
    }

    protected boolean isVisibleOnScreen(GameCamera gameCamera, float x, float y) {
        boolean isVisible = GameMechanics.isIntersectionBetweenAllignedRects(gameCamera.getActualPosition(), x, y, gameCamera.getVisibleZoneWidth(), gameCamera.getVisibleZoneHeight(), dim, dim);
        return isVisible;
    }

    public static void loadSprite(Tileset newTileset) {
        if (tileset == null){
            if (newTileset!= null) {
                tileset = newTileset;
            }
            else loadSprite();
        }
    }

    public static void loadSprite() {
        if (tileset == null){
            tileset = HeadsUpDisplay.mainGraphicTileset;
        }
    }

    @Override
    public void draw(GameCamera gameCamera) {
        if (Program.WITH_GRAPHIC && tileset != null && isVisibleOnScreen(gameCamera, xPos, yPos)) {
            Program.objectsFrame.pushStyle();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (withTint) Program.objectsFrame.tint(tintColor);
            Program.objectsFrame.image(tileset.picture.image, xPos - gameCamera.getActualXPositionRelativeToCenter(), yPos - gameCamera.getActualYPositionRelativeToCenter(), dim, dim, graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY);
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popStyle();
        }
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    public static String getPath() {
        return tileset.picture.getPath();
    }

    @Override
    public String getStringData() {
        //IndependentOnScreenPixel 1:xPos, yPox, width, colorX, colorY,alpha
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createPixel();
        System.out.println("Pixel has data string: " + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getDim() {
        return dim;
    }

    public int getAlpha() {
        return alpha;
    }

    public ImageZoneSimpleData getGraphicData() {
        return graphicData;
    }

    public int getColorX() {
        return colorX;
    }

    public int getColorY() {
        return colorY;
    }

    public void setColorX(int colorX) {
        this.colorX = colorX;
        initGraphic(colorX, colorY);
    }

    public void setColorY(int colorY) {
        this.colorY = colorY;
        initGraphic(colorX, colorY);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        initAlpha(alpha);
    }

    public int getLayer() {
        return layer;
    }
}
