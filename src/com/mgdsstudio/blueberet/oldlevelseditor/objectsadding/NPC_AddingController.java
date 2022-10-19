package com.mgdsstudio.blueberet.oldlevelseditor.objectsadding;

import com.mgdsstudio.blueberet.gameobjects.persons.Bowser;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameobjects.persons.Koopa;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.oldlevelseditor.LevelsEditorProcess;
import com.mgdsstudio.blueberet.loading.ObjectData;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

public class NPC_AddingController extends ObjectOnMapAddingController {


    public final static byte COMPLETED = 2;
    public final static byte END = COMPLETED;
    public static final byte SETTING_AI_MODEL_FOR_KOOPA = 1;
    public static final byte ADDING_LIFE_LINE_FOR_KOOPA = 2;

    public NPC_AddingController(){
        allignedWithGrid = false;
    }

    public byte getEndStatementForType(String className){
        if (className == Gumba.CLASS_NAME) return (byte)2;
        else if (className == Bowser.CLASS_NAME) return (byte)2;
        else if (className == Koopa.CLASS_NAME) return (byte)3;
        else return 2;
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
        /*
        if (gameObject.getClass() == Gumba.class) objectData.setClassName(Gumba.CLASS_NAME);
        else if (gameObject.getClass() == Bowser.class) objectData.setClassName(Bowser.CLASS_NAME);
        else if (gameObject.getClass() == Koopa.class) objectData.setClassName(Koopa.CLASS_NAME);
        */
        objectData.setPosition(nearestPointPos);

        //objectData.setLife();
        //addTextDataToRoundFile(objectData.getStringSaveData(), false);
        newObjectCanBeAdded = false;
        //levelsEditorProcess.getGameRound().addNewGameObject(gameObject);
        Program.engine.println("NPC was placed");
        Editor2D.setNewLocalStatement(END);
    }

    //objectData.setClassName("");

}
