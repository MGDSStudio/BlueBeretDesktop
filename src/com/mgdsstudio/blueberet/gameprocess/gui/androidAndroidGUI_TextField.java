package com.mgdsstudio.blueberet.gameprocess.gui;


import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class androidAndroidGUI_TextField extends androidGUI_Element {
    protected static Image pictureActive, picturePressed;//, pictureActive, pictureWithData, pictureBlocked;
    private Timer nextCharacterTimer;
    androidAndroidGUI_Slider slider;
    private final int timeToNextCharacter = 150;
    protected boolean forDigits;
    private String dataFromUser;
    private int minValue, maxValue;
    private boolean withDeleteCross = true;
    private Vec2 deleteCrossPosition;
    final static public int NORMAL_WIDTH = (int)(Program.engine.width/1.5f);
    final static public int NORMAL_HEIGHT = (int)(NORMAL_WIDTH/3f);
    final static private float RELATIVE_TEXT_SHIFTING = 0;

    final static public int NORMAL_WIDTH_IN_REDACTOR = (int)(NORMAL_WIDTH);
    final static public int NORMAL_HEIGHT_IN_REDACTOR = (int)(NORMAL_HEIGHT);
    final static private float RELATIVE_TEXT_SHIFTING_IN_REDACTOR = +NORMAL_HEIGHT_IN_REDACTOR/5f;

    final public static int ERROR_VALUE = -999999;

    private boolean forInteger = true;

    public androidAndroidGUI_TextField(Vec2 pos, int w, int h, String name, boolean forDigits, int minValue, int maxValue){
        super(pos, w, h, name);
        textPos = new Vec2(pos.x, pos.y+RELATIVE_TEXT_SHIFTING);
        dataFromUser = new String();
        this.forDigits = forDigits;
        if (forDigits) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
        init(forDigits);
        calculateTextSize(elementWidth, elementHeight*0.6f);
        if (withDeleteCross){
            deleteCrossPosition = new Vec2(pos.x-(elementWidth/2)+elementWidth*0.939f, pos.y);
        }
    }

    public androidAndroidGUI_TextField(Vec2 pos, int w, int h, String name){
        super(pos, w, h, name);
        textPos = new Vec2(pos.x, pos.y+RELATIVE_TEXT_SHIFTING);
        dataFromUser = new String();
        this.forDigits = false;
        init(forDigits);
        calculateTextSize(elementWidth, elementHeight*0.6f);
        if (withDeleteCross){
            deleteCrossPosition = new Vec2(pos.x-(elementWidth/2)+elementWidth*0.939f, pos.y);
        }
    }

    public void addCoppeledSlider(androidAndroidGUI_Slider slider){
        this.slider = slider;
    }

    private void init(boolean forDigits){

        try{
            if (!graphicLoaded) {
                if (pictureActive == null) pictureActive = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TextField.png"));
                if (picturePressed == null) picturePressed = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TextFieldPressed.png"));
                graphicLoaded = true;
            }
        }
        catch(Exception e){
            Program.engine.println("This picture was maybe already loaded" + e);
        }
    }

    private boolean canBeNextKeyValueRead(){
        if (Program.OS == Program.DESKTOP){
            if (nextCharacterTimer == null) {
                nextCharacterTimer = new Timer(timeToNextCharacter);
                return true;
            }
            else{
                if (nextCharacterTimer.isTime()){
                    nextCharacterTimer.setNewTimer(timeToNextCharacter);
                    return true;
                }
                else return false;
            }
        }
        else return true;
    }


    private boolean isCharDigit(char character){
        if (character == '0' || character == '1' || character == '2' || character == '3' || character == '4' || character == '5' || character == '6' || character == '7' || character == '8' || character == '9') return true;
        else return false;
    }



    private void updateTextWrite(char character, int keyCode){
        if (statement == PRESSED){
            //if ((Program.engine.keyPressed && Program.OS == Program.WINDOWS)||((Program.OS == Program.ANDROID) && Program.actualPressedKeyDigitValue>=0)) {
            if ((Program.engine.keyPressed && Program.OS == Program.DESKTOP) || (Program.OS == Program.ANDROID)) {
                if (Program.OS == Program.ANDROID) {
                    character = Program.actualPressedValue;
                    System.out.println("Char has code " + keyCode + " and character " + Program.actualPressedValue + " and keyCode " + Program.actualPressedKeyCode);
                }
                if (canBeNextKeyValueRead()) {
                    if (Program.engine.key == PApplet.BACKSPACE || Program.actualPressedKeyCode == 67 || PlayerControl.backspacePressed) {
                        if (dataFromUser.length() > 0) {
                            dataFromUser = dataFromUser.substring(0, dataFromUser.length() - 1);
                            int value = getDigitValue();
                            if (slider != null) updateSliderValue(value);
                        }
                    }
                    else {
                        if (!forDigits) {
                            if (!hasCyrrilic(character) && keyCode != 16 && character != '@'){
                                dataFromUser +=character;
                            }
                        }
                        else {
                            if (Program.OS == Program.DESKTOP || (Program.OS == Program.ANDROID && Program.actualPressedKeyDigitValue>=0)) {
                                if (isCharDigit(character)) {
                                    dataFromUser += character;
                                    int value = getDigitValue();
                                    if (value != ERROR_VALUE) {
                                        dataFromUser = "";
                                        dataFromUser += value;
                                        if (slider != null) updateSliderValue(value);
                                    }
                                }
                                if (character == '-') {
                                    if (minValue < 0) {
                                        String newString = new String();
                                        if (dataFromUser.charAt(0) != '-') {
                                            newString += '-';
                                            for (int i = 0; i < dataFromUser.length(); i++) {
                                                newString += dataFromUser.charAt(i);
                                            }
                                            dataFromUser = newString;
                                        } else {
                                            for (int i = 1; i < dataFromUser.length(); i++) {
                                                newString += dataFromUser.charAt(i);
                                            }
                                            dataFromUser = newString;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (Program.OS == Program.ANDROID) {
                    Program.actualPressedKeyDigitValue = -1;
                    Program.actualPressedValue = '@';
                }



            }



        }
    }

    private boolean hasCyrrilic(char character) {
        String chars = ""+character;
        for (int i = 0; i < chars.length(); i++) {
            if (Character.UnicodeBlock.of(chars.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC) ||
                    chars.charAt(i) == '-') {
                System.out.println("This string has cyrrilyc");
                return true;
            }
        }
        //System.out.println("This char " + character + " can be added to the text field");
        return false;
    }

    private void updateSliderValue(int newValue) {
        if (statement == PRESSED) {
            if (newValue >= minValue && newValue <= maxValue) {
                slider.setValue(newValue);
                slider.updateValueFromTextField();
            }
            //System.out.println("Value was updated");
        }
    }

    @Override
    public void update(Vec2 relativePos){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {

                if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                    if (!withDeleteCross) {
                        statement = PRESSED;
                        pressedNow = true;
                        if (!Program.isVirtualKeyboardOpened()) {
                            Program.openVirtualKeyboard(true);
                        }
                    }
                    else {
                        if (isCrossPressed(relativePos)) {
                            clear();
                        }
                        else {
                            statement = PRESSED;
                            pressedNow = true;
                            if (!Program.isVirtualKeyboardOpened()) {
                                Program.openVirtualKeyboard(true);
                                //Program.virtualKeyboardOpened = true;
                            }

                        }
                    }

                }
            }
            else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                statement = ACTIVE;
                pressedNow = false;

            }
            if (statement == PRESSED) updateTextWrite(Program.engine.key, Program.engine.keyCode);
        }
        /*
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Programm.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Programm.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Programm.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Programm.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                    if (!withDeleteCross) {
                        statement = PRESSED;
                        pressedNow = true;
                        if (!Programm.virtualKeyboardOpened) {
                            Programm.virtualKeyboardOpened = true;
                        }
                    }
                    else {
                        if (isCrossPressed(relativePos)) {
                            System.out.println("Cross pressed");
                            clear();
                        }
                        else {
                            statement = PRESSED;
                            pressedNow = true;
                            if (!Programm.virtualKeyboardOpened) {
                                Programm.virtualKeyboardOpened = true;
                            }
                        }
                    }
                }
            }
            else if (Programm.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                statement = ACTIVE;
                pressedNow = false;
            }
            if (statement == PRESSED) updateTextWrite(Programm.engine.key, Programm.engine.keyCode);
        }
        */
    }

    private boolean isCrossPressed(Vec2 relativePos) {
        if (Program.engine.dist(deleteCrossPosition.x, deleteCrossPosition.y, Program.engine.mouseX-relativePos.x, Program.engine.mouseY-relativePos.y)<elementHeight/1.5f){
            return true;
        }
        else return false;
    }

    private void clear() {
        System.out.println("Cleared");
        clearTextValue();
        /*
        setTextValue("");
        if (slider!= null){
            slider.setMinValue();
        }*/

        setStatement(ACTIVE);
    }

    public void update(Vec2 relativePos, char character, byte keyCode){
        if (statement != BLOCKED) {
            if (relativePos == null) {
                relativePos = new Vec2(0, 0);
            }
            if ((Program.engine.mouseX > (pos.x - (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseX < (pos.x + (elementWidth / 2) + relativePos.x)) && (Program.engine.mouseY > (pos.y - (elementHeight / 2) + relativePos.y)) && (Program.engine.mouseY < (pos.y + (elementHeight / 2) + relativePos.y))) {
                if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                    statement = PRESSED;
                    pressedNow = true;
                }
            }
            else if (Program.engine.mousePressed && !Editor2D.prevMousePressedStatement) {
                statement = ACTIVE;
                pressedNow = false;
            }
            if (statement == PRESSED) updateTextWrite(character, keyCode);
        }
    }

    public void draw(PGraphics graphic){
        drawButtonBody(graphic);
        drawValue(graphic, PApplet.CENTER);
    }


    public void drawValue(PGraphics graphic, int xAlignment){
        if (graphic!=null) {
            //graphic.beginDraw();
            graphic.pushStyle();
            graphic.fill(0xff1040E8);
            //graphic.fill(0x7C7777);

            graphic.textSize(textHeight);
            graphic.textAlign(xAlignment, xAlignment);
            graphic.text(dataFromUser, textPos.x, textPos.y);
            graphic.popStyle();
            //graphic.endDraw();
        }
        else {
            Program.engine.pushStyle();
            Program.engine.textFont(textFont);
            Program.engine.fill(0xffFF0A0A);
            Program.engine.textSize(textHeight);
            Program.engine.textAlign(xAlignment, xAlignment);
            Program.engine.text(dataFromUser, textPos.x, textPos.y);
            Program.engine.popStyle();
        }
    }

    protected void drawButtonBody(PGraphics graphic){
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.pushStyle();
        graphic.imageMode(PConstants.CENTER);
        if (statement == PRESSED) graphic.image(picturePressed.getImage(), pos.x, pos.y, elementWidth, elementHeight);
        else graphic.image(pictureActive.getImage(), pos.x, pos.y, elementWidth, elementHeight);
        if (statement != PRESSED && dataFromUser.length()==0){
            drawName(graphic, PApplet.CENTER);
        }
        /*
        if (statement == ACTIVE){
            graphic.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
        }
        else if (statement == PRESSED){
            if (Game2D.engine.keyPressed) {
                graphic.image(picturePressed.getImage(), pos.x, pos.y, elementWidth, elementHeight);
            }
            else graphic.image(pictureWithData.getImage(), pos.x, pos.y, elementWidth, elementHeight);
        }
        else graphic.image(pictureActive.getImage(), pos.x,pos.y, elementWidth, elementHeight);
        */
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();
    }

    public boolean isDigitValueBetweenMaxAndMin(){
        if (forDigits){
            if (getDigitValue() >= minValue && getDigitValue() <= maxValue){
                return true;
            }
            return false;
        }
        return false;
    }

    public int getDigitValue(){
        int digitValue = ERROR_VALUE;
        try {
            digitValue = Integer.parseInt(dataFromUser);
        }
        catch(NumberFormatException e){
            //System.out.println("this is not a digit value");
        }
        if (digitValue > maxValue) {
            //System.out.println("Value is more than max");
            digitValue = maxValue;
        }
        return digitValue;
    }

    public void setTextValue(String value) {
        dataFromUser = value;
        if (value == ""){
            if (slider != null){
                slider.setMinValue();
            }
        }
    }

    public void clearTextValue() {
        dataFromUser = "";
        setValue(minValue);
        if (slider != null){
            slider.setMinValue();
            //slider.updateValueFromTextField();
        }
        System.out.println("Text field cleared");
    }

    public void setValue(int value) {
        dataFromUser = "";
        dataFromUser+=value;
    }

    public int getDigitIntegerValue(){
        int digitValue = ERROR_VALUE;
        try {
            digitValue = Integer.parseInt(dataFromUser);
        }
        catch(NumberFormatException e){
            //System.out.println("this is not a digit value");
        }
        if (forInteger) {
            if (digitValue > maxValue) {
                digitValue = maxValue;
            }
        }
        return digitValue;
    }

    public String getTextValue() {
        return dataFromUser;
    }
}
