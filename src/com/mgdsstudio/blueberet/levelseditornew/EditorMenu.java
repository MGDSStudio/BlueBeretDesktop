package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.levelseditornew.sliders.*;
import com.mgdsstudio.engine.nesgui.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class EditorMenu {
    private PApplet engine;
    private PGraphics graphics;
    //private ArrayList<GuiElement> guiElements;
    private NES_SlidersController slidersController;
    private Frame mapFrame;
    private Tab tab;
    private final LevelsEditor levelsEditor;
    private ScreenCleaner screenCleaner;
    private FrameWithMoveableText console;
    private ArrayList <SliderButtonWithText> sliders = new ArrayList<>();

    public EditorMenu(PApplet engine, LevelsEditor levelsEditor) {
        this.engine = engine;
        //guiElements = new ArrayList<>();
        if (engine.sketchRenderer() == PConstants.JAVA2D) graphics = engine.createGraphics(engine.width, engine.height);
        else graphics = engine.createGraphics(engine.width, engine.height, PConstants.P2D);
        this.levelsEditor = levelsEditor;
        init();
    }

    private void init() {   //transfered
        ButtonWithCursor button = new ButtonWithCursor(graphics.width/2, graphics.height/2- GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*2, graphics.width/2, ButtonWithCursor.NORMAL_HEIGHT, "PRess me", graphics );
        DigitDataFieldWithText dataField = new DigitDataFieldWithText(graphics.width/2, (int) (button.getUpperY()-button.getHeight()*2),graphics.width/2, GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)/2, "Press small", graphics , 55);
        int distanceToTabX = graphics.width/20;
        int arrowWidth = graphics.width/9;
        int tabWidth = graphics.width-distanceToTabX*2-arrowWidth-distanceToTabX;
        tab = new Tab(distanceToTabX, graphics.height/2,tabWidth, (int) (graphics.height/1.5f), graphics.height-graphics.height/2-distanceToTabX, "Tab", graphics);
        int basicSubGuiHeight = (int) (Tab.getNormalButtonHeightRelativeToScreenSize(engine.width)*0.8f);
        tab.setGuiHeight(basicSubGuiHeight);
        int arrowLeft = (int) (graphics.width-tab.getLeftX()-arrowWidth);
        ScrollArrow arrowUp = new ScrollArrow(arrowLeft, (int) tab.getUpperY(), arrowWidth, graphics, ScrollArrow.DIRECTION_UP, tab);
        ScrollArrow arrowDown = new ScrollArrow(arrowLeft, (int) tab.getUpperY()+tab.getVisibleHeight()-arrowUp.getHeight(), arrowWidth, graphics, ScrollArrow.DIRECTION_DOWN, tab);
        tab.setUp(arrowUp);
        tab.setDown(arrowDown);
        /*
        tab.createGui("Press button", ButtonWithCursor.class, null);
        tab.createGui("Set text", TextDataFieldWithText.class, "Text");
        tab.createGui("Fill data", DigitDataFieldWithText.class, (123));
        tab.createGui("Radio", RadioButton.class, null);
        tab.createGui("Radio2", RadioButton.class,  null);
        tab.createGui("Radio3", RadioButton.class, null);
        tab.createGui("CheckBox 1", CheckBox.class, null);
        tab.createGui("CheckBox 2", CheckBox.class, null);*/

        int consoleWidth = (int) (graphics.width-tab.getLeftX()*2);
        console = new FrameWithMoveableText(graphics.width/2, (int) (tab.getUpperY()- GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width)*1.5f),
                consoleWidth, GuiElement.getNormalButtonHeightRelativeToScreenSize(engine.width), "Console", graphics , "YOU NEED TO SET THE DATA TO THE GAME WORLD");
        int mapZoneWidth = engine.width-3*distanceToTabX;
        int mapZoneHeight = (int) (console.getUpperY()-console.getHeight()/2-2*distanceToTabX);
        int mapCenterX = distanceToTabX+mapZoneWidth/2;
        int mapCenterY = distanceToTabX+mapZoneHeight/2;
        mapFrame = new Frame(mapCenterX, mapCenterY, mapZoneWidth, mapZoneHeight, "MAP", graphics);

        initSliders(arrowLeft+arrowUp.getWidth());
        tab.recalculateHeight();
        dataField.setMaxValue(6000);
        screenCleaner = new ScreenCleaner(mapFrame, tab);
    }

    private void initSliders(int startX){
        int sliderStartX =  startX;
        int yStart = (int) mapFrame.getUpperY();
        int singleSliderHeight = (int)mapFrame.getHeight()/8;
        SliderButtonWithText sliderFile = new SliderButtonWithText(sliderStartX, yStart,6*graphics.width/10, singleSliderHeight,  HighLevelNames.FILE, graphics);
        String [] subNamesForFile = new String[6];
        LowLevelFile.init();
        subNamesForFile[0] = LowLevelFile.NEW_MAP;
        subNamesForFile[1] = LowLevelFile.OPEN_MAP;
        subNamesForFile[2] = LowLevelFile.CLEAR_MAP;
        subNamesForFile[3] = LowLevelFile.TEST_MAP;
        subNamesForFile[4] = LowLevelFile.SAVE_MAP;
        subNamesForFile[5] = LowLevelFile.EXIT;
        sliderFile.addSubButtonsList(subNamesForFile, graphics);

        sliders.add(sliderFile);
        int yStep = sliderFile.getHeight();
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText sliderEdit = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.EDIT, graphics);
        String [] subNamesForEdit = new String[6];
        LowLevelEdit.init();
        subNamesForEdit[0] = LowLevelEdit.SELECT;
        subNamesForEdit[1] = LowLevelEdit.CLEAR_SELECTION;
        subNamesForEdit[2] = LowLevelEdit.MOVE;
        subNamesForEdit[3] = LowLevelEdit.COPY;
        subNamesForEdit[4] = LowLevelEdit.DELETE;
        subNamesForEdit[5] = LowLevelEdit.EDIT_OBJECT;
        sliderEdit.addSubButtonsList(subNamesForEdit, graphics);

        sliders.add(sliderEdit);
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText sliderSimpleRoundElement = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.SIMPLY_ROUND_ELEMENT, graphics);
        String [] subNamesForSimpleRoundElement = new String[4];
        LowLevelSimpleRoundElement.init();
        subNamesForSimpleRoundElement[0] = LowLevelSimpleRoundElement.NEW_ROUND_RECTANGULAR;
        subNamesForSimpleRoundElement[1] = LowLevelSimpleRoundElement.NEW_ROUND_CIRCLE;
        subNamesForSimpleRoundElement[2] = LowLevelSimpleRoundElement.NEW_ROUND_POLYGON;
        subNamesForSimpleRoundElement[3] = LowLevelSimpleRoundElement.NEW_COLLECTABLE_OBJECT;
        sliderSimpleRoundElement.addSubButtonsList(subNamesForSimpleRoundElement, graphics);

        sliders.add(sliderSimpleRoundElement);
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText sliderPerson = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.PERSON, graphics);
        String [] subNamesForPerson = new String[6];
        LowLevelPerson.init();
        subNamesForPerson[0] = LowLevelPerson.PLACE_PLAYER;
        subNamesForPerson[1] = LowLevelPerson.PLACE_GUMBA;
        subNamesForPerson[2] = LowLevelPerson.PLACE_KOOPA;
        subNamesForPerson[3] = LowLevelPerson.PLACE_SPIDER;
        subNamesForPerson[4] = LowLevelPerson.PLACE_SNAKE;
        subNamesForPerson[5] = LowLevelPerson.PLACE_BOWSER;
        sliderPerson.addSubButtonsList(subNamesForPerson, graphics);
        sliders.add(sliderPerson);

        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());
        sliderPerson.blockSubbutons(LowLevelPerson.PLACE_KOOPA);

        SliderButtonWithText sliderComplexRound = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.COMPLEX_ROUND_ELEMENT, graphics);
        String [] subNamesForComplexRound = new String[4];
        LowLevelPerson.init();
        subNamesForComplexRound[0] = LowLevelComplexRoundElement.NEW_ROUND_PIPE;
        subNamesForComplexRound[1] = LowLevelComplexRoundElement.NEW_BRIDGE;
        subNamesForComplexRound[2] = LowLevelComplexRoundElement.NEW_ROTATING_STICK;
        subNamesForComplexRound[3] = LowLevelComplexRoundElement.NEW_PLATFORM_SYSTEM;
        sliderComplexRound.addSubButtonsList(subNamesForComplexRound, graphics);
        sliders.add(sliderComplexRound);
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText sliderGraphic = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.GRAPHIC, graphics);
        String [] subNamesForGraphic = new String[3];
        LowLevelPerson.init();
        subNamesForGraphic[0] = LowLevelGraphic.BACKGROUND;
        subNamesForGraphic[1] = LowLevelGraphic.NEW_SPRITE;
        subNamesForGraphic[2] = LowLevelGraphic.NEW_ANIMATION;
        sliderGraphic.addSubButtonsList(subNamesForGraphic, graphics);
        sliders.add(sliderGraphic);
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText sliderZones = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.ZONE, graphics);
        String [] subNamesForZones= new String[7];
        LowLevelPerson.init();
        subNamesForZones[0] = LowLevelZones.NEW_LAVA_BALLS_ZONE;
        subNamesForZones[1] = LowLevelZones.NEW_CLEARING_ZONE;
        subNamesForZones[2] = LowLevelZones.NEW_BULLETS_BILL_ZONE;
        subNamesForZones[3] = LowLevelZones.NEW_OBJECTS_APPEARING_ZONE;
        subNamesForZones[4] = LowLevelZones.NEW_CAMERA_FIXATION_ZONE;
        subNamesForZones[5] = LowLevelZones.NEW_PORTAL_SYSTEM_ZONE;
        subNamesForZones[6] = LowLevelZones.NEW_LEVEL_END_ZONE;
        sliderZones.addSubButtonsList(subNamesForZones, graphics);
        sliders.add(sliderZones);
        yStart= (int) (sliders.get(sliders.size()-1).getUpperY()+sliderFile.getHeight());

        SliderButtonWithText preferencesSlider = new SliderButtonWithText(sliderStartX, yStart+yStep,6*graphics.width/10, singleSliderHeight,  HighLevelNames.PREFERENCES, graphics);
        String [] subNamesForPreferences= new String[4];
        LowLevelPreferences.init();
        subNamesForPreferences[0] = LowLevelPreferences.GRID_PREFERENCES;
        subNamesForPreferences[1] = LowLevelPreferences.DISPLAYING_PREFERENCES;
        subNamesForPreferences[2] =  LowLevelPreferences.SETTINGS;

        preferencesSlider.addSubButtonsList(subNamesForPreferences, graphics);
        sliders.add(preferencesSlider);

        slidersController = new NES_SlidersController(sliders, levelsEditor);


    }



    public void update(){
        for (SliderButtonWithText buttonWithText : sliders){
            buttonWithText.update(engine.mouseX, engine.mouseY);
        }
        try{
            console.update(engine.mouseX, engine.mouseY);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        tab.update(engine.mouseX, engine.mouseY);
        slidersController.update();
    }

    public void draw(){

        screenCleaner.clearFrame(graphics);
        console.draw(graphics);
        mapFrame.draw(graphics);
        tab.draw(graphics);
        for (int i = sliders.size()-1; i>=0; i--){
            sliders.get(i).draw(graphics);
        }


    }

    public Frame getFrame() {
        return mapFrame;
    }

    public void closeSliders() {
        for (int i = 0; i < sliders.size(); i++) sliders.get(i).startToClose();
    }

    public PGraphics getGraphics() {
        return graphics;
    }

    public Tab getTab() {
        return tab;
    }

    public FrameWithMoveableText getConsole() {
        return console;
    }
}
