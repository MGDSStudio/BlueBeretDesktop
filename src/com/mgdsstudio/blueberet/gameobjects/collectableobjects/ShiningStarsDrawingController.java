package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class ShiningStarsDrawingController {
    private SpriteAnimation animation;
    private AbstractCollectable object;
    private float actualShiftingX, actualShiftingY;
    private Vec2 actualStarPos;

    public ShiningStarsDrawingController(AbstractCollectable object){
        this.object = object;
        animation = new SpriteAnimation(InWorldObjectsGraphicData.stars);
        actualStarPos = new Vec2(object.getPixelPosition().x, object.getPixelPosition().y);
        resetStarPosition();
        System.out.println("Source is null : " + (InWorldObjectsGraphicData.mainGraphicTileset == null));
        if (InWorldObjectsGraphicData.mainGraphicTileset == null){
            InWorldObjectsGraphicData.mainGraphicTileset = HeadsUpDisplay.mainGraphicTileset;
        }
        int sprites = animation.getSpritesNumber();
        int randomStartSprite = (int) Program.engine.random(sprites);
        animation.setActualSpriteNumber((byte)randomStartSprite);
        animation.loadAnimation(InWorldObjectsGraphicData.mainGraphicTileset);
    }

    public void loadAnimation(Tileset tileset){
        animation.loadAnimation(tileset);
    }

    public void update(){
        if (animation.isAnimationAlreadyShown()){
            resetStarPosition();
        }
        actualStarPos.x = object.getPixelPosition().x+actualShiftingX;
        actualStarPos.y = object.getPixelPosition().y+actualShiftingY;
        //System.out.println("Star pos updated to " + actualStarPos.x + "x" + actualStarPos.y);
    }

    private void resetStarPosition() {
        actualShiftingX = Program.engine.random(-object.getWidth()/2f, object.getWidth()/2f);
        actualShiftingY = Program.engine.random(-object.getHeight()/2f, object.getHeight()/2f);
        animation.reset();
        if (InWorldObjectsGraphicData.mainGraphicTileset == null){
            InWorldObjectsGraphicData.mainGraphicTileset = HeadsUpDisplay.mainGraphicTileset;
            System.out.println("Upload again");
            animation.loadAnimation(InWorldObjectsGraphicData.mainGraphicTileset);
            animation.setTileset(HeadsUpDisplay.mainGraphicTileset);
            InWorldObjectsGraphicData.mainGraphicTileset = new Tileset(HUD_GraphicData.mainGraphicFile);
        }

    }

    public void draw(GameCamera gameCamera){
        //System.out.println("Stars were drawn");
        animation.update();
        animation.draw(gameCamera, actualStarPos, object.body.getAngle(), false);

    }

}
