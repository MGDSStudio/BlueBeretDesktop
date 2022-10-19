package com.mgdsstudio.blueberet.gameprocess;

public abstract class CollisionFilterCreator {
    //Categories
    final public static int CATEGORY_CAMERA =           0b0000_0000_0000_0001; // No collision
    final public static int CATEGORY_PIPE_INSIDE_AREA = 0b0000_0000_0000_0010;
    final public static int CATEGORY_GAME_OBJECT =      0b0000_0000_0000_0100;
    final public static int CATEGORY_PLAYER =           0b0000_0000_0000_1000;
    final public static int CATEGORY_BULLET =           0b0000_0000_0001_0000;
    final public static int CATEGORY_FLOWER =           0b0000_0000_0010_0000;
    final public static int CATEGORY_CORPSE =           0b0000_0000_0100_0000;
    final public static int CATEGORY_DRAGON =           0b0000_0000_1000_0000;
    final public static int CATEGORY_DRAGON_FIRE =      0b0000_0001_0000_0000;
    final public static int CATEGORY_COLLECTABLE_OBJECT=0b0000_0010_0000_0000;
    final public static int CATEGORY_WALL_FOR_CAMERA =  0b0000_0100_0000_0000;
    //final public static int CATEGORY_WALL_FOR_CAMERA =  0b0000_0100_0000_0000;
    /*
    final public static int CATEGORY_CAMERA = 0x0001; // No collision
    final public static int CATEGORY_PIPE_INSIDE_AREA = 0x0002;
    final public static int CATEGORY_GAME_OBJECT = 0x0004;
    final public static int CATEGORY_PLAYER = 0x0008;
    final public static int CATEGORY_BULLET = 0x0016;
    final public static int CATEGORY_FLOWER = 0x0032;
    final public static int CATEGORY_CORPSE = 0x0064;
*/
    /*
    final public static int CATEGORY_CAMERA =           0b0000_0000_0000_0001; // No collision
    final public static int CATEGORY_PIPE_INSIDE_AREA = 0b0000_0000_0000_0010;
    final public static int CATEGORY_GAME_OBJECT =      0b0000_0000_0000_0100;
    final public static int CATEGORY_PLAYER =           0b0000_0000_0000_1000;
    final public static int CATEGORY_BULLET =           0b0000_0000_0001_0000;
    final public static int CATEGORY_FLOWER =           0b0000_0000_0010_0000;
    final public static int CATEGORY_CORPSE =           0b0000_0000_0100_0000;

     */


    //Masks
    //final private static int MASK_CAMERA = 0b0000_0000_0000_0000; //No collisions
    final private static int MASK_CAMERA = CATEGORY_WALL_FOR_CAMERA; //Only with wall
    final private static int MASK_WALL_FOR_CAMERA = CATEGORY_CAMERA;
    final private static int MASK_PIPE_INSIDE_AREA = CATEGORY_GAME_OBJECT | CATEGORY_PLAYER;
    final private static int MASK_GAME_OBJECT = CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE; //No collisions
    final private static int MASK_PLAYER =      CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE; //No collisions
    //final private static int MASK_PLAYER =      CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT |                   CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE; //No collisions
    //
    final private static int MASK_BULLET =                                  CATEGORY_PLAYER | CATEGORY_GAME_OBJECT |                   CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE ;
    //final private static int MASK_BULLET =                                                    CATEGORY_GAME_OBJECT |                   CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE ;
    //
    final private static int MASK_FLOWER =                                  CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET                   | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE;
    final private static int MASK_CORPSE =                                  CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON | CATEGORY_DRAGON_FIRE;
    final private static int MASK_DRAGON =      CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE | CATEGORY_DRAGON; //No collisions
    final private static int MASK_DRAGON_FIRE = CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE; //No collisions
    //final private static int MASK_COLLECTABLE_OBJECT = CATEGORY_PIPE_INSIDE_AREA | CATEGORY_PLAYER | CATEGORY_GAME_OBJECT | CATEGORY_BULLET | CATEGORY_FLOWER | CATEGORY_CORPSE; //No collisions



    public static int getMaskForCategory(int category){

        //System.out.println("Categories: " + CATEGORY_CAMERA+ ", " + CATEGORY_PIPE_INSIDE_AREA + ", " + CATEGORY_GAME_OBJECT + ", " + CATEGORY_PLAYER + ", " + CATEGORY_BULLET + ", " + CATEGORY_FLOWER + ", " + CATEGORY_CORPSE);
        if (category == CATEGORY_CAMERA) return MASK_CAMERA;
        else if (category == CATEGORY_PIPE_INSIDE_AREA) return MASK_PIPE_INSIDE_AREA;
        else if (category == CATEGORY_GAME_OBJECT) return MASK_GAME_OBJECT;
        else if (category == CATEGORY_PLAYER) return MASK_PLAYER;
        else if (category == CATEGORY_BULLET) return MASK_BULLET;
        else if (category == CATEGORY_FLOWER) return MASK_FLOWER;
        else if (category == CATEGORY_CORPSE) return MASK_CORPSE;
        else if (category == CATEGORY_DRAGON) return MASK_DRAGON;
        else if (category == CATEGORY_DRAGON_FIRE) return MASK_DRAGON_FIRE;
        else if (category == CATEGORY_WALL_FOR_CAMERA) return MASK_WALL_FOR_CAMERA;
        else {
            System.out.println("No data about this category");
            return CATEGORY_CAMERA;
        }

    }


    /*
    public CollisionFilterCreator(GameRound gameRound){
        //setCategories(gameRound);
        //setBitMasks(gameRound);
        System.out.println("Filtering is created");
    }

    private void setCategories(GameRound gameRound) {
        for (Body b = PhysicGameWorld.controller.world.getBodyList(); b!=null; b=b.getNext()) {
            for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {

                try{
                    GameObject gameObject = PhysicGameWorld.getGameObjectByFixture(gameRound, f);
                    if (gameObject != null) {
                        if (gameObject.getClass() == Bullet.class) {
                            f.m_filter.categoryBits = CATEGORY_PLAYER_BULLET;
                        }
                        else if (gameObject instanceof Plant){
                            f.m_filter.categoryBits = CATEGORY_FLOWER;
                        }
                        else if (gameObject instanceof RoundPipe){
                            f.m_filter.categoryBits = CATEGORY_PIPE_INSIDE_AREA;
                        }
                        else f.m_filter.categoryBits = CATEGORY_SIMPLE_GAME_OBJECT;

                    }
                }
                catch (Exception e){
                    System.out.println("Can not set filter mask for this object; " );
                    e.printStackTrace();
                }
            }
        }
    }

    private void setBitMasks(GameRound gameRound) {
        for (Body b = PhysicGameWorld.controller.world.getBodyList(); b!=null; b=b.getNext()) {
            for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {

                try{
                    GameObject gameObject = PhysicGameWorld.getGameObjectByFixture(gameRound, f);
                    if (gameObject != null) {
                        if (gameObject.getClass() == Bullet.class) {
                            System.out.println("There are no bullets by loading");
                            f.m_filter.maskBits = MASK_PLAYER_BULLET;
                            //System.out.println("Bullet set mask ");
                        }
                        else if (gameObject instanceof Plant){
                            f.m_filter.maskBits = MASK_FLOWER;
                        }
                        else if (gameObject.getClass() == RoundPipe.class){

                            f.m_filter.maskBits = MASK_PIPE_INSIDE_AREA;
                            System.out.println("Pipe is for bullet not active");

                        }

                        else f.m_filter.maskBits = MASK_SIMPLE_GAME_OBJECT;
                    }
                }
                catch (Exception e){
                    System.out.println("Can not set filter mask for this object; " );
                    e.printStackTrace();
                }
            }
        }
    }
*/
}
