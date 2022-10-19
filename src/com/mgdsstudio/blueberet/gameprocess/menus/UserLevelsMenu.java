package com.mgdsstudio.blueberet.gameprocess.menus;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class UserLevelsMenu extends GameMenu {

    private SubWindow windowWithLevels;
    //private final Vec2 leftUpperCorner = new Vec2(Programm.engine.width/8,Programm.engine.width/8);

    public UserLevelsMenu(){
        Vec2 leftUpperCorner = new Vec2(Program.engine.width/8, Program.engine.width/8);
        int width = (int)(Program.engine.width-(2*leftUpperCorner.x));
        int height = (int)(Program.engine.height-(3.5f*leftUpperCorner.x));
        windowWithLevels = new SubWindow(leftUpperCorner, width, height);
        createGUI();
    }
    
    private void createGUI(){
        guiElements = new ArrayList<>();
        ArrayList <androidGUI_Element> insideElements = new ArrayList<>();
        background = new Image(Program.getAbsolutePathToAssetsFolder("Circuit 1.png"));
        background.getImage().resize(Program.engine.width, Program.engine.height);
        float startY = androidAndroidGUI_Button.NORMAL_HEIGHT;
        float buttonsStepAlongY = Program.engine.height/5.0f;
        //insideElements.add(new GUI_Button(new Vec2(Game2D.engine.width/2, startY), GUI_Button.NORMAL_WIDTH, GUI_Button.NORMAL_HEIGHT, testLevel, false));
        startY+=buttonsStepAlongY;
        ArrayList<String> allFiles = StringLibrary.getFilesListInAssetsFolder();
        ArrayList<String> rounds = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Program.USER_LEVELS_PREFIX, Program.USER_LEVELS_EXTENSION);
        System.out.println("there are " + rounds.size() + "levels");
        for (int i = 0; i < rounds.size(); i++) {
            LoadingMaster loadingMaster = new LoadingMaster((byte)StringLibrary.getLevelNumberFromName(rounds.get(i)), LoadingMaster.USER_LEVELS);
            String name = loadingMaster.getLevelName();
            //button.setUserValue(StringLibrary.getLevelNumberFromName(rounds.get(i)));
            androidAndroidGUI_Button button = new androidAndroidGUI_Button(new Vec2(Program.engine.width / 2, startY+i* androidAndroidGUI_Button.NORMAL_HEIGHT), androidAndroidGUI_Button.NORMAL_WIDTH, androidAndroidGUI_Button.NORMAL_HEIGHT, name, false);
            String anotherName = loadingMaster.getLevelName();
            button.setAnotherNameForDrawing(anotherName);
            button.setUserValue(StringLibrary.getLevelNumberFromName(rounds.get(i)));
            insideElements.add(button);
        }
        int yPosition = (int)insideElements.get(insideElements.size()-1).getPosition().y+ androidAndroidGUI_Button.NORMAL_HEIGHT;
        insideElements.add(new androidAndroidGUI_Button(new Vec2(Program.engine.width / 2, yPosition), androidAndroidGUI_Button.NORMAL_WIDTH, androidAndroidGUI_Button.NORMAL_HEIGHT, backButtonText, false));
        windowWithLevels.addGUI(insideElements);
    }

    public void update(){

        for (androidGUI_Element button : windowWithLevels.guiElements) {
            button.update(null);
            if (button.getStatement() == androidGUI_Element.RELEASED){
                if (button.getName() == backButtonText){
                    Program.gameStatement = Program.SOME_MENU;
                }
                else {
                    Program.gameStatement = Program.GAME_PROCESS;
                    Program.actualRoundNumber = (byte)button.getUserValue();
                }
            }
        }
        windowWithLevels.updateScrolling();
    }

    @Override
    public void draw(){
        super.draw();
        windowWithLevels.draw();
    }
    
    /*
    public UserLevelsMenu(){
        insideElements = new ArrayList<>();
        background = new Image(Programm.getRelativePathToAssetsFolder() + "Circuit 1.png");
        background.getImage().resize(Programm.engine.width, Programm.engine.height);
        float startY = GUI_Button.NORMAL_HEIGHT;
        float buttonsStepAlongY = Programm.engine.height/5.0f;
        //insideElements.add(new GUI_Button(new Vec2(Game2D.engine.width/2, startY), GUI_Button.NORMAL_WIDTH, GUI_Button.NORMAL_HEIGHT, testLevel, false));
        startY+=buttonsStepAlongY;
        ArrayList<String> allFiles = StringLibrary.getFilesListInAssetsFolder();
        ArrayList<String> rounds = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Programm.USER_LEVELS_PREFIX, Programm.USER_LEVELS_EXTENSION);
        System.out.println("there are " + rounds.size() + "levels");
        for (int i = 0; i < rounds.size(); i++) {
            LoadingMaster loadingMaster = new LoadingMaster((byte)StringLibrary.getLevelNumberFromName(rounds.get(i)), LoadingMaster.USER_LEVELS);
            String name = loadingMaster.getLevelName();
            //button.setUserValue(StringLibrary.getLevelNumberFromName(rounds.get(i)));
            GUI_Button button = new GUI_Button(new Vec2(Programm.engine.width / 2, startY+i*GUI_Button.NORMAL_HEIGHT), GUI_Button.NORMAL_WIDTH, GUI_Button.NORMAL_HEIGHT, name, false);
            String anotherName = loadingMaster.getLevelName();
            button.setAnotherNameForDrawing(anotherName);
            button.setUserValue(StringLibrary.getLevelNumberFromName(rounds.get(i)));
            insideElements.add(button);
        }
        int yPosition = (int)insideElements.get(insideElements.size()-1).getPosition().y+GUI_Button.NORMAL_HEIGHT;
        insideElements.add(new GUI_Button(new Vec2(Programm.engine.width / 2, yPosition), GUI_Button.NORMAL_WIDTH, GUI_Button.NORMAL_HEIGHT, backButtonText, false));

    }




    public void update(){
        for (GUI_Element button : insideElements) {
            button.update(null);
            if (button.getStatement() == GUI_Element.RELEASED){
                if (button.getName() == backButtonText){
                    Programm.gameStatement = Programm.MAIN_MENU;
                }
                else {
                    Programm.gameStatement = Programm.GAME_PROCESS;
                    Programm.actualRoundNumber = (byte)button.getUserValue();
                }
            }
        }
    }
    
    */
}
