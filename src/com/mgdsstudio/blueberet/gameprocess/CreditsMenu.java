package com.mgdsstudio.blueberet.gameprocess;

import java.util.ArrayList;

public class CreditsMenu {
    //ArrayList<String> authors = new ArrayList<String>();
    private ArrayList<String> authors;

    public CreditsMenu(){
        fillGraphicData();
       authors = new ArrayList<>();
       authors.add("Master484 https://opengameart.org/users/master484 - thanks for the flowers' body");
       authors.add("DanSevenStar.xyz - thanks for the coins");
       authors.add("Lars Doucet, Sean Choate, and Megan Bednarz for super miyamoto tileset");
       authors.add("Spring/Spring Enterprise - character design and animation of main character");
       authors.add("Spring/Spring Enterprise - character design and animation of main character");
       authors.add("Spring/Spring Enterprise - character design and animation of main character");
        authors.add("Composer: Spring Spring Cover Art: Lura Harenose");
        authors.add("Netgfx - raven graphic");
        authors.add("Paulina Riva - First level graphic");
    }

    private void fillGraphicData() {

    }

    private void fillSoundsData() {
        authors.add("hosch https://hisch.itch.io - sound design");
    }
}
