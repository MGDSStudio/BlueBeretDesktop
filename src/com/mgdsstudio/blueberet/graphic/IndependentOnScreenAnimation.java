package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

public class IndependentOnScreenAnimation extends IndependentOnScreenGraphic{
    public static final String CLASS_NAME = "IndependentOnScreenAnimation";
    public SpriteAnimation spriteAnimation;
    private boolean showOnce = false;
    protected boolean ended = false;
    private final String objectToDisplayName = "Animation";

    public IndependentOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees){
        this.spriteAnimation = spriteAnimation;
        this.position = position;
        this.angle = angleInDegrees;
    }

    public IndependentOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, boolean flip){
        this.spriteAnimation = spriteAnimation;
        this.position = position;
        this.angle = angleInDegrees;
        this.flip = flip;
    }

    public IndependentOnScreenAnimation(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, boolean flip, byte layer){
        /*this.spriteAnimation = spriteAnimation;
        this.position = position;
        this.angle = angleInDegrees;
        this.flip = flip;
        this.layer = layer;*/
        init(spriteAnimation, position, angleInDegrees, flip, layer);
    }

    public IndependentOnScreenAnimation(IndependentOnScreenAnimation template){
        /*this.spriteAnimation = template.spriteAnimation;
        this.position = template.position;
        this.angle = template.angle;
        this.flip = template.flip;
        this.layer = template.layer;*/
        init(template.spriteAnimation, template.position, template.angle, template.flip, template.layer);
    }

    private void init(SpriteAnimation spriteAnimation, Vec2 position, float angleInDegrees, boolean flip, byte layer){
        if (spriteAnimation == null) System.out.println("Source sprite animation is null");
        this.spriteAnimation = spriteAnimation;
        this.position = position;
        this.angle = angleInDegrees;
        this.flip = flip;
        this.layer = layer;
    }

    public IndependentOnScreenAnimation(GameObjectDataForStoreInEditor data){
        init(data.getSpriteAnimation(), data.getPosition(), data.getAngle(), flip, data.getLayer());
        System.out.println("Width of the animation: " + data.getSpriteAnimation().getWidth() + ", " + data.getSpriteAnimation().getHeight());
        System.out.println("Rows: " + spriteAnimation.getRowsNumber() + "; Collumns: " + spriteAnimation.getColumnsNumber());
        /*
        this.spriteAnimation = data.getSpriteAnimation();
        this.position = data.getPosition();
        this.angle = data.getAngle();
        this.flip = data.isFlip();
        this.layer = data.getLayer();*/
    }

    public void setShowOnce(boolean flag){
        showOnce = flag;
    }



    public void update(){
        spriteAnimation.update();
    }

    public void draw(GameCamera gameCamera){
        if (!ended) {
            if (showOnce) {
                if (spriteAnimation.getActualSpriteNumber() >= spriteAnimation.getSpritesNumber()-1) {
                    ended = true;
                }
                else {
                    spriteAnimation.tintUpdatingBySelecting(this, actualSelectionTintValue);
                    spriteAnimation.draw(gameCamera, position, angle, flip);
                }
            }
            else {
                spriteAnimation.tintUpdatingBySelecting(this, actualSelectionTintValue);
                spriteAnimation.draw(gameCamera, position, angle, flip);
            }
        }
    }

    public boolean canBeDeleted(){
        if (showOnce == true){
            //System.out.println("Animation is ended : " + ended);
            return ended;
        }
        else return false;
    }

    public boolean ifEnded(){
        return ended;
    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createIndependentSpriteAnimation();
        System.out.println("String for animation: " + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public float getWidth() {
        return spriteAnimation.width;
    }

    @Override
    public float getHeight() {
        return spriteAnimation.height;
    }

    @Override
    public String getPath() {
        return spriteAnimation.getPath();
    }

    public void pause() {
        spriteAnimation.pause();
    }

    public void resume() {
        spriteAnimation.resume();
    }

    public void setCanBeDeleted() {
        canBeDeleted = true;
    }

    public boolean isMustBeShownOnce() {
        return showOnce;
    }
}
