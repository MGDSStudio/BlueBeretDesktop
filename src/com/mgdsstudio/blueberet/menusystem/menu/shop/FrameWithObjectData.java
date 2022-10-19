package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public class FrameWithObjectData extends AbstractFrameWithText{
    private ImageZoneSimpleData imageZoneSimpleData;
    private TextInSimpleFrameDrawingController nameText;
    private final int TIME_BEFORE_FIRST_CLICK = 350;
    private float gapToFrame;

    private EightPartsFrameImage imageFrame;

    private final static int HIDDEN = 0;
    private final static int STARTED_TO_SHOW = 1;
    private final static int FULL_SHOWN = 2;
    private int stage = HIDDEN;

    private int imageWidth, imageHeight;

    private final PGraphics pGraphics;

    public FrameWithObjectData(Vec2 leftUpperCorner, int width, int height, String text, PGraphics graphics, ImageZoneSimpleData imageZoneSimpleData) {
        this.leftUpperCorner = leftUpperCorner;
        if (text == null) text = "";
        this.imageZoneSimpleData = imageZoneSimpleData;
        init(width, height, text, graphics);
        this.pGraphics = graphics;
    }

    private void init(int width, int height, String text, PGraphics graphics){
        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        gapToFrame = leftUpperCorner.x;
        Image image = HUD_GraphicData.mainGraphicFile;
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpperCorner);
        int textAreaHeight = (int) (frame.getHeight()/2-gapToFrame*2);
        Vec2 center = new Vec2(leftUpperCorner.x+width/2, leftUpperCorner.y+gapToFrame+textAreaHeight/2);
        int textAreaWidth = (int) (width-gapToFrame*2);
        Rectangular rectangular = new Rectangular(center, textAreaWidth, textAreaHeight);
        int stringsAlongY = 8;
        int timeToShowMessage = 2000;
        PFont font = graphics.textFont;
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, stringsAlongY, timeToShowMessage,font, graphics, false);

    }

    public void update(PApplet engine){
        if (stage != HIDDEN) {
            textInSimpleFrameDrawingController.update();
            nameText.update();
            updateStatement(engine);
        }
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

    @Override
    public void draw(PGraphics graphics){
        if (visible) {
            if (stage != HIDDEN) {
                frame.draw(graphics);
                imageFrame.draw(graphics);
                drawImage(graphics);
                textInSimpleFrameDrawingController.draw(graphics);
                nameText.draw(graphics);
            }
        }
        //textInSimpleFrameDrawingController.draw(graphics);
    }

    private void drawImage(PGraphics graphics) {
        if (imageZoneSimpleData != null){
            //System.out.println("Drawn");
            graphics.pushMatrix();
            graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(),
                    imageFrame.getLeftUpperCorner().x+imageFrame.getWidth()/2,
                    imageFrame.getLeftUpperCorner().y+imageFrame.getHeight()/2,
                    imageWidth, imageHeight,
                    imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
            graphics.popMatrix();
        }
    }

    public void startToShow(){
        stage = STARTED_TO_SHOW;
    }

    public void setText(String text, String name){
        createTextFrameOnLowerSide(text, false);
        createTextFrameOnRightSide(name, false);
    }

    @Override
    public void setText(String name, boolean alignmentAlongWidth){
        //createTextFrameOnLowerSide(text);
        createTextFrameOnRightSide(name, alignmentAlongWidth);
    }

    private void createTextFrameOnLowerSide(String text, boolean alignmentAlongWidth){
        float gap = imageFrame.getLeftUpperCorner().x-frame.getLeftUpperCorner().x;
        float leftUpperX = imageFrame.getLeftUpperCorner().x;
        float leftUpperY = imageFrame.getLeftUpperCorner().y+imageFrame.getHeight()+gap;
        float textAreaWidth = (frame.getWidth()-2*gap);
        //float textAreaHeight = imageFrame.getLeftUpperCorner().y+imageFrame.getHeight()+gap-frame.getLeftUpperCorner().y-gap;
        float lowerSide = frame.getLeftUpperCorner().y+frame.getHeight();
        float upperSide = imageFrame.getLeftUpperCorner().y+imageFrame.getHeight();
        float textAreaHeight = lowerSide-upperSide-gap;
        System.out.println("Text height " + textAreaHeight);
        float centerX = leftUpperX+textAreaWidth/2;
        float centerY = leftUpperY+textAreaHeight/2;
        Rectangular textArea = new Rectangular(centerX, centerY, textAreaWidth, textAreaHeight);
        int stringsAlongY = 6;
        int timeToShowMessage = 2000;


        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, textArea, stringsAlongY, timeToShowMessage,pGraphics.textFont, pGraphics, alignmentAlongWidth);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*3);
    }

    private void createTextFrameOnRightSide(String text, boolean alignmentAlongWidth){
        float gap = imageFrame.getLeftUpperCorner().x-frame.getLeftUpperCorner().x;
        //float gap = leftUpperCorner.x;
        float leftUpperX = imageFrame.getLeftUpperCorner().x+imageFrame.getWidth()+gap;
        float leftUpperY = imageFrame.getLeftUpperCorner().y;
        float textAreaWidth = (frame.getWidth()+frame.getLeftUpperCorner().x)-leftUpperX-gap;
        float textAreaHeight = frame.getHeight()-gap;
        System.out.println("Text height " + textAreaHeight);
        float centerX = leftUpperX+textAreaWidth/2;
        float centerY = leftUpperY+textAreaHeight/2;
        Rectangular textArea = new Rectangular(centerX, centerY, textAreaWidth, textAreaHeight);
        int stringsAlongY = 8;
        int timeToShowMessage = 2000;


        nameText = new TextInSimpleFrameDrawingController(text, textArea, stringsAlongY, timeToShowMessage,pGraphics.textFont, pGraphics, alignmentAlongWidth);
        nameText.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*3);
        nameText.setFullShow();
    }

    public void createImage(WeaponType weaponType){

        imageZoneSimpleData = HUD_GraphicData.getImageZoneForWeaponType(weaponType);
        System.out.println("Image was set for " + weaponType + " and is null: " + (imageZoneSimpleData == null));
        int additionalGap = (int) (frame.getWidth()*0.1f);
        float widthRelationship = (float)UpperPanel.WIDTH_FOR_WEAPON_FRAME/(float)UpperPanel.HEIGHT_FOR_WEAPON_FRAME;
        /*
        WIDTH_FOR_WEAPON_FRAME = (int) (HEIGHT*1f);
        public final static int HEIGHT_FOR_WEAPON_FRAME = (int) (HEIGHT*0.75f);*/
        imageFrame.setWidth((int) (widthRelationship*imageFrame.getHeight()));


        imageWidth = imageFrame.getWidth()-additionalGap;
        float reductionCoef = (float)imageWidth/(float)(imageZoneSimpleData.rightX-imageZoneSimpleData.leftX);

        imageHeight = (int) (reductionCoef*(float)(imageZoneSimpleData.lowerY-imageZoneSimpleData.upperY));
    }

    public void createImage(int objectNumber, int frameWidth, int frameHeight){
        imageZoneSimpleData = HUD_GraphicData.getImageZoneForObjectType(objectNumber, false);
        int additionalGap = (int) (frame.getWidth()*0.1f);
        //int frameWidth = frame.getWidth();
        //int frameHeight = frame.getHeight();
        float widthRelationship = (float)frameWidth/(float)frameHeight;
        //float widthRelationship = (float)UpperPanel.WIDTH_FOR_WEAPON_FRAME/(float)UpperPanel.HEIGHT_FOR_WEAPON_FRAME;
        /*
        WIDTH_FOR_WEAPON_FRAME = (int) (HEIGHT*1f);
        public final static int HEIGHT_FOR_WEAPON_FRAME = (int) (HEIGHT*0.75f);*/
        imageFrame.setWidth((int) (widthRelationship*imageFrame.getHeight()));
        imageHeight = imageFrame.getHeight()-additionalGap ;
        imageWidth = imageFrame.getWidth()-additionalGap;
    }

    public void hide(){
        stage = HIDDEN;
    }

    public void createImageFrame(float gapX, float gapY, float height) {
        Vec2 objectDataFramePos = new Vec2(leftUpperCorner.x+gapX, leftUpperCorner.y+gapY);

        //int height = (int) (frame.getHeight()-gapX*2);
        int width = (int) (frame.getHeight()-gapX*2);
        //System.out.println("Zone width: " + width + " x " + height);
        //ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        imageFrame = new EightPartsFrameImage(frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, (int) height, objectDataFramePos);
    }
}
