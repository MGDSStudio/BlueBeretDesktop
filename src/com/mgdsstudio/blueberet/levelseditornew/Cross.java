package com.mgdsstudio.blueberet.levelseditornew;

import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

class Cross {
    private boolean visible = true;
    private Vec2 theoreticalCoordinate, realCoordinate, objectCoordinate;
    private final Vec2 mutPos;
    private final LevelsEditor levelsEditor;
    private final int crossWidth;
    private final int linesThickness;

    private float mapZoneCenterX, mapZoneCenterY;

    public Cross(LevelsEditor levelsEditor) {
        theoreticalCoordinate = new Vec2(0,0);
        realCoordinate = new Vec2(0,0);
        objectCoordinate = new Vec2();
        mutPos = new Vec2();
        this.levelsEditor = levelsEditor;
        crossWidth = levelsEditor.gameMainController.getEngine().width/15;
        linesThickness = (int) (6f*((float)levelsEditor.gameMainController.getEngine().width)/((float)(Program.DEBUG_DISPLAY_WIDTH)));
        mapZoneCenterX = levelsEditor.getFrame().getLeftX()*1+levelsEditor.getFrame().getWidth()/2;
        mapZoneCenterY = levelsEditor.getFrame().getUpperY()*1+levelsEditor.getFrame().getHeight()/2;

        /*
         mapZoneCenterX = levelsEditor.getFrame().getLeftX()*2+levelsEditor.getFrame().getWidth()/2;
        mapZoneCenterY = levelsEditor.getFrame().getUpperY()*2+levelsEditor.getFrame().getHeight()/2;
         */
    }

    private void update(GameCamera gameCamera){
        if (visible){
            //mutPos.x = gameCamera.getActualPosition().x;
            //mutPos.y = gameCamera.getActualPosition().y;
            //realCoordinate = gameCamera.getOnMapPositionForPointForEditor(mutPos);
            //realCoordinate.y = gameCamera.getActualPosition().y;



            /*
            theoreticalCoordinate.x = realCoordinate.x;
            theoreticalCoordinate.y = realCoordinate.y;
             */
            Vec2 screenCenter = gameCamera.getActualPositionForScreenCenterInEditor();
            theoreticalCoordinate.x = screenCenter.x;
            theoreticalCoordinate.y = screenCenter.y;
            objectCoordinate = gameCamera.getOnMapPositionForPointForEditor(theoreticalCoordinate);


            //realCoordinate.y-= (levelsEditor.getMapZone().zoneHeight/2);
            mutPos.x = 11368;
            mutPos.y =1270;

            //realCoordinate.x = mutPos.x;
            //realCoordinate.y = mutPos.y;
            realCoordinate = theoreticalCoordinate;
            objectCoordinate.x = mapZoneCenterX;
            objectCoordinate.y = mapZoneCenterY;
            /*
            objectCoordinate.x = mapZoneCenterX;
            objectCoordinate.y = mapZoneCenterY;
             */
            //System.out.println("Theoretical: " + theoreticalCoordinate + " but nearest at: " + getNearestPointOnGrid(gameCamera, objectCoordinate));

            realCoordinate = objectCoordinate;
            //realCoordinate = getNearestPointOnGrid(gameCamera, objectCoordinate);
            //realCoordinate.x = realCoordinate.x;
            //realCoordinate.y = realCoordinate.y;
        }
    }

    private Vec2 getNearestPointOnGrid(GameCamera gameCamera, Vec2 point){
        if (levelsEditor.isPointOnMapArea(point.x, point.y)){

            float distanceToCenterLineX = (point.x - Program.engine.width/2);
            float distanceToCenterLineY = (point.y - Program.engine.height/2);
            point.x = ((gameCamera.getActualPosition().x + distanceToCenterLineX / gameCamera.getScale()));
            point.y = ((gameCamera.getActualPosition().y + distanceToCenterLineY / gameCamera.getScale()));
            if (Program.engine.frameCount %100 == 0) System.out.println("This function must be adjusted to object frame dimensions");
            float positivDeltaX = point.x% Editor2D.gridSpacing;
            float positivDeltaY = point.y%Editor2D.gridSpacing;
            float negativDeltaX = Editor2D.gridSpacing-positivDeltaX;
            float negativDeltaY = Editor2D.gridSpacing-positivDeltaY;

            if (positivDeltaX>negativDeltaX) point.x+=negativDeltaX;
            else point.x-=positivDeltaX   ;
            if (positivDeltaY>negativDeltaY) point.y+=negativDeltaY;
            else point.y-=positivDeltaY ;

            return point;
        }
        else {
            if (Program.debug) Program.engine.println("Point is not on the map zone!");
            return point;
        }
    }

    public void draw(GameCamera gameCamera, PGraphics graphic){
        if (visible){
            update(gameCamera);
            drawRealPos(gameCamera,graphic);

            //if (Program.debug) drawTheoreticalPos(gameCamera, graphic);
        }
    }

    private void drawRealPos(GameCamera gameCamera, PGraphics graphic) {
        graphic.pushMatrix();
        graphic.scale(Program.OBJECT_FRAME_SCALE);
        graphic.pushStyle();
        graphic.noFill();
        graphic.translate(realCoordinate.x - gameCamera.getActualXPositionRelativeToCenter(), realCoordinate.y - gameCamera.getActualYPositionRelativeToCenter());
        //System.out.println("Drawn real at: " + (int)realCoordinate.x + "x" +  (int)realCoordinate.y + " on screen " +  (int)(realCoordinate.x - gameCamera.getActualXPositionRelativeToCenter()) +"x"+  (int)(realCoordinate.y - gameCamera.getActualYPositionRelativeToCenter()));
        graphic.strokeWeight(linesThickness);
        graphic.stroke(0,255,0);
        graphic.line(-crossWidth/2, 0,crossWidth/2,0);
        graphic.line(0, -crossWidth/2, 0,crossWidth/2);

        graphic.popStyle();
        graphic.popMatrix();
    }

    private void drawTheoreticalPos(GameCamera gameCamera, PGraphics graphic) {

        graphic.pushMatrix();
        graphic.scale(Program.OBJECT_FRAME_SCALE);
        graphic.pushStyle();
        graphic.noFill();
        graphic.translate(theoreticalCoordinate.x - gameCamera.getActualXPositionRelativeToCenter(), theoreticalCoordinate.y - gameCamera.getActualYPositionRelativeToCenter());
        //System.out.println("Drawn at: " + (int)realCoordinate.x + "x" +  (int)realCoordinate.y + " on screen " +  (int)(realCoordinate.x - gameCamera.getActualXPositionRelativeToCenter()) +"x"+  (int)(realCoordinate.y - gameCamera.getActualYPositionRelativeToCenter()));
        graphic.strokeWeight(linesThickness);
        graphic.stroke(255,0,0);
        graphic.line(-crossWidth/2, 0,crossWidth/2,0);
        graphic.line(0, -crossWidth/2, 0,crossWidth/2);

        graphic.popStyle();
        graphic.popMatrix();

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
