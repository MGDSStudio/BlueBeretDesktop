package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class ShootAnimation extends AbstractSpriteInfinityAnimationSprite {

    public ShootAnimation(int x, int y, SoundInGameController soundInGameController) {
        super(x, y, soundInGameController);
        setFlip(false);
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 7, 3, 7, null);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 0, 7, tileset);
        initSprite(path, 4, 7, tileset);
        updateVisibilityRelativeToStage();
    }

    @Override
    public void update(){
        if (animations.get(0).getActualSpriteNumber() == 1) addSound();
        if (animations.get(0).getActualSpriteNumber() == 0 || actualStage == 0) resetSound();
        super.update();
    }

    @Override
    protected void resetSound(){
        if (mainSoundStartedOnThisLoop){

            mainSoundStartedOnThisLoop = false;
            soundInGameController.stopAllAudio();
            System.out.println("Sound was stopped");
            //soundInGameController.stopAllAudio();
        }
    }


    protected void addSound(){
        if (!mainSoundStartedOnThisLoop) {
            System.out.println("Sound was started for shot using");
            soundInGameController.setAndPlayAudio(SoundsInGame.HANDGUN_SHOT);
            mainSoundStartedOnThisLoop = true;
        }
    }
}


