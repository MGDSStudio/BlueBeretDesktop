package com.mgdsstudio.blueberet.gamelibraries;

import android.os.Build;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StringLibrary {

    private static ArrayList<String> getFilesListForAndroid(){
        ArrayList<String> names = new ArrayList<>();
        String [] files = AndroidSpecificFileManagement.getFilesListInAssets("");
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
        ArrayList<String> names = new ArrayList<>();

            if (Program.OS == Program.DESKTOP) {
                //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    try {
                        try (Stream<Path> paths = Files.walk(Paths.get(Program.getRelativePathToAssetsFolder()))) {
                            List<String> files = paths.filter(x -> Files.isRegularFile(x))
                                    .map(Path::toString)
                                    .collect(Collectors.toList());
                            // print all files
                            files.forEach(System.out::println);
                            for (int i = 0; i < files.size(); i++) names.add(files.get(i));

                        } catch (IOException ex) {
                            System.out.println("Can not get files list for this API version");
                            ex.printStackTrace();
                        }
                        //}
                        return names;
                    } catch (Exception e) {

                    }
                //}
            }
            else {
                names = getFilesListForAndroid();

            }

        return names;
    }

    /*
    public static ArrayList<String> getFilesListForAndroid(){
        if (Programm.OS == Programm.ANDROID) {
            ArrayList<String> names = new ArrayList<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try (Stream<Path> paths = Files.walk(Paths.get("Assets"))) {
                    List<String> files = paths.filter(x -> Files.isRegularFile(x))
                            .map(Path::toString)
                            .collect(Collectors.toList());
                    // print all files
                    files.forEach(System.out::println);
                    for (int i = 0; i < files.size(); i++) names.add(files.get(i));

                } catch (IOException ex) {
                    System.out.println("Can not get files list for this API version");
                    ex.printStackTrace();
                }
            }
            return names;
        }
        return null;
    }*/



    /*
    public static  ArrayList<String> getFilesListInAssets(){
        ArrayList <String> names = new ArrayList<>();
        String pathToCache = "";
        //System.out.println("Path to find files in cache: " + Environment.getExternalStorageDirectory() + AndroidSpecificFileManagement.SD_CACHE_PATH);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (Stream<Path> paths = Files.walk(Paths.get(pathToCache))) {
                List<String> files = paths.filter(x -> Files.isRegularFile(x))
                        .map(Path::toString)
                        .collect(Collectors.toList());
                files.forEach(System.out::println);
                for (int i = 0; i < files.size(); i++) names.add(files.get(i));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("Your android SDK is " + android.os.Build.VERSION.SDK_INT  + " must be at least " + android.os.Build.VERSION_CODES.O);
            String directoryPath;
            if (pathToCache.length()>1) directoryPath = pathToCache.substring(0,pathToCache.length()-1);
            else directoryPath = pathToCache;
            names = getFilesListInDirectory(directoryPath);
        }
        return names;
    }*/

    public static  ArrayList<String> getFilesListInCache(){
        ArrayList <String> names = new ArrayList<>();
        String pathToCache = Environment.getExternalStorageDirectory() + AndroidSpecificFileManagement.SD_CACHE_PATH;
        System.out.println("Path to find files in cache: " + Environment.getExternalStorageDirectory() + AndroidSpecificFileManagement.SD_CACHE_PATH);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (Stream<Path> paths = Files.walk(Paths.get(pathToCache))) {
                List<String> files = paths.filter(x -> Files.isRegularFile(x))
                        .map(Path::toString)
                        .collect(Collectors.toList());
                files.forEach(System.out::println);
                for (int i = 0; i < files.size(); i++) names.add(files.get(i));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("Your android SDK is " + android.os.Build.VERSION.SDK_INT  + " must be at least " + android.os.Build.VERSION_CODES.O);
            String directoryPath = pathToCache.substring(0,pathToCache.length()-1);
            names = getFilesListInDirectory(directoryPath);
        }
        return names;
    }

    public static ArrayList<String> getFilesListInDirectory(String pathToDirectory){
        Program.engine.requestPermission("android.permission.MANAGE_EXTERNAL_STORAGE");
        File dir = new File(pathToDirectory);
        System.out.println("Try to get files list for directory: " + pathToDirectory);
        ArrayList<String> namesList = new ArrayList<>();
        if (!dir.isDirectory()) System.out.println("This is not a directory: ");
        if (!dir.isFile()) System.out.println("This is not a file");
        if (!dir.exists()) System.out.println("This directory doesn't exist");
        File[] files = dir.listFiles();
        String[] names = dir.list();
        /*
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                namesList.add(files[i].getName());
                System.out.println("found file: " + files[i]);
            }
        }*/
        //else
        if (names != null) {
            for (int i = 0; i < names.length; i++) {
                namesList.add(names[i]);
                //System.out.println("found name: " + names[i]);
            }
        }
        else System.out.println("Can not get files in " + pathToDirectory);
        return namesList;
    }

    /*
    public static String getLevelNameByPath(String path){

    }*/


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
                /*
                if ((array.get(i).substring(0, stringToBeFound.length()-1) == "Assets") || (array.get(i).substring(0, stringToBeFound.length()-1).equals("Assets"))){
                    data.add(array.get(i));
                }*/
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


    public static void getFilesListInAssetsFolderForOldAndroid() {

    }
}
