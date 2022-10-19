package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class FrameDimensionChanger implements ISimpleUpdateable {
    public final static int FROM_RIGHT_TO_LEFT = 1;
    public final static int FROM_CENTER = 0;
    static final int NORMAL_TIME_TO_OPEN_FRAME = Program.LONG_PRESSING_TIME;
    private final boolean MAKE_LARGER = true, MAKE_SMALLER = false;

    private Timer timer;
    private int actualOpeningStage = 0;
    private boolean startedToOpen = false;
    private boolean startedToClose = false;
    private boolean fullOpened = false;
    private boolean fullClosed = true;

    private EightPartsFrameImage frame;
    private int stagesNumber = 4;
    private int endWidth, endHeight;
    private int widthChangingStep, heightChangingStep;
    //private int timeToFullWeaponChanging = 800;
    private int timeToNextWeaponChanging = 150;
    private int openingMethod = FROM_RIGHT_TO_LEFT;
    private int basicWidth, basicHeight;
    private boolean openingBlocked;
    private boolean closedAtThisFrame;

    private int startOpeningFrameNumber = -2, startClosingFrameNumber = -1;

    public FrameDimensionChanger(EightPartsFrameImage frame, int timeToFullWeaponChanging, int stagesNumber, int endWidth, int endHeight, int openingMethod, boolean openingBlocked) {
        this.openingBlocked = openingBlocked;
        this.frame = frame;
        this.stagesNumber = stagesNumber;
        this.endWidth = endWidth;
        this.endHeight = endHeight;
        this.openingMethod = openingMethod;
        basicWidth = frame.getWidth();
        basicHeight = frame.getHeight();

        init(stagesNumber, timeToFullWeaponChanging);
    }

    private void init(int stagesNumber, int timeToFullWeaponChanging){
        timeToNextWeaponChanging = timeToFullWeaponChanging/stagesNumber;
        widthChangingStep = (endWidth-frame.getWidth())/stagesNumber;
        heightChangingStep = (endHeight-frame.getHeight())/stagesNumber;
    }

    public void update(){
        if (!openingBlocked) {
            if (startedToOpen) {
                if (actualOpeningStage < (stagesNumber - 0)) {
                    if (timer.isTime()) {
                        timer.setNewTimer(timeToNextWeaponChanging);
                        actualOpeningStage++;
                        changeDimension(MAKE_LARGER);
                    }
                } else {
                    startedToOpen = false;
                    fullOpened = true;
                }
            }
            else if (startedToClose) {
                if (actualOpeningStage > 0) {
                    if (timer.isTime()) {
                        timer.setNewTimer(timeToNextWeaponChanging);
                        actualOpeningStage--;
                        changeDimension(MAKE_SMALLER);
                    }
                } else {
                    startedToClose = false;
                    fullClosed = true;
                    closedAtThisFrame = true;
                    return;
                }
            }
        }
        if (closedAtThisFrame) closedAtThisFrame = false;
        //else System.out.println("It is blocked " + openingBlocked);
    }

    private void changeDimension(boolean direction){
        int oldWidth = frame.getWidth();
        int oldHeight = frame.getHeight();
        int widthStep = widthChangingStep;
        int heightStep = heightChangingStep;
        if (direction == MAKE_SMALLER) widthStep*=-1;
        int newWidth = oldWidth+widthStep;
        int newHeight = oldHeight+heightStep;
        changePosition(oldWidth, oldHeight, newWidth, newHeight);
        frame.setWidth(newWidth);
        frame.setHeight(newHeight);
    }

    private void changePosition(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if (openingMethod == FROM_CENTER){
            //nothing to do
        }
        else if (openingMethod == FROM_RIGHT_TO_LEFT){
            Vec2 leftUpper = frame.getLeftUpperCorner();
            leftUpper.x-=(newWidth-oldWidth);
            leftUpper.y-=(newHeight-oldHeight);
        }
    }

    public void clickOnFrame(GameRound gameRound){
        if (!openingBlocked) {
            if (fullOpened) {
                startToClose();
                gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WEAPON_SELECTED);
            }
            if (fullClosed) {
                startToOpen();
                gameRound.getSoundController().setAndPlayAudio(SoundsInGame.WEAPON_FRAME_OPENING);
            }
        }
    }

    public void startToOpen(){
        if (!startedToOpen && !startedToClose && fullClosed) {
            fullClosed = false;
            startedToOpen = true;
            if (timer == null) {
                timer = new Timer(timeToNextWeaponChanging);
            } else timer.setNewTimer(timeToNextWeaponChanging);
            changeDimension(MAKE_LARGER);
            actualOpeningStage = 1;
            startOpeningFrameNumber = Program.engine.frameCount;
        }
    }

    public void startToClose() {
        if (!startedToOpen && !startedToClose && fullOpened) {
            fullOpened = false;
            startedToClose = true;
            if (timer == null) {
                timer = new Timer(timeToNextWeaponChanging);
            } else timer.setNewTimer(timeToNextWeaponChanging);
            changeDimension(MAKE_SMALLER);
            actualOpeningStage = stagesNumber - 1;
            startClosingFrameNumber = Program.engine.frameCount;
        }
    }

    public boolean isCompleteClosed(){
        return fullClosed;
    }

    public boolean isCompleteOpened(){
        return fullOpened;
    }

    public float getMaxWidth() {
        return endWidth;
    }

    public float getMaxHeight() {
        return endHeight;
    }

    public int getOpeningMethod() {
        return openingMethod;
    }

    public int getBasicWidth() {
        return basicWidth;
    }

    public int getBasicHeight() {
        return basicHeight;
    }

    public void close() {
        startToClose();
    }

    public boolean isOpeningBlocked() {
        return openingBlocked;
    }

    public void setOpeningBlocked(boolean openingBlocked) {
        this.openingBlocked = openingBlocked;
    }

    public boolean isMAKE_LARGER() {
        return MAKE_LARGER;
    }

    public int getStartOpeningFrameNumber() {
        return startOpeningFrameNumber;
    }

    public int getStartClosingFrameNumber() {
        return startClosingFrameNumber;
    }

    public boolean isClosedAtThisFrame(){
        return closedAtThisFrame;
    }

    public void setClosetAtThisFrame(boolean b) {
        closedAtThisFrame = false;
    }
}
