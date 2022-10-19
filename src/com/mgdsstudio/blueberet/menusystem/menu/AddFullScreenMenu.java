package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.androidspecific.MainActivity;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;
import processing.core.PGraphics;

public class AddFullScreenMenu extends AddMenu{
    private Timer timer;
    private final int TIME_TO_JUMP_TO_NEXT_MENU = 200;

    public AddFullScreenMenu(PApplet engine, PGraphics graphics, Object userValue) {
        super(engine, userValue);
        this.type = MenuType.FULL_SCREEN_ADD_MENU;
              /*if (userValue.getClass() == MenuType.class){
            nextMenu = (MenuType) userValue;
        }
        if (Program.withAdds && Program.OS == Program.ANDROID){
            System.out.println("Start to upload add");
            MainActivity mainActivity = (MainActivity) engine.getActivity();
            mainActivity.stopMusicForAdd();
            mainActivity.initRewardedAdd();
            addProgress =  true;
        }*/
    }

    @Override
    protected void initAdd(PApplet engine){
        //MainActivity mainActivity = (MainActivity) engine.getActivity();
        Program.iEngine.stopMusicForAdd();
        Program.iEngine.initFullScreenAdd();
        //mainActivity.stopMusicForAdd();
        //mainActivity.initFullScreenAdd();
    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (timer != null){
            if (timer.isTime()){
                toNextMenu(gameMenusController);
            }
        }
        if (addProgress){
            MainActivity mainActivity = (MainActivity) gameMenusController.getEngine().getActivity();
            gameMenusController.stopMusic();
            if (Program.iEngine.isFullScreenAddCompleted()){
                if (Program.iEngine.isFullScreenAdStartedToUpload()){
                    if (Program.iEngine.isFullScreenAddCompleted()){

                        if (timer == null) {
                            timer = new Timer(TIME_TO_JUMP_TO_NEXT_MENU);
                            gameMenusController.stopMusic();
                        }
                    }
                }
            }

            /*else if (mainActivity.isRewardedAppIsDismissed()){
                rewardPlayerForAd(gameMenusController);
            }

            if (mainActivity.isRewardedAppFailedToUpload()){
                gameMenusController.backToPrevMenu(type, nextMenu);
                mainActivity.resetRewardAdd();
            }*/
        }

    }

    private void toNextMenu(GameMenusController gameMenusController) {
        gameMenusController.setNewMenu(nextMenu);
        Program.iEngine.resetFullScreenAdd();
        //MainActivity mainActivity = (MainActivity) gameMenusController.getEngine().getActivity();
        //mainActivity.resetFullScreenAdd();
        gameMenusController.stopMusic();
    }

}
