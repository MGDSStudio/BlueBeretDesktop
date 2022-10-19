package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.LightSource;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class LightsController {
    private final ArrayList <LightSource> lightSources;
    private final int clearingFrame = 300;
    private boolean withPool = true;


    public LightsController() {
        lightSources = new ArrayList<>();
    }

    public ArrayList<LightSource> getLightSources() {
        return lightSources;
    }

    public void addFullScreenLight(){
        boolean poolUsed = false;
        if (withPool) {
            for (int i = (lightSources.size()-1) ; i >= 0; i--) {
                if (lightSources.get(i).isEnded()) {
                    lightSources.get(i).recreate(LightSource.TYPE_FULL_SCREEN_FLASH);
                    poolUsed = true;
                    if (Program.debug) System.out.println("New light for shot was recreated from pool");
                    break;
                }
            }
        }
        if (!poolUsed){
            if (Program.debug) System.out.println("New light for shot was added");
            LightSource lightSource = new LightSource(LightSource.TYPE_FULL_SCREEN_FLASH);
            lightSources.add(lightSource);
        }
        if (Program.debug) System.out.println("We have " + lightSources.size() + " light sources");
    }

    public void addBulletTimeLightWithoutPool(int time){

            if (Program.debug) System.out.println("New light for bullet time was added for " + time);
            LightSource lightSource = new LightSource(LightSource.TYPE_BULLET_TIME_FLASH, time);
            lightSources.add(lightSource);

        if (Program.debug) System.out.println("We have " + lightSources.size() + " light sources");
    }

    public void addBulletTimeLight(int time){
        boolean poolUsed = false;
        if (withPool) {
            for (LightSource lightSource : lightSources) {
                if (lightSource.isEnded()) {
                    lightSource.recreate(LightSource.TYPE_BULLET_TIME_FLASH, time);
                    poolUsed = true;
                    if (Program.debug)  System.out.println("New light for bullet time was recreated from pool for " + time);
                    break;
                }
            }
        }
        if (!poolUsed) {
            if (Program.debug) System.out.println("New light for bullet time was added for " + time);
            LightSource lightSource = new LightSource(LightSource.TYPE_BULLET_TIME_FLASH, time);
            lightSources.add(lightSource);
        }

        if (Program.debug) System.out.println("We have " + lightSources.size() + " light sources");
    }

    /*
    public void addSingleLight(float x, float y){
        //LightSource lightSource = new LightSource(LightSource.SHOT_FLASH_WITH_POS, x,y);
        LightSource lightSource = new LightSource(LightSource.SHOT_FLASH_WITH_POS, x,y);
        lightSources.add(lightSource);
    }*/

    public void addSingleLight(Bullet bullet){
        /*LightSource lightSource = new LightSource(bullet);
        lightSources.add(lightSource);*/
        if (Program.debug ) System.out.println("No flash for bullets");
    }

    public boolean areThereLights() {
        if (lightSources.size() > 0){
            boolean withLights = false;
            for (int i = (lightSources.size()-1); i >= 0; i--){
                if (lightSources.get(i).isEnded()){
                    //if (Program.engine.frameCount%clearingFrame == 0) lightSources.remove(i);
                }
                else {
                    withLights = true;
                    break;
                }
            }
            if (withLights) return true;
            else return false;
        }
        else return false;
    }

    /*
    public boolean areThereLights() {
        if (lightSources.size() > 0){
            boolean withLights = false;
            for (int i = (lightSources.size()-1); i >= 0; i--){
                if (lightSources.get(i).isEnded()){
                    //if (Program.engine.frameCount%clearingFrame == 0) lightSources.remove(i);
                }
                else withLights = true;
                break;
            }
            if (withLights) return true;
            else return false;
        }
        else return false;
    }
     */

    public void addExplosionLight(float x, float y) {
        LightSource lightSource = new LightSource(LightSource.TYPE_EXPLOSION_FLASH, x,y);
        lightSources.add(lightSource);
    }

    public void bulletTimeIsActivated(boolean activated) {
            for (LightSource lightSource : lightSources){
                lightSource.bulletTimeWasActivated(activated);
            }
    }

    public void clearEndedLights() {
        for (int i = (lightSources.size() - 1); i >= 0; i--) {
            if (lightSources.get(i).isEnded()) {
                lightSources.remove(i);
            }
        }
    }
}
