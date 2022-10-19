package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class AbstractGirlAnimation implements IAnimationTypes {
    protected int type;
    protected int timeToNextStage;
    protected Timer timer;
    protected Coordinate start, end, actualHandPos;
    protected int actualStage, prevStage;
    protected int stagesNumber;
    protected final int spriteDim = 96;
    protected int girlDim;
    protected ArrayList <SpriteAnimation> animations;
    protected ArrayList <StaticSprite> sprites;
    protected final String path = "How to play menu graphic" + TextureDecodeManager.getExtensionForSpriteGraphicFile();
    protected int x,y;
    protected boolean flip = true;
    protected boolean mainSoundStartedOnThisLoop, secondarySoundStartedOnThisLoop;
    protected SoundInGameController soundInGameController;

    public AbstractGirlAnimation(int x, int y, int stagesNumber, SoundInGameController soundInGameController) {
        this.x = x;
        this.y = y;
        this.stagesNumber = stagesNumber;
        animations = new ArrayList<>();
        sprites = new ArrayList<>();
        girlDim = (int)((float)Program.engine.width/(float)Program.DEBUG_DISPLAY_WIDTH) * HumanAnimationController.NORMAL_SPRITE_DIMENSION;
        System.out.println("Girl dimension is " + girlDim);
        this.soundInGameController = soundInGameController;
    }

    /*
    public AbstractGirlAnimation(int x, int y, int stagesNumber) {
        this.x = x;
        this.y = y;
        this.stagesNumber = stagesNumber;
        animations = new ArrayList<>();
        sprites = new ArrayList<>();
        girlDim = (int)((float)Program.engine.width/(float)Program.DEBUG_DISPLAY_WIDTH) *PlayerAnimationController.NORMAL_SPRITE_DIMENSION;
        System.out.println("Girl dimension is " + girlDim);
        soundInGameController = new SoundInGameController(2);
    }
     */



    public void goToNextStage(){
        actualStage++;
        if (actualStage > stagesNumber){
            actualStage = 0;
        }
    }

    protected void initAnimation(String path, int cellX1, int cellY1, int cellX2, int cellY2, Tileset tileset){
        initAnimation(path, cellX1, cellY1, cellX2, cellY2, tileset, HumanAnimationController.ANIMATION_FREQUENCY);
    }

    protected void initAnimation(String path, int cellX1, int cellY1, int cellX2, int cellY2, Tileset tileset, int frequency){
        int rows = cellY2-cellY1+1;
        int collumns = cellX2-cellX1+1;

        ImageZoneSimpleData data = new ImageZoneSimpleData(cellX1* spriteDim,cellY1* spriteDim,(cellX2+1)* spriteDim,(cellY2+1)* spriteDim);
        AnimationDataToStore animationDataToStore = new AnimationDataToStore(path, data.leftX,data.upperY,data.rightX, data.lowerY, girlDim, girlDim, (byte)rows, (byte)collumns, frequency);
        System.out.println("Graphic data: " + data);
        SpriteAnimation spriteAnimation = new SpriteAnimation(animationDataToStore);
        if (tileset != null) spriteAnimation.loadAnimation(tileset);
        else spriteAnimation.loadAnimation();
        animations.add(spriteAnimation);
        recolorBeret(spriteAnimation.getImage());
    }

    private void recolorBeret(Image image){
        BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(Program.engine);
        beretColorLoadingMaster.loadData();
        TwiceColor color = beretColorLoadingMaster.getBeretColor();
        BeretColorChanger beretColorChanger = new BeretColorChanger();
        beretColorChanger.changeColor(color, image);
    }

    protected void initSprite(String path, int cellX1, int cellY1, Tileset tileset){

        ImageZoneFullData data = new ImageZoneFullData(path, cellX1* spriteDim,cellY1* spriteDim,(cellX1+1)* spriteDim,(cellY1+1)* spriteDim);
        StaticSprite staticSprite = new StaticSprite(data,  girlDim, girlDim);
        if (tileset != null) staticSprite.loadSprite(tileset);
        else staticSprite.loadSprite();
        sprites.add(staticSprite);
        recolorBeret(sprites.get(0).getImage());
    }

    public void update(){
        for (SpriteAnimation animation : animations){
            animation.update();
        }
        prevStage = actualStage;
    }

    public void draw(PGraphics graphics){
        float flipValue = 1;
        if (flip) flipValue = -1;
        for (SpriteAnimation animation : animations){
            animation.draw(graphics, x,y, flipValue);

        }
        for (StaticSprite staticSprite : sprites){
            staticSprite.draw(graphics, x,y, flipValue);
        }
    }

    public abstract void updateVisibilityRelativeToStage() ;

    public void setStage(int actualStage) {
        this.actualStage = actualStage;
        updateVisibilityRelativeToStage();
        System.out.println("Stage: " + actualStage);
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public void reverseFlip(boolean flip) {
        if (flip) flip = false;
        else flip = true;
    }

    protected void resetSound(){
        if (mainSoundStartedOnThisLoop){
            mainSoundStartedOnThisLoop  = false;
            System.out.println("Sound was stopped");
            //soundInGameController.stopAllAudio();
        }
    }

    public boolean isFlip() {
        return flip;
    }

    public abstract boolean mustBeControlButtonFlipped();
}
