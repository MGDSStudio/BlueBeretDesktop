package com.mgdsstudio.blueberet.onscreenactions;
import processing.core.*;

public abstract class OnScreenAction
{
  protected PVector position;
  protected OnScreenActionType type = OnScreenActionType.NO_ACTION;

  public OnScreenActionType getType()
  {
	return type;
  }
  

  public void setPosition(PVector position)
  {
	this.position = position;
  }

  public PVector getPosition()
  {
	return position;
  }}
