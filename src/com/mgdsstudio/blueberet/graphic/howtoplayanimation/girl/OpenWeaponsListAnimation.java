package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.text.DecimalFormat;

public class OpenWeaponsListAnimation extends WithFrameAnimation{
    private static final int FRAME_WIDTH = UpperPanel.WIDTH_FOR_WEAPON_FRAME;
    private static String PRESSING_TIME_TEXT = "PRESSING" + '\n' +"TIME: ";
    private static String FRAME_OPENED_TEXT = "WEAPONS LIST" + '\n' + "OPENED!";



    public OpenWeaponsListAnimation(int x, int y, Rectangular controlZone, SoundInGameController soundInGameController) {
        super(x, y, controlZone, HIDDEN+1, FRAME_WIDTH, soundInGameController);
        flip = true;
    }

    @Override
    protected void initGraphic() {
        imageInFrame = InWorldObjectsGraphicData.mainGraphicFile;
        inFrameImageData = HUD_GraphicData.getImageZoneForObjectType(Weapon.getWeaponCodeForType(WeaponType.REVOLVER), false);
        imageWidth-=3f*eightPartsFrameImage.getWidth()*0.05f;
        initNominalDimensions(imageWidth,imageHeight);
    }

    @Override
    protected void loadLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN){
            PRESSING_TIME_TEXT = "Длительность" + '\n' + "нажатия: ";
            FRAME_OPENED_TEXT = "АРСЕНАЛ" + '\n' + "открыт";
            SEC_TEXT = " СЕК";
        }
        System.out.println("Language is changed!");
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
        if (actualStage == (0)){
            resetSound();
        }
        if (actualStage <= SPRITE) {
            for (SpriteAnimation animation : animations){
                animation.update();
            }
        }
        if (actualStage >= SPRITE) {
            if (prevStage < SPRITE){
                if (timer == null){
                    timer = new Timer(TIMER_TIME);
                }
                else timer.setNewTimer(TIMER_TIME);
            }
            if (prevStage == ANIMATION) {

            }
        }
        else if (actualStage == HIDDEN){
            if (animations.get(0).getActualSpriteNumber()!=0){
                animations.get(0).setActualSpriteNumber((byte)0);
            }
        }
        updateSound();
        prevStage = actualStage;
    }

    protected void updateSound(){
        if (actualStage >= SPRITE){
            if (!mainSoundStartedOnThisLoop) {
                if (timer != null) {
                    if (timer.isTime()) {
                        System.out.println("Sound was started for weapon frame opening");
                        soundInGameController.setAndPlayAudio(SoundsInGame.WEAPON_FRAME_OPENING);
                        mainSoundStartedOnThisLoop = true;
                    }

                }
            }
        }
    }

    @Override
    public void draw(PGraphics graphics) {
        super.draw(graphics);
        eightPartsFrameImage.draw(graphics);
        if (actualStage >= SPRITE){
            float restTime = (float)(TIMER_TIME-timer.getRestTime())/1000f;
            String text;
            graphics.pushStyle();
            graphics.textFont = font;
            if (restTime>=0) {
                String formattedFloat;
                if (restTime >= ((TIMER_TIME/1000f)-0.04f) && restTime <= ((TIMER_TIME/1000f)+0.04f)) formattedFloat = ""+(int)restTime;
                else formattedFloat = new DecimalFormat("#0.0").format(restTime);
                //System.out.println("Rest time: " + restTime);
                text = PRESSING_TIME_TEXT + formattedFloat+SEC_TEXT;
            }
            else text = FRAME_OPENED_TEXT;
            graphics.textAlign(PConstants.CENTER, PConstants.BOTTOM);
            graphics.text(text, frameCenterX, frameCenterY-eightPartsFrameImage.getHeight()*0.7f);
            graphics.popStyle();
        }
        if (actualStage <= SPRITE) {
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
