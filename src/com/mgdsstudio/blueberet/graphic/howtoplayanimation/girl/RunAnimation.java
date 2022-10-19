package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class RunAnimation extends AbstractSpriteInfinityAnimationSprite{

    public RunAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
    }

    protected void init(){
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1,0,4,0, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 0,0, tileset);
        initSprite(path, 5,0, tileset);
        updateVisibilityRelativeToStage();
    }


}
