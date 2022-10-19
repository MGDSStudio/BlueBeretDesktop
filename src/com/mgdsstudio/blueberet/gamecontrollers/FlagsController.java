package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;

import java.util.ArrayList;

public class FlagsController implements ISimpleUpdateable {
	ArrayList<Flag> flags = new ArrayList<Flag>();
	
	public FlagsController(ArrayList<Flag> flags) {
		for (int i = 0; i < flags.size(); i++) {
			this.flags.add(flags.get(i));
		}		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	
}
