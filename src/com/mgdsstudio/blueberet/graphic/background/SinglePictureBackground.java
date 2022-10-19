package com.mgdsstudio.blueberet.graphic.background;

import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.opengl.Texture;

public class SinglePictureBackground extends Background implements IDrawable {
    //Image testLowerLine;
    private Image picture;
    private float relativeMovementVelocity = 0.4f;
    //private static float relativeMovementVelocity = 0.4f;
    //PGraphics upperLine, lowerLine;
    PImage upperLine, lowerLine;
    //Color upperLineColor, lowerLineColor;

    public SinglePictureBackground (String path, int maxWidth, int leftUpperY, boolean movable){
        picture = new Image(Program.getAbsolutePathToAssetsFolder(path));
        if (picture != null) {
            if (picture.getImage().width!= maxWidth){
                float proportion = (float) picture.getImage().width/picture.getImage().height;
                picture.getImage().resize(maxWidth, (int) ((float)maxWidth/proportion));
                picture.getImage().loadPixels();
                int upperLineColor = picture.getImage().get(1,1);
                upperLine = Program.engine.createImage(picture.getImage().width, 1, PConstants.RGB);
                for (int i = 0; i < upperLine.width; i++) upperLine.set(i,0, upperLineColor);
                int lowerLineColor = picture.getImage().get(picture.getImage().width-1,picture.getImage().height-1);
                lowerLine = Program.engine.createImage(picture.getImage().width, 1, PConstants.RGB);
                for (int i = 0; i < lowerLine.width; i++) lowerLine.set(i,0, lowerLineColor);
            }
        }
        else {
            System.out.println("Background " + path + " does not found");
        }
        this.moveable = movable;
        this.leftUpperY = leftUpperY;
    }

    public SinglePictureBackground (String path, int maxWidth, int leftUpperX, int leftUpperY, float relativeVelocity){
        picture = new Image(path);
        if (picture != null) {
            if (picture.getImage().width!= maxWidth){
                float proportion = (float) picture.getImage().width/picture.getImage().height;
                picture.getImage().resize(maxWidth, (int) ((float)maxWidth/proportion));
                picture.getImage().loadPixels();
                int upperLineColor = picture.getImage().get(1,1);
                upperLine = Program.engine.createImage(picture.getImage().width, 1, PConstants.RGB);
                for (int i = 0; i < upperLine.width; i++) upperLine.set(i,0, upperLineColor);
                int lowerLineColor = picture.getImage().get(picture.getImage().width-1,picture.getImage().height-1);
                lowerLine = Program.engine.createImage(picture.getImage().width, 1, PConstants.RGB);
                for (int i = 0; i < lowerLine.width; i++) lowerLine.set(i,0, lowerLineColor);
            }
        }
        else {
            System.out.println("Background " + path + " does not found");
        }
        if (relativeVelocity != 0) moveable = true;
        else moveable = false;
        this.leftUpperX = leftUpperX;
        this.leftUpperY = leftUpperY;
    }



    @Override
    public void draw(GameCamera gameCamera) {
        //if (Game2D.WITH_GRAPHIC) {
        if (Program.WITH_BACKGROUND) {
            clearBackground();
            if (!moveable) {
                drawStaticBackground(gameCamera);
            } else {
                drawDynamicBackground(gameCamera);
            }
        }
        else {
            clearBackground();
        }

    }

    private void drawDynamicBackground(GameCamera gameCamera) {
        Program.objectsFrame.beginDraw();
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.imageMode(PConstants.CORNER);
        Program.objectsFrame.pushMatrix();
        final int reserveZone = 0;
        //System.out.println(Game2D.objectsFrame.width + "x" + Game2D.objectsFrame.height);
        int width = (int) ((gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)) - gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE))-2*reserveZone;
        int height = (int) ((gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)) - gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE))-2*reserveZone;
        int centerX = (Program.objectsFrame.width/2);
        float centerY = (leftUpperY+ Program.objectsFrame.height/2);
        //Game2D.objectsFrame.clip(0 , 0, width, height);
        //Game2D.objectsFrame.clip(centerX , centerY, width, height);
        //System.out.println("Zone: " + centerX + " x " + centerY + " width: " + width + " height: " + height);
        Program.objectsFrame.imageMode(PConstants.CENTER);
        Program.objectsFrame.clip(centerX , centerY, width, height);
        Program.objectsFrame.imageMode(PConstants.CORNER);
        //leftUpperX+
        int startBackgroundNumber = PApplet.floor(((gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)-leftUpperX)* relativeMovementVelocity /(float) picture.getImage().width));
        int maxBackgroundsNumber = PApplet.ceil((float) Program.objectsFrame.width/(float)picture.getImage().width)+1;
        if (maxBackgroundsNumber == 1) maxBackgroundsNumber = 2;
        for (int i = startBackgroundNumber; i < (startBackgroundNumber+maxBackgroundsNumber); i++) {
            /*
            if (i == (startBackgroundNumber)){
                Game2D.objectsFrame.image(picture.getImage(), i*picture.getImage().width-gameCamera.getActualPosition().x*relativeMovementVelocity, leftUpperY - gameCamera.getActualPosition().y, picture.getImage().width, picture.getImage().height,
                        (int)(picture.getImage().width-(gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)-(i*picture.getImage().width-gameCamera.getActualPosition().x*relativeMovementVelocity))), 0, picture.getImage().width, picture.getImage().height);

            }
            */
            Program.objectsFrame.image(picture.getImage(), i*picture.getImage().width-gameCamera.getActualPosition().x* relativeMovementVelocity, leftUpperY - (gameCamera.getActualPosition().y));

            //else Game2D.objectsFrame.image(picture.getImage(), i*picture.getImage().width-gameCamera.getActualPosition().x*relativeMovementVelocity, leftUpperY - (gameCamera.getActualPosition().y));

            //final int lowerLineThickness = (int)(gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-(leftUpperY+picture.getImage().height - (gameCamera.getActualPosition().y/gameCamera.getScale())));
            //
            final int lowerLineThickness = (int)(2*gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-(leftUpperY+picture.getImage().height)-gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE));
            //if (lowerLineThickness > 0) Game2D.objectsFrame.image(lowerLine, i*picture.getImage().width-gameCamera.getActualPosition().x*relativeMovementVelocity,leftUpperY+picture.getImage().height-(gameCamera.getActualPosition().y), picture.getImage().width, lowerLineThickness);
            //System.out.println("Lower line: " + lowerLineThickness + "; Board: " + (gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE) + "; picture side: " + (leftUpperY+picture.getImage().height)));
            if (lowerLineThickness > 0) {
                //System.out.println("Draw lower line with height: " + lowerLineThickness);
                Program.objectsFrame.image(picture.getImage(), i*picture.getImage().width-gameCamera.getActualPosition().x* relativeMovementVelocity,leftUpperY+picture.getImage().height-(gameCamera.getActualPosition().y), picture.getImage().width, lowerLineThickness, 0, picture.getImage().height-1, picture.getImage().width, picture.getImage().height);
            }

            final int upperScreenBoard = (int) gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
            final int upperLineThickness = 0 - upperScreenBoard;
            if (upperLineThickness > 0) {
                //System.out.println("Draw upper line with height: " + upperLineThickness);
                Program.objectsFrame.image(upperLine, i*picture.getImage().width-gameCamera.getActualPosition().x* relativeMovementVelocity, leftUpperY-upperLineThickness - gameCamera.getActualPosition().y, picture.getImage().width, upperLineThickness);
            }
        }
        Program.objectsFrame.popMatrix();
        Program.objectsFrame.imageMode(PConstants.CENTER);
        Program.objectsFrame.popStyle();
        Program.objectsFrame.endDraw();
    }

    private void setDebugTint(int i) {
        if (i == 0) Program.objectsFrame.noTint();
        else if (i == 1) Program.objectsFrame.tint(255,0,0,125);
        else if (i == 2) Program.objectsFrame.tint(0,0,255,125);
        else if (i == 3) Program.objectsFrame.tint(0,255,0,125);
    }

    /*
    Game2D.objectsFrame.beginDraw();
        Game2D.objectsFrame.pushStyle();
        Game2D.objectsFrame.imageMode(PConstants.CORNER);
        Game2D.objectsFrame.pushMatrix();
        int startBackgroundNumber = PApplet.floor((gameCamera.getActualPosition().x*relativeMovementVelocity-
                                                        (float)Game2D.objectsFrame.width/2*gameCamera.getScale())/(float) picture.getImage().width);
        int startX = (int) (startBackgroundNumber*picture.getImage().width-(((gameCamera.getActualPosition().x*relativeMovementVelocity-(float)Game2D.objectsFrame.width/2*gameCamera.getScale())%(float) picture.getImage().width)));

        Game2D.objectsFrame.translate(startX, 0);
        int maxBackgroundsNumber = PApplet.ceil(Game2D.objectsFrame.width/picture.getImage().width);
        if (maxBackgroundsNumber == 1) maxBackgroundsNumber = 2;
        byte startScreenNumber = 0;
        byte endScreenNumber = (byte)maxBackgroundsNumber;

        //if ((startX+picture.getImage().width)<gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) startScreenNumber = 1;
        //if ((startX+picture.getImage().width*2)>gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)) endScreenNumber = 2;
        //System.out.println("Start: " + startScreenNumber + " End: " + endScreenNumber + "; Right side of left background: " + (startX+picture.getImage().width) + "; Left screen side: " + gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE) + "Left side of right background: " + (startX+picture.getImage().width*2) + "; Right screen side: " + gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE) + "; Start X: " + startX);
        //System.out.println("endScreenNumber" + endScreenNumber);
        int additionalDistanceToFirstBackground = 0;
        for (byte i = startScreenNumber; i < endScreenNumber; i++) {
            //setDebugTint(i);
            if (i == 0){

            }
            Game2D.objectsFrame.image(picture.getImage(), i*picture.getImage().width, leftUpperY - (gameCamera.getActualPosition().y));
            //Game2D.objectsFrame.noTint();
            final int lowerLineThickness = (int)(gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-
                                                (leftUpperY+picture.getImage().height - (gameCamera.getActualPosition().y/gameCamera.getScale())));
            if (lowerLineThickness > 0) Game2D.objectsFrame.image(lowerLine, i*picture.getImage().width,leftUpperY+picture.getImage().height-(gameCamera.getActualPosition().y), picture.getImage().width, lowerLineThickness);
            //System.out.println("Thickness: " +lowerLineThickness + "; upper side: " + upperScreenBoard + "; upperBackgroundSideOnScreen: " + upperBackgroundSideOnScreen);
            //System.out.println("Lower Thickness: " +lowerLineThickness);


            final int upperScreenBoard = (int) gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
            final int upperBackgroundSideOnScreen = (int)(leftUpperY - (gameCamera.getActualPosition().y - Game2D.objectsFrame.height/2));
            final int upperLineThickness = 0 - upperScreenBoard;
            //System.out.println("Thickness: " +upperLineThickness + "; upper side: " + upperScreenBoard + "; upperBackgroundSideOnScreen: " + upperBackgroundSideOnScreen);
            if (upperLineThickness > 0) {
                Game2D.objectsFrame.image(upperLine, i*picture.getImage().width, leftUpperY-upperLineThickness - gameCamera.getActualPosition().y, picture.getImage().width, upperLineThickness);
                Game2D.objectsFrame.noTint();
            }
        }
        Game2D.objectsFrame.popMatrix();
        Game2D.objectsFrame.imageMode(PConstants.CENTER);
        Game2D.objectsFrame.popStyle();
        Game2D.objectsFrame.endDraw();

    */

    /*
    private void drawDynamicBackground(GameCamera gameCamera) {
        private void drawDynamicBackground(GameCamera gameCamera) {
        Game2D.objectsFrame.beginDraw();
        Game2D.objectsFrame.pushStyle();
        Game2D.objectsFrame.imageMode(PConstants.CORNER);
        Game2D.objectsFrame.pushMatrix();
        int startBackgroundNumber = PApplet.floor((gameCamera.getActualPosition().x*relativeMovementVelocity-(float)Game2D.objectsFrame.width/2*gameCamera.getScale())/(float) picture.getImage().width);
        int startX = (int) (startBackgroundNumber*picture.getImage().width-(((gameCamera.getActualPosition().x*relativeMovementVelocity-(float)Game2D.objectsFrame.width/2*gameCamera.getScale())%(float) picture.getImage().width)));

        Game2D.objectsFrame.translate(startX, 0);
        byte startScreenNumber = 0;
        byte endScreenNumber = 4;
        //if ((startX+picture.getImage().width)<gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)) startScreenNumber = 1;
        //if ((startX+picture.getImage().width*2)>gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)) endScreenNumber = 2;
        System.out.println("Start: " + startScreenNumber + " End: " + endScreenNumber + "; Right side of left background: " + (startX+picture.getImage().width) + "; Left screen side: " + gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE) + "Left side of right background: " + (startX+picture.getImage().width*2) + "; Right screen side: " + gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE) + "; Start X: " + startX);
        for (byte i = startScreenNumber; i < endScreenNumber; i++) {
            setDebugTint(i);
            Game2D.objectsFrame.image(picture.getImage(), i*picture.getImage().width, leftUpperY - (gameCamera.getActualPosition().y));
            Game2D.objectsFrame.noTint();
            final int lowerLineThickness = (int)(gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-
                                                (leftUpperY+picture.getImage().height - (gameCamera.getActualPosition().y/gameCamera.getScale())));
            if (lowerLineThickness > 0) Game2D.objectsFrame.image(lowerLine, i*picture.getImage().width,leftUpperY+picture.getImage().height-(gameCamera.getActualPosition().y), picture.getImage().width, lowerLineThickness);
            //System.out.println("Thickness: " +lowerLineThickness + "; upper side: " + upperScreenBoard + "; upperBackgroundSideOnScreen: " + upperBackgroundSideOnScreen);
            //System.out.println("Lower Thickness: " +lowerLineThickness);


            final int upperScreenBoard = (int) gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
            final int upperBackgroundSideOnScreen = (int)(leftUpperY - (gameCamera.getActualPosition().y - Game2D.objectsFrame.height/2));
            final int upperLineThickness = 0 - upperScreenBoard;
            //System.out.println("Thickness: " +upperLineThickness + "; upper side: " + upperScreenBoard + "; upperBackgroundSideOnScreen: " + upperBackgroundSideOnScreen);
            if (upperLineThickness > 0) {
                Game2D.objectsFrame.image(upperLine, i*picture.getImage().width, leftUpperY-upperLineThickness - gameCamera.getActualPosition().y, picture.getImage().width, upperLineThickness);
                Game2D.objectsFrame.noTint();
            }
        }
        Game2D.objectsFrame.popMatrix();
        Game2D.objectsFrame.imageMode(PConstants.CENTER);
        Game2D.objectsFrame.popStyle();
        Game2D.objectsFrame.endDraw();
    */

    private void drawStaticBackground(GameCamera gameCamera){
        Program.objectsFrame.beginDraw();
        Program.objectsFrame.pushStyle();
        //Game2D.objectsFrame.imageMode(PConstants.CORNER);
        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.imageMode(PConstants.CORNER);
        Program.objectsFrame.image(picture.getImage(), leftUpperX, leftUpperY - gameCamera.getActualPosition().y);
        if ((leftUpperY - gameCamera.getActualPosition().y+picture.getImage().height) > gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)) {
            final int lowerLineThickness = (int)(gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)-(leftUpperY+picture.getImage().height)+1);
            Program.objectsFrame.image(lowerLine, leftUpperX, leftUpperY - gameCamera.getActualPosition().y + picture.getImage().height, picture.getImage().width, lowerLineThickness);
            //System.out.println("Lower side is shown:" + lowerLineThickness);
        }
        else {
            //System.out.println("No lower side");
        }
        //Game2D.objectsFrame.image(lowerLine, startBackgroundXPosition+picture.getImage().width, leftUpperY - gameCamera.getActualPosition().y+picture.getImage().height);

        //
        Program.objectsFrame.popMatrix();
        Program.objectsFrame.imageMode(PConstants.CENTER);
        Program.objectsFrame.popStyle();
        Program.objectsFrame.endDraw();
    }

    @Override
    public String getStringData() {
        return null;
    }

    public byte getType(){
        return SCROLLABLE_PICTURE_BACKGROUND;
    }


    public void clearGraphic() {
        try {
            Object cacheInBackgrounds = Program.backgroundFrame.getCache(picture.image);
            if (cacheInBackgrounds instanceof Texture) ((Texture) cacheInBackgrounds).disposeSourceBuffer();
            Program.backgroundFrame.removeCache(picture.image);
            Program.engine.g.removeCache(picture.image);
            if (Program.debug){
                System.out.println("Object " + " was deleted from background: " + ", " + (Program.backgroundFrame.getCache(picture.image)!= null) + ", " + (Program.engine.g.getCache(picture.image)!= null));
            }
        }
        catch (Exception e){
            System.out.println("PImage is not in memory");
            e.printStackTrace();
        }
    }
}
