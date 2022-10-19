package com.mgdsstudio.blueberet.gameprocess.sound;

public class TrackData {
    private int audioType;
    private float startPos;
    private float endPos;
    private float volume;
    public final static float VOLUME_10_PERCENT = 0.15f;
    public final static float VOLUME_15_PERCENT = 0.15f;
    public final static float QUARTER_AUDIO = 0.25f;
    public final static float NORMAL_AUDIO = 0.5f;
    public final static float MAX_AUDIO = 1f;
    private boolean playing;

    public TrackData(float startPos, float endPos, float volume, int audioType) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.volume = NORMAL_AUDIO*volume;
        this.audioType = audioType;
    }

    public TrackData(float startPos, float endPos, float volume) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.volume = NORMAL_AUDIO*volume;
    }

    public float getStartPos() {
        return startPos;
    }

    public float getEndPos() {
        return endPos;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getAudioType() {
        return audioType;
    }
}
