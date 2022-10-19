package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class RoundPolygon extends RoundElement implements ISimpleUpdateable, IDrawable{
    public final static String objectToDisplayName = "Triangle";
    public static String CLASS_NAME = "RoundPolygon";
    private ArrayList <Vec2> verteciesAbsolutePosInPixel;
    private final Vec2 geometricalCenter = new Vec2();
    private final Vec2 graphicCenter = new Vec2();
    //private ArrayList<Coordinate> pointsInWorldCoordinates = new ArrayList<Coordinate>();


    //OLD REALISATION
    public RoundPolygon(Vec2 position, ArrayList<Vec2> verticiesData,  float angle, int life, boolean withSpring, BodyType bodyType) {
        ArrayList<Vec2> verticies = new ArrayList<>();
        for (int i = 0; i < verticiesData.size(); i++) {
            System.out.print("Was: " + verticiesData.get(i));
            Vec2 vector = new Vec2(PhysicGameWorld.controller.vectorPixelsToWorld(verticiesData.get(i)));
            verticies.add(new Vec2(vector.x, vector.y));
            System.out.println("is: " + verticies.get(i));
        }
        calculateWidth(verticies);
        calculateHeight(verticies);
        makeBodyOldRealisation(new Vec2(position.x, position.y), verticies, angle, bodyType);
        setLife(life, life);
        if (bodyType == BodyType.DYNAMIC) this.withSpring = withSpring;
        else withSpring = false;
        if (withSpring) {
            spring = new Spring(this);
            if (withSpring) body.setFixedRotation(true);
        }

    }

    //NEW REALISATION
    public RoundPolygon(ArrayList<Vec2> pointsPositionsInPixelCoordinates,  float angle, int life, boolean withSpring, BodyType bodyType) {
        this.verteciesAbsolutePosInPixel = pointsPositionsInPixelCoordinates;
        boundingWidth = GameMechanics.getTriangleWidthByVectors(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2));
        boundingHeight = GameMechanics.getTriangleHeightByVectors(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2));
        geometricalCenter.x = GameMechanics.getTriangleGeometricalCenter(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2)).x;
        geometricalCenter.y = GameMechanics.getTriangleGeometricalCenter(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2)).y;
        graphicCenter.x = GameMechanics.getTriangleGraphicCenter(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2)).x;
        graphicCenter.y = GameMechanics.getTriangleGraphicCenter(pointsPositionsInPixelCoordinates.get(0), pointsPositionsInPixelCoordinates.get(1), pointsPositionsInPixelCoordinates.get(2)).y;

        makeBodyNewRealisation(pointsPositionsInPixelCoordinates, angle, bodyType);
        setLife(life, life);
        if (bodyType == BodyType.DYNAMIC) this.withSpring = withSpring;
        else withSpring = false;
        if (withSpring) {
            spring = new Spring(this);
            if (withSpring) body.setFixedRotation(true);
        }
    }

    //NEW REALISATION
    public RoundPolygon(GameObjectDataForStoreInEditor data) {
        this.verteciesAbsolutePosInPixel = new ArrayList<>();
        verteciesAbsolutePosInPixel.add(data.getFirstPoint());
        verteciesAbsolutePosInPixel.add(data.getSecondPoint());
        verteciesAbsolutePosInPixel.add(data.getThirdPoint());
        geometricalCenter.x = GameMechanics.getTriangleGeometricalCenter(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1), verteciesAbsolutePosInPixel.get(2)).x;
        geometricalCenter.y = GameMechanics.getTriangleGeometricalCenter(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1), verteciesAbsolutePosInPixel.get(2)).y;
        graphicCenter.x = GameMechanics.getTriangleGraphicCenter(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1), verteciesAbsolutePosInPixel.get(2)).x;
        graphicCenter.y = GameMechanics.getTriangleGraphicCenter(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1),verteciesAbsolutePosInPixel.get(2)).y;

        boundingWidth = GameMechanics.getTriangleWidthByVectors(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1), verteciesAbsolutePosInPixel.get(2));
        boundingHeight = GameMechanics.getTriangleHeightByVectors(verteciesAbsolutePosInPixel.get(0), verteciesAbsolutePosInPixel.get(1), verteciesAbsolutePosInPixel.get(2));
        makeBodyNewRealisation(verteciesAbsolutePosInPixel, data.getAngle(), data.getBodyType());
        setLife(life, life);
        BodyType bodyType = data.getBodyType();
        if (bodyType == BodyType.DYNAMIC) this.withSpring = data.isWithSpring();
        else withSpring = false;
        if (withSpring) {
            spring = new Spring(this);
            if (withSpring) body.setFixedRotation(true);
        }
        loadImageData(data.getPathToTexture(), data.getGraphicLeftX(), data.getGraphicUpperY(), data.getGraphicRightX(), data.getGraphicLowerY(), data.getWidth(), data.getHeight(), false);

    }

    private void makeBodyNewRealisation(ArrayList<Vec2> pointsPositionsInPixelCoordinates, float angle, BodyType bodyType) {
        Vec2 centerInPixelCoordinates = getPixelCenter(pointsPositionsInPixelCoordinates);
        Vec2 centerInWorldCoordinates = PhysicGameWorld.controller.vectorPixelsToWorld(centerInPixelCoordinates);
        Vec2[] relativeCenterVerteciesInPixelCoordinate = getPixelRelativeCenterCoordinates(centerInPixelCoordinates, pointsPositionsInPixelCoordinates);
        PolygonShape sd = new PolygonShape();
        Vec2[] vertices = new Vec2[pointsPositionsInPixelCoordinates.size()];
        for (int i = 0; i <pointsPositionsInPixelCoordinates.size(); i++){
            vertices[i] = new Vec2(PhysicGameWorld.controller.coordPixelsToWorld(relativeCenterVerteciesInPixelCoordinate[i]));
        }
        sd.set(vertices, vertices.length);
        System.out.println("Center: " + centerInPixelCoordinates + "; In world: " + centerInWorldCoordinates + "; Coordinates: " + pointsPositionsInPixelCoordinates.size() +" or " + vertices.length);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(centerInWorldCoordinates);
        bd.angle = (float) Math.toRadians(-angle);
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd, 1.0f);
        body.getFixtureList().setDensity(1);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);

    }

    private Vec2[] getPixelRelativeCenterCoordinates(Vec2 centerInPixelCoordinates, ArrayList<Vec2> pointsPositionsInPixelCoordinates) {
        Vec2[] relativeCenterVerteciesInPixelCoordinate = new Vec2[pointsPositionsInPixelCoordinates.size()];
        for (int i = 0; i <pointsPositionsInPixelCoordinates.size(); i++){
            relativeCenterVerteciesInPixelCoordinate[i] = pointsPositionsInPixelCoordinates.get(i).sub(centerInPixelCoordinates );
        }
        return relativeCenterVerteciesInPixelCoordinate;
    }

    private Vec2 getPixelCenter(ArrayList<Vec2> pointsPositionsInPixelCoordinates) {
        ArrayList<Coordinate> coordinatesInPixelCoordinates = new ArrayList<>();
        for (Vec2 vector : pointsPositionsInPixelCoordinates){
            coordinatesInPixelCoordinates.add(new Coordinate(vector.x, vector.y));
        }
        Vec2 centerInPixelCoordinates = GameMechanics.getTriangleGeometricalCenter(coordinatesInPixelCoordinates.get(0) , coordinatesInPixelCoordinates.get(1)  , coordinatesInPixelCoordinates.get(2) );

        return centerInPixelCoordinates;
    }

    private void makeBodyOldRealisation(Vec2 center, ArrayList<Vec2> verticies, float angle, BodyType bodyType) {
        PolygonShape sd = new PolygonShape();
        Vec2[] vertices = new Vec2[verticies.size()];
        for (int i = 0; i < verticies.size(); i++){
            vertices[i] = verticies.get(i);
        }
        sd.set(vertices, vertices.length);
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        bd.angle = (float) Math.toRadians(angle);
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd, 1.0f);
        body.getFixtureList().setDensity(1);
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
    }

    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        System.out.println("Sprite  was loaded for " + this.getClass());
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
    }

    public void loadSprites(Tileset tilesetUnderPath) {
        sprite.loadSprite(tilesetUnderPath);
    }


    /*
    private void makeJawBody(Vec2 pos, int diameter, boolean side) {
		BodyDef bd = new BodyDef();
	    bd.type = BodyType.DYNAMIC;
	    Vec2 offset = null;
	    if (side == LEFT) offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2((diameter)/1.9f, (float)-3.6*rodLength/7));
	    else offset = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(-(diameter)/1.9f, (float)-3.6*rodLength/7));

	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(pos).add(offset));
    //if (side == RIGHT) bd.setAngle(0.4f);
    //else bd.setAngle(-0.4f);
	    if (side == LEFT) jaw1 = PhysicGameWorld.controller.createBody(bd);
	    else jaw2 = PhysicGameWorld.controller.createBody(bd);
    PolygonShape jawShape = new PolygonShape();
    Vec2[] vertices = new Vec2[4];
    float startAngle;
	    if (side == LEFT) startAngle = 0.00f;
	    else startAngle = PConstants.PI;
	    for (byte i = 0; i < vertices.length; i++) {
        vertices[i] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(
                (float)((diameter/2)*Math.cos(startAngle)),
                (float)((diameter/2)*Math.sin(startAngle))));
        if (side == LEFT) startAngle+=PConstants.PI/3;
        else startAngle-=PConstants.PI/3;
    }
	    jawShape.set(vertices, vertices.length);
	    if (side == LEFT) {
        jaw1.createFixture(jawShape, 1.0f);
        jaw1.setGravityScale(1);

    }
	    else {
        jaw2.createFixture(jawShape, 1.0f);
        jaw2.setGravityScale(1);
    }
	    if (Game2D.DEBUG) System.out.println("Flower jaw was succesfully made");
}


    */

/*
    public RoundPolygon(ArrayList<Vec2> verticiesData, float angle, int life, boolean withSpring) {
        this.withSpring = withSpring;
        this.life = life;
        convertVerticies(verticiesData);
        this.verticies = verticiesData;
        PolygonShape sd = new PolygonShape();
        Vec2[] verticesPositions = new Vec2[verticiesData.size()];
        for (int i = 0; i < verticiesData.size(); i++) {
            verticesPositions[i] = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(verticiesData.get(i).x, verticiesData.get(i).y));
        }
        sd.set(verticesPositions, verticiesData.size());
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(verticesPositions[0]));
        body = PhysicGameWorld.controller.createBody(bd);
        body.createFixture(sd, 1.0f);
        body.setTransform(body.getPosition(), angle);
    }*/

    /*
    private ArrayList<Vec2> convertVerticies(ArrayList<Vec2> verticiesData){
        for (int i = 0; i < verticiesData.size(); i++) {
            //Vec2 vector = new Vec2(PhysicGameWorld.controller.vectorPixelsToWorld(verticiesData.get(i)));
            Vec2 vector = new Vec2(PhysicGameWorld.controller.coordPixelsToWorld(verticiesData.get(i)));
            //vector = PhysicGameWorld.controller.vectorPixelsToWorld(verticiesData.get(i));
            verticiesData.get(i).x = vector.x;
            verticiesData.get(i).y = vector.y;
            //verticiesData.get(i).x = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(verticiesData.get(i).x, verticiesData.get(i).y)).x;
            //verticiesData.get(i).y = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(verticiesData.get(i).x, verticiesData.get(i).y)).y;
        }
        return verticiesData;
    }*/

    private void calculateWidth(ArrayList<Vec2> verticies){
        float minX = 999999;
        float maxX = -999999;
        for (int i = 0; i < verticies.size(); i++){
            if (verticies.get(i).x< minX) minX = verticies.get(i).x;
        }
        for (int i = 0; i < verticies.size(); i++){
            if (verticies.get(i).x> maxX) maxX = verticies.get(i).x;
        }
        for (int i = 0; i < verticies.size(); i++){
            //System.out.println("Verticies: " + verticies.get(i));
        }
        boundingWidth = PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels (maxX-minX));
        //System.out.println("Width: " + boundingWidth);
    }

    private void calculateHeight(ArrayList<Vec2> verticies){
        float minY = 999999;
        float maxY = -999999;
        for (int i = 0; i < verticies.size(); i++){
            if (verticies.get(i).y< minY) minY = verticies.get(i).y;
        }
        for (int i = 0; i < verticies.size(); i++){
            if (verticies.get(i).y> maxY) maxY = verticies.get(i).y;
        }
        boundingHeight = PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels (maxY-minY));
        //System.out.println("Height: " + boundingHeight);
    }

    @Override
    public void draw(GameCamera gameCamera) {
        //Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle();
        if (isVisibleOnScreen(gameCamera, graphicCenter)) {
            if (Program.debug) {
                Fixture f = body.getFixtureList();
                PolygonShape ps = (PolygonShape) f.getShape();
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(geometricalCenter.x - gameCamera.getActualXPositionRelativeToCenter(), geometricalCenter.y - gameCamera.getActualYPositionRelativeToCenter());
                //Program.objectsFrame.translate(graphicCenter.x - gameCamera.getActualXPositionRelativeToCenter(), graphicCenter.y - gameCamera.getActualYPositionRelativeToCenter());
                //
                //Program.objectsFrame.translate(geometricalCenter.x - gameCamera.getActualXPositionRelativeToCenter(), geometricalCenter.y - gameCamera.getActualYPositionRelativeToCenter());
                //Program.objectsFrame.translate(+ gameCamera.getActualXPositionRelativeToCenter(),  + gameCamera.getActualYPositionRelativeToCenter());
                Program.objectsFrame.rotate(a);
                Program.objectsFrame.pushStyle();
                Program.objectsFrame.stroke(120, 45, 45);
                Program.objectsFrame.strokeWeight(2);
                Program.objectsFrame.rectMode(PApplet.CENTER);
                //Program.objectsFrame.fill(200,55,55);
                //Program.objectsFrame.noFill();
                Program.objectsFrame.strokeWeight(0.8f);
                Program.objectsFrame.beginShape();
                //Program.objectsFrame.translate(gameCamera.getActualXPositionRelativeToCenter(), gameCamera.getActualYPositionRelativeToCenter());
                for (int i = 0; i < ps.getVertexCount(); i++) {
                    Vec2 v = PhysicGameWorld.controller.coordWorldToPixels(ps.getVertex(i));
                    //Vec2 v = PhysicGameWorld.controller.vecWorldToPixels(ps.getVertex(i));
                    //System.out.println("Vertex: " + (int) (v.x-geometricalCenter.x) + ", " + (int)(v.y+geometricalCenter.y));
                    Program.objectsFrame.vertex(v.x, v.y);
                }
                //System.out.println("; Geometrical : " + geometricalCenter);
                Program.objectsFrame.endShape(PConstants.CLOSE);
                Program.objectsFrame.popStyle();
                Program.objectsFrame.popMatrix();
            }
        }
        if (Program.WITH_GRAPHIC) {
            if (hasGraphic()) {
                if (sprite != null) {
                    tintUpdatingBySelecting();
                    sprite.draw(gameCamera, graphicCenter, -a);
                }

            }
        }

    }
    /*
    @Override
    public void draw(GameCamera gameCamera) {
        Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
        float a = body.getAngle();
        if (isVisibleOnScreen(gameCamera, pos)) {
            if (Program.debug){

            }

            Fixture f = body.getFixtureList();
            PolygonShape ps = (PolygonShape) f.getShape();
            //updateGraphicSidesData();
            if (Program.USE_BACKGROUND_BUFFER) {
                //Programm.objectsFrame.beginDraw();
                Program.objectsFrame.pushMatrix();
                Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
                Program.objectsFrame.translate(pos.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, pos.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
                Program.objectsFrame.rotate(-a);
                Program.objectsFrame.pushStyle();
                Program.objectsFrame.stroke(120, 45, 45);
                Program.engine.rectMode(PApplet.CENTER);
                Program.objectsFrame.noFill();
                Program.objectsFrame.strokeWeight(0.8f);
                Program.objectsFrame.beginShape();
                for (int i = 0; i < ps.getVertexCount(); i++) {
                    Vec2 v = PhysicGameWorld.controller.vectorWorldToPixels(ps.getVertex(i));
                    //System.out.print(";  Pos: " + i + " on: " + v);
                    Program.objectsFrame.vertex(v.x, v.y);
                }
                //System.out.println(" " );
                Program.objectsFrame.endShape(PConstants.CLOSE);
                Program.objectsFrame.popStyle();
                Program.objectsFrame.popMatrix();
                //Programm.objectsFrame.endDraw();
                if (Program.WITH_GRAPHIC) {
                    if (hasGraphic()) {
                        if (sprite != null) sprite.draw(gameCamera, body);
                        if (spriteAnimation != null) {
                            spriteAnimation.update();
                            spriteAnimation.draw(gameCamera, pos, a, false);
                        }
                    }
                }
            }
        }
    }
     */



    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }



    @Override
    public void update() {

    }

    @Override
    public String getStringData(){
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createRoundPolygon(RoundPolygon.class);
        System.out.println("Data string for round triangle: " + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    public ArrayList<Vec2> getVerteciesAbsolutePosInPixel() {
        return verteciesAbsolutePosInPixel;
    }

    public String getClassName(){
        return CLASS_NAME;
    }
}
