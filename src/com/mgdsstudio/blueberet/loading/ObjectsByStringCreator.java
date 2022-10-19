package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameprocess.GameRound;

import java.util.ArrayList;

public class ObjectsByStringCreator extends LoadingMaster{
    //String [] dataString;

    public ObjectsByStringCreator(String[] dataString) {
        super();
        fileData = dataString;
    }


    @Override
    public ArrayList<RoundElement> getRoundElements() {
        return super.getRoundElements();
    }


    public ArrayList<Person> getPersons(GameRound gameRound) {
        return super.getPersons(fileData, gameRound);
    }
}
