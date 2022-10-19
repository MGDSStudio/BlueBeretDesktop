package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;

public class SingleSpriteAnimationDataForFigures extends GraphicDataForFigures {
    private Animation animation;

    public SingleSpriteAnimationDataForFigures(Animation animation, int [] vertexes){
        this.animation = new Animation(animation);
        this.animation.setLeftX(vertexes[0]);
        this.animation.setUpperY(vertexes[1]);
        this.animation.setRightX(vertexes[2]);
        this.animation.setLowerY(vertexes[3]);
        this.vertexes = vertexes;
    }

    public Animation getAnimation() {
        return animation;
    }
}
