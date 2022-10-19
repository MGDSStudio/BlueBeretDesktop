package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class HowToPlayMenuFromOptions extends AbstractHowToPlayMenu{

    //protected static int actualPage = 0;

    public HowToPlayMenuFromOptions(PApplet engine, PGraphics graphics) {
        super(engine, graphics);

    }

    @Override
    public void backPressed(GameMenusController gameMenusController) {
        gameMenusController.setNewMenu(MenuType.OPTIONS);
    }


}
