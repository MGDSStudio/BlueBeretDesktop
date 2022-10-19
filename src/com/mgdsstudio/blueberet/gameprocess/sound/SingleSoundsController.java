package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.sound.SoundFile;

public class SingleSoundsController {
    private final SoundFile file;
    private float endPos;
    private boolean playingNow;

    public SingleSoundsController(String pathWithoutPrefix) {
        final boolean saveInRAM = false;
        file = new SoundFile(Program.engine, Program.getAbsolutePathToAssetsFolder(pathWithoutPrefix), saveInRAM);
    }

    public void update(){
        if (playingNow){
            if (file.isPlaying()){
                if (file.position() >= endPos){
                    file.stop();
                    playingNow = false;
                }
            }
            else playingNow = false;
        }
    }

    public boolean isPlayingNow() {
        return playingNow;
    }

    public void startToPlay(TrackData newTrackData){
        file.jump(newTrackData.getStartPos());
        endPos = newTrackData.getEndPos();
        playingNow = true;
        file.amp(newTrackData.getVolume());
    }

    public void stop() {
        if (playingNow) {
            playingNow = false;
            if (file.isPlaying()){
                file.stop();
            }
        }
    }

    public float getTimeToEnd(){
        if (playingNow){
            return endPos-file.position();
        }
        else  return -1f;
    }

    public void setVolume(float v) {
        file.amp(v);
    }
}
