package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.engine.nesgui.GuiElement;
import com.mgdsstudio.engine.nesgui.SliderButtonWithText;

import java.util.ArrayList;

public class NES_SlidersController {
    private final ArrayList<SliderButtonWithText> sliders;
    private final int CODE_NOTHING_PRESSED = -1;
    private final int SUB_PRESSED = -2;
    private final LevelsEditor levelsEditor;


    public NES_SlidersController(ArrayList<SliderButtonWithText> gui, LevelsEditor levelsEditor) {
        this.sliders = new ArrayList<>();
        for (GuiElement guiElement : gui){
            if (guiElement.getClass() == SliderButtonWithText.class){
                sliders.add((SliderButtonWithText) guiElement);
            }
        }
        //this.sliders = sliders;
        this.levelsEditor = levelsEditor;
    }


    public void update(){
        //boolean somePressed = false;
        int pressedHighNumber = CODE_NOTHING_PRESSED;
        boolean lowLevelPressed = false;
        for (int i = 0; i < sliders.size(); i++){
            if (sliders.get(i).getActualStatement() == GuiElement.RELEASED){
                pressedHighNumber = i;
                break;
            }
            else if (sliders.get(i).isSomeSubbuttonReleased()) {
                pressedHighNumber = i;
                lowLevelPressed = true;
                String name = sliders.get(i).getReleasedSubbuttonName();
                levelsEditor.saveDataForActualSubmenu();
                levelsEditor.setNewStatementByName(name);

                break;
            }
        }
        if (pressedHighNumber >=0){
            closeSlidersWithout(pressedHighNumber);
        }
        else if (lowLevelPressed) closeSlidersWithout(-1);
    }

    private void closeSlidersWithout(int pressedNumber) {
        for (int i = 0; i < sliders.size(); i++){
            if (i != pressedNumber) {
                sliders.get(i).startToClose();
            }
            /* it must contain:
            public void startToClose() {
            closeInsideList();
            movementController.setMovingStatement(Constants.CLOSING);
            }*/

        }
    }
}
