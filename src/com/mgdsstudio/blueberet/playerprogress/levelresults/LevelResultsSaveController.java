package com.mgdsstudio.blueberet.playerprogress.levelresults;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.SecretAreaZone;

import processing.core.PApplet;
import processing.data.IntList;
import processing.data.JSONObject;

import java.io.File;

public class LevelResultsSaveController extends LevelResultsController implements ILevelResults{
    //private JSONArray dataArray;
    private int levelStartTime;
    private int levelEndTime;
    private final boolean withPauseWhileActivityHidden = true;

    protected int pauseStartTime = -1;
    protected int pauseEndTime = -1;
    protected final IntList deltaTimes;

    public LevelResultsSaveController(GameRound gameRound, PApplet engine) {
        if (gameRound.getLevelType() == Program.MAIN_LEVEL) {
            enemiesOnLevel = calculateEnemiesNumber(gameRound);
            secretsOnLevel = calculateNotFoundedSecrets(gameRound);
            levelStartTime = engine.ceil((float)engine.millis()/1000f);
            zone = gameRound.getZoneNumber();
        }
        deltaTimes = new IntList(0);
    }

    public void updateData(GameRound gameRound, PApplet engine){
        if (gameRound.getLevelType() == Program.MAIN_LEVEL) {
            int restEnemiesOnLevel = calculateEnemiesNumber(gameRound);
            killedEnemies = enemiesOnLevel-restEnemiesOnLevel;
            int restSecretsOnLevel = calculateNotFoundedSecrets(gameRound);
            foundedSecrets = secretsOnLevel-restSecretsOnLevel;
            levelEndTime = engine.floor((float)engine.millis()/1000f);
            time = levelEndTime-levelStartTime;
            if (deltaTimes.size()>0){
                System.out.println("There are pauses");
                for (int i = 0; i < deltaTimes.size(); i++){
                    time-=deltaTimes.get(i);
                }
                if (time <= 0){
                    System.out.println("Trouble with pause long");
                }
            }
        }
    }

    public void saveData(){
        JSONObject zoneResultDataObject = new JSONObject();
        zoneResultDataObject.setInt(ZONE, zone);
        zoneResultDataObject.setInt(ENEMIES, enemiesOnLevel);
        zoneResultDataObject.setInt(KILLED, killedEnemies);
        zoneResultDataObject.setInt(SECRETS, secretsOnLevel);
        zoneResultDataObject.setInt(SECRETS_FOUNDED, foundedSecrets);
        zoneResultDataObject.setInt(TIME, time);
        String path = getPathToFile(zone);
        File file = new File(path);
        if (file.exists()){
            System.out.println("Prev data must be deleted: " + file.delete());
        }
        Program.engine.saveJSONObject(zoneResultDataObject, path);
    }



    private int calculateNotFoundedSecrets(GameRound gameRound) {
        int count = 0;
        for (SecretAreaZone secretAreaZone : gameRound.getSecretAreas()){
            if (!secretAreaZone.wasFounded()){
                count++;
            }
        }
        return count;
    }

    private int calculateEnemiesNumber(GameRound gameRound){
        int count = 0;
        for (Person person : gameRound.getPersons()){
            if (!person.equals(gameRound.getPlayer())){
                if (person.isAlive()){
                    count++;
                }
            }
        }
        return count;
    }

    public void continueTimers() {
        if (pauseEndTime < 0){
            pauseEndTime = Program.engine.millis()/1000;
        }

        calculateDeltaPauseTime();
    }

    private void calculateDeltaPauseTime() {
        int deltaTime = pauseEndTime-pauseStartTime;
        if (deltaTime<0){
            System.out.println("Something is wrong with delta time");
        }
        else if (deltaTime == 0){
            System.out.println("there are no pauses");
        }
        else {
            deltaTimes.append(deltaTime);
            System.out.println("Pause was : " + deltaTime + " seconds");
        }
    }

    public void pauseTimers() {
        pauseStartTime = Program.engine.millis()/1000;
    }

    /*
    public int getKilledEnemies() {
        return killedEnemies;
    }

    public int getFoundedSecrets() {
        return foundedSecrets;
    }

    public int getTime(){
        return time;
    }*/
}
