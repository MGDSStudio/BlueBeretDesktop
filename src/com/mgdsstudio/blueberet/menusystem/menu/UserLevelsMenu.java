package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class UserLevelsMenu extends AbstractMenuWithTitleAndBackButton {
    public static boolean loadAllLevels = false;
    private ArrayList <Integer> levelNumbers;
    private ArrayList <String> levelsList;
    private int startY;
    private final PApplet engine;
    protected int levelsOnPage;

    public UserLevelsMenu(PApplet engine, PGraphics graphics) {
        this.engine = engine;
        type = MenuType.USER_LEVELS;
        initLanguageSpecific();
        init(engine, graphics);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- ОДИНОЧНЫЕ МИССИИ -";
        }
        else{
            titleName = "- SINGLE MISSIONS -";
        }
    }

    protected void calculateLevelsOnPageNumber() {
        int step = guiElements.get(guiElements.size()-1).getHeight();
        int area = frame.getHeight();
        int number = PApplet.floor((area-6*step)/step);
        int nextPageButtonHeight = step*3;
        levelsOnPage = number;
        System.out.println("On page: " + levelsOnPage + ". levels. area: " + area +". Step "+ step);
        if (actualPage <0){
            actualPage = 0;
            //
        }
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        calculateLevelsOnPageNumber();
        loadUserLevelsList(engine, 0);
        pages = PApplet.ceil(levelsList.size()/levelsOnPage)+1;
        addPrevNextButtons(engine);

    }

    /*
    private void calculateLevelsOnPageNumber() {
        int step = guiElements.get(guiElements.size()-1).getHeight();
        int area = frame.getHeight();
        int number = PApplet.floor((area-6*step)/step);
        int nextPageButtonHeight = step*3;
        levelsOnPage = number;
        System.out.println("On page: " + levelsOnPage + ". levels. area: " + area +". Step "+ step);
        if (actualPage <0){
            actualPage = 0;
            //
        }
    }
     */

    private void loadUserLevelsList(PApplet engine, int page) {
        //levelsListWithNumber = new HashMap<>();
        levelNumbers = new ArrayList<>();
        levelsList = new ArrayList<>();
        startY = (int) (frame.getLeftUpperCorner().y+guiElements.get(guiElements.size()-1).getHeight()*2);
        ArrayList<String> allFiles = null;
        if (Program.OS == Program.DESKTOP){
            allFiles = StringLibrary.getFilesListInAssetsFolder();
        }
        else {
            allFiles = StringLibrary.getFilesListInCache();
        }

        ArrayList<String> rounds = getLevelsList(allFiles);
        System.out.println("There are " + rounds.size() + " levels from " + startY + "; In dir: " + allFiles.size() + " files");
        System.out.println("+++ Full files list in dir : " );
        for (int i = 0; i < allFiles.size(); i++){
            System.out.println("*** " + allFiles.get(i) );
        }
        for (int i = 0; i < rounds.size(); i++) {
            LoadingMaster loadingMaster = new LoadingMaster((byte)StringLibrary.getLevelNumberFromName(rounds.get(i)), LoadingMaster.USER_LEVELS);
            String name = loadingMaster.getLevelName();
            levelNumbers.add(new Integer(StringLibrary.getLevelNumberFromName(rounds.get(i))));
            levelsList.add(name);
        }
        fillActualSide(engine);
    }

    private ArrayList<String> getLevelsList(ArrayList<String> allFiles) {
        if (Program.debug || loadAllLevels){
            ArrayList<String> rounds = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Program.USER_LEVELS_PREFIX, Program.USER_LEVELS_EXTENSION);
            return rounds;
        }
        else {
            ArrayList<String> fullRoundsList = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Program.USER_LEVELS_PREFIX, Program.USER_LEVELS_EXTENSION);
            ArrayList<String> onlyUserRoundsList = new ArrayList<>();
            for (String string : fullRoundsList){
                if (string.contains("-")){
                    int number = string.indexOf('-');
                        if (number>0){
                            char nextChar = string.charAt(number+1);

                            if (isDigit(nextChar)){
                                onlyUserRoundsList.add(string);
                            }
                        }
                    }
                }
            return onlyUserRoundsList;
            }

        }

    private boolean isDigit(char nextChar) {
        if (nextChar == '0' || nextChar == '1' || nextChar == '2' || nextChar == '3' || nextChar == '4' || nextChar == '5' || nextChar == '6' || nextChar == '7' || nextChar == '8' || nextChar == '9') return true;
        else return false;
    }



    private void fillActualSide(PApplet engine) {
        int buttonsNumber = levelsList.size();
        String [] names;
        int [] numbers;
        if (buttonsNumber <= levelsOnPage){
            moreThanOne = false;
            actualPage = 0;
            names = new String[levelsList.size()];
            numbers = new int[names.length];
            for (int i = 0; i < names.length; i++){
                names[i]=levelsList.get(i);
                numbers[i]=levelNumbers.get(i);
            }
        }
        else
        {
            moreThanOne = true;
            if (actualPage > (PApplet.ceil(levelsList.size()/levelsOnPage))){
                actualPage = PApplet.floor(levelsList.size()/levelsOnPage);
            }
            int firstNumber = levelsOnPage*actualPage;
            int lastNumber = levelsOnPage*(actualPage+1);
            if (lastNumber > levelsList.size()){
                lastNumber = levelsList.size()-1;
            }
            names = new String[lastNumber-firstNumber];
            numbers = new int[names.length];
            for (int i = 0; i < (lastNumber-firstNumber); i++){
                names[i]=levelsList.get(firstNumber+i);
                numbers[i]=levelNumbers.get(firstNumber+i);
            }
        }
        //was
        //fillGuiWithCursorButtons(engine, startY, names, TO_DOWN);
        //now
        fillGuiWithCursorButtons(engine, startY, names, numbers, TO_DOWN);
        addLevelNumbersToButtons();
    }

    private void addLevelNumbersToButtons() {
        for (NES_GuiElement element : guiElements){
            if (element.getClass() == NES_ButtonWithCursor.class){

            }
        }
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.MAIN);
                }
                else if (element.getClass() == NES_ButtonWithCursor.class){
                    gameMenusController.switchOffMusic();
                    Program.actualRoundNumber = getLevelNumberForButton(element);
                    boolean gameStartedFromEditor = false;
                    gameMenusController.setUserValue(new Boolean(gameStartedFromEditor));
                    gameMenusController.setNewMenu(MenuType.USER_LEVEL_LOADING);
                }
            }
        }
    }

    private int getLevelNumberForButton(NES_GuiElement element) {
        if (element.getUserData() != null) {
            System.out.println("Level number " + (int) element.getUserData());
            return (int) element.getUserData();
        }
        else {
            System.out.println("There are no data about level number");
            return -2;
        }
    }

    //private void

    @Override
    protected void setPrevPage() {
        removeLevels();
        removePrevAndNextButtons();
        fillActualSide(engine);
        addPrevNextButtons(engine);
        ArrayList <NES_GuiElement> elementsToBeAligned = getElementsToBeAligned();
        alignButtons(elementsToBeAligned );
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
        removeLevels();
        removePrevAndNextButtons();
        fillActualSide(engine);
        addPrevNextButtons(engine);
        ArrayList <NES_GuiElement> elementsToBeAligned = getElementsToBeAligned();
        alignButtons(elementsToBeAligned );
    }

    private void removeLevels() {
        for (int  i = guiElements.size()-1; i >= 0; i--){
            if (guiElements.get(i).getClass() == NES_ButtonWithCursor.class){
                if (guiElements.get(i).getName() != GO_TO_PREVIOUS_MENU){
                    guiElements.remove(guiElements.get(i));
                }
            }
        }
    }






}
