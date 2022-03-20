package br.odb.giovanni.engine;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bitmap {
    private android.graphics.Bitmap sprite;
    private float x;
    private float y;

    public Bitmap(Resources resources, int baseTypeId) {
        sprite = BitmapFactory.decodeResource(resources, baseTypeId);
    }

    Bitmap(android.graphics.Bitmap androidBitmap) {
        sprite = androidBitmap;
    }

    public android.graphics.Bitmap getAndroidBitmap() {
        return sprite;
    }

    void setAndroidBitmap(android.graphics.Bitmap tile) {
        sprite = tile;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(sprite, x, y, paint);
    }

    public void prepareForGC() {
        sprite = null;
    }
}
