package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PGraphics;

public class PortalAnimationOld extends AbstractSpriteOnceAnimationSprite{
    private SpriteOnBlackBackgroundHiddingMaster rectMaster;

    public PortalAnimationOld(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        setFlip(false);
        //SpriteOnBlackBackgroundHiddingMaster(int x, int y, int zoneWidth, int zoneHeight, int timeToFullHidding, int numbers) {
        rectMaster = new SpriteOnBlackBackgroundHiddingMaster(x,y,(int)((float)girlDim*0.65f), girlDim,1000, 12);
    }

    @Override
    public boolean mustBeControlButtonFlipped(){
        return false;
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 5, 4, 5, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 0, 5, tileset);
        initSprite(path, 5, 5, tileset);
        updateVisibilityRelativeToStage();
        //animations.get(0).setUpdateFrequency(animations.get(0).getUpdateFrequency()/2);
    }

    @Override
    public void update(){
        if (actualStage == ANIMATION){
            rectMaster.update();
            if (prevStage == WAIT){
                rectMaster.recreate();
            }
        }
        super.update();
    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        if (actualStage != WAIT) rectMaster.draw(graphics);
    }

}