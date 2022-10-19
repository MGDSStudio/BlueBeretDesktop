package com.mgdsstudio.blueberet.graphic.splashes;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.BossBoar;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class JumpSplash extends Splash implements IDrawable {
    private static int addedSplashes = 0;
    protected SpriteAnimation animation;
    //private  static AnimationDataToStore animationDataToStore = new AnimationDataToStore(HeadsUpDisplay.mainGraphicSource.getPath(), new int[] {947, 193}, new int[] {967, 193+6*28}, 25*1.4f/2f, nominalHeight, (byte)6,(byte)1, frequency);
    private  static AnimationDataToStore animationDataToStore;


    private static Tileset animationTileset;
    //private Vec2 rightCenterPos;
    private boolean single = true;
    private float deltaXForDoubleSplashes = 0;

    private final static int yStep = 28;
    private final static ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(947, 193, 967, 193+6*yStep);
    private final static  int[] leftUpper = {(int) imageZoneSimpleData.leftX, (int) imageZoneSimpleData.upperY};
    private final static int[] rightLower = {(int) imageZoneSimpleData.rightX, (int) imageZoneSimpleData.lowerY};


    public JumpSplash(GameObject gameObject){
        //super(gameObject.body, new Vec2(0, gameObject.getHeight()/2), (int)0, STATIC);
        super(gameObject.body, new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body).x, PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body).y), (int)0, STATIC);
        uploadAnimationTileset();
        initGraphic(gameObject);
        setJumpY(gameObject);

        animation.setTint(Program.engine.color(255,255,255,200));
        layer = IndependentOnScreenStaticSprite.BEHIND_ALL;
        if (Program.debug){
            //addedSplashes++;
            //System.out.println("Added splashes: " + addedSplashes + "; Las is on: " + (int) basicSplashPos.x + "x" + (int) basicSplashPos.y);
        }
    }

    protected void uploadAnimationTileset() {
        if (animationTileset == null) animationTileset = HeadsUpDisplay.mainGraphicTileset;
    }




    private void initGraphic(GameObject gameObject) {
        if (animationDataToStore == null) {
            int nominalWidth = (int) (gameObject.getWidth() * 1.4f / 2f);
            float yStretchingCoef = 0.8f;
            int nominalHeight = (int) (yStretchingCoef * nominalWidth * (((rightLower[1] - leftUpper[1]) / 8f) / (rightLower[0] - leftUpper[0])));
            int frequency = 33 * 1;
            animationDataToStore = new AnimationDataToStore(HeadsUpDisplay.mainGraphicTileset.getPath(), leftUpper, rightLower, nominalWidth, nominalHeight, (byte) 6, (byte) 1, frequency);
        }
        //animationTileset = new Tileset((byte)1, animationDataToStore.getPath(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getRowsNumber());

        animation = new SpriteAnimation(animationDataToStore.getPath(), animationDataToStore.getLeftUpperCorner()[0], animationDataToStore.getLeftUpperCorner()[1], animationDataToStore.getRightLowerCorner()[0], animationDataToStore.getRightLowerCorner()[1], animationDataToStore.getGraphicWidth(), animationDataToStore.getGraphicHeight(), animationDataToStore.getRowsNumber(), animationDataToStore.getCollumnsNumber(), animationDataToStore.getFrequency() );
        animation.loadAnimation(animationTileset);
        if (splashFallingTimer == null) splashFallingTimer = new Timer(TIME_TO_FALL);
        else splashFallingTimer.setNewTimer(TIME_TO_FALL);
        //layer = IndependentOnScreenAnimation.IN_FRONT_OF_ALL;
        //correctPositions(gameObject);

    }




    private void reset(GameObject gameObject){
        initBasicData(gameObject.body, new Vec2(PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body).x, PhysicGameWorld.controller.getBodyPixelCoord(gameObject.body).y), (int)0, STATIC);
        initSecondaryData();
        resetAnimationData();
        setJumpY(gameObject);
    }

    private void setJumpY(GameObject gameObject) {
        if (gameObject instanceof Person){
            Person person = (Person) gameObject;
            float pixelDistToSplash = person.getPixelDistToSplash();
            basicSplashPos.y+= pixelDistToSplash;
            if (gameObject instanceof BossBoar){
                single = false;
                deltaXForDoubleSplashes = person.getPersonWidth()/2;
                //float xDist =
                secondarySplashPos = new Vec2(basicSplashPos.x, basicSplashPos.y);
                basicSplashPos.x-=deltaXForDoubleSplashes/2;
                secondarySplashPos.x+=deltaXForDoubleSplashes/2;
            }
        }
    }

    private void resetAnimationData() {
        if (animation != null) {
            animation.reset();
            animation.setAnimationStatement(SpriteAnimation.ACTIVE);
        }
        ended = false;
    }

    private void initSecondaryData(){
        if (splashFallingTimer == null) splashFallingTimer = new Timer(TIME_TO_FALL);
        else splashFallingTimer.setNewTimer(TIME_TO_FALL);
    }

    private void correctPositions(GameObject gameObject) {
        //rightCenterPos = new Vec2(basicSplashPos.x, basicSplashPos.y);
        /*float distanceBetweenSplashes = gameObject.getWidth()/2;
        centerPos.x-=distanceBetweenSplashes;
        rightCenterPos.x+=distanceBetweenSplashes;*/
    }

    /*
    private void resetAnimationData() {
        animation.reset();
        animation.setAnimationStatement(SpriteAnimation.ACTIVE);
        ended = false;
        System.out.println("Animation was reset");
    }*/



    protected void updateEndStatement() {
        if (!ended) {
            if (animation.isAnimationAlreadyShown()){
                animation.setAnimationStatement(SpriteAnimation.PAUSED);
                ended = true;
            }
        }
    }


    @Override
    public void draw(GameCamera gameCamera) {
        animation.update();
        updateEndStatement();
        if (!ended) {
            if (single){
                animation.draw(gameCamera, basicSplashPos, (int)0, false);
            }
            else {
                animation.draw(gameCamera, basicSplashPos, (int)0, false);
                animation.draw(gameCamera, secondarySplashPos, (int)0, false);
            }
            //float angle = animationGraphicAngle - bulletAngle + PApplet.degrees(attackedBody.getAngle()) - bodyStartAngle;

            //animation.draw(gameCamera, rightCenterPos, (int)0, true);
        }
    }


    public void recreate(GameObject gameObject) {
        reset(gameObject);
    }
}
