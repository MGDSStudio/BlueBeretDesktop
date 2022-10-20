package com.mgdsstudio.blueberet.gamelibraries;


import com.mgdsstudio.blueberet.mainpackage.Program;


import java.util.ArrayList;


public abstract class StringLibrary {

    public static ArrayList<String> getFilesListForAndroid(){
        ArrayList<String> names = new ArrayList<>();
        String [] files = FileManagement.getFilesListInAssets("");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                names.add(files[i]);
            }
        }
        else {
            System.out.println("Can not get list of files in assets folder in android mode");
            return null;
        }
        return names;
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String> getFilesListInAssetsFolder(){

        return Program.iEngine.getFilesListInAssetsFolder();

    }



    public static  ArrayList<String> getFilesListInCache(){
        return Program.iEngine.getFilesListInCache();
    }

    public static ArrayList<String> getFilesByPrefixAndSuffix(ArrayList<String> data, String prefix, String suffix){
        ArrayList <String> names = new ArrayList<>();
        for (String name : data){
            if (name.contains(prefix)){
                if (name.contains(suffix)){
                    names.add(name);
                }
            }
        }
        return names;
    }

    public static String deleteAssetsFromPath(String path){
        String newPath = new String();
        final String toFindString = "Assets\\";
        if (path.contains(toFindString)){
            int endOfAssetsName = path.indexOf(toFindString)+toFindString.length();
            newPath = path.substring(endOfAssetsName);
        }
        else newPath = path;
        return newPath;
    }

    public static boolean isDigit(char testChar){
        if (testChar == '1' || testChar == '2' || testChar == '3' ||testChar == '4' || testChar == '5' || testChar == '6' || testChar == '7' || testChar == '8' || testChar == '9' || testChar == '-') return true;
        else return false;
    }

    public static int getLevelNumberFromName(String levelName){
        int number = 0;
        //if (Program.debug){

       // }
        if (levelName.contains(Program.USER_LEVELS_PREFIX)){
            String stringValue = "";
            String stringToBeTested = levelName.substring(levelName.indexOf(Program.USER_LEVELS_PREFIX)+Program.USER_LEVELS_PREFIX.length()-1);
            for (int i = 0; i < stringToBeTested.length(); i++){
                char testChar = stringToBeTested.charAt(i);
                if (isDigit(testChar)){
                    stringValue+=(testChar);
                }
                else {
                    if (testChar == '0'){
                        stringValue+="0";
                    }
                    //else System.out.println("This char "+testChar+" is not a part of the level number ");
                }
            }
            number = Integer.parseInt(stringValue);

        }
        //System.out.print("This level name " + levelName + " is level " );
        //System.out.println(number );
        return number;
    }

    public static String[] convertArrayListToArray(ArrayList<String> list){
        String [] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
    }

    public static ArrayList<String> deleteFromArrayAllStringsExceptOne(ArrayList<String> array, String stringToBeFound) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).contains(stringToBeFound)) {
                data.add(array.get(i));
            }
        }
        return data;
    }

    public static ArrayList<String> deleteFromArrayAllStringsExceptOneWhichStartFrom(ArrayList<String> array, String stringToBeFound) {
        ArrayList<String> data = new ArrayList<>();
        System.out.println("This function is wrong and not is used");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).contains(stringToBeFound)) {

                if ((array.get(i).substring(0, stringToBeFound.length()-1) == stringToBeFound) || (array.get(i).substring(0, stringToBeFound.length()-1).equals(stringToBeFound))){
                    data.add(array.get(i));
                }


            }

        }
        return data;
    }

    public static ArrayList<String> deleteFromArrayAllStringsExceptOne(String[] array, String stringToBeFound) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i].contains(stringToBeFound)) {
                data.add(array[i]);
            }
        }
        return data;
    }

    public static ArrayList<String> leaveInArrayFilesWithExtension(ArrayList<String> array, String extension) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).contains(extension)) {
                data.add(array.get(i));
            }
        }
        for (String path : data) {
            System.out.println(path);
        }
        return data;
    }

    public static String getStringAfterPathDevider(String sourceString){
        String newString = "";
        for (int i = sourceString.length()-1; i>=0; i--){
            if (sourceString.charAt(i) != '/' && sourceString.charAt(i) != '\\') {
                newString = sourceString.charAt(i)+newString;
            }
            else {
                System.out.println("String was: " + sourceString + " is now: " + newString);
                return newString;
            }
        }
        System.out.println("In string: " +sourceString+ " there are no devider " + '/' +"or"+ '\\');
        return sourceString;
    }

}
