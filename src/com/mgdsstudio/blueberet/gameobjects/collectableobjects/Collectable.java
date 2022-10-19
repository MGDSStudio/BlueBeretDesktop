package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import org.jbox2d.common.Vec2;

public abstract class Collectable {

    //private Spring spring;
    private final String objectToDisplayName = "Collectable object";
    protected Timer jumpingTimer;
    private final static int JUMPING_FREQUENCY_FOR_STARS = 1700;
    private final static float CRITICAL_SPEED_FOR_DETERMINING_OBJECT_AS_STAYING = 2f;   // For stars
    public final static String CLASS_NAME = "CollectableObject";

    public final static byte COIN = 0;
    public final static byte BULLETS = 1;
    public final static byte WEAPON = 2;
    public final static byte MUSHROOM = 3;
    public final static byte STAR = 4;
    private byte type = COIN;
    //private boolean crushable = false;

    protected final static float NORMAL_MOVEMENT_VELOCITY_ALONG_X = 0.12f;   // for mushrooms and stars
    protected final static float NORMAL_MOVEMENT_VELOCITY_BY_JUMP = NORMAL_MOVEMENT_VELOCITY_ALONG_X*175f;
    private final static float CRITICAL_ANGLE_FOR_MUSHROOM_STOP = 20f;

    public final static boolean IN_WORLD = true;
    public final static boolean IN_BAG = false;
    private boolean stopped = false;
    private boolean inWorldPosition = IN_BAG;
    private Vec2 parentPos;

}
