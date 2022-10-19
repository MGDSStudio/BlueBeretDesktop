package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.common.Vec2;

public abstract class AbstractCollectable extends RoundElement {
    private static String OBJECT_TO_DISPLAY_NAME = "Collectable object";
    public final static boolean IN_WORLD = true;
    public final static boolean IN_BAG = false;
    protected boolean inWorldPosition = IN_BAG;
    protected Vec2 parentPos;
    //protected IndependentOnScreenAnimation independentOnScreenAnimation;
    protected  ShiningStarsDrawingController starsDrawingController;

    protected byte type = ABSTRACT_COIN;
    public final static byte ABSTRACT_COIN = 0;
    public final static byte WEAPON = 2;
    public final static byte MUSHROOM = 3;
    public final static byte STAR = 4;
    public final static byte BULLETS = 5;
    public final static byte ABSTRACT_GEM = 6;
    public final static byte ABSTRACT_AMMO = 7;
    public final static byte ABSTRACT_MEDICAL_KIT = 8;

    public final static byte SMALL_RED_STONE = 10;
    //public final static byte MEDIUM_RED_STONE = 11;
    public final static byte BIG_RED_STONE = 12;

    public final static byte SMALL_GREEN_STONE = 13;
    //public final static byte MEDIUM_GREEN_STONE = 14;
    public final static byte BIG_GREEN_STONE = 15;

    public final static byte SMALL_BLUE_STONE = 16;
    //public final static byte MEDIUM_BLUE_STONE = 17;
    public final static byte BIG_BLUE_STONE = 18;

    public final static byte SMALL_YELLOW_STONE = 19;
    //public final static byte MEDIUM_YELLOW_STONE = 20;
    public final static byte BIG_YELLOW_STONE = 21;

    public final static byte SMALL_WHITE_STONE = 22;
    //public final static byte MEDIUM_WHITE_STONE = 23;
    public final static byte BIG_WHITE_STONE = 24;

    public final static byte SMALL_COIN_COPPER = 25;
    public final static byte MEDIUM_COIN_COPPER = 35;
    public final static byte BIG_COIN_COPPER = 26;

    public final static byte SMALL_COIN_SILVER = 27;
    public final static byte MEDIUM_COIN_SILVER = 36;
    public final static byte BIG_COIN_SILVER = 28;

    public final static byte SMALL_COIN_GOLD = 29;
    public final static byte MEDIUM_COIN_GOLD = 37;
    public final static byte BIG_COIN_GOLD = 30;

    public final static byte SMALL_BAG = 51;
    public final static byte BIG_BAG = 52;

    public final static byte ABSTRACT_FRUIT= 40;
    public final static byte ANANAS = 41;
    public final static byte CARROT = 42;
    public final static byte APPLE = 43;
    public final static byte ORANGE = 44;
    public final static byte WATERMELON = 45;

    public final static byte SMALL_MEDICAL_KIT = 70;
    public final static byte MEDIUM_MEDICAL_KIT = 71;
    public final static byte LARGE_MEDICAL_KIT = 72;
    public final static byte SYRINGE = 73;
    public final static byte BULLETS_FOR_SHOTGUN = 75;
    public final static byte BULLETS_FOR_PISTOL = 76;
    public final static byte BULLETS_FOR_M79 = 77;
    public final static byte BULLETS_FOR_SMG = 78;
    public final static byte BULLETS_FOR_REVOLVER = 80;
    public final static byte BULLETS_FOR_HAND_GRENADE = 81;



    public final static byte HANDGUN = Weapon.HANDGUN;
    public final static byte REVOLVER = Weapon.REVOLVER;
    public final static byte SHOTGUN = Weapon.SHOTGUN;
    public final static byte SO_SHOTGUN = Weapon.SO_SHOTGUN;
    public final static byte SMG = Weapon.SMG;
    public final static byte GREENADE_LAUNCHER = Weapon.GREENADE_LAUNCHER;
    public final static byte HAND_GREENADE = Weapon.HAND_GREENADE;

    protected int valueToBeAddedByGain;
    protected boolean stopped = false;    //For moveable objects

    public static int getNormalValueForLocalObjectType(int localType) {
        //Fruit
        if (localType == ANANAS) return 15;
        else if (localType == CARROT) return 25;
        else if (localType == APPLE) return 35;
        else if (localType == ORANGE) return 45;
        else if (localType == WATERMELON) return 45;

        //Rouble
        else if (localType == SMALL_COIN_COPPER) return 1;
        else if (localType == SMALL_COIN_SILVER) return 3;
        else if (localType == SMALL_COIN_GOLD) return 6;
        //YEN
        else if (localType == MEDIUM_COIN_COPPER) return 2;
        else if (localType == MEDIUM_COIN_SILVER) return 5;
        else if (localType == MEDIUM_COIN_GOLD) return 10;
        //DOLLAR
        else if (localType == BIG_COIN_COPPER) return 4;
        else if (localType == BIG_COIN_SILVER) return 8;
        else if (localType == BIG_COIN_GOLD) return 15;

        //Gem
        else if (localType == SMALL_GREEN_STONE) return 12;
        else if (localType == BIG_GREEN_STONE) return 25;

        else if (localType == SMALL_RED_STONE) return 18;
        else if (localType == BIG_RED_STONE) return 30;

        else if (localType == SMALL_BLUE_STONE) return 25;
        else if (localType == BIG_BLUE_STONE) return 50;

        else if (localType == SMALL_YELLOW_STONE) return 35;
        else if (localType == BIG_YELLOW_STONE) return 70;

        else if (localType == SMALL_WHITE_STONE) return 50;
        else if (localType == BIG_WHITE_STONE) return 100;

        else {
            System.out.println("For this object there are no data about normal value");
            return 1;
        }

    }

    @Override
    public void draw(GameCamera gameCamera) {

    }

    protected void initStars(){
        if (inWorldPosition == IN_WORLD) {
            starsDrawingController = new ShiningStarsDrawingController(this);
        }
    }

    protected void drawStars(GameCamera gameCamera){
        if (starsDrawingController != null) {
            starsDrawingController.update();
            starsDrawingController.draw(gameCamera);
        }
        else {
            initStars();
            //if (inWorldPosition == IN_BAG) starsDrawingController = new ShiningStarsDrawingController(this);
        }
    }

    public static String getPathToMainTextureForType(byte type){
        String path = "";
        if (type == ABSTRACT_COIN) path = "Shining coin animation.png";
        else if (type == STAR) path = "Star-collectibles.png";
        else if (type == MUSHROOM) path = "Mushroom animation.png";
        else System.out.println("For this type there are no animation file");
        return path;
    }

    public abstract void update(GameRound gameRound);

    public void setInWorldPosition(boolean inWorldPosition){
        this.inWorldPosition = inWorldPosition;
    }

    public boolean getInWorldPosition(){
        return inWorldPosition;
    }

    public void setStopped(boolean flag) {
        stopped = flag;
    }

    public boolean isStopped() {
        return stopped;
    }

    public byte getType(){
        return  type;
    }

    public abstract void collisionWithBullet(Bullet bullet);

    public void deleteSpringAfterContact(){
        System.out.println("Try to delete spring");
        if (withSpring) {
            if (spring != null) {
                if (spring.mouseJoint != null){
                    Vec2 velocity = new Vec2(body.getLinearVelocity().x, body.getLinearVelocity().y);
                    float angular = body.getAngularVelocity();
                    PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                    spring.mouseJoint = null;
                    spring = null;
                    withSpring = false;
                    body.setAngularVelocity( angular);
                    body.setLinearVelocity(velocity);
                    body.resetMassData();
                    System.out.println("Spring was deleted for the collectable element; Vel: " + velocity + "; angular: " + angular);
                }
            }
        }
    }

    public void addBodyToWorld(Vec2 center){
        inWorldPosition = IN_WORLD;
        makeBody(center, (int)boundingWidth);
        //if (withSpring) spring = new Spring(this);
    }


    protected abstract void makeBody(Vec2 center, int boundingWidth);


    public final static boolean isTypeMoney(byte type){
        if (type >= SMALL_RED_STONE || type <= MEDIUM_COIN_GOLD){
            return true;
        }
        else return false;
    }

    public final static boolean isTypeCoin(byte type){
        if (type >= SMALL_COIN_COPPER || type <= MEDIUM_COIN_GOLD){
            return true;
        }
        else return false;
    }

    public final static boolean isTypeAmmo(byte type){
        if (type >= BULLETS_FOR_SHOTGUN && type <= BULLETS_FOR_HAND_GRENADE){
            return true;
        }
        else return false;
    }

    /*
    public abstract String getPrefixForGainText();

    public abstract String getSuffixForGainText();*/

    public abstract String getStringAddedToWorldByBeGained();

    public int getValueToBeAddedByGain() {
        return valueToBeAddedByGain;
    }

    public Vec2 getParentPos() {
        return parentPos;
    }

    @Override
    public String getObjectToDisplayName(){
        return OBJECT_TO_DISPLAY_NAME;
    }
}
