package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class HowToPlayMenuFromCampaign extends AbstractHowToPlayMenu{
    private String nameForBackButtonToBeDrawn = "BACK TO GAME";
    //protected static int actualPage = 0;

    public HowToPlayMenuFromCampaign (PApplet engine, PGraphics graphics) {
        super(engine, graphics);
        if (Program.LANGUAGE == Program.RUSSIAN){
            nameForBackButtonToBeDrawn = "В ИГРУ";
        }
        remakeGui(graphics);

    }

    private void remakeGui(PGraphics graphics){
        ArrayList <NES_GuiElement> toBeReposElements = new ArrayList<>();
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == GO_TO_PREVIOUS_MENU){
                guiElement.setName(nameForBackButtonToBeDrawn);
                toBeReposElements.add(guiElement);
            }
        }
        alignButtonsAlongY(toBeReposElements);

    }

    @Override
    public void backPressed(GameMenusController gameMenusController) {
        gameMenusController.setNewMenu(MenuType.MAIN_LEVEL_LOADING);
    }


}

