package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.zones.SingleFlagZone;

public class EndLevelZone extends SingleFlagZone implements IDrawable {
    final public static String CLASS_NAME = "EndLevelZone";
    public final static byte PLAYER_APPEARING_IN_ZONE = 0;
    public final static byte NPCS_LEAVING_ZONE = 1;

    final static public String PLAYER_APPEARING_IN_ZONE_STRING = "Player appears in zone";
    final static public String NPCS_LEAVING_ZONE_STRING = "NPC's leave zone";

    private int completedLevel = -1;
    private int nextZone = -1;
    private int firstZoneOfRound = 1;
    private boolean activated = false;

    public EndLevelZone(Flag flag, byte activatingCondition){
        this.flag = flag;
        this.activatingCondition = activatingCondition;
    }

    public EndLevelZone(Flag flag, byte activatingCondition, int completedLevel, int nextZone, int firstZoneOfRound){
        this.flag = flag;
        this.activatingCondition = activatingCondition;
        this.completedLevel = completedLevel;
        this.nextZone = nextZone;
        this.firstZoneOfRound = firstZoneOfRound;
    }

    public EndLevelZone(GameObjectDataForStoreInEditor objectData) {
        this.flag = objectData.getFlag();
        this.activatingCondition = objectData.getGoal();
    }

    @Override
    public void draw(GameCamera gameCamera) {
        if (Program.debug){
            flag.draw(gameCamera);
            //Game2D.engine.text(onScreenDebugTitle, flag.getPosition().x, flag.getPosition().y);
        }
    }

    public boolean isActivated(){
        return activated;
    }

    public void update(GameRound gameRound){
        if (!activated) {
            if (activatingCondition == PLAYER_APPEARING_IN_ZONE) {
                if (flag.inZone(gameRound.getPlayer().getPixelPosition())) {
                    System.out.println("You win! Player appears in end level zone");
                    activated = true;
                    gameRound.addWinScreen();
                    gameRound.setLevelWon(this);
                }
            }
            else if (activatingCondition == NPCS_LEAVING_ZONE){
                boolean isSomebodyInZone = false;
                for (Person person : gameRound.persons){
                    if (flag.inZone(person.getPixelPosition())){
                        if (person.isAlive()){
                            if (person.getClass() != Soldier.class) isSomebodyInZone = true;
                        }
                    }
                }
                if (!isSomebodyInZone){
                    System.out.println("You win! Zone is clear from enemies");
                    activated = true;
                    gameRound.addWinScreen();

                    gameRound.setLevelWon(this);
                }
            }
        }
    }

    @Override
    public String getStringData() {
        GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
        saveMaster.createEndLevelZone();
        System.out.println("Data string for end level zone" + saveMaster.getDataString());
        return saveMaster.getDataString();
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    public int getCompletedLevel() {
        return completedLevel;
    }

    public int getNextZone() {
        return nextZone;
    }

    public int getFirstZoneOfRound() {
        return firstZoneOfRound;
    }
}
