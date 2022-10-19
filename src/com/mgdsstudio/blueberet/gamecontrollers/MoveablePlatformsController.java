package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.MovablePlatform;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class MoveablePlatformsController implements IDrawable {
	//private StaticSprite sprite;
	public final static String CLASS_NAME = "MoveablePlatformsController";
	private final static String objectToDisplayName = "Platform system";
	public final Flag enter, exit;
	private ArrayList<MovablePlatform> platforms = new ArrayList<MovablePlatform>();
	//Transport directions
	public final static boolean ONE_DIRECTION = true;
	public final static boolean BACK_AND_FORTH = false;
	private boolean repeatability = ONE_DIRECTION;
	
	private float accelerate;		// pixels pro seconds
	private int angle;
	private final float NORMAL_ACCELERATE = 350f/2000f;
	private float velocity;
	private int platformWidth, platformHeight, platformsNumber;
	
	// Movement directions
	final private static boolean TO_ENTER = false;
	final private static boolean TO_EXIT = true;
	private boolean actualMovementDirection = TO_EXIT;
	
	public MoveablePlatformsController(Flag enter, Flag exit, int velocity, int platformWidth, int platformHeight, byte platformsNumber, boolean repeatability) {
		this.enter = enter;
		this.exit = exit;
		this.velocity = velocity;
		this.repeatability = repeatability;
		this.platformWidth = platformWidth;
		this.platformHeight = platformHeight;
		this.platformsNumber = platformsNumber;
		init(velocity, platformsNumber, platformWidth, platformHeight);
	}

	public MoveablePlatformsController(GameObjectDataForStoreInEditor data) {
		this.enter = data.getFirstFlag();
		this.exit = data.getSecondFlag();
		this.velocity = (int)data.getPlatformVelocity();
		this.repeatability = data.getUsingRepeateability();
		this.platformWidth = data.getPlatformWidth();
		this.platformHeight = data.getPlatformThickness();
		System.out.println("Dimensions of the platforms: " + platformWidth + "x " + platformHeight);
		this.platformsNumber = data.getPlatformsNumber();
		init((int)velocity, platformsNumber, platformWidth, platformHeight);

		for (int i = 0; i < platforms.size(); i++){
			boolean fill = false;
			if (data.getFill()!=0) fill = true;
			loadImageData(data.getPathToTexture(), data.getGraphicLeftX(), data.getGraphicUpperY(), data.getGraphicRightX(), data.getGraphicLowerY(), data.getPlatformWidth(), data.getPlatformThickness(), fill);


		}
	}



	private void init(int velocity, int platformsNumber, int platformWidth, int platformHeight){

		PVector wayBetweenFlags = new PVector(exit.getPosition().x, exit.getPosition().y);
		wayBetweenFlags.sub(enter.getPosition());
		this.angle = (int) Program.engine.degrees(wayBetweenFlags.heading());
		angle = (int)GameMechanics.convertAngleFromSimetricalScaleInto0_to_360Scale(angle);
		//System.out.println("Angle: " + angle);
		accelerate = NORMAL_ACCELERATE;
		if (repeatability == BACK_AND_FORTH) {
			//System.out.println("For this repeatabylity direction can be only one platform");
			platformsNumber = 1;
		}
		float distanceBetweenFlags = Program.engine.dist(exit.getPosition().x, exit.getPosition().y, enter.getPosition().x, enter.getPosition().y);
		float platformsStep = distanceBetweenFlags/platformsNumber;
		if (platformsNumber > 1){
			//distanceBetweenFlags = Game2D.engine.dist(exit.getPosition().x, exit.getPosition().y, enter.getPosition().x, enter.getPosition().y);

			int additionalWay = -(int)(platformHeight* Program.engine.sin(Program.engine.radians(angle))+platformWidth* Program.engine.cos(Program.engine.radians(angle)));
			platformsStep = (distanceBetweenFlags+additionalWay)/platformsNumber;
		}
		Vec2 firstPlatformPosition = new Vec2();
		if (platformsNumber == 1) {
			firstPlatformPosition.x = ((enter.getPosition().x+exit.getPosition().x)/2);
			firstPlatformPosition.y = ((enter.getPosition().y+exit.getPosition().y)/2);
			/*
			firstPlatformPosition.x = ((enter.getPosition().x+exit.getPosition().x)/2)-platformWidth/2;
			firstPlatformPosition.y = ((enter.getPosition().y+exit.getPosition().y)/2)-platformHeight/2;
			 */
		}
		else {
			firstPlatformPosition.x = exit.getPosition().x+(platformsStep)* Program.engine.cos(Program.engine.radians(angle))/2;
			firstPlatformPosition.y = exit.getPosition().y+(platformsStep)* Program.engine.sin(Program.engine.radians(angle))/2;
			/*
			firstPlatformPosition.x = exit.getPosition().x+((platformWidth/2)+platformsStep)*Game2D.engine.cos(Game2D.engine.radians(angle))/2;
			firstPlatformPosition.y = exit.getPosition().y+((platformHeight/2)+platformsStep)*Game2D.engine.sin(Game2D.engine.radians(angle))/2;
			*/
		}
		//System.out.println("First platform: " + firstPlatformPosition + "; Enter: " + enter.getPosition() + "; Exit: " + exit.getPosition());
		for (byte i = 0; i < platformsNumber; i++) {
			MovablePlatform moveablePlatform = new MovablePlatform(new Vec2(firstPlatformPosition.x+i*platformsStep* PApplet.cos(PApplet.radians(angle)), firstPlatformPosition.y+i*platformsStep*PApplet.sin(PApplet.radians(angle))), platformWidth, platformHeight);
			platforms.add(moveablePlatform);
			platforms.get(i).setMovementVelocity(velocity, angle);
		}
		//System.out.println("Repeatability: " + repeatability);

	}

	public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea){
		//sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
		for (byte i = 0; i < platforms.size(); i++){
			//System.out.println("Platforms graphic loaded:" + fillArea);
			platforms.get(i).loadImageData(path, xLeft, yLeft, xRight, yRight, width, height, fillArea);
		}

	}


	public StaticSprite getSprite(){
		//if (sprite == null) System.out.println("Don\t have sprite for platforms");
		return platforms.get(0).getSprite();	//every platforms are equals
		//return sprite;
	}

	public void loadSprites(Tileset tilesetUnderPath) {
		for (byte i = 0; i < platforms.size(); i++){
			platforms.get(i).loadSprites(tilesetUnderPath);
		}
		System.out.println("Textures for platforms were successfully uploaded");
	}
	
	public void setAccelerate(float accelerate) {
		this.accelerate = accelerate;
	}
	
	public void update () {		
		for (MovablePlatform platform : platforms) {
			if (repeatability == ONE_DIRECTION) {
				if (enter.inZone(platform.getPixelPosition())) {
					platform.transferTo(exit.getPosition());
				}
			}
			else {
				if (enter.inZone(platform.getPixelPosition())) {
					platform.addAccelerate(angle, getActualAccelerate(true));
				}
				else if (exit.inZone(platform.getPixelPosition())) {
					platform.addAccelerate(angle, getActualAccelerate(false));
					//System.out.println("The platform is in exit zone");
				}
				else {
					if (platform.getAbsoluteVelocity()<velocity) {
						if (actualMovementDirection == TO_EXIT) {
							platform.addAccelerate(angle, getActualAccelerate(true));
							//System.out.println("ADD " + + platform.getAbsoluteVelocity());
							if (platform.getAbsoluteVelocity()>velocity) actualMovementDirection = TO_ENTER;
						}
						else {
							platform.addAccelerate(angle, getActualAccelerate(false));
							//System.out.println("SUB " + + platform.getAbsoluteVelocity() + "; MAX: " + velocity);
							if (platform.getAbsoluteVelocity()>velocity) actualMovementDirection = TO_EXIT;
						}
					}
					/*
					else {
						if (actualMovementDirection == TO_ENTER) actualMovementDirection = TO_EXIT;
						else actualMovementDirection = TO_ENTER;
					}
					*/
				}
			}
			//if (repeatability == BACK_AND_FORTH) System.out.println("platform.getAbsoluteVelocity() : " + platform.getAbsoluteVelocity());
		}
		
	}
	
	private float getActualAccelerate(boolean inEnterZone) {
		if (inEnterZone) return -accelerate;
		else return accelerate;
		
	}

	public ArrayList<MovablePlatform> getPlatforms(){
		return platforms;
	}
	
	private void getActualSpeed() {	}
	
	@Override
	public void draw(GameCamera gameCamera) {
		if (Program.gameStatement == Program.LEVELS_EDITOR) {
			enter.draw(gameCamera);
			exit.draw(gameCamera);
			//System.out.println(exit.getPosition());
		}		
		for (MovablePlatform platform : platforms) {
			platform.draw(gameCamera);
		}
	}

	public String getStringData() {
		GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
		saveMaster.createMoveablePlatrofrmController();
		System.out.println("Data string for platform controller" +saveMaster.getDataString());
		return saveMaster.getDataString();
	}

	public String getClassName(){
		return CLASS_NAME;
	}

	public int getVelocity() {
		return (int)velocity;
	}

	public int getPlatformWidth(){
		//return (int)platforms.get(0).getWidth();
		return platformWidth;
	}

	public int getPlatformThickness(){
		//return (int)platforms.get(0).getHeight();
		return platformHeight;
	}

	public int getPlatformsNumber(){
		return platformsNumber;
		//return (int) platforms.size();
	}



	public boolean getUsingRepeatability() {
		System.out.println("Repeatability: " + repeatability);
		return this.repeatability;
	}
}
