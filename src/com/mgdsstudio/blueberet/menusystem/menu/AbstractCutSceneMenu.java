package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class AbstractCutSceneMenu extends AbstractMenu{

    protected static final String PATH_SUFFIX = ".gif";

    protected int screens ;
    protected int actualScreen = 0;
    protected TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;
    protected int imageWidth;

    //private Timer timerForShow;
    protected String [] actualText;
    protected PImage actualImage;

    public void draw(PGraphics graphics){
        super.draw(graphics);
        graphics.pushMatrix();
        graphics.imageMode(PApplet.CENTER);
        if (actualImage != null) {
            graphics.image(actualImage, graphics.width / 2, graphics.height / 2);
        }
        textInSimpleFrameDrawingController.draw(graphics);
        graphics.popMatrix();
    }



    public void update(GameMenusController gameMenusController, int mouseX , int mouseY) {
        updateTextInFrameDrawing(gameMenusController);
        updateForceForward(gameMenusController);
    }

    private void updateForceForward(GameMenusController gameMenusController) {
        if (Editor2D.prevMousePressedStatement && !Program.engine.mousePressed){
            if (textInSimpleFrameDrawingController.isFullShown()){
                /*setNextScreen(gameMenusController.getEngine());
                if (actualScreen < screens) {
                    textInSimpleFrameDrawingController.setNewText(actualText[0]);
                }*/
            }
            else {
                textInSimpleFrameDrawingController.setFullShow();
            }
        }
    }

    protected void showFullText(){
        textInSimpleFrameDrawingController.setFullShow();
    }

    private void updateTextInFrameDrawing(GameMenusController gameMenusController) {
        textInSimpleFrameDrawingController.update();
        if (textInSimpleFrameDrawingController.isActualTextIsReadyToBeChanged()){
            System.out.println("Text is ready to be changed");
            setNextScreen(gameMenusController.getEngine());
            if (actualScreen < screens) {
                textInSimpleFrameDrawingController.setNewText(actualText[0], true);
            }
        }
        else if (gameMenusController.getEngine().mousePressed){
            showFullText();
        }
    }

    protected abstract void setNextScreen(PApplet engine);

    //abstract void setNextScreen(PApplet engine);
}
