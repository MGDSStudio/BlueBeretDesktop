package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.texturepacker.TextureDecodeManager;

public abstract class HUD_GraphicData {
    public static Image mainGraphicFile;
    public static final ImageZoneSimpleData toPortalButton = new ImageZoneSimpleData(32-32,34-32, 32+32,34+32);
    public static final ImageZoneSimpleData toAnimatedPortalSecondButton = new ImageZoneSimpleData(32-32,34-32+249, 32+32,34+32+249);
    public static final ImageZoneSimpleData actionButton = new ImageZoneSimpleData(32-32+60,34-32+249, 32+32+60,34+32+249);


    public static final ImageZoneSimpleData reloadButton = new ImageZoneSimpleData(93-32,34-32, 93+32,34+32);
    public static final ImageZoneSimpleData shotButton = new ImageZoneSimpleData(93-32,96-32, 93+32,96+32);
    public static final ImageZoneSimpleData withoutAmmoShotButton = new ImageZoneSimpleData(1,1,2,2);
    public static final ImageZoneSimpleData jumpButton = new ImageZoneSimpleData(32-32,96-32, 32+32,96+32);
    public static final ImageZoneSimpleData backButton = new ImageZoneSimpleData(0,192-1, 64,256-1);
    public static final ImageZoneSimpleData kickButton = new ImageZoneSimpleData(31-32,159-32, 31+32,159+32);

    /*
    public static final ImageZoneSimpleData toPortalButton = new ImageZoneSimpleData(0,0, 64,64);
    public static final ImageZoneSimpleData reloadButton = new ImageZoneSimpleData(62,2, 126,66);
    public static final ImageZoneSimpleData shotButton = new ImageZoneSimpleData(61,65, 125,129);
    public static final ImageZoneSimpleData jumpButton = new ImageZoneSimpleData(0,64, 64,128);
    public static final ImageZoneSimpleData backButton = new ImageZoneSimpleData(0,192-1, 64,256-1);
    public static final ImageZoneSimpleData kickButton = new ImageZoneSimpleData(0,128, 64,128+64);
     */

    public static final ImageZoneSimpleData stickHole = new ImageZoneSimpleData(384,384, 512,512);

    /*
    //next three variables must be refilled after graphic will be ready
    public static final ImageZoneSimpleData stickCenterZone = new ImageZoneSimpleData(384,384, 512,512);
    public static final ImageZoneSimpleData stickGoToRightZone = new ImageZoneSimpleData(384,384, 512,512);
    public static final ImageZoneSimpleData stickRunToRightZone = new ImageZoneSimpleData(384,384, 512,512);*/

    public static final ImageZoneSimpleData loadingRing = new ImageZoneSimpleData(250,388, 375,508);
    public static final ImageZoneSimpleData stick = new ImageZoneSimpleData(68,134, 122,189);
    //public static final ImageZoneSimpleData stick = new ImageZoneSimpleData(69,125, 69+64,125+64);
    //public static final ImageZoneSimpleData stick = new ImageZoneSimpleData(64,128, 128,128+64);
    public static final ImageZoneSimpleData lifeLineClear = new ImageZoneSimpleData(0,256, 128,288);
    //public static final ImageZoneSimpleData lifeLineClear = new ImageZoneSimpleData(295,0, 422,31);
    public static final ImageZoneSimpleData blackColor = new ImageZoneSimpleData(700,410, 710,420);
    public static final ImageZoneSimpleData blackButtonBackground = new ImageZoneSimpleData(65,131, 122, 188);
    // was public static final ImageZoneSimpleData blackButtonBackground = new ImageZoneSimpleData(64,130, 123, 189);
    public static final ImageZoneSimpleData lifeLineFilled = new ImageZoneSimpleData(6,293, 123,314);

    //public static final ImageZoneSimpleData shotgunWeaponFire = new ImageZoneSimpleData(402,267, 123,314);


    private final static ImageZoneSimpleData pistol = new ImageZoneSimpleData(62,314,135,367);

    //private final static ImageZoneSimpleData pistol = new ImageZoneSimpleData(56,352,111,389);
    private final static ImageZoneSimpleData smg = new ImageZoneSimpleData(0,402,94,439);
    private final static ImageZoneSimpleData grenadeLauncher = new ImageZoneSimpleData(0,438,89,476);
    private final static ImageZoneSimpleData shotgun = new ImageZoneSimpleData(0,476,105,512);
    private final static ImageZoneSimpleData revolver = new ImageZoneSimpleData(0,351,71,395+8);
    private final static ImageZoneSimpleData handGrenade = new ImageZoneSimpleData(89,432,125+8,469);
    //private final static ImageZoneSimpleData sniperRiffle = new ImageZoneSimpleData(0,352,53,389);
    public final static ImageZoneSimpleData soShotgun = new ImageZoneSimpleData(0,325,57,352);

    public final static ImageZoneSimpleData basicRectForSimpleFramesWithRoundedCorners = new ImageZoneSimpleData(754,0,786,32);
    public final static ImageZoneSimpleData basicRectForSimpleFramesWithLeftLowerSharpCorner = new ImageZoneSimpleData(806,478,840,512);
    public final static ImageZoneSimpleData basicRectForSimpleFramesWithRightLowerSharpCorner = new ImageZoneSimpleData(806,478-33,840,512-33);

    //public final static ImageZoneSimpleData basicRectForDialogFramesWithoutBackground = new ImageZoneSimpleData(718,431,799,512);
    public final static ImageZoneSimpleData basicRectForDialogFrames = new ImageZoneSimpleData(651,356,803,511);
    public final static ImageZoneSimpleData downwardsArrow = new ImageZoneSimpleData(722,9,750,18);
    public final static ImageZoneSimpleData playerFaceFullLife = new ImageZoneSimpleData(456,2+5+3, 510, 60+3);
    public final static ImageZoneSimpleData playerFaceEyesClosedFullLife = new ImageZoneSimpleData(456,61+5+3, 510, 118+3);

    public final static ImageZoneSimpleData playerFaceHalfLife = new ImageZoneSimpleData(511,66, 565, 119);
    public final static ImageZoneSimpleData playerFaceHalfLifeMouthOpened = new ImageZoneSimpleData(588,291, 588+54, 291+53);
    public final static ImageZoneSimpleData playerFaceEyesClosedHalfLife = new ImageZoneSimpleData(511,66+59, 565, 119+59);


    public final static ImageZoneSimpleData playerFaceSmallLife = new ImageZoneSimpleData(563,66+2, 617, 119+2);
    public final static ImageZoneSimpleData playerFaceSmallLifeMouthOpened = new ImageZoneSimpleData(537,293+2, 537+54,293+53+2);
    public final static ImageZoneSimpleData playerFaceEyesClosedSmallLife = new ImageZoneSimpleData(563,66+59+2, 617, 119+59+2);
    public final static ImageZoneSimpleData playerFaceFullLifeMouthOpened = new ImageZoneSimpleData(456,117+5+3, 510, 175+3);
    public final static ImageZoneSimpleData boar = new ImageZoneSimpleData(510-26,322-26, 510+26,322+26);
    public final static ImageZoneSimpleData boarWithBlood = new ImageZoneSimpleData(510-26-28,322-26+55, 510+26-28,322+26+55);
    public final static ImageZoneSimpleData listArrow = new ImageZoneSimpleData(719,9, 753, 18);

    private final static int halfWidth = 26;


    public final static ImageZoneSimpleData dealerFaceEyesOpened = new ImageZoneSimpleData(424-halfWidth,0, 424+halfWidth, 56);
    public final static ImageZoneSimpleData dealerFaceEyesClosed = new ImageZoneSimpleData(424-halfWidth,55, 424+halfWidth, 55+56);
    public final static ImageZoneSimpleData dealerFaceMouthOpened = new ImageZoneSimpleData(424-halfWidth,55+56-1, 424+halfWidth, 55+56-1+55);

    public static void init(){
        //if (mainGraphicFile == null) mainGraphicFile = new Image(Program.getAbsolutePathToAssetsFolder("HUD.png"));
        mainGraphicFile = new Image(Program.getAbsolutePathToAssetsFolder("HUD"+ TextureDecodeManager.getExtensionForGraphicSources()));
    }

    public static ImageZoneSimpleData getImageZoneForWeaponType(WeaponType actualWeaponType) {
        if (actualWeaponType == WeaponType.SHOTGUN) return shotgun;
        else if (actualWeaponType == WeaponType.SAWED_OFF_SHOTGUN) return soShotgun;
        else if (actualWeaponType == WeaponType.REVOLVER) return revolver;
        else if (actualWeaponType == WeaponType.GRENADE) return handGrenade;
        else if (actualWeaponType == WeaponType.SMG) return smg;
        else if (actualWeaponType == WeaponType.HANDGUN) return pistol;
        else if (actualWeaponType == WeaponType.M79) return grenadeLauncher;
        else {
            System.out.println("There are no weapon "  + actualWeaponType);
            return null;
        }
    }

    public static ImageZoneSimpleData getImageZoneForObjectType(int type, boolean inWorld) {
        if (type == AbstractCollectable.LARGE_MEDICAL_KIT) {
            if (inWorld) return InWorldObjectsGraphicData.largeMedicalKitInWorld;
            else return InWorldObjectsGraphicData.largeMedicalKitInBag;
        }
        else if (type == AbstractCollectable.MEDIUM_MEDICAL_KIT) {
            if (inWorld) return InWorldObjectsGraphicData.mediumMedicalKitInWorld;
            else return InWorldObjectsGraphicData.mediumMedicalKitInBag;
        }

        else if (type == AbstractCollectable.SMALL_MEDICAL_KIT) {
            if (inWorld) return InWorldObjectsGraphicData.smallMedicalKitInWorld;
            else return InWorldObjectsGraphicData.smallMedicalKitInBag;
        }
        else if (type == AbstractCollectable.HANDGUN) return pistol;

        else if (type == AbstractCollectable.REVOLVER) return revolver;
        else if (type == AbstractCollectable.SO_SHOTGUN) return soShotgun;
        else if (type == AbstractCollectable.HAND_GREENADE) return handGrenade;
        else if (type == AbstractCollectable.SHOTGUN) return shotgun;
        else if (type == AbstractCollectable.SMG) return smg;
        else if (type == AbstractCollectable.GREENADE_LAUNCHER) return grenadeLauncher;

        else if (type == AbstractCollectable.BULLETS_FOR_PISTOL) return InWorldObjectsGraphicData.pistolAmmoInShop;
        else if (type == AbstractCollectable.BULLETS_FOR_REVOLVER) return InWorldObjectsGraphicData.revolverAmmoInShop;
        else if (type == AbstractCollectable.BULLETS_FOR_SHOTGUN) return InWorldObjectsGraphicData.shotgunAmmoInShop;
        else if (type == AbstractCollectable.BULLETS_FOR_M79) return InWorldObjectsGraphicData.m79AmmoInShop;
        else if (type == AbstractCollectable.BULLETS_FOR_HAND_GRENADE) return InWorldObjectsGraphicData.handGrenadeInWorldAsAmmo;
        else if (type == AbstractCollectable.BULLETS_FOR_SMG) return InWorldObjectsGraphicData.smgAmmoInShop;

        else if (type == AbstractCollectable.SYRINGE) {
            if (inWorld) return InWorldObjectsGraphicData.syringeInWorld;
            return InWorldObjectsGraphicData.syringeInBag;
        }
        else {
            System.out.println("There are no graphic data for this object type " + type);
            return null;
        }
    }


}
