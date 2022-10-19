package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamecontrollers.BulletTimeController;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class LightSource {

    private final static int FIRE_RED = 255, FIRE_GREEN = 255, FIRE_BLUE = 0;
    private final static int DRUG_RED = 35, DRUG_GREEN = 125, DRUG_BLUE = 5;
    private int red = FIRE_RED, green = FIRE_GREEN, blue = FIRE_BLUE;
    private Bullet bullet;
    private Coordinate pos;
    private int type;
    private Timer timer;
    private Timer intensityTimer;
    private final int SHOT_FLASH_TIME = 100;
    private final int BULLET_TIME = 1500;
    public final static int TYPE_FULL_SCREEN_FLASH = 0;
    public final static int SHOT_FLASH_WITH_POS = 1;
    public final static int TYPE_BULLET_FLASH = 2;

    public final static int TYPE_EXPLOSION_FLASH = 3;
    public final static int TYPE_BULLET_TIME_FLASH = 4;
    private boolean ended;

    private final static int MAX_Z_DISTANCE = 800;
    private final static int MIN_Z_DISTANCE = (int) (150*((float)Program.engine.width/(float)Program.XIAOMI_REDMI_WIDTH));


    private int actualZ = MAX_Z_DISTANCE;

    public LightSource(int type, float x, float y) {
        this.type = type;
        pos = new Coordinate(x,y);
        if (type == TYPE_FULL_SCREEN_FLASH){
            timer = new Timer(SHOT_FLASH_TIME);
            intensityTimer = new Timer(SHOT_FLASH_TIME);
        }
        else if (type == TYPE_EXPLOSION_FLASH){
            System.out.println("Explosion flash was added with time: " + Explosion.EXPLOSION_TIME);
            timer = new Timer(Explosion.EXPLOSION_TIME);
            intensityTimer = new Timer(Explosion.EXPLOSION_TIME);
        }
        else {
            timer = new Timer(SHOT_FLASH_TIME);
            intensityTimer = new Timer(SHOT_FLASH_TIME);
        }
        actualZ = MAX_Z_DISTANCE;
    }


    public LightSource(int type) {
        init(type, SHOT_FLASH_TIME);
        /*
        this.type = type;
        if (type == TYPE_FULL_SCREEN_FLASH){
            timer = new Timer(SHOT_FLASH_TIME);
        }
        else timer = new Timer(SHOT_FLASH_TIME);
        intensityTimer = new Timer(SHOT_FLASH_TIME);
        actualZ = MAX_Z_DISTANCE;*/
    }

    public LightSource(int type, int timeToHide) {
        init(type, timeToHide);
        /*this.type = type;
        timer = new Timer(timeToHide);
        intensityTimer = new Timer(timeToHide);
        actualZ = MAX_Z_DISTANCE;
        if (type == TYPE_BULLET_TIME_FLASH) {
            red = DRUG_RED;
            green = DRUG_GREEN;
            blue = DRUG_BLUE;
        }*/
    }

    private void init(int type, int timeToHide){
        this.type = type;
        if (timer == null) timer = new Timer(timeToHide);
        else timer.setNewTimer(timeToHide);
        if (intensityTimer == null) intensityTimer = new Timer(timeToHide);
        else intensityTimer.setNewTimer(timeToHide);
        actualZ = MAX_Z_DISTANCE;
        if (type == TYPE_BULLET_TIME_FLASH) {
            red = DRUG_RED;
            green = DRUG_GREEN;
            blue = DRUG_BLUE;
        }
        ended = false;
    }

    public void recreate(int type, int timeToHide){
        init(type, timeToHide);

    }

    public void recreate(int type){
        init(type, SHOT_FLASH_TIME);

    }

    public boolean isEnded(){
        //System.out.println("Rest time: " + timer.getRestTime() + " ended " + ended);
        if (ended) return true;
        else {
            if (timer.isTime()){
                ended = true;
                return ended;
            }
            else if (type == TYPE_BULLET_FLASH){
                if (!bullet.isActive()) {
                    ended = true;
                    return ended;
                }
                else return false;
            }
            else return false;
        }
    }

    public Coordinate getPos(){
        return pos;
    }

    public int getOnScreenX(GameCamera gameCamera){
        if (type == TYPE_FULL_SCREEN_FLASH || type == TYPE_BULLET_TIME_FLASH) {
            int onScreen = Program.engine.width/2;
            return onScreen;
        }
        else if (type == TYPE_BULLET_FLASH){
            int onScreen = (int) gameCamera.getOnScreenPosX(bullet.getPixelPosition().x);
            return onScreen;
        }
        else  return (int) gameCamera.getOnScreenPosX(pos.x);
    }

    public int getOnScreenY(GameCamera gameCamera){
        if (type == TYPE_FULL_SCREEN_FLASH || type == TYPE_BULLET_TIME_FLASH) {
            int onScreen = Program.engine.height/2;
            return onScreen;
        }
        else if (type == TYPE_BULLET_FLASH){
            int onScreen = (int) gameCamera.getOnScreenPosY(bullet.getPixelPosition().y);
            return onScreen;
        }
        else  return (int) gameCamera.getOnScreenPosY(pos.y);
    }

    public int getActualZDistanceToLight(){
        if (type == TYPE_BULLET_FLASH || type == TYPE_BULLET_TIME_FLASH){
            int distance = (MIN_Z_DISTANCE+(int) (intensityTimer.getRelativeRestTime()*(MAX_Z_DISTANCE-MIN_Z_DISTANCE)));

            return distance;
        }
        else if (type == TYPE_FULL_SCREEN_FLASH){
            return getRelativeDistanceForHillGraph();
        }
        else if (type == TYPE_EXPLOSION_FLASH){
            return getRelativeDistanceForHillGraph();
        }
        else {
           return getRelativeDistanceForHillGraph();
        }
    }

    private int getRelativeDistanceForHillGraph(){
        float relative = intensityTimer.getRelativeRestTime();
        if (relative<0){
            System.out.println("Relative value: " + relative + "; Trouble: ");
            relative = 0;
        }
        if (relative>=0.5f){
            float value = 1f-relative;  //from 0 to 0.5f
            int distanceForValue = (int) (value*(MAX_Z_DISTANCE-MIN_Z_DISTANCE)/0.5f);
            return distanceForValue+MIN_Z_DISTANCE;
        }
        else {
            float value = relative;
            int distanceForValue = (int) (value*(MAX_Z_DISTANCE-MIN_Z_DISTANCE)/0.5f);
            return distanceForValue+MIN_Z_DISTANCE;
        }
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void bulletTimeWasActivated(boolean activated) {
        if (activated) timer.enterBulletTimeMode(1f/BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS);
        else timer.enterBulletTimeMode(BulletTimeController.BULLET_TIME_COEF_FOR_OBJECTS);
    }
}
