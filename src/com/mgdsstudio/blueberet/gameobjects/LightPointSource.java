package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;

public class LightPointSource {
    private int x, y;
    private int diam;
    private int distToLight;
    private int time;
    private Timer timer;
    private boolean ended;
    private int changingCharacter;

    public LightPointSource(int x, int y, int diam, int time, int changingCharacter) {
        this.x = x;
        this.y = y;
        this.diam = diam;
        this.time = time;
        this.changingCharacter = changingCharacter;
    }

}
