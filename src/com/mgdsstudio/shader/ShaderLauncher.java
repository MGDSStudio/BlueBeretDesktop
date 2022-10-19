package com.mgdsstudio.shader;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import com.mgdsstudio.texturepacker.TextureEncryptManager;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PShader;



public class ShaderLauncher extends PApplet{
    private PImage img;
    private PShader edges;


    public static void main(String [] passedArgs){
        String[] appletArgs = new String[]{"com.mgdsstudio.shader.ShaderLauncher"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    @Override
    public void settings(){
        size(480, 320, P2D);
    }

    @Override
    public void setup(){
        img = loadImage("Soldier.png");
        edges = loadShader("MainPS.glsl", "MainVS.glsl");
        //edges = loadShader("MainPS.glsl");
        //edges.
    }


    @Override
    public void draw(){
        //shader(edges);
        filter(edges);
        image(img, 0, 0);

    }



}
