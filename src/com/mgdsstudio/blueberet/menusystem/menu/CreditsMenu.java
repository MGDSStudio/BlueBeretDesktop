package com.mgdsstudio.blueberet.menusystem.menu;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.graphic.HUD.hudpanels.TextInSimpleFrameDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.menusystem.GameMenusController;
import com.mgdsstudio.blueberet.menusystem.MenuType;
import com.mgdsstudio.blueberet.menusystem.gui.NES_ButtonWithCursor;
import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

public class CreditsMenu extends AbstractMenuWithTitleAndBackButton {
    private final int AUTHORS_PER_PAGE = 3;

    private String onActualScreenAuthors;
    private ArrayList<String> fullAuthorsList, actualAuthorsList;
    //private int startY;
    private final PApplet engine;
    //private int stringsOnFrameNumber;
    private TextInSimpleFrameDrawingController textInSimpleFrameDrawingController;

    public CreditsMenu(PApplet engine, PGraphics graphics) {
        this.engine = engine;
        type = MenuType.CREDITS;
        initLanguageSpecific();

        initTextAreaDimensions(graphics);
        init(engine, graphics);
    }

    protected void initLanguageSpecific() {
        //if (GO_TO_PREVIOUS_MENU != null) {
        if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
            titleName = "- АВТОРЫ -";
        } else {
            titleName = "- CREDITS -";
        }
        //}
    }

    private void initTextAreaDimensions(PGraphics graphics) {

    }


    @Override
    protected void init(PApplet engine, PGraphics graphics) {
        super.init(engine, graphics);
        if (actualPage<0 ) actualPage = 0;
        loadAuthorsList();
        initGraphicZoneData();
        calculateStringsOnFrameNmber();
        pages = PApplet.ceil((float)fullAuthorsList.size()/AUTHORS_PER_PAGE);
        System.out.println("Authors: " + fullAuthorsList.size() + "; pages: " + pages);
        addPrevNextButtons(engine);
    }

    private void initGraphicZoneData() {
        //loadActualText(engine);
        //float zoneHeight = graphics.height/6;
        float widthCoef = 0.8f;
        Rectangular rectangular = new Rectangular(graphics.width/2, frame.getLeftUpperCorner().y+frame.getHeight()/2, frame.getWidth()*widthCoef, frame.getHeight()*widthCoef);
        //System.out.println("Strings " + authorsList.size());
        textInSimpleFrameDrawingController = new TextInSimpleFrameDrawingController( onActualScreenAuthors, rectangular, 25, 2000, graphics.textFont, graphics, true);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*4f);
    }


    private void calculateStringsOnFrameNmber() {
        int step = guiElements.get(guiElements.size()-1).getHeight();
        int area = frame.getHeight();
        int number = PApplet.floor((area-6*step)/step);
        //stringsOnFrameNumber = number;
        //System.out.println("On page: " + levelsOnPage + ". levels. area: " + area +". Step "+ step);
        if (actualPage != 0){
            actualPage = 0;
        }
    }

    private void setNewText() {
        loadAuthorsList();
        textInSimpleFrameDrawingController.setNewText(onActualScreenAuthors, true);
        textInSimpleFrameDrawingController.setAppearingVelocity(textInSimpleFrameDrawingController.getAppearingVelocity()*4f);
        System.out.println("New text was added for page: " + actualPage + ". There are " + pages + " pages");
    }

    private void loadAuthorsList() {
        fullAuthorsList = new ArrayList<>();
        fillList();

        onActualScreenAuthors = " ";

        int startPage = actualPage*AUTHORS_PER_PAGE;
        int endPageString = (actualPage+1)*AUTHORS_PER_PAGE;
        if (endPageString > fullAuthorsList.size()) endPageString = fullAuthorsList.size();

        for (int i = startPage; i < endPageString; i++){
            onActualScreenAuthors+=" ";
            onActualScreenAuthors+= fullAuthorsList.get(i);
        }
        System.out.println("String to be shown: " + onActualScreenAuthors);
    }

    private void fillList() {

        if (Program.LANGUAGE == Program.RUSSIAN){
            fullAuthorsList.add("ЭТА ИГРА ЯВЛЯЕТСЯ ПЕРВОЙ РЕАЛИЗАЦИЕЙ НАШЕГО НОВОГО ИГРОВОГО ДВИЖКА. RPG ENGINE (RETRO PLATFORMER GAME ENGINE) ЭТО НАБОР ИНСТРУМЕНТОВ ДЛЯ РАЗРАБОТКИ ДВУМЕРНЫХ ПЛАТФОРМЕРОВ В РЕТРО СТИЛЕ С СОВРЕМЕННЫМ ГЕЙМПЛЕЕМ. " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("ДЛЯ СОЗДАНИЯ ЭТОЙ ИГРЫ МЫ ИСПОЛЬЗОВАЛИ НАШИ СОБСТВЕННЫЕ РАЗРАБОТКИ, В ТОМ ЧИСЛЕ ИГРОВОЙ ДВИЖОК, РЕДАКТОР УРОВНЕЙ И ДРУГИЕ ТЕХНОЛОГИИ НАШИХ ИНЖЕНЕРОВ. " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("СЛЕДУЮЩИЕ ЛЮДИ И КОМПАНИИ ПОУЧАВСТВОВАЛИ В РАЗРАБОТКЕ ЭТОЙ ИГРЫ. НАЖМИ КНОПКУ ДАЛЕЕ ЧТОБЫ ПЕРЕЙТИ К ПЕРЕЧНЮ АВТОРОВ.  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("ДИЗАЙН ИГРЫ И УРОВНЕЙ:  " + " MGDS STUDIO " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("ЗВУКОВОЕ И МУЗЫКАЛЬНОЕ СОПРОВОЖДЕНИЕ:  " + " SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + ". Музыкальное сопровождение уровней: Spring Spring Cover Art: Lura Harenose" + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("КЛЮЧЕВАЯ ГРАФИКА И АНИМАЦИЯ:  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " MGDS STUDIO И SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " НЕКОТОРЫЕ ЭЛЕМЕНТЫ ГРАФИКИ БЫЛИ СОЗДАНЫ С ПОМОЩЬЮ РАБОТ СЛЕДУЮЩИХ ХУДОЖНИКОВ: " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("ГРАФИКА ПЕРВОГО УРОВНЯ ОСНОВАНА НА ТАЙЛСЕТЕ ОТ Lars Doucet, Sean Choate, И Megan Bednarz ( super miyamoto tileset) " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("ОБРАЗ ГЛАВНОГО ГЕРОЯ И ЧАСТЬ АНИМАЦИЙ ОСНОВАНА НА ПЕРСОНАЖЕ SPRING/ Spring Enterprises  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("НЕКОТОРЫЕ ЭЛЕМЕНТЫ ГРАФИКИ БЫЛИ ВЫПОЛНИЛИ: Jared Polidario, Lanea Zimmerman, Summer Thaxton и Hannah Cohan");

            fullAuthorsList.add("В КАЧЕСТВЕ ПАСХАЛОК МЫ ИСПОЛЬЗОВАЛИ ПЕРСОНАЖЕЙ DEATH CAP, СОЗДАННОГО Kelvin Shadewing. www.kelvinshadewing.net И SNAPPA KAPPA ВЗЯТОГО ИЗ ИГРЫ https://evilartbunny.itch.io/ spirit-of-the-wind");

        }
        else {
            fullAuthorsList.add("This video game is the first implementation of our game engine. RPG ENGINE (RETRO PLATFORMER GAME ENGINE) IS OUR CREATION TOOL THAT HELPS US TO CREATE 2D PLATFORMERS WITH MODERN GAMEPLAY. " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("TO CREATE THIS GAME WE USED OUR GAME ENGINE, OUR LEVELS EDITOR AND OTHER TECHNOLOGIES THAT WAS DEVELOPED BY OUR ENGINEERS " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("THE NEXT AUTHORS AND COMPANIES WERE PARTICIPATED IN THE DEVELOPMENT PROCESS. PRESS NEXT TO READ THE AUTHORS LIST  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("GAME AND LEVEL DESIGN:  " + " MGDS STUDIO " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("SOUND AND MUSIC:  " + " SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + ". Some soundtracks of the game levels: Spring Spring Cover Art: Lura Harenose" + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("MAIN GRAPHIC AND ANIMATION:  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " MGDS STUDIO AND SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + "SOME ELEMENTS OF THE GAME GRAPHIC WAS CREATED WITH HELP FROM THE NEXT PEOPLES: " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("FIRST LEVEL GRAPHIC IS BASED ON A SOME TILESET FROM Lars Doucet, Sean Choate, and Megan Bednarz ( super miyamoto tileset) " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("MAIN CHARACTER AND SOME OF HER ANIMATIONS ARE BASED ON A CHARACTER WAS CREATED BY SPRING/ Spring Enterprises  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("SOME GRAPHIC WAS CREATED BY Jared Polidario, Lanea Zimmerman, Summer Thaxton and Hannah Cohan");

            fullAuthorsList.add("AS EASTER EGGs WE HAVE USED DEATH CAP CHARACTER CREATED BY Kelvin Shadewing. www.kelvinshadewing.net AND SNAPPA KAPPA FROM THE GAME BY THE NEXT LINK https://evilartbunny.itch.io/ spirit-of-the-wind");
            /*
            fullAuthorsList.add("This video game is the first implementation of our game engine. RPG ENGINE (RETRO PLATFORMER GAME ENGINE) IS OUR CREATION TOOL THAT HELPS US TO CREATE 2D PLATFORMERS WITH MODERN GAMEPLAY. " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("TO CREATE THIS GAME WE USED OUR GAME ENGINE, OUR LEVELS EDITOR AND OTHER TECHNOLOGIES THAT WAS DEVELOPED BY OUR ENGINEERS " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("THE NEXT AUTHORS AND COMPANIES WERE PARTICIPATED IN THE DEVELOPMENT PROCESS. PRESS NEXT TO READ THE LIST  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("GAME AND LEVEL DESIGN:  " + " MGDS STUDIO " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("SOUND AND MUSIC:  " + " SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + ". Some soundtracks of the game levels: Spring Spring Cover Art: Lura Harenose" + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("MAIN GRAPHIC AND ANIMATION:  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " MGDS STUDIO AND SPRING/ Spring Enterprises " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + "SOME PARTS OF THE GAME GRAPHIC WAS CREATED WITH HELP FROM THE NEXT PEOPLES: " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);

            fullAuthorsList.add("FIRST LEVEL GRAPHIC IS BASED ON A SOME TILESET FROM Lars Doucet, Sean Choate, and Megan Bednarz ( super miyamoto tileset) " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("MAIN CHARACTER AND SOME OF HER ANIMATIONS ARE BASED ON A CHARACTER WAS CREATED BY SPRING/ Spring Enterprises  " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
            fullAuthorsList.add("AS EASTER EGGs WE HAVE USED DEATH CAP CHARACTER CREATED BY Kelvin Shadewing. www.kelvinshadewing.net AND SNAPPA KAPPA FROM THE GAME BY THE NEXT LINK https://evilartbunny.itch.io/ spirit-of-the-wind");

             */

        }



    }

    /*
    private void fillActualSide(PApplet engine) {
        int buttonsNumber = firstAuthorsList.size();
        String [] names;
        if (buttonsNumber <= stringsOnFrameNumber){
            moreThanOne = false;
            actualPage = 0;
            names = new String[firstAuthorsList.size()];
            for (int i = 0; i < names.length; i++){
                names[i]= firstAuthorsList.get(i);
            }
        }
        else
        {
            moreThanOne = true;
            if (actualPage > (PApplet.ceil(firstAuthorsList.size()/stringsOnFrameNumber))){
                actualPage = PApplet.floor(firstAuthorsList.size()/stringsOnFrameNumber);
            }
            int firstNumber = stringsOnFrameNumber*actualPage;
            int lastNumber = stringsOnFrameNumber*(actualPage+1);
            if (lastNumber > firstAuthorsList.size()){
                lastNumber = firstAuthorsList.size()-1;
            }
            names = new String[lastNumber-firstNumber];
            for (int i = 0; i < (lastNumber-firstNumber); i++){
                names[i]= firstAuthorsList.get(firstNumber+i);
            }
        }
        startY = (int)(frame.getLeftUpperCorner().y+guiElements.get(0).getHeight()*2f);
        fillGuiWithTextLabels(engine, startY, names, TO_DOWN);
    }
*/

    public void update(GameMenusController gameMenusController, int mouseX , int mouseY){
        super.update(gameMenusController, mouseX, mouseY);
        textInSimpleFrameDrawingController.update();
        for (NES_GuiElement element : guiElements){
            if (element.getActualStatement() == NES_GuiElement.RELEASED) {
                if (element.getName() == GO_TO_PREVIOUS_MENU) {
                    gameMenusController.setNewMenu(MenuType.OPTIONS);
                }
            }
        }
    }

    //private void

    /*
     @Override
    protected void setPrevPage() {
        //removeLevels();
        removePrevAndNextButtons();
        fillActualSide(engine);
        addPrevNextButtons(engine);
        //ArrayList <NES_GuiElement> elementsToBeAligned = getElementsToBeAligned();
        alignButtons(elementsToBeAligned );
    }
     */

    @Override
    protected void setPrevPage() {
        removePrevAndNextButtons();
        setNewText();
        //fillActualSide(engine);
        addPrevNextButtons(engine);
    }

    @Override
    protected void setNextPage() {
        removePrevAndNextButtons();
        setNewText();
        //fillActualSide(engine);
        addPrevNextButtons(engine);

    }



    private ArrayList<NES_GuiElement> getElementsToBeAligned() {
        ArrayList<NES_GuiElement> elementsToBeAligned = new ArrayList<>();
        for (NES_GuiElement element : guiElements){
            if (element.getClass() == NES_ButtonWithCursor.class){
                if (element.getName() != GO_TO_PREVIOUS_MENU && element.getName() != PREV && element.getName() != NEXT){
                    elementsToBeAligned.add(element);
                }
                else System.out.println("This element " + element.getName() + " with class " + element.getClass() + "  will not be added to te aligning list");
            }
        }
        System.out.println("Elements: " + guiElements.size() + " to be aligned only " + elementsToBeAligned.size());
        return elementsToBeAligned;
    }



    private void removeLevels() {
        for (int  i = guiElements.size()-1; i >= 0; i--){
            if (guiElements.get(i).getClass() == NES_ButtonWithCursor.class){
                if (guiElements.get(i).getName() != GO_TO_PREVIOUS_MENU){
                    guiElements.remove(guiElements.get(i));
                }
            }
        }
    }

    @Override
    public void draw(PGraphics graphics){
        super.draw(graphics);
        textInSimpleFrameDrawingController.draw(graphics);
    }


    class CreditsList{
        private ArrayList <String> data;
        private int listNumber;
        
        CreditsList(int language){
            if (language == Program.RUSSIAN){
                initRussian(0);
            }
            else initEnglish(0);
        }

        void updateTextForPage(int page){

        }

        private void initEnglish(int page) {
            if (page == 0){
                fullAuthorsList.add("This game " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Lars Doucet, Sean Choate, and Megan Bednarz for super miyamoto tileset " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - character design and animation of main character " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - sound " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - audio " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Composer: Spring Spring Cover Art: Lura Harenose " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Netgfx - raven graphic " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Kelvin Shadewing - death cap graphic. www.kelvinshadewing.net");
            }
            else if (page == 0){
                fullAuthorsList.add("Master484 - thanks for the flowers' body " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Lars Doucet, Sean Choate, and Megan Bednarz for super miyamoto tileset " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - character design and animation of main character " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - sound " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - audio " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Composer: Spring Spring Cover Art: Lura Harenose " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Netgfx - raven graphic " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Kelvin Shadewing - death cap graphic. www.kelvinshadewing.net");
            }
        }

        private void initRussian(int page) {
            if (page == 0){
                fullAuthorsList.add("Master484 - thanks for the flowers' body " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Lars Doucet, Sean Choate, and Megan Bednarz for super miyamoto tileset " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - character design and animation of main character " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - sound " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Spring/Spring Enterprise - audio " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Composer: Spring Spring Cover Art: Lura Harenose " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Netgfx - raven graphic " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR + " " + TextInSimpleFrameDrawingController.CARRIAGE_RETURN_CHAR);
                fullAuthorsList.add("Kelvin Shadewing - death cap graphic. www.kelvinshadewing.net");
            }
        }

        public ArrayList<String> getData() {
            return data;
        }
    }

}
