package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PApplet;
import processing.core.PConstants;

public class MultiplePicturesBackground extends Background implements IDrawable {

    private Image[]pictures;

    public MultiplePicturesBackground (String path, String format, byte backgroundsAlongY, int width, int height, int leftUpperY, boolean moveable){
        pictures = new Image [backgroundsAlongY];
        for (byte i = 0; i < backgroundsAlongY; i++){
            Image image = new Image(path + i + format);
            pictures[i] = image;
            if (pictures[i] != null) {
                pictures[i].getImage().resize(width, height);
            }
            else {
                System.out.println("Background " + path + i + format + " does not found");
                image = new Image("BlackRect.png");
                pictures[i] = image;
                pictures[i].getImage().resize(width, height);
            }
        }
        this.moveable = moveable;
        this.leftUpperY = leftUpperY;
    }

    public void draw(GameCamera gameCamera){
        if (Program.WITH_GRAPHIC ) {
            if (!moveable) {
                int backgroundsAlongX = PApplet.ceil(Program.engine.width/gameCamera.getScale())/pictures[0].getImage().width;
                Program.objectsFrame.beginDraw();
                Program.objectsFrame.pushStyle();
                Program.objectsFrame.imageMode(PConstants.CORNER);
                Program.objectsFrame.pushMatrix();
                int startBackgroundAlongXNumber = PApplet.floor((gameCamera.getActualPosition().x- Program.engine.width/2)/pictures[0].getImage().width);
                Program.objectsFrame.translate((startBackgroundAlongXNumber*pictures[0].getImage().width-gameCamera.getActualPosition().x- Program.engine.width/2), 0);
                for (int j = 0; j < backgroundsAlongX; j++) {
                    for (byte i = 0; i < pictures.length; i++) {
                        Program.objectsFrame.image(pictures[i].getImage(), j*pictures[i].getImage().width, leftUpperY + pictures[i].getImage().height);
                    }

                }
                Program.objectsFrame.popMatrix();
                Program.objectsFrame.imageMode(PConstants.CENTER);
                Program.objectsFrame.popStyle();
                Program.objectsFrame.endDraw();
            }
        }
    }

    @Override
    public String getStringData() {
        return null;
    }

    @Override
    public byte getType() {
        return 0;
    }
}
