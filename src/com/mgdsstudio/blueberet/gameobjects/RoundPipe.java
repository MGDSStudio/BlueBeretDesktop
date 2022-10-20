package com.mgdsstudio.blueberet.gameobjects;


import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.Plant;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.PlantController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

//public class RoundPipe extends RoundStaticObject implements IUpdateable, IDrawable{
public class RoundPipe extends RoundElement implements IDrawable{
	private final String objectToDisplayName = "Pipe";
	public final static String CLASS_NAME = "RoundPipe";
	//private StaticSprite pipeWithoutFlangeSprite;
	private Flag flag;
	//public Flower flower;
	private PlantController plantController;
	private ObjectsClearingZone objectsClearingZone;

	// Orientations of entry
	//public static final byte TO_LEFT = 1;
	//public static final byte TO_RIGHT = 2;
	//public static final byte TO_UP = 3;
	//public static final byte TO_DOWN = 4;
	private byte entrySide = Program.TO_UP;

	//Body parameters
	public static final int WALL_THICKNESS = (int) (350/17.5);

	// only for graphic
	private Vec2 leftUpperPoint; 	// only for graphic
	private Vec2 flangePos;
	private Vec2 pipeWithoutFlangePos;
	private int flangeThickness = 100;

	// Dimensions
	final float w;
	final float h;
	//final int angle;
	private boolean withFlower = false;
	private byte flowerBehaviour;
	//private boolean oneGraphicElement = true;

	private float topWidthPixelValue;
	private float topHeightPixelValue = 1;

	
	public RoundPipe(PVector leftUpperCorner, float w, float h, int angle, byte flowerBehaviour, int flowerLife, int flowerDimension) {
		this.w = w;
		this.h = h;
		this.angleInDegrees = angle;
		if (angle == 270) entrySide = Program.TO_UP;
		else if (angle == 0  || angle == 360) entrySide = Program.TO_RIGHT;
		else if (angle == 90) entrySide = Program.TO_DOWN;
		else if (angle == 180) entrySide = Program.TO_LEFT;
		leftUpperPoint = new Vec2(0,0);
		if (flowerBehaviour != PlantController.NO_FLOWER) withFlower = true;
		makeBody(leftUpperCorner, w, h, entrySide);
		leftUpperPoint.x = getPixelPosition().x-w/2;
		leftUpperPoint.y = getPixelPosition().y-h/2;
		this.flowerBehaviour = flowerBehaviour;
		makeFlag();
		makePlant(flowerBehaviour, flowerLife, flowerDimension);
	}

	public RoundPipe(GameObjectDataForStoreInEditor data) {
		this.w = data.getWidth();
		this.h = data.getHeight();
		angleInDegrees = data.getAngle();
		if (angleInDegrees == 270) entrySide = Program.TO_UP;
		else if (angleInDegrees == 0  || angleInDegrees == 360) entrySide = Program.TO_RIGHT;
		else if (angleInDegrees == 90) entrySide = Program.TO_DOWN;
		else if (angleInDegrees == 180) entrySide = Program.TO_LEFT;
		System.out.println("Pipe angle: " + angleInDegrees + "; Side: " + entrySide);
		leftUpperPoint = new Vec2(0,0);
		flowerBehaviour = data.getFlowerBehaviour();
		if (flowerBehaviour!=PlantController.NO_FLOWER) withFlower = true;
		PVector leftUpperCorner = data.getLeftUpperCorner();
		makeBody(leftUpperCorner, w, h, entrySide);
		leftUpperPoint.x = getPixelPosition().x-w/2;
		leftUpperPoint.y = getPixelPosition().y-h/2;
		loadImageData(data.getPathToTexture(), data.getGraphicLeftX(), data.getGraphicUpperY(), data.getGraphicRightX(), data.getGraphicLowerY(), data.getWidth(), data.getHeight());
		System.out.println("data.getPathToTexture(): " + data.getPathToTexture());

		makeFlag();
		int flowerLife = (int)data.getLife();
		int flowerDimention = (int)data.getDiameter();
		makePlant(flowerBehaviour, flowerLife, flowerDimention);
	}

	private void fillSpritesPositionsData(){
		flangeThickness = sprite.getHeight();
		flangePos = new Vec2(0,0);
		pipeWithoutFlangePos = new Vec2(0,0);
		if (angleInDegrees == 270) {
			flangePos.x = getPixelPosition().x;
			flangePos.y = getPixelPosition().y-(h/2)+(flangeThickness/2);
			pipeWithoutFlangePos.x = getPixelPosition().x;
			pipeWithoutFlangePos.y = getPixelPosition().y+(flangeThickness/2);
		}
		else if (angleInDegrees == 90) {
			flangePos.x = getPixelPosition().x;
			flangePos.y = getPixelPosition().y+(h/2)-(flangeThickness/2);
			pipeWithoutFlangePos.x = getPixelPosition().x;
			pipeWithoutFlangePos.y = getPixelPosition().y-(flangeThickness/2);
		}
		else if (angleInDegrees == 0 || angleInDegrees == 360) {
			flangePos.x = getPixelPosition().x+(w/2)-(flangeThickness/2);
			flangePos.y = getPixelPosition().y;
			pipeWithoutFlangePos.x = getPixelPosition().x-(flangeThickness/2);
			pipeWithoutFlangePos.y = getPixelPosition().y;
		}
		else if (angleInDegrees == 180 || angleInDegrees == -180) {
			flangePos.x = getPixelPosition().x-(w/2)+(flangeThickness/2);
			flangePos.y = getPixelPosition().y;
			pipeWithoutFlangePos.x = getPixelPosition().x+(flangeThickness/2);
			pipeWithoutFlangePos.y = getPixelPosition().y;
		}
		else System.out.println("This angle " + angleInDegrees + " can not be used for pipes");
	}

	@Override
	public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
		/*
		if (!oneGraphicElement) {
			super.loadImageData(path, xLeft, yLeft, xRight, (int)(yRight-(Game2D.engine.abs(yRight-yLeft))/2), width,  (int) (width/2));
			//pipeWithoutFlangeSprite = new StaticSprite(path, xLeft, (int)(yRight-(Game2D.engine.abs(yRight-yLeft))/2), xRight, yRight, width,  (int) (height-(width/2)));
		}*/
		//else{
			super.loadImageData(path, xLeft, yLeft, xRight, yRight, (int)w,  (int)h);
		//}
		System.out.println("Sprites for pipes was succesfully uploaded");
	}

	public void loadSprites(Tileset tilesetForPipe, Tileset tilesetForJaw, Tileset tilesetForRod){
		sprite.loadSprite(tilesetForPipe);
		fillSpritesPositionsData();
		System.out.println("Tilesets for pipes was succesully uploaded");
		if (withFlower) makePlantGraphic(flowerBehaviour, tilesetForJaw, tilesetForRod);
		//makePlant(flowerBehaviour, tilesetForJaw, tilesetForRod);
	}

	public float getHeight(){
		return h;
	}

	public float getWidth(){
		return w;
	}

	private void makeBody(PVector center, float w, float h, byte entrySide) {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
	    //PVector leftUpperCorner = new PVector(center.x-w, center.y-h/2);
	    bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
	    bd.angle = PApplet.radians(angleInDegrees -270);
		//bd.angle = PApplet.radians(270-angle);
	    body = PhysicGameWorld.controller.createBody(bd);	    
	    PolygonShape leftWall = new PolygonShape();
	    float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(WALL_THICKNESS/2);
	    float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(h/2);
	    Vec2 offsetToLeftWall = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2((-w/2) + (WALL_THICKNESS/2), 0)));	    
	    leftWall.setAsBox(box2dW, box2dH, offsetToLeftWall, 0);
	    
	    PolygonShape rightWall = new PolygonShape();	    
	    Vec2 offsetToRightWall = PhysicGameWorld.controller.vectorPixelsToWorld((new Vec2((w/2) - (WALL_THICKNESS/2), 0)));
	    rightWall.setAsBox(box2dW, box2dH, offsetToRightWall, 0);
	    
	    PolygonShape top = new PolygonShape();
	    topWidthPixelValue = (w/2)-(WALL_THICKNESS);
	    float topWidth = PhysicGameWorld.controller.scalarPixelsToWorld(topWidthPixelValue);
		float topHeight = PhysicGameWorld.controller.scalarPixelsToWorld(topHeightPixelValue/2);
		//float topHeight = PhysicGameWorld.controller.scalarPixelsToWorld(topHeightPixelValue);

	    //Vec2 offsetToTop = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(0, -(h/2)+(WALL_THICKNESS)));
	    //top.setAsBox(topWidth, topHeight, offsetToTop, 0);
		//Vec2 offsetToTop = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(0, 0));
		Vec2 offsetToTop = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(0, (-h/2)+(topHeightPixelValue/2)));
		top.setAsBox(topWidth, topHeight, offsetToTop, 0);
		body.createFixture(top, 1f);
		body.getFixtureList().setUserData("Top");
		body.createFixture(leftWall, 1f);
	    body.createFixture(rightWall, 1f);


	    //body.setTransform(body.getPosition(), PApplet.radians(270-angle));
	    //setFilterData(top, COAlISION_FLOWER_WITH_PIPE_GROUP);
		setFilterDataForCategory(top);
	}

	private void setFilterDataForCategory(Shape topShape){
		System.out.println("Shape: Radius" + topShape.getRadius());
		int count = 0;
		for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
			if (f.getUserData() == "Top"){
				//if (true){
				f.m_filter.categoryBits = CollisionFilterCreator.CATEGORY_PIPE_INSIDE_AREA;
				f.m_filter.maskBits = CollisionFilterCreator.getMaskForCategory(CollisionFilterCreator.CATEGORY_PIPE_INSIDE_AREA);
				System.out.println("Top was founded for pipe and set filter mask: " + CollisionFilterCreator.CATEGORY_PIPE_INSIDE_AREA);
			}
			else {
				f.m_filter.categoryBits = CollisionFilterCreator.CATEGORY_GAME_OBJECT;
				f.m_filter.maskBits = CollisionFilterCreator.getMaskForCategory(CollisionFilterCreator.CATEGORY_GAME_OBJECT);
				System.out.println("Masks were set for walls; Category: " + CollisionFilterCreator.CATEGORY_GAME_OBJECT);
			}
			System.out.println("Radius : " + f.getShape().getRadius());
			System.out.println("Count: " + count);
			count++;
		}
	}

	/*
	public void setFilterData(Shape shape, byte group) {
		Filter filter = new Filter();
		filter.groupIndex = group;
		Fixture fixture = body.getFixtureList();
		fixture.getNext();
		fixture.getNext();
		fixture.setFilterData(filter);
	}

	 */

	private void makeFlag(){
		PVector centerPosition = new PVector(leftUpperPoint.x+w/2, leftUpperPoint.y+h/2);
		PVector relativeFlagPosition = new PVector(0, getHeight()/4);
		relativeFlagPosition.rotate(PApplet.radians(angleInDegrees -270));
		//relativeFlagPosition.rotate(PApplet.radians(270-angle));
		centerPosition.add(relativeFlagPosition);
		if (angleInDegrees == 270 || angleInDegrees == 90) flag = new Flag(centerPosition, w-2*WALL_THICKNESS, h/2, Flag.CLEAR_DEAD_FLOWER_ELEMENTS_IN_PIPE);
		else if (angleInDegrees == 180 || angleInDegrees == 0 ||  angleInDegrees == 360) flag = new Flag(centerPosition,  h/2, w-2*WALL_THICKNESS, Flag.CLEAR_DEAD_FLOWER_ELEMENTS_IN_PIPE);
		else System.out.println("This round pipe has not flag");
		objectsClearingZone = new ObjectsClearingZone(flag, ObjectsClearingZone.DELETE_CORPSES);
	}



	/*
	private void makeFlower(byte behaviour){
		if (withFlower){
			flower = new Flower(getAbsolutePosition(), behaviour, (int)(w-2*WALL_THICKNESS), (int)h, angle);
			loadFlowerImageData(flower);
		}
	}*/

	private void makePlantGraphic(byte behaviour, Tileset tilesetForJaw, Tileset tilesetForRod){
		if (withFlower){
			//plantController = new PlantController(this, new Vec2(getAbsolutePosition().x, getAbsolutePosition().y), (int)200, behaviour, (int)(w-2*WALL_THICKNESS), angle);

			loadPlantImageData(plantController, tilesetForJaw, tilesetForRod);

		}
	}

	private void makePlant(byte behaviour, int flowerLife, int flowerDimention){
		if (withFlower){
			plantController = new PlantController(this, new Vec2(getPixelPosition().x, getPixelPosition().y), flowerLife, behaviour, (int)(w-2*WALL_THICKNESS), angleInDegrees, flowerDimention);

			//loadPlantImageData(plantController, tilesetForJaw, tilesetForRod);

		}
	}

	public void loadFlowerImageData(Plant flower){
		System.out.println("Not implemented load flower");
		/*flower.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerBody.png"), Plant.ROD_PART, (int)1, (int)1, (int)31, (int)31, (int) flower.getRodLength(), (int)flower.getRodLength());
		flower.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws.png"), Plant.LEFT_JAW, (int)0, (int)0, (int)26, (int)25, (int) flower.getRodDiameter(), (int) flower.getRodDiameter());
		flower.loadImageData(Program.getAbsolutePathToAssetsFolder("FlowerJaws.png"), Plant.RIGHT_JAW, (int)0, (int)0, (int)26, (int)25, (int) flower.getRodDiameter(), (int) flower.getRodDiameter());
	*/}

	public void loadPlantImageData(PlantController plantController, Tileset tilesetForJaw, Tileset tilesetForRod){
		plantController.loadNormalGraphic();
		plantController.loadSprites(tilesetForJaw, tilesetForRod);
		System.out.println("Graphic for plant is loaded");
	}

	@Override
	public void draw(GameCamera gameCamera) {
		/*
		if (withFlower ) {
			plantController.draw(gameCamera);
		}*/
		Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
		if (isVisibleOnScreen(gameCamera, pos)) {
			if (Program.debug) {
				if (withFlower) {
					plantController.draw(gameCamera);
				}
				//Programm.objectsFrame.beginDraw();
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				//Game2D.engine.translate(x-gameCamera.getActualPosition().x+Game2D.engine.width/2, y-gameCamera.getActualPosition().y+Game2D.engine.height/2);
				Program.objectsFrame.translate(pos.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, pos.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
				Program.objectsFrame.rotate(body.getAngle());
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.stroke(20, 240, 20);
				//Game2D.objectsFrame.strokeWeight(4);
				Program.objectsFrame.rectMode(PConstants.CENTER);
				Program.objectsFrame.noFill();
				Program.objectsFrame.strokeWeight(4f);
				Program.objectsFrame.rect(-w / 2 + WALL_THICKNESS / 2, 0, WALL_THICKNESS, h);    //left wall
				Program.objectsFrame.rect(w / 2 - WALL_THICKNESS / 2, 0, WALL_THICKNESS, h);
				Program.objectsFrame.stroke(120, 40, 120);
				Program.objectsFrame.rect(0, -(h / 2) + WALL_THICKNESS / 2, topWidthPixelValue, topHeightPixelValue);
				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
				//Programm.objectsFrame.endDraw();
				if (flag != null && Program.debug) {
					flag.draw(gameCamera);
				}
				//System.out.println("Draw debug pipe conture");
			}
			if (sprite != null) sprite.draw(gameCamera, getPixelPosition(), -body.getAngle());
		}
	}

	private void updateClearingInPipes(ArrayList<RoundPipe> roundPipes){
		/*
		for (RoundPipe roundPipe : roundPipes){
			Flag clearingZone = roundPipe.getObjectClearingZone().;
			for (RoundPipe anotherRoundPipe : roundPipes){
				if (anotherRoundPipe.hasFlower()){

					if (!anotherRoundPipe.flower.isAlive()){
						if (anotherRoundPipe.flower.body != null && anotherRoundPipe.flower.body.isActive()){
							if (clearingZone.inZone(PhysicGameWorld.controller.getBodyPixelCoord(anotherRoundPipe.flower.body))){
								anotherRoundPipe.flower.body.setActive(false);
								PhysicGameWorld.controller.world.destroyBody(anotherRoundPipe.flower.body);
								//anotherRoundPipe.flower.body = null;
								System.out.println("Body was deleted from the pipe class");
							}
						}
					}
				}
			}

		}

		 */
	}

	public void update(GameRound gameRound) {

		if (withFlower) {
			plantController.update(gameRound);
			/*
			flower.update(gameRound);
			updateClearingInPipes(gameRound.getRoundPipes());
			*/
		}

	}

	public PlantController getPlantController(){
		return plantController;
	}

	public void deletePlantController(){
		plantController = null;
		withFlower = false;
	}


	public int getAngle() {
		System.out.println("Angle of pipe: " + angleInDegrees);
		return angleInDegrees;
	}

	/*
	public getPipeAngle(){
		return angle;
	}*/

	public boolean hasFlower(){
		return withFlower;
	}

	public PVector getLeftUpperCorner(){
		return new PVector(leftUpperPoint.x, leftUpperPoint.y);
	}

	public String getClassName(){
		return CLASS_NAME;
	}

	@Override
	public String getStringData(){
		GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
		saveMaster.createRoundPipe();
		System.out.println("Data string for round pipe: " +saveMaster.getDataString());
		return saveMaster.getDataString();
	}

	public byte getFlowerBehaviour() {
		return flowerBehaviour;
	}

	@Override
	public String getObjectToDisplayName() {
		return objectToDisplayName;
	}

	public ObjectsClearingZone getObjectClearingZone(){
		return objectsClearingZone;
	}


}

