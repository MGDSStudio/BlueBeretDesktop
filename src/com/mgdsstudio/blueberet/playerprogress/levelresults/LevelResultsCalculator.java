package com.mgdsstudio.blueberet.playerprogress.levelresults;

public class LevelResultsCalculator extends LevelResultsController implements ILevelResults{
    private static int start, end;

    //private static int start = 1, end = 4;

    public LevelResultsCalculator(int startZone, int endZone){
        init(startZone,endZone);
    }

    public LevelResultsCalculator(){
        if (start <= 0 && end  <= 0){
            System.out.println("Trouble! We can not get data for zones between " + start + " and " + end);
        }
        else init(start, end);
    }

    private void init(int startZone, int endZone){
        for (int i = startZone; i <= endZone; i++){
            LevelResultsLoadController levelResultsLoadController = new LevelResultsLoadController(i);
            if (levelResultsLoadController.hasData()){
                this.enemiesOnLevel+=levelResultsLoadController.getEnemiesOnLevel();
                this.killedEnemies+=levelResultsLoadController.getKilledEnemies();
                this.secretsOnLevel+=levelResultsLoadController.getSecretsOnLevel();
                this.foundedSecrets+=levelResultsLoadController.getFoundedSecrets();
                this.time+=levelResultsLoadController.getTime();
            }
            System.out.println("It is not very good. We create an exemplar of this class only to save the static variables. From the menu I can launch the simple constructor ");
            start = startZone;
            end = endZone;
        }
    }



}
