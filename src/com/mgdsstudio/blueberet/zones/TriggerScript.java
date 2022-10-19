package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import org.jbox2d.dynamics.BodyType;

public class TriggerScript extends AbstractTrigger implements IDrawable {


    public TriggerScript(GameRound gameRound, Flag flag, int objectX, int objectY, int action, int activatingCondition, int objectType, int delay, boolean once) {
        super(gameRound, flag, objectX, objectY, action, activatingCondition, objectType, delay, once);
    }

    protected void makeAction(GameRound gameRound) {
        //System.out.println("Trigger activated with action " + action);
        if (action == DELETE_OBJECT_WITH_BODY){
            deleteGameObject(gameRound);
        }
        else if (action == KILL_OBJECT_WITH_BODY){
            gameObjectToBeActivated.kill();
        }
        else if (action == CHANGE_BODY_TYPE_TO_DYNAMIC){
            gameObjectToBeActivated.body.setType(BodyType.DYNAMIC);
            gameObjectToBeActivated.body.setGravityScale(1);

        }
        else if (action == DELETE_GRAPHIC_OBJECT){
            gameRound.deleteGraphicObject(graphic);
        }
        else if (action == STOP_ANIMATION){
            try {
                IndependentOnScreenAnimation independentOnScreenAnimation = (IndependentOnScreenAnimation) graphic;
                independentOnScreenAnimation.pause();
            }
            catch (Exception e){
                System.out.println("Graphic is not an animation");
                e.printStackTrace();
            }
        }
        else if (action == RESUME_ANIMATION){
            try {
                IndependentOnScreenAnimation independentOnScreenAnimation = (IndependentOnScreenAnimation) graphic;
                independentOnScreenAnimation.resume();
            }
            catch (Exception e){
                System.out.println("Graphic is not an animation");
                e.printStackTrace();
            }
        }
        else if (action == RESUME_ANIMATION_IF_ACTIVATED_STOP_IF_DEACTIVATED){
            try {
                IndependentOnScreenAnimation independentOnScreenAnimation = (IndependentOnScreenAnimation) graphic;
                if (actionTypeToBeActivated == SWITCH_ON_ACTION) {
                    //System.out.println("Animation resumed");
                    independentOnScreenAnimation.resume();
                }
                else {
                    //System.out.println("Animation paused");
                    independentOnScreenAnimation.pause();
                }

            }
            catch (Exception e){
                System.out.println("Graphic is not an animation");
                e.printStackTrace();
            }
        }
    }
}
