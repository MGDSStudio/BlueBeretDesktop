package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

public class PortalAnimation extends AbstractAnimationSprite{
    private SpriteOnBlackBackgroundHiddingMaster rectMaster;

    public PortalAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        setFlip(false);
        rectMaster = new SpriteOnBlackBackgroundHiddingMaster(x,y,(int)((float)girlDim*0.65f), girlDim,1000, 12);
    }

    @Override
    public boolean mustBeControlButtonFlipped(){
        return false;
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 5, 4, 5, null, HumanAnimationController.IDLE_ANIMATION_FREQUENCY);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 5, 5, tileset);
        updateVisibilityRelativeToStage();
    }

    @Override
    public void update(){
        if (actualStage == 0) resetSound();
        if (actualStage == ANIMATION){

        }
        else if (actualStage >= SPRITE){
            addSound();
            rectMaster.update();
            if (prevStage == ANIMATION){
                rectMaster.recreate();
            }
        }
        super.update();
    }

    protected void addSound(){
        if (!mainSoundStartedOnThisLoop) {
            System.out.println("Sound was started for portal");
            soundInGameController.setAndPlayAudio(SoundsInGame.JUMP_IN_PORTAL);
            mainSoundStartedOnThisLoop = true;
        }
    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        if (actualStage >= SPRITE) rectMaster.draw(graphics);
    }

    @Override
    public void updateVisibilityRelativeToStage(){
        if (actualStage == ANIMATION){
            sprites.get(0).hide();
            animations.get(0).makeVisible();
        }
        else if (actualStage == SPRITE){
            sprites.get(0).makeVisible();
            animations.get(0).hide();
        }
        else {
            sprites.get(0).makeVisible();
            animations.get(0).hide();
        }
    }

}
