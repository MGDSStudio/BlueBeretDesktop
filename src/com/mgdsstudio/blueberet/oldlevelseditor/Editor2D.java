package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.androidspecific.AndroidLauncher;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import select.files.SelectLibrary;

import java.io.File;
import java.util.ArrayList;

public abstract class Editor2D {

    private static Timer nextObjectAddingTimer;
    final static private int TIME_TO_NEXT_MAP_ZONE_OPERATION = 400;
    public static int gridSpacing = (int)(80);

    public static boolean showGrid = true;
    public static int distanceToMapZoneBoard = (int)(Program.engine.width/18.33f);
    public static PVector leftUpperCorner = new PVector(distanceToMapZoneBoard, distanceToMapZoneBoard);
    public static PVector rightLowerCorner = new PVector(Program.engine.width-distanceToMapZoneBoard/0.5f, Program.engine.width-distanceToMapZoneBoard);;

    public static int zoneWidth = (int)PApplet.abs(rightLowerCorner.x-leftUpperCorner.x);
    public static int zoneHeight = (int)(zoneWidth-(((rightLowerCorner.y-leftUpperCorner.y)-(3*(9-1)))/9));
    public static boolean prevMousePressedStatement = false;  // false - wasn't pressed, true - was pressed


    public static int backgroundColor = 0xff3A76A5;
    public static int mapZoneColor = 0xffA2D3C3;
    public static int tabColor = 0xffE5E8BE;

    // control constants
    public static int TIME_TO_ADD_NEW_POINT = 500;
    public static final float maxMovementProOneFrameForStaticMouse = 6f;

    // Global Statements


    public final static byte ADDING_NEW_POINT = 20;
    //public static byte statement = NOTHING;

    //list of all global statements
    public final static byte ADDING_NEW_ROUND_BOX = 1;
    public final static byte ADDING_NEW_ROUND_CIRCLE = 2;
    public final static byte ADDING_NEW_ROUND_POLYGON = 3;
    public final static byte ADDING_COLLECTABLE_OBJECT = 4;

    public final static byte MAP_CLEARING = 10;
    public final static byte OPEN_MAP = 11;
    public final static byte NEW_MAP = 12;

    public final static byte OBJECT_SELECTING = 20;
    public final static byte OBJECT_DELETING = 21;
    public final static byte SELECTION_CANCEL = 22;
    public final static byte COPY_ELEMENT = 23;
    public final static byte MOVE_ELEMENT = 24;
    public final static byte OBJECT_EDITING = 25;

    public final static byte ADDING_OBJECT_CLEARING_ZONE = 30;
    public final static byte ADDING_END_LEVEL_ZONE = 31;
    public final static byte ADDING_PORTAL_SYSTEM = 32;
    public final static byte ADDING_PLATFORM_SYSTEM = 33;
    public final static byte ADDING_CAMERA_FIXATION_ZONE = 34;

    public final static byte ADDING_NEW_ROUND_PIPE = 40;
    public final static byte ADDING_NEW_BRIDGE = 41;

    public final static byte ADDING_NEW_INDEPENDENT_STATIC_SPRITE = 50;
    public final static byte ADDING_NEW_INDEPENDENT_SPRITE_ANIMATION = 51;

    public final static byte PLACE_PLAYER = 60;
    public final static byte PLACE_GUMBA = 61;
    public final static byte PLACE_KOOPA = 62;
    public final static byte PLACE_BOWSER = 63;
    public final static byte PLACE_SPIDER = 64;
    public final static byte PLACE_SNAKE = 65;

    public final static byte BACKGROUND = 77;

    public final static byte GRID_PREFERENCES = 90;


    private static byte globalStatement = ADDING_NEW_INDEPENDENT_STATIC_SPRITE;
    public static boolean globalStatementWasChanged = true;

    private static ArrayList<GameObjectDataForStoreInEditor> newObjectsData;
    private static File pathToOpenedFileFromUser;
    public static boolean fileWasChoosen = false;
    // Local statements

    //public final static byte NEW_STATIC_RECT = -100;

    public static byte localStatement = 1;
    public static boolean localStatementChanged = true;
    public static boolean GAME_ZONE_CAN_BE_MOVED = true;

    //Scaling
    public static final byte SCALLING_DOWN = -1;
    public static final byte SCALLING_UP = 1;
    public static final float SCALING_SPEED = 0.05f;
    public static byte scalling = 0;
    private static boolean levelWasChangedFromRedactor;
    // +1 to, -1 away

    public static void create(){
        //System.out.println("L:" + leftUpperCorner + "; R" + rightLowerCorner);
        nextObjectAddingTimer = new Timer();
        Program.engine.imageMode(PConstants.CENTER);
        nextObjectAddingTimer = new Timer(TIME_TO_NEXT_MAP_ZONE_OPERATION);
        newObjectsData = new ArrayList<>();
    }

    public static boolean wasMouseMoved(){
        if ((Program.engine.abs(Program.engine.mouseX- Program.engine.pmouseX) < maxMovementProOneFrameForStaticMouse) &&
                (Program.engine.abs(Program.engine.mouseY- Program.engine.pmouseY) < maxMovementProOneFrameForStaticMouse)){
            return false;
        }
        else return true;
    }

    public static boolean wasMouseMoved(float scale){
        //System.out.println("Actual movement = " + (Program.engine.mouseX- Program.engine.pmouseX) + " but max: " + maxMovementProOneFrameForStaticMouse*scale);
        if ((Program.engine.abs(Program.engine.mouseX- Program.engine.pmouseX) < (maxMovementProOneFrameForStaticMouse*scale)) &&
                (Program.engine.abs(Program.engine.mouseY- Program.engine.pmouseY) < (maxMovementProOneFrameForStaticMouse*scale))){
            return false;
        }
        else return true;
    }

    public static void resetTimer(){
        if (nextObjectAddingTimer != null) {
            nextObjectAddingTimer.setNewTimer(TIME_TO_NEXT_MAP_ZONE_OPERATION);
        }
        else{
            System.out.println("Next operation timer is null and can not be reset");
        }
    }

    public static boolean canBeNextOperationMade(){
        if (nextObjectAddingTimer != null) {
            if (nextObjectAddingTimer.isTime()) return true;
            else return false;
        }
        else {
            nextObjectAddingTimer = new Timer();
            return false;
        }
    }

    public static void setNewGlobalStatement(byte statement){
        globalStatement = statement;
        globalStatementWasChanged = true;
    }

    public static void setNewLocalStatement(byte statement){
        localStatement = statement;
        localStatementChanged = true;
        resetTimer();
    }

    public static void setNextLocalStatement(){
        localStatement++;
        localStatementChanged = true;
        resetTimer();
    }

    public static void setPrevLocalStatement(){
        localStatement--;
        localStatementChanged = true;
        resetTimer();
    }

    public static byte getGlobalStatement(){
        return globalStatement;
    }

    public static boolean isGlobalStatementChanged(){
        if (globalStatementWasChanged == true) {
            globalStatementWasChanged = false;
            return true;
        }
        else return false;
    }

    public static boolean isLocalStatementChanged() {
        if (localStatementChanged == true) {
            localStatementChanged = false;
            return true;
        }
        else return false;
    }

    public static float getMouseWayLengthProLastFrame(){
        float movement = 0.0f;
        float xMovement = (Program.engine.pmouseX- Program.engine.mouseX);
        float yMovement = (Program.engine.pmouseY- Program.engine.mouseY);
        movement = PApplet.sqrt(xMovement*xMovement+yMovement*yMovement);
        return movement;
    }

    public static ArrayList<GameObjectDataForStoreInEditor> getNewObjectsData() {
        return newObjectsData;
    }

    public static void addDataForNewObject(GameObjectDataForStoreInEditor data){
        newObjectsData.add(data);
    }

    public static void setExternalFilePath(File selection) {
        pathToOpenedFileFromUser = selection;
    }

    public static File getPathToOpenedFileFromUser(){
        return pathToOpenedFileFromUser;
    }

    public static boolean isChosenFilePicture(){
        if (pathToOpenedFileFromUser != null){
            if (pathToOpenedFileFromUser.canRead()){
                String name = pathToOpenedFileFromUser.getPath();
                String extension = name.substring(name.length()-3);
                System.out.println("Extension: " + extension);
                if (extension == "jpg" || extension == "png" || extension == "PEG"){
                    return true;
                }
            }
        }
        System.out.println("Path was: " + pathToOpenedFileFromUser);
        return false;
    }

    /*
    public static void copyFile(File source, File dest){
        try{
            if (Program.iEngine.isSdkMoreOrSameAsNeeded()) {
                Files.copy(source.toPath(), dest.toPath());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void leavingEditor(){

    }*/

    public static void changeLevelNumber(int newLevelNumber) {
        Program.actualRoundNumber = newLevelNumber;
        levelWasChangedFromRedactor = true;
    }

    public static void initFileLoader(){
        if (Program.OS == Program.ANDROID) {
            AndroidLauncher.selectLibrary = new SelectLibrary(Program.engine);
            if (!Program.engine.hasPermission("android.permission.READ_EXTERNAL_STORAGE")) {
                Program.engine.requestPermission("android.permission.READ_EXTERNAL_STORAGE", "handleRequest");
            }
            else{

                AndroidLauncher.selectLibrary.selectInput("Select a graphic file:", "fileSelected");
            }
        }
    }

    public static boolean isLevelChanged(){
        return levelWasChangedFromRedactor;
    }

    public static void levelChanging() {
        levelWasChangedFromRedactor = false;
    }
}
