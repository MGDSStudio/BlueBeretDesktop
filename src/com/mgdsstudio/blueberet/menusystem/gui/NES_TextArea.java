package com.mgdsstudio.blueberet.menusystem.gui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class NES_TextArea extends NES_GuiElement{
    //public int
    public boolean withAppearing;
    private float yRelativeStep;
    private float yStep, yNewStringStep;
    private ArrayList <ArrayList <String> > textToBeShown;
    private ArrayList <ArrayList <String> > textCanNotBeShown;

    private TextAreaArray textToBeShown1;
    /*private String [][] textToBeShown;
    private String [][]  textCanNotBeShown;*/



    public NES_TextArea(int centerX, int centerY, int width, int height, String name, float yRelativeStep, ArrayList <String> text, PGraphics graphics) {
        super(centerX, centerY, width, height, name, graphics);
        actualStatement = ACTIVE;
        prevStatement = ACTIVE;
        initTextStep(yRelativeStep, graphics);
        devideTextToSingleStrings(graphics, text);
    }

    private void initTextStep(float yRelativeStep, PGraphics graphics) {
        if (yRelativeStep <= 0){
            ///Must be adopted to height
            yStep = yRelativeStep*graphics.textSize;
            yNewStringStep = yStep*1.75f;
        }
        else {
            yStep = yRelativeStep*graphics.textSize;
            yNewStringStep = yStep*1.75f;
        }
        System.out.println("Step: " + yStep + " / " + yNewStringStep);
    }

    private void devideTextToSingleStrings(PGraphics graphics, ArrayList <String> text){
        float textHeight = graphics.textSize;
        float symbolWidth = graphics.textWidth(" ");
        int symbolsAlongX = PApplet.floor(width/symbolWidth);
        textToBeShown = new ArrayList<>();
        textCanNotBeShown = new ArrayList<>();
        for (String sourceString : text){
            if (sourceString.length() > symbolsAlongX){

            }
            else textToBeShown.add(new ArrayList<>());
        }
        System.out.println("Symbols along x: " + symbolsAlongX);
    }

    @Override
    public void draw(PGraphics graphic) {
        super.draw(graphic);
        drawName(graphic, PConstants.CENTER);
    }

    @Override

    public void update(int mouseX, int mouseY){
        //super.update(mouseX, mouseY);
        //Nothing to update
    }


    @Override
    protected void updateFunction() {
        // Npthing to update
    }
}
