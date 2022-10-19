package com.mgdsstudio.blueberet.menusystem.gui;

public class ColorWithName {
    public final static int RED = 0;
    public final static int YELLOW = 1;
    public final static int WHITE = 2;
    private int red, green, blue;

    public ColorWithName(int name) {
        if (name == RED){
            red = 255;
            green = 0;
            blue = 0;
        }
        else if (name == YELLOW){
            red = 255;
            green = 255;
            blue = 0;
        }
        else {
            red = 255;
            green = 255;
            blue = 255;
        }
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
