package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.CustomOnPanelImage;
import com.mgdsstudio.blueberet.menusystem.menu.shop.FrameWithObjectData;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

import java.util.ArrayList;

public class SingleCellWithImage {

    protected int width, height;
    protected int xPos, yPos;
    protected int xNumber, yNumber;
    protected int alongX, alongY;
    protected boolean visible = true;
    protected int objectCodeNumber;
    protected ArrayList <CustomOnPanelImage> images;
    private final OpenableFrameController frameController;
    private int actualDrawnNumber = 0;
    private int actualWeaponsNumber;
    private Timer timerToNextChanging;
    private final static int TIME_TO_NEXT_CHANGING = 500;
    private Vec2 centerPos;

    private boolean switchedOn = true;

    public SingleCellWithImage(OpenableFrameController frameController, CustomOnPanelImage image, int xNumber, int yNumber, int alongX, int alongY){
        this.frameController = frameController;
        images = new ArrayList<>();

        width = frameController.mainFrame.getHeight();
        height = frameController.mainFrame.getHeight();
        this.xNumber = xNumber;
        this.yNumber = yNumber;
        this.alongX = alongX;
        this.alongY = alongY;
        objectCodeNumber = image.getType();
        actualWeaponsNumber = frameController.getCells().size();
        centerPos = getPosForLeftFrameAlongX(frameController);
        centerPos.x*=(actualWeaponsNumber+1);
        xPos = (int) centerPos.x-width/2;
        yPos = (int) centerPos.y-height/2;
        image.setBasicPosX((int) centerPos.x);
        image.setBasicPosY((int) centerPos.y);
        images.add(image);
        System.out.println("Basic image will be drawn at " + image.getBasicPosX() + "x" + image.getBasicPosY() );
    }

    public void addImage(CustomOnPanelImage image){
        image.setBasicPosX((int) centerPos.x);
        image.setBasicPosY((int) centerPos.y);
        System.out.println("New image will be drawn at " + image.getBasicPosX() + "x" + image.getBasicPosY() );
        //System.out.println("New image has number " + actualDrawnNumber + " and code " + objectCodeNumber);

        images.add(image);

        //imageZoneData = HUD_GraphicData.getImageZoneForObjectType(code, false);
        //System.out.println("Drawn area: " + imageZoneData.leftX + "x" + imageZoneData.upperY + " to "+  imageZoneData.rightX + "x" + imageZoneData.lowerY);
        //
        //System.out.print("Added new image " + image.getType() + " to cell " + xNumber + " along x " + alongX + " and data: " );
        //System.out.println(" " + image.getImageZoneData().leftX + "x" + image.getImageZoneData().upperY + " to "+  image.getImageZoneData().rightX + "x" + image.getImageZoneData().lowerY);

    }

    public void changeImage(){
        actualDrawnNumber++;
        if (actualDrawnNumber>(images.size()-1)) actualDrawnNumber = 0;
        objectCodeNumber = images.get(actualDrawnNumber).getType();
        //images.get(actualDrawnNumber).setText(getTextForNewCell());
        System.out.println("New image has number " + actualDrawnNumber + " and code " + objectCodeNumber);
    }



    /*
    public void addSubcell(CustomOnPanelImage image){
        subCell = new SingleCellWithImage(frameController, image, xNumber,yNumber,alongX, alongY);
    }

    public void changeToSubcell(){
        CustomOnPanelImage buffer = image;
        image = subCell.image;
        subCell.image = buffer;
    }*/

    public void draw(PGraphics graphics){
        if (visible && switchedOn) {
            images.get(actualDrawnNumber).drawWithNumber(graphics);
            //System.out.println("Drawn: " + images.get(actualDrawnNumber).getBasicPosX() + " x " + images.get(actualDrawnNumber).getBasicPosY());
        }
    }

    public boolean isMouseOnCell(int x, int y){
        if (switchedOn) {
            return GameMechanics.isPointInRect(x, y, xPos, yPos, width, height);
        }
        else return false;
    }

    protected Vec2 getPosForLeftFrameAlongX(OpenableFrameController frameController){
        Vec2 leftUpperCorner = frameController.getLeftUpperCornerForFullOpened();
        int height = frameController.mainFrame.getHeight();
        int width = frameController.mainFrame.getWidth();
        leftUpperCorner.x+=width/2;
        leftUpperCorner.y+=height/2;
        return leftUpperCorner;
    }

    public int getObjectCodeNumber() {
        return objectCodeNumber;
    }

    protected int getCenterPosX(){
        return (xPos+width/2);
    }

    protected int getCenterPosY(){
        return (yPos + height/2);
    }

    public CustomOnPanelImage getActualImage() {
        return images.get(actualDrawnNumber);
    }

    public boolean canBeChanged() {
        if (images.size() > 1) {
            if (timerToNextChanging == null) {
                timerToNextChanging = new Timer(TIME_TO_NEXT_CHANGING);
                return true;
            } else {
                if (timerToNextChanging.isTime()) {
                    timerToNextChanging.setNewTimer(TIME_TO_NEXT_CHANGING);
                    return true;
                } else return false;
            }
        }
        else return false;
    }

    public boolean hasCellInternalCells() {
        if (images.size()>1) return true;
        else return false;
    }

    public boolean isSwitchedOn() {
        return switchedOn;
    }

    public void setSwitchedOn(boolean switchedOn) {
        this.switchedOn = switchedOn;
    }

    public void drawArrow(PGraphics graphics) {
        images.get(actualDrawnNumber).drawArrow(graphics);
    }
}
