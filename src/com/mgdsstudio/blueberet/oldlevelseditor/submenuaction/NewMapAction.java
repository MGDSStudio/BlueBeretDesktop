package com.mgdsstudio.blueberet.oldlevelseditor.submenuaction;

import com.mgdsstudio.blueberet.gamelibraries.StringLibrary;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.oldlevelseditor.*;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_Button;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_Element;
import com.mgdsstudio.blueberet.gameprocess.gui.androidGUI_ScrollableTab;
import com.mgdsstudio.blueberet.gameprocess.gui.androidAndroidGUI_TextField;
import com.mgdsstudio.blueberet.androidspecific.AndroidSpecificFileManagement;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public class NewMapAction extends SubmenuAction{
    private final static int NAME_ENTER = 1;
    private final static int CREATING = 2;
    private final static int END = 100;
    private int newLevelNumber = -1;
    private String newLevelName;

    public NewMapAction(MapZone mapZone, GameObjectDataForStoreInEditor objectData) {
        super(mapZone, objectData);
        Editor2D.localStatement = NAME_ENTER;
        newLevelName = "";
    }


    public void reconstructTab(androidGUI_ScrollableTab tab, byte globalStatement, byte localStatement) {
        if (localStatement <= NAME_ENTER) {
            createMenuWithTextArea(tab, "Enter level name");
        }
        else {
            tab.clearElements();
        }
    }

    @Override
    public void updateTextForConsole(OnScreenConsole onScreenConsole, LevelsEditorControl levelsEditorControl) {
        if (onScreenConsole.canBeTextChanged()) {
            if (Editor2D.localStatement <= NAME_ENTER){
                onScreenConsole.setText("Set the name for your level");
            }
            else if ((Editor2D.localStatement == CREATING)) {
                onScreenConsole.setText("New level creating");
            }
        }
    }

    protected void updateTabController(GameObjectDataForStoreInEditor objectData, LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController){
        if (Editor2D.canBeNextOperationMade()) {
            //ArrayList<GUI_Element> releasedElements = tabController.getReleasedElements();
            //ArrayList<GUI_Element> pressedElements = tabController.getReleasedElements();
            tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess);
            //tabUpdating(objectData, tabController, tabController.getTab(), levelsEditorProcess, releasedElements);
        }
    }

    private void updateTextEnter(androidGUI_ScrollableTab tab){
        ArrayList <androidGUI_Element> androidGui_elements = tab.getElements();
        for (androidGUI_Element androidGui_element : androidGui_elements){
            if (androidGui_element.getClass() == androidAndroidGUI_TextField.class){
                androidAndroidGUI_TextField textField = (androidAndroidGUI_TextField) androidGui_element;
                String textValue = textField.getTextValue();
                if (newLevelName != textValue) {
                    newLevelName = textValue;
                }
                //System.out.println("Text: " + newLevelName);
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess) {
        if (Editor2D.localStatement <= NAME_ENTER) {
            updateTextEnter(tab);
            if (newLevelName.length()<2) {

            }
            else {
                deblockApplyButton(tab);
                updateApplyPressing(tabController);
            }
        }
    }

    private void updateApplyPressing(ScrollableTabController tabController) {
        ArrayList <androidGUI_Element> releasedElements = tabController.getReleasedElements();
        for (androidGUI_Element releasedElement : releasedElements) {
            if (releasedElement.getClass() == androidAndroidGUI_Button.class) {
                newLevelNumber = getNextLevelNumber();
                objectData.setClassName("");
                System.out.println("New level number: " + newLevelNumber);
                Editor2D.setNewLocalStatement((byte) CREATING);
            }
            else if (releasedElement.getClass() == androidAndroidGUI_TextField.class){

            }
        }
    }

    private void deblockApplyButton(androidGUI_ScrollableTab tab) {
        ArrayList <androidGUI_Element> androidGui_elements = tab.getElements();
        for (androidGUI_Element androidGui_element : androidGui_elements){
            if (androidGui_element.getClass() == androidAndroidGUI_Button.class){
                if (androidGui_element.getStatement() == androidGUI_Element.HIDDEN || androidGui_element.getStatement() == androidGUI_Element.BLOCKED){
                    androidGui_element.setStatement(androidGUI_Element.ACTIVE);
                }
            }
        }
    }

    protected void tabUpdating(GameObjectDataForStoreInEditor objectData, ScrollableTabController tabController, androidGUI_ScrollableTab tab, LevelsEditorProcess levelsEditorProcess, ArrayList<androidGUI_Element> releasedElements) {
        if (releasedElements.size()>0) {
            if (Editor2D.localStatement == NAME_ENTER) {
                for (androidGUI_Element releasedElement : releasedElements) {
                    if (releasedElement.getClass() == androidAndroidGUI_Button.class) {
                        //LevelsListCreator levelsListCreator = new LevelsListCreator(ExternalRoundDataFileController.USER_LEVELS);
                        //String pathToFile = levelsListCreator.getLevelPathByLevelName(releasedElement.getName());
                        newLevelNumber = getNextLevelNumber();
                        objectData.setClassName("");
                        System.out.println("New level number: " + newLevelNumber);
                        Editor2D.setNewLocalStatement((byte) CREATING);
                    }
                    else if (releasedElement.getClass() == androidAndroidGUI_TextField.class){

                    }
                }
            }
        }

    }

    private int getNextLevelNumber() {
        int minNumber = -3;
        if (Program.OS == Program.DESKTOP){
            try {
                ArrayList<String> files = StringLibrary.getFilesListInAssetsFolder();
                    for (int i = 0; i < files.size(); i++) {
                        int levelNumber = StringLibrary.getLevelNumberFromName(files.get(i));
                        System.out.println("File has name: " + files.get(i) + " and level number " + levelNumber);
                        if (levelNumber < minNumber && levelNumber != 0) {
                            minNumber = levelNumber;
                            System.out.println("File " + files.get(i) + " is the smallest");
                        }
                    }

            }
            catch (Exception e){
                System.out.println("Can not get files list for this operating system");
                e.printStackTrace();
            }

        }
        else {
            ArrayList <String> files = StringLibrary.getFilesListInCache();
            for (int i = 0; i < files.size(); i++){
                try {
                    int levelNumber = StringLibrary.getLevelNumberFromName(files.get(i));
                    if (levelNumber < minNumber) {
                        minNumber = levelNumber;
                    }
                }
                catch (Exception e){
                    System.out.println("File " + files.get(i) + " is not a level");
                }
            }
        }
        minNumber--;
        return minNumber;

    }

    private void createClearLevel(LevelsEditorProcess levelsEditorProcess) {
        //File newLevelTemplate = null;
        final String TEMPLATE_NAME = "ClearTemplate.txt";
        String pathToTemplate  = Program.getRelativePathToAssetsFolder()+TEMPLATE_NAME;;
        String newLevelname;
        if (Program.OS == Program.DESKTOP){
            //pathToTemplate = Program.getRelativePathToAssetsFolder()+TEMPLATE_NAME;
            newLevelname = Program.getRelativePathToAssetsFolder()+Program.USER_LEVELS_PREFIX+newLevelNumber+Program.USER_LEVELS_EXTENSION;
        }
        else {
            //pathToTemplate = AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+TEMPLATE_NAME;
            newLevelname =AndroidSpecificFileManagement.getPathToCacheFilesInAndroid()+Program.USER_LEVELS_PREFIX+newLevelNumber+Program.USER_LEVELS_EXTENSION;
        }
        System.out.println("Path to template file: " + pathToTemplate);

        String [] templateData = createNewLevelData(pathToTemplate);

        System.out.println("Path to new file: " + newLevelname);
        //File newLevel = new File(Program.USER_LEVELS_PREFIX+newLevelNumber+Program.USER_LEVELS_EXTENSION);
        Program.engine.saveStrings(newLevelname, templateData);

    }

    private String [] createNewLevelData(String pathToTemplate){
        String [] templateData = Program.engine.loadStrings(pathToTemplate);
        for (int i = 0; i < templateData.length; i++){
            if (templateData[i].contains("/")){
                String originalName = templateData[i].substring(2);
                System.out.println("Original name: " + originalName);
                templateData[i] = "//" + newLevelName +" ";
                System.out.println("New name: " + templateData[i]);
                break;
            }
        }
        return templateData;
    }



    @Override
    public void update(LevelsEditorProcess levelsEditorProcess, ScrollableTabController tabController, GameObjectDataForStoreInEditor objectData){
        if (Editor2D.localStatement <= NAME_ENTER){
            updateTabController(objectData, levelsEditorProcess, tabController);
        }
        else if (Editor2D.localStatement == CREATING){
            createClearLevel(levelsEditorProcess);
            Editor2D.setNewLocalStatement((byte) END);
        }
        if (Editor2D.localStatement == END){
            Editor2D.setNewLocalStatement((byte)0);
            if (!Editor2D.isLevelChanged()) {
                Editor2D.changeLevelNumber(newLevelNumber);
                makePauseToNextOperation();

                System.out.println("Level number was changed to " + newLevelNumber);
            }
        }
    }



    @Override
    public byte getEndValue() {
        return END;
    }

    private void createMenuWithTextArea(androidGUI_ScrollableTab tab, String name) {
        tab.clearElements();
        androidGUI_Element textField = new androidAndroidGUI_TextField(new Vec2(((tab.getWidth() / 2)), Program.engine.width/3.918f), (int) (androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR * 1.3f), androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, name);


        tab.addGUI_Element(textField, null);
        androidGUI_Element buttonCreate = new androidAndroidGUI_Button(new Vec2(((tab.getWidth() / 4)), Program.engine.width/2.467f), androidAndroidGUI_Button.NORMAL_WIDTH_IN_REDACTOR / 2, androidAndroidGUI_Button.NORMAL_HEIGHT_IN_REDACTOR, APPLY, false);
        tab.addGUI_Element(buttonCreate, null);
        buttonCreate.setStatement(androidGUI_Element.BLOCKED);
        //buttonCreate.setStatement(GUI_Element.BLOCKED);
        repositionGUIAlongY(tab, tab.getElements());
        tab.recalculateHeight(tab.getElements());
        System.out.println("levels number: " + tab.getElements().size());
    }
}
