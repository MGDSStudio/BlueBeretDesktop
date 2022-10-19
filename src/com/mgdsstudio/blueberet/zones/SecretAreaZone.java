package com.mgdsstudio.blueberet.zones;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.textes.OnDisplayText;
import com.mgdsstudio.blueberet.mainpackage.Program;

public class SecretAreaZone extends SingleFlagZone{
    public static String CLASS_NAME = "SecretAreaZone";
    private String textToBeDrawn = "Secret founded!";
    private boolean activated;
    private final int red, green, blue;

   ///
    // private boolean wasFounded;

    public SecretAreaZone(Flag flag, int red, int green, int blue) {
        this.flag = flag;
        this.red = red;
        this.green = green;
        this.blue = blue;
        initLanguageSpecific();
    }

    private void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN){
            textToBeDrawn = "Найден секрет!";
        }
        else textToBeDrawn = "Secret found!";
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    private OnDisplayText createText(){
        OnDisplayText onDisplayText = new OnDisplayText(0,0,red,green,blue, textToBeDrawn, 3000);
        return onDisplayText;
    }

    public void update(GameRound gameRound){
        if (!activated){
            if (flag.inZone(gameRound.getPlayer().getPixelPosition())){
                if (gameRound.getPlayer().isAlive()) {
                    Soldier soldier = (Soldier) gameRound.getPlayer();
                    if (!soldier.isControlBlocked()) {
                        activated = true;
                        //canBeDeleted = true;
                        //wasFounded = true;
                        gameRound.addOnDisplayText(createText());
                        writeDataToFile();
                    }
                }
            }
        }
    }

    private void writeDataToFile() {

    }

    public boolean wasFounded(){
        if (activated) return  true;
        else return false;
    }

}
