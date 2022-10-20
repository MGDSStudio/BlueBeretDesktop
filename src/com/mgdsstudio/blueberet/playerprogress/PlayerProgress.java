package com.mgdsstudio.blueberet.playerprogress;

import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.IntList;
import processing.data.JSONArray;

import java.io.File;

abstract class PlayerProgress implements IPlayerProgressConstants{
    protected JSONArray dataArray;
    protected final static String fileName = "Progress.json";
    protected int count = 0;
    //Data to be saved with default values
    protected boolean companyWasAlreadyStarted = false;
    protected int lastCompletedZone = 0;
    protected int nextZone = 1;
    protected int lastCompletedLevel = 0;
    protected int restLifes = 99;
    protected IntList objectsToBeNotMoreUploaded = new IntList();

    @Override
    public final String toString(){
        String data = "Data: ";
        data+="companyWasAlreadyStarted: ";
        data+=companyWasAlreadyStarted;
        data+=". lastCompletedZone: ";
        data+=lastCompletedZone;
        return data;
    }

    protected void deleteFile(){
        String pathForFile;
        if (Program.OS == Program.DESKTOP) pathForFile = Program.getAbsolutePathToAssetsFolder(fileName);
        else pathForFile = FileManagement.getPathToCacheFilesInAndroid()+fileName;
        File file =  new File(pathForFile);
        System.out.println("Writeable: " + file.setWritable(true));
        System.out.println("Readable: " + file.setReadable(true));
        System.out.println("File was deleted: " +file.delete());
    }

    public final static boolean wasCompanyStarted(){
        String fullPath;
        if (Program.OS == Program.DESKTOP) fullPath = Program.getAbsolutePathToAssetsFolder(fileName);
        else if (Program.OS == Program.ANDROID) fullPath = FileManagement.getPathToCacheFilesInAndroid()+fileName;
        else fullPath = "";
        File file = new File(fullPath);
        if (file.exists()){
            if (Program.debug) System.out.println("Player progress file: " + fileName + " was already created");
            return true;
        }
        else {
            if (Program.debug)System.out.println("Player progress file: " + fileName + " was not created now");
            return false;
        }

    }

    public int getNextZone(){
        return (nextZone);
    }

    public int getLastCompletedZone() {
        return lastCompletedZone;
    }

    public int getNextLevel(){
        return (lastCompletedLevel+1);
    }

    public int getRestLifes(){
        return (restLifes);
    }

    public int getLastCompletedLevel() {
        return lastCompletedLevel;
    }

    public IntList getObjectsNotMoreUploaded() {
        return objectsToBeNotMoreUploaded;
    }

}
