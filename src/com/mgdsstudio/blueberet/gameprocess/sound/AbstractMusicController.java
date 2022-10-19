package com.mgdsstudio.blueberet.gameprocess.sound;

import processing.sound.SoundFile;

public abstract class AbstractMusicController {

    // Only one soundtrack can be uploaded - menu or level music
    protected static SoundFile file;
    protected static String pathToSoundtrack;
    protected float volume = TrackData.NORMAL_AUDIO;

    public abstract void update();

    public abstract void pausePlay();

    public abstract void resumePlay();

    public abstract void startToPlay();

    public void removeFromMemory(){
        file = null;
        pathToSoundtrack = null;
    }


}
