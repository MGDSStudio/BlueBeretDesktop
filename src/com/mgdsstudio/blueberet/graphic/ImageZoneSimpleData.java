package com.mgdsstudio.blueberet.graphic;

public class ImageZoneSimpleData {
    public final int leftX, upperY, rightX, lowerY;

    public ImageZoneSimpleData(int leftX, int upperY, int rightX, int lowerY){
        this.leftX = leftX;
        this.upperY = upperY;
        this.rightX = rightX;
        this.lowerY = lowerY;
    }

    /*
    public int leftX {
        return leftX;
    }

    public int upperY {
        return upperY;
    }

    public int rightX {
        return rightX;
    }

    public int lowerY {
        return lowerY;
    }*/

    @Override
    public String toString(){
        String text = ""+leftX + "," + upperY+";"+rightX+","+lowerY;
        return text;
    }
}
