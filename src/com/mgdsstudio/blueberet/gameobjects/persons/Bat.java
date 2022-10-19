package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.BatController;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.DragonflyController;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.LizardController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import com.mgdsstudio.texturepacker.TexturePacker;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PConstants;
import processing.core.PVector;

public class Bat extends Enemy{
    public final static String CLASS_NAME = "Bat";

    public static Bat createPersonFromData(int[] values){
        PVector position = new PVector(values[0], values[1]);
        int behaviour = values[2];
        int width = values[3];
        int life = values[4];
        int distanceToReact = values[5];
        Flag area = null;
        int awakeningByAiming = values[6];
        if (values.length>7){
            PVector center = new PVector(values[7], values[8]);
            int zoneWidth = values[9];
            int zoneHeight = values[10];
            area = new Flag(center, zoneWidth, zoneHeight, Flag.PATROL_AREA);
        }
        Bat bat = new Bat(position, behaviour, width, life, distanceToReact, awakeningByAiming, area);
        return bat;
    }

    public Bat(PVector pos, int behaviour, int width, int life, int distanceToReactOnPlayer, int awakeningByAiming, Flag patrolArea){
        init(pos, behaviour, width, life, distanceToReactOnPlayer, awakeningByAiming, patrolArea);
        findPlayerAtStart();
        maxVelocityAlongX = 15;
        actualAccelerate = 15;
    }

    private void init(PVector pos, int behaviour, int width, int life, int distanceToReactOnPlayer, int awakeningByAiming, Flag patrolArea) {
        this.boundingWidth = width;
        this.boundingHeight = BatGraphicData.getHeightRelativeToWidth(width);
        role = ENEMY;
        maxLife = life;
        setLife(life, life);
        makeBody(pos, behaviour);
        body.setFixedRotation(true);
        behaviourController = new BatController(this, behaviour, distanceToReactOnPlayer, awakeningByAiming, patrolArea, 1);
    }



    private void makeBody(PVector pos, int behaviour) {
        if (behaviour == BatController.HANG || behaviour == BatController.HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
            makeRectBody(pos);
        }
        else makeCircleBody(pos);
        body.setUserData(CLASS_NAME);

    }

    private void makeCircleBody(PVector pos) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(pos));
        body = PhysicGameWorld.controller.createBody(bd);

        CircleShape head = new CircleShape();
        float radiusCoef = 0.8f;
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld((boundingWidth/2f)*radiusCoef);
        Vec2 offset = new Vec2(0, 0);
        offset = PhysicGameWorld.controller.vectorPixelsToWorld(offset);
        head.m_p.set(offset.x, offset.y);

        body.createFixture(head, 1f);
        body.getFixtureList().setFriction(0.1f);
        body.setGravityScale(0.001f);

        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    private void makeRectBody(PVector pos) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(pos));
        body = PhysicGameWorld.controller.createBody(bd);

        PolygonShape bodyRect = new PolygonShape();

        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld((boundingWidth/2.26f)/2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((boundingHeight) / 2f);
        bodyRect.setAsBox(box2dW, box2dH);

        body.createFixture(bodyRect, 1f);
        body.getFixtureList().setFriction(1f);
        body.setGravityScale(0.0f);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    @Override
    public void update(GameRound gameRound){
        super.update();
        behaviourController.update(gameRound);
        if (canBeDeleted){

        }
    }


    @Override
    protected void updateAngle() {

    }

    @Override
    public int getPersonWidth() {
        return (int) boundingWidth;
    }

    @Override
    public boolean attackByDirectContact(Person nearestPerson) {
        if (behaviourController.getStatement() == BatController.HANG || behaviourController.getStatement() == BatController.HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE) {
            return false;
        }
        else return true;
    }

    @Override
    public int getAttackValue() {
        return (int) (10f* boundingWidth /35f);
    }

    @Override
    public void draw(GameCamera gameCamera) {
        float a = body.getAngle();
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        drawAnimation(pos, a, gameCamera);
        if (Program.debug) drawDebugGraphic(pos, a, gameCamera);
    }

    private void drawDebugGraphic(Vec2 pos, float a, GameCamera gameCamera){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.strokeWeight(1);
        Program.objectsFrame.stroke(25,25,155);

        if (behaviourController.getStatement() == BatController.HANG || behaviourController.getStatement() == BatController.HANG_AND_ATTACK_WHEN_PLAYER_APPEARS_IN_ZONE){
            Program.objectsFrame.rectMode(PConstants.CENTER);
            Program.objectsFrame.rect(0,0,boundingWidth/2.26f, boundingHeight);
        }
        else {
            try {
                CircleShape circleShape = (CircleShape) body.getFixtureList().getShape();
                float radius = PhysicGameWorld.controller.scalarWorldToPixels(circleShape.m_radius);
                Program.objectsFrame.ellipse(0, 0, radius * 2, radius * 2);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
        //behaviourController.draw(gameCamera);
    }

    private void drawAnimation(Vec2 pos, float angleInRadians, GameCamera gameCamera){
        tintUpdatingBySelecting();
        behaviourController.draw(enemiesAnimationController, gameCamera, pos, angleInRadians);
    }

    public void loadAnimationData(MainGraphicController mainGraphicController){
        super.loadAnimationData(mainGraphicController);
        float additionalGraphicScaleX  = 1.15f;
        float additionalGraphicScaleY  = 1.15f;
        SpriteAnimation flyAnimation = new SpriteAnimation(BatGraphicData.imageZoneFullDataForFlying.getName(), BatGraphicData.imageZoneFullDataForFlying.leftX, BatGraphicData.imageZoneFullDataForFlying.upperY, BatGraphicData.imageZoneFullDataForFlying.rightX, BatGraphicData.imageZoneFullDataForFlying.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)4,  16);
        enemiesAnimationController.addNewAnimation(flyAnimation, EnemiesAnimationController.FLY);
        //additionalGraphicScaleY  = 1.0f;
        StaticSprite corpse = new StaticSprite(BatGraphicData.path, BatGraphicData.corpse.leftX, BatGraphicData.corpse.upperY, BatGraphicData.corpse.rightX, BatGraphicData.corpse.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY));
        enemiesAnimationController.addNewSprite(corpse, EnemiesAnimationController.CORPSE_SINGLE_SPRITE);
        additionalGraphicScaleX  = 1.05f;
        additionalGraphicScaleY  = 1.05f;
        StaticSprite hangSprite = new StaticSprite(BatGraphicData.path, BatGraphicData.hang.leftX, BatGraphicData.hang.upperY, BatGraphicData.hang.rightX, BatGraphicData.hang.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY));
        enemiesAnimationController.addNewSprite(hangSprite, EnemiesAnimationController.HANG);
        enemiesAnimationController.loadSprites(mainGraphicController);
        //System.out.println("Bat graphic uploaded from path ! " + BatGraphicData.imageZoneFullDataForFlying.getName());
    }

    static class BatGraphicData{
        final static String path = Program.getAbsolutePathToAssetsFolder("Bat"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()) ;
        //final static String path = Program.getAbsolutePathToAssetsFolder("Bat.png") ;
        final static ImageZoneFullData imageZoneFullDataForFlying = new ImageZoneFullData(path, 3, 0, 140, 43);
        final static ImageZoneFullData corpse = new ImageZoneFullData(path,4,64-20, 35+4,64+20);
        final static ImageZoneFullData hang = new ImageZoneFullData(path,35,40, 76,86);

        static float getScaleCoef(int width){
            return ((float)width/(float)((corpse.rightX-corpse.leftX)));
        }

        static int getHeightRelativeToWidth(int width) {
            float coef  =  getScaleCoef(width);
            return (int) (coef*(corpse.lowerY-corpse.upperY));
        }



    }

    @Override
    public void simplifyBody(){
        BatController batController = (BatController) behaviourController;
        PhysicGameWorld.removeAllContactsWithFixture(body.getFixtureList());
        PhysicGameWorld.controller.world.destroyBody(body);
        makeBody(getPixelPosition(), batController.getStatement());
        batController.createSpring();
    }

    @Override
    public void kill() {
        //super.kill();
        BatController batController = (BatController) behaviourController;
        if (batController.isSleeping()) {
            System.out.println("Killed as sleeped");
            batController.kill();
            super.kill();
            //behaviourController.kill();
        }
        else {
            //System.out.println("Dragonfly body made simple");
            batController.kill();
            super.kill();
            //DragonflyController dragonflyController = (DragonflyController) behaviourController;
            //dragonflyController.dyingAnimationEnds();
            //canBeDeleted = true;
        }
    }
}
