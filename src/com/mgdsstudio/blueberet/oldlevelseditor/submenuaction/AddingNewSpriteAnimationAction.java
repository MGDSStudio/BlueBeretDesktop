package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.*;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.graphic.AnimationDataToStore;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import org.jbox2d.common.Vec2;

public class AddingNewSpriteAnimationAction extends AddingNewIndependentGraphicAction {
    private final static String ALONG_X = "Pictures along X";
    private final static String ALONG_Y = "Pictures along Y";
    private final static String FREQUENCY = "Frequency, sprites/sec";

    public final static byte ANIMATION_PARAMETERS_MENU = 15;
    private IndependentOnScreenAnimation animation;
    private static int previousValueAlongX = 3;
    private static int previousValueAlongY = 1;
    private static int previousFrequency = 8;

    public AddingNewSpriteAnimationAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(IndependentOnScreenStaticSprite.CLASS_NAME);
        graphicType = TilesetZone.ANIMATION;
    }

    @Override
    protected void textureRegionSelected(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, ScrollableTabController tabController) {
        Editor2D.setNewLocalStatement(ANIMATION_PARAMETERS_MENU);
        Animation animation = (Animation) tabController.getTab().getTilesetZone().getGraphic();
        objectData.setPathToTexture(animation.getPath());
        int[] coordinates = tabController.getTab().getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates();
        int [] leftUpper =  {coordinates[0], coordinates[1]};
        int [] rightLower =  {coordinates[2], coordinates[3]};
        System.out.println("Path was: " + objectData.getPathToTexture() + " or " + objectData.getPathToGraphic() + " and will be " + animation.getPath());
        objectData.setPathToTexture(animation.getPath());

        objectData.setLeftUpperGraphicCorner(new Vec2(leftUpper[0], leftUpper[1]));
        objectData.setRightLowerGraphicCorner(new Vec2(rightLower[0], rightLower[1]));
        AnimationDataToStore data = new AnimationDataToStore(objectData.getPathToTexture(), leftUpper, rightLower , objectData.getGraphicWidth(), objectData.getGraphicHeight(), (byte)previousValueAlongX, (byte)previousValueAlongY, (byte)previousFrequency);
        objectData.setSpriteAnimationByTextureData(data);
        tabController.zoneDeleting();
    }

    private void saveAnimation(){
        /*
        int[] coordinates = tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates();
        System.out.println("Graphic width: " + objectData.getGraphicWidth() + "; Width: " + objectData.getWidth());
        AnimationDataToStore data = new AnimationDataToStore(tabController.getTab().getTilesetZone().getGraphic().getPath(), (byte)coordinates[0], coordinates[1], coordinates[2], coordinates[3], objectData.getGraphicWidth(), objectData.getGraphicHeight(), (byte)objectData.getRowsNumber(), (byte)objectData.getCollumnsNumber(), objectData.getAnimationFrequency());
        objectData.setSpriteAnimationByTextureData(data);
        objectData.calculateGraphicDimentionsForRoundBox();
        tabController.zoneDeleting();*/
    }

    protected void createAnimation(GameObjectDataForStoreInEditor objectData){
        //SpriteAnimation spriteAnimation = new SpriteAnimation();
        //animation = new IndependentOnScreenAnimation();
    }

    protected void updatePathSelectedMenu(androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData){
        super.updatePathSelectedMenu(releasedElement,objectData);
        Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
    }

    //(GUI_ScrollableTab tab, int min, int max, String textFieldText, int savedValue){


    private void createAnimationParametersMenu(androidGUI_ScrollableTab tab){
        tab.clearElements();
        int min = 1; int max = 8;
        addSliderWithEmbeddedTextField(tab, min, max, ALONG_X, previousValueAlongX);
        addSliderWithEmbeddedTextField(tab, min, max, ALONG_Y, previousValueAlongY);
        min = 1;
        max = Program.NORMAL_FPS/2;
        addSliderWithEmbeddedTextField(tab, min, max, FREQUENCY, previousFrequency);
        androidGUI_Element buttonApply = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR , androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonApply, null);
        androidGUI_Element buttonBack = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR , androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, BACK, false);
        tab.addGUI_Element(buttonBack, null);
        androidGUI_Element buttonCancel = new androidAndroidGUI_Button(new Vec2(((3 * tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, CANCEL, false);
        tab.addGUI_Element(buttonCancel, null);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        super.reconstructTab(tab, globalStatement, localStatement);
        if (localStatement == ANIMATION_PARAMETERS_MENU) {
            createAnimationParametersMenu(tab);
        }
    }

    @Override
    protected void updateTabForStatement(LevelsEditorProcess levelsEditorProcess, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController){
        if (Editor2D.localStatement == ANIMATION_PARAMETERS_MENU) {
            updateAnimationParametersMenu(releasedElement, levelsEditorProcess, objectData);
            for (androidGUI_Element pressed : tabController.getPressedElements()){
                updateAnimationParametersMenu(pressed, levelsEditorProcess, objectData);
            }
            //updateAnimationParametersMenu(pressedElement, levelsEditorProcess, objectData);
        }
        else super.updateTabForStatement(levelsEditorProcess, releasedElement, objectData, tabController);
    }

    private void updateAnimationParametersMenu(androidGUI_Element releasedElement, LevelsEditorProcess levelsEditorProcess, GameObjectDataForStoreInEditor objectData){
        System.out.println("Released " + releasedElement.getClass() + " has name " + releasedElement.getName() + ". Is it ALONG_X" +  (releasedElement.getName() == ALONG_X)) ;
        Animation animation = levelsEditorProcess.figures.get(levelsEditorProcess.figures.size()-1).getAnimation();
        if (releasedElement.getName() == ALONG_X) {
            int value = 3;
            if (releasedElement.getClass() == androidAndroidGUI_Slider.class){
                androidAndroidGUI_Slider slider = (androidAndroidGUI_Slider) releasedElement;
                value = slider.getValue();
            }
            else if (releasedElement.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField field = (androidAndroidGUI_TextField) releasedElement;
                value = field.getValue();
            }
            if (value!= previousValueAlongX) {
                System.out.println("New value for Along x " + value);
                previousValueAlongX = value;
                objectData.setRowsNumber(value);
            }
            animation.setAlongX(value);
        }
        else if (releasedElement.getName() == ALONG_Y) {
            int value = 8;
            if (releasedElement.getClass() == androidAndroidGUI_Slider.class){
                androidAndroidGUI_Slider slider = (androidAndroidGUI_Slider) releasedElement;
                value = slider.getValue();
            }
            else if (releasedElement.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField field = (androidAndroidGUI_TextField) releasedElement;
                value = field.getValue();
            }
            if (value!= previousValueAlongY) {
                System.out.println("New value for Along y " + value);
                previousValueAlongY = value;
                objectData.setCollumnsNumber(value);

            }
            animation.setAlongY(value);
        }
        else if (releasedElement.getName() == FREQUENCY) {
            int value = 1;
            if (releasedElement.getClass() == androidAndroidGUI_Slider.class){
                androidAndroidGUI_Slider slider = (androidAndroidGUI_Slider) releasedElement;
                value = slider.getValue();
            }
            else if (releasedElement.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField field = (androidAndroidGUI_TextField) releasedElement;
                value = field.getValue();
            }
            if (value!= previousFrequency) {
                System.out.println("New value for frequency " + value);
                previousFrequency = value;
                objectData.setAnimationFrequency(value);
            }
            animation.setFrequency(value);
        }
        else if (releasedElement.getName() == APPLY){
            addTextToNewCreatedElement("Animation", levelsEditorProcess, levelsEditorProcess.figures.get(0));
            objectData.setRowsNumber(previousValueAlongX);
            objectData.setCollumnsNumber(previousValueAlongY );
            objectData.setAnimationFrequency(previousFrequency);
            objectData.setClassName(IndependentOnScreenAnimation.CLASS_NAME);
            Editor2D.setNewLocalStatement(END);
        }
        else if (releasedElement.getName() == BACK){
            Editor2D.setNewLocalStatement(TEXTURE_REGION_CHOOSING);
        }
    }
/*
    @Override
    protected void textureRegionSelected(GameObjectDataForStoreInEditor objectData,  GUI_ScrollableTab tab, ScrollableTabController tabController) {

    }*/

}
