package com.mgdsstudio.blueberet.playerprogress.levelresults;

import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;

abstract class LevelResultsController implements  ILevelResults{
    protected int enemiesOnLevel;
    protected int killedEnemies;
    protected int secretsOnLevel;
    protected int foundedSecrets;

    protected int time;
    protected int zone;


    protected String getPathToFile(int zone){
        String path = "";
        if (Program.OS == Program.DESKTOP){
            path = Program.getAbsolutePathToAssetsFolder(FILE_NAME+zone+EXTENSION);
        }
        else path = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+FILE_NAME+zone+EXTENSION;
        return path;
    }

    public int getEnemiesOnLevel() {
        return enemiesOnLevel;
    }

    public int getKilledEnemies() {
        return killedEnemies;
    }

    public int getSecretsOnLevel() {
        return secretsOnLevel;
    }

    public int getFoundedSecrets() {
        return foundedSecrets;
    }

    public int getTime() {
        return time;
    }



    @Override
    public String toString(){
        String divider = ". ";
        String minus = ": ";
        return ENEMIES + minus + enemiesOnLevel + divider
                + KILLED + minus + killedEnemies + divider
                + SECRETS + minus + secretsOnLevel + divider
                + SECRETS_FOUNDED + minus + foundedSecrets + divider
                + TIME + minus + time;
    }

    public void addEnemiesToLevel(int i) {
        enemiesOnLevel+=i;
    }



    /*
    public void fillResultsForActualZone(GameRound gameRound, PApplet engine){
        int restEnemies = calculateEnemiesNumber(gameRound);
        int restZones = calculateNotFoundedSecrets(gameRound);
        int restLevelTime = engine.millis();
    }*/
}
