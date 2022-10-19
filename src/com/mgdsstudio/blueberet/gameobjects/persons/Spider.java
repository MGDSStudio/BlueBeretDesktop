package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.SpiderController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class Spider extends Enemy implements IDrawable, ISimpleUpdateable {
    //private EnemyController behaviourController;
    //private static Tileset hitTileset;
    public final static String CLASS_NAME = "Spider";
    private final String objectToDisplayName = CLASS_NAME;
    // We need specific graphic file
    private static final int additionalShiftingAlongX = 40;
    private final static ImageZoneFullData imageZoneGoingData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Spider spritesheet"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()), 12-4, 7+2, 182-4,46+4);
    private final static ImageZoneFullData imageZoneShootData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Spider spritesheet"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()), 8, 93, 348,176);


    //private final static ImageZoneFullData imageZoneFullData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Spider spritesheet.png"), 47-45-21, 7+2, 47+45+92-21,46+4);

    //private final static float additionalGraphicScale  = 3.4f;
    public final static int NORMAL_WIDTH = 40;
    final static int NORMAL_HEIGHT = 33;
    public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
    private int diameter = NORMAL_WIDTH;
    private final float NORMAL_ACCELERATE = 5f;
    public static int NORMAL_LIFE = 200;
    static int normalMovementImpulseX = 5;
    static float maxNormalSpeed = 0.5f;
    private float headRadius, bodyRadius;
    private boolean polygonBody = true;
    private float angle = 0;

    public Spider(PVector position, byte behaviourModel, int life, int diameter) {
        init(position, life, diameter, behaviourModel);
        behaviourController = new SpiderController(this, behaviourModel);
        findPlayerAtStart();
        maxVelocityAlongX = 5;
    }

    private void init(PVector position, int life, int diameter, byte behaviourModel) {
        this.diameter = (int)diameter;
        role = ENEMY;
        boundingWidth = diameter;
        boundingHeight = diameter/2;
        bodyRadius = (2*boundingWidth/3)/3;
        headRadius = 0.92f*(boundingWidth-(bodyRadius*2))/2f;
        //System.out.println("Spider width: " + boundingWidth + "; Body rad: " + bodyRadius + " head: " + headRadius);
        if (!polygonBody) makeTwoCirclesBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
        else makePolygonBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
        maxLife = (int)life;
        setLife((int)life, (int)life);
        this.level = level;
        if (Program.debug) System.out.println("Spider was uploaded");
        body.setFixedRotation(true);
        if (level == FLYING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.FLYING_ALONG_SINUSOID_PATH, this);
        else if (level == JUMPING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_AND_REGULARLY_JUMP, this);
        else if (level == GOING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
        else globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
        findPlayerAtStart();
        mirrorBody();
    }

    private void makePolygonBody(Vec2 center, float width, float height) {
        PolygonShape sd = new PolygonShape();
        Vec2[] vertices = new Vec2[6];
        float offcetY = 0;
        vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, offcetY));
        vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2+offcetY));
        vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2+offcetY));
        vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, offcetY));
        vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/5), -height/1.2f+offcetY));
        vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/5), -height/1.2f+offcetY));
        /*
        vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, offcetY));
        vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2+offcetY));
        vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2+offcetY));
        vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, offcetY));
        vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), -height/2+offcetY));
        vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-2*width/5), -height/2+offcetY));
         */

        sd.set(vertices, vertices.length);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd, 1.0f);
        body.getFixtureList().setDensity(1);
        body.getFixtureList().setFriction(0.2f);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    private void makeTwoCirclesBody(Vec2 center, float width, float height) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape bodyShape = new CircleShape();
        bodyShape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(bodyRadius);
        Vec2 bodyOffset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(+bodyRadius, 0));
        //Vec2 bodyOffset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(((-width/3)), 0));
        bodyShape.m_p.set(bodyOffset.x,bodyOffset.y);
        CircleShape head = new CircleShape();
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(headRadius);
        Vec2 offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(((-width/2)*0.92f)+bodyRadius, (width)*0.15f));
        head.m_p.set(offset.x,offset.y);
        if (center == null) System.out.println("Position is null");
        body.createFixture(bodyShape,1.4f);
        body.createFixture(head, 0.2f);
        moovingDirectionIsChanging = true;
        mirrorBody();
        System.out.println("Spider body was successfully made");
        body.setFixedRotation(true);
        body.setAngularDamping(2);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    public void mirrorBody() {
        if (!polygonBody) {
            if (moovingDirectionIsChanging) {
                if (body.getLinearVelocity().x > 0.1f) {
                    CircleShape head = (CircleShape) getHeadFixture().getShape();
                    head.m_p.x = Math.abs(head.m_p.x);
                    sightDirection = TO_RIGHT;
                } else if (body.getLinearVelocity().x < (-0.1f)) {
                    CircleShape head = (CircleShape) getHeadFixture().getShape();
                    head.m_p.x = -1 * Math.abs(head.m_p.x);
                    sightDirection = TO_LEFT;
                }
            }
        }
    }

    public void mirrorBody(boolean orientation) {
        if (!polygonBody) {
            CircleShape head = (CircleShape) getHeadFixture().getShape();
            if (orientation == TO_RIGHT) {
                head.m_p.x = Math.abs(head.m_p.x);
                sightDirection = TO_RIGHT;
            }
            else {
                head.m_p.x = -1 * Math.abs(head.m_p.x);
                sightDirection = TO_LEFT;
            }
        }
        else{

        }
    }

    private Fixture getHeadFixture(){
        Fixture fixture = null;
        float radius = diameter/2f;
        float actualDelta = 99999f;
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            float shapeRadius = body.getFixtureList().getShape().getRadius();
            if (PApplet.abs(shapeRadius-radius) < actualDelta){
                fixture = f;
                actualDelta = PApplet.abs(shapeRadius-radius);
            }
        }
        return fixture;
    }

    private Fixture getBodyFixture(){
        Fixture head = getHeadFixture();
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (!f.equals(head)) return f;
        }
        return null;
    }



    @Override
    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        if (isVisibleOnScreen(gameCamera, pos)) {
            //behaviourController.draw(gameCamera);
            float a = body.getAngle();
            if (Program.debug) {
                if (polygonBody) drawPolygonBody(pos, a, gameCamera);
            }
            if (!dead) {
                if (behaviourController.isItAttacks()){
                    if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.SHOT).getAnimationStatement() == SpriteAnimation.PAUSED) {
                        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.SHOT).setAnimationStatement(SpriteAnimation.ACTIVE);
                    }
                }
                else if (behaviourController.isItGoing()) {
                    if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.PAUSED) {
                        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.ACTIVE);
                    }
                } else if (statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) {
                    if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.ACTIVE) {
                        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.PAUSED);
                    }
                }
                tintUpdatingBySelecting();
                float graphicAngle = (behaviourController.getGraphicAngle(a));
                boolean graphicFlipX = behaviourController.getGraphicFlipX();
                boolean graphicFlipY = behaviourController.getGraphicFlipY();
                if (behaviourController.getBehaviourType() != SpiderController.GO_ON_WALL && behaviourController.getBehaviourType() != SpiderController.GO_ON_WALL_AND_ATTACK) {
                    if (graphicFlipX == true) {
                        graphicFlipY = true;
                        graphicFlipX = false;
                        graphicAngle = -PApplet.PI;
                        if (angle > 3.14f) {
                            System.out.println("Angle was: " + angle);
                            angle = 0f;
                        }
                    }
                }
                pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
                enemiesAnimationController.drawAnimation(gameCamera, behaviourController.getActualAnimation(), pos, graphicAngle, graphicFlipX, graphicFlipY);
            } else {
                pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
                enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, false);
            }


        }

    }

    private void drawCirclesBody(Vec2 pos, float a, GameCamera gameCamera) {
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.noStroke();
        Program.objectsFrame.strokeWeight(4.5f);
        Program.objectsFrame.fill(25,25,155,150);
        try {
            CircleShape head = (CircleShape) getHeadFixture().getShape();
            Program.objectsFrame.fill(25,100);
            Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(head.m_p).y, headRadius * 2, headRadius * 2);
            CircleShape body = (CircleShape) getBodyFixture().getShape();
            Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(body.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(body.m_p).y, bodyRadius * 2, bodyRadius * 2);
        }
        catch (Exception e) {
            System.out.println("Spider body has no head");
            System.out.println(e);
        }
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
    }

    private void drawPolygonBody(Vec2 pos, float a, GameCamera gameCamera) {
        Fixture f = body.getFixtureList();
        PolygonShape ps = (PolygonShape) f.getShape();
        //Programm.objectsFrame.beginDraw();
        Program.objectsFrame.rectMode(PApplet.CENTER);
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.noFill();
        Program.objectsFrame.stroke(120, 0, 0);
        Program.objectsFrame.strokeWeight(2.5f);
        //if (isDead()) drawCross();
        Program.objectsFrame.strokeWeight(0.8f);
        Program.objectsFrame.beginShape();
        Program.objectsFrame.strokeWeight(2.5f);
        for (int i = 0; i < ps.getVertexCount(); i++) {
            Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
            Program.objectsFrame.vertex(v.x, v.y);
        }
        Program.objectsFrame.endShape(PApplet.CLOSE);
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
    }


    public void update(GameRound gameRound){
        super.update();
        behaviourController.update(gameRound);
        //System.out.println("Update " );
    }

    @Override
    protected void updateAngle() {

    }

    @Override
    public int getPersonWidth() {
        return 0;
    }

    @Override
    public boolean attackByDirectContact(Person nearestPerson) {
        if (nearestPerson.getPixelPosition().x> getPixelPosition().x) {
            System.out.println("Player is left from spider; Spider's orientation is " + orientation);
            if (orientation == TO_LEFT) return false;
            else return true;
        }
        else {
            System.out.println("Player is right from spider; Spider's orientation is " + orientation);
            if (orientation == TO_RIGHT) return false;
            else return true;
        }
    }


    @Override
    public void contactWithMoveableObject(){
        SpiderController spiderController = (SpiderController) behaviourController;
        if (spiderController.isOnWall()){
            spiderController.releaseFromWall(false);
        }
    }

    @Override
    public void attacked(Bullet bullet){
        super.attacked(bullet);
        SpiderController spiderController = (SpiderController) behaviourController;
        if (spiderController.isOnWall()) {
            boolean withRotation = false;
            if (!isAlive()) withRotation = true;
            spiderController.releaseFromWall(withRotation);
        }
    }

    @Override
    public void attacked(int value){
        super.attacked(value);
        behaviourController.attacked();
    }

    @Override
    public void kill() {
        super.kill();
        //behaviourController.kill();
        SpiderController spiderController = (SpiderController) behaviourController;
        if (spiderController.isOnWall()) {
            spiderController.releaseFromWall(true);

        }
    }

    @Override
    public int getAttackValue() {
        return (int) (boundingWidth/2.5f);
    }


    @Override
    public void loadAnimationData(MainGraphicController mainGraphicController){
        super.loadAnimationData(mainGraphicController);
        float additionalGraphicScale  = 2.05f;
        SpriteAnimation spriteAnimation = new SpriteAnimation(imageZoneGoingData.getName(), (int) imageZoneGoingData.leftX, (int) imageZoneGoingData.upperY, (int) imageZoneGoingData.rightX, (int) imageZoneGoingData.lowerY, (int) (boundingWidth*additionalGraphicScale), (int) (boundingHeight*additionalGraphicScale), (byte)1, (byte)2, (int) 8);
        enemiesAnimationController.addNewAnimation(spriteAnimation, EnemiesAnimationController.GO);
        SpriteAnimation shootAnimation = new SpriteAnimation(imageZoneGoingData.getName(), (int) imageZoneShootData.leftX, (int) imageZoneShootData.upperY, (int) imageZoneShootData.rightX, (int) imageZoneShootData.lowerY, (int) (boundingWidth*additionalGraphicScale), (int) (boundingHeight*additionalGraphicScale), (byte)2, (byte)4, (int) 8);
        enemiesAnimationController.addNewAnimation(shootAnimation, EnemiesAnimationController.SHOT);


        enemiesAnimationController.loadSprites(mainGraphicController);
        enemiesAnimationController.setDeadSprite(EnemiesAnimationController.GO, (int)2);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(0,0));

    }

    private void loadHittedSprite(){
        
    }


    @Override
    public String getStringData(){
        String data = new String();
        data = data+CLASS_NAME;
        data+= " 1";
        data+= LoadingMaster.MAIN_DATA_START_CHAR;
        data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).x;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).y;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        //if (behaviourController.getBehaviourType() == )

        data+= (int)behaviourController.getBehaviourType();
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= life;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= diameter;
        System.out.println("Data for spider " + data);
        return data;
    }

    public void setGraphicDimensionFromEditor(int newDiameter){
        System.out.println("Try to set new diameter: " + diameter + " is now " + newDiameter);
        diameter = newDiameter;
        boundingWidth = newDiameter;
        boundingHeight = newDiameter/2;
        enemiesAnimationController.setElementWidth((int) boundingWidth*4);
        enemiesAnimationController.setElementHeight((int) boundingHeight*4);
    }

    @Override
    public Vec2 getPositionForReleasedObject() {
        Vec2 pos = new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(body).x, PhysicGameWorld.controller.getBodyPixelCoord(body).y);
        float randomX = Program.engine.random(-boundingWidth/2, +boundingWidth/2);
        pos.x+=randomX;
        pos.y-=boundingHeight/1.5f;
        return pos;
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }
}
