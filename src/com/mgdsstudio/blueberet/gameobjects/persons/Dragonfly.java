package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.DragonflyController;
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
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PConstants;

public class Dragonfly extends Enemy implements IDrawable, ISimpleUpdateable {
    public final static String CLASS_NAME = "Dragonfly";
    private final String objectToDisplayName = CLASS_NAME;
    private static final int additionalShiftingAlongX = 40;

    public Dragonfly(Vec2 startPos, int behaviourModel, int life, int width, int timeToFlyOnPlace, Flag patrolZone) {
        init(startPos, life, width);
        behaviourController = new DragonflyController(this, behaviourModel, timeToFlyOnPlace, patrolZone);
        findPlayerAtStart();
    }

    private void init(Vec2 position, int life, int width) {
        final float sidesRelationship = 3f;
        this.boundingWidth = width;
        this.boundingHeight = (float)width/sidesRelationship;
        role = ENEMY;
        makeRectBody(position, boundingWidth, boundingHeight);
        maxLife = (int)life;
        setLife((int)life, (int)life);
        if (Program.debug) System.out.println("Dragonfly was uploaded");
        body.setFixedRotation(true);
        findPlayerAtStart();
    }

    private void makeRectBody(Vec2 position, float boundingWidth, float boundingHeight) {
        PolygonShape bodyRect = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(boundingWidth / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(boundingHeight / 2);
        bodyRect.setAsBox(box2dW, box2dH);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position));
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(bodyRect, 0.7f);
        body.getFixtureList().setFriction(5.01f);
        body.setGravityScale(0.01f);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    @Override
    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        if (isVisibleOnScreen(gameCamera, pos)) {
            float a = body.getAngle();
            if (Program.WITH_GRAPHIC) {
                if (!dead) {
                    drawAnimation(pos, a, gameCamera);
                } else {
                    enemiesAnimationController.drawDeadSprite(gameCamera, pos, a, false);
                }
            }
            if (Program.debug) {
                drawDebugGraphic(pos, a, gameCamera);
            }
        }
    }

    private void drawAnimation(Vec2 pos, float a, GameCamera gameCamera){
        if (!behaviourController.isStartedToDie()) {
            tintUpdatingBySelecting();
            if (body.getLinearVelocity().x < 0) enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, true);
            else enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, false);
        }
        else{
            boolean sideForDying = behaviourController.getLastOrientation();
            enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.DYING, pos, a, sideForDying);
        }
    }

    private void drawDebugGraphic(Vec2 pos, float a, GameCamera gameCamera){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.strokeWeight(1);
        Program.objectsFrame.stroke(25,25,155);
        Program.objectsFrame.rectMode(PConstants.CENTER);
        Program.objectsFrame.rect(0, 0, boundingWidth, boundingHeight);
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
        behaviourController.draw(gameCamera);
    }

    @Override
    public boolean isActualAnimationEnds(){
        int actualAnimation = enemiesAnimationController.getLastDrawnAnimationType();
        if (enemiesAnimationController.getSpriteAnimation(actualAnimation).isActualSpriteLast()){
            System.out.println("This is the last sprite in actual animation");
            return true;
        }
        return false;
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
        return true;
    }

    @Override
    public int getAttackValue() {
        return (int)(20f*(float)getPersonWidth()/70f);
    }



    @Override
    public void attacked(int damageValue) {
        super.attacked(damageValue);
        behaviourController.attacked();
    }

    public void setGraphicDimensionFromEditor(int newDiameter){
        enemiesAnimationController.setNewDimention((int)newDiameter);
    }

    public void setHalfTint() {
        enemiesAnimationController.setTint(Program.engine.color(255,133));
    }

    @Override
    public String getStringData(){
        System.out.println("!!! Data string for dragonfly is not created !!!");
        String data = new String();
        data = data+CLASS_NAME;
        data+= " 1";
        data+= LoadingMaster.MAIN_DATA_START_CHAR;
        data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).x;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= (int)PhysicGameWorld.controller.getBodyPixelCoord(body).y;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= life;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        //data+= ;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= (int)behaviourController.getBehaviourType();
        System.out.println("Data for dragonfly " + data);
        return data;
    }

    @Override
    public void setContactWithPlayer(Person player) {
        behaviourController.setContactWithPlayer(player);
    }

    @Override
    public void update(GameRound gameRound){
        super.update();
        behaviourController.update(gameRound);
    }

    @Override
    public void kill() {
        //super.kill();
        if (!behaviourController.isStartedToDie()) {
            System.out.println("Dragonfly started killed");
            behaviourController.kill();
        }
        else {
            /*System.out.println("Dragonfly body made simple");
            super.kill();
            DragonflyController dragonflyController = (DragonflyController) behaviourController;
            dragonflyController.dyingAnimationEnds();
            canBeDeleted = true;*/
        }
    }

    public void killAfterStartedToDie(){
        super.kill();
        DragonflyController dragonflyController = (DragonflyController) behaviourController;
        dragonflyController.dyingAnimationEnds();
        canBeDeleted = true;
    }

    public void loadAnimationData(MainGraphicController mainGraphicController){
        super.loadAnimationData(mainGraphicController);
        float additionalGraphicScaleX  = 1.0f;
        float additionalGraphicScaleY  = 1.3f;
        SpriteAnimation movementAnimation = new SpriteAnimation(DragonflyAnimationData.imageZoneFullDataForGoing.getName(), (int) DragonflyAnimationData.imageZoneFullDataForGoing.leftX, (int) DragonflyAnimationData.imageZoneFullDataForGoing.upperY, (int)DragonflyAnimationData.imageZoneFullDataForGoing.rightX, (int) DragonflyAnimationData.imageZoneFullDataForGoing.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)4, (byte)1,  34);
        movementAnimation.setLastSprite(3);
        additionalGraphicScaleY  = 1.6f;
        SpriteAnimation dyingAnimation = new SpriteAnimation(DragonflyAnimationData.imageZoneFullDataForDying.getName(), (int) DragonflyAnimationData.imageZoneFullDataForDying.leftX, (int) DragonflyAnimationData.imageZoneFullDataForDying.upperY, (int)DragonflyAnimationData.imageZoneFullDataForDying.rightX, (int) DragonflyAnimationData.imageZoneFullDataForDying.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)5, (byte)1,  8);
        enemiesAnimationController.addNewAnimation(movementAnimation, EnemiesAnimationController.GO);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(0,0));
        enemiesAnimationController.addNewAnimation(dyingAnimation, EnemiesAnimationController.DYING);
        enemiesAnimationController.setDeadSprite(EnemiesAnimationController.DYING, 4);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.DYING).setSpritesShifting(new Vec2(0,0));
        enemiesAnimationController.loadSprites(mainGraphicController);
    }

    private static class DragonflyAnimationData {
        final static String path = Program.getAbsolutePathToAssetsFolder("Dragonfly"+ TextureDecodeManager.getExtensionForSpriteGraphicFile()) ;
        //final static String path = Program.getAbsolutePathToAssetsFolder("Dragonfly.gif") ;
        final static ImageZoneFullData imageZoneFullDataForGoing = new ImageZoneFullData(path, 0, 130-62*2, 79, 130+62*2);
        final static ImageZoneFullData imageZoneFullDataForDying = new ImageZoneFullData(path, 79, 0, 162, 374);
    //final static ImageZoneFullData imageZoneFullDataForDying = new ImageZoneFullData(path, 79, 0, 162, 374);
    }

    public static Dragonfly createPersonFromData(int[] values){
        Vec2 position = new Vec2(values[0], values[1]);
        int behaviourMode = values[2];
        int life = values[3];
        int width = values[4];
        int timeToFlyOnPlace = values[5];
        Flag patrolArea = Flag.createFlagByLastValues(values);
        Dragonfly dragonfly = new Dragonfly(position, behaviourMode, life, width, timeToFlyOnPlace, patrolArea);
        System.out.println("Data for dragonfly: " + position + "; behaviour = " + behaviourMode + "; Life: " + life + "; Width: " + width);
        return dragonfly;
    }

    @Override
    public boolean canPersonAddSplash() {
        return false;
    }
}
