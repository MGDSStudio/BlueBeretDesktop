package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class PlayerDataSaveMaster extends PlayerDataController implements PlayerSavingDataConstants {

    public PlayerDataSaveMaster(Soldier soldier, boolean global, boolean levelType) {
        this.soldier = soldier;
        this.levelType = levelType;
        createPath(global);
        bag = soldier.getPlayerBag();
        bag.setInBarrelAmmoForWeapons(soldier);
        data = new JSONArray();
    }

    public void saveData(){
        saveMoney();
        saveWeapons();
        saveCollectable();
        saveAnotherPlayerParameters();
        saveOnDisk();
    }

    private void saveAnotherPlayerParameters() {
        JSONObject playerData = new JSONObject();
        playerData.setString(TYPE, PLAYER_PARAMETERS);
        playerData.setInt(MAX_LIFE, soldier.getMaxLife());
        playerData.setInt(ACTUAL_LIFE, soldier.getLife());
        playerData.setBoolean(VIEW_DIRECTION, soldier.orientation);
        playerData.setInt(MAIN_SELECTED_OBJECT, bag.getObjectOnMainFrame());
        data.setJSONObject(count, playerData);
        count++;
    }

    private void saveCollectable() {
        JSONObject kitObject = new JSONObject();
        kitObject.setString(TYPE, MEDICAL_KIT);
        kitObject.setInt(LARGE, bag.getLargeMedicalKitsNumber());
        kitObject.setInt(MEDIUM, bag.getMediumMedicalKitsNumber());
        kitObject.setInt(SMALL, bag.getSmallMedicalKitsNumber());
        kitObject.setInt(SYRINGE, bag.getSelectableObjectsNumberByType(AbstractCollectable.SYRINGE));
        data.setJSONObject(count, kitObject);
        count++;
    }

    protected void saveMoney() {
        JSONObject moneyObject = new JSONObject();
        moneyObject.setString(TYPE, MONEY);
        moneyObject.setInt(MONEY, bag.getMoney());
        data.setJSONObject(count, moneyObject);
        count++;
    }

    protected void saveWeaponsForOnlyFour() {
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.HANDGUN));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.SHOTGUN));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.M79));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.SMG));
        System.out.println("NOT FOR ALL WEAPONS");
    }
    protected void saveWeapons() {
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.REVOLVER));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.HANDGUN));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.SAWED_OFF_SHOTGUN));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.SHOTGUN));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.M79));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.SMG));
        fillDataForWeapon(PlayerBag.getNameForWeaponType(WeaponType.GRENADE));

    }

    private void fillDataForWeapon(String weaponName){
        JSONObject weaponObject = new JSONObject();
        weaponObject.setString(TYPE, WEAPON);
        boolean deblocked = bag.isWeaponDeblocked(PlayerBag.getWeaponTypeForName(weaponName));

        weaponObject.setBoolean(WEAPON_IS_DEBLOCKED, deblocked);
        //weaponObject.setBoolean(WEAPON_IS_DEBLOCKED, true);

        weaponObject.setInt(BULLETS, bag.getInBarrelAmmoForWeapon(PlayerBag.getWeaponTypeForName(weaponName)));
        weaponObject.setInt(MAGAZINES, bag.getRestAmmoForWeapon(PlayerBag.getWeaponTypeForName(weaponName)));
        System.out.println("Weapon " + weaponName + " deblocked: " + deblocked + " and has " + bag.getRestAmmoForWeapon(PlayerBag.getWeaponTypeForName(weaponName)));

        boolean weaponIsActual = false;
        if (soldier.getActualWeapon().getWeaponType() == PlayerBag.getWeaponTypeForName(weaponName)){
            System.out.println("This weapon " + weaponName + "is actual");
            weaponIsActual = true;
        }
        System.out.println(weaponName + " has in the barrel " + bag.getInBarrelAmmoForWeapon(PlayerBag.getWeaponTypeForName(weaponName)));
        weaponObject.setBoolean(WEAPON_IS_ACTUAL, weaponIsActual);
        weaponObject.setBoolean(ON_ACTIVE_PANEL, bag.getOnVisiblePanel(PlayerBag.getWeaponTypeForName(weaponName)));
        weaponObject.setString(WEAPON, weaponName);
        data.setJSONObject(count, weaponObject);
        count++;
    }

    protected void saveOnDisk(){
        Program.engine.saveJSONArray(data, pathToFile);
        System.out.println("Data was saved to " + pathToFile);
    }
}
