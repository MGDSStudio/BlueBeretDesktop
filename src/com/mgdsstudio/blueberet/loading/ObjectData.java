package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;

public class ObjectData {
    private GameObject gameObject;

    public ObjectData(GameObject gameObject){
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public String getStringSaveData(){
        Object objectClass = gameObject.getClass();
        Person person = (Person) gameObject;
        return person.getStringData();
        /*
        if (objectClass == Soldier.class){
            Soldier soldier = (Soldier)gameObject;
            return soldier.getStringData();
        }
        else if (objectClass == Gumba.class){
            Gumba gumba = (Gumba)gameObject;
            return gumba.getStringData();
        }
        else if (objectClass == Bowser.class){
            Bowser bowser = (Bowser)gameObject;
            return bowser.getStringData();
        }
        else if (objectClass == Koopa.class){
            Koopa koopa = (Koopa)gameObject;
            return koopa.getStringData();
        }

        else {
            System.out.println("For this game object I don't have string data");
            return "";
        }

         */
    }





}
