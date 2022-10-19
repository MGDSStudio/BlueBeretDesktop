package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.graphic.StaticSprite;
import com.mgdsstudio.blueberet.graphic.Tileset;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class AbstractGrenade extends LaunchableWhizbang {

    @Override
    public Body getBindedBody() {
        System.out.println("No bound body");
        return null;
    }

    protected void setData(float shootingAngle, float gravityScale) {
        System.out.println("Grenade angle was: " + shootingAngle);
        shootingAngle = Program.engine.radians(shootingAngle);
        System.out.println("Grenade angle is: " + shootingAngle);
        body.setTransform(body.getPosition(), -shootingAngle);
        body.setLinearVelocity(new Vec2(speed* Program.engine.cos(shootingAngle), -speed* Program.engine.sin(shootingAngle)));
        body.setGravityScale(gravityScale);
    }

    public abstract StaticSprite getSprite();

    public static void loadSprites(Tileset tilesetUnderPath){
        System.out.println("Try to load sprites for launchable " + (tilesetUnderPath==null));
        LaunchableGrenade.loadSprites(tilesetUnderPath);
        System.out.println("Try to load sprites for hand grenade ");
        HandGrenade.loadSprites(tilesetUnderPath);
    }

}
