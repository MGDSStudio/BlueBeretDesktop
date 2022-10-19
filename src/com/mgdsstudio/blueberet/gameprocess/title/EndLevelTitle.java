package com.mgdsstudio.blueberet.gameprocess.title;

public class EndLevelTitle extends SimpleTitle{
    public final static String GAME_OVER_TITLE_PATH = "GameOverScreen.png";
    public final static String WIN_TITLE_PATH = "YouWinScreen.png";

    public EndLevelTitle(String path, int timeToShow, int timeToChangeAlpha, int width, int height, byte showingMode) {
        super(path, timeToShow, timeToChangeAlpha, width, height, showingMode);
    }

    @Override
    public boolean isEnded(){
        return false;
    }

}
