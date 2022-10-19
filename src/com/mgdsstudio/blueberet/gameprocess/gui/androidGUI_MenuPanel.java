package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.graphic.ImageZoneFullData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class androidGUI_MenuPanel {
    private Vec2 leftUpperCorner;
    private Image icons;
    private  ImageZoneFullData[] imageZoneData;
    private String FILE = "File";
    private String EDIT = "Edit";
    private String SIMPLE_ROUND_ELEMENT = "Simple round element  ";
    private String COMPLEX_ROUND_ELEMENT = "Complex round element     ";
    private String PERSON = "Person";
    private String GRAPHIC = "Graphic";
    private String ZONE = "Zone";
    private String PREFERENCES = "Preferences";
    private String HELP = "Help";
    //"File", "Edit", "Simple round element", "Complex round element", "Person", "Graphic", "Zone", "Preferences", "Help"};
            //
    //private int upperX;
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    //private final static short
    private boolean side = RIGHT;
    private androidAndroidGUI_MovableComboBox[] comboBoxes;
    private PGraphics graphic;
    final int upperFreeZoneHeight;
    final int lowerFreeZoneHeight;
    final byte distanceBetweenComboBoxes = 5;
    private byte releasedElementNumber = -1;
    private boolean mouseIsOnSomeElement = false;
    public final static boolean withSingleGraphicElement = true;

    public androidGUI_MenuPanel(byte number){
        upperFreeZoneHeight = (int) Editor2D.leftUpperCorner.y;
        lowerFreeZoneHeight = (int)(Program.engine.height-upperFreeZoneHeight-Editor2D.zoneHeight);
        comboBoxes = new androidAndroidGUI_MovableComboBox[number];
        //leftUpperCorner = new Vec2(Game2D.engine.width - Editor2D.leftUpperCorner.x + distanceBetweenComboBoxes, Editor2D.leftUpperCorner.y);
        leftUpperCorner = new Vec2(Program.engine.width - Program.engine.width/13.33f, Editor2D.leftUpperCorner.y);

        int singleElementHeight = (int)((Editor2D.zoneHeight-(distanceBetweenComboBoxes*(number-1)))/number);
        //System.out.println("Height: " + (int)(singleElementHeight));
        Vec2 [] positions = getPositions(number, singleElementHeight);
        //Vec2 [] onScreenPositions =
        String [] names = new String[]{"File", "Edit", "Simple round element", "Complex round element", "Person", "Graphic", "Zone", "Preferences", "Help"};
        //GUI_MenuLowLevelTabType actionTypes [] new
        for (byte i = 0; i < comboBoxes.length; i++){
            //Vec2 nulPosition = new Vec2(leftUpperCorner.x, leftUpperCorner.y+i*singleElementHeight+distanceBetweenComboBoxes);
            comboBoxes[i] = new androidAndroidGUI_MovableComboBox(positions[i], singleElementHeight, names[i], getSubmenusNames(i), getGuiMenuLowLevelTabType(i));
        }

        System.out.println("Single element height: " + singleElementHeight);
        System.out.println("Single element width: " + comboBoxes[0].getWidth());
        if (Program.graphicRenderer == Program.OPENGL_RENDERER) graphic = Program.engine.createGraphics(Program.engine.width, Program.engine.height, PApplet.P2D);
        else graphic = Program.engine.createGraphics(Program.engine.width, Program.engine.height, PApplet.JAVA2D);

        icons = new Image(Program.getAbsolutePathToAssetsFolder("MenuIcons.png"));
        createImageZoneData(names);
        switchOffSomeComboBoxes();
    }



    private void createImageZoneData(String [] names) {
        imageZoneData = new ImageZoneFullData[names.length];
        imageZoneData[0] = new ImageZoneFullData(FILE, 59, 0, 116,60);
        imageZoneData[1] = new ImageZoneFullData(EDIT, 611, 8, 678,82);
        imageZoneData[2] = new ImageZoneFullData(SIMPLE_ROUND_ELEMENT, 4, 4, 54,54);
        imageZoneData[3] = new ImageZoneFullData(COMPLEX_ROUND_ELEMENT, 547, 1, 597,57);
        imageZoneData[4] = new ImageZoneFullData(PERSON, 260, 6, 314,66);
        imageZoneData[5] = new ImageZoneFullData(GRAPHIC, 392, 0, 450,60);
        imageZoneData[6] = new ImageZoneFullData(ZONE, 122, 5, 171,55);
        imageZoneData[7] = new ImageZoneFullData(PREFERENCES, 177, 0, 250,74);
        imageZoneData[8] = new ImageZoneFullData(HELP, 470, 10, 518,84);
    }

    private androidGUI_MenuLowLevelTabType[] getGuiMenuLowLevelTabType(byte number){
        androidGUI_MenuLowLevelTabType[] types;
        switch (number){
            case 0: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.NEW_MAP, androidGUI_MenuLowLevelTabType.OPEN_MAP, androidGUI_MenuLowLevelTabType.CLEAR_MAP, androidGUI_MenuLowLevelTabType.TEST_MAP, androidGUI_MenuLowLevelTabType.SAVE_MAP, androidGUI_MenuLowLevelTabType.EXIT}; break;
            case 1: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.MOVE_CAMERA, androidGUI_MenuLowLevelTabType.SELECT, androidGUI_MenuLowLevelTabType.CANCEL, androidGUI_MenuLowLevelTabType.MOVE, androidGUI_MenuLowLevelTabType.COPY, androidGUI_MenuLowLevelTabType.EDIT_OBJECT, androidGUI_MenuLowLevelTabType.DELETE}; break;
            case 2: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.NEW_ROUND_RECTANGULAR, androidGUI_MenuLowLevelTabType.NEW_ROUND_CIRCLE, androidGUI_MenuLowLevelTabType.NEW_ROUND_POLYGON, androidGUI_MenuLowLevelTabType.NEW_COLLECTABLE_OBJECT}; break;
            case 3: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.NEW_ROUND_PIPE, androidGUI_MenuLowLevelTabType.NEW_BRIDGE, androidGUI_MenuLowLevelTabType.NEW_ROTATING_STICK, androidGUI_MenuLowLevelTabType.NEW_PLATFORM_SYSTEM}; break;
            case 4: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.PLACE_PLAYER, androidGUI_MenuLowLevelTabType.PLACE_GUMBA, androidGUI_MenuLowLevelTabType.PLACE_KOOPA, androidGUI_MenuLowLevelTabType.PLACE_BOWSER, androidGUI_MenuLowLevelTabType.PLACE_SPIDER, androidGUI_MenuLowLevelTabType.PLACE_SNAKE}; break;
            //case 4: types = new GUI_MenuLowLevelTabType[]{GUI_MenuLowLevelTabType.PLACE_PLAYER, GUI_MenuLowLevelTabType.PLACE_GUMBA, GUI_MenuLowLevelTabType.PLACE_KOOPA, GUI_MenuLowLevelTabType.PLACE_BOWSER, GUI_MenuLowLevelTabType.PLACE_SPIDER, GUI_MenuLowLevelTabType.PLACE_SNAKE}; break;
            //
            case 5: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.BACKGROUND, androidGUI_MenuLowLevelTabType.NEW_SPRITE, androidGUI_MenuLowLevelTabType.NEW_ANIMATION}; break;
            case 6: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.NEW_LAVA_BALLS_ZONE, androidGUI_MenuLowLevelTabType.NEW_CLEARING_ZONE, androidGUI_MenuLowLevelTabType.NEW_BULLETS_BILL_ZONE, androidGUI_MenuLowLevelTabType.NEW_OBJECTS_APPEARING_ZONE, androidGUI_MenuLowLevelTabType.NEW_PORTAL_SYSTEM_ZONE, androidGUI_MenuLowLevelTabType.NEW_CAMERA_FIXATION_ZONE, androidGUI_MenuLowLevelTabType.NEW_LEVEL_END_ZONE}; break;
            case 7: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.GRID_PREFERENCES, androidGUI_MenuLowLevelTabType.DISPLAYING_PREFERENCES, androidGUI_MenuLowLevelTabType.SETTINGS}; break;
            case 8: types = new androidGUI_MenuLowLevelTabType[]{androidGUI_MenuLowLevelTabType.HOW_TO_WORK_WITH_CAMERA, androidGUI_MenuLowLevelTabType.HOW_TO_ADD_NEW_OBJECTS}; break;
            default : types = new androidGUI_MenuLowLevelTabType[0];
        }
        return types;
    }

    private void switchOffSomeComboBoxes(){
        //ArrayList <String> notCompletedDropDownElements = new ArrayList<>();
        for (int i = 0; i < comboBoxes.length; i++){
            androidAndroidGUI_DropDownElement[] subElements = comboBoxes[i].getElements();
            for (int j = 0; j < subElements.length; j++){
                if (subElements[j].getName().equals("Move camera") || subElements[j].getName().equals("Pipe") || subElements[j].getName().contains("Turt") ||
                         subElements[j].getName().equals("Rotating stick") || subElements[j].getName().equals("Lava balls zone") || subElements[j].getName().equals("Rockets zone") || subElements[j].getName().equals("Appearing zone") || subElements[j].getName().equals("Display") || subElements[j].getName().equals("Appearing zone") || subElements[j].getName().equals("Settings") || subElements[j].getName().equals("How to add new objects") ){
                    subElements[j].setStatement(androidGUI_Element.BLOCKED);
                    System.out.println("element " + subElements[j].getName() + " was blocked");
                }
            }
        }
    }

    private String[] getSubmenusNames(byte number){
        String[] submenus;
        switch (number){
            case 0: submenus = new String[]{"New map", "Open map", "Clear map", "Test map", "Save map", "Exit"}; break;
            case 1: submenus = new String[]{"Move camera", "Select", "Clear selection", "Move", "Copy", "Edit object", "Delete"}; break;
            case 2: submenus = new String[]{"Rectangular", "Circle", "Triangle", "Collectable object"}; break;
            case 3: submenus = new String[]{"Pipe", "Bridge", "Rotating stick", "Platform system"}; break;
            case 4: submenus = new String[]{"Player", "Mushroom man", "Turtle", "Dragon", "Spider", "Snake"}; break;
            case 5: submenus = new String[]{"Background", "Sprite", "Animation"}; break;
            case 6: submenus = new String[]{"Lava balls zone", "Clearing zone", "Rockets zone", "Appearing zone", "Portal system", "Camera fixation zone", "End level zone"}; break;
            case 7: submenus = new String[]{"Grid preferences", "Display", "Settings"}; break;
            case 8: submenus = new String[]{"How to work with camera", "How to add new objects"}; break;
            default : submenus = new String[0];
        }
        return submenus;
    }

    private Vec2 [] getPositions(byte number, int elementHeight){
        Vec2 [] positions = new Vec2[number];
        for (int i = 0; i < number; i++){
            positions[i] = new Vec2(leftUpperCorner.x, leftUpperCorner.y+i*(elementHeight+distanceBetweenComboBoxes));        //positions[i] = new Vec2(0, i*(elementHeight+distanceBetweenComboBoxes));

        }
        return positions;
    }

    public void update(){
        //System.out.println(comboBoxes[0].getWrappingStatement());
        byte pressedElement = -1;
        for (int i = 0; i < comboBoxes.length; i++){
            if (comboBoxes[i].isPressed(Program.engine.mouseX, Program.engine.mouseY, PApplet.CORNER)){
                if (comboBoxes[i].getStatement() != androidGUI_Element.HIDDEN){
                    pressedElement = (byte)i;
                    comboBoxes[i].setStatement(androidGUI_Element.PRESSED);
                }
            }
        }
        if (pressedElement < 0){
            for (int i = 0; i < comboBoxes.length; i++){
                if (comboBoxes[i].getPrevisiousStatement() == androidGUI_Element.PRESSED){
                    comboBoxes[i].setStatement(androidGUI_Element.RELEASED);
                }
            }
        }
        else {
            for (int i = 0; i < comboBoxes.length; i++) {
                if (i != pressedElement) {
                    if (pressedElement >= 0){
                        if (comboBoxes[i].getStatement() != androidGUI_Element.HIDDEN) {
                            comboBoxes[i].setStatement(androidGUI_Element.ACTIVE);
                            comboBoxes[i].setWrappingStatement(androidAndroidGUI_MovableComboBox.WRAPPING_UP);
                        }
                    }
                    else if (comboBoxes[i].getStatement() != androidGUI_Element.RELEASED) comboBoxes[i].setStatement(androidGUI_Element.ACTIVE);
                }
            }
        }
        for (androidAndroidGUI_MovableComboBox comboBox : comboBoxes) comboBox.update(leftUpperCorner, false);
    }

    public boolean isSomeComboBoxShifted(){
        for (int i = 0; i < comboBoxes.length; i++) {
            if (comboBoxes[i].getMovementPositionStatement() != androidAndroidGUI_MovableComboBox.ON_BASE_POSITION){
                return true;
            }
        }
        return false;
    }

    public androidAndroidGUI_MovableComboBox getShiftedComboBox(){
        for (int i = 0; i < comboBoxes.length; i++) {
            if (comboBoxes[i].getMovementPositionStatement() != androidAndroidGUI_MovableComboBox.ON_BASE_POSITION){
                return comboBoxes[i];
            }
        }
        return null;
    }

    public void returnShiftedComboBoxToNull(){
        for (int i = 0; i < comboBoxes.length; i++) {
            if (comboBoxes[i].getWrappingStatement() != androidAndroidGUI_MovableComboBox.COMPLETE_WRAPPED_UP) comboBoxes[i].setWrappingStatement(androidAndroidGUI_MovableComboBox.WRAPPING_UP);
            if (comboBoxes[i].getMovementPositionStatement() != androidAndroidGUI_MovableComboBox.ON_BASE_POSITION){
                comboBoxes[i].setMovementPositionStatement(androidAndroidGUI_MovableComboBox.FROM_OPENING_TO_BASE);
                comboBoxes[i].setStatement(androidGUI_Element.ACTIVE);
                //System.out.println("Come back");
            }
        }
    }

    public String getActualPressedElementName(){
        //if (Game2D.engine.mousePressed) {
            for (int i = 0; i < comboBoxes.length; i++) {
                if (comboBoxes[i].getWrappingStatement() != androidAndroidGUI_MovableComboBox.COMPLETE_WRAPPED_UP) {
                    if (comboBoxes[i].isSomeElementPressed()) {
                        //System.out.println("Element " + comboBoxes[i].getPressedElementName() + " pressed");
                        return comboBoxes[i].getPressedElementName();
                    }
                }
            }
        //}
        return null;
    }

    public androidGUI_MenuLowLevelTabType getActualPressedElementAction(){
        //if (Game2D.engine.mousePressed) {
        for (int i = 0; i < comboBoxes.length; i++) {
            if (comboBoxes[i].getWrappingStatement() != androidAndroidGUI_MovableComboBox.COMPLETE_WRAPPED_UP) {
                if (comboBoxes[i].isSomeElementPressed()) {
                    //System.out.println("Element " + comboBoxes[i].getPressedElementName() + " pressed");
                    return comboBoxes[i].getPressedElementActionType();
                }
            }
        }
        //}
        return null;
    }

    private void drawAsSingleGraphic(){

        graphic.beginDraw();
        graphic.clear();
        for (byte i = 0; i < comboBoxes.length; i++) {
            comboBoxes[i].drawWithIconsOnSingleGraphic(graphic, icons, imageZoneData[i]);
        }
        graphic.endDraw();
        Program.engine.imageMode(PConstants.CORNER);
        Program.engine.image(graphic,0,0);

    }

    public void draw(){
        if (withSingleGraphicElement) drawAsSingleGraphic();
        else drawAsMultipleGraphicFiles();

    }

    private void drawAsMultipleGraphicFiles(){
        for (byte i = 0; i < comboBoxes.length; i++) {
            comboBoxes[i].drawWithIconsOnSingleGraphic(icons, imageZoneData[i]);
        }
    }
    /*
    public void draw(){

        graphic.beginDraw();
        graphic.imageMode(PConstants.CORNER);
        for (byte i = 0; i < comboBoxes.length; i++) comboBoxes[i].draw(graphic);
        graphic.endDraw();
        for (byte i = 0; i < comboBoxes.length; i++) comboBoxes[i].drawName(graphic, PApplet.CORNER);
        Game2D.engine.imageMode(PApplet.CORNER);
        Game2D.engine.image(graphic, leftUpperCorner.x, leftUpperCorner.y);
        //System.out.println(leftUpperCorner);
        Game2D.engine.imageMode(PApplet.CENTER);
    }
    */

}
