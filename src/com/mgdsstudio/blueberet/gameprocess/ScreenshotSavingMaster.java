package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamelibraries.Timer;

class ScreenshotSavingMaster {
    public final static String MAKE_SCREENSHOT = "SCREEN";
    private final GameProcess gameProcess;
    private static int frameNumber = 0;
    private Timer timerToNextSaving;
    //private final GameRound gameRound;

    ScreenshotSavingMaster(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        timerToNextSaving = new Timer(100);
        //this.gameRound = gameRound;
    }



    void consoleInput(String s){
        if (s == MAKE_SCREENSHOT || s.contains(MAKE_SCREENSHOT)){
            if (timerToNextSaving.isTime()) {
                gameProcess.saveScreenshot();
                frameNumber++;
                System.out.println("Screenshot must be saved");
                timerToNextSaving.setNewTimer(300);
            }
        }
        else {
            System.out.println("Another command " + s);
        }
    }

    int getFrameNumber() {
        return frameNumber;
    }
}
