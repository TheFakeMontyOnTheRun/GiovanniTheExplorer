package br.odb.giovanni.menus;

import android.graphics.Bitmap;

public interface VirtualPadClient {
    void handleKeys(boolean[] keymap);

    Bitmap getBitmapOverlay();
}
