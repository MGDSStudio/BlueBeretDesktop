package com.mgdsstudio.blueberet.graphic.howtoplayanimation;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.graphic.simplegraphic.ImageWithData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PGraphics;

class TouchingHand {
    private ImageWithData hand;
    //private ImageWithVariableScale hand;
    private HandMovementController handMovementController;
    public final static boolean LEFT = true;
    private boolean side;
    private Coordinate actualPos;

    public TouchingHand(HandMovementController handMovementController, boolean side) {
        //this.hand = hand;
        float scale = getScaleForActualScreen();
        //float scale = 2.5f;
        if (side == LEFT) scale = -1*scale;
        this.hand = new ImageWithData(GameMenusController.sourceFile, GameMenusController.hand, scale);
        this.handMovementController = handMovementController;
        this.side = side;
    }

    private float getScaleForActualScreen() {
        float widthForDebug = 230;
        float coef = (float) Program.DEBUG_DISPLAY_WIDTH/widthForDebug;
        float debugScale = 2.5f;
        return coef/(Program.engine.width/Program.DEBUG_DISPLAY_WIDTH);
    }

    public void update(){
        handMovementController.update();
        //handMovementController.update();
    }

    public void draw(PGraphics graphics){
        draw(graphics, 1);
    }

    public void draw(PGraphics graphics, float scale){
        graphics.pushMatrix();
        if (scale != 1) graphics.scale(scale);
        graphics.translate(actualPos.x, actualPos.y);
        hand.draw(graphics);
        if (scale != 1) graphics.scale(1f/scale);
        graphics.popMatrix();
    }

    Coordinate getActualPos() {
        return actualPos;
    }

    public void setActualPos(Coordinate actualPos) {
       this.actualPos = actualPos;
    }
}
