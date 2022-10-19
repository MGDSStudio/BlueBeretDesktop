package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PVector;

public class CrosshairAlphaController {
    private int framesToResetAlphaChanel = Program.NORMAL_FPS/5;
    private final float MAXIMAL_DISTANCE_TO_SHOW_CROSSHAIR = Program.engine.width/2.2f;
    private int alpha = 0;
    private final float MINIMAL_DISTANCE_TO_HOLD_ALPHA = Program.engine.width/45f;
    public final static int MINIMAL_ALPHA = 128;
    private int alphaChangingStep = 8;
    //private int alphaChangingStep = 256/(5);
    private boolean debug = false;

    public CrosshairAlphaController(){

    }

    public void update(PVector prevPos, PVector actualPos){
        if (isCrosshairInHoldingZone(prevPos, actualPos)){
            if (alpha <255){
                alpha+=alphaChangingStep;
            }
            if (alpha > 255) alpha = 255;
        }
        else {
            calculateActualValue(prevPos, actualPos);
        }
    }

    private void calculateActualValue(PVector prevPos, PVector actualPos) {
        float distanceToPrevPos = prevPos.sub(actualPos).mag();
        if (distanceToPrevPos>MINIMAL_DISTANCE_TO_HOLD_ALPHA){  // always
            if (distanceToPrevPos > MAXIMAL_DISTANCE_TO_SHOW_CROSSHAIR){
                //System.out.println("Crosshair is invisible " + distanceToPrevPos);
                alpha = MINIMAL_ALPHA;
                //System.out.println();
            }
            else{
                float deltaDistance = MAXIMAL_DISTANCE_TO_SHOW_CROSSHAIR-MINIMAL_DISTANCE_TO_HOLD_ALPHA;
                float deltaAlpha = 255-MINIMAL_ALPHA;

                //float distance = MAXIMAL_DISTANCE_TO_SHOW_CROSSHAIR-MINIMAL_DISTANCE_TO_HOLD_ALPHA;
                //int reverseValue = (int)(distance*255f/90f);
                //alpha = 255-reverseValue;
                alpha = (int) (distanceToPrevPos*deltaAlpha/deltaDistance+MINIMAL_ALPHA);
                //System.out.println("Crosshair alpha is changed to " + distanceToPrevPos + " reverce: " );
            }
        }
    }

    private boolean isCrosshairInHoldingZone(PVector prevPos, PVector actualPos) {
        PVector vectorToPrevPos = PVector.sub(prevPos, actualPos);
        if (vectorToPrevPos.mag() <= MINIMAL_DISTANCE_TO_HOLD_ALPHA){
            if (debug) System.out.println("In hold zone; Dist " + vectorToPrevPos.mag());
            return true;
        }
        else return false;
    }

    public int getAlpha(){
        return alpha;
    }

}
