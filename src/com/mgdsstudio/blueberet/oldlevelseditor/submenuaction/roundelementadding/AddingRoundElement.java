package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.roundelementadding;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.ObjectWithSetableFormAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.PointAddingController;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.RectangularElementAdding;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.RoundElementAddingConstants;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.SubmenuAction;
import com.mgdsstudio.blueberet.loading.LoadingMaster;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class AddingRoundElement extends SubmenuAction implements RoundElementAddingConstants{
    protected PointAddingController pointAddingController;
    protected ObjectWithSetableFormAdding roundElementAddingProcess;
    protected final String UP = "↑";
    protected final String DOWN = "↓";
    protected final String LEFT = "←";
    protected final String RIGHT = "→";
    protected String BODY_TYPE_STATIC_STRING = "Static";
    protected String BODY_TYPE_DYNAMIC_STRING = "Dynamic";
    //protected String objectDebugGraphicName = "";
//protected final String objectDebugGraphicName = "";

    public AddingRoundElement(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
    }




    protected final void createLifeSettingMenu(androidGUI_ScrollableTab tab){
        createMenuWithSliderAndTextField(tab, "Immortal", "Life", 1, GameObject.IMMORTALY_LIFE, GameObject.IMMORTALY_LIFE);
        /*
        tab.clearElements();
        GUI_Element slider = new GUI_Slider(new Vec2(((tab.getWidth() / 2)), Game2D.engine.width/10.4f), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR , "", 1, GameObject.IMMORTALY_LIFE);
        slider.setText("Immortal");
        slider.setUserValue(GameObject.IMMORTALY_LIFE);
        GUI_Element textField = new GUI_TextField(new Vec2(((tab.getWidth() / 2)), Game2D.engine.width/3.918f), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Life", true, 1, GameObject.IMMORTALY_LIFE);
        ((GUI_TextField) textField).addCoppeledSlider((GUI_Slider) slider);
        ((GUI_Slider) slider).addCoppeledTextField((GUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);

        GUI_Element buttonApply = new GUI_Button(new Vec2(((tab.getWidth() / 4)), Game2D.engine.width/2.467f), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Apply", false);
        tab.addGUI_Element(buttonApply, null);
        GUI_Element buttonCancel = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Game2D.engine.width/2.467f), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);*/
    }



    protected final void createFlowerDiameterSettingMenu(androidGUI_ScrollableTab tab, int min, int max){
        createMenuWithSliderAndTextField(tab, null, "Jaw diameter", min, max, max);
        /*
        tab.clearElements();
        GUI_Element slider = new GUI_Slider(new Vec2(((tab.getWidth() / 2)), Game2D.engine.width/10.4f), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.9f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR , "", 1, GameObject.IMMORTALY_LIFE);
        slider.setText("Immortal");
        slider.setUserValue(GameObject.IMMORTALY_LIFE);
        GUI_Element textField = new GUI_TextField(new Vec2(((tab.getWidth() / 2)), Game2D.engine.width/3.918f), (int) (GUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Life", true, 1, GameObject.IMMORTALY_LIFE);
        ((GUI_TextField) textField).addCoppeledSlider((GUI_Slider) slider);
        ((GUI_Slider) slider).addCoppeledTextField((GUI_TextField) textField);
        tab.addGUI_Element(slider, null);
        tab.addGUI_Element(textField, null);

        GUI_Element buttonApply = new GUI_Button(new Vec2(((tab.getWidth() / 4)), Game2D.engine.width/2.467f), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Apply", false);
        tab.addGUI_Element(buttonApply, null);
        GUI_Element buttonCancel = new GUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Game2D.engine.width/2.467f), GUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, GUI_Button.NORMAL_HEIGHT_IN_REDACTOR, "Cancel", false);
        tab.addGUI_Element(buttonCancel, null);
        */
    }

    protected final void createBodyTypeChoosingMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.46f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BODY_TYPE_STATIC_STRING, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.46f + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BODY_TYPE_DYNAMIC_STRING, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), Program.engine.width/10.46f + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }

    protected final void createSpringAddingMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        androidGUI_Element buttonStatic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WITH_SPRING, false);
        tab.addGUI_Element(buttonStatic, null);
        androidGUI_Element buttonDynamic = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, WITHOUT_SPRING, false);
        tab.addGUI_Element(buttonDynamic, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 2)), 30 + 2 * (int) (6f * androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR / 5f)), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(null);
    }








    protected final void createBackgroundsInDirectoryChoosingMenu(androidGUI_ScrollableTab tab, String startNameToFind, String [] extensions){
        tab.clearElements();
        ArrayList<String> filesInGameDirectory;
        filesInGameDirectory = Program.iEngine.getFilesListInAssets();
        ArrayList<String> imageFilesFromGameDirectory = StringLibrary.deleteFromArrayAllStringsExceptOne(filesInGameDirectory, startNameToFind);
        ArrayList<String> imagesWithPrefix = new ArrayList<>();
        for (int j = 0; j < extensions.length; j++){
            ArrayList<String> imagesWithPrefixForActualExtension = StringLibrary.leaveInArrayFilesWithExtension(imageFilesFromGameDirectory, extensions[j]);
            for (int i = 0; i < imagesWithPrefixForActualExtension.size(); i++){
                String pathToBackground = StringLibrary.getStringAfterPathDevider(imagesWithPrefixForActualExtension.get(i));
                imagesWithPrefix.add(pathToBackground);
            }
        }
        System.out.println("Backgrounds:");
        for (String name : imagesWithPrefix){
            System.out.println(name);
        }
        System.out.println("Backgrounds end");
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < imagesWithPrefix.size(); i++) {
            images.add(StringLibrary.getStringAfterPathDevider(imagesWithPrefix.get(i)));
            //images.add(StringLibrary.deleteAssetsFromPath(imagesWithPrefix.get(i)));
            androidGUI_Element button = new androidAndroidGUI_ImageButton(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR * 1.45f), "", false, images.get(i), false);
            tab.addGUI_Element(button, null);
        }
        androidGUI_Element button = new androidAndroidGUI_Button(new Vec2(0, 0), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 0.6f), (int) (androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR), CANCEL, false);
        tab.addGUI_Element(button, null);

        repositionGUIAlongInTwoCollumns(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }







    @Override
    public abstract void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement);





    protected final void springAdding(androidGUI_ScrollableTab tab, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData ){
        if (releasedElement.getName() == WITH_SPRING) {
            objectData.setWithSpring(true);
            Editor2D.setNextLocalStatement();
        }
        else if (releasedElement.getName() == WITHOUT_SPRING) {
            objectData.setWithSpring(false);
            Editor2D.setNextLocalStatement();
        }
    }

    protected final void tilesetInDirectoryChoosing(androidGUI_ScrollableTab tab, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData ){
        Editor2D.setNextLocalStatement();
        objectData.setPathToTexture(releasedElement.getName());
        makePauseToNextOperation();
    }

    protected final void angleChoosingUpdating(LevelsEditorProcess levelsEditorProcess, androidGUI_Element pressedElement ){
        if (pressedElement.getClass() == androidAndroidGUI_AnglePicker.class) {
            Figure figure = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size() - 1);
            if (figure.getAngle() != (int) pressedElement.getValue()) {
                figure.setAngle((int) pressedElement.getValue());
            }
        }
    }

    protected final void textureRegionChoosing(androidGUI_ScrollableTab tab, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData ){
        Editor2D.setNextLocalStatement();
        TextureDataToStore data = new TextureDataToStore(tab.getTilesetZone().getGraphic(), tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), objectData.getFill());
        objectData.setStaticTextureData(data);
        objectData.calculateGraphicDimentionsForRoundBox();
        makePauseToNextOperation();
        tabController.zoneDeleting();
    }

    @Override
    public void draw(GameCamera gameCamera, LevelsEditorProcess levelsEditorProcess){
        pointAddingController.draw(gameCamera, levelsEditorProcess);
    }

    protected final void lifeSetting(androidGUI_ScrollableTab tab, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData ){
        if (releasedElement.getName() == APPLY) {
            Editor2D.setNextLocalStatement();
            makePauseToNextOperation();
            for (androidGUI_Element guiElement : tab.getElements()) {
                if (guiElement.getClass() == androidAndroidGUI_Slider.class) {
                    objectData.setLife((int) guiElement.getValue());
                    saveBasicLifeValue(objectData.getLife());
                }
            }
        }
    }

    protected final void diameterSetting(androidGUI_ScrollableTab tab, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData ){
        if (releasedElement.getName() == APPLY) {
            Editor2D.setNextLocalStatement();
            makePauseToNextOperation();
            for (androidGUI_Element guiElement : tab.getElements()) {
                if (guiElement.getClass() == androidAndroidGUI_Slider.class) {
                    objectData.setDiameter((int) guiElement.getValue());
                    //saveBasicLifeValue(objectData.getLife());
                }
            }
        }
    }

    protected void setGraphicDataForRoundElementWithoutTexture(GameObjectDataForStoreInEditor objectData) {
        Vec2 rightLowerCorner = LoadingMaster.getGraphicRightLowerCornerForRoundBoxWithoutGraphic(objectData.getWidth(), objectData.getHeight());
        objectData.setLeftUpperCorner(new PVector(0,0));
        objectData.setLeftUpperGraphicCorner(new Vec2(0,0));
        objectData.setRightLowerGraphicCorner(rightLowerCorner);
        System.out.println("Right lower graphic corner is: " + rightLowerCorner.x + "x" + rightLowerCorner.y);
    }

    protected abstract void saveBasicLifeValue(int value);


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




    protected abstract void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> pressedElements, ArrayList<androidGUI_Element> releasedElements);

    }
