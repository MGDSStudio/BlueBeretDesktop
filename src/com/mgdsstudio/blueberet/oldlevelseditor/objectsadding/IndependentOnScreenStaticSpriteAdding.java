package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import org.jbox2d.common.Vec2;

public class IndependentOnScreenStaticSpriteAdding extends ObjectWithSetableFormAdding{
    private IndependentOnScreenStaticSprite independentOnScreenSprite;
    //private RoundBox roundBox;
    //private float angle = 0;
    //private boolean withSpring = false;
    //private int life = GameObject.IMMORTALY_LIFE;
    //private BodyType bodyType = BodyType.STATIC;


    public final static byte NEW_OR_EXISTING_GRAPHIC = 3;
    public final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = 4;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 5;
    public final static byte FILL_OR_STRING_TEXTURE = 6;
    public final static byte TEXTURE_REGION_CHOOSING = 7;
    public final static byte COMPLETED = 8;
    public final static byte END = COMPLETED;

    public IndependentOnScreenStaticSpriteAdding(){
        endStatement = END;
    }




    public IndependentOnScreenStaticSprite getIndependentOnScreenStaticSprite(){
        /*roundBox = new RoundBox(getCenterPosition(firstPoint, secondPoint), angle, getWidth(firstPoint, secondPoint), getHeight(firstPoint, secondPoint), life, withSpring, bodyType);
        if (roundBox == null ) System.out.println("Created");
        return roundBox;*/
        return null;
    }

    public void addGraphicData(String path, int [] graphicData) {
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
        Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
        int width = (int) graphicData[4];
        int height = (int) graphicData[5];
        boolean fillArea = true;
        if (graphicData[6] == 0) fillArea = false;
        //roundBox.loadImageData(path, (int)leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, fillArea);
    }


}
