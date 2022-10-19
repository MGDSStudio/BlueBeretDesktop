package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.graphic.howtoplayanimation.HowToPlayScreenGraphic;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

abstract class AbstractHowToPlayMenu extends AbstractMenuWithTitleAndBackButton implements HowToPlaySubmenus {
    protected String[] howToPlayFullText;
    protected TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;
    protected HowToPlayScreenGraphic howToPlayScreenGraphic;
    //private final SoundInGameController soundInGameController;
    //protected String backButtonName;

    protected AbstractHowToPlayMenu(PApplet engine, PGraphics graphics) {
        type = MenuType.HOW_TO_PLAY_FROM_OPTIONS;
        initLanguageSpecific();
        init(engine, graphics);
        //soundInGameController = new SoundInGameController(2);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- КАК ИГРАТЬ -";
        }
        else{
            titleName = "- HOW TO PLAY -";
        }
    }
    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        initText();
        Rectangular rectangular = new Rectangular(graphics.width/2, (graphics.height/3), frame.getWidth(), frame.getHeight()/3f);
        howToPlayScreenGraphic = new HowToPlayScreenGraphic(engine, actualPage, rectangular);
        addPrevNextButtons(engine);
    }


    private void initText() {
        if (actualPage<0) actualPage = 0;
        howToPlayFullText = new String[13];
        if (actualPage > howToPlayFullText.length-1) actualPage = howToPlayFullText.length-1;
        if (Program.LANGUAGE == Program.ENGLISH){
            loadDefaultTexts();
        }
        else if (Program.LANGUAGE == Program.RUSSIAN){
            loadRussianTexts();
        }
        System.out.println("How to play must be translated in RUSSIAN!!!");
        Rectangular rectangular = new Rectangular(graphics.width/2, 7.3f*graphics.height/12, graphics.width*0.7f, frame.getHeight()/2.6f);
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController( howToPlayFullText[actualPage], rectangular, 17, 2000, graphics.textFont, graphics, true);
        //textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController( howToPlayFullText[actualPage], rectangular, 9, 2000, graphics.textFont, graphics, true);
        //
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*2f);
        pages = howToPlayFullText.length;
        //actualPage = 0;
    }

    private void loadRussianTexts() {
        howToPlayFullText [RUN] = "НАЖМИ НА ПРАВУЮ ЗОНУ СТИКА (ПРАВУЮ ЗОНУ БЕГА) ЧТОБЫ БЕЖАТЬ ВПРАВО. НАЖМИ НА ЛЕВУЮ ЗОНУ СТИКА (ЛЕВУЮ ЗОНУ БЕГА) ЧТОБЫ БЕЖАТЬ ВЛЕВО. ";
        howToPlayFullText [GO] = "НАЖМИ НА ВТОРУЮ СПРАВА ЗОНУ СТИКА (ПРАВУЮ ЗОНУ ХОТЬБЫ) ЧТОБЫ ИДТИ ВПРАВО. НАЖМИ НА ВТОРУЮ СЛЕВА ЗОНУ СТИКА (ЛЕВУЮ ЗОНУ ХОТЬБЫ) ЧТОБЫ ИДТИ ВЛЕВО. ";
        howToPlayFullText [SHOOT] = "НАЖМИ НА ПРАВУЮ НИЖНЮЮ КНОПКУ СО ВСПЫШКОЙ ЧТОБЫ ВЫСТРЕЛИТЬ. ";
        howToPlayFullText [AIM] = "НАЖМИ И ДВИГАЙ БОЛЬШИМ ПАЛЬЦЕМ ЛЕВОЙ РУКИ ПО ЦЕНТРАЛЬНОЙ ЗОНЕ СТИКА ЧТОБЫ ПРИЦЕЛИТЬСЯ. УБЕДИТЕСЬ, ЧТО В ОРУЖИИ ДОСТАТОЧНО ПАТРОНОВ. В ТОЧКЕ ПРИЦЕЛИВАНИЯ СТАБИЛИЗИРУЕТСЯ ПРИЦЕЛ В ТЕЧЕНИИ НЕСКОЛЬКИХ МГНОВЕНИЙ. ПРИЦЕЛИВАНИЕ МОЖНО СОВМЕЩАТЬ С ХОТЬБОЙ И СО СТРЕЛЬБОЙ.  ";
        howToPlayFullText [JUMP] = "НАЖМИ НА КНОПКУ СО СТРЕЛКОЙ, НАПРАВЛЕННОЙ ВВЕРХ, ЧТОБЫ ПОДПРЫГНУТЬ. ";
        howToPlayFullText [RELOAD] = "НАЖМИ НА КНОПКУ R ЧТОБЫ ПЕРЕЗАРЯДИТЬ ОРУЖИЕ. УБЕДИТЕСЬ, ЧТО В ОРУЖИИ ДОСТАТОЧНО ПАТРОНОВ.";
        howToPlayFullText [KICK] = "НАЖМИ НА КНОПКУ С БОТИНКОМ ЧТОБЫ ПНУТЬ ПРОТИВНИКА ИЛИ ПРЕДМЕТ ПЕРЕД СОБОЙ. ПИНОК МОЖНО СОВМЕЩАТЬ С ПРИЦЕЛИВАНИЕМ, ЕСЛИ ТРЕБУЕТСЯ ЗАПУСТИТЬ ПРОТИВНИКА ВЫШЕ ИЛИ НИЖЕ ПОСЛЕ АТАКИ. ";
        howToPlayFullText [PORTAL] = "НАЖМИ НА КНОПКУ С ЧЕЛОВЕКОМ, ВБЕГАЮЩИМ В ПОРТАЛ И ВЫБЕГАЮЩИМ ИЗ НЕГО, ЧТОБЫ ИСПОЛЬЗОВАТЬ ПОРТАЛ ИЛИ ПЕРЕБРАТЬСЯ НА СЛЕДУЮЩИЙ УРОВЕНЬ. ";
        howToPlayFullText [USE_OBJECT] = "НАЖМИТЕ НА ИКОНКУ С ПРЕДМЕТОМ НА ВЕРХНЕЙ ПАНЕЛИ ЧТОБЫ ИМ ВОСПОЛЬЗОВАТЬСЯ. ";
        howToPlayFullText [OPEN_ITEMS_LIST] = "НАЖМИТЕ  НА  ИКОНКУ С ПРЕДМЕТОМ НА ВЕРХНЕЙ ПАНЕЛИ И УДЕРЖИВАЙТЕ ЕЕ НАЖАТОЙ КАК МИНИМУМ 1 CEKУHДУ ,ЧTOБЫ ОТКРЫТЬ ИНВЕНТАРЬ. ДАЛЕЕ ВЫ МОЖЕТЕ ВЫБРАТЬ В ИНВЕНТАРЕ ДРУГОЙ ПРЕДМЕТ НАЖАТИЕМ НА НЕГО, ЧТОБЫ ПЕРЕМЕСТИТЬ ЕГО НА ВЕРХНЮЮ ПАНЕЛЬ, ЧТОБЫ ВОСПОЛЬЗОВАТЬСЯ ИМ В ПРОЦЕССЕ ИГРЫ. ";
        howToPlayFullText [CHANGE_CELL] = "ЕСЛИ ВЫ ИМЕЕТЕ БОЛЬШЕ ОДНОГО ОРУЖИЯ ОДНОГО И ТОГО ЖЕ ТИПА, ТО ОДНО ИЗ НИХ ОКАЖЕТСЯ СКРЫТО. ОТКРОЙТЕ ПЕРЕЧЕНЬ ОРУЖИЯ ДОЛГИМ КЛИКОМ ПО ИКОНКЕ С ОРУЖИЕМ И ВЫ ОБРАРУЖИТЕ ПОД ОДНИМ ИЗ ОРУЖИЙ БЕЛУЮ СТРЕЛКУ. ДОЛГИМ НАЖАТИЕМ НА ОРУЖИЕ ВЫ ЗАМЕНИТЕ ОРУЖИЕ В ЭТОЙ ИКОНКЕ НА ДРУГОЕ ОРУЖИЕ ЭТОГО ЖЕ ТИПА. ЭТО КАСАЕТСЯ СВЯЗОК - РЕВОЛЬВЕР +ПИСТОЛЕТ, ОБРЕЗ+ ДРОБОВИК, ГРАНАТА+ ГРАНАТОМЕТ. ";
        //howToPlayFullText [CHANGE_CELL] = "When the weapon frame complete opened you can see a small arrow under the weapon. Press the icon with the weapon for at least 1 second to change the weapon in the cell to the weapon on the subcell. Than you can select a new weapon to use it. IT IS USEFUL FOR NEXT PAIRS OF WEAPONS: REVOLVER +HANDGUN, SAWED- OFF SHOTGUN +SHOTGUN, HAND GRENADE +GRENADE LAUNCHER. ";
        //
        howToPlayFullText [OPEN_WEAPONS_LIST] = "нажмите на иконку с оружием на верхней панели и удерживайте ее нажатой как минимум 1 ceкунду ,чтoбы открыть перечень оружия. далее вы можете выбрать другое оружие нажатием на него. ";
        /*
        howToPlayFullText [OPEN_ITEMS_LIST] = "НАЖМИТЕ  НА  ИКОНКУ С ПРЕДМЕТОМ НА ВЕРХНЕЙ ПАНЕЛИ И УДЕРЖИВАЙТЕ ЕЕ НАЖАТОЙ КАК МИНИМУМ 1 CEKУHДУ ЧTOБЫ ОТКРЫТЬ ИНВЕНТАРЬ. ДАЛЕЕ ВЫ МОЖЕТЕ ВЫБРАТЬ В ИНВЕНТАРЕ ДРУГОЙ ПРЕДМЕТ НАЖАТИЕМ НА НЕГО, ЧТОБЫ ПЕРЕМЕСТИТЬ ЕГО НА ВЕРХНЮЮ ПАНЕЛЬ, ЧТОБЫ ВОСПОЛЬЗОВАТЬСЯ ИМ В ПРОЦЕССЕ ИГРЫ. ";
        howToPlayFullText [OPEN_WEAPONS_LIST] = "НАЖМИТЕ НА ИКОНКУ С ОРУЖИЕМ НА ВЕРХНЕЙ ПАНЕЛИ И УДЕРЖИВАЙТЕ ЕЕ НАЖАТОЙ КАК МИНИМУМ 1 CEKУHДУ ЧTOБЫ ОТКРЫТЬ ПЕРЕЧЕНЬ ОРУЖИЯ. ДАЛЕЕ ВЫ МОЖЕТЕ ВЫБРАТЬ В ДРУГОЕ ОРУЖИЕ НАЖАТИЕМ НА НЕГО. ";

         */

        howToPlayFullText [GOOD_LUCK] = " - ЖЕЛАЕМ УДАЧИ! - ";
    }

    private void loadDefaultTexts() {
        howToPlayFullText [RUN] = "Touch the right stick zone |right run zone| to run right. Touch the left stick zone |left run zone| to run left.";
        howToPlayFullText [GO] = "Touch the second from the left stick zone |left movement zone| to go left. Touch the second from the right stick zone |right movement zone| to go right.";
        howToPlayFullText [SHOOT] = "Press the button with the flash to shoot.";
        howToPlayFullText [AIM] = "Touch and move your finger on the central stick zone to aim. Be sure that you have enough bullets in the actual weapon. You can also go and aim or aim and shoot";
        howToPlayFullText [JUMP] = "Press the button with the arrow that points upwards to jump.";
        howToPlayFullText [RELOAD] = "Press R button to reload the weapon.";
        howToPlayFullText [KICK] = "Press the button with the boot to kick enemies and objects in front of the player. You can also aim and kick enemies if you need to launch enemy above or below the character";
        howToPlayFullText [PORTAL] = "Press the button with the man in the portal to use a portal or to be transferred to the next level.";
        howToPlayFullText [USE_OBJECT] = "Press the icon with an object on the upper panel to use it.";
        howToPlayFullText [OPEN_ITEMS_LIST] = "Press the icon with the object on the upper panel for at least 1 second to open your inventory list. Than you can select an item you want to set on the panel for rapidly using in the game.";
        howToPlayFullText [OPEN_WEAPONS_LIST] = "Press the icon with a weapon on the upper panel for at least 1 second to open the weapons bag. Than you can select a new weapon to use it.";
        //howToPlayFullText [CHANGE_CELL] = "When the weapon frame complete opened you can see a small arrow under the weapon. Press the icon with the weapon for at least 1 second to change the weapon in the cell to the weapon on the subcell. Than you can select a new weapon to use it. IT IS USEFUL FOR NEXT PAIRS OF WEAPONS: REVOLVER +HANDGUN, SAWED- OFF SHOTGUN +SHOTGUN, HAND GRENADE +GRENADE LAUNCHER. ";
        howToPlayFullText [CHANGE_CELL] = "When the weapon frame complete opened you can see a small arrow under the weapon. Press the icon with the weapon for at least 1 second to change the weapon in the cell to the weapon on the subcell. Than you can select a new weapon to use it. IT IS USEFUL FOR NEXT PAIRS OF WEAPONS: REVOLVER +HANDGUN, SAWED- OFF SHOTGUN +SHOTGUN, HAND GRENADE +GRENADE LAUNCHER. ";
        howToPlayFullText [GOOD_LUCK] = " - GOOD  LUCK! - ";
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED && element.getClass() == NES_ButtonWithCursor.class) {
                //if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    backPressed(gameMenusController);
                //}
            }
        }
        textInSimpleFrameDrawingController.update();
        howToPlayScreenGraphic.update();
    }



    @Override
    protected void setPrevPage() {
        System.out.println("Prev page. Actual page: " + actualPage);
        setNewPageData();
    }



    @Override
    protected void setNextPage() {
        System.out.println("Next page. Actual page: " + actualPage);
        setNewPageData();
    }

    private void setNewPageData(){
        textInSimpleFrameDrawingController.setNewText(howToPlayFullText[actualPage], true);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*2f);
        removePrevAndNextButtons();
        addPrevNextButtons(null);
        howToPlayScreenGraphic.setNewPage(actualPage);
    }

    @Override
    public void draw(PGraphics graphic){
        super.draw(graphic);
        textInSimpleFrameDrawingController.draw(graphic);
        howToPlayScreenGraphic.draw(graphic);
    }
}
