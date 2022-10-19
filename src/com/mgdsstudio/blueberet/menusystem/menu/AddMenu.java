package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import processing.core.PApplet;

abstract class AddMenu extends AbstractMenu{
    protected MenuType nextMenu;
    protected boolean addProgress = false;

    protected AddMenu(PApplet engine, Object userValue) {
        this.type = MenuType.REWARDED_ADDS_MENU;
        if (userValue.getClass() == MenuType.class){
            nextMenu = (MenuType) userValue;
        }
        if (Program.withAdds && Program.OS == Program.ANDROID){
            System.out.println("Start to upload add");
            initAdd(engine);
            addProgress =  true;
        }
    }

    protected abstract void initAdd(PApplet engine);



}
