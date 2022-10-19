package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class Grid implements IDrawable {
    //private int cellDimension;
    private int visibleWidth, visibleHeight;
    private final int gridNormalColor = Program.engine.color(5, 55);
    private final int gridFifthElementColor = Program.engine.color(150,25,25, 100);
    private final int gridNormalStrokeWeight = (int)(Program.engine.width/166.5f);
    private final int gridFifthElementStrokeWeight = gridNormalStrokeWeight+2;
    private final Vec2 mutPoint = new Vec2(0,0);

    public Grid(){
        init();
    }

    private void init() {
        visibleWidth= (Editor2D.zoneWidth);
        visibleHeight = (Editor2D.zoneHeight);
    }

    public void update(GameRound gameRound){

    }

    @Override
    public void draw(GameCamera gameCamera) {
        if (Editor2D.showGrid) {
            drawGrid(gameCamera);
        }
    }

    private void drawGrid(GameCamera gameCamera){




        mutPoint.x = ((gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)));
        mutPoint.y = ((gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE)));


        /*
        mutPoint.x = (gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE));
        mutPoint.x = gameCamera.getActualPosition().x-(Program.objectsFrame.width/2f*gameCamera.getScale());;
                //mutPoint.y = (gameCamera.getActualPosition().y)-Program.objectsFrame.height/(2f/gameCamera.getScale());
        mutPoint.y = ((gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE)));*/

        int positivDeltaX = (int)mutPoint.x% Editor2D.gridSpacing;
        int positivDeltaY = (int)mutPoint.y%Editor2D.gridSpacing;
        int negativDeltaX = Editor2D.gridSpacing-positivDeltaX;
        int negativDeltaY = Editor2D.gridSpacing-positivDeltaY;
        if (positivDeltaX>negativDeltaX) mutPoint.x+=negativDeltaX;
        else mutPoint.x-=positivDeltaX;
        if (positivDeltaY>negativDeltaY) mutPoint.y+=negativDeltaY;
        else mutPoint.y-=positivDeltaY ;
        visibleWidth = (int) ((Editor2D.zoneWidth)+(Editor2D.zoneWidth)*(GameCamera.maxScale-gameCamera.getScale()));
        visibleHeight = (int) ((Editor2D.zoneHeight)+(Editor2D.zoneHeight)*(GameCamera.maxScale-gameCamera.getScale()));

        int linesAlongX = PApplet.ceil(visibleWidth/(Program.OBJECT_FRAME_SCALE*Editor2D.gridSpacing*gameCamera.getScale()));
        int linesAlongY = PApplet.ceil(visibleHeight/(Program.OBJECT_FRAME_SCALE*Editor2D.gridSpacing*gameCamera.getScale()));

        Program.objectsFrame.pushMatrix();
        Program.objectsFrame.pushStyle();
        Program.objectsFrame.scale(Program.OBJECT_FRAME_SCALE);
        Program.objectsFrame.translate(mutPoint.x - gameCamera.getActualXPositionRelativeToCenter(), mutPoint.y - gameCamera.getActualYPositionRelativeToCenter());
        int absoluteXNumber = (PApplet.floor(mutPoint.x/Editor2D.gridSpacing))+0;
        int absoluteYNumber = (PApplet.floor(mutPoint.y/Editor2D.gridSpacing))+0;
        //System.out.println("Lines " + linesAlongX);
        for (int i = 0; i < linesAlongX; i++){
            if ((absoluteXNumber+i)%5 == 0) {
                Program.objectsFrame.strokeWeight(gridFifthElementStrokeWeight);
                Program.objectsFrame.stroke(gridFifthElementColor);
            }
            else {
                Program.objectsFrame.strokeWeight(gridNormalStrokeWeight);
                Program.objectsFrame.stroke(gridNormalColor);
            }
            Program.objectsFrame.line((i+1)*((Editor2D.gridSpacing)), 0, (i+1)*(Editor2D.gridSpacing), (Program.objectsFrame.height/gameCamera.getScale()/Program.OBJECT_FRAME_SCALE));
        }
        for (int i = 0; i < linesAlongY; i++){
            if ((absoluteYNumber+i)%5 == 0) {
                Program.objectsFrame.strokeWeight(gridFifthElementStrokeWeight);
                Program.objectsFrame.stroke(gridFifthElementColor);
            }
            else {
                Program.objectsFrame.strokeWeight(gridNormalStrokeWeight);
                Program.objectsFrame.stroke(gridNormalColor);
            }
            Program.objectsFrame.line(0, 0+(i)*((Editor2D.gridSpacing)), (Program.objectsFrame.width/gameCamera.getScale()/Program.OBJECT_FRAME_SCALE), 0+(i)*(Editor2D.gridSpacing));
        }
        Program.objectsFrame.popStyle();
        Program.objectsFrame.popMatrix();

    }
}
