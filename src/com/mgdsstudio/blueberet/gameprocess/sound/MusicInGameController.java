package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.sound.SoundFile;

public class MusicInGameController extends AbstractMusicController{
    public static boolean withMusic = true;
    private boolean audioFounded;
    public final static String CLASS_NAME = "Track";
    private static float endPlace;
    private boolean startWhenUploaded = false;
    public final boolean REPEATING = false;
    public final static boolean ONCE = true;
    private boolean repeateability;

    public MusicInGameController(String pathWithoutPrefix, boolean continueFromPrev, float volume) {
        if (pathWithoutPrefix.length()<1) {
            audioFounded = false;
            if (file != null){
                file.stop();
                file.removeFromCache();
                file = null;
            }
        }
        else {
            audioFounded = true;
        }
        if (withMusic && audioFounded) {
            final boolean saveInRAM = false;
            System.out.println("New audio file must be " + pathWithoutPrefix);
            if (pathToSoundtrack == null) {
                System.out.println("Audio was not uploaded yet. New audio track must be uploaded");
                pathToSoundtrack = pathWithoutPrefix;
                file = new SoundFile(Program.engine, Program.getAbsolutePathToAssetsFolder(pathWithoutPrefix), saveInRAM);
                System.out.println("Audio file is uploaded");
            } else {
                if (pathWithoutPrefix != pathToSoundtrack && !pathWithoutPrefix.equals(pathToSoundtrack)) {
                    System.out.println("Previous level had an another sound file. New audio track must be uploaded");
                    try{
                        file.removeFromCache();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    pathToSoundtrack = pathWithoutPrefix;
                    endPlace = 0;
                    file = new SoundFile(Program.engine, Program.getAbsolutePathToAssetsFolder(pathToSoundtrack), saveInRAM);
                }
                else {
                    System.out.println("This music was already played. It must not be uploaded again");
                    if (!continueFromPrev) endPlace = 0f;
                }
            }
        }
        this.volume = volume;
    }

    public final static String getPathToLevelSoundtrackWithoutRelativePath(int levelNumber){
        if (levelNumber == 1) return "Track level 1.wav";
        else return "Track level 1.wav";
    }

    public final static String getPathToLevelSoundtrackWithRelativePath(int levelNumber){
        if (levelNumber == 1) return Program.getAbsolutePathToAssetsFolder("Track level 1.wav");
        else return Program.getAbsolutePathToAssetsFolder("Track level 1.wav");
    }

    public void update(){
        if (audioFounded) {
            if (startWhenUploaded) {
                startToPlay();
            }
            else if (file != null) {
                if (!file.isPlaying()) {
                    if (repeateability == REPEATING) {
                        if (file.position() >= (file.duration() * 0.95f)) {
                            file.jump(0f);
                            //file.play();
                            if (Program.debug) System.out.println("Audio was started again");
                        } else {
                            if (!file.isPlaying()) {
                                file.play();
                            }
                            //System.out.println("Audio continues to play");
                        }
                    } else {
                        if (file.position() >= file.duration() * 0.99f) {
                            file.stop();
                            //Sound sound = new Sound(Program.engine);
                            //Sound.
                        }
                    }
                }
            }
        }
    }


    public void startToPlay(){
        if (audioFounded) {
            if (withMusic) {
                if (file == null) {
                    startWhenUploaded = true;
                    //System.out.println("Music is now null and was not uploaded yet");
                } else {
                    if (endPlace > 0 && endPlace < file.duration()) {
                        if (!file.isPlaying()) {
                            file.play();
                            file.amp(volume);
                            startWhenUploaded = false;
                        }
                    } else {
                        endPlace = 0;
                        try {
                            System.out.println("Music try to start to play. File is null: " + (file == null));
                            file.pause();
                            file.jump(endPlace);
                            startWhenUploaded = false;
                            file.amp(volume);
                        } catch (Exception e) {
                            startWhenUploaded = true;
                            e.printStackTrace();
                            System.out.println("Can not start audio");
                        }
                    }
                }
            }
        }
    }

    public void pausePlay(){
        if (withMusic && audioFounded) {
            try {
                endPlace = file.position();
                file.pause();
                System.out.println("Audio was stopped");
            }
            catch (Exception e){
                System.out.println("Audio can not be stopped. " + e.getMessage());
            }
        }
    }

    public void stopPlay(){
        if (withMusic) {
            file.stop();
        }
    }

    @Override
    public void resumePlay() {

        if (withMusic && audioFounded) {
            System.out.println("Music was resumed at " + Program.engine.millis()/1000  + " sec on pos: " + (int)file.position());
            file.play();
        }
    }


    public void setPlayOnce(boolean playOnce){
        repeateability = playOnce;
    }

}
