package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class StaticSprite extends SingleGraphicElement implements Cloneable{
    public static String BLACK_RECT_PATH = Program.getAbsolutePathToAssetsFolder("BlackRect.png");
    public final static String INVISIBLE_WALL = Program.getAbsolutePathToAssetsFolder("Invisible wall.png");
    //private String path;
    //Tileset tileset;
    //private int xLeft, yLeft, xRight, yRight;
    //private int width, height;
    // Fill area parameters
    final private static boolean FILL_ALONG_X = true;
    final private static boolean FILL_ALONG_Y = false;
    private boolean fillAlong;
    private int repeatingElementsNumber = 1;
    //private float widthForFilled, heightForFilled;

    public StaticSprite (byte predefinedPictureType, int width, int height){
        if (predefinedPictureType == CLEAR_RECT){
            ImageZoneFullData data = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder(BLACK_RECT_PATH), 0,0,200,200);
            this.path = data.getName();
            this.xLeft = (int)data.leftX;
            this.yLeft = (int)data.upperY;
            this.xRight = (int) data.rightX;
            this.yRight = (int) data.lowerY;
            this.width = (int)width;
            this.height = (int)height;
            parentElementHeight = this.height;
            parentElementWidth = this.width;
        }
    }

    public StaticSprite(ImageZoneFullData imageZoneFullData, int width, int height) {
        this.path = imageZoneFullData.getName();
        this.xLeft = (int) imageZoneFullData.leftX;
        this.yLeft = (int) imageZoneFullData.upperY;
        this.xRight = (int) imageZoneFullData.rightX;
        this.yRight = (int) imageZoneFullData.lowerY;
        this.width = (int) width;
        this.height = (int) height;
        parentElementHeight = this.height;
        parentElementWidth = this.width;
    }

    public StaticSprite(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height) {
        this.path = path;
        this.xLeft = xLeft;
        this.yLeft = yLeft;
        this.xRight = xRight;
        this.yRight = yRight;
        this.width = width;
        this.height = height;
        parentElementHeight = this.height;
        parentElementWidth = this.width;
        //System.out.println("Image with path: " + path+ " was created") ;
    }

    public StaticSprite(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillAreaWithSprite) {
        this.path = path;
        this.xLeft = xLeft;
        this.yLeft = yLeft;
        this.xRight = xRight;
        this.yRight = yRight;
        this.fillAreaWithSprite = fillAreaWithSprite;
        //fillArea(width, height, fillAreaWithSprite);
        System.out.println("Width was : " + width + " Height: " + height);
        if (fillAreaWithSprite == false) {
            this.width = width;
            this.height = height;
            parentElementHeight = this.height;
            parentElementWidth = this.width;
        }
        else {
            recalculateChildDimensions(width, height);
/*
            float xScaleSpriteFactor = (float) Program.engine.ceil(width / PApplet.abs((float) (xRight - xLeft)));
            float yScaleSpriteFactor = (float) Program.engine.ceil(height / PApplet.abs((float) (yRight - yLeft)));
            if (xScaleSpriteFactor > yScaleSpriteFactor) {
                fillAlong = FILL_ALONG_X;
                this.height = height;
                parentElementHeight = this.height;
                parentElementWidth = width;
                float theoreticalWidthOfSingleElement = (float) Program.engine.ceil(width * yScaleSpriteFactor / (xScaleSpriteFactor));
                repeatingElementsNumber = PApplet.round((float) width / theoreticalWidthOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.width = (int) Program.engine.ceil((width / repeatingElementsNumber));
            } else {
                fillAlong = FILL_ALONG_Y;
                this.width = width;
                parentElementWidth = this.width;
                parentElementHeight = height;
                float theoreticalHeightOfSingleElement = (float) Program.engine.ceil(height * xScaleSpriteFactor / yScaleSpriteFactor);
                repeatingElementsNumber = PApplet.round((float) height / theoreticalHeightOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.height = (int) Program.engine.ceil((height / repeatingElementsNumber));
            }

*/
        }
    }

    public void recalculateChildDimensions(){
        if (fillAreaWithSprite){
            recalculateChildDimensions(parentElementWidth, parentElementHeight);
        }
        else System.out.println("We need not to recalculate");
    }

    public void recalculateChildDimensions(int parentWidth, int parentHeight){
        if (fillAreaWithSprite){
            float xScaleSpriteFactor = (float) Program.engine.ceil(parentWidth / PApplet.abs((float) (xRight - xLeft)));
            float yScaleSpriteFactor = (float) Program.engine.ceil(parentHeight / PApplet.abs((float) (yRight - yLeft)));
            if (xScaleSpriteFactor > yScaleSpriteFactor) {
                fillAlong = FILL_ALONG_X;
                this.height = parentHeight;
                parentElementHeight = this.height;
                parentElementWidth = parentWidth;
                float theoreticalWidthOfSingleElement = (float) Program.engine.ceil(parentWidth * yScaleSpriteFactor / (xScaleSpriteFactor));
                repeatingElementsNumber = PApplet.round((float) parentWidth / theoreticalWidthOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.width = (int) Program.engine.ceil((1f*parentWidth / (1f*repeatingElementsNumber)));
            } else {
                fillAlong = FILL_ALONG_Y;
                this.width = parentWidth;
                parentElementWidth = this.width;
                parentElementHeight = parentHeight;
                float theoreticalHeightOfSingleElement = (float) Program.engine.ceil(parentHeight * xScaleSpriteFactor / yScaleSpriteFactor);
                repeatingElementsNumber = PApplet.round((float) parentHeight / theoreticalHeightOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.height = (int) Program.engine.ceil((1f*parentHeight / (1f*repeatingElementsNumber)));
            }
        }
        System.out.println("Parent width: " + parentWidth + "; Child width: " + width);
    }

    private void fillArea(int width, int height, boolean fillAreaWithSprite){
        if (fillAreaWithSprite == false) {
            this.width = width;
            this.height = height;
            parentElementHeight = this.height;
            parentElementWidth = this.width;
        }
        else {
            float xScaleSpriteFactor = (float) Program.engine.ceil(width / PApplet.abs((float) (xRight - xLeft)));
            float yScaleSpriteFactor = (float) Program.engine.ceil(height / PApplet.abs((float) (yRight - yLeft)));

            if (xScaleSpriteFactor > yScaleSpriteFactor) {
                fillAlong = FILL_ALONG_X;
                this.height = height;
                parentElementHeight = this.height;
                parentElementWidth = width;
                float theoreticalWidthOfSingleElement = (float) Program.engine.ceil(width * yScaleSpriteFactor / (xScaleSpriteFactor));
                repeatingElementsNumber = PApplet.round((float) width / theoreticalWidthOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.width = (int) Program.engine.ceil((width / repeatingElementsNumber));
            } else {
                fillAlong = FILL_ALONG_Y;
                this.width = width;
                parentElementWidth = this.width;
                parentElementHeight = height;
                float theoreticalHeightOfSingleElement = (float) Program.engine.ceil(width * xScaleSpriteFactor / yScaleSpriteFactor);
                repeatingElementsNumber = PApplet.round((float) width / theoreticalHeightOfSingleElement);
                if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
                this.height = (int) Program.engine.ceil((height / repeatingElementsNumber));
            }
            //System.out.println(" xScaleSpriteFactor " + xScaleSpriteFactor + "; yScaleSpriteFactor" + yScaleSpriteFactor);
        }
    }




    public void loadSprite(Tileset tileset) {
        this.tileset = tileset;
        if (!fillAreaWithSprite) initBoundingDimensions(width, height);
        else {
            if (fillAlong == FILL_ALONG_X) {
                initBoundingDimensions(repeatingElementsNumber*width, height);
            } else {
                initBoundingDimensions(width, repeatingElementsNumber*height);
            }
        }
    }

    public void loadSprite() {
        Image image = new Image(path);
        tileset = new Tileset(image);
        loadSprite(tileset);
    }

    public void setFillAreaWithSprite(boolean fill){
        this.fillAreaWithSprite = fill;
    }

    /*
    public StaticSprite(Tileset tileset, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        this.tileset = tileset;
        this.xLeft = xLeft;
        this.yLeft = yLeft;
        this.xRight = xRight;
        this.yRight = yRight;
        this.width = width;
        this.height = height;
    }

     */



    public void draw(GameCamera gameCamera, Body body) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle();
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null && isVisibleOnScreen(gameCamera, pos.x, pos.y)) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (withTint) {
                Program.objectsFrame.tint(tintColor);
            }
            if (fillAreaWithSprite == false) {
                Program.objectsFrame.image(tileset.picture.image, 0f, 0f, width, height, xLeft, yLeft, xRight, yRight);
            } else {
                drawFilledAreaAfterMatrixDeformations();
            }
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popMatrix();

        }
    }

    public void tintUpdatingBySelecting(IndependentOnScreenGraphic independentOnScreenStaticSprite, int actualSelectingValue) {

        if (independentOnScreenStaticSprite.isSelectionWasCleared()){
            //System.out.println("Drawn deselected " + actualSelectingValue);
            resetTint();
            independentOnScreenStaticSprite.clearSelection();
        }
        if (independentOnScreenStaticSprite.isSelected()) {
            //System.out.println("Drawn as selected " + actualSelectingValue);
            setTint(Program.engine.color(255, actualSelectingValue));
        }
    }


    private void drawFilledAreaAfterMatrixDeformations() {
        if (fillAlong == FILL_ALONG_X) {
            for (float i = 0f; i < repeatingElementsNumber; i++) {
                Program.objectsFrame.image(tileset.picture.image, -((repeatingElementsNumber - 1f) * width / 2f) + width * i, 0f, width, height, xLeft, yLeft, xRight, yRight);
            }
        } else {
            for (float i = 0f; i < repeatingElementsNumber; i++) {
                Program.objectsFrame.image(tileset.picture.image, 0f, -((repeatingElementsNumber - 1f) * height / 2f) + height * i, width, height, xLeft, yLeft, xRight, yRight);
            }
        }
    }


    public void draw(GameCamera gameCamera, Body body, boolean flip) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle();
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null && isVisibleOnScreen(gameCamera, pos.x, pos.y)) {
            //System.out.println("Draw the pipe");

            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            //Game2D.objectsFrame.rotate(-a);
            if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (fillAreaWithSprite == false)
                Program.objectsFrame.image(tileset.picture.image, 0f, 0f, width, height, xLeft, yLeft, xRight, yRight);
            else drawFilledAreaAfterMatrixDeformations();
            Program.objectsFrame.popMatrix();
        }
    }

    public void draw(GameCamera gameCamera, PVector pos, float a) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, pos.x, pos.y)) {

            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            if (fillAreaWithSprite == false)
                Program.objectsFrame.image(tileset.picture.image, 0, 0, width, height, xLeft, yLeft, xRight, yRight);
            else {
                if (a == 0 || a == 360) drawFilledAreaAfterMatrixDeformations();
            }
            Program.objectsFrame.popMatrix();
        }
    }



    private void updateBoundingDimensions(float angleInRadians) {
        int angleInDegrees = (int) PApplet.degrees(angleInRadians);
        if (angleInDegrees == 0 || angleInDegrees == 180 || angleInDegrees == 360 || angleInDegrees == -180){
            if (!fillAreaWithSprite) initBoundingDimensions(width, height);
            else {
                if (fillAlong == FILL_ALONG_X) {
                    initBoundingDimensions(repeatingElementsNumber*width, height);
                } else {
                    initBoundingDimensions(width, repeatingElementsNumber*height);
                }
            }
        }
        else if (angleInDegrees == 90 || angleInDegrees == -90 || angleInDegrees == 270 || angleInDegrees == -270){
            if (width == height){
                if (boundingWidth!=boundingHeight){
                    if (!fillAreaWithSprite) initBoundingDimensions(height, width);
                    else {
                        if (fillAlong == FILL_ALONG_X) {
                            initBoundingDimensions(height, repeatingElementsNumber*width);
                        } else {
                            initBoundingDimensions( repeatingElementsNumber*height, width);
                        }
                    }
                }
            }
        }
        else {
            if (!fillAreaWithSprite) {
                int boundingWidth = (int) (PApplet.cos(PApplet.abs(angleInRadians))*width+ PApplet.sin(PApplet.abs(angleInRadians))*height);
                int boundingHeight = (int) (PApplet.cos(PApplet.abs(angleInRadians))*height+ PApplet.sin(PApplet.abs(angleInRadians))*width);
                initBoundingDimensions(boundingWidth,  boundingHeight );
            }
            else {
                if (fillAlong == FILL_ALONG_X) {
                    int boundingWidth = (int) (PApplet.cos(PApplet.abs(angleInRadians))*repeatingElementsNumber*width+ PApplet.sin(PApplet.abs(angleInRadians))*height);
                    int boundingHeight = (int) (PApplet.cos(PApplet.abs(angleInRadians))*height+ PApplet.sin(PApplet.abs(angleInRadians))*width*repeatingElementsNumber);
                    //System.out.println("Bounding with along x: " + boundingWidth + "x" + boundingHeight + " by angle: " + angleInDegrees + " in radians " + angleInRadians);
                    initBoundingDimensions(boundingWidth, boundingHeight);

                } else {
                    int boundingWidth = (int) (PApplet.cos(PApplet.abs(angleInRadians))*width+ PApplet.sin(PApplet.abs(angleInRadians))*height*repeatingElementsNumber);
                    int boundingHeight = (int) (PApplet.cos(PApplet.abs(angleInRadians))*height*repeatingElementsNumber+ PApplet.sin(PApplet.abs(angleInRadians))*width);
                    //System.out.println("Bounding with along y: " + boundingWidth + "x" + boundingHeight + " by angle: " + angleInDegrees + " in radians " + angleInRadians);
                    initBoundingDimensions(boundingWidth, boundingHeight);
                }
            }

        }
        //System.out.println("Dims:  " + boundingWidth + "x" + boundingHeight + " by angle: " + angleInDegrees + " in radians " + angleInRadians);

        //else System.out.println("ANGLE " + angleInDegrees);
    }

    public void draw(GameCamera gameCamera, float x, float y, float a, float relativeVelocity, PGraphics graphic) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, x, y)) {
            graphic.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            graphic.translate(x - gameCamera.getActualPosition().x*relativeVelocity + graphic.width / 2, y - gameCamera.getActualPosition().y + graphic.height / 2);
            graphic.translate(0, 0);
            if (additionalAngle == 0) graphic.rotate(-a);
            else graphic.rotate(-a+PApplet.radians(additionalAngle));
            graphic.imageMode(PConstants.CENTER);
            if (fillAreaWithSprite == false) {
                graphic.image(tileset.picture.image, 0, 0, width, height, xLeft, yLeft, xRight, yRight);
            }
            else {
                if (a == 0 || a == 360) {
                    drawFilledAreaAfterMatrixDeformations();
                }
            }
            graphic.popMatrix();
        }
    }

    public void draw(GameCamera gameCamera, Vec2 pos, float a, float scaleX, float scaleY) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, pos)) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            if (scaleX != 1.0f || scaleY != 1.0f){
                Program.objectsFrame.scale(scaleX,scaleY);
            }
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (withTint) Program.objectsFrame.tint(tintColor);
            if (fillAreaWithSprite == false)
                Program.objectsFrame.image(tileset.picture.image, 0, 0, width, height, xLeft, yLeft, xRight, yRight);
            else {
                if (a == 0 || a == 360) drawFilledAreaAfterMatrixDeformations();
            }
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popMatrix();
        }
    }


    public void draw(GameCamera gameCamera, Vec2 pos, boolean flip) {
        //Bounding dims must not be updated
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, pos)) {
            Program.objectsFrame.imageMode(PConstants.CENTER);
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (flip) Program.objectsFrame.scale(-1,1);
            if (withTint) Program.objectsFrame.tint(tintColor);
            if (fillAreaWithSprite == false) {
                Program.objectsFrame.image(tileset.picture.image, 0,0, width, height, xLeft, yLeft, xRight, yRight);
            }
            else {
                drawFilledAreaAfterMatrixDeformations();
                System.out.println("This must be tested!");
            }
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popMatrix();
        }
        else{
            System.out.println("Drawn!!!!" + (isVisibleOnScreen(gameCamera, pos)));
        }
    }


    @Override
    public void draw(GameCamera gameCamera, Vec2 pos, float a) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, pos)) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (withTint) Program.objectsFrame.tint(tintColor);
            if (fillAreaWithSprite == false)
                Program.objectsFrame.image(tileset.picture.image, 0, 0, width, height, xLeft, yLeft, xRight, yRight);
            else {
                if (a == 0 || a == 360) drawFilledAreaAfterMatrixDeformations();
            }
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popMatrix();
        }
        else if (tileset == null) {
            //System.out.println("No graphic data for this object at pos " + pos + " and angle " + PApplet.degrees(a));
        }
    }

    public void draw(GameCamera gameCamera, Vec2 anker, PVector pos, float a, boolean flip) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null  && isVisibleOnScreen(gameCamera, pos.x ,pos.y)) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            if (additionalAngle == 0) Program.objectsFrame.rotate(-a);
            else Program.objectsFrame.rotate(-a+PApplet.radians(additionalAngle));
            if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
            Program.objectsFrame.imageMode(PConstants.CENTER);
            if (withTint) Program.objectsFrame.tint(tintColor);
            Program.objectsFrame.image(tileset.picture.image, anker.x, anker.y, width, height, xLeft, yLeft, xRight, yRight);
            if (withTint) Program.objectsFrame.noTint();
            Program.objectsFrame.popMatrix();
        }
    }

    public void draw(PGraphics graphics, Vec2 anker, PVector pos, float a, boolean flip) {
        updateBoundingDimensions(a);
        if (Program.WITH_GRAPHIC && tileset != null && visible) {
            graphics.pushMatrix();
            if (graphics.equals(Program.objectsFrame)){
                graphics.scale(Program.OBJECT_FRAME_SCALE);
            }
            //graphics.scale();
            graphics.translate(pos.x, pos.y);
            if (additionalAngle == 0) graphics.rotate(-a);
            else graphics.rotate(-a+PApplet.radians(additionalAngle));
            if (flip == Image.FLIP) graphics.scale(-1.0f, 1.0f);
            graphics.imageMode(PConstants.CENTER);
            if (withTint) graphics.tint(tintColor);
            graphics.image(tileset.picture.image, anker.x, anker.y, width, height, xLeft, yLeft, xRight, yRight);
            if (withTint) graphics.noTint();
            graphics.popMatrix();
        }
    }

    public void draw(PGraphics graphics, float x, float y, float scale){
        if (Program.WITH_GRAPHIC && tileset != null && visible) {
             graphics.pushMatrix();
            try {
                graphics.imageMode(PConstants.CENTER);
                graphics.translate(x, y);
                if (scale != 1) {
                    if (scale < 0){
                        // graphics.scale((PApplet.abs(scale)), scale);
                        graphics.scale(scale, (PApplet.abs(scale)));
                    }
                    else graphics.scale(scale);
                }
                graphics.image(tileset.picture.image, 0f, 0f, width, height, xLeft, yLeft, xRight, yRight);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                graphics.popMatrix();
            }
        }

    }

    public void draw(GameCamera gameCamera, Vec2 pos, float angleInRadians, boolean flipByY, boolean flipByX) {
        updateBoundingDimensions(angleInRadians);
        if (Program.WITH_GRAPHIC && tileset != null && isVisibleOnScreen(gameCamera, pos)) {
            if (angleInRadians == 0 && flipByY == false) {
                drawWithNoAngle(gameCamera, pos);
            }
            else {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                if (additionalAngle == 0) Program.objectsFrame.rotate(-angleInRadians);
                else Program.objectsFrame.rotate(-angleInRadians + PApplet.radians(additionalAngle));
                if (flipByY && !flipByX) Program.objectsFrame.scale(-1.0f, 1.0f);
                else if (!flipByY && flipByX) Program.objectsFrame.scale(1.0f, -1.0f);
                else if (flipByY && flipByX) Program.objectsFrame.scale(-1.0f, -1.0f);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                if (withTint) Program.objectsFrame.tint(tintColor);
                if (fillAreaWithSprite == false)
                    Program.objectsFrame.image(tileset.picture.image, 0f, 0f, width, height, xLeft, yLeft, xRight, yRight);
                else drawFilledAreaAfterMatrixDeformations();
                if (withTint) Program.objectsFrame.noTint();
                Program.objectsFrame.popMatrix();
            }
        }
    }

    public void draw(GameCamera gameCamera, Vec2 pos, float angleInRadians, boolean flip) {
        draw(gameCamera, pos, angleInRadians, flip, false);
        /*
        updateBoundingDimensions(angleInRadians);
        if (Program.WITH_GRAPHIC && tileset != null && isVisibleOnScreen(gameCamera, pos)) {
            if (angleInRadians == 0 && flip == false) {
                drawWithNoAngle(gameCamera, pos);
            }
            else {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                if (additionalAngle == 0) Program.objectsFrame.rotate(-angleInRadians);
                else Program.objectsFrame.rotate(-angleInRadians + PApplet.radians(additionalAngle));
                if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
                Program.objectsFrame.imageMode(PConstants.CENTER);
                if (withTint) Program.objectsFrame.tint(tintColor);
                if (fillAreaWithSprite == false)
                    Program.objectsFrame.image(tileset.picture.image, 0f, 0f, width, height, xLeft, yLeft, xRight, yRight);
                else drawFilledAreaAfterMatrixDeformations();
                if (withTint) Program.objectsFrame.noTint();
                Program.objectsFrame.popMatrix();
            }
        }*/

    }

    private void drawWithNoAngle(GameCamera gameCamera, Vec2 pos){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        if (Program.objectsFrame.imageMode != PConstants.CENTER) Program.objectsFrame.imageMode(PConstants.CENTER);
        if (withTint) Program.objectsFrame.tint(tintColor);
        if (fillAreaWithSprite == false)
            Program.objectsFrame.image(tileset.picture.image, pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter(), width, height, xLeft, yLeft, xRight, yRight);
        else drawFilledAreaWithoutMatrixDeformations(gameCamera, pos);
        if (withTint) Program.objectsFrame.noTint();
        Program.objectsFrame.popMatrix();
        //System.out.println("Drawn without angle");
    }

    private void drawFilledAreaWithoutMatrixDeformations(GameCamera gameCamera, Vec2 pos) {
        if (fillAlong == FILL_ALONG_X) {
            for (float i = 0f; i < repeatingElementsNumber; i++) {
                Program.objectsFrame.image(tileset.picture.image, pos.x - gameCamera.getActualXPositionRelativeToCenter()-((repeatingElementsNumber - 1f) * width / 2f) + width * i, pos.y - gameCamera.getActualYPositionRelativeToCenter(), width, height, xLeft, yLeft, xRight, yRight);
            }
        } else {
            for (float i = 0f; i < repeatingElementsNumber; i++) {
                Program.objectsFrame.image(tileset.picture.image, pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter()-((repeatingElementsNumber - 1f) * height / 2f) + height * i, width, height, xLeft, yLeft, xRight, yRight);
            }
        }
    }

    public Tileset getTileset() {
        return tileset;
    }

    public boolean getFill() {
        return fillAreaWithSprite;
    }

    /*
    public int getWidth() {
        return width;
    }
    */
    /*
    public int getHeight() {
        return height;
    }
    */

    public StaticSprite clone()
    {
        try {
            return (StaticSprite) super.clone();
        }
        catch (CloneNotSupportedException e){
            System.out.println("Can not clone this sprite; " + e);
            return null;
        }

    }




}
