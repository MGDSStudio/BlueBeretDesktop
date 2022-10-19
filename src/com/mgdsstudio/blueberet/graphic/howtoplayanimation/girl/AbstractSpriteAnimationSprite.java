package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;

abstract class AbstractSpriteAnimationSprite extends AbstractGirlAnimation{
    public final static int WAIT = 0;
    public final static int ANIMATION = 1;
    public final static int STAY = 2;

    AbstractSpriteAnimationSprite(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, STAY, soundInGameController);
        init();
    }

    protected abstract void init();

    public void updateVisibilityRelativeToStage(){
        if (actualStage == WAIT){
            sprites.get(0).makeVisible();
            sprites.get(1).hide();
            animations.get(0).hide();
            //System.out.println(" set to wait");
        }
        else if (actualStage == ANIMATION){
            sprites.get(0).hide();
            sprites.get(1).hide();
            animations.get(0).makeVisible();
            //animations.get(0)
            System.out.println(" set to animation");
        }
        else if (actualStage == STAY){
            sprites.get(0).hide();
            sprites.get(1).makeVisible();
            animations.get(0).hide();
            //System.out.println(" set to stay");
        }
        else {
           // System.out.println(" set something else");
        }
    }
}
