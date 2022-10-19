package com.mgdsstudio.blueberet.graphic;

public class ImageZoneVariableData extends ImageZoneSimpleData{
    private int actualLeftX, actualUpperY, actualRightX, actualLowerY;

    public ImageZoneVariableData(int leftX, int upperY, int rightX, int lowerY) {
        super(leftX, upperY, rightX, lowerY);
        this.actualLeftX = leftX;
        this.actualUpperY = upperY;
        this.actualRightX = rightX;
        this.actualLowerY = lowerY;
    }

    public ImageZoneVariableData(ImageZoneSimpleData data) {
        super(data.leftX, data.upperY, data.rightX, data.lowerY);
        this.actualLeftX = leftX;
        this.actualUpperY = upperY;
        this.actualRightX = rightX;
        this.actualLowerY = lowerY;
    }

    public int getActualLeftX() {
        return actualLeftX;
    }

    public int getActualUpperY() {
        return actualUpperY;
    }

    public int getActualRightX() {
        return actualRightX;
    }

    public int getActualLowerY() {
        return actualLowerY;
    }

    public void setActualLeftX(int actualLeftX) {
        this.actualLeftX = actualLeftX;
    }

    public void setActualUpperY(int actualUpperY) {
        this.actualUpperY = actualUpperY;
    }

    public void setActualRightX(int actualRightX) {
        this.actualRightX = actualRightX;
    }

    public void setActualLowerY(int actualLowerY) {
        this.actualLowerY = actualLowerY;
    }
}
