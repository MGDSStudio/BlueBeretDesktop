package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import com.mgdsstudio.blueberet.zones.SingleFlagZone;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewCameraFixationZoneAction extends OneZoneAddingAction{

    protected PointAddingController pointsAddingController;
    protected RectangularElementAdding secondZoneAdding, firstZoneAdding;
    private PointAddingController concentratingPointAddingController;

    //Statements
    public final static int FIRST_POINT_ADDING = RectangularElementAdding.FIRST_POINT_ADDING;
    public final static int SECOND_POINT_ADDING = RectangularElementAdding.SECOND_POINT_ADDING;
    public final static int ACTIVATING_CONDITION_SELECTING = SECOND_POINT_ADDING+1;
    public final static int DEACTIVATING_CONDITION_SELECTING = ACTIVATING_CONDITION_SELECTING+1;
    public final static int CAMERA_SCALE_SELECTION = DEACTIVATING_CONDITION_SELECTING+1;
    public final static int REPEATABILITY_SELECTION = CAMERA_SCALE_SELECTION+1;
    public final static int CONCENTRATING_POINT_SELECTION = REPEATABILITY_SELECTION+1;
    public final static int END = CONCENTRATING_POINT_SELECTION+1;

    //GUI texts
    private final String PLAYER_APPEARS_IN_ZONE = " Player enters the zone ";
    private final String ENEMIES_APPEAR_IN_ZONE = " Enemies leaves the zone ";

    private final String PLAYER_LEAVES_ZONE =    " Player leaves the zone ";
    private final String ENEMIES_LEAVE_ZONE = " Enemies leave the zone ";

    private final String MAX_SCALE = "    Max zoom    ";
    private final String MIN_SCALE = "    Min zoom    ";

    private final String ONCE =      "   Once    ";
    private final String REPEATING = " Repeating ";

    //public CameraFixationZone(Flag flag, int x, int y, int activatingCondition, int deactivatingCondition, boolean cameraScale, boolean repeateability){

    public AddingNewCameraFixationZoneAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
        objectData.setClassName(CameraFixationZone.CLASS_NAME);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == FIRST_POINT_ADDING) {
            clearAndMinimizeTab(tab);
        }
        else if (localStatement == SECOND_POINT_ADDING) {
            clearAndMinimizeTab(tab);
        }
        else if (localStatement == ACTIVATING_CONDITION_SELECTING) {
            createActivatingConditionSelection(tab);
            System.out.println("This menu is not need now");
        }
        else if (localStatement == DEACTIVATING_CONDITION_SELECTING) {
            createDeactivatingConditionSelection(tab);
            System.out.println("Deactivating menu was created");
        }
        else if (localStatement == CAMERA_SCALE_SELECTION) {
            createCameraScaleSelectionMenu(tab);
        }
        else if (localStatement == REPEATABILITY_SELECTION) {
            createRepeatabilityMenu(tab);
        }
        else if (localStatement == CONCENTRATING_POINT_SELECTION) {
            createClearMenuWithCancelButton(tab);
            System.out.println("clear menu was created!");
        }
    }

    private void createRepeatabilityMenu(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        androidGUI_Element buttonOnce = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ONCE, false);
        tab.addGUI_Element(buttonOnce, null);
        androidGUI_Element buttonRepeating = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, REPEATING, false);
        tab.addGUI_Element(buttonRepeating, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void createCameraScaleSelectionMenu(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        androidGUI_Element buttonMaxZoom = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MAX_SCALE, false);
        tab.addGUI_Element(buttonMaxZoom, null);
        androidGUI_Element buttonMinZoom = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MIN_SCALE, false);
        tab.addGUI_Element(buttonMinZoom, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void createActivatingConditionSelection(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        androidGUI_Element buttonAppearsInZone = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, PLAYER_APPEARS_IN_ZONE, false);
        tab.addGUI_Element(buttonAppearsInZone, null);
        androidGUI_Element buttonEnemiesLeavesZone = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ENEMIES_LEAVE_ZONE, false);
        tab.addGUI_Element(buttonEnemiesLeavesZone, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    private void createDeactivatingConditionSelection(androidGUI_ScrollableTab tab) {
        tab.clearElements();
        androidGUI_Element buttonAppearsInZone = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, PLAYER_LEAVES_ZONE, false);
        tab.addGUI_Element(buttonAppearsInZone, null);
        androidGUI_Element buttonEnemiesLeavesZone = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, ENEMIES_LEAVE_ZONE, false);
        tab.addGUI_Element(buttonEnemiesLeavesZone, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData) {
        if (Program.engine.frameCount%30==0) System.out.println("Local: " + Editor2D.localStatement);
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (firstZoneAdding == null && Editor2D.localStatement < ACTIVATING_CONDITION_SELECTING) firstZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement == CONCENTRATING_POINT_SELECTION && secondZoneAdding == null) secondZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            updatePointAddingForFirstZone(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
        }
        else if (Editor2D.localStatement == CONCENTRATING_POINT_SELECTION) {
            updatePointAddingForConcentrationPoint(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
        }
    }

    private void updatePointAddingForConcentrationPoint(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData) {
        if (concentratingPointAddingController == null) concentratingPointAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
        //if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() > 1) {
                concentratingPointAddingController.update(gameCamera, levelsEditorProcess);
                if (concentratingPointAddingController.canBeNewObjectAdded()) {
                    System.out.println("New point can be added");
                    concentratingPointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                    concentratingPointAddingController.endAdding();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 3 ) {
                    secondZoneAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size() - 1).getPosition());
                    System.out.println("First point for concentration point placed");
                    saveConcentrationPointData(objectData, levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size() - 1));
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                    concentratingPointAddingController = null;
                }
                /*
                } else if (levelsEditorProcess.pointsOnMap.size() == 2 && Editor2D.localStatement != DEACTIVATING_CONDITION_SELECTING) {
                    firstZoneAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    Editor2D.setNewLocalStatement((byte) DEACTIVATING_CONDITION_SELECTING);
                    System.out.println("Second point for first zone placed");
                    createFigure(firstZoneAdding, levelsEditorProcess, objectData);
                }*/
            }
        //}
    }

    private void saveConcentrationPointData(GameObjectDataForStoreInEditor objectData, Point point) {

        objectData.setConcentratingPointX((int) point.getPosition().x);
        objectData.setConcentratingPointY((int)point.getPosition().y);

        Editor2D.setNewLocalStatement((byte)END);
        System.out.println("End of the action");
    }

    protected void updatePointAddingForFirstZone(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size()<4){
                pointsAddingController.update(gameCamera, levelsEditorProcess);
                if (pointsAddingController.canBeNewObjectAdded()){
                    System.out.println("New point can be added");
                    pointsAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                    pointsAddingController.endAdding();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 0 && Editor2D.localStatement != FIRST_POINT_ADDING) Editor2D.setNewLocalStatement((byte) FIRST_POINT_ADDING);
                if (levelsEditorProcess.pointsOnMap.size() == 1 && Editor2D.localStatement != SECOND_POINT_ADDING) {
                    firstZoneAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    Editor2D.setNewLocalStatement((byte) SECOND_POINT_ADDING);
                    System.out.println("First point for first zone placed");
                }
                else if (levelsEditorProcess.pointsOnMap.size() == 2 && Editor2D.localStatement != DEACTIVATING_CONDITION_SELECTING) {
                    firstZoneAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    Editor2D.setNewLocalStatement((byte) DEACTIVATING_CONDITION_SELECTING);
                    System.out.println("Second point for first zone placed");
                    createFigure(firstZoneAdding, levelsEditorProcess, objectData);
                }
            }
        }
    }

    protected void createFigure(RectangularElementAdding rectZoneAdding, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (rectZoneAdding.equals(firstZoneAdding)){
            addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
            firstZoneAdding = null;
        }
        makePauseToNextOperation();
    }



    @Override
    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> guiReleasedElements = tabController.getReleasedElements();
        if (guiReleasedElements.size()>0) {
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, guiReleasedElements);
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0){
            for (androidGUI_Element releasedElement : releasedElements){
                if (releasedElement.getName() == CANCEL) {
                    Editor2D.setNewLocalStatement((byte) FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                }
                else{
                    if (Editor2D.localStatement == DEACTIVATING_CONDITION_SELECTING) {
                        if (releasedElement.getName() == PLAYER_LEAVES_ZONE){
                            objectData.setActivatedBy((byte) SingleFlagZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE);
                            objectData.setDeactivatedBy(CameraFixationZone.PLAYER_LEAVES_ZONE);
                            Editor2D.setNewLocalStatement((byte) CAMERA_SCALE_SELECTION);
                        }
                        else if (releasedElement.getName() == ENEMIES_LEAVE_ZONE){
                            objectData.setActivatedBy((byte) SingleFlagZone.ACTIVATING_BY_PLAYER_APPEARING_IN_ZONE);
                            objectData.setDeactivatedBy(CameraFixationZone.ENEMIES_LEAVES_ZONE);
                            Editor2D.setNewLocalStatement((byte) CAMERA_SCALE_SELECTION);
                        }
                    }
                    else if (Editor2D.localStatement == CAMERA_SCALE_SELECTION) {
                        if (releasedElement.getName() == MAX_SCALE) {
                            objectData.setCameraScale(CameraFixationZone.MAX_SCALE);
                            Editor2D.setNewLocalStatement((byte) REPEATABILITY_SELECTION);
                        } else if (releasedElement.getName() == MIN_SCALE) {
                            objectData.setCameraScale(CameraFixationZone.MIN_SCALE);
                            Editor2D.setNewLocalStatement((byte) REPEATABILITY_SELECTION);
                        }
                    }
                    else if (Editor2D.localStatement == REPEATABILITY_SELECTION) {
                        if (releasedElement.getName() == ONCE) {
                            objectData.setRepeateability(CameraFixationZone.ONCE);
                            Editor2D.setNewLocalStatement((byte) CONCENTRATING_POINT_SELECTION);
                        } else if (releasedElement.getName() == REPEATING) {
                            objectData.setRepeateability(CameraFixationZone.REPEATING);
                            Editor2D.setNewLocalStatement((byte) CONCENTRATING_POINT_SELECTION);
                        }
                        //makePauseToNextOperation();
                    }
                    System.out.println("Statement: " + Editor2D.localStatement);
                }
            }
        }

    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()){
            try {
                if (Editor2D.localStatement <=FIRST_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add first corner");
                }
                else if (Editor2D.localStatement ==SECOND_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add second corner");
                }
                else if (Editor2D.localStatement == ACTIVATING_CONDITION_SELECTING) {
                    setTextForConsole(onScreenConsole, "   When must the zone be activated?   ");
                }
                else if (Editor2D.localStatement == DEACTIVATING_CONDITION_SELECTING) {
                    setTextForConsole(onScreenConsole, "  When must the zone be deactivated?  ");
                }
                else if (Editor2D.localStatement == CAMERA_SCALE_SELECTION) {
                    setTextForConsole(onScreenConsole, "Which camera scale must be in the zone?");
                }
                else if (Editor2D.localStatement == REPEATABILITY_SELECTION) {
                    setTextForConsole(onScreenConsole, "    Choose repeatability    ");
                }
                else if (Editor2D.localStatement == CONCENTRATING_POINT_SELECTION) {
                    setTextForConsole(onScreenConsole, "Set the concentrating point for the camera");
                }
                else if (Editor2D.localStatement == END) {
                    setTextForConsole(onScreenConsole, "New camera fixation zone was added");
                }
                else setTextForConsole(onScreenConsole, " ");
            }
            catch (Exception e) {
                System.out.println("Can not change the text of the console.  " +  "; Trouble: " + e);
            }
        }
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        if (pointsAddingController != null){
            pointsAddingController.draw(gameCamera, levelsEditorProcess);
        }
        if (concentratingPointAddingController != null){
            concentratingPointAddingController.draw(gameCamera, levelsEditorProcess);
            //System.out.println("Arrows are drawn");
        }
    }


}
