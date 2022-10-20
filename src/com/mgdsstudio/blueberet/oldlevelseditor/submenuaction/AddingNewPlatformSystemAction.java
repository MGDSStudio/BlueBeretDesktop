package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamecontrollers.MoveablePlatformsController;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.MovablePlatform;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.gamelibraries.FileManagement;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class AddingNewPlatformSystemAction extends SubmenuWithTwoRectZones{

    //private PointAddingController firstPointAddingController, secondPointAddingController, pointsAddingController;
    //private PointAddingController pointsAddingController;
    //private RectangularElementAdding secondZoneAdding, firstZoneAdding;

    public final static byte SET_PLATFORM_DIMENSIONS = 5;
    public final static byte SET_PLATFORM_VELOCITY = 6;
    private final static byte SET_PLATFORMS_NUMBER = 7;
    private final static byte SET_PLATFORMS_MOVEMENT_PARAMETERS = 8;
    public final static byte NEW_OR_EXISTING_TILESET = 9;
    public final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = 10;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = 11;
    public final static byte FILL_OR_STRING_TEXTURE = 12;
    public final static byte TEXTURE_REGION_CHOOSING = 13;
    public final static byte COMPLETED = 14;
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

    private final String THICKER = "Thickness +";
    private final String THINNER = "Thickness -";
    private final String LONGER = "Width +";
    private final String SHORTER = "Width -";

    private final String WIDTH = "Width, px";
    private final String THICKNESS = "Thickness, px";
    private final String APPLY = "Apply";
    private final String VELOCITY = "Velocity, px";
    private final String PLATFORMS_NUMBER = "Number of platforms";
    //private final String FROM_ENTER_TO_EXIT = "One direction";
    //private final String HERE_AND_THERE = "Here and there ";

    //public static final byte BY_PLAYER = 1;
    //	public static final byte BY_ENEMIES = 2;
    //	public static final byte BY_EVERY_PERSON = 3;
    //Flag enter, Flag exit, byte activatedBy, boolean transferDirection, boolean usingRepeateability

    private  byte MAX_PLATFORMS_NUMBER = 20;
    private int maxPlatformWidth = 225, maxPlatformHeight = 125;
    private int maxPlatformsVelocity = 500;


    public AddingNewPlatformSystemAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(MoveablePlatformsController.CLASS_NAME);
        //firstPointAddingController = new PointAddingController();
        //secondPointAddingController = new PointAddingController();
        pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement == FIRST_POINT_ADDING) {
            tab.clearElements();
        }
        else if (localStatement == SECOND_POINT_ADDING) {
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 145), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            tab.recalculateHeight(null);
        }
        else if (localStatement == SET_PLATFORM_DIMENSIONS){
            tab.clearElements();
            tab.setMinimalHeight();
            tab.clearElements();
            androidGUI_Element widthSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , WIDTH, 1, maxPlatformWidth);
            widthSlider.setText(Integer.toString(maxPlatformWidth));
            widthSlider.setUserValue(1);
            androidGUI_Element widthTextField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WIDTH, true, 1, maxPlatformWidth);
            ((androidAndroidGUI_TextField) widthTextField).addCoppeledSlider((androidAndroidGUI_Slider) widthSlider);
            ((androidAndroidGUI_Slider) widthSlider).addCoppeledTextField((androidAndroidGUI_TextField) widthTextField);
            tab.addGUI_Element(widthSlider, null);
            tab.addGUI_Element(widthTextField, null);
            androidGUI_Element thicknessSlider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 122), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , THICKNESS, 1, maxPlatformHeight);
            thicknessSlider.setText(Integer.toString(maxPlatformHeight));
            thicknessSlider.setUserValue(1);
            androidGUI_Element thicknessTextField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 122+85-32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, THICKNESS, true, 1, maxPlatformHeight);
            ((androidAndroidGUI_TextField) thicknessTextField).addCoppeledSlider((androidAndroidGUI_Slider) thicknessSlider);
            ((androidAndroidGUI_Slider) thicknessSlider).addCoppeledTextField((androidAndroidGUI_TextField) thicknessTextField);
            tab.addGUI_Element(thicknessSlider, null);
            tab.addGUI_Element(thicknessTextField, null);

            androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 122+85-32+32), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Apply", false);
            tab.addGUI_Element(buttonApply, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 122+85-32+32+32), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());

        }
        else if (localStatement == SET_PLATFORM_VELOCITY){
            tab.clearElements();
            androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 2, VELOCITY, 1, maxPlatformsVelocity);
            slider.setText(Integer.toString(maxPlatformsVelocity));
            slider.setUserValue(1);
            androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, VELOCITY, true, 1, maxPlatformsVelocity);
            ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
            ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
            tab.addGUI_Element(slider, null);
            tab.addGUI_Element(textField, null);

            androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
            tab.addGUI_Element(buttonApply, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 185), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (localStatement == SET_PLATFORMS_NUMBER){
            tab.clearElements();
            androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), 32), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 2, PLATFORMS_NUMBER, 1, MAX_PLATFORMS_NUMBER);
            slider.setText("");
            slider.setUserValue(1);
            androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), 85), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, PLATFORMS_NUMBER, true, 1, MAX_PLATFORMS_NUMBER);
            ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
            ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
            tab.addGUI_Element(slider, null);
            tab.addGUI_Element(textField, null);

            androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
            tab.addGUI_Element(buttonApply, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (localStatement == SET_PLATFORMS_MOVEMENT_PARAMETERS){
            tab.clearElements();
            androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, FROM_START_TO_FINISH, false);
            tab.addGUI_Element(buttonStatic, null);
            androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, HERE_AND_THERE, false);
            tab.addGUI_Element(buttonDynamic, null);
            androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
        }
        else if (localStatement == NEW_OR_EXISTING_TILESET) {
            createNewOrExistingTilesetMenu(tab);
            /*
            tab.clearElements();
            tab.setMinimalHeight();
            GUI_Element buttonStatic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.USE_EXISTING_GRAPHIC, false);
            tab.addGUI_Element(buttonStatic, null);
            GUI_Element buttonDynamic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.LOAD_NEW_GRAPHIC, false);
            tab.addGUI_Element(buttonDynamic, null);
            GUI_Element buttonWithoutGraphic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.WITHOUT_GRAPHIC, false);
            tab.addGUI_Element(buttonWithoutGraphic, null);
            GUI_Element buttonCancel = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 3 * (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            */
        } else if (localStatement == FILL_OR_STRING_TEXTURE) {
            createFillOrStringTextureMenu(tab);
            /*
            tab.clearElements();
            tab.setMinimalHeight();
            GUI_Element buttonStatic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.STRETCH_GRAPHIC, false);
            tab.addGUI_Element(buttonStatic, null);
            GUI_Element buttonDynamic = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.FILL_WITH_TILES, false);
            tab.addGUI_Element(buttonDynamic, null);
            GUI_Element buttonCancel = new GUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * GUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(null);*/

        } else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
            createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
            /*
            int distanceToFirstElement = (int) (Program.engine.width / 11.1f);
            tab.clearElements();
            ArrayList<String> filesInGameDirectory = StringLibrary.getFilesListInAssetsFolder();
            ArrayList<String> tilesets = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, "Tileset");
            ArrayList<String> imagesWithPrefix = StringLibrary.leaveInArrayFilesWithExtension(tilesets, "png");
            ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < imagesWithPrefix.size(); i++) {
                images.add(StringLibrary.deleteAssetsFromPath(imagesWithPrefix.get(i)));
            }

            int i = 0;
            for (i = 0; i < images.size(); i++) {
                int xPos = 0;
                int yPos = 0;
                if (((i + 2) % 2) == 0) {
                    xPos = tab.getWidth() / 4;
                    yPos = (int) (distanceToFirstElement + 1 * distanceToFirstElement * i);
                } else {
                    xPos = 3 * tab.getWidth() / 4;
                    yPos = (int) (distanceToFirstElement + 1 * distanceToFirstElement * (i - 1));
                }
                GUI_Element button = new GUI_ImageButton(new Vec2(xPos, yPos), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.7f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AddingNewRoundBoxAction.STRETCH_GRAPHIC, false, images.get(i), true);
                tab.addGUI_Element(button, null);
            }
            GUI_Element buttonCancel = new GUI_Button(new Vec2(((tab.getWidth() / 2)), (int) (3 * distanceToFirstElement + 1 * distanceToFirstElement * (Program.engine.ceil(i / 2.0f) + 1))), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            tab.addGUI_Element(buttonCancel, null);
            tab.recalculateHeight(tab.getElements());
            */
        } else if (localStatement == TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);

            /*
            tab.clearElements();
            GUI_Element buttonApply = new GUI_Button(new Vec2(((tab.getWidth() / 4)), 148), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f), APPLY, false);
            tab.addGUI_Element(buttonApply, null);
            GUI_Element buttonCancel = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 148), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, (int) (GUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 0.7f), CANCEL, false);
            tab.addGUI_Element(buttonCancel, null);
            tab.addGUI_Element(buttonCancel, null);
            repositionGUIAlongY(tab, tab.getElements());
            tab.recalculateHeight(tab.getElements());
            */
        }
        else tab.clearElements();

    }

    private void calculateMaxPlatformDimensions(LevelsEditorProcess levelsEditorProcess) {
        if (levelsEditorProcess.figures.size()>1){
            int minWidth = 32000;
            int minHeight = 32000;
            for (int i = levelsEditorProcess.figures.size()-1; i>=levelsEditorProcess.figures.size()-2; i--){
                Figure actualFigure = levelsEditorProcess.figures.get(i);
                if (minWidth > actualFigure.getWidth())  minWidth = actualFigure.getWidth();
                if (minHeight > actualFigure.getHeight())  minHeight = actualFigure.getHeight();
            }

            maxPlatformWidth = minWidth;
            maxPlatformHeight = minHeight;
            System.out.println("Dim: " + maxPlatformWidth + " x " + maxPlatformHeight);
        }
    }



    private void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                platformsSystemUpdating(objectData, levelsEditorProcess, tabController, releasedElements);

            }
            else {
                if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, TilesetZone.SPRITE);
                }
            }

        }
        if (Editor2D.localStatement == GET_TILESET_FROM_EXTERNAL_STORAGE) updateFileInput();
    }

    /*
    private void zoneCreating(GameObjectDataForStoreInEditor objectData, GUI_ScrollableTab tab) {
        if (tab.getTilesetZone() == null) {
            System.out.println("Zone was added ");
            TilesetZone tilesetZone = new TilesetZone(objectData.getPathToTexture(), tab, new Vec2(ScrollableTabController.MINIMAL_FREE_SPACE, ScrollableTabController.MINIMAL_FREE_SPACE), new Vec2(tab.getWidth() - ScrollableTabController.MINIMAL_FREE_SPACE, tab.getHeight() - 1 * ScrollableTabController.MINIMAL_FREE_SPACE - tab.getElements().get(0).getHeight()));
            tab.createTilesetZone(tilesetZone);
        }
    }*/

    private void platformsSystemUpdating(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess,  ScrollableTabController tabController, ArrayList<androidGUI_Element> releasedElement) {
        for (androidGUI_Element element : releasedElement){
                if (element.getName() == CANCEL) {
                    if (element.getClass() == androidAndroidGUI_Button.class) {
                        Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                        System.out.println("Reset");
                    }
                }
                else if (Editor2D.localStatement == SET_PLATFORM_DIMENSIONS) {
                    if (element.getName() == APPLY) {
                        boolean firstSliderValueSaved = false;
                        boolean secondSliderValueSaved = false;
                        for (androidGUI_Element guiElement : tabController.getTab().getElements()) {
                            if (guiElement.getClass() == androidAndroidGUI_Slider.class) {
                                if (guiElement.getName() == WIDTH){
                                    objectData.setPlatformWidth((int) guiElement.getValue());

                                    System.out.println("W; " + objectData.getPlatformWidth());
                                    firstSliderValueSaved = true;
                                }
                                if (guiElement.getName() == THICKNESS){
                                    objectData.setPlatformThickness((int) guiElement.getValue());
                                    System.out.println("Th; " + objectData.getPlatformThickness());
                                    secondSliderValueSaved = true;
                                }
                            }
                        }
                        if (firstSliderValueSaved && secondSliderValueSaved) {
                            Editor2D.setNextLocalStatement();
                            makePauseToNextOperation();
                        }
                    }
                }
                else if (Editor2D.localStatement == SET_PLATFORM_VELOCITY) {
                    if (element.getName() == APPLY) {
                        Editor2D.setNextLocalStatement();
                        for (androidGUI_Element guiElement : tabController.getTab().getElements()) {
                            if (guiElement.getClass() == androidAndroidGUI_Slider.class) {
                                if (guiElement.getName() == VELOCITY){
                                    objectData.setPlatformVelocity((int) guiElement.getValue());
                                    System.out.println("Vel; " + (int) guiElement.getValue());
                                }
                            }
                        }
                    }
                    makePauseToNextOperation();
                }
                else if (Editor2D.localStatement == SET_PLATFORMS_NUMBER) {
                    if (element.getName() == APPLY) {
                        for (androidGUI_Element guiElement : tabController.getTab().getElements()) {
                            if (guiElement.getClass() == androidAndroidGUI_Slider.class) {
                                if (guiElement.getName() == PLATFORMS_NUMBER){
                                    objectData.setPlatformsNumber((int) guiElement.getValue());
                                    System.out.println("Number of platforms; " + (int) objectData.getPlatformsNumber());
                                    if (guiElement.getValue() > 1) {
                                        Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                                        objectData.setMovementParameter(MoveablePlatformsController.ONE_DIRECTION);
                                        objectData.setUsingRepeatability(MoveablePlatformsController.ONE_DIRECTION);
                                    }
                                    else Editor2D.setNextLocalStatement();

                                }
                            }
                        }
                        createPlatformFigure(levelsEditorProcess, objectData);

                    }
                    makePauseToNextOperation();
                }
                else if (Editor2D.localStatement == SET_PLATFORMS_MOVEMENT_PARAMETERS){
                    if (element.getName() == FROM_START_TO_FINISH) {
                        Editor2D.setNextLocalStatement();
                        objectData.setMovementParameter(MoveablePlatformsController.ONE_DIRECTION);
                        objectData.setUsingRepeatability(MoveablePlatformsController.ONE_DIRECTION);
                        System.out.println("Repeatability 1: " + objectData.getUsingRepeateability());
                    }
                    else if (element.getName() == HERE_AND_THERE) {
                        Editor2D.setNextLocalStatement();
                        objectData.setMovementParameter(MoveablePlatformsController.BACK_AND_FORTH);
                        objectData.setUsingRepeatability(MoveablePlatformsController.BACK_AND_FORTH);
                        System.out.println("Repeatability 2: " + objectData.getUsingRepeateability());
                    }
                    makePauseToNextOperation();
                }
                else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
                    if (element.getName() == USE_EXISTING_GRAPHIC) {
                        Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
                    } else if (element.getName() == LOAD_NEW_GRAPHIC) {
                        System.out.println("Try to load external graphic");
                        Editor2D.setNewLocalStatement(GET_TILESET_FROM_EXTERNAL_STORAGE);
                    } else if (element.getName() == WITHOUT_GRAPHIC) {
                        addTextToNewCreatedElement("platform system", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                        Editor2D.setNewLocalStatement(END);
                        tabController.zoneDeleting();
                    }
                    makePauseToNextOperation();
                }
                else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                    Editor2D.setNextLocalStatement();
                    objectData.setPathToTexture(element.getName());
                    makePauseToNextOperation();

                }
                else if (Editor2D.localStatement == FILL_OR_STRING_TEXTURE) {
                    if (element.getName() == STRETCH_GRAPHIC) {
                        Editor2D.setNextLocalStatement();
                        objectData.setFill(false);
                        levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1).setFillForSprite(false);
                        makePauseToNextOperation();
                    } else if (element.getName() == FILL_WITH_TILES) {
                        Editor2D.setNextLocalStatement();
                        objectData.setFill(true);
                        levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1).setFillForSprite(true);
                        makePauseToNextOperation();
                    }
                }
                else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    if (element.getName() == APPLY) {
                        Editor2D.setNextLocalStatement();
                        addTextToNewCreatedElement("platform system", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                        TextureDataToStore data = new TextureDataToStore(tabController.getTab().getTilesetZone().getGraphic(), tabController.getTab().getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), objectData.getFill());
                        objectData.setStaticTextureData(data);
                        objectData.calculateGraphicDimentionsForMoveablePlatform();
                        tabController.zoneDeleting();
                    }
                    makePauseToNextOperation();
                }
        }
        //System.out.println("Figures: " + levelsEditorProcess.figures.size());

    }

    private void createPlatformFigure(LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){

        try {
            Flag enter = new Flag(levelsEditorProcess.figures.get(0));
            Flag exit = new Flag(levelsEditorProcess.figures.get(1));
            //int velocity, int platformWidth, int platformHeight, byte platformsNumber, boolean repeatability
            MoveablePlatformsController platformsController = new MoveablePlatformsController(enter, exit, (int) 0, objectData.getPlatformWidth(), objectData.getPlatformThickness(), (byte) objectData.getPlatformsNumber(), objectData.getUsingRepeateability());

            ArrayList<MovablePlatform> platforms = platformsController.getPlatforms();
            for (MovablePlatform platform : platforms) {
                System.out.println("try to add platform");
                Figure platformFigure = new Figure(platform);
                levelsEditorProcess.figures.add(platformFigure);
                System.out.println("Figure added on " + platformFigure.getCenter() + "; Enter was on: " + levelsEditorProcess.figures.get(0).getCenter());
            }
        }
        catch (Exception e){
            System.out.println("Can not create figures of platforms");
        }


        //MovablePlatform platform = new MovablePlatform(enter, exit, (int)0, objectData.getPlatformWidth(), objectData.getPlatformThickness(), objectData.getPlatformsNumber(), objectData.getUsingRepeateability());
        /*
        Figure platformFigure = (Figure)levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).clone();
        Vec2 center = new Vec2(levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).getCenter().x, levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).getCenter().y);
        center.y+=25;
        platformFigure.setCenter(center);
        levelsEditorProcess.figures.add(platformFigure);*/

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
                else if (Editor2D.localStatement == SET_PLATFORM_DIMENSIONS) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set dimensions of platforms");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == SET_PLATFORM_VELOCITY) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set platforms velocity");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == SET_PLATFORMS_NUMBER) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set platforms number");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == SET_PLATFORMS_MOVEMENT_PARAMETERS) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Set come back way");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Which graphic do you want to use?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == GET_TILESET_FROM_EXTERNAL_STORAGE) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Copy png file to " + FileManagement.getPathToCacheFilesInAndroid());
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Select graphic file you want to use");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == FILL_OR_STRING_TEXTURE) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("How to apply texture?");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("Choose picture area for this object");
                    onScreenConsole.setText(actualConsoleText);
                }
                else if (Editor2D.localStatement == END) {
                    ArrayList<String> actualConsoleText = new ArrayList<>();
                    actualConsoleText.add("New platform system was added");
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
            if (pointsAddingController == null) pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);

            //if (pointsAddingController == null) System.out.println("It is null");
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
        }
    }

    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointsAddingController.draw(gameCamera, levelsEditorProcess);
    }


    protected void createFigure(RectangularElementAdding rectZoneAdding, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (rectZoneAdding.equals(firstZoneAdding)){
            addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
            firstZoneAdding = null;
        }
        else  if (rectZoneAdding.equals(secondZoneAdding)){
            addRectFigureOnMapZoneAndSaveData((byte)2, levelsEditorProcess, objectData);
            secondZoneAdding = null;
        }
        makePauseToNextOperation();
    }

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
                    Editor2D.setNewLocalStatement(SET_PLATFORM_DIMENSIONS);
                    System.out.println("Last point for second zone placed");
                    createFigure(secondZoneAdding, levelsEditorProcess, objectData);
                    calculateMaxPlatformDimensions(levelsEditorProcess);
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
