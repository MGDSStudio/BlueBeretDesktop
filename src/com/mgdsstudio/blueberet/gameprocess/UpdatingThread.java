package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.mainpackage.Program;

public class UpdatingThread extends Thread{
	private GameProcess gameProcess;
	private boolean ended = true;
	private boolean mustBeKilled;
	private boolean updatedForThisFrame;
	private int frameCount;
	private boolean twoRenderingByOneUpdating;

	UpdatingThread(GameProcess gameProcess){
		this.gameProcess = gameProcess;
		if (Program.NORMAL_FPS< Program.NORMAL_CPU_UPDATING_RATE){
			twoRenderingByOneUpdating = true;
		}
	}
	
	@Override
	public void run() {
		if (twoRenderingByOneUpdating) {
			updateForTwoGraphicUpdatingByOnePhysicStep();
		}
		else updateInSyncronizedThreads();
	}

	private void updateForTwoGraphicUpdatingByOnePhysicStep(){
		while (!mustBeKilled){
			if (!updatedForThisFrame) {
				gameProcess.updateGameInSingleThread();
				frameCount++;
				updatedForThisFrame = true;
			}
			else {
				try{
					sleep(1);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	private void updateInSyncronizedThreads(){
		while (!mustBeKilled){
			if (!updatedForThisFrame) {
				gameProcess.updateGameInSingleThread();
				frameCount++;
				updatedForThisFrame = true;
			}
			else {
				try{
					sleep(1);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}



	protected void setUpdatedForThisFrame(boolean flag){
		updatedForThisFrame = flag;
	}

	/*
	private void updateVar1(){
		ended = false;
		gameProcess.updateGameInSingleThread();
		ended = true;
	}*/

	/*
	public boolean isEnded(){
		return ended;
	}*/


}
