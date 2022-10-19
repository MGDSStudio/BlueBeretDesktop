package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenGraphic;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public abstract class AddingNewIndependentGraphicAction extends SubmenuAction{
    //Names for GUI elements
    final static String FILL_WITH_TILES = "Fill with tiles";
    final static String STRETCH_GRAPHIC = "Stretch graphic";
    final static String USE_EXISTING_GRAPHIC = "Use existing graphic";
    final static String LOAD_NEW_GRAPHIC = "Load from external storage";
    final static protected String FLIP = "Flip graphic";
    final static protected String NO_FLIP = "No flip";

    //Statements
    public final static byte FIRST_POINT_ADDING = 1;
    public final static byte SECOND_POINT_ADDING = 2;
    public final static byte ANGLE_CHOOSING = 3;

    public final static byte LAYER_CHOOSING = 4;
    public final static byte NEW_OR_EXISTING_TILESET = 5;
    public final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = 6;
    public final static byte FILL_OR_STRING_TEXTURE = 9;
    public final static byte TEXTURE_REGION_CHOOSING = 10;
    public final static byte FLIP_STATEMENT_CHOOSING = 11;
    public final static byte COMPLETED = 100;
    public static byte END = COMPLETED;

    private PointAddingController pointAddingController;
    private RectangularElementAdding roundBoxAdding;
    protected boolean graphicType;

    public AddingNewIndependentGraphicAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        pointAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == RectangularElementAdding.FIRST_POINT_ADDING) {
            tab.clearElements();
            tab.setMinimalHeight();
            System.out.println("RECREATED");
        }
        else if (localStatement == ANGLE_CHOOSING) {
           createAngleChoosingMenu(tab);
        }
        else if (localStatement == LAYER_CHOOSING) {
            createLayerChoosingMenuWithApplyButton(tab);
        }
        else if (localStatement == NEW_OR_EXISTING_TILESET) {
            createNewOrExistingTilesetMenu(tab);
        }
        else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
            createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
            /*
            tab.clearElements();
            ArrayList<String> filesInGameDirectory = StringLibrary.getFilesListInAssetsFolder();
            ArrayList<String> tilesets = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, "Tileset");
            ArrayList<String> imagesWithPrefix = StringLibrary.leaveInArrayFilesWithExtension(tilesets, "png");
            ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < imagesWithPrefix.size(); i++) {
                images.add(StringLibrary.deleteAssetsFromPath(imagesWithPrefix.get(i)));
                GUI_Element button = new GUI_ImageButton(new Vec2(0, 0), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 1.45f), "", false, images.get(i), false);
                tab.addGUI_Element(button, null);
            }
            GUI_Element backButton = new GUI_Button(new Vec2(0, 0), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR), BACK, false);
            tab.addGUI_Element(backButton, null);
            GUI_Element button = new GUI_Button(new Vec2(0, 0), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR), CANCEL, false);
            tab.addGUI_Element(button, null);
            repositionGUIAlongInTwoCollumns(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            */
        }
        else if (localStatement == TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);
        }
        else if (localStatement == FLIP_STATEMENT_CHOOSING) {
            tab.clearElements();
            tab.setMinimalHeight();
            androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, NO_FLIP, false);
            tab.addGUI_Element(buttonStatic, null);
            androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FLIP, false);
            tab.addGUI_Element(buttonDynamic, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);
        }
        else if (localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){

            clearAndMinimizeTab(tab);
        }
        else {
            System.out.println("Clear menu");
            tab.clearElements();
        }
    }

    @Override
    protected void createNewOrExistingTilesetMenu(androidGUI_ScrollableTab tab){
        super.createNewOrExistingTilesetMenu(tab);
        for (androidGUI_Element element : tab.getElements()){
            if (element.getName() == WITHOUT_GRAPHIC){
                tab.getElements().remove(element);
                break;
            }
        }
    }

    private void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
            else {
                if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, graphicType);
                }
            }
        }
        if (Editor2D.localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            updateFileInput();
        }
    }

    protected void updatePathSelectedMenu(androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData){
        objectData.setPathToTexture(releasedElement.getName());
    }

    protected void updateTabForStatement(LevelsEditorProcess levelsEditorProcess, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController){
        if (Editor2D.localStatement == ANGLE_CHOOSING) {
            if (releasedElement.getName() == APPLY) {
                Editor2D.setNextLocalStatement();
                System.out.println("Must be: " + NEW_OR_EXISTING_TILESET + " statement");
                makePauseToNextOperation();
                Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
                objectData.setAngle((int) (-figure.getAngle()));
                objectData.setGraphicWidth(levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).getWidth());
                objectData.setGraphicHeight(levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).getHeight());
            }
        }
        else if (Editor2D.localStatement == LAYER_CHOOSING) {
            if (releasedElement.getName() == BEHIND_OF_ALL) {
                objectData.setLayer(IndependentOnScreenGraphic.BEHIND_ALL);
            }
            else if (releasedElement.getName() == IN_FRONT_OF_ALL)
                objectData.setLayer(IndependentOnScreenGraphic.IN_FRONT_OF_ALL);
            else if (releasedElement.getName() == BEHIND_OF_PERSONS)
                objectData.setLayer(IndependentOnScreenGraphic.BEHIND_PERSONS);
            System.out.println("Layer: " + objectData.getLayer());
            Editor2D.setNextLocalStatement();
            makePauseToNextOperation();
        }
        else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
            updateNewOrExistingTilesetMenu(releasedElement);
        }

        else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
            System.out.println("SOMETHING TROUBLE !");
            if (releasedElement.getName() != BACK) {
                updatePathSelectedMenu(releasedElement, objectData);
            }
            else{
                System.out.println("Back was pressed");
            }
        }
        else if (Editor2D.localStatement == GET_TILESET_FROM_EXTERNAL_STORAGE) {
            System.out.println("Open file manager");
            Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
            makePauseToNextOperation();
        }
        else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
            if (releasedElement.getName() == APPLY) {
                textureRegionSelected(objectData, tabController.getTab(), tabController);
            }
            makePauseToNextOperation();
        }

    }

    private void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList <androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0){
            for (androidGUI_Element releasedElement : releasedElements){
                if (releasedElement.getName() == CANCEL) {
                    System.out.println("Cancel is pressed " + releasedElement.getClass());
                    Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                }
                if (releasedElement.getName() == BACK) {
                    backPressed();
                }
                else{
                    updateTabForStatement(levelsEditorProcess, releasedElement,  objectData, tabController);
                }
            }
        }
        if (pressedElements.size() > 0) {
            for (androidGUI_Element pressedElement : pressedElements) {
                if (Editor2D.localStatement == ANGLE_CHOOSING) {
                    if (levelsEditorProcess.figures.size() > 0) {
                        if (pressedElement.getClass() == androidAndroidGUI_AnglePicker.class) {
                            Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
                            if (figure.getAngle() != (int) pressedElement.getValue()) {
                                figure.setAngle((int) pressedElement.getValue());
                            }
                        }
                    }
                }
                //updateTabForStatement(levelsEditorProcess, pressedElement, objectData, tabController);
            }
        }
    }

    protected void backPressed(){
        System.out.println("Back is pressed by statement " + Editor2D.localStatement);
        if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING || Editor2D.localStatement == FILL_OR_STRING_TEXTURE){
            System.out.print("Statement was reset from " + Editor2D.localStatement);
            Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
            System.out.println(" to " + Editor2D.localStatement);
        }
        else if (Editor2D.localStatement == AddingNewSpriteAnimationAction.ANIMATION_PARAMETERS_MENU){
            Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
            System.out.println("Back pressed from animation parameters menu");
        }
        else {
            System.out.print("Statement was decremented from " + Editor2D.localStatement);
            Editor2D.setPrevLocalStatement();
            System.out.println("to " + Editor2D.localStatement);
        }
    }

    protected abstract void textureRegionSelected(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, ScrollableTabController tabController) ;


    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            try {
                if (roundBoxAdding.getLocalStatement() == FIRST_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add first point");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == SECOND_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add second point");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == ANGLE_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set angle of the graphic");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == LAYER_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set layer of the graphic");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == NEW_OR_EXISTING_TILESET) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Which graphic do you want to use?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == GET_TILESET_FROM_EXTERNAL_STORAGE) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Copy png file to the " + AndroidSpecificFileManagement.getPathToCacheFilesInAndroid());
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == TILESET_IN_DIRECTORY_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Select graphic file you want to use");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == FILL_OR_STRING_TEXTURE) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("How to apply texture?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == TEXTURE_REGION_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Adjust picture area for this graphic");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == FLIP_STATEMENT_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Flip graphic?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (roundBoxAdding.getLocalStatement() == END) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("New graphic was added");
                    onScreenConsole.setText(actualConsoleText);
                }
            }
            catch (Exception e) {
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
        if (roundBoxAdding == null) roundBoxAdding = new RectangularElementAdding();
        if (!roundBoxAdding.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.staticSpriteType, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                System.out.println("Added new polygon");
            }
        }
        else objectWasAdded(levelsEditorProcess);
        if (Editor2D.localStatement >= END){
            if (levelsEditorProcess.figures.size()>0){
                levelsEditorProcess.figures.clear();
                System.out.println("Figures cleared");
            }
        }
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointAddingController.draw(gameCamera, levelsEditorProcess);
    }

    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        roundBoxAdding = null;
        levelsEditorProcess.pointsOnMap.clear();
        Editor2D.localStatement = FIRST_POINT_ADDING;
        makePauseToNextOperation();
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundBoxAdding.getLocalStatement() != FIRST_POINT_ADDING)
                    roundBoxAdding.setNextStatement();
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundBoxAdding.getLocalStatement() != SECOND_POINT_ADDING) {
                    roundBoxAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    roundBoxAdding.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundBoxAdding.getLocalStatement() < ANGLE_CHOOSING) {
                    roundBoxAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    roundBoxAdding.setNextStatement();
                    Editor2D.localStatementChanged = true;
                    if (levelsEditorProcess.getGameRound() == null) System.out.println("Game round is null");
                    addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
                }
            }
        }
    }

    protected void updateFillOrStringTexture(androidGUI_Element releasedElement, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (releasedElement.getName() == STRETCH_GRAPHIC) {
            Editor2D.setNextLocalStatement();
            objectData.setFill(false);
            levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1).setFillForSprite(false);
            makePauseToNextOperation();
        } else if (releasedElement.getName() == FILL_WITH_TILES) {
            Editor2D.setNextLocalStatement();
            objectData.setFill(true);
            levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1).setFillForSprite(true);
            makePauseToNextOperation();
        }
    }

    @Override
    public byte getEndValue(){
        return END;
    }


}
