package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamecontrollers.BulletTimeController;
import com.mgdsstudio.blueberet.gamecontrollers.LaunchableWhizbangsController;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class HandGrenade extends AbstractGrenade {

    private final String objectToDisplayName = "Hand grenade";
    private static StaticSprite sprite;
    private static final float dimensionCoefficient = 2.3f;
    public final static int NORMAL_DIAMETER = 15;
    public final static float NORMAL_SPEED = 40;
    public final static boolean EXPLOSION_BY_CONTACT_WITH_ENEMY = true;
    private int timeBeforeExplosion = 1800;
    private Timer timer;
    private final LaunchableWhizbangsController launchableWhizbangsController;
    private boolean canBeExplodedByTimer;
    public final static float GRAVITY_COEF = 1f;

    public HandGrenade(PVector position, float shootingAngle, LaunchableWhizbangsController launchableWhizbangsController) {
        boundingWidth = NORMAL_DIAMETER;
        boundingHeight = NORMAL_DIAMETER;
        speed = NORMAL_SPEED;
        System.out.println("This greenade has angle " + shootingAngle + " and direction: " +direction);
        direction = TO_RIGHT;	// only for body making
        makeBody(new Vec2(position.x, position.y), (int)boundingWidth, (int)boundingHeight);
        setData(shootingAngle, 1f);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_BULLET);
        timer = new Timer(timeBeforeExplosion);
        this.launchableWhizbangsController = launchableWhizbangsController;
    }


    @Override
    protected void makeBody(Vec2 center, int w, int h) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);

        CircleShape circle = new CircleShape();
        circle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(w/2);
        Vec2 offset;
        offset = new Vec2(0,0);
        circle.m_p.set(offset.x,offset.y);
        body.createFixture(circle, 1.0f);
        body.setGravityScale(GRAVITY_COEF);
        body.getFixtureList().setFriction(1.499f);
        body.setAngularDamping(5);
        body.setBullet(true);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        //if (sprite == null) {
            sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width, height);
        //}
    }

    public static void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight){
        //if (sprite == null) {
            //System.out.println("Sprite for hand");
            sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, (int) ((xRight - xLeft) * dimensionCoefficient), (int) ((yRight - yLeft) * dimensionCoefficient));
        //}
    }

    @Override
    public void update(){
        super.update();
        if (!canBeExplodedByTimer) {
            if (timer.isTime()) {
                canBeExplodedByTimer = true;
                //launchableWhizbangsController.addNewExplosion();
            }
        }
        /*if (!exploded) {
            if (timer.isTime()) {
                //launchableWhizbangsController.killWhizbangWithExplosion(this);
                exploded = true;
            }
            else if (){

            }
        }*/


    }




    public StaticSprite getSprite(){
        return sprite;
    }

    public static void loadSprites(Tileset tilesetUnderPath) {
        sprite.loadSprite(tilesetUnderPath);
        System.out.println("Sprite for hand grenade was uploaded from path " + tilesetUnderPath.getPath());
    }

    @Override
    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle()- PApplet.PI/2;
        if (sprite != null) {
            sprite.draw(gameCamera, pos, a);
        }

        /*
        if (Program.debug){
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.rotate(-a);
            Program.objectsFrame.pushStyle();
            Program.objectsFrame.noFill();
            //Program.objectsFrame.rectMode(PConstants.CENTER);
            Program.objectsFrame.strokeWeight(1.8f);
            Program.objectsFrame.stroke(0);
            Program.objectsFrame.ellipse(0, 0, boundingWidth, boundingHeight);
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
        }
        */
    }

    @Override
    public boolean isBulletColisionWithFixtureMakeExplosion(Fixture collidedWhizbangFixture) {
        return true;
    }

    @Override
    public boolean isColisionWithFixtureMakeExplosion(Body contactBody, GameRound gameRound, Fixture collidedWhizbangFixture) {
        System.out.println("Test grenade explosion");
        if (!EXPLOSION_BY_CONTACT_WITH_ENEMY) {
        }
        else {
            ArrayList<Person> persons = gameRound.getPersons();
            for (Person person : persons) {
                if (contactBody.equals(person.body)) {
                    System.out.println("Contact with an enemy. Explosion must be added");
                    return true;
                }
                //for (Fixture f = person.body.getFixtureList(); f != null; f = f.getNext()) {
                    /*if (f.equals(collidedWhizbangFixture)) {
                        System.out.println("Contact with an enemy. Explosion must be added");
                        return true;
                    }*/
                //}
            }
        }
        return false;
    }

    @Override
    public boolean canBeExplodedByTimer() {
        return canBeExplodedByTimer;
    }

    public void bulletTimeIsActivated(boolean flag) {
        if (flag) timer.enterBulletTimeMode(BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS);
        else timer.enterBulletTimeMode(1f/BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS);
        /*
        float coef = BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS;
        System.out.print("Time before explosion was: " + timer.getRestTime());
        timer.bulletTimeModeApplied(flag, coef);
        System.out.println(" and now: " + timer.getRestTime());*/

    }
}
