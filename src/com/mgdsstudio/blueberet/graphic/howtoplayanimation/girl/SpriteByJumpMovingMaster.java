package com.mgdsstudio.blueberet.graphic.howtoplayanimation.girl;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PGraphics;

class SpriteByJumpMovingMaster {

    private int y, startY;
    private int zoneWidth;
    private int timeForSingleStageHidding;
    private int jumpHeight;
    private Timer timer;
    private int actualHeight;
    private int maxHeight;

    private final int STAGE_NOTHING = 0;
    final int STAGE_HIDDING = 1;
    final int STAGE_APPEARING = 2;
    private int stage;
    private int menuActualStage;
    private float velocity;
    private float delta;
    private int jumpStartMoment;
    private float gravityConst = 9.81f*13f;

    //private boolean firstLaunch;

    SpriteByJumpMovingMaster(int y, float jumpHeight, int timeToFullHiddingAndHidding) {
        this.y = y;
        this.startY = y;
        this.timeForSingleStageHidding = timeToFullHiddingAndHidding/2;
        calvulateGravityConstant(jumpHeight, timeToFullHiddingAndHidding);
        //velocity = (jumpHeight*2f)/((float)timeForSingleStageHidding/1000f);


        //float maxJumpHeight = velocity*velocity/(2f*gravityConst);
        velocity = -PApplet.sqrt(jumpHeight*2f*gravityConst);



        //velocity = -PApplet.sqrt((float)jumpHeight*2f*gravityConst);
        //float velocity1 =  -1f*timeToFullHiddingAndHidding*gravityConst/1000f;
        //velocity = velocity1;
        System.out.println("Jump velocity: " + velocity + " ot " + velocity);


    }

    private void calvulateGravityConstant(float jumpHeight, float timeForSingleStageHidding) {
        timeForSingleStageHidding/=1000f;
        gravityConst = 2f*(jumpHeight)/(timeForSingleStageHidding*timeForSingleStageHidding);
        System.out.println("Gravity const " + gravityConst + " time: " + timeForSingleStageHidding);

    }

    /*
    this.y = y;
        this.startY = y;
        this.timeForSingleStageHidding = timeToFullHiddingAndHidding;

        velocity = (jumpHeight*2f)/((float)timeToFullHiddingAndHidding/2000f);


        velocity = -PApplet.sqrt((float)jumpHeight*2f*gravityConst);

        float velocity1 =  -1f*timeToFullHiddingAndHidding*gravityConst/1000f;
        velocity = velocity1;
        System.out.println("Jump velocity: " + velocity + " ot " + velocity1);
     */



    void init(int zoneHeight) {
        //timer = new Timer(timeForSingleStageHidding);
        initPos(zoneHeight);

    }

    private void initPos(int zoneHeight) {
        //float step = zoneHeight/(pos.length+1);
        //maxHeight = zoneHeight;
        //float y = this.y-zoneHeight/2+(step/2f);
        /*for (int i = 0; i < pos.length; i++){
            pos[i] = new Coordinate(x, y);
            y+=step;
        }*/
    }

    void recreate(){
        timer.setNewTimer(timeForSingleStageHidding);
        actualHeight = 0;
    }

    void update(int stage){
        updateStatement();
    }

    private void updateStatement() {
        if (stage == STAGE_HIDDING){
            /*if (timer.isTime()){
                timer.setNewTimer(timeForSingleStageHidding);
                stage++;
            }*/
            updateHeight();
        }
        else if (stage == STAGE_APPEARING){
            /*if (timer.isTime()){
                timer.setNewTimer(timeForSingleStageHidding);
                stage++;
            }*/
            updateHeight();
        }
        else {
            //stage = 0;
            //if (timer !=) timer.stop();
            y = startY;
        }
    }


    private void updateHeight(){
        float deltaTime = ((float)Program.engine.millis()-(float)jumpStartMoment)/1000f;
        //y= (int) ((startY+velocity*deltaTime+(gravityConst*deltaTime*deltaTime)/2f));
        float deltaY = (velocity*deltaTime+((gravityConst*deltaTime*deltaTime)/2f));
        y= (int) ((startY+deltaY));

    }

    void draw(PGraphics graphics, StaticSprite staticSprite){
        /*if (stage == STAGE_APPEARING || stage == STAGE_HIDDING) {
            //staticSprite.draw
            graphics.pushStyle();
            graphics.rectMode(PConstants.CORNER);
            graphics.noStroke();
            graphics.fill(0, 0, 0);
            //graphics.rect(x, y, zoneWidth, actualHeight);
            //System.out.println("Rect drawn with height: " + actualHeight + " and width " + zoneWidth + " at pos: " + pos[i].x + "x " + pos[i].y);
            graphics.popStyle();
        }*/
    }

    void setStage(int stage) {
        this.stage = stage;
    }

    void startToHide(){
        jumpStartMoment = Program.engine.millis();
        stage = STAGE_HIDDING;
        if (timer == null) timer = new Timer(timeForSingleStageHidding);
        else timer.setNewTimer(timeForSingleStageHidding);
    }

    void startToAppear(){
        stage = STAGE_APPEARING;
        if (timer == null) timer = new Timer(timeForSingleStageHidding);
        else timer.setNewTimer(timeForSingleStageHidding);
    }

    public void stop() {
        stage = STAGE_NOTHING;
        y = this.startY;
    }

    public int getY() {
        return y;
    }
}
