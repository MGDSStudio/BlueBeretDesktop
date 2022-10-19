package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PFont;

public abstract class AbstractText {
    protected float actualX, actualY;
    protected String text;
    protected int color;
    public final static int BLACK = Program.engine.color(0);
    //public final static int WHITE = Program.engine.color(250);
    protected int fontHeight;
    public final static int WHITE = UpperPanel.ACTIVE_COLOR;
    public final static int GRAY = 180;
    protected Timer timer;
    protected static PFont font;

    protected static boolean fontUploaded;

    protected void createFont() {
        if (!fontUploaded) {
            if (HeadsUpDisplay.graphic != null && HeadsUpDisplay.graphic.textFont != null) {
                font = HeadsUpDisplay.graphic.textFont;
                fontUploaded = true;
                System.out.println("Font copied from panel font");
            }
            else {
                if (Program.OS == Program.ANDROID) font = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.secondaryFont));
                else font = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.mainFont));
                fontUploaded = true;
                System.out.println("Font created new from panel font");
            }
        }
    }

    public abstract void update();

    public abstract void draw(GameCamera gameCamera);

    public abstract boolean canBeDeleted();

    public void setColor(int color) {
        this.color = color;
    }
}
