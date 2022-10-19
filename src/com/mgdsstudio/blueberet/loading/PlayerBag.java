package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.MedicalKit;
import com.mgdsstudio.blueberet.gameobjects.persons.Human;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.AddingNewCollectableObjectAction;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;

import java.util.ArrayList;

public class PlayerBag {
    private final boolean allWeaponsOpened = true;
    private boolean isRevolverOnPanel = true;
    private boolean isHandgunOnPanel = false;
    private boolean isSO_ShotgunOnPanel = false;
    private boolean isShotgunOnPanel = true;
    private boolean isGrenadeOnPanel = false;
    private boolean isGrenadeLauncherOnPanel = true;
    private boolean isSMG_OnPanel = true;

    private int inShotgunAmmo, inPistolAmmo, inM79Ammo, inSMGAmmo;
    private int inSO_ShotgunAmmo, inRevolverAmmo, inGrenadeAmmo;
    private int ammoForShotgun, ammoForPistol, ammoForM79, ammoForSMG;
    private int ammoForRevolver, ammoForGrenade;
    private int maxAmmoForShotgun = 21, maxAmmoForPistol = 8, maxAmmoForM79 = 10, maxAmmoForSMG = 8;
    private int maxAmmoForRevolver = 30, maxAmmoForGrenade = 6;

    private int maxSmallMedicalKits = 5, maxMediumMedicalKits = 3, maxLargeMedicalKits = 1, maxSyringes = 3;
    //private int largeMedicalKits, mediumMedicalKits, smallMedicalKits;
    //private HashMap <Integer, Byte> medicalKits;
    private int money;
    //private int syringes;
    private ArrayList <SelectableInBagObject> selectableInBagObjects;
    private int objectOnMainFrame = -1;

    private ArrayList <WeaponType> weaponsOnUpperPanel;
    private ArrayList <WeaponType> weapons;
    private ArrayList < WeaponType> deblockedWeapons;
    //private final static String PATH = "Player bag.txt";

    public PlayerBag(boolean levelType, Human soldier){
        selectableInBagObjects = new ArrayList<>(3);
        deblockedWeapons = new ArrayList<>();
        createClearParameters();
        //deblockedWeapons.add(WeaponType.REVOLVER);
        /*if (tryToLoadDataFromFile(levelType, soldier) == false){ // not implemented
            //createStartDebugParameters();
            //System.out.println("Start debug parameters will be used");
        }*/

    }

    public void setWeaponParameters(int ammoForShotgun, int ammoForPistol, int ammoForM79, int ammoForSMG, int ammoForRevolver, int ammoForGrenades, int maxAmmoForShotgun, int maxAmmoForPistol, int maxAmmoForM79, int maxAmmoForSMG, int maxAmmoForRevolver, int maxAmmoForHandGrenades) {
        this.ammoForShotgun = ammoForShotgun;
        this.ammoForPistol = ammoForPistol;
        this.ammoForM79 = ammoForM79;
        this.ammoForSMG = ammoForSMG;
        this.ammoForRevolver = ammoForRevolver;
        this.ammoForGrenade = ammoForGrenades;
        this.maxAmmoForShotgun = maxAmmoForShotgun;
        this.maxAmmoForPistol = maxAmmoForPistol;
        this.maxAmmoForM79 = maxAmmoForM79;
        this.maxAmmoForSMG = maxAmmoForSMG;
        this.maxAmmoForRevolver = maxAmmoForRevolver;
        this.maxAmmoForGrenade = maxAmmoForHandGrenades;
    }
    /*
    public void setWeaponParameters(int ammoForShotgun, int ammoForPistol, int ammoForM79, int ammoForSMG, int maxAmmoForShotgun, int maxAmmoForPistol, int maxAmmoForM79, int maxAmmoForSMG) {
        this.ammoForShotgun = ammoForShotgun;
        this.ammoForPistol = ammoForPistol;
        this.ammoForM79 = ammoForM79;
        this.ammoForSMG = ammoForSMG;
        this.maxAmmoForShotgun = maxAmmoForShotgun;
        this.maxAmmoForPistol = maxAmmoForPistol;
        this.maxAmmoForM79 = maxAmmoForM79;
        this.maxAmmoForSMG = maxAmmoForSMG;
    }
     */


    private void saveActualParametersToDataFile() {
    }

    private void createClearParameters() {
        weaponsOnUpperPanel = new ArrayList<>();
        weapons = new ArrayList<>();
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.LARGE_MEDICAL_KIT, 0));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.MEDIUM_MEDICAL_KIT, 0));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.SMALL_MEDICAL_KIT, 0));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.SYRINGE,0));
    }

    private void createStartDebugParameters() {
        weaponsOnUpperPanel = new ArrayList<>();
        weapons = new ArrayList<>();
        if (allWeaponsOpened){
            weapons.add(WeaponType.HANDGUN);
            weapons.add(WeaponType.SHOTGUN);
            weapons.add(WeaponType.M79);
            weapons.add(WeaponType.SMG);

            weapons.add(WeaponType.SAWED_OFF_SHOTGUN);
            weapons.add(WeaponType.REVOLVER);
            weapons.add(WeaponType.GRENADE);

            if (isHandgunOnPanel) weaponsOnUpperPanel.add(WeaponType.HANDGUN);
            if (isRevolverOnPanel) weaponsOnUpperPanel.add(WeaponType.REVOLVER);

            if (isShotgunOnPanel) weaponsOnUpperPanel.add(WeaponType.SHOTGUN);
            if (isSO_ShotgunOnPanel) weaponsOnUpperPanel.add(WeaponType.SAWED_OFF_SHOTGUN);

            if (isGrenadeLauncherOnPanel) weaponsOnUpperPanel.add(WeaponType.M79);
            if (isGrenadeOnPanel) weaponsOnUpperPanel.add(WeaponType.GRENADE);

            if (isSMG_OnPanel) weaponsOnUpperPanel.add(WeaponType.SMG);
        }
        else {
            weapons.add(WeaponType.HANDGUN);
            weapons.add(WeaponType.SHOTGUN);
            weapons.add(WeaponType.M79);
            weapons.add(WeaponType.SMG);
            for (WeaponType weaponType : weapons){
                weaponsOnUpperPanel.add(weaponType);
            }
        }

        setWeaponParameters(14, 4,6,5, 18, 4,28,12,20,12, 18,8);
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.LARGE_MEDICAL_KIT, 1));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.MEDIUM_MEDICAL_KIT, 2));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.SMALL_MEDICAL_KIT, 3));
        selectableInBagObjects.add(new SelectableInBagObject(AbstractCollectable.SYRINGE,1));
        /*
        largeMedicalKits = 1;
        mediumMedicalKits = 2;
        smallMedicalKits = 3;*/
        fillInBarrelAmmoData();
        //medicalKits = new HashMap<>();
        money = 0;
        objectOnMainFrame = AbstractCollectable.SMALL_MEDICAL_KIT;
        deblockedWeapons.add(WeaponType.REVOLVER);
        //saveData();
    }

    private void saveData() {

    }

    private void fillInBarrelAmmoData() {
        inShotgunAmmo = FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY;
        inM79Ammo = FirearmsWeapon.GRENADE_LAUNCHER_MAGAZINE_CAPACITY;
        inPistolAmmo = FirearmsWeapon.PISTOLE_MAGAZINE_CAPACITY;
        inSMGAmmo = FirearmsWeapon.SMG_MAGAZINE_CAPACITY;

        inRevolverAmmo = FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY;
        inSO_ShotgunAmmo = FirearmsWeapon.SO_MAGAZINE_CAPACITY;
        inGrenadeAmmo = FirearmsWeapon.HAND_GRENADE_MAGAZINE_CAPACITY;
    }

    /*
    private void fillInBarrelAmmoData() {
        inShotgunAmmo = FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY;
        inM79Ammo = FirearmsWeapon.GREENADE_LAUNCHER_MAGAZINE_CAPACITY;
        inPistolAmmo = FirearmsWeapon.PISTOLE_MAGAZINE_CAPACITY;
        inSMGAmmo = FirearmsWeapon.SMG_MAGAZINE_CAPACITY;


    }
     */

    /*
    private void addAmmo(WeaponType weaponType){
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) {
            ammoForShotgun++;
            if (ammoForShotgun>maxAmmoForShotgun) ammoForShotgun = maxAmmoForShotgun;
        }
        else if (weaponType == WeaponType.REVOLVER) {
            ammoForRevolver++;
            if (ammoForRevolver>maxAmmoForRevolver) ammoForRevolver = maxAmmoForRevolver;
        }
        else if (weaponType == WeaponType.HANDGUN) {
            ammoForPistol++;
            if (ammoForPistol>maxAmmoForPistol) ammoForPistol = maxAmmoForPistol;
        }
        else if (weaponType == WeaponType.M79) {
            ammoForM79++;
            if (ammoForM79>maxAmmoForM79) ammoForM79 = maxAmmoForM79;
        }
        else if (weaponType == WeaponType.GRENADE) {
            ammoForGrenade++;
            if (ammoForGrenade>maxAmmoForGrenade) ammoForGrenade = maxAmmoForGrenade;
        }
        else if (weaponType == WeaponType.SMG) {
            ammoForSMG++;
            if (ammoForSMG>maxAmmoForSMG) ammoForSMG = maxAmmoForSMG;
        }
    }
*/
    public void addAmmo(WeaponType weaponType, int valueToBeAdded){
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) {
            ammoForShotgun+=valueToBeAdded;
            if (ammoForShotgun>maxAmmoForShotgun) ammoForShotgun = maxAmmoForShotgun;
        }
        else if (weaponType == WeaponType.REVOLVER) {
            ammoForRevolver+=valueToBeAdded;
            if (ammoForRevolver>maxAmmoForRevolver) ammoForRevolver = maxAmmoForRevolver;
        }
        else if (weaponType == WeaponType.HANDGUN) {
            ammoForPistol+=valueToBeAdded;
            if (ammoForPistol>maxAmmoForPistol) ammoForPistol = maxAmmoForPistol;
        }
        else if (weaponType == WeaponType.M79) {
            ammoForM79+=valueToBeAdded;
            if (ammoForM79>maxAmmoForM79) ammoForM79 = maxAmmoForM79;
        }
        else if (weaponType == WeaponType.GRENADE) {
            ammoForGrenade+=valueToBeAdded;
            if (ammoForGrenade>maxAmmoForGrenade) ammoForGrenade = maxAmmoForGrenade;
        }
        else if (weaponType == WeaponType.SMG) {
            ammoForSMG+=valueToBeAdded;
            if (ammoForSMG>maxAmmoForSMG) ammoForSMG = maxAmmoForSMG;
        }
    }

    public boolean getOneAmmo(WeaponType weaponType){
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) {
            if (ammoForShotgun<1) return false;
            else ammoForShotgun--;
        }
        else if (weaponType == WeaponType.REVOLVER) {
            if (ammoForRevolver<1) return false;
            else ammoForRevolver--;
        }
        else if (weaponType == WeaponType.HANDGUN) {
            if (ammoForPistol<1) return false;
            else ammoForPistol--;
        }
        else if (weaponType == WeaponType.M79) {
            if (ammoForM79<1) return false;
            else ammoForM79--;
        }
        else if (weaponType == WeaponType.GRENADE) {
            if (ammoForGrenade<1) return false;
            else ammoForGrenade--;
        }
        else if (weaponType == WeaponType.SMG) {
            if (ammoForSMG<1) return false;
            else ammoForSMG--;
        }
        return true;
    }

    public boolean areThereAmmoForWeapon(WeaponType weaponType){
        if (weaponType == WeaponType.SMG && ammoForSMG > 0) return true;
        else if (weaponType == WeaponType.M79 && ammoForM79 >0) return  true;
        else if (weaponType == WeaponType.GRENADE && ammoForGrenade >0) return  true;
        else if (weaponType == WeaponType.REVOLVER && ammoForRevolver >0) return true;
        else if (weaponType == WeaponType.HANDGUN && ammoForPistol >0) return true;
        else if (weaponType == WeaponType.SHOTGUN && ammoForShotgun > 0 ) return  true;
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN && ammoForShotgun > 0 ) return  true;
        else {
            return false;
        }
    }

    public int getRestAmmoForWeapon(WeaponType weaponType){
        if (weaponType == WeaponType.M79) return ammoForM79;
        else if (weaponType == WeaponType.SMG) return ammoForSMG;
        else if (weaponType == WeaponType.HANDGUN) return ammoForPistol;
        else if (weaponType == WeaponType.REVOLVER) return ammoForRevolver;
        else if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) return ammoForShotgun;
        else if (weaponType == WeaponType.GRENADE) return ammoForGrenade;
        else {
            System.out.println("This weapon is not known");
            return  -1;
        }
    }



    public int getMaxAmmoForWeapon(WeaponType weaponType){
        if (weaponType == WeaponType.M79) return maxAmmoForM79;
        else if (weaponType == WeaponType.SMG) return maxAmmoForSMG;
        else if (weaponType == WeaponType.REVOLVER) return maxAmmoForRevolver;
        else if (weaponType == WeaponType.HANDGUN) return maxAmmoForPistol;
        else if (weaponType == WeaponType.GRENADE) return maxAmmoForGrenade;
        else if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN) return maxAmmoForShotgun;
        else {
            System.out.println("This weapon with type " + weaponType + " is not known. Problem in bag");
            return  -1;
        }
    }

    public int getMaxObjects(int objectType){
        if (objectType == AbstractCollectable.LARGE_MEDICAL_KIT) return maxLargeMedicalKits;
        else if (objectType == AbstractCollectable.MEDIUM_MEDICAL_KIT) return maxMediumMedicalKits;
        else if (objectType == AbstractCollectable.SMALL_MEDICAL_KIT) return maxSmallMedicalKits;
        else if (objectType == AbstractCollectable.SYRINGE) return maxSyringes;
        else {
            System.out.println("This object with code " + objectType + " is not known. Problem in bag. I dont know which value for this is max");
            return  1;
        }
    }


    private boolean tryToLoadDataFromFile(boolean levelType, Soldier soldier) {
        /*
        String path = null;
        if (Program.OS == Program.ANDROID){
            path =  AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+ PlayerDataController.fileNameForLocalSavingInCompany;
            if (levelType == ExternalRoundDataFileController.MAIN_LEVELS){
                path =  AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+ PlayerDataController.fileNameForGlobalSavingSingleLevels;
            }
        }
        else if (Program.OS == Program.DESKTOP){
            path =  Program.getAbsolutePathToAssetsFolder(PlayerDataController.fileNameForLocalSavingInCompany);
            if (levelType == ExternalRoundDataFileController.MAIN_LEVELS){
                path =  AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+ PlayerDataController.fileNameForGlobalSavingSingleLevels;
            }
        }
        if (path != null){
            File file = new File(path);
            if (!file.exists()){
                System.out.println("File " + path + "  doesnot exist");
                return false;
            }
            else {
                System.out.println("Try to load data from file: " + path);
                //public PlayerDataLoadMaster(Soldier soldier, boolean global, boolean levelType) {
                //PlayerDataLoadMaster playerDataLoadMaster = new PlayerDataLoadMaster(soldier, true, levelType);
            }
        }
        */


        /*
        String pathToFile = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+ PlayerDataController.fileNameForCompany;
        File file = new File(pathToFile);
        if (file.exists()){
            System.out.println("Data file exists");
        }*/
        return false;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int money){
        this.money+=money;
    }

    /*
    public void confiscateMoney(int moneyToBeConfiscate){
        money-=moneyToBeConfiscate;
        if (money<0) {
            money = 0;
            System.out.println("Player has not enough money to confiscate");
        }
    }*/

    public int getInBarrelAmmoForWeapon(WeaponType weaponType){
        if (weaponType == WeaponType.SMG) return inSMGAmmo;
        else if (weaponType == WeaponType.HANDGUN) return inPistolAmmo;
        else if (weaponType == WeaponType.SHOTGUN) return inShotgunAmmo;
        else if (weaponType == WeaponType.M79) return inM79Ammo;
        else if (weaponType == WeaponType.GRENADE) return inGrenadeAmmo;
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return inSO_ShotgunAmmo;
        else if (weaponType == WeaponType.REVOLVER) return inRevolverAmmo;
        else {
            System.out.println("This weapon is not known. The type: " + weaponType ) ;
            return -1;
        }
    }

    public static String getNameForWeaponType(WeaponType weapon){
        if (weapon == WeaponType.HANDGUN) return AddingNewCollectableObjectAction.HANDGUN;
        else if (weapon == WeaponType.SHOTGUN) return AddingNewCollectableObjectAction.SHOTGUN;
        else if (weapon == WeaponType.M79) return AddingNewCollectableObjectAction.M79;
        else if (weapon == WeaponType.SMG) return AddingNewCollectableObjectAction.SMG;
        else if (weapon == WeaponType.REVOLVER) return AddingNewCollectableObjectAction.REVOLVER;
        else if (weapon == WeaponType.SAWED_OFF_SHOTGUN) return AddingNewCollectableObjectAction.SO_SHOTGUN;
        else if (weapon == WeaponType.GRENADE) return AddingNewCollectableObjectAction.HAND_GRENADE;
        else return "No data";
    }

    public static WeaponType getWeaponTypeForName(String name){
        if (name == AddingNewCollectableObjectAction.HANDGUN || name.equals(AddingNewCollectableObjectAction.HANDGUN)) return WeaponType.HANDGUN;
        else if (name == AddingNewCollectableObjectAction.SHOTGUN || name.equals(AddingNewCollectableObjectAction.SHOTGUN)) return WeaponType.SHOTGUN;
        else if (name == AddingNewCollectableObjectAction.SMG || name.equals(AddingNewCollectableObjectAction.SMG)) return WeaponType.SMG;
        else if (name == AddingNewCollectableObjectAction.M79 || name.equals(AddingNewCollectableObjectAction.M79)) return WeaponType.M79;
        else if (name == AddingNewCollectableObjectAction.HAND_GRENADE || name.equals(AddingNewCollectableObjectAction.HAND_GRENADE)) return WeaponType.GRENADE;
        else if (name == AddingNewCollectableObjectAction.REVOLVER || name.equals(AddingNewCollectableObjectAction.REVOLVER)) return WeaponType.REVOLVER;
        else if (name == AddingNewCollectableObjectAction.SO_SHOTGUN || name.equals(AddingNewCollectableObjectAction.SO_SHOTGUN)) return WeaponType.SAWED_OFF_SHOTGUN;
        else return null;
    }

    public void setInBarrelAmmoForWeapons(Soldier soldier){
        ArrayList <FirearmsWeapon> weapons = soldier.weapons;
        for (FirearmsWeapon weapon : weapons){
            if (weapon.getWeaponType() == WeaponType.HANDGUN){
                inPistolAmmo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.SMG){
                inSMGAmmo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.SHOTGUN){
                inShotgunAmmo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.M79){
                inM79Ammo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.SAWED_OFF_SHOTGUN){
                inSO_ShotgunAmmo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.REVOLVER){
                inRevolverAmmo = weapon.getRestBullets();
            }
            else if (weapon.getWeaponType() == WeaponType.GRENADE){
                inGrenadeAmmo = weapon.getRestBullets();
            }
        }
    }


    public void addMedicalKit(AbstractCollectable collectableObject) {
        MedicalKit medicalKit = (MedicalKit) collectableObject;
        selectableInBagObjects.add(new SelectableInBagObject(medicalKit.getType(), 1));
    }

    public void addSelectableObject(AbstractCollectable collectableObject) {
        //MedicalKit medicalKit = (MedicalKit) collectableObject;
        selectableInBagObjects.add(new SelectableInBagObject(collectableObject.getType(), 1));
    }


    public int getMedicalKitsNumber(int type) {
        return getSelectableObjectsNumberByType(type);
        /*for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getType() == type) return object.getNumber();
        }
        System.out.println("There are no objects with this type");
        return -1;
        */
    }

    public int getSelectableObjectsNumberByType(int type) {
        for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getType() == type) return object.getNumber();
        }
        System.out.println("There are no objects with this type");
        return -1;
    }

    public int getLargeMedicalKitsNumber() {
        return getMedicalKitsNumber(AbstractCollectable.LARGE_MEDICAL_KIT);
    }

    public int getMediumMedicalKitsNumber() {
        return getMedicalKitsNumber(AbstractCollectable.MEDIUM_MEDICAL_KIT);
    }

    public int getSmallMedicalKitsNumber() {
        return getMedicalKitsNumber(AbstractCollectable.SMALL_MEDICAL_KIT);
    }

    public void setMedicalKitsNumber(int type, int number) {
        boolean founded = false;
        for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getType() == type) {
                object.setNumber(number);
                founded = true;
            }
        }
        System.out.println("Founded in bag  " + founded + " for type: " + type + " . I have only " + selectableInBagObjects.size() + " objects");
    }

    public void setMediumMedicalKitsNumber(int kits) {
        setMedicalKitsNumber(AbstractCollectable.MEDIUM_MEDICAL_KIT, kits);
    }

    public void setSmallMedicalKitsNumber(int kits) {
        setMedicalKitsNumber(AbstractCollectable.SMALL_MEDICAL_KIT, kits);
    }

    public void setLargeMedicalKitsNumber(int kits) {
        setMedicalKitsNumber(AbstractCollectable.LARGE_MEDICAL_KIT, kits);
    }

    public void setSyringesNumber(int kits){
        setCollectableObjectsNumber(AbstractCollectable.SYRINGE, kits);
    }

    public void setCollectableObjectsNumber(int type, int number) {
        for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getType() == type) object.setNumber(number);
        }
    }

    public void confiscateCollectableObject(int type){
        for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getType() == type) {
                int actualNumber = object.getNumber();
                if (actualNumber >= 2){
                    object.setNumber(actualNumber-1);
                }
                else selectableInBagObjects.remove(object);
                break;
            }
        }
    }



    public boolean isWeaponDeblocked(WeaponType weaponType){
        for (WeaponType deblocked : deblockedWeapons){
            if (deblocked == weaponType){
                return true;
            }
        }
        return false;
        //if (weaponType == WeaponType.GRENADE)
        //return true;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setParametersForWeapon(WeaponType weaponType, int bullets, int magazines) {
        if (weaponType == WeaponType.HANDGUN){
            inPistolAmmo = bullets;
            ammoForPistol = magazines;
        }
        else if (weaponType == WeaponType.SHOTGUN){
            inShotgunAmmo = bullets;
            ammoForShotgun = magazines;
        }
        else if (weaponType == WeaponType.M79){
            inM79Ammo = bullets;
            ammoForM79 = magazines;
        }
        else if (weaponType == WeaponType.SMG){
            inSMGAmmo = bullets;
            ammoForSMG = magazines;
        }
        else if (weaponType == WeaponType.REVOLVER){
            inRevolverAmmo = bullets;
            ammoForRevolver = magazines;
        }
        else if (weaponType == WeaponType.GRENADE){
            inGrenadeAmmo = bullets;
            ammoForGrenade = magazines;
        }
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN){
            inSO_ShotgunAmmo = bullets;
            ammoForShotgun = magazines;
        }
        else {
            System.out.println("this weapon is not knew");
        }
    }

    public void setParametersForWeaponByString(String weaponName, int bullets, int magazines) {
        WeaponType weaponType = getWeaponTypeForName(weaponName);
        System.out.println("For weapon " + weaponName + " must be set " + magazines + " magazines");
        setParametersForWeapon(weaponType, bullets, magazines);
    }

    public ArrayList<SelectableInBagObject> getSelectableInBagObjects() {
        //System.out.println("Kits: " + selectableInBagObjects.get(0).getNumber());
        return selectableInBagObjects;
    }

    public int getSelectableInBagObjectsTypesNumber() {
        int number = 0;
        System.out.println("Player has " + selectableInBagObjects.size() + " objects in bag");
        for (SelectableInBagObject object : selectableInBagObjects){
            if (object.getNumber()>0) number++;
        }
        return number;
    }

    /*
    public boolean isWeaponActual(){

    }*/

    public int getObjectOnMainFrame() {
        System.out.println("Actual selected: " + objectOnMainFrame);
        return objectOnMainFrame;
    }

    public void setObjectOnMainFrame(int objectOnMainFrame) {
        this.objectOnMainFrame = objectOnMainFrame;
        System.out.println("Main object in bag set on "+ objectOnMainFrame);
    }

    public void confiscateObject(int objectType) {
        if (objectType == AbstractCollectable.SMALL_MEDICAL_KIT || objectType == AbstractCollectable.MEDIUM_MEDICAL_KIT || objectType == AbstractCollectable.LARGE_MEDICAL_KIT || objectType == AbstractCollectable.SYRINGE){
            confiscateCollectableObject(objectType);
        }
        else {
            System.out.println("this object type for confiscate is not known");

        }
    }

    public boolean areThereObjectsForType(int objectType) {
        if (objectType == AbstractCollectable.SMALL_MEDICAL_KIT || objectType == AbstractCollectable.MEDIUM_MEDICAL_KIT || objectType == AbstractCollectable.LARGE_MEDICAL_KIT || objectType == AbstractCollectable.SYRINGE){
            if (getSelectableObjectsNumberByType(objectType)>0){    //getMedicalKitsNumber(objectType)
                System.out.println("Player has yet " + getSelectableObjectsNumberByType(objectType) + " objects for type "+  objectType);
                return true;
            }
        }
        System.out.println("Player has not more objects of type " + objectType);
        return false;
    }

    public void updateActualWeaponsData(Soldier soldier) {
        ArrayList <FirearmsWeapon> weapons = soldier.getWeapons();
        for (FirearmsWeapon firearmsWeapon : weapons){
            WeaponType type = firearmsWeapon.getWeaponType();
            if (type == WeaponType.HANDGUN){
                inPistolAmmo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.SHOTGUN){
                inShotgunAmmo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.SMG){
                inSMGAmmo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.M79){
                inM79Ammo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.REVOLVER){
                inRevolverAmmo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.SAWED_OFF_SHOTGUN){
                inSO_ShotgunAmmo = firearmsWeapon.getRestBullets();
            }
            else if (type == WeaponType.GRENADE){
                inGrenadeAmmo = firearmsWeapon.getRestBullets();
            }
            else {
                System.out.println("This weapon type is not knew");
            }
        }
    }

    public boolean getOnVisiblePanel(WeaponType weaponType) {
        if (weaponType == WeaponType.REVOLVER) return isRevolverOnPanel;
        else if (weaponType == WeaponType.HANDGUN) return isHandgunOnPanel;
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return isSO_ShotgunOnPanel;
        else if (weaponType == WeaponType.SHOTGUN) return isShotgunOnPanel;
        else if (weaponType == WeaponType.GRENADE) return isGrenadeOnPanel;
        else if (weaponType == WeaponType.M79) return isGrenadeLauncherOnPanel;
        else if (weaponType == WeaponType.SMG) return isSMG_OnPanel;
        else {
            System.out.println("This weapon is not knew");
            return false;
        }
    }

    public ArrayList<WeaponType> getWeaponsOnUpperPanel() {
        return weaponsOnUpperPanel;
    }

    public ArrayList<WeaponType> getWeapons() {
        return weapons;
    }

    public void setVisibleWeaponsOnWeaponPanel(ArrayList<WeaponType> weaponsOnPanel) {
        setAllWeaponsAsNotOnPanel();
        weaponsOnUpperPanel.clear();
        for (WeaponType weaponType : weaponsOnPanel){
            if (weaponType == WeaponType.REVOLVER) isRevolverOnPanel = true;
            else if (weaponType == WeaponType.HANDGUN) isHandgunOnPanel = true;
            else if (weaponType == WeaponType.SHOTGUN) isShotgunOnPanel = true;
            else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) isSO_ShotgunOnPanel = true;
            else if (weaponType == WeaponType.M79) isGrenadeLauncherOnPanel = true;
            else if (weaponType == WeaponType.GRENADE) isGrenadeOnPanel = true;
            else if (weaponType == WeaponType.SMG) isSMG_OnPanel = true;
            weaponsOnUpperPanel.add(weaponType);
        }
    }

    private void setAllWeaponsAsNotOnPanel() {
        isRevolverOnPanel = false;
        isHandgunOnPanel = false;
        isShotgunOnPanel = false;
        isSO_ShotgunOnPanel = false;
        isGrenadeLauncherOnPanel = false;
        isGrenadeOnPanel = false;
        isSMG_OnPanel = false;
    }


    public void setDeblockedWeapons(ArrayList<WeaponType> deblockedWeapons) {
        this.deblockedWeapons = deblockedWeapons;
    }


    /*
    public ArrayList<FirearmsWeapon> getDeblockedWeapons() {
        ArrayList<FirearmsWeapon> weaponsToBeDeblocked = new ArrayList<>();
        for (WeaponType weaponType : deblockedWeapons){
            FirearmsWeapon weapon = new FirearmsWeapon(FirearmsWeapon.getWeaponCodeForType(weaponType), );
            weaponsToBeDeblocked.add(weapon);
        }
        return weaponsToBeDeblocked;
    }*/

    public void updateObjectsNumber() {

    }

    public int getMaxAmmoInBarrelForWeapon(WeaponType weaponType) {
        if (weaponType == WeaponType.M79) return FirearmsWeapon.GRENADE_LAUNCHER_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.SMG) return FirearmsWeapon.SMG_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.REVOLVER) return FirearmsWeapon.REVOLVER_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.HANDGUN) return FirearmsWeapon.PISTOLE_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.GRENADE) return FirearmsWeapon.HAND_GRENADE_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.SHOTGUN) return FirearmsWeapon.SHOTGUN_MAGAZINE_CAPACITY;
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return FirearmsWeapon.SO_MAGAZINE_CAPACITY;
        else {
            System.out.println("This weapon with type " + weaponType + " is not known. Problem in bag");
            return  -1;
        }
    }
}
