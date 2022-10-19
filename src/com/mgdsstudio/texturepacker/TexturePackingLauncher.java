package com.mgdsstudio.texturepacker;

import com.mgdsstudio.blueberet.mainpackage.Program;

import processing.core.PApplet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TexturePackingLauncher extends PApplet{
    //private PImage pic;
    private boolean dataMustBeLoaded;

    private boolean loadAllTheData = false;
    private boolean loadHud = false;
    private boolean loadHowToPlay = false;
    private boolean loadBackgrounds = false;
    private boolean loadGirl = false;

    //private final String specificFileToBeConverted = "Dragonfly.gif";
    //private final String specificFileToBeConverted = "Boar spritesheet.png";
    //private final String specificFileToBeConverted = "Spider spritesheet.png";
    //private final String specificFileToBeConverted = "HUD.png";
    //private final String specificFileToBeConverted = "Girl smg.gif";
    private final String specificFileToBeConverted =  "Mercenary SMG.png";
    private final String specificFileToBeRecovered = null;
    //private final String specificFileToBeRecovered = "Boar spritesheet"+TextureEncryptManager.MGDS_FILE_EXTENSION;
//private final String specificFileToBeRecovered = "HUD"+TextureEncryptManager.MGDS_FILE_EXTENSION;



    public static void main(String [] passedArgs){
        System.out.println("Converter launched");
        String[] appletArgs = new String[]{"com.mgdsstudio.texturepacker.TexturePackingLauncher"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    @Override
    public void settings(){
        size(320, 320);
    }

    @Override
    public void setup(){
        System.out.println("Try to convert");
        if (loadAllTheData) loadAllData();
        //else loadOnlyGirlEndEnemies();
        else{
            if (loadGirl){
                String [] girlFileNames = getGirlTilesets();
                for (int i = 0; i < girlFileNames.length; i++){
                    TextureEncryptManager manager = new TextureEncryptManager(Program.getAbsolutePathToAssetsFolder(girlFileNames[i]), this);
                    manager.encrypt();
                }
            }
            if (loadHud){
                TextureEncryptManager manager = new TextureEncryptManager(Program.getAbsolutePathToAssetsFolder("HUD.png"), this);
                manager.encrypt();
            }
            if (loadHowToPlay){
                TextureEncryptManager manager = new TextureEncryptManager(Program.getAbsolutePathToAssetsFolder("How to play menu graphic.gif"), this);
                manager.encrypt();
                TextureEncryptManager manager2 = new TextureEncryptManager(Program.getAbsolutePathToAssetsFolder("Menu graphic original.gif"), this);
                manager2.encrypt();
            }
            if (loadBackgrounds){
                loadBackgrounds();
            }
            if (specificFileToBeConverted != null){
                System.out.println("Try to convert file: " + specificFileToBeConverted + " from path: " + Program.getAbsolutePathToAssetsFolder(specificFileToBeConverted));
                TextureEncryptManager manager = new TextureEncryptManager(Program.getAbsolutePathToAssetsFolder(specificFileToBeConverted), this);
                manager.encrypt();
            }
            if (specificFileToBeRecovered != null){
                TextureDecodeManager manager = new TextureDecodeManager(Program.getAbsolutePathToAssetsFolder(specificFileToBeRecovered), this);
                manager.saveAsImage("png");
            }
        }

        //MainDialog.main(null);
        //MainDialog.setEngine(this);
        /*
        TextureEncryptManager manager = new TextureEncryptManager("Assets//File.png", this);
        manager.encrypt();
        TextureDecodeManager decodeManager = new TextureDecodeManager("Assets//File"+TexturePacker.FILE_EXTENSION, this);
        pic = decodeManager.getDecodedImage();*/
    }

    private String[] getGirlTilesets() {
        File dir = new File(Program.getRelativePathToAssetsFolder()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        ArrayList <String> girl = new ArrayList<>();
        for (int i = 0; i < arrFiles.length; i++){
            if (arrFiles[i].getName().contains("Girl ")){
                if (arrFiles[i].getName().contains(".gif")){
                    girl.add(arrFiles[i].getName());
                }
            }
        }
        String [] array  = new String[girl.size()];
        for (int i = 0; i < array.length; i++) array[i] = girl.get(i);
        return array;
    }

    private void loadBackgrounds() {
        File dir = new File(Program.getRelativePathToAssetsFolder()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        File[] backgrounds = getBackgroundsGraphic(arrFiles);
        System.out.println("Files list: " + backgrounds.length + " files");
        for (int i = 0; i < backgrounds.length; i++){
            if (isGraphicFile(backgrounds[i])){
                TextureEncryptManager manager = new TextureEncryptManager(backgrounds[i].getPath(), this);
                manager.encrypt();
                System.out.println("Already encrypted: " + (int)(100*(float)(i+1)/(float)backgrounds.length) + " %. Now try to encrypt: " + backgrounds[i].getName());
            }
        }
        System.out.println("Encrypting ended");
    }

    private void loadOnlyGirlEndEnemies(){
        File dir = new File(Program.getRelativePathToAssetsFolder()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        //List<File> lst = Arrays.asList(arrFiles);
        File[] persons = getOnlyPersonsGraphic(arrFiles);

        System.out.println("Files list: " + persons.length + " files");
        for (int i = 0; i < persons.length; i++){
            if (isGraphicFile(persons[i])){
                TextureEncryptManager manager = new TextureEncryptManager(persons[i].getPath(), this);
                manager.encrypt();
                System.out.println("Already encrypted: " + (int)(100*(float)i/(float)persons.length) + " %. Now try to encrypt: " + persons[i].getName());
            }
        }
        System.out.println("Encrypting ended");

    }

    private File[] getOnlyPersonsGraphic(File[] files) {
        ArrayList <File> persons = new ArrayList<>();
        for (int i = 0; i < files.length; i++){
            String name = files[i].getName();
            if (isPersonFileName(name)){
                persons.add(files[i]);
            }
        }
        File[] onlyPersons = new File[persons.size()];
        for (int i = 0; i < onlyPersons.length; i++){
            onlyPersons[i] = persons.get(i);
        }
        return onlyPersons;
    }

    private File[] getBackgroundsGraphic(File[] files) {
        ArrayList <File> backgrounds = new ArrayList<>();
        for (int i = 0; i < files.length; i++){
            String name = files[i].getName();
            if (name.contains("Background")){
                backgrounds.add(files[i]);
            }
        }
        File[] onlyPersons = new File[backgrounds.size()];
        for (int i = 0; i < onlyPersons.length; i++){
            onlyPersons[i] = backgrounds.get(i);
        }
        return onlyPersons;
    }

    private boolean isPersonFileName(String name) {
        if (name.contains("Girl") || name.contains("Spider")  || name.contains("Bat")  || name.contains("Dragon")  || name.contains("Bowser")  || name.contains("Koopa")  || name.contains("Dragonfly")  || name.contains("Snake")  || name.contains("Flower")  || name.contains("Lizard")){
            return true;
        }
        else return false;
    }

    private void loadAllData(){
        File dir = new File(Program.getRelativePathToAssetsFolder()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);
        System.out.println("Files list: " + lst.size() + " files");

        for (int i = 0; i < arrFiles.length; i++){
            if (isGraphicFile(arrFiles[i])){
                TextureEncryptManager manager = new TextureEncryptManager(arrFiles[i].getPath(), this);
                manager.encrypt();
                System.out.println("Already encrypted: " + (int)(100*(float)i/(float)arrFiles.length) + " %");
            }
        }

    }

    private boolean isGraphicFile(File filePath) {
        String name =  filePath.getName();
        for (int i = (name.length()-1); i >= 0; i--){
            if (name.charAt(i) =='.'){
                String extension = name.substring(i+1);
                if (isExtensionGraphic(extension)){
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    private boolean isExtensionGraphic(String extension) {
        if (extension.contains("png") || extension.contains("jpg") || extension.contains("gif") || extension.contains("PNG") || extension.contains("JPG") || extension.contains("GIF") || extension.contains("JPEG") ){
            return true;
        }
        else return false;
    }

    @Override
    public void draw(){
        if (dataMustBeLoaded){

            dataMustBeLoaded = false;
        }
        //image(pic,0,0);
    }




    public void setVisible(boolean b) {
        String [] data = new String[1];
        data[0] = "no data";
        main(data);
    }


}
