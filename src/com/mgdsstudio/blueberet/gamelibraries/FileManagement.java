package com.mgdsstudio.blueberet.gamelibraries;


import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.*;
import java.nio.channels.FileChannel;

public abstract class FileManagement {
    public static final String SD_CACHE_PATH = "/Android/data/com.mgdsstudio.blueberet.mainpackage/files/";
    public static final String dataToSave = Program.getAbsolutePathToAssetsFolder("preferences.txt");


    public static void createCacheFile() {
        File cacheDir = Program.iEngine.getExternalFilesDir("files");
        if (!cacheDir.exists()) {
            System.out.println("Directory cache is created");
            if (cacheDir.mkdir()) System.out.println("Directory files in Android is succesfully created");
            else System.out.println("Can not create cache directory");
        }
        else System.out.println("This directory already exists");


        File cacheFile = new File(Program.iEngine.getPathInCache(dataToSave));
        String pathToCacheFile = cacheFile.getAbsolutePath();
        try {
            if (!cacheFile.exists()) {
                System.out.println("File "+ cacheFile.getAbsolutePath() +" doesn't exist!");
                cacheFile.createNewFile();
                System.out.println("Cache file is created!");
            }
            else {
                System.out.println("File exists");
            }
        } catch (Exception e) {
            System.out.println("Can not create file " + pathToCacheFile + "; Exc:" + e);
        }
    }



    private static String  getSdWritableFilePathOrNull(String relativeFilename){
        File externalDir = new File(Program.iEngine.getPathInCache(null));
        System.out.println("External dir: " + externalDir);
        if ( externalDir == null ){
            System.out.println("External dir doesn't exist");
            return null;
        }

        String sketchName= "";
        File sketchSdDir = new File(externalDir, sketchName);
        File finalDir =  new File(sketchSdDir, relativeFilename);
        return finalDir.getAbsolutePath();
    }



    public static String[] getFilesListInAssets(String path) {
        String [] list;
        list = Program.iEngine.getAssets(path);
        if (list.length > 0) {
            for (String file : list) {
            }
        }
        return list;
    }

    private static String[] reverseArray(String[] startArray){
        String[] endArray = new String[startArray.length];
        for (int i = 0; i < startArray.length; i++){
            endArray[endArray.length-1-i] = startArray[i];
        }
        return endArray;
    }

    public static String getPathToCacheFilesInAndroid(){
        String pathToCache = Program.iEngine.getPathInCache(null);//Environment.getExternalStorageDirectory() + AndroidSpecificFileManagement.SD_CACHE_PATH
        return pathToCache;
    }


    public static void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }



}
