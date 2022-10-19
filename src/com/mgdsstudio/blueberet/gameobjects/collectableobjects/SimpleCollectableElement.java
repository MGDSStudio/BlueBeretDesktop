package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gamecontrollers.CollectableObjectsController;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.Spring;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.PApplet;

public abstract class SimpleCollectableElement extends AbstractCollectable{
    public final static String CLASS_NAME = "ObjectToBeCollected";
    protected ImageZoneSimpleData graphicData;
    public final static int BODY_WITH_SPRING = 0;
    public final static int STATIC_BODY_WITH_TRANSFER_TO_DYNAMIC = 1;
    public final static int STATIC_BODY = 2;
    public final static int DYNAMIC_BODY_WITH_0_GRAVITY = 3;
    public final static int DYNAMIC_BODY_WITH_NORMAL_GRAVITY = 4;
    protected int fixationType = STATIC_BODY;
    protected int width, height;
    protected float dimensionCoefficient = 2.5f;  //was 1.2f
    protected final static boolean BODY_FORM_RECT = true, BODY_FORM_CIRCLE = false;
    private final float  MAX_DELTA_POS_FOR_DYNAMIC_OBJECTS = 0.01f;
    private boolean releasedForDynamicWithoutSpringObjects = false;
    public final static float NORMAL_GRAVITY_SCALE = 1.5f;


    protected void init(Vec2 position, GameRound gameRound, int width, int height, boolean bodyForm){
        boundingWidth = width;
        boundingHeight = height;
        parentPos = position;
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        if (CollectableObjectsController.isObjectInAnAnother(position, gameRound)) {
            System.out.println("This object is in bag");
            inWorldPosition = IN_BAG;
            parentPos = position;
            this.withSpring = false;
        }
        else {
            inWorldPosition = IN_WORLD;
            if (bodyForm == BODY_FORM_RECT) makeRectBody(boundingWidth, boundingHeight);
            else makeCircleBody(boundingWidth, boundingHeight);
            body.setUserData(CLASS_NAME);
            setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        }
    }

    public void addBodyToWorld(Vec2 center){
        parentPos = center;
        inWorldPosition = IN_WORLD;
        makeRectBody(boundingHeight, boundingWidth);
        body.setUserData(CLASS_NAME);
        //body
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        //if (withSpring) spring = new Spring(this);
    }

    protected void calculateDimensions(float dimensionCoefficient) {
        width = (int) ((graphicData.rightX-graphicData.leftX)*dimensionCoefficient);
        height = (int) ((graphicData.lowerY-graphicData.upperY)*dimensionCoefficient);
        sprite.setWidth(width);
        sprite.setHeight(height);
    }

    protected void calculateDimensions() {
        width = (int) ((graphicData.rightX-graphicData.leftX)*dimensionCoefficient);
        height = (int) ((graphicData.lowerY-graphicData.upperY)*dimensionCoefficient);
        sprite.setWidth(width);
        sprite.setHeight(height);
    }

    private void makeRectBody(float width, float height){
        PolygonShape sd = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(height/2);
        sd.setAsBox(box2dW, box2dH);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 1.5f;
        fd.friction = 0.1f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();

        //setBodyType(bd, fixationType);
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(parentPos));
        bd.angle = (float) Math.toRadians(angleInDegrees);
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(fd);
        body.setGravityScale(NORMAL_GRAVITY_SCALE);
        setBodyType(body, fixationType);
    }

    private void makeCircleBody(float width, float height){
        BodyDef bd = new BodyDef();
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(parentPos));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape shape = new CircleShape();
        shape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/2);
        body.createFixture(shape, 1.5f);
        body.setGravityScale(NORMAL_GRAVITY_SCALE);
        body.getFixtureList().setFriction(0.99f);
        setBodyType(body, fixationType);

        /*

        PolygonShape sd = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(width/2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(height/2);
        sd.setAsBox(box2dW, box2dH);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 1.5f;
        fd.friction = 0.1f;
        fd.restitution = 0.05f;
        BodyDef bd = new BodyDef();

        //setBodyType(bd, fixationType);
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(parentPos));
        bd.angle = (float) Math.toRadians(angle);
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(fd);
        body.setGravityScale(1.5f);
        setBodyType(body, fixationType);*/

    }

    private void setBodyType(BodyDef bodyDef, int fixationType){
        if (fixationType == STATIC_BODY || fixationType == STATIC_BODY_WITH_TRANSFER_TO_DYNAMIC){
            bodyDef.type = BodyType.STATIC;
        }
        else if (fixationType == DYNAMIC_BODY_WITH_0_GRAVITY){
            bodyDef.type = BodyType.DYNAMIC;
        }
        else if (fixationType == DYNAMIC_BODY_WITH_NORMAL_GRAVITY){
            bodyDef.type = BodyType.DYNAMIC;
            bodyDef.setGravityScale(NORMAL_GRAVITY_SCALE);
        }
        else if (fixationType == BODY_WITH_SPRING){
            bodyDef.type = BodyType.DYNAMIC;
            spring = new Spring(this);
            withSpring = true;
        }
    }

    private void setBodyType(Body body, int fixationType){
        if (fixationType == STATIC_BODY || fixationType == STATIC_BODY_WITH_TRANSFER_TO_DYNAMIC){
            body.setType(BodyType.STATIC);
        }
        else if (fixationType == DYNAMIC_BODY_WITH_0_GRAVITY){
            body.setType(BodyType.DYNAMIC);
            body.setGravityScale(0);
            body.setLinearVelocity(new Vec2(0,0));
            withSpring = false;
        }
        else if (fixationType == DYNAMIC_BODY_WITH_NORMAL_GRAVITY){
            body.setType(BodyType.DYNAMIC);
            body.setGravityScale(NORMAL_GRAVITY_SCALE);
            body.setLinearVelocity(new Vec2(0,0));
            withSpring = false;
        }
        else if (fixationType == BODY_WITH_SPRING){
            body.setType(BodyType.DYNAMIC);
            spring = new Spring(this);
            withSpring = true;
        }
    }


    @Override
    public void draw(GameCamera gameCamera){
        if (inWorldPosition == IN_WORLD) {
            Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
            if (Program.debug && !hasGraphic()) {
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-body.getAngle());
                Program.objectsFrame.pushStyle();
                Program.objectsFrame.stroke(45, 145, 45);
                Program.objectsFrame.noFill();
                Program.objectsFrame.strokeWeight(3.8f);
                try {
                    int triyngNumber = 0;
                    for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
                        PolygonShape ps = (PolygonShape) f.getShape();
                        Program.objectsFrame.beginShape();
                        for (int i = 0; i < ps.getVertexCount(); i++) {
                            Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
                            Program.objectsFrame.vertex(v.x, v.y);
                        }
                        Program.objectsFrame.endShape(PApplet.CLOSE);
                        triyngNumber++;
                    }

                }
                catch (Exception e){
                    //System.out.println("Can not draw this object; Type " + weaponType + " Exc: " + e);
                }
                Program.objectsFrame.popStyle();
                Program.objectsFrame.popMatrix();

            }
            if (hasGraphic()) {
                if (graphicType == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
                    tintUpdatingBySelecting();
                    sprite.draw(gameCamera, body);
                    //System.out.println("Sprite was drawn  " + (sprite.getTileset() == null) + " and " + sprite.getxLeft() + 'x' + sprite.getyLeft() + "x" + sprite.getxRight() + "x" + sprite.getyRight());

                }
                else if (graphicType == SingleGraphicElement.SPRITE_ANIMATION_TYPE){
                    //spriteAnimation.update();
                    //spriteAnimation.draw(gameCamera, pos, PApplet.degrees(body.getAngle()), false);
                }
            }
            else {
                System.out.println("It has no graphic");
            }
            if (inWorldPosition == IN_WORLD) {
                drawStars(gameCamera);
            }
        }
    }

    public void update(GameRound gameRound) {
        if (springRelaxationController != null){
            springRelaxationController.update();
        }
        else{
            if (Program.engine.frameCount%4 == 0) {
                if (!releasedForDynamicWithoutSpringObjects && fixationType == DYNAMIC_BODY_WITH_0_GRAVITY && !withSpring) {
                    if (PApplet.abs(positionInPrevFrame.x - getPixelPosition().x) > MAX_DELTA_POS_FOR_DYNAMIC_OBJECTS || PApplet.abs(positionInPrevFrame.y - getPixelPosition().y) > MAX_DELTA_POS_FOR_DYNAMIC_OBJECTS) {
                        if (((positionInPrevFrame.x > MAX_DELTA_POS_FOR_DYNAMIC_OBJECTS) || (positionInPrevFrame.x < -MAX_DELTA_POS_FOR_DYNAMIC_OBJECTS))) {
                            releasedForDynamicWithoutSpringObjects = true;
                            body.setGravityScale(1);
                            //System.out.println("Object was released");
                        } else {
                            positionInPrevFrame.x = getPixelPosition().x;
                            positionInPrevFrame.y = getPixelPosition().y;
                            //System.out.println("POsition was updated 1");
                        }
                    } else {
                        positionInPrevFrame.x = getPixelPosition().x;
                        positionInPrevFrame.y = getPixelPosition().y;
                        //System.out.println("POsition was updated 2");
                    }
                }
                //System.out.println("fix type: " + fixationType);
            }
        }
    }

    @Override
    public void collisionWithBullet(Bullet bullet){

        if (fixationType == DYNAMIC_BODY_WITH_0_GRAVITY){
            body.setGravityScale(1);
            deleteSpring();
            withSpring = false;
        }
        else if (fixationType == BODY_WITH_SPRING){
            System.out.println("Try to release magazine");
            if (withSpring) deleteSpring();
        }
    }

    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea){
        /*boolean realFillValue = testFillAreaValue(width,height,fillArea);
        if (Programm.gameStatement == Programm.LEVELS_EDITOR){
            if (path == MainGraphicController.WITHOUT_GRAPHIC_STRING){
                path = StaticSprite.BLACK_RECT_PATH;
                System.out.println("Path for round box was set on " + path);
            }
        }
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height, realFillValue);*/
    }


    @Override
    public String getStringData(){
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createSimpleCollectableElement();
        System.out.println("Data string for simple collectable element: " + saveMaster.getDataString());
        return saveMaster.getDataString();
    }


    @Override
    protected void makeBody(Vec2 center, int boundingWidth) {
        parentPos = center;
        makeRectBody(boundingWidth,boundingWidth);
    }

    public int getFixationType(){
        return fixationType;
        /*
        int type = 0;
        if (inWorldPosition == IN_BAG){
            fixationType = DYNAMIC_BODY;
        }
        else {
            if (body != null){
                f
            }
            else{
                System.out.println("Body for collectable object is null");
                type = STATIC_BODY;
            }

        }*/

        /*
        if (fixationType == STATIC_BODY || fixationType == STATIC_BODY_WITH_TRANSFER_TO_DYNAMIC){
            bodyDef.type = BodyType.STATIC;
        }
        else if (fixationType == DYNAMIC_BODY){
            bodyDef.type = BodyType.DYNAMIC;
        }
        else if (fixationType == BODY_WITH_SPRING){
            bodyDef.type = BodyType.DYNAMIC;
            spring = new Spring(this);
            withSpring = true;
        }
         */
    }



    protected boolean isGem(){
        if (type == SMALL_RED_STONE || type == BIG_RED_STONE) return  true;
        else if (type == SMALL_GREEN_STONE || type == BIG_GREEN_STONE) return  true;
        else if (type == SMALL_BLUE_STONE || type == BIG_BLUE_STONE) return  true;
        else if (type == SMALL_YELLOW_STONE || type == BIG_YELLOW_STONE) return  true;
        else if (type == SMALL_WHITE_STONE || type == BIG_WHITE_STONE) return  true;
        else {
            System.out.println("This object has type " + type + " and it's not a stone");
            return false;
        }
    }

}
