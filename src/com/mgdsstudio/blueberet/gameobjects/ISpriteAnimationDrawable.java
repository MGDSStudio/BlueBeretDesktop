package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.graphic.Tileset;

public interface ISpriteAnimationDrawable {

    public SpriteAnimation getSpriteAnimation();

    public void loadAnimation(Tileset tilesetUnderPath);

    public void loadAnimationData(String path, int xLeft, int yLeft, int xRight, int yRight, int width, int height,  byte rowsNumber, byte columnsNumber, int updateFrequency);


}
