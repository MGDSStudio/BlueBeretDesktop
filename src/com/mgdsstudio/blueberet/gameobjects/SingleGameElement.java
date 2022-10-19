package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;

public abstract class SingleGameElement {        //This class was created to can select and delete elemenets from redactor
    public static String CLASS_NAME = "SingleGameElement";
    private boolean selected = false;
    public static int actualSelectionTintValue = 0;
    private static final boolean TINT_INCREASING = true;
    private static final boolean TINT_DECREASING = false;
    private static boolean tintChangingDirection = TINT_DECREASING;
    private static float tintChangingDirectionTime = 800f;
    private static float tintChangingSpeed = 255.00f/tintChangingDirectionTime;
    protected boolean selectionWasCleared = false;
    protected boolean canBeDeleted = false;
    public static GameRound gameRound;
    protected int uniqueId;

    public static void resetTintValue() {
        actualSelectionTintValue = 0;
    }

    public void clearSelection(){
        selectionWasCleared = true;

        //actualSelectionTintValue = 0;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isSelectionWasCleared() {
        return selectionWasCleared;
    }

    public String getStringData(){
        String data = "" + CLASS_NAME + " " + "1";
        System.out.println("The string data is not to be constructed for this game  and must be overridden in child classes");
        return data;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        /*if (selected == false){
            Game2D.objectsFrame.noTint();
        }*/
    }

    public void drawTint(){
        //System.out.println("Draw tint");
        if (selected) {
            Program.objectsFrame.tint(254, actualSelectionTintValue);
            //System.out.println("Tint:: " + actualSelectionTintValue + "; speed: " + tintChangingSpeed + "; step: " + (tintChangingSpeed * (float) Game2D.deltaTime));
        }
    }

    public static void updateActualSelectionTintValue(){
        if (tintChangingDirection == TINT_INCREASING) {
            actualSelectionTintValue += (tintChangingSpeed * (float) Program.deltaTime);
            if (actualSelectionTintValue > 255) {
                actualSelectionTintValue = 255;
                tintChangingDirection = TINT_DECREASING;
            }
        }
        else {
            actualSelectionTintValue -= (tintChangingSpeed * (float) Program.deltaTime);
            if (actualSelectionTintValue < 0) {
                actualSelectionTintValue = 0;
                tintChangingDirection = TINT_INCREASING;
            }
        }
    }

    public static int getTintForSelectedElement(){
        return actualSelectionTintValue;
    }

    public void resetTint(){
        if (selected){
            Program.objectsFrame.noTint();
        }
    }

    public String getClassName(){
        return CLASS_NAME;
    }

    public String getObjectToDisplayName() {
        return CLASS_NAME;
    }

    public void markAsDeletedApproved() {
        canBeDeleted = true;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    protected void notMoreLoadThisObject(){
        if (uniqueId < (-1) || uniqueId > 1){

        }
        else System.out.println("This object can not be deleted from the history.");
    }

    /*
    public abstract float getWidth();

    public abstract float getHeight();*/
}
