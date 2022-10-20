package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

import java.io.*;
import java.util.ArrayList;

public class FilesCopyMaster {
    private PApplet engine;

    public FilesCopyMaster(PApplet pApplet){
        this.engine = pApplet;
    }

    public void copyToDataInCache(String sourceFileName, String outputFileName){
        InputStream stream = null;
        FileOutputStream out = null;
        String sourceFullPath = sourceFileName;
        System.out.println("Source file is: " + sourceFullPath);
        File sourceFile = new File(sourceFullPath);
        if (!sourceFile.exists() && sourceFileName.contains("al player data tem")) {
            System.out.println("it is the template copierer.");
            ArrayList<String> data = null;
            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                data = StringLibrary.getFilesListInAssetsFolder();
            //}
            if (data == null) {
                //System.out.println("Data is null. SDK: " + android.os.Build.VERSION.SDK_INT + " must be: " + android.os.Build.VERSION_CODES.O);
                //StringLibrary.getFilesListInAssetsFolderForOldAndroid();
            }
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).contains("al player data tem")){
                    System.out.println("File:" + data.get(i) + " must be used") ;
                    sourceFile = new File(data.get(i));
                    break;
                }
            }
        }
        //if (sourceFile.exists()) {
            try {
                //stream = engine.getActivity().getAssets().open(sourceFile.getName());
                stream = Program.iEngine.createInputStreamInAssetsForFile(sourceFile.getName());
                //Program.engine.getActivity().getAssets().open(Program.getAbsolutePathToAssetsFolder( file));
                //return Program.engine.getActivity().getAssets().open( file);

                out = new FileOutputStream(FileManagement.getPathToCacheFilesInAndroid() + outputFileName);
                byte[] buf = new byte[8192];
                int length;
                while ((length = stream.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }
                System.out.println("File was copied under the new name: " + outputFileName);

            } catch (IOException ex) {
                System.out.println("Maybe we can not find the file: " + sourceFileName + " under the path: " + sourceFullPath);
                throw new RuntimeException(ex);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        /*else {
            System.out.println("Template file does not exists. There are files: ");
            ArrayList <String> data = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                data = StringLibrary.getFilesListInAssetsFolder();
            }
            for (int i = 0; i < data.size();i++){
                System.out.println(data.get(i));
            }
            copyFileMultiplatform(sourceFileName, AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+outputFileName);
        }*/

    }

    public void copyFileMultiplatform(String sourcePath, String outputPath) {
        InputStream input = null;
        FileOutputStream output = null;
        File inputFile = new File(sourcePath);
        if (inputFile.exists()) {

                try {
                    // Create FileInputStream and FileOutputStream objects
                    input = new FileInputStream(sourcePath);
                    output = new FileOutputStream(outputPath);
                    byte[] buf = new byte[1024];
                    int bytesRead;

                    // Write bytes to the destination
                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Could not copy the file to the destination: " + outputPath + ". Check if the folder or file already exists.");
                } finally {
                    // Close the streams
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("File was copied under the new name: " + outputPath);

        }
        else System.out.println("File doesnot exist");
    }


}
