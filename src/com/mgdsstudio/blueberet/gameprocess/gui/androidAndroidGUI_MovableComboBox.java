package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidAndroidGUI_MovableComboBox extends androidGUI_Element {
    private static float xVelocity = 0.000f;
    private final static float xMovementTime = 550;
    private static float yVelocity = 0.000f;
    private final static float yMovementTime = 550;

    public final static byte ON_BASE_POSITION = 0;
    public final static byte ON_OPENING_POSITION = 1;
    //public final static byte X_MOVEMENT = 2;
    public final static byte FROM_BASE_TO_OPENING = 7;
    public final static byte FROM_OPENING_TO_BASE = 8;
    public final static byte DROPPING_DOWN = 3;
    public final static byte WRAPPING_UP = 4;
    public final static byte COMPLETE_DROPPED_DOWN = 5;
    public final static byte COMPLETE_WRAPPED_UP = 6;
    private byte movementPositionStatement = ON_BASE_POSITION;
    private byte wrappingStatement = COMPLETE_WRAPPED_UP;
    private Timer additionalWrappingDownTimer;
    private final int MAX_WRAPPING_TIME = 1500;

    //private Vec2 leftUpperCorner;
    private Vec2 onScreenPos;
    private int nullPositionX;
    private int maxShiftedPositionX;
    private int maxShiftedPositionY;
    private String [] subButtonsNames;
    //private int height;
    private static Image unfocusedImage, focusedImage, pressedImage, disabledImage;
    private PGraphics graphic;

    private Image emblem;
    private static boolean isGraphicLoaded = false;
    private  androidAndroidGUI_DropDownElement[] elements;
    //private final String name;

    public androidAndroidGUI_MovableComboBox(Vec2 leftUpperCorner, int height, String name, String [] subButtonsNames, androidGUI_MenuLowLevelTabType[] types){
        super(leftUpperCorner, 0,height, name);
        this.subButtonsNames = subButtonsNames;
        elementWidth = (int)(Program.engine.width/2.38f);
        if (xVelocity == 0 ) xVelocity = elementWidth/(xMovementTime);
        //onScreenPos = new Vec2(leftUpperCorner.x+basicPointPos.x,leftUpperCorner.y+basicPointPos.y );
        nullPositionX = (int)leftUpperCorner.x;
        maxShiftedPositionX = (int)(Program.engine.width-elementWidth+2);
        if (!isGraphicLoaded) {
            try {
                unfocusedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_ComboBox.png"));
                unfocusedImage.getImage().resize(elementWidth, height);
                focusedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_ComboBoxSelected.png"));
                focusedImage.getImage().resize(elementWidth, height);
                pressedImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_ComboBoxPressed.png"));
                pressedImage.getImage().resize(elementWidth, height);
                disabledImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_ComboBoxDisabled.png"));
                disabledImage.getImage().resize(elementWidth, height);
                isGraphicLoaded = true;
            }
            catch (Exception e){
                System.out.println("Images for combo box can not be founded " + e);
            }
        }
        //this.leftUpperCorner = leftUpperCorner;
        elementHeight = height;
        this.name = name;
        calculateTextSize(elementWidth, height/2);
        //System.out.println("Text size: " + textHeight);
        //System.out.println("Height: " + elementHeight + "; width:" + elementWidth);
        textFont = Program.engine.createFont(Program.mainFontName, textHeight, true);
        float RELATIVE_TEXT_SHIFTING = -NORMAL_HEIGHT/7f;
        textPos = new Vec2(height, RELATIVE_TEXT_SHIFTING+height/2);
        //textPos = new Vec2(0, 0);
        if (Program.graphicRenderer == Program.OPENGL_RENDERER) graphic = Program.engine.createGraphics(elementWidth, elementHeight, PApplet.P2D);
        else graphic = Program.engine.createGraphics(elementWidth, elementHeight, PApplet.JAVA2D);
        elements = new androidAndroidGUI_DropDownElement[subButtonsNames.length];
        for (int i = 0; i < elements.length; i++){
            //elements[i] = new GUI_DropDownElement(new Vec2(leftUpperCorner.x,0), (int) (elementWidth - Editor2D.leftUpperCorner.y), elementHeight, subButtonsNames[i]);
            elements[i] = new androidAndroidGUI_DropDownElement(leftUpperCorner, (int) (elementWidth- Editor2D.leftUpperCorner.y), elementHeight, subButtonsNames[i], types[i]);
            elements[i].setStatement(ACTIVE);
        }
        maxShiftedPositionY = (int)(pos.y+elements.length*elementHeight);
        if (yVelocity == 0) yVelocity = elements.length*elementHeight/yMovementTime;
        //System.out.println("MaxShifted : " + maxShiftedPositionY);
    }



    /*
    public GUI_MovableComboBox(Vec2 leftUpperCorner, Vec2 basicPointPos, int height, String name){
        super(leftUpperCorner, 0,height, name);
        elementWidth = 130;
        onScreenPos = new Vec2(leftUpperCorner.x+basicPointPos.x,leftUpperCorner.y+basicPointPos.y );
        nullPositionX = (int)leftUpperCorner.x;
        maxShiftedPositionX = (int)(nullPositionX-elementWidth);

        if (!isGraphicLoaded) {
            try {
                unfocusedImage = new Image("GUI_ComboBox.png");
                unfocusedImage.getImage().resize(elementWidth, height);
                focusedImage = new Image("GUI_ComboBoxSelected.png");
                focusedImage.getImage().resize(elementWidth, height);
                pressedImage = new Image("GUI_ComboBoxPressed.png");
                pressedImage.getImage().resize(elementWidth, height);
                disabledImage = new Image("GUI_ComboBoxDisabled.png");
                disabledImage.getImage().resize(elementWidth, height);
                isGraphicLoaded = true;
            }
            catch (Exception e){
                System.out.println("Images for combo box can not be founded " + e);
            }
        }
        //this.leftUpperCorner = leftUpperCorner;
        elementHeight = height;
        this.name = name;

        calculateTextSize(elementWidth, height/2);
        //System.out.println("Text size: " + textHeight);
        System.out.println("Height: " + elementHeight + "; width:" + elementWidth);
        textFont = Game2D.engine.createFont(Game2D.mainFontName, textHeight, true);
        float RELATIVE_TEXT_SHIFTING = -NORMAL_HEIGHT/7f;
        textPos = new Vec2(leftUpperCorner.x+2*height/7, leftUpperCorner.y+RELATIVE_TEXT_SHIFTING+height/2);
        if (Game2D.graphicRenderer == Game2D.OPENGL_RENDERER) graphic = Game2D.engine.createGraphics(elementWidth, elementHeight, PApplet.P2D);
        else graphic = Game2D.engine.createGraphics(elementWidth, elementHeight, PApplet.JAVA2D);
    }
    */

    /*
    public void setElementWidth(int elementWidth){
        this.elementWidth = elementWidth;
    }
    */

    public void anotherComboBoxWasPressed(){

    }

    /*
    public byte getMovementPositionStatement(){
        return movementPositionStatement;
    }*/

    public boolean isSomeElementPressed(){
        for (byte i = 0; i < elements.length;i++){
            if (elements[i].getStatement() == PRESSED){
                return true;
            }
        }
        return false;
    }

    public androidGUI_MenuLowLevelTabType getPressedElementActionType(){
        for (byte i = 0; i < elements.length;i++){
            if (elements[i].getStatement() == PRESSED){
                return elements[i].getActionType();
            }
        }
        return null;
    }

    public String getPressedElementName(){
        for (byte i = 0; i < elements.length;i++){
            if (elements[i].getStatement() == PRESSED){
                return elements[i].getName();
            }
        }
        return "";
    }

    private void updateElementsPositionX(){
            for (int i = 0; i < elements.length; i++) {
                elements[i].setPosition(new Vec2(pos.x, elements[i].getPos().y));
            }
    }

    public byte getMovementPositionStatement() {
        return movementPositionStatement;
    }

    public void setMovementPositionStatement(byte movementPositionStatement){
        this.movementPositionStatement = movementPositionStatement;
    }

    private void updateMovementAlongX(){
        if (statement == PRESSED || statement == RELEASED || movementPositionStatement == FROM_BASE_TO_OPENING){
            if (pos.x > maxShiftedPositionX){
                pos.x-= xVelocity* Program.deltaTime;
                if (movementPositionStatement == ON_BASE_POSITION) movementPositionStatement = FROM_BASE_TO_OPENING;
            }
            if (pos.x < maxShiftedPositionX){
                pos.x = maxShiftedPositionX;
                if (movementPositionStatement == FROM_BASE_TO_OPENING) movementPositionStatement = ON_OPENING_POSITION;
            }
            else if (pos.x == maxShiftedPositionX && movementPositionStatement != ON_OPENING_POSITION) movementPositionStatement = ON_OPENING_POSITION;
        }
        else if (statement == ACTIVE || movementPositionStatement == FROM_OPENING_TO_BASE){
            if (pos.x < nullPositionX){
                pos.x+= xVelocity* Program.deltaTime;
                if (movementPositionStatement == ON_OPENING_POSITION) movementPositionStatement = FROM_OPENING_TO_BASE;
            }
            if (pos.x > nullPositionX){
                pos.x = nullPositionX;
                if (movementPositionStatement == FROM_OPENING_TO_BASE) movementPositionStatement = ON_BASE_POSITION;
            }
            else if (pos.x == maxShiftedPositionX && movementPositionStatement != ON_BASE_POSITION) movementPositionStatement = ON_BASE_POSITION;
        }
        updateElementsPositionX();
    }

    private void updateListDropping(){
        if (movementPositionStatement == ON_OPENING_POSITION){
            if (statement == PRESSED){
                if (wrappingStatement == COMPLETE_WRAPPED_UP) {
                    wrappingStatement = DROPPING_DOWN;
                    if (additionalWrappingDownTimer == null){
                        additionalWrappingDownTimer = new Timer(MAX_WRAPPING_TIME);
                    }
                    else additionalWrappingDownTimer.setNewTimer(MAX_WRAPPING_TIME);
                    System.out.println("Dropping down timer was started ");
                    releaseStatementForElements();
                }
                else if (wrappingStatement == COMPLETE_DROPPED_DOWN){
                    wrappingStatement = WRAPPING_UP;
                }
                else {
                    wrappingStatement = DROPPING_DOWN;
                    if (additionalWrappingDownTimer != null){
                        System.out.println("Trouble with statement for listDropping. Rest time: " + additionalWrappingDownTimer.getRestTime());
                        if (additionalWrappingDownTimer.isTime()){
                            wrappingStatement = COMPLETE_DROPPED_DOWN;
                            System.out.println("statement  was changed after long waiting");
                        }
                    }
                    releaseStatementForElements();
                }
            }
        }
    }

    private void releaseStatementForElements(){
        for (int i = 0; i < elements.length; i++){
            if (elements[i].getStatement() != HIDDEN && elements[i].getStatement() != BLOCKED) {
                elements[i].setStatement(ACTIVE);
            }
        }
    }

    public void setWrappingStatement(byte wrappingStatement){
        this.wrappingStatement = wrappingStatement;
    }

    public byte getWrappingStatement() {
        return wrappingStatement;
    }

    public void updateElementsPressedStatement(){
        byte pressedElement = -1;
        for (int i = 0; i < elements.length; i++){
            if (elements[i].isPressed(new Vec2(Program.engine.mouseX, Program.engine.mouseY), PApplet.CORNER)){
                if (elements[i].getStatement() != HIDDEN){
                    elements[i].setStatement(PRESSED);/*
                    if (additionalWrappingDownTimer == null){
                        additionalWrappingDownTimer = new Timer(MAX_WRAPPING_TIME);
                    }
                    else additionalWrappingDownTimer.setNewTimer(MAX_WRAPPING_TIME);*/
                    for (int j = 0; j < elements.length; j++){
                        if (elements[j].getStatement() == RELEASED) elements[j].setStatement(ACTIVE);
                    }
                    return;
                }
            }
        }
        if (pressedElement < 0){
            for (int i = 0; i < elements.length; i++){
                if (elements[i].getPrevisiousStatement() == PRESSED){
                    elements[i].setStatement(RELEASED);
                    //elements[i].setStatement(GUI_Element.RELEASED);
                    //System.out.println("It was released");
                }
                /*
                else if (elements[i].getPrevisiousStatement() != GUI_Element.PRESSED && elements[i].getPrevisiousStatement() != GUI_Element.RELEASED){
                    elements[i].setStatement(GUI_Element.ACTIVE);
                }*/
            }
        }
        else {
            for (int i = 0; i < elements.length; i++) {
                if (i != pressedElement) {
                    if (pressedElement >= 0){
                        if (elements[i].getStatement() != HIDDEN) {
                            elements[i].setStatement(ACTIVE);
                            //System.out.println("Reset statement");
                            //elements[i].setWrappingStatement(GUI_MovableComboBox.WRAPPING_UP);
                        }
                    }
                    //else if (elements[i].getStatement() != GUI_Element.RELEASED) elements[i].setStatement(GUI_Element.ACTIVE);
                }
            }
        }


        //for (GUI_MovableComboBox comboBox : comboBoxes) comboBox.update(leftUpperCorner, false);

    }

    private void returnElementsOnWrapedStatement(){
        for (int i = 0; i < elements.length; i++){
            float yPos =  pos.y+(i+1)*elementHeight;
            //maxShiftedPositionY = (int)(pos.y+elements.length*elementHeight);
            elements[i].setPosition(new Vec2(elements[i].getPosition().x, yPos));
        }
        //maxShiftedPositionY = (int)(pos.y+elements.length*elementHeight);
        System.out.println("Pos must be reset");
    }

    private void updateElements(){
        if (wrappingStatement != COMPLETE_WRAPPED_UP){
            if (wrappingStatement == DROPPING_DOWN){
                if (elements[elements.length-1].getPos().y < maxShiftedPositionY) {
                    for (int i = 0; i < elements.length; i++) {
                        float velocity = yVelocity * Program.deltaTime * ((i + 1.000f) / elements.length);
                        elements[i].moveAlongY(velocity);
                    }
                }
                else if (elements[elements.length-1].getPos().y > maxShiftedPositionY){
                     wrappingStatement = COMPLETE_DROPPED_DOWN;
                     returnElementsOnWrapedStatement();
                }
            }
            else if (wrappingStatement == WRAPPING_UP){
                if (elements[elements.length-1].getPos().y > pos.y) {
                    for (int i = 0; i < elements.length; i++) {
                        float velocity = yVelocity * Program.deltaTime * ((i + 1.000f) / elements.length);
                        elements[i].moveAlongY(-velocity);
                    }
                }
                else if (elements[elements.length-1].getPos().y < maxShiftedPositionY){
                    //System.out.println("upper pos");
                    wrappingStatement = COMPLETE_WRAPPED_UP;
                }
            }
        }
        for (int i = 0; i < elements.length; i++) elements[i].update();
    }

    public void update(Vec2 panelLeftUpperCorner, boolean anotherWasPressed) {
        updateMovementAlongX();
        updateListDropping();
        if (wrappingStatement == COMPLETE_DROPPED_DOWN) updateElementsPressedStatement();
        updateElements();
        previousStatement = statement;
    }

    public Vec2 getOnScreenPos() {
        return onScreenPos;
    }

    public androidAndroidGUI_DropDownElement[] getElements(){
        return elements;
    }



    public void draw(PGraphics panel){
        if (statement == PRESSED) panel.image(pressedImage.getImage(), pos.x,pos.y);
        else if (statement == ACTIVE) panel.image(unfocusedImage.getImage(), pos.x,pos.y);
        else if (statement == RELEASED) panel.image(focusedImage.getImage(), pos.x,pos.y);
        else panel.image(disabledImage.getImage(), pos.x,pos.y);
        //panel.image(unfoccusedImage.getImage(), pos.x,pos.y);
        //if (name == null )  System.out.println("Name is null");
        //drawName(panel, PApplet.CORNER);
    }

    private void drawIcon(Image icons, ImageZoneFullData imageZoneData){
        final float NULL_ZONE = elementHeight/7f;
        float iconHeight = (imageZoneData.lowerY-imageZoneData.upperY);
        float iconWidth = (imageZoneData.rightX-imageZoneData.leftX);
        float coef = iconHeight/iconWidth;
        graphic.image(icons.getImage(), NULL_ZONE, NULL_ZONE , (elementHeight-2*NULL_ZONE)/coef, elementHeight-2*NULL_ZONE, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
    }

    private void drawIconOnSingleGraphic(PGraphics singleGraphic, Image icons, ImageZoneFullData imageZoneData){
        final float NULL_ZONE = elementHeight/7f;
        float iconHeight = (imageZoneData.lowerY-imageZoneData.upperY);
        float iconWidth = (imageZoneData.rightX-imageZoneData.leftX);
        float coef = iconHeight/iconWidth;
        singleGraphic.image(icons.getImage(), pos.x+NULL_ZONE, pos.y+NULL_ZONE , (elementHeight-2*NULL_ZONE)/coef, elementHeight-2*NULL_ZONE, imageZoneData.leftX, imageZoneData.upperY, imageZoneData.rightX, imageZoneData.lowerY);
    }

    public void drawNameOnSingleGraphic(PGraphics graphic, int xAlignment){
        if (graphic!=null) {
            graphic.pushStyle();
            graphic.fill(normalTextColor);
            graphic.textSize(textHeight);
            graphic.textAlign(xAlignment, PConstants.CENTER);
            try {
                graphic.text(name, textPos.x+pos.x, textPos.y+pos.y);
            }
            catch (Exception e){

            }
            graphic.popStyle();
        }
    }

    public void drawWithIconsOnSingleGraphic(PGraphics singleGraphic, Image icons, ImageZoneFullData imageZoneData){
        if (wrappingStatement != COMPLETE_WRAPPED_UP){
            for (int i = 0; i < elements.length; i++){
                elements[i].drawOnSingleGraphic(singleGraphic, pos);
            }
        }
        if (statement == PRESSED) singleGraphic.image(pressedImage.getImage(), pos.x, pos.y);
        else {
            if (statement == ACTIVE) singleGraphic.image(unfocusedImage.getImage(), pos.x, pos.y);
            else {
                singleGraphic.image(focusedImage.getImage(), pos.x, pos.y);
            }
        }
        drawIconOnSingleGraphic(singleGraphic, icons, imageZoneData);
        drawNameOnSingleGraphic(singleGraphic, PApplet.CORNER);
    }

    public void drawWithIconsOnSingleGraphic(Image icons, ImageZoneFullData imageZoneData){
        graphic.beginDraw();
        if (wrappingStatement != COMPLETE_WRAPPED_UP){
            for (int i = 0; i < elements.length; i++){
                elements[i].draw(new Vec2(0,0));
            }
        }
        if (statement == PRESSED) graphic.image(pressedImage.getImage(), 0, 0);
        else {
            if (statement == ACTIVE) graphic.image(unfocusedImage.getImage(), 0, 0);
            else {
                graphic.image(focusedImage.getImage(), 0, 0);
            }
        }
        drawIcon(icons, imageZoneData);
        drawName(graphic, PApplet.CORNER);
        graphic.endDraw();
        Program.engine.imageMode(PConstants.CORNER);
        Program.engine.image(graphic, pos.x, pos.y);
        Program.engine.imageMode(PConstants.CENTER);
    }

    public void draw(){
        graphic.beginDraw();
        if (wrappingStatement != COMPLETE_WRAPPED_UP){
            for (int i = 0; i < elements.length; i++){
                elements[i].draw(new Vec2(0,0));
            }
        }
            if (statement == PRESSED) graphic.image(pressedImage.getImage(), 0, 0);
            else {
                if (statement == ACTIVE) graphic.image(unfocusedImage.getImage(), 0, 0);
                else {
                    graphic.image(focusedImage.getImage(), 0, 0);
                }
            }
        graphic.endDraw();
        drawName(graphic, PApplet.CORNER);
        Program.engine.imageMode(PConstants.CORNER);
        Program.engine.image(graphic, pos.x, pos.y);
        Program.engine.imageMode(PConstants.CENTER);
    }


    public boolean isMouseOnSomeDropDownElement() {
        for (byte i = 0; i < elements.length; i++){
            if (elements[i].isMouseOnElement(new Vec2(Program.engine.mouseX, Program.engine.mouseY), PApplet.CORNER)){
                return true;
            }
        }
        return false;
    }
}
