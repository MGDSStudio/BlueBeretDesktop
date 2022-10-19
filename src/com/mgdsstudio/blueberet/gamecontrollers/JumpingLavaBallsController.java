package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.LavaBall;
import com.mgdsstudio.blueberet.gameobjects.RoundCircle;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class JumpingLavaBallsController {
    private LavaBall lavaBall;
    private Flag startPos;
    final private Vec2 velocity;
    final private int normalTimeBetweenBallsGeneration;
    final private static float NORMAL_TIME_DISPERSION = 0.15f;
    private float timeDispersion = NORMAL_TIME_DISPERSION;
    private Timer timer;
    private boolean sleeped = false;
    //boolean ballWasAlreadyGenerated = false;
    //boolean ballIsNotLeavedStartPos = true;

    public JumpingLavaBallsController(Flag startPos, Vec2 velocity, int normalTimeBetweenBallsGeneration){
        this.startPos = startPos;
        this.velocity = PhysicGameWorld.controller.vectorPixelsToWorld(velocity);
        this.normalTimeBetweenBallsGeneration = normalTimeBetweenBallsGeneration;
        //appearingPosition = new Vec2();
        //boolean
    }

    public int getBoundingWidth(){
        return (int)startPos.getWidth();
    }

    public int getBoundingHeight(){
        return (int)startPos.getHeight();
    }

    public void setTimeDispersion(float timeDispersion){
        this.timeDispersion = timeDispersion;
    }

    public void updateOnScreenStatement(GameCamera gameCamera){
        if (Program.engine.frameCount % 2 == 0) {
            System.out.println("This func makes many garbage");
            if (GameMechanics.isIntersectionBetweenTwoRectsByCorners((new Vec2(startPos.getPosition().x-startPos.getWidth()/2, startPos.getPosition().y-startPos.getHeight()/2)), (int) startPos.getWidth(), (int) startPos.getHeight(), new Vec2(gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE), gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE)), (int) (gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)-gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)), (int) (gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE)))) {
                //System.out.println("On screen");
                if (sleeped) sleeped = false;
                //Vec2 firstLeftUpperCorner, int firstWidth, int firstHeight, Vec2 secondLeftUpperCorner, int secondWidth, int secondHeight
            }
            else {
                if (!sleeped) sleeped = true;
            }
        }
    }

    public boolean isSleeped(GameCamera gameCamera){
        updateOnScreenStatement(gameCamera);
        return sleeped;
    }

    private void loadImageData(RoundCircle roundCircle, String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        //System.out.println("Sprites was loaded for " + this.getClass());
        roundCircle.loadImageData(path, xLeft, yLeft, xRight, yRight, width,  height);
    }

    private void loadAnimationData(RoundCircle roundCircle){
        //System.out.println("Sprites was loaded for " + this.getClass());
        roundCircle.loadAnimationData( "Dragon fire animation.png", (int) 0, (int) 0, (int) 336, (int) 318, (int)LavaBall.MAX_TRACK_LENGTH, (int) 40, (byte)3, (byte)2, (int) 33);
        roundCircle.getSpriteAnimation().setSpritesShifting(new Vec2(12,0));
    }


    public void update(GameRound gameRound){
        //System.out.println("Slepped " + sleeped);
        if (timer == null) timer = new Timer(normalTimeBetweenBallsGeneration);
        else {
            if (timer.isTime()){
                if (!sleeped) {
                    lavaBall = new LavaBall(new Vec2(startPos.getPosition().x, startPos.getPosition().y), (int) 10, (int) 5, false, BodyType.DYNAMIC);
                    lavaBall.body.applyLinearImpulse(velocity, lavaBall.body.getPosition(), true);
                    lavaBall.saveMaxTrackVelocity();
                    loadImageData(lavaBall, "Tileset4.png", (int) 111, (int) 175, (int) 128, (int) 192, (int) 20, (int) 20);
                    loadAnimationData(lavaBall);
                    lavaBall.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(lavaBall.getSprite().getPath()));
                    lavaBall.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(lavaBall.getSpriteAnimation().getPath()));
                    gameRound.addNewRoundElement(lavaBall);
                    timer.setNewTimer((int) Program.engine.random(-timeDispersion * normalTimeBetweenBallsGeneration, timeDispersion * normalTimeBetweenBallsGeneration) + normalTimeBetweenBallsGeneration);
                }
                else{
                    timer.setNewTimer((int) Program.engine.random(-timeDispersion * normalTimeBetweenBallsGeneration, timeDispersion * normalTimeBetweenBallsGeneration) + normalTimeBetweenBallsGeneration);

                }
            }
        }
    }
}
