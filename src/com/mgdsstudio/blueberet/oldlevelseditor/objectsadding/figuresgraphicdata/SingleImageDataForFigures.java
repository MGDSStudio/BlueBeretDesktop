package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;

public class SingleImageDataForFigures extends GraphicDataForFigures {
    protected Image image;

    public SingleImageDataForFigures(Image image, int [] vertexes){
        this.image = new Image(image);
        this.vertexes = vertexes;
    }

    public Image getImage() {
        return image;
    }



    /*
    public boolean isSame(SingleImageDataForFigures another){
        boolean same = true;
        if (image.getPath() == another.getImage().getPath()){
            for (byte i = 0 ; i < vertexes.length; i++){
                if (vertexes[i] == another.vertexes[i]){ }
                else {
                    same = false;
                    return same;
                }
            }
        }
        else same = false;
        return same;
    }
*/

}
