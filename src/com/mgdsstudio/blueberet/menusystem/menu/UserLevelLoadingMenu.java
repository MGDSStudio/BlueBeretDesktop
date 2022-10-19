package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class UserLevelLoadingMenu extends AbstractLoadingMenu{
    private final boolean loadedFromEditor;

    public UserLevelLoadingMenu(PApplet engine, PGraphics graphics, boolean loadedFromEditor) {
        super(engine, graphics);
        this.loadedFromEditor = loadedFromEditor;
        this.loadingMode = LOADING_AS_LEVEL;
        this.menuLaunchedFromEditor = loadedFromEditor;
        System.out.println("User level loading menu was started from editor " + loadedFromEditor);
        type = MenuType.USER_LEVEL_LOADING;
        init(engine, graphics);
        initLanguageSpecific();
        //addText("SINGLE MISSION LOADING");
    }

    protected void initLanguageSpecific() {
        //if (GO_TO_PREVIOUS_MENU != null) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            addText(" ЗАГРУЗКА...");
        } else {
            addText("SINGLE MISSION LOADING");
        }
        //}
    }

    public boolean isLoadedFromEditor() {
        return loadedFromEditor;
    }

    @Override
    protected void closeMenuAndGoToGame(GameMenusController gameMenusController) {
        gameMenusController.loadLevel(loadingMode, menuLaunchedFromEditor, ExternalRoundDataFileController.USER_LEVELS);
    }
}
