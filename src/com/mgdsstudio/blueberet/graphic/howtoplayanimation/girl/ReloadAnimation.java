package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class ReloadAnimation extends AbstractSpriteOnceAnimationSprite {

    public ReloadAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        setFlip(false);
    }

    @Override
    public boolean mustBeControlButtonFlipped(){
        return false;
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 6, 9, 6, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 0, 6, tileset);
        initSprite(path, 10, 6, tileset);
        updateVisibilityRelativeToStage();
        animations.get(0).setUpdateFrequency(animations.get(0).getUpdateFrequency()/2);
    }


    @Override
    public void update(){
        super.update();
        if (actualStage == STAY){
            if (animations.get(0).getActualSpriteNumber() == 0){
                sprites.get(0).hide();
                sprites.get(1).makeVisible();
                animations.get(0).hide();
            }
        }
        /*if (actualStage == ANIMATION && prevStage == WAIT){
            animations.get(0).setAnimationStatement(SpriteAnimation.ACTIVE);
        }
        else if (actualStage == ANIMATION){
            if (animations.get(0).isActualSpriteLast()){
                animations.get(0).setAnimationStatement(SpriteAnimation.PAUSED);
                System.out.println("Animation stoped");
            }
            else {
            }
        }
        else if (actualStage == STAY){
            if (animations.get(0).isActualSpriteLast()){
                if (animations.get(0).getActualSpriteNumber() != 0) animations.get(0).setActualSpriteNumber((byte) 0);
            }
        }*/

    }
    @Override
    public void updateVisibilityRelativeToStage(){
        if (actualStage == WAIT){
            sprites.get(0).makeVisible();
            sprites.get(1).hide();
            animations.get(0).hide();
        }
        else if (actualStage == ANIMATION){
            sprites.get(0).hide();
            sprites.get(1).hide();
            animations.get(0).makeVisible();
        }
        else if (actualStage > STAY){
            sprites.get(0).hide();
            sprites.get(1).makeVisible();
            animations.get(0).hide();
        }
    }


}
