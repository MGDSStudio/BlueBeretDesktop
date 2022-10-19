package com.mgdsstudio.blueberet.menusystem.menu.shop;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.EightPartsFrameImage;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.gui.ColorWithName;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextLabel;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActionPanel extends AbstractFrameWithText{
    private ArrayList <NES_GuiElement> guiElements;

    private final int TIME_BEFORE_FIRST_CLICK = 350;
    private boolean buttonsMustBeAligned = false;

    private final PGraphics graphics;

    private float gapToFrame;

    private WeaponType embeddedWeapon;
    private int embeddedObjectNumber;

    public ActionPanel(Vec2 leftUpperCorner, int width, int height, PGraphics graphics) {
        this.leftUpperCorner = leftUpperCorner;
        this.graphics = graphics;
        guiElements = new ArrayList<>();
        init(width, height, graphics);
    }

    private void init(int width, int height, PGraphics graphics){

        ImageZoneSimpleData frameData = HUD_GraphicData.basicRectForDialogFrames;
        int basicSourceHeightForDialogFrames = (int) (UpperPanel.HEIGHT*0.7f);
        gapToFrame = leftUpperCorner.x;
        Image image = HUD_GraphicData.mainGraphicFile;
        frame = new EightPartsFrameImage(image, frameData,basicSourceHeightForDialogFrames,basicSourceHeightForDialogFrames, width, height, leftUpperCorner);
        int textAreaHeight = (int) (frame.getHeight()/2-gapToFrame*2);
        Vec2 center = new Vec2(leftUpperCorner.x+width/2, leftUpperCorner.y+gapToFrame+textAreaHeight/2);
        int textAreaWidth = (int) (width-gapToFrame*2);
        //Rectangular rectangular = new Rectangular(center, textAreaWidth, textAreaHeight);
        //int stringsAlongY = 8;
        //int timeToShowMessage = 2000;
        //PFont font = graphics.textFont;
        //textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController(text, rectangular, stringsAlongY, timeToShowMessage,font, graphics);
    }



    public void update(PApplet engine){
        if (visible) {
            if (buttonsMustBeAligned){
                alignButtonsAlongY(graphics);
                buttonsMustBeAligned = false;
            }
            for (NES_GuiElement nes_guiElement: guiElements){
                nes_guiElement.update(engine.mouseX, engine.mouseY);
            }

            //System.out.println("Updating");
            //textInSimpleFrameDrawingController.update();

            //updateStatement(engine);
        }
    }

    private void updateStatement(PApplet engine) {
        if (timerBeforeFirstClick == null) timerBeforeFirstClick = new Timer(TIME_BEFORE_FIRST_CLICK);
        else if (timerBeforeFirstClick.isTime()){
            if (engine.mousePressed){
                if (GameMechanics.isPointInRect(engine.mouseX, engine.mouseY, textInSimpleFrameDrawingController.getTextArea())){
                    if (statement != PRESSED ){
                        statement = PRESSED;
                    }
                }
                else statement = ACTIVE;
            }
            else {
                if (GameMechanics.isPointInRect(engine.mouseX, engine.mouseY, textInSimpleFrameDrawingController.getTextArea())){
                    if (statement == PRESSED) {
                        statement = RELEASED;
                        System.out.println("Released 1");
                        if (!textInSimpleFrameDrawingController.isFullShown())
                            textInSimpleFrameDrawingController.setFullShow();
                    }
                }
                else {
                    if (statement == PRESSED) {
                        statement = RELEASED;
                        System.out.println("Released 2");
                        if (!textInSimpleFrameDrawingController.isFullShown()) textInSimpleFrameDrawingController.setFullShow();
                    }
                    else statement = ACTIVE;
                }
            }
        }
    }


    @Override
    public void draw(PGraphics graphics){
        if (visible) {
            super.draw(graphics);
            //if (!buttonsMustBeAligned) {
                for (NES_GuiElement nes_guiElement : guiElements) {
                    nes_guiElement.draw(graphics);
                }

            //}

        }
    }

    public void createButtons(String[] names, int money) {
        if (guiElements.size() > 0 ) guiElements.clear();
        int buttons = names.length;
        int gap = frame.getHeight()/(buttons+2);
        int buttonHeight = gap/2;
        float startY = frame.getLeftUpperCorner().y+gap;
        for (int i =0 ; i < names.length; i++){
            NES_ButtonWithCursor button = new NES_ButtonWithCursor((int) (frame.getLeftUpperCorner().x+frame.getWidth()/2), (int) (startY+i*gap), frame.getWidth()/2, buttonHeight, names[i], graphics);
            guiElements.add(button);
        }
        NES_GuiElement label = new NES_TextLabel((int)(frame.getLeftUpperCorner().x+frame.getWidth()/2), (int) (startY+ names.length*gap), (int) (frame.getWidth()/1.5f), buttonHeight, getMoneyString(money), graphics);
        guiElements.add(label);
        //System.out.println("Button height: " + gap);
        buttonsMustBeAligned = true;
        for (NES_GuiElement guiElement : guiElements){
            guiElement.setVisible(false);
        }
    }

    private String getMoneyString(int value) {
        String actual = "-BALANCE : ";
        actual+=value;
        actual+= " USD-";
        return actual;
    }

    protected void alignButtonsAlongY(PGraphics graphics){
        ArrayList<NES_GuiElement> elements = guiElements;
        System.out.println("There are " + elements.size() + " buttons");
        int longestString = 0;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                int actualWidth = elements.get(i).getTextWidth();
                if (actualWidth > longestString) {
                    longestString = actualWidth;
                }
            }
        }
        System.out.println("Longest string is: " + longestString);
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                elements.get(i).setWidth(longestString);
            }
        }
        int leftX = graphics.width/2-longestString/2;
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getClass() == NES_ButtonWithCursor.class) {
                NES_ButtonWithCursor button = (NES_ButtonWithCursor) elements.get(i);
                button.setLeftX(leftX);
            }
        }
        for (NES_GuiElement guiElement : guiElements){
            guiElement.setVisible(true);
        }
    }

    public NES_GuiElement getReleasedButton(){
        for (NES_GuiElement nes_guiElement : guiElements){
            if (nes_guiElement.getActualStatement() == NES_GuiElement.RELEASED){
                return nes_guiElement;
            }
        }
        return null;
    }

    public void setCanBeBought(String name, boolean canBeBought) {
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == name) {
                if (!canBeBought) {
                    guiElement.setActualStatement(NES_GuiElement.BLOCKED);
                }
                else guiElement.setActualStatement(NES_GuiElement.ACTIVE);
            }
            if (guiElement.getClass() == NES_TextLabel.class){
                if (canBeBought) {
                    guiElement.setColor(new ColorWithName(ColorWithName.WHITE));
                }
                else guiElement.setColor(new ColorWithName(ColorWithName.RED));
            }
        }
    }

    public void setNotNeedToBeBought(String blockedButtonText){
        for (int i = (guiElements.size()-1); i >= 0; i--){
            if (guiElements.get(i).getClass() == NES_TextLabel.class){
                String text = "YOU HAVE ALREADY THIS WEAPON";
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text =    "У ВАС УЖЕ ЕСТЬ ЭТО ОРУЖИЕ";
                }
                guiElements.get(i).setAnotherTextToBeDrawnAsName(text);
                System.out.println("Text was changed");
                guiElements.get(i).setColor(new ColorWithName(ColorWithName.YELLOW));
            }
            else if (guiElements.get(i).getName() == blockedButtonText){
                guiElements.get(i).setVisible( false);
                guiElements.get(i).setActualStatement(NES_GuiElement.HIDDEN);
                guiElements.get(i).setAnotherTextToBeDrawnAsName("   ");
                //System.out.println("Button " + guiElements.get(i).getName() + " is visible " + guiElements.get(i).isVisible() );
            }
        }
    }

    public void setAmmoIsFull(String blockedButtonText){
        for (int i = (guiElements.size()-1); i >= 0; i--){
            if (guiElements.get(i).getClass() == NES_TextLabel.class){
                String text = "AMMO BAG IS FULL";
                if (Program.LANGUAGE == Program.RUSSIAN){
                    text =    "ИНВЕНТАРЬ ПОЛОН";
                }
                guiElements.get(i).setAnotherTextToBeDrawnAsName(text);
                System.out.println("Text was changed");
                guiElements.get(i).setColor(new ColorWithName(ColorWithName.YELLOW));
            }
        }
    }

    public void hideButton(String name) {
        for (NES_GuiElement guiElement : guiElements){
            if (guiElement.getName() == name) {
                guiElement.setActualStatement(NES_GuiElement.HIDDEN);
                NES_ButtonWithCursor button = (NES_ButtonWithCursor) guiElement;
                button.setVisible(false);
                System.out.println("Button " + guiElement.getName() + " was hidden");
                break;
            }
            else System.out.println("This gui name " + guiElement.getName() + " is not " + name);
        }
    }

    public void setCanBeBought(HashMap <String, Integer> data, int priceForOne, int score, int actualCount, int maxAmmo) {
        for (int i =0; i < guiElements.size(); i++){
            for (Map.Entry singleData : data.entrySet()) {
                int value = (int) singleData.getValue();
                String testString = ""+value;
                boolean canBeBought = false;
                if (score>=value*priceForOne){
                    if (value+actualCount <= maxAmmo) {
                        canBeBought = true;
                    }
                    else {
                        System.out.println("You can not buy " + testString + " ammo. You have already " + actualCount + " and max is " + maxAmmo);
                        canBeBought = false;
                    }
                }
                if (guiElements.get(i).getName().contains(testString)){
                    if (!canBeBought){
                        guiElements.get(i).setActualStatement(NES_GuiElement.BLOCKED);
                    }
                    else guiElements.get(i).setActualStatement(NES_GuiElement.ACTIVE);
                }

            }
        }
    }

    public WeaponType getEmbeddedWeapon() {
        return embeddedWeapon;
    }

    public void setEmbeddedWeapon(WeaponType embeddedWeapon) {
        setEmbeddedObjectNumberByWeaponType(embeddedWeapon);
        this.embeddedWeapon = embeddedWeapon;
    }

    private void setEmbeddedObjectNumberByWeaponType(WeaponType embeddedWeapon){
        if (embeddedWeapon == WeaponType.M79) embeddedObjectNumber = AbstractCollectable.BULLETS_FOR_M79;
        else if (embeddedWeapon == WeaponType.REVOLVER) embeddedObjectNumber = AbstractCollectable.BULLETS_FOR_REVOLVER;
        else if (embeddedWeapon == WeaponType.HANDGUN) embeddedObjectNumber = AbstractCollectable.BULLETS_FOR_PISTOL;
        else if (embeddedWeapon == WeaponType.SHOTGUN || embeddedWeapon == WeaponType.SAWED_OFF_SHOTGUN) embeddedObjectNumber = AbstractCollectable.BULLETS_FOR_SHOTGUN;
        else if (embeddedWeapon == WeaponType.GRENADE) embeddedObjectNumber = AbstractCollectable.BULLETS_FOR_HAND_GRENADE;
        else if (embeddedWeapon == WeaponType.SMG) embeddedObjectNumber = AbstractCollectable.SMG;
        else {
            System.out.println("Can not set data for this weapon");
        }
    }

    public int getEmbeddedObjectNumber() {
        return embeddedObjectNumber;
    }

    public void setEmbeddedObjectNumber(int embeddedObjectNumber) {
        this.embeddedObjectNumber = embeddedObjectNumber;
    }
}
