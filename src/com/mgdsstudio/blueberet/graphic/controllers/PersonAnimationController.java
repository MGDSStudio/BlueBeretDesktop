package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import processing.core.PImage;

import java.util.ArrayList;

public abstract class PersonAnimationController {
    public final static int STAY = 0;
    public final static int GO = 1;
    public final static int RUN = 2;
    public final static int TIPPING_OVER = 3;
    public final static int FLY = 4;
    public final static int CONTRA_JUMP = 5;
    public final static int JUMP_UP = 6;
    public final static int JUMP_DOWN = 7;
    public final static int ATTACK = 8;
    public final static int SHOT = 9;
    public final static int RELOAD = 10;
    public final static int DYING = 11;
    public final static int AWAKE = 12;
    public final static int IDLE = 13;
    public final static int KICK = 14;
    public final static int FLIP_JUMP = 15;

    public final static int READY_TO_ATTACK = 16;
    public final static int BODY_BY_AIM= 17;
    public final static int HEAD_BY_AIM_EYES_OPENED= 18;
    public final static int HEAD_BY_AIM_EYES_CLOSED= 19;
    public final static int HEAD_BY_AIM_MOUTH_OPENED= 20;
    public final static int CORPSE_SINGLE_SPRITE= 22;

    public final static int LUGGAGE = 23;
    public final static int LUGGAGE_END = 24;

    public final static int TAIL = 25;
    public final static int HANG = 26;

    protected boolean underAttack;
    protected boolean graphicFlip;

    public void setUnderAttack(boolean underAttack) {
        this.underAttack = underAttack;
    }

    public void stopBlinking() {
        System.out.println("Must be overriden");
    }

    public abstract ArrayList <SpriteAnimation> getAnimationsList();

    public abstract SpriteAnimation getAnimationForType(int type);

    public boolean getGraphicFlip() {
        return graphicFlip;
    }

    public void setGraphicFlip(boolean graphicFlip) {
        this.graphicFlip = graphicFlip;
    }

    public Tileset getTileset() {
        return getAnimationsList().get(0).getTileset();
    }
}
