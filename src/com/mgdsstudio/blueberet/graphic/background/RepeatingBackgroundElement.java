package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class RepeatingBackgroundElement extends Background{
    private final String objectToDisplayName = "Single background element";
    private StaticSprite sprite;
    private SpriteAnimation animation;
    private int xStep, yStep;
    private Vec2 pos;
    private int angle;
    private boolean graphicType;
    private float relativeVelocity;

    public RepeatingBackgroundElement(SingleGraphicElement singleGraphicElement, Vec2 pos, int xStep, int yStep, int angle, float relativeVelocity) {
        this.xStep = xStep;
        this.yStep = 0;
        this.pos = pos;
        this.leftUpperX = pos.x-singleGraphicElement.getWidth()/2;
        this.leftUpperY = pos.y-singleGraphicElement.getHeight()/2;
        /*
        this.pos = pos;
        this.leftUpperX = pos.x-singleGraphicElement.getWidth()/2;
        this.leftUpperY = pos.y-singleGraphicElement.getHeight()/2;

         */
        this.angle = angle;
        this.relativeVelocity = relativeVelocity;
        if (singleGraphicElement.getClass() == StaticSprite.class){
            graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
            sprite = (StaticSprite) singleGraphicElement;
        }
        else {
            graphicType = SingleGraphicElement.SPRITE_ANIMATION_TYPE;
            animation = (SpriteAnimation) singleGraphicElement;
        }
    }

    public RepeatingBackgroundElement (GameObjectDataForStoreInEditor data){
        this.xStep = data.getStep();
        this.yStep = xStep;
        this.pos = data.getPosition();
        this.angle = 0;
        this.relativeVelocity = data.getRelativeVelocity();
        //SingleGraphicElement graphicElement = data.getGraphicElement();
        SingleGraphicElement graphicElement = data.getStaticSprite();
        if (graphicElement.getClass() == StaticSprite.class){
            graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
            sprite = (StaticSprite) graphicElement;
        }
        else {
            graphicType = SingleGraphicElement.SPRITE_ANIMATION_TYPE;
            animation = (SpriteAnimation) graphicElement;
        }
    }

    @Override
    public String getPath(){
        return sprite.getPath();
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createBackground();
        //System.out.println("Data string for background: " + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public void loadGraphic(Tileset tileset){
        sprite.setTileset(tileset);
    }


    private void drawSprite(GameCamera gameCamera){
        int startBackgroundNumber = PApplet.floor((relativeVelocity*(gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)- sprite.getWidth()/2)/xStep)-2);
        int endBackgroundsNumber = PApplet.ceil((relativeVelocity*(gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)+ sprite.getWidth()/2)/ 1 /xStep)+2);
        for (int i = startBackgroundNumber; i < (endBackgroundsNumber); i++) {
            //System.out.println("Drawing" + sprite.getPath());
            //System.out.println("Sprite "+i+" is drawn on " + (pos.x+(i*xStep)) +"x"+ pos.y + " with size: " + sprite.getWidth() + "x" + sprite.getHeight());
            sprite.draw(gameCamera, pos.x+(i*xStep), pos.y, angle, relativeVelocity, Program.objectsFrame);
        }
        //if (sprite.getTileset() == null) System.out.print("tileset is null; ");
        //System.out.println();
    }

    private void drawAnimation(GameCamera gameCamera){
        int startBackgroundNumber = PApplet.floor((relativeVelocity*(gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)- animation.getWidth()/2)/ 1 /xStep)-2f);
        int endBackgroundsNumber = PApplet.ceil((relativeVelocity*(gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)+ animation.getWidth()/2)/ 1 /xStep)+2f);
        animation.update();
        for (int i = startBackgroundNumber; i < (endBackgroundsNumber); i++) {
            animation.draw(gameCamera, pos.x+(i*xStep), pos.y, angle, relativeVelocity);
        }
    }

    @Override
    public void setWidth(int value){
        sprite.setWidth((int)value);
        //picture.getImage().resize(width, height);
        //updatePictureData();
    }

    @Override
    public void setHeight(int value) {
        sprite.setHeight((int)value);
        //picture.getImage().resize(width, height);
        //updatePictureData();
    }

    @Override
    public void setRelativeVelocity(float value) {
        relativeVelocity = value;
    }

    @Override
    public void shift(float xShifting, float yShifting){
        pos.y+=yShifting;
        pos.x+=xShifting;
        leftUpperX+=xShifting;
        leftUpperY+=yShifting;
    }

    @Override
    public void draw(GameCamera gameCamera) {
        //System.out.println("try to draw ");
        if (!hide) {
            //System.out.println(" draw ");
            if (graphicType == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) {
                //System.out.println("Sprite drawing");
                drawSprite(gameCamera);
                //System.out.println("Sprite is drawn");
                //System.out.println(" succesfully");
            }
            else {
                drawAnimation(gameCamera);
                //System.out.println("Animation is drawn");
            }
        }
        else {
            //System.out.println("Background is hidden");
        }
    }

    public byte getType(){
        return REPEATING_BACKGROUND_ELEMENTS;
    }

    public String getObjectToDisplayName(){
        return objectToDisplayName;
    }

    public void setStep(int step){
        this.xStep = step;
    }

    public Vec2 getPos() {
        return pos;
    }

    public StaticSprite getSprite() {
        return sprite;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isGraphicType() {
        return graphicType;
    }

    public float getRelativeVelocity() {
        return relativeVelocity;
    }

    public int getStep(){
        return xStep;
    }
}
