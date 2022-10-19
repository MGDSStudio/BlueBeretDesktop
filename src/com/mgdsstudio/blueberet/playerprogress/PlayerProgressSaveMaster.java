package com.mgdsstudio.blueberet.playerprogress;

import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.IntList;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class PlayerProgressSaveMaster extends PlayerProgress{
    private JSONObject playerProgressDataObject;
    private JSONArray objectsNotMoreToAppear;

    public PlayerProgressSaveMaster() {
        dataArray = new JSONArray();
    }
        int functionWasCalledNumber = 0;
   /* public PlayerProgressSaveMaster(int idToBeAddedToList) {
        dataArray = new JSONArray();
    }*/

    public void writeValuesWithoutSaving(){

        count = 0;
        //dataArray = new JSONArray();
        playerProgressDataObject = new JSONObject();
        playerProgressDataObject.setInt(LAST_COMPLETED_LEVEL, lastCompletedLevel);
        playerProgressDataObject.setInt(LAST_COMPLETED_ZONE, lastCompletedZone);
        playerProgressDataObject.setInt(NEXT_ZONE, nextZone);
        playerProgressDataObject.setInt(REST_LIFES, restLifes);
        dataArray.append(playerProgressDataObject);
        dataArray.setJSONObject(count, playerProgressDataObject);
        count++;
        if (objectsNotMoreToAppear == null) objectsNotMoreToAppear = new JSONArray();
        if (objectsToBeNotMoreUploaded.size()>0) {
            for (int i = 0; i < objectsToBeNotMoreUploaded.size(); i++) {
                objectsNotMoreToAppear.append(objectsToBeNotMoreUploaded.get(i));
            }
            dataArray.append(objectsNotMoreToAppear);
        }
        functionWasCalledNumber++;
        System.out.println("This fun was called "+ functionWasCalledNumber + " times");
    }



    public void saveOnDisk(){

        String pathToSave;
        if (Program.OS == Program.DESKTOP) {
            pathToSave = Program.getAbsolutePathToAssetsFolder(fileName);
        }
        else {
            pathToSave = (AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+fileName);
        }
        deleteFile();
        System.out.println("Data in array " + dataArray.toString());
        Program.engine.saveJSONArray(dataArray, pathToSave);
    }

    /*
    public void writeValuesWithoutSaving(int lastCompletedLevel, int lastCompletedZone, int restLifes){
        mainProgressObject = new JSONObject();
        mainProgressObject.setInt(LAST_COMPLETED_LEVEL, lastCompletedLevel);
        mainProgressObject.setInt(LAST_COMPLETED_ZONE, lastCompletedZone);
        mainProgressObject.setInt(REST_LIFES, restLifes);
        dataArray.setJSONObject(count, mainProgressObject);
        count++;
    }*/

    private void fillDataWithActualValues(){
        PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
        master.loadData();
        restLifes = master.getRestLifes();
        lastCompletedZone = master.getLastCompletedZone();
        nextZone = master.getNextZone();
        lastCompletedLevel = master.getLastCompletedLevel();
        objectsToBeNotMoreUploaded = master.getObjectsNotMoreUploaded();
    }

    public void decrementRestLifes(){
        fillDataWithActualValues();
        restLifes--;
        if (restLifes <= 0 ) {
            restLifes = 0;
            System.out.println("There are no more lifes ");
        }
        System.out.println("Lifes are now " + restLifes);
        writeValuesWithoutSaving();
    }

    public void setNextZone(int actualZone, int nextZone){
        //fillDataWithActualValues();
        lastCompletedZone = actualZone;
        this.nextZone = nextZone;
        //writeDefaultValuesWithoutSaving();
    }

    public void setNextZoneWithFullDataRewriting(int actualZone, int nextZone){
        fillDataWithActualValues();
        lastCompletedZone = actualZone;
        this.nextZone = nextZone;
        writeValuesWithoutSaving();
    }

    public void setLastCompletedLevelOld(int lastLevel){
        fillDataWithActualValues();
        lastCompletedLevel = lastLevel;
        writeValuesWithoutSaving();
    }

    public void setLastCompletedLevel(int lastLevel){
        lastCompletedLevel = lastLevel;
    }

    private void updateVakue(String key, int value){
        if (key == LAST_COMPLETED_LEVEL){

        }
    }

    public void addListOfNotMoreUploadableObjects(IntList idsToBeNotMoreShown) {
        PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
        master.loadData();
        objectsToBeNotMoreUploaded = master.getObjectsNotMoreUploaded();


        //fillDataWithActualValues();
        //PlayerProgressLoadMaster playerProgressLoadMaster = new PlayerProgressLoadMaster();
        //playerProgressLoadMaster.loadObjectsIds();
        //System.out.println("List from " + idsToBeNotMoreShown.size() + " objects must be written");
        //loadObjectsIds();
        System.out.println("Array is null " + (objectsToBeNotMoreUploaded == null) + " has size " + objectsToBeNotMoreUploaded.size());
        for (int i = 0; i < idsToBeNotMoreShown.size(); i++) {
            objectsToBeNotMoreUploaded.append(idsToBeNotMoreShown.get(i));
        }
        //writeDefaultValuesWithoutSaving();
    }

    public void addNotMoreUploadableObject(int id) {
        fillDataWithActualValues();
        PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
        master.loadData();
        objectsToBeNotMoreUploaded = master.getObjectsNotMoreUploaded();
        objectsToBeNotMoreUploaded.append(id);

        //writeValuesWithoutSaving();
    }

    private void writeDataForObjectsWithoutSaving(){
        //objectsObject = new JSONObject();
        JSONArray objectsNotMoreToAppear = new JSONArray();
        for (int i = 0; i < objectsToBeNotMoreUploaded.size(); i++){
            objectsNotMoreToAppear.append(objectsToBeNotMoreUploaded.get(i));

        }
        dataArray.setJSONArray(1, objectsNotMoreToAppear);
    }


    /*
    public void addObjectIdToNotMoreAppearingList(int ID){
        fillDataWithActualValues();
    }*/

}
