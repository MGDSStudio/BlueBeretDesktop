package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.controllers.GraduallyTextAppearingControllerOld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public class TextInFrameDrawingControllerOld {
    private String sourceString;
    private String endMessageString = "";
    private ArrayList <String> textToBeDrawn;
    private Rectangular textArea;
    private int stringsAlongY;
    private int freeAreaWidth;
    private PFont textFont;
    private int textStep;
    private int actualStartStringToDraw;
    private boolean lastTextPortion;

    private Timer blinkTimer;
    private final int blinkTime = 350;
    private boolean actualEndMessageStringDrawn = true;
    private Timer timerToShowMessage;
    private int timeToShowMessage;
    private boolean ended;
    private Timer afterClickTimer;
    private final int AFTER_CLICK_TIME = 500;
    private GraduallyTextAppearingControllerOld graduallyTextAppearingController;
    private int actualApearingStringNumber;
    private boolean appearingCompleteEnds;
    private final int TIME_TO_APPEAR_FULL_STRING = 950;
    private boolean allStringsOnActualFrameAreAppeared;
    private int screensNumber = 1;
    private boolean jumpToNextStringPortion = false;

    public TextInFrameDrawingControllerOld(SMS sms, EightPartsFrameImage frameForText, int stringsAlongY, int freeAreaWidth, PFont textFont, PGraphics pGraphics) {
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
        int maxCharactersNumber = calculateMaxCharactersNumber(pGraphics, textArea.getWidth());
        init(singleCharacterWidth, maxCharactersNumber);
        graduallyTextAppearingController = new GraduallyTextAppearingControllerOld(Program.engine, maxCharactersNumber, 200);
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

    private void init(float singleCharacterWidth, int maxCharactersNumber) {
        textToBeDrawn = new ArrayList<>(stringsAlongY);
        if (sourceString.length() > maxCharactersNumber){
            System.out.println("Text is longer as one string: " + sourceString.length() + " from " + maxCharactersNumber);
            breakStringToSingleStrings(sourceString, textToBeDrawn, maxCharactersNumber);
        }
        else{
            System.out.println("This string is short");
            textToBeDrawn.add(sourceString);
            lastTextPortion = true;
        }
        System.out.println("Symbol font size: " + singleCharacterWidth + "" + "; Max characters: " + maxCharactersNumber);
        blinkTimer = new Timer(blinkTime);
        afterClickTimer = new Timer(AFTER_CLICK_TIME);
    }

    private void breakStringToSingleStrings(String sourceString, ArrayList<String> textToBeDrawn, int maxCharactersNumber ) {
        String[] words = sourceString.split(" ");
        String actualString = words[0];
        System.out.println("Source string: " + sourceString);
        for (int i = 0; i < words.length; i++){
            //System.out.println(words[i]);
        }
        //System.out.println();
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
        System.out.println("Text is ready; Size: " + textToBeDrawn.size() + " consists: ");
        for (int i = 0; i < textToBeDrawn.size(); i++){
            System.out.println("*** - " +textToBeDrawn.get(i));
            if (textToBeDrawn.size() <= (stringsAlongY-1)){
                //lastTextPortion = true;
            }
        }
        if (textToBeDrawn.size()< stringsAlongY) {
            System.out.println("This text for this message is too short and can be placed in single screen");
            lastTextPortion = true;
        }
        System.out.println();
    }

    private void allTextForThisFrameIsShown(){
        if (timerToShowMessage == null) {
            timerToShowMessage = new Timer(timeToShowMessage);
            allStringsOnActualFrameAreAppeared = true;
        }
        else {
            if (timerToShowMessage.isTime()) {
                System.out.println("Timer was set: " + timeToShowMessage);
                timerToShowMessage.setNewTimer(timeToShowMessage);
                allStringsOnActualFrameAreAppeared = true;
            }
            else {
                //allStringsOnActualFrameAreAppeared = false;
            }
        }

    }


    public void update(){
        if (!ended) {
            if (timerToShowMessage != null) {
                if (timerToShowMessage.isTime() && allStringsOnActualFrameAreAppeared) {
                    setNextPortionOfStringsToShow();
                } else if (!allStringsOnActualFrameAreAppeared) {
                    //System.out.println("Not every string is shown");
                }
            }
        }
    }

    public void draw(PGraphics graphics){
        if (!ended) {
            graphics.pushStyle();
            graphics.fill(255);
            graphics.textAlign(PConstants.LEFT, PConstants.CENTER);
            int lastStringNumber = stringsAlongY - 1;
            if ((actualStartStringToDraw + stringsAlongY - 1) > textToBeDrawn.size()) {
                lastStringNumber = textToBeDrawn.size()-actualStartStringToDraw;
            }
            for (int i = (lastStringNumber-1); i >= 0; i--) {
                int absolutStringNumber = i+actualStartStringToDraw;
                String text =  getActualStringToBeDrawn(i, absolutStringNumber);
                //System.out.println("relative " + i + "; Absolute: "  + absolutStringNumber ) ;
                graphics.text(text, textArea.getLeftUpperX(), textArea.getLeftUpperY() + (i) * textStep + textFont.getSize() / 2);
            }
            graphics.textAlign(PConstants.RIGHT, PConstants.CENTER);
            drawEndMessageString(graphics);
            graphics.popStyle();
            System.out.println("actualApearingStringNumber " + actualApearingStringNumber);
        }
    }

    private String getActualStringToBeDrawn(int relativeStringNumber, int absolutStringNumber) {
        String text;
        if (absolutStringNumber == (textToBeDrawn.size()-1) && absolutStringNumber < actualApearingStringNumber){
            allTextForThisFrameIsShown();
        }
        if ((absolutStringNumber < actualApearingStringNumber)){
            text = textToBeDrawn.get(absolutStringNumber);
            if (relativeStringNumber == (stringsAlongY-2)) {
                allTextForThisFrameIsShown();
            }
            return text;
        }
        else if (appearingCompleteEnds){
            text = graduallyTextAppearingController.getCharsForActualDrawingFrame(textToBeDrawn.get(textToBeDrawn.size()-1), Program.engine.millis());
            //text = graduallyTextAppearingController.getCharsForActualDrawingFrame(textToBeDrawn.get(textToBeDrawn.size()-1), Programm.engine.millis());
            if (!allStringsOnActualFrameAreAppeared) {
                allTextForThisFrameIsShown();
            }
            return text;
        }
        else if (absolutStringNumber == actualApearingStringNumber && relativeStringNumber < (stringsAlongY-1)){
            text = graduallyTextAppearingController.getCharsForActualDrawingFrame(textToBeDrawn.get(absolutStringNumber), Program.engine.millis());
            if (text.length() >= (textToBeDrawn.get(absolutStringNumber).length())) {
                graduallyTextAppearingController.restart(Program.engine,TIME_TO_APPEAR_FULL_STRING );

                if (actualApearingStringNumber < (textToBeDrawn.size()-0) ) {
                    if (!jumpToNextStringPortion) {
                        actualApearingStringNumber++;
                        System.out.println("Actual string number is ++ 2" + Program.engine.millis());
                    }

                }

                //if (actualApearingStringNumber < (textToBeDrawn.size()-1)) actualApearingStringNumber++;
                else appearingCompleteEnds = true;
                jumpToNextStringPortion = false;
                //jumpToNextStringPortion = false;
            }
            return text;
        }
        else {
            System.out.println(" For sting: " + absolutStringNumber + " appearingCompleteEnds: " + appearingCompleteEnds);
            System.out.println("This string is not showable " + absolutStringNumber);
            return "";
        }

    }

    private void drawEndMessageString(PGraphics graphics){
        if (allStringsOnActualFrameAreAppeared) {
            if (blinkTimer.isTime()) {
                if (actualEndMessageStringDrawn) actualEndMessageStringDrawn = false;
                else actualEndMessageStringDrawn = true;
                blinkTimer.setNewTimer(blinkTime);
            }
            if (actualEndMessageStringDrawn) {
                graphics.text(getEndMessageString(), textArea.getRightLowerX() - getDistanceFromRightSideToLastString(graphics), textArea.getLeftUpperY() + (stringsAlongY - 1) * textStep + textFont.getSize() / 2);
            }
        }
    }

    private int getDistanceFromRightSideToLastString(PGraphics graphics) {
        float symbolsWidth = graphics.textWidth(getEndMessageString());
        int textWidth = PApplet.ceil(symbolsWidth);
        return textWidth;
    }


    private String getEndMessageString(){
        if (!lastTextPortion){
            return SMSController.FORWARD_TEXT;
        }
        else return endMessageString;
    }

    private void setNextPortionOfStringsToShow(){
        if (lastTextPortion) ended = true;
        if (!ended) {
            actualStartStringToDraw += (stringsAlongY - 1);
            if ((actualStartStringToDraw + (stringsAlongY - 1)) > (textToBeDrawn.size())) {
                System.out.println("This text portion is the last. Size: " + textToBeDrawn.size() + " x " + ((actualStartStringToDraw + (stringsAlongY - 1))));
                lastTextPortion = true;
                ended = true;
            }
            //timerToShowMessage = new Timer(timeToShowMessage);
            afterClickTimer = new Timer(AFTER_CLICK_TIME);
            //graduallyTextAppearingController.setAllStringsToShow();
            graduallyTextAppearingController.restart(Program.engine,TIME_TO_APPEAR_FULL_STRING );
            jumpToNextStringPortion = false;
            if (actualApearingStringNumber < (textToBeDrawn.size()-1)) {
                System.out.println("Actual string number is ++ 1" + Program.engine.millis());
                actualApearingStringNumber++;
                jumpToNextStringPortion = true;
            }
            else appearingCompleteEnds = true;
            //timerToShowMessage = new Timer(timeToShowMessage);
            allTextForThisFrameIsShown();
            allStringsOnActualFrameAreAppeared = false;

        }
    }

    public boolean jumpToNextPortionOfStringsToShow(){
        if (afterClickTimer.isTime()) {
            if (allStringsOnActualFrameAreAppeared) {
                setNextPortionOfStringsToShow();
                //graduallyTextAppearingController.setAllStringsToShow();
            }
            else {
                graduallyTextAppearingController.setAllStringsToShow();
            }
            //graduallyTextAppearingController.setAllStringsToShow();
        }
        return ended;
    }

    public boolean isActualTextLast(){
        if ((actualStartStringToDraw+stringsAlongY-1)>= textToBeDrawn.size()){
            System.out.println("This text is the last portion");
            return true;
        }
        else return false;
    }

    public boolean isTimeEnds() {
        return ended;
    }

    public long getRestTime() {
        if (timerToShowMessage != null) {
            if (timerToShowMessage.getRestTime()<1){
                //ended = true;
            }
            return timerToShowMessage.getRestTime();
        }
        else return -1;
    }

    public void setEnded() {
        //ended = true;
        //lastTextPortion = true;
    }
}
