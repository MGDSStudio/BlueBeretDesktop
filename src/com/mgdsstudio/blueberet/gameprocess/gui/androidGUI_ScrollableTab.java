package com.mgdsstudio.blueberet.gameprocess.gui;

import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.TilesetZone;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class androidGUI_ScrollableTab {
    public static final int CRITICAL_DELTA_MOUSE_POS_FOR_SCROLLING = Program.engine.width/150;

    private ArrayList<androidGUI_Element> guiElements;
    //GUI_Scrollbar scrollbar;
    private TilesetZone tilesetZone;


    private int relativePositionY = 0;
    private int maxRelativePositionY = 0;
    Vec2 leftUpperCorner;
    int width, height;
    private PGraphics tabGraphic;
    private final Image frameImage;
    private int minimalHeight;

    //Controller data
    private boolean scrollingAlongX, scrollingAlongY;
    private boolean tabGraphicWasRecreatedByThisFrame;

    public androidGUI_ScrollableTab(LevelsEditorProcess levelsEditorProcess) {
        leftUpperCorner = new Vec2(Editor2D.leftUpperCorner.x, Editor2D.zoneHeight + 3 * Editor2D.leftUpperCorner.y + levelsEditorProcess.getOnScreenConsole().getHeight()-Editor2D.distanceToMapZoneBoard*0.5f);
        width = Editor2D.zoneWidth;
        minimalHeight = (int) (Program.engine.height - Editor2D.leftUpperCorner.y - leftUpperCorner.y);
        height = minimalHeight*4;
        if (Program.graphicRenderer == Program.OPENGL_RENDERER)
            tabGraphic = Program.engine.createGraphics(width, height + maxRelativePositionY, PApplet.P2D);
        else if (Program.graphicRenderer == Program.JAVA_RENDERER)
            tabGraphic = Program.engine.createGraphics(width, height + maxRelativePositionY, PApplet.JAVA2D);
        frameImage = new Image(Program.getAbsolutePathToAssetsFolder("GUI_TabFrame.png"));
        frameImage.getImage().resize(width, tabGraphic.height);
        guiElements = new ArrayList<androidGUI_Element>();
    }

    public TilesetZone getTilesetZone() {
        return tilesetZone;
    }

    public void setHeight(int height){

    }

    public PGraphics getTabGraphic() {
        return tabGraphic;
    }

    public int getMaxRelativePositionY(){
        return maxRelativePositionY;
    }

    public void setMinimalHeight(){
        this.height = minimalHeight;
        relativePositionY = 0;
    }

    public void recalculateHeight(ArrayList<androidGUI_Element> elements){
        scrollingAlongY = true;
        if (elements == null) elements = this.guiElements;
        float maxY = elements.get(elements.size()-1).getPosition().y;
        maxY+=elements.get(elements.size()-1).getHeight();

        int elementsHeight = 0;
        for (androidGUI_Element element: elements){
            elementsHeight+=element.getHeight();
        }
        float freeSpacesSingleHeight = (elementsHeight/elements.size())/1f;
        elementsHeight+=((elements.size()+1)*freeSpacesSingleHeight);
        elementsHeight = (int)maxY;
        this.height = (int)elementsHeight;

        relativePositionY = 0;
        updateMaxY();
    }

    public ArrayList<androidGUI_Element> getElements(){
        return guiElements;
    }

    public androidGUI_Element getPressedElement(){
        for (androidGUI_Element element: guiElements){
            if (element.getStatement() == androidGUI_Element.PRESSED) return element;
        }
        return null;
    }

    public androidGUI_Element getReleasedElement(){
        for (androidGUI_Element element: guiElements){
            if (element.getStatement() == androidGUI_Element.RELEASED) return element;
        }
        return null;
    }

    private void  updateMaxYWithTrouble() {
        if (height == minimalHeight) {
            maxRelativePositionY = 0;   // When many buttons must be recalculated
            tabGraphic.height = minimalHeight;
        }
        else if (height > minimalHeight){
            maxRelativePositionY = (int)(height-minimalHeight);
        }
        tabGraphic.height = minimalHeight+maxRelativePositionY;
        /*if (Program.graphicRenderer == Program.OPENGL_RENDERER) tabGraphic = Program.engine.createGraphics(width, minimalHeight + maxRelativePositionY, PApplet.P2D);
        else tabGraphic = Program.engine.createGraphics(width, minimalHeight+maxRelativePositionY, PApplet.JAVA2D);
        */
        tabGraphic.height = minimalHeight + maxRelativePositionY;
        frameImage.getImage().resize(width, tabGraphic.height);
    }

    private void updateMaxY() {

        if (tabGraphic != null){
            if (tilesetZone != null){
                if (tilesetZone.getGraphic() != null) {
                    tabGraphic.removeCache(tilesetZone.getGraphic().image);
                }
            }
            /*tabGraphic.removeCache(GUI_Button.pictureActive.getImage());
            tabGraphic.removeCache(GUI_Button.picturePressed.getImage());
            tabGraphic.removeCache(GUI_Button.pictureBlocked.getImage());
            tabGraphic.removeCache(GUI_TextField.pictureActive.getImage());
*/
            Program.engine.g.removeCache(tabGraphic);
            //tabGraphic.dispose();
            if (tabGraphic != null) tabGraphic = null;
            //System.out.println("Tab graphic was deleted");
            System.gc();
        }
        if (height == minimalHeight) {
            maxRelativePositionY = 0;   // When many buttons must be recalculated
            //tabGraphic.height = minimalHeight;
        }
        else if (height > minimalHeight){
            maxRelativePositionY = (int)(height-minimalHeight);
        }
        if (Program.graphicRenderer == Program.OPENGL_RENDERER) tabGraphic = Program.engine.createGraphics(width, minimalHeight + maxRelativePositionY, PApplet.P2D);
        else tabGraphic = Program.engine.createGraphics(width, minimalHeight+maxRelativePositionY, PApplet.JAVA2D);

        /*
        if (height == minimalHeight) {
            maxRelativePositionY = 0;   // When many buttons must be recalculated
            tabGraphic.height = minimalHeight;
        }
        else if (height > minimalHeight){
            maxRelativePositionY = (int)(height-minimalHeight);
        }
        tabGraphic.height = minimalHeight+maxRelativePositionY;

        tabGraphic.height = minimalHeight + maxRelativePositionY;
        frameImage.getImage().resize(width, tabGraphic.height);
        */
    }

    public void update(LevelsEditorProcess levelsEditorProcess) {
        boolean scrollingBlocked = false;
        for (androidGUI_Element guiElement : guiElements) {
            if (guiElement.isVisibleOnTab(this)) {
                if (guiElement.getClass() == androidAndroidGUI_Button.class)
                    guiElement.update(new Vec2(0, leftUpperCorner.y - relativePositionY));
                else guiElement.update(new Vec2(leftUpperCorner.x, leftUpperCorner.y - relativePositionY));
            }
            //else System.out.println("Element " + guiElement + " is not visible on tab");
        }
        if (tilesetZone != null) {
            tilesetZone.update(levelsEditorProcess, leftUpperCorner);
            scrollingBlocked = tilesetZone.canBeTabScrolled();
        }
        if (!scrollingBlocked) updateControl();
    }

    private void updateControl() {
        if (maxRelativePositionY > 0) {
            if (Program.engine.mousePressed == true) {
                if (isPointOnTab(new PVector(Program.engine.mouseX, Program.engine.mouseY))) {
                    if (4 * Program.engine.abs(Program.engine.mouseX - Program.engine.pmouseX) < (Program.engine.abs(Program.engine.mouseY - Program.engine.pmouseY))) {
                        scrollAlongYPosition(Program.engine.pmouseY - Program.engine.mouseY);
                        scrollingAlongY = true;
                    }
                }
            } else {
                scrollingAlongX = false;
                scrollingAlongY = false;
            }
        }
    }

    public void scrollAlongYPosition(float scrolling) {
        relativePositionY += scrolling;
        if (relativePositionY < 0) relativePositionY = 0;
        else if (relativePositionY > maxRelativePositionY) relativePositionY = maxRelativePositionY;
    }

    public boolean isPointOnTab(PVector pos) {
        if (pos.x > leftUpperCorner.x && pos.x < (leftUpperCorner.x + width) && pos.y > leftUpperCorner.y && pos.y < (leftUpperCorner.y + height)) {
            return true;
        } else return false;
    }

    public void hideButton(androidAndroidGUI_Button menuButton) {
        menuButton.hide();
    }

    /*
    public int getLeftline(){
        return leftUpperCorner.y;
    }*/

    public void addGUI_Element(androidGUI_Element button, Vec2 pos) {
        guiElements.add(button);
        if (button.getPosition().x == 0) button.getPosition().x = width / 2;
        if (button.getPosition().y == 0) {
            if (guiElements.size() == 1) {
                button.getPosition().y = button.elementHeight / 1.4f;
            } else {
                button.getPosition().y = button.elementHeight / 1.4f + (guiElements.size() - 1) * button.elementHeight;
            }
        }
        updateMaxY();
    }

    public void draw() {
        tabGraphic.beginDraw();
        tabGraphic.imageMode(PApplet.CORNER);
        tabGraphic.image(frameImage.getImage(), 0, 0);
        for (int i = 0; i < guiElements.size(); i++) {
            guiElements.get(i).draw(tabGraphic);
        }

        if (tilesetZone!=null) tilesetZone.draw(tabGraphic, null);
        Program.engine.noClip();
        tabGraphic.endDraw();
        Program.engine.imageMode(PApplet.CENTER);
        Program.engine.image(tabGraphic, leftUpperCorner.x + width / 2, leftUpperCorner.y + height / 2,
                width, height,
                0, (int) getVerticalVisibleZone().x, width, (int) getVerticalVisibleZone().y);
    }

    public PVector getVerticalVisibleZone() {
        PVector pos = new PVector(relativePositionY, relativePositionY + height);
        return pos;
    }

    public Vec2 getLeftUpperCorner() {
        return leftUpperCorner;
    }


    public int getWidth() {
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    public void clearElements() {

        guiElements.clear();
        if (tilesetZone != null) tilesetZone = null;
        /*
        for (GUI_Element element: guiElements){

        }*/
    }

    public Vec2 getPositionsForNextElementAlongY(int height){
        if (height == 0) height = androidAndroidGUI_Button.NORMAL_HEIGHT;

        Vec2 position = new Vec2(width/2,0);
        if (guiElements.size() == 0) {
            position.y = height / 1.4f;
        } else {
            float centralHeight = 0;
            for (int i = 0;i < guiElements.size(); i++){
                centralHeight+=guiElements.get(i).getHeight();
            }
            float anotherElementsHeight = centralHeight/guiElements.size();
            float lastElementPos = guiElements.get(guiElements.size()-1).getPosition().y;
            //float  anotherElementsHeight = guiElements.get(guiElements.size()-1).getHeight();
            position.y = (height / 1.4f) + lastElementPos+guiElements.get(guiElements.size()-1).getHeight();
            //position.y = height / 1.4f + (guiElements.size() - 1) * anotherElementsHeight;
        }
        return position;
    }

    public void setTilesetZone(TilesetZone tilesetZone) {
        this.tilesetZone = tilesetZone;
    }


    public void deleteTilesetZone() {
        tilesetZone = null;
    }

    public int getLastElementPosition() {
        if (guiElements.size()>0){
            int yPos = (int)(guiElements.get(guiElements.size() - 1).getPosition().y);
            return yPos;
        }
        else return 0;
    }

    public int getRelativePositionY() {
        return relativePositionY;
    }

    public int getElementPositionY(int number) {
        if (number <= (guiElements.size()-1)) {
            if (guiElements.size() > 0) {
                int yPos = (int) (guiElements.get(number).getPosition().y);
                return yPos;
            } else return 0;
        }
        else return 0;
    }

    public void setScrollable(boolean b) {
        if (b) scrollingAlongY = true;
        else {
            scrollingAlongY = false;
            maxRelativePositionY = 0;
        }
    }

    public int getVisibleHeight() {
        if (maxRelativePositionY <= 1) return height;
        else {
            if (height > (Program.engine.height-leftUpperCorner.y)){
                //System.out.println("Scrollable but on screen");
                return (int)(Program.engine.height-leftUpperCorner.y);
            }
            else return height;
        }
    }
}
