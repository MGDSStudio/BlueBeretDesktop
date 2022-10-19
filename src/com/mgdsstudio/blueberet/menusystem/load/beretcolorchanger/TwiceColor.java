package com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger;

import com.mgdsstudio.blueberet.mainpackage.Program;

public class TwiceColor implements IBeretColors {

    /*
    public static int NORMAL_BERET_BRIGHT_COLOR = Program.engine.color(54,71,121);
    public static int NORMAL_BERET_DARK_COLOR = Program.engine.color(59,123,206);
    */
    private String name;
    private int brightColor;
    private int darkColor;

    public TwiceColor(int brightColor, int darkColor) {
        this.brightColor = brightColor;
        this.darkColor = darkColor;
    }

    public TwiceColor(int midColor) {
        this.brightColor = brightColor;
        this.darkColor = darkColor;
    }

    public TwiceColor(String colorName) {
        this.name = colorName;
        updateColorDataByColorName(name);

    }

    private void updateColorDataByColorName(String name) {

        /*
        int NORMAL_BERET_BRIGHT_COLOR = Program.engine.color(54,71,121);
        int NORMAL_BERET_DARK_COLOR = Program.engine.color(59,123,206);
         */

        int defaultBrightRed = (int) Program.engine.red(NORMAL_BERET_BRIGHT_COLOR);
        int defaultBrightGreen = (int) Program.engine.green(NORMAL_BERET_BRIGHT_COLOR);
        int defaultBrightBlue = (int) Program.engine.blue(NORMAL_BERET_BRIGHT_COLOR);

        int defaultDarkRed = (int) Program.engine.red(NORMAL_BERET_DARK_COLOR);
        int defaultDarkGreen = (int) Program.engine.green(NORMAL_BERET_DARK_COLOR);
        int defaultDarkBlue = (int) Program.engine.blue(NORMAL_BERET_DARK_COLOR);

        int defaultDeltaRed = defaultDarkRed - defaultBrightRed;
        int defaultDeltaGreen = defaultDarkGreen - defaultBrightGreen;
        int defaultDeltaBlue = defaultDarkBlue - defaultBrightBlue;

        int middleColorDelta = (defaultDeltaBlue+defaultDeltaGreen + defaultDeltaRed) / 3;

        if (name == GREEN_NAME_EN) {
            brightColor = Program.engine.color(83, 115, 77);
            darkColor  = Program.engine.color(49, 80, 44);
        }
        else if (name == MAROON_NAME_EN){
            darkColor = Program.engine.color(128+middleColorDelta,0,0);
            brightColor = Program.engine.color(128-middleColorDelta,0,0);
        }
        else if (name == BLUE_NAME_EN) {
            brightColor  = Program.engine.color(62,69,183);
            darkColor = Program.engine.color(39,29,199);
        }
        else if (name == BLACK_NAME_EN) {
            brightColor = Program.engine.color(45,45,45);
            darkColor = Program.engine.color(15,15,15);
        }
        else if (name == ORANGE_NAME_EN) {
            darkColor = Program.engine.color(127,82,45);
            brightColor  = Program.engine.color(225,75,10);
        }
        else if (name == RED_NAME_EN) {
            darkColor = Program.engine.color(205,0,0);
            brightColor  = Program.engine.color(218,75,75);
        }
        else if (name == GREY_NAME_EN) {
            brightColor = Program.engine.color(161,161,161);
            darkColor  = Program.engine.color(85,85,85);
        }
        else if (name == WHITE_NAME_EN) {
            darkColor = Program.engine.color(255,255,255);
            brightColor = Program.engine.color(210,210,210);
        }
        else if (name == TAN_NAME_EN) {
            brightColor = Program.engine.color(210,180,140);
            darkColor =Program.engine.color(180,137,80);
        }
        /*
        if (name == GREEN_NAME_EN) {
            brightColor = Program.engine.color(49, 80, 44);
            darkColor  = Program.engine.color(83, 115, 77);
        }
        else if (name == MAROON_NAME_EN){
            darkColor = Program.engine.color(128-middleColorDelta,0,0);
            brightColor = Program.engine.color(128+middleColorDelta,0,0);
        }
        else if (name == BLUE_NAME_EN) {
            brightColor  = Program.engine.color(39,29,199);
            darkColor = Program.engine.color(62,69,183);
        }
        else if (name == BLACK_NAME_EN) {
            brightColor = Program.engine.color(15,15,15);
            darkColor = Program.engine.color(45,45,45);
        }
        else if (name == ORANGE_NAME_EN) {
            darkColor = Program.engine.color(127,82,45);
            brightColor  = Program.engine.color(225,75,10);
        }
        else if (name == RED_NAME_EN) {
            darkColor = Program.engine.color(218,75,75);
            brightColor  = Program.engine.color(225,0,0);
        }
        else if (name == GREY_NAME_EN) {
            brightColor = Program.engine.color(85,85,85);
            darkColor  = Program.engine.color(161,161,161);
        }
        else if (name == WHITE_NAME_EN) {
            darkColor = Program.engine.color(210,210,210);
            brightColor = Program.engine.color(255,255,255);
        }
        else if (name == TAN_NAME_EN) {
            brightColor = Program.engine.color(180,137,80);
            darkColor = Program.engine.color(210,180,140);
        }
         */

        else {
            System.out.println("Random color was used");
            float min = middleColorDelta+1;
            float max = middleColorDelta-1;
            int randomR = (int) Program.engine.random(min, max);
            int randomG = (int) Program.engine.random(min, max);
            int randomB = (int) Program.engine.random(min, max);
            darkColor = Program.engine.color(randomR + middleColorDelta, randomG+ middleColorDelta, randomB+middleColorDelta);
            brightColor = Program.engine.color(randomR - middleColorDelta, randomG- middleColorDelta, randomB-middleColorDelta);
        }


    }

    public TwiceColor(String colorName, int brightColor, int darkColor) {
        this.name = colorName;
        this.brightColor = brightColor;
        this.darkColor = darkColor;
    }

    public TwiceColor() {
        this.brightColor = NORMAL_BERET_BRIGHT_COLOR;
        this.darkColor = NORMAL_BERET_DARK_COLOR;
        this.name = IBeretColors.SKY_BLUE_NAME_EN;
    }

    public int getBrightColor() {
        return brightColor;
    }

    public int getDarkColor() {
        return darkColor;
    }

    public static String getTextRepresentationForColor(TwiceColor twiceColor){
        char divider = '\'';
        String text = "Dark: " + twiceColor.getDarkRed() + divider + twiceColor.getDarkGreen() + divider + twiceColor.getDarkBlue() + "; bright: " + twiceColor.getBrightRed() + divider + twiceColor.getBrightGreen() + divider + twiceColor.getBrightBlue();
        return text;
    }

    public int getDarkRed(){
        return (int) Program.engine.red(darkColor);
    }

    public int getDarkGreen(){
        return (int) Program.engine.green(darkColor);
    }

    public int getDarkBlue(){
        return (int) Program.engine.blue(darkColor);
    }

    public int getBrightRed(){
        return (int) Program.engine.red(brightColor);
    }

    public int getBrightGreen(){
        return (int) Program.engine.green(brightColor);
    }

    public int getBrightBlue(){
        return (int) Program.engine.blue(brightColor);
    }

    public String getName() {
        return name;
    }
}
