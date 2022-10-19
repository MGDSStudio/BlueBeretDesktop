package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.data.IntList;
import processing.data.JSONObject;

import java.util.ArrayList;

public class PlayerDataLoadMaster extends PlayerDataController implements PlayerSavingDataConstants{
    private ArrayList < WeaponType> deblockedWeapons;
    private IntList objects;
    public PlayerDataLoadMaster(Soldier soldier, boolean global, boolean levelType) {
        this.soldier = soldier;
        this.levelType = levelType;
        createPath(global);
        bag = soldier.getPlayerBag();
        //bag = null;
        if (bag == null) {
            bag = new PlayerBag(levelType, soldier);
        }
        else {
            System.out.println("Bag must not be created from data file");
        }
        data = Program.engine.loadJSONArray(pathToFile);
        if (Program.debug) System.out.println("Data must be uploaded from " + pathToFile + " level type : " + levelType);
        System.out.println("Data was loaded succesfully from path " + pathToFile);
        deblockedWeapons = new ArrayList<>();
        onActivePanelWeapons = new ArrayList<>();
        objects = new IntList();
    }

    public void loadData(){
        System.out.println("Data list has " + data.size() + " strings");
        boolean oneWeaponIsAlreadyActual = false;
            for (int i = 0; i < data.size(); i++){
                JSONObject object = data.getJSONObject(i);
                String name = object.getString(TYPE);
                if (name == MONEY || name.equals(MONEY)){
                    int moneyValue = object.getInt(MONEY);
                    bag.setMoney(moneyValue);
                    System.out.println("Money recovered up to: " + moneyValue);
                }
                else if (name == WEAPON || name.equals(WEAPON)){
                    int bullets = object.getInt(BULLETS);
                    int magazines = object.getInt(MAGAZINES);
                    boolean isActual = object.getBoolean(WEAPON_IS_ACTUAL);
                    boolean onPanel = object.getBoolean(ON_ACTIVE_PANEL);
                    String weaponName = object.getString(WEAPON);
                    boolean deblocked = object.getBoolean(WEAPON_IS_DEBLOCKED);
                    if (deblocked) {
                        deblockedWeapons.add(FirearmsWeapon.getWeaponTypeForName(weaponName));
                        System.out.println("weapon " + name + " is deblocked");
                    }
                    bag.setParametersForWeaponByString(weaponName, bullets, magazines);
                    WeaponType type = PlayerBag.getWeaponTypeForName(weaponName);
                    if (isActual) {
                        if (!oneWeaponIsAlreadyActual) {
                            System.out.println("Weapon " + weaponName + " with type " + type + "  must be set as actual");
                            soldier.setWeaponAsActual(type);
                            soldier.setWeaponAsDeblocked(PlayerBag.getWeaponTypeForName(weaponName));
                            oneWeaponIsAlreadyActual = true;
                        }
                        else {
                            System.out.println("*** More than one weapon is actual. Trouble!");
                            soldier.setWeaponAsDeblocked(PlayerBag.getWeaponTypeForName(weaponName));
                        }
                    }

                    if (onPanel){
                        System.out.println("Weapon is on panel: " + type);
                        onActivePanelWeapons.add(type);
                    }
                    System.out.println("Weapon " + weaponName + " recovered up to : " + bullets + " and " + magazines);
                    for (FirearmsWeapon weapon : soldier.weapons){
                        if (weapon.getWeaponType() == PlayerBag.getWeaponTypeForName(weaponName)){
                            weapon.setMagazineCapacity(bullets);
                            System.out.println("Weapons in barrel set on " + bullets + " for weapon " + weaponName);
                        }
                        else {

                        }
                    }
                }
                else if (name == MEDICAL_KIT || name.equals(MEDICAL_KIT)){
                    int larges = object.getInt(LARGE);
                    int mediums = object.getInt(MEDIUM);
                    int smalls = object.getInt(SMALL);
                    int syringes = object.getInt(SYRINGE);
                    bag.setLargeMedicalKitsNumber(larges);
                    bag.setMediumMedicalKitsNumber(mediums);
                    bag.setSmallMedicalKitsNumber(smalls);
                    bag.setSyringesNumber(syringes);

                    System.out.println("Medical kits were recovered: " + larges + "; " + mediums + "; " + smalls + " and syringes " + syringes);
                }
                else if (name == PLAYER_PARAMETERS || name.equals(PLAYER_PARAMETERS)){
                    int life = object.getInt(ACTUAL_LIFE);
                    int maxLife = object.getInt(MAX_LIFE);
                    boolean orient = object.getBoolean(VIEW_DIRECTION);
                    soldier.setLife(life);
                    soldier.setMaxLife(maxLife);
                    soldier.orientation = orient;
                    soldier.setSightDirection(orient);
                    int selectedMainObject = object.getInt(MAIN_SELECTED_OBJECT);
                    bag.setObjectOnMainFrame(selectedMainObject);
                    System.out.println("Main selected object: " + selectedMainObject + " is " );
                }
                else {
                    System.out.println("This object is not known. Name: " + name);
                }
            }
            fillPlayerBagByWeaponsList();
            updateObjectsInBagNumber();
    }

    private void updateObjectsInBagNumber() {
        bag.updateObjectsNumber();
    }

    private void fillPlayerBagByWeaponsList() {
        bag.setVisibleWeaponsOnWeaponPanel(onActivePanelWeapons);
        bag.setDeblockedWeapons(deblockedWeapons);
    }

    public ArrayList<WeaponType> getDeblockedWeapons() {
        return deblockedWeapons;
    }

    public ArrayList<WeaponType> getOnActivePanelWeapons() {
        return onActivePanelWeapons;
    }
}
