package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorSaveMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.IBeretColors;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithCoordinates;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ColorButton;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class BeretColorChangingMenu extends AbstractMenuWithTitleAndBackButton implements IBeretColors,  IImageColorChangeable{

    private int startY;
    private final PApplet engine;
    //private Image clothes;
    private ImageWithCoordinates clothes;
    private boolean useColorfulButtons = false;
    private int startFrame;
    private Coordinate imageCoordinate;
    private int imageWidth, imageHeight;
    //private final static

    public BeretColorChangingMenu (PApplet engine, PGraphics graphics) {
        this.engine = engine;
        type = MenuType.BERET_COLOR_CHANGING;
        loadClothes(graphics);
        initLanguageSpecific();
        init(engine, graphics);
    }

    /*public BeretColorChangingMenu (PApplet engine, PGraphics graphics, ImageWithCoordinates clothes) {
        this.engine = engine;
        type = MenuType.BERET_COLOR_CHANGING;
        this.clothes = clothes;
        initLanguageSpecific();
        init(engine, graphics);
    }*/

    private void loadClothes(PGraphics graphics) {
        boolean resaved = false;
        if (dataToBeSaved != null) {
            try{
                clothes = (ImageWithCoordinates) dataToBeSaved;
                System.out.println("Image data was recreated");
                resaved = true;
                imageCoordinate = clothes.getCoordinate();
                imageWidth = clothes.getWidth();
                imageHeight = clothes.getHeight();
            }
            catch (Exception e){
                System.out.println("Image data can not be recreated and must be created new");
            }
        }
        else {
            System.out.println("There are no saved data");
            if (!resaved) {
                Image image = new Image(Program.getAbsolutePathToAssetsFolder("Clothes picture.png"));
                clothes = new ImageWithCoordinates(image, image.image.width, image.image.height, new Coordinate(graphics.width / 2, graphics.height / 3));
                updateBeretColorForGraphic(engine, null, null, clothes.getImage());
                imageCoordinate = clothes.getCoordinate();
                imageWidth = clothes.getWidth();
                imageHeight = clothes.getHeight();
            }
        }
    }

    private void reloadClothes(){
        System.out.println("Clothes were reloaded");
        Image image = new Image(Program.getAbsolutePathToAssetsFolder("Clothes picture.png"));
        //imageWidth = clothes.getWidth();
        //imageHeight = clothes.getHeight();
        clothes = new ImageWithCoordinates(image, imageWidth, imageHeight, imageCoordinate);
        //updateBeretColorForGraphic(engine, null, null, clothes.getImage());
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- ВЫБЕРИТЕ ПОДХОДЯЩИЙ ЦВЕТ -";
        }
        else{
            titleName = "- CHOOSE BERET COLOR -";
        }
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        pages = 1;
        fillWithButtons(graphics);

    }

    private void bringPrevMenuButtonBack() {
        ArrayList <NES_GuiElement> guis = new ArrayList<>();
        for (NES_GuiElement guiElement: guiElements){
            if (guiElement.getName() == GO_TO_PREVIOUS_MENU){
                guis.add(guiElement);
            }
        }
        alignButtons(guis);
    }

    @Override
    protected void setPrevPage() {
        System.out.println("Not more pages");
    }

    @Override
    protected void setNextPage() {
        System.out.println("Not more pages");
    }

    private void fillWithButtons(PGraphics graphics) {
        if (useColorfulButtons) {
            fillMenuWithColorButtons(graphics);
        }
        else {
            fillMenuWithSimpleButtons(graphics);
        }
    }

    private void fillMenuWithSimpleButtons(PGraphics graphics) {
        int startPosY = (int) (frame.getLeftUpperCorner().y+frame.getHeight()-NES_GuiElement.NORMAL_HEIGHT*3);
        String [] names = createNamesList();
        float relativeDistance = 1.7f;
        if (Program.SIDES_RELATIONSHIP <1.8f){
            relativeDistance = 1.35f;
        }
        fillGuiWithCursorButtons(engine, startPosY, names, TO_UP, relativeDistance);
        ArrayList <NES_GuiElement> onlyColors = new ArrayList<>();
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() != GO_TO_PREVIOUS_MENU && guiElement.getName() != titleName){
                onlyColors.add(guiElement);
            }
        }
        System.out.println("On page " + guiElements.size() + " gui and " + onlyColors.size() + " buttons with color");
        alignButtons(onlyColors);
        TwiceColor [] colors = createColorsList(names);
        renameButtonsForActualLanguage();
        addValuesToButtons(onlyColors, colors);
    }

    private void renameButtonsForActualLanguage() {
        if (Program.LANGUAGE == Program.RUSSIAN) {
            ArrayList <NES_GuiElement> renamed = new ArrayList<>();
            for (int i = 0; i < guiElements.size(); i++) {
                if (guiElements.get(i).getName() == SKY_BLUE_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( SKY_BLUE_NAME_RU);
                }
                else if (guiElements.get(i).getName() == GREEN_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( GREEN_NAME_RU);
                }
                else if (guiElements.get(i).getName() == MAROON_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( MAROON_NAME_RU);
                }
                else if (guiElements.get(i).getName() == BLUE_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( BLUE_NAME_RU);
                }
                else if (guiElements.get(i).getName() == BLACK_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( BLACK_NAME_RU);
                }
                else if (guiElements.get(i).getName() == ORANGE_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( ORANGE_NAME_RU);
                }
                else if (guiElements.get(i).getName() == RED_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( RED_NAME_RU);
                }
                else if (guiElements.get(i).getName() == GREY_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( GREY_NAME_RU);
                }
                else if (guiElements.get(i).getName() == TAN_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( TAN_NAME_RU);
                }
                else if (guiElements.get(i).getName() == WHITE_NAME_EN) {
                    renamed.add(guiElements.get(i));
                    guiElements.get(i).setAnotherTextToBeDrawnAsName( WHITE_NAME_RU);
                }
            }
            alignButtonsAlongY(renamed);
        }
    }

    private void addValuesToButtons(ArrayList<NES_GuiElement> guiElements, TwiceColor[] colors) {
        for (int i = 0; i < colors.length; i++){
            guiElements.get(i).setUserData(colors[i]);
        }
    }

    private String[] createNamesList() {
        String [] names = new String[9];
        names[SKY_BLUE] = SKY_BLUE_NAME_EN;
        names[GREEN] = GREEN_NAME_EN;
        names[MAROON] = MAROON_NAME_EN;
        names[BLUE] = BLUE_NAME_EN;
        names[BLACK] = BLACK_NAME_EN;
        names[TAN] = TAN_NAME_EN;
        names[RED] = RED_NAME_EN;
        names[GREY] = GREY_NAME_EN;
        names[ORANGE] = ORANGE_NAME_EN;
        //names[WHITE] = WHITE_NAME;
        return names;
    }

    private TwiceColor [] createColorsList(String [] names) {
        TwiceColor [] colors = new TwiceColor[names.length];
        colors[SKY_BLUE] = new TwiceColor();
        colors[GREEN] = new TwiceColor(GREEN_NAME_EN);
        colors[MAROON] = new TwiceColor(MAROON_NAME_EN);
        colors[BLUE] = new TwiceColor(BLUE_NAME_EN);
        colors[BLACK] = new TwiceColor(BLACK_NAME_EN);
        colors[TAN] = new TwiceColor(TAN_NAME_EN);
        colors[RED] = new TwiceColor(RED_NAME_EN);
        colors[GREY] = new TwiceColor(GREY_NAME_EN);
        colors[ORANGE] = new TwiceColor(ORANGE_NAME_EN);
        //colors[WHITE] = new TwiceColor(WHITE_NAME);
        return colors;
    }

    private void fillMenuWithColorButtons(PGraphics graphics){
        int buttonWidth = (int) (graphics.width/3.5f);
        int buttonHeight = (int) (buttonWidth/2.5f);
        String name = "Sky blue";
        TwiceColor color = new TwiceColor();
        int startPosX = (int) (frame.getLeftUpperCorner().x + (buttonWidth * 1.35f) / 2f);
        int startPosY = (int) (clothes.getPosY() + 1.1f * (clothes.getHeight() / 2) + buttonHeight / 2);
        NES_ColorButton colorButton = new NES_ColorButton(startPosX, startPosY, buttonWidth, buttonHeight, name, graphics, color);
        guiElements.add(colorButton);
    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.CAMPAIGN);
                    dataToBeSaved = null;
                }
                else {
                    saveBeretColorDataNew(element.getUserData());
                    //BeretColorChanger.beretColorWasChanged = true;
                    HUD_GraphicData.init();
                }
            }
        }
        updateButtonsReplacing(gameMenusController.getEngine());
    }

    private void updateButtonsReplacing(PApplet engine) {
        if (startFrame == 0){
            startFrame = engine.frameCount;
        }
        if (startFrame > 0){
            bringPrevMenuButtonBack();
            startFrame = -1;
        }
    }

    private void saveBeretColorDataNew(Object userData) {
        TwiceColor newColor = (TwiceColor) userData;
        BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(engine);
        beretColorLoadingMaster.loadData();
        TwiceColor oldColor = new TwiceColor();
        reloadClothes();
        System.out.println("Old colors were: " + TwiceColor.getTextRepresentationForColor(oldColor) + " with name " + oldColor.getName());
        System.out.println("New colors must be: " + TwiceColor.getTextRepresentationForColor(newColor)+" with name " + newColor.getName());
        updateBeretColorForGraphic(engine, oldColor, newColor, clothes.getImage());
        BeretColorSaveMaster beretColorSaveMaster = new BeretColorSaveMaster(engine, newColor);
        beretColorSaveMaster.saveData();
    }

    private void saveBeretColorDataOld(Object userData) {
        TwiceColor newColor = (TwiceColor) userData;
        BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(engine);
        beretColorLoadingMaster.loadData();
        TwiceColor oldColor = beretColorLoadingMaster.getBeretColor();
        System.out.println("Old colors were: " + TwiceColor.getTextRepresentationForColor(oldColor) + " with name " + oldColor.getName());
        System.out.println("New colors must be: " + TwiceColor.getTextRepresentationForColor(newColor)+" with name " + newColor.getName());
        updateBeretColorForGraphic(engine, oldColor, newColor, clothes.getImage());
        BeretColorSaveMaster beretColorSaveMaster = new BeretColorSaveMaster(engine, newColor);
        beretColorSaveMaster.saveData();
    }

    /*
    private void updateBeretColorForGraphic(TwiceColor oldColor, TwiceColor newColor, Image image){
        BeretColorChanger beretColorChanger = new BeretColorChanger();
        if (oldColor == null){
            //Use default
            oldColor = new TwiceColor();
        }
        if (newColor == null){
            BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(engine);
            beretColorLoadingMaster.loadData();
            newColor = beretColorLoadingMaster.getBeretColor();
        }
        beretColorChanger.changeColor(oldColor.getBrightColor(), newColor.getBrightColor(), image);
        beretColorChanger.changeColor(oldColor.getDarkColor(), newColor.getDarkColor(), image);
    }*/

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        if (clothes != null){
            graphic.pushMatrix();
            clothes.draw(graphics, 0, 0);
            graphic.popMatrix();
        }
    }

}
