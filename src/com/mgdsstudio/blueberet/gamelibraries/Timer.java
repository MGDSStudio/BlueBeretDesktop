package com.mgdsstudio.blueberet.gamelibraries;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class Timer{

    private int timerStartTime;
    private long timerEndedTime;
    private boolean stopped;
    private boolean switchedOn;
    private int timeToBeDelay;
    
    public Timer() {
		// TODO Auto-generated constructor stub
	}

    public Timer(int timeToBeDelay){
        timerStartTime = Program.engine.millis();
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
        timerStartTime = Program.engine.millis();
        if (stopped) stopped = false;
        timerEndedTime = timerStartTime + timeToBeDelay ;
        switchedOn = true;
        this.timeToBeDelay = timeToBeDelay;
    }

    public long getRestTime(){
        if (isTime() == false) return (timerEndedTime- Program.engine.millis());
        else{
            switchedOn = false;
            return 0;
        }
    }

    public float getRelativeRestTime(){
        if (!isTime()) {
            return ((float) timerEndedTime - Program.engine.millis()) / ((float) timeToBeDelay);
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
        if (Program.engine.millis() >= timerEndedTime && !stopped) {
            switchedOn = false;
            return true;
        }
        else return false;
    }

    public long getInstalledTime(){
        return timeToBeDelay;
    }

    public long getTimeAfterAlarm(){
        return (Program.engine.millis() - timerEndedTime);
    }

    public void end() {
        stopped = true;
    }


    public void enterBulletTimeMode(float coef){
        //System.out.print("Rest time was: " + getRestTime() + " timer ended time: " + timerEndedTime);
        timerEndedTime= timerEndedTime+(long) (getRestTime()*coef);
        //System.out.println(". Rest time now: " + getRestTime() + " timer ended time: " + timerEndedTime);
        //Slow ratio is from 0 to 1f

        /*
        float relativeValue = getRelativeRestTime();
        System.out.print("Time was: " + relativeValue);
        long restTime = getRestTime();
        if (flag){
            //Must be slower
            int newEndTime = (int) (Program.engine.millis()+(float)restTime/slowRatio);
            timerEndedTime = newEndTime;
        }
        else{
            int newEndTime = (int) (Program.engine.millis()+(float)restTime*slowRatio);
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
