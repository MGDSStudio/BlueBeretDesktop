package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.graphic.CustomOnPanelImage;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.Collections;

public class WeaponFrameController extends OpenableFrameController{

    protected ArrayList <SingleCellWithImage> hiddenCells;
    private final ImageZoneSimpleData arrow;
    protected ArrayList<CustomOnPanelImage> hiddenImages;
    private WeaponType actualWeapon;
    private final int weaponsAlongX = 4;

    private boolean withSubCells;

    private boolean [] arrowsMask;
    private ArrowBlinkController arrowBlinkController;
    //private ArrayList <WeaponType> weaponsOnUpperPanel;
    //private ArrayList <WeaponType> weaponsOnSubPage;
    //private ArrayList <WeaponType> weapons;
    //private ArrayList <WeaponType> weaponsOnUpperPanel;

    //private HashMap<Integer, SingleCellWithImage> hiddenCells;
    //private HashMap<Integer, CustomOnPanelImage> hiddenImages;

    WeaponFrameController (Tileset tileset, EightPartsFrameImage frame, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeapons, ArrayList<WeaponType> weaponsOnUpperPanel,  int actualWeapon, PlayerBag playerBag){
        //fillWeaponsLists(playersWeapons, weaponsOnUpperPanel);
        arrow = HUD_GraphicData.listArrow;
        cells = new ArrayList<>( );
        images = new ArrayList<>();
        hiddenCells = new ArrayList<>( );
        hiddenImages = new ArrayList<>();
        this.mainFrame = frame;
        boolean openingBlocked = true;
        if (playersWeapons.size()>1) openingBlocked = false;
        frameDimensionChanger = new FrameDimensionChanger(frame, FrameDimensionChanger.NORMAL_TIME_TO_OPEN_FRAME,4,maxOpenedWidth, maxOpenedHeight,FrameDimensionChanger.FROM_RIGHT_TO_LEFT, openingBlocked);
        ArrayList <WeaponType> realWeaponsOnUpperPanel = getRealWeaponsOnUpperPanel(playersWeapons, weaponsOnUpperPanel);
        initGraphic(tileset, maxOpenedWidth, maxOpenedHeight, realWeaponsOnUpperPanel, playersWeapons, playerBag);
        //initGraphicForLowerLine(tileset, maxOpenedWidth, maxOpenedHeight, playersWeapons, weaponsOnUpperPanel, playerBag);

        int basicSourceHeightForSimpleFrames = (int) (UpperPanel.HEIGHT*0.35f);
        Vec2 posForSelectingFrame = getCenterForSelectingFrame(actualWeapon);
        initSelectingFrame(posForSelectingFrame, basicSourceHeightForSimpleFrames, HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners);
        setActualWeapon(Weapon.getWeaponTypeForCode(actualWeapon));
        initArrows();

    }

    public static WeaponType getAnotherWeaponInCell(WeaponType weaponType) {
        if (weaponType == WeaponType.REVOLVER) return  WeaponType.HANDGUN;
        else if (weaponType == WeaponType.HANDGUN) return  WeaponType.REVOLVER;
        else if (weaponType == WeaponType.SHOTGUN) return  WeaponType.SAWED_OFF_SHOTGUN;
        else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return  WeaponType.SHOTGUN;
        else if (weaponType == WeaponType.M79) return  WeaponType.GRENADE;
        else if (weaponType == WeaponType.GRENADE) return  WeaponType.M79;
        else if (weaponType == WeaponType.SMG) return null;
        else {
            System.out.println("No data about this weapon " + weaponType);
            return null;
        }
    }

    private ArrayList<WeaponType> getRealWeaponsOnUpperPanel(ArrayList<WeaponType> playersWeapons, ArrayList<WeaponType> weaponsOnUpperPanel) {
        ArrayList<WeaponType> onUpperRealList = new ArrayList<>();
        for (WeaponType deblocked : playersWeapons){
            boolean exist = false;
            for (WeaponType onPanel : weaponsOnUpperPanel){
                if (onPanel == deblocked){
                    onUpperRealList.add(onPanel);
                    break;
                }
            }
        }
        System.out.println("On panel in data file " + weaponsOnUpperPanel.size() + " weapons but real: " + onUpperRealList.size());
        return onUpperRealList;
    }

    private void initArrows() {
        arrowsMask = new boolean[weaponsAlongX];
        for (int i = 0; i < cells.size();i++){
            if (cells.get(i).hasCellInternalCells()){
                arrowsMask[i] = true;
            }
            else arrowsMask[i] = false;
        }
        arrowBlinkController = new ArrowBlinkController(cells);
    }

    private void initGraphic(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeaponsOnUpperLine, ArrayList<WeaponType> playersWeapons, PlayerBag playerBag){
        int additionalGap = (int) (mainFrame.getWidth()*0.1f);
        int height = mainFrame.getHeight()-additionalGap ;
        int xPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().x+ mainFrame.getWidth()/2);
        int yPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2);
        int invisibleCells = playersWeapons.size()-playersWeaponsOnUpperLine.size();
        int [] weaponsList = getWeaponsList(playersWeaponsOnUpperLine);
        for (int i = 0; i < weaponsList.length; i++){
            images.add(new CustomOnPanelImage(weaponsList[i], tileset, mainFrame.getWidth()-additionalGap, height, xPosInFrameCenter, yPosInFrameCenter));
        }
        System.out.println("Created " + weaponsList.length + " cells with weapons for upper cell");
        ArrayList <WeaponType> hiddenWeapons = getWeaponsOnLowerPanel(playersWeaponsOnUpperLine,  playersWeapons);
        weaponsList = getWeaponsList(hiddenWeapons);
        for (int i = 0; i < invisibleCells; i++){
            hiddenImages.add(new CustomOnPanelImage(weaponsList[i], tileset, mainFrame.getWidth()-additionalGap, height, xPosInFrameCenter, yPosInFrameCenter));
            images.add(new CustomOnPanelImage(weaponsList[i], tileset, mainFrame.getWidth()-additionalGap, height, xPosInFrameCenter, yPosInFrameCenter));
        }
        initCellsGraphicForWeapons(tileset,  maxOpenedWidth,  maxOpenedHeight, playersWeaponsOnUpperLine, hiddenWeapons, additionalGap, playerBag);
    }

    private ArrayList <WeaponType> getWeaponsOnLowerPanel(ArrayList<WeaponType> playerWeaponsOnUpperLine, ArrayList<WeaponType> playersWeapons){
        ArrayList <WeaponType> onLowerPanel = new ArrayList<>();
        for (int i = 0; i < playersWeapons.size(); i++){
            boolean existOnUpper = false;
            for (int j = 0; j < playerWeaponsOnUpperLine.size(); j++){
                if (playerWeaponsOnUpperLine.get(j) == playersWeapons.get(i)) existOnUpper = true;
            }
            if (!existOnUpper) onLowerPanel.add(playersWeapons.get(i));
        }
        return onLowerPanel;
    }

    private void initCellsGraphicForWeapons(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeaponsOnUpperLine, ArrayList<WeaponType> playersWeaponsOnLowerLine, int additionalGap,  PlayerBag playerBag) {
        final int widthForWeaponsInBag = (maxOpenedWidth- mainFrame.getWidth())/((weaponsAlongX+1))-additionalGap;
        int height = (int) ((maxOpenedHeight-additionalGap)*0.6f);
        String textStringToBeSet = REST_TEXT;
        for (int i = 0; i < playersWeaponsOnUpperLine.size(); i++){
            CustomOnPanelImage weaponOnPanelImage = new CustomOnPanelImage(Weapon.getWeaponCodeForType(playersWeaponsOnUpperLine.get(i)), tileset, widthForWeaponsInBag, height, 200, (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2), textStringToBeSet);
            SingleCellWithImage singleCellWithWeapon = new SingleCellWithImage(this, weaponOnPanelImage, 1+i, 1, weaponsAlongX, 1 );
            cells.add(singleCellWithWeapon);
        }
        for (int i = 0; i < playersWeaponsOnLowerLine.size(); i++){
            CustomOnPanelImage weaponOnPanelImage = new CustomOnPanelImage(Weapon.getWeaponCodeForType(playersWeaponsOnLowerLine.get(i)), tileset, widthForWeaponsInBag, height, 200, (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2), textStringToBeSet);
            SingleCellWithImage singleCellWithWeapon = new SingleCellWithImage(this, weaponOnPanelImage, 1+i, 1, weaponsAlongX, 1 );
            hiddenCells.add(singleCellWithWeapon);
        }
        addSubCells(playersWeaponsOnLowerLine, playersWeaponsOnUpperLine);
    }

    private void addSubCells(ArrayList <WeaponType> hiddenWeapons, ArrayList <WeaponType> weaponsOnUpperPanel) {
        boolean twoHandguns = false;
        boolean twoShotguns = false;
        boolean twoGrenades = false;
        System.out.println(" *** Weapons list on upper: ");
        for (int i = 0; i < weaponsOnUpperPanel.size(); i++){
            System.out.print(weaponsOnUpperPanel.get(i) + ", ");
        }
        System.out.println();
        System.out.println(" *** Weapons list on lower: ");
        for (int i = 0; i < hiddenWeapons.size(); i++){
            System.out.print(hiddenWeapons.get(i) + ", ");
        }
        System.out.println();
        for (int i = 0; i < weaponsOnUpperPanel.size(); i++){
            if (weaponsOnUpperPanel.get(i) == WeaponType.HANDGUN){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.REVOLVER){
                        twoHandguns = true;
                    }
                }
            }
            else if (weaponsOnUpperPanel.get(i) == WeaponType.REVOLVER){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.HANDGUN){
                        twoHandguns = true;
                    }
                }
            }
            else if (weaponsOnUpperPanel.get(i) == WeaponType.SHOTGUN){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.SAWED_OFF_SHOTGUN){
                        twoShotguns = true;
                    }
                }
            }
            else if (weaponsOnUpperPanel.get(i) == WeaponType.SAWED_OFF_SHOTGUN){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.SHOTGUN){
                        twoShotguns = true;
                    }
                }
            }
            else if (weaponsOnUpperPanel.get(i) == WeaponType.M79){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.GRENADE){
                        twoGrenades = true;
                    }
                }
            }
            else if (weaponsOnUpperPanel.get(i) == WeaponType.GRENADE){
                for (int j = 0; j < hiddenWeapons.size(); j++){
                    if (hiddenWeapons.get(j) == WeaponType.M79){
                        twoGrenades = true;
                    }
                }
            }
        }

        if (twoHandguns){
            for (int i = 0; i < cells.size(); i++){
                if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.HANDGUN)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.REVOLVER));
                }
                else if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.REVOLVER)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.HANDGUN));
                }
            }
        }
        if (twoShotguns){
            for (int i = 0; i < cells.size(); i++){
                if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.SHOTGUN)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.SAWED_OFF_SHOTGUN));
                }
                else if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.SAWED_OFF_SHOTGUN)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.SHOTGUN));
                }
            }
        }
        if (twoGrenades){
            for (int i = 0; i < cells.size(); i++){
                if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.M79)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.GRENADE));
                }
                else if (cells.get(i).getObjectCodeNumber() == Weapon.getWeaponCodeForType(WeaponType.GRENADE)){
                    cells.get(i).addImage(getImageForWeapon(WeaponType.M79));
                }
            }
        }

    }

    private CustomOnPanelImage getImageForWeaponOld(WeaponType weaponType){
        for (int i = 0; i < hiddenImages.size(); i++){
            if (hiddenImages.get(i).getType() == Weapon.getWeaponCodeForType(weaponType)){
                return hiddenImages.get(i);
            }
        }
        System.out.println("Image was not founded!!! ");
        return null;
        /*
        for (int i = 0; i < hiddenImages.size(); i++){
            if (hiddenImages.get(i).getType() == Weapon.getWeaponCodeForType(weaponType)){
                return hiddenImages.get(i);
            }
        }
        System.out.println("Image was not founded!!! ");
        return null;
         */
        //hiddenImages
    }

    private CustomOnPanelImage getImageForWeapon(WeaponType weaponType){
        for (int i = 0; i < hiddenCells.size(); i++){
            if (hiddenCells.get(i).getActualImage().getType() == Weapon.getWeaponCodeForType(weaponType)){
                return hiddenCells.get(i).getActualImage();
            }
        }
        System.out.println("Image was not founded!!! ");
        return null;
        /*
        for (int i = 0; i < hiddenImages.size(); i++){
            if (hiddenImages.get(i).getType() == Weapon.getWeaponCodeForType(weaponType)){
                return hiddenImages.get(i);
            }
        }
        System.out.println("Image was not founded!!! ");
        return null;
         */
        //hiddenImages
    }


    public WeaponType getActualWeapon() {
        return actualWeapon;
    }

    public void setActualWeapon(WeaponType actualWeapon) {
        this.actualWeapon = actualWeapon;
        for (int i = 0; i < images.size(); i++){
            if (images.get(i).getType() == Weapon.getWeaponCodeForType(actualWeapon)){
                actualArrayElementNumber = i;
                for (SingleCellWithImage cellWithWeapon : cells){
                    if (cellWithWeapon.getObjectCodeNumber() == (Weapon.getWeaponCodeForType(actualWeapon))){
                        secondaryFrame.setCenterPosition(cellWithWeapon.xPos+cellWithWeapon.width/2, cellWithWeapon.yPos+cellWithWeapon.height/2);
                    }
                }
            }
        }
    }

    public WeaponType getSelectedWeaponType() {
        for (SingleCellWithImage cell : cells){
            if (cell.isMouseOnCell(Program.engine.mouseX, Program.engine.mouseY)){
                return Weapon.getWeaponTypeForCode(cell.getObjectCodeNumber());
            }
        }
        return null;
    }


    public boolean hasCellInternalCells(){
        for (SingleCellWithImage cell : cells){
            if (cell.isMouseOnCell(Program.engine.mouseX, Program.engine.mouseY)){
                return cell.hasCellInternalCells();
            }
        }
        return false;
    }

    public void changeCellToSubcell(Person player){
        for (SingleCellWithImage cell : cells){
            if (cell.isMouseOnCell(Program.engine.mouseX, Program.engine.mouseY)){
                if (cell.hasCellInternalCells()){
                    arrowBlinkController.cellWasChanged(cell);
                    int oldImageNumber = cell.getObjectCodeNumber();
                    System.out.println("Image was changed from type " + cell.getActualImage().getType());
                    cell.changeImage();
                    System.out.println(" to " + cell.getActualImage().getType());
                    int newImageNumber = cell.getObjectCodeNumber();
                    changeImagesOnClosedFrame(oldImageNumber, newImageNumber);
                    Soldier soldier = (Soldier) player;
                    updateBulletsNumber(soldier.getPlayerBag());
                }
                break;
            }
        }
    }

    public boolean canCellBeChanged(){

        return arrowBlinkController.canActualSelectingCellBeChanged();
    }

    @Override
    public void update(){
        super.update();
        arrowBlinkController.update(Program.engine);
    }

    public void updateBulletsNumber(PlayerBag playerBag){
        for (SingleCellWithImage cell : cells){
            int weaponCode = cell.getObjectCodeNumber();
            int restAmmo = playerBag.getRestAmmoForWeapon(Weapon.getWeaponTypeForCode(weaponCode));
            int inBarrelAmmo = playerBag.getInBarrelAmmoForWeapon(Weapon.getWeaponTypeForCode(weaponCode));
            String newText = ("" + inBarrelAmmo + '/' + restAmmo);
            cell.getActualImage().setText(newText);
            System.out.println("Data updated for weapon " + Weapon.getWeaponTypeForCode(weaponCode) + " and set on " + newText);
        }
    }

    /*
    public boolean changeWeaponInActualSlot(){
        for (int i = 0; i < cells.size(); i++){
            if (cells.get(i).isMouseOnCell(Program.engine.mouseX, Program.engine.mouseY)){
                if (cells.get(i).canBeChanged()){
                    cells.get(i).changeImage();
                    return true;
                }
            }
        }
        return false;
    }

     */
    @Override
    public int getDefaultObjectCode() {
        return Weapon.getWeaponCodeForType(actualWeapon);
    }
/*
    @Override
    protected void
    OnClosedFrameNumber(PGraphics graphics){

        images.get(actualArrayElementNumber).drawWithoutNumber(graphics);

    }
    */

    private void changeImagesOnClosedFrame(int oldImageNumber, int newImageNumber) {
        int basicPos = 0;
        int reservePos = 0;
        for (int i = 0; i < images.size(); i++){
            if (images.get(i).getType() == oldImageNumber){
                basicPos = i;
            }
            else if (images.get(i).getType() == newImageNumber){
                reservePos = i;
            }
        }
        System.out.println("Old: " + basicPos + " res " + reservePos);
        SingleCellWithImage cellToBeChanged = null;
        int cellNumberToBeChanged = -1;
        for (int i = 0; i < cells.size(); i++){
            if (cells.get(i).getObjectCodeNumber() == newImageNumber){
                cellNumberToBeChanged = i;
            }
        }
        if (cellNumberToBeChanged >= 0) {
            cells.get(basicPos).setSwitchedOn(false);
            cells.get(cellNumberToBeChanged).getActualImage().updateDims();
            cells.get(cellNumberToBeChanged).setSwitchedOn(true);
            Collections.swap(cells, basicPos, cellNumberToBeChanged);
            //cells.set(basicPos, singleCellWithImage);
            //System.out.println("Cell was replaced");
        }

        Collections.swap(images, basicPos, reservePos);
    }

    private int[] getWeaponsList(ArrayList<WeaponType> weapons) {
        int [] weaponsList = new int[weapons.size()];
        for (int i = 0; i < weaponsList.length; i++){
            weaponsList[i] = FirearmsWeapon.getWeaponCodeForType(weapons.get(i));
        }
        return weaponsList;
    }


    @Override
    public void draw(PGraphics graphics, int drawMethod){
        super.draw(graphics,drawMethod);
        if (isFrameOpened()) {
            arrowBlinkController.draw(graphics);
        }
    }

    @Override
    public void closeFrame(){
        super.closeFrame();
        arrowBlinkController.frameClosing();
    }

    public ArrayList<WeaponType> getVisibleWeaponsOnPanel() {
        ArrayList <WeaponType> types = new ArrayList<>();
        for (SingleCellWithImage cell : cells){
            if (cell.visible){
                WeaponType type;
                if (!cell.hasCellInternalCells()){
                    type = FirearmsWeapon.getWeaponTypeForCode(cell.getObjectCodeNumber());
                    System.out.println("On panel in single cell " + type);

                }
                else {
                    type = FirearmsWeapon.getWeaponTypeForCode(cell.getActualImage().getType());
                    System.out.println("On panel in cell with subcells the actual cell has " + type);
                }
                if (type != null) types.add(type);
            }
        }
        return types;
    }
}
