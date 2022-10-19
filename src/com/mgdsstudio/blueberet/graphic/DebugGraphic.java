package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PVector;

public class DebugGraphic {
    private Vec2 startPos, endPos;
    private int width, height, tint, radius;
    public final static byte ARROW = 0;
    public final static byte CROSS = 1;
    public final static byte IMAGE = 2;
    public final static byte ROUND = 3;
    public final static byte LINE = 5;

    private Image debugImage;
    private byte type = ARROW;
    private int timeToShow = 2000;
    private int creatingMoment = 0;




    public DebugGraphic(byte type, Vec2 startPos, Vec2 endPos){
        this.type = type;
        this.startPos = startPos;
        this.endPos = endPos;
        creatingMoment = Program.engine.millis();
    }

    public DebugGraphic(byte type, PVector startPos, PVector endPos){
        this.type = type;
        this.startPos = new Vec2(startPos.x, startPos.y);
        this.endPos = new Vec2(endPos.x, endPos.y);
        creatingMoment = Program.engine.millis();
    }

    public DebugGraphic(byte type, Vec2 startPos){
        this.type = type;
        if (type == CROSS) {
            this.startPos = startPos;
        }
        creatingMoment = Program.engine.millis();
    }

    public DebugGraphic(byte type, Vec2 startPos, int radius){
        this.type = type;
        if (type == ROUND) {
            this.startPos = startPos;
            this.radius = radius;
        }
        creatingMoment = Program.engine.millis();
    }

    public DebugGraphic(Vec2 startPos, Image image, int width, int height){
        this.type = IMAGE;
        this.startPos = startPos;
        this.debugImage = image;
        this.width = width;
        this.height = height;
        creatingMoment = Program.engine.millis();
    }

    public void draw(GameCamera gameCamera) {
        //Program.objectsFrame.beginDraw();
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.imageMode(PConstants.CENTER);
        if (type == IMAGE) drawImage(gameCamera);
        else if (type == ROUND) drawRound(gameCamera);
        else if (type == ARROW) drawArrow(gameCamera);
        else if (type == LINE) drawLine(gameCamera);
        else drawCross(gameCamera);
        Program.objectsFrame.popMatrix();
        //Program.objectsFrame.endDraw();
    }

    private void drawImage(GameCamera gameCamera) {
        Program.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(), startPos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.image(debugImage.getImage(), 0,0, width,height );

    }

    private void drawRound(GameCamera gameCamera) {
        Program.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(), startPos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.strokeWeight(5);
        Program.objectsFrame.noFill();
        Program.objectsFrame.stroke(25,25,222);

        Program.objectsFrame.ellipse(0, 0, radius*2, radius*2);

    }

    private void drawArrow(GameCamera gameCamera) {
        Program.objectsFrame.translate( - gameCamera.getActualXPositionRelativeToCenter(),  - gameCamera.getActualYPositionRelativeToCenter());
        //Programm.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(), startPos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.strokeWeight(3);
        Program.objectsFrame.stroke(25,25,222);
        if (type == ARROW) {
            Program.objectsFrame.line(startPos.x, startPos.y, endPos.x, endPos.y);
            Program.objectsFrame.ellipse(endPos.x, endPos.y, 15f, 15f);

        }
    }

    private void drawLine(GameCamera gameCamera) {
        //Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        //Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());

        Program.objectsFrame.translate( - gameCamera.getActualXPositionRelativeToCenter(),  - gameCamera.getActualYPositionRelativeToCenter());
        //Programm.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(), startPos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.strokeWeight(7);
        Program.objectsFrame.stroke(25,25,222);
        if (type == LINE) {
            Program.objectsFrame.line(startPos.x, startPos.y, endPos.x, endPos.y);
            //Program.objectsFrame.ellipse(endPos.x, endPos.y, 15f, 15f);

        }
    }

    private void drawCross(GameCamera gameCamera) {


        Program.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(),  startPos.y- gameCamera.getActualYPositionRelativeToCenter());
        //Programm.objectsFrame.translate(startPos.x - gameCamera.getActualXPositionRelativeToCenter(), startPos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.strokeWeight(3);
        Program.objectsFrame.stroke(25,25,222);
        Program.objectsFrame.line(-Program.engine.width/10, 0, Program.engine.width/10, 0);
        Program.objectsFrame.line(0, -Program.engine.width/10, 0, Program.engine.width/10);

    }

    public boolean canBeDeleted(){
        if ((Program.engine.millis()-creatingMoment) > timeToShow){
            return true;
        }
        else return false;
    }

    public void setTimeToShow(int timeToShow) {
                this.timeToShow = timeToShow;
    }
}
