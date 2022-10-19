package com.mgdsstudio.blueberet.graphic.simplegraphic;

import com.mgdsstudio.blueberet.mainpackage.Program;

import com.mgdsstudio.texturepacker.TextureDecodeManager;
import com.mgdsstudio.texturepacker.TexturePacker;
import processing.core.PImage;

public class Image extends SimpleGraphicUnit{

	
	public Image(String path) {
		this.path = path;
		if (isMgdsTexture()) {
			loadMgdsImage();
		}
		else image = Program.engine.loadImage(path);
	}



	public Image(String path, PImage anotherPImage) {
		this.path = path;
		image = anotherPImage;
		//image.
	}



	public Image(Image anotherImage) {
		this.image = anotherImage.image;
	}
	
	public void loadNewImage (String path) {
		this.path = path;
		if (isMgdsTexture()) {
			//System.out.println("This image " + path + " is a mgds");
			loadMgdsImage();
		}
		else image = Program.engine.loadImage(path);
		//image = Program.engine.loadImage(path);
	}


	private void loadMgdsImage(){
		TextureDecodeManager decodeManager = new TextureDecodeManager(path, Program.engine);
		image = decodeManager.getDecodedImage();
	}

	public void setNewPictureFromAnotherImage (Image image) {
		this.image = image.getImage();
	}
	
	public PImage getImage() {
		return image;
	}

	public String getPath() {
		return path;
	}

	private boolean isMgdsTexture(){
		if (path.contains(TexturePacker.MGDS_FILE_EXTENSION)){
			return true;
		}
		else return false;
	}
}
