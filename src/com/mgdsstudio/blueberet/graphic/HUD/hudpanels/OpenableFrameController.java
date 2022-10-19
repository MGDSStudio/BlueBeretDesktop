package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;


import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.CustomOnPanelImage;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public abstract class OpenableFrameController extends FrameController{
    protected int actualArrayElementNumber = -1;    //in no objects
    protected FrameDimensionChanger frameDimensionChanger;
    protected ArrayList <SingleCellWithImage> cells;
    protected ArrayList<CustomOnPanelImage> images;
    protected final static String REST_TEXT = "";

    protected void initSelectingFrame(Vec2 posForSelectingFrame, int basicSourceForSimpleFrames, ImageZoneSimpleData basicRectForSimpleFrames) {
        float scale = 0.85f;
        int visibleWidth = (int) (mainFrame.getWidth()*scale);
        int visibleHeight = (int) (mainFrame.getHeight()*scale);
        secondaryFrame = new EightPartsFrameImage(basicRectForSimpleFrames, basicSourceForSimpleFrames, basicSourceForSimpleFrames, visibleWidth, visibleHeight, new Vec2(posForSelectingFrame.x-visibleWidth/2, posForSelectingFrame.y-visibleHeight/2));
    }

    public void closeFrame(){
        frameDimensionChanger.close();
    }

    @Override
    public boolean isFrameClicked() {
        return mainFrame.isClicked(Program.engine.mouseX, Program.engine.mouseY);
    }

    @Override
    public void clickOnFrame(GameRound gameRound) {
        frameDimensionChanger.clickOnFrame(gameRound);
    }

    public int getStartClosingFrameNumber(){
        return frameDimensionChanger.getStartClosingFrameNumber();
    }

    public int getStartOpeningFrameNumber(){
        return frameDimensionChanger.getStartOpeningFrameNumber();
    }

    public boolean isFrameClosed(){
        return frameDimensionChanger.isCompleteClosed();
    }

    public boolean isFrameOpened(){
        return frameDimensionChanger.isCompleteOpened();
    }

    public boolean isMouseOnFrame(int mouseX, int mouseY){
        if (GameMechanics.isPointInRect(mouseX, mouseY, mainFrame.getLeftUpperCorner().x, mainFrame.getLeftUpperCorner().y, mainFrame.getWidth(), mainFrame.getHeight())){
            return true;
        }
        else {
            return false;
        }
    }

    public void update(){
        frameDimensionChanger.update();
    }

    @Override
    public Vec2 getLeftUpperCornerForFullOpened() {
        Vec2 pos = new Vec2(0,0);
        if (frameDimensionChanger.getOpeningMethod() == FrameDimensionChanger.FROM_RIGHT_TO_LEFT) {
            pos.x = mainFrame.getLeftUpperCorner().x - frameDimensionChanger.getMaxWidth()+frameDimensionChanger.getBasicWidth();
            pos.y = mainFrame.getLeftUpperCorner().y - frameDimensionChanger.getMaxHeight()+frameDimensionChanger.getBasicHeight();
        }
        return pos;
    }

    protected Vec2 getCenterForSelectingFrame(int actualObject){
        Vec2 posForSelectingFrame = new Vec2(-400,-400);
        for (SingleCellWithImage cell : cells){
            if (cell.getObjectCodeNumber() == actualObject){
                posForSelectingFrame.x = cell.xPos+cell.width/2;
                posForSelectingFrame.y = cell.yPos+cell.height/2;
                System.out.println("Code for start selected object founded: " + actualObject);
            }
        }
        return posForSelectingFrame;
    }

    public ArrayList<SingleCellWithImage> getCells() {
        return cells;
    }

    protected void drawOnClosedFrameNumber(PGraphics graphics){
        images.get(actualArrayElementNumber).drawWithoutNumber(graphics);

    }

    public void draw(PGraphics graphics, int drawMethod){
        if (drawMethod == DRAW_IN_FRAME_CENTER) {
            if (isFrameClosed()) {
                if (actualArrayElementNumber >= 0 && actualArrayElementNumber< images.size()) {
                    drawOnClosedFrameNumber(graphics);
                }
            }
        }
        drawCells(graphics);
        if (frameDimensionChanger.isCompleteOpened()){
            secondaryFrame.draw(graphics);
        }
        mainFrame.draw(graphics);
        if (frameDimensionChanger.isClosedAtThisFrame()) frameDimensionChanger.setClosetAtThisFrame(false);
    }

    private void drawCells(PGraphics graphics) {
        //int i = 0;
        for (SingleCellWithImage singleCell : cells){
            if (isFrameOpened()){
                singleCell.draw(graphics);
                //System.out.println("Cell is drawn " + i);
                //i++;
            }
        }
    }

    public void setFrameToObject(int actualObject) {
        System.out.println("***Try to set new object for selecting " + actualObject + ". Objects: " + cells.size() + "; " + "; Images: " + images.size() + "; ");
        boolean founded = false;
        for (int i = 0; i < images.size(); i++){
            if (images.get(i).getType() == actualObject){
                actualArrayElementNumber = i;
                for (SingleCellWithImage cell : cells){
                    System.out.println(" cell code " + cell.getObjectCodeNumber());
                    if (cell.getObjectCodeNumber() == actualObject){
                        secondaryFrame.setCenterPosition(cell.xPos+cell.width/2, cell.yPos+cell.height/2);
                        founded = true;
                    }
                }
            }
            System.out.println(" Image code " + images.get(i).getType());
        }
        for (SingleCellWithImage cell : cells) System.out.println(" cell code " + cell.getObjectCodeNumber());
        System.out.println("***Founded: " + founded);
    }

    //Copied
    public int getSelectedObjectCode() {
        int code = -1;
        boolean onlyOneCellSelected = true;
        for (SingleCellWithImage cell : cells){
            if (cell.isMouseOnCell(Program.engine.mouseX, Program.engine.mouseY)){
                if (code<0){
                    code = cell.getObjectCodeNumber();
                    System.out.println("Cell " + Weapon.getWeaponTypeForCode(cell.getObjectCodeNumber()) + " was selected");
                }
                else {
                    onlyOneCellSelected = false;
                    System.out.println("Player has selected more than one cell");
                }
            }
        }
        if (!onlyOneCellSelected){
            System.out.println("We are testing only along one axis");
            int nearestCellAlongX = 0;
            int nearestDistance = 50000;
            for (int i = 0; i < cells.size(); i++){
                int dist = PApplet.abs(cells.get(i).getCenterPosX()-Program.engine.mouseX);
                if (dist < nearestDistance){
                  nearestDistance = dist;
                  nearestCellAlongX = i;
                }
            }
            code = cells.get(nearestCellAlongX).getObjectCodeNumber();
        }
        return code;
    }

    public void setActualArrayElementNumber(int actualArrayElementNumber) {
        this.actualArrayElementNumber = actualArrayElementNumber;
    }

    public abstract int getDefaultObjectCode();

    public boolean isClosedAtThisFrame(){
        return frameDimensionChanger.isClosedAtThisFrame();
    }
}
