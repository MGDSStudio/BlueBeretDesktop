package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.BossBoarController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class BossBoar extends Enemy{
    public final static String CLASS_NAME = "BossBoar";
    //private GameRound gameRound;
    private BodyData bodyData;

    public BossBoar(PVector pos, int width, int life, Flag leftLower, Flag leftUpper, Flag rightLower, Flag rightUpper, GameRound gameRound, Flag rightLowerJump, Flag rightUpperJump, Flag leftUpperJump, Flag leftLowerJump){
        init(pos, width, life, leftLower, leftUpper, rightLower, rightUpper, gameRound, rightLowerJump, rightUpperJump, leftUpperJump, leftLowerJump);
        findPlayerAtStart();
        maxVelocityAlongX = 15;
        maxVelocityAlongXByRunning = 28;
        actualAccelerate = 95;
        jumpStartSpeed = jumpStartSpeed*1.00f;
    }

    public static BossBoar createPersonFromData(int[] values, GameRound gameRound){
        //BossBoar 1:510,1640,150,500,-40,1640,240,80,-120,1400,240,80,680,1640,240,80,760,1400,240,80
        PVector position = new PVector(values[0], values[1]);
        int width = values[2];
        int life = values[3];
        int zoneWidth = values [12];
        int zoneHeight = values [13];
        Flag leftLower = new Flag(new PVector(values[4], values[5]), zoneWidth, zoneHeight, Flag.PATROL_AREA);
        Flag leftUpper = new Flag(new PVector(values[6], values[7]), zoneWidth, zoneHeight, Flag.PATROL_AREA);
        Flag rightLower = new Flag(new PVector(values[8], values[9]), zoneWidth, zoneHeight, Flag.PATROL_AREA);
        Flag rightUpper = new Flag(new PVector(values[10], values[11]), zoneWidth, zoneHeight, Flag.PATROL_AREA);

        int jumpZoneWidth = values [22];
        int jumpZoneHeight = values [23];
        Flag rightLowerJump = new Flag(new PVector(values[14], values[15]), jumpZoneWidth, jumpZoneHeight, Flag.JUMP_AREA);
        Flag rightUpperJump = new Flag(new PVector(values[16], values[17]), jumpZoneWidth, jumpZoneHeight, Flag.JUMP_AREA);
        Flag leftUpperJump = new Flag(new PVector(values[18], values[19]), jumpZoneWidth, jumpZoneHeight, Flag.JUMP_AREA);
        Flag leftLowerJump = new Flag(new PVector(values[20], values[21]), jumpZoneWidth, jumpZoneHeight, Flag.JUMP_AREA);


        System.out.println("Boar created at " + position.x + "x" + position.y + " and width " + width);
        BossBoar boar = new BossBoar(position, width, life, leftLower, leftUpper, rightLower, rightUpper, gameRound, rightLowerJump, rightUpperJump, leftUpperJump, leftLowerJump);
        return boar;
    }

    /*
    public static BossBoar createPersonFromData(int[] values, GameRound gameRound){
        //BossBoar 1:510,1640,150,500,-40,1640,240,80,-120,1400,240,80,680,1640,240,80,760,1400,240,80
        PVector position = new PVector(values[0], values[1]);
        int width = values[2];
        int life = values[3];
        Flag leftLower = new Flag(new PVector(values[4], values[5]), values[6], values[7], Flag.PATROL_AREA);
        Flag leftUpper = new Flag(new PVector(values[8], values[9]), values[10], values[11], Flag.PATROL_AREA);
        Flag rightLower = new Flag(new PVector(values[12], values[13]), values[14], values[15], Flag.PATROL_AREA);
        Flag rightUpper = new Flag(new PVector(values[16], values[17]), values[18], values[19], Flag.PATROL_AREA);
        System.out.println("Boar created at " + position.x + "x" + position.y + " and width " + width);
        BossBoar boar = new BossBoar(position, width, life, leftLower, leftUpper, rightLower, rightUpper, gameRound);
        return boar;
    }
     */

    private void init(PVector pos, int width, int life, Flag leftLower, Flag leftUpper, Flag rightLower, Flag rightUpper, GameRound gameRound, Flag rightLowerJump, Flag rightUpperJump, Flag leftUpperJump, Flag leftLowerJump) {
        this.boundingWidth = width;
        this.boundingHeight = width*2.5f/4f;
        role = ENEMY;
        maxLife = life;
        setLife(life, life);
        makeBody(pos, width);
        body.setFixedRotation(true);
        behaviourController = new BossBoarController(this, leftLower, leftUpper, rightLower, rightUpper, gameRound, rightLowerJump, rightUpperJump, leftUpperJump, leftLowerJump);
    }



    private void makeBody(PVector position, int width) {
        bodyData = new BodyData(width);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position));
        body = PhysicGameWorld.controller.createBody(bd);

        float bodyWidth = bodyData.bodyWidth;
        float bodyHeight = bodyData.bodyHeight;

        Coordinate bodyAnchor = bodyData.bodyAnchor;
        Vec2[] bodyVertices = new Vec2[4];
        PolygonShape bodyTrapezoidShape = new PolygonShape();
        bodyVertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x+bodyWidth/2, bodyAnchor.y));    //right lower
        bodyVertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x+bodyWidth/2-bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        bodyVertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x-bodyWidth/2+bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        bodyVertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x-bodyWidth/2, bodyAnchor.y)); // left lower
        bodyTrapezoidShape.set(bodyVertices, bodyVertices.length);



        Vec2 backFeetAnchor = bodyData.rightFeetAnchor;
        Vec2[] backFeetVertices = new Vec2[4];
        float feetWidth = bodyData.feetWidth;
        float feetHeight = bodyData.feetHeight;
        PolygonShape backFeetTrapezoidShape = new PolygonShape();
        backFeetVertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(backFeetAnchor.x+feetWidth/2, backFeetAnchor.y+feetHeight/2));    //right lower
        backFeetVertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(backFeetAnchor.x+feetWidth/2, backFeetAnchor.y-feetHeight/2)));
        backFeetVertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(backFeetAnchor.x-feetWidth/2, backFeetAnchor.y+feetHeight/2)));
        backFeetVertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(backFeetAnchor.x-feetWidth/2, backFeetAnchor.y-feetHeight/2)); // left lower
        backFeetTrapezoidShape.set(backFeetVertices, backFeetVertices.length);


        Vec2 frontFeetAnchor = bodyData.leftFeetAnchor;
        Vec2[] frontFeetVertices = new Vec2[4];
        PolygonShape frontFeetTrapezoidShape = new PolygonShape();
        frontFeetVertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(frontFeetAnchor.x+feetWidth/2, frontFeetAnchor.y+feetHeight/2));    //right lower
        frontFeetVertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(frontFeetAnchor.x+feetWidth/2, frontFeetAnchor.y-feetHeight/2)));
        frontFeetVertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(frontFeetAnchor.x-feetWidth/2, frontFeetAnchor.y+feetHeight/2)));
        frontFeetVertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(frontFeetAnchor.x-feetWidth/2, frontFeetAnchor.y-feetHeight/2)); // left lower
        frontFeetTrapezoidShape.set(frontFeetVertices, frontFeetVertices.length);



        bodyData.worldCenterForBodyAfterDead = PhysicGameWorld.controller.vectorPixelsToWorld(bodyAnchor.x, frontFeetAnchor.y);

        float stomachWidth = bodyData.stomachWidth;
        float stomachHeight = feetHeight*0.8f;
        Vec2 stomachAnchor = bodyData.stomachAnchor;
        Vec2[] stomachVertices = new Vec2[4];
        PolygonShape stomachShape = new PolygonShape();
        stomachVertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(stomachAnchor.x+stomachWidth/2, stomachAnchor.y+stomachHeight/2));    //right lower
        stomachVertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(stomachAnchor.x+stomachWidth/2, stomachAnchor.y-stomachHeight/2)));
        stomachVertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(stomachAnchor.x-stomachWidth/2, stomachAnchor.y+stomachHeight/2)));
        stomachVertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(stomachAnchor.x-stomachWidth/2, stomachAnchor.y-stomachHeight/2)); // left lower
        stomachShape.set(stomachVertices, stomachVertices.length);

        body.createFixture(stomachShape, 3.1f);
        body.getFixtureList().setUserData(BodyData.TUMMY);
        body.getFixtureList().setFriction(0.6f);
        body.createFixture(bodyTrapezoidShape, 1.3f);
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.getUserData() == null){
                f.setUserData(BodyData.BODY);
                f.setFriction(0.8f);
            }
        }

        body.createFixture(frontFeetTrapezoidShape, 3.1f);
        //body.getFixtureList().getNext().setUserData(BodyData.FEET);
        body.getFixtureList().getNext().setFriction(0.8f);
        body.createFixture(backFeetTrapezoidShape, 3.1f);
        //body.getFixtureList().getNext().getNext().setUserData(BodyData.FEET);
        //body.getFixtureList().getNext().getNext().setUserData(BodyData.FEET);
        body.getFixtureList().getNext().getNext().setFriction(0.8f);

        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.getUserData() == null){
                f.setUserData(BodyData.FOOT);
                f.setFriction(0.1f);
            }
            //System.out.println("Fixtures are: " + f.getUserData());
        }

        body.setGravityScale(1.00f);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    /*
    private void makeBody(PVector position, int width) {
        bodyData = new BodyData(width);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position));
        body = PhysicGameWorld.controller.createBody(bd);

        float bodyWidth = bodyData.bodyWidth;
        float bodyHeight = bodyData.bodyHeight;

        Coordinate bodyAnchor = bodyData.bodyAnchor;
        Vec2[] vertices = new Vec2[4];
        PolygonShape bodyTrapezoidShape = new PolygonShape();
        vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x+bodyWidth/2, bodyAnchor.y));    //right lower
        vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x+bodyWidth/2-bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x-bodyWidth/2+bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x-bodyWidth/2, bodyAnchor.y)); // left lower
        bodyTrapezoidShape.set(vertices, vertices.length);



        float feetWidth = bodyData.feetWidth;
        float feetHeight = bodyData.feetHeight;
        //Vec2 feetAnchor = bodyData.feetAnchor;

        PolygonShape backFeet = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(bodyWidth / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(bodyHeight / 2);
        Vec2 feetAnchor = new Vec2(vertices[3].x+box2dW, bodyAnchor.y-box2dH);
        backFeet.setAsBox(box2dW, box2dH, feetAnchor,0);

        body.createFixture(backFeet, 3.1f);
        body.getFixtureList().setUserData(BodyData.FEET);
        body.getFixtureList().setFriction(0.01f);
        //body.getFixtureList().setUserData(BodyData.FEET);
        //body.createFixture(tailShape, 0.15f);
        body.createFixture(bodyTrapezoidShape, 1.3f);
        body.getFixtureList().setFriction(0.6f);
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.getUserData() == null){
                f.setUserData(BodyData.BODY);
            }
        }
        body.setGravityScale(1.00f);


        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }
     */


    @Override
    public void update(GameRound gameRound){
        super.update();
        behaviourController.update(gameRound);
        if (canBeDeleted){

        }
    }

    @Override
    public void draw(GameCamera gameCamera) {
        float a = body.getAngle();
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        drawAnimation(pos, a, gameCamera);
        if (Program.debug) drawDebugGraphic(pos, a, gameCamera);
    }

    private void drawAnimation(Vec2 pos, float angleInRadians, GameCamera gameCamera){
        tintUpdatingBySelecting();
        behaviourController.draw(enemiesAnimationController, gameCamera, pos, angleInRadians);
    }

    private void drawDebugGraphic(Vec2 pos, float a, GameCamera gameCamera){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.strokeWeight(1);
        Program.objectsFrame.stroke(25,25,155);
        //if (isAlive()) {
            drawDebugBody();
        //}
       // drawDebugFeet();
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
        //behaviourController.draw(gameCamera);
    }



    private void drawDebugBody() {
        //BossBoar 1:510,1640,150,10,-40,1700,-80,1400,680,1700,720,1400,240,240,440,1680,600,1480,40,1480,200,1680,80,80
        int j = 0;
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            //if (f.getUserData().equals(BodyData.BODY)){
                PolygonShape shape = (PolygonShape) f.getShape();
                Program.objectsFrame.beginShape();
                Program.objectsFrame.strokeWeight(2.5f);
                if (f.getUserData() == BodyData.TUMMY) Program.objectsFrame.stroke(255,0,0);
                else Program.objectsFrame.stroke(255,255,0);
                /*
                if (j == 1) Program.objectsFrame.stroke(255,0,0);
                else if (j == 2) Program.objectsFrame.stroke(255,255,0);
                else if (j == 3) Program.objectsFrame.stroke(0,255,0);
                 */
            try {
                for (int i = 0; i < shape.getVertexCount(); i++) {
                    Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(shape.getVertex(i));
                    Program.objectsFrame.vertex(v.x, v.y);
                }
            }catch (Exception e){

            }

                Program.objectsFrame.endShape(PApplet.CLOSE);
                j++;
            //}
        }
    }

    @Override
    protected void updateAngle() {

    }

    @Override
    public boolean attackByDirectContact(Person nearestPerson) {
        return true;
    }

    @Override
    public int getAttackValue() {
        return (int) (10f* boundingWidth /35f);
    }

    public void loadAnimationData(MainGraphicController mainGraphicController){
        System.out.println("Graphic for animation data must be uploaded");
        super.loadAnimationData(mainGraphicController);
        float additionalGraphicScaleX  = 1.5f;
        float additionalGraphicScaleY  = 1.45f;
        SpriteAnimation goAnimation = new SpriteAnimation(BoarGraphicData.imageZoneForGoing.getName(), BoarGraphicData.imageZoneForGoing.leftX, BoarGraphicData.imageZoneForGoing.upperY, BoarGraphicData.imageZoneForGoing.rightX, BoarGraphicData.imageZoneForGoing.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)8,  16);
        enemiesAnimationController.addNewAnimation(goAnimation, EnemiesAnimationController.GO);
        SpriteAnimation runAnimation = new SpriteAnimation(BoarGraphicData.imageZoneForRunning.getName(), BoarGraphicData.imageZoneForRunning.leftX, BoarGraphicData.imageZoneForRunning.upperY, BoarGraphicData.imageZoneForRunning.rightX, BoarGraphicData.imageZoneForRunning.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)6,  16);
        enemiesAnimationController.addNewAnimation(runAnimation, EnemiesAnimationController.RUN);
        SpriteAnimation idleAnimation = new SpriteAnimation(BoarGraphicData.imageZoneForIdle.getName(), BoarGraphicData.imageZoneForIdle.leftX, BoarGraphicData.imageZoneForIdle.upperY, BoarGraphicData.imageZoneForIdle.rightX, BoarGraphicData.imageZoneForIdle.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)8,  16);
        enemiesAnimationController.addNewAnimation(idleAnimation, EnemiesAnimationController.IDLE);
        SpriteAnimation dyingAnimation = new SpriteAnimation(BoarGraphicData.imageZoneForIdle.getName(), BoarGraphicData.imageZoneForDying.leftX, BoarGraphicData.imageZoneForDying.upperY, BoarGraphicData.imageZoneForDying.rightX, BoarGraphicData.imageZoneForDying.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)8,  16);
        enemiesAnimationController.addNewAnimation(dyingAnimation, EnemiesAnimationController.DYING);
        //additionalGraphicScaleY  = 1.0f;
        StaticSprite corpse = new StaticSprite(BoarGraphicData.imageZoneForIdle.getName(), BoarGraphicData.corpse.leftX, BoarGraphicData.corpse.upperY, BoarGraphicData.corpse.rightX, BoarGraphicData.corpse.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY));
        enemiesAnimationController.addNewSprite(corpse, EnemiesAnimationController.CORPSE_SINGLE_SPRITE);
        enemiesAnimationController.loadSprites(mainGraphicController);

        //System.out.println("Bat graphic uploaded from path ! " + BatGraphicData.imageZoneFullDataForFlying.getName());

        System.out.println("Graphic for animation data was uploaded");
    }

    public void dublicateAccelerate(float accelerate) {
        this.actualAccelerate=accelerate;
    }

    public void deleteFeet() {
        int fixturesCount = 0;
        int count = 0;
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) count++;
        System.out.println("Body has " + count + " fixtures");
        ArrayList <Fixture> fixturesListToBeDeleted = new ArrayList<>();
        for (Fixture f=body.getFixtureList(); f!= null; f=f.getNext()){
            String dataString = f.getUserData().toString();
            fixturesCount++;
            if (dataString.contains(BodyData.FOOT)){
                fixturesListToBeDeleted.add(f);
            }
        }
        for (int i = (fixturesListToBeDeleted.size()-1); i >= 0; i--) {
            int contactsNumber = 0;
            for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
                if (f1.equals(fixturesListToBeDeleted.get(i)) || f2.equals(fixturesListToBeDeleted.get(i))) {
                    contactsNumber++;
                    PhysicGameWorld.beginContacts.remove(m);
                }
            }
            for (int m = (PhysicGameWorld.endContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.endContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.endContacts.get(m).getFixtureB();
                if (f1.equals(fixturesListToBeDeleted.get(i)) || f2.equals(fixturesListToBeDeleted.get(i))) {
                    contactsNumber++;
                    PhysicGameWorld.endContacts.remove(m);
                }
            }
            System.out.println("Try to delete head fixture. It was " + contactsNumber + " contacts with it");
            try {
                if (!PhysicGameWorld.controller.world.isLocked()) {
                    body.destroyFixture(fixturesListToBeDeleted.get(i));
                } else System.out.println("World is locked. Can not simplify body");
                System.out.println("Fixture of the head was deleted. It was " + contactsNumber + " contacts with it");
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public void playerCanNotKillBoar() {
        BossBoarController bossBoarController = (BossBoarController) behaviourController;
        bossBoarController.playerCanNotKillEnemy();
    }

    static class BoarGraphicData{
        final static String path = Program.getAbsolutePathToAssetsFolder("Boar spritesheet"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()) ;
        //final static String path = Program.getAbsolutePathToAssetsFolder("Boar"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()) ;
        final static int halfWidth = 32;
        final static int graphicCenterY = 30;
        final static int halfHeight = 17;
        final static ImageZoneFullData imageZoneForIdle = new ImageZoneFullData(path, 36-halfWidth, graphicCenterY-halfHeight, 36+(64*7)+halfWidth, graphicCenterY+halfHeight);
        final static ImageZoneFullData imageZoneForGoing = new ImageZoneFullData(path, 36-halfWidth, graphicCenterY-halfHeight+48, 36+64*7+halfWidth, graphicCenterY+halfHeight+48);
        final static ImageZoneFullData imageZoneForRunning = new ImageZoneFullData(path, 36-halfWidth, graphicCenterY-halfHeight+48*2, 36+64*5+halfWidth, graphicCenterY+halfHeight+48*2);
        final static ImageZoneFullData imageZoneForDying = new ImageZoneFullData(path, 36-halfWidth, graphicCenterY-halfHeight+48*3, 36+64*7+halfWidth, graphicCenterY+halfHeight+48*3);

        final static ImageZoneFullData corpse = new ImageZoneFullData(path, 36+64*6+halfWidth, graphicCenterY-halfHeight+48*3, 36+64*7+halfWidth, graphicCenterY+halfHeight+48*3);

        static float getScaleCoef(int width){
            return ((float)width/(float)((corpse.rightX-corpse.leftX)));
        }

        static int getHeightRelativeToWidth(int width) {
            float coef  =  getScaleCoef(width);
            return (int) (coef*(corpse.lowerY-corpse.upperY));
        }



    }

    class BodyData{
        float bodyWidth;
        float bodyHeight;
        Coordinate bodyAnchor;

        float feetWidth;
        float feetHeight;
        Vec2 rightFeetAnchor;
        Vec2 leftFeetAnchor;
        Vec2 tailAnchor;
        Vec2 stomachAnchor;
        Vec2 worldCenterForBodyAfterDead;

        float stomachWidth;
        final static String FOOT = "Feet";
        final static String BODY = "Body";
        final static String TUMMY = "Tummy";

        BodyData(float width){
            initBodyData(width);
        }

        private void initBodyData(float width) {
            bodyWidth = width;
            bodyHeight = boundingHeight*0.6f;
            bodyAnchor = new Coordinate(0, 0);
            feetWidth = width/6f;
            stomachWidth = width/3f;
            feetHeight = boundingHeight-bodyHeight;
            rightFeetAnchor = new Vec2(bodyWidth/2-feetWidth/2, feetHeight/2);

            leftFeetAnchor = new Vec2(-bodyWidth/2+feetWidth/2, rightFeetAnchor.y);
            /*
            rightFeetAnchor = new Vec2(stomachWidth/2+feetWidth/2, feetHeight/2);
            leftFeetAnchor = new Vec2(-stomachWidth/2-feetWidth/2, rightFeetAnchor.y);*/
            stomachAnchor = new Vec2((0f), rightFeetAnchor.y);

            /*
            rightFeetAnchor = new Vec2(bodyWidth/2-feetWidth/2, feetHeight/2);
            leftFeetAnchor = new Vec2(rightFeetAnchor.x-feetWidth-stomachWidth, rightFeetAnchor.y);
            stomachAnchor = new Vec2((leftFeetAnchor.x+stomachWidth/2+feetWidth/2), rightFeetAnchor.y);
             */


            /*
            backFeetAnchor = new Vec2(bodyWidth/2-feetWidth/2, feetHeight/2);
            frontFeetAnchor = new Vec2(backFeetAnchor.x-feetWidth*2-stomachWidth, backFeetAnchor.y);
            stomachAnchor = new Vec2((backFeetAnchor.x+frontFeetAnchor.x)/2, backFeetAnchor.y);
             */

            /*
            backFeetAnchor = PhysicGameWorld.controller.vectorPixelsToWorld(bodyWidth/2-feetWidth/2, feetHeight*2);
            tailAnchor = PhysicGameWorld.controller.vectorPixelsToWorld(-width*0.35f, -bodyAnchor.y*1.5f);
             */

        }
    }

    @Override
    public boolean canFixtureBeAttacked(Fixture attackedFixture, Bullet bullet){
            if (attackedFixture.getUserData() != null){
                if (attackedFixture.getUserData() == BodyData.TUMMY){
                    if (behaviourController.canBeAttacked()) {
                        final float criticalRightAngle = 330f;
                        final float criticalLeftAngle = 210f;
                        if (bullet.getStartAngle() < criticalRightAngle && bullet.getStartAngle() > criticalLeftAngle) {
                            //System.out.println("This fixture can be attacked by angle: " + bullet.getStartAngle());
                            return true;
                        } else {
                            //System.out.println("Bullet angle: " + bullet.getStartAngle());
                            return false;
                        }
                    }
                }
            }

        /*for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getUserData() != null){
                if (f.getUserData() == BodyData.TUMMY){
                    System.out.println("This fixture can be attacked");
                    return true;
                }
            }
        }*/
        return false;
    }

    @Override
    public void attacked(int value){
        int realAttackValue = behaviourController.correctAttackValue(value);
        System.out.println("Enemy must be attacked to " + realAttackValue + " points");
        super.attacked(realAttackValue);

    }

    @Override
    public boolean canPersonBeAttackedByJump() {
        return false;
    }

    @Override
    public void simplifyBody(){
        int fixturesCount = 0;
        int count = 0;
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) count++;
        System.out.println("Body has " + count + " fixtures");
        ArrayList <Fixture> fixturesList = new ArrayList<>();
        //for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext())
        for (Fixture f=body.getFixtureList(); f!= null; f=f.getNext()){
            String dataString = f.getUserData().toString();
            fixturesCount++;
            if (!dataString.contains(BodyData.BODY)){
                fixturesList.add(f);
            }
            else {
                PolygonShape polygonShape = (PolygonShape)f.getShape();
                System.out.println("Center was at " + polygonShape.m_centroid);
                Vec2[] verticies = polygonShape.getVertices();
                float deltaY = polygonShape.m_centroid.y-bodyData.worldCenterForBodyAfterDead.y;
                for (int i  = 0; i < verticies.length; i++){
                    verticies[i].y-=deltaY;
                }
                //polygonShape.m_centroid.x = bodyData.worldCenterForBodyAfterDead.x;
                //polygonShape.m_centroid.y = bodyData.worldCenterForBodyAfterDead.y;
                //System.out.println("Center is now at " + polygonShape.m_centroid);

                        /*
                        for (int i = 0; i < shape.getVertexCount(); i++) {
                    Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(shape.getVertex(i));
                    Program.objectsFrame.vertex(v.x, v.y);
                }
                         */

            }
            System.out.println("This fixture has data: " + f.getUserData());
        }
        for (int i = (fixturesList.size()-1); i >= 0; i--) {
            int contactsNumber = 0;
            for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
                if (f1.equals(fixturesList.get(i)) || f2.equals(fixturesList.get(i))) {
                    contactsNumber++;
                    PhysicGameWorld.beginContacts.remove(m);
                }
            }
            for (int m = (PhysicGameWorld.endContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.endContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.endContacts.get(m).getFixtureB();
                if (f1.equals(fixturesList.get(i)) || f2.equals(fixturesList.get(i))) {
                    contactsNumber++;
                    PhysicGameWorld.endContacts.remove(m);
                }
            }
            System.out.println("Try to delete head fixture. It was " + contactsNumber + " contacts with it");
            try {
                if (!PhysicGameWorld.controller.world.isLocked()) {
                    body.destroyFixture(fixturesList.get(i));
                } else System.out.println("World is locked. Can not simplify body");
                System.out.println("Fixture of the head was deleted. It was " + contactsNumber + " contacts with it");
            } catch (Exception e) {
                System.out.println("Can not delete head fixture");
                e.printStackTrace();
            }
        }

        System.out.println("There are " + fixturesCount + " fixtures");
    }

    @Override
    public boolean canBeAttackedByKick() {
        return false;
    }

    @Override
    public boolean canBeAttackedThroughtExplosion(Fixture fixture) {
        if (fixture.getUserData() != null){
            if (fixture.getUserData() == BodyData.TUMMY) return true;
        }
        return false;
    }

    @Override
    public float getPixelDistToSplash() {
        return boundingHeight/3f;
    }
    @Override
    public boolean canBeAttackedByFlyingObject() {
        return false;
    }

}
