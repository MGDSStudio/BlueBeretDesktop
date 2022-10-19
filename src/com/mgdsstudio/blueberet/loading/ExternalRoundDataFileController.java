package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ExternalRoundDataFileController {
    protected String path = "";
    final public static boolean MAIN_LEVELS = true;
    final public static boolean USER_LEVELS = false;
    protected final static String MAIN_LEVELS_BASIC_NAME = Program.USER_LEVELS_PREFIX;
    protected final static String USER_LEVELS_BASIC_NAME = Program.USER_LEVELS_PREFIX;
    protected final static String FILE_EXTENSION = Program.USER_LEVELS_EXTENSION;

    final public static String soldierType = "Soldier";
    final public static String gumbaType = "Gumba";
    final public static String koopaType = "Koopa";
    final public static String bowserType = "Bowser";


    final public static String roundBoxType = "RoundBox";
    final public static String objectClearingZoneType = ObjectsClearingZone.CLASS_NAME;

    final public static String staticSpriteType = IndependentOnScreenStaticSprite.CLASS_NAME;

    static final public char DIVIDER_BETWEEN_VALUES = ',';
    static final public char DIVIDER_BETWEEN_GRAPHIC_DATA = 'x';
    static final protected char VERTICES_START_CHAR = '%';
    static final protected char DIVIDER_BETWEEN_VERTICES = 'v';

    static final public char MAIN_DATA_START_CHAR = ':';
    static final public char GRAPHIC_NAME_START_CHAR = '#';
    static final public char GRAPHIC_NAME_END_CHAR = ';';

    // Loading options
    protected final static boolean LOADING_WITH_PROCESSING_STRING_DECODER = false;
    protected final static boolean LOADING_WITH_JAVA_STRING_DECODER = true;

    public  static char STRING_DEVIDER_FOR_WINDOWS = 92;    //  \
    public  static char STRING_DEVIDER_FOR_ANDROID = 47;   //  /
    protected String[] fileData;

    public static boolean FIND_FILE_IN_CACHE = false;
    public static boolean FIND_FILE_IN_ASSETS = true;
    private boolean whereFindFile;

    protected final String LANGUAGE_SPECIFIC_CHARS_ENTER = "%_";

    public ExternalRoundDataFileController(int fileNumber, boolean levelType){
        path = getPathToFileOnCache(fileNumber, levelType);
    }

    public ExternalRoundDataFileController(int fileNumber, boolean levelType, boolean whereFindFile){
        //String prefix;
        if (levelType == USER_LEVELS){
            System.out.println("This level is user level");
            if (Program.OS == Program.ANDROID){
                path = getPathToFileOnCache(fileNumber, USER_LEVELS);
            }
            else path = getPathToFileOnAssets(fileNumber, USER_LEVELS);
            System.out.println("This level is user level from path: " + path);
        }
        else {
            System.out.println("This level is main level");
            path = getPathToFileOnAssets(fileNumber, levelType);
            System.out.println("This level is main level from path: " + path);
        }/*
        if (whereFindFile == FIND_FILE_IN_CACHE && Program.OS == Program.ANDROID) path = getPathToFileOnCache(fileNumber, levelType);
        else path = getPathToFileOnAssets(fileNumber, levelType);*/
    }

    ExternalRoundDataFileController(){
    }

    private static String getPathForAndroid(String fileNameForWindowsMode){
        String path = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+STRING_DEVIDER_FOR_ANDROID+fileNameForWindowsMode;
        System.out.println("For android full path to round file : " + path);
        return path;
    }

    public static String getPathToFileOnAssets(int fileNumber, boolean levelType){
        String path = new String();
        path+= Program.getRelativePathToAssetsFolder();
        if (levelType == MAIN_LEVELS) path+= MAIN_LEVELS_BASIC_NAME;
        else path+= USER_LEVELS_BASIC_NAME;
        String number = "";
        number+=fileNumber;
        path = path.concat(number);
        path = path.concat(FILE_EXTENSION);

        System.out.println("Full path to round file on assets: " + path);
        return path;
    }

    public static String getPathToFileOnCache(int fileNumber, boolean levelType){
        String path = new String();
        if (levelType == MAIN_LEVELS) path+= MAIN_LEVELS_BASIC_NAME;
        else path+= USER_LEVELS_BASIC_NAME;
        String number = "";
        number+=fileNumber;
        path = path.concat(number);
        path = path.concat(FILE_EXTENSION);
        if (Program.OS == Program.DESKTOP) {
            System.out.println("For windows full path to round file : " + Program.getAbsolutePathToAssetsFolder(path));
            return path;
        }
        else return getPathForAndroid(path);
    }

    public static void deleteLastStringDataFromFile(String path, String className){

    }

    protected void loadingFileDataInProcessingMode(){
        try {
            fileData = Program.engine.loadStrings(path);
            System.out.println("Round data from " + path + " was successfully loaded with processing loader");
        } catch (Exception e) {
            System.out.println("Can not load from " + path + " using processing loader");
        }
    }

    protected boolean loadingFileDataInJavaMode(){
        String directory = "Assets";
        try {
            //For desktop ArrayList<String> data = whenReadWithBufferedReader_thenCorrect(Programm.engine.sketchPath() + devider + directory + devider + path);
            char devider = STRING_DEVIDER_FOR_ANDROID;
            if (Program.OS == Program.DESKTOP) devider = STRING_DEVIDER_FOR_WINDOWS;
            String fullPath;
            if (Program.OS == Program.ANDROID) fullPath = Program.engine.sketchPath + devider + directory + devider + path;
            else fullPath = path;
            ArrayList<String> data = whenReadWithBufferedReader_thenCorrect(fullPath);
            if (data == null) {
                //System.out.println("Can not load data using java mode");
                return true;
            }
            //ArrayList<String> data = whenReadWithBufferedReader_thenCorrect(path);
            System.out.println("Data is null: "  + (data == null) + "; Sketch full path: " + fullPath);
            fileData = new String[data.size()];
            for (int i = 0; i < fileData.length; i++) {
                fileData[i] = data.get(i);
            }
        }
        catch (Exception e) {
            //System.out.println("Can not load with this loader; " + e);
            return true;
        }
        return false;
    }

    protected ArrayList<String> whenReadWithBufferedReader_thenCorrect(String fileName) {

        try {
            System.out.println("Sketch path: " + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            ArrayList<String> data = new ArrayList<>();
            String currentLine = new String();
            while (currentLine != null) {
                if (currentLine.length() > 1) data.add(currentLine);
                currentLine = reader.readLine();
            }
            reader.close();
            return data;
        } catch (IOException e) {
            System.out.println("Can not load data from file: " + e);
            return null;
        }

        /*
        try {
            System.out.println("Sketch path: " + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            ArrayList<String> data = new ArrayList<>();
            String currentLine = new String();
            while (currentLine != null) {
                if (currentLine.length() > 1) data.add(currentLine);
                currentLine = reader.readLine();
            }
            reader.close();
            return data;
        } catch (IOException e) {
            System.out.println("Can not load data from file: " + e);
        }

        return null;

        */
    }

}
