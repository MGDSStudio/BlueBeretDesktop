package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.HandGrenade;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PVector;

class CroshairPoints{
    private final PVector actualPos, prevPos;
    private final Image graphic;
    private final Soldier soldier;
    private final ImageZoneSimpleData imageDataWhite, imageDataBlack;

    private final int pointsNumber = 4;

    private int [] diameters;
    private int  [] alphas;

    private final boolean WITH_GRAY_CHANGING = true;
    private int actualGreyTint = 255;

    private float greyChangingVelocity;
    private boolean tintChangingDirection;
    private float gravityCoef = HandGrenade.GRAVITY_COEF* 2.55f;
    private Coordinate [] pos;
    private float posStep;
    private float actualWeaponAngle;

    private boolean useRunningPoints = true;
    private Timer nextStartPointTimer;
    private final int NEXT_POINT_TIME = 150;
    private int actualStartPoint = 0;
    private boolean visible;

    private boolean useParabola = true;
    private final float posToTimeTransferCoef = 2;

    public CroshairPoints(Soldier soldier) {
        this.soldier = soldier;
        actualPos = new PVector(soldier.getPixelPosition().x, soldier.getPixelPosition().y);
        prevPos = new PVector(soldier.getPixelPosition().x, soldier.getPixelPosition().y);
        graphic = HeadsUpDisplay.mainGraphicSource;

        imageDataWhite = new ImageZoneSimpleData(678,0, 683,5);
        imageDataBlack = new ImageZoneSimpleData(673,0, 678,5);
        initDiams(pointsNumber);
        initGreyData(pointsNumber);
        initGravityData();
    }

    private void initGravityData() {
        // width = 500 coef = 3.8
        // width = 720 coef = 2.55
        if (Program.engine.width == Program.DEBUG_DISPLAY_WIDTH) gravityCoef = HandGrenade.GRAVITY_COEF* 3.8f;
        else if (Program.engine.width == 720 && Program.engine.height == 1440) {
            System.out.println("It is a HUAWEY Y5");
            gravityCoef = HandGrenade.GRAVITY_COEF* 2.55f;
        }
        else if (Program.engine.width == 480) gravityCoef = HandGrenade.GRAVITY_COEF* 2.25f;

        else{
            float rel = (float)Program.DEBUG_DISPLAY_WIDTH/2.55f;
            float newCoef = (float)(Program.engine.width)/rel;
            gravityCoef = HandGrenade.GRAVITY_COEF* 2.55f;
            System.out.println("Width of the screen: " + Program.engine.width);
            useParabola = false;
        }
    }

    private void initGreyData(int pointsNumber) {
        final int maxGrey = 255;
        final int minGrey = 0;
        final int timeToFullAlphaChanging = 1000;
        greyChangingVelocity = (float)maxGrey/(float) timeToFullAlphaChanging;
        System.out.println("Grey changing velocity: " + greyChangingVelocity + " per ms");
    }

    private void initDiams(int pointsNumber){
        diameters = new int[pointsNumber];
        float startDiameter = Soldier.getNormalBodyWidth()/2.2f;
        float endDiameter = startDiameter/2.5f;
        float diamStep = (startDiameter-endDiameter)/pointsNumber;

        alphas = new int[pointsNumber];
        int startAlpha = 255;
        int endAlpha = 25;
        int alphaStep = (startAlpha-endAlpha)/pointsNumber;

        pos = new Coordinate[pointsNumber];
        for (int i = 0; i < diameters.length; i++){
            diameters[i] = (int) (startDiameter-(i*diamStep));
            alphas[i] = (startAlpha-(i*alphaStep));
            if (Program.debug) System.out.println("Point " + i + " diam: " + diameters[i] + ", alpha: " + alphas[i]);
        }

        posStep = startDiameter*2;

    }

    public void draw(GameCamera gameCamera) {
        if (visible) {
            Program.objectsFrame.pushMatrix();
            Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
            Program.objectsFrame.translate(actualPos.x - gameCamera.getActualXPositionRelativeToCenter(), actualPos.y - gameCamera.getActualYPositionRelativeToCenter());
            Program.objectsFrame.pushStyle();
            float actualX;
            float actualY;
            ImageZoneSimpleData data = getImageData();
            updateActualGrey();
            for (int i = 0; i < pos.length; i++) {
                Program.objectsFrame.tint(actualGreyTint, alphas[i]);
                actualX = getActualPosX(i+2);
                actualY = getActualPosY(i+2);
                int pointDiam = getActualDiameter(i);
                Program.objectsFrame.image(graphic.getImage(), actualX, actualY, pointDiam, pointDiam, data.leftX, data.upperY, data.rightX, data.lowerY);
            }
            Program.objectsFrame.popStyle();
            Program.objectsFrame.popMatrix();
        }
    }

    private float getActualPosX(int pointNumber){
        if (!useParabola) return ((pointNumber ) * posStep * PApplet.cos(PApplet.radians(actualWeaponAngle)));
        else {
            float time = posToTimeTransferCoef*pointNumber;
            float posX = HandGrenade.NORMAL_SPEED*time*PApplet.cos(PApplet.radians(actualWeaponAngle));
            return posX;
        }
    }

    private float getActualPosY(int pointNumber){
        if (!useParabola) return ((pointNumber) * posStep * PApplet.sin(PApplet.radians(actualWeaponAngle)));
        else {
            float time = posToTimeTransferCoef*pointNumber;
            float posY = PApplet.sin(PApplet.radians(actualWeaponAngle))*HandGrenade.NORMAL_SPEED*time+(gravityCoef*time*time)/2;
            return posY;
        }
    }

    private void updateActualGrey() {
        if (WITH_GRAY_CHANGING) {
            float value = Program.deltaTime*greyChangingVelocity;
            if (tintChangingDirection){ //to 0
                actualGreyTint-=value;
                if (actualGreyTint<=0){
                    actualGreyTint = 0;
                    tintChangingDirection = false;
                }
            }
            else {
                actualGreyTint+=value;
                if (actualGreyTint>=255){
                    actualGreyTint = 255;
                    tintChangingDirection = true;
                }
            }
        }
        //System.out.println("Actual tint " + actualGreyTint);
    }

    private int getActualDiameter(int pointNumber){
        if (useRunningPoints) {
            updateActualStartPointNumber();
            if (actualStartPoint == 0) return diameters[pointNumber];
            else {
                int realNumber = pointNumber - actualStartPoint;
                if (realNumber < 0) realNumber += diameters.length;
                return diameters[realNumber];
            }
        }
        else return diameters[pointNumber];
    }

    private void updateActualStartPointNumber(){
        if (nextStartPointTimer == null) {
            nextStartPointTimer = new Timer(NEXT_POINT_TIME);
            actualStartPoint = 0;
        }
        else if (nextStartPointTimer.isTime()){
            actualStartPoint++;
            if (actualStartPoint>= pointsNumber) actualStartPoint = 0;
            nextStartPointTimer.setNewTimer(NEXT_POINT_TIME);
        }
    }

    private ImageZoneSimpleData getImageData(){
        return imageDataWhite;
    }

    public void update(GameRound gameRound) {
        if (gameRound.getPlayer().isAlive() && gameRound.getPlayer().getActualWeapon().areThereBulletsInMagazine() && gameRound.getPlayer().getStatement() != Person.IN_AIR && !gameRound.getPlayer().isPersonRunning() && !gameRound.getPlayer().isTransferingThroughPortal()) {
            if (!visible) visible = true;
            actualWeaponAngle = soldier.getWeaponAngle();
            updateParabola(actualWeaponAngle);
        }
        else if (visible) visible = false;
    }

    private void updateParabola(float shootingAngle) {
        actualPos.x = soldier.getPixelPosition().x;
        actualPos.y = soldier.getPixelPosition().y;


    }

    public void consoleInput(String s) {
        /*
        try {
            int value = Integer.parseInt(s);
            float valueFloat = (float)value/100;
            //gravityCoef = HandGrenade.GRAVITY_COEF* valueFloat;
            System.out.println("New coef = " + valueFloat);
            System.out.println("For width: " + Program.engine.width);
            // 3.8 for desktop with width:
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
