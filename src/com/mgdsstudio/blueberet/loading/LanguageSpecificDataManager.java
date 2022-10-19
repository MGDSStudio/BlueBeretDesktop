package com.mgdsstudio.blueberet.loading;

import com.mgdsstudio.blueberet.mainpackage.Program;

import java.util.Locale;

public class LanguageSpecificDataManager {
    private String language;
    private boolean withAdoptedLanguage = true;

    public LanguageSpecificDataManager() {
        if (withAdoptedLanguage) init();
    }

    private void init(){
        String language = "en";
        String actualLanguage = Locale.getDefault().getLanguage();
        //if (Program.OS == Program.ANDROID){
            if (isRussian(actualLanguage) && Program.OS == Program.ANDROID){
                System.out.println("This user has a russian interface: " + actualLanguage);
                Program.LANGUAGE = Program.RUSSIAN;
            }
            else {
                System.out.println("This user has an interface "  + actualLanguage);
                Program.LANGUAGE = Program.ENGLISH;
                actualLanguage = language;
            }
            this.language = actualLanguage;

        //}

    }

    public String getLanguage() {
        return language;
    }

    private boolean isRussian(String lang){
        if (lang == "RU" || lang == "ru"){
            return true;
        }
        else return false;
    }

    public boolean isRussian(){
        return  (isRussian(language));
    }
}
