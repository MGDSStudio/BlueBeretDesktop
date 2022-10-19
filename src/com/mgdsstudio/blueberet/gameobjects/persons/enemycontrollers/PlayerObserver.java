package com.mgdsstudio.blueberet.gameobjects.persons.enemycontrollers;

import com.mgdsstudio.blueberet.gameobjects.Bullet;
import com.mgdsstudio.blueberet.gameobjects.Explosion;
import com.mgdsstudio.blueberet.gameobjects.persons.Enemy;
import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PApplet;

import java.util.ArrayList;

class PlayerObserver {
    private int updatingFrame = 2;
    private int distToReactOnRunningPlayer = -1;
    private int distToReactOnBoom = -1;

    private int distToFindPlayerAhead = -1;

    //private final Soldier player;
    private final Enemy enemy;


    public PlayerObserver(int updatingFrame, int distToReactOnRunningPlayer, int distToReactOnBoom, int distToFindPlayerAhead, Enemy enemy) {
        //can not get player from setup
        this.updatingFrame = updatingFrame;
        this.distToReactOnRunningPlayer = distToReactOnRunningPlayer;
        this.distToReactOnBoom = distToReactOnBoom;
        this.distToFindPlayerAhead = distToFindPlayerAhead;
        this.enemy = enemy;
    }

    boolean isPlayerAhead(Person player){
        return isPlayerAhead(player, distToFindPlayerAhead);
    }

    boolean isPlayerAhead(Person player, int distance){
        if (distance > 0){
            if (Program.engine.frameCount % updatingFrame == 0) {
                // System.out.println("Dist to player ahead: " + (PApplet.abs(player.getPixelPosition().x-enemy.getPixelPosition().x)) + " must abe " + distToFindPlayerAhead);
                if (PApplet.abs(player.getPixelPosition().x-enemy.getPixelPosition().x)<distance) {
                    //System.out.print("Max dist: "+ distToFindPlayerAhead +" lizard goes to " );

                    if (enemy.orientation == Person.TO_LEFT) {
                        //System.out.print(" left " );
                        if (player.getPixelPosition().x > enemy.getPixelPosition().x) {
                            float deltaX =  PApplet.abs(player.getPixelPosition().x - enemy.getPixelPosition().x);
                            float deltaY =  PApplet.abs(player.getPixelPosition().y - enemy.getPixelPosition().y);
                            //System.out.println("Player is ahead from right! " + " delta y " + PApplet.abs(player.getPixelPosition().y - enemy.getPixelPosition().y) + "; x: " + PApplet.abs(player.getPixelPosition().x - enemy.getPixelPosition().x));
                            //System.out.println(" and player is right " );
                            if (deltaX > (deltaY*3)) return true;
                        }
                        //else System.out.println(" but player is left " );
                    }
                    else {
                        //System.out.print(" right " );
                        if (player.getPixelPosition().x < enemy.getPixelPosition().x) {
                            float deltaX =  PApplet.abs(player.getPixelPosition().x - enemy.getPixelPosition().x);
                            float deltaY =  PApplet.abs(player.getPixelPosition().y - enemy.getPixelPosition().y);
                            //System.out.println("Player is ahead from right! " + " delta y " + PApplet.abs(player.getPixelPosition().y - enemy.getPixelPosition().y) + "; y: " + PApplet.abs(player.getPixelPosition().x - enemy.getPixelPosition().x));
                            //System.out.println(" and player is left " );
                            if (deltaX > (deltaY*3)) return true;
                        }
                        //else System.out.println(" but player is right " );
                    }
                }
            }
        }
        return false;
    }

    private boolean isPlayerOnDistance(int distToFindPlayer, Person player){
        if (Program.engine.frameCount % updatingFrame == 0) {
            if (player.isPersonRunning()) {
                if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, player.getPixelPosition().x, player.getPixelPosition().y) < distToFindPlayer){
                    return true;
                }
                else return false;
            }
            else return false;
        }
        else return false;
    }

    boolean isPlayerRunningFromTheBackOnDistance(Person player){
        if (player.isPersonRunning() || player.isLanded()) {
            boolean near = isPlayerOnDistance(distToReactOnRunningPlayer, player);
            if (near) {
                if (isPlayerAhead(player, distToReactOnRunningPlayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isBoomOnDistanceFromTheBack(GameRound gameRound){
        if (Program.engine.frameCount % updatingFrame == 0) {
            ArrayList < Explosion> explosions = gameRound.explosions;
            for (Explosion explosion : explosions){
                if (!explosion.isEnded()) {
                    if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, explosion.getPosition().x, explosion.getPosition().y) < distToReactOnBoom) {
                        if (enemy.orientation == Person.TO_LEFT) {
                            if (explosion.getPosition().x > enemy.getPixelPosition().x) {
                                return true;
                            }
                        } else {
                            if (explosion.getPosition().x < enemy.getPixelPosition().x) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        else return false;
    }

    boolean isBoomOnDistance(GameRound gameRound){
        if (Program.engine.frameCount % updatingFrame == 0) {
            ArrayList < Explosion> explosions = gameRound.explosions;
            for (Explosion explosion : explosions){
                if (!explosion.isEnded()) {
                    if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, explosion.getPosition().x, explosion.getPosition().y) < distToReactOnBoom) {
                        return true;
                    }
                }
            }
            return false;
        }
        else return false;
    }


private boolean isActionAppearAtActualLoop(long moment){
    if (moment == Program.engine.frameCount){
        return true;
    }
    else if (moment >= (Program.engine.frameCount-updatingFrame) && moment < Program.engine.frameCount){
        return true;
    }
    else return false;
}

    boolean isShotOnDistance(GameRound gameRound){
        if (Program.engine.frameCount % updatingFrame == 0) {
            ArrayList <Bullet> explosions = gameRound.bullets;
            for (Bullet bullet : explosions){
                if (bullet.isActive() ) {
                    if (isActionAppearAtActualLoop(bullet.shotStartingFrame)) {
                        if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, bullet.getShotPosition().x, bullet.getShotPosition().y) < distToReactOnBoom) {

                                    return true;

                        }
                    }
                }
            }
            return false;
        }
        else return false;
    }
    boolean isShotAhead(GameRound gameRound){
        if (Program.engine.frameCount % updatingFrame == 0) {
            ArrayList <Bullet> explosions = gameRound.bullets;
            for (Bullet bullet : explosions){
                if (bullet.isActive() ) {
                    if (isActionAppearAtActualLoop(bullet.shotStartingFrame)) {
                        if (PApplet.dist(enemy.getPixelPosition().x, enemy.getPixelPosition().y, bullet.getShotPosition().x, bullet.getShotPosition().y) < distToReactOnBoom) {
                            if (enemy.orientation == Person.TO_LEFT) {
                                if (bullet.getShotPosition().x > enemy.getPixelPosition().x) {
                                    return true;
                                }
                            } else {
                                if (bullet.getShotPosition().x < enemy.getPixelPosition().x) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        else return false;
    }



}
