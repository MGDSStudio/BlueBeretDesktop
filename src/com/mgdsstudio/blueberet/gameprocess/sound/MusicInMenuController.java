package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.sound.SoundFile;

public class MusicInMenuController extends AbstractMusicController{
    //protected SoundFile file;
    public static boolean withMusic = true;
    private String path;
    private boolean started;
    private final String MAIN_MENU_SOUNDTRACK = "Menu music.wav";
    private final String INTRO_MENU_SOUNDTRACK = "Intro music.wav";
    private static boolean alreadyStarted;
    //private final boolean playOnlyOnce = true;

    public MusicInMenuController(float volume) {
        init(Program.getAbsolutePathToAssetsFolder(MAIN_MENU_SOUNDTRACK));
        this.volume = volume;
    }

    private void init(String path){

        pathToSoundtrack = path;
        if (withMusic && !alreadyStarted) {
            uploadMusic(path);
        }
    }

    private final void uploadMusic(String path){
        //if (!alreadyStarted) {


            //if (pathToSoundtrack != path) {
                final boolean saveInRAM = false;
                pathToSoundtrack = path;
                file = new SoundFile(Program.engine, Program.getAbsolutePathToAssetsFolder(path), saveInRAM);
                file.stop();
            //}
            if (alreadyStarted){
                System.out.println("This music was already started");
            }
        //}
    }

    @Override
    public void update() {
        System.out.println("Music is playing: " + file.isPlaying());
    }

    public boolean isPlaying(){
        if (file!= null) return file.isPlaying();
        else return false;
    }

    @Override
    public void pausePlay() {
        pause();
        //stop();
    }

    @Override
    public void resumePlay() {
        try {
            resume();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Can not resume music play");
        }
        //stop();
    }

    @Override
    public void startToPlay() {
        //System.out.println("Menu music already started: " + started + " prev ended: " + isPreviousEnded() + " with music: " + withMusic);
        reloadIfNeed();
        if (withMusic && (!started) && !alreadyStarted) {
            try {
                file.amp(volume);
                file.play();
                System.out.println("Menu music started with volume " + volume);
                started = true;
            } catch (Exception e) {
                System.out.println("Can not play menu sound. Exception: " + e);
            }
            alreadyStarted = true;
        }
    }

    private void reloadIfNeed() {
        if (pathToSoundtrack == path || pathToSoundtrack.equals(path)){
            if (Program.debug) System.out.println("Menu music must not be uploaded");
        }
        else {
            stop();
            System.out.print("Menu music was uploaded again. Prev: " + pathToSoundtrack + "" );
            uploadMusic(pathToSoundtrack);
            System.out.println(" . New: " + pathToSoundtrack + "" );
            started = false;
        }
    }

    public void initIntroMusic(){
        pathToSoundtrack = INTRO_MENU_SOUNDTRACK;
        uploadMusic(INTRO_MENU_SOUNDTRACK);
    }

    // || isPreviousEnded()

    private boolean isPreviousEnded() {
        if (file.position() >= file.duration()){
            System.out.println("Music is ended 1");
            return true;
        }
        else if (!file.isPlaying()){
            System.out.println("Music is ended 2");
            return true;
        }
        else {
            System.out.println("Music is not ended and is playing now and must not be started from the 0 pos");
            System.out.println("Pos: " + (int)file.position() + " from " + (int)file.duration() + " and " + file.isPlaying());
            return false;
        }
    }

    public void stop(){
        if (withMusic) {
            if (file != null) {
                System.out.println("Music was stopped at " + Program.engine.millis()/1000 + " second ");
                if (file.isPlaying()) System.out.println("Music is playing now. Channels: " + file.channels() );
                else System.out.println("It seems, that music is not playing now");
                //System.out.println("Channels: " + file.channels() );
                //file.jump(file.duration()-0.1f);
                file.stop();
                file.removeFromCache();
            }
            else {
                System.out.println("Music is already switched off");
            }
            started = false;
        }
    }

    public void pause(){
        if (withMusic) {
            if (file != null) {
                System.out.println("Music was paused at " + Program.engine.millis() / 1000 + " sec on pos: " + (int) file.position());
                file.pause();
                started = false;
            }
        }
    }

    public void resume(){
        if (withMusic) {
           /* if (!file.isPlaying()) {*/
            //if (!file.isPlaying()) {
            if (file != null){
                System.out.println("Music was resumed at " + Program.engine.millis() / 1000 + " sec on pos: " + (int) file.position());
                file.play();
            //}
                started = true;
            //}
            /*else {
                System.out.println("File is already playing and need not to be resumed");
            }*/
        }
            else System.out.println("Audio track is null");
        }

    }

    public void loadNewMusic(String s) {
        started = false;
        alreadyStarted = false;
        init(Program.getAbsolutePathToAssetsFolder(s));

        //uploadMusic(s);
        System.out.println("New music " + s + " was uploaded");
    }
}
