package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

class BackgroundShadowerForWeaponChanging {
    private long startTime;
    private float timeToShadowing = 500;
    private long goalTime;
    private long endTime;
    private final byte NOTHING = 0;
    private final byte TO_SHADOW = 1;
    private final byte SHADOWING = 2;
    private final byte TO_DESHADOW = 3;
    private byte statement = NOTHING;
    private int START_TINT = 0;
    private int END_TINT = 60;
    private int actualTint = START_TINT;
    private float tintChangingCoefficient;

    BackgroundShadowerForWeaponChanging(){
        calculateTintCoef();
        System.out.println("Tint step: " + tintChangingCoefficient);
    }

    BackgroundShadowerForWeaponChanging(int timeToShadowing){
        this.timeToShadowing = timeToShadowing;
        calculateTintCoef();
        System.out.println("Tint step: " + tintChangingCoefficient);
    }

    private void calculateTintCoef(){
        tintChangingCoefficient = (END_TINT-START_TINT)/(timeToShadowing);
    }

    boolean isSwitchedOff() {
        if (statement == NOTHING) return  true;
        else return false;
    }

    void switchOff(){
        if (statement != NOTHING){
            statement = TO_DESHADOW;
        }
    }

    void switchOn(int startTime){
        if (statement == NOTHING) {
            this.startTime = startTime;
            //goalTime = startTime + timeToShadowing;
            statement = TO_SHADOW;
        }
    }

    void update(){
        if (statement != NOTHING){
            if (statement == TO_SHADOW){
                actualTint+= (int)(tintChangingCoefficient* Program.deltaTime);
                if (actualTint >= END_TINT) {
                    statement = SHADOWING;
                    actualTint = END_TINT;
                }
            }
            else if (statement == SHADOWING){

            }
            else if (statement == TO_DESHADOW){
                actualTint-= (int)(tintChangingCoefficient* Program.deltaTime);
                if (actualTint <= START_TINT) {
                    statement = NOTHING;
                    actualTint = START_TINT;
                }
            }
            System.out.println("Ting: " + actualTint);
        }
    }


    void draw(PGraphics graphics){
        if (statement != NOTHING) {
            graphics.background(0, actualTint);
        }
    }
}
