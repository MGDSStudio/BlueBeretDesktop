package com.mgdsstudio.blueberet.menusystem.gui;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

class Timer {

    private int timerStartTime;
    private long timerEndedTime;
    private boolean stopped;
    private boolean switchedOn;
    private int timeToBeDelay;
    private final PApplet engine;    

    public Timer(int timeToBeDelay, PApplet engine){
        this.engine = engine;
        timerStartTime = engine.millis();
        timerEndedTime = timerStartTime + timeToBeDelay;
        switchedOn = true;
        this.timeToBeDelay = timeToBeDelay;
    }
    
    public boolean wasSet() {
    	if (timerEndedTime == 0) return false;
    	else return true;
    }
    
    public void reset() {
    	timerEndedTime = 0;
    }

    public void setNewTimer(int timeToBeDelay ){
        timerStartTime = engine.millis();
        if (stopped) stopped = false;
        timerEndedTime = timerStartTime + timeToBeDelay ;
        switchedOn = true;
        this.timeToBeDelay = timeToBeDelay;
    }

    public long getRestTime(){
        if (isTime() == false) return (timerEndedTime- engine.millis());
        else{
            switchedOn = false;
            return 0;
        }
    }

    public float getRelativeRestTime(){
        if (!isTime()) {
            return ((float) timerEndedTime - engine.millis()) / ((float) timeToBeDelay);
        }
        else return 0f;
    }

    public boolean isSwitchedOn(){
        return switchedOn;
    }

    public boolean isSwitchedOff(){
        return !switchedOn;
    }

    public void stop(){
        switchedOn = false;
        stopped = true;
        //timerEndedTime=0; it was
    }

    public boolean isTime(){
        if (engine.millis() >= timerEndedTime && !stopped) {
            switchedOn = false;
            return true;
        }
        else return false;
    }

    public long getInstalledTime(){
        return timeToBeDelay;
    }

    public long getTimeAfterAlarm(){
        return (engine.millis() - timerEndedTime);
    }

    public void end() {
        stopped = true;
    }

    public void bulletTimeModeApplied(boolean flag, float slowRatio){
        //Slow ratio is from 0 to 1f
        System.out.println("This functions seems not work");
        /*
        float relativeValue = getRelativeRestTime();
        System.out.print("Time was: " + relativeValue);
        long restTime = getRestTime();
        if (flag){
            //Must be slower
            int newEndTime = (int) (engine.millis()+(float)restTime/slowRatio);
            timerEndedTime = newEndTime;
        }
        else{
            int newEndTime = (int) (engine.millis()+(float)restTime*slowRatio);
            timerEndedTime = newEndTime;
        }
        float newRelativeValue = getRelativeRestTime();
        System.out.print(" after " + relativeValue);
        boolean ended = false;
        int step = 5;
        if (newRelativeValue>relativeValue){
            while(ended){
                if (getRelativeRestTime()>relativeValue){
                    timeToBeDelay+=step;
                }
                else {
                    ended = false;
                }
            }
        }
        else{
            while(ended){
                if (getRelativeRestTime()<relativeValue){
                    timeToBeDelay-=step;
                }
                else {
                    ended = false;
                }
            }
        }
        System.out.println(" And now: " + getRelativeRestTime());
*/
    }



}
