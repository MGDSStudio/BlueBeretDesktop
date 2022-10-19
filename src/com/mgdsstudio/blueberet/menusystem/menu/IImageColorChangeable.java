package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.graphic.BeretColorChanger;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.BeretColorLoadingMaster;
import com.mgdsstudio.blueberet.menusystem.load.beretcolorchanger.TwiceColor;
import processing.core.PApplet;

public interface IImageColorChangeable {

    default void updateBeretColorForGraphic(PApplet pApplet, TwiceColor oldColor, TwiceColor newColor, Image image){
        BeretColorChanger beretColorChanger = new BeretColorChanger();
        if (oldColor == null){
            oldColor = new TwiceColor();
        }
        if (newColor == null){
            BeretColorLoadingMaster beretColorLoadingMaster = new BeretColorLoadingMaster(pApplet);
            beretColorLoadingMaster.loadData();
            newColor = beretColorLoadingMaster.getBeretColor();

        }
        System.out.println("Try to change the bright color: ");
        beretColorChanger.changeColor(oldColor.getBrightColor(), newColor.getBrightColor(), image);
        System.out.println("Try to change the dark color: ");
        beretColorChanger.changeColor(oldColor.getDarkColor(), newColor.getDarkColor(), image);
    }
}
