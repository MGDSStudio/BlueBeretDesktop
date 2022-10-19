package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenMovableSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class MoveableSpritesAddingController {
    //Types
    public final static int HANDGUN_MAGAZINE = -1;
    public final static int SMG_MAGAZINE = -2;
    public final static int BULLET = -3;

    public final static int BERET = 1;
    public final static int LIZARD_TAIL = 2;
    public final static int HANDGUN_SLEEVE = 10;
    public final static int SHOTGUN_SLEEVE = 11;
    public final static int M79_SLEEVE = 12;
    public final static int SMG_SLEEVE = HANDGUN_SLEEVE;
    public final static int REVOLVER_SLEEVE = HANDGUN_SLEEVE;

    public final static int HANDGUN = 20;

    /*
    public final static int REVOLVER = 21;
    public final static int SHOTGUN = 22;*/


    public final static int NOT_DETERMINED = 0;

    private ArrayList <IndependentOnScreenMovableSprite> moveableSprites;

    public MoveableSpritesAddingController(ArrayList <IndependentOnScreenMovableSprite> moveableSprites){
        this.moveableSprites = moveableSprites;
    }

    public void addNewIndependentOnScreenMoveableSprite(IndependentOnScreenMovableSprite sprite){
        moveableSprites.add(sprite);
    }

    public boolean existsFree(int type){
        if (type != NOT_DETERMINED) {
            for (IndependentOnScreenMovableSprite screenMovableSprite : moveableSprites) {
                if (screenMovableSprite.getType() == type) {
                    //System.out.println("This sprite already exists in the world");
                    if (screenMovableSprite.isEnded()) {
                        //System.out.println("This sprite has ended");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public IndependentOnScreenMovableSprite getExistingFreeSprite(int type){
        if (type != NOT_DETERMINED) {
            for (IndependentOnScreenMovableSprite screenMovableSprite : moveableSprites) {
                if (screenMovableSprite.getType() == type) {
                    if (screenMovableSprite.isEnded()) {
                        return screenMovableSprite;
                    }
                }
            }
        }
        return null;
    }

    public void createBulletSleeveGraphicForType(Soldier player, int graphicType, GameRound gameRound) {
        ImageZoneSimpleData sleeveAnimation = InWorldObjectsGraphicData.getImageZoneForBullet(graphicType);
        float sleeveDimensionCoef = 1f;
        String path = HeadsUpDisplay.mainGraphicSource.getPath();
        final int sleeveWidth = (int) ((sleeveAnimation.rightX-sleeveAnimation.leftX)*sleeveDimensionCoef);
        final int sleeveHeight = (int) ((sleeveAnimation.lowerY-sleeveAnimation.upperY)*sleeveDimensionCoef);
        float directionCoef = 1f;
        float distToAppearingPlace = player.getPersonWidth()/2f;
        if (player.getWeaponAngle() >90 && player.getWeaponAngle()<270) {
            distToAppearingPlace*=(-1);
            directionCoef=-1f;
        }
        float deltaXPos =  Program.engine.random(-5,5);
        Vec2 position = new Vec2(player.getPixelPosition().x+distToAppearingPlace+deltaXPos, player.getPixelPosition().y);
        float randomX = 55*directionCoef+ Program.engine.random(-20,20);
        float randomY = -65*directionCoef+ Program.engine.random(-25,25);
        int weaponCode = graphicType;
        if (existsFree(weaponCode)){
            getExistingFreeSprite(weaponCode).recreate(position, 0f, randomX, randomY, 0, 345, directionCoef*350);

            System.out.println("Graphic for sleeve was recreated from pool for type " + weaponCode);
        }
        else {
            System.out.println("New bullet was created " + graphicType);
            StaticSprite staticSprite = new StaticSprite(path, sleeveAnimation.leftX, sleeveAnimation.upperY, sleeveAnimation.rightX, sleeveAnimation.lowerY, sleeveWidth, sleeveHeight);
            IndependentOnScreenMovableSprite movableSprite = new IndependentOnScreenMovableSprite(staticSprite, position, 0f, 35 * directionCoef, -85, 0, 345, directionCoef * 350, 3000, weaponCode);
            movableSprite.getStaticSprite().loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(path));
            gameRound.addNewIndependentOnScreenMoveableSprite(movableSprite);
        }

    }
}
