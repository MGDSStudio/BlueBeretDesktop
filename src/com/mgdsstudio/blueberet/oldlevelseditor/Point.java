package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;
import processing.core.PVector;

public class Point {
	  Vec2 pos;
	  final static byte NOT_ACTIVE = 1;
	  final static byte SELECTED = 2;
	  byte statement = NOT_ACTIVE;
	  
	  final static int NOT_ACTIVE_COLOR = 0xff7A6262;
	  final static int SELECTED_COLOR = 0xff8E76D8;
	  
	  final static int NOT_ACTIVE_STROKE_WEIGHT = (int)(Program.engine.width/33.3f);
	  final static int SELECTED_STROKE_WEIGHT = (int)(Program.engine.width/33.3f);

	  private boolean hidden = false;
	  public static boolean ON_OBJECT_FRAME = true;

	  public Point(PVector pos){
	  	if (pos != null) this.pos = new Vec2(pos.x, pos.y);
	  	else System.out.println("The position of point is null");
	  }

	  public void hide(boolean flag ){
		  hidden = flag;
	  }
	  
	  public void draw(GameCamera gameCamera, MapZone mapZone, PVector nullPosition){
	  	if (!hidden) {
		  if (ON_OBJECT_FRAME){
			  drawOnObjectFrame(gameCamera, Program.objectsFrame);
		  }
		  else {
			  drawOnMapZone(gameCamera, mapZone);
		  }
		}
	  }

	private void drawOnObjectFrame(GameCamera gameCamera, PGraphics graphic) {
		graphic.pushMatrix();
		graphic.scale(Program.OBJECT_FRAME_SCALE);
		graphic.pushStyle();
		graphic.noFill();
		graphic.translate(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
		if (statement == NOT_ACTIVE) {
			graphic.strokeWeight(NOT_ACTIVE_STROKE_WEIGHT);
			graphic.stroke(NOT_ACTIVE_COLOR);
		} else if (statement == SELECTED) {
			graphic.strokeWeight(SELECTED_STROKE_WEIGHT);
			graphic.stroke(SELECTED_COLOR);
		} else {
			graphic.strokeWeight(SELECTED_STROKE_WEIGHT);
			graphic.stroke(SELECTED_COLOR);
		}
		graphic.point(0, 0);
		graphic.popStyle();
		graphic.popMatrix();
	}

	private void drawOnMapZone(GameCamera gameCamera, MapZone mapZone) {
		mapZone.mapGraphic.pushMatrix();
		mapZone.mapGraphic.scale(Program.OBJECT_FRAME_SCALE);
		mapZone.mapGraphic.pushStyle();
		mapZone.mapGraphic.noFill();
		//mapZone.mapGraphic.translate(, );
		if (statement == NOT_ACTIVE) {
			mapZone.mapGraphic.strokeWeight(NOT_ACTIVE_STROKE_WEIGHT);
			mapZone.mapGraphic.stroke(NOT_ACTIVE_COLOR);
		} else if (statement == SELECTED) {
			mapZone.mapGraphic.strokeWeight(SELECTED_STROKE_WEIGHT);
			mapZone.mapGraphic.stroke(SELECTED_COLOR);
		} else {
			mapZone.mapGraphic.strokeWeight(SELECTED_STROKE_WEIGHT);
			mapZone.mapGraphic.stroke(SELECTED_COLOR);
		}
		mapZone.mapGraphic.point(pos.x - gameCamera.getActualXPositionRelativeToCenter(), pos.y - gameCamera.getActualYPositionRelativeToCenter());
		mapZone.mapGraphic.popStyle();
		mapZone.mapGraphic.popMatrix();
	}

	public Vec2 getPosition(){
	    return pos;
	  }

	  @Override
	public String toString(){
		  return (""+(int)pos.x+"x"+(int)pos.y);
	  }

}
