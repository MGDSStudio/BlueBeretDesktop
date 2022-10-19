package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamecontrollers.GameCameraController;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.engine.nesgui.Frame;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

public abstract class AbstractCamera {
    public static final byte RIGHT_SIDE = 1;
    public static final byte LEFT_SIDE = 2;
    public static final byte UPPER_SIDE = 3;
    public static final byte LOWER_SIDE = 4;
    public final static boolean CAMERA_TO_OBJECT = true;
    public final static boolean CAMERA_TO_AIM_PLACE = false;

    protected GameCameraController gameCameraController;
    protected Box2DProcessing cameraWorldController;
    public final static String CAMERA_BODY_NAME = "Camera";
    protected Body actualCameraPositionBody;
    protected boolean inSomeCameraFixationZone;
    private final float brakingDistance = 1f;
    private final float brakingCoef = 0.0005f;
    private final float movementCoef = 0.15f;
    public final boolean MOVEMENT_WITH_ACCELERATE = true;
    protected final PVector mutMovementVector = new PVector();

    protected float scale;

    protected boolean springCameraMovement = true;

    protected CameraSpring cameraSpring;

    protected PVector actualPosition, goalPosition;
    protected Vec2 cameraCenterPositionInEditor;
    protected float visibleZoneWidth = Program.objectsFrame.width/Program.OBJECT_FRAME_SCALE, visibleZoneHeight = Program.objectsFrame.height/Program.OBJECT_FRAME_SCALE;

    public static Vec2 lastCameraPositionInEditor;

    public static float minScale = 0.85f* (float)Program.engine.width/ (float)Program.XIAOMI_REDMI_WIDTH*1.2f;
    protected static float diferenceBetweenMaxAndMinScale = 0.25f;
    public static float maxScale = minScale+diferenceBetweenMaxAndMinScale;
    public static float minScaleInEditorMode = minScale-0.05f;
    public static float maxScaleInEditorMode = maxScale+diferenceBetweenMaxAndMinScale/3;


    protected void initScales(){
        minScale = 0.85f* (float)Program.engine.width/ (float)Program.XIAOMI_REDMI_WIDTH*1.2f;
        diferenceBetweenMaxAndMinScale = 0.25f;
        maxScale = minScale+diferenceBetweenMaxAndMinScale;
        minScaleInEditorMode = minScale-0.05f;
        maxScaleInEditorMode = maxScale+diferenceBetweenMaxAndMinScale/3;
    }




    public boolean isInSomeCameraFixationZone() {
        return inSomeCameraFixationZone;
    }

    public void setInSomeCameraFixationZone(boolean inSomeCameraFixationZone) {
        this.inSomeCameraFixationZone = inSomeCameraFixationZone;
    }

    public void setController(GameCameraController gameCameraController) {
        this.gameCameraController = gameCameraController;
    }

    public GameCameraController getGameCameraController() {
        return gameCameraController;
    }

    public void brakeTranslatingInEditor(){
        if (MOVEMENT_WITH_ACCELERATE){
            mutMovementVector.x = 0;
            mutMovementVector.y = 0;
        }
    }

    public void addMovementVector(float x, float y , float prevX, float prevY){
        float deltaX = -(x-prevX);
        float deltaY = -(y-prevY);
        mutMovementVector.x+= (deltaX*movementCoef);
        mutMovementVector.y+= (deltaY*movementCoef);
    }

    public void updateInEditorMovement() {
        if (MOVEMENT_WITH_ACCELERATE){
            if (mutMovementVector.x == 0 && mutMovementVector.y == 0){

            }
            else {
                float braking = brakingCoef*Program.deltaTime;
                if (braking > 0) braking = 0.1f;
                mutMovementVector.mult(1-braking);
                if (mutMovementVector.mag() <= brakingDistance){
                    mutMovementVector.x = 0;
                    mutMovementVector.y = 0;
                }
                else {
                    translate(mutMovementVector);
                }
            }
        }
    }

    public void translate(PVector shifting) {
        actualPosition.x+= shifting.x;
        actualPosition.y+= shifting.y;
        updateActualRelativeToCenterPos();
    }

    protected abstract void updateActualRelativeToCenterPos();


    protected void makeBody() {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(cameraWorldController.coordPixelsToWorld(actualPosition));
        actualCameraPositionBody = cameraWorldController.createBody(bd);
        CircleShape sd = new CircleShape();
        sd.m_radius = cameraWorldController.scalarPixelsToWorld(2.001f);
        sd.m_p.set(0,0);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 2f;
        fd.friction = 0.01f;
        fd.restitution = 0.1f;
        fd.setSensor(true);
        try {
            setCameraBodyParameters(fd);
        }
        catch (Exception e){
            System.out.println("Can not create camera body; " + e);
            System.out.println("Try again; ");
            if (Program.gameStatement == Program.GAME_PROCESS) {
                makeBody();
            }
        }
        actualCameraPositionBody.setUserData(CAMERA_BODY_NAME);
    }

    private void setCameraBodyParameters(FixtureDef fd){
        actualCameraPositionBody.createFixture(fd);
        actualCameraPositionBody.setGravityScale(0);
    }

    protected void setFilterDataForCategory(int category){
        for (Fixture f = actualCameraPositionBody.getFixtureList(); f!=null; f=f.getNext()) {
            f.m_filter.categoryBits = category;
            f.m_filter.maskBits = CollisionFilterCreator.getMaskForCategory(category);
        }
    }

    public float getScale(){
        return scale;
    }

    public boolean isRelaxed() {
        if (cameraSpring != null){
            return cameraSpring.relaxed;
        }
        else return false;
    }

    public void makeCameraMovementWithoutSpring(){
        springCameraMovement = false;
    }



    public PVector getActualPosition() {
        return actualPosition;
    }

    public abstract void changeScale(byte scallingDirection);
    public float getVisibleZoneWidth() {
        return visibleZoneWidth;
    }

    public float getVisibleZoneHeight() {
        return visibleZoneHeight;
    }

    public void saveCameraPos(){
        if (lastCameraPositionInEditor== null) {
            lastCameraPositionInEditor = new Vec2();
        }
        lastCameraPositionInEditor.x = actualPosition.x;
        lastCameraPositionInEditor.y = actualPosition.y;
    }

    public void updateScaleForLevelseditor(float scaleValue) {
        scale+=scaleValue;
        if (Program.gameStatement == Program.LEVELS_EDITOR) {
            if (scale > maxScaleInEditorMode) scale = maxScaleInEditorMode;
            else if (scale < minScaleInEditorMode) scale = minScaleInEditorMode;
        }
        else if (Program.gameStatement == Program.GAME_PROCESS){
            if (scale > maxScale) scale = maxScale;
            else if (scale < minScale) scale = minScale;
        }
    }

    public void updateCenterPositionInEditor(Frame mapZone, PApplet engine) {
        //cameraCenterPositionInEditor.y= actualPosition.y-mapZone.getHeight()/(2*scale);
        float additionalY = -mapZone.getUpperY()/scale-(mapZone.getHeight()/2)/scale;
        //System.out.println("Additional: " + additionalY);
        cameraCenterPositionInEditor.y= actualPosition.y+additionalY;
        //cameraCenterPositionInEditor.x = actualPosition.x-(((engine.width-(mapZone.getLeftX()+mapZone.getWidth()))+mapZone.getLeftX())/2)/scale;
        cameraCenterPositionInEditor.x = actualPosition.x-(mapZone.getLeftX())/(2*scale);

    }

    public Vec2 getActualPositionForScreenCenterInEditor() {
        return cameraCenterPositionInEditor;
    }

}
