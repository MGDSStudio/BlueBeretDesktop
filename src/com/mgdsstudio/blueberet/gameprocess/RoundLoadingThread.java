package com.mgdsstudio.blueberet.gameprocess;

public class RoundLoadingThread extends Thread {
	GameProcess gameProcess;
	
	public RoundLoadingThread(GameProcess gameProcess){
		this.gameProcess = gameProcess;
	}
	
	@Override
	public void run() {
		gameProcess.gameCreating(null);
		this.interrupt();
		/*
		try {
			PhysicGameWorld.init();
			if (Programm.gameStatement == Programm.GAME_PROCESS)
				gameProcess.gameCamera = new GameCamera(new PVector(Programm.engine.width / 2, Programm.engine.height / 2), GameCamera.CAMERA_IN_GAME);

			gameProcess.objectsActiveLoader = new ObjectsActiveLoader();
				gameProcess.changeRound(Programm.actualRoundNumber);

				gameProcess.init();
			gameProcess.gameControl = new GameControl((Soldier) gameProcess.gameRound.getPlayer());
				//gameProcess.fps_HUD = new FPS_HUD();
			try {
				if (Programm.gameStatement == Programm.LEVELS_EDITOR) {
					gameProcess.gameCamera = new GameCamera(new PVector(gameProcess.gameRound.getPlayer().getPositionInPrevFrame().x, gameProcess.gameRound.getPlayer().getPositionInPrevFrame().y), GameCamera.CAMERA_IN_EDITOR);
					gameProcess.levelsEditorProcess = new LevelsEditorProcess(gameProcess.gameRound, gameProcess.gameCamera);
				}
				gameProcess.gameCamera.setNewActualPosition(gameProcess.gameRound.getPlayer().getAbsolutePosition());
			} catch (Exception e) {
				System.out.println("Can not place camera on player. Set on 0:0" + e);
				gameProcess.gameCamera = new GameCamera(new PVector(Programm.engine.width / 2, Programm.engine.height / 2), GameCamera.CAMERA_IN_GAME);
				gameProcess.levelsEditorProcess = new LevelsEditorProcess(gameProcess.gameRound, gameProcess.gameCamera);
				gameProcess.gameCamera.setNewActualPosition(new PVector(0, 0));
			}
			gameProcess.roundLoaded = true;
			this.interrupt();
		}
		catch (Exception e){
			System.out.println("Something goes wrong by level loading; " );
			e.printStackTrace();
			//Programm.troubleByLoading = true;
		}
		System.out.println("Round loaded");

		 */
	}


}
