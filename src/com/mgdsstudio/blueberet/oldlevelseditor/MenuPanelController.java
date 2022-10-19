package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_MenuLowLevelTabType;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_MenuPanel;
import com.mgdsstudio.blueberet.mainpackage.GameMainController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.MenuType;

public class MenuPanelController {
    androidGUI_MenuPanel menuPanel;
    LevelsEditorProcess levelsEditorProcess;

    public MenuPanelController(androidGUI_MenuPanel menuPanel, LevelsEditorProcess levelsEditorProcess) {
        this.menuPanel = menuPanel;
        this.levelsEditorProcess = levelsEditorProcess;
    }

    public void update(GameRound gameRound, GameMainController gameMainController) {
        if (menuPanel.getActualPressedElementAction() != null) {
            setNewAction(menuPanel.getActualPressedElementAction(), gameRound, gameMainController);
            menuPanel.returnShiftedComboBoxToNull();
            //resetLocalStatement();
        }

    }

    private void setNewAction(androidGUI_MenuLowLevelTabType action, GameRound gameRound, GameMainController gameMainController) {
        switch (action) {
            case EXIT:
                //Program.gameStatement = Program.MAIN_MENU;
                levelsEditorProcess.writeObjectsDataToRoundFile();
                levelsEditorProcess.saveCameraPos();
                gameMainController.jumpToMenu(MenuType.MAIN);
                return;
            case SAVE_MAP:
                levelsEditorProcess.writeObjectsDataToRoundFile();
                return;
            case OPEN_MAP:
                Editor2D.setNewGlobalStatement(Editor2D.OPEN_MAP);
                return;
            case NEW_MAP:
                Editor2D.setNewGlobalStatement(Editor2D.NEW_MAP);
                return;
            case TEST_MAP:
                Program.levelLaunchedFromRedactor = true;
                Program.jumpFromRedactorToGame = true;
                boolean loadedFromEditor = true;
                gameMainController.getMenusController().setUserValue(new Boolean(loadedFromEditor));
                levelsEditorProcess.writeObjectsDataToRoundFile();
                levelsEditorProcess.saveCameraPos();
                gameMainController.jumpToMenu(MenuType.USER_LEVEL_LOADING);
                Editor2D.setNewLocalStatement((byte) 0);
                Editor2D.setNewGlobalStatement(Editor2D.SELECTION_CANCEL);
                return;
            case NEW_ROUND_RECTANGULAR:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_BOX);
                return;
            case NEW_ROUND_CIRCLE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_CIRCLE);
                return;
            case NEW_ROUND_POLYGON:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_POLYGON);
                return;
            case PLACE_PLAYER:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_PLAYER);
                return;//levelsEditorProcess.placeObjectToMousePlace(gameRound.getPlayer()); return;
            case PLACE_GUMBA:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_GUMBA);
                return;
            case PLACE_BOWSER:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_BOWSER);
                return;
            case PLACE_SPIDER:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_SPIDER);
                return;
            case PLACE_SNAKE:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_SNAKE);
                return;
            case PLACE_KOOPA:
                Editor2D.setNewGlobalStatement(Editor2D.PLACE_KOOPA);
                return;
            case NEW_CLEARING_ZONE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_OBJECT_CLEARING_ZONE);
                return;
            case NEW_SPRITE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_INDEPENDENT_STATIC_SPRITE);
                return;
            case NEW_ANIMATION:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_INDEPENDENT_SPRITE_ANIMATION);
                return;
            case CLEAR_MAP:
                Editor2D.setNewGlobalStatement(Editor2D.MAP_CLEARING);
                return;
            case GRID_PREFERENCES:
                Editor2D.setNewGlobalStatement(Editor2D.GRID_PREFERENCES);
                return;
            case SELECT:
                Editor2D.setNewGlobalStatement(Editor2D.OBJECT_SELECTING);
                return;
            case EDIT_OBJECT:
                Editor2D.setNewGlobalStatement(Editor2D.OBJECT_EDITING);
                return;
            case DELETE:
                Editor2D.setNewGlobalStatement(Editor2D.OBJECT_DELETING);
                return;
            case NEW_CAMERA_FIXATION_ZONE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_CAMERA_FIXATION_ZONE);
                return;
            case NEW_LEVEL_END_ZONE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_END_LEVEL_ZONE);
                return;
            case CANCEL:
                Editor2D.setNewGlobalStatement(Editor2D.SELECTION_CANCEL);
                return;
            case NEW_PORTAL_SYSTEM_ZONE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_PORTAL_SYSTEM);
                return;
            case NEW_PLATFORM_SYSTEM:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_PLATFORM_SYSTEM);
                return;
            case NEW_ROUND_PIPE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_PIPE);
                return;
            case COPY:
                Editor2D.setNewGlobalStatement(Editor2D.COPY_ELEMENT); return;
            case MOVE:
                Editor2D.setNewGlobalStatement(Editor2D.MOVE_ELEMENT); return;
            case NEW_BRIDGE:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_BRIDGE); return;
            case NEW_COLLECTABLE_OBJECT:
                Editor2D.setNewGlobalStatement(Editor2D.ADDING_COLLECTABLE_OBJECT); return;
            case BACKGROUND:
                Editor2D.setNewGlobalStatement(Editor2D.BACKGROUND); return;
            default: {
                System.out.println("There are no action for this button! " + action.name());
                return;
            }
        }

    }

    /*
    case EXIT: Game2D.gameStatement = Game2D.MAIN_MENU; levelsEditorProcess.writeObjectsDataToRoundFile(); return;
            case SAVE_MAP: levelsEditorProcess.writeObjectsDataToRoundFile(); return;
            case TEST_MAP: Game2D.gameStatement = Game2D.GAME_PROCESS; Game2D.levelLaunchedFromRedactor = true; Game2D.jumpFromRedactorToGame = true; levelsEditorProcess.writeObjectsDataToRoundFile(); return;
            case NEW_ROUND_RECTANGULAR: Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_BOX); return;
            case NEW_ROUND_CIRCLE: Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_CIRCLE); return;
            case NEW_ROUND_POLYGON: Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_ROUND_POLYGON); return;
            case PLACE_PLAYER: Editor2D.setNewGlobalStatement(Editor2D.PLACE_PLAYER); return;//levelsEditorProcess.placeObjectToMousePlace(gameRound.getPlayer()); return;
            case PLACE_GUMBA: Editor2D.setNewGlobalStatement(Editor2D.PLACE_GUMBA); return;
            case PLACE_BOWSER: Editor2D.setNewGlobalStatement(Editor2D.PLACE_BOWSER); return;
            case PLACE_KOOPA: Editor2D.setNewGlobalStatement(Editor2D.PLACE_KOOPA); return;
            case NEW_CLEARING_ZONE: Editor2D.setNewGlobalStatement(Editor2D.ADDING_OBJECT_CLEARING_ZONE); return;
            case NEW_SPRITE: Editor2D.setNewGlobalStatement(Editor2D.ADDING_NEW_INDEPENDEND_STATIC_SPRITE); return;
            case CLEAR_MAP: Editor2D.setNewGlobalStatement(Editor2D.MAP_CLEARING);return;
            case GRID_PREFERENCES: Editor2D.setNewGlobalStatement(Editor2D.GRID_PREFERENCES); resetLocalStatement(); return;
            default: return;
    */

    private void resetLocalStatement() {

        Editor2D.localStatement = 0;
    }

    private void setGlobalStatement(byte statement) {
        //if (pressedDroppedDownElement == )
        Editor2D.setNewGlobalStatement(statement);
    }

    /*
    public boolean isMouseOnSomeButton(){
        if (menuPanel.isSomeComboBoxShifted()){
            if (menuPanel.getShiftedComboBox().isMouseOnElement(new Vec2(Game2D.engine.mouseX, Game2D.engine.mouseY), PApplet.CORNER)){
                System.out.println("Mouse is on the element " + menuPanel.getShiftedComboBox().toString());
                return true;
            }
        }
        return false;
    }
    */
}
