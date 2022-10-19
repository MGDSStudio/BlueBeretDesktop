package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.Figure;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.Point;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

public class PointAddingController extends ObjectOnMapAddingController {
    protected boolean canPointLaysOnPreviousPointLine;
    private int figureType = -1;
    private final PVector mutMousePos = new PVector(0,0);

    public PointAddingController(boolean magnetingTo){
        this.magnetingTo = magnetingTo;
        figureType = Figure.RECTANGULAR_SHAPE;
    }

    /*
    public PointAddingController(){
        figureType = Figure.RECTANGULAR_SHAPE;
    }*/

    public PointAddingController(int figureType){
        this.figureType = figureType;
    }

    @Override
    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject){
        PVector nearestPointPos;
        mutMousePos.x = Program.engine.mouseX;
        mutMousePos.y = Program.engine.mouseY;
        if (magnetingTo == TO_VERTEX){
            nearestPointPos = LevelsEditorProcess.getNearestPointOnGrid(gameCamera, mutMousePos);
        }
        else{
            nearestPointPos = LevelsEditorProcess.getNearestCellCenterOnGrid(gameCamera, mutMousePos);
            nearestPointPos.x-= Editor2D.gridSpacing/2;
            nearestPointPos.y-= Editor2D.gridSpacing/2;
        }
        Point newPoint = new Point(nearestPointPos);
        boolean addingBlocked = canBeAdded(newPoint, levelsEditorProcess);
        if (!addingBlocked) {
            levelsEditorProcess.pointsOnMap.add(newPoint);
            lastAddedPointPosition = new Vec2(newPoint.getPosition().x, newPoint.getPosition().y);
        }
        else {
            System.out.println("This point can not be added. It intersects with an another");
        }
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        newObjectCanBeAdded = false;
        Program.engine.println("New point was added. Nearest point: " + nearestPointPos);
    }

    private boolean canBeAdded(Point newPoint, LevelsEditorProcess levelsEditorProcess){
        boolean addingBlocked = false;
        if (levelsEditorProcess.pointsOnMap.size()>0){
            Point prevPoint = levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size()-1);
            if (figureType == Figure.RECTANGULAR_SHAPE){
                if (prevPoint.getPosition().x == newPoint.getPosition().x || prevPoint.getPosition().y == newPoint.getPosition().y) {
                    addingBlocked = true;
                }
            }
            else if (figureType ==  Figure.CIRCLE_SHAPE){
                if (prevPoint.getPosition().x == newPoint.getPosition().x && prevPoint.getPosition().y == newPoint.getPosition().y){
                    addingBlocked = true;
                }
            }
            else if (figureType == Figure.TRIANGLE_SHAPE) {
                if (levelsEditorProcess.pointsOnMap.size()==1){
                    if (prevPoint.getPosition().x == newPoint.getPosition().x && prevPoint.getPosition().y == newPoint.getPosition().y) {
                        addingBlocked = true;
                    }
                }
                else if (levelsEditorProcess.pointsOnMap.size()==2){
                    boolean sameX = false;
                    boolean sameY = false;
                    Point firstPoint = levelsEditorProcess.pointsOnMap.get(0);
                    Point secondPoint = levelsEditorProcess.pointsOnMap.get(1);
                    if ((int)firstPoint.getPosition().x == (int)secondPoint.getPosition().x){
                        sameY = true;
                    }
                    if ((int)firstPoint.getPosition().y == (int)secondPoint.getPosition().y){
                        sameX = true;
                    }
                    if (sameX == false && sameY == false){
                        addingBlocked = false;
                        System.out.println("First two points doesn't lay on a same line");
                    }
                    else {
                        if (sameX){
                            System.out.println("First two points lay on a same horizontal line");
                            if ((int)firstPoint.getPosition().y == newPoint.getPosition().y){
                                System.out.println("New point lays also on the same horizontal line");
                                addingBlocked = true;
                            }
                        }
                        else if (sameY){
                            System.out.println("First two points lay on a same vertical line");
                            if ((int)firstPoint.getPosition().x == newPoint.getPosition().x){
                                System.out.println("New point lays also on the same vertical line");
                                addingBlocked = true;
                            }
                        }
                    }
                }
                else {
                    System.out.println("There are too many points on the map zone");
                }
            }
            return addingBlocked;
        }
        else return addingBlocked;
    }

    public Vec2 getLastAddedPointPosition(){
        return lastAddedPointPosition;
    }


    /*
    @Override
    protected void objectMustBeAdded(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject nothing){
        PVector nearestPointPos = LevelsEditorProcess.getNearestPoint(gameCamera, new PVector(Game2D.engine.mouseX, Game2D.engine.mouseY));
        Game2D.engine.println("nearestPoint: " + nearestPointPos + " mouse on screen: " + new PVector(Game2D.engine.mouseX, Game2D.engine.mouseY));
        Point newPoint = new Point(nearestPointPos);
        boolean isLine = false;
        if (levelsEditorProcess.pointsOnMap.size()>0){
            for (Point point : levelsEditorProcess.pointsOnMap){
                if (point.getPosition().x == newPoint.getPosition().x || point.getPosition().y == newPoint.getPosition().y){
                    isLine = true;
                }
            }
        }
        if (!isLine) levelsEditorProcess.pointsOnMap.add(newPoint);
        timer.stop();
        timer = null;
        Game2D.engine.println("New point was added");
    }
    */

    public void deletePoints(LevelsEditorProcess levelsEditorProcess) {
        levelsEditorProcess.pointsOnMap.clear();
    }
}
