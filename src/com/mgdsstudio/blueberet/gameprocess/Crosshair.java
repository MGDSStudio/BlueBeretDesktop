package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamecontrollers.CrosshairAlphaController;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import processing.core.PVector;

class Crosshair implements IDrawable {

    public final static boolean CROSSHAIR_CROSS = true;
    public final static boolean CROSSHAIR_POINTS = false;
    private boolean croshairType = CROSSHAIR_CROSS;

    //private final Soldier soldier;

    private float shootingAngle;

    private CrosshairCross crosshairCross;
    private CroshairPoints croshairPoints;

    Crosshair(Soldier soldier){
        crosshairCross = new CrosshairCross(soldier);
        croshairPoints = new CroshairPoints(soldier);
    }


    @Override
    public void draw(GameCamera gameCamera) {
        //System.out.println("Draw crosshair for type: " + croshairType);
        if (croshairType == CROSSHAIR_CROSS) crosshairCross.draw(gameCamera);
        else croshairPoints.draw(gameCamera);
    }



    public void setCroshairType(boolean type){
        this.croshairType = type;
    }

    public void update(GameRound gameRound){
        if (croshairType == CROSSHAIR_CROSS) {
            crosshairCross.update(gameRound);
        }
        else croshairPoints.update(gameRound);
        /*
        if (croshairType == CROSSHAIR_CROSS) {
            crosshairCross.update(gameRound, shootingAngle);
        }
        else croshairPoints.update(gameRound, shootingAngle);
         */
    }

    public void consoleInput(String s) {
        croshairPoints.consoleInput(s);
    }
}
