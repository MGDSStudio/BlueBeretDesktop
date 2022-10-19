package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

public class IndependentOnScreenStaticSprite extends IndependentOnScreenGraphic{
    public static final String CLASS_NAME = "IndependentOnScreenStaticSprite";
    private final String objectToDisplayName = "Picture";
    public StaticSprite staticSprite;

    public IndependentOnScreenStaticSprite(StaticSprite staticSprite, Vec2 position, float angleInDegrees){
        this.staticSprite = staticSprite;
        this.position = position;
        this.angle = angleInDegrees;
    }

    public IndependentOnScreenStaticSprite(StaticSprite staticSprite, Vec2 position, float angleInDegrees, boolean flip){
        this.staticSprite = staticSprite;
        this.position = position;
        this.angle = angleInDegrees;
        this.flip = flip;
    }

    public IndependentOnScreenStaticSprite(StaticSprite staticSprite, Vec2 position, float angleInDegrees, boolean flip, byte layer){
        this.staticSprite = staticSprite;
        this.position = position;
        this.angle = angleInDegrees;
        this.flip = flip;
        this.layer = layer;
    }

    public IndependentOnScreenStaticSprite(IndependentOnScreenStaticSprite template){
        this.staticSprite = template.staticSprite;
        this.position = template.position;
        this.angle = template.angle;
        this.flip = template.flip;
        this.layer = template.layer;
    }


    public IndependentOnScreenStaticSprite(GameObjectDataForStoreInEditor data){
        this.staticSprite = data.getStaticSprite();
        this.position = data.getPosition();
        this.angle = data.getAngle();
        boolean fill = false;
        if (data.getFill() != 0) fill = true;
        System.out.println("Next string was not testet yet !");
        //staticSprite.fillAreaWithSprite = fill;
        this.flip = data.isFlip();
        this.layer = data.getLayer();
    }


    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createIndependentStaticSprite();
        return saveMaster.getDataString();
    }

    public void draw(GameCamera gameCamera){
        staticSprite.tintUpdatingBySelecting(this, actualSelectionTintValue);
        staticSprite.draw(gameCamera, position, Program.engine.radians(angle), flip);
    }

    public float getWidth(){
        return staticSprite.getParentElementWidth();
    }

    public float getHeight(){
        return staticSprite.getParentElementHeight();
    }

    public float getChildWidth(){
        return staticSprite.getWidth();
    }

    public float getChildHeight(){
        return staticSprite.getHeight();
    }

    public String getPath() {
        return staticSprite.path;
    }






    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    public boolean getFill(){
        if (staticSprite == null) System.out.println("Static sprite in independed sprite is null");
        return staticSprite.getFill();
    }

    public boolean getFlip(){
        return flip;
    }

    protected void tintUpdatingBySelecting() {
        /*
            if (isSelected()) {
                //System.out.println("Set ting value");
                staticSprite.setTint(Programm.engine.color(255, actualSelectionTintValue));
            }
            if (selectionWasCleared){
                staticSprite.resetTint();
                System.out.println("Selection tint is reset");
                selectionWasCleared = false;
            }*/
    }

    public void setWidth(int width){
        if (staticSprite.getFill() == true){
            System.out.println("This sprite has fill. Width was " + staticSprite.getWidth() + "; parent width was " + staticSprite.parentElementWidth);
            staticSprite.parentElementWidth = width;
            staticSprite.recalculateChildDimensions();
        }
        else {
            System.out.println("This sprite has no fill");
            staticSprite.setWidth(width);
            staticSprite.parentElementWidth = width;
        }
    }

    public void setHeight(int height){
        if (staticSprite.getFill() == true){
            System.out.println("This sprite has fill. Height was " + staticSprite.getHeight() + "; parent height was " + staticSprite.parentElementHeight);
            staticSprite.parentElementHeight = height;
            staticSprite.recalculateChildDimensions();
        }
        else {
            System.out.println("This sprite has no fill");
            staticSprite.setHeight(height);
            staticSprite.parentElementHeight = height;
        }
    }

    /*
    public void setHeight(int height){
        staticSprite.setHeight(height);
        staticSprite.parentElementHeight = height;
    }*/

    public void setParentWidth(int width){
        staticSprite.parentElementWidth = width;
    }

    public void setParentHeight(int height){
        staticSprite.parentElementHeight = height;
    }

    public void setChildWidth(int width){
        staticSprite.setWidth(width);
    }

    public void setChildHeight(int height){
        staticSprite.setHeight(height);
    }

}
