package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public class UpperPanelMessageController extends FrameController{
    //private EightPartsFrameImage portraitFrame;
    //private Tileset tileset;
    //private PFont textFont;
    //private int textHeight;
    private int normalTextColor;
    private ArrayList <String> messageList;
    private ArrayList <ImageZoneSimpleData> pictures;
    //private HashMap<PortraitPicture, ImageZoneSimpleData> portraits;
    //private PortraitPicture actualPortrait = PortraitPicture.PlAYER_FACE;
    public final static boolean SHOWN = true;
    private boolean statement = SHOWN;
    private SMSController smsController;

    UpperPanelMessageController (UpperPanel upperPanel, Tileset tileset, EightPartsFrameImage portraitFrame, EightPartsFrameImage messageFrame, PFont textFont){
        this.mainFrame = portraitFrame;
        this.secondaryFrame = messageFrame;
        //portraits = new HashMap<>(3);
        //initGraphic(tileset);
        createFont();
        smsController = new SMSController(upperPanel, messageFrame, portraitFrame, null, textFont);
    }

    private void createFont() {

        normalTextColor = Program.engine.color(255);
    }


    @Override
    public void draw(PGraphics graphics, int graphicMethod) {
        if (statement == SHOWN && smsController.areThereMessages()) {
            mainFrame.draw(graphics);
            secondaryFrame.draw(graphics);
            smsController.writeText(graphics);
        }
        //graphics.image(tileset.getPicture().getImage(), );
    }

    @Override
    public void clickOnFrame(GameRound gameRound) {
        if (statement == SHOWN){
            if (isClickOnTextField()){
                setStatement(!SHOWN);

            }
        }
    }

    private boolean isClickOnTextField() {
        return true;
    }

    @Override
    public Vec2 getLeftUpperCornerForFullOpened() {
        return null;
    }

    @Override
    public boolean isFrameClicked() {
        return smsController.isMessageFrameClicked();
    }

    public boolean getStatement() {
        return statement;
    }

    public void setStatement(boolean statement) {
        this.statement = statement;
    }

    public void addMessage(SMS message, GameRound gameRound){
        System.out.println("Message was added");
        smsController.addMessage(message, gameRound);

        //messageList.add(message.getText());
    }

    public boolean areThereShownMessages(){
        return smsController.areThereShownMessages();
        //return !smsController.areThereShownMessages();
    }

    public boolean closeMessageFrameIfPossible(){
        boolean ended = smsController.closeMessageFrameIfPossible();
        return ended;
    }

    public boolean isShown(){
        return getStatement();
    }
}
