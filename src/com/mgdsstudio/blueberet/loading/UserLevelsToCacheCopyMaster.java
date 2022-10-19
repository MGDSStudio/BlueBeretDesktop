package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UserLevelsToCacheCopyMaster {
    private PApplet engine;

    public UserLevelsToCacheCopyMaster(PApplet pApplet){
        this.engine = pApplet;
    }

    public void copyToDataInCache(){
        ArrayList<String> dataInCahe = null;
        try {
            dataInCahe = StringLibrary.getFilesListInCache();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Data in cache: ");
        if (dataInCahe != null && dataInCahe.size()<2){
            try {
                AndroidSpecificFileManagement.createCacheFile();
                copyUserLevelsToCache();
                System.out.println("Data was copied in cache directory");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("It is not need to copy files to cache directory");
    }

    private void copyUserLevelsToCache() throws IOException {
        String [] dataInAssets =  AndroidSpecificFileManagement.getFilesListInAssets("");
        System.out.println("Start to copy to cache: " + dataInAssets.length + " files");
        File dir = new File(AndroidSpecificFileManagement.getPathToCacheFilesInAndroid());
        if (!dir.exists()) {
            System.out.println("Try to create new folder: " + AndroidSpecificFileManagement.getPathToCacheFilesInAndroid());
            dir.mkdirs();
        }
        File cacheFile = new File(engine.getActivity().getBaseContext().getCacheDir(), "Pref.txt");
        cacheFile.createNewFile();
        System.out.println("Dir exists: " + dir.getAbsolutePath() + "; Data in folder size: " + dataInAssets.length);

        for (int i = 0; i < dataInAssets.length; i++) {
            if (dataInAssets[i].contains(Program.USER_LEVELS_PREFIX)) {
                try {
                    File source = null;
                    try {
                        InputStream stream = engine.getActivity().getAssets().open(dataInAssets[i]);
                        FileOutputStream out = new FileOutputStream(AndroidSpecificFileManagement.getPathToCacheFilesInAndroid() + dataInAssets[i]);
                        byte[] buf = new byte[8192];
                        int length;
                        while ((length = stream.read(buf)) > 0) {
                            out.write(buf, 0, length);
                        }
                        stream.close();
                        out.close();
                    } catch (Exception e) {
                        System.out.println("Can not get source file; " + source.getAbsolutePath() + e);
                    }
                } catch (Exception e1) {
                    System.out.println("Can not copy file from " + e1);
                }
            }
        }
        System.out.println("Complete copied to cache folder");

    }
}
