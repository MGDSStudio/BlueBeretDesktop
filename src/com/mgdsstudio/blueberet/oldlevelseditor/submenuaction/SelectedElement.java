package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.RoundBox;
import com.mgdsstudio.blueberet.gameobjects.RoundElement;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_CheckBox;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.SingleGraphicElement;

class SelectedElement {
    private SingleGameElement selectedObject;
    private androidAndroidGUI_CheckBox relatedCheckBox;
    private byte type = 0;
    private String dataString;
    private SingleGraphicElement graphic;

    SelectedElement(SingleGameElement selectedObject){
        this.selectedObject = selectedObject;
        dataString = selectedObject.getStringData();
        try{
            System.out.println("Data string for this object is: " + dataString);
        }
        catch (Exception e){
            System.out.println("Data string for this object is not knew");
        }
        saveGraphic();
    }

    public SingleGraphicElement getGraphic(){
        return graphic;
    }

    private void saveGraphic() {
        if (selectedObject.getClass() == RoundBox.class){
            RoundBox roundBox = (RoundBox)selectedObject;
            if (roundBox.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) graphic = roundBox.getSprite();
        }
        else if (selectedObject instanceof RoundElement){
            RoundElement roundElement = (RoundElement) selectedObject;
            if (roundElement.getGraphicType() == SingleGraphicElement.STATIC_SPRITE_GRAPHIC_TYPE) graphic = roundElement.getSprite();
        }
        else if (selectedObject instanceof Enemy){
            Enemy enemy = (Enemy) selectedObject;
            graphic = enemy.getPersonAnimationController().getAnimationsList().get(0);
        }
        else if (selectedObject instanceof AbstractCollectable){
            AbstractCollectable collectableObject = (AbstractCollectable) selectedObject;
            graphic = collectableObject.getSprite();
        }
        else{
            if (selectedObject.getClass() == IndependentOnScreenStaticSprite.class){
                IndependentOnScreenStaticSprite independentOnScreenStaticSprite = (IndependentOnScreenStaticSprite) selectedObject;
                graphic = independentOnScreenStaticSprite.staticSprite;
            }
            else if (selectedObject.getClass() == IndependentOnScreenAnimation.class){
                IndependentOnScreenAnimation independentOnScreenAnimation = (IndependentOnScreenAnimation) selectedObject;
                System.out.println("It must be remade");
                graphic = independentOnScreenAnimation.spriteAnimation;
            }
        }
    }

    public String getDataString() {
        return dataString;
    }

    public void setRelatedCheckBox(androidAndroidGUI_CheckBox checkBox){
        relatedCheckBox = checkBox;
    }

    public SingleGameElement getSelectedObject() {
        return selectedObject;
    }

    public SingleGameElement getSelectedObjectByCheckBox(androidAndroidGUI_CheckBox activeCheckBox){
        if (relatedCheckBox.equals(activeCheckBox)){
            return selectedObject;
        }
        return null;
    }

    public androidAndroidGUI_CheckBox getCheckBox() {
        return relatedCheckBox;
    }




}
