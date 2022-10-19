package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.SingleFlagZoneAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public abstract class OneZoneAddingAction extends SubmenuAction{
    protected PointAddingController pointAddingController;
    protected RectangularElementAdding rectangularElementAdding;

    public OneZoneAddingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        pointAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
    }

    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointAddingController.draw(gameCamera, levelsEditorProcess);
    }

    protected void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        rectangularElementAdding = null;
        levelsEditorProcess.pointsOnMap.clear();
        levelsEditorProcess.figures.clear();
        Editor2D.localStatement = RectangularElementAdding.FIRST_POINT_ADDING;
        makePauseToNextOperation();
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= RectangularElementAdding.SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && Editor2D.localStatement != RectangularElementAdding.FIRST_POINT_ADDING)
                    rectangularElementAdding.setNextStatement();

                if (levelsEditorProcess.pointsOnMap.size() == 1 && Editor2D.localStatement != RectangularElementAdding.SECOND_POINT_ADDING) {
                    rectangularElementAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    rectangularElementAdding.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && Editor2D.localStatement < SingleFlagZoneAdding.COMPLETED) {
                    rectangularElementAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    rectangularElementAdding.setNextStatement();
                    Editor2D.localStatementChanged = true;
                    if (levelsEditorProcess.getGameRound() == null) System.out.println("Game round is null");
                    addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
                }
            }
        }
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        System.out.println("This function must be overriden");
    }


    @Override
    public byte getEndValue(){
        return RectangularElementAdding.END;
    }

}
