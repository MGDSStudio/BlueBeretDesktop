package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class IndependentOnScreenMovableSprite extends IndependentOnScreenGraphic{


    private float xStartVelocity, yStartVelocity, xAccelerate, yAccelerate, angleVelocity, angleAccelerate;
    private Timer timerToClose;
    private StaticSprite staticSprite;
    private boolean ended;
    private int timeToClose;
    private int type;


    /*
    public IndependentOnScreenMovableSprite(StaticSprite staticSprite, Vec2 position, float angleInDegrees, float xStartVelocity, float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity, int timeToClose) {
        this.xStartVelocity = xStartVelocity;
        this.yStartVelocity = yStartVelocity;
        this.xAccelerate = xAccelerate;
        this.yAccelerate = yAccelerate;
        this.angleVelocity = angleVelocity;
        this.timeToClose = timeToClose;
        timerToClose = new Timer(timeToClose);
        this.staticSprite = staticSprite;
        this.position = position;
        this.angle = angleInDegrees;
        this.type = MoveableSpritesAddingController.NOT_DETERMINED;
    }*/

    public IndependentOnScreenMovableSprite(StaticSprite staticSprite, Vec2 position, float angleInDegrees, float xStartVelocity, float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity, int timeToClose, int type) {
        init(staticSprite, position, angleInDegrees, xStartVelocity, yStartVelocity, xAccelerate, yAccelerate, angleVelocity, timeToClose, type);
    }

    private void init(StaticSprite staticSprite, Vec2 position, float angleInDegrees, float xStartVelocity, float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity, int timeToClose, int type){
        this.xStartVelocity = xStartVelocity;
        this.yStartVelocity = yStartVelocity;
        this.xAccelerate = xAccelerate;
        this.yAccelerate = yAccelerate;
        this.angleVelocity = angleVelocity;
        this.timeToClose = timeToClose;
        this.staticSprite = staticSprite;
        this.position = position;
        this.angle = angleInDegrees;
        this.type = type;
        timerToClose = new Timer(timeToClose);
        ended = false;
    }

    public void recreate(Vec2 position, float angleInDegrees, float xStartVelocity, float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity){
        init(this.staticSprite, position, angleInDegrees, xStartVelocity, yStartVelocity, xAccelerate, yAccelerate, angleVelocity, this.timeToClose, this.type);
    }

    public void draw(GameCamera gameCamera){
        update();
        staticSprite.draw(gameCamera, position, Program.engine.radians(angle), flip);
    }


    private void update(){
        updatePositionAndAngle();
        if (timerToClose != null){
            if (timerToClose.isTime()){
                ended = true;
            }
        }
    }

    private void updatePositionAndAngle() {
        float coef = (Program.deltaTime/1000f);
        position.x+=xStartVelocity*coef ;
        position.y+=yStartVelocity*coef;
        angle+=angleVelocity*coef;
        xStartVelocity+=xAccelerate*coef;
        yStartVelocity+=yAccelerate*coef;
        angleVelocity+=angleAccelerate*coef;
    }

    public StaticSprite getStaticSprite() {
        return staticSprite;
    }

    public void setStaticSprite(StaticSprite staticSprite) {
        this.staticSprite = staticSprite;
    }

    public boolean isEnded() {
        return ended;
    }

    public int getType() {
        return type;
    }

    public float getxAccelerate() {
        return xAccelerate;
    }

    public void setxAccelerate(float xAccelerate) {
        this.xAccelerate = xAccelerate;
    }

    public float getyAccelerate() {
        return yAccelerate;
    }

    public void setyAccelerate(float yAccelerate) {
        this.yAccelerate = yAccelerate;
    }

    public float getAngleAccelerate() {
        return angleAccelerate;
    }

    public void setAngleAccelerate(float angleAccelerate) {
        this.angleAccelerate = angleAccelerate;
    }

    @Override
    public String getStringData() {
        System.out.println("No string for this object");
        return "";
    }

    @Override
    public float getWidth() {
        return staticSprite.width;
    }

    @Override
    public float getHeight() {
        return staticSprite.height;
    }

    @Override
    public String getPath() {
        return staticSprite.getPath();
    }
}
