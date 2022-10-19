package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.Gumba;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.textes.DissolvingAndUpwardsMovingText;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.loading.DeleteStringsMaster;
import com.mgdsstudio.blueberet.loading.LevelDataStringDecoder;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.DebugVersionCreatingMaster;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;

import java.util.ArrayList;

public abstract class SubmenuAction implements RoundElementAddingConstants{
    private final static String DEFAULT_TILESET_FOR_EDITOR = "Tileset5";

    //After recreation data
    protected GameObjectDataForStoreInEditor objectData;


    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 7;
    protected final static String BEHIND_OF_ALL = "Behind everything";
    protected final static String BEHIND_OF_PERSONS = "Behind characters";
    protected final static String IN_FRONT_OF_ALL = "In front of all";

    public final static String CANCEL = "Cancel";
    public final static String APPLY = "Apply";
    public final  static String BACK = "Previous menu";
    public final static String NEXT = "Next menu";
    protected final int distanceToFirstGUICheckBox = (int)(Program.engine.width/13.3f);
    protected boolean fileManagerOpened = false;
    protected boolean fileManagerClosed = false;

    protected static String tilesetStartName = "Tileset";
    protected static String tilesetExtension = "png";
    protected static String backgroundStartName = Background.CLASS_NAME;
    protected static String [] backgroundExtensions = {"png", "jpg", "jpeg"};

    protected final boolean WIDTH = true;
    protected final boolean HEIGHT = false;

    protected MapZone mapZone;
    protected static ArrayList<String> actualConsoleText = new ArrayList<>();
    //protected ScrollableTabController scrollableTabController;
    //protected GameObjectDataForStoreInEditor objectData;
    //protected byte globalStatement;

    public SubmenuAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData){
        this.mapZone = mapZone;
        this.objectData = objectData;
    }

    protected void zoneCreating(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, byte linkTo, boolean graphicType) {
        if (tab.getTilesetZone() == null) {
            System.out.println("Zone was added ");
            float GUI_Height = 1.35f* androidAndroidGUI_Button.NORMAL_HEIGHT;
            if (tab.getElements().size()>0) GUI_Height = 1.4f*tab.getElements().get(0).getHeight();
            TilesetZone tilesetZone = new TilesetZone(objectData.getPathToTexture(), tab, new Vec2(0,0), new Vec2(tab.getWidth()*7.2f/9f, tab.getHeight() - 1 * ScrollableTabController.MINIMAL_FREE_SPACE - GUI_Height*1.1f), linkTo, graphicType);
            tab.setTilesetZone(tilesetZone);
        }
    }

    protected final void createAngleChoosingMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element element = new androidAndroidGUI_AnglePicker(new Vec2(((tab.getWidth() / 2)), Program.engine.width/5.37f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2.5f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 2.5f));
        tab.addGUI_Element(element, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.46f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Apply", false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Program.engine.width/2.46f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);
        tab.recalculateHeight(null);
    }

    protected final void createFillOrStringTextureMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, STRETCH_GRAPHIC, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FILL_WITH_TILES, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    protected void repositionGUIAlongInTwoCollumns(androidGUI_ScrollableTab tab, ArrayList<androidGUI_Element> elements){
        float lastElementPosY = 0f;
        int additionalHeight = 0;
        float distanceToFirstElement = elements.get(0).getHeight()*0.6f;
        for (int i = 0; i < elements.size(); i++){
            int xPos = 0;
            int yPos = 0;
            if (((i + 2) % 2) == 0) {
                xPos = tab.getWidth() / 4;
                yPos = (int) (distanceToFirstElement + 1 * distanceToFirstElement * i);
            }
            else {
                xPos = 3 * tab.getWidth() / 4;
                yPos = (int) (distanceToFirstElement + 1 * distanceToFirstElement * (i - 1));

            }
            Vec2 newPos = new Vec2(xPos, yPos);
            elements.get(i).setPosition(newPos);
        }
    }
    
    private void deleteObjectsFromMap(LevelsEditorProcess levelsEditorProcess, SelectedElement selectedElement){
        levelsEditorProcess.getGameRound().deleteObjectsFromMap(selectedElement.getSelectedObject());       //Deleting from the world
        System.out.println("Object was deleted from the actual world");
    }

    protected void addTextForDeletedObject(GameRound gameRound, SingleGameElement toBeDeleted, String suffix){
        Vec2 pos = new Vec2(0,0);
        String text = "";
        if (toBeDeleted instanceof GameObject){
            GameObject object = (GameObject) toBeDeleted;
            pos.x = object.getPixelPosition().x;
            pos.y = object.getPixelPosition().y;
            text = toBeDeleted.getObjectToDisplayName();
        }
        else if (toBeDeleted instanceof IndependentOnScreenGraphic){
            IndependentOnScreenGraphic object = (IndependentOnScreenGraphic) toBeDeleted;
            pos.x = object.getPosition().x;
            pos.y = object.getPosition().y;
            text = object.getObjectToDisplayName();
        }
        text+=suffix;
        DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(pos.x, pos.y, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, text, DissolvingAndUpwardsMovingText.NORMAL_DISSOLVING_TIME, DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, 0, DissolvingAndUpwardsMovingText.GRAY);
        gameRound.addNewDissolvingText(dissolvingText);
    }

    protected void deleteObjects(LevelsEditorProcess levelsEditorProcess, String text) {
        GameRound gameRound = levelsEditorProcess.getGameRound();
        for (int i = (SelectingAction.selectedElements.size()-1); i >= 0; i--) {
            SingleGameElement toBeDeleted = SelectingAction.selectedElements.get(i).getSelectedObject();
            if (toBeDeleted.getClass() != Soldier.class) {
                boolean inBridge = false;
                if (toBeDeleted instanceof RoundElement) {
                    if (gameRound.isObjectPartOfSomeBridge(toBeDeleted)) {
                        inBridge = true;
                    }
                }
                if (!inBridge) {
                    deleteObjectsFromMap(levelsEditorProcess, SelectingAction.selectedElements.get(i));
                    addTextForDeletedObject(gameRound, SelectingAction.selectedElements.get(i).getSelectedObject(), text);
                    //addTextForDeletedObject(gameRound, SelectingAction.selectedElements.get(i).getSelectedObject());
                    for (int j = (Editor2D.getNewObjectsData().size() - 1); j >= 0; j--) {                                                      //Deleting from the data to be saved
                        String testDataString = Editor2D.getNewObjectsData().get(j).getDataString();
                        boolean elementFounded = false;
                        if (testDataString.equals(SelectingAction.selectedElements.get(i).getSelectedObject().getStringData())) {
                            System.out.println("Element in the buffer with string " + SelectingAction.selectedElements.get(i).getSelectedObject().getStringData() + "was deleted");
                            elementFounded = true;
                        }
                        if (!elementFounded)
                            System.out.println("Was: " + testDataString + " is not : " + SelectingAction.selectedElements.get(i).getSelectedObject().getStringData());
                    }
                    ArrayList<GameObjectDataForStoreInEditor> dataInBuffer = Editor2D.getNewObjectsData();
                    boolean objectHasDataInUnsavedBuffer = false;
                    for (int j = (dataInBuffer.size() - 1); j >= 0; j--) {
                        if (dataInBuffer.get(j).getDataString().equals(SelectingAction.selectedElements.get(i).getSelectedObject().getStringData()) || dataInBuffer.get(j).getDataString() == (SelectingAction.selectedElements.get(i).getSelectedObject().getStringData())) {
                            System.out.println("This object was in the save buffer and will be deleted");
                            dataInBuffer.remove(j);
                            objectHasDataInUnsavedBuffer = true;
                        }
                    }
                    if (!objectHasDataInUnsavedBuffer)
                        System.out.println("This object wasn't founded in the save buffer. Name was: " + SelectingAction.selectedElements.get(i).getSelectedObject().getStringData());
                    DeleteStringsMaster deleteStringsMaster = new DeleteStringsMaster(Program.actualRoundNumber, LoadingMaster.USER_LEVELS);
                    deleteStringsMaster.deleteString(SelectingAction.selectedElements.get(i).getSelectedObject().getStringData());
                }
            }
        }
    }

    /*
    protected void updateNewGraphicAdding(){
        if (Editor2D.fileWasChoosen){
            System.out.println("File was choosen");
            if (Editor2D.isChosenFilePicture()){
                String prefix = "";
                if (Program.OS == Program.WINDOWS) {
                    prefix = Program.getRelativePathToAssetsFolder();
                }
                String newBackground = prefix+"Background-128.jpg";
                File backgroundFile = new File(newBackground);
                Editor2D.copyFile(Editor2D.getPathToOpenedFileFromUser(), backgroundFile);
            }
            Editor2D.fileWasChoosen = false;
        }
    }*/

    protected void repositionGUIAlongY(androidGUI_ScrollableTab tab, ArrayList<androidGUI_Element> elements){
        float lastElementPosY = 0f;
        if (elements.size()>0) lastElementPosY = elements.get(0).getHeight()*1.0f;
        for (int i = 0; i < elements.size(); i++){
            int additionalHeight = 0;
            if (elements.get(i).getClass() == androidAndroidGUI_AnglePicker.class){
                additionalHeight = elements.get(i).getHeight();
            }
            if (elements.get(i).getClass() == androidAndroidGUI_Slider.class){
                additionalHeight = (int)(elements.get(i).getHeight()*1.3f);
            }
            if (i>0) {
                lastElementPosY+=additionalHeight+elements.get(i).getHeight()/1.5f;
            }
            Vec2 newPos = new Vec2(tab.getWidth()/2, lastElementPosY);
            elements.get(i).setPosition(newPos);
            if (i<(elements.size()-1))  lastElementPosY+=elements.get(i+1).getHeight()/1.5f;
        }
        /*
        float lastElementPosY = 0f;
        if (elements.size()>0) lastElementPosY = elements.get(0).getHeight()*1.0f;
        int additionalHeight = 0;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == GUI_AnglePicker.class){
                additionalHeight = elements.get(i).getHeight();
            }
            if (elements.get(i).getClass() == GUI_Slider.class){
                additionalHeight = (int)(elements.get(i).getHeight()*1.3f);
            }
        }
        for (int i = 0; i < elements.size(); i++){
            if (i>0) {
                lastElementPosY+=additionalHeight+elements.get(i).getHeight()/1.5f;
            }
            Vec2 newPos = new Vec2(tab.getWidth()/2, lastElementPosY);
            elements.get(i).setPosition(newPos);
            if (i<(elements.size()-1))  lastElementPosY+=elements.get(i+1).getHeight()/1.5f;
        }
        */
    }

    public abstract void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData);



    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess) {

    }

    public abstract void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement);

    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){

    }

    public abstract void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl);

    protected boolean mustBeTabReconstructed(){
        if (Editor2D.isGlobalStatementChanged() || Editor2D.isLocalStatementChanged()){
            return true;
        }
        else return false;
    }

    private int calculateWidthForFigure(byte figureNumber, LevelsEditorProcess levelsEditorProcess){
        int width = 0;
        if (figureNumber == 1){
            if (levelsEditorProcess.pointsOnMap.get(1).getPosition().x>levelsEditorProcess.pointsOnMap.get(0).getPosition().x){
                width = (int) PApplet.abs(levelsEditorProcess.pointsOnMap.get(1).getPosition().x-levelsEditorProcess.pointsOnMap.get(0).getPosition().x);
            }
            else {
                width = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(0).getPosition().x-levelsEditorProcess.pointsOnMap.get(1).getPosition().x);
            }
        }
        else {
            if (levelsEditorProcess.pointsOnMap.get(3).getPosition().x>levelsEditorProcess.pointsOnMap.get(2).getPosition().x){
                width = (int) PApplet.abs(levelsEditorProcess.pointsOnMap.get(3).getPosition().x-levelsEditorProcess.pointsOnMap.get(2).getPosition().x);
            }
            else {
                width = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(2).getPosition().x-levelsEditorProcess.pointsOnMap.get(3).getPosition().x);
            }
        }
        return width;
    }

    private int calculateHeightForFigure(byte figureNumber, LevelsEditorProcess levelsEditorProcess){
        int height = 0;
        if (figureNumber == 1){
            if (levelsEditorProcess.pointsOnMap.get(1).getPosition().y>levelsEditorProcess.pointsOnMap.get(0).getPosition().y){
                height = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(1).getPosition().y-levelsEditorProcess.pointsOnMap.get(0).getPosition().y);
            }
            else {
                height = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(0).getPosition().y-levelsEditorProcess.pointsOnMap.get(1).getPosition().y);
            }
        }
        else {
            if (levelsEditorProcess.pointsOnMap.get(3).getPosition().y>levelsEditorProcess.pointsOnMap.get(0).getPosition().y){
                height = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(3).getPosition().y-levelsEditorProcess.pointsOnMap.get(2).getPosition().y);
            }
            else {
                height = (int)PApplet.abs(levelsEditorProcess.pointsOnMap.get(2).getPosition().y-levelsEditorProcess.pointsOnMap.get(3).getPosition().y);
            }
        }
        return height;
    }
    /*
    protected void addFigureOnMapZoneAndSaveData(byte figureNumber, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        Vec2 center = null;
        int width = calculateWidthForFigure(figureNumber, levelsEditorProcess);
        int height = calculateHeightForFigure(figureNumber, levelsEditorProcess);
        System.out.println("Figure was created. Points: " + levelsEditorProcess.pointsOnMap.size());
        if (figureNumber == 1){
            center = GameMechanics.getAlignedRectCenter(levelsEditorProcess.pointsOnMap.get(0).getPosition(), levelsEditorProcess.pointsOnMap.get(1).getPosition());
        }
        else if (figureNumber == 2){
            center = GameMechanics.getAlignedRectCenter(levelsEditorProcess.pointsOnMap.get(2).getPosition(), levelsEditorProcess.pointsOnMap.get(3).getPosition());
        }
        if (figureNumber == 1) {
            objectData.setPosition(new Vec2(center.x, center.y));
        }
        else if (figureNumber == 2) objectData.setSecondPosition(new Vec2(center.x, center.y));

        ArrayList <Point> points = new ArrayList<>();
        if (figureNumber == 1){
            objectData.setWidth(width);
            objectData.setHeight(height);
            points.add(levelsEditorProcess.pointsOnMap.get(0));
            points.add(levelsEditorProcess.pointsOnMap.get(1));
        }
        else if (figureNumber == 2) {
            objectData.setSecondWidth(width);
            objectData.setSecondHeight(height);
            points.add(levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size()-2));
            points.add(levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size()-1));
        }
        Figure figure = new Figure(points, center, Figure.RECTANGULAR_SHAPE);
        levelsEditorProcess.addNewFigure(figure);
        System.out.println("Figures on map: " + levelsEditorProcess.figures.size() + " points on map: " + levelsEditorProcess.pointsOnMap.size());
        for (Point point : levelsEditorProcess.pointsOnMap){
            point.hide(true);
        }
    }
     */


    protected void addRectFigureOnMapZoneAndSaveData(byte figureNumber, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        Vec2 center = null;
        int width = calculateWidthForFigure(figureNumber, levelsEditorProcess);
        int height = calculateHeightForFigure(figureNumber, levelsEditorProcess);
        System.out.println("Figure was created. Points: " + levelsEditorProcess.pointsOnMap.size());
        if (figureNumber == 1){
            center = GameMechanics.getAlignedRectCenter(levelsEditorProcess.pointsOnMap.get(0).getPosition(), levelsEditorProcess.pointsOnMap.get(1).getPosition());
        }
        else if (figureNumber == 2){
            center = GameMechanics.getAlignedRectCenter(levelsEditorProcess.pointsOnMap.get(2).getPosition(), levelsEditorProcess.pointsOnMap.get(3).getPosition());
        }
        if (figureNumber == 1) {
            objectData.setPosition(new Vec2(center.x, center.y));
        }
        else if (figureNumber == 2) objectData.setSecondPosition(new Vec2(center.x, center.y));

        ArrayList <Point> points = new ArrayList<>();
        if (figureNumber == 1){
            objectData.setWidth(width);
            objectData.setHeight(height);
            points.add(levelsEditorProcess.pointsOnMap.get(0));
            points.add(levelsEditorProcess.pointsOnMap.get(1));
        }
        else if (figureNumber == 2) {
            objectData.setSecondWidth(width);
            objectData.setSecondHeight(height);
            points.add(levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size()-2));
            points.add(levelsEditorProcess.pointsOnMap.get(levelsEditorProcess.pointsOnMap.size()-1));
        }
        Figure figure = new Figure(points, center, Figure.RECTANGULAR_SHAPE);
        levelsEditorProcess.addNewFigure(figure);
        System.out.println("Figures on map: " + levelsEditorProcess.figures.size() + " points on map: " + levelsEditorProcess.pointsOnMap.size());
        for (Point point : levelsEditorProcess.pointsOnMap){
            point.hide(true);
        }
    }

    protected void updateFileInput() {
        if (!fileManagerOpened) {
            System.out.println("Open file manager");
            //fileManagerOpened = true;
            //Editor2D.initFileLoader();
        }

    }

    protected boolean mustBeElementMadeSmaller(GameObjectDataForStoreInEditor objectData){
        return false;
        /*
        if (mustBeElementMadeSmaller(objectData.getWidth(), objectData.getHeight(), objectData.getAngle())) {
            return true;
        }
        return false;*/
    }

    protected boolean mustBeElementMadeSmaller(int width, int height, int angle){
        /*
        if (angle == 0 || angle == 90 || angle == 180 || angle == 270 || angle == -90 || angle == -270) {
            if (width > 5 && height > 5) {
                return true;
            }
            return false;
        }
        */
        return false;
    }

    public void clearRedundantObjects(GameRound gameRound){
        System.out.println("Nothing to clear for this menu " + this.getClass());
    }

    protected void makePauseToNextOperation(){
        //Editor2D.resetTimer();
    }

    protected final void setTextForConsole(OnScreenConsole onScreenConsole, String text){
        boolean doNotUpdate = false;
        if (onScreenConsole.getText().size()==1){
            if (onScreenConsole.getText().get(0).equals(text) || onScreenConsole.getText().get(0).equals(text)){
                doNotUpdate = true;
            }
        }
        if (doNotUpdate == false){
            ArrayList<String> actualConsoleText = new ArrayList<>();
            actualConsoleText.add(text);
            onScreenConsole.recalculateFontDimension();
            onScreenConsole.setText(actualConsoleText);
        }
    }

    public abstract byte getEndValue();


    public void dispose(LevelsEditorProcess levelsEditorProcess) {
        System.out.println("This menu "+ this.getClass() +"  is closed. Nothing must be cleared");
        //Program.gameStatement = Program.MAIN_MENU;
    }

    protected SelectedElement addNewObjectWithBodyToSelection(ArrayList <SelectedElement> selectedElements , GameRound gameRound, Body bodyToFind){
        GameObject gameElement = PhysicGameWorld.getGameObjectByBody(gameRound, bodyToFind);
        if (gameElement != null) {
            boolean wasAlreadyAdded = false;
            for (int i = 0; i < selectedElements.size(); i++){
                if (selectedElements.get(i).getSelectedObject().equals(gameElement)){
                    wasAlreadyAdded = true;
                }
            }
            if (wasAlreadyAdded){

            }
            else {
                SelectedElement selectedElement = new SelectedElement(gameElement);
                System.out.println("Element: " + gameElement.getClass() + " was selected");
                selectedElements.add(selectedElement);
                Editor2D.setNextLocalStatement();
                gameElement.setSelected(true);
                return selectedElement;
            }
        }
        try{
            System.out.println("Object has pos: " + gameElement.getStringData() + "; with pos: " + gameElement.getPixelPosition());
        }
        catch(Exception e){
            System.out.println("Can not get object data");
        }
        return null;
    }

    protected SelectedElement addNewObjectWithoutBodyToSelection(ArrayList <SelectedElement> selectedElements , GameRound gameRound, SingleGameElement singleGameElement){
        if (singleGameElement != null) {
            boolean wasAlreadyAdded = false;
            for (int i = 0; i < selectedElements.size(); i++){
                if (selectedElements.get(i).getSelectedObject().equals(singleGameElement)){
                    wasAlreadyAdded = true;
                }
            }
            if (wasAlreadyAdded){

            }
            else {
                SelectedElement selectedElement = new SelectedElement(singleGameElement);
                selectedElements.add(selectedElement);
                Editor2D.setNextLocalStatement();
                singleGameElement.setSelected(true);
                System.out.println("Element was selected again " + selectedElement.getSelectedObject());
                return selectedElement;
            }
        }
        try{
            //System.out.println("Object has pos: " + singleGameElement.getStringData() + "; with pos: " + singleGameElement.getPixelPosition());
        }
        catch(Exception e){
            System.out.println("Can not get object data");
        }
        return null;
    }

    protected int[] getValuesFromString(String oldObjectDataString, LevelDataStringDecoder stringDecoder){
        String className = stringDecoder.getClassNameFromDataString();
        LevelDataStringDecoder decoder = new LevelDataStringDecoder(stringDecoder.getTextDataFromDataString(oldObjectDataString, className));
        int[] values;
        System.out.println("Class name for the object to be copied: " + className);
        if (className == IndependentOnScreenStaticSprite.CLASS_NAME || className.equals(IndependentOnScreenStaticSprite.CLASS_NAME) || className == IndependentOnScreenAnimation.CLASS_NAME || className.equals(IndependentOnScreenAnimation.CLASS_NAME) ){
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
        }
        else {
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
        }
        return values;
    }

    protected int getElementDimension(String className, String oldObjectDataString, LevelDataStringDecoder decoder, boolean widthOrHeight){
        int dimension = 80;
        int[] values;
        if (className == IndependentOnScreenStaticSprite.CLASS_NAME || className.equals(IndependentOnScreenStaticSprite.CLASS_NAME) || className == IndependentOnScreenAnimation.CLASS_NAME || className.equals(IndependentOnScreenAnimation.CLASS_NAME) ){
            values = decoder.getGraphicData(LoadingMaster.GRAPHIC_NAME_END_CHAR, LoadingMaster.DIVIDER_BETWEEN_GRAPHIC_DATA);
            if (widthOrHeight == WIDTH) dimension = values[4];
            else dimension = values[5];
        }
        else {
            values = decoder.getValues(LoadingMaster.MAIN_DATA_START_CHAR, LoadingMaster.DIVIDER_BETWEEN_VALUES, LoadingMaster.GRAPHIC_NAME_START_CHAR);
            if (className == RoundBox.CLASS_NAME || className.equals(RoundBox.CLASS_NAME)) {
                if (widthOrHeight == WIDTH) dimension = values[3];
                else dimension = values[4];
            }
            else if (className == Gumba.CLASS_NAME || className.equals(Gumba.CLASS_NAME)) {
                if (values.length > 4) dimension = values[4];
                else dimension = Gumba.NORMAL_DIAMETER;
            }
        }
        return dimension;
    }

    protected void updateCancelPressing(androidGUI_Element element) {
        if (element.getName().equals(CANCEL)){
            Editor2D.setNewLocalStatement((byte) 0);
        }
    }

    protected void createClearMenuWithCancelButton(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element cancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(cancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    protected void createNewOrExistingTilesetMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, USE_EXISTING_GRAPHIC, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, LOAD_NEW_GRAPHIC, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonWithoutGraphic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WITHOUT_GRAPHIC, false);
        tab.addGUI_Element(buttonWithoutGraphic, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    protected final void createMenuWithSliderAndTextField(androidGUI_ScrollableTab tab, String sliderMaxText, String textFieldText, int min, int max, int startValue){
        tab.clearElements();
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , "", min, max);
        if (sliderMaxText != null) slider.setText(sliderMaxText);
        else slider.setText(Integer.toString(max));
        slider.setUserValue(startValue);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), Program.engine.width/3.918f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, textFieldText, true, min, max);
        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        tab.recalculateHeight(tab.getElements());
    }


    protected final void createGraphicFileInDirectoryChoosingMenu(androidGUI_ScrollableTab tab, String startNameToFind, String extension){
        tab.clearElements();
        ArrayList<String> filesInGameDirectory = StringLibrary.getFilesListInAssetsFolder();
        ArrayList<String> filesInCache = null;
        if (!DebugVersionCreatingMaster.USE_GRAPHIC_FROM_CACHE || Program.OS == Program.DESKTOP) filesInCache = new ArrayList<>();
        else filesInCache = StringLibrary.getFilesListInCache();
        ArrayList<String> tilesets = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, startNameToFind);
        //ArrayList<String> tilesetsInCache = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, startNameToFind);
        ArrayList<String> imagesWithPrefix = StringLibrary.leaveInArrayFilesWithExtension(tilesets, extension);
        if (DebugVersionCreatingMaster.USE_GRAPHIC_FROM_CACHE){
            boolean containsDefault = false;
            for (String string : imagesWithPrefix){

                if (string.contains(DEFAULT_TILESET_FOR_EDITOR)){
                    containsDefault = true;
                    System.out.println("Assets dir contains default tileset");
                }
            }
            if (containsDefault){
                for (int i = (imagesWithPrefix.size()-1); i >= 0; i--){
                    if (!imagesWithPrefix.get(i).contains(DEFAULT_TILESET_FOR_EDITOR)){
                        imagesWithPrefix.remove(i);
                    }
                }
            }
            else System.out.println("There are no string with name: " + DEFAULT_TILESET_FOR_EDITOR);
        }
        ArrayList<String> imagesWithPrefixInCache = StringLibrary.leaveInArrayFilesWithExtension(filesInCache, extension);
        ArrayList<String> images = new ArrayList<>();

        ArrayList<String> filesInCacheAndAssetsFolders = new ArrayList<>();
        for (String string : imagesWithPrefix){
            filesInCacheAndAssetsFolders.add(string);
        }
        for (String string : imagesWithPrefixInCache){
            filesInCacheAndAssetsFolders.add(string);
        }
        for (int i = 0; i < filesInCacheAndAssetsFolders.size(); i++) {
            System.out.println("Created " + filesInCacheAndAssetsFolders.size() + " buttons");
            images.add(StringLibrary.deleteAssetsFromPath(filesInCacheAndAssetsFolders.get(i)));
            androidGUI_Element button = new androidAndroidGUI_ImageButton(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 1.45f), "", false, images.get(i), true);
            tab.addGUI_Element(button, null);
        }
        androidGUI_Element button = new androidAndroidGUI_Button(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR), CANCEL, false);
        tab.addGUI_Element(button, null);
        repositionGUIAlongInTwoCollumns(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    protected void createTextureRegionChoosingMenu(androidGUI_ScrollableTab tab){
        tab.setMinimalHeight();
        tab.clearElements();

        float buttonHeight = androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f;
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), tab.getHeight()), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (buttonHeight), APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), tab.getHeight()), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f), CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        tab.recalculateHeight(tab.getElements());
        float yButtonPos = tab.getHeight() - buttonHeight;
        buttonApply.setPosition(new Vec2(((tab.getWidth() / 4)), yButtonPos));
        buttonCancel.setPosition(new Vec2(((3 * tab.getWidth() / 4)), yButtonPos));
        tab.setScrollable(false);
    }

    protected void addSliderWithCopeledTextField(androidGUI_ScrollableTab tab, int startValue, int min, int max, String sliderMaxText, String textFieldText){
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , "", min, max);
        if (sliderMaxText != null) slider.setText(sliderMaxText);
        else slider.setText(Integer.toString(max));
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), Program.engine.width/3.918f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, textFieldText, true, min, max);
        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);
        slider.setUserValue(startValue);
    }

    /*
    protected void replaceStringInDataFile(String sourceObjectDataString, String stringData) {

    }*/


    protected void createLayerChoosingMenuWithApplyButton(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BEHIND_OF_ALL, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BEHIND_OF_PERSONS, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonInFront = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, IN_FRONT_OF_ALL, false);
        tab.addGUI_Element(buttonInFront, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    protected void createLayerChoosingMenuWithBackAndNextButton(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BEHIND_OF_ALL, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BEHIND_OF_PERSONS, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonInFront = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, IN_FRONT_OF_ALL, false);
        tab.addGUI_Element(buttonInFront, null);
        androidGUI_Element buttonNext = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, NEXT, false);
        tab.addGUI_Element(buttonNext, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    protected void addNewDissolvingTextToTheLastSelectedObject(GameRound gameRound, String text) {
        Vec2 pos = getPixelPosForLastSelectedElement();
        float x = pos.x;
        float y = pos.y;
        int valueType = 0;
        DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(x, y, DissolvingAndUpwardsMovingText.NORMAL_Y_VELOCITY, text, DissolvingAndUpwardsMovingText.NORMAL_DISSOLVING_TIME, DissolvingAndUpwardsMovingText.NORMAL_STAGES_NUMBER, valueType);
        gameRound.addNewDissolvingText(dissolvingText);
    }

    protected Vec2 getPixelPosForLastSelectedElement(){
        if (SelectingAction.selectedElements.size() > 0){
            SingleGameElement singleGameElement = SelectingAction.selectedElements.get(SelectingAction.selectedElements.size()-1).getSelectedObject();
            if (singleGameElement instanceof IndependentOnScreenGraphic){
                IndependentOnScreenGraphic graphic = (IndependentOnScreenGraphic) singleGameElement;
                return graphic.getPosition();
            }
            else if (singleGameElement instanceof RoundElement){
                RoundElement roundElement = (RoundElement) singleGameElement;
                return new Vec2(roundElement.getPixelPosition().x, roundElement.getPixelPosition().y);
            }
            else if (singleGameElement instanceof GameObject){
                GameObject gameObject = (GameObject) singleGameElement;
                return new Vec2(gameObject.getPixelPosition().x, gameObject.getPixelPosition().y);
                //return roundElement.getPixelPosition();
            }
        }
        System.out.println("For this element I can not determine the position");
        return null;
    }

    protected void addTextToNewCreatedElement(String objectName, LevelsEditorProcess levelsEditorProcess, Figure figure){
        Vec2 textPos = figure.getCenter();
        String text = "New " + objectName + " was added";
        DissolvingAndUpwardsMovingText dissolvingText = new DissolvingAndUpwardsMovingText(textPos.x, textPos.y-levelsEditorProcess.figures.get(0).getHeight()/2,text);
        levelsEditorProcess.getGameRound().addNewDissolvingText(dissolvingText);
    }

    protected void updateNewOrExistingTilesetMenu(androidGUI_Element releasedElement){
        if (releasedElement.getName() == USE_EXISTING_GRAPHIC) {
            Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
        }
        else if (releasedElement.getName() == LOAD_NEW_GRAPHIC) {

        }
        makePauseToNextOperation();
    }

    protected void addSliderWithEmbeddedTextField(androidGUI_ScrollableTab tab, int min, int max, String textFieldText, int savedValue){
        androidAndroidGUI_Slider sliderAlongX = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , textFieldText, min, max);
        sliderAlongX.setText(Integer.toString(max));
        sliderAlongX.setUserValue(savedValue);
        androidAndroidGUI_TextField textFieldAlongX = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), Program.engine.width/3.918f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, textFieldText, true, min, max);
        (textFieldAlongX).addCoppeledSlider(sliderAlongX);
        sliderAlongX.addCoppeledTextField( textFieldAlongX);
        textFieldAlongX.setUserValue(savedValue);
        tab.addGUI_Element(sliderAlongX, null);
        tab.addGUI_Element(textFieldAlongX, null);
    }

    protected final void clearAndMinimizeTab(androidGUI_ScrollableTab tab){
        tab.clearElements();
        tab.setMinimalHeight();
    }
}
