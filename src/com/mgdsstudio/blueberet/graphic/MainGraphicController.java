package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import processing.core.PImage;
import processing.opengl.Texture;

import java.util.ArrayList;

public class MainGraphicController {
    final static private char START_SYMBOL_FOR_IMAGE_PATH = '#';
    final static private char START_SYMBOL_FOR_ELEMENTS_NUMBER = ':';
    final static private char DIVIDER_BETWEEN_ELEMENTS_NUMBER = 'x';
    public final static String WITHOUT_GRAPHIC_STRING = "No_data";

    public ArrayList<Tileset> getTilesets() {
        return tilesets;
    }

    private ArrayList<Tileset> tilesets = new ArrayList<Tileset>();

    public MainGraphicController(){}

    public void addRoundsGraphicFromFile(String path) {  // load all graphic from a round data file
        //boolean hasData = false;
        System.out.println("Try to load level data from path: " + path);
        String[] pathData = Program.engine.loadStrings(path);
        //hasData = true;
        if (pathData!= null){
            ArrayList<String> pathesToImages = new ArrayList<String>();
            System.out.println("*** Data was loaded");
            try {
                for (int i = 0; i < pathData.length; i++) {
                    final int index = pathData[i].indexOf(LoadingMaster.GRAPHIC_NAME_START_CHAR);
                    if (index != -1) {
                        pathesToImages.add(pathData[i].substring(index + 1, pathData[i].indexOf(LoadingMaster.GRAPHIC_NAME_END_CHAR)));
                    }
                }
            }
            catch (Exception e) {
                System.out.println("This data string has nothing after # symbol");
            }
            System.out.println("It was: " + pathesToImages.size());
            clearRepeatingPathes(pathesToImages);
            printPathesToConsole(pathesToImages);
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println("It is: " + pathesToImages.size());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            fillTilesets(pathesToImages);
        }
        else System.out.println("Can not load round data for this round");
    }

    private void fillTilesets(ArrayList<String> pathesToImages) {
        for (int i = 0 ; i < pathesToImages.size(); i++){
            System.out.println("Try to load tileset from path: " + pathesToImages.get(i));
            if (pathesToImages.get(i) == WITHOUT_GRAPHIC_STRING){
                System.out.println("Tileset for : " + pathesToImages.get(i) + " was not loaded");
            }
            else {
                tilesets.add(new Tileset(i, pathesToImages.get(i)));
                System.out.println("Successfully loaded tileset: " + pathesToImages.get(i));
            }
        }
        if (HeadsUpDisplay.mainGraphicTileset == null) {
            System.out.println("Main graphic tileset is null");
            HeadsUpDisplay.mainGraphicTileset = createNewTileset("HUD"+ TextureDecodeManager.getExtensionForGraphicSources());
            if (HeadsUpDisplay.mainGraphicTileset != null) System.out.println(" and not more null");
        }
        tilesets.add(HeadsUpDisplay.mainGraphicTileset);
    }

    public Tileset getTilesetUnderPath(String path){
        if (path == WITHOUT_GRAPHIC_STRING) return null;
        else {
            for (Tileset tileset : tilesets) {
                if (tileset != null && tileset.getPath() != null) {
                    if (tileset.getPath().equals(path) || (tileset.getPath() == path)) {
                        return tileset;
                    }
                }
            }
            try {
                Tileset tileset = createNewTileset(path);
                System.out.println("New tileset was created for path : " + path);
                return tileset;
            } catch (Exception e) {
                System.out.println(" It is impossible to create new tileset");
                System.out.println("Can not get tileset for " + path);
                return null;
            }
            //return null;
            //return null;
        }
    }

    private Tileset createNewTileset(String path){
        if (path!= null){
            Tileset tileset = new Tileset(getTilesetNumber(path), path);
            tilesets.add(tileset);
            return tileset;
        }
        else {
            System.out.println("This object has no graphic");
            return null;
        }

    }

    private int getTilesetNumber(String path){
        int number = 0;
        final String name = "Tileset";
        if (path.contains(name)){
            try {
                int startNumber = name.length();
                int startIndex = path.indexOf(name)+name.length();
                String numberInString = path.substring(startIndex);
                String extension = ".png";
                if (path.contains(extension)) {
                    int extensionNumber = numberInString.indexOf(extension);
                    numberInString = numberInString.substring(0, extensionNumber);
                } else {
                    extension = ".jpg";
                    if (path.contains(extension)) {
                        int extensionNumber = numberInString.indexOf(extension);
                        numberInString = numberInString.substring(0, extensionNumber);
                    } else {
                        extension = ".jpeg";
                        if (path.contains(extension)) {
                            int extensionNumber = numberInString.indexOf(extension);
                            numberInString = numberInString.substring(0, extensionNumber);
                        }
                    }
                }
                System.out.println("Number of tileset: " + numberInString) ;
                return (int)Integer.parseInt(numberInString);
            }
            catch (Exception e){
                System.out.println("This tileset has a complex name; Path was: " + path + " Name is: ");
            }
        }
        return 0;
    }

    private byte getPicturesAlongX(String textData){
        int startPos = textData.indexOf(START_SYMBOL_FOR_ELEMENTS_NUMBER)+1;
        String elementsAlongX = textData.substring(startPos, DIVIDER_BETWEEN_ELEMENTS_NUMBER-1);
        return (byte) (Integer.parseInt(elementsAlongX));
    }

    private byte getPicturesAlongY(String textData){
        int startPos = textData.indexOf(DIVIDER_BETWEEN_ELEMENTS_NUMBER)+1;
        String elementsAlongX = textData.substring(startPos, DIVIDER_BETWEEN_ELEMENTS_NUMBER-1);
        return (byte) (Integer.parseInt(elementsAlongX));
    }

    private void printPathesToConsole(ArrayList<String> pathesToImages){
        for (String path : pathesToImages){
            System.out.println(path);
        }
    }

    /*
    private void clearRepeatingPathes(ArrayList<String> pathesToImages) {
        //this function must be researched
        ArrayList<String> pathesToBeDeleted = new ArrayList<String>();
        String [] pathes = new String[pathesToBeDeleted.size()];
        for (int i = 0 ; i < pathesToBeDeleted.size(); i++){
            pathes[i] = pathesToBeDeleted.get(i);
        }
        for (int i = 0; i < pathes.length; i++){
            String actualString = pathes[i];
            for (int j = (j+1); j < pathes.length; j++) {
                if (pathes.[j] != null)

            }
        }
    }
    */

    private void clearRepeatingPathes(ArrayList<String> pathesToImages) {
        String [] pathes = new String[pathesToImages.size()];
        for (int i = 0 ; i < pathesToImages.size(); i++){
            pathes[i] = pathesToImages.get(i);
        }
        for (int i = 0; i < pathes.length; i++){
            String actualString = pathes[i];
            for (int j = (i+1); j < pathesToImages.size() ; j++){
                if (pathesToImages.get(j).equals(actualString)){
                    pathes[j] = null;
                }
            }
        }
        pathesToImages.clear();
        for (int i = 0; i < pathes.length; i++){
            if (pathes[i]!=null){
                pathesToImages.add(pathes[i]);
            }
        }
    }
    /*
    private void clearRepeatingPathes(ArrayList<String> pathesToImages) {
        //this function must be researched
        ArrayList<String> pathesToBeDeleted = new ArrayList<String>();
        String [] pathes = new String[pathesToBeDeleted.size()];
        for (int i = 0 ; i < pathesToBeDeleted.size(); i++){
            pathes[i] = pathesToBeDeleted.get(i);
        }
        for (int i = 0; i < pathesToImages.size(); i++){
            String actualString = pathesToImages.get(i);
            for (int j = (i+1); j < pathesToImages.size() ; j++){
                if (pathesToImages.get(j).equals(actualString)){
                    pathesToImages.remove(pathesToImages.get(i));
                }
            }
        }
    }
    */

    public void clearGraphic() {
        for (Tileset tileset : tilesets){
            try {
                PImage image = tileset.picture.image;
                Object cacheInObjects = Program.objectsFrame.getCache(image);
                if (cacheInObjects instanceof Texture) ((Texture) cacheInObjects).disposeSourceBuffer();
                Object cacheInBackgrounds = Program.backgroundFrame.getCache(image);
                if (cacheInBackgrounds instanceof Texture) ((Texture) cacheInBackgrounds).disposeSourceBuffer();
                Program.objectsFrame.removeCache(image);
                Program.backgroundFrame.removeCache(image);
                Program.engine.g.removeCache(image);
                if (Program.debug){
                    System.out.println("Object " + " was deleted from all three graphic objects: " + (Program.objectsFrame.getCache(image)!= null) + ", " + (Program.backgroundFrame.getCache(image)!= null) + ", " + (Program.engine.g.getCache(image)!= null));
                }
            }
            catch (Exception e){
                System.out.println("PImage is not in memory");
                e.printStackTrace();
            }
        }
        System.gc();
        System.out.println("Graphic was cleared from the memory");
    }
/*
    void forceCacheRemoval(PGraphics pg) {
        for (PImage img: images) {
            Object cache = pg.getCache(img);

            if (cache instanceof Texture)
                ((Texture) cache).disposeSourceBuffer();

            pg.removeCache(img);
        }
    }*/

}
