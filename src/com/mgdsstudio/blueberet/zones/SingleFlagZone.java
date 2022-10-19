package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class SingleFlagZone extends SingleGameElement implements IDrawable {
    protected Flag flag;
    public final static int ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE = 0;
    public final static int ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE_AND_ZONE_FREE_FROM_PERSONS = 1;
    public final static int ACTIVATING_WHEN_ZONE_FREE_FROM_PERSONS = 2;
    public final static int ACTIVATING_WHEN_PLAYER_HAS_NOT_ENOUGH_HEALTH = 3;
    public final static int ACTIVATING_WHEN_ZONE_HAS_PERSONS = 4;

    public final static int WORK_ONLY_IF_ZONE_HAS_ALIVE_PERSONS = 7;

    public int activatingCondition = ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE;

    public PVector getAbsolutePosition() {
        return  flag.getPosition();
    }

    public float getWidth() {
        return flag.getWidth();
    }

    public float getHeight() {
        return flag.getHeight();
    }

    public abstract String getClassName();

    /*
    protected boolean isPersonInZone(Person person){
        if (flag.inZone(person.getPixelPosition())){
            return true;
        }
        else return false;
    }*/

    protected boolean isGameObjectInZone(GameObject object){
        if (flag.inZone(object.getPixelPosition())){
            return true;
        }
        else return false;
    }

    public Flag getFlag() {
        return flag;
    }

    @Override
    public void draw(GameCamera gameCamera) {
        flag.draw(gameCamera);
    }

    @Override
    public String getObjectToDisplayName(){
        return "Zone";
    }


    protected boolean isPlayerInZone(GameRound gameRound) {
        if (flag.inZone(gameRound.getPlayer().getPixelPosition())){
            return true;
        }
        else return false;
    }

    protected boolean isZoneFreeFromPersons(GameRound gameRound) {
        ArrayList<Person> persons = gameRound.getPersons();
        for (Person person: persons) {
            if (!person.equals(gameRound.getPlayer())) {
                if (person.isAlive()) {
                    if (flag.inZone(person.getPixelPosition())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
