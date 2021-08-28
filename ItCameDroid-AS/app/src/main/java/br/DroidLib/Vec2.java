package br.DroidLib;

public class Vec2 {
    public final float x;
    public final float y;

    public Vec2(float i, float j) {
        this.x = i;
        this.y = j;
    }

    public boolean isCloseEnoughToConsiderEqualTo(Vec2 position) {
        return false;
    }
}
