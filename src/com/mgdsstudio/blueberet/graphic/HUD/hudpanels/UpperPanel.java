package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.oldlevelseditor.Editor2D;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public class UpperPanel {
    public static final int HEIGHT = (int)(Program.engine.width/4f);//(Program.DISPLAY_PROPORTION/Program.DISPLAY_PROPORTION_FOR_XIAOMI);
    private LineController lifeLineValueController, bulletsValueController, restAmmoController, moneyController;
    private PFont textFont;
    private final int mainFontHeight;
    public final static int ACTIVE_COLOR = 255; //was 180
    private EightPartsFrameImage weaponFrame, inventoryFrame, dialogFrameInScreenCenter, portraitZoneFrame, textZoneFrame;//, ;
    private final static int NORMAL_DIALOG_FRAME_WIDTH = (int) (Program.engine.width*0.7f);
    private static int NORMAL_DIALOG_FRAME_HEIGHT;
    private WeaponFrameController weaponFrameController;
    private CollectableObjectsFrameController inventoryObjectsFrameController;
    private UpperPanelMessageController messageController;
    private final int ADDITIONAL_GAP = Program.engine.width/100;
    private boolean actualVisibilityStatement = OnScreenButton.VISIBLE, prevVisibilityStatement = !OnScreenButton.VISIBLE;
    private boolean forceRedrawingOnNextFrame = true;
    private boolean firstRedrawn = false;
    private boolean forceBackgroundRedrawing;
    public final static int WIDTH_FOR_WEAPON_FRAME = (int) (HEIGHT*1f);
    public final static int HEIGHT_FOR_WEAPON_FRAME = (int) (HEIGHT*0.75f);
    public final static int WIDTH_FOR_OBJECTS_FRAME = (int)(HEIGHT_FOR_WEAPON_FRAME *0.8f);
    public final static int BASIC_SOURCE_HEIGHT_FOR_SIMPLE_FRAMES = (int) (HEIGHT*0.35f);
    public final static int NORMAL_FONT_HEIGHT = Program.engine.width/23;

    public UpperPanel(Soldier player, PGraphics graphics, ArrayList<WeaponType> playersDeblockedWeapons) {

        if (Program.USE_MAIN_FONT_IN_GAME) {
            textFont = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.mainFont));
            mainFontHeight = NORMAL_FONT_HEIGHT;
        }
        else {
            textFont = Program.engine.loadFont(Program.getAbsolutePathToAssetsFolder(Program.secondaryFont));
            mainFontHeight = (int) ((float) NORMAL_FONT_HEIGHT *Program.FONTS_DIMENSION_RELATIONSHIP);
        }

        //mainFontHeight = Program.engine.width/23;
        setTextGraphicData(graphics);
        int lifeTextPosX = HEIGHT/6;
        int lifeTextPosY = HEIGHT/6;
        int yTextStep = HEIGHT/5;
        lifeLineValueController = new LifeLineValueController(player, lifeTextPosX, lifeTextPosY, graphics);
        lifeTextPosY+= (int)((yTextStep*1f));
        bulletsValueController = new BulletsValueController(player, lifeTextPosX, lifeTextPosY, graphics);
        lifeTextPosY+= (int)((yTextStep*1f));
        restAmmoController = new RestAmmoValueController(player, lifeTextPosX, lifeTextPosY, graphics);
        lifeTextPosY+= (int)((yTextStep*1f));
        moneyController = new MoneyValueController(player, lifeTextPosX, lifeTextPosY, graphics);
        NORMAL_DIALOG_FRAME_HEIGHT = (int) (GameCamera.VISIBLE_Y_AREA_HEIGHT*0.6f);
        initFrames();
        int maxWidthWhenOpened = calculateMaxWidthForFullOpenedWeaponsFrame(4);  //Programm.engine.width-(HEIGHT- weaponFrame.getHeight());
        System.out.println("Frame width for weapons: " + maxWidthWhenOpened);
        weaponFrameController = new WeaponFrameController(HeadsUpDisplay.mainGraphicTileset, weaponFrame, maxWidthWhenOpened, weaponFrame.getHeight(), playersDeblockedWeapons, player.getOnUpperPanelWeapons(), Weapon.getWeaponCodeForType(player.getActualWeapon().getWeaponType()), player.getPlayerBag());
        maxWidthWhenOpened = calculateMaxWidthForFullOpenedInventoryFrame(weaponFrame.getWidth());  //Programm.engine.width-(HEIGHT- weaponFrame.getHeight());
        System.out.println("Frame width for objects: " + maxWidthWhenOpened + " ");
        int mainSelectedObject = player.getPlayerBag().getObjectOnMainFrame();
        inventoryObjectsFrameController = new CollectableObjectsFrameController(HeadsUpDisplay.mainGraphicTileset, inventoryFrame, maxWidthWhenOpened, inventoryFrame.getHeight(), player.getPlayerBag(), mainSelectedObject);
        messageController = new UpperPanelMessageController(this , HeadsUpDisplay.mainGraphicTileset, portraitZoneFrame, textZoneFrame, graphics.textFont);
    }

    private int calculateMaxWidthForFullOpenedInventoryFrame (int weaponFrameWidth){
        int maxWidth = Program.engine.width-(HEIGHT- weaponFrame.getHeight());
        return (maxWidth-weaponFrameWidth);

    }

    /*
    private int calculateMaxWidthForFullOpenedInventoryFrame (int weaponFrameWidth, int elementsNumber){
        int maxWidth = Program.engine.width-(HEIGHT- weaponFrame.getHeight());
        if (elementsNumber >= 4){
            return maxWidth-weaponFrameWidth;
            //return Programm.engine.width-(HEIGHT- weaponFrame.getHeight());
        }
        else {
            //System.out.println("Width for frame: " + ((maxWidth*elementsNumber/4)-weaponFrameWidth));
            return (maxWidth-weaponFrameWidth);
            //return (maxWidth*elementsNumber/4)-weaponFrameWidth;
        }
    }
     */


    private int calculateMaxWidthForFullOpenedWeaponsFrame (int elementsNumber){
        int maxWidth = Program.engine.width-(HEIGHT- weaponFrame.getHeight());
        if (elementsNumber >= 4){
            return maxWidth;
            //return Programm.engine.width-(HEIGHT- weaponFrame.getHeight());
        }
        else {
            return maxWidth*elementsNumber/4;
        }
    }

    private void initFrames() {

        ImageZoneSimpleData basicRectForSimpleFrames = HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners;
        initWeaponFrame(BASIC_SOURCE_HEIGHT_FOR_SIMPLE_FRAMES, basicRectForSimpleFrames);
        initInventoryFrameAsSingle(BASIC_SOURCE_HEIGHT_FOR_SIMPLE_FRAMES, basicRectForSimpleFrames);
        //initMedicalKitFrame(basicSourceHeightForSimpleFrames, basicRectForSimpleFrames);
        int basicSourceHeightForDialogFrames = (int) (HEIGHT*0.7f);
        ImageZoneSimpleData basicRectForDialogFrames = HUD_GraphicData.basicRectForDialogFrames;
        initDialogFrame(basicSourceHeightForDialogFrames, basicRectForDialogFrames);
        float freeZone = Program.engine.width/200f;
        initPortraitFrame(basicSourceHeightForDialogFrames, basicRectForDialogFrames, freeZone);
        initTextZoneFrame(basicSourceHeightForDialogFrames, basicRectForDialogFrames, freeZone);
        dialogFrameInScreenCenter.setShown(false);
        //textZoneFrame.setShown(false);
        //portraitZoneFrame.setShown(false);
    }



    private void initDialogFrame(int basicSourceForDialogFrames, ImageZoneSimpleData basicRectForDialogFrames) {
        int basicWidth = basicSourceForDialogFrames;
        dialogFrameInScreenCenter = new EightPartsFrameImage(basicRectForDialogFrames,  basicWidth, basicSourceForDialogFrames, NORMAL_DIALOG_FRAME_WIDTH, NORMAL_DIALOG_FRAME_HEIGHT, new Vec2(Program.engine.width/2-NORMAL_DIALOG_FRAME_WIDTH/2, GameCamera.SCREEN_Y_CENTER-NORMAL_DIALOG_FRAME_HEIGHT/2) );
    }

    private void initPortraitFrame(int basicSourceForDialogFrames, ImageZoneSimpleData basicRectForDialogFrames, float freeZone) {
        int basicWidth = basicSourceForDialogFrames;
        float posX = freeZone;
        float posY = freeZone;
        int width = (int) (HEIGHT-2*freeZone);
        int height = width;
        portraitZoneFrame = new EightPartsFrameImage(basicRectForDialogFrames,  basicWidth, basicSourceForDialogFrames, width, height, new Vec2(posX, posY) );
    }

    private void initTextZoneFrame(int basicSourceForDialogFrames, ImageZoneSimpleData basicRectForDialogFrames, float freeZone) {
        int basicWidth = basicSourceForDialogFrames;
        float posX = portraitZoneFrame.getLeftUpperCorner().x+ portraitZoneFrame.getWidth()+freeZone;
        float posY = portraitZoneFrame.getLeftUpperCorner().y;
        int width = (int) (Program.engine.width-freeZone-posX);
        int height = (int) (HEIGHT-2*freeZone);
        textZoneFrame = new EightPartsFrameImage(basicRectForDialogFrames,  basicWidth, basicSourceForDialogFrames, width, height, new Vec2(posX, posY) );

    }

    private void initWeaponFrame(int basicSourceForSimpleFrames, ImageZoneSimpleData basicRectForSimpleFrames) {
        //int visibleWidth = (int) (HEIGHT*1f/(Program.DISPLAY_PROPORTION*Program.DISPLAY_PROPORTION_FOR_XIAOMI));
        int visibleWidth = WIDTH_FOR_WEAPON_FRAME;
        int visibleHeight = HEIGHT_FOR_WEAPON_FRAME;
        weaponFrame = new EightPartsFrameImage(basicRectForSimpleFrames, basicSourceForSimpleFrames,basicSourceForSimpleFrames, visibleWidth, visibleHeight, new Vec2(Program.engine.width-visibleWidth-(HEIGHT-visibleHeight)/2, 1*(HEIGHT/2-visibleHeight/2)) );
    }

    private void initInventoryFrameAsSingle(int basicSourceForSimpleFrames, ImageZoneSimpleData basicRectForSimpleFrames) {
        int visibleWidth = (int) (HEIGHT*0.65f);
        //int visibleWidth = (int) (HEIGHT*1f/(Program.DISPLAY_PROPORTION*Program.DISPLAY_PROPORTION_FOR_XIAOMI));
        //int visibleHeight = (int) (HEIGHT*0.75f);
        inventoryFrame = new EightPartsFrameImage(basicRectForSimpleFrames, basicSourceForSimpleFrames,basicSourceForSimpleFrames, visibleWidth, weaponFrame.getHeight(), new Vec2(weaponFrame.getLeftUpperCorner().x-visibleWidth-ADDITIONAL_GAP, weaponFrame.getLeftUpperCorner().y) );
        //inventoryFrame = new EightPartsFrameImage(basicRectForSimpleFrames, basicSourceForSimpleFrames,basicSourceForSimpleFrames, weaponFrame.getHeight(), weaponFrame.getHeight(), new Vec2(Programm.engine.width-visibleWidth-(HEIGHT-visibleHeight)/2-ADDITIONAL_GAP-weaponFrame.getWidth(), 1*(HEIGHT/2-visibleHeight/2)) );
    }

    private void drawWithRegularRedrawing(PGraphics graphics){
        drawBackground(graphics);
        setTextGraphicData(graphics);
        //System.out.println("Next and last strings must be deleted and ");
        PlayerControl.withAdoptingGuiRedrawing = false;
        if (weaponFrameController.isFrameClosed() && inventoryObjectsFrameController.isFrameClosed()) {
            if (!messageController.areThereShownMessages()) {
                lifeLineValueController.draw(graphics);
                bulletsValueController.draw(graphics);
                restAmmoController.draw(graphics);
                moneyController.draw(graphics);
                inventoryFrame.draw(graphics);
            }
        }
        if (inventoryObjectsFrameController.isFrameClosed()) weaponFrameController.draw(graphics, FrameController.DRAW_IN_FRAME_CENTER);
        if (weaponFrameController.isFrameClosed()) inventoryObjectsFrameController.draw(graphics, FrameController.DRAW_IN_FRAME_CENTER);
        messageController.draw(graphics, 0);
        PlayerControl.withAdoptingGuiRedrawing = true;
    }

    private void drawWithAdoptedRedrawing(PGraphics graphics){
        if (mustBeBackgroundRedrawn()) drawBackground(graphics);
        setTextGraphicData(graphics);
        if (weaponFrameController.isFrameClosed() && inventoryObjectsFrameController.isFrameClosed()) {
            if (!messageController.areThereShownMessages()) {
                lifeLineValueController.draw(graphics);
                bulletsValueController.draw(graphics);
                restAmmoController.draw(graphics);
                moneyController.draw(graphics);
                inventoryFrame.draw(graphics);
            }
        }
        if (inventoryObjectsFrameController.isFrameClosed()) weaponFrameController.draw(graphics, FrameController.DRAW_IN_FRAME_CENTER);
        if (weaponFrameController.isFrameClosed()) inventoryObjectsFrameController.draw(graphics, FrameController.DRAW_IN_FRAME_CENTER);
        messageController.draw(graphics, 0);

    }

    public void draw(PGraphics graphics){
        if (PlayerControl.withAdoptingGuiRedrawing && !forceRedrawingOnNextFrame){ //was alse || forceRedrawingOnNextFrame
            drawWithAdoptedRedrawing(graphics);
        }
        else {
            drawWithRegularRedrawing(graphics);
            if (forceRedrawingOnNextFrame) forceRedrawingOnNextFrame = false;
        }
        prevVisibilityStatement = actualVisibilityStatement;
    }

    protected void setTextGraphicData(PGraphics graphics){
        graphics.textFont(textFont, mainFontHeight);    //maybe only once?
        setActiveColor(graphics);
        graphics.textAlign(PConstants.LEFT, PConstants.TOP);    //maybe only once?
    }

    protected void setActiveColor(PGraphics graphics){
        graphics.fill(ACTIVE_COLOR); //maybe only once?
    }

    private void drawBackground(PGraphics graphics){
        //graphics.pushStyle();
        //graphics.noTint();
        if (graphics.imageMode == PConstants.CENTER) graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), Program.engine.width/2,HEIGHT/2,Program.engine.width, HEIGHT, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
        else graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), 0,0,Program.engine.width, HEIGHT, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);

        //graphics.rectMode(PConstants.CENTER);
        //graphics.rectMode(PConstants.CORNER);
        //graphics.popStyle();
        //graphics.image(HeadsUpDisplay.mainGraphicSource.getImage(), 0,0,Program.engine.width, HEIGHT, HUD_GraphicData.blackColor.leftX, HUD_GraphicData.blackColor.upperY, HUD_GraphicData.blackColor.rightX, HUD_GraphicData.blackColor.lowerY);
        //
        /*
        graphics.pushStyle();
        graphics.noStroke();
        graphics.rectMode(PConstants.CORNER);
        graphics.fill(0);
        graphics.rect(0,0, Program.engine.width, HEIGHT);
        graphics.popStyle();
        */
    }

    private boolean mustBeBackgroundRedrawn() {
        if (PlayerControl.withAdoptingGuiRedrawing || forceBackgroundRedrawing){
            if (forceBackgroundRedrawing) forceBackgroundRedrawing = false;
            if (prevVisibilityStatement == false && actualVisibilityStatement == OnScreenButton.VISIBLE) return true;
            else {
                if (!weaponFrameController.isFrameClosed() || !inventoryObjectsFrameController.isFrameClosed() || forceRedrawingOnNextFrame || !firstRedrawn) {
                    if (!firstRedrawn) {
                        firstRedrawn = true;
                        forceRedrawingOnNextFrame = true;
                    }
                    return true;
                }
                else return false;
            }
        }
        else return true;
    }

    public void setWeaponForDrawingInWeaponFrame(WeaponType weapon){
        weaponFrameController.setActualWeapon(weapon);
    }

    public void setInventoryObjectForDrawingInObjectFrame(int objectType){
        //inventoryObjectsFrameController.setActualObject(objectType);
        //setActualObject
        inventoryObjectsFrameController.setFrameToObject(objectType);
        inventoryObjectsFrameController.setActualObject(objectType);
    }

    public void update(Soldier player) {
        weaponFrameController.update();
        inventoryObjectsFrameController.update();
        lifeLineValueController.update(player);
        bulletsValueController.update(player);
        restAmmoController.update(player);
        moneyController.update(player);
        if (weaponFrameController.isClosedAtThisFrame() || inventoryObjectsFrameController.isClosedAtThisFrame() || forceRedrawingOnNextFrame || messageController.areThereShownMessages()){
            /*if (inventoryObjectsFrameController.isClosedAtThisFrame()) System.out.println("Weapon frame was closed and must be redrawn" );
            if (inventoryObjectsFrameController.isClosedAtThisFrame()) System.out.println("Inventory frame was closed and must be redrawn" );
            if (forceRedrawingOnNextFrame) System.out.println("Frame must be redrawn after some object was got" );*/
            setUpperPanelMustBeRedrawn();
        }
    }

    private void setUpperPanelMustBeRedrawn() {
        forceBackgroundRedrawing = true;
        lifeLineValueController.setLineMustBeRedrawnAtThisFrame(true);
        bulletsValueController.setLineMustBeRedrawnAtThisFrame(true);
        restAmmoController.setLineMustBeRedrawnAtThisFrame(true);
        moneyController.setLineMustBeRedrawnAtThisFrame(true);
    }

    public void drawCleared(PGraphics graphic) {
        drawBackground(graphic);
    }

    public boolean isMousePressedOnWeaponFrame() {
        return weaponFrameController.isMouseOnFrame(Program.engine.mouseX, Program.engine.mouseY);
    }

    public boolean isMousePressedOnInventoryFrame() {
        return inventoryObjectsFrameController.isMouseOnFrame(Program.engine.mouseX, Program.engine.mouseY);
    }

    public void clickOnWeaponsFrame(GameRound gameRound) {
        weaponFrameController.clickOnFrame(gameRound);
    }

    public void clickOnInventoryFrame(GameRound gameRound) {
        inventoryObjectsFrameController.clickOnFrame(gameRound);
    }

    public int getStartClosingFrameNumberForInventoryFrame(){
        return inventoryObjectsFrameController.getStartClosingFrameNumber();
    }

    public int getStartOpeningFrameNumberForInventoryFrame(){
        return inventoryObjectsFrameController.getStartOpeningFrameNumber();
    }

    public WeaponType getSelectedWeapon() {
        //return weaponFrameController.getSelectedObjectCode(Weapon.getWeaponCodeForType());
        return weaponFrameController.getSelectedWeaponType();
    }

    public boolean hasCellInternalCells() {
        //return weaponFrameController.getSelectedObjectCode(Weapon.getWeaponCodeForType());
        return weaponFrameController.hasCellInternalCells();
    }

    public void changeCellToSubcell(Person player){
        weaponFrameController.changeCellToSubcell(player);
    }

    public boolean canCellBeChanged(){
        return weaponFrameController.canCellBeChanged();
    }
    public int getSelectedInventoryObject() {
        return inventoryObjectsFrameController.getSelectedObjectCode();
    }

    public int getDefaultInventoryObject() {
        return inventoryObjectsFrameController.getDefaultObjectCode();
    }

    public boolean isMouseReleasedOnWeaponFrame() {
        if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
            return weaponFrameController.isMouseOnFrame(Program.engine.mouseX, Program.engine.mouseY);
        }
        return false;
    }

    public boolean isMouseOnInventoryFrame() {
        //if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
            return inventoryObjectsFrameController.isMouseOnFrame(Program.engine.mouseX, Program.engine.mouseY);
        //}
        //return false;
    }

    public boolean isMouseReleasedOnInventoryFrame() {
        if (!Program.engine.mousePressed && Editor2D.prevMousePressedStatement){
            return inventoryObjectsFrameController.isMouseOnFrame(Program.engine.mouseX, Program.engine.mouseY);
        }
        return false;
    }

    public void setWeaponFrameToWeapon(WeaponType weaponType) {
        weaponFrameController.setFrameToObject(Weapon.getWeaponCodeForType(weaponType));
    }

    public void setInventoryFrameToObject(int type) {
        inventoryObjectsFrameController.setFrameToObject(type);
    }

    public boolean isWeaponFrameActive() {
        if (weaponFrameController.isFrameClosed()) return false;
        else return true;
    }

    public boolean isInventoryFrameActive() {
        if (inventoryObjectsFrameController.isFrameClosed()) return false;
        else return true;
    }

    public void closeWeaponFrame() {
        if (!weaponFrameController.isFrameClosed()){
            weaponFrameController.closeFrame();
            System.out.println("Frame is closing");
        }
    }

    public void closeInventoryFrame() {
        if (!inventoryObjectsFrameController.isFrameClosed()){
            inventoryObjectsFrameController.closeFrame();
            System.out.println("Inventory frame is closing");
        }
    }

    public boolean isWeaponFrameCompleteClosed() {
        return weaponFrameController.isFrameClosed();
    }

    public boolean isWeaponFrameCompleteOpened() {
        return weaponFrameController.isFrameOpened();
    }

    public boolean isInventoryFrameCompleteClosed() {
        return inventoryObjectsFrameController.isFrameClosed();
    }

    public boolean isInventoryFrameCompleteOpened() {
        return inventoryObjectsFrameController.isFrameOpened();
    }

    public void addMessage(SMS message, GameRound gameRound) {
        messageController.addMessage(message, gameRound);
    }

    public boolean areThereShownMessages() {
        if (!messageController.areThereShownMessages()) return false;
        else return true;
    }

    public boolean isMessageFrameClicked() {
        return messageController.isFrameClicked();
    }

    public boolean closeMessageFrameIfPossible() {
        boolean ended = messageController.closeMessageFrameIfPossible();
        return ended;
    }

    public void updateInventoryObjectsNumber(int type, int number) {
        //inventoryObjectsFrameController = null;
        //inventoryObjectsFrameController = new CollectableObjectsFrameController();
        inventoryObjectsFrameController.addNewObjectToFrame(type, number);
        //medicalKitFrame.updateKitsNumber(playerBag);
    }

    public void confiscateObjectFromFrame(int objectType) {
        inventoryObjectsFrameController.confiscateObjectFromFrame(objectType);
    }

    public void updateNumberForObjectsOnOpenedFrame(int objectType, int number) {
        inventoryObjectsFrameController.updateNumberForObjectsOnOpenedFrame(objectType, number);
    }



    public boolean canObjectFrameBeOpened() {
        return inventoryObjectsFrameController.canObjectFrameBeOpened();
    }

    public void updateBulletsNumber(PlayerBag playerBag) {
        weaponFrameController.updateBulletsNumber(playerBag);
    }

    public void setRedrawUpperHudOnNextFrame(boolean flag) {
       forceRedrawingOnNextFrame = flag;
    }

    public boolean isMouseOnUpperPanel() {
        if (Program.engine.mouseX>0 && (Program.engine.mouseX<Program.engine.width)){
            if (Program.engine.mouseY>0 && Program.engine.mouseY<HEIGHT){
                return true;
            }
        }
        return false;
    }

    public void redrawColorForBulletsLifeLineName(PGraphics graphics) {
        BulletsValueController bulletsValueController = (BulletsValueController)this.bulletsValueController;
        bulletsValueController.resetColorToNormal(graphics);
    }

    public ArrayList<WeaponType> getVisibleWeaponsOnPanel() {
        return weaponFrameController.getVisibleWeaponsOnPanel();
    }
}
