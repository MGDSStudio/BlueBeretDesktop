package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class NES_ColorButton extends NES_ElementWithFrameSelection{
    private TwiceColor twiceColor;
    private Image image;
    private int imageHeight;
    private ImageZoneSimpleData imageData;
    //private int width, height;
    //private float cursorDimensionCoefficient = 1.01f;

    public NES_ColorButton(int centerX, int centerY, int width, int height, String name, PGraphics graphics, float cursorDimensionCoefficient, TwiceColor twiceColor) {
        super(centerX, centerY, width, height, name, graphics, cursorDimensionCoefficient);

        init(centerX, centerY);
        createImage(twiceColor, 3);
    }

    public NES_ColorButton(int centerX, int centerY, int width, int height, String name, PGraphics graphics, TwiceColor twiceColor) {
        super(centerX, centerY, width, height, name, graphics,1.01f);
        init(centerX, centerY);
        createImage(twiceColor, 3);
    }

    private void createImage(TwiceColor twiceColor, int colors) {
        String path = "Color button template " + colors + ".gif";
        image = new Image(Program.getAbsolutePathToAssetsFolder(path));
        if (image.getImage() != null){
            changeImageDimensions();
            setNewColor(twiceColor, colors);
        }
    }

    private void setNewColor(TwiceColor twiceColor, int colors) {
        image.getImage().loadPixels();
        int [] pixels = image.getImage().pixels;
        int [] grayColorValues = getColorValues(pixels, colors);
        int brightRed = twiceColor.getBrightRed();
        int brightGreen = twiceColor.getBrightGreen();
        int brightBlue = twiceColor.getBrightBlue();
        int darkRed = twiceColor.getDarkRed();
        int darkGreen = twiceColor.getDarkGreen();
        int darkBlue = twiceColor.getDarkBlue();
        int deltaRed = (PApplet.abs(brightRed-darkRed));
        int deltaGreen = (PApplet.abs(brightGreen-darkGreen));
        int deltaBlue = (PApplet.abs(brightBlue-darkBlue));
        int stepToNextRedColor = deltaRed/(grayColorValues.length-1);
        int stepToNextBlueColor = deltaBlue/(grayColorValues.length-1);
        int stepToNextGreenColor = deltaGreen/(grayColorValues.length-1);
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == grayColorValues[0]){       //most dark
                pixels[i] = twiceColor.getDarkColor();
            }
            else if (pixels[i] == grayColorValues[grayColorValues.length-1]){       //most bright
                pixels[i] = twiceColor.getBrightColor();
            }
        }
        for (int j = 1; j < (grayColorValues.length-1); j++){
            int newRed = brightRed+stepToNextRedColor*(j);
            int newGreen = brightGreen+stepToNextGreenColor*(j);
            int newBlue = brightBlue+stepToNextBlueColor*j;
            int newColor = Program.engine.color(newRed, newGreen, newBlue);
            System.out.println("New color for pixel is: " + newRed + "," + newGreen + "," + newBlue );
            for (int i = 0; i < pixels.length; i++){
                if (pixels[i] == grayColorValues[j]){
                    pixels[i] = newColor;
                }
            }
        }
        image.getImage().updatePixels();
    }

    private int[] getColorValues(int[] pixels, int number) {
        ArrayList <Integer> colors = new ArrayList<>();
        for (int i = 0; i < pixels.length; i++){
            boolean mustBeAdded = true;
            for (int j = 0; j < colors.size(); j++){
                if (colors.get(j) == pixels[i]){
                    mustBeAdded = false;
                }
            }
            if (mustBeAdded == true){
                Integer newColor = new Integer(pixels[i]);
                colors.add(newColor);
            }
        }
        int [] variants = new int[colors.size()];
        for (int i = 0; i < variants.length; i++){
            variants[i] = colors.get(i);
        }
        System.out.println("There are " + colors.size() + " colors in graphic file");
        if (number != colors.size()){
            System.out.println("There are trouble. The number of colors in the template is not right!");
        }
        return variants;
    }

    private void changeImageDimensions() {
        float relationship = (float)width/(float)height;
        imageHeight = (int)((float) width/relationship);
        imageData = new ImageZoneSimpleData(0,0, image.image.width, imageHeight);
        //image.image.resize(width, height);

    }

    @Override
    protected void updateFunction() {

    }

    @Override
    protected void finalize(){
        try {
            Program.engine.g.removeCache(image.getImage());
            if (image.image != null) image.image = null;
            image = null;
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        graphics.pushStyle();
        graphics.imageMode(PApplet.CENTER);
        drawImage(graphics);
        graphics.popStyle();
        if (actualStatement == PRESSED || actualStatement == RELEASED) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CORNER);
            drawCursor(graphics);
            graphics.popStyle();
        }

        //drawName(graphics);
    }

    private void drawImage(PGraphics graphics) {
        graphics.image(image.image, x,y, width, height, imageData.leftX, imageData.upperY, imageData.rightX, imageData.lowerY);
    }

    @Override
    public void update(int mouseX, int mouseY){
        if (prevStatement != actualStatement) prevStatement = actualStatement;
        if (actualStatement != BLOCKED && actualStatement != HIDDEN){
            if (GameMechanics.isPointInRect(mouseX, mouseY, leftX, upperY, width, height)){
                if (Program.engine.mousePressed){
                    if (actualStatement != PRESSED) actualStatement = PRESSED;
                }
                else if (actualStatement == PRESSED){
                    actualStatement = RELEASED;
                }
                else if (actualStatement == RELEASED ){
                    actualStatement = ACTIVE;
                }
            }
            else{
                actualStatement = ACTIVE;
            }
        }
    }

}
