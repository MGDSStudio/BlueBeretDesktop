package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamecontrollers.MoveableSpritesAddingController;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.texturepacker.TextureDecodeManager;

public abstract class InWorldObjectsGraphicData {
    public static Image mainGraphicFile;
    public static Tileset mainGraphicTileset = HeadsUpDisplay.mainGraphicTileset;
    //private final static int normalGraphicWidth =
    //public static final ImageZoneSimpleData pistolAmmo = new ImageZoneSimpleData(0,0,700+14,140+19);
    public static final ImageZoneSimpleData pistolAmmoInWorld = new ImageZoneSimpleData(700-1,140,714+1,159);
    public static final ImageZoneSimpleData pistolAmmoInShop = new ImageZoneSimpleData(757-3,76-1,777+3,115+1);

    public static final ImageZoneSimpleData revolverAmmoInWorld = new ImageZoneSimpleData(778,194 , 801, 217);
    //public static final ImageZoneSimpleData revolverAmmoInWorld = new ImageZoneSimpleData(700,79-5,739,118+5);
    public static final ImageZoneSimpleData revolverAmmoInShop = new ImageZoneSimpleData(700,79-5,739,118+5);
    public static final ImageZoneSimpleData shotgunAmmoInWorld = new ImageZoneSimpleData(720, 166,738, 186);
    public static final ImageZoneSimpleData shotgunAmmoInShop = new ImageZoneSimpleData(700-2, 40-5,738+2, 73+5);
    public static final ImageZoneSimpleData smgAmmoInWorld = new ImageZoneSimpleData(720,135,721+13,135+24);
    public static final ImageZoneSimpleData smgAmmoInShop = new ImageZoneSimpleData(756,35,779,76);
    public static final ImageZoneSimpleData m79AmmoInWorld = new ImageZoneSimpleData(743,137,743+13,137+22);
    public static final ImageZoneSimpleData m79AmmoInShop = new ImageZoneSimpleData(102,374,125,420);
    public static final ImageZoneSimpleData largeMedicalKitInWorld = new ImageZoneSimpleData(703,194 , 726, 217);
    public static final ImageZoneSimpleData largeMedicalKitInBag = new ImageZoneSimpleData(565,200-23+3-1, 613, 200+23+3+2);
    public static final ImageZoneSimpleData mediumMedicalKitInWorld = new ImageZoneSimpleData(728,194 , 740, 217);
    //12x23
    public static final ImageZoneSimpleData mediumMedicalKitInBag = new ImageZoneSimpleData(538-18,183 , 538+18, 220);
    public static final ImageZoneSimpleData smallMedicalKitInWorld = new ImageZoneSimpleData(740,188 , 753, 215);
    public static final ImageZoneSimpleData smallMedicalKitInBag = new ImageZoneSimpleData(493-13,183 , 512+13, 220);
    public static final ImageZoneSimpleData syringeInWorld = new ImageZoneSimpleData(754,185 , 766, 216);
    //public static final ImageZoneSimpleData syringeInBag = new ImageZoneSimpleData(594-149,233-41 , 639-149, 249-41);
    public static final ImageZoneSimpleData syringeInBag = new ImageZoneSimpleData(444-14,200-22 , 489-14, 200+22);
    public final static ImageZoneSimpleData grenadeLauncherBullet = new ImageZoneSimpleData(768,169,777,186);
    public final static ImageZoneSimpleData handGrenadeInWorldAsBullet = new ImageZoneSimpleData(768,187,776,198);
    public final static ImageZoneSimpleData handGrenadeInWorldAsAmmo = new ImageZoneSimpleData(782,174,801,193);
    //public final static ImageZoneSimpleData grenadeLauncherCompleteBullet = m79Ammo;
    public final static ImageZoneSimpleData grenadeLauncherSleeve = new ImageZoneSimpleData(766,143,779,158);
    public static final ImageZoneSimpleData shotgunSleeve = new ImageZoneSimpleData(701, 163,713, 188);
    public final static ImageZoneSimpleData simpleBullet = new ImageZoneSimpleData(743-2, 167-2,751+2, 175+2);

    public static final ImageZoneSimpleData sack = new ImageZoneSimpleData(704, 249,720, 267);

    // Money
    public final static ImageZoneSimpleData smallRedStone= new ImageZoneSimpleData(720,293,736,309);
    public final static ImageZoneSimpleData smallGreenStone= new ImageZoneSimpleData(704,293,704+16,309);
    public final static ImageZoneSimpleData smallBlueStone= new ImageZoneSimpleData(736,293,736+16,309);
    public final static ImageZoneSimpleData smallYellowStone= new ImageZoneSimpleData(752,293,768,309);
    public final static ImageZoneSimpleData smallWhiteStone= new ImageZoneSimpleData(768,293,784,309);

    public final static ImageZoneSimpleData bigGreenStone= new ImageZoneSimpleData(704,267,730,293);
    public final static ImageZoneSimpleData bigRedStone= new ImageZoneSimpleData(704+26,267,704+26*2,293);
    public final static ImageZoneSimpleData bigBlueStone= new ImageZoneSimpleData(704+26*2,267,704+26*3,293);
    public final static ImageZoneSimpleData bigYellowStone= new ImageZoneSimpleData(704+26*3,267,704+26*4,293);
    public final static ImageZoneSimpleData bigWhiteStone= new ImageZoneSimpleData(704+26*4,267,704+26*5,293);

    public final static ImageZoneSimpleData bigCoinGold = new ImageZoneSimpleData(703,217,719,233);
    public final static ImageZoneSimpleData smallCoinGold = new ImageZoneSimpleData(703,217+16,719,233+16);
    public final static ImageZoneSimpleData bigCoinSilver = new ImageZoneSimpleData(703+16,217,719+16,233);
    public final static ImageZoneSimpleData smallCoinSilver = new ImageZoneSimpleData(703+16,217+16,719+16,233+16);
    public final static ImageZoneSimpleData bigCoinCopper = new ImageZoneSimpleData(703+16*2,217,719+16*2,233);
    public final static ImageZoneSimpleData smallCoinCopper = new ImageZoneSimpleData(703+16*2,217+16,719+16*2,233+16);

    public final static ImageZoneSimpleData mediumCoinGold = new ImageZoneSimpleData(751,217,751+16,233);
    public final static ImageZoneSimpleData mediumCoinSilver = new ImageZoneSimpleData(751+16,217,751+2*16,233);
    public final static ImageZoneSimpleData mediumCoinCopper = new ImageZoneSimpleData(751+16*2,217,751+16*3,233);

    public final static ImageZoneSimpleData ananas = new ImageZoneSimpleData(405,409,425,442);
    public final static ImageZoneSimpleData carrot = new ImageZoneSimpleData(408,446,425,477);
    public final static ImageZoneSimpleData apple = new ImageZoneSimpleData(438,446,464,478);
    public final static ImageZoneSimpleData orange = new ImageZoneSimpleData(399,482,428,512);
    public final static ImageZoneSimpleData watermelon = new ImageZoneSimpleData(434,488,466,512);

    public final static ImageZoneSimpleData shotFlash = new ImageZoneSimpleData(402, 480, 432, 512);
    //public final static ImageZoneSimpleData whiteScreen = new ImageZoneSimpleData(0,0,500,500);
    public final static ImageZoneSimpleData whiteScreen = new ImageZoneSimpleData(32,217,34,219);
    public final static ImageZoneSimpleData beretImageZoneSimpleData = new ImageZoneSimpleData(138, 1007, 159, 1024);
    //public final static ImageZoneSimpleData mediumRedStone= new ImageZoneSimpleData(703,217,703+16,217+16);    //public final static ImageZoneSimpleData mediumGreenStone= new ImageZoneSimpleData(703,217,703+16,217+16);// public final static ImageZoneSimpleData mediumBlueStone= new ImageZoneSimpleData(703,217,703+16,217+16);//public final static ImageZoneSimpleData mediumYellowStone= new ImageZoneSimpleData(703,217,703+16,217+16);//public final static ImageZoneSimpleData mediumWhiteStone = new ImageZoneSimpleData(703,217,703+16,217+16);



    public static AnimationDataToStore stars;// = new AnimationDataToStore( HeadsUpDisplay.mainGraphicTileset.getPath(), 705, 309, 705+14*9, 309+14, 14,14,(byte)1,(byte)9, 11);

    //mainGraphicTileset.getPath(),
    //public AnimationDataToStore( int leftX, int upperY, int rightX, int lowerY, int graphicWidth, int graphicHeight, byte rowsNumber, byte collumnsNumber, int frequency){


    /*
    public final static byte SMALL_RED_STONE = 10;
    public final static byte MEDIUM_RED_STONE = 11;
    public final static byte BIG_RED_STONE = 12;

    public final static byte SMALL_GREEN_STONE = 13;
    public final static byte MEDIUM_GREEN_STONE = 14;
    public final static byte BIG_GREEN_STONE = 15;

    public final static byte SMALL_BLUE_STONE = 16;
    public final static byte MEDIUM_BLUE_STONE = 17;
    public final static byte BIG_BLUE_STONE = 18;

    public final static byte SMALL_YELLOW_STONE = 19;
    public final static byte MEDIUM_YELLOW_STONE = 20;
    public final static byte BIG_YELLOW_STONE = 21;

    public final static byte SMALL_WHITE_STONE = 22;
    public final static byte MEDIUM_WHITE_STONE = 23;
    public final static byte BIG_WHITE_STONE = 24;

    public final static byte SMALL_COIN_COPPER = 25;
    public final static byte BIG_COIN_COPPER = 26;

    public final static byte SMALL_COIN_SILVER = 27;
    public final static byte BIG_COIN_SILVER = 28;

    public final static byte SMALL_COIN_GOLD = 29;
    public final static byte BIG_COIN_GOLD = 30;
     */

    public static void init(){
        if (mainGraphicFile == null) {
            if (HUD_GraphicData.mainGraphicFile != null) {
                mainGraphicFile = HUD_GraphicData.mainGraphicFile;
            }
            else mainGraphicFile = new Image(Program.getAbsolutePathToAssetsFolder("HUD"+ TextureDecodeManager.getExtensionForGraphicSources()));

            //stars = new AnimationDataToStore(mainGraphicFile.path, 0, 0, 705+14*9, 309+14, 314,314,(byte)1,(byte)8, 6);
            //stars = new AnimationDataToStore(mainGraphicFile.path, 705, 309, 705+14*9, 309+14, 14,14,(byte)1,(byte)9, 6);
            stars = new AnimationDataToStore( mainGraphicFile.getPath(), 705, 309, 705+13*8, 309+14, 25,25,(byte)1,(byte)8, 10);

            System.out.println("Graphic data for int world objects was loaded. Path: " + mainGraphicFile.getPath() );
        }
        else {
            System.out.println("Graphic data already uploaded");
        }
    }

    public static ImageZoneSimpleData getGraphicDataForWeapon(WeaponType weaponType) {
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) return shotgunAmmoInWorld;
        else if (weaponType == WeaponType.M79) return m79AmmoInWorld;
        else if (weaponType == WeaponType.SMG) return smgAmmoInWorld;
        else if (weaponType == WeaponType.HANDGUN) return pistolAmmoInWorld;
        else if (weaponType == WeaponType.REVOLVER) return revolverAmmoInWorld;
        else if (weaponType == WeaponType.GRENADE) return handGrenadeInWorldAsAmmo;
        else{
            System.out.println("This ammo has no graphic");
            return null;
        }
    }

    public static ImageZoneSimpleData getImageZoneForBullet(int graphicType) {
        if (graphicType == MoveableSpritesAddingController.HANDGUN_SLEEVE){
            return InWorldObjectsGraphicData.simpleBullet;
        }
        else {
            System.out.println("No bullets data for graphic " + graphicType);
            return simpleBullet;
        }
    }

}
