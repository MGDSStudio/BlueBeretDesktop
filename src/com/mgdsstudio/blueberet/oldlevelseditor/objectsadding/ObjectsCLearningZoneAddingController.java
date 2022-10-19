package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public class ObjectsCLearningZoneAddingController extends ObjectOnMapAddingController{

    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, ObjectsClearingZone objectsClearingZone){
        /*
        Vec2 nearestPointPos = gameCamera.getOnMapPositionForPointForEditor(new Vec2(Game2D.engine.mouseX, Game2D.engine.mouseY));
        gameObject.setNewPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        ObjectData objectData = new ObjectData(gameObject);
        addTextDataToRoundFile(objectData.getStringSaveData(), false);
        newObjectCanBeAdded = false;
        levelsEditorProcess.getGameRound().addNewGameObject(gameObject);
        Game2D.engine.println("NPC was placed");
        */
    }
}
