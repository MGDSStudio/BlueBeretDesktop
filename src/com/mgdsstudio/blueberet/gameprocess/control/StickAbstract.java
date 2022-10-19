package com.mgdsstudio.blueberet.gameprocess.control;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

public abstract class StickAbstract {
    protected static Image stickPicture;
    protected static ImageZoneSimpleData stickZoneSimpleData;
    public final static byte IN_DEAD_ZONE = 1;
    public final static byte IN_AIMING_ZONE = 2;
    public final static byte IN_GO_ZONE = 3;
    public final static byte IN_RUN_ZONE = 4;
    protected byte actualZone = IN_DEAD_ZONE;
    public final static byte IN_LEFT_GO_ZONE = 5;
    public final static byte IN_LEFT_RUN_ZONE = 6;
    public final static byte IN_RIGHT_GO_ZONE = 7;
    public final static byte IN_RIGHT_RUN_ZONE = 8;
    protected boolean inMovementZone;
    protected Vec2 actualPosition, basicPosition;
    public final static boolean VISIBLE = OnScreenButton.VISIBLE;
    protected boolean visibilityStatement = VISIBLE;
    protected boolean previousVisibilityStatement = false;
    protected int stickVisibleDiameter;
    protected int angleToClickPlace;
    protected int deadZoneRadius;

    protected void updateStickPos(Vec2 touchesCenter) {
        if (actualZone == IN_DEAD_ZONE) {
            actualPosition.x = basicPosition.x;
            actualPosition.y = basicPosition.y;
        }
        else {
            actualPosition.x = touchesCenter.x;
            actualPosition.y = touchesCenter.y;
        }
        //distanceFromHoleToStick = 0;
    }

    public byte getActualZone(){
        return actualZone;
    }

    protected void loadStickPictureData(){
        stickPicture = HeadsUpDisplay.mainGraphicSource;
        stickZoneSimpleData = HUD_GraphicData.stick;
    }

    public boolean getRelativeSide() {

        if (angleToClickPlace > 270 || angleToClickPlace < 90) return Person.TO_RIGHT;
        else return Person.TO_LEFT;
    }

    protected abstract void updateAngle(Vec2 touchesCenter);

    public boolean isInRightLeftMovementZone(){
        return inMovementZone;
    }

    public float getAngle(){
        //System.out.println("Actual angle " + angleToClickPlace);
        return angleToClickPlace;
    }

    public abstract  void draw(PGraphics graphics);

    public void setVisibility(boolean visible){
        this.visibilityStatement = visible;
    }


}
