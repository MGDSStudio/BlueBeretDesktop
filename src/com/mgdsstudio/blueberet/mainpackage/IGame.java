package com.mgdsstudio.blueberet.mainpackage;

public interface IGame {
    // Development mode constants
    public final static boolean EVERY_FRAME_UPDATING = true;
    final static byte FRAMES_NUMBER_TO_MISS = 0;
    final static boolean USE_BACKGROUND_BUFFER = false;
    final static boolean ANDROID = false;
    final static boolean DEBUG_MODE = true;
    final static boolean MULTITHREADED = true;
    final static byte FRAMES_NUMBER_FOR_UPDATING_OF_SECONDARY_OBJECTS = 50;
    final static byte DISPLAY_SCALE = 2;
    final static boolean USE_OPEN_GL = false;
    final static byte ANTI_ALIASING = 8;
    final static boolean PLAYER_CAN_NOT_BE_ATTACKED = true;
    final static byte AI_UPDATE_FREQUENCY = 20; // frames number to pass before updating

    // Animation data constants
    final static byte NORMAL_ANIMATION_CHANGING_TIME = 75;
    final static byte FAST_ANIMATION_CHANGING_TIME = 50;
    final static byte SLOW_ANIMATION_CHANGING_TIME = 125;

    // Buttons
    public final static boolean ROUND_BUTTON = true;
    public final static boolean RECTANGULAR_BUTTON = false;
    public final static boolean BUTTON_IS_PRESSED = true;
    public final static boolean BUTTON_IS_NOT_PRESSED = false;
    public final static byte BUTTON_SPEAK = 0;
    public final static byte BUTTON_ATTACK = 1;
    public final static byte BUTTON_BOW_SHOOTING = 2;
    public final static byte BUTTON_FIRE_WEAPON_SHOOTING = 3;
    public final static byte BUTTON_SPELLING = 4;

    public final static byte STICK_IS_IN_CENTER_PLACE = 0;
    public final static byte STICK_IN_AIMING_ZONE = 1;
    public final static byte STICK_IN_DEATH_ZONE = 2;
    public final static byte STICK_IN_GO_ZONE = 3;
    public final static byte STICK_IN_RUN_ZONE = 4;
    public final static byte STICK_IS_FAR_AWAY_FROM_CENTER_PLACE = 5;

    // Game statements
    public final static byte START_VIDEO_IS_PLAYING = 0;
    public final static byte START_MENU_IS_SHOWED = 1;
    public final static byte OPTIONS_MENU = 2;
    public final static byte STORY_MENU = 3;
    public final static byte SINGLE_MISSIONS_MENU = 4;
    public final static byte AUTHORS_MENU = 5;
    public final static byte ISOMETRIC_RECTANGULAR_PHYSIC_FORM = 2;
    public final static byte RECTANGULAR_2D_PHYSIC_FORM = 3;
    public final static byte LOADING_SCREEN = 10;
    public final static byte THE_END_SCREEN = 100;
    public final static byte GAME_PROCESS = -1;

    public final static boolean ON = true;
    public final static boolean OFF = false;

    // Types of object physic
    public final static boolean SOLID_BODY = true;  // this object type is penetrable
    public final static byte WITHOUT_PHYSIC_MODEL = 0;
    public final static byte CIRCULAR_PHYSIC_FORM = 1;
    public final static byte SQUARE_2D_PHYSIC_FORM = 4;

    // Right triangle types
    public final static byte NORTH_WEST_TRIANGLE_DIRECTION = 4;  // Richtung der Hipothenuse
    public final static byte NORTH_EAST_TRIANGLE_DIRECTION = 6;  // Richtung der Hipothenuse
    public final static byte SOUTH_EAST_TRIANGLE_DIRECTION = 8;  // Richtung der Hipothenuse
    public final static byte SOUTH_WEST_TRIANGLE_DIRECTION = 10;  // Richtung der Hipothenuse

    //types of obstacles physic forms
    public final static byte CIRCLE_OBSTACLE = 0;
    public final static byte RECTANGULAR_OBSTACLE = 1;
    public final static byte HORIZONTAL_TRIANGLE_OBSTACLE = 2;
    public final static byte EAST_ORIENTED_TRIANGLE_OBSTACLE = 3;
    public final static byte SOUTH_EAST_ORIENTED_TRIANGLE_OBSTACLE = 4;  // Richtung des rechten Winkels
    public final static byte SOUTH_ORIENTED_TRIANGLE_OBSTACLE = 5;  // Richtung des rechten Winkels
    public final static byte SOUTH_WEST_ORIENTED_TRIANGLE_OBSTACLE = 6;  // Richtung des rechten Winkels
    public final static byte WEST_ORIENTED_TRIANGLE_OBSTACLE = 7;  // Richtung des rechten Winkels
    public final static byte NORTH_WEST_ORIENTED_TRIANGLE_OBSTACLE = 8;  // Richtung des rechten Winkels
    public final static byte NORTH_ORIENTED_TRIANGLE_OBSTACLE = 9;  // Richtung des rechten Winkels
    public final static byte NORTH_EAST_ORIENTED_TRIANGLE_OBSTACLE = 10;  // Richtung des rechten Winkels
    //final static byte TRIANGLE_OBSTACLE = 11; // Dieser Typ wird eingesetzt, bevor der echte Typ bestimmt wird!

    public final static boolean TRANSPARENT = true;
    public final static boolean NOT_TRANSPARENT = false;

    // Sprites directions
    public final static byte EAST_DIRECTION = 0;
    public final static byte SOUTH_EAST_DIRECTION = 5;
    public final static byte SOUTH_DIRECTION = 4;
    public final static byte SOUTH_WEST_DIRECTION = 6;
    public final static byte WEST_DIRECTION = 7;
    public final static byte NORTH_WEST_DIRECTION = 3;
    public final static byte NORTH_DIRECTION = 1;
    public final static byte NORTH_EAST_DIRECTION = 2;

/*Frueher war:
final static byte EAST_DIRECTION = 0;
final static byte SOUTH_EAST_DIRECTION = 1;
final static byte SOUTH_DIRECTION = 2;
final static byte SOUTH_WEST_DIRECTION = 3;
final static byte WEST_DIRECTION = 4;
final static byte NORTH_WEST_DIRECTION = 5;
final static byte NORTH_DIRECTION = 6;
final static byte NORTH_EAST_DIRECTION = 7;
*/



    // Rotating directions
    public final static boolean CCW_DIRECTION = false;
    public final static boolean CW_DIRECTION = true;

    // quaters
    public final static byte SOUTH_EAST_QUARTER = 1;
    public final static byte SOUTH_WEST_QUARTER = 2;
    public final static byte NORTH_WEST_QUARTER = 3;
    public final static byte NORTH_EAST_QUARTER = 4;

    // Coordinate systems
    public final static boolean X_AXIS = true;
    public final static boolean Y_AXIS = false;

    // Other constants
    public final static int NOMINAL = 0;   // for resetting a dimentions to the basic value
    public final static int MAX_WEIGHT_FOR_OBJECTS = 9999;
    public final static int NORMAL_MAN_MASS = 80;
    public final static int NORMAL_WOMAN_MASS = 60;
    public final static boolean TWO_PERSONS_ARE_CLOSE_FOR_TALKING = true;
    public final static boolean TWO_PERSONS_ARE_FAR_AWAY_FOR_TALKING = false;
    public final static byte PERSON_NORMAL_RADIUS = 15;


    // Flags
    public final static boolean NOT_ACTIVATED = false;
    public final static boolean ACTIVATED = true;

    // Characters types
    public final static byte SOLDIER = 1;
    public final static byte BARBAR = 2;
    public final static byte SWORDSTAN_SHIELD = 3;
    public final static byte PIRATE_SAILOR = 4;

    // Characters behaviour
    public final static byte PLAYER = 1;
    public final static byte ALLY = 2;
    public final static byte NEUTRAL_CHARACTER = 3;
    public final static byte ENEMY = 4;
    public final static byte FRIEND = 5;
    public final static byte CITY_RESIDENT = 6;

    // Actions/Statements
    public final static byte PAUSED = 1;
    public final static byte ATTACK = 2;
    public final static byte BEEN_HIT = 3;
    public final static byte GREATING = 4;
    public final static byte SHOOTING = 5;
    public final static byte TALKING = 6;
    public final static byte THROWING = 7;
    public final static byte TIPPING_OVER = 8;
    public final static byte WALKING = 9;
    public final static byte RUNNING = 10;

    // Attack types
    public final static byte COLD_WEAPON_EASY_ATTACK = 1;
    public final static byte COLD_WEAPON_ATTACK_AROUND_PERSON_SYMMETRICAL = 2;
    public final static byte BOW_SHOT_EASY = 3;
    public final static byte RIFLE_SHOT_EASY = 4;
    public final static byte RIFLE_SHOT_THREE_BULLETS = 5;
    public final static byte MAGIC_BALL = 6;
    public final static byte GREENADE_LAUCNHER_SHOT = 7;
    // Damage calculatuin constants
    public final static int PARRYING_OF_ATTACK = -1;
    public final static int EVASION_OF_ATTACK = 0;


    // Communication between persons constants
    final float DISTANCE_TO_STOP_FOR_TALKING_COEFFICIENT = 3.0f;
    final float DISTANCE_TO_ATTACK_WITH_COLD_WEAPON_COEFFICIENT = 1.8f;

    // Objects type
    public final static byte ANIMATED_OBJECT = 1;
    public final static byte STATIC_OBJECT = 2;
    public final static byte PERSON = 3;

    // Static objects
    public final static int BAKERY_1 = 1, POTERY_1 = 2, HOUSE_1 = 3, BARRACKS_1 = 4, HOUSE_2 = 5, HOUSE_3 = 6, TREE_1 = 11, TREE_2 = 12, TREE_3 = 13, TREE_4 = 14, TREE_5 = 15,
            TREE_6 = 16, TREE_7 = 17, TREE_8 = 18, TREE_9 = 19, TREE_10 = 20, HOUSE_4 = 21, HOUSE_5 = 22, HOUSE_6 = 23, HOUSE_7 = 24, HOUSE_8 = 25, HOUSE_9 = 26, TEMPLE_1 = 27;

    // Another pictures
    public final static int STOP_SYMBOL = -1;
    public final static int FLAG = -2;
}
