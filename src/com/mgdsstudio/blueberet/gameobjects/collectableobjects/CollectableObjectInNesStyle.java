package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gamecontrollers.CollectableObjectsController;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.Spring;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;

public class CollectableObjectInNesStyle extends AbstractCollectable {

    //private Spring spring;
    private final String objectToDisplayName = "Collectable object";
    protected Timer jumpingTimer;
    private final static int JUMPING_FREQUENCY_FOR_STARS = 1700;
    private final static float CRITICAL_SPEED_FOR_DETERMINING_OBJECT_AS_STAYING = 2f;   // For stars
    public final static String CLASS_NAME = "CollectableObject";


    //private boolean crushable = false;

    protected final static float NORMAL_MOVEMENT_VELOCITY_ALONG_X = 0.12f;   // for mushrooms and stars
    protected final static float NORMAL_MOVEMENT_VELOCITY_BY_JUMP = NORMAL_MOVEMENT_VELOCITY_ALONG_X*175f;
    private final static float CRITICAL_ANGLE_FOR_MUSHROOM_STOP = 20f;







    public CollectableObjectInNesStyle(Vec2 position, byte type, GameRound gameRound, int dimension, boolean withSpring){
        init(position,type,gameRound,dimension,withSpring);
    }

    public CollectableObjectInNesStyle(GameObjectDataForStoreInEditor objectData, GameRound gameRound){
        init(objectData.getPosition(), objectData.getType(), gameRound, objectData.getDimension(), objectData.isWithSpring());
        loadAnimationData(objectData.getPathToTexture(), objectData.getGraphicLeftX(), objectData.getGraphicUpperY(), objectData.getGraphicRightX(), objectData.getGraphicLowerY(), objectData.getDimension(), objectData.getDimension(), (byte)objectData.getRowsNumber(), (byte)objectData.getCollumnsNumber(), objectData.getAnimationFrequency());
    }

    public CollectableObjectInNesStyle(CollectableObjectInNesStyle objectTemplate, GameRound gameRound) {
        init(GameMechanics.pVectorToVec2(objectTemplate.getPixelPosition()),objectTemplate.type, gameRound, (int)objectTemplate.getWidth(), objectTemplate.withSpring);
        SpriteAnimation spriteAnimation = objectTemplate.getSpriteAnimation();
        loadAnimationData(spriteAnimation.getPath(), spriteAnimation.getxLeft(), spriteAnimation.getyLeft(), spriteAnimation.getxRight(), spriteAnimation.getyRight(), spriteAnimation.getWidth() , spriteAnimation.getHeight(), spriteAnimation.getRowsNumber(), spriteAnimation.getColumnsNumber(), spriteAnimation.getUpdateFrequency());
    }

    protected void init(Vec2 position, byte type, GameRound gameRound, int dimension, boolean withSpring){
        this.type = type;
        boundingWidth = dimension;
        boundingHeight = dimension;
        if (type == ABSTRACT_COIN || type == STAR || type == MUSHROOM) graphicType = SingleGraphicElement.SPRITE_ANIMATION_TYPE;
        else graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        if (CollectableObjectsController.isObjectInAnAnother(position, gameRound)) {
            inWorldPosition = IN_BAG;
            parentPos = position;
            this.withSpring = false;
        }
        else {
            inWorldPosition = IN_WORLD;
            makeBody(position, dimension);
            if (withSpring) {
                spring = new Spring(this);
                this.withSpring = withSpring;
            }
            body.setUserData(CLASS_NAME);
            setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        }
    }

    @Override
    protected void makeBody(Vec2 position, int dimension){
        if (type == ABSTRACT_COIN) {
            if (inWorldPosition == IN_WORLD) makeRoundBody(position, dimension/2);
        }
        else {
            if (type == STAR){
                if (inWorldPosition == IN_WORLD) makePentagonBody(position, dimension);
            }
            else if (type == MUSHROOM){
                if (inWorldPosition == IN_WORLD) makeMushroomBody(position, dimension/2);
            }
            life = IMMORTALY_LIFE;
        }

        body.setUserData(CLASS_NAME);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    @Override
    public String getStringAddedToWorldByBeGained() {
        return "SOME OBJECTS";
    }

    private void makeMushroomBody(Vec2 position, int boundingDiameter) {
        try {
            boundingDiameter*=2;        // to test
            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position).add(new Vec2(0, 0)));
            body = PhysicGameWorld.controller.createBody(bd);
            PolygonShape headShape = new PolygonShape();
            Vec2[] vertices = new Vec2[4];
            float startAngle = 0;
            for (byte i = 0; i < vertices.length; i++) {
                vertices[i] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(
                        (float) ((boundingDiameter / 2) * Math.cos(startAngle)),
                        (float) ((boundingDiameter / 2) * Math.sin(startAngle))));
                startAngle -= PConstants.TWO_PI / 6;
            }
            headShape.set(vertices, vertices.length);
            PolygonShape legShape = new PolygonShape();
            Vec2 offcet = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(0,boundingDiameter/4));
            float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(boundingDiameter / 4);
            float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(boundingDiameter / 4);
            legShape.setAsBox(box2dW, box2dH, offcet, 0f);
            body.createFixture(legShape, 0.9f);
            body.getFixtureList().setFriction(0.01f);
            body.createFixture(headShape, 0.2f);
            body.setGravityScale(1);
            body.setAngularDamping(0.99f);
            body.setLinearDamping(0.01f);
        }
        catch (Exception e){
            System.out.println("Can not make mushroom body: " + e);
        }
    }



    private void makePentagonBody(Vec2 position, int boundingDiameter) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position).add(new Vec2(0,0)));
        body = PhysicGameWorld.controller.createBody(bd);
        PolygonShape shape = new PolygonShape();
        Vec2[] vertices = new Vec2[5];
        float startAngle = 3*PConstants.TWO_PI/20;;
        for (byte i = 0; i < vertices.length; i++) {
            vertices[i] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(
                    (float)((boundingDiameter/2)*Math.cos(startAngle)),
                    (float)((boundingDiameter/2)*Math.sin(startAngle))));
            startAngle+=PConstants.TWO_PI/5;
        }
        shape.set(vertices, vertices.length);
        body.createFixture(shape, 1.0f);
        body.setAngularDamping(0.01f);
        body.setGravityScale(1);
    }





    private void makeRoundBody(Vec2 center, int radius) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape head = new CircleShape();
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(radius);
        body.createFixture(head, 0.5f);
        body.setFixedRotation(false);
        body.getFixtureList().setFriction(0.99f);
        body.setAngularDamping(0.99f);
    }


    public void update(GameRound gameRound) {
        if (springRelaxationController != null){
            springRelaxationController.update();
        }
        if (!stopped) {
            if ((type == MUSHROOM || type == STAR) && !withSpring) {
                if (type == MUSHROOM) {
                    float bodyAngle = PApplet.degrees(body.getAngle());
                    if ((bodyAngle < CRITICAL_ANGLE_FOR_MUSHROOM_STOP && bodyAngle >= 0) || (bodyAngle > (-1 * CRITICAL_ANGLE_FOR_MUSHROOM_STOP) && bodyAngle <= 0)) {
                        if (body.getLinearVelocity().x > 0) {
                            body.applyLinearImpulse(new Vec2(NORMAL_MOVEMENT_VELOCITY_ALONG_X * body.getMass(), 0), body.getPosition(), true);
                        } else {
                            body.applyLinearImpulse(new Vec2(-NORMAL_MOVEMENT_VELOCITY_ALONG_X * body.getMass(), 0), body.getPosition(), true);
                        }
                    }
                } else if (type == STAR) {
                    if (body.getLinearVelocity().x > 0) {
                        body.applyLinearImpulse(new Vec2(NORMAL_MOVEMENT_VELOCITY_ALONG_X * body.getMass(), 0), body.getPosition(), true);
                    } else {
                        body.applyLinearImpulse(new Vec2(-NORMAL_MOVEMENT_VELOCITY_ALONG_X * body.getMass(), 0), body.getPosition(), true);
                    }
                    if (jumpingTimer == null) jumpingTimer = new Timer(JUMPING_FREQUENCY_FOR_STARS);
                    else {
                        if (jumpingTimer.isTime()) {
                            if (isObjectOnPlatformOrAnotherObject()) {
                                jumpingTimer.setNewTimer(JUMPING_FREQUENCY_FOR_STARS);
                                makeJump();
                            }
                        }
                    }

                }
            }
        }
    }

    /*
    public PVector getParentPos(){
        return new PVector(parentPos.x, parentPos.y);
    }*/

    private boolean isObjectOnPlatformOrAnotherObject(){
        if (PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(body.getLinearVelocity().y)) < CRITICAL_SPEED_FOR_DETERMINING_OBJECT_AS_STAYING) return  true;
        else return false;
    }

    private void makeJump(){
        body.applyLinearImpulse(new Vec2(0,NORMAL_MOVEMENT_VELOCITY_BY_JUMP), body.getPosition(), true);
    }


    @Override
    public void setStopped(boolean flag){
        stopped = true;
        if (jumpingTimer!= null) jumpingTimer = null;
    }




    @Override
    public void killBody() {
        try {
            spring.mouseJoint.setFrequency(0.001f);
            spring.destroy();
        }
        catch (Exception e){
            System.out.println("Can not delete mouse joint from the world");
        }
        super.killBody();
    }

    public static String getPathToMainTextureForType(byte type){
        String path = "";
        if (type == ABSTRACT_COIN) path = "Shining coin animation.png";
        else if (type == STAR) path = "Star-collectibles.png";
        else if (type == MUSHROOM) path = "Mushroom animation.png";
        else System.out.println("For this type there are no animation file");
        return path;
    }

    /*
    @Override
    public void deleteSpring(){

        spring.mouseJoint.setDampingRatio(spring.mouseJoint.getDampingRatio() / 4);
        spring.mouseJoint.setFrequency(spring.mouseJoint.getFrequency() / 4);
        spring.mouseJoint.setMaxForce(spring.mouseJoint.getMaxForce() / 4);
        if (withSpring) {
            if (spring != null) {
                if (spring.mouseJoint != null){
                    //System.out.println("Try to delete spring");
                    //System.out.println("Spring was deleted for the collectable element; Vel: " +  body.m_linearVelocity + "; angular: " + body.m_angularVelocity);
                    //spring.mouseJoint.

                    Vec2 velocity = new Vec2(body.getLinearVelocity().x, body.getLinearVelocity().y);
                    float angular = body.getAngularVelocity();
                    PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                    spring.mouseJoint = null;
                    spring = null;
                    withSpring = false;
                    body.setAngularVelocity( angular);
                    body.setLinearVelocity(velocity);
                    body.resetMassData();
                    //System.out.println("Spring was deleted for the collectable element; Vel: " + velocity + "; angular: " + angular);

                }
            }

        }
    }


    public void deleteSpringAfterContact(){
        if (withSpring) {
            if (spring != null) {
                if (spring.mouseJoint != null){
                    //System.out.println("Spring was deleted for the collectable element; Vel: " +  body.m_linearVelocity + "; angular: " + body.m_angularVelocity);
                    //spring.mouseJoint.
                    Vec2 velocity = new Vec2(body.getLinearVelocity().x, body.getLinearVelocity().y);
                    float angular = body.getAngularVelocity();
                    PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                    spring.mouseJoint = null;
                    spring = null;
                    withSpring = false;
                    body.setAngularVelocity( angular);
                    body.setLinearVelocity(velocity);
                    body.resetMassData();
                    System.out.println("Spring was deleted for the collectable element; Vel: " + velocity + "; angular: " + angular);

                }
            }

        }
    }

    */




    @Override
    public void deleteSpring(){
        if (withSpring){
            /*
            cameraSpring.mouseJoint.setDampingRatio(cameraSpring.mouseJoint.getDampingRatio() / 4);
			cameraSpring.mouseJoint.setFrequency(cameraSpring.mouseJoint.getFrequency() / 4);
			cameraSpring.mouseJoint.setMaxForce(cameraSpring.mouseJoint.getMaxForce() / 4);
             */
            spring.mouseJoint.setDampingRatio(spring.mouseJoint.getDampingRatio() / 20);
            spring.mouseJoint.setFrequency(spring.mouseJoint.getFrequency() / 20);
            spring.mouseJoint.setMaxForce(0f);
            System.out.println("Spring pos: " + PhysicGameWorld.controller.coordWorldToPixels(spring.mouseJoint.getTarget()) + "; Body base: " + PhysicGameWorld.controller.coordWorldToPixels(PhysicGameWorld.controller.getGroundBody().getPosition()));
            spring.mouseJoint.setTarget(body.getPosition());
            PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
            spring.mouseJoint = null;
            //springRelaxationController = new SpringRelaxationController(spring, body);
            withSpring = false;
        }
    }




    public static Vec2 getLeftUpperGraphicCornerForType(byte type){
        Vec2 coordinate = new Vec2(0,0);
        if (type == ABSTRACT_COIN) {
            coordinate.x = 0;
            coordinate.y = 0;
        }
        else if (type == STAR) {
            coordinate.x = 0;
            coordinate.y = 0;
        }
        else if (type == MUSHROOM) {
            coordinate.x = 0;
            coordinate.y = 0;
        }
        return coordinate;
    }

    public static Vec2 getRightLowerGraphicCornerForType(byte type){
        Vec2 coordinate = new Vec2(0,0);
        if (type == ABSTRACT_COIN) {
            coordinate.x = 200;
            coordinate.y = 150;
        }
        else if (type == STAR) {
            coordinate.x = 390;
            coordinate.y = 192;
        }
        else if (type == MUSHROOM) {
            coordinate.x = 512;
            coordinate.y = 512;
        }
        return coordinate;
    }

    public static byte getRowsNumberForAnimation(byte type){
        byte value = 1;
        if (type == ABSTRACT_COIN) value = 2;
        else if (type == STAR) value = 1;
        else if (type == MUSHROOM) value = 5;
        return value;
    }

    public static byte getCollumnsNumberForAnimation(byte type){
        byte value = 1;
        if (type == ABSTRACT_COIN) value = 3;
        else if (type == STAR) value = 2;
        else if (type == MUSHROOM) value = 6;
        return value;
    }

    public static int getAnimationFrequency(byte type){
        int value = 66;
        /*
        if (type == COIN) value = 3;
        else if (type == STAR) value = 2;
        else if (type == MUSHROOM) value = 1;*/
        return value;
    }


    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createCollectableObject();
        System.out.println("Data string for collectable object" + saveMaster.getDataString());
        return saveMaster.getDataString();
    }
    //private void draw

    public void draw(GameCamera gameCamera){
        if (inWorldPosition == IN_WORLD) {
            Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
            if (Program.debug && !hasGraphic()) {
                //Programm.objectsFrame.beginDraw();
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(-body.getAngle());
                Program.objectsFrame.pushStyle();
                Program.objectsFrame.stroke(45, 145, 45);
                Program.objectsFrame.noFill();
                Program.objectsFrame.strokeWeight(3.8f);
                try {
                    if (type == STAR || type == MUSHROOM) {
                        int triyngNumber = 0;
                        //System.out.println("Try to draw ");
                        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
                            PolygonShape ps = (PolygonShape) f.getShape();
                            Program.objectsFrame.beginShape();
                            for (int i = 0; i < ps.getVertexCount(); i++) {
                                Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
                                Program.objectsFrame.vertex(v.x, v.y);
                            }
                            Program.objectsFrame.endShape(PApplet.CLOSE);
                            //System.out.println("Numbers " + triyngNumber);
                            triyngNumber++;
                        }
                        //Fixture f = body.getFixtureList();
                    } else if (type == ABSTRACT_COIN) Program.objectsFrame.ellipse(0, 0, boundingWidth, boundingHeight);
                }
                catch (Exception e){
                    System.out.println("Can not draw this object; Type " + type + " Exc: " + e);
                }
                Program.objectsFrame.popStyle();
                Program.objectsFrame.popMatrix();
                //Programm.objectsFrame.endDraw();
            }
            if (hasGraphic()) {
                if (graphicType == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
                    sprite.draw(gameCamera, body);
                }
                else if (graphicType == SingleGraphicElement.SPRITE_ANIMATION_TYPE){
                    spriteAnimation.update();
                    //if (type == MUSHROOM) System.out.println("Animation");
                    spriteAnimation.draw(gameCamera, pos, PApplet.degrees(body.getAngle()), false);
                }
            }
        }
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    @Override
    public String getClassName(){
        return CLASS_NAME;
    }

    public void collisionWithBullet(Bullet bullet){

    }
}
