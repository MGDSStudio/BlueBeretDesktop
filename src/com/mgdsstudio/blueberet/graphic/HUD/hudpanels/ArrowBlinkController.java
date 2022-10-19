package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;


class ArrowBlinkController {
    private final ArrayList <SingleCellWithImage> cells;
    //private int actualSelectedCellNumber;
    private final boolean [] selectedOnThisFrameCells;
    private boolean mustBeReset;
    private Timer blinkTimer;
    private int blinkTimes = 4;
    private int actualBlinkCount = 0;

    private final boolean ARROW_VISIBLE = false;
    private boolean actualBlinkStatement = ARROW_VISIBLE;

    private int [] blinkDelays = new int[blinkTimes];
    private int actualBlinkingCell = -1;

    private final int ALL_SHOWN = 0;
    private final  int BLINK_ONLY_ONE = 1;
    private final  int BLINK_NOTHING = 2;
    private int drawingStatement = ALL_SHOWN;

    private Timer allArrowsBlinkTimer;
    private boolean actualAllArowsDrawnStatement = !ARROW_VISIBLE;
    private final int ALL_ARROWS_SHOWING_TIME = 750;
    private final int ALL_ARROWS_HIDING_TIME = 150;
    public ArrowBlinkController(ArrayList<SingleCellWithImage> cells) {
        this.cells = cells;
        selectedOnThisFrameCells = new boolean[cells.size()];
        initBlinkData();
    }

    private void initBlinkData() {
        int subCount = 4;
        for (int i = 0; i < blinkTimes; i++){
            for (int j = 0; j < i; j++){
                subCount++;
            }
        }
        int minimalDelay = Program.LONG_PRESSING_TIME/(subCount*2);
        System.out.println("Minimal delay for blink: " + minimalDelay + " sub count: " + subCount);
        for (int i = (blinkDelays.length-1); i >= 0; i--){
            int countFrom0 = (blinkDelays.length-1)-i;
            blinkDelays[i] = minimalDelay+(minimalDelay*countFrom0);
        }
    }

    public void update(PApplet engine){
        if (mustBeReset){
            for (int i = 0; i < selectedOnThisFrameCells.length; i++){
                selectedOnThisFrameCells[i] = false;
            }
            //alreadyReset = true;
            mustBeReset = false;
        }
        //else if (!alreadyReset) {
        if (!engine.mousePressed) {
            mustBeReset = true;
            if (drawingStatement == BLINK_ONLY_ONE){
                drawingStatement = ALL_SHOWN;
            }
        }
        //}
        //updateSelecting(engine);
        updateArrowBlinkStatement(engine);
    }

    void frameClosing(){
        drawingStatement = ALL_SHOWN;
        actualBlinkStatement = ARROW_VISIBLE;
    }
    private void updateArrowBlinkStatement(PApplet engine) {
        if (drawingStatement == ALL_SHOWN) {
            if (isSomeCellSelected(engine)) {
                int selected = getSelectedCell(engine);
                if (selected >= 0) {
                    drawingStatement = BLINK_ONLY_ONE;
                    actualBlinkingCell = selected;
                    //actualSelectedCellNumber = selected;
                    int blinkTime = blinkDelays[0];
                    if (blinkTimer == null) blinkTimer = new Timer(blinkTime);
                    else blinkTimer.setNewTimer(blinkTime);
                    actualBlinkStatement = !ARROW_VISIBLE;
                    actualBlinkCount = 0;
                }
            }
            else {
                if (allArrowsBlinkTimer == null){
                    allArrowsBlinkTimer = new Timer(ALL_ARROWS_HIDING_TIME);
                    actualAllArowsDrawnStatement = !ARROW_VISIBLE;
                }
                else {
                    if (allArrowsBlinkTimer.isTime()) {
                        if (actualAllArowsDrawnStatement == ARROW_VISIBLE) {
                            allArrowsBlinkTimer = new Timer(ALL_ARROWS_HIDING_TIME);
                            actualAllArowsDrawnStatement = !ARROW_VISIBLE;
                        } else {
                            allArrowsBlinkTimer = new Timer(ALL_ARROWS_SHOWING_TIME);
                            actualAllArowsDrawnStatement = ARROW_VISIBLE;
                        }
                    }
                }
            }
        }
        else if (drawingStatement == BLINK_ONLY_ONE){
            //System.out.println("Count: " + actualBlinkCount + " statement: " + actualBlinkStatement + " actual delay: " + blinkDelays[actualBlinkCount]);
            if (actualBlinkCount >= (blinkTimes)){
                drawingStatement = BLINK_NOTHING;
            }
            else {
                if (blinkTimer.isTime()){
                    if (actualBlinkStatement == ARROW_VISIBLE){
                        actualBlinkCount++;
                        actualBlinkStatement = !ARROW_VISIBLE;
                    }
                    else {
                        actualBlinkStatement = ARROW_VISIBLE;
                    }
                    //System.out.println("Statement changed " + actualBlinkStatement + "; Count: " + actualBlinkCount);
                    if (actualBlinkCount< blinkDelays.length) blinkTimer.setNewTimer(blinkDelays[actualBlinkCount]);
                    else blinkTimer.setNewTimer(blinkDelays[blinkDelays.length-1]);
                }
            }
        }
    }

    void draw(PGraphics graphics){
        if (drawingStatement == ALL_SHOWN) {
            boolean cellsActualDrawn = areCellsActualDrawn();
            if (cellsActualDrawn) {
                for (int i = 0; i < cells.size(); i++) {
                    if (cells.get(i).hasCellInternalCells()) {
                        cells.get(i).drawArrow(graphics);
                    }
                }
            }
        }
        else if (drawingStatement == BLINK_ONLY_ONE) {
            if (actualBlinkingCell >= 0 && cells.get(actualBlinkingCell).hasCellInternalCells()){
                if (isArrowActualDrawn(actualBlinkingCell)){
                    cells.get(actualBlinkingCell).drawArrow(graphics);
                }
            }
        }
    }

    private boolean areCellsActualDrawn() {
        if (allArrowsBlinkTimer == null) return true;
        else {
            if (actualAllArowsDrawnStatement == ARROW_VISIBLE) return true;
            else return false;
        }
    }

    private boolean isArrowActualDrawn(int actualBlinkingCell) {
        if (drawingStatement == BLINK_ONLY_ONE){
            if (actualBlinkingCell == actualBlinkingCell){
                if (actualBlinkStatement == ARROW_VISIBLE) return true;
                else return false;
            }
            else System.out.println("Actual blinks: " + actualBlinkingCell + " but selected " + actualBlinkingCell);
        }
        return false;
    }



    private int getSelectedCell(PApplet engine) {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).isMouseOnCell(engine.mouseX, engine.mouseY)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isSomeCellSelected(PApplet engine) {
        if (engine.mousePressed) {
            for (SingleCellWithImage singleCellWithImage : cells) {
                if (singleCellWithImage.isMouseOnCell(engine.mouseX, engine.mouseY)) {
                    return true;
                }
            }
        }
        return false;
    }





    public boolean canActualSelectingCellBeChanged() {
        if (selectedOnThisFrameCells[actualBlinkingCell] == false) return  true;
        else return false;
    }

    public void cellWasChanged(SingleCellWithImage cell) {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).equals(cell)){
                selectedOnThisFrameCells[actualBlinkingCell] = true;
                //actualBlinkingCell = -1;
                drawingStatement = BLINK_NOTHING;
            }
        }
    }
}
