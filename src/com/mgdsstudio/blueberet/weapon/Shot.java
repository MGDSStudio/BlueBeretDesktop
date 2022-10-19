package com.mgdsstudio.blueberet.weapon;

import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.IDrawable;
import com.mgdsstudio.blueberet.gameobjects.ISimpleUpdateable;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

import processing.core.PVector;

public class Shot extends GameObject implements IDrawable, ISimpleUpdateable {

	Shot(PVector position, float angle) {
		// TODO Auto-generated constructor stub
		super(position, angle);	
	}	
		
	public void update() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(GameCamera gameCamera) {
		// TODO Auto-generated method stub
		
	}
}
