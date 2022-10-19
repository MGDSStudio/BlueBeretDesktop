package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundInGameController;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SingleCellWithImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.controllers.HumanAnimationController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.text.DecimalFormat;

public class OpenSubcellAnimation extends WithFrameAnimation{
    private static final int FRAME_WIDTH = UpperPanel.WIDTH_FOR_WEAPON_FRAME;
    private static String PRESSING_TIME_TEXT = "PRESSING" + '\n' +"TIME: ";
    private static String FRAME_OPENED_TEXT = "WEAPONS LIST" + '\n' + "OPENED!";
    private ImageZoneSimpleData withGrenadeData;

    private final static int STAY_WITH_M79 = 0;
    protected final static int STAY_WITH_GRENADE = 1;
    private SingleCellWithImage singleCellWithImage;
    private CustomOnPanelImage customOnPanelImage;
    private boolean tilesetWasInit;
    public OpenSubcellAnimation(int x, int y, Rectangular controlZone, SoundInGameController soundInGameController) {
        super(x, y, controlZone, HIDDEN+1, FRAME_WIDTH, soundInGameController);
        flip = false;


    }

    @Override
    protected void initGraphic() {
        imageInFrame = InWorldObjectsGraphicData.mainGraphicFile;
        inFrameImageData = HUD_GraphicData.getImageZoneForObjectType(Weapon.getWeaponCodeForType(WeaponType.M79), false);
        withGrenadeData = HUD_GraphicData.getImageZoneForObjectType(Weapon.getWeaponCodeForType(WeaponType.GRENADE), false);
        imageWidth-=3f*eightPartsFrameImage.getWidth()*0.05f;
        initNominalDimensions(imageWidth,imageHeight);
        //int code, Tileset tileset, int width, int height, int posX, int posY
        System.out.println("Sizes: " + eightPartsFrameImage.getWidth() +"x" + eightPartsFrameImage.getHeight() );
        customOnPanelImage = new CustomOnPanelImage(FirearmsWeapon.getWeaponCodeForType(WeaponType.M79), InWorldObjectsGraphicData.mainGraphicTileset, eightPartsFrameImage.getWidth(), eightPartsFrameImage.getHeight(), (int)frameCenterX, (int)frameCenterY);
        //if (InWorldObjectsGraphicData.mainGraphicTileset == null) System.out.println("Image was not init yet");

        //customOnPanelImage.setText(" ");
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
        initAnimation(Program.getAbsolutePathToAssetsFolder(path), 1, 5, 4, 5, null, HumanAnimationController.IDLE_ANIMATION_FREQUENCY);
        Tileset tileset = animations.get(0).getTileset();
        initSprite(path, 4, 3, tileset);
        initSprite(path, 5, 3, tileset);
        /*
        initSprite(path, 5, 4, tileset);
        initSprite(path, 6, 4, tileset);
         */
        animations.get(0).hide();
        updateVisibilityRelativeToStage();
    }



    @Override
    public void update() {
        //changeImages();
        updateVisibilityRelativeToStage();
        if (actualStage == (0)){
            resetSound();
        }
        if (actualStage <= STAY_WITH_GRENADE) {
            /*for (SpriteAnimation animation : animations){
                animation.update();
            }*/
        }
        if (actualStage >= STAY_WITH_GRENADE) {

            if (prevStage < STAY_WITH_GRENADE){
                if (timer == null){
                    timer = new Timer(TIMER_TIME);
                }
                else timer.setNewTimer(TIMER_TIME);
            }

        }
        updateSound();
        prevStage = actualStage;
    }

    private void changeImages() {
        if (actualStage < STAY_WITH_GRENADE ){
            inFrameImageData = HUD_GraphicData.getImageZoneForObjectType(Weapon.getWeaponCodeForType(WeaponType.M79), false);
        }
        else if (prevStage == STAY_WITH_GRENADE && actualStage == (STAY_WITH_GRENADE+1)){
            inFrameImageData = HUD_GraphicData.getImageZoneForObjectType(Weapon.getWeaponCodeForType(WeaponType.GRENADE), false);
        }
    }

    private void updateImageChanging() {
        if (timer != null) {
            float restTime = (float) (TIMER_TIME - timer.getRestTime()) / 1000f;
            if (restTime == 0) {
                System.out.println("Changing");
            }
        }
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


    private ImageZoneSimpleData getActualZoneData(){
        if (actualStage < 2) return inFrameImageData;
        else return withGrenadeData;
    }
    @Override
    public void draw(PGraphics graphics) {
        super.draw(graphics);
        eightPartsFrameImage.draw(graphics);
        ImageZoneSimpleData data = getActualZoneData();
        graphics.image(imageInFrame.getImage(), frameCenterX, frameCenterY, imageWidth, imageHeight, data.leftX, data.upperY, data.rightX, data.lowerY);

        if (actualStage >= SPRITE){
            float restTime = (float)(TIMER_TIME-timer.getRestTime())/1000f;
            String text;
            graphics.pushStyle();
            graphics.textFont = font;
            if (restTime>=0) {
                String formattedFloat;
                if (restTime >= ((TIMER_TIME/1000f)-0.04f) && restTime <= ((TIMER_TIME/1000f)+0.04f)) formattedFloat = ""+(int)restTime;
                else formattedFloat = new DecimalFormat("#0.0").format(restTime);
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

        if (!tilesetWasInit) {
            //CustomOnPanelImage.tilesetWithMainGraphicFile = InWorldObjectsGraphicData.mainGraphicTileset;

            customOnPanelImage.setTileset(HUD_GraphicData.mainGraphicFile);
            //customOnPanelImage.setTileset(InWorldObjectsGraphicData.mainGraphicTileset);
            if (CustomOnPanelImage.getTilesetWithMainGraphicFile() != null) tilesetWasInit = true;
        }
        else {

            customOnPanelImage.drawArrow(graphics);
        }
    }

    @Override
    public void updateVisibilityRelativeToStage() {
        if (actualStage == STAY_WITH_M79) {
            sprites.get(0).makeVisible();
            sprites.get(1).hide();
            //System.out.println("Actual drawn first");
        } else if (actualStage > STAY_WITH_GRENADE) {
            sprites.get(0).hide();
            sprites.get(1).makeVisible();
            //System.out.println("Actual drawn second");
        }
    }
}