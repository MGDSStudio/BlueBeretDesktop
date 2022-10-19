package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInMenu;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextLabel;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class YouNeedToRelaunchMenu extends AbstractMenuWithTitleAndBackButton{
    private final PApplet engine;
    private String OK_LETS_GO = "OK, LET'S GO!";
    private String GAME_WILL_BE_RELAUNCHED_IN_PREFIX = "GAME ENDS AFTER ";
    private String GAME_WILL_BE_RELAUNCHED_IN_SUFFIX = " SEC";

    private Timer timer;
    private final int TIME_BEFORE_END = 5000;
    private int prevRestSeconds;
    TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;

    public YouNeedToRelaunchMenu(PApplet engine, PGraphics graphics) {
        this.engine = engine;
        type = MenuType.LEVEL_RESULTS_MENU;
        initLanguageSpecific();
        init(engine, graphics);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- НУЖНО ПЕРЕЗАПУСТИТЬ ИГРУ -";
            GAME_WILL_BE_RELAUNCHED_IN_PREFIX = "ИГРА ЗАКРОЕТСЯ ЧЕРЕЗ ";
            GAME_WILL_BE_RELAUNCHED_IN_SUFFIX = " СЕК";
            OK_LETS_GO = "ОК, ПЕРЕЗАПУСКАЕМ!";
        }
        else {
            titleName = "- GAME MUST BE RESTARTED -";
        }
    }


    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        fillActualSide(engine, graphics);
        renameBackButton(graphics);

    }

    private void renameBackButton(PGraphics graphics) {
        ArrayList<NES_GuiElement> toBeAligned = new ArrayList<>();
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == GO_TO_PREVIOUS_MENU){
                String newName = getLanguageSpecificNameForNextButton();
                guiElement.setAnotherTextToBeDrawnAsName(newName);
                NES_ButtonWithCursor buttonWithCursor = (NES_ButtonWithCursor) guiElement;
                //buttonWithCursor.setCenterX(graphics);
                buttonWithCursor.alignAlongY(graphics);
                toBeAligned.add(guiElement);
                break;
            }
        }
        //alignButtons(toBeAligned);
        //alignButtonsAlongY(null);
        //alignButtonsAlongY(toBeAligned);
    }

    private String getLanguageSpecificNameForNextButton() {
        int chars = GO_TO_PREVIOUS_MENU.length();
        String newString = "";
        if (Program.LANGUAGE == Program.RUSSIAN) newString = OK_LETS_GO;
        else newString = OK_LETS_GO;
        boolean aligned = false;
        while (!aligned){
            newString+=" ";
            if (newString.length()>=chars) aligned = true;
            else newString=(" " + newString);
            if (newString.length()>=chars) aligned = true;
        }
        System.out.println("This function is not very well but works. But the cursor is too left from the button name");
        return newString;
    }

    private void fillActualSide(PApplet engine, PGraphics graphics) {
        float widthCoef = 0.8f;
        Rectangular rectangular = new Rectangular(graphics.width/2, frame.getLeftUpperCorner().y+frame.getHeight()/2, frame.getWidth()*widthCoef, frame.getHeight()*0.6f);
        String text = getText(engine);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, 17, 2000, graphics.textFont, graphics, false);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*3f);

        NES_TextLabel textLabel = new NES_TextLabel((int) rectangular.getCenterX(), (int) (rectangular.getCenter().y+rectangular.getHeight()/2+3*guiElements.get(0).getHeight()), guiElements.get(0).getHeight(), guiElements.get(0).getHeight(), (GAME_WILL_BE_RELAUNCHED_IN_PREFIX+" " + GAME_WILL_BE_RELAUNCHED_IN_SUFFIX), graphics);
        guiElements.add(textLabel);
        textLabel.block(true);
    }



    private String getText(PApplet engine) {
        String text = "";
        if (Program.LANGUAGE== Program.RUSSIAN) {
            text += "ВЫ ИЗМЕНИЛИ ОСВЕЩЕНИЕ В ИГРЕ ЧЕРЕЗ МЕНЮ НАСТРОЕК. ДЛЯ ТОГО, ЧТОБЫ ИЗМЕНЕНИЯ ВСТУПИЛИ В СИЛУ ВАМ НЕОБХОДИМО ПЕРЕЗАПУСТИТЬ ИГРУ. ";
            text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
            text += " НАЖМИТЕ НА КНОПКУ ВНИЗУ ЭКРАНА ЧТОБЫ ЗАКРЫТЬ ИГРУ И ЗАПУСТИТЕ ЕЁ ПОВТОРНО, ИЛИ ДОЖДИТЕСЬ, ПОКА ИГРА ЗАКРОЕТСЯ САМОСТОЯТЕЛЬНО И СНОВА ЗАПУСТИТЕ ЕЁ. ";
        }
        else {
            text += "YOU HAVE CHANGED THE LIGHTS RENDERING TYPE IN THE OPTIONS MENU. YOU NEED TO RESTART THE GAME TO APPLY THE CHANGES. ";
            text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
            text += " PRESS THE BUTTON ON THE BOTTOM OF THE SCREEN TO CLOSE THE GAME OR WAIT FOR FIVE SECOND AND THE GAME WILL BE CLOSED AUTOMATICALLY. AFTER THAT YOU NEED TO START THE GAME AGAIN. ";
        }
        return text;
    }


    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        textInSimpleFrameDrawingController.update();
        updateTimer(gameMenusController);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.EXIT_MENU);
                }
            }
        }
    }

    private void updateTimer(GameMenusController gameMenusController) {
        if (textInSimpleFrameDrawingController.isActualTextIsReadyToBeChanged()){
            if (timer == null){
                timer = new Timer(TIME_BEFORE_END);
            }
        }
        if (timer != null){
            if (timer.isTime()){
                gameMenusController.setNewMenu(MenuType.EXIT_MENU);
            }
            else {
                int restTime = PApplet.ceil(timer.getRestTime()/1000f);

                for (NES_GuiElement nes_guiElement : guiElements){
                    if (nes_guiElement.getClass() == NES_TextLabel.class){
                        if (nes_guiElement.getName() != titleName) {
                            String newName = GAME_WILL_BE_RELAUNCHED_IN_PREFIX + " " + restTime + GAME_WILL_BE_RELAUNCHED_IN_SUFFIX;
                            nes_guiElement.setName(newName);
                            if (prevRestSeconds != restTime) {
                                prevRestSeconds = restTime;
                                gameMenusController.switchOffMusic();
                                gameMenusController.pressedSoundOn(nes_guiElement);
                                gameMenusController.addSound(SoundsInMenu.BUTTON_SELECTED);
                            }
                            if (nes_guiElement.getActualStatement() == NES_GuiElement.BLOCKED){
                                nes_guiElement.setActualStatement(NES_GuiElement.ACTIVE);
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void setPrevPage() {
        System.out.println("Only one page");
    }



    @Override
    protected void setNextPage() {
        System.out.println("Only one page");
    }

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        textInSimpleFrameDrawingController.draw(graphic);
    }


}
