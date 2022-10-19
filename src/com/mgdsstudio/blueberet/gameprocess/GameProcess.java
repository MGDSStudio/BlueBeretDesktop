package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamecontrollers.BulletTimeController;
import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gameobjects.LightSource;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.OneGraphicBackground;
import com.mgdsstudio.blueberet.graphic.background.SinglePictureBackground;
import com.mgdsstudio.blueberet.graphic.screeneffects.ScreenOvershadower;
import com.mgdsstudio.blueberet.graphic.screeneffects.ScreenOvershadowerWithAppearing;
import com.mgdsstudio.blueberet.graphic.screeneffects.ScreenOvershadowerWithHidding;
import com.mgdsstudio.blueberet.graphic.textes.OnDisplayText;
import com.mgdsstudio.blueberet.levelseditornew.AbstractLevelsEditor;
import com.mgdsstudio.blueberet.levelseditornew.LevelsEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.Point;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.PlayerDataController;
import com.mgdsstudio.blueberet.loading.PlayerDataLoadMaster;
import com.mgdsstudio.blueberet.mainpackage.*;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.AbstractBeretColorMaster;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressSaveMaster;
import com.mgdsstudio.blueberet.playerprogress.levelresults.LevelResultsSaveController;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

//import com.mgdsstudio.blueberet.main_package.PhysicWorldUpdating;

public class GameProcess {
	private final GameMainController gameMainController;

	private PlayerControl playerControl;
	private GameRound gameRound;
	//private PhysicGameWorld physicGameWorld;
	private GameCamera gameCamera;

	//PhysicWorldUpdatingThread physicWorldUpdatingThread;
	private RoundLoadingThread roundLoadingThread;
	private BackgroundUpdatingThread backgroundUpdatingThread;
	//private LoadingScreen loadingScreen;
	//PImage testImage;
	boolean roundLoaded;
	boolean onlyLoading = false; // It's for testing of data loading but not for game
	private ObjectsActiveLoader objectsActiveLoader;

	//Levels editor fields
	AbstractLevelsEditor levelsEditorProcess;
	//LevelsEditorProcess levelsEditorProcess;
	//private LevelsEditor levelsEditor;

	private boolean frameStarted;
	private boolean drawingStarted;
	//private int trying;
	private final boolean MULTITHREADED_GAME = false;
	private boolean multithreadedGame = false;
	private boolean multithreadedLoading = false;
	private UpdatingThread updatingThread;
	private boolean updatingLaunched;
	private boolean newLevelReloading = false;
	private ScreenOvershadower screenOvershadowerWithAppearing, screenOvershadowerWithHidding;
	private int updateLoops;
	private final int LAST_LOADING_FRAME = 1000;
	private int actualFrameNumber;
	private boolean actualUpdatingEnds;
	private static BulletTimeController bulletTimeController;
	private GameCameraController gameCameraController;
	private boolean twoRenderingProOneUpdating = false;
	private UpdatingThreadRateController updatingThreadRateController;

	private static boolean bulletTimeMustBeActivated;
	private static int bulletTimeActivatedTime = -1;
	private ArrayList<OnDisplayText> onDisplayTexts;
	private boolean endLevelTextAdded = false;
	private boolean levelLoaded = false;
	private boolean loadedFromEditor;
	private boolean levelType;
	private FramesSaver framesSaver;
	private int distanceToColor = 90;
	private int concentration = 600;
	private boolean screenshotMustBeSaved;
	private ScreenshotSavingMaster screenshotSavingMaster;


	/*
	private float coef1 = -1.3310076f;
	private float coef2 = 0.0028900004f;
	private float coef3 = 3.500002E-6f;
*/

	private float coef1 = -0.386f;
	private float coef2 = 0.0019899998f;
	private float coef3 = 0.00000f;

	private VideoRecorder videoRecorder;
	private BlackBoards blackBoards;

	//private int distanceToLight = 310;

	//0.1f, 0.01f, 0.00000f)
	//private PShape objectsFrameShape;

	public GameProcess(GameMainController gameMainController, boolean loadedFromEditor, boolean levelType) {
		this.levelType = levelType;
		this.gameMainController = gameMainController;
		this.loadedFromEditor = loadedFromEditor;
		initBeretColor();
		System.out.println("Game process was started from editor " + loadedFromEditor + "; It is level from user " + (levelType == ExternalRoundDataFileController.USER_LEVELS));
		Program.recreateFrames();
		if (MULTITHREADED_GAME) {
			if (Program.gameStatement == Program.GAME_PROCESS) multithreadedGame = true;
			else if (Program.gameStatement == Program.LEVELS_EDITOR) multithreadedGame = false;
		} else {
			multithreadedGame = false;
		}
		load();
		if (Program.OS == Program.DESKTOP) framesSaver = new FramesSaver(gameCamera, 100, 100);
		if (Program.USE_3D) {
			//objectsFrameShape = Program.engine.createShape();
		}
		if (Program.OS == Program.DESKTOP) {
			blackBoards = new BlackBoards(Program.engine, 1000, 8);
			videoRecorder = new VideoRecorder(Program.engine, Program.getRelativePathToAssetsFolder()+"Videos\\Trailer 1\\", 2);

			screenshotSavingMaster = new ScreenshotSavingMaster(this);
		}
		gameRound.savePlayerControl(playerControl);


	}

	private void initBeretColor() {
		if (AbstractBeretColorMaster.exists()) {
			Program.withBeretColorChanging = true;
		} else {
			Program.withBeretColorChanging = false;
		}
	}

	public static void activateBulletTime(int normalSlowTime) {
		bulletTimeMustBeActivated = true;
		bulletTimeActivatedTime = normalSlowTime;
	}

	void gameCreating(PVector newPlayerPos) {
		try {
			PhysicGameWorld.init(this);
			if (Program.gameStatement == Program.GAME_PROCESS) {
				gameCamera = new GameCamera(new PVector(Program.engine.width / 2, Program.engine.height / 2), GameCamera.CAMERA_IN_GAME);
				gameCameraController = new GameCameraController(gameCamera);
				gameCamera.setController(gameCameraController);
				System.out.println("Camera in game was created");
			}
			objectsActiveLoader = new ObjectsActiveLoader();
			changeRound(Program.actualRoundNumber, levelType);
			loadSavedData();
			if (newPlayerPos != null) {
				System.out.print("Player was translated to another pos; Pos was: " + gameRound.getPlayer().getPixelPosition());
				gameRound.getPlayer().transferTo(newPlayerPos);
				System.out.println(" and now " + gameRound.getPlayer().getPixelPosition());
			}
			init();
			playerControl = new PlayerControl(gameCameraController, (Soldier) gameRound.getPlayer());
			try {
				if (Program.gameStatement == Program.LEVELS_EDITOR) {
					gameCamera = new GameCamera(new PVector(gameRound.getPlayer().getPositionInPrevFrame().x, gameRound.getPlayer().getPositionInPrevFrame().y), GameCamera.CAMERA_IN_EDITOR);
					if (Program.levelsEditorType == Program.OLD_EDITOR) levelsEditorProcess = new LevelsEditorProcess(gameMainController, gameRound, gameCamera);
					else levelsEditorProcess = new LevelsEditor(gameMainController, gameRound, gameCamera);
					System.out.println("Camera in editor was created");
				}
				gameCamera.setNewActualPosition(gameRound.getPlayer().getPixelPosition());
			} catch (Exception e) {
				System.out.println("Can not place camera on player. Set on 0:0" + e);
				if (Program.gameStatement == Program.LEVELS_EDITOR) {
					gameCamera = new GameCamera(new PVector(Program.engine.width / 2, Program.engine.height / 2), GameCamera.CAMERA_IN_EDITOR);
				} else if (Program.gameStatement == Program.GAME_PROCESS) {
					gameCamera = new GameCamera(new PVector(Program.engine.width / 2, Program.engine.height / 2), GameCamera.CAMERA_IN_GAME);
				}
				if (Program.levelsEditorType == Program.OLD_EDITOR)	levelsEditorProcess = new LevelsEditorProcess(gameMainController, gameRound, gameCamera);
				else levelsEditorProcess = new LevelsEditor(gameMainController, gameRound, gameCamera);
				gameCamera.setNewActualPosition(new PVector(0, 0));
			}
			roundLoaded = true;
			screenOvershadowerWithAppearing = new ScreenOvershadowerWithAppearing(2000, 5);
			screenOvershadowerWithAppearing.setRedIncensity((float)gameRound.getPlayer().getLife()/(float)gameRound.getPlayer().getMaxLife());
			bulletTimeController = new BulletTimeController(gameRound);
			updatingThreadRateController = new UpdatingThreadRateController();

		} catch (Exception e) {
			System.out.println("Something goes wrong by level loading; ");
			e.printStackTrace();
		}
		//System.out.println("Round loaded. Player pos " + gameRound.getPlayer().getAbsolutePosition());
		if (gameRound != null) {
			onDisplayTexts = gameRound.getOnDisplayTexts();
		}
		else System.out.println("Game round is null");
		if (gameRound.hasLevelBossed()){
			objectsActiveLoader.switchOnActiveObjectsLoading(gameRound, false);
		}
	}

	private void loadSavedData() {
		try {
			System.out.println("Try to recovery user data");
			PlayerDataLoadMaster playerDataLoadMaster = new PlayerDataLoadMaster((Soldier) gameRound.getPlayer(), PlayerDataController.GLOBAL_SAVING, levelType);
			playerDataLoadMaster.loadData();
			System.out.println("Data was recovered");
		} catch (Exception e) {
			System.out.println("Can not recovery user saved data");
			e.printStackTrace();
		}
	}

	private void load() {
		if (multithreadedLoading) {
			if (Program.gameStatement == Program.GAME_PROCESS || Program.gameStatement == Program.LEVELS_EDITOR) {
				//loadingScreen = new LoadingScreen();
				roundLoadingThread = new RoundLoadingThread(this);
				roundLoadingThread.setDaemon(true);
				roundLoadingThread.start();
				roundLoadingThread.setPriority(9);
				if (onlyLoading && Program.debug)
					System.out.println("It's only loading mode for graphic loading test on Raspberry");
			}
		} else {
			gameCreating(null);
		}
		if (Program.gameStatement == Program.LEVELS_EDITOR) loadLevelsEditor();
		if (Program.gameStatement == Program.GAME_PROCESS) {
			System.out.println("Frame rate set on: " + Program.NORMAL_FPS);
			Program.engine.frameRate(Program.NORMAL_FPS);
		} else if (Program.gameStatement == Program.LEVELS_EDITOR) {
			Program.engine.frameRate(60);
			System.out.println("Frame rate set on: " + 60);
		}
		if (multithreadedGame) {
			Thread actual = Thread.currentThread();
			actual.setPriority(Thread.MIN_PRIORITY);
			updatingThread = new UpdatingThread(this);
			updatingThread.setDaemon(true);
			updatingThread.setPriority(Thread.MAX_PRIORITY - Thread.MIN_PRIORITY);
		}

	}

	private void loadLevelsEditor() {
		Editor2D.create();
		//gameCamera.setSpringCameraMovement(false);
	}

	void init() {
		Program.engine.rectMode(PConstants.CENTER);
		Program.engine.imageMode(PConstants.CENTER);
	}

	public void changeRound(int newRoundNumber, boolean levelType) {
		System.out.println("Try to load level: " + newRoundNumber);
		loadNewRound(newRoundNumber, levelType);
	}

	public void loadNewRound(int newRoundNumber, boolean levelType) {
		if (gameRound != null) {
			gameRound.clearAllObjectsWithoutPlayer();
		}
		gameRound = new GameRound(newRoundNumber, gameCamera, loadedFromEditor, levelType);
	}

	private void showMousePosition() {
		if (Program.engine.mousePressed) {
			if (Program.engine.mouseButton == PConstants.RIGHT) {
				if (Program.debug) {
					System.out.println("Mouse Position:" +
							((gameCamera.getActualPosition().x + ((Program.engine.mouseX - (Program.engine.width) / 2) / gameCamera.getScale()))) + " x " +
							((gameCamera.getActualPosition().y + ((Program.engine.mouseY - (Program.engine.height) / 2) / gameCamera.getScale()))));
				}
			}
		}
	}

	private void updateBulletTime() {
		if (bulletTimeMustBeActivated) {
			if (bulletTimeController == null) {
				bulletTimeController = new BulletTimeController(gameRound, bulletTimeActivatedTime);
			} else {
				bulletTimeController.activate(bulletTimeActivatedTime);
				gameRound.getSoundController().setAndPlayAudio(SoundsInGame.BULLET_TIME_STARTED);
			}
			System.out.println("Bullet time mode is activated");
			gameRound.activateBulletTime(bulletTimeActivatedTime);
			bulletTimeMustBeActivated = false;
		} else bulletTimeController.update();
	}

	public boolean isBulletTimeModeActivated() {
		if (Program.gameStatement == Program.GAME_PROCESS) {
			if (bulletTimeController.isActivated()) return true;
			else return false;
		} else return false;
	}

	private void updateGame() {

		updateControl();
		//gameRound.drawBulletTimeRect(gameCamera);
		updateBulletTime();
		if (Program.OS == Program.DESKTOP){
			if (videoRecorder != null){
				if (PlayerControl.sPressed){
					blackBoards.start();
				}
				else if (PlayerControl.bPressed){
					blackBoards.stop();

				}
			}
		}
		if (!PhysicGameWorld.assertionErrorAppears) {
			if (true) {

				PhysicGameWorld.update();

				updatingThreadRateController.update();
				playerControl.setCPUUpdatingFrequency(updatingThreadRateController.getActualRate());

				gameRound.update(gameCamera);
				clear();
				gameRound.updateContacts();

				gameRound.endContactsUpdating();
				gameRound.updatePortals(playerControl);
				gameRound.updateLogicZones(playerControl, gameCameraController);
				showMousePosition();
				if (Program.engine.frameCount % 15 == 1) {
					if (gameRound.isPlayerLoosed()) {
						playerControl.hideCompleteHud(true, true);
						if (!endLevelTextAdded) {
							String youLoosed = "YOU  LOOSED";
							String pressBack = "PRESS BACK TO RETURN";
							if (Program.LANGUAGE == Program.RUSSIAN){
								youLoosed = "ВЫ ПРОИГРАЛИ";
								pressBack = "НАЖМИТЕ НАЗАД";
							}
							OnDisplayText text1 = new OnDisplayText(0, (int) (-Program.engine.height / 2 + 3f * UpperPanel.HEIGHT / 2), 255, 255, 255, youLoosed, 30000);
							OnDisplayText text2 = new OnDisplayText(0, (int) (-Program.engine.height / 2 + 3.5f * UpperPanel.HEIGHT / 2), 255, 255, 255, pressBack, 30000);
							onDisplayTexts.add(text1);
							onDisplayTexts.add(text2);
							endLevelTextAdded = true;
							System.out.println("You loosed text was added");
						}
					}
					if (gameRound.isPlayerWon()) {
						playerControl.hideCompleteHud(true, false);
						if (!endLevelTextAdded) {
							String youLoosed = "YOU  WON";
							String pressBack = "PRESS BACK TO RETURN";
							if (Program.LANGUAGE == Program.RUSSIAN){
								youLoosed = "УРОВЕНЬ ПРОЙДЕН";
								pressBack = "НАЖМИТЕ НАЗАД";
							}
							OnDisplayText text1 = new OnDisplayText(0, -Program.engine.height / 2 + 3 * UpperPanel.HEIGHT / 2, 255, 255, 255, youLoosed, 90000);
							OnDisplayText text2 = new OnDisplayText(0, (int) (-Program.engine.height / 2 + 3.5f * UpperPanel.HEIGHT / 2), 255, 255, 255, pressBack, 90000);
							onDisplayTexts.add(text1);
							onDisplayTexts.add(text2);
							endLevelTextAdded = true;
							System.out.println("You win text was added");
						}
					}
				}
				objectsActiveLoader.update(gameRound, gameCamera);
				updateLoops++;
				actualUpdatingEnds = true;
			}
		}
		if (gameRound.isLevelEnds()) endLevel();
		for (OnDisplayText text : onDisplayTexts) {
			text.update();
		}
	}

	private void endLevel() {
		if (gameRound.isPlayerWon() || gameRound.isPlayerLoosed()) {
			playerControl.setBackButtonAsVisible();
			if (bulletTimeController.isActivated()) bulletTimeController.deactivate();
		}
	}

	private void updateLevelsEditor() {
		showMousePosition();
		if (Program.jumpFromGameToRedactor) {
			try {
				if (gameCamera == null) {
					PVector pos = null;
					if (gameRound == null || gameRound.getPlayer() == null) {

					}
					if (pos == null) pos = new PVector(0, 0);
					gameCamera = new GameCamera(pos, GameCamera.CAMERA_IN_EDITOR);
					System.out.println("Camera was recreated");
				}
			} catch (AssertionError error) {
				System.out.println("Can not recreate camera; New Camera is in null place");
				float x = 0;
				float y = 0;
				if (gameRound.getPlayer().getPixelPosition() != null) {
					x = gameRound.getPlayer().getPixelPosition().x;
					y = gameRound.getPlayer().getPixelPosition().y;
				}
				gameCamera = new GameCamera(new PVector(x, y), GameCamera.CAMERA_IN_EDITOR);
			}
		}
		objectsActiveLoader.update(gameRound, gameCamera);
		if (gameCamera == null) {
			levelsEditorProcess.recreateCamera(gameCamera);
			gameCamera = new GameCamera(new PVector(GameCamera.lastCameraPositionInEditor.x, GameCamera.lastCameraPositionInEditor.y), GameCamera.CAMERA_IN_EDITOR);
			levelsEditorProcess.setEditorCamera(gameCamera);
		}
		if (gameCamera != null) {
			try {

				levelsEditorProcess.update(gameCamera);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Program.gameStatement == Program.LEVELS_EDITOR) {
				if (Editor2D.isLevelChanged()) {
					Editor2D.levelChanging();
					load();
					Editor2D.localStatement = 0;
					Editor2D.setNewGlobalStatement(Editor2D.OBJECT_SELECTING);
					System.out.println("New level was created");
				}
			}
			gameCamera.updateInEditorMovement();
		}
	}

	protected void updateGameInSingleThread() {
		if (Program.gameStatement == Program.GAME_PROCESS) updateGame();
		else if (Program.gameStatement == Program.LEVELS_EDITOR) updateLevelsEditor();
	}

	protected boolean isDrawingStarted() {
		return drawingStarted;
	}

	protected boolean isDrawingEnded() {
		return drawingStarted;
	}

	private void update() {
		//frameStarted = true;
		if (multithreadedGame) {
			if (twoRenderingProOneUpdating) {
				updatingThread.start();
			} else {
				if (!updatingLaunched) {
					updatingLaunched = true;
					updatingThread.start();
				}
			}
		} else {
			updateGameInSingleThread();
		}
		if (Program.OS == Program.DESKTOP && PlayerControl.key8Pressed){
			//screenshotSavingMaster.consoleInput(ScreenshotSavingMaster.MAKE_SCREENSHOT);
		}
	}

	private void printActualMousePosition() {
		if (Program.engine.frameCount % 30 == 1) {
			if (Program.debug) {
				//System.out.println("Mouse position: " + (Game2D.engine.mouseX+gameCamera.getActualPosition().x) + "x" + (Game2D.engine.mouseY+gameCamera.getActualPosition().y));
			}
		}
	}

	private void clear() {
		gameRound.clearRedundantBullets(gameCamera);
	}


	private void drawWithSyncronisation() {
		//It function is used
		if (actualUpdatingEnds || updateLoops < LAST_LOADING_FRAME) {
			actualUpdatingEnds = false;
			drawingStarted = true;
			if (!Background.backgroundAtAnotherFrame) startObjectFrameDrawingBuffer();
			drawBackgrounds();
			if (Background.backgroundAtAnotherFrame) startObjectFrameDrawingBuffer();
			gameRound.drawGameWorldObjects(gameCamera, isBulletTimeModeActivated());
			if (Program.debug) gameRound.drawDebugGraphic(gameCamera);
			if (Program.gameStatement == Program.LEVELS_EDITOR && Point.ON_OBJECT_FRAME) {
				try {
					if (gameCamera != null) levelsEditorProcess.drawObjectsInGameWorld(gameCamera);
					else {
						System.out.println("Camera is null");
						gameCamera = new GameCamera(new PVector(GameCamera.lastCameraPositionInEditor.x, GameCamera.lastCameraPositionInEditor.y), GameCamera.CAMERA_IN_EDITOR);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			endObjectFrameDrawingBuffer();
			if (Program.gameStatement == Program.GAME_PROCESS) {
				if (roundLoaded) {
					try {
						gameCameraController.update(gameRound.getPlayer(), playerControl);
					} catch (Exception e) {
						//System.out.println("Can not update camera pos");
					}
				}
				Program.engine.clip((int) ((Program.engine.width / 2)), (int) ((Program.engine.height / 2)), (int) (Program.engine.width), (int) (Program.engine.height));

			} else if (Program.gameStatement == Program.LEVELS_EDITOR) {
				if (levelsEditorProcess != null) levelsEditorProcess.drawBackground();
				Program.engine.imageMode(PConstants.CORNER);
				Program.engine.clip(levelsEditorProcess.getFrame().getLeftX()+2, levelsEditorProcess.getFrame().getUpperY()+2, levelsEditorProcess.getFrame().getWidth()-4, levelsEditorProcess.getFrame().getHeight()-4);

				Program.engine.imageMode(PConstants.CENTER);
			}
			if (Background.backgroundAtAnotherFrame) renderBackground();
			renderObjectFrame(gameCamera);
			objectsActiveLoader.draw(gameCamera);
			if (levelsEditorProcess != null) {
				if (Program.gameStatement == Program.LEVELS_EDITOR) {
					Program.engine.noClip();
					levelsEditorProcess.draw(gameCamera);
				}
			}
			Program.engine.image(HeadsUpDisplay.graphic, Program.engine.width / 2, Program.engine.height / 2);
			if (Program.gameStatement == Program.GAME_PROCESS) {
				playerControl.draw(gameCamera, gameRound.getPlayer().isAlive());
				if (gameRound.areThereActiveTitles()) gameRound.drawTitles();
			}
			frameStarted = false;
			drawingStarted = false;
			if (Program.OS == Program.DESKTOP){
				if (videoRecorder != null){
					//blackBoards.draw();
					//videoRecorder.update();
				}
			}
			if (multithreadedGame) updatingThread.setUpdatedForThisFrame(false);

		} else {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		for (OnDisplayText text : onDisplayTexts) {
			text.update();
			text.draw();
		}

		if ((Program.engine.frameCount & 30) == 0) {
			//System.out.println("Smooth: " + Program.engine.sketchSmooth() + " for frame: " + Program.objectsFrame.smooth);
		}
	}

	private void renderBackground() {
		int visibleAreaHalfWidth = (int) (Program.backgroundFrame.width - (Program.backgroundFrame.width * (gameCamera.getScale() - GameCamera.minScale))) / 2;
		int visibleAreaHalfHeight = (int) (Program.backgroundFrame.height - (Program.backgroundFrame.height * (gameCamera.getScale() - GameCamera.minScale))) / 2;
		Program.engine.image(Program.backgroundFrame, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height,
				(int) (((Program.backgroundFrame.width / 2f) - visibleAreaHalfWidth)),
				(int) (((Program.backgroundFrame.height / 2f) - (visibleAreaHalfHeight))),
				(int) (((Program.backgroundFrame.width / 2f) + (visibleAreaHalfWidth))),
				(int) (((Program.backgroundFrame.height / 2f) + (visibleAreaHalfHeight))));
	}

	private void renderObjectFrame(GameCamera gameCamera) {
		if (Program.OS == Program.DESKTOP && Program.gameStatement == Program.GAME_PROCESS) {
			framesSaver.update();
		}
		int visibleAreaHalfWidth = (int) (Program.objectsFrame.width - (Program.objectsFrame.width * (gameCamera.getScale() - GameCamera.minScale))) / 2;
		int visibleAreaHalfHeight = (int) (Program.objectsFrame.height - (Program.objectsFrame.height * (gameCamera.getScale() - GameCamera.minScale))) / 2;
		if (screenshotMustBeSaved){

		}
		if (Program.USE_3D) draw3D(visibleAreaHalfWidth, visibleAreaHalfHeight);
			//if (Program.USE_3D) draw3DWithPointLight(gameCamera, visibleAreaHalfWidth, visibleAreaHalfHeight);
		else draw2D(visibleAreaHalfWidth, visibleAreaHalfHeight);
		if (screenshotMustBeSaved){
			Program.backgroundFrame.save(Program.getAbsolutePathToAssetsFolder("Screenshot/Background " + screenshotSavingMaster.getFrameNumber()+ ".png"));
			Program.objectsFrame.save(Program.getAbsolutePathToAssetsFolder("Screenshot/World " + screenshotSavingMaster.getFrameNumber()+ ".png"));
			System.out.println(" Screenshot was saved ");
			screenshotMustBeSaved = false;
		}
		//Program.engine.filter(PConstants.GRAY);
	}

	private void draw2D(int visibleAreaHalfWidth, int visibleAreaHalfHeight) {
		Program.engine.image(Program.objectsFrame, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height,
				(int) (((Program.objectsFrame.width / 2f) - visibleAreaHalfWidth)),
				(int) (((Program.objectsFrame.height / 2f) - (visibleAreaHalfHeight))),
				(int) (((Program.objectsFrame.width / 2f) + (visibleAreaHalfWidth))),
				(int) (((Program.objectsFrame.height / 2f) + (visibleAreaHalfHeight))));
	}

	private void draw3D(int visibleAreaHalfWidth, int visibleAreaHalfHeight) {
		boolean areLight = gameRound.areThereLights();
		Program.setMouseWheelRotation(Program.NO_ROTATION);
		if (areLight) {
			float x;
			float y;
			int red;
			int green;
			int blue;
			ArrayList <LightSource> lights = gameRound.getActualLights();
			//Program.engine.ambientLight(205, 205, 103);
			Program.engine.ambientLight(255, 255, 128);
			Program.engine.lightFalloff(coef1, coef2, coef3);
			for (int i = (lights.size()-1); i >= 0; i--) {
				if (!lights.get(i).isEnded()) {
					try {
						x = lights.get(i).getOnScreenX(gameCamera);
						y = lights.get(i).getOnScreenY(gameCamera);
						red = lights.get(i).getRed();
						green = lights.get(i).getGreen();
						blue = lights.get(i).getBlue();
						int distToLight = lights.get(i).getActualZDistanceToLight();
						//System.out.println("Dist to " + i + " light is: " + distToLight);
						Program.engine.pointLight(red, green, blue, x, y, distToLight);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			Program.engine.ambientLight(128, 128, 128);
			Program.engine.directionalLight(128, 128, 128, 0, 0, -1);
		}

		Program.engine.lightSpecular(0, 0, 0);
		Program.engine.beginShape();
		Program.engine.texture(Program.objectsFrame);
		final int addDist = 1;
		Program.engine.vertex(0,-addDist,(int)((Program.objectsFrame.width /2f)-visibleAreaHalfWidth ),(int)(((Program.objectsFrame.height /2f)-(visibleAreaHalfHeight ))));
		Program.engine.vertex(Program.engine.width+addDist,-addDist,(int)((Program.objectsFrame.width /2f)+visibleAreaHalfWidth ),(int)(((Program.objectsFrame.height /2f)-(visibleAreaHalfHeight ))));
		Program.engine.vertex(Program.engine.width+addDist,Program.engine.height+addDist,(int)((Program.objectsFrame.width /2f)+visibleAreaHalfWidth ),(int)(((Program.objectsFrame.height /2f)+(visibleAreaHalfHeight ))));
		Program.engine.vertex(0,Program.engine.height+addDist,(int)((Program.objectsFrame.width /2f)-visibleAreaHalfWidth ),(int)(((Program.objectsFrame.height /2f)+(visibleAreaHalfHeight ))));
		Program.engine.endShape();
		Program.engine.noLights();

	}



	private void draw3DWithPointLight(GameCamera gameCamera, int visibleAreaHalfWidth, int visibleAreaHalfHeight){
		Program.engine.ambientLight(128, 128, 128);
		Program.engine.directionalLight(128, 128, 128, 0, 0, -1);
		Program.engine.lightFalloff(coef1, coef2, coef3);
		Program.engine.lightSpecular(0, 0, 0);
		int x = 384;
		int y = 1143;
		int onScreenX = (int) (x-gameCamera.getActualXPositionRelativeToCenter()/2);
		int onScreenY = (int) (y-gameCamera.getActualYPositionRelativeToCenter()/2);
		if (Program.engine.mousePressed) {
			int sources = 1;
			for (int i = 0; i < sources; i++) {
				Program.engine.pointLight(155, 155, 0,  Program.engine.width/2, Program.engine.height/2, distanceToColor);
				//Program.engine.pointLight(155, 155, 0,  Program.engine.width/2, Program.engine.height/2, distanceToColor);
			}
			System.out.println("On screen at " + onScreenX + " x " + onScreenY);
		}
		/*
		if (Program.OS == Program.WINDOWS){

			if (PlayerControl.keyUpPressed){
				coef1+=0.001f;
			}
			else if (PlayerControl.keyDownPressed){
				coef1-=0.001f;
			}
			else if (PlayerControl.keyRightPressed){
				coef2+=0.0001f;
			}
			else if (PlayerControl.keyLeftPressed){
				coef2-=0.0001f;
			}
			else if (PlayerControl.key5Pressed){
				distanceToColor-=1f;
			}
			else if (PlayerControl.key6Pressed){
				distanceToColor+=1f;
			}
			else if (PlayerControl.key7Pressed){
				coef3-=0.0000001f;
			}
			else if (PlayerControl.key8Pressed){
				coef3+=0.0000001f;
			}

			if (Program.engine.keyPressed){
				System.out.println("Actual coef: " + coef1 + ", " + coef2 + ", " + coef3 + "; Dist: " + distanceToColor);
			}
		}*/



		//else Program.engine.noLights();
		/*
		Program.engine.ambientLight(128, 128, 128);
		Program.engine.directionalLight(128, 128, 128, 0, 0, -1);
		Program.engine.lightFalloff(1, 0, 0);
		Program.engine.lightSpecular(0, 0, 0);

		if (Program.engine.mousePressed) {
			int sources = 4;
			float angle = PApplet.PI/8f;
			for (int i = 0; i < sources; i++) {
				Program.engine.spotLight(255, 0, 0, Program.engine.mouseX, Program.engine.mouseY, distanceToColor, 0, 0, -1, angle, concentration);
				concentration+=2;
				if (concentration >10000) concentration = 0;
			}

		}
		else Program.engine.noLights();
		*/
		/*
		if (Program.engine.mousePressed) {
			int sources = 1;
			float angle = PApplet.PI/2f;
			for (int i = 0; i < sources; i++) {
				Program.engine.spotLight(255, 255, 255, Program.engine.mouseX, Program.engine.mouseY, distanceToColor, 0, 0, -1, angle, 2f);
				concentration+=2;
				if (concentration >10000) concentration = 0;
			}
			System.out.println("Concentration: " + concentration);*/




		Program.engine.beginShape();
		Program.engine.texture(Program.objectsFrame);
		Program.engine.vertex(0,0, (int)((Program.objectsFrame.width / 2f) - visibleAreaHalfWidth ), (int)(((Program.objectsFrame.height / 2f) - (visibleAreaHalfHeight ))));
		Program.engine.vertex(Program.engine.width,0, (int)((Program.objectsFrame.width / 2f) + visibleAreaHalfWidth ), (int)(((Program.objectsFrame.height / 2f) - (visibleAreaHalfHeight ))));
		Program.engine.vertex(Program.engine.width,Program.engine.height, (int)((Program.objectsFrame.width / 2f) + visibleAreaHalfWidth ), (int)(((Program.objectsFrame.height / 2f) + (visibleAreaHalfHeight ))));
		Program.engine.vertex(0,Program.engine.height, (int)((Program.objectsFrame.width / 2f) - visibleAreaHalfWidth ), (int)(((Program.objectsFrame.height / 2f) + (visibleAreaHalfHeight ))));
		Program.engine.endShape();
		Program.engine.noLights();

		/*
		Program.engine.image(Program.objectsFrame, Program.engine.width / 2, Program.engine.height / 2, Program.engine.width, Program.engine.height,
				(int)(((Program.objectsFrame.width / 2f) - visibleAreaHalfWidth )),
				(int)(((Program.objectsFrame.height / 2f) - (visibleAreaHalfHeight ) )),
				(int)(((Program.objectsFrame.width / 2f) + (visibleAreaHalfWidth ) )),
				(int)(((Program.objectsFrame.height / 2f) + (visibleAreaHalfHeight ) )));

		*/
	}

	private void draw() {
		drawWithSyncronisation();
	}

	private void launchBackgroundThread() {
		//if (!drawingStarted)
		if (backgroundUpdatingThread.isBackgroundForThisFrameAlreadyDrawn()){
			try {
				System.out.println("Thread was started; " + Program.engine.millis());
				backgroundUpdatingThread.start();
				System.out.println("Thread was ended" + Program.engine.millis());
			}
			catch (Exception e){
				System.out.println("Can not draw background");
				e.printStackTrace();
			}
		}
	}

	boolean isFrameStarted(){
		return frameStarted;
	}

	boolean isFrameEnded(){
		return !frameStarted;
	}
	
	private void updateControl() {
		playerControl.update(this, gameRound, gameCamera, gameMainController);
	}

	public void jumpToAnotherLevel(int levelNumber, PVector pos){
		screenOvershadowerWithHidding = new ScreenOvershadowerWithHidding(2000,5);
		screenOvershadowerWithHidding.setRedIncensity((float)gameRound.getPlayer().getLife()/(float)gameRound.getPlayer().getMaxLife());
		gameRound.getBackgrounds().clear();
		System.out.println("Jump to another level");
		newLevelReloading = true;
		if (levelType == ExternalRoundDataFileController.MAIN_LEVELS) {
			PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
			playerProgressSaveMaster.setNextZoneWithFullDataRewriting(Program.actualRoundNumber, levelNumber);
			playerProgressSaveMaster.saveOnDisk();
		}
		else {
			System.out.println("For single missions there are no global saving");
		}
		Program.actualRoundNumber = (int) levelNumber;
		gameCreating(pos);
	}



	public void game() {
		if ((roundLoaded && !onlyLoading)) {
			update();
			if (!newLevelReloading) {
				draw();
				screenOvershadowerWithAppearing.update();
				screenOvershadowerWithAppearing.draw();
			}
			else {
				if (screenOvershadowerWithHidding!= null) {
					screenOvershadowerWithHidding.update();
					screenOvershadowerWithHidding.draw();
					if (screenOvershadowerWithHidding.isEnded()) {
						screenOvershadowerWithHidding.restart();
						newLevelReloading = false;
					}
				}
			}
			if (roundLoadingThread != null) {
				roundLoadingThread.interrupt();
				roundLoadingThread = null;
				//loadingScreen = null;
			}
			Program.updateDeltaTime();
		}
		else {
			if (multithreadedLoading) {
				//loadingScreen.draw();
				if (Program.canNotLoadLevelOrEditor) {
					System.out.println("Can not load level or editor");
				}
			}
		}
	}


	public void contactStartedSolveInterrupt(Contact arg0, Manifold arg1) {
		//gameRound.contactStartedSolveInterrupt(arg0, arg1);
	}

    public void contactPreSolveInterrupt(Contact arg0, Manifold arg1) {
		gameRound.contactPreSolveInterrupt(arg0, arg1);
    }

	private void startObjectFrameDrawingBuffer(){
		Program.objectsFrame.beginDraw();
		Program.objectsFrame.imageMode(PConstants.CORNER);
		if (!bulletTimeController.isActivated()) {
			Program.objectsFrame.clear();
			Program.objectsFrame.noTint();
		}
		else {
			if (Program.OS == Program.DESKTOP){
				bulletTimeController.makeSmoothnessWindowsStyle(Program.objectsFrame);
			}
			else {
				if (Program.engine.frameCount%3==0)	bulletTimeController.makeSmoothnessAndroidStyle(Program.objectsFrame);
				//else Program.objectsFrame.clear();
				//bulletTimeController.makeSmoothnessAndroidStyle(Program.objectsFrame);
			}
		}
		Program.objectsFrame.pushStyle();
		Program.objectsFrame.imageMode(PConstants.CORNER);
	}

	private void endObjectFrameDrawingBuffer(){
		if (Program.OS == Program.DESKTOP){
			if (gameRound.getPlayer().isAttacked){

			}
		}
		Program.objectsFrame.endDraw();
	}

	void drawBackgrounds() {
		if (Background.backgroundAtAnotherFrame){
			drawBackgroundsAtSeparateGraphic();
		}
		else drawBackgroundsAtSameGraphic();
	}



	void drawBackgroundsAtSameGraphic(){
		//Program.objectsFrame.pushStyle();
		gameRound.drawBackground(gameCamera);
		Program.objectsFrame.imageMode(PConstants.CENTER);
		Program.objectsFrame.popStyle();
	}


	void drawBackgroundsAtSeparateGraphic() {
		Program.backgroundFrame.beginDraw();
		//System.out.println("Background frame opened");
		gameRound.drawBackground(gameCamera);
		//System.out.println("Background frame closed");
		Program.backgroundFrame.endDraw();

	}

	@Override
	protected void finalize(){

		try {
			for (Background background : gameRound.getBackgrounds()){
				if (background instanceof SinglePictureBackground){
					SinglePictureBackground bcg = (SinglePictureBackground) background;
					bcg.clearGraphic();
				}
				else  if (background instanceof OneGraphicBackground){
					OneGraphicBackground bcg = (OneGraphicBackground) background;
					bcg.clearGraphic();
				}
			}
			SingleGameElement.gameRound = null;
			gameRound.getMainGraphicController().clearGraphic();
			gameRound = null;
			gameCamera = null;
			PhysicGameWorld.clearPreContacts();
			PhysicGameWorld.clearBeginContacts();
			PhysicGameWorld.clearEndContacts();
			PhysicGameWorld.clearPostContacts();
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			super.finalize();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void setMultithreadedGame(boolean multithreadedGame) {
		this.multithreadedGame = multithreadedGame;
	}

	public GameCamera getCamera() {
		return gameCamera;
	}

	public BulletTimeController getBulletTimeController() {
		return bulletTimeController;
	}



    public void addPreContact(Contact arg0, Manifold arg1) {
		gameRound.addPreContact(arg0, arg1);
    }

	public void switchOffMusic() {
		try {
			gameRound.switchOffMusic();
		}
		catch (Exception e){
			System.out.println("No audio controller was init");
		}
	}

	/*public void savePlayer(Soldier player) {
		this.savedPlayer = player;
	}*/

	public void onFlick( float x, float y, float px, float py, float v){
		if (Program.gameStatement == Program.LEVELS_EDITOR){
			levelsEditorProcess.onFlick(x, y, px, py, v);
		}
	}

	public void onPinch(float x, float y, float value){
		if (Program.gameStatement == Program.LEVELS_EDITOR){
			levelsEditorProcess.onPinch(x, y, value);
		}
	}

    public void backPressed() {
		if (Program.gameStatement == Program.GAME_PROCESS){
			gameRound.backPressed();

		}
    }

	public boolean isLoadedFromEditor() {
		return loadedFromEditor;
	}

	public boolean isLevelLoaded() {
		return levelLoaded;
    }

	public void pauseMusic() {
		gameRound.pauseSoundtrack();
	}

	public void resumeMusic() {
		gameRound.resumeSoundtrack();
	}

	public boolean isLevelType() {
		return levelType;
	}

    public boolean hasPlayerWon() {
		if (gameRound.isPlayerWon()){
			return true;
		}
		else return false;
    }

    public void stopMusic() {
		if (gameRound != null){
			gameRound.getSoundController().stopAllAudio();
			gameRound.getMusicController().stopPlay();
		}
    }

    public void continueTimers() {
		LevelResultsSaveController controller = gameRound.getLevelResultsSaveController();
		controller.continueTimers();
    }

	public void pauseTimers() {
		LevelResultsSaveController controller = gameRound.getLevelResultsSaveController();
		controller.pauseTimers();
	}

    public void consoleInput(String s) {
		gameRound.consoleInput(s);
		screenshotSavingMaster.consoleInput(s);
    }

    public void saveScreenshot() {
		screenshotMustBeSaved = true;

    }
}
