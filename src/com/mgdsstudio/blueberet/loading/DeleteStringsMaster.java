package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DeleteStringsMaster extends LoadingMaster{
    public final static byte DELETE_ALL_STARTS_WITH = 0;
    public final static byte DELETE_FIRST_STARTS_WITH = 1;

    public DeleteStringsMaster(int fileNumber, boolean levelType) {
        super(fileNumber, levelType);
    }

    public void deleteAllStringsExceptLevelName(){
        String levelName = fileData[0];
        saveDataToFile(path, levelName, false);
    }

    public void deleteAllStringsExceptLevelNameAndPlayer() {
        String levelName = fileData[0];
        ArrayList<String> newDataArray = new ArrayList<>();
        newDataArray.add(levelName);
        for (int i = 0; i < fileData.length; i++){
            if (fileData[i].contains(Soldier.CLASS_NAME)){
                newDataArray.add(fileData[i]);
            }
            //else newDataArray.add(fileData[i]);
        }
        saveDataArrayToFile(path, newDataArray);
    }

    public void deleteStringsStartsWith(String textToBeFound, byte deleteOption){
        if (deleteOption == DELETE_FIRST_STARTS_WITH){
            boolean alreadyDeleted = false;
            ArrayList<String> newDataArray = new ArrayList<>();
            for (int i = 0; i < fileData.length; i++){
                if (fileData[i].contains(textToBeFound) && !alreadyDeleted){
                    alreadyDeleted = true;
                }
                else newDataArray.add(fileData[i]);
            }
            saveDataArrayToFile(path, newDataArray);
        }
        else if (deleteOption == DELETE_ALL_STARTS_WITH){
            ArrayList<String> newDataArray = new ArrayList<>();
            for (int i = 0; i < fileData.length; i++){
                if (fileData[i].contains(textToBeFound)){
                }
                else newDataArray.add(fileData[i]);
            }
            saveDataArrayToFile(path, newDataArray);
        }
    }

    private boolean saveDataArrayToFile(String path, ArrayList <String> data){
        try {
            if (Program.OS == Program.DESKTOP) {
                PrintWriter output = new PrintWriter((new FileWriter(path, false)));

                for (String dataString : data) output.println(dataString);
                output.flush();
                output.close();
            }
            else if (Program.OS == Program.ANDROID){
                PrintWriter output = new PrintWriter((new FileWriter(path, false)));
                for (String dataString : data) output.println(dataString);
                output.flush();
                output.close();
            }
        }
        catch (IOException e){
            System.out.println("System is busy. Can not delete data from file");
            try {
                String reserveDataPath = path.substring(0,path.length()-4);
                reserveDataPath = reserveDataPath+"-recovery.txt";
                PrintWriter output = new PrintWriter((new FileWriter(reserveDataPath, false)));
                for (String dataString : data) output.println(dataString);
                output.flush();
                output.close();
            }
            catch (Exception e1){
                System.out.println("Can not delete round data again and create recovery");
            }

        }
        boolean saved = true;
        return saved;
    }

    public static boolean saveDataToFile(String path, String data, boolean append){
        try {
            if (Program.OS == Program.DESKTOP) {
                PrintWriter output = new PrintWriter((new FileWriter(path, append)));
                output.println(data);
                output.flush();
                output.close();
            }
            else if (Program.OS == Program.ANDROID){
                PrintWriter output = new PrintWriter((new FileWriter(path, append)));
                output.println(data);
                output.flush();
                output.close();
            }
        }
        catch (IOException e){
            System.out.println("System is busy");
        }
        boolean saved = true;
        return saved;
    }



    public void deleteString(String stringData) {
        replaceString(stringData, null);
    }

    public void replaceStringWithRightOccurence(String stringToBeDeleted, String stringToBeAdded) {
        ArrayList<String> newDataArray = new ArrayList<>();
        boolean stringFound = false;
        for (int i = 0; i < fileData.length; i++){
            if (fileData[i].contains(stringToBeDeleted)){
                System.out.println("This string was founded in data file : In file: " + fileData[i] + " string number: " + i + " contains " + stringToBeDeleted);
                if (stringToBeAdded != null) {
                    newDataArray.add(stringToBeAdded);
                    System.out.println(" and was replaced through " + stringToBeAdded);
                }
                stringFound = true;
            }
            else newDataArray.add(fileData[i]);
        }
        if (!stringFound) {
            System.out.println("Data was not founded " + stringToBeDeleted + "; Array has "+fileData.length+" strings and consists: ");
            //int nearestStringNumber = getNearestStringFor(stringToBeDeleted, fileData);
        }
        saveDataArrayToFile(path, newDataArray);
    }

    public void replaceString(String stringToBeDeleted, String stringToBeAdded) {
        ArrayList<String> newDataArray = new ArrayList<>();
        boolean stringFound = false;
        for (int i = 0; i < fileData.length; i++){
            if (fileData[i].contains(stringToBeDeleted)){
                System.out.println("This string was founded in data file : In file: " + fileData[i] + " string number: " + i + " contains " + stringToBeDeleted);
                if (stringToBeAdded != null) {
                    newDataArray.add(stringToBeAdded);
                    System.out.println(" and was replaced through " + stringToBeAdded);
                }
                stringFound = true;
            }
            else newDataArray.add(fileData[i]);
        }
        if (!stringFound) {
            System.out.println("Data was not founded " + stringToBeDeleted + "; Array has "+fileData.length+" strings and consists: ");
            //String stringToBeReplaces =  getNearestStringFor(stringToBeDeleted, fileData);
            int stringToBeReplacesNumber =  getNearestStringNumberFor(stringToBeDeleted, fileData);
            //

            if (fileData[stringToBeReplacesNumber] != null ){
                //newDataArray.
                newDataArray.set(stringToBeReplacesNumber, stringToBeAdded);
                System.out.println("String was replaced after was not founde " + stringToBeAdded + " nearest string is " + fileData[stringToBeReplacesNumber]);

            }
            else {
                System.out.println("String was not replaced after was not founded " + stringToBeAdded + " nearest string is " + fileData[stringToBeReplacesNumber]);
            }
        }
        saveDataArrayToFile(path, newDataArray);
    }




    private int getNearestStringNumberFor(String stringToBeFounded, String[] fileData) {
        int sourceSymbols = stringToBeFounded.length();
        int maxFoundedSymbols = 0;
        int stringNumber = -1;
        for (int i = 0 ; i < fileData.length; i++){
            if (fileData[i].length() == stringToBeFounded.length()) {
                int foundedSymbols = 0;
                for (int j = 0; j < fileData[i].length(); j++) {
                    if (fileData[i].charAt(j) == stringToBeFounded.charAt(j)) {
                        foundedSymbols++;
                    }
                }
                if (foundedSymbols > maxFoundedSymbols) {
                    maxFoundedSymbols = foundedSymbols;
                    stringNumber = i;
                }
            }
        }
        System.out.println("For the string: " + stringToBeFounded + " there are no occusions. But we have string:  " + fileData[stringNumber]+ "; It have: " + maxFoundedSymbols + " from " + sourceSymbols + " in the source string");
        return stringNumber;
    }

    private String getNearestStringFor(String stringToBeFounded, String[] fileData) {
        int sourceSymbols = stringToBeFounded.length();
        int maxFoundedSymbols = 0;
        int stringNumber = -1;
        for (int i = 0 ; i < fileData.length; i++){
            if (fileData[i].length() == stringToBeFounded.length()) {
                int foundedSymbols = 0;
                for (int j = 0; j < fileData[i].length(); j++) {
                    if (fileData[i].charAt(j) == stringToBeFounded.charAt(j)) {
                        foundedSymbols++;
                    }
                }
                if (foundedSymbols > maxFoundedSymbols) {
                    maxFoundedSymbols = foundedSymbols;
                    stringNumber = i;
                }
            }
        }
        System.out.println("For the string: " + stringToBeFounded + " there are no occusions. But we have string:  " + fileData[stringNumber]+ "; It have: " + maxFoundedSymbols + " from " + sourceSymbols + " in the source string");
        return fileData[stringNumber];
    }

    public void sentStringToTheBack(String stringToBeTransferred){
        ArrayList<String> newDataArray = new ArrayList<>();
        newDataArray.add(fileData[0]);
        newDataArray.add(stringToBeTransferred);
        int oldPos = -1;
        boolean stringFound = false;
        for (int i = 1; i < fileData.length; i++){

            if (fileData[i].contains(stringToBeTransferred)){
                oldPos = i;
                System.out.println("This string was founded in data file : In file: " + fileData[i] + " string number: " + i + " contains " + stringToBeTransferred + " and has pos: " + oldPos);
                stringFound = true;
            }
            else newDataArray.add(fileData[i]);

        }
        if (!stringFound) {
            System.out.println("Data was not founded " + stringToBeTransferred + "; Array has "+fileData.length+" strings and consists: ");
            for (int i = 0; i < fileData.length; i++){
                System.out.println(fileData[i]);
            }
            System.out.println(" String was only added");
        }
        saveDataArrayToFile(path, newDataArray);
    }

    public void sentStringToTheFront(String stringToBeTransferred){
        ArrayList<String> newDataArray = new ArrayList<>();
        if (stringToBeTransferred.length()>0) {
            int oldPos = -1;
            boolean stringFound = false;
            for (int i = 0; i < fileData.length; i++) {
                if (fileData[i].contains(stringToBeTransferred)) {
                    oldPos = i;
                    System.out.println("This string was founded in data file : In file: " + fileData[i] + " string number: " + i + " contains " + stringToBeTransferred + " and has pos: " + oldPos);
                    stringFound = true;
                }
                else newDataArray.add(fileData[i]);
            }
            if (!stringFound) {
                System.out.println("Data was not founded " + stringToBeTransferred + "; Array has " + fileData.length + " strings and consists: ");
                for (int i = 0; i < fileData.length; i++) {
                    System.out.println(fileData[i]);
                }
                System.out.println(" String was only added");
            }
            newDataArray.add(stringToBeTransferred);
            saveDataArrayToFile(path, newDataArray);
        }
        else{
            System.out.println("This string is too short " + stringToBeTransferred);
        }
    }


}
