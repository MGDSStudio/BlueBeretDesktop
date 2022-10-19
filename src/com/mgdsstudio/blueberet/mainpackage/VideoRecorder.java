package com.mgdsstudio.blueberet.mainpackage;

import processing.core.PApplet;

public class VideoRecorder {
    private int frameNumberToBeSaved = 1; // every
    private final PApplet engine;
    private boolean activated;
    private final String path;
    private static int frameNumber = 1;
    private int activatingFrame;
    private int framesBeforeRecording = 120;

    private final String fileName = "Frame";

    public VideoRecorder(PApplet engine, String path, int frameNumberToBeSaved){
        this.engine = engine;
        this.path = path;
        this.frameNumberToBeSaved = frameNumberToBeSaved;
    }

    public void update(){
        if (activated) {
            if (engine.frameCount >= activatingFrame) {
                if (frameNumberToBeSaved == 1) {
                    engine.save(path + fileName + frameNumber + ".png");
                    frameNumber++;
                } else if (engine.frameCount % frameNumberToBeSaved == 1) {
                    engine.save(path + fileName + frameNumber + ".png");
                    frameNumber++;
                }
            }
        }
    }

    public void start(){
        if (!activated) {
            activated = true;
            activatingFrame = engine.frameCount+framesBeforeRecording;
        }
        else System.out.println("*** It is already activated!");
    }

    public void stop(){
        if (activated) activated = false;
        else System.out.println("*** It was not launched");
    }

}
