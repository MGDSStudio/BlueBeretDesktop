package com.mgdsstudio.blueberet.playerprogress.levelresults;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.data.JSONObject;

import java.io.File;

public class LevelResultsLoadController extends LevelResultsController implements ILevelResults{
    private boolean noData;

    LevelResultsLoadController(int zoneNumber){
        loadData(zoneNumber);
    }

    private void loadData(int zoneNumber) {
        String path = getPathToFile(zoneNumber);
        File file = new File(path);
        if (file.exists()){
            JSONObject data = Program.engine.loadJSONObject(path);
            enemiesOnLevel = data.getInt(ENEMIES);
            killedEnemies = data.getInt(KILLED);
            secretsOnLevel = data.getInt(SECRETS);
            foundedSecrets  = data.getInt(SECRETS_FOUNDED);
            time = data.getInt(TIME);
        }
        else {
            noData = true;
            System.out.println("This zone was not played yet. File " + path + " does not exist");
        }
    }

    public boolean hasData() {
        return !noData;
    }
}
