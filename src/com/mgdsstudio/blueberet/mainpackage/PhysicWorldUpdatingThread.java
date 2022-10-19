package com.mgdsstudio.blueberet.mainpackage;



public class PhysicWorldUpdatingThread extends Thread{	
	
	@Override
	public void run() {		
		while (true) {			
			PhysicGameWorld.update();
		}
	}
}
