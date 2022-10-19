package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

public class RectangularElementAdding extends ObjectWithSetableFormAdding{

    public final static byte ANGLE_CHOOSING = 3;
    public final static byte LIFE_SETTING = 4;
    public final static byte BODY_TYPE_CHOOSING = 5;
    public final static byte SPRING_ADDING = 6;
    public final static byte NEW_OR_EXISTING_TILESET = 7;
    public final static byte UPLOAD_TILESET_FROM_STORAGE_TO_CACHE = 8;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 9;
    public final static byte FILL_OR_STRING_TEXTURE = 10;
    public final static byte TEXTURE_REGION_CHOOSING = 11;
    public final static byte COMPLETED = 12;
    public final static byte END = COMPLETED;



    public RectangularElementAdding(){
        endStatement = END;
    }




}
