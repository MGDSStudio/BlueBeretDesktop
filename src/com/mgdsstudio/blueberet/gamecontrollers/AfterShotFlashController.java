package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SingleFlash;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AfterShotFlashController {
    private ArrayList<SingleFlash> flashes = new ArrayList<>();
    private final StaticSprite image;
    public final static int NORMAL_WIDTH = 90;
    public final static int NORMAL_HEIGHT = 95;
    private boolean graphicLoaded;
    private final boolean withFlashes = false;

    public AfterShotFlashController(GameRound gameRound){
        final ImageZoneSimpleData data = InWorldObjectsGraphicData.shotFlash;
        //if (image == null)
         image = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), data.leftX, data.upperY, data.rightX, data.lowerY, NORMAL_WIDTH, NORMAL_HEIGHT);
        //image.loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(InWorldObjectsGraphicData.mainGraphicFile.getPath()));
        flashes.clear();
    }



    public void addNewFlash(Person person){
        if (person.getActualWeapon().getWeaponType() != WeaponType.GRENADE){
            if (withFlashes) {
                float x = person.getPixelPosition().x;
                float y = person.getPixelPosition().y;
                boolean side = !person.getSightDirection();
                if (side == Person.TO_LEFT) x += (person.getWidth() / 15);
                else x -= (person.getWidth() / 15);
                if (flashes.size() == 0) {
                    SingleFlash flash = new SingleFlash(new Vec2(x, y), side);
                    flash.start();
                    flashes.add(flash);
                    if (!graphicLoaded) {
                        image.loadSprite(InWorldObjectsGraphicData.mainGraphicTileset);
                        graphicLoaded = true;
                    }
                } else {
                    boolean recreatedFromPool = false;
                    for (SingleFlash existingFlash : flashes) {
                        if (existingFlash.isEnded()) {
                            existingFlash.recreate(x, y, side);
                            existingFlash.start();
                            recreatedFromPool = true;
                        }
                    }
                    if (!recreatedFromPool) {
                        SingleFlash flash = new SingleFlash(new Vec2(x, y), side);
                        flash.start();
                        flashes.add(flash);
                    }
                }
            }
        }
        else {
            System.out.println("For this weapon flash can not be added " + person.getActualWeapon());
        }
    }

    public void update(){
        if (withFlashes) {
            for (SingleFlash singleFlash : flashes) {
                singleFlash.update();
            }
        }
    }

    public void draw(GameCamera gameCamera){
        if (withFlashes) {
            for (SingleFlash singleFlash : flashes) singleFlash.draw(gameCamera, image);
        }
    }

}
