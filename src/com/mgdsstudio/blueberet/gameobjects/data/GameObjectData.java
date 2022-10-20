package com.mgdsstudio.blueberet.gameobjects.data;

import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import org.jbox2d.common.Vec2;

public abstract class GameObjectData {
    protected String className = "";    //Must be overriden
    protected String dataString = "";
    protected int id = 1;

    public abstract void createDataString();

    public String getDataString() {
        return dataString;
    }

    public void setClassName(Class name) {
        this.className = name.getName();
    }

    protected final void createStartData() {
        dataString = className + ' ' + ":";
    }

    protected final void addValueToDataStringWithCommaSeparator(Vec2 center) {
        dataString+=(int)center.x;
        dataString+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
        dataString+=(int)center.y;
        dataString+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
    }

    protected final void addValueToDataStringWithCommaSeparator(float center) {
        dataString+=(int)center;
        dataString+= ExternalRoundDataFileController.DIVIDER_BETWEEN_VALUES;
    }

    /*
    protected final void deleteLastChar(){
        if (dataString != null){
            dataString = dataString.substring(0,dataString.length()-1);
        }
    }*/
}
