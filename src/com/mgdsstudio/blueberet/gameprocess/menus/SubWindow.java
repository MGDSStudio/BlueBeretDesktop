package com.mgdsstudio.blueberet.gameprocess.menus;

import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_TextArea;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


import java.util.ArrayList;

public class SubWindow extends GameMenu {
    private Vec2 leftUpperCorner;
    public static final int CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING = Program.engine.width/150;
    public final static int NORMAL_SCROLLING_VALUE_FOR_MOUSE_WHEEL_ROTATION = (int)(Program.engine.width/30f);


    protected final boolean X_AXIS = true, Y_AXIS = false;
    protected int visibleHeight, visibleWidth;
    protected boolean scrollableAlongX, scrollableAlongY;
    protected int maxRelativeX, maxRelativeY;
    //protected Vec2 relativePosition;
    protected boolean scrollingNow = false;
    protected Vec2 menuPositionShifting;
    protected int width, height;
    protected boolean scrollable = true;
    protected PGraphics tabGraphic;
    private static Image tabImage;
    private static boolean graphicLoaded = false;
    private int relativePositionY = 0;
    private int maxRelativePositionY = 0;

    public SubWindow(Vec2 leftUpperCorner, int width, int height) {
        this.leftUpperCorner = leftUpperCorner;
        this.width = width;
        visibleHeight = height;
        this.height = height;
        guiElements = new ArrayList<>();
        loadGraphic();

    }

    private void loadGraphic() {
        if (Program.graphicRenderer == Program.OPENGL_RENDERER)
            tabGraphic = Program.engine.createGraphics(width, height + maxRelativePositionY, PApplet.P2D);
        if (Program.graphicRenderer == Program.JAVA_RENDERER)
            tabGraphic = Program.engine.createGraphics(width, height + maxRelativePositionY, PApplet.JAVA2D);
        if (!graphicLoaded) {
            try {
                tabImage = new Image(Program.getAbsolutePathToAssetsFolder("MenuBackground.png"));
                System.out.println("Successfully loaded background");
                tabImage.getImage().resize(width, tabGraphic.height);
                graphicLoaded = true;
            }
            catch (Exception e){
                System.out.println("Can not resize background image");
                e.printStackTrace();
            }
        }
    }

    void addGUI(ArrayList <androidGUI_Element> androidGui_elements){
        for (androidGUI_Element androidGui_element : androidGui_elements){
            this.guiElements.add(androidGui_element);
        }
        recalculateHeight();
    }

    private void recalculateHeight() {


    }


    protected void addTextData(ArrayList<String> textData, androidAndroidGUI_TextArea textArea){
        System.out.println("Function add text data must be overriden");
    }

    protected void hideBackMenu(){

        Program.engine.pushStyle();
        Program.engine.fill(50,125);
        Program.engine.noStroke();
        Program.engine.rect(0,0, Program.engine.width, Program.engine.height);
        Program.engine.popStyle();
        /*
        menu.beginDraw();
        menu.pushStyle();
        menu.fill(50,125);
        menu.noStroke();
        menu.rect(0,0, Programm.engine.width, Programm.engine.height);
        menu.popStyle();*/
    }

    protected void updateScrolling(){
        boolean scrollingStatementChanged = false;
        if (isPointOnTable(new Vec2(Program.engine.mouseX, Program.engine.mouseY))) {
            if (Program.engine.mousePressed == true) {
                if (scrollableAlongY) {
                    if (Program.engine.abs(Program.engine.mouseY - Program.engine.pmouseY) > CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING) {
                        scroll(Program.engine.pmouseY - Program.engine.mouseY, Y_AXIS);
                        scrollingStatementChanged = true;
                    }
                }
                if (scrollableAlongX) {
                    if (Program.engine.abs(Program.engine.mouseX - Program.engine.pmouseX) > CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING) {
                        scroll(Program.engine.pmouseX - Program.engine.mouseX, X_AXIS);
                        scrollingStatementChanged = true;
                    }
                }
            }
            else {
                if (Program.OS == Program.DESKTOP){
                    if (scrollableAlongY) {
                        //System.out.println("Rotation: " + Programm.getMouseWheelRotation());
                        if (Program.getMouseWheelRotation() == Program.BACKWARD_ROTATION) {
                            scroll(-SubWindow.NORMAL_SCROLLING_VALUE_FOR_MOUSE_WHEEL_ROTATION, Y_AXIS);
                            scrollingStatementChanged = true;
                        }
                        else if (Program.getMouseWheelRotation() == Program.FORWARD_ROTATION){
                            scroll(SubWindow.NORMAL_SCROLLING_VALUE_FOR_MOUSE_WHEEL_ROTATION, Y_AXIS);
                            scrollingStatementChanged = true;
                        }
                    }
                }
            }
        }
        if (!scrollingStatementChanged) scrollingNow = false;
    }

    public boolean isPointOnTable(Vec2 pos) {
        if (pos.x > leftUpperCorner.x && pos.x < (leftUpperCorner.x + visibleWidth) && pos.y > leftUpperCorner.y && pos.y < (leftUpperCorner.y + visibleHeight)) {
            return true;
        } else return false;
    }

    public void scroll(float scrolling, boolean axis) {
        if (axis == Y_AXIS) {
            relativePositionY+=scrolling;
            if (relativePositionY < 0) relativePositionY = 0;
            if (relativePositionY > maxRelativeY) relativePositionY = (int)maxRelativeY;
            menuPositionShifting.y = leftUpperCorner.y-relativePositionY;
            System.out.println("Scrolling Y " + scrolling  + "; FPS: " + Program.engine.frameRate + "; Max relative: " +  maxRelativeY + "Scrolling: " + scrolling);
        }
        scrollingNow = true;

    }

    @Override
    public void draw(){
        hideBackMenu();
        drawBackground();

        for (androidGUI_Element button : guiElements) button.draw(tabGraphic);
        Program.engine.pushMatrix();
        Program.engine.imageMode(PConstants.CORNER);
        //Programm.engine.image(tabGraphic, leftUpperCorner.x, leftUpperCorner.y, width, height);


        Program.engine.image(tabGraphic, leftUpperCorner.x, leftUpperCorner.y, width, visibleHeight, 0, (int)relativePositionY, width, (int)relativePositionY+visibleHeight);
        Program.engine.imageMode(PConstants.CENTER);

        //drawBackground();
        //        for (GUI_Element button : guiElements) button.draw(tabGraphic);
        //        Programm.engine.pushMatrix();
        //        Programm.engine.imageMode(PConstants.CORNER);
        //        Programm.engine.image(tabGraphic, leftUpperCorner.x, leftUpperCorner.y, width, height);
        //        Programm.engine.imageMode(PConstants.CENTER);
        //        Programm.engine.popMatrix();

        Program.engine.imageMode(PConstants.CENTER);
        Program.engine.popMatrix();
        //System.out.println("Drawn " + guiElements.size() + "; leftUpperCorner " + leftUpperCorner + "; W: " + width + "; visH: " + visibleHeight);
    }

    public boolean isScrollingNow(){
        return scrollingNow;
    }

    protected void drawBackground(){
        if (!scrollable){
            tabGraphic.beginDraw();
            tabGraphic.pushStyle();
            tabGraphic.imageMode(PApplet.CORNER);
            tabGraphic.image(tabImage.getImage(), 0, 0, width, height);
            tabGraphic.popStyle();
            tabGraphic.endDraw();
        }
        else {
            tabGraphic.beginDraw();
            tabGraphic.pushStyle();
            tabGraphic.imageMode(PApplet.CORNER);
            tabGraphic.image(tabImage.getImage(), 0, 0, width, height);
            tabGraphic.popStyle();
            tabGraphic.endDraw();
        }
    }

}
