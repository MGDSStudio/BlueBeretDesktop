package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.PConstants;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

//import javax.activation.UnsupportedDataTypeException;

public class GameCamera extends AbstractCamera {

    public static boolean cameraInGameConcentrationOnCrosshair = true;

	public final static boolean CAMERA_IN_GAME = true;
	public final static boolean CAMERA_IN_EDITOR = false;

	private boolean cameraMode;

	private float movementAccelerate = (float)500/100f;	// is soon overridden
	private float movementSpeed = 0f;
	private float targetMovementSpeed = 0f;
	private float maxMovementSpeed = (float)500/4.3f;	// is soon overridden
	public float scallingAccelerate = 0.0004f;	//0.0005f;
	private float scallingConstantSpeed = 0.003f;

	private final PVector actualPosRelativeToCenter;


	private float actualMaxScale = maxScale;
	private final float criticalDistanceToMakeJump = Program.objectsFrame.width*4.5f;




	private boolean constantScale = false;
	public float targetScale = scale;
	private boolean externalZoomChanging = false;
	private float relativeCameraShiftAlongX = (Program.engine.width/3)/scale;
	public float relativeCameraShiftAlongY = -(Program.engine.width/13)/scale;	// Distance between playerY and center line of the screen
	private final boolean CAMERA_GOES_TO_USER = true;
	private final boolean CAMERA_GOES_FROM_USER = false;
	//Braking
	private float brakingWayDistance = (float) Program.engine.width/8;	//(float)Game2D.engine.width/41;
	final float maximalDifferenceToCatchCamera = Program.engine.width/15;	// to catch the camera target/ It was Game2D.engine.width/30
	private int brakingTime = 2000;
	boolean braking = false;
	private long brakingMoment;
	PVector brakingPosition;

	final float minDeltaPosition = 500f/1000f;
	final float maxDeltaPosition = 500/5f;

	//Display sides

	public final static int VISIBLE_Y_AREA_HEIGHT = (int) (Program.engine.height- UpperPanel.HEIGHT- PlayerControl.SCREEN_CONTROL_AREA_HEIGHT);
	public static final int SCREEN_Y_CENTER = UpperPanel.HEIGHT+VISIBLE_Y_AREA_HEIGHT/2;
	private float ADDITIONAL_Y_DISTANCE = -((Program.engine.height/2f)-UpperPanel.HEIGHT-VISIBLE_Y_AREA_HEIGHT/2f);	// -Programm.engine.height/5f	//Maybe I need to delete (Program.engine.height-Program.objectsFrame.height)
	private Vec2 actualPosForBraking;
	public static boolean cameraBodyInAnotherWorld = true;
	//private float visibleZoneWidth = Program.objectsFrame.width/Program.OBJECT_FRAME_SCALE, visibleZoneHeight = Program.objectsFrame.height/Program.OBJECT_FRAME_SCALE;
	private final float additionalVisibleZoneSideCoefficient = 1.25f;
	private boolean mustBeRelaxedOnNextFrame;
	private boolean withBlackHudPanels = true;
	private int debugDistance = 0;
	private int debugDistance2 = 0;
	private final float valueToBeSubrtractOne;// = ADDITIONAL_Y_DISTANCE+((int)(70f*Program.SIDES_RELATIONSHIP/Program.SIDES_RELATIONSHIP_FOR_PIXEL_4A));

	public GameCamera(PVector position, boolean cameraMode) {
		scale =  maxScale;
		visibleZoneWidth = Program.objectsFrame.width/Program.OBJECT_FRAME_SCALE;
		visibleZoneHeight = Program.objectsFrame.height/Program.OBJECT_FRAME_SCALE;
		initScales();
		if (cameraWorldController == null) {
			if (cameraBodyInAnotherWorld) {
				cameraWorldController = new Box2DProcessing(Program.engine);
				cameraWorldController.createWorld();
				cameraWorldController.setGravity(0, 0);
				cameraWorldController.setScaleFactor(30);
			}
			else cameraWorldController = PhysicGameWorld.controller;
		}
		if (cameraMode == CAMERA_IN_GAME) {
			relativeCameraShiftAlongX = 0;
			relativeCameraShiftAlongY = -(Program.objectsFrame.width / 13f) / scale;
			//relativeCameraShiftAlongY = -(Program.objectsFrame.width / 13f) / scale;
			if (Program.debug) System.out.println("Camera created as in game");
		}
		else {
			relativeCameraShiftAlongX = 0;
			relativeCameraShiftAlongY = 0;
			System.out.println("Something is strange in the next function");
			//relativeCameraShiftAlongY = -Program.engine.height/2;
			//relativeCameraShiftAlongY = -(Editor2D.zoneHeight/2)*maxScale-Program.engine.height/2;
			relativeCameraShiftAlongY = (Editor2D.zoneHeight/2)*scale;

			if (Program.debug) System.out.println("Camera created as in editor");
		}
		if (cameraMode == CAMERA_IN_EDITOR) ADDITIONAL_Y_DISTANCE = 0;
		if (cameraMode == CAMERA_IN_EDITOR && lastCameraPositionInEditor!= null){
			loadPreviousCameraPosInEditor();
		}
		else {
			if (cameraMode == CAMERA_IN_EDITOR) actualPosition = new PVector(position.x, position.y+relativeCameraShiftAlongY);
			else actualPosition = new PVector(position.x-50, position.y);
			goalPosition = new PVector(position.x, position.y);
		}
		scale = (maxScale+minScale)/2;
		targetScale = scale;
		this.cameraMode = cameraMode;
		if (springCameraMovement) {
			makeBody();
			cameraSpring = new CameraSpring(actualPosition.x, actualPosition.y, actualCameraPositionBody, cameraWorldController);
			actualPosForBraking = new Vec2(actualPosition.x, actualPosition.y);
		}
		System.out.println("Camera max scale: " + maxScale + "; Min: " + minScale);
		if (cameraBodyInAnotherWorld) {}
		else setFilterDataForCategory(CollisionFilterCreator.CATEGORY_CAMERA);
		actualPosRelativeToCenter = new PVector(0, 0);
		updateActualRelativeToCenterPos();
		System.out.println("Camera start place " + (int)actualPosition.x + "; " + (int)actualPosition.y);
		float heightsRelationship = (float)Program.PIXEL_4A_HEIGHT/(float)Program.engine.height;
		float heightRelativeValue = heightsRelationship*50f;
		float variableDist = (int)((115*(Program.SIDES_RELATIONSHIP/Program.SIDES_RELATIONSHIP_FOR_PIXEL_4A))-heightRelativeValue);
		//variableDist
		valueToBeSubrtractOne = ADDITIONAL_Y_DISTANCE+variableDist;
		cameraCenterPositionInEditor = new Vec2(actualPosition.x, actualPosition.y);
		//System.out.println("Distance: " + ADDITIONAL_Y_DISTANCE + "; Variable distance: " +variableDist + " all dist: "  + valueToBeSubrtractOne + heightsRelationship + "Height relative: " + heightRelativeValue + " by heights relationships: " + heightsRelationship);
	}

	public float getDisplayBoard(byte side) {
		float coordinate = 0;
		final float additionalAreaCoef = 1.2f;
		int visibleAreaHalfWidth = (int) (additionalAreaCoef*((Program.OBJECT_FRAME_SCALE*Program.engine.width/(2*scale*Program.OBJECT_FRAME_SCALE))));
		int visibleAreaHalfHeight = (int) (additionalAreaCoef*((Program.OBJECT_FRAME_SCALE*Program.engine.height/(2*scale*Program.OBJECT_FRAME_SCALE))));
		if (side == RIGHT_SIDE) coordinate = actualPosition.x+ visibleAreaHalfWidth;
		else if (side == LEFT_SIDE) coordinate = actualPosition.x- visibleAreaHalfWidth;
		else if (side == UPPER_SIDE) coordinate = actualPosition.y- visibleAreaHalfHeight;
		else if (side == LOWER_SIDE) coordinate = actualPosition.y+ visibleAreaHalfHeight;
		else System.out.println("There are no side");
		return coordinate;
	}

	protected void updateActualRelativeToCenterPos() {
		actualPosRelativeToCenter.x = actualPosition.x- Program.objectsFrame.width;
		actualPosRelativeToCenter.y =  actualPosition.y-(Program.objectsFrame.height)/(2f*Program.OBJECT_FRAME_SCALE)-(valueToBeSubrtractOne);
	}

	@Override
	public void changeScale(byte scallingDirection) {
		if (scallingDirection == Editor2D.SCALLING_DOWN) {
			if (scale>minScaleInEditorMode)	scale-=Editor2D.SCALING_SPEED;
		}
		else if (scallingDirection == Editor2D.SCALLING_UP) {
			if (scale<maxScaleInEditorMode) scale+=Editor2D.SCALING_SPEED;
		}
	}



	public float getActualXPositionRelativeToCenter() {
		return actualPosRelativeToCenter.x;
	}

	public float getActualYPositionRelativeToCenter() {
		return actualPosRelativeToCenter.y;
	}



	public void updateForPosition(PVector targetObjectPosition, float targetScale) {
		if (mustBeRelaxedOnNextFrame){
			mustBeRelaxedOnNextFrame = false;
		}
		if (springCameraMovement) {
			goalPosition = targetObjectPosition;
			this.targetScale = targetScale;
			moveCameraToSpring();
			zooming(targetScale);
		}
		updateActualRelativeToCenterPos();
		cameraWorldController.step(0.0166f, 3, 5);
		updateVisibleZoneData();
	}

	private void updateVisibleZoneData() {
		visibleZoneWidth = getDisplayBoard(RIGHT_SIDE)-getDisplayBoard(LEFT_SIDE);
		if (withBlackHudPanels){
			visibleZoneHeight = Program.engine.height/scale- 0.35f*UpperPanel.HEIGHT-0.35f*PlayerControl.SCREEN_CONTROL_AREA_HEIGHT;
		}
		else visibleZoneHeight = Program.engine.height/scale;
	}


	protected void updateCameraInEditor(){

	}



	public void updateForCharacter(Person person, boolean cameraAim) {
		if (mustBeRelaxedOnNextFrame){
			mustBeRelaxedOnNextFrame = false;
		}
		if (cameraMode == CAMERA_IN_GAME) updateCameraInGame(person, cameraAim);
		else updateCameraInEditor();
		updateActualRelativeToCenterPos();
		try {
			if (cameraBodyInAnotherWorld) cameraWorldController.step(0.0166f, 5, 8);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		updateVisibleZoneData();

	}

	private void updateCameraInGame(Person person, boolean cameraAim){
		if (person.getClass() == Soldier.class) {
			updateMovingStatement();

			setNewGoalPositionByNewMethod(person, cameraAim);
			if (springCameraMovement) {
				moveCameraToSpring();
			}
			if (braking == false) {
				if (!springCameraMovement) {
					if (mustGoalPositionBeUpdated()) {
						if (!springCameraMovement) moveCameraToGoalPoint();
					} else {
						if (movementSpeed > 0) movementSpeed = 0;
					}
				}
			}
			else {

			}
		}
		if (person.role == Person.PLAYER) {
			if (!constantScale) updateScaleNew(person);
		}
		updateRelativeCameraShiftAlongX();
	}


	
	private void updateMovingStatement() {
		final int distance = (int) Program.engine.dist(actualPosition.x,actualPosition.y, goalPosition.x, goalPosition.y);
		if (Math.abs(distance) < brakingWayDistance) {
			if (braking != true) {
				braking = true;	
				movementSpeed = 0;
				brakingMoment = Program.engine.millis();
				if (brakingPosition == null) brakingPosition = new PVector(actualPosition.x, actualPosition.y);
				else {
					brakingPosition.x = actualPosition.x;
					brakingPosition.y = actualPosition.y;
				}
			}
		}
		else {
			if (braking == true) {
				braking = false;
				brakingMoment = 0;				
			}
		}
	}

	private void updateRelativeCameraShiftAlongX() {		
		if (cameraInGameConcentrationOnCrosshair) {
			relativeCameraShiftAlongX = ((Program.engine.width / 3f) / scale); //must be deleted
		}
		else{
			relativeCameraShiftAlongX = 0;
		}
	}

	private void changeScaleAccordDirection(boolean changingDirection){
		if (changingDirection == CAMERA_GOES_TO_USER){
			scale-=scallingConstantSpeed;
		}
		else scale+=scallingConstantSpeed;
	}

	private void updateScaleNew(Person person) {
		if (scale>actualMaxScale) scale = actualMaxScale;
		else {
			if (scale<minScale) scale = minScale;
			else{
				if (person.getStatement() == Person.IN_AIR){
					changeScaleAccordDirection(CAMERA_GOES_FROM_USER);
					if (scale<minScale) scale = minScale;
				}
				else {
					if (person.isPersonRunning()){
						changeScaleAccordDirection(CAMERA_GOES_FROM_USER);
						actualMaxScale = maxScale;
						if (scale>actualMaxScale) scale = actualMaxScale;
					}
					else {
						if (person.isPersonStaying()) {
							changeScaleAccordDirection(CAMERA_GOES_TO_USER);
							if (scale<minScale) scale = minScale;
						}
						else {    //He is  going
							//System.out.println("GOING");
							if (scale > (maxScale - diferenceBetweenMaxAndMinScale / 2f)) {
								changeScaleAccordDirection(CAMERA_GOES_TO_USER);
							}
							else {
								actualMaxScale = maxScale - diferenceBetweenMaxAndMinScale / 2f;
								changeScaleAccordDirection(CAMERA_GOES_FROM_USER);
							}
							if (scale < minScale) scale = minScale;
							else if (scale > actualMaxScale) scale = actualMaxScale;
						}
					}
				}
			}
		}
	}



	private void moveCameraToSpring() {
		if (mustCameraMakeJumpToGoalObject()){
			actualPosition.x = goalPosition.x;
			actualPosition.y = goalPosition.y;
			actualPosForBraking.x = actualPosition.x;
			actualPosForBraking.y = actualPosition.y;
				try {
					actualCameraPositionBody.setTransform(cameraWorldController.coordPixelsToWorld(actualPosForBraking), actualCameraPositionBody.getAngle());
				}
				catch (AssertionError error) {
					makeCameraMovementWithoutSpring();
					System.out.println("Spring is not more used. Camera is moving without spring; Error: ");
					System.out.println(error);
				}
		}
		else {
			actualPosition = cameraSpring.getActualPosition(goalPosition.x, goalPosition.y);
		}
	}

	private boolean mustCameraMakeJumpToGoalObject(){
		if (Program.engine.dist(actualPosition.x, actualPosition.y, goalPosition.x,goalPosition.y) > (criticalDistanceToMakeJump/scale)){
			return true;
		}
		else return false;
	}
	
	private void moveCameraToGoalPoint() {
		if (Program.deltaTime > 14 && Program.deltaTime < 100) {
			maxMovementSpeed = (float) Program.deltaTime/0.36f;
			movementAccelerate = (float) Program.deltaTime/50f;
		}
		if (movementSpeed > maxMovementSpeed || movementSpeed < (-maxMovementSpeed)) targetMovementSpeed = maxMovementSpeed;
		else {
			if (movementSpeed > 0) {
				targetMovementSpeed = Program.engine.map(movementSpeed, 0, maxMovementSpeed, minDeltaPosition, maxDeltaPosition);
			}
			else if (movementSpeed < 0) targetMovementSpeed = Program.engine.map(movementSpeed, -maxMovementSpeed, 0, -maxDeltaPosition, -minDeltaPosition);
			else targetMovementSpeed+=movementAccelerate;
		}		
			
		if (targetMovementSpeed > movementSpeed) {
			if ((Math.abs(movementSpeed-targetMovementSpeed)>maxMovementSpeed)) movementSpeed = maxMovementSpeed;
			else movementSpeed+=movementAccelerate;				
			PVector vectorToGoalPlace = PVector.sub(goalPosition, actualPosition);		
			vectorToGoalPlace.normalize();
			actualPosition.add(vectorToGoalPlace.mult(movementSpeed));				
		}
		else if (targetMovementSpeed < movementSpeed) {	
			if (movementSpeed<(-maxMovementSpeed)) movementSpeed = -maxMovementSpeed;	
			else movementSpeed-=movementAccelerate;		
			PVector vectorToGoalPlace = PVector.sub(goalPosition, actualPosition);		
			vectorToGoalPlace.normalize();
			actualPosition.add(vectorToGoalPlace.mult(movementSpeed));
		}
	}

	public Vec2 getOnMapPositionForPointForEditor(Vec2 point){
		return new Vec2(actualPosition.x-Editor2D.leftUpperCorner.x-Editor2D.zoneWidth/2+((point.x-(Program.engine.width)/2)/scale), actualPosition.y-Editor2D.zoneHeight+((point.y-(Program.engine.height)/2)/scale));
	}

	public float getOnScreenPosX(float x){
		float pos = (x - getActualXPositionRelativeToCenter())*Program.OBJECT_FRAME_SCALE;
		return pos;
	}

	public float getOnScreenPosY(float y){
		float pos = (y - getActualYPositionRelativeToCenter())*Program.OBJECT_FRAME_SCALE;
		return pos;
	}


	private float getDistanceBetweenActualAndGoalPositions() {
		final float deltaX = Math.abs(goalPosition.x-actualPosition.x);
		final float deltaY = Math.abs(goalPosition.y-actualPosition.y);		
		final float distance = (float)Math.sqrt((deltaX*deltaX) + (deltaY+deltaY));
		return distance;
	}

	private boolean mustGoalPositionBeUpdated() {			
		if ((getDistanceBetweenActualAndGoalPositions()) > maximalDifferenceToCatchCamera) {
			return true;
		}
		else {
			movementSpeed = 0;
			actualPosition.x = goalPosition.x;
			actualPosition.y = goalPosition.y;			
			return false;			 
		}
	}
	
	public void setNewActualPosition(PVector pos) {
		try {
			if (springCameraMovement) actualCameraPositionBody.setTransform(cameraWorldController.coordPixelsToWorld(pos), 0f);
			else actualPosition = pos;
		}
		catch (Exception e){
			System.out.println(e);
			if (pos==null) {
				actualPosition = new PVector(0,0);
			}
			else actualPosition = new PVector(0,0);
		}
	}

	private void setNewGoalPositionByNewMethod(Person person, boolean cameraAim) {
		if (cameraAim == CAMERA_TO_OBJECT) {
			goalPosition.x = person.getPixelPosition().x-Program.objectsFrame.width/2;
			goalPosition.y = person.getPixelPosition().y-Program.objectsFrame.height/2+debugDistance+Program.engine.width/2;
		}
		else{
			PVector objectPosition = person.getPixelPosition();
			objectPosition.y+=relativeCameraShiftAlongY;
			final byte deadAngleZone = 10;
			float wiewAngle = person.getWeaponAngle();
			if (wiewAngle>360) wiewAngle-=360;
			if (wiewAngle<0) wiewAngle=360+wiewAngle;
			//
			float xDistance = 0;
			if (wiewAngle>90 && wiewAngle<270) xDistance = -relativeCameraShiftAlongX;
			else xDistance = relativeCameraShiftAlongX;
			wiewAngle = (float) Math.toRadians(wiewAngle);
			float yDistance;
			final float devider = 5f;
			final float angleInDegrees = (float) Math.toDegrees(wiewAngle);
			if (angleInDegrees<(90-deadAngleZone) && angleInDegrees>= 0) {
				yDistance = (float) Math.sin(wiewAngle/devider)*relativeCameraShiftAlongX;
				goalPosition.y = objectPosition.y + yDistance;
			}
			else if (angleInDegrees<180 && angleInDegrees>= (90+deadAngleZone)) {
				yDistance = (float) Math.sin(PConstants.PI-((PConstants.PI-wiewAngle)/devider))*relativeCameraShiftAlongX;
				goalPosition.y = objectPosition.y + yDistance;
			}
			else if (angleInDegrees<(270-deadAngleZone) && angleInDegrees>= 180) {
				yDistance = (float) Math.sin(PConstants.PI+((wiewAngle-PConstants.PI)/devider))*relativeCameraShiftAlongX;
				goalPosition.y = objectPosition.y + yDistance;
			}
			else if (angleInDegrees<(360) && angleInDegrees>= (270+deadAngleZone)) {
				yDistance = (float) Math.sin(PConstants.TWO_PI-((PConstants.TWO_PI-wiewAngle)/devider))*relativeCameraShiftAlongX;
				goalPosition.y = objectPosition.y + yDistance;
			}
			goalPosition.x = objectPosition.x + xDistance;
		}
	}
	
	public void setScale(float newlScale) {
		scale = newlScale;
	}

	public void setGoalPosition(Vec2 goalPosition){
		this.goalPosition = new PVector(goalPosition.x, goalPosition.y);
	}
	public void relaxCameraSpring() {
			if (!cameraSpring.isRelaxed()) {
					if (!cameraWorldController.world.isLocked()) {
						if (Program.OS == Program.DESKTOP || GameCameraController.WITH_CAMERA_RELAXING){
							try {
								cameraSpring.mouseJoint.setDampingRatio(cameraSpring.mouseJoint.getDampingRatio() / 3f);
								cameraSpring.mouseJoint.setFrequency(cameraSpring.mouseJoint.getFrequency() / 2f);
								cameraSpring.mouseJoint.setMaxForce(cameraSpring.mouseJoint.getMaxForce() / 2f);
								System.out.println("Camera is relaxed; Data: " + cameraSpring.mouseJoint.getDampingRatio() + "; Freq: " + cameraSpring.mouseJoint.getFrequency() + "; Max force: " + cameraSpring.mouseJoint.getMaxForce());
								cameraSpring.relaxed = true;
							} catch (AssertionError e) {
								PhysicGameWorld.assertionErrorAppears = true;
								cameraSpring.relaxed = true;
								System.out.println("Can not relax camera; Assertion error");

							}
						}
						else {

						}
					} else System.out.println("World is locked");
			}
	}

	public void setNewPosition(Vec2 newPosition){
		actualPosition.x = newPosition.x;
		actualPosition.y = newPosition.y;
	}








    private void zooming(float targetZoom) {
		if (scale > targetZoom){
			scale-=(scallingAccelerate/0.15f);
			if (scale<targetZoom) scale = targetZoom;
		}
		else if (scale < targetZoom){
			scale+=(scallingAccelerate/0.15f);
			if (scale>targetZoom) scale = targetZoom;
		}
    }



	private void loadPreviousCameraPosInEditor(){
		if (lastCameraPositionInEditor != null) {
			actualPosition = new PVector(0,0);
			goalPosition = new PVector(0,0);
			actualPosition.x = lastCameraPositionInEditor.x;
			actualPosition.y = lastCameraPositionInEditor.y;
			goalPosition.x = actualPosition.x;
			goalPosition.y = actualPosition.y;
		}
	}




}
