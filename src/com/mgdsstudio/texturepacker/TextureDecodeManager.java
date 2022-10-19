package com.mgdsstudio.texturepacker;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;

public class TextureDecodeManager extends TexturePacker{
    public static boolean useMgdsTextures = true;
    private final static String PERSON_SPRITES_EXTENSION = ".gif";
    private final static String GRAPHIC_EXTENSION = ".png";

    public TextureDecodeManager(String path, PApplet engine) {
        super(path, engine);
    }

    public PImage getDecodedImage(){
        PImage image;
        String [] data = engine.loadStrings(path);
        int w = getWidth(data);
        int h = getHeight(data);
        image = engine.createImage(w,h, PConstants.ARGB);
        image.loadPixels();
        int [] pixels = image.pixels;
        for (int i = 0; i < data.length-1;i++){
            ArrayList <CoordinateWithColor> coordinateWithColors = getCoordinatesWithColors(data[i]);
            for (CoordinateWithColor coordinateWithColor : coordinateWithColors){
                pixels[getAbsCoordinate(w, coordinateWithColor)] = coordinateWithColor.color;
            }
        }
        image.updatePixels();
        return image;
    }

    private int getAbsCoordinate(int w, CoordinateWithColor coordinateWithColor){
        int fullNumber = coordinateWithColor.y*w+coordinateWithColor.x;
        return fullNumber;
    }

    private int getAbsCoordinate(int w, int x, int y){
        int fullNumber = y*w+x;
        return fullNumber;
    }

    private ArrayList<Pixel> getDataPixels(String[] data) {
        ArrayList<Pixel> dataPixels = new ArrayList<>();
        for (int i = 0; i < data.length-1;i++){
            int color = getColor(data[i]);
            ArrayList <CoordinateWithColor> coordinateWithColors = getCoordinatesWithColors(data[i]);
            for (CoordinateWithColor coordinateWithColor : coordinateWithColors){

                //Pixel pixel = new Pixel(coordinate.x, coordinate.y);
            }
        }
        return dataPixels;
    }

    private ArrayList <CoordinateWithColor> getCoordinatesWithColors(String data) {
        ArrayList <CoordinateWithColor> coordinateWithColors = new ArrayList<>();
        boolean ended = false;
        String [] substrings = data.split(String.valueOf(DEVIDER_Y_VALUE));
        String yString;
        String xString;
        int centerCharPos;
        int colorValue = -1;
        for (int i = 0 ; i < substrings.length; i++){
            centerCharPos = substrings[i].indexOf(WIDTH_HEIGHT_CHAR);
            if (centerCharPos > 0) {
                yString = substrings[i].substring(centerCharPos+1);
                int valueY = Integer.parseInt(yString);
                int valueX;
                if (i != 0) {
                    xString = substrings[i].substring(0, centerCharPos);
                } else {
                    //System.out.println("String " + substrings[i] + " and i : " + i);
                    int startPos = substrings[i].indexOf(START_PIXELS_CHAR);
                    xString = substrings[i].substring(startPos+1, centerCharPos);
                    String colorString = substrings[i].substring(0, startPos);
                    colorValue = Integer.parseInt(colorString);
                }
                valueX = Integer.parseInt(xString);
                coordinateWithColors.add(new CoordinateWithColor(valueX, valueY, colorValue));
            }

        }
        //String [] substrings = data.split();

        return coordinateWithColors;
    }

    private int getColor(String data) {
        int endColorPlace;
            endColorPlace = data.indexOf(START_PIXELS_CHAR);
            if (endColorPlace>0) {
                return Integer.parseInt(data.substring(0, endColorPlace));
            }
        else {
            System.out.println("No data for color in String " + data);
            return -1;
            }
    }

    private int getHeight(String[] data) {
        int deviderPos  = data[data.length-1].indexOf(WIDTH_HEIGHT_CHAR);
        int h = Integer.parseInt(data[data.length-1].substring(deviderPos+1));
        if (debug) System.out.println("Height from the texture data: " + h);
        return h;
    }

    private int getWidth(String[] data) {
        int deviderPos  = data[data.length-1].indexOf(WIDTH_HEIGHT_CHAR);
        int w = Integer.parseInt(data[data.length-1].substring(0,deviderPos));
        if (debug) System.out.println("Width from the texture data file: " + w);
        return w;
    }

    private int[] getColors(String[] data) {
        int [] colors = new int[data.length-1];
        int endColorPlace;
        for (int i = 0; i < colors.length;i++){
            endColorPlace = data[i].indexOf(START_PIXELS_CHAR);
            if (endColorPlace>0) {
                colors[i] = Integer.parseInt(data[i].substring(0, endColorPlace));
                //if (debug) System.out.println("color: " + i + " " + colors[i]);
            }
        }
        return colors;
    }

    public static String getExtensionForSpriteGraphicFile(){
        if (useMgdsTextures) return MGDS_FILE_EXTENSION;
        else return PERSON_SPRITES_EXTENSION;
    }

    public static String getExtensionForGraphicSources(){
        if (useMgdsTextures) return MGDS_FILE_EXTENSION;
        else return GRAPHIC_EXTENSION;
    }

    public void saveAsImage(String extension){
        PImage image = getDecodedImage();
        PGraphics graphics = engine.createGraphics(image.width, image.height);
        graphics.beginDraw();
        graphics.imageMode(PConstants.CORNER);
        graphics.image(image,0,0);
        graphics.endDraw();
        String fileNameWithoutExtension = path.substring(0,path.indexOf('.'));
        String pathToSave = Program.getAbsolutePathToAssetsFolder(fileNameWithoutExtension+"."+extension);
        graphics.save(pathToSave);
        System.out.println(pathToSave);
    }
}
