package br.odb.giovanni.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Tile extends br.odb.giovanni.engine.Bitmap {
    private int type;

    public Tile(Resources resources, int resId) {
        super(resources, resId);

        type = resId;
    }

    public Tile(int baseTypeId, Bitmap androidBitmap) {
        super(androidBitmap);

        type = baseTypeId;
    }

    public int getType() {
        return type;
    }

    public void setTile(int type, android.graphics.Bitmap tile) {
        super.setAndroidBitmap(tile);
        this.type = type;
    }
}
