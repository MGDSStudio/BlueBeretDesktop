package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.controllers.GraduallyTextAppearingController;
import com.mgdsstudio.blueberet.graphic.controllers.PhotoDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public class TextInSimpleFrameDrawingController extends AbstractTextInFrameDrawingController{
    public final static char CARRIAGE_RETURN_CHAR = '&';
    private int stringsAlongY;

    private Timer timerToShowMessage;
    private int timeToShowMessage;

    private int screensNumber = 1;

    private String[][] messagesText;
    private int textOnScreenNumber;
    private int actualDrawnScreenNumber = 0;
    private boolean lastWordBlinkingStarted;
    private int maxCharactersNumber;
    private boolean firstScreenWordBlinkStarted;
    //private PhotoDrawingController photoDrawingController;

    private boolean actualTextIsReadyToBeChanged;
    private final float singleCharacterWidth;
    private boolean alignmentFromCenter = true;
    private boolean drawOnlyInArea = true;

    public TextInSimpleFrameDrawingController(String sourceString, Rectangular textArea, int stringsAlongY, int timeToShowMessage, PFont textFont, PGraphics pGraphics, boolean alignmentAlongWidth) {
        textOnScreenNumber = stringsAlongY;
        this.stringsAlongY = stringsAlongY;
        this.textFont = textFont;
        this.sourceString = sourceString;
        this.timeToShowMessage = timeToShowMessage;
        this.textArea = textArea;
        textStep = (int) (this.textArea.getHeight()/(stringsAlongY));
        singleCharacterWidth = (textFont.width('i')*textFont.getSize());
        maxCharactersNumber = calculateMaxCharactersNumber(pGraphics, this.textArea.getWidth());
        init(singleCharacterWidth, maxCharactersNumber, alignmentAlongWidth);
    }

    public TextInSimpleFrameDrawingController(String sourceString, Rectangular textArea, int stringsAlongY, int timeToShowMessage, PFont textFont, PGraphics pGraphics, boolean drawOnlyInArea, boolean alignmentAlongWidth) {
        textOnScreenNumber = stringsAlongY;
        this.drawOnlyInArea = drawOnlyInArea;
        this.stringsAlongY = stringsAlongY;
        this.textFont = textFont;
        this.sourceString = sourceString;
        this.timeToShowMessage = timeToShowMessage;
        this.textArea = textArea;
        textStep = (int) (this.textArea.getHeight()/(stringsAlongY));
        singleCharacterWidth = (textFont.width('i')*textFont.getSize());
        maxCharactersNumber = calculateMaxCharactersNumber(pGraphics, this.textArea.getWidth());
        init(singleCharacterWidth, maxCharactersNumber, alignmentAlongWidth);
    }

    private int calculateMaxCharactersNumber(PGraphics graphics, float maxWidth){
        int charNumber = 1;
        if (graphics != null){
            String textToDetermine = "H";
            while(true) {
                if (graphics.textWidth(textToDetermine) >= maxWidth){
                    break;
                }
                charNumber++;
                textToDetermine+='H';
            }
        }
        else{

        }
        System.out.println("Characters along x = " + charNumber);
        return charNumber;
    }


    private void init(float singleCharacterWidth, int maxCharactersNumberForSingleString, boolean alignmentAlongWidth) {

        textToBeDrawn = new ArrayList<>(stringsAlongY);
        if (sourceString.length() > maxCharactersNumberForSingleString){
            if (!drawOnlyInArea) {
                stringsAlongY = 99;
                textOnScreenNumber = stringsAlongY;
            }
            textToBeDrawn = breakStringToSingleStringsWithCarriageReturnSymbols(sourceString, maxCharactersNumberForSingleString);
            breakStringsToSingleScreens();
            if (!drawOnlyInArea) {
                //messagesText = new String[1][1];
                //messagesText[0][0] = sourceString;
            }
            System.out.println("Text string is devided to " + textToBeDrawn.size() + "");

        }
        else{
            textToBeDrawn.add(sourceString);
            screensNumber = 1;
            messagesText = new String[1][1];
            messagesText[0][0] = sourceString;
        }
        System.out.println("Symbol font size: " + singleCharacterWidth + "" + "; Max characters: " + maxCharactersNumberForSingleString);
        System.out.println("This message has " + screensNumber + " screens and " + stringsAlongY + " strings on screen at time");
        graduallyTextAppearingController = new GraduallyTextAppearingController(Program.engine, maxCharactersNumber, timeToShowMessage, messagesText[0], alignmentAlongWidth);
        for (int i = 0; i < messagesText[actualDrawnScreenNumber].length; i++){
            System.out.println("Array of strings : " + i + " : " + messagesText[actualDrawnScreenNumber][i]);
        }
    }

    private void breakStringsToSingleScreens() {
        System.out.println("textToBeDrawn.size() " + textToBeDrawn.size() + " stringsAlongY " + stringsAlongY);
        screensNumber = PApplet.ceil((textToBeDrawn.size())/(stringsAlongY));
        if (screensNumber <= 0) screensNumber = 1;
        messagesText = new String[screensNumber][textOnScreenNumber];
        int absNumeration = 0;
        for (int i = 0; i < screensNumber; i++){
            for (int j = 0; j< textOnScreenNumber; j++){
                try {
                    if (absNumeration < textToBeDrawn.size()) {
                        String singleString = textToBeDrawn.get(absNumeration);
                        if (singleString == null) singleString = "";
                        messagesText[i][j] = singleString;
                    }
                    else {
                        messagesText[i][j] = "";
                    }
                    absNumeration++;
                }
                catch (Exception e){
                    e.printStackTrace();
                    try{
                        messagesText[i][j] = "";
                    }
                    catch (Exception e1){
                        System.out.println("Inside exception");
                    }
                    absNumeration++;
                }
            }
        }
        System.out.println("*** Text on screens; Screens: " + screensNumber + "; Source text length: " + textToBeDrawn.size());
        for (int i = 0; i < screensNumber; i++){
            for (int j = 0; j< textOnScreenNumber; j++){
                System.out.println(" Screen: " + i + " : " + messagesText[i][j]);
            }
        }
        System.out.println("*** Ended; All strings number was: " + textToBeDrawn.size());
    }

    private ArrayList<String> breakStringToSingleStringsWithoutCarriageReturnSymbol(String sourceString, int maxCharactersNumber ) {
        String[] words = sourceString.split(" ");
        String actualString = words[0];
        System.out.println("Try to break string in single strings");
        for (int i = 1; i < words.length; i++){
            int newLength = actualString.length()+words[i].length();
            if (newLength <= maxCharactersNumber){
                actualString+=" ";
                actualString+= words[i];
            }
            else {
                System.out.println("Actual length: " + newLength + "; String: " + actualString);
                textToBeDrawn.add(actualString);
                String nextElementStartString = words[i];
                actualString = nextElementStartString;
            }
            if (i == (words.length-1)) {
                System.out.println("Last string is: " + actualString);
                textToBeDrawn.add(actualString);
            }
        }
        if (textToBeDrawn.size()< stringsAlongY) {
            screensNumber = 1;
        }
        System.out.println();
        return textToBeDrawn;
    }

    private ArrayList<String> breakStringToSingleStringsWithoutCarriageReturnSymbols(String sourceString, int maxCharactersNumber ) {
        String[] words = sourceString.split(" ");
        String actualString = words[0];
        System.out.println("Try to break string in single strings");
        for (int i = 1; i < words.length; i++){
            int newLength = actualString.length()+words[i].length();
            if (newLength <= maxCharactersNumber){
                actualString+=" ";
                actualString+= words[i];
            }
            else {
                System.out.println("Actual length: " + newLength + "; String: " + actualString);
                textToBeDrawn.add(actualString);
                String nextElementStartString = words[i];
                actualString = nextElementStartString;
            }
            if (i == (words.length-1)) {
                System.out.println("Last string is: " + actualString);
                textToBeDrawn.add(actualString);
            }
        }
        if (textToBeDrawn.size()< stringsAlongY) {
            screensNumber = 1;
        }
        System.out.println();
        return textToBeDrawn;
    }



    private ArrayList<String> breakStringToSingleStringsWithCarriageReturnSymbols(String sourceString, int maxCharactersNumber ) {
        String[] words = sourceString.split(" ");
        String actualString = words[0];
        System.out.println("Try to break string in single strings");
        for (int i = 1; i < words.length; i++){
            int newLength = actualString.length()+words[i].length();
            if (words[i].contains(""+CARRIAGE_RETURN_CHAR)){
                //System.out.println("This word " + words[i] + " is a carriage return symbol " + "; String will be " + actualString);
                String actualStringWithoutSpaces =  getStringWithoutManySpaces(actualString);
                textToBeDrawn.add(actualStringWithoutSpaces);
                //String nextElementStartString = words[i];
                actualString = "";
            }
            else {
                if (newLength <= maxCharactersNumber) {
                    if (actualString.length()> 0) actualString += " ";

                    //actualString += " " ;
                    actualString += words[i];
                } else {
                    System.out.println("New length: " + newLength + " will be longer as max length: " + maxCharactersNumber + ". String will be: " + actualString);
                    textToBeDrawn.add(actualString);
                    String nextElementStartString = words[i];
                    actualString = nextElementStartString;
                }
                if (i == (words.length - 1)) {
                    System.out.println("Last string is: " + actualString);
                    textToBeDrawn.add(actualString);
                }
            }
        }
        if (drawOnlyInArea) {
            if (textToBeDrawn.size() < stringsAlongY) {
                screensNumber = 1;
            }
            System.out.println();
        }
        else {
            screensNumber = 1;
            System.out.println();
        }
        System.out.println("Text devided into " + textToBeDrawn.size() + " strings");
        return textToBeDrawn;
    }

    private String getStringWithoutManySpaces(String actualString) {
        //System.out.println("String to be simplified: " + actualString);
        if (actualString.length()>0) {
            String withoutSpaces = "" + actualString;
            if (actualString.charAt(0) == ' ') withoutSpaces = actualString.substring(1);
            if (actualString.charAt(actualString.length() - 1) == ' ')
                withoutSpaces = withoutSpaces.substring(0, withoutSpaces.length() - 2);
            return withoutSpaces;
        }
        return actualString;
    }

    public void update(){
            if (graduallyTextAppearingController.isFullAppeared()){
                if (timerToShowMessage == null) {
                    timerToShowMessage = new Timer(timeToShowMessage);
                    lastWordBlinkingStarted = true;
                    if (!firstScreenWordBlinkStarted) {
                        firstScreenWordBlinkStarted = true;
                    }
                }
                else{
                    if (timerToShowMessage.isTime()){
                        timerToShowMessage = null;
                        lastWordBlinkingStarted = false;
                        actualTextIsReadyToBeChanged = true;
                    }
                }
            }

    }

    public void draw(PGraphics graphics){
            graphics.pushStyle();
            graphics.fill(255);
            graphics.textAlign(PConstants.LEFT, PConstants.CENTER);
            drawMessageText(graphics);
            //drawMessageTextFromCorner(graphics);
            graphics.textAlign(PConstants.RIGHT, PConstants.CENTER);
            graphics.popStyle();

    }

    public void setNewText(String newText, boolean alignmentAlongWidth){
        System.out.println("New string is: " + newText);
        this.sourceString = newText;
        init(singleCharacterWidth, maxCharactersNumber, alignmentAlongWidth);
        actualTextIsReadyToBeChanged = false;
    }





    public boolean isActualTextIsReadyToBeChanged() {
        return actualTextIsReadyToBeChanged;
    }

    public boolean isFullShown() {
        return graduallyTextAppearingController.isFullAppeared();
    }


    public void setStringsAlongY(int i) {
        stringsAlongY = i;
        actualDrawnScreenNumber = 0;
        //messagesText[actualDrawnScreenNumber]
        //actualDrawnScreenNumber = ();
    }
}
