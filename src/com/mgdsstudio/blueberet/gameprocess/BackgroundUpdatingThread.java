package com.mgdsstudio.blueberet.gameprocess;

public class BackgroundUpdatingThread  extends Thread{
    private boolean backgroundForThisFrameAlreadyDrawn = true;
    GameProcess gameProcess;

    public BackgroundUpdatingThread(GameProcess gameProcess){
        this.gameProcess = gameProcess;
    }

    @Override
    public void run() {
        if (gameProcess.isFrameStarted()) {
            backgroundForThisFrameAlreadyDrawn = false;
            gameProcess.drawBackgrounds();
            backgroundForThisFrameAlreadyDrawn = true;
        }

        /*
        PhysicGameWorld.init();
        if (Programm.gameStatement == Programm.GAME_PROCESS) gameProcess.gameCamera = new GameCamera(new PVector(Programm.engine.width/2, Programm.engine.height/2), GameCamera.CAMERA_IN_GAME);

        gameProcess.objectsActiveLoader = new ObjectsActiveLoader();
        gameProcess.changeRound(Programm.actualRoundNumber);
        gameProcess.init();
        if  (Programm.gameStatement == Programm.LEVELS_EDITOR) {
            gameProcess.levelsEditorProcess = new LevelsEditorProcess(gameProcess.gameRound, gameProcess.gameCamera);
            gameProcess.gameCamera = new GameCamera(new PVector(gameProcess.gameRound.getPlayer().getAbsolutePosition().x,gameProcess.gameRound.getPlayer().getAbsolutePosition().y), GameCamera.CAMERA_IN_EDITOR);

        }
        gameProcess.gameControl = new GameControl(Programm.OS);
        gameProcess.fps_HUD = new FPS_HUD();
        //gameProcess.gameCamera.setNewActualPosition(new PVector(2500,0));
        try{
            gameProcess.gameCamera.setNewActualPosition(gameProcess.gameRound.getPlayer().getAbsolutePosition());
        }
        catch (Exception e){
            System.out.println("Can not place camera on player. Set on 0:0" + e);
            gameProcess.gameCamera.setNewActualPosition(new PVector(0,0));
        }
        gameProcess.roundLoaded = true;

         */
        this.interrupt();
    }

    public boolean isBackgroundForThisFrameAlreadyDrawn() {
        return backgroundForThisFrameAlreadyDrawn;
    }
}
