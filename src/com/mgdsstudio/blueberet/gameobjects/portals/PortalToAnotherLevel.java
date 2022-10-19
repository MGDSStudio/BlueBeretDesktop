package com.mgdsstudio.blueberet.gameobjects.portals;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class PortalToAnotherLevel extends Portal implements IDrawable {
    public final static String CLASS_NAME = "AnotherLevelPortal";
    private int enterLevelNumber;
    private int exitLevelNumber;
    private static Person player;
    private boolean withBody = false;
    private boolean startedToJumpToAnotherLevel;
    private boolean alreadyWasVisited;
    //private PlayerControl playerControl;

    public PortalToAnotherLevel(GameRound gameRound, Flag enter, Flag exit, int enterLevelNumber, int exitLevelNumber) {
        //this.playerControl = playerControl;
        this.enter = enter;
        this.exit = exit;
        this.enterLevelNumber = enterLevelNumber;
        this.exitLevelNumber = exitLevelNumber;
        System.out.println("New portal to next leve was created");
    }


    public void update(GameRound gameRound) {
        if (!alreadyWasVisited) {
            if (enter.inZone(gameRound.getPlayer().getPixelPosition())) {
                alreadyWasVisited = true;
                gameRound.pauseSoundtrack();
                gameRound.getSoundController().setAndPlayAudio(SoundsInGame.END_LEVEL_ZONE_ACHIVED);
                if (Program.debug) System.out.println("End level music was added");
            }
        }

    }

    public int getExitLevelNumber() {
        return exitLevelNumber;
    }

    public void savePlayer(Person player){
        this.player = player;
    }

    public Person getPlayer(){
        return player;
    }

    @Override
    public void draw(GameCamera gameCamera) {
        enter.draw(gameCamera);
        //System.out.println("Portal is drawn");
    }


}
