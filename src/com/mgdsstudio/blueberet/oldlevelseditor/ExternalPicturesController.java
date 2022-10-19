package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.File;

public class ExternalPicturesController {

    public ExternalPicturesController() {

    }

    public String[] getFilesList() {
        String[] pathnames;
        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(getPath());
        // Populates the array with names of files and directories
        pathnames = f.list();
        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
        return pathnames;
    }

    public static String getPath() {
        String path = new String();
        if (Program.OS == Program.DESKTOP) {
            path = Program.getRelativePathToAssetsFolder();

        }
        else if (Program.OS == Program.ANDROID) {
            path = "";
        }
        return path;
    }
}
