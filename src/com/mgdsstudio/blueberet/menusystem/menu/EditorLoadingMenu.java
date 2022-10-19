package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class EditorLoadingMenu extends AbstractLoadingMenu{


    public EditorLoadingMenu(PApplet engine, PGraphics graphics) {
        super(engine, graphics);
        this.loadingMode = LOADING_AS_EDITOR;
        this.menuLaunchedFromEditor = false;
        type = MenuType.EDITOR_LOADING;
        init(engine, graphics);
        initLanguageSpecific();
        //addText("EDITOR LOADING");
    }

    protected void initLanguageSpecific() {
        //if (GO_TO_PREVIOUS_MENU != null) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            addText("ЗАГРУЗКА РЕДАКТОРА");
        } else {
            addText("EDITOR LOADING");
        }
        //}
    }

    protected void closeMenuAndGoToGame(GameMenusController gameMenusController) {
        gameMenusController.loadLevel(LOADING_AS_EDITOR, menuLaunchedFromEditor, ExternalRoundDataFileController.USER_LEVELS);
    }




}
