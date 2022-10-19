package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;

public class HUD_LifeLine {
    private Timer showingTimer, blendingTimer;
    private final Person person;
    //private static StaticSprite objectsFrame;
    //private static StaticSprite lifeLine;
    private final static byte distanceFromUpperBodyLineToLifeLine = 25;
    private int distanceFromBodyCenterToLifeLine;
    private final static int NORMAL_SHOWING_TIME = 1500;
    private final static int NORMAL_BLENDING_TIME = 1000;
    private int previsiousLifeValue;
    //private boolean ended = false;

    private static final byte NOT_SHOWN = 0;
    private static final byte SHOWN = 1;
    private static final byte BLENDING = 2;
    private static final byte CAN_BE_DELETED = 3;
    private byte statement = NOT_SHOWN;
    //static private Image objectsFrame;
    //static private Image redLine;
    //private int length;
    private int actualLifeLineWidth;
    private float scaleX = 1.000f;
    private float additionalX = 0;
    private boolean notMoreUpdate = false;
    //private float scaleY = 1.0f;

    //Blending data
    final static private int basicTint = 255;
    private static int BASIC_TINT = Program.engine.color(255, basicTint);
    private int actualAlphaForTint = basicTint;
    private int actualColor = Program.engine.color(255, actualAlphaForTint);
    final static float colorChangingProFrame = (255.00f)/NORMAL_BLENDING_TIME;
    private final Vec2 mutFramePos = new Vec2(0,0);
    private final Vec2 mutLinePos = new Vec2(0,0);

    public HUD_LifeLine(Person person, int width){
        distanceFromBodyCenterToLifeLine = (int)(distanceFromUpperBodyLineToLifeLine+person.getHeight()/2);
        this.person = person;
        previsiousLifeValue = person.getLife();
    }

    private void updateLineLength(){
        if (!notMoreUpdate) {
            float life = person.getLife();
            if (life < 0) life = 0;

            float actualLifeLineWidth = PApplet.map(life, 0, person.getMaxLife(), 0, HUD_LifeLinesController.FULL_LIFE_LINE_WIDTH);
            scaleX = actualLifeLineWidth / HUD_LifeLinesController.FULL_LIFE_LINE_WIDTH;
            //System.out.println("Line scale: " + scaleX);
            //System.out.println("Line width: " + PApplet.map(person.getLife(), 0, person.getMaxLife(), 0, HUD_LifeLinesController.FULL_LIFE_LINE_WIDTH));
            additionalX = -(((HUD_LifeLinesController.FULL_LIFE_LINE_WIDTH / 2) - 3) - ((scaleX / 2) * actualLifeLineWidth));
            if (!notMoreUpdate && life == 0) notMoreUpdate = true;
        }
    }

    private void updateActualTint(){
        if (actualAlphaForTint > 0) {
            actualColor = Program.engine.color(255, actualAlphaForTint);
            actualAlphaForTint-=(colorChangingProFrame* Program.deltaTime);
        }
        else {
            if (actualAlphaForTint != 0) actualAlphaForTint = 0;
        }
    }

    private void resetTint(StaticSprite staticSprite){
        actualAlphaForTint = basicTint;
        actualColor = Program.engine.color(255, actualAlphaForTint);
        staticSprite.resetTint();
    }

    public void update(){
        if (statement!=CAN_BE_DELETED) {
            if (statement == BLENDING) updateActualTint();
            if (person.getLife() != previsiousLifeValue && !notMoreUpdate) {
                statement = SHOWN;
                previsiousLifeValue = person.getLife();
                if (showingTimer == null) showingTimer = new Timer(NORMAL_SHOWING_TIME);
                else showingTimer = new Timer(NORMAL_SHOWING_TIME);
                updateLineLength();
            }
            else {
                if (showingTimer != null) {
                    if (showingTimer.isTime() && statement == SHOWN) {
                        statement = BLENDING;
                        if (blendingTimer == null) blendingTimer = new Timer(NORMAL_BLENDING_TIME);
                        else blendingTimer.setNewTimer(NORMAL_BLENDING_TIME);
                    }
                    else if (blendingTimer!=null){
                        if (blendingTimer.isTime() && statement == BLENDING){
                            if (person.isAlive()) {
                                //System.out.println("*-* Hidden");
                                blendingTimer.stop();
                                //blendingTimer = null;
                                statement = NOT_SHOWN;
                                //resetTint();
                            }
                            else {
                                //System.out.println("*-* Deleted");
                                blendingTimer = null;
                                statement = CAN_BE_DELETED;
                                //resetTint();
                            }
                        }
                    }
                }
            }
        }
    }

    public void drawFrame(GameCamera gameCamera, Image imageFile, ImageZoneSimpleData imageZoneSimpleData){
        /*
        if (sprite!= null && statement!=CAN_BE_DELETED) {
            if (statement == BLENDING) {
                sprite.setTint(actualColor);
            } else if (statement == SHOWN) {
                if (sprite.withTint) resetTint(sprite);
            }
            if (sprite != null && (statement == SHOWN || statement == BLENDING)) {
                sprite.draw(gameCamera, new Vec2(person.getAbsolutePosition().x, person.getAbsolutePosition().y - distanceFromBodyCenterToLifeLine), 0f);
            }
        }*/
    }

    public void drawFrame(GameCamera gameCamera, StaticSprite sprite){
        if (sprite!= null && statement!=CAN_BE_DELETED) {
            if (statement == BLENDING) {
                sprite.setTint(actualColor);
            } else if (statement == SHOWN) {
                if (sprite.withTint) resetTint(sprite);
            }
            if (sprite != null && (statement == SHOWN || statement == BLENDING)) {
                mutFramePos.x = person.getPixelPosition().x;
                mutFramePos.y = person.getPixelPosition().y - distanceFromBodyCenterToLifeLine;
                sprite.draw(gameCamera, mutFramePos, 0f);
            }
        }
    }

    public void drawLine(GameCamera gameCamera, StaticSprite sprite){
        if (sprite!= null && statement!=CAN_BE_DELETED) {
            mutLinePos.x = person.getPixelPosition().x + additionalX;
            mutLinePos.y = person.getPixelPosition().y - distanceFromBodyCenterToLifeLine;
            if (statement == SHOWN) {
                if (sprite.withTint) resetTint(sprite);
                sprite.draw(gameCamera, mutLinePos, 0f, scaleX, 1);
            }
            else if (statement == BLENDING){
                sprite.setTint(actualColor);

                sprite.draw(gameCamera, mutLinePos, 0f, scaleX, 1);
            }
        }
    }


    public boolean canBeDeleted(){
        if (statement == CAN_BE_DELETED) return true;
        else return false;
    }



    public Person getPerson(){
        return person;
    }


    public byte getStatement() {
        return statement;
    }
}
