package com.mgdsstudio.blueberet.oldlevelseditor.preferences;

import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.JSONArray;

import java.io.File;

public abstract class EditorPreferencesDataController {
    protected JSONArray data;
    protected final static String fileName = "Editor preferences.json";
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
            File file = new File(AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+path);
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
        else return AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+fileName;
    }


}
