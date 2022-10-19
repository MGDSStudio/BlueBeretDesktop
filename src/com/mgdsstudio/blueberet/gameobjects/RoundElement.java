package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamecontrollers.SpringRelaxationController;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;

public abstract class RoundElement extends GameObject implements IDrawable, IStaticSpriteDrawable, ISpriteAnimationDrawable{
    protected boolean secondary;
    protected boolean graphicType;
    protected StaticSprite sprite;
    protected SpriteAnimation spriteAnimation;
    protected int graphicLeftSide, graphicRightSide, graphicUpperSide, graphicLowerSide;


    protected Spring spring;
    protected SpringRelaxationController springRelaxationController;
    protected boolean withSpring;

    public void setAsSecondary(boolean flag){
        secondary = flag;
    }

    public boolean isSecondary(){
        return secondary;
    }

    public void deleteSpring(){
        if (withSpring) {
            if (spring != null) {
                if (spring.mouseJoint != null){
                    PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                    System.out.println("Spring was deleted from the world");
                    spring.mouseJoint = null;
                    spring = null;
                    withSpring = false;
                }
            }

        }
    }

    protected void tintUpdatingBySelecting(){
        if (isSelected()) {
            //System.out.println("Set ting value");
            sprite.setTint(Program.engine.color(255, actualSelectionTintValue));
        }
        if (selectionWasCleared){
            sprite.resetTint();
            //System.out.println("Selection tint is reset");
            selectionWasCleared = false;
        }
    }

    /*
    public void deleteSpring(){
        if (withSpring) {
            try {
                if (spring != null) {
                    if (spring.mouseJoint != null){
                        try {
                            PhysicGameWorld.controller.world.destroyJoint(spring.mouseJoint);
                            System.out.println("Spring was deleted from the world");
                        }
                        catch (Exception e){
                            System.out.println("Can not delete joint from the world");
                        }
                        try{
                            spring.mouseJoint = null;
                        }
                        catch (Exception e){
                            System.out.println("Can not delete joint from the object");
                        }
                        try{
                            spring = null;
                        }
                        catch (Exception e){
                            System.out.println("Can not make spring null");
                        }
                        //spring.gameObject = null;
                        //body.resetMassData();
                        //System.out.println("was: joint");
                    }
                    System.out.println("was: spring ");
                }
                else System.out.println("Problem with the joint deleting");
                //System.out.println("Joint was succesfully deleted ");
            }
            catch (Exception e) {
                System.out.println("Can not delete joint from the world " + e);
            }
            withSpring = false;
            //spring.mouseJoint = null;
            //spring = null;
        }
    }
    */
    public boolean hasSpring() {
        return withSpring;
    }



    public boolean hasGraphic(){
        if (sprite!=null || spriteAnimation != null) return true;
        else return false;
    }

    public boolean getGraphicType(){
        if (sprite != null) return SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        else if (spriteAnimation != null) return SingleGraphicElement.SPRITE_ANIMATION_TYPE;
        else {
            System.out.println("No graphic for " + this.getClass());
            return  false;
        }
    }

    @Override
    public SpriteAnimation getSpriteAnimation(){
        return spriteAnimation;
    }

    @Override
    public StaticSprite getSprite(){
        return sprite;
    }

    @Override
    public void loadSprites(Tileset tilesetUnderPath) {
        sprite.loadSprite(tilesetUnderPath);
    }

    @Override
    public void loadAnimation(Tileset tilesetUnderPath) {
        spriteAnimation.loadAnimation(tilesetUnderPath);
    }

    void updateGraphicSidesData(){
        if (sprite!=null) {
            graphicLeftSide = (int) (getPixelPosition().x - sprite.getWidth() / 2);
            graphicRightSide = (int) (getPixelPosition().x + sprite.getWidth() / 2);
            graphicUpperSide = (int) (getPixelPosition().y - sprite.getHeight() / 2);
            graphicLowerSide = (int) (getPixelPosition().y + sprite.getHeight() / 2);
        }
    }

    @Override
    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height){
        //System.out.println("Sprites was loaded for " + this.getClass());
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height);
    }

    protected boolean testFillAreaValue(int width, int height, boolean fillArea){
        boolean realFillValue = fillArea;
        float graphicMaxDelta = width/height;
        if (height > width) graphicMaxDelta = height/width;
        System.out.println("Delta 1: " + graphicMaxDelta);
        if (fillArea && graphicMaxDelta<2){
            System.out.println("For this block is fill data wrong");
            realFillValue = false;
        }
        return realFillValue;
    }

    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, boolean fillArea){
        boolean realFillValue = testFillAreaValue(width,height,fillArea);
        if (Program.gameStatement == Program.LEVELS_EDITOR){
            if (path == MainGraphicController.WITHOUT_GRAPHIC_STRING){
                path = StaticSprite.BLACK_RECT_PATH;
                System.out.println("Path for round box was set on " + path);
            }
        }
        sprite = new StaticSprite(path, xLeft, yLeft, xRight, yRight, width,  height, realFillValue);
    }

    /*
    public void loadAnimationData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height, byte spritesNumber, int updateFrequency){
        spriteAnimation = new SpriteAnimation(path, xLeft, yLeft, xRight, yRight, width, height, spritesNumber, updateFrequency);
    }*/

    @Override
    public void loadAnimationData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height,  byte rowsNumber, byte columnsNumber, int updateFrequency){
        spriteAnimation = new SpriteAnimation(path, xLeft, yLeft, xRight, yRight, width,  height, rowsNumber, columnsNumber, updateFrequency);
    }



}
