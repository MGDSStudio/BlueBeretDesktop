package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.menusystem.menu.ContinueLastGameMenu;
import com.mgdsstudio.blueberet.menusystem.menu.MainMenu;
import com.mgdsstudio.texturepacker.TextureDecodeManager;
import java.util.Calendar;
import java.util.Date;

public abstract class DebugVersionCreatingMaster {
    public final static boolean USE_GRAPHIC_FROM_CACHE = true;

    public static void init(boolean debug){
        if (debug) {
            ContinueLastGameMenu.mustBeShopBlocked = false;
            MainMenu.mustBeEditorAndUserLevelsBlocked = false;
            TextureDecodeManager.useMgdsTextures = true;
        }
        else {
            ContinueLastGameMenu.mustBeShopBlocked = true;
            MainMenu.mustBeEditorAndUserLevelsBlocked = true;
            TextureDecodeManager.useMgdsTextures = true;
            //Date currentTime = Calendar.getInstance().getTime();
        }
        if (Program.OS == Program.ANDROID) updateAdInit();
    }

    private static void updateAdInit() {
        final int startAddYear = 2022;
        final int startAddMonth = 8; // Januar has number 0
        final int startAddDay = 14;

        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int actualMonth= Calendar.getInstance().get(Calendar.MONTH);
        int actualDay = Calendar.getInstance().get(Calendar.DATE);
        System.out.println("Now: " + actualYear + "year " + ". Day: " + actualDay  + "; Month: " + actualMonth);
        System.out.println("For ad it must be: " + startAddYear + "year " + ". Day: " +  startAddMonth  + "; Month: " + startAddDay);
        boolean addAd = false;
        if (actualYear > startAddYear) addAd = true;
        else if (actualYear == startAddYear){
            if (actualMonth > startAddMonth) addAd = true;
            else if (actualMonth == startAddMonth){
                if (actualDay >= startAddDay) addAd = true;
                else addAd = false;
            }
        }
        if (actualYear >= startAddYear){
            if (actualMonth >= startAddMonth){
                if (actualDay >= startAddDay){
                    addAd = true;
                }
            }
        }
        if (addAd){
            Program.withAdds = true;
            System.out.println("Add must be added");
        }
        else {
            System.out.println("Add must not be added");
            Program.withAdds = false;
        }

    }
}
