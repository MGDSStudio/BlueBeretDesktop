package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;

public interface IStaticSpriteDrawable {

    public StaticSprite getSprite();


    public void loadSprites(Tileset tilesetUnderPath);


    public void loadImageData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height);

}
