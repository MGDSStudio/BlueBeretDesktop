package com.mgdsstudio.blueberet.gameprocess.gui;



import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;

import org.jbox2d.common.Vec2;
import processing.core.*;

import java.util.ArrayList;

public class androidGUI_Element {
	protected int normalTextColor = Program.engine.color(155,155,0);	//#2C5538		//0xff1040E8
	protected static final int blockedColor = Program.engine.color(80,125);
	protected String userData;
	protected boolean onScrollableField = true;
	protected PFont textFont;
	protected Vec2 pos, textPos;
  	protected int elementWidth, elementHeight;
  	private int userValue;
  	protected String name = "";
  	protected int textHeight = 6;
	public final static byte ACTIVE = 0;
	public final static byte PRESSED = 1;
	public final static byte HIDDEN = 2;
	public final static byte BLOCKED = 3;
	public final static byte RELEASED = 4;

	protected byte statement = ACTIVE;
	protected boolean pressedNow = false;
	protected byte previousStatement = androidGUI_Element.ACTIVE;

	protected boolean graphicLoaded = false;
	// Dimensions
	  final int NORMAL_HEIGHT = (int) (Program.engine.height/50);
	  
	  
	  androidGUI_Element(Vec2 pos, int w, int h, String name){
	    this.pos = pos;
	    elementWidth = w;
	    elementHeight = h;
		this.name = name;
	  }
	  
	  public void move(Vec2 shift){
	    pos.add(shift);
	  }
	  
	  public void setStatement(byte statement){
	    this.statement = statement;
	  }

	public void setText(String newText){
	  	name = newText;
	}

	  public byte getStatement(){
	    return statement;
	  }

		public byte getPrevisiousStatement(){
			return previousStatement;
		}


	public boolean isMouseOnElement(int mousePosX, int mousePosY, int mode) {
		if (mode == PApplet.CENTER) {
			if (mousePosX > (this.pos.x - elementWidth / 2) &&
					mousePosX < (this.pos.x + elementWidth / 2) &&
					mousePosY > (this.pos.y - elementHeight / 2) &&
					mousePosY < (this.pos.y + elementHeight / 2)) {

				return true;
				//}
			}
		}
		else if (mode == PApplet.CORNER) {
			if (mousePosX > (this.pos.x) && mousePosX < (this.pos.x + elementWidth) && mousePosY > (this.pos.y) && mousePosY < (this.pos.y + elementHeight)) {

				return true;

			} else return false;
		}
		return false;
	}

	public boolean isMouseOnElement(Vec2 mousePos, int mode) {
		if (mode == PApplet.CENTER) {
			if (mousePos.x > (this.pos.x - elementWidth / 2) &&
					mousePos.x < (this.pos.x + elementWidth / 2) &&
					mousePos.y > (this.pos.y - elementHeight / 2) &&
					mousePos.y < (this.pos.y + elementHeight / 2)) {

				return true;
				//}
			}
		}
		else if (mode == PApplet.CORNER) {
				if (mousePos.x > (this.pos.x) && mousePos.x < (this.pos.x + elementWidth) && mousePos.y > (this.pos.y) && mousePos.y < (this.pos.y + elementHeight)) {

						return true;

				} else return false;
			}
		return false;
	}

	public boolean isPressed(int mousePosX, int mousePosY, int mode){
		if (statement != BLOCKED && statement != HIDDEN){
			if (mode == PApplet.CENTER) {
				if (mousePosX > (this.pos.x - elementWidth / 2) &&
						mousePosX < (this.pos.x + elementWidth / 2) &&
						mousePosY > (this.pos.y - elementHeight / 2) &&
						mousePosY < (this.pos.y + elementHeight / 2)) {
					if (Program.engine.mousePressed) {
						return true;
					} else return false;
				} else return false;
			}
			else if (mode == PApplet.CORNER){
				//System.out.println("Mouse: " + mousePos + "; Element: " + pos + " to " + new Vec2(pos.x+elementWidth, pos.y+elementHeight));
				if (mousePosX > (this.pos.x) &&
						mousePosX < (this.pos.x + elementWidth) &&
						mousePosY > (this.pos.y) &&
						mousePosY < (this.pos.y + elementHeight)) {
					//System.out.println("Mouse: " + mousePos + "; Element: " + pos);
					if (Program.engine.mousePressed) {
						return true;
					} else return false;
				} else return false;
			}
		}
		return false;
	}

	  public boolean isPressed(Vec2 mousePos, int mode){
	    if (statement != BLOCKED && statement != HIDDEN){
	    	if (mode == PApplet.CENTER) {
				if (mousePos.x > (this.pos.x - elementWidth / 2) &&
						mousePos.x < (this.pos.x + elementWidth / 2) &&
						mousePos.y > (this.pos.y - elementHeight / 2) &&
						mousePos.y < (this.pos.y + elementHeight / 2)) {
					if (Program.engine.mousePressed) {
						return true;
					} else return false;
				} else return false;
			}
	    	else if (mode == PApplet.CORNER){
				//System.out.println("Mouse: " + mousePos + "; Element: " + pos + " to " + new Vec2(pos.x+elementWidth, pos.y+elementHeight));
				if (mousePos.x > (this.pos.x) &&
						mousePos.x < (this.pos.x + elementWidth) &&
						mousePos.y > (this.pos.y) &&
						mousePos.y < (this.pos.y + elementHeight)) {
					//System.out.println("Mouse: " + mousePos + "; Element: " + pos);
					if (Program.engine.mousePressed) {
						return true;
					} else return false;
				} else return false;
			}
	    }
	    return false;
	  }

	  public void setPosition(Vec2 pos){
	  	this.pos = pos;
	  	if (textPos != null) {
			textPos.x = pos.x;
			textPos.y = pos.y;
		}

	  }

	public boolean isReleased(Vec2 mousePos, Vec2 pMousePos, int mode){
		if (statement != BLOCKED && statement != HIDDEN){
			if (mode == PApplet.CENTER) {
				if (mousePos.x > (this.pos.x - elementWidth / 2) &&
						mousePos.x < (this.pos.x + elementWidth / 2) &&
						mousePos.y > (this.pos.y - elementHeight / 2) &&
						mousePos.y < (this.pos.y + elementHeight / 2)) {
					if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
						if (isMouseOnElement(pMousePos, mode)){
							return true;
						}
						else return false;
					} else return false;
				} else return false;
			}
			else if (mode == PApplet.CORNER){
				//System.out.println("Mouse: " + mousePos + "; Element: " + pos + " to " + new Vec2(pos.x+elementWidth, pos.y+elementHeight));
				if (mousePos.x > (this.pos.x) &&
						mousePos.x < (this.pos.x + elementWidth) &&
						mousePos.y > (this.pos.y) &&
						mousePos.y < (this.pos.y + elementHeight)) {
					//System.out.println("Mouse: " + mousePos + "; Element: " + pos);
					if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
						if (isMouseOnElement(pMousePos, mode)) {
							return true;
						}
						else return false;
					} else return false;
				} else return false;
			}
		}
		return false;
	}

	protected void calculateTextSize(String textToBeShown, float maxWidth, float maxHeight){
		Program.engine.textSize(textHeight);
		float actualWidth = Program.engine.textWidth(textToBeShown);
		if (maxHeight > 0 && maxHeight > 0) {
			while (actualWidth < (9 * maxWidth / 11) && textHeight <= (10 * maxHeight / 11)) {
				textHeight++;
				Program.engine.textSize(textHeight);
				actualWidth = Program.engine.textWidth(textToBeShown);
			}
		}
		else if (maxHeight > 0 && maxWidth <= 0){
			while (textHeight <= (8 * maxHeight / 11)) {
				textHeight++;
				Program.engine.textSize(textHeight);
			}
		}
		if (textHeight>elementHeight*0.68f){
			textHeight = (int)(elementHeight*0.68f);
		}
		if (Program.OS == Program.DESKTOP) textFont = Program.engine.createFont(Program.mainFontNameForWindows, textHeight, true);
		else textFont = Program.engine.createFont(Program.mainFontNameForAndroid, textHeight, true);

	}

	protected void calculateTextSize(float maxWidth, float maxHeight){

		Program.engine.textSize(textHeight);
		textFont = Program.engine.createFont(Program.mainFontName, textHeight, true);
		//textFont = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder()+Program.mainFont);
		float actualWidth = maxWidth;
		try {
			actualWidth = Program.engine.textWidth(name);
		}
		catch (Exception e){
			System.out.println("Can not get text width ");
			System.out.println("for name " + name);
		}
		if (maxHeight > 0 && maxHeight > 0) {
			while (actualWidth < (7 * maxWidth / 11) && textHeight <= (8 * maxHeight / 11)) {
				textHeight++;
				Program.engine.textSize(textHeight);
				actualWidth = Program.engine.textWidth(name);
			}
		}
		else if (maxHeight > 0 && maxWidth <= 0){
			while (textHeight <= (8 * maxHeight / 11)) {
				textHeight++;
				Program.engine.textSize(textHeight);
			}
		}
		if (textHeight>(elementHeight*0.68f)){
			textHeight = (int)(elementHeight*0.68f);
		}
		textFont = Program.engine.createFont(Program.mainFontName, textHeight, true);
		//textFont = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder()+Program.mainFont);

		//Game2D.engine.println("Text size for button: " + textHeight + " element height: " + elementHeight + "; actualWidth: " + actualWidth + "; Element width: " + elementWidth);

	  	/*
		Game2D.engine.textSize(textHeight);
		float actualWidth = Game2D.engine.textWidth(name);
	  	if (maxHeight > 0 && maxHeight > 0) {
			while (actualWidth < (9 * maxWidth / 11) && textHeight <= (9 * maxHeight / 11)) {
				textHeight++;
				Game2D.engine.textSize(textHeight);
				actualWidth = Game2D.engine.textWidth(name);
			}
		}
	  	else if (maxHeight > 0 && maxWidth <= 0){
			while (textHeight <= (9 * maxHeight / 11)) {
				textHeight++;
				Game2D.engine.textSize(textHeight);
			}
		}
		textFont = Game2D.engine.createFont(Game2D.mainFontName, textHeight, true);


	  	*/
	}

	  public String getName(){
	  	return name;
	  }
	  
	  public Vec2 getPosition(){
	   return pos; 
	  }

	  public int getWidth(){
	  	return (int) elementWidth;
	  }

	public int getHeight(){
		return (int) elementHeight;
	}

	public void setWidth(int width){
		elementWidth = width;
	}
	  
	  public void hide(){
	    if (statement != HIDDEN) statement = HIDDEN;
	  }
	  
	  public void doNotHide(){
	    if (statement == HIDDEN) statement = ACTIVE;
	  }


		public int getValue(){
			return statement;
		}
	  
	  public void drawName(PGraphics graphic, int xAlignment){
	  	drawGUIName(graphic,xAlignment,this.textFont);
	  }

	  private void drawGUIName(PGraphics graphic, int xAlignment, PFont font){
		  if (graphic!=null) {
			  //graphic.beginDraw();
			  graphic.pushStyle();
			  graphic.textFont(font);
			  graphic.fill(normalTextColor);
			  graphic.textSize(textHeight);
			  graphic.textAlign(xAlignment, PConstants.CENTER);
			  try {
				  graphic.text(name, textPos.x, textPos.y);
			  }
			  catch (Exception e){

			  }
			  graphic.popStyle();
		  }
		  else {
			  Program.engine.pushStyle();
			  Program.engine.textFont(font);
			  Program.engine.fill(normalTextColor);
			  Program.engine.textSize(textHeight);
			  Program.engine.textAlign(xAlignment, PConstants.CENTER);
			  Program.engine.text(name, textPos.x, textPos.y);
			  Program.engine.popStyle();
		  }
	  }

	public void drawName(PGraphics graphic, int xAlignment, PFont textFont){
		  drawGUIName(graphic, xAlignment,textFont);
		  /*
		if (graphic!=null) {
			graphic.pushStyle();
			graphic.fill(normalTextColor);
			graphic.textSize(textHeight);
			graphic.textAlign(xAlignment, PConstants.CENTER);
			try {
				graphic.text(name, textPos.x, textPos.y);
			}
			catch (Exception e){

			}
			graphic.popStyle();
		}
		else {
			Programm.engine.pushStyle();
			Programm.engine.textFont(textFont);
			Programm.engine.fill(normalTextColor);
			Programm.engine.textSize(textHeight);
			Programm.engine.textAlign(xAlignment, PConstants.CENTER);
			Programm.engine.text(name, textPos.x, textPos.y);
			Programm.engine.popStyle();
		}
		*/
	}


	protected static String getLongestStringFromArray(ArrayList<String> text){
		String longestString = "";
		int longestStringNumber = 0;
		for (int i =1; i < text.size(); i++){
			if (text.get(i).length()>text.get(longestStringNumber).length()){
				longestStringNumber = i;
			}
		}
		longestString = text.get(longestStringNumber);
		System.out.println("Longest string: " + longestString);
		return longestString;
	}

	public void update(Vec2 vec2) {
	  	System.out.println("This update must be overriden");
	}

	public void draw(PGraphics pGraphics) {
		System.out.println("This draw must be overriden");
	}

	public void draw() {
		System.out.println("This draw must be overriden");
	}

    public void setUserValue(int userValue) {
		this.userValue = userValue;
    }

	public void setStartValue(int userValue) {
		this.userValue = userValue;
	}

    public int getUserValue(){
	  	return userValue;
	}

	public boolean isVisibleOnTab(androidGUI_ScrollableTab tab){
	  	Vec2 leftUpper = tab.getLeftUpperCorner();
	  	int width = tab.getWidth();
	  	int visibleHeight = tab.getVisibleHeight();

	  	int relativePositionY = tab.getRelativePositionY();
	  	int maxRelativePositionY = tab.getMaxRelativePositionY();
	  	if ((pos.y> relativePositionY) && (pos.y < (relativePositionY+visibleHeight))) return true;

	  	else return false;
	}



	public void setUserData(String userData){
		this.userData = userData;
	}

	public String getUserData(){
	  	return userData;
	}

	public void setOnScrollableField(boolean flag){
		onScrollableField = flag;
	}

}
