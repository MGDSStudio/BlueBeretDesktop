package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;

public class TextureData {
    private Image image;
    private String path;
    private int[] positions;
    private byte fill;

    public TextureData(Image image, int[] positions, boolean fill){
        this.image = image;
        this.path = image.getPath();
        this.positions = positions;
    }
}
