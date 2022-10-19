package com.mgdsstudio.blueberet.gameobjects;

import java.util.ArrayList;

public class BulletsPool {
    private ArrayList<Bullet> bullets;
    private int lastFreeObjectNumber;
    private boolean clear;


    /*
    public BulletsPool (int dimension){
        bullets = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++){
            Bullet bullet = new Bullet();
            bullets.add(bullet);
        }
        lastFreeObjectNumber = dimension-1;
    }*/

    /*public Bullet getObject(){
        Bullet bullet = bullets.get(lastFreeObjectNumber);
        bullets.remove(lastFreeObjectNumber);
        lastFreeObjectNumber--;
        if (lastFreeObjectNumber < 0) {
            lastFreeObjectNumber = 0;
            Bullet newBullet = new Bullet();
            bullets.add(newBullet);
            //clear = true;
        }
        return bullet;
    }*/

    public void addObject(Bullet bullet){
        lastFreeObjectNumber++;
        bullets.add(bullet);
        bullet.setActive(false);
        System.out.println("Pool size: " + bullets.size());

    }


}
