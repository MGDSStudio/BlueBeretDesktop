package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class EndCutSceneMenu extends AbstractCutSceneMenu{
    private final String [] pathes;
    private final String PATH_PREFIX = "End cutscene";
    private boolean withHelpProjectMenu = true;

    public EndCutSceneMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.END_CUTSCENE_MENU;
        screens = 2;
        System.out.println("End cut scene menu is shown");
        pathes = new String[screens];
        for (int i = 0; i < screens; i++){
            pathes[i] = Program.getRelativePathToAssetsFolder()+PATH_PREFIX+ (i+1) + PATH_SUFFIX;
        }
        init(engine, graphics);

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
        ArrayList<String> textForActualScreen = new ArrayList<>();
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
                textForActualScreen.add(" --- ПОЗДРАВЛЯЕМ! --- ");
            }
            else if (actualScreen == 1){
                textForActualScreen.add(" ЭТО КОНЕЦ ДЕМО ВЕРСИИ. БУДЬТЕ УВЕРЕНЫ, ЧТО МЫ ПРОДОЛЖАЕМ РАЗРАБОТКУ ЭТОЙ ИГРЫ И ДОБАВИМ В НЕЕ БОЛЬШЕ УРОВНЕЙ, ПРОТИВНИКОВ И ОРУЖИЯ. БЛАГОДАРИМ ВАС ");
            }
        } else {
            if (actualScreen == 0){
                textForActualScreen.add(" --- CONGRATULATION! --- ");
            }
            else if (actualScreen == 1){
                textForActualScreen.add(" THIS IS THE END OF THE DEMO VERSION.  BE SURE THAT WE CONTINUE THE DEVELOPMENT AND WE WILL ADD MORE LEVELS, ENEMIES AND WEAPONS TO THE GAME. THANK YOU! ");
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

        }
        else {
            if (!withHelpProjectMenu) {
                gameMenusController.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                System.out.println("Data was saved on disk");
            }
            else {
                gameMenusController.setNewMenu(MenuType.DO_YOU_LIKE_OUR_GAME_MENU);

            }
        }
    }



    @Override
    protected void setNextScreen(PApplet engine) {
        actualScreen++;
        loadActualImage(engine);
        loadActualText(engine);
    }


}
