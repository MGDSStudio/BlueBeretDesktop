package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.RoundCircle;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RoundCircleAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding.AddingRoundElement;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;

import java.util.ArrayList;

public final class AddingNewRoundCircleAction extends AddingRoundElement implements RoundElementAddingConstants{
    private static int BASIC_LIFE_VALUE = 1;


    public AddingNewRoundCircleAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(RoundCircle.CLASS_NAME);
        pointAddingController = new PointAddingController(Figure.CIRCLE_SHAPE);
        Editor2D.localStatement = RoundCircleAdding.FIRST_POINT_ADDING;
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= RoundCircleAdding.FIRST_POINT_ADDING) {
            clearAndMinimizeTab(tab);

        }
        else if (localStatement <= RoundCircleAdding.DIAMETER_CHOOSING) {
            createDiameterChoosingMenu(tab);
        }
        if (localStatement == RoundCircleAdding.ANGLE_CHOOSING) {
            createAngleChoosingMenu(tab);
        }
        else if (localStatement == RoundCircleAdding.LIFE_SETTING) {
            createLifeSettingMenu(tab);
            for (androidGUI_Element element : tab.getElements()) {
                if (element.getClass() == androidAndroidGUI_Slider.class) {
                    ((androidAndroidGUI_Slider) element).setValue(BASIC_LIFE_VALUE);
                }
            }
        }
        else if (localStatement == RoundCircleAdding.BODY_TYPE_CHOOSING) {
            createBodyTypeChoosingMenu(tab);

        } else if (localStatement == RoundCircleAdding.SPRING_ADDING) {
            createSpringAddingMenu(tab);
        }
        else if (localStatement == RoundCircleAdding.NEW_OR_EXISTING_TILESET) {
            createNewOrExistingTilesetMenu(tab);
        }

        else if (localStatement == RoundCircleAdding.TILESET_IN_DIRECTORY_CHOOSING) {
            createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
        }
        else if (localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();
            clearAndMinimizeTab(tab);
        }
        else if (localStatement == RoundCircleAdding.TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);
        }
        else tab.clearElements();

    }

    private void createDiameterChoosingMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element slider = new androidAndroidGUI_Slider(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.4f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR , "", 1, GameObject.IMMORTALY_LIFE);
        slider.setText("");
        slider.setUserValue(GameObject.IMMORTALY_LIFE);
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), Program.engine.width/3.918f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Life", true, 1, GameObject.IMMORTALY_LIFE);
        ((androidAndroidGUI_TextField) textField).addCoppeledSlider((androidAndroidGUI_Slider) slider);
        ((androidAndroidGUI_Slider) slider).addCoppeledTextField((androidAndroidGUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);

        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Apply", false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);
    }





    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (roundElementAddingProcess == null) roundElementAddingProcess = new RoundCircleAdding();
        if (!roundElementAddingProcess.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, RoundCircle.CLASS_NAME, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < RectangularElementAdding.ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
            }
        }
        else objectWasAdded(levelsEditorProcess);
    }

    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        roundElementAddingProcess = null;
        levelsEditorProcess.pointsOnMap.clear();
        Editor2D.localStatement = RoundCircleAdding.FIRST_POINT_ADDING;
        System.out.println("The previous line is not right");
        makePauseToNextOperation();
    }

    @Override
    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController) {
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
            else {
                if (Editor2D.localStatement == RectangularElementAdding.TEXTURE_REGION_CHOOSING) {
                    zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, TilesetZone.SPRITE);
                }
            }
        }
        if (Editor2D.localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();
            updateFileInput();
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0){
            System.out.println("Local " + Editor2D.localStatement);
            for (androidGUI_Element releasedElement : releasedElements){
                if (releasedElement.getName() == CANCEL) {
                    Editor2D.setNewLocalStatement(RoundCircleAdding.FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                }
                else{

                    if (Editor2D.localStatement == RoundCircleAdding.ANGLE_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            Editor2D.setNextLocalStatement();
                            makePauseToNextOperation();
                            Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
                            objectData.setAngle((-figure.getAngle()));
                        }
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.LIFE_SETTING) {
                        lifeSetting(tab, releasedElement, objectData);
                        System.out.println("Actual menu is life setting ");
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.BODY_TYPE_CHOOSING) {
                        if (releasedElement.getName() == BODY_TYPE_STATIC_STRING) {
                            System.out.println("Static is pressed");
                            objectData.setBodyType(BodyType.STATIC);
                            Editor2D.setNewLocalStatement(RoundCircleAdding.NEW_OR_EXISTING_TILESET);
                        } else if (releasedElement.getName() == BODY_TYPE_DYNAMIC_STRING) {
                            System.out.println("Dynamic is pressed");
                            objectData.setBodyType(BodyType.DYNAMIC);
                            Editor2D.setNextLocalStatement();
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.SPRING_ADDING) {
                        springAdding(tab, releasedElement, objectData);
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.NEW_OR_EXISTING_TILESET) {
                        if (releasedElement.getName() == USE_EXISTING_GRAPHIC) {
                            Editor2D.setNewLocalStatement(RoundCircleAdding.TILESET_IN_DIRECTORY_CHOOSING);
                        }
                        else if (releasedElement.getName() == LOAD_NEW_GRAPHIC) {
                            System.out.println("Try to load external graphic");
                            Editor2D.setNewLocalStatement(RoundCircleAdding.GET_TILESET_FROM_EXTERNAL_STORAGE);
                        } else if (releasedElement.getName() == WITHOUT_GRAPHIC) {
                            addTextToNewCreatedElement("round circle", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            Editor2D.setNewLocalStatement(RoundCircleAdding.END);
                            objectData.setPathToTexture("No_data");
                            setGraphicDataForRoundElementWithoutTexture(objectData);
                            tabController.zoneDeleting();
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.TILESET_IN_DIRECTORY_CHOOSING) {
                        tilesetInDirectoryChoosing(tab, releasedElement, objectData);
                        Editor2D.setNewLocalStatement(RoundCircleAdding.TEXTURE_REGION_CHOOSING);
                    }
                    else if (Editor2D.localStatement == RoundCircleAdding.TEXTURE_REGION_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            addTextToNewCreatedElement("round circle", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            textureRegionChoosing(tab, tabController, objectData);
                        }
                        makePauseToNextOperation();
                    }
                }
            }
        }
        /*
        if (pressedElements.size() > 0) {
            for (GUI_Element pressedElement : pressedElements) {
                if (Editor2D.localStatement == RectangularElementAdding.ANGLE_CHOOSING) {
                    if (levelsEditorProcess.figures.size() > 0) {
                        angleChoosingUpdating(levelsEditorProcess, pressedElement);
                    }
                }
            }
        }*/
    }



    @Override
    public byte getEndValue(){
        return RoundCircleAdding.END;
    }

    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_LIFE_VALUE = value;
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= RoundCircleAdding.SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundElementAddingProcess.getLocalStatement() != RoundCircleAdding.FIRST_POINT_ADDING)
                    roundElementAddingProcess.setNextStatement();
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundElementAddingProcess.getLocalStatement() != RoundCircleAdding.SECOND_POINT_ADDING) {
                    roundElementAddingProcess.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    roundElementAddingProcess.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundElementAddingProcess.getLocalStatement() < RoundCircleAdding.ANGLE_CHOOSING) {
                    roundElementAddingProcess.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    roundElementAddingProcess.setNextStatement();
                    addCircleFigureOnMapZoneAndSaveData(levelsEditorProcess, objectData);
                    Editor2D.setNewLocalStatement(RoundCircleAdding.LIFE_SETTING);
                }
            }
        }
    }

    protected void addCircleFigureOnMapZoneAndSaveData(LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (levelsEditorProcess.pointsOnMap.size() > 1){
            Point firstPoint = levelsEditorProcess.pointsOnMap.get(0);
            Point secondPoint = levelsEditorProcess.pointsOnMap.get(1);
            int xRadius = (int) PApplet.abs(firstPoint.getPosition().x-secondPoint.getPosition().x);
            int yRadius = (int) PApplet.abs(firstPoint.getPosition().y-secondPoint.getPosition().y);
            int radius = xRadius;
            if (xRadius<yRadius) radius = yRadius;
            Vec2 center = firstPoint.getPosition();
            ArrayList <Point> points = new ArrayList<>();
            points.add(firstPoint);
            points.add(secondPoint);
            Figure figure = new Figure(points, center, Figure.CIRCLE_SHAPE);
            levelsEditorProcess.addNewFigure(figure);
            for (Point point : levelsEditorProcess.pointsOnMap){
                point.hide(true);
            }
            objectData.setWidth(radius*2);
            objectData.setHeight(radius*2);
            objectData.setDiameter(radius*2);
            System.out.println("Radius set " + radius);
            objectData.setPosition(firstPoint.getPosition());
        }
        else {
            System.out.println("There are not enough points");
        }

    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            //if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX) {
            try {

                if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.FIRST_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add circle center by long tap");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.SECOND_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Set radius through adding second point");
                }/*
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.ANGLE_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Set angle on the angle picker");
                }*/
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.LIFE_SETTING) {
                    setTextForConsole(onScreenConsole, "Set life");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.BODY_TYPE_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose body type");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.SPRING_ADDING) {
                    setTextForConsole(onScreenConsole, "Add spring if you want");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.NEW_OR_EXISTING_TILESET) {
                    setTextForConsole(onScreenConsole, "Which graphic do you want to use?");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.GET_TILESET_FROM_EXTERNAL_STORAGE) {
                    setTextForConsole(onScreenConsole, "Copy graphic png file to " + AndroidSpecificFileManagement.getPathToCacheFilesInAndroid());
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.TILESET_IN_DIRECTORY_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose graphic file");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RoundCircleAdding.TEXTURE_REGION_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose picture area for this object");
                }
                else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.END) {
                    setTextForConsole(onScreenConsole, "New round circle was added");
                }
                else setTextForConsole(onScreenConsole, "Successfully!");
            } catch (Exception e) {
                System.out.println("Can not change the text of the console " + e);
                ArrayList<String> actualConsoleText = new ArrayList<>();
                actualConsoleText.add("Successfully");
                onScreenConsole.setText(actualConsoleText);
            }
            //}
        }

    }

}
