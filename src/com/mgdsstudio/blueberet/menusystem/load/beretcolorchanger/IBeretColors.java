package com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger;

import com.mgdsstudio.blueberet.mainpackage.Program;

public interface IBeretColors {
    int SKY_BLUE = 0;
    int GREEN = 1;
    int MAROON = 2;
    int BLUE = 3;
    int BLACK = 4;
    int TAN = 5;
    int RED = 6;
    int GREY = 7;
    int ORANGE = 8;
    int WHITE = 9;

    String SKY_BLUE_NAME_EN = "Sky blue";
    String GREEN_NAME_EN = "Green";
    String MAROON_NAME_EN = "Maroon";
    String BLUE_NAME_EN = "Blue";
    String BLACK_NAME_EN = "Black";
    String ORANGE_NAME_EN = "Orange";
    String RED_NAME_EN = "Red";
    String GREY_NAME_EN = "Grey";
    String TAN_NAME_EN = "Tan";
    String WHITE_NAME_EN = "White";

    String SKY_BLUE_NAME_RU = "НЕБЕСНО-ГОЛУБОЙ";
    String GREEN_NAME_RU = "ЗЕЛЕНЫЙ";
    String MAROON_NAME_RU = "КРАПОВЫЙ";
    String BLUE_NAME_RU = "СИНИЙ";
    String BLACK_NAME_RU = "ЧЕРНЫЙ";
    String ORANGE_NAME_RU = "ОРАНЖЕВЫЙ";
    String RED_NAME_RU = "КРАСНЫЙ";
    String GREY_NAME_RU = "СЕРЫЙ";
    String TAN_NAME_RU = "ПЕСЧАНЫЙ";
    String WHITE_NAME_RU = "БЕЛЫЙ";


    String BERET_COLOR_NAME = "Color name";
    String BRIGHT_COLOR_NAME = "Bright color name";
    String DARK_COLOR_NAME = "Dark color name";

/*
    int NORMAL_BERET_BRIGHT_COLOR = Program.engine.color(24,56,120);
    int NORMAL_BERET_DARK_COLOR = Program.engine.color(42,135,209);
*/


    int NORMAL_BERET_BRIGHT_COLOR = Program.engine.color(59,123,206);
    int NORMAL_BERET_DARK_COLOR = Program.engine.color(54,71,121);


}
