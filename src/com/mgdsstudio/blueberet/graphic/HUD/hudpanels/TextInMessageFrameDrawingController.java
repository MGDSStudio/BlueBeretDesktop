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

public class TextInMessageFrameDrawingController extends AbstractTextInFrameDrawingController{
    private String endMessageString = "";
    private int stringsAlongY;
    private boolean lastTextPortion;
    private Timer blinkTimer;
    private final int blinkTime = 350;
    private boolean actualEndMessageStringDrawn = true;
    private Timer timerToShowMessage;
    private int timeToShowMessage;
    private boolean ended;

    private int screensNumber = 1;

    private String[][] messagesText;
    private int textOnScreenNumber;
    private int actualDrawnScreenNumber = 0;
    private boolean lastWordBlinkingStarted;
    private int maxCharactersNumber;
    private boolean firstScreenWordBlinkStarted;
    private PhotoDrawingController photoDrawingController;

    public TextInMessageFrameDrawingController(SMS sms, EightPartsFrameImage frameForText, int stringsAlongY, int freeAreaWidth, PFont textFont, PGraphics pGraphics, PhotoDrawingController photoDrawingController) {
        textOnScreenNumber = stringsAlongY-1;
        this.photoDrawingController = photoDrawingController;
        this.textFont = textFont;
        if (sms == null) initClearMessage();
        else {
            this.sourceString = sms.getText();
            endMessageString = sms.getEndMessageText();
            timeToShowMessage = sms.getTimeToClose();
        }
        this.textArea = new Rectangular(frameForText.getLeftUpperCorner().x + frameForText.getWidth() / 2, frameForText.getLeftUpperCorner().y + frameForText.getHeight() / 2, frameForText.getWidth()-3*freeAreaWidth, frameForText.getHeight()-2*freeAreaWidth);
        this.stringsAlongY = stringsAlongY;
        textStep = (int) (textArea.getHeight()/(stringsAlongY));
        System.out.println("Zone the text to be drawn: " + textArea.getCenter() + "; WxH: " + textArea.getWidth() + " x " + textArea.getHeight() + "; original: " + frameForText.getWidth() + "x" + frameForText.getHeight());
        float singleCharacterWidth = (textFont.width('i')*textFont.getSize());
        maxCharactersNumber = calculateMaxCharactersNumber(pGraphics, textArea.getWidth());
        init(singleCharacterWidth, maxCharactersNumber);
        graduallyTextAppearingController = new GraduallyTextAppearingController(Program.engine, maxCharactersNumber, 1200, messagesText[0], true);
        for (int i = 0; i < messagesText[actualDrawnScreenNumber].length; i++){
            System.out.println("Array of strings : " + i + " : " + messagesText[actualDrawnScreenNumber][i]);
        }


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
            //int maxCharactersNumber = PApplet.floor(textArea.getWidth()/singleCharacterWidth);
        }
        System.out.println("Characters along x = " + charNumber);
        return charNumber;
    }

    private void initClearMessage(){
        this.sourceString = SMSController.FORWARD_TEXT;
        endMessageString = SMSController.CLOSE;
        System.out.println("Message is null");
        timeToShowMessage = 3000;
    }

    private void init(float singleCharacterWidth, int maxCharactersNumberForSingleString) {
        textToBeDrawn = new ArrayList<>(stringsAlongY);
        if (sourceString.length() > maxCharactersNumberForSingleString){
            textToBeDrawn = breakStringToSingleStrings(sourceString, maxCharactersNumberForSingleString);
            breakStringsToSingleScreens();
        }
        else{
            textToBeDrawn.add(sourceString);
            screensNumber = 1;
            lastTextPortion = true;
            messagesText = new String[1][1];
            messagesText[0][0] = sourceString;
        }
        //actualDrawnText = new String[textOnScreenNumber];
        System.out.println("Symbol font size: " + singleCharacterWidth + "" + "; Max characters: " + maxCharactersNumberForSingleString);
        blinkTimer = new Timer(blinkTime);
        //afterClickTimer = new Timer(AFTER_CLICK_TIME);
        System.out.println("This message has " + screensNumber + " screens and " + stringsAlongY + " strings on screen at time");


    }

    private void breakStringsToSingleScreens() {
        screensNumber = PApplet.ceil((textToBeDrawn.size()*1f)/(stringsAlongY-1f));
        System.out.println("We have: " + textToBeDrawn.size() + " strings for " + (stringsAlongY-1)  + " strings and it is " + PApplet.ceil(textToBeDrawn.size()/(stringsAlongY-1)));
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
                        //messagesText[j][i] = textToBeDrawn.get(absNumeration);
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

    private ArrayList<String> breakStringToSingleStrings(String sourceString, int maxCharactersNumber ) {
        String[] words = sourceString.split(" ");
        String actualString = words[0];
        System.out.println("Try to break string in single strings");
        for (int i = 1; i < words.length; i++){
            int newLength = actualString.length()+words[i].length();
            if (newLength <= maxCharactersNumber){
                actualString+=" ";
                actualString+= words[i];
                //System.out.println("Try: " + i + "; length: " + newLength + "; String: " + actualString);
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
            lastTextPortion = true;
        }
        System.out.println();
        return textToBeDrawn;
    }

    public void update(){
        if (!ended) {
            if (graduallyTextAppearingController.isFullAppeared()){

                if (timerToShowMessage == null) {
                    timerToShowMessage = new Timer(timeToShowMessage);
                    lastWordBlinkingStarted = true;
                    blinkTimer = new Timer(blinkTime);
                    if (!firstScreenWordBlinkStarted) {
                        firstScreenWordBlinkStarted = true;
                        if (photoDrawingController.getPortraitPicture() == PortraitPicture.SMARTPHONE_WITH_UNREAD_MESSAGE){
                            photoDrawingController.setPortraitPicture(PortraitPicture.SMARTPHONE_WITH_READ_MESSAGE);
                        }
                    }
                }
                else{
                    if (timerToShowMessage.isTime()){
                        timerToShowMessage = null;
                        lastWordBlinkingStarted = false;
                        loadNewTextArray();
                    }
                }
            }
        }
    }

    private void loadNewTextArray(){
        String[] newPortion = new String[textOnScreenNumber];
        System.out.println("*** New text portion ***");
        actualDrawnScreenNumber++;
        if (actualDrawnScreenNumber < messagesText.length) {
            for (int i = 0; i < newPortion.length; i++) {
                newPortion[i] = messagesText[actualDrawnScreenNumber][i];
                System.out.println(newPortion[i]);
            }
            System.out.println("-------");
            graduallyTextAppearingController = new GraduallyTextAppearingController(Program.engine, maxCharactersNumber, 1200, newPortion, true);
            if (actualDrawnScreenNumber == (messagesText.length-1)) lastTextPortion = true;
        }
        else {
            ended = true;
            System.out.println("Time to show was ended");
        }
    }

    public void draw(PGraphics graphics){
        if (!ended) {
            graphics.pushStyle();
            graphics.fill(255);
            graphics.textAlign(PConstants.LEFT, PConstants.CENTER);
            drawMessageText(graphics);
            graphics.textAlign(PConstants.RIGHT, PConstants.CENTER);
            drawEndMessageString(graphics);
            graphics.popStyle();
        }
    }



    private void drawEndMessageString(PGraphics graphics){
        if (lastWordBlinkingStarted) {
            if (blinkTimer.isTime()) {
                if (actualEndMessageStringDrawn) actualEndMessageStringDrawn = false;
                else actualEndMessageStringDrawn = true;
                blinkTimer.setNewTimer(blinkTime);
            }
            if (actualEndMessageStringDrawn) {
                graphics.pushStyle();
                if (Program.OS == Program.ANDROID) graphics.textAlign(PApplet.RIGHT, PApplet.CENTER);
                else graphics.textAlign(39, PApplet.CENTER);
                graphics.text(getEndMessageString() , textArea.getRightLowerX(), textArea.getLeftUpperY() + (stringsAlongY - 1) * textStep + textFont.getSize() / 2);

                //graphics.text(getEndMessageString(), textArea.getRightLowerX() - getDistanceFromRightSideToLastString(graphics), textArea.getLeftUpperY() + (stringsAlongY - 1) * textStep + textFont.getSize() / 2);
                graphics.popStyle();
            }
        }
    }

    private int getDistanceFromRightSideToLastString(PGraphics graphics) {
        float symbolsWidth = graphics.textWidth(getEndMessageString());
        int textWidth = PApplet.ceil( symbolsWidth );
        return textWidth;
    }


    private String getEndMessageString(){
        if (!lastTextPortion){
            return SMSController.FORWARD_TEXT;
        }
        else return endMessageString;
    }


    public boolean isTimeEnds() {
        return ended;
    }

    public void frameWasClicked() {
        if (actualDrawnScreenNumber == textOnScreenNumber){
            System.out.println("The last screen");
            if (lastWordBlinkingStarted){
                ended = true;
                timerToShowMessage.end();
                timerToShowMessage = null;
                lastWordBlinkingStarted = false;
                System.out.println("Message ends by click");
            }
            else {
                lastWordBlinkingStarted = true;
                graduallyTextAppearingController.setFullAppeared();
            }
        }
        else{
            if (lastWordBlinkingStarted) {
                timerToShowMessage.end();
                timerToShowMessage = null;
                lastWordBlinkingStarted = false;
                loadNewTextArray();
            }
            else if (!graduallyTextAppearingController.isFullAppeared()){
                graduallyTextAppearingController.setFullAppeared();
            }
        }
    }

    public boolean isEnded() {
        return ended;
    }
}
