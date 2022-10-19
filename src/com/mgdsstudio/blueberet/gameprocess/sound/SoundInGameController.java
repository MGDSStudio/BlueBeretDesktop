package com.mgdsstudio.blueberet.gameprocess.sound;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.*;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.weapon.WeaponType;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.*;

public class SoundInGameController extends AbstractSoundController implements SoundsInGame {


    private int previousUpdatingTime;
    private boolean lastStatement;
    private final static boolean LOCKED = true;
    private  int lastTime;
    private Timer timerBeforeFirstLaunch;
    private final static int TIMER_BEFORE_FIRST_LAUNCH = 700;
    private boolean started;
    private boolean noMoreAudios;

    public SoundInGameController(int lines){
        super(lines, "Sounds.wav");
    }

    @Override
    protected void initTrackData() {
        data.add(new TrackData(0f,1f, TrackData.NORMAL_AUDIO, COIN_ACHIVED));
        data.add( new TrackData(1.2f,1.73f, TrackData.NORMAL_AUDIO, ELEMENT_CRUSHED_2));
        data.add(new TrackData(1.73f,2.37f, TrackData.NORMAL_AUDIO, ELEMENT_CRUSHED_1));
        data.add( new TrackData(2.37f,2.8f, TrackData.NORMAL_AUDIO, EXPLOSION_2));
        data.add(new TrackData(2.8f,3.7f, TrackData.NORMAL_AUDIO, EXPLOSION_3));
        data.add( new TrackData(2.8f,3.7f, TrackData.MAX_AUDIO, SHOTGUN_SHOT));
        data.add(new TrackData(4.5f,4.9f, TrackData.NORMAL_AUDIO, HANDGUN_SHOT));
        data.add(new TrackData(4.912f,5.3f, TrackData.NORMAL_AUDIO, KICK));
        data.add(new TrackData(5.43f,6.25f, TrackData.NORMAL_AUDIO, M79_SHOT));
        data.add(new TrackData(6.25f,7.03f, TrackData.NORMAL_AUDIO, MEDICAL_KIT_EATEN));
        data.add(new TrackData(7f,7.5f, TrackData.NORMAL_AUDIO, MENU_APPROVED));
        data.add(new TrackData(7.94f,8.1f, TrackData.NORMAL_AUDIO, MENU_SELECTED));
        data.add(new TrackData(8.15f,8.95f, TrackData.NORMAL_AUDIO, OBJECT_ACHIVED));
        data.add(new TrackData(8.95f,10.0f, TrackData.NORMAL_AUDIO, PLAYER_DEAD));
        data.add(new TrackData(10.0f,10.4f, TrackData.NORMAL_AUDIO, WEAPON_FRAME_OPENING));
        data.add(new TrackData(10.45f,10.78f, TrackData.NORMAL_AUDIO, WEAPON_SELECTED));
        data.add(new TrackData(10.78f,11.15f, TrackData.NORMAL_AUDIO, AMMO_GOT));
        data.add(new TrackData(11.15f,11.75f, TrackData.NORMAL_AUDIO, EXPLOSION_4));
        data.add(new TrackData(11.75f,12.25f, TrackData.NORMAL_AUDIO, HANDGUN_SHOT_2));
        data.add(new TrackData(12.25f,12.7f, TrackData.NORMAL_AUDIO, JUMP_ENDS));
        data.add(new TrackData(12.7f,16.25f, TrackData.NORMAL_AUDIO, JUMP_IN_PORTAL));
        data.add(new TrackData(16.3f,16.7f, TrackData.NORMAL_AUDIO, JUMP_START));
        data.add(new TrackData(16.7f,17.3f, TrackData.NORMAL_AUDIO, MEDICAL_KIT_GOT));
        data.add(new TrackData(17.3f,17.55f, TrackData.NORMAL_AUDIO, MONEY_GOT));
        data.add(new TrackData(17.58f,18.6f, TrackData.NORMAL_AUDIO, M79_SHOT));
        data.add(new TrackData(18.8f,19.3f, TrackData.NORMAL_AUDIO, SMG_SHOT));
        data.add(new TrackData(19.3f,19.55f, TrackData.NORMAL_AUDIO, ENEMY_HITTED));
        data.add(new TrackData(19.55f,20.0f, TrackData.NORMAL_AUDIO, ENEMY_HITTED_2));
        data.add(new TrackData(20.0f,21.7f, TrackData.NORMAL_AUDIO, END_LEVEL_ZONE_ACHIVED));
        data.add(new TrackData(22.0f,22.25f, TrackData.NORMAL_AUDIO, SOME_SOUND_1));
        data.add(new TrackData(22.55f,22.65f, TrackData.MAX_AUDIO, SOME_SOUND_2));
        data.add(new TrackData(23.05f,24.05f, TrackData.NORMAL_AUDIO, POWER_UP));
        data.add(new TrackData(24.5f,24.85f, TrackData.NORMAL_AUDIO, RELOAD_COMPLETED));
        data.add(new TrackData(25.0f,25.9f, TrackData.NORMAL_AUDIO, BULLET_TIME_STARTED));
        data.add(new TrackData(26.0f, 26.25f, TrackData.NORMAL_AUDIO, ENEMY_HITTED_3));
        data.add(new TrackData(26.5f, 28.7f, TrackData.NORMAL_AUDIO, FALL_IN_WATER));
        data.add(new TrackData(29.12f, 30.48f, TrackData.NORMAL_AUDIO, IMPORTANT_ITEM_GAINED));
        data.add(new TrackData(30.7f, 32.86f, TrackData.NORMAL_AUDIO, LOOSED));
        data.add(new TrackData(33.1f, 34.93f, TrackData.NORMAL_AUDIO, TELEPHONE));
        data.add(new TrackData(35.07f, 35.183f, TrackData.MAX_AUDIO, WALL_HITTED));
        data.add(new TrackData(35.35f, 35.6f, TrackData.QUARTER_AUDIO, STEP_1));
        data.add(new TrackData(35.79f, 36.08f, TrackData.MAX_AUDIO, MAGAZINE_TO_WEAPON));
        data.add(new TrackData(36.08f, 38.15f, TrackData.QUARTER_AUDIO, BOSS_INTRO));
    }



    @Override
    protected boolean canBeSoundAdded(){
        if (started) return true;
        else {
            if (timerBeforeFirstLaunch == null) {
                timerBeforeFirstLaunch = new Timer(TIMER_BEFORE_FIRST_LAUNCH);
                return false;
            } else if (timerBeforeFirstLaunch.isTime()) {
                started = true;
                if (Program.debug ) System.out.println("Sound started at  "+ Program.engine.millis()/1000);
                return true;
            }
            else return false;
        }
    }
    public void setAndPlayAudioForShot(WeaponType weaponType){
        if (canBeSoundAdded()) {
            if (!noMoreAudios) {
                if (weaponType == WeaponType.SHOTGUN || weaponType == WeaponType.SAWED_OFF_SHOTGUN)
                    setAndPlayAudio(SHOTGUN_SHOT);
                else if (weaponType == WeaponType.HANDGUN || weaponType == WeaponType.REVOLVER)
                    setAndPlayAudio(HANDGUN_SHOT);
                else if (weaponType == WeaponType.SMG) setAndPlayAudio(SMG_SHOT);
                else if (weaponType == WeaponType.M79) setAndPlayAudio(M79_SHOT);
            }
        }
    }


    public void setAndPlayAudioForCollectedElement(AbstractCollectable abstractCollectable) {
        if (canBeSoundAdded()) {
            if (!noMoreAudios) {
                if (abstractCollectable.getClass() == WeaponMagazine.class) setAndPlayAudio(AMMO_GOT);
                else if (abstractCollectable.getClass() == Money.class) {
                    Money money = (Money) abstractCollectable;
                    if (money.isCoin()) setAndPlayAudio(COIN_ACHIVED);
                    else setAndPlayAudio(MONEY_GOT);
                } else if (abstractCollectable.getClass() == MedicalKit.class) setAndPlayAudio(MEDICAL_KIT_GOT);
                else setAndPlayAudio(OBJECT_ACHIVED);
            }
        }
    }

    public void setAndPlayAudioForCollectedElement(AbstractCollectable abstractCollectable, float amplitude) {
        if (canBeSoundAdded()) {
            if (!noMoreAudios) {
                if (abstractCollectable.getClass() == WeaponMagazine.class) setAndPlayAudio(AMMO_GOT);
                else if (abstractCollectable.getClass() == Money.class) {
                    Money money = (Money) abstractCollectable;
                    if (money.isCoin()) setAndPlayAudio(COIN_ACHIVED);
                    else setAndPlayAudio(MONEY_GOT);
                } else if (abstractCollectable.getClass() == MedicalKit.class) setAndPlayAudio(MEDICAL_KIT_GOT);
                else if (abstractCollectable.getClass() == Fruit.class) setAndPlayAudio(MEDICAL_KIT_GOT);
                else {
                    setAndPlayAudio(IMPORTANT_ITEM_GAINED, amplitude);
                }
            }
        }
    }

    public void noMoreAudio(){
        noMoreAudios = true;
    }
}
