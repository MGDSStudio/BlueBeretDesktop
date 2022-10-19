package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.controllers.WeaponAngleGraphicController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;
import processing.core.PVector;

public class WeaponAngleChangingAnimation extends AbstractGirlAnimation implements IAnimationTypes{
    //private StaticSprite handsAndWeaponTo0, handsAndWeaponTo315, handsAndWeaponTo45;

    public final static int WAIT_BEFORE = 0;
    public final static int ANGLE_CHANGING = 1;
    public final static int WAIT_AFTER = 2;
    private float actualAngle;
    //private Soldier soldier;
    private final int timeOfStage;
    private float deltaAngle;
    private WeaponAngleGraphicController weaponAngleGraphicController;
    private final int UPPER_ZONE = 0;
    private final int CENTRAL_ZONE = 1;
    private final int LOWER_ZONE = 2;
    private Vec2 handsAnchorFrom0ToDown, handsAnchorFrom0ToUp, handsAnchorFor315, handsAnchorFor45;

    public WeaponAngleChangingAnimation(int x, int y, int time, SoundInGameController soundInGameController) {
        super(x, y, 3, soundInGameController);
        this.timeOfStage = time;
        init();
    }


    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 5, 4, 5, null, HumanAnimationController.IDLE_ANIMATION_FREQUENCY);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 1, 3, tileset);    //Weapon up
        initSprite(path, 3, 3, tileset);    //Weapon forward
        initSprite(path, 2, 3, tileset);    //Weapon down
        initHandsSprites(path, tileset);
        updateCriticalAngles();
        weaponAngleGraphicController = new WeaponAngleGraphicController();
        updateVisibilityRelativeToStage();


    }

    private void initHandsSprites(String path, Tileset tileset) {
        WeaponAngleGraphicController weaponAngleGraphicController = new WeaponAngleGraphicController();
        handsAnchorFor45 = weaponAngleGraphicController.getHandsAnchorFor45();
        handsAnchorFor315 = weaponAngleGraphicController.getHandsAnchorFor315();
        handsAnchorFrom0ToDown = weaponAngleGraphicController.getHandsAnchorFrom0ToDown();
        handsAnchorFrom0ToUp = weaponAngleGraphicController.getHandsAnchorFrom0ToUp();

        ImageZoneFullData handsAndWeaponTo315ImageData = weaponAngleGraphicController.getHandsAndWeaponTo315WeaponData();
        ImageZoneFullData handsAndWeaponTo0ImageData = weaponAngleGraphicController.getHandsAndWeaponTo0ImageData();
        ImageZoneFullData handsAndWeaponTo45ImageData = weaponAngleGraphicController.getHandsAndWeaponTo45WeaponData();

        //ImageZoneFullData data = new ImageZoneFullData(path, cellX1* spriteDim,cellY1* spriteDim,(cellX1+1)* spriteDim,(cellY1+1)* spriteDim);
        //weaponAngleGraphicController.getHandsAndWeaponTo45WeaponData();


        //ImageZoneFullData data = new ImageZoneFullData(path, handsAndWeaponTo0ImageData);
        float girlDimension = girlDim;
        float weaponWidth = weaponAngleGraphicController.getWeaponWidth(sprites.get(0), handsAndWeaponTo315ImageData, (int) (girlDimension));
        float weaponHeight = weaponAngleGraphicController.getWeaponHeight(handsAndWeaponTo315ImageData, weaponWidth);
        StaticSprite staticSpriteTo315 = new StaticSprite(handsAndWeaponTo315ImageData,  (int)weaponWidth, (int)weaponHeight);
        if (tileset != null) staticSpriteTo315.loadSprite(tileset);
        else staticSpriteTo315.loadSprite();
        sprites.add(staticSpriteTo315);
        weaponWidth = weaponAngleGraphicController.getWeaponWidth(sprites.get(1), handsAndWeaponTo0ImageData, (int) (girlDimension));
        weaponHeight = weaponAngleGraphicController.getWeaponHeight(handsAndWeaponTo0ImageData, weaponWidth);
        StaticSprite staticSpriteTo0 = new StaticSprite(handsAndWeaponTo0ImageData,  (int)weaponWidth, (int)weaponHeight);
        if (tileset != null) staticSpriteTo0.loadSprite(tileset);
        else staticSpriteTo0.loadSprite();
        sprites.add(staticSpriteTo0);
        weaponWidth = weaponAngleGraphicController.getWeaponWidth(sprites.get(2), handsAndWeaponTo45ImageData, (int) (girlDimension));
        weaponHeight = weaponAngleGraphicController.getWeaponHeight(handsAndWeaponTo45ImageData, weaponWidth);
        StaticSprite staticSpriteTo45 = new StaticSprite(handsAndWeaponTo45ImageData,  (int)weaponWidth, (int)weaponHeight);
        if (tileset != null) staticSpriteTo45.loadSprite(tileset);
        else staticSpriteTo45.loadSprite();
        sprites.add(staticSpriteTo45);
    }

    private void updateCriticalAngles() {

        //public final static int criticalUpperAngle = 340;
        //public final static int criticalLowerAngle = 20;
    }

    @Override
    public void update(){
        updateAngle();
        super.update();
        updateSubsprite();
    }

    private void updateSubsprite() {
        if (actualStage == ANGLE_CHANGING) {
            if (isAngleInZone(UPPER_ZONE)) {
                showSpritesWithNumbers(0,3);
            } else if (isAngleInZone(CENTRAL_ZONE)) {
                showSpritesWithNumbers(1,4);
            } else if (isAngleInZone(LOWER_ZONE)) {
                showSpritesWithNumbers(2,5);
            }
            //System.out.println("Zone " + isAngleInZone(UPPER_ZONE) + "," + isAngleInZone(CENTRAL_ZONE) + "," + isAngleInZone(LOWER_ZONE) + " by angle: " + actualAngle);
        }
    }

    private void showSpritesWithNumbers(int s1, int s2){
        for (int i = 0; i < sprites.size(); i++){
            if (i==s1 || i == s2){
                sprites.get(i).makeVisible();
            }
            else sprites.get(i).hide();
        }
    }

    private void updateAngle() {
        if (actualStage == ANGLE_CHANGING){
            if (prevStage != ANGLE_CHANGING) {
                if (timer != null) {
                    timer.setNewTimer(timeOfStage);
                }
                else timer = new Timer(timeOfStage);
            }
            if (timer != null) {
                float relativeTime = 1f - ((float) (timer.getRestTime())) / (float) timer.getInstalledTime();
                float deltaAngle = 360-WeaponAngleGraphicController.upperMaxAngle+WeaponAngleGraphicController.lowerMinAngle;
                float relativeMaxAngle = deltaAngle*relativeTime;
                actualAngle = WeaponAngleGraphicController.upperMaxAngle+relativeMaxAngle;
                if (actualAngle>360) actualAngle-=360;
                //System.out.println("Actual angle: " + actualAngle);
            }

        }

    }

    @Override
    public void updateVisibilityRelativeToStage() {
        if (actualStage == WAIT_BEFORE || actualStage == WAIT_AFTER){
            for (StaticSprite staticSprite : sprites) {
                staticSprite.hide();
            }
            animations.get(0).makeVisible();
        }
        else {
            animations.get(0).hide();
        }
    }





    private boolean isAngleInZone(int zone) {
        if (zone == UPPER_ZONE){
            if (actualAngle>= WeaponAngleGraphicController.upperMaxAngle && actualAngle <= WeaponAngleGraphicController.criticalUpperAngle) return true;
            else return false;
        }
        else if (zone == CENTRAL_ZONE){
            if ((actualAngle > WeaponAngleGraphicController.criticalUpperAngle && actualAngle <= 360) || (actualAngle>=0 && actualAngle <= WeaponAngleGraphicController.criticalLowerAngle)) return true;
            else return false;
        }
        else if (zone == LOWER_ZONE){
            if (actualAngle>WeaponAngleGraphicController.criticalLowerAngle && actualAngle < 90) return true;
            else return false;
        }
        else return false;
    }

    @Override
    public boolean mustBeControlButtonFlipped() {
        return false;
    }

    @Override
    public void draw(PGraphics graphics){
        float flipValue = 1;
        if (flip) flipValue = -1;
        for (SpriteAnimation animation : animations){
            animation.draw(graphics, x,y, flipValue);
        }
        for (int i = 0; i < sprites.size(); i++){
            if (i<= 2) sprites.get(i).draw(graphics, x,y, flipValue);
            else drawSpriteForScpecificAnchor(sprites.get(i), graphics, flipValue);
        }
    }

    private void drawSpriteForScpecificAnchor(StaticSprite staticSprite, PGraphics graphics, float flipValue) {
        float x = 0;
        float y = 0;

        //float coef = (float)girlDim/Soldier.bodyWidth;
        float coef = (float)1f;
        if (isAngleInZone(UPPER_ZONE)){
            x+=(handsAnchorFor315.x*coef);
            y+=(handsAnchorFor315.y*coef);
        }
        else if (isAngleInZone(CENTRAL_ZONE)){
            x+=(handsAnchorFrom0ToDown.x*coef);
            y+=(handsAnchorFrom0ToDown.y*coef);
        }
        else if (isAngleInZone(LOWER_ZONE)){
            x+=(handsAnchorFor45.x*coef);
            y+=(handsAnchorFor45.y*coef);
        }

        //x = 0;
        //y = 0;
        //PApplet.radians(actualAngle)
        staticSprite.draw(graphics, new Vec2(x,y), new PVector(this.x, this.y), 0f, flip);
        //staticSprite.draw(graphics, x,y, flipValue);
        //aw(GameCamera gameCamera, Vec2 anker, PVector pos, float a, boolean flip) {


    }
}
