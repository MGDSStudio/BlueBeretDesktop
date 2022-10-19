package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.TextureDataToStore;
import com.mgdsstudio.blueberet.oldlevelseditor.*;

public class AddingNewStaticSpriteAction extends AddingNewIndependentGraphicAction {


    public AddingNewStaticSpriteAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        objectData.setClassName(IndependentOnScreenStaticSprite.CLASS_NAME);
        graphicType = TilesetZone.SPRITE;

    }

    @Override
    protected void textureRegionSelected(GameObjectDataForStoreInEditor objectData, androidGUI_ScrollableTab tab, ScrollableTabController tabController) {
        Editor2D.setNewLocalStatement(FLIP_STATEMENT_CHOOSING);
        TextureDataToStore data = new TextureDataToStore(tab.getTilesetZone().getGraphic(), tab.getTilesetZone().getGraphicChoosingArea().getStaticTextureOnTilesetCoordinates(), objectData.getFill());
        System.out.println("Fill was get: " + objectData.getFill());
        objectData.setStaticSpriteByTextureData(data);
        objectData.calculateGraphicDimentionsForRoundBox();
        tabController.zoneDeleting();
    }

    protected void updatePathSelectedMenu(androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData){
        super.updatePathSelectedMenu(releasedElement,objectData);
        //Editor2D.setNextLocalStatement();
        //objectData.setPathToTexture(releasedElement.getName());
        Editor2D.setNewLocalStatement(FILL_OR_STRING_TEXTURE);
        //makePauseToNextOperation();
    }

    @Override
    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        super.reconstructTab(tab, globalStatement, localStatement);
        if (localStatement == FILL_OR_STRING_TEXTURE) {
            createFillOrStringTextureMenu(tab);
        }
    }

    @Override
    protected void updateTabForStatement(LevelsEditorProcess levelsEditorProcess, androidGUI_Element releasedElement, GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController){
        if (Editor2D.localStatement == FILL_OR_STRING_TEXTURE) {
            updateFillOrStringTexture(releasedElement, levelsEditorProcess, objectData);
        }
        else if (Editor2D.localStatement == FLIP_STATEMENT_CHOOSING){
            if (releasedElement.getName() == FLIP) {
                addTextToNewCreatedElement("sprite", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                Editor2D.setNewLocalStatement(END);
                objectData.setFlip(true);
                makePauseToNextOperation();
            } else if (releasedElement.getName() == NO_FLIP) {
                addTextToNewCreatedElement("sprite", levelsEditorProcess, levelsEditorProcess.figures.get(0));
                Editor2D.setNewLocalStatement(END);
                objectData.setFlip(false);
                makePauseToNextOperation();
            }
        }
        else super.updateTabForStatement(levelsEditorProcess, releasedElement, objectData, tabController);
        }
    }
