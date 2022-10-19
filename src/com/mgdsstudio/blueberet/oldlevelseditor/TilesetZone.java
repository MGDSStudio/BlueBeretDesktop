package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.TouchScreenActionBuffer;
import com.mgdsstudio.blueberet.onscreenactions.OnPinchAction;
import com.mgdsstudio.blueberet.onscreenactions.OnScreenActionType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class TilesetZone {
    private Image graphic;
    //private Animation animation;
    private Timer newPlaceChoosingTimer, timerToStartSinglePixelMovementAfterJump;
    private final int TIME_FOR_ARROW_BUTTON_RELEASE = 800;
    private final int AREA_CHOOSING_TIME = 600;
    private GraphicChoosingArea graphicChoosingArea;

    private androidGUI_ScrollableTab tab;
    private Vec2 position;
    private Vec2 leftUpperCorner;
    private Vec2 rightLowerCorner;
    private int normalWidth;
    private int normalHeight;
    private float scale = 1.00f;
    private final float scaleChangingStep = 0.05f;
    private float MIN_SCALE = 0.05f;
    private float MAX_SCALE = 5f;
    private boolean graphicWasMovedOrScaled = false;
    private float deathZone = 0.02f;
    private boolean textureOnMapZoneWasUpdated = false;
    private boolean textureMustBeUpdated = true;
    public static final byte LINK_TO_LAST_FIGURE = 0;
    public static final byte LINK_TO_GRAPHIC_ELEMENT = 1;
    public static final byte LINK_TO_FIRST_FIGURE = 2;
    private byte forWhichElement = LINK_TO_GRAPHIC_ELEMENT;
    private SingleGraphicElement singleGraphicElement;
    private ArrayList <androidAndroidGUI_Button> arrowButtons;

    protected final String UP = "↑";
    protected final String DOWN = "↓";
    protected final String LEFT = "←";
    protected final String RIGHT = "→";

    private String actualPressedButton = "";

    private final int TIME_FOR_LONG_PRESS = 1000;
    private final float MOVEMENT_VELOCITY_FOR_ARROW_MOVEMENT = 1.1f;

    public final static boolean SPRITE = false;
    public final static boolean ANIMATION = true;
    private boolean graphicType = SPRITE;



    public TilesetZone (String path, androidGUI_ScrollableTab tab, Vec2 leftUpperCorner, Vec2 rightLowerCorner, byte forWhichElement, boolean graphicType){

        if (graphicType == SPRITE) graphic = new Image(Program.getAbsolutePathToAssetsFolder(path));
        else graphic = new Animation(Program.getAbsolutePathToAssetsFolder(path));
        this.graphicType = graphicType;
        this.tab = tab;
        this.leftUpperCorner = leftUpperCorner;
        this.rightLowerCorner = rightLowerCorner;
        normalWidth = (int)((rightLowerCorner.x-leftUpperCorner.x)*scale);
        normalHeight = (int)((rightLowerCorner.y-leftUpperCorner.y)*scale);
        int [] areaStartData = GraphicChoosingArea.getAreaPreviousData();
        Vec2 startCrossPosition = new Vec2(areaStartData[0], areaStartData[1]);
        startCrossPosition.x+=leftUpperCorner.x;
        startCrossPosition.y+=leftUpperCorner.y;
        graphicChoosingArea = new GraphicChoosingArea(startCrossPosition, tab.getLeftUpperCorner() ,leftUpperCorner, (int)(areaStartData[2]-areaStartData[0]), (int)(areaStartData[3]-areaStartData[1]), graphic.getImage().width, graphic.getImage().height);
        position = new Vec2(0,0);
        calculateMinScale();
        scale = ((MAX_SCALE+MIN_SCALE)/2)* Program.engine.width/ Program.DEBUG_DISPLAY_WIDTH;
        if (scale > MAX_SCALE) MAX_SCALE = scale;
        System.out.println("Tileset zone scale: " + scale);
        this.forWhichElement =forWhichElement;
        createButtons();
        timerToStartSinglePixelMovementAfterJump = new Timer(TIME_FOR_ARROW_BUTTON_RELEASE);
    }

    private void createButtons(){
        arrowButtons = new ArrayList<>();
        Vec2 freeZoneLeftUpperCorner = new Vec2(rightLowerCorner.x, leftUpperCorner.y);
        Vec2 freeZoneRightLowerCorner = new Vec2(leftUpperCorner.x+tab.getWidth(), rightLowerCorner.y);
        int xPos = (int)((freeZoneLeftUpperCorner.x+(freeZoneRightLowerCorner.x-freeZoneLeftUpperCorner.x)/2f));
        int yStep =  (int)((freeZoneRightLowerCorner.y-freeZoneLeftUpperCorner.y)/4f);
        int buttonWidth = (int)((freeZoneRightLowerCorner.x-freeZoneLeftUpperCorner.x)*0.85f);
        int buttonHeight = (int)(yStep*0.85f);
        //int distanceToButtonX = (int)(buttonWidth/1.3f);

        //float buttonsStep =  buttonHeight*1.1f;
        androidAndroidGUI_Button up =  new androidAndroidGUI_Button(new Vec2((xPos), yStep/2), buttonWidth, buttonHeight, UP, false);

        androidAndroidGUI_Button down =  new androidAndroidGUI_Button(new Vec2((xPos), 3*yStep/2), buttonWidth, buttonHeight, DOWN, false);
        //tab.addGUI_Element(down, null);
        androidAndroidGUI_Button left =  new androidAndroidGUI_Button(new Vec2((xPos), 5*yStep/2), buttonWidth, buttonHeight, LEFT, false);
        //tab.addGUI_Element(left, null);
        androidAndroidGUI_Button right =  new androidAndroidGUI_Button(new Vec2((xPos), 7*yStep/2), buttonWidth, buttonHeight, RIGHT, false);
        //tab.addGUI_Element(right, null);
        /*
        GUI_Button up =  new GUI_Button(new Vec2(((tab.getWidth()-distanceToButtonX)), buttonsStep/2), buttonWidth, buttonHeight, UP, false);

        GUI_Button down =  new GUI_Button(new Vec2(((tab.getWidth()-distanceToButtonX)), 2*buttonsStep), buttonWidth, buttonHeight, DOWN, false);
        //tab.addGUI_Element(down, null);
        GUI_Button left =  new GUI_Button(new Vec2(((tab.getWidth()-distanceToButtonX)), 3*buttonsStep), buttonWidth, buttonHeight, LEFT, false);
        //tab.addGUI_Element(left, null);
        GUI_Button right =  new GUI_Button(new Vec2(((tab.getWidth()-distanceToButtonX)), 4*buttonsStep), buttonWidth, buttonHeight, RIGHT, false);
        //tab.addGUI_Element(right, null);
        */
        arrowButtons.add(up);
        arrowButtons.add(down);
        arrowButtons.add(left);
        arrowButtons.add(right);
    }

    public Image getGraphic() {
        return graphic;
    }

    public GraphicChoosingArea getGraphicChoosingArea(){
        return graphicChoosingArea;
    }

    private void calculateMinScale(){
        float minScaleForWidth = PApplet.abs(leftUpperCorner.x-rightLowerCorner.x)/ graphic.getImage().width;
        float minScaleForHeight = PApplet.abs(leftUpperCorner.y-rightLowerCorner.y)/ graphic.getImage().height;
        if (minScaleForWidth < minScaleForHeight) MIN_SCALE = minScaleForWidth;
        else MIN_SCALE = minScaleForHeight;
        //System.out.println("Min scale " + MIN_SCALE);
    }

    public boolean canBeTabScrolled(){
        //System.out.println("Tab can be scrolled: " + graphicChoosingArea.canBeTabScrolled());
        return graphicChoosingArea.canBeTabScrolled();
    }

    public void update(LevelsEditorProcess levelsEditorProcess, Vec2 relativePos){
        if (graphicWasMovedOrScaled == true) graphicWasMovedOrScaled = false;
        graphicChoosingArea.update(scale, position);
        if (isPointOnZone(Program.engine.mouseX, Program.engine.mouseY, relativePos)) {
            if (graphicChoosingArea.getStatement() != GraphicChoosingArea.AREA_MOVED && graphicChoosingArea.getStatement()!= GraphicChoosingArea.AREA_CHOOSED) {
                updateScale(relativePos);
                if (!Editor2D.wasMouseMoved()) newPlaceForGraphicChoosingAreaChoosing(relativePos);
                else updateZoneMovement(relativePos);
                updateGraphicBoardsPositions();
            }
        }
        if (mustBeGraphicDataUpdated()) {
            System.out.println("Graphic type " + graphicType);
            if (forWhichElement == LINK_TO_LAST_FIGURE) levelsEditorProcess.addTextureToActualFigure(graphicChoosingArea.getTextureData(graphic, graphicType));
            else if (forWhichElement == LINK_TO_FIRST_FIGURE) levelsEditorProcess.addTextureToFirstFigure(graphicChoosingArea.getTextureData(graphic, graphicType));
            else {
                if (singleGraphicElement != null) {
                    singleGraphicElement.setTextureData(graphicChoosingArea.getTextureData(graphic, graphicType));
                    System.out.println("Picture updated !. Test it for animation");
                }
            }
            textureWasUploadedOnMapZone();
        }
        updateArrowButtons(relativePos);
    }

    private int getMovingStepForArrow(String direction){
        int nominalStep = 0;
        if (direction == UP) nominalStep = graphicChoosingArea.getHeight();
        else if (direction == DOWN) nominalStep = graphicChoosingArea.getHeight();
        else if (direction == LEFT) nominalStep = graphicChoosingArea.getWidth();
        else if (direction == RIGHT)  nominalStep = graphicChoosingArea.getWidth();
        return nominalStep;
    }

    private void updateArrowButtons(Vec2 relativePos){
        for (androidAndroidGUI_Button button : arrowButtons){
            button.update(relativePos);
            if (button.isLongPressed(TIME_FOR_LONG_PRESS)){
                String direction = button.getName();
                translateGraphicChoosingArea(direction, getMovingStepForArrow(direction));
                button.setStatement(androidGUI_Element.ACTIVE);
                button.resetPressedMoment();
                timerToStartSinglePixelMovementAfterJump = new Timer(TIME_FOR_ARROW_BUTTON_RELEASE);
            }
            else {
                if (button.getStatement() == androidGUI_Element.RELEASED && timerToStartSinglePixelMovementAfterJump.isTime()){
                    String direction = button.getName();
                    translateGraphicChoosingArea(direction, 1f);
                    button.setStatement(androidGUI_Element.ACTIVE);

                }
            }
        }
    }

    private void translateGraphicChoosingArea(String direction, float value){
        Vec2 shifting = new Vec2(0,0);
        if (direction == UP) shifting.y=-value;
        else if (direction == DOWN) shifting.y+=value;
        else if (direction == LEFT) shifting.x=-value;
        else if (direction == RIGHT) shifting.x+=value;
        graphicChoosingArea.translateArea(shifting);
        updateGraphicBoardsPositions();
        textureMustBeUpdated = true;
    }

    private void textureWasUploadedOnMapZone(){
        //textureOnMapZoneWasUpdated = true;
        textureMustBeUpdated = false;
        graphicChoosingArea.textureWasUploadedOnMapZone();
    }

    public boolean mustBeGraphicDataUpdated(){
        if (textureMustBeUpdated) return true;
        if (graphicChoosingArea.mustBeGraphicDataUpdated()) return true;
        else return false;
    }

    public SingleGraphicElement getSingleGraphicElement() {
        return singleGraphicElement;
    }

    public void setSingleGraphicElement(SingleGraphicElement singleGraphicElement) {
        this.singleGraphicElement = singleGraphicElement;
    }

    private void newPlaceForGraphicChoosingAreaChoosing(Vec2 relativePos) {
        if (!graphicChoosingArea.isMouseOnArea()) {
            if (graphicChoosingArea.canBeGraphicZoneMoved()) {
                if (Program.engine.mousePressed) {
                    if (!Editor2D.prevMousePressedStatement) {
                        if (newPlaceChoosingTimer == null) {
                            newPlaceChoosingTimer = new Timer(AREA_CHOOSING_TIME);
                            System.out.println("Area replacing timer was started");
                        }
                    }
                    if (newPlaceChoosingTimer != null) {
                        if (newPlaceChoosingTimer.isTime()) {
                            newPlaceChoosingTimer = null;
                            System.out.println("Area was replaced");
                            Vec2 areaShifting = getAreaShifting(relativePos);
                            graphicChoosingArea.translateArea(areaShifting);
                            textureMustBeUpdated = true;
                        }
                    }
                }
                else if (newPlaceChoosingTimer != null) newPlaceChoosingTimer = null;
            }
        }
        else {
            if (newPlaceChoosingTimer != null) newPlaceChoosingTimer = null;
        }
    }


    private Vec2 getAreaShifting(Vec2 relativePos){
        Vec2 actualPos = new Vec2(graphicChoosingArea.getPosition().x, graphicChoosingArea.getPosition().y);
        //System.out.println("position was: " + actualPos);
        Vec2 newPos = new Vec2(Program.engine.mouseX, Program.engine.mouseY);
        //System.out.println("Graphic pos: " + position);
        newPos.x-=relativePos.x;
        newPos.y-=relativePos.y;

        //newPos.x-=leftUpperCorner.x;
        //newPos.y-=leftUpperCorner.y;
        newPos.x/=scale;
        newPos.y/=scale;

        newPos.x-=(graphicChoosingArea.getWidth()/2);
        newPos.y-=(graphicChoosingArea.getHeight()/2);

        newPos.x+=(position.x);
        newPos.y+=(position.y);

        newPos.x-=(actualPos.x);
        newPos.y-=(actualPos.y);
        //System.out.println("Scale: " + scale);
        //System.out.println("newPos: " + newPos);
        //System.out.println("actualPos: " + actualPos);


        //newPos.y-=(+tab.getLeftUpperCorner().y-tab.getTabGraphic().height);
        //System.out.println("tab: " + tab.getLeftUpperCorner().y + "; " + tab.getHeight() + "; " + tab.getRelativePositionY() + "; " + tab.getMaxRelativePositionY()+ "; Tab objectsFrame: " + tab.getTabGraphic().width+ "x"+ tab.getTabGraphic().height+"Scale: " + scale);
        //newPos.y-=(tab.getMaxRelativePositionY());
        //newPos.y-=(tab.getLeftUpperCorner().y);
        //System.out.println("Shifting is: " + newPos);
        return newPos;
    }

    private void updateScale(Vec2 relativePos){
        //System.out.println("Scale updating");
        if (graphicChoosingArea.canBeGraphicZoneMoved()) {
            //System.out.println("Scale " + scale);
            if (Program.OS == Program.DESKTOP) {
                if (Program.getMouseWheelRotation() == Program.FORWARD_ROTATION) {
                    scale += scaleChangingStep;
                    Program.setMouseWheelRotation(Program.NO_ROTATION);
                    graphicWasMovedOrScaled = true;
                } else if (Program.getMouseWheelRotation() == Program.BACKWARD_ROTATION) {
                    scale -= scaleChangingStep;
                    Program.setMouseWheelRotation(Program.NO_ROTATION);
                    graphicWasMovedOrScaled = true;
                }
                if (scale > MAX_SCALE) {
                    scale = MAX_SCALE;
                } else if (scale < MIN_SCALE) scale = MIN_SCALE + deathZone;
            }
            else {
                //System.out.println("For android can the scale of the zone not changed yet");
            }
        }
        if (Program.OS == Program.ANDROID){
            if (TouchScreenActionBuffer.getActionType() == OnScreenActionType.PINCH){
                OnPinchAction action = (OnPinchAction)TouchScreenActionBuffer.getAction();
                if (tab.isPointOnTab(action.getCenter())) {
                    float scaleValue = action.getValue()*0.035f;
                    scale += scaleValue;
                    if (scale > MAX_SCALE) {
                        scale = MAX_SCALE;
                    }
                    else if (scale < MIN_SCALE) scale = MIN_SCALE + deathZone;

                }
                else {
                    System.out.print("Pinch is not on tab; Pos: " + (int)action.getPosition().x + "X" + (int)action.getPosition().y);
                    //System.out.println("Tab is on: " )
                }
                TouchScreenActionBuffer.clearBuffer();
            }
        }
    }

    public boolean isPointOnZone(int x, int y, Vec2 relativePos){
        if (relativePos == null){
            relativePos = new Vec2(0,0);
        }
        if (x>(leftUpperCorner.x+relativePos.x) &&
                x<(rightLowerCorner.x+relativePos.x) &&
                y>(leftUpperCorner.y+relativePos.y) &&
                y<(rightLowerCorner.y+relativePos.y)){
            //MapRedactor.engine.println("Finger on the map zone");
            return true;
        }
        else return false;
    }

    public boolean isPointOnZone(Vec2 pos, Vec2 relativePos){
        if (relativePos == null){
            relativePos = new Vec2(0,0);
        }
        if (pos.x>(leftUpperCorner.x+relativePos.x) &&
                pos.x<(rightLowerCorner.x+relativePos.x) &&
                pos.y>(leftUpperCorner.y+relativePos.y) &&
                pos.y<(rightLowerCorner.y+relativePos.y)){

            return true;
        }
        else return false;
    }

    public void draw(PGraphics graphic, Vec2 relativePos){
        graphic.pushMatrix();
        graphic.pushStyle();
        drawPicture(graphic, this.graphic, (byte)0);
        drawNewPlaceAreaRect(graphic);
        graphic.imageMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        graphicChoosingArea.draw(graphic);
        for (androidAndroidGUI_Button button : arrowButtons) button.draw(graphic);
    }

    private void drawNewPlaceAreaRect(PGraphics graphic) {
        if (newPlaceChoosingTimer!=null){
            if (!newPlaceChoosingTimer.isTime()) {
                int alpha = 255;
                alpha = (int)(255-(alpha * newPlaceChoosingTimer.getRestTime() / AREA_CHOOSING_TIME));

                //Vec2 shifting = new Vec2(+(Game2D.engine.mouseX - Game2D.engine.pmouseX) / scale, +(Game2D.engine.mouseY - Game2D.engine.pmouseY) / scale);

                graphicChoosingArea.drawRectOnNewPlace(getAreaShifting(leftUpperCorner), alpha, graphic);

            }
        }
    }

    private void drawPicture(PGraphics graphic, Image image, byte picturePart){
        graphic.imageMode(PConstants.CORNER);
        graphic.image(image.getImage(), leftUpperCorner.x, leftUpperCorner.y, normalWidth, normalHeight, (int)position.x, (int)position.y, (int)position.x+(int)(normalWidth/scale), (int)position.y+(int)(normalHeight/scale));

    }

    private Vec2 getCenter(){
        Vec2 center = new Vec2(position.x+(normalWidth/scale)/2, position.y+(normalHeight/scale)/2);
        return center;
    }

    private Vec2 getRightLowerVisibleAreaCorner(){
        Vec2 corner = getCenter();
        corner.x+=(int)(normalWidth/scale)/2;
        corner.y+=(int)(normalHeight/scale)/2;
        return corner;
    }


    private void updateGraphicBoardsPositions(){
        if (position.x<0) position.x = 0;
        else if (getRightLowerVisibleAreaCorner().x>(graphic.getImage().width)) {
            position.x = (graphic.getImage().width)-normalWidth/scale;
            //System.out.println("Right pos");
        }
        if (position.y<0) position.y = 0;
        else if (getRightLowerVisibleAreaCorner().y>(graphic.getImage().height)) {
            //System.out.println("Lower pos");
            position.y = (graphic.getImage().height)-normalHeight/scale;
        }
    }

    private void updateZoneMovement(Vec2 relativePos){
        if (graphicChoosingArea.canBeGraphicZoneMoved()) {
            //System.out.println("Zone can be moved");
            if (Program.engine.mousePressed && Editor2D.prevMousePressedStatement) {
                if (Editor2D.wasMouseMoved()) {
                    //if (isPointOnZone(new Vec2(Programm.engine.pmouseX, Programm.engine.pmouseY), relativePos) == true) {
                    if (isPointOnZone(Program.engine.pmouseX, Program.engine.pmouseY, relativePos) == true) {
                        if (Program.engine.abs(Program.engine.dist(Program.engine.mouseX, Program.engine.pmouseX, Program.engine.mouseY, Program.engine.pmouseY)) > Editor2D.maxMovementProOneFrameForStaticMouse) {
                            position.x -= (Program.engine.mouseX - Program.engine.pmouseX) / scale;
                            position.y -= (Program.engine.mouseY - Program.engine.pmouseY) / scale;
                            if (Program.debug) System.out.println("Visible zone is translated");
                            graphicWasMovedOrScaled = true;
                            //textureMustBeUpdated
                        }
                    }
                }
            }
        }
    }



}
