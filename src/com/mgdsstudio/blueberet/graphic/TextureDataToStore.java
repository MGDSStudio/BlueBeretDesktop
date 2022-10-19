package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;

public class TextureDataToStore {
    private Image image;
    private String path;
    private int[] positions;
    private byte fill;

    public Image getImage() {
        return image;
    }

    public String getPath() {
        return deleteAssetsFromPath(path);
    }

    public int[] getPositions() {
        return positions;
    }

    public byte getFill() {
        return fill;
    }

    public int getGraphicLeftX(){
        if (positions != null) return positions[0];
        else {
            System.out.println("No data about key point on tileset");
            return 0;
        }
    }

    public int getGraphicLeftY(){
        if (positions != null) return positions[1];
        else {
            System.out.println("No data about key point on tileset");
            return 0;
        }
    }

    public int getGraphicRightX(){
        if (positions != null) return positions[2];
        else {
            System.out.println("No data about key point on tileset");
            return 0;
        }
    }

    public int getGraphicRightY(){
        if (positions != null) return positions[3];
        else {
            System.out.println("No data about key point on tileset");
            return 0;
        }
    }

    public TextureDataToStore(Image image, int[] positions, boolean fill){
        this.image = image;
        this.path = image.getPath();
        this.positions = positions;
        if (fill == true) this.fill = 1;
        else this.fill = 0;
    }

    public TextureDataToStore(Image image, int[] positions, byte fill){
        this.image = image;
        this.path = image.getPath();
        this.positions = positions;
        this.fill = fill;
    }

    private void init(){

    }

    private String deleteAssetsFromPath(String path){
        String newPath = new String();
        final String toFindString = "Assets\\";
        if (path.contains(toFindString)){
            int endOfAssetsName = path.indexOf(toFindString)+toFindString.length();
            //System.out.println("String was: " + path);
            newPath = path.substring(endOfAssetsName);
            //System.out.println("String is: " + newPath);
        }
        else newPath = path;
        return newPath;
    }


}
