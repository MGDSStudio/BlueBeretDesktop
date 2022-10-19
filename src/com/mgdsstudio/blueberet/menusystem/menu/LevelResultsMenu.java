package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.EndGameDeterminer;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.playerprogress.levelresults.LevelResultsCalculator;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class LevelResultsMenu extends AbstractMenuWithTitleAndBackButton{
    private final PApplet engine;
    private String ENEMIES = " ENEMIES KILLED: ";
    private String SECRETS = " SECRETS FOUND: ";
    private String TIME = " TIME: ";
    private String MIN = " MIN ";
    private String SEC = " SEC";
    TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;

    public LevelResultsMenu(PApplet engine, PGraphics graphics) {
        this.engine = engine;
        type = MenuType.LEVEL_RESULTS_MENU;
        initLanguageSpecific();
        init(engine, graphics);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- РЕЗУЛЬТАТЫ -";
            ENEMIES = " УБИТО ПРОТИВНИКОВ: ";
            SECRETS = " ОБНАРУЖЕНО СЕКРЕТОВ: ";
            TIME = " ВРЕМЯ:      ";
            MIN = " МИН ";
            SEC = " СЕК";
        }
        else {
            titleName = "- RESULTS -";
        }
    }


    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        fillActualSide(engine);
        renameBackButton(graphics);

    }

    private void renameBackButton(PGraphics graphics) {
        ArrayList <NES_GuiElement> toBeAligned = new ArrayList<>();
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
        if (Program.LANGUAGE == Program.RUSSIAN) newString = "ПРОДОЛЖИТЬ";
        else newString = "CONTINUE";
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

    private void fillActualSide(PApplet engine) {
        float widthCoef = 0.8f;
        Rectangular rectangular = new Rectangular(graphics.width/2, frame.getLeftUpperCorner().y+frame.getHeight()/2, frame.getWidth()*widthCoef, frame.getHeight()*widthCoef);
        String text = getText(engine);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, 18, 2000, graphics.textFont, graphics, true);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*1f);
    }



    private String getText(PApplet engine) {
        String text = "";
        LevelResultsCalculator levelResultsCalculator = new LevelResultsCalculator();
        text+=ENEMIES;
        text+=levelResultsCalculator.getKilledEnemies();
        text+='/';
        text+=levelResultsCalculator.getEnemiesOnLevel();
        text+=" ";
        text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
        //text+=" ";
        //text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
        text+=SECRETS;
        text+=levelResultsCalculator.getFoundedSecrets();
        text+='/';
        text+=levelResultsCalculator.getSecretsOnLevel();
        text+=" ";
        text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
        text+=TIME;
        int allSec = levelResultsCalculator.getTime();
        int minutes = getMinutesFromSeconds(allSec);
        int seconds = getRestSecondsFromSeconds(allSec);
        text+=' ';
        if (minutes >0) {
            text += minutes;
            text += MIN;
        }
        text+=seconds;
        text+=SEC;
        text+=" ";
        text+=TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR;
        //System.out.println("String to be drawn: " + text);
        return text;
    }

    private int getMinutesFromSeconds(float allSec) {
        int min = PApplet.floor(allSec/60f);
        return min;
    }

    private int getRestSecondsFromSeconds(int allSec) {
        int sec = allSec%60;
        return sec;
    }


    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        textInSimpleFrameDrawingController.update();
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    EndGameDeterminer endGameDeterminer = new EndGameDeterminer();
                    if (!endGameDeterminer.didPlayerEndGame()){
                        gameMenusController.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                        System.out.println("You go back to the ccontinue last game menu");
                    }
                    else{
                        gameMenusController.setNewMenu(MenuType.END_CUTSCENE_MENU);
                        System.out.println("Game is ended");
                    }
                }
                else if (element.getClass() == NES_ButtonWithCursor.class){

                }
            }
        }
    }


    @Override
    protected void setPrevPage() {
       System.out.println("Only one page");
    }

    private ArrayList<NES_GuiElement> getElementsToBeAligned() {
        ArrayList<NES_GuiElement> elementsToBeAligned = new ArrayList<>();
        for (NES_GuiElement element : guiElements){
            if (element.getClass() == NES_ButtonWithCursor.class){
                if (element.getName() != GO_TO_PREVIOUS_MENU && element.getName() != PREV && element.getName() != NEXT){
                    elementsToBeAligned.add(element);
                }
                else System.out.println("This element " + element.getName() + " with class " + element.getClass() + "  will not be added to te aligning list");
            }
        }
        System.out.println("Elements: " + guiElements.size() + " to be aligned only " + elementsToBeAligned.size());
        return elementsToBeAligned;
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
