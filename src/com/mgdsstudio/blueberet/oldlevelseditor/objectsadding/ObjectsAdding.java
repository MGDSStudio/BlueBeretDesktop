package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;

public abstract class ObjectsAdding {
    //protected GameObject newGameObject;
    public final static byte START_STATEMENT = 0;
    //protected byte localStatement = START_STATEMENT;
    protected byte endStatement = 1;
    protected boolean completed = false;

    public void setNextStatement(){
        if (Editor2D.localStatement<endStatement) Editor2D.setNextLocalStatement();
        if (Editor2D.localStatement >= endStatement) completed = true;
    }



    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean flag){
        completed = flag;
    }

    public byte getLocalStatement(){
        return Editor2D.localStatement;
    }



}
