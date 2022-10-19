package com.mgdsstudio.blueberet.graphic.bullettimescreen;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public abstract class BulletTimeScreen {
    protected boolean activated;
    //private Timer timer;
    //private final static int STOPPED = 0;
    protected final static int LONG_PULSATION = 1;
    protected final static int SHORT_PULSATION = 2;
    protected final static int END = 3;
    protected int mainStage;
    protected Timer timerToNextStage;
    protected final int NORMAL_TIME_FOR_LONG_PULSE = 1500;
    protected final float cameraAddingScaleByPulse = 0.1f;
    protected int actualLongPulsationStage = 0;
    protected int actualShortPulsationStage = 0;
    protected final int [] brakingPulsationTimes = {800,550,350,200};

    protected int longPulseTime;  // = NORMAL_TIME_FOR_LONG_PULSE/ Must be adjusted for specific bullet time
    protected int longPulsesNumber;   //First stages number


    public abstract void activate(int time);


    public final boolean isActivated() {
        return activated;
    }


    public abstract void draw(GameCamera gameCamera);
}
