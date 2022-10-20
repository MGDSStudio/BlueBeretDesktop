package com.mgdsstudio.blueberet.playerprogress.levelresults;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.File;

class FileManager implements ILevelResults{
    private String [] data;

    FileManager(GameRound gameRound) {
        String path = "";
        if (Program.OS == Program.DESKTOP) path = Program.getAbsolutePathToAssetsFolder(FILE_NAME);
        else if (Program.OS == Program.ANDROID) path = FileManagement.getPathToCacheFilesInAndroid()+FILE_NAME;
        File file = new File(path);
        if (!file.exists()){
            createClearFile(gameRound, path);
        }

    }

    private void createClearFile(GameRound gameRound, String path) {



    }

     void addData (GameRound gameRound, String path) {
        data = new String[6];

    }


}
