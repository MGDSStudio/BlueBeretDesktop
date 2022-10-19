package com.mgdsstudio.blueberet.menusystem;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;

import java.util.ArrayList;

public class EndGameDeterminer {

    public EndGameDeterminer(){

    }

    public boolean didPlayerEndGame(){
            //ArrayList<String> filesList = StringLibrary.getFilesListInAssetsFolder();
            PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
            master.loadData();
            int nextZone = master.getNextZone();
            if (master.getNextZone()>Program.LAST_LEVEL_OF_THE_GAME){
                System.out.println("Level with number " + nextZone + " was not founded. Maybe the player already ended the game");
                return true;
            }
            else {
                return false;
            }
    }

    public boolean didPlayerEndGameByTheLastLevel(){
        ArrayList<String> filesList = StringLibrary.getFilesListInAssetsFolder();
        PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
        master.loadData();
        int nextZone = master.getNextZone();
        for (String assetsData : filesList){
            if (assetsData.contains(Program.USER_LEVELS_PREFIX)){
                System.out.println("Try to get level number in file with name: " + assetsData);
                int levelNumber = StringLibrary.getLevelNumberFromName(assetsData);
                if (levelNumber == nextZone){
                    System.out.println("There are level with number " + nextZone + " in assets directory with name " + assetsData);
                    return false;
                }
            }
        }
        System.out.println("Level with number " + nextZone + " was not founded. Maybe the player already ended the game");
        return true;
    }

    /*

     */
}
