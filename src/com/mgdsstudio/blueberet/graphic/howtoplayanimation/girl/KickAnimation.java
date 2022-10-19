package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class KickAnimation extends AbstractSpriteOnceAnimationSprite {

    public KickAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        setFlip(true);
    }

    @Override
    public boolean mustBeControlButtonFlipped(){
        return false;
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 4, 5, 4, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 0, 4, tileset);
        initSprite(path, 6, 4, tileset);
        updateVisibilityRelativeToStage();
        //animations.get(0).setUpdateFrequency(animations.get(0).getUpdateFrequency()/2);
    }

    @Override
    public void update(){
        super.update();
        //System.out.println("Sprite " + animations.get(0).getActualSpriteNumber() + " statement: " + actualStage) ;
    }



}
