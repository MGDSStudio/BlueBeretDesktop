package com.mgdsstudio.blueberet.gameobjects.persons.flower;

import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import processing.core.PConstants;

import java.util.ArrayList;

public abstract class Plant extends Enemy {
    //public Body body;
    protected StaticSprite sprite;
    private final static String objectToDisplayName = "Plant";

    protected static final float RELATIVE_GRAPHIC_ANGLE_FOR_JAWS = -PConstants.PI/8;	// I can not rotate the sprite; It makes the game with this additional angle
    public final static byte ROD_PART = 1;
    public final static byte LEFT_JAW = 2;
    public final static byte RIGHT_JAW = 3;

    protected int diameter;
    //protected int pipeHeight;
    protected int rodHeight;


    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;

    // Type of attack
    final private static byte FROM_BULLET = 1;
    final private static byte FROM_EXPLOSION = 2;

    // Life
    private int life;

    protected int basicAngle;
    protected final static boolean hideGraphic = false;

    protected Body[] linkedBodies;

    private PlantController plantController;



    public Plant(PlantController plantController, int life, int pipeInsideDiameter, int basicAngle, int rodHeight) {
        role = ENEMY;
        diameter = pipeInsideDiameter;
        this.basicAngle = basicAngle;
        this.rodHeight = rodHeight;
        //Vec2 jointPosition = new Vec2(position.x, position.y);
        //this.pipeHeight = pipeHeight;
        //if (partType == ROD_PART) makeRodBody(new Vec2(jointPosition.x, jointPosition.y), diameter);
        //else if (partType == LEFT_JAW) makeJawBody(new Vec2(jointPosition.x, jointPosition.y), diameter, LEFT);
        //else if (partType == RIGHT_JAW) makeJawBody(new Vec2(jointPosition.x, jointPosition.y), diameter, RIGHT);

        maxLife = life;
        setLife(life, life);
        this.plantController = plantController;
        //if (ObjectsActiveLoader.ACTIVE_LOADING) linkedBodies = new Body[2];
    }

    /*
    public void loadImageData(String path, byte part, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        System.out.println("Sprite " + part +  "  was loaded for " + this.getClass());
        if (part == ROD_PART) rodSprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
        else if (part == LEFT_JAW) leftJawSprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
        else if (part == RIGHT_JAW) rightJawSprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
        else System.out.print("There are no part for this picture");
    }

    public void loadSprites(Tileset tilesetUnderPath, byte part) {
        if (part == ROD_PART) rodSprite.loadSprite(tilesetUnderPath);
        else if (part == LEFT_JAW) leftJawSprite.loadSprite(tilesetUnderPath);
        else if (part == RIGHT_JAW) rightJawSprite.loadSprite(tilesetUnderPath);
        else System.out.print("There are no part for this picture");
    }

    public StaticSprite getSprite(byte part){
        if (part == ROD_PART) return rodSprite;
        else if (part == LEFT_JAW) return leftJawSprite;
        else if (part == RIGHT_JAW) return rightJawSprite;
        else {
            System.out.print("There are no part for this picture");
            return null;
        }
    }*/

    public StaticSprite getSprite(){
        return sprite;
    }

    public String getPathToSprite(){
        return sprite.getPath();
    }


    private void setFilterData(byte group) {
        Filter filter = new Filter();
        filter.groupIndex = group;
        for (Fixture fixture = body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
            fixture.setFilterData(filter);
        }
    }

    @Override
    public boolean isPartOfSomeJoint(){
        return true;
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }


    public void setLinkedBodies(Body [] bodies){
        this.linkedBodies = bodies;
    }

    public ArrayList <Body> getJoindedBodies(){
        return plantController.getBodies();
    }




    @Override
    protected void updateAngle() {
        System.out.println("Function updateAngle for plant was not overridden");
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
        return 30;
    }
}
