package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.SingleImageDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.SingleSpriteAnimationDataForFigures;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class GraphicChoosingArea {
    private Timer areaChoosingTimer;
    private final int AREA_CHOOSING_TIME = 600;
    private final int TIME_TO_ALLOW_TAB_SCROLLING = 150;
    private Timer runningColorTimer;
    private final int TIME_TO_NEXT_RUNNING_COLOR = 150;
    private byte actualCornerNumberWithRunningColor = 0;

    private Timer cornerChoosingTimer;
    private final int CORNER_CHOOSING_TIME = AREA_CHOOSING_TIME/3;

    private Vec2 position;
    private Vec2 relativePosition;
    private Vec2 basePosition;
    private static Vec2 tabLeftUpperCorner;
    public Vec2 pictureRelativeShiftingFromTabLeftUpperCorner;
    private int width, height;
    private int imageWidth, imageHeight;
    private float scale = 1.0f;
    private boolean mouseOnArea = false;
    private final byte MIN_DISTANCE_BETWEEN_CORNERS = 8;

    private GraphicChoosingCorner [] corners;
    private boolean someCornerChoosed;


    public final static byte LEFT_UPPER_CORNER = 0;
    public final static byte RIGHT_UPPER_CORNER = 1;
    public final static byte LEFT_LOWER_CORNER = 2;
    public final static byte RIGHT_LOWER_CORNER = 3;


    public final static byte NOTHING = -1;
    public final static byte AREA_CHOOSED = -2;
    public final static byte AREA_MOVED = -3;
    public final static byte SOME_CORNER_CHOOSED = -4;
    public final static byte LEFT_UPPER_CORNER_CHOOSED = LEFT_UPPER_CORNER;
    public final static byte RIGHT_UPPER_CORNER_CHOOSED = RIGHT_UPPER_CORNER;
    public final static byte LEFT_LOWER_CORNER_CHOOSED = LEFT_LOWER_CORNER;
    public final static byte RIGHT_LOWER_CORNER_CHOOSED = RIGHT_LOWER_CORNER;
    public final static byte LEFT_UPPER_CORNER_MOVED = LEFT_UPPER_CORNER+10;
    public final static byte RIGHT_UPPER_CORNER_MOVED = RIGHT_UPPER_CORNER+10;
    public final static byte LEFT_LOWER_CORNER_MOVED = LEFT_LOWER_CORNER+10;
    public final static byte RIGHT_LOWER_CORNER_MOVED = RIGHT_LOWER_CORNER+10;



    private byte statement;
    private boolean textureMustBeUpdated = true;
    private static int[] areaPreviousData;

    //mutable
    private final Vec2 mutMousePos = new Vec2(0,0);

    public GraphicChoosingArea(Vec2 position, Vec2 tabLeftUpperCorner, Vec2 relativePosition, int width, int height, int imageWidth, int imageHeight){
        this.tabLeftUpperCorner = tabLeftUpperCorner;
        //System.out.println("Tab: " + tabLeftUpperCorner + "; Relatibe: " + relativePosition);
        pictureRelativeShiftingFromTabLeftUpperCorner = new Vec2(relativePosition.x, relativePosition.y);
        this.height = height;
        this.width = width;
        this.position = position;
        this.basePosition = relativePosition;
        this.relativePosition = relativePosition;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        corners = new GraphicChoosingCorner[4];
        Vec2 basicPosition = new Vec2(position.x-relativePosition.x, position.y-relativePosition.y);
        for (byte i = 0; i < corners.length; i++) {
            Vec2 actualElementPos;
            if (i == 0) actualElementPos = new Vec2(basicPosition.x, basicPosition.y);
            else if (i == 1) actualElementPos = new Vec2(basicPosition.x+width, basicPosition.y);
            else if (i == 2) actualElementPos = new Vec2(basicPosition.x, basicPosition.y+height);
            else if (i == 3) actualElementPos = new Vec2(basicPosition.x+width, basicPosition.y+height);
            else actualElementPos = null;
            corners[i] = new GraphicChoosingCorner(i, actualElementPos, relativePosition);
        }
    }



    public void draw(PGraphics graphic){
        drawCorners(graphic);
        drawChoosingArrowsToAngle(graphic);
        //graphic.beginDraw();
        //graphic.pushMatrix();
        //graphic.pushStyle();

        //graphic.translate(-relativePosition.x*scale+getCornerPosition(LEFT_UPPER_CORNER).x*scale+basePosition.x, -relativePosition.y*scale+getCornerPosition(LEFT_UPPER_CORNER).y*scale+basePosition.y);
        //graphic.translate(-relativePosition.x*scale+position.x*scale-basePosition.x, -relativePosition.y*scale+position.y*scale-basePosition.y);


        //graphic.translate(-relativePosition.x*scale+position.x*scale-basePosition.x, -relativePosition.y*scale+position.y*scale-basePosition.y);  // it was

        //graphic.imageMode(PConstants.CENTER);
        //graphic.stroke(200,0,0);
        //graphic.strokeWeight(5);
        //graphic.point(0,0);
        //graphic.popStyle();
        //graphic.popMatrix();
        //graphic.endDraw();
    }

    private void drawChoosingArrowsToAngle(PGraphics graphic) {
        for (byte i = 0; i < corners.length; i++){
            if (corners[i].getStatement() == GraphicChoosingCorner.IN_CHOOSING_PROCESS){
                //System.out.println("Distance: " );
                if (cornerChoosingTimer!=null){
                    //if (!cornerChoosingTimer.isTime()){
                        float relativeDist = (Program.engine.width/15f)*cornerChoosingTimer.getRestTime()/CORNER_CHOOSING_TIME;
                         corners[i].draw(relativeDist, graphic);
                    corners[i].draw(relativeDist/3f, graphic);
                    corners[i].draw(2f*relativeDist/3f, graphic);
                        //System.out.println("Distance: " + relativeDist);
                    //}
                }

            }
        }
    }

    public void textureWasUploadedOnMapZone(){
        textureMustBeUpdated = false;
    }

    public boolean mustBeGraphicDataUpdated(){
        return textureMustBeUpdated;
    }

    public TextureDataToStore getStaticTextureDataToStore(LevelsEditorProcess levelsEditorProcess, TilesetZone tilesetZone){
        int [] verticies = new int[4];
        verticies[0] = (int)(position.x-basePosition.x);
        verticies[1] = (int)(position.y-basePosition.y);
        verticies[2] = (int)(verticies[0]+width);
        verticies[3] = (int)(verticies[1]+height);
        TextureDataToStore data = new TextureDataToStore(tilesetZone.getGraphic(), verticies, levelsEditorProcess.levelsEditorControl.objectData.getFill());
        return data;
    }

    private Vec2 getCornerPosition(byte whichCorner){
        return corners[whichCorner].getPosition();
    }

    private void updateCornerSelecting(Vec2 pos, Vec2 relativePos) {
        if (Program.engine.mousePressed) {
            if (!Editor2D.prevMousePressedStatement) {
                for (byte i = 0; i < corners.length; i++) {
                    if (isPointOnCorner(corners[i], pos, relativePos)) {
                        if (corners[i].getStatement() == GraphicChoosingCorner.NOT_SELECTED || corners[i].getStatement() == GraphicChoosingCorner.IN_CHOOSING_PROCESS) {
                            corners[i].setStatement(GraphicChoosingCorner.IN_CHOOSING_PROCESS);
                            System.out.println("Corner is choosed");
                            if (cornerChoosingTimer == null) {
                                System.out.println("Corner started to choosed");
                                //statement = SOME_CORNER_CHOOSED;
                                cornerChoosingTimer = new Timer(CORNER_CHOOSING_TIME);
                            }
                        }
                    }
                }
            }
            //if (someCornerChoosen == false) {
                if (cornerChoosingTimer!=null) {
                    if (cornerChoosingTimer.isTime()) {
                        System.out.println("Corner is picked and can be moved");
                        for (byte i = 0; i < corners.length; i++) {
                            if (isPointOnCorner(corners[i], pos, relativePos)) {
                                corners[i].setStatement(GraphicChoosingCorner.SELECTED);
                                //someCornerChoosen = true;
                                cornerChoosingTimer = null;
                            }
                        }
                    }
                }
            //}
        }
        else {
            releaseCorners();
        }
    }

    private void releaseCorners(){
        for (byte i = 0; i < corners.length; i++) {
            if (corners[i].getStatement() == GraphicChoosingCorner.SELECTED || corners[i].getStatement() == GraphicChoosingCorner.IN_CHOOSING_PROCESS) {
                System.out.println("Corner is released");
                corners[i].setStatement(GraphicChoosingCorner.NOT_SELECTED);
            }
        }
        if (cornerChoosingTimer != null) cornerChoosingTimer = null;
        if (statement == SOME_CORNER_CHOOSED) statement = 0;
    }

    public boolean isPointOnCorner(GraphicChoosingCorner corner, Vec2 pos, Vec2 relativePos){
        if (relativePos == null){
            relativePos = new Vec2(0,0);
        }
        float crossX = (corner.getPosition().x-relativePos.x)*scale;
        float crossY =  (corner.getPosition().y-relativePos.y)*scale;
        //if (PApplet.dist(pos.x+basePosition.x, pos.y+basePosition.y, crossX, crossY)<GraphicChoosingCorner.getWidth()*scale){
        System.out.print("Point on: " + (int)pos.x + "x" + (int)pos.y + "; Corner pos: " + (int)crossX + "x" + (int)crossY + "; Test radius: " + (GraphicChoosingCorner.getWidth()*scale*1.2f));
        if (PApplet.dist(pos.x, pos.y, crossX, crossY)<(GraphicChoosingCorner.getWidth()*scale*1.2f)){
            System.out.println(": Caught");
            return true;
        }
        else {
            System.out.println(": Not caught");
            return false;
        }
    }



    public boolean isPointOnArea(Vec2 pos, Vec2 relativePos){
        if (relativePos == null){
            relativePos = new Vec2(0,0);
        }
        if (pos.x>(((corners[0].getPosition().x-relativePos.x)*scale+basePosition.x)) &&
                pos.x<(((corners[1].getPosition().x-relativePos.x)*scale+basePosition.x)) &&
                pos.y>(((corners[0].getPosition().y-relativePos.y)*scale+basePosition.y)) &&
                pos.y<(((corners[3].getPosition().y-relativePos.y)*scale)+basePosition.y)){

         /*
            if (pos.x>(((corners[0].getPosition().x-relativePos.x)*scale+basePosition.x)) &&
                pos.x<(((corners[1].getPosition().x-relativePos.x)*scale+basePosition.x)) &&
                pos.y>(((corners[0].getPosition().y-relativePos.y)*scale+basePosition.x)) &&
                pos.y<(((corners[3].getPosition().y-relativePos.y)*scale)+basePosition.x)){
            */
            /*
            if (pos.x>((corners[0].getPosition().x-relativePos.x)*scale) &&
                pos.x<((corners[1].getPosition().x-relativePos.x)*scale) &&
                pos.y>((corners[0].getPosition().y-relativePos.y)*scale) &&
                pos.y<((corners[3].getPosition().y-relativePos.y)*scale)){
            */


            mouseOnArea = true;
            return true;
        }
        else {
            mouseOnArea = false;
            return false;
        }

    }

    private void drawCorners(PGraphics graphic) {
        for (byte i = 0; i < corners.length; i++){
            corners[i].draw(0, graphic);

        }
        if (statement != AREA_CHOOSED && statement != AREA_MOVED){
            if (Program.engine.mousePressed) drawWrappingRect(graphic);
        }
    }

    private void drawWrappingRect(PGraphics graphic) {
        if (areaChoosingTimer != null){
            float relativeShifting = Program.engine.width/21.2f;
            if (!areaChoosingTimer.isTime()) {
                relativeShifting = relativeShifting * areaChoosingTimer.getRestTime() / AREA_CHOOSING_TIME;
                for (byte i = 0; i < corners.length; i++) {
                    corners[i].draw(relativeShifting, graphic);
                    corners[i].draw(relativeShifting / 2, graphic);
                }
            }
        }

    }

    public byte getStatement(){
        return statement;
    }



    public Vec2 getPosition(){
        return position;
    }

    public void update(float scale, Vec2 relativePos) {
        this.scale = scale;
        this.relativePosition = relativePos;
        for (int i = 0; i < corners.length; i++){
            corners[i].update(relativePosition, scale);
        }
        updateMouseControl(relativePos);
    }

    public boolean canBeTabScrolled(){
        if (someCornerChoosed) return false;
        else if (areaChoosingTimer != null){
            if (!areaChoosingTimer.isTime()){
                if (areaChoosingTimer.getRestTime()>(AREA_CHOOSING_TIME-TIME_TO_ALLOW_TAB_SCROLLING)){
                    return false;
                }
                else return true;  //was true
            }
            else return true;
        }
        else return true;
    }

    public boolean isMouseOnArea(){
        return mouseOnArea;
    }

    private void updateMouseControl(Vec2 relativePos){
        mutMousePos.x = Program.engine.mouseX-tabLeftUpperCorner.x;
        mutMousePos.y = Program.engine.mouseY-tabLeftUpperCorner.y;
        //Vec2 testPoint = new Vec2(Program.engine.mouseX, Program.engine.mouseY);
        //testPoint.x-= tabLeftUpperCorner.x;
        //testPoint.y-= tabLeftUpperCorner.y;
        updateCornerSelecting(mutMousePos, relativePos);

        for (byte i = 0; i < corners.length; i++) {
            if (cornerChoosingTimer!= null) {
                if (!cornerChoosingTimer.isTime()) someCornerChoosed = true;
            }
            if (corners[i].getStatement() == GraphicChoosingCorner.SELECTED && !someCornerChoosed){
                someCornerChoosed = true;
            }
        }
        if (!someCornerChoosed) {
            if (isPointOnArea(mutMousePos, relativePos)) {
                if (Program.engine.mousePressed) {
                    if (!Editor2D.prevMousePressedStatement) {
                        System.out.println("Started to press on zone");
                        if (Editor2D.getMouseWayLengthProLastFrame() < Editor2D.maxMovementProOneFrameForStaticMouse) {
                            if (areaChoosingTimer == null) {
                                areaChoosingTimer = new Timer(AREA_CHOOSING_TIME);
                                System.out.println("timer of area choosing was started");
                            }
                        }
                    }
                    else {
                        if (areaChoosingTimer != null) {
                            if (areaChoosingTimer.isTime()) {
                                if (statement != AREA_CHOOSED) {
                                    statement = AREA_CHOOSED;
                                    areaChoosingTimer = null;
                                    for (byte i = 0; i < corners.length; i++) corners[i].makeGraphicChosen(true);
                                    System.out.println("Area started to move after choosing");
                                }
                            }
                        }
                        if (statement == AREA_CHOOSED || statement == AREA_MOVED) {
                            Vec2 shifting = new Vec2(+(Program.engine.mouseX - Program.engine.pmouseX) / scale, +(Program.engine.mouseY - Program.engine.pmouseY) / scale);
                            System.out.println("Area is dragging");
                            translateArea(shifting);
                            textureMustBeUpdated = true;
                        }
                    }
                }
                else {
                    if (statement == AREA_CHOOSED || statement == AREA_MOVED) {
                        releaseArea();
                        if (!Program.engine.mousePressed) makeRunningColorForCorners();
                    }
                }
                //System.out.println("In zone");
            }
            else {
                if (statement != 0) releaseArea();
                if (!Program.engine.mousePressed) makeRunningColorForCorners();
            }
        }
        else updateCornersZooming();
        //System.out.println("Statement: " + statement);
    }

    public boolean canBeGraphicZoneMoved(){
        boolean canBeMoved = true;
        for (byte i = 0; i < corners.length; i++) {
            if (corners[i].getStatement() == GraphicChoosingCorner.SELECTED) {
                canBeMoved = false;
                //return canBeMoved;
            }
        }
        if (statement == AREA_CHOOSED || statement == AREA_MOVED)
            canBeMoved = false;
        if (areaChoosingTimer != null){
            if (!Program.engine.mousePressed) canBeMoved = true;
            else canBeMoved = false;
        }
        if (cornerChoosingTimer != null){
            if (!Program.engine.mousePressed) canBeMoved = true;
            else canBeMoved = false;
        }
        if (canBeMoved) {
            resetAllTimers();
        }
        //System.out.println("Can be moved: " + canBeMoved);
        return canBeMoved;

    }

    private void resetAllTimers() {
        if (areaChoosingTimer != null) {
            areaChoosingTimer = null;
            System.out.println("Area choosing timer was reset");
            //if (statement == AREA_CHOOSED)
        }
        if (cornerChoosingTimer != null){
            cornerChoosingTimer = null;
            System.out.println("Corner choosing timer was reset");
            if (Program.engine.mousePressed){
                for (byte i = 0; i < corners.length; i++){
                    if (corners[i].getStatement() == GraphicChoosingCorner.SELECTED){
                        corners[i].setStatement(GraphicChoosingCorner.NOT_SELECTED);
                        System.out.println("Corner statement set on NOT SELECTED");
                    }
                }
            }
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    private void updateCornersZooming() {
        //System.out.println("Some corners are choosen");
        byte cornerToBeSelectedNumber = -1;
        for (byte i = 0; i < corners.length; i++) {
            if (corners[i].getStatement() == GraphicChoosingCorner.SELECTED){
                cornerToBeSelectedNumber = i;
            }
        }
        if (cornerToBeSelectedNumber>=0){
            Vec2 shifting = getShiftingByMouseZooming();
            corners[cornerToBeSelectedNumber].translate(shifting);
            if (cornerToBeSelectedNumber == 0){
                corners[1].translate(0, shifting.y);
                corners[2].translate(shifting.x, 0);
            }
            else if (cornerToBeSelectedNumber == 1){
                corners[0].translate(0, shifting.y);
                corners[3].translate(shifting.x, 0);
            }
            else if (cornerToBeSelectedNumber == 2){
                corners[3].translate(0, shifting.y);
                corners[0].translate(shifting.x, 0);
            }
            else if (cornerToBeSelectedNumber == 3){
                corners[2].translate(0, shifting.y);
                corners[1].translate(shifting.x, 0);
            }
            //if (cornerToBeSelectedNumber!=3) updatePosition();
            updateMinimalDistanceBetweenCorners();
            updateWidthFromCorners();
            textureMustBeUpdated = true;
        }
    }

    private void updateMinimalDistanceBetweenCorners() {
        /*
        if ((corners[1].getPosition().x-corners[0].getPosition().x)<MIN_DISTANCE_BETWEEN_CORNERS){
            System.out.println("Minimal width");
        }
        if ((corners[2].getPosition().y-corners[0].getPosition().y)<MIN_DISTANCE_BETWEEN_CORNERS){
            System.out.println("Minimal height");
        }
        */
    }

    private void updateWidthFromCorners(){
        System.out.println("Dimensions were: " + width + " x " + height);
        float previsiousWidth = width;
        float previsiousHeight = height;
        width = (int)(corners[1].getPosition().x-corners[0].getPosition().x);
        height = (int)(corners[2].getPosition().y-corners[0].getPosition().y);
        //Vec2 shiftingVector = new Vec2((width-previsiousWidth)/2,(height-previsiousHeight)/2);
        position.x=corners[0].getPosition().x+basePosition.x;
        position.y=corners[0].getPosition().y+basePosition.y;
        //releaseArea();
        System.out.println("Dimensions are: " + width + " x " + height);
    }

    private Vec2 getShiftingByMouseZooming(){
        Vec2 shifting = new Vec2(+(Program.engine.mouseX - Program.engine.pmouseX) / scale, +(Program.engine.mouseY - Program.engine.pmouseY) / scale);
        float xShifting  = shifting.x;
        float yShifting  = shifting.y;
        if ((xShifting+position.x)<0) xShifting = (int)(0-position.x);
        else if ((xShifting+position.x+width/2)>imageWidth) {
            xShifting = (int)(imageWidth-position.x-width/2);
            //System.out.println("Right side!");
        }
        if ((yShifting+position.y)<0) yShifting = (int)(0-position.y);
        else if ((yShifting+position.y+height/2)>imageHeight) {
            yShifting = (int)(imageHeight-position.y-height/2);
            //System.out.println("Lower side!");
        }
        position.x+=xShifting;
        position.y+=yShifting;
        return new Vec2(xShifting, yShifting);
    }

    private void makeRunningColorForCorners() {
        if (runningColorTimer == null) runningColorTimer = new Timer(TIME_TO_NEXT_RUNNING_COLOR);
        if (runningColorTimer.isTime()) {
            runningColorTimer.setNewTimer(TIME_TO_NEXT_RUNNING_COLOR);
            actualCornerNumberWithRunningColor++;
            if (actualCornerNumberWithRunningColor > (corners.length-1)) actualCornerNumberWithRunningColor = 0;
            for (byte i = 0; i < corners.length; i++) {
                corners[i].setTint(GraphicChoosingCorner.BASIC_COLOR_TINT);
            }
            if (actualCornerNumberWithRunningColor == 2) corners[actualCornerNumberWithRunningColor+1].setTint(GraphicChoosingCorner.RUNNING_COLOR_TINT);
            else if (actualCornerNumberWithRunningColor == 3) corners[actualCornerNumberWithRunningColor-1].setTint(GraphicChoosingCorner.RUNNING_COLOR_TINT);
            else corners[actualCornerNumberWithRunningColor].setTint(GraphicChoosingCorner.RUNNING_COLOR_TINT);
        }
    }

    private void releaseArea(){
        statement = 0;
        System.out.println("Area was released");
        Vec2 shifting = new Vec2(0,0);
        float minX = PApplet.floor(position.x);
        if ((minX+0.5f)<position.x) shifting.x = minX+1-position.x;
        else shifting.x = position.x-minX;
        float minY = PApplet.floor(position.y);
        if ((minY+0.5f)<position.y) shifting.y = minY+1-position.y;
        else shifting.y = position.y-minY;
        translateArea(shifting);
        textureMustBeUpdated = true;
        for (byte i = 0; i < corners.length; i++) corners[i].makeGraphicChosen(false);
        if (areaChoosingTimer != null) areaChoosingTimer = null;
    }



    public void translateArea(Vec2 shifting){
        float xShifting  = shifting.x;
        float yShifting  = shifting.y;
        if ((xShifting+position.x)<0) xShifting = (int)(0-position.x);
        else if ((xShifting+position.x+width/2)>imageWidth) {
            xShifting = (int)(imageWidth-position.x-width/2);
            //System.out.println("Right side!");
        }
        if ((yShifting+position.y)<0) yShifting = (int)(0-position.y);
        else if ((yShifting+position.y+height/2)>imageHeight) {
            yShifting = (int)(imageHeight-position.y-height/2);
            //System.out.println("Lower side!");
        }
        position.x+=xShifting;
        position.y+=yShifting;
        for (byte i = 0; i < corners.length; i++) corners[i].translate(xShifting, yShifting);

    }



    public int[] getStaticTextureOnTilesetCoordinates() {
        int [] verticies = new int[4];
        verticies[0] = (int)(position.x-basePosition.x);
        verticies[1] = (int)(position.y-basePosition.y);
        verticies[2] = (int)(verticies[0]+width);
        verticies[3] = (int)(verticies[1]+height);
        saveAreaPreviousData();
        return verticies;
    }



    private void saveAreaPreviousData(){
        if (areaPreviousData == null) areaPreviousData = new int[4];
        areaPreviousData[0] = (int)(position.x-basePosition.x);
        areaPreviousData[1] = (int)(position.y-basePosition.y);
        areaPreviousData[2] = (int)(areaPreviousData[0]+width);
        areaPreviousData[3] = (int)(areaPreviousData[1]+height);
    }

    public static int [] getAreaPreviousData(){
        if (areaPreviousData == null) {
            areaPreviousData = new int[4];
            areaPreviousData[0] = 1;
            areaPreviousData[1] = 1;
            areaPreviousData[2] = (int)(areaPreviousData[0]+15);
            areaPreviousData[3] = (int)(areaPreviousData[1]+15);
        }
        return areaPreviousData;
    }

    public void drawRectOnNewPlace(Vec2 newPlaceRelativeToCorners, int tint, PGraphics graphic) {
        //graphic.beginDraw();
        graphic.pushMatrix();
        graphic.rectMode(PConstants.CORNER);
        graphic.pushStyle();
        graphic.noStroke();
        graphic.fill(255, tint);
        graphic.rectMode = PConstants.CORNER;
        Vec2 tilesetZoneShifting = new Vec2(relativePosition.x*scale, relativePosition.y*scale);
        float xTranslation = tilesetZoneShifting.x+
                (getCornerPosition(LEFT_UPPER_CORNER).x)*scale
                        +basePosition.x;
        float yTranslation = tilesetZoneShifting.y+
                (getCornerPosition(LEFT_UPPER_CORNER).y)*scale
                +basePosition.y;
        graphic.translate(xTranslation, yTranslation);
        //graphic.translate(newPlaceRelativeToCorners.x, newPlaceRelativeToCorners.y);
        System.out.println("Shifting is: " + newPlaceRelativeToCorners);
        int rectWidth = (int) ((getCornerPosition(RIGHT_UPPER_CORNER).x - getCornerPosition(LEFT_UPPER_CORNER).x)*scale);
        int rectHeight = (int) ((getCornerPosition(RIGHT_LOWER_CORNER).y - getCornerPosition(LEFT_UPPER_CORNER).y)*scale);
        graphic.rect(0, 0, rectWidth, rectHeight);

        graphic.rectMode(PConstants.CORNER);
        graphic.popStyle();
        graphic.popMatrix();
        //graphic.endDraw();


    }

    public GraphicDataForFigures getTextureData (Image tileset, boolean type){
        if (type == TilesetZone.SPRITE) return getTextureData(tileset);
        else if (type == TilesetZone.ANIMATION) return getAnimationData(tileset);
        else return null;
    }

    private SingleSpriteAnimationDataForFigures getAnimationData(Image graphic) {
        int [] verticies = new int[4];
        verticies[0] = (int)(position.x-basePosition.x);
        verticies[1] = (int)(position.y-basePosition.y);
        verticies[2] = (verticies[0]+width);
        verticies[3] = (verticies[1]+height);
        SingleSpriteAnimationDataForFigures data = new SingleSpriteAnimationDataForFigures((Animation) graphic, verticies);
        return data;
    }

    private SingleImageDataForFigures getTextureData(Image graphic) {
        int [] verticies = new int[4];
        verticies[0] = (int)(position.x-basePosition.x);
        verticies[1] = (int)(position.y-basePosition.y);
        verticies[2] = (int)(verticies[0]+width);
        verticies[3] = (int)(verticies[1]+height);
        SingleImageDataForFigures data = new SingleImageDataForFigures((Image) graphic, verticies);
        return data;
    }
}
