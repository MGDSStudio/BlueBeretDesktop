package com.mgdsstudio.blueberet.graphic;

import processing.core.PVector;

public interface ISelectable {

    boolean isPointOnElement(float x, float y);

    String getObjectToDisplayName();


}
