package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class GraphicChoosingCorner {
    public final static int RUNNING_COLOR_TINT = Program.engine.color(5,230,55);
    public final static int CHOOSEN_COLOR_TINT = Program.engine.color(255,0,0);
    public final static int BASIC_COLOR_TINT = Program.engine.color(255,225);
    private static Image image;
    private Vec2 position;
    private Vec2 relativePosition;
    private Vec2 nullPosition;
    private float scale;
    private int tintValue = 0;
    private static int width;
    private static int height;

    public final static byte NOT_SELECTED = 0;
    public final static byte SELECTED = 1;
    public final static byte IN_CHOOSING_PROCESS = -1;
    private byte statement = NOT_SELECTED;

    private static boolean graphicLoaded = false;
    //private static Image corner;

    private byte orientation;

    GraphicChoosingCorner(byte orientation, Vec2 position, Vec2 nullPosition){
        this.orientation = orientation;
        this.position = new Vec2(position.x, position.y);
        this.nullPosition = nullPosition;
        //this.position = position;
        if (!graphicLoaded){
            image = new Image(Program.getAbsolutePathToAssetsFolder("AreaChoosingCross.png"));
            image.getImage().resize((int)(Program.engine.width/25f), (int)(Program.engine.width/25f));
            width = (int)(image.getImage().width/2.0f);
            height = (int)(image.getImage().height/2.0f);
            graphicLoaded = true;
        }
    }

    public static int getWidth(){
        return width;
    }

    public byte getOrientation (){
        return orientation;
    }

    public byte getStatement(){
        return statement;
    }

    public Vec2 getPosition(){
        return position;
    }

    public void update(Vec2 relativePosition, float scale){
        if (this.relativePosition == null) this.relativePosition = new Vec2(0,0);
        else {
            this.relativePosition.x = relativePosition.x;
            this.relativePosition.y = relativePosition.y;
        }
        this.scale = scale;
    }

    private Vec2 getPositionForCross(Vec2 relativePosition){
        /*
        if (orientation == GraphicChoosingArea.LEFT_UPPER_CORNER) return new Vec2(position.x-relativePosition.x-width/2, position.y-relativePosition.y-height/2);
        else if (orientation == GraphicChoosingArea.RIGHT_UPPER_CORNER) return new Vec2(position.x-relativePosition.x+width/2, position.y-relativePosition.y-height/2);
        else if (orientation == GraphicChoosingArea.LEFT_LOWER_CORNER) return new Vec2(position.x-relativePosition.x-width/2, position.y-relativePosition.y+height/2);
        else if (orientation == GraphicChoosingArea.RIGHT_LOWER_CORNER) return new Vec2(position.x-relativePosition.x+width/2, position.y-relativePosition.x+height/2);

        else return null;
        */

        return null;
    }

    private void drawOnBasePosition(PGraphics graphic){
        graphic.translate(-relativePosition.x*scale+position.x*scale+nullPosition.x, -relativePosition.y*scale+position.y*scale+nullPosition.y);

        if (orientation == GraphicChoosingArea.RIGHT_UPPER_CORNER) graphic.scale(-1,1);
        else if (orientation == GraphicChoosingArea.LEFT_LOWER_CORNER) graphic.scale(1,-1);
        else if (orientation == GraphicChoosingArea.RIGHT_LOWER_CORNER) graphic.scale(-1,-1);
        graphic.tint(tintValue);
    }

    public void draw(float shiftingFromAngle, PGraphics graphic) {
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);

        if (shiftingFromAngle == 0) drawOnBasePosition(graphic);
        else drawInAnotherPos(shiftingFromAngle, graphic);
        graphic.image(image.getImage(), 0, 0);

        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
    }

    public void translate(Vec2 shifting){
        position.x+=shifting.x;
        position.y+=shifting.y;
    }

    public void translate(float xShifting, float yShifting){
        position.x+=xShifting;
        position.y+=yShifting;
        position.x = PApplet.abs(position.x);
        position.y = PApplet.abs(position.y);
    }



    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void makeGraphicChosen(boolean flag) {
        if (flag) tintValue = Program.engine.color(255,0,0);
        else tintValue = 0;
    }

    public void setTint(int value) {
        tintValue = value;
    }

    public void drawInAnotherPos(float relativeShifting, PGraphics graphic) {
        graphic.translate(-relativePosition.x*scale+position.x*scale+nullPosition.x, -relativePosition.y*scale+position.y*scale+nullPosition.y);

        if (orientation == GraphicChoosingArea.RIGHT_UPPER_CORNER) {
            graphic.translate(relativeShifting, -relativeShifting);
            graphic.scale(-1,1);
        }
        else if (orientation == GraphicChoosingArea.LEFT_LOWER_CORNER) {
            graphic.translate(-relativeShifting, relativeShifting);
            graphic.scale(1,-1);
        }
        else if (orientation == GraphicChoosingArea.RIGHT_LOWER_CORNER) {
            graphic.translate(relativeShifting, relativeShifting);
            graphic.scale(-1,-1);
        }
        else{
            graphic.translate(-relativeShifting, -relativeShifting);
        }
        graphic.tint(tintValue);
    }

    public void setStatement(byte statement) {
        this.statement = statement;
    }
}
