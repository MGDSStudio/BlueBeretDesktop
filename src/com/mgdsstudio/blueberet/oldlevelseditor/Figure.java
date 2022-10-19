package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.MovablePlatform;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Animation;
import com.mgdsstudio.blueberet.graphic.simplegraphic.SimpleGraphicUnit;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.GraphicDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.SingleImageDataForFigures;
import com.mgdsstudio.blueberet.oldlevelseditor.objectsadding.figuresgraphicdata.SingleSpriteAnimationDataForFigures;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.*;

import java.util.ArrayList;

public class Figure {
		private PShape shapeGraphic;
		private SimpleGraphicUnit sprite;
		private Animation animation;
		int[] vertexPositions;
		//private ImageZoneSimpleData imageZoneSimpleData = new ImageZoneSimpleData(0,0,1,0);
	  public final static byte RECTANGULAR_SHAPE = 1;
	  public final static byte TRIANGLE_SHAPE = 2;
	  public final static byte CIRCLE_SHAPE = 3;
		public final static byte SINGLE_POINT = 4;
	  private byte shape = RECTANGULAR_SHAPE;
	  private int angle;
	  private Vec2 center;
	  private ArrayList<Point> points;
	  private boolean editingNow = true;
	  private boolean fill;
	  private int width;
	  private final static byte STROKE_WEIGHT = (byte)(Program.engine.width/72f);
	  private Timer nextSideTimer;
	  private final static int TIME_TO_NEXT_LINE_SHOW = 250;
	  private final static int UPPER = 0;
	  private final static int RIGHT = 1;
	  private final static int LOWER = 2;
	private final static int FIRST_SIDE = 0;
	private final static int SECOND_SIDE = 1;

	  private int actualSideToShow = UPPER;
	  private final boolean WITH_SINGLE_LINES = true;

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		private int height;
	  // Fill data
		final private static boolean FILL_ALONG_X = true;
		final private static boolean FILL_ALONG_Y = false;
		private boolean fillAlong;
		private int repeatingElementsNumber = 1;
		private int singleElementWidth, singleElementHeight;
		private boolean fillDataWasCalculated = false;
	    private static boolean ON_OBJECT_FRAME = true;

		public Figure(MovablePlatform platform){
			this.center = new Vec2(platform.getPixelPosition().x, platform.getPixelPosition().y);
			points = new ArrayList<Point>();
			points.add(new Point(new PVector(center.x-platform.getWidth()/2, center.y-platform.getHeight()/2)));
			points.add(new Point(new PVector(center.x+platform.getWidth()/2, center.y-platform.getHeight()/2)));
			points.add(new Point(new PVector(center.x-platform.getWidth()/2, center.y+platform.getHeight()/2)));
			points.add(new Point(new PVector(center.x+platform.getWidth()/2, center.y+platform.getHeight()/2)));
			this.shape = RECTANGULAR_SHAPE;
			init();
		}

	  public Figure(ArrayList<Point> newPoints, Vec2 center, byte shape){
	  	points = new ArrayList<Point>();
	  	this.center = center;
	    this.shape = shape;
	    for (int i = 0; i < newPoints.size(); i++){
	      points.add(newPoints.get(i));
	      points.get(i).statement = Point.SELECTED;
	    }
	    init();
	  }



	  private void init(){
		  if (shape == RECTANGULAR_SHAPE){
			  width = (int)PApplet.abs(points.get(1).pos.x-points.get(0).pos.x);
			  height = (int)PApplet.abs(points.get(1).pos.y-points.get(0).pos.y);
			  shapeGraphic = Program.engine.createShape(PConstants.RECT, 0, 0, width, height);
			  shapeGraphic.setStroke(Program.engine.color(Point.SELECTED_COLOR));
			  shapeGraphic.setStrokeWeight(Program.engine.ceil(2*Point.SELECTED_STROKE_WEIGHT/3));
		  }
		  else if (shape == TRIANGLE_SHAPE){
			  shapeGraphic = Program.engine.createShape();
			  shapeGraphic.beginShape();
			  shapeGraphic.vertex(0,0);
			  shapeGraphic.vertex(points.get(1).pos.x-points.get(0).pos.x, points.get(1).pos.y-points.get(0).pos.y);
			  shapeGraphic.vertex(points.get(2).pos.x-points.get(0).pos.x, points.get(2).pos.y-points.get(0).pos.y);
			  shapeGraphic.strokeWeight(Program.engine.ceil(2*Point.SELECTED_STROKE_WEIGHT/3));
			  shapeGraphic.stroke(Point.SELECTED_COLOR);
			  shapeGraphic.endShape(PConstants.CLOSE);
			  width = GameMechanics.getTriangleWidthByPoints(points.get(0), points.get(1), points.get(2));
			  height = GameMechanics.getTriangleHeightByPoints(points.get(0), points.get(1), points.get(2));
		  }
		  else if (shape == CIRCLE_SHAPE){
			  width = (int)PApplet.abs((points.get(1).pos.x-points.get(0).pos.x)*2);
			  height = width;
			  shapeGraphic = Program.engine.createShape(PConstants.ELLIPSE, 0, 0, width, height);
			  shapeGraphic.setStroke(Program.engine.color(Point.SELECTED_COLOR));
			  shapeGraphic.setStrokeWeight(Program.engine.ceil(2*Point.SELECTED_STROKE_WEIGHT/3));
		  }
		  else System.out.println("there are no data about this figures");
		  nextSideTimer = new Timer(TIME_TO_NEXT_LINE_SHOW);
	  }

	  public int getRadius(){
			if (shape != CIRCLE_SHAPE) System.out.println("It is not a circle!");
			return width;
	  }

	  public void setAngle(int newAngleInDegrees){
	  	angle = newAngleInDegrees;
	  }

	  public int getAngle(){
	  	return angle;
	  }

	  public boolean isEditingNow(){
	  	return editingNow;
	  }
	  
	  public ArrayList<PVector> getCoordinates(){    
	    ArrayList<PVector> coordinates = new ArrayList<PVector>();    
	    for (int i = 0 ; i < points.size(); i++){
			Vec2 newPos = points.get(i).getPosition();
			PVector newPosPVector = new PVector(newPos.x, newPos.y);
	      coordinates.add(newPosPVector);
	    }    
	    return coordinates;
	  }

	  public Vec2 getCenter(){
	  	return center;
	  }
	  

	  public byte getShapeType(){
	    return shape;
	  }

	private void drawSprite(GameCamera gameCamera, PGraphics mapGraphic){
		mapGraphic.imageMode(PConstants.CENTER);
		if (!fill) {
			//System.out.println("Sprite is drawn " + width + "; " + height);
			mapGraphic.image(sprite.image, 0, 0, width, height, vertexPositions[0], vertexPositions[1], vertexPositions[2], vertexPositions[3]);
		}
		else{
			if (fillAlong == FILL_ALONG_X) {
				for (int i = 0; i < repeatingElementsNumber; i++) {
					mapGraphic.image(sprite.image, -((repeatingElementsNumber - 1) * singleElementWidth / 2) + singleElementWidth * i, 0, singleElementWidth, singleElementHeight, vertexPositions[0], vertexPositions[1], vertexPositions[2], vertexPositions[3]);
				}
			} else {
				for (int i = 0; i < repeatingElementsNumber; i++) {
					mapGraphic.image(sprite.image, 0, -((repeatingElementsNumber - 1) * singleElementHeight / 2) + singleElementHeight * i, singleElementWidth, singleElementHeight, vertexPositions[0], vertexPositions[1], vertexPositions[2], vertexPositions[3]);
				}
			}
		}
	}

	private void drawAnimation( PGraphics mapGraphic){
		animation.update();
		mapGraphic.imageMode(PConstants.CENTER);
		mapGraphic.image(animation.getImage(), 0, 0, width, height, animation.getActualLeftX(), animation.getActualUpperY(), animation.getActualRightX(), animation.getActualLowerY());
	}
/*
	private void drawOnSeparateFrame(GameCamera gameCamera, MapZone mapZone, PVector nullPosition){
		mapZone.mapGraphic.pushMatrix();
		mapZone.mapGraphic.pushStyle();
		mapZone.mapGraphic.scale(Program.OBJECT_FRAME_SCALE);
		if (shape == RECTANGULAR_SHAPE){
			mapZone.mapGraphic.translate(center.x - gameCamera.getActualPosition().x+nullPosition.x , center.y - gameCamera.getActualPosition().y+nullPosition.y);
			mapZone.mapGraphic.rotate(PApplet.radians(angle));
			mapZone.mapGraphic.rectMode(PApplet.CENTER);
			int width = (int)PApplet.abs(points.get(1).pos.x-points.get(0).pos.x);
			int height = (int)PApplet.abs(points.get(1).pos.y-points.get(0).pos.y);
			if (sprite !=null){
				drawSprite(gameCamera, mapZone.mapGraphic);
			}
			if (animation != null){
				drawAnimation(mapZone.mapGraphic);
			}
			mapZone.mapGraphic.stroke(Program.engine.color(180,56,55,200));
			mapZone.mapGraphic.strokeWeight(STROKE_WEIGHT);
			mapZone.mapGraphic.noFill();
			if (!WITH_SINGLE_LINES) mapZone.mapGraphic.rect(0,0,width,height);
			else drawSingleLine(mapZone.mapGraphic, width, height);
		}
		else if (shape == CIRCLE_SHAPE){
			if (Program.OBJECT_FRAME_SCALE == 1){
				mapZone.mapGraphic.translate(center.x - gameCamera.getActualPosition().x+nullPosition.x + Program.objectsFrame.width / 2, center.y - gameCamera.getActualPosition().y+nullPosition.y + Program.objectsFrame.height / 2);
			}
			else mapZone.mapGraphic.translate(center.x - gameCamera.getActualXPositionRelativeToCenter(), center.y - gameCamera.getActualYPositionRelativeToCenter());
			mapZone.mapGraphic.rotate(PApplet.radians(angle));
			if (sprite !=null){
				drawSprite(gameCamera, mapZone.mapGraphic);
			}
			if (animation != null){
				drawAnimation(mapZone.mapGraphic);
			}
			mapZone.mapGraphic.stroke(Program.engine.color(180,56,55,200));
			mapZone.mapGraphic.strokeWeight(STROKE_WEIGHT);
			mapZone.mapGraphic.noFill();
			//mapZone.mapGraphic.ellipse(0,0,width,height);
			drawArc(mapZone.mapGraphic, width, height);
		}
		else if (shape == TRIANGLE_SHAPE){
			mapZone.mapGraphic.translate(gameCamera.getActualPosition().x, gameCamera.getActualPosition().y);
			mapZone.mapGraphic.scale(gameCamera.getScale());
			mapZone.mapGraphic.stroke(Program.engine.color(180,56,55,200));
			mapZone.mapGraphic.strokeWeight(STROKE_WEIGHT);
			mapZone.mapGraphic.translate(points.get(0).pos.x, points.get(0).pos.y);
			mapZone.mapGraphic.shape(shapeGraphic, 0,0);
		}
		mapZone.mapGraphic.popStyle();
		mapZone.mapGraphic.popMatrix();
	}
*/
	private void drawFigures(GameCamera gameCamera) {


	}

	  public void draw(GameCamera gameCamera, MapZone mapZone, PVector nullPosition) {
		  if (ON_OBJECT_FRAME) {
			  drawOnObjectFrame(gameCamera, Program.objectsFrame);
		  } else {
			  //drawOnMapZone(gameCamera, mapZone);
		  }
		  if (WITH_SINGLE_LINES) updateSideShowingTimer();
	  }


			/*

	  	if (nullPosition == null){
	  		nullPosition = new PVector(0,0);
		}
		if (!ON_OBJECT_FRAME) drawOnSeparateFrame(gameCamera, mapZone, nullPosition);
		else  drawOnObjectFrame(gameCamera, Program.objectsFrame, nullPosition);*/


	private void drawOnObjectFrame(GameCamera gameCamera, PGraphics graphic) {
		graphic.pushMatrix();
		graphic.pushStyle();
		graphic.scale(Program.OBJECT_FRAME_SCALE);
		if (shape == RECTANGULAR_SHAPE){
			if (Program.OBJECT_FRAME_SCALE == 1){
				graphic.translate(center.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, center.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
			}
			graphic.translate(center.x - gameCamera.getActualXPositionRelativeToCenter(), center.y - gameCamera.getActualYPositionRelativeToCenter());
			graphic.rotate(PApplet.radians(angle));
			graphic.rectMode(PApplet.CENTER);
			int width = (int)PApplet.abs(points.get(1).pos.x-points.get(0).pos.x);
			int height = (int)PApplet.abs(points.get(1).pos.y-points.get(0).pos.y);
			if (sprite !=null){
				drawSprite(gameCamera, graphic);
			}
			if (animation != null){
				drawAnimation(graphic);
			}
			graphic.stroke(Program.engine.color(180,56,55,200));
			graphic.strokeWeight(STROKE_WEIGHT);
			graphic.noFill();
			if (!WITH_SINGLE_LINES) graphic.rect(0,0,width,height);
			else drawSingleLineForRect(graphic, width, height);
		}
		else if (shape == CIRCLE_SHAPE){
			if (Program.OBJECT_FRAME_SCALE == 1){
				graphic.translate(center.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, center.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
			}
			else graphic.translate(center.x - gameCamera.getActualXPositionRelativeToCenter(), center.y - gameCamera.getActualYPositionRelativeToCenter());
			graphic.rotate(PApplet.radians(angle));
			if (sprite !=null){
				drawSprite(gameCamera, graphic);
			}
			if (animation != null){
				drawAnimation(graphic);
			}
			graphic.stroke(Program.engine.color(180,56,55,200));
			graphic.strokeWeight(STROKE_WEIGHT);
			graphic.noFill();
			drawArc(graphic, width, height);
		}
		else if (shape == TRIANGLE_SHAPE){
			if (Program.OBJECT_FRAME_SCALE == 1){
				graphic.translate(center.x - gameCamera.getActualPosition().x + Program.objectsFrame.width / 2, center.y - gameCamera.getActualPosition().y + Program.objectsFrame.height / 2);
			}
			else graphic.translate(center.x - gameCamera.getActualXPositionRelativeToCenter(), center.y - gameCamera.getActualYPositionRelativeToCenter());
			graphic.rotate(PApplet.radians(angle));
			if (sprite !=null){
				drawSprite(gameCamera, graphic);
			}
			if (animation != null){
				drawAnimation(graphic);
			}
			graphic.stroke(Program.engine.color(180,56,55,200));
			graphic.strokeWeight(STROKE_WEIGHT);
			graphic.noFill();
			drawSingleLineForTriangle(graphic, width, height);

			/*
			graphic.translate(gameCamera.getActualPosition().x, gameCamera.getActualPosition().y);
			graphic.scale(gameCamera.getScale());
			graphic.stroke(Program.engine.color(180,56,55,200));
			graphic.strokeWeight(STROKE_WEIGHT);
			graphic.translate(points.get(0).pos.x, points.get(0).pos.y);
			graphic.shape(shapeGraphic, 0,0);
			*/
		}
		graphic.popStyle();
		graphic.popMatrix();
	}

	private void drawArc(PGraphics graphics, int width, int height) {
		//graphics.arc(0, 0, width, height, 3*PApplet.QUARTER_PI, 5*PApplet.QUARTER_PI);

		if (actualSideToShow == UPPER) {
			graphics.arc(0, 0, width, height, 5f*PApplet.QUARTER_PI, 7f*PApplet.QUARTER_PI);
		}
		else if (actualSideToShow == RIGHT) {
			graphics.arc(0, 0, width, height, 7f*PApplet.QUARTER_PI, 9f*PApplet.QUARTER_PI);
		}
		else if (actualSideToShow == LOWER) {
			graphics.arc(0, 0, width, height, 1f*PApplet.QUARTER_PI, 3f*PApplet.QUARTER_PI);
		}
		else {//Right
			graphics.arc(0, 0, width, height, 3f*PApplet.QUARTER_PI, 5f*PApplet.QUARTER_PI);
		}

	}


	private void drawSingleLineForRect(PGraphics graphics, int width, int height) {
			if (actualSideToShow == UPPER) {
				graphics.line(-width/2, -height/2, width/2, -height/2);
				graphics.line(width/2, -height/2, width/2, height/2);
			}
			else if (actualSideToShow == RIGHT) {
				graphics.line(width/2, -height/2, width/2, height/2);
				graphics.line(-width/2, height/2, width/2, height/2);
			}
			else if (actualSideToShow == LOWER) {
				graphics.line(-width/2, height/2, width/2, height/2);
				graphics.line(-width/2, -height/2, -width/2, height/2);
			}
			else {
				graphics.line(-width/2, -height/2, -width/2, height/2);
				graphics.line(-width/2, -height/2, width/2, -height/2);
			}
	}

	private void drawSingleLineForTriangle(PGraphics graphics, int width, int height) {
		//center.x
		graphics.translate(-center.x, -center.y);
		if (actualSideToShow == FIRST_SIDE) {
			graphics.line(points.get(0).pos.x, points.get(0).pos.y, points.get(1).pos.x, points.get(1).pos.y);
		}
		else if (actualSideToShow == SECOND_SIDE) {
			graphics.line(points.get(1).pos.x, points.get(1).pos.y, points.get(2).pos.x, points.get(2).pos.y);
		}
		else {
			graphics.line(points.get(2).pos.x, points.get(2).pos.y, points.get(0).pos.x, points.get(0).pos.y);
		}
	}


	private void updateSideShowingTimer() {
		if (nextSideTimer.isTime()){
			nextSideTimer.setNewTimer(TIME_TO_NEXT_LINE_SHOW);
			actualSideToShow++;
			if (shape == TRIANGLE_SHAPE) {
				if (actualSideToShow>2) actualSideToShow = 0;
			}
			else if (actualSideToShow>3) actualSideToShow = 0;
		}
	}

	/*
	private void setTexture(SingleImageDataForFigures data, boolean type) {
			if (type == TilesetZone.SPRITE) {
				this.texture = data.getImage();
			}
			else {
				SingleSpriteAnimationDataForFigures animationData = (SingleSpriteAnimationDataForFigures)data;
				this.animation = data.getImage();
			}
		this.vertexPositions = data.getVertexes();
		calculateFillData();
	}
	 */

	private void setGraphic(GraphicDataForFigures data, boolean type) {
		if (type == TilesetZone.SPRITE) {
				System.out.println("Try to save as sprite ");
				SingleImageDataForFigures imageData = null;
				imageData = (SingleImageDataForFigures)data;
				this.sprite = imageData.getImage();

		}
		else {
			System.out.println("Try to save as animation");
			SingleSpriteAnimationDataForFigures imageData = (SingleSpriteAnimationDataForFigures) data;
			this.animation = imageData.getAnimation();

			System.out.println("Succesfully saved animation data");
		}
		this.vertexPositions = data.getVertexes();
		calculateFillData();
	}

	public void setGraphicData(GraphicDataForFigures data) {
		if (data.getClass() == SingleImageDataForFigures.class){
			//SingleImageDataForFigures imageData = (SingleImageDataForFigures)data;
			setGraphic(data, TilesetZone.SPRITE);
		}
		else if (data.getClass() == SingleSpriteAnimationDataForFigures.class){
			SingleSpriteAnimationDataForFigures animationData = (SingleSpriteAnimationDataForFigures)data;
			//setAnimation(animationData, TilesetZone.ANIMATION);
			setGraphic(animationData, TilesetZone.ANIMATION);
		}
	}
/*
	public void setAnimation(SingleSpriteAnimationDataForFigures data) {
		this.animation = data.getAnimation();
		this.vertexPositions = data.getVertexes();
	}

 */

	public void setFillForSprite(boolean fill) {
	  	this.fill = fill;
		calculateFillData();
	}

	public void calculateFillData(){
		if (fill && vertexPositions != null){
			float xScaleSpriteFactor = (float) Program.engine.ceil(width / PApplet.abs((float) (vertexPositions[2] - vertexPositions[0])));
			float yScaleSpriteFactor = (float) Program.engine.ceil(height / PApplet.abs((float) (vertexPositions[3] - vertexPositions[1])));
			if (xScaleSpriteFactor > yScaleSpriteFactor) {
				fillAlong = FILL_ALONG_X;
				singleElementHeight = this.height;
				float theoreticalWidthOfSingleElement = (float) Program.engine.ceil(width * yScaleSpriteFactor / (xScaleSpriteFactor));
				repeatingElementsNumber = PApplet.round((float) width / theoreticalWidthOfSingleElement);
				if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
				singleElementWidth = (int) Program.engine.ceil((width / repeatingElementsNumber));
			} else {
				fillAlong = FILL_ALONG_Y;
				singleElementWidth = this.width;
				float theoreticalHeightOfSingleElement = (float) Program.engine.ceil(height * xScaleSpriteFactor / yScaleSpriteFactor);
				repeatingElementsNumber = PApplet.round((float) height / theoreticalHeightOfSingleElement);
				if (repeatingElementsNumber <= 0) repeatingElementsNumber = 1;
				singleElementHeight = (int) Program.engine.ceil((height / repeatingElementsNumber));
			}
		}
	}

	@Override
	protected void finalize(){
		if (Program.DELETE_IMAGES_FROM_CACHE) {
			try {
				if (sprite != null) {
					Program.objectsFrame.removeCache(sprite.image);
				} else if (animation != null) {
					Program.objectsFrame.removeCache(animation.image);
				}
				if (sprite != null) sprite.image = null;
				if (animation != null) animation.image = null;
				if (shapeGraphic != null) shapeGraphic = null;
				System.out.println("Figure graphic was removed from the memory");

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public Animation getAnimation() {
		return animation;
	}
}
