package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.GlobalAI_Controller;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

public class Raven extends Enemy implements IDrawable, ISimpleUpdateable {
    public final static String CLASS_NAME = "Raven";
    private final String objectToDisplayName = "Raven";
    // We need specific graphic file
    public final static ImageZoneFullData imageZoneFullData = new ImageZoneFullData(Program.getAbsolutePathToAssetsFolder("Gumbas spritesheet.png") , (int)1, (int)1, (int)489, (int)528);
    private final static float additionalGraphicScale  = 1.4f;
    public final static int NORMAL_WIDTH = 40;
    final static int NORMAL_HEIGHT = 33;
    public final static int NORMAL_DIAMETER = NORMAL_WIDTH;
    private int diameter = NORMAL_WIDTH;
    private final float NORMAL_ACCELERATE = 5f;
    public static int NORMAL_LIFE = 200;
    static int normalMovementImpulseX = 5;
    static float maxNormalSpeed = 0.5f;
    private float headRadius;

    public Raven(PVector position, int life, int diameter, byte behaviourModel) {
        init(position, life, diameter, behaviourModel);
    }

    private void init(PVector position, int life, int diameter, byte behaviourModel) {
        this.diameter = (int)diameter;
        role = ENEMY;
        boundingWidth = diameter;
        boundingHeight = diameter;
        headRadius = boundingWidth/3.1f;
        makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
        maxLife = (int)life;
        setLife((int)life, (int)life);
        this.level = level;
        if (Program.debug) System.out.println("Raven was uploaded");
        body.setFixedRotation(true);
        if (level == FLYING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.FLYING_ALONG_SINUSOID_PATH, this);
        else if (level == JUMPING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_AND_REGULARLY_JUMP, this);
        else if (level == GOING) globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
        else globalAIController = new GlobalAI_Controller(GlobalAI_Controller.GO_LEFT_AND_RIGHT, this);
        findPlayerAtStart();
    }

    private void makeBody(Vec2 center, float width, float height) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape bodyShape = new CircleShape();
        bodyShape.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(width/2);
        Vec2 bodyOffset = new Vec2(0, 0);
        bodyShape.m_p.set(bodyOffset.x,bodyOffset.y);
        /*
        PolygonShape corpus = new PolygonShape();
        Vec2[] vertices = new Vec2[6];
        vertices[0] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-width/2, 0));
        vertices[1] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(2*width/5), height/2));
        vertices[2] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((2*width/5), height/2));
        vertices[3] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(width/2, 0));
        vertices[4] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((1*width/8), -height/2));
        vertices[5] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((-1*width/8), -height/2));
        corpus.set(vertices, vertices.length);
        */
        CircleShape head = new CircleShape();
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(headRadius);
        Vec2 offset = new Vec2((float) ((-width/2)*0.92), (float) ((height/2)*(-0.92)));
        head.m_p.set(offset.x,offset.y);
        if (center == null) System.out.println("Position is null");
        body.createFixture(bodyShape,1.4f);
        body.createFixture(head, 0.2f);
        moovingDirectionIsChanging = true;
        mirrorBody();
        System.out.println("Raven body was successfully made");
        body.setFixedRotation(true);
        body.setAngularDamping(2);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    private void mirrorBody() {
        if (moovingDirectionIsChanging) {
            if (body.getLinearVelocity().x>0.1f) {
                Fixture f = getHeadFixture();
                CircleShape head = (CircleShape) f.getShape();
                head.m_p.x=Math.abs(head.m_p.x);
                sightDirection = TO_RIGHT;
            }
            else if (body.getLinearVelocity().x<(-0.1f)) {
                Fixture f = getHeadFixture();
                CircleShape head = (CircleShape) f.getShape();
                head.m_p.x=-1*Math.abs(head.m_p.x);
                sightDirection = TO_LEFT;
            }
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

    }

    @Override
    protected void updateAngle() {

    }

    @Override
    public int getPersonWidth() {
        return (int) getWidth();
    }

    @Override
    public boolean attackByDirectContact(Person nearestPerson) {
        return true;
    }

    @Override
    public int getAttackValue() {
        return 0;
    }
}
