package com.mgdsstudio.blueberet.gameprocess;


import com.mgdsstudio.blueberet.gamecontrollers.*;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableObjectInNesStyle;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableSack;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.SimpleCollectableElement;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.Plant;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.PlantController;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.portals.Portal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalToAnotherLevel;
import com.mgdsstudio.blueberet.gameobjects.portals.SimplePortal;
import com.mgdsstudio.blueberet.gameprocess.sound.MusicInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.gameprocess.title.EndLevelTitle;
import com.mgdsstudio.blueberet.gameprocess.title.SimpleTitle;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.graphic.background.SingleColorBackground;
import com.mgdsstudio.blueberet.graphic.bullettimescreen.BulletTime2DScreen;
import com.mgdsstudio.blueberet.graphic.bullettimescreen.BulletTime3DScreen;
import com.mgdsstudio.blueberet.graphic.effectsmasters.AbstractGraphicFocusMaster;
import com.mgdsstudio.blueberet.graphic.effectsmasters.BackgroundFocusMaster;
import com.mgdsstudio.blueberet.graphic.effectsmasters.RedColorGraphicMaster;
import com.mgdsstudio.blueberet.graphic.splashes.DustSplash;
import com.mgdsstudio.blueberet.graphic.splashes.JumpSplash;
import com.mgdsstudio.blueberet.graphic.splashes.ShotFireSplash;
import com.mgdsstudio.blueberet.graphic.splashes.Splash;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.graphic.textes.OnDisplayText;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.RoundLoader;
import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressSaveMaster;
import com.mgdsstudio.blueberet.playerprogress.levelresults.LevelResultsCalculator;
import com.mgdsstudio.blueberet.playerprogress.levelresults.LevelResultsSaveController;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.blueberet.zones.*;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.IntList;

import java.util.ArrayList;

public class GameRound{
	//End level data
	public final static boolean withSplashesPool  = true;
	private PlayerControl playerControl;
	private boolean playerWins = false;
	private boolean playerLoosed = false;

	//Graphic
	MainGraphicController mainGraphicController;
	private HUD_LifeLinesController hud_lifeLinesController;
	private final AbstractGraphicFocusMaster backgroundFocusMaster;
	private final RedColorGraphicMaster redColorGraphicMaster;
	private ArrayList <Background> backgrounds;
	private AfterShotFlashController afterShotFlashController;

	//Audio
	private SoundInGameController soundController;



	private MusicInGameController musicController;

	//constants
	final private static byte CLEARING_OBJECTS_FRAME_FREQUENCY = 2;
	final public static byte UPDATE_FREQUENCY_FOR_SECONDARY_OBJECTS = 3;

	InactiveObjectsCleaner inactiveObjectsCleaner;

	private ArrayList <CameraFixationZone> cameraFixationZones;
	private Crosshair playerCrosshair;
	private BulletTime2DScreen bulletTime2DScreen;
	private BulletTime3DScreen bulletTime3DScreen;

	public ArrayList<RoundElement> roundElements;
	ArrayList<Person> persons;
	public ArrayList<Bullet> bullets ;
	public ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	ArrayList<PipePortal> portals;
	public ArrayList<SimplePortal> simplePortals;
	ArrayList<PortalTransferController> portalTransferControllers = new ArrayList<PortalTransferController>();
 	ArrayList<RoundRotatingStick> roundRotatingSticks;
 	ArrayList<MoveablePlatformsController> moveablePlatformsControllers;
 	ArrayList<RoundPipe> roundPipes;
	ArrayList <Bridge> bridges;
	public LaunchableWhizbangsController launchableWhizbangsController;
	public ArrayList<IndependentOnScreenAnimation> independentOnScreenAnimations;
	public ArrayList<IndependentOnScreenStaticSprite> independentOnScreenSprites;
	public ArrayList<GameObject> objectsToBeDeleted = new ArrayList<GameObject>();
	private ArrayList<ObjectsClearingZone> objectsClearingZones;
	public CollectableObjectsController collectableObjectsController;
	public ArrayList <ObjectsAppearingZone> objectsAppearingZones;
	public ArrayList<JumpingLavaBallsController> jumpingLavaBallsControllers;
	ArrayList<SimpleTitle> titles;
	private ArrayList<EndLevelZone> endLevelZones;
	private ArrayList<AbstractTrigger> abstractTriggers;
	private ArrayList<BulletRay> bulletRays;
	private SplashOnGroundAddingController splashOnGroundAddingController;
	private HittingController hittingController;
	private ArrayList <DebugGraphic> debugGraphics;
	public ArrayList<Splash> splashes;
	public Grid grid;
	private boolean usingBulletsPool = true;
	private ArrayList <PortalToAnotherLevel> portalsToAnotherLevel;
	private ArrayList <MessageAddingZone> messageAddingZones;
	private ArrayList <SecretAreaZone> secretAreaZones;
	private ArrayList <DissolvingAndUpwardsMovingText> dissolvingAndUpwardsMovingTexts;
	private ArrayList <IndependentOnScreenPixel> pixels;
	private ArrayList <IndependentOnScreenMovableSprite> moveableSprites;
	//private ArrayList <IndependentOnScreenGraphic> moveableSprites;
	private MoveableSpritesAddingController moveableSpritesAddingController;
	private ArrayList <OnDisplayText> onDisplayTexts;
	private final boolean withEndLevelScreen = false;
	private boolean roundStartedFromEditor;
	private boolean levelType;
	private int levelNumber;
	private final LevelResultsSaveController levelResultsSaveController;

	private IntList idsToBeNotMoreShown;

	private LightsController lightsController;

	GameRound(int roundNumber, GameCamera gameCamera, boolean roundStartedFromEditor, boolean levelType){
		this.roundStartedFromEditor = roundStartedFromEditor;
		this.levelType = levelType;
		this.levelNumber = roundNumber;
		SingleGameElement.gameRound = this;
		System.out.println("!!! Previous string must not be used. It has leakage I thing!!!");

		mainGraphicController = new MainGraphicController();
		RoundLoader roundLoader = new RoundLoader(roundNumber, levelType);
		String pathToLevels = "";
		if (levelType == ExternalRoundDataFileController.MAIN_LEVELS){
			if (Program.OS == Program.DESKTOP) {
				pathToLevels = Program.getRelativePathToAssetsFolder() + Program.USER_LEVELS_PREFIX + roundNumber + Program.USER_LEVELS_EXTENSION;
			}
			else {
				pathToLevels = FileManagement.getPathToCacheFilesInAndroid() + Program.USER_LEVELS_PREFIX + roundNumber + Program.USER_LEVELS_EXTENSION;
			}
		}
		else {
			pathToLevels = Program.getRelativePathToAssetsFolder() + Program.USER_LEVELS_PREFIX + roundNumber + Program.USER_LEVELS_EXTENSION;
		}
		mainGraphicController.addRoundsGraphicFromFile(pathToLevels);
		roundElements = roundLoader.getRoundElements();
		messageAddingZones = roundLoader.getMessageAddingZones();
		secretAreaZones = roundLoader.getSecretAreaZones();
		roundRotatingSticks = roundLoader.getRoundRotatingSticks(PhysicGameWorld.controller);

		persons = roundLoader.getPersons(PhysicGameWorld.controller, this);
		createBulletsPool();
		bridges=roundLoader.getBridges(this, PhysicGameWorld.controller);
		moveablePlatformsControllers = roundLoader.getMovablePlatforms(PhysicGameWorld.controller);
		portals = roundLoader.getPortals(PhysicGameWorld.controller);
		simplePortals = roundLoader.getSimplePortals();
		launchableWhizbangsController = roundLoader.getLaunchableWhizbangsController(gameCamera);
		roundPipes = roundLoader.getRoundPipes();
		backgrounds = roundLoader.loadBackgrounds();
		independentOnScreenAnimations = roundLoader.getIndependentOnScreenAnimations();
		independentOnScreenSprites = roundLoader.getIndependentOnScreenStaticSprites();
		pixels = roundLoader.getIndependentOnScreenPixels();
		objectsClearingZones = roundLoader.getObjectsClearingZones();
		addClearingZonesFromPipesToMainArray();
		collectableObjectsController = roundLoader.getCollectableObjectsController(this);
		objectsAppearingZones = roundLoader.getObjectsAppearingZonesControllers(this);
		jumpingLavaBallsControllers = roundLoader.getJumpingLavaBallsControllers();
		endLevelZones = roundLoader.getEndLevelZones();
		portalsToAnotherLevel = roundLoader.getPortalsToAnotherLevels(this);

		inactiveObjectsCleaner = new InactiveObjectsCleaner();

		onDisplayTexts = roundLoader.getOnDisplayTextes();
		hittingController = new HittingController(this);

		titles = new ArrayList<>();
		bulletRays = new ArrayList<>();
		if (Program.debug) System.out.println("Game round was successfully loaded");
		if (Program.debug) debugGraphics = new ArrayList<>();
		splashes = new ArrayList<>();
		Explosion.loadGraphic(this);
		if (Program.gameStatement == Program.LEVELS_EDITOR) grid = new Grid();
		boolean crosshairReady = false;
		while(!crosshairReady) {
			Soldier player = (Soldier) getPlayer();
			if (player != null) {
				if (Program.gameStatement == Program.GAME_PROCESS) {
					playerCrosshair = new Crosshair(player);
					changeCroshairType(player.getActualWeapon().getWeaponType());
				}
				crosshairReady = true;
			}
			else {
				player = new Soldier(new PVector(0,0), this);
				persons.add(player);
			}
			System.out.println("Crosshair is loading");
		}

		splashOnGroundAddingController = new SplashOnGroundAddingController();
		if (Program.WITH_LIFE_LINES) hud_lifeLinesController = new HUD_LifeLinesController(this);
		loadGraphic();

		addPlantsToPersons();
		dissolvingAndUpwardsMovingTexts = new ArrayList<>();
		cameraFixationZones = roundLoader.getCameraFixationZones();
		if (Program.gameStatement == Program.GAME_PROCESS) {
			soundController = new SoundInGameController(3);
			//musicController = new MusicInGameController(MusicInGameController.getPathToLevelSoundtrackWithoutRelativePath(roundNumber),true, TrackData.VOLUME_10_PERCENT);
			musicController = roundLoader.getMusicInGameController();
			musicController.startToPlay();
		}

		idsToBeNotMoreShown = new IntList();

		lightsController = new LightsController();
		if (Program.gameStatement == Program.GAME_PROCESS) {
			if (Program.engine.sketchRenderer() == PApplet.P3D) {
				System.out.println("Using 3D renderer");
				bulletTime3DScreen = new BulletTime3DScreen(this);
			}
			else bulletTime2DScreen = new BulletTime2DScreen();
		}
		abstractTriggers = roundLoader.getTriggers(this);
		levelResultsSaveController = new LevelResultsSaveController(this, Program.engine);
		backgroundFocusMaster = new BackgroundFocusMaster(backgrounds);
		redColorGraphicMaster = new RedColorGraphicMaster(independentOnScreenSprites);
		//redColorGraphicMaster.applyEffectToGraphic(RedColorGraphicMaster.NORMAL_RED_VALUE);
		//roundLoader = null;

	}

	public void addObjectIdToBeNotMoreUploaded(SingleGameElement singleGameElement){
		if (singleGameElement.getUniqueId() > 1 || singleGameElement.getUniqueId() < (-1)) {
			idsToBeNotMoreShown.append(singleGameElement.getUniqueId());
			PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
			playerProgressSaveMaster.addNotMoreUploadableObject(singleGameElement.getUniqueId());
			playerProgressSaveMaster.writeValuesWithoutSaving();
			System.out.println("Object " + singleGameElement.getClassName() + " with ID " + singleGameElement.getUniqueId());
			playerProgressSaveMaster.saveOnDisk();
		}
		else System.out.println("this object has no unique ID " + singleGameElement.getUniqueId());
	}

	private void createBulletsPool() {
		bullets = new ArrayList<>(15);
		for (int i = 0; i < 15; i++){
			Bullet bullet = new Bullet(getPlayer());
			bullets.add(bullet);
		}
	}

	public boolean isUsingBulletsPool(){
		return usingBulletsPool;
	}

	private void addClearingZonesFromPipesToMainArray(){
		for (RoundPipe roundPipe :roundPipes){
			objectsClearingZones.add(roundPipe.getObjectClearingZone());
		}
	}

	private void addPlantsToPersons(){
		for (RoundPipe roundPipe : roundPipes){
			if (roundPipe.hasFlower()){
				if (roundPipe.getPlantController() != null){
					ArrayList <Plant> plantParts = roundPipe.getPlantController().getPlants();
					for (Plant plant : plantParts){
						persons.add(plant);
					}
				}
			}
		}
		/*
		for (PlantController plantController : plantControllers){
			ArrayList <Plant> plantParts = plantController.getPlants();
			for (Plant plant : plantParts){
				persons.add(plant);
			}
		}
		*/
	}

	public ArrayList<BulletRay> getBulletRays() {
		return bulletRays;
	}

	private void memoryInit(int roundNumber){

	}

	private void loadGraphic() {

		moveableSprites = new ArrayList<>();
		moveableSpritesAddingController = new MoveableSpritesAddingController(moveableSprites);
		for (Background background : backgrounds){
			if (background != null && background.getClass() != SingleColorBackground.class ) {
				System.out.println("Path to background: " + background.getPath());
				background.loadGraphic(mainGraphicController.getTilesetUnderPath(background.getPath()));
			}
		}
		try{
			for (IndependentOnScreenStaticSprite independentOnScreenStaticSprite : independentOnScreenSprites){
				independentOnScreenStaticSprite.staticSprite.loadSprite(mainGraphicController.getTilesetUnderPath(independentOnScreenStaticSprite.staticSprite.getPath()));

			}
		}
		catch (Exception e){
			System.out.println("******** No static sprites for this level. Exception: " + e);
		}
		try{
			for (IndependentOnScreenAnimation independentOnScreenAnimation : independentOnScreenAnimations){
				independentOnScreenAnimation.spriteAnimation.loadAnimation(mainGraphicController.getTilesetUnderPath(independentOnScreenAnimation.spriteAnimation.getPath()));
			}
		}
		catch (Exception e){
			System.out.println("******** No independent sprite animations. Exception: " + e);
		}
		IndependentOnScreenPixel.loadSprite(mainGraphicController.getTilesetUnderPath(HeadsUpDisplay.mainGraphicSource.getPath()));
		try {
			for (RoundElement roundElement : roundElements) {
				if (roundElement.hasGraphic()) {
					roundElement.loadSprites(mainGraphicController.getTilesetUnderPath(roundElement.getSprite().getPath()));
				}
			}
		} catch (Exception e) {
			System.out.println("******** No graphic data for round elements. Exception: " + e);
		}
		try{
			for (Person person : persons){

					person.loadAnimationData(mainGraphicController);


			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for person. Exception: " + e);
		}
		try{
			for (ObjectsAppearingZone objectsAppearingZone : objectsAppearingZones){
				if (objectsAppearingZone.getObject() instanceof Person){
					Person person = (Person) objectsAppearingZone.getObject();
					person.loadAnimationData(mainGraphicController);
					System.out.println("Person in objects appearing zone was upload his graphic");
				}
				else if (objectsAppearingZone.getObject().getClass() == CollectableObjectInNesStyle.class){
					CollectableObjectInNesStyle object = (CollectableObjectInNesStyle) objectsAppearingZone.getObject();
					if (object.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) object.loadSprites(mainGraphicController.getTilesetUnderPath(object.getSprite().getPath()));
					else if (object.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) object.loadAnimation(mainGraphicController.getTilesetUnderPath(object.getSpriteAnimation().getPath()));
					System.out.println("Game object is upload his graphic");
				}
				else if (objectsAppearingZone.getObject() instanceof AbstractCollectable){
					AbstractCollectable object = (AbstractCollectable) objectsAppearingZone.getObject();
					if (object.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) object.loadSprites(mainGraphicController.getTilesetUnderPath(object.getSprite().getPath()));
					else if (object.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) object.loadAnimation(mainGraphicController.getTilesetUnderPath(object.getSpriteAnimation().getPath()));
					System.out.println("Game object is upload his graphic");
				}
				else {
					System.out.println("For this object can not find any graphic data for appearing; " + objectsAppearingZone.getObject().getClass());
				}
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for new appearing objects. Exception: " + e);
		}
		try {
			for (RoundPipe roundPipe : roundPipes) {
				roundPipe.loadSprites(mainGraphicController.getTilesetUnderPath(roundPipe.getSprite().getPath()), null, null);
				if (roundPipe.hasFlower()) {
					String pathToLeftJaw = roundPipe.getPlantController().getFlowerPart(PlantController.LEFT_JAW).getSprite().getPath();
					String pathToRightJaw = roundPipe.getPlantController().getFlowerPart(PlantController.RIGHT_JAW).getSprite().getPath();
					String pathToRod = roundPipe.getPlantController().getFlowerPart(PlantController.ROD).getSprite().getPath();
					roundPipe.loadSprites(mainGraphicController.getTilesetUnderPath(roundPipe.getSprite().getPath()), mainGraphicController.getTilesetUnderPath(pathToLeftJaw), mainGraphicController.getTilesetUnderPath(pathToRod));

				}
			}
		}

		catch (Exception e) {
			System.out.println("******** No graphic data for round pipes. Exception: " + e);
		}
		if (Program.WITH_LIFE_LINES) hud_lifeLinesController.loadGraphic(mainGraphicController);
		try {
			DragonFire.loadAnimation(mainGraphicController.getTilesetUnderPath(DragonFire.getSpriteAnimation().getPath()));
			String path = InWorldObjectsGraphicData.mainGraphicFile.getPath();
			System.out.println("Path for whizbang: " + (path ) + " but tileset is null " + (mainGraphicController.getTilesetUnderPath(path) == null));
			AbstractGrenade.loadSprites(mainGraphicController.getTilesetUnderPath(path));
			if (launchableWhizbangsController != null) {
				String pathForBill = BulletBill.getSprite().getPath();
				System.out.println("Path for bullet bill = null: " + (path == null));
				BulletBill.loadSprites(mainGraphicController.getTilesetUnderPath(pathForBill));
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for launchable whizbangs. Exception: " + e);
		}



		/*
		try {
			DragonFire.loadAnimation(mainGraphicController.getTilesetUnderPath(DragonFire.getSpriteAnimation().getPath()));
			String path = InWorldObjectsGraphicData.mainGraphicFile.getPath();
			System.out.println("Path for whizbang: " + (path ) + " but tileset is null " + (mainGraphicController.getTilesetUnderPath(path) == null));
			AbstractGrenade.loadSprites(mainGraphicController.getTilesetUnderPath(path));
			if (launchableWhizbangsController != null) {
				String pathForBill = BulletBill.getSprite().getPath();
				System.out.println("Path for bullet bill = null: " + (path == null));
				BulletBill.loadSprites(mainGraphicController.getTilesetUnderPath(pathForBill));
			}
		} catch (Exception e) {
			System.out.println("******** No graphic data for launchable whizbangs. Exception: " + e);
		}
		 */


		try {
			for (MoveablePlatformsController moveablePlatformsController : moveablePlatformsControllers) {
				//System.out.println("Trying");
				moveablePlatformsController.loadSprites(mainGraphicController.getTilesetUnderPath(moveablePlatformsController.getSprite().getPath()));
				System.out.println("Graphic for platforms is loaded");
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for movable platforms. Exception: "+ e);
		}
		try{
			for (Bridge bridge : bridges) {
				bridge.loadSprites(mainGraphicController.getTilesetUnderPath(bridge.getSprite().getPath()));
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for bridge. Exception: "+ e);
		}
		try{
			for (RoundRotatingStick roundRotatingStick : roundRotatingSticks){
				roundRotatingStick.loadSprites(mainGraphicController.getTilesetUnderPath(roundRotatingStick.getSprite().getPath()));
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for round rotating sticks. Exception: "+ e);
		}
		try{
			for (int i = 0; i < collectableObjectsController.getObjectsNumber(); i++) {
				AbstractCollectable collectable = collectableObjectsController.getCollectableObjects().get(i);
				//System.out.println("Path: " + collectableObjectsController.getSpriteAnimation(i).getPath());
				if (collectable.hasGraphic()){
					if (collectable.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE){
						collectable.loadAnimation(mainGraphicController.getTilesetUnderPath(InWorldObjectsGraphicData.mainGraphicFile.getPath()));
						//collectableObjectsController.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObjectsController.getSpriteAnimation(i).getPath()), i);
						//collectableObjectsController.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObjectsController.getSpriteAnimation(i).getPath()), i);
						//
					}
					else {
						collectable.loadSprites(mainGraphicController.getTilesetUnderPath(InWorldObjectsGraphicData.mainGraphicFile.getPath()));
						System.out.println("Graphic was loaded. Tileset is null " + (mainGraphicController.getTilesetUnderPath(InWorldObjectsGraphicData.mainGraphicFile.getPath())==null));
					}
						/*
						collectableObjectsController.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObjectsController.getSpriteAnimation(i).getPath()), i);

						 */
				}
			}
		}
		catch (Exception e){
			System.out.println("******** No graphic data for collectable elements. Exception: "+ e);
		}
		afterShotFlashController = new AfterShotFlashController(this);
	}



	private boolean areThereAtLeastOneFullScreenBackground(){
		//boolean thereAre = false;
		for (Background background : backgrounds){
			if (!background.isHide()){
				if (background.getClass() == SingleColorBackground.class || background.getClass() == ScrollableAlongXBackground.class){
					//thereAre = true;
					return true;
				}
			}
		}
		return false;
	}

	void clearGraphic(){
		//for (Background background : backgrounds) background.clear(gameCamera);
		Program.objectsFrame.beginDraw();
		Program.objectsFrame.clear();
		Program.objectsFrame.endDraw();

		//System.out.println("Cleared");

	}

	void drawBackground(GameCamera gameCamera) {
		if (backgrounds != null) {
			if (backgrounds.size() == 0) {
				Background.drawBackgroundAsWhite();
			}
			else{
				boolean someFullScreenBackgroundsShown = false;
				for (Background background : backgrounds) {
					if (background.getClass() == ScrollableAlongXBackground.class || background.getClass() == SingleColorBackground.class){
						someFullScreenBackgroundsShown = true;
					}
				}
				if (!someFullScreenBackgroundsShown){
					Background.drawBackgroundAsWhite();
				}
				for (Background background : backgrounds) {
					background.draw(gameCamera);
				}
			}
		}
		else {
			Background.drawBackgroundAsWhite();
		}
	}




	public void setFixedRotationForAllLivingPersons(){
		for (Person person : persons){
			if (person.isAlive()) {
				if (!person.body.isFixedRotation() && (person instanceof Plant) == false) {
					person.setFixedRotation();
				}
			}
		}
	}




	private void drawByMultithreading(GameCamera gameCamera, boolean withGrayscale){
		if (withGrayscale) {
			drawBulletTimeRect(gameCamera);
		}
		for (IndependentOnScreenPixel pixel : pixels){
			if (pixel.getLayer() == ILayerable.BEHIND_ALL) pixel.draw(gameCamera);	//scaled!
		}
		for (int i = 0; i < independentOnScreenSprites.size(); i++){
			if (independentOnScreenSprites.get(i).layer == IndependentOnScreenGraphic.BEHIND_ALL) independentOnScreenSprites.get(i).draw(gameCamera);
		}
		for (int i = 0; i < independentOnScreenAnimations.size(); i++){
			if (independentOnScreenAnimations.get(i).layer == IndependentOnScreenGraphic.BEHIND_ALL) independentOnScreenAnimations.get(i).draw(gameCamera);	//scaled!
		}

		for (int i = 0; i < splashes.size(); i++){
			if (splashes.get(i).getLayer() == IndependentOnScreenGraphic.BEHIND_ALL) splashes.get(i).draw(gameCamera);	//Scaled
		}


		for (int i = 0; i < independentOnScreenSprites.size(); i++){
			if (independentOnScreenSprites.get(i).layer == IndependentOnScreenGraphic.BEHIND_PERSONS) independentOnScreenSprites.get(i).draw(gameCamera);
		}
		for (int i = 0; i < independentOnScreenAnimations.size(); i++){
			if (independentOnScreenAnimations.get(i).layer == IndependentOnScreenGraphic.BEHIND_PERSONS) independentOnScreenAnimations.get(i).draw(gameCamera);
		}
		for (IndependentOnScreenPixel pixel : pixels){
			if (pixel.getLayer() == ILayerable.BEHIND_PERSONS) pixel.draw(gameCamera);
		}
		for (IndependentOnScreenMovableSprite screenMovableSprite : moveableSprites){
			screenMovableSprite.draw(gameCamera);	//Scaled!
			//System.out.println("Sprite: " + screenMovableSprite.getType() + " is drawn");
		}
		for (int i = 0; i < bullets.size(); i++){
			if (!bullets.get(i).isSleeped()) bullets.get(i).draw(gameCamera);
		}
		if (launchableWhizbangsController != null ) launchableWhizbangsController.draw(gameCamera);
		collectableObjectsController.draw(gameCamera);
		for (int i = (persons.size()-1) ; i >=0; i--){
			persons.get(i).draw(gameCamera);
		}
		/*
		for (int i = 0 ; i < persons.size(); i++){
			persons.get(i).draw(gameCamera);
		}
		 */
		for (int i = 0; i < bridges.size(); i++){
			bridges.get(i).draw(gameCamera);
		}
		for (int i = 0; i < roundElements.size(); i++){
			if (!roundElements.get(i).isSleeped()) roundElements.get(i).draw(gameCamera);
		}
		for (int i = 0; i < roundPipes.size(); i++){
			roundPipes.get(i).draw(gameCamera); //Scaled~
		}
		for (int i = 0; i < moveablePlatformsControllers.size(); i++){
			moveablePlatformsControllers.get(i).draw(gameCamera);
		}
		for (int i = 0; i < roundRotatingSticks.size(); i++){
			roundRotatingSticks.get(i).draw(gameCamera);
		}
		for (int i = 0; i < bulletRays.size(); i++){
			bulletRays.get(i).draw(gameCamera);
		}
		for (int i = 0; i < splashes.size(); i++){
			if (splashes.get(i).getLayer() == IndependentOnScreenGraphic.IN_FRONT_OF_ALL) splashes.get(i).draw(gameCamera);
		}
		for (int i = 0; i < explosions.size(); i++){
			explosions.get(i).draw(gameCamera);
		}

		for (int i = 0; i < independentOnScreenSprites.size(); i++){
			if (independentOnScreenSprites.get(i).layer == IndependentOnScreenGraphic.IN_FRONT_OF_ALL) independentOnScreenSprites.get(i).draw(gameCamera);
		}
		for (int i = 0; i < independentOnScreenAnimations.size(); i++){
			if (independentOnScreenAnimations.get(i).layer == IndependentOnScreenGraphic.IN_FRONT_OF_ALL) independentOnScreenAnimations.get(i).draw(gameCamera);
		}
		for (IndependentOnScreenPixel pixel : pixels){
			if (pixel.getLayer() == ILayerable.IN_FRONT_OF_ALL) pixel.draw(gameCamera);
		}
		if (Program.WITH_LIFE_LINES) hud_lifeLinesController.draw(gameCamera);
		for (MessageAddingZone messageAddingZone : messageAddingZones){
			messageAddingZone.draw(gameCamera);
		}
		if (Program.gameStatement == Program.LEVELS_EDITOR) {
			if (grid!=null)	grid.draw(gameCamera);
			for (int i = 0; i < independentOnScreenAnimations.size(); i++){
				independentOnScreenAnimations.get(i).update();
			}
		}
		else {
			if (playerCrosshair!= null) playerCrosshair.draw(gameCamera);
		}
		//afterShotFlashController.draw(gameCamera);
		for (int i = 0; i < objectsClearingZones.size(); i++){
			objectsClearingZones.get(i).draw(gameCamera);
		}
		for (int i = 0; i < endLevelZones.size(); i++){
			endLevelZones.get(i).draw(gameCamera);
		}
		for (int i = 0; i < abstractTriggers.size(); i++){
			abstractTriggers.get(i).draw(gameCamera);
		}
		for (int i = 0; i < portals.size(); i++){
			portals.get(i).draw(gameCamera);
		}
		for (int i = 0; i < simplePortals.size(); i++){
			simplePortals.get(i).draw(gameCamera);
		}
		for (DissolvingAndUpwardsMovingText text : dissolvingAndUpwardsMovingTexts){
			if (Program.gameStatement == Program.LEVELS_EDITOR) text.update();
			text.draw(gameCamera);
		}
		for (PortalToAnotherLevel portalToAnotherLevel : portalsToAnotherLevel){
			portalToAnotherLevel.draw(gameCamera);
		}

		for (CameraFixationZone zone : cameraFixationZones){
			zone.draw(gameCamera);
		}
		for (SecretAreaZone secretAreaZone : secretAreaZones){
			secretAreaZone.draw(gameCamera);
		}
	}

	private void drawBulletTimeRect(GameCamera gameCamera) {
		if (Program.gameStatement == Program.GAME_PROCESS) {
			if (Program.engine.sketchRenderer() == PApplet.P3D) {
				bulletTime3DScreen.draw(gameCamera);
			}
			else {
				bulletTime2DScreen.draw(gameCamera);
			}
		}
	}

	void activateBulletTime(int time){
		if (Program.engine.sketchRenderer() == PApplet.P2D || Program.engine.sketchRenderer() == PApplet.JAVA2D) {
			bulletTime2DScreen.activate(time);
			bulletTime2DScreen.loadGraphic(HeadsUpDisplay.mainGraphicTileset);
		}
		else {
			bulletTime3DScreen.activate(time);
		}

	}

	void drawGameWorldObjects(GameCamera gameCamera, boolean withGrayscale) {
		drawByMultithreading(gameCamera, withGrayscale);
	}

	public boolean areThereActiveTitles(){
		if (titles.size()>0) {
			return true;
		}
		else return false;
	}



	public void drawTitles(){
		for (SimpleTitle title : titles){
			title.draw();
		}
	}
	
	public ArrayList<Person> getPersons() {
		return persons;
	}

	/*
	public ArrayList<RoundDynamicElement> getRoundDynamicBoxes() {
		return roundSimpleTiles;
	}
	*/



	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void removeBullet(int i) {
		bullets.remove(i);
	}

	public void update(GameCamera gameCamera) {
		//getPlayer().body.setActive(true);
		setFixedRotationForAllLivingPersons();
		inactiveObjectsCleaner.update(this);
		if (Program.WITH_LIFE_LINES) hud_lifeLinesController.update(this);
		//whiteScreen.update(gameCamera);

			if (collectableObjectsController != null) {
				collectableObjectsController.update(this);
			}
			for (EndLevelZone zone : endLevelZones) {
				zone.update(this);
			}
			for (AbstractTrigger abstractTrigger : abstractTriggers) {
				abstractTrigger.update(this);
			}
			for (ObjectsClearingZone objectsClearingZone : objectsClearingZones) {
				objectsClearingZone.update(this);
			}
			for (Bullet bullet : bullets) {
				if (!bullet.isSleeped()) bullet.update();
			}

			for (int i = (persons.size()-1); i>=0; i--){
				Person person = persons.get(i);
				if (!person.isSleeped() && person.isAlive()) {
					person.update(this);
				} else if (!person.isSleeped() && person.getClass() == Snake.class) person.update();
				else if (person.getClass() == Lizard.class || person.getClass() == BossBoar.class) person.update(this);
			}
			splashOnGroundAddingController.update(this);
			/*
			for (Person person : persons) {
				if (!person.isSleeped() && person.isAlive()) {
					person.update(this);
				} else if (!person.isSleeped() && person.getClass() == Snake.class) person.update();
				else if (person.getClass() == Lizard.class) person.update(this);
			}*/
			/*
			for (Person person : persons) {
				if (!person.isSleeped() && person.isAlive()) {
					person.update(this);
				} else if (!person.isSleeped() && person.getClass() == Snake.class) person.update();
				else if (person.getClass() == Lizard.class) person.update(this);
			}
			 */

			for (IndependentOnScreenAnimation independentOnScreenAnimation : independentOnScreenAnimations) {
				independentOnScreenAnimation.update();
			}
			for (JumpingLavaBallsController jumpingLavaBallsController : jumpingLavaBallsControllers) {
				jumpingLavaBallsController.update(this);
			}
			for (BulletRay bulletRay : bulletRays) {
				bulletRay.update(this);
			}
			for (SimplePortal portal : simplePortals) {
				portal.update(this, gameCamera);
			}

			try {
				for (Bridge bridge : bridges) {
					if (!bridge.wasCrushed()) bridge.update(this);
					else {
						System.out.println("The bridge was deleted");
						bridges.remove(bridge);
					}
				}
				for (ObjectsAppearingZone objectsAppearingZone : objectsAppearingZones)
					objectsAppearingZone.update(this);
				for (PortalTransferController portalTransferController : portalTransferControllers) {
					portalTransferController.update(this);
				}
				for (Explosion explosion : explosions) {
					explosion.update();
				}
				for (RoundRotatingStick roundRotatingStick : roundRotatingSticks) {
					if (!roundRotatingStick.isSleeped()) roundRotatingStick.update();
				}
				for (MoveablePlatformsController moveablePlatformsController : moveablePlatformsControllers) {
					moveablePlatformsController.update();
				}
				for (RoundPipe roundPipe : roundPipes) {
					roundPipe.update(this);
				}
				if (launchableWhizbangsController != null) launchableWhizbangsController.update(this);

			} catch (Exception e) {
				System.out.println("Can not update some objects!");
				System.out.println(e);
			}

			for (Splash bloodSplash : splashes) bloodSplash.update();

			for (SimpleTitle title : titles) {
				title.update();
			}
		for (DissolvingAndUpwardsMovingText text : dissolvingAndUpwardsMovingTexts) {
			text.update();
		}
		for (SecretAreaZone zone : secretAreaZones) {
			zone.update(this);
		}
		if (Program.gameStatement == Program.GAME_PROCESS) {
			playerCrosshair.update(this);
			musicController.update();
			//soundController.update();
		}
		/*
		for (OnDisplayText text : onDisplayTexts){
			text.update();	updated in gameProcess
		}*/
		afterShotFlashController.update();
	}


	public void addNewRoundElement(RoundElement roundElement){
		roundElements.add(roundElement);
	}

	public void addNewRoundBox(RoundBox roundBox){
		//System.out.println("Size was: " + roundElements.size());
		//System.out.println("Add will : " + roundBox);
		roundElements.add(roundBox);
	}

	/*
	void updatePlayerTransportThroughPortal(GameControl gameControl) {
		boolean personOnFlag = false;
		boolean flowerOnPortal = true;
		int nearestPortalNumber = 0;
		for (int i = 0; i < portals.size(); i++) {
			if (portals.get(i).mustBeOnScreenButtonShown(getPlayer())) {
				personOnFlag = true;
				nearestPortalNumber = i;
				for (RoundPipe roundPipe : roundPipes){
					if (roundPipe.hasFlower()) {
						if (portals.get(i).isPortalFreeFromFlower(roundPipe.flower)) {
							flowerOnPortal = true;
							return;
						}

					}
				}

}
		}
				if (flowerOnPortal || !personOnFlag) gameControl.hideButton(GameControl.BUTTON_PORTAL, true);
				if (personOnFlag) gameControl.hideButton(GameControl.BUTTON_PORTAL, false);
				else gameControl.hideButton(GameControl.BUTTON_PORTAL, true);
				boolean objectIsAlreadyTransported = false;
				if (gameControl.isButtonPressed(GameControl.BUTTON_PORTAL) && !getPlayer().isTransferingThroughPortal()) {
				for (PortalTransfer portalTransfer : portalTransfers) {
				if (portalTransfer.object.equals(getPlayer())) {
				objectIsAlreadyTransported = true;
				}
				}
				if (!objectIsAlreadyTransported) portalTransfers.add(new PortalTransfer(gameControl, portals.get(nearestPortalNumber), getPlayer()));
				}
				}
	*/

	void updatePlayerTransportThroughPortal(PlayerControl playerControl) {
		boolean personOnFlag = false;
		boolean flowerOnPortal = false;
		int nearestPortalNumber = 0;
		Flag flag = null;
		for (int i = 0; i < portals.size(); i++) {
			if (portals.get(i).mustBeOnScreenButtonShown(getPlayer())) {
				personOnFlag = true;
				nearestPortalNumber = i;
				if (portals.get(i).getTransferDirection() == PipePortal.ENTER_TO_EXIT) flag = portals.get(i).enter;
				for (RoundPipe roundPipe : roundPipes) {
					if (roundPipe.hasFlower()) {
						if (!portals.get(i).isPortalFreeFromFlower(roundPipe.getPlantController())) {
							flowerOnPortal = true;
							return;
						}
					}
				}
				if (flowerOnPortal) return;
			}
		}
		if (flowerOnPortal || !personOnFlag) playerControl.hideButton(PlayerControl.BUTTON_PORTAL, true);
		if (personOnFlag) {
			int angle = 0;
			if (flag!= null){
				if (flag.getDirection() == Flag.TO_LEFT) angle = 90;
				else if (flag.getDirection() == Flag.TO_RIGHT) angle = -90;
				else if (flag.getDirection() == Flag.TO_UP) angle = 180;
			}
			playerControl.rotateButton(PlayerControl.BUTTON_PORTAL, angle);
			playerControl.hideButton(PlayerControl.BUTTON_PORTAL, false);
		}
		else {
			if (!playerControl.isPlayerCanEnterPortal()) playerControl.hideButton(PlayerControl.BUTTON_PORTAL, true);
		}
		boolean objectIsAlreadyTransported = false;	
		if (playerControl.isButtonPressed(PlayerControl.BUTTON_PORTAL) && !getPlayer().isTransferingThroughPortal()) {
			for (PortalTransferController portalTransferController : portalTransferControllers) {
				if (portalTransferController.object.equals(getPlayer())) {
					objectIsAlreadyTransported = true;
				}
			}
			if (!objectIsAlreadyTransported) portalTransferControllers.add(new PortalTransferController(playerControl, portals.get(nearestPortalNumber), getPlayer()));
		}
	}

	/*
	void updatePlayerTransportThroughPortal(GameControl gameControl) {
		boolean personOnFlag = false;
		boolean flowerOnPortal = false;
		int nearestPortalNumber = 0;
		for (int i = 0; i < portals.size(); i++) {
			if (portals.get(i).mustBeOnScreenButtonShown(getPlayer())) {
				personOnFlag = true;
				nearestPortalNumber = i;
				for (RoundPipe roundPipe : roundPipes) {
					if (roundPipe.hasFlower()) {
						if (portals.get(i).mustBeOnScreenButtonShown(roundPipe.flower)) {
							flowerOnPortal = true;
							return;
						}
					}
				}
				if (flowerOnPortal) return;
			}
		}
		if (flowerOnPortal || !personOnFlag) gameControl.hideButton(GameControl.BUTTON_PORTAL, true);
		if (personOnFlag) gameControl.hideButton(GameControl.BUTTON_PORTAL, false);
		else gameControl.hideButton(GameControl.BUTTON_PORTAL, true);
		boolean objectIsAlreadyTransported = false;
		if (gameControl.isButtonPressed(GameControl.BUTTON_PORTAL) && !getPlayer().isTransferingThroughPortal()) {
			for (PortalTransfer portalTransfer : portalTransfers) {
				if (portalTransfer.object.equals(getPlayer())) {
					objectIsAlreadyTransported = true;
				}
			}
			if (!objectIsAlreadyTransported) portalTransfers.add(new PortalTransfer(gameControl, portals.get(nearestPortalNumber), getPlayer()));
		}
	}
	*/
		
	void updatePersonsTransportThroughPortal(PlayerControl playerControl) {
        boolean personOnFlag = false;
        boolean flowerOnPortal = false;
        int nearestPortalNumber = 0;
        if (portals.size()>0){
        for (Person person : persons) {
            if (person.getClass() != Soldier.class && person.isAlive()) {
                for (int i = 0; i < portals.size(); i++) {
                    if (portals.get(i).mustBeOnScreenButtonShown(person) && portals.get(i).getActivatedBy() != PipePortal.BY_PLAYER) {
                        personOnFlag = true;
                        nearestPortalNumber = i;
                        for (RoundPipe roundPipe : roundPipes) {
                            if (roundPipe.hasFlower()) {
								PlantController plantController = roundPipe.getPlantController();
								if (plantController != null){
									for (Plant part : plantController.getPlants()){
										if (!part.isDead()){
											if (portals.get(i).mustBeOnScreenButtonShown(part)){
												flowerOnPortal = true;
												return;
											}
										}

									}


									/*
									if (portals.get(i).mustBeOnScreenButtonShown(roundPipe.getPlantController().getPlants().get(0))
											|| portals.get(i).mustBeOnScreenButtonShown(roundPipe.getPlantController().getPlants().get(1)) ||
											portals.get(i).mustBeOnScreenButtonShown(roundPipe.getPlantController().getPlants().get(2))) {
										flowerOnPortal = true;
										return;
									}
									*/

								}


                            }
                        }
                        if (flowerOnPortal) return;	//maybe break?
                    }
                }
                boolean objectIsAlreadyTransported = false;
                if (!person.isTransferingThroughPortal()) {
                    for (PortalTransferController portalTransferController : portalTransferControllers) {
                        if (portalTransferController.object.equals(person)) {
                            objectIsAlreadyTransported = true;
                        }
                    }
                    if (!objectIsAlreadyTransported && portals.get(nearestPortalNumber).getActivatedBy() != PipePortal.BY_PLAYER) {
                        portalTransferControllers.add(new PortalTransferController(playerControl, portals.get(nearestPortalNumber), person));
                    }
                }
            }
        }
    }
	}
		
	void updatePortals(PlayerControl playerControl) {
		for (int i = 0; i < portalTransferControllers.size(); i++) {
			portalTransferControllers.get(i).update(this);
			if (portalTransferControllers.get(i).isEnded()) {
				if (portalTransferControllers.get(i).mustBePortalDeleted()) {
					System.out.println("This portal can be deleted");
					for (int j = 0; j < portals.size(); j++) {
						if (portals.get(j).equals(portalTransferControllers.get(i).portal)) portals.remove(portals.get(j));
					}
				}
				portalTransferControllers.remove(portalTransferControllers.get(i));
			}
		}
		updatePlayerTransportThroughPortal(playerControl);
		updatePersonsTransportThroughPortal(playerControl);
	}
		
	public void updateContacts() {
		//final boolean saveUnderLayingBodies  = false;
		//if (saveUnderLayingBodies) updateObjectsUnderPersons();
		hittingController.update(this);//updateHitting();!!!!
	}

	private void updateObjectsUnderPersons() {
		//System.out.println("Begin contacts: " + PhysicGameWorld.beginContacts.size());
		for (Contact contact : PhysicGameWorld.beginContacts){
			Body b1 = contact.m_fixtureA.getBody();
			Body b2 = contact.m_fixtureB.getBody();
			if (b1 != null && b2 != null && b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC) {
				for (Person person : persons) {
					if (person.body.equals(b1)){
						person.setLowerLayingBody(b2);
						break;
					}
					else if (person.body.equals(b2)){
						person.setLowerLayingBody(b1);
						break;
					}
				}
			}
		}
		for (Contact contact : PhysicGameWorld.endContacts){
			Body b1 = contact.m_fixtureA.getBody();
			Body b2 = contact.m_fixtureB.getBody();
			if (b1 != null && b2 != null && b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC) {
				for (Person person : persons) {
					if (person.body.equals(b1)){
						if (person.getLowerLayingBody() != null && person.getLowerLayingBody().equals(b2)) {
							person.setLowerLayingBody(b1);
							break;
						}
					}
					else if (person.body.equals(b2)){
						if (person.getLowerLayingBody() != null && person.getLowerLayingBody().equals(b1)) {
							person.setLowerLayingBody(b2);
							break;
						}
					}
				}
			}
		}
	}

	public void endContactsUpdating() {
		hittingController.endUpdating(this);//updateHitting();!!!!
	}
	
	void updateHitting() {
		/*
		updateBulletsHitting();
		if (launchableWhizbangsController == null) System.out.println("Controller is null");
		if (launchableWhizbangsController!= null) launchableWhizbangsController.updateWhizbangsCoalisions(this);
		*/
	}
	



	/*
	private void updateWhizbangsCoalisions() {
		for (Contact contact : PhysicGameWorld.beginContacts) {
			Fixture f1 = contact.getFixtureA();
			Fixture f2 = contact.getFixtureB();			
			Body b1 = f1.getBody();
			Body b2 = f2.getBody();	
			for (int i = 0; i < launchableWhizbangsController.getWhizbangsNumber(); i++) {				
				try {
					if (launchableWhizbangsController.getBody(i) == b1 && !b2.isBullet()) {
						if (f1.getType() == ShapeType.CIRCLE) {		
							Explosion explosion = new Explosion(PhysicGameWorld.controller.coordWorldToPixels(b1.getPosition()), launchableWhizbangsController.getWhizbang(i));						
							explosions.add(explosion);
							launchableWhizbangsController.killBullet(i);
							ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
							singleCollidings = explosions.get(explosions.size()-1).getCollidings();
							applyExplosionsDamages(singleCollidings, explosions.get(explosions.size()-1).getMaxImpulse(), explosions.get(explosions.size()-1).getMaxDamage()); 
							System.out.println("Collidings number: " + singleCollidings.size());
						}
						else {
							launchableWhizbangsController.stopWhizbang(i);
						}
					}
					else if (launchableWhizbangsController.getBody(i) == b2 && !b1.isBullet()) {
						if (f2.getType() == ShapeType.CIRCLE) {
							Explosion explosion = new Explosion(PhysicGameWorld.controller.coordWorldToPixels(b2.getPosition()), launchableWhizbangsController.getWhizbang(i));						
							explosions.add(explosion);
							launchableWhizbangsController.killBullet(i);
							ArrayList<SingleColliding> singleCollidings = new ArrayList<SingleColliding>();
							singleCollidings = explosions.get(explosions.size()-1).getCollidings();						
							applyExplosionsDamages(singleCollidings, explosions.get(explosions.size()-1).getMaxImpulse(), explosions.get(explosions.size()-1).getMaxDamage());
							System.out.println("Collidings number: " + singleCollidings.size());
						}
						else {
							launchableWhizbangsController.stopWhizbang(i);
						}
					}
				}
				catch (Exception e) {
					System.out.println("Bullet bill coalision has a trouble; " +  e);
				}				
			}
		}
	}
	*/
	public void moveAttackPositionBack(Vec2 attackPos, float attackAngle){
		System.out.println("Attack place was on " + attackPos);
		final int stepBackValue = 150;
		float newAngle = attackAngle+180;
		if (newAngle>360) newAngle-=360;
		attackPos.x+=stepBackValue*PApplet.cos(PApplet.radians(newAngle));
		attackPos.y+=stepBackValue*PApplet.sin(PApplet.radians(newAngle));
		System.out.println("Attack place is on " + attackPos);
		System.out.println("Angle: " + newAngle);
	}



	public void addNewIndependentOnScreenStaticSprite(IndependentOnScreenStaticSprite independentOnScreenSprite){
		independentOnScreenSprites.add(independentOnScreenSprite);
		independentOnScreenSprite.staticSprite.loadSprite((mainGraphicController.getTilesetUnderPath(independentOnScreenSprite.getPath())));
	}

	public void addNewIndependentOnScreenAnimation(IndependentOnScreenAnimation independentOnScreenAnimation){
		independentOnScreenAnimations.add(independentOnScreenAnimation);
		if (independentOnScreenAnimation.spriteAnimation.getTileset() == null) {
			independentOnScreenAnimation.spriteAnimation.loadAnimation((mainGraphicController.getTilesetUnderPath(independentOnScreenAnimation.getPath())));
		}
	}



	public void addNewIndependentOnScreenMoveableSprite(IndependentOnScreenMovableSprite sprite){
		//moveableSpritesAddingController.addNewIndependentOnScreenMoveableSprite(sprite);
		System.out.println("*** Maybe I need to add sprite to the controller!. But falling velocity will be larger");
		moveableSprites.add(sprite);
	}

	public boolean existEndedMoveableSpritesForType(int type){
		return moveableSpritesAddingController.existsFree(type);
	}

	public IndependentOnScreenMovableSprite getEndedSpriteForType(int type){

		System.out.println("Moveable sprites array size: " + moveableSprites.size());
		return moveableSpritesAddingController.getExistingFreeSprite(type);
	}

	public void releaseLastCollectableGameObject(GameObject roundDynamicElement, float angleInDegrees){
		if (roundDynamicElement.hasAnyCollectableObjects()){
				AbstractCollectable collectableObject = roundDynamicElement.getCollectableObjects().get(roundDynamicElement.getCollectableObjects().size()-1);


				Vec2 objectPosition = roundDynamicElement.getPositionForReleasedObject();

				collectableObject.addBodyToWorld(objectPosition);
				final float normalObjectSpeed = 200;
				if (angleInDegrees > 1 && angleInDegrees < 179){		// Objects can jump only up
					angleInDegrees = 360-angleInDegrees;
				}
				Vec2 linearImpulse = new Vec2(normalObjectSpeed*PApplet.cos(PApplet.radians(angleInDegrees)),-normalObjectSpeed*PApplet.sin(PApplet.radians(angleInDegrees)));
				collectableObject.body.applyLinearImpulse((PhysicGameWorld.controller.vectorPixelsToWorld(linearImpulse)), collectableObject.body.getWorldCenter(), true);
				if (collectableObject.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
					System.out.println("Try to load sprite");
					collectableObject.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObject.getSprite().getPath()));
				}
				else if (collectableObject.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) {
					try{
						collectableObject.loadAnimation(mainGraphicController.getTilesetUnderPath(collectableObject.getSpriteAnimation().getPath()));
					}
					catch (Exception e){
						System.out.println("******** No graphic data for collectable elements. Exception: " + e);
					}
				}
				collectableObjectsController.addNewcollectableObject(collectableObject);
			roundDynamicElement.getCollectableObjects().remove(roundDynamicElement.getCollectableObjects().size()-1);
			collectableObject.body.setType(BodyType.DYNAMIC);
			collectableObject.body.setGravityScale(1f);
		}
		System.out.println("New objects were appear in the world");
	}

	private void releaseAsSingleObjects(GameObject gameObject, float angleInDegrees){
		for (int i = (gameObject.getCollectableObjects().size() - 1); i >= 0; i--) {
			AbstractCollectable collectableObject = gameObject.getCollectableObjects().get(i);
			collectableObject.setNewPosition(new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y));
			collectableObject.addBodyToWorld(PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body));
			collectableObject.body.setType(BodyType.DYNAMIC);
			final float normalObjectSpeed = 200;
			if (angleInDegrees > 1 && angleInDegrees < 179) {        // Objects can jump only up
				angleInDegrees = 360 - angleInDegrees;
			}
			Vec2 linearImpulse = new Vec2(normalObjectSpeed * PApplet.cos(PApplet.radians(angleInDegrees)), -normalObjectSpeed * PApplet.sin(PApplet.radians(angleInDegrees)));
			collectableObject.body.applyLinearImpulse((PhysicGameWorld.controller.vectorPixelsToWorld(linearImpulse)), collectableObject.body.getWorldCenter(), true);

			if (collectableObject.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
				try {
					collectableObject.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObject.getSprite().getPath()));
					System.out.println("Sprite wal loaded succesfully");
				} catch (Exception e) {
					System.out.println("Graphic for collectable object can not be loaded");
				}
			}
			else if (collectableObject.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) {
				try {
					collectableObject.loadAnimation(mainGraphicController.getTilesetUnderPath(collectableObject.getSpriteAnimation().getPath()));
				} catch (Exception e) {
					System.out.println("******** No graphic data for collectable elements. Exception: " + e);
				}
			}

			collectableObjectsController.addNewcollectableObject(collectableObject);
			System.out.println("New objects were appear in the world " + collectableObject);
		}
		/*
		CollectableSack sack = new CollectableSack(new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y-gameObject.getHeight()/0.8f), AbstractCollectable.BIG_BAG, SimpleCollectableElement.DYNAMIC_BODY);
		for (int i = (gameObject.getCollectableObjects().size() - 1); i >= 0; i--) {
			sack.addObject(gameObject.getCollectableObjects().get(i));
			collectableObjectsController.getCollectableObjects().remove(gameObject.getCollectableObjects().get(i));
		}
		System.out.println("Sack has now " + sack.getObjects());
		final float normalObjectSpeed = 700;
		if (angleInDegrees > 1 && angleInDegrees < 179) {        // Objects can jump only up
			angleInDegrees = 360 - angleInDegrees;
		}
		Vec2 linearImpulse = new Vec2(normalObjectSpeed * PApplet.cos(PApplet.radians(angleInDegrees)), -normalObjectSpeed * PApplet.sin(PApplet.radians(angleInDegrees)));
		sack.body.applyLinearImpulse((PhysicGameWorld.controller.vectorPixelsToWorld(linearImpulse)), sack.body.getWorldCenter(), true);

		if (sack.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
			try {
				sack.loadSprites(mainGraphicController.getTilesetUnderPath(sack.getSprite().getPath()));
				System.out.println("Sprite for the bag loaded succesfully");
			} catch (Exception e) {
				System.out.println("Graphic for collectable object can not be loaded");
			}
		}
		else if (sack.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) {
			try {
				sack.loadAnimation(mainGraphicController.getTilesetUnderPath(sack.getSpriteAnimation().getPath()));
			} catch (Exception e) {
				System.out.println("******** No graphic data for collectable elements. Exception: " + e);
			}
		}
		gameObject.getCollectableObjects().clear();
		collectableObjectsController.addNewcollectableObject(sack);
		sack.setInWorldPosition(AbstractCollectable.IN_WORLD);
		 */



	}

	private void releaseAsSingleObjectsOld(GameObject gameObject, float angleInDegrees){
		for (int i = (gameObject.getCollectableObjects().size() - 1); i >= 0; i--) {
			AbstractCollectable collectableObject = gameObject.getCollectableObjects().get(i);
			collectableObject.setNewPosition(new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y));
			collectableObject.addBodyToWorld(PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body));
			collectableObject.body.setType(BodyType.DYNAMIC);
			final float normalObjectSpeed = 200;
			if (angleInDegrees > 1 && angleInDegrees < 179) {        // Objects can jump only up
				angleInDegrees = 360 - angleInDegrees;
			}
			Vec2 linearImpulse = new Vec2(normalObjectSpeed * PApplet.cos(PApplet.radians(angleInDegrees)), -normalObjectSpeed * PApplet.sin(PApplet.radians(angleInDegrees)));
			collectableObject.body.applyLinearImpulse((PhysicGameWorld.controller.vectorPixelsToWorld(linearImpulse)), collectableObject.body.getWorldCenter(), true);
			if (collectableObject.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
				try {
					collectableObject.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObject.getSprite().getPath()));
					System.out.println("Sprite wal loaded succesfully");
				} catch (Exception e) {
					System.out.println("Graphic for collectable object can not be loaded");
				}
			}
			else if (collectableObject.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) {
				try {
					collectableObject.loadAnimation(mainGraphicController.getTilesetUnderPath(collectableObject.getSpriteAnimation().getPath()));
				} catch (Exception e) {
					System.out.println("******** No graphic data for collectable elements. Exception: " + e);
				}
			}
			collectableObjectsController.addNewcollectableObject(collectableObject);
			System.out.println("New objects were appear in the world " + collectableObject);
		}
	}


	private void releaseAsSack(GameObject gameObject, float angleInDegrees){
		CollectableSack sack = new CollectableSack(new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y-gameObject.getHeight()/0.8f), AbstractCollectable.BIG_BAG, SimpleCollectableElement.DYNAMIC_BODY_WITH_NORMAL_GRAVITY);
		for (int i = (gameObject.getCollectableObjects().size() - 1); i >= 0; i--) {
			sack.addObject(gameObject.getCollectableObjects().get(i));
			collectableObjectsController.getCollectableObjects().remove(gameObject.getCollectableObjects().get(i));
		}
		System.out.println("Sack has now " + sack.getObjects());
		final float normalObjectSpeed = 700;
		if (angleInDegrees > 1 && angleInDegrees < 179) {        // Objects can jump only up
			angleInDegrees = 360 - angleInDegrees;
		}
		Vec2 linearImpulse = new Vec2(normalObjectSpeed * PApplet.cos(PApplet.radians(angleInDegrees)), -normalObjectSpeed * PApplet.sin(PApplet.radians(angleInDegrees)));
		sack.body.applyLinearImpulse((PhysicGameWorld.controller.vectorPixelsToWorld(linearImpulse)), sack.body.getWorldCenter(), true);

		if (sack.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
			try {
				sack.loadSprites(mainGraphicController.getTilesetUnderPath(sack.getSprite().getPath()));
				System.out.println("Sprite for the bag loaded succesfully");
			} catch (Exception e) {
				System.out.println("Graphic for collectable object can not be loaded");
			}
		}
		else if (sack.getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE) {
			try {
				sack.loadAnimation(mainGraphicController.getTilesetUnderPath(sack.getSpriteAnimation().getPath()));
			} catch (Exception e) {
				System.out.println("******** No graphic data for collectable elements. Exception: " + e);
			}
		}
		gameObject.getCollectableObjects().clear();
		collectableObjectsController.addNewcollectableObject(sack);
		sack.setInWorldPosition(AbstractCollectable.IN_WORLD);

	}

	public void releaseGameObjects(GameObject gameObject, float angleInDegrees){
		boolean withSackRealising = true;
		if (gameObject.hasAnyCollectableObjects()){
			System.out.println("This object has: " + gameObject.getCollectableObjects().size() + " objects inside");
			if (!withSackRealising || gameObject.getCollectableObjects().size()==1) {
				try {
					releaseLastCollectableGameObject(gameObject, angleInDegrees);
				}
				catch (AssertionError error) {
					System.out.println("Can not release collectable objects; Asserion error " + error.getMessage());
				}
			}
			else{
				try {
					releaseAsSack(gameObject, angleInDegrees);
				}
				catch (AssertionError error){
					System.out.println("Can not release collectable objects; Asserion error " + error.getMessage());
				}
			}
			//gameObject.getCollectableObjects().clear();
		}

	}




	

	

	



	public Person getPlayer() {
		for (Person person : persons) {
			if (person.role == Person.PLAYER) return person;
		}
		return null;
	}

	public void addNewBullet(Weapon weapon, float angle, PVector shotPosition, Person owner) {
		boolean bulletTimeActivated = isBulletTimeActivated();
		if (!usingBulletsPool ) {
			Bullet bullet = new Bullet(weapon, angle, shotPosition, Bullet.NOT_NULL_MASS, bulletTimeActivated, owner);
			bullets.add(bullet);
			if (Program.USE_3D){
				if (weapon.type == Weapon.HANDGUN || weapon.type == Weapon.SMG)	addBulletLight(bullet);
			}
		}
		else {
			boolean poolMustBeLarger = true;
			for (int i = 0; i < bullets.size(); i++){
				if (poolMustBeLarger && !bullets.get(i).isActive()){
					bullets.get(i).createFromPool(weapon, angle, shotPosition, Bullet.NOT_NULL_MASS, bulletTimeActivated, owner);
					if (Program.USE_3D){
						if (weapon.type == Weapon.HANDGUN || weapon.type == Weapon.SMG)	addBulletLight(bullets.get(i));
					}
					poolMustBeLarger = false;
					System.out.println("New bullet was recreated from pool. Actual size: " + bullets.size());
				}
			}
			if (poolMustBeLarger){
				Bullet bullet = new Bullet(weapon, angle, shotPosition, Bullet.NOT_NULL_MASS, bulletTimeActivated, owner);
				if (Program.USE_3D){
					if (weapon.type == Weapon.HANDGUN || weapon.type == Weapon.SMG)	 addBulletLight(bullet);
				}
				bullets.add(bullet);
				System.out.println("Pool must be larger. New size: " + bullets.size());
			}
		}
	}

	private boolean isBulletTimeActivated() {
		if (Program.engine.sketchRenderer() == PApplet.P3D){
			return bulletTime3DScreen.isActivated();
		}
		else return bulletTime2DScreen.isActivated();
	}

	public void clearRedundantBullets(GameCamera gameCamera) {
		if ((Program.engine.frameCount % CLEARING_OBJECTS_FRAME_FREQUENCY) == 0 && bullets.size() > 0) {
			try {
				for (int i = (bullets.size()-1); i >= 0; i--) {
					if (bullets.get(i).isActive()) {
						float distanceToCamera = Program.engine.dist(PhysicGameWorld.controller.getBodyPixelCoord(bullets.get(i).body).x, PhysicGameWorld.controller.getBodyPixelCoord(bullets.get(i).body).y, gameCamera.getActualPosition().x, gameCamera.getActualPosition().y);
						if (distanceToCamera > 2 * Program.objectsFrame.height / gameCamera.maxScale) {
							//if (Programm.DEBUG) System.out.println("Bullet №" + i + " has not in showing area and must be deleted");
							//if (bullets.get(i).body != null) bullets.get(i).killBody();
							//bullets.get(i).body.setActive(false);
							//PhysicGameWorld.controller.destroyBody(bullets.get(i).body);
							//Bullet bulletToBeDeleted = bullets.get(i);
							//bulletsPool.addObject(bulletToBeDeleted);
							//bullets.remove(bulletToBeDeleted);
							bullets.get(i).setActive(false);

						} else if (bullets.get(i).mustBeBulletDeletedAfterLongFlight()) {
							if (Program.debug)
								System.out.println("Bullet №" + i + " has to long flying and must be deleted");
							//if (bullets.get(i).body != null) bullets.get(i).killBody();
						/*
						bullets.get(i).body.setActive(false);
						PhysicGameWorld.controller.destroyBody(bullets.get(i).body);
						bullets.remove(bullets.get(i));
						*/
							//Bullet bulletToBeDeleted = bullets.get(i);
							//bulletsPool.addObject(bulletToBeDeleted);
							//bullets.remove(bulletToBeDeleted);
							bullets.get(i).setActive(false);
							//return;
						} else {
							int velocityInPixels = Program.engine.floor(new PVector(
									PhysicGameWorld.controller.scalarWorldToPixels(bullets.get(i).body.getLinearVelocity().x),
									PhysicGameWorld.controller.scalarWorldToPixels(bullets.get(i).body.getLinearVelocity().y)).mag());
							if (velocityInPixels < 10) {
								if (Program.debug)
									System.out.println("Bullet №" + i + " has low velocity value and must be deleted");
								//PhysicGameWorld.clearContactsWithBody(bullets.get(i).body);
								//Bullet bulletToBeDeleted = bullets.get(i);
								//bulletsPool.addObject(bulletToBeDeleted);
								//bullets.remove(bulletToBeDeleted);
							/*
							bullets.get(i).body.setActive(false);
							PhysicGameWorld.controller.destroyBody(bullets.get(i).body);
							bullets.remove(bullets.get(i));
							*/
								//return;
								bullets.get(i).setActive(false);
							}
						}
					}
				}
			}
			catch (Exception e) {
				System.out.println("Can not clear bullets");
			}
		}
		//System.out.println("In the world : " + PhysicGameWorld.getBodies().size() + " bodies");
	}

	/*
	public void clearRedundantRoundElements() {		
		for (int i = 0; i < roundElements.size(); i++) {
			if (roundElements.get(i).getLife() <= 0) {
				System.out.println("Killed!");
				roundElements.remove(i);
				return;
			}
		}
	}

	 */

	/*
	void clearRedundantSprings() {
		//System.out.println("springs.size():" + springs.size());
		// springs can not be deleted
		//System.out.println("Springs: " + springs.size());
	    //if (Game2D.engine.frameCount % CLEARING_OBJECTS_FRAME_FREQUENCY == 2) {
			for (int i = 0; i < springs.size(); i++) {
				if (springs.get(i).mustBeDeleted()) {
					System.out.println("Springs: " + springs.size());
					springs.remove(springs.get(i));	
					return;
				}
			}
	    //}		
	}
	*/

	/*	transfered to the specific class
	public void clearEndedExplosions() {
		for (Explosion explosion : explosions) {
			if (explosion.isEnded() == true) {
				explosions.remove(explosion);
				return;
			}
		}
	}
	*/
	
	public void clearObjectsToBeDeleted(){
		if (objectsToBeDeleted.size()>0){
			for (GameObject gameObject : objectsToBeDeleted){
				try{
					gameObject.body.setActive(false);
					PhysicGameWorld.controller.destroyBody(gameObject.body);
				}
				catch (Exception e){
					System.out.println("Can not remove objects from the world");
				}
			}
		}
	}



	public void addNewLaunchableGreenade(FirearmsWeapon weapon, float angle, PVector shotPosition) {
		shotPosition.add(weapon.getBulletEndPlace(weapon, angle));
		if (!PhysicGameWorld.arePointInAnyBody(shotPosition)) {
			launchableWhizbangsController.addNewLaunchableGreenade(new LaunchableGrenade(shotPosition, angle));
		}
		else {
			if (Program.debug) System.out.println("Greenade is in somebody/something");
		}
	}

	public void addNewHandGreenade(FirearmsWeapon weapon, float angle, PVector shotPosition) {
		shotPosition.add(weapon.getBulletEndPlace(weapon, angle));
		if (!PhysicGameWorld.arePointInAnyBody(shotPosition)) {
			System.out.println("Hand grenade was created");
			launchableWhizbangsController.addNewHandGreenade(new HandGrenade(shotPosition, angle, launchableWhizbangsController));
		}
		else {
			if (Program.debug) System.out.println("Hand Greenade is in somebody/something");
		}
	}



	public void addNewDragonFire(Person person) {
		launchableWhizbangsController.addNewDragonFire(person);
	}

    public ArrayList<RoundPipe> getRoundPipes() {
		return roundPipes;
    }

	public ArrayList<RoundRotatingStick> getRoundRotatingSticks() {
		return roundRotatingSticks;
	}

	public ArrayList<Bridge> getBridges() {
		return bridges;
	}

	public void removePerson(int j) {
		persons.remove(j);
	}

	public void removePerson(Person personToBeRemoved) {
		for (int i = (persons.size()-1); i>= 0; i--){
			if (persons.get(i).equals(personToBeRemoved)){
				persons.remove(i);
			}
		}
	}


	public void addNewGameObject(SingleFlagZone zone){
		if (zone.getClass() == ObjectsClearingZone.class) objectsClearingZones.add((ObjectsClearingZone)zone);
		else if (zone.getClass() == EndLevelZone.class) endLevelZones.add((EndLevelZone) zone);
		else if (zone.getClass() == CameraFixationZone.class) cameraFixationZones.add((CameraFixationZone) zone);
		else {
			System.out.println("There are no data about this zone type");
		}
	}

	public void addNewGameObject(MoveablePlatformsController controller){
		//controller.loadSprites(mainGraphicController.getTilesetUnderPath(controller.getSprite().getPath()));
		moveablePlatformsControllers.add(controller);

	}

	public void addNewGameObject(PipePortal portal){
		portals.add(portal);
	}

	public void addNewGraphic(IndependentOnScreenGraphic gameObject) {
		if (gameObject instanceof IndependentOnScreenStaticSprite) {
			IndependentOnScreenStaticSprite sprite = (IndependentOnScreenStaticSprite) gameObject;
			sprite.staticSprite.loadSprite (mainGraphicController.getTilesetUnderPath(sprite.staticSprite.getPath()));
			independentOnScreenSprites.add(sprite);
			System.out.println("Static sprite was added ");
		}

		else if (gameObject instanceof IndependentOnScreenAnimation) {
			IndependentOnScreenAnimation sprite = (IndependentOnScreenAnimation) gameObject;
			sprite.spriteAnimation.loadAnimation (mainGraphicController.getTilesetUnderPath(sprite.spriteAnimation.getPath()));
			independentOnScreenAnimations.add(sprite);
			System.out.println("Animation was added ");
		}
	}

	public void addNewGameObject(Bridge gameObject) {
		bridges.add(gameObject);
	}

	public void addNewGameObject(GameObject gameObject) {
		boolean withIncapsulation = true;
		if (withIncapsulation) {
			if (gameObject instanceof Person) {
				Person person = (Person) gameObject;
				person.loadAnimationData(mainGraphicController);
				persons.add(person);
				if (Program.WITH_LIFE_LINES) hud_lifeLinesController.addHudLifeLineForNewPersons(this, person);
				levelResultsSaveController.addEnemiesToLevel(1);
			}
		}
		if (withIncapsulation){
			if (gameObject.getClass() == RoundPipe.class) {
				RoundPipe roundPipe = (RoundPipe) gameObject;
				if (roundPipe.hasGraphic()) roundPipe.loadSprites(mainGraphicController.getTilesetUnderPath(roundPipe.getSprite().getPath()));
				roundPipes.add(roundPipe);
			}
			else if (gameObject instanceof SimpleCollectableElement) {
				AbstractCollectable collectableObject = (AbstractCollectable) gameObject;
				collectableObjectsController.addNewcollectableObject(collectableObject);
				try {
					collectableObjectsController.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObjectsController.getSpriteAnimation(collectableObjectsController.getObjectsNumber() - 1).getPath()), (collectableObjectsController.getObjectsNumber() - 1));
				}
				catch (Exception e){
					System.out.println("Can not load graphic for new simple collectable; " + e);
				}
				System.out.println("new Collectable element was added");
			}
			else if (gameObject.getClass() == CollectableObjectInNesStyle.class) {
				CollectableObjectInNesStyle collectableObject = (CollectableObjectInNesStyle) gameObject;
				collectableObjectsController.addNewcollectableObject(collectableObject);
				try {
					collectableObjectsController.loadSprites(mainGraphicController.getTilesetUnderPath(collectableObjectsController.getSpriteAnimation(collectableObjectsController.getObjectsNumber() - 1).getPath()), (collectableObjectsController.getObjectsNumber() - 1));
				}
				catch (Exception e){
					System.out.println("Can not load graphic for new object; " + e);
				}
			}
			else if (gameObject instanceof RoundElement){
				RoundElement roundElement = (RoundElement) gameObject;
				try {
					if (roundElement.hasGraphic())
						roundElement.loadSprites(mainGraphicController.getTilesetUnderPath(roundElement.getSprite().getPath()));
					roundElements.add(roundElement);
				}
				catch ( Exception e){
					System.out.println("Can not add object of type: " + gameObject.getClass() + " to the world; " + e);
				}
			}
		}


	}

    public GameObject getGameObjectByBody(Body testBody) {
		for (RoundElement roundElement : roundElements){
			if (roundElement.body.equals(testBody)) return roundElement;
		}
		for (Person person : persons){
			if (person.body.equals(testBody)) return person;
		}
		for (AbstractCollectable collectableObject : collectableObjectsController.getCollectableObjects()){
			if (collectableObject.body.equals(testBody)) return collectableObject;
		}
		for (RoundPipe roundPipe : roundPipes){
			if (roundPipe.body.equals(testBody)) return roundPipe;
		}
		for (MoveablePlatformsController moveablePlatformsControllers : moveablePlatformsControllers) {
			for (MovablePlatform movablePlatform : moveablePlatformsControllers.getPlatforms()) {
				if (movablePlatform.body.equals(testBody)) return movablePlatform;
			}
		}
		System.out.println("can not find any game object on this point");
		return null;
    }

    private void addNewDust(Vec2 point, float angle){

	}

	public ArrayList<IndependentOnScreenStaticSprite> getIndependentOnScreenStaticSprites() {
		return independentOnScreenSprites;
	}

	public ArrayList<IndependentOnScreenAnimation> getIndependentOnScreenAnimations() {
		return independentOnScreenAnimations;
	}

	public MainGraphicController getMainGraphicController() {
		return mainGraphicController;
	}

	public HUD_LifeLinesController getHud_lifeLinesController(){
		return hud_lifeLinesController;
	}

	public LaunchableWhizbangsController getLaunchableWhizbangsController() {
		return launchableWhizbangsController;
	}

    public void addGraphicElement(IndependentOnScreenGraphic graphicElement) {
		if (graphicElement.getClass() == IndependentOnScreenStaticSprite.class) {
			IndependentOnScreenStaticSprite newSprite = (IndependentOnScreenStaticSprite) graphicElement;
			independentOnScreenSprites.add(newSprite);
		}
		else if (graphicElement.getClass() == IndependentOnScreenAnimation.class) {
			IndependentOnScreenAnimation newAnimation = (IndependentOnScreenAnimation) graphicElement;
			independentOnScreenAnimations.add(newAnimation);
		}
		else {
			System.out.println("This graphic element " + graphicElement.getClassName() + " can not be added to the game world");
		}
    }

    public void clearObjects(String which) {
		if (which == "All"){
			try {
				for (int i = (persons.size()-1); i >= 0; i--) {
					if (persons.get(i).equals(getPlayer())) {
						persons.get(i).transferTo(new PVector(0, 0));
					} else {
						persons.remove(persons.get(i));
					}
				}
			}
			catch (Exception e){
				System.out.println("Can not delete persons " + e);
			}
			//persons.clear();
			roundElements.clear();
			independentOnScreenSprites.clear();
			independentOnScreenAnimations.clear();
			bridges.clear();
			portals.clear();

			roundRotatingSticks.clear();
			moveablePlatformsControllers.clear();
			roundPipes.clear();

			objectsClearingZones.clear();
			objectsAppearingZones.clear();
			endLevelZones.clear();
			jumpingLavaBallsControllers.clear();
			collectableObjectsController.getCollectableObjects().clear();
			backgrounds.clear();
			//bridge = null;
			System.out.println("Objects were deleted");
		}

    }

	public SingleGameElement getObjectOnScreenByPosition(Vec2 nearestPoint) {
		return null;
	}

	private void deletePerson(Person personToBeDeleted){
		for (int i = (persons.size()-1); i >= 0; i--){
			if (persons.get(i).equals(personToBeDeleted)) {
				persons.get(i).body.setActive(false);
				PhysicGameWorld.controller.destroyBody(persons.get(i).body);
				persons.remove(persons.get(i));
			}
		}
	}

	private void deleteRoundElement(RoundElement roundElementToBeDeleted){
		for (int i = (roundElements.size()-1); i >= 0; i--){
			if (roundElements.get(i).equals(roundElementToBeDeleted)) {
				roundElements.get(i).body.setActive(false);
				PhysicGameWorld.controller.destroyBody(roundElements.get(i).body);
				roundElements.remove(roundElements.get(i));
				return;
			}
		}
	}

	private void deleteCollectableObject(CollectableObjectInNesStyle collectableObject){
		for (int i = (collectableObjectsController.getCollectableObjects().size()-1); i >= 0; i--){
			if (collectableObjectsController.getCollectableObjects().get(i).equals(collectableObject)) {
				collectableObjectsController.getCollectableObjects().get(i).body.setActive(false);
				PhysicGameWorld.controller.destroyBody(collectableObjectsController.getCollectableObjects().get(i).body);
				collectableObjectsController.getCollectableObjects().remove(collectableObjectsController.getCollectableObjects().get(i));
				return;
			}
		}
	}

	private void deleteCollectableObject(SimpleCollectableElement collectableObject){
		for (int i = (collectableObjectsController.getCollectableObjects().size()-1); i >= 0; i--){
			if (collectableObjectsController.getCollectableObjects().get(i).equals(collectableObject)) {
				collectableObjectsController.getCollectableObjects().get(i).body.setActive(false);
				PhysicGameWorld.controller.destroyBody(collectableObjectsController.getCollectableObjects().get(i).body);
				collectableObjectsController.getCollectableObjects().remove(collectableObjectsController.getCollectableObjects().get(i));
				return;
			}
		}
	}

	private void deleteIndependentOnScreenGraphicRoundElement(IndependentOnScreenGraphic graphicElementToBeDeleted){
		for (int i = (independentOnScreenAnimations.size()-1); i >= 0; i--){
			if (independentOnScreenAnimations.get(i).equals(graphicElementToBeDeleted)) {
				independentOnScreenAnimations.remove(independentOnScreenAnimations.get(i));
				return;
			}
		}
		for (int i = (independentOnScreenSprites.size()-1); i >= 0; i--){
			if (independentOnScreenSprites.get(i).equals(graphicElementToBeDeleted)) {
				independentOnScreenSprites.remove(independentOnScreenSprites.get(i));
				return;
			}
		}
	}

    public void deleteObjectsFromMap(SingleGameElement selectedElement) {
		if (selectedElement instanceof Person){
			Person personToBeDeleted = (Person) selectedElement;
			if (personToBeDeleted.hasAnyCollectableObjects()){
				personToBeDeleted.getCollectableObjects().clear();
			}
			deletePerson(personToBeDeleted);
			System.out.println("Objects were deleted from the world");
		}
		else if (selectedElement instanceof CollectableObjectInNesStyle){	//Collectable object is instance of RoundElement !!! not logical
			CollectableObjectInNesStyle collectableObject = (CollectableObjectInNesStyle) selectedElement;
			deleteCollectableObject(collectableObject);
			System.out.println("collectable  Objects were deleted from the world");
		}
		else if (selectedElement instanceof SimpleCollectableElement){	//Collectable object is instance of RoundElement !!! not logical
			SimpleCollectableElement collectableObject = (SimpleCollectableElement) selectedElement;
			deleteCollectableObject(collectableObject);
			System.out.println("collectable  Objects were deleted from the world");
		}
		else if (selectedElement instanceof RoundElement){
			RoundElement roundElementToBeDeleted = (RoundElement) selectedElement;
			if (roundElementToBeDeleted.hasAnyCollectableObjects()){
				roundElementToBeDeleted.getCollectableObjects().clear();
			}
			deleteRoundElement(roundElementToBeDeleted);
			System.out.println("Round elements were deleted from the world");
		}

		else if (selectedElement instanceof IndependentOnScreenGraphic){
			IndependentOnScreenGraphic graphicToBeDeleted = (IndependentOnScreenGraphic) selectedElement;
			deleteIndependentOnScreenGraphicRoundElement(graphicToBeDeleted);
			System.out.println("Graphic objects were deleted from the world");
		}
		else if (selectedElement instanceof Portal){
			Portal portal = (Portal) selectedElement;
			deletePortal(portal);
		}
		else if (selectedElement instanceof ObjectsClearingZone){
			ObjectsClearingZone objectClearingZone = (ObjectsClearingZone) selectedElement;
			deleteZone(objectClearingZone);
		}
		else if (selectedElement instanceof MessageAddingZone){
			MessageAddingZone objectClearingZone = (MessageAddingZone) selectedElement;
			deleteZone(objectClearingZone);
		}
		else if (selectedElement instanceof CameraFixationZone){
			CameraFixationZone ca = (CameraFixationZone) selectedElement;
			deleteZone(ca);
		}
		else System.out.println("Selected element can not be deleted from the world. Fill function:  deleteObjectsFromMap(SingleGameElement selectedElement)");

    }

	private void deleteZone(SingleFlagZone zoneToBeDeleted) {
		for (int i = (objectsClearingZones.size()-1); i >= 0; i--){
			if (objectsClearingZones.get(i).equals(zoneToBeDeleted)){
				objectsClearingZones.remove(zoneToBeDeleted);
				return;
			}
		}
		for (int i = (messageAddingZones.size()-1); i >= 0; i--){
			if (messageAddingZones.get(i).equals(zoneToBeDeleted)){
				messageAddingZones.remove(zoneToBeDeleted);
				return;
			}
		}
		for (int i = (cameraFixationZones.size()-1); i >= 0; i--){
			if (cameraFixationZones.get(i).equals(zoneToBeDeleted)){
				cameraFixationZones.remove(zoneToBeDeleted);
				return;
			}
		}
	}

	private void deletePortal(Portal toBeDeleted) {
		for (int i = (portals.size()-1); i >= 0; i--){
			if (portals.get(i).equals(toBeDeleted)){
				portals.remove(i);
				System.out.println("Portal was deleted");
				break;
			}
		}
		for (int i = (portalsToAnotherLevel.size()-1); i >= 0; i--){
			if (portalsToAnotherLevel.get(i).equals(toBeDeleted)){
				portalsToAnotherLevel.remove(i);
				System.out.println("Portal to another level was deleted");
				break;
			}
		}
		for (int i = (simplePortals.size()-1); i >= 0; i--){
			if (simplePortals.get(i).equals(toBeDeleted)){
				simplePortals.remove(i);
				System.out.println("Simple portal was deleted");
				break;
			}
		}
	}

	public void addGameOverScreen() {
		if (!isGameOverScreenShown()) {
			if (withEndLevelScreen) {
				titles.add(new EndLevelTitle(EndLevelTitle.GAME_OVER_TITLE_PATH, 4000, 4000, (int) (Program.engine.width * 0.85f), 0, SimpleTitle.WITH_APPEARING));
				System.out.println("New game over screen was added;" + titles.size());
			}
			setGameOver();
			musicController.pausePlay();
			musicController.setPlayOnce(MusicInGameController.ONCE);
			//musicController.removeFromMemory();
			soundController.setAndPlayAudio(SoundsInGame.PLAYER_DEAD);
			soundController.noMoreAudio();
		}
	}

	/*
	public boolean isGameOverScreenShown() {
		return gameOverScreenShown;
	}*/

	public void addWinScreen() {
		if (withEndLevelScreen) {
			System.out.println("New win screen was added");
			titles.add(new EndLevelTitle(EndLevelTitle.WIN_TITLE_PATH, 5000, 4000, (int) (Program.engine.width * 0.85f), 0, SimpleTitle.WITH_APPEARING));

		}
	}

	public void setLevelWon(EndLevelZone endLevelZone) {
		if (!playerWins && !playerLoosed) {
			playerWins = true;
			musicController.pausePlay();
			//musicController.stPlay();
			musicController.setPlayOnce(MusicInGameController.ONCE);
			//musicController.removeFromMemory();
			musicController = new MusicInGameController("Player win 1.wav", false, 1.0f);
			musicController.startToPlay();
			musicController.setPlayOnce(MusicInGameController.ONCE);
			getPlayer().recoveryLifeInPercent(100);
			if (levelType == ExternalRoundDataFileController.MAIN_LEVELS) {
				PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
				playerProgressSaveMaster.setLastCompletedLevel(endLevelZone.getCompletedLevel());
				playerProgressSaveMaster.setNextZone(levelNumber, endLevelZone.getNextZone());
				playerProgressSaveMaster.addListOfNotMoreUploadableObjects(idsToBeNotMoreShown);
				playerProgressSaveMaster.writeValuesWithoutSaving();
				playerProgressSaveMaster.saveOnDisk();
				System.out.println("Player progress was updated. Level was: " + playerProgressSaveMaster.getLastCompletedLevel() + "; next zone: " + playerProgressSaveMaster.getNextZone());
				saveResultsForActualRound();
				int firstZoneInRound = endLevelZone.getFirstZoneOfRound();
				LevelResultsCalculator levelResultsCalculator = new LevelResultsCalculator(firstZoneInRound, levelNumber);
				System.out.println("Data for rounds from : "+ firstZoneInRound + " to " + levelNumber + " is " + levelResultsCalculator);
			}

			System.out.println("An another music for level won must be added");
		}

	}

	public void saveResultsForActualRound(){
		levelResultsSaveController.updateData(this, Program.engine);
		//levelResultsController.fillResultsForActualZone(this, Program.engine);
		levelResultsSaveController.saveData();
		System.out.println("Result: " + levelResultsSaveController.toString());
	}

	public void setGameOver() {
		if (!playerLoosed && !playerWins) {
			playerLoosed = true;
			if (levelType == ExternalRoundDataFileController.MAIN_LEVELS){
				PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
				playerProgressSaveMaster.decrementRestLifes();
				playerProgressSaveMaster.saveOnDisk();
				System.out.println("Player has now " + playerProgressSaveMaster.getRestLifes() + " lifes in company mode");
			}
		}
		for (Person person : persons){
			if (person.getClass() == BossBoar.class){
				BossBoar boar = (BossBoar)person;
				boar.playerCanNotKillBoar();
				break;
			}
		}

	}

	public boolean isLevelEnds(){
		if (playerLoosed) return true;
		else if (playerWins) return true;
		else return false;
	}

	public boolean isPlayerWon(){
		return playerWins;
	}

	public boolean isPlayerLoosed(){
		return playerLoosed;
	}

	public ArrayList<MoveablePlatformsController> getMoveablePlatformsControllers() {
		return moveablePlatformsControllers;
	}


	public void contactPreSolveInterrupt(Contact arg0, Manifold arg1) {
		Body bodyA = arg0.m_fixtureA.getBody(), bodyB = arg0.m_fixtureB.getBody();
		boolean firstBodyIsBullet = false;
		boolean secondBodyIsBullet = false;
				//bullet or whizbang
		if (bodyA.getUserData() != null){			//has data
			if (bodyA.getUserData() == Bullet.BULLET){		////bullet
				firstBodyIsBullet = true;
			}
		}
		if (!firstBodyIsBullet){
			if (bodyB.getUserData() != null){			//has data
				if (bodyB.getUserData() == Bullet.BULLET){		////bullet
					secondBodyIsBullet = true;
				}
			}
		}
		if (firstBodyIsBullet || secondBodyIsBullet){
			if (firstBodyIsBullet){
				if (bodyB.isFixedRotation()) {
					if (PhysicGameWorld.getGameObjectByBody(this, bodyB) instanceof Person) {
						bodyB.setFixedRotation(false);
						bodyB.resetMassData();
					}
				}
				if (bodyB.getType() == BodyType.STATIC){
					try{
						if (PhysicGameWorld.getGameObjectByBody(this, bodyB) instanceof RoundBox){
							Bullet bullet = getBulletByBody(bodyA);
							if (willBeCrushedByColision((RoundBox)PhysicGameWorld.getGameObjectByBody(this, bodyA), bullet)) {
								bodyB.m_type = BodyType.DYNAMIC;
								bodyB.setType(BodyType.DYNAMIC);
								bodyB.resetMassData();
							}
						}
					}
					catch (Exception e){
						System.out.println("Can not determine roune element");
					}
				}
			}
			else {
				if (bodyA.isFixedRotation()) {
					if (PhysicGameWorld.getGameObjectByBody(this, bodyA) instanceof Person) {
						bodyA.setFixedRotation(false);
						bodyA.resetMassData();
					}
				}
				if (bodyA.getType() == BodyType.STATIC){
					try{
						if (PhysicGameWorld.getGameObjectByBody(this, bodyA) instanceof RoundBox){
							Bullet bullet = getBulletByBody(bodyB);
							if (willBeCrushedByColision((RoundBox)PhysicGameWorld.getGameObjectByBody(this, bodyA), bullet)) {
								bodyA.m_type = BodyType.DYNAMIC;
								bodyA.setType(BodyType.DYNAMIC);
								bodyA.resetMassData();
							}
						}
					}
					catch (Exception e){
						System.out.println("Can not determine round element");
					}
				}
			}
		}

		//else{
			collectableObjectsController.updatePreContactWithSomeBody(bodyA, bodyB, this);
		//}
	}

	private Bullet getBulletByBody(Body body) {
		try{
			for (Bullet bullet : bullets){
				if (bullet.body.equals(body)){
					return bullet;
				}
			}
		}
		catch (Exception e){
			System.out.println("Can not find bullet with thiw body " + body);
		}
		return null;
	}

	private boolean willBeCrushedByColision(RoundBox gameObject, Bullet bullet) {
		if (bullet != null) {
			if (gameObject.willBeKilledByAttack(Weapon.getNormalAttackPower(bullet.fromWeapon))) {
				System.out.println("This object will be crushed");
				return true;
			}
		}
		return false;
	}

	public void addDebugGraphic(DebugGraphic debugGraphic){
		if (Program.debug){
			debugGraphics.add(debugGraphic);
		}
	}


	public HittingController getHittingController() {
		return hittingController;
	}

	public void drawDebugGraphic(GameCamera gameCamera) {
		for (int i = 0; i < debugGraphics.size(); i++){
			debugGraphics.get(i).draw(gameCamera);
		}
	}



	public void addSplash(Splash splash){
		/*System.out.println("We have already " + splashes.size());
		boolean foundedInPull = false;
		for (Splash alreadyExistingSplash : splashes){
			if (alreadyExistingSplash.getClass() == splash.getClass()){
				System.out.println("There are already a splash with the same type in pool");
				if (alreadyExistingSplash.isEnded()){
					//alreadyExistingSplash.recreate(splash);
					foundedInPull = true;
					break;
				}
			}
		}
		if (!foundedInPull) {*/
			splashes.add(splash);
		//}
	}

    public void addBackground(Background background) {
		backgrounds.add(background);
    }

    public ArrayList <Background> getBackgrounds(){
		return backgrounds;
	}

	public boolean isGameOverScreenShown() {
		if (!playerLoosed) return false;
		else return true;
	}

	public ArrayList <DebugGraphic> getDebugGraphics(){
		return debugGraphics;
	}


	public ArrayList<PortalToAnotherLevel> getPortalsToAnotherLevel() {
		return portalsToAnotherLevel;
	}

    public void clearAllObjects() {
		backgrounds.clear(); System.out.println("Background were cleared");
		persons.clear();
		roundElements.clear();
		independentOnScreenAnimations.clear();
		independentOnScreenSprites.clear();
		roundPipes.clear();
		bullets.clear();
    }



	public void updateLogicZones(PlayerControl playerControl, GameCameraController gameCameraController) {
		for (MessageAddingZone messageAddingZone : messageAddingZones){
			messageAddingZone.update(this, playerControl, gameCameraController);
		}
		for (CameraFixationZone zone: cameraFixationZones){
			zone.update(this, gameCameraController);
		}
		for (PortalToAnotherLevel portals : portalsToAnotherLevel){
			portals.update(this);
		}
	}

	public void addMessage(SMS message, PlayerControl playerControl) {
		playerControl.addMessageToHud(message, this);
	}

	public void addMessage(MessageAddingZone messageAddingZone) {
		messageAddingZones.add(messageAddingZone);
	}

	public boolean isUpperPanelFreeFromMessages(){
		return playerControl.isUpperPanelFreeFromMessages();
	}

    public void attackPlayer(Person attackingPerson) {
		hittingController.attackPlayer((Soldier) getPlayer(), this, attackingPerson);
    }

	public boolean isPointInSomeDeadZones(float testX, float testY) {
		for (ObjectsClearingZone zone : objectsClearingZones){
			if (zone.getFlag().inZone(testX, testY)) return true;
		}
		return false;
	}

	public boolean areThereActiveBullets() {
		for (Bullet bullet : bullets){
			if (bullet.isActive()) return true;
		}
		return false;
	}

	public boolean areThereActiveExplosions() {
		for (Explosion explosion : explosions){
			if (!explosion.isEnded()) return true;
		}
		return false;
	}

	public boolean isPointInSomeRoundElements(float testX, float testY) {
		for (RoundElement roundElement : roundElements){
			if (roundElement.body.getFixtureList().testPoint(new Vec2(testX, testY))){
				return true;
			}
		}
		return false;
	}

	public boolean isPointInSomeRoundElements(Vec2 testPointInWorldCoordinates) {
		for (RoundElement roundElement : roundElements){
			if (roundElement.body.getFixtureList().testPoint(testPointInWorldCoordinates)){
				return true;
			}
		}
		return false;
	}

	public GameObject getGameObjectByCoordinate(int x, int y) {
		Body body = PhysicGameWorld.getBodyAtPoint(new PVector(x, y));
		if (body != null){
			GameObject gameObject = PhysicGameWorld.getGameObjectByBody(this, body);
			if (gameObject != null){
				return gameObject;
			}
			else {
				System.out.println("Can not find an object by body");
				return null;
			}
		}
		else return null;
	}

    public IndependentOnScreenGraphic getGraphicAtPoint(PVector freePlace) {
		for (IndependentOnScreenStaticSprite staticSprite : independentOnScreenSprites){
			if (staticSprite.isPointOnElement(freePlace)){
				return staticSprite;
			}
		}
		for (IndependentOnScreenAnimation animation : independentOnScreenAnimations){
			if (animation.isPointOnElement(freePlace)){
				return animation;
			}
		}
		return null;
    }

	public ArrayList<SingleGameElement> getObjectsInRect(float x1, float y1, float x2, float y2) {
		ArrayList <SingleGameElement > selectedElementsOnScreen = new ArrayList<>();
		PVector majorRectCenter = new PVector(x1+(x2-x1)/2, y1+(y2-y1)/2);
		float majorWidth = x2-x1;
		float majorHeight = y2-y1;
		for (RoundElement roundElement : roundElements){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, roundElement.getPixelPosition().x, roundElement.getPixelPosition().y, majorWidth, majorHeight, roundElement.getWidth(), roundElement.getHeight())){
				SingleGameElement singleGameElement = roundElement;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (IndependentOnScreenStaticSprite graphicElement : independentOnScreenSprites){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, graphicElement.getPosition().x, graphicElement.getPosition().y, majorWidth, majorHeight, graphicElement.getWidth(), graphicElement.getHeight())){
				SingleGameElement singleGameElement = graphicElement;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (IndependentOnScreenAnimation graphicElement : independentOnScreenAnimations){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, graphicElement.getPosition().x, graphicElement.getPosition().y, majorWidth, majorHeight, graphicElement.getWidth(), graphicElement.getHeight())){
				SingleGameElement singleGameElement = graphicElement;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (RoundPipe pipes : roundPipes){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, pipes.getPixelPosition().x, pipes.getPixelPosition().y, majorWidth, majorHeight, pipes.getWidth(), pipes.getHeight())){
				SingleGameElement singleGameElement = pipes;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (Person person : persons){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, person.getPixelPosition().x, person.getPixelPosition().y, majorWidth, majorHeight, person.getWidth(), person.getHeight())){
				if (!person.equals(getPlayer())) {
					SingleGameElement singleGameElement = person;
					selectedElementsOnScreen.add(singleGameElement);
				}
			}
		}
		for (Bridge bridge : bridges){
			for (RoundElement roundElement : bridge.getSegments()) {
				if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, roundElement.getPixelPosition().x, roundElement.getPixelPosition().y, majorWidth, majorHeight, roundElement.getWidth(), roundElement.getHeight())) {
					if (!bridge.equals(getPlayer())) {
						SingleGameElement singleGameElement = roundElement;
						selectedElementsOnScreen.add(singleGameElement);
					}
				}
			}
		}
		for (AbstractCollectable collectableElement : collectableObjectsController.getCollectableObjects()){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, collectableElement.getPixelPosition().x, collectableElement.getPixelPosition().y, majorWidth, majorHeight, collectableElement.getWidth(), collectableElement.getHeight())){
				SingleGameElement singleGameElement = collectableElement;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (Portal portal : portals){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, portal.getEnter().getPosition().x, portal.getEnter().getPosition().y, majorWidth, majorHeight, portal.enter.getWidth(), portal.enter.getHeight())){
				SingleGameElement singleGameElement = portal;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}

		for (ObjectsClearingZone zone : objectsClearingZones){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, zone.getFlag().getPosition().x, zone.getFlag().getPosition().y, majorWidth, majorHeight, zone.getFlag().getWidth(), zone.getFlag().getHeight())){
				SingleGameElement singleGameElement = zone;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (MessageAddingZone zone : messageAddingZones){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, zone.getFlag().getPosition().x, zone.getFlag().getPosition().y, majorWidth, majorHeight, zone.getFlag().getWidth(), zone.getFlag().getHeight())){
				SingleGameElement singleGameElement = zone;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		for (CameraFixationZone zone : cameraFixationZones){
			if (GameMechanics.isIntersectionBetweenAllignedRects(majorRectCenter, zone.getFlag().getPosition().x, zone.getFlag().getPosition().y, majorWidth, majorHeight, zone.getFlag().getWidth(), zone.getFlag().getHeight())){
				SingleGameElement singleGameElement = zone;
				selectedElementsOnScreen.add(singleGameElement);
			}
		}
		System.out.println("On screen founded: " + selectedElementsOnScreen.size() + " elements.");
		return selectedElementsOnScreen;
	}

	public void addPreContact(Contact arg0, Manifold arg1) {
		collectableObjectsController.preContact(arg0, arg1);
	}

	public void addNewDissolvingText(DissolvingAndUpwardsMovingText text){
		System.out.println("New text was added ");
		dissolvingAndUpwardsMovingTexts.add(text);
	}

	public ArrayList<DissolvingAndUpwardsMovingText> getDissolvingAndUpwardsMovingTexts() {
		return dissolvingAndUpwardsMovingTexts;
	}

	public void setPlayer(Soldier savedPlayer) {
		for (int i = (persons.size()-1); i >= 0; i--){
			if (persons.get(i).getClass() == Soldier.class){
				persons.remove(i);
				break;
			}
		}
		persons.add(savedPlayer);
	}

	public void clearAllObjectsWithoutPlayer() {
	}

	public SoundInGameController getSoundController() {
		return soundController;
	}

	public void addShootingFlash(Person person) {
		afterShotFlashController.addNewFlash(person);
	}

	public void createBulletSleeveGraphicForType(Soldier player, int graphicType) {
		moveableSpritesAddingController.createBulletSleeveGraphicForType(player, graphicType, this);
	}

	public ArrayList<IndependentOnScreenMovableSprite> getMoveableSprites() {
		return moveableSprites;
	}

	public void endActualLevel() {
		musicController.pausePlay();
		//EnemiesAnimationController.clearTilesetsArray();
	}

	public boolean isObjectPartOfSomeBridge(SingleGameElement toBeDeleted) {
		for (Bridge bridge : bridges){
			if (bridge.isObjectPart(toBeDeleted)){
				return true;
			}
		}
		return false;
	}

	public void switchOffMusic() {
		if (soundController != null){
			soundController.stopAllAudio();
		}
		if (musicController != null) {
			musicController.pausePlay();
		}
	}


	public ArrayList<OnDisplayText> getOnDisplayTexts() {
		if (onDisplayTexts == null){
			onDisplayTexts = new ArrayList<>();
		}
		return onDisplayTexts;
	}

	public ArrayList<PipePortal> getPortals() {
		return portals;
	}

	public ArrayList<SimplePortal> getSimplePortals() {
		return simplePortals;
	}


	public ArrayList<MessageAddingZone> getMessageAddingZones() {
		return messageAddingZones;
	}

	public ArrayList<ObjectsClearingZone> getObjectsClearingZones() {
		return objectsClearingZones;
	}

	public ArrayList<CameraFixationZone> getCameraFixationZones() {
		return cameraFixationZones;
	}

	public void sentObjectToArrayStart(SingleGameElement selectedElement) {
		changeObjectInArrayPos(selectedElement, false);
	}

	public void sentObjectToArrayEnd(SingleGameElement selectedElement) {
		changeObjectInArrayPos(selectedElement, true);
	}

	private void changeObjectInArrayPos(SingleGameElement object, boolean toArrayStart){
		if (object instanceof IndependentOnScreenGraphic){
			System.out.println("It is graphic");
			int actualPos = -1;
			if (object.getClass() == IndependentOnScreenStaticSprite.class){
				for (int i = 0; i < independentOnScreenSprites.size(); i++){
					if (independentOnScreenSprites.get(i).equals(object)){
						actualPos = i;
						System.out.println("Object founded in sprites array");
						break;
					}
				}
				if (actualPos>= 0){
					int newPos;
					if (toArrayStart){
						newPos = 0;
					}
					if (toArrayStart){
						independentOnScreenSprites.remove(actualPos);
						independentOnScreenSprites.add(0, (IndependentOnScreenStaticSprite) object);
						System.out.println("Sprite jumped to the array start");
					}
					else{
						independentOnScreenSprites.remove(actualPos);
						independentOnScreenSprites.add((IndependentOnScreenStaticSprite) object);
						System.out.println("Sprite jumped to the array end");
					}
				}

			}
			else {
				System.out.println("It graphic element can not be transfered to the back of the layer");
			}
		}
		else {
			System.out.println("It element can not be transfered to the back of the layer");
		}
	}

	public boolean isRoundStartedFromEditor() {
		return roundStartedFromEditor;
	}

    public void pauseSoundtrack() {
		if (musicController != null){
			musicController.pausePlay();
		}
    }

	public void resumeSoundtrack() {
		if (musicController != null){
			System.out.println("Music was resumed from gameRound");
			musicController.resumePlay();
		}
	}

	public boolean getLevelType() {
		return levelType;
	}

    public void addOnDisplayText(OnDisplayText text) {
		onDisplayTexts.add(text);
    }

    public ArrayList <SecretAreaZone> getSecretAreas() {
		return secretAreaZones;
    }

	public boolean areThereLights() {
		if (Program.USE_3D) {
			if (lightsController.areThereLights()) {
				return true;
			} else {
				//System.out.println("No lights");
				return false;
			}
		}
		else return false;
	}

	public void addFullScreenFlash(){
		if (Program.USE_3D) {
			lightsController.addFullScreenLight();
		}
	}

	public void addBulletLight(Bullet bullet){
		if (Program.USE_3D) {
			//lightsController.addSingleLight(bullet);
		}
	}

	public void addExplosionLight(float x, float y){
		if (Program.USE_3D){
			lightsController.addExplosionLight(x, y);
		}
	}

	public int getZoneNumber() {
		return levelNumber;
	}

	public ArrayList<LightSource> getActualLights() {
		return lightsController.getLightSources();
	}

	public void addBulletTimeLight(int longPulseTime) {
		if (Program.USE_3D){
			lightsController.addBulletTimeLight(longPulseTime);
		}
	}

	public void updateTimersForBulletTime(boolean direction) {
		ArrayList <LaunchableWhizbang> whizbangs = launchableWhizbangsController.getWhizbangs();
		if (direction == BulletTimeController.TO_SLOW) {
			lightsController.bulletTimeIsActivated(true);
			for (LaunchableWhizbang launchableWhizbang : whizbangs){
				if (launchableWhizbang.getClass() == HandGrenade.class){
					HandGrenade handGrenade = (HandGrenade) launchableWhizbang;
					handGrenade.bulletTimeIsActivated(true);
				}
			}
		}
		else {
			lightsController.bulletTimeIsActivated(false);
			for (LaunchableWhizbang launchableWhizbang : whizbangs){
				if (launchableWhizbang.getClass() == HandGrenade.class){
					HandGrenade handGrenade = (HandGrenade) launchableWhizbang;
					handGrenade.bulletTimeIsActivated(false);
				}
			}
		}

	}

	/*
	public int getEnemiesNumber() {
		int count = 0;
		for (Person person : persons){
			if (!person.equals(getPlayer())){
				if (person.isAlive()){
					count++;
				}
			}
		}
		return count;
	}*/

	public MusicInGameController getMusicController() {
		return musicController;
	}

	public LevelResultsSaveController getLevelResultsSaveController() {
		return levelResultsSaveController;
	}

	public void changeCroshairType(WeaponType weaponType) {
		if (weaponType == WeaponType.GRENADE){
			if (Program.debug) System.out.println("Set crosshair as points");
			playerCrosshair.setCroshairType(Crosshair.CROSSHAIR_POINTS);
		}
		else {
			if (Program.debug) System.out.println("Set crosshair as cros");
			playerCrosshair.setCroshairType(Crosshair.CROSSHAIR_CROSS);
		}
	}

	public void consoleInput(String s) {
		playerCrosshair.consoleInput(s);
	}

	public void deleteGraphicObject(IndependentOnScreenGraphic graphic) {
		for (IndependentOnScreenAnimation animation : independentOnScreenAnimations){
			if (animation.equals(graphic)){
				independentOnScreenAnimations.remove(animation);
				if (Program.debug) System.out.println("Animation was delete after trigger was activated");
				return;
			}
		}
		for (IndependentOnScreenStaticSprite sprite : independentOnScreenSprites){
			if (sprite.equals(graphic)){
				independentOnScreenAnimations.remove(sprite);
				if (Program.debug) System.out.println("Sprite was delete after trigger was activated");
				return;
			}
		}
	}

	public void savePlayerControl(PlayerControl playerControl) {
		this.playerControl = playerControl;
	}

    public boolean hasLevelBossed() {
		for (Person person : persons){
			if (person.getClass() == BossBoar.class){
				return true;
			}
		}
		return false;
    }

	public void addEndLevelZone(EndLevelZone endLevelZone) {
		endLevelZones.add(endLevelZone);
	}

	public void setNewMusicInGameController(MusicInGameController musicInGameController) {
		this.musicController.pausePlay();
		this.musicController = musicInGameController;
	}

	public void loadNewSoundtrack(String absolutePathToAssetsFolder, boolean b, float v) {
		if (this.musicController == null){
			musicController = new MusicInGameController(absolutePathToAssetsFolder, b,v);
		}
		else {
			musicController.pausePlay();
			musicController = new MusicInGameController(absolutePathToAssetsFolder, b,v);
		}
	}

    public ArrayList<AbstractTrigger> getTriggers() {
		return abstractTriggers;
    }

	public void backPressed() {
		switchOffMusic();
		if (getPlayer().isAlive()) {
			for (Person person : persons) {
				if (person.getClass() == BossBoar.class) {
					BossBoar boar = (BossBoar) person;
					boar.playerCanNotKillBoar();
					break;
				}
			}
		}
	}

	public void addFireSplash(Soldier player, Vec2 shotPointRelativeToBodyCenter, int weaponAngle, boolean dynamic, WeaponType weaponType) {
		if (withSplashesPool) {
			boolean foundedInPull = false;
			for (Splash alreadyExistingSplash : splashes) {
				if (alreadyExistingSplash.getClass() == ShotFireSplash.class) {
					if (alreadyExistingSplash.isEnded()) {
						ShotFireSplash fireSplash = (ShotFireSplash) alreadyExistingSplash;
						fireSplash.recreate(player, shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);
						foundedInPull = true;
						if (Program.debug) System.out.println("Shot flash was recreated from pool");
						break;
					}
				}
			}
			if (!foundedInPull) {
				Splash splash = new ShotFireSplash(player, shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);
				splashes.add(splash);

			}
		}
		else {
			Splash splash = new ShotFireSplash(player, shotPointRelativeToBodyCenter, weaponAngle, Splash.DYNAMIC, weaponType);
			splashes.add(splash);
		}

	}

	public void addDustSplash(Body body, Vec2 bulletShotPlace, int bulletAngle, boolean aStatic) {
		if (withSplashesPool) {
			boolean foundedInPull = false;
			for (Splash alreadyExistingSplash : splashes) {
				if (alreadyExistingSplash.getClass() == DustSplash.class) {

					if (alreadyExistingSplash.isEnded()) {
						DustSplash dustSplash = (DustSplash) alreadyExistingSplash;
						dustSplash.recreate(body, bulletShotPlace, bulletAngle, aStatic);
						foundedInPull = true;
						if (Program.debug) System.out.println("Dust splash was recreated from pool");
						break;
					}
				}
			}
			if (!foundedInPull) {
				Splash splash = new DustSplash(body, bulletShotPlace, bulletAngle, aStatic);
				splashes.add(splash);
			}
		}
		else {
			Splash splash = new DustSplash(body, bulletShotPlace, bulletAngle, aStatic);
			splashes.add(splash);
		}
	}

	public void addJumpSplash(Person person) {
		if (withSplashesPool) {
			boolean foundedInPull = false;
			for (Splash alreadyExistingSplash : splashes) {
				if (alreadyExistingSplash.getClass() == JumpSplash.class) {
					if (alreadyExistingSplash.isEnded()) {
						JumpSplash jump = (JumpSplash) alreadyExistingSplash;
						jump.recreate(person);
						foundedInPull = true;
						if (Program.debug) System.out.println("Jump splash was recreated from pool");
						break;
					}
				}
			}
			if (!foundedInPull) {
				JumpSplash splash = new JumpSplash(person);
				if (Program.debug) System.out.println("New jump splash was created");
				splashes.add(splash);
			}
		}
		else {
			JumpSplash splash = new JumpSplash(person);
			if (Program.debug) System.out.println("New jump splash was created");
			splashes.add(splash);
		}
	}

	public AbstractGraphicFocusMaster getBackgroundFocusMaster() {
		return backgroundFocusMaster;
	}



	public void playerChangedHisLifeValue(int startValue, int newValue, int maxLife){
		redColorGraphicMaster.setColorRelativeToLife(startValue, newValue, maxLife);
	}


	public void clearMemory() {
		lightsController.clearEndedLights();

	}
}
