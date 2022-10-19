package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class FPS_HUD {
    private boolean graphicRenderer;
	
	public FPS_HUD(){}

    public FPS_HUD(boolean graphicRenderer){
	    this.graphicRenderer = graphicRenderer;
    }
	
	public static void showFrameRate(){
        Program.engine.pushStyle();
        Program.engine.fill(0xff06C41A);
        Program.engine.textSize(Program.engine.height/50);
        final String FPS = "FPS: " + (byte) Program.engine.frameRate;
        Program.engine.text(FPS, 13* Program.engine.width/16, Program.engine.height-(12* Program.engine.height/13));
        Program.engine.popStyle();
    }


    public void showFrameRateWithRenderer(boolean graphicRenderer) {
        Program.engine.pushStyle();
        Program.engine.fill(Program.engine.color(212,15,15));
        Program.engine.textSize(Program.engine.height/50);
        String FPS = "FPS: " + (byte) Program.engine.frameRate;
        if (graphicRenderer == Program.OPENGL_RENDERER) FPS = FPS + " OPEN GL";
        else  FPS = FPS + " JAVA";
        try {
            Program.engine.text(FPS, 9* Program.engine.width/16, UpperPanel.HEIGHT+(11.5f* Program.engine.height/13));
        }
        catch ( Exception e){
            System.out.println("Can not draw frame rate text;");
            e.printStackTrace();
        }

        Program.engine.popStyle();
    }
}
