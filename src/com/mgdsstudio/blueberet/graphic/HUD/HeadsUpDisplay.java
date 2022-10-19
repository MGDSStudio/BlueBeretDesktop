package com.mgdsstudio.blueberet.graphic.HUD;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.LowerBackground;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.SMS;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.UpperPanel;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public final class HeadsUpDisplay {
    public static Tileset mainGraphicTileset;
    public static Image mainGraphicSource = new Image(Program.getAbsolutePathToAssetsFolder("HUD"+ TextureDecodeManager.getExtensionForGraphicSources()));

    private final static boolean SELF_UPDATING = true;
    private OnScreenText fpsHud;
    public static boolean showFPS;
    //private OnScreenText levelNameText;
    public static PGraphics graphic;
    private Soldier player;
    private PlayerControl playerControl;
    private UpperPanel upperPanel;
    private LowerBackground lowerBackground;
    private boolean weaponChangingThroughSwipe;
    private int actualCPUUpdatingFrequency;
    private long memory;
    // private final static boolean withAdoptedRedrawing = PlayerControl.WITH_ADOPTING_GUI_REDRAWING;

    public static void loadMainGraphicTileset(){
        if (mainGraphicTileset == null){
            mainGraphicTileset  = new Tileset((int)100, mainGraphicSource, (byte)1, (byte)1);
        }
    }

    public HeadsUpDisplay(PlayerControl playerControl, Soldier soldier){
        /*
        imageZoneCoin = new ImageZoneSimpleData(430, 0, 505, 70);
        imageZoneStar = new ImageZoneSimpleData(430,68, 507, 140);
        imageZoneMushroom = new ImageZoneSimpleData(430,136, 507, 136+68);*/

        if (Program.graphicRenderer == Program.JAVA_RENDERER)
        graphic = Program.engine.createGraphics(Program.engine.width, Program.engine.height, PApplet.JAVA2D);
        else graphic = Program.engine.createGraphics(Program.engine.width, Program.engine.height, PApplet.P2D);
        graphic.noSmooth();
        //graphic.textFont();
        //else graphic.smooth(Program.ANTI_ALIASING);
        this.player = soldier;
        this.playerControl = playerControl;
        //fpsHud = new OnScreenText(0, UpperPanel.HEIGHT+ Program.engine.height-(16* Program.engine.height/17), Program.engine.color(212,15,15), Program.engine.height/50);
        //
        //mainGraphicTileset = new Tileset((int)100, mainGraphicSource, (byte)1, (byte)1);
        loadMainGraphicTileset();
        upperPanel = new UpperPanel(soldier, graphic, soldier.getDeblockedWeapons());

        lowerBackground = new LowerBackground(PlayerControl.SCREEN_CONTROL_AREA_HEIGHT);
        if (showFPS) fpsHud = new OnScreenText(Program.engine.width/20, Program.engine.height- lowerBackground.height+ (1*Program.engine.height/55), Program.engine.color(25,225,15), Program.engine.height/50);
        //levelNameText = new OnScreenText(Program.engine.width/2, Program.engine.height/2, Program.engine.color(25,225,15), Program.engine.height/50);

        soldier.setUpperPanel(upperPanel);
    }

    public void update(GameRound gameRound){
        if (SELF_UPDATING){
            if (Program.OS == Program.ANDROID){
            }
            if (!weaponChangingThroughSwipe) {
                if (playerControl.mustCameraConcentrateToPlayer()){
                    player.setGraphicOnWeaponChanging(true);
                }
                else player.setGraphicOnWeaponChanging(false);
            }
        }
        upperPanel.update((Soldier) gameRound.getPlayer());
        if (Program.engine.frameCount%10 == 0) memory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
    }

    private void changeWeapon(Person person, WeaponType newWeapon) {
        if (person.weapons.size()>1) {
            for (FirearmsWeapon weapon : player.weapons){
                if (weapon.getWeaponType() == newWeapon){
                    weapon.setWeaponAsActual(true);
                }
                else weapon.setWeaponAsActual(false);
            }
            playerControl.updateButtonShootingButtonStatement(player);
        }
    }

    private void drawWithRegularRedrawing(boolean mustBeHidden, ArrayList<OnScreenButton> buttons, boolean hideControlButtons){
        //System.out.println("I need to make adopted redrawing");
        if (!mustBeHidden) upperPanel.draw(graphic);
        else upperPanel.drawCleared(graphic);
        lowerBackground.draw(graphic);
        if (!hideControlButtons) {
            for (OnScreenButton onScreenButton : buttons) {
                if (!mustBeHidden || onScreenButton.getFunction() == PlayerControl.BUTTON_BACK_TO_REDACTOR) {
                    onScreenButton.draw(graphic);
                }
            }
        }
        if (!mustBeHidden) {

                    }
        if (showFPS) fpsHud.draw(graphic, getFPStextString()+'/'+actualCPUUpdatingFrequency + "; Memory: " + memory );

    }

    private void drawWithAdoptedRedrawing(boolean mustBeHidden, ArrayList<OnScreenButton> buttons, boolean hideControlButtons){
        if (!mustBeHidden) upperPanel.draw(graphic);
        else {
            //upperPanel.draw(graphic);
            upperPanel.drawCleared(graphic);
        }
        lowerBackground.draw(graphic);
        //if (mustBeHidden) System.out.println("Must be hidden");
        if (!hideControlButtons) {
            for (OnScreenButton onScreenButton : buttons) {
                if (onScreenButton.getFunction() == PlayerControl.BUTTON_BACK_TO_REDACTOR) {
                    //System.out.println("Button is pressed " + onScreenButton.getFunction());
                    onScreenButton.draw(graphic);
                }
                else if (!mustBeHidden) {
                    onScreenButton.draw(graphic);
                }
            }
        }
        if ((!mustBeHidden && Program.debug) || showFPS) {
           fpsHud.draw(graphic, getFPStextString()+"/CPU: "+actualCPUUpdatingFrequency + " RAM: " + memory + " mb" );
        }
        //else fpsHud.draw(graphic, getFPStextString()+"/CPU: "+actualCPUUpdatingFrequency + " RAM: " + memory + " mb" );
        /*else if (player.isAlive() ) {
            //fpsHud.draw(graphic, getFPStextString()+"/CPU: "+actualCPUUpdatingFrequency + " RAM: " + memory + " mb" );
            //System.out.println("Hidden !");
        }*/

    }

    public void draw(boolean mustBeHidden, ArrayList<OnScreenButton> buttons, boolean hideControlButtons){
        /*if (levelNameText != null){
            levelNameText.draw(graphic, "NAME");
        }*/
        if (PlayerControl.withAdoptingGuiRedrawing){
            drawWithAdoptedRedrawing(mustBeHidden, buttons, hideControlButtons);
        }
        else {
            drawWithRegularRedrawing(mustBeHidden, buttons, hideControlButtons);
        }

    }



    private String getFPStextString() {
        String FPS = "FPS: " + (int) Program.engine.frameRate;
        if (Program.graphicRenderer == Program.OPENGL_RENDERER) FPS = FPS + " OGL";
        else  FPS = FPS + " JAVA";
        return FPS;
    }

    public PGraphics getGraphic(){
        return graphic;
    }

    public void setNewWeapon(WeaponType weaponType) {
        upperPanel.setWeaponForDrawingInWeaponFrame(weaponType);
    }
    public void setNewInventoryObject(int type) {
        upperPanel.setInventoryObjectForDrawingInObjectFrame(type);
    }


    public boolean isMouseOnWeaponFrame() {
        return upperPanel.isMousePressedOnWeaponFrame();
    }
    public boolean isMousePressedOnInventoryFrame() {
        return upperPanel.isMousePressedOnInventoryFrame();
    }

    public boolean isMouseReleasedOnWeaponFrame() {
        return upperPanel.isMouseReleasedOnWeaponFrame();
    }
    public boolean isMouseOnInventoryFrame() {
        return upperPanel.isMouseOnInventoryFrame();
    }

    public boolean isMouseReleasedOnInventoryFrame() {
        return upperPanel.isMouseReleasedOnInventoryFrame();
    }


    public void clickOnWeaponFrame(GameRound gameRound) {
        upperPanel.clickOnWeaponsFrame(gameRound);
    }
    public void clickOnInventoryObjectFrame(GameRound gameRound) {
        upperPanel.clickOnInventoryFrame(gameRound);
    }

    public int getStartClosingFrameNumberForInventoryFrame(){
        return upperPanel.getStartClosingFrameNumberForInventoryFrame();
    }

    public int getStartOpeningFrameNumberForInventoryFrame(){
        return upperPanel.getStartOpeningFrameNumberForInventoryFrame();
    }


    public WeaponType getSelectedWeapon() {
        return upperPanel.getSelectedWeapon();
    }
    public boolean hasCellInternalCells() {
        return upperPanel.hasCellInternalCells();
    }

    public void changeCellToSubcell(Person player){
        upperPanel.changeCellToSubcell(player);
    }

    public boolean canCellBeChanged(){
        return upperPanel.canCellBeChanged();
    }

    public int getSelectedInventoryObjectType() {
        return upperPanel.getSelectedInventoryObject();
    }

    public int getDefaultInventoryObjectType() {
        return upperPanel.getDefaultInventoryObject();
    }

    public void setFrameToNewWeapon(WeaponType weaponType) {
        upperPanel.setWeaponFrameToWeapon(weaponType);
    }
    public void setFrameToNewInventoryObject(int newObjectType) {
        upperPanel.setInventoryFrameToObject( newObjectType);
    }

    public boolean isWeaponFrameActive() {
        return upperPanel.isWeaponFrameActive();
    }
    public boolean isInventoryFrameActive() {
        return upperPanel.isInventoryFrameActive();
    }

    public void stopWeaponSelecting() {
        if (isWeaponFrameActive()){
            upperPanel.closeWeaponFrame();
        }
    }
    public void stopInventoryObjectSelecting() {
        if (isInventoryFrameActive()){
            upperPanel.closeInventoryFrame();
        }
    }

    public boolean isWeaponFrameCompleteClosed() {
        return upperPanel.isWeaponFrameCompleteClosed();
    }
    public boolean isInventoryObjectFrameCompleteClosed() {
        return upperPanel.isInventoryFrameCompleteClosed();
    }

    public boolean isInventoryObjectFrameCompleteOpened() {
        return upperPanel.isInventoryFrameCompleteOpened();
    }

    public boolean isWeaponFrameCompleteOpened() {
        return upperPanel.isWeaponFrameCompleteOpened();
    }

    public void addMessage(SMS message, GameRound gameRound) {
        upperPanel.addMessage(message, gameRound);
    }

    public boolean areFramesFreeToClick() {
        return !upperPanel.areThereShownMessages();
    }


    public boolean isMessageFrameOpened() {
        if (upperPanel.areThereShownMessages()){
            return true;
        }
        else return false;
    }

    public boolean isMessageFrameClicked() {
        if (upperPanel.isMessageFrameClicked()){
            return true;
        }
        else return false;
    }

    public boolean closeMessageFrameIfPossible() {
        boolean ended =  upperPanel.closeMessageFrameIfPossible();
        return ended;
    }

    public boolean areThereShownMessages() {
        return upperPanel.areThereShownMessages();
    }

    public void startToCloseWeaponFrame() {
    }

    public void startToOpenWeaponFrame() {
    }

    public void startToCloseInventoryObjectsFrame() {
    }

    public void startToOpenInventoryObjectsFrame() {
    }



    public int getActualCPUUpdatingFrequency() {
        return actualCPUUpdatingFrequency;
    }

    public void setCPUUpdatingFrequency(int frequency) {

        actualCPUUpdatingFrequency = frequency;
    }

    public void confiscateObjectFromFrame(int objectType) {
        upperPanel.confiscateObjectFromFrame(objectType);
    }

    public void updateNumberForObjectsOnOpenedFrame(int objectType, int number){
        upperPanel.updateNumberForObjectsOnOpenedFrame(objectType, number);
    }

    public boolean canObjectFrameOpened() {
        return upperPanel.canObjectFrameBeOpened();
    }

    public void updateBulletsNumber(PlayerBag playerBag) {
        upperPanel.updateBulletsNumber(playerBag);
    }

    public void playerEndedReloading() {

    }

    public void playerStartedReloading() {
        upperPanel.redrawColorForBulletsLifeLineName(graphic);
        lowerBackground.deleteAllFromThePanel();
    }

    public void setRedrawUpperHudOnNextFrame(boolean flag) {
        upperPanel.setRedrawUpperHudOnNextFrame(flag);
    }

    public boolean isMouseOnUpperPanel() {
        if (upperPanel.isMouseOnUpperPanel()) return true;
        else return false;
    }

    public boolean isMouseOnLowerPanel() {
        if (lowerBackground.isMouseOnLowerBackground()) return true;
        else return false;
    }

    public void setRegularRedrawn() {
        lowerBackground.setRegularRedrawn(true);
    }

    public void clearLowerPanel() {
        lowerBackground.clearPanel(graphic);
    }

    public ArrayList<WeaponType> getVisibleWeaponsOnPanel() {
        return upperPanel.getVisibleWeaponsOnPanel();
    }
}
