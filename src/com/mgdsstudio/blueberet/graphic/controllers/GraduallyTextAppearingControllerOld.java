package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

public class GraduallyTextAppearingControllerOld {
    private int timeToStartAppearing;
    private int timeToEndAppearing;
    private float appearingVelocity;    // Chars pro
    private int maxCharNumbersInOneString;

    public GraduallyTextAppearingControllerOld(PApplet pApplet, int maxCharNumbersInOneString, int timeToShow) {
        this.maxCharNumbersInOneString = maxCharNumbersInOneString;
        init(pApplet.millis(), timeToShow);
    }

    private void init(int startTime,  int timeToShow){
        timeToStartAppearing = startTime;
        timeToEndAppearing = timeToStartAppearing + timeToShow;
        if (maxCharNumbersInOneString >0) {
            calculateAppearingVelocity(timeToEndAppearing - timeToStartAppearing, maxCharNumbersInOneString);
        }
        System.out.println("Showing started on " + timeToStartAppearing + " and ends on " + timeToEndAppearing);
    }

    private void calculateAppearingVelocity(float deltaTime, int maxCharNumbersInOneString){
        appearingVelocity = maxCharNumbersInOneString/(deltaTime);
    }

    public String getCharsForActualDrawingFrame(String source, int actualTime){
        int charNumbers = getCharNumbers(actualTime);
        if (charNumbers > 0 && charNumbers <= source.length()) {
            return source.substring(0,charNumbers);
        }
        else return source;
    }

    public void setAllStringsToShow(){
        timeToStartAppearing = Program.engine.millis();
    }

    private int getCharNumbers(int actualTime){
        //Right!
        int timeFlownAway = actualTime-timeToStartAppearing;
        int charNumbers = PApplet.ceil(appearingVelocity * timeFlownAway);
        return charNumbers;
    }

    public void restart(PApplet pApplet, int timeToShow){
        init(pApplet.millis(), timeToShow);
    }
}
