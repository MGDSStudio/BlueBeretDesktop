package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamelibraries.FileManagement;

import java.io.File;

public class AddByBackPressingMenuController {
    private final int NUMBER_TO_APPEAR_ADD = 2;
    private int actualNumber = -1;
    private boolean active;
    private final static String FILE_NAME = "Back launches.txt";
    private static String path;

    public AddByBackPressingMenuController() {
        if (Program.OS == Program.ANDROID){
            if (Program.withAdds) {
                active = true;
            }
            path = FileManagement.getPathToCacheFilesInAndroid()+FILE_NAME;
        }
        else System.out.println("No adds for Desktop ");
    }

    public boolean mustBeFullScreenAddShown(){
        if (!active){
            return false;
        }
        else {
            File file = new File(path);
            if (file.exists()){
                actualNumber = getIntFromFile();

                if (actualNumber >= 0) {
                    if (actualNumber % NUMBER_TO_APPEAR_ADD == 0) {
                        return true;
                    }
                    else return false;
                }
                else return false;
            }
            else {
                System.out.println("This is the first launch");
                actualNumber = 1;
                createClearFile();
                return false;
            }
        }
    }

    public void incrementLaunchesNumber(){
        if (active){
            File file = new File(path);
            if (file.exists()){
                int valueFromFile = getIntFromFile();
                if (valueFromFile < 0) valueFromFile = 0;
                valueFromFile++;
                actualNumber = valueFromFile;
                fillFileWithData(valueFromFile);
                System.out.println("New data is: " + actualNumber);
            }
            else {
                System.out.println("I can not increment. I need to create a clear file again");
                createClearFile();
            }
        }
        else {
            System.out.println(" On this system I can not save data");
        }
    }

    private int getIntFromFile() {
        String [] data = Program.engine.loadStrings(path);
        if (data != null){
            try {
                int value = Integer.parseInt(data[0]);
                System.out.println("Value from file = " + value + " file has only " + data.length + " strings ");
                return value;
            }
            catch (Exception e){
                System.out.println("Can not read file " + path);
                e.printStackTrace();
                return -1;
            }
        }
        else return -1;
    }

    private void createClearFile() {
        String data = "1";
        fillFileWithData(data);
    }

    private void fillFileWithData(String data){
        String [] dataArray = new String[1];
        dataArray[0]= data;
        Program.engine.saveStrings(path, dataArray);
        System.out.println("Data " + dataArray[0] + " was saved to " + path);
    }

    private void fillFileWithData(int data){
        String dataArray = ""+data;
        fillFileWithData(dataArray);
    }

}
