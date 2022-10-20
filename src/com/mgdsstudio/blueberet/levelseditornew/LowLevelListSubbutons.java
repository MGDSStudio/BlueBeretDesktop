package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.levelseditornew.sliders.LowLevelFile;
import com.mgdsstudio.blueberet.levelseditornew.sliders.LowLevelPreferences;
import com.mgdsstudio.blueberet.levelseditornew.sliders.LowLevelZones;

enum LowLevelListSubbutons {
    NEW_MAP,
    OPEN_MAP,
    CLEAR_MAP,
    TEST_MAP,
    SAVE_MAP,
    EXIT,


    /*Edit submenus*/
    MOVE_CAMERA,
    SELECT,
    CANCEL,
    MOVE,
    COPY,
    DELETE,
    EDIT_OBJECT,

    NEW_ROUND_RECTANGULAR,      /*Simply round element submenus*/
    NEW_ROUND_CIRCLE,
    NEW_ROUND_POLYGON,
    NEW_COLLECTABLE_OBJECT,



    NEW_ROUND_PIPE,         //Complex round element submenus
    NEW_BRIDGE,
    NEW_ROTATING_STICK,
    NEW_PLATFORM_SYSTEM,


    //Person submenus
    PLACE_PLAYER,
    PLACE_GUMBA,
    PLACE_KOOPA,
    PLACE_BOWSER,
    PLACE_SPIDER,
    PLACE_SNAKE,

    //Graphic submenus
    BACKGROUND,
    NEW_SPRITE,
    NEW_ANIMATION,


    //Zone submenus
    NEW_LAVA_BALLS_ZONE,
    NEW_CLEARING_ZONE,
    NEW_BULLETS_BILL_ZONE,
    NEW_OBJECTS_APPEARING_ZONE,
    NEW_CAMERA_FIXATION_ZONE,
    NEW_PORTAL_SYSTEM_ZONE,
    NEW_LEVEL_END_ZONE,


    //Preferences submenu
    GRID_PREFERENCES,
    DISPLAYING_PREFERENCES,
    SETTINGS,

    HOW_TO_WORK_WITH_CAMERA,
    HOW_TO_ADD_NEW_OBJECTS; //Help submenus

    static LowLevelListSubbutons getStatementByName(String name){


        if (name == LowLevelPreferences.GRID_PREFERENCES) return DISPLAYING_PREFERENCES;
        else if (name == LowLevelZones.NEW_CLEARING_ZONE) return NEW_CLEARING_ZONE;
        else if (name == LowLevelFile.SAVE_MAP) return SAVE_MAP;
        else return DISPLAYING_PREFERENCES;
    }
}
