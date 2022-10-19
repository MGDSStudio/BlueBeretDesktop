package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.loading.ObjectData;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

public class Simple_NPC_AddingController extends ObjectOnMapAddingController{
    public final static byte COMPLETED = 4;
    public final static byte END = COMPLETED;
    public static final byte SETTING_AI_MODEL_FOR_KOOPA = 1;
    public static final byte ADDING_LIFE_LINE_FOR_KOOPA = 2;
    public static final byte START_STATEMENT = 1;
    public static final byte ADDING_LIFE_LINE_FOR_GUMBA = 1;
    public static final byte ADDING_LIFE_LINE_FOR_BOWSER = 1;


    public Simple_NPC_AddingController(){
        allignedWithGrid = false;
    }

    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject){
        Vec2 nearestPointPos = gameCamera.getOnMapPositionForPointForEditor(new Vec2(Program.engine.mouseX, Program.engine.mouseY));
        gameObject.setNewPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        ObjectData objectData = new ObjectData(gameObject);
        addTextDataToRoundFile(objectData.getStringSaveData(), false);
        newObjectCanBeAdded = false;
        levelsEditorProcess.getGameRound().addNewGameObject(gameObject);
        Program.engine.println("NPC was placed");
    }

    public void addNewObject(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, GameObject gameObject, GameObjectDataForStoreInEditor objectData){
        Vec2 nearestPointPos = gameCamera.getOnMapPositionForPointForEditor(new Vec2(Program.engine.mouseX, Program.engine.mouseY));
        gameObject.setNewPosition(new Vec2(nearestPointPos.x, nearestPointPos.y));
        objectData.setPosition(nearestPointPos);
        if (timer!= null) {
            timer.stop();
            timer = null;
        }
        objectData.setPosition(nearestPointPos);
        newObjectCanBeAdded = false;
        Program.engine.println("NPC was placed");
        Editor2D.setNewLocalStatement(END);
    }



}
