package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.IBeretColors;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;

public class BeretColorChanger implements IBeretColors {
    private int darkBeretColor = NORMAL_BERET_DARK_COLOR;
    private int brightBeretColor = NORMAL_BERET_BRIGHT_COLOR;
    public static boolean beretColorWasChanged = false;

    public BeretColorChanger(){
        darkBeretColor = NORMAL_BERET_DARK_COLOR;
        brightBeretColor = NORMAL_BERET_BRIGHT_COLOR;
    }

    public BeretColorChanger(TwiceColor sourceColor){
        darkBeretColor = sourceColor.getDarkColor();
        brightBeretColor = sourceColor.getBrightColor();
    }

    public Image changeColor(TwiceColor newColor, Image image){
        int newDark = newColor.getDarkColor();
        int newBright = newColor.getBrightColor();
        image.getImage().loadPixels();
        int [] pixels = image.getImage().pixels;
        int countDark = 0;
        int countBright = 0;
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == darkBeretColor){
                pixels[i] = newDark;
                countDark++;
            }
            else if (pixels[i] == brightBeretColor){
                pixels[i] = newBright;
                countBright++;
            }
        }
        System.out.println("Bright: " + countBright + " dark: " + countDark);
        image.getImage().updatePixels();
        //beretColorWasChanged = true;
        return image;
    }

    public Image changeColor(int sourceColor, int newColor, Image image){
        drawDebugText(sourceColor, newColor);
        image.getImage().loadPixels();
        int pixelsToBeChanged  = 0;
        int [] pixels = image.getImage().pixels;
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == sourceColor){
                pixels[i] = newColor;
                pixelsToBeChanged++;
            }
        }
        image.getImage().updatePixels();
        System.out.println("Founded " + pixelsToBeChanged + " to be changed");
        //beretColorWasChanged = true;
        return image;
    }



    private void drawDebugText(int sourceColor, int newColor) {
        System.out.print("Try to change color from: " + (int)Program.engine.red(sourceColor) + "," + (int)Program.engine.green(sourceColor) + "," + (int)Program.engine.blue(sourceColor));
        System.out.println(" up to color: " + (int)Program.engine.red(newColor) + "," + (int)Program.engine.green(newColor) + "," + (int)Program.engine.blue(newColor));
    }

    public Image changeBeretColor(int newColorDark, int newColorBright, Image image){
        Image image1 = changeColor(darkBeretColor, newColorDark, image);
        //beretColorWasChanged = true;
        return changeColor(brightBeretColor, newColorBright, image1);
    }

    /*
    public void changeColor(int sourceColor, int newColor, Image image){
        image.getImage().loadPixels();
        int [] pixels = image.getImage().pixels;
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == sourceColor){
                System.out.println("Pixel founded! " + i%image.getImage().width + "x" + (PApplet.floor(i/image.getImage().height)));
                pixels[i] = sourceColor;
            }
        }
        image.getImage().updatePixels();
    }*/
}
