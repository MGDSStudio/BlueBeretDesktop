package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PGraphics;

public class UseItemAnimation extends WithFrameAnimation{
    private static final int FRAME_WIDTH = UpperPanel.HEIGHT_FOR_WEAPON_FRAME;
    private String PREFIX = "+20 HP";
    private DissolvingAndUpwardsMovingText dissolvingText;

    public UseItemAnimation(int x, int y, Rectangular controlZone, SoundInGameController soundInGameController) {
        super(x, y, controlZone, HIDDEN+1, FRAME_WIDTH, soundInGameController);
    }

    @Override
    protected void initGraphic() {
        imageInFrame = InWorldObjectsGraphicData.mainGraphicFile;
        inFrameImageData = InWorldObjectsGraphicData.smallMedicalKitInBag;
        imageWidth-=3f*eightPartsFrameImage.getWidth()*0.05f;
        initNominalDimensions(imageWidth,imageWidth);
        flip = false;
    }

    @Override
    protected void loadLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN){
            PREFIX = "ЖИЗНЬ+20";
        }
    }

    @Override
    protected void init() {
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 8, 4, 8, null, HumanAnimationController.IDLE_ANIMATION_FREQUENCY);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 5, 8, tileset);
        updateVisibilityRelativeToStage();
    }

    @Override
    public void update() {
        if (actualStage == 0){
            resetSound();
        }
        if (actualStage <= SPRITE) {
            for (SpriteAnimation animation : animations){
                animation.update();
            }
        }
        if (actualStage >= SPRITE) {
            if (actualStage > SPRITE){
                if (prevStage == SPRITE){
                    addSound();
                    dissolvingText = new DissolvingAndUpwardsMovingText(x,y-sprites.get(0).getHeight()*0.35f, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, PREFIX, (int) (TIMER_TIME*1.5f), DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, 0);
                    if (timer == null){
                        timer = new Timer(TIMER_TIME);
                    }
                    else timer.setNewTimer(TIMER_TIME);
                }
                dissolvingText.update((int) (1000f/Program.engine.frameRate));
            }
            if (prevStage == ANIMATION) {

            }
        }
        else if (actualStage == HIDDEN){
            if (animations.get(0).getActualSpriteNumber()!=0){
                animations.get(0).setActualSpriteNumber((byte)0);
            }
        }
        prevStage = actualStage;
    }

    protected void addSound(){
        if (!mainSoundStartedOnThisLoop) {
            System.out.println("Sound was started for item using");
            soundInGameController.setAndPlayAudio(SoundsInGame.WEAPON_SELECTED);
            mainSoundStartedOnThisLoop = true;
        }
    }

    @Override
    public void draw(PGraphics graphics) {
        super.draw(graphics);
        eightPartsFrameImage.draw(graphics);
        if (actualStage >= SPRITE){
            graphics.pushStyle();
            graphics.textAlign(PConstants.CENTER, PConstants.BOTTOM);
            graphics.popStyle();
            if (actualStage > SPRITE) dissolvingText.draw(graphics);
        }
        if (actualStage >= SPRITE){

        }
        //if ((actualStage == ANIMATION)  || (actualStage > HIDDEN)) {
        if ((actualStage == ANIMATION)  || (actualStage == SPRITE)) {
            graphics.image(imageInFrame.getImage(), frameCenterX, frameCenterY, imageWidth, imageHeight, inFrameImageData.leftX, inFrameImageData.upperY, inFrameImageData.rightX, inFrameImageData.lowerY);
        }

    }

    @Override
    public void updateVisibilityRelativeToStage() {
        if (actualStage == ANIMATION) {
            sprites.get(0).hide();
            animations.get(0).makeVisible();
        } else if (actualStage > SPRITE) {
            sprites.get(0).makeVisible();
            animations.get(0).hide();
        }
    }

}
