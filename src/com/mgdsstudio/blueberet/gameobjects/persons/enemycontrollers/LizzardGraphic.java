package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class LizzardGraphic {
    static PApplet engine;
    protected PImage spritesheet;
    protected ImageZoneSimpleData head;
    protected ImageZoneSimpleData body;

    protected ImageZoneSimpleData luggage;
    protected ImageZoneSimpleData luggageEnd;

    protected Coordinate bodyAnchor, headAnchor, bodyCenter, headCenter;
    protected float headAngle = 0f;
    protected float scale  = 1f;

    protected int width, height;
    protected int headWidth, headHeight;

    protected int luggageBasicWidth, luggageBasicHeight, luggageEndWidth, luggageEndHeight;


    protected LizardAttackController lizardAttackController;
    protected LizardHeadController lizardHeadController;

    public LizzardGraphic(PApplet engine, float scale){
        this.scale = scale;
        this.engine = engine;
        body = new ImageZoneSimpleData(65,91, 130,131);
        head = new ImageZoneSimpleData(95,137, 126,159);
        luggage= new ImageZoneSimpleData(96-8,379, 125,394);
        luggageEnd = new ImageZoneSimpleData(94,395, 101,410);

        //head = new ImageZoneSimpleData(65,91+42, 130,131+42);
        width = (int) ((body.rightX-body.leftX)*scale);
        height = (int) ((body.lowerY-body.upperY)*scale);
        headWidth = (int) ((head.rightX-head.leftX)*scale);
        headHeight = (int) ((head.lowerY-head.upperY)*scale);

        luggageBasicWidth = (int) ((luggage.rightX-luggage.leftX)*scale);
        luggageBasicHeight = (int) ((luggage.lowerY-luggage.upperY)*scale);
        luggageEndWidth  = (int) ((luggageEnd.rightX-luggageEnd.leftX)*scale);
        luggageEndHeight = (int) ((luggageEnd.lowerY-luggageEnd.upperY)*scale);

        bodyAnchor = new Coordinate(0,0);
        headAnchor = new Coordinate(14*scale,-3.5f*scale);
        bodyCenter = new Coordinate(engine.width/2, engine.height/2);
        headCenter = new Coordinate(bodyCenter.x+headAnchor.x, bodyCenter.y+headAnchor.y);
        engine.imageMode(PConstants.CENTER);
        spritesheet = engine.loadImage(Program.getAbsolutePathToAssetsFolder("Lizard spritesheet.png"));
        lizardAttackController = new LizardAttackController(null, 300,800, headCenter, 200);
        lizardHeadController = new LizardHeadController(lizardAttackController, 300*4);
    }


    public void update(){
        lizardAttackController.update(headAngle, new PVector(bodyCenter.x, bodyCenter.y), null);
        lizardHeadController.update();
    }
    public void draw(){
        engine.image(spritesheet, bodyCenter.x+bodyAnchor.x, bodyCenter.y+bodyAnchor.y, width, height, body.leftX, body.upperY, body.rightX, body.lowerY);
        drawLuggage();
        engine.pushMatrix();
        engine.translate(bodyCenter.x, bodyCenter.y);
        engine.translate(headAnchor.x, headAnchor.y);
        engine.rotate(PApplet.radians(headAngle));
        if (lizardAttackController.isInHoldZone()) {
            float randomCritValue = headWidth/56.364f;
            engine.translate(engine.random(-randomCritValue, randomCritValue), engine.random(-2.2f, randomCritValue));
        }
        ImageZoneSimpleData actualHead = lizardHeadController.getActualHeadImageData();
        engine.image(spritesheet, 0, 0, headWidth, headHeight, actualHead.leftX, actualHead.upperY, actualHead.rightX, actualHead.lowerY);
        engine.popMatrix();
    }

    public void draw(GameCamera gameCamera, Vec2 pos, float angleInRadians, boolean flip){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-angleInRadians);
        if (flip == Image.FLIP) Program.objectsFrame.scale(-1.0f, 1.0f);
        Program.objectsFrame.imageMode(PConstants.CENTER);
        ImageZoneSimpleData actualHead = lizardHeadController.getActualHeadImageData();
        Program.objectsFrame.image(spritesheet, 0, 0, headWidth, headHeight, actualHead.leftX, actualHead.upperY, actualHead.rightX, actualHead.lowerY);


        Program.objectsFrame.popMatrix();
        /*


        engine.image(spritesheet, bodyCenter.x+bodyAnchor.x, bodyCenter.y+bodyAnchor.y, width, height, body.leftX, body.upperY, body.rightX, body.lowerY);
        drawLuggage();
        engine.pushMatrix();
        engine.translate(bodyCenter.x, bodyCenter.y);
        engine.translate(headAnchor.x, headAnchor.y);
        engine.rotate(PApplet.radians(headAngle));
        if (lizardAttackController.isInHoldZone()) {
            float randomCritValue = headWidth/56.364f;
            engine.translate(engine.random(-randomCritValue, randomCritValue), engine.random(-2.2f, randomCritValue));
        }

        engine.image(spritesheet, 0, 0, headWidth, headHeight, actualHead.leftX, actualHead.upperY, actualHead.rightX, actualHead.lowerY);
        engine.popMatrix();

         */
    }

    private void drawLuggage() {
        if (lizardAttackController.isAttackStarted()){
            Coordinate start = headCenter;
            Coordinate end = lizardAttackController.getActualCoordinate();
            Coordinate center = lizardAttackController.getCenter();
            int width = (int) lizardAttackController.getActualDist();
            engine.pushMatrix();
            engine.translate(center.x, center.y);
            engine.pushStyle();
            engine.strokeWeight(12);
            engine.stroke(255,25,25);
            engine.rotate(PApplet.radians(headAngle));
            engine.image(spritesheet, 0,0, width, luggageBasicHeight, luggage.leftX, luggage.upperY, luggage.rightX, luggage.lowerY);
            engine.image(spritesheet, width/2,0, luggageEndWidth, luggageEndHeight, luggageEnd.leftX, luggageEnd.upperY, luggageEnd.rightX, luggageEnd.lowerY);
            engine.stroke(25,252,25);
            engine.popStyle();
            engine.popMatrix();
        }
    }

    public void incrementAngle(){
        headAngle++;
    }

    public void decrementAngle(){
        headAngle--;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setHeadAngle(float headAngle) {
        if (lizardAttackController.canBeHeadAngleChanged()) {
            this.headAngle = headAngle;
        }
    }

    @Override
    public String toString(){
        String data = "Anchor body: " + bodyAnchor + "; head: " + headAnchor + "; Scale: " + scale;
        return data;
    }

    public void startAttack() {
        lizardAttackController.startAttack(headAngle);
        lizardHeadController.startAttack();
    }
}
