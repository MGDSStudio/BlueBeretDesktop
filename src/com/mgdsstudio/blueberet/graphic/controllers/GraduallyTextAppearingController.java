package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

public class GraduallyTextAppearingController {
    private int timeToStartAppearing;
    private int timeToEndAppearing;
    private float appearingVelocity;    // Chars pro
    private int maxCharNumbersInOneString;
    private String [] actualDrawnText;
    private String [] allText;
    private boolean fullAppeared;
    private int actualStringToBeFilled = 0;
    private boolean addSpaces;

    public GraduallyTextAppearingController(PApplet pApplet, int maxCharNumbersInOneString, int timeToShow, String [] allText, boolean alignmentAlongWidth) {
        this.maxCharNumbersInOneString = maxCharNumbersInOneString;
        if (alignmentAlongWidth) addSpaces = true;
        init(pApplet.millis(), timeToShow, allText);
    }

    public void update(){
        if (!fullAppeared) {
            int appearingCharNumber = getCharNumbers(Program.engine.millis());
                try {
                    if (appearingCharNumber>(allText[actualStringToBeFilled].length())){
                        if (actualStringToBeFilled<(allText.length-1)) {    //was -1
                            testFullLengthAchived(appearingCharNumber);
                            actualDrawnText[actualStringToBeFilled] = allText[actualStringToBeFilled];
                            actualStringToBeFilled++;
                            int deltaTime = timeToEndAppearing - timeToStartAppearing;
                            timeToStartAppearing = Program.engine.millis();
                            timeToEndAppearing = timeToStartAppearing + deltaTime;
                        }
                        else{
                            fullAppeared = true;
                        }
                    }
                    else {
                        if (appearingCharNumber >= allText[actualStringToBeFilled].length()-1) {    // was -1
                            actualDrawnText[actualStringToBeFilled] = allText[actualStringToBeFilled];
                        }
                        else {
                            actualDrawnText[actualStringToBeFilled] = allText[actualStringToBeFilled].substring(0, appearingCharNumber);
                        }

                    }

                }
                catch (Exception e){
                    System.out.println(e);
                }
        }
    }

    private void testFullLengthAchived(int appearingCharNumber) {

    }




    private void init(int startTime,  int timeToShow, String [] allText){
        this.allText = allText;
        if (addSpaces){
            addSpacesToText();
        }
        actualDrawnText = new String[allText.length];
        for (int i = 0; i < actualDrawnText.length; i++){
            actualDrawnText[i] = "";
        }
        timeToStartAppearing = startTime;
        timeToEndAppearing = timeToStartAppearing + timeToShow;
        if (maxCharNumbersInOneString >0) {
            calculateAppearingVelocity(timeToEndAppearing - timeToStartAppearing, maxCharNumbersInOneString);
        }
        System.out.println("Showing started on " + timeToStartAppearing + " and ends on " + timeToEndAppearing);
    }

    private String addSpacesToString(String source){
        String newString = ""+source;
        boolean fullLength = false;
        int actualCharNumber = source.length()-1;
        //System.out.println("String to be added with spaces: " + source);
        if (source.length() > 0) {
            while (!fullLength) {
                for (int j = actualCharNumber; j > 0; j--) {
                    if (j > 0 && !fullLength) {
                        if (source.charAt(j) == ' ') {
                            newString = source.substring(0, j) + ' ' + source.substring(j);
                            source = newString;
                            if (newString.length() >= maxCharNumbersInOneString) {
                                fullLength = true;
                            }
                        }
                    }
                }
                if (newString.length() >= maxCharNumbersInOneString) {
                    fullLength = true;
                }
            }
        }
        newString = source;
        return newString;
    }

    /*
    private String addSpacesToString(String source){
        String newString = ""+source;
        boolean fullLength = false;
        int actualCharNumber = source.length()-1;
        if (source.length() > 0) {
            while (!fullLength) {
                for (int j = actualCharNumber; j > 0; j--) {
                    if (j > 0 && !fullLength) {
                        if (source.charAt(j) == ' ') {
                            newString = source.substring(0, j) + ' ' + source.substring(j);
                            source = newString;
                            if (newString.length() >= maxCharNumbersInOneString) {
                                fullLength = true;
                            }
                        }
                    }
                }
                if (!fullLength) {
                    for (int j = actualCharNumber; j > 0; j--) {
                        if (j > 0 && !fullLength) {
                            if (source.charAt(j) == ' ') {
                                newString = source.substring(0, j) + ' ' + source.substring(j);
                                source = newString;
                                if (newString.length() >= maxCharNumbersInOneString) {
                                    fullLength = true;
                                }
                            }
                        }
                    }
                    fullLength = true;
                }
            }
        }
        newString = source;
       // System.out.println("*String was transformed from: " + source + " with " + source.length() + " chars");
        //System.out.println("***String was transformed to: " + newString + " with " + newString.length() + " chars");
        return newString;
    }
     */

    private void addSpacesToText() {
        int lastMeaningString = getLastMeaningStringNumber(allText);
        for (int i = 0; i < lastMeaningString; i++){
            if (allText[i].length() < maxCharNumbersInOneString){
                boolean moreThanOneWord = isWordsNumberMoreThanOne(allText[i]);
                if (moreThanOneWord){

                    String newString = addSpacesToString(allText[i]);
                    allText[i] = newString;
                }
            }
        }
    }

    private int getLastMeaningStringNumber(String[] allText) {
        int lastString = allText.length;
        for (int i =(allText.length-1); i >= 0; i--){

            if (allText[i].length() == 0){
                System.out.println("String: " + allText[i] + " is clear");
            }
            else {
                System.out.println("String: " + allText[i] + " is last and has number " + i);
                return i;
            }
        }
        return lastString;
    }

    private boolean isWordsNumberMoreThanOne(String string){
        boolean firstWordFounded = false;
        int firstWordCharPos = -1;
        for (int i = 0; i < string.length(); i++){
            if (!firstWordFounded) {
                if (string.charAt(i) != ' '){
                    firstWordFounded = true;
                    firstWordCharPos = i;
                    break;
                }
            }
        }
        if (firstWordFounded) {
            boolean secondSpaceFounded = false;
            int secondSpaceChar = -1;
            if (firstWordCharPos < (string.length() - 2)) {
                for (int i = (firstWordCharPos + 1); i < string.length(); i++) {
                    if (string.charAt(i) == ' ') {
                        secondSpaceFounded = true;
                        secondSpaceChar = i;
                        //System.out.println("The first char is " + string.charAt(i));
                        break;
                    }
                }
            }
            if (secondSpaceFounded){
                boolean secondWordFounded = false;
                for (int i = (secondSpaceChar+1); i < string.length(); i++){
                    if (!secondWordFounded) {
                        if (string.charAt(i) != ' '){
                            //System.out.println("String: " + string + " has more than one word");
                            secondWordFounded = true;
                            return true;
                        }
                    }
                }
            }
        }
        System.out.println("String: " + string + " has one or no words");
        return false;
    }

    private void calculateAppearingVelocity(float deltaTime, int maxCharNumbersInOneString){
        appearingVelocity = maxCharNumbersInOneString/(deltaTime);
    }

    public void setAppearingVelocity(float appearingVelocity){
        this.appearingVelocity = appearingVelocity;
    }

    public float getAppearingVelocity() {
        return appearingVelocity;
    }

    private int getCharNumbers(int actualTime){
        //Right!
        float timeFlownAway = actualTime-timeToStartAppearing;
        int charNumbers = PApplet.ceil(appearingVelocity * timeFlownAway);
        return charNumbers;
    }



    public boolean isFullAppeared() {
        return fullAppeared;
    }

    public String[] getActualDrawnText() {
        return actualDrawnText;
    }

    public void restartWithNewStrings(PApplet pApplet, int timeToShow, String [] newText) {
        allText = newText;
        init(pApplet.millis(), timeToShow, newText);
    }

    public void setFullAppeared() {
        fullAppeared = true;
        for (int i = 0 ; i < allText.length; i++){
            actualDrawnText[i] = allText[i];
        }
    }

    public String [] getFullText(){
        return allText;
    }

    public boolean isSingleString(){
        //System.out.println("StringsL " + allText.length) ;
        if (allText.length == 1) return  true;
        else return false;
    }

    public void setText() {
        actualDrawnText[0] = " ";
        allText[0] = " ";
    }
}
