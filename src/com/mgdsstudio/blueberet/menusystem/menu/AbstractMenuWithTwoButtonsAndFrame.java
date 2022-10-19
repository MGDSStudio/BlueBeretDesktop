package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithCoordinates;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class AbstractMenuWithTwoButtonsAndFrame extends AbstractMenu{
    //protected static String YES = "YES, TELL ME MORE";
    //protected static String NO = "START THE GAME";
    protected static String MESSAGE_TEXT = "IF YOU PLAY AT THE FIRST TIME IT IS RECOMMENDABLE TO LEARN HOW TO PLAY. DO YOU WANT TO GET TO KNOW THE GAME CONTROL SYSTEM?";
    protected static String titleName = "- DO YOU KNOW HOW TO PLAY? -";
    protected TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;
    protected boolean withMenuHiding = true;
    protected EightPartsFrameImage frame;

    /*
    public AbstractMenuWithTwoButtonsAndFrame(PApplet engine, PGraphics graphics, MenuType menuType) {
        type = menuType;
        initLanguageSpecific();
        init(engine, graphics, null);
        initFrame(engine);
    }*/

    public AbstractMenuWithTwoButtonsAndFrame(PApplet engine, PGraphics graphics, MenuType menuType, String [] names) {
        type = menuType;
        initLanguageSpecific();
        init(engine, graphics, names);
        initFrame(engine);
    }

    protected final void initFrame(PApplet engine) {
        int upperY = engine.height/12;
        int lowerY= engine.height-upperY ;
        int frameHeight = lowerY-upperY-2*guiElements.get(0).getHeight();
        float widthCoefficient = 0.85f;
        int frameWidth = (int)(engine.width*widthCoefficient);
        Vec2 leftUpper = new Vec2(engine.width*(1f-widthCoefficient)/2f, upperY+guiElements.get(0).getHeight());
        int basicWidth = (int) (0.5f*(frameWidth * PIXELS_ALONG_X_FOR_NES)/engine.width);
        System.out.println("Frame width = " + basicWidth);
        frame = new EightPartsFrameImage(GameMenusController.sourceFile, GameMenusController.dialogFrameZone, basicWidth, basicWidth, frameWidth, frameHeight,leftUpper);
    }

    protected final void init(PApplet engine, PGraphics graphics,  String [] buttonNames) {
        int upperY = engine.height/2;
        int headerTextPos = graphics.height/13;
        int textAreaHeight = (int) (graphics.height*0.4f);
        //int textAreaHeight = (int) (graphics.height*0.2f);
        initTitle(engine, headerTextPos, titleName);
        upperY+= (guiElements.get(guiElements.size()-1).getHeight()*2);

        int upperFramePos = engine.height/12;
        int lowerY= engine.height-upperFramePos*2 ;
        String [] names = buttonNames;
        fillGuiWithCursorButtons(engine, lowerY, names,TO_UP);
        Coordinate pictureCoordinate;
        pictureCoordinate = new Coordinate(graphics.width/2, 0);    // Y will be reset
        Image image = new Image(Program.getAbsolutePathToAssetsFolder("Clothes picture.png"));
        float coef = 0.75f;
        if (Program.SIDES_RELATIONSHIP < 1.8f)  coef = 0.62f;
        int width = (int)((float)graphics.width*coef);
        int originalWidth = image.image.width;
        int height = (int)((float)image.image.height*(float)width/(float)originalWidth);
        ImageWithData clothesImage;
        clothesImage = new ImageWithCoordinates(image, width,  height, pictureCoordinate);
        int imageCenter = (int) (guiElements.get(0).getHeight()*4f+guiElements.get(0).getUpperY()+clothesImage.getImage().image.height);
        pictureCoordinate.y = imageCenter;
        Rectangular rectangular = new Rectangular(graphics.width/2, graphics.height/3, graphics.width*0.65f, textAreaHeight);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(MESSAGE_TEXT, rectangular, 16, 2000, graphics.textFont, graphics,true);
        //textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(MESSAGE_TEXT, rectangular, 8, 2000, graphics.textFont, graphics);
        //
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*2f);
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements) {
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                buttonPressed(gameMenusController, element.getName());
            }
        }
        textInSimpleFrameDrawingController.update();
    }

    protected abstract void buttonPressed(GameMenusController gameMenusController, String name);

    protected abstract void initLanguageSpecific();

    @Override
    public final void draw(PGraphics graphic){
        if (withMenuHiding) {
            super.draw(graphic);
        }
        frame.draw(graphic);
        textInSimpleFrameDrawingController.draw(graphic);
    }

}
