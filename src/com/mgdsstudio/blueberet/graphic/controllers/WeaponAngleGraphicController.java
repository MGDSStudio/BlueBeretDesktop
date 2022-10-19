package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.control.FivePartsStick;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class WeaponAngleGraphicController {
    private static final int ADD_BULLET_TO_SCREEN_SPRITE_NUMBER = 5;

    private SpriteAnimation goingBodyWithLegsAnimation;
    private StaticSprite handsAndWeaponTo0, handsAndWeaponTo315, handsAndWeaponTo45;
    private StaticSprite bodyGoTo0, bodyGoTo315, bodyGoTo45;
    private StaticSprite legsStayStaticSprite;
    private SpriteAnimation goLegsAnimation, shotBodyAnimationTo0, shotBodyAnimationTo315, shotBodyAnimationTo45, flashAnimation;
    private Human player;
    public final static int upperMaxAngle = 360-FivePartsStick.MAX_UPPER_ANGLE;
    public final static int lowerMinAngle = FivePartsStick.MAX_UPPER_ANGLE;
    public final static int criticalUpperAngle = 340;
    public final static int criticalLowerAngle = 20;
    private int weaponGraphicRotationAngleFor315 = 32;
    private final int weaponGraphicRotationAngleFor45 = 30;
    private boolean shootingAnimationStarted = false;
    private final int shotFrameToStartShotAnimationSlowly = 4;
    private boolean shotAnimationIsSlowed;
    private final int shotFrameToStartShotAnimationFaster = 7;
    private boolean shotAnimationIsFaster ;
    private boolean shotStarted;
    private final int WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP = -5;
    private final int WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD = 10;
    private final int WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN = -40;
    private boolean shotAnimationIsForEveryAngleSame = true;
    public final static int NORMAL_SHOOTING_UPDATING_FREQUENCY = 13;
    public final static int FAST_SHOOTING_UPDATING_FREQUENCY = 26;
    private boolean withSameGraphicFor45And315Sprites = true;
    private boolean stepSoundAdded;

    private ImageZoneFullData handsAndWeaponTo0ImageData;
    private ImageZoneFullData handsAndWeaponTo45WeaponData;
    private ImageZoneFullData handsAndWeaponTo315WeaponData;

    private boolean angleSpecific = true;

    private Vec2 handsAnchorFrom0ToDown, handsAnchorFrom0ToUp, handsAnchorFor315, handsAnchorFor45;
    private final Vec2 mutPos = new Vec2();

    public WeaponAngleGraphicController(Human player, int width, int height, String pathToGraphicFile, int sourceDimension, int animationFrequency) {
        this.player = player;
        createAnchors();
        initBasicData(pathToGraphicFile);
        loadImageGraphic(pathToGraphicFile, width, height, sourceDimension, animationFrequency);
    }

    private void initBasicData(String pathToGraphicFile) {
        handsAndWeaponTo0ImageData = new ImageZoneFullData(pathToGraphicFile, 0,1009, 38,1024);
        handsAndWeaponTo45WeaponData = new ImageZoneFullData(pathToGraphicFile, 59,1005, 94,1023);
        handsAndWeaponTo315WeaponData = new ImageZoneFullData(pathToGraphicFile, 203,992, 240,1020);
    }

    public WeaponAngleGraphicController() {
        createAnchors();
        int sourceSingleImageDimension = 96;
        float width = Soldier.getNormalBodyWidth()*1.07f;
        width*=(HumanAnimationController.stretchingCoef*0.7f);
        int height=(int)(width);
        String pathToGraphicFile = HumanAnimationController.getPathToActualSpritesheet(WeaponType.M79);
        initBasicData(pathToGraphicFile);
        loadImageGraphic(pathToGraphicFile, (int) width, height, sourceSingleImageDimension, HumanAnimationController.ANIMATION_FREQUENCY);
    }

    private void createAnchors(){
        float relativeHandsShiftingFor0AlongX = 12f;    //12
        float relativeHandsShiftingFor0AlongY = 0f;
        handsAnchorFrom0ToDown = new Vec2(relativeHandsShiftingFor0AlongX, relativeHandsShiftingFor0AlongY);
        relativeHandsShiftingFor0AlongY = -1f;
        handsAnchorFrom0ToUp = new Vec2(relativeHandsShiftingFor0AlongX, relativeHandsShiftingFor0AlongY);
        float relativeHandsShiftingFor315AlongX = 10f;   //8
        float relativeHandsShiftingFor315AlongY = -1f;
        handsAnchorFor315 = new Vec2(relativeHandsShiftingFor315AlongX, relativeHandsShiftingFor315AlongY);
        float relativeHandsShiftingFor45AlongX = 12f;   //12
        float relativeHandsShiftingFor45AlongY = 12f;
        handsAnchorFor45 = new Vec2(relativeHandsShiftingFor45AlongX, relativeHandsShiftingFor45AlongY);
        System.out.println("New anchors for hands!");
    }

    private void createAnchorsOld(){
        float relativeHandsShiftingFor0AlongX = 12f;    //12
        float relativeHandsShiftingFor0AlongY = 3f;
        handsAnchorFrom0ToDown = new Vec2(relativeHandsShiftingFor0AlongX, relativeHandsShiftingFor0AlongY);
        relativeHandsShiftingFor0AlongY = -1f;
        handsAnchorFrom0ToUp = new Vec2(relativeHandsShiftingFor0AlongX, relativeHandsShiftingFor0AlongY);
        float relativeHandsShiftingFor315AlongX = 10f;   //8
        float relativeHandsShiftingFor315AlongY = -1f;
        handsAnchorFor315 = new Vec2(relativeHandsShiftingFor315AlongX, relativeHandsShiftingFor315AlongY);
        float relativeHandsShiftingFor45AlongX = 12f;   //12
        float relativeHandsShiftingFor45AlongY = 11f;
        handsAnchorFor45 = new Vec2(relativeHandsShiftingFor45AlongX, relativeHandsShiftingFor45AlongY);
    }

    /*
    public ImageZoneFullData getImageDataForHandsAndWeaponTo0(String path){

        //ImageZoneFullData handsAndWeaponTo0ImageData = new ImageZoneFullData(pathToGraphicFile, 0,1009, 38,1024);
    }*/

    private void loadImageGraphic(String pathToGraphicFile, int width, int height, int sourceSingleImageDimension, int animationFrequency) {
        createFlashAnimation(width, height, animationFrequency);
        int bodyCenterOnGraphicXfor0 = 848-608;
        int bodyCenterOnGraphicYfor0 = 183-4+95-26;
        int shiftingX = 2;
        ImageZoneFullData bodyTo0ImageDataForGoing = new ImageZoneFullData(pathToGraphicFile, bodyCenterOnGraphicXfor0-(sourceSingleImageDimension/2)+shiftingX,bodyCenterOnGraphicYfor0-(sourceSingleImageDimension/2), bodyCenterOnGraphicXfor0+(sourceSingleImageDimension/2)+shiftingX,bodyCenterOnGraphicYfor0+(sourceSingleImageDimension/2));
        bodyGoTo0 =  new StaticSprite(bodyTo0ImageDataForGoing, width, height);

        //ImageZoneFullData handsAndWeaponTo0ImageData = new ImageZoneFullData(pathToGraphicFile, 0,1009, 38,1024);
        float weaponWidth = getWeaponWidth(bodyTo0ImageDataForGoing, handsAndWeaponTo0ImageData, width);
        float weaponHeight = getWeaponHeight(handsAndWeaponTo0ImageData, weaponWidth);
        handsAndWeaponTo0 =  new StaticSprite(handsAndWeaponTo0ImageData, (int)weaponWidth, (int)weaponHeight);
        int bodyCenterOnGraphicXfor315 = 660-608;
        int bodyCenterOnGraphicYfor315 = 158-10+95;
        ImageZoneFullData bodyTo315ImageDataByGoing = new ImageZoneFullData(pathToGraphicFile, bodyCenterOnGraphicXfor315-(sourceSingleImageDimension/2),bodyCenterOnGraphicYfor315-(sourceSingleImageDimension/2), bodyCenterOnGraphicXfor315+(sourceSingleImageDimension/2),bodyCenterOnGraphicYfor315+(sourceSingleImageDimension/2));
        bodyGoTo315  =  new StaticSprite(bodyTo315ImageDataByGoing, width, height);


        //ImageZoneFullData handsAndWeaponTo315WeaponData = new ImageZoneFullData(pathToGraphicFile, 203,992, 240,1020);

        weaponWidth = getWeaponWidth(bodyTo315ImageDataByGoing, handsAndWeaponTo315WeaponData, width);
        weaponHeight = getWeaponHeight(handsAndWeaponTo315WeaponData, weaponWidth);

        //handsAndWeaponTo315 = new StaticSprite(handsAndWeaponTo315WeaponData, (int)weaponWidth, (int)weaponHeight);
       


        int bodyCenterOnGraphicXfor45 = 748-608;
        int bodyCenterOnGraphicYfor45 = 158-10+95;
        ImageZoneFullData bodyTo45ImageDataForGoing = new ImageZoneFullData(pathToGraphicFile, bodyCenterOnGraphicXfor45-(sourceSingleImageDimension/2),bodyCenterOnGraphicYfor45-(sourceSingleImageDimension/2), bodyCenterOnGraphicXfor45+(sourceSingleImageDimension/2),bodyCenterOnGraphicYfor45+(sourceSingleImageDimension/2));
        bodyGoTo45  =  new StaticSprite(bodyTo45ImageDataForGoing, width, height);
        //shiftingX = 12;
        shiftingX = 0;
        weaponWidth = getWeaponWidth(bodyTo45ImageDataForGoing, handsAndWeaponTo45WeaponData, width);
        weaponHeight = getWeaponHeight(handsAndWeaponTo45WeaponData, weaponWidth);
        handsAndWeaponTo45 = new StaticSprite(handsAndWeaponTo45WeaponData, (int)weaponWidth, (int)weaponHeight);
        //ImageZoneFullData handsAndWeaponTo315WeaponData = new ImageZoneFullData(pathToGraphicFile, 59+shiftingX,1005, 94+shiftingX,1023);

        if (withSameGraphicFor45And315Sprites) {
            weaponGraphicRotationAngleFor315 = 32;
            handsAndWeaponTo315 = new StaticSprite(handsAndWeaponTo45WeaponData, (int)weaponWidth, (int)weaponHeight);
        }
        else {
            weaponGraphicRotationAngleFor315 = -15;
            handsAndWeaponTo315 = new StaticSprite(handsAndWeaponTo315WeaponData, (int)(weaponWidth*1.2f), (int)(weaponHeight*1.3f));
        }
        System.out.println("Hands and weapon for 45 and 315 is a same. It must be remade");
        int shiftingY = 4;
        AnimationDataToStore goAnimationData = new AnimationDataToStore(pathToGraphicFile, 1024-sourceSingleImageDimension*4, 2*sourceSingleImageDimension+shiftingY, 1024,3*sourceSingleImageDimension+shiftingY, (int) width, (int) height, (byte)1, (byte)4, animationFrequency);
        goLegsAnimation = new SpriteAnimation(goAnimationData);

        shiftingX = 3;
        int yStart = 3*sourceSingleImageDimension+shiftingY+110;

        AnimationDataToStore shotBodyAnimationDataFor0 = new AnimationDataToStore(pathToGraphicFile, shiftingX, yStart, sourceSingleImageDimension*8+shiftingX,shiftingY+yStart+sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)8, (int) (animationFrequency*2));
        shotBodyAnimationTo0 = new SpriteAnimation(shotBodyAnimationDataFor0);
        shiftingX = 0;
        shiftingY = 2;
        AnimationDataToStore shotBodyAnimationData45 = new AnimationDataToStore(pathToGraphicFile, shiftingX+sourceSingleImageDimension*2, yStart+shiftingY, shiftingX+sourceSingleImageDimension*6,shiftingY+yStart+sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)4, (int) (FAST_SHOOTING_UPDATING_FREQUENCY));
        shotBodyAnimationTo45 = new SpriteAnimation(shotBodyAnimationData45);
        AnimationDataToStore shotBodyAnimationData315 = new AnimationDataToStore(pathToGraphicFile, shiftingX, yStart+shiftingY, shiftingX+sourceSingleImageDimension*4,shiftingY+yStart+sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)4, (int) (FAST_SHOOTING_UPDATING_FREQUENCY));
        shotBodyAnimationTo315 = new SpriteAnimation(shotBodyAnimationData315);


        int spritesForAnimation = 5;
        int shiftingRelativeRunX = 6*sourceSingleImageDimension;
        int shiftingRelativeRunY = 4*sourceSingleImageDimension;
        AnimationDataToStore goingData = new AnimationDataToStore(pathToGraphicFile, shiftingRelativeRunX,shiftingRelativeRunY, sourceSingleImageDimension*spritesForAnimation+shiftingRelativeRunX,sourceSingleImageDimension+shiftingRelativeRunY, (int) width, (int) height, (byte)1, (byte)spritesForAnimation, NORMAL_SHOOTING_UPDATING_FREQUENCY);
        //AnimationDataToStore goingData = new AnimationDataToStore(pathToGraphicFile, shiftingX+sourceSingleImageDimension*2, yStart+shiftingY, shiftingX+sourceSingleImageDimension*6,shiftingY+yStart+sourceSingleImageDimension, (int) width, (int) height, (byte)1, (byte)4, (int) (FAST_SHOOTING_UPDATING_FREQUENCY));
        goingBodyWithLegsAnimation = new SpriteAnimation(goingData);
        goingBodyWithLegsAnimation.setUpdateFrequency((int)(HumanAnimationController.ANIMATION_FREQUENCY ));

        shiftingY = 4;
        shiftingX = 0;
        ImageZoneFullData legsForStayingSpriteData = new ImageZoneFullData(pathToGraphicFile, 1024-sourceSingleImageDimension*4+shiftingX, 2*sourceSingleImageDimension+shiftingY, 1024-sourceSingleImageDimension*3+shiftingX,3*sourceSingleImageDimension+shiftingY);
        System.out.println("Graphic data: " + legsForStayingSpriteData.toString());
        legsStayStaticSprite = new StaticSprite(legsForStayingSpriteData, width, height);



    }

    private void createFlashAnimation(int width, int height, int animationFrequency) {

    }

    public void setForShootingAnimationsFrequency(int frequency){
        shotBodyAnimationTo45.setUpdateFrequency(frequency);
        shotBodyAnimationTo315.setUpdateFrequency(frequency);
        shotBodyAnimationTo0.setUpdateFrequency(frequency);
    }

    public float getWeaponHeight(ImageZoneFullData weaponImageData, float weaponWidth) {
        float weaponGraphicWidth = weaponImageData.rightX-weaponImageData.leftX;
        float coefficient = weaponWidth/weaponGraphicWidth;
        float weaponGraphicHeight = weaponImageData.lowerY-weaponImageData.upperY;
        return weaponGraphicHeight*coefficient;
    }

    private float getWeaponWidth(ImageZoneFullData bodyImageData, ImageZoneSimpleData weaponImageData, int bodyWidth){
        float bodyGraphicWidth = bodyImageData.rightX-bodyImageData.leftX;
        float weaponGraphicWidth = weaponImageData.rightX-weaponImageData.leftX;
        float relativeValue = weaponGraphicWidth/bodyGraphicWidth;
        return relativeValue*bodyWidth;
    }

    public float getWeaponWidth(StaticSprite sprite, ImageZoneSimpleData weaponImageData, int bodyWidth){
        float bodyGraphicWidth = sprite.getxRight()-sprite.getxLeft();
        float weaponGraphicWidth = weaponImageData.rightX-weaponImageData.leftX;
        float relativeValue = weaponGraphicWidth/bodyGraphicWidth;
        return relativeValue*bodyWidth;
    }

    public void loadAnimationData(MainGraphicController mainGraphicController) {
        Tileset tileset = mainGraphicController.getTilesetUnderPath(handsAndWeaponTo0.getPath());
        handsAndWeaponTo0.loadSprite(tileset);
        handsAndWeaponTo315.loadSprite(tileset);
        handsAndWeaponTo45.loadSprite(tileset);
        bodyGoTo0.loadSprite(tileset);
        bodyGoTo315.loadSprite(tileset);
        bodyGoTo45.loadSprite(tileset);
        legsStayStaticSprite.loadSprite(tileset);
        goLegsAnimation.loadAnimation(tileset);
        shotBodyAnimationTo0.loadAnimation(tileset);
        shotBodyAnimationTo45.loadAnimation(tileset);
        shotBodyAnimationTo315.loadAnimation(tileset);
        goingBodyWithLegsAnimation.loadAnimation(tileset);
    }

    public void update(GameRound gameRound){

    }



    public void drawGoing(float weaponAngle, GameCamera gameCamera, boolean flip, boolean hasBullets, SoundInGameController soundController){
        if (angleSpecific){
            drawGoingNotForGrenades(weaponAngle,gameCamera,flip,hasBullets,soundController);
        }
        else {
            drawGoingForGrenades(weaponAngle,gameCamera,flip,hasBullets,soundController);
        }

    }

    private float getUpdatedAngle(float weaponAngle){
        float updatedAngle = weaponAngle;
        if (weaponAngle<upperMaxAngle && weaponAngle > (180+360-upperMaxAngle)){
            if (weaponAngle >270) updatedAngle = upperMaxAngle;
            else updatedAngle = (180+360-upperMaxAngle);
        }
        else{
            if (weaponAngle<(180-lowerMinAngle) && weaponAngle > (lowerMinAngle)){
                if (weaponAngle <90) updatedAngle = lowerMinAngle;
                else updatedAngle = (180-lowerMinAngle);
            }
        }
        return updatedAngle;
    }

    private void drawGoingForGrenades(float basicWeaponAngle, GameCamera gameCamera, boolean flip, boolean hasBullets, SoundInGameController soundController){
        goingBodyWithLegsAnimation.update();
        goLegsAnimation.update();
        addStepSound(goingBodyWithLegsAnimation, soundController);
        mutPos.x = player.getPixelPosition().x;
        mutPos.y = player.getPixelPosition().y;

        if (!shootingAnimationStarted) goingBodyWithLegsAnimation.draw(gameCamera, mutPos, 0f, flip);
        else {
            goLegsAnimation.draw(gameCamera, mutPos, 0f, flip);
            //System.out.println("Animation for attack started!");
            float weaponAngle = getUpdatedAngle(basicWeaponAngle);
            if (mustBe0GradAnimationShown(weaponAngle)){
                //bodyGoTo0.draw(gameCamera, player.body, flip);
                Vec2 anchor = handsAnchorFrom0ToDown;
                if (weaponAngle < 270 && weaponAngle > 90) {
                    if (weaponAngle > 180) anchor = handsAnchorFrom0ToUp;
                    if (!shootingAnimationStarted) {
                        if (!hasBullets){
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                        }
                        else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle) + PConstants.PI, flip);
                    }
                    else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
                else
                {
                    if (weaponAngle < 0 || weaponAngle > 270) anchor = handsAnchorFrom0ToUp;
                    if (!shootingAnimationStarted) {
                        if (!hasBullets){
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                        }
                        else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle), flip);
                    }
                    else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
            }
            else if (mustBe315GradAnimationShown(weaponAngle)){
                if (weaponAngle < 270 && weaponAngle > 90) {
                    if (!shootingAnimationStarted) {
                        if (!hasBullets){
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                        }
                        else {
                            handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor315) + PConstants.PI, flip);
                        }
                    }
                    else {
                        drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                    }
                }
                else {
                    if (!shootingAnimationStarted) {
                        if (!hasBullets){
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                        }
                        else {
                            handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor315), flip);
                        }
                    } else {
                        drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                    }
                }
            }
            else if (mustBe45GradAnimationShown(weaponAngle)) {
                if (weaponAngle < 270 && weaponAngle > 90) {
                    if (!shootingAnimationStarted) {
                        if (!hasBullets) {
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                        } else {
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFrom0ToDown, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                        }
                    } else drawShotAnimation(gameCamera, player.getPixelPosition(), flip, weaponAngle);
                } else {
                    if (!shootingAnimationStarted) {
                        if (!hasBullets) {
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                        } else {
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor45), flip);
                        }
                    } else drawShotAnimation(gameCamera, player.getPixelPosition(), flip, weaponAngle);

                }
            }
            }


    }

    private void drawGoingNotForGrenades(float weaponAngle, GameCamera gameCamera, boolean flip, boolean hasBullets, SoundInGameController soundController){
        goLegsAnimation.update();
        addStepSound(goLegsAnimation, soundController);
        mutPos.x = player.getPixelPosition().x;
        mutPos.y = player.getPixelPosition().y;
        goLegsAnimation.draw(gameCamera, mutPos, 0f, flip);
        if (weaponAngle<upperMaxAngle && weaponAngle > (180+360-upperMaxAngle)){
            if (weaponAngle >270) weaponAngle = upperMaxAngle;
            else weaponAngle = (180+360-upperMaxAngle);
        }
        else{
            if (weaponAngle<(180-lowerMinAngle) && weaponAngle > (lowerMinAngle)){
                if (weaponAngle <90) weaponAngle = lowerMinAngle;
                else weaponAngle = (180-lowerMinAngle);
            }
        }
        if (mustBe0GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo0.draw(gameCamera, player.body, flip);
            Vec2 anchor = handsAnchorFrom0ToDown;
            if (weaponAngle < 270 && weaponAngle > 90) {
                if (weaponAngle > 180) anchor = handsAnchorFrom0ToUp;
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                    }
                    else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle) + PConstants.PI, flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
            else
            {
                if (weaponAngle < 0 || weaponAngle > 270) anchor = handsAnchorFrom0ToUp;
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                    }
                    else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle), flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }

        }
        else if (mustBe315GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo315.draw(gameCamera, player.body, flip);
            if (weaponAngle < 270 && weaponAngle > 90) {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                    }
                    else {
                        handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor315) + PConstants.PI, flip);
                    }
                }
                else {
                    drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
            } else {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                    }
                    else {
                        handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor315), flip);
                    }
                } else {
                    drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
            }
        }
        else if (mustBe45GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo45.draw(gameCamera, player.body, flip);
            if (weaponAngle < 270 && weaponAngle > 90) {
                if (!shootingAnimationStarted) {
                    if (!hasBullets) {
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                    } else {
                        handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                    }
                } else drawShotAnimation(gameCamera, player.getPixelPosition(), flip, weaponAngle);

            } else
            if (!shootingAnimationStarted) {
                if (!hasBullets){
                    handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                }
                else {
                    handsAndWeaponTo45.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor45), flip);
                }
            }
            else drawShotAnimation(gameCamera, player.getPixelPosition(), flip ,weaponAngle);

        }
        else{
            System.out.println("*** There are no animation for this angle "  + weaponAngle);
        }
    }



    /*
    if (!shootingAnimationStarted) bodyGoTo45.draw(gameCamera, player.body, flip);
            if (weaponAngle < 270 && weaponAngle > 90) {
                if (!shootingAnimationStarted) {
                    if (!hasBullets) {
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                    }
                    else {
                        handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                    }
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                else {
                    if (!shootingAnimationStarted) {
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFrom0ToDown, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                    }
                    else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
            } else {
                if (!hasBullets){
                    handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                }
                else {
                    if (!shootingAnimationStarted) {
                        if (!hasBullets) {
                            handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor45), flip);
                        }
                        else handsAndWeaponTo45.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor45), flip);
                    }
                    else drawShotAnimation(gameCamera, player.getPixelPosition(), flip ,weaponAngle);
                }
            }
     */


    /*
    else {
                    if (!shootingAnimationStarted) {
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFrom0ToDown, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                    }
                    else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
                }
     */


    private void addStepSound(SpriteAnimation goLegsAnimation, SoundInGameController soundController) {
            if (goLegsAnimation.getActualSpriteNumber() == 2){
                if (!stepSoundAdded){
                    stepSoundAdded = true;
                    soundController.setAndPlayAudio(SoundsInGame.STEP_1);
                }
            }
            else {
                if (stepSoundAdded){
                    stepSoundAdded = false;
                }
            }

    }

    private void drawShotAnimation(GameCamera gameCamera, PVector playerPos, boolean flip, float weaponAngle){
        SpriteAnimation shotBodyAnimation = getActualShotAnimation(weaponAngle);
        shotBodyAnimation.update();
        if (shotAnimationIsForEveryAngleSame) {
            if (shotBodyAnimation.getActualSpriteNumber() == shotFrameToStartShotAnimationSlowly && !shotAnimationIsSlowed) {
                shotBodyAnimation.setUpdateFrequency((int) (shotBodyAnimation.getUpdateFrequency() / 3));
                shotAnimationIsSlowed = true;
                shotAnimationIsFaster = false;
                System.out.println("Shot animation is slowed; Frames: " + shotBodyAnimation.getSpritesNumber());
            } else if (shotBodyAnimation.getActualSpriteNumber() == shotFrameToStartShotAnimationFaster && !shotAnimationIsFaster) {
                shotBodyAnimation.setUpdateFrequency((int) (shotBodyAnimation.getUpdateFrequency() * 3));
                shotAnimationIsFaster = true;
                shotAnimationIsSlowed = false;
                System.out.println("Shot animation is faster");
            }
            if (shotBodyAnimation.isAnimationAlreadyShown()){
                shootingAnimationStarted = false;
                shotBodyAnimation.setActualSpriteNumber((byte) (shotBodyAnimation.getSpritesNumber()-1));
            }
        }
        if (shotBodyAnimation.isAnimationAlreadyShown()){
            resetAllShootingAnimations();
            Vec2 pos = new Vec2(playerPos.x, playerPos.y);
            shotBodyAnimation.drawSingleSprite(gameCamera, pos, 0, flip, (byte)shotBodyAnimation.getLastSprite());
        }
        else {
            shotBodyAnimation.draw(gameCamera,  playerPos, 0, flip);
        }
        //Vec2 pos = new Vec2(playerPos.x, playerPos.y);
        //Vec2 pos = new Vec2(playerPos.x, playerPos.y);


    }

    private void resetAllShootingAnimations(){
        shotBodyAnimationTo45.reset();
        shotBodyAnimationTo315.reset();
        shotBodyAnimationTo0.reset();
        shotBodyAnimationTo45.setActualSpriteNumber((byte) 0);
        shotBodyAnimationTo315.setActualSpriteNumber((byte) 0);
        shotBodyAnimationTo0.setActualSpriteNumber((byte) 0);
        shootingAnimationStarted = false;
        //System.out.println("Shooting animation ends");
    }

    private SpriteAnimation getActualShotAnimation(float weaponAngle) {
        if (shotAnimationIsForEveryAngleSame) return shotBodyAnimationTo0;
        else {
            if (mustBe0GradAnimationShown(weaponAngle)) {
                //System.out.println("0 grad body animation is shown");
                return shotBodyAnimationTo0;
            }
            else if (mustBe45GradAnimationShown(weaponAngle)) {
                //System.out.println("45 grad body animation is shown");
                return shotBodyAnimationTo45;
            }
            else if (mustBe315GradAnimationShown(weaponAngle)) {
                //System.out.println("315 grad body animation is shown ");
                return shotBodyAnimationTo315;
            }
            else {
                System.out.println("There are no animation data for this angle");
                return shotBodyAnimationTo0;
            }
        }
    }


    public void drawStaying(float weaponAngle, GameCamera gameCamera, boolean flip, boolean hasBullets) {
        legsStayStaticSprite.draw(gameCamera, player.body, flip);
        if (weaponAngle<upperMaxAngle && weaponAngle > (180+360-upperMaxAngle)){
            if (weaponAngle >270) weaponAngle = upperMaxAngle;
            else weaponAngle = (180+360-upperMaxAngle);
        }
        else{
            if (weaponAngle<(180-lowerMinAngle) && weaponAngle > (lowerMinAngle)){
                if (weaponAngle <90) weaponAngle = lowerMinAngle;
                else weaponAngle = (180-lowerMinAngle);
            }
        }
        if (mustBe0GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo0.draw(gameCamera, player.body, flip);
            Vec2 anchor = handsAnchorFrom0ToDown;
            if (weaponAngle < 270 && weaponAngle> 90) {
                if (!shootingAnimationStarted) {
                    if (weaponAngle > 180) anchor = handsAnchorFrom0ToUp;
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                    }
                    else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle) + PConstants.PI, flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip, weaponAngle);
            }
            else {
                if (!shootingAnimationStarted) {
                    if (weaponAngle < 0 || weaponAngle > 270) anchor = handsAnchorFrom0ToUp;
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_FORWARD), flip);
                    }
                    else handsAndWeaponTo0.draw(gameCamera, anchor, player.getPixelPosition(), -PApplet.radians(weaponAngle), flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
        }
        else if (mustBe315GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo315.draw(gameCamera, player.body, flip);
            if (weaponAngle < 270 && weaponAngle> 90) {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                    }
                    else handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor315) + PConstants.PI, flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
            else {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_UP), flip);
                    }
                    else handsAndWeaponTo315.draw(gameCamera, handsAnchorFor315, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor315), flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
        }
        else if (mustBe45GradAnimationShown(weaponAngle)){
            if (!shootingAnimationStarted) bodyGoTo45.draw(gameCamera, player.body, flip);
            if (weaponAngle < 270 && weaponAngle> 90) {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(-WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                    }
                    else handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(weaponAngle + weaponGraphicRotationAngleFor45) + PConstants.PI, flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
            else {
                if (!shootingAnimationStarted) {
                    if (!hasBullets){
                        handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(+WEAPON_WITHOUT_BULLETS_ANGLE_FOR_DOWN), flip);
                    }
                    else handsAndWeaponTo45.draw(gameCamera, handsAnchorFor45, player.getPixelPosition(), -PApplet.radians(weaponAngle - weaponGraphicRotationAngleFor45), flip);
                }
                else drawShotAnimation(gameCamera, player.getPixelPosition(), flip,weaponAngle);
            }
        }
    }

    private boolean mustBe315GradAnimationShown(float weaponAngle){
        if (((weaponAngle >= upperMaxAngle) && weaponAngle <= (criticalUpperAngle) )){
            return true;
        }
        else {
            if ((weaponAngle >= (180+360-criticalUpperAngle) && (weaponAngle <= (180+360-upperMaxAngle)))  ){
                return true;
            }
            else return false;
        }
    }

    private boolean mustBe45GradAnimationShown(float weaponAngle){
        if ((weaponAngle > upperMaxAngle && weaponAngle <= (360-criticalUpperAngle) || (weaponAngle < lowerMinAngle && weaponAngle >=criticalLowerAngle))){
            return true;
        }
        else {
            if ((weaponAngle > (180-lowerMinAngle) && (weaponAngle <= (180-(criticalLowerAngle)))) ||  (weaponAngle > (180+criticalUpperAngle) && (weaponAngle <= (180+360-criticalUpperAngle)))  ){
                return true;
            }
            else return false;
        }
    }

    private boolean mustBe0GradAnimationShown(float weaponAngle){
        if ((weaponAngle > criticalUpperAngle && weaponAngle <= 360) || (weaponAngle < criticalLowerAngle && weaponAngle >=0)){
            return true;
        }
        else {
            if ((weaponAngle > (180-criticalLowerAngle) && (weaponAngle < (180+360-criticalUpperAngle)))){
                return true;
            }
            else return false;
        }
    }

    public SpriteAnimation getShotBodyAnimationTo0() {
        return shotBodyAnimationTo0;
    }

    public SpriteAnimation getShotBodyAnimationTo45() {
        return shotBodyAnimationTo45;
    }

    public SpriteAnimation getShotBodyAnimationTo315() {
        return shotBodyAnimationTo315;
    }

    public boolean isShotStarted() {
        return shotStarted;
    }

    public void setShotStarted() {
        shotStarted = true;
        shootingAnimationStarted = true;
        shotBodyAnimationTo45.reset();
        shotBodyAnimationTo315.reset();
        shotBodyAnimationTo0.reset();

    }

    public boolean isPlayerShooting(){
        if (shootingAnimationStarted) return true;
        else return false;
    }

    public boolean isShotAnimationIsForEveryAngleSame() {
        return shotAnimationIsForEveryAngleSame;
    }

    public void setShotAnimationIsForEveryAngleSame(boolean shotAnimationIsForEveryAngleSame) {
        this.shotAnimationIsForEveryAngleSame = shotAnimationIsForEveryAngleSame;
    }

    public SpriteAnimation getGoLegsAnimation() {
        return goLegsAnimation;
    }

    public ImageZoneFullData getHandsAndWeaponTo0ImageData() {
        return handsAndWeaponTo0ImageData;
    }

    public ImageZoneFullData getHandsAndWeaponTo45WeaponData() {
        return handsAndWeaponTo45WeaponData;
    }

    public ImageZoneFullData getHandsAndWeaponTo315WeaponData() {
        return handsAndWeaponTo315WeaponData;
    }

    public Vec2 getHandsAnchorFrom0ToDown() {
        return handsAnchorFrom0ToDown;
    }

    public Vec2 getHandsAnchorFrom0ToUp() {
        return handsAnchorFrom0ToUp;
    }

    public Vec2 getHandsAnchorFor315() {
        return handsAnchorFor315;
    }

    public Vec2 getHandsAnchorFor45() {
        return handsAnchorFor45;
    }

    public void setWithAngleSpecificGraphic(boolean flag) {
        angleSpecific= flag;
    }

    public boolean isAngleSpecific() {
        return angleSpecific;
    }

    public SpriteAnimation getShotBodyAnimationForActualAngle(float weaponAngle) {
        System.out.println("Actual angle: " + shotBodyAnimationTo0.getActualSpriteNumber() + ", " + shotBodyAnimationTo45.getActualSpriteNumber() + ", " + shotBodyAnimationTo315.getActualSpriteNumber());
        if (mustBe0GradAnimationShown(weaponAngle)) return shotBodyAnimationTo0;
        else if (mustBe315GradAnimationShown(weaponAngle)) return shotBodyAnimationTo315;
        else if (mustBe45GradAnimationShown(weaponAngle) ) return shotBodyAnimationTo45;
        else {
            System.out.println("No data about animation for this angle");
            return null;
        }

    }

    public int getActualSpriteForShootingAnimation() {
        if (shotBodyAnimationTo315.getActualSpriteNumber()>0) return shotBodyAnimationTo315.getActualSpriteNumber();
        else if (shotBodyAnimationTo0.getActualSpriteNumber()>0) return shotBodyAnimationTo0.getActualSpriteNumber();
        else if (shotBodyAnimationTo45.getActualSpriteNumber()>0) return shotBodyAnimationTo45.getActualSpriteNumber();
        else {
            return 0;
        }
    }

    public void playerRun() {
        if (!angleSpecific){

            if (shotStarted) {

            }
            if (shootingAnimationStarted){
                shootingAnimationStarted = false;
                shotStarted = false;
                if (player.getActualWeapon().getWeaponType() == WeaponType.GRENADE) {
                    boolean grenadeAlreadyThrown = false;
                    if (shotBodyAnimationTo315.getActualSpriteNumber()>= PlayerGreenadeAnimationController.SPRITE_FOR_GRENADE_ADDING){
                        //System.out.println("Player thrown a grenade under 315");
                        grenadeAlreadyThrown = true;
                    }
                    else if (shotBodyAnimationTo45.getActualSpriteNumber()>= PlayerGreenadeAnimationController.SPRITE_FOR_GRENADE_ADDING){
                        //System.out.println("Player thrown a grenade under 45");
                        grenadeAlreadyThrown = true;
                    }
                    else if (shotBodyAnimationTo0.getActualSpriteNumber()>= PlayerGreenadeAnimationController.SPRITE_FOR_GRENADE_ADDING){
                       // System.out.println("Player thrown a grenade under 0");
                        grenadeAlreadyThrown = true;
                    }
                    else{
                        //System.out.println("The grenade was not thrown yet");
                    }
                    if (grenadeAlreadyThrown){

                    }
                    else {
                        player.getActualWeapon().setMagazineCapacity(player.getActualWeapon().getMaxMagazineCapacity());
                    }
                    //player.getActualWeapon().fillMagazine();
                    //player.getActualWeapon().setMagazineCapacity(player.getActualWeapon().getMaxMagazineCapacity());
                    //System.out.println("Ammo recovered");
                }
            }
            shotBodyAnimationTo315.setActualSpriteNumber((byte) 0);
            shotBodyAnimationTo45.setActualSpriteNumber((byte) 0);
            shotBodyAnimationTo0.setActualSpriteNumber((byte) 0);
        }
    }
}
