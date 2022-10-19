package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;

abstract class AbstractAnimationSprite extends AbstractGirlAnimation{   //For portal
    protected final static int ANIMATION = 0;
    protected final static int SPRITE = 1;
    protected final static int HIDDEN = 2;


    AbstractAnimationSprite(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, HIDDEN, soundInGameController);
        init();
    }

    AbstractAnimationSprite(int x, int y, int lastStage, SoundInGameController soundInGameController) {
        super(x, y, lastStage, soundInGameController);
        init();
    }

    protected abstract void init();


    @Override
    public void update(){
        if (actualStage == ANIMATION){
            /*if (animations.get(0).isActualSpriteLast()){
                animations.get(0).setAnimationStatement(SpriteAnimation.PAUSED);
            }
            if (prevStage >= HIDDEN){
                if (animations.get(0).getAnimationStatement() == SpriteAnimation.PAUSED){
                    animations.get(0).setAnimationStatement(SpriteAnimation.ACTIVE);
                }
            }*/
        }
        else if (actualStage == SPRITE){
            if (animations.get(0).getActualSpriteNumber()!=0){
                animations.get(0).setActualSpriteNumber((byte)0);
            }
        }
        else if (actualStage == HIDDEN){

        }
        super.update();
    }


    public void updateAsOnce(){
        if (actualStage == ANIMATION){
            if (animations.get(0).isActualSpriteLast()){
                animations.get(0).setAnimationStatement(SpriteAnimation.PAUSED);
            }
            if (prevStage >= HIDDEN){
                if (animations.get(0).getAnimationStatement() == SpriteAnimation.PAUSED){
                    animations.get(0).setAnimationStatement(SpriteAnimation.ACTIVE);
                }
            }
        }
        else if (actualStage == SPRITE){
            if (animations.get(0).isActualSpriteLast()){
                animations.get(0).setActualSpriteNumber((byte)0);
            }
        }
        else if (actualStage == HIDDEN){

        }
        super.update();
    }
}
