package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.MoveableSpritesAddingController;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.LizardController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;

public class Lizard extends Enemy implements IDrawable, ISimpleUpdateable {
    public final static String CLASS_NAME = "Lizard";
    private final String objectToDisplayName = CLASS_NAME;
    public final static int NORMAL_WIDTH = 60;
    private int alpha;
    //private final static String path = Program.getAbsolutePathToAssetsFolder("Lizard spritesheet.png") ;

    private int width, height;
    private final String FEET = "Feet";
    private final String BODY = "Body";
    private BodyData bodyData;
    private boolean succesfullySimplyfied = false;

    private final TailFallingController tailFallingController;

    public Lizard(Vec2 position, int life, int width, int attackTime, int holdLuggageTime, int delayBeforeAttackTime, int attackDistance, boolean withFallingOnLoweredPlatform, int attackProbability, int alpha) {
        init(position, life, width, attackTime, holdLuggageTime, delayBeforeAttackTime, attackDistance, withFallingOnLoweredPlatform);
        behaviourController = new LizardController(this, attackTime, holdLuggageTime, delayBeforeAttackTime, attackDistance, withFallingOnLoweredPlatform, attackProbability, LizardAnimationData.getRelativeScale(width));
        findPlayerAtStart();
        maxVelocityAlongX = 3;
        actualAccelerate = 15;
        tailFallingController = new TailFallingController();
        this.alpha = alpha;
    }

    private void init(Vec2 position, int life, int width, int attackTime, int holdLuggageTime, int delayBeforeAttackTime, int attackDistance, boolean withFallingOnLoweredPlatform) {
        this.width = width;
        this.height = (int)((float)width/LizardAnimationData.xToY);

        role = ENEMY;

        boundingWidth = width;
        boundingHeight = height;
        makeBody(new Vec2(position.x, position.y), width);
        maxLife = life;
        setLife(life, life);
        System.out.println("Lizard was created");
        body.setFixedRotation(true);
        findPlayerAtStart();
        //mirrorBody();
    }

    private void makeBody(Vec2 position, float boundingWidth) {
        bodyData = new BodyData(width);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position));
        body = PhysicGameWorld.controller.createBody(bd);



        float bodyWidth = bodyData.bodyWidth;
        float bodyHeight = bodyData.bodyHeight;
        System.out.println("Body data: " + bodyWidth + "x" + bodyHeight);

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
        Vec2 feetAnchor = bodyData.feetAnchor;

        //Coordinate feetAnchor = bodyData.bodyAnchor;

        PolygonShape feetShape = new PolygonShape();
        Vec2[] feetVertices = new Vec2[4];
        feetVertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x+bodyWidth/2, bodyAnchor.y));    //right lower
        feetVertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x+bodyWidth/2-feetHeight/1, bodyAnchor.y+feetHeight)));
        feetVertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x-bodyWidth/2+feetHeight/1, bodyAnchor.y+feetHeight)));
        feetVertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x-bodyWidth/2, bodyAnchor.y)); // left lower
        feetShape.set(feetVertices, feetVertices.length);


        /*
        PolygonShape feetShape = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(feetWidth / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(feetHeight / 2);
        feetShape.setAsBox(box2dW, box2dH, feetAnchor,0);

*/
        /*CircleShape tailShape = new CircleShape();
        Vec2 tailAnchor = bodyData.tailAnchor;
        tailShape.m_p.set(tailAnchor);
        float tailRadius = bodyHeight/2.5f;
        tailShape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(tailRadius);*/

        body.createFixture(feetShape, 0.5f);
        body.getFixtureList().setFriction(0.01f);
        body.getFixtureList().setUserData(FEET);
        //body.createFixture(tailShape, 0.15f);
        body.createFixture(bodyTrapezoidShape, 1.1f);
        body.getFixtureList().setFriction(0.6f);
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            if (f.getUserData() == null){
                f.setUserData(BODY);
            }
        }
        body.setGravityScale(1.00f);


        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);


    }

    private void makeBodyWithTail(Vec2 position, float boundingWidth) {
        bodyData = new BodyData(width);
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(position));
        body = PhysicGameWorld.controller.createBody(bd);

        PolygonShape bodyTrapezoidShape = new PolygonShape();
        float bodyWidth = bodyData.bodyWidth;
        float bodyHeight = bodyData.bodyHeight;
        System.out.println("Body data: " + bodyWidth + "x" + bodyHeight);
        Coordinate bodyAnchor = bodyData.bodyAnchor;
        Vec2[] vertices = new Vec2[4];
        vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x+bodyWidth/2, bodyAnchor.y));    //right lower
        vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x+bodyWidth/2-bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2(bodyAnchor.x-bodyWidth/2+bodyHeight*0.65f, bodyAnchor.y-bodyHeight)));
        vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(bodyAnchor.x-bodyWidth/2, bodyAnchor.y)); // left lower
        bodyTrapezoidShape.set(vertices, vertices.length);

        float feetWidth = bodyData.feetWidth;
        float feetHeight = bodyData.feetHeight;
        Vec2 feetAnchor = bodyData.feetAnchor;

        PolygonShape feetShape = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(feetWidth / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(feetHeight / 2);
        feetShape.setAsBox(box2dW, box2dH, feetAnchor,0);

        CircleShape tailShape = new CircleShape();
        Vec2 tailAnchor = bodyData.tailAnchor;
        tailShape.m_p.set(tailAnchor);
        float tailRadius = bodyHeight/2.5f;
        tailShape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(tailRadius);

        body.createFixture(feetShape, 0.5f);
        body.getFixtureList().setFriction(0.01f);
        body.getFixtureList().setUserData(FEET);
        body.createFixture(tailShape, 0.15f);
        body.createFixture(bodyTrapezoidShape, 1.1f);

        body.setGravityScale(1.00f);


        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);


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

            /*
            if (behaviourController.getStatement() == LizardController.ST_GO){
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, graphicFlip);
            }
            else if (behaviourController.getStatement() == LizardController.ST_AIMING){
                //enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.BODY_BY_AIM, pos, a, graphicFlip);
                enemiesAnimationController.drawSprite(gameCamera, EnemiesAnimationController.BODY_BY_AIM, pos, a, graphicFlip);

                //enemiesAnimationController.drawSprite();
                //enemiesAnimationController.drawSingleSprite();
            }
*/

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
        try {
            drawDebugFeet();
            //drawDebugTail();
            drawDebugBody();
        }
        catch (Exception e){

        }
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
        //behaviourController.draw(gameCamera);
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
        return false;
    }

    @Override
    public int getAttackValue() {
        return (int)(12f*(float)getPersonWidth()/70f);
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
        System.out.println("!!! Data string for Lizzard is not created !!!");
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

        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= (int)behaviourController.getBehaviourType();
        System.out.println("Data for lizard " + data);
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
        if (canBeDeleted){
            if (!succesfullySimplyfied){
                simplifyBody(gameRound);
            }

        }
    }

    @Override
    public void kill() {
        //super.kill();
        behaviourController.kill();
        simplifyBody(null);
        canBeDeleted = true;
    }


    protected void simplifyBody(GameRound gameRound) {
        int fixCound = 0;
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()){
            fixCound++;
        }
        if (fixCound == 1){
            succesfullySimplyfied = true;
            System.out.println("Body have: " + fixCound + " fixtures");
            if (gameRound != null){
                tailFallingController.addFallingTail(gameRound);
            }
            super.kill();

        }
        else {
            try {
                System.out.println("Body have: " + fixCound + " fixtures");
                System.out.println("Try to simplify body");
                body.setActive(false);
                for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
                    if (f.getUserData().equals(FEET)) {
                        PhysicGameWorld.removeAllContactsWithFixture(f);
                        //f.destroy();
                        body.destroyFixture(f);
                        System.out.println("Feet were deleted");
                        break;
                    }
                }
                System.out.println("Successfully removed fixtures!");
                body.setActive(true);

                int count = 0;
                for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
                    count++;
                }
                tailFallingController.saveData(this);
                super.kill();
                System.out.println("We have: " + count + " fixtures");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadAnimationData(MainGraphicController mainGraphicController){
        super.loadAnimationData(mainGraphicController);

        int [] colorsToBeSaved = new int[2];
        colorsToBeSaved[0] = Program.engine.color(248,120,248);
        colorsToBeSaved[1] = Program.engine.color(216,0,204);
        mainGraphicController.getTilesetUnderPath(LizardAnimationData.imageZoneFullDataForGoing.getName()).setAlphaForColors(alpha, colorsToBeSaved);
        //mainGraphicController.getTilesetUnderPath(LizardAnimationData.imageZoneFullDataForGoing.getName()).setAlpha(alpha);

        float additionalGraphicScaleX  = 1.0f;
        float additionalGraphicScaleY  = 1.0f;
        SpriteAnimation movementAnimation = new SpriteAnimation(LizardAnimationData.imageZoneFullDataForGoing.getName(), LizardAnimationData.imageZoneFullDataForGoing.leftX, LizardAnimationData.imageZoneFullDataForGoing.upperY, LizardAnimationData.imageZoneFullDataForGoing.rightX, LizardAnimationData.imageZoneFullDataForGoing.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)9, (byte)1,  8);
        additionalGraphicScaleY  = 1.0f;
        SpriteAnimation readyToAttackAnimation = new SpriteAnimation(LizardAnimationData.imageZoneFullDataForAttackStarting.getName(), LizardAnimationData.imageZoneFullDataForAttackStarting.leftX, LizardAnimationData.imageZoneFullDataForAttackStarting.upperY, LizardAnimationData.imageZoneFullDataForAttackStarting.rightX, LizardAnimationData.imageZoneFullDataForAttackStarting.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)2, (byte)1,  8);
        enemiesAnimationController.addNewAnimation(movementAnimation, EnemiesAnimationController.GO);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(0,0));
        enemiesAnimationController.addNewAnimation(readyToAttackAnimation, EnemiesAnimationController.READY_TO_ATTACK);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.READY_TO_ATTACK).setSpritesShifting(new Vec2(0,0));
        //public StaticSprite(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height) {

        StaticSprite bodyByAimSprite = new StaticSprite(LizardAnimationData.path, LizardAnimationData.bodyGraphic.leftX, LizardAnimationData.bodyGraphic.upperY, LizardAnimationData.bodyGraphic.rightX, LizardAnimationData.bodyGraphic.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY));
        enemiesAnimationController.addNewSprite(bodyByAimSprite, EnemiesAnimationController.BODY_BY_AIM);

        StaticSprite corpse = new StaticSprite(LizardAnimationData.path, LizardAnimationData.corpse.leftX, LizardAnimationData.corpse.upperY, LizardAnimationData.corpse.rightX, LizardAnimationData.corpse.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY));
        enemiesAnimationController.addNewSprite(corpse, EnemiesAnimationController.CORPSE_SINGLE_SPRITE);


        ImageZoneSimpleData imageZoneSimpleDataForHead = LizardAnimationData.eyesOpened;
        float relativeScale = LizardAnimationData.getRelativeScale(width);
        int headWidth = (int) ((imageZoneSimpleDataForHead.rightX-imageZoneSimpleDataForHead.leftX)*relativeScale);
        int headHeight = (int) ((imageZoneSimpleDataForHead.lowerY-imageZoneSimpleDataForHead.upperY)*relativeScale);
        System.out.println("Rel scale: " + relativeScale);
        StaticSprite headWithOpenedEyesByAim = new StaticSprite(LizardAnimationData.path, LizardAnimationData.eyesOpened.leftX, LizardAnimationData.eyesOpened.upperY, LizardAnimationData.eyesOpened.rightX, LizardAnimationData.eyesOpened.lowerY, headWidth, headHeight);
        enemiesAnimationController.addNewSprite(headWithOpenedEyesByAim, EnemiesAnimationController.HEAD_BY_AIM_EYES_OPENED);

        StaticSprite headWithClosedEyesByAim = new StaticSprite(LizardAnimationData.path, LizardAnimationData.eyesClosed.leftX, LizardAnimationData.eyesClosed.upperY, LizardAnimationData.eyesClosed.rightX, LizardAnimationData.eyesClosed.lowerY, headWidth, headHeight);
        enemiesAnimationController.addNewSprite(headWithClosedEyesByAim, EnemiesAnimationController.HEAD_BY_AIM_EYES_CLOSED);

        StaticSprite headWithOpenedMouth = new StaticSprite(LizardAnimationData.path, LizardAnimationData.mouthOpened.leftX, LizardAnimationData.mouthOpened.upperY, LizardAnimationData.mouthOpened.rightX, LizardAnimationData.mouthOpened.lowerY, headWidth, headHeight);
        enemiesAnimationController.addNewSprite(headWithOpenedMouth, EnemiesAnimationController.HEAD_BY_AIM_MOUTH_OPENED);

        int tailWidth = LizardAnimationData.getTailWidthRelativeToWidth(width);
        int tailHeight = LizardAnimationData.getTailHeightRelativeToWidth(width);

        StaticSprite tail = new StaticSprite(LizardAnimationData.path, LizardAnimationData.tail.leftX, LizardAnimationData.tail.upperY, LizardAnimationData.tail.rightX, LizardAnimationData.tail.lowerY, tailWidth, tailHeight);
        enemiesAnimationController.addNewSprite(tail, EnemiesAnimationController.TAIL);

        int luggageEndWidth = LizardAnimationData.getLuggageEndWidthRelativeToWidth(width);
        int luggageEndHeight = LizardAnimationData.getLuggageEndHeightRelativeToWidth(width);
        StaticSprite luggageEnd = new StaticSprite(LizardAnimationData.path, LizardAnimationData.luggageEnd.leftX, LizardAnimationData.luggageEnd.upperY, LizardAnimationData.luggageEnd.rightX, LizardAnimationData.luggageEnd.lowerY, luggageEndWidth, luggageEndHeight);
        enemiesAnimationController.addNewSprite(luggageEnd, EnemiesAnimationController.LUGGAGE_END);

        int luggageHeight = LizardAnimationData.getLuggageHeightRelativeToWidth(width);
        StaticSprite luggage = new StaticSprite(LizardAnimationData.path, LizardAnimationData.luggage.leftX, LizardAnimationData.luggage.upperY, LizardAnimationData.luggage.rightX, LizardAnimationData.luggage.lowerY, luggageEndWidth, luggageHeight);
        enemiesAnimationController.addNewSprite(luggage, EnemiesAnimationController.LUGGAGE);
            System.out.println("Luggage data: " +  luggageHeight);
        enemiesAnimationController.loadSprites(mainGraphicController);
        //enemiesAnimationController.getSprite(EnemiesAnimationController.LUGGAGE_END).getTileset().setAlpha(120);
        LizardController lizardController =  (LizardController) behaviourController;
        lizardController.setLuggageHeight(luggageHeight);
    }

    private static class LizardAnimationData {
        //

        final static String path = Program.getAbsolutePathToAssetsFolder("Lizard spritesheet.png") ;
        private static int firstCenterY  = 22;
        private static int effectiveHeight  = 38;
        private static int imagesStep  = 42;
        private static int startY = firstCenterY-effectiveHeight/2;
        private static int endY = firstCenterY-effectiveHeight/2+9*imagesStep;
        final static ImageZoneFullData imageZoneFullDataForGoing = new ImageZoneFullData(path, 0, startY, 64, endY);
        final static ImageZoneFullData imageZoneFullDataForAttackStarting = new ImageZoneFullData(path, 65, 26-20, 130, 26+20+44);


        final static ImageZoneSimpleData bodyGraphic = new ImageZoneSimpleData(65,91, 130,131);

        final static ImageZoneFullData corpse = new ImageZoneFullData(path,65,endY-imagesStep+2, 130,endY+2);

        final static ImageZoneFullData luggage = new ImageZoneFullData(path,96-8,381, 125,394);
        final static ImageZoneFullData luggageEnd = new ImageZoneFullData(path,94,396, 101,410);
        final static ImageZoneSimpleData eyesOpened = new ImageZoneSimpleData(95,137, 126,159);
        final static ImageZoneSimpleData eyesClosed = new ImageZoneSimpleData(95,137+42, 126,159+42);

        final static ImageZoneSimpleData mouthOpened = new ImageZoneSimpleData(95,137+84, 126,159+84);

        final static ImageZoneSimpleData tail = new ImageZoneSimpleData(64,320, 82,339);
        final static float xToY = (float)(bodyGraphic.rightX- bodyGraphic.leftX)/(float)(bodyGraphic.lowerY- bodyGraphic.upperY);

        static float getRelativeScale(int width){
            return  (float)width/(float)(LizardAnimationData.imageZoneFullDataForGoing.rightX-LizardAnimationData.imageZoneFullDataForGoing.leftX);
        }

        public static int getLuggageHeightRelativeToWidth(int width) {
            float coef  =  getScaleCoef(width);
            return (int) (coef*(luggage.lowerY-luggage.upperY));
        }

        private static float getScaleCoef(int width){
            return ((float)width/(float)((bodyGraphic.rightX-bodyGraphic.leftX)));
        }

        public static int getLuggageEndWidthRelativeToWidth(int width) {
            float coef  =  getScaleCoef(width);
            return (int) (coef*(luggageEnd.lowerY-luggageEnd.upperY));
        }

        public static int getLuggageEndHeightRelativeToWidth(int width) {
            float coef  = getScaleCoef(width);
            return (int) (coef*(luggageEnd.lowerY-luggageEnd.upperY));
        }

        public static int getTailWidthRelativeToWidth(int width) {
            float coef  = getScaleCoef(width);
            return (int) (coef*(tail.rightX-tail.leftX));
        }
        public static int getTailHeightRelativeToWidth(int width) {
            float coef  = getScaleCoef(width);
            return (int) (coef*(tail.lowerY-tail.upperY));
        }

    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    public static Lizard createPersonFromData(int[] values){
        Vec2 position = new Vec2(values[0], values[1]);
        int life = values[2];
        int width = values[3];
        int attackTime = values[4];
        int holdLuggageTime = values[5];
        int delayBeforeAttackTime = values[6];
        int attackDistance = values[7];
        int withFallingInt = values[8];
        boolean withFalling = true;
        if (withFallingInt == 0) withFalling = false;
        int attackPorvability = values[9];
        int alpha = values[10];
        Lizard lizard = new Lizard(position, life, width, attackTime, holdLuggageTime, delayBeforeAttackTime, attackDistance, withFalling, attackPorvability, alpha);
        return lizard;
    }

    class BodyData{


        float bodyWidth;
        float bodyHeight;
        Coordinate bodyAnchor;

        float feetWidth;
        float feetHeight;
        Vec2 feetAnchor;

        Vec2 tailAnchor;

        BodyData(float width){
            initBodyData(width);
        }

        private void initBodyData(float width) {
            bodyWidth = width*0.72f;
            bodyHeight = height/2;
            bodyAnchor = new Coordinate(0, height/8);

            feetWidth = width/2;
            feetHeight = (float)height*0.2f;
            feetAnchor = PhysicGameWorld.controller.vectorPixelsToWorld(width/9f, feetHeight);

            //tailAnchor = PhysicGameWorld.controller.vectorPixelsToWorld(-width*0.35f, -bodyAnchor.y);
            //tailAnchor = PhysicGameWorld.controller.coordPixelsToWorld(-width*0.35f, -bodyAnchor.y);
            tailAnchor = PhysicGameWorld.controller.vectorPixelsToWorld(-width*0.35f, -bodyAnchor.y*1.5f);
        }
    }

    public void mirrorBody() {
        /*
        if (moovingDirectionIsChanging) {
            if (body.getLinearVelocity().x > 0.1f) {
                CircleShape tail = (CircleShape) getTailFixture().getShape();
                tail.m_p.x = Math.abs(tail.m_p.x);
                sightDirection = TO_RIGHT;
            } else if (body.getLinearVelocity().x < (-0.1f)) {
                CircleShape head = (CircleShape) getTailFixture().getShape();
                head.m_p.x = -1 * Math.abs(head.m_p.x);
                sightDirection = TO_LEFT;
            }
        }*/
    }

    private Fixture getTailFixture() {
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getType() == ShapeType.CIRCLE){
                return f;
            }
        }
        //System.out.println("Tail is not founded!");
        return null;
    }

    private void drawDebugBody() {
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getUserData().equals(BODY)){
                PolygonShape shape = (PolygonShape) f.getShape();
                Program.objectsFrame.beginShape();
                Program.objectsFrame.strokeWeight(2.5f);
                for (int i = 0; i < shape.getVertexCount(); i++) {
                    Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(shape.getVertex(i));
                    Program.objectsFrame.vertex(v.x, v.y);
                }
                //System.out.println("Body is drawn");
                Program.objectsFrame.endShape(PApplet.CLOSE);
            }
        }
    }

    private void drawDebugBodyOld() {
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getType() != ShapeType.CIRCLE){
                if (f.getUserData() == null){
                    PolygonShape shape = (PolygonShape) f.getShape();
                    Program.objectsFrame.beginShape();
                    Program.objectsFrame.strokeWeight(2.5f);
                    for (int i = 0; i < shape.getVertexCount(); i++) {
                        Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(shape.getVertex(i));
                        Program.objectsFrame.vertex(v.x, v.y);
                    }
                    System.out.println("Body is drawn");
                    Program.objectsFrame.endShape(PApplet.CLOSE);
                }
            }
        }
    }


    private void drawDebugTail(){
        //for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
           // if (f.getType() == ShapeType.CIRCLE){
                CircleShape shape = (CircleShape) getTailFixture().getShape();
                Vec2 offset = shape.m_p;
                Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(offset).x, PhysicGameWorld.controller.vectorWorldToPixels(offset).y, PhysicGameWorld.controller.scalarWorldToPixels(shape.m_radius*2f), PhysicGameWorld.controller.scalarWorldToPixels(shape.m_radius*2f));

            //}
       // }
    }
    private void drawDebugFeet(){
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getType() != ShapeType.CIRCLE){
                if (f.getUserData() != null){
                    if (f.getUserData() == FEET){
                        PolygonShape shape = (PolygonShape) f.getShape();
                        Program.objectsFrame.beginShape();
                        Program.objectsFrame.strokeWeight(2.5f);
                        for (int i = 0; i < shape.getVertexCount(); i++) {
                            Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(shape.getVertex(i));
                            Program.objectsFrame.vertex(v.x, v.y);
                        }
                        Program.objectsFrame.endShape(PApplet.CLOSE);
                    }
                }
            }
        }
    }

    class TailFallingController{
        float directionCoef = 1f; //or -1
        private Vec2 startPos;

        void saveData(Lizard lizard){
            //PVector bodyCenter = lizard.getPixelPosition();
            LizardController lizardController = (LizardController) lizard.behaviourController;
            startPos = lizardController.getTailPos(bodyData.tailAnchor);
            //startPos = new Vec2(lizard.getPixelPosition().x, lizard.getPixelPosition().y);
            //if (lizard.orientation == TO_LEFT) directionCoef = -1f;
            directionCoef = lizardController.getDirectionCoef();
        }

        void addFallingTail(GameRound gameRound){
            IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(enemiesAnimationController.getSprite(EnemiesAnimationController.TAIL), startPos, 0f, 35*directionCoef, -135, 0, 245, directionCoef*350, 3000, MoveableSpritesAddingController.LIZARD_TAIL);
            movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(enemiesAnimationController.getSprite(PersonAnimationController.TAIL).getPath()));
            gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);


        }

    }

}
