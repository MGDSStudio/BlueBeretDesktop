package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gamecontrollers.KickController;
import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamecontrollers.WallInFrontOfPersonDeterminingController;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableSack;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.Money;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.WeaponMagazine;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.MercenaryBehaviourController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.controllers.*;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.loading.PlayerDataLoadMaster;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class Mercenary extends Human implements IDrawable {

    private SpritesheetChangingController spritesheetChangingController;
    private ArrayList<WeaponType> deblockedWeapons;
    public final static String CLASS_NAME = "Mercenary";
    private final String objectToDisplayName = "Mercenary";
    //private static Image sprite;
    private static boolean graphicLoaded;
    //int width, height;

    private final float upperWeaponAngleDeadZone = 30;
    private final float lowerWeaponAngleDeadZone = 40;
    public final static boolean MOVEMENT_WITH_ACCELERATE = true;
    private final boolean withFeet = false;

    private boolean drawStaticSprite = false;

    private KickController kickController;
    private boolean withDebugGraphicInContactedPlaces = true;
    private PlayerBag playerBag;
    private UpperPanel upperPanel;

    private Timer timerToBlockPlayer;

    private MercenaryBehaviourController behaviourController;

    public Mercenary(PVector position, GameRound gameRound, WeaponType weaponType, int behaviour) {
        bodyData = new HumanBodyData(this.getClass());
        behaviourController = new MercenaryBehaviourController(this, behaviour);
        this.gameRound = gameRound;
        positionInPrevFrame.x = position.x;
        positionInPrevFrame.y = position.y;
        role = PLAYER;
        boundingWidth = bodyData.getNORMAL_WIDTH();
        boundingHeight = bodyData.getNORMAL_HEIGHT();
        makeBody(new Vec2(position.x, position.y), boundingWidth, boundingHeight);
        makeDeadBody();
        if (Program.debug) System.out.println("Mercenary was uploaded");


        weapons.add(new FirearmsWeapon(Weapon.SO_SHOTGUN, this));
        weapons.add(new FirearmsWeapon(Weapon.SHOTGUN, this));

        weapons.add(new FirearmsWeapon(Weapon.REVOLVER, this));
        weapons.add(new FirearmsWeapon(Weapon.HANDGUN, this));

        weapons.add(new FirearmsWeapon(Weapon.SMG, this));
        weapons.get(1).setWeaponAsActual(true);
        weapons.add(new FirearmsWeapon(Weapon.GREENADE_LAUNCHER, this));
        weapons.add(new FirearmsWeapon(Weapon.HAND_GREENADE, this));

        playerBag = new PlayerBag(gameRound.getLevelType(), this);

        body.setFixedRotation(true);
        calculateJumpVelocity();
        if (!graphicLoaded) {
            graphicLoaded = true;
        }
        actualAccelerate = 490;
        normalAccelerate = actualAccelerate;
        //maxVelocityAlongX = 400;
        createAnimationController();
        deblockWeapons(gameRound.getLevelType());
        setFilterDataForCategory(CollisionFilterCreator.CATEGORY_PLAYER);
        kickController = new KickController( this, animationController, gameRound.getHittingController());
        life = 80;
        this.maxLife = life;
        wallInFrontOfPersonDeterminingController = new WallInFrontOfPersonDeterminingController(6, 3, 5, this);
        updateOrientationByWeaponAngle();
    }

    public static float getNormalBodyWidth() {
        //HumanBodyData humanBodyData = new HumanBodyData(Soldier.class);
        return 25f / 0.7f;
    }

    public void updateOrientationByWeaponAngle() {
        if (weaponAngle > 90 && weaponAngle < 270) {
            orientation = TO_LEFT;
            sightDirection = TO_LEFT;
        }
        else {
            orientation = TO_RIGHT;
            sightDirection = TO_RIGHT;
        }
    }


    private void createAnimationController(){
        boolean bulletTimeActivated = false;
        if (getActualWeapon().getWeaponType() == WeaponType.SHOTGUN) animationController = new PlayerShotgunAnimationController(this, bulletTimeActivated);
        else if (getActualWeapon().getWeaponType() == WeaponType.M79) animationController = new PlayerGreenadeLauncherAnimationController(this, bulletTimeActivated);
        else if (getActualWeapon().getWeaponType() == WeaponType.HANDGUN) animationController = new PlayerPistoleAnimationController(this,bulletTimeActivated);
        else if (getActualWeapon().getWeaponType() == WeaponType.SMG) animationController = new PlayerSMGAnimationController(this, bulletTimeActivated);

        else if (getActualWeapon().getWeaponType() == WeaponType.REVOLVER) animationController = new PlayerRevolverAnimationController(this, bulletTimeActivated);
        else if (getActualWeapon().getWeaponType() == WeaponType.SAWED_OFF_SHOTGUN) animationController = new PlayerSawedOffShotgunAnimationController(this, bulletTimeActivated);
        else if (getActualWeapon().getWeaponType() == WeaponType.GRENADE) animationController = new PlayerGreenadeAnimationController(this, bulletTimeActivated);


        else animationController = new PlayerPistoleAnimationController(this,bulletTimeActivated);
        spritesheetChangingController = new SpritesheetChangingController(getActualWeapon().getWeaponType(), animationController);
    }

    private void calculateJumpVelocity() {
        jumpMaxHeight = 20;
        float startSpeed = PApplet.sqrt(jumpMaxHeight * PApplet.abs(2 * PhysicGameWorld.GRAVITY));
        jumpStartSpeed = startSpeed;
        System.out.println("Jump speed along y: " + jumpStartSpeed + "; it must be updated for another characters");
    }

    public void recalculateJumpVelocity(boolean bulletTime) {
        float startSpeed = 0;
        if (bulletTime){
            startSpeed = PApplet.sqrt(jumpMaxHeight * PApplet.abs(2f * PhysicGameWorld.GRAVITY*4f));
        }
        else {
            startSpeed = PApplet.sqrt(jumpMaxHeight * PApplet.abs(2f * PhysicGameWorld.GRAVITY*1f));
        }
        jumpStartSpeed = startSpeed;
        System.out.println("This function is not adusted to the real mercenary bullet time coef");
    }


    private void deblockWeapons(boolean levelType) {
        deblockedWeapons = new ArrayList<>();
        deblockedWeapons.add(WeaponType.SMG);
        //Soldier soldier, boolean global, boolean levelType

        //PlayerDataLoadMaster master = new PlayerDataLoadMaster(this, false, levelType);
        //master.loadData();

        //deblockedWeapons =  master.getDeblockedWeapons();
        //System.out.println("Player deblocked " + deblockedWeapons.size() + " weapons " );
		/*
		deblockedWeapons.add(WeaponType.HANDGUN);
		deblockedWeapons.add(WeaponType.SHOTGUN);
		deblockedWeapons.add(WeaponType.SMG);

		deblockedWeapons.add(WeaponType.M79);*/
    }

    public ArrayList<WeaponType> getDeblockedWeapons() {
        return deblockedWeapons;
    }

    @Override
    public void setWeaponAngle(float angleInDegrees) {
        if ((angleInDegrees < (90 - lowerWeaponAngleDeadZone) && angleInDegrees >= 0) || (angleInDegrees >= (90 + lowerWeaponAngleDeadZone) && (angleInDegrees <= (270 - upperWeaponAngleDeadZone)) || (angleInDegrees <= 360 && angleInDegrees >= 270 + upperWeaponAngleDeadZone)))
            weaponAngle = angleInDegrees;
        else {

            //System.out.println("Angle can not be set; " + angleInDegrees);
        }
    }

    @Override
    public void run(boolean withAccelerate, boolean direction) {
        if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, Person.RUN_MOVEMENT) && !underAttack)
            super.run(withAccelerate, direction);
    }

    public void run(boolean withAccelerate, boolean direction, GameRound gameRound) {
        if (withDebugGraphicInContactedPlaces) {
            if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, gameRound, Person.RUN_MOVEMENT)  && !underAttack)
                super.run(withAccelerate, direction);
        } else {
            if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, Person.RUN_MOVEMENT))
                super.run(withAccelerate, direction);
        }
    }

    @Override
    public void move(boolean withAccelerate, boolean direction) {
        if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, Person.GO_MOVEMENT) && !underAttack)
            super.move(withAccelerate, direction);
        //if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction)) super.move( withAccelerate, direction);
    }


    public void move(boolean withAccelerate, boolean direction, GameRound gameRound) {
        //if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction)) super.move( withAccelerate, direction);

        if (withDebugGraphicInContactedPlaces) {
            if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, gameRound, Person.GO_MOVEMENT)  && !underAttack)
                super.move(withAccelerate, direction);
        } else {
            if (!wallInFrontOfPersonDeterminingController.areThereWallInFrontOfPlayer(direction, Person.GO_MOVEMENT)  && !underAttack)
                super.move(withAccelerate, direction);
        }
    }


    private void setFilterData(byte group) {
        Filter filter = new Filter();
        filter.groupIndex = group;
        for (Fixture f = body.getFixtureList(); f != null; f = f.getNext()) {
            f.setFilterData(filter);
        }
    }





    @Override
    public void draw(GameCamera gameCamera) {
        updateSpriteChangingBeyWeaponSelecting();
        if (drawStaticSprite) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.pushStyle();
            Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
            float a = body.getAngle();
            Program.objectsFrame.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.rotate(-a);
            if (Program.debug) {
                Program.objectsFrame.rectMode(PConstants.CENTER);
                Program.objectsFrame.noFill();
                Program.objectsFrame.strokeWeight(1.8f);
            }
            drawTint();
            if (Program.debug) {
                Program.objectsFrame.rect(0, 0, bodyData.getBodyWidth(), bodyData.getBodyHeight());
                tintUpdatingBySelecting();
            }
            setTintValue();

            if (Program.debug) {
                Program.objectsFrame.ellipse(0, bodyData.getDeltaY(), bodyData.getHeadRadius() * 2, bodyData.getHeadRadius() * 2);
                resetTint();
            }
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
        }
        else {
            animationController.draw(getWeaponAngle(), gameCamera, underAttack, jumpAfterAttack, gameRound.getSoundController());
        }
    }

    private void updateSpriteChangingBeyWeaponSelecting() {
        if (spritesheetChangingController.isStarted()){
            if (spritesheetChangingController.isEnded()){
                animationController = spritesheetChangingController.getPlayerAnimationController();
                spritesheetChangingController.endLoading();
            }
        }
    }

    @Override
    public void loadAnimationData(MainGraphicController mainGraphicController) {
		/*animationController.loadAnimationData(mainGraphicController);
		ColorChanger colorChanger = new ColorChanger();
		int sourceColor = Program.engine.color(54,71,121);
		int newColor = Program.engine.color(254,1,1);
		Image image;
		image = colorChanger.changeColor(sourceColor, newColor, animationController.getTilesetForActualGraphic().getPicture());
		animationController.setImageForActualTileset(image);
		sourceColor = Program.engine.color(59,123,206);
		newColor = Program.engine.color(170,1,1);
		colorChanger.changeColor(sourceColor, newColor, animationController.getTilesetForActualGraphic().getPicture());
		System.out.println("Color was changed");
		*/


        animationController.loadAnimationData(mainGraphicController);
		/*ColorChanger colorChanger = new ColorChanger();
		int sourceColor = Program.engine.color(54,71,121);
		int newColor = Program.engine.color(254,1,1);
		colorChanger.changeColor(sourceColor, newColor, animationController.getTilesetForActualGraphic().getPicture());
		sourceColor = Program.engine.color(59,123,206);
		newColor = Program.engine.color(170,1,1);
		colorChanger.changeColor(sourceColor, newColor, animationController.getTilesetForActualGraphic().getPicture());
		System.out.println("Color was changed");*/


    }



    private void setTintValue() {
        if (isSelected()) {
            Program.objectsFrame.tint(Program.engine.color(255, SingleGameElement.actualSelectionTintValue));
        }
        if (selectionWasCleared) {
            Program.objectsFrame.noTint();
            System.out.println("Selection tint is reset");
            selectionWasCleared = false;
        }
    }



    public void update(GameRound gameRound) {
        super.update();
        if (underAttackTimer != null) {
            if (underAttackTimer.isTime()) {
                underAttack = false;
                stopBlinking();
                if (timerToBlockPlayer == null) setControlBlocked(false);
                else if (timerToBlockPlayer.isTime()){
                    setControlBlocked(false);
                }
            }
        }
        else {
            if (timerToBlockPlayer == null) setControlBlocked(false);
            else if (timerToBlockPlayer.isTime()){
                setControlBlocked(false);
            }
        }
        updateStatement();
        kickController.update(gameRound);
        getActualWeapon().update(gameRound, this);
        animationController.update(gameRound);

        if (blockedThoughtExternalForces){
            blockHumanMovementForOneFrame(false);
        }
        if (isShootingAtThisFrame()) updatePixelPos();
        behaviourController.update(gameRound);
    }




    boolean isSpeedMax() {
        if (body.getLinearVelocity().x >= maxVelocityAlongX) {
            return true;
        } else return false;
    }


    @Override
    public String getStringData() {
        String data = new String();
        data = data + CLASS_NAME;
        data += " 1";
        data += LoadingMaster.MAIN_DATA_START_CHAR;
        data += (int) PhysicGameWorld.controller.getBodyPixelCoord(body).x;
        data += ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        data += (int) PhysicGameWorld.controller.getBodyPixelCoord(body).y;
        return data;
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    @Override
    public Vec2 getKickAttackRightUpperCorner() {
        float distanceToFootX = 0.7f * getPersonWidth();
        float distanceToFootY = -distanceToFootX * 0.1f;
        System.out.println("WxH" + distanceToFootX + "x" + distanceToFootY);
        Vec2 attackPlace = new Vec2(getPixelPosition().x + distanceToFootX, getPixelPosition().y + distanceToFootY);
        if (orientation == TO_LEFT) {
            attackPlace.x = getPixelPosition().x - distanceToFootX;
        }
        return attackPlace;
    }

    @Override
    public Rectangular getKickAttackZone() {
        float footLength = 0.7f * getPersonWidth();
        float zoneWidth = getPersonWidth() * 2.0f;
        float zoneHeight = bodyData.getBodyHeight() / 2f;

        float deltaDistance = bodyData.getBodyHeight() / 20f;
        Rectangular rectangular = null;
        if (orientation == TO_RIGHT) {
            rectangular = new Rectangular(getPixelPosition().x + zoneWidth / 2, getPixelPosition().y - deltaDistance + (zoneHeight / 2), zoneWidth, zoneHeight);
        } else {
            rectangular = new Rectangular(getPixelPosition().x - zoneWidth / 2, getPixelPosition().y - deltaDistance + (zoneHeight / 2), zoneWidth, zoneHeight);
        }
        if (Program.debug) System.out.println("Player pos: " + getPixelPosition() + "; Zone left upper: " + (int) rectangular.getLeftUpperX() + "x" + (int) rectangular.getLeftUpperY() + "; right lower: " + (int) rectangular.getRightLowerX() + "x" + (int) rectangular.getRightLowerY() + "; Zone height: " + (int) zoneHeight);

        return rectangular;

    }

    @Override
    public boolean isKicking() {
        return kicking;
    }

	/*
	public boolean isKickMoment() {
		return animationController.isKickMoment();
	}*/



    @Override
    public boolean canBeKickMade() {
        return kickController.canBeKickMade();
    }

    @Override
    public void makeKick() {
        kickController.makeKick();

    }

    @Override
    public boolean attackByDirectContact(Person nearestPerson) {
        return false;
    }

    @Override
    public int getAttackValue() {
        System.out.println("Player can not attack by direct contact");
        return 0;
    }








    @Override
    public PersonAnimationController getPersonAnimationController() {
        return animationController;
    }

    @Override
    protected void updateAngle() {
        System.out.println("Function update angle was not overriden by soldier");
    }

    public void attacked(int damageValue) {
        /*if (!underAttack) {

        }*/
        super.attacked(damageValue);
        behaviourController.attacked();
        if (getActualWeapon().isReloading()) setReloadCompleted(true);
        System.out.println("Mercenary is attacked");
    }

    public boolean isControlBlocked() {
        return controlBlocked;
    }

    public void setControlBlocked(boolean controlBlocked) {
        this.controlBlocked = controlBlocked;
        if (controlBlocked) jumpAfterAttack = true;
        else jumpAfterAttack = false;
        if (Program.debug && Program.engine.frameCount %300 == 0) System.out.println("The last conditions must be tested!");
    }

    public void setControlBlocked(int time) {
        this.controlBlocked = true;
        jumpAfterAttack = false;
        if (timerToBlockPlayer == null){
            timerToBlockPlayer = new Timer(time);
        }
        else timerToBlockPlayer.setNewTimer(time);
        //if (Program.debug && Program.engine.frameCount %300 == 0) System.out.println("The last conditions must be tested!");
    }



    public void stopBlinking() {
        animationController.stopBlinking();
    }

    public boolean isWeaponChangingGraphicInProcess() {
        return animationController.isWaiting();
    }

    public void setGraphicOnWeaponChanging(boolean changeGraphicOn) {
        animationController.setWaiting(changeGraphicOn);
    }

    @Override
    protected boolean areThereObjectsUnderPerson() {
        if (wallInFrontOfPersonDeterminingController.areThereWallUnderPlayer()) {
            //if (Program.debug) System.out.println("There are an object under the player ");
        }
        return wallInFrontOfPersonDeterminingController.areThereWallUnderPlayer();
    }

    public void controlWasBlocked(boolean blocked) {
        animationController.setControlBlocked(blocked);
    }

    public void startToReload() {
        getActualWeapon().reload();
        animationController.startToReload();
    }








    public void setWeaponAsActual(WeaponType weaponType) {
        for (FirearmsWeapon weapon : weapons){
            if (weapon.getWeaponType() == weaponType){
                weapon.setWeaponAsActual(true);
            }
            else weapon.setWeaponAsActual(false);
        }
        System.out.println("Try to load spritesheet for weapon: " + weaponType + " controller is null: " + (spritesheetChangingController==null));
        spritesheetChangingController.loadNewSpritesheet(weaponType);
    }



    @Override
    public void addNewCollectableObject(AbstractCollectable collectableObject){
        System.out.println("Object has type: " + collectableObject.getType());
        if (collectableObject.getType() == AbstractCollectable.BIG_BAG || collectableObject.getType() == AbstractCollectable.SMALL_BAG){
            CollectableSack collectableSack = (CollectableSack) collectableObject;
            if (collectableSack.areThereObjects()){
                while (collectableSack.areThereObjects()){
                    AbstractCollectable abstractCollectable = collectableSack.getObjectFromSack(-1);
                    System.out.println("Try to add " + abstractCollectable.getType() + " to the player");
                    addNewCollectableObject(abstractCollectable);
                }
            }
            else System.out.println("This bag has no objects inside: " );
            collectableSack.body.setActive(false);
            collectableSack.markAsDeletedApproved();
            PhysicGameWorld.controller.destroyBody(collectableSack.body);
        }
        else if (WeaponMagazine.isTypeAmmo(collectableObject.getType())){
            WeaponMagazine weaponMagazine = (WeaponMagazine) collectableObject;
            WeaponType weaponType = weaponMagazine.getWeaponType();
            playerBag.addAmmo(weaponType, collectableObject.getValueToBeAddedByGain());
        }
        else if (collectableObject.getType() == AbstractCollectable.SMALL_MEDICAL_KIT || collectableObject.getType() == AbstractCollectable.MEDIUM_MEDICAL_KIT || collectableObject.getType() == AbstractCollectable.LARGE_MEDICAL_KIT){
            playerBag.addMedicalKit(collectableObject);
            upperPanel.updateInventoryObjectsNumber(collectableObject.getType(), 1);
        }
        else if (collectableObject.getType() == AbstractCollectable.SYRINGE){
            playerBag.addSelectableObject(collectableObject);
            upperPanel.updateInventoryObjectsNumber(collectableObject.getType(), 1);
        }

        else if (collectableObject instanceof Money){
            playerBag.addMoney(collectableObject.getValueToBeAddedByGain());
        }
        else {

            System.out.println("This object type is not known");
            super.addNewCollectableObject(collectableObject);
        }
    }

    public void setWeaponAsDeblocked(WeaponType weaponType){
        //System.out.println("Weapon was deblocked");
        boolean alreadyDeblocked = false;
        for (WeaponType weapon : deblockedWeapons){
            if (weapon == weaponType){
                alreadyDeblocked = true;
                System.out.println("This weapon already exists");
            }
        }
        if (!alreadyDeblocked){
            deblockedWeapons.add(weaponType);
            System.out.println("This weapon was added to the existing weapons array");
        }
    }

    public void setUpperPanel(UpperPanel upperPanel) {
        this.upperPanel = upperPanel;
    }

    @Override
    public boolean isPersonRunning(){
        if (actualByUserPressedMovement == PlayerControl.USER_PRESSES_RUN_LEFT || actualByUserPressedMovement == PlayerControl.USER_PRESSES_RUN_RIGHT) return true;
        else return false;
    }

    public ArrayList <FirearmsWeapon> getWeapons(){
        return weapons;
    }



    public void makeJump(boolean contraFlip) {
        super.makeJump();
        if (statement == IN_AIR){
            animationController.setJumpAsContraFlip(contraFlip);
        }
    }

    @Override
    public void setActualByUserPressedMovement(int actualByUserPressedMovement) {
        if (underAttack){
            this.actualByUserPressedMovement = PlayerControl.USER_DOESNOT_TOUCH_STICK;
        }
        else {
            this.actualByUserPressedMovement = actualByUserPressedMovement;
        }
        if (actualByUserPressedMovement==PlayerControl.USER_PRESSES_RUN_RIGHT || actualByUserPressedMovement== PlayerControl.USER_PRESSES_RUN_LEFT){
            animationController.playerRun();
        }
    }




    public ArrayList <WeaponType>  getOnUpperPanelWeapons() {
        return playerBag.getWeaponsOnUpperPanel();
    }



    public boolean isPlayerFreeFromNet() {
        return true;
    }
}