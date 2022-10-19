package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

public class JumpAnimation extends AbstractSingleSprites{
    private final int STAY = 0;
    private final int JUMP_UP = 1;
    private final int JUMP_DOWN = 2;
    private final int LANDED = 3;
    private final int WAIT_AFTER_LANDING = 4;
    //private SpriteByJumpHiddingMaster spriteByJumpHiddingMaster;
    private SpriteByJumpMovingMaster spriteByJumpMovingMaster;

    public JumpAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y,4, soundInGameController);
        setFlip(false);
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 3, 1, 4, 1, null);
        animations.get(0).setAnimationStatement(SpriteAnimation.SWITCHED_OFF);
        Tileset tileset = animations.get(0).getTileset();
        for (int i = 0; i < (WAIT_AFTER_LANDING+1); i++){
            initSprite(path, i, 1, tileset);
        }
        updateVisibilityRelativeToStage();
        spriteByJumpMovingMaster = new SpriteByJumpMovingMaster(y, Program.engine.width/6f, 1000);
    }


    @Override
    public void updateVisibilityRelativeToStage() {
        makeSpriteVisible(actualStage);
    }

    private void makeSpriteVisible(int numberToBeVisible){
        for (int i = 0; i < sprites.size(); i++){
            if (i == numberToBeVisible){
                sprites.get(i).makeVisible();
            }
            else sprites.get(i).hide();
        }
        //updateSpriteHiddingMaster();
    }

    private void updateSpriteHiddingMaster() {
        if (actualStage != prevStage){
            if (actualStage == JUMP_UP){
                spriteByJumpMovingMaster.startToHide();
            }
            else if (actualStage == JUMP_DOWN){
                spriteByJumpMovingMaster.startToAppear();
            }
            else spriteByJumpMovingMaster.stop();
        }
        spriteByJumpMovingMaster.update(actualStage);

        //if (actualStage == 0)
    }

    @Override
    public void update(){
        updateSpriteHiddingMaster();
        if (actualStage == 0) resetSound();
        else if (actualStage == JUMP_UP){
            addMainSound();
        }
        else if (actualStage > JUMP_DOWN){
            addSecondarySound();
        }
        super.update();
    }

    @Override
    protected void resetSound(){
        super.resetSound();
        if (secondarySoundStartedOnThisLoop){
            secondarySoundStartedOnThisLoop  = false;
            //System.out.println("Sound was stopped");
            //soundInGameController.stopAllAudio();
        }
    }

    protected void addSecondarySound(){
        if (!secondarySoundStartedOnThisLoop) {
            System.out.println("Sound was started for jump end using");
            soundInGameController.setAndPlayAudio(SoundsInGame.JUMP_ENDS);
            secondarySoundStartedOnThisLoop = true;
        }
    }

    protected void addMainSound(){
        if (!mainSoundStartedOnThisLoop) {
            System.out.println("Sound was started for jump start using");
            soundInGameController.setAndPlayAudio(SoundsInGame.JUMP_START);
            mainSoundStartedOnThisLoop = true;
        }
    }

    @Override
    public boolean mustBeControlButtonFlipped() {
        return false;
    }

    @Override
    public void draw(PGraphics graphics){
        //super.draw(graphics);
        float flipValue = 1;
        for (SpriteAnimation animation : animations){
            animation.draw(graphics, x,y, flipValue);
        }
        for (StaticSprite staticSprite : sprites){
            staticSprite.draw(graphics, x,spriteByJumpMovingMaster.getY(), flipValue);
        }



        //spriteByJumpHiddingMaster.draw(graphics);
    }
}
