package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_ImageButton extends androidAndroidGUI_Button {

    private final Image image;
    private String path;
    private boolean withMovement = true;
    private float imageMovingLinearVelocity = 0.25f;
    private final Vec2 imageMovingVelocity;
    private final Vec2 imagePositions;
    private float imageScale = 1.5f* Program.DEBUG_DISPLAY_WIDTH/ Program.engine.width;

    //private final byte RIGHT_SIDE;


    public androidAndroidGUI_ImageButton(Vec2 pos, int w, int h, String name, boolean withFixation, String pathToTexture, boolean withMovement) {
        super(pos, w, h, name, withFixation);
        Image image1;
        this.path = pathToTexture;
        this.withMovement = withMovement;
        try {
            image1 = new Image(Program.getAbsolutePathToAssetsFolder(pathToTexture));
        }
        catch (Exception e){
            System.out.println("Can not load image for image button");
            image1 = pictureActive;
        }
        this.image = image1;
        imagePositions = new Vec2();
        imageMovingVelocity = new Vec2( imageMovingLinearVelocity* PApplet.cos(PConstants.PI/4), imageMovingLinearVelocity* PApplet.sin(PConstants.PI/4));
        init(withFixation);
    }

    @Override
    public String getName(){
        return path;
    }

    @Override
    public void draw(PGraphics graphic){
        drawImage(graphic);
        drawButtonBody(graphic);
    }

    private void drawImage(PGraphics graphic) {
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        if (!pictureActive.equals(image)) {
            if (withMovement) {
                updateVisibleZoneCoordinates();
                int[] coordinates = getVisibleZoneCoordinates();
                graphic.image(image.getImage(), pos.x, pos.y, elementWidth * 0.95f, elementHeight * 0.90f, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            } else {
                graphic.image(image.getImage(), pos.x, pos.y, elementWidth * 0.95f, elementHeight * 0.90f);
            }
        }
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
    }

    private void updateVisibleZoneCoordinates(){
        imagePositions.addLocal(imageMovingVelocity.mul(imageMovingLinearVelocity* Program.deltaTime));
        if ((imagePositions.x+elementWidth*imageScale)>image.getImage().width){
            imagePositions.x = -1+image.getImage().width-elementWidth*imageScale;
            rotateVelocityVector(GameCamera.RIGHT_SIDE);
        }
        else if ((imagePositions.x)<0){
            imagePositions.x = 1;
            rotateVelocityVector(GameCamera.LEFT_SIDE);
        }
        if ((imagePositions.y+elementHeight*imageScale)>image.getImage().height){
            imagePositions.y = image.getImage().height-elementHeight*imageScale;
            rotateVelocityVector(GameCamera.LOWER_SIDE);
        }
        else if ((imagePositions.y)<0){
            //System.out.println("Upper side");
            imagePositions.y = 0;
            rotateVelocityVector(GameCamera.UPPER_SIDE);
        }
    }

    private void rotateVelocityVector(byte side){
        if (side == GameCamera.RIGHT_SIDE){
            imageMovingVelocity.x = -imageMovingVelocity.x;
        }
        else if (side == GameCamera.LEFT_SIDE){
            imageMovingVelocity.x = -imageMovingVelocity.x;
        }
        else if (side == GameCamera.LOWER_SIDE){
            imageMovingVelocity.y = -imageMovingVelocity.y;
        }
        else if (side == GameCamera.UPPER_SIDE){
            imageMovingVelocity.y = -imageMovingVelocity.y;
        }
    }

    private int[] getVisibleZoneCoordinates(){
        int [] coordinates = new int[4];
        coordinates[0] = (int)imagePositions.x;
        coordinates[1] = (int)imagePositions.y;
        coordinates[2] = (int)(imagePositions.x+elementWidth*imageScale);
        coordinates[3] = (int)(imagePositions.y+elementHeight*imageScale);
        return coordinates;
    }
    @Override
    protected void finalize(){
        try {
            if (image!= null){
                Program.objectsFrame.removeCache(image.image);
                Program.backgroundFrame.removeCache(image.image);
                Program.engine.g.removeCache(image.image);
            }
            //image = null;
            System.out.println("Image button graphic was removed from the memory");
            super.finalize();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
