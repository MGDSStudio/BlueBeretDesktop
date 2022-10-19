package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PGraphics;

public class OnScreenWeapon {
    private int yPosition;
    private int[] drawPositions;
    private float velocity;
    private WeaponType type;
    private final static ImageZoneSimpleData pointsForPistole = HUD_GraphicData.getImageZoneForWeaponType(WeaponType.HANDGUN);
    private final static ImageZoneSimpleData pointsForGreenadeLauncher = HUD_GraphicData.getImageZoneForWeaponType(WeaponType.M79);
    private final static ImageZoneSimpleData pointsForAssaultRiffle = HUD_GraphicData.getImageZoneForWeaponType(WeaponType.SMG);
    private final static ImageZoneSimpleData pointsForShotgun = HUD_GraphicData.getImageZoneForWeaponType(WeaponType.SHOTGUN);

    static float  brakingAccelerate = 4f/(1000f/ Program.NORMAL_FPS);
    private int criticalDistanceToStop = 3;
    private int leftUpperX = 0;
    private int leftUpperY = 0;
    private int rightLowerX = 512;
    private int rightLowerY = 256;
    private final static float weaponWidth = (Program.engine.width/2.2f);
    private float weaponHeight;
    private static byte weaponsNumber;
    private static int cannotBeDrawnValue = -9999;
    private final static boolean TO_UP = true;
    private final static boolean TO_DOWN = false;
    
    private final static int MAX_ALPHA_TINT = 255;    //In center
    private final static int MIN_ALPHA_TINT = -100;    // on side
    private int nearestDistanceToScreenCenter;
    //private boolean brakingOnCenter;
    //private static boolean movementStopped;
    private boolean inCenterAndBlending;

    private static int hidingTint;

    private static boolean stoppedThroughExternalBraking;

    public OnScreenWeapon(WeaponType weaponType, int yPosition, byte number){
        this.yPosition = yPosition;
        drawPositions = new int[3];
        for (int i = 0; i < drawPositions.length; i++) drawPositions[i] = yPosition;
        this.type = weaponType;
        weaponsNumber = number;
        fillGraphicCornersData();
    }

    public static void setHiddingTint(int tint) {
        hidingTint = tint;
    }

    public static int getHidingTint() {
        return hidingTint;
    }

    public static boolean isCompleteHidden(){
        if (hidingTint<1) return true;
        else return false;
    }

    private void fillGraphicCornersData(){
        ImageZoneSimpleData data;
        if (type == WeaponType.HANDGUN) data = pointsForPistole;
        else if (type == WeaponType.M79) data = pointsForGreenadeLauncher;
        else if (type == WeaponType.SMG) data = pointsForAssaultRiffle;
        else if (type == WeaponType.SHOTGUN) data = pointsForShotgun;
        else data = null;
        leftUpperX = data.leftX;
        leftUpperY = data.upperY;
        rightLowerX = data.rightX;
        rightLowerY = data.lowerY;
        weaponHeight = weaponWidth*(1.00f*(rightLowerY-leftUpperY)/(rightLowerX-leftUpperX));
        //System.out.println("weapon height: " + weaponHeight);
    }

    public void update(){
        if (!stoppedThroughExternalBraking){
            if (velocity>0) {
                velocity-= brakingAccelerate* Program.deltaTime;
                updateDrawnPosition(TO_UP);
            }
            else if (velocity <0){
                velocity+= brakingAccelerate* Program.deltaTime;
                updateDrawnPosition(TO_DOWN);
            }
            //if ((velocity < criticalDistanceToStop) && (velocity > -criticalDistanceToStop)) velocity = 0;
            yPosition+=velocity;
        }
        /*
        if (PApplet.abs(velocity)>criticalDistanceToStop){
            if (velocity>0) {
                velocity-= brakingAccelerate*Programm.deltaTime;
                updateDrawnPosition(TO_UP);
            }
            else {
                velocity+= brakingAccelerate*Programm.deltaTime;
                updateDrawnPosition(TO_DOWN);
            }
            if ((velocity < criticalDistanceToStop) && (velocity > -criticalDistanceToStop)) velocity = 0;
            yPosition+=velocity;
        }

         */
        updateNearestDistanceToCenter();
    }

    private void updateNearestDistanceToCenter() {
        nearestDistanceToScreenCenter = Program.engine.height/2-yPosition;
            // >0 under center; < 0 upper center
    }

    private void updateDrawnPosition(boolean direction) {
        if (direction == TO_DOWN) {
            if (yPosition < (-(weaponHeight / 2))) {
                yPosition += (WeaponChangingController.yStep * (weaponsNumber));
            }
        }
        else {
            if (yPosition > (Program.engine.height+(weaponHeight / 2))) {
                yPosition -= (WeaponChangingController.yStep * (weaponsNumber));
            }
        }
    }

    public void addAccelerate(int accelerate){
        velocity+=accelerate;
        //if (brakingOnCenter) brakingOnCenter = false;
    }

    private int getActualTintRenew(int position) {
        int maxDistance = Program.engine.height / 2;
        boolean mustBeHidden = false;
        if ((position<0) || (position > Program.engine.height)) mustBeHidden = true;
        if (mustBeHidden) {
            return MIN_ALPHA_TINT;
        }
        else {
            int distanceToCenter = maxDistance - position;
            int reverseDistanceToCenter = maxDistance - distanceToCenter;
            int tint = MIN_ALPHA_TINT + reverseDistanceToCenter * (MAX_ALPHA_TINT - MIN_ALPHA_TINT) / (maxDistance / 2);
            if (tint > MAX_ALPHA_TINT) tint = MAX_ALPHA_TINT;
            return tint;
        }
    }

    private int getActualTintUpdated(int position) {
        int maxDistance = Program.engine.height / 2;
        boolean mustBeHidden = false;
        if ((position<0) || (position > Program.engine.height)) mustBeHidden = true;
        //int distanceToCenter = maxDistance - position;
        if (mustBeHidden) {
            //System.out.println("for pos: " + position + " tint is 0");
            return MIN_ALPHA_TINT;
        }
        else {
            int distanceToCenter = maxDistance - position;
            int reverseDistanceToCenter = maxDistance - distanceToCenter;
            if (stoppedThroughExternalBraking) return hidingTint;
            else {
                int tint = MIN_ALPHA_TINT + reverseDistanceToCenter * (MAX_ALPHA_TINT - MIN_ALPHA_TINT) / (maxDistance / 2);
                if (tint > MAX_ALPHA_TINT) tint = MAX_ALPHA_TINT;
                //System.out.println("Tint for pos: " + position + " tint is " + (MIN_ALPHA_TINT + reverseDistanceToCenter * (MAX_ALPHA_TINT - MIN_ALPHA_TINT) / (maxDistance / 2)));
                return tint;
            }
        }
    }

    public void draw(PGraphics graphic, int tint){

        if (tint >= 0){
            drawWithHidding(graphic);
        }
        else drawWithNormalAlpha(graphic);
    }

    private void drawWithHidding(PGraphics graphic){
        graphic.tint(255, hidingTint);
        graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width/2,yPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        int secondDrawPosition = getSecondDrawPosition();
        if (secondDrawPosition != cannotBeDrawnValue){
            graphic.tint(255, hidingTint);
            graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width / 2, secondDrawPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        }
    }

    private void drawWithNormalAlpha(PGraphics graphic){
        int tint = getActualTintUpdated(yPosition); //= getActualTintRelativeToCenter(yPosition);
        //System.out.println("Another weapon; ");
        graphic.tint(255, tint);
        graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width/2,yPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        int secondDrawPosition = getSecondDrawPosition();
        if (secondDrawPosition != cannotBeDrawnValue){
            tint = getActualTintUpdated(secondDrawPosition);
            if (tint > 0) {
                graphic.tint(255, tint);
                graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width / 2, secondDrawPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
            }
        }
    }


    /*

     public void draw(PGraphics graphic, boolean whichTintValue){
        graphic.beginDraw();
        int tint = getActualTintUpdated(yPosition); //= getActualTintRelativeToCenter(yPosition);
        if (tint > 0){
            graphic.tint(255, tint);
            //System.out.print("Drawn on: " + yPosition);
            graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Programm.engine.width/2,yPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        }
        int secondDrawPosition = getSecondDrawPosition();
        if (secondDrawPosition != cannotBeDrawnValue){
            tint = getActualTintUpdated(secondDrawPosition);
            if (tint > 0) {
                graphic.noTint();
                graphic.tint(255, tint);
                //System.out.println (" and: " + secondDrawPosition);
                graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Programm.engine.width / 2, secondDrawPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
            }
        }
        graphic.endDraw();
    }
     */


    private int getSecondDrawPosition(){
        if (weaponsNumber <= 4){
            if (yPosition>(Program.engine.height/2)){
                return yPosition-(weaponsNumber*WeaponChangingController.yStep);
            }
            else {
                return yPosition+(weaponsNumber*WeaponChangingController.yStep);
            }
        }
        else return cannotBeDrawnValue;
    }

    public int getNearestDistanceToScreenCenter() {
        return nearestDistanceToScreenCenter;
    }

    public boolean isMoving() {
        if (velocity == 0) return false;
        else return true;
    }



    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public WeaponType getType() {
        return type;
    }

    public void addPosition(float additionalPos) {
        yPosition+=additionalPos;
    }

    /*
    public static void setMovementStopped(boolean flag) {
        movementStopped = flag;
    }

    public boolean isMovementStopped(){
        return movementStopped;
    }
*/
    public static boolean isStoppedThroughExternalBraking() {
        return stoppedThroughExternalBraking;
    }

    public static void setStoppedThroughExternalBraking(boolean stoppedThroughExternalBraking) {
        OnScreenWeapon.stoppedThroughExternalBraking = stoppedThroughExternalBraking;
    }

    public int getPosition() {
        return yPosition;
    }
}

/*
private int yPosition;
    private int[] drawPositions;
    private float velocity;
    private WeaponType type;
    private final static ImageZoneSimpleData pointsForPistole = new ImageZoneSimpleData(514,3,581,39);
    private final static ImageZoneSimpleData pointsForGreenadeLauncher = new ImageZoneSimpleData(508,39,675,66);
    private final static ImageZoneSimpleData pointsForAssaultRiffle = new ImageZoneSimpleData(521,69,620,122);
    private final static ImageZoneSimpleData pointsForShotgun = new ImageZoneSimpleData(520,122,634,161);

    static float  brakingAccelerate = 4f/(1000f/Programm.NORMAL_FPS);
    private int criticalDistanceToStop = 3;
    private int leftUpperX = 0;
    private int leftUpperY = 0;
    private int rightLowerX = 512;
    private int rightLowerY = 256;
    private final static int weaponWidth = (int)(Programm.engine.width/2.2f);
    private final static int weaponHeight = (int)(weaponWidth/2.4f);
    private static byte weaponsNumber;
    private static int cannotBeDrawnValue = -9999;
    private final static boolean TO_UP = true;
    private final static boolean TO_DOWN = false;

    private final static int MAX_ALPHA_TINT = 255;    //In center
    private final static int MIN_ALPHA_TINT = -100;    // on side
    private int nearestDistanceToScreenCenter;
    private boolean brakingOnCenter;
    private static boolean movementStopped;

    private static int hidingTint;

    private static boolean stoppedThroughExternalBraking;

    public OnScreenWeapon(WeaponType weaponType, int yPosition, byte number){
        this.yPosition = yPosition;
        drawPositions = new int[3];
        for (int i = 0; i < drawPositions.length; i++) drawPositions[i] = yPosition;
        this.type = weaponType;
        weaponsNumber = number;
        fillGraphicCornersData();
    }

    public static void setHiddingTint(int tint) {
        hidingTint = tint;
    }

    public static int getHidingTint() {
        return hidingTint;
    }

    public static boolean isCompleteHidden(){
        if (hidingTint<1) return true;
        else return false;
    }

    private void fillGraphicCornersData(){
        ImageZoneSimpleData data;
        if (type == WeaponType.PISTOLE) data = pointsForPistole;
        else if (type == WeaponType.GREENADE_LAUNCHER) data = pointsForGreenadeLauncher;
        else if (type == WeaponType.ASSAULT_RIFFLE) data = pointsForAssaultRiffle;
        else if (type == WeaponType.SHOTGUN) data = pointsForShotgun;
        else data = null;
        leftUpperX = data.getLeftUpperX();
        leftUpperY = data.getLeftUpperY();
        rightLowerX = data.getRightLowerX();
        rightLowerY = data.getRightLowerY();
    }

    public void update(){
        if (PApplet.abs(velocity)>criticalDistanceToStop){
            if (velocity>0) {
                velocity-= brakingAccelerate*Programm.deltaTime;
                updateDrawnPosition(TO_UP);
            }
            else {
                velocity+= brakingAccelerate*Programm.deltaTime;
                updateDrawnPosition(TO_DOWN);
            }
            if ((velocity < criticalDistanceToStop) && (velocity > -criticalDistanceToStop)) velocity = 0;
            yPosition+=velocity;
            updateNearestDistanceToCenter();
        }

    }

    private void updateNearestDistanceToCenter() {
        nearestDistanceToScreenCenter = Programm.engine.height/2-yPosition;
            // >0 under center; < 0 upper center
    }

    private void updateDrawnPosition(boolean direction) {
        if (direction == TO_DOWN) {
            if (yPosition < (weaponHeight / 2)) {
                yPosition += WeaponChangingController.yStep * (weaponsNumber);
            }
        }
        else {
            if (yPosition > (Programm.engine.height+(weaponHeight / 2))) {
                yPosition -= WeaponChangingController.yStep * (weaponsNumber);
            }
        }
    }

    public void addAccelerate(int accelerate){
        velocity+=accelerate;
        if (brakingOnCenter) brakingOnCenter = false;
    }

    private int getActualTintUpdated(int position) {
        int maxDistance = Programm.engine.height / 2;
        int distanceToCenter = PApplet.abs(maxDistance - position);
        if (distanceToCenter > maxDistance) return MIN_ALPHA_TINT;
        else {
            int reverseDistanceToCenter = maxDistance - distanceToCenter;
            if (movementStopped) return hidingTint;
            else return MIN_ALPHA_TINT + reverseDistanceToCenter * (MAX_ALPHA_TINT - MIN_ALPHA_TINT) / (maxDistance / 2);
        }
    }

    public void draw(PGraphics graphic){
        graphic.beginDraw();
        int tint = getActualTintUpdated(yPosition); //= getActualTintRelativeToCenter(yPosition);
        if (tint > 0){
            graphic.tint(255, tint);
            graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Programm.engine.width/2,yPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        }
        int secondDrawPosition = getSecondDrawPosition();
        if (secondDrawPosition != cannotBeDrawnValue){
            tint = getActualTintUpdated(secondDrawPosition);
            if (tint > 0) {
                graphic.tint(255, tint);
                graphic.image(HeadsUpDisplay.mainGraphicSource.getImage(), Programm.engine.width / 2, secondDrawPosition, weaponWidth, weaponHeight, leftUpperX, leftUpperY, rightLowerX, rightLowerY);
            }
        }
        graphic.endDraw();
    }


    private int getSecondDrawPosition(){
        if (weaponsNumber <= 3){
            if (yPosition>(Programm.engine.height/2)){
                return yPosition-(weaponsNumber*WeaponChangingController.yStep);
            }
            else {
                return yPosition+(weaponsNumber*WeaponChangingController.yStep);
            }
        }
        else return cannotBeDrawnValue;
    }

    public int getNearestDistanceToScreenCenter() {
        return nearestDistanceToScreenCenter;
    }

    public boolean isMoving() {
        if (velocity == 0) return false;
        else return true;
    }



    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public WeaponType getType() {
        return type;
    }

    public void addPosition(float additionalPos) {
        yPosition+=additionalPos;
    }

    public static void setMovementStopped(boolean flag) {
        movementStopped = flag;
    }

    public boolean isMovementStopped(){
        return movementStopped;
    }

    public static boolean isStoppedThroughExternalBraking() {
        return stoppedThroughExternalBraking;
    }

    public static void setStoppedThroughExternalBraking(boolean stoppedThroughExternalBraking) {
        OnScreenWeapon.stoppedThroughExternalBraking = stoppedThroughExternalBraking;
    }
*/

