package com.mgdsstudio.blueberet.gamelibraries;

import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class RectangularWithAngle {
    public Vec2 [] points = new Vec2[4];

    public RectangularWithAngle(){
        for (int i = 0; i < points.length; i++) points[i] = new Vec2();
        points[0].x = 0;
        points[0].x = 0;
        points[1].x = 1;
        points[1].y = 0;
        points[2].x = 0;
        points[2].y = 1;
        points[3].x = 1;
        points[3].y = 1;
    }

    public RectangularWithAngle(Vec2 center, float angleInRadians, float width, float height){
        points[0] = new Vec2(center.x-(width/2)* PApplet.cos(angleInRadians), center.y-(height/2)* PApplet.cos(angleInRadians));
        points[1] = new Vec2(center.x+(width/2)* PApplet.cos(angleInRadians), center.y-(height/2)* PApplet.cos(angleInRadians));
        points[2] = new Vec2(center.x-(width/2)* PApplet.cos(angleInRadians), center.y+(height/2)* PApplet.cos(angleInRadians));
        points[3] = new Vec2(center.x+(width/2)* PApplet.cos(angleInRadians), center.y+(height/2)* PApplet.cos(angleInRadians));
    }

    public RectangularWithAngle(Vec2 leftUpperCorner, float width, float height){ //angle is 0
        points[0] = new Vec2(leftUpperCorner.x, leftUpperCorner.y);
        points[1] = new Vec2(leftUpperCorner.x+width, leftUpperCorner.y);
        points[2] = new Vec2(leftUpperCorner.x, leftUpperCorner.y+height);
        points[3] = new Vec2(leftUpperCorner.x+width, leftUpperCorner.y+height);
    }

    public void setNewPoints(Vec2 leftUpperCorner, float width, float height){ //angle is 0
        points[0].x = leftUpperCorner.x;
        points[0].x = leftUpperCorner.y;
        points[1].x = leftUpperCorner.x+width;
        points[1].y = leftUpperCorner.y;
        points[2].x = leftUpperCorner.x;
        points[2].y = leftUpperCorner.y+height;
        points[3].x = leftUpperCorner.x+width;
        points[3].y = leftUpperCorner.y+height;
    }


    public void setNewPointsByTwoAnglePositions(float leftX, float upperY, float rightX, float lowerY){ //angle is 0
        points[0].x = leftX;
        points[0].y = upperY;
        points[1].x = rightX;
        points[1].y = upperY;
        points[2].x = leftX;
        points[2].y = lowerY;
        points[3].x = rightX;
        points[3].y = lowerY;
    }

    public void setNewPointsByCenterAndDims(float centerX, float centerY, float width, float height){ //angle is 0
        points[0].x = centerX-width/2f;
        points[0].y = centerY-height/2f;
        points[1].x = centerX+width/2f;
        points[1].y = centerY-height/2f;
        points[2].x = centerX-width/2f;
        points[2].y = centerY+height/2f;
        points[3].x = centerX+width/2f;
        points[3].y = centerY+height/2f;
    }

}
