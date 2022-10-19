package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class GoAnimation extends AbstractSpriteInfinityAnimationSprite{

    public GoAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        //init();
    }

    protected void init(){
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1,2,4,2, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 1,2, tileset);
        initSprite(path, 5,2, tileset);
        updateVisibilityRelativeToStage();
    }

    /*
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
        else if (actualStage == STAY){
            sprites.get(0).hide();
            sprites.get(1).makeVisible();
            animations.get(0).hide();
        }
    }*/
}
