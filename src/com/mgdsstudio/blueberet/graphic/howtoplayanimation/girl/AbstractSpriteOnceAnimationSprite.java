package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;

abstract class AbstractSpriteOnceAnimationSprite extends AbstractSpriteAnimationSprite{

    AbstractSpriteOnceAnimationSprite(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
    }

    @Override
    public void update(){
        if (actualStage == ANIMATION && prevStage == WAIT){
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
        }
        super.update();
    }
}
