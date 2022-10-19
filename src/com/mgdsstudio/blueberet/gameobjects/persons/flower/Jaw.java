package com.mgdsstudio.blueberet.gameobjects.persons.flower;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Jaw extends Plant{
    //protected static StaticSprite sprite;

    public static int NORMAL_LIFE = 180;
    private boolean side;
    private int jawDiameter;

    public Jaw(PlantController plantController, Vec2 position, int life, boolean side, int pipeInsideDiameter, int basicAngle, int rodHeight, int jawDiameter){
        super(plantController, life, pipeInsideDiameter, basicAngle,  rodHeight);
        this.side = side;
        this.jawDiameter = jawDiameter;
        makeBody(position);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_FLOWER);
    }

    protected void makeBody(Vec2 pos) {
        System.out.println("jawDiameter "+ jawDiameter);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        float xNormalDistanceToCenter = (jawDiameter)/2+2f;
        float yNormalDistanceToCenter = -rodHeight/2f-2;
        if (basicAngle == 0 || basicAngle == 180) {
            xNormalDistanceToCenter = -(jawDiameter)/2f-2;
            yNormalDistanceToCenter = -rodHeight/2f-2;
        }
        PVector pOffset = new PVector(xNormalDistanceToCenter, yNormalDistanceToCenter);
        //System.out.println("Offset for diam: " + jawDiameter + " is " + pOffset);
        pOffset.rotate(PApplet.radians(basicAngle-270));
        Vec2 offset = null;
        if (basicAngle == 90 || basicAngle == 270) {
            if (side == LEFT) offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(pOffset.x, pOffset.y));
            else offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-pOffset.x, pOffset.y));
        }
        else if (basicAngle == 0 || basicAngle == 180){
            if (side == LEFT) offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(pOffset.x, -pOffset.y));
            else offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(pOffset.x, pOffset.y));
        }
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(pos).add(offset));
        body = PhysicGameWorld.controller.createBody(bd);
        PolygonShape jawShape = createShape();

        body.createFixture(jawShape, 1.0f);
        body.setGravityScale(0.1f);
        //if (Game2D.DEBUG) System.out.println("Flower jaw was succesfully made");
    }



    public int getJawDiameter() {
        return jawDiameter;
    }

    private PolygonShape createShape(){
        PolygonShape jawShape = new PolygonShape();
        Vec2[] vertices = new Vec2[4];
        float startAngle = PApplet.radians(270-basicAngle);
        if (basicAngle == 0 || basicAngle == 180) startAngle = PApplet.radians(basicAngle-270);
        if (side == LEFT);
        else startAngle = startAngle-PConstants.PI;
        for (byte i = 0; i < vertices.length; i++) {
            vertices[i] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(
                    (float)((jawDiameter/2)*Math.cos(startAngle)),
                    (float)((jawDiameter/2)*Math.sin(startAngle))));
            if (side == LEFT) startAngle+=PConstants.PI/3;
            else startAngle-=PConstants.PI/3;
        }
        jawShape.set(vertices, vertices.length);
        return jawShape;
    }

    public void draw(GameCamera gameCamera) {
        try {
            if (body != null) {
                float a = 0;
                Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
                try {
                    //Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
                    if (side == LEFT) a = body.getAngle() + RELATIVE_GRAPHIC_ANGLE_FOR_JAWS;
                    else if (side == RIGHT) a = body.getAngle() - RELATIVE_GRAPHIC_ANGLE_FOR_JAWS;
                } catch (Exception e) {
                    System.out.println("Can not drawn");
                }
                if (Program.WITH_GRAPHIC && !hideGraphic) {
                    try {
                        if (side == LEFT) sprite.draw(gameCamera, body);
                        else sprite.draw(gameCamera, body, Image.FLIP);

                    } catch (Exception e) {
                        System.out.println("Can not draw the flower 1");
                    }
                }
                if (Program.debug) {
                    Program.objectsFrame.beginDraw();
                    Program.objectsFrame.pushMatrix();
                    Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                    Program.objectsFrame.pushStyle();
                    Program.objectsFrame.noFill();
                    Program.objectsFrame.strokeWeight(4.8f);
                    Program.objectsFrame.stroke(0);
                    if (pos != null) {
                        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                        Program.objectsFrame.rotate(-a);    //Why-?????
                        Program.objectsFrame.beginShape();
                        PolygonShape ps = (PolygonShape) body.getFixtureList().getShape();
                        if (ps != null) {
                            for (int i = 0; i < ps.getVertexCount(); i++) {
                                Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
                                Program.objectsFrame.vertex(v.x, v.y);
                            }
                        }
                        Program.objectsFrame.endShape(PApplet.CLOSE);
                        Program.objectsFrame.strokeWeight(11);
                        if (ps != null) {
                            Program.objectsFrame.point(0, 0);
                        }

                    }
                    Program.objectsFrame.popStyle();
                    Program.objectsFrame.popMatrix();
                    Program.objectsFrame.endDraw();
                }
            }
        }
        catch ( Exception e){
            System.out.println("By rendering the body was deleted");
            Program.objectsFrame.endShape();
            Program.objectsFrame.popMatrix();
        }
    }

    public void loadImageData(String path, byte part, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        System.out.println("Sprite " + part +  "  was loaded for " + this.getClass());
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
        sprite.setAdditionalAngle((int) (basicAngle-270));
        /*
        if (basicAngle == 90) {
            if (side == LEFT) {
                sprite.setAdditionalAngle((int) -180);
                System.out.println("Additional angle was set for left side ");
            }
            else {
                sprite.setAdditionalAngle((int) -180);
            }
        }
        if (basicAngle == 0) {
            if (side == LEFT) {
                sprite.setAdditionalAngle((int) 90);
                System.out.println("Additional angle was set for left side ");
            }
            else {
                sprite.setAdditionalAngle((int) 90);
            }
        }
        if (basicAngle == 180) {
            if (side == LEFT) {
                sprite.setAdditionalAngle((int) -90);
                System.out.println("Additional angle was set for left side ");
            }
            else {
                sprite.setAdditionalAngle((int) -90);
            }
        }
        */
        //else if (side == RIGHT_JAW) rightJawSprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
        //else System.out.print("There are no part for this picture");
    }

    public void loadSprite(Tileset tileset){
        sprite.loadSprite(tileset);
    }
}
