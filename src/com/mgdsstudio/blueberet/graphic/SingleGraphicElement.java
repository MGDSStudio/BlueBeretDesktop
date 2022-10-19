package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.SingleImageDataForFigures;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public abstract class SingleGraphicElement {
    protected boolean visible = true;
    protected Tileset tileset;
    protected String path;
    protected int width, height;
    protected int parentElementWidth, parentElementHeight;
    protected boolean withTint = false;
    protected boolean withSelectionTint = false;
    protected int tintColor;
    protected int additionalAngle = 0;
    protected float boundingWidth = width, boundingHeight = height;

    final public static boolean STATIC_SPRITE_GRAPHIC_TYPE = false;
    final public static boolean SPRITE_ANIMATION_TYPE = true;
    protected int xLeft, yLeft, xRight, yRight;
    protected Vec2 pixelsSpritesShifting;
    public static final byte CLEAR_RECT = 1;

    //Mutable
    private final static boolean VISIBLE = true;
    private boolean lastVisibleStatement = VISIBLE;
    public final static int VISIBILITY_UPDATING_FREQUENCY = 2;

    public boolean isFillAreaWithSprite() {
        return fillAreaWithSprite;
    }

    protected boolean fillAreaWithSprite = false; // when false the sprite is drawn only once; when true: to complete fill the area

    public int getxLeft(){
        return xLeft;
    }

    public int getyLeft(){
        return yLeft;
    }

    public int getxRight(){
        return xRight;
    }

    public int getyRight(){
        return yRight;
    }

    public String getPath(){
        return path;
    }

    public int getWidth(){ return width; }

    public int getParentElementWidth() {
        if (parentElementWidth>0)        return parentElementWidth;
        else return width;
    }

    public int getParentElementHeight() {
        if (parentElementHeight>0)return parentElementHeight;
        else return height;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setSpritesShifting(Vec2 pixelsSpritesShifting){
        this.pixelsSpritesShifting = pixelsSpritesShifting;
    }

    public void setTint(int tintColor) {
        if (!withTint) withTint = true;
        this.tintColor = tintColor;
    }

    public void setMomochromeTint(int white, int alpha) {
       tintColor = Program.engine.color(white, alpha);
       if (!withTint) withTint = true;
    }

    public int getTintColor() {
        return tintColor;
    }

    public void resetTint(){
        tintColor = Program.engine.color(255, 255);
        if (withTint) withTint = false;
        //System.out.println("Tint was reset");
    }

    public Image getImage(){
        return tileset.picture;
    }

    public void setTileset(Tileset tileset) {
        this.tileset =tileset;
    }


    public void setAdditionalAngle(int additionalAngle){
        this.additionalAngle = additionalAngle;
    }

    public void setTextureData(GraphicDataForFigures textureData) {
        xLeft = textureData.getVertexes()[0];
        yLeft = textureData.getVertexes()[1];
        xRight = textureData.getVertexes()[2];
        yRight = textureData.getVertexes()[3];
        System.out.println("New data: ");
        for (int i = 0; i < textureData.getVertexes().length; i++){
            System.out.print(textureData.getVertexes()[0] + "x");
        }
        System.out.println("; end");
        if (textureData.getClass() == SingleImageDataForFigures.class){
            SingleImageDataForFigures data = (SingleImageDataForFigures) textureData;
            Image image = data.getImage();
        }
        else {
            System.out.println("Is not used!");
        }
    }

    public void setPath(String name) {
        this.path = name;
    }

    protected void initBoundingDimensions(float boundingWidth, float boundingHeight){
        this.boundingWidth = boundingWidth;
        this.boundingHeight = boundingHeight;
    }



    //public void draw(GameCamera gameCamera, PVector pos, float a) {
    public abstract void draw(GameCamera gameCamera, Vec2 drawPosition, float i);

    protected boolean isVisibleOnScreen(GameCamera gameCamera, Vec2 graphicElementCenter){
        return isVisibleOnScreen(gameCamera, graphicElementCenter.x, graphicElementCenter.y);
    }

    protected boolean isVisibleOnScreen(GameCamera gameCamera, float x, float y) {
        if (!visible) return false;
        else {
            boolean isVisible = GameMechanics.isIntersectionBetweenAllignedRects(gameCamera.getActualPosition(), x, y, gameCamera.getVisibleZoneWidth(), gameCamera.getVisibleZoneHeight(), boundingWidth, boundingHeight);

            return isVisible;
        }
    }




    protected boolean isVisibleOnScreen(GameCamera gameCamera, float x, float y, float a) {
        boolean isVisible = GameMechanics.isIntersectionBetweenAllignedRects(gameCamera.getActualPosition(), x, y, gameCamera.getVisibleZoneWidth(), gameCamera.getVisibleZoneHeight(), boundingWidth, boundingHeight);
        return isVisible;
    }


    public Tileset getTileset() {
        return tileset;
    }

    public void hide() {
        visible = false;
    }

    public void makeVisible() {
        visible = true;

    }
}
