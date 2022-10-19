package com.mgdsstudio.blueberet.onscreenactions;

import processing.core.PVector;

public class OnPinchAction extends OnScreenAction
{
  //private PVector center;
  private float value;

  public OnPinchAction(PVector center, float value)
  {
	this.position = center;
	this.value = value;
	this.type = OnScreenActionType.PINCH;
  }

  public void setCenter(PVector center)
  {
	this.position = center;
  }

  public PVector getCenter()
  {
	return position;
  }

  public void setValue(float value)
  {
	this.value = value;
  }

  public float getValue()
  {
	return value;
  }
  
  
}
