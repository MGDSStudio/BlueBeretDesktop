package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.Bridge;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectWithSetableFormAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class AddingNewBridgeAction  extends SubmenuWithTwoRectZones{
    //Not finished

    //private PointAddingController pointsAddingController;
    //private RectangularElementAdding secondZoneAdding, firstZoneAdding;
    //Statements
    private final static byte FIRST_BRIDGE_CORNER_ADDING = FIRST_POINT_ADDING;
    private final static byte SECOND_BRIDGE_CORNER_ADDING = SECOND_POINT_ADDING;
    private final static byte FIRST_ZONE_CORNER_ADDING = THIRD_POINT_ADDING;
    private final static byte SECOND_ZONE_CORNER_ADDING = FOURTH_POINT_ADDING;
    private final static byte SEGMENTS_NUMBER_SELECTING = 5;
    private final static byte NEW_OR_EXISTING_TILESET = RectangularElementAdding.NEW_OR_EXISTING_TILESET;
    public final static byte TILESET_IN_DIRECTORY_CHOOSING = RectangularElementAdding.TILESET_IN_DIRECTORY_CHOOSING;
    public final static byte TEXTURE_REGION_CHOOSING  = RectangularElementAdding.TEXTURE_REGION_CHOOSING;
    public final static byte COMPLETED = 12;
    public final static byte END = COMPLETED;
    private int maxSegmentsNumber = 10;
    private static int segmentsNumber = 3;
    private int bridgeWidth, bridgeHeight;

    //protected PointAddingController pointAddingController;
    protected ObjectWithSetableFormAdding roundElementAddingProcess;

    public AddingNewBridgeAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(Bridge.CLASS_NAME);
        pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
        Editor2D.localStatement = FIRST_BRIDGE_CORNER_ADDING;
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= FIRST_BRIDGE_CORNER_ADDING) tab.clearElements();
        else if (localStatement == SECOND_BRIDGE_CORNER_ADDING){}
        else if (localStatement == FIRST_ZONE_CORNER_ADDING) createClearMenuWithCancelButton(tab);
        else if (localStatement == SECOND_ZONE_CORNER_ADDING) createClearMenuWithCancelButton(tab);
        else if (localStatement == SEGMENTS_NUMBER_SELECTING) createMenuWithSliderAndTextField(tab, null, "Segments", 2, getMaxSegmentsNumber(), segmentsNumber);
        else if (localStatement == NEW_OR_EXISTING_TILESET) createNewOrExistingTilesetMenu(tab);
        else if (localStatement == TILESET_IN_DIRECTORY_CHOOSING) createGraphicFileInDirectoryChoosingMenu(tab, tilesetStartName, tilesetExtension);
        else if (localStatement == RectangularElementAdding.TEXTURE_REGION_CHOOSING) createTextureRegionChoosingMenu(tab);
    }

    private int getMaxSegmentsNumber() {
        if (bridgeWidth > 0 && bridgeHeight > 0){
            int number = PApplet.ceil(bridgeWidth/bridgeHeight)+1;
            if (number<3) number = 3;
            return number;
        }
        else {
            System.out.println("Bridge dims are 0");
            return 10;
        }
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            try {
                if (Editor2D.localStatement <= FIRST_BRIDGE_CORNER_ADDING) setTextForConsole(onScreenConsole, "Set first point for bridge");
                else if (Editor2D.localStatement == SECOND_BRIDGE_CORNER_ADDING) setTextForConsole(onScreenConsole, "Set second point for bridge");
                else if (Editor2D.localStatement == FIRST_ZONE_CORNER_ADDING) setTextForConsole(onScreenConsole, "Set first corner for activating zone");
                else if (Editor2D.localStatement == SECOND_ZONE_CORNER_ADDING) setTextForConsole(onScreenConsole, "Set second corner for activating zone");
                else if (Editor2D.localStatement == SEGMENTS_NUMBER_SELECTING) setTextForConsole(onScreenConsole, "Enter number of single bridge parts");
                else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET) setTextForConsole(onScreenConsole, "Which graphic do you want to use?");
                else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING) setTextForConsole(onScreenConsole, "Select graphic file you want to use");
                else if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) setTextForConsole(onScreenConsole, "Choose picture area for the bridge element");
                else{
                    setTextForConsole(onScreenConsole, "Successfully added");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateMaxSegmentsNumber(LevelsEditorProcess process) {
        Figure figure = process.figures.get(0);
        float width = figure.getWidth();
        float height = figure.getHeight();
        if (width>height){
            maxSegmentsNumber = PApplet.ceil(width/height)*2;
        }
        else maxSegmentsNumber = 2;
    }

//(GUI_ScrollableTab tab, String sliderMaxText, String textFieldText, int min, int max, int startValue){

    private void loadFirstCornerAddingScreen(androidGUI_ScrollableTab tab){
        tab.clearElements();
        /*
        GUI_Element buttonGem = new GUI_Button(new Vec2(((tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, GEM, false);
        tab.addGUI_Element(buttonGem, null);
        GUI_Element buttonCoin = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MONEY_COIN, false);
        tab.addGUI_Element(buttonCoin, null);
        GUI_Element buttonAmmo = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, AMMO, false);
        tab.addGUI_Element(buttonAmmo, null);
        GUI_Element buttonMedicalKit = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, MEDICAL_KIT, false);
        tab.addGUI_Element(buttonMedicalKit, null);
        GUI_Element buttonSlowMo = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), 135), GUI_Button.NORMAL_WIDTH_IN_REDACTOR, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, SYRINGE, false);
        tab.addGUI_Element(buttonSlowMo, null);
        repositionGUIAlongY(tab, tab.getElements());

        tab.recalculateHeight(tab.getElements());*/
    }

    @Override
    public byte getEndValue() {
        return END;
    }

    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        updateTabController(objectData, levelsEditorProcess, tabController);
        if (firstZoneAdding == null && Editor2D.localStatement < THIRD_POINT_ADDING) firstZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement == THIRD_POINT_ADDING && secondZoneAdding == null) secondZoneAdding = new RectangularElementAdding();
        if (Editor2D.localStatement <= FOURTH_POINT_ADDING) {
            if (pointsAddingController == null) pointsAddingController = new PointAddingController(Figure.RECTANGULAR_SHAPE);
            updatePointAdding(levelsEditorProcess.getEditorCamera(), levelsEditorProcess, ExternalRoundDataFileController.roundBoxType, objectData);
            if (Editor2D.localStatement > FOURTH_POINT_ADDING){
                saveBridgeDimensionsForElementsNumberCalculation(levelsEditorProcess.figures);
                System.out.println("Try to save bridge geomentric data");
                Figure bridgeFigure = levelsEditorProcess.figures.get(0);
                objectData.setLeftUpperCorner(new PVector(bridgeFigure.getCenter().x - bridgeFigure.getWidth()/2, bridgeFigure.getCenter().y - bridgeFigure.getHeight()/2));
                bridgeFigure.setFillForSprite(true);
                objectData.setWidth(bridgeFigure.getWidth());
                objectData.setHeight(bridgeFigure.getHeight());
                Figure flagFigure = levelsEditorProcess.figures.get(1);
                PVector zoneCenter = new PVector(flagFigure.getCenter().x, flagFigure.getCenter().y);
                Flag activatingZone = new Flag(zoneCenter, flagFigure.getWidth(), flagFigure.getHeight(), Flag.BRIDGE_CRUSH);
                objectData.setFlag(activatingZone);
                System.out.println("Bridge geometric data were saved");
            }
        }
    }

    private void saveBridgeDimensionsForElementsNumberCalculation(ArrayList<Figure> figures) {
        bridgeWidth = figures.get(0).getWidth();
        bridgeHeight = figures.get(0).getHeight();
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointsAddingController.draw(gameCamera, levelsEditorProcess);
    }

    private void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            ArrayList<androidGUI_Element> pressedElements = tabController.getPressedElements();
            ArrayList<androidGUI_Element> releasedElements = tabController.getReleasedElements();
            if (pressedElements.size() >0 || releasedElements.size()>0) {
                tabUpdating(pressedElements, releasedElements, objectData, levelsEditorProcess.getGameRound(), tabController, levelsEditorProcess);
            }
            else {
                if (Editor2D.localStatement == TEXTURE_REGION_CHOOSING) {
                    if (tabController.getTab().getTilesetZone() == null) {
                        zoneCreating(objectData, tabController.getTab(), TilesetZone.LINK_TO_FIRST_FIGURE, TilesetZone.SPRITE);
                        levelsEditorProcess.figures.get(0).setFillForSprite(true);
                    }
                }
            }
        }
    }

    protected void tabUpdating(ArrayList <androidGUI_Element> pressedElements, ArrayList <androidGUI_Element> releasedElements, GameObjectDataForStoreInEditor objectData, GameRound gameRound, ScrollableTabController tabController, LevelsEditorProcess levelsEditorProcess) {
        if (Editor2D.localStatement == SEGMENTS_NUMBER_SELECTING) {
            updateSegmentsNumberMenu(pressedElements, releasedElements, gameRound, objectData, levelsEditorProcess);
        }
        else if (Editor2D.localStatement == NEW_OR_EXISTING_TILESET){
            updateNewOrExistingTilesetMenu(pressedElements, releasedElements, gameRound, objectData, levelsEditorProcess);
        }
        else if (Editor2D.localStatement == TILESET_IN_DIRECTORY_CHOOSING){
            updateTilesetInDirChoosingMenu(pressedElements, releasedElements, gameRound, objectData, levelsEditorProcess);
        }
        else{
            updateTextureZone(releasedElements, gameRound, objectData, tabController, levelsEditorProcess);
        }
    }

    private void updateTextureZone(ArrayList<androidGUI_Element> releasedElements, GameRound gameRound, GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, LevelsEditorProcess levelsEditorProcess) {
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getName() == APPLY) {
                    Editor2D.setNewLocalStatement(COMPLETED);
                    androidGUI_ScrollableTab tab = tabController.getTab();
                    TextureDataToStore data = new TextureDataToStore(tab.getTilesetZone().getGraphic(), tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), false);
                    objectData.setStaticSpriteByTextureData(data);
                    objectData.calculateGraphicDimentionsForRoundBox();
                    tabController.zoneDeleting();
                }
                else if (androidGui_element.getName() == CANCEL){
                    Editor2D.setNewLocalStatement(FIRST_BRIDGE_CORNER_ADDING);
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                }
            }

        }

    }





    private void updateTilesetInDirChoosingMenu(ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements, GameRound gameRound, GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess) {
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
                objectData.setPathToTexture(androidGui_element.getName());
                objectData.setFill(true);
            }
            else if (androidGui_element.getName() == CANCEL){
                Editor2D.setNewLocalStatement(FIRST_BRIDGE_CORNER_ADDING);
                levelsEditorProcess.pointsOnMap.clear();
                levelsEditorProcess.figures.clear();
            }
        }
    }


    protected void updateNewOrExistingTilesetMenu(ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements, GameRound gameRound, GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess) {
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getName() == USE_EXISTING_GRAPHIC) {
                    Editor2D.setNewLocalStatement(TILESET_IN_DIRECTORY_CHOOSING);
                }
                else if (androidGui_element.getName() == LOAD_NEW_GRAPHIC) {

                }
                else if (androidGui_element.getName() == CANCEL){
                    levelsEditorProcess.pointsOnMap.clear();
                    levelsEditorProcess.figures.clear();
                        Editor2D.setNewLocalStatement(FIRST_BRIDGE_CORNER_ADDING);

                }
            }
        }
    }

    private void updateSegmentsNumberMenu(ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements, GameRound gameRound, GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess) {
        for (int i = 0; i < releasedElements.size(); i++) {
            androidGUI_Element androidGui_element = releasedElements.get(i);
            if (androidGui_element.getStatement() == androidGUI_Element.RELEASED) {
                if (androidGui_element.getClass() == androidAndroidGUI_Button.class) {
                    if (androidGui_element.getName() == APPLY){
                        Editor2D.setNewLocalStatement(NEW_OR_EXISTING_TILESET);
                        objectData.setSegmentsNumber(segmentsNumber);
                        System.out.println("Saved with segments number: " + segmentsNumber);
                    }
                    else if (androidGui_element.getName() == CANCEL){
                        Editor2D.setNewLocalStatement(FIRST_BRIDGE_CORNER_ADDING);
                        levelsEditorProcess.pointsOnMap.clear();
                        levelsEditorProcess.figures.clear();
                    }
                }
                else if (androidGui_element.getClass() == androidAndroidGUI_Slider.class || androidGui_element.getClass() == androidAndroidGUI_TextField.class) {
                    if (androidGui_element.getValue() > 2) {
                        int value;
                        value = androidGui_element.getValue();
                        segmentsNumber = value;
                        System.out.println("Slider value was updated to " + segmentsNumber);
                    }
                }
            }
        }
    }

}
