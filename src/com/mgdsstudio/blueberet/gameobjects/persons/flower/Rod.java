package com.mgdsstudio.blueberet.gameobjects.persons.flower;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
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

public class Rod extends Plant{
    public static int NORMAL_LIFE = 150;
    //protected static StaticSprite sprite;
    private int thickness;
    private final static int NORMAL_THICKNESS = 18;



    public Rod(PlantController plantController, Vec2 position, int life, int pipeInsideDiameter, int basicAngle, int rodHeight){
        super(plantController, life,pipeInsideDiameter,basicAngle, rodHeight);
        thickness = NORMAL_THICKNESS;
        makeBody(position);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_FLOWER);
    }

    protected void makeBody(Vec2 position) {
        PolygonShape sd = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(thickness/2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(rodHeight/2);
        sd.setAsBox(box2dW, box2dH);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        Vec2 offset = new Vec2(0, 0);	//	    Vec2 offset = new Vec2(0, );
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position.add(offset)));
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd,1);
        body.setGravityScale(1);
        body.setTransform(body.getPosition(), PApplet.radians(basicAngle+90));
        if (Program.debug) System.out.println("Flower rod was successfully made");
    }

    public float getHeight(){
        return rodHeight;
    }

    public void draw(GameCamera gameCamera) {
        try {
            if (body != null) {
                if (sprite != null && body != null) {
                    Program.objectsFrame.imageMode(PConstants.CENTER);
                    if (!hideGraphic) {
                        //System.out.println("SPrite drawn");
                        sprite.draw(gameCamera, PhysicGameWorld.getBodyPixelCoord(body), body.getAngle());
                    }
                }
                if (Program.debug) {
                    if (body != null) {
                        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
                        float angle = body.getAngle();
                        //Program.objectsFrame.beginDraw();
                        Program.objectsFrame.pushMatrix();
                        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                        Program.objectsFrame.pushStyle();
                        Program.objectsFrame.noFill();
                        Program.objectsFrame.strokeWeight(2.8f);
                        Program.objectsFrame.stroke(0);
                        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                        Program.objectsFrame.rotate(-angle);
                        Program.objectsFrame.rectMode(PConstants.CENTER);
                        Program.objectsFrame.rect(0, 0, thickness, rodHeight);
                        //System.out.println("Draw rod");
                        Program.objectsFrame.popStyle();
                        Program.objectsFrame.popMatrix();
                        //System.out.println("Rod Drawn");
                        //Program.objectsFrame.endDraw();
                    }
                }
            }
        }
        catch ( Exception e){
            System.out.println("By rendering the body was deleted");
            //Program.objectsFrame.endShape();
            //Program.objectsFrame.popMatrix();
        }
    }

    public void loadImageData(String path, byte part, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        System.out.println("Sprite " + part +  "  was loaded for " + this.getClass());
        System.out.println("IT must not be loaded. It must be got from the mainGraphicFontroller");
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
    }

    public void loadSprite(Tileset tileset){
        sprite.loadSprite(tileset);
    }
}
