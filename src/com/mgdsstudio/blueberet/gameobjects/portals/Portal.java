package com.mgdsstudio.blueberet.gameobjects.portals;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.SingleGameElement;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

public abstract class Portal extends SingleGameElement {
    private final String objectToDisplayName = "Portal";
    public final static boolean ENTER = true;
    public final static boolean EXIT = false;
    public final static boolean DISPOSABLE = true;
    public final static boolean REUSEABLE = false;
    protected boolean usingRepeateability = REUSEABLE;
    public final static boolean ENTER_TO_EXIT = true;
    public final static boolean HERE_AND_THERE = false;
    protected boolean transferDirection = ENTER_TO_EXIT;
    // activatedBy
    public static final byte BY_PLAYER = 1;
    public static final byte BY_ENEMIES = 2;
    public static final byte BY_EVERY_PERSON = 3;
    protected byte activatedBy = BY_PLAYER;

    public Flag enter, exit;

    protected void drawFlags(GameCamera gameCamera) {
        enter.draw(gameCamera);
        exit.draw(gameCamera);
    }

    public boolean isUsingRepeateability() {
        return usingRepeateability;
    }

    public Flag getEnter() {
        return enter;
    }

    public Flag getExit() {
        return exit;
    }

    public boolean mustBeOnScreenButtonShown(Person person) {
        if (person.body != null) {
            if (enter.inZone(person.getPixelPosition())) {
                return true;
            }
            else return false;
        }
        else {
            System.out.println("This person has no body. I can not test to show button Portal; Person's class:" + person.getClass());
            return false;
        }
    }

    public void update(GameRound gameRound, GameCamera gameCamera) {
        /*
            if (activatedBy == BY_PLAYER) {
                if (enter.inZone(gameRound.getPlayer().getAbsolutePosition())) {
                    playerTransfer(gameCamera, gameRound.getPlayer());
                }
            }
            else if (activatedBy == BY_EVERY_PERSON) {
                for (int i = 0; i < gameRound.getPersons().size(); i++) {
                    if (enter.inZone(gameRound.getPersons().get(i).getAbsolutePosition())) {
                        personTransfer(gameRound.getPersons().get(i));
                    }
                }
            }
            else if (activatedBy == BY_ENEMIES) {
                for (int i = 0; i < gameRound.getPersons().size(); i++) {
                    if (enter.inZone(gameRound.getPersons().get(i).getAbsolutePosition())) {
                        if (!gameRound.getPlayer().equals(gameRound.getPersons().get(i))) {
                            personTransfer(gameRound.getPersons().get(i));
                        }
                    }
                }
            }
            */

    }

    @Override
    public String getObjectToDisplayName() {
        return objectToDisplayName;
    }
}
