package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithFrameSelection;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ListButton;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class AbstractMenuWithTitleAndBackButton extends AbstractMenu{
    protected String titleName = "-       -";
    protected EightPartsFrameImage frame;
    protected boolean moreThanOne;
    protected static int actualPage = -1;

    protected int pages;
    protected String PREV = " PREV";
    protected String NEXT = " NEXT";

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine,graphics);
        initLanguageSpecificButtons();
        int upperY = engine.height/12;
        initTitle(engine, upperY, titleName);
        int lowerY= engine.height-upperY ;
        String [] names = new String[]{GO_TO_PREVIOUS_MENU};
        fillGuiWithCursorButtons(engine,lowerY, names,TO_DOWN);
        int frameHeight = lowerY-upperY-2*guiElements.get(0).getHeight();
        float widthCoefficient = 0.85f;
        int frameWidth = (int)(engine.width*widthCoefficient);
        Vec2 leftUpper = new Vec2(engine.width*(1f-widthCoefficient)/2f, upperY+guiElements.get(0).getHeight());
        int basicWidth = (int) (0.5f*(frameWidth * PIXELS_ALONG_X_FOR_NES)/engine.width);
        System.out.println("Frame width = " + basicWidth);
        frame = new EightPartsFrameImage(GameMenusController.sourceFile, GameMenusController.dialogFrameZone, basicWidth, basicWidth, frameWidth, frameHeight,leftUpper);

    }

    protected void initLanguageSpecificButtons() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            PREV = " ПРЕД.";
            NEXT = "СЛЕД. ";

        }
        else{
            PREV = " PREV";
            NEXT = "NEXT ";
        }
    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == PREV) {
                    actualPage--;
                    setPrevPage();
                    System.out.println("Prev page");
                    break;
                }
                else if (element.getName() == NEXT){
                    actualPage++;
                    setNextPage();
                    System.out.println("Next page");
                    break;
                }
            }
        }
    }




    protected abstract void setPrevPage();

    protected abstract void setNextPage();

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        frame.draw(graphic);
    }


    protected void addPrevNextButtons(PApplet engine){
        NES_GuiElement lower = guiElements.get(guiElements.size()-1);
        int upperSide = lower.getUpperY()+lower.getHeight();
        int lowerSide = (int) (frame.getLeftUpperCorner().y+frame.getHeight());
        int height = lowerSide-upperSide;
        int yPos = lowerSide-guiElements.get(guiElements.size()-1).getHeight()*3;
        int buttonWidth = frame.getWidth()/6;
        //int centerX, int centerY, int width, int height, String name
        boolean withNext = false;
        boolean withPrev = false;
        if (actualPage<(pages-1)) withNext = true;
        if (actualPage>0) withPrev = true;
        if (withPrev){
            int xPos = (int) (frame.getLeftUpperCorner().x+ frame.getWidth()/6);
            NES_ButtonWithFrameSelection prev = new NES_ButtonWithFrameSelection(xPos, yPos, buttonWidth, NES_ListButton.NORMAL_HEIGHT*2, PREV, graphics);
            guiElements.add(prev);
        }
        if (withNext){
            int xPos = (int) (frame.getLeftUpperCorner().x+frame.getWidth()-frame.getWidth()/6);
            NES_ButtonWithFrameSelection next = new NES_ButtonWithFrameSelection(xPos, yPos, buttonWidth, NES_ListButton.NORMAL_HEIGHT*2, NEXT, graphics);
            guiElements.add(next);
        }
        System.out.println("With prev " + withPrev + "; Next: " + withNext + "Pages: " + pages);
    }

    protected final void removePrevAndNextButtons() {
        for (int  i = guiElements.size()-1; i >= 0; i--){
            if (guiElements.get(i).getClass() == NES_ButtonWithFrameSelection.class){
                if (guiElements.get(i).getName() == PREV || guiElements.get(i).getName() == NEXT){
                    guiElements.remove(guiElements.get(i));
                }
            }
        }
    }


}
