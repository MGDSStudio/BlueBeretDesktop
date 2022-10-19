package com.mgdsstudio.blueberet.oldlevelseditor;


import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.*;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.*;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.AddingNewSpriteAnimationAction;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;


public final class ScrollableTabController {
    /*
    final static String CANCEL = "Cancel";
    final static String APPLY = "Apply";
    final static String FILL_WITH_TILES = "Fill with tiles";
    final static String STRETCH_GRAPHIC = "Stretch graphic";
    final static String USE_EXISTING_GRAPHIC = "Use existing graphic";
    final static String LOAD_NEW_GRAPHIC = "Load from external storage";
    final static String WITHOUT_GRAPHIC = "Without graphic";
    final static String WITH_SPRING = "With spring";
    final static String WITHOUT_SPRING = "Without spring";*/
    private final ArrayList<androidGUI_Element> mutableReleaseElements = new ArrayList<>();
    private final ArrayList<androidGUI_Element> mutablePressedElements = new ArrayList<>();

    final static public int MINIMAL_FREE_SPACE = 10;

    androidGUI_ScrollableTab tab;
    //MapZone mapZone;
    public final Vec2 basicPosition;
    //boolean scrollingAlongX, scrollingAlongY;

    ScrollableTabController(androidGUI_ScrollableTab tab) {
        this.tab = tab;
        basicPosition = tab.getLeftUpperCorner();    // This is a link not a copy
        init();
    }

    private void init() {
    }


    public androidGUI_ScrollableTab getTab(){
        return tab;
    }


    public void zoneDeleting() {
        if (tab.getTilesetZone() != null) {
            tab.deleteTilesetZone();
        }
    }

    public boolean mustBeLocalStatementReset(SubmenuAction submenuAction) {
        if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_BOX) {
            if (Editor2D.localStatement == RectangularElementAdding.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_CIRCLE) {
            if (Editor2D.localStatement == RoundCircleAdding.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_POLYGON) {
            if (Editor2D.localStatement == AddingNewRoundPolygon.END) {
                return true;
            }
            else return false;
        }
        if (Editor2D.getGlobalStatement() == Editor2D.ADDING_COLLECTABLE_OBJECT) {
            if (Editor2D.localStatement == AddingNewCollectableObjectAction.END) {
                return true;
            }
            else return false;
        }
        if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_STATIC_SPRITE) {
            if (Editor2D.localStatement == AddingNewStaticSpriteAction.END) {
                return true;
            }
            else return false;
        }
        if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_INDEPENDENT_SPRITE_ANIMATION) {
            if (Editor2D.localStatement == AddingNewSpriteAnimationAction.END) {
                return true;
            }
            else return false;
        }
        if (Editor2D.getGlobalStatement() == Editor2D.OBJECT_SELECTING) {
            if (Editor2D.localStatement == SelectingAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_OBJECT_CLEARING_ZONE) {
            if (Editor2D.localStatement == ObjectsClearingZoneAdding.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_END_LEVEL_ZONE) {
            if (Editor2D.localStatement == SingleFlagZoneAdding.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_GUMBA || Editor2D.getGlobalStatement() == Editor2D.PLACE_BOWSER) {
            if (Editor2D.localStatement == Simple_NPC_AddingController.END) {
                System.out.println("NPC can be added");
                return true;

            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_KOOPA) {
            if (Editor2D.localStatement == AddingNewKoopaAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.PLACE_PLAYER) {
            if (Editor2D.localStatement == PlayerReplacingAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.MAP_CLEARING) {
            if (Editor2D.localStatement == ClearMapAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PORTAL_SYSTEM) {
            if (Editor2D.localStatement == AddingNewPortalSystemAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_PLATFORM_SYSTEM) {
            if (Editor2D.localStatement == AddingNewPlatformSystemAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_CAMERA_FIXATION_ZONE) {
            if (Editor2D.localStatement == AddingNewCameraFixationZoneAction.END) {
                System.out.println("Local statement is end statement for camera fixation zone");
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_BRIDGE) {
            if (Editor2D.localStatement == AddingNewBridgeAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.COPY_ELEMENT) {
            if (Editor2D.localStatement == CopyAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.ADDING_NEW_ROUND_PIPE) {
            if (Editor2D.localStatement == AddingNewRoundPipeAction.END) {
                return true;
            }
            else return false;
        }
        else if (Editor2D.getGlobalStatement() == Editor2D.OPEN_MAP){
            if (Editor2D.localStatement == OpenMapAction.END) {
                return true;
            }
            else return false;
        }
        else{
            byte endValueForMenu = submenuAction.getEndValue();
            //System.out.println("Test statement: "  + Editor2D.localStatement + " must be " + endValueForMenu);
            if (Editor2D.localStatement == endValueForMenu){
                System.out.println("End of menu");
                return true;
            }
            else return false;
        }


    }



    public void resetLocalStatementTest() {
        Editor2D.setNewLocalStatement((byte) 0);
    }




    public ArrayList<androidGUI_Element> getPressedElements(){
        //ArrayList<GUI_Element> pressedElements = new ArrayList<>();
        mutablePressedElements.clear();
        for (androidGUI_Element element: tab.getElements()){
            if (element.getStatement() == androidGUI_Element.PRESSED) mutablePressedElements.add(element);
        }
        return mutablePressedElements;
    }

    public ArrayList<androidGUI_Element> getReleasedElements(){
        //ArrayList<GUI_Element> releaseElements = new ArrayList<>();
        mutableReleaseElements.clear();
        for (androidGUI_Element element: tab.getElements()){
            if (element.getStatement() == androidGUI_Element.RELEASED) mutableReleaseElements.add(element);
        }
        return mutableReleaseElements;
    }

    public void update(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess) {

        if (Editor2D.canBeNextOperationMade()) {

        }

    }

    private void makePauseToNextOperation() {
        Editor2D.resetTimer();
    }


}
