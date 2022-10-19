package com.mgdsstudio.blueberet.graphic;

public class ImageZoneFullData extends ImageZoneSimpleData{
    private final String name;

    public ImageZoneFullData(String name, int leftUpperX, int leftUpperY, int rightLowerX, int rightLowerY){
        super(leftUpperX, leftUpperY, rightLowerX, rightLowerY);
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString(){
        return leftX +"x"+ upperY +";"+ rightX +"x"+ lowerY;
    }
}
