package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.SpriteAnimation;
import com.mgdsstudio.blueberet.androidspecific.MainActivity;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.sound.SoundFile;

public class ExitMenu extends AbstractMenu{
    private final Timer timer;
    private final int timeToShowNoiseScreen = 1200;
    private final SpriteAnimation noiseAnimation;
    private SoundFile soundFile;


    public ExitMenu(PApplet engine, PGraphics graphics) {
        init(engine, graphics);
        noiseAnimation = new SpriteAnimation(Program.getAbsolutePathToAssetsFolder("Noise animation.gif"), 0,0,getGraphicWidth(engine, 1113/3),1113,graphics.width, graphics.height,(byte)3,(byte)1,33);
        noiseAnimation.loadAnimation();
        final boolean saveInRAM = false;
        soundFile = new SoundFile(engine, Program.getAbsolutePathToAssetsFolder("TV noise.wav"), saveInRAM);
        timer = new Timer(timeToShowNoiseScreen);
        soundFile.play();
        soundFile.amp(0.2f);
    }
    
    private int getGraphicWidth(PApplet engine, int pixelsAlongY){
        float  width = engine.width;
        float height = engine.height;
        float coef = height/width;
        float sourceWidth = pixelsAlongY/coef;
        System.out.println("Source width: " + sourceWidth);
        return (int) sourceWidth;
    }

    @Override
    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        if (timer.isTime()){
            soundFile.stop();
            //soundFile = null;
            System.out.println("Next function crush the program but it is good. The activity will be recreated");
            if (Program.OS == Program.ANDROID) {
                Program.iEngine.exitWithoutActivitySaving();
                //MainActivity mainActivity = (MainActivity) gameMenusController.getEngine().getActivity();
                //mainActivity.exitWithoutActivitySaving();
                //gameMenusController.getEngine().getActivity().finish();

            }
            else {
                gameMenusController.getEngine().exit();
            }
            //gameMenusController.getEngine().die("Kill");
            //gameMenusController.getEngine().getActivity().finishActivity(0);
            gameMenusController.getEngine().exit();
        }
        noiseAnimation.update();
    }

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        try {
            //System.out.println("Width: " + noiseAnimation.getWidth() + "; Screen: " + Program.engine.width + "; Graphic: " + graphic.width);
            noiseAnimation.draw(graphics, graphic.width / 2, graphic.height / 2, 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
