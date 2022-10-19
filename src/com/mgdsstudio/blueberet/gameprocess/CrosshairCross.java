package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamecontrollers.CrosshairAlphaController;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PVector;

class CrosshairCross{
    private CrosshairAlphaController alphaController;
    private final ImageZoneSimpleData imageZoneSimpleData;
    private final PVector actualPos, prevPos;
    private final Image graphic;
    private float drawingAngle;
    private final float sightDiameter = (int) (Soldier.getNormalBodyWidth()*1.25f) ;
    private final int minVisibleDistance = (int)(Program.DISPLAY_WIDTH*2.3f);    //pixels
    private final int step = 3;
    private int maxItterationsNumber = (minVisibleDistance /step);
    //private final int MIN_ITTERATIONS_NUMBER = maxItterationsNumber;

    /*Next three must be with constants filled or also changed in getTint() function*/
    private final static int ON_PERSON = Program.engine.color(200,15,15);
    private final static int ON_CRUSHABLE = Program.engine.color(255,213,0);
    private final static int ON_NOT_CRUSHABLE = Program.engine.color(145);

    private int tintValue = ON_NOT_CRUSHABLE;
    private boolean visible;
    private boolean withColorChanging = true;
    private final static int COLOR_UPDATING_STEP = 2;

    private final int minimalDistance = 60;
    private final float CROSHAIR_DIM_COEF = 1.35f;
    private final Soldier soldier;

    public CrosshairCross(Soldier soldier) {
        this.soldier = soldier;
        actualPos = new PVector(soldier.getPixelPosition().x, soldier.getPixelPosition().y);
        prevPos = new PVector(soldier.getPixelPosition().x, soldier.getPixelPosition().y);
        graphic = HeadsUpDisplay.mainGraphicSource;
        final int centerX = 544;
        final int centerY = 31;
        final int width = 55;
        imageZoneSimpleData = new ImageZoneSimpleData(centerX-width/2,centerY-width/2, centerX+width/2,centerY+width/2);
        alphaController = new CrosshairAlphaController();
    }

    private int getActualTint(int tintWithoutTransparence){
        if (tintWithoutTransparence == ON_PERSON){
            return Program.engine.color(200,15,15, alphaController.getAlpha());
        }
        else if (tintWithoutTransparence == ON_CRUSHABLE){
            return Program.engine.color(255,213,0, alphaController.getAlpha());
        }
        else {
            return Program.engine.color(145, alphaController.getAlpha());
        }
    }


    private void updateTestLength(float shootingAngle) {
        float maxVisibleDistance;
        if (shootingAngle <270 && shootingAngle > 90) maxVisibleDistance = -(int)(minVisibleDistance/ PApplet.cos(PApplet.radians(shootingAngle)));
        else maxVisibleDistance = (int)(minVisibleDistance/PApplet.cos(PApplet.radians(shootingAngle)));
        maxItterationsNumber = (int)(maxVisibleDistance/step);
    }

    private void updatePos(GameRound gameRound, float shootingAngle){
        PVector personPos = soldier.getPixelPosition();
        prevPos.x = actualPos.x;
        prevPos.y = actualPos.y;
        actualPos.x = personPos.x;
        actualPos.y = personPos.y;
        shootingAngle = soldier.getWeaponAngle();
        boolean objectAchieved = false;
        for (int i = 0; i < maxItterationsNumber; i++){
            if (i == 0){
                actualPos.x += minimalDistance * PApplet.cos(PApplet.radians(shootingAngle));
                actualPos.y += minimalDistance * PApplet.sin(PApplet.radians(shootingAngle));
            }
            else {
                if (!objectAchieved) {
                    actualPos.x += step * PApplet.cos(PApplet.radians(shootingAngle));
                    actualPos.y += step * PApplet.sin(PApplet.radians(shootingAngle));
                    objectAchieved = isObjectOnPlace(gameRound, actualPos);
                    if (objectAchieved) {
                        break;
                    }
                }
            }
        }
        if (objectAchieved) {
            visible = true;
        }
    }

    private boolean isObjectOnPlace(GameRound gameRound, PVector actualPos) {
        try {
            if (PhysicGameWorld.arePointInAnyBodyButNotInBullet(actualPos)) {
                if (withColorChanging) updateTint(gameRound, actualPos);
                return true;
            }
            else return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void updateTint(GameRound gameRound, PVector actualPos) {
        if ( (Program.engine.frameCount%COLOR_UPDATING_STEP==1)){
            Body attackingBody = PhysicGameWorld.getBodyAtPoint(actualPos);
            if (attackingBody != null) {
                GameObject attackedObject = PhysicGameWorld.getGameObjectByBody(gameRound, attackingBody);
                if (attackedObject != null) {
                    if (attackedObject.getLife() < GameObject.IMMORTALY_LIFE) {
                        boolean objectIsPerson = false;
                        for (Person person : gameRound.getPersons()) {
                            if (person.equals(attackedObject)) {
                                tintValue = getActualTint(ON_PERSON);
                                objectIsPerson = true;
                            }
                        }
                        if (!objectIsPerson) {
                            tintValue = getActualTint(ON_CRUSHABLE);
                        }
                    } else tintValue = getActualTint(ON_NOT_CRUSHABLE);
                } else {
                    System.out.println("The crosshair can not determine this object");
                }
            }
            else {
                System.out.println("The crosshair can not determine this body; ");
            }
        }

    }

    private int getActualSightDiameter(float alpha){
        if (alpha >= 254){
            return (int) ((int) sightDiameter*CROSHAIR_DIM_COEF);
        }
        else return (int) ((sightDiameter*CROSHAIR_DIM_COEF)*alpha/255f);
    }

    
    public void draw(GameCamera gameCamera) {        
        if (visible){
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(actualPos.x - gameCamera.getActualXPositionRelativeToCenter(), actualPos.y - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.rotate(PApplet.radians(drawingAngle));
            Program.objectsFrame.pushStyle();
            Program.objectsFrame.tint(tintValue);
            int dim = getActualSightDiameter(alphaController.getAlpha());
            Program.objectsFrame.image(graphic.getImage(), 0,0, dim, dim, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
        }
    }

    public void update(GameRound gameRound) {
        float shootingAngle = soldier.getWeaponAngle();
        if (gameRound.getPlayer().isAlive() && gameRound.getPlayer().getActualWeapon().areThereBulletsInMagazine() && gameRound.getPlayer().getStatement() != Person.IN_AIR && !gameRound.getPlayer().isPersonRunning() && !gameRound.getPlayer().isTransferingThroughPortal()) {
            updateTestLength(shootingAngle);
            updatePos(gameRound, shootingAngle);
            alphaController.update(prevPos, actualPos);
            drawingAngle = shootingAngle;
            if (!visible) visible = true;
        }
        else visible = false;
    }
}
