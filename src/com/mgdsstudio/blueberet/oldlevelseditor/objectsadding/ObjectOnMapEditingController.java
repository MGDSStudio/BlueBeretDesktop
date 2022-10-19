package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public abstract class ObjectOnMapEditingController {
    final static public boolean ACTIVE = true;
    final static public boolean ENDED = false;
    protected boolean statement = ACTIVE;
    protected boolean newObjectCanBeAdded = false;
    protected Timer timer;
    protected AddingCross addingCross;
    protected boolean allignedWithGrid = true;
    protected final boolean DRAW_ON_OBJECTS_FRAME = true;

    public void update(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){

    }

    /*
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        System.out.println("this function must be overriden");
    }*/

    public abstract void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess);

    public void switchOffTimer(){
        if (timer != null){
            if (timer.getRestTime()!=0){
                timer.isSwitchedOff();
                timer = null;
                //System.out.println("Timer was switched off 3");
            }
        }
    }

    public boolean canBeNewObjectAdded(){
        return newObjectCanBeAdded;
    }


    public void setAllignedWithGrid(boolean allignedWithGrid) {
        this.allignedWithGrid = allignedWithGrid;
    }
}
