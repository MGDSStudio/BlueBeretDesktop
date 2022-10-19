package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Slider;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding.AddingRoundElement;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.dynamics.BodyType;

import java.util.ArrayList;

public class AddingNewRoundBoxAction extends AddingRoundElement implements RoundElementAddingConstants{
    private static int BASIC_LIFE_VALUE = 1;

    public AddingNewRoundBoxAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(RoundBox.CLASS_NAME);
        pointAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= RectangularElementAdding.FIRST_POINT_ADDING) {
            clearAndMinimizeTab(tab);
        }
        if (localStatement == RectangularElementAdding.ANGLE_CHOOSING) {
            createAngleChoosingMenu(tab);
        }
        else if (localStatement == RectangularElementAdding.LIFE_SETTING) {
            createLifeSettingMenu(tab);
            for (androidGUI_Element element : tab.getElements()) {
                if (element.getClass() == androidAndroidGUI_Slider.class) {
                    ((androidAndroidGUI_Slider) element).setValue(BASIC_LIFE_VALUE);
                }
            }
        }
        else if (localStatement == RectangularElementAdding.BODY_TYPE_CHOOSING) {
            createBodyTypeChoosingMenu(tab);

        } else if (localStatement == RectangularElementAdding.SPRING_ADDING) {
            createSpringAddingMenu(tab);
        }
        else if (localStatement == RectangularElementAdding.NEW_OR_EXISTING_TILESET) {
            createNewOrExistingTilesetMenu(tab);
        }
        else if (localStatement == RectangularElementAdding.FILL_OR_STRING_TEXTURE) {
            createFillOrStringTextureMenu(tab);
        }
        else if (localStatement == RectangularElementAdding.TILESET_IN_DIRECTORY_CHOOSING) {
            createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
        }
        else if (localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();

            clearAndMinimizeTab(tab);
            updateFileInput();
        }
        else if (localStatement == RectangularElementAdding.TEXTURE_REGION_CHOOSING) {
            createTextureRegionChoosingMenu(tab);
        }
        else tab.clearElements();

    }



    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX)
                    tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, pressedElements, releasedElements);
            }
            else {
                if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX) {
                    if (Editor2D.localStatement == RectangularElementAdding.TEXTURE_REGION_CHOOSING) {
                        zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_LAST_FIGURE, TilesetZone.SPRITE);
                    }
                }
            }
        }
        if (Editor2D.localStatement == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE){
            //updateNewGraphicAdding();
            updateFileInput();
        }
    }

    @Override
    protected void saveBasicLifeValue(int value){
        BASIC_LIFE_VALUE = value;
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList <androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements) {
            //System.out.println("Released elements : " + releasedElements.size());
        if (releasedElements.size()>0){
            for (androidGUI_Element releasedElement : releasedElements){
                if (releasedElement.getName() == CANCEL) {
                    System.out.println("Cancel is pressed " + releasedElement.getClass());
                    Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_BOX);
                    Editor2D.setNewLocalStatement(RectangularElementAdding.FIRST_POINT_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                    makePauseToNextOperation();
                }
                else{
                    if (Editor2D.localStatement == RectangularElementAdding.ANGLE_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            Editor2D.setNextLocalStatement();
                            makePauseToNextOperation();
                            Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
                            objectData.setAngle((int) (-figure.getAngle()));
                        }
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.LIFE_SETTING) {
                        lifeSetting(tab, releasedElement, objectData);
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.BODY_TYPE_CHOOSING) {
                        if (releasedElement.getName() == "Static") {
                            System.out.println("Static is pressed");
                            objectData.setBodyType(BodyType.STATIC);
                            Editor2D.setNewLocalStatement(RectangularElementAdding.NEW_OR_EXISTING_TILESET);
                        } else if (releasedElement.getName() == "Dynamic") {
                            System.out.println("Dynamic is pressed");
                            objectData.setBodyType(BodyType.DYNAMIC);
                            Editor2D.setNextLocalStatement();
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.SPRING_ADDING) {
                       springAdding(tab, releasedElement, objectData);
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.NEW_OR_EXISTING_TILESET) {
                        if (releasedElement.getName() == USE_EXISTING_GRAPHIC) {
                            Editor2D.setNewLocalStatement(RectangularElementAdding.TILESET_IN_DIRECTORY_CHOOSING);
                        }
                        else if (releasedElement.getName() == LOAD_NEW_GRAPHIC) {
                            System.out.println("Try to load external graphic");
                            Editor2D.setNewLocalStatement(RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE);
                        } else if (releasedElement.getName() == WITHOUT_GRAPHIC) {
                            addTextToNewCreatedElement("round box", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            Editor2D.setNewLocalStatement(RectangularElementAdding.END);
                            objectData.setPathToTexture("No_data");
                            setGraphicDataForRoundElementWithoutTexture(objectData);
                            tabController.zoneDeleting();
                        }
                        else if (releasedElement.getName() == BACK) {
                            Editor2D.setNewLocalStatement(RectangularElementAdding.BODY_TYPE_CHOOSING);
                        }
                        makePauseToNextOperation();
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.TILESET_IN_DIRECTORY_CHOOSING) {
                        tilesetInDirectoryChoosing(tab, releasedElement, objectData);
                    }
                    else if (Editor2D.localStatement == RectangularElementAdding.FILL_OR_STRING_TEXTURE) {
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
                    else if (Editor2D.localStatement == RectangularElementAdding.TEXTURE_REGION_CHOOSING) {
                        if (releasedElement.getName() == APPLY) {
                            addTextToNewCreatedElement("round box", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                            textureRegionChoosing(tab, tabController, objectData);
                        }
                        makePauseToNextOperation();
                    }
                }
            }
        }
        if (pressedElements.size() > 0) {
            for (androidGUI_Element pressedElement : pressedElements) {
                if (Editor2D.localStatement == RectangularElementAdding.ANGLE_CHOOSING) {
                    if (levelsEditorProcess.figures.size() > 0) {
                        angleChoosingUpdating(levelsEditorProcess, pressedElement);
                    }
                }
            }
        }
    }

    /*
    private void setGraphicDataForBoxWithoutTexture(GameObjectDataForStoreInEditor objectData) {
        Vec2 rightLowerCorner = LoadingMaster.getGraphicRightLowerCornerForRoundBoxWithoutGraphic(objectData.getWidth(), objectData.getHeight());
        objectData.setLeftUpperCorner(new PVector(0,0));
        objectData.setLeftUpperGraphicCorner(new Vec2(0,0));
        objectData.setRightLowerGraphicCorner(rightLowerCorner);
        System.out.println("Right lower graphic corner is: " + rightLowerCorner.x + "x" + rightLowerCorner.y);
    }
*/

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl){
            if (onScreenConsole.canBeTextChanged()){
                //if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX) {
                    try {
                        if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.FIRST_POINT_ADDING) {
                            setTextForConsole(onScreenConsole, "Add first point by long tap");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.SECOND_POINT_ADDING) {
                            setTextForConsole(onScreenConsole, "Add second point by long tap");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.ANGLE_CHOOSING) {
                            setTextForConsole(onScreenConsole, "Set angle of the box on angle picker");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.LIFE_SETTING) {
                            setTextForConsole(onScreenConsole, "Set life");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.BODY_TYPE_CHOOSING) {
                            setTextForConsole(onScreenConsole, "Choose body type");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.SPRING_ADDING) {
                            setTextForConsole(onScreenConsole, "Add spring if you want");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.NEW_OR_EXISTING_TILESET) {
                            setTextForConsole(onScreenConsole, "Which graphic do you want to use?");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.UPLOAD_TILESET_FROM_STORAGE_TO_CACHE) {
                            setTextForConsole(onScreenConsole, "Choose graphic file from external storage");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.TILESET_IN_DIRECTORY_CHOOSING) {
                            setTextForConsole(onScreenConsole, "Choose graphic file");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.FILL_OR_STRING_TEXTURE) {
                            setTextForConsole(onScreenConsole, "How to apply texture");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.TEXTURE_REGION_CHOOSING) {
                            setTextForConsole(onScreenConsole, "Choose picture area for this object");
                        }
                        else if (roundElementAddingProcess.getLocalStatement() == RectangularElementAdding.END) {
                            setTextForConsole(onScreenConsole, "New round box was added");
                        }
                    } catch (Exception e) {
                        System.out.println("Can not change the text of the console " + e);
                        ArrayList<String> actualConsoleText = new ArrayList<>();
                        actualConsoleText.add("Successfully");
                        onScreenConsole.setText(actualConsoleText);
                    }
                //}
            }

    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (roundElementAddingProcess == null) roundElementAddingProcess = new RectangularElementAdding();
        if (!roundElementAddingProcess.isCompleted()) {
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
            if (pointAddingController.canBeNewObjectAdded() && Editor2D.localStatement < RectangularElementAdding.ANGLE_CHOOSING){
                pointAddingController.addNewObject(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, null);
                System.out.println("Added new polygon");
            }
        }
        else objectWasAdded(levelsEditorProcess);
    }



    private void objectWasAdded(LevelsEditorProcess levelsEditorProcess){
        roundElementAddingProcess = null;
        levelsEditorProcess.pointsOnMap.clear();
        Editor2D.localStatement = RectangularElementAdding.FIRST_POINT_ADDING;
        System.out.println("The previous line is not right");
        makePauseToNextOperation();
    }

    @Override
    protected void updatePointAdding(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess, String type, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= RectangularElementAdding.SECOND_POINT_ADDING) {
            if (levelsEditorProcess.pointsOnMap.size() <= 2) {
                pointAddingController.update(gameCamera, levelsEditorProcess);
                if (levelsEditorProcess.pointsOnMap.size() == 0 && roundElementAddingProcess.getLocalStatement() != RectangularElementAdding.FIRST_POINT_ADDING)
                    roundElementAddingProcess.setNextStatement();
                if (levelsEditorProcess.pointsOnMap.size() == 1 && roundElementAddingProcess.getLocalStatement() != RectangularElementAdding.SECOND_POINT_ADDING) {
                    roundElementAddingProcess.addFirstPoint(levelsEditorProcess.pointsOnMap.get(0).getPosition());
                    roundElementAddingProcess.setNextStatement();
                }
                if (levelsEditorProcess.pointsOnMap.size() == 2 && roundElementAddingProcess.getLocalStatement() < RectangularElementAdding.ANGLE_CHOOSING) {
                    roundElementAddingProcess.addSecondPoint(levelsEditorProcess.pointsOnMap.get(1).getPosition());
                    roundElementAddingProcess.setNextStatement();
                    Editor2D.localStatementChanged = true;
                    if (levelsEditorProcess.getGameRound() == null) System.out.println("Game round is null");
                    addRectFigureOnMapZoneAndSaveData((byte)1, levelsEditorProcess, objectData);
                }
            }
        }
    }

    /*
    private void makeRoundElementSmall(GameObjectDataForStoreInEditor objectData) {
        if (mustBeElementMadeSmaller(objectData)) {
            objectData.setWidth((int) (objectData.getWidth() - 1));
            objectData.setHeight((int) (objectData.getHeight() - 1));
        }
    }*/

    @Override
    public byte getEndValue(){
        return RectangularElementAdding.END;
    }


}
