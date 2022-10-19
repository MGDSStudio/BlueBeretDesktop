package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.RoundPolygon;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Slider;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RoundCircleAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RoundPolygonAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding.AddingRoundElement;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;

public class AddingNewRoundPolygon extends AddingRoundElement implements RoundElementAddingConstants{
    private final static byte FIRST_POINT_ADDING = RoundCircleAdding.FIRST_POINT_ADDING;
    private final static byte SECOND_POINT_ADDING = RoundCircleAdding.SECOND_POINT_ADDING;
    private final static byte THIRD_POINT_ADDING = RoundCircleAdding.SECOND_POINT_ADDING+1;
    private final static byte ANGLE_CHOOSING = THIRD_POINT_ADDING+1;
    private final static byte LIFE_SETTING = ANGLE_CHOOSING+1;
    private final static byte BODY_TYPE_CHOOSING = LIFE_SETTING+1;
    private final static byte SPRING_ADDING = BODY_TYPE_CHOOSING+1;
    private final static byte NEW_OR_EXISTING_TILESET = SPRING_ADDING+1;
    private final static byte GET_TILESET_FROM_EXTERNAL_STORAGE = NEW_OR_EXISTING_TILESET+1;
    private final static byte TILESET_IN_DIRECTORY_CHOOSING = GET_TILESET_FROM_EXTERNAL_STORAGE+1;

    public final static byte TEXTURE_REGION_CHOOSING = TILESET_IN_DIRECTORY_CHOOSING+1;
    public final static byte COMPLETED = 100;
    public final static byte END = COMPLETED;

    private static int BASIC_LIFE_VALUE = 1;
//RoundPolygon 1:1090,70,0,9999,0,0%-25v25v25v25v25v-25#Tileset2.png;400x0x500x98x50x50
    public AddingNewRoundPolygon(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(RoundPolygon.CLASS_NAME);
        pointAddingController = new PointAddingController(Figure.TRIANGLE_SHAPE);
        if (Editor2D.localStatement!= FIRST_POINT_ADDING){
            Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
        }
        if (roundElementAddingProcess == null) roundElementAddingProcess = new RoundPolygonAdding();
        //if (roundElementAddingProcess == null) roundElementAddingProcess = new RoundPolygonAdding();
        System.out.println("Polygon adding menu is created");
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= FIRST_POINT_ADDING) {
            clearAndMinimizeTab(tab);
            if (localStatement != FIRST_POINT_ADDING) Editor2D.localStatement = FIRST_POINT_ADDING;
        }
        else if (localStatement == SECOND_POINT_ADDING) {
            createClearMenuWithCancelButton(tab);
        }
        else if (localStatement == THIRD_POINT_ADDING) {
            createClearMenuWithCancelButton(tab);
        }
        else if (localStatement == ANGLE_CHOOSING) {
            createAngleChoosingMenu(tab);
        }
        else if (localStatement == LIFE_SETTING) {
            createLifeSettingMenu(tab);
            for (androidGUI_Element element : tab.getElements()) {
                if (element.getClass() == androidAndroidGUI_Slider.class) {
                    ((androidAndroidGUI_Slider) element).setValue(BASIC_LIFE_VALUE);
                }
            }
        }
        else if (localStatement == BODY_TYPE_CHOOSING) {
            createBodyTypeChoosingMenu(tab);
        } else if (localStatement == SPRING_ADDING) {
            createSpringAddingMenu(tab);
        }
        else if (localStatement == NEW_OR_EXISTING_TILESET) {
            createNewOrExistingTilesetMenu(tab);
        }
        else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
            createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
        }
        else if (localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();
            clearAndMinimizeTab(tab);
        }
        else if (localStatement == TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);
        }

        else tab.clearElements();

    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        //System.out.println("Local: " + Editor2D.localStatement);
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (roundElementAddingProcess == null) roundElementAddingProcess = new RoundPolygonAdding();
        if (!roundElementAddingProcess.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, RoundPolygon.CLASS_NAME, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
            }
            //else System.out.println("Can " + pointAddingController.canBeNewObjectAdded() + "; St: " + Editor2D.localStatement);
        }
        else objectWasAdded(levelsEditorProcess);
    }

    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        roundElementAddingProcess = null;
        levelsEditorProcess.pointsOnMap.clear();
        //Editor2D.localStatement = FIRST_POINT_ADDING;
        System.out.println("The previous line is not right");
        makePauseToNextOperation();
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0){
            System.out.println("Local statement " + Editor2D.localStatement);
            for (androidGUI_Element releasedElement : releasedElements){
                if (releasedElement.getName() == CANCEL) {
                    Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                }
                else{
                    if (Editor2D.localStatement == ANGLE_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            Editor2D.setNextLocalStatement();
                            makePauseToNextOperation();
                            Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
                            objectData.setAngle((-figure.getAngle()));
                        }
                    }
                    else if (Editor2D.localStatement == LIFE_SETTING) {
                        lifeSetting(tab, releasedElement, objectData);
                        System.out.println("Actual menu is life setting ");
                    }
                    else if (Editor2D.localStatement == BODY_TYPE_CHOOSING) {
                        if (releasedElement.getName() == BODY_TYPE_STATIC_STRING) {
                            System.out.println("Static is pressed");
                            objectData.setBodyType(BodyType.STATIC);
                            Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                        } else if (releasedElement.getName() == BODY_TYPE_DYNAMIC_STRING) {
                            System.out.println("Dynamic is pressed");
                            objectData.setBodyType(BodyType.DYNAMIC);
                            Editor2D.setNextLocalStatement();
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == SPRING_ADDING) {
                        springAdding(tab, releasedElement, objectData);
                    }
                    else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) {
                        if (releasedElement.getName() == USE_EXISTING_GRAPHIC) {
                            Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
                        }
                        else if (releasedElement.getName() == LOAD_NEW_GRAPHIC) {
                            System.out.println("Try to load external graphic");
                            Editor2D.setNewLocalStatement(GET_TILESET_FROM_EXTERNAL_STORAGE);
                        } else if (releasedElement.getName() == WITHOUT_GRAPHIC) {
                            addTextToNewCreatedElement(RoundPolygon.objectToDisplayName, levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            Editor2D.setNewLocalStatement(END);
                            objectData.setPathToTexture("No_data");
                            setGraphicDataForRoundElementWithoutTexture(objectData);
                            tabController.zoneDeleting();
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) {
                        tilesetInDirectoryChoosing(tab, releasedElement, objectData);
                        Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
                    }
                    else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            addTextToNewCreatedElement(" round triangle ", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            textureRegionChoosing(tab, tabController, objectData);
                            Editor2D.setNewLocalStatement(END);
                            clearFiguresAndPoints(levelsEditorProcess);
                        }
                        makePauseToNextOperation();
                    }
                }
            }
        }
        if (pressedElements.size() > 0) {
            for (androidGUI_Element pressedElement : pressedElements) {
                if (Editor2D.localStatement == ANGLE_CHOOSING) {
                    if (levelsEditorProcess.figures.size() > 0) {
                        angleChoosingUpdating(levelsEditorProcess, pressedElement);
                    }
                }
            }
        }
    }

    protected void clearFiguresAndPoints(LevelsEditorProcess levelsEditorProcess) {
        levelsEditorProcess.figures.clear();
        levelsEditorProcess.pointsOnMap.clear();
    }


    @Override
    public byte getEndValue(){
        return END;
    }

    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_LIFE_VALUE = value;
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <=THIRD_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 3) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundElementAddingProcess.getLocalStatement() <FIRST_POINT_ADDING)
                    Editor2D.setNewLocalStatement(FIRST_POINT_ADDING);
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundElementAddingProcess.getLocalStatement() !=SECOND_POINT_ADDING) {
                    roundElementAddingProcess.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    Editor2D.setNewLocalStatement(SECOND_POINT_ADDING);
                    System.out.println("First point was added");
                    //roundElementAddingProcess.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundElementAddingProcess.getLocalStatement() !=SECOND_POINT_ADDING) {
                    roundElementAddingProcess.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    //roundElementAddingProcess.setNextStatement();
                    Editor2D.setNewLocalStatement(THIRD_POINT_ADDING);
                    System.out.println("Second point was added");
                }
                if (levelsEditorProcess.pointsOnMap.size() == 3 && roundElementAddingProcess.getLocalStatement() < ANGLE_CHOOSING) {
                    roundElementAddingProcess.addThirdPoint(levelsEditorProcess.pointsOnMap.get(2).getPosition());
                    //roundElementAddingProcess.setNextStatement();
                    addTriangleFigureOnMapZoneAndSaveData(levelsEditorProcess, objectData);
                    Editor2D.setNewLocalStatement(ANGLE_CHOOSING);
                }
            }
        }
    }

    protected void addTriangleFigureOnMapZoneAndSaveData(LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        if (levelsEditorProcess.pointsOnMap.size() > 1){
            Point firstPoint = levelsEditorProcess.pointsOnMap.get(0);
            Point secondPoint = levelsEditorProcess.pointsOnMap.get(1);
            Point thirdPoint = levelsEditorProcess.pointsOnMap.get(2);
            Vec2 center = GameMechanics.getTriangleGraphicCenter(firstPoint, secondPoint, thirdPoint);
            System.out.println("Triangle center is: " + (int)center.x + "x" + (int)center.y + "; Points are at: " + firstPoint + ", " + secondPoint + ", " + thirdPoint);
            ArrayList <Point> pointsForFigure = new ArrayList<>();
            pointsForFigure.add(firstPoint);
            pointsForFigure.add(secondPoint);
            pointsForFigure.add(thirdPoint);
            Figure figure = new Figure(pointsForFigure, center, Figure.TRIANGLE_SHAPE);
            levelsEditorProcess.addNewFigure(figure);
            for (Point point : levelsEditorProcess.pointsOnMap){
                point.hide(true);
            }
            int width = GameMechanics.getTriangleWidthByPoints(firstPoint, secondPoint, thirdPoint);
            int height = GameMechanics.getTriangleHeightByPoints(firstPoint, secondPoint, thirdPoint);
            objectData.setWidth(width);
            objectData.setGraphicWidth(width);
            objectData.setHeight(height);
            objectData.setGraphicHeight(height);
            objectData.setFirstPoint(new Vec2(firstPoint.getPosition().x, firstPoint.getPosition().y));
            objectData.setSecondPoint(new Vec2(secondPoint.getPosition().x, secondPoint.getPosition().y));
            objectData.setThirdPoint(new Vec2(thirdPoint.getPosition().x, thirdPoint.getPosition().y));

            System.out.println("Width " + width + "; height: " + height );
            objectData.setPosition(center);
        }
        else {
            System.out.println("There are not enough points");
        }   ////RoundPolygon 1:1090,70,0,9999,0,0%-25v25v25v25v25v-25#Tileset2.png;400x0x500x98x50x50

    }


    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
        if (onScreenConsole.canBeTextChanged()){
            //if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX) {
            try {
                if (roundElementAddingProcess.getLocalStatement() <=FIRST_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add first point by long tap");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==SECOND_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add second point by long tap");
                }
                else if (roundElementAddingProcess.getLocalStatement() == THIRD_POINT_ADDING) {
                    setTextForConsole(onScreenConsole, "Add third point by long tap");
                }
                else if (roundElementAddingProcess.getLocalStatement() == ANGLE_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Set angle");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==LIFE_SETTING) {
                    setTextForConsole(onScreenConsole, "Set life");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==BODY_TYPE_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose body type");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==SPRING_ADDING) {
                    setTextForConsole(onScreenConsole, "Add spring if you want");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==NEW_OR_EXISTING_TILESET) {
                    setTextForConsole(onScreenConsole, "Which graphic do you want to use?");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==GET_TILESET_FROM_EXTERNAL_STORAGE) {
                    setTextForConsole(onScreenConsole, "Choose graphic file from external storage");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==TILESET_IN_DIRECTORY_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose graphic file");
                }
                else if (roundElementAddingProcess.getLocalStatement() ==TEXTURE_REGION_CHOOSING) {
                    setTextForConsole(onScreenConsole, "Choose picture area for this triangle");
                }
                else if (roundElementAddingProcess.getLocalStatement() == END) {
                    setTextForConsole(onScreenConsole, "New round triangle was added");
                }
                else setTextForConsole(onScreenConsole, "Successfully!");
            }
            catch (Exception e) {
                System.out.println("Can not change the text of the console.  " +  (roundElementAddingProcess == null) + "; Trouble: " + e);
            }
        }

    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController) {
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
            else {
                if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, TilesetZone.SPRITE);
                }
            }
        }
        if (Editor2D.localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();
            updateFileInput();
        }
    }


}
