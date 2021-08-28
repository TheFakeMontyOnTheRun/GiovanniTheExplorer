package br.DroidLib;

import android.graphics.Point;

public class Actor {
    public boolean killed;
    Vec2 mPoint = new Vec2(0, 0);

    public Vec2 getPosition() {
        return mPoint;
    }

    public void setPosition(Vec2 vec2) {

    }

    public void tick(long timeInMS) {

    }

    public void touched(Actor b) {

    }
}
