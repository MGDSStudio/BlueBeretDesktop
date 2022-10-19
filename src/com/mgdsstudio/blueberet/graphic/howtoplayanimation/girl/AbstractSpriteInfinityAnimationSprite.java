package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;

abstract class AbstractSpriteInfinityAnimationSprite extends AbstractSpriteAnimationSprite{
    /*public final static int WAIT = 0;
    public final static int ANIMATION = 1;
    public final static int STAY = 2;*/

    AbstractSpriteInfinityAnimationSprite(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        //init();
    }


    @Override
    public boolean mustBeControlButtonFlipped(){
        return true;
    }
}
