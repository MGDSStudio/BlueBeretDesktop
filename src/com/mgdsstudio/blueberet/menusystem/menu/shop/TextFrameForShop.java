package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public class TextFrameForShop extends AbstractFrameWithText{

    private final int TIME_BEFORE_FIRST_CLICK = 350;


    private float gapToFrame;
    private final PGraphics graphics;
    private  int width;
    private  int height;
    private final int NORMAL_STRINGS_ALONG_Y = 8;
    private int normalHeight;

    public static final int NORMAL_HEIGHT = 8;
    static final int FULL_HEIGHT = 16;

    public TextFrameForShop(Vec2 leftUpperCorner, int width, int height, String text, PGraphics graphics, int stringsAlongY) {
        this.leftUpperCorner = leftUpperCorner;
        if (text == null) text = "";
        this.graphics = graphics;
        this.width = width;
        this.height = height;
        this.normalHeight = height;
        init(width, height, text, graphics, stringsAlongY);
    }

    private void init(int width, int height, String text, PGraphics graphics, int stringsAlongY){

        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        gapToFrame = leftUpperCorner.x;
        Image image = HUD_GraphicData.mainGraphicFile;
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpperCorner);
        int textAreaHeight = (int) (frame.getHeight()/2-gapToFrame*2);
        Vec2 center = new Vec2(leftUpperCorner.x+width/2, leftUpperCorner.y+gapToFrame+textAreaHeight/2);
        int textAreaWidth = (int) (width-gapToFrame*2);
        Rectangular rectangular = new Rectangular(center, textAreaWidth, textAreaHeight);
        // int stringsAlongY = 8;   //WAS
        stringsAlongY = 9;
        int timeToShowMessage = 2000;
        PFont font = graphics.textFont;
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, stringsAlongY, timeToShowMessage,font, graphics, false);
    }

    private void initOld(int width, int height, String text, PGraphics graphics){

        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        gapToFrame = leftUpperCorner.x;
        Image image = HUD_GraphicData.mainGraphicFile;
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpperCorner);
        int textAreaHeight = (int) (frame.getHeight()/2-gapToFrame*2);
        Vec2 center = new Vec2(leftUpperCorner.x+width/2, leftUpperCorner.y+gapToFrame+textAreaHeight/2);
        int textAreaWidth = (int) (width-gapToFrame*2);
        Rectangular rectangular = new Rectangular(center, textAreaWidth, textAreaHeight);
        int stringsAlongY = 16;
        // int stringsAlongY = 8;   //WAS
        int timeToShowMessage = 2000;
        PFont font = graphics.textFont;
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, stringsAlongY, timeToShowMessage,font, graphics, true);
    }


    /*
    void recreateTextDrawingController(){

    }
    void recreateTextDrawingController(String text, int stringsAlongY){
        setText(text);
        float deltaY;
        if (stringsAlongY == NORMAL_STRINGS_ALONG_Y) {
            height = normalHeight;

        }
        else {
            float coef = (float)stringsAlongY/(float)NORMAL_STRINGS_ALONG_Y;
            height = (int) (coef*normalHeight);
        }
        System.out.println("New height: " + height + " new pos");
        deltaY = height-normalHeight;
        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        gapToFrame = leftUpperCorner.x;
        leftUpperCorner.y+=deltaY;
        Image image = HUD_GraphicData.mainGraphicFile;
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpperCorner);
        int textAreaHeight = (int) (frame.getHeight()/2-gapToFrame*2);
        Vec2 center = new Vec2(leftUpperCorner.x+width/2, leftUpperCorner.y+gapToFrame+textAreaHeight/2);
        int textAreaWidth = (int) (width-gapToFrame*2);
        Rectangular rectangular = new Rectangular(center, textAreaWidth, textAreaHeight);

        // int stringsAlongY = 8;   //WAS
        int timeToShowMessage = 2000;
        PFont font = graphics.textFont;
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, stringsAlongY, timeToShowMessage,font, graphics);
        setText(text);


   }*/


    public void update(PApplet engine){
        textInSimpleFrameDrawingController.update();
        updateStatement(engine);
    }

    private void updateStatement(PApplet engine) {
        if (timerBeforeFirstClick == null) timerBeforeFirstClick = new Timer(TIME_BEFORE_FIRST_CLICK);
        else if (timerBeforeFirstClick.isTime()){
            if (engine.mousePressed){
                if (GameMechanics.isPointInRect(engine.mouseX, engine.mouseY, textInSimpleFrameDrawingController.getTextArea())){
                    if (statement != PRESSED ){
                        statement = PRESSED;
                    }
                }
                else statement = ACTIVE;
            }
            else {
                if (GameMechanics.isPointInRect(engine.mouseX, engine.mouseY, textInSimpleFrameDrawingController.getTextArea())){
                    if (statement == PRESSED) {
                        statement = RELEASED;
                        System.out.println("Released 1");
                        if (!textInSimpleFrameDrawingController.isFullShown())
                            textInSimpleFrameDrawingController.setFullShow();
                    }
                }
                else {
                    if (statement == PRESSED) {
                        statement = RELEASED;
                        System.out.println("Released 2");
                        if (!textInSimpleFrameDrawingController.isFullShown()) textInSimpleFrameDrawingController.setFullShow();
                    }
                    else statement = ACTIVE;
                }
            }
        }
    }



    public float getGapToFrame() {
        return gapToFrame;
    }



}
