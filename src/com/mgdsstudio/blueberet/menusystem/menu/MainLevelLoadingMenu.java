package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.ImageZoneSimpleData;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import com.mgdsstudio.blueberet.menusystem.gui.NES_TextLabel;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressLoadMaster;
import com.mgdsstudio.blueberet.playerprogress.PlayerProgressSaveMaster;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class MainLevelLoadingMenu extends AbstractMenu{
    private String TIP_PREFIX = "TIP: ";
    private String GET_READY = "GET READY!";
    private ImageZoneSimpleData playerFace = GameMenusController.playerHead;
    private final String LEVEL_START_BUTTON_NAME = " ";
    private int centerX, centerY;
    private int centerWidth, centerHeight;
    private Timer minShowingTimeTimer;
    private final int MIN_TIME_TO_SHOW = 1500;
    private final PlayerProgressSaveMaster playerProgressSaveMaster;
    private int levelNumberToBeLoadedNext = 1;

    public MainLevelLoadingMenu(PApplet engine, PGraphics graphics, int levelNumberToBeLoadedNext) {
        type = MenuType.MAIN_LEVEL_LOADING;
        initLanguageSpecific();
        this.levelNumberToBeLoadedNext = levelNumberToBeLoadedNext;
        init(engine, graphics);
        playerProgressSaveMaster = new PlayerProgressSaveMaster();
        Program.actualRoundNumber = playerProgressSaveMaster.getNextZone();
        //int restLifes = playerProgressSaveMaster.getRestLifes();
        PlayerProgressLoadMaster master = new PlayerProgressLoadMaster();
        master.loadData();
        System.out.println();
        String restLifesText = "    x "+master.getRestLifes();
        addPlayerPicture(restLifesText);
        addGetReadyText(graphics);
        addTip(engine, graphics);
        minShowingTimeTimer = new Timer(MIN_TIME_TO_SHOW);
    }

    protected void initLanguageSpecific() {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            TIP_PREFIX = "СОВЕТ: ";
            GET_READY = "БУДЬ ГОТОВ!";
        } else {
            TIP_PREFIX = "TIP: ";
            GET_READY = "GET READY!";
        }
    }

    private void addPlayerPicture(String text) {
        NES_TextLabel restLifes = new NES_TextLabel(centerX, centerY, centerWidth, centerHeight, text, graphics);
        guiElements.add(restLifes);
    }

    private void addGetReadyText( PGraphics graphics) {
        NES_TextLabel restLifes = new NES_TextLabel(centerX, centerY+centerHeight*2, centerWidth, centerHeight, GET_READY, graphics);
        guiElements.add(restLifes);
    }

    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        centerX = engine.width/2;
        centerY = engine.height*4/13;
        centerWidth = engine.width/12;
        centerHeight = (int) (centerWidth/2.5f);
        NES_TextLabel levelStartButton = new NES_TextLabel(engine.width/2, engine.height/2, engine.width, engine.height, LEVEL_START_BUTTON_NAME, graphics);
        guiElements.add(levelStartButton);
    }

    private void addTip(PApplet engine, PGraphics graphics) {
        int lowerY = (int) (12f*engine.height/13f);
        String actualTip = createTip(engine);
        ArrayList <String> singleTippStrings = devideTippToSingleStringsBySpaces(actualTip, engine, graphics);
        String[] names = convertToArrayAndReverce(singleTippStrings);

        System.out.println("Strings: " + names.length + " names: " + names[names.length-1] + ": Lower: " + lowerY);
        fillGuiWithTextLabels(engine, lowerY, names, TO_UP);
        System.out.println("Last element: " + guiElements.get(guiElements.size()-1).getUpperY());
    }

    private String[] convertToArrayAndReverce(ArrayList<String> singleTippStrings) {
        String[] names = new String[singleTippStrings.size()];
        for (int i = 0; i < singleTippStrings.size(); i++){
            names[(names.length-1)-i] = singleTippStrings.get(i);
        }
        return names;
    }

    private ArrayList<String> devideTippToSingleStringsBySpaces(String tip, PApplet engine, PGraphics graphics) {
        final int stringMaxWidth = (int) (engine.width*0.95f);
        final float charWWidth = graphics.textWidth('_');
        final int maxChars = PApplet.floor(stringMaxWidth/charWWidth);
        ArrayList <String> singleTippStrings = new ArrayList<>();
        String [] words = tip.split(" ");
        System.out.println("Tip has " + words.length + " words");
        String toBeAdded = "";
        for (int i = 0; i < words.length; i++){
            if ((toBeAdded.length()+words[i].length())<maxChars){
                toBeAdded+=words[i];
                toBeAdded+=' ';
            }
            else {
                singleTippStrings.add(toBeAdded);
                toBeAdded = " " +words[i] + " ";
            }
        }
        singleTippStrings.add(toBeAdded);


        return singleTippStrings;
    }

    private ArrayList<String> devideTippToSingleStringsBySymbols(String tip, PApplet engine, PGraphics graphics) {
        final int stringMaxWidth = (int) (engine.width*0.95f);
        final float charWWidth = graphics.textWidth('_');
        final int maxChars = PApplet.floor(stringMaxWidth/charWWidth);
        ArrayList <String> singleTippStrings = new ArrayList<>();
        int tipWidth = (int) graphics.textWidth(tip);
        if (tipWidth < stringMaxWidth){
            singleTippStrings.add(tip);
        }
        else {
            System.out.println("Chars in single string " + maxChars + " in source: " + tip);
            String restString = ""+ tip;
            int lastSymbolNumber = 0;
            boolean ended = false;
            while (!ended){
                int restSize = restString.length();
                if (restSize > (maxChars-1)){
                    String firstPartString = restString.substring(0, maxChars-1);
                    singleTippStrings.add(firstPartString);
                    restString = restString.substring(maxChars);
                }
                else {
                    String firstPartString = restString.substring(0, restSize-1);
                    singleTippStrings.add(firstPartString);
                    ended = true;
                }
            }

        }
        System.out.println("*** Strings: ");
        for (String  string : singleTippStrings){
            System.out.println(string);
        }
        return singleTippStrings;
    }

    private String createTip(PApplet engine) {
        String tip = "";
        ArrayList <String> tipps = new ArrayList<>();
        if (Program.LANGUAGE == Program.RUSSIAN){
            fillWithRussianTipps(tipps);
        }
        else {
            fillWithEnglishTipps(tipps);
        }
        System.out.println("Tipps must be translated in RUSSIAN!!!");
        final int tippsNumber = tipps.size();
        int tippNumber = (int) engine.random(-1,tippsNumber);
        tip = TIP_PREFIX+tipps.get(tippNumber);
        return tip;
    }

    private void fillWithEnglishTipps(ArrayList <String> tipps){
        tipps.add("FOR MOVEMENT AND AIMING YOUR HAVE A CONTROL AREA AT THE LEFT BOTTOM CORNER OF THE DISPLAY.");
        tipps.add("YOU CAN AIM AND SHOOT ONLY IF YOU ARE STAYING OR GOING");
        tipps.add("MOVABLE OBJECTS CAN HIT AN ENEMY");
        tipps.add("WHEN THE CROSSHAIR IS YELLOW THE UNDER IT LAYING OBJECT CAN BE CRUSHED");
        tipps.add("WHEN THE CROSSHAIR IS GRAY THE UNDER IT LAYING OBJECT IS IMMORTAL");
        tipps.add("WHEN THE ENEMY GOT AN OBJECT YOU CAN RETURN THE OBJECT AFTER THE ENEMY WILL BE DEAD");
        tipps.add("WHEN YOU FALL ON THE GROUND IN FRONT OF AN OBJECT IT WILL BE THROWN AWAY");
        tipps.add("SHORT TAP ON THE ITEM FRAME TO USE THE SELECTED ITEM. LONG PRESS TO OPEN THE ITEMS LIST");
        tipps.add("TAP ON THE WEAPON FRAME TO OPEN THE WEAPONS LIST");
        tipps.add("IF YOU WANT TO THROW AN OBJECT OR AN ENEMY HIGH IN THE AIR YOU NEED TO STAY CLOSE TO IT AND PRESS THE BOOT BUTTON AND TOUCH THE CONTROL AREA IN THE UPPER PART");
        tipps.add("SLEEPING SNAKES AWAKE WHEN YOU ARE RUNNING, FALLING OR SHOOTING. DON'T AWAKE THEY. THEY ARE DANGEROUS");
        tipps.add("BEST WAY TO ATTACK A LIZARD IS TO APPROACH FROM THE BACK");
        tipps.add("DO NOT RUN, JUMP OR FALL NEAR SLEEPING BATS TOUCH - YOU CAN AWAKE THEM. ");
    }

    private void fillWithRussianTipps(ArrayList <String> tipps){
        tipps.add("ДЛЯ ПРИЦЕЛИВАНИЯ И ХОДЬБЫ ИСПОЛЬЗУЙТЕ ЗОНУ УПРАВЛЕНИЯ В ЛЕВОМ НИЖНЕМ УГЛУ ДИСПЛЕЯ ");
        tipps.add("ВЕСТИ СТРЕЛЬБУ ВОЗМОЖНО ТОЛЬКО СТОЯ ИЛИ ПРИ ХОДЬБЕ ");
        tipps.add("БЫСТРО ДВИЖУЩИЕСЯ ОБЪЕКТЫ МОГУТ НАНЕСТИ ПОВРЕЖДЕНИЯ ПРОТИВНИКАМ ");
        tipps.add("КОГДА ПРИЦЕЛ ЖЕЛТЫЙ, ПРЕДМЕТ ПОД НИМ МОЖЕТ БЫТЬ РАЗРУЖЕН ПРИ ВЫСТРЕЛЕ ");
        tipps.add("КОГДА ПРИЦЕЛ СЕРЫЙ, ПРЕДМЕТ ПОД НИМ НЕ МОЖЕТ БЫТЬ УНИЧТОЖЕН НИКАКИМ ОБРАЗОМ ");
        tipps.add("ЕСЛИ ПРОТИВНИК ПОДОБРАЛ ПРЕДМЕТ, ТО ВЫ МОЖЕТЕ ВЕРНУТЬ ЕГО, УНИЧТОЖИВ ПРОТИВНИКА ");
        tipps.add("КОГДА ВЫ ПРИЗЕМЛЯЕТЕСЬ ВБЛИЗИ КАКОГО-ЛИБО ПРЕДМЕТА, ТО ОН МОЖЕТ БЫТЬ ОТБРОШЕН ПРОЧЬ ");
        tipps.add("КРАТКОВРЕМЕННОЕ НАЖАТИЕ НА ПРЕДМЕТ В РАМКЕ ЧТОБЫ ЕГО ИСПОЛЬЗОВАТЬ. ДЛИТЕЛЬНОЕ НАЖАТИЕ ОТКРЫВАЕТ ИНВЕНТАРЬ. ");
        tipps.add("ДЛИТЕЛЬНОЕ НАЖАТИЕ НА ИКОНКУ С ОРУЖИЕМ ОТКРЫВАЕТ ПЕРЕЧЕНЬ ОРУЖИЯ ИГРОКА. ");
        tipps.add("ЕСЛИ ВЫ ХОТИТЕ ПОДБРОСИТЬ ОБЪЕКТ ИЛИ ПРОТИВНИКА ВЫСОКО В ВОЗДУХ, ВАМ НУЖНО ПОДОЙТИ К НЕМУ И НАЖАТЬ КНОПКУ С БОТИНКОМ ОДНОВРЕМЕННО НАЖИМАЯ НА ВЕРХНЮЮ ЧАСТЬ ЗОНЫ ПРИЦЕЛИВАНИЯ.");
        tipps.add("СПЯЩУЮ ЗМЕЮ МОЖНО РАЗБУДИТЬ, ЕСЛИ ВЫ БЕЖИТЕ, ПАДАЕТЕ ИЛИ СТРЕЛЯЕТЕ. ЛУЧШЕ ЭТОГО НЕ ДЕЛАТЬ, ТАК КАК ОНИ ОПАСНЫ");
        tipps.add("ЛУЧШИЙ СПОСОБ ДЛЯ АТАКИ ЗМЕЙ ЭТО ПОДКРАСТСЯ СО СПИНЫ");
        tipps.add("НЕ БЕГАЙ, НЕ ПРЫГАЙ И НЕ ИЗДАВАЙ НИКАКИХ ЗВУКОВ ВБЛИЗИ СПЯЩИХ ЛЕТУЧИХ МЫШЕЙ ЧТОБЫ НЕ РАЗБУДИТЬ ИХ");
    }

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        if (minShowingTimeTimer.isTime()){
            Program.actualRoundNumber = levelNumberToBeLoadedNext;
            gameMenusController.loadLevel(false, false, ExternalRoundDataFileController.MAIN_LEVELS);
            System.out.println("Try to load level: " + levelNumberToBeLoadedNext);
        }
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.PRESSED) {

            }
        }
        //System.out.println("Updating");
    }

    public void draw(PGraphics graphics){
        super.draw(graphics);
        drawPlayerHead(graphics);
    }

    private void drawPlayerHead(PGraphics graphics) {
        if (graphics != null) {
            graphics.pushStyle();
            graphics.imageMode(PApplet.CENTER);
            graphics.image(GameMenusController.sourceFile.getImage(),  centerX-centerWidth/2, centerY, graphics.textSize*1.3f, graphics.textSize*1.3f, playerFace.leftX, playerFace.upperY, playerFace.rightX, playerFace.lowerY);
            graphics.popStyle();
        }
    }
}
