package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.menu.shop.*;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ShopMenu extends AbstractMenuWithTitleAndBackButton{
    private FrameWithFace playerFace, handlerFace;
    private TextFrameForShop textFrameForShop;
    private FrameWithObjectData frameWithObjectData;
    private ActionPanel actionPanel;

    //private ShopController shopController;
    private ShopMenuController shopMenuController;
    private final PGraphics graphics;
    private boolean mainMusicStopped;
    private Timer musicStartTimer;
    private boolean musicStarted;
    public final static int MONEY_FOR_ADD = 150;

    public ShopMenu(PApplet engine, PGraphics graphics, Object userValue, GameMenusController gameMenusController) {
        this.graphics = graphics;
        initLanguageSpecific();
        super.init(engine, graphics);
        initFrames(graphics);
        frame.setShown(false);
        type = MenuType.SHOP;
        shopMenuController = new ShopMenuController(this);
    }

    private void changeSoundtrack(GameMenusController gameMenusController) {
        gameMenusController.getMusicController().stop();
        gameMenusController.getMusicController().loadNewMusic("Shop soundtrack.wav");
        gameMenusController.getMusicController().startToPlay();
    }


    private void initTextFrameForShop(int size){

        Vec2 faceLeftUpper = playerFace.getLeftUpper();
        Vec2 frameLeftUpper= new Vec2();
        int gap = (int) faceLeftUpper.x;
        frameLeftUpper.x = faceLeftUpper.x;
        frameLeftUpper.y = faceLeftUpper.y+playerFace.getFrameWidth();
        int lowerFrameY = (int) (Program.engine.height-playerFace.getFrameWidth()-playerFace.getLeftUpper().y);
        int width = (int) (handlerFace.getLeftUpper().x-frameLeftUpper.x+playerFace.getFrameWidth());
        int height = (int) (lowerFrameY-frameLeftUpper.y);
        //textFrameForShop = new TextFrameForShop(frameLeftUpper, width, height, shopMenuController.getText(ShopMenuController.TEXT_GREETING), graphics);
        //textFrameForShop = new TextFrameForShop(frameLeftUpper, width, height, null, graphics, size);
    }

    private void initFaces(){
        playerFace = new FrameWithFace(FrameWithFace.PLAYER, new Vec2(Program.engine.width/6, Program.engine.width/5));
        handlerFace = new FrameWithFace(FrameWithFace.HANDLER, new Vec2(5*Program.engine.width/6, Program.engine.width/5));

    }
    private void initFrames(PGraphics graphics){
        initFaces();
        Vec2 faceLeftUpper = playerFace.getLeftUpper();
        Vec2 frameLeftUpper= new Vec2();
        int gap = (int) faceLeftUpper.x;
        frameLeftUpper.x = faceLeftUpper.x;
        frameLeftUpper.y = faceLeftUpper.y+playerFace.getFrameWidth()+gap;
        int lowerFrameY = (int) (Program.engine.height-playerFace.getFrameWidth()-playerFace.getLeftUpper().y);
        int width = (int) (handlerFace.getLeftUpper().x-frameLeftUpper.x+playerFace.getFrameWidth());
        int height = (int) (lowerFrameY-frameLeftUpper.y);
        //textFrameForShop = new TextFrameForShop(frameLeftUpper, width, height, shopMenuController.getText(ShopMenuController.TEXT_GREETING), graphics);
        textFrameForShop = new TextFrameForShop(frameLeftUpper, width, height, null, graphics, TextFrameForShop.NORMAL_HEIGHT);

        //HUD_GraphicData.getImageZoneForWeaponType(WeaponType.REVOLVER)
        Vec2 objectDataFramePos = new Vec2(frameLeftUpper.x+frameLeftUpper.x, frameLeftUpper.y+frameLeftUpper.x);
        int objectDataFrameWidth = (int) (graphics.width-objectDataFramePos.x*2 );
        //int objectDataFrameHeight = (int) (faceHeight+2*frameLeftUpper.x);
        int objectDataFrameHeight = (int) (playerFace.getFrameWidth()*3f);

        frameWithObjectData = new FrameWithObjectData(objectDataFramePos, objectDataFrameWidth, objectDataFrameHeight, null, graphics, null);
        frameWithObjectData.createImageFrame(gap, gap, playerFace.getFrameWidth());

        //public ActionPanel(Vec2 leftUpperCorner, int width, int height, String text, PGraphics graphics) {
        float upperY = frameWithObjectData.getFrame().getLeftUpperCorner().y+frameWithObjectData.getFrame().getHeight()+gap;

        float lowerY = frame.getHeight()+frame.getLeftUpperCorner().y-gap;
        Vec2 actionPanelPos = new Vec2(frameWithObjectData.getFrame().getLeftUpperCorner().x, upperY);
        int actionPanelHeight = (int) (lowerY-upperY-3*gap);
        actionPanel = new ActionPanel(actionPanelPos, objectDataFrameWidth, actionPanelHeight, graphics);
        actionPanel.setVisible(false);
    }


    public void changeFramesDimensions(boolean maxAndMin)    {
        final float gap = playerFace.getLeftUpper().x;
        final int fullHeight = textFrameForShop.getFrame().getHeight();
        final float minHeight = playerFace.getFrameWidth()+gap*2;

        final float restHeight = fullHeight-minHeight;
        final float largeFrameHeight = restHeight-3*gap;

        if (maxAndMin){
            frameWithObjectData.getFrame().setHeight((int) largeFrameHeight);
            actionPanel.getFrame().setHeight((int) minHeight);
        }
        else {
            actionPanel.getFrame().setHeight((int) largeFrameHeight);
            frameWithObjectData.getFrame().setHeight((int) minHeight);
        }

        float secondAreaY = frameWithObjectData.getFrame().getLeftUpperCorner().y+frameWithObjectData.getFrame().getHeight()+gap;
        actionPanel.getFrame().setLeftUpperCorner(actionPanel.getFrame().getLeftUpperCorner().x, secondAreaY);

    }

    public FrameWithObjectData getFrameWithObjectData() {
        return frameWithObjectData;
    }

    protected void initLanguageSpecific() {
        //if (GO_TO_PREVIOUS_MENU != null) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- МАГАЗИН -";
        } else {
            titleName = "-GUN STORE-";
        }
        //}
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        updateGraphic(gameMenusController.getEngine());
        if (!mainMusicStopped){
            gameMenusController.stopMusic();
            mainMusicStopped = true;
            changeSoundtrack(gameMenusController);
        }
        //updateController(gameMenusController.getEngine());
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.CONTINUE_LAST_GAME);
                    gameMenusController.stopMusic();
                }
            }
        }
        shopMenuController.update(gameMenusController, mouseX , mouseY);
    }





    private void updateGraphic(PApplet engine) {
        playerFace.update(engine);
        handlerFace.update(engine);
        textFrameForShop.update(engine);
        frameWithObjectData.update(engine);
        actionPanel.update(engine);
    }

    @Override
    protected void setPrevPage() {

    }

    @Override
    protected void setNextPage() {

    }

    @Override
    public void draw(PGraphics graphics){

        super.draw(graphics);
        playerFace.draw(graphics);
        handlerFace.draw(graphics);
        textFrameForShop.draw(graphics);
        shopMenuController.draw(graphics);
        frameWithObjectData.draw(graphics);
        actionPanel.draw(graphics);
    }


    public FrameWithFace getPlayerFace() {
        return playerFace;
    }

    public FrameWithFace getHandlerFace() {
        return handlerFace;
    }

    public TextFrameForShop getTextFrameForShop() {
        return textFrameForShop;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public PGraphics getGraphics() {
        return graphics;
    }

    public void createTextFrameForShop(int fullHeight) {
        initTextFrameForShop(fullHeight);
    }

    public void createTextFrameForShop(float coef) {
        //initTextFrameForShop(coef);
    }
}
