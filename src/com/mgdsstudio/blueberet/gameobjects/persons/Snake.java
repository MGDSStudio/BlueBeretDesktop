package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.SnakeController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
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
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class Snake extends Enemy {
    //private EnemyController behaviourController;
    public final static String CLASS_NAME = "Snake";
    private final String objectToDisplayName = "Snake";
    public final static int NORMAL_WIDTH = 40;

    public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
    private int diameter = NORMAL_WIDTH;
    private final float NORMAL_ACCELERATE = 5f;
    public static int NORMAL_LIFE = 200;
    static int normalMovementImpulseX = 5;
    static float maxNormalSpeed = 0.5f;
    private float headRadius, bodyRadius;
    private final float tailDensity = 1.4f, headDensity = 0.2f;
    private int AI_type;
    public final static int GO_AND_ATTACK = 0;
    public final static int GO_ATTACK_FOLLOW = 1;
    private boolean headMustBeDeleted, headWasDeleted;
    private final boolean WITHOUT_BODY_MIRRORING = true;

    //private static TexturePacker TextureDecodeManager;
    //final static String path = Program.getAbsolutePathToAssetsFolder("Snake spritesheet.gif") ;
    final static String path = Program.getAbsolutePathToAssetsFolder("Snake spritesheet" + TextureDecodeManager.getExtensionForSpriteGraphicFile()) ;

    private final static ImageZoneFullData imageZoneFullDataForGoing = new ImageZoneFullData(path, (int)90, 4, (int)180, (int)128+4);
    private final static ImageZoneFullData imageZoneFullDataForDying = new ImageZoneFullData(path, (int)0,4, (int)90, (int)128+4);
    private final static ImageZoneFullData imageZoneFullDataForAwaking = new ImageZoneFullData(path, (int)0, (int)(157+4), (int)135, (int)157+32+4);
    private final static ImageZoneFullData attackData = new ImageZoneFullData(path, (int)0, (int)(100), (int)135, (int)(100+64));


    public Snake(PVector position, byte behaviourModel, int life, int diameter) {
        init(position, life, diameter, behaviourModel);
        behaviourController = new SnakeController(this, behaviourModel);
        findPlayerAtStart();
        maxVelocityAlongX = 5;
    }

    private void init(PVector position, int life, int diameter, byte behaviourModel) {
        this.diameter = (int)diameter;
        role = ENEMY;
        boundingWidth = diameter;
        boundingHeight = diameter;
        bodyRadius = (2*boundingWidth/3)/2;
        headRadius = 0.92f*(boundingWidth-(bodyRadius*2))/2f;
        //System.out.println("Snake width: " + boundingWidth + "; Body rad: " + bodyRadius + " head: " + headRadius);
        makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
        maxLife = (int)life;
        setLife((int)life, (int)life);
        //this.level = level;
        if (Program.debug) System.out.println("Snake was uploaded");
        body.setFixedRotation(true);
        /*if (level == Koopa.FLYING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.FLYING_ALONG_SINUSOID_PATH, this);
        else if (level == Koopa.JUMPING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_AND_REGULARLY_JUMP, this);
        else if (level == Koopa.GOING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
        else globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);*/
        findPlayerAtStart();
        mirrorBody();
    }

    private void makeBody(Vec2 center, float width, float height) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        final float additionalShiftingAlongY = -height/3.2f;
        PolygonShape tail = new PolygonShape();
        float tailHalfWidth = PhysicGameWorld.controller.scalarPixelsToWorld(width/4);
        float tailHalfHeight = PhysicGameWorld.controller.scalarPixelsToWorld((height/8));
        Vec2 offsetToTail = new Vec2(0, PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        tail.setAsBox(tailHalfWidth, tailHalfHeight, offsetToTail, 0);

        CircleShape frontCircle = new CircleShape();
        CircleShape backCircle = new CircleShape();
        frontCircle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/4f);
        backCircle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/4f);
        Vec2 offsetToFrontCircle = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(width/2f), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        Vec2 offsetToBackCircle = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(-width/2f), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        frontCircle.m_p.set(offsetToFrontCircle.x,offsetToFrontCircle.y);
        backCircle.m_p.set(offsetToBackCircle.x,offsetToBackCircle.y);

        //Head and hals
        PolygonShape head = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(width/4f);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((height/4f));
        Vec2 offset;
        if (WITHOUT_BODY_MIRRORING) {
            //offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld((width/4f)), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus
            offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(0), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus
            //offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld((width/4f)), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus
        }
        else {
            offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(0), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus
        }

        head.setAsBox(box2dW, box2dH, offset, PApplet.radians(-20));

        body.createFixture(tail,tailDensity);
        body.createFixture(head, headDensity);
        body.createFixture(frontCircle, tailDensity);
        body.createFixture(backCircle, tailDensity);
        moovingDirectionIsChanging = true;

        mirrorBody();

        body.setFixedRotation(true);
        body.setAngularDamping(2);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        System.out.println("Snake body was successfully made");
    }

    private void makeBodyOldAndStopsOnBoard(Vec2 center, float width, float height) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        final float additionalShiftingAlongY = -height/3.2f;
        PolygonShape tail = new PolygonShape();
        float tailHalfWidth = PhysicGameWorld.controller.scalarPixelsToWorld(width/4);
        float tailHalfHeight = PhysicGameWorld.controller.scalarPixelsToWorld((height/8));
        Vec2 offsetToTail = new Vec2(0, PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        tail.setAsBox(tailHalfWidth, tailHalfHeight, offsetToTail, 0);

        CircleShape frontCircle = new CircleShape();
        CircleShape backCircle = new CircleShape();
        frontCircle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/4f);
        backCircle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/8f);
        Vec2 offsetToFrontCircle = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(width/2f), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        Vec2 offsetToBackCircle = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(-width/2f), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY));  // maybe minus
        frontCircle.m_p.set(offsetToFrontCircle.x,offsetToFrontCircle.y);
        backCircle.m_p.set(offsetToBackCircle.x,offsetToBackCircle.y);

        //Head and hals
        PolygonShape head = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(width/4f);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((height/4f));
        Vec2 offset;
        if (WITHOUT_BODY_MIRRORING) offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld((width/4f)), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus
        else offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(0), PhysicGameWorld.controller.scalarPixelsToWorld(additionalShiftingAlongY)-PhysicGameWorld.controller.scalarPixelsToWorld(+height/2f));  // maybe minus

        head.setAsBox(box2dW, box2dH, offset, PApplet.radians(-20));

        body.createFixture(tail,tailDensity);
        body.createFixture(head, headDensity);
        body.createFixture(frontCircle, tailDensity);
        body.createFixture(backCircle, tailDensity);
        moovingDirectionIsChanging = true;

        mirrorBody();

        body.setFixedRotation(true);
        body.setAngularDamping(2);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        System.out.println("Snake body was successfully made");
    }


    private Fixture getHeadFixture(){
        int size = 0;
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            size++;
            if (f.getDensity() == headDensity || Float.compare(f.getDensity(), headDensity) == 0){
                return f;
            }
        }
        System.out.println("Head fixture was not founded; Body has only " + size + " fixtures");
        return null;
    }

    public void mirrorBody() {
           // on testing
            try {
                if (moovingDirectionIsChanging && !dead) {
                    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld((boundingWidth / 2f));
                    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((boundingHeight / 2f));
                    float directionCoef = 1f;
                    if (body.getLinearVelocity().x > 0.1f) {
                        sightDirection = TO_RIGHT;
                    } else if (body.getLinearVelocity().x < (-0.1f)) {
                        directionCoef = -1f;
                        sightDirection = TO_LEFT;
                    }
                    body.destroyFixture(getHeadFixture());
                    Vec2 offset = new Vec2(PhysicGameWorld.controller.scalarPixelsToWorld(directionCoef * (boundingWidth / 4f)), 0f);  // maybe minus
                    PolygonShape head = new PolygonShape();
                    head.setAsBox(box2dW, box2dH, offset, 0);
                    body.createFixture(head, headDensity);
                    //System.out.println("Direction was changed");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
        //setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }



    /*
    private Fixture getBodyFixture(){
        Fixture head = getHeadFixture();
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (!f.equals(head)) return f;
        }
        return null;

    }*/

    private void drawDebugGraphic(Vec2 pos, float a, GameCamera gameCamera){
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
        Program.objectsFrame.rotate(-a);
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.strokeWeight(1);
        Program.objectsFrame.stroke(25,25,155);
        for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
            if (f.getShape().getClass() == PolygonShape.class) {
                PolygonShape shape = (PolygonShape) f.getShape();
                Vec2[] points = shape.m_vertices;
                for (int i = 0; i < points.length; i++) {
                    Program.objectsFrame.point(PhysicGameWorld.controller.vectorWorldToPixels(points[i]).x, PhysicGameWorld.controller.vectorWorldToPixels(points[i]).y);
                }
                Program.objectsFrame.stroke(255, 25, 15);
                Program.objectsFrame.strokeWeight(1);
            }
            else {
                CircleShape circle = (CircleShape) f.getShape();
                Program.objectsFrame.stroke(22, 255, 15);
                Program.objectsFrame.strokeWeight(1);
                Program.objectsFrame.noFill();
                Program.objectsFrame.ellipse(PhysicGameWorld.controller.vectorWorldToPixels(circle.m_p).x, PhysicGameWorld.controller.vectorWorldToPixels(circle.m_p).y, PhysicGameWorld.controller.scalarWorldToPixels(f.getShape().m_radius) * 2f, PhysicGameWorld.controller.scalarWorldToPixels(f.getShape().m_radius) * 2f);
            }
        }
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();
    }

    public void setGraphicFlip(boolean flip){
        enemiesAnimationController.setGraphicFlip(flip);
    }

    private void drawAnimation(Vec2 pos, float a, GameCamera gameCamera){
        if (!behaviourController.isStartedToDie()) {
            if (behaviourController.getStatement() == SnakeController.SLEEP){
                enemiesAnimationController.drawSingleSpriteFromAnimation(gameCamera, EnemiesAnimationController.AWAKE, pos, a, enemiesAnimationController.getGraphicFlip(), 0);
            }
            else if (behaviourController.getStatement() == SnakeController.AWAKE){
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.AWAKE, pos, a);
            }
            else if (behaviourController.getStatement() == SnakeController.FALL_ASLEEP){
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.AWAKE, pos, a);
            }
            else if (behaviourController.getStatement() == SnakeController.ATTACK){
                enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.ATTACK, pos, a);
            }
            else {
                if (statement == IN_AIR) {
                    if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.ACTIVE) {
                        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.PAUSED);
                    }
                }
                else if (statement == ON_MOVEABLE_PLATFORM || statement == ON_GROUND) {
                    if (enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).getAnimationStatement() == SpriteAnimation.PAUSED) {
                        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setAnimationStatement(SpriteAnimation.ACTIVE);
                    }
                }
                tintUpdatingBySelecting();
                if (body.getLinearVelocity().x < 0) enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, true);
                else enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.GO, pos, a, false);
            }
        }
        else{
            boolean sideForDying = behaviourController.getLastOrientation();
            enemiesAnimationController.drawAnimation(gameCamera, EnemiesAnimationController.DYING, pos, a, sideForDying);
        }
    }

    @Override
    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
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
    public void setContactWithPlayer(Person player) {
        behaviourController.setContactWithPlayer(player);
    }

    @Override
    public void update(GameRound gameRound){
        super.update();
        if (body.isActive()) {
            behaviourController.update(gameRound);
        }
        else System.out.println("Snake is not active " + this);
        if (headMustBeDeleted && !headWasDeleted) {
            simplifyBody();
            System.out.println("Snake body must be simplified");
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
        return false;
    }

    @Override
    public void kill() {
        //super.kill();
        if (!behaviourController.isStartedToDie()) {
            behaviourController.kill();
            if (Program.debug) System.out.println("It was killed but the body is not simplyfied het");
        }
        else {
            SnakeController snakeController = (SnakeController) behaviourController;
            if (enemiesAnimationController.getSpriteAnimation(PersonAnimationController.DYING).isActualSpriteLast()) {
                headMustBeDeleted = true;
                simplifyBody();
                super.kill();
                if (Program.debug) System.out.println("Snake body made simple");
            }
        }
    }

    @Override
    public void simplifyBody(){
        /*float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld((boundingWidth / 2f));
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((boundingHeight / 2f));

         */
        /*float directionCoef = 1f;
        if (body.getLinearVelocity().x > 0.1f) {
            sightDirection = TO_RIGHT;
        } else if (body.getLinearVelocity().x < (-0.1f)) {
            directionCoef = -1f;
            sightDirection = TO_LEFT;
        }
         */
        Fixture head = getHeadFixture();
        System.out.println("Try to simplify snake body");
        if (head != null) {
            int contactsNumber = 0;
            for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
                if (f1.equals(getHeadFixture()) || f2.equals(getHeadFixture())) {
                    contactsNumber++;
                    PhysicGameWorld.beginContacts.remove(m);
                }
            }
            for (int m = (PhysicGameWorld.endContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.endContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.endContacts.get(m).getFixtureB();
                if (f1.equals(getHeadFixture()) || f2.equals(getHeadFixture())) {
                    contactsNumber++;
                    PhysicGameWorld.endContacts.remove(m);
                }
            }
            System.out.println("Try to delete head fixture. It was " + contactsNumber + " contacts with it");
            try {
                if (!PhysicGameWorld.controller.world.isLocked()) {
                    body.destroyFixture(head);
                }
                else System.out.println("World is locked. Can not simplify body");
                System.out.println("Fixture of the head was deleted. It was " + contactsNumber + " contacts with it");
                headWasDeleted = true;
            } catch (Exception e) {
                System.out.println("Can not delete head fixture");
                e.printStackTrace();
            }
        }
        else{
            System.out.println();
        }
    }



    @Override
    public int getAttackValue() {
        return 35;
    }


    @Override
    public void loadAnimationData(MainGraphicController mainGraphicController){
        super.loadAnimationData(mainGraphicController);
        float additionalGraphicScaleX  = 2.2f;
        float additionalGraphicScaleY  = 1.55f;
        SpriteAnimation movementAnimation = new SpriteAnimation(imageZoneFullDataForGoing.getName(), (int) imageZoneFullDataForGoing.leftX, (int) imageZoneFullDataForGoing.upperY, (int)imageZoneFullDataForGoing.rightX, (int) imageZoneFullDataForGoing.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)4, (byte)2, (int) 8);
        movementAnimation.setLastSprite((int)5);
        SpriteAnimation dyingAnimation = new SpriteAnimation(imageZoneFullDataForDying.getName(), (int) imageZoneFullDataForDying.leftX, (int) imageZoneFullDataForDying.upperY, (int)imageZoneFullDataForDying.rightX, (int) imageZoneFullDataForDying.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)4, (byte)2, (int) 8);
        dyingAnimation.setLastSprite((int)4);
        enemiesAnimationController.addNewAnimation(movementAnimation, EnemiesAnimationController.GO);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.GO).setSpritesShifting(new Vec2(0,0));
        enemiesAnimationController.addNewAnimation(dyingAnimation, EnemiesAnimationController.DYING);
        //dyingAnimation.setPlayOnce(true);
        enemiesAnimationController.setDeadSprite(EnemiesAnimationController.DYING, (int)4);
        enemiesAnimationController.getSpriteAnimation(EnemiesAnimationController.DYING).setSpritesShifting(new Vec2(0,0));
        SpriteAnimation awakeAnimation = new SpriteAnimation(imageZoneFullDataForGoing.getName(), (int) imageZoneFullDataForAwaking.leftX, (int) imageZoneFullDataForAwaking.upperY, (int)imageZoneFullDataForAwaking.rightX, (int) imageZoneFullDataForAwaking.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)1, (byte)3, (int) 8);
        enemiesAnimationController.addNewAnimation(awakeAnimation, EnemiesAnimationController.AWAKE);
        SpriteAnimation attackAnimation = new SpriteAnimation(attackData.getName(), (int) attackData.leftX, (int) attackData.upperY, (int)attackData.rightX, (int) attackData.lowerY, (int) (boundingWidth*additionalGraphicScaleX), (int) (boundingHeight*additionalGraphicScaleY), (byte)2, (byte)3, (int) 8);
        attackAnimation.setLastSprite((int) (attackAnimation.getSpritesNumber()-2));
        attackAnimation.setSpritesShifting(new Vec2(-11,0));
        enemiesAnimationController.addNewAnimation(attackAnimation, EnemiesAnimationController.ATTACK);
        enemiesAnimationController.loadSprites(mainGraphicController);
    }


    @Override
    public void attacked(int damageValue) {
        super.attacked(damageValue);
        behaviourController.attacked();
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
        data+= life;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= diameter;
        data+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data+= (int)behaviourController.getBehaviourType();
        System.out.println("Data for snake " + data);
        return data;
    }

    @Override
    public void setGraphicDimensionFromEditor(int newDiameter){
        System.out.println("Try to set new diameter for snake: " + diameter + " is now " + newDiameter);
        diameter = newDiameter;
        boundingWidth = newDiameter;
        boundingHeight = newDiameter;
        enemiesAnimationController.setNewDimensionForAllAnimations(newDiameter*2);
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }
}
