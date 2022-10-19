package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveMaster extends ExternalRoundDataFileController{
    GameRound gameRound;
    GameObject gameObject;
    byte fileNumber;
    boolean levelType;
    private String dataString;
    private static final int elementsNumber = 1;

    public SaveMaster(GameRound gameRound, GameObject gameObject, byte fileNumber, boolean levelType){
        super(fileNumber, levelType);
        dataString = new String();
        this.gameRound = gameRound;
        this.gameObject = gameObject;
        this.fileNumber = fileNumber;
        this.levelType = levelType;
    }


    public boolean saveRoundElement(){
        if (gameObject.getClass() == RoundBox.class){
            createRoundBox();
            return true;
        }
        return true;
    }

    private void createRoundBox() {
        addNameAndNumber(roundBoxType);
        addPosition(gameObject.getPixelPosition());
        addAngle(gameObject.body.getAngle());
        addWidth(gameObject.getWidth());
        addHeight(gameObject.getHeight());
        addLife(gameObject.getLife());
        addBodyType(gameObject.body.getType());
        RoundBox roundElement = (RoundBox)gameObject;
        addSpring(roundElement.hasSpring());
        endDataString();
    }

    public void saveData(){
        addDataToFile(path, dataString);
    }



    public void saveGraphicData(String objectType, String fileName, int [] graphicData){
        if (objectType == roundBoxType){
            RoundElement roundElement = (RoundBox) gameObject;
            roundElement.loadImageData(fileName, (int)1,(int)131,(int)16,(int)146,(int)roundElement.getWidth(),(int)roundElement.getHeight(),true);
            roundElement.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(roundElement.getSprite().getPath()));
        }
        addGraphicData(fileName, graphicData);
        /*
        Vec2 leftUpper = new Vec2(graphicData[0], graphicData[1]);
            Vec2 rightLower = new Vec2(graphicData[2], graphicData[3]);
            int width = (int) graphicData[4];
            int height = (int) graphicData[5];
            boolean fillArea = true;
            if (graphicData[6] == 0) fillArea = false;
            RoundBox roundBox = (RoundBox) gameObject;
            roundBox.loadImageData(path, (int)leftUpper.x, (int) leftUpper.y, (int) rightLower.x, (int) rightLower.y, width, height, fillArea);
        */
    }

    private void addGraphicData(String fileName, int [] graphicData){
        dataString+=fileName;
        dataString+=GRAPHIC_NAME_END_CHAR;
        dataString+=graphicData[0];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[1];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[2];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[3];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[4];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[5];
        addDeviderBetweenGraphicDataValues();
        dataString+=graphicData[6];
    }

    private void addDeviderBetweenGraphicDataValues(){
        dataString+=DIVIDER_BETWEEN_GRAPHIC_DATA;
    }

    private void endDataString() {
        if (dataString.charAt(dataString.length()-1) == DIVIDER_BETWEEN_VALUES){
            dataString = dataString.substring(0, (dataString.length()-1));
        }
        dataString+=GRAPHIC_NAME_START_CHAR;
    }

    private void addSpring(boolean withSpring){
        if (withSpring) dataString+=1;
        else dataString+=0;
        addDeviderBetweenMainDataValues();
    }

    private void addBodyType(BodyType bodyType){
        byte value = 0;
        if (bodyType == BodyType.STATIC) value = 0;
        else if (bodyType == BodyType.KINEMATIC) value = 1;
        else value = 2;
        dataString+=(value);
        addDeviderBetweenMainDataValues();
    }

    private void addLife(int life){
        dataString+=(int)(life);
        addDeviderBetweenMainDataValues();
    }

    private void addHeight(float height) {
        dataString+=(int)(height);
        addDeviderBetweenMainDataValues();
    }

    private void addWidth(float width) {
        dataString+=(int)(width);
        addDeviderBetweenMainDataValues();
    }

    private void addAngle(float angleInRadians) {
        dataString+=(int)(PApplet.degrees(angleInRadians));
        addDeviderBetweenMainDataValues();
    }

    private void addPosition(PVector absolutePosition) {
        dataString+=(int)(absolutePosition.x);
        addDeviderBetweenMainDataValues();
        dataString+=(int)(absolutePosition.y);
        addDeviderBetweenMainDataValues();
    }

    private void addDeviderBetweenMainDataValues(){
        dataString+=DIVIDER_BETWEEN_VALUES;
    }

    private void addNameAndNumber(String type){
        dataString = new String();
        dataString = roundBoxType;
        dataString+=' ';
        dataString+=elementsNumber;
        dataString+=MAIN_DATA_START_CHAR;
        //elementsNumber++;
    }

    public static boolean saveDataToFile(String path, String data){
        try {
            if (Program.OS == Program.DESKTOP) {
                PrintWriter output = new PrintWriter((new FileWriter(Program.getAbsolutePathToAssetsFolder(path) , false)));
                output.println(data);
                output.flush();
                output.close();
            }
            else if (Program.OS == Program.ANDROID){
                PrintWriter output = new PrintWriter((new FileWriter(path, false)));
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

    public static boolean addDataToFile(String path, String data){
        try {
            if (Program.OS == Program.DESKTOP) {
                PrintWriter output = new PrintWriter((new FileWriter(Program.getAbsolutePathToAssetsFolder(path), true)));
                //output.println(); //!!!!!
                output.println(data);
                output.flush();
                output.close();
            }
            else if (Program.OS == Program.ANDROID){
                PrintWriter output = new PrintWriter((new FileWriter(path, true)));
                //output.println();   //!!!!!
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
    /*
    BufferedReader reader = Game2D.engine.createReader(path);
        ArrayList <String> fileContent = new ArrayList<>();
        String actualString;
        try {
            while ((actualString = reader.readLine()) != null) {
                fileContent.add(actualString);
            }
            reader.close();
        }
        catch (IOException e){
                System.out.println("Exception");
        }
        PrintWriter output;
        if (Game2D.OS == Game2D.WINDOWS) output = Game2D.engine.createWriter("Assets//" + path);
        else output = Game2D.engine.createWriter(path);
        output.println(dataString);
        for (int i = 0; i < (fileContent.size()); i ++){
            output.println(fileContent.get(i));
        }
        output.println(dataString);
        output.flush();
        output.close();
        boolean saved = true;
        return saved;
    */

    /*
    String [] fileContent = Game2D.engine.loadStrings(path);
        PrintWriter output;
        if (Game2D.OS == Game2D.WINDOWS) output = Game2D.engine.createWriter("Assets//" + path);
        else output = Game2D.engine.createWriter(path);
        output.println(dataString);
        for (int i = 0; i < (fileContent.length); i ++){
            output.println(fileContent[i]);
        }
        output.flush();
        output.close();
        boolean saved = true;
        return saved;
    */

    /*
    String [] fileContent = Game2D.engine.loadStrings(path);
        ArrayList <String> dataA
        String [] data = new String[fileContent.length+1];
        for (int i = 0; i < (fileContent.length-1); i ++){
            data[i] = fileContent[i];
        }
        try {
            data[data.length] = dataString;
        }
        catch (Exception e){
            try {
                data[data.length - 1] = dataString;
            }
            catch (Exception e1){

            }
        }
        boolean saved = false;
        try {
            if (Game2D.OS == Game2D.WINDOWS) Game2D.engine.saveStrings("Assets//" + path, data);
            else if (Game2D.OS == Game2D.ANDROID) Game2D.engine.saveStrings(path, data);
            System.out.println("This object was succesfully added to the level data");
            saved = true;
        }
        catch (Exception e){
            System.out.println("File is busy. Can not save data to " + path + ". Exception: " + e);
        }
        return saved;
    */
}
