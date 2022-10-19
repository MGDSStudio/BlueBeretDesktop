package com.mgdsstudio.blueberet.onscreenactions;
import processing.core.*;

public class OnFlickAction extends OnScreenAction
{
  private PVector previous;
  private float velocity;
  
  public OnFlickAction(PVector previous, PVector last, float velocity){
	this.previous = previous;
    this.velocity = velocity;
    this.position = last;
	type = OnScreenActionType.FLICK;
  }

  public void setVelocity(float velocity)
  {
	this.velocity = velocity;
  }

  public float getVelocity()
  {
	return velocity;
  }

  public void setPrevious(PVector previous)
  {
	this.previous = previous;
  }

  public PVector getPrevious()
  {
	return previous;
  }

  public void setLast(PVector last)
  {
	this.position = last;
  }

  public PVector getLast()
  {
	return position;
  }

  public PVector getMovement(){
      PVector movement = new PVector();
      movement = PVector.sub(position, previous);
      return movement;
  }
  
  
}
