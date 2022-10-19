package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class MovableOnScreenAnimation extends IndependentOnScreenAnimation{
    private float xStartVelocity, yStartVelocity, xAccelerate, yAccelerate, angleVelocity, angleAccelerate;
    private Timer timerToClose;

    public MovableOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees) {
        super(spriteAnimation, position, angleInDegrees);
    }

    public MovableOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, float xStartVelocity,float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity) {
        super(spriteAnimation, position, angleInDegrees);
        this.xStartVelocity = xStartVelocity;
        this.yStartVelocity =yStartVelocity;
        this.xAccelerate = xAccelerate;
        this.yAccelerate = yAccelerate;
        this.angleVelocity = angleVelocity;
    }

    public MovableOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, float xStartVelocity,float yStartVelocity, float xAccelerate, float yAccelerate, float angleVelocity, int timeToClose) {
        super(spriteAnimation, position, angleInDegrees);
        this.xStartVelocity = xStartVelocity;
        this.yStartVelocity =yStartVelocity;
        this.xAccelerate = xAccelerate;
        this.yAccelerate = yAccelerate;
        this.angleVelocity = angleVelocity;
        timerToClose = new Timer(timeToClose);
    }

    public MovableOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, boolean flip) {
        super(spriteAnimation, position, angleInDegrees, flip);
    }

    @Override
    public void update(){
        super.update();
        updatePositionAndAngle();
        if (timerToClose != null){
            if (timerToClose.isTime()){
                ended = true;
            }
        }
    }

    @Override
    public boolean canBeDeleted(){
        if (ended) System.out.println("Moveable animation can be deleted ");
        return ended;
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

    @Override
    public void draw(GameCamera gameCamera){
        if (!ended) {
            super.draw(gameCamera);
        }
    }

    public void stopMovement() {
        xStartVelocity = 0;
        yStartVelocity = 0;
        yAccelerate = 0;
        xAccelerate = 0;
    }


}
