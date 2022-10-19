package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithCoordinates;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorsList;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.IBeretColors;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DoYouWantChangeColorMenu extends AbstractMenu implements  IImageColorChangeable{
    private static String CHANGE_COLOR = "CHANGE BERET COLOR";
    private static String LEAVE_PROPOSED = "LEAVE PROPOSED COLOR";
    private static String titleName = "- PLAYER COLOR SELECTION -";
    private String proposing = "STARTING A NEW GAME, YOU WILL PLAY IN A SKY BLUE BERET AND A SKY BLUE BRETON STRIPED TOP. IF YOU WANT, YOU CAN CHANGE THE COLORS OF THE BERET BEFORE STARTING THE GAME. TO DO THIS, YOU WILL NEED TO VIEW THE ADVERTISEMENT. ";
    private TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;
    //private Image clothes;
    private ImageWithData clothesImage;
    private Coordinate pictureCoordinate;

    public DoYouWantChangeColorMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.DO_YOU_WANT_TO_CHANGE_COLOR;
        initLanguageSpecific(engine);
        initAdSpecific(engine);
        init(engine, graphics);
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        loadClothes(engine, graphics);
        int headerTextPos = graphics.height/13;
        int textAreaHeight = (int) (graphics.height*0.25f);
        initTitle(engine, headerTextPos, titleName);
        int imageCenter = (int) (guiElements.get(0).getHeight()*4f+guiElements.get(0).getUpperY()+clothesImage.getImage().image.height);
        pictureCoordinate.y = imageCenter;
        String [] names;
        names = new String[]{GO_TO_PREVIOUS_MENU, CHANGE_COLOR, LEAVE_PROPOSED};
        int buttonsStartPos = (int) (graphics.height-graphics.height/9f);
        fillGuiWithCursorButtons(engine,buttonsStartPos, names,TO_UP);
        float coef = 2.5f;
        if (Program.SIDES_RELATIONSHIP < 1.8f){
            coef = 1.5f;
        }
        int textAreaPos = (int) (guiElements.get(0).getHeight()*coef+ pictureCoordinate.y+clothesImage.getImage().image.height/0.9f+textAreaHeight/2f);
        Rectangular rectangular = new Rectangular(graphics.width/2, textAreaPos, graphics.width*0.8f, textAreaHeight);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(proposing, rectangular, 9, 2000, graphics.textFont, graphics, true);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*2f);
    }

    private void loadClothes(PApplet engine, PGraphics graphics) {
        Image image = new Image(Program.getAbsolutePathToAssetsFolder("Clothes picture.png"));

        float coef = 0.75f;
        if (Program.SIDES_RELATIONSHIP < 1.8f)  coef = 0.62f;
        int width = (int)((float)graphics.width*coef);
        int originalWidth = image.image.width;
        int height = (int)((float)image.image.height*(float)width/(float)originalWidth);
        pictureCoordinate = new Coordinate(graphics.width/2, 0);    // Y will be reset
        clothesImage = new ImageWithCoordinates(image, width,  height, pictureCoordinate);
        updateBeretColorForGraphic(engine, null, null, clothesImage.getImage());
        //image.image.resize((int) (graphics.width*coef), 0);
        //image.setWidth((int)((float)graphics.width*coef));
        //image.
        //clothesImage.setWidth((int)((float)graphics.width*coef));
    }

    ImageWithData getClothes() {
        return clothesImage;
    }

    protected void initLanguageSpecific(PApplet engine) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            CHANGE_COLOR = "СМЕНИТЬ ЦВЕТ БЕРЕТА";
            LEAVE_PROPOSED = "ОСТАВИТЬ ПРЕДЛОЖЕННЫЙ ЦВЕТ";
            titleName = "- ВЫБОР РАСЦВЕТКИ -";
            proposing = "НАЧАВ НОВУЮ ИГРУ ВЫ БУДЕТЕ ИГРАТЬ В "+ getActualColorNameForBeret(engine)+" БЕРЕТЕ И БЕЛОЙ ТЕЛЬНЯШКЕ С ПОЛОСКАМИ ТАКОЙ ЖЕ РАСЦВЕТКИ. ЕСЛИ ВЫ ХОТИТЕ, ВЫ МОЖЕТЕ СМЕНИТЬ РАСЦВЕТКУ ПЕРЕД НАЧАЛОМ ИГРЫ. ДЛЯ ЭТОГО ПОТРЕБУЕТСЯ ПРОСМОТРЕТЬ РЕКЛАМУ. ";
        }
        else{
            proposing = "STARTING A NEW GAME, YOU WILL PLAY IN A "+ getActualColorNameForBeret(engine)+ " BERET AND A SKY BLUE BRETON STRIPED TOP. IF YOU WANT, YOU CAN CHANGE THE COLORS OF THE BERET BEFORE STARTING THE GAME. TO DO THIS, YOU WILL NEED TO VIEW THE ADVERTISEMENT. ";

        }
    }

    protected void initAdSpecific(PApplet engine) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            if (!Program.withAdds) proposing = "НАЧАВ НОВУЮ ИГРУ ВЫ БУДЕТЕ ИГРАТЬ В " + getActualColorNameForBeret(engine) + " БЕРЕТЕ И БЕЛОЙ ТЕЛЬНЯШКЕ С ПОЛОСКАМИ ТАКОЙ ЖЕ РАСЦВЕТКИ. НО ВЫ МОЖЕТЕ ВЫБРАТЬ ДРУГОЙ ЦВЕТ. ";
        }
        else
        {
        if (!Program.withAdds) proposing = "STARTING A NEW GAME, YOU WILL PLAY IN A " + getActualColorNameForBeret(engine) + " BERET AND A SKY BLUE BRETON STRIPED TOP. IF YOU WANT, YOU CAN CHANGE THE COLORS OF THE BERET. ";

        }
    }

    private String getActualColorNameForBeret(PApplet engine){
        //System.out.println("Actual language: " + Program.LANGUAGE);
        try {
            BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(engine);
            beretColorLoadingMaster.loadData();
            TwiceColor color = beretColorLoadingMaster.getBeretColor();
            String colorName = color.getName();
            if (Program.LANGUAGE == Program.ENGLISH) return colorName;
            else{
                String translatedName = getTranslatedNameForColor(colorName);
                return getInCaseConvertedNameForColor(translatedName);
            }
        }
        catch ( Exception e ){
            if (Program.LANGUAGE == Program.ENGLISH) return BeretColorsList.SKY_BLUE_NAME_EN;
            else {
                if (Program.LANGUAGE == Program.RUSSIAN) return getInCaseConvertedNameForColor(BeretColorsList.SKY_BLUE_NAME_RU);
                else return BeretColorsList.SKY_BLUE_NAME_EN;
            }
        }
    }


    private String getInCaseConvertedNameForColor(String colorName) {
        if (Program.LANGUAGE == Program.RUSSIAN) {
            if (colorName == IBeretColors.SKY_BLUE_NAME_RU) return "НЕБЕСНО ГОЛУБОМ";
            else if (colorName == IBeretColors.GREEN_NAME_RU) return "ЗЕЛЕНОМ";
            else if (colorName == IBeretColors.MAROON_NAME_RU) return "КРАПОВОМ";
            else if (colorName == IBeretColors.BLUE_NAME_RU) return "СИНЕМ";
            else if (colorName == IBeretColors.BLACK_NAME_RU) return "ЧЕРНОМ";
            else if (colorName == IBeretColors.ORANGE_NAME_RU) return "ОРАНЖЕВОМ";
            else if (colorName == IBeretColors.RED_NAME_RU) return "КРАСНОМ";
            else if (colorName == IBeretColors.GREY_NAME_RU) return "СЕРОМ";
            else if (colorName == IBeretColors.TAN_NAME_RU) return "ПЕСОЧНОМ";
            else if (colorName == IBeretColors.WHITE_NAME_RU) return "БЕЛОМ";
            else {
                System.out.println("This color name can not be cased from " + colorName);
                return colorName;
            }
        }
        else {
            System.out.println("For this language this color name can not be cased.");
            return colorName;
        }
    }

    private String getTranslatedNameForColor(String colorName) {
        if (Program.LANGUAGE == Program.RUSSIAN) {
            if (colorName.contains(IBeretColors.SKY_BLUE_NAME_EN)) return IBeretColors.SKY_BLUE_NAME_RU;
            else if (colorName.contains(IBeretColors.GREEN_NAME_EN)) return "ЗЕЛЕНЫЙ";
            else if (colorName.contains(IBeretColors.MAROON_NAME_EN)) return "КРАПОВЫЙ";
            else if (colorName.contains(IBeretColors.BLUE_NAME_EN)) return "СИНИЙ";
            else if (colorName.contains(IBeretColors.BLACK_NAME_EN)) return "ЧЕРНЫЙ";
            else if (colorName.contains(IBeretColors.ORANGE_NAME_EN)) return "ОРАНЖЕВЫЙ";
            else if (colorName.contains(IBeretColors.RED_NAME_EN)) return IBeretColors.RED_NAME_RU;
            else if (colorName.contains(IBeretColors.GREY_NAME_EN)) return IBeretColors.GREEN_NAME_RU;
            else if (colorName.contains(IBeretColors.TAN_NAME_EN)) return IBeretColors.TAN_NAME_RU;
            else if (colorName.contains(IBeretColors.WHITE_NAME_EN)) return IBeretColors.WHITE_NAME_RU;
            else {
                System.out.println("This color name " + colorName + "  can not be translated.");
                return colorName;
            }
        }
        else {
            System.out.println("For this language I can not translate the color name " + colorName);
            return colorName;
        }

        /*
         if (Program.LANGUAGE == Program.RUSSIAN) {
            if (colorName == IBeretColors.SKY_BLUE_NAME_EN) return IBeretColors.SKY_BLUE_NAME_RU;
            else if (colorName == IBeretColors.GREEN_NAME_EN) return "ЗЕЛЕНЫЙ";
            else if (colorName == IBeretColors.MAROON_NAME_EN) return "КРАПОВЫЙ";
            else if (colorName == IBeretColors.BLUE_NAME_EN) return "СИНИЙ";
            else if (colorName == IBeretColors.BLACK_NAME_EN) return "ЧЕРНЫЙ";
            else if (colorName == IBeretColors.ORANGE_NAME_EN) return "ОРАНЖЕВЫЙ";
            else if (colorName == IBeretColors.RED_NAME_EN) return IBeretColors.RED_NAME_RU;
            else if (colorName == IBeretColors.GREY_NAME_EN) return IBeretColors.GREEN_NAME_RU;
            else if (colorName == IBeretColors.TAN_NAME_EN) return IBeretColors.TAN_NAME_RU;
            else if (colorName == IBeretColors.WHITE_NAME_EN) return IBeretColors.WHITE_NAME_RU;
            else {
                System.out.println("This color name " + colorName + "  can not be translated.");
                return colorName;
            }
        }
        else {
            System.out.println("For this language I can not translate the color name " + colorName);
            return colorName;
        }
         */

    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.CAMPAIGN);
                }
                else if (element.getName() == CHANGE_COLOR) {
                    if (!Program.withAdds) {
                        gameMenusController.setNewMenu(MenuType.BERET_COLOR_CHANGING);
                    }
                    else {
                        System.out.println("Try to start add menu");
                        gameMenusController.setUserValue(MenuType.BERET_COLOR_CHANGING);
                        gameMenusController.setNewMenu(MenuType.REWARDED_ADDS_MENU);
                    }
                    dataToBeSaved = clothesImage;
                }
                else if (element.getName() == LEAVE_PROPOSED) {
                    gameMenusController.setNewMenu(MenuType.CAMPAIGN);
                }
            }
        }
        textInSimpleFrameDrawingController.update();
    }

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        textInSimpleFrameDrawingController.draw(graphic);
        if (clothesImage != null){
            //System.out.println("Clothes was drawn");
            clothesImage.draw(graphics);
        }
    }
}
