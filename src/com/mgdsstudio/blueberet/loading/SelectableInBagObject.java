package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.InWorldObjectsGraphicData;

public class SelectableInBagObject {
    private final int type;
    private ImageZoneSimpleData imageZoneSimpleData;
    private int number;

    public SelectableInBagObject(int type, int number) {
        this.type = type;
        this.number = number;
        initGraphicData();
    }

    private void initGraphicData() {
        if (type == AbstractCollectable.LARGE_MEDICAL_KIT) imageZoneSimpleData = InWorldObjectsGraphicData.largeMedicalKitInWorld;
        else if (type == AbstractCollectable.MEDIUM_MEDICAL_KIT) imageZoneSimpleData = InWorldObjectsGraphicData.mediumMedicalKitInWorld;
        else if (type == AbstractCollectable.SMALL_MEDICAL_KIT) imageZoneSimpleData = InWorldObjectsGraphicData.smallMedicalKitInWorld;
        else if (type == AbstractCollectable.SYRINGE) imageZoneSimpleData = InWorldObjectsGraphicData.syringeInWorld;
        else {
            System.out.println("This object has no graphic data");
            imageZoneSimpleData = InWorldObjectsGraphicData.bigCoinCopper;
        }
    }


    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
