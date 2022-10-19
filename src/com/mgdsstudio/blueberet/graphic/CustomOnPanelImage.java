package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import processing.core.PConstants;
import processing.core.PGraphics;

public class CustomOnPanelImage {
    protected static Tileset tilesetWithMainGraphicFile;
    protected int width, height;
    protected int arrowWidth, arrowHeight;
    protected int basicPosX, basicPosY, imagePosY, arrowPosY;
    protected ImageZoneSimpleData imageZoneData;
    protected int type;
    private String text;
    private boolean withText;
    private int nominalWidth, nominalHeight;
    private final float textHeightCoef = 0.7f;
    private int textAbsPosY;
    private float xDimCoef, yDimCoef;
    private float xNomDimCoef, yNomDimCoef;
    private final int cellWidth,  cellHeight;
    private final ImageZoneSimpleData arrowData;
    public CustomOnPanelImage(int code, Tileset tileset, int width, int height, int posX, int posY){
        this.type = code;
        this.cellWidth = width;
        this.cellHeight = height;
        arrowData = HUD_GraphicData.listArrow;
        imageZoneData = HUD_GraphicData.getImageZoneForObjectType(code, false);
        tilesetWithMainGraphicFile = tileset;
        this.basicPosX = posX;
        this.basicPosY = posY;
        initNominalDimensions(width, height);
        imagePosY = posY;
        initDimensions(width,height);
        //initDimensionsForWithTextDrawing(width,height);
        withText = false;
        System.out.println("Panel image with code " + type + "  added without text");
        initArrowDataWithoutText();
    }

    private void initArrowDataWithoutText() {

        arrowWidth = (int) ((float)cellWidth*0.65f);
        float coef = (float)(arrowData.rightX-arrowData.leftX)/arrowWidth;
        arrowHeight = (int) ((float)(arrowData.lowerY-arrowData.upperY)/coef);
        arrowPosY = (int) ((basicPosY+cellHeight-textAbsPosY)+textAbsPosY)-3*arrowHeight;
    }

    public CustomOnPanelImage(int code, Tileset tileset, int width, int height, int posX, int posY, String text){
        this.type = code;
        arrowData = HUD_GraphicData.listArrow;
        this.cellWidth = width;
        this.cellHeight = height;
        imageZoneData = HUD_GraphicData.getImageZoneForObjectType(code, false);
        tilesetWithMainGraphicFile = tileset;
        this.basicPosX = posX;
        this.basicPosY = posY;
        initPos(posY);

        initNominalDimensions(width, height);
        this.text= text;
        initDimensionsForWithTextDrawing(width,height);
        withText = true;
        System.out.println("Panel image with code " + type + "  added with text");
    }

    private void initPos(int posY){
        imagePosY = (int) (posY-(height*0.25f));
    }

    private void initDimensionsForWithTextDrawing(int width, int height){
        float normalTextHeight = HeadsUpDisplay.graphic.textSize;
        System.out.println("Text size: " + normalTextHeight);
        int updatedHeight = (int) (height-normalTextHeight*1.01f);
        //textRelativePosY = (int) ((normalTextHeight*0.95f)/2f+height/2);
        textAbsPosY = (int) (basicPosY+height/4f);
        //textAbsPosY = basicPosY+height/2;
        imagePosY -=((normalTextHeight*1.01f)/2f);

        //basicPosY -=((normalTextHeight*1.15f)/2f);
        //initNominalDimensions(width, height);
        initDimensions(width, updatedHeight);
        initArrowDataWithText();
    }

    private void initArrowDataWithText() {

        arrowWidth = (int) ((float)cellWidth*0.65f);
        float coef = (float)(arrowData.rightX-arrowData.leftX)/arrowWidth;
        arrowHeight = (int) ((float)(arrowData.lowerY-arrowData.upperY)/coef);
        arrowPosY = (int) ((basicPosY+cellHeight-textAbsPosY)+textAbsPosY)-2*arrowHeight;
    }

    protected void initNominalDimensions(int width, int height){
        float sourceX = imageZoneData.rightX-imageZoneData.leftX;
        float sourceY  = imageZoneData.lowerY-imageZoneData.upperY;
        float dimCoef = sourceY/sourceX;
        if (sourceX>sourceY){
            this.nominalWidth = width;
            this.nominalHeight = (int) (dimCoef*height);
        }
        else if (sourceY>sourceX){
            this.nominalHeight = height;
            this.nominalWidth = (int) (width/dimCoef);
        }
        else {
            this.nominalWidth = width;
            this.nominalHeight = height;
        }
        xDimCoef = (float)width/(imageZoneData.rightX-imageZoneData.leftX);
        yDimCoef = (float)height/(imageZoneData.lowerY-imageZoneData.upperY);
        xNomDimCoef = (float)nominalWidth/(imageZoneData.rightX-imageZoneData.leftX);
        yNomDimCoef = (float)nominalHeight/(imageZoneData.lowerY-imageZoneData.upperY);
        //System.out.println("Nominal dims for code: " + type + " is " + nominalWidth + "x" + nominalHeight);
    }

    protected void initDimensions(int width, int height){
        float sourceX = imageZoneData.rightX-imageZoneData.leftX;
        float sourceY  = imageZoneData.lowerY-imageZoneData.upperY;
        float dimCoef = sourceY/sourceX;
        if (sourceX>sourceY){
            this.width = width;
            this.height = (int) (dimCoef*height);
        }
        else if (sourceY>sourceX){
            this.height = height;
            this.width = (int) (width/dimCoef);
        }
        else {
            this.width = width;
            this.height = height;
        }
    }

    public int getBasicPosX() {
        return basicPosX;
    }

    public void setBasicPosX(int basicPosX) {
        this.basicPosX = basicPosX;
    }

    public int getBasicPosY() {
        return basicPosY;
    }

    public void setBasicPosY(int basicPosY) {
        this.basicPosY = basicPosY;
    }

    /*
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
*/


    public void drawWithNumber(PGraphics graphics){
        graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, imagePosY, width, height, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
        //graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, imagePosY, width, height, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
        //
        //System.out.println("Drawn area: " + imageZoneData.leftX + "x" + imageZoneData.upperY + " to "+  imageZoneData.rightX + "x" + imageZoneData.lowerY);
        //System.out.println("Drawn with dims: " + width + "x" + height + " by nominal dims: " + nominalWidth + "x" + nominalHeight);
        //System.out.println("Drawn with dims: " + width + "x" + height + " by nominal dims: " + nominalWidth + "x" + nominalHeight);
        if (imageZoneData.equals(HUD_GraphicData.soShotgun)){
            //System.out.println("Must be drawn with text: " + withText + " : " + text);
        }
        if (withText) {
            graphics.pushStyle();
            graphics.textSize = graphics.textSize*textHeightCoef;
            graphics.textAlign(PConstants.CENTER, PConstants.CENTER);
            graphics.text(text, basicPosX, textAbsPosY);
            //System.out.println("New text: " + this.text);
            graphics.popStyle();
        }

    }

    public void setTileset(Tileset tileset){
        tilesetWithMainGraphicFile = tileset;
    }

    public void setTileset(Image image){

        tilesetWithMainGraphicFile = new Tileset(image);
    }

    public void drawArrow(PGraphics graphics){
        if (withText) {
            graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, arrowPosY, arrowWidth, arrowHeight, arrowData.leftX, arrowData.upperY, arrowData.rightX, arrowData.lowerY);
        }
        else {
            graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, arrowPosY, arrowWidth, arrowHeight, arrowData.leftX, arrowData.upperY, arrowData.rightX, arrowData.lowerY);

        }
        //System.out.println("Tileset is null " + (tilesetWithMainGraphicFile == null) + " arrow data: " + (arrowData == null));
        //System.out.println("Arrow was drawn at " + basicPosX + "x" + basicPosY + " width: " + arrowWidth + "x " + arrowHeight + " and pos: " + arrowData.leftX);

        //graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, arrowPosY, arrowWidth, arrowHeight, arrowData.leftX, arrowData.upperY, arrowData.rightX, arrowData.lowerY);

        //System.out.println("Arrow was drawn at " + basicPosX + "x" + arrowPosY + " width dims: " + arrowWidth + "x" + arrowHeight);
    }

    public void drawWithoutNumber(PGraphics graphics){
        if (graphics.imageMode == PConstants.CENTER) graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX, basicPosY, nominalWidth, nominalHeight, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
        else graphics.image(tilesetWithMainGraphicFile.picture.image, basicPosX-nominalWidth/2, basicPosY-nominalHeight/2, nominalWidth, nominalHeight, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
    }


    /*
    public void drawWithNumber(PGraphics graphics, int number, float textRelativeShiftX, float textRelativeShiftY){
        graphics.image(tilesetWithMainGraphicFile.picture.image, posX+shiftingX, posY+shiftingY, width, height, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
        graphics.text(Integer.toString(number), posX+textRelativeShiftX, posY+textRelativeShiftY);
    }
     */

    public int getType(){
        return type;
    }

    public static Tileset getTilesetWithMainGraphicFile() {
        return tilesetWithMainGraphicFile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageZoneSimpleData getImageZoneData() {
        return imageZoneData;
    }

    public boolean isWithText() {
        return withText;
    }

    public void setText(String text) {
        this.text = text;
        if (!withText) {
            withText = true;
            initDimensionsForWithTextDrawing(this.width, this.height);
        }

    }

    public void updateDims() {
        //width = nominalWidth;
        //imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY
        /*
        (imageZoneData.rightX-imageZoneData.leftX);
        yDimCoef = (float)height/(imageZoneData.lowerY-imageZoneData.upperY);
         */
        /*
        System.out.print("Old dims: " + width + "x" + height);
        width = (int) ((imageZoneData.rightX-imageZoneData.leftX)*xDimCoef);
        height = (int) ((imageZoneData.lowerY-imageZoneData.upperY)*yDimCoef);

        nominalWidth = (int) ((imageZoneData.rightX-imageZoneData.leftX)*xNomDimCoef);
        nominalHeight = (int) ((imageZoneData.lowerY-imageZoneData.upperY)*yNomDimCoef);
        System.out.println(". New dims: " + width + "x" + height);

         */
    }
}
