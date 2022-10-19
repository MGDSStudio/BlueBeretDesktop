package com.mgdsstudio.blueberet.mainpackage;

public class UpdatingThreadRateController {
    //final int normalFrameTime;
    int previousStart;
    //private int [] values = n;
    private long prevFrameStartTime;
    private int updatingFrequency;

    public UpdatingThreadRateController(){

    }

    public UpdatingThreadRateController(int graphicFPS, int processorUpdatingRate){
        //normalFrameTime = 1000/processorUpdatingRate;
        if (processorUpdatingRate < graphicFPS){

        }
    }

    public void newUpdatingStarted(){
        previousStart = Program.engine.millis();
    }

    public void update(){
        updatingFrequency = (int) (1000/(Program.engine.millis()-prevFrameStartTime));
        prevFrameStartTime = Program.engine.millis();
    }

    public int getActualRate(){
        return updatingFrequency;
    }
}
