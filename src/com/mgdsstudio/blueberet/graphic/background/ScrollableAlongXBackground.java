package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.*;

public class ScrollableAlongXBackground extends OneGraphicBackground{
    private final String objectToDisplayName = "Scrollable picture background";
    //private Vec2 prevCameraPos;
    //private final static float MAXIMAL_CAMERA_DELTA_POS = 0.5f;
    private boolean withUpperLine = true;
    private boolean withLowerLine = true;
    //private PImage upperLine, lowerLine;
    private ImageZoneSimpleData imageZoneSimpleDataForBackground, imageZoneForUpperLine, imageZoneForLowerLine;
    private int maxPicturesAlongX = 1;
    private final int ADDITIONAL_X_AREA_WIDTH = (int) (Program.engine.width*0.1f);

    public ScrollableAlongXBackground (String path, int width, int height, int leftUpperX, int leftUpperY, float relativeVelocity, boolean withUpperLine, boolean withLowerLine, ImageZoneSimpleData imageZoneSimpleDataForBackground){
        this.path = Program.getAbsolutePathToAssetsFolder(path);
        this.width = width;
        this.height = height;
        this.leftUpperX = leftUpperX;
        this.leftUpperY = leftUpperY;
        this.relativeVelocity = relativeVelocity;
        this.withLowerLine = withLowerLine;
        this.withUpperLine = withUpperLine;
        //System.out.println("X" + leftUpperX + "; Y: " + leftUpperY + "; Width: " + width + "; Height: " + height);
        this.imageZoneSimpleDataForBackground = imageZoneSimpleDataForBackground;
        //prevCameraPos = new Vec2();
        //initGraphicData(imageZoneSimpleDataForBackground);
    }

    private void initGraphicData(){

            initLines();

        calculatePicturesNumber();
    }

    private void calculatePicturesNumber() {
        float maxVisibleWidth = Program.objectsFrame.width;
        if (backgroundAtAnotherFrame)  maxVisibleWidth = Program.backgroundFrame.width;
        maxPicturesAlongX = PApplet.ceil(maxVisibleWidth/width)+1;
        //System.out.println("Pictures along x: " + maxPicturesAlongX + "; Visible width : " + maxVisibleWidth + "; Single background width: " + width);
    }


    private void createImageZoneSimpleDataForFullScreen() {
        imageZoneSimpleDataForBackground = new ImageZoneSimpleData(0,0, pictureMustNotBeUsed.getImage().width, pictureMustNotBeUsed.getImage().height);
    }

    public ScrollableAlongXBackground (GameObjectDataForStoreInEditor data){
        this.path = data.getPathToTexture();
        this.width = data.getWidth();
        this.height = data.getHeight();
        this.leftUpperX = data.getLeftUpperCorner().x;
        this.leftUpperY = data.getLeftUpperCorner().y;
        this.relativeVelocity = data.getRelativeVelocity();
        this.withLowerLine = data.isWithUpperLine();
        this.withUpperLine = data.isWithLowerLine();
        System.out.println("X" + leftUpperX + "; Y: " + leftUpperY + "; Width: " + width + "; Height: " + height);
        //prevCameraPos = new Vec2();
        //initGraphicData(data.getImageZoneSimpleData());
    }

    @Override
    public void loadGraphic(Tileset tileset) {
        initGraphicData();
        try {
            //picture = new Image(Program.getAbsolutePathToAssetsFolder(path));
            this.tileset = tileset;
            if (this.tileset != null && Program.debug) System.out.println("For this background we have set the tileset: " + tileset);
            else if (this.tileset == null) System.out.println("We have got null background");
            //System.out.println("Try to load background " +this.getObjectToDisplayName()+ " under path: " + Program.getAbsolutePathToAssetsFolder(path));

        }
        catch (Exception e){
            this.tileset = new Tileset(new Image(path));
            e.printStackTrace();
            pictureMustNotBeUsed = new Image(Program.getAbsolutePathToAssetsFolder(path));
            System.out.println("Background was created new for the background: " + pictureMustNotBeUsed.getImage().width + "x" + pictureMustNotBeUsed.getImage().height);
        }
    }


    private void initLines(){
        //System.out.println("Background must be resized to " + width + "x" + height);
        try {
            imageZoneForUpperLine = new ImageZoneSimpleData(imageZoneSimpleDataForBackground.leftX, imageZoneSimpleDataForBackground.upperY, imageZoneSimpleDataForBackground.rightX, imageZoneSimpleDataForBackground.upperY+1);
            imageZoneForLowerLine = new ImageZoneSimpleData(imageZoneSimpleDataForBackground.leftX, imageZoneSimpleDataForBackground.lowerY-1, imageZoneSimpleDataForBackground.rightX, imageZoneSimpleDataForBackground.lowerY);

        }
        catch (Exception e){
            //System.out.println("Can not resize background");
            e.printStackTrace();
        }
    }




    @Override
    public void draw(GameCamera gameCamera) {
        if (!hide) {
            PGraphics graphics;
            if (Background.backgroundAtAnotherFrame) graphics = Program.backgroundFrame;
            else graphics = Program.objectsFrame;
            graphics.pushMatrix();
            graphics.scale(BACKGROUND_DIMENSION_COEFFICIENT);
            if (withUpperLine) drawUpperLine(gameCamera, graphics);
            if (withLowerLine) drawLowerLine(gameCamera, graphics);
            drawDynamicBackground(gameCamera, graphics);
            graphics.popMatrix();
        }
    }

    public void setWidth(int value){
        width = value;
        initLines();
    }

    public void setHeight(int value) {
        height = value;
        initLines();
    }

    public void setRelativeVelocity(float value) {
        relativeVelocity = value;
    }


    @Override
    public void shift(float xShifting, float yShifting){
        leftUpperX+=xShifting;
        leftUpperY+=yShifting;
        //System.out.println("New pos: " + leftUpperX + "x" + leftUpperY);
    }

    private void drawUpperLineWithMatricies(GameCamera gameCamera, PGraphics graphics) {
        final int upperScreenBoard = (int) gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
        int upperLineThickness = 0 - upperScreenBoard+(int)leftUpperY;
        if (upperLineThickness > 0) {
            float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
            float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
            boolean nearestFounded = false;
            if (leftCameraBoard>drawnLeftSide){
                while(!nearestFounded){
                    if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                        nearestFounded = true;
                        drawnLeftSide-=(width);
                    }
                    else drawnLeftSide+=width;
                }
            }
            else{
                while(!nearestFounded){
                    if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                    else drawnLeftSide-=(width);
                }
            }
            float drawnPosX = drawnLeftSide+width/2;
            float drawnPosY = leftUpperY-upperLineThickness/2;
            graphics.imageMode(PConstants.CENTER);
            graphics.pushMatrix();
            graphics.translate(drawnPosX - gameCamera.getActualXPositionRelativeToCenter(), drawnPosY - gameCamera.getActualYPositionRelativeToCenter());
            for (int i = 0; i < maxPicturesAlongX; i++){
                graphics.image(pictureMustNotBeUsed.getImage(), i*width, 0, width, upperLineThickness+1, imageZoneForUpperLine.leftX, imageZoneForUpperLine.upperY, imageZoneForUpperLine.rightX, imageZoneForUpperLine.lowerY);
            }
            graphics.popMatrix();
        }
    }

    private void drawUpperLine(GameCamera gameCamera, PGraphics graphics) {
        final int upperScreenBoard = (int) gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
        int upperLineThickness = 0 - upperScreenBoard+(int)leftUpperY;
        if (upperLineThickness > 0) {
            float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
            float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
            boolean nearestFounded = false;
            if (leftCameraBoard>drawnLeftSide){
                while(!nearestFounded){
                    if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                        nearestFounded = true;
                        drawnLeftSide-=(width);
                    }
                    else drawnLeftSide+=width;
                }
            }
            else{
                while(!nearestFounded){
                    if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                    else drawnLeftSide-=(width);
                }
            }
            float drawnPosX = drawnLeftSide+width/2;
            float drawnPosY = leftUpperY-upperLineThickness/2;
            graphics.imageMode(PConstants.CENTER);
            for (int i = 0; i < maxPicturesAlongX; i++){
                graphics.image(tileset.getPicture().getImage(), drawnPosX - gameCamera.getActualXPositionRelativeToCenter()+i*width, drawnPosY - gameCamera.getActualYPositionRelativeToCenter(), width, upperLineThickness+1, imageZoneForUpperLine.leftX, imageZoneForUpperLine.upperY, imageZoneForUpperLine.rightX, imageZoneForUpperLine.lowerY);
            }
        }
    }

    private void drawLowerLine(GameCamera gameCamera, PGraphics graphics) {
        final float backgroundLowerSide = leftUpperY+ height - gameCamera.getActualYPositionRelativeToCenter();
        final float lowerLineThickness = gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-backgroundLowerSide;
        if (lowerLineThickness > 0) {
            float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
            float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
            boolean nearestFounded = false;
            if (leftCameraBoard>drawnLeftSide){
                while(!nearestFounded){
                    if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                        nearestFounded = true;
                        drawnLeftSide-=(width);
                    }
                    else drawnLeftSide+=width;
                }
            }
            else{
                while(!nearestFounded){
                    if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                    else drawnLeftSide-=(width);
                }
            }
            float drawnPosX = drawnLeftSide+width/2;
            float drawnPosY = leftUpperY+lowerLineThickness/2+height;
            for (int i = 0; i < maxPicturesAlongX; i++){
                graphics.image(tileset.getPicture().getImage(), drawnPosX - gameCamera.getActualXPositionRelativeToCenter()+i*width, drawnPosY - gameCamera.getActualYPositionRelativeToCenter()-1, width, lowerLineThickness+1, imageZoneForLowerLine.leftX, imageZoneForLowerLine.upperY, imageZoneForLowerLine.rightX, imageZoneForLowerLine.lowerY);
            }
        }
    }
/*
    private void drawLowerWihtMatricies(GameCamera gameCamera, PGraphics graphics) {
        final float backgroundLowerSide = leftUpperY+ height - gameCamera.getActualYPositionRelativeToCenter();
        final float lowerLineThickness = gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-backgroundLowerSide;
        if (lowerLineThickness > 0) {
            float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
            float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
            boolean nearestFounded = false;
            if (leftCameraBoard>drawnLeftSide){
                while(!nearestFounded){
                    if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                        nearestFounded = true;
                        drawnLeftSide-=(width);
                    }
                    else drawnLeftSide+=width;
                }
            }
            else{
                while(!nearestFounded){
                    if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                    else drawnLeftSide-=(width);
                }
            }
            float drawnPosX = drawnLeftSide+width/2;
            float drawnPosY = leftUpperY+lowerLineThickness/2+height;
            graphics.imageMode(PConstants.CENTER);
            graphics.pushMatrix();
            graphics.translate(drawnPosX - gameCamera.getActualXPositionRelativeToCenter(), drawnPosY - gameCamera.getActualYPositionRelativeToCenter());
            for (int i = 0; i < maxPicturesAlongX; i++){
                graphics.image(picture.getImage(), i*width, -1, width, lowerLineThickness+1, imageZoneForLowerLine.leftX, imageZoneForLowerLine.upperY, imageZoneForLowerLine.rightX, imageZoneForLowerLine.lowerY);
            }
            graphics.popMatrix();
        }
    }
*/

    private void drawDynamicBackground(GameCamera gameCamera, PGraphics graphics) {
       float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)-ADDITIONAL_X_AREA_WIDTH;
        float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
        boolean nearestFounded = false;
        if (leftCameraBoard>drawnLeftSide){
            while(!nearestFounded){
                if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                    nearestFounded = true;
                    drawnLeftSide-=(width);
                }
                else drawnLeftSide+=width;
            }
        }
        else{
            while(!nearestFounded){
                if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                else drawnLeftSide-=(width);
            }
        }
        float drawnPosX = drawnLeftSide+width/2;
        float drawnPosY = leftUpperY+ height/2;
        for (int i = 0; i < maxPicturesAlongX; i++){
            graphics.image(tileset.getPicture().getImage(), i*width+drawnPosX - gameCamera.getActualXPositionRelativeToCenter(), drawnPosY - gameCamera.getActualYPositionRelativeToCenter(), width, height, imageZoneSimpleDataForBackground.leftX, imageZoneSimpleDataForBackground.upperY, imageZoneSimpleDataForBackground.rightX, imageZoneSimpleDataForBackground.lowerY);
        }
    }
/*
    private void drawDynamicBackgroundWithMatricies(GameCamera gameCamera, PGraphics graphics) {
        float leftCameraBoard = gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
        float drawnLeftSide = leftUpperX-((leftCameraBoard-leftUpperX)*relativeVelocity);
        boolean nearestFounded = false;
        if (leftCameraBoard>drawnLeftSide){
            while(!nearestFounded){
                if (drawnLeftSide>=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) {
                    nearestFounded = true;
                    drawnLeftSide-=(width);
                }
                else drawnLeftSide+=width;
            }
        }
        else{
            while(!nearestFounded){
                if (drawnLeftSide<=gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) nearestFounded = true;
                else drawnLeftSide-=(width);
            }
        }
        float drawnPosX = drawnLeftSide+width/2;
        float drawnPosY = leftUpperY+ height/2;
        graphics.imageMode(PConstants.CENTER);
        graphics.pushMatrix();
        graphics.translate(drawnPosX - gameCamera.getActualXPositionRelativeToCenter(), drawnPosY - gameCamera.getActualYPositionRelativeToCenter());
        for (int i = 0; i < maxPicturesAlongX; i++){
            graphics.image(picture.getImage(), i*width, 0, width, height, imageZoneSimpleDataForBackground.leftX, imageZoneSimpleDataForBackground.upperY, imageZoneSimpleDataForBackground.rightX, imageZoneSimpleDataForBackground.lowerY);
        }
        graphics.popMatrix();
    }
*/


    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createBackground();
        return saveMaster.getDataString();
    }

    @Override
    public byte getType(){
        return SCROLLABLE_PICTURE_BACKGROUND;
    }

    public String getObjectToDisplayName(){
        return objectToDisplayName;
    }

    @Override
    protected void loadGraphic() {
        System.out.println("Nothing to implement");
    }
}