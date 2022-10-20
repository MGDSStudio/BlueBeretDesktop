package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gamecontrollers.CollectableObjectsController;
import com.mgdsstudio.blueberet.gamecontrollers.JumpingLavaBallsController;
import com.mgdsstudio.blueberet.gamecontrollers.LaunchableWhizbangsController;
import com.mgdsstudio.blueberet.gamecontrollers.MoveablePlatformsController;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalToAnotherLevel;
import com.mgdsstudio.blueberet.gameobjects.portals.SimplePortal;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.gameprocess.sound.MusicInGameController;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenPixel;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.textes.OnDisplayText;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.zones.*;
import com.mgdsstudio.blueberet.gameobjects.Bridge;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.gameobjects.RoundRotatingStick;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;


public class RoundLoader {
	private LoadingMaster loadingMaster;
	int actualRoundNumber;

	public RoundLoader(int actualRoundNumber, boolean levelType) {
		this.actualRoundNumber = actualRoundNumber;
		System.out.println("Try to load new level as userLevel: " + (levelType == LoadingMaster.USER_LEVELS));
		loadingMaster = new LoadingMaster(actualRoundNumber, levelType);
	}

	public RoundLoader(String dataString) {
		System.out.println("Try to load new level data from: " + dataString);
		loadingMaster = new LoadingMaster(dataString);
	}

	public MusicInGameController getMusicInGameController(){
		return loadingMaster.getMusicInGameController();
	}

	public ArrayList<RoundElement> getRoundElements() {
		ArrayList<RoundElement> roundElements = new ArrayList<RoundElement>();
		ArrayList<RoundElement> loadedRoundElements = loadingMaster.getRoundElements();
		for (RoundElement roundElement : loadedRoundElements) roundElements.add(roundElement);
		return roundElements;
	}

	public Bridge getBridge(GameRound gameRound, Box2DProcessing physicWorldController) {
		return loadingMaster.getBridge(gameRound);
	}

	public ArrayList<Bridge> getBridges(GameRound gameRound, Box2DProcessing physicWorldController) {
		return loadingMaster.getBridges(gameRound);
	}
	
	public ArrayList<RoundPipe> getRoundPipes(){
		ArrayList<RoundPipe> roundPipes = loadingMaster.getRoundPipes();
		return roundPipes;
	}
	
	public ArrayList<Person> getPersons (Box2DProcessing physicWorldController, GameRound gameRound){
        ArrayList<Person> persons = loadingMaster.getPersons(null, gameRound);
		return persons;
	}

	public ArrayList<PipePortal> getPortals(Box2DProcessing controller) {
		ArrayList<PipePortal> portals = loadingMaster.getPortals();
		return portals;
	}

	public ArrayList<SimplePortal> getSimplePortals() {
		ArrayList<SimplePortal> portals = loadingMaster.getSimplePortals();
		return portals;
	}

	public ArrayList<RoundRotatingStick> getRoundRotatingSticks(Box2DProcessing controller) {
		ArrayList<RoundRotatingStick> rotatingSticks = loadingMaster.getRoundRotatingSticks();
		return rotatingSticks;
	}

	public ArrayList<JumpingLavaBallsController> getJumpingLavaBallsControllers(){
		ArrayList<JumpingLavaBallsController> jumpingLavaBallsControllers = loadingMaster.getJumpingLavaBallsControllers();//
		return jumpingLavaBallsControllers;
	}

	public ArrayList<MoveablePlatformsController> getMovablePlatforms(Box2DProcessing controller) {
		ArrayList<MoveablePlatformsController> moveablePlatformsControllers = loadingMaster.getMovablePlatforms();
		return moveablePlatformsControllers;
	}

	public LaunchableWhizbangsController getLaunchableWhizbangsController(GameCamera gameCamera) {
		LaunchableWhizbangsController launchableWhizbangsController = loadingMaster.getLaunchableWhizbangsController(gameCamera);
		if (launchableWhizbangsController == null){
			launchableWhizbangsController = new LaunchableWhizbangsController(false, gameCamera, null);
			System.out.println("It is loaded without bullet bills");
		}

		return launchableWhizbangsController;
	}

	public Background loadBackground() {
        Background background = loadingMaster.getBackground();
		return background;
	}

	public ArrayList<Background> loadBackgrounds() {
		ArrayList<Background> backgrounds = loadingMaster.getBackgrounds();
		return backgrounds;
	}

	public ArrayList<IndependentOnScreenAnimation> getIndependentOnScreenAnimations() {
		ArrayList<IndependentOnScreenAnimation> independentOnScreenAnimations = loadingMaster.getIndependentOnScreenAnimations();
		return independentOnScreenAnimations;
    }

	public ArrayList<IndependentOnScreenStaticSprite> getIndependentOnScreenStaticSprites() {
		ArrayList<IndependentOnScreenStaticSprite> independentOnScreenSprites = loadingMaster.getIndependentOnScreenStaticSprites();
		return independentOnScreenSprites;
	}

	public ArrayList<IndependentOnScreenPixel> getIndependentOnScreenPixels() {
		ArrayList<IndependentOnScreenPixel> pixels = loadingMaster.getIndependentOnScreenPixels();
		return pixels;
	}

    public ArrayList<ObjectsClearingZone> getObjectsClearingZones() {
		ArrayList<ObjectsClearingZone> objectsClearingZones = loadingMaster.getObjectsClearingZones();
		return  objectsClearingZones;
    }

	public ArrayList<EndLevelZone> getEndLevelZones() {
		ArrayList<EndLevelZone> zones = loadingMaster.getEndLevelZones();
		return zones;
	}

	public ArrayList<AbstractTrigger> getTriggers(GameRound gameRound) {
		ArrayList<AbstractTrigger> zones = loadingMaster.getTriggers(gameRound);
		return zones;
	}

    public CollectableObjectsController getCollectableObjectsController(GameRound gameRound) {
		CollectableObjectsController collectableObjectsController = loadingMaster.getCollectableObjectsController(gameRound);
    	return collectableObjectsController;
    }

    public ArrayList <ObjectsAppearingZone> getObjectsAppearingZonesControllers(GameRound gameRound) {
		ArrayList <ObjectsAppearingZone> objectsAppearingZonesControllers = loadingMaster.getObjectsAppearingZonesControllers(gameRound);
        return objectsAppearingZonesControllers;
    }

    public ArrayList<PortalToAnotherLevel> getPortalsToAnotherLevels(GameRound gameRound) {
		ArrayList <PortalToAnotherLevel> portalsToAnotherLevels = new ArrayList<>();
		portalsToAnotherLevels = loadingMaster.getPortalsToAnotherLevel(gameRound, actualRoundNumber);
		return portalsToAnotherLevels;
    }

    public ArrayList<MessageAddingZone> getMessageAddingZones() {
		ArrayList<MessageAddingZone> messageAddingZones;
		messageAddingZones = loadingMaster.getMessageAddingZones();
		return messageAddingZones;
    }

	public ArrayList<SecretAreaZone> getSecretAreaZones() {
		ArrayList<SecretAreaZone> messageAddingZones;
		messageAddingZones = loadingMaster.getSecretAreaZones();
		return messageAddingZones;
	}

	public ArrayList <OnDisplayText> getOnDisplayTextes(){
		ArrayList <OnDisplayText> onDisplayTexts;
		onDisplayTexts = loadingMaster.getOnDisplayTexts();
		return onDisplayTexts;
	}

	public ArrayList <CameraFixationZone> getCameraFixationZones(){
		ArrayList <CameraFixationZone> cameraFixationZones;
		cameraFixationZones = loadingMaster.getCameraFixationZones();
		return cameraFixationZones;
	}
}
