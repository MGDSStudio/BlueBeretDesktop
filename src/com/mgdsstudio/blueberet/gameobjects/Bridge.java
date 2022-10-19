package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.ISelectable;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

import java.util.ArrayList;

public class Bridge implements ISelectable {
	public final static String CLASS_NAME = "Bridge";

	private boolean active = true;
	public final static float GAP_WIDTH_BETWEEN_ELEMENTS = 5;
	private final static int graphicWidthAdditionalValue = (int)GAP_WIDTH_BETWEEN_ELEMENTS;
	private PVector leftUpperCorner, rightLowerCorner;
	private Flag flag;
	private ArrayList<RoundElement> segments = new ArrayList<RoundElement>();
	private int singleElementGraphicWidth;
	
	public Bridge(GameRound gameRound, PVector leftUpperCorner, PVector rightLowerCorner, int segmentsAlongX, Flag flag){
		this.leftUpperCorner = leftUpperCorner;
		this.rightLowerCorner = rightLowerCorner;
		final float width = Program.engine.abs(leftUpperCorner.x-rightLowerCorner.x)/segmentsAlongX;
		final float height = Program.engine.abs(leftUpperCorner.y-rightLowerCorner.y);
		singleElementGraphicWidth = (int) width;
		this.flag = flag;
		for (int i = 0; i < segmentsAlongX; i++) {
			Vec2 center = new Vec2(leftUpperCorner.x+i*width+ GAP_WIDTH_BETWEEN_ELEMENTS /2+width/2, leftUpperCorner.y+height/2);
			RoundElement roundElement = new RoundBox(center, 0, width, height, GameObject.IMMORTALY_LIFE, false, BodyType.STATIC);
			segments.add(roundElement);
			gameRound.roundElements.add(segments.get(i));
		}
	}

	public Bridge(GameRound gameRound, GameObjectDataForStoreInEditor objectData){
		this.leftUpperCorner = objectData.getLeftUpperCorner();
		System.out.println("Bridge must test. Left upper corner can be on the another side");
		//this.leftUpperCorner = new PVector();
		//this.leftUpperCorner.x = objectData.getPosition().x-objectData.getWidth()/2f;
		//this.leftUpperCorner.y = objectData.getPosition().y-objectData.getHeight()/2f;
		this.rightLowerCorner = new PVector();
		rightLowerCorner.x = leftUpperCorner.x+objectData.getWidth();
		rightLowerCorner.y = leftUpperCorner.y+objectData.getHeight();
		int segmentsAlongX = objectData.getSegmentsNumber();
		final float width = Program.engine.floor(leftUpperCorner.x-rightLowerCorner.x)/segmentsAlongX;
		singleElementGraphicWidth = (int) width;
		final float height = Program.engine.abs(leftUpperCorner.y-rightLowerCorner.y);
		this.flag = objectData.getFlag();
		for (int i = 0; i < segmentsAlongX; i++) {
			Vec2 center = new Vec2(leftUpperCorner.x+i*width+ GAP_WIDTH_BETWEEN_ELEMENTS /2+width/2, leftUpperCorner.y+height/2);
			RoundElement roundElement = new RoundBox(center, 0, width, height, GameObject.IMMORTALY_LIFE, false, BodyType.STATIC);
			segments.add(roundElement);
			gameRound.roundElements.add(segments.get(i));
		}
		loadImageData(objectData.getPathToGraphic(), objectData.getGraphicLeftX(), objectData.getGraphicUpperY(), objectData.getGraphicRightX(), objectData.getGraphicLowerY(), (int)(width+ GAP_WIDTH_BETWEEN_ELEMENTS), (int)height, false);
		for (RoundElement roundElement : segments){
			roundElement.getSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(objectData.getPathToGraphic()));
		}
	}

	public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea){
		for (byte i = 0; i < segments.size(); i++){
			segments.get(i).loadImageData(path, xLeft, yLeft, xRight, yRight, singleElementGraphicWidth+graphicWidthAdditionalValue*2, height, fillArea);
		}
	}

	public StaticSprite getSprite(){
		return segments.get(0).getSprite();
	}

	public void loadSprites(Tileset tilesetUnderPath) {
		for (byte i = 0; i < segments.size(); i++){
			segments.get(i).loadSprites(tilesetUnderPath);
		}
		System.out.println("Textures for platforms were successfully uploaded");
	}

	public float getSingleElementGraphicWidth(){
		return segments.get(0).getWidth();
	}

	public float getHeight(){
		return segments.get(0).getHeight();
	}

	
	public void update(GameRound gameRound) {
		if (active) {
			if (flag.inZone(gameRound.getPlayer().getPixelPosition())) {
				crush(gameRound);
				active = false;
			}
		}
	}
	
	public void draw(GameCamera gameCamera) {
		if (active) {
			flag.draw(gameCamera);
		}
	}

	public boolean wasCrushed(){
		return !active;
	}

	private void crush(GameRound gameRound) {
		System.out.println("Static objects was: " + gameRound.roundElements.size());
		float torque = 0;
		for (RoundElement segment : segments) {
			torque = Program.engine.random(1, 5);
			if (Program.engine.random(1) < 0.5f) torque *= -1;
			segment.body.setType(BodyType.DYNAMIC);
			segment.body.setAngularVelocity(torque);
		}
		if (segments!= null) segments = null;
		active = false;
	}

	public ArrayList<RoundElement> getSegments() {
		return segments;
	}

	public String getStringData() {
		//Bridge 1:33405,1075,34040,1125,13,34095,925,50,75#Tileset1.png;137x304x152x319x50x50x1
		GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
		saveMaster.createBridge();
		System.out.println("Data string for bridge: " + saveMaster.getDataString());
		return saveMaster.getDataString();
	}

	public PVector getLeftUpperCorner() {
		return leftUpperCorner;
	}

	public PVector getRightLowerCorner() {
		return rightLowerCorner;
	}

	public Flag getFlag() {
		return flag;
	}

	public boolean isObjectPart(SingleGameElement toBeDeleted) {
		for (RoundElement roundElement : segments){
			if (toBeDeleted.equals(roundElement)) return true;
		}
		return false;
	}

	@Override
	public boolean isPointOnElement(float x, float y) {
		for (RoundElement roundElement : segments){
			Vec2 posInWorld = PhysicGameWorld.coordPixelsToWorld(new PVector(x,y));
			if (roundElement.body.getFixtureList().testPoint(posInWorld)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getObjectToDisplayName() {
		return CLASS_NAME;
	}
}
