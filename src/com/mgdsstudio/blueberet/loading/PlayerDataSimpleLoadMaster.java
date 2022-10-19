package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.data.JSONObject;

import java.util.ArrayList;

public class PlayerDataSimpleLoadMaster extends PlayerDataController implements PlayerSavingDataConstants{
    private int money = -1;
    private ArrayList <WeaponType> deblockedWeaponsList;
    public PlayerDataSimpleLoadMaster(){
        levelType = ExternalRoundDataFileController.MAIN_LEVELS;
        createPath(ExternalRoundDataFileController.MAIN_LEVELS);
        data = Program.engine.loadJSONArray(pathToFile);
        deblockedWeaponsList = new ArrayList<>();
    }

    public void loadData(){
        for (int i = 0; i < data.size(); i++){
            JSONObject object = data.getJSONObject(i);
            String name = object.getString(TYPE);
            if (name == MONEY || name.equals(MONEY)){
                int moneyValue = object.getInt(MONEY);
                money = moneyValue;
                System.out.println("Money recovered up to: " + moneyValue);
            }
            else if (name == WEAPON || name.equals(WEAPON)){
                int bullets = object.getInt(BULLETS);
                int magazines = object.getInt(MAGAZINES);
                boolean isActual = object.getBoolean(WEAPON_IS_ACTUAL);
                boolean deblocked = object.getBoolean(WEAPON_IS_DEBLOCKED);
                String weaponName = object.getString(WEAPON);
                if (deblocked){
                    deblockedWeaponsList.add(FirearmsWeapon.getWeaponTypeForName(weaponName));
                }

                System.out.println("Weapon " + weaponName + " recovered up to : " + bullets + " and " + magazines + " and deblocked " + deblocked);

            }
            else if (name == MEDICAL_KIT || name.equals(MEDICAL_KIT)){
                int larges = object.getInt(LARGE);
                int mediums = object.getInt(MEDIUM);
                int smalls = object.getInt(SMALL);
                int syringes = object.getInt(SYRINGE);
                System.out.println("Medical kits were recovered: " + larges + "; " + mediums + "; " + smalls + " and syringes " + syringes);
            }
            else if (name == PLAYER_PARAMETERS || name.equals(PLAYER_PARAMETERS)){
                int life = object.getInt(ACTUAL_LIFE);
                int maxLife = object.getInt(MAX_LIFE);
                boolean orient = object.getBoolean(VIEW_DIRECTION);
                int selectedMainObject = object.getInt(MAIN_SELECTED_OBJECT);
                System.out.println("Main selected object: " + selectedMainObject);
            }
        }
    }

    public int getMoney() {

        return money;
    }

    public boolean hasPlayerDeblockedWeapon(String name) {
        WeaponType weaponType = FirearmsWeapon.getWeaponTypeForName(name);
        return hasPlayerDeblockedWeapon(weaponType);
/*
        for (WeaponType weaponType : deblockedWeaponsList){
            if (weaponType==FirearmsWeapon.getWeaponTypeForName(name)){
                System.out.println("Player deblocked this weapon " + name);
                return true;
            }
        }
        System.out.print("Weapon " + name + " is not deblocked in list: ");
        for (WeaponType weaponType : deblockedWeaponsList) System.out.print(weaponType + ", ");
        System.out.println();
        return false;
        */
    }

    public boolean hasPlayerDeblockedWeapon(WeaponType weaponTypeToBeFind) {
        for (WeaponType weaponType : deblockedWeaponsList){
            if (weaponType==weaponTypeToBeFind){
                System.out.println("Player deblocked this weapon " + weaponTypeToBeFind);
                return true;
            }
        }
        System.out.print("Weapon " + weaponTypeToBeFind + " is not deblocked in list: ");
        for (WeaponType weaponType : deblockedWeaponsList) System.out.print(weaponType + ", ");
        System.out.println();
        return false;
    }

    public ArrayList<WeaponType> getDeblockedWeaponsList() {
        return deblockedWeaponsList;
    }

    public int getMagazinesForAmmo(WeaponType weaponType) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            if (type != null && type.contains(WEAPON)) {
                String name = jsonObject.getString(WEAPON);
                if (name != null && FirearmsWeapon.getWeaponTypeForName(name) == weaponType) {
                    int magazines = jsonObject.getInt(MAGAZINES);
                    System.out.println("Weapon has " + magazines );
                    return magazines;
                }
            }
        }
        System.out.println("Can not determine magazines count ");
        return 99;
    }

    public boolean hasFullMagazines(WeaponType weaponTypeForAmmoName) {
        int magazines = getMagazinesForAmmo(weaponTypeForAmmoName);
        PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
        int max = playerBag.getMaxAmmoForWeapon(weaponTypeForAmmoName);
        if (magazines >= max) return  true;
        else return false;
    }

    public int getObjectsCount(int collectableObjectCode) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            if (type != null && type.contains(MEDICAL_KIT)) {
                int itemCount = jsonObject.getInt(getStringForAbstractCollectableCode(collectableObjectCode));
                System.out.println("Weapon has " + itemCount );
                return itemCount;
            }
        }
        System.out.println("Can not determine item count ");
        return 0;
    }

    private String getStringForAbstractCollectableCode(int code){
        if (code == AbstractCollectable.SMALL_MEDICAL_KIT) return SMALL;
        else if (code == AbstractCollectable.MEDIUM_MEDICAL_KIT) return MEDIUM;
        else if (code == AbstractCollectable.LARGE_MEDICAL_KIT) return LARGE;
        else if (code == AbstractCollectable.SYRINGE) return SYRINGE;
        else {
            System.out.println("No data for object " + code);
            return SMALL;
        }
    }

    public boolean hasFullItems(int code) {
        int getActualObjects = getObjectsCount(code);
        PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);
        int max = playerBag.getMaxObjects(code);
        System.out.println("Max objects count: " + max);
        if (getActualObjects >= max) return  true;
        else return false;
    }
}
