package com.mgdsstudio.blueberet.gameobjects.portals;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PVector;

public abstract class PortalWithVisibleMoving extends Portal implements IDrawable {
    public final static String CLASS_NAME = "Portal";
    protected Timer transferTimer, transportationTimer, timeToNextTransferTimer;
    protected final int TIME_FOR_TRANSFER = 1000;
    protected final int TIME_FOR_TRANSPORTATION = 1500;
    protected final int TIME_FOR_NEXT_TRANSPORTATION = 4000;
    public final static float NORMAL_TRANSFERING_SPEED = 350/20.5f;	// hidding of person in the portal in pixels/sec
    public final static byte SWITCHED_OFF = -1;
    public final static byte NOT_IN_ZONE = 0;
    public final static byte IN_PORTAL_ENTER_ZONE = 1;
    public final static byte IN_TRANSFER_ZONE = 2;
    public final static byte IN_PORTAL_EXIT_ZONE = 3;
    public final static byte PAUSE_AFTER_LAST_TRANSFER = 4;
    public final static byte TRANSPORTATION_ENDED = 5;
    public final static byte WAITING_FOR_NEXT_TRANSPORTATION = 6;
    protected byte stage = NOT_IN_ZONE;
    private boolean ended;

    public byte isPersonInPortal(GameObject person) {
        if (ended) return NOT_IN_ZONE;
        else {
            if (stage == NOT_IN_ZONE) {
                if (enter.inZone(person.getPixelPosition())) {
                    stage = IN_PORTAL_ENTER_ZONE;
                    if (transferTimer == null) {
                        transferTimer = new Timer(TIME_FOR_TRANSFER);
                    } else transferTimer.setNewTimer(TIME_FOR_TRANSFER);
                }
            } else if (stage == IN_PORTAL_ENTER_ZONE) {
                if (transferTimer != null && transferTimer.isTime()) {
                    transferTimer = null;
                    stage = IN_TRANSFER_ZONE;
                    if (transportationTimer == null) {
                        transportationTimer = new Timer(TIME_FOR_TRANSPORTATION);
                    } else transportationTimer.setNewTimer(TIME_FOR_TRANSPORTATION);
                }
            } else if (stage == IN_TRANSFER_ZONE) {
                if (transportationTimer != null && transportationTimer.isTime()) {
                    transportationTimer = null;
                    stage = IN_PORTAL_EXIT_ZONE;
                    if (transferTimer == null) {
                        transferTimer = new Timer(TIME_FOR_TRANSFER);
                    } else transferTimer.setNewTimer(TIME_FOR_TRANSFER);
                }
            } else if (stage == IN_PORTAL_EXIT_ZONE) {
                if (transferTimer.isTime()) {
                    transferTimer = null;
                    stage = TRANSPORTATION_ENDED;
                }
            } else if (stage == TRANSPORTATION_ENDED) {
                if (usingRepeateability == DISPOSABLE) {
                    stage = SWITCHED_OFF;
                } else {
                    stage = NOT_IN_ZONE;
                    if (enter.inZone(person.getPixelPosition())) {
                        stage = IN_PORTAL_ENTER_ZONE;
                        if (transferTimer == null) {
                            transferTimer = new Timer(TIME_FOR_TRANSFER);
                        } else transferTimer.setNewTimer(TIME_FOR_TRANSFER);
                    }
                }
                if (usingRepeateability == DISPOSABLE) ended = true;
            }
            return stage;
        }
    }

    public PVector getEnterCenterPos() {
        return enter.getPosition();
    }

    public PVector getExitCenterPos() {
        return exit.getPosition();
    }

    @Override
    public void draw(GameCamera gameCamera) {
        drawFlags(gameCamera);
    }

    public byte getActivatedBy() {
        return activatedBy;
    }

    public byte getDirection(boolean whichFlag) {
        if (whichFlag == ENTER) return enter.getDirection();
        else return exit.getDirection();
    }

    public boolean getUsingRepeateability() {
        return usingRepeateability;
    }

}
