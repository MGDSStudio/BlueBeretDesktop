package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PConstants;

public class RoundCircle extends RoundElement implements ISimpleUpdateable, IDrawable{
    private final String objectToDisplayName = "Circle";
    public final static String CLASS_NAME = "RoundCircle";

    public RoundCircle (Vec2 position, int radius, int life, boolean withSpring, BodyType bodyType) {
        makeBody(new Vec2(position.x, position.y), radius, withSpring, bodyType);
        setLife(life, life);
        this.withSpring = withSpring;
        if (withSpring) {
            spring = new Spring(this);
            if (withSpring) body.setFixedRotation(true);
        }
        boundingWidth = radius*2;
        boundingHeight = radius*2;
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    public RoundCircle(GameObjectDataForStoreInEditor data) {
        withSpring = data.isWithSpring();
        BodyType bodyType = data.getBodyType();
        Vec2 position = data.getPosition();
        int radius = data.getDiameter()/2;
        boundingWidth = radius*2;
        boundingHeight = radius*2;
        System.out.println("Radius: " + radius);
        if (withSpring)  bodyType = BodyType.DYNAMIC;
        //makeBody(data.getPosition(), data.getAngle(), data.getWidth(), data.getHeight(), bodyType);
        makeBody(position, radius, withSpring, bodyType);
        setLife((int)data.getLife(), (int)data.getLife());
        if (withSpring) {
            spring = new Spring(this);
            body.setFixedRotation(true);
        }
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        boolean fill = false;
        if (data.getFill()!=0) fill = true;
        float graphicDelta = data.getWidth()/data.getHeight();
        System.out.println("Delta: " + graphicDelta);
        if (graphicDelta<2){
            System.out.println("For this block is fill data wrong");
        }
        //boolean realFillValue = testFillAreaValue(data.getWidth(), data.getHeight(), fill);
        loadImageData(data.getPathToTexture(), data.getGraphicLeftX(), data.getGraphicUpperY(), data.getGraphicRightX(), data.getGraphicLowerY(), data.getWidth(), data.getHeight(), false);
    }

    private void makeBody(Vec2 center, int radius, boolean withSpring, BodyType bodyType) {
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape shape = new CircleShape();
        shape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(radius);
        body.createFixture(shape, 1f);
        body.getFixtureList().setFriction(0.99f);
        body.setAngularDamping(0.99f);
        body.setGravityScale(1f);
    }

    protected void drawDebugModel(GameCamera gameCamera, Vec2 pos, float a){
        //Programm.objectsFrame.beginDraw();
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.noFill();
        Program.objectsFrame.rectMode(PConstants.CENTER);
        Program.objectsFrame.strokeWeight(1.8f);
        Program.objectsFrame.stroke(0);
        Program.objectsFrame.ellipse(0, 0, boundingWidth, boundingHeight);
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
        //Programm.objectsFrame.endDraw();
    }

    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        if (isVisibleOnScreen(gameCamera, pos)) {
            updateGraphicSidesData();
            float a = body.getAngle();
            if (Program.debug) {
                drawDebugModel(gameCamera, pos, a);
            }
            if (spriteAnimation != null) {
                spriteAnimation.update();
                spriteAnimation.draw(gameCamera, pos, a, false);
            }
            if (sprite != null) {
                sprite.draw(gameCamera, body);
            }

        }
        //else System.out.println("The object is not visible");
    }

    @Override
    public void update() {

    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    @Override
    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea){
        boolean realFillValue = testFillAreaValue(width,height,fillArea);
        if (Program.gameStatement == Program.LEVELS_EDITOR){
            if (path == MainGraphicController.WITHOUT_GRAPHIC_STRING){
                path = StaticSprite.INVISIBLE_WALL;
                System.out.println("Path for round circle was set on " + path);
                xLeft = 0;
                yLeft = 0;
                xRight = 48;
                yRight = 50;
            }
        }
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height, realFillValue);
    }

    @Override
    public String getStringData(){
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createRoundCircle();
        System.out.println("Data string for round circle: " +saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    public String getClassName(){
        return CLASS_NAME;
    }
}
