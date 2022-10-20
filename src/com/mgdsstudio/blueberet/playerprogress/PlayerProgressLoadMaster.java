package com.mgdsstudio.blueberet.playerprogress;

import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class PlayerProgressLoadMaster extends PlayerProgress{

    public PlayerProgressLoadMaster() {
        String pathForFile;
        if (Program.OS == Program.DESKTOP) pathForFile = Program.getAbsolutePathToAssetsFolder(fileName);
        else pathForFile = FileManagement.getPathToCacheFilesInAndroid()+fileName;
        dataArray = Program.engine.loadJSONArray(pathForFile);
    }

    public void loadData(){
        System.out.println("Data array has " + dataArray.size() + " objects");
        for (int i = 0; i < dataArray.size(); i++){
            if (i == 0) {
                JSONObject object = dataArray.getJSONObject(i);
                lastCompletedLevel = object.getInt(LAST_COMPLETED_LEVEL);
                lastCompletedZone = object.getInt(LAST_COMPLETED_ZONE);
                nextZone = object.getInt(NEXT_ZONE);
                restLifes = object.getInt(REST_LIFES);
            }
            else if (i == 1){
                try {
                    JSONArray list = dataArray.getJSONArray(i);
                    int[] values = list.getIntArray();
                    for (int j = 0; j < values.length; j++) {
                        objectsToBeNotMoreUploaded.append(values[j]);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /*void loadObjectsIds(){
        System.out.println("Data array has " + dataArray.size() + " objects");
        for (int i = 0; i < dataArray.size(); i++){
            if (i == 1){
                try {
                    JSONArray list = dataArray.getJSONArray(i);
                    int[] values = list.getIntArray();
                    for (int j = 0; j < values.length; j++) {
                        objectsToBeNotMoreUploaded.append(values[j]);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }*/

    public JSONArray getDataArray(){
        return dataArray;
    }

    public int getFirstZoneForActualLevel() {
        System.out.println("*** Attention! It is trouble! The saved data must have more data about the first zone of the level! I need to create a table with constants!");
        System.out.println("*** Attention! It is trouble! The saved data must have more data about the first zone of the level! I need to create a table with constants!");
        return 1;
    }




/*
    public int getRestLifes() {
        return restLifes;
    }
*/
}
