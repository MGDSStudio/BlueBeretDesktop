package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableObjectInNesStyle;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.HUD.WeaponChangingController;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;

import com.mgdsstudio.blueberet.onscreenactions.OnFlickAction;
import com.mgdsstudio.blueberet.onscreenactions.OnPinchAction;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.io.File;


public abstract class Program implements IGameConstants {

    public static final int LAST_LEVEL_OF_THE_GAME = 4;
    //Colors
    public static boolean withBeretColorChanging = false;
    public static boolean withAdds = true;
    public static boolean USE_3D = true;
    //File management

    public final static String mainFont = "Main font.vlw";
    public final static String secondaryFont = "Secondary font.vlw";
    public final static float FONTS_DIMENSION_RELATIONSHIP = 23f/36f;
    //public static String LANGUAGE;

    public static IEngine iEngine;

    public static boolean USE_MAIN_FONT_IN_GAME = false;
    public static boolean USE_MAIN_FONT_IN_MENU = false;
    public static String PATH_TO_ASSETS_FOR_WINDOWS = "App//src//main//Assets//";
    public final static String mainFontNameForWindows = "Serif";
    public final static String mainFontNameForAndroid = "Serif";

    // with this display dimentions has the game camera normal scale = 1;
    public final static int XIAOMI_REDMI_WIDTH = 720;  // For xiaomi redmi x4
    public final static int XIAOMI_REDMI_HEIGHT = 1280;  // For xiaomi redmi x4
    public final static float SIDES_RELATIONSHIP_FOR_XIAOMI = (float)XIAOMI_REDMI_HEIGHT/(float)XIAOMI_REDMI_WIDTH;
    public final static int PIXEL_4A_WIDTH = 1080;  // For xiaomi redmi x4
    public final static int PIXEL_4A_HEIGHT = 2340;  // For xiaomi redmi x4
    public final static float SIDES_RELATIONSHIP_FOR_PIXEL_4A = (float)PIXEL_4A_HEIGHT/(float)PIXEL_4A_WIDTH;

    public static float SIDES_RELATIONSHIP;

    /*public final static int POCO_WIDTH = 1080;  // For xiaomi redmi x4
    public final static int POCO_HEIGHT = 2400;  // For xiaomi redmi x4
    public static float WIDTH_TO_XIAOMI_COEF = 0.75f;*/

    //public static float DISPLAY_PROPORTION;


    public final static int DEBUG_DISPLAY_HEIGHT = (int)  (889/ 1f);
    public final static int DEBUG_DISPLAY_WIDTH = (int) (DEBUG_DISPLAY_HEIGHT/ SIDES_RELATIONSHIP_FOR_XIAOMI);
    //public final static int DEBUG_DISPLAY_WIDTH = (int) (DEBUG_DISPLAY_HEIGHT/ 2.222f);


    /*
    public final static int DEBUG_DISPLAY_WIDTH = (int) (500/ 1f);
    public final static int DEBUG_DISPLAY_HEIGHT = (int)  (889/ 1f);
     */
    public final static int DEBUG_DISPLAY_WIDTH_FOR_XIAOMI_PROPORTION = (int) (500/ 1f);
    public final static int DEBUG_DISPLAY_HEIGHT_FOR_XIAOMI_PROPORTION = (int)  (889/ 1f);
    //public static final float DISPLAY_PROPORTION_FOR_XIAOMI = DISPLAY_PROPORTION;
//public static final float DISPLAY_PROPORTION_FOR_XIAOMI = (float)DEBUG_DISPLAY_HEIGHT_FOR_XIAOMI_PROPORTION/(float)DEBUG_DISPLAY_HEIGHT_FOR_XIAOMI_PROPORTION;
    public static int ANTI_ALIASING = 0;

    public final static int DISPLAY_WIDTH = (int) (DEBUG_DISPLAY_WIDTH);
    public final static int DISPLAY_HEIGHT = (int) (DEBUG_DISPLAY_HEIGHT);

    public static int NORMAL_FPS = 40;
    //public static final int NORMAL_CPU_UPDATING_RATE = NORMAL_FPS/2;
    public static final int NORMAL_CPU_UPDATING_RATE = NORMAL_FPS;
    public final static String mainFontName = "Roboto";

    //Directions
    public static final byte TO_LEFT = 1;
    public static final byte TO_RIGHT = 2;
    public static final byte TO_UP = 3;
    public static final byte TO_DOWN = 4;

    //Control features
    public static boolean canNotLoadLevelOrEditor = false;

    // Levels
    public final static byte MAIN_TEST_LEVEL = -1;
    public final static byte SECONDARY_TEST_LEVEL = 7;
    public final static byte TEST_LEVEL = 2;

    public final static boolean USER_LEVEL = ExternalRoundDataFileController.USER_LEVELS;
    public final static boolean MAIN_LEVEL = ExternalRoundDataFileController.MAIN_LEVELS;
    public final static boolean actualLevelType = USER_LEVEL;
    public static int actualRoundNumber = SECONDARY_TEST_LEVEL;

   //Game statements
    public final static byte GAME_PROCESS = -1;
    public final static byte LEVELS_EDITOR = -2;
    public final static byte SOME_MENU = 3;
    public final static byte USER_LEVELS_MENU = 4;
    public static byte gameStatement = SOME_MENU;  // Editor is on
    //public static byte previousGameStatement = LEVELS_EDITOR;
    //public static byte gameStatement = MAIN_MENU;  // Editor is on

    public static boolean levelLaunchedFromRedactor = false;
    public static boolean jumpFromRedactorToGame = true;
    public static boolean jumpFromGameToRedactor = false;
    public static boolean jumpFromGameToMenu = false;

    public static boolean roundIsLoaded;
    public static boolean cameraIsMoving;
    //Secondary game variables


    /* Main game objects */
    public static PGraphics objectsFrame;
    public static PGraphics backgroundFrame;
    public static PApplet engine;
    public static boolean troubleByLoading;

    //public static int width, height;

    //public static final boolean OS = ANDROID;
    public static boolean OS;
    // Main game constants
    public static final boolean WITH_GRAPHIC = true;
    public static final boolean WITH_BACKGROUND = true;
    public final static boolean JAVA_RENDERER = false;
    public final static boolean OPENGL_RENDERER = true;
    public static boolean graphicRenderer = OPENGL_RENDERER;
    //public static final boolean graphicRenderer = OPENGL_RENDERER;

    public static final boolean TIME_STEP_STATIC = false;
    public static boolean debug;
    public static int deltaTime = 0;
    public static int lastFrameTime = 0;

    //Control interrupt
    public final static byte NO_ROTATION = 0;
    public final static byte FORWARD_ROTATION = 1;
    public final static byte BACKWARD_ROTATION = 2;
    private static byte mouseWheelRotation = NO_ROTATION;
    //public static boolean prevMousePressedStatement;  // false - wasn't pressed, true - was pressed
    private static boolean longPressed;
    public final static int LONG_PRESSING_TIME = 650;
    private static int longPressingTime;
    private static Timer longPressedTimer ;
    private static int pressingStartFrame = -1;
    private static int releasingStartFrame = -1;

    //DataFormats
    public static final String USER_LEVELS_EXTENSION = ".txt";
    public static final String USER_LEVELS_PREFIX = "UserLevel";
    public static final String MAIN_LEVELS_PREFIX = "Zone";
    //keyword
    public static byte actualPressedKeyDigitValue = -1;
    public static char actualPressedValue = '@';
    public static int actualPressedKeyCode = -9999;

    //Android specific and data transfer
    private static String sketchPath = "";
    public static File fileSelectedByUser;
    public static boolean virtualKeyboardOpened = false;

    //Language
    public final static byte RUSSIAN = 0;
    public final static byte ENGLISH = 1;
    public final static byte CHINESE = 3;
    public final static boolean USE_ALWAYS_ENGLISH_IN_MENU = false;
    public static byte LANGUAGE = RUSSIAN;
    //public static byte LANGUAGE = ENGLISH;

    //hidden elements
    public static final boolean WITH_LIFE_LINES = false;
    public final static float OBJECT_FRAME_SCALE = 0.5f;

    private final static boolean RECREATE_GRAPHICS = true;

    public final static boolean DELETE_IMAGES_FROM_CACHE = false;
    public static boolean appWasHidden = false;

    public final static boolean OLD_EDITOR = false;
    public final static boolean NEW_EDITOR = true;
    public final static boolean levelsEditorType = NEW_EDITOR;
    //public final static float OBJECT_FRAME_SCALE = 0.5f;

    private static void clearMemoryFromGraphic(){
        Program.objectsFrame.dispose();
        Program.backgroundFrame.dispose();
        Program.engine.g.removeCache(objectsFrame);
        Program.engine.g.removeCache(backgroundFrame);
        Program.engine.g.clear();
        System.out.println("Frames were deleted from the main graphic file");
    }

    private static void createGraphics(boolean makeNew){
        boolean inAccordingToScreenDims = true;
        boolean DEBUG_FOR_CAMERA_IN_EDITOR = true;
        final float EDITOR_GRAPHIC_SCALE = GameCamera.minScale/GameCamera.minScaleInEditorMode;
        float coef = EDITOR_GRAPHIC_SCALE;

        if (gameStatement == GAME_PROCESS) {
            //OBJECT_FRAME_SCALE = 0.5f;
            coef = 1f;
        }

        if (DEBUG_FOR_CAMERA_IN_EDITOR) coef = 1f;
        float cameraMinScale = GameCamera.minScale; // was so
        int basicWidth;
        int basicHeight;
        if (inAccordingToScreenDims){
            basicWidth = engine.width;
            basicHeight = engine.height;
        }
        else {

            float sourceWidth = (XIAOMI_REDMI_WIDTH * SIDES_RELATIONSHIP_FOR_XIAOMI);
            float sourceHeight = (sourceWidth*((float)engine.height/(float)engine.width));
            System.out.println("Graphic dims: " + sourceWidth + "x" + sourceHeight);
            basicWidth = (int)sourceWidth;
            basicHeight = (int)sourceHeight;
        }
        int newWidthForObjectsFrame = (int) ((OBJECT_FRAME_SCALE/cameraMinScale)*coef*basicWidth);
        int newHeightForObjectsFrame = (int) ((OBJECT_FRAME_SCALE/ cameraMinScale)*coef*basicHeight );
        int newWidthForBackgroundFrame = (int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*basicWidth / cameraMinScale);
        int newHeightForBackgroundFrame = (int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*basicHeight / cameraMinScale);
        System.out.println("Frame width " + newWidthForObjectsFrame + "; Camera min scale: " + cameraMinScale);
        boolean dimensionsWasChanged = true;
        if (objectsFrame == null || backgroundFrame == null || appWasHidden) makeNew = true;
        else if (objectsFrame.width == newWidthForObjectsFrame && objectsFrame.height == newHeightForObjectsFrame){
            System.out.println("The PGraphics can be saved and they need not to be recreated");
            dimensionsWasChanged =  false;
        }
        if (makeNew || dimensionsWasChanged) {
            if (objectsFrame != null && backgroundFrame != null) {
                Program.objectsFrame.dispose();
                Program.backgroundFrame.dispose();
                engine.g.removeCache(objectsFrame);
                engine.g.removeCache(backgroundFrame);
            }
            if (graphicRenderer == OPENGL_RENDERER) {
                objectsFrame = engine.createGraphics(newWidthForObjectsFrame, newHeightForObjectsFrame, PConstants.P2D);
                objectsFrame.smooth(ANTI_ALIASING);
                backgroundFrame = engine.createGraphics(newWidthForBackgroundFrame, newHeightForBackgroundFrame, PConstants.P2D);
                backgroundFrame.smooth(ANTI_ALIASING);
                System.out.println("Graphic was created for OPEN GL mode");
            } else {
                if (OS == ANDROID) objectsFrame = engine.createGraphics(newWidthForObjectsFrame, newHeightForObjectsFrame, PConstants.JAVA2D);
                else objectsFrame = engine.createGraphics(newWidthForObjectsFrame, newHeightForObjectsFrame);
                objectsFrame.smooth(ANTI_ALIASING);
                if (OS == ANDROID) backgroundFrame = engine.createGraphics(newWidthForBackgroundFrame, newHeightForBackgroundFrame, PConstants.JAVA2D);
                else backgroundFrame = engine.createGraphics(newWidthForBackgroundFrame, newHeightForBackgroundFrame);
                backgroundFrame.smooth(ANTI_ALIASING);
                System.out.println("Graphic was created for JAVA mode");
            }
            /*
            if ( Program.ANTI_ALIASING == 0) {
                objectsFrame.noSmooth();
                backgroundFrame.noSmooth();
            }
            else {
                objectsFrame.smooth(Program.ANTI_ALIASING);
                backgroundFrame.smooth(Program.ANTI_ALIASING);
            }*/
            System.out.println("PGraphic objects were created new");
        }
        else {
            System.out.println("PGraphic objects are the sames");
            /*
            System.out.println("Frames are the same and were only resized");
            objectsFrame.resize(newWidthForObjectsFrame, newHeightForObjectsFrame);
            backgroundFrame.resize(newWidthForBackgroundFrame, newHeightForBackgroundFrame);
            */
            /*
            objectsFrame.width = newWidthForObjectsFrame;
            objectsFrame.height = newHeightForObjectsFrame;
            backgroundFrame.width = newWidthForBackgroundFrame;
            backgroundFrame.height = newHeightForBackgroundFrame;
            System.out.println("PGraphic objects are the same. They have changed their dimensions");*/
        }
        if (appWasHidden) appWasHidden = false;
    }

    public static void recreateFrames(){
        if (objectsFrame != null && backgroundFrame != null) {
            if (RECREATE_GRAPHICS){
                createGraphics(false);
            }
            else {
                clearMemoryFromGraphic();
                createGraphics(true);
            }
        }
        else createGraphics(true);
        /*


        if (gameStatement == GAME_PROCESS) coef = 1f;
        if (graphicRenderer == OPENGL_RENDERER) {
            objectsFrame = engine.createGraphics((int) (OBJECT_FRAME_SCALE*coef*engine.width / GameCamera.minScale), (int) (OBJECT_FRAME_SCALE*coef*engine.height / GameCamera.minScale), PConstants.P2D);
            final float COEF = 1.00f;
            backgroundFrame = engine.createGraphics((int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*engine.width*COEF / GameCamera.minScale), (int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*engine.height*COEF / GameCamera.minScale), PConstants.P2D);
            if ( Program.ANTI_ALIASING == 0) {
                objectsFrame.noSmooth();
                backgroundFrame.noSmooth();
            }
            else {
                objectsFrame.smooth(Program.ANTI_ALIASING);
                backgroundFrame.smooth(Program.ANTI_ALIASING);
            }
        }
        else {
            objectsFrame = engine.createGraphics((int) (OBJECT_FRAME_SCALE*coef*engine.width / GameCamera.minScale), (int) (OBJECT_FRAME_SCALE*coef*engine.height / GameCamera.minScale), PConstants.JAVA2D);
            backgroundFrame = engine.createGraphics((int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*engine.width / GameCamera.minScale), (int) (Background.BACKGROUND_DIMENSION_COEFFICIENT*coef*engine.height / GameCamera.minScale), PConstants.JAVA2D);
        }
        */
        backgroundFrame.scale(Background.BACKGROUND_DIMENSION_COEFFICIENT);
        objectsFrame.scale(OBJECT_FRAME_SCALE);
        objectsFrame.beginDraw();
        objectsFrame.imageMode(PConstants.CENTER);
        if (graphicRenderer == OPENGL_RENDERER) {
            objectsFrame.strokeWeight(2);
            objectsFrame.stroke(250, 0, 0);
            objectsFrame.noFill();
            objectsFrame.rectMode(PConstants.CENTER);
        }
        objectsFrame.endDraw();
    }

    public static void init(PApplet parent) {
        iEngine = (IEngine) parent;
        engine = parent;
        SIDES_RELATIONSHIP = ((float)parent.height/(float)parent.width);
        if (OS == DESKTOP){
            USE_MAIN_FONT_IN_GAME = true;
            USE_MAIN_FONT_IN_MENU = true;
        }
        //DISPLAY_PROPORTION = (float)engine.height/(float)engine.width;
        System.out.println("Display proportion : " +SIDES_RELATIONSHIP);
        /*
        if (graphicRenderer == OPENGL_RENDERER) {
            objectsFrame = parent.createGraphics((int) (parent.width / GameCamera.minScale), (int) (parent.height / GameCamera.minScale), PConstants.P2D);
            final float COEF = 1.00f;
            backgroundFrame = parent.createGraphics((int) (parent.width*COEF / GameCamera.minScale), (int) (parent.height*COEF / GameCamera.minScale), PConstants.P2D);
            if ( Program.ANTI_ALIASING == 0) {
                objectsFrame.noSmooth();
                backgroundFrame.noSmooth();
            }
            else {
                objectsFrame.smooth(Program.ANTI_ALIASING);
                backgroundFrame.smooth(Program.ANTI_ALIASING);
            }
        }
        else {
            objectsFrame = parent.createGraphics((int) (parent.width / GameCamera.minScale), (int) (parent.height / GameCamera.minScale), PConstants.JAVA2D);
            backgroundFrame = parent.createGraphics((int) (parent.width / GameCamera.minScale), (int) (parent.height / GameCamera.minScale), PConstants.JAVA2D);
        }
        objectsFrame.beginDraw();
        objectsFrame.imageMode(PConstants.CENTER);
        if (graphicRenderer == OPENGL_RENDERER) {
            objectsFrame.strokeWeight(2);
            objectsFrame.stroke(250, 0, 0);
            objectsFrame.noFill();
            objectsFrame.rectMode(PConstants.CENTER);
        }
        objectsFrame.endDraw();
        */
        parent.strokeWeight(2);
        parent.stroke(250, 0, 0);
        parent.noFill();
        parent.rectMode(PConstants.CENTER);
        parent.imageMode(PConstants.CENTER);
        //parent.textMode(PConstants.CENTER, PConstants.CENTER);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        //physicWorldUpdatingThread = new PhysicWorldUpdatingThread();
        //physicWorldUpdatingThread.setDaemon(true);
        //physicWorldUpdatingThread.setPriority(1);
        if (debug) System.out.println("Game2D class was loaded");

    }

    public static void setMouseWheelRotation(byte rotation) {
        mouseWheelRotation = rotation;
    }


    public static byte getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    public static void updateDeltaTime() {
        Program.deltaTime = Program.engine.millis() - Program.lastFrameTime;
        Program.lastFrameTime = Program.engine.millis();
        if (deltaTime > 1500) deltaTime = 1;
    }

    public static boolean isActualStatementMenu(){
        if (gameStatement == SOME_MENU || gameStatement == USER_LEVELS_MENU) return true;
        else return false;
    }


    public static void resetFixedRotationForBodiesWithBullets(Body body, Body body1) {
        if (body.getType() == BodyType.DYNAMIC && body1.getType() == BodyType.DYNAMIC){
            if (body.getUserData() != null && body1.getUserData() != null){
                if (body.getUserData() == Soldier.CLASS_NAME && body1.getUserData() == CollectableObjectInNesStyle.CLASS_NAME){
                    //System.out.println("Soldier and collectable object 1");
                    float mass = body1.getMass();
                    mass/=100f;
                    body1.m_mass = mass;
                    body1.resetMassData();
                }
                else if (body1.getUserData() == Soldier.CLASS_NAME && body.getUserData() == CollectableObjectInNesStyle.CLASS_NAME) {
                    float mass = body.getMass();
                    mass/=100f;
                    body.m_mass = mass;
                    body.resetMassData();
                }
            }
        }
        /*
        if (body.isBullet() || body1.isBullet()) {
            if (body.isFixedRotation()) {
                body.setFixedRotation(false);
            }
            if (body1.isFixedRotation()) {
                body1.setFixedRotation(false);
            }
        }
        */
    }

    public static boolean isBodyBullet(Body body){
        if (body.getUserData()!= null){
            if (body.getUserData() == Bullet.BULLET){
                return true;
            }
            else return false;
        }
        else return false;
    }


    public static String getRelativePathToAssetsFolder(){
        if (OS == DESKTOP) return PATH_TO_ASSETS_FOR_WINDOWS;
        else return "";
    }

    public static String getAbsolutePathToAssetsFolder(String relativePath){
        if (OS == ANDROID){
            if (relativePath.contains(PATH_TO_ASSETS_FOR_WINDOWS)){
                if (debug) System.out.println("New path: " + relativePath.substring(relativePath.indexOf(PATH_TO_ASSETS_FOR_WINDOWS)+PATH_TO_ASSETS_FOR_WINDOWS.length()));
                return relativePath.substring(relativePath.indexOf(PATH_TO_ASSETS_FOR_WINDOWS)+PATH_TO_ASSETS_FOR_WINDOWS.length());
            }
            else return relativePath;
        }
        else {
            if (relativePath.contains(PATH_TO_ASSETS_FOR_WINDOWS)){
                return relativePath;
            }
            else return PATH_TO_ASSETS_FOR_WINDOWS+relativePath;
        }
    }

    public static void setSketchPath(String sketchPathToBeSet) {
        sketchPath = sketchPathToBeSet;
    }

    public static void updateLongPressingStatement(boolean mousePressed, boolean prevMousePressedStatement) {
        if (mousePressed && !prevMousePressedStatement) {
            if (longPressedTimer == null) longPressedTimer = new Timer(LONG_PRESSING_TIME);
            longPressedTimer.setNewTimer(LONG_PRESSING_TIME);
            longPressed = false;
            pressingStartFrame = engine.frameCount;
            //longPressingTime = engine.millis();
        }
        else if (!mousePressed && prevMousePressedStatement){
            releasingStartFrame = engine.frameCount;
            if (longPressedTimer != null && longPressedTimer.isTime()){
                longPressed = true;
                longPressedTimer.stop();

                //longPressingTime = engine.millis();
            }
        }
        else if (!mousePressed){
            if (longPressedTimer != null) {
                longPressedTimer.stop();
                longPressed = false;
            }
        }
    }

    public final static int getFramesBetweenReleaseAndPress(){
        return releasingStartFrame-pressingStartFrame;
    }

    public final static int getFramesBetweenPressAndRelease(){
        return pressingStartFrame-releasingStartFrame;
    }


    public void onDoubleTap(float x, float y)
    {
        //ActionsBuffer.saveAction(ActionsBuffer.doubleTap, new PVector(x,y));
    }

    public static void onFlick(float x, float y, float px, float py, float v)
    {
        if (WeaponChangingController.WITH_FLICK_CHANGING) {
            OnFlickAction action = new OnFlickAction(new PVector(px, py), new PVector(x, y), v);
            //ActionsBuffer.saveAction(ActionsBuffer.flick, new PVector(px,py), new PVector(x,y));
            System.out.println("New Flick is saved");
            TouchScreenActionBuffer.addNewAction(action);
        }
        if (Program.gameStatement == Program.LEVELS_EDITOR){
            OnFlickAction action = new OnFlickAction(new PVector(px, py), new PVector(x, y), v);
            TouchScreenActionBuffer.addNewAction(action);
        }
    }

    public static void onPinch(float x, float y, float value){
        OnPinchAction action = new OnPinchAction(new PVector(x,y),value);
        //ActionsBuffer.saveAction(action);
        System.out.println("New Pinch is saved");
        TouchScreenActionBuffer.addNewAction(action);
    }

    public static void openVirtualKeyboard(boolean flag){
        iEngine.openKeyboard(flag);
        /*
        if (OS == ANDROID) {
            if (flag && !virtualKeyboardOpened) {
                engine.openKeyboard();
                virtualKeyboardOpened = true;
                System.out.println("Keyword opened");
            }
            else if (!flag && virtualKeyboardOpened){
                iEngine.openKeyboard(false);
                //engine.closeKeyboard();
                virtualKeyboardOpened = false;
                System.out.println("Keyword opened");
            }
            else{
                System.out.println("Trouble: " + virtualKeyboardOpened + " must be " + flag);
            }
        }*/
        /*

if (OS == ANDROID) {
            if (flag && !virtualKeyboardOpened) {
                engine.openKeyboard();
                virtualKeyboardOpened = true;
            }
            else if (!flag && virtualKeyboardOpened){
                engine.closeKeyboard();
                virtualKeyboardOpened = false;
            }
        }         */

    }

    public static boolean isLongPressed(){
        return longPressed;
    }

    public static boolean isLongPressing(){
        if (longPressedTimer != null) {
            if (longPressedTimer.isTime()){
                return true;
            }
            else return false;
        }
        else {
            if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
                longPressedTimer = new Timer(LONG_PRESSING_TIME);
            }
            return false;
        }
    }

    public static boolean isVirtualKeyboardOpened() {
        return virtualKeyboardOpened;
    }
}
