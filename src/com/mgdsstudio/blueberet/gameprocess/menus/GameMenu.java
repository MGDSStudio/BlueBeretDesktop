package com.mgdsstudio.blueberet.gameprocess.menus;

import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class GameMenu {
    protected final String backButtonText = "Back";
    protected Image background;
    protected ArrayList<androidGUI_Element> guiElements;


    public void update(){
        if (guiElements != null) {
            for (androidGUI_Element button : guiElements) {
                button.update(null);
            }
        }

    }

    public void draw() {
        if (background != null) Program.engine.background(background.getImage());
        for (androidGUI_Element button : guiElements) button.draw();
    }
}
