package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.oldlevelseditor.Figure;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class Flag implements ISimpleUpdateable, IDrawable{
	//Sprites
	private static Image spriteIn, spriteOut;
	private static Image graphic;
	private static ImageZoneSimpleData ENTER_IMAGE_POS, EXIT_IMAGE_POS, ARROW_POS, SKULL_IMAGE_POS, FLAG_IMAGE_POS;
	//private static boolean graphicLoaded = false;
	private static String pathToImageIn = "FlagIn.png";
	private static String pathToImageOut = "FlagOut.png";
	private static boolean graphicLoaded = false;
	public final static boolean IN = true;
	public final static boolean OUT = false;
	private boolean imageType = IN;
	private int imageWidth, imageHeight;
	
	private PVector position;
	private float radius;
	private float width, height;			
	
	//Form
	public final static boolean RECT_FORM = true;
	public final static boolean CIRCLE_FORM = false;
	private boolean form = CIRCLE_FORM;
	
	// Statements
	public final static byte IS_NOT_ACTIVATED = 1;
	public final static byte PLAYER_IN = 2;
	public final static byte ANOTHER_PERSON_IN = 3;
	public final static byte PERSONS_IN = 4;
	public final static byte OBJECTS_IN = 5;
	public final static byte MANY_OBJECTS_IN = 6;
	public final static byte SWITCHED_OFF = 0;
	private byte statement = IS_NOT_ACTIVATED;
	
	// Missions
	public final static byte NO_MISSION = 0;
	public final static byte PORTAL_ENTER_ZONE = 1;
	public final static byte PORTAL_EXIT_ZONE = 2;
	public final static byte BRIDGE_CRUSH = 3;
	public final static byte DELETE_OBJECT_FROM_WORLD = 4;
	public final static byte IMMEDIATELY_TRANSPORT_ENTER_ZONE = 5;
	public final static byte IMMEDIATELY_TRANSPORT_EXIT_ZONE = 6;
	public final static byte CLEAR_OBJECTS = 7;
	public final static byte CLEAR_DEAD_FLOWER_ELEMENTS_IN_PIPE = 8;
	public final static byte OBJECTS_APPEARING_ZONE = 9;
	public final static byte BULLET_BILL_ACTIVATING_ZONE = 10;
	public final static byte PLAYER_COMMING_IN_ZONE_TRIGGER = 11;
	public final static byte PLAYER_LEAVING_ZONE_TRIGGER = 12;
	public final static byte PERSON_COMMING_IN_ZONE_TRIGGER = 13;
	public final static byte PERSONS_LEAVING_ZONE_TRIGGER = 14;	// when all persons leaving the area the flag is activated
	public final static byte FULL_CLEARING_ZONE_TRIGGER = 15;	// when all crushable objects leaving the area the flag is activated
	public final static byte MOVEMENT_AREA_ZONE = 16;	// when all crushable objects leaving the area the flag is activated


	public final static byte MESSAGE_ADDING_ZONE = 17;	// when all crushable objects leaving the area the flag is activated
	public final static byte CAMERA_FIXATION_ZONE = 17;
	public final static byte PATROL_AREA = 18;	// when all crushable objects leaving the area the flag is activated
	public final static byte SECRET_AREA = 19;	// when all crushable objects leaving the area the flag is activated
	public final static byte JUMP_AREA = 20;	// when all crushable objects leaving the area the flag is activated
	public final static byte END_LEVEL = 21;	// when all crushable objects leaving the area the flag is activated
	// public final static byte

	private byte mission = PORTAL_ENTER_ZONE;
	
	//Portal after transfer moving direction or another directions (if it's using with a portal)
	public static final byte NO_DIRECTION = 0;
	public static final byte TO_RIGHT = 1;
	public static final byte TO_DOWN = 2;
	public static final byte TO_LEFT = 3;
	public static final byte TO_UP = 4;
	private byte direction = NO_DIRECTION;
	private float spriteRotatingAngleInRadians;

	private int color = -1;


	public Flag(PVector position, float radius, byte mission) {
		this.position = new PVector(position.x, position.y);
		this.radius = radius;		
		form = CIRCLE_FORM;
		if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR) loadImage(mission);
		this.mission = mission;
	}

	public Flag(Figure figure) {
		this.position = new PVector(figure.getCenter().x, figure.getCenter().y);
		this.width = figure.getWidth();
		this.height = figure.getHeight();
		form = RECT_FORM;
		if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR) loadImage(mission);
		this.mission = NO_MISSION;
	}
	
	public Flag(PVector position, float width, float height, byte mission) {
		//this.position = new PVector(position.x, position.y);
		this.position = position;
		this.width = width;
		this.height = height;
		form = RECT_FORM;
		if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR) loadImage(mission);
		this.mission = mission;
		spriteRotatingAngleInRadians = 0;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight(){
		return height;
	}

	public Flag(PVector position, float width, float height, byte mission, byte direction) {
		//this.position = new PVector(position.x, position.y);
		this.position = position;
		this.width = width;
		this.height = height;
		form = RECT_FORM;		
		if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR) loadImage(mission);
		this.mission = mission;
		this.direction = direction;
		if (direction == TO_UP) spriteRotatingAngleInRadians = PConstants.PI;
		else if (direction == TO_LEFT) spriteRotatingAngleInRadians = PConstants.HALF_PI;
		else if (direction == TO_RIGHT) spriteRotatingAngleInRadians = -PConstants.HALF_PI;
		else spriteRotatingAngleInRadians = 0;
	}
	
	private void loadImage(byte mission) {
		if (!graphicLoaded) {
			String prefix = Program.getRelativePathToAssetsFolder();
			try {
				spriteIn = new Image(prefix+pathToImageIn);
				spriteOut = new Image(prefix+pathToImageOut);
				graphic = new Image(Program.getAbsolutePathToAssetsFolder("Flags.png"));
				graphicLoaded = true;
				ENTER_IMAGE_POS = new ImageZoneSimpleData(193,1, 361, 113);
				EXIT_IMAGE_POS = new ImageZoneSimpleData(1,1, 169, 113);
				ARROW_POS  = new ImageZoneSimpleData(259, 129, 339, 191);
				SKULL_IMAGE_POS  = new ImageZoneSimpleData(13, 123, 115, 260);
				FLAG_IMAGE_POS = new ImageZoneSimpleData(125, 127, 238, 251);
				//spriteOut.getImage().resize((int) width, (int) height);
				System.out.println("Graphic loaded for flags");
			}
			catch (Exception e) {
				System.out.println("Can not load graphic for flags");
				e.printStackTrace();
			}
		}
		calculateImageDimensions();
		if (mission == PORTAL_ENTER_ZONE) imageType = IN;
		else if (mission == PORTAL_EXIT_ZONE) imageType = OUT;		
		else imageType = IN;

	}

	private void calculateImageDimensions(){
		boolean smallestWidth = false;
		if (height > width) smallestWidth = true;
		float flagWidth = graphic.getImage().width;
		float flagHeight = graphic.getImage().height;
		/*
		float flagWidth = spriteIn.getImage().width;
		float flagHeight = spriteIn.getImage().height;
		*/
		float dimesionsCoefficient = flagWidth/flagHeight;
		int smallestDimention = (int)height;
		if (smallestWidth){
			if (width< Program.engine.width/12f){
				imageWidth = (int)(width);
				//imageWidth = (int)(width*0.85f);
			}
			else{
				//imageWidth = (int)(Game2D.engine.width/12f);
				imageWidth = (int)(width*0.85f);
			}
			imageHeight = (int)(imageWidth/dimesionsCoefficient);
		}
		else{
			if (height< Program.engine.width/12f){
				imageHeight = (int)(height);
				//imageHeight = (int)(height*0.85f);
			}
			else{
				imageHeight = (int)(height*0.85f);
				//imageHeight = (int)(Game2D.engine.width/12f);
			}
			imageWidth = (int)(imageHeight*dimesionsCoefficient);
		}
		//System.out.println("Width: " + width + "Height:: " + height + "dimesionsCoefficient" + dimesionsCoefficient);
	}

	public byte getMission() {
		return mission;
	}

	public ArrayList<GameObject> isSomeGameObjectInFlagZone(ArrayList<Person> persons){
		ArrayList<GameObject> gameObjectsInZone = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjectsInZone) {
			if (inZone(gameObject.getPixelPosition())) {
				gameObjectsInZone.add(gameObject);
			}			
		}
		if (gameObjectsInZone.size()>0) {
			if (statement != SWITCHED_OFF) {
				if (statement != PERSONS_IN) {
					statement = OBJECTS_IN;
				}
				else statement = MANY_OBJECTS_IN;
			}
		}
		else if (statement != SWITCHED_OFF) {
			statement = IS_NOT_ACTIVATED;
		}
		return gameObjectsInZone;	
		
	}
	
	public ArrayList<Person> isSomebodyInFlagZone(ArrayList<Person> persons) {
		ArrayList<Person> personsInZone = new ArrayList<Person>();
		for (Person person : persons) {
			if (inZone(person.getPixelPosition())) {
				personsInZone.add(person);
			}			
		}
		if (personsInZone.size()>0) {
			if (statement != SWITCHED_OFF) {
				if (statement != OBJECTS_IN) {
					statement = PERSONS_IN;
				}
				else statement = MANY_OBJECTS_IN;	
			}
		}
		else if (statement != SWITCHED_OFF) {
			statement = IS_NOT_ACTIVATED;
		}
		return personsInZone;		
	}

	public boolean inZone(float toTestPositionX, float toTestPositionY) {
		if (form == CIRCLE_FORM) {
			if (Program.engine.dist(toTestPositionX, toTestPositionY, position.x, position.y) <= radius) {
				return true;
			}
			else return false;
		}
		else if (form == RECT_FORM) {
			if (toTestPositionX >= (position.x-width/2) &&
					toTestPositionX <= (position.x+width/2) &&
					toTestPositionY >= (position.y-height/2) &&
					toTestPositionY <= (position.y+height/2)) {
				return true;
			}
			else return false;
		}
		else {
			System.out.println("The form is not known");
			return false;
		}
	}

	public boolean inZone(Vec2 toTestPosition) {
		return inZone(toTestPosition.x, toTestPosition.y);
		/*
		if (form == CIRCLE_FORM) {
			if (Programm.engine.dist(toTestPosition.x, toTestPosition.y, position.x, position.y) <= radius) {
				return true;
			}
			else return false;
		}
		else if (form == RECT_FORM) {
			if (toTestPosition.x >= (position.x-width/2) &&
					toTestPosition.x <= (position.x+width/2) &&
					toTestPosition.y >= (position.y-height/2) &&
					toTestPosition.y <= (position.y+height/2)) {
				return true;
			}
			else return false;
		}
		else {
			System.out.println("The form is not known");
			return false;
		}
		*/
	}
	
	public boolean inZone(PVector toTestPosition) {
		return inZone(toTestPosition.x, toTestPosition.y);
		/*
		if (form == CIRCLE_FORM) {
			if (Programm.engine.dist(toTestPosition.x, toTestPosition.y, position.x, position.y) <= radius) {
				return true;
			}
			else return false;
		}
		else if (form == RECT_FORM) {
			if (toTestPosition.x >= (position.x-width/2) &&		
				toTestPosition.x <= (position.x+width/2) &&
				toTestPosition.y >= (position.y-height/2) &&
				toTestPosition.y <= (position.y+height/2)) {
				return true;
			}
			else return false;
		}
		else {
			System.out.println("The form is not known");
			return false;
		}
		*/
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub		
	}
	
	public void setStatement(byte statement) {
		this.statement = statement;
	}
	
	public byte getStatement() {
		return statement;
	}

	@Override
	public void draw(GameCamera gameCamera) {
			if (Program.debug || Program.gameStatement == Program.LEVELS_EDITOR) {
				Program.objectsFrame.pushMatrix();
				Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
				Program.objectsFrame.pushStyle();
				Program.objectsFrame.translate(position.x - gameCamera.getActualXPositionRelativeToCenter(), position.y - gameCamera.getActualYPositionRelativeToCenter());
				Program.objectsFrame.noFill();
				Program.objectsFrame.rectMode(PConstants.CENTER);
				byte strokeWeight;
				if (color == -1) {
					if (mission == CLEAR_OBJECTS) Program.objectsFrame.stroke(200, 0, 15);
					else if (mission == SECRET_AREA) Program.objectsFrame.stroke(255, 255, 0);
					else if (mission == JUMP_AREA) Program.objectsFrame.stroke(0, 255, 0);
					else Program.objectsFrame.stroke(15, 0, 200);
					strokeWeight = (byte)(Program.engine.width/111f);
					Program.objectsFrame.strokeWeight(strokeWeight);
				}
				else {
					strokeWeight = (byte)(Program.engine.width/46f);
					Program.objectsFrame.strokeWeight(strokeWeight);
					Program.objectsFrame.stroke(color);
				}

				Program.objectsFrame.rect(0, 0, width-strokeWeight, height-strokeWeight);
				Program.objectsFrame.tint(255, 120);

				if (mission == PORTAL_ENTER_ZONE) {
					Program.objectsFrame.image(graphic.getImage(), 0, 0, imageWidth, imageHeight, ENTER_IMAGE_POS.leftX, ENTER_IMAGE_POS.upperY, ENTER_IMAGE_POS.rightX, ENTER_IMAGE_POS.lowerY);
				}
				else if (mission == PORTAL_EXIT_ZONE) {
					Program.objectsFrame.image(graphic.getImage(), 0, 0, imageWidth, imageHeight, EXIT_IMAGE_POS.leftX, EXIT_IMAGE_POS.upperY, EXIT_IMAGE_POS.rightX, EXIT_IMAGE_POS.lowerY);
				}
				else  if (mission == CLEAR_OBJECTS){
					Program.objectsFrame.image(graphic.getImage(), 0, 0, imageWidth, imageHeight, SKULL_IMAGE_POS.leftX, SKULL_IMAGE_POS.upperY, SKULL_IMAGE_POS.rightX, SKULL_IMAGE_POS.lowerY);
				}
				else if (mission == CAMERA_FIXATION_ZONE){
					//Program.objectsFrame.image(graphic.getImage(), 0, 0, imageWidth, imageHeight, SKULL_IMAGE_POS.leftX, SKULL_IMAGE_POS.upperY, SKULL_IMAGE_POS.rightX, SKULL_IMAGE_POS.lowerY);
				}
				else Program.objectsFrame.image(graphic.getImage(), 0, 0, imageWidth, imageHeight, FLAG_IMAGE_POS.leftX, FLAG_IMAGE_POS.upperY, FLAG_IMAGE_POS.rightX, FLAG_IMAGE_POS.lowerY);

				Program.objectsFrame.popStyle();
				Program.objectsFrame.popMatrix();
			}
	}
	
	public PVector getPosition() {
		return position;
	}
	
	public byte getDirection() {
		return direction;
	}

    public void setFrameColor(int color) {
		this.color = color;
    }

    public boolean isPointIn(Vec2 nearestPoint) {
		return GameMechanics.isPointInRect(nearestPoint.x, nearestPoint.y, position.x-width/2, position.y-height/2, width, height);
    }

	public static Flag createFlagByLastValues(int[]values){
		PVector position = new PVector(values[values.length-5], values[values.length-4]);
		int zoneWidth = values[values.length-3];
		int zoneHeight = values[values.length-2];
		int mission = values[values.length-1];
		Flag flag = new Flag(position, zoneWidth, zoneHeight, (byte) mission);
		return flag;
	}
}

