package com.mgdsstudio.blueberet.menusystem.gui;

import java.util.ArrayList;
import java.util.HashMap;

class TextAreaArray{
    //private ArrayList <String> mainString, substring;
    private HashMap <Integer, ArrayList<String>> array;
    private int count;

    TextAreaArray(){
        array = new HashMap<>();
    }

    void addAbsatz(ArrayList <String> absatz){
        array.put(count, absatz);
        count++;
    }

    ArrayList <String> getAbsatz(int number){
        return array.get(number);
    }
}
