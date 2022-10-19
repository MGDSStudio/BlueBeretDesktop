package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.oldlevelseditor.MapZone;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public abstract class SubmenuWithTwoRectZones extends SubmenuAction{
    protected PointAddingController pointsAddingController;
    protected RectangularElementAdding secondZoneAdding, firstZoneAdding;
    protected final static byte FIRST_POINT_ADDING = 1;
    protected final static byte SECOND_POINT_ADDING = 2;
    protected final static byte THIRD_POINT_ADDING = 3;
    public final static byte FOURTH_POINT_ADDING = 4;

    public SubmenuWithTwoRectZones(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
    }

    protected void createFigure(RectangularElementAdding rectZoneAdding, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (rectZoneAdding.equals(firstZoneAdding)){
            addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
            firstZoneAdding = null;
        }
        else  if (rectZoneAdding.equals(secondZoneAdding)){
            addRectFigureOnMapZoneAndSaveData((byte)2, levelsEditorProcess, objectData);
            secondZoneAdding = null;
        }
        else{
            System.out.println("It is not equals ");
        }
        makePauseToNextOperation();

    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= FOURTH_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size()<4){
                pointsAddingController.update(gameCamera, levelsEditorProcess);
                if (pointsAddingController.canBeNewObjectAdded()){
                    System.out.println("New point can be added");
                    pointsAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                    pointsAddingController.endAdding();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 0 && Editor2D.localStatement != FIRST_POINT_ADDING) Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                if (levelsEditorProcess.pointsOnMap.size() == 1 && Editor2D.localStatement != SECOND_POINT_ADDING) {
                    firstZoneAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    Editor2D.setNewLocalStatement(SECOND_POINT_ADDING);
                    System.out.println("First point for first zone placed");
                }
                else if (levelsEditorProcess.pointsOnMap.size() == 2 && Editor2D.localStatement != THIRD_POINT_ADDING) {
                    firstZoneAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    Editor2D.setNewLocalStatement(THIRD_POINT_ADDING);
                    System.out.println("Second point for first zone placed");
                    createFigure(firstZoneAdding, levelsEditorProcess, objectData);
                }
                if (levelsEditorProcess.pointsOnMap.size() == 3 && Editor2D.localStatement != FOURTH_POINT_ADDING) {
                    secondZoneAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(2).getPosition());
                    Editor2D.setNewLocalStatement(FOURTH_POINT_ADDING);
                    System.out.println("First point for second zone placed");
                }
                if (levelsEditorProcess.pointsOnMap.size() == 4) {
                    secondZoneAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(3).getPosition());
                    Editor2D.setNewLocalStatement((byte) (FOURTH_POINT_ADDING+1));
                    System.out.println("Last point for second zone placed");
                    createFigure(secondZoneAdding, levelsEditorProcess, objectData);
                }
            }
        }
    }

}
