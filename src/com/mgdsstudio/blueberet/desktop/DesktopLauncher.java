package com.mgdsstudio.blueberet.desktop;



import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.mainpackage.IEngine;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.loading.LanguageSpecificDataManager;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.preferences.PreferencesDataLoadMaster;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.MouseEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DesktopLauncher extends PApplet implements ContactListener, RayCastCallback, IEngine {
    private boolean gameScreenIsLeft = false;
    private final Vec2 centerPos = new Vec2();


    public static void main(String[] passedArgs) {
        System.out.println("Desktop version loaded successfully");
        String[] appletArgs = new String[] { "com.mgdsstudio.blueberet.desktop.DesktopLauncher" };
        System.out.println("Args: ");
        for (int i = 0; i < passedArgs.length; i++){
            System.out.println("*** " + passedArgs[i]);
        }
        if (passedArgs != null) {
            try {
                PApplet.main(PApplet.concat(appletArgs, passedArgs));
            }
            catch (Exception e){
                e.printStackTrace();
                PApplet.main(appletArgs);
            }
        } else {
            PApplet.main(appletArgs);
        }
        Program.OS = Program.DESKTOP;
        Program.debug = true;
        //Program.debug = false;
        Program.withAdds = false;
        System.out.println("Desktop version started");

    }

    private GameMainController gameMainController;


    public void settings() {
        String renderer;
        if (Program.USE_3D) renderer = P3D;
        else if (Program.graphicRenderer == Program.OPENGL_RENDERER) renderer = P2D;
        else renderer = JAVA2D;
        size(Program.DISPLAY_WIDTH, Program.DISPLAY_HEIGHT, renderer);
        if (Program.debug) smooth(Program.ANTI_ALIASING);
        else smooth(8);
        Program.ANTI_ALIASING = 8;
        updatePreferences();
    }

    public void setup() {
        frameRate(Program.NORMAL_FPS);
        Program.init(this);
        gameMainController = new GameMainController(this);
        if (gameScreenIsLeft) getSurface().setLocation(0,0);
        else getSurface().setLocation(displayWidth-90+width,displayHeight- Program.DISPLAY_HEIGHT-60);
    }

    private void updatePreferences() {
        LanguageSpecificDataManager manager = new LanguageSpecificDataManager();
        System.out.println("Actual language: " + manager.getLanguage());
        try {
            if (PreferencesDataLoadMaster.dataFileExists()) {
                System.out.println("I need to use an another library to json read");
                PreferencesDataLoadMaster preferencesDataLoadMaster = new PreferencesDataLoadMaster();
                int value = preferencesDataLoadMaster.getAntiAliasingWithoutProcessing();
                System.out.println("Anti aliasing: "  + value);
                Program.ANTI_ALIASING = value;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw() {
        gameMainController.update();
        gameMainController.draw();

        try {
            readConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        println("Scalling");
        if (e > 0)
            Program.setMouseWheelRotation(Program.BACKWARD_ROTATION);
        else if (e < 0)
            Program.setMouseWheelRotation(Program.FORWARD_ROTATION);
    }

    public void fileSelected(File selection) {
        System.out.println("Some file was chosen in Desktop mode");
        if (Program.gameStatement == Program.LEVELS_EDITOR) {
            Editor2D.setNextLocalStatement();
            Editor2D.setExternalFilePath(selection);
            Editor2D.fileWasChoosen = true;
        }
    }


    @Override
    public void beginContact(Contact contact) {
        PhysicGameWorld.addBeginContact(contact);

    }

    @Override
    public void endContact(Contact contact) {
        PhysicGameWorld.addEndContact(contact);
        gameMainController.contactStartedSolveInterrupt(contact, null);
    }

    @Override
    public void postSolve(Contact arg0, ContactImpulse arg1) {
        // TODO Auto-generated method stub
        // gameMainController.contactStartedSolveInterrupt(arg0, null);
        // PhysicGameWorld.addPostContact(arg0, arg1);
    }


    @Override
    public void preSolve(Contact arg0, Manifold arg1) {
        Program.resetFixedRotationForBodiesWithBullets(arg0.m_fixtureA.getBody(),arg0.m_fixtureB.getBody());
        //if (Programm.gameStatement == Programm.GAME_PROCESS) gameMainController.addPreContact(arg0, arg1);
    }

    public void keyPressed() {
        if (Program.OS == Program.DESKTOP) {
            if (keyCode == 37)
                PlayerControl.keyLeftPressed = true;
            else if (keyCode == 39)
                PlayerControl.keyRightPressed = true;
            else if (keyCode == 38)
                PlayerControl.keyUpPressed = true;
            else if (keyCode == 40)
                PlayerControl.keyDownPressed = true;
            else if (keyCode == 75)
                PlayerControl.kickKeyPressed = true;
            else if (keyCode == 55)
                PlayerControl.key7Pressed = true;
            else if (keyCode == 56)
                PlayerControl.key8Pressed = true;
            else if (keyCode == 53)
                PlayerControl.key5Pressed = true;
            else if (keyCode == 54)
                PlayerControl.key6Pressed = true;
            else if (key == 'v' || key == 'V' || key == 'М' || key == 'м') PlayerControl.sPressed = true;
            else if (key == 'b' || key == 'B' || key == 'И' || key == 'и') PlayerControl.bPressed = true;

            else if (key == PConstants.BACKSPACE)
                PlayerControl.backspacePressed = true;
            if (keyCode == 16)
                PlayerControl.shiftPressed = true;

            if (keyCode == 80) {
                if (isLooping()) {
                    System.gc();
                    noLoop();
                } else
                    loop();
            }
            //println("Key " + keyCode + " pressed");
        }
    }

    public void keyReleased() {
        if (Program.OS == Program.DESKTOP) {
            if (keyCode == 37)
                PlayerControl.keyLeftPressed = false;
            else if (keyCode == 39)
                PlayerControl.keyRightPressed = false;
            else if (keyCode == 38)
                PlayerControl.keyUpPressed = false;
            else if (keyCode == 40)
                PlayerControl.keyDownPressed = false;
            else if (keyCode == 53)
                PlayerControl.key5Pressed = false;
            else if (keyCode == 54)
                PlayerControl.key6Pressed = false;
            else if (keyCode == 55)
                PlayerControl.key7Pressed = false;
            else if (keyCode == 56)
                PlayerControl.key8Pressed = false;
            else if (keyCode == 75)
                PlayerControl.kickKeyPressed = false;
            else if (key == 'v' || key == 'V' || key == 'М' || key == 'м') PlayerControl.sPressed = false;
            else if (key == 'b' || key == 'B' || key == 'И' || key == 'и') PlayerControl.bPressed = false;
            else if (key == PConstants.BACKSPACE)
                PlayerControl.backspacePressed = false;
            if (keyCode == 16)
                PlayerControl.shiftPressed = false;
            else if (keyCode == 2)
                backPressed();

            else if (keyCode == 27 || key == 'H' || key == 'h' || key == 'р' || key == 'Р')
                gameMainController.backPressed();
        }
    }

    public void backPressed() {
        if (Program.gameStatement == Program.LEVELS_EDITOR)
            Program.gameStatement = Program.SOME_MENU;
        else if (Program.gameStatement == Program.GAME_PROCESS) {
            if (Program.levelLaunchedFromRedactor) {
                Program.jumpFromRedactorToGame = true;
                Program.gameStatement = Program.LEVELS_EDITOR;
            } else
                Program.gameStatement = Program.SOME_MENU;
        } else if (Program.gameStatement == Program.SOME_MENU)
            exit();
    }

    @Override
    public float reportFixture(Fixture fixture, Vec2 vec2, Vec2 vec21, float v) {
		/*
		//The point of collision with the fixture in box coordinates
		System.out.println("Coalision with fixture: ");
		System.out.println(fixture + " at point " + PhysicGameWorld.controller.coordWorldToPixels(point));

		collisionPoint.set(point);
		//-1 to ignore this fixture and continue
		//0 to terminate the ray cast
		//fraction to clip the ray at this point
		//1 to not the clip the ray and continues

		isColliding=true;
		collisionPoint.set(point);
		if(!collisionBodies.contains(fixture.getBody()))
			collisionBodies.add(fixture.getBody());
		return fraction;
		*/
        return 0f;
    }

    public static final void switchOffKetaiGesture(){
        System.out.println("In Desktop mode is not useful");
    }

    public static final void switchOnKetaiGesture(){
        System.out.println("In Desktop mode is not useful");
    }

    private void readConsole(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //System.out.print("Enter String");
        try {
            if (System.in.available()>0) {
                try {
                    String s = br.readLine();
                    //System.out.println("Data: " + s);
                    gameMainController.consoleInput(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //System.out.print("Enter Integer:");
                /*try {
                    int i = Integer.parseInt(br.readLine());
                } catch (NumberFormatException | IOException nfe) {
                    System.err.println("Invalid Format!");
                }*/
            }
            else{
                //System.out.println("No data");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object getMainActivity() {
        if (Program.debug) System.out.println("No activity in desktop mode");
        return null;
    }

    @Override
    public void stopMusicForAdd() {
        if (Program.debug) System.out.println("No add in desktop mode");
    }

    @Override
    public void exitWithoutActivitySaving() {
        if (Program.debug) System.out.println("No activity in desktop mode");
    }

    @Override
    public void initFullScreenAdd() {
        if (Program.debug) System.out.println("No full screen ads in desktop mode");
    }

    @Override
    public void initRewardedAdd() {
        if (Program.debug) System.out.println("No rewarded screen ads in desktop mode");
    }

    @Override
    public boolean isRewardedAddStartedToUpload() {
        return false;
    }

    @Override
    public boolean isRewardedAppFailedToUpload() {
        return false;
    }

    @Override
    public boolean isPlayerMustBeRewarded() {
        return false;
    }

    @Override
    public boolean isRewardedAppIsDismissed() {
        return false;
    }

    @Override
    public void resetFullScreenAdd() {

    }

    @Override
    public boolean isFullScreenAdStartedToUpload() {
        return false;
    }

    @Override
    public void resetRewardAdd() {

    }

    @Override
    public boolean isFullScreenAddCompleted() {
        return false;
    }

    @Override
    public void addToastMessage(String text) {

    }

    @Override
    public boolean isSdkMoreOrSameAsNeeded() {
        return false;
    }

    @Override
    public int getRewardValue() {
        return 0;
    }

    @Override
    public String getPathInCache(String additionalPath) {
        return null;
    }

    @Override
    public String[] getAssets(String path) {
        if (Program.debug) System.out.println("Get assets is not tested");
        File file = new File(Program.getRelativePathToAssetsFolder());
        String [] array = file.list();
        return array;
    }

    @Override
    public File getExternalFilesDir(String childDirName) {
        return null;
    }

    @Override
    public InputStream createInputStreamInAssetsForFile(String file) {
        return null;
    }

    @Override
    public void copyDataToClipboard(String MAIL, String path) {
        if (Program.debug) System.out.println("Can not copy using this clipboard function");
    }

    @Override
    public void openKeyboard(boolean flag) {
        System.out.println("No keyboard for this OS");
    }

    @Override
    public void copy(File source, File output) {
        try {
            FileManagement.copy(source, output);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStringToClippboard(String dataString) {

    }

    @Override
    public ArrayList<String> getFilesListInAssets() {

        File file = new File(Program.getRelativePathToAssetsFolder());
        ArrayList<String> list = new ArrayList<>();
        String [] array = file.list();
        for (int i = 0; i < array.length; i++){
            list.add(array[i]);
        }
        return list;
    }

    @Override
    public String getSketchPath() {
        return sketchPath();
    }

    @Override
    public void copyUserLevelsToCache() {
        System.out.println("Levels must not be copied to the cache folder");
    }

    @Override
    public Vec2 getCenterTouchPosition(Vec2 basicPosition, int maxZoneRadius) {
        centerPos.x = mouseX;
        centerPos.y = mouseY;
        return centerPos;
    }

    @Override
    public void fillOnStickTouchesArray(ArrayList<Coordinate> onStickTouchesArray, ArrayList<Coordinate> mutStickTouchesPool, Rectangular allStickHoleZone) {
        if (Program.debug) System.out.println("Touches array must not be cleared");
        onStickTouchesArray.clear();
    }

    @Override
    public boolean isButtonTouched(OnScreenButton onScreenButton) {
        if (Program.engine.mousePressed == true) {
            if (Program.engine.dist(onScreenButton.getX(), onScreenButton.getY(), Program.engine.mouseX, Program.engine.mouseY) < onScreenButton.getRadius()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<String> getFilesListInCache() {
        System.out.println("Function is not implemented");
        return null;
    }

    @Override
    public ArrayList<String> getFilesListInAssetsFolder() {
        ArrayList<String> names = new ArrayList<>();
        try {
            try (Stream<Path> paths = Files.walk(Paths.get(Program.getRelativePathToAssetsFolder()))) {
                List<String> files = paths.filter(x -> Files.isRegularFile(x))
                        .map(Path::toString)
                        .collect(Collectors.toList());
                files.forEach(System.out::println);
                for (int i = 0; i < files.size(); i++) names.add(files.get(i));

            } catch (IOException ex) {
                System.out.println("Can not get files list for this API version");
                ex.printStackTrace();
            }

        } catch (Exception e) {

        }
        return names;
    }

}
