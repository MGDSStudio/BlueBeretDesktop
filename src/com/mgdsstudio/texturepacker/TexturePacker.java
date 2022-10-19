package com.mgdsstudio.texturepacker;

import processing.core.PApplet;

public class TexturePacker {
    protected final String END_PIXEL_DATA_CHAR = "%";
    protected final String START_PIXELS_CHAR = ",";
    protected final char WIDTH_HEIGHT_CHAR = 'x';
    protected PApplet engine;
    protected String path;
    protected final boolean debug = true;
    public static final String MGDS_FILE_EXTENSION = ".mgdstexture";
    protected final char DEVIDER_X_Y = 'x';
    protected final char DEVIDER_Y_VALUE = ':';

    public TexturePacker(String path, PApplet engine) {
        this.path = path;
        this.engine = engine;
    }
}
