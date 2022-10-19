package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public abstract class AbstractSoundController extends Thread{
    protected ArrayList<TrackData> data;
    protected ArrayList <SingleSoundsController> soundControllers;
    protected boolean active = false;
    public static boolean withSound = true;
    protected abstract void initTrackData();

    public AbstractSoundController(int lines, String path){
        if (withSound){
            active = true;
            soundControllers = new ArrayList<>(lines);
            data = new ArrayList<>();
            initTrackData();
            for (int i = 0; i < lines; i++){
                System.out.println("Sound try to load " + i + " from " + lines + " single lines");
                SingleSoundsController singleSoundsController = new SingleSoundsController(path);
                soundControllers.add(singleSoundsController);
            }
            this.start();
        }
        else {
            soundControllers = null;
            data = null;
        }
    }

    public void update(){
        for (SingleSoundsController controller : soundControllers){
            controller.update();
        }
    }

    @Override
    public void run() {
        while (active){
            //System.out.println("Updating");
            this.update();
            try {
                sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean canBeSoundAdded(){
        return true;
    }

    public void setAndPlayAudio(int type){
        if (canBeSoundAdded()) {
            boolean thereAreFreeControllers = false;
            for (int i = 0; i < soundControllers.size(); i++) {
                if (!soundControllers.get(i).isPlayingNow()) {
                    soundControllers.get(i).startToPlay(getTrackDataForType(type));
                    thereAreFreeControllers = true;
                    if (Program.debug) System.out.println("Audio track " + type + " was launched on controller " + i);
                    break;
                }
            }
            if (!thereAreFreeControllers) {
                reswitchAudioByOneOfThePlayingControllers(type);
            }
        }
    }

    public void setAndPlayAudio(int type, float amplitude){
        if (canBeSoundAdded()) {
            if (amplitude == 1) setAndPlayAudio(type);
            else {
                boolean thereAreFreeControllers = false;
                for (int i = 0; i < soundControllers.size(); i++) {
                    if (!soundControllers.get(i).isPlayingNow()) {
                        soundControllers.get(i).startToPlay(getTrackDataForType(type));
                        soundControllers.get(i).setVolume(amplitude);
                        if (Program.debug) System.out.println("Audio track " + type + " was launched");
                        thereAreFreeControllers = true;
                    }
                }
                if (!thereAreFreeControllers) {
                    reswitchAudioByOneOfThePlayingControllers(type);
                }
            }
        }
    }

    public void stopAllAudio(){
        for (SingleSoundsController controller : soundControllers){
            controller.stop();
        }
    }

    private void reswitchAudioByOneOfThePlayingControllers(int type) {
        int closestToEndNumber = 0;
        float closestToEndTime = 1000f;
        for (int i = 0; i < soundControllers.size(); i++){
            float timeToEnd = soundControllers.get(i).getTimeToEnd();
            if (timeToEnd < closestToEndTime){
                closestToEndTime= timeToEnd;
                closestToEndNumber = i;
            }
        }
        soundControllers.get(closestToEndNumber).startToPlay(getTrackDataForType(type));
        System.out.println("New audio was set on " + closestToEndNumber + " controller");
    }


    private TrackData getTrackDataForType(int type){
        for (TrackData trackData : data){
            if (trackData.getAudioType() == type){
                return trackData;
            }
        }
        System.out.println("Sound for this type is not founded");
        return data.get(0);
    }

    public void setActive(boolean flag){
        this.active = flag;
        if (active){
            run();
        }
        else {
            System.out.println("Audio was stopped");
        }
    }
}
