package com.mgdsstudio.blueberet.graphic;

public class AnimationData {
    public final static byte STAY = 0;
    public final static byte GO = 1;
    public final static byte RUN = 2;
    public final static byte TIPPING_OVER = 3;
    public final static byte FLY = 4;
    public final static byte CONTRA_JUMP = 5;
    public final static byte JUMP_UP = 6;
    public final static byte JUMP_DOWN = 7;
    public final static byte ATTACK = 8;
    public final static byte SHOT = 9;
    public final static byte RELOAD = 10;
    private byte type = GO;

    private byte startSprite;
    private byte endSprite;
    private int spriteFrequency;

    public AnimationData(byte type, byte startSprite, byte endSprite, int spriteFrequency){
        this.type = type;
        this.startSprite = startSprite;
        this.endSprite = endSprite;
        this.spriteFrequency = spriteFrequency;
    }

    public byte getEndSprite() {
        return endSprite;
    }

    public byte getStartSprite() {
        return startSprite;
    }

    public byte getType() {
        return type;
    }

    public int getSpriteFrequency() {
        return spriteFrequency;
    }


}
