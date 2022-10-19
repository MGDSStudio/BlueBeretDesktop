package com.mgdsstudio.blueberet.mainpackage;
import com.mgdsstudio.blueberet.onscreenactions.*;
import com.mgdsstudio.blueberet.onscreenactions.OnScreenAction;
import com.mgdsstudio.blueberet.onscreenactions.OnScreenActionType;

public abstract class TouchScreenActionBuffer
{
  private static OnScreenAction action;
  private static OnScreenActionType actionType = OnScreenActionType.NO_ACTION;

  public static OnScreenAction getAction()
  {
	return action;
  }


  public static OnScreenActionType getActionType()
  {
	return actionType;
  }
  
  public static void addNewAction(OnScreenAction newAction){
	action = newAction;
	actionType = action.getType();
  }
  
  public static void clearBuffer(){
	action = null;
	actionType = OnScreenActionType.NO_ACTION;
  }
  
  
}
