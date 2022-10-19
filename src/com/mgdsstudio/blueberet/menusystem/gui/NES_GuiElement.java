package com.mgdsstudio.blueberet.menusystem.gui;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
//import com.mgdsstudio.blueberet.gamelibraries.Timer;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

public abstract class NES_GuiElement {
    private Object userData;
    private float effectiveHeightCoef = 1.5f;
    public final static int PRESSED = 1, RELEASED = 2, ACTIVE = 0, BLOCKED = 4, HIDDEN = 3;
    //, LONG_PRESSED =5
    private boolean longPressing;
    public final static int NORMAL_HEIGHT = Program.engine.width/26;
    private final static int LONG_PRESSING_TIME = 1500;
    private Timer longPressTimer;
    protected int ACTIVE_COLOR_RED = 255, ACTIVE_COLOR_GREEN = 255, ACTIVE_COLOR_BLUE = 255, BLOCKED_COLOR = 100;
    protected int colorRed = ACTIVE_COLOR_RED;
    protected int colorGreen = ACTIVE_COLOR_GREEN;
    protected int colorBlue = ACTIVE_COLOR_BLUE;
    protected static Image graphicFile;

    protected static PFont font;
    protected static int textHeight = NORMAL_HEIGHT;
    protected int textWidth;

    protected int x, y;
    protected int leftX, upperY;
    protected int width, height = NORMAL_HEIGHT;
    protected String name;
    protected int actualStatement;
    protected int prevStatement;
    private boolean fontInitialized;
    private static boolean withCoutureRect = false;
    private String anotherTextToBeDrawnAsName;
    protected boolean hidden;
    private boolean visible = true;

    private boolean drawDigitWithAnotherColor;
    //private final PApplet engine;

    NES_GuiElement(int centerX, int centerY, int width, int height, String name, PGraphics graphics){
        //engine = graphics.parent;
        this.x = centerX;
        this.y = centerY;
        this.leftX = (int) (x-width/2f);
        this.upperY = (int) (y-height/2f);
        if (width>0) {
            this.width = width;
        }
        else setWidthByFontWidth(graphics);
        this.height = height;
        if (name != null) this.name = name;
        else this.name = "No name";
        initGraphic();

       // if (Program.debug) withCoutureRect = true;
    }

    protected void setWidthByFontWidth(PGraphics graphics) {
        if (font!=null){
            width = (int) graphics.textWidth(name);
        }

    }

    protected void initGraphic(){
        if (graphicFile == null) {
            graphicFile = GameMenusController.sourceFile;
            System.out.println("Graphic was loaded: " + !(graphicFile == null) + "Path: " + GameMenusController.sourceFile.getPath());
        }
    }

    protected final void initFont(PGraphics graphics){
        if (font == null){
            if (Program.USE_MAIN_FONT_IN_MENU){
                font = Program.engine.loadFont(Program.getRelativePathToAssetsFolder()+Program.mainFont);
                textHeight = NORMAL_HEIGHT;
            }
            else {
                font = Program.engine.loadFont(Program.getRelativePathToAssetsFolder()+Program.secondaryFont);
                textHeight = (int)((float)NORMAL_HEIGHT*Program.FONTS_DIMENSION_RELATIONSHIP);
            }
            graphics.textFont = font;
            graphics.textSize(textHeight);
            if (width<=0){
                width = (int) graphics.textWidth(name);
            }
            System.out.println("Font height was set on " + textHeight);
        }
        if (name != null) textWidth = (int) graphics.textWidth(name);
        fontInitialized = true;
    }

    protected void drawName(PGraphics graphic){

            drawName(graphic, PConstants.LEFT);
        /*}
        else drawAnotherName(graphic, PConstants.LEFT);*/
    }


    protected void drawName(PGraphics graphic, int xAlignment){
        graphic.pushStyle();
        if (graphic.textFont != font) graphic.textFont(font);
        graphic.textAlign(xAlignment, PConstants.CENTER);
        if (!drawDigitWithAnotherColor) {
            if (actualStatement == BLOCKED) {
                graphic.tint(colorRed, colorGreen, colorBlue);
            }
            else {
                //if (this.getClass() == NES_TextLabel.class) System.out.println("Text drawn " + getTextTOBeDrawn() + " with color " + colorRed + ", " + colorGreen + ", " + colorBlue);
                graphic.fill(colorRed, colorGreen, colorBlue);
            }
            graphic.textSize(textHeight);
            if (xAlignment == PConstants.LEFT) {
                graphic.text(getTextTOBeDrawn(), leftX, y);
            } else {
                graphic.text(getTextTOBeDrawn(), x, y);
            }
        }
        else {
            if (actualStatement == BLOCKED) {
                graphic.tint(colorRed, colorGreen, colorBlue);
            }
            else {
                //if (this.getClass() == NES_TextLabel.class) System.out.println("Text drawn " + getTextTOBeDrawn() + " with color " + colorRed + ", " + colorGreen + ", " + colorBlue);
                graphic.fill(colorRed, colorGreen, colorBlue);
            }
            graphic.textSize(textHeight);
            if (xAlignment == PConstants.LEFT) {
                graphic.text(getTextTOBeDrawn(), leftX, y);
            } else {
                graphic.text(getTextTOBeDrawn(), x, y);
            }
        }
        graphic.popStyle();
    }

    private String getTextTOBeDrawn(){
        if (anotherTextToBeDrawnAsName != null){
            return anotherTextToBeDrawnAsName;
        }
        else if (name != null) return  name;
        else {
            return "No name to be drawn";
        }
    }

    protected void drawDebugRect(PGraphics graphic){
        graphic.pushStyle();
        graphic.rectMode(PConstants.CORNER);
        graphic.noFill();
        graphic.strokeWeight(graphic.width/150);
        graphic.stroke(255);
        graphic.rect(leftX,upperY, width, textHeight*effectiveHeightCoef);
        graphic.popStyle();
    }

    public void update(int mouseX, int mouseY){
        if (!hidden) {
            if (prevStatement != actualStatement) prevStatement = actualStatement;
            if (actualStatement != BLOCKED && actualStatement != HIDDEN) {
                if (GameMechanics.isPointInRect(mouseX, mouseY, leftX, upperY, width, textHeight * effectiveHeightCoef)) {
                    if (Program.engine.mousePressed) {
                        if (actualStatement != PRESSED) actualStatement = PRESSED;
                    } else if (actualStatement == PRESSED) {
                        actualStatement = RELEASED;
                    } else if (actualStatement == RELEASED) {
                        actualStatement = ACTIVE;
                    }
                } else {
                    actualStatement = ACTIVE;
                }
            }
            updateLongPressingTimer();
            updateFunction();
        }
    }

    protected void updateLongPressingTimer(){
        if (prevStatement == ACTIVE && actualStatement == PRESSED){
            if (longPressTimer == null){
                longPressTimer = new Timer(LONG_PRESSING_TIME, Program.engine);
            }
            else longPressTimer.setNewTimer(LONG_PRESSING_TIME);
        }
        if (actualStatement == PRESSED){
            if (longPressTimer.isTime()){
                if (longPressing != true){
                    longPressing = true;
                }
            }
            else if (longPressing != false) longPressing = false;
        }
        else if (longPressing != false) longPressing = false;
    }


    public void draw(PGraphics graphics) {
        if (!hidden) {
            if (!fontInitialized) initFont(graphics);
            if (withCoutureRect) drawDebugRect(graphics);
        }
    }


    protected abstract void updateFunction();

    public int getActualStatement() {
        return actualStatement;
    }

    public boolean wasStatementChanged(){
        if (actualStatement != prevStatement) return true;
        else return false;

    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }


    public int getUpperY() {
        return upperY;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        int delta = this.leftX - leftX;
        this.leftX = leftX;
        x-=delta;
    }

    public void setUpperY(int upperY) {
        int delta = this.upperY - upperY;
        this.upperY = upperY;
        y-=delta;
    }


    public int getTextWidth() {
        return textWidth;
    }

    public void setWidth(int width) {
        int delta = this.width-width;
        this.width = width;
        leftX+=delta/2;
        x=leftX+width/2;
    }

    public boolean isLongPressing() {
        return longPressing;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public void block(boolean flag){
        if (flag == true) {
            actualStatement = BLOCKED;
        }
        else {
            actualStatement = ACTIVE;
        }
    }

    public void setAnotherTextToBeDrawnAsName(String anotherTextToBeDrawnAsName) {
        this.anotherTextToBeDrawnAsName = anotherTextToBeDrawnAsName;
    }

    public void setActualStatement(int actualStatement) {
        this.actualStatement = actualStatement;
    }

    public void setCenterX(int x){
        int deltaX = this.x-x;
        this.x = x;
        this.leftX = leftX-deltaX;
        //x = graphics.width/2;
    }

    public void alignAlongY(PGraphics graphics) {
        if (anotherTextToBeDrawnAsName != null){

            float coef = (float)anotherTextToBeDrawnAsName.length()/(float)name.length();
            int shifting = (int) ((float)width*coef/2f);
            if (coef>1f){
                shifting*=(-1f) ;
            }
            System.out.println("Gui was shifted to "+ shifting + " and coef: " + coef + "; Name was: " + name + " and now: " + anotherTextToBeDrawnAsName);
            leftX+=shifting;
            leftX = (graphics.width/2)-(width/2);
            //setWidth((int)((float)width*coef));
        }
    }

    public void setName(String name) {
        this.name = name;

    }

    public void hide(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public void setColor(ColorWithName colorWithName){
        colorRed = colorWithName.getRed();
        colorGreen = colorWithName.getGreen();
        colorBlue = colorWithName.getBlue();
        System.out.println("New color was set for this gui " + colorRed + ", " + colorGreen + ", " + colorBlue );
    }

    public void setDrawDigitWithAnotherColor(boolean drawDigitWithAnotherColor) {
        this.drawDigitWithAnotherColor = drawDigitWithAnotherColor;
    }
}
