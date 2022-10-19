package com.mgdsstudio.blueberet.gamecontrollers;



import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.splashes.BloodSplash;
import com.mgdsstudio.blueberet.graphic.splashes.DustSplash;
import com.mgdsstudio.blueberet.graphic.splashes.Splash;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class HittingController {
    private static SpriteAnimation crushingAnimation;
    private static ImageZoneSimpleData imageZoneCrushing;
    //private static IndependentOnScreenAnimation independentOnScreenCrushingAnimation;
    private final float NORMAL_DIMENTION_COEF_FOR_GRAPHIC = 3.5f;
    //private final String PATH_TO_SPRITE_ANIMATION = Program.getAbsolutePathToAssetsFolder("SpaceTileset.png");
    //private final String PATH_TO_SPRITE_ANIMATION = Programm.getRelativePathToAssetsFolder()+"SpaceTileset.png";
    private boolean graphicLoaded;

    private final int MINIMAL_ATTACKING_VELOCITY = 17;    //pxls pro sec
    private final float MINIMAL_MASS_DIFFERENCE = 0.2f;    //pxls pro sec
    private final int MINIMAL_ATTACK_VALUE = 2;
    private final float attackingVelocityCoefficientByColidingWithRoundElement = 1.1f;
    private final float attackingDimensionCoefficientByColidingWithRoundElement = 5.1f;
    private final float MAX_ATTACK_VALUE_FOR_JUMP = 90;

    //attack types
    public final static boolean ATTACK_FROM_WEAPON = true;
    public final static boolean ATTACK_FROM_EXPLOSION = false;
    private LandingController landingController;
    private final static float CRITICAL_Y_ACCELERATE_FOR_ATTACK = 50.01f;
    private final float ANGULAR_VELOCITY_AFTER_CRUSH_COEFFICIENT = 0.5f;
    private final boolean ATTACK_FROM_LEFT = true;
    private static final boolean ATTACK_FROM_RIGHT = false;
    final float attackRadiusByFalling = 100;
    //Mutable
    private Body mutB1, mutB2;
    private final static boolean WITH_BLOOD_SPLASHES = false;
    private Vec2 mutableImpulseInPixels = new Vec2();
    //private final Vec2 mutableImpulseInWorld = new Vec2();
    private final boolean WITH_ATTACK_BY_CONTACT_WITH_SOME_PERSON = false;

    public HittingController(GameRound gameRound){
        init(gameRound);
    }

    private void init(GameRound gameRound){
        if (!graphicLoaded) {
            imageZoneCrushing = new ImageZoneSimpleData(1024-32, 512-32*10, 1024, 512);
            crushingAnimation = new SpriteAnimation(HeadsUpDisplay.mainGraphicSource.getPath(), (int) imageZoneCrushing.leftX, (int) (imageZoneCrushing.upperY), (int) (imageZoneCrushing.rightX), (int) (imageZoneCrushing.lowerY), (int)(50*NORMAL_DIMENTION_COEF_FOR_GRAPHIC) , (int) (50*NORMAL_DIMENTION_COEF_FOR_GRAPHIC), (byte)10, (byte)1, (int) SpriteAnimation.NORMAL_ANIMATION_UPDATING_FREQUENCY);
            //crushingAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(crushingAnimation.getPath()));
            crushingAnimation.loadAnimation(HeadsUpDisplay.mainGraphicTileset);
            System.out.println("Graphic for crushing is loaded");
            graphicLoaded = true;
        }
        landingController = new LandingController(gameRound.getPlayer(), LandingController.CRITICAL_DELTA_ACCELERATE_FOR_ATTACK);
    }

    public void update(GameRound gameRound){
        if (!gameRound.isPlayerLoosed()) {
            updateAttackByFalling(gameRound);
            updateBulletsHitting(gameRound);
        }

        updateCollidingWithFlyingElementsHitting(gameRound);
        updateCollidingWithAnotherPersons(gameRound);
        if (gameRound.launchableWhizbangsController == null) System.out.println("Controller is null");
        if (!gameRound.isPlayerLoosed()) {
            updatePlayerHitting(gameRound);
        }
        if (gameRound.launchableWhizbangsController!= null) gameRound.launchableWhizbangsController.updateWhizbangsCoalisions(gameRound, this);


        //PhysicGameWorld.clearBeginContacts();
        //PhysicGameWorld.clearEndContacts();
    }

    public void endUpdating(GameRound gameRound){

        PhysicGameWorld.clearBeginContacts();
        PhysicGameWorld.clearEndContacts();
    }

    private void updateAttackByFalling(GameRound gameRound) {
        landingController.update(gameRound);
        if (landingController.isFallingOnEnemy()){
            landingController.makeReboundJump();
            Person attackedPerson = landingController.getAttackedPerson();
            Fixture attackedFixture = landingController.getAttackedFixture();
            if (attackedFixture != null) {
                if (attackedPerson != null) {
                    if (attackedPerson.canPersonBeAttackedByJump()) {
                        hitPersonByJump(gameRound, attackedPerson, landingController.getAttackValueInPercent());
                    }
                }
            }
        }
        else if (landingController.isFallingOnObjectStaticObject()){
            System.out.println("Player falls on an object");
            ArrayList <GameObject> attackedObjects = determineThrownUpBodies(gameRound);
            applyThrowImpulseToBodies(gameRound, attackedObjects);
        }
    }

    private void applyThrowImpulseToBodies(GameRound gameRound, ArrayList <GameObject> attackedObjects) {
        final float maxAttackImpulse = 300;
        float xImpulse = 20;
        final float minAttackImpulse = 0;
        for (GameObject gameObject : attackedObjects){
            if (!gameRound.getPlayer().equals(gameObject)){
                float xDistanceToPlayer = PApplet.abs(gameObject.getPixelPosition().x-gameRound.getPlayer().getPixelPosition().x);
                if (xDistanceToPlayer<attackRadiusByFalling){
                    float relativeDistance = xDistanceToPlayer/attackRadiusByFalling;
                    float jumpImpulseValue = relativeDistance*(maxAttackImpulse-minAttackImpulse);
                    if (gameObject.body.getPosition().x<gameRound.getPlayer().body.getPosition().x) xImpulse*=(-1);
                    Vec2 jumpImulse = new Vec2(xImpulse, PhysicGameWorld.controller.scalarPixelsToWorld(jumpImpulseValue));
                    gameObject.body.applyLinearImpulse(jumpImulse, gameObject.body.getPosition(), true);
                    System.out.println("X dist: " + xDistanceToPlayer + "; Relative: " + relativeDistance + "; Jump impulse: " + jumpImulse.y);
                }
            }
        }
    }

    private ArrayList <GameObject>  determineThrownUpBodies(GameRound gameRound) {
        Person player = gameRound.getPlayer();
        Vec2 playerWorldPos = player.body.getPosition();
        Vec2 leftLowerCorner = new Vec2(playerWorldPos.x-PhysicGameWorld.controller.scalarPixelsToWorld(attackRadiusByFalling), playerWorldPos.y-PhysicGameWorld.controller.scalarPixelsToWorld(player.getHeight()));
        Vec2 rightUpperCorner = new Vec2(playerWorldPos.x+PhysicGameWorld.controller.scalarPixelsToWorld(attackRadiusByFalling), 1+playerWorldPos.y-PhysicGameWorld.controller.scalarPixelsToWorld(player.getHeight()));
        if (Program.OS == Program.DESKTOP) {
            Vec2 leftLowerPixelPos = PhysicGameWorld.controller.coordWorldToPixels(leftLowerCorner);
            Vec2 rightUpperPixelPos = PhysicGameWorld.controller.coordWorldToPixels(rightUpperCorner);
            DebugGraphic debugGraphic2 = new DebugGraphic(DebugGraphic.CROSS, leftLowerPixelPos);
            DebugGraphic debugGraphic1 = new DebugGraphic(DebugGraphic.CROSS, rightUpperPixelPos);
            gameRound.addDebugGraphic(debugGraphic1);
            gameRound.addDebugGraphic(debugGraphic2);
        }
        AABB aabb = new AABB(leftLowerCorner, rightUpperCorner);
        ArrayList <GameObject> attackedObjects = new ArrayList<>();
        QueryCallback callback = new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                GameObject object  = PhysicGameWorld.getGameObjectByBody(gameRound, fixture.getBody());
                if (object != null) {
                    attackedObjects.add(object);
                }
                else{
                    System.out.println("I can not determine this object ");
                }
                //System.out.println("Object was added to be thrown up " + object.getClass());
                return true;
            }
        };
        if (!PhysicGameWorld.controller.world.isLocked()) {
            PhysicGameWorld.controller.world.queryAABB(callback, aabb);
        }
        else{
            System.out.println("World is locked");
        }
        System.out.println("In zone we have: " + attackedObjects.size() + " objects");
        return attackedObjects;
    }





    private void hitPersonByJump(GameRound gameRound, Person attackedPerson, float percentValue) {
        if (attackedPerson.getClass() != Soldier.class){
            attackedPerson.attacked((int)(MAX_ATTACK_VALUE_FOR_JUMP*percentValue/100f));
            attackedPerson.blockAttackAbility(true);
            System.out.println("Attack value: " + (MAX_ATTACK_VALUE_FOR_JUMP*percentValue/100f));
            if (!attackedPerson.isAlive()){
                gameRound.releaseGameObjects(attackedPerson, 0);
            }
        }
    }

    private void updateCollidingWithAnotherPersons(GameRound gameRound){
        try {
            for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
                Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
                Body b1 = f1.getBody();
                Body b2 = f2.getBody();
                if (b1 != null && b2 != null) {
                    if (b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC) {
                        for (Person person : gameRound.getPersons()) {
                            if (!person.equals(gameRound.getPlayer())) {
                                if (person.body.equals(b1)) {
                                    for (Person somePerson : gameRound.getPersons()) {
                                        if (somePerson.body.equals(b2)) {
                                            //int attackValue = calculateAttackValueByBrickContact(b2, b1);

                                            if (WITH_ATTACK_BY_CONTACT_WITH_SOME_PERSON) flyingElementCollidingWithPerson(gameRound, person, 1);
                                        }
                                    }
                                } else if (person.body.equals(b2)) {
                                    for (Person somePerson : gameRound.getPersons()) {
                                        if (somePerson.body.equals(b1)) {
                                            //int attackValue = calculateAttackValueByBrickContact(b1, b2);

                                            if (WITH_ATTACK_BY_CONTACT_WITH_SOME_PERSON)flyingElementCollidingWithPerson(gameRound, person, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            if (Program.debug ) e.printStackTrace();
        }
    }

    private void updateCollidingWithFlyingElementsHitting(GameRound gameRound) {
        for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
            Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
            Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
            Body b1 = f1.getBody();
            Body b2 = f2.getBody();
            if (b1 != null && b2 != null) {
                if (b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC) {
                    for (Person person : gameRound.getPersons()) {
                        if (!person.equals(gameRound.getPlayer())) {
                            if (person.body.equals(b1)) {
                                for (RoundElement roundElement : gameRound.roundElements) {
                                    if (roundElement.body.equals(b2) && !roundElement.hasSpring()) {
                                        int attackValue = calculateAttackValueByBrickContact(b2, b1);
                                        flyingElementCollidingWithPerson(gameRound, person, attackValue);
                                    }
                                }
                            } else if (person.body.equals(b2)) {
                                for (RoundElement roundElement : gameRound.roundElements) {
                                    if (roundElement.body.equals(b1) && !roundElement.hasSpring()) {
                                        int attackValue = calculateAttackValueByBrickContact(b1, b2);
                                        flyingElementCollidingWithPerson(gameRound, person, attackValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int calculateAttackValueByBrickContact(Body objectBody, Body personBody) {
        int valueFromVelocity = 0;
        int valueFromDimension = 0;
        Vec2 relativeVelocity = new Vec2(objectBody.getLinearVelocity().x, objectBody.getLinearVelocity().y);
        Vec2 secondObjectVelocity = new Vec2(personBody.getLinearVelocity().x, personBody.getLinearVelocity().y);
        relativeVelocity.sub(secondObjectVelocity);
        float velocityAbsoluteValue = relativeVelocity.length();
        float massDiference = objectBody.getMass()/personBody.getMass();
        if (velocityAbsoluteValue > MINIMAL_ATTACKING_VELOCITY && massDiference > MINIMAL_MASS_DIFFERENCE) {
            if (massDiference > MINIMAL_MASS_DIFFERENCE) {
                valueFromDimension = Program.engine.floor((massDiference) * attackingDimensionCoefficientByColidingWithRoundElement * MINIMAL_ATTACK_VALUE);
            }

            if (velocityAbsoluteValue > MINIMAL_ATTACKING_VELOCITY) {
                valueFromVelocity = Program.engine.floor((velocityAbsoluteValue - MINIMAL_ATTACKING_VELOCITY) * attackingVelocityCoefficientByColidingWithRoundElement * MINIMAL_ATTACK_VALUE);
            }
        }
        return valueFromVelocity+valueFromDimension;
    }


    private void bulletColidingWithPerson(GameRound gameRound, Person person, Bullet bullet, Fixture attackedFixture) {
        if (person.canFixtureBeAttacked(attackedFixture, bullet)) {
            person.attacked(bullet);
            if (!person.isAlive()) {
                gameRound.getSoundController().setAndPlayAudio(SoundsInGame.ENEMY_HITTED);
                gameRound.releaseGameObjects(person, bullet.getLastAngleInDegrees());
            } else gameRound.getSoundController().setAndPlayAudio(SoundsInGame.ENEMY_HITTED_2);
        }
    }

    private void flyingElementCollidingWithPerson(GameRound gameRound, Person person, int attackValue){
        if (person.canBeAttackedByFlyingObject()) {
            if (attackValue > 0) person.attacked(attackValue);
            else if (attackValue == 0) person.contactWithMoveableObject();
            if (!person.isAlive()) {
                gameRound.releaseGameObjects(person, 0);
                //System.out.println(person + " is colliding with round element");
            }
        }

    }

    private void explosionCollidingWithPerson(GameRound gameRound, Person person, float angle) {
        //person.attacked(bullet);
        if (!person.isAlive()){
            gameRound.releaseGameObjects(person, angle);
        }
    }

    public void applyExplosionsDamages(GameRound gameRound, ArrayList<SingleColliding> singleCollidings, int maxImpuls, int maxDamageValue) {
        if (singleCollidings.size()>0) {
            System.out.println("Colisions: " + singleCollidings.size());
            boolean impulseAplied = false;
            for (int j = 0; j < singleCollidings.size(); j ++) {
                    float explosionRayAngle = singleCollidings.get(j).getAngle();		// to test
                    PVector vec = PVector.fromAngle(Program.engine.radians(explosionRayAngle));
                    //Vec2 impulse = new Vec2(vec.x, vec.y);
                     mutableImpulseInPixels.x = vec.x;
                     mutableImpulseInPixels.y = vec.y;
                    mutableImpulseInPixels.x*=100*maxImpuls/singleCollidings.get(j).getRelativeDistanceFromStart();
                    mutableImpulseInPixels.y*=100*maxImpuls/singleCollidings.get(j).getRelativeDistanceFromStart();
                    mutableImpulseInPixels = PhysicGameWorld.controller.vectorPixelsToWorld(mutableImpulseInPixels);
                    float maxRelDistForBeenAttacked = 70;
                    int damageValue = 0;
                    if (singleCollidings.get(j).getRelativeDistanceFromStart() < maxRelDistForBeenAttacked) damageValue = Program.engine.ceil(100*maxDamageValue/singleCollidings.get(j).getRelativeDistanceFromStart());

                    Vec2 impulsePosition = PhysicGameWorld.controller.vectorPixelsToWorld(new Vec2(singleCollidings.get(j).getCollidingPlace().x, singleCollidings.get(j).getCollidingPlace().y));
                    for (Person person : gameRound.getPersons()) {
                        if (person.body.equals(singleCollidings.get(j).getFixture().getBody())) {
                            if (person.getClass() == Koopa.class && person.level == Person.FLYING) {
                                    person.level = Person.JUMPING;    // Wahrscheinlich muss geloescht werden
                                    person.attacked(9999);
                                    if (!person.isAlive()) gameRound.releaseGameObjects(person, explosionRayAngle);
                                    person.globalAIController.changeBehaviourModel(GlobalAI_Controller.GO_AND_REGULARLY_JUMP);
                                    singleCollidings.get(j).getFixture().getBody().setAngularVelocity(0);
                                    singleCollidings.get(j).getFixture().getBody().applyLinearImpulse(mutableImpulseInPixels, impulsePosition, true);

                            }
                            else {
                                if (person.canBeAttackedThroughtExplosion(singleCollidings.get(j).getFixture())) {
                                    person.attacked(damageValue);
                                    if (person.getClass() == Soldier.class && person.isDead()) {
                                        addFallingWeaponAndBeret(gameRound);
                                        gameRound.addGameOverScreen();
                                    }
                                    try {
                                        if (damageValue > 0) {
                                            explosionCollidingWithPerson(gameRound, person, explosionRayAngle);
                                            singleCollidings.get(j).getFixture().getBody().setAngularVelocity(0);
                                        }
                                        //singleCollidings.get(j).getFixture().getBody().applyLinearImpulse(impulse, impulsePosition, true);
                                    } catch (Exception e) {
                                        System.out.println("Person " + person.getClass() + " can not be transfered through explosion");
                                        System.out.println(e);
                                    } finally {
                                        impulseAplied = false;
                                        //was impulseAplied = true;
                                    }
                                    //person.body.applyLinearImpulse(impulse, impulsePosition, true);
                                    if (!person.isAlive()){
                                        float angle = person.body.getAngle();
                                        if (singleCollidings.get(j).getCollidingPlace().x > person.getPixelPosition().x) {
                                            System.out.println("Explosion is right from the player " + singleCollidings.get(j).getCollidingPlace().x);
                                            angle += 0.2f;
                                        } else {
                                            System.out.println("Explosion is left from the player " + singleCollidings.get(j).getCollidingPlace().x);
                                            angle -= 0.2f;
                                        }
                                        if (person instanceof Human) {
                                            person.body.setTransform(person.body.getPosition(), angle);
                                            System.out.println("Human has after the explosion angle: " + PApplet.degrees(angle));
                                        }
                                        System.out.println("Soldier was hit through the explosion " + damageValue + "; Impulse: " + mutableImpulseInPixels);
                                    }
                                    else {
                                        singleCollidings.get(j).getFixture().getBody().applyLinearImpulse(mutableImpulseInPixels, impulsePosition, true);
                                    }
                                }
                                else impulseAplied = true;
                            }
                        }
                    }
                    if (!impulseAplied) {
                        for (int i = (gameRound.collectableObjectsController.getObjectsNumber()-1); i >= 0; i--) {
                            if (gameRound.collectableObjectsController.getCollectableObjects().get(i).body.equals(singleCollidings.get(j).getFixture().getBody())){
                                float angle = singleCollidings.get(j).getAngle();
                                float relativeDistance = singleCollidings.get(j).getRelativeDistanceFromStart();
                                System.out.println("Collectable object is in attack zone of the explosion under angle: " + angle + " and value: " + relativeDistance);
                                gameRound.collectableObjectsController.attackFromExplosion(i, angle, relativeDistance);
                            }
                        }
                    }
                    if (!impulseAplied){
                        for (int i = (gameRound.getLaunchableWhizbangsController().getWhizbangsNumber()-1); i >= 0; i--) {
                            if (singleCollidings.get(j).getRelativeDistanceFromStart()<85) {
                                gameRound.getLaunchableWhizbangsController().attackFromExplosion(i);
                            }
                        }
                    }
                    if (!impulseAplied) {
                        Fixture attackedFixture = singleCollidings.get(j).getFixture();
                        if (attackedFixture != null && attackedFixture.getBody() != null) {
                            if (attackedFixture.getBody().getType() != BodyType.KINEMATIC) {
                                attackedFixture.getBody().applyLinearImpulse(mutableImpulseInPixels, impulsePosition, true);
                                if (attackedFixture.getBody().getGravityScale() <= 0.1f) {
                                    attackedFixture.getBody().setGravityScale(1);    // for bullets bill
                                }
                                int bricksNumber = gameRound.roundElements.size();
                                ArrayList<RoundElement> bricksToBeDeleted = new ArrayList<RoundElement>();
                                for (int i = 0; i < bricksNumber; i++) {
                                    if (gameRound.roundElements.get(i).body.equals(singleCollidings.get(j).getFixture().getBody())) {
                                        gameRound.roundElements.get(i).attacked(damageValue);
                                        System.out.println("Brick is attacked by value: " + damageValue);
                                        if (gameRound.roundElements.get(i).isDead()) {
                                            if (gameRound.roundElements.get(i).hasSpring())
                                                gameRound.roundElements.get(i).deleteSpring();
                                            bricksToBeDeleted.add(gameRound.roundElements.get(i));
                                        }
                                    }
                                }
                                for (RoundElement brickToBeDeleted : bricksToBeDeleted) {
                                    for (int k = 0; k < gameRound.roundElements.size(); k++) {
                                        if (gameRound.roundElements.get(k).equals(brickToBeDeleted)) {
                                            gameRound.roundElements.get(k).killBody();
                                            float angle = PApplet.degrees(singleCollidings.get(j).getAngle());
                                            gameRound.releaseGameObjects(gameRound.roundElements.get(k), PApplet.radians(angle));
                                            crushBrickInSegments(gameRound, gameRound.roundElements.get(k), new Vec2(singleCollidings.get(j).getCollidingPlace().x, singleCollidings.get(j).getCollidingPlace().y), angle, ATTACK_FROM_EXPLOSION);
                                        }
                                    }
                                }
                                bricksToBeDeleted.clear();
                            }
                        }
                    }
            }
        }
    }



    /*
    private void playerContactWithAnotherPerson(Soldier player, GameRound gameRound, Person person){
        System.out.println("Contact player with " + person.getClass() + " fall 1");
        if (person.attackByDirectContact(player) && !person.isAttackAbilityBlocked() && person.isAlive()){
            if (player.isAlive()) {
                player.attacked(person.getAttackValue());
                player.setControlBlocked(true);
                player.addJumpAfterAttack(person);
                if (player.isDead()) {
                    addFallingWeaponAndBeret( gameRound);
                    gameRound.addGameOverScreen();

                    //break;
                }
            player.stopBlinking();
            }
        }
        else if (person.isAttackAbilityBlocked()) System.out.println(person.getClass() + " can not attack now; It is under attack blocking");
    }*/

    private void addFallingWeaponAndBeret(GameRound gameRound){
        if (!gameRound.isGameOverScreenShown()) {
            System.out.println("Falling beret animation was added");
            float directionCoef = -1f;
            if (gameRound.getPlayer().getSightDirection()) directionCoef=1f;
            addFallingBeret(gameRound, directionCoef);
            directionCoef*=(-1);
            addFallingWeapon(gameRound, directionCoef);
        }
    }

    private void addFallingWeapon(GameRound gameRound, float directionCoef){
        ImageZoneSimpleData beretImageZoneSimpleData = new ImageZoneSimpleData(94, 1009, 131, 1024);
        Soldier player = (Soldier) gameRound.getPlayer();
        String path = player.getPersonAnimationController().getAnimationsList().get(0).getPath();
        int weaponCode = Weapon.getWeaponCodeForType(player.getActualWeapon().getWeaponType());
        int onImageWidth = beretImageZoneSimpleData.rightX - beretImageZoneSimpleData.leftX;
        int onImageHeight = beretImageZoneSimpleData.lowerY - beretImageZoneSimpleData.upperY;
        final int weaponWidth = (int) (gameRound.getPlayer().getHeight());
        float coef = weaponWidth / onImageWidth;
        final int weaponHeight = (int) (coef * onImageHeight);
        System.out.println("Path to spritesheet: " + path + " weapon code " + weaponCode);
        HumanAnimationController playerAnimationController = (HumanAnimationController) gameRound.getPlayer().getPersonAnimationController();
        Tileset tileset = playerAnimationController.getTilesetForActualGraphic();
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x, gameRound.getPlayer().getPixelPosition().y);
        if (gameRound.existEndedMoveableSpritesForType(weaponCode)){
            gameRound.getEndedSpriteForType(weaponCode).recreate(position, 0f, 35*directionCoef, -85, 0, 345, directionCoef*350);
            System.out.println("Graphic was recreated from pool for type " + weaponCode);
        }
        else {
            StaticSprite staticSprite = new StaticSprite(path, beretImageZoneSimpleData.leftX, beretImageZoneSimpleData.upperY, beretImageZoneSimpleData.rightX, beretImageZoneSimpleData.lowerY, weaponWidth, weaponHeight);
            IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, position, 0f, 35 * directionCoef, -85, 0, 345, directionCoef * 350, 3000, weaponCode);
            movableSprite.getStaticSprite().loadSprite(tileset);
            gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
        }

    }

    private void addFallingWeaponAsAnimation(GameRound gameRound, float directionCoef){
        ImageZoneSimpleData beretImageZoneSimpleData = new ImageZoneSimpleData(94, 1009, 131, 1024);
        Soldier player = (Soldier) gameRound.getPlayer();
        String path = player.getPersonAnimationController().getAnimationsList().get(0).getPath();
        System.out.println("Path to spritesheet: " + path);
        int onImageWidth = beretImageZoneSimpleData.rightX - beretImageZoneSimpleData.leftX;
        int onImageHeight = beretImageZoneSimpleData.lowerY - beretImageZoneSimpleData.upperY;
        final int beretWidth = (int) (gameRound.getPlayer().getHeight());
        float coef = beretWidth / onImageWidth;
        final int beretHeight = (int) (coef * onImageHeight);
        SpriteAnimation weaponAnimation = new SpriteAnimation(path, (int) beretImageZoneSimpleData.leftX, (int) beretImageZoneSimpleData.upperY, (int) beretImageZoneSimpleData.rightX, (int) beretImageZoneSimpleData.lowerY, beretWidth, beretHeight, (byte) 1, (byte) 1, (int) 1);
        weaponAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(path));
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x, gameRound.getPlayer().getPixelPosition().y);
        MovableOnScreenAnimation movableOnScreenAnimation = new MovableOnScreenAnimation(weaponAnimation, position, 0f, 35*directionCoef, -85, 0, 345, directionCoef*350, 3000);
        gameRound.addNewIndependentOnScreenAnimation(movableOnScreenAnimation);
    }

    private void addFallingBeret(GameRound gameRound, float directionCoef){
        ImageZoneSimpleData beretImageZoneSimpleData = InWorldObjectsGraphicData.beretImageZoneSimpleData;
        String path = HumanAnimationController.getPathToActualSpritesheet(gameRound.getPlayer().getActualWeapon().getWeaponType());
        int onImageWidth = beretImageZoneSimpleData.rightX - beretImageZoneSimpleData.leftX;
        int onImageHeight = beretImageZoneSimpleData.lowerY - beretImageZoneSimpleData.upperY;
        final int beretWidth = gameRound.getPlayer().getPersonWidth();
        float coef = beretWidth / onImageWidth;
        final int beretHeight = (int) (coef * onImageHeight);
        HumanAnimationController playerAnimationController = (HumanAnimationController) gameRound.getPlayer().getPersonAnimationController();
        Tileset tileset = playerAnimationController.getTilesetForActualGraphic();
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x, gameRound.getPlayer().getPixelPosition().y-gameRound.getPlayer().getHeight()/2f);
        StaticSprite staticSprite = new StaticSprite(path, beretImageZoneSimpleData.leftX, beretImageZoneSimpleData.upperY, beretImageZoneSimpleData.rightX, beretImageZoneSimpleData.lowerY, beretWidth, beretHeight);
        IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, position, 0f, 35*directionCoef, -135, 0, 245, directionCoef*350, 3000, MoveableSpritesAddingController.BERET);
        movableSprite.getStaticSprite().loadSprite(tileset);
        gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
    }

    /*
    private void addFallingBeretAsAnimation(GameRound gameRound, float directionCoef){
        ImageZoneSimpleData beretImageZoneSimpleData = InWorldObjectsGraphicData.beretImageZoneSimpleData;
        String path = PlayerAnimationController.getPathToActualSpritesheet(gameRound.getPlayer().getActualWeapon().getWeaponType());
        int onImageWidth = beretImageZoneSimpleData.rightX - beretImageZoneSimpleData.leftX;
        int onImageHeight = beretImageZoneSimpleData.lowerY - beretImageZoneSimpleData.upperY;
        final int beretWidth = gameRound.getPlayer().getPersonWidth();
        float coef = beretWidth / onImageWidth;
        final int beretHeight = (int) (coef * onImageHeight);
        PlayerAnimationController playerAnimationController = (PlayerAnimationController) gameRound.getPlayer().getPersonAnimationController();
        Tileset tileset = playerAnimationController.getTilesetForActualGraphic();
        System.out.println("Path to actual spritesheet: " + path + "; Tileset is null: " + (tileset == null)) ;
        SpriteAnimation beretAnimation = new SpriteAnimation(path, (int) beretImageZoneSimpleData.leftX, (int) beretImageZoneSimpleData.upperY, (int) beretImageZoneSimpleData.rightX, (int) beretImageZoneSimpleData.lowerY, beretWidth, beretHeight, (byte) 1, (byte) 1, (int) 1);
        beretAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(path));
        beretAnimation.loadAnimation(tileset);
        Vec2 position = new Vec2(gameRound.getPlayer().getPixelPosition().x, gameRound.getPlayer().getPixelPosition().y-gameRound.getPlayer().getHeight()/2f);
        MovableOnScreenAnimation movableOnScreenAnimation = new MovableOnScreenAnimation(beretAnimation, position, 0f, 35*directionCoef, -135, 0, 245, directionCoef*350, 3000);
        gameRound.addNewIndependentOnScreenAnimation(movableOnScreenAnimation);
    }*/


    public void attackPlayerByLuggage(GameRound gameRound, float angle, Lizard lizard) {
        attackPlayer((Soldier) gameRound.getPlayer(), gameRound, lizard);
    }
    public void attackPlayer(Soldier player, GameRound gameRound, GameObject gameObject){
        int playerStartLife = player.getLife();

        int attackValue = 1;
        if (gameObject instanceof Person){
            Person person = (Person) gameObject;
            attackValue = person.getAttackValue();
            player.attacked(attackValue);
        }
        else if (gameObject instanceof LaunchableWhizbang){
            LaunchableWhizbang whizbang = (LaunchableWhizbang) gameObject;
            attackValue = whizbang.getAttackValue();
            player.attacked(whizbang.getAttackValue());
        }
        else player.attacked(attackValue);
        player.setControlBlocked(true);
        player.addJumpAfterAttack(gameObject);
        if (player.isDead()){
            addFallingWeaponAndBeret(gameRound);
            gameRound.addGameOverScreen();
            Vec2 bodyCenterPos = player.body.getPosition();
            boolean attackSide = getAttackSide(gameObject, player);
            float xImpulsValue = 15;
            if (attackSide == ATTACK_FROM_RIGHT) xImpulsValue*=(-1);
            Vec2 attackVector = PhysicGameWorld.controller.vectorPixelsToWorld(new PVector( xImpulsValue,5));
            player.body.applyLinearImpulse(attackVector, bodyCenterPos, true);
        }
        player.stopBlinking();
        System.out.println("Player was attacked by " + gameObject.getClass());
        gameRound.playerChangedHisLifeValue(playerStartLife, player.getLife(), player.getMaxLife());
    }

    private boolean getAttackSide(GameObject attackingObject, Person attackedPerson){
        float xAttacking = attackingObject.body.getPosition().x;
        float xAttacked = attackedPerson.body.getPosition().x;
        if (xAttacking > xAttacked) return ATTACK_FROM_RIGHT;
        else return ATTACK_FROM_LEFT;
    }

    private void updatePlayerHitting(GameRound gameRound){
        for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
            Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
            Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureB();
            Body b1 = f1.getBody();
            Body b2 = f2.getBody();
            Soldier player = (Soldier) gameRound.getPlayer();
            //boolean playerDead = false;
            if (b1!=null && b2 != null) {
                if (b1.equals(player.body)) {
                    for (Person person : gameRound.getPersons()) {
                        if (!person.equals(player) && person.body.equals(b2)) {
                            System.out.println("Contact player with " + person.getClass() + " fall 1");
                            person.setContactWithPlayer(gameRound.getPlayer());
                            if (person.attackByDirectContact(player) && !person.isAttackAbilityBlocked() && person.isAlive()) {
                                attackPlayer(player, gameRound, person);
                                if (player.isDead()) {
                                    addFallingWeaponAndBeret(gameRound);
                                    break;
                                }
                            } else if (person.isAttackAbilityBlocked())
                                System.out.println(person.getClass() + " can not attack now; It is under attack blocking");
                        }
                    }
                    if (player.isAlive()) {
                        for (LaunchableWhizbang whizbang : gameRound.getLaunchableWhizbangsController().whizbangs) {
                            if (whizbang.body.equals(b2)) {
                                if (whizbang.getClass() == DragonFire.class) {
                                    attackPlayer(player, gameRound, whizbang);
                                    if (player.isDead()) {
                                        addFallingWeaponAndBeret(gameRound);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (b2.equals(player.body)) {
                    for (Person person : gameRound.getPersons()) {
                        if (!person.equals(player) && person.body.equals(b1)) {
                            System.out.println("Contact player with " + person.getClass() + " fall 2");
                            person.setContactWithPlayer(gameRound.getPlayer());
                            if (person.attackByDirectContact(player) && !person.isAttackAbilityBlocked() && person.isAlive()) {
                                attackPlayer(player, gameRound, person);
                                if (player.isDead()) {
                                    addFallingWeaponAndBeret(gameRound);
                                    break;
                                }
                            } else if (person.isAttackAbilityBlocked())
                                System.out.println(person.getClass() + " can not attack now; It is under attack blocking");

                        }
                    }
                    if (player.isAlive()) {
                        for (LaunchableWhizbang whizbang : gameRound.getLaunchableWhizbangsController().whizbangs) {
                            if (whizbang.body.equals(b1)) {
                                if (whizbang.getClass() == DragonFire.class) {
                                    attackPlayer(player, gameRound, whizbang);
                                    if (player.isDead()) {
                                        addFallingWeaponAndBeret(gameRound);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateBulletsHitting(GameRound gameRound) {
        for (int m = (PhysicGameWorld.beginContacts.size() - 1); m >= 0; m--) {
            try {
                if (PhysicGameWorld.beginContacts.get(m) != null && PhysicGameWorld.beginContacts.get(m) != null) {
                    mutB1 = PhysicGameWorld.beginContacts.get(m).getFixtureA().getBody();
                    mutB2 = PhysicGameWorld.beginContacts.get(m).getFixtureB().getBody();
                    for (int i = 0; i < gameRound.bullets.size(); i++) {    // from 1 but not from 0!!! To test!
                        if (!gameRound.bullets.get(i).isSleeped()) {
                            if (gameRound.bullets.get(i).body == mutB1) {//
                                boolean bulletWasDeleted = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (!bulletWasDeleted && person.body == mutB2) {
                                        bulletColidingWithPerson(gameRound, person, gameRound.bullets.get(i), PhysicGameWorld.beginContacts.get(m).getFixtureB());
                                        bulletWasDeleted = true;
                                        if (Program.debug) System.out.println("Enemy was hitted 1");
                                        int bulletAngle = gameRound.bullets.get(i).getStartAngle();
                                        float bodyRadius = person.getPersonWidth() * 2;
                                        Vec2 relativePos = new Vec2(bodyRadius * PApplet.cos(PApplet.radians(bulletAngle)), bodyRadius * PApplet.sin(PApplet.radians(bulletAngle)));
                                        relativePos = relativePos.negate();
                                        if (WITH_BLOOD_SPLASHES) {
                                            BloodSplash bloodSplash = new BloodSplash(person.body, new Vec2(relativePos), bulletAngle, Splash.DYNAMIC);
                                            gameRound.addSplash(bloodSplash);
                                        }

                                    }
                                }
                                if (!bulletWasDeleted) {
                                    if (gameRound.collectableObjectsController.getObjectsNumber() > 0) {
                                        for (int n = (gameRound.collectableObjectsController.getObjectsNumber() - 1); n >= 0; n--) {
                                            if (!bulletWasDeleted && gameRound.collectableObjectsController.getCollectableObjects().get(n).body.equals(mutB2)) {
                                                gameRound.collectableObjectsController.coalisionWithBullet(gameRound, n, gameRound.bullets.get(i));
                                                bulletWasDeleted = true;
                                                if (Program.debug)
                                                    System.out.println("Collectable object was hitted 1");
                                            }
                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    addSplashByAngle(gameRound.bullets.get(i), mutB2, gameRound);    //was so    addSplash((byte) 0, gameRound.bullets.get(i),b1, gameRound);
                                    for (RoundPipe roundPipe : gameRound.getRoundPipes()) {
                                        if (roundPipe.hasFlower()) {

                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    for (int j = 0; j < gameRound.roundElements.size(); j++) {
                                        if (!bulletWasDeleted && gameRound.roundElements.get(j).body == mutB2 && !gameRound.roundElements.get(j).isImmortal()) {
                                            gameRound.roundElements.get(j).attacked(gameRound.bullets.get(i));
                                            if (gameRound.roundElements.get(j).isDead()) {
                                                if (gameRound.roundElements.get(j).hasSpring())
                                                    gameRound.roundElements.get(j).deleteSpring();
                                                gameRound.roundElements.get(j).killBody();
                                                //PVector shootingVector = new PVector(b1.getLinearVelocity().x, b1.getLinearVelocity().y);
                                                //float shootingAngle = PApplet.degrees(shootingVector.heading());
                                                float shootingAngle = gameRound.bullets.get(i).getLastAngleInDegrees();
                                                crushBrickInSegments(gameRound, gameRound.roundElements.get(j), PhysicGameWorld.controller.getBodyPixelCoord(gameRound.bullets.get(i).body), shootingAngle, ATTACK_FROM_WEAPON);
                                            }
                                            else gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WALL_HITTED);
                                            bulletWasDeleted = true;
                                            if (Program.debug) System.out.println("Round element was hitted 1");
                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    for (int n = 0; n < gameRound.launchableWhizbangsController.getWhizbangsNumber(); n++) {
                                        if (!bulletWasDeleted && gameRound.launchableWhizbangsController.getBody(n).equals(mutB2)) {
                                            Fixture f2 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                                            gameRound.launchableWhizbangsController.coalisionWithBullet(gameRound, gameRound.launchableWhizbangsController.getWhizbang(n), f2, gameRound.bullets.get(i));
                                            bulletWasDeleted = true;
                                            if (Program.debug) System.out.println("Whizbang was hitted 1");
                                        }
                                    }
                                }

                                //if (Game2D.DEBUG) System.out.println("No another coalisions");
                                if (!bulletWasDeleted) {
                                    boolean coalisionWithAnotherBullet = false;
                                    for (int k = 0; k < gameRound.bullets.size(); k++) {
                                        if (gameRound.bullets.get(k).body.equals(mutB2)) {
                                            coalisionWithAnotherBullet = true;
                                            if (Program.debug) System.out.println("Another bullet was hitted 1");
                                        }
                                    }
                                    if (!coalisionWithAnotherBullet) {
                                        bulletWasDeleted = true;
                                        System.out.println("Bullet was succesfully deleted");
                                    }
                                }
                                if (bulletWasDeleted) {
                                    try {
                                        if (gameRound.isUsingBulletsPool()) {
                                            gameRound.bullets.get(i).setActive(false);
                                        } else {
                                            mutB1.setActive(false);
                                            PhysicGameWorld.controller.destroyBody(mutB1);
                                            gameRound.bullets.remove(i);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("***Can not delete bullet or bullet body №" + i + "Bullet body b1");
                                        System.out.println(e);
                                    }
                                }
                            } else if (gameRound.bullets.get(i).body == mutB2) {
                                boolean bulletWasDeleted = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (!bulletWasDeleted && person.body == mutB1) {
                                        if (Program.debug) System.out.println("Enemy was hitted 2");
                                        bulletColidingWithPerson(gameRound, person, gameRound.bullets.get(i), PhysicGameWorld.beginContacts.get(m).getFixtureA());
                                        bulletWasDeleted = true;
                                        if (WITH_BLOOD_SPLASHES) {
                                            int bulletAngle = gameRound.bullets.get(i).getStartAngle();
                                            float bodyRadius = person.getPersonWidth() * 2;

                                            Vec2 relativePos = new Vec2(bodyRadius * PApplet.cos(PApplet.radians(bulletAngle)), bodyRadius * PApplet.sin(PApplet.radians(bulletAngle)));
                                            relativePos = relativePos.negate();
                                            BloodSplash bloodSplash = new BloodSplash(person.body, new Vec2(relativePos), bulletAngle, Splash.DYNAMIC);
                                            gameRound.addSplash(bloodSplash);
                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    if (gameRound.collectableObjectsController.getObjectsNumber() > 0) {
                                        for (int n = (gameRound.collectableObjectsController.getObjectsNumber() - 1); n >= 0; n--) {
                                            if (!bulletWasDeleted && gameRound.collectableObjectsController.getCollectableObjects().get(n).body.equals(mutB1)) {
                                                gameRound.collectableObjectsController.coalisionWithBullet(gameRound, n, gameRound.bullets.get(i));
                                                bulletWasDeleted = true;
                                                if (Program.debug) System.out.println("Collectable object was hitted 2");
                                            }
                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    addSplashByAngle(gameRound.bullets.get(i), mutB1, gameRound);

                                }
                                if (!bulletWasDeleted) {
                                    for (int j = 0; j < gameRound.roundElements.size(); j++) {
                                        if (!gameRound.roundElements.get(j).isImmortal()) {
                                            if (!bulletWasDeleted && gameRound.roundElements.get(j).body == mutB1) {
                                                if (gameRound.bullets.get(i).framesAfterShot() == 0)
                                                    gameRound.bullets.get(i).reduceMassAndVelocity();
                                                gameRound.roundElements.get(j).attacked(gameRound.bullets.get(i));
                                                if (gameRound.roundElements.get(j).isDead()) {
                                                    if (gameRound.roundElements.get(j).hasSpring())
                                                        gameRound.roundElements.get(j).deleteSpring();
                                                    gameRound.roundElements.get(j).killBody();
                                                    //PVector shootingVector = new PVector(b1.getLinearVelocity().x, b1.getLinearVelocity().y);
                                                    //float shootingAngle = PApplet.degrees(shootingVector.heading());
                                                    float shootingAngle = gameRound.bullets.get(i).getLastAngleInDegrees();
                                                    crushBrickInSegments(gameRound, gameRound.roundElements.get(j), PhysicGameWorld.controller.getBodyPixelCoord(gameRound.bullets.get(i).body), shootingAngle, ATTACK_FROM_WEAPON);
                                                }
                                                else gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WALL_HITTED);
                                                bulletWasDeleted = true;
                                                if (Program.debug) System.out.println("Round element was hitted 2");
                                            }
                                        } else {
                                            if (gameRound.roundElements.get(j).hasAnyCollectableObjects()) {
                                                float shootingAngle = gameRound.bullets.get(i).getStartAngle();
                                                gameRound.collectableObjectsController.releaseLastCollectableObject(gameRound, gameRound.roundElements.get(j), shootingAngle);

                                            }
                                        }
                                    }
                                }
                                if (!bulletWasDeleted) {
                                    for (int n = 0; n < gameRound.launchableWhizbangsController.getWhizbangsNumber(); n++) {
                                        if (!bulletWasDeleted && gameRound.launchableWhizbangsController.getBody(n).equals(mutB1)) {
                                            Fixture f1 = PhysicGameWorld.beginContacts.get(m).getFixtureA();
                                            gameRound.launchableWhizbangsController.coalisionWithBullet(gameRound, gameRound.launchableWhizbangsController.getWhizbang(n), f1, gameRound.bullets.get(i));
                                            bulletWasDeleted = true;
                                            if (Program.debug) System.out.println("whizbang was hitted 2");
                                        }
                                    }
                                }

                                if (!bulletWasDeleted) {
                                    boolean coalisionWithAnotherBullet = false;
                                    for (int k = 0; k < gameRound.bullets.size(); k++) {
                                        if (gameRound.bullets.get(k).body.equals(mutB1)) {
                                            coalisionWithAnotherBullet = true;
                                            if (Program.debug) System.out.println("Another bullet was hitted 2");
                                        }
                                    }
                                    if (!coalisionWithAnotherBullet) {
                                        if (PhysicGameWorld.getGameObjectByBody(gameRound, mutB1) == null) {

                                        }
                                        bulletWasDeleted = true;
                                        //if (Program.debug) System.out.println("Another object was hitted 2");
                                    }
                                }
                                if (bulletWasDeleted) {
                                    try {
                                        if (gameRound.isUsingBulletsPool()) {
                                            gameRound.bullets.get(i).setActive(false);
                                        } else {
                                            PhysicGameWorld.controller.destroyBody(mutB2);
                                            gameRound.bullets.remove(i);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("***Can not delete bullet or bullet body №" + i + "Bullet body b2");
                                        System.out.println(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("The bullets array is null");
                e.printStackTrace();
            }
        }
    }

    private void addSplashByAngle(Bullet bullet, Body body, GameRound gameRound){
        int bulletAngle = bullet.getStartAngle();
        Vec2 bulletShotPlace = new Vec2(bullet.getShotPosition().x, bullet.getShotPosition().y);
        int step = 2;
        boolean colisionPointFounded = false;
        for (int i = 0; i < 300; i++){
            if (!colisionPointFounded) {
                bulletShotPlace.x+= (step * PApplet.cos(PApplet.radians(bulletAngle)));
                bulletShotPlace.y+= (step * PApplet.sin(PApplet.radians(bulletAngle)));
                try{
                for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
                    if (f.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(bulletShotPlace))) {
                        colisionPointFounded = true;
                        i = 200;
                    }
                }
                }
                catch (Exception e){
                    System.out.println("Collision with launchable whizbang has trouble " + e);
                }

            }
        }
        if (colisionPointFounded){
            gameRound.addDustSplash(body, bulletShotPlace, bulletAngle, Splash.STATIC);
        }

        /*
        Vec2 bulletPos = PhysicGameWorld.controller.getBodyPixelCoord(bullet.body);
        Vec2 bodyPos = PhysicGameWorld.controller.getBodyPixelCoord(body);
        float bodyRadius = PApplet.dist(bulletPos.x, bulletPos.y, bodyPos.x, bodyPos.y);
        System.out.println("distance to center: " + bodyRadius);
        Vec2 relativePos = new Vec2(bodyRadius*PApplet.cos(PApplet.radians(bulletAngle)), bodyRadius*PApplet.sin(PApplet.radians(bulletAngle)));
        relativePos = relativePos.negate();
        //DustSplash splash = new DustSplash(body, bulletPos, bulletAngle);
        DustSplash splash = new DustSplash(body, new Vec2(relativePos), bulletAngle);
        */
        // DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, bulletPos);
        //gameRound.addDebugGraphic(debugGraphic);
    }



    private void loadGraphicForNewCrushing(GameRound gameRound, RoundElement roundDynamicElement){
        System.out.println("Try to create crushing animation");
        float explosionDimension = roundDynamicElement.getWidth();
        if (roundDynamicElement.getWidth()>roundDynamicElement.getHeight()) explosionDimension = roundDynamicElement.getHeight();
        crushingAnimation.setWidth((int)(NORMAL_DIMENTION_COEF_FOR_GRAPHIC*explosionDimension));
        crushingAnimation.setHeight((int)(NORMAL_DIMENTION_COEF_FOR_GRAPHIC*explosionDimension));
        IndependentOnScreenAnimation independentOnScreenAnimation = new IndependentOnScreenAnimation(crushingAnimation, PhysicGameWorld.controller.getBodyPixelCoord(roundDynamicElement.body), 0f);
        independentOnScreenAnimation.setLayer(IndependentOnScreenAnimation.IN_FRONT_OF_ALL);
        independentOnScreenAnimation.setShowOnce(true);
        gameRound.addNewIndependentOnScreenAnimation (independentOnScreenAnimation);
        independentOnScreenAnimation.spriteAnimation.loadAnimation(HeadsUpDisplay.mainGraphicTileset);
        System.out.println("Crushing animation was added");
    }

    public void crushBrickInSegments(GameRound gameRound, RoundElement roundDynamicElement, Vec2 attackPos, float attackAngle, boolean attackType) {
        loadGraphicForNewCrushing(gameRound, roundDynamicElement);
        gameRound.releaseGameObjects(roundDynamicElement, attackAngle);
        if (roundDynamicElement.getClass() == RoundBox.class) {
            if (roundDynamicElement.getMaxLife()>1) {
                gameRound.moveAttackPositionBack(attackPos, attackAngle);
                final float bricksCrushingCoefficient = 7.3f;
                final float startVelocityForCrushedBricks = roundDynamicElement.body.getMass() * bricksCrushingCoefficient;
                float width = roundDynamicElement.getWidth();
                float height = roundDynamicElement.getHeight();
                float shreddingCoef = 0.0f;
                if (height > width) shreddingCoef = height / width;
                else shreddingCoef = width / height;
                if (shreddingCoef > 2.5f) {
                    crushBrick(gameRound, (byte) 2, roundDynamicElement, startVelocityForCrushedBricks, attackPos, attackAngle);
                } else {
                    if (attackType == ATTACK_FROM_EXPLOSION)
                        crushBrick(gameRound, (byte) 4, roundDynamicElement, startVelocityForCrushedBricks, attackPos, attackAngle);
                    else if (attackType == ATTACK_FROM_WEAPON)
                        crushBrick(gameRound, (byte) 3, roundDynamicElement, startVelocityForCrushedBricks, attackPos, attackAngle);
                }
            }
            else{
                for (int i = 0; i < gameRound.roundElements.size(); i++) {
                    if (roundDynamicElement.equals(gameRound.roundElements.get(i))) {
                        gameRound.roundElements.get(i).body.setActive(false);
                        PhysicGameWorld.controller.destroyBody(gameRound.roundElements.get(i).body);
                        gameRound.roundElements.get(i).body = null;
                        gameRound.roundElements.remove(gameRound.roundElements.get(i));
                    }
                }
            }
        }
        else if (roundDynamicElement.getClass() == RoundPolygon.class) {
            System.out.println("This polygon is crushed without pieces");
            for (int i = 0; i < gameRound.roundElements.size(); i++) {
                if (roundDynamicElement.equals(gameRound.roundElements.get(i))) {
                    gameRound.roundElements.get(i).body.setActive(false);
                    PhysicGameWorld.controller.destroyBody(gameRound.roundElements.get(i).body);
                    gameRound.roundElements.get(i).body = null;
                    gameRound.roundElements.remove(gameRound.roundElements.get(i));
                }
            }
        }
        else if (roundDynamicElement.getClass() == RoundCircle.class) {
            System.out.println("This circle is crushed without pieces");
            for (int i = 0; i < gameRound.roundElements.size(); i++) {
                if (roundDynamicElement.equals(gameRound.roundElements.get(i))) {
                    gameRound.roundElements.get(i).body.setActive(false);
                    PhysicGameWorld.controller.destroyBody(gameRound.roundElements.get(i).body);
                    gameRound.roundElements.get(i).body = null;
                    gameRound.roundElements.remove(gameRound.roundElements.get(i));
                }
            }
        }	//(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, boolean flip){
        float randomValue = Program.engine.random(100);
        if (randomValue>50) gameRound.getSoundController().setAndPlayAudio(SoundsInGame.ELEMENT_CRUSHED_1);
        else gameRound.getSoundController().setAndPlayAudio(SoundsInGame.ELEMENT_CRUSHED_2);
    }

    private void crushBrickInTwoParts(float parentWidth, float parentHeight, float childWidth, float childHeight, float crushCoefficient){

    }

    private void crushBrick(GameRound gameRound, byte childNumber, RoundElement boxToBeCrushed, float childRelativeVelocity, Vec2 attackPlace, float atackAngle)  {
        Vec2 position = new Vec2(boxToBeCrushed.body.getPosition().x, boxToBeCrushed.body.getPosition().y);
        int life =  (int) (3*boxToBeCrushed.getMaxLife());
        if (life > GameObject.IMMORTALY_LIFE) life = GameObject.IMMORTALY_LIFE;
        position = PhysicGameWorld.controller.coordWorldToPixels(position);
        float angleInDegrees = (float) Math.toDegrees(boxToBeCrushed.body.getAngle());
        float angleInRadians = boxToBeCrushed.body.getAngle();
        float parentWidth = boxToBeCrushed.getWidth();
        float parentHeight = boxToBeCrushed.getHeight();
        float childWidth = 0f;
        float childHeight = 0f;
        float crushCoefficient = 4f/9f;
        PVector attackPlaceToPVector = new PVector(attackPlace.x, attackPlace.y);
        if (Program.debug) System.out.println("Basic pos: " + boxToBeCrushed.getPixelPosition() + "; Angle in degrees: " + angleInDegrees);
        if (childNumber == 3 || childNumber == 4) {
            byte restBricksNumber = childNumber;
            childNumber = 4;
            childWidth = crushCoefficient*parentWidth;
            childHeight = crushCoefficient*parentHeight;
            PVector fromCenterOfBoxToFirstChild= new PVector(-childWidth/2, -childHeight/2);
            fromCenterOfBoxToFirstChild.rotate(angleInRadians);
            PVector center = new PVector(position.x, position.y);
            center.rotate(PConstants.PI);
            PVector [] childPositions = new PVector [childNumber];
            childPositions[0] = new PVector(-childWidth/2, -childHeight/2);
            childPositions[1] = new PVector(childWidth/2, -childHeight/2);
            childPositions[2] = new PVector(-childWidth/2, childHeight/2);
            childPositions[3] = new PVector(childWidth/2, childHeight/2);
            Vec2 [] explosionVelocities = new Vec2 [childPositions.length];
            for (int i = 0; i < childPositions.length; i++) {
                Vec2 bodyPos = PhysicGameWorld.controller.getBodyPixelCoord(boxToBeCrushed.body);
                Vec2 bulletPos = new Vec2(attackPlace.x, attackPlace.y);
                Vec2 vectorToBricks = bodyPos.sub(bulletPos);
                float angleValueInDegrees = 0f;
                angleValueInDegrees = PApplet.degrees(PApplet.atan(vectorToBricks.y/vectorToBricks.x));
                if (vectorToBricks.x>0) {}
                else {
                    if (angleValueInDegrees>0){
                        angleValueInDegrees+=180;
                    }
                    else if (angleValueInDegrees<0){
                        angleValueInDegrees-=180;
                    }
                }
                if (angleValueInDegrees<0) angleValueInDegrees+=360;
                System.out.println("Angle = " + (angleValueInDegrees));
                childPositions[i].rotate(angleInRadians);
                childPositions[i].sub(center);
                PVector vectorToChild = new PVector(childPositions[i].x,childPositions[i].y);
                vectorToChild.sub(attackPlaceToPVector);
                float angleFromAttackPlaceToChild = vectorToChild.heading();
                float randomVelocityAngle = Program.engine.random(-PConstants.PI/9, PConstants.PI/9);
                explosionVelocities[i] = new Vec2();
                explosionVelocities[i].x = (float)(childRelativeVelocity*Math.cos(PApplet.radians(angleValueInDegrees)+randomVelocityAngle));
                explosionVelocities[i].y = (float) (-childRelativeVelocity*Math.sin(PApplet.radians(angleValueInDegrees)+randomVelocityAngle));
                System.out.println("Vector = " + (explosionVelocities[i]));
            }
            int brickToBeCrushedNumber = getCrushedBrickNumber(childPositions, attackPlaceToPVector);
            for (int i = 0; i < childPositions.length; i++) {
                if (i != brickToBeCrushedNumber || restBricksNumber == 4) {
                    RoundBox box = new RoundBox(new Vec2(childPositions[i].x, childPositions[i].y), angleInDegrees ,childWidth, childHeight, life, false, BodyType.DYNAMIC);
                    box.setAsSecondary(true);   // Can be deleted after hiding from the camera
                    if (boxToBeCrushed.hasGraphic()){
                        boolean fillArea = boxToBeCrushed.getSprite().getFill();
                        ImageZoneSimpleData imageZoneSimpleData = getImageZoneForCrushingToFourParts(boxToBeCrushed, crushCoefficient, i, fillArea);
                        box.loadImageData(boxToBeCrushed.getSprite().getPath(),(int) imageZoneSimpleData.leftX, (int) imageZoneSimpleData.upperY, (int) imageZoneSimpleData.rightX, (int)imageZoneSimpleData.lowerY,(int)childWidth,(int)childHeight,  false);
                        box.getSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(box.getSprite().getPath()));
                    }
                    gameRound.roundElements.add(box);
                    explosionVelocities[i].x/=(gameRound.roundElements.get(gameRound.roundElements.size()-1).body.getMass());
                    explosionVelocities[i].y/=(gameRound.roundElements.get(gameRound.roundElements.size()-1).body.getMass());
                    gameRound.roundElements.get(gameRound.roundElements.size()-1).body.setLinearVelocity(explosionVelocities[i]);
                }
            }
        }
        else if (childNumber == 2) {
            try {
                crushCoefficient = 0.5f;
                if (parentWidth > parentHeight) {
                    childWidth = crushCoefficient*parentWidth;
                    childHeight = (parentHeight);
                }
                else {
                    childWidth = (parentWidth);
                    childHeight = crushCoefficient * parentHeight;
                }
                System.out.println("Child width: " + childWidth + "; height: " + childHeight + " ; Parent: " + parentWidth + "x" + parentHeight);
                PVector vec= new PVector(0, -childHeight/2);
                PVector fromCenterOfBoxToFirstChild = vec.rotate(angleInRadians);
                PVector sourceBoxPos = new PVector(position.x, position.y);
                PVector center = sourceBoxPos.rotate(PConstants.PI);
                PVector [] childPositions = getChildPositions(childNumber, childWidth, childHeight, angleInDegrees, boxToBeCrushed);
                Vec2 [] explosionVelocities = new Vec2 [childPositions.length];
                for (int i = 0; i < childPositions.length; i++) {
                    //childPositions[i].rotate(angleInRadians);
                    childPositions[i].sub(center);
                    PVector toChild = new PVector(childPositions[i].x,childPositions[i].y);
                    PVector vectorToChild = toChild.sub(attackPlaceToPVector);
                    float angleFromAttackPlaceToChild = vectorToChild.heading();
                    float randomVelocityAngle = Program.engine.random(-PConstants.PI/4, PConstants.PI/4);
                    explosionVelocities[i] = new Vec2();
                    explosionVelocities[i].x = (float)(childRelativeVelocity*Math.cos(angleFromAttackPlaceToChild+randomVelocityAngle));
                    explosionVelocities[i].y = (float) (childRelativeVelocity*Math.sin(angleFromAttackPlaceToChild+randomVelocityAngle));
                }
                System.out.println("Crushed in 2 parts");
                for (int i = 0; i < childPositions.length; i++) {
                    boolean fillArea = boxToBeCrushed.getSprite().getFill();
                    ImageZoneSimpleData imageZoneSimpleData = getImageZoneForCrushingToTwoParts(boxToBeCrushed, crushCoefficient, i, fillArea);
                    RoundBox box = new RoundBox(new Vec2(childPositions[i].x, childPositions[i].y), angleInDegrees ,childWidth, childHeight, life, false, BodyType.DYNAMIC);

                    System.out.println("Child dims: " + childWidth + "x" + childHeight);
                    explosionVelocities[i].x/=(box.body.getMass());
                    explosionVelocities[i].y/=(box.body.getMass());
                    box.loadImageData(boxToBeCrushed.getSprite().getPath(),(int) imageZoneSimpleData.leftX, (int) imageZoneSimpleData.upperY, (int) imageZoneSimpleData.rightX, (int)imageZoneSimpleData.lowerY,(int)childWidth,(int)childHeight,  fillArea);
                    box.getSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(box.getSprite().getPath()));
                    box.body.setLinearVelocity(explosionVelocities[i]);
                    // next function down't work
                    boolean rotateDirection = getRotatingDirection(atackAngle, attackPlace, box.getPixelPosition());
                    if (rotateDirection) box.body.setAngularVelocity(Program.engine.random(0,ANGULAR_VELOCITY_AFTER_CRUSH_COEFFICIENT*box.body.getMass()));
                    else box.body.setAngularVelocity(Program.engine.random(-ANGULAR_VELOCITY_AFTER_CRUSH_COEFFICIENT*box.body.getMass(), 0));

                    //System.out.println("Child mass: " + box.body.getMass());
                    //box.body.resetMassData();
                    gameRound.roundElements.add(box);
                }
            }
            catch (Exception e) {
                System.out.println("Can not crush the brick;"  + e);
            }
        }
        boxToBeCrushed.body.setActive(false);
        PhysicGameWorld.controller.destroyBody(boxToBeCrushed.body);
        gameRound.roundElements.remove(boxToBeCrushed);
    }

    private boolean getRotatingDirection(float bulletAngle, Vec2 attackPlace, PVector elementPos){
        final boolean CW = false;
        final boolean CCW = true;
        PVector fromBulletPlaceToElement = new PVector(elementPos.x-attackPlace.x, elementPos.y-attackPlace.y);
        float angleToElement = PApplet.degrees(fromBulletPlaceToElement.heading());
        if (angleToElement > 0) return CW;
        else return CCW;
    }

    private PVector [] getChildPositions(int childNumber, float childWidth, float childHeight, float angleInDegrees, RoundElement boxToBeCrushed) {
        PVector [] childPositions = new PVector [childNumber];
        int parentWidth = (int)boxToBeCrushed.getWidth();
        int parentHeight = (int)boxToBeCrushed.getHeight();
        float devidingTranslatingCoef = 11f/10f;
        if (parentWidth > parentHeight) {
            System.out.println("Crushing of object with large width under angle: " + angleInDegrees);
            childPositions[0] = new PVector(-devidingTranslatingCoef*childWidth * PApplet.cos(PApplet.radians(angleInDegrees))/2, -devidingTranslatingCoef*childWidth * PApplet.sin(PApplet.radians(angleInDegrees))/2);
            childPositions[1] = new PVector(-childPositions[0].x, -childPositions[0].y);
            System.out.println("Positions: " + childPositions[0] + " znd " + childPositions[1]);
        }
        else{
            System.out.println("Crushing of object with large height ander angle: " + angleInDegrees);
            childPositions[0] = new PVector(-devidingTranslatingCoef*childHeight * PApplet.sin(PApplet.radians(angleInDegrees))/2, -devidingTranslatingCoef*childHeight * PApplet.cos(PApplet.radians(angleInDegrees))/2);
            childPositions[1] = new PVector(-childPositions[0].x, -childPositions[0].y);
            System.out.println("Positions: " + childPositions[0] + " znd " + childPositions[1]);
        }
        return childPositions;
    }

    private PVector [] getChildPositionsDoesNotWorkProterly(int childNumber, float childWidth, float childHeight, float angleInDegrees, RoundElement boxToBeCrushed) {
        PVector [] childPositions = new PVector [childNumber];
        int parentWidth = (int)boxToBeCrushed.getWidth();
        int parentHeight = (int)boxToBeCrushed.getHeight();
        float devidingTranslatingCoef = 11f/10f;
        if (parentWidth > parentHeight) {
            System.out.println("Crushing of object with large width under angle: " + angleInDegrees);
            childPositions[0] = new PVector(-devidingTranslatingCoef*childWidth * PApplet.sin(PApplet.radians(angleInDegrees))/2, -devidingTranslatingCoef*childWidth * PApplet.cos(PApplet.radians(angleInDegrees))/2);
            childPositions[1] = new PVector(-childPositions[0].x, -childPositions[0].y);
            System.out.println("Positions: " + childPositions[0] + " znd " + childPositions[1]);


        }
        else{
            System.out.println("Crushing of object with large height ander angle: " + angleInDegrees);
            childPositions[0] = new PVector(-devidingTranslatingCoef*childHeight * PApplet.sin(PApplet.radians(angleInDegrees))/2, -devidingTranslatingCoef*childHeight * PApplet.cos(PApplet.radians(angleInDegrees))/2);
            childPositions[1] = new PVector(-childPositions[0].x, -childPositions[0].y);
            System.out.println("Positions: " + childPositions[0] + " znd " + childPositions[1]);


        }
        return childPositions;
    }

    //This function doesn't work properly but can be released
    private ImageZoneSimpleData getImageZoneForCrushingToFourParts(RoundElement boxToBeCrushed, float crushCoefficient, int elementNumber, boolean fillArea){
        int leftUpperTextureX = 156;
        int leftUpperTextureY = 15;
        int rightLowerTextureX = 167;
        int rightLowerTextureY = 23;
        System.out.println("Fill value: " + fillArea);
        if (!fillArea) { // Stretch texture
            if (boxToBeCrushed.getWidth() < boxToBeCrushed.getHeight()) {
                System.out.println("It works good! Test with graphic stretching 1");
                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = (int) (leftUpperTextureX + crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                }
                else if (elementNumber == 1) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight()-crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                }
                else if (elementNumber == 2) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight()-crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = (int) (leftUpperTextureX + crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                else if (elementNumber == 3) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight()-crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight()-crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
            else{   // fill with single elements
                System.out.println("It works good! Test with graphic stretching 2");
                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = (int) (leftUpperTextureX + crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                }
                else if (elementNumber == 1) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight()-crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                }
                else if (elementNumber == 2) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight()-crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = (int) (leftUpperTextureX + crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                else if (elementNumber == 3) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight()-crushCoefficient*(boxToBeCrushed.getSprite().getxRight()-boxToBeCrushed.getSprite().getxLeft()));
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight()-crushCoefficient*(boxToBeCrushed.getSprite().getyRight()-boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
        }
        else {
            if (boxToBeCrushed.getWidth() < boxToBeCrushed.getHeight()) {
                System.out.println("Test with elements segmenting 1");
                if (elementNumber == 0 || elementNumber == 2) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = (int) (leftUpperTextureX + crushCoefficient * (boxToBeCrushed.getSprite().getxRight() - boxToBeCrushed.getSprite().getxLeft()));
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                } else if (elementNumber == 1 || elementNumber == 3) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight() - crushCoefficient * (boxToBeCrushed.getSprite().getxRight() - boxToBeCrushed.getSprite().getxLeft()));
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                System.out.println("Pos on tileset: " + leftUpperTextureX + "x" + leftUpperTextureY + "; " + rightLowerTextureX + "x" + rightLowerTextureY);
            }
            else {
                System.out.println("Test with elements segmenting 2");
                if (elementNumber == 0 || elementNumber == 1) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient * (boxToBeCrushed.getSprite().getyRight() - boxToBeCrushed.getSprite().getyLeft()));
                } else if (elementNumber == 2 || elementNumber == 3) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight() - crushCoefficient * (boxToBeCrushed.getSprite().getyRight() - boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
        }

        ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(leftUpperTextureX, leftUpperTextureY, rightLowerTextureX, rightLowerTextureY);
        return imageZoneSimpleData;
    }

    private ImageZoneSimpleData getImageZoneForCrushingToTwoParts(RoundElement boxToBeCrushed, float crushCoefficient, int elementNumber, boolean fillArea){
        int leftUpperTextureX = 156;
        int leftUpperTextureY = 15;
        int rightLowerTextureX = 167;
        int rightLowerTextureY = 23;
        //System.out.println("Source width: " + boxToBeCrushed.getWidth()  + "; Height: " + boxToBeCrushed.getHeight());
        if (!fillArea) {
            if (boxToBeCrushed.getWidth() < boxToBeCrushed.getHeight()) {
                System.out.println("Fall 1 for crushing in two parts and without filling but with stretching");
                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = (int) (leftUpperTextureY + crushCoefficient * (boxToBeCrushed.getSprite().getyRight() - boxToBeCrushed.getSprite().getyLeft()));
                } else if (elementNumber == 1) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = (int) (boxToBeCrushed.getSprite().getyRight() - crushCoefficient * (boxToBeCrushed.getSprite().getyRight() - boxToBeCrushed.getSprite().getyLeft()));
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
            else{
                System.out.println("Fall 2 for crushing in two parts and without filling but with stretching");
                System.out.println("Dimensions was: " + boxToBeCrushed.getWidth() + "; is: " + boxToBeCrushed.getHeight());
                //System.out.println("Dimensions is: " + boxToBeCrushed.getWidth() + "; is: " + boxToBeCrushed.getHeight());

                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = (int) (boxToBeCrushed.getSprite().getxLeft() + crushCoefficient * (boxToBeCrushed.getSprite().getxRight()- (boxToBeCrushed.getSprite().getxLeft())));
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                else if (elementNumber == 1) {
                    leftUpperTextureX = (int) (boxToBeCrushed.getSprite().getxRight() - crushCoefficient * (boxToBeCrushed.getSprite().getxRight()- (boxToBeCrushed.getSprite().getxLeft())));
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
        }
        else{

            if (boxToBeCrushed.getWidth() < boxToBeCrushed.getHeight()) {
                System.out.println(" Fall 1 for crushing in two parts with filling when width < height");
                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                else if (elementNumber == 1) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
            else{
                System.out.println("Fall 2 for crushing in two parts with filling when width > height");
                if (elementNumber == 0) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
                else if (elementNumber == 1) {
                    leftUpperTextureX = boxToBeCrushed.getSprite().getxLeft();
                    leftUpperTextureY = boxToBeCrushed.getSprite().getyLeft();
                    rightLowerTextureX = boxToBeCrushed.getSprite().getxRight();
                    rightLowerTextureY = boxToBeCrushed.getSprite().getyRight();
                }
            }
        }
        ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(leftUpperTextureX, leftUpperTextureY, rightLowerTextureX, rightLowerTextureY);
        return imageZoneSimpleData;
    }

    private int getCrushedBrickNumber(PVector[] childPositions, PVector attackPlace) {
        float minDistance = 200000f;
        int number = 0;
        System.out.println("try to find nearest brick");
        for (int i = 0; i < childPositions.length; i++) {
            float actualDistance = Program.engine.dist(childPositions[i].x, childPositions[i].y, attackPlace.x, attackPlace.y);
            if (i == 0) {
                minDistance = actualDistance;
            }
            else {
                if (actualDistance<minDistance) {
                    minDistance = actualDistance;
                    number = i;
                }
            }
        }
        System.out.println("Founded " + number);
        return number;
    }



}
