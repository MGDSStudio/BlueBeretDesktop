package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.*;

public class androidAndroidGUI_Button extends androidGUI_Element {

	  protected static Image picturePressed, pictureActive, pictureBlocked;
	  protected boolean withFixation;
	  final static public int NORMAL_WIDTH = (int)(Program.engine.width/1.5f);
	  final static public int NORMAL_HEIGHT = (int)(NORMAL_WIDTH/3f);
	  final static private float RELATIVE_TEXT_SHIFTING = -NORMAL_HEIGHT/8f;
	  private String anotherNameToShow;

		final static public int NORMAL_WIDTH_IN_REDACTOR = (int)(Editor2D.zoneWidth/1.5f);
		final static public int NORMAL_HEIGHT_IN_REDACTOR = (int)(NORMAL_WIDTH_IN_REDACTOR/5f);
		final static private float RELATIVE_TEXT_SHIFTING_IN_REDACTOR = -NORMAL_HEIGHT_IN_REDACTOR/7f;

		private final static byte TIME_MOMENT_FOR_NO_PRESSED_BUTTON = -1;
	private long pressedMoment = TIME_MOMENT_FOR_NO_PRESSED_BUTTON;
	  // Dimentions
	  
	  public androidAndroidGUI_Button(Vec2 pos, int w, int h, String name, boolean withFixation){
	    super(pos, w, h, name);

	    textPos = new Vec2(pos.x, pos.y+RELATIVE_TEXT_SHIFTING);
	    init(withFixation);
	    calculateTextSize(elementWidth, elementHeight);

	  }

	  protected void init(boolean withFixation){
		  this.withFixation = withFixation;
		  try{
			  if (!graphicLoaded) {
				  if (pictureActive == null) pictureActive = new Image(Program.getAbsolutePathToAssetsFolder("GUI_Button.png"));
				  if (picturePressed == null) picturePressed = new Image(Program.getAbsolutePathToAssetsFolder("GUI_ButtonPressed.png"));
				  graphicLoaded = true;
			  }
		  }
		  catch(Exception e){
			  Program.engine.println("This picture was maybe already loaded" + e);
		  }
	  }


	  public void draw(){
		  if (statement == ACTIVE){
			  Program.engine.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
		  }
		  else if (statement == PRESSED){
			  Program.engine.image(picturePressed.getImage(), pos.x,pos.y, elementWidth, elementHeight);
		  }
		  drawName(null, PApplet.CENTER);
	  }
	    
	  public void draw(PGraphics graphic){
	    drawButtonBody(graphic);
	    drawName(graphic, PApplet.CENTER);
	  }

		protected void drawButtonBody(PGraphics graphic){
			//graphic.beginDraw();
			graphic.pushMatrix();
			graphic.pushStyle();
			graphic.imageMode(PConstants.CENTER);
			if (statement == ACTIVE){
				graphic.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
			}
			else if (statement == PRESSED){
				graphic.image(picturePressed.getImage(), pos.x,pos.y, elementWidth, elementHeight);
			}
			else graphic.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
			graphic.imageMode(PConstants.CORNER);
			graphic.popStyle();
			graphic.popMatrix();
			//graphic.endDraw();
		}
	  
	  public void update(Vec2 relativePos){
	  	if (statement != BLOCKED) {
			if (relativePos == null) {
				relativePos = new Vec2(0, 0);
			}
			if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
				if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
					statement = PRESSED;
					pressedNow = true;
					if (pressedMoment == TIME_MOMENT_FOR_NO_PRESSED_BUTTON) pressedMoment = Program.engine.millis();
				}
				else if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
					if (pressedNow == true) {
						pressedNow = false;
						statement = RELEASED;
					}
				}
				else if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
					statement = PRESSED;
				}
				else if (!Program.engine.mousePressed && !Editor2D.prevMousePressedStatement && statement == RELEASED)statement = ACTIVE;
			}
			else if (statement == PRESSED || statement == RELEASED) {
				statement = ACTIVE;
				if (pressedNow) pressedNow = false;
			}
		}
	  	if (statement != PRESSED) {
	  		if (pressedMoment != TIME_MOMENT_FOR_NO_PRESSED_BUTTON) {
				pressedMoment = TIME_MOMENT_FOR_NO_PRESSED_BUTTON;
			}
		}
	  }

	  public boolean isLongPressed(int pressingTimeForLongPress){
	  	 if (statement == PRESSED){
	  	 	if ((Program.engine.millis()-pressedMoment)>=pressingTimeForLongPress){
				return true;
			 }
	  	 	else System.out.println("It is not ready: " + (Program.engine.millis()-pressedMoment));
		 }
	  	 return false;
	  }

	  public void resetPressedMoment(){
		  pressedMoment = Program.engine.millis();
	  }

    public void setAnotherNameForDrawing(String anotherName) {
	  	this.anotherNameToShow = name;
	  	name = anotherName;
		calculateTextSize(elementWidth, elementHeight);
    }
    @Override
	public String getName(){
	  	if (anotherNameToShow != null){
	  		return anotherNameToShow;
		}
	  	else return name;
	}
}
