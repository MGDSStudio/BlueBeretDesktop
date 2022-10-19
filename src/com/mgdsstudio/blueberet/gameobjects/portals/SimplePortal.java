package com.mgdsstudio.blueberet.gameobjects.portals;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class SimplePortal extends Portal implements IDrawable {
    public final static String CLASS_NAME = "SimplePortal";
    private Timer repeatingTimer;
    private boolean personOnExitZoneAfterPortal;
    private boolean ended;

    public SimplePortal(Flag enter, Flag exit, byte activatedBy, boolean transferDirection, boolean usingRepeateability) {
        this.enter = enter;
        this.exit = exit;
        this.activatedBy = activatedBy;
        this.transferDirection = transferDirection;
        this.usingRepeateability = usingRepeateability;
    }

    public SimplePortal(GameObjectDataForStoreInEditor data) {
        this.enter = data.getFirstFlag();
        this.exit = data.getSecondFlag();
        if (enter == null) System.out.println("First flag is null");
        this.activatedBy = data.getActivatedBy();
        this.transferDirection = data.getTransferDirection();
        this.usingRepeateability = data.getUsingRepeateability();
    }

    public void update(GameRound gameRound, GameCamera gameCamera) {
        if (!ended) {
            if (activatedBy == BY_PLAYER) {
                if (enter.inZone(gameRound.getPlayer().getPixelPosition())) {
                    playerTransfer(gameCamera, gameRound.getPlayer());
                }
            }
            else if (activatedBy == BY_EVERY_PERSON) {
                for (int i = 0; i < gameRound.getPersons().size(); i++) {
                    if (enter.inZone(gameRound.getPersons().get(i).getPixelPosition())) {
                        personTransfer(gameRound.getPersons().get(i));
                    }
                }
            }
            else if (activatedBy == BY_ENEMIES) {
                for (int i = 0; i < gameRound.getPersons().size(); i++) {
                    if (enter.inZone(gameRound.getPersons().get(i).getPixelPosition())) {
                        if (!gameRound.getPlayer().equals(gameRound.getPersons().get(i))) {
                            personTransfer(gameRound.getPersons().get(i));
                        }
                    }
                }
            }
        }
    }

    private void playerTransfer(GameCamera gameCamera, Person player){
        Vec2 oldPos = new Vec2(player.getPixelPosition().x, player.getPixelPosition().y);
        personTransfer(player);
        Vec2 newPos = new Vec2(player.getPixelPosition().x, player.getPixelPosition().y);
        //System.out.println("Player is on zone " + gameCamera.isInSomeCameraFixationZone());
        if (!gameCamera.isInSomeCameraFixationZone()) {
            cameraTransfer(gameCamera, oldPos, newPos);
            gameCamera.updateForCharacter(player, GameCamera.CAMERA_TO_AIM_PLACE);
        }
        System.out.println("Old pos: " + oldPos + "new: " + newPos);
        //
    }

    private void cameraTransfer(GameCamera gameCamera, Vec2 oldPos, Vec2 newPos) {
        Vec2 shifting = newPos.sub(oldPos);
        //Vec2 shifting = oldPos.sub(newPos);

        gameCamera.setNewActualPosition(new PVector(shifting.x, shifting.y));
        gameCamera.setNewPosition(shifting);
        gameCamera.setGoalPosition(shifting);
    }


    private void personTransfer(GameObject gameObject){
        PVector shifting = new PVector(exit.getPosition().x, exit.getPosition().y);
        shifting.sub(enter.getPosition());
        PVector newPos = new PVector(gameObject.getPixelPosition().x,gameObject.getPixelPosition().y).add(shifting);
        //Vec2 newPos = new Vec2(exit.getPosition().x, exit.getPosition().y);
        //PVector relativeDistToCenter = new PVector(gameObject.getAbsolutePosition().x,gameObject.getAbsolutePosition().y).sub(new PVector(enter.getPosition().x, enter.getPosition().y));
        //System.out.println("relative: " + relativeDistToCenter);

        gameObject.body.setTransform(PhysicGameWorld.controller.coordPixelsToWorld(newPos), gameObject.body.getAngle());


        /*
        PVector relativeDistToCenter = new PVector(gameObject.getAbsolutePosition().x,gameObject.getAbsolutePosition().y).sub(new PVector(enter.getPosition().x, enter.getPosition().y));
        System.out.println("relative: " + relativeDistToCenter);
        gameObject.body.setTransform(PhysicGameWorld.controller.coordPixelsToWorld((exit.getPosition()).add(relativeDistToCenter)), gameObject.body.getAngle());
        */
        //gameObject.body.setTransform(PhysicGameWorld.controller.coordPixelsToWorld(exit.getPosition()), gameObject.body.getAngle());
        personOnExitZoneAfterPortal = true;
        if (usingRepeateability == DISPOSABLE) ended = true;
    }

    public boolean canBeDelete(){
        if (ended){
            return true;
        }
        else return false;
    }

    @Override
    public void draw(GameCamera gameCamera) {
        drawFlags(gameCamera);
    }

    /*
    @Override
    public float getWidth() {
       return enter.getWidth();
    }

    @Override
    public float getHeight() {
        return enter.getHeight();
    }*/
}
