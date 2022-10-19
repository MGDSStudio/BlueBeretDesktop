package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;

public class AnimationDataToStore {
    private Image image;
    private String path;
    private int[] leftUpperCorner;
    private int[] rightLowerCorner;
    private int graphicWidth;
    private int graphicHeight;
    private byte rowsNumber;
    private byte collumnsNumber;
    private int frequency;

    public AnimationDataToStore(String path, int[] leftUpperCorner, int[] rightLowerCorner, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency){
        this.path = path;
        this.leftUpperCorner = leftUpperCorner;
        this.rightLowerCorner = rightLowerCorner;
        this.graphicHeight = graphicHeight;
        this.graphicWidth = graphicWidth;
        this.rowsNumber = rowsNumber;
        this.collumnsNumber = collumnsNumber;
        this.frequency = frequency;
    }

    public AnimationDataToStore(int[] leftUpperCorner, int[] rightLowerCorner, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency){
        this.path = HeadsUpDisplay.mainGraphicSource.getPath();
        this.leftUpperCorner = leftUpperCorner;
        this.rightLowerCorner = rightLowerCorner;
        this.graphicHeight = graphicHeight;
        this.graphicWidth = graphicWidth;
        this.rowsNumber = rowsNumber;
        this.collumnsNumber = collumnsNumber;
        this.frequency = frequency;
    }

    public AnimationDataToStore( int leftX, int upperY, int rightX, int lowerY, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency){
        this.path = HeadsUpDisplay.mainGraphicSource.getPath();
        leftUpperCorner = new int[] {(int) leftX, (int) upperY};
        rightLowerCorner = new int[] {(int) rightX, (int) lowerY};
        this.graphicHeight = graphicHeight;
        this.graphicWidth = graphicWidth;
        this.rowsNumber = rowsNumber;
        this.collumnsNumber = collumnsNumber;
        this.frequency = frequency;
    }



    public AnimationDataToStore(String path,  int leftX, int upperY, int rightX, int lowerY, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency){
        this.path = path;
        leftUpperCorner = new int[] {(int) leftX, (int) upperY};
        rightLowerCorner = new int[] {(int) rightX, (int) lowerY};
        this.graphicHeight = graphicHeight;
        this.graphicWidth = graphicWidth;
        this.rowsNumber = rowsNumber;
        this.collumnsNumber = collumnsNumber;
        this.frequency = frequency;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int[] getLeftUpperCorner() {
        return leftUpperCorner;
    }

    public void setLeftUpperCorner(int[] leftUpperCorner) {
        this.leftUpperCorner = leftUpperCorner;
    }

    public int[] getRightLowerCorner() {
        return rightLowerCorner;
    }

    public void setRightLowerCorner(int[] rightLowerCorner) {
        this.rightLowerCorner = rightLowerCorner;
    }

    public int getGraphicWidth() {
        return graphicWidth;
    }

    public void setGraphicWidth(int graphicWidth) {
        this.graphicWidth = graphicWidth;
    }

    public int getGraphicHeight() {
        return graphicHeight;
    }

    public void setGraphicHeight(int graphicHeight) {
        this.graphicHeight = graphicHeight;
    }

    public byte getRowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(byte rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public byte getCollumnsNumber() {
        return collumnsNumber;
    }

    public void setCollumnsNumber(byte collumnsNumber) {
        this.collumnsNumber = collumnsNumber;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
