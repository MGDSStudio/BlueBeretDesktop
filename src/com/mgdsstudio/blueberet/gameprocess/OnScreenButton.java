package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;

import processing.core.PConstants;
import processing.core.PGraphics;

public class OnScreenButton {
	public static final boolean MULTITOUCH = true;
    protected ImageZoneSimpleData imageZoneSimpleData;

	//form and dimensions
	final static boolean ROUND = true;
    final static boolean RECTANGULAR = false;
    public final static int normalDimention = (int) (Program.engine.width/11.3f);
    //final static int normalDimention = (int) (Program.engine.height/20.2f);/20./2

    //private PImage mainImage;
    private final Tileset graphicTileset;
    private int x, y, buttonWidth, buttonHeight, buttonRadius,  buttonNominalRadius;
    public final static boolean VISIBLE = true;
    protected boolean pressedStatement, previousPressedStatement, circleForm, rectangleForm, previousVisibilityStatement = !VISIBLE, actualVisibilityStatement = VISIBLE;


    private int startPressingMoment, pressTime;
    private int maxTimeForGrowing = 1500; // millisecond
    //private byte anotherColorByPressingMode = 4;
    private byte buttonFunction;
    private float buttonAngleInRadians = 0;
    //public final static boolean withAdoptedRedrawing = PlayerControl.WITH_ADOPTING_GUI_REDRAWING;
    protected boolean redrawnOnEveryFrame;
    protected boolean drawBackground;
    private boolean flip;
    //private boolean generalGraphicUsed;

    OnScreenButton (Tileset tileset, ImageZoneSimpleData imageZoneSimpleData, int x, int y, boolean circleOrRect, byte buttonFunction, boolean redrawnOnEveryFrame, boolean neverDrawBackground){
        graphicTileset = tileset;
        drawBackground = !neverDrawBackground;
        this.imageZoneSimpleData = imageZoneSimpleData;
        //generalGraphicUsed = true;
        this.x = x;
        this.y = y;
        if (circleOrRect == ROUND) {
            circleForm = true;
            buttonRadius = normalDimention;
            buttonNominalRadius = buttonRadius;
        }
        else {
            rectangleForm = true;
            buttonWidth = normalDimention*2;
            buttonHeight = normalDimention*2;
        }
        this.buttonFunction = buttonFunction;
        this.redrawnOnEveryFrame = redrawnOnEveryFrame;
    }
    
    public byte getFunction(){
        return buttonFunction;
    }

    public boolean getButtonReleaseStatement(){ // auf diese Funktion soll man früher zugreifen, als zur Funktion getPressTime()

        if (getButtonPressedStatement() == false && pressTime > 100) {
            pressTime = 0;
            return true;
        }
        else return false;
    }

    boolean isButtonTouched(){   // minimum one touch place
		if (Program.OS == Program.ANDROID) {
			if (MULTITOUCH) {
			    if (Program.engine.touches.length>0) {
			        for (int i = 0; i < Program.engine.touches.length; i++) {
			            if (Program.engine.dist(x, y, Program.engine.touches[i].x, Program.engine.touches[i].y) < buttonNominalRadius) {
			                return true;
			            }
			        }
			        return false;
			    }
			    return false;
			}
			return false;
		}
	  /* For windows mode */
	  // There is no multitouch
    	else if (Program.OS == Program.DESKTOP) {
    		if (Program.engine.mousePressed == true) {
	            if (Program.engine.dist(x, y, Program.engine.mouseX, Program.engine.mouseY) < buttonNominalRadius) {
	                return true;
	            }            
    		}
    		return false;
	    }
	    return false;
	}



    public boolean getButtonPressedStatement(){
        if (circleForm == true){
            if (isButtonTouched() == true){
                pressedStatement = true;
            }
            else {
                pressedStatement = false;
            }
            return pressedStatement;
        }
        else if (rectangleForm == true){		// To change for rect buttons!
            if (Program.engine.mousePressed == true){
                if (Program.engine.mouseX>(x-(buttonWidth/2)) && Program.engine.mouseX<(x+(buttonWidth/2)) && Program.engine.mouseY>(y-(buttonHeight/2)) && Program.engine.mouseY<(y+(buttonHeight/2))){
                    pressedStatement = true;
                    return true;
                }
                else {
                    pressedStatement = false;
                    return false;
                }
            }
            else {
                pressedStatement = false;
                return false;
            }
        }
        else {
            pressedStatement = false;
            return false;
        }
    }

    /*
    public int getPressTime(){
        if (statement == true && previsiousStatement == false) {
            startPressingMoment = Program.engine.millis();
            previsiousStatement = statement;
            return 0;
        }
        else if (statement == true && previsiousStatement == true){
            pressTime =  Program.engine.millis() - startPressingMoment;
            previsiousStatement = statement;
            return 0;
        }
        else if (statement == false && previsiousStatement == true) {
            previsiousStatement = statement;
            startPressingMoment = 0;
            return pressTime;
        }

        else {
            previsiousStatement = statement;
            return 0;
        }
    }
*/

    /*
    public void updatePictureDimention(){
        if (toGrow == true) {
            if (getButtonPressedStatement() == true){
                if (pressTime < maxTimeForGrowing){
                    pictureScale = 1 + (maxPictureScale - 1)*(float)((pressTime)/(maxTimeForGrowing));
                }
                else pictureScale = maxPictureScale+Game2D.engine.random(-0.3f,0.3f);
            }
            else {
                if (pictureScale > 1) pictureScale-= 0.05f;
                else if (pictureScale < 1) pictureScale = 1;
            }
        }
    }*/

    /*
    public void setTint(){
        if (anotherColorByPressingMode != 0){
            if(anotherColorByPressingMode == 1){
                Program.engine.tint(Program.engine.parseInt((150*pressTime)/(maxTimeForGrowing)),130,130);
            }
            if(anotherColorByPressingMode == 2){
                Program.engine.tint(130, Program.engine.parseInt((150*pressTime)/(maxTimeForGrowing)),130);
            }
            if(anotherColorByPressingMode == 3){
                Program.engine.tint(130,130, Program.engine.parseInt((150*pressTime)/(maxTimeForGrowing)));
            }
            if(anotherColorByPressingMode == 4){
                Program.engine.tint(255,150+ Program.engine.parseInt((105*pressTime)/(maxTimeForGrowing)));
            }
        }
    }*/
/*
    public void resetTint(){
        Program.engine.noTint();
    }
*/
    private void drawCircleButtonWithBackgroundClearingAndSpecificAngle(PGraphics graphic){
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.translate(x, y);

        boolean withSimpleCircle = false;
        if (withSimpleCircle) {
            if (Program.engine.frameCount % 2 == 0) {
                graphic.noStroke();
                graphic.fill(0);
                graphic.ellipse(0, 0, buttonRadius * 2.2f, buttonRadius * 2.2f);
            }
        }
        else{
            graphic.image(graphicTileset.getPicture().getImage(), 0, 0, buttonRadius * 2.1f, buttonRadius * 2.1f, HUD_GraphicData.blackButtonBackground.leftX, HUD_GraphicData.blackButtonBackground.upperY, HUD_GraphicData.blackButtonBackground.rightX, HUD_GraphicData.blackButtonBackground.lowerY);

        }
        if (flip){
            graphic.rotate(buttonAngleInRadians+PConstants.PI);
            graphic.scale(-1,1);
        }
        else {
            graphic.rotate(buttonAngleInRadians);
        }
        if (graphic.imageMode == PConstants.CENTER)  {
            graphic.image(graphicTileset.getPicture().getImage(), 0, 0, buttonRadius * 2, buttonRadius * 2, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        }
        else {
            //graphic.image(graphicTileset.getPicture().getImage(), 0 - buttonRadius, 0 - buttonRadius, buttonRadius * 2, buttonRadius * 2, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            //graphic.image(graphicTileset.getPicture().getImage(), 0 - buttonRadius, 0 - buttonRadius, buttonRadius * 2, buttonRadius * 2, imageZoneSimpleData.leftX, imageZoneSimpleData.upperY, imageZoneSimpleData.rightX, imageZoneSimpleData.lowerY);
        }
        graphic.popStyle();
        graphic.popMatrix();
    }

    protected ImageZoneSimpleData getActualImageData(){
        return imageZoneSimpleData;
    }

    public void drawWithAdoptedRedrawing(PGraphics graphic) {
        if (redrawnOnEveryFrame && actualVisibilityStatement == VISIBLE){
            if (circleForm) {
                drawCircleButtonWithBackgroundClearingAndSpecificAngle(graphic);
            }
        }
        else {
            if (actualVisibilityStatement != previousVisibilityStatement) {
                if (circleForm && (buttonAngleInRadians > 0.05f || buttonAngleInRadians < -0.05f)) {
                    drawWithAngleSettingWithAdoptedRedrawing(graphic);
                }
                else {
                    if (circleForm) {
                        if (actualVisibilityStatement == VISIBLE) {
                            if (graphic.imageMode == PConstants.CENTER)
                                graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonRadius * 2, buttonRadius * 2, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                            else
                                graphic.image(graphicTileset.getPicture().getImage(), x - buttonRadius, y - buttonRadius, buttonRadius * 2, buttonRadius * 2, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);

                        } else {
                            //System.out.println("Button " + buttonFunction + " is hidden as a black rect");
                            if (drawBackground) {
                                if (graphic.imageMode == PConstants.CENTER)
                                    graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonRadius * 2, buttonRadius * 2, HUD_GraphicData.blackButtonBackground.leftX, HUD_GraphicData.blackButtonBackground.upperY, HUD_GraphicData.blackButtonBackground.rightX, HUD_GraphicData.blackButtonBackground.lowerY);
                                else
                                    graphic.image(graphicTileset.getPicture().getImage(), x - buttonRadius, y - buttonRadius, buttonRadius * 2.1f, buttonRadius * 2.1f, HUD_GraphicData.blackButtonBackground.leftX, HUD_GraphicData.blackButtonBackground.upperY, HUD_GraphicData.blackButtonBackground.rightX, HUD_GraphicData.blackButtonBackground.lowerY);
                            }
                        }

                    }
                    else {
                        if (actualVisibilityStatement == VISIBLE) {
                            if (graphic.imageMode == PConstants.CENTER)  graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                            else graphic.image(graphicTileset.getPicture().getImage(), x-buttonWidth/2, y-buttonHeight/2, buttonWidth, buttonHeight, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                        }
                        else {
                            if (drawBackground) {
                                if (graphic.imageMode == PConstants.CENTER)
                                    graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                                else
                                    graphic.image(graphicTileset.getPicture().getImage(), x - buttonWidth / 2, y - buttonHeight / 2, buttonWidth, buttonHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                            }
                        }
                    }
                }
            }
        }



        //if (actualVisibilityStatement != previousVisibilityStatement) System.out.println("Button with code " + buttonFunction + " has changed his statement");
        previousVisibilityStatement = actualVisibilityStatement;
    }

    public void draw(PGraphics graphics){
        if (PlayerControl.withAdoptingGuiRedrawing) drawWithAdoptedRedrawing(graphics);
        else drawWithRegularRedrawing(graphics);
    }

    public void drawWithRegularRedrawing(PGraphics graphic) {
        if (actualVisibilityStatement == VISIBLE){
            if (circleForm && (buttonAngleInRadians > 0.05f || buttonAngleInRadians < -0.05f)) {
                drawWithAngleSettingWithRegularRedrawing(graphic);
            } else {
                if (circleForm) {
                    graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonRadius * 2, buttonRadius * 2, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                }
                else graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
            }
        }
        //if (actualVisibilityStatement != previousVisibilityStatement) System.out.println("Button with code " + buttonFunction + " has changed his statement");
        previousVisibilityStatement = actualVisibilityStatement;
    }

    protected void drawWithAngleSettingWithAdoptedRedrawing(PGraphics graphic) {
        graphic.pushMatrix();
        if (actualVisibilityStatement == VISIBLE) {
            if (circleForm == true) {
                graphic.pushStyle();
                graphic.translate(x, y);
                graphic.rotate(buttonAngleInRadians);
                if (flip) {
                    System.out.println("Draw with flip");
                    graphic.scale(-1, 1);
                }
                graphic.image(graphicTileset.getPicture().getImage(), 0, 0, buttonRadius * 2, buttonRadius * 2, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                graphic.popStyle();
            } else
                graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
        }
        else {
            if (drawBackground) {
                if (circleForm == true) {
                    //graphic.pushStyle();
                    //graphic.translate(x, y);
                    graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonRadius * 2.1f, buttonRadius * 2.1f, HUD_GraphicData.blackButtonBackground.leftX, HUD_GraphicData.blackButtonBackground.upperY, HUD_GraphicData.blackButtonBackground.rightX, HUD_GraphicData.blackButtonBackground.lowerY);

                    //graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonRadius * 2.1f, buttonRadius * 2.1f, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
                    //graphic.popStyle();
                } else
                    graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
            }
        }
        graphic.popMatrix();
    }

    private void drawWithAngleSettingWithRegularRedrawing(PGraphics graphic) {
        graphic.pushMatrix();
        if (actualVisibilityStatement == true) {
            if (circleForm == true) {
                graphic.pushStyle();
                graphic.translate(x, y);
                graphic.rotate(buttonAngleInRadians);
                if (flip) {
                    System.out.println("Draw with flip");
                    graphic.scale(-1, 1);
                }
                graphic.image(graphicTileset.getPicture().getImage(), 0, 0, buttonRadius*2, buttonRadius*2, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
                graphic.popStyle();
            }
            else graphic.image(graphicTileset.getPicture().getImage(), x, y, buttonWidth, buttonHeight, getActualImageData().leftX, getActualImageData().upperY, getActualImageData().rightX, getActualImageData().lowerY);
        }
        graphic.popMatrix();
    }



    public void hide(){
        actualVisibilityStatement = false;
    }

    public void doNotHide(){
        actualVisibilityStatement = VISIBLE;
    }

    boolean isVisible(){
        return actualVisibilityStatement;
    }

    public void setAngleInDegrees(int angle) {
        buttonAngleInRadians = Program.engine.radians(angle);
    }

    public void setFlip(boolean flag) {
        //System.out.println("Flip was set " + flag);
        this.flip = flag;
    }
}
