package com.mgdsstudio.texturepacker;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.IntList;
import processing.data.StringList;

public class TextureEncryptManager extends TexturePacker{
    private String[] data;

    public TextureEncryptManager(String path, PApplet engine) {
        super(path, engine);
    }
    //private int pixelsPositions;



    public PImage encrypt(){
        PImage image = engine.loadImage(path);
        if (image != null){
            //engine.image(image,0,0);
            IntList originalPixels = getUniquePixelsCount(image);
            encryptPixels(image, originalPixels, image.width, image.height);

            saveToOriginalDir(path);
        }
        else System.out.println("Can not load file: " + path);
        return image;
    }



    private void saveToOriginalDir(String path) {
        int point = -1;
        for (int i = (path.length()-1); i >= 0; i--){
            if (path.charAt(i) == '.'){
                point = i;
                break;
            }
        }
        String prefix = path.substring(0,point);
        String fullPath = prefix+ MGDS_FILE_EXTENSION;
        engine.saveStrings(fullPath, data);
        System.out.println("Data saved at " + fullPath + " but source had path: " + path);
    }


    private void encryptPixels(PImage image, IntList originalPixels, int imageWidth, int imageHeight) {
        StringList colorDataList = new StringList();
        StringList coordinatesList = new StringList();
        for (int i = 0; i < originalPixels.size(); i++){
            colorDataList.append(""+originalPixels.get(i));
            coordinatesList.append(START_PIXELS_CHAR);
        }
        colorDataList.append(""+imageWidth+WIDTH_HEIGHT_CHAR+imageHeight);
        coordinatesList.append("");
        int [] pixels = image.pixels;
        int x,y;
        int alpha = 0;
        for (int i = 0; i < pixels.length; i++) {
            x = getX(i, imageWidth, imageHeight);
            y = getY(i, imageWidth, imageHeight);
            alpha = (int) engine.alpha(pixels[i]);
            if (alpha != 0) {
                for (int j = 0; j < originalPixels.size(); j++) {
                    if (pixels[i] == originalPixels.get(j)) {
                        String dataToBeSaved;
                        dataToBeSaved = ""+x + DEVIDER_X_Y + y + DEVIDER_Y_VALUE;
                        String actual = coordinatesList.get(j)+dataToBeSaved;
                        coordinatesList.set(j,actual);
                        break;
                    }
                }
            }
        }
        data = new String[colorDataList.size()];
        for (int i = 0; i < data.length; i++){
            data[i] = colorDataList.get(i)+coordinatesList.get(i);
        }
    }


    private void encryptPixelsOld(PImage image, IntList originalPixels, int imageWidth, int imageHeight) {
        StringList list = new StringList();
        for (int i = 0; i < originalPixels.size(); i++){
            list.append(""+originalPixels.get(i));
        }
        list.append(END_PIXEL_DATA_CHAR);
        //image.loadPixels();
        int [] pixels = image.pixels;
        int x,y;
        int alpha = 0;
        for (int i = 0; i < pixels.length; i++) {
            x = getX(i, imageWidth, imageHeight);
            y = getY(i, imageWidth, imageHeight);
            alpha = (int) engine.alpha(pixels[i]);
            if (alpha != 0) {
                for (int j = 0; j < originalPixels.size(); j++) {
                    if (pixels[i] == originalPixels.get(j)) {
                        String dataToBeSaved;
                        dataToBeSaved = "" + x + DEVIDER_X_Y + y + DEVIDER_Y_VALUE + originalPixels.get(j);
                        list.append(dataToBeSaved);
                        break;
                    }
                }
            }
        }
        data = new String[list.size()];
        for (int i = 0; i < data.length; i++){
            data[i] = list.get(i);
        }
    }

    private int getY(int i, int imageWidth, int imageHeight) {
        return engine.floor(i/imageWidth);
    }

    private int getX(int i, int imageWidth, int imageHeight) {
        return i%imageWidth;
    }

    private IntList getUniquePixelsCount(PImage image){
        IntList originalPixels = new IntList();
        image.loadPixels();
        int [] pixels = image.pixels;
        int color;
        for (int i  = 0; i < pixels.length; i++){
            boolean exists = false;
            for (int j = 0; j < originalPixels.size(); j++){
                if (originalPixels.get(j) == pixels[i]){
                    exists = true;
                    break;
                }
            }
            if (!exists){
                color = pixels[i];
                originalPixels.append(color);
            }
        }
        if (debug) System.out.println("We have " + originalPixels.size() + " unique colors in " + pixels.length + " pixels" );
        return originalPixels;
    }


    /*
    image.loadPixels();
        int [] pixels = image.pixels;
        int lastElement = 0;
        int [] originalPixels = new int[pixels.length];
        for (int i  = 0; i < pixels.length; i++){
            boolean exists = false;
            int color = -1;
            for (Integer integer : originalPixels){
                if (integer.intValue() == pixels[[i]]){
                    exists = true;
                    color = integer.intValue();
                    lastElement++;
                    break;
                }
                //else System.out.println("Color: " + integer.intValue() + " is not " + pixels[i]);
            }
            if (!exists){
                originalPixels.append(color);
            }
        }
        System.out.println("We have " + originalPixels.size() + " unique colors in " + pixels.length + " pixels" );
        return originalPixels;
     */


    private IntList getUniquePixelsCountWithSingleColors(PImage image){
        IntList originalPixels = new IntList();
        image.loadPixels();
        int [] pixels = image.pixels;
        int r = -1;
        int g = -1;
        int b = -1;
        int a = -1;

        int r1 = -1;
        int g1 = -1;
        int b1 = -1;
        int a1 = -1;

        for (int i  = 0; i < pixels.length; i++){
            boolean exists = false;
            int color = -1;
            r = (int) engine.red(pixels[i]);
            g = (int) engine.green(pixels[i]);
            b = (int) engine.blue(pixels[i]);
            a = (int ) engine.alpha(pixels[i]);
            for (Integer integer : originalPixels){
                r1 = (int) engine.red(integer);
                g1 = (int) engine.green(integer);
                b1 = (int) engine.blue(integer);
                a1 = (int ) engine.alpha(integer);
                if (r==r1 && g == g1 && b ==b1 && a == a1 ){
                    exists = true;
                    color = integer.intValue();
                    break;
                }
                //else System.out.println("Color: " + integer.intValue() + " is not " + pixels[i]);
            }
            if (!exists){
                originalPixels.append(color);
            }
        }
        System.out.println("We have " + originalPixels.size() + " unique colors");
        return originalPixels;
    }
}
