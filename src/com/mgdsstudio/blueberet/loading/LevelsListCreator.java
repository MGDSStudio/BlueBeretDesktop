package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.ArrayList;

public class LevelsListCreator {
    private boolean levelType;
    private ArrayList <String> fileNamesWithPath;
    private ArrayList <String> levelNames;
    private boolean levelsDetectingType = BY_LIST_OF_ALL_FILES;
    public static final boolean BY_LIST_OF_ALL_FILES = true;
    public static final boolean BY_BASIC_FILES_NAMES = false;

    public LevelsListCreator(boolean levelType){
        this.levelType  = levelType;
        createLevelsList();
        createLevelNames();
    }

    private void createLevelNames() {
        //ArrayList<String> rounds = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Game2D.USER_LEVELS_PREFIX, Game2D.USER_LEVELS_EXTENSION);
        levelNames = new ArrayList<>();
        for (String path : fileNamesWithPath) {
            LoadingMaster loadingMaster = new LoadingMaster((byte)(StringLibrary.getLevelNumberFromName(path)), ExternalRoundDataFileController.USER_LEVELS);
            String name = loadingMaster.getLevelName();
            levelNames.add(name);
        }

        //String name = loadingMaster.getLevelName();
    }

    private void createLevelsList(){
        fileNamesWithPath = new ArrayList<>();
        ArrayList<String> allFiles = null;
        if (Program.OS == Program.DESKTOP) allFiles = StringLibrary.getFilesListInAssetsFolder();
        else if (Program.OS == Program.ANDROID) allFiles = StringLibrary.getFilesListInCache();
        fileNamesWithPath = StringLibrary.getFilesByPrefixAndSuffix(allFiles, Program.USER_LEVELS_PREFIX, Program.USER_LEVELS_EXTENSION);
        System.out.println("There are " + fileNamesWithPath.size() + " levels");
    }

    public ArrayList <String> getLevelsPathesList(){
        return fileNamesWithPath;
    }

    public ArrayList <String> getLevelsNamesList(){
        return levelNames;
    }

    public int getLevelNumberByPath(String path) {
        int number = StringLibrary.getLevelNumberFromName(path);
            if (number != 0){
                return number;
            }
        System.out.println("This file name "+ path +" has wrong structure");
        return -2;
    }

    public String getLevelPathByLevelName(String name) {
        for (int i = 0; i < levelNames.size(); i++){
            if (levelNames.get(i).equals(name)){
                return fileNamesWithPath.get(i);
            }
            else if (levelNames.get(i) == (name)){
                return fileNamesWithPath.get(i);
            }
        }
        return "";
    }
}
