package com.mgdsstudio.blueberet.gameobjects.portals;


import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.flower.PlantController;
import com.mgdsstudio.blueberet.loading.GameObjectStringDataConstructor;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import org.jbox2d.dynamics.Body;

public class PipePortal extends PortalWithVisibleMoving  {

	
	public PipePortal(Flag enter, Flag exit, byte activatedBy, boolean transferDirection, boolean usingRepeateability) {
		this.enter = enter;
		this.exit = exit;
		this.activatedBy = activatedBy;
		this.transferDirection = transferDirection;	
		this.usingRepeateability = usingRepeateability;
	}

	public PipePortal(GameObjectDataForStoreInEditor data) {
		this.enter = data.getFirstFlag();
		this.exit = data.getSecondFlag();
		if (enter == null) System.out.println("First flag is null");
		this.activatedBy = data.getActivatedBy();
		this.transferDirection = data.getTransferDirection();
		this.usingRepeateability = data.getUsingRepeateability();
	}


	public boolean isPortalFreeFromFlower(PlantController plantController) {
		if (plantController.isAllPlantPartsKilled()){
			return true;
		}
		else {
			try{
				//boolean someElementOn = false;
				for (Body plantPart : plantController.getBodies()){
					if (plantPart != null){
						if (enter.inZone(PhysicGameWorld.controller.getBodyPixelCoord(plantPart))) return false;
					}
				}
				return true;
			}
			catch (Exception e){
				System.out.println(" Can not determine flower after exception 2");
				return true;
			}
		}
	}



	

	

	

	

	
	public byte getStage() {
		return stage;
	}
	

	


	public boolean getTransferDirection(){
		return transferDirection;
	}

	@Override
	public String getStringData() {
		GameObjectStringDataConstructor saveMaster = new GameObjectStringDataConstructor(this);
		saveMaster.createPortalSystem();
		System.out.println("Data string for portal system zone" +saveMaster.getDataString());
		return saveMaster.getDataString();
	}

	public String getClassName(){
		return CLASS_NAME;
	}


	public float getWidth() {
		return enter.getWidth();
	}


	public float getHeight() {
		return enter.getHeight();
	}

}
