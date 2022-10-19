package com.mgdsstudio.blueberet.oldlevelseditor;

//import com.mgdsstudio.blueberet.gameleveleditor.MapRedactor;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.engine.nesgui.Frame;
import processing.core.*;

import java.util.ArrayList;

public class MapZone {
	  //GameCamera camera;
	  final static byte SCALLING_DOWN = 1;
	  final static byte SCALLING_UP = -1;
	  final static float MAX_SCALE = 1f;
	  final static float MIN_SCALE = 0.5f;
	  final static float SCALLING_SPEED = 0.005f;
	  private final int gridNormalColor = Program.engine.color(5, 55);
	private final int gridFifthElementColor = Program.engine.color(150,25,25, 100);
	private final int gridNormalStrokeWeight = (int)(Program.engine.width/166.5f);
	private final int gridFifthElementStrokeWeight = gridNormalStrokeWeight+2;
	  
	  private final PVector nullPosition;
	  public int zoneWidth;
	  public int zoneHeight;  
	  public final PGraphics mapGraphic;
	  //private PImage frameAroundMap;
	  private boolean hideFrame = false;
	  private int additionalValue = 0;

	private ArrayList<Point> pointsOnMap = new ArrayList<>();
	private ArrayList<Figure> figures = new ArrayList<>();
	  
	  public MapZone(){
	    zoneWidth = (int)(Editor2D.zoneWidth);
	    zoneHeight = (int)(Editor2D.zoneHeight);
	    nullPosition = Editor2D.leftUpperCorner;
	    if (Program.graphicRenderer == Program.JAVA_RENDERER) mapGraphic = Program.engine.createGraphics((int)(Program.objectsFrame.width) , (int)(Program.objectsFrame.height), PApplet.JAVA2D);
	    else mapGraphic = Program.engine.createGraphics((int)(Program.objectsFrame.width) , (int)(Program.objectsFrame.height), PApplet.P2D);
		mapGraphic.smooth(Program.ANTI_ALIASING);
	  }

	public MapZone(Frame frame){
		zoneWidth = frame.getWidth();
		zoneHeight = frame.getHeight();
		nullPosition = new PVector(frame.getLeftX(), frame.getUpperY());
		if (Program.graphicRenderer == Program.JAVA_RENDERER) mapGraphic = Program.engine.createGraphics((int)(Program.objectsFrame.width) , (int)(Program.objectsFrame.height), PApplet.JAVA2D);
		else mapGraphic = Program.engine.createGraphics((int)(Program.objectsFrame.width) , (int)(Program.objectsFrame.height), PApplet.P2D);
		mapGraphic.smooth(Program.ANTI_ALIASING);
	}

	public PVector getNullPosition() {
		return nullPosition;
	}

	public float getLowerBoard(){
	    if (nullPosition != null || zoneHeight != 0){
	      return 2*nullPosition.y+zoneHeight;
	      
	    }
	    else {
	    	Program.engine.println("Don't have data about zone dimensions!");
	      return 0;
	    }
	  }
	  
	  public void update(){
	    //updateControl();
	  }



	public boolean isPointOnMapZone(float posX, float posY){
		if (posX>Editor2D.leftUpperCorner.x &&
				posX<Editor2D.rightLowerCorner.x &&
				posY>Editor2D.leftUpperCorner.y &&
				posY<Editor2D.rightLowerCorner.y){
			return true;
		}
		else return false;
	}
	  
	  
	  /*
	  public PVector getNearestPoint(PVector point){   
	    if (isPointOnMapZone(point)){
	      point.x = (point.x-camera.getActualPosition().x-nullPosition.x)/scale;
	      point.y = (point.y-camera.getActualPosition().y-nullPosition.y)/scale;
	      float nearestX = Game2D.engine.round(point.x/ Editor2D.gridSpacing);
	      float nearestY =  Game2D.engine.round(point.y/ Editor2D.gridSpacing);
	      point.x = nearestX*Editor2D.gridSpacing;
	      point.y = nearestY*Editor2D.gridSpacing;
	      return point;      
	    }
	    else {
	    	Game2D.engine.println("Point is not on the map zone!");
	      return null;
	    }
	  }
	  */


	  /*
	  PVector getNearestPoint(PVector point){    
	    point.x = point.x-nullPosition.x+camera.position.x;
	    point.y = point.y-nullPosition.y+camera.position.y;
	    float nearestX = round(point.x/ MapRedactor.gridSpacing);
	    float nearestY = round(point.y/ MapRedactor.gridSpacing);    
	    point.x = nearestX*MapRedactor.gridSpacing;
	    point.y = nearestY*MapRedactor.gridSpacing;
	    return point;
	  }
	  
	  */
	  
	  
	  public void moveCamera(GameCamera gameCamera, PVector shifting){
	    shifting.mult(gameCamera.getScale());
	    //camera.move(shifting);
		  gameCamera.translate(shifting);
	  }
	  
	  public boolean isPointVisible(PVector pos, GameCamera gameCamera){  // is it on the visible zone part?
	    if (pos.x>gameCamera.getActualPosition().x &&
	        pos.y>gameCamera.getActualPosition().y &&
	        pos.x<(gameCamera.getActualPosition().x+zoneWidth*gameCamera.getScale()) &&
	        pos.y<(gameCamera.getActualPosition().y+zoneHeight*gameCamera.getScale())) return true;
	    else return false;
	  }

	public void draw(GameCamera gameCamera, MapZone mapZone) {
		if (figures.size() > 0) {
			for (Figure figure : figures) {
				figure.draw(gameCamera, mapZone, null);
			}
		}
		if (pointsOnMap.size() > 0) {
			for (Point point : pointsOnMap) {
				point.draw(gameCamera, mapZone, null);
			}
		}
	}

	public ArrayList<Point> getPointsOnMap() {
		return pointsOnMap;
	}

	public ArrayList<Figure> getFigures() {
		return figures;
	}


}
