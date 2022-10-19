package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;
import processing.core.PGraphics;

class SpriteOnBlackBackgroundHiddingMaster {
    private int x,y;
    private int zoneWidth;
    private int timeToFullHidding;
    private Timer timer;
    private int actualHeight;
    private final Coordinate [] pos;
    private int maxHeight;
    //private boolean firstLaunch;

    SpriteOnBlackBackgroundHiddingMaster(int x, int y, int zoneWidth, int zoneHeight, int timeToFullHidding, int numbers) {
        this.x = x;
        this.y = y;
        this.zoneWidth = zoneWidth;
        this.timeToFullHidding = timeToFullHidding;
        pos = new Coordinate[numbers];
        init(zoneHeight);
    }

    void init(int zoneHeight) {
        timer = new Timer(timeToFullHidding);
        initPos(zoneHeight);

    }

    private void initPos(int zoneHeight) {
        float step = zoneHeight/(pos.length+1);
        maxHeight = Program.engine.ceil(step*1.05f);
        float y = this.y-zoneHeight/2+(step/2f);
        for (int i = 0; i < pos.length; i++){
            pos[i] = new Coordinate(x, y);
            y+=step;
        }
    }

    void recreate(){
        timer.setNewTimer(timeToFullHidding);
        actualHeight = 0;
    }

    void update(){
        updateHeight();
    }


    private void updateHeight(){
        float deltaTime = (float)(timeToFullHidding-timer.getRestTime())/1000f;
        actualHeight = (int) ((float)maxHeight*deltaTime);
        //System.out.println("Time: " + deltaTime);
    }

    void draw(PGraphics graphics){
        for (int i = 0; i < pos.length; i++){
            graphics.pushStyle();
            graphics.rectMode(PConstants.CENTER);
            graphics.noStroke();
            graphics.fill(0,0,0);
            graphics.rect(pos[i].x,pos[i].y, zoneWidth,actualHeight);
            //System.out.println("Rect drawn with height: " + actualHeight + " and width " + zoneWidth + " at pos: " + pos[i].x + "x " + pos[i].y);
            graphics.popStyle();
        }

    }

}
