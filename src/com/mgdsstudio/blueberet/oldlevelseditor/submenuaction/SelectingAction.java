package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;


import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalToAnotherLevel;
import com.mgdsstudio.blueberet.gameobjects.portals.SimplePortal;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_CheckBox;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectOnMapSelectingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.zones.AbstractTrigger;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import com.mgdsstudio.blueberet.zones.MessageAddingZone;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import processing.core.PVector;

import java.util.ArrayList;

public class SelectingAction extends SubmenuAction{
    //private ArrayList <GameObject> selectedObjects;
    //private GameObject selectedObject;
    //private ArrayList <IndependentOnScreenGraphic> selectedGraphic;
    //private IntList stringsInDataFileToBeDeleted;
    public static ArrayList <SelectedElement> selectedElements = new ArrayList<>();
    private boolean newObjectWasAdded = false;
    private boolean someObjectWasDeleted = false;
    public final static byte FIRST_ELEMENT_SELECTION = 1;
    public final static byte ALREADY_SELECTED = 2;
    public final static byte END = 127;   //This statement can not be achived
    private final static String CLEAR_SELECTION = "Clear selection";
    private String textDataToBeShown = "";
    private final String SELECT_ALL_ON_SCREEN_STRING = "Select visible";

    private ObjectOnMapSelectingController selectingController;

    public SelectingAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        selectedElements = new ArrayList<>();
        selectingController = new ObjectOnMapSelectingController();
    }



    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (Editor2D.localStatement <= FIRST_ELEMENT_SELECTION){
            tab.clearElements();
            Editor2D.localStatement = FIRST_ELEMENT_SELECTION;
            androidGUI_Element buttonManual = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SELECT_ALL_ON_SCREEN_STRING, false);
            tab.addGUI_Element(buttonManual, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (Editor2D.localStatement > FIRST_ELEMENT_SELECTION){
            if (selectedElements.size() == 0) {
                fillTabWithElementsFromUpToDown(tab, false);
            }
            else fillTabWithElementsFromUpToDown(tab, false);
        }
    }

    public boolean mustBeTabReconstructed(){
        if (newObjectWasAdded) return true;
        else if (someObjectWasDeleted) return true;
        else return false;
    }

    protected void updateTabController(ScrollableTabController tabController, GameCamera editorCamera, GameRound gameRound){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (releasedElements.size()>0) {
                tabUpdating(tabController, releasedElements, editorCamera, gameRound);
            }
        }
    }
/*
    private void setGUIonNewPlaces(GUI_ScrollableTab tab){
        int yPos = distanceToFirstGUICheckBox;
        for (int i = 0; i < tab.getElements().size()-1; i++){
            if (i>0) yPos=(int)(distanceToFirstGUICheckBox+i*0.5f*GUI_CheckBox.NORMAL_HEIGHT);
            Vec2 elementPosition = new Vec2(tab.getWidth()/2, yPos);
            tab.getElements().get(i).setPosition(elementPosition);
        }
        yPos+=(int)(0.5f*GUI_Button.NORMAL_HEIGHT);
        tab.getElements().get(tab.getElements().size()-1).setPosition(new Vec2(tab.getWidth()/2, yPos));
        tab.recalculateHeight(null);
    }
*/

    protected void fillTabWithElementsFromUpToDown(androidGUI_ScrollableTab tab, boolean onlyOneSelected){
        int checkBoxes = 0;
        for (androidGUI_Element androidGui_element : tab.getElements()){
            if (androidGui_element.getClass() == androidAndroidGUI_CheckBox.class){
                checkBoxes++;
            }
        }
        if (checkBoxes < (selectedElements.size())){
            for (androidGUI_Element androidGui_element : tab.getElements()){
                if (androidGui_element.getName() == SELECT_ALL_ON_SCREEN_STRING || androidGui_element.getName().equals(SELECT_ALL_ON_SCREEN_STRING)){
                    tab.getElements().remove(androidGui_element);
                    break;
                }
            }
            Vec2 elementPosition = new Vec2(tab.getWidth()/2, 25f* Program.engine.width/ Program.DEBUG_DISPLAY_WIDTH);
            int checkBoxesStep = (int)(0.5f* androidAndroidGUI_CheckBox.NORMAL_HEIGHT);
            if (tab.getElements().size() > 0){
                if (tab.getElements().get(tab.getElements().size()-1).getName() == CLEAR_SELECTION) tab.getElements().remove(tab.getElements().size()-1);
                int lastElementPositionY = tab.getLastElementPosition();
                elementPosition.y = lastElementPositionY+checkBoxesStep;
            }
            boolean alreadyAdded = false;

            for (androidGUI_Element androidGui_element : tab.getElements()){
                androidAndroidGUI_CheckBox checkBox = (androidAndroidGUI_CheckBox) androidGui_element;
                if (selectedElements.get(selectedElements.size()-1).equals(checkBox)){
                    alreadyAdded = true;
                    System.out.println("This element was already added");
                }
            }
            if (!alreadyAdded) {
                String name = selectedElements.get(selectedElements.size() - 1).getSelectedObject().getObjectToDisplayName();
                androidAndroidGUI_CheckBox checkbox = new androidAndroidGUI_CheckBox(elementPosition, androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, name);
                checkbox.setFlagSet(true);
                tab.addGUI_Element(checkbox, checkbox.getPosition());
                selectedElements.get(selectedElements.size() - 1).setRelatedCheckBox(checkbox);
            }
            deleteRepeatingCheckBoxes(tab);
            int buttonsStep = (int)(0.75f* androidAndroidGUI_Button.NORMAL_HEIGHT);
            androidGUI_Element buttonManual = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SELECT_ALL_ON_SCREEN_STRING, false);
            tab.addGUI_Element(buttonManual, null);
            Vec2 clearButtonPosition = new Vec2(elementPosition.x, elementPosition.y+buttonsStep);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(clearButtonPosition, androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CLEAR_SELECTION, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
            newObjectWasAdded = false;
            System.out.println(" tab was reconstructed");
        }
        else {
            System.out.println("Too many checkboxes");
        }

    }

    private void deleteRepeatingSelectedElements() {
        ArrayList <SelectedElement> newElements = new ArrayList<>();
        for (int i = (selectedElements.size()-1); i >= 0; i--){
            for (SelectedElement selectedElement : newElements){
                if (selectedElement.equals(selectedElements.get(i))){

                }
            }
        }

    }

    private void deleteRepeatingCheckBoxes(androidGUI_ScrollableTab tab) {

        int checkBoxes = 0;
        for (androidGUI_Element androidGui_element : tab.getElements()){
            if (androidGui_element.getClass() == androidAndroidGUI_CheckBox.class){
                checkBoxes++;
            }
        }
        if (checkBoxes != (selectedElements.size())){
            if (checkBoxes > selectedElements.size()){
                System.out.println("Too many check boxes " + "; Boxes: " + checkBoxes + "; Selected: " + selectedElements.size());
            }
            else {
                System.out.println("Too few check boxes " + "; Boxes: " + checkBoxes + "; Selected: " + selectedElements.size());
                for (int i = (selectedElements.size()-1); i >= 0; i--) {
                    if (selectedElements.get(i).getCheckBox() == null){
                        selectedElements.remove(i);
                    }
                }
                System.out.println("Elements without checkboxes were deleted. Now: " + "; Boxes: " + checkBoxes + "; Selected: " + selectedElements.size());
            }
        }
        else System.out.println("Elements: " + selectedElements.size() + " checkBoxes: " + checkBoxes);
        for (int i = (selectedElements.size()-1); i >= 0; i--){
            int checkboxes = 0;
            androidGUI_Element testedCheckbox = selectedElements.get(i).getCheckBox();
            for (int j = (tab.getElements().size()-1); j>= 0;  j--){
                if (testedCheckbox.equals(tab.getElements().get(j))){
                    checkboxes++;
                    System.out.println("This element " + i + " has " + j + " checkbox ");
                }
                if (checkboxes > 1){
                    System.out.println("This element has two or more checkboxes and will have only one");
                    selectedElements.remove(j);
                }
            }
        }
    }


    protected void tabUpdating(ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElements, GameCamera editorCamera, GameRound gameRound) {
        if (releasedElements.size()>0) {
            if (Editor2D.localStatement > FIRST_ELEMENT_SELECTION) {
                for (int j = releasedElements.size()-1; j >= 0; j--) {
                    if (releasedElements.get(j).getName() == CLEAR_SELECTION) {
                        Editor2D.setNewLocalStatement(FIRST_ELEMENT_SELECTION);
                        SingleGameElement.resetTintValue();
                        for (int i = 0; i < SelectingAction.selectedElements.size(); i++) {
                            SelectingAction.selectedElements.get(i).getSelectedObject().setSelected(false);
                            SelectingAction.selectedElements.get(i).getSelectedObject().clearSelection();
                        }
                        tabController.getTab().clearElements();
                        selectedElements.clear();
                    }
                    else{
                        for (int i = 0; i < selectedElements.size(); i++){
                            if (selectedElements.get(i).getCheckBox().equals(releasedElements.get(j))){
                                selectedElements.get(i).getSelectedObject().clearSelection();
                                selectedElements.remove(i);
                                tabController.getTab().getElements().remove(releasedElements.get(j));

                                if (selectedElements.size()==0){
                                    selectedElements.clear();
                                    tabController.getTab().clearElements();
                                    Editor2D.setNewLocalStatement(FIRST_ELEMENT_SELECTION);
                                    System.out.println("It was the last element");
                                }
                                repositionGUIAlongY(tabController.getTab(), tabController.getTab().getElements());
                                break;
                            }
                        }
                        if (selectedElements.size() == 1){
                            addSelectedToClipboard(selectedElements.get(0));
                        }
                    }
                }
            }
            for (int j = (releasedElements.size()-1); j >= 0; j--) {
                if (releasedElements.get(j).getName() == SELECT_ALL_ON_SCREEN_STRING) {
                    selectAllVisibleObjects(editorCamera, gameRound, tabController.getTab());
                }
            }


        }
    }

    private void selectAllVisibleObjects(GameCamera editorCamera, GameRound gameRound, androidGUI_ScrollableTab tab) {
        float x1 = editorCamera.getDisplayBoard(GameCamera.LEFT_SIDE);
        float y1 = editorCamera.getDisplayBoard(GameCamera.UPPER_SIDE);
        float x2 = editorCamera.getDisplayBoard(GameCamera.RIGHT_SIDE);
        float y2 = editorCamera.getDisplayBoard(GameCamera.LOWER_SIDE);
        ArrayList <SingleGameElement > newSelectedElementsOnScreen = gameRound.getObjectsInRect(x1, y1, x2, y2);
        int addedCheckBoxes = 0;
        for (int i = 0; i < newSelectedElementsOnScreen.size(); i++){
            boolean alreadyExists = false;
            for (SelectedElement element : selectedElements) {
                if (element.getSelectedObject().equals(newSelectedElementsOnScreen.get(i))){
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                addedCheckBoxes++;
                SelectedElement selectedElement = new SelectedElement(newSelectedElementsOnScreen.get(i));
                selectedElements.add(selectedElement);
                selectedElement.getSelectedObject().setSelected(true);
                fillTabWithElementsFromUpToDown(tab, false);
            }
            else {
                System.out.println("This object was already selected");
            }

        }
        //System.out.println("Check boxes to be added: " + addedCheckBoxes);
        Editor2D.setNextLocalStatement();

        //protected void addNewObjectToSelection(ArrayList <SelectedElement> selectedElements , GameRound gameRound, Body bodyToFind){
    }

    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            if (textDataToBeShown.length()<1) {
                try {
                    if (Editor2D.localStatement == FIRST_ELEMENT_SELECTION) {
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Select at least one element");
                        onScreenConsole.setText(actualConsoleText);
                    }
                    else {
                        if (Editor2D.localStatement > FIRST_ELEMENT_SELECTION) {
                            ArrayList<String> actualConsoleText = new ArrayList<>();
                            actualConsoleText.add("Select more elements or clear selection");
                            onScreenConsole.setText(actualConsoleText);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Can not change the text of the console " + e);
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Successfully");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            else {
                try {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add(textDataToBeShown);
                    onScreenConsole.setText(actualConsoleText);
                }
                catch (Exception e) {
                    System.out.println("Can not change the text of the console " + e);
                }
                textDataToBeShown = "";
            }
        }
    }


    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        selectingController.draw(gameCamera, levelsEditorProcess);
    }


    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(tabController, levelsEditorProcess.getEditorCamera(), levelsEditorProcess.getGameRound());
        if (Editor2D.localStatement >= FIRST_ELEMENT_SELECTION){
            selectingController.update(levelsEditorProcess.getEditorCamera(), levelsEditorProcess);
            if (selectingController.canBeNewObjectAdded() && Editor2D.localStatement >= FIRST_ELEMENT_SELECTION){
                findSelectedObject(levelsEditorProcess, levelsEditorProcess.getEditorCamera(), tabController.getTab());
                selectingController.endSelection();
                if (selectedElements.size() == 1){
                    addSelectedToClipboard(selectedElements.get(0));
                }
            }
        }
        if (Editor2D.localStatement==FIRST_ELEMENT_SELECTION){

        }
        if (Editor2D.localStatement >= 126) Editor2D.localStatement = 2;

    }

    private void addSelectedToClipboard(SelectedElement selectedElement) {
        Program.iEngine.addStringToClippboard(selectedElement.getDataString());
        String name = selectedElement.getDataString();
        if (Program.OS == Program.ANDROID) {

        }
        else System.out.println(name + " was selected");
    }

    private void findSelectedObject(LevelsEditorProcess levelsEditorProcess, GameCamera gameCamera, androidGUI_ScrollableTab tab) {
        if (LevelsEditorProcess.isPointOnMapZone(new PVector(Program.engine.mouseX, Program.engine.mouseY)) == true){
            Vec2 nearestPoint = new Vec2(levelsEditorProcess.getPointInWorldPosition(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY)).x, levelsEditorProcess.getPointInWorldPosition(gameCamera, new PVector(Program.engine.mouseX, Program.engine.mouseY)).y);
            boolean objectWithBodyFounded = selectElementWithBody(levelsEditorProcess, nearestPoint, tab);
            boolean graphicFounded = findGraphicElementUnderSelection(levelsEditorProcess.getGameRound(),  nearestPoint, tab);
            findPortalsUnderSelection(levelsEditorProcess.getGameRound(),  nearestPoint, tab);
            findZonesUnderSelection(levelsEditorProcess.getGameRound(), nearestPoint, tab);
        }
    }

    private boolean selectElementWithBody(LevelsEditorProcess levelsEditorProcess, Vec2 nearestPoint, androidGUI_ScrollableTab tab){
        Vec2 testPoint = PhysicGameWorld.controller.coordPixelsToWorld(nearestPoint);
        boolean founded = false;
        for (Body b = PhysicGameWorld.controller.world.getBodyList(); b!=null; b=b.getNext()) {
            for (Fixture f = b.getFixtureList(); f!=null; f=f.getNext()) {
                if (f.testPoint(testPoint)) {
                    SelectedElement newElement = addNewObjectWithBodyToSelection(selectedElements, levelsEditorProcess.getGameRound(), b);
                    if (newElement != null){
                        Editor2D.setNextLocalStatement();
                        newElement.getSelectedObject().setSelected(true);
                        founded = true;
                        fillTabWithElementsFromUpToDown(tab, false);
                    }
                }
            }
        }
        return founded;
    }

    private void findZonesUnderSelection(GameRound gameRound, Vec2 nearestPoint, androidGUI_ScrollableTab tab) {
        ArrayList <MessageAddingZone> messageAddingZones = gameRound.getMessageAddingZones();
        for (int i = (messageAddingZones.size()-1); i >= 0; i--){
            if (messageAddingZones.get(i).getFlag().isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, messageAddingZones.get(i), tab);
                System.out.println("New message adding zone was selected");
            }
        }
        ArrayList <ObjectsClearingZone> objectClearingZones = gameRound.getObjectsClearingZones();
        for (int i = (objectClearingZones.size()-1); i >= 0; i--){
            if (objectClearingZones.get(i).getFlag().isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, objectClearingZones.get(i), tab);
                System.out.println("New objects clearing zone was selected");
            }
        }
        ArrayList <CameraFixationZone> cameraFixationZones = gameRound.getCameraFixationZones();
        for (int i = (cameraFixationZones.size()-1); i >= 0; i--){
            if (cameraFixationZones.get(i).getFlag().isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, cameraFixationZones.get(i), tab);
                System.out.println("New camera fixation zone was selected");
            }
        }
        ArrayList <AbstractTrigger> triggers = gameRound.getTriggers();
        for (int i = (triggers.size()-1); i >= 0; i--){
            if (triggers.get(i).getFlag().isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, triggers.get(i), tab);
                System.out.println("New trigger was selected");
            }
        }
        /*
        ArrayList <PortalToAnotherLevel> portalsToAnotherLevel = gameRound.getPortalsToAnotherLevel();
        for (int i = (portalsToAnotherLevel.size()-1); i >= 0; i--){
            if (portalsToAnotherLevel.get(i).enter.isPointIn(nearestPoint) || portalsToAnotherLevel.get(i).exit.isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, portalsToAnotherLevel.get(i));
                System.out.println("New portal to another level was selected");
            }
        }*/
    }

    private void findPortalsUnderSelection(GameRound gameRound, Vec2 nearestPoint, androidGUI_ScrollableTab tab) {
        ArrayList <PipePortal> portals = gameRound.getPortals();
        for (int i = (portals.size()-1); i >= 0; i--){
            if (portals.get(i).enter.isPointIn(nearestPoint) || portals.get(i).exit.isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, portals.get(i), tab);
                System.out.println("New pipe portal was selected");
            }
        }
        ArrayList <SimplePortal> simplePortals = gameRound.getSimplePortals();
        for (int i = (simplePortals.size()-1); i >= 0; i--){
            if (simplePortals.get(i).enter.isPointIn(nearestPoint) || simplePortals.get(i).exit.isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, simplePortals.get(i), tab);
                System.out.println("New simple portal was selected");
            }
        }
        ArrayList <PortalToAnotherLevel> portalsToAnotherLevel = gameRound.getPortalsToAnotherLevel();
        for (int i = (portalsToAnotherLevel.size()-1); i >= 0; i--){
            if (portalsToAnotherLevel.get(i).enter.isPointIn(nearestPoint) || portalsToAnotherLevel.get(i).exit.isPointIn(nearestPoint)){
                addNewObjectToSelection(gameRound, portalsToAnotherLevel.get(i), tab);
                System.out.println("New portal to another level was selected");
            }
        }
    }

    private void addNewObjectToSelection(GameRound gameRound, SingleGameElement portal, androidGUI_ScrollableTab tab){
        boolean wasAlreadyAdded = false;
        for (int i = 0; i < selectedElements.size(); i++){
            if (selectedElements.get(i).getSelectedObject().equals(portal)){
                wasAlreadyAdded = true;
            }
        }
        if (wasAlreadyAdded){
            System.out.println("This element was already added");
            textDataToBeShown = "This object was already selected";
        }
        else {
            SelectedElement selectedElement = new SelectedElement(portal);
            System.out.println("Element: " + portal.getClass() + " was selected");
            selectedElements.add(selectedElement);
            Editor2D.setNextLocalStatement();
            portal.setSelected(true);
            fillTabWithElementsFromUpToDown(tab, false);
        }
    }

    private boolean findGraphicElementUnderSelection(GameRound gameRound, Vec2 nearestPoint, androidGUI_ScrollableTab tab) {
        boolean founded = false;
        for (IndependentOnScreenStaticSprite graphicElement : gameRound.getIndependentOnScreenStaticSprites()){
            if (graphicElement.isPointOnElement(nearestPoint)){
                founded = true;
                addNewObjectToSelection(gameRound, graphicElement, tab);

            }
        }
        for (IndependentOnScreenAnimation graphicElement : gameRound.getIndependentOnScreenAnimations()){
            if (graphicElement.isPointOnElement(nearestPoint)){
                founded = true;
                addNewObjectToSelection(gameRound, graphicElement, tab);
                //fillTabWithElementsFromUpToDown(tab, false);
            }
        }
        return founded;
    }

    private void addNewObjectToSelection(GameRound gameRound, IndependentOnScreenGraphic graphic, androidGUI_ScrollableTab tab){
            boolean wasAlreadyAdded = false;
            for (int i = 0; i < selectedElements.size(); i++){
                if (selectedElements.get(i).getSelectedObject().equals(graphic)){
                    wasAlreadyAdded = true;
                }
            }
            if (wasAlreadyAdded){
                System.out.println("This element was already added");
                textDataToBeShown = "This object was already selected";
            }
            else {
                SelectedElement selectedElement = new SelectedElement(graphic);
                System.out.println("Element: " + graphic.getClass() + " was selected");
                selectedElements.add(selectedElement);
                Editor2D.setNextLocalStatement();
                graphic.setSelected(true);
                fillTabWithElementsFromUpToDown(tab, false);
            }
        try{
            System.out.println("Object has pos: " + graphic.getStringData() + "; with pos: " + graphic.getPosition());
        }
        catch(Exception e){
            System.out.println("Can not get object data");
        }
    }



    @Override
    public byte getEndValue(){
        return END;
    }

}
