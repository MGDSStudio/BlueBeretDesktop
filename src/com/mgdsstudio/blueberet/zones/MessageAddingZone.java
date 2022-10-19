package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.effectsmasters.BackgroundFocusMaster;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.LifeLineValueController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.PortraitPicture;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMSController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public class MessageAddingZone extends SingleFlagZone implements IDrawable {
    final public static String CLASS_NAME = "MessageAddingZone";
    final private static String objectToDisplayName = "Message zone";
    //private Flag flag;
    private SMS message;
    private boolean withPlayerBlocking;
    private boolean messageAdded;

    //private int activatingCondition = ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE;
    private PVector cameraPosToConcentrate;
    private boolean cameraScale;

    public MessageAddingZone(Flag flag, SMS message, int activatingCondition, boolean withPlayerBlocking, PVector cameraPosToConcentrate, boolean scale) {
        this.flag = flag;
        this.cameraScale = scale;
        if (message == null || message.getText() == null) {
            System.out.println("There are no message text;");
            message = new SMS("No text", SMSController.CLOSE, PortraitPicture.SMARTPHONE_WITH_READ_MESSAGE);
        }
        this.message = message;
        this.activatingCondition = activatingCondition;
        this.withPlayerBlocking = withPlayerBlocking;
        this.cameraPosToConcentrate = cameraPosToConcentrate;
    }

    public MessageAddingZone(PVector position, float width, float height, SMS message, int activatingCondition){
        this.flag = new Flag(position,width,height, Flag.NO_MISSION);
        this.message = message;
        this.activatingCondition = activatingCondition;
    }

    public Flag getFlag() {
        return flag;
    }

    public SMS getMessage() {
        return message;
    }

    public void update(GameRound gameRound, PlayerControl playerControl, GameCameraController gameCameraController){
        if (!messageAdded) {
            if (activatingCondition == ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE) {
                if (canBeActivatedByPlayer(gameRound.getPlayer())){
                    addMessage(gameRound, gameRound.getPlayer(), playerControl, gameCameraController);
                    gameRound.addObjectIdToBeNotMoreUploaded(this);
                }
            }
            else if (activatingCondition == ACTIVATING_WHEN_ZONE_FREE_FROM_PERSONS) {
                if (canBeActivatedByFreeFromEnemiesCondition(gameRound.getPersons(), gameRound.getPlayer())){
                    addMessage(gameRound, gameRound.getPlayer(),playerControl, gameCameraController);
                    gameRound.addObjectIdToBeNotMoreUploaded(this);
                }
            }
            else if (activatingCondition == ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE_AND_ZONE_FREE_FROM_PERSONS) {

                if (canBeActivatedByFreeFromEnemiesAndPlayerInZoneCondition(gameRound.getPersons(), gameRound.getPlayer())){
                    addMessage(gameRound, gameRound.getPlayer(),playerControl, gameCameraController);
                    gameRound.addObjectIdToBeNotMoreUploaded(this);
                }
            }
            else if (activatingCondition == ACTIVATING_WHEN_PLAYER_HAS_NOT_ENOUGH_HEALTH){
                if (hasPlayerEnoughHealth(gameRound.getPlayer())) {
                    addMessage(gameRound, gameRound.getPlayer(),playerControl, gameCameraController);
                    gameRound.addObjectIdToBeNotMoreUploaded(this);
                }
            }
            else {
                System.out.println("For this condition there are no code written yet");
            }
        }
    }



    private boolean hasPlayerEnoughHealth(Person soldier) {
        if ((float)soldier.getLife()/(float)soldier.getMaxLife() <= LifeLineValueController.CRITICAL_VALUE_TO_BE_YELLOW){
            return true;
        }
        else return false;
    }

    private void addMessage(GameRound gameRound, Person player, PlayerControl playerControl, GameCameraController gameCameraController){
        if (message.getAbonent() == PortraitPicture.PlAYER_FACE || message.getAbonent() == PortraitPicture.PLAYER_SPEAKING_FACE){
            updatePlayerFaceRelativeToLifeValue(player);
            gameRound.getBackgroundFocusMaster().applyEffectToGraphic(BackgroundFocusMaster.NORMAL_ONE_STEP_RATE);
        }
        else gameRound.getBackgroundFocusMaster().applyEffectToGraphic(BackgroundFocusMaster.MINIMAL_ONE_STEP_RATE);
        gameRound.addMessage(message, playerControl);
        if (withPlayerBlocking) {
            playerControl.setControlBlocked(true);
            playerControl.clearLowerHudForOneFrame();
        }
        if (cameraPosToConcentrate != null) {
            gameCameraController.addPosToConcentrate(cameraPosToConcentrate, GameCameraController.MESSAGE);
            if (Program.debug) System.out.println("Message added with camera concentrating and scale " + cameraScale);
            gameCameraController.setConstantScale(cameraScale);
            if (withPlayerBlocking) {
                if (cameraPosToConcentrate.x > player.getPixelPosition().x) {
                    player.setWeaponAngle(0f);
                    player.setSightDirection(Soldier.TO_RIGHT);
                }
                else {
                    player.setWeaponAngle(180f);
                    player.setSightDirection(Soldier.TO_LEFT);
                }
                player.body.setLinearVelocity(new Vec2(0, gameRound.getPlayer().body.getLinearVelocity().y));
            }
        }

        messageAdded = true;
        System.out.println("New message was added");
    }

    private void updatePlayerFaceRelativeToLifeValue(Person player) {
        float max = player.getMaxLife();
        float life = player.getLife();
        float criticalMax = 0.6f;
        float criticalMin = 0.25f;
        if (life/max <= criticalMin){
            if (message.getAbonent() == PortraitPicture.PLAYER_SPEAKING_FACE){
                message.setAbonent(PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE);
            }
            else message.setAbonent(PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE);
        }
        else if (life<=criticalMax){
            if (message.getAbonent() == PortraitPicture.PLAYER_SPEAKING_FACE){
                message.setAbonent(PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE);
            }
            else message.setAbonent(PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE);
        }
    }

    private boolean canBeActivatedByFreeFromEnemiesCondition(ArrayList <Person> persons, Person player) {
        boolean somePersonIn = false;
        for (Person person : persons){
            if (!person.equals(player)){
                if (person.isAlive()){
                    if (flag.inZone(person.getPixelPosition())) {
                        somePersonIn = true;
                    }
                }
            }
        }
        return !somePersonIn;
    }

    private boolean canBeActivatedByFreeFromEnemiesAndPlayerInZoneCondition(ArrayList <Person> persons, Person player) {
        boolean somePersonIn = false;
        boolean playerInZone = true;
        for (Person person : persons){
            if (!person.equals(player)){
                if (person.isAlive()){
                    if (flag.inZone(person.getPixelPosition())) {
                        somePersonIn = true;
                    }
                }
            }
            else {
                if (!flag.inZone(player.getPixelPosition())){
                    playerInZone = false;
                }
            }
        }
        //System.out.println("Persons: " + somePersonIn + " player : " + playerInZone);
        if (somePersonIn == false && playerInZone == true) return true;
        else return false;

    }

    private boolean canBeActivatedByPlayer(Person player){
        if (flag.inZone(player.getPixelPosition())) {
            return true;
        }
        else return false;
    }

    public void draw(GameCamera gameCamera){
        flag.draw(gameCamera);
    }

    public boolean isWithPlayerBlocking() {
        return withPlayerBlocking;
    }

    public int getActivatingCondition() {
        return activatingCondition;
    }

    public PVector getCameraPosToConcentrate() {
        return cameraPosToConcentrate;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public String getObjectToDisplayName(){
        return objectToDisplayName;
    }
}
