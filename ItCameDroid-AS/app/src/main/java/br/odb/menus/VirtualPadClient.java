package br.odb.menus;

import android.graphics.Bitmap;

public interface VirtualPadClient {
	void handleKeys(boolean[] keymap);

	Bitmap getBitmapOverlay();
}
