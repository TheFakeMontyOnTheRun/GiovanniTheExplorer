package br.odb.giovanni.menus

import android.graphics.Bitmap

interface VirtualPadClient {
    fun handleKeys(keymap: BooleanArray?)
    val bitmapOverlay: Bitmap?
}