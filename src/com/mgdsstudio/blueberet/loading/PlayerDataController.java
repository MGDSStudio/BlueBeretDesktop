package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import processing.data.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class PlayerDataController {
    protected JSONArray data;
    protected PlayerBag bag;
    protected Soldier soldier;
    public final static String fileNameForGlobalSavingInCompany = "Global player data.json";
    protected final static String fileNameForLocalSavingInCompany = "Local player data.json";

    protected final static String fileNameForGlobalSavingSingleLevels = "Single mission global player data.json";
    protected final static String fileNameForLocalSavingSingleLevels = "Single mission local player data.json";

    protected String fileName;
    protected int count = 0;
    public final static boolean GLOBAL_SAVING = true;
    protected String pathToFile;
    protected boolean levelType;
    protected ArrayList<WeaponType> onActivePanelWeapons;

    protected void createPath(boolean savingType){
        //levelType = savingType;
        if (levelType == ExternalRoundDataFileController.MAIN_LEVELS) fileName = fileNameForGlobalSavingInCompany;
        else fileName = fileNameForGlobalSavingSingleLevels;
        System.out.println("Level type name for this level is " + fileName);
        pathToFile = "";
        if (Program.OS == Program.ANDROID){
            pathToFile = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+ fileName;
            copyTemplateFile(savingType, pathToFile);
        }
        else{
            pathToFile = Program.getAbsolutePathToAssetsFolder(fileName);
        }
        System.out.println("Data must be saved to: " + pathToFile);

    }

    private void copyTemplateFile(boolean savingType, String endFilePath) {
        File file = new File(pathToFile);
        if (file.exists()){
            System.out.println("File exists;");
        }
        else {
            System.out.println("File must be copied to " + file.getAbsolutePath() + " does not exist");
            File file1 = new File(fileNameForGlobalSavingInCompany);
            if (true){
                try {
                    //InputStream stream = Program.engine.getActivity().getAssets().open(Program.getAbsolutePathToAssetsFolder(fileName));
                    InputStream stream = Program.iEngine.createInputStreamInAssetsForFile(Program.getAbsolutePathToAssetsFolder(fileName));


                    FileOutputStream out = new FileOutputStream(pathToFile);
                    byte[] buf = new byte[8192];
                    int length;
                    while ((length = stream.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                    stream.close();
                    out.close();
                    System.out.println("Template copied!");
                } catch (Exception e) {
                    System.out.println("Can not get source file; " + file1.getAbsolutePath() + e);
                }
            }
            else {
                System.out.println("Template file " + file1.getAbsolutePath() + " in assets does not exist! ");
            }
        }
    }

}
