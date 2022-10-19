package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.DebugGraphic;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class PatrolAreaDevider {
    private final Flag patrolArea;
    private final int minElementsAlongAxis;

    private final Coordinate [] centers;
    static final boolean USE_FULL_AREA = false;
    static final boolean USE_ONLY_PERIMETER = true;
    private final float criticalDistToSamePoint;
    private int cellHeight;

    public PatrolAreaDevider(Flag patrolArea, int minElementsAlongAxis, boolean usingType) {
        this.patrolArea = patrolArea;
        this.minElementsAlongAxis = minElementsAlongAxis;
        int x = minElementsAlongAxis;
        int y = minElementsAlongAxis;
        if (patrolArea.getWidth()>patrolArea.getHeight()){
            x = calculateCentersAlongAxisNumber();
        }
        else if (patrolArea.getWidth()<patrolArea.getHeight()){
            y = calculateCentersAlongAxisNumber();
        }
        if (usingType == USE_ONLY_PERIMETER) {
            centers = new Coordinate[x * 2 + y * 2 - 4];
            createKeyPointsForPerimeter(patrolArea, x, y);
        }
        else {
            centers = new Coordinate[x * y];
            createKeyPointsForFullArea(patrolArea, x, y);
        }
        criticalDistToSamePoint = patrolArea.getWidth()/(x*3f);
        cellHeight = (int) (patrolArea.getHeight()/y);
    }

    Coordinate generateRandomPoint(float actualX, float actualY, GameRound gameRound){
        boolean founded = false;
        Coordinate newPoint = null;
        if (Program.debug && Program.OS == Program.DESKTOP) {
            for (int i = 0; i < centers.length; i++) {
                DebugGraphic debugGraphic = new DebugGraphic(DebugGraphic.CROSS, new Vec2(centers[i].x, centers[i].y));
                gameRound.addDebugGraphic(debugGraphic);
                debugGraphic.setTimeToShow(1000);
            }
        }
        while (!founded){
            int random = (int) Program.engine.random(centers.length);
            float generatedX = centers[random].x;
            float generatedY = centers[random].y;
            if (PApplet.dist(generatedX, generatedY, actualX,actualY) >= criticalDistToSamePoint){
                founded = true;
                newPoint = new Coordinate(generatedX, generatedY);
                //System.out.println("Generated point: " + (int)generatedX + "x" + (int)generatedY);
            }
            else {
                System.out.println("Generated the same point");
            }
        }
        return newPoint;
    }


    private void createKeyPointsForPerimeter(Flag patrolArea, int x, int y) {
        float width = patrolArea.getWidth()/ x;
        float height = patrolArea.getWidth()/ y;
        int number = 0;
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                if (i == 0 || j == 0 || i == (x-1) || j == (y-1)) {
                    float leftX = patrolArea.getPosition().x-patrolArea.getWidth()/2;
                    float upperY = patrolArea.getPosition().y-patrolArea.getHeight()/2;
                    Coordinate center = new Coordinate(leftX +i * width - width / 2f, upperY+j * height - height / 2f);
                    centers[number] = center;
                    number++;
                }
            }
        }
    }
    private void createKeyPointsForFullArea(Flag patrolArea, int x, int y) {
        float width = patrolArea.getWidth()/ x;
        float height = patrolArea.getWidth()/ y;
        int number = 0;
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                Coordinate center = new Coordinate(i*width-width/2f, j*height-height/2f);
                centers[number] = center;
                number++;
            }
        }
    }

    private int calculateCentersAlongAxisNumber() {
        float relationshipX = (float)patrolArea.getWidth()/(float)patrolArea.getHeight();
        float relationshipY = 1f/relationshipX;
        if (relationshipX>relationshipY){
            return PApplet.ceil(minElementsAlongAxis*relationshipX);
        }
        else if (relationshipY>relationshipX){
            return PApplet.ceil(minElementsAlongAxis*relationshipY);
        }
        else return minElementsAlongAxis;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    void draw(GameCamera gameCamera){

    }
}
