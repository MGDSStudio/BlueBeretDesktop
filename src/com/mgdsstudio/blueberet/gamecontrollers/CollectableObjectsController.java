package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableObjectInNesStyle;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.Fruit;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.SimpleCollectableElement;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.TrackData;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.graphic.*;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;

import java.util.ArrayList;

public class CollectableObjectsController {
    private ArrayList<AbstractCollectable> collectableObjects;
    public CollectableObjectsController(){
        collectableObjects = new ArrayList<>();
    }

    // Mutable
    private Body mutB1;
    private Body mutB2;

    public static boolean isObjectInAnAnother(Vec2 position, GameRound gameRound){
        if (gameRound == null) return AbstractCollectable.IN_BAG;
        else {
            boolean objectIsBySomePersonOrObject = false;
            for (Person person : gameRound.getPersons()) {
                for (Fixture fixture = person.body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                    if (fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position)) && !objectIsBySomePersonOrObject) {
                        System.out.println("Object is placed in " + person.getClass());
                        objectIsBySomePersonOrObject = true;
                    }
                }
            }
            if (!objectIsBySomePersonOrObject) {
                for (RoundElement roundElement : gameRound.roundElements) {
                    for (Fixture fixture = roundElement.body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                        if (fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position)) && !objectIsBySomePersonOrObject) {
                            System.out.println("Object is placed in " + roundElement.getClass());
                            objectIsBySomePersonOrObject = true;
                        }
                    }
                }
            }
            return objectIsBySomePersonOrObject;
        }

    }

    public static GameObject getAnotherObjectOnThisPlace(Vec2 position, GameRound gameRound){
        for (Person person : gameRound.getPersons()) {
            for (Fixture fixture = person.body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                if (fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position))) {
                    return person;
                }
            }
        }
        for (RoundElement roundElement : gameRound.roundElements) {
            for (Fixture fixture = roundElement.body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
                if (fixture.testPoint(PhysicGameWorld.controller.coordPixelsToWorld(position))) {
                    return roundElement;
                }
            }
        }
        return null;
    }

    private void determineObjectInWorldPosition(CollectableObjectInNesStyle collectableObject, GameRound gameRound){

    }



    private void placeCollectableObjectsInRoundElements(GameRound gameRound) {
        for (AbstractCollectable collectableObject : collectableObjects){
            Vec2 position = PhysicGameWorld.controller.getBodyPixelCoord(collectableObject.body);
            for (Person person : gameRound.getPersons()){
                for (Fixture fixture = person.body.getFixtureList(); fixture!=null; fixture=fixture.getNext()) {
                    if (fixture.testPoint(position)) {
                        System.out.println("Object " + collectableObject.getClass() + " is placed in " + person.getClass());
                        return;
                    }
                }

            }
        }

    }


    public void addNewcollectableObject(AbstractCollectable collectableObject){
        collectableObjects.add(collectableObject);
    }



    public void loadSprites(Tileset tilesetUnderPath, int i) {
        if (collectableObjects.get(i).getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE)  collectableObjects.get(i).loadSprites(tilesetUnderPath);
        if (collectableObjects.get(i).getGraphicType() == SingleGraphicElement.SPRITE_ANIMATION_TYPE)  {
            collectableObjects.get(i).loadAnimation(tilesetUnderPath);
            if (collectableObjects.get(i).getType() == CollectableObjectInNesStyle.MUSHROOM) collectableObjects.get(i).getSpriteAnimation().setLastSprite((int)24);
        }
        System.out.println("Textures for collectable elements were successfully uploaded");
    }


    public StaticSprite getSprite(int elementNumber){
        return collectableObjects.get(elementNumber).getSprite();	//every platforms are equals

    }

    public SpriteAnimation getSpriteAnimation(int elementNumber){
        return collectableObjects.get(elementNumber).getSpriteAnimation();
    }

    public SingleGraphicElement getGraphicElement(int collectableElementNumber){
        if (collectableObjects.get(collectableElementNumber).getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) return collectableObjects.get(collectableElementNumber).getSprite();
        else return collectableObjects.get(collectableElementNumber).getSpriteAnimation();
    }

    public int getObjectsNumber(){
        return collectableObjects.size();
    }

    public ArrayList <AbstractCollectable> getCollectableObjects(){
        return collectableObjects;
    }


    private void personGotTheObject(GameRound gameRound, Person person, AbstractCollectable abstractCollectable){
        boolean gotByPlayer = false;
        if (person.equals(gameRound.getPlayer())) gotByPlayer = true;
        addNewDissolvingTextToGameWorld(gameRound, person, abstractCollectable);

        abstractCollectable.body.setGravityScale(SimpleCollectableElement.NORMAL_GRAVITY_SCALE);
        abstractCollectable.body.setType(BodyType.DYNAMIC);
        abstractCollectable.deleteSpring();
        if (!mustBeDeleted(abstractCollectable)){
            abstractCollectable.setInWorldPosition(CollectableObjectInNesStyle.IN_BAG);
            abstractCollectable.body.setActive(false);
            person.addNewCollectableObject(abstractCollectable);
            collectableObjects.remove(abstractCollectable);
            PhysicGameWorld.controller.destroyBody(abstractCollectable.body);
        }
        else{
            person.recoveryLifeInAbsoluteValues(abstractCollectable.getValueToBeAddedByGain());
            abstractCollectable.body.setActive(false);
            collectableObjects.remove(abstractCollectable);
            PhysicGameWorld.controller.destroyBody(abstractCollectable.body);
        }
        if (gotByPlayer) {
            gameRound.getSoundController().setAndPlayAudioForCollectedElement(abstractCollectable);
        }
        else gameRound.getSoundController().setAndPlayAudioForCollectedElement(abstractCollectable, TrackData.QUARTER_AUDIO);
    }

    private boolean mustBeDeleted(AbstractCollectable abstractCollectable) {
        if (abstractCollectable.getClass() == Fruit.class){
            return true;
        }
        else return false;
    }

    private void addNewDissolvingTextToGameWorld(GameRound gameRound, Person person, AbstractCollectable abstractCollectable) {
        String text = abstractCollectable.getStringAddedToWorldByBeGained();
        int valueType = 0;
        DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(person.getPixelPosition().x, person.getPixelPosition().y-person.getHeight()/1.5f, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, text, DissolvingAndUpwardsMovingText.NORMAL_DISSOLVING_TIME, DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, valueType);
        gameRound.addNewDissolvingText(dissolvingText);
        dissolvingText.setColor(AbstractText.WHITE);
        //int xOnDisplay = (int) person.getOnScreenPosition(gameRound.getLaunchableWhizbangsController().gameCamera).x;
        //int yOnDisplay = (int) person.getOnScreenPosition(gameRound.getLaunchableWhizbangsController().gameCamera).y;
        //dissolvingText.setColor(getColorForDissolvingText(xOnDisplay, yOnDisplay-Program.engine.height/20));
        //getColorForDissolvingText(xOnDisplay, yOnDisplay-Program.engine.height/20);
    }

    private int getColorForDissolvingText(int x, int y) {
        int color = 0;
        try {
            Program.engine.loadPixels();
            color = Program.engine.get(x, y);
            System.out.println("Color is RGB: R: " + Program.engine.red(color) + ", G: " + Program.engine.green(color) + ", B: " + Program.engine.blue(color));
            Program.engine.updatePixels();
        }
        catch (Exception e){
            System.out.println("Only in main thread");
        }
        return color;
    }



    public void update(GameRound gameRound) {
        for (Contact contact : PhysicGameWorld.beginContacts) {
            mutB1 = contact.getFixtureA().getBody();
            mutB2 = contact.getFixtureB().getBody();
                for (int i = (collectableObjects.size()-1); i>=0; i--) {
                    if (collectableObjects.get(i).getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) {
                        if (mutB1.equals(collectableObjects.get(i).body) || mutB2.equals(collectableObjects.get(i).body)) {
                            if (mutB1.equals(collectableObjects.get(i).body)) {
                                boolean objectWasGot = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (mutB2.equals(person.body) && person.isAlive()) {
                                        personGotTheObject(gameRound, person, collectableObjects.get(i));
                                        objectWasGot = true;
                                    }
                                }
                                if (!objectWasGot) collectableObjects.get(i).deleteSpring();
                            }
                            else if (mutB2.equals(collectableObjects.get(i).body)) {
                                boolean objectWasGot = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (mutB1.equals(person.body) && person.isAlive()) {
                                        personGotTheObject(gameRound, person, collectableObjects.get(i));
                                        objectWasGot = true;
                                        
                                    }
                                }
                                if (!objectWasGot) collectableObjects.get(i).deleteSpring();
                            }
                        }
                    }
                }

        }
        for (int i = (collectableObjects.size()-1); i >= 0; i--){
            collectableObjects.get(i).update(gameRound);
            if (collectableObjects.get(i).isCanBeDeleted()){
                collectableObjects.remove(collectableObjects.get(i));
                System.out.println("This collectable object was deleted");
            }
        }
    }
/*
    public void updateOld (GameRound gameRound) {
        for (Contact contact : PhysicGameWorld.beginContacts) {
            Fixture f1 = contact.getFixtureA();
            Fixture f2 = contact.getFixtureB();
            Body b1 = f1.getBody();
            Body b2 = f2.getBody();
            if (b1.getType() == BodyType.DYNAMIC && b2.getType() == BodyType.DYNAMIC){
                for (int i = (collectableObjects.size()-1); i>=0; i--) {
                    if (collectableObjects.get(i).getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) {
                        if (b1.equals(collectableObjects.get(i).body) || b2.equals(collectableObjects.get(i).body)) {
                            if (b1.equals(collectableObjects.get(i).body)) {
                                boolean objectWasGot = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (b2.equals(person.body) && person.isAlive()) {
                                        System.out.println("The object " + collectableObjects.get(i).getClass() + " was reached by " + person.getClass());
                                        collectableObjects.get(i).deleteSpring();
                                        collectableObjects.get(i).setInWorldPosition(CollectableObjectInNesStyle.IN_BAG);
                                        collectableObjects.get(i).body.setActive(false);
                                        person.addNewCollectableObject(collectableObjects.get(i));
                                        collectableObjects.remove(i);
                                        objectWasGot = true;
                                    }
                                }
                                if (!objectWasGot) collectableObjects.get(i).deleteSpring();
                            }
                            else if (b2.equals(collectableObjects.get(i).body)) {
                                boolean objectWasGot = false;
                                for (Person person : gameRound.getPersons()) {
                                    if (b1.equals(person.body) && person.isAlive()) {
                                        System.out.println("The object " + collectableObjects.get(i).getClass() + " was reached by " + person.getClass());
                                        collectableObjects.get(i).deleteSpring();
                                        collectableObjects.get(i).setInWorldPosition(CollectableObjectInNesStyle.IN_BAG);
                                        collectableObjects.get(i).body.setActive(false);
                                        person.addNewCollectableObject(collectableObjects.get(i));
                                        collectableObjects.remove(i);
                                        objectWasGot = true;
                                    }
                                }
                                if (!objectWasGot) collectableObjects.get(i).deleteSpring();
                            }
                        }
                    }
                }
            }
        }
        for (AbstractCollectable collectableObject : collectableObjects){
            collectableObject.update(gameRound);
        }
    }
*/

    public void draw(GameCamera gameCamera) {
        for (int i = 0; i < collectableObjects.size(); i++){
            if (!collectableObjects.get(i).isSleeped()) collectableObjects.get(i).draw(gameCamera);
        }
    }

    public void attackFromExplosion(int n, float angle, float relativeDistanceToExplosion){
        final float basicValue = 20;
        final float attackValue = basicValue*relativeDistanceToExplosion/100f;
        Vec2 attackVelocity = new Vec2(attackValue* Program.engine.cos(PApplet.radians(angle)), attackValue* Program.engine.sin(PApplet.radians(angle)));
        if (collectableObjects.get(n).hasSpring()){
            collectableObjects.get(n).body.setLinearVelocity(new Vec2(0,0));
            collectableObjects.get(n).body.setAngularVelocity(0);
            System.out.println("Delete spring by the explosion");
            collectableObjects.get(n).deleteSpring();
            collectableObjects.get(n).body.setLinearVelocity(PhysicGameWorld.controller.vectorPixelsToWorld(attackVelocity));
            collectableObjects.get(n).body.setAngularVelocity(0);
            collectableObjects.get(n).body.resetMassData();
        }

        if (collectableObjects.get(n).getType() == CollectableObjectInNesStyle.MUSHROOM){
            collectableObjects.get(n).body.setActive(false);
            PhysicGameWorld.controller.destroyBody(collectableObjects.get(n).body);
            collectableObjects.get(n).body.setActive(true);
            collectableObjects.remove(n);
        }
    }

    public void coalisionWithBullet(GameRound gameRound, int n) {
        try{
            System.out.println("Colision with bullet + " + collectableObjects.get(n).hasSpring());
            if (collectableObjects.get(n).hasSpring()){
                collectableObjects.get(n).body.setLinearVelocity(new Vec2(0,0));
                collectableObjects.get(n).body.setAngularVelocity(0);
                collectableObjects.get(n).deleteSpring();
                collectableObjects.get(n).body.setLinearVelocity(new Vec2(0,0));
                collectableObjects.get(n).body.setAngularVelocity(0);
                collectableObjects.get(n).body.resetMassData();
            }
            else {
                if (collectableObjects.get(n).getLife() != GameObject.IMMORTALY_LIFE){
                    PhysicGameWorld.controller.destroyBody(collectableObjects.get(n).body);
                    collectableObjects.remove(n);
                }
                if (!collectableObjects.get(n).isStopped()) {
                    collectableObjects.get(n).setStopped(true);
                    collectableObjects.get(n).collisionWithBullet(null);
                }
            }
        }
        catch (Exception e){
            System.out.println("Can not update bullet hitting with collectableObject " + e);
        }
    }

    public void coalisionWithBullet(GameRound gameRound, int n, Bullet bullet) {
        collectableObjects.get(n).collisionWithBullet(bullet);
        gameRound.getSoundController().setAndPlayAudio(SoundInGameController.WALL_HITTED);
    }

    /*
    public void updatePostContactWithSomeBody(Body bodyA, Body bodyB, GameRound gameRound) {
        for (AbstractCollectable collectableObject:collectableObjects){
            if (collectableObject.getInWorldPosition() == AbstractCollectable.IN_WORLD) {
                if (collectableObject.hasSpring()) {
                    if (collectableObject.body.equals(bodyA)) {
                        deleteSpring(collectableObject);
                        System.out.println("First object:" + gameRound.getGameObjectByBody(bodyA).getClass() + "; second: " + gameRound.getGameObjectByBody(bodyB).getClass());
                    }
                    else if (collectableObject.body.equals(bodyB)) {
                        deleteSpring(collectableObject);
                        System.out.println("First object:" + gameRound.getGameObjectByBody(bodyA).getClass() + "; second: " + gameRound.getGameObjectByBody(bodyB).getClass());
                    }
                }
            }
        }
    }
*/

    public void updatePreContactWithSomeBody(Body bodyA, Body bodyB, GameRound gameRound) {
        for (AbstractCollectable collectableObject:collectableObjects){
            if (collectableObject.getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) {
                if (collectableObject.hasSpring()) {
                    if (collectableObject.body.equals(bodyA)) {
                        deleteSpring(collectableObject);
                        System.out.println("First object:" + gameRound.getGameObjectByBody(bodyA).getClass() + "; second: " + gameRound.getGameObjectByBody(bodyB).getClass());
                    }
                    else if (collectableObject.body.equals(bodyB)) {
                        deleteSpring(collectableObject);
                        System.out.println("First object:" + gameRound.getGameObjectByBody(bodyA).getClass() + "; second: " + gameRound.getGameObjectByBody(bodyB).getClass());
                    }
                }
            }
        }
    }

    private void deleteSpring(AbstractCollectable collectableObject){
        collectableObject.deleteSpringAfterContact();
    }

    public void releaseLastCollectableObject(GameRound gameRound, GameObject gameObject, float angle) {
        System.out.println("Last object is free");
        gameRound.releaseLastCollectableGameObject(gameObject, angle);
    }

    public void preContact(Contact arg0, Manifold arg1) {
        Body body1 = arg0.m_fixtureA.getBody();
        Body body2 = arg0.m_fixtureB.getBody();
        for (AbstractCollectable collectableObject : collectableObjects){
            if (collectableObject.body.getType() == BodyType.STATIC) {
                if (body1.equals(collectableObject.body)) {
                    collectableObject.body.setType(BodyType.DYNAMIC);
                    System.out.println("Object released");
                }
                else if (body2.equals(collectableObject.body)) {
                    collectableObject.body.setType(BodyType.DYNAMIC);
                    System.out.println("Object released");
                }
            }
        }
    }

    public void setCollectableObjects(ArrayList<AbstractCollectable> collectableObjects) {
        this.collectableObjects = collectableObjects;
    }
}
