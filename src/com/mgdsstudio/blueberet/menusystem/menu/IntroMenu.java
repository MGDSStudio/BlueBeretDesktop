package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.loading.FilesCopyMaster;
import com.mgdsstudio.blueberet.loading.PlayerDataController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressSaveMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.io.File;
import java.util.ArrayList;

public class IntroMenu extends AbstractCutSceneMenu{
    private final String [] pathes;
    private static final String PATH_PREFIX = "Intro";
    private boolean musicUploaded;
    private boolean withHowToPlayMenu;
    
    public IntroMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.INTRO_MENU;
        initHowToPlay();
        screens = 2;
        pathes = new String[screens];
        for (int i = 0; i < screens; i++){
            pathes[i] = getPathForScreenGraphic(i);
        }
        init(engine, graphics);
        createNewPlayerDataFiles(engine);
    }

    private void createNewPlayerDataFiles(PApplet engine) {

        FilesCopyMaster master = new FilesCopyMaster(engine);
        if (Program.OS == Program.ANDROID) master.copyToDataInCache("Global player data template.json", PlayerDataController.fileNameForGlobalSavingInCompany);
        else if (Program.OS == Program.DESKTOP) master.copyFileMultiplatform(Program.getAbsolutePathToAssetsFolder("Global player data template.json"), Program.getAbsolutePathToAssetsFolder(PlayerDataController.fileNameForGlobalSavingInCompany));

    }

    private void initHowToPlay() {

        /*if (!wasGameAlreadyStarted()){
            withHowToPlayMenu = true;
        }*/

    }

    private static String getPathForScreenGraphic(int number){
        return Program.getRelativePathToAssetsFolder()+PATH_PREFIX+ (number+1) + PATH_SUFFIX;
    }

    public static boolean areCutScenesAlreadyDeveloped(PApplet engine){
        String path = getPathForScreenGraphic(0);
        //PImage image = engine.loadImage(path);
        File file = new File(path);
        if (file.exists()) {
            System.out.println("Intro is not ready right now");
            return  true;
        }
        else return false;/*
        if (image != null) return true;
        else return false;*/
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine,graphics);
        loadActualImage(engine);
        loadActualText(engine);
        float zoneHeight = graphics.height/6;
        Rectangular rectangular = new Rectangular(graphics.width/2, graphics.height-zoneHeight*1.2f, graphics.width*0.8f, zoneHeight);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController( actualText[0], rectangular, 6, 2000, graphics.textFont, graphics, true);
    }

    private void loadActualText(PApplet engine) {
        int stringsNumber = 1;
        ArrayList <String> textForActualScreen = new ArrayList<>();
        textForActualScreen.clear();
        initLanguageSpecific(textForActualScreen);

        stringsNumber = textForActualScreen.size();
        if (stringsNumber > 0){
            actualText = new String[stringsNumber];
            for (int i = 0; i < textForActualScreen.size(); i++){
                actualText[i] = textForActualScreen.get(i);
            }
        }
        else {
            actualText = new String[1];
            actualText[0] = "";
        }
    }

    protected void initLanguageSpecific(ArrayList<String> textForActualScreen) {


        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            if (actualScreen == 0){
                textForActualScreen.add(" ---ПРОЛОГ--- ");
            }
            else if (actualScreen == 1){
                textForActualScreen.add(" ... ПОКА ЕЩЕ НЕ ГОТОВ, НО БУДЬТЕ УВЕРЕНЫ, ОН СКОРО ПОЯВИТЬСЯ. МЫ ОБЕЩАЕМ! ");
            }
        } else {
            if (actualScreen == 0){
                textForActualScreen.add(" ---THE INTRO--- ");
            }
            else if (actualScreen == 1){
                textForActualScreen.add(" ...IS NOT READY RIGHT NOW BUT WILL BE SOONER.  WE PROMISE ");
            }
        }
    }

    private void loadActualImage(PApplet engine){
        try {
            actualImage = engine.loadImage(pathes[actualScreen]);
            imageWidth = (int) (graphics.width * 0.8f);
            actualImage.resize(imageWidth, 0);
        }
        catch (Exception e){
            
        }
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        if (actualScreen < screens){
            if (!musicUploaded){
                gameMenusController.initIntroMusic();
                musicUploaded = true;
            }
        }
        else {
            gameMenusController.switchOffMusic();
            PlayerProgressSaveMaster playerProgressSaveMaster = new PlayerProgressSaveMaster();
            playerProgressSaveMaster.writeValuesWithoutSaving();
            playerProgressSaveMaster.saveOnDisk();
            gameMenusController.setLevelNumberToBeLoadedNext(playerProgressSaveMaster.getNextLevel());
            if (!withHowToPlayMenu) gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
            else gameMenusController.setNewMenu(MenuType.WOULD_YOU_LIKE_TO_LEARN_HOW_TO_PLAY_MENU);
            System.out.println("Data was saved on disk");
        }
    }



    @Override
    protected void setNextScreen(PApplet engine) {
        actualScreen++;
        loadActualImage(engine);
        loadActualText(engine);
    }





}
