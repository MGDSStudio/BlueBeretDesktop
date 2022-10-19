package com.mgdsstudio.blueberet.oldlevelseditor;

public class ImageZoneData {
    private String name;
    private int leftUpperX, leftUpperY, rightLowerX, rightLowerY;

    public ImageZoneData(String name, int leftUpperX, int leftUpperY, int rightLowerX, int rightLowerY){
        this.name = name;
        this.leftUpperX = leftUpperX;
        this.leftUpperY = leftUpperY;
        this.rightLowerX = rightLowerX;
        this.rightLowerY = rightLowerY;
    }

    public String getName() {
        return name;
    }

    public int getLeftUpperX() {
        return leftUpperX;
    }

    public int getLeftUpperY() {
        return leftUpperY;
    }

    public int  getRightLowerX() {
        return rightLowerX;
    }

    public int  getRightLowerY() {
        return rightLowerY;
    }
}
