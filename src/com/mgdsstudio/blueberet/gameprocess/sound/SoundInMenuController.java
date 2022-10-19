package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.menusystem.gui.NES_GuiElement;

public class SoundInMenuController extends AbstractSoundController implements SoundsInMenu{
    private int lastPlayedSound = -1;
    private NES_GuiElement lastPressedElement;

    public SoundInMenuController(int lines){
        super(lines, "Menu sounds.wav");

    }

    public void setAndPlayAudioForButtonPressed(NES_GuiElement elementWasClicked){
        //setAndPlayAudio(elementWasClicked, BUTTON_SELECTED);
        setAndPlayAudio(elementWasClicked, BUTTON_SELECTED);
        //System.out.println("Released");
    }

    public void setAndPlayAudioForButtonReleased(NES_GuiElement elementWasClicked){
        setAndPlayAudio(elementWasClicked, BUTTON_TO_MENU_PRESSED );
        //System.out.println("Pressed");
    }

    public void setAndPlayAudioForObjectBought(){
        //setAndPlayAudio(OBJECT_BOUGHT );
        //System.out.println("Pressed");
    }



    private void setAndPlayAudio(NES_GuiElement elementWasClicked, int trackNumber){
        if (lastPressedElement == null) {
            lastPressedElement = elementWasClicked;
            lastPlayedSound = trackNumber;
            setAndPlayAudio(trackNumber);
        }
        else {
            if (lastPressedElement != elementWasClicked || lastPlayedSound != trackNumber){
                setAndPlayAudio(trackNumber);
                lastPressedElement = elementWasClicked;
                lastPlayedSound = trackNumber;
            }
            else {
                //System.out.println("The sound for this gui was already player.");
            }
        }
    }

    public void setAndPlayAudioForGameStarted(){
        setAndPlayAudio(GAME_STARTED);
        if (lastPressedElement != null) lastPressedElement= null;
    }

    protected void initTrackData() {
        data.add( new TrackData(2.165f,2.57f, TrackData.MAX_AUDIO, BUTTON_SELECTED ));
        data.add(new TrackData(2.62f,2.88f, TrackData.NORMAL_AUDIO, BUTTON_TO_MENU_PRESSED));
        data.add(new TrackData(0f,2.15f, TrackData.MAX_AUDIO, GAME_STARTED));
        data.add(new TrackData(2.95f,4.4f, TrackData.MAX_AUDIO, OBJECT_BOUGHT));
    }

    @Override
    public void stopAllAudio(){
        super.stopAllAudio();
        if (lastPressedElement != null) lastPressedElement= null;
    }
}
