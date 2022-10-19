package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class SingleFlash {

    //private float x, y;
    private Vec2 pos;
    private final static int FLASH_TIME = 150;

    private final static int START_ALPHA = 15;

    private int actualTint = START_ALPHA;
    private Timer timer;
    private int startTime;
    private boolean ended = false;
    private boolean flip;

    public SingleFlash(Vec2 pos, boolean flip) {
        this.pos = pos;
        this.flip = flip;
    }

    public void recreate(float x, float y, boolean flip){
        pos.x = x;
        pos.y = y;
        this.flip = flip;
    }

    public void start(){
        if (timer == null) timer = new Timer(FLASH_TIME);
        else timer.setNewTimer(FLASH_TIME);
        startTime = Program.engine.millis();
        if (ended) {
            actualTint = START_ALPHA;
            ended = false;
        }
    }

    public void draw(GameCamera gameCamera, StaticSprite image){
        if (!ended){
            image.setMomochromeTint(255, actualTint);
            image.draw(gameCamera, pos, flip);
        }
    }

    public void update(){
        if (!ended){
            if (timer.isTime()){
                ended = true;
            }
            else updateActualTint();
        }
    }

    private void updateActualTint() {
        int timeAfterStart = Program.engine.millis()-startTime;
        int value = START_ALPHA*timeAfterStart/FLASH_TIME;
        actualTint = START_ALPHA-value;
        if (actualTint<=0) {
            actualTint = 0;
            ended = true;
        }
    }

    public boolean isEnded() {
        return ended;
    }
}
