package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gamecontrollers.MoveableSpritesAddingController;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.MedicalKit;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.Syringe;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.portals.Portal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalToAnotherLevel;
import com.mgdsstudio.blueberet.gameprocess.control.DPadSize;
import com.mgdsstudio.blueberet.gameprocess.control.FivePartsStick;
import com.mgdsstudio.blueberet.gameprocess.control.MovementStick;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.gameprocess.sound.TrackData;
import com.mgdsstudio.blueberet.graphic.effectsmasters.AbstractGraphicFocusMaster;
import com.mgdsstudio.blueberet.graphic.effectsmasters.BackgroundFocusMaster;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.PortraitPicture;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMSController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.PlayerDataSaveMaster;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class PlayerControl{
	private final BackMovementAndShootingController backMovementAndShootingController;


	public static DPadSize dPadSize;
	private HeadsUpDisplay hud;
	private Timer panelAfterMessageClosingTimer;
	private final int TIME_TO_DEBLOCK_UPPER_PANEL = 200;
	private boolean frameControllersAreBlocked = false;
	private GameCameraController gameCameraController;
	private Soldier player;
	//private static Image graphic;
	private static boolean graphicLoaded = false;
	private float shotDispersion = 0f;
	private float maxShotDispersion = 100f;
	private float shotDispersionVelocity = 3f*18f / 33f;
	private boolean attacking = false;

	//Movement in air
	private final static boolean CAN_MOVE_IN_AIR = true;

	//Buttons
	public static final byte BUTTON_JUMP = 5;
	public static final byte BUTTON_SHOOTING = 6;
	public static final byte BUTTON_PORTAL = 7;
	public static final byte BUTTON_RELOAD = 8;
	public static final byte BUTTON_BACK_TO_REDACTOR = 9;
	public static final byte BUTTON_WITHOUT_AMMO_SHOT = 13;
	public static final byte BUTTON_KICK = 10;

	private ArrayList<OnScreenButton> buttons = new ArrayList<OnScreenButton>();

	//Joystick joystick;
	private MovementStick movementStick;
	private FivePartsStick rectStick;
	private boolean WITH_RECTANGULAR_STICK = true;

	//Desktop specific
	public static boolean keyUpPressed = false;

	public static boolean keyDownPressed = false;

	public static boolean key7Pressed = false;
	public static boolean key8Pressed = false;

	public static boolean key5Pressed = false;
	public static boolean key6Pressed = false;
	public static boolean keyRightPressed = false;
	public static boolean keyLeftPressed = false;
	public static boolean shiftPressed = false;
	public static boolean kickKeyPressed = false;
	public static boolean backspacePressed = false;

	public static boolean sPressed;

	public static boolean bPressed;

	boolean controllerType;
	private boolean kickingNow;
	private final boolean PLAYER_MOVEMENT_WITH_ACCELERATE = false;

	//private Soldier player;


	// Weapons changing direction
	public static final boolean NEXT_WEAPON = true;
	public static final boolean PREV_WEAPON = false;

	private boolean allControlButtonsAreHidden;
	private boolean playerCanEnterPortal;
	private boolean portalButtonMustBeShown;
	private boolean angleResetingByStickRelease = true;
	private final static int yStepBetweenButtons = Program.engine.height / 13;
	private final static int distanceToLowerButton = Program.engine.height - Program.engine.height / 10;
	public static final int SCREEN_CONTROL_AREA_HEIGHT = Program.engine.height - (distanceToLowerButton - 3 * yStepBetweenButtons);
	private boolean hudHidden;
	private boolean playerLoosed;
	private boolean reloadingNow;
	private boolean playerSelectingAnotherWeapon, playerSelectingInventoryObject;
	private boolean controlIsBlockedThroughMessage;


	public final static int USER_PRESSES_RUN_RIGHT = 5;
	public final static int USER_PRESSES_RUN_LEFT = 4;
	public final static int USER_PRESSES_GO_RIGHT = 3;
	public final static int USER_PRESSES_GO_LEFT = 2;
	public final static int USER_PRESSES_ON_AIMING_ZONE = 1;
	public final static int USER_DOESNOT_TOUCH_STICK = 0;

	private Soldier playerSavedForNextLevelJumping;
	private boolean jumpingToAnotherLevel;
	private boolean longPressingWasReleasedAfterLastOpening;
	private int inventoryOpeningStartFrame = 1;
	private int mouseReleasingLastFrame = 0;
	private boolean canBeInventoryFrameClosedAfterLongPressing = false;
	private boolean messageAddedThroughInventoryTouch = false;
	boolean canNotOpenFrameToShowInventory;        // If the inventory has only 0 or 1 object tnen player can not take a medical through a long pressing
	public static boolean withAdoptingGuiRedrawing = true;
	private static boolean forceRedrawingForOneFrame = false;
	private boolean hudWasClearedAfterLastHidding = false;
	private final boolean WITH_KICK_BUTTON_ROTATION = true;

	private final PVector mutMousePlace = new PVector();
	private final PVector mutVectorToTouchesPlace = new PVector();
	private Timer noAmmoShotTimer;
	private final int NO_AMMO_SOUND_TIME = 150;
	private final boolean useAnimatedPortalButton = true;
	public final static boolean BACK_ON_SCREEN_BUTTON_BLOCKED = true;
	private boolean clearLowerHudForNextFrame = false;
	private boolean prevMovementDirection;


	PlayerControl(GameCameraController gameCameraController, Soldier soldier) {
		hud = new HeadsUpDisplay(this, soldier);

		this.player = soldier;
		prevMovementDirection = soldier.orientation;
		backMovementAndShootingController = new BackMovementAndShootingController();
		this.gameCameraController = gameCameraController;
		if (!graphicLoaded) {
			//graphic = HeadsUpDisplay.mainGraphicSource;
		}
		int xStepBetweenButtons = (int) (2.6f * OnScreenButton.normalDimention);
		int xStepFromRightBoardToButton = (int) ((float)xStepBetweenButtons*0.45f);
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.shotButton, Program.engine.width - xStepFromRightBoardToButton, distanceToLowerButton, OnScreenButton.ROUND, BUTTON_SHOOTING, true, false));
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.jumpButton, Program.engine.width - xStepFromRightBoardToButton, distanceToLowerButton - 2 * yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_JUMP, false, false));
		if (useAnimatedPortalButton) buttons.add(new AnimatedOnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.toPortalButton, Program.engine.width - xStepFromRightBoardToButton-xStepBetweenButtons/2, distanceToLowerButton - yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_PORTAL, false, HUD_GraphicData.toAnimatedPortalSecondButton));
		else buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.toPortalButton, Program.engine.width - xStepFromRightBoardToButton-xStepBetweenButtons/2, distanceToLowerButton - yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_PORTAL, false, false));

		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.reloadButton, (Program.engine.width - xStepBetweenButtons - xStepFromRightBoardToButton), distanceToLowerButton - 2 * yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_RELOAD, false, false));
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.withoutAmmoShotButton, Program.engine.width - xStepFromRightBoardToButton, distanceToLowerButton, OnScreenButton.ROUND, BUTTON_WITHOUT_AMMO_SHOT, false, true));
/*
buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.shotButton, Program.engine.width - xStepFromRightBoardToButton, distanceToLowerButton, OnScreenButton.ROUND, BUTTON_SHOOTING, true, false));

		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.jumpButton, Program.engine.width - xStepBetweenButtons / 2, distanceToLowerButton - 2 * yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_JUMP, false, false));
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.toPortalButton, Program.engine.width - xStepBetweenButtons, distanceToLowerButton - yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_PORTAL, false, false));
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.reloadButton, (int) (Program.engine.width - 3f * xStepBetweenButtons / 2), distanceToLowerButton - 2 * yStepBetweenButtons, OnScreenButton.ROUND, BUTTON_RELOAD, false, false));
		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.withoutAmmoShotButton, Program.engine.width - xStepBetweenButtons / 2, distanceToLowerButton, OnScreenButton.ROUND, BUTTON_WITHOUT_AMMO_SHOT, false, true));

 */
		if (WITH_KICK_BUTTON_ROTATION)
			buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.kickButton, (int) (Program.engine.width - xStepBetweenButtons - xStepFromRightBoardToButton), distanceToLowerButton, OnScreenButton.ROUND, BUTTON_KICK, true, false));
		else
			buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.kickButton, (int) (Program.engine.width - xStepBetweenButtons - xStepFromRightBoardToButton), distanceToLowerButton, OnScreenButton.ROUND, BUTTON_KICK, false, false));

		buttons.add(new OnScreenButton(HeadsUpDisplay.mainGraphicTileset, HUD_GraphicData.backButton, (int) (Program.engine.width - OnScreenButton.normalDimention * 1.15f), (int) (OnScreenButton.normalDimention * 1.15f) + UpperPanel.HEIGHT, OnScreenButton.RECTANGULAR, BUTTON_BACK_TO_REDACTOR, true, true));
		//SCREEN_CONTROL_AREA_HEIGHT = Programm.engine.height-(distanceToLowerButton-3*yStepBetweenButtons);
		System.out.println("Button pos: ");
		getButton(BUTTON_RELOAD).hide();
		getButton(BUTTON_PORTAL).hide();
		if (!Program.levelLaunchedFromRedactor) {
			getButton(BUTTON_BACK_TO_REDACTOR).hide();
		}
		float width = (Program.engine.width / 1.99f);
		float height = FivePartsStick.NORMAL_STICK_HEIGHT;
		float leftX = (Program.engine.width / 3.4f)-width/2;
		float lowerY = (Program.engine.height - Program.engine.width / 3.5f)+height/2;
		System.out.println("Original stick dims for are: " + width + "x" + height);
		float dimCoef = 1f;
		if (dPadSize == DPadSize.MEDIUM) {
			dimCoef = 0.8f;
		}
		else if (dPadSize == DPadSize.SMALL) dimCoef = 0.6f;
		if (WITH_RECTANGULAR_STICK) {
			width*=dimCoef;
			height*=dimCoef;
			System.out.println("New stick dims for :" + dPadSize + " are: " + width + "x" + height);
			rectStick = new FivePartsStick((int) (leftX), (int) (lowerY), (int) width, (int) height);
		}
		else
			movementStick = new MovementStick((int) (Program.engine.width / 3.8f), (int) (Program.engine.height - Program.engine.width / 3.3f), (int) (Program.engine.width / 2.5f));

		//player = soldier;
	}

	public static void setForceRedrawingForOneFrame() {
		forceRedrawingForOneFrame = true;
	}

	public void hideAllButtons(boolean toHide) {
		for (OnScreenButton button : buttons) {
			if (toHide) button.hide();
			else button.doNotHide();
		}
	}

	public void hideButton(byte buttonToBeBlocked, boolean toHide) {
		for (OnScreenButton button : buttons) {
			if (button.getFunction() == buttonToBeBlocked) {
				if (toHide) button.hide();
				else button.doNotHide();
			}
		}
	}

	public void rotateButton(byte buttonToBeRotated, int angle) {
		if ((buttonToBeRotated != BUTTON_PORTAL) || (buttonToBeRotated == BUTTON_PORTAL && !useAnimatedPortalButton)){
			for (OnScreenButton button : buttons) {
				if (button.getFunction() == buttonToBeRotated) {
					button.setAngleInDegrees(angle);
				}
			}
		}

	}

	private void updateShootingPace() {
		if (attacking) {
			//System.out.println("Shot dispersion must be larger and now " + shotDispersion);
			shotDispersion+=(Program.deltaTime * shotDispersionVelocity);
			//System.out.println("Shot dispersion must be larger and now " + shotDispersion);
			if (shotDispersion > 100) shotDispersion = 100;
		} else {
			shotDispersion-=(Program.deltaTime * (shotDispersionVelocity / 10.5f));
			//System.out.println("Shot dispersion must be lower and now " + shotDispersion + "; Step: " + (Program.deltaTime * (shotDispersionVelocity / 8.5f)));
			if (shotDispersion < 0) shotDispersion = 0;
		}
		//System.out.println("Disp: "  + shotDispersion);
	}

	public boolean mustCameraConcentrateToPlayer() {
		if (playerSelectingAnotherWeapon || playerSelectingInventoryObject) return true;
		else return false;
	}

	public void update(GameProcess gameProcess, GameRound gameRound, GameCamera gameCamera, GameMainController gameMainController) {
		hud.update(gameRound);
		if (frameControllersAreBlocked) {
			if (panelAfterMessageClosingTimer.isTime()) {
				frameControllersAreBlocked = false;
			}
		}
		if (!controlIsBlockedThroughMessage) {
			portalButtonMustBeShown = true;
			if (kickingNow) portalButtonMustBeShown = false;
			if (player.isAlive()) {
				if (!player.isTransferingThroughPortal() ) {
					allControlButtonsAreHidden = player.isControlBlocked();
					if (!player.isControlBlocked()) {
						if (player.getStatement() == Person.IN_AIR) hideButton(BUTTON_KICK, true);
						boolean someFrameIsOpened = false;
						if (hud.isInventoryObjectFrameCompleteOpened() || hud.isWeaponFrameCompleteOpened())
							someFrameIsOpened = true;
						if (!someFrameIsOpened && player.getStatement() == Person.ON_GROUND)
							updatePlayerKicking(gameRound);
						if (!kickingNow) {
							hideAllButtons(false);
							if (player.getStatement() == Person.IN_AIR) hideButton(BUTTON_KICK, true);
							if (Program.OS == Program.ANDROID) {
								if (WITH_RECTANGULAR_STICK) updateRectangularMovementStick(gameRound);
								else updateCircleMovementStick(gameRound);
							} else {
								updateKeywordMovement(gameRound);
								updatePlayerWeaponAngleForDesktop(gameRound, gameCamera);
							}
							if (!someFrameIsOpened) {    // only when all frames are closed
								updatePlayerJump(gameRound);
								updatePlayerShooting(gameRound);
								updatePlayerReloading(gameRound);
							} else {
								hideAllButtons(true);
							}
							if (!someFrameIsOpened) {
								if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject)
									updateMovementThroughPortal(gameRound);
							} else {
								portalButtonMustBeShown = false;
								hideButton(BUTTON_PORTAL, true);
							}
							updatePlayerInventoryObjectChanging(gameRound);
							updatePlayerWeaponChanging(gameRound);
							if (!someFrameIsOpened) {
								if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject)
									updateJumpToNextLevel(gameProcess, gameRound);
							}
						}
					} else {
						//System.out.println("Control is blocked throug a message");
					}
				} else hideButton(BUTTON_PORTAL, true);
			} else {
				hideAllButtons(true);
				hideButton(BUTTON_BACK_TO_REDACTOR, false);
				withAdoptingGuiRedrawing = false;
				hud.setRegularRedrawn();
			}

			updateShootingPace();
			updateBackToRedactorButton(gameRound, gameMainController, gameProcess);
		} else {
			hideAllButtons(true);
		}

		updateMessageClosing(gameRound);

		messageAddedThroughInventoryTouch = false;
		if (player.isTransferingThroughPortal() && getButton(BUTTON_PORTAL).isVisible()) {
			hideButton(BUTTON_PORTAL, true);
			System.out.println("Button portal was hidden");
		}
		updateStickVisibilityStatement();
		if (!gameRound.isPlayerWon() && !gameRound.isPlayerLoosed()) {
			hideButton(BUTTON_BACK_TO_REDACTOR, true);
		}
		updateNoAmmoShot(gameRound);

	}

	private void updateNoAmmoShot(GameRound gameRound) {
		hideButton(BUTTON_WITHOUT_AMMO_SHOT, false);
		if (isButtonPressed(BUTTON_WITHOUT_AMMO_SHOT)) {
			if (!player.getActualWeapon().areThereBulletsInMagazine()) {
				//System.out.println("Bullets are not in weapon");
				if (noAmmoShotTimer == null) {
					noAmmoShotTimer = new Timer(NO_AMMO_SOUND_TIME);
					gameRound.getSoundController().setAndPlayAudio(SoundsInGame.SOME_SOUND_2, TrackData.MAX_AUDIO);
					//System.out.println("No ammo sound was added");
				} else if (noAmmoShotTimer.isTime()) {
					gameRound.getSoundController().setAndPlayAudio(SoundsInGame.SOME_SOUND_2);
					//System.out.println("No ammo sound was added");
					noAmmoShotTimer.setNewTimer(NO_AMMO_SOUND_TIME);
				}
			}
		}
	}

	private void updateStickVisibilityStatement() {
		if (withAdoptingGuiRedrawing){
			if (!controlIsBlockedThroughMessage) {
				if (!hudHidden && !playerLoosed && !reloadingNow) {
					if (!allControlButtonsAreHidden) {
						if (WITH_RECTANGULAR_STICK) rectStick.setVisibility(OnScreenButton.VISIBLE);
						else movementStick.setVisibility(OnScreenButton.VISIBLE);
					}
					else {
						if (WITH_RECTANGULAR_STICK) rectStick.setVisibility(!OnScreenButton.VISIBLE);
						else movementStick.setVisibility(!OnScreenButton.VISIBLE);
					}
				}
				else {
					if (WITH_RECTANGULAR_STICK) rectStick.setVisibility(!OnScreenButton.VISIBLE);
					else movementStick.setVisibility(!OnScreenButton.VISIBLE);
				}
			}
			else {
				if (WITH_RECTANGULAR_STICK) rectStick.setVisibility(!OnScreenButton.VISIBLE);
				else movementStick.setVisibility(!OnScreenButton.VISIBLE);
			}
		}
	}

/*
	public void updateOld(GameProcess gameProcess, GameRound gameRound, GameCamera gameCamera) {
		hud.update(gameRound);
		if (frameControllersAreBlocked){
			if (panelAfterMessageClosingTimer.isTime()){
				frameControllersAreBlocked = false;
			}
		}
		if (!controlIsBlockedThroughMessage) {
			if (!player.isAlive()){
				hideAllButtons(true);
				if (!gameRound.isPlayerWon() && !gameRound.isPlayerLoosed()) {
					if (Program.OS == Program.WINDOWS) hideButton(BUTTON_BACK_TO_REDACTOR, false);
				}
			}
			else {
				if (player.isTransferingThroughPortal()) hideAllButtons(true);
				else {
					allControlButtonsAreHidden = player.isControlBlocked();
					if (!player.isControlBlocked()) {
						updatePlayerKicking(gameRound);
						if (!kickingNow) {
							if (Program.OS == Program.ANDROID) {
								if (WITH_RECTANGULAR_STICK) updateRectangularMovementStick(gameRound);
								else updateCircleMovementStick(gameRound);
							}
							else {
								updateKeywordMovement(gameRound);
								updatePlayerWeaponAngle(gameRound, gameCamera);
							}
							updatePlayerJump(gameRound);

							updatePlayerShooting(gameRound);
							updatePlayerReloading(gameRound);
							if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject) updateMovementThroughPortal(gameRound);

							updatePlayerInventoryObjectChanging(gameRound);
							updatePlayerWeaponChanging(gameRound);
							if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject) updateJumpToNextLevel(gameProcess, gameRound);
							//updateBulletTime(gameProcess);
						}
					}
				}
			}
			//portalButtonMustBeShown = true;
			//if (kickingNow) portalButtonMustBeShown = false;
			updateShootingPace();
			if (Program.OS == Program.WINDOWS) updateBackToRedactorButton(gameRound);
		}
		else {
			hideAllButtons(true);
		}
		updateMessageClosing();
		messageAddedThroughInventoryTouch = false;
	}
*/

	private void updateMessageClosing(GameRound gameRound) {
		if (hud.isMessageFrameOpened() && !messageAddedThroughInventoryTouch){
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
				if (hud.isMessageFrameClicked()) {
					System.out.println("Message field is clicked");
					boolean closed = hud.closeMessageFrameIfPossible();
					if (panelAfterMessageClosingTimer == null) panelAfterMessageClosingTimer = new Timer(TIME_TO_DEBLOCK_UPPER_PANEL);
					else panelAfterMessageClosingTimer.setNewTimer(TIME_TO_DEBLOCK_UPPER_PANEL);
					frameControllersAreBlocked = true;
					if (closed && controlIsBlockedThroughMessage){
						if (!hud.isMessageFrameOpened()) {
							System.out.println("Player is not more blocked");
							controlIsBlockedThroughMessage = false;
							gameCameraController.deletePosToConcentrate();
							player.controlWasBlocked(false);
							hud.setRedrawUpperHudOnNextFrame(true);
							//gameRound.getBackgroundFocusMaster().setBackgroundsAsNormalInOneStep();
						}
					}
				}
			}
			if (controlIsBlockedThroughMessage && !hud.areThereShownMessages()){
				controlIsBlockedThroughMessage = false;
				player.controlWasBlocked(false);
				System.out.println("Control is deblocked");
				hud.setRedrawUpperHudOnNextFrame(true);
				//gameRound.getBackgroundFocusMaster().setBackgroundsAsNormalInOneStep();
			}
			if (!hud.areThereShownMessages()){
				System.out.println("There are no messages");
				gameCameraController.deletePosToConcentrate();
				player.controlWasBlocked(false);
				hud.setRedrawUpperHudOnNextFrame(true);
				//gameRound.getBackgroundFocusMaster().setBackgroundsAsNormalInOneStep();
			}
		}
		else if (controlIsBlockedThroughMessage){
			player.controlWasBlocked(false);
			if (gameCameraController.isConcentratePositionSource() == GameCameraController.MESSAGE) {
				gameCameraController.deletePosToConcentrate();
			}
			controlIsBlockedThroughMessage = false;
			System.out.println("Camera pos was deblocked");
			hud.setRedrawUpperHudOnNextFrame(true);
			//gameRound.getBackgroundFocusMaster().setBackgroundsAsNormalInOneStep();
		}
		else if (gameCameraController.areThePositionForConcentrate()){
			if (gameCameraController.isConcentratePositionSource() == GameCameraController.MESSAGE) {
				gameCameraController.deletePosToConcentrate();
				hud.setRedrawUpperHudOnNextFrame(true);
				System.out.println("Delete from control");
			}
		}
	}

	private void updateBulletTime(GameProcess gameProcess) {
		if (false == true) {
			gameProcess.getBulletTimeController().activate();
			System.out.println("Bullet time activated");
		}
	}

	public void setPortalButtonMustBeShown(boolean portalButtonMustBeShown) {
		this.portalButtonMustBeShown = portalButtonMustBeShown;
	}

	private void updateJumpToNextLevel(GameProcess gameProcess, GameRound gameRound) {
		boolean onSomePortal = false;
		for (PortalToAnotherLevel portalToAnotherLevel : gameRound.getPortalsToAnotherLevel()){
			if (portalToAnotherLevel.enter.inZone(gameRound.getPlayer().getPixelPosition())){
				boolean anotherPersonIsOnPortal = false;
				for (Person person : gameRound.getPersons()){
					if (!person.equals(gameRound.getPlayer()) && person.isAlive()){
						if (portalToAnotherLevel.enter.inZone(person.getPixelPosition())){
							System.out.println("enemy is in zone");
							anotherPersonIsOnPortal = true;
						}
					}
				}
				if (!anotherPersonIsOnPortal) onSomePortal = true;
			}
		}
		if (onSomePortal) {
			portalButtonMustBeShown = true;
			playerCanEnterPortal = true;
			hideButton(BUTTON_PORTAL, false);
			if (isButtonPressed(BUTTON_PORTAL)){
				for (PortalToAnotherLevel portalToAnotherLevel : gameRound.getPortalsToAnotherLevel()){
					if (portalToAnotherLevel.enter.inZone(gameRound.getPlayer().getPixelPosition())){
						gameRound.getSoundController().setAndPlayAudio(SoundsInGame.POWER_UP);
						PVector newPlayerPos = new PVector(portalToAnotherLevel.exit.getPosition().x, portalToAnotherLevel.exit.getPosition().y);
						System.out.println("New place must be : " + newPlayerPos);
						PlayerDataSaveMaster playerDataSaveMaster = new PlayerDataSaveMaster(player, PlayerDataSaveMaster.GLOBAL_SAVING, gameRound.getLevelType());
						playerDataSaveMaster.saveData();
						gameProcess.jumpToAnotherLevel(portalToAnotherLevel.getExitLevelNumber(), newPlayerPos);
						if (gameRound.getLevelType() == Program.MAIN_LEVEL){
							gameRound.saveResultsForActualRound();
						}
						gameRound.endActualLevel();
						portalToAnotherLevel.savePlayer(gameRound.getPlayer());
						jumpingToAnotherLevel = true;
						portalButtonMustBeShown = false;
					}
				}
			}
		}
		else {
			playerCanEnterPortal = false;
			portalButtonMustBeShown = false;
		}
		if (!jumpingToAnotherLevel) {
			for (Portal portalToAnotherLevel : gameRound.portals) {
				if (portalToAnotherLevel.enter.inZone(gameRound.getPlayer().getPixelPosition())) {
					playerCanEnterPortal = true;
					portalButtonMustBeShown = true;
				}
			}
		}
	}

	public boolean isPortalButtonMustBeShown() {
		return portalButtonMustBeShown;
	}

	public boolean isPlayerCanEnterPortal() {
		return playerCanEnterPortal;
	}

	private void updatePlayerKicking(GameRound gameRound) {
		kickingNow = gameRound.getPlayer().isKicking();
			for (OnScreenButton button : buttons) {
				if (button.getFunction() == BUTTON_KICK) {
					if (button.getButtonPressedStatement()) {
						if (gameRound.getPlayer().canBeKickMade()) {
							if (!playerSelectingAnotherWeapon) {
								gameRound.getPlayer().makeKick();
								getButton(BUTTON_PORTAL).hide();
								kickingNow = true;
								hideAllButtons(true);
							} else {
								stopWeaponSelecting(gameRound);
							}
						}
					}
				}
			}
		if (Program.OS == Program.DESKTOP){
			if (kickKeyPressed == true) {
				if (gameRound.getPlayer().canBeKickMade()) {
					if (!playerSelectingAnotherWeapon) {
						gameRound.getPlayer().makeKick();
						getButton(BUTTON_PORTAL).hide();
						kickingNow = true;
						hideAllButtons(true);
					}
					else {
						stopWeaponSelecting(gameRound);
					}
				}
			}
		}
		if (reloadingNow || player.getStatement() == Person.IN_AIR) {
			getButton(BUTTON_KICK).hide();
			//System.out.println("Kicking button is hidden");
		}
		else {
			if (!kickingNow) getButton(BUTTON_KICK).doNotHide();
		}
	}

	private void updateWeaponFrameOpening(GameRound gameRound){
		if (Program.isLongPressing() && Program.engine.mousePressed) {        // Long pressing
			hud.clickOnWeaponFrame(gameRound);
			gameRound.clearMemory();
			player.getPlayerBag().updateActualWeaponsData(player);
			hud.updateBulletsNumber(player.getPlayerBag());
			gameRound.getBackgroundFocusMaster().applyEffectToGraphic(AbstractGraphicFocusMaster.NORMAL_ONE_STEP_RATE);
			//gameRound.getSpritesFocusMaster().setGraphicAsAntialisedInOneStep(AbstractGraphicFocusMaster.NORMAL_ONE_STEP_RATE);
		}
	}
	private void updatePlayerWeaponChanging(GameRound gameRound) {
		updateWeaponChangingStatement();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingInventoryObject) {
			if (hud.isMouseOnWeaponFrame() && hud.areFramesFreeToClick()) {
				if (hud.isWeaponFrameCompleteClosed()) {                        // full closed
					updateWeaponFrameOpening(gameRound);
				}
				else {
					if (hud.isWeaponFrameCompleteOpened()){
						updateWeaponOnOpenedFrameSelecting(gameRound);
					}
				}
			}
		}
	}

	private void updateWeaponOnOpenedFrameSelecting(GameRound gameRound){
		if (!Program.isLongPressing()) {        // It is full opened and finger was released after last press
			WeaponType weaponType = hud.getSelectedWeapon();
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {     // Make all action
				if (weaponType != null) {
					player.setWeaponAsActual(weaponType);
					hud.setNewWeapon(weaponType);
					hud.startToCloseWeaponFrame();
					stopWeaponSelecting(gameRound);
					gameRound.changeCroshairType(weaponType);
				} else {
					hud.clickOnWeaponFrame(gameRound);
					gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
				}
			} else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {    // Set secondary frame to the new object
				if (weaponType != null) {
					hud.setFrameToNewWeapon(weaponType);
				}

			}

		}
		else{
			boolean hasCellInternalCells = hud.hasCellInternalCells();
			if (hasCellInternalCells){
				if (hud.canCellBeChanged()) {
					hud.changeCellToSubcell(gameRound.getPlayer());
					ArrayList <WeaponType> weaponsOnPanel = hud.getVisibleWeaponsOnPanel();
					player.getPlayerBag().setVisibleWeaponsOnWeaponPanel(weaponsOnPanel);
					//player.getPlayerBag().setWeaponOnPanel()
				}
				/*System.out.println("CellWas changed");
				WeaponType weaponType = hud.getSelectedWeapon();
				if (weaponType != null) {
					System.out.println("New weapon type " + weaponType);
					player.setWeaponAsActual(weaponType);
					hud.setNewWeapon(weaponType);
					hud.startToCloseWeaponFrame();
					stopWeaponSelecting(gameRound);
				} else {
					System.out.println("No weapon " );
					hud.clickOnWeaponFrame(gameRound);
				}*/

			}
			else System.out.println("This cell has only only one image ");

			WeaponType weaponType = hud.getSelectedWeapon();
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {     // Make all action
				if (weaponType != null) {
					player.setWeaponAsActual(weaponType);
					hud.setNewWeapon(weaponType);
					hud.startToCloseWeaponFrame();
					stopWeaponSelecting(gameRound);
					gameRound.changeCroshairType(weaponType);
				} else {
					hud.clickOnWeaponFrame(gameRound);
				}
			}
			else if (Program.engine.mousePressed) {    // Set secondary frame to the new object
				hud.setFrameToNewWeapon(weaponType);
			}
		}

	}

	/*
	private void updateWeaponOnOpenedFrameSelecting(GameRound gameRound){
		if (!Program.isLongPressing()) {        // It is full opened and finger was released after last press
			WeaponType weaponType = hud.getSelectedWeapon();
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {     // Make all action
				if (weaponType != null) {
					player.setWeaponAsActual(weaponType);
					hud.setNewWeapon(weaponType);
					hud.startToCloseWeaponFrame();
					stopWeaponSelecting(gameRound);
				} else {
					hud.clickOnWeaponFrame(gameRound);
				}
			} else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {    // Set secondary frame to the new object
				if (weaponType != null) {
					hud.setFrameToNewWeapon(weaponType);
				}
			}
		}
		else{
			boolean hasCellInternalCells = hud.hasCellInternalCells();
			if (hasCellInternalCells){


				hud.changeCellToSubcell();
				System.out.println("CellWas changed");
				WeaponType weaponType = hud.getSelectedWeapon();
				if (weaponType != null) {
					System.out.println("New weapon type " + weaponType);
					player.setWeaponAsActual(weaponType);
					hud.setNewWeapon(weaponType);
					hud.startToCloseWeaponFrame();
					stopWeaponSelecting(gameRound);
				} else {
					System.out.println("No weapon " );
					hud.clickOnWeaponFrame(gameRound);
				}

			}
			else System.out.println("This cell has only only one image ");
		}
	}
	 */


	private void updatePlayerInventoryObjectChanging(GameRound gameRound){
		updateInventoryObjectChangingStatement();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingAnotherWeapon) {
			if (hud.isMouseOnInventoryFrame() && hud.areFramesFreeToClick()){	// Finger on frame
				if (isInventoryFrameCompleteClosed()){							// full closed
					if (Program.isLongPressing() && Program.engine.mousePressed){		// Long pressing
						if (hud.isInventoryObjectFrameCompleteClosed()){			    // full closed
							hud.clickOnInventoryObjectFrame(gameRound);
							if (!hud.canObjectFrameOpened()) canNotOpenFrameToShowInventory = true;
							else canNotOpenFrameToShowInventory = false;
							if (canNotOpenFrameToShowInventory) {
								if (Program.debug) System.out.println("Try to open. Click on frame. But can not");
							}
							gameRound.getBackgroundFocusMaster().applyEffectToGraphic(BackgroundFocusMaster.NORMAL_ONE_STEP_RATE);
							gameRound.clearMemory();
						}
					}
					else if (!canNotOpenFrameToShowInventory && !Program.isLongPressed() && Editor2D.prevMousePressedStatement && !Program.engine.mousePressed){	// && Editor2D.prevMousePressedStatement && Program.engine.mousePressed
						clickOnSelectedInventoryObject(gameRound);
					}
					else canNotOpenFrameToShowInventory = false;
				}
				else {
					if (hud.isInventoryObjectFrameCompleteOpened() && !Program.isLongPressing()) {		// It is full opened and finger was released after last press
						int selectedObjectType = hud.getSelectedInventoryObjectType();
						if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement ) {     // Make all action
							if (selectedObjectType >= 0) {
								hud.setNewInventoryObject(selectedObjectType);
								hud.startToCloseInventoryObjectsFrame();
								stopInventoryObjectSelecting(gameRound);
							} else {
								hud.clickOnInventoryObjectFrame(gameRound);
								gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
							}
						} else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {    // Set secondary frame to the new object
							if (selectedObjectType >= 0) {
								hud.setFrameToNewInventoryObject(selectedObjectType);
								player.getPlayerBag().setObjectOnMainFrame(selectedObjectType);
							}
						}

					}
				}
			}
		}
	}

	private SMS createMessageForEnoughMedical(){
		String messageText;
		if (Program.LANGUAGE == Program.RUSSIAN) messageText = "Мне не нужна аптечка. Я в полном порядке. ";
		else messageText = "I don't need to use the medical kit. I feel good";
		SMS sms = new SMS(messageText, SMSController.CLOSE, PortraitPicture.PLAYER_SPEAKING_FACE, 1500);
		return sms;
	}

	private void addNewDissolvingTextToGameWorld(GameRound gameRound, Person person, String text) {
		//String text = abstractCollectable.getStringAddedToWorldByBeGained();
		int valueType = 0;
		DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(person.getPixelPosition().x, person.getPixelPosition().y-person.getHeight()/1.5f, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, text, DissolvingAndUpwardsMovingText.NORMAL_DISSOLVING_TIME, DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, valueType);
		gameRound.addNewDissolvingText(dissolvingText);
	}

	private void lifeAdding(GameRound gameRound, int  objectType){
		float nominalRecoveryValueInPercent = MedicalKit.getLifeInPercentToBeAddedByMedicalKitEating(objectType);
		float maximalAddedValue = nominalRecoveryValueInPercent*player.getMaxLife()/100f;
		float lifeUpToFull = player.getMaxLife() - player.getLife();
		int valueForScreen = 0;
		if (maximalAddedValue <= lifeUpToFull) valueForScreen = (int) maximalAddedValue;
		else valueForScreen = (int) lifeUpToFull;
		player.recoveryLifeInPercent(nominalRecoveryValueInPercent);
		String text = "+" + valueForScreen + " HP";
		addNewDissolvingTextToGameWorld(gameRound, player, text);
		gameRound.getSoundController().setAndPlayAudio(SoundsInGame.MEDICAL_KIT_EATEN);
	}

	private void clickOnSelectedInventoryObject(GameRound gameRound) {
		int objectType = hud.getDefaultInventoryObjectType();
		System.out.println("Player presses the selected object with type " + objectType);
		if (objectType >= 0){
			gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
			if (objectType == AbstractCollectable.SMALL_MEDICAL_KIT || objectType == AbstractCollectable.MEDIUM_MEDICAL_KIT || objectType == AbstractCollectable.LARGE_MEDICAL_KIT){
				if (player.getMaxLife()> player.getLife() && player.isAlive()){
					System.out.println("Player can use the medical kit");
					lifeAdding(gameRound, objectType);
					System.out.println("Player has now: " + player.getLife() + "/" + player.getMaxLife());
					takeObjectFromBagAndFrame(objectType);
				}
				else {
					System.out.println("It's not need to eat the medical kit");
					gameRound.getSoundController().setAndPlayAudio(SoundsInGame.SOME_SOUND);
					addMessageToHud(createMessageForEnoughMedical(), gameRound);
					messageAddedThroughInventoryTouch = true;
				}
			}
			else if (objectType == AbstractCollectable.SYRINGE){
				System.out.println("Player can activate the bullet time");
				GameProcess.activateBulletTime(Syringe.NORMAL_SLOW_TIME);
				takeObjectFromBagAndFrame(objectType);
			}
			else {
				System.out.println("I don't know what I need to do for this object type");
			}
		}

	}

	private void takeObjectFromBagAndFrame(int objectType) {
		player.getPlayerBag().confiscateObject(objectType);
		hud.updateNumberForObjectsOnOpenedFrame(objectType, player.getPlayerBag().getSelectableObjectsNumberByType(objectType));
		hud.setRedrawUpperHudOnNextFrame(true);
		if (!player.getPlayerBag().areThereObjectsForType(objectType)){
			hud.confiscateObjectFromFrame(objectType);

		}
		int objectOnFrame = hud.getDefaultInventoryObjectType();
		player.getPlayerBag().setObjectOnMainFrame(objectOnFrame);
	}

/*
	private void updatePlayerInventoryObjectChangingDoesnotWork(GameRound gameRound){
		updateInventoryObjectChangingStatement();
		if (!Program.engine.mousePressed) mouseReleasingLastFrame = Program.engine.frameCount;
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingAnotherWeapon) {
			if (hud.isMouseOnInventoryFrame() && hud.areFramesFreeToClick()){	// Finger on frame
				int selectedObjectType = hud.getSelectedInventoryObjectType();
				if (Program.isLongPressing() && mouseReleasingLastFrame > inventoryOpeningStartFrame){
					if (mouseReleasingLastFrame > inventoryOpeningStartFrame) {
						hud.clickOnInventoryObjectFrame(gameRound);
						inventoryOpeningStartFrame = Program.engine.frameCount;
					}
				}
				else{
					if (mouseReleasingLastFrame > inventoryOpeningStartFrame){
						if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {     // Make all action
							if (selectedObjectType >= 0) {
								hud.setNewInventoryObject(selectedObjectType);
								hud.startToCloseInventoryObjectsFrame();
								stopInventoryObjectSelecting(gameRound);
							}
							else {
								hud.clickOnInventoryObjectFrame(gameRound);
							}
						}
						else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {    // Set secondary frame to the new object
							if (selectedObjectType >= 0) {
								hud.setFrameToNewInventoryObject(selectedObjectType);
							}
						}
					}
				}
			}
		}
	}*/

	/*
	private void updatePlayerInventoryObjectChangingWithoutLongPressing(GameRound gameRound){
		updateInventoryObjectChangingStatement();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingAnotherWeapon) {
			if (hud.isMouseOnInventoryFrame() && hud.areFramesFreeToClick()){	// Finger on frame
				int selectedObjectType = hud.getSelectedInventoryObjectType();
				if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){	 // Make all action
					if (selectedObjectType >= 0) {
						hud.setNewInventoryObject(selectedObjectType);
						hud.startToCloseInventoryObjectsFrame();
						stopInventoryObjectSelecting(gameRound);
					}
					else {
						hud.clickOnInventoryObjectFrame(gameRound);
					}
				}
				else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement){	// Set secondary frame to the new object
					if (selectedObjectType >= 0) {
						hud.setFrameToNewInventoryObject(selectedObjectType);
					}
				}
			}
		}
	}*/

	/*
	private void updatePlayerInventoryObjectChangingOld(GameRound gameRound){
		updateInventoryObjectChangingStatement();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingAnotherWeapon) {
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
				if (hud.isMouseOnInventoryFrame() && hud.areFramesFreeToClick()){
					int selectedObjectType = hud.getSelectedInventoryObjectType();
					if (selectedObjectType >= 0){
						System.out.println("Click on inventory frame and new object has number " + selectedObjectType);
						hud.setNewInventoryObject(selectedObjectType);
						hud.startToCloseInventoryObjectsFrame();
						stopInventoryObjectSelecting(gameRound);
					}
					else{
						hud.clickOnInventoryObjectFrame(gameRound);
					}
				}
			}
			else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement){
				if (hud.isMousePressedOnInventoryFrame()){
					int selectedObjectType = hud.getSelectedInventoryObjectType();
					if (selectedObjectType >= 0){
						for (int i = 0; i < player.getPlayerBag().getSelectableInBagObjects().size(); i++) {
							if (selectedObjectType == player.getPlayerBag().getSelectableInBagObjects().get(i).getType()){
								System.out.println("Selected object width type " + selectedObjectType);
								player.getPlayerBag().setObjectOnMainFrame(selectedObjectType);
							}

						}
						hud.setFrameToNewInventoryObject(selectedObjectType);
					}
				}
			}
		}
	}*/

	/*
	private void updatePlayerWeaponChangingWithoutLongPressing(GameRound gameRound) {
		updateWeaponChangingStatement();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !frameControllersAreBlocked && !playerSelectingInventoryObject) {
			if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
				if (hud.isMouseReleasedOnWeaponFrame() && hud.areFramesFreeToClick()){
					WeaponType weaponType = hud.getSelectedWeapon();
					//hud.clickOnWeaponFrame();
					if (weaponType != null){
						System.out.println("Click on weapon frame and weapon " + weaponType);
						player.setWeaponAsActual(weaponType);
						hud.setNewWeapon(weaponType);
						hud.startToCloseWeaponFrame();
						stopWeaponSelecting(gameRound);
					}
					else{
						hud.clickOnWeaponFrame(gameRound);
					}
				}
			}
			else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement){
				if (hud.isMouseOnWeaponFrame()){
					WeaponType weaponType = hud.getSelectedWeapon();
					if (weaponType != null){
						for (byte i = 0; i < gameRound.getPlayer().weapons.size(); i++) {
							if (weaponType.equals(gameRound.getPlayer().weapons.get(i).getWeaponType()) || weaponType == gameRound.getPlayer().weapons.get(i).getWeaponType()) {
								gameRound.getPlayer().weapons.get(i).setWeaponAsActual(true);
							}
							else gameRound.getPlayer().weapons.get(i).setWeaponAsActual(false);
						}
						hud.setFrameToNewWeapon(weaponType);
					}
				}
			}
		}
	}*/

	private void updateWeaponChangingStatement() {
		if (hud.isWeaponFrameActive()) playerSelectingAnotherWeapon = true;
		else playerSelectingAnotherWeapon = false;
	}

	private void updateInventoryObjectChangingStatement() {
		if (hud.isInventoryFrameActive()) playerSelectingInventoryObject = true;
		else playerSelectingInventoryObject = false;

	}


	/*
	private void changeWeapon(Person person, boolean which) {
		if (person.weapons.size()>1) {
			byte actualWeaponNumber = 1;
			for (byte i = 0; i < person.weapons.size(); i++) {
				if (person.weapons.get(i).isActual()) actualWeaponNumber = i;	
			}
			if (which == PlayerControl.NEXT_WEAPON) {
				if (actualWeaponNumber==(person.weapons.size()-1)) {
					setWeaponAsActual(person.weapons, (byte)0);
				}
				else setWeaponAsActual(person.weapons, (byte)(actualWeaponNumber+1));
			}
			else {
				if (actualWeaponNumber == 0) {
					setWeaponAsActual(person.weapons, (byte)(person.weapons.size()-1));
				}
				else setWeaponAsActual(person.weapons, (byte)(actualWeaponNumber-1));
			}

		}
		else if (Programm.DEBUG) System.out.println("Can not change weapon. There are not 2 or more weapons");

		
	}
*/

	/*
	private void setWeaponAsActual(ArrayList<FirearmsWeapon> weapons, byte weaponNumber) {
		for (byte i = 0; i < weapons.size(); i++) weapons.get(i).setWeaponAsActual(false);
		weapons.get(weaponNumber).setWeaponAsActual(true);
		hud.setNewWeapon(weapons.get(weaponNumber).getWeaponType());
	}*/

	private void updateMovementThroughPortal(GameRound gameRound) {
		/*
		if (gameRound.mustBeButtonPortalHidden()){
			if (getButton(BUTTON_PORTAL).isVisible()) getButton(BUTTON_PORTAL).hide();
			else  getButton(BUTTON_PORTAL).doNotHide();
		}*/
		//gameRound.mustBeButtonPortalHidden();
		if (player.isTransferingThroughPortal()) {
			for (OnScreenButton button : buttons) {
				if (button.isVisible()) button.hide();
			}
		}
		//else portalButtonMustBeShown = true;
	}

	public void setBackButtonAsVisible(){
		OnScreenButton button = getButton(BUTTON_BACK_TO_REDACTOR);
		button.doNotHide();
	}

	private void updateBackToRedactorButton(GameRound gameRound, GameMainController gameMainController, GameProcess gameProcess){
		OnScreenButton button = getButton(BUTTON_BACK_TO_REDACTOR);
		if (button.isVisible()) {
			if (button.getButtonPressedStatement()) {
				if (!player.isAlive() || (player.isAlive() && (gameRound.isPlayerWon() || gameRound.isPlayerLoosed()))) {
					System.out.println("Back pressed ");
					try {
						gameRound.switchOffMusic();
						if (gameProcess.isLevelType() == ExternalRoundDataFileController.USER_LEVELS) {
							if (gameRound.isRoundStartedFromEditor()) {
								gameMainController.backPressed();
							} else {
								gameMainController.backPressed();
								gameMainController.getMenusController().setNewMenu(MenuType.USER_LEVELS);
							}
						} else {
							if (!BACK_ON_SCREEN_BUTTON_BLOCKED) {
								gameMainController.backPressed();
								gameMainController.getMenusController().setNewMenu(MenuType.CONTINUE_LAST_GAME);
							}
						}
						gameProcess = null;
					} catch (Exception e) {

					}
				}
			}
		}
	}

	private void updatePlayerReloading(GameRound gameRound) {
		//hideButton(BUTTON_RELOAD, false);
		reloadingNow = false;
		if (true) {
			if (!player.getActualWeapon().canAttack() ) {
				if ((!player.getActualWeapon().areThereBulletsInMagazine()) || (gameRound.getPlayer().getActualWeapon().isReloading())){
					if (getButton(BUTTON_SHOOTING).isVisible()) {
						getButton(BUTTON_SHOOTING).hide();
					}
				}
				else if (gameRound.getPlayer().getActualWeapon().isReloading() ){
					if (getButton(BUTTON_KICK).isVisible()){
						getButton(BUTTON_KICK).hide();
					}
				}
			}
			if (!gameRound.getPlayer().getActualWeapon().canBeWeaponReloaded() || gameRound.getPlayer().getActualWeapon().isReloading() || !player.getPlayerBag().areThereAmmoForWeapon(player.getActualWeapon().getWeaponType()) || player.getStatement() == Person.IN_AIR || attacking){
				if (getButton(BUTTON_RELOAD).isVisible()) {
					getButton(BUTTON_RELOAD).hide();
					if (getButton(BUTTON_SHOOTING).isVisible() && gameRound.getPlayer().getActualWeapon().isReloading()) {
						getButton(BUTTON_SHOOTING).hide();
					}
					if (getButton(BUTTON_KICK).isVisible() && gameRound.getPlayer().getActualWeapon().isReloading()){
						getButton(BUTTON_KICK).hide();
					}
				}
			}
			else {
				if (!getButton(BUTTON_RELOAD).isVisible()) {
					getButton(BUTTON_RELOAD).doNotHide();
				}
				if (getButton(BUTTON_RELOAD).getButtonPressedStatement()) {
					player.startToReload();
					gameRound.clearMemory();
					gameRound.getBackgroundFocusMaster().applyEffectToGraphic(BackgroundFocusMaster.NORMAL_ONE_STEP_RATE);
					hud.playerStartedReloading();
					rectStick.hideForNextFrame();
					if (playerSelectingAnotherWeapon) stopWeaponSelecting(gameRound);
					getButton(BUTTON_RELOAD).hide();
					System.out.println("Player started to reload. Reload button was hidden");
				}
			}
			if (gameRound.getPlayer().getActualWeapon().isReloading()) {
				reloadingNow = true;
				if (getButton(BUTTON_SHOOTING).isVisible()) {
					getButton(BUTTON_SHOOTING).hide();
				}
				if (gameRound.getPlayer().getActualWeapon().isReloadCompleted()) {	//Doesnot work
					getButton(BUTTON_SHOOTING).doNotHide();
					//gameRound.getBackgroundFocusMaster().setBackgroundsAsNormalInOneStep();
				}
			}
		}
	}

	private void updatePlayerJump(GameRound gameRound) {
		//System.out.println("Player statement" + gameRound.getPlayer().getStatement());
			if (Program.engine.keyPressed || buttons.get(1).getButtonPressedStatement()) {
				if ((keyUpPressed || buttons.get(1).getButtonPressedStatement()) && 
				gameRound.getPlayer().getStatement() != Person.IN_AIR &&
				!gameRound.getPlayer().isTransferingThroughPortal() &&
				!gameRound.getPlayer().getActualWeapon().isReloading()) {
					//gameRound.getPlayer().makeJump();
					boolean jumpAsFlip = isJumpContraFlip();
					//System.out.println("Jump is flip " + jumpAsFlip );
					player.makeJump(jumpAsFlip);
					//gameRound.addJumpSplash(player);
					gameRound.getSoundController().setAndPlayAudio(SoundsInGame.JUMP_START);
					if (playerSelectingAnotherWeapon) stopWeaponSelecting(gameRound);
				}
			}
			if (kickingNow){
				if (getButton(BUTTON_JUMP).isVisible()) {
					getButton(BUTTON_JUMP).hide();
				}
			}
			else {
				if (gameRound.getPlayer().getStatement() == Person.IN_AIR || gameRound.getPlayer().getActualWeapon().isReloading()) {
					if (getButton(BUTTON_JUMP).isVisible()) {
						getButton(BUTTON_JUMP).hide();
					}
				} else if (gameRound.getPlayer().getStatement() != Person.IN_AIR && !gameRound.getPlayer().getActualWeapon().isReloading()) {
					if (!getButton(BUTTON_JUMP).isVisible()) {
						getButton(BUTTON_JUMP).doNotHide();
					}
				}
			}
				
	}

	private boolean isJumpContraFlip() {
		if (Program.OS == Program.DESKTOP){
			if (shiftPressed){
				if (keyLeftPressed || keyRightPressed){
					return true;
				}
				else return false;
			}
			else return false;
		}
		else {
			if (rectStick.getActualZone() == FivePartsStick.IN_RUN_ZONE){
				return true;
			}
			else {
				return false;
			}
		}
	}

	/*
	private void stopWeaponSelecting() {
		hud.stopWeaponSelecting();
	}*/

	private void stopInventoryObjectSelecting(GameRound gameRound) {
		hud.stopInventoryObjectSelecting();
		gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WEAPON_SELECTED);
		gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
	}

	private void stopWeaponSelecting(GameRound gameRound) {
		hud.stopWeaponSelecting();
		gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WEAPON_SELECTED);
		gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
	}

	private void attack(GameRound gameRound){
		//System.out.println("Dispersion: " + shotDispersion + "; ");
		gameRound.getPlayer().getActualWeapon().attack(shotDispersion / maxShotDispersion, gameRound);
		gameRound.getPlayer().setAttackStartsOnThisFrame();
		gameRound.getSoundController().setAndPlayAudioForShot(gameRound.getPlayer().getActualWeapon().getWeaponType());

		gameRound.addShootingFlash(gameRound.getPlayer());
		if (player.getActualWeapon().getWeaponType() == WeaponType.SMG || player.getActualWeapon().getWeaponType() == WeaponType.HANDGUN) {
			gameRound.createBulletSleeveGraphicForType(player, MoveableSpritesAddingController.HANDGUN_SLEEVE);
		}

		gameCameraController.addScalingByShot();
		hideAllButtons(true);
		attacking = true;
		if (player.getActualWeapon().getWeaponType() != WeaponType.M79 && player.getActualWeapon().getWeaponType() != WeaponType.GRENADE) gameRound.addFullScreenFlash();
	}

	private void updatePlayerShooting(GameRound gameRound) {
		attacking = false;
		if (gameRound.getPlayer().getStatement() != Person.IN_AIR && !gameRound.getPlayer().isPersonRunning()) {
			if (Program.OS == Program.DESKTOP && !hud.isWeaponFrameActive()) {
				if ((Program.engine.mousePressed) &&
						!getButton(BUTTON_PORTAL).getButtonPressedStatement() &&
						!getButton(BUTTON_JUMP).getButtonPressedStatement() &&
						!getButton(BUTTON_RELOAD).getButtonPressedStatement() &&
						!getButton(BUTTON_KICK).getButtonPressedStatement()) {
					if (gameRound.getPlayer().getClass() == Soldier.class) {
						if (gameRound.getPlayer().getActualWeapon().canAttack() &&	!gameRound.getPlayer().isTransferingThroughPortal() &&
								!gameRound.getPlayer().getActualWeapon().isReloading()) {
							if (!playerSelectingAnotherWeapon) {
								if (!isMouseOnHud()) {
									attack(gameRound);
								}
							} else {
								stopWeaponSelecting(gameRound);
							}

						}
					}
				}
			}
			else if (Program.OS == Program.ANDROID) {
				if (getButton(BUTTON_SHOOTING).getButtonPressedStatement()) {
						if (player.getActualWeapon().canAttack() &&
								!player.isTransferingThroughPortal() &&
								!player.getActualWeapon().isReloading() &&
								!player.isKicking()) {
							if (!playerSelectingAnotherWeapon) {
								attack(gameRound);
							} else {
								stopWeaponSelecting(gameRound);
							}

							gameRound.getPlayer().setAttackStartsOnThisFrame();
						}

				}
			}
		}
		else hideButton(BUTTON_SHOOTING, true);
	}

	private boolean isMouseOnHud() {
		if (hud.isMouseOnUpperPanel() || hud.isMouseOnLowerPanel()){
			return true;
		}
		else return false;
	}

	private OnScreenButton getButton(byte action) {
		for (OnScreenButton button : buttons) {
			if (button.getFunction() == action) {
				return button;			
			}
		}
		System.out.println("Can not find any button with this action: " + action);
		return null;		
	}
	
	


	private void updatePlayerWeaponAngleForDesktop(GameRound gameRound, GameCamera gameCamera) {	// Angle to camera but not to mouse
		mutMousePlace.x = Program.engine.mouseX;
		mutMousePlace.y = Program.engine.mouseY+UpperPanel.HEIGHT-(Program.engine.width/13f)/gameCamera.getScale();
		if (!gameRound.getPlayer().getActualWeapon().isReloading() && !gameRound.getPlayer().isTransferingThroughPortal()) {
			PVector playerOnScreenPosition = new PVector();
			playerOnScreenPosition.x = gameRound.getPlayer().getOnScreenPosition(gameCamera).x;
			playerOnScreenPosition.y = gameRound.getPlayer().getOnScreenPosition(gameCamera).y;
			PVector vectorToTarget = PVector.sub(mutMousePlace, playerOnScreenPosition);
			float angle;
			angle = PApplet.degrees((vectorToTarget.heading()));
			applyWeaponAngle(gameRound, angle);
			rotateButton(BUTTON_SHOOTING, (int) gameRound.getPlayer().getWeaponAngle());
			rotateKickButton();
			//System.out.println("New angle: " + angle);
		}
	}

	private void rotateKickButton(){
		int buttonAngle = (int) player.getKickAngleForActualStickOrientation(false);
		if (buttonAngle < 270 && buttonAngle > 90){
			flipButton(BUTTON_KICK, true);
		}
		else flipButton(BUTTON_KICK, false);
		rotateButton(BUTTON_KICK, buttonAngle);
	}

	private void flipButton(byte buttonToBeFliped, boolean flag) {
		for (OnScreenButton button : buttons){
			if (button.getFunction() == buttonToBeFliped){
				button.setFlip(flag);
			}
		}
	}


	private void applyWeaponAngle(GameRound gameRound, float angle) {
		if (Program.OS == Program.DESKTOP) {
			if (angle < 0) angle = 360 + angle;
			if (angle > 360) angle = angle - 360;
		}
		gameRound.getPlayer().setWeaponAngle(angle);

		player.updateOrientationByWeaponAngle();
		if (Program.OS == Program.ANDROID) {
			if (angle < 90 || angle > 270) {
			 if (gameRound.getPlayer().orientation == Person.TO_LEFT) gameRound.getPlayer().orientation = Person.TO_RIGHT;
			}
			if (angle >= 90 && angle <= 270) {
			if (gameRound.getPlayer().orientation == Person.TO_RIGHT) gameRound.getPlayer().orientation = Person.TO_LEFT;
			}
		}
	}

	private void updateRectangularMovementStick(GameRound gameRound) {

		if (!gameRound.getPlayer().getActualWeapon().isReloading()) {
			if (Program.OS == Program.ANDROID) {

				rectStick.update();
				backMovementAndShootingController.update(Program.engine);
				byte zone = rectStick.getActualZone();
				boolean movementSide = rectStick.getRelativeSide();
				if (playerSelectingAnotherWeapon && zone != MovementStick.IN_DEAD_ZONE) stopWeaponSelecting(gameRound);
				if (playerSelectingInventoryObject && zone != MovementStick.IN_DEAD_ZONE) stopInventoryObjectSelecting(gameRound);
				if (isWeaponFrameCompleteClosed() && isInventoryFrameCompleteClosed()){
					if (zone == MovementStick.IN_AIMING_ZONE) {
						gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_ON_AIMING_ZONE);
					}
					else {
						if (zone == MovementStick.IN_GO_ZONE) {
							if (movementSide == Person.TO_LEFT) {
								if (rectStick.isInRightLeftMovementZone()) gameRound.getPlayer().move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
								if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) {
									//if (mustBeDirectionChanged()) {
										//gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;
										//movementSide = Person.TO_RIGHT;
									//}
								}
								gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_GO_LEFT);
							} else {
								if (rectStick.isInRightLeftMovementZone())	gameRound.getPlayer().move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
								if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT) {
									//if (mustBeDirectionChanged()) {
										//gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
										//movementSide = Person.TO_LEFT;
									//}
								}
								gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_GO_RIGHT);
							}
						} else if (zone == MovementStick.IN_RUN_ZONE) {
							if (movementSide == Person.TO_LEFT) {
								if (rectStick.isInRightLeftMovementZone())	gameRound.getPlayer().run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
								if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;
								gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_RUN_LEFT);
							}
							else {
								if (rectStick.isInRightLeftMovementZone())	gameRound.getPlayer().run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
								if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT) 	gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
								gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_RUN_RIGHT);
							}
						}
						else gameRound.getPlayer().setActualByUserPressedMovement(USER_DOESNOT_TOUCH_STICK);
					}
				}
				if (angleResetingByStickRelease || zone != MovementStick.IN_DEAD_ZONE) updatePlayerWeaponAngleForAndroid(gameRound, movementSide, zone);
				if (rectStick.isInRightLeftMovementZone()){
					prevMovementDirection =  rectStick.getRelativeSide();
				}
			}

		}

	}


	private boolean isWeaponFrameCompleteClosed() {
		return hud.isWeaponFrameCompleteClosed();
	}

	private boolean isInventoryFrameCompleteClosed() {
		return hud.isInventoryObjectFrameCompleteClosed();
	}

	private void updateCircleMovementStick(GameRound gameRound) {
		System.out.println("Dont use it");
		if (!gameRound.getPlayer().getActualWeapon().isReloading()) {
			if (Program.OS == Program.ANDROID) {
				movementStick.update();
				byte zone = movementStick.getActualZone();
				if (zone == MovementStick.IN_AIMING_ZONE){

				}
				else {
					if (zone == MovementStick.IN_GO_ZONE) {
						boolean movementSide = movementStick.getRelativeSide();
						if (movementSide == Person.TO_LEFT) {
							if (movementStick.isInRightLeftMovementZone()) gameRound.getPlayer().move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
							if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT)
								gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;
						} else {
							if (movementStick.isInRightLeftMovementZone()) gameRound.getPlayer().move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
							if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT)
								gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
						}
					}
					else if (zone == MovementStick.IN_RUN_ZONE){
						boolean movementSide = movementStick.getRelativeSide();
						if (movementSide == Person.TO_LEFT)	{
							if (movementStick.isInRightLeftMovementZone()) gameRound.getPlayer().run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT);
							if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;
						}
						else {
							if (movementStick.isInRightLeftMovementZone()) gameRound.getPlayer().run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
							if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT)
								gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
						}
					}
				}
				//if (zone != MovementStick.IN_DEAD_ZONE) updatePlayerWeaponAngleForAndroid(gameRound, side, zone);
			}
		}
	}

	private void updatePlayerWeaponAngleForAndroid(GameRound gameRound, boolean side, byte zone) {
		if (Program.OS == Program.ANDROID) {
			float angle = rectStick.getAngle();
			if (zone == MovementStick.IN_GO_ZONE || zone == MovementStick.IN_AIMING_ZONE ){
				if (side == Person.TO_LEFT){

					if (backMovementAndShootingController.mustBeWeaponAngleFlipped()){
						//System.out.println("Player goes to the left. Direction must be changed");
						if (angle > 180) angle = 360-(angle-180);
						else if (angle <= 180) angle = 180-angle;
					}
					//else System.out.println("Player goes to the right. Direction in not changed");

				}
				else {

					if (backMovementAndShootingController.mustBeWeaponAngleFlipped()){
						//System.out.println("Player goes to the right. Direction must be changed");
						if (angle > 270) angle = 180+(360-angle);
						else if (angle < 90) angle = 180-angle;
					}
					//else System.out.println("Player goes to the right. Direction in not changed");

				}

			}
			else {

			}
			if (angleResetingByStickRelease) {
				if (WITH_RECTANGULAR_STICK) applyWeaponAngle(gameRound, angle);
				else applyWeaponAngle(gameRound, angle);
			}
			rotateButton(BUTTON_SHOOTING, (int) gameRound.getPlayer().getWeaponAngle());
			rotateKickButton();
		}
	}

	private void updateKeywordMovement(GameRound gameRound){
		boolean moving = false;
		if (!gameRound.getPlayer().getActualWeapon().isReloading()) {
			gameRound.getPlayer().setActualByUserPressedMovement(USER_DOESNOT_TOUCH_STICK);
			if (Program.engine.keyPressed) {
				if (keyLeftPressed && (gameRound.getPlayer().getStatement() != Person.IN_AIR || CAN_MOVE_IN_AIR)) {	//if (keyLeftPressed && gameRound.getPlayer().getStatement() == Person.ON_GROUND)
					if (!shiftPressed) {
						Soldier soldier = (Soldier) gameRound.getPlayer();
						soldier.move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT, gameRound);
						gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_GO_LEFT);
					}
					else {
						Soldier soldier = (Soldier) gameRound.getPlayer();
						soldier.run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_LEFT, gameRound);
						gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_RUN_LEFT);
					}
					if (playerSelectingAnotherWeapon) stopWeaponSelecting(gameRound);
					if (playerSelectingInventoryObject) stopInventoryObjectSelecting(gameRound);
					if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject) {
						moving = true;
						if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) {
							gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;
						}
					}
				}
				else if (keyRightPressed && (gameRound.getPlayer().getStatement() != Person.IN_AIR || CAN_MOVE_IN_AIR)) {	//else if (keyRightPressed && gameRound.getPlayer().getStatement() == Person.ON_GROUND)
					if (!shiftPressed) {
						//gameRound.getPlayer().move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT);
						Soldier soldier = (Soldier) gameRound.getPlayer();
						soldier.move(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT, gameRound);
						gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_GO_RIGHT);
					}
					else {
						Soldier soldier = (Soldier) gameRound.getPlayer();
						soldier.run(PLAYER_MOVEMENT_WITH_ACCELERATE, Person.TO_RIGHT, gameRound);
						gameRound.getPlayer().setActualByUserPressedMovement(USER_PRESSES_RUN_RIGHT);
					}
					if (playerSelectingAnotherWeapon) stopWeaponSelecting(gameRound);
					if (playerSelectingInventoryObject) stopInventoryObjectSelecting(gameRound);
					if (!playerSelectingAnotherWeapon && !playerSelectingInventoryObject) {
						moving = true;
						if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT) {
							gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
						}
					}
				}
			}
		}
		if (!PLAYER_MOVEMENT_WITH_ACCELERATE && !moving){
			stopPersonAlongX(gameRound.getPlayer());
		}
	}

	private void stopPersonAlongX(Person soldier) {
		//soldier.body.getLinearVelocity().x = 0;
	}

	/*
	private void updatePlayerMovement(GameRound gameRound) {
		//System.out.println("Shift: " + shiftPressed + "; Player: " + gameRound.getPlayer().isPersonRunning());
		if (!gameRound.getPlayer().getActualWeapon().isReloading()) {
			if (Programm.OS == Programm.WINDOWS) {
				if (Programm.engine.keyPressed) {
					if (keyLeftPressed && (gameRound.getPlayer().getStatement() != Person.IN_AIR || CAN_MOVE_IN_AIR)) {	//if (keyLeftPressed && gameRound.getPlayer().getStatement() == Person.ON_GROUND)

						if (!shiftPressed) gameRound.getPlayer().move(Person.TO_LEFT);
						else gameRound.getPlayer().run(Person.TO_LEFT);
						if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) {
							gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;						
						}
					}				
					else if (keyRightPressed && (gameRound.getPlayer().getStatement() != Person.IN_AIR || CAN_MOVE_IN_AIR)) {	//else if (keyRightPressed && gameRound.getPlayer().getStatement() == Person.ON_GROUND)
						if (!shiftPressed) gameRound.getPlayer().move(Person.TO_RIGHT);
						else gameRound.getPlayer().run(Person.TO_RIGHT);
						if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT) {
							gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
						}
					}
				}
			}
			else if (Programm.OS == Programm.ANDROID) {
				//PVector [] touches = new PVector[100];
				ArrayList<PVector> onStickTouches = new ArrayList<PVector>();
				for (int i = 0; i < Programm.engine.touches.length; i++) {
					if (GameMechanics.isPointInCircle(new PVector(Programm.engine.touches[i].x, Programm.engine.touches[i].y), joystick.getBasicPosition(), joystick.MAX_DISTANCE_FOR_ONE_FINGER_TOUCH)){
						onStickTouches.add(new PVector(Programm.engine.touches[i].x, Programm.engine.touches[i].y));
					}				
				}
				PVector center = joystick.getCenterTouchPosition();
				byte actualStickZone = joystick.getStickActualZone();
				if (center.x < joystick.getBasicPosition().x && GameMechanics.isPointInCircle(center, joystick.getBasicPosition(), joystick.MAX_DISTANCE_FOR_ONE_FINGER_TOUCH)) {
					if (actualStickZone == Joystick.STICK_IN_GO_ZONE || actualStickZone == Joystick.STICK_IN_RUN_ZONE)	gameRound.getPlayer().move(Person.TO_LEFT);
					if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_RIGHT) {
						gameRound.getPlayer().orientation = gameRound.getPlayer().TO_LEFT;						
					}
				}
				else if (center.x > joystick.getBasicPosition().x && GameMechanics.isPointInCircle(center, joystick.getBasicPosition(), joystick.MAX_DISTANCE_FOR_ONE_FINGER_TOUCH)) {
					if (actualStickZone == Joystick.STICK_IN_GO_ZONE || actualStickZone == Joystick.STICK_IN_RUN_ZONE) gameRound.getPlayer().move(Person.TO_RIGHT);
					if (gameRound.getPlayer().orientation == gameRound.getPlayer().TO_LEFT) {
						gameRound.getPlayer().orientation = gameRound.getPlayer().TO_RIGHT;
					}
				}
			}
		}

	}
	*/


	private void drawWithRegularUpdating(){
		HeadsUpDisplay.graphic.clear();
		if (!controlIsBlockedThroughMessage) {
			if (portalButtonMustBeShown && !kickingNow && !playerSelectingAnotherWeapon)
				hideButton(BUTTON_PORTAL, false);
			else hideButton(BUTTON_PORTAL, true);
		}
		else hideButton(BUTTON_PORTAL, true);
		blockBackButton();
		hud.draw(hudHidden, buttons, allControlButtonsAreHidden);

		if (!controlIsBlockedThroughMessage) {
			if (!hudHidden && !playerLoosed && !reloadingNow) {
				if (Program.OS == Program.ANDROID) {
					if (!allControlButtonsAreHidden) {
						if (WITH_RECTANGULAR_STICK) rectStick.draw(HeadsUpDisplay.graphic);
						else movementStick.draw(HeadsUpDisplay.graphic);
					}
				}
			}
		}


	}

	private void drawWithAdoptingRedrawing(){
		//Does not ready
		if (!controlIsBlockedThroughMessage) {
			if (portalButtonMustBeShown && !kickingNow && !playerSelectingAnotherWeapon)
				hideButton(BUTTON_PORTAL, false);
			else hideButton(BUTTON_PORTAL, true);
		}
		else hideButton(BUTTON_PORTAL, true);
		blockBackButton();
		hud.draw(hudHidden, buttons, allControlButtonsAreHidden);
		if (withAdoptingGuiRedrawing){
			if (WITH_RECTANGULAR_STICK) rectStick.draw(HeadsUpDisplay.graphic);
			else movementStick.draw(HeadsUpDisplay.graphic);
		}
		else{
			if (!controlIsBlockedThroughMessage) {
				if (!hudHidden && !playerLoosed && !reloadingNow) {
					if (!allControlButtonsAreHidden) {
						if (WITH_RECTANGULAR_STICK) rectStick.draw(HeadsUpDisplay.graphic);
						else movementStick.draw(HeadsUpDisplay.graphic);
					}
					//else
				}
			}
		}
		//System.out.println("Drawn");
	}

	private void blockBackButton() {
		for (OnScreenButton onScreenButton : buttons){
			if (onScreenButton.getFunction() == BUTTON_BACK_TO_REDACTOR){
				onScreenButton.hide();
			}
		}
	}

	private void drawWithAdoptingRedrawingAndUpdatingEveryThirdFrame(){
		//Does not ready
		if (!controlIsBlockedThroughMessage) {
			if (portalButtonMustBeShown && !kickingNow && !playerSelectingAnotherWeapon)
				hideButton(BUTTON_PORTAL, false);
			else hideButton(BUTTON_PORTAL, true);
		}
		else hideButton(BUTTON_PORTAL, true);
		if (Program.engine.frameCount%3==0) {
			HeadsUpDisplay.graphic.clear();
			hud.draw(hudHidden, buttons, allControlButtonsAreHidden);
			if (!controlIsBlockedThroughMessage) {
				if (!hudHidden && !playerLoosed && !reloadingNow) {
					if (!allControlButtonsAreHidden) {
						if (WITH_RECTANGULAR_STICK) rectStick.draw(HeadsUpDisplay.graphic);
						else movementStick.draw(HeadsUpDisplay.graphic);
					}
				}
			}
		}
	}

	private boolean isSomeButtonChangeVisibility(){
		return true;
	}
	
	public void draw(GameCamera gameCamera, boolean drawControl) {
		HeadsUpDisplay.graphic.beginDraw();
		if (withAdoptingGuiRedrawing) drawWithAdoptingRedrawing();
		else {
			drawWithRegularUpdating();
		}
		if (hudHidden && !hudWasClearedAfterLastHidding){
			hud.clearLowerPanel();
		}
		if (clearLowerHudForNextFrame){
			hud.clearLowerPanel();
			System.out.println("Panel is cleared!");
			clearLowerHudForNextFrame = false;
			//clearLowerHudForNextFrame = false;
		}

		//if (hudHidden ) System.out.println("HUD is hidden");
		HeadsUpDisplay.graphic.endDraw();
	}

	public void setControlBlocked(boolean blocked){
		if (blocked){
			System.out.println("Player is blocked");
			player.controlWasBlocked(true);
		}
		controlIsBlockedThroughMessage = blocked;
	}

	public boolean isButtonPressed(byte buttonMission) {
		boolean isPressed = false;
		for (OnScreenButton button : buttons) {
			if (button.getFunction() == buttonMission) {
				if (button.getButtonPressedStatement() && button.isVisible()) {
					return true;				
				}
			}
		}
		return isPressed;
	}

	public static boolean isUserPressRunning(){
		if (Program.OS == Program.DESKTOP){
			if (shiftPressed) return true;
			else return false;
		}
		else if (Program.OS == Program.ANDROID){
			return false;
		}
		else return false;
	}

	public void updateButtonShootingButtonStatement(Soldier player) {
		System.out.println("weapong visible or not testing");
		if (player.getActualWeapon().areThereBulletsInMagazine()){
			for (OnScreenButton button : buttons){
				if (button.getFunction() == BUTTON_SHOOTING){
					//if (!button.isVisible()) {
						hideButton(BUTTON_SHOOTING, false);
						System.out.println("Shooting button is shown");
					//}
				}
			}
		}
		else {
			for (OnScreenButton button : buttons) {
				if (button.getFunction() == BUTTON_SHOOTING) {
					//if (button.isVisible()) {
						hideButton(BUTTON_SHOOTING, true);
						System.out.println("Shooting button is hidden");
					//}
				}
			}
		}
	}

	public void hideCompleteHud(boolean flag, boolean playerLoosed) {
		if (flag && !hudHidden) {
			hudHidden = true;
			hudWasClearedAfterLastHidding = false;
			//hud.clearLowerPanel();
		}
		else if (!flag && hudHidden){
			hudWasClearedAfterLastHidding = false;
			hudHidden = false;
			//hud.clearLowerPanel();
		}
		this.playerLoosed = playerLoosed;
	}

    public void addMessageToHud(SMS message, GameRound gameRound) {
		hud.addMessage(message, gameRound);
		if (message.getAbonent() == PortraitPicture.SMARTPHONE_WITH_UNREAD_MESSAGE || message.getAbonent() == PortraitPicture.SMARTPHONE_WITH_READ_MESSAGE){
			gameRound.getSoundController().setAndPlayAudio(SoundsInGame.OBJECT_ACHIVED);
		}
    }

	public void setCPUUpdatingFrequency(int frequency){
		hud.setCPUUpdatingFrequency(frequency);
	}

    public boolean isUpperPanelFreeFromMessages() {
		return !hud.areThereShownMessages();
    }

	public void clearLowerHudForOneFrame() {
		//clearLowerHudForNextFrame = true;
		//rectStick.hideForOneFrame();
	}

	private class BackMovementAndShootingController{
		private final boolean ABILITY_GO_BACK_AND_SHOOT_FORWARD = true;

		private final boolean SHOOTING = true;

		private int shootStartFrame = -1;
		private boolean soldierOrientationByShotStart;
		private boolean actualShootingStatement;
		private boolean previousShootingStatement;
		public BackMovementAndShootingController() {
		}

		private void updateShootingStatement(){
			if (player.getActualWeapon().areThereBulletsInMagazine()) {
				if (getButton(BUTTON_SHOOTING).isButtonTouched()) {
					actualShootingStatement = SHOOTING;
				}
				else {
					actualShootingStatement = !SHOOTING;
					shootStartFrame = -1;
				}
			}
			else {
				actualShootingStatement = !SHOOTING;
				shootStartFrame = -1;
			}
		}
		protected void update(PApplet engine){
			updateShootingStatement();
			if (previousShootingStatement != actualShootingStatement){
				shootStartFrame = engine.frameCount;
				soldierOrientationByShotStart = player.orientation;
			}
			previousShootingStatement = actualShootingStatement;
		}

		protected boolean mustBeWeaponAngleFlipped() {
			if (actualShootingStatement == SHOOTING) {
				if (rectStick.getRelativeSide() == soldierOrientationByShotStart) {
					return false;
				}
				else return true;
			}
			return false;
		}
	}
}
