package com.mgdsstudio.blueberet.gameprocess.control;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class MovementStick extends StickAbstract{
    private static Image stickHole;
    private static ImageZoneSimpleData stickHoleZoneSimpleData;

    /*
    public static int STICK_DEATH_ZONE_RADIUS;
    public static int STICK_RUN_ZONE_RADIUS;
    public final static boolean STICK_IS_TOUCHED_IN_AIMING_ZONE = true;

     */

    final int MAX_DISTANCE_FOR_ONE_FINGER_TOUCH = (int) Program.engine.width/7;


    private int aimingZoneRadius;
    private int goZoneRadius;
    private int maxZoneRadius;
    private int maxLeftRightMovementZoneHeight;

    private int holeVisibleDiameter;
    private int distanceFromHoleToStick;

    public MovementStick (int x, int y, int diameter) {
        calculateDimensions(diameter);
        if (stickHole == null) {
            stickHole = HeadsUpDisplay.mainGraphicSource;
            stickHoleZoneSimpleData = HUD_GraphicData.stickHole;
        }
        if (stickPicture == null) {
            loadStickPictureData();
        }
        stickVisibleDiameter = (int)(diameter/2.8f);
        holeVisibleDiameter = diameter;

        actualPosition = new Vec2(x, y);
        basicPosition = new Vec2(x, y);
        visibilityStatement = true;
        //STICK_DEATH_ZONE_RADIUS = STICK_AIMING_ZONE;
        //STICK_RUN_ZONE_RADIUS = WALK_TO_RUN_JOYSTICK_TRANSITION_VALUE;

    }



    protected void calculateDimensions(int diameter){
        int radius = diameter/2;
        float relativeDeadZoneRadius = 0.03f;
        float relativeAimingZoneRadius = 0.61f;
        float relativeGoZoneRadius = 0.83f;
        //float relativeRunZoneRadius = 1f;
        deadZoneRadius = (int)(radius*relativeDeadZoneRadius);
        aimingZoneRadius = (int)(radius*relativeAimingZoneRadius);;
        goZoneRadius  = (int)(radius*relativeGoZoneRadius);
        maxZoneRadius  = radius;
        maxLeftRightMovementZoneHeight = radius;
        System.out.println("Radiuses: " + deadZoneRadius + "x" + aimingZoneRadius + "x" + goZoneRadius + "; Max: " + maxZoneRadius);

    }

    public void update(){
        if (Program.OS == Program.ANDROID){
            updateStatementForAndroidMode();
        }
    }

    private Vec2 getCenterTouchPosition() {
        if (Program.OS == Program.DESKTOP) {
            return new Vec2(Program.engine.mouseX, Program.engine.mouseY);
        }
        else if (Program.OS == Program.ANDROID) {
            ArrayList<PVector> onStickTouches = new ArrayList<PVector>();
            for (int i = 0; i < Program.engine.touches.length; i++) {
                if (GameMechanics.isPointInCircle(Program.engine.touches[i].x, Program.engine.touches[i].y, basicPosition.x, basicPosition.y, maxZoneRadius*2)){
                    onStickTouches.add(new PVector(Program.engine.touches[i].x, Program.engine.touches[i].y));
                }
            }
            float x = 0;
            float y = 0;
            for (int j = 0; j < onStickTouches.size(); j++) {
                x+=onStickTouches.get(j).x;
                y+=onStickTouches.get(j).y;
            }
            x/=onStickTouches.size();
            y/=onStickTouches.size();
            return new Vec2(x,y);
        }
        else {
            System.out.println("There are no data about OS for this stick control");
            return null;
        }
    }

    public void updateStatementForAndroidMode() {
        /*
        float relativeDeadZoneRadius = 0.03f;
        float relativeAimingZoneRadius = 0.4f;
        float relativeGoZoneRadius = 0.7f;
        //float relativeRunZoneRadius = 1f;
        deadZoneRadius = (int)(radius*relativeDeadZoneRadius);
        aimingZoneRadius = (int)(radius*relativeAimingZoneRadius);;
        goZoneRadius  = (int)(radius*relativeGoZoneRadius);
        */
        Vec2 touchesCenter = getCenterTouchPosition();
        if (PApplet.dist(touchesCenter.x, touchesCenter.y, basicPosition.x, basicPosition.y) <= deadZoneRadius) {
            actualZone = IN_DEAD_ZONE;
        }
        else if (PApplet.dist(touchesCenter.x, touchesCenter.y, basicPosition.x, basicPosition.y) <= aimingZoneRadius) {
            actualZone = IN_AIMING_ZONE;
            updateAngle(touchesCenter);
        }
        else if (PApplet.dist(touchesCenter.x, touchesCenter.y, basicPosition.x, basicPosition.y) <= goZoneRadius) {
            actualZone = IN_GO_ZONE;
            updateAngle(touchesCenter);
        }
        else if (PApplet.dist(touchesCenter.x, touchesCenter.y, basicPosition.x, basicPosition.y) <= maxZoneRadius) {
            actualZone = IN_RUN_ZONE;
            updateAngle(touchesCenter);
        }
        else actualZone = IN_DEAD_ZONE;
        updateStickPos(touchesCenter);
        updateStickInMovementZonePosition(touchesCenter);
    }



    protected void updateAngle(Vec2 touchesCenter) {
        angleToClickPlace = PApplet.parseInt(GameMechanics.angleDetermining((int)basicPosition.x, (int)basicPosition.y, touchesCenter.x, touchesCenter.y));
        angleToClickPlace = PApplet.parseInt(GameMechanics.convertAngleFromSimetricalScaleInto0_to_360Scale(angleToClickPlace));
        if (angleToClickPlace > 360) {
            while (angleToClickPlace >360) {
                angleToClickPlace-=360;
            }
        }
        else if (angleToClickPlace < 0){
            while (angleToClickPlace<0){
                angleToClickPlace+=360;
            }
        }
    }

    private void updateStickInMovementZonePosition(Vec2 touchesCenter){
        if (touchesCenter.y>=(basicPosition.y-maxLeftRightMovementZoneHeight/2) && touchesCenter.y<=(basicPosition.y+maxLeftRightMovementZoneHeight/2)){
            inMovementZone = true;
        }
        else inMovementZone = false;
    }


    public void draw(PGraphics graphics) {
        if (visibilityStatement == true) {

            graphics.image(stickHole.getImage(), basicPosition.x, basicPosition.y, holeVisibleDiameter, holeVisibleDiameter, stickHoleZoneSimpleData.leftX, stickHoleZoneSimpleData.upperY, stickHoleZoneSimpleData.rightX, stickHoleZoneSimpleData.lowerY);
            graphics.image(stickPicture.getImage(), actualPosition.x, actualPosition.y, stickVisibleDiameter, stickVisibleDiameter, stickZoneSimpleData.leftX, stickZoneSimpleData.upperY, stickZoneSimpleData.rightX, stickZoneSimpleData.lowerY);


        }
    }


    /*
    public boolean getTouchedStatement(){
        if (isStickTouched() == true){  // it was through mouse pressed variable
            if (distanceFromHoleToStick > STICK_AIMING_ZONE && distanceFromHoleToStick < STICK_MAX_ZONE_RADIUS){
                return STICK_IS_TOUCHED_IN_AIMING_ZONE;
            }
            else return false;
        }
        else return false;
    }
    */

    public void resetStickPosition(){
        actualPosition.x = basicPosition.x;
        actualPosition.y = basicPosition.y;
        distanceFromHoleToStick = 0;
    }





    /*
    @Override
    public boolean getRelativeSide() {
        System.out.println("Actual angle " + angleToClickPlace);
        if (angleToClickPlace > 270 || angleToClickPlace < 90) return Person.TO_RIGHT;
        else return Person.TO_LEFT;
    }*/
}
