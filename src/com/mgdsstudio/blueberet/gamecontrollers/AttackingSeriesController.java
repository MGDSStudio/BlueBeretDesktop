package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;

public class AttackingSeriesController {
    private float actualValue;
    private int actualLevel = 1;
    private final float maxValueForFirstLevel = 10;
    private float addingValueForFirstLevelForShot = 20;
    private final float levelChangingCoef = 1.2f;
    private final float fallingDownVelocity = 10f;  // per second

    private Soldier player;

    public AttackingSeriesController(Soldier soldier, float actualValue, int actualLevel){
        this.actualLevel = actualLevel;
        this.actualValue = actualValue;
        this.player = soldier;
    }

    public void update(){

    }

    public void enemyWasHitted(){

    }


}
