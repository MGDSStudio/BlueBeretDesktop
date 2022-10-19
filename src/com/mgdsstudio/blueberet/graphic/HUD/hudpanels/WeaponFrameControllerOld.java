package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.graphic.CustomOnPanelImage;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.FirearmsWeapon;
import com.mgdsstudio.blueberet.weapon.Weapon;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class WeaponFrameControllerOld extends OpenableFrameController{

    private WeaponType actualWeapon;
    private final int weaponsAlongX = 5;

    private boolean withSubCells;

    private boolean [] cellsMask;
    private ArrayList <WeaponType> weaponsOnUpperPanel;
    private ArrayList <WeaponType> weaponsOnSubPage;

    WeaponFrameControllerOld (Tileset tileset, EightPartsFrameImage frame, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeapons, ArrayList<WeaponType> weaponsOnUpperPanel,  int actualWeapon, PlayerBag playerBag){

        fillObjectsMask(playersWeapons);
        cells = new ArrayList<>(playersWeapons.size());
        this.mainFrame = frame;
        images = new ArrayList<>(playersWeapons.size());
        boolean openingBlocked = true;
        if (playersWeapons.size()>1) openingBlocked = false;
        frameDimensionChanger = new FrameDimensionChanger(frame, FrameDimensionChanger.NORMAL_TIME_TO_OPEN_FRAME,4,maxOpenedWidth, maxOpenedHeight,FrameDimensionChanger.FROM_RIGHT_TO_LEFT, openingBlocked);
        initGraphic(tileset, maxOpenedWidth, maxOpenedHeight, playersWeapons, playerBag);
        int basicSourceHeightForSimpleFrames = (int) (UpperPanel.HEIGHT*0.35f);
        Vec2 posForSelectingFrame = getCenterForSelectingFrame(actualWeapon);
        initSelectingFrame(posForSelectingFrame, basicSourceHeightForSimpleFrames, HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners);
        setActualWeapon(Weapon.getWeaponTypeForCode(actualWeapon));
    }

    private void fillObjectsMask(ArrayList<WeaponType> playersWeapons) {
        cellsMask = new boolean[playersWeapons.size()];
        for (int i = 0; i < playersWeapons.size(); i++){
            cellsMask[i] = false;
        }
        withSubCells = hasSubCells(playersWeapons);
        if (withSubCells){
            System.out.println("This weapons list has sub cells");
            boolean handgun = false;
            boolean revolver = false;
            for (WeaponType type: playersWeapons){
                if (type == WeaponType.REVOLVER) revolver = true;
                if (type == WeaponType.HANDGUN) handgun = true;
            }
            if (handgun && revolver){
                cellsMask[0] = true;
            }
            boolean shotgun = false;
            boolean soShotgun = false;
            for (WeaponType type: playersWeapons){
                if (type == WeaponType.SHOTGUN) shotgun = true;
                if (type == WeaponType.SAWED_OFF_SHOTGUN) soShotgun = true;
            }
            if (shotgun && soShotgun){
                cellsMask[1] = true;
            }
        }
        System.out.print("Cells mask: ");
        for (int i = 0; i < cellsMask.length; i++){
            System.out.println(cellsMask[i] + ", ");
        }
        System.out.println();
    }
    private boolean hasSubCells(ArrayList<WeaponType> playersWeapons) {
        for (int i = 0 ; i < playersWeapons.size(); i++){
            if (playersWeapons.get(i).equals(WeaponType.HANDGUN)){
                for (int j = 0 ; j < playersWeapons.size(); j++){
                    if (playersWeapons.get(j).equals(WeaponType.REVOLVER)){
                        return true;
                    }
                }
            }
            else if (playersWeapons.get(i).equals(WeaponType.REVOLVER)){
                for (int j = 0 ; j < playersWeapons.size(); j++){
                    if (playersWeapons.get(j).equals(WeaponType.HANDGUN)){
                        return true;
                    }
                }
            }
            if (playersWeapons.get(i).equals(WeaponType.SHOTGUN)){
                for (int j = 0 ; j < playersWeapons.size(); j++){
                    if (playersWeapons.get(j).equals(WeaponType.SAWED_OFF_SHOTGUN)){
                        return true;
                    }
                }
            }
            if (playersWeapons.get(i).equals(WeaponType.SAWED_OFF_SHOTGUN)){
                for (int j = 0 ; j < playersWeapons.size(); j++){
                    if (playersWeapons.get(j).equals(WeaponType.SHOTGUN)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void initGraphic(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeapons,  PlayerBag playerBag){
        int additionalGap = (int) (mainFrame.getWidth()*0.1f);
        int height = mainFrame.getHeight()-additionalGap ;
        int xPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().x+ mainFrame.getWidth()/2);
        int yPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2);
        int [] weaponsList = getWeaponsList(playersWeapons);
        for (int i = 0; i < weaponsList.length; i++){
            images.add(new CustomOnPanelImage(weaponsList[i], tileset, mainFrame.getWidth()-additionalGap, height, xPosInFrameCenter, yPosInFrameCenter));

        }
        System.out.println("Created " + weaponsList.length + " cells with weapons");
        initCellsGraphicForWeapons(tileset,  maxOpenedWidth,  maxOpenedHeight, playersWeapons, additionalGap, playerBag);
    }

    private int[] getWeaponsList(ArrayList<WeaponType> playersWeapons) {
        int [] weaponsList = new int[playersWeapons.size()];
        for (int i = 0; i < weaponsList.length; i++){
            weaponsList[i] = FirearmsWeapon.getWeaponCodeForType(playersWeapons.get(i));
        }
        return weaponsList;
    }


    private void initCellsGraphicForWeapons(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, ArrayList<WeaponType> playersWeapons, int additionalGap,  PlayerBag playerBag) {
        int widthForWeaponsInBag = (maxOpenedWidth- mainFrame.getWidth())/((weaponsAlongX+1))-additionalGap;
        int height = (int) ((maxOpenedHeight-additionalGap)*0.6f);
        for (int i = 0; i < playersWeapons.size(); i++){
            String textStringToBeSet = REST_TEXT;
            CustomOnPanelImage weaponOnPanelImage = new CustomOnPanelImage(Weapon.getWeaponCodeForType(playersWeapons.get(i)), tileset, widthForWeaponsInBag, height, 200, (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2), textStringToBeSet);
            SingleCellWithImage singleCellWithWeapon = new SingleCellWithImage(this, weaponOnPanelImage, 1+i, 1, 4, 1 );
            cells.add(singleCellWithWeapon);
        }
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
                System.out.println("Cell " + Weapon.getWeaponTypeForCode(cell.getObjectCodeNumber()) + " was selected");
                return Weapon.getWeaponTypeForCode(cell.getObjectCodeNumber());
            }
        }
        return null;
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

    @Override
    public int getDefaultObjectCode() {
        return Weapon.getWeaponCodeForType(actualWeapon);
    }
}
