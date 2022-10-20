package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;


public class MercenaryBehaviourController extends EnemyController{
    private interface IBehaviours{
        int PATROL_PLATFORM_AND_ATTACK_IF_VISIBLE = 0;
    }

    private interface IConstants{
        int TIME_FOR_STOPPING_BY_WALL = 1000;
    }

    private interface GlobalStatements{

    }

    private interface LocalStatements{

    }

    private ShootingController shootingController;
    //private Mercenary mercenary;
    //private int behaviour;

    public MercenaryBehaviourController(Mercenary enemy, int behaviourType) {
        super(enemy, behaviourType);
        stayingTimer = new Timer(IConstants.TIME_FOR_STOPPING_BY_WALL);
        shootingController = new ShootingController((Mercenary) enemy);
        //this.mercenary = enemy;
        //this.behaviour = behaviourType;
    }


    public void update(GameRound gameRound) {
        if (behaviourType == IBehaviours.PATROL_PLATFORM_AND_ATTACK_IF_VISIBLE){
            updatePatrolPlatform(gameRound);
        }
    }

    private void updatePatrolPlatform(GameRound gameRound) {
        boolean groundUnderFeet = areThereGroundUnderFeet(gameRound, movementDirection);
        boolean mustAttack = shootingController.mustAttack(gameRound);
        //if (mustAttack) System.out.println("Attack!");
        if (mustAttack) shootingController.attackPlayer(gameRound);
        else {
            if (groundUnderFeet) {
                updateMovementOnGround(gameRound);
            } else {
                boolean clearingZonesUnderFeet = areThereClearingZoneInFront(gameRound);
                if (!clearingZonesUnderFeet) {
                    updateMovementOnGround(gameRound);
                } else {
                    //System.out.println("Clear area under the feet and there are no paltform");
                    braking();
                }
            }
        }

    }



    private void updateMovementOnGround(GameRound gameRound) {
        if (enemy.getStatement() != Person.IN_AIR) {
            move();
        }
    }

    private void braking() {
        if (movementDirection == LEFT) {
            mutableBrakingAccelerate.x = enemy.body.getMass() * 172.2f;
        } else {
            mutableBrakingAccelerate.x = -enemy.body.getMass() * 172.2f;
        }
        enemy.body.applyForceToCenter(mutableBrakingAccelerate);
    }

    private void move(){
        if (movementDirection == LEFT) {
            enemy.move(true, Person.TO_LEFT);
            enemy.setActualByUserPressedMovement(PlayerControl.USER_PRESSES_GO_LEFT);
            enemy.orientation = Person.TO_LEFT;
            applyWeaponAngle(180);
        } else if (movementDirection == RIGHT) {
            enemy.move(true, Person.TO_RIGHT);
            enemy.setActualByUserPressedMovement(PlayerControl.USER_PRESSES_GO_RIGHT);
            enemy.orientation = Person.TO_RIGHT;
            applyWeaponAngle(0);
        }
        if (isBlockedAlongX() == true) {
            changeMovementDirection();
        }
    }

    private void applyWeaponAngle(float angle) {
        if (Program.OS == Program.DESKTOP) {
            if (angle < 0) angle = 360 + angle;
            if (angle > 360) angle = angle - 360;
        }
        enemy.setWeaponAngle(angle);
        //System.out.println("Angle was set on " + angle);
        Human human = (Human) enemy;
        human.updateOrientationByWeaponAngle();
        if (Program.OS == Program.ANDROID) {
            if (angle < 90 || angle > 270) {
                if (enemy.orientation == Person.TO_LEFT) enemy.orientation = Person.TO_RIGHT;
            }
            if (angle >= 90 && angle <= 270) {
                if (enemy.orientation == Person.TO_RIGHT) enemy.orientation = Person.TO_LEFT;
            }
        }
    }


    public void kill() {

    }


    public boolean isStartedToDie() {
        return false;
    }


    public void attacked() {

    }

    protected boolean areThereGroundUnderFeet(GameRound gameRound, int movementDirection) {
        mutableTestPointForGroundFinding.x = enemy.body.getPosition().x;
        if (movementDirection == LEFT){
            mutableTestPointForGroundFinding.x -= PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getWidth()/1.515f);
        }
        else{
            mutableTestPointForGroundFinding.x +=PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getWidth()/1.515f);
        }
        mutableTestPointForGroundFinding.y= enemy.body.getPosition().y-PhysicGameWorld.controller.scalarPixelsToWorld(enemy.getHeight()*1.25f);
        if (Program.engine.frameCount%2 == 0) {
            if (!gameRound.isPointInSomeRoundElements(mutableTestPointForGroundFinding)) {
                return false;
            }
            else {
                if (Program.debug) {
                    DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, PhysicGameWorld.controller.coordWorldToPixels(mutableTestPointForGroundFinding));
                    gameRound.addDebugGraphic(debugGraphic);
                }
                return true;
            }
        }
        return true;
    }

    private class ShootingController{

        private final Mercenary mercenary;
        private class AttackParameters{
            private int deltaVisibleAngle = 110;
            private int criticalLeftUpperAngle = 180+deltaVisibleAngle;
            private int criticalLeftLowerAngle = 180-deltaVisibleAngle;
            private int criticalRightUpperAngle = 360-deltaVisibleAngle;
            private int criticalRightLowerAngle = deltaVisibleAngle;

            public int getDeltaVisibleAngle() {
                return deltaVisibleAngle;
            }

            public int getCriticalLeftUpperAngle() {
                return criticalLeftUpperAngle;
            }

            public int getCriticalLeftLowerAngle() {
                return criticalLeftLowerAngle;
            }

            public int getCriticalRightUpperAngle() {
                return criticalRightUpperAngle;
            }

            public int getCriticalRightLowerAngle() {
                return criticalRightLowerAngle;
            }
        }

        private AttackParameters attackParameters;
        ShootingController(Mercenary mercenary){
            this.mercenary = mercenary;
            attackParameters = new AttackParameters();
        }

        public void attackPlayer(GameRound gameRound) {

        }

        protected boolean mustAttack(GameRound gameRound){
            float angleToPlayer = GameMechanics.getAngleToObject(enemy.getPixelPosition(), gameRound.getPlayer().getPixelPosition());
            if (enemy.orientation == Person.TO_LEFT) {
                if (gameRound.getPlayer().getPixelPosition().x < enemy.getPixelPosition().x) { //left
                    if (angleToPlayer < attackParameters.criticalLeftUpperAngle && angleToPlayer > attackParameters.criticalLeftLowerAngle) {
                        return true;
                    }
                }
            }
            else {
                if (gameRound.getPlayer().getPixelPosition().x > enemy.getPixelPosition().x) { //left
                    if (angleToPlayer < attackParameters.criticalRightLowerAngle || (angleToPlayer > attackParameters.criticalRightUpperAngle)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
