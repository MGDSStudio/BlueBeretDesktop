package com.mgdsstudio.blueberet.graphic.controllers;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import processing.core.PApplet;

import java.util.HashMap;

class SpeakingFaceController {

    private Timer timer;
    private final static int FRAME_TO_CLOSE_EYES_BY_SPEAKING = 9;
    private final static int TIME_TO_NEXT_EYES_CLOSING_CIRCLE = 2000;
    private final static int TIME_TO_NEXT_EYES_OPENING_CIRCLE = 250;
    private final static int TIME_TO_NEXT_MOUTH_OPENED_CIRCLE = 120;
    private final float randomForkValue = 0.8f;
    private int speakingCicles = 0;

    final static int EYES_OPENED = 0;
    final static int EYES_CLOSED = 1;
    final static int MOUTH_OPENED = 2;
    private int actualImage = EYES_OPENED;

    private int getNextRandomTimeForSpeaking(PApplet engine){
        if (speakingCicles % FRAME_TO_CLOSE_EYES_BY_SPEAKING == 1){
            return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);
        }
        else {
            if (actualImage == EYES_OPENED){
                return (int) engine.random(TIME_TO_NEXT_MOUTH_OPENED_CIRCLE-TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue, TIME_TO_NEXT_MOUTH_OPENED_CIRCLE+TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue);
            }
            else if (actualImage == MOUTH_OPENED){
                return (int) engine.random(TIME_TO_NEXT_MOUTH_OPENED_CIRCLE-TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue, TIME_TO_NEXT_MOUTH_OPENED_CIRCLE+TIME_TO_NEXT_MOUTH_OPENED_CIRCLE*randomForkValue);
            }
            else {
                return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);

            }
            //return (int) engine.random(TIME_TO_NEXT_EYES_OPENING_CIRCLE-TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue, TIME_TO_NEXT_EYES_OPENING_CIRCLE+TIME_TO_NEXT_EYES_OPENING_CIRCLE*randomForkValue);
        }

    }

    public void update(PApplet engine){
        /*if (faceStatement == WAITING){
            updateWaiting(engine);
        }
        else */
        updateSpeaking(engine);
    }

    private void updateSpeaking(PApplet engine) {
        if (timer == null){
            int time = getNextRandomTimeForSpeaking(engine);
            timer = new Timer(time);
        }
        else {
            if (timer.isTime()){
                speakingCicles++;
                if (speakingCicles % FRAME_TO_CLOSE_EYES_BY_SPEAKING == 1){
                    actualImage = EYES_CLOSED;
                }
                else {
                    if (actualImage == EYES_OPENED) actualImage = MOUTH_OPENED;
                    else if (actualImage == MOUTH_OPENED) actualImage = EYES_OPENED;
                    else actualImage = MOUTH_OPENED;
                }
                int time = getNextRandomTimeForSpeaking(engine);
                timer.setNewTimer(time);
            }

        }

    }

    int getActualImage(){
        return actualImage;
    }
}
