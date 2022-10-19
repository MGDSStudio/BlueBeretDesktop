package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class WeaponChangingController {
    private ArrayList<WeaponType> playersWeaponTypes;
    private ArrayList<OnScreenWeapon> onScreenWeapons;
    private BackgroundShadowerForWeaponChanging backgroundShadowerForWeaponChanging;
    private int onFlickHeight = (int)( Program.engine.width*0.85f);
    private final float criticalFlickVelocityForNextWeapon = Program.engine.height/15f;
    private final float transverseToLongitudinalCoefficient = 2f;


    private final float CRITICAL_Y_VELOCITY_TO_LAUNCH_WEAPON_CHANGING = 7f;
    private final float MAXIMAL_VELOCITY_TO_BRAKING_ON_CENTER = 30f;
    private final int criticalDistanceToStopWeaponsMovement = 4;

    public static int yStep = (int)(Program.engine.height/2.5f);
    private WeaponType actualWeaponType;
    private Timer timerToHideWeaponsAfterMovement;
    private final int TIME_TO_HIDE_WEAPONS = 1200;

    private final static byte NOTHING = 0;
    private final static byte MOVEMENT = 1;
    private final static byte BRAKING_ON_CENTER = 2;
    private final static byte BRAKED_AND_HIDDING = 3;
    private byte changingStatement = NOTHING;
    private boolean onlyOneWeapon = false;

    private OnScreenWeapon weaponInCenter;
    private boolean firstLaunch = true;
    public final static boolean WITH_FLICK_CHANGING = false;


    WeaponChangingController(ArrayList<WeaponType> playersWeapons, Weapon actualWeapon){
        if (playersWeapons != null){
            if (playersWeapons.size() > 0){
                this.playersWeaponTypes = playersWeapons;
                createOnScreenWeapons(actualWeapon);
            }
            else onlyOneWeapon = true;
        }
        else onlyOneWeapon = true;
        actualWeaponType = actualWeapon.getWeaponType();
        backgroundShadowerForWeaponChanging = new BackgroundShadowerForWeaponChanging(500);
    }

    private void createOnScreenWeapons(Weapon actualWeapon){
        onScreenWeapons = new ArrayList<>();
        int startPos = Program.engine.height/2;
        int actualWeaponNumber = 0;
        for (int i = 0; i < playersWeaponTypes.size(); i++) {
            if (playersWeaponTypes.get(i) == actualWeapon.getWeaponType()){
                actualWeaponNumber = i;
                System.out.println("Weapon was " + actualWeapon.getWeaponType() + " has number: " + i);
            }
        }
        int startValue = 0;
        for (int i = actualWeaponNumber; i < (actualWeaponNumber+playersWeaponTypes.size()); i++) {
            int realNumber = i;
            if (i>=playersWeaponTypes.size()) realNumber-= playersWeaponTypes.size();
            onScreenWeapons.add(new OnScreenWeapon(playersWeaponTypes.get(realNumber), startPos+realNumber*yStep, (byte) playersWeaponTypes.size()));
            startValue++;
        }
        for (int i = 0; i < playersWeaponTypes.size(); i++) System.out.println("Pos for: " + i + " is " + onScreenWeapons.get(i).getPosition());
        for (int i = 0; i < playersWeaponTypes.size(); i++) {
            onScreenWeapons.add(new OnScreenWeapon(playersWeaponTypes.get(i), startPos+i*yStep, (byte) playersWeaponTypes.size()));
        }
        int shifting = 0;
        for (int i = 0; i < onScreenWeapons.size(); i++){
            if (playersWeaponTypes.get(i) == actualWeapon.getWeaponType()){
                System.out.println("Type: " + actualWeapon.getWeaponType() + "; Pos: " + onScreenWeapons.get(i).getPosition());
                int value = i+1;
                if (value > onScreenWeapons.size()) value-=onScreenWeapons.size();
                shifting = (Program.engine.height/2)-onScreenWeapons.get(i-1).getPosition();
                break;
            }
        }
        System.out.println("Shifting: " + shifting);
        for (int i = 0; i < onScreenWeapons.size(); i++){
            System.out.print("Position was " + onScreenWeapons.get(i).getPosition());
            onScreenWeapons.get(i).addPosition(shifting);
            System.out.println(" is " + onScreenWeapons.get(i).getPosition());
        }
        try {
            for (int i = 0; i < onScreenWeapons.size(); i++){
                if (onScreenWeapons.get(i).getType() == actualWeapon.getWeaponType()){
                    System.out.println("New pos for actual weapon is on " + onScreenWeapons.get(i).getPosition());
                }
            }
        }
        catch ( Exception e){
            System.out.println("Can not get positions data");
            e.printStackTrace();
        }
    }

    void  update(){
        if (changingStatement == NOTHING){
            updateChangingStarting();

        }
        else if (changingStatement == MOVEMENT){
            updateChangingStarting();
            updateMovementOnCenter();
        }
        else if (changingStatement == BRAKING_ON_CENTER){
            updateBrakedStatement();
        }
        else if (changingStatement == BRAKED_AND_HIDDING){
            updateHidingTint();
        }
        if (changingStatement != NOTHING) {
            for (OnScreenWeapon weapon : onScreenWeapons) weapon.update();

        }
        backgroundShadowerForWeaponChanging.update();
        //updateStatement();
    }

    public boolean isWeaponChangingInProcess(){
        if (changingStatement == MOVEMENT || changingStatement == BRAKING_ON_CENTER){
            return true;
        }
        else return false;
    }

    private void updateBrakedStatement() {
        weaponInCenter = getNearestToCenterWeapon();
        System.out.println("Center weapon: " + weaponInCenter.getType());
        brakeWeaponsThroughDirectPosChanging(weaponInCenter, weaponInCenter.getNearestDistanceToScreenCenter());
    }

    private void updateMovementOnCenter(){
        if (Program.engine.mousePressed) {
            if (!isFingerOnCenterZone()) {
                if (!OnScreenWeapon.isStoppedThroughExternalBraking()) updateBrakingOnScreenCenterThroughDirectRepositions();
                else changingStatement = BRAKED_AND_HIDDING;
            }
        }
        else {
            if (!OnScreenWeapon.isStoppedThroughExternalBraking()) updateBrakingOnScreenCenterThroughDirectRepositions();
            else changingStatement = BRAKED_AND_HIDDING;
        }
    }

    private void updateChangingStarting() {
        if (Program.engine.mousePressed) {
            if (isFingerOnCenterZone()) {
                updateFlippingStart();
            }

        }
    }

    private void updateHidingTint() {
        if (timerToHideWeaponsAfterMovement == null) timerToHideWeaponsAfterMovement = new Timer(TIME_TO_HIDE_WEAPONS);
        if (timerToHideWeaponsAfterMovement != null) {
            int tint = 255*(int)timerToHideWeaponsAfterMovement.getRestTime()/TIME_TO_HIDE_WEAPONS;
            if (tint <= 1) {
                if (changingStatement != NOTHING) changingStatement = NOTHING;
                tint = 0;
                //OnScreenWeapon.setHiddingTint(255);
            }
            OnScreenWeapon.setHiddingTint(tint);

        }
        else {
            //System.out.println("");
        }
    }


    private void updateBrakingOnScreenCenterThroughDirectRepositions() {
        //if (onScreenWeapons.get(0).isMoving()){
        OnScreenWeapon weapon = getNearestToCenterWeapon();
        if (PApplet.abs(weapon.getVelocity()) < MAXIMAL_VELOCITY_TO_BRAKING_ON_CENTER){
            changingStatement = BRAKING_ON_CENTER;
        }
        //}
    }

    private void brakeWeaponsThroughDirectPosChanging(OnScreenWeapon onScreenWeapon, int nearestDistanceToScreenCenter) {
        float additionalPos = nearestDistanceToScreenCenter/2;
        System.out.println("nearestDistanceToScreenCenter : " + nearestDistanceToScreenCenter + "; Pos: " + onScreenWeapon.getPosition());
        boolean mustBeBlocked = false;
        if ((nearestDistanceToScreenCenter) < criticalDistanceToStopWeaponsMovement ) {
            mustBeBlocked = true;
        }
        else {
            if (changingStatement != BRAKING_ON_CENTER) changingStatement = BRAKING_ON_CENTER;
        }
        for (OnScreenWeapon weapon : onScreenWeapons){
            weapon.addPosition(additionalPos);
            weapon.setVelocity(0);
        }
        if (mustBeBlocked) {
            OnScreenWeapon.setStoppedThroughExternalBraking(true);
        }
        actualWeaponType = onScreenWeapon.getType();
        if (mustBeBlocked && changingStatement != BRAKED_AND_HIDDING) {
            if (timerToHideWeaponsAfterMovement == null) timerToHideWeaponsAfterMovement = new Timer(TIME_TO_HIDE_WEAPONS);
            else timerToHideWeaponsAfterMovement.setNewTimer(TIME_TO_HIDE_WEAPONS);
            OnScreenWeapon.setHiddingTint(255);
            System.out.println("Started to change tint");
            changingStatement = BRAKED_AND_HIDDING;
            backgroundShadowerForWeaponChanging.switchOff();
        }
    }

    private OnScreenWeapon getNearestToCenterWeapon() {
        int distanceToCenter = Program.engine.height;
        OnScreenWeapon weapon = null;
        for (int i = 0; i < onScreenWeapons.size(); i++){
            int distance = onScreenWeapons.get(i).getNearestDistanceToScreenCenter();
            if (PApplet.abs(distance)<PApplet.abs(distanceToCenter)) {
                distanceToCenter = distance;
                weapon = onScreenWeapons.get(i);
            }
        }
        return weapon;
    }

    private void updateFlippingStart() {
        if (isVerticalFlickLongerAsHorizontal()) {
            float yVelocity = getVelocityY();
            if (PApplet.abs(yVelocity) > CRITICAL_Y_VELOCITY_TO_LAUNCH_WEAPON_CHANGING) {
                if (firstLaunch) firstLaunch = false;
                if (OnScreenWeapon.isStoppedThroughExternalBraking()) OnScreenWeapon.setStoppedThroughExternalBraking(false);
                for (OnScreenWeapon weapon : onScreenWeapons) {
                    weapon.addAccelerate((int) (yVelocity / 1.5f));
                }
                changingStatement = MOVEMENT;
                System.out.println("Added accelerate: " + yVelocity);
                backgroundShadowerForWeaponChanging.switchOn(Program.engine.millis());
                if (timerToHideWeaponsAfterMovement != null) {
                    timerToHideWeaponsAfterMovement.stop();
                }
            }
        }
    }

    public WeaponType getActualWeaponType(){
        return actualWeaponType;
    }

    private float getVelocityY() {
        return Program.engine.mouseY- Program.engine.pmouseY;
    }

    private boolean isVerticalFlickLongerAsHorizontal() {
        float absoluteDeltaX = PApplet.abs(Program.engine.mouseX- Program.engine.pmouseX);
        float absoluteDeltaY = PApplet.abs(Program.engine.mouseY- Program.engine.pmouseY);
        System.out.println("X: " + absoluteDeltaX + " ; Y: " + absoluteDeltaY);
        if (absoluteDeltaY > absoluteDeltaX){
            if ((absoluteDeltaY/absoluteDeltaX) >= transverseToLongitudinalCoefficient){
                return true;
            }
            else return false;
        }
        else return false;
    }

    private boolean isFingerOnCenterZone(){
        if (GameMechanics.isPointInRect(Program.engine.mouseX, Program.engine.mouseY, 0,(Program.engine.height/8), Program.engine.width, onFlickHeight)){
            return true;
        }
        else return false;
    }

    void draw(PGraphics graphic){
        graphic.imageMode(PApplet.CENTER);
        backgroundShadowerForWeaponChanging.draw(graphic);
        if (changingStatement != NOTHING) {
            for (OnScreenWeapon onScreenWeapon : onScreenWeapons) {
                if ((changingStatement == BRAKING_ON_CENTER) && onScreenWeapon.equals(weaponInCenter)) {
                    onScreenWeapon.draw(graphic, OnScreenWeapon.getHidingTint());
                } else {
                    onScreenWeapon.draw(graphic, -2);
                }

            }
        }

    }


}

