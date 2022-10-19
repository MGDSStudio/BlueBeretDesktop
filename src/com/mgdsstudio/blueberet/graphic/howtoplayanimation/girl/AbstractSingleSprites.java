package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;

abstract class AbstractSingleSprites extends AbstractGirlAnimation{

    public AbstractSingleSprites(int x, int y, int stagesNumber, SoundInGameController soundInGameController) {
        super(x, y, stagesNumber, soundInGameController);
        init();
    }

    protected abstract void init();


}
