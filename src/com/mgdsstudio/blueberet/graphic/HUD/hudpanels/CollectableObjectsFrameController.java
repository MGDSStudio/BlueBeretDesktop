package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.loading.PlayerBag;
import com.mgdsstudio.blueberet.graphic.CustomOnPanelImage;
import com.mgdsstudio.blueberet.graphic.HUD.HUD_GraphicData;
import com.mgdsstudio.blueberet.graphic.Tileset;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class CollectableObjectsFrameController extends OpenableFrameController{
    private int actualType;
    private boolean blocked;
    private final int maxOpenedWidth, maxOpenedHeight;
    private PlayerBag playerBag;


    CollectableObjectsFrameController (Tileset tileset, EightPartsFrameImage frame, int maxOpenedWidth, int maxOpenedHeight, PlayerBag playerBag, int actualSelected){
        System.out.println("Actual selected is: " + actualSelected);
        this.actualType = actualSelected;
        cells = new ArrayList<>(4);
        this.mainFrame = frame;
        this.maxOpenedWidth = maxOpenedWidth;
        this.maxOpenedHeight = maxOpenedHeight;
        this.playerBag = playerBag;
        images = new ArrayList<>(4);
        boolean openingBlocked = true;
        if (playerBag.getSelectableInBagObjectsTypesNumber() > 1) openingBlocked = false;
        System.out.println("For collectable it is blocked " + openingBlocked + " with size: " + playerBag.getSelectableInBagObjectsTypesNumber());
        createFrameDimensionsData(tileset, openingBlocked, actualSelected);

    }

    private void createFrameDimensionsData(Tileset tileset, boolean openingBlocked, int actualSelected){
        frameDimensionChanger = new FrameDimensionChanger(mainFrame, FrameDimensionChanger.NORMAL_TIME_TO_OPEN_FRAME,4,maxOpenedWidth, maxOpenedHeight,FrameDimensionChanger.FROM_RIGHT_TO_LEFT, openingBlocked);
        initGraphic(tileset, maxOpenedWidth, maxOpenedHeight, playerBag);
        int basicSourceHeightForSimpleFrames = (int) (UpperPanel.HEIGHT*0.35f);
        Vec2 posForSelectingFrame = getCenterForSelectingFrame(actualSelected);
        initSelectingFrame(posForSelectingFrame, basicSourceHeightForSimpleFrames, HUD_GraphicData.basicRectForSimpleFramesWithRoundedCorners);
        setActualObject(actualType);
    }

    protected void initGraphic(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, PlayerBag playerBag){
        int additionalGap = (int) (mainFrame.getWidth()*0.05f);
        //int additionalGap = (int) (mainFrame.getWidth()*0.1f);
        int height = mainFrame.getHeight()-additionalGap ;
        int xPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().x+ mainFrame.getWidth()/2);
        int yPosInFrameCenter = (int) (mainFrame.getLeftUpperCorner().y+ mainFrame.getHeight()/2);
        for (int i = 0; i < playerBag.getSelectableInBagObjects().size(); i++){
            if (playerBag.getSelectableInBagObjects().get(i).getNumber() >0){
                images.add(new CustomOnPanelImage(playerBag.getSelectableInBagObjects().get(i).getType(), tileset, mainFrame.getWidth()-additionalGap, height, xPosInFrameCenter, yPosInFrameCenter));
            }
        }
        initCellsGraphicForObjects(tileset,  maxOpenedWidth,  maxOpenedHeight, playerBag, additionalGap);
    }

    private void initCellsGraphicForObjects(Tileset tileset, int maxOpenedWidth, int maxOpenedHeight, PlayerBag playerBag, int additionalGap) {
        int widthForObjectsInBag = (maxOpenedWidth- mainFrame.getWidth())/((playerBag.getSelectableInBagObjectsTypesNumber()+1))-additionalGap*3;
        int height = (int) ((maxOpenedHeight-additionalGap)*0.6f);
        int number = 0;
        for (int i = 0; i < playerBag.getSelectableInBagObjects().size(); i++){
            try {
                if (playerBag.getSelectableInBagObjects().get(i).getNumber() > 0) {
                    String textToBeAdded = REST_TEXT+ playerBag.getSelectableInBagObjects().get(i).getNumber();
                    CustomOnPanelImage onPanelImage = new CustomOnPanelImage(playerBag.getSelectableInBagObjects().get(i).getType(), tileset, widthForObjectsInBag, height, 200, (int) (mainFrame.getLeftUpperCorner().y + mainFrame.getHeight() / 2), textToBeAdded);
                    System.out.println("Number " + playerBag.getSelectableInBagObjects().get(i).getType() + " image is null " + (onPanelImage == null));
                    SingleCellWithImage singleCellWithObject = new SingleCellWithImage(this, onPanelImage, 1 + number, 1, 4, 1);
                    cells.add(singleCellWithObject);
                    number++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Init!");
    }

    public void setActualObject(int type) {
        this.actualType = type;
        for (int i = 0; i < images.size(); i++){
            if (images.get(i).getType() == type){
                actualArrayElementNumber = i;
                for (SingleCellWithImage cellWithWeapon : cells){
                    if (cellWithWeapon.getObjectCodeNumber() == (type)){
                        secondaryFrame.setCenterPosition(cellWithWeapon.xPos+cellWithWeapon.width/2, cellWithWeapon.yPos+cellWithWeapon.height/2);
                    }
                }
            }

        }
    }

    public int getActualType() {
        return actualType;
    }

    @Override
    public int getDefaultObjectCode() {
        return actualType;
    }

    public void addNewObjectToFrame(int type, int number){
        boolean alreadyExists = false;
        for (int i = cells.size()-1; i >= 0; i--) {
            if (cells.get(i).getObjectCodeNumber() == type) {
                alreadyExists = true;
            }
        }
        if (cells.size()==0) this.actualType = type;
        if (!alreadyExists){
            System.out.println("We haven't an another object for this type");
            images.clear();
            cells.clear();
            //public CustomOnPanelImage(int code, Tileset tileset, int width, int height, int posX, int posY){
            if (HeadsUpDisplay.mainGraphicTileset == null) System.out.println(" *** Graphic tileset is null *** ");//Tileset tileset, boolean openingBlocked, int actualSelected){
            int newSelectedType = type;
            if (actualType>=0) newSelectedType = actualType;
            createFrameDimensionsData(HeadsUpDisplay.mainGraphicTileset, true, newSelectedType);
            int additionalGap = (int) (mainFrame.getWidth()*0.1f);
        }
        else System.out.println("This object already exists");
        if (cells.size()>1) frameDimensionChanger.setOpeningBlocked(false);

    }



    public void updateNumberForObjectsOnOpenedFrame(int type, int number){
        for (int i = cells.size()-1; i >= 0; i--){
            if (cells.get(i).getObjectCodeNumber() == type){
                cells.get(i).getActualImage().setText(REST_TEXT + number);
                //images.get(i).setText(REST_TEXT + number);
                System.out.println("Type " + type + " has changed his data to " + number );
                break;
            }
        }
    }


    public void confiscateObjectFromFrame(int type){
        for (int i = cells.size()-1; i >= 0; i--){
            if (cells.get(i).getObjectCodeNumber() == type){
                cells.remove(i);
                System.out.println( i+ " cell was deleted");
                break;
            }
        }
        for (int i = images.size()-1; i >= 0; i--){
            if (images.get(i).getType() == type){
                images.remove(i);
                System.out.println( i+ " image was deleted");
                break;
            }
        }
        int newCodeForChangedObject = -1;
        if (cells.size() > 0){
            newCodeForChangedObject = 0;
            setActualObject(cells.get(0).getObjectCodeNumber());
            actualArrayElementNumber = newCodeForChangedObject;
        }
        else {
            setActualObject(newCodeForChangedObject);

        }
        if (cells.size() <= 1) frameDimensionChanger.setOpeningBlocked(true);
    }

    public boolean canObjectFrameBeOpened(){
        return !frameDimensionChanger.isOpeningBlocked();
    }
}
