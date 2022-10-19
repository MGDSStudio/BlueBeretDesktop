package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PConstants;

public class LoadingScreen {
	//GameProcess gameProcess;
	private Image loadingRing;
	private static final float angularVelocity = PConstants.PI*0.55f;
	private float ringAngle = 0.1f;
	private int loadingFrameNumber = 0;
	private Timer loadingTimer;
	private final int CRITICAL_LOADING_TIME = 6000;

	//private boolean active;
	
	public LoadingScreen (){
		//this.gameProcess = gameProcess;.
		loadingRing = new Image(Program.getAbsolutePathToAssetsFolder("Progress bar element.png"));
		loadingRing.getImage().resize(Program.engine.width/8, Program.engine.width/8);
		loadingTimer = new Timer(CRITICAL_LOADING_TIME);
		//active = true;
	}
	
	public void draw() {
		ringAngle+= (angularVelocity/33);		
		Program.engine.background(254);
		Program.engine.pushMatrix();
		Program.engine.translate(Program.engine.width/2, Program.engine.height/2);
		Program.engine.rotate(ringAngle);
		Program.engine.image(loadingRing.getImage(), 0, 0);
		Program.engine.popMatrix();
		loadingFrameNumber++;
		testCriticalLoadingTime();
	}

	private void testCriticalLoadingTime(){
		if (loadingTimer.isTime()){
			Program.gameStatement = Program.SOME_MENU;
			System.out.println("Can not load. Try again");
			Program.canNotLoadLevelOrEditor = true;
		}
	}
	
	
}
