package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PVector;

import java.util.ArrayList;

public class TriggerTeleporter extends AbstractTrigger implements IDrawable {
    private int newPosX, newPosY;
    private boolean mustBeTransferedWhenZoneFree;
    private boolean withForceTeleportion;

    private Flag teleportEndPlace;
    private final Vec2 mutTestPos = new Vec2(0,0);
    private boolean objectFounded;


    private int testX, testY;

    public TriggerTeleporter(GameRound gameRound, Flag flag, int objectX, int objectY, int action, int activatingCondition, int objectType, int delay, boolean once, int transfrerPosX, int transferPosY, boolean withForceTeleportion ) {
        super(gameRound, flag, objectX, objectY, TELEPORT_OBJECT, activatingCondition, OBJECT_WITH_BODY, delay, true);

        this.newPosX = transfrerPosX;
        this.newPosY = transferPosY;
        this.withForceTeleportion =  withForceTeleportion;
        if (!withForceTeleportion){
            if (gameObjectToBeActivated!= null){
                int zoneWidth = (int) (gameObjectToBeActivated.getWidth()*1.2f);
                int zoneHeight = (int) (gameObjectToBeActivated.getHeight()*1.2f);
                teleportEndPlace = new Flag(new PVector(newPosX, newPosY), zoneWidth, zoneHeight, Flag.NO_MISSION);
            }
        }
        this.testX = objectX;
        this.testY = objectY;
        canBeDeleted = false;
    }

    @Override
    public void update(GameRound gameRound){
        if (!objectFounded){
            gameObjectToBeActivated = gameRound.getGameObjectByCoordinate(testX, testY);
            if (gameObjectToBeActivated != null) {
                System.out.println("Game object is founded: " + gameObjectToBeActivated.getClass());
                objectFounded = true;
            }

        }
        super.update(gameRound);
    }
    protected void makeAction(GameRound gameRound) {
        teleportObject(gameRound);
        if (mustBeTransferedWhenZoneFree){
            teleportObject(gameRound);
        }
    }

    private void teleportObject(GameRound gameRound) {
        System.out.println("Try to teleport to " + newPosX + "x" + newPosY);
        if (gameObjectToBeActivated != null){
            if (gameObjectToBeActivated.isAlive()) {
                if (withForceTeleportion) teleport();
                else {
                    if (isTeleportPlaceFree()) {
                        teleport();
                    }
                    else {
                        canBeDeleted = false;
                        if (Program.debug) System.out.println("Zone is not free");
                    }
                }
            }
            else if (Program.debug){
                System.out.println("Object is already killed");
            }
        }
    }

    private boolean isTeleportPlaceFree() {
        if (teleportEndPlace!=null){
            boolean nothing = true;
            ArrayList < Body> bodies = PhysicGameWorld.getBodies();
            for (Body body : bodies){
                if (teleportEndPlace.inZone(PhysicGameWorld.controller.getBodyPixelCoord(body))){
                    return nothing;
                }
            }
            return nothing;
        }
        return false;
    }

    private void teleport() {
        gameObjectToBeActivated.body.setActive(true);
        float angle = gameObjectToBeActivated.body.getAngle();
        Vec2 newPos = PhysicGameWorld.controller.coordPixelsToWorld(newPosX, newPosY);
        gameObjectToBeActivated.body.setTransform(newPos, angle);
        canBeDeleted = true;
        System.out.println("Object " + gameObjectToBeActivated.getClass() + " was transfered to " + newPosX + ',' + newPosY);
    }
}
