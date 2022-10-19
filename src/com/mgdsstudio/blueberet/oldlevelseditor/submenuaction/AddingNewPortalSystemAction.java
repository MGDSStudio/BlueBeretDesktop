package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_RadioButton;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewPortalSystemAction extends SubmenuWithTwoRectZones{
    //private PointAddingController firstPointAddingController, secondPointAddingController, pointsAddingController;

    private final static byte FIRST_POINT_ADDING = 1;
    private final static byte SECOND_POINT_ADDING = 2;
    private final static byte THIRD_POINT_ADDING = 3;
    private final static byte FOURTH_POINT_ADDING = 4;
    public final static byte ACTIVATED_BY = 5;
    private final static byte TRANSFER_DIRECTION = 6;
    private final static byte USING_REPEATABILITY = 7;
    private final static byte COMPLETED = 8;
    public final static byte END = COMPLETED;


    private final String FOR_PLAYER = "For player";
    private final String FOR_ENEMIES = "For enemies";
    private final String FOR_EVERY_PERSON = "For every character";

    private final String FROM_START_TO_FINISH = "One directional";
    private final String HERE_AND_THERE = "Here and there";

    private final String REUSEABLE = "Reuseable";
    private final String DISPOSABLE = "Disposable";

    private final String DOWNWARD = "Downward";
    private final String UPWARD = "Upward";
    private final String TO_THE_LEFT = "To the left";
    private final String TO_THE_RIGHT = "To the right";

    //public static final byte BY_PLAYER = 1;
    //	public static final byte BY_ENEMIES = 2;
    //	public static final byte BY_EVERY_PERSON = 3;
    //Flag enter, Flag exit, byte activatedBy, boolean transferDirection, boolean usingRepeateability

    public AddingNewPortalSystemAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(PipePortal.CLASS_NAME);
        //firstPointAddingController = new PointAddingController();
        //secondPointAddingController = new PointAddingController();
        pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {

            if (localStatement <= FIRST_POINT_ADDING) {
                tab.clearElements();
                //tab.setMinimalHeight();
                androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, DOWNWARD);
                tab.addGUI_Element(element, null);
                element.setStatement(androidGUI_Element.PRESSED);
                androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 45), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, UPWARD);
                tab.addGUI_Element(element2, null);
                androidAndroidGUI_RadioButton element3 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 75), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, TO_THE_LEFT);
                tab.addGUI_Element(element3, null);
                androidAndroidGUI_RadioButton element4 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 105), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, TO_THE_RIGHT);
                tab.addGUI_Element(element4, null);

                ArrayList<androidAndroidGUI_RadioButton> allRadioButtons = new ArrayList<>();
                allRadioButtons.add(element);
                allRadioButtons.add(element2);
                allRadioButtons.add(element3);
                allRadioButtons.add(element4);

                element.addAnotherRadioButtonsInGroup(allRadioButtons);
                element2.addAnotherRadioButtonsInGroup(allRadioButtons);
                element3.addAnotherRadioButtonsInGroup(allRadioButtons);
                element4.addAnotherRadioButtonsInGroup(allRadioButtons);

                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(null);
                if (Editor2D.localStatement != FIRST_POINT_ADDING) Editor2D.localStatement = FIRST_POINT_ADDING;
            }
            else if (localStatement == SECOND_POINT_ADDING) {
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 145), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(null);
            }
            else if (localStatement == ACTIVATED_BY){
                tab.clearElements();
                tab.setMinimalHeight();
                androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FOR_PLAYER, false);
                tab.addGUI_Element(buttonStatic, null);
                androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FOR_ENEMIES, false);
                tab.addGUI_Element(buttonDynamic, null);
                androidGUI_Element buttonWithoutGraphic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FOR_EVERY_PERSON, false);
                tab.addGUI_Element(buttonWithoutGraphic, null);
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(tab.getElements());
            }
            else if (localStatement == TRANSFER_DIRECTION){
                tab.clearElements();
                tab.setMinimalHeight();
                androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FROM_START_TO_FINISH, false);
                tab.addGUI_Element(buttonStatic, null);
                androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HERE_AND_THERE, false);
                tab.addGUI_Element(buttonDynamic, null);
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(tab.getElements());
            }
            else if (localStatement == USING_REPEATABILITY){
                tab.clearElements();
                tab.setMinimalHeight();
                androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, REUSEABLE, false);
                tab.addGUI_Element(buttonStatic, null);
                androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, DISPOSABLE, false);
                tab.addGUI_Element(buttonDynamic, null);
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(tab.getElements());
            }

    }

    private void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        ArrayList<androidGUI_Element> guiReleasedElements = tabController.getReleasedElements();
        //System.out.println("Released elements: " + guiReleasedElements.size());
        if (Editor2D.canBeNextOperationMade()) {
            if (guiReleasedElements.size()>0) {
                portalSystemUpdating(objectData, levelsEditorProcess, guiReleasedElements);
            }
        }
    }

    private void portalSystemUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElement) {
        for (androidGUI_Element element : releasedElement){
            if (element.getClass() == androidAndroidGUI_Button.class) {
                if (element.getName() == CANCEL) {
                    Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                    levelsEditorProcess.figures.clear();
                    levelsEditorProcess.pointsOnMap.clear();
                    System.out.println("Reset");
                } else if (Editor2D.localStatement == ACTIVATED_BY) {
                    if (element.getName() == FOR_PLAYER) {
                        System.out.println("FOR_PLAYER is pressed");
                        objectData.setActivatedBy(PipePortal.BY_PLAYER);
                        Editor2D.setNewLocalStatement(USING_REPEATABILITY);
                        objectData.setTransferDirection(PipePortal.ENTER_TO_EXIT);
                    } else if (element.getName() == FOR_ENEMIES) {
                        System.out.println("FOR_ENEMIES is pressed");
                        objectData.setActivatedBy(PipePortal.BY_ENEMIES);
                        Editor2D.setNewLocalStatement(USING_REPEATABILITY);
                        objectData.setTransferDirection(PipePortal.ENTER_TO_EXIT);
                    } else if (element.getName() == FOR_EVERY_PERSON) {
                        System.out.println("FOR_EVERY_PERSON is pressed");
                        objectData.setActivatedBy(PipePortal.BY_EVERY_PERSON);
                        Editor2D.setNewLocalStatement(USING_REPEATABILITY);
                        objectData.setTransferDirection(PipePortal.ENTER_TO_EXIT);
                    }
                    makePauseToNextOperation();
                    /*  // not more need
                } else if (Editor2D.localStatement == TRANSFER_DIRECTION) {
                    if (element.getName() == FROM_START_TO_FINISH) {
                        System.out.println("FROM_START_TO_FINISH is pressed");
                        objectData.setTransferDirection(PipePortal.ENTER_TO_EXIT);
                        Editor2D.setNextLocalStatement();
                    } else if (element.getName() == HERE_AND_THERE) {
                        System.out.println("HERE_AND_THERE is pressed");
                        objectData.setTransferDirection(PipePortal.HERE_AND_THERE);
                        Editor2D.setNextLocalStatement();
                    }
                    makePauseToNextOperation();*/
                } else if (Editor2D.localStatement == USING_REPEATABILITY) {
                    if (element.getName() == REUSEABLE) {
                        addTextToNewCreatedElement("portal system", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                        addTextToNewCreatedElement("portal system", levelsEditorProcess, levelsEditorProcess.figures.get(1));
                        System.out.println("REUSEABLE_TO_FINISH is pressed");
                        objectData.setUsingRepeatability(PipePortal.REUSEABLE);
                        //Editor2D.setNextLocalStatement();
                        Editor2D.setNewLocalStatement(END);
                        System.out.println("End pressed");
                    } else if (element.getName() == DISPOSABLE) {
                        addTextToNewCreatedElement("portal system", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                        addTextToNewCreatedElement("portal system", levelsEditorProcess, levelsEditorProcess.figures.get(1));
                        System.out.println("DISPOSABLE is pressed");
                        objectData.setUsingRepeatability(PipePortal.DISPOSABLE);
                        //Editor2D.setNextLocalStatement();
                        Editor2D.setNewLocalStatement(END);
                        System.out.println("End pressed");
                    }
                    makePauseToNextOperation();
                }
            }
            else if (element.getClass() == androidAndroidGUI_RadioButton.class){
                if (Editor2D.localStatement >= FIRST_POINT_ADDING && Editor2D.localStatement <= SECOND_POINT_ADDING){
                    if (element.getName() == DOWNWARD) objectData.setFirstFlagDirection(Flag.TO_DOWN);
                    else if (element.getName() == UPWARD) objectData.setFirstFlagDirection(Flag.TO_UP);
                    else if (element.getName() == TO_THE_LEFT) objectData.setFirstFlagDirection(Flag.TO_LEFT);
                    else if (element.getName() == TO_THE_RIGHT) objectData.setFirstFlagDirection(Flag.TO_RIGHT);
                    //System.out.println("First Direction set on " + element.getName());
                }
                else if (Editor2D.localStatement >= THIRD_POINT_ADDING && Editor2D.localStatement <= FOURTH_POINT_ADDING){
                    if (element.getName() == DOWNWARD) objectData.setSecondFlagDirection(Flag.TO_DOWN);
                    else if (element.getName() == UPWARD) objectData.setSecondFlagDirection(Flag.TO_UP);
                    else if (element.getName() == TO_THE_LEFT) objectData.setSecondFlagDirection(Flag.TO_LEFT);
                    else if (element.getName() == TO_THE_RIGHT) objectData.setSecondFlagDirection(Flag.TO_RIGHT);
                    //System.out.println("Second Direction set on " + element.getName());
                }
            }
        }
    }


    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            try {
                if (Editor2D.localStatement == FIRST_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add corner for enter zone");
                    onScreenConsole.setText(actualConsoleText);
                } else if (Editor2D.localStatement == SECOND_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add next corner for enter zone");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == THIRD_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add corner for exit zone");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == FOURTH_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add next corner for exit zone");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == ACTIVATED_BY) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Who can use this portal?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == TRANSFER_DIRECTION) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Is only one directional transfer allowed?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == USING_REPEATABILITY) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("How many times is it usable?");
                    onScreenConsole.setText(actualConsoleText);
                }

            } catch (Exception e) {
                System.out.println("Can not change the text of the console " + e);
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully");
                onScreenConsole.setText(actualConsoleText);
            }

        }

    }


    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (firstZoneAdding == null && Editor2D.localStatement < THIRD_POINT_ADDING) firstZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement == THIRD_POINT_ADDING && secondZoneAdding == null) secondZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement <= FOURTH_POINT_ADDING) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
        }
    }

    /*
        @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (firstZoneAdding == null && Editor2D.localStatement < THIRD_POINT_ADDING) firstZoneAdding = new RoundBoxAdding();
        if (Editor2D.localStatement == THIRD_POINT_ADDING && secondZoneAdding == null) secondZoneAdding = new RoundBoxAdding();

        if (Editor2D.localStatement < THIRD_POINT_ADDING) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
            if (firstPointAddingController.canBeNewObjectAdded()) {
                firstPointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                System.out.println("Added first polygon");
                Editor2D.setNewLocalStatement(THIRD_POINT_ADDING);
            }
            else if (Editor2D.localStatement == THIRD_POINT_ADDING) {
                System.out.println("First zone created");
                objectWasAdded(firstZoneAdding, levelsEditorProcess, objectData);
            }
        }
        else if (Editor2D.localStatement >= THIRD_POINT_ADDING) {
            if (secondZoneAdding != null) {
                updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
                if (secondPointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < ACTIVATED_BY) {
                    secondPointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                    System.out.println("Added second polygon");
                    Editor2D.setNewLocalStatement(ACTIVATED_BY);
                }
                else if (Editor2D.localStatement > FOURTH_POINT_ADDING && levelsEditorProcess.pointsOnMap.size() == 4) {
                    System.out.println("Second zone created ");
                    objectWasAdded(secondZoneAdding, levelsEditorProcess, objectData);
                }
            }
        }
    }

     */



    /*
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (firstZoneAdding == null) firstZoneAdding = new RoundBoxAdding();
        if (Editor2D.localStatement < THIRD_POINT_ADDING) {
            if (!firstZoneAdding.isCompleted()) {
                updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
                if (firstPointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < THIRD_POINT_ADDING) {
                    firstPointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                    System.out.println("Added first polygon");
                }
            }
            else objectWasAdded(firstZoneAdding, levelsEditorProcess);
        }
        if (Editor2D.localStatement >= THIRD_POINT_ADDING) {
            if (Editor2D.localStatement == THIRD_POINT_ADDING){
                if (secondZoneAdding == null) secondZoneAdding = new RoundBoxAdding();
            }
            if (secondZoneAdding != null) {
                if (!secondZoneAdding.isCompleted()) {
                    updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
                    if (secondPointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < ACTIVATED_BY) {
                        secondPointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                        System.out.println("Added second polygon");
                        Editor2D.setNextLocalStatement();
                    }
                } else objectWasAdded(secondZoneAdding, levelsEditorProcess);
            }
        }
    }
    */

    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointsAddingController.draw(gameCamera, levelsEditorProcess);
    }

    /*
    protected void createFigure(RectangularElementAdding portalAdding, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (portalAdding.equals(firstZoneAdding)){
            addFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
            firstZoneAdding = null;
        }
        else  if (portalAdding.equals(secondZoneAdding)){
            addFigureOnMapZoneAndSaveData((byte)2, levelsEditorProcess, objectData);
            secondZoneAdding = null;
        }
        makePauseToNextOperation();
    }*/

    /*
    protected void objectWasAdded(RoundBoxAdding portalAdding, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (portalAdding.equals(firstZoneAdding)){
            Editor2D.setNewLocalStatement(THIRD_POINT_ADDING);
            addFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
            firstZoneAdding = null;
        }
        else  if (portalAdding.equals(secondZoneAdding)){
            Editor2D.setNewLocalStatement(ACTIVATED_BY);
            addFigureOnMapZoneAndSaveData((byte)2, levelsEditorProcess, objectData);
            secondZoneAdding = null;
        }
        makePauseToNextOperation();
    }*/

    /*
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
                    Editor2D.setNewLocalStatement(ACTIVATED_BY);
                    System.out.println("Last point for second zone placed");
                    createFigure(secondZoneAdding, levelsEditorProcess, objectData);
                }
            }
        }
    }
    */

    @Override
    public byte getEndValue(){
        return END;
    }

}
