package com.mgdsstudio.blueberet.menusystem.load.preferences;

import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.JSONArray;

import java.io.File;

public abstract class PreferencesDataController {
    protected JSONArray data;
    protected final static String fileName = "Preferences.json";
    protected int count;

    public static boolean dataFileExists(){
        String path = Program.getRelativePathToAssetsFolder()+ fileName;
        if (Program.OS == Program.DESKTOP){
            File file = new File(path);
            if (file.exists()){
                System.out.println("File exists");
                return true;
            }
            else {
                System.out.println("It doesn't exist");
                return false;
            }
        }
        else {
            //System.out.println("For this OS there are no method to determine file existing");
            File file = new File(FileManagement.getPathToCacheFilesInAndroid()+path);
            if (file.exists()){
                System.out.println("File exists");
                return true;
            }
            else {
                System.out.println("It doesn't exist");
                return false;
            }
            //return false;
        }
    }

    protected String getPathToFile(){
        if (Program.OS == Program.DESKTOP) return Program.getRelativePathToAssetsFolder()+fileName;
        else return FileManagement.getPathToCacheFilesInAndroid()+fileName;
    }


}
