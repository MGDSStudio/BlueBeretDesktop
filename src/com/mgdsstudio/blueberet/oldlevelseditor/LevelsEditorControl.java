package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_MovableComboBox;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;

import com.mgdsstudio.blueberet.levelseditornew.IEditorControl;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectWithSetableFormAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RoundCircleAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.*;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.AddingNewSpriteAnimationAction;
import com.mgdsstudio.blueberet.mainpackage.*;
import com.mgdsstudio.blueberet.onscreenactions.OnPinchAction;
import com.mgdsstudio.blueberet.onscreenactions.OnScreenActionType;
import processing.core.PApplet;
import processing.core.PVector;

public class LevelsEditorControl implements IEditorControl {
    private final GameMainController gameMainController;
    ScrollableTabController tabsController;
    GameObjectDataForStoreInEditor objectData;
    SubmenuAction submenuAction;
    private GameCamera editorCamera;
    private LevelsEditorProcess levelsEditorProcess;




    public LevelsEditorControl(androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, GameCamera editorCamera, GameMainController gameMainController){
        this.gameMainController = gameMainController;
        tabsController = new ScrollableTabController(tab);
        objectData = new GameObjectDataForStoreInEditor();
        createNewObjects(levelsEditorProcess.mapZone);
        if (ZONE_MANIPULATIONS_BY_DIRECT_CONTROL) {
            this.editorCamera = editorCamera;
            this.levelsEditorProcess = levelsEditorProcess;
        }
    }

    private void deleteAnotherObjects(LevelsEditorProcess levelsEditorProcess){
        if (Editor2D.getGlobalStatement() == Editor2D.PLACE_PLAYER){
            levelsEditorProcess.pointsOnMap.clear();        //Question
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement >= RectangularElementAdding.TEXTURE_REGION_CHOOSING) levelsEditorProcess.figures.clear();
                else if (levelsEditorProcess.figures.size()>1) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_CIRCLE){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement >= RoundCircleAdding.TEXTURE_REGION_CHOOSING) levelsEditorProcess.figures.clear();
                else if (levelsEditorProcess.figures.size()>1) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_POLYGON){
            if (Editor2D.localStatement <= ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement >= AddingNewRoundPolygon.TEXTURE_REGION_CHOOSING) {
                    System.out.println("Test this string!!!");
                    levelsEditorProcess.figures.clear();
                }
                else if (levelsEditorProcess.figures.size()>1) levelsEditorProcess.figures.clear();
                //System.out.println("Polygon adding menu is created");
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_PIPE){
            if (Editor2D.localStatement <= AddingNewRoundPipeAction.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement >= AddingNewRoundPipeAction.TEXTURE_REGION_CHOOSING) levelsEditorProcess.figures.clear();
                else if (levelsEditorProcess.figures.size()>1) {
                    levelsEditorProcess.figures.clear();
                }
                System.out.println("Cleared");
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_STATIC_SPRITE){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_SPRITE_ANIMATION){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.SELECTION_CANCEL){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
            }
            if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_GUMBA || Editor2D.getGlobalStatement() == Editor2D.PLACE_BOWSER){
            levelsEditorProcess.pointsOnMap.clear();
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_OBJECT_CLEARING_ZONE){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
            }
            if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_END_LEVEL_ZONE){
            if (Editor2D.localStatement == ObjectWithSetableFormAdding.FIRST_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
                System.out.println("Figures were cleared");
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_COLLECTABLE_OBJECT){
            if (levelsEditorProcess.pointsOnMap.size() > 1) levelsEditorProcess.pointsOnMap.clear();
            if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            System.out.println("Figures and points were cleared");
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_BRIDGE){
            if (levelsEditorProcess.pointsOnMap.size() > 1) levelsEditorProcess.pointsOnMap.clear();
            if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            System.out.println("Figures and points were cleared for the round bridge adding");
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.GRID_PREFERENCES){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.MAP_CLEARING){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OBJECT_SELECTING){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.BACKGROUND){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                levelsEditorProcess.pointsOnMap.clear();

            }
            if (levelsEditorProcess.figures.size()>0) {
                levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.COPY_ELEMENT){
            if (levelsEditorProcess.pointsOnMap.size() >= 1)  {
                levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PORTAL_SYSTEM){
            if (levelsEditorProcess.pointsOnMap.size() > 3)  {
                if (Editor2D.localStatement > AddingNewPortalSystemAction.ACTIVATED_BY) levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement > AddingNewPortalSystemAction.ACTIVATED_BY) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PLATFORM_SYSTEM){
            if (levelsEditorProcess.pointsOnMap.size() > 3)  {
                if (Editor2D.localStatement > AddingNewPlatformSystemAction.SET_PLATFORM_DIMENSIONS) levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement == RectangularElementAdding.FIRST_POINT_ADDING) levelsEditorProcess.figures.clear();
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_CAMERA_FIXATION_ZONE){
            if (levelsEditorProcess.pointsOnMap.size() > 1)  {
                if (Editor2D.localStatement > AddingNewCameraFixationZoneAction.ACTIVATING_CONDITION_SELECTING) levelsEditorProcess.pointsOnMap.clear();
                if (levelsEditorProcess.figures.size()>0 && Editor2D.localStatement == RectangularElementAdding.FIRST_POINT_ADDING) levelsEditorProcess.figures.clear();
            }
        }
        else {

        }

    }



    private void createNewObjects(MapZone mapZone){
        if (Editor2D.getGlobalStatement() == Editor2D.PLACE_PLAYER){
            if (submenuAction == null) submenuAction = new PlayerReplacingAction(mapZone, objectData);
            else if (submenuAction.getClass() != PlayerReplacingAction.class) {
                submenuAction = new PlayerReplacingAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX){
            if (submenuAction == null) {
                if (objectData == null) System.out.println("objectData is null");
                submenuAction = new AddingNewRoundBoxAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewRoundBoxAction.class) {
                submenuAction = new AddingNewRoundBoxAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_CIRCLE){
            if (submenuAction == null) {
                if (objectData == null) System.out.println("objectData is null");
                submenuAction = new AddingNewRoundCircleAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewRoundCircleAction.class) {
                submenuAction = new AddingNewRoundCircleAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_POLYGON){
            if (submenuAction == null) {
                if (objectData == null) System.out.println("objectData is null");
                submenuAction = new AddingNewRoundPolygon(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);

            }
            else if (submenuAction.getClass() != AddingNewRoundPolygon.class) {
                submenuAction = new AddingNewRoundPolygon(mapZone, objectData);
                //Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OBJECT_SELECTING){
            if (submenuAction == null) {
                if (objectData == null) System.out.println("objectData is null");
                submenuAction = new SelectingAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != SelectingAction.class) {
                submenuAction = new SelectingAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.COPY_ELEMENT){
            if (submenuAction == null) {
                submenuAction = new CopyAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != CopyAction.class) {
                submenuAction = new CopyAction(mapZone, objectData);
                //System.out.println("Copy menu");
                Editor2D.localStatement = 1;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.MOVE_ELEMENT){
            if (submenuAction == null) {
                submenuAction = new MovingAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != MovingAction.class) {
                submenuAction = new MovingAction(mapZone, objectData);
                Editor2D.localStatement = 1;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OBJECT_EDITING){
            if (submenuAction == null) {
                submenuAction = new ObjectEditingAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != ObjectEditingAction.class) {
                submenuAction = new ObjectEditingAction(mapZone, objectData);
                Editor2D.localStatement = 1;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_BRIDGE){
            if (submenuAction == null) {
                submenuAction = new AddingNewBridgeAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewBridgeAction.class) {
                submenuAction = new AddingNewBridgeAction(mapZone, objectData);
                Editor2D.localStatement = 1;
            }
        }

        else if (Editor2D.getGlobalStatement() == Editor2D.SELECTION_CANCEL){
            if (submenuAction == null) {
                if (objectData == null) System.out.println("objectData is null");
                submenuAction = new ClearSelectionAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != ClearSelectionAction.class) {
                submenuAction = new ClearSelectionAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_GUMBA){
            if (submenuAction == null) submenuAction = new AddingNewGumbaAction(mapZone, objectData, Gumba.CLASS_NAME);
            else if (submenuAction.getClass() != AddingNewGumbaAction.class) {
                submenuAction = new AddingNewGumbaAction(mapZone, objectData, Gumba.CLASS_NAME);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_SPIDER){
            if (submenuAction == null) submenuAction = new AddingNewSpiderAction(mapZone, objectData, Spider.CLASS_NAME);
            else if (submenuAction.getClass() != AddingNewSpiderAction.class) {
                submenuAction = new AddingNewSpiderAction(mapZone, objectData, Spider.CLASS_NAME);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_SNAKE){
            if (submenuAction == null) submenuAction = new AddingNewSnakeAction(mapZone, objectData, Snake.CLASS_NAME);
            else if (submenuAction.getClass() != AddingNewSnakeAction.class) {
                submenuAction = new AddingNewSnakeAction(mapZone, objectData, Snake.CLASS_NAME);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.BACKGROUND){
            if (submenuAction == null) submenuAction = new AddingNewBackgroundAction(mapZone, objectData);
            else if (submenuAction.getClass() != AddingNewBackgroundAction.class) {
                submenuAction = new AddingNewBackgroundAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_BOWSER){
            if (submenuAction == null) submenuAction = new AddingNewBowserAction(mapZone, objectData, Bowser.CLASS_NAME);
            else if (submenuAction.getClass() != AddingNewBowserAction.class) {
                submenuAction = new AddingNewBowserAction(mapZone, objectData, Bowser.CLASS_NAME);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_COLLECTABLE_OBJECT){
            if (submenuAction == null) submenuAction = new AddingNewCollectableObjectAction(mapZone, objectData);
            else if (submenuAction.getClass() != AddingNewCollectableObjectAction.class) {
                submenuAction = new AddingNewCollectableObjectAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_KOOPA){
            if (submenuAction == null) submenuAction = new AddingNewKoopaAction(mapZone, objectData, Koopa.CLASS_NAME);
            else if (submenuAction.getClass() != AddingNewKoopaAction.class) {
                submenuAction = new AddingNewKoopaAction(mapZone, objectData, Koopa.CLASS_NAME);
                Editor2D.localStatement = 0;
            }
        }

        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_STATIC_SPRITE){
            if (submenuAction == null) submenuAction = new AddingNewStaticSpriteAction(mapZone, objectData);
            else if (submenuAction.getClass() != AddingNewStaticSpriteAction.class) {
                submenuAction = new AddingNewStaticSpriteAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_SPRITE_ANIMATION){
            if (submenuAction == null) submenuAction = new AddingNewSpriteAnimationAction(mapZone, objectData);
            else if (submenuAction.getClass() != AddingNewSpriteAnimationAction.class) {
                submenuAction = new AddingNewSpriteAnimationAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_OBJECT_CLEARING_ZONE){
            if (submenuAction == null) {
                submenuAction = new AddingNewObjectClearingZoneAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewObjectClearingZoneAction.class) {
                submenuAction = new AddingNewObjectClearingZoneAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_END_LEVEL_ZONE){
            if (submenuAction == null) {
                submenuAction = new AddingNewEndLevelZoneAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewEndLevelZoneAction.class) {
                submenuAction = new AddingNewEndLevelZoneAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PORTAL_SYSTEM){
            if (submenuAction == null) {
                submenuAction = new AddingNewPortalSystemAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewPortalSystemAction.class) {
                submenuAction = new AddingNewPortalSystemAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.NEW_MAP){
            if (submenuAction == null) {
                submenuAction = new NewMapAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != NewMapAction.class) {
                submenuAction = new NewMapAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PLATFORM_SYSTEM){
            if (submenuAction == null) {
                submenuAction = new AddingNewPlatformSystemAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewPlatformSystemAction.class) {
                submenuAction = new AddingNewPlatformSystemAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_CAMERA_FIXATION_ZONE){
            if (submenuAction == null) {
                submenuAction = new AddingNewCameraFixationZoneAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewCameraFixationZoneAction.class) {
                submenuAction = new AddingNewCameraFixationZoneAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_PIPE){
            if (submenuAction == null) {
                submenuAction = new AddingNewRoundPipeAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != AddingNewRoundPipeAction.class) {
                submenuAction = new AddingNewRoundPipeAction(mapZone, objectData);
                Editor2D.localStatement = 0;

            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.GRID_PREFERENCES){
            if (submenuAction == null) {
                submenuAction = new GridPreferencesAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != GridPreferencesAction.class) {
                submenuAction = new GridPreferencesAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OPEN_MAP){
            if (submenuAction == null) {
                submenuAction = new OpenMapAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != OpenMapAction.class) {
                submenuAction = new OpenMapAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }

        else if (Editor2D.getGlobalStatement() == Editor2D.MAP_CLEARING){
            if (submenuAction == null) {
                submenuAction = new ClearMapAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != ClearMapAction.class) {
                submenuAction = new ClearMapAction(mapZone, objectData);
                Editor2D.localStatement = 0;
            }
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OBJECT_DELETING){
            if (submenuAction == null) {
                submenuAction = new ObjectDeletingAction(mapZone, objectData);   //submenuAction = new AddingNewRoundBoxAction(mapZone, tabsController);
            }
            else if (submenuAction.getClass() != ObjectDeletingAction.class) {
                submenuAction = new ObjectDeletingAction(mapZone, objectData);
                Editor2D.localStatement = 1;
            }
        }

    }

    private boolean isMouseOnMenuPanel(LevelsEditorProcess levelsEditorProcess){
        boolean mouseOnMenuPanel = false;
        if (levelsEditorProcess.menuPanel.isSomeComboBoxShifted()) {
            if (levelsEditorProcess.menuPanel.getShiftedComboBox().isMouseOnElement(Program.engine.mouseX, Program.engine.mouseY, PApplet.CORNER))  mouseOnMenuPanel = true;
            if (levelsEditorProcess.menuPanel.getShiftedComboBox().getWrappingStatement() != androidAndroidGUI_MovableComboBox.COMPLETE_WRAPPED_UP) {
                if (levelsEditorProcess.menuPanel.getShiftedComboBox().isMouseOnSomeDropDownElement())
                    mouseOnMenuPanel = true;
            }
        }
        return mouseOnMenuPanel;
    }

    private void clearRedundantObjects(GameRound gameRound){
        submenuAction.clearRedundantObjects(gameRound);
        //System.out.println("Cleared");
    }

    public void update(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        boolean localChanging = Editor2D.isLocalStatementChanged();
        boolean globalChanging = Editor2D.isGlobalStatementChanged();
        if (localChanging || globalChanging){
            if (submenuAction != null) {
                submenuAction.reconstructTab(tabsController.tab, Editor2D.getGlobalStatement(), Editor2D.localStatement);
                if (globalChanging) {
                    clearRedundantObjects(levelsEditorProcess.getGameRound());
                    submenuAction.dispose(levelsEditorProcess);
                }
            }
            else System.out.println("This condition is new and can not delete figures and points from the world; Local: " + Editor2D.localStatement);
            if (globalChanging || Editor2D.localStatement <= 1) {
                deleteAnotherObjects(levelsEditorProcess);
            }
            createNewObjects(levelsEditorProcess.mapZone);
            if (Editor2D.localStatement == 0){
                if (levelsEditorProcess.levelsEditorControl.objectData == null) {
                    System.out.println("Game data was recreated");
                    levelsEditorProcess.levelsEditorControl.objectData = new GameObjectDataForStoreInEditor();
                }
            }
            PhysicGameWorld.makeAllBodiesInactive();
            System.gc();
        }
        if (Editor2D.canBeNextOperationMade()) {
            if (!isMouseOnMenuPanel(levelsEditorProcess)) {
                submenuAction.update(levelsEditorProcess, tabsController, objectData);
            }
        }
        if (Program.OS == Program.DESKTOP) updateCameraMovementOnWindows(gameCamera, levelsEditorProcess);
        else if (Program.OS == Program.ANDROID) {
            if (!ZONE_MANIPULATIONS_BY_DIRECT_CONTROL) {
                updateCameraMovementOnAndroid(gameCamera, levelsEditorProcess);
            }
        }
        levelsEditorProcess.getMenuPanelController().update(levelsEditorProcess.getGameRound(), gameMainController);
    }


    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        submenuAction.draw(gameCamera, levelsEditorProcess);
    }


    //Copied
    private void scaleInDesktopMode(LevelsEditorProcess levelsEditorProcess, GameCamera gameCamera){
        scaling(Program.getMouseWheelRotation(), gameCamera);
        levelsEditorProcess.menuPanel.returnShiftedComboBoxToNull();

    }


    //Copied
    private void moveZoneInWindowsStyle(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (LevelsEditorProcess.isPointOnMapZone(Program.engine.mouseX, Program.engine.mouseY)){
            if (Editor2D.GAME_ZONE_CAN_BE_MOVED){
                if (Program.engine.mousePressed == true && Editor2D.prevMousePressedStatement == true){
                    if (Editor2D.wasMouseMoved(gameCamera.getScale())){ //was *
                        if (Program.engine.abs(Program.engine.dist(Program.engine.mouseX, Program.engine.mouseY, Program.engine.pmouseX, Program.engine.pmouseY))>(Editor2D.maxMovementProOneFrameForStaticMouse*gameCamera.getScale())){
                            if (!gameCamera.MOVEMENT_WITH_ACCELERATE){
                                gameCamera.translate(new PVector(-(Program.engine.mouseX - Program.engine.pmouseX) / gameCamera.getScale(), -(Program.engine.mouseY - Program.engine.pmouseY) / gameCamera.getScale()));
                                levelsEditorProcess.menuPanel.returnShiftedComboBoxToNull();
                            }
                            else {
                                gameCamera.addMovementVector(Program.engine.mouseX, Program.engine.mouseY, Program.engine.pmouseX, Program.engine.pmouseY);
                            }
                            if (Program.isVirtualKeyboardOpened()) Program.openVirtualKeyboard(false);

                        }
                    }
                }
            }
        }
    }


    //Copied
    private void updateCameraMovementOnAndroid(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (TouchScreenActionBuffer.getActionType() == OnScreenActionType.PINCH){
            System.out.println("Player zooms");
            OnPinchAction action = (OnPinchAction)TouchScreenActionBuffer.getAction();
            if (levelsEditorProcess.mapZone.isPointOnMapZone(action.getCenter().x, action.getCenter().y)) {
                float scaleValue = action.getValue()*0.005f;    //System.out.print("Scale was: " + gameCamera.getScale() +"; Value: " + action.getValue() + " and pos : " + action.getPosition().x + "x" + action.getPosition());
                gameCamera.updateScaleForLevelseditor(scaleValue);
            }
            if (Program.isVirtualKeyboardOpened()) Program.openVirtualKeyboard(false);
        }
        else {
            moveZoneInWindowsStyle(gameCamera, levelsEditorProcess);
            TouchScreenActionBuffer.clearBuffer();
            //Program.openVirtualKeyboard(false);
        }
        TouchScreenActionBuffer.clearBuffer();
    }

    private void updateCameraMovementOnWindows(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        boolean noScalling = true;
        if (Program.OS == Program.DESKTOP && Program.getMouseWheelRotation() != 0) {
            scaleInDesktopMode(levelsEditorProcess, gameCamera);
            noScalling = false;
        }
        if (noScalling){
            moveZoneInWindowsStyle(gameCamera, levelsEditorProcess);
        }
    }



    //Copied
    private void scaling(byte direction, GameCamera gameCamera){
        if (direction == Program.BACKWARD_ROTATION){
            gameCamera.changeScale(Editor2D.SCALLING_DOWN);
        }
        else if (direction == Program.FORWARD_ROTATION){
            gameCamera.changeScale(Editor2D.SCALLING_UP);
        }
        Program.setMouseWheelRotation((byte)0);
    }

    public GameObjectDataForStoreInEditor getObjectData() {
        return objectData;
    }


    public void onFlick( float x, float y, float px, float py, float v){
        if ( ZONE_MANIPULATIONS_BY_DIRECT_CONTROL){
            moveZoneInWindowsStyle(editorCamera, levelsEditorProcess);
            TouchScreenActionBuffer.clearBuffer();
            Program.openVirtualKeyboard(false);
        }
    }

    public void onPinch(float x, float y, float value){
        if ( ZONE_MANIPULATIONS_BY_DIRECT_CONTROL){
            System.out.println("Player zooms");
            OnPinchAction action = (OnPinchAction)TouchScreenActionBuffer.getAction();
            if (levelsEditorProcess.mapZone.isPointOnMapZone(action.getCenter().x, action.getCenter().y)) {
                float scaleValue = action.getValue()*0.005f;    //System.out.print("Scale was: " + gameCamera.getScale() +"; Value: " + action.getValue() + " and pos : " + action.getPosition().x + "x" + action.getPosition());
                editorCamera.updateScaleForLevelseditor(scaleValue);
            }
            Program.openVirtualKeyboard(false);
        }
    }


}
