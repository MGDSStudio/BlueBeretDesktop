package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class HUD_LifeLinesController {
    ArrayList<HUD_LifeLine> hud_lifeLines;

    private static Image imageFile;
    public final ImageZoneSimpleData lifeLineClearZoneData;
    public final ImageZoneSimpleData lifeLineRedFillingZoneData;
   // public final ImageZoneSimpleData lifeLineFilled;

    //private static StaticSprite lifeLine;

    private static StaticSprite frame;
    private static StaticSprite lifeLine;

    final static private int FRAME_WIDTH = 50;
    final static private int FRAME_HEIGHT = 10;
    static int FULL_LIFE_LINE_WIDTH;
    static private int FULL_LIFE_LINE_HEIGHT;
    //private int actualLifeLineWidth;
    static Vec2 lifeLineLeftUpperCorner;
    private boolean graphicLoaded;

    public HUD_LifeLinesController(GameRound gameRound){
        //final String path = Programm.getRelativePathToAssetsFolder()+"SleekBars.png";
        //final String path = HeadsUpDisplay.;
        Tileset tileset;
        tileset = HeadsUpDisplay.mainGraphicTileset;
        imageFile = HeadsUpDisplay.mainGraphicSource;
        hud_lifeLines = new ArrayList<>(4);
        lifeLineClearZoneData = HUD_GraphicData.lifeLineClear;
        lifeLineRedFillingZoneData = HUD_GraphicData.lifeLineFilled;
        //hud_lifeLines = new ArrayList<>(gameRound.getPersons().size());
        byte boardThickness = 5;
        float imageWidth = 128;
        float imageHeight = 32;
        FULL_LIFE_LINE_WIDTH = (int)(FRAME_WIDTH*(imageWidth-2*boardThickness)/imageWidth);
        FULL_LIFE_LINE_HEIGHT = (int)(FRAME_HEIGHT*(imageHeight-2*boardThickness)/imageHeight);
        for (Person person : gameRound.getPersons()){
            if (person.getClass() != Soldier.class) hud_lifeLines.add(new HUD_LifeLine(person, FULL_LIFE_LINE_WIDTH));
        }
        System.out.println("Width" + FULL_LIFE_LINE_WIDTH);
        frame = new StaticSprite(imageFile.getPath(), (int)lifeLineClearZoneData.leftX, (int)lifeLineClearZoneData.upperY, (int)(lifeLineClearZoneData.rightX),(int)lifeLineClearZoneData.lowerY, FRAME_WIDTH, FRAME_HEIGHT);
        lifeLine = new StaticSprite(imageFile.getPath(), (int)lifeLineRedFillingZoneData.leftX, (int)lifeLineRedFillingZoneData.upperY, (int)(lifeLineRedFillingZoneData.rightX),(int)lifeLineRedFillingZoneData.lowerY, FULL_LIFE_LINE_WIDTH, FULL_LIFE_LINE_HEIGHT);
        /*
        frame = new StaticSprite(imageFile.getPath(), (int)0, (int)0, (int)(imageWidth-1),(int)32, FRAME_WIDTH, FRAME_HEIGHT);
        lifeLine = new StaticSprite(imageFile.getPath(), (int)boardThickness, (int)37, (int)(imageWidth-1-boardThickness),(int)58, FULL_LIFE_LINE_WIDTH, FULL_LIFE_LINE_HEIGHT);


         */

        lifeLineLeftUpperCorner = new Vec2(-FULL_LIFE_LINE_WIDTH/2, -FULL_LIFE_LINE_HEIGHT/2);

        //objectsFrame.loadSprites(mainGraphicController.getTilesetUnderPath(bridge.getSprite().getPath()));
    }

    public void update(GameRound gameRound){
        if (!graphicLoaded ) loadGraphic();
        for (int i = (hud_lifeLines.size()-1); i>=0; i--){
            //System.out.println("Statement " + hud_lifeLines.get(i).getStatement());
            if (hud_lifeLines.get(i).canBeDeleted()){
                System.out.println("HUD was deleted for " + i);
                gameRound.getHud_lifeLinesController().hud_lifeLines.remove(hud_lifeLines.get(i));
            }
        }
        for (HUD_LifeLine hud_lifeLine : hud_lifeLines){
            hud_lifeLine.update();
        }
    }

    public ArrayList <HUD_LifeLine> getHud_lifeLines(){
        return hud_lifeLines;
    }

    public void addHudLifeLineForNewPersons(GameRound gameRound, Person person){
        gameRound.getHud_lifeLinesController().hud_lifeLines.add(new HUD_LifeLine(person, FULL_LIFE_LINE_WIDTH));
    }

    public void draw(GameCamera gameCamera){

        for (int i = 0; i < hud_lifeLines.size(); i++){
            hud_lifeLines.get(i).drawFrame(gameCamera, frame);
            hud_lifeLines.get(i).drawLine(gameCamera, lifeLine);

            /*
            hud_lifeLines.get(i).drawFrame(gameCamera, imageFile, lifeLineClearZoneData);
            hud_lifeLines.get(i).drawLine(gameCamera, imageFile, lifeLineClearZoneData);

             */
            /*
            hud_lifeLines.get(i).drawFrame(gameCamera, frame);
            hud_lifeLines.get(i).drawLine(gameCamera, lifeLine);
            */
        }
        /*
        for (HUD_LifeLine hud_lifeLine : hud_lifeLines){
            hud_lifeLine.drawFrame(gameCamera, frame);
            hud_lifeLine.drawLine(gameCamera, lifeLine);

        }*/
    }

    private void loadGraphic() {
        frame.loadSprite(HeadsUpDisplay.mainGraphicTileset);
        lifeLine.loadSprite(HeadsUpDisplay.mainGraphicTileset);
        graphicLoaded = true;

    }

    public void loadGraphic(MainGraphicController mainGraphicController) {
        frame.loadSprite(HeadsUpDisplay.mainGraphicTileset);
        lifeLine.loadSprite(HeadsUpDisplay.mainGraphicTileset);

        /*
        frame.loadSprite(mainGraphicController.getTilesetUnderPath(HUD_GraphicData.mainGraphicFile.path));
        lifeLine.loadSprite(mainGraphicController.getTilesetUnderPath(HUD_GraphicData.mainGraphicFile.path));*/
        //System.out.println("Tileset is null : " + (tileset == null));
    }
}
