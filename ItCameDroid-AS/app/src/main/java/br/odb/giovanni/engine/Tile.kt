package br.odb.giovanni.engine

import android.content.res.Resources

class Tile : Bitmap {
    var type: Int
        private set

    constructor(resources: Resources?, resId: Int) : super(resources, resId) {
        type = resId
    }

    constructor(baseTypeId: Int, androidBitmap: android.graphics.Bitmap?) : super(androidBitmap) {
        type = baseTypeId
    }

    fun setTile(type: Int, tile: android.graphics.Bitmap?) {
        super.androidBitmap = tile
        this.type = type
    }
}