package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gamecontrollers.BulletTimeController;
import com.mgdsstudio.blueberet.gamecontrollers.MoveableSpritesAddingController;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.AbstractBeretColorMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class HumanAnimationController extends PersonAnimationController{
    public static MainGraphicController mainGraphicController;

    protected static SpriteAnimation runAnimation, idleAnimation, kickAnimation, flipJumpAnimation, reloadAnimation;
    protected static SpriteAnimation goCompleteBody;    //  only for
    protected static StaticSprite jumpUpSprite, jumpDownSprite, hittedSprite, deadStayingSprite, deadBackLayingSprite, deadFrontLayingSprite, waitingSprite;
    protected WeaponAngleGraphicController weaponAngleGraphicController;

    //muttable
    private final static ArrayList<SpriteAnimation> mutAllAnimations = new ArrayList<>(11);

    protected final static boolean SHOW = true;
    protected final static boolean HIDE = false;
    protected boolean actualDrawingStatement = SHOW;

    protected final int STATEMENT_CHANGING_NUMBER = 16;
    protected int timeToNextChanging;
    protected Timer timerToChangeVisibility;

    protected final static float stretchingCoef = 6.6f;
    public static int ANIMATION_FREQUENCY = 11;
    public static int IDLE_ANIMATION_FREQUENCY = ANIMATION_FREQUENCY/2;
    public static int NORMAL_SPRITE_DIMENSION = (int)(stretchingCoef*25f / 0.7f);

    public static float criticalVelocityToStartRunning = 420f;
    public static final float criticalVelocityToStartGoing = 150f;
    protected Human human;
    protected boolean actualJumpStartedAsSimple = false;
    protected boolean actualJumpStartedAsFlip = false;
    protected final int KICK_ATTACK_SPRITE_NUMBER = 3;

    protected final float angleDispersionForDeadSprite = 5f;
    protected Timer bodyToStaticTransformationTimer;
    protected final int TIME_TO_MAKE_CORPSE_STATIC = 1700;
    protected boolean waiting;
    protected boolean playerControlBlocked;
    protected WeaponType weaponType;

    private  boolean graphicUploaded;

    protected final static String PATH_TO_SHOTGUN_ANIMATION = "Girl shotgun"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_SMG_ANIMATION = "Girl smg"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_PISTOL_ANIMATION = "Girl pistol"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_M79_ANIMATION = "Girl gl"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_SO_SHOTGUN_ANIMATION = "Girl SO"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_REVOLVER_ANIMATION = "Girl revolver"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected final static String PATH_TO_GRENADE_ANIMATION = "Girl grenade"+ TextureDecodeManager.getExtensionForSpriteGraphicFile();


    /*

    protected final static String PATH_TO_SHOTGUN_ANIMATION = "Girl shotgun.gif";
    protected final static String PATH_TO_SMG_ANIMATION = "Girl smg.gif";
    protected final static String PATH_TO_PISTOL_ANIMATION = "Girl pistol.gif";
    protected final static String PATH_TO_M79_ANIMATION = "Girl gl.gif";
    protected final static String PATH_TO_SO_SHOTGUN_ANIMATION = "Girl SO.gif";
    protected final static String PATH_TO_REVOLVER_ANIMATION = "Girl revolver.gif";
    protected final static String PATH_TO_GRENADE_ANIMATION = "Girl grenade.gif";
    */

    //protected final static String PATH_TO_SNIPER_RIFFLE_ANIMATION = "Girl sniper.gif";

    protected final float sleevesImageDimensionCoef = 1.0f;
    protected final float shotgunSleevesImageDimensionCoef = 0.75f;
    protected boolean reloadingCompletedOnThisFrame = false;
    protected boolean reloadingSoundWasAdded = false;
    private boolean stepSoundAdded;
    private boolean magazineAddedSoundAdded;
    private final boolean USE_EXTERNAL_JUMP_ANIMATION_TYPE_SELECTION = true;
    private boolean jumpAsContraFlip;

    private final Vec2 mutPlayerPos = new Vec2(0,0);

    //private SoundInGameController soundController;


    public HumanAnimationController(Human soldier, String path, boolean bulletTimeActivated){
        human = soldier;
        this.weaponType = soldier.getActualWeapon().getWeaponType();
        if (bulletTimeActivated) ANIMATION_FREQUENCY /= (BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS/BulletTimeController.bulletTimeCoefForPlayer);
        timeToNextChanging = soldier.getTimeAfterAttackInGodMode()/STATEMENT_CHANGING_NUMBER;   //=200 ms
        if (!graphicUploaded) loadImageGraphic((int)(soldier.getPersonWidth()*1.07f), getPathToActualSpritesheet(weaponType));
        setUniqueAnimationData();
        if (mainGraphicController != null) {
            System.out.println("Animation was uploaded succesfully");
            loadAnimationData(mainGraphicController);
        }
    }



    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public Human getHuman() {
        return human;
    }

    public boolean isKickMoment(){
        if (human.isKicking()){
            if (kickAnimation.getActualSpriteNumber() == KICK_ATTACK_SPRITE_NUMBER){
                System.out.println("It is moment for kicking");
                return true;
            }
            else {
                System.out.println("It is not the moment for kicking" + kickAnimation.getActualSpriteNumber());
                return false;
            }
        }
        else {
            System.out.println("Player is not kicking now");
            return false;
        }
    }

    protected void resetJumpingAnimations() {
        actualJumpStartedAsFlip = false;
        actualJumpStartedAsSimple = false;
    }



    @Override
    public void stopBlinking(){
        //System.out.println("Is not stopped blink");
    }

    public boolean isKickAnimationEnded() {
        if (kickAnimation.isAnimationAlreadyShown()){
            System.out.println("Animation ends");
            kickAnimation.reset();
            return true;
        }
        else return false;
    }

    public static String getPathToActualSpritesheet(WeaponType weaponType){
        if (weaponType == WeaponType.SHOTGUN) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_SHOTGUN_ANIMATION));
        else if (weaponType == WeaponType.HANDGUN) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_PISTOL_ANIMATION));
        else if (weaponType == WeaponType.SMG) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_SMG_ANIMATION));

        else if (weaponType == WeaponType.REVOLVER) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_REVOLVER_ANIMATION));
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_SO_SHOTGUN_ANIMATION));
        else if (weaponType == WeaponType.GRENADE) return (Program.getAbsolutePathToAssetsFolder(PATH_TO_GRENADE_ANIMATION));
        else return (Program.getAbsolutePathToAssetsFolder(PATH_TO_M79_ANIMATION));
    }



    public void setControlBlocked(boolean blocked){
        playerControlBlocked = blocked;
    }

    public boolean isPlayerControlBlocked() {
        return playerControlBlocked;
    }

    @Override
    public ArrayList<SpriteAnimation> getAnimationsList() {

        //ArrayList <SpriteAnimation> spriteAnimations = new ArrayList<>(10);
        mutAllAnimations.clear();
        //mutAllAnimations.add(goAnimation);
        mutAllAnimations.add(runAnimation);
        mutAllAnimations.add(idleAnimation);
        mutAllAnimations.add(kickAnimation);
        mutAllAnimations.add(flipJumpAnimation);
        mutAllAnimations.add(reloadAnimation);
        mutAllAnimations.add(weaponAngleGraphicController.getShotBodyAnimationTo0());
        mutAllAnimations.add(weaponAngleGraphicController.getShotBodyAnimationTo45());
        mutAllAnimations.add(weaponAngleGraphicController.getShotBodyAnimationTo315());
        mutAllAnimations.add(weaponAngleGraphicController.getGoLegsAnimation());
        return mutAllAnimations;
    }

    @Override
    public SpriteAnimation getAnimationForType(int type) {
        if (type == RELOAD) return reloadAnimation;
        else if (type == SHOT) return weaponAngleGraphicController.getShotBodyAnimationTo0();
        //else if (type == GO) return goAnimation;
        else if (type == RUN) return runAnimation;
        else if (type == IDLE) return idleAnimation;
        else if (type == KICK) return kickAnimation;
        else if (type == FLIP_JUMP) return flipJumpAnimation;
        else return null;
        //System.out.println("This function is not overriden");
        //return null;
    }

    public void update(GameRound gameRound){
        //weaponAngleGraphicController.update(gameRound);
    }

    protected void drawPlayerReloadingAnimation(GameCamera gameCamera, boolean flip, SoundInGameController soundController) {
        reloadAnimation.update();
        if (mustBeMagizineAddingSoundSwitchedOn()){
            if (!magazineAddedSoundAdded){
                magazineAddedSoundAdded = true;
                soundController.setAndPlayAudio(SoundsInGame.MAGAZINE_TO_WEAPON);
            }
        }
        else if (magazineAddedSoundAdded == true){
            magazineAddedSoundAdded = false;
        }
    }

    protected abstract boolean mustBeMagizineAddingSoundSwitchedOn();

    protected void loadImageGraphic(int width, String pathToAnimationDataFile){
        float yShifting = 5;
        width*=(stretchingCoef*0.7f);
        NORMAL_SPRITE_DIMENSION = width;
        int height=(int)(width);
        int spritesForAnimation = 4;
        int sourceSingleImageDimension = 96;
        AnimationDataToStore runAnimationData = new AnimationDataToStore(pathToAnimationDataFile, 0,0, sourceSingleImageDimension*spritesForAnimation,sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)4, ANIMATION_FREQUENCY);
        runAnimation = new SpriteAnimation(runAnimationData);
        AnimationDataToStore flipJumpAnimationData = new AnimationDataToStore(pathToAnimationDataFile,0,sourceSingleImageDimension, sourceSingleImageDimension*spritesForAnimation,sourceSingleImageDimension*2, (int) width, (int) height, (byte)1, (byte)4, ANIMATION_FREQUENCY);
        flipJumpAnimation = new SpriteAnimation(flipJumpAnimationData);
        int yShiftingForDeadSprite = 5;
        ImageZoneFullData deadStayingSpriteData = new ImageZoneFullData(pathToAnimationDataFile, sourceSingleImageDimension*4,sourceSingleImageDimension*3+yShiftingForDeadSprite, 5*sourceSingleImageDimension,sourceSingleImageDimension*4+yShiftingForDeadSprite);
        deadStayingSprite = new StaticSprite(deadStayingSpriteData, width, height);
        ImageZoneFullData jumpToUpData = new ImageZoneFullData(pathToAnimationDataFile, 0,sourceSingleImageDimension*3, sourceSingleImageDimension,sourceSingleImageDimension*4);
        jumpUpSprite = new StaticSprite(jumpToUpData, width, height);
        ImageZoneFullData jumpDownData = new ImageZoneFullData(pathToAnimationDataFile, sourceSingleImageDimension,sourceSingleImageDimension*3, 2*sourceSingleImageDimension,sourceSingleImageDimension*4);
        jumpDownSprite = new StaticSprite(jumpDownData, width, height);
        int additionalXShiftingForWaiting = 0;
        ImageZoneFullData waitingSpriteData = new ImageZoneFullData(pathToAnimationDataFile, additionalXShiftingForWaiting+sourceSingleImageDimension*4,768, sourceSingleImageDimension*5+additionalXShiftingForWaiting,768+sourceSingleImageDimension);
        waitingSprite = new StaticSprite(waitingSpriteData, width, height);
        int additionalXShiftingForMovement = +1;
        AnimationDataToStore goAnimationData = new AnimationDataToStore(pathToAnimationDataFile, additionalXShiftingForMovement,768, sourceSingleImageDimension*spritesForAnimation+additionalXShiftingForMovement,768+sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)4, ANIMATION_FREQUENCY);
        AnimationDataToStore kickAnimationData = new AnimationDataToStore(pathToAnimationDataFile, 0,768+sourceSingleImageDimension, sourceSingleImageDimension*5,768+2*sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)5, ANIMATION_FREQUENCY);

        kickAnimation = new SpriteAnimation(kickAnimationData);
        spritesForAnimation = 9;
        int xShifting = -3;
        AnimationDataToStore idleAnimationData = new AnimationDataToStore(pathToAnimationDataFile, (int)0+xShifting,(int)(768-sourceSingleImageDimension), sourceSingleImageDimension*spritesForAnimation+xShifting,768+sourceSingleImageDimension-sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)9, (int)(ANIMATION_FREQUENCY /2f));

        idleAnimation = new SpriteAnimation(idleAnimationData);
        AnimationDataToStore reloadAnimationData = new AnimationDataToStore(pathToAnimationDataFile, (int)415,(int)0, (int)(415+6*sourceSingleImageDimension), (int)(2*sourceSingleImageDimension), (int) width, (int) height, (byte)2, (byte)6, (int)(ANIMATION_FREQUENCY /2f));

        reloadAnimation = new SpriteAnimation(reloadAnimationData);
        hittedSprite = jumpUpSprite;
        loadLayingSprites(sourceSingleImageDimension, width, height, pathToAnimationDataFile);

        weaponAngleGraphicController = new WeaponAngleGraphicController(human, width, height, pathToAnimationDataFile, sourceSingleImageDimension, ANIMATION_FREQUENCY);
        graphicUploaded = true;
    }

    private void loadLayingSprites(int sourceSingleImageDimension, int width, int height, String pathToAnimationDataFile){
        int xShiftingForDeadSprite = -4;
        int yShiftingForDeadSprite = 0;
        float dimensionsCoef = 1.15f;
        ImageZoneFullData deadBackLayingSpriteData = new ImageZoneFullData(pathToAnimationDataFile, sourceSingleImageDimension*2+xShiftingForDeadSprite,sourceSingleImageDimension*3+yShiftingForDeadSprite, 3*sourceSingleImageDimension+xShiftingForDeadSprite,sourceSingleImageDimension*4+yShiftingForDeadSprite);
        deadBackLayingSprite = new StaticSprite(deadBackLayingSpriteData, (int)(width*dimensionsCoef), (int)(height*dimensionsCoef));
        xShiftingForDeadSprite = -9;
        ImageZoneFullData deadFrontLayingSpriteData = new ImageZoneFullData(pathToAnimationDataFile, sourceSingleImageDimension*6+xShiftingForDeadSprite,sourceSingleImageDimension*3+yShiftingForDeadSprite, 7*sourceSingleImageDimension+xShiftingForDeadSprite,sourceSingleImageDimension*4+yShiftingForDeadSprite);
        deadFrontLayingSprite = new StaticSprite(deadFrontLayingSpriteData, width, height);
    }



    protected void drawDeadSprite(GameCamera gameCamera, boolean flip){
        int bodyAngle = (int)(PApplet.degrees(human.body.getAngle()));
        //if (Program.debug) System.out.print("Corpse angle: " + bodyAngle);
        if (bodyAngle >(270-angleDispersionForDeadSprite)&& bodyAngle <(270+angleDispersionForDeadSprite)){
            bodyAngle-=360;
            human.body.setTransform(human.body.getPosition(), human.body.getAngle()-2*PApplet.PI);
        }
        //if (Program.debug) System.out.println("; Corpse angle after transformation: " + bodyAngle);
        if ((bodyAngle > (90-angleDispersionForDeadSprite)) && (bodyAngle < (90+angleDispersionForDeadSprite))){
            if (bodyToStaticTransformationTimer == null) bodyToStaticTransformationTimer = new Timer(TIME_TO_MAKE_CORPSE_STATIC);
            if (flip) {
                deadFrontLayingSprite.draw(gameCamera, human.body, flip);
            }
            else deadBackLayingSprite.draw(gameCamera, human.body, flip);
        }
        else if ((bodyAngle < (-90+angleDispersionForDeadSprite)) && (bodyAngle > (-90-angleDispersionForDeadSprite))){
            //System.out.println("Player lays to right side; Flip: " + flip);
            if (bodyToStaticTransformationTimer == null) bodyToStaticTransformationTimer = new Timer(TIME_TO_MAKE_CORPSE_STATIC);
            if (!flip) {
                //System.out.println("deadFrontLayingSprite: " + (deadFrontLayingSprite == null));
                deadFrontLayingSprite.draw(gameCamera, human.body, flip);
            }
            else deadBackLayingSprite.draw(gameCamera, human.body, flip);
            //angleDispersionForDeadSprite
        }
        else {
            if (bodyToStaticTransformationTimer != null) bodyToStaticTransformationTimer = null;
            deadStayingSprite.draw(gameCamera, human.body, flip);
        }
        if (bodyToStaticTransformationTimer != null && bodyToStaticTransformationTimer.isTime() && human.body.getType() != BodyType.STATIC){
            human.body.setType(BodyType.STATIC);
            //System.out.println("Player body is static");
        }
    }

    protected void drawJumping(float weaponAngle, GameCamera gameCamera, boolean underAttack, boolean jumpAfterAttack, boolean flip){
        if (!USE_EXTERNAL_JUMP_ANIMATION_TYPE_SELECTION) {
            if (!actualJumpStartedAsSimple && !actualJumpStartedAsFlip) {
                System.out.println("Vel: " + PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(human.body.getLinearVelocity().x)));
                if (PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(human.body.getLinearVelocity().x)) > criticalVelocityToStartRunning) {
                    actualJumpStartedAsFlip = true;
                    actualJumpStartedAsSimple = false;
                } else {
                    actualJumpStartedAsFlip = false;
                    actualJumpStartedAsSimple = true;
                }
            }
        }
        else {
            actualJumpStartedAsFlip = jumpAsContraFlip;
        }
      if (actualJumpStartedAsFlip) {
            flipJumpAnimation.update();
            flipJumpAnimation.draw(gameCamera, new Vec2(human.getPixelPosition().x, human.getPixelPosition().y), 0f, flip);
        } else {
            //System.out.println("Prev vel: " + player.getPrevisiousLinearSpeedAlongY());
            if (human.getPrevisiousLinearSpeedAlongY() < 0) {
                jumpUpSprite.draw(gameCamera, human.body, flip);
            } else jumpDownSprite.draw(gameCamera, human.body, flip);
        }
    }

    public Tileset getTilesetForActualGraphic(){
        //this.mainGraphicController = mainGraphicController;
        Tileset tileset = mainGraphicController.getTilesetUnderPath(getPathToActualSpritesheet(weaponType));
        return tileset;
    }

    public void setImageForActualTileset(Image image){
        Tileset tileset = mainGraphicController.getTilesetUnderPath(getPathToActualSpritesheet(weaponType));
        tileset.setPicture(image);

    }

    public void loadAnimationData(MainGraphicController mainGraphicController) {

        this.mainGraphicController = mainGraphicController;
        Tileset tileset = mainGraphicController.getTilesetUnderPath(getPathToActualSpritesheet(weaponType));
        deadStayingSprite.loadSprite(tileset);
        deadBackLayingSprite.loadSprite(tileset);
        deadFrontLayingSprite.loadSprite(tileset);
        jumpUpSprite.loadSprite(tileset);
        jumpDownSprite.loadSprite(tileset);
        runAnimation.loadAnimation(tileset);
        flipJumpAnimation.loadAnimation(tileset);
        //goAnimation.loadAnimation(tileset);
        idleAnimation.loadAnimation(tileset);
        kickAnimation.loadAnimation(tileset);
        reloadAnimation.loadAnimation(tileset);
        weaponAngleGraphicController.loadAnimationData(mainGraphicController);
        waitingSprite.loadSprite(tileset);
        if (Program.withBeretColorChanging) changeColorForAllAnimations();
        System.out.println("Player graphic was uploaded");
    }

    protected void changeColorForAllAnimations() {
        changeBeretColorForAnimation(runAnimation);
    }

    protected void changeBeretColorForAnimation(SpriteAnimation animation) {
        if (AbstractBeretColorMaster.exists()){
            BeretColorLoadingMaster master = new BeretColorLoadingMaster(Program.engine);
            master.loadData();
            TwiceColor beretColor = master.getBeretColor();
            BeretColorChanger beretColorChanger = new BeretColorChanger();

            beretColorChanger.changeBeretColor(beretColor.getDarkColor(), beretColor.getBrightColor(), animation.getImage());
        }
        else {
            System.out.println("Can not change beret color");
        }

    }

    protected abstract void setUniqueAnimationData();


    public void draw(float weaponAngle, GameCamera gameCamera, boolean underAttack, boolean jumpAfterAttack, SoundInGameController soundController){
        PVector playerPos = human.getPixelPosition();
        mutPlayerPos.x = playerPos.x;
        mutPlayerPos.y = playerPos.y;
        if (!underAttack && actualDrawingStatement == HIDE) actualDrawingStatement = SHOW;
        boolean flip = false;
        if (weaponAngle<270 && weaponAngle>90) {
            flip = true;
        }
        if (human.isDead()){
            if (actualDrawingStatement == HIDE) actualDrawingStatement = SHOW;
            drawDeadSprite(gameCamera, flip);
        }
        else {
            if (underAttack) {
                if (timerToChangeVisibility == null) {
                    timerToChangeVisibility = new Timer(timeToNextChanging);
                    actualDrawingStatement = HIDE;
                } else {
                    if (timerToChangeVisibility.isTime()) {
                        timerToChangeVisibility.setNewTimer(timeToNextChanging);
                        if (actualDrawingStatement == HIDE) actualDrawingStatement = SHOW;
                        else actualDrawingStatement = HIDE;
                    }
                }
            }
            if (jumpAfterAttack && human.getStatement() == Person.IN_AIR) {
                if (human.getStatement() == Person.IN_AIR) {
                    if (actualDrawingStatement == SHOW) hittedSprite.draw(gameCamera, human.body, flip);
                }
            } else {
                if (actualDrawingStatement == SHOW) {
                    if (human.getActualWeapon().isReloading()) {
                        drawPlayerReloadingAnimation(gameCamera, flip, soundController);
                    } else {
                        if (human.getStatement() == Person.IN_AIR) {
                            drawJumping(weaponAngle,gameCamera,underAttack,jumpAfterAttack, flip);
                        }
                        else {
                            if (human.isKicking()){
                                kickAnimation.update();
                                kickAnimation.draw(gameCamera, new Vec2(human.getPixelPosition().x, human.getPixelPosition().y), 0f, flip);
                            }
                            else {
                                if (!playerControlBlocked) {
                                    if (waiting || underAttack && human.getStatement() != Person.IN_AIR) {
                                        waitingSprite.draw(gameCamera, human.body, flip);
                                    } else {
                                        if (PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(human.body.getLinearVelocity().x)) < criticalVelocityToStartGoing && (human.getActualByUserPressedMovement() != PlayerControl.USER_PRESSES_GO_RIGHT && human.getActualByUserPressedMovement() != PlayerControl.USER_PRESSES_GO_LEFT && human.getActualByUserPressedMovement() != PlayerControl.USER_PRESSES_RUN_RIGHT && human.getActualByUserPressedMovement() != PlayerControl.USER_PRESSES_RUN_LEFT)) {
                                            float weapongAngle = human.getWeaponAngle();
                                            if (!weaponAngleGraphicController.isPlayerShooting() && !human.isShootingAtThisFrame() && ((weapongAngle > 359 && weapongAngle < 361) || (weapongAngle < 1 && weapongAngle > -1) || (weapongAngle > 179 && weapongAngle < 181))) {
                                                idleAnimation.update();
                                                idleAnimation.draw(gameCamera, new Vec2(human.getPixelPosition().x, human.getPixelPosition().y), 0f, flip);
                                            } else {
                                                if (human.isShootingAtThisFrame()) {
                                                    weaponAngleGraphicController.setShotStarted();
                                                }
                                                weaponAngleGraphicController.drawStaying(weaponAngle, gameCamera, flip, human.getActualWeapon().areThereBulletsInMagazine());
                                            }
                                        }
                                        else {
                                            if (PApplet.abs(PhysicGameWorld.controller.scalarWorldToPixels(human.body.getLinearVelocity().x)) > criticalVelocityToStartRunning && ( human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_RUN_RIGHT || human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_RUN_LEFT)) {
                                                runAnimation.update();
                                                updateStepAdding(runAnimation, soundController);
                                                runAnimation.draw(gameCamera, new Vec2(human.getPixelPosition().x, human.getPixelPosition().y), 0f, flip);
                                            }
                                            else {
                                                //System.out.println("Going");
                                                if (human.isShootingAtThisFrame()) {
                                                    weaponAngleGraphicController.setShotStarted();
                                                }
                                                if ((human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_GO_RIGHT || human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_GO_LEFT || human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_RUN_RIGHT || human.getActualByUserPressedMovement() == PlayerControl.USER_PRESSES_RUN_LEFT))  weaponAngleGraphicController.drawGoing(weaponAngle, gameCamera, flip,  human.getActualWeapon().areThereBulletsInMagazine(), soundController);
                                                else {
                                                    weaponAngleGraphicController.drawStaying(weaponAngle, gameCamera, flip, human.getActualWeapon().areThereBulletsInMagazine());

                                                    /*
                                                    if (weaponAngleGraphicController.isAngleSpecific()) {
                                                        weaponAngleGraphicController.drawStaying(weaponAngle, gameCamera, flip, player.getActualWeapon().areThereBulletsInMagazine());
                                                    }
                                                    else {
                                                        idleAnimation.update();
                                                        idleAnimation.draw(gameCamera, mutPlayerPos, 0f, flip);
                                                        //weaponAngleGraphicController(weaponAngle, gameCamera, flip, player.getActualWeapon().areThereBulletsInMagazine());
                                                    }*/
                                                }
                                            }
                                        }
                                    }
                                }
                                else{
                                    idleAnimation.update();
                                    idleAnimation.draw(gameCamera, mutPlayerPos, 0f, flip);
                                }
                            }
                            resetJumpingAnimations();
                        }
                    }
                }
            }
        }
    }



    protected void updateStepAdding(SpriteAnimation runAnimation, SoundInGameController soundController) {
        if (runAnimation.getActualSpriteNumber() == 2){
            if (!stepSoundAdded){
                stepSoundAdded = true;
                soundController.setAndPlayAudio(SoundsInGame.STEP_1);
            }
        }
        else {
            if (stepSoundAdded){
                stepSoundAdded = false;
            }
        }
    }

    /*
    protected void addSleeve(GameRound gameRound, ImageZoneSimpleData sleeveAnimation) {
        String path = HeadsUpDisplay.mainGraphicSource.getPath();
        final int beretHeight = (int)sleeveWidth;
        float directionCoef = 1f;
        float distToAppearingPlace = gameRound.getPlayer().getPersonWidth()/2f;
        if (gameRound.getPlayer().getWeaponAngle() >90 && gameRound.getPlayer().getWeaponAngle()<270) {
            distToAppearingPlace*=(-1);
            directionCoef=-1f;
            System.out.println("Orientation is to left for angle : " + gameRound.getPlayer().getWeaponAngle());
        }
        SpriteAnimation weaponAnimation = new SpriteAnimation(path, (int) sleeveAnimation.leftX, (int) sleeveAnimation.upperY, (int) sleeveAnimation.rightX, (int) sleeveAnimation.lowerY, (int) sleeveWidth, beretHeight, (byte) 1, (byte) 1, (int) 1);
        weaponAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(path));
        Vec2 position = new Vec2(gameRound.getPlayer().getAbsolutePosition().x+distToAppearingPlace, gameRound.getPlayer().getAbsolutePosition().y);
        float randomX = 55*directionCoef+Programm.engine.random(-20,20);
        float randomY = -65*directionCoef+Programm.engine.random(-25,25);
        MovableOnScreenAnimation movableOnScreenAnimation = new MovableOnScreenAnimation(weaponAnimation, position, 0f, randomX, randomY, 0, 345, directionCoef*350, 3000);
        gameRound.addNewIndependentOnScreenAnimation(movableOnScreenAnimation);
        System.out.println("Sleeve is added with dim: " + sleeveWidth + "; H: " + beretHeight);
    }*/

    private Vec2 getSleeveApearingPoint(GameRound gameRound, ImageZoneSimpleData sleeveAnimation, float sleeveDimensionCoef){




        float distToAppearingPlace = gameRound.getPlayer().getPersonWidth()/2f;
        if (gameRound.getPlayer().getWeaponAngle() >90 && gameRound.getPlayer().getWeaponAngle()<270) {
            distToAppearingPlace*=(-1);
        }
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x+distToAppearingPlace, gameRound.getPlayer().getPixelPosition().y);
        return position;
    }

    private int getSleeveCode(){
        int sleeveCode = 0;
        if (human.getActualWeapon().getWeaponType() == WeaponType.SHOTGUN || human.getActualWeapon().getWeaponType() == WeaponType.SAWED_OFF_SHOTGUN) sleeveCode = MoveableSpritesAddingController.SHOTGUN_SLEEVE;
        else if (human.getActualWeapon().getWeaponType() == WeaponType.M79) sleeveCode = MoveableSpritesAddingController.M79_SLEEVE;
        else if (human.getActualWeapon().getWeaponType() == WeaponType.SMG) sleeveCode = MoveableSpritesAddingController.SMG_MAGAZINE;
        else if (human.getActualWeapon().getWeaponType() == WeaponType.HANDGUN) sleeveCode = MoveableSpritesAddingController.HANDGUN_MAGAZINE;
        else if (human.getActualWeapon().getWeaponType() == WeaponType.REVOLVER) sleeveCode = MoveableSpritesAddingController.HANDGUN_SLEEVE;
        return sleeveCode;
    }

    protected void addSleeve(GameRound gameRound, ImageZoneSimpleData sleeveAnimation, float sleeveDimensionCoef, int count) {
        String path = HeadsUpDisplay.mainGraphicSource.getPath();
        Vec2 position = getSleeveApearingPoint(gameRound, sleeveAnimation, sleeveDimensionCoef);
        final int sleeveWidth = (int) ((sleeveAnimation.rightX-sleeveAnimation.leftX)*sleeveDimensionCoef);
        final int sleeveHeight = (int) ((sleeveAnimation.lowerY-sleeveAnimation.upperY)*sleeveDimensionCoef);
        float directionCoef = 1f;
        if (gameRound.getPlayer().getWeaponAngle() >90 && gameRound.getPlayer().getWeaponAngle()<270) {
            directionCoef=-1f;
        }
        int sleeveCode = getSleeveCode();
        float randomY = -65*directionCoef+ Program.engine.random(-25,25);
        float randomForFirstX = 0;
        float randomForSecondX = 0;
        float [] randomForAll = new float[count];
        if (count == 1){
            randomForFirstX = 55*directionCoef+ Program.engine.random(-20,20);
        }
        else if (count == 2){
            randomForFirstX = 35*directionCoef;
            randomForSecondX = -35*directionCoef;
        }
        else if (count >2){
            for (int i = 0; i < randomForAll.length; i++){
                randomForAll[i] = Program.engine.random(25,65)*directionCoef;
            }
        }
        if (count == 1) {
            System.out.println("Single sleeve was added");
            if (gameRound.existEndedMoveableSpritesForType(sleeveCode)) {
                gameRound.getEndedSpriteForType(sleeveCode).recreate(position, 0f, randomForFirstX, randomY, 0, 345, directionCoef * 350);
                System.out.println("Graphic was recreated from pool for type " + sleeveCode);
            } else {
                StaticSprite staticSprite = new StaticSprite(path, sleeveAnimation.leftX, sleeveAnimation.upperY, sleeveAnimation.rightX, sleeveAnimation.lowerY, sleeveWidth, sleeveHeight);
                IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, position, 0f, 35 * directionCoef, -85, 0, 345, directionCoef * 350, 3000, sleeveCode);
                movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(path));
                gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
            }
        }
        else if (count == 2){
            System.out.println("Double sleeve was added");
            Vec2 posForFirst = new Vec2(position.x-5, position.y+5);
            if (gameRound.existEndedMoveableSpritesForType(sleeveCode)) {

                gameRound.getEndedSpriteForType(sleeveCode).recreate(posForFirst, 0f, randomForFirstX, randomY+5, 0, 345, directionCoef * 350);
                System.out.println("Graphic was recreated from pool for type " + sleeveCode);
            } else {
                StaticSprite staticSprite = new StaticSprite(path, sleeveAnimation.leftX, sleeveAnimation.upperY, sleeveAnimation.rightX, sleeveAnimation.lowerY, sleeveWidth, sleeveHeight);
                IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, posForFirst, 0f, randomForFirstX, -85, 0, 345, directionCoef * 350, 3000, sleeveCode);
                movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(path));
                gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
                System.out.println("Graphic was created new " + sleeveCode);
            }
            Vec2 posForSecond = new Vec2(position.x+5, position.y-5);
            if (gameRound.existEndedMoveableSpritesForType(sleeveCode)) {
                gameRound.getEndedSpriteForType(sleeveCode).recreate(posForSecond, 0f, randomForSecondX, randomY-5, 0, 345, directionCoef * 350);
                System.out.println("Graphic was recreated from pool for type " + sleeveCode);
            } else {

                StaticSprite staticSprite = new StaticSprite(path, sleeveAnimation.leftX, sleeveAnimation.upperY, sleeveAnimation.rightX, sleeveAnimation.lowerY, sleeveWidth, sleeveHeight);
                IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, posForSecond, 90f, randomForSecondX, -75, 0, 345, -directionCoef * 150, 3000, sleeveCode);
                movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(path));
                gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
                System.out.println("Graphic was created new " + sleeveCode);
            }
        }
        else if (count > 2 ){
            System.out.println("Many sleeves were added " + count);
            Vec2 [] pos = new Vec2[count];
            float [] angles = new float[count] ;
            float [] angleVelocities = new float[count];
            float [] xVelocities = new float[count];
            for (int i = 0; i < pos.length; i++){
                pos[i] = new Vec2(position.x-Program.engine.random(-10,10), position.y+Program.engine.random(-10,10));
                angles[i] = Program.engine.random(360);
                angleVelocities[i] = Program.engine.random(directionCoef*250, directionCoef*450);
                xVelocities[i] = 55*directionCoef+ Program.engine.random(-20,20);
                        //
            }
            for (int i = 0; i < count; i++) {
                if (gameRound.existEndedMoveableSpritesForType(sleeveCode)) {
                    gameRound.getEndedSpriteForType(sleeveCode).recreate(pos[i], angles[i], xVelocities[i], randomY + 5, 0, 345, angleVelocities[i]);
                    System.out.println("Graphic was recreated from pool for type " + sleeveCode);
                } else {
                    StaticSprite staticSprite = new StaticSprite(path, sleeveAnimation.leftX, sleeveAnimation.upperY, sleeveAnimation.rightX, sleeveAnimation.lowerY, sleeveWidth, sleeveHeight);
                    IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, pos[i], angles[i], xVelocities[i], -85, 0, 345, angleVelocities[i], 3000, sleeveCode);
                    movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(path));
                    gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
                    System.out.println("Graphic was created new " + sleeveCode);
                }
            }

        }
    }

    /*
    protected void addSleeveAsAnimation(GameRound gameRound, ImageZoneSimpleData sleeveAnimation, float sleeveDimensionCoef) {
        String path = HeadsUpDisplay.mainGraphicSource.getPath();
        final int sleeveWidth = (int) ((sleeveAnimation.rightX-sleeveAnimation.leftX)*sleeveDimensionCoef);
        final int sleeveHeight = (int) ((sleeveAnimation.lowerY-sleeveAnimation.upperY)*sleeveDimensionCoef);
        float directionCoef = 1f;
        float distToAppearingPlace = gameRound.getPlayer().getPersonWidth()/2f;
        if (gameRound.getPlayer().getWeaponAngle() >90 && gameRound.getPlayer().getWeaponAngle()<270) {
            distToAppearingPlace*=(-1);
            directionCoef=-1f;
            System.out.println("Orientation is to left for angle : " + gameRound.getPlayer().getWeaponAngle());
        }
        SpriteAnimation weaponAnimation = new SpriteAnimation(path, (int) sleeveAnimation.leftX, (int) sleeveAnimation.upperY, (int) sleeveAnimation.rightX, (int) sleeveAnimation.lowerY, (int) sleeveWidth, sleeveHeight, (byte) 1, (byte) 1, (int) 1);
        weaponAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(path));
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x+distToAppearingPlace, gameRound.getPlayer().getPixelPosition().y);
        float randomX = 55*directionCoef+ Program.engine.random(-20,20);
        float randomY = -65*directionCoef+ Program.engine.random(-25,25);
        MovableOnScreenAnimation movableOnScreenAnimation = new MovableOnScreenAnimation(weaponAnimation, position, 0f, randomX, randomY, 0, 345, directionCoef*350, 3000);
        gameRound.addNewIndependentOnScreenAnimation(movableOnScreenAnimation);
        System.out.println("Sleeve is added with dim: " + sleeveWidth + "; H: " + sleeveHeight + " for weapon " + weaponType);
    }*/

    public abstract void startToReload();



    public void reboundJumpStarted(){
        actualJumpStartedAsSimple = true;
        actualJumpStartedAsFlip = false;
    }

    protected void updateSoundAdding(GameRound gameRound){
        if (reloadingCompletedOnThisFrame){
            reloadingCompletedOnThisFrame = false;
            if (!reloadingSoundWasAdded){
                gameRound.getSoundController().setAndPlayAudio(SoundsInGame.RELOAD_COMPLETED);
                reloadingSoundWasAdded = true;
            }
        }
    }

    public int getActualBodyAndHandsSpriteNumberByAttack(){
        return weaponAngleGraphicController.getActualSpriteForShootingAnimation();
        //return weaponAngleGraphicController.getShotBodyAnimationForActualAngle(player.getWeaponAngle()).getActualSpriteNumber();
        //weaponAngleGraphicController.getShotBodyAnimationTo45().getActualSpriteNumber();
    }

    public void setJumpAsContraFlip(boolean contraFlip){
        jumpAsContraFlip = contraFlip;
    }

    public void playerRun() {
        weaponAngleGraphicController.playerRun();
    }
}
