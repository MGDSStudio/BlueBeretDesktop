package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PGraphics;

public class FramesSaver {

    private static int savingFrameNumber = 1;
    private final String FRAME_FILENAME = "Frame ";
    private final String FRAME_EXTENSION = ".png";

    private final int NOTHING = 0;
    private final int STARTED_ON_THIS_FRAME = 1;
    private final int REGULAR_SAVING = 2;
    private final int END_ON_THIS_FRAME = 3;
    private final int ENDED = 4;
    private int statement = NOTHING;
    private int startFrameToSave = savingFrameNumber;
    private GameCamera gameCamera;
    private int width = -1, height = -1;
    private PGraphics frame;
    private boolean fullscreen;


    public FramesSaver(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        fullscreen = true;
    }

    public FramesSaver(GameCamera gameCamera, int width, int height) {
        this.gameCamera = gameCamera;
        this.width = width;
        this.height = height;
        fullscreen = false;
        frame = Program.engine.createGraphics(width, height, PConstants.P2D);
        frame.smooth(4);
        frame.beginDraw();
        frame.clear();
        frame.endDraw();
    }

    void update(){
        updateLaunch();
        if (statement == NOTHING){

        }
        else if (statement == REGULAR_SAVING){
            if (fullscreen) updateFrameSaving(Program.objectsFrame);
            else updateFrameAreaSaving(Program.objectsFrame);
        }
    }

    private void updateLaunch() {
        //if (Program.debug) {
            if (Program.OS == Program.DESKTOP) {
                if (statement == NOTHING) {
                    if (isButtonPressed()){
                        statement = STARTED_ON_THIS_FRAME;
                        startFrameToSave = savingFrameNumber;
                        gameCamera.setScale(GameCamera.maxScale);
                        System.out.println("Frame saving launched from frame " + savingFrameNumber);
                    }
                }
                else if (statement == STARTED_ON_THIS_FRAME) {
                    if (!isButtonPressed()){
                        statement = REGULAR_SAVING;
                        gameCamera.setScale(GameCamera.maxScale);
                        System.out.println("Start to save " + statement);
                    }
                }
                else if (statement == REGULAR_SAVING) {
                    if (isButtonPressed()){
                        statement = END_ON_THIS_FRAME;
                        gameCamera.setScale(GameCamera.maxScale);
                        System.out.println("End to save " + statement);
                    }
                }
                else if (statement == END_ON_THIS_FRAME) {
                    if (isButtonPressed()){
                        statement = ENDED;
                        gameCamera.setScale(GameCamera.maxScale);
                        System.out.println("Saving ended " + statement + "; First sprite: " + startFrameToSave + " last: " + (savingFrameNumber-1));
                    }
                }
                else if (statement == ENDED) {
                    if (!isButtonPressed()){
                        statement = NOTHING;
                    }
                }
            }
        //}
    }

    private void updateFrameSaving(PGraphics graphics) {
        graphics.save(Program.getAbsolutePathToAssetsFolder(FRAME_FILENAME + savingFrameNumber + FRAME_EXTENSION));
            savingFrameNumber++;
        }

    private void updateFrameAreaSaving(PGraphics source) {
        frame.beginDraw();
        frame.clear();
        frame.pushStyle();
        //frame.loadPixels();
        frame.imageMode(PConstants.CENTER);
        frame.image(source,frame.width/2,frame.height/2);
        frame.popStyle();
        //frame.updatePixels();
        frame.save(Program.getAbsolutePathToAssetsFolder(FRAME_FILENAME + savingFrameNumber + FRAME_EXTENSION));
        frame.endDraw();

        /*
        source.pushStyle();
        source.loadPixels();
        source.imageMode(PConstants.CENTER);
        //sourcePixels = source.pixels;
        PImage area;
        area = source.get(source.width/2, source.height/2, source.width, source.height);
        source.updatePixels();

        source.popStyle();
        frame.beginDraw();
        frame.pushStyle();
        frame.loadPixels();
        for (int i = 0; i < frame.width; i++){
            for (int j = 0; j < frame.height; j++){
               // frame.get()

            }
        }
        frame.imageMode(PConstants.CORNER);
        //frame.set(0,0,area);
        frame.image(area,frame.width/2,frame.height/2);
        frame.popStyle();
        frame.updatePixels();
        frame.save(Program.getAbsolutePathToAssetsFolder(FRAME_FILENAME + savingFrameNumber + FRAME_EXTENSION));
        frame.endDraw();
        * */


        savingFrameNumber++;
    }

    private boolean isButtonPressed() {
        if (Program.engine.keyPressed) {
            if (Program.engine.key == 'S' || Program.engine.key == 's' || Program.engine.key == 'ы' || Program.engine.key == 'Ы') {
                return true;
            } else {
                return false;
            }
        }
        else return false;
    }


}
