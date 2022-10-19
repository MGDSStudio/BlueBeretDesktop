package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.loading.DeleteStringsMaster;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.loading.ObjectData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

public class PlayerAddingController extends ObjectOnMapAddingController{


    public PlayerAddingController() {
        //magnetingTo = TO_CELL_CENTER;
    }

    public void addObjectOnNewPlace(GameCamera gameCamera, GameObject gameObject, GameObjectDataForStoreInEditor objectData){
        Vec2 nearestPointPos = gameCamera.getOnMapPositionForPointForEditor(new Vec2(Program.engine.mouseX, Program.engine.mouseY));
        gameObject.setNewPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        objectData.setPosition(nearestPointPos);
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
        deleteStringsMaster.deleteStringsStartsWith(Soldier.CLASS_NAME, DeleteStringsMaster.DELETE_FIRST_STARTS_WITH);
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        newObjectCanBeAdded = false;
    }

    public void addObjectOnPlace(Vec2 newPlace, GameObject gameObject, GameObjectDataForStoreInEditor objectData){
        gameObject.setNewPosition(newPlace);
        objectData.setPosition(newPlace);
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        newObjectCanBeAdded = false;
    }


    @Override
    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject){
        Vec2 nearestPointPos = gameCamera.getOnMapPositionForPointForEditor(new Vec2(Program.engine.mouseX, Program.engine.mouseY));
        levelsEditorProcess.getGameRound().getPlayer().setNewPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        ObjectData objectData = new ObjectData(levelsEditorProcess.getGameRound().getPlayer());
        addTextDataToRoundFile(objectData.getStringSaveData(), false);
        newObjectCanBeAdded = false;
        Program.engine.println("Player was placed on new plase");
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
        deleteStringsMaster.deleteStringsStartsWith(Soldier.CLASS_NAME, DeleteStringsMaster.DELETE_FIRST_STARTS_WITH);
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
        Game2D.engine.println("Player was placed");
    */



        /*
        */
    }


    public void saveDataToFile(Soldier player){
        ObjectData objectData = new ObjectData(player);
        addTextDataToRoundFile(objectData.getStringSaveData(), false);
    }

    public void setOnNewPosition(GameObject soldier, Vec2 position){
        if (position == null) System.out.println("Position is null");
        if (soldier == null) System.out.println("Game object is null");
        soldier.setNewPosition(position);
    }

    public void deletePrevisiosPositionForPlayerInDataFile(){
        DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
        deleteStringsMaster.deleteStringsStartsWith(Soldier.CLASS_NAME, DeleteStringsMaster.DELETE_FIRST_STARTS_WITH);
        newObjectCanBeAdded = false;
    }

    public void end(){
        newObjectCanBeAdded = false;
    }

}
