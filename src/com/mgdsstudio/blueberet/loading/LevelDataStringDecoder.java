package com.mgdsstudio.blueberet.loading;

public class LevelDataStringDecoder {
    protected String stringToBeDecoded;
    //byte actualDataElement = 0;
    int actualStartCharNumber = 0;
    //boolean valuesDecoded = false;


    public LevelDataStringDecoder(String stringToBeDecoded){
        this.stringToBeDecoded = stringToBeDecoded;
    }

    /*
    int getNextValue(char devidingChar, char endChar){
        System.out.println("stringToBeDecoded " + stringToBeDecoded);
        if (valuesDecoded == false) {
            //System.out.print("String to be decoded " + stringToBeDecoded + "; ");
            String textDataToBeReturn;
            try {
                textDataToBeReturn = stringToBeDecoded.substring(actualStartCharNumber, stringToBeDecoded.indexOf(devidingChar));
                System.out.println("Value: " + textDataToBeReturn);

            }
            catch (Exception e1) {
                try {
                    textDataToBeReturn = stringToBeDecoded.substring(actualStartCharNumber, stringToBeDecoded.indexOf(endChar));
                    System.out.println("Last value: " + textDataToBeReturn);
                    valuesDecoded = true;
                }
                catch (Exception e2) {
                    System.out.println("Wrong data structure");
                    valuesDecoded = true;
                    return -1;
                }
            }
            stringToBeDecoded = stringToBeDecoded.substring(stringToBeDecoded.indexOf(devidingChar)+1);
            System.out.println("Text representation:" + textDataToBeReturn);
            System.out.println("Int value:" + Integer.parseInt(textDataToBeReturn));
            return Integer.parseInt(textDataToBeReturn);
        }
        else return  -1;
    }
    */

    public String getClassNameFromDataString(){
        String className = "";
        System.out.println("String to be decoded: " +stringToBeDecoded);
        if (stringToBeDecoded != null){
            className = stringToBeDecoded.substring(0,stringToBeDecoded.indexOf(" "));
            /*
            if (stringToBeDecoded.contains(RoundBox.CLASS_NAME)){
                if (isStringOnStart(RoundBox.CLASS_NAME)) return RoundBox.CLASS_NAME;
            }
            */
        }
        else {

        }
        //System.out.println("Class name: " + className + "; equals " + RoundBox.CLASS_NAME + "; " + (RoundBox.CLASS_NAME.equals(className)));
        //return RoundBox.CLASS_NAME;
        return className;
    }



    public String getTextDataFromDataString(String text, String type) {
        String textData = "";
        if (text.length() > 0) {
            if (text.contains(type)) {
                if (type.equals(text.substring(0, type.length()))) {
                    textData = text.substring(text.indexOf(LoadingMaster.MAIN_DATA_START_CHAR) + 1);
                    ;
                } else {
                    System.out.println("This object name is not at begin");
                }
            } else System.out.println("This string " + textData + " has no " + type + " substring");
        }
        return textData;
    }

    private boolean isStringOnStart(String toFindString){
        if (stringToBeDecoded.indexOf(toFindString) == 1 || stringToBeDecoded.indexOf(toFindString) == 0) return true;
        else return false;
    }

    static String getStringFromValue(String string, char startChar){
        System.out.println("Test string: " + string + "; start char: " + startChar);
        string = string.substring(string.indexOf(startChar)+1);
        System.out.println("Rest string: " + string);
        return  string;
    }

    static String getStringToValue(String string, char endChar){
        //System.out.println("Test string: " + string + "; start char: " + startChar + "; end char: " + endChar);
        string = string.substring(0, string.indexOf(endChar));
        //System.out.println("Rest string: " + string);
        return  string;
    }

    public int [] getValuesUpdated(char startChar, char dividingChar, char endChar){
        int [] values = new int[valuesNumber(dividingChar, endChar)];
        String testString = "";
        testString = testString.concat(stringToBeDecoded);
        testString+= stringToBeDecoded;
        //System.out.println("String to be decoded: " + testString + "; End");
        for (int i = 0; i < values.length; i++){
            try {
                int endSymbolPosition = testString.indexOf(dividingChar);
                if (endSymbolPosition != -1) {
                    String textDataToBeReturn = "";
                    if (startChar == LoadingMaster.MAIN_DATA_START_CHAR) {
                        textDataToBeReturn = testString.substring(0, endSymbolPosition);
                    }
                    else textDataToBeReturn = testString.substring(testString.indexOf(startChar)+1, endSymbolPosition);
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                else {
                    String textDataToBeReturn = "";
                    if (endChar == LoadingMaster.END_ROW_SYMBOL) textDataToBeReturn = testString.substring(0);
                    else {
                        textDataToBeReturn = testString.substring(0, testString.indexOf(endChar));
                    }
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                // System.out.println("Test string after " + i + " itteration: " + testString);
            }
            catch (Exception e){
                System.out.println("Can not devide. Must be:  " + values.length + " values. TestString: " + testString + "; Exc: " + e);
                for (int j = 0; j< values.length;j++) {
                    System.out.print(values[j] + ",");
                }
                System.out.println("");
            }
        }
        return  values;
    }


    public int [] getValues(char startChar, char dividingChar, char endChar){
        int [] values = new int[valuesNumber(dividingChar, endChar)];
        String testString = "";
        testString+= testString.concat(stringToBeDecoded);
        for (int i = 0; i < values.length; i++){
            try {
                int endSymbolPosition = testString.indexOf(dividingChar);
                if (endSymbolPosition != -1) {
                    String textDataToBeReturn = "";
                    if (startChar == LoadingMaster.MAIN_DATA_START_CHAR) {
                        int indexOfSymbol = testString.indexOf(startChar);
                        //System.out.println("Test string: " + testString);
                        textDataToBeReturn = testString.substring(indexOfSymbol+1, endSymbolPosition);
                        //System.out.println("Start char is a : for the string: " + testString);
                        //textDataToBeReturn = testString.substring(0, endSymbolPosition);
                    }
                    else {
                        //System.out.println("TROUBLE. IT MUST NOT BE! ");
                        //System.out.println("Start char is not a : ");
                        int indexOfSymbol = testString.indexOf(" ");
                        //System.out.println("Test string: " + testString);
                        textDataToBeReturn = testString.substring(indexOfSymbol+1, endSymbolPosition);
                        //textDataToBeReturn = testString.substring(testString.indexOf(startChar)+1, endSymbolPosition);
                    }
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                else {
                    String textDataToBeReturn = "";
                    if (endChar == LoadingMaster.END_ROW_SYMBOL) textDataToBeReturn = testString.substring(0);
                    else {
                        try {
                            textDataToBeReturn = testString.substring(0, testString.indexOf(endChar));
                        }
                        catch (Exception e){
                            System.out.println("This string has no symbol " + endChar);
                            textDataToBeReturn = testString.substring(0, LoadingMaster.END_ROW_SYMBOL);
                        }
                    }
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                //System.out.println("Test string after " + i + " itteration: " + testString);
            }
            catch (Exception e){
                    System.out.println("Can not devide. Must be:  " + values.length + " values. TestString: " + testString + "; Exc: " + e);
                    for (int j = 0; j< values.length;j++) {
                        System.out.print(values[j] + ",");
                    }
                    System.out.println("");
            }
        }
        return  values;
    }

    /*
    public int [] getValues(char startChar, char dividingChar, char endChar){
        int [] values = new int[valuesNumber(dividingChar, endChar)];
        String testString = "";
        testString+= testString.concat(stringToBeDecoded);
        for (int i = 0; i < values.length; i++){
            try {
                int endSymbolPosition = testString.indexOf(dividingChar);
                if (endSymbolPosition != -1) {
                    String textDataToBeReturn = "";
                    if (startChar == LoadingMaster.MAIN_DATA_START_CHAR) {
                        textDataToBeReturn = testString.substring(0, endSymbolPosition);
                    }
                    else textDataToBeReturn = testString.substring(testString.indexOf(startChar)+1, endSymbolPosition);
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                else {
                    String textDataToBeReturn = "";
                    if (endChar == LoadingMaster.END_ROW_SYMBOL) textDataToBeReturn = testString.substring(0);
                    else {
                        try {
                            textDataToBeReturn = testString.substring(0, testString.indexOf(endChar));
                        }
                        catch (Exception e){
                            System.out.println("This string has no symbol " + endChar);
                            textDataToBeReturn = testString.substring(0, LoadingMaster.END_ROW_SYMBOL);
                        }
                    }
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                //System.out.println("Test string after " + i + " itteration: " + testString);
            }
            catch (Exception e){
                    System.out.println("Can not devide. Must be:  " + values.length + " values. TestString: " + testString + "; Exc: " + e);
                    for (int j = 0; j< values.length;j++) {
                        System.out.print(values[j] + ",");
                    }
                    System.out.println("");
            }
        }
        return  values;
    }
    */


    public int [] getGraphicData(char startChar, char devidingChar){
        int [] values = new int[valuesNumber(devidingChar, LoadingMaster.END_ROW_SYMBOL)];
        String testString = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar)+1);
        //testString = testString.concat(stringToBeDecoded);
        for (int i = 0; i < values.length; i++){
            try {
                int endSymbolPosition = testString.indexOf(devidingChar);
                if (endSymbolPosition != -1) {
                    String textDataToBeReturn = testString.substring(0, endSymbolPosition);
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
                else {
                    String textDataToBeReturn = testString.substring(0);
                    testString=testString.substring(endSymbolPosition+1);
                    values[i] = Integer.parseInt(textDataToBeReturn);
                }
            }
            catch (Exception e){
                System.out.println("Can not devide. Length:  " + values.length + " Exc: " + e);
                for (int k = 0; k < values.length; k++){
                    System.out.println(k+": "+values[k]);
                }
            }
        }
        return  values;
    }



    String getPathToAudio(char startChar, char endChar){
        String path = "";
        try{
            path = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar)+1, stringToBeDecoded.indexOf(endChar));
            if (!path.contains(".wav") && !path.contains(".aif") && !path.contains(".aiff")){
                System.out.println("This object has no graphic data. Path set on ");
                path = "";
            }
        }
        catch (Exception e){
            System.out.println("No audio data");
            return null;
        }
        return path;
    }

    String getPathToTexture(char startChar, char endChar){
        String path = "";
        try{
        path = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar)+1, stringToBeDecoded.indexOf(endChar));
            if (!path.contains(".png") && !path.contains(".gif") && !path.contains(".jpg") && !path.contains(".jpeg")){
                System.out.println("This object has no graphic data. Path set on ");
                path = "No_data";
            }
        }
        catch (Exception e){
            System.out.println("No texture data");
            return null;
        }
        return path;
    }

    String getStringFromChar(char startChar){
        String path = "";
        try{
            // waspath = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar)+1, (stringToBeDecoded.length()-1));
            path = stringToBeDecoded.substring(stringToBeDecoded.indexOf(startChar)+1, (stringToBeDecoded.length()-1));
        }
        catch (Exception e){
            System.out.println("Can not get string from: " + stringToBeDecoded + " after symbol: " + startChar + " to " + (stringToBeDecoded.length()-1 )+ " element");
            return null;
        }
        return path;
    }


    int valuesNumber(char devidingChar, char endChar){
        int valuesNumber = 0;
        boolean stopped = false;
        String lengthTest = "";
        lengthTest = lengthTest.concat(stringToBeDecoded);
        if (endChar == LoadingMaster.END_ROW_SYMBOL || !stringToBeDecoded.contains(""+endChar)) {
            while (!stopped) {
                if (lengthTest.indexOf(devidingChar) != -1) {
                    valuesNumber++;
                    actualStartCharNumber = lengthTest.indexOf(devidingChar) + 1;
                    lengthTest = lengthTest.substring(actualStartCharNumber);
                } else stopped = true;
            }
        }
        else{
            lengthTest = lengthTest.concat(stringToBeDecoded);
            //System.out.println("String was " + lengthTest);
            lengthTest=lengthTest.substring(0, lengthTest.indexOf(endChar));
            //System.out.println("String is " + lengthTest);
            while (!stopped) {
                if (lengthTest.indexOf(devidingChar) != -1) {
                    valuesNumber++;
                    actualStartCharNumber = lengthTest.indexOf(devidingChar) + 1;
                    lengthTest = lengthTest.substring(actualStartCharNumber);
                } else stopped = true;
            }
        }
        if (valuesNumber!=0) valuesNumber++;
        actualStartCharNumber = 0;
        return valuesNumber;
    }

    public int[] getValuesFromGraphicString() {
        int [] values = getGraphicData(LoadingMaster.GRAPHIC_NAME_END_CHAR, LoadingMaster.DIVIDER_BETWEEN_GRAPHIC_DATA);
        return values;
    }

    public boolean hasStringChar(char verticesStartChar) {
        boolean contains = false;
        for (int i = 0; i < stringToBeDecoded.length(); i++){
            if (stringToBeDecoded.charAt(i) == verticesStartChar){
                contains = true;
                break;
            }
        }
        return contains;
    }

    public int getId(String name) {
        if (stringToBeDecoded.contains(name)){
            int startChar = stringToBeDecoded.indexOf(name)+name.length()+1;
            int endChar = stringToBeDecoded.indexOf(LoadingMaster.MAIN_DATA_START_CHAR);
            String idString = stringToBeDecoded.substring(startChar, endChar);
            return Integer.parseInt(idString);
        }
        else {
            int startChar = stringToBeDecoded.indexOf(" ")+1;
            int endChar = stringToBeDecoded.indexOf(LoadingMaster.MAIN_DATA_START_CHAR);
            if (startChar  < 0 || endChar < 0){
                System.out.println("In String " + stringToBeDecoded +  " I can not find " + LoadingMaster.MAIN_DATA_START_CHAR);
                return 0;
            }
            else {
                String idString = stringToBeDecoded.substring(startChar, endChar);
                return Integer.parseInt(idString);
            }
        }
    }

    public int getId() {
        int startChar = stringToBeDecoded.indexOf(" ")+1;
        int endChar = stringToBeDecoded.indexOf(LoadingMaster.MAIN_DATA_START_CHAR);
        if (startChar  < 0 || endChar < 0){
            System.out.println("In String " + stringToBeDecoded +  " I can not find " + LoadingMaster.MAIN_DATA_START_CHAR);
            return 0;
        }
        else {
            String idString = stringToBeDecoded.substring(startChar, endChar);
            return Integer.parseInt(idString);
        }
    }

}
