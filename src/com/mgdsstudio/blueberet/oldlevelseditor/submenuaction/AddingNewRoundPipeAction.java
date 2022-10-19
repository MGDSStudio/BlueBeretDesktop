package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.classestoberemoved.Flower;
import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.PlantController;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding.AddingRoundElement;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewRoundPipeAction extends AddingRoundElement {
    PointAddingController pointAddingController;
    RectangularElementAdding roundBoxAdding;
    public final static byte FIRST_POINT_ADDING = 1;
    public final static byte SECOND_POINT_ADDING = 2;
    public final static byte FLOWER_EXISTENCE_SETTING = 3;
    public final static byte FLOWER_LIFE_SETTING = 4;
    public final static byte FLOWER_DIAMETER_SETTING = 5;
    public final static byte NEW_OR_EXISTING_TILESET = 6;
    public final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = 7;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 8;
    public final static byte TEXTURE_REGION_CHOOSING = 9;
    public final static byte COMPLETED = 10;
    public final static byte END = COMPLETED;

    private final String WITHOUT_FLOWER = "Without flower";
    private final String STAY_AND_BITE = "Flower static";
    private final String UP_BITE_DOWN = "Flower dynamic";

    private final String UP = "Upwards";
    private final String DOWN = "Downwards";
    private final String LEFT = "To the left";
    private final String RIGHT = "To the right";

    //saved data
    private int pipeDiameter;

    /*
    public static final byte NO_FLOWER = -1;
	public static final byte NO_ACTIVE = 0;
	public static final byte STAY_AND_BITE = 1;
	public static final byte UP_BITE_DOWN = 2;
	public static final byte UP_UNDER_PLAYER = 3;
     */

    //RoundPipe 1:720,1090,130,220,270,2#Tileset2.png;400x600x600x800x130x220

    private static int BASIC_FLOWER_LIFE_VALUE = 120;

    public AddingNewRoundPipeAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(RoundPipe.CLASS_NAME);
        pointAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
        Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);

    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        int distanceToFirstElement = (int) (Program.engine.width / 11.1f);
            if (localStatement <= FIRST_POINT_ADDING) {
                tab.clearElements();
                tab.setMinimalHeight();
                System.out.println("RECREATED");

                //pointAddingController.setStatement(PointAddingController.ENDED);
                //roundBoxAdding.setCompleted(true);
                androidAndroidGUI_RadioButton element = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 15), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, UP);
                tab.addGUI_Element(element, null);
                element.setStatement(androidGUI_Element.PRESSED);
                androidAndroidGUI_RadioButton element2 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 40), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, DOWN);
                tab.addGUI_Element(element2, null);
                androidAndroidGUI_RadioButton element3 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, LEFT);
                tab.addGUI_Element(element3, null);
                androidAndroidGUI_RadioButton element4 = new androidAndroidGUI_RadioButton(new Vec2(((tab.getWidth() / 2)), 65), (int)(androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR*1.3f), androidAndroidGUI_RadioButton.NORMAL_HEIGHT_IN_REDACTOR, RIGHT);
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
            }
            else if (localStatement == SECOND_POINT_ADDING) {
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SubmenuAction.CANCEL, false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(null);
                System.out.println("Cancel button was added");
            }

            else if (localStatement == FLOWER_EXISTENCE_SETTING) {
                tab.clearElements();
                androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WITHOUT_FLOWER, false);
                tab.addGUI_Element(buttonApply, null);
                androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, STAY_AND_BITE, false);
                tab.addGUI_Element(buttonStatic, null);
                androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, UP_BITE_DOWN, false);
                tab.addGUI_Element(buttonDynamic, null);
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SubmenuAction.CANCEL, false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(null);
            }
            else if (localStatement == FLOWER_LIFE_SETTING){
                createLifeSettingMenu(tab);
                for (androidGUI_Element element : tab.getElements()) {
                    if (element.getClass() == androidAndroidGUI_Slider.class) {
                        ((androidAndroidGUI_Slider) element).setValue(BASIC_FLOWER_LIFE_VALUE);
                    }
                }
            }
            else if (localStatement == FLOWER_DIAMETER_SETTING) {
                System.out.println("Min: " + PlantController.getMinimalFLowerDiameter() + "; max: " + PlantController.getMaximalFLowerDiameter(pipeDiameter) + "; Pipe: " + pipeDiameter);
                createFlowerDiameterSettingMenu(tab, PlantController.getMinimalFLowerDiameter(), PlantController.getMaximalFLowerDiameter(pipeDiameter));
            }
            else if (localStatement == NEW_OR_EXISTING_TILESET) {
                tab.clearElements();
                tab.setMinimalHeight();
                androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewStaticSpriteAction.USE_EXISTING_GRAPHIC, false);
                tab.addGUI_Element(buttonStatic, null);
                androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewStaticSpriteAction.LOAD_NEW_GRAPHIC, false);
                tab.addGUI_Element(buttonDynamic, null);
                androidGUI_Element buttonWithoutGraphic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.WITHOUT_GRAPHIC, false);
                tab.addGUI_Element(buttonWithoutGraphic, null);
                androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SubmenuAction.CANCEL, false);
                tab.addGUI_Element(buttonCancel, null);
                repositionGUIAlongY(tab, tab.getElements());
                tab.recalculateHeight(null);
            }
            else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                tab.clearElements();
                ArrayList<String> filesInGameDirectory = StringLibrary.getFilesListInAssetsFolder();
                ArrayList<String> tilesets = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, "Tileset");
                ArrayList<String> imagesWithPrefix = StringLibrary.leaveInArrayFilesWithExtension(tilesets, "png");
                ArrayList<String> images = new ArrayList<>();
                for (int i = 0; i < imagesWithPrefix.size(); i++) {
                    images.add(StringLibrary.deleteAssetsFromPath(imagesWithPrefix.get(i)));
                    androidGUI_Element button = new androidAndroidGUI_ImageButton(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 1.45f), "", false, images.get(i), true);
                    tab.addGUI_Element(button, null);
                }
                androidGUI_Element button = new androidAndroidGUI_Button(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR), SubmenuAction.CANCEL, false);
                tab.addGUI_Element(button, null);
                repositionGUIAlongInTwoCollumns(tab, tab.getElements());
                tab.recalculateHeight(tab.getElements());
            } else if (localStatement == TEXTURE_REGION_CHOOSING) {
                createTextureRegionChoosingMenu(tab);
                /*
                tab.setMinimalHeight();
                tab.clearElements();
                GUI_Element buttonApply = new GUI_Button(new Vec2(((tab.getWidth() / 4)), 148), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f), "Apply", false);
                tab.addGUI_Element(buttonApply, null);
                GUI_Element buttonCancel = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 148), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f), "Cancel", false);
                tab.addGUI_Element(buttonCancel, null);
                */
            }

    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            try {
                if (Editor2D.localStatement == FIRST_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add corner for pipe");
                    onScreenConsole.setText(actualConsoleText);
                } else if (Editor2D.localStatement == SECOND_POINT_ADDING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Add next corner for pipe");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == FLOWER_EXISTENCE_SETTING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Must pipe have a flower?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == FLOWER_LIFE_SETTING){
                    setTextForConsole(onScreenConsole, "Set life value for flower");
                }
                else if (Editor2D.localStatement == FLOWER_DIAMETER_SETTING){
                    setTextForConsole(onScreenConsole, "Set jaws diameter");
                }
                else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Which graphic do you want to use?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == GET_TILESET_FROM_EXTERNAL_STORAGE) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Choose graphic file from external storage");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Select graphic file you want to use");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Choose picture area for this object");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == END) {

                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("New pipe was added");
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
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointAddingController.draw(gameCamera, levelsEditorProcess);
    }

    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        roundBoxAdding = null;
        levelsEditorProcess.pointsOnMap.clear();
        levelsEditorProcess.figures.clear();
        Editor2D.localStatement = FIRST_POINT_ADDING;
        makePauseToNextOperation();
        System.out.println("Object was added");
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (Editor2D.localStatement == FLOWER_EXISTENCE_SETTING){
                if (objectData.getWidth()>=PlantController.getMinimalFLowerDiameter() && objectData.getHeight() >= PlantController.MINIMAL_PIPE_HEIGHT_FOR_PLANT) {

                }
            }
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
            else {
                if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, TilesetZone.SPRITE);
                }
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList <androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        //System.out.println("Released elements : " + releasedElements.size());
        if (releasedElements.size() > 0) {
            for (androidGUI_Element releasedElement : releasedElements) {
                if (releasedElement.getName() == SubmenuAction.CANCEL) {
                    System.out.println("Cancel is pressed " + releasedElement.getClass());
                    //Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_BOX);
                    Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                    makePauseToNextOperation();
                }
                else if (Editor2D.localStatement == FIRST_POINT_ADDING || Editor2D.localStatement == SECOND_POINT_ADDING) {
                    if (releasedElement.getName() == UP) objectData.setAngle((int) 270);
                    else if (releasedElement.getName() == LEFT) objectData.setAngle((int) 180);
                    else if (releasedElement.getName() == DOWN) objectData.setAngle((int) 90);
                    else if (releasedElement.getName() == RIGHT) objectData.setAngle((int) 0);

                }
                else if (Editor2D.localStatement == FLOWER_EXISTENCE_SETTING) {
                        makePauseToNextOperation();
                        if (releasedElement.getName() == WITHOUT_FLOWER) {
                            objectData.setFlowerBehaviour(Flower.NO_FLOWER);
                            Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                        } else if (releasedElement.getName() == STAY_AND_BITE) {
                            objectData.setFlowerBehaviour(Flower.STAY_AND_BITE);
                            Editor2D.setNextLocalStatement();
                        } else if (releasedElement.getName() == UP_BITE_DOWN) {
                            objectData.setFlowerBehaviour(Flower.UP_BITE_DOWN);
                            Editor2D.setNextLocalStatement();
                        } else Editor2D.setNextLocalStatement();
                    }
                else if (Editor2D.localStatement == FLOWER_LIFE_SETTING) {
                    lifeSetting(tab, releasedElement, objectData);
                }
                else if (Editor2D.localStatement == FLOWER_DIAMETER_SETTING) {
                    diameterSetting(tab, releasedElement, objectData);
                }
                else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
                    //pointAddingController = null;
                        if (releasedElement.getName() == AddingNewStaticSpriteAction.USE_EXISTING_GRAPHIC) {
                            Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
                        }
                        else if (releasedElement.getName() == AddingNewStaticSpriteAction.LOAD_NEW_GRAPHIC) {
                            Editor2D.setNewLocalStatement(GET_TILESET_FROM_EXTERNAL_STORAGE);
                        }
                        else if (releasedElement.getName() == AddingNewRoundBoxAction.WITHOUT_GRAPHIC) {
                            addTextToNewCreatedElement("pipe", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            Editor2D.setNewLocalStatement(END);
                            tabController.zoneDeleting();
                        }
                        makePauseToNextOperation();
                    } else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                        Editor2D.setNextLocalStatement();
                        objectData.setPathToTexture(releasedElement.getName());
                        makePauseToNextOperation();
                    } else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                        if (releasedElement.getName() == SubmenuAction.APPLY) {
                            addTextToNewCreatedElement("pipe", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            Editor2D.setNewLocalStatement(END);
                            TextureDataToStore data = new TextureDataToStore(tab.getTilesetZone().getGraphic(), tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), objectData.getFill());
                            //objectData.setStaticTextureData(data);
                            objectData.setStaticSpriteByTextureData(data);
                            objectData.calculateGraphicDimentionsForRoundBox();
                            makePauseToNextOperation();
                            tabController.zoneDeleting();

                        }
                        makePauseToNextOperation();
                    }

            }
        }
    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        //System.out.println("New angle: " + objectData.getAngle() + " was set ");
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (roundBoxAdding == null) roundBoxAdding = new RectangularElementAdding();
        if (!roundBoxAdding.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.staticSpriteType, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < FLOWER_EXISTENCE_SETTING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                System.out.println("Added new polygon");
            }
        }
        else objectWasAdded(levelsEditorProcess);
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundBoxAdding.getLocalStatement() != FIRST_POINT_ADDING) {
                    roundBoxAdding.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundBoxAdding.getLocalStatement() != SECOND_POINT_ADDING) {
                    roundBoxAdding.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    roundBoxAdding.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundBoxAdding.getLocalStatement() < FLOWER_EXISTENCE_SETTING) {
                    roundBoxAdding.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    objectData.setLeftUpperCorner(roundBoxAdding.getPosition());
                    objectData.setWidth(roundBoxAdding.getWidth());
                    objectData.setHeight(roundBoxAdding.getHeight());
                    roundBoxAdding.setNextStatement();
                    Editor2D.localStatementChanged = true;
                    if (levelsEditorProcess.getGameRound() == null) System.out.println("Game round is null");
                    addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
                    System.out.println("Dim: " + PlantController.getMinimalFLowerDiameter() + "; " + objectData.getHeight());
                    pipeDiameter = objectData.getWidth();
                    if (objectData.getWidth()<PlantController.getMinimalFLowerDiameter() || objectData.getHeight() < PlantController.MINIMAL_PIPE_HEIGHT_FOR_PLANT){
                        Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                        System.out.println("This pipe can not have a flower");
                    }
                    else {
                        System.out.println("This pipe can have a flower; W: " +  pipeDiameter + "; H: " + objectData.getHeight());
                        Editor2D.setNewLocalStatement(FLOWER_EXISTENCE_SETTING);
                    }

                }
            }
        }
        /*
if (Editor2D.localStatement <= SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundElementAddingProcess.getLocalStatement() != FIRST_POINT_ADDING) {
                    roundElementAddingProcess.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundElementAddingProcess.getLocalStatement() != SECOND_POINT_ADDING) {
                    roundElementAddingProcess.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    objectData.setLeftUpperCorner(new PVector(levelsEditorProcess.pointsOnMap.get(0).getPosition().x, levelsEditorProcess.pointsOnMap.get(0).getPosition().y));
                    roundElementAddingProcess.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundElementAddingProcess.getLocalStatement() < FLOWER_EXISTENCE_SETTING) {
                    roundElementAddingProcess.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    objectData.setWidth(roundElementAddingProcess.getWidth());
                    objectData.setHeight(roundElementAddingProcess.getHeight());
                    roundElementAddingProcess.setNextStatement();
                    Editor2D.localStatementChanged = true;
                    if (levelsEditorProcess.getGameRound() == null) System.out.println("Game round is null");
                    addFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
                }
            }
        }
        */

    }

    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_FLOWER_LIFE_VALUE = (int)value;
    }


    @Override
    public byte getEndValue(){
        return END;
    }

}
