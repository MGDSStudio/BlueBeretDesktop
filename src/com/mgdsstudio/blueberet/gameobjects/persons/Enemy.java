package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers.EnemyController;
import com.mgdsstudio.blueberet.graphic.EnemiesAnimationController;
import com.mgdsstudio.blueberet.graphic.MainGraphicController;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.mainpackage.Program;

public abstract class Enemy extends Person {
    protected EnemiesAnimationController enemiesAnimationController;
    protected EnemyController behaviourController;
    @Override

    public void loadAnimationData(MainGraphicController mainGraphicController){
        enemiesAnimationController = new EnemiesAnimationController();
    }

    @Override
    public PersonAnimationController getPersonAnimationController(){
        return enemiesAnimationController;
    }

    @Override
    public void kill(){
        super.kill();
        if (enemiesAnimationController != null){
            enemiesAnimationController.setDead(true);
        }
    }

    @Override
    protected void tintUpdatingBySelecting(){
        if (isSelected()) {
            if (enemiesAnimationController != null) {
                enemiesAnimationController.setTint(Program.engine.color(255, actualSelectionTintValue));
            }
        }
        if (selectionWasCleared){
            if (enemiesAnimationController != null) {
                enemiesAnimationController.resetTint();
            }
            System.out.println("Selection tint is reset");
            selectionWasCleared = false;
        }
    }

    public void setGraphicDimensionFromEditor(int newDiameter){
        enemiesAnimationController.setNewDimention((int)newDiameter);
    }

    public void setHalfTint() {
        enemiesAnimationController.setTint(Program.engine.color(255,133));
    }

    @Override
    public void attacked(int damageValue) {
        if (enemiesAnimationController != null) {
            enemiesAnimationController.setUnderAttack(true);
        }
        super.attacked(damageValue);
    }

}
