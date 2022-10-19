package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PGraphics;
import processing.core.PVector;

public class AddingCross {
    private Image imageArrow, imageHand;
    private boolean imageLoaded = false;
    //private Timer timer;
    private final static int START_DISTANCE_BETWEEN_CROSSES = (int)(Program.objectsFrame.width/5f);
    private static int DEAD_ZONE;
    private static final int maxTint = 255;
    private static final int minTint = 0;
    public final static boolean ARROW = true;
    public final static boolean HAND = false;
    private boolean type;
    private final boolean DRAW_IN_OBJECTS_FRAME = true;


    public AddingCross(boolean type){
        recreate(type);
    }

    public void recreate(boolean type){
        this.type = type;
        if (!imageLoaded){
            try {
                imageArrow = new Image(Program.getAbsolutePathToAssetsFolder("AddingCross.png"));
                imageHand = new Image(Program.getAbsolutePathToAssetsFolder("AddingCross.png"));
                int dimension = (int)(Program.engine.width/8.0f);
                imageArrow.getImage().resize(dimension, dimension);
                DEAD_ZONE = (int)(imageArrow.getImage().width/1.5f);
                if (Program.debug) System.out.println("Drawing cross was uploaded");
                imageLoaded = true;
            }
            catch (Exception e){
                System.out.println("There are no graphic data for AddingCross");
            }
        }
    }

    /*
    private Image getImage(){
        if (type == ARROW) return imageArrow;
        else return imageHand;
    }*/

    /*
    private PImage getImage(){
        if (type == ARROW) return imageArrow.getImage();
        else return imageHand.getImage();
    }
     */

    public void drawToCenter(PGraphics graphics, float relativePos, PVector point, GameCamera gameCamera){
        graphics.pushMatrix();
        graphics.scale(Program.OBJECT_FRAME_SCALE);
        graphics.translate(point.x - gameCamera.getActualXPositionRelativeToCenter(), point.y - gameCamera.getActualYPositionRelativeToCenter());
        graphics.pushStyle();
        graphics.tint(255, (maxTint-(maxTint-minTint)*relativePos/100f));
        graphics.image(imageArrow.image, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE);
        graphics.scale(-1,-1);
        graphics.image(imageArrow.image, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE- Editor2D.gridSpacing, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE- Editor2D.gridSpacing);
        graphics.scale(1,-1);
        graphics.image(imageArrow.image, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE- Editor2D.gridSpacing, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE );    // right upper
        graphics.scale(-1,-1);
        graphics.image(imageArrow.image, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE, (-relativePos*START_DISTANCE_BETWEEN_CROSSES/100)-DEAD_ZONE- Editor2D.gridSpacing);   //left lower
        graphics.noTint();
        graphics.popMatrix();
    }
    
    public void drawToVertex(PGraphics graphics, float relativePos, PVector point, GameCamera gameCamera){
        graphics.pushMatrix();
        graphics.scale(Program.OBJECT_FRAME_SCALE);
        //graphics.scale(Program.OBJECT_FRAME_SCALE);
        graphics.translate(point.x - gameCamera.getActualXPositionRelativeToCenter(), point.y - gameCamera.getActualYPositionRelativeToCenter());
        graphics.pushStyle();
        graphics.tint(255, (maxTint-(maxTint-minTint)*relativePos/100f));
        System.out.println("Drawn on objects frame");
        graphics.stroke(0,155,155);
        graphics.strokeWeight(5);
        graphics.line(-50,0, 50, 0);
        graphics.line(0,-50, 0, 50);
        //graphics.line(0,0, 0, -10);
        //graphics.line(0,0, 0, 10);
        //System.out.println("Point drawn 1");
        try {
            graphics.image(imageArrow.image, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE);
            graphics.scale(-1, -1);
            graphics.image(imageArrow.image, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE);
            graphics.scale(1, -1);
            graphics.image(imageArrow.image, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE);
            graphics.scale(-1, -1);
            graphics.image(imageArrow.image, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE, (-relativePos * START_DISTANCE_BETWEEN_CROSSES / 100) - DEAD_ZONE);
        }
        catch (Exception e){
            System.out.println("Cross can not be drawn: " + e);
        }
        graphics.noTint();
        graphics.popStyle();
        graphics.popMatrix();

        //graphics.endDraw();
        
    }

    
    
}
