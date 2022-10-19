package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.WeaponFrameController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

public class PlayerDataSimpleSaveMaster extends PlayerDataController implements PlayerSavingDataConstants{
    private String[] data;
    private JSONArray jsonArray;
    public PlayerDataSimpleSaveMaster(){
        levelType = ExternalRoundDataFileController.MAIN_LEVELS;
        createPath(ExternalRoundDataFileController.MAIN_LEVELS);
        //data = Program.engine.loadJSONArray(pathToFile);

    }

    public void changeStringInJson(int oldValue, int newValue, String key){
        data = Program.engine.loadStrings(pathToFile);
        for (int i = 0; i < data.length; i++){
            if (data[i].contains(key)) {
                String oldMoneyString = ""+oldValue;
                String newMoneyString = ""+newValue;
                if (data[i].contains(oldMoneyString)){
                    System.out.println("Data must be replaced");
                    System.out.println("Old string was: " + data[i]);
                    int startChar = data[i].indexOf(oldMoneyString);
                    int endChar = startChar+oldMoneyString.length();
                    String startString = data[i].substring(0,startChar);
                    String endString = data[i].substring(endChar);
                    data[i] = startString + newMoneyString+ endString;
                    System.out.println("New string is: " + data[i]);
                    break;
                }

            }
        }
    }

    public void setNewMoney(int oldMoney, int newMoney){
        data = Program.engine.loadStrings(pathToFile);
        changeStringInJson(oldMoney, newMoney, MONEY);
        //moneyObject.setInt(MONEY, money);
        //count++;
    }

    public void saveOnDisk(){
        if (data !=null) {
            Program.engine.saveStrings(pathToFile, data);
            //Program.engine.saveJSONArray(data, pathToFile);
            System.out.println("Data was saved to " + pathToFile);
        }
        if (jsonArray != null){
            Program.engine.saveJSONArray(jsonArray, pathToFile);
        }
        else{
            System.out.println("Can not save. Data is null");
        }
    }

    public void deblockWeapon(WeaponType weaponType) {
        jsonArray = Program.engine.loadJSONArray(pathToFile);
        //System.out.println("Try to deblock weapon by buying");
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            //System.out.println("Type: " + type);
            if (type != null && type.contains(WEAPON)){
                String name = jsonObject.getString(WEAPON);
                WeaponType typeToBeDeblocked = FirearmsWeapon.getWeaponTypeForName(name);
                //System.out.println("Name: " + name);
                if (name != null && typeToBeDeblocked == weaponType) {
                    jsonObject.setBoolean(WEAPON_IS_DEBLOCKED, true);
                    PlayerBag playerBag = new PlayerBag(ExternalRoundDataFileController.MAIN_LEVELS, null);

                    int bulletsMustBeAddedToTheFirstMagazine = playerBag.getMaxAmmoInBarrelForWeapon(weaponType);;
                    jsonObject.setInt(BULLETS, bulletsMustBeAddedToTheFirstMagazine);
                    System.out.println("Weapon " + weaponType + " was deblocked");
                    jsonObject.setBoolean(ON_ACTIVE_PANEL, true);
                    WeaponType neighbourWeapon = WeaponFrameController.getAnotherWeaponInCell(weaponType);
                    if (neighbourWeapon != null){
                        setWeaponAsNotOnPanel(neighbourWeapon);
                    }
                    setAllWeaponsAsNotActual();
                    jsonObject.setBoolean(WEAPON_IS_ACTUAL, true);
                    break;
                }
            }
        }




    }

    private void setWeaponAsNotOnPanel(WeaponType weaponType) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            //System.out.println("Type: " + type);
            if (type != null && type.contains(WEAPON)) {
                String name = jsonObject.getString(WEAPON);
                if (name != null && FirearmsWeapon.getWeaponTypeForName(name) == weaponType) {
                    if (jsonObject.getBoolean(ON_ACTIVE_PANEL)) {
                        System.out.println("Weapon " + jsonObject.getString(WEAPON) + " was on panel and must be set as not more on panel");
                        jsonObject.setBoolean(ON_ACTIVE_PANEL, false);
                    }
                }
            }
        }
    }

    private void setAllWeaponsAsNotActual() {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            //System.out.println("Type: " + type);
            if (type != null && type.contains(WEAPON)) {
                if (jsonObject.getBoolean(WEAPON_IS_ACTUAL)){
                    System.out.println("Weapon " + jsonObject.getString(WEAPON) + " was actual and must be set as not more actual ");
                    jsonObject.setBoolean(WEAPON_IS_ACTUAL, false);
                }
            }
        }

    }

   /* private void setNewInt(int value, String typeToBeUsed){
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            if (type != null && type.contains(typeToBeUsed)) {
                String name = jsonObject.getString(WEAPON);
                if (name != null && FirearmsWeapon.getWeaponTypeForName(name) == weaponType) {
                    if (jsonObject.getBoolean(ON_ACTIVE_PANEL)) {
                        System.out.println("Weapon " + jsonObject.getString(WEAPON) + " was on panel and must be set as not more on panel");
                        jsonObject.setBoolean(ON_ACTIVE_PANEL, false);
                    }
                }
            }
        }
    }*/


    public void decrementMoney(int cost) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            if (type != null && type.contains(MONEY)) {
                int value = jsonObject.getInt(MONEY);
                value-=cost;
                jsonObject.setInt(MONEY, value);
                System.out.println("New money is " + value);
                break;
            }
        }
    }

    public void incrementItems(int typeToBeIncremented, int count) {
        jsonArray = Program.engine.loadJSONArray(pathToFile);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String typeName = jsonObject.getString(TYPE);
            if (typeName != null && typeName.contains(MEDICAL_KIT)) {
                String objectName = getNameForObjectType(typeToBeIncremented);
                int actualCount = jsonObject.getInt(objectName);
                int newCount = count + actualCount;
                System.out.println("Item " + typeToBeIncremented + " with name "+ objectName + " has now " + newCount);
                jsonObject.setInt(objectName, newCount);

                //else System.out.println("This weapon has name " + name);
            }
        }
    }

    private String getNameForObjectType(int type){
        if (type == AbstractCollectable.SMALL_MEDICAL_KIT) return SMALL;
        else if (type == AbstractCollectable.MEDIUM_MEDICAL_KIT) return MEDIUM;
        else if (type == AbstractCollectable.LARGE_MEDICAL_KIT) return LARGE;
        else if (type == AbstractCollectable.SYRINGE) return SYRINGE;
        else {
            System.out.println("No string for object type " + type);
            return "";
        }
    }

    public void incrementAmmo(int collectableType, WeaponType weaponType, int count) {
        jsonArray = Program.engine.loadJSONArray(pathToFile);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString(TYPE);
            if (type != null && type.contains(WEAPON)) {
                String name = jsonObject.getString(WEAPON);
                ArrayList <WeaponType> list = getWeaponsListThatMustChangeMagazinesCount(weaponType);
                for (int j = 0; j < list.size(); j++) {
                    if (name != null && FirearmsWeapon.getWeaponTypeForName(name) == list.get(j)) {
                        int magazines = jsonObject.getInt(MAGAZINES);
                        int newMagazines = count + magazines;
                        System.out.println("Weapon " + jsonObject.getString(WEAPON) + " has now " + newMagazines);
                        jsonObject.setInt(MAGAZINES, newMagazines);
                    }
                }
                //else System.out.println("This weapon has name " + name);
            }
        }
    }

    private ArrayList <WeaponType> getWeaponsListThatMustChangeMagazinesCount(WeaponType weaponType){
        ArrayList <WeaponType> list = new ArrayList<>();
        if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN){
            list.add(WeaponType.SAWED_OFF_SHOTGUN);
            list.add(WeaponType.SHOTGUN);
        }
        else list.add(weaponType);
        return list;

    }


}
