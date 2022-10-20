package com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger;

import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.JSONArray;

import java.io.File;

public abstract class AbstractBeretColorMaster implements IBeretColors{
    protected JSONArray data;
    protected final static String fileName = "Player color.json";
    //protected String fileName;
    protected int count;
    protected static TwiceColor beretColor;

    protected static String getPathToFile(){
        if (Program.OS == Program.DESKTOP) return Program.getRelativePathToAssetsFolder()+fileName;
        else return FileManagement.getPathToCacheFilesInAndroid()+fileName;
    }

    public TwiceColor getBeretColor() {
        return beretColor;
    }

    public static boolean exists(){
        final File file = new File(getPathToFile());
        if (file.exists()){

            return true;
        }
        else return false;
    }
}
