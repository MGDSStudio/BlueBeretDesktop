package com.mgdsstudio.blueberet.gameobjects.collectableobjects;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;

public class WeaponMagazine extends SimpleCollectableElement {

    private WeaponType weaponType;

    public WeaponMagazine(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType) {
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    public WeaponMagazine(GameObjectDataForStoreInEditor objectData, GameRound gameRound){
        Vec2 position = objectData.getPosition();
        byte type = (byte) objectData.getLocalType();
        int valueToBeAddedByGain = objectData.getKeyValue();
        int fixationType = objectData.getFixationType();
        initBasicData(position, type, gameRound, valueToBeAddedByGain, fixationType);
    }

    protected void initBasicData(Vec2 position, byte type, GameRound gameRound, int valueToBeAddedByGain, int fixationType){
        this.type = type;
        this.fixationType = fixationType;
        if (type == BULLETS_FOR_SHOTGUN) weaponType = WeaponType.SHOTGUN;
        else if (type == BULLETS_FOR_M79) weaponType = WeaponType.M79;
        else if (type == BULLETS_FOR_PISTOL) weaponType = WeaponType.HANDGUN;
        else if (type == BULLETS_FOR_SMG) weaponType = WeaponType.SMG;
        else if (type == BULLETS_FOR_REVOLVER) weaponType = WeaponType.REVOLVER;
        else if (type == BULLETS_FOR_HAND_GRENADE) weaponType = WeaponType.GRENADE;
        else {
            System.out.println("This weapon type is not known");
            weaponType = WeaponType.SHOTGUN;
        }
        fillBasicGraphicData();
        calculateDimensions();
        this.valueToBeAddedByGain = valueToBeAddedByGain;
        init(position, gameRound, width, height, BODY_FORM_RECT);
        initStars();
    }



    private void fillBasicGraphicData() {
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) graphicData = InWorldObjectsGraphicData.shotgunAmmoInWorld;
        else if (weaponType == WeaponType.HANDGUN) graphicData = InWorldObjectsGraphicData.pistolAmmoInWorld;
        else if (weaponType == WeaponType.M79) graphicData = InWorldObjectsGraphicData.m79AmmoInWorld;
        else if (weaponType == WeaponType.SMG) graphicData = InWorldObjectsGraphicData.smgAmmoInWorld;
        else if (weaponType == WeaponType.REVOLVER) graphicData = InWorldObjectsGraphicData.revolverAmmoInWorld;
        else if (weaponType == WeaponType.GRENADE) graphicData = InWorldObjectsGraphicData.handGrenadeInWorldAsBullet;
        else {
            graphicData = InWorldObjectsGraphicData.shotgunAmmoInWorld;
        }
        graphicType = SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE;
        graphicData = InWorldObjectsGraphicData.getGraphicDataForWeapon(weaponType);
        System.out.println();
        sprite = new StaticSprite(InWorldObjectsGraphicData.mainGraphicFile.getPath(), graphicData.leftX, graphicData.upperY, graphicData.rightX, graphicData.lowerY, 1, 1);
        loadSprites(InWorldObjectsGraphicData.mainGraphicTileset);
    }










    @Override
    public String getStringAddedToWorldByBeGained(){
        if (type == BULLETS_FOR_SHOTGUN) return "SHOTGUN SHELLS";
        else if (type == BULLETS_FOR_REVOLVER) return "REVOLVER BULLETS";
        else if (type == BULLETS_FOR_PISTOL) return "HANDGUN AMMO";
        else if (type == BULLETS_FOR_SMG) return "SMG AMMO";

        else if (type == BULLETS_FOR_M79) return "GRENADES";
        else if (type == BULLETS_FOR_HAND_GRENADE) return "HAND GRENADES";
        /*
        if (type == BULLETS_FOR_SHOTGUN) return valueToBeAddedByGain+ " SHOTGUN AMMO";
        else if (type == BULLETS_FOR_PISTOL) return valueToBeAddedByGain+ " HANDGUN AMMO";
        else if (type == BULLETS_FOR_SMG) return valueToBeAddedByGain+ " SMG AMMO";
        else if (type == BULLETS_FOR_SMG) return valueToBeAddedByGain+ " GRENADE";
         */
        else return " ";
    }


    public WeaponType getWeaponType() {
        return weaponType;
    }

    @Override
    public String getObjectToDisplayName(){
        return "Weapon magazine";
    }

}
