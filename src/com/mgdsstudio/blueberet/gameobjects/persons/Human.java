package com.mgdsstudio.blueberet.gameobjects.persons;

import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.controllers.PlayerSawedOffShotgunAnimationController;
import com.mgdsstudio.blueberet.graphic.controllers.PlayerShotgunAnimationController;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;

public abstract class Human extends Person{
    protected GameRound gameRound;
    protected HumanAnimationController animationController;
    protected boolean blockedThoughtExternalForces = false;

    protected boolean kicking;

    protected boolean corpseBecameCircle = true;
    protected HumanBodyData bodyData;
    protected PlayerBag playerBag;


    public void blockHumanMovementForOneFrame(boolean flag) {
        if (flag){
            blockedThoughtExternalForces = true;
            controlBlocked = true;
            animationController.setControlBlocked(true);
        }
        else {
            blockedThoughtExternalForces = false;
            controlBlocked = false;
            animationController.setControlBlocked(false);
        }
    }

    @Override
    public void addReboundJump() {
        super.addReboundJump();
        animationController.reboundJumpStarted();
    }

    public  final void setLife(int life){
        this.life = life;
    }

    public final void setMaxLife(int life){
        this.maxLife = life;
    }


    public final void setKickEnded(boolean flag) {
        kicking = !flag;
    }

    public void setKicking(boolean kicking) {
        this.kicking = kicking;
    }

    public Vec2 getAttackValueForKick() {
        float attackLinearImpulsValue = 1200f;
        float kickAngle = getKickAngleForActualStickOrientation(true);
        System.out.println("Correction angle for kick; Attack under angle: " + kickAngle + "; weapon angle: " + weaponAngle);
        Vec2 attackValue = new Vec2(attackLinearImpulsValue * PApplet.cos(PApplet.radians(kickAngle)), attackLinearImpulsValue * PApplet.sin(PApplet.radians(kickAngle)));
        return attackValue;
    }

    public float getKickAngleForActualStickOrientation(boolean withRandomValue){
        float randomValue = 0f;
        if (withRandomValue) randomValue = Program.engine.random(-5, 5);
        float attackAngle = 315 + randomValue;
        if (orientation == TO_LEFT) {
            attackAngle = 225 + randomValue;
        }
        float correctionAngle = getCorrectionAngleForKick();
        attackAngle+=correctionAngle;
        return attackAngle;
    }




    private float getCorrectionAngleForKick() {
        float relativeNullAngle = 0;
        if (orientation == TO_RIGHT){
            if (weaponAngle >= 0 && weaponAngle < 90) {
                relativeNullAngle = weaponAngle;	// 0 to 60
            }
            else if (weaponAngle <=360 && weaponAngle > 270){
                relativeNullAngle = weaponAngle-360;
                //300-360
            }
        }
        else {
            relativeNullAngle = weaponAngle-180;
        }


        float coef =0.5f;
        return (relativeNullAngle*coef);
    }

    public int getKickAttackPower() {
        return 45;
    }

    public void kill() {
        dead = true;
        body.setFixedRotation(false);
        if (corpseBecameCircle) {
            body.setActive(false);
            makeDeadBody();
            body.setGravityScale(body.getGravityScale()*0.8f);
            body.setActive(true);
            setFilterDataForCategory(CollisionFilterCreator.CATEGORY_PLAYER);
        }
        body.setActive(true);
        body.setGravityScale(4.5f);
        body.resetMassData();
        //System.out.println("Object " + this.getClass() + " is dead");
    }

    protected final void makeDeadBody() {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(getPixelPosition()));
        body = PhysicGameWorld.controller.createBody(bd);
        CircleShape footCircle = new CircleShape();
        footCircle.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(bodyData.getHeadRadius());
        Vec2 offset = new Vec2(0, bodyData.getDeltaY());
        offset = PhysicGameWorld.controller.vectorPixelsToWorld(offset);
        footCircle.m_p.set(offset.x, offset.y);
        PolygonShape bodyUpperPart = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(bodyData.getBodyWidth() / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld((bodyData.getBodyHeight() / 2) - bodyData.getHeadRadius());
        bodyUpperPart.setAsBox(box2dW, box2dH);
        boolean succesfully = false;
        while (!succesfully) {
            try {
                //body.createFixture(bodyUpperPart, 0.1f);
                body.createFixture(bodyUpperPart, 1.7f);
                //else body.createFixture(sd, 16.7f);
                if (corpseBecameCircle)	body.createFixture(footCircle, 0.01f);
                else body.createFixture(footCircle, 16.7f);
                for (Fixture f = body.getFixtureList(); f != null; f= body.getFixtureList().getNext()){
                    if (f.getShape().getClass() == CircleShape.class) {
                        float friction  = 3.5f;
                        System.out.println("Fixture friction set on " + friction);

                        body.getFixtureList().setFriction(friction);
                        break;
                    }
                }
                body.setGravityScale(2);
                succesfully = true;
            } catch (Exception e) {
                System.out.println("Can not create body for soldier");
            }
        }

        System.out.println("Dead body was created for player");
        body.setUserData(CLASS_NAME);
    }

    protected void makeBody(Vec2 center, float width, float height) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        bd.position.set(PhysicGameWorld.controller.coordPixelsToWorld(center));
        body = PhysicGameWorld.controller.createBody(bd);

        CircleShape head = new CircleShape();
        head.m_radius = PhysicGameWorld.controller.scalarPixelsToWorld(bodyData.getHeadRadius());
        Vec2 offset = new Vec2(0, bodyData.getDeltaY());
        offset = PhysicGameWorld.controller.vectorPixelsToWorld(offset);
        head.m_p.set(offset.x, offset.y);
        PolygonShape bodyRect = new PolygonShape();
        float box2dW = PhysicGameWorld.controller.scalarPixelsToWorld(bodyData.getBodyWidth() / 2);
        float box2dH = PhysicGameWorld.controller.scalarPixelsToWorld(bodyData.getBodyHeight() / 2);
        bodyRect.setAsBox(box2dW, box2dH);

        boolean succesfully = false;
        while (!succesfully) {
            try {
                if (corpseBecameCircle){
                    body.createFixture(bodyRect, 12.7f);
                }
                else body.createFixture(bodyRect, 0.7f);
                body.getFixtureList().setFriction(5.01f);
                //System.out.println("Mercenary friction to floor must be tested");
                body.createFixture(head, 0.7f);
                //System.out.println("Mercenary was created without feet");
                body.setGravityScale(2);
                succesfully = true;
            } catch (Exception e) {
                System.out.println("Can not create body for soldier");
            }
        }
        //body.setGravityScale(2);
        body.setUserData(CLASS_NAME);
    }

    public float getBodyWidth() {
        return bodyData.bodyWidth;
    }

    @Override
    public int getPersonWidth() {
        return (int) bodyData.getBodyWidth();
    }

    public boolean isShootingAtThisFrame() {
        if (attackStartedOnPrevFrame && attackStartedOnPrevFrame) return true;
        else return false;
    }


    public void setReloadCompleted(boolean broken) {
        if (getActualWeapon().getWeaponType() != WeaponType.SHOTGUN && getActualWeapon().getWeaponType() != WeaponType.REVOLVER){
            reloadWeaponsWithMagazines(broken);
        }
        else {
            reloadWeaponWithSingleBullets(broken);
        }
        gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
    }


    public PlayerBag getPlayerBag() {
        return playerBag;
    }

    public int getMoney(){
        return playerBag.getMoney();
    }

    private void reloadWeaponsWithMagazines(boolean broken){
        if (getActualWeapon().getWeaponType() == WeaponType.SAWED_OFF_SHOTGUN){
            try {
                PlayerSawedOffShotgunAnimationController controller = (PlayerSawedOffShotgunAnimationController) animationController;
                if (controller.isTwoSleevesMustBeThrownByThisReloading()){
                    playerBag.getOneAmmo(getActualWeapon().getWeaponType());
                    boolean hasBullets = playerBag.getOneAmmo(getActualWeapon().getWeaponType());
                    if (!hasBullets){
                        System.out.println("Player has only one sleeve");
                        getActualWeapon().setReloadCompleted(1);
                    }
                    else {
                        System.out.println("Player has given 2 shells for this reloading cycle");
                        getActualWeapon().setReloadCompleted(2);
                    }
                }
                else {
                    playerBag.getOneAmmo(getActualWeapon().getWeaponType());
                    getActualWeapon().setReloadCompleted(-1);
                    System.out.println("Player has given 2 shells for this reloading cycle");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                playerBag.getOneAmmo(getActualWeapon().getWeaponType());
                getActualWeapon().setReloadCompleted(-1);
            }
        }
        else {
            playerBag.getOneAmmo(getActualWeapon().getWeaponType());
            if (broken) getActualWeapon().setReloadBroken();
            else getActualWeapon().setReloadCompleted(-1);
        }
    }

    private void reloadWeaponWithSingleBullets(boolean broken){
        if (!broken) {
            int newBullets = getActualWeapon().getMaxMagazineCapacity() - getActualWeapon().getRestBullets();
            int inBag = playerBag.getRestAmmoForWeapon(getActualWeapon().getWeaponType());
            System.out.print("Player has: " + inBag + " bullets");
            System.out.println(" and needs " + newBullets);
            if (inBag < newBullets) newBullets = inBag;
            for (int i = 0; i < newBullets; i++) {
                playerBag.getOneAmmo(getActualWeapon().getWeaponType());
            }
            getActualWeapon().setReloadCompleted(newBullets);
        }
        else {
            PlayerShotgunAnimationController controller =  (PlayerShotgunAnimationController) animationController;
            int alreadyAdded = controller.getBulletsWasAlreadyAdded();
            for (int i = 0; i < alreadyAdded; i++) {
                playerBag.getOneAmmo(getActualWeapon().getWeaponType());
            }
            getActualWeapon().setReloadCompleted(alreadyAdded);
            controller.resetBulletsWasAlreadyAdded();
        }
    }

    public static float getNormalBodyWidth() {
        //HumanBodyData humanBodyData = new HumanBodyData(Soldier.class);
        return 25f / 0.7f;
    }

    public void recoveryGrenade() {
        getActualWeapon().setMagazineCapacity(getActualWeapon().getMaxMagazineCapacity());
    }

    public void updateOrientationByWeaponAngle() {
        if (weaponAngle > 90 && weaponAngle < 270) {
            orientation = TO_LEFT;
            sightDirection = TO_LEFT;
        }
        else {
            orientation = TO_RIGHT;
            sightDirection = TO_RIGHT;
        }
    }
    protected class HumanBodyData{
        private int NORMAL_WIDTH = 120;
        private int NORMAL_HEIGHT = 70;
        public float bodyWidth = 25f / 0.7f;
        private float bodyHeight = 110;


        private float headRadius = 10;
        private float deltaY = (-bodyHeight / 2) + headRadius;

        protected HumanBodyData(Class human){
            if (human == Soldier.class){
                bodyWidth = Soldier.getNormalBodyWidth();
                deltaY = 4.5f*headRadius;
            }
            else {
                bodyWidth = Mercenary.getNormalBodyWidth();
                deltaY = 4.5f*headRadius;
            }

        }



        public int getNORMAL_WIDTH() {
            return NORMAL_WIDTH;
        }

        public int getNORMAL_HEIGHT() {
            return NORMAL_HEIGHT;
        }

        public float getBodyWidth() {
            return bodyWidth;
        }

        public float getBodyHeight() {
            return bodyHeight;
        }

        public float getHeadRadius() {
            return headRadius;
        }

        public float getDeltaY() {
            return deltaY;
        }





    }
}
